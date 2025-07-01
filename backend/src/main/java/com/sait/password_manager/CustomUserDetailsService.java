package com.sait.password_manager; // Stelle sicher, dass der Paketname korrekt ist
//Diese CustomUserDetailsService-Klasse ist die Brücke zwischen Spring Security und deiner Benutzer-Datenbank.
//Bu CustomUserDetailsService sınıfı, Spring Security ile senin veritabanın arasındaki köprüdür.
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Wir verwenden unser UserRepository, um den Benutzer aus der Datenbank zu finden.
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Benutzer nicht gefunden: " + username));
    }
}

//Kısa Özet 📝
//CustomUserDetailsService, Spring Security'nin UserDetailsService sözleşmesini uygulayan bir servistir.
//
//Tek bir görevi vardır: Verilen bir kullanıcı adına göre, kullanıcıyı veritabanından bulup Spring Security'e sunmak.
//
//Veritabanı sorgusu için UserRepository'yi kullanır.
//
//Kullanıcıyı bulursa, UserDetails tipindeki kullanıcı nesnesini döndürür.
//
//Kullanıcıyı bulamazsa, UsernameNotFoundException fırlatarak kimlik doğrulama işleminin başarısız olmasını sağlar.

// Kurze Zusammenfassung 📝
//CustomUserDetailsService ist ein Dienst, der das UserDetailsService-Interface von Spring Security implementiert.
//
//Er hat eine einzige Aufgabe: die Daten eines Benutzers anhand seines Benutzernamens aus der Datenbank zu laden und sie Spring Security bereitzustellen.
//
//Er verwendet ein UserRepository für die eigentliche Datenbankabfrage.
//
//Wenn der Benutzer gefunden wird, gibt er ein UserDetails-Objekt zurück.
//
//Wenn er nicht gefunden wird, muss er eine UsernameNotFoundException werfen, um den Authentifizierungsprozess fehlschlagen zu lassen.