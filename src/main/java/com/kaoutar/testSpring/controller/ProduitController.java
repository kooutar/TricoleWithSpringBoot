package com.kaoutar.testSpring.controller;

import com.kaoutar.testSpring.dto.ProduitDTO;
import com.kaoutar.testSpring.service.ProduitService;
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
@RequestMapping("/api/produits")
public class ProduitController {
    private final ProduitService produitService;

    @PostMapping
    public ResponseEntity<ProduitDTO> create(@Valid @RequestBody ProduitDTO produitDTO) {
        ProduitDTO saved = produitService.save(produitDTO);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<Page<ProduitDTO>> getAllProduits(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return ResponseEntity.ok(produitService.getAllProduits(pageable));
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

    @GetMapping("/search/{nom}")
    public List<ProduitDTO> getProduitByName(@PathVariable String nom) {
        return produitService.getProduitByName(nom);

    }


}
