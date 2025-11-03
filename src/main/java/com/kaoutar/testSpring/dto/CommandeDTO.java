package com.kaoutar.testSpring.dto;

import com.kaoutar.testSpring.enums.StatusCommande;
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
    private StatusCommande statut;
    private Double totalCommande;
    private Integer quntite;
    private Long fournisseurId;
    private String fournisseurNom;
    private List<MouvementDTO> mouvements;


}
