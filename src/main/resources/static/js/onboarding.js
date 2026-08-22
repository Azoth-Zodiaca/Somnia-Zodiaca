const simboliSegni = {
    ARIETE: "\u2648",
    TORO: "\u2649",
    GEMELLI: "\u264A",
    CANCRO: "\u264B",
    LEONE: "\u264C",
    VERGINE: "\u264D",
    BILANCIA: "\u264E",
    SCORPIONE: "\u264F",
    SAGITTARIO: "\u2650",
    CAPRICORNO: "\u2651",
    ACQUARIO: "\u2652",
    PESCI: "\u2653"
};

document.addEventListener("DOMContentLoaded", () => {
    const luogoInput = document.querySelector("#luogo-nascita");
    const dataInput = document.querySelector("#data-nascita");
    const risultatiContainer = document.querySelector("#risultati-localita");
    const segnoOutput = document.querySelector("#segno-solare");

    if (!luogoInput || !risultatiContainer) {
        return;
    }

    let timer;

    const form = document.querySelector("form");

    form.addEventListener("submit", event => {
        const geonameId = document.querySelector("#geoname-id").value;

        if (!geonameId) {
            event.preventDefault();
            alert("Seleziona una località dai risultati.");
        }
    });

    luogoInput.addEventListener("input", () => {
        clearTimeout(timer);

        document.querySelector("#geoname-id").value = "";
        document.querySelector("#latitudine").value = "";
        document.querySelector("#longitudine").value = "";
        document.querySelector("#timezone").value = "";

        const query = luogoInput.value.trim();

        if (query.length < 3) {
            risultatiContainer.innerHTML = "";
            return;
        }
        
        timer = setTimeout(() => {
            cercaLocalita(query);
        }, 750);
    });

    if (dataInput && segnoOutput) {
        dataInput.addEventListener("change", () => {
            const segno = calcolaSegno(dataInput.value);

            if (!segno) {
                segnoOutput.textContent = "Inserisci una data valida";
                return;
            }

            segnoOutput.textContent =
                `${simboliSegni[segno]} ${segno}`;
        });
    }
});

async function cercaLocalita(query) {
    const risultatiContainer =
        document.querySelector("#risultati-localita");

    try {
        const response = await fetch(
            `/api/geocoding/search?query=${encodeURIComponent(query)}`
        );

        if (!response.ok) {
            throw new Error(`Ricerca località fallita: ${response.status}`);
        }

        const risultati = await response.json();
        risultatiContainer.innerHTML = "";

        if (risultati.length === 0) {
            risultatiContainer.textContent = "Nessuna località trovata";
            return;
        }

        risultati.forEach(localita => {
            const button = document.createElement("button");

            button.type = "button";
            button.className = "location-result";
            button.textContent =
                `${localita.nome}, ${localita.codicePaese}`;

            button.addEventListener("click", () => {
                selezionaLocalita(localita);
            });

            risultatiContainer.appendChild(button);
        });
    } catch (error) {
        risultatiContainer.textContent =
            "Impossibile cercare la località";
        console.error(error);
    }
}

function selezionaLocalita(localita) {
    document.querySelector("#luogo-nascita").value =
        `${localita.nome}, ${localita.codicePaese}`;

    document.querySelector("#geoname-id").value =
        localita.geonameId;

    document.querySelector("#latitudine").value =
        localita.latitudine;

    document.querySelector("#longitudine").value =
        localita.longitudine;

    document.querySelector("#timezone").value =
        localita.timezoneId ?? "";

    document.querySelector("#risultati-localita").innerHTML = "";
}

function calcolaSegno(data) {
    if (!data) {
        return null;
    }

    const [anno, mese, giorno] = data
        .split("-")
        .map(Number);

    if ((mese === 3 && giorno >= 21) || (mese === 4 && giorno <= 19)) {
        return "ARIETE";
    }

    if ((mese === 4 && giorno >= 20) || (mese === 5 && giorno <= 20)) {
        return "TORO";
    }

    if ((mese === 5 && giorno >= 21) || (mese === 6 && giorno <= 20)) {
        return "GEMELLI";
    }

    if ((mese === 6 && giorno >= 21) || (mese === 7 && giorno <= 22)) {
        return "CANCRO";
    }

    if ((mese === 7 && giorno >= 23) || (mese === 8 && giorno <= 22)) {
        return "LEONE";
    }

    if ((mese === 8 && giorno >= 23) || (mese === 9 && giorno <= 22)) {
        return "VERGINE";
    }

    if ((mese === 9 && giorno >= 23) || (mese === 10 && giorno <= 22)) {
        return "BILANCIA";
    }

    if ((mese === 10 && giorno >= 23) || (mese === 11 && giorno <= 21)) {
        return "SCORPIONE";
    }

    if ((mese === 11 && giorno >= 22) || (mese === 12 && giorno <= 21)) {
        return "SAGITTARIO";
    }

    if ((mese === 12 && giorno >= 22) || (mese === 1 && giorno <= 19)) {
        return "CAPRICORNO";
    }

    if ((mese === 1 && giorno >= 20) || (mese === 2 && giorno <= 18)) {
        return "ACQUARIO";
    }

    return "PESCI";
}