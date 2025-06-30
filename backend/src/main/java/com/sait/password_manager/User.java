package com.sait.password_manager; // Stelle sicher, dass der Paketname korrekt ist

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails { // Wir implementieren das UserDetails-Interface

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    // --- Methoden von UserDetails ---
    // In dieser Phase unseres Projekts können wir diese Methoden einfach implementieren.
    // Später, wenn wir Rollen (ADMIN, USER etc.) hinzufügen, werden wir die getAuthorities()-Methode füllen.

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Vorerst hat der Benutzer keine Rollen.
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        // Prüft, ob das Konto abgelaufen ist. Vorerst immer gültig.
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Prüft, ob das Konto gesperrt ist. Vorerst immer entsperrt.
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Prüft, ob die Anmeldeinformationen (Passwort) abgelaufen sind.
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Prüft, ob das Konto aktiviert ist.
        return true;
    }
}