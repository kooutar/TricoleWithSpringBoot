package com.kaoutar.testSpring.service;

import com.kaoutar.testSpring.TestSpringApplication;
import com.kaoutar.testSpring.dto.FournisseurDTO;
import com.kaoutar.testSpring.mapper.FournisseurMapper;
import com.kaoutar.testSpring.model.Fournisseur;
import com.kaoutar.testSpring.reposetry.FournisseurRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.*;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FournisseurServiceTest {

    @Mock
    private FournisseurRepository repo;

    @Mock
    private FournisseurMapper mapper;

    @InjectMocks
    private FournisseurService fournisseurService;

    @Test
    void testSaveFournisseur() {

        FournisseurDTO dto = new FournisseurDTO();
        dto.setSociete("Société X");
        dto.setEmail("emailX@gmail.com");

        Fournisseur entity = new Fournisseur();
        entity.setSociete("Société X");
        entity.setEmail("emailX@gmail.com");

       when(mapper.toEntity(dto)).thenReturn(entity);
       when(repo.save(entity)).thenReturn(entity);
       when(mapper.toDto(entity)).thenReturn(dto);


        FournisseurDTO saved = fournisseurService.save(dto);

        assertNotNull(saved);
        assertEquals("Société X",saved.getSociete());
    }

  @Test
    void testGetFournisseurById(){
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setId(1L);
        fournisseur.setSociete("TestSociete");

        FournisseurDTO dto = new FournisseurDTO();
        dto.setId(1L);
        dto.setSociete("TestSociete");

        when(repo.findById(1L)).thenReturn(Optional.of(fournisseur));
        when(mapper.toDto(fournisseur)).thenReturn(dto);

        FournisseurDTO result= fournisseurService.getFournisseurById(1L);
        assertEquals(1L,result.getId());
        assertEquals("TestSociete",result.getSociete());
      verify(repo,times(1)).findById(1L);
  }

  @Test
    void  testDeleteFournisseur(){
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setId(1L);

        when(repo.findById(1L)).thenReturn(Optional.of(fournisseur));
        String message = fournisseurService.deleteFournisseur(1L);
        assertEquals("delete with success", message);
        verify(repo).delete(fournisseur);
  }
  @Test
    void testUpdateFournisseur(){
      FournisseurDTO dto = new FournisseurDTO();
      dto.setSociete("Société Modifiée");
      dto.setAdresse("Nouvelle Adresse");
      dto.setContact("Nouveau Contact");
      dto.setEmail("modifie@email.com");
      dto.setTelephone("0123456789");
      dto.setVille("Rabat");
      dto.setIce("1234567890");

      // Entité existante en base
      Fournisseur existingEntity = new Fournisseur();
      existingEntity.setId(1L);
      existingEntity.setSociete("Ancienne société");

      Fournisseur updatedEntity = new Fournisseur();
      updatedEntity.setId(1L);
      updatedEntity.setSociete("Société Modifiée");
      updatedEntity.setAdresse("Nouvelle Adresse");
      updatedEntity.setContact("Nouveau Contact");
      updatedEntity.setEmail("modifie@email.com");
      updatedEntity.setTelephone("0123456789");
      updatedEntity.setVille("Rabat");
      updatedEntity.setIce("1234567890");


      when(repo.findById(1L)).thenReturn(Optional.of(existingEntity));
      when(repo.save(any(Fournisseur.class))).thenReturn(updatedEntity);
      when(mapper.toDto(updatedEntity)).thenReturn(dto);


      FournisseurDTO result = fournisseurService.updateFournisseur(1L, dto);


      assertNotNull(result);
      assertEquals("Société Modifiée", result.getSociete());
      assertEquals("Nouvelle Adresse", result.getAdresse());
      assertEquals("modifie@email.com", result.getEmail());

      verify(repo, times(1)).findById(1L);
      verify(repo, times(1)).save(any(Fournisseur.class));
      verify(mapper, times(1)).toDto(any(Fournisseur.class));


  }

    @Test
    void testGetAllFournisseurs() {
        // ========== ARRANGE - Préparation des données ==========

        // 1. Création d'un Pageable (paramètres de pagination)
        Pageable pageable = PageRequest.of(0, 10, Sort.by("societe").ascending());
        // Page 0, taille 10, tri par société ascendant

        // 2. Création de fournisseurs de test (entités)
        Fournisseur fournisseur1 = new Fournisseur();
        fournisseur1.setId(1L);
        fournisseur1.setSociete("Société A");
        fournisseur1.setEmail("societeA@email.com");
        fournisseur1.setVille("Casablanca");

        Fournisseur fournisseur2 = new Fournisseur();
        fournisseur2.setId(2L);
        fournisseur2.setSociete("Société B");
        fournisseur2.setEmail("societeB@email.com");
        fournisseur2.setVille("Rabat");

        Fournisseur fournisseur3 = new Fournisseur();
        fournisseur3.setId(3L);
        fournisseur3.setSociete("Société C");
        fournisseur3.setEmail("societeC@email.com");
        fournisseur3.setVille("Marrakech");

        // 3. Création d'une liste de fournisseurs
        List<Fournisseur> fournisseurList = Arrays.asList(fournisseur1, fournisseur2, fournisseur3);

        // 4. Création d'une Page mockée (simulation de la réponse du repository)
        Page<Fournisseur> fournisseurPage = new PageImpl<>(fournisseurList, pageable, fournisseurList.size());
        // PageImpl(contenu, pageable, total d'éléments)

        // 5. Création des DTOs correspondants
        FournisseurDTO dto1 = new FournisseurDTO();
        dto1.setId(1L);
        dto1.setSociete("Société A");
        dto1.setEmail("societeA@email.com");

        FournisseurDTO dto2 = new FournisseurDTO();
        dto2.setId(2L);
        dto2.setSociete("Société B");
        dto2.setEmail("societeB@email.com");

        FournisseurDTO dto3 = new FournisseurDTO();
        dto3.setId(3L);
        dto3.setSociete("Société C");
        dto3.setEmail("societeC@email.com");

        // 6. Configuration des mocks
        when(repo.findAll(pageable)).thenReturn(fournisseurPage);

        // Mock du mapper pour chaque fournisseur
        when(mapper.toDto(fournisseur1)).thenReturn(dto1);
        when(mapper.toDto(fournisseur2)).thenReturn(dto2);
        when(mapper.toDto(fournisseur3)).thenReturn(dto3);

        // ========== ACT - Exécution de la méthode à tester ==========
        Page<FournisseurDTO> result = fournisseurService.getAllFournisseurs(pageable);

        // ========== ASSERT - Vérifications ==========

        // 1. Vérifier que le résultat n'est pas null
        assertNotNull(result);

        // 2. Vérifier le nombre d'éléments dans la page
        assertEquals(3, result.getContent().size());

        // 3. Vérifier le nombre total d'éléments
        assertEquals(3, result.getTotalElements());

        // 4. Vérifier le nombre total de pages
        assertEquals(1, result.getTotalPages());

        // 5. Vérifier qu'on est sur la première page
        assertEquals(0, result.getNumber());

        // 6. Vérifier la taille de la page
        assertEquals(10, result.getSize());

        // 7. Vérifier le contenu des DTOs
        List<FournisseurDTO> content = result.getContent();
        assertEquals("Société A", content.get(0).getSociete());
        assertEquals("Société B", content.get(1).getSociete());
        assertEquals("Société C", content.get(2).getSociete());

        // 8. Vérifier les IDs
        assertEquals(1L, content.get(0).getId());
        assertEquals(2L, content.get(1).getId());
        assertEquals(3L, content.get(2).getId());

        // ========== VERIFY - Vérification des appels aux mocks ==========

        // Vérifier que findAll a été appelé avec le bon Pageable
        verify(repo, times(1)).findAll(pageable);

        // Vérifier que le mapper a été appelé 3 fois (une fois par fournisseur)
        verify(mapper, times(3)).toDto(any(Fournisseur.class));
    }
    @Test
    void testDeleteFournisseur_WhenNotExist() {
        // GIVEN
        Long id = 1L;
        when(repo.findById(id)).thenReturn(Optional.empty());

        // WHEN
        String result = fournisseurService.deleteFournisseur(id);

        // THEN
        assertEquals("supplier don't exist", result);
        verify(repo, never()).delete(any(Fournisseur.class));
    }

    @Test
    void testSearchFournisseurs() {
        // GIVEN
        String societe = "tech";
        Pageable pageable = PageRequest.of(0, 2);

        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setId(1L);
        fournisseur.setSociete("TechCorp");

        FournisseurDTO dto = new FournisseurDTO();
        dto.setId(1L);
        dto.setSociete("TechCorp");

        Page<Fournisseur> fournisseurPage = new PageImpl<>(List.of(fournisseur));
        when(repo.findBySocieteContainingIgnoreCase(societe, pageable)).thenReturn(fournisseurPage);
        when(mapper.toDto(fournisseur)).thenReturn(dto);

        // WHEN
        Page<FournisseurDTO> result = fournisseurService.searchFournisseurs(societe, pageable);

        // THEN
        assertEquals(1, result.getContent().size());
        assertEquals("TechCorp", result.getContent().get(0).getSociete());
        verify(repo, times(1)).findBySocieteContainingIgnoreCase(societe, pageable);
        verify(mapper, times(1)).toDto(fournisseur);
    }
    @Test
    void testGetFournisseurById_WhenNotExist_ShouldThrowException() {
        // GIVEN
        Long id = 99L;
        when(repo.findById(id)).thenReturn(Optional.empty());

        // WHEN + THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fournisseurService.getFournisseurById(id);
        });

        // Vérifie le message de l'exception
        assertEquals("Fournisseur non trouvé avec l'ID : " + id, exception.getMessage());

        // Vérifie que mapper.toDto() n'a pas été appelé
        verify(mapper, never()).toDto(any());
    }

    @Test
    void testUpdateFournisseur_WhenNotExist_ShouldThrowException() {
        // GIVEN
        Long id = 10L;
        FournisseurDTO dto = new FournisseurDTO();
        dto.setSociete("Nouvelle Société");

        when(repo.findById(id)).thenReturn(Optional.empty());

        // WHEN + THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fournisseurService.updateFournisseur(id, dto);
        });

        // ✅ Vérifie le message exact de l’exception
        assertEquals("Fournisseur non trouvé avec l'ID : " + id, exception.getMessage());

        // ✅ Vérifie qu’aucune autre méthode n’a été appelée
        verify(mapper, never()).updateFournisseurFromDto(any(), any());
        verify(repo, never()).save(any());
        verify(mapper, never()).toDto(any());
    }






}
