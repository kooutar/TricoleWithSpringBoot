package com.kaoutar.testSpring.service;

import com.kaoutar.testSpring.dto.MouvementDTO;
import com.kaoutar.testSpring.enums.StatutMouvement;
import com.kaoutar.testSpring.mapper.MouvementMapper;
import com.kaoutar.testSpring.model.Commande;
import com.kaoutar.testSpring.model.Mouvement;
import com.kaoutar.testSpring.model.Produit;
import com.kaoutar.testSpring.reposetry.MouvementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MouvementServiceTest {
    @Mock
    private MouvementRepository mouvementRepository;

    @Mock
    private MouvementMapper mouvementMapper;

    @InjectMocks
    private MouvementService mouvementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 🧪 TEST 1 : Méthode save()
    @Test
    void testSave_Success() {
        // --- 1. Données simulées ---
        Produit produit = new Produit();
        produit.setId(1L);
        produit.setPrix_unitaire(100.0);

        Commande commande = new Commande();
        commande.setId(10L);

        MouvementDTO mouvementDTO = new MouvementDTO();
        mouvementDTO.setQuantite(5);

        Mouvement mouvementEntity = new Mouvement();
        mouvementEntity.setQuantite(5);

        Mouvement savedEntity = new Mouvement();
        savedEntity.setId(1L);
        savedEntity.setProduit(produit);
        savedEntity.setCommande(commande);

        MouvementDTO savedDTO = new MouvementDTO();
        savedDTO.setId(1L);

        // --- 2. Mock comportement ---
        when(mouvementMapper.toEntity(mouvementDTO)).thenReturn(mouvementEntity);
        when(mouvementRepository.save(mouvementEntity)).thenReturn(savedEntity);
        when(mouvementMapper.toDto(savedEntity)).thenReturn(savedDTO);

        // --- 3. Appel méthode ---
        MouvementDTO result = mouvementService.save(mouvementDTO, produit, commande);

        // --- 4. Vérifications ---
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(mouvementMapper).toEntity(mouvementDTO);
        verify(mouvementRepository).save(mouvementEntity);
        verify(mouvementMapper).toDto(savedEntity);
    }

    // 🧪 TEST 2 : createMouvementForProduit()
    @Test
    void testCreateMouvementForProduit() {
        Produit produit = new Produit();
        produit.setId(1L);

        MouvementDTO mouvementDTO = new MouvementDTO();
        MouvementDTO expectedDTO = new MouvementDTO();

        // Mock : on redirige vers save()
        when(mouvementMapper.toEntity(any())).thenReturn(new Mouvement());
        when(mouvementRepository.save(any())).thenReturn(new Mouvement());
        when(mouvementMapper.toDto(any())).thenReturn(expectedDTO);

        MouvementDTO result = mouvementService.createMouvementForProduit(mouvementDTO, produit);

        assertNotNull(result);
        verify(mouvementRepository, times(1)).save(any());
    }

    // 🧪 TEST 3 : createMouvementForCommande()
    @Test
    void testCreateMouvementForCommande() {
        Produit produit = new Produit();
        Commande commande = new Commande();
        MouvementDTO mouvementDTO = new MouvementDTO();
        MouvementDTO expected = new MouvementDTO();

        when(mouvementMapper.toEntity(any())).thenReturn(new Mouvement());
        when(mouvementRepository.save(any())).thenReturn(new Mouvement());
        when(mouvementMapper.toDto(any())).thenReturn(expected);

        MouvementDTO result = mouvementService.createMouvementForCommande(mouvementDTO, commande, produit);

        assertNotNull(result);
        verify(mouvementRepository, times(1)).save(any());
    }

    // 🧪 TEST 4 : updateMouvementsForCommandeSortie()
    @Test
    void testUpdateMouvementsForCommandeSortie() {
        Mouvement m1 = new Mouvement();
        Mouvement m2 = new Mouvement();

        Commande commande = new Commande();
        commande.setMouvements(Arrays.asList(m1, m2));

        mouvementService.updateMouvementsForCommandeSortie(commande);

        assertEquals(StatutMouvement.SORTIE, m1.getStatut());
        assertEquals(StatutMouvement.SORTIE, m2.getStatut());
        verify(mouvementRepository, times(2)).save(any(Mouvement.class));
    }

    // 🧪 TEST 5 : calculerCUMP() avec mouvements
    @Test
    void testCalculerCUMP_WithMouvements() {
        Produit produit = new Produit();
        produit.setPrix_unitaire(50.0);

        Mouvement m1 = new Mouvement();
        m1.setProduit(produit);
        m1.setQuantite(2);

        Mouvement m2 = new Mouvement();
        m2.setProduit(produit);
        m2.setQuantite(3);

        List<Mouvement> mouvements = Arrays.asList(m1, m2);

        when(mouvementRepository.findByProduitIdAndStatut(1L, StatutMouvement.ENTREE))
                .thenReturn(mouvements);

        double result = mouvementService.calculerCUMP(1L);

        assertEquals(50.0, result); // (50*2 + 50*3) / (2+3) = 50
        verify(mouvementRepository).findByProduitIdAndStatut(1L, StatutMouvement.ENTREE);
    }

    // 🧪 TEST 6 : calculerCUMP() sans mouvements
    @Test
    void testCalculerCUMP_NoMouvements() {
        when(mouvementRepository.findByProduitIdAndStatut(1L, StatutMouvement.ENTREE))
                .thenReturn(Collections.emptyList());

        double result = mouvementService.calculerCUMP(1L);

        assertEquals(0.0, result);
    }

    // 🧪 Cas 1 : Calcul normal du coût total
    @Test
    void testCalculerCoutTotalApprovisionnement_Success() {
        Long produitId = 1L;

        // Mouvements de sortie (2 mouvements)
        Mouvement mvt1 = new Mouvement();
        mvt1.setQuantite(5);
        Mouvement mvt2 = new Mouvement();
        mvt2.setQuantite(10);

        // Simuler que le CUMP (coût moyen pondéré) est 20.0
        MouvementService spyService = Mockito.spy(mouvementService);
        doReturn(20.0).when(spyService).calculerCUMP(produitId);

        // Mock du repository : mouvements SORTIE
        when(mouvementRepository.findByProduitIdAndStatut(produitId, StatutMouvement.SORTIE))
                .thenReturn(List.of(mvt1, mvt2));

        // Appel de la méthode
        double result = spyService.calculerCoutTotalApprovisionnement(produitId);

        // Vérification du calcul : (5 + 10) * 20 = 300
        assertEquals(300.0, result);

        verify(mouvementRepository, times(1))
                .findByProduitIdAndStatut(produitId, StatutMouvement.SORTIE);
    }

    // 🧪 Cas 2 : Aucun mouvement SORTIE
    @Test
    void testCalculerCoutTotalApprovisionnement_AucunMouvement() {
        Long produitId = 1L;

        MouvementService spyService = Mockito.spy(mouvementService);
        doReturn(15.0).when(spyService).calculerCUMP(produitId);

        // Aucun mouvement dans le repository
        when(mouvementRepository.findByProduitIdAndStatut(produitId, StatutMouvement.SORTIE))
                .thenReturn(List.of());

        double result = spyService.calculerCoutTotalApprovisionnement(produitId);

        assertEquals(0.0, result);
        verify(mouvementRepository, times(1))
                .findByProduitIdAndStatut(produitId, StatutMouvement.SORTIE);
    }

}
