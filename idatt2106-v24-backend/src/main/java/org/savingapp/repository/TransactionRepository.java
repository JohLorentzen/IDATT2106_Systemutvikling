package org.savingapp.repository;

import org.savingapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository for the Transaction entity.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
