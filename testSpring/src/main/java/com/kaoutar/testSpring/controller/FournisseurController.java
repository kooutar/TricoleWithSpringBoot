package com.kaoutar.testSpring.controller;

import com.kaoutar.testSpring.dto.FournisseurDTO;
import com.kaoutar.testSpring.model.Fournisseur;
import com.kaoutar.testSpring.service.FournisseurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor

@RequestMapping("/api/fournisseurs")
public class FournisseurController {
   private  final FournisseurService  fournisseurService;

    @PostMapping
    public ResponseEntity<FournisseurDTO> create(@Valid @RequestBody FournisseurDTO f) {
        FournisseurDTO saved=fournisseurService.save(f);
        return ResponseEntity.ok(saved);
    }


    @GetMapping
    public List<FournisseurDTO> getAllFournisseur(){return fournisseurService.getAllFournisseur();}

    @GetMapping("/{id}")
    public FournisseurDTO GetFournisseurById(@PathVariable Long id){return  fournisseurService.getFournisseurById(id);}

    @PutMapping("/{id}")
    public  ResponseEntity <FournisseurDTO> Update(@PathVariable Long id, @Valid @RequestBody FournisseurDTO fournisseur){
        FournisseurDTO updated= fournisseurService.updateFournisseur(id,fournisseur);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public  String delete(@PathVariable Long id){return  fournisseurService.deleteFournisseur(id);}

}
