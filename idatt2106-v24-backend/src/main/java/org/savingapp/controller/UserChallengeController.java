package org.savingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.savingapp.dto.UserChallengeDTO;
import org.savingapp.service.UserChallengeService;
import org.savingapp.util.UserUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;


/**
 * Controller for the User Challenge API.
 */
@RestController
@RequestMapping("/rest")
@AllArgsConstructor
@Tag(name = "User Challenge Controller", description = "Get and post user challenges.")
public class UserChallengeController {

    private final UserChallengeService userChallengeService;
    private final UserUtil userUtil;


    /**
     * Handles the request for getting pages of challenges for the current user.
     *
     * @param pageable Pageable instance representing the pagination information.
     * @return ResponseEntity<Page<UserChallengeDTO>> instance representing the HTTP response.
     */
    @GetMapping("/user-challenges")
    @Operation(summary = "Get user challenges",
            description = "Get all challenges for the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Page of user challenges returned"),
                    @ApiResponse(responseCode = "404", description = "No user challenges found"),
                    @ApiResponse(responseCode = "500", description = "Unknown error occurred")
            })
    public ResponseEntity<Page<UserChallengeDTO>> getUserChallenges(Pageable pageable) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userChallengeService.getUserChallenges(pageable, userUtil.getCurrentUser()));
        } catch (NullPointerException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the request for posting a new challenge for the authenticated user.
     *
     * @param userChallengeDTO UserChallengeDTO instance representing the user challenge data.
     * @return ResponseEntity<?> instance representing the HTTP response.
     */
    @PostMapping("/user-challenge")
    @Operation(summary = "Post user challenge",
            description = "Post a new challenge for a user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User challenge saved"),
                    @ApiResponse(responseCode = "404", description = "User challenge's referred challenge not found"),
                    @ApiResponse(responseCode = "500", description = "Unknown error occurred")
            }
    )
    public ResponseEntity<?> postUserChallenge(@RequestBody UserChallengeDTO userChallengeDTO) {
        try {
            userChallengeService.saveUserChallenge(userChallengeDTO, userUtil.getCurrentUser());

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NullPointerException | NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the request for calculating the amount saved for the authenticated user.
     *
     * @return ResponseEntity<?> instance representing the HTTP response.
     */
    @Operation(summary = "Calculate amount saved",
            description = "Calculate the amount saved for the authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Amount saved calculated"),
                    @ApiResponse(responseCode = "500", description = "Unknown error occurred")
            }
    )
    @GetMapping("/user-challenges/calculate")
    public ResponseEntity<?> calculateAmountSaved() {
        try {
            userChallengeService.calculateAmountSaved(userUtil.getCurrentUser());

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}