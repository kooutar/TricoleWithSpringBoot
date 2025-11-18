package com.kaoutar.testSpring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaoutar.testSpring.dto.CommandeDTO;
import com.kaoutar.testSpring.enums.StatusCommande;
import com.kaoutar.testSpring.service.CommandeService;
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

@WebMvcTest(CommandeController.class)
class CommandeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommandeService commandeService;

    @Autowired
    private ObjectMapper objectMapper;

    // =========================================================================
    // TEST CREATE
    // =========================================================================
    @Test
    void testCreateCommande() throws Exception {

        CommandeDTO commande = new CommandeDTO();
        commande.setId(1L);
        commande.setStatut(StatusCommande.ENTREE);

        Mockito.when(commandeService.createCommande(Mockito.any(), Mockito.eq(10L)))
                .thenReturn(commande);

        mockMvc.perform(post("/api/commandes")
                        .param("produitId", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commande)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.statut").value("ENTREE"));
    }

    // =========================================================================
    // TEST GET ALL
    // =========================================================================
    @Test
    void testGetAllCommandes() throws Exception {

        CommandeDTO commande = new CommandeDTO();
        commande.setId(5L);

        Page<CommandeDTO> page = new PageImpl<>(List.of(commande));

        Mockito.when(commandeService.getAllCommandes(Mockito.any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/commandes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(5));
    }

    // =========================================================================
    // TEST GET BY ID
    // =========================================================================
    @Test
    void testGetCommandeById() throws Exception {

        CommandeDTO commande = new CommandeDTO();
        commande.setId(7L);

        Mockito.when(commandeService.getCommandeById(7L)).thenReturn(commande);

        mockMvc.perform(get("/api/commandes/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7));
    }

    // =========================================================================
    // TEST UPDATE
    // =========================================================================
    @Test
    void testUpdateCommande() throws Exception {

        CommandeDTO commande = new CommandeDTO();
        commande.setId(8L);
        commande.setStatut(StatusCommande.SORTIE);

        Mockito.when(commandeService.updateCommande(Mockito.eq(8L), Mockito.any()))
                .thenReturn(commande);

        mockMvc.perform(put("/api/commandes/8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commande)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("SORTIE"));
    }

    // =========================================================================
    // TEST DELETE
    // =========================================================================
    @Test
    void testDeleteCommande() throws Exception {

        Mockito.doNothing().when(commandeService).deleteCommande(3L);

        mockMvc.perform(delete("/api/commandes/3"))
                .andExpect(status().isNoContent());
    }
}
