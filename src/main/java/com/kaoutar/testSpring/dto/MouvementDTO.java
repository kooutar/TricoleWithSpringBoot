package com.kaoutar.testSpring.dto;
import com.kaoutar.testSpring.enums.StatutMouvement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MouvementDTO {
    private Long id;
    private StatutMouvement statut;
    private Integer quantite;
    private Date dateMouvement;
    private Long commandeId;
    private Long produitId;
    private String produitNom;
}
