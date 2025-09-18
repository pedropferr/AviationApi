package com.pedro.aviationapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedro.aviationapi.api.dtos.AirportRequest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;

@SpringBootTest
@AutoConfigureMockMvc
public class AirportControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

//    @Test
//    void whenAirportCodeIsValid_thenReturnsOk() throws Exception {
//        var request = new AirportRequest();
//        request.airportCode = "AVL";
//
//        mockMvc.perform(post("/api/aeroportos/consultar")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Recebido: AVL"));
//    }

    @Test
    void whenAirportCodeIsEmpty_thenReturnsAllRelevantErrors() throws Exception {
        var request = new AirportRequest();
        request.airportCode = ""; // campo vazio

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
                .andExpect(jsonPath("$[0]").value("airportCode: Código do aeroporto invalido"));
    }

    @Test
    void whenAirportCodeTooShort_thenReturnsSizeError() throws Exception {
        var request = new AirportRequest();
        request.airportCode = "AB";

        mockMvc.perform(post("/api/aeroportos/consultar")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value("airportCode: O código deve ter entre 3 e 50 caracteres"));
    }

    @Test
    void whenAirportCodeTooLong_thenReturnsSizeError() throws Exception {
        var request = new AirportRequest();
        request.airportCode = "ABCDE";

        mockMvc.perform(post("/api/aeroportos/consultar")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value("airportCode: O código deve ter entre 3 e 50 caracteres"))
                .andExpect(jsonPath("$[1]").value("airportCode: Código do aeroporto invalido"));
    }
}
