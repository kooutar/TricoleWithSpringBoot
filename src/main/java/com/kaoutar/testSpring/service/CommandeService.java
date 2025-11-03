package com.kaoutar.testSpring.service;

import com.kaoutar.testSpring.dto.CommandeDTO;
import com.kaoutar.testSpring.mapper.CommandeMapper;
import com.kaoutar.testSpring.model.Commande;
import com.kaoutar.testSpring.reposetry.CommandeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommandeService {
    private final CommandeRepository commandeRepository;
    private final CommandeMapper commandeMapper;

    @Transactional
    public CommandeDTO createCommande(CommandeDTO commandeDTO) {
        try {
            Commande commande = commandeMapper.toEntity(commandeDTO);
            Commande savedCommande = commandeRepository.save(commande);
            return commandeMapper.toDto(savedCommande);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de la commande: " + e.getMessage());
        }
    }




    public List<CommandeDTO> getAllCommandes() {
        List<Commande> commandes = commandeRepository.findAll();
        return commandes.stream()
                .map(commande -> {
                    // On ignore les mouvements pour éviter les problèmes de chargement
                    return commandeMapper.toDto(commande);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CommandeDTO getCommandeById(Long id) {
        return commandeRepository.findById(id)
                .map(commandeMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'ID: " + id));
    }

    @Transactional
    public CommandeDTO updateCommande(Long id, CommandeDTO commandeDTO) {
        Commande existingCommande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'ID: " + id));

        // Mise à jour des champs
        existingCommande.setQuntite(commandeDTO.getQuntite());
        existingCommande.setDate_commande(commandeDTO.getDateCommande());
        if (commandeDTO.getStatut() != null) {
            existingCommande.setStatut(commandeDTO.getStatut());
        }

        Commande updatedCommande = commandeRepository.save(existingCommande);
        return commandeMapper.toDto(updatedCommande);
    }

    @Transactional
    public void deleteCommande(Long id) {
        if (!commandeRepository.existsById(id)) {
            throw new RuntimeException("Commande non trouvée avec l'ID: " + id);
        }
        commandeRepository.deleteById(id);
    }
}
