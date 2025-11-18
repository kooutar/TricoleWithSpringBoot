package com.kaoutar.testSpring.controller;

import com.kaoutar.testSpring.service.MouvementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MouvementController.class)
class MouvementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MouvementService mouvementService;

    @Test
    void testGetCUMP() throws Exception {
        Long produitId = 1L;

        when(mouvementService.calculerCUMP(produitId)).thenReturn(25.75);

        mockMvc.perform(get("/api/mouvements/cump/{produitId}", produitId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("25.75"));
    }

    @Test
    void testGetCoutTotalApprovisionnement() throws Exception {
        Long produitId = 1L;

        when(mouvementService.calculerCoutTotalApprovisionnement(produitId)).thenReturn(1200.50);

        mockMvc.perform(get("/api/mouvements/cout-approvisionnement/{produitId}", produitId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1200.5"));  // Note: JSON supprime les zéros inutiles
    }
}
