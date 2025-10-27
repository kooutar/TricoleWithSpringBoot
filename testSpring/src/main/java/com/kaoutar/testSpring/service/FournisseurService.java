package com.kaoutar.testSpring.service;

import com.kaoutar.testSpring.model.Fournisseur;
import com.kaoutar.testSpring.reposetry.FournisseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FournisseurService {

    private final FournisseurRepository repo;
     @Autowired
    public FournisseurService(FournisseurRepository repository) {
        this.repo = repository;
    }



    public Fournisseur save(Fournisseur f) { return repo.save(f); }
    public List<Fournisseur> getAll() { return repo.findAll(); }
    public Fournisseur getById(Long id) { return repo.findById(id).orElse(null); }
    public void delete(Long id) { repo.deleteById(id); }
}
