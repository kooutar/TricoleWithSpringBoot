package com.kaoutar.testSpring.mapper;

import com.kaoutar.testSpring.dto.FournisseurDTO;
import com.kaoutar.testSpring.model.Fournisseur;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FournisseurMapper {
    FournisseurDTO toDto(Fournisseur entity);
    Fournisseur toEntity(FournisseurDTO dto);
}
