package com.kaoutar.testSpring.service;

import com.kaoutar.testSpring.dto.MouvementDTO;
import com.kaoutar.testSpring.dto.ProduitDTO;
import com.kaoutar.testSpring.enums.StatutMouvement;
import com.kaoutar.testSpring.mapper.MouvementMapper;
import com.kaoutar.testSpring.mapper.ProduitMapper;
import com.kaoutar.testSpring.model.Commande;
import com.kaoutar.testSpring.model.Mouvement;
import com.kaoutar.testSpring.model.Produit;
import com.kaoutar.testSpring.reposetry.MouvementRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
@AllArgsConstructor
public class MouvementService {

    private final MouvementRepository mouvementRepository;
    private final MouvementMapper mouvementMapper;

    public MouvementDTO save(MouvementDTO mouvementDTO, Produit produit, Commande commande) {
        Mouvement mouvement = mouvementMapper.toEntity(mouvementDTO);
        if (produit != null) {
            mouvement.setProduit(produit);
        }
        if (commande != null) {
            mouvement.setCommande(commande);
        }
        Mouvement savedMouvement = mouvementRepository.save(mouvement);
        return mouvementMapper.toDto(savedMouvement);
    }

    public MouvementDTO createMouvementForProduit(MouvementDTO mouvementDTO, Produit produit) {
        return save(mouvementDTO, produit, null);
    }

    public MouvementDTO createMouvementForCommande(MouvementDTO mouvementDTO, Commande commande, Produit produit) {
        return save(mouvementDTO, produit, commande);
    }

    public void updateMouvementsForCommandeSortie(Commande commande) {
        List<Mouvement> mouvements = commande.getMouvements();
        for (Mouvement mouvement : mouvements) {
            mouvement.setStatut(StatutMouvement.SORTIE);
            mouvementRepository.save(mouvement);
        }
    }

}
