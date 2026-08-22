document.addEventListener("DOMContentLoaded", () => {
    const input = document.querySelector("#luogo-nascita");

    if (!input) {
        return;
    }

    let timer;

    input.addEventListener("input", () => {
        clearTimeout(timer);

        const query = input.value.trim();

        if (query.length < 3) {
            return;
        }

        timer = setTimeout(() => {
            cercaLocalita(query);
        }, 750);
    });
});

async function cercaLocalita(query) {
    try {
        const response = await fetch(
            `/api/geocoding/search?query=${encodeURIComponent(query)}`
        );

        if (!response.ok) {
            throw new Error("Ricerca località fallita");
        }

        const risultati = await response.json();

        console.log(risultati);
        // Qui mostrerai i risultati e permetterai all'utente di sceglierne uno.
    } catch (error) {
        console.error(error);
    }
}