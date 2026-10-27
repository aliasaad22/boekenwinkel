
# API-documentatie — Boekenwinkel

## 1. Inleiding

De Boekenwinkel-backend is gebouwd met Java en Spring Boot. De API gebruikt HTTP-verzoeken om boeken, gebruikers, reserveringen en reviews te beheren.

De standaardbasis-URL is:

`http://localhost:8080`

De API retourneert gegevens in JSON-formaat.

## 2. Boeken

Basisendpoint: `/books`

| Methode | Endpoint | Beschrijving | Successtatus |
|---|---|---|---|
| GET | `/books` | Alle boeken ophalen | 200 OK |
| GET | `/books/{id}` | Een boek ophalen via ID | 200 OK |
| POST | `/books` | Een boek toevoegen | 201 Created |
| PUT | `/books/{id}` | Een boek aanpassen | 200 OK |
| DELETE | `/books/{id}` | Een boek verwijderen | 204 No Content |

Bij een onbekend boek kunnen de GET-, PUT- en DELETE-verzoeken een `404 Not Found` opleveren.

## 3. Gebruikers

Basisendpoint: `/users`

| Methode | Endpoint | Beschrijving | Successtatus |
|---|---|---|---|
| POST | `/users` | Een gebruiker registreren | 201 Created |
| GET | `/users?email={email}` | Een gebruiker zoeken via e-mailadres | 200 OK |
| GET | `/users/{id}` | Een gebruiker ophalen via ID | 200 OK |
| POST | `/users/login` | Inloggen | 200 OK |
| GET | `/users/{id}/profile` | Een gebruikersprofiel ophalen | 200 OK |
| GET | `/users/{id}/purchased-books` | Aangekochte boeken ophalen | 200 OK |

### 3.1 Gebruiker registreren

Verzoek: `POST /users`

Voorbeeld van de JSON-invoer:

```json
{
  "name": "Testgebruiker",
  "email": "test@example.com",
  "password": "MijnVeiligeWachtwoord123"
}
```

Bij een succesvolle registratie retourneert de API `201 Created`.

De response bevat het gebruikers-ID, de naam en het e-mailadres. Het wachtwoord wordt niet teruggegeven. De service codeert het wachtwoord voordat het wordt opgeslagen.

Als het e-mailadres al bestaat, wordt een `EmailAlreadyExistsException` gegenereerd. De uiteindelijke HTTP-status hangt af van de foutafhandeling in de applicatie.

### 3.2 Inloggen

Verzoek: `POST /users/login`

Voorbeeld van de JSON-invoer:

```json
{
  "email": "test@example.com",
  "password": "MijnVeiligeWachtwoord123"
}
```

Bij een succesvolle login retourneert de API het gebruikers-ID, de naam en het e-mailadres.

Bij onjuiste inloggegevens retourneert de controller `401 Unauthorized`.

De loginmethode controleert het ingevoerde wachtwoord met het opgeslagen gecodeerde wachtwoord.

### 3.3 Gebruiker zoeken

Een gebruiker kan worden opgezocht via het e-mailadres:

`GET /users?email=test@example.com`

Een gebruiker kan ook worden opgezocht via het ID:

`GET /users/{id}`

Bij een gevonden gebruiker retourneert de API het gebruikers-ID, de naam en het e-mailadres. Als de gebruiker niet bestaat, retourneert de controller `404 Not Found`.

### 3.4 Gebruikersprofiel ophalen

Verzoek: `GET /users/{id}/profile`

Dit endpoint retourneert de profielgegevens van de gebruiker en diens reserveringen.

Als de gebruiker niet bestaat, retourneert de service `404 Not Found`.

### 3.5 Aangekochte boeken ophalen

Verzoek: `GET /users/{id}/purchased-books`

Dit endpoint retourneert de boeken die de gebruiker heeft aangekocht. Alleen reserveringen met betaalstatus `PAID` worden meegenomen.

Als de gebruiker niet bestaat, retourneert de service `404 Not Found`.

## 4. Reserveringen

Basisendpoint: `/reservations`

| Methode | Endpoint | Beschrijving | Successtatus |
|---|---|---|---|
| POST | `/reservations` | Een reservering aanmaken | 200 OK |
| PUT | `/reservations/{id}/confirm` | Een reservering bevestigen | 200 OK |
| GET | `/reservations` | Alle reserveringen ophalen | 200 OK |
| PUT | `/reservations/{id}/pay` | Een reservering als betaald markeren | 200 OK |
| GET | `/reservations/user/{userId}` | Reserveringen van een gebruiker ophalen | 200 OK |

### 4.1 Reservering aanmaken

Verzoek: `POST /reservations`

Voorbeeld van de JSON-invoer:

```json
{
  "userId": 2,
  "bookId": 2,
  "pickupDate": "2026-10-30"
}
```

De invoer bevat het gebruikers-ID, het boek-ID en de gewenste afhaaldatum.

De afhaaldatum wordt in de huidige code als tekst (`String`) verwerkt en vervolgens omgezet naar een datum.

De service controleert:
- of de afhaaldatum geldig is en niet in het verleden ligt;
- of het boek bestaat;
- of de gebruiker bestaat;
- of er voldoende voorraad beschikbaar is.

Als de invoer ongeldig is, kan de API `400 Bad Request` retourneren. Als een boek of gebruiker niet bestaat, kan `404 Not Found` optreden. Bij onvoldoende voorraad kan `409 Conflict` optreden.

### 4.2 Reservering bevestigen

Verzoek: `PUT /reservations/{id}/confirm`

Bij een geldige bevestiging wordt de reservering bevestigd en wordt de voorraad van het boek met één verminderd.

Een reservering kan alleen worden bevestigd wanneer de status `PENDING` is en er voorraad beschikbaar is.

Bij een niet-bestaande reservering kan `404 Not Found` optreden. Als de reservering niet in de juiste status staat of de voorraad onvoldoende is, kan `409 Conflict` optreden.

### 4.3 Betaling markeren

Verzoek: `PUT /reservations/{id}/pay`

Dit endpoint markeert een bevestigde reservering als betaald. Het betreft een simulatie van betaling en geen echte online betaling.

De reservering moet de status `CONFIRMED` hebben. Een betaling kan niet opnieuw worden geregistreerd als de betaalstatus al `PAID` is.

Afhankelijk van de situatie kan de API `404 Not Found` of `409 Conflict` retourneren.

### 4.4 Reserveringen ophalen

Alle reserveringen kunnen worden opgehaald via:

`GET /reservations`

De reserveringen van één gebruiker kunnen worden opgehaald via:

`GET /reservations/user/{userId}`

Bij succes retourneert de API een lijst met reserveringen.

## 5. Reviews

Basisendpoint: `/reviews`

| Methode | Endpoint | Beschrijving | Successtatus |
|---|---|---|---|
| GET | `/reviews/book/{bookId}` | Reviews van een boek ophalen | 200 OK |
| POST | `/reviews` | Een review toevoegen | 200 OK |
| GET | `/reviews/book/{bookId}/average` | Gemiddelde score ophalen | 200 OK |
| DELETE | `/reviews/{id}` | Een review verwijderen | 200 OK |

### 5.1 Reviews van een boek ophalen

Verzoek: `GET /reviews/book/{bookId}`

Dit endpoint retourneert de reviews die bij het opgegeven boek horen.

### 5.2 Gemiddelde score ophalen

Verzoek: `GET /reviews/book/{bookId}/average`

Dit endpoint retourneert de gemiddelde score van de reviews voor het opgegeven boek.

### 5.3 Review toevoegen

Verzoek: `POST /reviews`

Voorbeeld van de JSON-invoer:

```json
{
  "userId": 2,
  "bookId": 2,
  "score": 5,
  "text": "Een heel goed boek!"
}
```

De `userId` verwijst naar de gebruiker en de `bookId` naar het beoordeelde boek.

De service controleert onder andere:
- of de score tussen 1 en 5 ligt;
- of de gebruiker het boek heeft betaald;
- of de gebruiker niet al een review voor hetzelfde boek heeft toegevoegd.

Bij een geldige review retourneert de API de opgeslagen reviewgegevens.

Afhankelijk van de fout kan de API de volgende statussen retourneren:

- `400 Bad Request`: de score is ongeldig;
- `403 Forbidden`: de gebruiker voldoet niet aan de aankoopvoorwaarde;
- `409 Conflict`: er bestaat al een review voor dezelfde gebruiker en hetzelfde boek.

### 5.4 Review verwijderen

Verzoek: `DELETE /reviews/{id}`

Dit endpoint verwijdert een review op basis van het review-ID. Bij succes retourneert de controller `200 OK`.

## 6. Belangrijke HTTP-foutmeldingen

| HTTP-status | Betekenis |
|---|---|
| 400 Bad Request | De invoer is ongeldig, bijvoorbeeld een score buiten 1–5 of een ongeldige afhaaldatum |
| 401 Unauthorized | De login is mislukt |
| 403 Forbidden | De gebruiker voldoet niet aan de aankoopvoorwaarde voor een review |
| 404 Not Found | Een gevraagd boek, gebruiker of reservering bestaat niet |
| 409 Conflict | Een actie is niet toegestaan, bijvoorbeeld wegens onvoldoende voorraad, een dubbele review of een ongeldige statusovergang |

De exacte foutstatus hangt af van het endpoint en de situatie. Niet iedere foutstatus geldt voor ieder endpoint.

## 7. Beveiliging

De huidige `SecurityConfig` laat anonieme toegang toe tot de endpoints voor boeken, gebruikers, reserveringen en reviews.

Dit betekent dat Spring Security voor deze routes geen login vereist. De loginmethode controleert wel de inloggegevens, maar de getoonde controller maakt op zichzelf geen beveiligde sessie of token aan.

Controleer of deze configuratie overeenkomt met de beveiligingseisen van de opdracht. Vooral profielgegevens, gebruikersgegevens en reserveringen moeten worden beoordeeld op toegangscontrole en eigenaarschap.

## 8. Opmerkingen

- De endpoints en successtatussen zijn beschreven op basis van de bekeken controllers.
- De foutmeldingen zijn gebaseerd op de bekeken controller- en servicelogica.
- De voorbeelden gebruiken testgegevens. Gebruik geen echte wachtwoorden in documentatie.
- De registratie- en reviewvoorbeelden vereisen dat de opgegeven gegevens voldoen aan de validatieregels van de applicatie.
- De API-documentatie moet worden bijgewerkt als endpoints of validatieregels veranderen.
