package com.kaoutar.testSpring.mapper;


import com.kaoutar.testSpring.dto.MouvementDTO;
import com.kaoutar.testSpring.model.Mouvement;

import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MouvementMapper {

    @Mappings({
            @Mapping(source = "commande.id", target = "commandeId"),
            @Mapping(source = "produit.id", target = "produitId"),
            @Mapping(source = "produit.nom", target = "produitNom")
    })
    MouvementDTO toDto(Mouvement entity);

    List<MouvementDTO> toDtoList(List<Mouvement> entities);

    @Mappings({
            @Mapping(target = "commande", ignore = true),
            @Mapping(target = "produit", ignore = true)
    })
    Mouvement toEntity(MouvementDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "commande", ignore = true)
    @Mapping(target = "produit", ignore = true)
    void UpdateMouvementFromDTO(MouvementDTO dto, @MappingTarget Mouvement entity);
}
    

