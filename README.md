# Boekenwinkel

## 1. Beschrijving

Boekenwinkel is een webapplicatie voor een online boekenwinkel. Gebruikers kunnen het beschikbare boekenaanbod bekijken, een account aanmaken, aanmelden, boeken reserveren, de betalingsstatus opvolgen en reviews bekijken of toevoegen.

De applicatie bestaat uit een frontend, een backend en een MySQL-database. De frontend biedt de gebruikersinterface aan. De backend verwerkt aanvragen, voert bedrijfslogica uit en communiceert met de database.

Dit project werd ontwikkeld in het kader van de opleiding **Graduaat Programmeren aan Erasmushogeschool Brussel**.

**GitHub-repository:** https://github.com/aliasaad22/boekenwinkel

## 2. Functionaliteiten

De applicatie bevat de volgende functionaliteiten:

### Boeken
- Boeken opvragen en bekijken.
- Boekgegevens ophalen via de backend-API.
- Boekgegevens bewaren in de MySQL-database.
- De beschikbare voorraad opvolgen.

### Gebruikers
- Een gebruikersaccount aanmaken.
- Aanmelden bij de applicatie.
- Wachtwoorden hashen met Spring Security Crypto.

### Reserveringen en betalingen
- Een reservering aanmaken voor een boek.
- De status van een reservering opvolgen.
- Een reservering bevestigen.
- De beschikbare voorraad verminderen wanneer een reservering wordt aangemaakt.
- De betalingsstatus opvolgen.
- Een betaling registreren nadat de reservering bevestigd is.

### Reviews
- Een review voor een boek toevoegen.
- Reviews van een boek bekijken.
- De gemiddelde score van een boek opvragen.
- Een review verwijderen.
- Scores valideren en ongeldige scores weigeren.
- Controleren of aan de voorwaarden voor het toevoegen van een review is voldaan.

De concrete API-endpoints, verzoekgegevens, antwoorden en HTTP-statuscodes zijn beschreven in `docs/API-DOCUMENTATIE.md`.

## 3. Gebruikte technologieën

### Backend

| Technologie | Doel |
|---|---|
| Java 21 | Programmeertaal voor de backend |
| Spring Boot | Opbouw en uitvoering van de backend |
| Spring Web / Spring MVC | Verwerken van HTTP-verzoeken en REST-endpoints |
| Spring Data JPA | Toegang tot databasegegevens via repositories |
| Hibernate | Koppeling tussen Java-objecten en databasetabellen |
| MySQL | Relationele database voor de applicatiegegevens |
| Maven | Beheren van dependencies, bouwen en testen |
| Maven Wrapper | Maven-opdrachten uitvoeren zonder een afzonderlijke Maven-installatie |
| Spring Security Crypto | Hashen van wachtwoorden |

### Frontend

| Technologie | Doel |
|---|---|
| HTML | Structuur van de webpagina |
| CSS | Vormgeving van de gebruikersinterface |
| JavaScript | Interactie en verwerking aan de clientzijde |
| Fetch API | HTTP-verzoeken versturen naar de backend |

### Testen

De backend bevat tests voor onder andere de boek-, gebruikers-, reserverings- en reviewfunctionaliteiten. De tests worden uitgevoerd met de Maven Wrapper.

De actuele testresultaten zijn te controleren met de instructies in hoofdstuk 8.

## 4. Projectstructuur

De repository is als volgt georganiseerd:

```text
boekenwinkel/
├── backend/
│   └── boekenwinkel/
│       ├── .mvn/
│       ├── mvnw
│       ├── mvnw.cmd
│       ├── pom.xml
│       └── src/
│           ├── main/
│           └── test/
├── frontend/
│   ├── index.html
│   ├── app.js
│   └── style.css
├── docs/
│   ├── PLANNING.md
│   ├── API-DOCUMENTATIE.md
│   └── TESTDOCUMENTATIE.md
└── README.md
```

### Toelichting

- `backend/boekenwinkel/` bevat de Java- en Spring Boot-backend.
- `src/main/` bevat de applicatiecode en configuratie.
- `src/test/` bevat de backendtests.
- `pom.xml` beschrijft de Maven-configuratie en dependencies.
- `mvnw` en `mvnw.cmd` zijn de Maven Wrapper-scripts voor Unix-gebaseerde systemen en Windows.
- `frontend/` bevat de HTML-, CSS- en JavaScript-bestanden.
- `docs/PLANNING.md` beschrijft de projectplanning.
- `docs/API-DOCUMENTATIE.md` beschrijft de API-endpoints.
- `docs/TESTDOCUMENTATIE.md` beschrijft de tests en hun resultaten.
- `README.md` bevat de algemene projectinformatie en de installatie- en uitvoerinstructies.

## 5. Vereisten

Om het project lokaal uit te voeren, heb je nodig:

- Java JDK 21.
- MySQL Server.
- Git.
- Een moderne webbrowser.
- Een terminal, bijvoorbeeld PowerShell op Windows.

Een afzonderlijke Maven-installatie is niet nodig, omdat het project een Maven Wrapper bevat.

## 6. Installatie en configuratie

### 6.1. Repository downloaden

Open een terminal en voer uit:

```bash
git clone https://github.com/aliasaad22/boekenwinkel.git
cd boekenwinkel
```

De repository wordt nu lokaal gedownload.

### 6.2. MySQL configureren

Start MySQL Server en meld je aan met een MySQL-account dat databases en gebruikers mag beheren.

Maak de database aan als deze nog niet bestaat:

```sql
CREATE DATABASE IF NOT EXISTS boekenwinkel;
```

De backend moet vervolgens met deze database kunnen verbinden. Gebruik de database-instellingen die in `backend/boekenwinkel/src/main/resources/application.properties` staan.

Als je met een afzonderlijke databasegebruiker werkt, kun je die aanmaken met:

```sql
CREATE USER IF NOT EXISTS 'boekenwinkel_app'@'localhost'
IDENTIFIED BY 'KIES_EEN_STERK_WACHTWOORD';
```

Geef die gebruiker toegang tot de projectdatabase:

```sql
GRANT ALL PRIVILEGES ON boekenwinkel.*
TO 'boekenwinkel_app'@'localhost';

FLUSH PRIVILEGES;
```

Vervang het voorbeeldwachtwoord door een eigen sterk wachtwoord.

**Let op:** gebruik deze databasegebruikersnaam alleen wanneer die overeenkomt met je backendconfiguratie. Als de configuratie een andere gebruiker gebruikt, moet je de SQL-opdrachten en de configuratie op elkaar afstemmen.

### 6.3. Databaseverbinding instellen

Open het bestand:

`backend/boekenwinkel/src/main/resources/application.properties`

Controleer of de database-URL, gebruikersnaam en het wachtwoord overeenkomen met je MySQL-configuratie.

Een voorbeeld van een configuratie is:

```properties
spring.application.name=boekenwinkel

spring.datasource.url=jdbc:mysql://localhost:3306/boekenwinkel
spring.datasource.username=boekenwinkel_app
spring.datasource.password=JOUW_WACHTWOORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Gebruik de bestaande projectconfiguratie als uitgangspunt en vervang de voorbeeldgegevens waar nodig.

De belangrijkste instellingen zijn:

- `spring.datasource.url`: bepaalt met welke database de backend verbinding maakt.
- `spring.datasource.username`: bepaalt welke MySQL-gebruiker wordt gebruikt.
- `spring.datasource.password`: bevat het wachtwoord van die gebruiker.
- `spring.jpa.hibernate.ddl-auto=update`: laat Hibernate het databaseschema tijdens de ontwikkeling aanpassen aan de JPA-entiteiten.
- `spring.jpa.show-sql=true`: toont SQL-verzoeken in de console.

De instelling `ddl-auto=update` is handig tijdens de ontwikkeling. Voor productie is een gecontroleerde strategie voor databasewijzigingen aanbevolen.

**Beveiliging:** commit geen echte wachtwoorden of andere geheimen naar GitHub. Bewaar vertrouwelijke configuratiegegevens buiten de broncode.

## 7. De applicatie starten

### 7.1. Backend starten op Windows

Open PowerShell of de terminal in IntelliJ IDEA.

Navigeer naar de backendmap:

```powershell
cd backend\boekenwinkel
```

Start de applicatie:

```powershell
.\mvnw.cmd spring-boot:run
```

Wacht totdat Spring Boot aangeeft dat de applicatie gestart is. Controleer de console als er problemen zijn met Java, MySQL of de configuratie.

### 7.2. Backend starten op Linux of macOS

Navigeer naar de backendmap:

```bash
cd backend/boekenwinkel
```

Start de applicatie:

```bash
./mvnw spring-boot:run
```

Als het script geen uitvoerrechten heeft, kun je die indien nodig instellen:

```bash
chmod +x mvnw
```

De backend gebruikt standaard poort `8080`, tenzij de configuratie anders bepaalt.

Basisadres:

`http://localhost:8080`

Dit basisadres hoeft geen aparte webpagina te tonen. Gebruik de endpoints uit `docs/API-DOCUMENTATIE.md` om de API te testen.

### 7.3. Frontend openen

Open `frontend/index.html` in een browser of gebruik een lokale ontwikkelserver.

De frontend verstuurt HTTP-verzoeken naar de backend met de Fetch API. De backend moet actief zijn wanneer je functies gebruikt die gegevens uit de database nodig hebben.

De lokale frontend moet bereikbaar zijn vanaf een oorsprong die door de CORS-configuratie van de backend wordt toegestaan. Controleer die configuratie wanneer de browser verzoeken blokkeert.

In de ontwikkelomgeving werd de frontend onder andere via `localhost:63342` gebruikt.

## 8. Tests uitvoeren

De backendtests kunnen worden uitgevoerd met de Maven Wrapper.

### Windows

Open een tweede terminal en voer uit:

```powershell
cd backend\boekenwinkel
.\mvnw.cmd test
```

### Linux of macOS

```bash
cd backend/boekenwinkel
./mvnw test
```

Maven voert de beschikbare tests uit en toont de resultaten in de terminal.

Controleer:

- Het aantal uitgevoerde tests.
- Het aantal geslaagde tests.
- Het aantal fouten en mislukte tests.
- De uiteindelijke buildstatus.

Bij een recente lokale testuitvoering werden **39 tests uitgevoerd zonder fouten of mislukte tests**, met als resultaat `BUILD SUCCESS`.

Voer de tests opnieuw uit wanneer de code verandert. De testresultaten kunnen namelijk veranderen na nieuwe wijzigingen.

Raadpleeg `docs/TESTDOCUMENTATIE.md` voor de gedocumenteerde testresultaten. Zorg ervoor dat het aantal tests en de resultaten in dat document overeenkomen met de laatste testuitvoering.

Een geslaagde testuitvoering betekent niet automatisch dat iedere mogelijke gebruikerssituatie getest is.

## 9. API-documentatie

De backend stelt REST-endpoints beschikbaar voor de verschillende onderdelen van de applicatie.

De volledige documentatie staat in:

`docs/API-DOCUMENTATIE.md`

Deze documentatie beschrijft onder andere:

- Boeken.
- Gebruikers.
- Reserveringen.
- Reviews.
- Verwachte verzoekgegevens en antwoorden.
- Relevante HTTP-statuscodes.
- Toegangsregels en beveiligingsaandachtspunten.

Gebruik de documentatie om de API te testen met een geschikt HTTP-testprogramma of vanuit de frontend.

## 10. Planning en documentatie

De projectdocumentatie staat in de map `docs/`.

| Bestand | Beschrijving |
|---|---|
| `docs/PLANNING.md` | Projectdoel, planning, fasen en kwaliteitscriteria |
| `docs/API-DOCUMENTATIE.md` | Backend-endpoints, verzoeken, antwoorden en statuscodes |
| `docs/TESTDOCUMENTATIE.md` | Tests en testresultaten |

Samen met de broncode geven deze documenten een overzicht van de ontwikkeling, werking en controle van het project.

## 11. Beveiliging en beperkingen

De applicatie gebruikt Spring Security Crypto voor het hashen van wachtwoorden. Hierdoor hoeven wachtwoorden niet als gewone tekst opgeslagen te worden.

Het hashen van wachtwoorden is echter niet hetzelfde als het beveiligen van alle API-endpoints.

De huidige beveiligingsconfiguratie laat bepaalde endpoints voor boeken, gebruikers, reserveringen en reviews publiek toegankelijk zijn. Daardoor is de applicatie niet zonder verdere controle geschikt voor productiegebruik.

Mogelijke verbeteringen voor een productieomgeving zijn:

- Geschikte authenticatie en autorisatie implementeren.
- Controleren of een gebruiker een bepaalde reservering of review mag beheren.
- Vertrouwelijke configuratiegegevens buiten de broncode bewaren.
- Invoer aan de backend valideren.
- Foutmeldingen controleren op onbedoelde blootstelling van gevoelige informatie.
- HTTPS gebruiken.
- Databasewijzigingen via een gecontroleerde migratiestrategie beheren.
- Regelmatig back-ups maken en herstel testen.

Deze aandachtspunten beschrijven mogelijke verdere verbeteringen en betekenen niet dat al deze functies momenteel geïmplementeerd zijn.

## 12. Bronnen en AI-ondersteuning

Tijdens de ontwikkeling werden technische documentatie en ontwikkelhulpmiddelen gebruikt.

### Technische bronnen

- [Spring Boot-documentatie](https://docs.spring.io/spring-boot/index.html)
- [Spring Data JPA-documentatie](https://docs.spring.io/spring-data/jpa/reference/)
- [MySQL-documentatie](https://dev.mysql.com/doc/)
- [Java-documentatie](https://docs.oracle.com/en/java/)
- [Git-documentatie](https://git-scm.com/doc/)
- [Maven-documentatie](https://maven.apache.org/guides/)



## 13. Auteur en project

Dit project werd ontwikkeld in het kader van de opleiding **Graduaat Programmeren aan Erasmushogeschool Brussel**.

**GitHub-repository:** https://github.com/aliasaad22/boekenwinkel
