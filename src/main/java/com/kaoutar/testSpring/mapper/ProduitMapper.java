package com.kaoutar.testSpring.mapper;

import com.kaoutar.testSpring.dto.ProduitDTO;
import com.kaoutar.testSpring.model.Produit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProduitMapper {

    ProduitDTO toDto(Produit entity);

    List<ProduitDTO> toDtoList(List<Produit> entities);

    Produit toEntity(ProduitDTO dto);

    @Mapping(target = "id", ignore = true)
    void UpdateProduitFromDTO(ProduitDTO dto, @MappingTarget Produit entity);


}
