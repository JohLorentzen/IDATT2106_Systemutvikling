package org.savingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.savingapp.enums.AccountType;

import java.util.List;

/**
 * Represents a bank account data transfer object.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private Long id;
    private String name;
    private Double balance;
    private AccountType type;
    private List<TransactionDTO> ingoingTransactions;
    private List<TransactionDTO> outgoingTransactions;
}
