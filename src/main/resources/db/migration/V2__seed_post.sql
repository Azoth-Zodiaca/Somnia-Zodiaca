USE somniazodiaca;

START TRANSACTION;

-- Recupera l'hash dell'admin per permettere il login agli utenti demo
SELECT password_hash INTO @password_demo
FROM utenti
WHERE
    username = 'admin'
LIMIT 1;

-- Password demo: "Password123!" (hash bcrypt, valido per tutti gli utenti demo)
SET @password_demo = '$2b$10$KM5dc/e4KR8SeI/bPOKfx.ltVAF2YyaRQgsv1/82M.WvdSfplY37.';

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

SELECT id INTO @segno_toro
FROM segni_zodiacali
WHERE
    nome = 'TORO'
LIMIT 1;

SELECT id INTO @segno_cancro
FROM segni_zodiacali
WHERE
    nome = 'CANCRO'
LIMIT 1;

SELECT id INTO @segno_gemelli
FROM segni_zodiacali
WHERE
    nome = 'GEMELLI'
LIMIT 1;

SELECT id INTO @segno_vergine
FROM segni_zodiacali
WHERE
    nome = 'VERGINE'
LIMIT 1;

SELECT id INTO @segno_sagittario
FROM segni_zodiacali
WHERE
    nome = 'SAGITTARIO'
LIMIT 1;

SELECT id INTO @segno_bilancia
FROM segni_zodiacali
WHERE
    nome = 'BILANCIA'
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
        giorni_ricompensa_giornaliera,
        segno_zodiacale_id,
        ascendente_id,
        created_at,
        updated_at
    )
SELECT
    'sofia_rossi',
    'sofia.rossi@demo.it',
    @password_demo,
    'BASE',
    500,
    3,
    0,
    @segno_pesci,
    @segno_acquario,
    NOW(),
    NOW()
WHERE
    NOT EXISTS (
        SELECT 1
        FROM utenti
        WHERE
            username = 'sofia_rossi'
    );

INSERT INTO
    utenti (
        username,
        email,
        password_hash,
        ruolo,
        qi,
        giorni_consecutivi,
        giorni_ricompensa_giornaliera,
        segno_zodiacale_id,
        ascendente_id,
        created_at,
        updated_at
    )
SELECT
    'marco_bianchi',
    'marco.bianchi@demo.it',
    @password_demo,
    'BASE',
    350,
    5,
    0,
    @segno_ariete,
    @segno_pesci,
    NOW(),
    NOW()
WHERE
    NOT EXISTS (
        SELECT 1
        FROM utenti
        WHERE
            username = 'marco_bianchi'
    );

INSERT INTO
    utenti (
        username,
        email,
        password_hash,
        ruolo,
        qi,
        giorni_consecutivi,
        giorni_ricompensa_giornaliera,
        segno_zodiacale_id,
        ascendente_id,
        created_at,
        updated_at
    )
SELECT
    'elisa_romano',
    'elisa.romano@demo.it',
    @password_demo,
    'BASE',
    720,
    8,
    0,
    @segno_acquario,
    @segno_ariete,
    NOW(),
    NOW()
WHERE
    NOT EXISTS (
        SELECT 1
        FROM utenti
        WHERE
            username = 'elisa_romano'
    );

INSERT INTO
    utenti (
        username,
        email,
        password_hash,
        ruolo,
        qi,
        giorni_consecutivi,
        giorni_ricompensa_giornaliera,
        segno_zodiacale_id,
        ascendente_id,
        created_at,
        updated_at
    )
SELECT
    'luca_ferrari',
    'luca.ferrari@demo.it',
    @password_demo,
    'BASE',
    640,
    6,
    0,
    @segno_leone,
    @segno_ariete,
    NOW(),
    NOW()
WHERE
    NOT EXISTS (
        SELECT 1
        FROM utenti
        WHERE
            username = 'luca_ferrari'
    );

INSERT INTO
    utenti (
        username,
        email,
        password_hash,
        ruolo,
        qi,
        giorni_consecutivi,
        giorni_ricompensa_giornaliera,
        segno_zodiacale_id,
        ascendente_id,
        created_at,
        updated_at
    )
SELECT
    'giulia_conti',
    'giulia.conti@demo.it',
    @password_demo,
    'PREMIUM',
    480,
    4,
    0,
    @segno_leone,
    @segno_acquario,
    NOW(),
    NOW()
WHERE
    NOT EXISTS (
        SELECT 1
        FROM utenti
        WHERE
            username = 'giulia_conti'
    );

INSERT INTO
    utenti (
        username,
        email,
        password_hash,
        ruolo,
        qi,
        giorni_consecutivi,
        giorni_ricompensa_giornaliera,
        segno_zodiacale_id,
        ascendente_id,
        created_at,
        updated_at
    )
SELECT
    'alice_moretti',
    'alice.moretti@demo.it',
    @password_demo,
    'PREMIUM',
    810,
    9,
    9,
    @segno_toro,
    @segno_cancro,
    NOW(),
    NOW()
WHERE
    NOT EXISTS (
        SELECT 1
        FROM utenti
        WHERE
            username = 'alice_moretti'
    );

INSERT INTO
    utenti (
        username,
        email,
        password_hash,
        ruolo,
        qi,
        giorni_consecutivi,
        giorni_ricompensa_giornaliera,
        segno_zodiacale_id,
        ascendente_id,
        created_at,
        updated_at
    )
SELECT
    'davide_esposito',
    'davide.esposito@demo.it',
    @password_demo,
    'BASE',
    290,
    2,
    2,
    @segno_gemelli,
    @segno_vergine,
    NOW(),
    NOW()
WHERE
    NOT EXISTS (
        SELECT 1
        FROM utenti
        WHERE
            username = 'davide_esposito'
    );

INSERT INTO
    utenti (
        username,
        email,
        password_hash,
        ruolo,
        qi,
        giorni_consecutivi,
        giorni_ricompensa_giornaliera,
        segno_zodiacale_id,
        ascendente_id,
        created_at,
        updated_at
    )
SELECT
    'sara_greco',
    'sara.greco@demo.it',
    @password_demo,
    'BASE',
    560,
    7,
    7,
    @segno_cancro,
    @segno_toro,
    NOW(),
    NOW()
WHERE
    NOT EXISTS (
        SELECT 1
        FROM utenti
        WHERE
            username = 'sara_greco'
    );

INSERT INTO
    utenti (
        username,
        email,
        password_hash,
        ruolo,
        qi,
        giorni_consecutivi,
        giorni_ricompensa_giornaliera,
        segno_zodiacale_id,
        ascendente_id,
        created_at,
        updated_at
    )
SELECT
    'matteo_russo',
    'matteo.russo@demo.it',
    @password_demo,
    'PREMIUM',
    930,
    12,
    12,
    @segno_sagittario,
    @segno_leone,
    NOW(),
    NOW()
WHERE
    NOT EXISTS (
        SELECT 1
        FROM utenti
        WHERE
            username = 'matteo_russo'
    );

INSERT INTO
    utenti (
        username,
        email,
        password_hash,
        ruolo,
        qi,
        giorni_consecutivi,
        giorni_ricompensa_giornaliera,
        segno_zodiacale_id,
        ascendente_id,
        created_at,
        updated_at
    )
SELECT
    'noemi_galli',
    'noemi.galli@demo.it',
    @password_demo,
    'BASE',
    410,
    3,
    3,
    @segno_vergine,
    @segno_bilancia,
    NOW(),
    NOW()
WHERE
    NOT EXISTS (
        SELECT 1
        FROM utenti
        WHERE
            username = 'noemi_galli'
    );

-- Recupera gli ID degli utenti
SELECT id INTO @sofia_id
FROM utenti
WHERE
    username = 'sofia_rossi'
LIMIT 1;

SELECT id INTO @marco_id
FROM utenti
WHERE
    username = 'marco_bianchi'
LIMIT 1;

SELECT id INTO @ely_id
FROM utenti
WHERE
    username = 'elisa_romano'
LIMIT 1;

SELECT id INTO @luca_id
FROM utenti
WHERE
    username = 'luca_ferrari'
LIMIT 1;

SELECT id INTO @giulia_id
FROM utenti
WHERE
    username = 'giulia_conti'
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
        umore,
        stile,
        scadenza_cache,
        created_at,
        updated_at
    )
SELECT @sogno_sofia_id, 'Interpretazione simbolica del sogno degli specchi', 'Entrare in una stanza piena di specchi è un viaggio profondo nel cuore della tua identità. Nel linguaggio dei sogni, lo specchio mostra le molteplici sfaccettature che vivono dentro di te: i ruoli che interpreti, i desideri, le paure o i potenziali ancora inespressi.

La confusione che hai provato è del tutto comprensibile. Trovarsi davanti a così tante versioni di sé può far sentire smarriti e far nascere la domanda: "Chi sono davvero?". Questo sogno potrebbe riflettere un momento di ricerca interiore, in cui ti senti diviso tra diverse strade, aspettative o aspetti della tua personalità.

Simbolicamente, la stanza degli specchi non è una trappola, ma un invito all\'integrazione. Ogni riflesso è una tessera del tuo complesso mosaico interiore. Il sogno sembra suggerire di guardare queste parti con dolcezza: non devi sceglierne necessariamente una sola, ma puoi iniziare ad accoglierle tutte, ricordando che il centro di ogni immagine sei sempre tu.', 'CONFUSO', 'SIMBOLICO', NULL, NOW(), NOW()
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
        umore,
        stile,
        scadenza_cache,
        created_at,
        updated_at
    )
SELECT @sogno_marco_id, 'Interpretazione simbolica del sogno del ponte', 'Il tuo sogno è un\'immagine di straordinaria bellezza e saggezza interiore. Nella prospettiva di Carl Jung, il ponte è un archetipo potente: simboleggia la transizione, un collegamento tra il conscio e l\'inconscio, tra ciò che sei stato e ciò che stai diventando.

Il fatto che il ponte prenda forma solo mentre muovi i passi suggerisce che potresti essere in una fase di individuazione, quel viaggio verso la realizzazione del tuo Sé autentico. La tua psiche ti mostra che non serve vedere la destinazione finale per avanzare: il cammino si crea attraverso la fiducia e l\'azione nel presente.

La serenità che hai provato è l\'elemento chiave: indica un\'armonia profonda tra il tuo Io e le tue risorse interiori. Senti, a un livello inconscio, che la vita ti sostiene ad ogni passo.

Senza pretendere di svelare il futuro, questo sogno sembra regalarti un prezioso promemoria: continua a camminare con coraggio, poiché possiedi già dentro di te tutto ciò che serve per costruire la tua strada.', 'SERENO', 'JUNGHIANO', NULL, NOW(), NOW()
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
        umore,
        stile,
        scadenza_cache,
        created_at,
        updated_at
    )
SELECT @sogno_ely_id, 'Interpretazione simbolica del sogno dell orchestra', 'Questo sogno dipinge un\'immagine di straordinaria armonia e bellezza interiore. L\'orchestra rappresenta la tua vita, le diverse parti del tuo essere o le relazioni che ti circondano: tanti elementi complessi che cercano un linguaggio comune.

La figura del direttore d\'orchestra simboleggia spesso il controllo rigido, l\'autorità o la necessità di dover sempre guidare ogni dettaglio della propria esistenza. Vederla assente, senza che questo generi il caos, suggerisce una profonda scoperta: la fiducia. Il fatto che nessuno fosse stonato indica la presenza di un\'armonia spontanea, un ritmo naturale che non ha bisogno di essere forzato per produrre bellezza.

La serenità che hai provato rivela un momento di profonda pace. È come se il tuo inconscio ti stesse mostrando che, anche quando smetti di controllare tutto, le cose possono andare al posto giusto.

È un dolce invito simbolico a fidarti di te stesso e del flusso della vita: a volte, la musica più bella nasce proprio quando ci concediamo il lusso di non dirigerla.', 'SERENO', 'SIMBOLICO', NULL, NOW(), NOW()
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
        umore,
        stile,
        scadenza_cache,
        created_at,
        updated_at
    )
SELECT @sogno_luca_id, 'Interpretazione simbolica del sogno della montagna', 'Un sogno così vivido porta con sé la forza dei grandi archetipi celesti. L\'emozione intensa che hai provato indica che la tua anima sta rispondendo a un richiamo importante.

Simbolicamente, la tua corsa evoca l\'energia dinamica di Marte: un impulso di coraggio, passione e forte determinazione. La montagna rappresenta l\'archetipo di Saturno e del Capricorno: indica le tue ambizioni più alte, la maturità interiore e i traguardi che richiedono impegno ma regalano una visione elevata. Il Sole che brilla sulla cima richiama la vitalità solare del Leone: è la luce della tua vera essenza, la chiarezza d\'intento e la gioia di manifestarti.

Questa visione non è un destino già scritto, ma la mappa di un\'energia che sta muovendosi dentro di te. Il sogno suggerisce che sei pronto a dirigerti verso una meta luminosa e appagante. Il tuo fuoco interiore ti sta spingendo ad salire più in alto: accogli questo slancio con fiducia e lascia che la tua luce guidi il tuo cammino.', 'INTENSO', 'ASTROLOGICO', NULL, NOW(), NOW()
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
        umore,
        stile,
        scadenza_cache,
        created_at,
        updated_at
    )
SELECT @sogno_giulia_id, 'Interpretazione simbolica del sogno del palco', 'Che sogno luminoso e profondo. Nella psicologia junghiana, il palco simboleggia la Persona, ovvero l\'immagine o il ruolo sociale che mostriamo al mondo. La folla rappresenta lo sguardo della collettività, ma anche i diversi aspetti del tuo stesso inconscio che ti osservano.

In genere, trovarsi esposti su un palco evoca ansia da prestazione. Il fatto che tu provassi serenità e nessuna paura è un segnale di grande armonia interiore. Suggerisce che stai vivendo un momento di profonda integrazione tra chi sei davvero dentro e ciò che mostri all\'esterno: non ti senti più minacciato dal giudizio altrui, né senti il bisogno di indossare una maschera per compiacere.

In chiave simbolica, il sogno potrebbe rappresentare una tappa fondamentale del tuo processo di individuazione (il diventare pienamente te stesso). La tua psiche ti sta mostrando che sei pronto a occupare il tuo spazio nel mondo con autenticità. Accogli questo sogno come un invito a fidarti della tua luce interiore anche nella vita di tutti i giorni.', 'SERENO', 'JUNGHIANO', NULL, NOW(), NOW()
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

-- Aggiunge commenti demo ai post per testare la sezione Social
INSERT INTO
    commenti (
        post_id,
        utente_id,
        testo,
        created_at,
        updated_at
    )
SELECT p.id, @marco_id, 'Mi e capitato qualcosa di simile: questo sogno fa riflettere molto.', NOW() - INTERVAL 90 MINUTE, NOW() - INTERVAL 90 MINUTE
FROM post p
WHERE
    p.testo_visibile = 'Non riesco a smettere di pensare a questo sogno.'
    AND NOT EXISTS (
        SELECT 1
        FROM commenti c
        WHERE
            c.post_id = p.id
            AND c.utente_id = @marco_id
            AND c.testo = 'Mi e capitato qualcosa di simile: questo sogno fa riflettere molto.'
    );

INSERT INTO
    commenti (
        post_id,
        utente_id,
        testo,
        created_at,
        updated_at
    )
SELECT p.id, @giulia_id, 'La fiducia nel percorso si sente davvero in queste parole.', NOW() - INTERVAL 70 MINUTE, NOW() - INTERVAL 70 MINUTE
FROM post p
WHERE
    p.testo_visibile = 'Non riesco a smettere di pensare a questo sogno.'
    AND NOT EXISTS (
        SELECT 1
        FROM commenti c
        WHERE
            c.post_id = p.id
            AND c.utente_id = @giulia_id
            AND c.testo = 'La fiducia nel percorso si sente davvero in queste parole.'
    );

INSERT INTO
    commenti (
        post_id,
        utente_id,
        testo,
        created_at,
        updated_at
    )
SELECT p.id, @sofia_id, 'Che bella immagine, sembra un invito a guardare avanti.', NOW() - INTERVAL 55 MINUTE, NOW() - INTERVAL 55 MINUTE
FROM post p
WHERE
    p.testo_visibile = 'Mi sono svegliato con una grande energia addosso.'
    AND NOT EXISTS (
        SELECT 1
        FROM commenti c
        WHERE
            c.post_id = p.id
            AND c.utente_id = @sofia_id
            AND c.testo = 'Che bella immagine, sembra un invito a guardare avanti.'
    );

INSERT INTO
    commenti (
        post_id,
        utente_id,
        testo,
        created_at,
        updated_at
    )
SELECT p.id, @ely_id, 'Mi piace il significato di crescita che emerge da questo sogno.', NOW() - INTERVAL 40 MINUTE, NOW() - INTERVAL 40 MINUTE
FROM post p
WHERE
    p.testo_visibile = 'Mi sono svegliato con una grande energia addosso.'
    AND NOT EXISTS (
        SELECT 1
        FROM commenti c
        WHERE
            c.post_id = p.id
            AND c.utente_id = @ely_id
            AND c.testo = 'Mi piace il significato di crescita che emerge da questo sogno.'
    );

INSERT INTO
    commenti (
        post_id,
        utente_id,
        testo,
        created_at,
        updated_at
    )
SELECT p.id, @sofia_id, 'A volte condividere queste sensazioni aiuta a capirle meglio.', NOW() - INTERVAL 25 MINUTE, NOW() - INTERVAL 25 MINUTE
FROM post p
WHERE
    p.testo_visibile = 'Non pensavo di sentirmi cosi sicura nel sogno.'
    AND NOT EXISTS (
        SELECT 1
        FROM commenti c
        WHERE
            c.post_id = p.id
            AND c.utente_id = @sofia_id
            AND c.testo = 'A volte condividere queste sensazioni aiuta a capirle meglio.'
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
        'sofia_rossi',
        'marco_bianchi',
        'elisa_romano',
        'luca_ferrari',
        'giulia_conti'
    )
ORDER BY p.data_pubblicazione DESC;