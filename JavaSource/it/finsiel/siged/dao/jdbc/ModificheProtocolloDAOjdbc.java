package it.finsiel.siged.dao.jdbc;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.integration.ModificheProtocolloDAO;
import it.finsiel.siged.mvc.vo.protocollo.ModificaProtocolloVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.finsiel.siged.util.StringUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class ModificheProtocolloDAOjdbc implements ModificheProtocolloDAO {
    private static Logger logger = Logger
            .getLogger(ModificheProtocolloDAOjdbc.class.getName());

    private static final String[] elencoCampi;

    private static final ResourceBundle bundleDescrCampi;

    private static StringBuffer MODIFICHE_QRY;
    
  //  private static StringBuffer CONTA_MODIFICHE_QRY;

    private JDBCManager jdbcMan = new JDBCManager();

    private static void appendCampo(StringBuffer out, String campo) {
        out.append(", CASE WHEN o.").append(campo).append("=n.").append(campo);
        out.append(" OR (o.").append(campo).append(" IS NULL AND n.");
        out.append(campo).append(" IS NULL) THEN NULL ELSE o.");
        out.append(campo).append(" END AS ").append(campo);
    }

    static {
        elencoCampi = new String[] { "text_oggetto", "data_documento",
                "data_ricezione", "data_protocollo_mittente",
                "nume_protocollo_mittente", "desc_denominazione_mittente",
                "desc_cognome_mittente", "desc_nome_mittente", "indi_mittente",
                "indi_cap_mittente", "indi_localita_mittente",
                "indi_provincia_mittente", "indi_nazione_mittente",
                "data_annullamento", "data_scarico", "text_nota_annullamento",
                "text_provvedimento_annullament", "annotazione_chiave",
                "annotazione_posizione", "annotazione_descrizione",
                "num_prot_emergenza", "flag_riservato", "codi_documento_doc" };

        bundleDescrCampi = ResourceBundle
                .getBundle(ModificheProtocolloDAOjdbc.class.getName());

        /*
         * stato_protocollo, flag_scarto
         */
        // campi
        StringBuffer campi = new StringBuffer();
        campi.append("n.protocollo_id, n.versione, n.data_registrazione, ");
        campi.append("n.row_created_time, n.row_created_user, ");
        campi.append("n.nume_protocollo, n.anno_registrazione, ");
        campi.append("n.registro_id, n.ufficio_protocollatore_id ");
        for (int i = 0; i < elencoCampi.length; i++) {
            appendCampo(campi, elencoCampi[i]);
        }
        appendCampo(campi, "titolario_id");
        appendCampo(campi, "tipo_documento_id");
        appendCampo(campi, "documento_id");
        appendCampo(campi, "ufficio_mittente_id");
        appendCampo(campi, "utente_mittente_id");
        appendCampo(campi, "ufficio_assegnatario_id");
        appendCampo(campi, "utente_assegnatario_id");

        // viste protocollo con assegnatari competenti
        String storiaView = "(SELECT s.*,"
                + " a.ufficio_assegnatario_id, a.utente_assegnatario_id "
                + "FROM storia_protocolli s LEFT JOIN "
                + " storia_protocollo_assegnatari a ON "
                + "a.protocollo_id=s.protocollo_id AND "
                + "a.versione=s.versione AND a.flag_competente=1)";
        String protocolloView = "(SELECT s.*,"
                + " a.ufficio_assegnatario_id, a.utente_assegnatario_id "
                + "FROM protocolli s LEFT JOIN "
                + " protocollo_assegnatari a ON "
                + "a.protocollo_id=s.protocollo_id AND "
                + "a.versione=s.versione AND a.flag_competente=1)";

        // join principale
        String join = " INNER JOIN " + storiaView + " o ON "
                + "n.protocollo_id=o.protocollo_id AND n.versione=o.versione+1";

       // private String getModificaQuery(StringBuffer qry){
        // query
       StringBuffer qry = new StringBuffer();
        //qry.append("SELECT p.*, ");
        qry.append("CASE WHEN p.titolario_id IS NULL THEN NULL ELSE ");
        qry.append("(SELECT path_titolario FROM titolario t WHERE");
        qry.append(" t.titolario_id=p.titolario_id) END ");
        qry.append("AS titolario, ");
        qry.append("CASE WHEN p.tipo_documento_id IS NULL THEN NULL ELSE ");
        qry.append("(SELECT desc_tipo_documento FROM tipi_documento t WHERE");
        qry.append(" t.tipo_documento_id=p.tipo_documento_id) END ");
        qry.append("AS tipo_documento, ");
        qry.append("CASE WHEN p.documento_id IS NULL THEN NULL ELSE ");
        qry.append("(SELECT filename FROM documenti d WHERE");
        qry.append(" d.documento_id=p.documento_id) END ");
        qry.append("AS documento, ");
        qry.append("CASE WHEN p.ufficio_mittente_id IS NULL THEN NULL ELSE ");
        qry.append("(SELECT descrizione FROM uffici u WHERE");
        qry.append(" u.ufficio_id=p.ufficio_mittente_id) END ");
        qry.append("AS ufficio_mittente, ");
        qry.append("CASE WHEN p.utente_mittente_id IS NULL THEN NULL ELSE ");
        qry.append("(SELECT cognome || ' ' || nome FROM utenti u WHERE");
        qry.append(" u.utente_id=p.utente_mittente_id) END ");
        qry.append("AS utente_mittente, ");
        qry
                .append("CASE WHEN p.ufficio_assegnatario_id IS NULL THEN NULL ELSE ");
        qry.append("(SELECT descrizione FROM uffici u WHERE");
        qry.append(" u.ufficio_id=p.ufficio_assegnatario_id) END ");
        qry.append("AS ufficio_assegnatario, ");
        qry
                .append("CASE WHEN p.utente_assegnatario_id IS NULL THEN NULL ELSE ");
        qry.append("(SELECT cognome || ' ' || nome FROM utenti u WHERE");
        qry.append(" u.utente_id=p.utente_assegnatario_id) END ");
        qry.append("AS utente_assegnatario ");
        qry.append("FROM (");
        qry.append("SELECT ").append(campi);
        qry.append(" FROM ").append(storiaView).append(" n ").append(join);
        qry.append(" UNION SELECT ").append(campi);
        qry.append(" FROM ").append(protocolloView).append(" n ").append(join);
        qry.append(") p ");
        qry.append(" WHERE p.registro_id=? AND p.ufficio_protocollatore_id=?");
        qry.append(" AND p.data_registrazione>=? AND p.data_registrazione<=?");
        qry.append(" ORDER BY p.protocollo_id, p.versione, p.row_created_time");
        MODIFICHE_QRY = new StringBuffer();
      //  CONTA_MODIFICHE_QRY = new StringBuffer();
        MODIFICHE_QRY.append("SELECT p.*, ");
       // CONTA_MODIFICHE_QRY.append("SELECT count(p.*), ");
        MODIFICHE_QRY.append(qry);
       // CONTA_MODIFICHE_QRY.append(qry);
       // logger.debug("Query per Registro Modifiche: " + MODIFICHE_QRY);
    }

    private void registraModifica(List<ModificaProtocolloVO> elencoModifiche, String campo,
            ResultSet rs) throws SQLException {
        if (rs.getString(campo) != null) {
            ModificaProtocolloVO elemento = new ModificaProtocolloVO();
            // elemento.setNumeroProtocollo(rs.getString("nume_protocollo") +
            // '/'
            // + rs.getString("anno_registrazione"));
            elemento
                    .setNumeroProtocollo((rs.getString("anno_registrazione") + StringUtil
                            .formattaNumeroProtocolli(rs
                                    .getString("nume_protocollo"))));
            elemento.setDataModifica(rs.getTimestamp("row_created_time"));
            elemento.setUtente(rs.getString("row_created_user"));
            String descr = bundleDescrCampi.getString(campo);
            if (descr == null) {
                descr = campo;
            }
            elemento.setCampo(descr);
            elemento.setValorePrecedente(rs.getString(campo));
            elencoModifiche.add(elemento);
        }
    }

    public List<ModificaProtocolloVO> getModifiche(int registroId, int ufficioId, long dataInizio,
            long dataFine) throws DataException {
        List<ModificaProtocolloVO> elenco = new ArrayList<ModificaProtocolloVO>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(MODIFICHE_QRY.toString());
            // logger.info(MODIFICHE_QRY);
            pstmt.setInt(1, registroId);
            pstmt.setInt(2, ufficioId);
            pstmt.setDate(3, new Date(dataInizio));
            pstmt.setTimestamp(4, new java.sql.Timestamp(dataFine
                    + Constants.GIORNO_MILLISECONDS - 1));

            rs = pstmt.executeQuery();

            while (rs.next()) {
                for (int i = 0; i < elencoCampi.length; i++) {
                    registraModifica(elenco, elencoCampi[i], rs);
                }
                registraModifica(elenco, "titolario", rs);
                registraModifica(elenco, "tipo_documento", rs);
                registraModifica(elenco, "documento", rs);
                registraModifica(elenco, "ufficio_mittente", rs);
                registraModifica(elenco, "utente_mittente", rs);
                registraModifica(elenco, "ufficio_assegnatario", rs);
                registraModifica(elenco, "utente_assegnatario", rs);
            }

        } catch (Exception e) {
            logger.error("Errore nel caricamento delle modifiche", e);
            throw new DataException(e.getMessage());
        } finally {
        	jdbcMan.closeAll(rs, pstmt, connection);
        }
        return elenco;
    }

    public int contaModifiche(int registroId, int ufficioId, long dataInizio,
            long dataFine) throws DataException {
    	int conta=0;
        //List elenco = new ArrayList();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(MODIFICHE_QRY.toString());
            // logger.info(MODIFICHE_QRY);
            pstmt.setInt(1, registroId);
            pstmt.setInt(2, ufficioId);
            pstmt.setDate(3, new Date(dataInizio));
            pstmt.setTimestamp(4, new java.sql.Timestamp(dataFine
                    + Constants.GIORNO_MILLISECONDS - 1));

            rs = pstmt.executeQuery();

          //  if (rs.next()) {
            	//rs.getRow()
            	conta=rs.getRow();
           // }
                
        } catch (Exception e) {
            logger.error("Errore nel caricamento delle modifiche", e);
            throw new DataException(e.getMessage());
        } finally {
        	jdbcMan.closeAll(rs, pstmt, connection);
        }
        return conta;
    }
    
}
