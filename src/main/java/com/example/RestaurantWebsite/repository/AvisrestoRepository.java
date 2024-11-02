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
public interface AvisrestoRepository extends JpaRepository<Avisresto, Long> {
    Optional<Avisresto> findByNom(String nom);

    List<Avisresto> findByEmail(String email);

    List<Avisresto> findByEtoile(int etoile);

    List<Avisresto> findByLiked(int liked);

    List<Avisresto> findByDislike(int dislike);

    List<Avisresto> findByCommentaire(String commentaire);

    List<Avisresto> findByIdresto(Long idresto);
}
