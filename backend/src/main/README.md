# Passwort-Manager API

Bei diesem Projekt handelt es sich um eine Passwort-Manager-Backend-Anwendung mit einer sicheren REST-API auf JWT-Basis, die im Rahmen der Ausbildung "Fachinformatiker für Anwendungsentwicklung" entwickelt wurde.

## ✨ Merkmale

- Sichere Benutzerregistrierung und Anmeldung (mit JWT)
- Vollständige CRUD-Operationen (Erstellen, Lesen, Aktualisieren, Löschen) für Passwortdatensätze
- Benutzerspezifischer Datenzugriff (Jeder Benutzer sieht nur seine eigenen Passwörter)
- Moderne und mehrschichtige Architektur (Controller, Service, Repository)

## 🛠️ Technologie-Stack

- Backend:** Java 17, Spring Boot 3
- Datenbank:** MySQL
- Sicherheit:** Spring Security, JSON Web Tokens (JWT)
- **Datenzugriff:** Spring Data JPA / Hibernate
- **Build Tool:** Maven

## 🚀 API Endpoints

| Methode  | URL                   | Beschreibung                                               | Zugriff   |
|----------|-----------------------|------------------------------------------------------------|-----------|
| `POST`   | `/api/auth/register`  | Registriert einen neuen Benutzer.                          | Public    |
| `POST`   | `/api/auth/login`     | Meldet den Benutzer an und liefert JWT.                    | Public    |
| `GET`    | `/api/passwords`      | Listet die Passwörter des angemeldeten Benutzers auf.      | Geschützt |
| `POST`   | `/api/passwords`      | Erstellt einen neuen Passwortdatensatz.                    | Geschützt |
| `PUT`    | `/api/passwords/{id}` | Aktualisiert den Passwortdatensatz mit der angegebenen ID. |           |
| `DELETE` | `/api/passwords/{id}` | Löscht den Passwort-Datensatz mit der angegebenen ID.      | Geschützt |

## ⚙️ Wie wird ausgeführt?

1. klonen Sie das Projekt.
2. erstellen Sie eine Datenbank mit dem Namen `password_manager_db` auf Ihrem lokalen MySQL-Server.
3. aktualisieren Sie den Datenbank-Benutzernamen und das Passwort in `src/main/resources/application.properties` auf Ihren eigenen.
4. starten Sie die Anwendung von Ihrer IDE oder mit dem `mvn spring-boot:run` Befehl.