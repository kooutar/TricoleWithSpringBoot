package com.kaoutar.testSpring.reposetry;

import com.kaoutar.testSpring.model.Produit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
    Optional<Produit> findByNom(String nom);
    Page<Produit> findByNomContainingIgnoreCase(String nom, Pageable pageable);
}
