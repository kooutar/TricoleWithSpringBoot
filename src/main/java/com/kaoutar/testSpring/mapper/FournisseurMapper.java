package com.kaoutar.testSpring.mapper;

import com.kaoutar.testSpring.dto.FournisseurDTO;
import com.kaoutar.testSpring.model.Fournisseur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FournisseurMapper {
    FournisseurDTO toDto(Fournisseur entity);
    Fournisseur toEntity(FournisseurDTO dto);
    @Mapping(target = "id", ignore = true)
    // ✅ Cette méthode met à jour un entity existant à partir d’un DTO
    void updateFournisseurFromDto(FournisseurDTO dto, @MappingTarget Fournisseur fournisseur);
}
