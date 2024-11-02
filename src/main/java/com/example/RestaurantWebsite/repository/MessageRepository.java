package com.example.RestaurantWebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.RestaurantWebsite.model.Message;


import java.util.Optional;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> findByNom(String nom);

    Optional<Message> findByEmail(String email);

    Optional<Message> findByPhone(int phone);

    List<Message> findByCommentaire(String commentaire);

    Optional<Message> findByIdresto(int idresto);
}
