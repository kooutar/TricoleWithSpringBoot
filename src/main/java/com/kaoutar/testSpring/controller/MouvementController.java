package com.kaoutar.testSpring.controller;

import com.kaoutar.testSpring.service.MouvementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mouvements")
@RequiredArgsConstructor
public class MouvementController {
    private final MouvementService mouvementService;

    @GetMapping("/cump/{produitId}")
    public ResponseEntity<Double> getCUMP(@PathVariable Long produitId) {
        double cump = mouvementService.calculerCUMP(produitId);
        return ResponseEntity.ok(cump);
    }

    @GetMapping("/cout-approvisionnement/{produitId}")
    public ResponseEntity<Double> getCoutTotalApprovisionnement(@PathVariable Long produitId) {
        double coutTotal = mouvementService.calculerCoutTotalApprovisionnement(produitId);
        return ResponseEntity.ok(coutTotal);
    }
}
