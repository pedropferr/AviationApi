package com.pedro.aviationapi.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.pedro.aviationapi.application.services.AirportService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.pedro.aviationapi.shared.dtos.AirportResponse;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class AirportControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AirportService airportService;

    @Test
    void testSearchAirportsValidCode() throws Exception {
        when(airportService.getAirportsByCode("AVL"))
                .thenReturn(CompletableFuture.completedFuture(
                        List.of(new AirportResponse("AVL", "KAVL", "ASHEVILLE RGNL", "ASHEVILLE", "", ""))
                ));

        MvcResult mvcResult = mockMvc.perform(get("/api/aeroportos/AVL"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].faaCode").value("AVL"))
                .andExpect(jsonPath("$[0].name").value("ASHEVILLE RGNL"))
                .andExpect(jsonPath("$[0].city").value("ASHEVILLE"))
                .andDo(result -> System.out.println("BODY: " + result.getResponse().getContentAsString()));
    }

    @Test
    void testSearchAirportsNotFound() throws Exception {
        when(airportService.getAirportsByCode("ZZZ"))
                .thenReturn(CompletableFuture.completedFuture(List.of()));

        MvcResult mvcResult = mockMvc.perform(get("/api/aeroportos/ZZZ"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound());
    }
}
