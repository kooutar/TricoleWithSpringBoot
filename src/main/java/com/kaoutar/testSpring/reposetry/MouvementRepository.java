package com.kaoutar.testSpring.reposetry;

import com.kaoutar.testSpring.enums.StatutMouvement;
import com.kaoutar.testSpring.model.Mouvement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MouvementRepository extends JpaRepository<Mouvement,Long> {
    List<Mouvement> findByProduitIdAndStatut(Long produitId, StatutMouvement statut);
}
