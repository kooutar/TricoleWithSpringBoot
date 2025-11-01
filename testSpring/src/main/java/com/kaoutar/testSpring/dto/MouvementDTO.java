package com.kaoutar.testSpring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class MouvementDTO {
    private Long id;
    private String statut;
    private Integer quantite;
    private LocalDate dateMouvement;


    private Long commandeId;
    private Long produitId;
    private String produitNom;


}
