const boekenContainer = document.querySelector("#boeken");
const boekDetails = document.querySelector("#boek-details");
const detailsInhoud = document.querySelector("#details-inhoud");
const terugKnop = document.querySelector("#terug-knop");

async function laadGemiddeldeScore(boekId) {
    const response = await fetch(
        "http://localhost:8080/reviews/book/" + boekId + "/average"
    );

    if (!response.ok) {
        throw new Error("Gemiddelde reviewscore ophalen is mislukt.");
    }

    return await response.json();

}

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

            detailsKnop.addEventListener("click", async function () {
                boekenContainer.hidden = true;
                boekDetails.hidden = false;

                detailsInhoud.innerHTML = "";

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

                detailsInhoud.appendChild(titelDetail);
                detailsInhoud.appendChild(auteursDetail);
                detailsInhoud.appendChild(beschrijvingDetail);
                detailsInhoud.appendChild(prijsDetail);
                detailsInhoud.appendChild(voorraadDetail);
                detailsInhoud.appendChild(isbnDetail);
                detailsInhoud.appendChild(scoreDetail);

                try {
                    const score = await laadGemiddeldeScore(boek.id);
                    scoreDetail.textContent = "Gemiddelde score: " + score;
                } catch (error) {
                    console.error(
                        "Fout bij het ophalen van de gemiddelde score:",
                        error
                    );

                    scoreDetail.textContent =
                        "Gemiddelde score niet beschikbaar.";
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

terugKnop.addEventListener("click", function () {
    boekDetails.hidden = true;
    boekenContainer.hidden = false;
});

laadBoeken();