package com.kaoutar.testSpring.service;
import com.kaoutar.testSpring.dto.CommandeDTO;
import com.kaoutar.testSpring.dto.MouvementDTO;
import com.kaoutar.testSpring.enums.StatusCommande;
import com.kaoutar.testSpring.enums.StatutMouvement;
import com.kaoutar.testSpring.mapper.CommandeMapper;
import com.kaoutar.testSpring.model.Commande;
import com.kaoutar.testSpring.model.Mouvement;
import com.kaoutar.testSpring.model.Produit;
import com.kaoutar.testSpring.reposetry.CommandeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class CommandeServiceTest {
    @Mock
    private CommandeRepository commandeRepository;

    @Mock
    private CommandeMapper commandeMapper;

    @Mock
    private MouvementService mouvementService;

    @Mock
    private ProduitService produitService;

    @InjectMocks
    @Spy
    private CommandeService commandeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 🧪 1️⃣ Test createCommande() - succès
    @Test
    void testCreateCommande_Success() {
        Long produitId = 1L;
        Produit produit = new Produit();
        produit.setId(produitId);
        produit.setNom("Clavier");
        produit.setQnte_stock(10);

        CommandeDTO commandeDTO = new CommandeDTO();
        commandeDTO.setQuntite(5);

        Commande commande = new Commande();
        commande.setId(1L);

        Commande savedCommande = new Commande();
        savedCommande.setId(1L);

        CommandeDTO savedCommandeDTO = new CommandeDTO();
        savedCommandeDTO.setId(1L);

        // Mock des dépendances
        when(produitService.findById(produitId)).thenReturn(Optional.of(produit));
        when(commandeMapper.toEntity(commandeDTO)).thenReturn(commande);
        when(commandeRepository.save(commande)).thenReturn(savedCommande);
        when(commandeMapper.toDto(savedCommande)).thenReturn(savedCommandeDTO);

        // Appel méthode
        CommandeDTO result = commandeService.createCommande(commandeDTO, produitId);

        // Vérifications
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(mouvementService, times(1)).createMouvementForCommande(any(MouvementDTO.class), eq(savedCommande), eq(produit));
        verify(produitService, times(1)).saveProduit(produit);
    }

    // 🧪 2️⃣ Test createCommande() - stock insuffisant
    @Test
    void testCreateCommande_StockInsuffisant() {
        Long produitId = 1L;
        Produit produit = new Produit();
        produit.setId(produitId);
        produit.setNom("Clavier");
        produit.setQnte_stock(2);

        CommandeDTO commandeDTO = new CommandeDTO();
        commandeDTO.setQuntite(5);

        when(produitService.findById(produitId)).thenReturn(Optional.of(produit));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> commandeService.createCommande(commandeDTO, produitId));

        assertTrue(ex.getMessage().contains("Stock insuffisant"));
        verify(commandeRepository, never()).save(any());
    }

    // 🧪 3️⃣ Test getCommandeById()
    @Test
    void testGetCommandeById_Success() {
        Commande commande = new Commande();
        commande.setId(1L);
        CommandeDTO commandeDTO = new CommandeDTO();
        commandeDTO.setId(1L);

        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));
        when(commandeMapper.toDto(commande)).thenReturn(commandeDTO);

        CommandeDTO result = commandeService.getCommandeById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(commandeRepository, times(1)).findById(1L);
    }

    // 🧪 4️⃣ Test deleteCommande() - commande existante
    @Test
    void testDeleteCommande_Success() {
        when(commandeRepository.existsById(1L)).thenReturn(true);

        commandeService.deleteCommande(1L);

        verify(commandeRepository, times(1)).deleteById(1L);
    }

    // 🧪 5️⃣ Test deleteCommande() - commande non trouvée
    @Test
    void testDeleteCommande_NotFound() {
        when(commandeRepository.existsById(1L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> commandeService.deleteCommande(1L));

        assertTrue(ex.getMessage().contains("non trouvée"));
        verify(commandeRepository, never()).deleteById(1L);
    }

    // 🧪 6️⃣ Test getAllCommandes(Pageable)
    @Test
    void testGetAllCommandes_Pageable() {
        Pageable pageable = PageRequest.of(0, 5);
        Commande commande = new Commande();
        CommandeDTO commandeDTO = new CommandeDTO();

        Page<Commande> page = new PageImpl<>(List.of(commande));

        when(commandeRepository.findAll(pageable)).thenReturn(page);
        when(commandeMapper.toDto(commande)).thenReturn(commandeDTO);

        Page<CommandeDTO> result = commandeService.getAllCommandes(pageable);

        assertEquals(1, result.getContent().size());
        verify(commandeRepository, times(1)).findAll(pageable);
    }
    // 🧪 1️⃣ Cas : mise à jour sans changement de statut ni quantité
    @Test
    void testUpdateCommande_SimpleUpdate() {
        Long id = 1L;
        Commande existing = new Commande();
        existing.setId(id);
        existing.setQuntite(5);
        existing.setStatut(StatusCommande.ENTREE);
        existing.setMouvements(List.of());

        CommandeDTO dto = new CommandeDTO();
        dto.setQuntite(5); // même quantité
        dto.setStatut(StatusCommande.ENTREE);

        Commande updated = new Commande();
        updated.setId(id);

        CommandeDTO updatedDto = new CommandeDTO();
        updatedDto.setId(id);

        when(commandeRepository.findById(id)).thenReturn(Optional.of(existing));
        when(commandeRepository.save(existing)).thenReturn(updated);
        when(commandeMapper.toDto(updated)).thenReturn(updatedDto);

        CommandeDTO result = commandeService.updateCommande(id, dto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(mouvementService, never()).updateMouvementsForCommandeSortie(any());
        verify(commandeRepository, times(1)).save(existing);
    }

    // 🧪 2️⃣ Cas : changement de statut → SORTIE
    @Test
    void testUpdateCommande_ChangementEnSortie() {
        Long id = 1L;
        Commande existing = new Commande();
        existing.setId(id);
        existing.setQuntite(5);
        existing.setStatut(StatusCommande.ENTREE);
        existing.setMouvements(List.of());

        CommandeDTO dto = new CommandeDTO();
        dto.setQuntite(5);
        dto.setStatut(StatusCommande.SORTIE); // changement vers SORTIE

        Commande updated = new Commande();
        updated.setId(id);
        updated.setStatut(StatusCommande.SORTIE);

        CommandeDTO updatedDto = new CommandeDTO();
        updatedDto.setId(id);

        when(commandeRepository.findById(id)).thenReturn(Optional.of(existing));
        when(commandeRepository.save(existing)).thenReturn(updated);
        when(commandeMapper.toDto(updated)).thenReturn(updatedDto);

        CommandeDTO result = commandeService.updateCommande(id, dto);

        assertNotNull(result);
        verify(mouvementService, times(1)).updateMouvementsForCommandeSortie(updated);
        verify(commandeRepository, times(1)).save(existing);
    }

    // 🧪 3️⃣ Cas : changement de quantité
    @Test
    void testUpdateCommande_ChangementQuantite() {
        Long id = 1L;

        Produit produit = new Produit();
        produit.setNom("Souris");

        Mouvement mouvement = new Mouvement();
        mouvement.setProduit(produit);

        Commande existing = new Commande();
        existing.setId(id);
        existing.setQuntite(5);
        existing.setStatut(StatusCommande.ENTREE);
        existing.setMouvements(List.of(mouvement));

        CommandeDTO dto = new CommandeDTO();
        dto.setQuntite(8); // changement de quantité
        dto.setStatut(StatusCommande.ENTREE);

        Commande updated = new Commande();
        updated.setId(id);

        CommandeDTO updatedDto = new CommandeDTO();
        updatedDto.setId(id);

        when(commandeRepository.findById(id)).thenReturn(Optional.of(existing));
        when(commandeRepository.save(existing)).thenReturn(updated);
        when(commandeMapper.toDto(updated)).thenReturn(updatedDto);

        // On espionne la méthode updateProduitStock
        doNothing().when(commandeService).updateProduitStock(any(Produit.class), anyInt(), anyBoolean());

        CommandeDTO result = commandeService.updateCommande(id, dto);

        assertNotNull(result);
        verify(commandeService, times(1)).updateProduitStock(produit, 3, false);
        verify(commandeRepository, times(1)).save(existing);
    }

    // 🧪 Cas 1 : Stock existant, entrée (ajout)
    @Test
    void testUpdateProduitStock_Entree_AvecStock() {
        Produit produit = new Produit();
        produit.setNom("PC");
        produit.setQnte_stock(10);

        commandeService.updateProduitStock(produit, 5, true);

        assertEquals(15, produit.getQnte_stock());
        verify(produitService, times(1)).saveProduit(produit);
    }

    @Test
    void testUpdateProduitStock_Sortie_AvecStock() {
        Produit produit = new Produit();
        produit.setNom("PC");
        produit.setQnte_stock(10);

        commandeService.updateProduitStock(produit, 4, false);

        assertEquals(6, produit.getQnte_stock());
        verify(produitService, times(1)).saveProduit(produit);
    }

    // 🧪 Cas 3 : Stock null, entrée
    @Test
    void testUpdateProduitStock_Entree_StockNull() {
        Produit produit = new Produit();
        produit.setNom("Clavier");
        produit.setQnte_stock(null); // stock null

        commandeService.updateProduitStock(produit, 8, true);

        assertEquals(8, produit.getQnte_stock());
        verify(produitService, times(1)).saveProduit(produit);
    }

    // 🧪 Cas 4 : Stock insuffisant (sortie trop grande)
    @Test
    void testUpdateProduitStock_StockInsuffisant() {
        Produit produit = new Produit();
        produit.setNom("Souris");
        produit.setQnte_stock(3);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                commandeService.updateProduitStock(produit, 5, false));

        assertTrue(ex.getMessage().contains("Stock insuffisant"));
        verify(produitService, never()).saveProduit(any());
    }

}
