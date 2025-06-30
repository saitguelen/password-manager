package com.sait.password_manager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordEntryRepository extends JpaRepository<PasswordEntry, Long> {
    // Findet alle Passworteinträge, die zu einem bestimmten Benutzer gehören.
    List<PasswordEntry> findByUserId(Long userId);
}