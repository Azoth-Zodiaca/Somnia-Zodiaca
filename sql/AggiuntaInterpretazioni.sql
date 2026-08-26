USE somniazodiaca;

START TRANSACTION;

SELECT id INTO @admin_id
FROM utenti
WHERE username = 'admin'
LIMIT 1;

INSERT INTO sogni (
    utente_id,
    testo,
    created_at,
    updated_at
)
VALUES (
    @admin_id,
    'Ho sognato di camminare sotto un cielo pieno di stelle.',
    NOW(),
    NOW()
);

SET @sogno_id = LAST_INSERT_ID();

INSERT INTO interpretazioni (
    sogno_id,
    prompt,
    testo,
    umore,
    stile,
    scadenza_cache,
    created_at,
    updated_at
)
VALUES (
    @sogno_id,
    'Interpretazione junghiana del sogno',
    'Questo sogno può rappresentare il desiderio di esplorare nuove possibilità interiori.',
    'SERENO',
    'ASTROLOGICO',
    NOW() + INTERVAL 48 HOUR,
    NOW(),
    NOW()
);

COMMIT;

START TRANSACTION;

SELECT id INTO @admin_id
FROM utenti
WHERE username = 'admin'
LIMIT 1;

INSERT INTO sogni (
    utente_id,
    testo,
    created_at,
    updated_at
)
VALUES (
    @admin_id,
    'Ho sognato una casa luminosa vicino al mare.',
    NOW(),
    NOW()
);

SET @sogno_id = LAST_INSERT_ID();

INSERT INTO interpretazioni (
    sogno_id,
    prompt,
    testo,
    umore,
    stile,
    scadenza_cache,
    created_at,
    updated_at
)
VALUES (
    @sogno_id,
    'Interpretazione simbolica del sogno',
    'La casa può rappresentare la tua identità, mentre il mare richiama la dimensione emotiva.',
    'SERENO',
    'SIMBOLICO',
    NULL,
    NOW(),
    NOW()
);

COMMIT;

START TRANSACTION;

SELECT id INTO @admin_id
FROM utenti
WHERE username = 'admin'
LIMIT 1;

INSERT INTO sogni (
    utente_id,
    testo,
    created_at,
    updated_at
)
VALUES (
    @admin_id,
    'Ho sognato un orologio che correva velocissimo.',
    NOW(),
    NOW()
);

SET @sogno_id = LAST_INSERT_ID();

INSERT INTO interpretazioni (
    sogno_id,
    prompt,
    testo,
    umore,
    stile,
    scadenza_cache,
    created_at,
    updated_at
)
VALUES (
    @sogno_id,
    'Interpretazione junghiana del sogno',
    'Il tempo accelerato può indicare pressione, cambiamento o paura di perdere un’occasione.',
    'ANSIOSO',
    'JUNGHIANO',
    NOW() + INTERVAL 1 MINUTE,
    NOW(),
    NOW()
);

COMMIT;

START TRANSACTION;

SELECT id INTO @admin_id
FROM utenti
WHERE username = 'admin'
LIMIT 1;

INSERT INTO sogni (
    utente_id,
    testo,
    created_at,
    updated_at
)
VALUES (
    @admin_id,
    'Ho sognato una porta chiusa.',
    NOW(),
    NOW()
);

SET @sogno_id = LAST_INSERT_ID();

INSERT INTO interpretazioni (
    sogno_id,
    prompt,
    testo,
    umore,
    stile,
    scadenza_cache,
    created_at,
    updated_at
)
VALUES (
    @sogno_id,
    'Interpretazione junghiana del sogno',
    'La porta chiusa può simboleggiare un ostacolo o una possibilità ancora inesplorata.',
    'CONFUSO',
    'SIMBOLICO',
    NOW() - INTERVAL 1 MINUTE,
    NOW(),
    NOW()
);

COMMIT;

INSERT INTO sogni (utente_id, testo, created_at, updated_at)
SELECT u.id, dati.testo, NOW(), NOW()
FROM utenti u
JOIN (
    SELECT 'alice_moretti' AS username, 'Ho sognato un sentiero tra le colline.' AS testo
    UNION ALL SELECT 'davide_esposito', 'Ho sognato un libro con pagine bianche.'
    UNION ALL SELECT 'sara_greco', 'Ho sognato una luce accesa nella notte.'
    UNION ALL SELECT 'matteo_russo', 'Ho sognato una strada che portava lontano.'
    UNION ALL SELECT 'noemi_galli', 'Ho sognato un laboratorio pieno di colori.'
) dati ON dati.username = u.username
WHERE NOT EXISTS (
    SELECT 1 FROM sogni s
    WHERE s.utente_id = u.id AND s.testo = dati.testo
);

INSERT INTO interpretazioni (
    sogno_id, prompt, testo, umore, stile, scadenza_cache, created_at, updated_at
)
SELECT s.id, 'Interpretazione simbolica del sogno',
    CASE u.username
        WHEN 'alice_moretti' THEN 'Il sentiero richiama un percorso paziente verso un obiettivo concreto.'
        WHEN 'davide_esposito' THEN 'Le pagine bianche rappresentano curiosita e nuove idee ancora da sviluppare.'
        WHEN 'sara_greco' THEN 'La luce nella notte suggerisce conforto e una risorsa interiore presente.'
        WHEN 'matteo_russo' THEN 'La strada simboleggia apertura al cambiamento e desiderio di scoperta.'
        ELSE 'I colori richiamano attenzione, creativita e ordine da portare nella vita quotidiana.'
    END,
    'SERENO', 'SIMBOLICO', NULL, NOW(), NOW()
FROM sogni s
JOIN utenti u ON u.id = s.utente_id
WHERE s.testo IN (
    'Ho sognato un sentiero tra le colline.',
    'Ho sognato un libro con pagine bianche.',
    'Ho sognato una luce accesa nella notte.',
    'Ho sognato una strada che portava lontano.',
    'Ho sognato un laboratorio pieno di colori.'
)
AND NOT EXISTS (
    SELECT 1 FROM interpretazioni i WHERE i.sogno_id = s.id
);

SELECT
    i.id,
    s.testo AS sogno,
    i.scadenza_cache,
    CASE
        WHEN i.scadenza_cache IS NULL THEN 'PERMANENTE'
        WHEN i.scadenza_cache > NOW() THEN 'TEMPORANEA'
        ELSE 'SCADUTA'
    END AS stato
FROM interpretazioni i
JOIN sogni s ON s.id = i.sogno_id
JOIN utenti u ON u.id = s.utente_id
WHERE u.username = 'admin'
ORDER BY i.id DESC;