package org.savingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.hibernate.NonUniqueResultException;
import org.savingapp.dto.UserInsightDTO;
import org.savingapp.exception.EntityNotFoundException;
import org.savingapp.service.UserInsightService;
import org.savingapp.util.UserUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Controller for the User Insight API.
 */
@Tag(
        name = "User Insight Controller",
        description = "Retrieve, create, and update a users insight information."
)
@RestController
@AllArgsConstructor
@RequestMapping("/rest")
public class UserInsightController {

    private final UserInsightService userInsightService;
    private final UserUtil userUtil;


    /**
     * Handles the request for getting a user's insight.
     *
     * @return ResponseEntity<UserInsightDTO> instance representing the HTTP response.
     */
    @Operation(
            summary = "Get a user's insight",
            description = "Retrieve a user's interests, life status and the expense categories they wish to start saving"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved insight"),
            @ApiResponse(responseCode = "204", description = "No user insight was found"),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred")
    })
    @GetMapping(path = "/user-insight", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInsightDTO> getUserInsight() {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userInsightService.getUserInsight(userUtil.getCurrentUser()));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the request for updating or creating a user's insight.
     *
     * @param insight UserInsightDTO instance representing the user's insight data.
     * @return ResponseEntity<UserInsightDTO> instance representing the HTTP response.
     */
    @Operation(
            summary = "Update or create a user's insight",
            description = "Updates a user's insight if it already exists, and creates insight if it doesn't"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created or updated insight"),
            @ApiResponse(responseCode = "400", description = "Bad request due to primary key violation"),
            @ApiResponse(responseCode = "404", description = "No user insight was found"),
            @ApiResponse(responseCode = "409", description = "Cannot update because there are multiple representations of the same entity"),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred")
    })
    @PostMapping("/user-insight")
    public ResponseEntity<UserInsightDTO> updateUserInsight(
            @RequestBody UserInsightDTO insight
    ) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userInsightService.saveInsight(userUtil.getCurrentUser(), insight));
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (NonUniqueResultException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}