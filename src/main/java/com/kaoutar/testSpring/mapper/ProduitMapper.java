package com.kaoutar.testSpring.mapper;

import com.kaoutar.testSpring.dto.ProduitDTO;

import com.kaoutar.testSpring.model.Produit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface ProduitMapper {
    ProduitDTO toDto(Produit entity);
    Produit toEntity(ProduitDTO dto);
}
