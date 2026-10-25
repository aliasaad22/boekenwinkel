# API-documentatie — Boekenwinkel

## 1. Inleiding

De Boekenwinkel-backend is gebouwd met Java en Spring Boot. De API gebruikt HTTP-verzoeken om boeken, gebruikers, reserveringen en reviews te beheren.

De standaardbasis-URL is:

`http://localhost:8080`

## 2. Boeken

Basisendpoint: `/books`

| Methode | Endpoint | Beschrijving | Successtatus |
|---|---|---|---|
| GET | `/books` | Alle boeken ophalen | 200 OK |
| GET | `/books/{id}` | Een boek ophalen via ID | 200 OK |
| POST | `/books` | Een boek toevoegen | 201 Created |
| PUT | `/books/{id}` | Een boek aanpassen | 200 OK |
| DELETE | `/books/{id}` | Een boek verwijderen | 204 No Content |

Bij een onbekend boek kunnen de GET-, PUT- en DELETE-verzoeken met een `404 Not Found` eindigen.

## 3. Gebruikers

Basisendpoint: `/users`

| Methode | Endpoint | Beschrijving | Successtatus |
|---|---|---|---|
| POST | `/users` | Een gebruiker registreren | 201 Created |
| GET | `/users?email={email}` | Een gebruiker zoeken via e-mailadres | 200 OK |
| GET | `/users/{id}` | Een gebruiker ophalen via ID | 200 OK |
| POST | `/users/login` | Inloggen | 200 OK |
| GET | `/users/{id}/profile` | Een gebruikersprofiel ophalen | 200 OK bij succes |
| GET | `/users/{id}/purchased-books` | Aangekochte boeken ophalen | 200 OK bij succes |

Een gebruiker die niet gevonden wordt, kan een `404 Not Found` opleveren. Een login met onjuiste gegevens geeft `401 Unauthorized`.

Bij registratie en login geeft de controller een `BookUserResponse` terug met het gebruikers-ID, de naam en het e-mailadres.

## 4. Reserveringen

Basisendpoint: `/reservations`

| Methode | Endpoint | Beschrijving |
|---|---|---|
| POST | `/reservations` | Een reservering aanmaken |
| PUT | `/reservations/{id}/confirm` | Een reservering bevestigen |
| GET | `/reservations` | Alle reserveringen ophalen |
| PUT | `/reservations/{id}/pay` | Een reservering als betaald markeren |
| GET | `/reservations/user/{userId}` | Reserveringen van een gebruiker ophalen |

### Reservering aanmaken

Verzoek: `POST /reservations`

Voorbeeld van de JSON-invoer:

```json
{
  "userId": 2,
  "bookId": 2,
  "pickupDate": "2026-10-30"
}
```

De invoer bevat het gebruikers-ID, het boek-ID en de gewenste afhaaldatum. De afhaaldatum wordt in de huidige code als tekst (`String`) verwerkt.

De service controleert of het boek bestaat en of er voorraad beschikbaar is.

### Reservering bevestigen

Verzoek: `PUT /reservations/{id}/confirm`

Bij een geldige bevestiging wordt de reservering bevestigd en wordt de voorraad van het boek met één verminderd.

### Betaling markeren

Verzoek: `PUT /reservations/{id}/pay`

Dit endpoint markeert een bevestigde reservering als betaald. Het betreft een simulatie van betaling.

## 5. Reviews

Basisendpoint: `/reviews`

| Methode | Endpoint | Beschrijving | Successtatus |
|---|---|---|---|
| GET | `/reviews/book/{bookId}` | Reviews van een boek ophalen | 200 OK |
| POST | `/reviews` | Een review toevoegen | 200 OK |
| GET | `/reviews/book/{bookId}/average` | Gemiddelde score ophalen | 200 OK |
| DELETE | `/reviews/{id}` | Een review verwijderen | 200 OK |

### Review toevoegen

Verzoek: `POST /reviews`

De controller verwacht een `ReviewRequest` met de gegevens die nodig zijn om een review te maken: gebruikers-ID, boek-ID, score en tekst.

De service controleert onder andere of de score geldig is, of de gebruiker het boek heeft betaald en of er al een review bestaat.

## 6. Belangrijke foutmeldingen

| HTTP-status | Betekenis in de applicatie |
|---|---|
| 400 Bad Request | Ongeldige invoer, bijvoorbeeld een score buiten 1–5 |
| 401 Unauthorized | Login mislukt |
| 403 Forbidden | Een review is niet toegestaan omdat niet aan de aankoopvoorwaarde is voldaan |
| 404 Not Found | Een gevraagd boek, gebruiker of reservering bestaat niet |
| 409 Conflict | Een actie is niet toegestaan, bijvoorbeeld wegens onvoldoende voorraad of een dubbele review |

De exacte foutstatus hangt af van het endpoint en de situatie. Niet iedere foutstatus geldt voor ieder endpoint.

## 7. Opmerkingen

- De endpoints en successtatussen zijn beschreven op basis van de controllers.
- De genoemde foutmeldingen zijn gebaseerd op de momenteel bekeken controller- en servicelogica.
- De API-documentatie moet worden bijgewerkt als endpoints of validatieregels veranderen.
