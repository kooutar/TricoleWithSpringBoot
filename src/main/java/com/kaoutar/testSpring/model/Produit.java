package com.kaoutar.testSpring.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private Double prix_unitaire;
    private String categorie;
    private String  description;

    // Relation Many-to-Many via Mouvement
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Mouvement> mouvements;
}