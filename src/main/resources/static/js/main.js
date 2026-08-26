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
  initSocialActions();
  initSocialMarkdown();
  initNatalMarkdown();
  initSettingsMenu();
  initDeleteAccountConfirmation();
  initHistoryButtons();
  apriInterpretazioneDaQuery();
  initSvuotaOracolo();
  aggiornaScadenze();
  setInterval(aggiornaScadenze, 60000);
  initUploadProfilo();
  initInterpretationExpanders();
  initTemaNatalePremium();
});

function configurazioneOracolo() {
  var form = document.getElementById("oracolo-form");

  if (!form) {
    return {};
  }

  return {
    costoInterpretazione: Number(form.dataset.costoInterpretazione),
    costoPermanenza: Number(form.dataset.costoPermanenza),
    durataCacheOre: Number(form.dataset.durataCacheOre),
    limiteSognoCaratteri: Number(form.dataset.limiteSognoCaratteri)
  };
}

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
  DASHBOARD
--------------------------------------------------------- */

function apriInterpretazioneDaQuery() {
  var form = document.getElementById("oracolo-form");

  if (!form) {
    return;
  }

  var interpretazioneId = form.dataset.interpretazioneId;
  var azione = form.dataset.azione;

  if (!interpretazioneId) {
    return;
  }

  var historyButton = document.querySelector(
    '.history-open[data-interpretazione-id="' +
    interpretazioneId +
    '"]'
  );

  if (!historyButton) {
    return;
  }

  historyButton.click();

  if (azione === "salva") {
    window.setTimeout(function () {
      var saveButton = document.getElementById(
        "salva-interpretazione"
      );

      if (saveButton && saveButton.dataset.action !== "delete") {
        saveButton.click();
      }
    }, 100);
  }

  if (azione === "pubblica") {
    window.setTimeout(function () {
      var shareBox = document.getElementById(
        "condivisione-interpretazione"
      );

      if (shareBox) {
        shareBox.scrollIntoView({
          behavior: "smooth",
          block: "center"
        });
      }
    }, 100);
  }
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

  var maxLength = configurazioneOracolo().limiteSognoCaratteri;
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
      counter.textContent = "0 / " + configurazioneOracolo().limiteSognoCaratteri;
    }

    resetRisultatoOracolo();

    textarea.focus();
  });
}

function resetRisultatoOracolo() {
  var resultBox = document.getElementById("risultato-oracolo");
  var resultText = document.getElementById("testo-interpretazione");
  var saveButton = document.getElementById("salva-interpretazione");
  var externalShareButton = document.getElementById("condividi-esternamente");
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
    saveButton.textContent = "Salva interpretazione - " +
      configurazioneOracolo().costoPermanenza + " QI";
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

  if (externalShareButton) {
    externalShareButton.disabled = true;
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
        counter.textContent = testoSogno.length + " / " +
          configurazioneOracolo().limiteSognoCaratteri;
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
        : "Salva interpretazione - " +
        configurazioneOracolo().costoPermanenza + " QI";
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
  var externalShareButton = document.getElementById("condividi-esternamente");
  var submitButton = form
    ? form.querySelector('button[type="submit"]')
    : null;

  if (!form || !textarea || !resultBox || !resultText) return;

  initExternalShare(resultText, textarea, externalShareButton);

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

      aggiornaInterpretazioniResidue(form, saveData.interpretazioniResidue);

      mostraFormCondivisione(savedInterpretationId, false);

      resultBox.dataset.interpretazioneId = savedInterpretationId;

      var scadenza = new Date(
        Date.now() + configurazioneOracolo().durataCacheOre * 60 * 60 * 1000
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

      if (externalShareButton) {
        externalShareButton.disabled = false;
      }
    } catch (error) {
      console.error(error);
      resultText.textContent = error.message ||
        "Non è stato possibile interpretare il sogno.";
    } finally {
      aggiornaDisponibilitaInterpretazione(submitButton);
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
  var externalShareButton = document.getElementById("condividi-esternamente");

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
          counter.textContent = textarea.value.length + " / " +
            configurazioneOracolo().limiteSognoCaratteri;
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
          : "Salva interpretazione - " +
          configurazioneOracolo().costoPermanenza + " QI";
      }

      if (externalShareButton) {
        externalShareButton.disabled = false;
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

function initExternalShare(resultText, postDescription, shareButton) {
  var dialog = document.getElementById("condivisione-esterna-dialog");
  var closeButton = document.getElementById("chiudi-condivisione-esterna");
  var copyTextButton = document.getElementById("copia-testo-condivisione");
  var copyImageButton = document.getElementById("copia-immagine-condivisione");
  var previewDescription = document.getElementById("anteprima-descrizione-condivisione");
  var previewText = document.getElementById("anteprima-testo-condivisione");
  var previewUsername = document.getElementById("anteprima-username-condivisione");
  var previewAvatar = document.getElementById("anteprima-avatar-condivisione");
  var previewSigns = document.getElementById("anteprima-segni-condivisione");
  var status = document.getElementById("esito-condivisione-esterna");

  if (!dialog || !shareButton || !resultText || !postDescription) return;

  function getProfileData() {
    var sidebar = document.querySelector(".sidebar-user");
    var usernameElement = sidebar
      ? sidebar.querySelector(".username")
      : document.querySelector(".greeting strong:nth-of-type(2)");
    var avatarImage = sidebar ? sidebar.querySelector(".avatar img") : null;
    var signElement = sidebar ? sidebar.querySelector(".sign") : null;

    return {
      username: usernameElement
        ? usernameElement.textContent.trim().replace(/^@/, "")
        : "utente",
      avatarImage: avatarImage,
      signs: signElement ? signElement.textContent.replace(/\s+/g, " ").trim() : "Segno non impostato"
    };
  }

  function getShareText() {
    var description = postDescription.value.trim();
    var interpretation = resultText.innerText.trim();
    return [
      "SOGNO:\n" + (description || "Nessuna descrizione"),
      "INTERPRETAZIONE:\n" + interpretation
    ].join("\n\n");
  }

  function setStatus(message) {
    if (status) status.textContent = message;
  }

  shareButton.addEventListener("click", function () {
    var profile = getProfileData();
    var username = profile.username;
    var description = postDescription.value.trim();
    previewUsername.textContent = "@" + username;
    previewAvatar.replaceChildren();
    if (profile.avatarImage) {
      var avatarImage = profile.avatarImage.cloneNode(true);
      previewAvatar.appendChild(avatarImage);
    } else {
      previewAvatar.textContent = username.charAt(0).toUpperCase() || "U";
    }
    previewSigns.textContent = profile.signs;
    previewDescription.classList.toggle("is-empty", !description);
    previewDescription.innerHTML = description
      ? DOMPurify.sanitize(marked.parse(description))
      : "Aggiungi una descrizione nella schermata dell'Oracolo";
    previewText.innerHTML = resultText.innerHTML;
    setStatus("");
    dialog.showModal();
  });

  closeButton.addEventListener("click", function () { dialog.close(); });
  dialog.addEventListener("click", function (event) {
    if (event.target === dialog) dialog.close();
  });

  copyTextButton.addEventListener("click", async function () {
    try {
      await navigator.clipboard.writeText(getShareText());
      setStatus("Testo copiato negli appunti.");
    } catch (error) {
      setStatus("Impossibile copiare il testo in questo browser.");
    }
  });

  copyImageButton.addEventListener("click", async function () {
    var preview = document.getElementById("anteprima-condivisione-esterna");
    var canvas;
    try {
      canvas = await creaImmagineDaAnteprima(preview);
      var blob = await new Promise(function (resolve) {
        canvas.toBlob(resolve, "image/png");
      });
      await navigator.clipboard.write([
        new ClipboardItem({ "image/png": blob })
      ]);
      setStatus("Immagine copiata negli appunti.");
    } catch (error) {
      if (!canvas) {
        setStatus("Impossibile creare l'immagine del post in questo browser.");
        return;
      }
      var link = document.createElement("a");
      link.download = "somnia-zodiaca-post.png";
      link.href = canvas.toDataURL("image/png");
      link.click();
      setStatus("Il browser non permette la copia: immagine scaricata.");
    }
  });
}

async function creaImmagineDaAnteprima(preview) {
  if (!preview) {
    throw new Error("Anteprima non disponibile");
  }

  var rect = preview.getBoundingClientRect();
  var width = Math.ceil(rect.width);
  var height = Math.ceil(rect.height);
  var clone = preview.cloneNode(true);
  clone.removeAttribute("id");
  clone.style.width = width + "px";
  clone.style.height = height + "px";
  clone.style.maxWidth = "none";
  clone.style.maxHeight = "none";
  clone.style.margin = "0";

  var sourceImages = Array.from(preview.querySelectorAll("img"));
  var clonedImages = Array.from(clone.querySelectorAll("img"));
  await Promise.all(clonedImages.map(async function (image, index) {
    var sourceImage = sourceImages[index];
    var sourceUrl = sourceImage && (sourceImage.currentSrc || sourceImage.src);

    if (!sourceUrl || sourceUrl.startsWith("data:")) {
      return;
    }

    try {
      var response = await fetch(sourceUrl, { credentials: "include" });
      if (!response.ok) return;
      var blob = await response.blob();
      image.src = await new Promise(function (resolve, reject) {
        var reader = new FileReader();
        reader.onload = function () { resolve(reader.result); };
        reader.onerror = reject;
        reader.readAsDataURL(blob);
      });
    } catch (error) {
      image.src = sourceUrl;
    }
  }));

  function copyComputedStyles(source, target) {
    var computed = window.getComputedStyle(source);
    Array.from(computed).forEach(function (property) {
      target.style.setProperty(
        property,
        computed.getPropertyValue(property),
        computed.getPropertyPriority(property)
      );
    });

    Array.from(source.children).forEach(function (child, index) {
      if (target.children[index]) {
        copyComputedStyles(child, target.children[index]);
      }
    });
  }

  copyComputedStyles(preview, clone);
  clone.style.width = width + "px";
  clone.style.height = height + "px";
  clone.style.maxWidth = "none";
  clone.style.maxHeight = "none";
  clone.style.margin = "0";

  var imageInterpretation = clone.querySelector(
    ".external-share-interpretation"
  );
  if (imageInterpretation) {
    var interpretationText = imageInterpretation.innerText.trim();
    var maximumCharacters = 280;
    if (interpretationText.length > maximumCharacters) {
      interpretationText = interpretationText.slice(0, maximumCharacters);
      interpretationText = interpretationText.slice(
        0,
        interpretationText.lastIndexOf(" ")
      ).trim() + "...";
    }
    imageInterpretation.textContent = interpretationText;
    imageInterpretation.style.display = "block";
    imageInterpretation.style.webkitBoxOrient = "initial";
    imageInterpretation.style.webkitLineClamp = "none";
    imageInterpretation.style.overflow = "hidden";
    imageInterpretation.style.height = "auto";
    imageInterpretation.style.maxHeight = "none";
  }

  clone.style.height = "auto";
  var measuringWrapper = document.createElement("div");
  measuringWrapper.style.position = "absolute";
  measuringWrapper.style.left = "-10000px";
  measuringWrapper.style.top = "0";
  measuringWrapper.style.width = width + "px";
  measuringWrapper.style.visibility = "hidden";
  measuringWrapper.appendChild(clone);
  document.body.appendChild(measuringWrapper);

  var brand = clone.querySelector(".external-share-brand");
  if (brand) {
    var cloneRect = clone.getBoundingClientRect();
    var brandRect = brand.getBoundingClientRect();
    height = Math.ceil(brandRect.bottom - cloneRect.top + 20);
  }
  clone.style.height = height + "px";
  measuringWrapper.remove();

  var styles = "";
  Array.from(document.styleSheets).forEach(function (sheet) {
    try {
      styles += Array.from(sheet.cssRules).map(function (rule) {
        return rule.cssText;
      }).join("\n");
    } catch (error) {
      // I fogli esterni non accessibili non sono necessari per la card locale.
    }
  });

  var svg = [
    '<svg xmlns="http://www.w3.org/2000/svg" xmlns:xhtml="http://www.w3.org/1999/xhtml" width="',
    width,
    '" height="',
    height,
    '">',
    '<foreignObject width="100%" height="100%">',
    '<xhtml:div xmlns="http://www.w3.org/1999/xhtml" style="width:',
    width,
    'px;height:',
    height,
    'px;background:#171827;">',
    '<xhtml:style>',
    styles,
    '</xhtml:style>',
    new XMLSerializer().serializeToString(clone),
    '</xhtml:div></foreignObject></svg>'
  ].join("");

  var image = new Image();
  image.src = "data:image/svg+xml;charset=utf-8," + encodeURIComponent(svg);
  await new Promise(function (resolve, reject) {
    image.onload = resolve;
    image.onerror = reject;
  });

  var canvas = document.createElement("canvas");
  canvas.width = width;
  canvas.height = height;
  canvas.getContext("2d").drawImage(image, 0, 0, width, height);
  return canvas;
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
  var form = document.getElementById("oracolo-form");

  if (button) {
    var saldoInsufficiente = saldo && Number(saldo.textContent.trim()) <
      configurazioneOracolo().costoInterpretazione;
    var limiteRaggiunto = form && form.dataset.interpretazioniResidue === "0";
    button.disabled = Boolean(saldoInsufficiente || limiteRaggiunto);
  }
}

function aggiornaInterpretazioniResidue(form, residue) {
  var valore = Number(residue);

  if (!Number.isFinite(valore) || valore < 0) {
    return;
  }

  form.dataset.interpretazioniResidue = String(valore);

  var elemento = document.getElementById("interpretazioni-residue");
  if (elemento) {
    elemento.textContent = valore + " interpretazioni rimaste questa settimana";
  }

  aggiornaDisponibilitaInterpretazione();
}

/* ---------------------------------------------------------
  SOCIAL
--------------------------------------------------------- */
function initNatalMarkdown() {
  const elements = document.querySelectorAll(".natal-interpretation-text");

  if (elements.length === 0 ||
    typeof marked === "undefined" ||
    typeof DOMPurify === "undefined") {
    return;
  }

  elements.forEach(element => {
    const markdown = element.textContent.trim();

    element.innerHTML = DOMPurify.sanitize(
      marked.parse(markdown)
    );
  });
}

function initSocialMarkdown() {
  var markdownElements = document.querySelectorAll(".js-markdown-content");

  if (markdownElements.length === 0 ||
    typeof marked === "undefined" ||
    typeof DOMPurify === "undefined") {
    return;
  }

  markdownElements.forEach(function (element) {
    element.innerHTML = DOMPurify.sanitize(marked.parse(element.textContent));
  });
}

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
    form.addEventListener("submit", function (event) {
      var endpoint = form.dataset.commentEndpoint;
      if (!endpoint) return;

      event.preventDefault();
      var submitButton = form.querySelector("button[type='submit']");
      if (submitButton) submitButton.disabled = true;

      fetch(endpoint, {
        method: "POST",
        headers: { "X-Requested-With": "XMLHttpRequest" },
        body: new FormData(form)
      })
        .then(function (response) {
          if (!response.ok) throw new Error("Comment request failed");
          return response.json();
        })
        .then(function (data) {
          var panel = form.closest(".social-comments-panel");
          var list = panel && panel.querySelector(".social-comments-list");
          if (!list) return;
          var emptyMessage = list.querySelector(".social-comments-empty");
          if (emptyMessage) emptyMessage.remove();
          list.insertAdjacentHTML("beforeend",
            "<div class='social-comment'><strong>@" + escapeHtml(data.username) +
            "</strong><p>" + escapeHtml(data.testo) + "</p></div>");
          form.reset();
          var postId = form.dataset.postId;
          document.querySelectorAll("[data-comments-target]").forEach(function (button) {
            if (button.dataset.commentsTarget.endsWith("-" + postId)) {
              var count = button.querySelector("span:last-child");
              if (count) count.textContent = data.count;
            }
          });
        })
        .catch(function () { form.submit(); })
        .finally(function () {
          if (submitButton) submitButton.disabled = false;
        });
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

  var initialTarget = initialTargetId
    ? document.getElementById(initialTargetId)
    : null;
  var initialButton = initialTargetId
    ? document.querySelector(
      '[data-comments-target="' + initialTargetId + '"]'
    )
    : null;

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

function initSocialActions() {
  document.querySelectorAll("form[data-follow-action]").forEach(function (form) {
    form.addEventListener("submit", function (event) {
      event.preventDefault();
      var username = form.dataset.followUsername;
      var action = form.dataset.followAction;
      var endpoint = form.action + "/ajax";
      var button = form.querySelector("button");
      if (!username || !button) return;

      button.disabled = true;
      fetch(endpoint, {
        method: "POST",
        headers: { "X-Requested-With": "XMLHttpRequest" },
        body: new FormData(form)
      })
        .then(function (response) {
          if (!response.ok) throw new Error("Follow request failed");
          return response.json();
        })
        .then(function () {
          document.querySelectorAll("form[data-follow-username='" + username + "']").forEach(function (matchingForm) {
            var matchingButton = matchingForm.querySelector("button");
            if (!matchingButton) return;
            var following = action === "follow";
            matchingForm.dataset.followAction = following ? "unfollow" : "follow";
            matchingForm.action = matchingForm.action.replace(/\/(?:un)?follow(?:\/ajax)?$/, following ? "/unfollow" : "/follow");
            matchingButton.textContent = following ? "Smetti di seguire" : "Segui";
            matchingButton.classList.toggle("btn-primary", !following);
            matchingButton.classList.toggle("btn-ghost", following);
          });
        })
        .catch(function () { form.submit(); })
        .finally(function () { button.disabled = false; });
    });
  });
}

function escapeHtml(value) {
  var div = document.createElement("div");
  div.textContent = value;
  return div.innerHTML;
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
