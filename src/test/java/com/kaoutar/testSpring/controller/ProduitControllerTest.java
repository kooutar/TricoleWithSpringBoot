package com.kaoutar.testSpring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaoutar.testSpring.dto.ProduitDTO;
import com.kaoutar.testSpring.service.ProduitService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProduitController.class)
class ProduitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProduitService produitService;

    @Autowired
    private ObjectMapper objectMapper;

    // =========================================================================
    // TEST CREATE
    // =========================================================================
    @Test
    void testCreateProduit() throws Exception {

        ProduitDTO produit = new ProduitDTO();
        produit.setId(1L);
        produit.setNom("Produit A");
        produit.setPrix_unitaire(100.0);
        produit.setQnte_stock(5);

        Mockito.when(produitService.save(Mockito.any())).thenReturn(produit);

        mockMvc.perform(post("/api/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Produit A"));
    }

    // =========================================================================
    // TEST GET ALL
    // =========================================================================
    @Test
    void testGetAllProduits() throws Exception {

        ProduitDTO produit = new ProduitDTO();
        produit.setId(1L);
        produit.setNom("Produit X");

        Page<ProduitDTO> page = new PageImpl<>(List.of(produit));

        Mockito.when(produitService.getAllProduits(Mockito.any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/produits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nom").value("Produit X"));
    }

    // =========================================================================
    // TEST GET BY ID
    // =========================================================================
    @Test
    void testGetProduitById() throws Exception {

        ProduitDTO produit = new ProduitDTO();
        produit.setId(10L);
        produit.setNom("Produit Test");

        Mockito.when(produitService.getProduitById(10L)).thenReturn(produit);

        mockMvc.perform(get("/api/produits/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nom").value("Produit Test"));
    }

    // =========================================================================
    // TEST UPDATE
    // =========================================================================
    @Test
    void testUpdateProduit() throws Exception {

        ProduitDTO produit = new ProduitDTO();
        produit.setId(5L);
        produit.setNom("Produit Modifié");

        Mockito.when(produitService.updateProduit(Mockito.eq(5L), Mockito.any()))
                .thenReturn(produit);

        mockMvc.perform(put("/api/produits/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Produit Modifié"));
    }

    // =========================================================================
    // TEST DELETE
    // =========================================================================
    @Test
    void testDeleteProduit() throws Exception {

        Mockito.when(produitService.deleteProduit(3L))
                .thenReturn("Produit supprimé");

        mockMvc.perform(delete("/api/produits/3"))
                .andExpect(status().isOk())
                .andExpect(content().string("Produit supprimé"));
    }


    // =========================================================================
    // TEST SEARCH BY NAME
    // =========================================================================
    @Test
    void testSearchProduitByName() throws Exception {

        ProduitDTO produit = new ProduitDTO();
        produit.setId(7L);
        produit.setNom("Laptop");

        Mockito.when(produitService.getProduitByName("Laptop"))
                .thenReturn(List.of(produit));

        mockMvc.perform(get("/api/produits/search/Laptop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(7))
                .andExpect(jsonPath("$[0].nom").value("Laptop"));
    }
}
