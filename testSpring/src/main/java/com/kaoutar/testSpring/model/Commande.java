package com.kaoutar.testSpring.model;

import com.kaoutar.testSpring.enums.StatusCommande;
import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantite;
    private LocalDate dateCommande;
    private StatusCommande status =StatusCommande.ENTREE;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;
}
