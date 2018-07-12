package it.finsiel.siged.dao.jdbc;

import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.integration.ConfigurazioneUtenteDAO;
import it.finsiel.siged.mvc.vo.protocollo.ConfigurazioneUtenteVO;
import it.finsiel.siged.rdb.JDBCManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class ConfigurazioneUtenteDAOjdbc implements ConfigurazioneUtenteDAO {
    private static final String SELECT_CONFIGURAZIONE_PER_UTENTE = "SELECT * FROM RIPETIDATI WHERE utente_id=?";

    private static final String INSERT_CONFIGURAZIONE = "INSERT INTO RIPETIDATI (tipodocumento, "
            + "data_documento, ricevuto_il, tipo_mittente, mittente, assegnatario_utente_id, "
            + "assegnatario_ufficio_id, "
            + "destinatario, titolario, utente_id, oggetto,"
            + "check_oggetto,check_tipodocumento,check_data_documento,check_ricevuto_il,"
            + "check_tipo_mittente,check_mittente, check_destinatario, check_assegnatario, check_titolario,parametri_stampante) "
            + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public final static String UPDATE_CONFIGURAZIONE = "UPDATE RIPETIDATI "
            + " SET tipodocumento=?, data_documento=?, ricevuto_il=?, tipo_mittente=?, mittente=?,"
            + " assegnatario_ufficio_id=?, assegnatario_utente_id=?, destinatario=?, titolario=?, oggetto=?, "
            + " check_oggetto=?,check_tipodocumento=?,check_data_documento=?,check_ricevuto_il=?,"
            + " check_tipo_mittente=?,check_mittente=?, check_destinatario=?, check_assegnatario =?, check_titolario =? WHERE utente_id=?";

    public final static String UPDATE_CONFIGURAZIONE_STAMPANTE = "UPDATE RIPETIDATI "
            + " SET parametri_stampante=? WHERE utente_id=?";

    static Logger logger = Logger.getLogger(FaldoneDAOjdbc.class.getName());

    private JDBCManager jdbcMan = new JDBCManager();

    public ConfigurazioneUtenteVO getConfigurazione(int utenteId)
            throws DataException {
        ConfigurazioneUtenteVO configurazioneVO = new ConfigurazioneUtenteVO();
        Connection connection = null;
        try {
            connection = jdbcMan.getConnection();
            configurazioneVO = getConfigurazione(connection, utenteId);
        } catch (Exception e) {
            throw new DataException(e.getMessage() + " UtenteId :" + utenteId);
        } finally {
            jdbcMan.close(connection);
        }
        return configurazioneVO;
    }

    public ConfigurazioneUtenteVO getConfigurazione(Connection connection,
            int utenteId) throws DataException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ConfigurazioneUtenteVO configurazioneVO = new ConfigurazioneUtenteVO();
        configurazioneVO.setReturnValue(ReturnValues.UNKNOWN);
        try {
            pstmt = connection
                    .prepareStatement(SELECT_CONFIGURAZIONE_PER_UTENTE);
            pstmt.setInt(1, utenteId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                configurazioneVO.setUtenteId(rs.getInt("utente_id"));
                configurazioneVO.setCheckAssegnatari(rs
                        .getString("check_assegnatario"));
                configurazioneVO.setCheckDataDocumento(rs
                        .getString("check_data_documento"));
                configurazioneVO.setCheckDestinatari(rs
                        .getString("check_destinatario"));
                configurazioneVO.setCheckMittente(rs
                        .getString("check_mittente"));
                configurazioneVO.setCheckOggetto(rs.getString("check_oggetto"));
                configurazioneVO.setCheckRicevutoIl(rs
                        .getString("check_ricevuto_il"));
                configurazioneVO.setCheckTipoDocumento(rs
                        .getString("check_tipodocumento"));
                configurazioneVO.setCheckTipoMittente(rs
                        .getString("check_tipo_mittente"));
                configurazioneVO.setCheckTitolario(rs
                        .getString("check_titolario"));
                configurazioneVO.setTipoDocumentoId(rs.getInt("tipodocumento"));
                configurazioneVO.setDataDocumento(rs
                        .getString("data_documento"));
                configurazioneVO.setDataRicezione(rs.getString("ricevuto_il"));
                configurazioneVO.setTipoMittente(rs.getString("tipo_mittente"));
                configurazioneVO.setMittente(rs.getString("mittente"));
                configurazioneVO.setAssegnatarioUtenteId(rs
                        .getInt("assegnatario_utente_id"));
                configurazioneVO.setAssegnatarioUfficioId(rs
                        .getInt("assegnatario_ufficio_id"));
                configurazioneVO.setDestinatario(rs.getString("destinatario"));
                configurazioneVO.setTitolario(rs.getInt("titolario"));
                configurazioneVO.setTitolarioId(rs.getInt("titolario"));
                configurazioneVO.setOggetto(rs.getString("oggetto"));
                configurazioneVO.setParametriStampante(rs.getString("parametri_stampante"));
                configurazioneVO.setReturnValue(ReturnValues.FOUND);
            }

        } catch (Exception e) {
            logger.error("ConfigurazioneUtenteDAOjdbc: Load getConfigurazione",
                    e);
            throw new DataException(
                    "ConfigurazioneUtenteDAOjdbc: Cannot load getConfigurazione");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
        }
        return configurazioneVO;
    }

    public void salvaConfigurazione(Connection connection,
            ConfigurazioneUtenteVO vo) throws DataException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            if (connection == null) {
                logger.warn("newConfigurazione() - Invalid Connection :"
                        + connection);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }

            pstmt = connection.prepareStatement(INSERT_CONFIGURAZIONE);
            pstmt.setInt(1, vo.getTipoDocumentoId());

            pstmt.setString(2, vo.getDataDocumento());
            pstmt.setString(3, vo.getDataRicezione());

            pstmt.setString(4, vo.getTipoMittente());
            pstmt.setString(5, vo.getMittente());
            pstmt.setInt(6, vo.getAssegnatarioUtenteId());
            pstmt.setInt(7, vo.getAssegnatarioUfficioId());
            pstmt.setString(8, vo.getDestinatario());
            pstmt.setInt(9, vo.getTitolarioId());
            pstmt.setInt(10, vo.getUtenteId());
            pstmt.setString(11, vo.getOggetto());
            pstmt.setString(12, vo.getCheckOggetto());
            pstmt.setString(13, vo.getCheckTipoDocumento());
            pstmt.setString(14, vo.getCheckDataDocumento());
            pstmt.setString(15, vo.getCheckRicevutoIl());
            pstmt.setString(16, vo.getCheckTipoMittente());
            pstmt.setString(17, vo.getCheckMittente());
            pstmt.setString(18, vo.getCheckDestinatari());
            pstmt.setString(19, vo.getCheckAssegnatari());
            pstmt.setString(20, vo.getCheckTitolario());
            pstmt.setString(21, vo.getParametriStampante());

            pstmt.executeUpdate();

        } catch (Exception e) {
            logger.error("Load salvaConfigurazione", e);
            throw new DataException("Cannot load salvaConfigurazione");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
        }

    }

    public ConfigurazioneUtenteVO aggiornaConfigurazione(Connection conn,
            ConfigurazioneUtenteVO vo) throws DataException {
        vo.setReturnValue(ReturnValues.UNKNOWN);
        PreparedStatement pstmt = null;
        try {
            if (conn == null) {
                logger.warn("aggiornaConfigurazione() - Invalid Connection :"
                        + conn);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }

            pstmt = conn.prepareStatement(UPDATE_CONFIGURAZIONE);

            pstmt.setInt(1, vo.getTipoDocumentoId());
            pstmt.setString(2, vo.getDataDocumento());
            pstmt.setString(3, vo.getDataRicezione());
            pstmt.setString(4, vo.getTipoMittente());
            pstmt.setString(5, vo.getMittente());
            pstmt.setInt(6, vo.getAssegnatarioUfficioId());
            pstmt.setInt(7, vo.getAssegnatarioUtenteId());
            pstmt.setString(8, vo.getDestinatario());
            pstmt.setInt(9, vo.getTitolarioId());
            pstmt.setString(10, vo.getOggetto());
            pstmt.setString(11, vo.getCheckOggetto());
            pstmt.setString(12, vo.getCheckTipoDocumento());
            pstmt.setString(13, vo.getCheckDataDocumento());
            pstmt.setString(14, vo.getCheckRicevutoIl());
            pstmt.setString(15, vo.getCheckTipoMittente());
            pstmt.setString(16, vo.getCheckMittente());
            pstmt.setString(17, vo.getCheckDestinatari());
            pstmt.setString(18, vo.getCheckAssegnatari());
            pstmt.setString(19, vo.getCheckTitolario());
            pstmt.setInt(20, vo.getUtenteId());

            int n = pstmt.executeUpdate();

            if (n == 1) {
                vo = getConfigurazione(conn, vo.getUtenteId());
                if (vo.getReturnValue() == ReturnValues.FOUND) {
                    logger.debug("Configurazione utente aggiornato:"
                            + vo.getUtenteId());
                } else {
                    logger
                            .warn("Errore nella lettura della configuraziobne utente dopo l'aggiornamento! Id:"
                                    + vo.getUtenteId());
                    throw new DataException(
                            "Errore nell'aggiornamento della configuraziobne utente .");
                }
            } else {
                vo.setReturnValue(ReturnValues.INVALID);
                throw new DataException(
                        "Errore in aggiornamento configurazione");
            }

        } catch (SQLException e) {
            logger.error("aggiorna Configurazione", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
        return vo;
    }

    public ConfigurazioneUtenteVO aggiornaParametriStampante(Connection conn,
            ConfigurazioneUtenteVO vo) throws DataException {
        vo.setReturnValue(ReturnValues.UNKNOWN);
        PreparedStatement pstmt = null;
        try {
            if (conn == null) {
                logger.warn("aggiornaConfigurazione() - Invalid Connection :"
                        + conn);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }

            pstmt = conn.prepareStatement(UPDATE_CONFIGURAZIONE_STAMPANTE);

            pstmt.setString(1, vo.getParametriStampante());
            pstmt.setInt(2, vo.getUtenteId());

            int n = pstmt.executeUpdate();

            if (n == 1) {
                vo = getConfigurazione(conn, vo.getUtenteId());
                if (vo.getReturnValue() == ReturnValues.FOUND) {
                    logger.info("Configurazione utente aggiornato:"
                            + vo.getUtenteId());
                } else {
                    logger
                            .warn("Errore nella lettura della configuraziobne utente dopo l'aggiornamento! Id:"
                                    + vo.getUtenteId());
                    throw new DataException(
                            "Errore nell'aggiornamento della configuraziobne utente .");
                }
            } else {
                vo.setReturnValue(ReturnValues.INVALID);
                throw new DataException(
                        "Errore in aggiornamento configurazione");
            }

        } catch (SQLException e) {
            logger.error("aggiorna Configurazione", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
        return vo;
    }

}