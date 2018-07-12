package it.finsiel.siged.constant;

/*
 * @author Almaviva sud.
 */

public class EmailConstants {

    public static final int EMAIL_USCITA_NON_INVIATA = 0;

    public static final int EMAIL_USCITA_INVIATA = 1;

    public static final int EMAIL_INGRESSO_NON_PROTOCOLLATA = 0;

    public static final int EMAIL_INGRESSO_PROTOCOLLATA = 1;
    
    public static final int EMAIL_INGRESSO_ELIMINATA = 2;

    public static final String DESTINATARIO_TIPO_EMAIL = "Email";

    public static final String DATICERT_XML = "daticert.xml";

    public static final String POSTACERT_EML = "postacert.eml";

    public static final String SMIME_P7S = "smime.p7s";

    public static final String TIPO_ACCETTAZIONE = "accettazione";

    public static final String TIPO_ANOMALIA = "anomalia";

    public static final String TIPO_POSTA_CERTIFICATA = "posta-certificata";

    public static final String TIPO_CONSEGNA = "avvenuta-consegna";
    
    public static final String TIPO_MANCATA_CONSEGNA = "errore-consegna";

    public static final String TIPO_ALTRO = "altro";

    public static final int ERROR_EMAIL_INGRESSO = 1;

    public static final int SUCCESS_EMAIL_INGRESSO  = 2;
    
    public static final int ERROR_EMAIL_USCITA = 3;

    public static final int SUCCESS_EMAIL_USCITA  = 4;

    public static final int LOG_CRL = 12;

}