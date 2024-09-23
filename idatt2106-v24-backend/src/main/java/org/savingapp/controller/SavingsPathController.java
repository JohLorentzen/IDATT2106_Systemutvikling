package org.savingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.savingapp.dto.CompletedUserChallengeDTO;
import org.savingapp.dto.SavingsPathDTO;
import org.savingapp.dto.UserChallengeDTO;
import org.savingapp.exception.EntityNotFoundException;
import org.savingapp.service.SavingsPathService;
import org.savingapp.util.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Controller for the Savings Path API.
 */
@Tag(
        name = "Savings Path Controller",
        description = "Retrieve a savings path based on the active savings goal."
)
@AllArgsConstructor
@RestController
@RequestMapping("/rest")
public class SavingsPathController {

    private final SavingsPathService savingsPathService;
    private final UserUtil userUtil;


    /**
     * Handles the fetching of the user's savings goal in the format of a savings path.
     *
     * @param id The id of the savings goal to get the savings path for.
     * @return ResponseEntity<SavingsPathDTO> instance representing the HTTP response.
     */
    @GetMapping("/savings-path")
    @Operation(summary = "Get savings path",
            description = "Get data needed for the savings path, based on the active savings goal or the provided id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Savings path successfully retrieved"),
                    @ApiResponse(responseCode = "204", description = "User has no active saving goal"),
                    @ApiResponse(responseCode = "404", description = "Savings path not found"),
                    @ApiResponse(responseCode = "500", description = "Failed to get fetch savings path")
            })
    public ResponseEntity<SavingsPathDTO> getSavingsPath(@RequestParam(required = false) Long id) {
        try {
            SavingsPathDTO savingsPathDTO;

            if (id != null) {
                savingsPathDTO = savingsPathService.getSavingsPathById(id);
            } else {
                savingsPathDTO = savingsPathService.getSavingsPathByUser(userUtil.getCurrentUser());
            }

            if (savingsPathDTO == null) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

            return ResponseEntity.status(HttpStatus.OK).body(savingsPathDTO);
        } catch (NullPointerException | EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the fetching of the user's savings goal in the format of a savings path.
     *
     * @param id The id of the savings goal to get the completed challenges for.
     * @return ResponseEntity<List<CompletedUserChallengeDTO>> instance representing the HTTP response.
     */
    @GetMapping("/savings-path/completed-challenges")
    @Operation(summary = "Get completed user challenges",
            description = "Get all of the users completed user challenges, " +
                    "either by savings goal ID or the currently active savings goal",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Savings path successfully sent"),
                    @ApiResponse(responseCode = "500", description = "Savings goal or user challenge not found")
            })
    public ResponseEntity<List<CompletedUserChallengeDTO>> getCompletedUserChallenges(@RequestParam(required = false) Long id) {
        List<CompletedUserChallengeDTO> completedUserChallengesDTO;
        try {
            if (id != null) {
                completedUserChallengesDTO = savingsPathService.getCompletedUserChallengesBySavingsPathId(
                        id,
                        userUtil.getCurrentUser()
                );
            } else {
                completedUserChallengesDTO = savingsPathService.getCompletedUserChallenges(userUtil.getCurrentUser());
            }

            return ResponseEntity.status(HttpStatus.OK).body(completedUserChallengesDTO);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the fetching of the user's savings goal in the format of a savings path.
     *
     * @return ResponseEntity<?> instance representing the HTTP response.
     */
    @PostMapping("/savings-path/completed-challenge")
    @Operation(summary = "Save a user challenge as completed",
            description = "Add a completed user challenge to ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Savings path successfully sent"),
                    @ApiResponse(responseCode = "500", description = "User challenge to complete was not found")
            })
    public ResponseEntity<?> addCompletedUserChallenge(@RequestBody UserChallengeDTO userChallengeDTO) {
        try {
            savingsPathService.addCompletedUserChallenge(userChallengeDTO, userUtil.getCurrentUser());

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}