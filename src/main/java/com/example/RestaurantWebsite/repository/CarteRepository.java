package com.example.RestaurantWebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.RestaurantWebsite.model.Carte;
import com.example.RestaurantWebsite.model.Site;

import java.util.Optional;
import java.util.List;

@Repository
public interface CarteRepository extends JpaRepository<Carte, Long> {
    Optional<Carte> findByNom(String nom);

    // Optional<Carte> findByResto(int resto);

    List<Carte> findByCategorie(String categorie);

    // List<Carte> findByResto(int resto);

    List<Site> findBySiteRestaurant(String restaurant);
    
    List<Carte> findByPrixBetween(int prixMin, int prixMax);

     // Méthode pour trouver les cartes par site
    List<Carte> findBySite(Site site);
}
