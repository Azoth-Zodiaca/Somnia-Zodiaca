document.addEventListener("DOMContentLoaded", () => {
    const chartContainer = document.querySelector("#natal-chart");

    if (!chartContainer) {
        return;
    }

    renderChart(chartContainer, parseChartData(chartContainer.dataset.chart));
});

const zodiacSymbols = [
    "\u2648", "\u2649", "\u264a", "\u264b", "\u264c", "\u264d",
    "\u264e", "\u264f", "\u2650", "\u2651", "\u2652", "\u2653"
];

const planetSymbols = {
    Sun: "\u2609", Moon: "\u263d", Mercury: "\u263f", Venus: "\u2640",
    Mars: "\u2642", Jupiter: "\u2643", Saturn: "\u2644", Uranus: "\u2645",
    Neptune: "\u2646", Pluto: "\u2647", Sole: "\u2609", Luna: "\u263d",
    Mercurio: "\u263f", Venere: "\u2640", Marte: "\u2642", Giove: "\u2643",
    Saturno: "\u2644", Urano: "\u2645", Nettuno: "\u2646", Plutone: "\u2647"
};

function parseChartData(rawData) {
    if (!rawData) {
        return demoChartData();
    }

    try {
        return JSON.parse(rawData);
    } catch (error) {
        console.error("Dati della carta natale non validi", error);
        return demoChartData();
    }
}

function demoChartData() {
    return {
        houses: {
            ascendant: 119.52,
            mc: 10.72,
            cusps: Array.from({ length: 12 }, (_, index) => (119.52 + index * 30) % 360)
        },
        planets: [
            { name: "Sole", longitude: 228.77 },
            { name: "Luna", longitude: 285.4 },
            { name: "Mercurio", longitude: 214.2 },
            { name: "Venere", longitude: 198.1 },
            { name: "Marte", longitude: 72.8 },
            { name: "Giove", longitude: 342.6 },
            { name: "Saturno", longitude: 16.4 }
        ]
    };
}

function renderChart(container, chartData) {
    const size = 560;
    const center = size / 2;
    const outerRadius = 238;
    const innerRadius = 188;
    const houses = chartData.houses || {};
    const ascendant = numberOr(houses.ascendant, 119.52);
    const mc = numberOr(houses.mc, 10.72);
    const cusps = Array.isArray(houses.cusps) ? houses.cusps : [];
    const planets = Array.isArray(chartData.planets)
        ? chartData.planets
        : Object.entries(chartData.planets || {}).map(([name, value]) => ({ name, ...value }));

    const point = (longitude, radius) => {
        const radians = ((longitude - ascendant + 180) * Math.PI) / 180;
        return { x: center + radius * Math.cos(radians), y: center + radius * Math.sin(radians) };
    };

    let svg = `<svg viewBox="0 0 ${size} ${size}" class="astrology-chart-svg" role="img" aria-label="Carta astrale">`;
    svg += `<circle cx="${center}" cy="${center}" r="${outerRadius}" class="chart-border-outer"/>`;
    svg += `<circle cx="${center}" cy="${center}" r="${innerRadius}" class="chart-border-inner"/>`;
    svg += `<circle cx="${center}" cy="${center}" r="70" class="chart-core"/>`;

    zodiacSymbols.forEach((symbol, index) => {
        const label = point(index * 30 + 15, 214);
        const start = point(index * 30, outerRadius);
        const end = point(index * 30, innerRadius);
        svg += `<line x1="${start.x}" y1="${start.y}" x2="${end.x}" y2="${end.y}" class="zodiac-division"/>`;
        svg += `<text x="${label.x}" y="${label.y}" class="zodiac-glyph" text-anchor="middle" dominant-baseline="central">${symbol}</text>`;
    });

    cusps.forEach((cusp, index) => {
        const start = point(Number(cusp), innerRadius);
        const end = point(Number(cusp), 70);
        const axisClass = index === 0 ? " axis-ascendant" : index === 9 ? " axis-mc" : "";
        svg += `<line x1="${start.x}" y1="${start.y}" x2="${end.x}" y2="${end.y}" class="house-division${axisClass}"/>`;
    });

    planets.forEach(planet => {
        const longitude = Number(planet.longitude);
        if (!Number.isFinite(longitude)) {
            return;
        }
        const position = point(longitude, 145);
        const name = planet.name || planet.nome || "Pianeta";
        const symbol = planet.symbol || planet.simbolo || planetSymbols[name] || "*";
        svg += `<g class="chart-planet-glyph" data-name="${name}"><text x="${position.x}" y="${position.y}" class="planet-glyph-text" text-anchor="middle" dominant-baseline="central">${symbol}</text><title>${name}: ${longitude.toFixed(2)}°</title></g>`;
    });

    const ascendantPoint = point(ascendant, outerRadius);
    const mcPoint = point(mc, outerRadius);
    svg += `<text x="${ascendantPoint.x}" y="${ascendantPoint.y}" class="chart-axis-label" text-anchor="middle">ASC</text>`;
    svg += `<text x="${mcPoint.x}" y="${mcPoint.y}" class="chart-axis-label" text-anchor="middle">MC</text></svg>`;
    container.innerHTML = svg;
}

function numberOr(value, fallback) {
    const number = Number(value);
    return Number.isFinite(number) ? number : fallback;
}
