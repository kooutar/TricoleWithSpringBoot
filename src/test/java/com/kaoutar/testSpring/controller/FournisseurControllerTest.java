package com.kaoutar.testSpring.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaoutar.testSpring.dto.FournisseurDTO;
import com.kaoutar.testSpring.service.FournisseurService;
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
@WebMvcTest(FournisseurController.class)
public class FournisseurControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FournisseurService fournisseurService;

    @Autowired
    private ObjectMapper objectMapper;

    // -----------------------------------------
    // TEST CREATE
    // -----------------------------------------
    @Test
    void testCreateFournisseur() throws Exception {

        FournisseurDTO dto = new FournisseurDTO();
        dto.setId(1L);
        dto.setSociete("Test Company");
        dto.setAdresse("Rue ABC");
        dto.setContact("John");
        dto.setEmail("test@mail.com");
        dto.setTelephone("0600000000");
        dto.setVille("Casa");
        dto.setIce("1234567855");

        Mockito.when(fournisseurService.save(Mockito.any())).thenReturn(dto);

        mockMvc.perform(post("/api/fournisseurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.societe").value("Test Company"));
    }

    // -----------------------------------------
    // TEST GET ALL
    // -----------------------------------------
    @Test
    void testGetAllFournisseurs() throws Exception {

        FournisseurDTO dto = new FournisseurDTO();
        dto.setId(1L);
        dto.setSociete("Test");

        Page<FournisseurDTO> page = new PageImpl<>(List.of(dto));

        Mockito.when(fournisseurService.getAllFournisseurs(Mockito.any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/fournisseurs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    // -----------------------------------------
    // TEST GET BY ID
    // -----------------------------------------
    @Test
    void testGetFournisseurById() throws Exception {

        FournisseurDTO dto = new FournisseurDTO();
        dto.setId(5L);
        dto.setSociete("Supplier 5");


        Mockito.when(fournisseurService.getFournisseurById(5L)).thenReturn(dto);

        mockMvc.perform(get("/api/fournisseurs/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.societe").value("Supplier 5"));
    }

    // -----------------------------------------
    // TEST UPDATE
    // -----------------------------------------
    @Test
    void testUpdateFournisseur() throws Exception {

        FournisseurDTO dto = new FournisseurDTO();
        dto.setId(10L);
        dto.setSociete("Updated");
        dto.setAdresse("Rue ABC");
        dto.setContact("John");
        dto.setEmail("test@mail.com");
        dto.setTelephone("0600000000");
        dto.setVille("Casa");
        dto.setIce("1234567855");

        Mockito.when(fournisseurService.updateFournisseur(Mockito.eq(10L), Mockito.any()))
                .thenReturn(dto);

        mockMvc.perform(put("/api/fournisseurs/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.societe").value("Updated"));
    }

    // -----------------------------------------
    // TEST DELETE
    // -----------------------------------------
    @Test
    void testDeleteFournisseur() throws Exception {

        Mockito.when(fournisseurService.deleteFournisseur(3L))
                .thenReturn("Fournisseur supprimé");

        mockMvc.perform(delete("/api/fournisseurs/3"))
                .andExpect(status().isOk())
                .andExpect(content().string("Fournisseur supprimé"));
    }

    @Test
    void testSearchFournisseurs() throws Exception {

        FournisseurDTO dto = new FournisseurDTO();
        dto.setId(1L);
        dto.setSociete("Alpha");

        Page<FournisseurDTO> page = new PageImpl<>(List.of(dto));

        // Mock du service
        Mockito.when(fournisseurService.searchFournisseurs(
                Mockito.eq("Alpha"),
                Mockito.any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(get("/api/fournisseurs/search")
                        .param("societe", "Alpha")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "societe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].societe").value("Alpha"));
    }

}
