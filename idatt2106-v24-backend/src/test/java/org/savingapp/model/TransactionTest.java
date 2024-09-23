package org.savingapp.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.savingapp.enums.Category;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionTest {
    private Transaction transaction;

    @BeforeEach
    public void setup() {
        transaction = new Transaction();
    }

    @Test
    public void testId() {
        Long idValue = 1L;
        transaction.setId(idValue);
        assertEquals(idValue, transaction.getId());
    }

    @Test
    public void testFromAccount() {
        Account fromAccount = new Account();
        transaction.setFromAccount(fromAccount);
        assertEquals(fromAccount, transaction.getFromAccount());
    }

    @Test
    public void testToAccount() {
        Account toAccount = new Account();
        transaction.setToAccount(toAccount);
        assertEquals(toAccount, transaction.getToAccount());
    }

    @Test
    public void testAmount() {
        Double amount = 100.0;
        transaction.setAmount(amount);
        assertEquals(amount, transaction.getAmount());
    }

    @Test
    public void testTimestamp() {
        LocalDateTime timestamp = LocalDateTime.now();
        transaction.setTimestamp(timestamp);
        assertEquals(timestamp, transaction.getTimestamp());
    }

    @Test
    public void testCategory() {
        Category category = Category.TRANSPORTATION;
        transaction.setCategory(category);
        assertEquals(category, transaction.getCategory());
    }
}
