package com.kaoutar.testSpring.TestIntegration;

import com.kaoutar.testSpring.model.Mouvement;
import com.kaoutar.testSpring.model.Produit;
import com.kaoutar.testSpring.enums.StatutMouvement;

import com.kaoutar.testSpring.reposetry.MouvementRepository;
import com.kaoutar.testSpring.reposetry.ProduitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase // utilise H2 automatiquement
class MouvementControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private MouvementRepository mouvementRepository;

    private Long produitId;

    @BeforeEach
    void setup() {
        mouvementRepository.deleteAll();
        produitRepository.deleteAll();

        // Création produit
        Produit p = new Produit();
        p.setNom("Test Produit");
        p.setPrix_unitaire(10.00);
        produitRepository.save(p);
        produitId = p.getId();

        // Ajouter des mouvements d’entrée
        Mouvement m1 = new Mouvement();
        m1.setProduit(p);
       // m1.setPrixUnitaire(10.0);
        m1.setQuantite(10);
        m1.setStatut(StatutMouvement.ENTREE);
        mouvementRepository.save(m1);

        Mouvement m2 = new Mouvement();
        m2.setProduit(p);
        //m2.setPrixUnitaire(20.0);
        m2.setQuantite(10);
        m2.setStatut(StatutMouvement.ENTREE);
        mouvementRepository.save(m2);
    }

    @Test
    void testIntegrationCUMP() throws Exception {
        mockMvc.perform(get("/api/mouvements/cump/" + produitId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("15.0")); // (10*10 + 20*10) / 20
    }
}