package com.kaoutar.testSpring.mapper;

import com.kaoutar.testSpring.dto.MouvementDTO;
import com.kaoutar.testSpring.model.Mouvement;
import com.kaoutar.testSpring.model.Commande;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = "spring")
public interface MouvementMapper {
     @Mapping(source = "produitId" , target = "produit.id")
     @Mapping(source = "commandeId" , target = "commande.id")
     @Mapping(source = "produitNom", target = "produit.nom")
     Mouvement toEntity(MouvementDTO dto);
     @Mapping( source = "produit.id", target = "produitId")
     @Mapping( source = "commande.id", target = "commandeId")
     @Mapping( source = "produit.nom", target = "produitNom")
     MouvementDTO toDTO(Mouvement entity);

}
