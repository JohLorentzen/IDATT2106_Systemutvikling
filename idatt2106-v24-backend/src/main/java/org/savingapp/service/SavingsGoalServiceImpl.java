package org.savingapp.service;

import lombok.AllArgsConstructor;
import org.savingapp.dto.SavingsGoalDTO;
import org.savingapp.exception.ChangeActiveGoalException;
import org.savingapp.exception.EntityNotFoundException;
import org.savingapp.model.SavingsGoal;
import org.savingapp.model.SavingsPath;
import org.savingapp.model.User;
import org.savingapp.repository.SavingsGoalRepository;
import org.savingapp.repository.SavingsPathRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * Implementation of the SavingsGoalService.
 */
@AllArgsConstructor
@Service
public class SavingsGoalServiceImpl implements SavingsGoalService {

    private final SavingsGoalRepository savingsGoalRepo;
    private final SavingsPathRepository savingsPathRepo;

    /**
     * Unwraps an optional savings goal.
     *
     * @param unwrappedGoal The optional savings goal.
     * @param id            The id of the savings goal.
     * @return The unwrapped savings goal.
     */
    public static SavingsGoal unwrapGoal(Optional<SavingsGoal> unwrappedGoal, Long id) {
        if (unwrappedGoal.isPresent()) return unwrappedGoal.get();
        else throw new EntityNotFoundException(id, SavingsGoal.class);
    }

    /**
     * Converts a savings goal entity to a DTO.
     *
     * @param goal The savings goal.
     * @return The DTO.
     */
    public static SavingsGoalDTO toDTO(SavingsGoal goal) {
        return SavingsGoalDTO.builder()
                .id(goal.getId())
                .title(goal.getTitle())
                .goalAmount(goal.getGoalAmount())
                .savedAmount(goal.getSavedAmount())
                .endDate(goal.getEndDate())
                .isActive(goal.isActive())
                .build();
    }

    /**
     * Converts a DTO to a savings goal entity.
     *
     * @param goalDTO The DTO.
     * @param user    The user.
     * @return The savings goal.
     */
    public static SavingsGoal toEntity(SavingsGoalDTO goalDTO, User user) {
        return SavingsGoal.builder()
                .id(goalDTO.getId())
                .title(goalDTO.getTitle())
                .goalAmount(goalDTO.getGoalAmount())
                .savedAmount(goalDTO.getSavedAmount())
                .endDate(goalDTO.getEndDate())
                .isActive(goalDTO.isActive())
                .user(user)
                .build();
    }

    /**
     * Sets the active savings goal for a user.
     *
     * @param newGoalId The id of the new goal.
     * @param user      The user.
     */
    @Override
    public void setActiveSavingsGoal(Long newGoalId, User user) {
        SavingsGoal currentActiveGoal = toEntity(getActiveSavingsGoal(user), user);

        SavingsGoal newActiveGoal = unwrapGoal(savingsGoalRepo.findById(newGoalId), newGoalId);
        if (Objects.equals(currentActiveGoal.getId(), newGoalId) || newActiveGoal.getEndDate().isBefore(LocalDate.now())) {
            throw new ChangeActiveGoalException(newGoalId);
        }

        currentActiveGoal.setActive(false);
        newActiveGoal.setActive(true);

        savingsGoalRepo.save(currentActiveGoal);
        savingsGoalRepo.save(newActiveGoal);
    }

    /**
     * Gets the active savings goal for a user.
     *
     * @param user The user.
     * @return The active savings goal.
     */
    @Override
    public SavingsGoalDTO getActiveSavingsGoal(User user) {
        SavingsGoal activeSavingsGoal =
                user.getSavingGoals().stream()
                        .filter(SavingsGoal::isActive)
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException(SavingsGoal.class));

        return toDTO(activeSavingsGoal);
    }

    /**
     * Gets the archived savings goals for a user.
     *
     * @param user The user.
     * @return The archived savings goals.
     */
    @Override
    public List<SavingsGoalDTO> getArchivedSavingGoals(User user) {
        LocalDate today = LocalDate.now();

        List<SavingsGoalDTO> archivedGoals =
                user.getSavingGoals().stream()
                        .filter(s ->
                                (!s.isActive() && s.getEndDate().isBefore(today))
                                        || s.getSavedAmount() >= s.getGoalAmount()
                        )
                        .map(s -> toDTO(s))
                        .toList();

        return archivedGoals;
    }

    /**
     * Gets the current savings goals for a user.
     *
     * @param user The user.
     * @return The current savings goals.
     */
    @Override
    public List<SavingsGoalDTO> getCurrentSavingGoals(User user) {
        LocalDate today = LocalDate.now();

        List<SavingsGoalDTO> currentGoals =
                user.getSavingGoals().stream()
                        .filter(s -> s.getEndDate().isAfter(today) || s.getEndDate().isEqual(today))
                        .map(s -> toDTO(s))
                        .toList();

        if (currentGoals.isEmpty()) throw new EntityNotFoundException(SavingsGoal.class);

        return currentGoals;
    }

    /**
     * Updates a savings goal or creates a new one.
     *
     * @param newGoal The new or existing savings goal.
     * @param user    The user.
     * @return The updated or new savings goal.
     */
    @Override
    public SavingsGoalDTO updateSavingsGoal(SavingsGoalDTO newGoal, User user) {
        SavingsGoal goal;
        if (newGoal.getId() != null) {
            goal = savingsGoalRepo.findById(newGoal.getId()).orElseThrow(() -> new EntityNotFoundException(newGoal.getId(), SavingsGoal.class));
            goal.setId(newGoal.getId());
            goal.setTitle(newGoal.getTitle());
            goal.setGoalAmount(newGoal.getGoalAmount());
            goal.setSavedAmount(newGoal.getSavedAmount());
            goal.setEndDate(newGoal.getEndDate());
            setActiveGoal(newGoal, user, goal);
        } else {
            goal = toEntity(newGoal, user);
            savingsGoalRepo.save(goal);
            savingsPathRepo.save(
                    SavingsPath.builder()
                            .savingsGoal(goal)
                            .build()
            );
            setActiveGoal(newGoal, user, goal);
        }

        if (isCompletedSavingsGoal(goal)) {
            goal.setActive(false);
        }

        savingsGoalRepo.save(goal);
        return toDTO(goal);
    }

    private void setActiveGoal(SavingsGoalDTO newGoal, User user, SavingsGoal goal) {
        SavingsGoal activeSavingsGoal =
                user.getSavingGoals().stream()
                        .filter(SavingsGoal::isActive)
                        .findFirst().orElse(null);
        if (newGoal.isActive() && activeSavingsGoal != null && !Objects.equals(activeSavingsGoal.getId(), newGoal.getId())) {
            activeSavingsGoal.setActive(false);
            savingsGoalRepo.save(activeSavingsGoal);
        }
        goal.setActive(newGoal.isActive());
    }

    /**
     *  Archives a savings goal.
     *
     * @param id The id of the savings goal.
     */
    @Override
    public void archiveSavingsGoal(Long id) {
        savingsGoalRepo.findById(id).ifPresentOrElse(goal -> {
            goal.setActive(false);
            goal.setTitle(goal.getTitle() + " (arkivert)");
            goal.setEndDate(LocalDate.now().minusDays(1));
            savingsGoalRepo.save(goal);
        }, () -> {
            throw new EntityNotFoundException(id, SavingsGoal.class);
        });
    }

    /**
     * Checks if the current goal is finished by checkick the
     * saved amount against goal amount, or by checking if end date is before
     * today's date.
     *
     * @param goal The goal that gets checked
     * @return True if goal is completed, false otherwise
     */
    private boolean isCompletedSavingsGoal(SavingsGoal goal) {
        LocalDate today = LocalDate.now();
        return goal.getSavedAmount() >= goal.getGoalAmount() || goal.getEndDate().isBefore(today);
    }
}
