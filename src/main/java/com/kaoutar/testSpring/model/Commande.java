package com.kaoutar.testSpring.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kaoutar.testSpring.enums.StatusCommande;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Data
@Entity
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quntite;
    private LocalDate date_commande;
    private Double montant_total;
    @Enumerated(EnumType.STRING)
    private StatusCommande statut =StatusCommande.ENTREE;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Mouvement> mouvements = new ArrayList<>();

}
