package com.pedro.aviationapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedro.aviationapi.api.controllers.AirportController;
import com.pedro.aviationapi.api.dtos.AirportRequest;
import com.pedro.aviationapi.api.dtos.AirportResponse;
import com.pedro.aviationapi.application.services.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AirportController.class)
public class AirportControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AirportService airportService;

    @Test
    void testConsultAirportWithApiCall() throws Exception {
        AirportResponse apiResponse = new AirportResponse(
                "AVL", "KAVL", "Asheville Regional",
                "Asheville", "NC", "USA", "API"
        );

        when(airportService.getAirportsByCode("AVL"))
                .thenReturn(apiResponse);

        AirportRequest request = new AirportRequest();
        request.airportCode = "AVL";

        mockMvc.perform(post("/api/aeroportos/consultar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("faaCode").value("AVL"))
                .andExpect(jsonPath("source").value("API"));
    }

    @Test
    void testConsultAirportWithCacheHit() throws Exception {
        AirportResponse cachedResponse = new AirportResponse(
                "AVL", "KAVL", "Asheville Regional",
                "Asheville", "NC", "USA", "CACHE"
        );

        when(airportService.getAirportsByCode("AVL"))
                .thenReturn(cachedResponse);

        AirportRequest request = new AirportRequest();
        request.airportCode = "AVL";

        mockMvc.perform(post("/api/aeroportos/consultar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("faaCode").value("AVL"))
                .andExpect(jsonPath("source").value("CACHE"));
    }
}