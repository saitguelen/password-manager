package com.sait.password_manager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Data JPA leitet aus dem Methodennamen die SQL-Abfrage selbst ab.
    // Diese Methode findet einen Benutzer anhand seines Benutzernamens.
    Optional<User> findByUsername(String username);
}
