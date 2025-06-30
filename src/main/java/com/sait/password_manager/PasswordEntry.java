package com.sait.password_manager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "password_entries")
public class PasswordEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String website;

    @Column(nullable = false)
    private String username; // Der Benutzername für die Webseite

    @Column(nullable = false)
    private String encryptedPassword; // Das Passwort für die Webseite

    // Dies stellt die Beziehung zwischen PasswordEntry und User her.
    // Viele Passworteinträge können zu einem Benutzer gehören.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore // Verhindert, dass dieses Feld in der JSON-Antwort angezeigt wird.
    private User user;
}