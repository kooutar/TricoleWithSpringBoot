package com.kaoutar.testSpring.service;

import com.kaoutar.testSpring.dto.ProduitDTO;
import com.kaoutar.testSpring.mapper.ProduitMapper;
import com.kaoutar.testSpring.model.Produit;
import com.kaoutar.testSpring.reposetry.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProduitService {
    private final ProduitRepository repo;
    private  final ProduitMapper mapper;
    @Autowired
    public ProduitService(ProduitRepository repository, ProduitMapper mapper) {
        this.repo = repository;
        this.mapper = mapper;
    }

    public ProduitDTO save(ProduitDTO f) {

        List<ProduitDTO> produits = getProduitByName(f.getNom());

        Optional<ProduitDTO> existingProduit = produits.stream()
                .filter(p -> Objects.equals(p.getPrix_unitaire(), f.getPrix_unitaire()))
                .findFirst();

        if (existingProduit.isPresent()) {
            ProduitDTO produitByName = existingProduit.get();
            Integer newQnte = produitByName.getQnte_stock() + f.getQnte_stock();
            produitByName.setQnte_stock(newQnte);

            Produit updated = repo.save(mapper.toEntity(produitByName));
            return mapper.toDto(updated);
        }
        Produit savedEntity = repo.save(mapper.toEntity(f));
        return mapper.toDto(savedEntity);
    }


    public List<ProduitDTO> getAllProduit() {
        List<Produit> produits = repo.findAll();
        return produits.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public  ProduitDTO updateProduit(Long id , ProduitDTO ProduitDTO){
        Optional<Produit> ProduitById= Optional.ofNullable(repo.findById(id).orElseThrow(() -> new RuntimeException("produit non trouvé avec l'ID : " + id)));

        mapper.UpdateProduitFromDTO(ProduitDTO, ProduitById.get());

        Produit updated = repo.save(ProduitById.get());
        return mapper.toDto(updated);
    }

    public String deleteProduit(Long id) {
        Optional<Produit> ProduitById=repo.findById(id);
        if(ProduitById.isPresent()){
            repo.delete(ProduitById.get());
            return "delete with succes";
        }
        return "product don't exist";
    }

    public ProduitDTO getProduitById(Long id) {
        Optional<Produit> produitById = repo.findById(id);
        return produitById.map(mapper::toDto).orElse(null);
    }

    public List<ProduitDTO> getProduitByName(String nom) {
        return repo.findByNom(nom)
                .map(mapper::toDto)
                .stream().collect(Collectors.toList());
    }


}
