package org.savingapp.model;

import jakarta.persistence.*;
import lombok.Data;
import org.savingapp.enums.Category;

import java.time.LocalDateTime;


/**
 * Represents a transaction.
 */
@Data
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account fromAccount;

    @ManyToOne
    private Account toAccount;

    private Double amount;

    private LocalDateTime timestamp;

    private Category category;
}
