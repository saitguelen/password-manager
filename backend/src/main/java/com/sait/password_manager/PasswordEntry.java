package com.sait.password_manager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "password_entries")
public class PasswordEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String website;

    @Column(nullable = false)
    private String username; // Der Benutzername für die Webseite

    @Column(nullable = false)
    private String encryptedPassword; // Das Passwort für die Webseite

    // Dies stellt die Beziehung zwischen PasswordEntry und User her.
    // Viele Passworteinträge können zu einem Benutzer gehören.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore // Verhindert, dass dieses Feld in der JSON-Antwort angezeigt wird.
    private User user;
}
/*
@Data (Lombok)
Zweck: Diese Annotation ist anders als die anderen und stammt aus der Lombok-Bibliothek.
Ihr Ziel ist es, uns vom Schreiben von "Boilerplate"-Code zu befreien, also von sich wiederholendem Standardcode.

Wenn du die @Data-Annotation über eine Klasse setzt, generiert Lombok zur Kompilierzeit (compile time) automatisch Folgendes für diese Klasse:

getter-Methoden für alle Felder (getId(), getName()).
setter-Methoden für alle nicht-final Felder (setId(), setName()).
Eine toString()-Methode (um den Inhalt der Klasse auszugeben).
equals()- und hashCode()-Methoden (für Objektvergleiche).
Einen Konstruktor, der alle erforderlichen Felder enthält.
Kurz gesagt, es erspart dir das Schreiben von Dutzenden von Codezeilen und lässt die Klasse sauberer aussehen.

* @Data anotasyonunu bir sınıfın üzerine koyduğunda, Lombok derleme anında (compile time) o sınıf için şunları otomatik olarak oluşturur:

Tüm alanlar için getter metotları (getId(), getName()).
Tüm final olmayan alanlar için setter metotları (setId(), setName()).
toString() metodu (sınıfın içeriğini yazdırmak için).
equals() ve hashCode() metotları (nesne karşılaştırmaları için).
Gerekli alanları içeren bir constructor.
Kısacası, onlarca satır kod yazmaktan kurtarır ve sınıfın daha temiz görünmesini sağlar.*/