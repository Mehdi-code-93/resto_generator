package com.example.RestaurantWebsite.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "restaurants")
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String restaurant;
    private String police;
    @Column(name = "banniere", columnDefinition = "LONGBLOB")
    private byte[] banniere;
    private String couleur;
    private String textCouleur;
    private String boutonCouleur;
    private String lienCouleur;
    private String adresse;
    private String emailContact;
    private String telephone;
    private String username;
    private String password;

    // @OneToMany(mappedBy = "site")
    // private List<Carte> cartes;

    @OneToMany(mappedBy = "site")
    private List<Carte> cartes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getPolice() {
        return police;
    }

    public void setPolice(String police) {
        this.police = police;
    }

    public byte[] getBanniere() {
        return banniere;
    }

    public void setBanniere(byte[] banniere) {
        this.banniere = banniere;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public String getTextcouleur() {
        return textCouleur;
    }

    public void setTextcouleur(String textCouleur) {
        this.textCouleur = textCouleur;
    }

    public String getBoutoncouleur() {
        return boutonCouleur;
    }

    public void setBoutoncouleur(String boutonCouleur) {
        this.boutonCouleur = boutonCouleur;
    }

    public String getLiencouleur() {
        return lienCouleur;
    }

    public void setLiencouleur(String lienCouleur) {
        this.lienCouleur = lienCouleur;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmailcontact() {
        return emailContact;
    }

    public void setEmailcontact(String emailContact) {
        this.emailContact = emailContact;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}