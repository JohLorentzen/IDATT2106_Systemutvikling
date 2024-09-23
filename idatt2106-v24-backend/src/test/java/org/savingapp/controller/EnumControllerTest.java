package org.savingapp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.savingapp.enums.Category;
import org.savingapp.enums.InterestEnum;
import org.savingapp.enums.LifeSituation;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class EnumControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private EnumController enumController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(enumController).build();
    }

    @Test
    @DisplayName("Should return all categories")
    public void shouldReturnAllCategories() throws Exception {
        mockMvc.perform(get("/enums/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(Category.values().length)));
    }

    @Test
    @DisplayName("Should return all interests")
    public void shouldReturnAllInterests() throws Exception {
        mockMvc.perform(get("/enums/interests")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(InterestEnum.values().length)));
    }

    @Test
    @DisplayName("Should return all life situations")
    public void shouldReturnAllLifeSituations() throws Exception {
        mockMvc.perform(get("/enums/life-situations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(LifeSituation.values().length)));
    }
}