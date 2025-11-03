package com.kaoutar.testSpring.controller;

import com.kaoutar.testSpring.dto.ProduitDTO;
import com.kaoutar.testSpring.service.ProduitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/produits")
public class ProduitController {
    private final ProduitService produitService;

    @PostMapping
    public ResponseEntity<ProduitDTO> create(@Valid @RequestBody ProduitDTO produitDTO) {
        ProduitDTO saved = produitService.save(produitDTO);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<ProduitDTO>> getAllProduits() {
        return ResponseEntity.ok(produitService.getAllProduit());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProduitDTO> getProduitById(@PathVariable Long id) {
        ProduitDTO produit = produitService.getProduitById(id);
        return ResponseEntity.ok(produit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProduitDTO> updateProduit(@PathVariable Long id, @Valid @RequestBody ProduitDTO produitDTO) {
        ProduitDTO updated = produitService.updateProduit(id, produitDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduit(@PathVariable Long id) {
        String result = produitService.deleteProduit(id);
        return ResponseEntity.ok(result);
    }
}
