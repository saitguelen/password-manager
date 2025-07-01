package com.sait.password_manager;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
//Kısa Özet 📝
//LoginRequest, bir DTO (Data Transfer Object)'dur.
//
//Amacı, istemciden (client) sunucuya (server) gönderilen giriş bilgilerini paket halinde taşımaktır.
//
//@Controller içinde @RequestBody ile birlikte kullanılarak gelen JSON verisini Java nesnesine çevirir.
//
//        Kodun temiz, esnek ve güvenli olmasını sağlar.
//
//@Data (Lombok) anotasyonu, standart getter/setter gibi metotları otomatik oluşturarak kod tekrarını önler.


//Kurze Zusammenfassung 📝
//LoginRequest ist ein DTO (Data Transfer Object).
//
//Sein Zweck ist es, Anmeldeinformationen (Benutzername/Passwort) vom Client zum Server zu transportieren.
//
//Es wird in einem @Controller mit @RequestBody verwendet, um eingehende JSON-Daten in ein Java-Objekt umzuwandeln.
//
//Es sorgt für sauberen, flexiblen und sicheren Code.
//
//        Die @Data (Lombok)-Annotation verhindert Code-Wiederholungen, indem sie Standardmethoden wie Getter/Setter automatisch generiert.