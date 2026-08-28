create table badge (
    attivo bit not null,
    ricompensa_qi integer not null,
    soglia integer,
    created_at datetime(6) not null,
    id bigint not null auto_increment,
    updated_at datetime(6) not null,
    codice varchar(50) not null,
    icona varchar(100) not null,
    nome varchar(100) not null,
    descrizione varchar(255) not null,
    tipo_condizione enum ('GIORNI_CONSECUTIVI','LIKE_RICEVUTI','MAPPA_NATALE','NUMERO_COMMENTI','NUMERO_INTERPRETAZIONI','NUMERO_POST','NUMERO_SOGNI','UTENTE_PREMIUM') not null,
    primary key (id)
) engine=InnoDB;

create table commenti (
    created_at datetime(6) not null,
    id bigint not null auto_increment,
    post_id bigint not null,
    updated_at datetime(6) not null,
    utente_id bigint not null,
    testo TEXT not null,
    primary key (id)
) engine=InnoDB;

create table cosmetici (
    prezzo_qi integer not null,
    created_at datetime(6) not null,
    id bigint not null auto_increment,
    updated_at datetime(6) not null,
    nome varchar(100) not null,
    descrizione TEXT,
    primary key (id)
) engine=InnoDB;

create table elementi (
    created_at datetime(6) not null,
    id bigint not null auto_increment,
    updated_at datetime(6) not null,
    descrizione TEXT,
    nome enum ('ACQUA','ARIA','FUOCO','TERRA') not null,
    primary key (id)
) engine=InnoDB;

create table interpretazioni (
    created_at datetime(6) not null,
    id bigint not null auto_increment,
    scadenza_cache datetime(6),
    sogno_id bigint,
    updated_at datetime(6) not null,
    prompt TEXT not null,
    testo TEXT not null,
    stile enum ('ASTROLOGICO','JUNGHIANO','SIMBOLICO'),
    umore enum ('ANSIOSO','CONFUSO','INTENSO','SERENO'),
    primary key (id)
) engine=InnoDB;

create table inventari_cosmetici (
    equipaggiato bit not null,
    cosmetico_id bigint not null,
    created_at datetime(6) not null,
    data_acquisto datetime(6) not null,
    id bigint not null auto_increment,
    updated_at datetime(6) not null,
    utente_id bigint not null,
    primary key (id)
) engine=InnoDB;

create table like_post (
    created_at datetime(6) not null,
    id bigint not null auto_increment,
    post_id bigint not null,
    updated_at datetime(6) not null,
    utente_id bigint not null,
    primary key (id)
) engine=InnoDB;

create table metalli (
    created_at datetime(6) not null,
    id bigint not null auto_increment,
    updated_at datetime(6) not null,
    descrizione TEXT,
    nome enum ('ARGENTO','FERRO','MERCURIO','ORO','PIOMBO','RAME','STAGNO') not null,
    primary key (id)
) engine=InnoDB;

create table pianeti (
    created_at datetime(6) not null,
    id bigint not null auto_increment,
    updated_at datetime(6) not null,
    descrizione TEXT,
    nome enum ('GIOVE','LUNA','MARTE','MERCURIO','NETTUNO','PLUTONE','SATURNO','SOLE','URANO','VENERE') not null,
    primary key (id)
) engine=InnoDB;

create table post (
    numero_like integer not null,
    created_at datetime(6) not null,
    data_pubblicazione datetime(6) not null,
    id bigint not null auto_increment,
    interpretazione_id bigint,
    updated_at datetime(6) not null,
    utente_id bigint,
    testo_visibile TEXT not null,
    primary key (id)
) engine=InnoDB;

create table segni_zodiacali (
    created_at datetime(6) not null,
    elemento_id bigint not null,
    id bigint not null auto_increment,
    metallo_id bigint not null,
    pianeta_id bigint not null,
    updated_at datetime(6) not null,
    descrizione TEXT,
    modalita enum ('CARDINALE','FISSO','MOBILE') not null,
    nome enum ('ACQUARIO','ARIETE','BILANCIA','CANCRO','CAPRICORNO','GEMELLI','LEONE','PESCI','SAGITTARIO','SCORPIONE','TORO','VERGINE') not null,
    primary key (id)
) engine=InnoDB;

create table sogni (
    created_at datetime(6) not null,
    id bigint not null auto_increment,
    updated_at datetime(6) not null,
    utente_id bigint,
    testo TEXT not null,
    primary key (id)
) engine=InnoDB;

create table temi_natali (
    data_nascita date not null,
    latitudine decimal(10,7),
    longitudine decimal(10,7),
    ora_nascita time(0) not null,
    created_at datetime(6) not null,
    data_creazione datetime(6) not null,
    geoname_id bigint,
    id bigint not null auto_increment,
    updated_at datetime(6) not null,
    utente_id bigint not null,
    timezone varchar(100),
    luogo_nascita varchar(255) not null,
    analisi_gemini LONGTEXT,
    interpretazione_astroway LONGTEXT,
    risposta_astroway LONGTEXT,
    primary key (id)
) engine=InnoDB;

create table utenti (
    giorni_consecutivi integer not null,
    giorni_ricompensa_giornaliera integer not null,
    qi integer not null,
    ultima_ricompensa_giornaliera date,
    ascendente_id bigint,
    created_at datetime(6) not null,
    id bigint not null auto_increment,
    premium_attivato_at datetime(6),
    prossimo_bonus_premium_at datetime(6),
    segno_zodiacale_id bigint,
    ultimo_accesso datetime(6),
    updated_at datetime(6) not null,
    username varchar(50) not null,
    avatar_path varchar(255),
    banner_path varchar(255),
    email varchar(255) not null,
    password_hash varchar(255) not null,
    ruolo enum ('ADMIN','BASE','PREMIUM') not null,
    primary key (id)
) engine=InnoDB;

create table utenti_badge (
    badge_id bigint not null,
    created_at datetime(6) not null,
    id bigint not null auto_increment,
    updated_at datetime(6) not null,
    utente_id bigint not null,
    primary key (id)
) engine=InnoDB;

create table utenti_follow (
    created_at datetime(6) not null,
    follower_id bigint not null,
    id bigint not null auto_increment,
    seguito_id bigint not null,
    updated_at datetime(6) not null,
    primary key (id)
) engine=InnoDB;

alter table badge
    add constraint UKm07ppw776gn4s1k40w6juhod8 unique (codice);

alter table elementi
    add constraint uk_elementi_nome unique (nome);

alter table inventari_cosmetici
    add constraint uq_utente_ruolo unique (utente_id, cosmetico_id);

alter table like_post
    add constraint uk_like_post_utente unique (post_id, utente_id);

alter table metalli
    add constraint uk_metalli_nome unique (nome);

alter table pianeti
    add constraint uk_pianeti_nome unique (nome);

alter table segni_zodiacali
    add constraint uk_segni_zodiacali_nome unique (nome);

alter table temi_natali
    add constraint UKea23e2jdytfft2mukn174wx2q unique (utente_id);

alter table utenti
    add constraint UKtn8mwk6h2wn28yyj7fco65gls unique (username);

alter table utenti
    add constraint UK9b90mk1nolf3ou90p42a93tjo unique (email);

alter table utenti_badge
    add constraint uk_utente_badge unique (utente_id, badge_id);

alter table utenti_follow
    add constraint uk_utenti_follow unique (follower_id, seguito_id);

alter table commenti
    add constraint FK5ie6vb5mnr0j99e82rbqo489q
    foreign key (post_id)
    references post (id);

alter table commenti
    add constraint FK4aditjgi8iph9b98qymyvs7m1
    foreign key (utente_id)
    references utenti (id);

alter table interpretazioni
    add constraint FKbqilefavtyve2758ib1wib56t
    foreign key (sogno_id)
    references sogni (id);

alter table inventari_cosmetici
    add constraint FK23401732mjyhs0t3gd5yancj5
    foreign key (cosmetico_id)
    references cosmetici (id);

alter table inventari_cosmetici
    add constraint FK3ds7kjhqx7dt14wbtg2at655x
    foreign key (utente_id)
    references utenti (id);

alter table like_post
    add constraint FKnu91sbh82a5nj1o3xh0sgwhu8
    foreign key (post_id)
    references post (id);

alter table like_post
    add constraint FKqax5df8lyyk02glhjn5ujhuhn
    foreign key (utente_id)
    references utenti (id);

alter table post
    add constraint FK9xqcgvyksg1hiop69x7l0b11e
    foreign key (interpretazione_id)
    references interpretazioni (id);

alter table post
    add constraint FK3xwndb62i4o57fy56cplo4mtl
    foreign key (utente_id)
    references utenti (id);

alter table segni_zodiacali
    add constraint fk_segni_zodiacali_elementi
    foreign key (elemento_id)
    references elementi (id);

alter table segni_zodiacali
    add constraint fk_segni_zodiacali_metalli
    foreign key (metallo_id)
    references metalli (id);

alter table segni_zodiacali
    add constraint fk_segni_zodiacali_pianeti
    foreign key (pianeta_id)
    references pianeti (id);

alter table sogni
    add constraint FKkiiunt9cu43w9etwlihomb7nm
    foreign key (utente_id)
    references utenti (id);

alter table temi_natali
    add constraint FKsvsrbcg1yjpc15fu7j4v6xp1d
    foreign key (utente_id)
    references utenti (id);

alter table utenti
    add constraint FKdi2qm9utmr8ta7ofcf7xqsi8p
    foreign key (ascendente_id)
    references segni_zodiacali (id);

alter table utenti
    add constraint FK96n03birfg9ukjjd47tw18m21
    foreign key (segno_zodiacale_id)
    references segni_zodiacali (id);

alter table utenti_badge
    add constraint FK6ofiagva5onbjobknidyb0pic
    foreign key (badge_id)
    references badge (id);

alter table utenti_badge
    add constraint FKcgg84gpue5hk6lpkgg52fgqrg
    foreign key (utente_id)
    references utenti (id);

alter table utenti_follow
    add constraint FKtgjrmrpj5gru4gk7614smo1ud
    foreign key (follower_id)
    references utenti (id);

alter table utenti_follow
    add constraint FKcxrlgj7qep18uln31789stwmp
    foreign key (seguito_id)
    references utenti (id);
