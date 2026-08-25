/* =========================================================
  SOMNIA ZODIACA - SCRIPT CONDIVISO
  Funzioni organizzate per area funzionale. Gli inizializzatori
  verificano autonomamente se gli elementi necessari esistono,
  così questo file puo essere incluso in tutte le pagine.
  ========================================================= */

/* ---------------------------------------------------------
  AVVIO
  Registra tutti i comportamenti condivisi dopo il caricamento
  del DOM. Le dichiarazioni di funzione sono hoisted, quindi
  l'ordine di questo elenco resta esplicito e leggibile.
--------------------------------------------------------- */
document.addEventListener("DOMContentLoaded", function () {
  initSidebarToggle();
  initSocialFollowing();
  initTabs();
  initChipSelectors();
  initOracoloCounter();
  initOracoloForm();
  initLikeButtons();
  initComments();
  initSettingsMenu();
  initDeleteAccountConfirmation();
  initHistoryButtons();
  initSvuotaOracolo();
  aggiornaScadenze();
  setInterval(aggiornaScadenze, 60000);
  initUploadProfilo();
  initInterpretationExpanders();
  initTemaNatalePremium();
});

/* ---------------------------------------------------------
   AUTENTICAZIONE E PROFILO
--------------------------------------------------------- */

// Controlla la complessita minima della password e aggiorna l'indicatore.
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


// Mostra o nasconde il form per cambiare l'immagine del profilo.
function initUploadProfilo() {
  var editButton = document.getElementById("mostra-upload-profilo");
  var uploadForm = document.getElementById("upload-profilo-form");

  if (!editButton || !uploadForm) {
    return;
  }

  editButton.addEventListener("click", function () {
    uploadForm.classList.toggle("is-hidden");
  });
}

/* ---------------------------------------------------------
  NAVIGAZIONE E CONTROLLI GENERALI
--------------------------------------------------------- */

// Apre e chiude la sidebar mobile, inclusi backdrop, Escape e resize.
function initSidebarToggle() {
  var toggleBtn = document.getElementById("sidebar-toggle");
  var sidebar = document.getElementById("sidebar");
  var backdrop = document.getElementById("sidebar-backdrop");

  if (!toggleBtn || !sidebar) return;

  function setSidebarState(isOpen) {
    sidebar.classList.toggle("open", isOpen);
    document.body.classList.toggle("sidebar-open", isOpen);

    toggleBtn.setAttribute("aria-expanded", String(isOpen));
    toggleBtn.setAttribute(
      "aria-label",
      isOpen ? "Chiudi menu di navigazione" : "Apri menu di navigazione"
    );

    if (backdrop) {
      backdrop.classList.toggle("visible", isOpen);
      backdrop.setAttribute("aria-hidden", String(!isOpen));
    }
  }

  toggleBtn.addEventListener("click", function (event) {
    event.preventDefault();

    var isOpen = sidebar.classList.contains("open");
    setSidebarState(!isOpen);
  });

  if (backdrop) {
    backdrop.addEventListener("click", function () {
      setSidebarState(false);
    });
  }

  sidebar.querySelectorAll("a").forEach(function (link) {
    link.addEventListener("click", function () {
      setSidebarState(false);
    });
  });

  document.addEventListener("keydown", function (event) {
    if (event.key === "Escape" && sidebar.classList.contains("open")) {
      setSidebarState(false);
    }
  });

  window.addEventListener("resize", function () {
    if (window.innerWidth > 960) {
      setSidebarState(false);
    }
  });
}

// Personalizza il saluto della topbar in base all'ora locale.
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

// Gestisce le tab collegate a pannelli tramite data-target.
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
      var panelIds = Array.from(group.querySelectorAll(".js-tab"))
        .map(function (currentTab) { return currentTab.getAttribute("data-target"); });
      document.querySelectorAll(".js-tab-panel").forEach(function (panel) {
        if (panelIds.indexOf(panel.id) !== -1) {
          panel.classList.toggle("is-hidden", panel.id !== targetId);
        }
      });
    });
  });
}

// Mantiene una sola scelta attiva per ogni gruppo di chip.
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
  ORACOLO
--------------------------------------------------------- */

function initTemaNatalePremium() {
  var checkbox = document.getElementById("usa-tema");
  var dialog = document.getElementById("tema-premium-dialog");
  var closeButton = document.getElementById("chiudi-tema-premium");

  if (!checkbox || !dialog || checkbox.dataset.premium === "true") return;

  checkbox.addEventListener("click", function (event) {
    event.preventDefault();
    dialog.showModal();
  });

  if (closeButton) {
    closeButton.addEventListener("click", function () {
      dialog.close();
    });
  }

  dialog.addEventListener("click", function (event) {
    if (event.target === dialog) {
      dialog.close();
    }
  });
}

// Aggiorna il numero di caratteri inseriti nel sogno.
function initOracoloCounter() {
  var textarea = document.getElementById("dream-text");
  var counter = document.getElementById("dream-counter");
  if (!textarea || !counter) return;

  var maxLength = 4000;
  textarea.addEventListener("input", function () {
    counter.textContent = textarea.value.length + " / " + maxLength;
  });
}

function initSvuotaOracolo() {
  var button = document.getElementById("svuota-oracolo");
  var textarea = document.getElementById("dream-text");
  var counter = document.getElementById("dream-counter");

  if (!button || !textarea) return;

  button.addEventListener("click", function () {
    textarea.value = "";


    if (counter) {
      counter.textContent = "0 / 4000";
    }

    resetRisultatoOracolo();

    textarea.focus();
  });
}

function resetRisultatoOracolo() {
  var resultBox = document.getElementById("risultato-oracolo");
  var resultText = document.getElementById("testo-interpretazione");
  var saveButton = document.getElementById("salva-interpretazione");
  var shareBox = document.getElementById("condivisione-interpretazione");
  var shareText = document.getElementById("testo-post");
  var publishButton = document.getElementById("pubblica-interpretazione");

  if (resultBox) {
    resultBox.hidden = true;
    resultBox.removeAttribute("data-interpretazione-id");
  }

  if (resultText) {
    resultText.innerHTML = "";
  }

  if (saveButton) {
    saveButton.setAttribute("hidden", "");
    saveButton.classList.add("is-hidden");
    saveButton.classList.remove("btn-ghost");
    saveButton.classList.add("btn-primary");
    saveButton.disabled = false;
    saveButton.textContent = "Salva interpretazione - 20 QI";
  }

  if (shareBox) {
    shareBox.classList.add("is-hidden");
  }

  if (shareText) {
    shareText.value = "";
  }

  if (publishButton) {
    publishButton.disabled = true;
  }
}

// Converte una scadenza ISO nel testo mostrato nella cronologia.
function testoScadenza(scadenza) {
  var differenza =
    new Date(scadenza).getTime() - Date.now();

  if (differenza <= 0) {
    return "Scaduta";
  }

  var minuti = Math.ceil(differenza / 60000);
  var ore = Math.floor(minuti / 60);
  var minutiRestanti = minuti % 60;

  if (ore > 0 && minutiRestanti > 0) {
    return "Scade tra " + ore + " ore e " +
      minutiRestanti + " minuti";
  }

  if (ore > 0) {
    return "Scade tra " + ore +
      (ore === 1 ? " ora" : " ore");
  }

  return "Scade tra " + minuti +
    (minuti === 1 ? " minuto" : " minuti");
}

function aggiornaScadenze() {
  document.querySelectorAll(
    ".history-status span[data-scadenza]"
  ).forEach(function (statusText) {
    statusText.textContent = testoScadenza(
      statusText.getAttribute("data-scadenza")
    );
  });
}

function aggiornaOpzioniOracolo(umore, stile) {
  [
    ["umore", umore],
    ["stile", stile]
  ].forEach(function (opzione) {
    var gruppo = opzione[0];
    var valore = opzione[1];
    var valoreNormalizzato = valore ? valore.toUpperCase() : "";
    var input = document.getElementById(gruppo);

    if (input && valore) {
      input.value = valore;
    }

    document
      .querySelectorAll('.chip[data-group="' + gruppo + '"]')
      .forEach(function (chip) {
        chip.classList.toggle(
          "selected",
          chip.getAttribute("data-value").toUpperCase() === valoreNormalizzato
        );
      });
  });
}

function aggiungiInterpretazioneAllaLista(
  testoSogno,
  testoInterpretazione,
  interpretazioneId,
  scadenza,
  umore,
  stile
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

  item.dataset.interpretazioneId = interpretazioneId;

  var status = document.createElement("div");
  status.className = "history-status";

  var statusText = document.createElement("span");
  statusText.className = "status-temporary";
  statusText.setAttribute("data-scadenza", scadenza);
  statusText.textContent = testoScadenza(scadenza);

  status.appendChild(statusText);

  var dreamRow = document.createElement("div");
  dreamRow.className = "history-dream-row";

  var dream = document.createElement("div");
  dream.className = "history-dream";
  dream.textContent = testoSogno;

  var tags = document.createElement("div");
  tags.className = "history-tags";

  if (umore) {
    var moodTag = document.createElement("span");
    moodTag.className = "history-tag history-tag-mood";
    moodTag.textContent = umore;
    tags.appendChild(moodTag);
  }

  if (stile) {
    var styleTag = document.createElement("span");
    styleTag.className = "history-tag history-tag-style";
    styleTag.textContent = stile;
    tags.appendChild(styleTag);
  }

  dreamRow.appendChild(dream);
  dreamRow.appendChild(tags);

  var button = document.createElement("button");
  button.type = "button";
  button.className = "history-open";
  button.textContent = "Leggi";
  button.dataset.interpretazioneId = interpretazioneId;
  button.dataset.permanente = "false";
  button.dataset.scadenza = scadenza;
  button.dataset.testoSogno = testoSogno;
  button.dataset.umore = umore;
  button.dataset.stile = stile;

  button.addEventListener("click", function () {
    var resultBox = document.getElementById("risultato-oracolo");
    var resultText = document.getElementById("testo-interpretazione");
    var saveButton = document.getElementById("salva-interpretazione");
    var textarea = document.getElementById("dream-text");
    var permanente = button.dataset.permanente === "true";

    if (textarea) {
      textarea.value = testoSogno;
      var counter = document.getElementById("dream-counter");
      if (counter) {
        counter.textContent = testoSogno.length + " / 4000";
      }
    }

    aggiornaOpzioniOracolo(umore, stile);

    resultBox.hidden = false;
    resultBox.dataset.interpretazioneId = interpretazioneId;

    mostraFormCondivisione(interpretazioneId, permanente);

    resultText.innerHTML = DOMPurify.sanitize(
      marked.parse(testoInterpretazione)
    );

    if (saveButton) {
      saveButton.hidden = false;
      saveButton.style.display = "inline-block";
      saveButton.disabled = false;
      saveButton.textContent = permanente
        ? "Cancella interpretazione"
        : "Salva interpretazione - 20 QI";
      saveButton.dataset.action = permanente ? "delete" : "save";
      saveButton.classList.toggle("btn-ghost", permanente);
      saveButton.classList.toggle("btn-primary", !permanente);
    }
  });

  item.appendChild(date);
  item.appendChild(status);
  item.appendChild(dreamRow);
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
  var submitButton = form
    ? form.querySelector('button[type="submit"]')
    : null;

  if (!form || !textarea || !resultBox || !resultText) return;

  aggiornaDisponibilitaInterpretazione(submitButton);

  var generatedInterpretation = "";
  var savedInterpretationId = null;

  form.addEventListener("submit", async function (event) {
    event.preventDefault();

    var dream = textarea.value.trim();

    if (!dream) {
      textarea.focus();
      return;
    }

    resetRisultatoOracolo();

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

    submitButton.disabled = true;

    if (saveButton) {
      saveButton.hidden = true;
      saveButton.classList.add("is-hidden");
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

      var saveResponse = await fetch("/app/oracolo/salva", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "X-CSRF-TOKEN": csrfInput.value
        },
        body: JSON.stringify({
          testoSogno: dream,
          prompt: "Interpretazione " +
            document.getElementById("stile").value +
            " del sogno",
          interpretazione: generatedInterpretation,
          umore: document.getElementById("umore").value,
          stile: document.getElementById("stile").value
        })
      });

      if (!saveResponse.ok) {
        throw new Error(await saveResponse.text());
      }

      var saveData = await saveResponse.json();
      savedInterpretationId = Number(saveData.id);
      aggiornaSaldoQi(saveData.qi);

      mostraFormCondivisione(savedInterpretationId, false);

      resultBox.dataset.interpretazioneId = savedInterpretationId;

      var scadenza = new Date(
        Date.now() + 48 * 60 * 60 * 1000
      ).toISOString();

      aggiungiInterpretazioneAllaLista(
        dream,
        generatedInterpretation,
        savedInterpretationId,
        scadenza,
        richiesta.umore,
        richiesta.stile
      );

      if (saveButton) {
        saveButton.removeAttribute("hidden");
        saveButton.classList.remove("is-hidden");
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

      var interpretationId = Number(
        resultBox.dataset.interpretazioneId
      );

      if (saveButton.dataset.action === "delete") {
        saveButton.disabled = true;

        try {
          var deleteResponse = await fetch("/app/oracolo/cancella", {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              "X-CSRF-TOKEN": csrfInput.value
            },
            body: JSON.stringify({
              interpretazioneId: interpretationId
            })
          });

          if (!deleteResponse.ok) {
            throw new Error("Impossibile cancellare l'interpretazione.");
          }

          var historyItem = document.querySelector(
            '.history-item[data-interpretazione-id="' +
            interpretationId +
            '"]'
          );

          if (historyItem) {
            var statusText = historyItem.querySelector(".history-status span");
            var historyButton = historyItem.querySelector(".history-open");
            var scadenza = new Date().toISOString();

            if (statusText) {
              statusText.className = "status-temporary";
              statusText.textContent = "Scaduta";
              statusText.setAttribute("data-scadenza", scadenza);
            }

            if (historyButton) {
              historyButton.dataset.permanente = "false";
              historyButton.dataset.scadenza = scadenza;
            }
          }

          resetRisultatoOracolo();
        } catch (error) {
          console.error(error);
          saveButton.disabled = false;
        }

        return;
      }

      saveButton.disabled = true;

      try {
        var response = await fetch("/app/oracolo/rendi-permanente", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "X-CSRF-TOKEN": csrfInput.value
          },
          body: JSON.stringify({
            interpretazioneId: interpretationId
          })
        });

        var responseData = await response.json();

        if (!response.ok) {
          throw new Error("Impossibile rendere permanente l'interpretazione.");
        }

        aggiornaSaldoQi(responseData.qi);

        saveButton.dataset.action = "delete";
        saveButton.textContent = "Cancella interpretazione";
        saveButton.classList.remove("btn-primary");
        saveButton.classList.add("btn-ghost");
        saveButton.hidden = false;
        saveButton.classList.remove("is-hidden");
        saveButton.disabled = false;

        var publishButton = document.getElementById("pubblica-interpretazione");
        if (publishButton) {
          publishButton.disabled = false;
        }

        var historyItem = document.querySelector(
          '.history-item[data-interpretazione-id="' +
          interpretationId +
          '"]'
        );

        if (historyItem) {
          var statusText = historyItem.querySelector(".history-status span");

          if (statusText) {
            statusText.className = "status-permanent";
            statusText.textContent = "Permanente";
            statusText.removeAttribute("data-scadenza");
          }

          var historyButton = historyItem.querySelector(".history-open");

          if (historyButton) {
            historyButton.dataset.permanente = "true";
            historyButton.removeAttribute("data-scadenza");
          }
        }

      } catch (error) {
        console.error(error);
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
      var interpretation = button.getAttribute(
        "data-interpretazione"
      );
      var interpretationId = button.getAttribute(
        "data-interpretazione-id"
      );

      var permanente =
        button.getAttribute("data-permanente") === "true";

      var scadenza = button.getAttribute("data-scadenza");
      var historyItem = button.closest(".history-item");
      var statusText = historyItem
        ? historyItem.querySelector(".history-status span")
        : null;

      if (!permanente && scadenza && statusText) {
        statusText.textContent = testoScadenza(scadenza);
      }

      if (!interpretationId) {
        return;
      }

      resultBox.hidden = false;
      resultBox.dataset.interpretazioneId = interpretationId;

      var textarea = document.getElementById("dream-text");
      if (textarea) {
        textarea.value = button.getAttribute("data-testo-sogno") || "";
        var counter = document.getElementById("dream-counter");
        if (counter) {
          counter.textContent = textarea.value.length + " / 4000";
        }
      }

      aggiornaOpzioniOracolo(
        button.getAttribute("data-umore"),
        button.getAttribute("data-stile")
      );

      mostraFormCondivisione(interpretationId, permanente);

      var saveButton = document.getElementById("salva-interpretazione");

      if (saveButton) {
        saveButton.hidden = false;
        saveButton.classList.remove("is-hidden");
        saveButton.disabled = false;
        saveButton.dataset.action = permanente ? "delete" : "save";
        saveButton.classList.toggle("btn-ghost", permanente);
        saveButton.classList.toggle("btn-primary", !permanente);
        saveButton.textContent = permanente
          ? "Cancella interpretazione"
          : "Salva interpretazione - 20 QI";
      }

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

function mostraFormCondivisione(interpretazioneId, permanente) {
  var formBox = document.getElementById("condivisione-interpretazione");
  var hiddenId = document.getElementById(
    "condivisione-interpretazione-id"
  );
  var testoPost = document.getElementById("testo-post");
  var publishButton = document.getElementById("pubblica-interpretazione");

  if (!formBox || !hiddenId) {
    return;
  }

  hiddenId.value = interpretazioneId;
  formBox.classList.remove("is-hidden");

  if (publishButton) {
    publishButton.disabled = !permanente;
    publishButton.title = permanente
      ? "Pubblica nella community"
      : "Rendi permanente l'interpretazione prima di pubblicarla";
  }

  if (testoPost) {
    testoPost.value = "";
  }
}

function aggiornaSaldoQi(qi) {
  var saldo = document.getElementById("saldo-qi");

  if (saldo && Number.isFinite(Number(qi))) {
    saldo.textContent = qi;
    aggiornaDisponibilitaInterpretazione();
  }
}

function aggiornaDisponibilitaInterpretazione(submitButton) {
  var button = submitButton || document.querySelector(
    '#oracolo-form button[type="submit"]'
  );
  var saldo = document.getElementById("saldo-qi");

  if (button && saldo) {
    button.disabled = Number(saldo.textContent.trim()) < 20;
  }
}

/* ---------------------------------------------------------
  SOCIAL
--------------------------------------------------------- */

// Apre e chiude il pannello degli utenti seguiti.
function initSocialFollowing() {
  var toggleBtn = document.getElementById("social-following-toggle");
  var panel = document.getElementById("social-following-panel");
  var closeBtn = document.getElementById("social-following-close");
  var backdrop = document.getElementById("social-following-backdrop");

  if (!toggleBtn || !panel) return;

  function setPanelState(isOpen) {
    panel.classList.toggle("is-open", isOpen);
    if (backdrop) backdrop.classList.toggle("is-open", isOpen);
    document.body.classList.toggle("social-following-open", isOpen);
    toggleBtn.setAttribute("aria-expanded", String(isOpen));
  }

  toggleBtn.addEventListener("click", function () {
    setPanelState(!panel.classList.contains("is-open"));
  });

  if (closeBtn) closeBtn.addEventListener("click", function () {
    setPanelState(false);
  });

  if (backdrop) backdrop.addEventListener("click", function () {
    setPanelState(false);
  });

  document.addEventListener("keydown", function (event) {
    if (event.key === "Escape" && panel.classList.contains("is-open")) {
      setPanelState(false);
    }
  });
}

// Invia il like in AJAX e sincronizza tutte le copie dello stesso post.
function initLikeButtons() {
  var likeForms = document.querySelectorAll("form[data-like-endpoint]");

  likeForms.forEach(function (form) {
    form.addEventListener("submit", function (event) {
      event.preventDefault();

      var button = form.querySelector(".like-btn");
      var endpoint = form.dataset.likeEndpoint;

      if (!button || !endpoint) return;

      button.disabled = true;

      fetch(endpoint, {
        method: "POST",
        headers: {
          "X-Requested-With": "XMLHttpRequest"
        },
        body: new FormData(form)
      })
        .then(function (response) {
          if (!response.ok) throw new Error("Like request failed");
          return response.json();
        })
        .then(function (data) {
          var postId = button.getAttribute("data-post-id");
          var matchingButtons = document.querySelectorAll(
            '.like-btn[data-post-id="' + postId + '"]'
          );

          matchingButtons.forEach(function (matchingButton) {
            var icon = matchingButton.querySelector(".like-icon");
            var count = matchingButton.querySelector(".like-count");

            matchingButton.classList.toggle("liked", data.liked);
            matchingButton.setAttribute("aria-pressed", String(data.liked));
            if (icon) icon.textContent = data.liked ? "♥" : "♡";
            if (count) count.textContent = data.count;
          });
        })
        .catch(function () {
          form.submit();
        })
        .finally(function () {
          button.disabled = false;
        });
    });
  });
}

function initComments() {
  var buttons = document.querySelectorAll(".js-comments-toggle");
  var panels = document.querySelectorAll(".social-comments-panel");
  var commentForms = document.querySelectorAll(".social-comment-form");

  commentForms.forEach(function (form) {
    form.addEventListener("submit", function () {
      sessionStorage.setItem("social-comment-scroll", String(window.scrollY));

      var commentPanel = form.closest(".social-comments-panel");
      if (commentPanel) {
        sessionStorage.setItem("social-comment-panel", commentPanel.id);
      }
    });
  });

  buttons.forEach(function (button) {
    button.addEventListener("click", function () {
      var targetId = button.getAttribute("data-comments-target");
      var target = document.getElementById(targetId);
      var isOpen = target && !target.classList.contains("is-hidden");

      panels.forEach(function (panel) {
        panel.classList.add("is-hidden");
      });
      buttons.forEach(function (otherButton) {
        otherButton.setAttribute("aria-expanded", "false");
      });

      if (!isOpen && target) {
        target.classList.remove("is-hidden");
        button.setAttribute("aria-expanded", "true");
      }
    });
  });

  var initialTargetId = window.location.hash.substring(1);
  var savedPanelId = sessionStorage.getItem("social-comment-panel");
  if (!initialTargetId && savedPanelId) {
    initialTargetId = savedPanelId;
  }

  var initialTarget = document.getElementById(initialTargetId);
  var initialButton = document.querySelector(
    '[data-comments-target="' + initialTargetId + '"]'
  );

  if (initialTarget && initialButton) {
    initialTarget.classList.remove("is-hidden");
    initialButton.setAttribute("aria-expanded", "true");
  }

  var savedScroll = sessionStorage.getItem("social-comment-scroll");
  if (savedScroll !== null) {
    sessionStorage.removeItem("social-comment-scroll");
    sessionStorage.removeItem("social-comment-panel");
    window.requestAnimationFrame(function () {
      window.requestAnimationFrame(function () {
        window.scrollTo(0, Number(savedScroll));
      });
    });
  }
}

function initInterpretationExpanders() {
  var buttons = document.querySelectorAll(
    ".js-expand-interpretation"
  );

  buttons.forEach(function (button) {
    button.addEventListener("click", function () {
      var interpretationBox = button.closest(".interpretation-box");

      if (!interpretationBox) {
        return;
      }

      var text = interpretationBox.querySelector(
        ".social-interpretation-text"
      );

      if (!text) {
        return;
      }

      var expanded = text.classList.toggle("is-expanded");

      button.textContent = expanded
        ? "Riduci \u2039"
        : "Leggi tutto \u203A";
    });
  });
}

/* ---------------------------------------------------------
  IMPOSTAZIONI E ACCOUNT
--------------------------------------------------------- */

// Mostra la sola sezione impostazioni richiesta e aggiorna il menu attivo.
function initSettingsMenu() {
  var links = document.querySelectorAll(".settings-menu a");
  var sections = document.querySelectorAll(".settings-section");

  if (links.length === 0) return;

  var activeLink = document.querySelector(".settings-menu a.active");
  var activeTargetId = activeLink
    ? activeLink.getAttribute("data-target")
    : null;

  var requestedSection = new URLSearchParams(window.location.search).get("sezione");
  var requestedLink = requestedSection
    ? document.querySelector('.settings-menu a[data-target="sezione-' + requestedSection + '"]')
    : null;

  if (requestedLink) {
    activeLink = requestedLink;
    activeTargetId = requestedLink.getAttribute("data-target");
    links.forEach(function (item) {
      item.classList.toggle("active", item === requestedLink);
    });
  }

  sections.forEach(function (section) {
    section.classList.toggle("is-hidden", section.id !== activeTargetId);
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
        section.classList.toggle("is-hidden", section.id !== targetId);
      });
    });
  });
}


// Chiede conferma prima dell'eliminazione definitiva dell'account.
function initDeleteAccountConfirmation() {
  var form = document.querySelector("form[action*='/account/elimina']");
  if (!form) return;

  form.addEventListener("submit", function (event) {
    if (!window.confirm("Sei sicuro di voler eliminare definitivamente il tuo account?")) {
      event.preventDefault();
    }
  });
}

/* ---------------------------------------------------------
  COMPONENTI VISIVI
--------------------------------------------------------- */

// Applica alle barre la percentuale fornita dall'attributo data-percentuale.
document.querySelectorAll("[data-percentuale]").forEach(barra => {
  barra.style.width =
    `${barra.dataset.percentuale}%`;
});
