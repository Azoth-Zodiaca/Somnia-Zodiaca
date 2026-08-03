// Quicksilver — vanilla JS runtime (trimmed to index.html only)
// index.html calls qsInit() on load, but has no elements that need
// initializing (no [data-countdown], no like/expand buttons, no
// qsToast() call) — kept as a no-op so that call doesn't throw.
window.qsInit = ()=>{};
