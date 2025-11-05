package com.kaoutar.testSpring.service;

import com.kaoutar.testSpring.dto.CommandeDTO;
import com.kaoutar.testSpring.dto.MouvementDTO;

import com.kaoutar.testSpring.enums.StatusCommande;
import com.kaoutar.testSpring.enums.StatutMouvement;
import com.kaoutar.testSpring.mapper.CommandeMapper;
import com.kaoutar.testSpring.model.Commande;

import com.kaoutar.testSpring.model.Produit;
import com.kaoutar.testSpring.reposetry.CommandeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommandeService {
    private final CommandeRepository commandeRepository;
    private final CommandeMapper commandeMapper;
    private  final  MouvementService mouvementService;

    private final ProduitService produitService;

    @Transactional
    public CommandeDTO createCommande(CommandeDTO commandeDTO, Long produitId) {
        try {

            Produit produit = produitService.findById(produitId)
                    .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID: " + produitId));


            Commande commande = commandeMapper.toEntity(commandeDTO);
            Commande savedCommande = commandeRepository.save(commande);


            MouvementDTO mouvementDTO = new MouvementDTO();
            mouvementDTO.setQuantite(commandeDTO.getQuntite());
            mouvementDTO.setStatut(StatutMouvement.AJUSTEMENT);
            mouvementDTO.setDateMouvement(new Date());
            mouvementDTO.setProduitId(produitId);

            mouvementService.createMouvementForCommande(mouvementDTO, savedCommande, produit);

            return commandeMapper.toDto(savedCommande);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de la commande: " + e.getMessage());
        }
    }




    public Page<CommandeDTO> getAllCommandes(Pageable pageable) {
        Page<Commande> commandePage = commandeRepository.findAll(pageable);
        return commandePage.map(commandeMapper::toDto);
    }

    @Transactional(readOnly = true)
    public CommandeDTO getCommandeById(Long id) {
        return commandeRepository.findById(id)
                .map(commandeMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'ID: " + id));
    }

    @Transactional
    public CommandeDTO updateCommande(Long id, CommandeDTO commandeDTO) {
        try {
            Commande existingCommande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'ID: " + id));

            // Vérifier si le statut change en SORTIE
            boolean changementEnSortie = commandeDTO.getStatut() != null &&
                                       commandeDTO.getStatut().equals(StatusCommande.SORTIE) &&
                                       !existingCommande.getStatut().equals(StatusCommande.SORTIE);

            // Mise à jour des champs de la commande
            existingCommande.setQuntite(commandeDTO.getQuntite());
            existingCommande.setDate_commande(commandeDTO.getDateCommande());
            if (commandeDTO.getStatut() != null) {
                existingCommande.setStatut(commandeDTO.getStatut());
            }

            // Sauvegarder la commande
            Commande updatedCommande = commandeRepository.save(existingCommande);

            // Si le statut change en SORTIE, mettre à jour les mouvements
            if (changementEnSortie) {
                mouvementService.updateMouvementsForCommandeSortie(updatedCommande);
            }

            return commandeMapper.toDto(updatedCommande);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la commande: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteCommande(Long id) {
        if (!commandeRepository.existsById(id)) {
            throw new RuntimeException("Commande non trouvée avec l'ID: " + id);
        }
        commandeRepository.deleteById(id);
    }


}
