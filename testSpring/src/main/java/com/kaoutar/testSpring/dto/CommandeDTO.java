package com.kaoutar.testSpring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommandeDTO {
    private Long id;
    private LocalDate dateCommande;
    private String statut;
    private Double montantTotal;
    private Integer quntite;


    private Long fournisseurId;


    private String fournisseurNom;

    private List<MouvementDTO> mouvements;
}
