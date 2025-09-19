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
import org.springframework.test.web.servlet.MvcResult;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;
import java.util.List;

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
                .thenReturn(CompletableFuture.completedFuture(List.of(apiResponse)));

        AirportRequest request = new AirportRequest();
        request.airportCode = "AVL";

        MvcResult mvcResult = mockMvc.perform(post("/api/aeroportos/consultar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].faaCode").value("AVL"))
                .andExpect(jsonPath("$[0].source").value("API"));
    }

    @Test
    void testConsultAirportWithCacheHit() throws Exception {
        AirportResponse cachedResponse = new AirportResponse(
                "AVL", "KAVL", "Asheville Regional",
                "Asheville", "NC", "USA", "CACHE"
        );

        when(airportService.getAirportsByCode("AVL"))
                .thenReturn(CompletableFuture.completedFuture(List.of(cachedResponse)));

        AirportRequest request = new AirportRequest();
        request.airportCode = "AVL";

        MvcResult mvcResult = mockMvc.perform(post("/api/aeroportos/consultar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].faaCode").value("AVL"))
                .andExpect(jsonPath("$[0].source").value("CACHE"));
    }
}