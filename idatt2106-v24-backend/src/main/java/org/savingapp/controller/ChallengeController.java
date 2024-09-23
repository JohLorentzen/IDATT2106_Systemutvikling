package org.savingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.savingapp.dto.ChallengeDTO;
import org.savingapp.dto.UserChallengeDTO;
import org.savingapp.enums.Category;
import org.savingapp.enums.LifeSituation;
import org.savingapp.exception.EntityNotFoundException;
import org.savingapp.model.Challenge;
import org.savingapp.model.UserChallenge;
import org.savingapp.repository.ChallengeRepository;
import org.savingapp.service.ChallengeService;
import org.savingapp.service.UserChallengeService;
import org.savingapp.util.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Controller for the Challenge API.
 */
@Tag(
        name = "Challenge Controller",
        description = "Retrieve and post challenges."
)
@RestController
@RequestMapping("/rest")
@AllArgsConstructor
public class ChallengeController {

    private final UserUtil userUtil;
    private final ChallengeService challengeService;
    private final UserChallengeService userChallengeService;
    private final ChallengeRepository challengeRepository;


    /**
     * Handles the request for getting a challenge.
     *
     * @return ResponseEntity<ChallengeDTO> instance representing the HTTP response.
     */
    @Operation(summary = "Get a challenge", description = "Get a recommended challenge for a user based on insights.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved a challenge"),
            @ApiResponse(responseCode = "404", description = "Challenge not found"),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred")
    })
    @GetMapping("/challenge")
    public ResponseEntity<ChallengeDTO> getChallenge() {

        try {
            ChallengeDTO challengeDTO = new ChallengeDTO(1L, "Dummy", "DUMMY DUMMY", Category.NOT_CATEGORIZED,
                    -1, -1, -1.0, LifeSituation.WORKING);

            return ResponseEntity.status(HttpStatus.OK).body(challengeService.getChallenge().orElse(challengeDTO));
        } catch (NullPointerException | EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the request for accepting a challenge and creating a user challenge.
     *
     * @param userChallengeDTO UserChallengeDTO instance representing the user challenge data.
     * @return ResponseEntity<?> instance representing the HTTP response.
     */
    @Operation(summary = "Accept a given challenge",
            description = "Accept the current challenge.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully accepted the challenge"),
                    @ApiResponse(responseCode = "500", description = "Unknown error occurred")
            }
    )
    @PostMapping("/challenge/accept")
    public ResponseEntity<?> acceptChallenge(@RequestBody UserChallengeDTO userChallengeDTO) {
        try {
            Challenge challenge = new Challenge(
                    userChallengeDTO.getChallenge().getId(),
                    userChallengeDTO.getChallenge().getName(),
                    userChallengeDTO.getChallenge().getDescription(),
                    userChallengeDTO.getChallenge().getCategory(),
                    userChallengeDTO.getChallenge().getLifeSituation(),
                    userChallengeDTO.getChallenge().getDuration(),
                    userChallengeDTO.getChallenge().getSum(),
                    userChallengeDTO.getChallenge().getAverageAmount());

            challengeRepository.save(challenge);

            UserChallenge userChallenge = new UserChallenge(
                    userChallengeDTO.getId(),
                    userUtil.getCurrentUser(),
                    userChallengeDTO.getGoalAmount(),
                    userChallengeDTO.getSavedAmount(),
                    userChallengeDTO.getFromDate(),
                    userChallengeDTO.getToDate(),
                    challenge);

            userChallengeService.acceptChallenge(userChallenge);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the request for rejecting a challenge.
     *
     * @param challengeDTO ChallengeDTO instance representing the challenge data.
     * @return ResponseEntity<?> instance representing the HTTP response.
     */
    @Operation(summary = "Reject a given challenge",
            description = "Reject the current challenge.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully rejected the challenge"),
                    @ApiResponse(responseCode = "500", description = "Unknown error occurred")
            }
    )
    @PostMapping("/challenge/reject")
    public ResponseEntity<?> rejectChallenge(@RequestBody ChallengeDTO challengeDTO) {
        try {
            userChallengeService.rejectChallenge(
                    userUtil.getCurrentUser(),
                    new Challenge(
                            challengeDTO.getId(),
                            challengeDTO.getName(),
                            challengeDTO.getDescription(),
                            challengeDTO.getCategory(),
                            challengeDTO.getLifeSituation(),
                            challengeDTO.getDuration(),
                            challengeDTO.getSum(),
                            challengeDTO.getAverageAmount()));

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the request for posting a challenge.
     *
     * @param challengeDTO ChallengeDTO instance representing the challenge data.
     * @return ResponseEntity<?> instance representing the HTTP response.
     */
    @Operation(summary = "Post a challenge", description = "Post a new challenge.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully posted a challenge"),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred")
    })
    @PostMapping("/challenge")
    public ResponseEntity<?> postChallenge(@RequestBody ChallengeDTO challengeDTO) {
        try {
            challengeService.saveChallenge(challengeDTO);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}