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
/*
* @RestController, Spring Framework'te bir sınıfı RESTful Web Servisi olarak işaretlemek için kullanılan özel bir anotasyondur.

Bu anotasyonu daha iyi anlamak için onu iki parçaya ayırmalıyız, çünkü @RestController aslında iki farklı anotasyonun birleşimidir:

@Controller: Bu, Spring'in temel web anotasyonudur. Bir sınıfın üzerine @Controller yazdığında, Spring'e
* "Bu sınıf, web'den gelen istekleri (HTTP request) işlemekle sorumlu bir bileşendir" dersin. Geleneksel Spring MVC yapısında,
* @Controller sınıfları genellikle bir "view" (görünüm) adı döndürürdü. Örneğin, bir HTML sayfasının adını ("anasayfa.html") döndürür ve Spring bu sayfayı kullanıcıya gösterirdi.

@ResponseBody: Bu anotasyon ise bir metodun üzerine konur. Spring'e şunu söyler: "Bu metodun döndürdüğü değeri
* bir view (görünüm) adı olarak kabul etme. Bunun yerine, bu değeri doğrudan HTTP yanıtının gövdesine (body) yaz.
* " Döndürülen nesne (örneğin bir User nesnesi), genellikle otomatik olarak JSON formatına dönüştürülür ve istemciye (client) bu şekilde gönderilir.

Peki @RestController Nedir?
@RestController = @Controller + @ResponseBody

İşte bu kadar basit! Bir sınıfı @RestController ile işaretlediğinde, Spring'e aynı anda iki şey söylemiş olursun:
Bu sınıf bir @Controller'dır, yani web isteklerini işler.
Bu sınıfın içindeki tüm metotlara sanki @ResponseBody eklenmiş gibi davran. Yani,
* bu sınıftaki hiçbir metot bir view (görünüm) döndürmeyecek, hepsi doğrudan veri (genellikle JSON) döndürecek.

Benzetme:
@Controller: Geleneksel bir lokanta gibidir. Müşteri (istemci) sipariş verir, aşçı (metot) yemeği hazırlar ve garsona (Spring) hangi masaya (view) gideceğini söyler.

@RestController: Bir "paket servis" (take-away) dükkanı gibidir. Müşteri sipariş verir, aşçı yemeği (veriyi) doğrudan paketler
* (HTTP Response Body) ve müşteriye teslim eder. Masa veya servis yoktur, sadece veri vardır.

* @RestController, REST API'leri (sadece veri döndüren servisler) oluşturmak için kullanılır.
@Controller ve @ResponseBody anotasyonlarının birleşimidir ve bir kolaylıktır.
Bu anotasyonla işaretlenmiş bir sınıftaki tüm metotlar, dönüş değerlerini otomatik olarak HTTP yanıtının gövdesine (genellikle JSON formatında) yazar.
Amacı, view (HTML gibi görünümler) sunan geleneksel web denetleyicileri ile sadece veri sunan API denetleyicilerini birbirinden ayırmaktır.
*
* @RestController ist eine spezielle Annotation im Spring Framework, um eine Klasse als RESTful Web Service zu kennzeichnen.

Um diese Annotation besser zu verstehen, müssen wir sie in zwei Teile zerlegen, denn @RestController ist eigentlich eine Kombination aus zwei anderen Annotationen:

@Controller: Dies ist eine grundlegende Web-Annotation von Spring. Wenn du @Controller
* über eine Klasse schreibst, sagst du Spring: "Diese Klasse ist eine Komponente,
* die für die Verarbeitung von Anfragen aus dem Web (HTTP-Requests) verantwortlich ist.
* " Im traditionellen Spring MVC wurden @Controller-Klassen oft verwendet, um den Namen einer "View" (Ansicht) zurückzugeben.
* Zum Beispiel gaben sie den Namen einer HTML-Seite ("startseite.html") zurück, und Spring renderte diese Seite für den Benutzer.

@ResponseBody: Diese Annotation wird über einer Methode platziert. Sie sagt zu Spring: "Interpretiere den Rückgabewert
* dieser Methode nicht als Namen einer View. Schreibe diesen Wert stattdessen direkt in den Body der HTTP-Antwort.
* " Das zurückgegebene Objekt (z. B. ein User-Objekt) wird normalerweise automatisch in das JSON-Format konvertiert und so an den Client gesendet.

Was ist also @RestController?

@RestController = @Controller + @ResponseBody

So einfach ist das! Wenn du eine Klasse mit @RestController kennzeichnest, teilst du Spring zwei Dinge gleichzeitig mit:

Diese Klasse ist ein @Controller, verarbeitet also Web-Anfragen.

Behandle alle Methoden in dieser Klasse so, als ob sie mit @ResponseBody annotiert wären.
* Das bedeutet, keine Methode in dieser Klasse wird eine View zurückgeben, sondern alle werden direkt Daten (typischerweise JSON) zurückgeben.

Eine Analogie:

@Controller: Wie ein traditionelles Restaurant. Der Kunde (Client) bestellt,
* der Koch (Methode) bereitet das Essen zu und sagt dem Kellner (Spring), an welchen Tisch (View) es serviert werden soll.

@RestController: Wie ein "Take-Away"-Imbiss. Der Kunde bestellt, der Koch verpackt das Essen (die Daten)
* direkt (HTTP Response Body) und übergibt es dem Kunden. Es gibt keine Tische oder Service, nur die reinen Daten.
*
* Kurze Zusammenfassung 📝
@RestController ist eine spezielle Spring-Annotation zur Erstellung von REST-APIs (Dienste, die nur Daten zurückgeben).
Es ist eine praktische Kombination der Annotationen @Controller und @ResponseBody.
Alle Methoden in einer mit @RestController markierten Klasse schreiben ihre Rückgabewerte automatisch in den Body der HTTP-Antwort (typischerweise als JSON).
Sein Zweck ist es, traditionelle Web-Controller, die Views (wie HTML) bereitstellen, von API-Controllern zu trennen, die nur Daten bereitstellen.*/