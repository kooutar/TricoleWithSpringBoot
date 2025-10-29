package com.kaoutar.testSpring.controller;

import com.kaoutar.testSpring.dto.ProduitDTO;
import com.kaoutar.testSpring.dto.ProduitDTO;
import com.kaoutar.testSpring.service.ProduitService;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor


@Getter
@Setter
@RequestMapping("/api/produits")
public class ProduitController {
    final ProduitService produitService;

    @PostMapping
    public ResponseEntity<ProduitDTO> create(@Valid @RequestBody ProduitDTO f) {
        ProduitDTO saved=produitService.save(f);
        return ResponseEntity.ok(saved);
    }
    @GetMapping
    public List<ProduitDTO> getAllProduit(){return produitService.getAllProduit();}
    @PutMapping
    public  ResponseEntity<ProduitDTO> Update(@PathVariable Long id, @Valid @RequestBody ProduitDTO Produit){
        ProduitDTO updated= produitService.updateProduit(id,Produit);
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping
    public  String delete(@PathVariable Long id){return  produitService.deleteProduit(id);}
    
    
}
