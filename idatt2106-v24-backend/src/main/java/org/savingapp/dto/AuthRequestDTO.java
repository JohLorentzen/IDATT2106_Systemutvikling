package org.savingapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Represents a user credentials pair data transfer object.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDTO {
    @Schema(description = "The username of the user", example = "testuser1")
    private String username;

    @Schema(description = "The password of the user", example = "password")
    private String password;
}
