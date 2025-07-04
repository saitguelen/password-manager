package com.sait.password_manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    // Diese Methode wird vor jedem einzelnen Testfall ausgeführt.
    // Sie stellt sicher, dass jeder Test mit einem sauberen, neuen User-Objekt startet.
    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("hashedPassword");
    }

    // Testet, ob die Getter- und Setter-Methoden (von Lombok generiert) korrekt funktionieren.
    @Test
    void testGettersAndSetters() {
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("hashedPassword", user.getPassword());
    }

    // Testet die Methoden, die vom UserDetails-Interface implementiert wurden.
    // Diese stellen sicher, dass das Benutzerkonto standardmäßig aktiv und gültig ist.
    @Test
    void testUserDetailsMethods() {
        // In unserer einfachen Implementierung hat der Benutzer keine speziellen Rollen (Authorities).
        assertTrue(user.getAuthorities().isEmpty(), "Authorities sollten leer sein.");

        // Diese Methoden sollten standardmäßig 'true' zurückgeben, um das Konto als aktiv zu kennzeichnen.
        assertTrue(user.isAccountNonExpired(), "Konto sollte nicht abgelaufen sein.");
        assertTrue(user.isAccountNonLocked(), "Konto sollte nicht gesperrt sein.");
        assertTrue(user.isCredentialsNonExpired(), "Anmeldeinformationen sollten nicht abgelaufen sein.");
        assertTrue(user.isEnabled(), "Konto sollte aktiviert sein.");
    }
}
