package org.savingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.savingapp.dto.AccountDTO;
import org.savingapp.exception.EntityNotFoundException;
import org.savingapp.service.AccountService;
import org.savingapp.util.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Controller for the Account API.
 */
@Tag(name = "Account", description = "Account API")
@RequestMapping("/rest")
@RestController
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final UserUtil userUtil;


    /**
     * Handles the request for getting all accounts of the current user.
     *
     * @return ResponseEntity<List<AccountDTO>> instance representing the HTTP response.
     */
    @Operation(summary = "Get all accounts of the current user",
            description = "Retrieve all accounts owned by the current user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Accounts retrieval failed"),
                    @ApiResponse(responseCode = "500", description = "Unknown error occurred")
            })
    @GetMapping("/accounts")
    public ResponseEntity<?> getAllUserAccounts() {
        try {
            List<AccountDTO> accounts = accountService.getAllUserAccounts(userUtil.getCurrentUser().getId());

            return ResponseEntity.status(HttpStatus.OK).body(accounts);
        } catch (NullPointerException | EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}