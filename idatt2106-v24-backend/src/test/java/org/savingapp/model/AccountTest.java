package org.savingapp.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.savingapp.enums.AccountType;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountTest {
    private Account account;

    @BeforeEach
    public void setup() {
        account = new Account();
    }

    @Test
    public void testId() {
        Long idValue = 1L;
        account.setId(idValue);
        assertEquals(idValue, account.getId());
    }

    @Test
    public void testName() {
        String name = "Test Account";
        account.setName(name);
        assertEquals(name, account.getName());
    }

    @Test
    public void testBalance() {
        Double balance = 500.0;
        account.setBalance(balance);
        assertEquals(balance, account.getBalance());
    }

    @Test
    public void testType() {
        AccountType type = AccountType.CHECKING;
        account.setType(type);
        assertEquals(type, account.getType());
    }

    @Test
    public void testIngoingTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        account.setIngoingTransactions(transactions);
        assertEquals(transactions, account.getIngoingTransactions());
    }

    @Test
    public void testOutgoingTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        account.setOutgoingTransactions(transactions);
        assertEquals(transactions, account.getOutgoingTransactions());
    }

    @Test
    public void testUser() {
        User user = new User();
        account.setUser(user);
        assertEquals(user, account.getUser());
    }
}
