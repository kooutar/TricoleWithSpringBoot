package com.kaoutar.testSpring.service;

import com.kaoutar.testSpring.dto.MouvementDTO;
import com.kaoutar.testSpring.dto.ProduitDTO;
import com.kaoutar.testSpring.reposetry.MouvementRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MouvementService {

    public  final MouvementRepository mouvementRepository;

    public MouvementDTO save(MouvementDTO mouvementDTO){
        return null;
    }

}
