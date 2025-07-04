package com.sait.password_manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// Gibt an, dass wir JUnit 5 und Mockito zusammen verwenden.
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    // @Mock: Erstellt ein Mock-Objekt für das UserRepository.
    // Anstatt eine Verbindung zur echten Datenbank herzustellen, steuern wir sein Verhalten.
    @Mock
    private UserRepository userRepository;

    // @Mock: Erstellt ebenfalls ein Mock-Objekt für den PasswordEncoder.
    @Mock
    private PasswordEncoder passwordEncoder;

    // @InjectMocks: Erstellt eine Instanz des UserService und injiziert die oben
    // deklarierten Mock-Objekte (@Mock) automatisch in diesen Service.
    @InjectMocks
    private UserService userService;

    private User user;

    // @BeforeEach: Diese Methode wird vor jedem einzelnen @Test-Fall ausgeführt.
    // Sie sorgt für einen sauberen Ausgangspunkt für jeden Test.
    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
    }

    // @Test: Kennzeichnet diese Methode als einen Testfall.
    @Test
    void registerUser_shouldHashPasswordAndSaveUser() {
        // 1. ARRANGE (Vorbereitungsphase)
        // Wir definieren, wie sich unsere Mock-Objekte verhalten sollen.
        String hashedPassword = "hashedPassword123";

        // "Wenn die Methode passwordEncoder.encode mit 'password123' aufgerufen wird,
        // dann gib den Wert 'hashedPassword123' zurück."
        when(passwordEncoder.encode("password123")).thenReturn(hashedPassword);

        // "Wenn die Methode userRepository.save mit einem beliebigen User-Objekt aufgerufen wird,
        // dann gib das von uns vorbereitete 'user'-Objekt zurück."
        when(userRepository.save(any(User.class))).thenReturn(user);

        // 2. ACT (Aktionsphase)
        // Wir rufen die eigentliche Methode auf, die wir testen möchten.
        User savedUser = userService.registerUser(user);

        // 3. ASSERT (Überprüfungsphase)
        // Wir überprüfen, ob die Ergebnisse unseren Erwartungen entsprechen.
        assertNotNull(savedUser, "Der gespeicherte Benutzer sollte nicht null sein.");
        assertEquals("testuser", savedUser.getUsername(), "Der Benutzername sollte unverändert bleiben.");
        assertEquals(hashedPassword, savedUser.getPassword(), "Das Passwort sollte gehasht sein.");
    }
}
