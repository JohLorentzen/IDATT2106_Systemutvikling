package org.savingapp.service;

import org.savingapp.dto.AccountDTO;

import java.util.List;


/**
 * Interface for the AccountService.
 */
public interface AccountService {

    /**
     * Retrieve all accounts for a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of AccountDTOs for the user.
     */
    List<AccountDTO> getAllUserAccounts(Long userId);
}
