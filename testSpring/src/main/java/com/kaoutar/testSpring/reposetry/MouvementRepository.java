package com.kaoutar.testSpring.reposetry;

import com.kaoutar.testSpring.model.Mouvement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MouvementRepository extends JpaRepository<Mouvement,Long> {
}
