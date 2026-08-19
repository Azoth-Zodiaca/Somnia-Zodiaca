document.addEventListener("DOMContentLoaded", () => {
    const current = window.location.pathname;

    document.querySelectorAll(".sidebar a").forEach(link => {
        const href = link.getAttribute("href");

        if (!href) return;

        const normalizedHref = href.startsWith("/")
            ? href
            : "/" + href.replace(".html", "");

        if (normalizedHref === current) {
            link.classList.add("active");
        }
    });
});
