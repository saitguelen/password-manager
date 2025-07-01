package com.sait.password_manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/passwords") // Alle Adressen in diesem Controller beginnen mit /api/passwords
public class PasswordEntryController {

    @Autowired
    private PasswordEntryRepository passwordEntryRepository;

    @Autowired
    private UserRepository userRepository;

    // Listet alle Passwörter des eingeloggten Benutzers auf
    @GetMapping
    public ResponseEntity<List<PasswordEntry>> getAllEntriesForUser() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername).orElseThrow();

        List<PasswordEntry> entries = passwordEntryRepository.findByUserId(currentUser.getId());
        return ResponseEntity.ok(entries);
    }

    // Erstellt einen neuen Passworteintrag für den eingeloggten Benutzer
    @PostMapping
    public ResponseEntity<PasswordEntry> createEntry(@RequestBody PasswordEntry passwordEntry) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername).orElseThrow();

        passwordEntry.setUser(currentUser); // Den Eintrag mit dem aktuellen Benutzer verknüpfen
        PasswordEntry savedEntry = passwordEntryRepository.save(passwordEntry);

        return new ResponseEntity<>(savedEntry, HttpStatus.CREATED);
    }
    /**
     * Aktualisiert einen vorhandenen Passworteintrag.
      @param id Die ID des zu aktualisierenden Eintrags.
      @param newEntryData Die neuen Daten des Passworteintrags.
      @return Der aktualisierte Eintrag oder ein Fehler.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PasswordEntry> updateEntry(@PathVariable Long id, @RequestBody PasswordEntry newEntryData) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername).orElseThrow();

        Optional<PasswordEntry> optionalEntry = passwordEntryRepository.findById(id);

        if (optionalEntry.isEmpty()) {
            return ResponseEntity.notFound().build(); // Eintrag nicht gefunden
        }

        PasswordEntry existingEntry = optionalEntry.get();

        // SICHERHEITSÜBERPRÜFUNG: Gehört dieser Eintrag dem eingeloggten Benutzer?
        if (!existingEntry.getUser().getId().equals(currentUser.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Zugriff verweigert
        }

        // Felder aktualisieren
        existingEntry.setWebsite(newEntryData.getWebsite());
        existingEntry.setUsername(newEntryData.getUsername());
        existingEntry.setEncryptedPassword(newEntryData.getEncryptedPassword());

        PasswordEntry updatedEntry = passwordEntryRepository.save(existingEntry);
        return ResponseEntity.ok(updatedEntry);
    }

    /**
     * Löscht einen vorhandenen Passworteintrag.
      @param id Die ID des zu löschenden Eintrags.
      @return Erfolgs- oder Fehlerstatus.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long id) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername).orElseThrow();

        Optional<PasswordEntry> optionalEntry = passwordEntryRepository.findById(id);

        if (optionalEntry.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        PasswordEntry entryToDelete = optionalEntry.get();

        // SICHERHEITSÜBERPRÜFUNG: Gehört dieser Eintrag dem eingeloggten Benutzer?
        if (!entryToDelete.getUser().getId().equals(currentUser.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        passwordEntryRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // Kein Inhalt nach erfolgreichem Löschen
    }
}
