package org.savingapp.service;

import lombok.AllArgsConstructor;
import org.savingapp.dto.ChallengeDTO;
import org.savingapp.model.Challenge;
import org.savingapp.model.ChallengeSkeleton;
import org.savingapp.model.User;
import org.savingapp.model.UserInsight;
import org.savingapp.repository.ChallengeRepository;
import org.savingapp.util.UserUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * Implementation of the ChallengeService.
 */
@AllArgsConstructor
@Service
public class ChallengeServiceImpl implements ChallengeService {

    private UserUtil userUtil;
    private GenerateChallengeService generateChallengeService;
    private ChallengeRepository challengeRepository;


    /**
     * Gets a challenge.
     *
     * @return The challenge.
     */
    @Override
    public Optional<ChallengeDTO> getChallenge() {
        User currentUser = userUtil.getCurrentUser();
        Challenge challenge = getRandomChallenge(currentUser);
        return Optional.of(new ChallengeDTO(
                challenge.getId(), challenge.getName(),
                challenge.getDescription(), challenge.getCategory(),
                challenge.getDuration(), challenge.getSum(),
                challenge.getAverageAmount(), challenge.getLifeSituation()
        ));
    }


    /**
     * Saves a challenge.
     *
     * @param challengeDTO The challenge to save.
     */
    @Override
    public void saveChallenge(ChallengeDTO challengeDTO) {
        challengeRepository.save(
                new Challenge(
                        challengeDTO.getId(),
                        challengeDTO.getName(),
                        challengeDTO.getDescription(),
                        challengeDTO.getCategory(),
                        challengeDTO.getLifeSituation(),
                        challengeDTO.getDuration(),
                        challengeDTO.getSum(),
                        challengeDTO.getAverageAmount()
                ));
    }

    /**
     * Generates a random challenge for a user
     *
     * @param user the user to generate the challenge for
     * @return the generated challenge
     */
    @Override
    public Challenge getRandomChallenge(User user) {
        UserInsight userInsight = user.getUserInsight();
        ChallengeSkeleton skeleton = generateChallengeService.generateRandomCuratedChallenge(user);

        String description = "Spar " + skeleton.getSum() + " på " + skeleton.getCategory().getNorwegianText() + " på " + skeleton.getDuration() + " dager!";
        if (skeleton.getDuration() % 7 == 0) {
            int weeks = skeleton.getDuration() / 7;
            description = "Spar " + skeleton.getSum() + " på " + skeleton.getCategory().getNorwegianText() + " på " + weeks + " uker!";
        } else if (skeleton.getDuration() == 30) {
            description = "Spar " + skeleton.getSum() + " på " + skeleton.getCategory().getNorwegianText() + " på en måned!";
        }
        return new Challenge(null,
                skeleton.getCategory().getChallengeTitle(),
                description,
                skeleton.getCategory(),
                userInsight.getLifeSituation(),
                skeleton.getDuration(),
                skeleton.getSum(),
                ((double) skeleton.getSum() / (double) skeleton.getDuration())
        );
    }

    /**
     * Generates a challenge for a user based on the sum
     *
     * @param user the user to generate the challenge for
     * @param sum  the sum the challenge will have
     * @return the generated challenge
     */
    @Override
    public Challenge getChallengeBySum(User user, int sum) {
        UserInsight userInsight = user.getUserInsight();
        ChallengeSkeleton skeleton = generateChallengeService.generateCuratedChallengeBySum(user, sum);

        String description = "Spar " + skeleton.getSum() + " på " + skeleton.getCategory().getNorwegianText() + " på " + skeleton.getDuration() + " dager!";
        if (skeleton.getDuration() % 7 == 0) {
            int weeks = skeleton.getDuration() / 7;
            description = "Spar " + skeleton.getSum() + " på " + skeleton.getCategory().getNorwegianText() + " på " + weeks + " uker!";
        } else if (skeleton.getDuration() == 30) {
            description = "Spar " + skeleton.getSum() + " på " + skeleton.getCategory().getNorwegianText() + " på en måned!";
        }
//        return new Challenge(null, "Name", description, skeleton.getCategory(), userInsight.getLifeSituation(), skeleton.getDuration(), skeleton.getSum(), ((double)skeleton.getSum()/(double)skeleton.getDuration()));
        return Challenge.builder()
                .name(skeleton.getCategory().getChallengeTitle())
                .description(description)
                .category(skeleton.getCategory())
                .lifeSituation(userInsight.getLifeSituation())
                .duration(skeleton.getDuration())
                .sum(skeleton.getSum())
                .averageAmount((double) skeleton.getSum() / skeleton.getDuration())
                .build();
    }

    /**
     * Generates a challenge for a user based on the duration
     *
     * @param user     the user to generate the challenge for
     * @param duration the duration the challenge will have
     * @return the generated challenge
     */
    @Override
    public Challenge getChallengeByDuration(User user, int duration) {
        UserInsight userInsight = user.getUserInsight();
        ChallengeSkeleton skeleton = generateChallengeService.generateCuratedChallengeByDuration(user, duration);

        String description = "Spar " + skeleton.getSum() + " på " + skeleton.getCategory().getNorwegianText() + " på " + skeleton.getDuration() + " dager!";
        if (skeleton.getDuration() % 7 == 0) {
            int weeks = skeleton.getDuration() / 7;
            description = "Spar " + skeleton.getSum() + " på " + skeleton.getCategory().getNorwegianText() + " på " + weeks + " uker!";
        } else if (skeleton.getDuration() == 30) {
            description = "Spar " + skeleton.getSum() + " på " + skeleton.getCategory().getNorwegianText() + " på en måned!";
        }
//        return new Challenge(null, "Name", description, skeleton.getCategory(), userInsight.getLifeSituation(), skeleton.getDuration(), skeleton.getSum(), ((double)skeleton.getSum()/(double)skeleton.getDuration()));
        return Challenge.builder()
                .name(skeleton.getCategory().getChallengeTitle())
                .description(description)
                .category(skeleton.getCategory())
                .lifeSituation(userInsight.getLifeSituation())
                .duration(skeleton.getDuration())
                .sum(skeleton.getSum())
                .averageAmount((double) skeleton.getSum() / skeleton.getDuration())
                .build();
    }
}
