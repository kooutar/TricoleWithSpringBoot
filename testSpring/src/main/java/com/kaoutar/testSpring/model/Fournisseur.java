package com.kaoutar.testSpring.model;


import jakarta.persistence.GenerationType;
import jakarta.persistence.*;


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

    public Fournisseur(Long id, String ice, String ville, String telephone, String email, String contact, String adresse, String societe) {
        this.id = id;
        this.ice = ice;
        this.ville = ville;
        this.telephone = telephone;
        this.email = email;
        this.contact = contact;
        this.adresse = adresse;
        this.societe = societe;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSociete() {
        return societe;
    }

    public void setSociete(String societe) {
        this.societe = societe;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getIce() {
        return ice;
    }

    public void setIce(String ice) {
        this.ice = ice;
    }
}
