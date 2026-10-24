
const boekenContainer = document.querySelector("#boeken");
const boekDetails = document.querySelector("#boek-details");
const detailsInhoud = document.querySelector("#details-inhoud");
const terugKnop = document.querySelector("#terug-knop");

// Reviews
const reviewFormulier = document.querySelector("#review-formulier");
const reviewScore = document.querySelector("#review-score");
const reviewTekst = document.querySelector("#review-tekst");
const reviewBericht = document.querySelector("#review-bericht");

// Reserveringen
const reserveringSectie = document.querySelector("#reservering-sectie");
const ophaaldatum = document.querySelector("#ophaaldatum");
const reserveringBericht = document.querySelector("#reservering-bericht");
const reserverenKnop = document.querySelector("#reserveren-knop");

const mijnReserveringenSectie =
    document.querySelector("#mijn-reserveringen-sectie");

const reserveringenLadenKnop =
    document.querySelector("#reserveringen-laden-knop");

const mijnReserveringen =
    document.querySelector("#mijn-reserveringen");

const mijnReserveringenBericht =
    document.querySelector("#mijn-reserveringen-bericht");

let huidigBoekId = null;

// Registratie
const registratieFormulier =
    document.querySelector("#registratie-formulier");

const registratieBericht =
    document.querySelector("#registratie-bericht");

// Login
const loginFormulier =
    document.querySelector("#login-formulier");

const loginBericht =
    document.querySelector("#login-bericht");

// Gebruikersprofiel
const gebruikerStatus = document.querySelector("#gebruiker-status");
const uitlogKnop = document.querySelector("#uitlog-knop");
const profielKnop = document.querySelector("#profiel-knop");
const mijnProfiel = document.querySelector("#mijn-profiel");
const profielInhoud = document.querySelector("#profiel-inhoud");
const gekochteBoeken = document.querySelector("#gekochte-boeken");
const profielTerugKnop = document.querySelector("#profiel-terug-knop");

// Reviews plaatsen
reviewFormulier.addEventListener("submit", async function (event) {
    event.preventDefault();

    const opgeslagenGebruiker =
        sessionStorage.getItem("ingelogdeGebruiker");

    if (!opgeslagenGebruiker) {
        reviewBericht.textContent =
            "Je moet ingelogd zijn om een review te plaatsen.";
        return;
    }

    const gebruiker = JSON.parse(opgeslagenGebruiker);

    const nieuweReview = {
        bookId: huidigBoekId,
        userId: gebruiker.id,
        score: Number(reviewScore.value),
        text: reviewTekst.value.trim()
    };

    try {
        const response = await fetch("http://localhost:8080/reviews", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(nieuweReview)
        });

        if (!response.ok) {
            const foutmelding = await response.text();

            reviewBericht.textContent =
                "Fout " + response.status + ": " + foutmelding;

            console.error("Fout bij review plaatsen:", foutmelding);
            return;
        }

        reviewBericht.textContent =
            "Je review is succesvol geplaatst!";

        reviewFormulier.reset();

    } catch (error) {
        reviewBericht.textContent =
            "Er is een verbindingsfout met de server.";

        console.error("Fout bij review plaatsen:", error);
    }
});

// Profiel en gekochte boeken ophalen
profielKnop.addEventListener("click", async function () {
    const opgeslagenGebruiker =
        sessionStorage.getItem("ingelogdeGebruiker");

    if (!opgeslagenGebruiker) {
        return;
    }

    const gebruiker = JSON.parse(opgeslagenGebruiker);

    try {
        const profielResponse = await fetch(
            "http://localhost:8080/users/" +
            gebruiker.id + "/profile"
        );

        if (!profielResponse.ok) {
            throw new Error("Profiel ophalen is mislukt.");
        }

        const profiel = await profielResponse.json();

        profielInhoud.innerHTML = "";

        const naam = document.createElement("p");
        naam.textContent = "Naam: " + profiel.name;

        const email = document.createElement("p");
        email.textContent = "E-mailadres: " + profiel.email;

        profielInhoud.appendChild(naam);
        profielInhoud.appendChild(email);

        const boekenResponse = await fetch(
            "http://localhost:8080/users/" +
            gebruiker.id + "/purchased-books"
        );

        if (!boekenResponse.ok) {
            throw new Error("Gekochte boeken ophalen is mislukt.");
        }

        const boeken = await boekenResponse.json();

        gekochteBoeken.innerHTML = "";

        if (boeken.length === 0) {
            gekochteBoeken.textContent =
                "Je hebt nog geen gekochte boeken.";
        } else {
            boeken.forEach(function (boek) {
                const boekKaart = document.createElement("article");

                const titel = document.createElement("h4");
                titel.textContent = boek.title;

                const auteurs = document.createElement("p");
                auteurs.textContent = "Auteur: " + boek.authors;

                const prijs = document.createElement("p");
                prijs.textContent = "Prijs: €" + boek.price;

                boekKaart.appendChild(titel);
                boekKaart.appendChild(auteurs);
                boekKaart.appendChild(prijs);

                gekochteBoeken.appendChild(boekKaart);
            });
        }

        boekenContainer.hidden = true;
        boekDetails.hidden = true;
        mijnProfiel.hidden = false;

    } catch (error) {
        console.error("Fout bij het ophalen van het profiel:", error);
        alert("Je profiel of gekochte boeken konden niet worden geladen.");
    }
});

// Gemiddelde reviewscore ophalen
async function laadGemiddeldeScore(boekId) {
    const response = await fetch(
        "http://localhost:8080/reviews/book/" +
        boekId + "/average"
    );

    if (!response.ok) {
        throw new Error("Gemiddelde reviewscore ophalen is mislukt.");
    }

    return await response.json();
}

// Boeken ophalen en weergeven
async function laadBoeken() {
    try {
        const response = await fetch("http://localhost:8080/books");

        if (!response.ok) {
            throw new Error("Boeken ophalen is mislukt.");
        }

        const boeken = await response.json();

        boekenContainer.innerHTML = "";

        boeken.forEach(function (boek) {
            const boekKaart = document.createElement("article");
            boekKaart.classList.add("boek-kaart");

            const titel = document.createElement("h3");
            titel.textContent = boek.title;

            const auteurs = document.createElement("p");
            auteurs.textContent = "Auteur: " + boek.authors;

            const beschrijving = document.createElement("p");
            beschrijving.textContent = boek.description;

            const prijs = document.createElement("p");
            prijs.textContent = "Prijs: €" + boek.price;

            const voorraad = document.createElement("p");
            voorraad.textContent = "Voorraad: " + boek.stock;

            const detailsKnop = document.createElement("button");
            detailsKnop.textContent = "Bekijk details";
            detailsKnop.classList.add("details-knop");

            // Boekdetails openen
            detailsKnop.addEventListener("click", async function () {
                boekenContainer.hidden = true;
                boekDetails.hidden = false;
                detailsInhoud.innerHTML = "";

                huidigBoekId = boek.id;

                const opgeslagenGebruiker =
                    sessionStorage.getItem("ingelogdeGebruiker");

                reviewFormulier.hidden = !opgeslagenGebruiker;
                reserveringSectie.hidden = !opgeslagenGebruiker;
                mijnReserveringenSectie.hidden = !opgeslagenGebruiker;

                reserveringBericht.textContent = "";
                reviewBericht.textContent = "";
                mijnReserveringenBericht.textContent = "";
                mijnReserveringen.innerHTML = "";

                const titelDetail = document.createElement("h3");
                titelDetail.textContent = boek.title;

                const auteursDetail = document.createElement("p");
                auteursDetail.textContent = "Auteur: " + boek.authors;

                const beschrijvingDetail = document.createElement("p");
                beschrijvingDetail.textContent = boek.description;

                const prijsDetail = document.createElement("p");
                prijsDetail.textContent = "Prijs: €" + boek.price;

                const voorraadDetail = document.createElement("p");
                voorraadDetail.textContent = "Voorraad: " + boek.stock;

                const isbnDetail = document.createElement("p");
                isbnDetail.textContent = "ISBN: " + boek.isbn;

                const scoreDetail = document.createElement("p");
                scoreDetail.textContent = "Gemiddelde score: laden...";

                const reviewsTitel = document.createElement("h4");
                reviewsTitel.textContent = "Reviews";

                const reviewsContainer = document.createElement("div");
                reviewsContainer.textContent = "Reviews laden...";

                detailsInhoud.appendChild(titelDetail);
                detailsInhoud.appendChild(auteursDetail);
                detailsInhoud.appendChild(beschrijvingDetail);
                detailsInhoud.appendChild(prijsDetail);
                detailsInhoud.appendChild(voorraadDetail);
                detailsInhoud.appendChild(isbnDetail);
                detailsInhoud.appendChild(scoreDetail);
                detailsInhoud.appendChild(reviewsTitel);
                detailsInhoud.appendChild(reviewsContainer);

                try {
                    const score = await laadGemiddeldeScore(boek.id);

                    scoreDetail.textContent =
                        "Gemiddelde score: " + score;

                    const reviewsResponse = await fetch(
                        "http://localhost:8080/reviews/book/" + boek.id
                    );

                    if (!reviewsResponse.ok) {
                        throw new Error("Reviews ophalen is mislukt.");
                    }

                    const reviews = await reviewsResponse.json();

                    reviewsContainer.innerHTML = "";

                    if (reviews.length === 0) {
                        reviewsContainer.textContent =
                            "Er zijn nog geen reviews.";
                    } else {
                        reviews.forEach(function (review) {
                            const reviewKaart =
                                document.createElement("article");

                            reviewKaart.classList.add("review-kaart");

                            const reviewScoreElement =
                                document.createElement("p");

                            reviewScoreElement.classList.add("review-score");
                            reviewScoreElement.textContent =
                                "Score: " + review.score + "/5";

                            const reviewTekstElement =
                                document.createElement("p");

                            reviewTekstElement.textContent = review.text;

                            reviewKaart.appendChild(reviewScoreElement);
                            reviewKaart.appendChild(reviewTekstElement);

                            reviewsContainer.appendChild(reviewKaart);
                        });
                    }

                } catch (error) {
                    console.error("Fout bij het ophalen van reviews:", error);

                    scoreDetail.textContent =
                        "Gemiddelde score niet beschikbaar.";

                    reviewsContainer.textContent =
                        "Reviews konden niet worden geladen.";
                }
            });

            boekKaart.appendChild(titel);
            boekKaart.appendChild(auteurs);
            boekKaart.appendChild(beschrijving);
            boekKaart.appendChild(prijs);
            boekKaart.appendChild(voorraad);
            boekKaart.appendChild(detailsKnop);

            boekenContainer.appendChild(boekKaart);
        });

    } catch (error) {
        console.error("Fout bij het ophalen van boeken:", error);

        boekenContainer.textContent =
            "De boeken konden niet worden geladen.";
    }
}

// Terug naar de boekenlijst
terugKnop.addEventListener("click", function () {
    boekDetails.hidden = true;
    boekenContainer.hidden = false;
});

// Terug naar de boekenlijst vanuit het profiel
profielTerugKnop.addEventListener("click", function () {
    mijnProfiel.hidden = true;
    boekenContainer.hidden = false;
});

// Account registreren
registratieBericht.textContent = "";

registratieFormulier.addEventListener("submit", async function (event) {
    event.preventDefault();

    const gebruiker = {
        name: document.querySelector("#registratie-naam").value,
        email: document.querySelector("#registratie-email").value,
        password: document.querySelector("#registratie-wachtwoord").value
    };

    try {
        const response = await fetch("http://localhost:8080/users", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(gebruiker)
        });

        if (!response.ok) {
            throw new Error(
                "Registratie is mislukt. Controleer de gegevens."
            );
        }

        registratieBericht.textContent = "Je account is aangemaakt!";
        registratieFormulier.reset();

    } catch (error) {
        console.error("Fout bij registratie:", error);
        registratieBericht.textContent = error.message;
    }
});

// Inloggen
loginBericht.textContent = "";

loginFormulier.addEventListener("submit", async function (event) {
    event.preventDefault();

    const gebruiker = {
        email: document.querySelector("#login-email").value,
        password: document.querySelector("#login-wachtwoord").value
    };

    try {
        const response = await fetch("http://localhost:8080/users/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(gebruiker)
        });

        if (!response.ok) {
            throw new Error(
                "Inloggen mislukt. Controleer je e-mailadres en wachtwoord."
            );
        }

        const ingelogdeGebruiker = await response.json();

        sessionStorage.setItem(
            "ingelogdeGebruiker",
            JSON.stringify(ingelogdeGebruiker)
        );

        loginBericht.textContent =
            "Je bent succesvol ingelogd als " +
            ingelogdeGebruiker.name + "!";

        toonIngelogdeGebruiker();
        loginFormulier.reset();

    } catch (error) {
        console.error("Fout bij inloggen:", error);
        loginBericht.textContent = error.message;
    }
});

// Ingelogde gebruiker weergeven
function toonIngelogdeGebruiker() {
    const opgeslagenGebruiker =
        sessionStorage.getItem("ingelogdeGebruiker");

    if (opgeslagenGebruiker) {
        const gebruiker = JSON.parse(opgeslagenGebruiker);

        gebruikerStatus.textContent =
            "Ingelogd als: " + gebruiker.name;

        gebruikerStatus.hidden = false;
        profielKnop.hidden = false;
        uitlogKnop.hidden = false;

    } else {
        gebruikerStatus.hidden = true;
        profielKnop.hidden = true;
        uitlogKnop.hidden = true;
    }
}

// Uitloggen
uitlogKnop.addEventListener("click", function () {
    sessionStorage.removeItem("ingelogdeGebruiker");

    gebruikerStatus.hidden = true;
    profielKnop.hidden = true;
    uitlogKnop.hidden = true;

    loginBericht.textContent = "Je bent uitgelogd!";

    reviewFormulier.hidden = true;
    reserveringSectie.hidden = true;
    mijnReserveringenSectie.hidden = true;
});

// Boek reserveren
reserverenKnop.addEventListener("click", async function () {
    const opgeslagenGebruiker =
        sessionStorage.getItem("ingelogdeGebruiker");

    if (!opgeslagenGebruiker) {
        reserveringBericht.textContent =
            "Je moet ingelogd zijn om een boek te reserveren.";
        return;
    }

    if (!huidigBoekId) {
        reserveringBericht.textContent = "Selecteer eerst een boek.";
        return;
    }

    if (!ophaaldatum.value) {
        reserveringBericht.textContent = "Kies eerst een ophaaldatum.";
        return;
    }

    const gebruiker = JSON.parse(opgeslagenGebruiker);

    const nieuweReservering = {
        userId: gebruiker.id,
        bookId: huidigBoekId,
        pickupDate: ophaaldatum.value
    };

    try {
        const response = await fetch(
            "http://localhost:8080/reservations",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(nieuweReservering)
            }
        );

        if (!response.ok) {
            const foutmelding = await response.text();

            reserveringBericht.textContent =
                "Reserveren mislukt (" +
                response.status + "): " + foutmelding;

            console.error("Fout bij reserveren:", foutmelding);
            return;
        }

        reserveringBericht.textContent =
            "Je reservering is aangemaakt!";

        ophaaldatum.value = "";

    } catch (error) {
        reserveringBericht.textContent =
            "Er is een verbindingsfout met de server.";

        console.error("Fout bij reserveren:", error);
    }
});

// Mijn reserveringen ophalen en weergeven
reserveringenLadenKnop.addEventListener("click", async function () {
    const opgeslagenGebruiker =
        sessionStorage.getItem("ingelogdeGebruiker");

    if (!opgeslagenGebruiker) {
        mijnReserveringenBericht.textContent =
            "Je moet ingelogd zijn om je reserveringen te bekijken.";
        return;
    }

    const gebruiker = JSON.parse(opgeslagenGebruiker);

    try {
        const response = await fetch(
            "http://localhost:8080/reservations/user/" + gebruiker.id
        );

        if (!response.ok) {
            mijnReserveringenBericht.textContent =
                "De reserveringen konden niet worden opgehaald.";
            return;
        }

        const reserveringen = await response.json();

        mijnReserveringen.innerHTML = "";
        mijnReserveringenBericht.textContent = "";

        if (reserveringen.length === 0) {
            mijnReserveringenBericht.textContent =
                "Je hebt nog geen reserveringen.";
            return;
        }


        for (const reservering of reserveringen) {
            const reserveringElement = document.createElement("p");

            reserveringElement.textContent =
                "Reservering #" + reservering.id +
                " | Boek-ID: " + reservering.bookId +
                " | Ophaaldatum: " + reservering.pickupDate +
                " | Status: " + reservering.status +
                " | Betaling: " + reservering.paymentStatus;

            mijnReserveringen.appendChild(reserveringElement);

            try {
                const antwoord = await fetch(
                    "http://localhost:8080/books/" + reservering.bookId
                );

                if (antwoord.ok) {
                    const boek = await antwoord.json();

                    reserveringElement.textContent =
                        "Reservering #" + reservering.id +
                        " | Boek: " + boek.title +
                        " | Ophaaldatum: " + reservering.pickupDate +
                        " | Status: " + reservering.status +
                        " | Betaling: " + reservering.paymentStatus;
                }
            } catch (fout) {
                console.error("Boektitel ophalen mislukt:", fout);
            }
        }


    } catch (error) {
        mijnReserveringenBericht.textContent =
            "Er is een verbindingsfout met de server.";

        console.error("Fout bij ophalen reserveringen:", error);
    }
});

// Applicatie starten
toonIngelogdeGebruiker();
laadBoeken();
