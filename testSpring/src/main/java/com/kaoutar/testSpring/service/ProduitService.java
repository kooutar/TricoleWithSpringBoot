package com.kaoutar.testSpring.service;

import com.kaoutar.testSpring.dto.ProduitDTO;
import com.kaoutar.testSpring.mapper.ProduitMapper;
import com.kaoutar.testSpring.model.Produit;
import com.kaoutar.testSpring.reposetry.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        Produit savedEntity= repo.save(mapper.toEntity(f));
        return  mapper.toDto(savedEntity);
    }


    public List<ProduitDTO> getAllProduit() {
        List<Produit> Produits= repo.findAll();
        return Produits.stream().map(mapper::toDto).collect(Collectors.toList())  ;
    }

    public  ProduitDTO updateProduit(Long id , ProduitDTO ProduitDTO){
        Optional<Produit> ProduitById= repo.findById(id);
        if(ProduitById.isPresent()){
            Produit ProduitUpdate= repo.save(ProduitById.get());
            return mapper.toDto(ProduitUpdate);
        }
       return  null;
    }

    public String deleteProduit(Long id) {
        Optional<Produit> ProduitById=repo.findById(id);
        if(ProduitById.isPresent()){
            repo.delete(ProduitById.get());
            return "delete with succes";
        }
        return "product don't exist";
    }
}
