package org.savingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.savingapp.dto.UserStatsDTO;
import org.savingapp.service.UserStatsService;
import org.savingapp.util.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller for the User Stats API.
 */
@Tag(
        name = "User Stats Controller",
        description = "Retrieve user stats"
)
@RestController
@AllArgsConstructor
@RequestMapping("/rest")
public class UserStatsController {

    private final UserStatsService userStatsService;
    private final UserUtil userUtil;


    /**
     * Handles the request for getting a user's stats.
     *
     * @return ResponseEntity<UserStatsDTO> instance representing the HTTP response.
     */
    @Operation(
            summary = "Get a user's stats",
            description = "Retrieve a user's saving stats",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved stats"),
                    @ApiResponse(responseCode = "500", description = "Unknown error occurred")
            }
    )
    @GetMapping("/user-stats")
    public ResponseEntity<UserStatsDTO> getTotalAmountSaved() {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userStatsService.getUserStats(userUtil.getCurrentUser()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}