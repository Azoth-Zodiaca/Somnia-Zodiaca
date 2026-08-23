USE somniazodiaca;

START TRANSACTION;

-- Recupera l'hash dell'admin per permettere il login agli utenti demo
SELECT password_hash INTO @password_demo
FROM utenti
WHERE
    username = 'admin'
LIMIT 1;

-- Recupera i segni zodiacali
SELECT id INTO @segno_ariete
FROM segni_zodiacali
WHERE
    nome = 'ARIETE'
LIMIT 1;

SELECT id INTO @segno_pesci
FROM segni_zodiacali
WHERE
    nome = 'PESCI'
LIMIT 1;

SELECT id INTO @segno_acquario
FROM segni_zodiacali
WHERE
    nome = 'ACQUARIO'
LIMIT 1;

SELECT id INTO @segno_leone
FROM segni_zodiacali
WHERE
    nome = 'LEONE'
LIMIT 1;

-- Crea utenti demo se non esistono
INSERT INTO
    utenti (
        username,
        email,
        password_hash,
        ruolo,
        qi,
        giorni_consecutivi,
        profilo_colore,
        segno_zodiacale_id,
        ascendente_id,
        created_at,
        updated_at
    )
SELECT
    'sofia_pesci',
    'sofia.pesci@demo.it',
    @password_demo,
    'BASE',
    500,
    3,
    '#8B5CF6',
    @segno_pesci,
    @segno_acquario,
    NOW(),
    NOW()
WHERE
    NOT EXISTS (
        SELECT 1
        FROM utenti
        WHERE
            username = 'sofia_pesci'
    );

INSERT INTO
    utenti (
        username,
        email,
        password_hash,
        ruolo,
        qi,
        giorni_consecutivi,
        profilo_colore,
        segno_zodiacale_id,
        ascendente_id,
        created_at,
        updated_at
    )
SELECT
    'marco_ariete',
    'marco.ariete@demo.it',
    @password_demo,
    'BASE',
    350,
    5,
    '#F97316',
    @segno_ariete,
    @segno_pesci,
    NOW(),
    NOW()
WHERE
    NOT EXISTS (
        SELECT 1
        FROM utenti
        WHERE
            username = 'marco_ariete'
    );

INSERT INTO
    utenti (
        username,
        email,
        password_hash,
        ruolo,
        qi,
        giorni_consecutivi,
        profilo_colore,
        segno_zodiacale_id,
        ascendente_id,
        created_at,
        updated_at
    )
SELECT
    'ely_acquario',
    'ely.acquario@demo.it',
    @password_demo,
    'BASE',
    720,
    8,
    '#38BDF8',
    @segno_acquario,
    @segno_ariete,
    NOW(),
    NOW()
WHERE
    NOT EXISTS (
        SELECT 1
        FROM utenti
        WHERE
            username = 'ely_acquario'
    );

INSERT INTO
    utenti (
        username,
        email,
        password_hash,
        ruolo,
        qi,
        giorni_consecutivi,
        profilo_colore,
        segno_zodiacale_id,
        ascendente_id,
        created_at,
        updated_at
    )
SELECT
    'luca_leone',
    'luca.leone@demo.it',
    @password_demo,
    'BASE',
    640,
    6,
    '#F5B942',
    @segno_leone,
    @segno_ariete,
    NOW(),
    NOW()
WHERE
    NOT EXISTS (
        SELECT 1
        FROM utenti
        WHERE
            username = 'luca_leone'
    );

INSERT INTO
    utenti (
        username,
        email,
        password_hash,
        ruolo,
        qi,
        giorni_consecutivi,
        profilo_colore,
        segno_zodiacale_id,
        ascendente_id,
        created_at,
        updated_at
    )
SELECT
    'giulia_leone',
    'giulia.leone@demo.it',
    @password_demo,
    'BASE',
    480,
    4,
    '#EC4899',
    @segno_leone,
    @segno_acquario,
    NOW(),
    NOW()
WHERE
    NOT EXISTS (
        SELECT 1
        FROM utenti
        WHERE
            username = 'giulia_leone'
    );

-- Recupera gli ID degli utenti
SELECT id INTO @sofia_id
FROM utenti
WHERE
    username = 'sofia_pesci'
LIMIT 1;

SELECT id INTO @marco_id
FROM utenti
WHERE
    username = 'marco_ariete'
LIMIT 1;

SELECT id INTO @ely_id
FROM utenti
WHERE
    username = 'ely_acquario'
LIMIT 1;

SELECT id INTO @luca_id
FROM utenti
WHERE
    username = 'luca_leone'
LIMIT 1;

SELECT id INTO @giulia_id
FROM utenti
WHERE
    username = 'giulia_leone'
LIMIT 1;

-- Sogno e interpretazione di Sofia
INSERT INTO
    sogni (
        utente_id,
        testo,
        created_at,
        updated_at
    )
SELECT @sofia_id, 'Ero in una stanza piena di specchi. Ogni riflesso mostrava una versione diversa di me.', NOW(), NOW()
WHERE
    NOT EXISTS (
        SELECT 1
        FROM sogni
        WHERE
            utente_id = @sofia_id
            AND testo LIKE 'Ero in una stanza piena di specchi%'
    );

SELECT id INTO @sogno_sofia_id
FROM sogni
WHERE
    utente_id = @sofia_id
    AND testo LIKE 'Ero in una stanza piena di specchi%'
ORDER BY id DESC
LIMIT 1;

INSERT INTO
    interpretazioni (
        sogno_id,
        prompt,
        testo,
        tipo,
        scadenza_cache,
        created_at,
        updated_at
    )
SELECT @sogno_sofia_id, 'Interpretazione simbolica del sogno degli specchi', 'Gli specchi possono rappresentare il rapporto con le diverse parti della propria identita e con i cambiamenti ancora possibili.', 'JUNGIANA', NULL, NOW(), NOW()
WHERE
    NOT EXISTS (
        SELECT 1
        FROM interpretazioni
        WHERE
            sogno_id = @sogno_sofia_id
    );

SELECT id INTO @interpretazione_sofia_id
FROM interpretazioni
WHERE
    sogno_id = @sogno_sofia_id
ORDER BY id DESC
LIMIT 1;

-- Sogno e interpretazione di Marco
INSERT INTO
    sogni (
        utente_id,
        testo,
        created_at,
        updated_at
    )
SELECT @marco_id, 'Camminavo su un ponte che si costruiva mentre avanzavo.', NOW(), NOW()
WHERE
    NOT EXISTS (
        SELECT 1
        FROM sogni
        WHERE
            utente_id = @marco_id
            AND testo LIKE 'Camminavo su un ponte%'
    );

SELECT id INTO @sogno_marco_id
FROM sogni
WHERE
    utente_id = @marco_id
    AND testo LIKE 'Camminavo su un ponte%'
ORDER BY id DESC
LIMIT 1;

INSERT INTO
    interpretazioni (
        sogno_id,
        prompt,
        testo,
        tipo,
        scadenza_cache,
        created_at,
        updated_at
    )
SELECT @sogno_marco_id, 'Interpretazione simbolica del sogno del ponte', 'Il ponte che appare durante il cammino suggerisce fiducia nel processo e disponibilita ad affrontare una nuova fase.', 'COGNITIVA', NULL, NOW(), NOW()
WHERE
    NOT EXISTS (
        SELECT 1
        FROM interpretazioni
        WHERE
            sogno_id = @sogno_marco_id
    );

SELECT id INTO @interpretazione_marco_id
FROM interpretazioni
WHERE
    sogno_id = @sogno_marco_id
ORDER BY id DESC
LIMIT 1;

-- Sogno e interpretazione di Ely
INSERT INTO
    sogni (
        utente_id,
        testo,
        created_at,
        updated_at
    )
SELECT @ely_id, 'Un orchestra suonava senza direttore e nessuno era stonato.', NOW(), NOW()
WHERE
    NOT EXISTS (
        SELECT 1
        FROM sogni
        WHERE
            utente_id = @ely_id
            AND testo LIKE 'Un orchestra suonava%'
    );

SELECT id INTO @sogno_ely_id
FROM sogni
WHERE
    utente_id = @ely_id
    AND testo LIKE 'Un orchestra suonava%'
ORDER BY id DESC
LIMIT 1;

INSERT INTO
    interpretazioni (
        sogno_id,
        prompt,
        testo,
        tipo,
        scadenza_cache,
        created_at,
        updated_at
    )
SELECT @sogno_ely_id, 'Interpretazione simbolica del sogno dell orchestra', 'L orchestra senza direttore puo indicare collaborazione spontanea, sintonia e capacita di contribuire a un obiettivo comune.', 'FOLKLORISTICA', NULL, NOW(), NOW()
WHERE
    NOT EXISTS (
        SELECT 1
        FROM interpretazioni
        WHERE
            sogno_id = @sogno_ely_id
    );

SELECT id INTO @interpretazione_ely_id
FROM interpretazioni
WHERE
    sogno_id = @sogno_ely_id
ORDER BY id DESC
LIMIT 1;

INSERT INTO
    sogni (
        utente_id,
        testo,
        created_at,
        updated_at
    )
SELECT @luca_id, 'Ho sognato di correre verso una montagna illuminata dal sole.', NOW(), NOW()
WHERE
    NOT EXISTS (
        SELECT 1
        FROM sogni
        WHERE
            utente_id = @luca_id
            AND testo LIKE 'Ho sognato di correre verso una montagna%'
    );

SELECT id INTO @sogno_luca_id
FROM sogni
WHERE
    utente_id = @luca_id
    AND testo LIKE 'Ho sognato di correre verso una montagna%'
ORDER BY id DESC
LIMIT 1;

INSERT INTO
    interpretazioni (
        sogno_id,
        prompt,
        testo,
        tipo,
        scadenza_cache,
        created_at,
        updated_at
    )
SELECT @sogno_luca_id, 'Interpretazione simbolica del sogno della montagna', 'La montagna illuminata puo rappresentare ambizione, energia e il desiderio di raggiungere un obiettivo importante.', 'JUNGIANA', NULL, NOW(), NOW()
WHERE
    NOT EXISTS (
        SELECT 1
        FROM interpretazioni
        WHERE
            sogno_id = @sogno_luca_id
    );

SELECT id INTO @interpretazione_luca_id
FROM interpretazioni
WHERE
    sogno_id = @sogno_luca_id
ORDER BY id DESC
LIMIT 1;

INSERT INTO
    sogni (
        utente_id,
        testo,
        created_at,
        updated_at
    )
SELECT @giulia_id, 'Ero su un palco davanti a molte persone, ma non avevo paura.', NOW(), NOW()
WHERE
    NOT EXISTS (
        SELECT 1
        FROM sogni
        WHERE
            utente_id = @giulia_id
            AND testo LIKE 'Ero su un palco davanti a molte persone%'
    );

SELECT id INTO @sogno_giulia_id
FROM sogni
WHERE
    utente_id = @giulia_id
    AND testo LIKE 'Ero su un palco davanti a molte persone%'
ORDER BY id DESC
LIMIT 1;

INSERT INTO
    interpretazioni (
        sogno_id,
        prompt,
        testo,
        tipo,
        scadenza_cache,
        created_at,
        updated_at
    )
SELECT @sogno_giulia_id, 'Interpretazione simbolica del sogno del palco', 'Il palco puo indicare il bisogno di esprimersi, mostrarsi agli altri e riconoscere il proprio valore.', 'COGNITIVA', NULL, NOW(), NOW()
WHERE
    NOT EXISTS (
        SELECT 1
        FROM interpretazioni
        WHERE
            sogno_id = @sogno_giulia_id
    );

SELECT id INTO @interpretazione_giulia_id
FROM interpretazioni
WHERE
    sogno_id = @sogno_giulia_id
ORDER BY id DESC
LIMIT 1;

-- Inserisce otto post dimostrativi
INSERT INTO
    post (
        utente_id,
        interpretazione_id,
        testo_visibile,
        data_pubblicazione,
        numero_like,
        created_at,
        updated_at
    )
SELECT
    @sofia_id,
    @interpretazione_sofia_id,
    'Non riesco a smettere di pensare a questo sogno.',
    NOW() - INTERVAL 2 HOUR,
    12,
    NOW() - INTERVAL 2 HOUR,
    NOW() - INTERVAL 2 HOUR
WHERE
    NOT EXISTS (
        SELECT 1
        FROM post
        WHERE
            testo_visibile = 'Non riesco a smettere di pensare a questo sogno.'
    );

INSERT INTO
    post (
        utente_id,
        interpretazione_id,
        testo_visibile,
        data_pubblicazione,
        numero_like,
        created_at,
        updated_at
    )
SELECT
    @marco_id,
    @interpretazione_marco_id,
    'Questa immagine mi ha lasciato una sensazione di fiducia.',
    NOW() - INTERVAL 5 HOUR,
    8,
    NOW() - INTERVAL 5 HOUR,
    NOW() - INTERVAL 5 HOUR
WHERE
    NOT EXISTS (
        SELECT 1
        FROM post
        WHERE
            testo_visibile = 'Questa immagine mi ha lasciato una sensazione di fiducia.'
    );

INSERT INTO
    post (
        utente_id,
        interpretazione_id,
        testo_visibile,
        data_pubblicazione,
        numero_like,
        created_at,
        updated_at
    )
SELECT
    @ely_id,
    @interpretazione_ely_id,
    'Un sogno sulla collaborazione e sulla sintonia.',
    NOW() - INTERVAL 1 DAY,
    15,
    NOW() - INTERVAL 1 DAY,
    NOW() - INTERVAL 1 DAY
WHERE
    NOT EXISTS (
        SELECT 1
        FROM post
        WHERE
            testo_visibile = 'Un sogno sulla collaborazione e sulla sintonia.'
    );

INSERT INTO
    post (
        utente_id,
        interpretazione_id,
        testo_visibile,
        data_pubblicazione,
        numero_like,
        created_at,
        updated_at
    )
SELECT
    @sofia_id,
    @interpretazione_sofia_id,
    'Forse cambiamo continuamente, anche quando non ce ne accorgiamo.',
    NOW() - INTERVAL 2 DAY,
    21,
    NOW() - INTERVAL 2 DAY,
    NOW() - INTERVAL 2 DAY
WHERE
    NOT EXISTS (
        SELECT 1
        FROM post
        WHERE
            testo_visibile = 'Forse cambiamo continuamente, anche quando non ce ne accorgiamo.'
    );

INSERT INTO
    post (
        utente_id,
        interpretazione_id,
        testo_visibile,
        data_pubblicazione,
        numero_like,
        created_at,
        updated_at
    )
SELECT
    @marco_id,
    @interpretazione_marco_id,
    'Il ponte sembrava fragile, ma continuava a comparire sotto i miei piedi.',
    NOW() - INTERVAL 3 DAY,
    6,
    NOW() - INTERVAL 3 DAY,
    NOW() - INTERVAL 3 DAY
WHERE
    NOT EXISTS (
        SELECT 1
        FROM post
        WHERE
            testo_visibile = 'Il ponte sembrava fragile, ma continuava a comparire sotto i miei piedi.'
    );

INSERT INTO
    post (
        utente_id,
        interpretazione_id,
        testo_visibile,
        data_pubblicazione,
        numero_like,
        created_at,
        updated_at
    )
SELECT
    @ely_id,
    @interpretazione_ely_id,
    'Mi e sembrato un sogno molto positivo.',
    NOW() - INTERVAL 4 DAY,
    10,
    NOW() - INTERVAL 4 DAY,
    NOW() - INTERVAL 4 DAY
WHERE
    NOT EXISTS (
        SELECT 1
        FROM post
        WHERE
            testo_visibile = 'Mi e sembrato un sogno molto positivo.'
    );

INSERT INTO
    post (
        utente_id,
        interpretazione_id,
        testo_visibile,
        data_pubblicazione,
        numero_like,
        created_at,
        updated_at
    )
SELECT
    @sofia_id,
    @interpretazione_sofia_id,
    'Avete mai sognato di incontrare una versione diversa di voi?',
    NOW() - INTERVAL 6 DAY,
    18,
    NOW() - INTERVAL 6 DAY,
    NOW() - INTERVAL 6 DAY
WHERE
    NOT EXISTS (
        SELECT 1
        FROM post
        WHERE
            testo_visibile = 'Avete mai sognato di incontrare una versione diversa di voi?'
    );

INSERT INTO
    post (
        utente_id,
        interpretazione_id,
        testo_visibile,
        data_pubblicazione,
        numero_like,
        created_at,
        updated_at
    )
SELECT
    @marco_id,
    @interpretazione_marco_id,
    'A volte il percorso si chiarisce soltanto facendo il primo passo.',
    NOW() - INTERVAL 8 DAY,
    4,
    NOW() - INTERVAL 8 DAY,
    NOW() - INTERVAL 8 DAY
WHERE
    NOT EXISTS (
        SELECT 1
        FROM post
        WHERE
            testo_visibile = 'A volte il percorso si chiarisce soltanto facendo il primo passo.'
    );

INSERT INTO
    post (
        utente_id,
        interpretazione_id,
        testo_visibile,
        data_pubblicazione,
        numero_like,
        created_at,
        updated_at
    )
SELECT
    @luca_id,
    @interpretazione_luca_id,
    'Mi sono svegliato con una grande energia addosso.',
    NOW() - INTERVAL 3 HOUR,
    14,
    NOW() - INTERVAL 3 HOUR,
    NOW() - INTERVAL 3 HOUR
WHERE
    NOT EXISTS (
        SELECT 1
        FROM post
        WHERE
            testo_visibile = 'Mi sono svegliato con una grande energia addosso.'
    );

INSERT INTO
    post (
        utente_id,
        interpretazione_id,
        testo_visibile,
        data_pubblicazione,
        numero_like,
        created_at,
        updated_at
    )
SELECT
    @luca_id,
    @interpretazione_luca_id,
    'Forse il sogno mi sta ricordando di puntare piu in alto.',
    NOW() - INTERVAL 2 DAY,
    22,
    NOW() - INTERVAL 2 DAY,
    NOW() - INTERVAL 2 DAY
WHERE
    NOT EXISTS (
        SELECT 1
        FROM post
        WHERE
            testo_visibile = 'Forse il sogno mi sta ricordando di puntare piu in alto.'
    );

INSERT INTO
    post (
        utente_id,
        interpretazione_id,
        testo_visibile,
        data_pubblicazione,
        numero_like,
        created_at,
        updated_at
    )
SELECT
    @giulia_id,
    @interpretazione_giulia_id,
    'Non pensavo di sentirmi cosi sicura nel sogno.',
    NOW() - INTERVAL 7 HOUR,
    17,
    NOW() - INTERVAL 7 HOUR,
    NOW() - INTERVAL 7 HOUR
WHERE
    NOT EXISTS (
        SELECT 1
        FROM post
        WHERE
            testo_visibile = 'Non pensavo di sentirmi cosi sicura nel sogno.'
    );

INSERT INTO
    post (
        utente_id,
        interpretazione_id,
        testo_visibile,
        data_pubblicazione,
        numero_like,
        created_at,
        updated_at
    )
SELECT
    @giulia_id,
    @interpretazione_giulia_id,
    'A volte bisogna solo avere il coraggio di salire sul palco.',
    NOW() - INTERVAL 4 DAY,
    9,
    NOW() - INTERVAL 4 DAY,
    NOW() - INTERVAL 4 DAY
WHERE
    NOT EXISTS (
        SELECT 1
        FROM post
        WHERE
            testo_visibile = 'A volte bisogna solo avere il coraggio di salire sul palco.'
    );

COMMIT;

-- Controllo finale
SELECT p.id, u.username, sz.nome AS segno, p.testo_visibile, p.numero_like, p.data_pubblicazione
FROM
    post p
    JOIN utenti u ON u.id = p.utente_id
    LEFT JOIN segni_zodiacali sz ON sz.id = u.segno_zodiacale_id
WHERE
    u.username IN (
        'sofia_pesci',
        'marco_ariete',
        'ely_acquario',
        'luca_leone',
        'giulia_leone'
    )
ORDER BY p.data_pubblicazione DESC;