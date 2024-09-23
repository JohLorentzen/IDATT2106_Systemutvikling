package org.savingapp.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.savingapp.dto.BudgetDTO;
import org.savingapp.dto.BudgetPostDTO;
import org.savingapp.enums.Category;
import org.savingapp.service.BudgetService;
import org.savingapp.util.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Budget", description = "Budget API")
@RequestMapping("/rest")
@RestController
@AllArgsConstructor
public class BudgetController {

    private final UserUtil userUtil;
    private final BudgetService budgetService;


    /**
     * Handles the request for getting all budgets of the current user.
     *
     * @return ResponseEntity<List<BudgetDTO>> instance representing the HTTP response.
     */
    @Operation(summary = "Get all budgets of the current user",
            description = "Retrieve all budgets of the current user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Budgets retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Unknown error occurred")
            }
    )
    @GetMapping("/all-budgets")
    public ResponseEntity<List<BudgetDTO>> getAllUserBudgets() {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(budgetService.getAllUserBudgets(userUtil.getCurrentUser().getId()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the request for getting the current active budget of the current user.
     *
     * @return ResponseEntity<BudgetDTO> instance representing the HTTP response.
     */
    @Operation(summary = "Get the current active budget of the current user",
            description = "Retrieve the current active budget of the current user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Active budget retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Unknown error occurred")
            }
    )
    @GetMapping("/active-budget")
    public ResponseEntity<BudgetDTO> getActiveUserBudget() {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(budgetService.getActiveBudget(userUtil.getCurrentUser().getId()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the request for getting the budget by id.
     *
     * @param budgetId Long instance representing the budget id.
     * @return ResponseEntity<BudgetDTO> instance representing the HTTP response.
     */
    @Operation(summary = "Get the budget by id",
            description = "Retrieve the budget by the provided id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Budget retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Unknown error occurred")
            })
    @GetMapping("/budget")
    public ResponseEntity<BudgetDTO> getBudget(Long budgetId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(budgetService.getBudget(budgetId));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the request for getting all budget posts of the budget.
     *
     * @param budgetId Long instance representing the budget id.
     * @return ResponseEntity<List<BudgetPostDTO>> instance representing the HTTP response.
     */
    @Operation(summary = "Get all budget posts of the budget",
            description = "Retrieve all budget posts of the budget",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Budget posts retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Unknown error occurred")
            }
    )
    @GetMapping("/budget-posts")
    public ResponseEntity<List<BudgetPostDTO>> getBudgetPosts(Long budgetId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(budgetService.getBudgetPosts(budgetId));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the request for adding an existing budget.
     *
     * @param budgetDTO BudgetDTO instance representing the budget data.
     * @return ResponseEntity<?> instance representing the HTTP response.
     */
    @Operation(summary = "Add a budget",
            description = "Add a new budget",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Budget added successfully"),
                    @ApiResponse(responseCode = "500", description = "Unknown error occurred")
            }
    )
    @PostMapping("/add-budget")
    public ResponseEntity<?> addBudget(@RequestBody BudgetDTO budgetDTO) {
        try {
            budgetService.addBudget(userUtil.getCurrentUser().getId(), budgetDTO);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the request for finding a budget by month and year.
     *
     * @param month int instance representing the month.
     * @param year  int instance representing the year.
     * @return ResponseEntity<?> instance representing the HTTP response.
     */
    @Operation(summary = "Find budget by month and year",
            description = "Find the budget by the provided month and year",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Budget found successfully"),
                    @ApiResponse(responseCode = "500", description = "Unknown error occurred")
            }
    )
    @GetMapping("/budget-by-month-and-year")
    public ResponseEntity<BudgetDTO> getBudgetByMonthAndYear(int month, int year) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(budgetService.getBudgetByMonthAndYear(userUtil.getCurrentUser().getId(), month, year));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the request for deleting a budget.
     *
     * @param budgetId Long instance representing the budget id.
     * @return ResponseEntity<?> instance representing the HTTP response.
     */
    @Operation(summary = "Delete a budget",
            description = "Delete the budget by the provided id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Budget deleted successfully"),
                    @ApiResponse(responseCode = "500", description = "Unknown error occurred")
            }
    )
    @DeleteMapping("/delete-budget")
    public ResponseEntity<?> deleteBudget(Long budgetId) {
        try {
            budgetService.deleteBudget(budgetId);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the request for adding a budget post.
     *
     * @param budgetId      Long instance representing the budget id.
     * @param budgetPostDTO BudgetPostDTO instance representing the budget post data.
     * @return ResponseEntity<?> instance representing the HTTP response.
     */
    @Operation(summary = "Add a budget post",
            description = "Add a new budget post",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Budget post added successfully"),
                    @ApiResponse(responseCode = "409", description = "Budget post already exists"),
                    @ApiResponse(responseCode = "500", description = "Unknown error occurred")
            }
    )
    @PostMapping("/add-budget-post")
    public ResponseEntity<?> addBudgetPost(Long budgetId, @RequestBody BudgetPostDTO budgetPostDTO) {
        try {
            budgetService.addBudgetPost(budgetId, budgetPostDTO);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the request for updating a budget post.
     *
     * @param budgetId      Long instance representing the budget id.
     * @param budgetPostDTO BudgetPostDTO instance representing the budget post data.
     * @return ResponseEntity<?> instance representing the HTTP response.
     */
    @Operation(summary = "Update a budget post",
            description = "Update a budget post",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Budget post updated successfully"),
                    @ApiResponse(responseCode = "500", description = "Unknown error occurred")
            }
    )
    @PostMapping("/update-budget-post")
    public ResponseEntity<?> updateBudgetPost(Long budgetId, @RequestBody BudgetPostDTO budgetPostDTO) {
        try {
            budgetService.updateBudgetPost(budgetId, budgetPostDTO);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the request for deleting a budget post.
     *
     * @param budgetId Long instance representing the budget id.
     * @param category Category instance representing the category.
     * @return ResponseEntity<?> instance representing the HTTP response.
     */
    @Operation(summary = "Delete a budget post",
            description = "Delete a budget post",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Budget post deleted successfully"),
                    @ApiResponse(responseCode = "500", description = "Unknown error occurred")
            }
    )
    @DeleteMapping("/delete-budget-post")
    public ResponseEntity<?> deleteBudgetPost(Long budgetId, Category category) {
        try {
            budgetService.deleteBudgetPost(budgetId, category);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the request for creating a budget.
     *
     * @return ResponseEntity<BudgetDTO> instance representing the HTTP response.
     */
    @Operation(summary = "Create a budget",
            description = "Create a new budget",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Budget created successfully"),
                    @ApiResponse(responseCode = "500", description = "Unknown error occurred")
            }
    )
    @GetMapping("/create-budget")
    public ResponseEntity<BudgetDTO> createBudget() {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(budgetService.createBudget(userUtil.getCurrentUser().getId()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}