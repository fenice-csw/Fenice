--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.1
-- Dumped by pg_dump version 9.5.5

-- Started on 2018-07-13 02:39:54

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 7 (class 2615 OID 294268)
-- Name: fenice; Type: SCHEMA; Schema: -; Owner: fenice
--

CREATE SCHEMA fenice;


ALTER SCHEMA fenice OWNER TO fenice;

--
-- TOC entry 1 (class 3079 OID 294270)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 3193 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = fenice, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 163 (class 1259 OID 303263)
-- Name: acquisizione_massiva_logs; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE acquisizione_massiva_logs (
    nome_file character varying(500) NOT NULL,
    errore character varying(400),
    aoo_id integer NOT NULL,
    data_log timestamp without time zone DEFAULT now()
);


ALTER TABLE acquisizione_massiva_logs OWNER TO fenice;

--
-- TOC entry 268 (class 1259 OID 306072)
-- Name: allegati_doc_amm_trasparente; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE allegati_doc_amm_trasparente (
    dc_id integer NOT NULL,
    doc_sezione_id integer NOT NULL,
    flag_riservato character(1) DEFAULT '0'::bpchar NOT NULL,
    tipo integer NOT NULL,
    flag_principale character(1) DEFAULT '0'::bpchar NOT NULL,
    flag_pubblicabile character(1) DEFAULT '0'::bpchar
);


ALTER TABLE allegati_doc_amm_trasparente OWNER TO fenice;

--
-- TOC entry 259 (class 1259 OID 305715)
-- Name: allegati_doc_repertorio; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE allegati_doc_repertorio (
    dc_id integer NOT NULL,
    doc_repertorio_id integer NOT NULL,
    flag_riservato character(1) DEFAULT '0'::bpchar NOT NULL,
    tipo integer NOT NULL,
    flag_principale character(1) DEFAULT '0'::bpchar NOT NULL,
    flag_pubblicabile character(1) DEFAULT '0'::bpchar
);


ALTER TABLE allegati_doc_repertorio OWNER TO fenice;

--
-- TOC entry 266 (class 1259 OID 306022)
-- Name: amm_trasparente; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE amm_trasparente (
    sezione_id integer NOT NULL,
    aoo_id integer,
    descrizione character varying(500),
    ufficio_responsabile_id integer NOT NULL,
    flag_web numeric(1,0) DEFAULT 0
);


ALTER TABLE amm_trasparente OWNER TO fenice;

--
-- TOC entry 267 (class 1259 OID 306041)
-- Name: amm_trasparente_documenti; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE amm_trasparente_documenti (
    doc_id integer NOT NULL,
    note text,
    oggetto character varying(500),
    capitolo character varying(50),
    importo numeric(18,2),
    data_validita_inizio timestamp without time zone,
    data_validita_fine timestamp without time zone,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    ufficio_id integer,
    data_creazione timestamp without time zone,
    sez_id integer,
    numero_documento_sezione integer NOT NULL,
    flag_stato integer DEFAULT 0 NOT NULL,
    descrizione text,
    numero_documento character varying(32),
    settore_proponente character varying(255),
    protocollo_id integer
);


ALTER TABLE amm_trasparente_documenti OWNER TO fenice;

--
-- TOC entry 164 (class 1259 OID 303270)
-- Name: amministrazione; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE amministrazione (
    amministrazione_id integer NOT NULL,
    codi_amministrazione character varying(20) NOT NULL,
    desc_amministrazione character varying(254) NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer DEFAULT 0 NOT NULL,
    flag_ldap character(1) DEFAULT 0 NOT NULL,
    ldap_versione integer,
    ldap_porta integer DEFAULT 389,
    ldap_use_ssl character(1) DEFAULT 0,
    ldap_host character varying(256),
    ldap_dn character varying(256),
    path_doc character varying(255) NOT NULL,
    path_doc_protocollo character varying(255),
    flag_reg_separato character(1) DEFAULT 1 NOT NULL,
    ftp_host character varying(100),
    ftp_port integer DEFAULT 9990,
    ftp_user character varying(100),
    ftp_pass character varying(100),
    id_unita_amministrativa integer DEFAULT 0,
    versione_corrente_fenice character varying(10),
    ftp_folder character varying(100),
    flag_web_socket numeric(1,0) NOT NULL
);


ALTER TABLE amministrazione OWNER TO fenice;

--
-- TOC entry 244 (class 1259 OID 305107)
-- Name: amministrazioni_partecipanti; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE amministrazioni_partecipanti (
    amministrazioni_id integer NOT NULL,
    nominativo character varying(160),
    rubrica_id integer,
    tipo_procedimenti_id integer
);


ALTER TABLE amministrazioni_partecipanti OWNER TO fenice;

--
-- TOC entry 165 (class 1259 OID 303282)
-- Name: aree_organizzative; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE aree_organizzative (
    aoo_id integer NOT NULL,
    codi_aoo character varying(100) NOT NULL,
    desc_aoo character varying(100) NOT NULL,
    data_istituzione timestamp without time zone,
    responsabile_nome character varying(40),
    responsabile_cognome character varying(100),
    responsabile_email character varying(100),
    responsabile_telefono character varying(16),
    data_soppressione timestamp without time zone,
    telefono character varying(16),
    fax character varying(16),
    indi_dug character varying(10) NOT NULL,
    indi_toponimo character varying(40) NOT NULL,
    indi_civico character varying(10) NOT NULL,
    indi_cap character varying(8) NOT NULL,
    indi_comune character varying(50) NOT NULL,
    email character varying(100),
    dipartimento_codice character varying(10),
    dipartimento_descrizione character varying(100),
    tipo_aoo character(1) DEFAULT 'L'::bpchar NOT NULL,
    provincia_id integer,
    codi_documento_doc character varying(32),
    flag_pdf character(1) DEFAULT '0'::bpchar NOT NULL,
    amministrazione_id integer NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    nume_blocco_titolario numeric(2,0),
    versione integer DEFAULT 0 NOT NULL,
    dipendenza_titolario_ufficio integer,
    titolario_livello_minimo integer DEFAULT 0,
    utente_responsabile_id integer,
    flag_documento_readable numeric(1,0) DEFAULT 1,
    flag_ricerca_full numeric(1,0) DEFAULT 1,
    fattura_id_committente character varying(50),
    ga_abilitata numeric(1,0) DEFAULT 0,
    ga_username character varying(70),
    ga_pwd character varying(50),
    ga_flag_invio numeric(1,0) DEFAULT 0,
    ga_timer time without time zone,
    anni_visibilita_bacheca character varying(2) DEFAULT 1,
    max_righe character varying(5) DEFAULT 1000,
    flag_p7m integer DEFAULT 0,
    CONSTRAINT aree_organizzative_flag_pdf_check CHECK ((((flag_pdf)::text = (1)::text) OR ((flag_pdf)::text = (0)::text))),
    CONSTRAINT aree_organizzative_tipo_aoo_check CHECK (((tipo_aoo = 'F'::bpchar) OR (tipo_aoo = 'L'::bpchar)))
);


ALTER TABLE aree_organizzative OWNER TO fenice;

--
-- TOC entry 253 (class 1259 OID 305420)
-- Name: assegnatari_lista_distribuzione; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE assegnatari_lista_distribuzione (
    id_assegnatario_lista integer NOT NULL,
    id_lista integer NOT NULL,
    id_carica integer,
    id_ufficio integer,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now()
);


ALTER TABLE assegnatari_lista_distribuzione OWNER TO fenice;

--
-- TOC entry 166 (class 1259 OID 303298)
-- Name: ca_crl; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE ca_crl (
    ca_id integer NOT NULL,
    id integer NOT NULL,
    url character varying(500) NOT NULL,
    tipo character varying(20) NOT NULL,
    data_emissione timestamp without time zone,
    codice_errore integer DEFAULT 4 NOT NULL
);


ALTER TABLE ca_crl OWNER TO fenice;

--
-- TOC entry 167 (class 1259 OID 303305)
-- Name: ca_lista; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE ca_lista (
    ca_id integer NOT NULL,
    issuer_cn character varying(500) NOT NULL,
    valido_dal timestamp without time zone NOT NULL,
    valido_al timestamp without time zone NOT NULL,
    certificato bytea
);


ALTER TABLE ca_lista OWNER TO fenice;

--
-- TOC entry 168 (class 1259 OID 303311)
-- Name: ca_revoked_list; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE ca_revoked_list (
    ca_id integer NOT NULL,
    data_revoca timestamp without time zone NOT NULL,
    serial_number character varying(100) NOT NULL
);


ALTER TABLE ca_revoked_list OWNER TO fenice;

--
-- TOC entry 240 (class 1259 OID 304948)
-- Name: cariche; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE cariche (
    carica_id integer NOT NULL,
    denominazione character varying(160),
    ufficio_id integer NOT NULL,
    profilo_id integer,
    utente_id integer,
    versione integer DEFAULT 0 NOT NULL,
    flag_attivo numeric(1,0) DEFAULT 1 NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    flag_dirigente numeric(1,0) DEFAULT 0,
    flag_referente numeric(1,0) DEFAULT 0,
    flag_responsabile_ente numeric(1,0) DEFAULT 0,
    flag_referente_ufficio_protocollo numeric(1,0) DEFAULT 0
);


ALTER TABLE cariche OWNER TO fenice;

--
-- TOC entry 169 (class 1259 OID 303314)
-- Name: caselle_email; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE caselle_email (
    casella_id integer NOT NULL,
    server character varying(30) NOT NULL,
    protocollo_tcp character varying(20) NOT NULL,
    utente character varying(20) NOT NULL,
    password character varying(20) NOT NULL,
    indirizzo character varying(100) NOT NULL,
    tipo character(1) NOT NULL,
    ssl_porta character varying(5),
    flag_ufficiale character(1) DEFAULT '0'::bpchar NOT NULL,
    fk_aoo integer,
    flag_ssl character(1) DEFAULT '0'::bpchar NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer DEFAULT 0 NOT NULL,
    CONSTRAINT caselle_email_flag_ssl_check CHECK ((((flag_ssl)::text = (1)::text) OR ((flag_ssl)::text = (0)::text))),
    CONSTRAINT caselle_email_flag_ufficiale_check CHECK ((((flag_ufficiale)::text = (0)::text) OR ((flag_ufficiale)::text = (1)::text))),
    CONSTRAINT caselle_email_tipo_check CHECK ((((tipo = 'I'::bpchar) OR (tipo = 'U'::bpchar)) OR (tipo = 'S'::bpchar)))
);


ALTER TABLE caselle_email OWNER TO fenice;

--
-- TOC entry 252 (class 1259 OID 305367)
-- Name: check_posta_interna; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE check_posta_interna (
    check_id integer NOT NULL,
    protocollo_id integer,
    data_operazione timestamp with time zone,
    ufficio_assegnante_id integer,
    ufficio_assegnatario_id integer,
    flag_competente numeric(1,0),
    carica_assegnatario_id integer,
    carica_assegnante_id integer,
    check_presa_visione numeric(1,0) DEFAULT 0
);


ALTER TABLE check_posta_interna OWNER TO fenice;

--
-- TOC entry 170 (class 1259 OID 303331)
-- Name: doc_cartelle; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE doc_cartelle (
    dc_id integer NOT NULL,
    parent_id integer,
    nome character varying(100) NOT NULL,
    aoo_id integer NOT NULL,
    is_root integer DEFAULT 0,
    carica_id integer
);


ALTER TABLE doc_cartelle OWNER TO fenice;

--
-- TOC entry 3194 (class 0 OID 0)
-- Dependencies: 170
-- Name: TABLE doc_cartelle; Type: COMMENT; Schema: fenice; Owner: fenice
--

COMMENT ON TABLE doc_cartelle IS 'documentale, albero delle cartelle';


--
-- TOC entry 171 (class 1259 OID 303335)
-- Name: doc_file_attr; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE doc_file_attr (
    dfa_id integer NOT NULL,
    dc_id integer NOT NULL,
    dfr_id integer,
    nome character varying(100) NOT NULL,
    versione integer NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(150),
    data_documento timestamp without time zone DEFAULT now(),
    oggetto character varying(255),
    note character varying(255),
    descrizione character varying(255),
    descrizione_argomento character varying(255),
    tipo_documento_id integer,
    titolario_id integer,
    stato_lav character(1) DEFAULT 0 NOT NULL,
    stato_arc character(1) DEFAULT 'L'::bpchar NOT NULL,
    owner_id integer NOT NULL,
    row_updated_time timestamp without time zone DEFAULT now(),
    carica_lav_id integer,
    procedimento_id integer
);


ALTER TABLE doc_file_attr OWNER TO fenice;

--
-- TOC entry 3195 (class 0 OID 0)
-- Dependencies: 171
-- Name: TABLE doc_file_attr; Type: COMMENT; Schema: fenice; Owner: fenice
--

COMMENT ON TABLE doc_file_attr IS 'documentale, attributi dei file';


--
-- TOC entry 172 (class 1259 OID 303346)
-- Name: doc_file_permessi; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE doc_file_permessi (
    dfp_id integer NOT NULL,
    dfa_id integer NOT NULL,
    tipo_permesso integer NOT NULL,
    ufficio_id integer,
    versione integer DEFAULT 0 NOT NULL,
    msg character varying(255),
    carica_id integer
);


ALTER TABLE doc_file_permessi OWNER TO fenice;

--
-- TOC entry 173 (class 1259 OID 303350)
-- Name: doc_file_rep; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE doc_file_rep (
    dfr_id integer NOT NULL,
    data bytea,
    filename character varying(500),
    content_type character varying(100),
    file_size integer,
    impronta character varying(255),
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32)
);


ALTER TABLE doc_file_rep OWNER TO fenice;

--
-- TOC entry 3196 (class 0 OID 0)
-- Dependencies: 173
-- Name: TABLE doc_file_rep; Type: COMMENT; Schema: fenice; Owner: fenice
--

COMMENT ON TABLE doc_file_rep IS 'documentale, file repository, contiene i file in formato binario';


--
-- TOC entry 174 (class 1259 OID 303357)
-- Name: documenti; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE documenti (
    documento_id integer NOT NULL,
    descrizione character varying(500),
    filename character varying(500) NOT NULL,
    content_type character varying(100),
    file_size integer,
    impronta character varying(255),
    data bytea,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    path text
);


ALTER TABLE documenti OWNER TO fenice;

--
-- TOC entry 248 (class 1259 OID 305229)
-- Name: documenti_editor; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE documenti_editor (
    documento_id integer NOT NULL,
    txt text,
    nome character varying(50),
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    carica_id integer NOT NULL,
    flag_stato integer DEFAULT 0 NOT NULL,
    versione integer DEFAULT 0 NOT NULL,
    oggetto text,
    flag_tipo integer DEFAULT 0 NOT NULL,
    msg_carica character varying(2000)
);


ALTER TABLE documenti_editor OWNER TO fenice;

--
-- TOC entry 258 (class 1259 OID 305700)
-- Name: documenti_repertori; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE documenti_repertori (
    doc_repertorio_id integer NOT NULL,
    note text,
    oggetto character varying(500),
    capitolo character varying(50),
    importo numeric(18,2),
    data_validita_inizio timestamp without time zone,
    data_validita_fine timestamp without time zone,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    ufficio_id integer,
    data_creazione timestamp without time zone,
    rep_id integer,
    numero_documento_repertorio integer NOT NULL,
    flag_stato integer DEFAULT 0 NOT NULL,
    descrizione text,
    numero_documento character varying(32),
    settore_proponente character varying(255),
    protocollo_id integer
);


ALTER TABLE documenti_repertori OWNER TO fenice;

--
-- TOC entry 175 (class 1259 OID 303364)
-- Name: email_coda_invio; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE email_coda_invio (
    id integer NOT NULL,
    aoo_id integer NOT NULL,
    data_creazione date,
    stato integer DEFAULT 0 NOT NULL,
    protocollo_id integer NOT NULL,
    data_invio date
);


ALTER TABLE email_coda_invio OWNER TO fenice;

--
-- TOC entry 176 (class 1259 OID 303368)
-- Name: email_coda_invio_destinatari; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE email_coda_invio_destinatari (
    msg_id integer NOT NULL,
    email character varying(200) NOT NULL,
    nominativo character varying(200),
    tipo character varying(5) NOT NULL
);


ALTER TABLE email_coda_invio_destinatari OWNER TO fenice;

--
-- TOC entry 177 (class 1259 OID 303371)
-- Name: email_ingresso; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE email_ingresso (
    email_id integer NOT NULL,
    descrizione character varying(500),
    filename character varying(500) NOT NULL,
    content_type character varying(100),
    dimensione integer,
    impronta character varying(255),
    testo_messaggio bytea,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    email_mittente character varying(200),
    nome_mittente character varying(200),
    email_oggetto character varying(200),
    data_spedizione timestamp without time zone,
    data_ricezione timestamp without time zone,
    segnatura bytea,
    flag_stato integer DEFAULT 0 NOT NULL,
    aoo_id integer NOT NULL,
    flag_anomalia integer DEFAULT 0 NOT NULL,
    message_header_id character varying(255)
);


ALTER TABLE email_ingresso OWNER TO fenice;

--
-- TOC entry 178 (class 1259 OID 303379)
-- Name: email_ingresso_allegati; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE email_ingresso_allegati (
    id integer NOT NULL,
    email_id integer NOT NULL,
    descrizione character varying(500),
    filename character varying(500) NOT NULL,
    content_type character varying(100),
    dimensione integer,
    impronta character varying(255),
    data bytea,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32)
);


ALTER TABLE email_ingresso_allegati OWNER TO fenice;

--
-- TOC entry 179 (class 1259 OID 303386)
-- Name: email_logs; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE email_logs (
    email_id integer NOT NULL,
    errore character varying(400),
    tipo_log integer,
    aoo_id integer NOT NULL,
    data_log timestamp without time zone DEFAULT now()
);


ALTER TABLE email_logs OWNER TO fenice;

--
-- TOC entry 180 (class 1259 OID 303393)
-- Name: faldone_fascicoli; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE faldone_fascicoli (
    faldone_id integer NOT NULL,
    fascicolo_id integer NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer DEFAULT 0 NOT NULL
);


ALTER TABLE faldone_fascicoli OWNER TO fenice;

--
-- TOC entry 181 (class 1259 OID 303399)
-- Name: faldone_procedimenti; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE faldone_procedimenti (
    faldone_id integer NOT NULL,
    procedimento_id integer NOT NULL
);


ALTER TABLE faldone_procedimenti OWNER TO fenice;

--
-- TOC entry 182 (class 1259 OID 303402)
-- Name: faldoni; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE faldoni (
    faldone_id integer NOT NULL,
    aoo_id integer NOT NULL,
    oggetto character varying(500) NOT NULL,
    ufficio_id integer NOT NULL,
    titolario_id integer,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    codice_locale character varying(20),
    sotto_categoria character varying(250),
    nota character varying(250),
    numero_faldone character varying(11) NOT NULL,
    anno integer DEFAULT 0 NOT NULL,
    numero integer DEFAULT 0 NOT NULL,
    data_creazione date NOT NULL,
    data_carico timestamp without time zone,
    data_scarico timestamp without time zone,
    data_evidenza timestamp without time zone,
    data_movimento timestamp without time zone,
    stato_id integer,
    versione integer DEFAULT 0 NOT NULL,
    collocazione_label1 character varying(200),
    collocazione_label2 character varying(200),
    collocazione_label3 character varying(200),
    collocazione_label4 character varying(200),
    collocazione_valore1 character varying(200),
    collocazione_valore2 character varying(200),
    collocazione_valore3 character varying(200),
    collocazione_valore4 character varying(200)
);


ALTER TABLE faldoni OWNER TO fenice;

--
-- TOC entry 183 (class 1259 OID 303413)
-- Name: fascicoli; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE fascicoli (
    fascicolo_id integer NOT NULL,
    aoo_id integer,
    codice character varying(10),
    progressivo numeric(18,0),
    collocazione character varying(100),
    note character varying(1024),
    processo_id integer,
    registro_id integer,
    data_apertura timestamp without time zone,
    data_chiusura timestamp without time zone,
    stato integer,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    titolario_id integer,
    versione integer DEFAULT 0 NOT NULL,
    ufficio_intestatario_id integer,
    ufficio_responsabile_id integer,
    data_evidenza timestamp without time zone,
    anno_riferimento integer,
    tipo integer DEFAULT 0 NOT NULL,
    data_ultimo_movimento timestamp without time zone,
    data_scarto timestamp without time zone,
    data_carico timestamp without time zone,
    data_scarico timestamp without time zone,
    collocazione_label1 character varying(200),
    collocazione_label2 character varying(200),
    collocazione_label3 character varying(200),
    collocazione_label4 character varying(200),
    collocazione_valore1 character varying(200),
    collocazione_valore2 character varying(200),
    collocazione_valore3 character varying(200),
    collocazione_valore4 character varying(200),
    parent_id integer,
    giorni_max integer,
    giorni_alert integer,
    oggetto text,
    descrizione text,
    nome text,
    carica_istruttore_id integer,
    carica_responsabile_id integer,
    carica_intestatario_id integer,
    comune character varying(50),
    capitolo character varying(100),
    path_progressivo character varying(100),
    delegato character varying(200),
    interessato character varying(200),
    indi_delegato character varying(250),
    indi_interessato character varying(250)
);


ALTER TABLE fascicoli OWNER TO fenice;

--
-- TOC entry 251 (class 1259 OID 305293)
-- Name: fascicolo_allacci; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE fascicolo_allacci (
    fascicolo_id integer NOT NULL,
    fascicolo_allacciato_id integer NOT NULL,
    row_created_user character varying(32),
    ufficio_proprietario_id integer
);


ALTER TABLE fascicolo_allacci OWNER TO fenice;

--
-- TOC entry 184 (class 1259 OID 303423)
-- Name: fascicolo_documenti; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE fascicolo_documenti (
    dfa_id integer NOT NULL,
    fascicolo_id integer NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer DEFAULT 0 NOT NULL,
    ufficio_proprietario_id integer
);


ALTER TABLE fascicolo_documenti OWNER TO fenice;

--
-- TOC entry 185 (class 1259 OID 303429)
-- Name: fascicolo_protocolli; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE fascicolo_protocolli (
    protocollo_id integer NOT NULL,
    fascicolo_id integer NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer DEFAULT 0 NOT NULL,
    ufficio_proprietario_id integer
);


ALTER TABLE fascicolo_protocolli OWNER TO fenice;

--
-- TOC entry 186 (class 1259 OID 303435)
-- Name: identificativi; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE identificativi (
    nome_tabella character varying(128) NOT NULL,
    id_corrente integer NOT NULL
);


ALTER TABLE identificativi OWNER TO fenice;

--
-- TOC entry 187 (class 1259 OID 303438)
-- Name: invio_classificati; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE invio_classificati (
    id integer NOT NULL,
    dfa_id integer NOT NULL,
    aoo_id integer NOT NULL,
    ufficio_mittente_id integer,
    utente_mittente_id integer,
    procedimento_id integer,
    data_scadenza timestamp without time zone,
    text_scadenza text
);


ALTER TABLE invio_classificati OWNER TO fenice;

--
-- TOC entry 188 (class 1259 OID 303441)
-- Name: invio_classificati_destinatari; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE invio_classificati_destinatari (
    id integer NOT NULL,
    dfa_id integer NOT NULL,
    flag_tipo_destinatario character(1) NOT NULL,
    indirizzo character varying(100),
    email character varying(100),
    destinatario character varying(160),
    citta character varying(100),
    data_spedizione timestamp without time zone,
    flag_conoscenza character(1) DEFAULT '0'::bpchar NOT NULL,
    data_effettiva_spedizione timestamp without time zone,
    versione integer DEFAULT 0 NOT NULL,
    titolo_id integer,
    mezzo_spedizione integer,
    CONSTRAINT invio_classificati_destinatari_flag_conoscenza_check CHECK (((flag_conoscenza = '1'::bpchar) OR (flag_conoscenza = '0'::bpchar))),
    CONSTRAINT invio_classificati_destinatari_flag_tipo_destinatario_check CHECK ((((flag_tipo_destinatario = 'F'::bpchar) OR (flag_tipo_destinatario = 'G'::bpchar)) OR (flag_tipo_destinatario = 'A'::bpchar)))
);


ALTER TABLE invio_classificati_destinatari OWNER TO fenice;

--
-- TOC entry 250 (class 1259 OID 305261)
-- Name: invio_classificati_fascicoli; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE invio_classificati_fascicoli (
    id integer NOT NULL,
    dfa_id integer NOT NULL,
    fascicolo_id integer NOT NULL
);


ALTER TABLE invio_classificati_fascicoli OWNER TO fenice;

--
-- TOC entry 189 (class 1259 OID 303448)
-- Name: invio_fascicoli; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE invio_fascicoli (
    id integer NOT NULL,
    fascicolo_id integer NOT NULL,
    dfa_id integer NOT NULL,
    flag_documento_principale character(1) DEFAULT '0'::bpchar NOT NULL,
    aoo_id integer NOT NULL,
    ufficio_mittente_id integer,
    utente_mittente_id integer
);


ALTER TABLE invio_fascicoli OWNER TO fenice;

--
-- TOC entry 190 (class 1259 OID 303452)
-- Name: invio_fascicoli_destinatari; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE invio_fascicoli_destinatari (
    id integer NOT NULL,
    fascicolo_id integer NOT NULL,
    flag_tipo_destinatario character(1) NOT NULL,
    indirizzo character varying(100),
    email character varying(100),
    destinatario character varying(160),
    citta character varying(100),
    data_spedizione timestamp without time zone,
    flag_conoscenza character(1) DEFAULT '0'::bpchar NOT NULL,
    data_effettiva_spedizione timestamp without time zone,
    versione integer DEFAULT 0 NOT NULL,
    titolo_id integer,
    mezzo_spedizione integer,
    CONSTRAINT invio_fascicoli_destinatari_flag_conoscenza_check CHECK (((flag_conoscenza = '1'::bpchar) OR (flag_conoscenza = '0'::bpchar))),
    CONSTRAINT invio_fascicoli_destinatari_flag_tipo_destinatario_check CHECK ((((flag_tipo_destinatario = 'F'::bpchar) OR (flag_tipo_destinatario = 'G'::bpchar)) OR (flag_tipo_destinatario = 'A'::bpchar)))
);


ALTER TABLE invio_fascicoli_destinatari OWNER TO fenice;

--
-- TOC entry 263 (class 1259 OID 305903)
-- Name: job_scheduled_logs; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE job_scheduled_logs (
    js_id integer NOT NULL,
    message character varying(400),
    status integer,
    aoo_id integer NOT NULL,
    data_log date
);


ALTER TABLE job_scheduled_logs OWNER TO fenice;

--
-- TOC entry 191 (class 1259 OID 303459)
-- Name: lista_distribuzione; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE lista_distribuzione (
    id integer NOT NULL,
    descrizione character varying(255) NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    aoo_id integer NOT NULL,
    flag_tipo character varying(1)
);


ALTER TABLE lista_distribuzione OWNER TO fenice;

--
-- TOC entry 261 (class 1259 OID 305805)
-- Name: mail_config; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE mail_config (
    config_id integer NOT NULL,
    aoo_id integer NOT NULL,
    pec_abilitata integer DEFAULT 0 NOT NULL,
    pec_email character varying(200),
    pec_username character varying(50),
    pec_password character varying(50),
    pec_ssl_port character varying(5),
    pec_pop3_host character varying(100),
    pec_smtp_host character varying(100),
    pec_tipo_protocollo character varying(10) DEFAULT 'POP3'::character varying NOT NULL,
    pec_smtp_port character varying(5),
    data_ultima_pec timestamp without time zone DEFAULT now(),
    pn_abilitata integer DEFAULT 0 NOT NULL,
    pn_email character varying(200),
    pn_username character varying(50),
    pn_password character varying(50),
    pn_ssl_port character varying(5),
    pn_pop3_host character varying(100),
    pn_use_ssl character varying(1),
    pn_smtp_host character varying(100),
    prec_email_invio character varying(200),
    prec_email_ricezione character varying(200),
    prec_username character varying(50),
    prec_password character varying(50),
    prec_smtp_host character varying(100),
    mail_timer integer
);


ALTER TABLE mail_config OWNER TO fenice;

--
-- TOC entry 192 (class 1259 OID 303464)
-- Name: menu; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE menu (
    menu_id integer NOT NULL,
    titolo character varying(40) NOT NULL,
    descrizione character varying(254) NOT NULL,
    parent_id integer,
    posizione integer NOT NULL,
    link character varying(512),
    root_function integer DEFAULT 1 NOT NULL,
    unique_name character varying(40)
);


ALTER TABLE menu OWNER TO fenice;

--
-- TOC entry 262 (class 1259 OID 305845)
-- Name: notifiche_fattura_elettronica; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE notifiche_fattura_elettronica (
    nfe_id integer NOT NULL,
    aoo_id integer NOT NULL,
    progressivo character varying(3),
    anno integer
);


ALTER TABLE notifiche_fattura_elettronica OWNER TO fenice;

SET default_with_oids = true;

--
-- TOC entry 193 (class 1259 OID 303471)
-- Name: oggetti; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE oggetti (
    descrizione text NOT NULL,
    id integer NOT NULL,
    giorni_alert integer,
    aoo_id integer
);


ALTER TABLE oggetti OWNER TO fenice;

SET default_with_oids = false;

--
-- TOC entry 239 (class 1259 OID 304933)
-- Name: oggetto_assegnatari; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE oggetto_assegnatari (
    oggetto_id integer NOT NULL,
    ufficio_id integer NOT NULL
);


ALTER TABLE oggetto_assegnatari OWNER TO fenice;

--
-- TOC entry 241 (class 1259 OID 304971)
-- Name: permessi_carica; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE permessi_carica (
    carica_id integer NOT NULL,
    menu_id integer NOT NULL
);


ALTER TABLE permessi_carica OWNER TO fenice;

--
-- TOC entry 194 (class 1259 OID 303483)
-- Name: procedimenti; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE procedimenti (
    procedimento_id integer NOT NULL,
    data_avvio timestamp without time zone NOT NULL,
    ufficio_id integer NOT NULL,
    stato_id integer NOT NULL,
    tipo_finalita_id integer NOT NULL,
    oggetto text NOT NULL,
    referente_id integer,
    posizione_id character varying(1),
    data_evidenza timestamp without time zone,
    note character varying(1000),
    protocollo_id integer NOT NULL,
    numero_procedimento character varying(100) NOT NULL,
    anno integer DEFAULT 0 NOT NULL,
    numero integer DEFAULT 0 NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer DEFAULT 0 NOT NULL,
    aoo_id integer NOT NULL,
    giorni_alert integer,
    giorni_max integer,
    tipo_procedimento_id integer,
    responsabile_id integer,
    fascicolo_id integer,
    indicazioni text,
    estremi_provvedimento character varying(250),
    carica_lav_id integer,
    estremi_sospensione text,
    data_sospensione timestamp without time zone,
    data_scadenza timestamp without time zone,
    flag_sospeso numeric(1,0) DEFAULT 0,
    autorita character varying(500),
    delegato character varying(200),
    interessato character varying(200),
    indi_delegato character varying(250),
    indi_interessato character varying(250)
);


ALTER TABLE procedimenti OWNER TO fenice;

--
-- TOC entry 195 (class 1259 OID 303494)
-- Name: procedimenti_faldone; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE procedimenti_faldone (
    procedimento_id integer NOT NULL,
    faldone_id integer NOT NULL
);


ALTER TABLE procedimenti_faldone OWNER TO fenice;

--
-- TOC entry 196 (class 1259 OID 303497)
-- Name: procedimenti_fascicolo; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE procedimenti_fascicolo (
    procedimento_id integer NOT NULL,
    fascicolo_id integer NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer NOT NULL
);


ALTER TABLE procedimenti_fascicolo OWNER TO fenice;

--
-- TOC entry 245 (class 1259 OID 305122)
-- Name: procedimento_istruttori; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE procedimento_istruttori (
    procedimento_id integer NOT NULL,
    carica_id integer NOT NULL,
    versione integer,
    flag_lavorato numeric(1,0) DEFAULT 0,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32)
);


ALTER TABLE procedimento_istruttori OWNER TO fenice;

--
-- TOC entry 265 (class 1259 OID 305982)
-- Name: procedimento_uffici_partecipanti; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE procedimento_uffici_partecipanti (
    puv_id integer NOT NULL,
    procedimento_id integer,
    ufficio_id integer,
    flag_visibilita numeric(1,0) DEFAULT 1 NOT NULL,
    versione integer DEFAULT 0 NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32)
);


ALTER TABLE procedimento_uffici_partecipanti OWNER TO fenice;

--
-- TOC entry 197 (class 1259 OID 303502)
-- Name: profili; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE profili (
    profilo_id integer NOT NULL,
    codi_profilo character varying(50) NOT NULL,
    desc_profilo character varying(200) NOT NULL,
    data_inizio_validita timestamp without time zone NOT NULL,
    data_fine_validita timestamp without time zone,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer DEFAULT 0 NOT NULL,
    aoo_id integer DEFAULT 1 NOT NULL
);


ALTER TABLE profili OWNER TO fenice;

--
-- TOC entry 198 (class 1259 OID 303509)
-- Name: profili$menu; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE "profili$menu" (
    profilo_id integer NOT NULL,
    menu_id integer NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer DEFAULT 0 NOT NULL
);


ALTER TABLE "profili$menu" OWNER TO fenice;

--
-- TOC entry 199 (class 1259 OID 303515)
-- Name: protocolli; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE protocolli (
    protocollo_id integer NOT NULL,
    anno_registrazione integer NOT NULL,
    nume_protocollo integer,
    data_registrazione timestamp without time zone NOT NULL,
    flag_tipo_mittente character(1),
    flag_tipo character(1) NOT NULL,
    data_documento timestamp without time zone,
    tipo_documento_id integer,
    registro_id integer NOT NULL,
    titolario_id integer,
    ufficio_protocollatore_id integer,
    data_scadenza timestamp without time zone,
    data_effettiva_registrazione timestamp without time zone DEFAULT now(),
    data_ricezione timestamp without time zone,
    data_protocollo_mittente timestamp without time zone,
    nume_protocollo_mittente character varying(50),
    desc_denominazione_mittente character varying(250),
    indi_mittente character varying(250),
    indi_cap_mittente character varying(20),
    indi_localita_mittente character varying(100),
    indi_provincia_mittente character varying(20),
    indi_nazione_mittente character varying(20),
    data_annullamento timestamp without time zone,
    data_scarico timestamp without time zone,
    stato_protocollo character(1) DEFAULT 'S'::bpchar NOT NULL,
    text_nota_annullamento character varying(2000),
    text_provvedimento_annullament character varying(2000),
    codi_documento_doc character varying(64),
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    utente_mittente_id integer,
    versione integer DEFAULT 0 NOT NULL,
    annotazione_chiave character varying(255),
    annotazione_posizione character varying(10),
    annotazione_descrizione character varying(2000),
    aoo_id integer NOT NULL,
    documento_id integer,
    ufficio_mittente_id integer,
    flag_scarto character(1) DEFAULT '0'::bpchar,
    flag_mozione numeric(1,0) DEFAULT 0,
    flag_riservato numeric(1,0) DEFAULT 0,
    num_prot_emergenza integer DEFAULT 0,
    registro_anno_numero numeric(18,0) NOT NULL,
    text_estremi_autorizzazione text,
    intervallo_emergenza character varying(100),
    text_oggetto text,
    desc_cognome_mittente character varying(250),
    desc_nome_mittente character varying(200),
    carica_protocollatore_id integer,
    giorni_alert integer,
    numero_email integer,
    flag_fattura_elettronica numeric(1,0) DEFAULT 0,
    utente_protocollatore_id integer,
    flag_repertorio numeric(1,0) DEFAULT 0,
    flag_anomalia numeric(1,0) DEFAULT 0,
    CONSTRAINT protocolli_flag_tipo_check CHECK (((((flag_tipo = 'I'::bpchar) OR (flag_tipo = 'U'::bpchar)) OR (flag_tipo = 'P'::bpchar)) OR (flag_tipo = 'R'::bpchar))),
    CONSTRAINT protocolli_flag_tipo_mittente_check CHECK ((((flag_tipo_mittente = 'F'::bpchar) OR (flag_tipo_mittente = 'G'::bpchar)) OR (flag_tipo_mittente = 'M'::bpchar)))
);


ALTER TABLE protocolli OWNER TO fenice;

--
-- TOC entry 3197 (class 0 OID 0)
-- Dependencies: 199
-- Name: COLUMN protocolli.documento_id; Type: COMMENT; Schema: fenice; Owner: fenice
--

COMMENT ON COLUMN protocolli.documento_id IS 'identifica, se presente, il documento principale del nostro protocollo';


--
-- TOC entry 200 (class 1259 OID 303532)
-- Name: protocollo_allacci; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE protocollo_allacci (
    allaccio_id integer NOT NULL,
    protocollo_id integer NOT NULL,
    protocollo_allacciato_id integer NOT NULL,
    versione integer DEFAULT 0 NOT NULL,
    flag_principale numeric(1,0) DEFAULT 0
);


ALTER TABLE protocollo_allacci OWNER TO fenice;

--
-- TOC entry 201 (class 1259 OID 303537)
-- Name: protocollo_allegati; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE protocollo_allegati (
    allegato_id integer NOT NULL,
    protocollo_id integer,
    documento_id integer NOT NULL,
    versione integer DEFAULT 0 NOT NULL
);


ALTER TABLE protocollo_allegati OWNER TO fenice;

--
-- TOC entry 202 (class 1259 OID 303541)
-- Name: protocollo_annotazioni; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE protocollo_annotazioni (
    annotazione_id integer NOT NULL,
    protocollo_id integer NOT NULL,
    codi_annotazione numeric(4,0) NOT NULL,
    desc_annotazione character varying(1024),
    codi_userid character varying(32) NOT NULL,
    data_annotazione timestamp without time zone NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer DEFAULT 0 NOT NULL
);


ALTER TABLE protocollo_annotazioni OWNER TO fenice;

--
-- TOC entry 203 (class 1259 OID 303550)
-- Name: protocollo_assegnatari; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE protocollo_assegnatari (
    assegnatario_id integer NOT NULL,
    protocollo_id integer,
    data_assegnazione timestamp without time zone NOT NULL,
    data_operazione timestamp without time zone,
    stat_assegnazione character(1) DEFAULT 'S'::bpchar NOT NULL,
    ufficio_assegnante_id integer,
    ufficio_assegnatario_id integer,
    utente_assegnatario_id integer,
    utente_assegnante_id integer,
    versione integer DEFAULT 0 NOT NULL,
    flag_competente numeric(1,0) DEFAULT 0,
    messaggio character varying(255),
    carica_assegnatario_id integer,
    carica_assegnante_id integer,
    check_presa_visione numeric(1,0),
    check_lavorato integer DEFAULT 0 NOT NULL,
    flag_titolare_procedimento integer DEFAULT 0 NOT NULL,
    CONSTRAINT protocollo_assegnatari_stat_assegnazione_check CHECK ((((stat_assegnazione = 'S'::bpchar) OR (stat_assegnazione = 'A'::bpchar)) OR (stat_assegnazione = 'R'::bpchar)))
);


ALTER TABLE protocollo_assegnatari OWNER TO fenice;

--
-- TOC entry 204 (class 1259 OID 303557)
-- Name: protocollo_destinatari; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE protocollo_destinatari (
    destinatario_id integer NOT NULL,
    flag_tipo_destinatario character(1) NOT NULL,
    indirizzo character varying(100),
    email character varying(100),
    destinatario character varying(160),
    mezzo_spedizione character varying(50),
    citta character varying(100),
    data_spedizione timestamp without time zone,
    flag_conoscenza character(1) DEFAULT '0'::bpchar NOT NULL,
    protocollo_id integer,
    data_effettiva_spedizione timestamp without time zone,
    versione integer DEFAULT 0 NOT NULL,
    titolo_id integer,
    note character varying(500),
    mezzo_spedizione_id integer,
    codice_postale character varying(5),
    flag_presso character(1),
    flag_pec character(1),
    prezzo_spedizione character varying(10),
    CONSTRAINT protocollo_destinatari_flag_conoscenza_check CHECK (((flag_conoscenza = '1'::bpchar) OR (flag_conoscenza = '0'::bpchar))),
    CONSTRAINT protocollo_destinatari_flag_tipo_destinatario_check CHECK ((((flag_tipo_destinatario = 'F'::bpchar) OR (flag_tipo_destinatario = 'G'::bpchar)) OR (flag_tipo_destinatario = 'A'::bpchar)))
);


ALTER TABLE protocollo_destinatari OWNER TO fenice;

--
-- TOC entry 205 (class 1259 OID 303567)
-- Name: protocollo_mittenti; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE protocollo_mittenti (
    mittente_id integer NOT NULL,
    indirizzo character varying(100),
    email character varying(100),
    citta character varying(100),
    provincia character varying(100),
    protocollo_id integer,
    versione integer DEFAULT 0 NOT NULL,
    codice_postale character varying(5),
    descrizione text,
    tipo character varying(20) DEFAULT 'F'::character varying NOT NULL,
    civico character varying(50),
    soggetto_id integer
);


ALTER TABLE protocollo_mittenti OWNER TO fenice;

--
-- TOC entry 206 (class 1259 OID 303574)
-- Name: protocollo_procedimenti; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE protocollo_procedimenti (
    procedimento_id integer NOT NULL,
    protocollo_id integer NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer NOT NULL,
    flag_sospeso integer DEFAULT 0 NOT NULL
);


ALTER TABLE protocollo_procedimenti OWNER TO fenice;

--
-- TOC entry 207 (class 1259 OID 303579)
-- Name: province; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE province (
    provincia_id integer NOT NULL,
    codi_provincia character varying(2) NOT NULL,
    desc_provincia character varying(50) NOT NULL,
    regione_id integer NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now()
);


ALTER TABLE province OWNER TO fenice;

--
-- TOC entry 208 (class 1259 OID 303587)
-- Name: regioni; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE regioni (
    regione_id integer NOT NULL,
    desc_regione character varying(50) NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now()
);


ALTER TABLE regioni OWNER TO fenice;

--
-- TOC entry 209 (class 1259 OID 303592)
-- Name: registri; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE registri (
    registro_id integer NOT NULL,
    codi_registro character varying(30) NOT NULL,
    desc_registro character varying(100),
    nume_ultimo_progressivo integer,
    nume_ultimo_progr_interno integer,
    nume_ultimo_fascicolo integer,
    data_apertura_registro timestamp without time zone NOT NULL,
    aoo_id integer NOT NULL,
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer DEFAULT 0 NOT NULL,
    flag_ufficiale numeric(1,0) DEFAULT 0,
    flag_aperto_ingresso numeric(1,0) DEFAULT 0,
    flag_aperto_uscita numeric(1,0) DEFAULT 0,
    flag_data_bloccata numeric(1,0) DEFAULT 0
);


ALTER TABLE registri OWNER TO fenice;

--
-- TOC entry 260 (class 1259 OID 305730)
-- Name: repertori; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE repertori (
    repertorio_id integer NOT NULL,
    aoo_id integer,
    descrizione character varying(500),
    ufficio_responsabile_id integer NOT NULL,
    flag_web numeric(1,0) DEFAULT 0
);


ALTER TABLE repertori OWNER TO fenice;

--
-- TOC entry 243 (class 1259 OID 305087)
-- Name: riferimenti_legislativi; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE riferimenti_legislativi (
    riferimento_id integer NOT NULL,
    tipo_procedimenti_id integer,
    documento_id integer NOT NULL,
    procedimento_id integer
);


ALTER TABLE riferimenti_legislativi OWNER TO fenice;

--
-- TOC entry 210 (class 1259 OID 303601)
-- Name: ripetidati; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE ripetidati (
    utente_id integer NOT NULL,
    oggetto character varying(2000),
    check_oggetto character(1),
    tipodocumento integer,
    check_tipodocumento character(1),
    data_documento character(1),
    check_data_documento character(1),
    ricevuto_il character(1),
    check_ricevuto_il character(1),
    tipo_mittente character(1),
    check_tipo_mittente character(1),
    mittente character varying(100),
    check_mittente character(1),
    destinatario character varying(150),
    check_destinatario character(1),
    assegnatario_utente_id integer,
    check_assegnatario character(1),
    assegnatario_ufficio_id integer,
    titolario integer,
    check_titolario character(1),
    parametri_stampante character varying(200)
);


ALTER TABLE ripetidati OWNER TO fenice;

--
-- TOC entry 211 (class 1259 OID 303607)
-- Name: rubrica; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE rubrica (
    rubrica_id integer NOT NULL,
    text_note character varying(500),
    indi_web character varying(50),
    indi_email character varying(100),
    indi_cap character varying(8),
    indi_comune character varying(50) NOT NULL,
    flag_is_folder character(1) DEFAULT '0'::bpchar NOT NULL,
    aoo_id integer NOT NULL,
    flag_settore numeric(1,0),
    desc_qualifica character varying(30),
    data_nascita timestamp without time zone,
    sesso character varying(1),
    codi_partita_iva character varying(11),
    desc_comune_nascita character varying(50),
    pers_referente character varying(90),
    tele_referente character varying(16),
    codi_stato_civile character varying(10),
    provincia_nascita_id integer,
    provincia_id integer,
    flag_tipo_rubrica character varying(20) NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer DEFAULT 0 NOT NULL,
    desc_ditta character varying(500),
    pers_cognome character varying(500),
    pers_nome character varying(500),
    tele_telefono character varying(50),
    tele_fax character varying(50),
    codi_fiscale character varying(50),
    codi_codice character varying(50),
    indi_dug character varying(50),
    indi_toponimo character varying(50),
    indi_civico character varying(50),
    CONSTRAINT rubrica_flag_is_folder_check CHECK ((((flag_is_folder)::text = (0)::text) OR ((flag_is_folder)::text = (1)::text))),
    CONSTRAINT rubrica_flag_tipo_rubrica_check CHECK ((((flag_tipo_rubrica)::text = 'F'::text) OR ((flag_tipo_rubrica)::text = 'G'::text)))
);


ALTER TABLE rubrica OWNER TO fenice;

--
-- TOC entry 212 (class 1259 OID 303619)
-- Name: rubrica_lista_distribuzione; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE rubrica_lista_distribuzione (
    id_lista integer NOT NULL,
    id_rubrica integer NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    tipo_soggetto character varying(1) NOT NULL
);


ALTER TABLE rubrica_lista_distribuzione OWNER TO fenice;

--
-- TOC entry 213 (class 1259 OID 303624)
-- Name: segnature; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE segnature (
    segnature_id integer NOT NULL,
    protocollo_id integer NOT NULL,
    tipo_protocollo character(1),
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer DEFAULT 0 NOT NULL,
    text_segnatura text
);


ALTER TABLE segnature OWNER TO fenice;

--
-- TOC entry 214 (class 1259 OID 303633)
-- Name: spedizioni; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE spedizioni (
    spedizioni_id integer NOT NULL,
    codi_spedizione character varying(5) NOT NULL,
    desc_spedizione character varying(50) NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer DEFAULT 0 NOT NULL,
    flag_abilitato integer DEFAULT 1 NOT NULL,
    flag_cancellabile integer DEFAULT 1 NOT NULL,
    aoo_id integer DEFAULT 1 NOT NULL,
    prezzo character varying(10)
);


ALTER TABLE spedizioni OWNER TO fenice;

--
-- TOC entry 257 (class 1259 OID 305570)
-- Name: storia_amministrazione; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE storia_amministrazione (
    amministrazione_id integer NOT NULL,
    codi_amministrazione character varying(20) NOT NULL,
    desc_amministrazione character varying(254) NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer DEFAULT 0 NOT NULL,
    flag_ldap character(1) DEFAULT 0 NOT NULL,
    ldap_versione integer,
    ldap_porta integer DEFAULT 389,
    ldap_use_ssl character(1) DEFAULT 0,
    ldap_host character varying(256),
    ldap_dn character varying(256),
    path_doc character varying(255) NOT NULL,
    path_doc_protocollo character varying(255),
    flag_reg_separato character(1) DEFAULT 1 NOT NULL,
    ftp_host character varying(100),
    ftp_port integer DEFAULT 9990,
    ftp_user character varying(20),
    ftp_pass character varying(20),
    id_unita_amministrativa integer DEFAULT 0,
    versione_corrente_fenice character varying(10)
);


ALTER TABLE storia_amministrazione OWNER TO fenice;

--
-- TOC entry 254 (class 1259 OID 305444)
-- Name: storia_cariche; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE storia_cariche (
    carica_id integer NOT NULL,
    denominazione character varying(160),
    ufficio_id integer NOT NULL,
    profilo_id integer,
    utente_id integer,
    versione integer DEFAULT 0 NOT NULL,
    flag_attivo numeric(1,0) DEFAULT 1 NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    flag_dirigente numeric(1,0) DEFAULT 0,
    flag_referente numeric(1,0) DEFAULT 0,
    flag_responsabile_ente numeric(1,0) DEFAULT 0,
    flag_referente_ufficio_protocollo numeric(1,0)
);


ALTER TABLE storia_cariche OWNER TO fenice;

--
-- TOC entry 246 (class 1259 OID 305198)
-- Name: storia_doc_file_attr; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE storia_doc_file_attr (
    dfa_id integer NOT NULL,
    dc_id integer NOT NULL,
    dfr_id integer,
    nome character varying(100) NOT NULL,
    versione integer NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(150),
    data_documento timestamp without time zone DEFAULT now(),
    oggetto character varying(255),
    note character varying(255),
    descrizione character varying(255),
    descrizione_argomento character varying(255),
    tipo_documento_id integer,
    titolario_id integer,
    stato_lav character(1) DEFAULT 0 NOT NULL,
    stato_arc character(1) DEFAULT 'L'::bpchar NOT NULL,
    owner_id integer NOT NULL,
    row_updated_time timestamp without time zone DEFAULT now(),
    carica_lav_id integer,
    procedimento_id integer
);


ALTER TABLE storia_doc_file_attr OWNER TO fenice;

--
-- TOC entry 247 (class 1259 OID 305211)
-- Name: storia_doc_file_permessi; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE storia_doc_file_permessi (
    dfp_id integer NOT NULL,
    dfa_id integer NOT NULL,
    tipo_permesso integer NOT NULL,
    ufficio_id integer,
    versione integer DEFAULT 0 NOT NULL,
    msg character varying(255),
    carica_id integer
);


ALTER TABLE storia_doc_file_permessi OWNER TO fenice;

--
-- TOC entry 249 (class 1259 OID 305245)
-- Name: storia_documenti_editor; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE storia_documenti_editor (
    documento_id integer NOT NULL,
    txt text,
    nome character varying(50),
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    carica_id integer NOT NULL,
    flag_stato integer NOT NULL,
    versione integer NOT NULL,
    oggetto text,
    flag_tipo integer DEFAULT 0 NOT NULL,
    msg_carica character varying(2000)
);


ALTER TABLE storia_documenti_editor OWNER TO fenice;

--
-- TOC entry 215 (class 1259 OID 303678)
-- Name: storia_faldone_fascicoli; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE storia_faldone_fascicoli (
    faldone_id integer NOT NULL,
    fascicolo_id integer NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer NOT NULL
);


ALTER TABLE storia_faldone_fascicoli OWNER TO fenice;

--
-- TOC entry 216 (class 1259 OID 303683)
-- Name: storia_faldoni; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE storia_faldoni (
    faldone_id integer NOT NULL,
    aoo_id integer NOT NULL,
    oggetto character varying(500) NOT NULL,
    ufficio_id integer NOT NULL,
    titolario_id integer,
    row_created_time timestamp without time zone DEFAULT ('now'::text)::timestamp(6) with time zone,
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT ('now'::text)::timestamp(6) with time zone,
    codice_locale character varying(20),
    sotto_categoria character varying(250),
    nota character varying(250),
    numero_faldone character varying(11) NOT NULL,
    anno integer DEFAULT 0 NOT NULL,
    numero integer DEFAULT 0 NOT NULL,
    data_creazione date NOT NULL,
    data_carico timestamp without time zone,
    data_scarico timestamp without time zone,
    data_evidenza timestamp without time zone,
    data_movimento timestamp without time zone,
    stato_id integer,
    versione integer DEFAULT 0 NOT NULL,
    collocazione_label1 character varying(200),
    collocazione_label2 character varying(200),
    collocazione_label3 character varying(200),
    collocazione_label4 character varying(200),
    collocazione_valore1 character varying(200),
    collocazione_valore2 character varying(200),
    collocazione_valore3 character varying(200),
    collocazione_valore4 character varying(200)
);


ALTER TABLE storia_faldoni OWNER TO fenice;

--
-- TOC entry 217 (class 1259 OID 303694)
-- Name: storia_fascicoli; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE storia_fascicoli (
    fascicolo_id integer NOT NULL,
    aoo_id integer,
    codice character varying(10),
    progressivo numeric(18,0),
    collocazione character varying(100),
    note character varying(1024),
    processo_id integer,
    registro_id integer,
    data_apertura timestamp without time zone,
    data_chiusura timestamp without time zone,
    stato integer,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    titolario_id integer,
    versione integer DEFAULT 0 NOT NULL,
    ufficio_intestatario_id integer,
    ufficio_responsabile_id integer,
    data_evidenza timestamp without time zone,
    anno_riferimento integer,
    tipo integer DEFAULT 0 NOT NULL,
    data_ultimo_movimento timestamp without time zone,
    data_scarto timestamp without time zone,
    data_carico timestamp without time zone,
    data_scarico timestamp without time zone,
    collocazione_label1 character varying(200),
    collocazione_label2 character varying(200),
    collocazione_label3 character varying(200),
    collocazione_label4 character varying(200),
    collocazione_valore1 character varying(200),
    collocazione_valore2 character varying(200),
    collocazione_valore3 character varying(200),
    collocazione_valore4 character varying(200),
    parent_id integer,
    giorni_max integer,
    giorni_alert integer,
    oggetto text,
    descrizione text,
    nome text,
    carica_istruttore_id integer,
    carica_responsabile_id integer,
    carica_intestatario_id integer,
    comune character varying(50),
    capitolo character varying(100),
    path_progressivo character varying(100),
    delegato character varying(200),
    interessato character varying(200),
    indi_delegato character varying(250),
    indi_interessato character varying(250)
);


ALTER TABLE storia_fascicoli OWNER TO fenice;

--
-- TOC entry 218 (class 1259 OID 303704)
-- Name: storia_fascicolo_documenti; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE storia_fascicolo_documenti (
    dfa_id integer NOT NULL,
    fascicolo_id integer NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer NOT NULL,
    ufficio_proprietario_id integer
);


ALTER TABLE storia_fascicolo_documenti OWNER TO fenice;

--
-- TOC entry 219 (class 1259 OID 303709)
-- Name: storia_fascicolo_protocolli; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE storia_fascicolo_protocolli (
    protocollo_id integer NOT NULL,
    fascicolo_id integer NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer DEFAULT 0 NOT NULL,
    ufficio_proprietario_id integer
);


ALTER TABLE storia_fascicolo_protocolli OWNER TO fenice;

--
-- TOC entry 220 (class 1259 OID 303715)
-- Name: storia_procedimenti; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE storia_procedimenti (
    procedimento_id integer NOT NULL,
    data_avvio timestamp without time zone NOT NULL,
    ufficio_id integer NOT NULL,
    stato_id integer NOT NULL,
    tipo_finalita_id integer NOT NULL,
    oggetto text NOT NULL,
    referente_id integer,
    posizione_id character varying(1),
    data_evidenza timestamp without time zone,
    note character varying(1000),
    protocollo_id integer NOT NULL,
    numero_procedimento character varying(100) NOT NULL,
    anno integer DEFAULT 0 NOT NULL,
    numero integer DEFAULT 0 NOT NULL,
    row_created_time timestamp without time zone DEFAULT ('now'::text)::timestamp(6) with time zone,
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT ('now'::text)::timestamp(6) with time zone,
    versione integer DEFAULT 0 NOT NULL,
    aoo_id integer NOT NULL,
    giorni_alert integer,
    giorni_max integer,
    tipo_procedimento_id integer,
    responsabile_id integer,
    fascicolo_id integer,
    indicazioni text,
    estremi_provvedimento character varying(250),
    carica_lav_id integer,
    estremi_sospensione text,
    data_sospensione timestamp without time zone,
    data_scadenza timestamp without time zone,
    flag_sospeso numeric(1,0) DEFAULT 0,
    autorita character varying(500),
    delegato character varying(200),
    interessato character varying(200),
    indi_delegato character varying(250),
    indi_interessato character varying(250)
);


ALTER TABLE storia_procedimenti OWNER TO fenice;

--
-- TOC entry 221 (class 1259 OID 303726)
-- Name: storia_procedimenti_fascicolo; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE storia_procedimenti_fascicolo (
    procedimento_id integer NOT NULL,
    fascicolo_id integer NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer NOT NULL
);


ALTER TABLE storia_procedimenti_fascicolo OWNER TO fenice;

--
-- TOC entry 255 (class 1259 OID 305486)
-- Name: storia_procedimento_istruttori; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE storia_procedimento_istruttori (
    procedimento_id integer NOT NULL,
    carica_id integer NOT NULL,
    versione integer NOT NULL,
    flag_lavorato numeric(1,0) DEFAULT 0,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32)
);


ALTER TABLE storia_procedimento_istruttori OWNER TO fenice;

--
-- TOC entry 222 (class 1259 OID 303731)
-- Name: storia_protocolli; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE storia_protocolli (
    protocollo_id integer NOT NULL,
    anno_registrazione integer NOT NULL,
    nume_protocollo integer,
    data_registrazione timestamp without time zone NOT NULL,
    flag_tipo_mittente character(1),
    flag_tipo character(1) NOT NULL,
    data_documento timestamp without time zone,
    tipo_documento_id integer,
    registro_id integer NOT NULL,
    titolario_id integer,
    ufficio_protocollatore_id integer,
    data_scadenza timestamp without time zone,
    data_effettiva_registrazione timestamp without time zone,
    data_ricezione timestamp without time zone,
    data_protocollo_mittente timestamp without time zone,
    nume_protocollo_mittente character varying(50),
    desc_denominazione_mittente character varying(250),
    indi_mittente character varying(250),
    indi_cap_mittente character varying(20),
    indi_localita_mittente character varying(100),
    indi_provincia_mittente character varying(20),
    indi_nazione_mittente character varying(20),
    data_annullamento timestamp without time zone,
    data_scarico timestamp without time zone,
    stato_protocollo character(1) DEFAULT 'S'::bpchar NOT NULL,
    text_nota_annullamento character varying(2000),
    text_provvedimento_annullament character varying(2000),
    codi_documento_doc character varying(64),
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    utente_mittente_id integer,
    versione integer DEFAULT 0 NOT NULL,
    annotazione_chiave character varying(255),
    annotazione_posizione character varying(10),
    annotazione_descrizione character varying(2000),
    aoo_id integer NOT NULL,
    documento_id integer,
    ufficio_mittente_id integer,
    flag_scarto character(1) DEFAULT '0'::bpchar NOT NULL,
    flag_mozione numeric(1,0) DEFAULT 0,
    flag_riservato numeric(1,0) DEFAULT 0,
    num_prot_emergenza integer DEFAULT 0,
    registro_anno_numero numeric(18,0) NOT NULL,
    text_estremi_autorizzazione text,
    intervallo_emergenza character varying(100),
    text_oggetto text,
    desc_cognome_mittente character varying(250),
    desc_nome_mittente character varying(200),
    carica_protocollatore_id integer,
    giorni_alert integer,
    numero_email integer,
    flag_fattura_elettronica numeric(1,0) DEFAULT 0,
    utente_protocollatore_id integer,
    flag_repertorio numeric(1,0) DEFAULT 0,
    flag_anomalia numeric(1,0) DEFAULT 0,
    CONSTRAINT storia_protocolli_flag_scarto_check CHECK (((flag_scarto = '0'::bpchar) OR (flag_scarto = '1'::bpchar))),
    CONSTRAINT storia_protocolli_flag_tipo_check CHECK (((((flag_tipo = 'I'::bpchar) OR (flag_tipo = 'U'::bpchar)) OR (flag_tipo = 'P'::bpchar)) OR (flag_tipo = 'R'::bpchar)))
);


ALTER TABLE storia_protocolli OWNER TO fenice;

--
-- TOC entry 223 (class 1259 OID 303748)
-- Name: storia_protocollo_allacci; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE storia_protocollo_allacci (
    allaccio_id integer NOT NULL,
    protocollo_id integer NOT NULL,
    protocollo_allacciato_id integer NOT NULL,
    versione integer DEFAULT 0 NOT NULL,
    flag_principale numeric(1,0) DEFAULT 0
);


ALTER TABLE storia_protocollo_allacci OWNER TO fenice;

--
-- TOC entry 224 (class 1259 OID 303753)
-- Name: storia_protocollo_allegati; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE storia_protocollo_allegati (
    allegato_id integer NOT NULL,
    protocollo_id integer,
    documento_id integer NOT NULL,
    versione integer DEFAULT 0 NOT NULL
);


ALTER TABLE storia_protocollo_allegati OWNER TO fenice;

--
-- TOC entry 225 (class 1259 OID 303757)
-- Name: storia_protocollo_annotazioni; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE storia_protocollo_annotazioni (
    annotazione_id integer NOT NULL,
    protocollo_id integer NOT NULL,
    codi_annotazione numeric(4,0) NOT NULL,
    desc_annotazione character varying(1024),
    codi_userid character varying(32) NOT NULL,
    data_annotazione timestamp without time zone NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer DEFAULT 0 NOT NULL
);


ALTER TABLE storia_protocollo_annotazioni OWNER TO fenice;

--
-- TOC entry 226 (class 1259 OID 303766)
-- Name: storia_protocollo_assegnatari; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE storia_protocollo_assegnatari (
    assegnatario_id integer NOT NULL,
    protocollo_id integer,
    data_assegnazione timestamp without time zone NOT NULL,
    data_operazione timestamp without time zone,
    stat_assegnazione character(1) DEFAULT 'S'::bpchar NOT NULL,
    ufficio_assegnante_id integer,
    ufficio_assegnatario_id integer,
    utente_assegnatario_id integer,
    utente_assegnante_id integer,
    versione integer DEFAULT 0 NOT NULL,
    flag_competente numeric(1,0) DEFAULT 0,
    messaggio character varying(255),
    carica_assegnatario_id integer,
    carica_assegnante_id integer,
    check_presa_visione numeric(1,0),
    check_lavorato integer DEFAULT 0 NOT NULL,
    flag_titolare_procedimento integer DEFAULT 0 NOT NULL,
    CONSTRAINT storia_protocollo_assegnatari_stat_assegnazione_check CHECK ((((stat_assegnazione = 'S'::bpchar) OR (stat_assegnazione = 'A'::bpchar)) OR (stat_assegnazione = 'R'::bpchar)))
);


ALTER TABLE storia_protocollo_assegnatari OWNER TO fenice;

--
-- TOC entry 227 (class 1259 OID 303773)
-- Name: storia_protocollo_destinatari; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE storia_protocollo_destinatari (
    destinatario_id integer NOT NULL,
    flag_tipo_destinatario character(1) NOT NULL,
    indirizzo character varying(100),
    email character varying(100),
    destinatario character varying(160),
    mezzo_spedizione character varying(50),
    citta character varying(100),
    data_spedizione timestamp without time zone,
    flag_conoscenza character(1) DEFAULT '0'::bpchar NOT NULL,
    protocollo_id integer,
    data_effettiva_spedizione timestamp without time zone,
    versione integer DEFAULT 0 NOT NULL,
    titolo_id integer,
    note character varying(500),
    mezzo_spedizione_id integer,
    codice_postale character varying(5),
    flag_presso character(1),
    flag_pec character(1),
    prezzo_spedizione character varying(10),
    CONSTRAINT storia_protocollo_destinatari_flag_conoscenza_check CHECK (((flag_conoscenza = '1'::bpchar) OR (flag_conoscenza = '0'::bpchar))),
    CONSTRAINT storia_protocollo_destinatari_flag_tipo_destinatario_check CHECK ((((flag_tipo_destinatario = 'F'::bpchar) OR (flag_tipo_destinatario = 'G'::bpchar)) OR (flag_tipo_destinatario = 'A'::bpchar)))
);


ALTER TABLE storia_protocollo_destinatari OWNER TO fenice;

--
-- TOC entry 242 (class 1259 OID 305061)
-- Name: storia_protocollo_mittenti; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE storia_protocollo_mittenti (
    mittente_id integer NOT NULL,
    indirizzo character varying(100),
    email character varying(100),
    citta character varying(100),
    provincia character varying(100),
    protocollo_id integer,
    versione integer DEFAULT 0 NOT NULL,
    codice_postale character varying(5),
    descrizione text,
    tipo character varying(20) DEFAULT 'F'::character varying NOT NULL,
    civico character varying(50),
    soggetto_id integer
);


ALTER TABLE storia_protocollo_mittenti OWNER TO fenice;

--
-- TOC entry 228 (class 1259 OID 303783)
-- Name: storia_protocollo_procedimenti; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE storia_protocollo_procedimenti (
    procedimento_id integer NOT NULL,
    protocollo_id integer NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer NOT NULL,
    flag_sospeso integer DEFAULT 0 NOT NULL
);


ALTER TABLE storia_protocollo_procedimenti OWNER TO fenice;

--
-- TOC entry 229 (class 1259 OID 303788)
-- Name: storia_registri; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE storia_registri (
    registri_id integer NOT NULL,
    codi_registro character varying(30) NOT NULL,
    desc_registro character varying(100),
    nume_ultimo_progressivo integer,
    nume_ultimo_progr_interno integer,
    nume_ultimo_fascicolo integer,
    data_apertura_registro timestamp without time zone NOT NULL,
    aoo_id integer NOT NULL,
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer DEFAULT 0 NOT NULL,
    flag_ufficiale numeric(1,0) DEFAULT 0,
    flag_aperto_ingresso numeric(1,0) DEFAULT 0,
    flag_aperto_uscita numeric(1,0) DEFAULT 0,
    flag_data_bloccata numeric(1,0) DEFAULT 0
);


ALTER TABLE storia_registri OWNER TO fenice;

--
-- TOC entry 230 (class 1259 OID 303797)
-- Name: storia_titolario; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE storia_titolario (
    titolario_id integer NOT NULL,
    parent_id integer,
    aoo_id integer NOT NULL,
    desc_titolario character varying(255),
    parent_full_desc character varying(500),
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    versione integer DEFAULT 0 NOT NULL,
    massimario integer,
    responsabile_id integer,
    ufficio_responsabile_id integer
);


ALTER TABLE storia_titolario OWNER TO fenice;

--
-- TOC entry 256 (class 1259 OID 305504)
-- Name: storico_organigramma; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE storico_organigramma (
    st_org_id integer NOT NULL,
    aoo_id integer NOT NULL,
    descrizione text,
    data bytea,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32)
);


ALTER TABLE storico_organigramma OWNER TO fenice;

--
-- TOC entry 231 (class 1259 OID 303814)
-- Name: tipi_documento; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE tipi_documento (
    tipo_documento_id integer NOT NULL,
    desc_tipo_documento character varying(254),
    aoo_id integer NOT NULL,
    flag_attivazione character(1) DEFAULT '0'::bpchar,
    nume_gg_scadenza integer,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    flag_default character(1) DEFAULT '0'::bpchar NOT NULL,
    versione integer DEFAULT 0 NOT NULL,
    CONSTRAINT tipi_documento_flag_attivazione_check CHECK ((((flag_attivazione)::text = (1)::text) OR ((flag_attivazione)::text = (0)::text)))
);


ALTER TABLE tipi_documento OWNER TO fenice;

--
-- TOC entry 232 (class 1259 OID 303823)
-- Name: tipi_procedimento; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE tipi_procedimento (
    tipo_procedimenti_id integer NOT NULL,
    tipo text NOT NULL,
    giorni_max integer,
    giorni_alert integer,
    titolario_id integer,
    flag_ull numeric(1,0) DEFAULT 0
);


ALTER TABLE tipi_procedimento OWNER TO fenice;

--
-- TOC entry 264 (class 1259 OID 305965)
-- Name: tipi_procedimento_uffici; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE tipi_procedimento_uffici (
    tpu_id integer NOT NULL,
    tipo_procedimenti_id integer,
    ufficio_id integer,
    versione integer DEFAULT 0 NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    flag_principale numeric(1,0) DEFAULT 0 NOT NULL
);


ALTER TABLE tipi_procedimento_uffici OWNER TO fenice;

--
-- TOC entry 233 (class 1259 OID 303826)
-- Name: titolario; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE titolario (
    titolario_id integer NOT NULL,
    codi_titolario character varying(20) NOT NULL,
    desc_titolario character varying(255) NOT NULL,
    parent_id integer,
    aoo_id integer NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer DEFAULT 0 NOT NULL,
    path_titolario character varying(500),
    massimario integer,
    ufficio_id integer,
    utente_id integer,
    responsabile_id integer,
    ufficio_responsabile_id integer
);


ALTER TABLE titolario OWNER TO fenice;

--
-- TOC entry 234 (class 1259 OID 303835)
-- Name: titolario$uffici; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE "titolario$uffici" (
    ufficio_id integer NOT NULL,
    titolario_id integer NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer DEFAULT 0 NOT NULL
);


ALTER TABLE "titolario$uffici" OWNER TO fenice;

--
-- TOC entry 235 (class 1259 OID 303841)
-- Name: titoli_destinatari; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE titoli_destinatari (
    id integer NOT NULL,
    descrizione character varying(255) NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now()
);


ALTER TABLE titoli_destinatari OWNER TO fenice;

--
-- TOC entry 236 (class 1259 OID 303846)
-- Name: uffici; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE uffici (
    ufficio_id integer NOT NULL,
    descrizione character varying(254) NOT NULL,
    parent_id integer,
    aoo_id integer,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer DEFAULT 0 NOT NULL,
    tipo character(1) NOT NULL,
    flag_attivo numeric(1,0) DEFAULT 1,
    flag_accettazione_automatica numeric(1,0) DEFAULT 0,
    indi_email character varying(50),
    telefono character varying(20),
    fax character varying(20),
    piano character varying(2),
    stanza character varying(5),
    email_username character varying(50),
    email_password character varying(50),
    data_ultima_mail timestamp without time zone DEFAULT now(),
    flag_ufficio_protocollo numeric(1,0) DEFAULT 0
);


ALTER TABLE uffici OWNER TO fenice;

--
-- TOC entry 237 (class 1259 OID 303854)
-- Name: utenti; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE utenti (
    utente_id integer NOT NULL,
    user_name character varying(32) NOT NULL,
    email character varying(256),
    cognome character varying(100) NOT NULL,
    nome character varying(40) NOT NULL,
    codicefiscale character varying(16) NOT NULL,
    matricola character varying(10),
    passwd character varying(20) NOT NULL,
    data_fine_attivita timestamp without time zone,
    aoo_id integer,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer DEFAULT 0 NOT NULL,
    flag_abilitato numeric(1,0) DEFAULT 1
);


ALTER TABLE utenti OWNER TO fenice;

--
-- TOC entry 238 (class 1259 OID 303864)
-- Name: utenti$registri; Type: TABLE; Schema: fenice; Owner: fenice
--

CREATE TABLE "utenti$registri" (
    utente_id integer NOT NULL,
    registro_id integer NOT NULL,
    row_created_time timestamp without time zone DEFAULT now(),
    row_created_user character varying(32),
    row_updated_user character varying(32),
    row_updated_time timestamp without time zone DEFAULT now(),
    versione integer DEFAULT 0 NOT NULL
);


ALTER TABLE "utenti$registri" OWNER TO fenice;

--
-- TOC entry 3080 (class 0 OID 303263)
-- Dependencies: 163
-- Data for Name: acquisizione_massiva_logs; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY acquisizione_massiva_logs (nome_file, errore, aoo_id, data_log) FROM stdin;
\.


--
-- TOC entry 3185 (class 0 OID 306072)
-- Dependencies: 268
-- Data for Name: allegati_doc_amm_trasparente; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY allegati_doc_amm_trasparente (dc_id, doc_sezione_id, flag_riservato, tipo, flag_principale, flag_pubblicabile) FROM stdin;
\.


--
-- TOC entry 3176 (class 0 OID 305715)
-- Dependencies: 259
-- Data for Name: allegati_doc_repertorio; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY allegati_doc_repertorio (dc_id, doc_repertorio_id, flag_riservato, tipo, flag_principale, flag_pubblicabile) FROM stdin;
\.


--
-- TOC entry 3183 (class 0 OID 306022)
-- Dependencies: 266
-- Data for Name: amm_trasparente; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY amm_trasparente (sezione_id, aoo_id, descrizione, ufficio_responsabile_id, flag_web) FROM stdin;
\.


--
-- TOC entry 3184 (class 0 OID 306041)
-- Dependencies: 267
-- Data for Name: amm_trasparente_documenti; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY amm_trasparente_documenti (doc_id, note, oggetto, capitolo, importo, data_validita_inizio, data_validita_fine, row_created_time, row_created_user, row_updated_user, row_updated_time, ufficio_id, data_creazione, sez_id, numero_documento_sezione, flag_stato, descrizione, numero_documento, settore_proponente, protocollo_id) FROM stdin;
\.


--
-- TOC entry 3081 (class 0 OID 303270)
-- Dependencies: 164
-- Data for Name: amministrazione; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY amministrazione (amministrazione_id, codi_amministrazione, desc_amministrazione, row_created_time, row_created_user, row_updated_user, row_updated_time, versione, flag_ldap, ldap_versione, ldap_porta, ldap_use_ssl, ldap_host, ldap_dn, path_doc, path_doc_protocollo, flag_reg_separato, ftp_host, ftp_port, ftp_user, ftp_pass, id_unita_amministrativa, versione_corrente_fenice, ftp_folder, flag_web_socket) FROM stdin;
1	CodAmm	Amministrazione	2018-07-13 02:24:52.184	\N	\N	2018-07-13 02:24:52.184	0	0	0	0	\N	\N	\N	/opt/Fenice/documenti_acquisizione_massiva	/opt/Fenice/documenti_protocollo	1	\N	9990	\N	\N	0	1.5.11	\N	0
\.


--
-- TOC entry 3161 (class 0 OID 305107)
-- Dependencies: 244
-- Data for Name: amministrazioni_partecipanti; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY amministrazioni_partecipanti (amministrazioni_id, nominativo, rubrica_id, tipo_procedimenti_id) FROM stdin;
\.


--
-- TOC entry 3082 (class 0 OID 303282)
-- Dependencies: 165
-- Data for Name: aree_organizzative; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY aree_organizzative (aoo_id, codi_aoo, desc_aoo, data_istituzione, responsabile_nome, responsabile_cognome, responsabile_email, responsabile_telefono, data_soppressione, telefono, fax, indi_dug, indi_toponimo, indi_civico, indi_cap, indi_comune, email, dipartimento_codice, dipartimento_descrizione, tipo_aoo, provincia_id, codi_documento_doc, flag_pdf, amministrazione_id, row_created_time, row_created_user, row_updated_user, row_updated_time, nume_blocco_titolario, versione, dipendenza_titolario_ufficio, titolario_livello_minimo, utente_responsabile_id, flag_documento_readable, flag_ricerca_full, fattura_id_committente, ga_abilitata, ga_username, ga_pwd, ga_flag_invio, ga_timer, anni_visibilita_bacheca, max_righe, flag_p7m) FROM stdin;
1	AOOFULL	AOO FULL	\N					\N			dug	toponimo	civico	cap	comune	\N			F	\N	\N	0	1	\N	\N	\N	\N	\N	0	0	0	\N	1	1	\N	0	\N	\N	0	\N	1	1000	0
\.


--
-- TOC entry 3170 (class 0 OID 305420)
-- Dependencies: 253
-- Data for Name: assegnatari_lista_distribuzione; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY assegnatari_lista_distribuzione (id_assegnatario_lista, id_lista, id_carica, id_ufficio, row_created_time, row_created_user, row_updated_user, row_updated_time) FROM stdin;
\.


--
-- TOC entry 3083 (class 0 OID 303298)
-- Dependencies: 166
-- Data for Name: ca_crl; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY ca_crl (ca_id, id, url, tipo, data_emissione, codice_errore) FROM stdin;
\.


--
-- TOC entry 3084 (class 0 OID 303305)
-- Dependencies: 167
-- Data for Name: ca_lista; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY ca_lista (ca_id, issuer_cn, valido_dal, valido_al, certificato) FROM stdin;
\.


--
-- TOC entry 3085 (class 0 OID 303311)
-- Dependencies: 168
-- Data for Name: ca_revoked_list; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY ca_revoked_list (ca_id, data_revoca, serial_number) FROM stdin;
\.


--
-- TOC entry 3157 (class 0 OID 304948)
-- Dependencies: 240
-- Data for Name: cariche; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY cariche (carica_id, denominazione, ufficio_id, profilo_id, utente_id, versione, flag_attivo, row_created_time, row_created_user, flag_dirigente, flag_referente, flag_responsabile_ente, flag_referente_ufficio_protocollo) FROM stdin;
1	ROOT	1	\N	1	0	1	2018-07-13 02:26:20.177	ROOT	0	0	0	\N
\.


--
-- TOC entry 3086 (class 0 OID 303314)
-- Dependencies: 169
-- Data for Name: caselle_email; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY caselle_email (casella_id, server, protocollo_tcp, utente, password, indirizzo, tipo, ssl_porta, flag_ufficiale, fk_aoo, flag_ssl, row_created_time, row_created_user, row_updated_user, row_updated_time, versione) FROM stdin;
\.


--
-- TOC entry 3169 (class 0 OID 305367)
-- Dependencies: 252
-- Data for Name: check_posta_interna; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY check_posta_interna (check_id, protocollo_id, data_operazione, ufficio_assegnante_id, ufficio_assegnatario_id, flag_competente, carica_assegnatario_id, carica_assegnante_id, check_presa_visione) FROM stdin;
\.


--
-- TOC entry 3087 (class 0 OID 303331)
-- Dependencies: 170
-- Data for Name: doc_cartelle; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY doc_cartelle (dc_id, parent_id, nome, aoo_id, is_root, carica_id) FROM stdin;
1	\N	admin	1	1	1
\.


--
-- TOC entry 3088 (class 0 OID 303335)
-- Dependencies: 171
-- Data for Name: doc_file_attr; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY doc_file_attr (dfa_id, dc_id, dfr_id, nome, versione, row_created_time, row_created_user, data_documento, oggetto, note, descrizione, descrizione_argomento, tipo_documento_id, titolario_id, stato_lav, stato_arc, owner_id, row_updated_time, carica_lav_id, procedimento_id) FROM stdin;
\.


--
-- TOC entry 3089 (class 0 OID 303346)
-- Dependencies: 172
-- Data for Name: doc_file_permessi; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY doc_file_permessi (dfp_id, dfa_id, tipo_permesso, ufficio_id, versione, msg, carica_id) FROM stdin;
\.


--
-- TOC entry 3090 (class 0 OID 303350)
-- Dependencies: 173
-- Data for Name: doc_file_rep; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY doc_file_rep (dfr_id, data, filename, content_type, file_size, impronta, row_created_time, row_created_user) FROM stdin;
\.


--
-- TOC entry 3091 (class 0 OID 303357)
-- Dependencies: 174
-- Data for Name: documenti; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY documenti (documento_id, descrizione, filename, content_type, file_size, impronta, data, row_created_time, row_created_user, path) FROM stdin;
\.


--
-- TOC entry 3165 (class 0 OID 305229)
-- Dependencies: 248
-- Data for Name: documenti_editor; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY documenti_editor (documento_id, txt, nome, row_created_time, row_created_user, carica_id, flag_stato, versione, oggetto, flag_tipo, msg_carica) FROM stdin;
\.


--
-- TOC entry 3175 (class 0 OID 305700)
-- Dependencies: 258
-- Data for Name: documenti_repertori; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY documenti_repertori (doc_repertorio_id, note, oggetto, capitolo, importo, data_validita_inizio, data_validita_fine, row_created_time, row_created_user, row_updated_user, row_updated_time, ufficio_id, data_creazione, rep_id, numero_documento_repertorio, flag_stato, descrizione, numero_documento, settore_proponente, protocollo_id) FROM stdin;
\.


--
-- TOC entry 3092 (class 0 OID 303364)
-- Dependencies: 175
-- Data for Name: email_coda_invio; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY email_coda_invio (id, aoo_id, data_creazione, stato, protocollo_id, data_invio) FROM stdin;
\.


--
-- TOC entry 3093 (class 0 OID 303368)
-- Dependencies: 176
-- Data for Name: email_coda_invio_destinatari; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY email_coda_invio_destinatari (msg_id, email, nominativo, tipo) FROM stdin;
\.


--
-- TOC entry 3094 (class 0 OID 303371)
-- Dependencies: 177
-- Data for Name: email_ingresso; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY email_ingresso (email_id, descrizione, filename, content_type, dimensione, impronta, testo_messaggio, row_created_time, row_created_user, email_mittente, nome_mittente, email_oggetto, data_spedizione, data_ricezione, segnatura, flag_stato, aoo_id, flag_anomalia, message_header_id) FROM stdin;
\.


--
-- TOC entry 3095 (class 0 OID 303379)
-- Dependencies: 178
-- Data for Name: email_ingresso_allegati; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY email_ingresso_allegati (id, email_id, descrizione, filename, content_type, dimensione, impronta, data, row_created_time, row_created_user) FROM stdin;
\.


--
-- TOC entry 3096 (class 0 OID 303386)
-- Dependencies: 179
-- Data for Name: email_logs; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY email_logs (email_id, errore, tipo_log, aoo_id, data_log) FROM stdin;
\.


--
-- TOC entry 3097 (class 0 OID 303393)
-- Dependencies: 180
-- Data for Name: faldone_fascicoli; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY faldone_fascicoli (faldone_id, fascicolo_id, row_created_time, row_created_user, row_updated_user, row_updated_time, versione) FROM stdin;
\.


--
-- TOC entry 3098 (class 0 OID 303399)
-- Dependencies: 181
-- Data for Name: faldone_procedimenti; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY faldone_procedimenti (faldone_id, procedimento_id) FROM stdin;
\.


--
-- TOC entry 3099 (class 0 OID 303402)
-- Dependencies: 182
-- Data for Name: faldoni; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY faldoni (faldone_id, aoo_id, oggetto, ufficio_id, titolario_id, row_created_time, row_created_user, row_updated_user, row_updated_time, codice_locale, sotto_categoria, nota, numero_faldone, anno, numero, data_creazione, data_carico, data_scarico, data_evidenza, data_movimento, stato_id, versione, collocazione_label1, collocazione_label2, collocazione_label3, collocazione_label4, collocazione_valore1, collocazione_valore2, collocazione_valore3, collocazione_valore4) FROM stdin;
\.


--
-- TOC entry 3100 (class 0 OID 303413)
-- Dependencies: 183
-- Data for Name: fascicoli; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY fascicoli (fascicolo_id, aoo_id, codice, progressivo, collocazione, note, processo_id, registro_id, data_apertura, data_chiusura, stato, row_created_time, row_created_user, row_updated_user, row_updated_time, titolario_id, versione, ufficio_intestatario_id, ufficio_responsabile_id, data_evidenza, anno_riferimento, tipo, data_ultimo_movimento, data_scarto, data_carico, data_scarico, collocazione_label1, collocazione_label2, collocazione_label3, collocazione_label4, collocazione_valore1, collocazione_valore2, collocazione_valore3, collocazione_valore4, parent_id, giorni_max, giorni_alert, oggetto, descrizione, nome, carica_istruttore_id, carica_responsabile_id, carica_intestatario_id, comune, capitolo, path_progressivo, delegato, interessato, indi_delegato, indi_interessato) FROM stdin;
\.


--
-- TOC entry 3168 (class 0 OID 305293)
-- Dependencies: 251
-- Data for Name: fascicolo_allacci; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY fascicolo_allacci (fascicolo_id, fascicolo_allacciato_id, row_created_user, ufficio_proprietario_id) FROM stdin;
\.


--
-- TOC entry 3101 (class 0 OID 303423)
-- Dependencies: 184
-- Data for Name: fascicolo_documenti; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY fascicolo_documenti (dfa_id, fascicolo_id, row_created_time, row_created_user, row_updated_user, row_updated_time, versione, ufficio_proprietario_id) FROM stdin;
\.


--
-- TOC entry 3102 (class 0 OID 303429)
-- Dependencies: 185
-- Data for Name: fascicolo_protocolli; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY fascicolo_protocolli (protocollo_id, fascicolo_id, row_created_time, row_created_user, row_updated_user, row_updated_time, versione, ufficio_proprietario_id) FROM stdin;
\.


--
-- TOC entry 3103 (class 0 OID 303435)
-- Dependencies: 186
-- Data for Name: identificativi; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY identificativi (nome_tabella, id_corrente) FROM stdin;
AREE_ORGANIZZATIVE	1
DOC_CARTELLE	1
SPEDIZIONI	1
TIPI_DOCUMENTO	3
TITOLI_DESTINATARI	1
UFFICI	1
CARICHE	1
UTENTI	2
REGISTRI	3
ASSEGNATARI_LISTA_DISTRIBUZIONE	0
NOTIFICHE_FATTURA_ELETTRONICA	1
TIPI_PROCEDIMENTI_UFFICI	0
\.


--
-- TOC entry 3104 (class 0 OID 303438)
-- Dependencies: 187
-- Data for Name: invio_classificati; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY invio_classificati (id, dfa_id, aoo_id, ufficio_mittente_id, utente_mittente_id, procedimento_id, data_scadenza, text_scadenza) FROM stdin;
\.


--
-- TOC entry 3105 (class 0 OID 303441)
-- Dependencies: 188
-- Data for Name: invio_classificati_destinatari; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY invio_classificati_destinatari (id, dfa_id, flag_tipo_destinatario, indirizzo, email, destinatario, citta, data_spedizione, flag_conoscenza, data_effettiva_spedizione, versione, titolo_id, mezzo_spedizione) FROM stdin;
\.


--
-- TOC entry 3167 (class 0 OID 305261)
-- Dependencies: 250
-- Data for Name: invio_classificati_fascicoli; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY invio_classificati_fascicoli (id, dfa_id, fascicolo_id) FROM stdin;
\.


--
-- TOC entry 3106 (class 0 OID 303448)
-- Dependencies: 189
-- Data for Name: invio_fascicoli; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY invio_fascicoli (id, fascicolo_id, dfa_id, flag_documento_principale, aoo_id, ufficio_mittente_id, utente_mittente_id) FROM stdin;
\.


--
-- TOC entry 3107 (class 0 OID 303452)
-- Dependencies: 190
-- Data for Name: invio_fascicoli_destinatari; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY invio_fascicoli_destinatari (id, fascicolo_id, flag_tipo_destinatario, indirizzo, email, destinatario, citta, data_spedizione, flag_conoscenza, data_effettiva_spedizione, versione, titolo_id, mezzo_spedizione) FROM stdin;
\.


--
-- TOC entry 3180 (class 0 OID 305903)
-- Dependencies: 263
-- Data for Name: job_scheduled_logs; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY job_scheduled_logs (js_id, message, status, aoo_id, data_log) FROM stdin;
\.


--
-- TOC entry 3108 (class 0 OID 303459)
-- Dependencies: 191
-- Data for Name: lista_distribuzione; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY lista_distribuzione (id, descrizione, row_created_time, row_created_user, row_updated_user, row_updated_time, aoo_id, flag_tipo) FROM stdin;
\.


--
-- TOC entry 3178 (class 0 OID 305805)
-- Dependencies: 261
-- Data for Name: mail_config; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY mail_config (config_id, aoo_id, pec_abilitata, pec_email, pec_username, pec_password, pec_ssl_port, pec_pop3_host, pec_smtp_host, pec_tipo_protocollo, pec_smtp_port, data_ultima_pec, pn_abilitata, pn_email, pn_username, pn_password, pn_ssl_port, pn_pop3_host, pn_use_ssl, pn_smtp_host, prec_email_invio, prec_email_ricezione, prec_username, prec_password, prec_smtp_host, mail_timer) FROM stdin;
1	1	0							POP3		2018-07-13 02:33:55.441	0						0		\N	\N	\N	\N	\N	0
\.


--
-- TOC entry 3109 (class 0 OID 303464)
-- Dependencies: 192
-- Data for Name: menu; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY menu (menu_id, titolo, descrizione, parent_id, posizione, link, root_function, unique_name) FROM stdin;
49	Presa in carico	Presa in carico	1	3	/page/protocollo/presaInCarico.do	1	\N
12	Anagrafica	Anagrafica	1	5	\N	1	\N
7	Documento in uscita	Registrazione documento in uscita	2	2	/page/protocollo/uscita/documento.do?annullaAction=true	1	\N
2	Registrazione	Registrazione protocollo	1	1	\N	1	\N
9	Documentale	Gestione documentale	\N	2	\N	2	\N
61	Gestione registro	Gestione del registro	1	7	/page/protocollo/gestione-registro.do	1	\N
50	Scarico/Riassegnazione	Scarico	1	4	/page/protocollo/scarico.do	1	\N
122	Faldoni	Gestione dei faldoni	1	9	\N	1	\N
45	Nuova Persona Fisica	Inserisce una nuova Persona Fisica nella rubrica	12	2	/page/protocollo/anagrafica/persona-fisica/nuovo.do	1	\N
47	Nuova Persona Giuridica	Inserisce una nuova Persona Giuridica nella rubrica	12	4	/page/protocollo/anagrafica/persona-giuridica/nuovo.do	1	\N
125	Procedimenti	Gestione dei procedimenti	1	10	\N	1	\N
132	Configurazione Utente	Configurazione Utente	1	13	/page/protocollo/ripetiDati.do	1	\N
4	E-mail in ingresso	Registrazione e-mail in ingresso	2	3	/page/protocollo/ingresso/email.do	1	\N
3	Documento in ingresso	Registrazione documento in ingresso	2	1	/page/protocollo/ingresso/documento.do?annullaAction=true	1	\N
11	Registri	Registri	10	2	/page/amministrazione/registri.do	3	\N
15	Titolario	Titolario	10	3	/page/amministrazione/titolario.do	3	\N
10	Amministrazione	Amministrazione del Sistema	\N	3	\N	3	\N
17	Mezzi di spedizione	Mezzi di spedizione	10	4	/page/amministrazione/mezzispedizione/spedizione.do	3	\N
18	Profili	Profili	19	4	/page/amministrazione/profilo.do	3	\N
19	Organizzazione	Organizzazione	10	6	\N	3	\N
21	Tipi documenti	Tipi documenti	10	8	/page/amministrazione/tipidocumento.do	3	\N
23	Report	Report	\N	4	\N	4	\N
24	Registro protocollo	Registro protocollo	40	1	/page/stampa/prn-pro/registro.do	4	\N
25	Scaricati	Scaricati	40	2	/page/stampa/prn-pro/scaricati.do	4	\N
26	in Lavorazione	in Lavorazione	40	3	/page/stampa/prn-pro/lavorazione.do	4	\N
39	Spediti	Spediti	40	5	/page/stampa/prn-pro/spediti.do	4	\N
40	Protocollo	Protocollo	23	1	\N	4	\N
41	Assegnati	Assegnati	40	6	/page/stampa/prn-pro/assegnati.do	4	\N
42	Annullati	Annullati	40	7	/page/stampa/prn-pro/annullati.do	4	\N
43	Anagrafica	Anagrafica	23	2	/page/stampa/rubrica.do	4	\N
44	Organigramma	Organigramma	23	3	/page/stampa/organizzazione.do	4	\N
16	Help	Help	\N	5	\N	5	\N
52	Protocollo	Protocollo	16	1	\N	5	\N
53	Documento in ingresso	Documento in ingresso	58	1	/page/help/hlp-pro/hlpregingresso.do	5	\N
54	Documento in uscita	Documento in uscita	58	2	/page/help/hlp-pro/hlpreguscita.do	5	\N
55	Ricerca	Ricerca	52	2	/page/help/hlp-pro/hlpricerca.do	5	\N
56	Presa in carico	Presa in carico	52	3	/page/help/hlp-pro/hlppresaincarico.do	5	\N
57	Scarico	Scarico	52	4	/page/help/hlp-pro/hlpscarico.do	5	\N
58	Registrazione	Registrazione	52	1	\N	5	\N
59	Anagrafica	Anagrafica	52	5	/page/help/hlp-pro/hlpanagrafica.do	5	\N
62	Uffici	Gestione Uffici	19	2	/page/amministrazione/organizzazione/ufficio.do?btnAnnulla=true	3	\N
63	Utenti	Gestione Utenti	19	3	/page/amministrazione/organizzazione/utenti/cerca.do	3	\N
64	Ricarica organizzazione	Ricarica organizzazione	19	5	/page/amministrazione/organizzazione.do	3	\N
65	Statistiche protocolli	Statistiche protocolli	40	8	/page/stampa/prn-pro/statistiche.do	4	\N
66	Documentale	Documentale	16	2	\N	5	\N
67	Amministrazione	Amministrazione	16	3	\N	5	\N
68	Reports	Reports	16	4	\N	5	\N
69	Registri	Registri	67	1	/page/help/hlp-amm/hlpregistro.do	5	\N
70	Titolario	Titolario	67	2	/page/help/hlp-amm/hlptitolario.do	5	\N
8	Stato e-mail in uscita	Controlla stato e-mail in uscita	2	4	/page/protocollo/ingresso/emailLog.do?annullaAction=true	1	\N
123	Nuovo	Inserisci un nuovo faldone	122	1	/page/faldone.do?annullaAction=true	1	\N
33	Cerca Fascicoli	Ricerca dei fascicoli	154	4	/page/fascicoli.do?btnAnnullaRicerca=true	2	\N
28	Documenti Condivisi	Documenti Condivisi	153	2	/page/documentale/documentiCondivisi.do	2	\N
46	Persona Fisica	Cerca una Persona Fisica sulla rubrica	12	1	/page/protocollo/anagrafica/persona-fisica/cerca.do?annulla=true	1	\N
48	Persona Giuridica	Cerca una Persona Giuridica sulla rubrica	12	3	/page/protocollo/anagrafica/persona-giuridica/cerca.do?annulla=true	1	\N
129	Lista Distribuzione Esterna	Cerca una lista di distribuzione esterna	12	5	/page/protocollo/anagrafica/listaDistribuzione.do?annullaAction=true	1	\N
130	Nuova lista distribuzione	Inserisci una lista di distribuzione esterna	12	6	/page/protocollo/anagrafica/listaDistribuzione/nuovo.do?annullaAction=true	1	\N
6	Ricerca	Ricerca dei protocolli	1	2	/page/protocollo/ricerca.do	1	\N
60	Rifiutati	Gestione dei rifiutati	1	6	/page/protocollo/ingresso/respinti.do	1	\N
128	Registro emergenza	Gestione del registro d'emergenza	1	11	/page/protocollo/emergenza.do	1	\N
131	Evidenze	Gestione delle evidenze	1	12	/page/evidenza/cerca.do	1	\N
96	Documenti da archivio	Protocolla documenti dall'archivio	2	5	/page/protocollo/uscita/archivio/documenti.do	1	\N
97	Fascicoli da archivio	Protocolla fascicoli dall'archivio	2	6	/page/protocollo/uscita/archivio/fascicoli.do	1	\N
124	Ricerca	Ricerca dei faldoni	122	2	/page/faldone/cerca.do?annullaAction=true	1	\N
1	Protocollo	Abilita la zona lavoro	\N	1	/page/protocollo/dashboard.do	1	\N
27	Archivio Documenti	Archivio dei documenti	153	1	/page/documentale/cartelle.do	2	\N
71	Mezzi di spedizione	Mezzi di spedizione	67	3	/page/help/hlp-amm/hlpmezzispedizione.do	5	\N
72	Organizzazione	Organizzazione	67	4	\N	5	\N
73	AOO	AOO	72	1	/page/help/hlp-amm/hlpaoo.do	5	\N
74	Uffici	Uffici	72	2	/page/help/hlp-amm/hlpuffici.do	5	\N
75	Utenti	Utenti	72	3	/page/help/hlp-amm/hlputenti.do	5	\N
76	Profili	Profili	72	4	/page/help/hlp-amm/hlpprofili.do	5	\N
77	Ricarica organizzazione	Ricarica organizzazione	72	5	/page/help/hlp-amm/hlpricaricaorganizzazione.do	5	\N
78	Tipi documenti	Tipi documenti	67	5	/page/help/hlp-amm/hlptipidocumento.do	5	\N
79	Protocollo	Protocollo	68	1	\N	5	\N
80	Registro protocollo	Registro protocollo	79	1	/page/help/hlp-rpt/hlp-pro/hlpregistro.do	5	\N
81	Scaricati	Scaricati	79	2	/page/help/hlp-rpt/hlp-pro/hlpscarico.do	5	\N
82	in Lavorazione	in Lavorazione	79	3	/page/help/hlp-rpt/hlp-pro/hlplavorazione.do	5	\N
83	Spediti	Spediti	79	4	/page/help/hlp-rpt/hlp-pro/hlpspediti.do	5	\N
84	Assegnati	Assegnati	79	5	/page/help/hlp-rpt/hlp-pro/hlpassegnati.do	5	\N
85	Statistiche protocolli	Statistiche protocolli	79	6	/page/help/hlp-rpt/hlp-pro/hlpstatistiche.do	5	\N
86	Anagrafica	Anagrafica	68	2	/page/help/hlp-rpt/hlpanagrafica.do	5	\N
87	Organigramma	Organigramma	68	3	/page/help/hlp-rpt/hlporganigramma.do	5	\N
88	Annullati	Annullati	79	7	/page/help/hlp-rpt/hlp-pro/hlpannullati.do	5	\N
89	Scarto protocolli	Scarto protocolli	10	10	\N	3	\N
90	Scarta protocolli	Scarta protocolli	89	1	/page/amministrazione/scarto.do	3	\N
91	Archivio scarti	Archivio scarti	89	2	/page/amministrazione/archivioScarto.do	3	\N
92	Gestione Firma Digitale	Gestione Firma Digitale	10	11	\N	3	\N
93	Importa Lista CA	Importa Lista CA	92	1	/page/amministrazione/firmadigitale/aggiorna_crl.do	3	\N
94	Elenco CA	Lista CA Elenco CA	92	2	/page/amministrazione/firmadigitale/lista_ca.do	3	\N
95	Nuova CA	Nuova CA	92	3	/page/amministrazione/firmadigitale/edit_ca.do?nuovo=Nuova CA	3	\N
99	Archivio documenti	Archivio documenti	66	1	/page/help/hlp-doc/archivioDocumenti.do	5	\N
100	Documenti condivisi	Documenti condivisi	66	2	/page/help/hlp-doc/documentiCondivisi.do	5	\N
101	Cerca in archivio	Cerca in archivio	66	3	/page/help/hlp-doc/cerca.do	5	\N
102	Fascicoli	Fascicoli	66	4	/page/help/hlp-doc/fascicoli.do	5	\N
103	Email in ingresso	Email in ingresso	58	3	/page/help/hlp-pro/hlpemailingresso.do	5	\N
104	Email log	Email log	58	4	/page/help/hlp-pro/hlpemaillog.do	5	\N
105	Documenti da archivio	Documenti da archivio	58	5	/page/help/hlp-pro/hlpdocumentidaarchivio.do	5	\N
106	Fascicoli da archivio	Fascicoli da archivio	58	6	/page/help/hlp-pro/hlpfascicolidaarchivio.do	5	\N
107	Dati amministrazione	Dati amministrazione	67	6	/page/help/hlp-amm/hlpdatiamministrazione.do	5	\N
108	Scarto protocolli	Scarto protocolli	67	7	\N	5	\N
109	Scarta protocolli	Scarta protocolli	108	1	/page/help/hlp-amm/hlpscartaprotocolli.do	5	\N
110	Archivio scarti	Archivio scarti	108	2	/page/help/hlp-amm/hlparchivioscarti.do	5	\N
111	Gestione firma digitale	Gestione firma digitale	67	8	\N	5	\N
112	Importa lista CA	Importa lista CA	111	1	/page/help/hlp-amm/hlpimportalistaCA.do	5	\N
113	Elenco CA	Elenco CA	111	2	/page/help/hlp-amm/hlpelencoCA.do	5	\N
114	Nuova CA	Nuova CA	111	3	/page/help/hlp-amm/hlpnuovaCA.do	5	\N
115	Rifiutati	Rifiutati	52	6	/page/help/hlp-pro/hlpriassegnazione.do	5	\N
116	Gestione registro	Gestione registro	52	7	/page/help/hlp-pro/hlpgestioneregistro.do	5	\N
117	Acquisizione massiva	Acquisizione massiva	10	12	\N	3	\N
118	Acquisisci nuovi documenti	Acquisisci nuovi documenti	117	1	/page/amministrazione/acquisizioneMassiva.do	3	\N
119	Logs acquisizioni	Logs acquisizioni	117	2	/page/amministrazione/acquisizioneMassiva.do?btnLogs=true	3	\N
120	Acquisizione massiva	Acquisizione massiva	67	9	/page/help/hlp-amm/hlpacquisizioneMassiva.do	5	\N
121	Gestione titoli destinatario	Gestione titoli destinatario	10	13	/page/amministrazione/titoliDestinatario.do	3	\N
133	Protocolli da scartare	Protocolli da scartare	40	9	/page/stampa/prn-pro/daScartare.do	4	\N
135	Registro modifiche documenti	Registro modifiche documenti	10	14	/page/amministrazione/registroModifiche.do	3	\N
136	Tipi procedimento	Tipi procedimento	10	15	/page/amministrazione/tipiProcedimento.do	3	\N
139	Oggettario	Oggettario	10	16	/page/amministrazione/oggettario.do	3	\N
140	Import Titolario	Import Titolario	10	17	/page/amministrazione/importTitolario.do	3	\N
142	Ingresso d`emergenza	Registrazione d`emergenza in ingresso	2	8	/page/protocollo/emergenzaviewingresso.do?ingressoAction=true	1	\N
143	Uscita d`emergenza	Registrazione d`emergenza in uscita	2	9	/page/protocollo/emergenzaviewuscita.do?uscitaAction=true	1	\N
144	Veline	Stampa Veline	23	4	/page/stampa/veline.do	4	\N
145	Modifica il Protocollo	Modifica il Protocollo	2	20	no_link	1	\N
146	Annulla il Protocollo	Annulla il Protocollo	2	21	_no_link	1	\N
147	Cruscotti	Cruscotti	1	14	\N	1	\N
153	Documenti	Documenti	9	1	\N	2	documentale_doc
154	Fascicoli	Fascicoli	9	2	\N	2	documentale_fasc
141	Posta Interna	Registrazione documento di posta interna	2	7	/page/protocollo/posta_interna/documento.do?annullaAction=true	1	\N
150	Inserisci Riservate	Inserisci registrazioni riservate	2	22	ins_ris	1	isert_reserved
151	Leggi Riservate	Leggi registrazioni riservate	2	23	leg_ris	1	read_reserved
152	Riassegna Protocolli	Riassegna i protocolli assegnati	2	24	ri_assign	1	reassign
148	Amministrazione	Abilita il cruscotto amministrazione	147	1	/page/cruscotto/amministrazione.do	1	\N
149	Struttura	Abilita il cruscotto struttura	147	2	/page/cruscotto/struttura.do	1	\N
155	Editor	Abilita l'editor	153	4	/page/documentale/editor.do	2	editor
159	Titolario	Stampa Titolario	23	5	/page/stampa/titolario.do	4	stampa_titolario
161	Lista Distribuzione Interna	Cerca una lista di distribuzione interna	12	7	/page/protocollo/anagrafica/listaDistribuzioneInterna.do?annullaAction=true	1	find_ld_int
162	Nuova lista distribuzione interna	Inserisci una lista di distribuzione interna	12	8	/page/protocollo/anagrafica/listaDistribuzioneInterna/nuovo.do?annullaAction=true	1	new_ld_int
156	Fatture	Registrazione delle fatture	2	11	/page/fatture/documento.do	1	fatture
160	Cambia Mittente PI	Modifica il mittente nella Posta Interna	2	25	change_sender	1	change_sender
127	Ricerca	Ricerca dei procedimenti	125	2	/page/procedimento/ricerca.do?ricercaIniziale=true	1	\N
37	Cerca in archivio	Ricerca dei documenti in archivio	153	3	/page/documentale/cerca.do?btnAnnulla=true	2	\N
134	Nuovo fascicolo	Inserisci un nuovo fascicolo	154	5	/page/fascicolo.do?btnNuovo=true	2	\N
157	Versamento in Deposito	Gestione dell'archivio di deposito	154	6	/page/ricerca_deposito.do	2	archivio_deposito_doc
163	Ricerca fascicoli in tutti gli uffici	Ricerca fascicoli in tutti gli uffici	154	7	serch_folders_all_offices	2	serch_folders_all_offices
164	Notifiche Posta Interna	Notifiche Posta Interna	40	10	/page/stampa/prn-pro/notifichePI.do	4	stampa_notifichePI
165	Modifica i fascicoli	Modifica i fascicoli	154	8	modify_folders	2	modify_folders
166	Modifica Posta Interna	Modifica Posta Interna	2	26	no_link	1	modify_pi
167	Archivio generale	Archivio generale	154	9	access_all_folders	2	access_all_folders
168	Gestione Repertori	Gestione Repertori	10	18	/page/repertori.do	3	\N
169	Repertorio	Repertori	9	6	\N	2	\N
170	Gestione Repertorio	Gestione Singolo Repertorio	169	1	/page/repertori.do?assegnatiAction=true	2	\N
171	Visualizza Repertori	Visualizza Repertori	169	2	/page/repertoriView.do	2	\N
172	Invio Repertori	Invio Repertori	169	3	no_link_repertori	2	invio_repertori
173	Leggi avvisi mail	Leggi avvisi mail	2	27	no_link	1	mail_log
174	Notifica di esito committente	Notifica di esito committente	2	28	result_notification_client	1	result_notification_client
175	Conservazione	Conservazione file di Registro	23	6	\N	4	conservation_menu_parent
176	Registro Giornaliero	Conservazione file registro giornaliero	175	1	/page/stampa/conservazione/registro_giornaliero.do	4	conservation_daily_registry
177	Registro Annuale	Conservazione file registro annuale	175	2	/page/stampa/conservazione/registro_annuale.do	4	conservation_yearly_registry
178	Invio PEC	Invio PEC da Registrazione documento in uscita	2	29	no_link	1	enable_send_pec
179	Amm. Trasparente	Amm. Trasparente	10	19	/page/amm_trasparente.do	3	\N
\.


--
-- TOC entry 3179 (class 0 OID 305845)
-- Dependencies: 262
-- Data for Name: notifiche_fattura_elettronica; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY notifiche_fattura_elettronica (nfe_id, aoo_id, progressivo, anno) FROM stdin;
\.


--
-- TOC entry 3110 (class 0 OID 303471)
-- Dependencies: 193
-- Data for Name: oggetti; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY oggetti (descrizione, id, giorni_alert, aoo_id) FROM stdin;
\.


--
-- TOC entry 3156 (class 0 OID 304933)
-- Dependencies: 239
-- Data for Name: oggetto_assegnatari; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY oggetto_assegnatari (oggetto_id, ufficio_id) FROM stdin;
\.


--
-- TOC entry 3158 (class 0 OID 304971)
-- Dependencies: 241
-- Data for Name: permessi_carica; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY permessi_carica (carica_id, menu_id) FROM stdin;
1	169
1	168
1	6
1	49
1	12
1	7
1	2
1	8
1	46
1	96
1	97
1	9
1	61
1	50
1	60
1	122
1	128
1	45
1	48
1	47
1	125
1	127
1	130
1	131
1	123
1	132
1	124
1	129
1	4
1	3
1	1
1	11
1	15
1	10
1	17
1	18
1	19
1	21
1	23
1	24
1	25
1	26
1	39
1	40
1	41
1	42
1	43
1	44
1	16
1	52
1	53
1	54
1	55
1	56
1	57
1	58
1	59
1	62
1	63
1	64
1	65
1	66
1	67
1	68
1	69
1	70
1	33
1	27
1	28
1	71
1	72
1	73
1	74
1	75
1	76
1	77
1	78
1	79
1	80
1	81
1	82
1	83
1	84
1	85
1	86
1	87
1	88
1	89
1	90
1	91
1	92
1	93
1	94
1	95
1	99
1	100
1	101
1	102
1	103
1	104
1	105
1	106
1	107
1	108
1	109
1	110
1	111
1	112
1	113
1	114
1	115
1	116
1	117
1	118
1	119
1	120
1	121
1	133
1	135
1	136
1	139
1	140
1	141
1	142
1	143
1	144
1	145
1	146
1	147
1	148
1	149
1	151
1	150
1	152
1	153
1	154
1	134
1	155
1	37
1	156
1	178
\.


--
-- TOC entry 3111 (class 0 OID 303483)
-- Dependencies: 194
-- Data for Name: procedimenti; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY procedimenti (procedimento_id, data_avvio, ufficio_id, stato_id, tipo_finalita_id, oggetto, referente_id, posizione_id, data_evidenza, note, protocollo_id, numero_procedimento, anno, numero, row_created_time, row_created_user, row_updated_user, row_updated_time, versione, aoo_id, giorni_alert, giorni_max, tipo_procedimento_id, responsabile_id, fascicolo_id, indicazioni, estremi_provvedimento, carica_lav_id, estremi_sospensione, data_sospensione, data_scadenza, flag_sospeso, autorita, delegato, interessato, indi_delegato, indi_interessato) FROM stdin;
\.


--
-- TOC entry 3112 (class 0 OID 303494)
-- Dependencies: 195
-- Data for Name: procedimenti_faldone; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY procedimenti_faldone (procedimento_id, faldone_id) FROM stdin;
\.


--
-- TOC entry 3113 (class 0 OID 303497)
-- Dependencies: 196
-- Data for Name: procedimenti_fascicolo; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY procedimenti_fascicolo (procedimento_id, fascicolo_id, row_created_time, row_created_user, row_updated_user, row_updated_time, versione) FROM stdin;
\.


--
-- TOC entry 3162 (class 0 OID 305122)
-- Dependencies: 245
-- Data for Name: procedimento_istruttori; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY procedimento_istruttori (procedimento_id, carica_id, versione, flag_lavorato, row_created_time, row_created_user) FROM stdin;
\.


--
-- TOC entry 3182 (class 0 OID 305982)
-- Dependencies: 265
-- Data for Name: procedimento_uffici_partecipanti; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY procedimento_uffici_partecipanti (puv_id, procedimento_id, ufficio_id, flag_visibilita, versione, row_created_time, row_created_user) FROM stdin;
\.


--
-- TOC entry 3114 (class 0 OID 303502)
-- Dependencies: 197
-- Data for Name: profili; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY profili (profilo_id, codi_profilo, desc_profilo, data_inizio_validita, data_fine_validita, row_created_time, row_created_user, row_updated_user, row_updated_time, versione, aoo_id) FROM stdin;
\.


--
-- TOC entry 3115 (class 0 OID 303509)
-- Dependencies: 198
-- Data for Name: profili$menu; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY "profili$menu" (profilo_id, menu_id, row_created_time, row_created_user, row_updated_user, row_updated_time, versione) FROM stdin;
\.


--
-- TOC entry 3116 (class 0 OID 303515)
-- Dependencies: 199
-- Data for Name: protocolli; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY protocolli (protocollo_id, anno_registrazione, nume_protocollo, data_registrazione, flag_tipo_mittente, flag_tipo, data_documento, tipo_documento_id, registro_id, titolario_id, ufficio_protocollatore_id, data_scadenza, data_effettiva_registrazione, data_ricezione, data_protocollo_mittente, nume_protocollo_mittente, desc_denominazione_mittente, indi_mittente, indi_cap_mittente, indi_localita_mittente, indi_provincia_mittente, indi_nazione_mittente, data_annullamento, data_scarico, stato_protocollo, text_nota_annullamento, text_provvedimento_annullament, codi_documento_doc, row_created_time, row_created_user, utente_mittente_id, versione, annotazione_chiave, annotazione_posizione, annotazione_descrizione, aoo_id, documento_id, ufficio_mittente_id, flag_scarto, flag_mozione, flag_riservato, num_prot_emergenza, registro_anno_numero, text_estremi_autorizzazione, intervallo_emergenza, text_oggetto, desc_cognome_mittente, desc_nome_mittente, carica_protocollatore_id, giorni_alert, numero_email, flag_fattura_elettronica, utente_protocollatore_id, flag_repertorio, flag_anomalia) FROM stdin;
\.


--
-- TOC entry 3117 (class 0 OID 303532)
-- Dependencies: 200
-- Data for Name: protocollo_allacci; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY protocollo_allacci (allaccio_id, protocollo_id, protocollo_allacciato_id, versione, flag_principale) FROM stdin;
\.


--
-- TOC entry 3118 (class 0 OID 303537)
-- Dependencies: 201
-- Data for Name: protocollo_allegati; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY protocollo_allegati (allegato_id, protocollo_id, documento_id, versione) FROM stdin;
\.


--
-- TOC entry 3119 (class 0 OID 303541)
-- Dependencies: 202
-- Data for Name: protocollo_annotazioni; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY protocollo_annotazioni (annotazione_id, protocollo_id, codi_annotazione, desc_annotazione, codi_userid, data_annotazione, row_created_time, row_created_user, row_updated_user, row_updated_time, versione) FROM stdin;
\.


--
-- TOC entry 3120 (class 0 OID 303550)
-- Dependencies: 203
-- Data for Name: protocollo_assegnatari; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY protocollo_assegnatari (assegnatario_id, protocollo_id, data_assegnazione, data_operazione, stat_assegnazione, ufficio_assegnante_id, ufficio_assegnatario_id, utente_assegnatario_id, utente_assegnante_id, versione, flag_competente, messaggio, carica_assegnatario_id, carica_assegnante_id, check_presa_visione, check_lavorato, flag_titolare_procedimento) FROM stdin;
\.


--
-- TOC entry 3121 (class 0 OID 303557)
-- Dependencies: 204
-- Data for Name: protocollo_destinatari; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY protocollo_destinatari (destinatario_id, flag_tipo_destinatario, indirizzo, email, destinatario, mezzo_spedizione, citta, data_spedizione, flag_conoscenza, protocollo_id, data_effettiva_spedizione, versione, titolo_id, note, mezzo_spedizione_id, codice_postale, flag_presso, flag_pec, prezzo_spedizione) FROM stdin;
\.


--
-- TOC entry 3122 (class 0 OID 303567)
-- Dependencies: 205
-- Data for Name: protocollo_mittenti; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY protocollo_mittenti (mittente_id, indirizzo, email, citta, provincia, protocollo_id, versione, codice_postale, descrizione, tipo, civico, soggetto_id) FROM stdin;
\.


--
-- TOC entry 3123 (class 0 OID 303574)
-- Dependencies: 206
-- Data for Name: protocollo_procedimenti; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY protocollo_procedimenti (procedimento_id, protocollo_id, row_created_time, row_created_user, row_updated_user, row_updated_time, versione, flag_sospeso) FROM stdin;
\.


--
-- TOC entry 3124 (class 0 OID 303579)
-- Dependencies: 207
-- Data for Name: province; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY province (provincia_id, codi_provincia, desc_provincia, regione_id, row_created_time, row_created_user, row_updated_user, row_updated_time) FROM stdin;
0	 	 	0	2018-07-13 02:24:50.359	\N	\N	2018-07-13 02:24:50.359
1	TO	Torino	1	2018-07-13 02:24:50.39	\N	\N	2018-07-13 02:24:50.39
2	VC	Vercelli	1	2018-07-13 02:24:50.406	\N	\N	2018-07-13 02:24:50.406
3	NO	Novara	1	2018-07-13 02:24:50.406	\N	\N	2018-07-13 02:24:50.406
4	CN	Cuneo	1	2018-07-13 02:24:50.406	\N	\N	2018-07-13 02:24:50.406
5	AT	Asti	1	2018-07-13 02:24:50.421	\N	\N	2018-07-13 02:24:50.421
6	AL	Alessandria	1	2018-07-13 02:24:50.421	\N	\N	2018-07-13 02:24:50.421
96	BI	Biella	1	2018-07-13 02:24:50.437	\N	\N	2018-07-13 02:24:50.437
103	VB	Verbano-Cusio-Osola	1	2018-07-13 02:24:50.437	\N	\N	2018-07-13 02:24:50.437
7	AO	Aosta	2	2018-07-13 02:24:50.453	\N	\N	2018-07-13 02:24:50.453
12	VA	Varese	3	2018-07-13 02:24:50.453	\N	\N	2018-07-13 02:24:50.453
13	CO	Como	3	2018-07-13 02:24:50.468	\N	\N	2018-07-13 02:24:50.468
14	SO	Sondrio	3	2018-07-13 02:24:50.468	\N	\N	2018-07-13 02:24:50.468
15	MI	Milano	3	2018-07-13 02:24:50.484	\N	\N	2018-07-13 02:24:50.484
16	BG	Bergamo	3	2018-07-13 02:24:50.484	\N	\N	2018-07-13 02:24:50.484
17	BS	Brescia	3	2018-07-13 02:24:50.499	\N	\N	2018-07-13 02:24:50.499
18	PV	Pavia	3	2018-07-13 02:24:50.499	\N	\N	2018-07-13 02:24:50.499
19	CR	Cremona	3	2018-07-13 02:24:50.515	\N	\N	2018-07-13 02:24:50.515
20	MN	Mantova	3	2018-07-13 02:24:50.531	\N	\N	2018-07-13 02:24:50.531
97	LC	Lecco	3	2018-07-13 02:24:50.531	\N	\N	2018-07-13 02:24:50.531
98	LO	Lodi	3	2018-07-13 02:24:50.546	\N	\N	2018-07-13 02:24:50.546
21	BZ	Bolzano	4	2018-07-13 02:24:50.546	\N	\N	2018-07-13 02:24:50.546
22	TN	Trento	4	2018-07-13 02:24:50.562	\N	\N	2018-07-13 02:24:50.562
23	VR	Verona	5	2018-07-13 02:24:50.562	\N	\N	2018-07-13 02:24:50.562
24	VI	Vicenza	5	2018-07-13 02:24:50.577	\N	\N	2018-07-13 02:24:50.577
25	BL	Belluno	5	2018-07-13 02:24:50.577	\N	\N	2018-07-13 02:24:50.577
26	TV	Treviso	5	2018-07-13 02:24:50.593	\N	\N	2018-07-13 02:24:50.593
27	VE	Venezia	5	2018-07-13 02:24:50.609	\N	\N	2018-07-13 02:24:50.609
28	PD	Padova	5	2018-07-13 02:24:50.609	\N	\N	2018-07-13 02:24:50.609
29	RO	Rovigo	5	2018-07-13 02:24:50.624	\N	\N	2018-07-13 02:24:50.624
30	UD	Udine	6	2018-07-13 02:24:50.624	\N	\N	2018-07-13 02:24:50.624
31	GO	Gorizia	6	2018-07-13 02:24:50.64	\N	\N	2018-07-13 02:24:50.64
32	TS	Trieste	6	2018-07-13 02:24:50.64	\N	\N	2018-07-13 02:24:50.64
93	PN	Pordenone	6	2018-07-13 02:24:50.655	\N	\N	2018-07-13 02:24:50.655
8	IM	Imperia	7	2018-07-13 02:24:50.655	\N	\N	2018-07-13 02:24:50.655
9	SV	Savona	7	2018-07-13 02:24:50.671	\N	\N	2018-07-13 02:24:50.671
10	GE	Genova	7	2018-07-13 02:24:50.671	\N	\N	2018-07-13 02:24:50.671
11	SP	La Spezia	7	2018-07-13 02:24:50.687	\N	\N	2018-07-13 02:24:50.687
33	PC	Piacenza	8	2018-07-13 02:24:50.702	\N	\N	2018-07-13 02:24:50.702
34	PR	Parma	8	2018-07-13 02:24:50.702	\N	\N	2018-07-13 02:24:50.702
35	RE	Reggio Emilia	8	2018-07-13 02:24:50.718	\N	\N	2018-07-13 02:24:50.718
36	MO	Modena	8	2018-07-13 02:24:50.718	\N	\N	2018-07-13 02:24:50.718
37	BO	Bologna	8	2018-07-13 02:24:50.733	\N	\N	2018-07-13 02:24:50.733
38	FE	Ferrara	8	2018-07-13 02:24:50.733	\N	\N	2018-07-13 02:24:50.733
39	RA	Ravenna	8	2018-07-13 02:24:50.749	\N	\N	2018-07-13 02:24:50.749
40	FC	Forli - Cesena	8	2018-07-13 02:24:50.749	\N	\N	2018-07-13 02:24:50.749
99	RN	Rimini	8	2018-07-13 02:24:50.765	\N	\N	2018-07-13 02:24:50.765
45	MS	Massa - Carrara	9	2018-07-13 02:24:50.78	\N	\N	2018-07-13 02:24:50.78
46	LU	Lucca	9	2018-07-13 02:24:50.78	\N	\N	2018-07-13 02:24:50.78
47	PT	Pistoia	9	2018-07-13 02:24:50.796	\N	\N	2018-07-13 02:24:50.796
48	FI	Firenze	9	2018-07-13 02:24:50.796	\N	\N	2018-07-13 02:24:50.796
49	LI	Livorno	9	2018-07-13 02:24:50.811	\N	\N	2018-07-13 02:24:50.811
50	PI	Pisa	9	2018-07-13 02:24:50.827	\N	\N	2018-07-13 02:24:50.827
51	AR	Arezzo	9	2018-07-13 02:24:50.827	\N	\N	2018-07-13 02:24:50.827
52	SI	Siena	9	2018-07-13 02:24:50.843	\N	\N	2018-07-13 02:24:50.843
53	GR	Grosseto	9	2018-07-13 02:24:50.843	\N	\N	2018-07-13 02:24:50.843
100	PO	Prato	9	2018-07-13 02:24:50.858	\N	\N	2018-07-13 02:24:50.858
54	PG	Perugia	10	2018-07-13 02:24:50.874	\N	\N	2018-07-13 02:24:50.874
55	TR	Terni	10	2018-07-13 02:24:50.874	\N	\N	2018-07-13 02:24:50.874
41	PU	Pesaro e Urbino	11	2018-07-13 02:24:50.889	\N	\N	2018-07-13 02:24:50.889
42	AN	Ancona	11	2018-07-13 02:24:50.889	\N	\N	2018-07-13 02:24:50.889
43	MC	Macerata	11	2018-07-13 02:24:50.905	\N	\N	2018-07-13 02:24:50.905
44	AP	Ascoli Piceno	11	2018-07-13 02:24:50.905	\N	\N	2018-07-13 02:24:50.905
56	VT	Viterbo	12	2018-07-13 02:24:50.921	\N	\N	2018-07-13 02:24:50.921
57	RI	Rieti	12	2018-07-13 02:24:50.921	\N	\N	2018-07-13 02:24:50.921
58	RM	Roma	12	2018-07-13 02:24:50.936	\N	\N	2018-07-13 02:24:50.936
59	LT	Latina	12	2018-07-13 02:24:50.936	\N	\N	2018-07-13 02:24:50.936
60	FR	Frosinone	12	2018-07-13 02:24:50.952	\N	\N	2018-07-13 02:24:50.952
66	AQ	L'Aquila	13	2018-07-13 02:24:50.952	\N	\N	2018-07-13 02:24:50.952
67	TE	Teramo	13	2018-07-13 02:24:50.952	\N	\N	2018-07-13 02:24:50.952
68	PE	Pescara	13	2018-07-13 02:24:50.967	\N	\N	2018-07-13 02:24:50.967
69	CH	Chieti	13	2018-07-13 02:24:50.967	\N	\N	2018-07-13 02:24:50.967
70	CB	Campobasso	14	2018-07-13 02:24:50.983	\N	\N	2018-07-13 02:24:50.983
94	IS	Isernia	14	2018-07-13 02:24:50.983	\N	\N	2018-07-13 02:24:50.983
61	CE	Caserta	15	2018-07-13 02:24:50.999	\N	\N	2018-07-13 02:24:50.999
62	BN	Benevento	15	2018-07-13 02:24:50.999	\N	\N	2018-07-13 02:24:50.999
63	NA	Napoli	15	2018-07-13 02:24:50.999	\N	\N	2018-07-13 02:24:50.999
64	AV	Avellino	15	2018-07-13 02:24:51.014	\N	\N	2018-07-13 02:24:51.014
65	SA	Salerno	15	2018-07-13 02:24:51.03	\N	\N	2018-07-13 02:24:51.03
71	FG	Foggia	16	2018-07-13 02:24:51.03	\N	\N	2018-07-13 02:24:51.03
72	BA	Bari	16	2018-07-13 02:24:51.03	\N	\N	2018-07-13 02:24:51.03
73	TA	Taranto	16	2018-07-13 02:24:51.045	\N	\N	2018-07-13 02:24:51.045
74	BR	Brindisi	16	2018-07-13 02:24:51.045	\N	\N	2018-07-13 02:24:51.045
75	LE	Lecce	16	2018-07-13 02:24:51.061	\N	\N	2018-07-13 02:24:51.061
76	PZ	Potenza	17	2018-07-13 02:24:51.061	\N	\N	2018-07-13 02:24:51.061
77	MT	Matera	17	2018-07-13 02:24:51.077	\N	\N	2018-07-13 02:24:51.077
78	CS	Cosenza	18	2018-07-13 02:24:51.092	\N	\N	2018-07-13 02:24:51.092
79	CZ	Catanzaro	18	2018-07-13 02:24:51.092	\N	\N	2018-07-13 02:24:51.092
80	RC	Reggio di Calabria	18	2018-07-13 02:24:51.092	\N	\N	2018-07-13 02:24:51.092
101	KR	Crotone	18	2018-07-13 02:24:51.108	\N	\N	2018-07-13 02:24:51.108
102	VV	Vibo Valentia	18	2018-07-13 02:24:51.108	\N	\N	2018-07-13 02:24:51.108
81	TP	Trapani	19	2018-07-13 02:24:51.123	\N	\N	2018-07-13 02:24:51.123
82	PA	Palermo	19	2018-07-13 02:24:51.123	\N	\N	2018-07-13 02:24:51.123
83	ME	Messina	19	2018-07-13 02:24:51.139	\N	\N	2018-07-13 02:24:51.139
84	AG	Agrigento	19	2018-07-13 02:24:51.139	\N	\N	2018-07-13 02:24:51.139
85	CL	Caltanissetta	19	2018-07-13 02:24:51.155	\N	\N	2018-07-13 02:24:51.155
86	EN	Enna	19	2018-07-13 02:24:51.155	\N	\N	2018-07-13 02:24:51.155
87	CT	Catania	19	2018-07-13 02:24:51.155	\N	\N	2018-07-13 02:24:51.155
88	RG	Ragusa	19	2018-07-13 02:24:51.17	\N	\N	2018-07-13 02:24:51.17
89	SR	Siracusa	19	2018-07-13 02:24:51.17	\N	\N	2018-07-13 02:24:51.17
90	SS	Sassari	20	2018-07-13 02:24:51.186	\N	\N	2018-07-13 02:24:51.186
91	NU	Nuoro	20	2018-07-13 02:24:51.186	\N	\N	2018-07-13 02:24:51.186
92	CA	Cagliari	20	2018-07-13 02:24:51.201	\N	\N	2018-07-13 02:24:51.201
95	OR	Oristano	20	2018-07-13 02:24:51.201	\N	\N	2018-07-13 02:24:51.201
\.


--
-- TOC entry 3125 (class 0 OID 303587)
-- Dependencies: 208
-- Data for Name: regioni; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY regioni (regione_id, desc_regione, row_created_time, row_created_user, row_updated_user, row_updated_time) FROM stdin;
0	 	2018-07-13 02:24:50.203	\N	\N	2018-07-13 02:24:50.203
1	Piemonte	2018-07-13 02:24:50.219	\N	\N	2018-07-13 02:24:50.219
2	Valle d'Aosta	2018-07-13 02:24:50.234	\N	\N	2018-07-13 02:24:50.234
3	Lombardia	2018-07-13 02:24:50.234	\N	\N	2018-07-13 02:24:50.234
4	Trentino-Alto Adige	2018-07-13 02:24:50.234	\N	\N	2018-07-13 02:24:50.234
5	Veneto	2018-07-13 02:24:50.25	\N	\N	2018-07-13 02:24:50.25
6	Friuli-Venezia Giulia	2018-07-13 02:24:50.265	\N	\N	2018-07-13 02:24:50.265
7	Liguria	2018-07-13 02:24:50.265	\N	\N	2018-07-13 02:24:50.265
8	Emilia Romagna	2018-07-13 02:24:50.265	\N	\N	2018-07-13 02:24:50.265
9	Toscana	2018-07-13 02:24:50.281	\N	\N	2018-07-13 02:24:50.281
10	Umbria	2018-07-13 02:24:50.281	\N	\N	2018-07-13 02:24:50.281
11	Marche	2018-07-13 02:24:50.297	\N	\N	2018-07-13 02:24:50.297
12	Lazio	2018-07-13 02:24:50.297	\N	\N	2018-07-13 02:24:50.297
13	Abruzzi	2018-07-13 02:24:50.312	\N	\N	2018-07-13 02:24:50.312
14	Molise	2018-07-13 02:24:50.312	\N	\N	2018-07-13 02:24:50.312
15	Campania	2018-07-13 02:24:50.328	\N	\N	2018-07-13 02:24:50.328
16	Puglia	2018-07-13 02:24:50.328	\N	\N	2018-07-13 02:24:50.328
17	Basilicata	2018-07-13 02:24:50.343	\N	\N	2018-07-13 02:24:50.343
18	Calabria	2018-07-13 02:24:50.343	\N	\N	2018-07-13 02:24:50.343
19	Sicilia	2018-07-13 02:24:50.359	\N	\N	2018-07-13 02:24:50.359
20	Sardegna	2018-07-13 02:24:50.359	\N	\N	2018-07-13 02:24:50.359
\.


--
-- TOC entry 3126 (class 0 OID 303592)
-- Dependencies: 209
-- Data for Name: registri; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY registri (registro_id, codi_registro, desc_registro, nume_ultimo_progressivo, nume_ultimo_progr_interno, nume_ultimo_fascicolo, data_apertura_registro, aoo_id, row_updated_user, row_updated_time, versione, flag_ufficiale, flag_aperto_ingresso, flag_aperto_uscita, flag_data_bloccata) FROM stdin;
1	RegUff	Registro Ufficiale	\N	\N	\N	2018-07-13 00:00:00	1	\N	\N	1	1	1	1	0
2	PosInt	Posta Interna	\N	\N	\N	2018-07-13 00:00:00	1	\N	\N	1	0	1	1	0
3	Fatt	Fatture	\N	\N	\N	2018-07-13 02:30:21.715	1	\N	2018-07-13 02:30:21.715	1	0	1	0	0
\.


--
-- TOC entry 3177 (class 0 OID 305730)
-- Dependencies: 260
-- Data for Name: repertori; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY repertori (repertorio_id, aoo_id, descrizione, ufficio_responsabile_id, flag_web) FROM stdin;
\.


--
-- TOC entry 3160 (class 0 OID 305087)
-- Dependencies: 243
-- Data for Name: riferimenti_legislativi; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY riferimenti_legislativi (riferimento_id, tipo_procedimenti_id, documento_id, procedimento_id) FROM stdin;
\.


--
-- TOC entry 3127 (class 0 OID 303601)
-- Dependencies: 210
-- Data for Name: ripetidati; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY ripetidati (utente_id, oggetto, check_oggetto, tipodocumento, check_tipodocumento, data_documento, check_data_documento, ricevuto_il, check_ricevuto_il, tipo_mittente, check_tipo_mittente, mittente, check_mittente, destinatario, check_destinatario, assegnatario_utente_id, check_assegnatario, assegnatario_ufficio_id, titolario, check_titolario, parametri_stampante) FROM stdin;
\.


--
-- TOC entry 3128 (class 0 OID 303607)
-- Dependencies: 211
-- Data for Name: rubrica; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY rubrica (rubrica_id, text_note, indi_web, indi_email, indi_cap, indi_comune, flag_is_folder, aoo_id, flag_settore, desc_qualifica, data_nascita, sesso, codi_partita_iva, desc_comune_nascita, pers_referente, tele_referente, codi_stato_civile, provincia_nascita_id, provincia_id, flag_tipo_rubrica, row_created_time, row_created_user, row_updated_user, row_updated_time, versione, desc_ditta, pers_cognome, pers_nome, tele_telefono, tele_fax, codi_fiscale, codi_codice, indi_dug, indi_toponimo, indi_civico) FROM stdin;
\.


--
-- TOC entry 3129 (class 0 OID 303619)
-- Dependencies: 212
-- Data for Name: rubrica_lista_distribuzione; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY rubrica_lista_distribuzione (id_lista, id_rubrica, row_created_time, row_created_user, row_updated_user, row_updated_time, tipo_soggetto) FROM stdin;
\.


--
-- TOC entry 3130 (class 0 OID 303624)
-- Dependencies: 213
-- Data for Name: segnature; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY segnature (segnature_id, protocollo_id, tipo_protocollo, row_created_time, row_created_user, row_updated_user, row_updated_time, versione, text_segnatura) FROM stdin;
\.


--
-- TOC entry 3131 (class 0 OID 303633)
-- Dependencies: 214
-- Data for Name: spedizioni; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY spedizioni (spedizioni_id, codi_spedizione, desc_spedizione, row_created_time, row_created_user, row_updated_user, row_updated_time, versione, flag_abilitato, flag_cancellabile, aoo_id, prezzo) FROM stdin;
\.


--
-- TOC entry 3174 (class 0 OID 305570)
-- Dependencies: 257
-- Data for Name: storia_amministrazione; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY storia_amministrazione (amministrazione_id, codi_amministrazione, desc_amministrazione, row_created_time, row_created_user, row_updated_user, row_updated_time, versione, flag_ldap, ldap_versione, ldap_porta, ldap_use_ssl, ldap_host, ldap_dn, path_doc, path_doc_protocollo, flag_reg_separato, ftp_host, ftp_port, ftp_user, ftp_pass, id_unita_amministrativa, versione_corrente_fenice) FROM stdin;
\.


--
-- TOC entry 3171 (class 0 OID 305444)
-- Dependencies: 254
-- Data for Name: storia_cariche; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY storia_cariche (carica_id, denominazione, ufficio_id, profilo_id, utente_id, versione, flag_attivo, row_created_time, row_created_user, flag_dirigente, flag_referente, flag_responsabile_ente, flag_referente_ufficio_protocollo) FROM stdin;
\.


--
-- TOC entry 3163 (class 0 OID 305198)
-- Dependencies: 246
-- Data for Name: storia_doc_file_attr; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY storia_doc_file_attr (dfa_id, dc_id, dfr_id, nome, versione, row_created_time, row_created_user, data_documento, oggetto, note, descrizione, descrizione_argomento, tipo_documento_id, titolario_id, stato_lav, stato_arc, owner_id, row_updated_time, carica_lav_id, procedimento_id) FROM stdin;
\.


--
-- TOC entry 3164 (class 0 OID 305211)
-- Dependencies: 247
-- Data for Name: storia_doc_file_permessi; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY storia_doc_file_permessi (dfp_id, dfa_id, tipo_permesso, ufficio_id, versione, msg, carica_id) FROM stdin;
\.


--
-- TOC entry 3166 (class 0 OID 305245)
-- Dependencies: 249
-- Data for Name: storia_documenti_editor; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY storia_documenti_editor (documento_id, txt, nome, row_created_time, row_created_user, carica_id, flag_stato, versione, oggetto, flag_tipo, msg_carica) FROM stdin;
\.


--
-- TOC entry 3132 (class 0 OID 303678)
-- Dependencies: 215
-- Data for Name: storia_faldone_fascicoli; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY storia_faldone_fascicoli (faldone_id, fascicolo_id, row_created_time, row_created_user, row_updated_user, row_updated_time, versione) FROM stdin;
\.


--
-- TOC entry 3133 (class 0 OID 303683)
-- Dependencies: 216
-- Data for Name: storia_faldoni; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY storia_faldoni (faldone_id, aoo_id, oggetto, ufficio_id, titolario_id, row_created_time, row_created_user, row_updated_user, row_updated_time, codice_locale, sotto_categoria, nota, numero_faldone, anno, numero, data_creazione, data_carico, data_scarico, data_evidenza, data_movimento, stato_id, versione, collocazione_label1, collocazione_label2, collocazione_label3, collocazione_label4, collocazione_valore1, collocazione_valore2, collocazione_valore3, collocazione_valore4) FROM stdin;
\.


--
-- TOC entry 3134 (class 0 OID 303694)
-- Dependencies: 217
-- Data for Name: storia_fascicoli; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY storia_fascicoli (fascicolo_id, aoo_id, codice, progressivo, collocazione, note, processo_id, registro_id, data_apertura, data_chiusura, stato, row_created_time, row_created_user, row_updated_user, row_updated_time, titolario_id, versione, ufficio_intestatario_id, ufficio_responsabile_id, data_evidenza, anno_riferimento, tipo, data_ultimo_movimento, data_scarto, data_carico, data_scarico, collocazione_label1, collocazione_label2, collocazione_label3, collocazione_label4, collocazione_valore1, collocazione_valore2, collocazione_valore3, collocazione_valore4, parent_id, giorni_max, giorni_alert, oggetto, descrizione, nome, carica_istruttore_id, carica_responsabile_id, carica_intestatario_id, comune, capitolo, path_progressivo, delegato, interessato, indi_delegato, indi_interessato) FROM stdin;
\.


--
-- TOC entry 3135 (class 0 OID 303704)
-- Dependencies: 218
-- Data for Name: storia_fascicolo_documenti; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY storia_fascicolo_documenti (dfa_id, fascicolo_id, row_created_time, row_created_user, row_updated_user, row_updated_time, versione, ufficio_proprietario_id) FROM stdin;
\.


--
-- TOC entry 3136 (class 0 OID 303709)
-- Dependencies: 219
-- Data for Name: storia_fascicolo_protocolli; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY storia_fascicolo_protocolli (protocollo_id, fascicolo_id, row_created_time, row_created_user, row_updated_user, row_updated_time, versione, ufficio_proprietario_id) FROM stdin;
\.


--
-- TOC entry 3137 (class 0 OID 303715)
-- Dependencies: 220
-- Data for Name: storia_procedimenti; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY storia_procedimenti (procedimento_id, data_avvio, ufficio_id, stato_id, tipo_finalita_id, oggetto, referente_id, posizione_id, data_evidenza, note, protocollo_id, numero_procedimento, anno, numero, row_created_time, row_created_user, row_updated_user, row_updated_time, versione, aoo_id, giorni_alert, giorni_max, tipo_procedimento_id, responsabile_id, fascicolo_id, indicazioni, estremi_provvedimento, carica_lav_id, estremi_sospensione, data_sospensione, data_scadenza, flag_sospeso, autorita, delegato, interessato, indi_delegato, indi_interessato) FROM stdin;
\.


--
-- TOC entry 3138 (class 0 OID 303726)
-- Dependencies: 221
-- Data for Name: storia_procedimenti_fascicolo; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY storia_procedimenti_fascicolo (procedimento_id, fascicolo_id, row_created_time, row_created_user, row_updated_user, row_updated_time, versione) FROM stdin;
\.


--
-- TOC entry 3172 (class 0 OID 305486)
-- Dependencies: 255
-- Data for Name: storia_procedimento_istruttori; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY storia_procedimento_istruttori (procedimento_id, carica_id, versione, flag_lavorato, row_created_time, row_created_user) FROM stdin;
\.


--
-- TOC entry 3139 (class 0 OID 303731)
-- Dependencies: 222
-- Data for Name: storia_protocolli; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY storia_protocolli (protocollo_id, anno_registrazione, nume_protocollo, data_registrazione, flag_tipo_mittente, flag_tipo, data_documento, tipo_documento_id, registro_id, titolario_id, ufficio_protocollatore_id, data_scadenza, data_effettiva_registrazione, data_ricezione, data_protocollo_mittente, nume_protocollo_mittente, desc_denominazione_mittente, indi_mittente, indi_cap_mittente, indi_localita_mittente, indi_provincia_mittente, indi_nazione_mittente, data_annullamento, data_scarico, stato_protocollo, text_nota_annullamento, text_provvedimento_annullament, codi_documento_doc, row_created_time, row_created_user, utente_mittente_id, versione, annotazione_chiave, annotazione_posizione, annotazione_descrizione, aoo_id, documento_id, ufficio_mittente_id, flag_scarto, flag_mozione, flag_riservato, num_prot_emergenza, registro_anno_numero, text_estremi_autorizzazione, intervallo_emergenza, text_oggetto, desc_cognome_mittente, desc_nome_mittente, carica_protocollatore_id, giorni_alert, numero_email, flag_fattura_elettronica, utente_protocollatore_id, flag_repertorio, flag_anomalia) FROM stdin;
\.


--
-- TOC entry 3140 (class 0 OID 303748)
-- Dependencies: 223
-- Data for Name: storia_protocollo_allacci; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY storia_protocollo_allacci (allaccio_id, protocollo_id, protocollo_allacciato_id, versione, flag_principale) FROM stdin;
\.


--
-- TOC entry 3141 (class 0 OID 303753)
-- Dependencies: 224
-- Data for Name: storia_protocollo_allegati; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY storia_protocollo_allegati (allegato_id, protocollo_id, documento_id, versione) FROM stdin;
\.


--
-- TOC entry 3142 (class 0 OID 303757)
-- Dependencies: 225
-- Data for Name: storia_protocollo_annotazioni; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY storia_protocollo_annotazioni (annotazione_id, protocollo_id, codi_annotazione, desc_annotazione, codi_userid, data_annotazione, row_created_time, row_created_user, row_updated_user, row_updated_time, versione) FROM stdin;
\.


--
-- TOC entry 3143 (class 0 OID 303766)
-- Dependencies: 226
-- Data for Name: storia_protocollo_assegnatari; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY storia_protocollo_assegnatari (assegnatario_id, protocollo_id, data_assegnazione, data_operazione, stat_assegnazione, ufficio_assegnante_id, ufficio_assegnatario_id, utente_assegnatario_id, utente_assegnante_id, versione, flag_competente, messaggio, carica_assegnatario_id, carica_assegnante_id, check_presa_visione, check_lavorato, flag_titolare_procedimento) FROM stdin;
\.


--
-- TOC entry 3144 (class 0 OID 303773)
-- Dependencies: 227
-- Data for Name: storia_protocollo_destinatari; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY storia_protocollo_destinatari (destinatario_id, flag_tipo_destinatario, indirizzo, email, destinatario, mezzo_spedizione, citta, data_spedizione, flag_conoscenza, protocollo_id, data_effettiva_spedizione, versione, titolo_id, note, mezzo_spedizione_id, codice_postale, flag_presso, flag_pec, prezzo_spedizione) FROM stdin;
\.


--
-- TOC entry 3159 (class 0 OID 305061)
-- Dependencies: 242
-- Data for Name: storia_protocollo_mittenti; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY storia_protocollo_mittenti (mittente_id, indirizzo, email, citta, provincia, protocollo_id, versione, codice_postale, descrizione, tipo, civico, soggetto_id) FROM stdin;
\.


--
-- TOC entry 3145 (class 0 OID 303783)
-- Dependencies: 228
-- Data for Name: storia_protocollo_procedimenti; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY storia_protocollo_procedimenti (procedimento_id, protocollo_id, row_created_time, row_created_user, row_updated_user, row_updated_time, versione, flag_sospeso) FROM stdin;
\.


--
-- TOC entry 3146 (class 0 OID 303788)
-- Dependencies: 229
-- Data for Name: storia_registri; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY storia_registri (registri_id, codi_registro, desc_registro, nume_ultimo_progressivo, nume_ultimo_progr_interno, nume_ultimo_fascicolo, data_apertura_registro, aoo_id, row_updated_user, row_updated_time, versione, flag_ufficiale, flag_aperto_ingresso, flag_aperto_uscita, flag_data_bloccata) FROM stdin;
\.


--
-- TOC entry 3147 (class 0 OID 303797)
-- Dependencies: 230
-- Data for Name: storia_titolario; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY storia_titolario (titolario_id, parent_id, aoo_id, desc_titolario, parent_full_desc, row_created_time, row_created_user, versione, massimario, responsabile_id, ufficio_responsabile_id) FROM stdin;
\.


--
-- TOC entry 3173 (class 0 OID 305504)
-- Dependencies: 256
-- Data for Name: storico_organigramma; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY storico_organigramma (st_org_id, aoo_id, descrizione, data, row_created_time, row_created_user) FROM stdin;
\.


--
-- TOC entry 3148 (class 0 OID 303814)
-- Dependencies: 231
-- Data for Name: tipi_documento; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY tipi_documento (tipo_documento_id, desc_tipo_documento, aoo_id, flag_attivazione, nume_gg_scadenza, row_created_time, row_created_user, row_updated_user, row_updated_time, flag_default, versione) FROM stdin;
1	Lettera	1	0	\N	2018-07-13 02:24:52.403	admin	\N	2018-07-13 02:24:52.403	1	0
2	Fax	1	0	\N	2018-07-13 02:24:52.434	admin	\N	2018-07-13 02:24:52.434	0	0
3	Raccomandata	1	0	\N	2018-07-13 02:24:52.434	admin	\N	2018-07-13 02:24:52.434	0	0
\.


--
-- TOC entry 3149 (class 0 OID 303823)
-- Dependencies: 232
-- Data for Name: tipi_procedimento; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY tipi_procedimento (tipo_procedimenti_id, tipo, giorni_max, giorni_alert, titolario_id, flag_ull) FROM stdin;
\.


--
-- TOC entry 3181 (class 0 OID 305965)
-- Dependencies: 264
-- Data for Name: tipi_procedimento_uffici; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY tipi_procedimento_uffici (tpu_id, tipo_procedimenti_id, ufficio_id, versione, row_created_time, row_created_user, flag_principale) FROM stdin;
\.


--
-- TOC entry 3150 (class 0 OID 303826)
-- Dependencies: 233
-- Data for Name: titolario; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY titolario (titolario_id, codi_titolario, desc_titolario, parent_id, aoo_id, row_created_time, row_created_user, row_updated_user, row_updated_time, versione, path_titolario, massimario, ufficio_id, utente_id, responsabile_id, ufficio_responsabile_id) FROM stdin;
\.


--
-- TOC entry 3151 (class 0 OID 303835)
-- Dependencies: 234
-- Data for Name: titolario$uffici; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY "titolario$uffici" (ufficio_id, titolario_id, row_created_time, row_created_user, row_updated_user, row_updated_time, versione) FROM stdin;
\.


--
-- TOC entry 3152 (class 0 OID 303841)
-- Dependencies: 235
-- Data for Name: titoli_destinatari; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY titoli_destinatari (id, descrizione, row_created_time, row_created_user, row_updated_user, row_updated_time) FROM stdin;
999	 	\N	\N	\N	\N
1	Sig.re/ra	\N	\N	\N	\N
\.


--
-- TOC entry 3153 (class 0 OID 303846)
-- Dependencies: 236
-- Data for Name: uffici; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY uffici (ufficio_id, descrizione, parent_id, aoo_id, row_created_time, row_created_user, row_updated_user, row_updated_time, versione, tipo, flag_attivo, flag_accettazione_automatica, indi_email, telefono, fax, piano, stanza, email_username, email_password, data_ultima_mail, flag_ufficio_protocollo) FROM stdin;
1	ROOT	\N	1	2018-07-13 02:24:52.278	\N	\N	2018-07-13 02:24:52.278	0	C	1	0	\N	\N	\N	\N	\N	\N	\N	2018-07-13 02:33:55.519	\N
\.


--
-- TOC entry 3154 (class 0 OID 303854)
-- Dependencies: 237
-- Data for Name: utenti; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY utenti (utente_id, user_name, email, cognome, nome, codicefiscale, matricola, passwd, data_fine_attivita, aoo_id, row_created_time, row_created_user, row_updated_user, row_updated_time, versione, flag_abilitato) FROM stdin;
1	admin1	\N	Amministratore	Amministratore	1	\N	admin1	\N	1	2018-07-13 02:24:52.309	\N	\N	2018-07-13 02:24:52.309	0	1
2	admin	\N	Amministratore AOO	Amministratore AOO	1	\N	admin	\N	1	2018-07-13 02:26:37.836	\N	\N	2018-07-13 02:26:37.836	0	1
\.


--
-- TOC entry 3155 (class 0 OID 303864)
-- Dependencies: 238
-- Data for Name: utenti$registri; Type: TABLE DATA; Schema: fenice; Owner: fenice
--

COPY "utenti$registri" (utente_id, registro_id, row_created_time, row_created_user, row_updated_user, row_updated_time, versione) FROM stdin;
1	1	2018-07-13 02:24:52.34	\N	\N	2018-07-13 02:24:52.34	0
1	2	2018-07-13 02:24:52.356	\N	\N	2018-07-13 02:24:52.356	0
2	1	2018-07-13 02:26:37.867	\N	\N	2018-07-13 02:26:37.867	0
2	2	2018-07-13 02:26:37.898	\N	\N	2018-07-13 02:26:37.898	0
1	3	\N	\N	\N	\N	0
2	3	\N	\N	\N	\N	0
\.


--
-- TOC entry 2594 (class 2606 OID 303871)
-- Name: $1; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY menu
    ADD CONSTRAINT "$1" UNIQUE (parent_id, posizione);


--
-- TOC entry 2765 (class 2606 OID 305719)
-- Name: all_doc_repertorio_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY allegati_doc_repertorio
    ADD CONSTRAINT all_doc_repertorio_pkey PRIMARY KEY (dc_id, doc_repertorio_id);


--
-- TOC entry 2783 (class 2606 OID 306079)
-- Name: all_doc_sezione_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY allegati_doc_amm_trasparente
    ADD CONSTRAINT all_doc_sezione_pkey PRIMARY KEY (dc_id, doc_sezione_id);


--
-- TOC entry 2622 (class 2606 OID 303875)
-- Name: allacci_protocollo_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_allacci
    ADD CONSTRAINT allacci_protocollo_pkey PRIMARY KEY (allaccio_id);


--
-- TOC entry 2528 (class 2606 OID 303877)
-- Name: amministrazione_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY amministrazione
    ADD CONSTRAINT amministrazione_pkey PRIMARY KEY (amministrazione_id);


--
-- TOC entry 2735 (class 2606 OID 305111)
-- Name: amministrazioni_partecipanti_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY amministrazioni_partecipanti
    ADD CONSTRAINT amministrazioni_partecipanti_pkey PRIMARY KEY (amministrazioni_id);


--
-- TOC entry 2530 (class 2606 OID 303879)
-- Name: aree_organizzative_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY aree_organizzative
    ADD CONSTRAINT aree_organizzative_pkey PRIMARY KEY (aoo_id);


--
-- TOC entry 2630 (class 2606 OID 303881)
-- Name: assegnatari_protocollo_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_assegnatari
    ADD CONSTRAINT assegnatari_protocollo_pkey PRIMARY KEY (assegnatario_id);


--
-- TOC entry 2753 (class 2606 OID 305426)
-- Name: assegnatario$lista_distribuzione_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY assegnatari_lista_distribuzione
    ADD CONSTRAINT "assegnatario$lista_distribuzione_pkey" PRIMARY KEY (id_assegnatario_lista);


--
-- TOC entry 2535 (class 2606 OID 303883)
-- Name: ca_url_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY ca_crl
    ADD CONSTRAINT ca_url_pkey PRIMARY KEY (id);


--
-- TOC entry 2727 (class 2606 OID 304955)
-- Name: cariche_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY cariche
    ADD CONSTRAINT cariche_pkey PRIMARY KEY (carica_id);


--
-- TOC entry 2542 (class 2606 OID 303885)
-- Name: caselle_email_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY caselle_email
    ADD CONSTRAINT caselle_email_pkey PRIMARY KEY (casella_id);


--
-- TOC entry 2751 (class 2606 OID 305372)
-- Name: check_posta_interna_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY check_posta_interna
    ADD CONSTRAINT check_posta_interna_pkey PRIMARY KEY (check_id);


--
-- TOC entry 2636 (class 2606 OID 303887)
-- Name: destinatari_protocollo_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_destinatari
    ADD CONSTRAINT destinatari_protocollo_pkey PRIMARY KEY (destinatario_id);


--
-- TOC entry 2547 (class 2606 OID 303891)
-- Name: doc_file_attr_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY doc_file_attr
    ADD CONSTRAINT doc_file_attr_pkey PRIMARY KEY (dfa_id);


--
-- TOC entry 2549 (class 2606 OID 303893)
-- Name: doc_file_permessi_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY doc_file_permessi
    ADD CONSTRAINT doc_file_permessi_pkey PRIMARY KEY (dfp_id);


--
-- TOC entry 2551 (class 2606 OID 303895)
-- Name: doc_file_rep_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY doc_file_rep
    ADD CONSTRAINT doc_file_rep_pkey PRIMARY KEY (dfr_id);


--
-- TOC entry 2763 (class 2606 OID 305709)
-- Name: doc_repertorio_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY documenti_repertori
    ADD CONSTRAINT doc_repertorio_pkey PRIMARY KEY (doc_repertorio_id);


--
-- TOC entry 2781 (class 2606 OID 306051)
-- Name: doc_sezione_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY amm_trasparente_documenti
    ADD CONSTRAINT doc_sezione_pkey PRIMARY KEY (doc_id);


--
-- TOC entry 2578 (class 2606 OID 303897)
-- Name: documenti$fascicoli_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY fascicolo_documenti
    ADD CONSTRAINT "documenti$fascicoli_pkey" PRIMARY KEY (dfa_id, fascicolo_id);


--
-- TOC entry 2743 (class 2606 OID 305239)
-- Name: documenti_editor_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY documenti_editor
    ADD CONSTRAINT documenti_editor_pkey PRIMARY KEY (documento_id);


--
-- TOC entry 2553 (class 2606 OID 303899)
-- Name: documenti_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY documenti
    ADD CONSTRAINT documenti_pkey PRIMARY KEY (documento_id);


--
-- TOC entry 2555 (class 2606 OID 303901)
-- Name: email_coda_invio_pk; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY email_coda_invio
    ADD CONSTRAINT email_coda_invio_pk PRIMARY KEY (id);


--
-- TOC entry 2559 (class 2606 OID 303903)
-- Name: email_ingresso_allegati_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY email_ingresso_allegati
    ADD CONSTRAINT email_ingresso_allegati_pkey PRIMARY KEY (id);


--
-- TOC entry 2561 (class 2606 OID 303905)
-- Name: email_ingresso_logs_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY email_logs
    ADD CONSTRAINT email_ingresso_logs_pkey PRIMARY KEY (email_id);


--
-- TOC entry 2557 (class 2606 OID 303907)
-- Name: email_ingresso_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY email_ingresso
    ADD CONSTRAINT email_ingresso_pkey PRIMARY KEY (email_id);


--
-- TOC entry 2565 (class 2606 OID 303909)
-- Name: faldone_procedimenti_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY faldone_procedimenti
    ADD CONSTRAINT faldone_procedimenti_pkey PRIMARY KEY (faldone_id, procedimento_id);


--
-- TOC entry 2567 (class 2606 OID 303911)
-- Name: faldoni_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY faldoni
    ADD CONSTRAINT faldoni_pkey PRIMARY KEY (faldone_id);


--
-- TOC entry 2563 (class 2606 OID 303913)
-- Name: fascicoli_faldoni_key; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY faldone_fascicoli
    ADD CONSTRAINT fascicoli_faldoni_key PRIMARY KEY (faldone_id, fascicolo_id, versione);


--
-- TOC entry 2572 (class 2606 OID 303915)
-- Name: fascicoli_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY fascicoli
    ADD CONSTRAINT fascicoli_pkey PRIMARY KEY (fascicolo_id);


--
-- TOC entry 2574 (class 2606 OID 305475)
-- Name: fascicoli_progressivo_parent_titolario_anno_key; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY fascicoli
    ADD CONSTRAINT fascicoli_progressivo_parent_titolario_anno_key UNIQUE (anno_riferimento, progressivo, parent_id, titolario_id);


--
-- TOC entry 2749 (class 2606 OID 305297)
-- Name: fascicolo_allaccio_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY fascicolo_allacci
    ADD CONSTRAINT fascicolo_allaccio_pkey PRIMARY KEY (fascicolo_id, fascicolo_allacciato_id);


--
-- TOC entry 2586 (class 2606 OID 303917)
-- Name: ic_pk_id; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY invio_classificati_destinatari
    ADD CONSTRAINT ic_pk_id PRIMARY KEY (id);


--
-- TOC entry 2747 (class 2606 OID 305265)
-- Name: icf_pk_id; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY invio_classificati_fascicoli
    ADD CONSTRAINT icf_pk_id PRIMARY KEY (id);


--
-- TOC entry 2582 (class 2606 OID 303919)
-- Name: identificativi_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY identificativi
    ADD CONSTRAINT identificativi_pkey PRIMARY KEY (nome_tabella);


--
-- TOC entry 2590 (class 2606 OID 303921)
-- Name: if_pk_id; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY invio_fascicoli_destinatari
    ADD CONSTRAINT if_pk_id PRIMARY KEY (id);


--
-- TOC entry 2773 (class 2606 OID 305907)
-- Name: job_scheduled_logs_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY job_scheduled_logs
    ADD CONSTRAINT job_scheduled_logs_pkey PRIMARY KEY (js_id);


--
-- TOC entry 2538 (class 2606 OID 303923)
-- Name: lista_ca_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY ca_lista
    ADD CONSTRAINT lista_ca_pkey PRIMARY KEY (ca_id);


--
-- TOC entry 2592 (class 2606 OID 303925)
-- Name: lista_distr_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY lista_distribuzione
    ADD CONSTRAINT lista_distr_pkey PRIMARY KEY (id);


--
-- TOC entry 2769 (class 2606 OID 305816)
-- Name: mail_config_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY mail_config
    ADD CONSTRAINT mail_config_pkey PRIMARY KEY (config_id);


--
-- TOC entry 2596 (class 2606 OID 303927)
-- Name: menu_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY menu
    ADD CONSTRAINT menu_pkey PRIMARY KEY (menu_id);


--
-- TOC entry 2641 (class 2606 OID 303929)
-- Name: mittenti_protocollo_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_mittenti
    ADD CONSTRAINT mittenti_protocollo_pkey PRIMARY KEY (mittente_id);


--
-- TOC entry 2598 (class 2606 OID 305218)
-- Name: nome_univoco_menu; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY menu
    ADD CONSTRAINT nome_univoco_menu UNIQUE (unique_name);


--
-- TOC entry 2771 (class 2606 OID 305849)
-- Name: notifiche_fattura_elettronica_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY notifiche_fattura_elettronica
    ADD CONSTRAINT notifiche_fattura_elettronica_pkey PRIMARY KEY (nfe_id);


--
-- TOC entry 2612 (class 2606 OID 303931)
-- Name: numero_univoco; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocolli
    ADD CONSTRAINT numero_univoco UNIQUE (registro_id, anno_registrazione, nume_protocollo);


--
-- TOC entry 2600 (class 2606 OID 303933)
-- Name: oggetti_key; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY oggetti
    ADD CONSTRAINT oggetti_key PRIMARY KEY (id);


--
-- TOC entry 2725 (class 2606 OID 304937)
-- Name: oggetto_assegnatari_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY oggetto_assegnatari
    ADD CONSTRAINT oggetto_assegnatari_pkey PRIMARY KEY (ufficio_id, oggetto_id);


--
-- TOC entry 2729 (class 2606 OID 304975)
-- Name: permessi_carica_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY permessi_carica
    ADD CONSTRAINT permessi_carica_pkey PRIMARY KEY (carica_id, menu_id);


--
-- TOC entry 2606 (class 2606 OID 303937)
-- Name: pf_procedimento_fascicoli; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY procedimenti_fascicolo
    ADD CONSTRAINT pf_procedimento_fascicoli PRIMARY KEY (procedimento_id, fascicolo_id);


--
-- TOC entry 2682 (class 2606 OID 303939)
-- Name: pf_storia_procedim_fascic; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_procedimenti_fascicolo
    ADD CONSTRAINT pf_storia_procedim_fascic PRIMARY KEY (procedimento_id, fascicolo_id, versione);


--
-- TOC entry 2545 (class 2606 OID 303941)
-- Name: pk; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY doc_cartelle
    ADD CONSTRAINT pk PRIMARY KEY (dc_id);


--
-- TOC entry 2644 (class 2606 OID 303943)
-- Name: pk_ass_prot_proc; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_procedimenti
    ADD CONSTRAINT pk_ass_prot_proc PRIMARY KEY (procedimento_id, protocollo_id);


--
-- TOC entry 2584 (class 2606 OID 303945)
-- Name: pk_invio_classificati; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY invio_classificati
    ADD CONSTRAINT pk_invio_classificati PRIMARY KEY (id);


--
-- TOC entry 2588 (class 2606 OID 303947)
-- Name: pk_invio_fascicoli; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY invio_fascicoli
    ADD CONSTRAINT pk_invio_fascicoli PRIMARY KEY (id);


--
-- TOC entry 2696 (class 2606 OID 303953)
-- Name: pk_sto_prot_proc; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocollo_procedimenti
    ADD CONSTRAINT pk_sto_prot_proc PRIMARY KEY (procedimento_id, protocollo_id, versione);


--
-- TOC entry 2705 (class 2606 OID 303955)
-- Name: pk_tipi_procedimento; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY tipi_procedimento
    ADD CONSTRAINT pk_tipi_procedimento PRIMARY KEY (tipo_procedimenti_id);


--
-- TOC entry 2604 (class 2606 OID 303957)
-- Name: procedimenti_faldone_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY procedimenti_faldone
    ADD CONSTRAINT procedimenti_faldone_pkey PRIMARY KEY (procedimento_id, faldone_id);


--
-- TOC entry 2737 (class 2606 OID 305126)
-- Name: procedimento_istruttori_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY procedimento_istruttori
    ADD CONSTRAINT procedimento_istruttori_pkey PRIMARY KEY (procedimento_id, carica_id);


--
-- TOC entry 2602 (class 2606 OID 303959)
-- Name: procedimento_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY procedimenti
    ADD CONSTRAINT procedimento_pkey PRIMARY KEY (procedimento_id);


--
-- TOC entry 2610 (class 2606 OID 303961)
-- Name: profili$menu_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY "profili$menu"
    ADD CONSTRAINT "profili$menu_pkey" PRIMARY KEY (profilo_id, menu_id);


--
-- TOC entry 2608 (class 2606 OID 303963)
-- Name: profili_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY profili
    ADD CONSTRAINT profili_pkey PRIMARY KEY (profilo_id);


--
-- TOC entry 2580 (class 2606 OID 303965)
-- Name: protocolli$fascicoli_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY fascicolo_protocolli
    ADD CONSTRAINT "protocolli$fascicoli_pkey" PRIMARY KEY (protocollo_id, fascicolo_id);


--
-- TOC entry 2614 (class 2606 OID 303967)
-- Name: protocolli_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocolli
    ADD CONSTRAINT protocolli_pkey PRIMARY KEY (protocollo_id);


--
-- TOC entry 2626 (class 2606 OID 303969)
-- Name: protocollo_allegati_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_allegati
    ADD CONSTRAINT protocollo_allegati_pkey PRIMARY KEY (allegato_id);


--
-- TOC entry 2628 (class 2606 OID 303971)
-- Name: protocollo_annotazioni_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_annotazioni
    ADD CONSTRAINT protocollo_annotazioni_pkey PRIMARY KEY (annotazione_id);


--
-- TOC entry 2638 (class 2606 OID 303973)
-- Name: protocollo_destinatari_destinatario_id_key; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_destinatari
    ADD CONSTRAINT protocollo_destinatari_destinatario_id_key UNIQUE (destinatario_id, protocollo_id, mezzo_spedizione_id);


--
-- TOC entry 2646 (class 2606 OID 303975)
-- Name: province_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY province
    ADD CONSTRAINT province_pkey PRIMARY KEY (provincia_id);


--
-- TOC entry 2777 (class 2606 OID 305989)
-- Name: puv_id_primary_key; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY procedimento_uffici_partecipanti
    ADD CONSTRAINT puv_id_primary_key PRIMARY KEY (puv_id);


--
-- TOC entry 2648 (class 2606 OID 303977)
-- Name: regioni_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY regioni
    ADD CONSTRAINT regioni_pkey PRIMARY KEY (regione_id);


--
-- TOC entry 2650 (class 2606 OID 303979)
-- Name: registri_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY registri
    ADD CONSTRAINT registri_pkey PRIMARY KEY (registro_id);


--
-- TOC entry 2616 (class 2606 OID 303981)
-- Name: registro_anno_numero; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocolli
    ADD CONSTRAINT registro_anno_numero UNIQUE (registro_anno_numero);


--
-- TOC entry 2767 (class 2606 OID 305737)
-- Name: repertorio_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY repertori
    ADD CONSTRAINT repertorio_pkey PRIMARY KEY (repertorio_id);


--
-- TOC entry 2733 (class 2606 OID 305091)
-- Name: riferimenti_legislativi_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY riferimenti_legislativi
    ADD CONSTRAINT riferimenti_legislativi_pkey PRIMARY KEY (riferimento_id);


--
-- TOC entry 2654 (class 2606 OID 303983)
-- Name: ripeti_dati_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY ripetidati
    ADD CONSTRAINT ripeti_dati_pkey PRIMARY KEY (utente_id);


--
-- TOC entry 2660 (class 2606 OID 304803)
-- Name: rubrica$lista_distribuzione_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY rubrica_lista_distribuzione
    ADD CONSTRAINT "rubrica$lista_distribuzione_pkey" PRIMARY KEY (id_rubrica, id_lista);


--
-- TOC entry 2656 (class 2606 OID 303985)
-- Name: rubrica_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY rubrica
    ADD CONSTRAINT rubrica_pkey PRIMARY KEY (rubrica_id);


--
-- TOC entry 2662 (class 2606 OID 303987)
-- Name: segnature_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY segnature
    ADD CONSTRAINT segnature_pkey PRIMARY KEY (segnature_id);


--
-- TOC entry 2779 (class 2606 OID 306030)
-- Name: sezione_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY amm_trasparente
    ADD CONSTRAINT sezione_pkey PRIMARY KEY (sezione_id);


--
-- TOC entry 2665 (class 2606 OID 303989)
-- Name: spedizioni_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY spedizioni
    ADD CONSTRAINT spedizioni_pkey PRIMARY KEY (spedizioni_id);


--
-- TOC entry 2759 (class 2606 OID 305512)
-- Name: st_org_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storico_organigramma
    ADD CONSTRAINT st_org_pkey PRIMARY KEY (st_org_id);


--
-- TOC entry 2678 (class 2606 OID 303991)
-- Name: st_protoc$fascicoli_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_fascicolo_protocolli
    ADD CONSTRAINT "st_protoc$fascicoli_pkey" PRIMARY KEY (protocollo_id, fascicolo_id, versione);


--
-- TOC entry 2676 (class 2606 OID 303999)
-- Name: storia$documenti$fascicoli_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_fascicolo_documenti
    ADD CONSTRAINT "storia$documenti$fascicoli_pkey" PRIMARY KEY (dfa_id, fascicolo_id, versione);


--
-- TOC entry 2686 (class 2606 OID 304001)
-- Name: storia_allacci_protocollo_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocollo_allacci
    ADD CONSTRAINT storia_allacci_protocollo_pkey PRIMARY KEY (allaccio_id, versione);


--
-- TOC entry 2761 (class 2606 OID 305584)
-- Name: storia_amministrazione_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_amministrazione
    ADD CONSTRAINT storia_amministrazione_pkey PRIMARY KEY (amministrazione_id, versione);


--
-- TOC entry 2692 (class 2606 OID 304003)
-- Name: storia_assegnatari_protocollo_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocollo_assegnatari
    ADD CONSTRAINT storia_assegnatari_protocollo_pkey PRIMARY KEY (assegnatario_id, versione);


--
-- TOC entry 2755 (class 2606 OID 305453)
-- Name: storia_cariche_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_cariche
    ADD CONSTRAINT storia_cariche_pkey PRIMARY KEY (carica_id, versione);


--
-- TOC entry 2694 (class 2606 OID 304005)
-- Name: storia_destinatari_protocollo_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocollo_destinatari
    ADD CONSTRAINT storia_destinatari_protocollo_pkey PRIMARY KEY (destinatario_id, versione);


--
-- TOC entry 2739 (class 2606 OID 305210)
-- Name: storia_doc_file_attr_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_doc_file_attr
    ADD CONSTRAINT storia_doc_file_attr_pkey PRIMARY KEY (dfa_id, versione);


--
-- TOC entry 2741 (class 2606 OID 305216)
-- Name: storia_doc_file_permessi_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_doc_file_permessi
    ADD CONSTRAINT storia_doc_file_permessi_pkey PRIMARY KEY (dfp_id, versione);


--
-- TOC entry 2745 (class 2606 OID 305253)
-- Name: storia_documenti_editor_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_documenti_editor
    ADD CONSTRAINT storia_documenti_editor_pkey PRIMARY KEY (documento_id, versione);


--
-- TOC entry 2667 (class 2606 OID 304011)
-- Name: storia_faldone_fascicoli_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_faldone_fascicoli
    ADD CONSTRAINT storia_faldone_fascicoli_pkey PRIMARY KEY (faldone_id, fascicolo_id, versione);


--
-- TOC entry 2669 (class 2606 OID 304013)
-- Name: storia_faldoni_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_faldoni
    ADD CONSTRAINT storia_faldoni_pkey PRIMARY KEY (faldone_id, versione);


--
-- TOC entry 2674 (class 2606 OID 304015)
-- Name: storia_fascicoli_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_fascicoli
    ADD CONSTRAINT storia_fascicoli_pkey PRIMARY KEY (fascicolo_id, versione);


--
-- TOC entry 2757 (class 2606 OID 305490)
-- Name: storia_procedimento_istruttori_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_procedimento_istruttori
    ADD CONSTRAINT storia_procedimento_istruttori_pkey PRIMARY KEY (procedimento_id, carica_id, versione);


--
-- TOC entry 2680 (class 2606 OID 304017)
-- Name: storia_procedimento_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_procedimenti
    ADD CONSTRAINT storia_procedimento_pkey PRIMARY KEY (procedimento_id, versione);


--
-- TOC entry 2684 (class 2606 OID 304019)
-- Name: storia_protocolli_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocolli
    ADD CONSTRAINT storia_protocolli_pkey PRIMARY KEY (protocollo_id, versione);


--
-- TOC entry 2688 (class 2606 OID 304021)
-- Name: storia_protocollo_allegati_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocollo_allegati
    ADD CONSTRAINT storia_protocollo_allegati_pkey PRIMARY KEY (allegato_id, versione);


--
-- TOC entry 2690 (class 2606 OID 304023)
-- Name: storia_protocollo_annotazioni_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocollo_annotazioni
    ADD CONSTRAINT storia_protocollo_annotazioni_pkey PRIMARY KEY (annotazione_id, versione);


--
-- TOC entry 2731 (class 2606 OID 305070)
-- Name: storia_protocollo_mittenti_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocollo_mittenti
    ADD CONSTRAINT storia_protocollo_mittenti_pkey PRIMARY KEY (mittente_id, versione);


--
-- TOC entry 2698 (class 2606 OID 304025)
-- Name: storia_registri_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_registri
    ADD CONSTRAINT storia_registri_pkey PRIMARY KEY (registri_id, versione);


--
-- TOC entry 2700 (class 2606 OID 304027)
-- Name: storia_titolario_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_titolario
    ADD CONSTRAINT storia_titolario_pkey PRIMARY KEY (titolario_id, versione);


--
-- TOC entry 2702 (class 2606 OID 304031)
-- Name: tipi_documento_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY tipi_documento
    ADD CONSTRAINT tipi_documento_pkey PRIMARY KEY (tipo_documento_id);


--
-- TOC entry 2711 (class 2606 OID 304033)
-- Name: titolario$uffici_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY "titolario$uffici"
    ADD CONSTRAINT "titolario$uffici_pkey" PRIMARY KEY (ufficio_id, titolario_id);


--
-- TOC entry 2707 (class 2606 OID 304035)
-- Name: titolario_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY titolario
    ADD CONSTRAINT titolario_pkey PRIMARY KEY (titolario_id);


--
-- TOC entry 2713 (class 2606 OID 304037)
-- Name: titolo_destinatario_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY titoli_destinatari
    ADD CONSTRAINT titolo_destinatario_pkey PRIMARY KEY (id);


--
-- TOC entry 2775 (class 2606 OID 305971)
-- Name: tpu_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY tipi_procedimento_uffici
    ADD CONSTRAINT tpu_pkey PRIMARY KEY (tpu_id);


--
-- TOC entry 2717 (class 2606 OID 304039)
-- Name: uffici_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY uffici
    ADD CONSTRAINT uffici_pkey PRIMARY KEY (ufficio_id);


--
-- TOC entry 2652 (class 2606 OID 305260)
-- Name: unique_reg_doc_aoo; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY registri
    ADD CONSTRAINT unique_reg_doc_aoo UNIQUE (codi_registro, aoo_id);


--
-- TOC entry 2723 (class 2606 OID 304043)
-- Name: utenti$registri_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY "utenti$registri"
    ADD CONSTRAINT "utenti$registri_pkey" PRIMARY KEY (utente_id, registro_id);


--
-- TOC entry 2719 (class 2606 OID 304045)
-- Name: utenti_pkey; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY utenti
    ADD CONSTRAINT utenti_pkey PRIMARY KEY (utente_id);


--
-- TOC entry 2721 (class 2606 OID 304047)
-- Name: utenti_user_name_key; Type: CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY utenti
    ADD CONSTRAINT utenti_user_name_key UNIQUE (user_name);


--
-- TOC entry 2533 (class 1259 OID 304048)
-- Name: ca_id_idx; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX ca_id_idx ON ca_crl USING btree (ca_id);


--
-- TOC entry 2539 (class 1259 OID 304049)
-- Name: ca_revoked_id_idx; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX ca_revoked_id_idx ON ca_revoked_list USING btree (ca_id);


--
-- TOC entry 2568 (class 1259 OID 304050)
-- Name: fascicoli_index1; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX fascicoli_index1 ON fascicoli USING btree (ufficio_intestatario_id);


--
-- TOC entry 2569 (class 1259 OID 304051)
-- Name: fascicoli_index2; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX fascicoli_index2 ON fascicoli USING btree (ufficio_responsabile_id);


--
-- TOC entry 2570 (class 1259 OID 304052)
-- Name: fascicoli_index3; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX fascicoli_index3 ON fascicoli USING btree (aoo_id);


--
-- TOC entry 2536 (class 1259 OID 304053)
-- Name: issuer_cn_idx; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE UNIQUE INDEX issuer_cn_idx ON ca_lista USING btree (ca_id);


--
-- TOC entry 2540 (class 1259 OID 304054)
-- Name: serial_number_idx; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX serial_number_idx ON ca_revoked_list USING btree (serial_number);


--
-- TOC entry 2670 (class 1259 OID 304055)
-- Name: storia_fascicoli_index1; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX storia_fascicoli_index1 ON storia_fascicoli USING btree (ufficio_intestatario_id);


--
-- TOC entry 2671 (class 1259 OID 304056)
-- Name: storia_fascicoli_index2; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX storia_fascicoli_index2 ON storia_fascicoli USING btree (ufficio_responsabile_id);


--
-- TOC entry 2672 (class 1259 OID 304057)
-- Name: storia_fascicoli_index3; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX storia_fascicoli_index3 ON storia_fascicoli USING btree (aoo_id);


--
-- TOC entry 2714 (class 1259 OID 304058)
-- Name: uffici_idx1; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX uffici_idx1 ON uffici USING btree (parent_id);


--
-- TOC entry 2715 (class 1259 OID 304059)
-- Name: uffici_idx2; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX uffici_idx2 ON uffici USING btree (aoo_id);


--
-- TOC entry 2623 (class 1259 OID 304060)
-- Name: xif1allacci_protocollo; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX xif1allacci_protocollo ON protocollo_allacci USING btree (protocollo_id);


--
-- TOC entry 2631 (class 1259 OID 304061)
-- Name: xif1assegnatari_protocollo; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX xif1assegnatari_protocollo ON protocollo_assegnatari USING btree (protocollo_id);


--
-- TOC entry 2639 (class 1259 OID 304062)
-- Name: xif1destinatari_protocollo; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX xif1destinatari_protocollo ON protocollo_destinatari USING btree (protocollo_id);


--
-- TOC entry 2642 (class 1259 OID 304064)
-- Name: xif1mittenti_protocollo; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX xif1mittenti_protocollo ON protocollo_mittenti USING btree (protocollo_id);


--
-- TOC entry 2663 (class 1259 OID 304065)
-- Name: xif1segnature; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX xif1segnature ON segnature USING btree (protocollo_id);


--
-- TOC entry 2703 (class 1259 OID 304067)
-- Name: xif1tipi_documento; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX xif1tipi_documento ON tipi_documento USING btree (aoo_id);


--
-- TOC entry 2624 (class 1259 OID 304068)
-- Name: xif2allacci_protocollo; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX xif2allacci_protocollo ON protocollo_allacci USING btree (protocollo_allacciato_id);


--
-- TOC entry 2531 (class 1259 OID 304069)
-- Name: xif2aree_organizzative; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX xif2aree_organizzative ON aree_organizzative USING btree (provincia_id);


--
-- TOC entry 2632 (class 1259 OID 304070)
-- Name: xif2assegnatari_protocollo; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX xif2assegnatari_protocollo ON protocollo_assegnatari USING btree (utente_assegnatario_id);


--
-- TOC entry 2543 (class 1259 OID 304071)
-- Name: xif2caselle_email; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX xif2caselle_email ON caselle_email USING btree (fk_aoo);


--
-- TOC entry 2708 (class 1259 OID 304072)
-- Name: xif2titolario; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX xif2titolario ON titolario USING btree (parent_id);


--
-- TOC entry 2532 (class 1259 OID 304073)
-- Name: xif3aree_organizzative; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX xif3aree_organizzative ON aree_organizzative USING btree (amministrazione_id);


--
-- TOC entry 2575 (class 1259 OID 304075)
-- Name: xif3fascicoli; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX xif3fascicoli ON fascicoli USING btree (registro_id);


--
-- TOC entry 2617 (class 1259 OID 304076)
-- Name: xif3protocolli; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX xif3protocolli ON protocolli USING btree (tipo_documento_id);


--
-- TOC entry 2709 (class 1259 OID 304077)
-- Name: xif3titolario; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX xif3titolario ON titolario USING btree (aoo_id);


--
-- TOC entry 2576 (class 1259 OID 304078)
-- Name: xif4fascicoli; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX xif4fascicoli ON fascicoli USING btree (titolario_id);


--
-- TOC entry 2618 (class 1259 OID 304079)
-- Name: xif4protocolli; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX xif4protocolli ON protocolli USING btree (titolario_id);


--
-- TOC entry 2657 (class 1259 OID 304080)
-- Name: xif4rubrica; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX xif4rubrica ON rubrica USING btree (provincia_nascita_id);


--
-- TOC entry 2658 (class 1259 OID 304081)
-- Name: xif5rubrica; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX xif5rubrica ON rubrica USING btree (provincia_id);


--
-- TOC entry 2619 (class 1259 OID 304083)
-- Name: xif7protocolli; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX xif7protocolli ON protocolli USING btree (ufficio_protocollatore_id);


--
-- TOC entry 2633 (class 1259 OID 304084)
-- Name: xif8assegnatari_protocollo; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX xif8assegnatari_protocollo ON protocollo_assegnatari USING btree (ufficio_assegnante_id);


--
-- TOC entry 2634 (class 1259 OID 304085)
-- Name: xif9assegnatari_protocollo; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX xif9assegnatari_protocollo ON protocollo_assegnatari USING btree (ufficio_assegnatario_id);


--
-- TOC entry 2620 (class 1259 OID 304086)
-- Name: xif9protocolli; Type: INDEX; Schema: fenice; Owner: fenice
--

CREATE INDEX xif9protocolli ON protocolli USING btree (utente_mittente_id);


--
-- TOC entry 2878 (class 2606 OID 304087)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY province
    ADD CONSTRAINT "$1" FOREIGN KEY (regione_id) REFERENCES regioni(regione_id);


--
-- TOC entry 2785 (class 2606 OID 304092)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY aree_organizzative
    ADD CONSTRAINT "$1" FOREIGN KEY (provincia_id) REFERENCES province(provincia_id);


--
-- TOC entry 2920 (class 2606 OID 304097)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY uffici
    ADD CONSTRAINT "$1" FOREIGN KEY (parent_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2880 (class 2606 OID 304102)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY rubrica
    ADD CONSTRAINT "$1" FOREIGN KEY (aoo_id) REFERENCES aree_organizzative(aoo_id);


--
-- TOC entry 2911 (class 2606 OID 304107)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY tipi_documento
    ADD CONSTRAINT "$1" FOREIGN KEY (aoo_id) REFERENCES aree_organizzative(aoo_id);


--
-- TOC entry 2913 (class 2606 OID 304112)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY titolario
    ADD CONSTRAINT "$1" FOREIGN KEY (titolario_id) REFERENCES titolario(titolario_id);


--
-- TOC entry 2922 (class 2606 OID 304117)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY utenti
    ADD CONSTRAINT "$1" FOREIGN KEY (aoo_id) REFERENCES aree_organizzative(aoo_id);


--
-- TOC entry 2790 (class 2606 OID 304122)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY caselle_email
    ADD CONSTRAINT "$1" FOREIGN KEY (fk_aoo) REFERENCES aree_organizzative(aoo_id);


--
-- TOC entry 2859 (class 2606 OID 304127)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_allacci
    ADD CONSTRAINT "$1" FOREIGN KEY (protocollo_id) REFERENCES protocolli(protocollo_id);


--
-- TOC entry 2923 (class 2606 OID 304132)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY "utenti$registri"
    ADD CONSTRAINT "$1" FOREIGN KEY (utente_id) REFERENCES utenti(utente_id);


--
-- TOC entry 2885 (class 2606 OID 304147)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY segnature
    ADD CONSTRAINT "$1" FOREIGN KEY (protocollo_id) REFERENCES protocolli(protocollo_id);


--
-- TOC entry 2865 (class 2606 OID 304152)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_assegnatari
    ADD CONSTRAINT "$1" FOREIGN KEY (protocollo_id) REFERENCES protocolli(protocollo_id);


--
-- TOC entry 2872 (class 2606 OID 304157)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_destinatari
    ADD CONSTRAINT "$1" FOREIGN KEY (protocollo_id) REFERENCES protocolli(protocollo_id);


--
-- TOC entry 2820 (class 2606 OID 304167)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY fascicolo_protocolli
    ADD CONSTRAINT "$1" FOREIGN KEY (protocollo_id) REFERENCES protocolli(protocollo_id);


--
-- TOC entry 2907 (class 2606 OID 304172)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_registri
    ADD CONSTRAINT "$1" FOREIGN KEY (aoo_id) REFERENCES aree_organizzative(aoo_id);


--
-- TOC entry 2879 (class 2606 OID 304177)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY registri
    ADD CONSTRAINT "$1" FOREIGN KEY (aoo_id) REFERENCES aree_organizzative(aoo_id);


--
-- TOC entry 2809 (class 2606 OID 304182)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY fascicoli
    ADD CONSTRAINT "$1" FOREIGN KEY (registro_id) REFERENCES registri(registro_id);


--
-- TOC entry 2918 (class 2606 OID 304187)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY "titolario$uffici"
    ADD CONSTRAINT "$1" FOREIGN KEY (ufficio_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2848 (class 2606 OID 304192)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY "profili$menu"
    ADD CONSTRAINT "$1" FOREIGN KEY (profilo_id) REFERENCES profili(profilo_id);


--
-- TOC entry 2864 (class 2606 OID 304197)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_annotazioni
    ADD CONSTRAINT "$1" FOREIGN KEY (protocollo_id) REFERENCES protocolli(protocollo_id);


--
-- TOC entry 2850 (class 2606 OID 304202)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocolli
    ADD CONSTRAINT "$1" FOREIGN KEY (tipo_documento_id) REFERENCES tipi_documento(tipo_documento_id);


--
-- TOC entry 2896 (class 2606 OID 304207)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocollo_allacci
    ADD CONSTRAINT "$1" FOREIGN KEY (protocollo_id, versione) REFERENCES storia_protocolli(protocollo_id, versione);


--
-- TOC entry 2899 (class 2606 OID 304212)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocollo_annotazioni
    ADD CONSTRAINT "$1" FOREIGN KEY (protocollo_id, versione) REFERENCES storia_protocolli(protocollo_id, versione);


--
-- TOC entry 2900 (class 2606 OID 304217)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocollo_assegnatari
    ADD CONSTRAINT "$1" FOREIGN KEY (protocollo_id, versione) REFERENCES storia_protocolli(protocollo_id, versione);


--
-- TOC entry 2905 (class 2606 OID 304222)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocollo_destinatari
    ADD CONSTRAINT "$1" FOREIGN KEY (protocollo_id, versione) REFERENCES storia_protocolli(protocollo_id, versione);


--
-- TOC entry 2908 (class 2606 OID 304227)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_titolario
    ADD CONSTRAINT "$1" FOREIGN KEY (titolario_id) REFERENCES titolario(titolario_id);


--
-- TOC entry 2875 (class 2606 OID 304232)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_mittenti
    ADD CONSTRAINT "$1" FOREIGN KEY (protocollo_id) REFERENCES protocolli(protocollo_id);


--
-- TOC entry 2925 (class 2606 OID 304938)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY oggetto_assegnatari
    ADD CONSTRAINT "$1" FOREIGN KEY (ufficio_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2930 (class 2606 OID 304976)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY permessi_carica
    ADD CONSTRAINT "$1" FOREIGN KEY (carica_id) REFERENCES cariche(carica_id);


--
-- TOC entry 2932 (class 2606 OID 305071)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocollo_mittenti
    ADD CONSTRAINT "$1" FOREIGN KEY (protocollo_id, versione) REFERENCES storia_protocolli(protocollo_id, versione);


--
-- TOC entry 2944 (class 2606 OID 305298)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY fascicolo_allacci
    ADD CONSTRAINT "$1" FOREIGN KEY (fascicolo_id) REFERENCES fascicoli(fascicolo_id);


--
-- TOC entry 2947 (class 2606 OID 305373)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY check_posta_interna
    ADD CONSTRAINT "$1" FOREIGN KEY (protocollo_id) REFERENCES protocolli(protocollo_id);


--
-- TOC entry 2967 (class 2606 OID 305972)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY tipi_procedimento_uffici
    ADD CONSTRAINT "$1" FOREIGN KEY (tipo_procedimenti_id) REFERENCES tipi_procedimento(tipo_procedimenti_id);


--
-- TOC entry 2969 (class 2606 OID 305990)
-- Name: $1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY procedimento_uffici_partecipanti
    ADD CONSTRAINT "$1" FOREIGN KEY (procedimento_id) REFERENCES procedimenti(procedimento_id);


--
-- TOC entry 2888 (class 2606 OID 304237)
-- Name: $10; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocolli
    ADD CONSTRAINT "$10" FOREIGN KEY (documento_id) REFERENCES documenti(documento_id);


--
-- TOC entry 2786 (class 2606 OID 304242)
-- Name: $2; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY aree_organizzative
    ADD CONSTRAINT "$2" FOREIGN KEY (amministrazione_id) REFERENCES amministrazione(amministrazione_id);


--
-- TOC entry 2921 (class 2606 OID 304247)
-- Name: $2; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY uffici
    ADD CONSTRAINT "$2" FOREIGN KEY (aoo_id) REFERENCES aree_organizzative(aoo_id);


--
-- TOC entry 2881 (class 2606 OID 304252)
-- Name: $2; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY rubrica
    ADD CONSTRAINT "$2" FOREIGN KEY (provincia_nascita_id) REFERENCES province(provincia_id);


--
-- TOC entry 2914 (class 2606 OID 304257)
-- Name: $2; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY titolario
    ADD CONSTRAINT "$2" FOREIGN KEY (parent_id) REFERENCES titolario(titolario_id);


--
-- TOC entry 2860 (class 2606 OID 304262)
-- Name: $2; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_allacci
    ADD CONSTRAINT "$2" FOREIGN KEY (protocollo_allacciato_id) REFERENCES protocolli(protocollo_id);


--
-- TOC entry 2810 (class 2606 OID 304267)
-- Name: $2; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY fascicoli
    ADD CONSTRAINT "$2" FOREIGN KEY (titolario_id) REFERENCES titolario(titolario_id);


--
-- TOC entry 2919 (class 2606 OID 304272)
-- Name: $2; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY "titolario$uffici"
    ADD CONSTRAINT "$2" FOREIGN KEY (titolario_id) REFERENCES titolario(titolario_id);


--
-- TOC entry 2866 (class 2606 OID 304282)
-- Name: $2; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_assegnatari
    ADD CONSTRAINT "$2" FOREIGN KEY (ufficio_assegnante_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2821 (class 2606 OID 304292)
-- Name: $2; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY fascicolo_protocolli
    ADD CONSTRAINT "$2" FOREIGN KEY (fascicolo_id) REFERENCES fascicoli(fascicolo_id);


--
-- TOC entry 2924 (class 2606 OID 304297)
-- Name: $2; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY "utenti$registri"
    ADD CONSTRAINT "$2" FOREIGN KEY (registro_id) REFERENCES registri(registro_id);


--
-- TOC entry 2849 (class 2606 OID 304302)
-- Name: $2; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY "profili$menu"
    ADD CONSTRAINT "$2" FOREIGN KEY (menu_id) REFERENCES menu(menu_id);


--
-- TOC entry 2901 (class 2606 OID 304307)
-- Name: $2; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocollo_assegnatari
    ADD CONSTRAINT "$2" FOREIGN KEY (ufficio_assegnante_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2889 (class 2606 OID 304312)
-- Name: $2; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocolli
    ADD CONSTRAINT "$2" FOREIGN KEY (tipo_documento_id) REFERENCES tipi_documento(tipo_documento_id);


--
-- TOC entry 2851 (class 2606 OID 304317)
-- Name: $2; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocolli
    ADD CONSTRAINT "$2" FOREIGN KEY (registro_id) REFERENCES registri(registro_id);


--
-- TOC entry 2909 (class 2606 OID 304322)
-- Name: $2; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_titolario
    ADD CONSTRAINT "$2" FOREIGN KEY (parent_id) REFERENCES titolario(titolario_id);


--
-- TOC entry 2926 (class 2606 OID 304943)
-- Name: $2; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY oggetto_assegnatari
    ADD CONSTRAINT "$2" FOREIGN KEY (oggetto_id) REFERENCES oggetti(id);


--
-- TOC entry 2931 (class 2606 OID 304981)
-- Name: $2; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY permessi_carica
    ADD CONSTRAINT "$2" FOREIGN KEY (menu_id) REFERENCES menu(menu_id);


--
-- TOC entry 2945 (class 2606 OID 305303)
-- Name: $2; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY fascicolo_allacci
    ADD CONSTRAINT "$2" FOREIGN KEY (fascicolo_allacciato_id) REFERENCES fascicoli(fascicolo_id);


--
-- TOC entry 2948 (class 2606 OID 305378)
-- Name: $2; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY check_posta_interna
    ADD CONSTRAINT "$2" FOREIGN KEY (ufficio_assegnante_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2968 (class 2606 OID 305977)
-- Name: $2; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY tipi_procedimento_uffici
    ADD CONSTRAINT "$2" FOREIGN KEY (ufficio_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2970 (class 2606 OID 305995)
-- Name: $2; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY procedimento_uffici_partecipanti
    ADD CONSTRAINT "$2" FOREIGN KEY (ufficio_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2882 (class 2606 OID 304327)
-- Name: $3; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY rubrica
    ADD CONSTRAINT "$3" FOREIGN KEY (provincia_id) REFERENCES province(provincia_id);


--
-- TOC entry 2915 (class 2606 OID 304332)
-- Name: $3; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY titolario
    ADD CONSTRAINT "$3" FOREIGN KEY (aoo_id) REFERENCES aree_organizzative(aoo_id);


--
-- TOC entry 2867 (class 2606 OID 304337)
-- Name: $3; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_assegnatari
    ADD CONSTRAINT "$3" FOREIGN KEY (ufficio_assegnatario_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2861 (class 2606 OID 304342)
-- Name: $3; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_allacci
    ADD CONSTRAINT "$3" FOREIGN KEY (protocollo_id) REFERENCES protocolli(protocollo_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2902 (class 2606 OID 304352)
-- Name: $3; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocollo_assegnatari
    ADD CONSTRAINT "$3" FOREIGN KEY (ufficio_assegnatario_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2890 (class 2606 OID 304357)
-- Name: $3; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocolli
    ADD CONSTRAINT "$3" FOREIGN KEY (registro_id) REFERENCES registri(registro_id);


--
-- TOC entry 2852 (class 2606 OID 304362)
-- Name: $3; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocolli
    ADD CONSTRAINT "$3" FOREIGN KEY (titolario_id) REFERENCES titolario(titolario_id);


--
-- TOC entry 2910 (class 2606 OID 304367)
-- Name: $3; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_titolario
    ADD CONSTRAINT "$3" FOREIGN KEY (aoo_id) REFERENCES aree_organizzative(aoo_id);


--
-- TOC entry 2949 (class 2606 OID 305383)
-- Name: $3; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY check_posta_interna
    ADD CONSTRAINT "$3" FOREIGN KEY (ufficio_assegnatario_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2868 (class 2606 OID 304372)
-- Name: $4; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_assegnatari
    ADD CONSTRAINT "$4" FOREIGN KEY (utente_assegnatario_id) REFERENCES utenti(utente_id);


--
-- TOC entry 2903 (class 2606 OID 304377)
-- Name: $4; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocollo_assegnatari
    ADD CONSTRAINT "$4" FOREIGN KEY (utente_assegnatario_id) REFERENCES utenti(utente_id);


--
-- TOC entry 2891 (class 2606 OID 304382)
-- Name: $4; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocolli
    ADD CONSTRAINT "$4" FOREIGN KEY (titolario_id) REFERENCES titolario(titolario_id);


--
-- TOC entry 2858 (class 2606 OID 305001)
-- Name: $4; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocolli
    ADD CONSTRAINT "$4" FOREIGN KEY (carica_protocollatore_id) REFERENCES cariche(carica_id);


--
-- TOC entry 2869 (class 2606 OID 304392)
-- Name: $5; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_assegnatari
    ADD CONSTRAINT "$5" FOREIGN KEY (utente_assegnante_id) REFERENCES utenti(utente_id);


--
-- TOC entry 2904 (class 2606 OID 304397)
-- Name: $5; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocollo_assegnatari
    ADD CONSTRAINT "$5" FOREIGN KEY (utente_assegnante_id) REFERENCES utenti(utente_id);


--
-- TOC entry 2853 (class 2606 OID 304407)
-- Name: $5; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocolli
    ADD CONSTRAINT "$5" FOREIGN KEY (ufficio_protocollatore_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2892 (class 2606 OID 304412)
-- Name: $6; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocolli
    ADD CONSTRAINT "$6" FOREIGN KEY (ufficio_protocollatore_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2854 (class 2606 OID 304417)
-- Name: $6; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocolli
    ADD CONSTRAINT "$6" FOREIGN KEY (ufficio_mittente_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2870 (class 2606 OID 305006)
-- Name: $6; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_assegnatari
    ADD CONSTRAINT "$6" FOREIGN KEY (carica_assegnatario_id) REFERENCES cariche(carica_id);


--
-- TOC entry 2950 (class 2606 OID 305388)
-- Name: $6; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY check_posta_interna
    ADD CONSTRAINT "$6" FOREIGN KEY (carica_assegnatario_id) REFERENCES cariche(carica_id);


--
-- TOC entry 2893 (class 2606 OID 304422)
-- Name: $7; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocolli
    ADD CONSTRAINT "$7" FOREIGN KEY (ufficio_mittente_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2855 (class 2606 OID 304427)
-- Name: $7; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocolli
    ADD CONSTRAINT "$7" FOREIGN KEY (utente_mittente_id) REFERENCES utenti(utente_id);


--
-- TOC entry 2871 (class 2606 OID 305011)
-- Name: $7; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_assegnatari
    ADD CONSTRAINT "$7" FOREIGN KEY (carica_assegnante_id) REFERENCES cariche(carica_id);


--
-- TOC entry 2951 (class 2606 OID 305393)
-- Name: $7; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY check_posta_interna
    ADD CONSTRAINT "$7" FOREIGN KEY (carica_assegnante_id) REFERENCES cariche(carica_id);


--
-- TOC entry 2894 (class 2606 OID 304432)
-- Name: $8; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocolli
    ADD CONSTRAINT "$8" FOREIGN KEY (utente_mittente_id) REFERENCES utenti(utente_id);


--
-- TOC entry 2856 (class 2606 OID 304437)
-- Name: $8; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocolli
    ADD CONSTRAINT "$8" FOREIGN KEY (aoo_id) REFERENCES aree_organizzative(aoo_id);


--
-- TOC entry 2895 (class 2606 OID 304442)
-- Name: $9; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocolli
    ADD CONSTRAINT "$9" FOREIGN KEY (aoo_id) REFERENCES aree_organizzative(aoo_id);


--
-- TOC entry 2857 (class 2606 OID 304447)
-- Name: $9; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocolli
    ADD CONSTRAINT "$9" FOREIGN KEY (documento_id) REFERENCES documenti(documento_id);


--
-- TOC entry 2961 (class 2606 OID 305720)
-- Name: all_doc_repertorio_documento_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY allegati_doc_repertorio
    ADD CONSTRAINT all_doc_repertorio_documento_id_fkey FOREIGN KEY (dc_id) REFERENCES documenti(documento_id);


--
-- TOC entry 2962 (class 2606 OID 305725)
-- Name: all_doc_repertorio_repertorio_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY allegati_doc_repertorio
    ADD CONSTRAINT all_doc_repertorio_repertorio_id_fkey FOREIGN KEY (doc_repertorio_id) REFERENCES documenti_repertori(doc_repertorio_id);


--
-- TOC entry 2977 (class 2606 OID 306080)
-- Name: all_doc_sezione_documento_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY allegati_doc_amm_trasparente
    ADD CONSTRAINT all_doc_sezione_documento_id_fkey FOREIGN KEY (dc_id) REFERENCES documenti(documento_id);


--
-- TOC entry 2978 (class 2606 OID 306085)
-- Name: all_doc_sezione_sezione_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY allegati_doc_amm_trasparente
    ADD CONSTRAINT all_doc_sezione_sezione_id_fkey FOREIGN KEY (doc_sezione_id) REFERENCES amm_trasparente_documenti(doc_id);


--
-- TOC entry 2936 (class 2606 OID 305112)
-- Name: amministrazioni_partecipanti_rubrica_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY amministrazioni_partecipanti
    ADD CONSTRAINT amministrazioni_partecipanti_rubrica_id_fkey FOREIGN KEY (rubrica_id) REFERENCES rubrica(rubrica_id);


--
-- TOC entry 2937 (class 2606 OID 305117)
-- Name: amministrazioni_partecipanti_titolario_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY amministrazioni_partecipanti
    ADD CONSTRAINT amministrazioni_partecipanti_titolario_id_fkey FOREIGN KEY (tipo_procedimenti_id) REFERENCES tipi_procedimento(tipo_procedimenti_id);


--
-- TOC entry 2876 (class 2606 OID 304452)
-- Name: ass_proc_prot_proc_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_procedimenti
    ADD CONSTRAINT ass_proc_prot_proc_id_fkey FOREIGN KEY (procedimento_id) REFERENCES procedimenti(procedimento_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2877 (class 2606 OID 304457)
-- Name: ass_proc_prot_prot_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_procedimenti
    ADD CONSTRAINT ass_proc_prot_prot_id_fkey FOREIGN KEY (protocollo_id) REFERENCES protocolli(protocollo_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2928 (class 2606 OID 304961)
-- Name: cariche_profilo_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY cariche
    ADD CONSTRAINT cariche_profilo_id_fkey FOREIGN KEY (profilo_id) REFERENCES profili(profilo_id);


--
-- TOC entry 2927 (class 2606 OID 304956)
-- Name: cariche_ufficio_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY cariche
    ADD CONSTRAINT cariche_ufficio_id_fkey FOREIGN KEY (ufficio_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2929 (class 2606 OID 304966)
-- Name: cariche_utente_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY cariche
    ADD CONSTRAINT cariche_utente_id_fkey FOREIGN KEY (utente_id) REFERENCES utenti(utente_id);


--
-- TOC entry 2827 (class 2606 OID 305178)
-- Name: doc_file_attr_invio_classificati_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY invio_classificati
    ADD CONSTRAINT doc_file_attr_invio_classificati_fkey FOREIGN KEY (procedimento_id) REFERENCES procedimenti(procedimento_id);


--
-- TOC entry 2798 (class 2606 OID 305173)
-- Name: doc_file_attr_procedimento_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY doc_file_attr
    ADD CONSTRAINT doc_file_attr_procedimento_id_fkey FOREIGN KEY (procedimento_id) REFERENCES procedimenti(procedimento_id);


--
-- TOC entry 2960 (class 2606 OID 305937)
-- Name: doc_repertorio_protocollo_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY documenti_repertori
    ADD CONSTRAINT doc_repertorio_protocollo_id_fkey FOREIGN KEY (protocollo_id) REFERENCES protocolli(protocollo_id);


--
-- TOC entry 2957 (class 2606 OID 305710)
-- Name: doc_repertorio_ufficio_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY documenti_repertori
    ADD CONSTRAINT doc_repertorio_ufficio_id_fkey FOREIGN KEY (ufficio_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2973 (class 2606 OID 306052)
-- Name: doc_sezione_protocollo_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY amm_trasparente_documenti
    ADD CONSTRAINT doc_sezione_protocollo_id_fkey FOREIGN KEY (protocollo_id) REFERENCES protocolli(protocollo_id);


--
-- TOC entry 2974 (class 2606 OID 306057)
-- Name: doc_sezione_ufficio_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY amm_trasparente_documenti
    ADD CONSTRAINT doc_sezione_ufficio_id_fkey FOREIGN KEY (ufficio_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2940 (class 2606 OID 305240)
-- Name: documenti_editor_carica_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY documenti_editor
    ADD CONSTRAINT documenti_editor_carica_id_fkey FOREIGN KEY (carica_id) REFERENCES cariche(carica_id);


--
-- TOC entry 2817 (class 2606 OID 304462)
-- Name: documenti_fascicoli_f1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY fascicolo_documenti
    ADD CONSTRAINT documenti_fascicoli_f1 FOREIGN KEY (dfa_id) REFERENCES doc_file_attr(dfa_id);


--
-- TOC entry 2818 (class 2606 OID 304467)
-- Name: documenti_fascicoli_f2; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY fascicolo_documenti
    ADD CONSTRAINT documenti_fascicoli_f2 FOREIGN KEY (fascicolo_id) REFERENCES fascicoli(fascicolo_id);


--
-- TOC entry 2813 (class 2606 OID 304917)
-- Name: fascicoli_id_parent_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY fascicoli
    ADD CONSTRAINT fascicoli_id_parent_fkey FOREIGN KEY (parent_id) REFERENCES fascicoli(fascicolo_id);


--
-- TOC entry 2946 (class 2606 OID 306000)
-- Name: fascicolo_proprietario_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY fascicolo_allacci
    ADD CONSTRAINT fascicolo_proprietario_id FOREIGN KEY (ufficio_proprietario_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2819 (class 2606 OID 306005)
-- Name: fascicolo_proprietario_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY fascicolo_documenti
    ADD CONSTRAINT fascicolo_proprietario_id FOREIGN KEY (ufficio_proprietario_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2822 (class 2606 OID 306010)
-- Name: fascicolo_proprietario_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY fascicolo_protocolli
    ADD CONSTRAINT fascicolo_proprietario_id FOREIGN KEY (ufficio_proprietario_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2784 (class 2606 OID 304472)
-- Name: fk_acquisizione_massiva_aoo_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY acquisizione_massiva_logs
    ADD CONSTRAINT fk_acquisizione_massiva_aoo_id FOREIGN KEY (aoo_id) REFERENCES aree_organizzative(aoo_id);


--
-- TOC entry 2791 (class 2606 OID 304477)
-- Name: fk_aoo_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY doc_cartelle
    ADD CONSTRAINT fk_aoo_id FOREIGN KEY (aoo_id) REFERENCES aree_organizzative(aoo_id);


--
-- TOC entry 2788 (class 2606 OID 304482)
-- Name: fk_ca_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY ca_crl
    ADD CONSTRAINT fk_ca_id FOREIGN KEY (ca_id) REFERENCES ca_lista(ca_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2952 (class 2606 OID 305427)
-- Name: fk_car_lista_distr; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY assegnatari_lista_distribuzione
    ADD CONSTRAINT fk_car_lista_distr FOREIGN KEY (id_lista) REFERENCES lista_distribuzione(id);


--
-- TOC entry 2953 (class 2606 OID 305432)
-- Name: fk_carica; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY assegnatari_lista_distribuzione
    ADD CONSTRAINT fk_carica FOREIGN KEY (id_carica) REFERENCES cariche(carica_id);


--
-- TOC entry 2801 (class 2606 OID 305046)
-- Name: fk_carica_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY doc_file_permessi
    ADD CONSTRAINT fk_carica_id FOREIGN KEY (carica_id) REFERENCES cariche(carica_id);


--
-- TOC entry 2797 (class 2606 OID 305041)
-- Name: fk_carica_owner; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY doc_file_attr
    ADD CONSTRAINT fk_carica_owner FOREIGN KEY (owner_id) REFERENCES cariche(carica_id);


--
-- TOC entry 2794 (class 2606 OID 304487)
-- Name: fk_cartelle; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY doc_file_attr
    ADD CONSTRAINT fk_cartelle FOREIGN KEY (dc_id) REFERENCES doc_cartelle(dc_id);


--
-- TOC entry 2802 (class 2606 OID 304497)
-- Name: fk_coda_invio_protocolli; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY email_coda_invio
    ADD CONSTRAINT fk_coda_invio_protocolli FOREIGN KEY (protocollo_id) REFERENCES protocolli(protocollo_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2799 (class 2606 OID 304502)
-- Name: fk_dfa_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY doc_file_permessi
    ADD CONSTRAINT fk_dfa_id FOREIGN KEY (dfa_id) REFERENCES doc_file_attr(dfa_id);


--
-- TOC entry 2793 (class 2606 OID 305031)
-- Name: fk_doc_cartelle_carica; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY doc_cartelle
    ADD CONSTRAINT fk_doc_cartelle_carica FOREIGN KEY (carica_id) REFERENCES cariche(carica_id);


--
-- TOC entry 2796 (class 2606 OID 305036)
-- Name: fk_doc_file_attr_carica_lav; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY doc_file_attr
    ADD CONSTRAINT fk_doc_file_attr_carica_lav FOREIGN KEY (carica_lav_id) REFERENCES cariche(carica_id);


--
-- TOC entry 2804 (class 2606 OID 304507)
-- Name: fk_email; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY email_ingresso_allegati
    ADD CONSTRAINT fk_email FOREIGN KEY (email_id) REFERENCES email_ingresso(email_id);


--
-- TOC entry 2807 (class 2606 OID 304512)
-- Name: fk_faldone_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY faldone_procedimenti
    ADD CONSTRAINT fk_faldone_id FOREIGN KEY (faldone_id) REFERENCES faldoni(faldone_id);


--
-- TOC entry 2805 (class 2606 OID 304517)
-- Name: fk_faldoni_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY faldone_fascicoli
    ADD CONSTRAINT fk_faldoni_id FOREIGN KEY (faldone_id) REFERENCES faldoni(faldone_id);


--
-- TOC entry 2846 (class 2606 OID 304522)
-- Name: fk_fascicoli; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY procedimenti_fascicolo
    ADD CONSTRAINT fk_fascicoli FOREIGN KEY (fascicolo_id) REFERENCES fascicoli(fascicolo_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2886 (class 2606 OID 304527)
-- Name: fk_fascicoli1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_procedimenti_fascicolo
    ADD CONSTRAINT fk_fascicoli1 FOREIGN KEY (fascicolo_id) REFERENCES fascicoli(fascicolo_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2814 (class 2606 OID 305016)
-- Name: fk_fascicoli_carica_1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY fascicoli
    ADD CONSTRAINT fk_fascicoli_carica_1 FOREIGN KEY (carica_istruttore_id) REFERENCES cariche(carica_id);


--
-- TOC entry 2815 (class 2606 OID 305021)
-- Name: fk_fascicoli_carica_2; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY fascicoli
    ADD CONSTRAINT fk_fascicoli_carica_2 FOREIGN KEY (carica_responsabile_id) REFERENCES cariche(carica_id);


--
-- TOC entry 2816 (class 2606 OID 305026)
-- Name: fk_fascicoli_carica_3; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY fascicoli
    ADD CONSTRAINT fk_fascicoli_carica_3 FOREIGN KEY (carica_intestatario_id) REFERENCES cariche(carica_id);


--
-- TOC entry 2806 (class 2606 OID 304532)
-- Name: fk_fascicoli_faldone_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY faldone_fascicoli
    ADD CONSTRAINT fk_fascicoli_faldone_id FOREIGN KEY (fascicolo_id) REFERENCES fascicoli(fascicolo_id);


--
-- TOC entry 2811 (class 2606 OID 304537)
-- Name: fk_fascicoli_uffici_1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY fascicoli
    ADD CONSTRAINT fk_fascicoli_uffici_1 FOREIGN KEY (ufficio_intestatario_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2812 (class 2606 OID 304542)
-- Name: fk_fascicoli_uffici_2; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY fascicoli
    ADD CONSTRAINT fk_fascicoli_uffici_2 FOREIGN KEY (ufficio_responsabile_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2942 (class 2606 OID 305266)
-- Name: fk_fascicolo_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY invio_classificati_fascicoli
    ADD CONSTRAINT fk_fascicolo_id FOREIGN KEY (fascicolo_id) REFERENCES fascicoli(fascicolo_id);


--
-- TOC entry 2795 (class 2606 OID 304557)
-- Name: fk_file_rep; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY doc_file_attr
    ADD CONSTRAINT fk_file_rep FOREIGN KEY (dfr_id) REFERENCES doc_file_rep(dfr_id);


--
-- TOC entry 2883 (class 2606 OID 304567)
-- Name: fk_lista_distr; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY rubrica_lista_distribuzione
    ADD CONSTRAINT fk_lista_distr FOREIGN KEY (id_lista) REFERENCES lista_distribuzione(id);


--
-- TOC entry 2873 (class 2606 OID 304572)
-- Name: fk_mezzi_sped_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_destinatari
    ADD CONSTRAINT fk_mezzi_sped_id FOREIGN KEY (mezzo_spedizione_id) REFERENCES spedizioni(spedizioni_id);


--
-- TOC entry 2803 (class 2606 OID 304577)
-- Name: fk_msg_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY email_coda_invio_destinatari
    ADD CONSTRAINT fk_msg_id FOREIGN KEY (msg_id) REFERENCES email_coda_invio(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2792 (class 2606 OID 304582)
-- Name: fk_parent_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY doc_cartelle
    ADD CONSTRAINT fk_parent_id FOREIGN KEY (parent_id) REFERENCES doc_cartelle(dc_id);


--
-- TOC entry 2847 (class 2606 OID 304587)
-- Name: fk_procedimenti; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY procedimenti_fascicolo
    ADD CONSTRAINT fk_procedimenti FOREIGN KEY (procedimento_id) REFERENCES procedimenti(procedimento_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2887 (class 2606 OID 304592)
-- Name: fk_procedimenti1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_procedimenti_fascicolo
    ADD CONSTRAINT fk_procedimenti1 FOREIGN KEY (procedimento_id) REFERENCES procedimenti(procedimento_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2843 (class 2606 OID 305193)
-- Name: fk_procedimenti_carica_lav_procedimenti; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY procedimenti
    ADD CONSTRAINT fk_procedimenti_carica_lav_procedimenti FOREIGN KEY (carica_lav_id) REFERENCES cariche(carica_id);


--
-- TOC entry 2808 (class 2606 OID 304597)
-- Name: fk_procedimenti_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY faldone_procedimenti
    ADD CONSTRAINT fk_procedimenti_id FOREIGN KEY (procedimento_id) REFERENCES procedimenti(procedimento_id);


--
-- TOC entry 2844 (class 2606 OID 304602)
-- Name: fk_procedimento_faldone_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY procedimenti_faldone
    ADD CONSTRAINT fk_procedimento_faldone_id FOREIGN KEY (faldone_id) REFERENCES faldoni(faldone_id);


--
-- TOC entry 2845 (class 2606 OID 304607)
-- Name: fk_procedimento_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY procedimenti_faldone
    ADD CONSTRAINT fk_procedimento_id FOREIGN KEY (procedimento_id) REFERENCES procedimenti(procedimento_id);


--
-- TOC entry 2838 (class 2606 OID 304612)
-- Name: fk_protocolli_procedimenti; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY procedimenti
    ADD CONSTRAINT fk_protocolli_procedimenti FOREIGN KEY (protocollo_id) REFERENCES protocolli(protocollo_id);


--
-- TOC entry 2884 (class 2606 OID 304627)
-- Name: fk_rubrica; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY rubrica_lista_distribuzione
    ADD CONSTRAINT fk_rubrica FOREIGN KEY (id_rubrica) REFERENCES rubrica(rubrica_id);


--
-- TOC entry 2874 (class 2606 OID 304642)
-- Name: fk_tit_dest_prot; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_destinatari
    ADD CONSTRAINT fk_tit_dest_prot FOREIGN KEY (titolo_id) REFERENCES titoli_destinatari(id);


--
-- TOC entry 2906 (class 2606 OID 304647)
-- Name: fk_tit_dest_prot_storia; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocollo_destinatari
    ADD CONSTRAINT fk_tit_dest_prot_storia FOREIGN KEY (titolo_id) REFERENCES titoli_destinatari(id);


--
-- TOC entry 2828 (class 2606 OID 304652)
-- Name: fk_titolo; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY invio_classificati_destinatari
    ADD CONSTRAINT fk_titolo FOREIGN KEY (titolo_id) REFERENCES titoli_destinatari(id);


--
-- TOC entry 2834 (class 2606 OID 304657)
-- Name: fk_titolo; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY invio_fascicoli_destinatari
    ADD CONSTRAINT fk_titolo FOREIGN KEY (titolo_id) REFERENCES titoli_destinatari(id);


--
-- TOC entry 2839 (class 2606 OID 304662)
-- Name: fk_uffici_procedimenti; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY procedimenti
    ADD CONSTRAINT fk_uffici_procedimenti FOREIGN KEY (ufficio_id) REFERENCES uffici(ufficio_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2954 (class 2606 OID 305437)
-- Name: fk_ufficio; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY assegnatari_lista_distribuzione
    ADD CONSTRAINT fk_ufficio FOREIGN KEY (id_ufficio) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2800 (class 2606 OID 304672)
-- Name: fk_ufficio_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY doc_file_permessi
    ADD CONSTRAINT fk_ufficio_id FOREIGN KEY (ufficio_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2823 (class 2606 OID 304717)
-- Name: ic_fk_aoo_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY invio_classificati
    ADD CONSTRAINT ic_fk_aoo_id FOREIGN KEY (aoo_id) REFERENCES aree_organizzative(aoo_id);


--
-- TOC entry 2824 (class 2606 OID 304722)
-- Name: ic_fk_dfa_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY invio_classificati
    ADD CONSTRAINT ic_fk_dfa_id FOREIGN KEY (dfa_id) REFERENCES doc_file_attr(dfa_id);


--
-- TOC entry 2829 (class 2606 OID 304727)
-- Name: ic_fk_dfa_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY invio_classificati_destinatari
    ADD CONSTRAINT ic_fk_dfa_id FOREIGN KEY (dfa_id) REFERENCES doc_file_attr(dfa_id);


--
-- TOC entry 2943 (class 2606 OID 305271)
-- Name: ic_fk_dfa_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY invio_classificati_fascicoli
    ADD CONSTRAINT ic_fk_dfa_id FOREIGN KEY (dfa_id) REFERENCES doc_file_attr(dfa_id);


--
-- TOC entry 2825 (class 2606 OID 304732)
-- Name: ic_fk_ufficio_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY invio_classificati
    ADD CONSTRAINT ic_fk_ufficio_id FOREIGN KEY (ufficio_mittente_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2826 (class 2606 OID 304737)
-- Name: ic_fk_utente_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY invio_classificati
    ADD CONSTRAINT ic_fk_utente_id FOREIGN KEY (utente_mittente_id) REFERENCES utenti(utente_id);


--
-- TOC entry 2830 (class 2606 OID 304742)
-- Name: if_fascicolo_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY invio_fascicoli
    ADD CONSTRAINT if_fascicolo_id FOREIGN KEY (fascicolo_id) REFERENCES fascicoli(fascicolo_id);


--
-- TOC entry 2831 (class 2606 OID 304747)
-- Name: if_fk_aoo_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY invio_fascicoli
    ADD CONSTRAINT if_fk_aoo_id FOREIGN KEY (aoo_id) REFERENCES aree_organizzative(aoo_id);


--
-- TOC entry 2832 (class 2606 OID 304752)
-- Name: if_fk_ufficio_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY invio_fascicoli
    ADD CONSTRAINT if_fk_ufficio_id FOREIGN KEY (ufficio_mittente_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2833 (class 2606 OID 304757)
-- Name: if_fk_utente_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY invio_fascicoli
    ADD CONSTRAINT if_fk_utente_id FOREIGN KEY (utente_mittente_id) REFERENCES utenti(utente_id);


--
-- TOC entry 2835 (class 2606 OID 304762)
-- Name: ifd_fascicolo_id; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY invio_fascicoli_destinatari
    ADD CONSTRAINT ifd_fascicolo_id FOREIGN KEY (fascicolo_id) REFERENCES fascicoli(fascicolo_id);


--
-- TOC entry 2789 (class 2606 OID 304767)
-- Name: lista_crl_ca_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY ca_revoked_list
    ADD CONSTRAINT lista_crl_ca_id_fkey FOREIGN KEY (ca_id) REFERENCES ca_lista(ca_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2836 (class 2606 OID 304772)
-- Name: lista_distribuzione_fk1; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY lista_distribuzione
    ADD CONSTRAINT lista_distribuzione_fk1 FOREIGN KEY (aoo_id) REFERENCES aree_organizzative(aoo_id);


--
-- TOC entry 2965 (class 2606 OID 305817)
-- Name: mail_config_aoo_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY mail_config
    ADD CONSTRAINT mail_config_aoo_id_fkey FOREIGN KEY (aoo_id) REFERENCES aree_organizzative(aoo_id);


--
-- TOC entry 2966 (class 2606 OID 305850)
-- Name: mnotifiche_fattura_elettronica_aoo_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY mail_config
    ADD CONSTRAINT mnotifiche_fattura_elettronica_aoo_id_fkey FOREIGN KEY (aoo_id) REFERENCES aree_organizzative(aoo_id);


--
-- TOC entry 2837 (class 2606 OID 305152)
-- Name: oggetti_aoo_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY oggetti
    ADD CONSTRAINT oggetti_aoo_id_fkey FOREIGN KEY (aoo_id) REFERENCES aree_organizzative(aoo_id);


--
-- TOC entry 2842 (class 2606 OID 305147)
-- Name: procedimento_fascicolo_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY procedimenti
    ADD CONSTRAINT procedimento_fascicolo_id_fkey FOREIGN KEY (fascicolo_id) REFERENCES fascicoli(fascicolo_id);


--
-- TOC entry 2938 (class 2606 OID 305127)
-- Name: procedimento_istruttori_carica_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY procedimento_istruttori
    ADD CONSTRAINT procedimento_istruttori_carica_id_fkey FOREIGN KEY (carica_id) REFERENCES cariche(carica_id);


--
-- TOC entry 2955 (class 2606 OID 305491)
-- Name: procedimento_istruttori_carica_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_procedimento_istruttori
    ADD CONSTRAINT procedimento_istruttori_carica_id_fkey FOREIGN KEY (carica_id) REFERENCES cariche(carica_id);


--
-- TOC entry 2939 (class 2606 OID 305132)
-- Name: procedimento_istruttori_procediemento_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY procedimento_istruttori
    ADD CONSTRAINT procedimento_istruttori_procediemento_id_fkey FOREIGN KEY (procedimento_id) REFERENCES procedimenti(procedimento_id);


--
-- TOC entry 2956 (class 2606 OID 305496)
-- Name: procedimento_istruttori_procediemento_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_procedimento_istruttori
    ADD CONSTRAINT procedimento_istruttori_procediemento_id_fkey FOREIGN KEY (procedimento_id) REFERENCES procedimenti(procedimento_id);


--
-- TOC entry 2840 (class 2606 OID 305137)
-- Name: procedimento_responsabile_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY procedimenti
    ADD CONSTRAINT procedimento_responsabile_id_fkey FOREIGN KEY (responsabile_id) REFERENCES cariche(carica_id);


--
-- TOC entry 2841 (class 2606 OID 305142)
-- Name: procedimento_tipo_procedimento_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY procedimenti
    ADD CONSTRAINT procedimento_tipo_procedimento_id_fkey FOREIGN KEY (tipo_procedimento_id) REFERENCES tipi_procedimento(tipo_procedimenti_id);


--
-- TOC entry 2862 (class 2606 OID 304777)
-- Name: protocollo_allegati_documento_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_allegati
    ADD CONSTRAINT protocollo_allegati_documento_id_fkey FOREIGN KEY (documento_id) REFERENCES documenti(documento_id);


--
-- TOC entry 2863 (class 2606 OID 304782)
-- Name: protocollo_allegati_protocollo_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY protocollo_allegati
    ADD CONSTRAINT protocollo_allegati_protocollo_id_fkey FOREIGN KEY (protocollo_id) REFERENCES protocolli(protocollo_id);


--
-- TOC entry 2959 (class 2606 OID 305878)
-- Name: protocollo_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY documenti_repertori
    ADD CONSTRAINT protocollo_id_fkey FOREIGN KEY (protocollo_id) REFERENCES protocolli(protocollo_id);


--
-- TOC entry 2975 (class 2606 OID 306062)
-- Name: protocollo_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY amm_trasparente_documenti
    ADD CONSTRAINT protocollo_id_fkey FOREIGN KEY (protocollo_id) REFERENCES protocolli(protocollo_id);


--
-- TOC entry 2963 (class 2606 OID 305738)
-- Name: repertorio_aoo_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY repertori
    ADD CONSTRAINT repertorio_aoo_id_fkey FOREIGN KEY (aoo_id) REFERENCES aree_organizzative(aoo_id);


--
-- TOC entry 2958 (class 2606 OID 305743)
-- Name: repertorio_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY documenti_repertori
    ADD CONSTRAINT repertorio_id_fkey FOREIGN KEY (rep_id) REFERENCES repertori(repertorio_id);


--
-- TOC entry 2964 (class 2606 OID 305766)
-- Name: repertorio_uffico_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY repertori
    ADD CONSTRAINT repertorio_uffico_id_fkey FOREIGN KEY (ufficio_responsabile_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2934 (class 2606 OID 305092)
-- Name: riferimenti_legislativi_documento_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY riferimenti_legislativi
    ADD CONSTRAINT riferimenti_legislativi_documento_id_fkey FOREIGN KEY (documento_id) REFERENCES documenti(documento_id);


--
-- TOC entry 2933 (class 2606 OID 305102)
-- Name: riferimenti_legislativi_procedimento_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY riferimenti_legislativi
    ADD CONSTRAINT riferimenti_legislativi_procedimento_id_fkey FOREIGN KEY (procedimento_id) REFERENCES procedimenti(procedimento_id);


--
-- TOC entry 2935 (class 2606 OID 305097)
-- Name: riferimenti_legislativi_tipo_procedimento_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY riferimenti_legislativi
    ADD CONSTRAINT riferimenti_legislativi_tipo_procedimento_id_fkey FOREIGN KEY (tipo_procedimenti_id) REFERENCES tipi_procedimento(tipo_procedimenti_id);


--
-- TOC entry 2971 (class 2606 OID 306031)
-- Name: sezione_aoo_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY amm_trasparente
    ADD CONSTRAINT sezione_aoo_id_fkey FOREIGN KEY (aoo_id) REFERENCES aree_organizzative(aoo_id);


--
-- TOC entry 2976 (class 2606 OID 306067)
-- Name: sezione_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY amm_trasparente_documenti
    ADD CONSTRAINT sezione_id_fkey FOREIGN KEY (sez_id) REFERENCES amm_trasparente(sezione_id);


--
-- TOC entry 2972 (class 2606 OID 306036)
-- Name: sezione_uffico_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY amm_trasparente
    ADD CONSTRAINT sezione_uffico_id_fkey FOREIGN KEY (ufficio_responsabile_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2941 (class 2606 OID 305254)
-- Name: storia_documenti_editor_carica_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_documenti_editor
    ADD CONSTRAINT storia_documenti_editor_carica_id_fkey FOREIGN KEY (carica_id) REFERENCES cariche(carica_id);


--
-- TOC entry 2897 (class 2606 OID 304792)
-- Name: storia_protocollo_allegati_documento_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocollo_allegati
    ADD CONSTRAINT storia_protocollo_allegati_documento_id_fkey FOREIGN KEY (documento_id) REFERENCES documenti(documento_id);


--
-- TOC entry 2898 (class 2606 OID 304797)
-- Name: storia_protocollo_allegati_protocollo_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY storia_protocollo_allegati
    ADD CONSTRAINT storia_protocollo_allegati_protocollo_id_fkey FOREIGN KEY (protocollo_id, versione) REFERENCES storia_protocolli(protocollo_id, versione);


--
-- TOC entry 2912 (class 2606 OID 305082)
-- Name: tipi_procedimento_titolario_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY tipi_procedimento
    ADD CONSTRAINT tipi_procedimento_titolario_id_fkey FOREIGN KEY (titolario_id) REFERENCES titolario(titolario_id);


--
-- TOC entry 2916 (class 2606 OID 304912)
-- Name: titolario_responsabile_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY titolario
    ADD CONSTRAINT titolario_responsabile_id_fkey FOREIGN KEY (responsabile_id) REFERENCES utenti(utente_id);


--
-- TOC entry 2917 (class 2606 OID 304928)
-- Name: titolario_ufficio_responsabile_id_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY titolario
    ADD CONSTRAINT titolario_ufficio_responsabile_id_fkey FOREIGN KEY (ufficio_responsabile_id) REFERENCES uffici(ufficio_id);


--
-- TOC entry 2787 (class 2606 OID 305183)
-- Name: utenti_aree_organizzative_fkey; Type: FK CONSTRAINT; Schema: fenice; Owner: fenice
--

ALTER TABLE ONLY aree_organizzative
    ADD CONSTRAINT utenti_aree_organizzative_fkey FOREIGN KEY (utente_responsabile_id) REFERENCES utenti(utente_id);


--
-- TOC entry 3192 (class 0 OID 0)
-- Dependencies: 8
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2018-07-13 02:39:55

--
-- PostgreSQL database dump complete
--

