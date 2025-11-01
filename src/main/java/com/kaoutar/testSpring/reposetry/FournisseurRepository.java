package com.kaoutar.testSpring.reposetry;

import com.kaoutar.testSpring.model.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur , Long> {
}
