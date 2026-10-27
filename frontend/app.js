
const boekenContainer = document.querySelector("#boeken");
const accountSectie = document.querySelector("#account-sectie");
const boekenSectie = document.querySelector("#boeken-sectie");
const boekDetails = document.querySelector("#boek-details");
const detailsInhoud = document.querySelector("#details-inhoud");
const terugKnop = document.querySelector("#terug-knop");

const loginPaneel = document.querySelector("#login-paneel");
const registratiePaneel = document.querySelector("#registratie-paneel");
const ingelogdePaneel = document.querySelector("#ingelogde-paneel");

const boekenMenuKnop = document.querySelector("#boeken-menu-knop");
const reserveringenMenuKnop = document.querySelector("#reserveringen-menu-knop");
const profielMenuKnop = document.querySelector("#profiel-menu-knop");
const loginMenuKnop = document.querySelector("#login-menu-knop");
const registratieMenuKnop = document.querySelector("#registratie-menu-knop");
const uitlogMenuKnop = document.querySelector("#uitlog-menu-knop");

const reviewFormulier = document.querySelector("#review-formulier");
const reviewScore = document.querySelector("#review-score");
const reviewTekst = document.querySelector("#review-tekst");
const reviewBericht = document.querySelector("#review-bericht");

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

const registratieFormulier =
    document.querySelector("#registratie-formulier");

const registratieBericht =
    document.querySelector("#registratie-bericht");

const loginFormulier =
    document.querySelector("#login-formulier");

const loginBericht =
    document.querySelector("#login-bericht");

const gebruikerStatus = document.querySelector("#gebruiker-status");
const mijnProfiel = document.querySelector("#mijn-profiel");
const profielInhoud = document.querySelector("#profiel-inhoud");
const profielBericht = document.querySelector("#profiel-bericht");
const gekochteBoeken = document.querySelector("#gekochte-boeken");
const profielTerugKnop = document.querySelector("#profiel-terug-knop");

let huidigBoekId = null;

function toonAccountPaneel(paneel) {
    accountSectie.hidden = false;
    boekenSectie.hidden = true;
    mijnProfiel.hidden = true;
    mijnReserveringenSectie.hidden = true;
    boekDetails.hidden = true;

    loginPaneel.hidden = paneel !== "login";
    registratiePaneel.hidden = paneel !== "registratie";
    ingelogdePaneel.hidden = true;
}
loginMenuKnop.addEventListener("click", function () {
    toonAccountPaneel("login");
});


registratieMenuKnop.addEventListener("click", function () {
    toonAccountPaneel("registratie");
});
function toonIngelogdeGebruiker() {
    const opgeslagenGebruiker =
        sessionStorage.getItem("ingelogdeGebruiker");

    const ingelogd = Boolean(opgeslagenGebruiker);

    if (ingelogd) {
        const gebruiker = JSON.parse(opgeslagenGebruiker);

        gebruikerStatus.textContent =
            "Ingelogd als: " + gebruiker.name;
    } else {
        gebruikerStatus.textContent = "";
    }

    loginMenuKnop.hidden = ingelogd;
    registratieMenuKnop.hidden = ingelogd;
    uitlogMenuKnop.hidden = !ingelogd;
    reserveringenMenuKnop.hidden = !ingelogd;
    profielMenuKnop.hidden = !ingelogd;

    loginPaneel.hidden = true;
    registratiePaneel.hidden = true;
    ingelogdePaneel.hidden = !ingelogd;
}

function toonBoekenlijst() {
    accountSectie.hidden = true;
    boekenSectie.hidden = false;
    mijnProfiel.hidden = true;
    mijnReserveringenSectie.hidden = true;
    boekDetails.hidden = true;

    loginPaneel.hidden = true;
    registratiePaneel.hidden = true;

    boekenContainer.hidden = false;
}
boekenMenuKnop.addEventListener("click", function () {
    toonBoekenlijst();
});

function toonAccountStatus() {
    const ingelogd = Boolean(
        sessionStorage.getItem("ingelogdeGebruiker")
    );

    reviewFormulier.hidden = !ingelogd;
    reserveringSectie.hidden = !ingelogd;
    mijnReserveringenSectie.hidden = !ingelogd;
}

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

async function laadReviews(boekId, scoreElement, reviewsContainer) {
    try {
        const score = await laadGemiddeldeScore(boekId);

        scoreElement.textContent = "Gemiddelde score: " + score;

        const response = await fetch(
            "http://localhost:8080/reviews/book/" + boekId
        );

        if (!response.ok) {
            throw new Error("Reviews ophalen is mislukt.");
        }

        const reviews = await response.json();

        reviewsContainer.innerHTML = "";

        if (reviews.length === 0) {
            reviewsContainer.textContent = "Er zijn nog geen reviews.";
            return;
        }

        reviews.forEach(function (review) {
            const reviewKaart = document.createElement("article");
            reviewKaart.classList.add("review-kaart");

            const scoreElement = document.createElement("p");
            scoreElement.classList.add("review-score");
            scoreElement.textContent = "Score: " + review.score + "/5";

            const tekstElement = document.createElement("p");
            tekstElement.textContent = review.text;

            reviewKaart.appendChild(scoreElement);
            reviewKaart.appendChild(tekstElement);
            reviewsContainer.appendChild(reviewKaart);
        });
    } catch (error) {
        console.error("Fout bij het ophalen van reviews:", error);

        scoreElement.textContent =
            "Gemiddelde score niet beschikbaar.";

        reviewsContainer.textContent =
            "Reviews konden niet worden geladen.";
    }
}

async function laadBoeken() {
    try {
        const response = await fetch("http://localhost:8080/books");

        if (!response.ok) {
            throw new Error("Boeken ophalen is mislukt.");
        }

        const boeken = await response.json();

        boekenContainer.innerHTML = "";

        if (boeken.length === 0) {
            boekenContainer.textContent =
                "Er zijn momenteel geen boeken beschikbaar.";
            return;
        }

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

            const beschikbaarheid = document.createElement("p");

            if (boek.stock > 5) {
                beschikbaarheid.textContent = "Beschikbaar";
            } else if (boek.stock > 0) {
                beschikbaarheid.textContent = "Beperkte voorraad";
            } else {
                beschikbaarheid.textContent = "Tijdelijk niet beschikbaar";
            }

            boekKaart.appendChild(beschikbaarheid);

            const detailsKnop = document.createElement("button");
            detailsKnop.type = "button";
            detailsKnop.textContent = "Bekijk details";
            detailsKnop.classList.add("details-knop");

            detailsKnop.addEventListener("click", async function () {
                huidigBoekId = boek.id;

                loginPaneel.hidden = true;
                registratiePaneel.hidden = true;



                accountSectie.hidden = true;
                boekenSectie.hidden = true;
                boekenContainer.hidden = true;
                mijnProfiel.hidden = true;
                mijnReserveringenSectie.hidden = true;
                boekDetails.hidden = false;

                detailsInhoud.innerHTML = "";

                reserveringBericht.textContent = "";
                reviewBericht.textContent = "";
                mijnReserveringenBericht.textContent = "";
                mijnReserveringen.innerHTML = "";

                toonAccountStatus();

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

                await laadReviews(
                    boek.id,
                    scoreDetail,
                    reviewsContainer
                );
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

terugKnop.addEventListener("click", function () {
    toonBoekenlijst();
});

profielTerugKnop.addEventListener("click", function () {
    toonBoekenlijst();
});

registratieFormulier.addEventListener("submit", async function (event) {
    event.preventDefault();

    const gebruiker = {
        name: document.querySelector("#registratie-naam").value.trim(),
        email: document.querySelector("#registratie-email").value.trim(),
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
            const foutmelding = await response.text();

            throw new Error(
                foutmelding || "Registratie is mislukt."
            );
        }

        registratieBericht.textContent =
            "Je account is aangemaakt! Je kunt nu inloggen.";

        registratieFormulier.reset();
        toonAccountPaneel("login");
        loginBericht.textContent =
            "Je account is aangemaakt. Log nu in.";
    } catch (error) {
        console.error("Fout bij registratie:", error);
        registratieBericht.textContent = error.message;
    }
});

loginFormulier.addEventListener("submit", async function (event) {
    event.preventDefault();

    const gebruiker = {
        email: document.querySelector("#login-email").value.trim(),
        password: document.querySelector("#login-wachtwoord").value
    };

    loginBericht.textContent = "";

    try {
        const response = await fetch(
            "http://localhost:8080/users/login",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(gebruiker)
            }
        );

        if (!response.ok) {
            if (response.status === 401) {
                loginBericht.textContent =
                    "E-mailadres of wachtwoord is onjuist. Probeer opnieuw.";
                return;
            }

            loginBericht.textContent =
                "Inloggen is tijdelijk niet mogelijk. Probeer later opnieuw.";
            return;
        }

        const ingelogdeGebruiker = await response.json();

        sessionStorage.setItem(
            "ingelogdeGebruiker",
            JSON.stringify(ingelogdeGebruiker)
        );

        loginFormulier.reset();

        toonIngelogdeGebruiker();
        toonAccountStatus();
        toonBoekenlijst();

        loginBericht.textContent = "Je bent succesvol ingelogd.";
    } catch (error) {
        console.error("Fout bij inloggen:", error);

        loginBericht.textContent =
            "Er is geen verbinding met de server. Probeer later opnieuw.";
    }
});

uitlogMenuKnop.addEventListener("click", function () {
    sessionStorage.removeItem("ingelogdeGebruiker");

    toonIngelogdeGebruiker();
    toonBoekenlijst();
    toonAccountStatus();

    reserveringBericht.textContent = "";
    reviewBericht.textContent = "";
    mijnReserveringenBericht.textContent = "";
    mijnReserveringen.innerHTML = "";

    loginBericht.textContent = "Je bent uitgelogd.";
    registratieBericht.textContent = "";
});

profielMenuKnop.addEventListener("click", async function () {
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
                boekKaart.classList.add("boek-kaart");

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

        accountSectie.hidden = true;
        boekenSectie.hidden = true;
        boekenContainer.hidden = true;
        boekDetails.hidden = true;
        mijnReserveringenSectie.hidden = true;
        mijnProfiel.hidden = false;

        loginPaneel.hidden = true;
        registratiePaneel.hidden = true;
    } catch (error) {
        console.error("Fout bij het ophalen van het profiel:", error);

        profielBericht.textContent =
            "Je profiel of gekochte boeken konden niet worden geladen. Probeer het opnieuw.";
    }
});

reserveringenMenuKnop.addEventListener("click", async function () {
    const opgeslagenGebruiker =
        sessionStorage.getItem("ingelogdeGebruiker");

    if (!opgeslagenGebruiker) {
        return;
    }

    accountSectie.hidden = true;
    boekenSectie.hidden = true;
    boekenContainer.hidden = true;
    boekDetails.hidden = true;
    mijnProfiel.hidden = true;

    loginPaneel.hidden = true;
    registratiePaneel.hidden = true;
    mijnReserveringenSectie.hidden = false;
    mijnReserveringen.innerHTML = "";
    mijnReserveringenBericht.textContent = "";




    await laadMijnReserveringen();
});

reviewFormulier.addEventListener("submit", async function (event) {
    event.preventDefault();

    const opgeslagenGebruiker =
        sessionStorage.getItem("ingelogdeGebruiker");

    if (!opgeslagenGebruiker) {
        reviewBericht.textContent =
            "Je moet ingelogd zijn om een review te plaatsen.";
        return;
    }

    if (!huidigBoekId) {
        reviewBericht.textContent = "Selecteer eerst een boek.";
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
            if (response.status === 403) {
                reviewBericht.textContent =
                    "Je kunt pas een review schrijven nadat je het boek hebt gekocht.";
                reviewBericht.className = "bericht-fout";
                return;
            }

            reviewBericht.textContent =
                "Je review kon niet worden geplaatst. Probeer het later opnieuw.";
            reviewBericht.className = "bericht-fout";
            return;
        }

        reviewBericht.textContent =
            "Je review is succesvol geplaatst!";

        reviewFormulier.reset();

        const scoreElement = detailsInhoud.querySelector(
            "p:nth-of-type(6)"
        );

        const reviewsContainer =
            detailsInhoud.querySelector("h4")?.nextElementSibling;

        if (scoreElement && reviewsContainer) {
            await laadReviews(
                huidigBoekId,
                scoreElement,
                reviewsContainer
            );
        }
    } catch (error) {
        console.error("Fout bij review plaatsen:", error);
        reviewBericht.textContent = error.message;
    }
});

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

    const vandaag = new Date();
    vandaag.setHours(0, 0, 0, 0);

    const gekozenDatum = new Date(ophaaldatum.value + "T00:00:00");

    if (gekozenDatum < vandaag) {
        reserveringBericht.textContent =
            "Kies een ophaaldatum vanaf vandaag.";
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

            throw new Error(
                "Reserveren mislukt (" +
                response.status + "): " + foutmelding
            );
        }

        reserveringBericht.textContent =
            "Je reservering is aangemaakt!";

        ophaaldatum.value = "";
    } catch (error) {
        console.error("Fout bij reserveren:", error);
        reserveringBericht.textContent = error.message;
    }
});

reserveringenLadenKnop.addEventListener("click", async function () {
    await laadMijnReserveringen();
});

async function laadMijnReserveringen() {
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
            throw new Error(
                "De reserveringen konden niet worden opgehaald."
            );
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
            const reserveringElement = document.createElement("article");
            reserveringElement.classList.add("reservering-kaart");

            const betaalStatusTekst =
                reservering.paymentStatus === "PAID"
                    ? "Betaald"
                    : "Nog niet betaald";

            const reserveringStatusTekst =
                reservering.status === "CONFIRMED"
                    ? "Bevestigd"
                    : reservering.status === "PENDING"
                        ? "In afwachting"
                        : reservering.status;

            const boekTekst = document.createElement("p");

            boekTekst.textContent =
                "Reservering #" + reservering.id +
                " | Boek-ID: " + reservering.bookId +
                " | Ophaaldatum: " + reservering.pickupDate +
                " | Status: " + reserveringStatusTekst +
                " | Betaling: " + betaalStatusTekst;

            reserveringElement.appendChild(boekTekst);

            if (
                reservering.status === "CONFIRMED" &&
                reservering.paymentStatus !== "PAID"
            ) {
                const betaalKnop = document.createElement("button");
                betaalKnop.type = "button";
                betaalKnop.textContent = "Betaal reservering";

                betaalKnop.addEventListener("click", async function () {
                    betaalKnop.disabled = true;
                    betaalKnop.textContent = "Betaling verwerken...";

                    try {
                        const betaalResponse = await fetch(
                            "http://localhost:8080/reservations/" +
                            reservering.id + "/pay",
                            {
                                method: "PUT"
                            }
                        );

                        if (!betaalResponse.ok) {
                            const foutmelding =
                                await betaalResponse.text();

                            throw new Error(
                                "Betaling mislukt (" +
                                betaalResponse.status + "): " +
                                foutmelding
                            );
                        }

                        await laadMijnReserveringen();
                    } catch (error) {
                        console.error("Fout bij betaling:", error);

                        mijnReserveringenBericht.textContent =
                            error.message;

                        betaalKnop.disabled = false;
                        betaalKnop.textContent = "Betaal reservering";
                    }
                });

                reserveringElement.appendChild(betaalKnop);
            }

            try {
                const boekResponse = await fetch(
                    "http://localhost:8080/books/" + reservering.bookId
                );

                if (boekResponse.ok) {
                    const boek = await boekResponse.json();

                    const titel = document.createElement("p");
                    titel.textContent = "Boek: " + boek.title;

                    reserveringElement.prepend(titel);
                }
            } catch (error) {
                console.error("Boektitel ophalen mislukt:", error);
            }

            mijnReserveringen.appendChild(reserveringElement);
        }
    } catch (error) {
        console.error("Fout bij ophalen reserveringen:", error);

        mijnReserveringenBericht.textContent =
            "Er is een verbindingsfout met de server.";
    }
}

toonIngelogdeGebruiker();
toonAccountStatus();
toonBoekenlijst();
laadBoeken();
