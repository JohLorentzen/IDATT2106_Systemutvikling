package org.savingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.savingapp.enums.Category;

import java.time.LocalDateTime;


/**
 * Represents a transaction data transfer object.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Long id;
    private Double amount;
    private LocalDateTime timestamp;
    private Category category;
}
