package org.savingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.savingapp.enums.Category;
import org.savingapp.enums.InterestEnum;
import org.savingapp.enums.LifeSituation;
import org.savingapp.enums.Localizable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Controller for the Enums API.
 */
@Tag(name = "Enums", description = "Get available categories, interests, and life situations")
@RestController
@RequestMapping("/enums")
public class EnumController {


    /**
     * Handles the request for getting all category-enums.
     *
     * @return ResponseEntity<List<EnumRepresentation<Category>>> instance representing the HTTP response.
     */
    @Operation(summary = "Get all categories",
            description = "Retrieve all available expense categories",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved categories"),
                    @ApiResponse(responseCode = "500", description = "Unknown error occurred")
            }
    )
    @GetMapping("/categories")
    public ResponseEntity<List<EnumRepresentation<Category>>> getCategories() {
        try {
            List<EnumRepresentation<Category>> enumRepresentations = Arrays.stream(Category.values())
                    .map(EnumRepresentation::new)
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(enumRepresentations);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


    /**
     * Handles the request for getting all interest-enums.
     *
     * @return ResponseEntity<List<EnumRepresentation<InterestEnum>>> instance representing the HTTP response.
     */
    @Operation(summary = "Get all interests",
            description = "Retrieve all available interests",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved interests"),
                    @ApiResponse(responseCode = "500", description = "Unknown error occurred")
            }
    )
    @GetMapping("/interests")
    public ResponseEntity<List<EnumRepresentation<InterestEnum>>> getInterests() {
        try {
            List<EnumRepresentation<InterestEnum>> enumRepresentations = Arrays.stream(InterestEnum.values())
                    .map(EnumRepresentation::new)
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(enumRepresentations);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the request for getting all life situation-enums.
     *
     * @return ResponseEntity<List<EnumRepresentation<LifeSituation>>> instance representing the HTTP response.
     */
    @Operation(summary = "Get all life situations",
            description = "Retrieve all available life situations",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved life situations"),
                    @ApiResponse(responseCode = "500", description = "Unknown error occurred")
            })
    @GetMapping("/life-situations")
    public ResponseEntity<List<EnumRepresentation<LifeSituation>>> getLifeSituations() {
        try {
            List<EnumRepresentation<LifeSituation>> enumRepresentations = Arrays.stream(LifeSituation.values())
                    .map(EnumRepresentation::new)
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(enumRepresentations);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Represents an enum value with its name and localized text.
     *
     * @param <T> Enum type that extends Localizable.
     */
    public static class EnumRepresentation<T extends Enum<T> & Localizable> {

        private final T enumValue;


        /**
         * Creates an instance of EnumRepresentation.
         *
         * @param enumValue Enum value to represent.
         */
        public EnumRepresentation(T enumValue) {
            this.enumValue = enumValue;
        }


        /**
         * Gets the English text of the enum value.
         *
         * @return String representing the English text.
         */
        public String getEnglishText() {
            return enumValue.getEnglishText();
        }


        /**
         * Gets the Norwegian text of the enum value.
         *
         * @return String representing the Norwegian text.
         */
        public String getNorwegianText() {
            return enumValue.getNorwegianText();
        }


        /**
         * Gets the name of the enum value.
         *
         * @return String representing the name.
         */
        public String getName() {
            return enumValue.name();
        }
    }
}