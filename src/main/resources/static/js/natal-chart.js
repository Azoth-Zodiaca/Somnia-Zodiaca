document.addEventListener("DOMContentLoaded", () => {
    const chartContainer = document.querySelector("#natal-chart");

    if (!chartContainer) {
        return;
    }

    const ascendant = chartContainer.dataset.ascendant;
    const mc = chartContainer.dataset.mc;

    chartContainer.textContent =
        `Ascendente: ${ascendant}° · MC: ${mc}°`;
});