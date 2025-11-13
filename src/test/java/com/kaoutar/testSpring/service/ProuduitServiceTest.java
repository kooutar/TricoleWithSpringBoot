package com.kaoutar.testSpring.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.kaoutar.testSpring.dto.MouvementDTO;
import com.kaoutar.testSpring.dto.ProduitDTO;
import com.kaoutar.testSpring.model.Produit;
import com.kaoutar.testSpring.enums.StatutMouvement;
import com.kaoutar.testSpring.mapper.ProduitMapper;


import com.kaoutar.testSpring.reposetry.ProduitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class ProduitServiceTest {

    @Mock
    private ProduitRepository repo;

    @Mock
    private ProduitMapper mapper;

    @Mock
    private MouvementService mouvementService;

    @Spy
    @InjectMocks
    private ProduitService produitService;

    private ProduitDTO produitDTO;
    private Produit produitEntity;

    @BeforeEach
    void setUp() {
        produitDTO = new ProduitDTO();
        produitDTO.setNom("Produit Test");
        produitDTO.setPrix_unitaire(100.0);
        produitDTO.setQnte_stock(10);

        produitEntity = new Produit();
        produitEntity.setId(1L);
        produitEntity.setNom("Produit Test");
        produitEntity.setPrix_unitaire(100.0);
        produitEntity.setQnte_stock(10);
    }

    @Test
    void save_WhenProductExists_ShouldUpdateQuantity() {
        // Given
        ProduitDTO existingProduitDTO = new ProduitDTO();
        existingProduitDTO.setId(1L);
        existingProduitDTO.setNom("Produit Test");
        existingProduitDTO.setPrix_unitaire(100.0);
        existingProduitDTO.setQnte_stock(20);

        Produit existingEntity = new Produit();
        existingEntity.setId(1L);
        existingEntity.setNom("Produit Test");
        existingEntity.setPrix_unitaire(100.0);
        existingEntity.setQnte_stock(30); // 20 + 10

        // Mock getProduitByName avec doReturn pour éviter l'appel réel
        doReturn(Arrays.asList(existingProduitDTO))
                .when(produitService).getProduitByName("Produit Test");

        when(mapper.toEntity(any(ProduitDTO.class))).thenReturn(existingEntity);
        when(repo.save(any(Produit.class))).thenReturn(existingEntity);
        when(mapper.toDto(any(Produit.class))).thenReturn(existingProduitDTO);

        // When
        ProduitDTO result = produitService.save(produitDTO);

        // Then
        assertNotNull(result);
        verify(repo, times(1)).save(any(Produit.class));
        verify(mouvementService, times(1))
                .createMouvementForProduit(any(MouvementDTO.class), any(Produit.class));
    }

    @Test
    void save_WhenProductExistsWithDifferentPrice_ShouldCreateNewProduct() {
        // Given
        ProduitDTO existingProduitDTO = new ProduitDTO();
        existingProduitDTO.setId(1L);
        existingProduitDTO.setNom("Produit Test");
        existingProduitDTO.setPrix_unitaire(150.0); // Prix différent
        existingProduitDTO.setQnte_stock(20);

        Produit newEntity = new Produit();
        newEntity.setId(2L);
        newEntity.setNom("Produit Test");
        newEntity.setPrix_unitaire(100.0);
        newEntity.setQnte_stock(10);

        // Mock getProduitByName
        doReturn(Arrays.asList(existingProduitDTO))
                .when(produitService).getProduitByName("Produit Test");

        when(mapper.toEntity(produitDTO)).thenReturn(newEntity);
        when(repo.save(newEntity)).thenReturn(newEntity);
        when(mapper.toDto(newEntity)).thenReturn(produitDTO);

        // When
        ProduitDTO result = produitService.save(produitDTO);

        // Then
        assertNotNull(result);
        verify(repo, times(1)).save(newEntity);
        verify(mouvementService, times(1))
                .save(any(MouvementDTO.class), eq(newEntity), isNull());
    }

    @Test
    void save_WhenProductDoesNotExist_ShouldCreateNewProduct() {
        // Given
        Produit newEntity = new Produit();
        newEntity.setId(1L);
        newEntity.setNom("Produit Test");
        newEntity.setPrix_unitaire(100.0);
        newEntity.setQnte_stock(10);

        // Mock getProduitByName returns empty list
        doReturn(Collections.emptyList())
                .when(produitService).getProduitByName("Produit Test");

        when(mapper.toEntity(produitDTO)).thenReturn(newEntity);
        when(repo.save(newEntity)).thenReturn(newEntity);
        when(mapper.toDto(newEntity)).thenReturn(produitDTO);

        // When
        ProduitDTO result = produitService.save(produitDTO);

        // Then
        assertNotNull(result);
        verify(repo, times(1)).save(newEntity);
        verify(mouvementService, times(1))
                .save(any(MouvementDTO.class), eq(newEntity), isNull());
    }

    @Test
    void save_ShouldCreateMouvementWithCorrectData() {
        // Given
        Produit newEntity = new Produit();
        newEntity.setId(1L);

        doReturn(Collections.emptyList())
                .when(produitService).getProduitByName(anyString());

        when(mapper.toEntity(produitDTO)).thenReturn(newEntity);
        when(repo.save(newEntity)).thenReturn(newEntity);
        when(mapper.toDto(newEntity)).thenReturn(produitDTO);

        // When
        produitService.save(produitDTO);

        // Then
        verify(mouvementService).save(
                argThat(mouvement ->
                        mouvement.getQuantite().equals(10) &&
                                mouvement.getStatut() == StatutMouvement.ENTREE &&
                                mouvement.getDateMouvement() != null &&
                                mouvement.getProduitId().equals(1L)
                ),
                eq(newEntity),
                isNull()
        );
    }

    @Test
    void save_WhenQuantityIsAdded_ShouldCalculateCorrectTotal() {
        // Given
        ProduitDTO existingProduitDTO = new ProduitDTO();
        existingProduitDTO.setId(1L);
        existingProduitDTO.setQnte_stock(50);
        existingProduitDTO.setPrix_unitaire(100.0);

        ProduitDTO newProduitDTO = new ProduitDTO();
        newProduitDTO.setNom("Produit Test");
        newProduitDTO.setPrix_unitaire(100.0);
        newProduitDTO.setQnte_stock(25);

        Produit updatedEntity = new Produit();
        updatedEntity.setId(1L);
        updatedEntity.setQnte_stock(75);

        doReturn(Arrays.asList(existingProduitDTO))
                .when(produitService).getProduitByName("Produit Test");

        when(mapper.toEntity(any(ProduitDTO.class))).thenReturn(updatedEntity);
        when(repo.save(any(Produit.class))).thenReturn(updatedEntity);
        when(mapper.toDto(any(Produit.class))).thenReturn(existingProduitDTO);

        // When
        produitService.save(newProduitDTO);

        // Then
        verify(repo).save(argThat(produit ->
                produit.getQnte_stock() == 75
        ));
    }

    @Test
    void save_WhenProductExistsSamePrice_ShouldCallCreateMouvementForProduit() {
        // Given
        ProduitDTO existingProduitDTO = new ProduitDTO();
        existingProduitDTO.setId(1L);
        existingProduitDTO.setPrix_unitaire(100.0);
        existingProduitDTO.setQnte_stock(20);

        Produit existingEntity = new Produit();
        existingEntity.setId(1L);

        doReturn(Arrays.asList(existingProduitDTO))
                .when(produitService).getProduitByName(anyString());

        when(mapper.toEntity(any())).thenReturn(existingEntity);
        when(repo.save(any())).thenReturn(existingEntity);
        when(mapper.toDto(any())).thenReturn(existingProduitDTO);

        // When
        produitService.save(produitDTO);

        // Then
        verify(mouvementService).createMouvementForProduit(
                argThat(mouvement ->
                        mouvement.getQuantite().equals(10) &&
                                mouvement.getStatut() == StatutMouvement.ENTREE
                ),
                eq(existingEntity)
        );
    }

    @Test
    void save_WhenNewProduct_ShouldCallSaveWithNullFournisseur() {
        // Given
        Produit newEntity = new Produit();
        newEntity.setId(1L);

        doReturn(Collections.emptyList())
                .when(produitService).getProduitByName(anyString());

        when(mapper.toEntity(any())).thenReturn(newEntity);
        when(repo.save(any())).thenReturn(newEntity);
        when(mapper.toDto(any())).thenReturn(produitDTO);

        // When
        produitService.save(produitDTO);

        // Then
        verify(mouvementService).save(
                any(MouvementDTO.class),
                eq(newEntity),
                isNull()
        );
    }
}

