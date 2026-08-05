// Quicksilver — vanilla JS runtime
// Loaded with `defer` from fragments/app-layout.html, so it runs after the
// DOM is parsed but before DOMContentLoaded — no inline <script> or inline
// event handlers needed anywhere in the pages themselves (the site's CSP
// blocks both: script-src 'self', style-src 'self').

// Countdown badges: <span data-countdown="ms-remaining">
function initCountdowns(){
  document.querySelectorAll('[data-countdown]').forEach(el => {
    let ms = parseInt(el.dataset.countdown, 10);
    const pad = n => String(n).padStart(2, '0');
    const tick = () => {
      ms = Math.max(0, ms - 1000);
      const h = Math.floor(ms / 3600000), m = Math.floor((ms % 3600000) / 60000), s = Math.floor((ms % 60000) / 1000);
      el.textContent = `⏳ ${pad(h)}:${pad(m)}:${pad(s)}`;
      if (ms < 6 * 3600000){ el.classList.add('twinkle', 'text-warning'); el.classList.remove('text-quicksilver'); }
    };
    tick();
    setInterval(tick, 1000);
  });
}

// Progress bars: <div class="bar"><i data-fill="72"></i></div>
// Set via the CSSOM (el.style.width = ...) rather than the style attribute —
// CSP's style-src-attr blocks the HTML `style` attribute, not per-property
// CSSOM assignment, so this is the CSP-safe way to drive a dynamic width.
function initBars(){
  document.querySelectorAll('.bar > i[data-fill]').forEach(el => {
    el.style.width = el.dataset.fill + '%';
  });
}

// Toast messages
function toast(msg){
  let t = document.querySelector('.toast');
  if (!t){ t = document.createElement('div'); t.className = 'toast'; document.body.appendChild(t); }
  t.textContent = msg;
  t.classList.add('show');
  clearTimeout(t._to);
  t._to = setTimeout(() => t.classList.remove('show'), 1800);
}
window.qsToast = toast;

// Any element with data-toast="message" fires that toast on click, instead
// of an inline onclick="qsToast(...)" handler (also blocked by CSP).
function initToastButtons(){
  document.querySelectorAll('[data-toast]').forEach(el => {
    el.addEventListener('click', () => toast(el.dataset.toast));
  });
}

let qsInitDone = false;
function qsInit(){
  if (qsInitDone) return;
  qsInitDone = true;
  initCountdowns();
  initBars();
  initToastButtons();
}
window.qsInit = qsInit;

// Run now (safe — this script is loaded with `defer`, so the DOM is already
// parsed by the time this executes), plus a DOMContentLoaded fallback in
// case that assumption is ever wrong for some reason.
qsInit();
document.addEventListener('DOMContentLoaded', qsInit);
