package com.kaoutar.testSpring.reposetry;

import com.kaoutar.testSpring.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
}

