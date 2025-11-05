package com.kaoutar.testSpring.reposetry;

import com.kaoutar.testSpring.model.Fournisseur;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {
    Page<Fournisseur> findBySocieteContainingIgnoreCase(String societe, Pageable pageable);
}
