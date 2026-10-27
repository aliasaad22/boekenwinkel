# API-documentatie — Boekenwinkel

## 1. Inleiding

De Boekenwinkel-backend is ontwikkeld met Java en Spring Boot. De API gebruikt HTTP-verzoeken om boeken, gebruikers, reserveringen en reviews te beheren.

**Basis-URL**

`http://localhost:8080`

De API gebruikt JSON voor de uitwisseling van gegevens.

## 2. Boeken

Basisendpoint: `/books`

| Methode | Endpoint | Beschrijving | Successtatus |
|---|---|---|---|
| GET | `/books` | Alle boeken ophalen | 200 OK |
| GET | `/books/{id}` | Een boek ophalen via ID | 200 OK |
| POST | `/books` | Een boek toevoegen | 201 Created |
| PUT | `/books/{id}` | Een boek aanpassen | 200 OK |
| DELETE | `/books/{id}` | Een boek verwijderen | 204 No Content |

Bij `GET /books/{id}`, `PUT /books/{id}` en `DELETE /books/{id}` kan `404 Not Found` optreden als het boek niet bestaat.

Bij het toevoegen van een boek retourneert de controller `201 Created`. Bij het succesvol verwijderen van een boek retourneert de controller `204 No Content`.

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

Bij een succesvolle registratie retourneert de API `201 Created`. De response bevat het gebruikers-ID, de naam en het e-mailadres. Het wachtwoord wordt niet teruggegeven.

De service slaat het wachtwoord gehasht op met BCrypt.


Als het e-mailadres al bestaat, retourneert de service een `ResponseStatusException` met HTTP-status `409 Conflict`. De foutmelding is: "Dit e-mailadres is al in gebruik." De bestaande gebruiker wordt niet opnieuw opgeslagen.

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

De service vergelijkt het ingevoerde wachtwoord met het opgeslagen BCrypt-wachtwoord. De loginmethode maakt op zichzelf geen beveiligde sessie of token aan.

### 3.3 Gebruiker zoeken

Een gebruiker kan worden opgezocht via het e-mailadres:

`GET /users?email=test@example.com`

Een gebruiker kan ook worden opgezocht via het ID:

`GET /users/{id}`

Bij succes retourneert de API het gebruikers-ID, de naam en het e-mailadres. Als de gebruiker niet bestaat, retourneert de controller `404 Not Found`.

### 3.4 Gebruikersprofiel ophalen

Verzoek: `GET /users/{id}/profile`

Dit endpoint retourneert de profielgegevens van de gebruiker en diens reserveringen.

Als de gebruiker niet bestaat, retourneert de service `404 Not Found`.

### 3.5 Aangekochte boeken ophalen

Verzoek: `GET /users/{id}/purchased-books`

Dit endpoint retourneert de boeken die de gebruiker heeft aangekocht. Alleen reserveringen met betaalstatus `PAID` worden meegenomen.

Als de gebruiker bestaat maar geen betaalde boeken heeft, retourneert de API een lege lijst met status `200 OK`.

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

De service controleert:

- of de afhaaldatum het formaat `JJJJ-MM-DD` heeft;
- of de afhaaldatum niet in het verleden ligt;
- of het boek bestaat;
- of de gebruiker bestaat;
- of het boek op voorraad is.

Mogelijke foutstatussen:

- `400 Bad Request`: de datum is ongeldig of ligt in het verleden.
- `404 Not Found`: het boek of de gebruiker bestaat niet.
- `409 Conflict`: het boek is niet op voorraad.

### 4.2 Reservering bevestigen

Verzoek: `PUT /reservations/{id}/confirm`

Bij een geldige bevestiging wordt de reservering bevestigd en wordt de voorraad van het boek met één verminderd.

De reservering moet de status `PENDING` hebben en het boek moet op voorraad zijn.

Mogelijke foutstatussen:

- `404 Not Found`: de reservering of het boek bestaat niet.
- `409 Conflict`: de reservering is al verwerkt of het boek is niet op voorraad.

### 4.3 Betaling markeren

Verzoek: `PUT /reservations/{id}/pay`

Dit endpoint markeert een bevestigde reservering als betaald. Het is een simulatie van betaling en voert geen echte online betaling uit.

De reservering moet de status `CONFIRMED` hebben. Een reservering kan niet opnieuw als betaald worden gemarkeerd wanneer de betaalstatus al `PAID` is.

Mogelijke foutstatussen:

- `404 Not Found`: de reservering bestaat niet.
- `409 Conflict`: de reservering is niet bevestigd of is al betaald.

### 4.4 Reserveringen ophalen

Alle reserveringen kunnen worden opgehaald via:

`GET /reservations`

De reserveringen van een gebruiker kunnen worden opgehaald via:

`GET /reservations/user/{userId}`

Bij succes retourneert de API een lijst met reserveringen. Als er geen reserveringen zijn, kan de response een lege lijst bevatten.

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

Dit endpoint retourneert de reviews die bij het opgegeven boek horen. Als er geen reviews zijn, retourneert de service een lege lijst.

### 5.2 Gemiddelde score ophalen

Verzoek: `GET /reviews/book/{bookId}/average`

Dit endpoint retourneert de gemiddelde score van de reviews voor het opgegeven boek.

Als er geen reviews zijn, retourneert de service `0.0`.

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

De service controleert:

- of de score tussen 1 en 5 ligt;
- of de gebruiker bestaat;
- of het boek bestaat;
- of de gebruiker het boek heeft betaald;
- of de gebruiker nog geen review voor hetzelfde boek heeft geplaatst.

Mogelijke foutstatussen:

- `400 Bad Request`: de score ligt niet tussen 1 en 5.
- `403 Forbidden`: de gebruiker heeft het boek niet betaald.
- `404 Not Found`: de gebruiker of het boek bestaat niet.
- `409 Conflict`: de gebruiker heeft al een review voor dit boek geplaatst.

Bij succes retourneert de API de opgeslagen reviewgegevens met status `200 OK`.

### 5.4 Review verwijderen

Verzoek: `DELETE /reviews/{id}`

Dit endpoint verwijdert een review op basis van het review-ID.

Bij succes retourneert de huidige controller `200 OK`. Als de review niet bestaat, retourneert de service `404 Not Found`.

## 6. Overzicht van HTTP-foutstatussen

| HTTP-status | Betekenis |
|---|---|
| 400 Bad Request | De invoer is ongeldig, bijvoorbeeld een ongeldige score of afhaaldatum. |
| 401 Unauthorized | De login is mislukt. |
| 403 Forbidden | De gebruiker voldoet niet aan de aankoopvoorwaarde voor een review. |
| 404 Not Found | Een gevraagd boek, gebruiker, reservering of review bestaat niet. |
| 409 Conflict | De actie is niet toegestaan, bijvoorbeeld door onvoldoende voorraad, een dubbele review of een ongeldige statusovergang. |

Niet iedere foutstatus geldt voor ieder endpoint. De foutstatus hangt af van de uitgevoerde actie en de situatie.

## 7. Beveiliging

De huidige `SecurityConfig` laat anonieme toegang toe tot de endpoints voor boeken, gebruikers, reserveringen en reviews.

Dit betekent dat Spring Security voor deze routes geen login vereist. De loginmethode controleert wel de inloggegevens, maar de getoonde controller maakt op zichzelf geen beveiligde sessie of token aan.

De API-documentatie beschrijft hiermee het huidige gedrag van de applicatie. Controleer of dit overeenkomt met de beveiligingseisen van de opdracht. Vooral gebruikersprofielen, gebruikersgegevens en reserveringen verdienen aandacht wat betreft toegangscontrole en eigenaarschap.

## 8. Opmerkingen

- De endpoints en successtatussen zijn gebaseerd op de bekeken controllers.
- De beschreven foutstatussen zijn gebaseerd op de bekeken controller- en servicelogica.
- De voorbeelden gebruiken testgegevens. Gebruik geen echte wachtwoorden in documentatie.
- Voorbeelden met gebruikers-, boek- en reserverings-ID's werken alleen als de bijbehorende gegevens bestaan.
- De registratie- en review-endpoints kunnen aanvullende foutafhandeling hebben die niet in de controller zelf staat.
- Werk deze documentatie bij wanneer endpoints, validatieregels of beveiligingsinstellingen veranderen.
