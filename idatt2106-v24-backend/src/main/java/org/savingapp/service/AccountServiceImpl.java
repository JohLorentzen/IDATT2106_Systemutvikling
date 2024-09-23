package org.savingapp.service;

import lombok.AllArgsConstructor;
import org.savingapp.dto.AccountDTO;
import org.savingapp.dto.TransactionDTO;
import org.savingapp.model.Transaction;
import org.savingapp.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Implementation of the AccountService.
 */
@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;


    /**
     * Fetches all accounts for a user.
     * @param userId The user ID.
     * @return The accounts.
     */
    @Override
    public List<AccountDTO> getAllUserAccounts(Long userId) {
        return accountRepository.findByUserId(userId).stream().map(account -> {
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setId(account.getId());
            accountDTO.setName(account.getName());
            accountDTO.setType(account.getType());
            accountDTO.setBalance(account.getBalance());
            accountDTO.setIngoingTransactions(mapTransactions(account.getIngoingTransactions()));
            accountDTO.setOutgoingTransactions(mapTransactions(account.getOutgoingTransactions()));
            return accountDTO;
        }).toList();
    }


    /**
     * Maps a list of transactions to DTOs.
     * @param transactions The transactions.
     * @return The DTOs.
     */
    private List<TransactionDTO> mapTransactions(List<Transaction> transactions) {
        return transactions.stream().map(transaction -> {
            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setId(transaction.getId());
            transactionDTO.setAmount(transaction.getAmount());
            transactionDTO.setTimestamp(transaction.getTimestamp());
            transactionDTO.setCategory(transaction.getCategory());
            return transactionDTO;
        }).toList();
    }
}
