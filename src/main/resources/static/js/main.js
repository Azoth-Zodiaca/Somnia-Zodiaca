/* =========================================================
   QUICKSILVER - JAVASCRIPT PRINCIPALE
   File unico e commentato, diviso in piccole funzioni.
   Ogni funzione controlla da sola se gli elementi che le
   servono esistono nella pagina corrente, quindi puoi
   includere questo file in TUTTE le pagine senza errori.
   ========================================================= */

document.addEventListener("DOMContentLoaded", function () {
  initSidebarToggle();
  initTabs();
  initChipSelectors();
  initShopCategories();
  initOracoloCounter();
  initOracoloForm();
  initLikeButtons();
  initInventoryEquip();
  initSettingsMenu();
  initDeleteAccountConfirmation();
  initHistoryButtons();
});

// controllo della forza della password nella pagina di registrazione
document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("register-form");
  const password = document.getElementById("password");
  const strengthMessage = document.getElementById("password-strength");

  if (!password || !strengthMessage) return;

  function passwordForte(value) {
    return value.length >= 8 &&
      /[a-z]/.test(value) &&
      /[A-Z]/.test(value) &&
      /\d/.test(value) &&
      /[^A-Za-z0-9]/.test(value);
  }

  password.addEventListener("input", function () {
    const value = password.value;
    let strength = 0;

    if (value.length >= 8) strength++;
    if (/[a-z]/.test(value) && /[A-Z]/.test(value)) strength++;
    if (/\d/.test(value)) strength++;
    if (/[^A-Za-z0-9]/.test(value)) strength++;

    strengthMessage.textContent = [
      "Inserisci una password",
      "Molto debole",
      "Debole",
      "Media",
      "Forte"
    ][strength];

    strengthMessage.classList.toggle("password-strong", strength === 4);
  });

  if (form) {
    form.addEventListener("submit", function (event) {
      if (!passwordForte(password.value)) {
        event.preventDefault();
        password.setCustomValidity(
          "La password deve contenere almeno 8 caratteri, una maiuscola, una minuscola, un numero e un simbolo."
        );
        password.reportValidity();
      } else {
        password.setCustomValidity("");
      }
    });
  }
});

/* ---------------------------------------------------------
   1) Apertura/chiusura della sidebar su schermi piccoli.
   Serve un bottone con id="sidebar-toggle" (non presente
   in ogni pagina: se manca, la funzione non fa nulla).
--------------------------------------------------------- */
function initSidebarToggle() {
  var toggleBtn = document.getElementById("sidebar-toggle");
  var sidebar = document.getElementById("sidebar");
  if (!toggleBtn || !sidebar) return;

  toggleBtn.addEventListener("click", function () {
    sidebar.classList.toggle("open");
  });
}

// Saluto in base all'orario (Buongiorno / Buon pomeriggio / Buona sera) nella topbar
document.addEventListener('DOMContentLoaded', () => {
  const elementoSaluto = document.getElementById('saluto-orario');

  if (!elementoSaluto) {
    return;
  }

  const ora = new Date().getHours();

  if (ora >= 5 && ora < 12) {
    elementoSaluto.textContent = 'Buongiorno';
  } else if (ora >= 12 && ora < 18) {
    elementoSaluto.textContent = 'Buon Pomeriggio';
  } else {
    elementoSaluto.textContent = 'Buonasera';
  }
});

/* ---------------------------------------------------------
   2) Tab generiche (es. Riepilogo / Carta / Pianeti / Case
   nel Tema Natale, oppure Popolari / Recenti nel Social).
   Basta dare a ogni link classe "js-tab" e un attributo
   data-target che punta all'id del contenuto da mostrare.
--------------------------------------------------------- */
function initTabs() {
  var tabs = document.querySelectorAll(".js-tab");
  if (tabs.length === 0) return;

  tabs.forEach(function (tab) {
    tab.addEventListener("click", function (event) {
      event.preventDefault();

      var group = tab.closest("[data-tab-group]");
      if (!group) return;

      // Rimuove "active" da tutte le tab dello stesso gruppo
      group.querySelectorAll(".js-tab").forEach(function (t) {
        t.classList.remove("active");
      });
      tab.classList.add("active");

      // Nasconde tutti i pannelli del gruppo e mostra solo quello scelto
      var targetId = tab.getAttribute("data-target");
      group.querySelectorAll(".js-tab-panel").forEach(function (panel) {
        panel.style.display = (panel.id === targetId) ? "block" : "none";
      });
    });
  });
}

/* ---------------------------------------------------------
   3) Selettori a "chip" (es. Umore e Stile nella pagina
   Oracolo). Ogni bottone con classe "chip" e attributo
   data-group diventa selezionabile; solo uno per gruppo
   può essere attivo alla volta.
--------------------------------------------------------- */
function initChipSelectors() {
  var chips = document.querySelectorAll(".chip");
  if (chips.length === 0) return;

  chips.forEach(function (chip) {
    chip.addEventListener("click", function () {
      var groupName = chip.getAttribute("data-group");
      var value = chip.getAttribute("data-value");

      document
        .querySelectorAll('.chip[data-group="' + groupName + '"]')
        .forEach(function (currentChip) {
          currentChip.classList.remove("selected");
        });

      chip.classList.add("selected");

      var hiddenInput = document.getElementById(groupName);
      if (hiddenInput) {
        hiddenInput.value = value;
      }
    });
  });
}

/* ---------------------------------------------------------
   4) Filtri categoria dello Shop (Tutti / Cornici / Sfondi...).
   Aggiunge/rimuove la classe "active" e mostra/nasconde
   gli articoli in base a data-category.
--------------------------------------------------------- */
function initShopCategories() {
  var buttons = document.querySelectorAll(".shop-categories button");
  if (buttons.length === 0) return;

  buttons.forEach(function (btn) {
    btn.addEventListener("click", function () {
      buttons.forEach(function (b) { b.classList.remove("active"); });
      btn.classList.add("active");

      var category = btn.getAttribute("data-category");
      document.querySelectorAll(".shop-item").forEach(function (item) {
        var itemCategory = item.getAttribute("data-category");
        var show = (category === "tutti" || category === itemCategory);
        item.style.display = show ? "" : "none";
      });
    });
  });
}

/* ---------------------------------------------------------
   5) Contatore caratteri nel form "Racconta il tuo sogno"
   della pagina Oracolo. Richiede una textarea con
   id="dream-text" e un elemento con id="dream-counter".
--------------------------------------------------------- */
function initOracoloCounter() {
  var textarea = document.getElementById("dream-text");
  var counter = document.getElementById("dream-counter");
  if (!textarea || !counter) return;

  var maxLength = 4000;
  textarea.addEventListener("input", function () {
    counter.textContent = textarea.value.length + " / " + maxLength;
  });
}

function aggiungiInterpretazioneAllaLista(
  testoSogno,
  testoInterpretazione
) {
  var history = document.getElementById("oracolo-history");
  var emptyMessage = document.getElementById("history-empty");
  var count = document.getElementById("history-count");

  if (!history) return;

  if (emptyMessage) {
    emptyMessage.remove();
  }

  var item = document.createElement("article");
  item.className = "history-item";

  var date = document.createElement("div");
  date.className = "history-date";
  date.textContent = "Adesso";

  var dream = document.createElement("div");
  dream.className = "history-dream";
  dream.textContent = testoSogno;

  var button = document.createElement("button");
  button.type = "button";
  button.className = "history-open";
  button.textContent = "Leggi";

  button.addEventListener("click", function () {
    var resultBox = document.getElementById("risultato-oracolo");
    var resultText = document.getElementById("testo-interpretazione");

    resultBox.hidden = false;
    resultText.innerHTML = DOMPurify.sanitize(
      marked.parse(testoInterpretazione)
    );
  });

  item.appendChild(date);
  item.appendChild(dream);
  item.appendChild(button);

  var firstItem = history.querySelector(".history-item");

  if (firstItem) {
    history.insertBefore(item, firstItem);
  } else {
    history.appendChild(item);
  }

  if (count) {
    count.textContent = parseInt(count.textContent, 10) + 1;
  }
}

function initOracoloForm() {
  var form = document.getElementById("oracolo-form");
  var textarea = document.getElementById("dream-text");
  var resultBox = document.getElementById("risultato-oracolo");
  var resultText = document.getElementById("testo-interpretazione");
  var saveButton = document.getElementById("salva-interpretazione");
  var saveMessage = document.getElementById("salvataggio-messaggio");

  if (!form || !textarea || !resultBox || !resultText) return;

  var generatedInterpretation = "";

  form.addEventListener("submit", async function (event) {
    event.preventDefault();

    var dream = textarea.value.trim();

    if (!dream) {
      textarea.focus();
      return;
    }

    var csrfInput = form.querySelector('input[name="_csrf"]');

    if (!csrfInput) {
      resultText.textContent = "Token di sicurezza non trovato.";
      return;
    }

    var richiesta = {
      testoSogno: dream,
      umore: document.getElementById("umore").value,
      stile: document.getElementById("stile").value,
      usaTemaNatale: document.getElementById("usa-tema").checked
    };

    var submitButton = form.querySelector('button[type="submit"]');

    submitButton.disabled = true;

    if (saveButton) {
      saveButton.hidden = true;
      saveButton.style.display = "none";
    }

    if (saveMessage) {
      saveMessage.textContent = "";
    }

    generatedInterpretation = "";

    resultBox.hidden = false;
    resultText.textContent = "Sto interpretando il sogno...";

    try {
      var response = await fetch("/app/oracolo/interpreta", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "X-CSRF-TOKEN": csrfInput.value
        },
        body: JSON.stringify(richiesta)
      });

      var responseText = await response.text();

      if (!response.ok) {
        throw new Error(
          "Errore HTTP " + response.status + ": " + responseText
        );
      }

      generatedInterpretation = responseText;

      if (typeof marked === "undefined" ||
        typeof DOMPurify === "undefined") {
        throw new Error("Librerie Markdown non caricate.");
      }

      resultText.innerHTML = DOMPurify.sanitize(
        marked.parse(generatedInterpretation)
      );

      if (saveButton) {
        saveButton.removeAttribute("hidden");
        saveButton.style.display = "inline-block";
        saveButton.disabled = false;
      }
    } catch (error) {
      console.error(error);
      resultText.textContent =
        "Non è stato possibile interpretare il sogno.";
    } finally {
      submitButton.disabled = false;
    }
  });

  if (saveButton) {
    saveButton.addEventListener("click", async function () {
      var csrfInput = form.querySelector('input[name="_csrf"]');

      saveButton.disabled = true;
      saveMessage.textContent = "Salvataggio in corso...";

      var richiesta = {
        testoSogno: textarea.value.trim(),
        prompt: "Interpretazione " +
          document.getElementById("stile").value +
          " del sogno",
        interpretazione: generatedInterpretation
      };

      try {
        var response = await fetch("/app/oracolo/salva", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "X-CSRF-TOKEN": csrfInput.value
          },
          body: JSON.stringify(richiesta)
        });

        var message = await response.text();

        if (!response.ok) {
          throw new Error(
            "Errore HTTP " + response.status + ": " + message
          );
        }

        saveMessage.textContent = message;
        saveButton.textContent = "Interpretazione salvata";
        saveButton.disabled = true;

        aggiungiInterpretazioneAllaLista(
          textarea.value.trim(),
          generatedInterpretation
        );
      } catch (error) {
        console.error(error);
        saveMessage.textContent =
          "Salvataggio non riuscito: QI insufficienti o errore del server.";
        saveButton.disabled = false;
      }
    });
  }
}

function initHistoryButtons() {
  var historyButtons = document.querySelectorAll(".history-open");
  var resultBox = document.getElementById("risultato-oracolo");
  var resultText = document.getElementById("testo-interpretazione");

  if (!resultBox || !resultText || historyButtons.length === 0) {
    return;
  }

  historyButtons.forEach(function (button) {
    button.addEventListener("click", function () {
      var interpretation = button.getAttribute("data-interpretazione");

      if (!interpretation) {
        return;
      }

      resultBox.hidden = false;
      resultText.innerHTML = DOMPurify.sanitize(
        marked.parse(interpretation)
      );

      resultBox.scrollIntoView({
        behavior: "smooth",
        block: "start"
      });
    });
  });
}

/* ---------------------------------------------------------
   6) Bottone "like" nei post del Social. Cambia stile e
   incrementa/decrementa il numero visualizzato.
   Richiede: <button class="like-btn" data-liked="false">
             <span class="like-count">24</span></button>
--------------------------------------------------------- */
function initLikeButtons() {
  var likeButtons = document.querySelectorAll(".like-btn");
  if (likeButtons.length === 0) return;

  likeButtons.forEach(function (btn) {
    btn.addEventListener("click", function () {
      var countEl = btn.querySelector(".like-count");
      var liked = btn.getAttribute("data-liked") === "true";
      var count = parseInt(countEl.textContent, 10) || 0;

      if (liked) {
        btn.setAttribute("data-liked", "false");
        btn.classList.remove("liked");
        countEl.textContent = count - 1;
      } else {
        btn.setAttribute("data-liked", "true");
        btn.classList.add("liked");
        countEl.textContent = count + 1;
      }
    });
  });
}

/* ---------------------------------------------------------
   7) Equipaggia/rimuovi un cosmetico nella pagina Inventario.
   Solo dimostrativo lato client: in Spring questa azione
   andrebbe collegata a una chiamata al server (form o fetch)
   che salva la scelta nel database.
--------------------------------------------------------- */
function initInventoryEquip() {
  var equipButtons = document.querySelectorAll(".js-equip-btn");
  if (equipButtons.length === 0) return;

  equipButtons.forEach(function (btn) {
    btn.addEventListener("click", function () {
      var equipped = btn.getAttribute("data-equipped") === "true";
      if (equipped) {
        btn.setAttribute("data-equipped", "false");
        btn.textContent = "Equipaggia";
        btn.classList.remove("btn-primary");
        btn.classList.add("btn-ghost");
      } else {
        btn.setAttribute("data-equipped", "true");
        btn.textContent = "Equipaggiato \u2713";
        btn.classList.remove("btn-ghost");
        btn.classList.add("btn-primary");
      }
    });
  });
}

/* ---------------------------------------------------------
   8) Menu laterale della pagina Impostazioni: mostra solo
   la sezione scelta ed evidenzia la voce di menu attiva.
--------------------------------------------------------- */
function initSettingsMenu() {
  var links = document.querySelectorAll(".settings-menu a");
  var sections = document.querySelectorAll(".settings-section");

  if (links.length === 0) return;

  var activeLink = document.querySelector(".settings-menu a.active");
  var activeTargetId = activeLink
    ? activeLink.getAttribute("data-target")
    : null;

  sections.forEach(function (section) {
    section.style.display =
      section.id === activeTargetId ? "block" : "none";
  });

  links.forEach(function (link) {
    link.addEventListener("click", function (event) {
      event.preventDefault();

      var targetId = link.getAttribute("data-target");

      links.forEach(function (item) {
        item.classList.remove("active");
      });

      link.classList.add("active");

      sections.forEach(function (section) {
        section.style.display =
          section.id === targetId ? "block" : "none";
      });
    });
  });
}


// pop up per eliminazione account in impostazioni
function initDeleteAccountConfirmation() {
  var form = document.querySelector("form[action*='/account/elimina']");
  if (!form) return;

  form.addEventListener("submit", function (event) {
    if (!window.confirm("Sei sicuro di voler eliminare definitivamente il tuo account?")) {
      event.preventDefault();
    }
  });
}
