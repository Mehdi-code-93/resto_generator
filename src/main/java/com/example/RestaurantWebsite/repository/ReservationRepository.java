package com.example.RestaurantWebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.RestaurantWebsite.model.Carte;
import com.example.RestaurantWebsite.model.Reservation;

import java.util.Optional;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByNom(String nom);

    Optional<Reservation> findByEmail(String email);

    Optional<Reservation> findByPhone(int phone);

    List<Reservation> findByCommentaire(String commentaire);

    Optional<Reservation> findByPersonne(int personne);

    Optional<Reservation> findByIdresto(int idresto);


}
