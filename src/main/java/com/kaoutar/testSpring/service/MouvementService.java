package com.kaoutar.testSpring.service;

import com.kaoutar.testSpring.dto.MouvementDTO;
import com.kaoutar.testSpring.mapper.MouvementMapper;
import com.kaoutar.testSpring.model.Commande;
import com.kaoutar.testSpring.model.Mouvement;
import com.kaoutar.testSpring.model.Produit;
import com.kaoutar.testSpring.reposetry.MouvementRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;



@AllArgsConstructor
@Service
public class MouvementService {

    public  final MouvementRepository mouvementRepository;
    public  final MouvementMapper mouvementMapper;

    public MouvementDTO save(MouvementDTO mouvementDTO, Object source) {
        if (source instanceof Produit) {
            Produit produit = (Produit) source;
            Mouvement mouvement = mouvementMapper.toEntity(mouvementDTO);
            mouvement.setProduit(produit);  // Set the product before saving
            Mouvement savedMouvement = mouvementRepository.save(mouvement);
            return mouvementMapper.toDto(savedMouvement);
        }
        if (source instanceof Commande) {
            Commande commande = (Commande) source;
            Mouvement mouvement = mouvementMapper.toEntity(mouvementDTO);
            mouvement.setCommande(commande);  // Set the commande before saving
            Mouvement savedMouvement = mouvementRepository.save(mouvement);
            return mouvementMapper.toDto(savedMouvement);
        }
        return null;
    }

}
