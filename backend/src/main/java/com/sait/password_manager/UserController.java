package com.sait.password_manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // Alle Endpunkte in diesem Controller beginnen mit /api/auth.
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil; // Wir injizieren unsere JwtUtil-Klasse.


    // Im nächsten Schritt werden wir eine Hilfsklasse zur JWT-Erstellung verwenden.
    // @Autowired
    // private JwtUtil jwtUtil;

    /**
     * Endpunkt zur Registrierung eines neuen Benutzers.
     *
     * @param user Das JSON-Objekt, das die Daten des zu registrierenden Benutzers enthält.
     * @return Der erstellte Benutzer und der HTTP-Status 201 Created.
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    /**
     * Endpunkt für den Benutzer-Login.
     *
     * @param loginRequest Das JSON-Objekt, das Benutzername und Passwort enthält.
     * @return Eine Antwort, die nach erfolgreichem Login vorerst ein temporäres Token enthält.
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        // Authentifizierung mit Spring Security.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        // Wenn die Authentifizierung erfolgreich ist, wird sie für die Sitzung im Sicherheitskontext gespeichert.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // DIESER TEIL GIBT VORERST EINE PROVISORISCHE ANTWORT ZURÜCK.
        // IM NÄCHSTEN SCHRITT WERDEN WIR HIER EIN ECHTES JWT ERZEUGEN.
        String jwt = jwtUtil.generateToken(authentication);

        return ResponseEntity.ok(new LoginResponse(jwt));
    }

    // NEUER GESCHÜTZTER ENDPUNKT
    @GetMapping("/test")
    public String protectedEndpoint() {
        // Wir holen die Informationen des aktuell eingeloggten Benutzers.
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return "Hallo " + username + "! Du hast erfolgreich auf diesen geschützten Endpunkt zugegriffen.";
    }
}