package com.kaoutar.testSpring.service;

import com.kaoutar.testSpring.dto.FournisseurDTO;
import com.kaoutar.testSpring.mapper.FournisseurMapper;
import com.kaoutar.testSpring.model.Fournisseur;
import com.kaoutar.testSpring.reposetry.FournisseurRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FournisseurService {

    private final FournisseurRepository repo;
    private  final FournisseurMapper mapper;

    public FournisseurDTO save(FournisseurDTO f) {
         Fournisseur savedntity= repo.save(mapper.toEntity(f));
         return  mapper.toDto(savedntity);
     }


    public List<FournisseurDTO> getAllFournisseur() {
         List<Fournisseur> fournisseurs= repo.findAll();
        return fournisseurs.stream().map(f->mapper.toDto(f) ).collect(Collectors.toList())  ;
     }

     public  FournisseurDTO updateFournisseur(Long id , FournisseurDTO fournisseurDTO){
          Optional<Fournisseur> fournisseurById= repo.findById(id);
          Fournisseur fournisseurUpdate= repo.save(fournisseurById.get());
          return mapper.toDto(fournisseurUpdate);
     }

    public String deleteFournisseur(Long id) {
         Optional<Fournisseur> fournisseurById=repo.findById(id);
         if(fournisseurById.isPresent()){
             repo.delete(fournisseurById.get());
             return "delete with succes";
         }
         return "supplier don't exist";
    }

    public FournisseurDTO getFournisseurById(Long id) {
        return mapper.toDto(repo.findById(id).get());
    }
}
