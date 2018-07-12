package it.finsiel.siged.constant;

import java.awt.Color;
import java.awt.Point;


public class FileConstants {

    public static final String ICC_PROFILE_PATH = "/icc/AdobeRGB1998.icc";

    public static final String MIME_TYPE_PDF = "application/pdf";

    public static final String MIME_TYPE_XP7M = "application/x-pkcs7-mime";

    public static final String MIME_TYPE_P7M = "application/pkcs7-mime";

    public static final int BUFFER_SIZE = 8192;

    public static final int BARCODE_DPI = 180;

    public static final double BARCODE_HEIGHT = 15;// mm
    

    public static final Point PROTOCOLLO_HEADER_LEFT_TOP = new Point(30, 70);

    public static final String PROTOCOLLO_HEADER_FONTNAME = "Courier";

    public static final float PROTOCOLLO_HEADER_FONTSIZE = 10;

    public static final Color PROTOCOLLO_HEADER_FONTCOLOR = Color.BLACK;
    

    public static final String SHA = "SHA-1";
    
    public static final String SHA256= "SHA-256";

    public static final String STAMPA_ETICHETTE_DESTINATARI_REPORT_TEMPLATE = "/WEB-INF/reports/StampaEtichetteDestinatari.jrxml";

    public static final String STAMPA_REGISTRO_MODIFICHE_REPORT_TEMPLATE = "/WEB-INF/reports/StampaRegistroModificheDoc.jrxml";

    public static final String STAMPA_REGISTRO_REPORT_TEMPLATE = "/WEB-INF/reports/StampaRegistro.jrxml";
    
    public static final String STAMPA_RICERCA_REPORT_TEMPLATE = "/WEB-INF/reports/StampaRicerca.jrxml";
    
    public static final String STAMPA_RICERCA_FASCICOLI_REPORT_TEMPLATE = "/WEB-INF/reports/StampaRicercaFascicoli.jrxml";
    
    public static final String STAMPA_ELENCO_DEPOSITO_TEMPLATE = "/WEB-INF/reports/StampaElencoVersamentoDeposito.jrxml";

    public static final String STAMPA_VELINE_REPORT_TEMPLATE = "/WEB-INF/reports/StampaVelina.jrxml";

    public static final String STAMPA_BBCCAA_TEMPLATE_TEMPLATE = "/WEB-INF/reports/StampaTemplateBBCCAA.jrxml";
    
    public static final String STAMPA_BBCCAA_TEMPLATE_IMAGE = "/WEB-INF/reports/regione.gif";

    public static final String STAMPA_FRONTESPIZIO_TEMPLATE = "/WEB-INF/reports/StampaFrontespizioFascicolo.jrxml";
    
    public static final String STAMPA_ETICHETTA_ERSU_TEMPLATE = "/WEB-INF/reports/StampaEtichettaErsu.jrxml";
    
    public static final String STAMPA_PROCEDIMENTO_TEMPLATE = "/WEB-INF/reports/StampaFrontespizioProcedimento.jrxml";
    
    public static final String STAMPA_COMUNICAZIONE_AVVIO = "/WEB-INF/reports/StampaComunicazioneAvvio.jrxml";

    public static final String STAMPA_DOC_REPERTORIO_TEMPLATE = "/WEB-INF/reports/StampaDocumentoRepertorio.jrxml";
    
    public static final String STAMPA_REPERTORIO_TEMPLATE = "/WEB-INF/reports/StampaRepertorio.jrxml";
 
    public static final String STAMPA_RIFIUTATO_REPORT_TEMPLATE = "/WEB-INF/reports/StampaRifiutato.jrxml";

    public static final String STAMPA_PROTOCOLLI_SCARICATI_REPORT_TEMPLATE = "/WEB-INF/reports/StampaProtocolliScaricati.jrxml";

    public static final String STAMPA_PROTOCOLLI_LAVORAZIONE_REPORT_TEMPLATE = "/WEB-INF/reports/StampaProtocolliLavorazione.jrxml";

    public static final String STAMPA_PROTOCOLLI_ASSEGNATI_REPORT_TEMPLATE = "/WEB-INF/reports/StampaProtocolliAssegnati.jrxml";

    public static final String STAMPA_PROTOCOLLI_ANNULLATI_REPORT_TEMPLATE = "/WEB-INF/reports/StampaProtocolliAnnullati.jrxml";

    public static final String STAMPA_PROTOCOLLI_DA_SCARTARE_REPORT_TEMPLATE = "/WEB-INF/reports/StampaProtocolliDaScartare.jrxml";

    public static final String STAMPA_PROTOCOLLI_SPEDITI_REPORT_TEMPLATE = "/WEB-INF/reports/StampaProtocolliSpediti.jrxml";
    
    public static final String STAMPA_NOTIFICHE_POSTA_INTERNA_TEMPLATE = "/WEB-INF/reports/StampaNotifichePostaInterna.jrxml";

    public static final String STAMPA_PERSONE_FISICHE_REPORT_TEMPLATE = "/WEB-INF/reports/StampaPersoneFisiche.jrxml";

    public static final String STAMPA_PERSONE_GIURIDICHE_REPORT_TEMPLATE = "/WEB-INF/reports/StampaPersoneGiuridiche.jrxml";

    public static final String STAMPA_STATISTICHE_PROTOCOLLI_REPORT_TEMPLATE = "/WEB-INF/reports/StampaStatisticheProtocolli.jrxml";

    public static final String STAMPA_ORGANIZZAZIONE_REPORT_TEMPLATE = "/WEB-INF/reports/StampaOrganizzazione.jrxml";
    
    public static final String STAMPA_TITOLARIO_REPORT_TEMPLATE = "/WEB-INF/reports/StampaTitolario.jrxml";

    public static final String STAMPA_ETICHETTA_PROTOCOLLO_REPORT_TEMPLATE = "/WEB-INF/reports/StampaEtichettaProtocollo.jrxml";
    
    public static final String STAMPA_PROCEDIMENTI_AVVOCATO_GENERALE_TEMPLATE = "/WEB-INF/reports/StampaElencoDecretiULL.jrxml";
    
    public static final String STAMPA_CONSERVAZIONE_REGISTRO_GIORNALIERO_TEMPLATE = "/WEB-INF/reports/StampaConservazioneRegistroGiornaliero.jrxml";

    public static final String STAMPA_CONSERVAZIONE_REGISTRO_MODIFICHE_GIORNALIERO_TEMPLATE = "/WEB-INF/reports/StampaConservazioneRegistroModificheGiornaliero.jrxml";
    
    public static final String STAMPA_CONSERVAZIONE_REGISTRO_ANNUO_TEMPLATE = "/WEB-INF/reports/StampaConservazioneRegistroAnnuo.jrxml";


    public static final String STAMPA_AMM_TRASPARENTE_TEMPLATE = "/WEB-INF/reports/StampaAmmTrasparenteSezione.jrxml";
    
    public static final String STAMPA_DOC_AMM_TRASPARENTE_TEMPLATE = "/WEB-INF/reports/StampaDocumentoAmmTrasparente.jrxml";
}