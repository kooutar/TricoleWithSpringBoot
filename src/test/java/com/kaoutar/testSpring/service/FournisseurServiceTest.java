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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

      // Entité après mise à jour
      Fournisseur updatedEntity = new Fournisseur();
      updatedEntity.setId(1L);
      updatedEntity.setSociete("Société Modifiée");
      updatedEntity.setAdresse("Nouvelle Adresse");
      updatedEntity.setContact("Nouveau Contact");
      updatedEntity.setEmail("modifie@email.com");
      updatedEntity.setTelephone("0123456789");
      updatedEntity.setVille("Rabat");
      updatedEntity.setIce("1234567890");

      // Configuration des mocks
      when(repo.findById(1L)).thenReturn(Optional.of(existingEntity));
      when(repo.save(any(Fournisseur.class))).thenReturn(updatedEntity);
      when(mapper.toDto(updatedEntity)).thenReturn(dto);

      // Act
      FournisseurDTO result = fournisseurService.updateFournisseur(1L, dto);

      // Assert
      assertNotNull(result);
      assertEquals("Société Modifiée", result.getSociete());
      assertEquals("Nouvelle Adresse", result.getAdresse());
      assertEquals("modifie@email.com", result.getEmail());

      // Vérifications
      verify(repo, times(1)).findById(1L);
      verify(repo, times(1)).save(any(Fournisseur.class));
      verify(mapper, times(1)).toDto(any(Fournisseur.class));


  }

}
