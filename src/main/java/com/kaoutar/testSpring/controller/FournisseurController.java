package com.kaoutar.testSpring.controller;

import com.kaoutar.testSpring.dto.FournisseurDTO;
import com.kaoutar.testSpring.service.FournisseurService;
import  com.kaoutar.testSpring.model.Fournisseur;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fournisseurs")
public class FournisseurController {

    private final FournisseurService fournisseurService;


    @PostMapping
    public ResponseEntity<FournisseurDTO> create(@Valid @RequestBody FournisseurDTO f) {
        FournisseurDTO saved = fournisseurService.save(f);
        return ResponseEntity.ok(saved);
    }


    @GetMapping
    public ResponseEntity<Page<FournisseurDTO>> getAllFournisseurs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return ResponseEntity.ok(fournisseurService.getAllFournisseurs(pageable));
    }


    @GetMapping("/search")
    public ResponseEntity<Page<FournisseurDTO>> searchFournisseurs(
            @RequestParam String societe,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "societe") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return ResponseEntity.ok(fournisseurService.searchFournisseurs(societe, pageable));
    }


    @GetMapping("/{id}")
    public ResponseEntity<FournisseurDTO> getFournisseurById(@PathVariable Long id) {
        FournisseurDTO fournisseur = fournisseurService.getFournisseurById(id);
        return ResponseEntity.ok(fournisseur);
    }


    /*@GetMapping("/email/{email}")
    public ResponseEntity<FournisseurDTO> getFournisseurByEmail(@PathVariable String email) {
        FournisseurDTO fournisseur = fournisseurService.getFournisseurByEmail(email);
        return ResponseEntity.ok(fournisseur);
    }*/


    @PutMapping("/{id}")
    public ResponseEntity<FournisseurDTO> update(@PathVariable Long id,
                                                 @Valid @RequestBody FournisseurDTO fournisseur) {
        FournisseurDTO updated = fournisseurService.updateFournisseur(id, fournisseur);
        return ResponseEntity.ok(updated);
    }

    // ✅ Supprimer un fournisseur
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        String message = fournisseurService.deleteFournisseur(id);
        return ResponseEntity.ok(message);
    }
}

