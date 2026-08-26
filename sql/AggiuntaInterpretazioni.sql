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