package com.kaoutar.testSpring.model;


import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Fournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @Column
    private String societe;
    @Column
    private String adresse;
    @Column
    private String contact;
    @Column(unique = true)
    private String email;
    @Column
    private String telephone;
    @Column
    private String ville;
    @Column
    private String ice;
    @OneToMany(mappedBy = "fournisseur", cascade = CascadeType.ALL)
    private List<Commande> commandeList;


}