# Boekenwinkel

## 1. Beschrijving

Boekenwinkel is een webapplicatie voor een online boekenwinkel. Gebruikers kunnen het beschikbare boekenaanbod bekijken, een account aanmaken, zich aanmelden, reserveringen beheren en reviews over boeken lezen en toevoegen.

De applicatie bestaat uit een backend die verantwoordelijk is voor de verwerking en opslag van gegevens en een frontend waarmee gebruikers met de applicatie kunnen werken. De gegevens worden opgeslagen in een MySQL-database.

Dit project werd ontwikkeld als onderdeel van de opleiding **Graduaat Programmeren aan Erasmushogeschool Brussel**.

## 2. Functionaliteiten

De applicatie ondersteunt de volgende functionaliteiten:

- Boeken opvragen en bekijken.
- Boekgegevens beheren via de backend-API.
- Gebruikers registreren en aanmelden.
- Wachtwoorden veilig hashen met Spring Security Crypto.
- Reserveringen aanmaken en beheren.
- Reserveringen bevestigen en betalingen registreren.
- Reviews toevoegen, bekijken en verwijderen.
- Scores van reviews valideren.
- Gemiddelde reviewscores van boeken opvragen.
- Gegevens opslaan en ophalen uit een MySQL-database.
- Backendfunctionaliteiten aanbieden via REST-endpoints.

De beschikbare functionaliteiten en toegangsregels worden verder beschreven in de API-documentatie.

## 3. Gebruikte technologieën

### Backend

- **Java 21** — programmeertaal.
- **Spring Boot** — framework voor de backendapplicatie.
- **Spring Web** — ondersteuning voor webfunctionaliteiten en REST-endpoints.
- **Spring Data JPA** — toegang tot en beheer van databasegegevens.
- **Hibernate** — ORM-technologie voor de koppeling tussen Java-objecten en databasetabellen.
- **MySQL** — relationele database.
- **Maven** — beheer van dependencies en het bouwen en testen van het project.
- **Maven Wrapper** — maakt het mogelijk Maven-opdrachten uit te voeren zonder een afzonderlijke Maven-installatie.
- **Spring Security Crypto** — ondersteuning voor het hashen van wachtwoorden.

### Frontend

- **HTML** — structuur van de webpagina.
- **CSS** — opmaak en vormgeving.
- **JavaScript** — interactie en verwerking aan de clientzijde.
- **Fetch API** — communicatie tussen de frontend en de backend via HTTP-verzoeken.

## 4. Projectstructuur

Het project is als volgt georganiseerd:

```text
boekenwinkel/
├── backend/
│   └── boekenwinkel/
│       ├── .mvn/
│       ├── mvnw
│       ├── mvnw.cmd
│       ├── pom.xml
│       └── src/
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

**Toelichting:**

- `backend/boekenwinkel/` bevat de Java- en Spring Boot-backend.
- `pom.xml` beschrijft de Maven-configuratie en de dependencies.
- `mvnw` en `mvnw.cmd` zijn de Maven Wrapper-bestanden voor respectievelijk Unix-gebaseerde systemen en Windows.
- `frontend/` bevat de HTML-, CSS- en JavaScript-bestanden.
- `docs/PLANNING.md` beschrijft de planning van het project.
- `docs/API-DOCUMENTATIE.md` beschrijft de backend-endpoints, verzoeken en antwoorden.
- `docs/TESTDOCUMENTATIE.md` bevat de testresultaten en testinformatie.
- `README.md` bevat de algemene informatie en instructies om het project uit te voeren.

## 5. Vereisten

Om het project lokaal te installeren en uit te voeren, heb je het volgende nodig:

- Java JDK 21.
- MySQL Server.
- Git, om de repository te downloaden.
- Een moderne webbrowser.
- Een terminal, zoals PowerShell op Windows.

Het project bevat een Maven Wrapper. Daarom hoef je Maven niet afzonderlijk te installeren. Bij het eerste gebruik kan de Wrapper de vereiste Maven-versie downloaden.

## 6. Installatie en configuratie

### 6.1. De repository downloaden

Open een terminal en voer de volgende opdrachten uit:

```bash
git clone https://github.com/aliasaad22/boekenwinkel.git
cd boekenwinkel
```

De volledige projectmap wordt nu lokaal aangemaakt.

### 6.2. MySQL starten

Controleer of MySQL Server actief is en of je toegang hebt tot de MySQL-server.

Meld je aan met een MySQL-account dat databases en gebruikers mag beheren.

Maak de database aan als die nog niet bestaat:

```sql
CREATE DATABASE IF NOT EXISTS boekenwinkel;
```

De backend gebruikt de databasegebruiker `boekenwinkel_app`. Maak deze gebruiker aan als die nog niet bestaat:

```sql
CREATE USER IF NOT EXISTS 'boekenwinkel_app'@'localhost'
IDENTIFIED BY 'KIES_EEN_WACHTWOORD';
```

Geef de gebruiker toegang tot de database:

```sql
GRANT ALL PRIVILEGES ON boekenwinkel.*
TO 'boekenwinkel_app'@'localhost';

FLUSH PRIVILEGES;
```

Vervang `KIES_EEN_WACHTWOORD` door een sterk wachtwoord.

**Belangrijk:** als de databasegebruiker al bestaat, wordt het bestaande wachtwoord door `CREATE USER IF NOT EXISTS` niet automatisch gewijzigd. Zorg ervoor dat het wachtwoord overeenkomt met de configuratie van de backend.

### 6.3. De databaseverbinding configureren

Open het configuratiebestand:

`backend/boekenwinkel/src/main/resources/application.properties`

Controleer of de databaseverbinding correct ingesteld is:

```properties
spring.application.name=boekenwinkel

spring.datasource.url=jdbc:mysql://localhost:3306/boekenwinkel
spring.datasource.username=boekenwinkel_app
spring.datasource.password=JOUW_WACHTWOORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Vervang `JOUW_WACHTWOORD` door het wachtwoord van de MySQL-gebruiker.

De belangrijkste instellingen zijn:

- `spring.datasource.url` bepaalt met welke database de backend verbinding maakt.
- `spring.datasource.username` bepaalt welke databasegebruiker wordt gebruikt.
- `spring.datasource.password` bevat het wachtwoord van die gebruiker.
- `spring.jpa.hibernate.ddl-auto=update` laat Hibernate het databaseschema aanpassen aan de JPA-entiteiten.
- `spring.jpa.show-sql=true` toont SQL-verzoeken in de console.

De instelling `ddl-auto=update` is handig tijdens de ontwikkeling. Voor een productieomgeving is een gecontroleerde database-migratiestrategie aanbevolen.

**Beveiliging:** plaats geen echte wachtwoorden of andere geheimen in een publieke GitHub-repository. Gebruik voor een gedeelde of productieomgeving een geschikte manier om configuratiegeheimen buiten de broncode te bewaren.

## 7. De applicatie starten

### 7.1. De backend starten op Windows

Open PowerShell of de terminal in IntelliJ IDEA.

Navigeer vanuit de hoofdmap van het project naar de backendmap:

```powershell
cd backend\boekenwinkel
```

Start vervolgens Spring Boot:

```powershell
.\mvnw.cmd spring-boot:run
```

Wacht totdat Spring Boot aangeeft dat de applicatie gestart is. Controleer de console op fouten met betrekking tot Java, MySQL of de configuratie.

### 7.2. De backend starten op Linux of macOS

Navigeer naar de backendmap:

```bash
cd backend/boekenwinkel
```

Voer daarna uit:

```bash
./mvnw spring-boot:run
```

Als de wrapper niet uitvoerbaar is, kun je indien nodig eerst de uitvoerrechten instellen:

```bash
chmod +x mvnw
```

De backend gebruikt standaard poort `8080`, tenzij de configuratie anders bepaalt.

Basisadres:

`http://localhost:8080`

De basis-URL is niet noodzakelijk een webpagina. Gebruik de endpoints uit `docs/API-DOCUMENTATIE.md` om de beschikbare API-functionaliteiten te testen.

### 7.3. De frontend openen

Open het bestand `frontend/index.html` in een moderne webbrowser of gebruik een lokale ontwikkelserver.

De frontend communiceert met de backend via de Fetch API. Zorg ervoor dat de backend actief is voordat je functionaliteiten gebruikt waarvoor gegevens uit de database nodig zijn.

Als de browser verzoeken naar de backend blokkeert, controleer dan de CORS-configuratie en gebruik een geschikte lokale webserver.

## 8. Tests uitvoeren

De backendtests kunnen met de Maven Wrapper worden uitgevoerd.

Open een tweede terminal en navigeer naar de backendmap.

Op Windows:

```powershell
cd backend\boekenwinkel
.\mvnw.cmd test
```

Op Linux of macOS:

```bash
cd backend/boekenwinkel
./mvnw test
```

Maven voert de beschikbare tests uit en toont de resultaten in de terminal.

Controleer de uitvoer op:

- Het aantal uitgevoerde tests.
- Het aantal geslaagde tests.
- Eventuele fouten of mislukte tests.
- De uiteindelijke Maven-buildstatus.

Een succesvolle build is een indicatie dat de uitgevoerde tests geslaagd zijn, maar betekent niet automatisch dat iedere mogelijke gebruikerssituatie getest is.

Raadpleeg `docs/TESTDOCUMENTATIE.md` voor de gedocumenteerde testresultaten.

## 9. API-documentatie

De backend biedt REST-endpoints waarmee de frontend gegevens kan opvragen en bewerken.

De volledige endpointdocumentatie staat in:

`docs/API-DOCUMENTATIE.md`

Deze documentatie beschrijft onder andere:

- Boeken.
- Gebruikers.
- Reserveringen.
- Reviews.
- Verwachte verzoekgegevens en antwoorden.
- Relevante HTTP-statuscodes.
- Beveiligingsaandachtspunten.

Gebruik deze documentatie om de API te testen met een geschikt HTTP-testprogramma of vanuit de frontend.

## 10. Planning en testdocumentatie

De projectdocumentatie staat in de map `docs/`.

| Bestand | Inhoud |
|---|---|
| `docs/PLANNING.md` | Projectdoel, technologieën, fasen en kwaliteitscriteria. |
| `docs/API-DOCUMENTATIE.md` | Beschrijving van de backend-endpoints en HTTP-statuscodes. |
| `docs/TESTDOCUMENTATIE.md` | Gedocumenteerde tests en testresultaten. |

Deze documenten vullen de README aan en geven meer informatie over de ontwikkeling, werking en controle van het project.

## 11. Beveiliging en aandachtspunten

De applicatie gebruikt Spring Security Crypto voor het hashen van wachtwoorden. Wachtwoorden horen niet als gewone tekst in de database opgeslagen te worden.

De huidige beveiligingsconfiguratie laat de beschreven boeken-, gebruikers-, reserverings- en review-endpoints publiek toegankelijk zijn. Dit is een belangrijk aandachtspunt: het hashen van wachtwoorden betekent niet dat de API automatisch beschermd is.

Voor een productieomgeving zijn onder andere de volgende verbeteringen aanbevolen:

- Geschikte authenticatie en autorisatie implementeren.
- Controleren welke gebruiker een reservering of review mag beheren.
- Vertrouwelijke configuratiegegevens buiten de broncode bewaren.
- Invoer aan de backend valideren.
- Foutmeldingen controleren zodat ze geen gevoelige informatie blootgeven.
- HTTPS gebruiken in een productieomgeving.
- Een gecontroleerde strategie gebruiken voor database-migraties en back-ups.

De bestaande functionaliteiten en configuratie moeten worden beoordeeld voordat de applicatie als productiegeschikt wordt beschouwd.

## 12. Bronnen en AI-ondersteuning

Tijdens de ontwikkeling zijn technische documentatie en ontwikkelhulpmiddelen gebruikt.

### Technische bronnen

- [Spring Boot-documentatie](https://docs.spring.io/spring-boot/index.html)
- [Spring Data JPA-documentatie](https://docs.spring.io/spring-data/jpa/reference/)
- [MySQL-documentatie](https://dev.mysql.com/doc/)
- [Java-documentatie](https://docs.oracle.com/en/java/)
- [Git-documentatie](https://git-scm.com/doc/)
- [Maven-documentatie](https://maven.apache.org/guides/)

### Gebruik van AI

ChatGPT werd gebruikt als ondersteunend hulpmiddel tijdens de ontwikkeling. De ondersteuning omvatte onder andere:

- Het begrijpen van programmeerconcepten.
- Het analyseren en oplossen van programmeerfouten.
- Het ondersteunen bij het schrijven en controleren van tests.
- Het verbeteren van technische documentatie.
- Het verduidelijken van Spring Boot-, Java- en databaseconcepten.

Voorgestelde wijzigingen werden tijdens de ontwikkeling gecontroleerd en getest waar van toepassing. Deze vermelding beschrijft het gebruik van AI als hulpmiddel en vervangt niet de eigen controle, het begrip en de verantwoordelijkheid van de ontwikkelaar voor de ingediende code.

## 13. Auteur en project

Dit project werd ontwikkeld in het kader van de opleiding Graduaat Programmeren aan Erasmushogeschool Brussel.

GitHub-repository: [aliasaad22/boekenwinkel](https://github.com/aliasaad22/boekenwinkel)