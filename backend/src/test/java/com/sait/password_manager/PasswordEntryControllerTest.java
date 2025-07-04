package com.sait.password_manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Dies ist ein Integrationstest, daher starten wir die volle Spring Boot-Umgebung.
@SpringBootTest
// Konfiguriert MockMvc automatisch und macht es einsatzbereit.
@AutoConfigureMockMvc
class PasswordEntryControllerTest {

    // Wird verwendet, um HTTP-Anfragen zu simulieren.
    @Autowired
    private MockMvc mockMvc;

    // Wird verwendet, um Java-Objekte in JSON-Strings umzuwandeln.
    @Autowired
    private ObjectMapper objectMapper;

    // In diesem Test möchten wir das Verhalten des UserRepository kontrollieren,
    // daher ersetzen wir es durch ein Mock-Objekt.
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEntryRepository passwordEntryRepository;

    // @Test: Kennzeichnet diese Methode als einen Testfall.
    // @WithMockUser: Diese magische Annotation sorgt dafür, dass unser Test so ausgeführt wird,
    // als wäre ein Benutzer mit dem Namen "testuser" eingeloggt. Sie füllt den SecurityContext für uns.
    @Test
    @WithMockUser(username = "testuser")
    void createEntry_whenLoggedIn_shouldReturnCreatedEntry() throws Exception {
        // 1. ARRANGE (Vorbereitungsphase)
        // Wir bereiten die notwendigen Objekte und das Verhalten der Mock-Objekte für den Test vor.
        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        PasswordEntry newEntry = new PasswordEntry();
        newEntry.setWebsite("test.com");
        newEntry.setUsername("user_on_site");
        newEntry.setEncryptedPassword("secret_password");

        PasswordEntry savedEntry = new PasswordEntry();
        savedEntry.setId(100L);
        savedEntry.setUser(testUser);
        savedEntry.setWebsite(newEntry.getWebsite());
        savedEntry.setUsername(newEntry.getUsername());
        savedEntry.setEncryptedPassword(newEntry.getEncryptedPassword());

        // Wir definieren das Verhalten der Mock-Objekte:
        // "Wenn userRepository.findByUsername mit 'testuser' aufgerufen wird, gib unseren testUser zurück"
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        // "Wenn passwordEntryRepository.save mit einem beliebigen PasswordEntry-Objekt aufgerufen wird, gib unseren savedEntry zurück"
        when(passwordEntryRepository.save(any(PasswordEntry.class))).thenReturn(savedEntry);

        // 2. ACT (Aktionsphase)
        // Wir simulieren eine echte API-Anfrage mit MockMvc.
        mockMvc.perform(post("/api/passwords") // Die Adresse, an die die POST-Anfrage gesendet wird
                        .contentType(MediaType.APPLICATION_JSON) // Der Typ der gesendeten Daten ist JSON
                        .content(objectMapper.writeValueAsString(newEntry))) // Die zu sendenden JSON-Daten

                // 3. ASSERT (Überprüfungsphase)
                // Wir überprüfen, ob die zurückgegebene Antwort unseren Erwartungen entspricht.
                .andExpect(status().isCreated()) // Erwarte den HTTP-Statuscode 201 Created
                .andExpect(jsonPath("$.id").value(100L)) // Erwarte, dass das id-Feld im zurückgegebenen JSON 100 ist
                .andExpect(jsonPath("$.website").value("test.com")); // Erwarte, dass das website-Feld im zurückgegebenen JSON "test.com" ist
    }
}
