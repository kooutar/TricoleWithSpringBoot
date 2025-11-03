package com.kaoutar.testSpring.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Integer qnte_stock;




}
