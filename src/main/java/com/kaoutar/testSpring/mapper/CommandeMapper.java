package com.kaoutar.testSpring.mapper;

import com.kaoutar.testSpring.dto.CommandeDTO;
import com.kaoutar.testSpring.model.Commande;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {MouvementMapper.class})
public interface CommandeMapper {

    @Mappings({
        @Mapping(source = "date_commande", target = "dateCommande"),
        @Mapping(source = "quntite", target = "quntite"),
        @Mapping(source = "statut", target = "statut"),
            @Mapping(source = "montant_total" , target = "totalCommande"),
        @Mapping(source = "fournisseur.id", target = "fournisseurId"),
            @Mapping(source = "mouvements", target = "mouvements") // Mapper les mouvements
    })
    CommandeDTO toDto(Commande commande);

    @Mappings({
        @Mapping(source = "dateCommande", target = "date_commande"),
        @Mapping(source = "quntite", target = "quntite"),
        @Mapping(source = "statut", target = "statut"),
            @Mapping(source = "totalCommande" , target = "montant_total"),
        @Mapping(target = "fournisseur.id", source = "fournisseurId"),
            @Mapping(target = "fournisseur", ignore = true),
            @Mapping(target = "mouvements", ignore = true)
    })
    Commande toEntity(CommandeDTO dto);
}
