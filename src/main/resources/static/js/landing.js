// Ruota zodiacale + interazioni della landing. Nessun inline script (CSP: script-src 'self').
(function () {
    const SEGNI = [
        ["♈", "fuoco"], ["♉", "terra"], ["♊", "aria"], ["♋", "acqua"],
        ["♌", "fuoco"], ["♍", "terra"], ["♎", "aria"], ["♏", "acqua"],
        ["♐", "fuoco"], ["♑", "terra"], ["♒", "aria"], ["♓", "acqua"]
    ];
    const COL = { fuoco: "var(--el-fire)", terra: "var(--el-earth)", aria: "var(--el-air)", acqua: "var(--el-water)" };

    function zodiacWheel(size) {
        const cx = size / 2, cy = size / 2, r = size / 2 - 8, inner = r * 0.62;
        const f = n => n.toFixed(3);
        const parts = SEGNI.map(([glifo, el], i) => {
            const a1 = (i * 30 - 90) * Math.PI / 180, a2 = ((i + 1) * 30 - 90) * Math.PI / 180, mid = (a1 + a2) / 2;
            const x1 = f(cx + Math.cos(a1) * r), y1 = f(cy + Math.sin(a1) * r);
            const x2 = f(cx + Math.cos(a2) * r), y2 = f(cy + Math.sin(a2) * r);
            const gx = f(cx + Math.cos(mid) * (r * .82)), gy = f(cy + Math.sin(mid) * (r * .82));
            const col = COL[el];
            return `<g>
        <line x1="${cx}" y1="${cy}" x2="${x1}" y2="${y1}" stroke="oklch(.3 .04 285)" stroke-width=".6"/>
        <path d="M ${cx} ${cy} L ${x1} ${y1} A ${r} ${r} 0 0 1 ${x2} ${y2} Z"
              fill="color-mix(in oklch, ${col} 12%, transparent)" opacity=".7"/>
        <text x="${gx}" y="${gy}" text-anchor="middle" dominant-baseline="central"
              fill="${col}" class="wheel-glyph">${glifo}</text></g>`;
        }).join("");

        return `<svg viewBox="0 0 ${size} ${size}" width="${size}" height="${size}" role="img"
                 aria-label="Ruota zodiacale">
      <defs><radialGradient id="wbg" cx="50%" cy="50%" r="50%">
        <stop offset="0%" stop-color="oklch(.24 .08 290)"/>
        <stop offset="100%" stop-color="oklch(.09 .02 285)"/>
      </radialGradient></defs>
      <circle cx="${cx}" cy="${cy}" r="${r}" fill="url(#wbg)" stroke="oklch(.35 .05 285)"/>
      <circle cx="${cx}" cy="${cy}" r="${inner}" fill="none" stroke="oklch(.3 .04 285)" stroke-dasharray="2 4"/>
      ${parts}
      <circle cx="${cx}" cy="${cy}" r="${inner}" fill="oklch(.11 .02 285)" stroke="oklch(.35 .05 285)"/>
      <text x="${cx}" y="${cy - 8}" text-anchor="middle" class="wheel-title">Quicksilver</text>
      <text x="${cx}" y="${cy + 14}" text-anchor="middle" class="wheel-sub">⋆ ORACOLO ⋆</text>
    </svg>`;
    }

    const mount = document.getElementById("wheel-mount");
    if (mount) mount.innerHTML = zodiacWheel(Number(mount.dataset.wheelSize) || 380);

    // "Leggi tutto"
    document.querySelectorAll("[data-expand]").forEach(btn => {
        btn.addEventListener("click", () => {
            const card = btn.closest("article");
            const open = card.classList.toggle("is-expanded");
            card.querySelectorAll(".expandable").forEach(p => {
                p.classList.toggle("line-clamp-3", !open && p.classList.contains("dream-text"));
                p.classList.toggle("line-clamp-4", !open && !p.classList.contains("dream-text"));
            });
            btn.textContent = open ? "Riduci ▴" : "Leggi tutto ▾";
        });
    });
})();
