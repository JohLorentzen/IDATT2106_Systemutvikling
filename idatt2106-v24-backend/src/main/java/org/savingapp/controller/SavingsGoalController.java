package org.savingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.savingapp.dto.SavingsGoalDTO;
import org.savingapp.exception.EntityNotFoundException;
import org.savingapp.service.SavingsGoalService;
import org.savingapp.util.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Controller for the Savings Goal API.
 */
@Tag(
        name = "Savings Goal Controller",
        description = "Retrieve, create, update and delete a users savings goal."
)
@RestController
@AllArgsConstructor
@RequestMapping("/rest")
public class SavingsGoalController {

    private final SavingsGoalService goalService;
    private final UserUtil userUtil;


    /**
     * Handles the request for setting a new active savings goal.
     *
     * @param id The id of the goal to set as active.
     * @return ResponseEntity<?> instance representing the HTTP response.
     */
    @Operation(
            summary = "Set a new active goal",
            description = "Sets the goal with the id to active goal and disables the previous."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated active savings goal"),
            @ApiResponse(responseCode = "404", description = "Failed to set new goal as active"),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred")
    })
    @PostMapping("/set-active/{id}")
    public ResponseEntity<?> setNewActiveSavingsGoal(@PathVariable Long id) {
        try {
            goalService.setActiveSavingsGoal(id, userUtil.getCurrentUser());

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the request for getting a user's active savings goal.
     *
     * @return ResponseEntity<SavingsGoalDTO> instance representing the HTTP response.
     */
    @Operation(
            summary = "Get a user's active savings goal",
            description = "Retrieve a user's active savings goal. Finds goal connected to the user that makes the call."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved active savings goal"),
            @ApiResponse(responseCode = "404", description = "Active goal not found"),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred")
    })
    @GetMapping("/savings-goal")
    public ResponseEntity<SavingsGoalDTO> getActiveSavingsGoal() {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(goalService.getActiveSavingsGoal(userUtil.getCurrentUser()));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the request for getting a user's current available saving goals.
     *
     * @return ResponseEntity<List<SavingsGoalDTO>> instance representing the HTTP response.
     */
    @Operation(
            summary = "Get a user's current available saving goals",
            description = "Retrieve a user's current available goals. These are goals that a user can set as active"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved available saving goals"),
            @ApiResponse(responseCode = "204", description = "No available goals found"),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred")
    })
    @GetMapping("/current-saving-goals")
    public ResponseEntity<List<SavingsGoalDTO>> getCurrentGoals() {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(goalService.getCurrentSavingGoals(userUtil.getCurrentUser()));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the request for getting a user's archived saving goals.
     *
     * @return ResponseEntity<List<SavingsGoalDTO>> instance representing the HTTP response.
     */
    @Operation(
            summary = "Get a user's archived saving goals",
            description = "Retrieves the users archived goals. These are goals with an end date before current date."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved archived saving goals"),
            @ApiResponse(responseCode = "204", description = "User has no archived goals currently"),
            @ApiResponse(responseCode = "404", description = "No archived goals found"),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred")
    })
    @GetMapping("/archived-saving-goals")
    public ResponseEntity<List<SavingsGoalDTO>> getArchivedGoals() {
        try {
            List<SavingsGoalDTO> archivedGoals = goalService.getArchivedSavingGoals(userUtil.getCurrentUser());

            if (archivedGoals != null && archivedGoals.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.status(HttpStatus.OK).body(archivedGoals);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the request for updating or creating a new savings goal.
     *
     * @param savingsGoal SavingsGoalDTO instance representing the savings goal data.
     * @return ResponseEntity<SavingsGoalDTO> instance representing the HTTP response.
     */
    @Operation(
            summary = "Update or create a new savings goal",
            description = "Update a savings goal if it already exists. Create a new savings goal if it doesn't."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully updated or created savings goal"),
            @ApiResponse(responseCode = "404", description = "No goal with this id was found"),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred")
    })
    @PostMapping("/savings-goal")
    public ResponseEntity<SavingsGoalDTO> updateSavingsGoal(@RequestBody SavingsGoalDTO savingsGoal) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(goalService.updateSavingsGoal(savingsGoal, userUtil.getCurrentUser()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the request for deleting a user's active savings goal.
     *
     * @param id The id of the goal to delete.
     * @return ResponseEntity<?> instance representing the HTTP response.
     */
    @Operation(
        summary = "Delete/archive the user's active savings goal",
        description = "Archives a user's active savings goal by id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted goal"),
            @ApiResponse(responseCode = "404", description = "No saving goal with that id was found"),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred")
    })
    @DeleteMapping("/savings-goal/{id}")
    public ResponseEntity<?> deleteSavingsGoal(@PathVariable Long id) {
        try {
            goalService.archiveSavingsGoal(id);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}