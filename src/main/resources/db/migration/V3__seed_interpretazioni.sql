USE somniazodiaca;

START TRANSACTION;

SELECT id INTO @admin_id
FROM utenti
WHERE
    username = 'admin'
LIMIT 1;

INSERT INTO
    sogni (
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

INSERT INTO
    interpretazioni (
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
        'Sognare di camminare sotto un manto di stelle è un\'esperienza dal profondo valore cosmico. La sensazione di serenità che hai vissuto è la chiave di lettura principale: riflette un momento di meraviglioso allineamento interiore, come se i "pianeti" della tua vita emotiva stessero trovando un\'armonia perfetta.

Simbolicamente, le stelle sono da sempre i luminari che orientano i viaggiatori: rappresentano le tue speranze più pure, le tue intuizioni e una guida superiore. Il fatto che tu stessi camminando con calma indica una fiducia tranquilla nel tuo percorso di crescita. È un dolce richiamo della tua anima a riconoscere che possiedi già la luce necessaria per orientarti: la tua "mappa celeste" interna è chiara e luminosa.

Da una prospettiva astrologica, questo sogno evoca le energie benefiche di Giove e Venere: portatori di pace, protezione ed espansione della coscienza. Piuttosto che un presagio del futuro, il sogno è uno splendido specchio del tuo presente: un invito a fidarti dell\'Universo e del tuo cammino.',
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
WHERE
    username = 'admin'
LIMIT 1;

INSERT INTO
    sogni (
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

INSERT INTO
    interpretazioni (
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
        'Un sogno meraviglioso, che sembra donarti una piacevole ventata di armonia e di respiro profondo.

Nel linguaggio dei simboli, la casa rappresenta il tuo "Io", la tua interiorità e il tuo spazio di sicurezza. Vederla luminosa evoca un senso di grande chiarezza, consapevolezza e speranza: potrebbe indicare che stai attraversando, o desiderando, un momento di ritrovata pace mentale e apertura verso la vita.

Il mare, immutabile e profondo, è la metafora dell\'inconscio, delle emozioni e della forza vitale. Il fatto che la tua casa sia vicino al mare suggerisce un dialogo sano e armonioso tra la tua parte razionale (la struttura della casa) e il tuo mondo emotivo (l\'acqua). Non ti senti travolto dalle tue emozioni, ma ne accogli la vicinanza con fiducia.

La serenità che hai avvertito è il dono più prezioso di questa visione: non è una previsione del futuro, ma un riflesso del tuo stato d\'animo attuale o il ricordo di quanto possa essere bello abitare, con grazia, la tua luce interiore.',
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
WHERE
    username = 'admin'
LIMIT 1;

INSERT INTO
    sogni (
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

INSERT INTO
    interpretazioni (
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
        'Accogliere questo sogno significa mettersi in ascolto di un messaggio profondo del tuo inconscio. L\'ansia che hai provato non è un presagio negativo, ma una bussola emotiva che merita attenzione.

Nella prospettiva junghiana, l\'orologio simboleggia il tempo del Conscio (Chronos): le scadenze, il controllo dell\'Ego e le pressioni del mondo esterno. Un orologio che corre a velocità impazzita riflette spesso una frattura tra i ritmi frenetici del tuo Io e i bisogni più autentici della tua Anima.

Forse stai attraversando un momento in cui ti senti sopraffatto, con la costante sensazione di "rincorrere" qualcosa o di non avere abbastanza tempo per realizzare ciò che desideri. L\'inconscio rende visibile questa tua urgenza interiore.

Questo sogno non predice il futuro, ma ti offre uno specchio: ti invita a rallentare. È una chiamata a passare dal tempo dell\'orologio al tempo dell\'Anima (Kairos), ritrovando il tuo spazio per respirare e riprendere il controllo della tua vita.',
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
WHERE
    username = 'admin'
LIMIT 1;

INSERT INTO
    sogni (
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

INSERT INTO
    interpretazioni (
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
        'Sognare una porta chiusa è un\'esperienza evocativa, e la confusione che hai provato è una reazione del tutto naturale di fronte a un simbolo così potente.

In chiave simbolica, la porta rappresenta un confine, una soglia tra il noto e l\'ignoto, tra il tuo presente e una nuova possibilità. Una porta chiusa non indica necessariamente un blocco negativo, ma piuttosto un invito alla pausa e alla riflessione.

La confusione che hai avvertito potrebbe rispecchiare un momento della tua vita in cui ti senti davanti a un\'incertezza o a un capitolo non ancora del tutto chiaro. È come se il tuo inconscio stesse esplorando un limite: ti chiedi cosa ci sia "oltre", se quell\'accesso vada forzato, se manchi una chiave o se semplicemente non sia ancora arrivato il momento giusto per varcare quel passaggio.

Ascolta questo sogno con gentilezza: le porte chiuse ci ricordano che il nostro percorso ha i suoi tempi e che a volte fermarsi sulla soglia serve a proteggerci o a prepararci meglio a ciò che verrà.',
        'CONFUSO',
        'SIMBOLICO',
        NOW() - INTERVAL 1 MINUTE,
        NOW(),
        NOW()
    );

COMMIT;

INSERT INTO
    sogni (
        utente_id,
        testo,
        created_at,
        updated_at
    )
SELECT u.id, dati.testo, NOW(), NOW()
FROM utenti u
    JOIN (
        SELECT 'alice_moretti' AS username, 'Ho sognato un sentiero tra le colline.' AS testo
        UNION ALL
        SELECT 'davide_esposito', 'Ho sognato un libro con pagine bianche.'
        UNION ALL
        SELECT 'sara_greco', 'Ho sognato una luce accesa nella notte.'
        UNION ALL
        SELECT 'matteo_russo', 'Ho sognato una strada che portava lontano.'
        UNION ALL
        SELECT 'noemi_galli', 'Ho sognato un laboratorio pieno di colori.'
    ) dati ON dati.username = u.username
WHERE
    NOT EXISTS (
        SELECT 1
        FROM sogni s
        WHERE
            s.utente_id = u.id
            AND s.testo = dati.testo
    );

INSERT INTO
    interpretazioni (
        sogno_id,
        prompt,
        testo,
        umore,
        stile,
        scadenza_cache,
        created_at,
        updated_at
    )
SELECT
    s.id,
    'Interpretazione simbolica del sogno',
    CASE u.username
        WHEN 'alice_moretti' THEN 'Il sentiero richiama un percorso paziente verso un obiettivo concreto.'
        WHEN 'davide_esposito' THEN 'Le pagine bianche rappresentano curiosita e nuove idee ancora da sviluppare.'
        WHEN 'sara_greco' THEN 'La luce nella notte suggerisce conforto e una risorsa interiore presente.'
        WHEN 'matteo_russo' THEN 'La strada simboleggia apertura al cambiamento e desiderio di scoperta.'
        ELSE 'I colori richiamano attenzione, creativita e ordine da portare nella vita quotidiana.'
    END,
    'SERENO',
    'SIMBOLICO',
    NULL,
    NOW(),
    NOW()
FROM sogni s
    JOIN utenti u ON u.id = s.utente_id
WHERE
    s.testo IN (
        'Ho sognato un sentiero tra le colline.',
        'Ho sognato un libro con pagine bianche.',
        'Ho sognato una luce accesa nella notte.',
        'Ho sognato una strada che portava lontano.',
        'Ho sognato un laboratorio pieno di colori.'
    )
    AND NOT EXISTS (
        SELECT 1
        FROM interpretazioni i
        WHERE
            i.sogno_id = s.id
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
FROM
    interpretazioni i
    JOIN sogni s ON s.id = i.sogno_id
    JOIN utenti u ON u.id = s.utente_id
WHERE
    u.username = 'admin'
ORDER BY i.id DESC;