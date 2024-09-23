package org.savingapp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.savingapp.dto.ChallengeDTO;
import org.savingapp.enums.Category;
import org.savingapp.enums.LifeSituation;
import org.savingapp.service.ChallengeServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChallengeControllerTest {

    @Mock
    private ChallengeServiceImpl challengeService;

    @InjectMocks
    private ChallengeController challengeController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(challengeController).build();
    }

    @Test
    public void getChallengeReturnsChallengeWhenExists() throws Exception {
        ChallengeDTO challengeDTO = new ChallengeDTO(1L, "Challenge", "Try to drive less!",
                Category.CLOTHING, 2, 100, 400.00, LifeSituation.STUDENT);
        when(challengeService.getChallenge()).thenReturn(Optional.of(challengeDTO));

        mockMvc.perform(get("/rest/challenge"))
                .andExpect(status().isOk());
    }

    @Test
    public void getChallengeReturnsNotFoundWhenDoesNotExist() throws Exception {
        //TODO: Implement this test
    }

    @Test
    public void postChallengeReturnsOkWhenSuccessful() throws Exception {
        mockMvc.perform(post("/rest/challenge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"title\":\"Challenge\",\"description\":\"Try to drive less!\"," +
                                "\"category\":\"CLOTHING\",\"difficulty\":2,\"savings\":400," +
                                "\"lifeSituation\":\"STUDENT\",\"duration\":30}"))
                .andExpect(status().isOk());
    }
}