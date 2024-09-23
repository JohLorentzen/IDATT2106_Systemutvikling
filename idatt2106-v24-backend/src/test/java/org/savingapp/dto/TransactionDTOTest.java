package org.savingapp.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.savingapp.enums.Category;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionDTOTest {
    private TransactionDTO transactionDTO;

    @BeforeEach
    public void setup() {
        transactionDTO = new TransactionDTO();
    }

    @Test
    public void testId() {
        Long id = 1L;
        transactionDTO.setId(id);
        assertEquals(id, transactionDTO.getId());
    }

    @Test
    public void testAmount() {
        Double amount = 100.0;
        transactionDTO.setAmount(amount);
        assertEquals(amount, transactionDTO.getAmount());
    }

    @Test
    public void testTimestamp() {
        LocalDateTime timestamp = LocalDateTime.now();
        transactionDTO.setTimestamp(timestamp);
        assertEquals(timestamp, transactionDTO.getTimestamp());
    }

    @Test
    public void testCategory() {
        Category category = Category.TRANSPORTATION;
        transactionDTO.setCategory(category);
        assertEquals(category, transactionDTO.getCategory());
    }
}
