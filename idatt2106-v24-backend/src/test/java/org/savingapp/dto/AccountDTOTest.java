package org.savingapp.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.savingapp.enums.AccountType;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountDTOTest {
    private AccountDTO accountDTO;

    @BeforeEach
    public void setup() {
        accountDTO = new AccountDTO();
    }

    @Test
    public void testId() {
        Long id = 1L;
        accountDTO.setId(id);
        assertEquals(id, accountDTO.getId());
    }

    @Test
    public void testName() {
        String name = "Test Account";
        accountDTO.setName(name);
        assertEquals(name, accountDTO.getName());
    }

    @Test
    public void testBalance() {
        Double balance = 1000.0;
        accountDTO.setBalance(balance);
        assertEquals(balance, accountDTO.getBalance());
    }

    @Test
    public void testType() {
        AccountType type = AccountType.CHECKING;
        accountDTO.setType(type);
        assertEquals(type, accountDTO.getType());
    }

    @Test
    public void testIngoingTransactions() {
        TransactionDTO transactionDTO = new TransactionDTO();
        // Set properties for transactionDTO here
        accountDTO.setIngoingTransactions(Collections.singletonList(transactionDTO));
        assertEquals(1, accountDTO.getIngoingTransactions().size());
    }

    @Test
    public void testOutgoingTransactions() {
        TransactionDTO transactionDTO = new TransactionDTO();
        // Set properties for transactionDTO here
        accountDTO.setOutgoingTransactions(Collections.singletonList(transactionDTO));
        assertEquals(1, accountDTO.getOutgoingTransactions().size());
    }
}
