package com.example.RestaurantWebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.RestaurantWebsite.model.Carte;
import com.example.RestaurantWebsite.model.Site;
import com.example.RestaurantWebsite.model.Avis;
import com.example.RestaurantWebsite.model.Avisresto;

import java.util.Optional;
import java.util.List;

@Repository
public interface AvisRepository extends JpaRepository<Avis, Long> {
    Optional<Carte> findByNom(String nom);

    Optional<Carte> findByEmail(String email);

    Optional<Carte> findByIdresto(int idresto);

    Optional<Avisresto> findByEtoile(int etoile);

    // Optional<Carte> findByResto(int resto);

    List<Carte> findByCommentaire(String commentaire);

    // List<Carte> findByResto(int resto);

    List<Site> findByPlat(String plat);
}
