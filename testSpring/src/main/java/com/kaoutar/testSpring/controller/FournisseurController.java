package com.kaoutar.testSpring.controller;

import com.kaoutar.testSpring.model.Fournisseur;
import com.kaoutar.testSpring.service.FournisseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/Fournisseurs")
public class FournisseurController {
   private  final FournisseurService  fournisseurService;
    @Autowired
    public FournisseurController(FournisseurService fournisseurService) {
        this.fournisseurService = fournisseurService;
    }
    @PostMapping
    public Fournisseur create(@RequestBody Fournisseur f) { return fournisseurService.save(f); }

    @GetMapping
    public List<Fournisseur> all() { return fournisseurService.getAll(); }

    @GetMapping("/{id}")
    public Fournisseur get(@PathVariable Long id) { return fournisseurService.getById(id); }

    @PutMapping("/{id}")
    public Fournisseur update(@PathVariable Long id, @RequestBody Fournisseur f) {
        f.setId(id);
        return fournisseurService.save(f);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { fournisseurService.delete(id); }
}
