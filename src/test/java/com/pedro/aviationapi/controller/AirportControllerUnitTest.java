package com.pedro.aviationapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedro.aviationapi.api.controllers.AirportController;
import com.pedro.aviationapi.api.dtos.AirportRequest;
import com.pedro.aviationapi.application.services.AirportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AirportController.class)
public class AirportControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AirportService airportService;

    @Test
    void whenAirportCodeIsEmpty_thenReturnsAllRelevantErrors() throws Exception {
        var request = new AirportRequest();
        request.airportCode = "";

        mockMvc.perform(post("/api/aeroportos/consultar")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[?(@ == 'airportCode: O código é obrigatório!')]").exists())
                .andExpect(jsonPath("$[?(@ == 'airportCode: O código deve ter entre 3 e 50 caracteres')]").exists())
                .andExpect(jsonPath("$[?(@ == 'airportCode: Código do aeroporto invalido')]").exists());
    }

    @Test
    void whenAirportCodeInvalidPattern_thenReturnsPatternError() throws Exception {
        var request = new AirportRequest();
        request.airportCode = "A1@";

        mockMvc.perform(post("/api/aeroportos/consultar")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[?(@ == 'airportCode: Código do aeroporto invalido')]").exists());
    }

    @Test
    void whenAirportCodeTooShort_thenReturnsSizeError() throws Exception {
        var request = new AirportRequest();
        request.airportCode = "AB";

        mockMvc.perform(post("/api/aeroportos/consultar")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[?(@ == 'airportCode: Código do aeroporto invalido')]").exists())
                .andExpect(jsonPath("$[?(@ == 'airportCode: O código deve ter entre 3 e 50 caracteres')]").exists());
    }

    @Test
    void whenAirportCodeTooLong_thenReturnsSizeError() throws Exception {
        var request = new AirportRequest();
        request.airportCode = "ABCDE";

        mockMvc.perform(post("/api/aeroportos/consultar")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[?(@ == 'airportCode: Código do aeroporto invalido')]").exists())
                .andExpect(jsonPath("$[?(@ == 'airportCode: O código deve ter entre 3 e 50 caracteres')]").exists());
    }
}

