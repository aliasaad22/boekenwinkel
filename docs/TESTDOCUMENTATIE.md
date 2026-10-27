# Testdocumentatie

## 1. Doel

Deze documentatie beschrijft de uitgevoerde tests voor de backend van de boekenwinkel. De tests controleren de belangrijkste functionaliteiten, waaronder boekenbeheer, reserveringen, beoordelingen en gebruikersbeheer. Daarnaast wordt gecontroleerd hoe de applicatie reageert op ongeldige gegevens en uitzonderlijke situaties.

## 2. Testresultaat

De volledige testsuite is uitgevoerd met Maven via de Maven Wrapper.

- **Totaal aantal uitgevoerde tests:** 38
- **Aantal geslaagde tests:** 38
- **Aantal mislukte tests:** 0
- **Aantal fouten:** 0
- **Aantal overgeslagen tests:** 0
- **Resultaat:** alle 38 tests zijn geslaagd.

De testuitvoering is succesvol afgerond met `BUILD SUCCESS`.

## 3. Overzicht van de testklassen

| Testklasse | Doel |
|---|---|
| `BoekenwinkelApplicationTests` | Controleren of de applicatiecontext correct kan worden geladen. |
| `BookServiceTests` | De werking van de boekfunctionaliteit testen. |
| `ReservationServiceTests` | De werking van reserveringen, bevestigingen en betalingen testen. |
| `ReviewServiceTests` | De werking van beoordelingen en de bijbehorende controles testen. |
| `BookUserServiceTests` | De registratie-, login- en relevante profielfunctionaliteit testen. |

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

### 4.4 Gebruikersbeheer

De tests van `BookUserServiceTests` controleren onder andere:

- Inloggen met een onbekend e-mailadres.
- Inloggen met een verkeerd wachtwoord.
- Succesvol inloggen met correcte gegevens.
- Omgaan met een gebruiker die niet bestaat bij profiel- en boekopvragingen.
- Een gebruiker registreren en controleren of het wachtwoord geëncodeerd wordt opgeslagen.
- Een registratie met een bestaand e-mailadres weigeren.

## 5. Conclusie

De volledige backend-testsuite is succesvol uitgevoerd. Alle 38 tests zijn geslaagd, zonder fouten, mislukte tests of overgeslagen tests.

De tests controleren belangrijke onderdelen van de boek-, reserverings-, beoordelings- en gebruikersfunctionaliteit. Ze helpen om fouten vroegtijdig op te sporen en geven meer vertrouwen dat de geteste onderdelen correct werken.

De testresultaten bevestigen dat alle uitgevoerde tests slagen. Ze vormen geen garantie dat iedere mogelijke situatie of de volledige applicatie foutloos werkt.
