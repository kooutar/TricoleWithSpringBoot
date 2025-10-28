package com.kaoutar.testSpring.service;

import com.kaoutar.testSpring.dto.FournisseurDTO;
import com.kaoutar.testSpring.mapper.FournisseurMapper;
import com.kaoutar.testSpring.model.Fournisseur;
import com.kaoutar.testSpring.reposetry.FournisseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FournisseurService {

    private final FournisseurRepository repo;
    private  final FournisseurMapper mapper;
     @Autowired
    public FournisseurService(FournisseurRepository repository, FournisseurMapper mapper) {
        this.repo = repository;
         this.mapper = mapper;
     }

public FournisseurDTO convertToDto(Fournisseur entity){return mapper.toDto(entity);}

    public Fournisseur save(Fournisseur f) { return repo.save(f); }
    public List<Fournisseur> getAll() { return repo.findAll(); }
    public Fournisseur getById(Long id) { return repo.findById(id).orElse(null); }
    public void delete(Long id) { repo.deleteById(id); }
}
