package com.sait.password_manager; // Stelle sicher, dass der Paketname korrekt ist

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@Entity
@Table(name = "users")//Das heißt, dass wir bei der Arbeit mit Java-Objekten keine SQL-Abfragen im Hintergrund schreiben müssen.
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

    /*Der erste und wichtigste Mitarbeiter – Der Benutzer (User.java)
Jede Küche braucht Personal. Mein erster und wichtigster Mitarbeiter ist der Benutzer (User).
Ich habe eine "Personalakte" (User.java als Entity) erstellt, die festlegt, welche Informationen
wir über jeden Benutzer speichern müssen: einen einzigartigen Benutzernamen und ein Passwort.
*/

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Vorerst hat der Benutzer keine Rollen.
        //Collection ist ein basis Interface in Java ->Man kann es sich wie eine Schachtel vorstellen, in die man mehr als Object einen Gegenstand legen kann.
//        Wenn ein Benutzer versucht, sich anzumelden, ruft Spring Security
//        diese getAuthorities()-Methode auf, um zu fragen: "Was darf dieser Benutzer tun?".
//                Anhand der zurückgegebenen Berechtigungen wird entschieden,
//                auf welche Seiten der Benutzer zugreifen oder welche Operationen er durchführen kann.
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
/*
Was dieser Code bedeutet:
Wir haben eine Java-Klasse namens User.
Diese Klasse entspricht der Tabelle users in der Datenbank (@Entity, @Table).
Jedes User-Objekt hat eine eindeutige id, deren Wert automatisch von der Datenbank zugewiesen wird (@Id, @GeneratedValue).
Andere Felder wie username, email und password entsprechen anderen Spalten in der Tabelle.
Wir müssen Methoden wie user.getId() oder user.setUsername(...) nicht selbst schreiben; @Data erledigt das alles für uns.

3. Kurze Zusammenfassung
@Entity: Markiert eine Klasse als Datenbank-Entität.
@Table: Gibt den Namen der Datenbanktabelle an (optional).
@Id: Macht ein Feld zum Primärschlüssel der Tabelle.
@GeneratedValue: Sorgt dafür, dass der Wert des Primärschlüssels automatisch generiert wird.
@Data: (von Lombok) Hält die Klasse sauber, indem es Standardcode wie Getter und Setter automatisch generiert.
Diese fünf Annotationen bilden die Grundlage für die Erstellung von Datenbankmodellen in der modernen Java-Backend-Entwicklung (insbesondere mit Spring Boot).

Bu kodun anlamı:
User adında bir Java sınıfımız var.
Bu sınıf, veritabanındaki users tablosuna karşılık geliyor (@Entity, @Table).
Her User nesnesinin benzersiz bir id'si var ve bu değer veritabanı tarafından otomatik olarak atanıyor (@Id, @GeneratedValue).
username, email, password gibi diğer alanlar tablodaki diğer sütunlara karşılık geliyor.
user.getId(), user.setUsername(...) gibi metotları yazmamıza gerek yok, @Data bizim için hepsini hallediyor.
3. Kısa Özet
@Entity: Sınıfı bir veritabanı varlığı olarak işaretler.
@Table: Veritabanı tablo adını (opsiyonel olarak) belirtir.
@Id: Alanı tablonun birincil anahtarı (Primary Key) yapar.
@GeneratedValue: Birincil anahtarın değerinin otomatik olarak üretilmesini sağlar.
@Data: (Lombok'tan) Getter, Setter gibi standart kodları otomatik üreterek sınıfı temiz tutar.

Bu beş anotasyon, modern Java backend geliştirmede (özellikle Spring Boot ile) veritabanı modelleri oluşturmanın temelini oluşturur.*/
/*Was ist GrantedAuthority?
- Dies ist eine Schnittstelle von Spring Security. Wörtlich bedeutet es "Erteilte Autorität".
- Es ist ein Standard, der jede Berechtigung im System repräsentiert (z.B. "Administrator werden", "Artikel schreiben", "Kommentar löschen").
- Normalerweise werden diese Berechtigungen in Rollen ausgedrückt und mit dem Präfix ROLE_ geschrieben (z. B. ROLE_ADMIN, ROLE_USER).
Dies ist zwar keine Vorschrift, aber eine gängige Praxis.
- Damit eine Klasse als GrantedAuthority gilt, muss sie diese Schnittstelle implementieren und eine Methode namens getAuthority() enthalten.
Diese Methode gibt den Namen der Berechtigung (z.B. "ROLE_ADMIN") als String zurück.

- <? extends GrantedAuthority> (Wildcard)
- Dies ist der Teil, der am verwirrendsten aussieht, aber die größte Flexibilität bietet. Dies wird in Java "Upper Bounded Wildcard" genannt.
- Das bedeutet: "Diese Sammlung kann Objekte jeder Klasse enthalten, die die Schnittstelle GrantedAuthority implementiert."

- Warum ist das notwendig? Angenommen, Sie haben eine Klasse namens Role in Ihrem System, und diese Klasse implementiert die Schnittstelle GrantedAuthority.
Vielleicht haben Sie auch eine andere Klasse namens Permission, die ebenfalls das GrantedAuthority-Interface implementiert.
- Wenn die Methode Collection<GrantedAuthority> zurückgeben würde, könnten Sie nur Objekte vom Typ GrantedAuthority direkt darin ablegen.

- Aber dank Collection<? extends GrantedAuthority> kann die Methode sowohl Collection<Role> als auch Collection<Permission> zurückgeben.
Das macht unseren Code viel flexibler und wiederverwendbar. Wir sagen Spring Security: "Ich gebe dir eine Collection, die etwas enthält,
das ein Nachkomme von GrantedAuthority ist, kümmere dich nicht darum, ob der Typ genau Role oder Permission ist".

- return Collections.emptyList(); Was bedeutet das?
- Diese Zeile bedeutet "return an empty, unmodifiable list".
- Wie im deutschen Kommentar über dem Code angegeben (// Vorerst hat der Benutzer keine Rollen.), bedeutet dies "for now the user has no role/authorisation".
Es wird oft in den frühen Phasen der Entwicklung oder für bestimmte Arten von Benutzern verwendet
(z. B. ein Benutzer, der sich nur registriert hat, aber noch nicht zugelassen wurde).


GrantedAuthority Nedir?
Bu, Spring Security'e ait bir arayüzdür. Kelime anlamı "Verilmiş Yetki" demektir.
Sistemdeki her bir yetkiyi (örneğin "Yönetici Olma", "Makale Yazma", "Yorum Silme") temsil eden bir standarttır.
Genellikle bu yetkiler, rollerle ifade edilir ve ROLE_ önekiyle yazılır (örneğin ROLE_ADMIN, ROLE_USER). Bu bir kural olmasa da yaygın bir kullanımdır.
Bir sınıfın GrantedAuthority sayılabilmesi için bu arayüzü uygulaması ve getAuthority() adında bir metot içermesi gerekir. Bu metot, yetkinin adını (örn: "ROLE_ADMIN") bir String olarak döndürür.

<? extends GrantedAuthority> Neden Kullanılmış? (Wildcard - Joker Karakter)
İşte en kafa karıştırıcı görünen ama en esnekliği sağlayan kısım burası. Buna Java'da "Upper Bounded Wildcard" denir.
Anlamı: "Bu koleksiyon, GrantedAuthority arayüzünü uygulayan herhangi bir sınıfın nesnelerini içerebilir."
Neden Gerekli? Düşün ki, sisteminde Role (Rol) adında bir sınıfın var ve bu sınıf GrantedAuthority arayüzünü uyguluyor.
Belki bir de Permission (İzin) adında başka bir sınıfın var ve o da GrantedAuthority arayüzünü uyguluyor.

Eğer metot Collection<GrantedAuthority> döndürseydi, içine sadece doğrudan GrantedAuthority tipinde nesneler koyabilirdin.
Ama Collection<? extends GrantedAuthority> sayesinde, metot hem Collection<Role> döndürebilir hem de Collection<Permission> döndürebilir.
Bu, kodumuzu çok daha esnek ve yeniden kullanılabilir hale getirir.
Spring Security'e "Sana GrantedAuthority soyundan gelen bir şeyler içeren bir koleksiyon vereceğim,
tipi tam olarak Role mi Permission mı, bununla sen uğraşma" demiş oluyoruz.

return Collections.emptyList(); Ne Demek?

Bu satır, "içi boş, değiştirilemez bir liste döndür" anlamına gelir.
Kodun üzerindeki Almanca yorumda (// Vorerst hat der Benutzer keine Rollen.) belirtildiği gibi, bu "şimdilik kullanıcının hiçbir rolü/yetkisi yok" demektir.
 Genellikle geliştirmenin ilk aşamalarında veya belirli kullanıcı tipleri için (örneğin sadece kayıt olmuş ama henüz onaylanmamış bir kullanıcı) kullanılır.*/