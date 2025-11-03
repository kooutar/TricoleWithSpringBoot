package com.kaoutar.testSpring.dto;

import com.kaoutar.testSpring.model.Commande;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProduitDTO {
    private Long id;
    private String nom;
    private Double prix_unitaire;
    private String categorie;
    private String description;




}
