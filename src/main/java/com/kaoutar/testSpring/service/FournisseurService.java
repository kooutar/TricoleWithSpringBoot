package com.kaoutar.testSpring.service;

import com.kaoutar.testSpring.dto.FournisseurDTO;
import com.kaoutar.testSpring.mapper.FournisseurMapper;
import com.kaoutar.testSpring.model.Fournisseur;
import com.kaoutar.testSpring.reposetry.FournisseurRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FournisseurService {

    private final FournisseurRepository repo;
    private  final FournisseurMapper mapper;
     @Autowired
    public FournisseurService(FournisseurRepository repository, FournisseurMapper mapper) {
        this.repo = repository;
         this.mapper = mapper;
     }


    public FournisseurDTO save(FournisseurDTO f) {
         Fournisseur savedntity= repo.save(mapper.toEntity(f));
         return  mapper.toDto(savedntity);
     }


    public List<FournisseurDTO> getAllFournisseur() {
         List<Fournisseur> fournisseurs= repo.findAll();
        return fournisseurs.stream().map(f->mapper.toDto(f) ).collect(Collectors.toList())  ;
     }

     public  FournisseurDTO updateFournisseur(Long id , FournisseurDTO fournisseurDTO){
         Fournisseur fournisseur = repo.findById(id)
                 .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé avec l'ID : " + id));

         // ✅ MapStruct met à jour uniquement les champs du DTO
         mapper.updateFournisseurFromDto(fournisseurDTO, fournisseur);

         Fournisseur updated = repo.save(fournisseur);
         return mapper.toDto(updated);
     }

    public String deleteFournisseur(Long id) {
         Optional<Fournisseur> fournisseurById=repo.findById(id);
         if(fournisseurById.isPresent()){
             repo.delete(fournisseurById.get());
             return "delete with succes";
         }
         return "supplier don't exist";
    }
    // ✅ Trouver par ID

    public FournisseurDTO getFournisseurById(Long id) {
        Fournisseur fournisseur = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé avec l'ID : " + id));
        return mapper.toDto(fournisseur);
    }

    // ✅ Trouver par email

   /* public FournisseurDTO getFournisseurByEmail(String email) {
        Fournisseur fournisseur = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé avec l'email : " + email));
        return mapper.toDto(fournisseur);
    }*/
}
