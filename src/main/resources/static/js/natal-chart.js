document.addEventListener("DOMContentLoaded", () => {
    const chartContainer = document.querySelector("#natal-chart");
    const dataScript = document.querySelector("#tema-chart-data");

    if (!chartContainer || !dataScript) return;

    try {
        const temaData = JSON.parse(dataScript.textContent);

        // Configurazione dimensioni SVG
        const size = 500;
        const center = size / 2;
        const radius = size / 2 - 40;

        // Estrazione dati di base dalle cuspi delle case di AstroWay
        const houses = temaData.houses || {};
        const ascendant = parseFloat(houses.ascendant) || 0;
        const mc = parseFloat(houses.mc) || 0;

        // Creazione elemento SVG principale
        let svgHtml = `<svg viewBox="0 0 ${size} ${size}" class="astrology-chart-svg">`;

        // 1. Cerchio Zodiacale Esterno e Interno
        svgHtml += `<circle cx="${center}" cy="${center}" r="${radius}" class="chart-border-outer" />`;
        svgHtml += `<circle cx="${center}" cy="${center}" r="${radius - 35}" class="chart-border-inner" />`;

        // Funzione di utilità per convertire i gradi in coordinate cartesiane (invertendo l'asse Y di default dei cerchi)
        // Sottraiamo l'ascendente per ruotare la carta e posizionare l'Ascendente sempre a sinistra (0° geometrici a sinistra)
        const getCoordinates = (degrees, currentRadius) => {
            const angleInRadians = ((degrees - ascendant + 180) * Math.PI) / 180;
            return {
                x: center + currentRadius * Math.cos(angleInRadians),
                y: center + currentRadius * Math.sin(angleInRadians)
            };
        };

        // 2. Disegno dei 12 Settori dei Segni (30° ciascuno)
        for (let i = 0; i < 12; i++) {
            const startDeg = i * 30;
            const p1 = getCoordinates(startDeg, radius);
            const p2 = getCoordinates(startDeg, radius - 35);
            svgHtml += `<line x1="${p1.x}" y1="${p1.y}" x2="${p2.x}" y2="${p2.y}" class="zodiac-division" />`;
        }

        // 3. Disegno delle 12 Case Astrologiche
        if (houses.cusps && Array.isArray(houses.cusps)) {
            houses.cusps.forEach((cusp, index) => {
                const pStart = getCoordinates(cusp, radius - 35);
                const pEnd = getCoordinates(cusp, 60); // Arriva vicino al centro vuoto

                // Evidenzia le linee dell'Ascendente (Casa 1) e del Medio Cielo (Casa 10)
                let lineClass = "house-division";
                if (index === 0) lineClass = "house-division axis-ascendant";
                if (index === 9) lineClass = "house-division axis-mc";

                svgHtml += `<line x1="${pStart.x}" y1="${pStart.y}" x2="${pEnd.x}" y2="${pEnd.y}" class="${lineClass}" />`;
            });
        }

        // 4. Posizionamento dei Simboli dei Pianeti
        // AstroWay restituisce i pianeti tipicamente in una struttura ad oggetto o lista sotto 'planets' o 'points'
        const planets = temaData.planets || temaData.points || {};
        Object.entries(planets).forEach(([name, data]) => {
            if (data && data.longitude !== undefined) {
                const coords = getCoordinates(data.longitude, radius - 55);
                const symbol = data.simbolo || name.substring(0, 2); // Fallback alle prime due lettere se manca il glifo

                svgHtml += `
                    <g class="chart-planet-glyph" data-name="${name}" data-deg="${data.longitude}">
                        <text x="${coords.x}" y="${coords.y}" text-anchor="middle" dominant-baseline="central" class="planet-glyph-text">${symbol}</text>
                        <title>${name}: ${data.longitude.toFixed(2)}°</title>
                    </g>
                `;
            }
        });

        // Chiude il tag SVG e lo inietta nel contenitore
        svgHtml += `</svg>`;
        chartContainer.innerHTML = svgHtml;

    } catch (e) {
        console.error("Errore nel parsing o rendering dei dati del grafico natale:", e);
        chartContainer.textContent = "Impossibile caricare il grafico interattivo.";
    }
});
