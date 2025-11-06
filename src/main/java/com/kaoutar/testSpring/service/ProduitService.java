package com.kaoutar.testSpring.service;

import com.kaoutar.testSpring.dto.MouvementDTO;
import com.kaoutar.testSpring.dto.ProduitDTO;
import com.kaoutar.testSpring.enums.StatutMouvement;
import com.kaoutar.testSpring.mapper.ProduitMapper;
import com.kaoutar.testSpring.model.Mouvement;
import com.kaoutar.testSpring.model.Produit;
import com.kaoutar.testSpring.reposetry.ProduitRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
@AllArgsConstructor
@Service
public class ProduitService {
    private final ProduitRepository repo;
    private  final ProduitMapper mapper;
    private final  MouvementService mouvementService;


    public ProduitDTO save(ProduitDTO f) {
        List<ProduitDTO> produits = getProduitByName(f.getNom());

        if (produits.stream()
                .anyMatch(p -> Objects.equals(p.getPrix_unitaire(), f.getPrix_unitaire()))) {

            ProduitDTO existingProduit = produits.stream()
                    .filter(p -> Objects.equals(p.getPrix_unitaire(), f.getPrix_unitaire()))
                    .findFirst()
                    .get();

            Integer newQnte = existingProduit.getQnte_stock() + f.getQnte_stock();
            existingProduit.setQnte_stock(newQnte);

            // Sauvegarder le produit d'abord
            Produit produitEntity = repo.save(mapper.toEntity(existingProduit));

            // Créer et sauvegarder le mouvement
            MouvementDTO mouvementDTO = new MouvementDTO();
            mouvementDTO.setQuantite(f.getQnte_stock());
            mouvementDTO.setStatut(StatutMouvement.ENTREE);
            mouvementDTO.setDateMouvement(new Date());

            mouvementService.createMouvementForProduit(mouvementDTO, produitEntity);

            return mapper.toDto(produitEntity);
        } else {

            Produit savedEntity = repo.save(mapper.toEntity(f));


            MouvementDTO mouvementDTO = new MouvementDTO();
            mouvementDTO.setQuantite(f.getQnte_stock());
            mouvementDTO.setStatut(StatutMouvement.ENTREE);
            mouvementDTO.setDateMouvement(new Date());
            mouvementDTO.setProduitId(savedEntity.getId());


            mouvementService.save(mouvementDTO, savedEntity, null);

            return mapper.toDto(savedEntity);
        }
    }


    public Page<ProduitDTO> getAllProduits(Pageable pageable) {
        Page<Produit> produitPage = repo.findAll(pageable);
        return produitPage.map(mapper::toDto);
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

    public Optional<Produit> findById(Long id) {
        return repo.findById(id);
    }

    public Produit saveProduit(Produit produit) {
        return repo.save(produit);
}}
