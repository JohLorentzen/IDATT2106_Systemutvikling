package org.savingapp.model;

import jakarta.persistence.*;
import lombok.Data;
import org.savingapp.enums.AccountType;

import java.util.List;

/**
 * Represents an account.
 */
@Data
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double balance;

    private AccountType type;

    @OneToMany(mappedBy = "toAccount")
    private List<Transaction> ingoingTransactions;

    @OneToMany(mappedBy = "fromAccount", fetch = FetchType.EAGER)
    private List<Transaction> outgoingTransactions;

    @ManyToOne
    private User user;
}
