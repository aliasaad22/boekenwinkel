# Testdocumentatie

## 1. Doel

Deze documentatie beschrijft de uitgevoerde tests voor de backend van de boekenwinkel. De tests controleren of de belangrijkste onderdelen correct werken en hoe de applicatie reageert op ongeldige gegevens en uitzonderlijke situaties.

## 2. Testresultaat

De bestaande tests zijn uitgevoerd in IntelliJ IDEA.

- **Totaal aantal geslaagde tests:** 31
- **Totaal aantal mislukte tests:** 0
- **Resultaat:** alle 31 tests zijn geslaagd.

## 3. Overzicht van de testklassen

| Testklasse | Doel |
|---|---|
| `BoekenwinkelApplicationTests` | Controleren of de applicatiecontext correct kan worden geladen. |
| `BookServiceTests` | De werking van de boekfunctionaliteit testen. |
| `ReservationServiceTests` | De werking van reserveringen, bevestigingen en betalingen testen. |
| `ReviewServiceTests` | De werking van beoordelingen en de bijbehorende controles testen. |

## 4. Geteste situaties

### 4.1 Boeken

De boekentests controleren onder andere:

- Alle boeken ophalen.
- Een boek opzoeken met een bestaand ID.
- Een boek opzoeken dat niet bestaat.
- Een boek opslaan en bijwerken.
- Een bestaand boek verwijderen.
- Omgaan met een boek dat niet bestaat bij het bijwerken of verwijderen.

### 4.2 Reserveringen

De reserveringstests controleren onder andere:

- Reserveringen bevestigen en de voorraad controleren.
- Omgaan met onvoldoende voorraad.
- Een onbekende reservering of een onbekend boek verwerken.
- Betalingen uitvoeren en herhaalde betalingen controleren.
- Controleren of betalen mogelijk is vóór of na bevestiging.
- Ongeldige reserveringsstatussen verwerken.

### 4.3 Beoordelingen

De beoordelingstests controleren onder andere:

- Beoordelingen van een boek ophalen.
- Het gemiddelde van beoordelingen berekenen.
- Het gemiddelde berekenen wanneer er geen beoordelingen zijn.
- Een geldige beoordeling opslaan.
- Ongeldige scores en onbekende gebruikers of boeken verwerken.
- Controleren of een gebruiker het boek heeft betaald.
- Dubbele beoordelingen voorkomen.
- Een beoordeling verwijderen.

## 5. Conclusie

Tijdens de uitgevoerde testreeks zijn alle 31 tests geslaagd. Er waren geen mislukte tests. De bestaande tests controleren belangrijke onderdelen van de boek-, reserverings- en beoordelingsfunctionaliteit.

Er bestaat momenteel geen afzonderlijke testklasse voor `BookUserService`. De registratie-, login- en profielfunctionaliteit wordt daarom in deze documentatie niet als afzonderlijk getest beschouwd.
