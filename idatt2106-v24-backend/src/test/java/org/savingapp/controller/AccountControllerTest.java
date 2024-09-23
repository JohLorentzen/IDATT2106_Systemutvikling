package org.savingapp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.savingapp.dto.AccountDTO;
import org.savingapp.model.User;
import org.savingapp.service.AccountService;
import org.savingapp.util.UserUtil;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private UserUtil userUtil;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    public void getAllUserAccountsReturnsAccountsWhenExist() throws Exception {
        User user = new User();
        user.setId(1L);
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setName("Test Account");
        accountDTO.setId(1L);
        accountDTO.setBalance(500.0);
        accountDTO.setIngoingTransactions(Collections.emptyList());
        accountDTO.setOutgoingTransactions(Collections.emptyList());
        when(userUtil.getCurrentUser()).thenReturn(user);
        when(accountService.getAllUserAccounts(user.getId())).thenReturn(Collections.singletonList(accountDTO));

        mockMvc.perform(get("/rest/accounts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"id\": 1, \"name\": \"Test Account\", \"balance\": 500.0, " +
                        "\"type\": null, \"ingoingTransactions\": [], \"outgoingTransactions\": []}]"));
    }

    @Test
    public void getAllUserAccountsReturnsEmptyWhenNoAccountsExist() throws Exception {
        User user = new User();
        user.setId(1L);
        when(userUtil.getCurrentUser()).thenReturn(user);
        when(accountService.getAllUserAccounts(user.getId())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/rest/accounts"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }
}