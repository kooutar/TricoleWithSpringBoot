package com.kaoutar.testSpring.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.kaoutar.testSpring.dto.MouvementDTO;
import com.kaoutar.testSpring.dto.ProduitDTO;
import com.kaoutar.testSpring.model.Produit;
import com.kaoutar.testSpring.enums.StatutMouvement;
import com.kaoutar.testSpring.mapper.ProduitMapper;


import com.kaoutar.testSpring.reposetry.ProduitRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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


    }

    @Test
    void testGetAllProduits() {
        // 1. Préparer les données
        Produit produit1 = new Produit();
        produit1.setId(1L);
        produit1.setNom("Produit1");

        Produit produit2 = new Produit();
        produit2.setId(2L);
        produit2.setNom("Produit2");

        List<Produit> produits = List.of(produit1, produit2);
        Page<Produit> pageProduits = new PageImpl<>(produits);

        // 2. Mock le repo pour renvoyer la page
        Pageable pageable = PageRequest.of(0, 10);
        when(repo.findAll(pageable)).thenReturn(pageProduits);

        // 3. Mock le mapper pour transformer les entités en DTO
        ProduitDTO dto1 = new ProduitDTO();
        dto1.setId(1L);
        dto1.setNom("Produit1");

        ProduitDTO dto2 = new ProduitDTO();
        dto2.setId(2L);
        dto2.setNom("Produit2");

        when(mapper.toDto(produit1)).thenReturn(dto1);
        when(mapper.toDto(produit2)).thenReturn(dto2);

        // 4. Appeler la méthode
        Page<ProduitDTO> result = produitService.getAllProduits(pageable);

        // 5. Vérifier les résultats
        assertEquals(2, result.getContent().size());
        assertEquals("Produit1", result.getContent().get(0).getNom());
        assertEquals("Produit2", result.getContent().get(1).getNom());

        // Vérifier que repo.findAll a été appelé une seule fois
        verify(repo, times(1)).findAll(pageable);
    }
    @Test
    void testDeleteProduit_Exists() {
        Long id = 1L;
        Produit produitExistant = new Produit();
        produitExistant.setId(id);

        // Mock repo.findById pour renvoyer le produit
        when(repo.findById(id)).thenReturn(Optional.of(produitExistant));

        // Appeler la méthode
        String result = produitService.deleteProduit(id);

        // Vérifications
        assertEquals("delete with succes", result);
        verify(repo, times(1)).findById(id);
        verify(repo, times(1)).delete(produitExistant);
    }

    @Test
    void testDeleteProduit_NotExists() {
        Long id = 1L;

        // Mock repo.findById pour renvoyer vide
        when(repo.findById(id)).thenReturn(Optional.empty());

        // Appeler la méthode
        String result = produitService.deleteProduit(id);

        // Vérifications
        assertEquals("product don't exist", result);
        verify(repo, times(1)).findById(id);
        verify(repo, never()).delete(any());
    }

    @Test
    void testGetProduitById_Exists() {
        Long id = 1L;
        Produit produit = new Produit();
        produit.setId(id);
        produit.setNom("Produit1");

        ProduitDTO produitDTO = new ProduitDTO();
        produitDTO.setId(id);
        produitDTO.setNom("Produit1");

        // Mock repo.findById
        when(repo.findById(id)).thenReturn(Optional.of(produit));

        // Mock mapper
        when(mapper.toDto(produit)).thenReturn(produitDTO);

        // Appeler la méthode
        ProduitDTO result = produitService.getProduitById(id);

        // Vérifications
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Produit1", result.getNom());

        verify(repo, times(1)).findById(id);
        verify(mapper, times(1)).toDto(produit);
    }

    @Test
    void testGetProduitById_NotExists() {
        Long id = 1L;

        // Mock repo.findById pour retourner vide
        when(repo.findById(id)).thenReturn(Optional.empty());

        // Vérifier que l'exception est bien levée
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> produitService.getProduitById(id));

        assertEquals("Produit non trouvé avec l'ID : " + id, exception.getMessage());
        verify(repo, times(1)).findById(id);
        verify(mapper, never()).toDto(any());
    }
    // 🧪 Cas 1 : le produit existe et est bien mis à jour
    @Test
    void testUpdateProduit_Success() {
        // --- 1. Données simulées ---
        Long id = 1L;

        Produit ancienProduit = new Produit();
        ancienProduit.setId(id);
        ancienProduit.setNom("Ancien Nom");

        ProduitDTO produitDTO = new ProduitDTO();
        produitDTO.setNom("Nouveau Nom");

        Produit produitMisAJour = new Produit();
        produitMisAJour.setId(id);
        produitMisAJour.setNom("Nouveau Nom");

        ProduitDTO produitDTOResult = new ProduitDTO();
        produitDTOResult.setId(id);
        produitDTOResult.setNom("Nouveau Nom");

        // --- 2. Comportement des mocks ---
        when(repo.findById(id)).thenReturn(Optional.of(ancienProduit)); // simulate findById
        doNothing().when(mapper).UpdateProduitFromDTO(produitDTO, ancienProduit); // simulate update (void)
        when(repo.save(ancienProduit)).thenReturn(produitMisAJour); // simulate save
        when(mapper.toDto(produitMisAJour)).thenReturn(produitDTOResult); // simulate mapping to DTO

        // --- 3. Appel de la méthode testée ---
        ProduitDTO result = produitService.updateProduit(id, produitDTO);

        // --- 4. Vérifications ---
        assertNotNull(result);
        assertEquals("Nouveau Nom", result.getNom());
        assertEquals(id, result.getId());

        verify(repo, times(1)).findById(id);
        verify(mapper, times(1)).UpdateProduitFromDTO(produitDTO, ancienProduit);
        verify(repo, times(1)).save(ancienProduit);
        verify(mapper, times(1)).toDto(produitMisAJour);
    }

    // 🧪 Cas 2 : le produit n’existe pas → exception levée
    @Test
    void testUpdateProduit_NotFound() {
        Long id = 99L;
        ProduitDTO produitDTO = new ProduitDTO();

        when(repo.findById(id)).thenReturn(Optional.empty()); // produit introuvable

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> produitService.updateProduit(id, produitDTO));

        assertEquals("produit non trouvé avec l'ID : " + id, exception.getMessage());

        verify(repo, times(1)).findById(id);
        verify(mapper, never()).UpdateProduitFromDTO(any(), any());
        verify(repo, never()).save(any());
    }

    // 🧪 Cas 1 : Produit trouvé
    @Test
    void testGetProduitByName_Found() {
        // --- 1. Données simulées ---
        String nom = "Ordinateur";
        Produit produit = new Produit();
        produit.setId(1L);
        produit.setNom(nom);

        ProduitDTO dto = new ProduitDTO();
        dto.setId(1L);
        dto.setNom(nom);

        // --- 2. Mock des comportements ---
        when(repo.findByNom(nom)).thenReturn(Optional.of(produit));
        when(mapper.toDto(produit)).thenReturn(dto);

        // --- 3. Appel de la méthode ---
        List<ProduitDTO> result = produitService.getProduitByName(nom);

        // --- 4. Vérifications ---
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Ordinateur", result.get(0).getNom());

        verify(repo, times(1)).findByNom(nom);
        verify(mapper, times(1)).toDto(produit);
    }

    // 🧪 Cas 2 : Produit non trouvé
    @Test
    void testGetProduitByName_NotFound() {
        String nom = "Inexistant";

        // --- 1. Mock du repo pour retourner vide ---
        when(repo.findByNom(nom)).thenReturn(Optional.empty());

        // --- 2. Appel de la méthode ---
        List<ProduitDTO> result = produitService.getProduitByName(nom);

        // --- 3. Vérifications ---
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repo, times(1)).findByNom(nom);
        verify(mapper, never()).toDto(any());
    }

    // 🧪 Cas 1 : Produit trouvé
    @Test
    void testFindById_Found() {
        Long id = 1L;
        Produit produit = new Produit();
        produit.setId(id);
        produit.setNom("Ordinateur");

        // Simuler le retour du repo
        when(repo.findById(id)).thenReturn(Optional.of(produit));

        // Appel de la méthode
        Optional<Produit> result = produitService.findById(id);

        // Vérifications
        assertTrue(result.isPresent());
        assertEquals("Ordinateur", result.get().getNom());
        verify(repo, times(1)).findById(id);
    }

    // 🧪 Cas 2 : Produit non trouvé → exception
    @Test
    void testFindById_NotFound() {
        Long id = 99L;

        // Simuler un Optional vide
        when(repo.findById(id)).thenReturn(Optional.empty());

        // Vérifier que l'exception est levée
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> produitService.findById(id));

        assertEquals("Produit non trouvé avec l'ID : " + id, exception.getMessage());
        verify(repo, times(1)).findById(id);
    }

    // 🧪 Cas 3 : Sauvegarde d’un produit
    @Test
    void testSaveProduit() {
        Produit produit = new Produit();
        produit.setNom("Clavier");

        Produit savedProduit = new Produit();
        savedProduit.setId(1L);
        savedProduit.setNom("Clavier");

        // Mock du comportement
        when(repo.save(produit)).thenReturn(savedProduit);

        // Appel de la méthode
        Produit result = produitService.saveProduit(produit);

        // Vérifications
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Clavier", result.getNom());

        verify(repo, times(1)).save(produit);
    }
}

