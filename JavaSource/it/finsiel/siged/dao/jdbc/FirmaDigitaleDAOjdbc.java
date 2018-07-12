package it.finsiel.siged.dao.jdbc;

import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.CRLNonAggiornataException;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.integration.FirmaDigitaleDAO;
import it.finsiel.siged.mvc.vo.firma.CaVO;
import it.finsiel.siged.mvc.vo.firma.CrlUrlVO;
import it.finsiel.siged.rdb.JDBCManager;

import java.security.cert.X509CRLEntry;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class FirmaDigitaleDAOjdbc implements FirmaDigitaleDAO {

    static Logger logger = Logger.getLogger(FirmaDigitaleDAOjdbc.class
            .getName());

    private JDBCManager jdbcMan = new JDBCManager();

    private void caricaCa(ResultSet rs, CaVO vo) throws SQLException {
        vo.setId(rs.getInt("ca_id"));
        vo.setIssuerCN(rs.getString("issuer_cn"));
        vo.setValidoDal(rs.getDate("valido_dal"));
        vo.setValidoAl(rs.getDate("valido_al"));
        vo.setReturnValue(ReturnValues.FOUND);
    }

    public CaVO salvaCertificateAuthotority(CaVO vo) throws DataException {
        CaVO caVO;
        Connection connection = null;
        try {
            connection = jdbcMan.getConnection();
            connection.setAutoCommit(false);
            caVO = salvaCertificateAuthotority(connection, vo);
            connection.commit();
        } catch (Exception e) {
            jdbcMan.rollback(connection);
            throw new DataException(e.getMessage());
        } finally {
            jdbcMan.close(connection);
        }
        return caVO;
    }

    public CaVO salvaCertificateAuthotority(Connection connection, CaVO vo)
            throws DataException {

        CaVO newCa = new CaVO();
        newCa.setReturnValue(ReturnValues.UNKNOWN);

        PreparedStatement pstmt = null;

        try {
            if (connection == null) {
                logger.warn("Connection is:" + connection);
                throw new DataException("La connessione fornita ? invalida.");
            }

            pstmt = connection.prepareStatement(INSERT_CA);
            pstmt.setInt(1, vo.getId().intValue());
            pstmt.setString(2, vo.getIssuerCN());
            pstmt.setDate(3, new Date(vo.getValidoDal().getTime()));
            pstmt.setDate(4, new Date(vo.getValidoAl().getTime()));
            pstmt.executeUpdate();
            logger.debug("CA inserita - id=" + vo.getId().intValue());

        } catch (Exception e) {
            logger.error("salvaCertificateAuthotority", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }

        newCa = getCA(connection, vo.getId().intValue());
        newCa.setReturnValue(ReturnValues.SAVED);
        return newCa;
    }

    public CrlUrlVO salvaCaCRLPuntoDistribuzione(CrlUrlVO vo)
            throws DataException {
        CrlUrlVO crlVO;
        Connection connection = null;
        try {
            connection = jdbcMan.getConnection();
            connection.setAutoCommit(false);
            crlVO = salvaCaCRLPuntoDistribuzione(connection, vo);
            connection.commit();
        } catch (Exception e) {
            jdbcMan.rollback(connection);
            throw new DataException(e.getMessage());
        } finally {
            jdbcMan.close(connection);
        }
        return crlVO;
    }

    public CrlUrlVO salvaCaCRLPuntoDistribuzione(Connection connection,
            CrlUrlVO vo) throws DataException {
        PreparedStatement pstmt = null;

        try {
            if (connection == null) {
                logger.warn("Connection is:" + connection);
                throw new DataException("La connessione fornita ? invalida.");
            }
            pstmt = connection.prepareStatement(INSERT_CA_CRL);
            pstmt.setInt(1, vo.getId().intValue());
            pstmt.setInt(2, vo.getCaId());
            pstmt.setString(3, vo.getUrl());
            pstmt.setString(4, vo.getTipo());
            if (vo.getDataEmissione() != null)
                pstmt.setDate(5, new Date(vo.getDataEmissione().getTime()));
            else
                pstmt.setNull(5, Types.TIMESTAMP);

            pstmt.executeUpdate();
            logger.debug("CA CRL inserita - id=" + vo.getId().intValue());

        } catch (Exception e) {
            logger.error("salvaCADistributionPoint", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
        vo.setReturnValue(ReturnValues.SAVED);
        return vo;
    }

    public CaVO getCA(int id) throws DataException {
        CaVO out;
        Connection connection = null;
        try {
            connection = jdbcMan.getConnection();
            out = getCA(connection, id);
        } catch (Exception e) {
            throw new DataException(e.getMessage());
        } finally {
            jdbcMan.close(connection);
        }
        return out;
    }

    public CaVO getCA(Connection connection, int id) throws DataException {

        CaVO vo = new CaVO();
        vo.setReturnValue(ReturnValues.UNKNOWN);

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            if (connection == null) {
                logger.warn("Connection is:" + connection);
                throw new DataException("La connessione fornita ? invalida.");
            }
            pstmt = connection.prepareStatement(SELECT_CA_BY_ID);
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                // Imposta Campi Oggetto
                caricaCa(rs, vo);
                vo.setCrlUrls(getPuntiDistribuzioneCRLByCaId(connection, id));
            } else {
                vo.setReturnValue(ReturnValues.NOT_FOUND);
            }

        } catch (Exception e) {
            logger.error("getCA", e);
            throw new DataException("Impossibile leggere i dati della CA");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
        }
        return vo;
    }

    public CaVO getCAByCN(Connection connection, String cn)
            throws DataException {

        CaVO vo = new CaVO();
        vo.setReturnValue(ReturnValues.UNKNOWN);

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            if (connection == null) {
                logger.warn("Connection is:" + connection);
                throw new DataException("La connessione fornita ? invalida.");
            }
            pstmt = connection.prepareStatement(SELECT_CA_BY_CN);
            pstmt.setString(1, cn);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                // Imposta Campi Oggetto

                vo.setId(rs.getInt("ca_id"));
                vo.setIssuerCN(rs.getString("issuer_cn"));
                vo.setValidoDal(rs.getDate("valido_dal"));
                vo.setValidoAl(rs.getDate("valido_al"));
                vo.setReturnValue(ReturnValues.FOUND);
            } else {
                vo.setReturnValue(ReturnValues.NOT_FOUND);
            }

        } catch (Exception e) {
            logger.error("getCA", e);
            throw new DataException("Impossibile leggere i dati della CA");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
        }
        return vo;
    }

    public Date getDataSeRevocato(String issuerDN, String serial)
            throws DataException, CRLNonAggiornataException {
        Date revoca = null;
        int codice = 0;
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            connection = jdbcMan.getConnection();

            pstmt = connection.prepareStatement(SELECT_REVOKED_CERTIFICATE);
            pstmt.setString(1, issuerDN);
            pstmt.setString(2, serial);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                revoca = rs.getDate("data_revoca");
            } else {
                jdbcMan.close(rs);
                jdbcMan.close(pstmt);
                pstmt = connection.prepareStatement(SELECT_STATO_CRL_BY_CN);
                pstmt.setString(1, issuerDN);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    int codiceErrore = rs.getInt("CODICE_ERRORE");
                    if (codiceErrore > 0) {
                        codice = codiceErrore;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("getDataSeRevocato", e);
            throw new DataException("Impossibile leggere i dati");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
            jdbcMan.close(connection);
        }
        if (codice > 0) {
            throw new CRLNonAggiornataException(
                    "CRL non aggiornata. Codice errore:" + codice);
        }
        return revoca;
    }

    public Collection<CaVO> getAllCA() throws DataException {

        ArrayList<CaVO> all = new ArrayList<CaVO>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(SELECT_ALL_CA);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                // Imposta Campi Oggetto
                CaVO vo = new CaVO();
                caricaCa(rs, vo);
                vo.setCrlUrls(getPuntiDistribuzioneCRLByCaId(connection, vo
                        .getId().intValue()));
                all.add(vo);
            }

        } catch (Exception e) {
            logger.error("getCA", e);
            throw new DataException("Impossibile leggere i dati della CA");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
            jdbcMan.close(connection);
        }
        return all;
    }

    public Collection<CrlUrlVO> getPuntiDistribuzioneCRL() throws DataException {
        ArrayList<CrlUrlVO> cas = new ArrayList<CrlUrlVO>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(SELECT_ALL_CRL_DP);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                CrlUrlVO vo = new CrlUrlVO();
                vo.setId(rs.getInt("id"));
                vo.setUrl(rs.getString("url"));
                vo.setCaId(rs.getInt("ca_id"));
                vo.setTipo(rs.getString("tipo"));
                vo
                        .setDataEmissione(rs.getDate("data_emissione") == null ? (null)
                                : new java.util.Date(rs.getDate(
                                        "data_emissione").getTime()));
                vo.setReturnValue(ReturnValues.FOUND);
                cas.add(vo);
            }

        } catch (Exception e) {
            logger.error("getCA", e);
            throw new DataException("Impossibile leggere i dati della CA");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
        }
        return cas;
    }

    private Map<String,CrlUrlVO> getPuntiDistribuzioneCRLByCaId(Connection connection, int caId)
            throws DataException {
        HashMap<String,CrlUrlVO> cas = new HashMap<String,CrlUrlVO>(1);
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            if (connection == null) {
                logger.warn("Connection is:" + connection);
                throw new DataException("La connessione fornita non e' valida.");
            }
            pstmt = connection.prepareStatement(SELECT_ALL_CRL_DP_BY_CA_ID);
            pstmt.setInt(1, caId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                CrlUrlVO vo = new CrlUrlVO();
                vo.setId(rs.getInt("id"));
                vo.setUrl(rs.getString("url"));
                vo.setCaId(rs.getInt("ca_id"));
                vo.setTipo(rs.getString("tipo"));
                vo
                        .setDataEmissione(rs.getDate("data_emissione") == null ? (null)
                                : new java.util.Date(rs.getDate(
                                        "data_emissione").getTime()));
                vo.setReturnValue(ReturnValues.FOUND);
                cas.put(vo.getUrl(), vo);
            }

        } catch (Exception e) {
            logger.error("getCA", e);
            throw new DataException("Impossibile leggere i dati della CA");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
        }
        return cas;
    }

    /*
     * Aggiorna la lista dei certificati revocati per la CA in argomento.
     * Elimina ogni record presente ed inserisce i nuovi (set in argomento)
     */

    public void salvaListaCertificatiRevocati(Set<X509CRLEntry> revokedCerts, int caId,
            int crlId) throws DataException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            connection = jdbcMan.getConnection();
            connection.setAutoCommit(false);
            pstmt = connection
                    .prepareStatement(DELETE_REVOKED_CERTIFICATES_BY_CA_ID);
            pstmt.setInt(1, caId);
            pstmt.executeUpdate();
            pstmt.close();
            if (revokedCerts != null) {
                // inserisco i certificati revocati
                Iterator<X509CRLEntry> it = revokedCerts.iterator();
                while (it.hasNext()) {
                    X509CRLEntry entry = it.next();
                    pstmt = connection
                            .prepareStatement(INSERT_REVOKED_CERT_ENTRY);
                    pstmt.setInt(1, caId);
                    pstmt.setString(2, entry.getSerialNumber().toString());
                    pstmt.setDate(3, new Date(entry.getRevocationDate()
                            .getTime()));
                    pstmt.executeUpdate();
                    pstmt.close();
                }
            }
            setStatoErroreCRL(connection, crlId, Parametri.CRL_OK);
            connection.commit();
        } catch (Exception e) {
            logger.error("", e);
            jdbcMan.rollback(connection);
            throw new DataException(e.getMessage());
        } finally {
            jdbcMan.close(connection);
        }
    }

    public void setStatoErroreCRL(int caCrlId, int errorCode) {
        Connection connection = null;

        try {
            connection = jdbcMan.getConnection();
            setStatoErroreCRL(connection, caCrlId, errorCode);
            connection.commit();
        } catch (Exception e) {
            logger.error("", e);
        } finally {

            jdbcMan.close(connection);
        }
    }

    public void setStatoErroreCRL(Connection connection, int caCrlId,
            int errorCode) throws Exception {
        PreparedStatement pstmt = null;
        try {
            if (connection == null) {
                logger.warn("Connection is:" + connection);
                throw new DataException("La connessione fornita non e' valida.");
            }
            pstmt = connection.prepareStatement(UPDATE_STATO_CRL);
            pstmt.setInt(1, errorCode);
            pstmt.setInt(2, caCrlId);
            pstmt.executeUpdate();

        } finally {
            jdbcMan.close(pstmt);
        }
    }

    /**
     * Cancella una CA ed i relativi dati: i punti di distribuzione delle CRL e
     * la lista dei Certificati Revocati
     */
    public void cancellaCA(Connection connection, int caId)
            throws DataException {
        PreparedStatement pstmt = null;
        try {
            if (connection == null) {
                logger.warn("Connection is:" + connection);
                throw new DataException("La connessione fornita non e' valida.");
            }
            pstmt = connection
                    .prepareStatement(DELETE_ALL_REVOKED_CERTIFICATES_BY_CA_ID);
            pstmt.setInt(1, caId);
            pstmt.executeUpdate();
            pstmt.close();
            pstmt = connection.prepareStatement(DELETE_ALL_CA_CRL_BY_CA_ID);
            pstmt.setInt(1, caId);
            pstmt.executeUpdate();
            pstmt.close();
            pstmt = connection.prepareStatement(DELETE_CA_BY_ID);
            pstmt.setInt(1, caId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            throw new DataException(
                    "Errore durante la cancellazione delle CA.\nDettaglio Errore:\n"
                            + e.getMessage());
        } finally {
            jdbcMan.close(pstmt);
        }
    }

    /**
     * Cancella tutte le CA ed i relativi dati: i punti di distribuzione delle
     * CRL e la lista dei Certificati Revocati
     */
    public void cancellaTutteCA() throws DataException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            connection = jdbcMan.getConnection();
            connection.setAutoCommit(false);
            pstmt = connection
                    .prepareStatement(DELETE_ALL_REVOKED_CERTIFICATES);
            pstmt.executeUpdate();
            pstmt.close();
            pstmt = connection.prepareStatement(DELETE_ALL_CA_CRL);
            pstmt.executeUpdate();
            pstmt.close();
            pstmt = connection.prepareStatement(DELETE_CA);
            pstmt.executeUpdate();
            pstmt.close();
            connection.commit();
        } catch (Exception e) {
            jdbcMan.rollback(connection);
            throw new DataException(
                    "Errore durante la cancellazione delle CA.\nDettaglio Errore:\n"
                            + e.getMessage());
        } finally {
            jdbcMan.close(pstmt);
            jdbcMan.close(connection);
        }
    }

    private final static String SELECT_REVOKED_CERTIFICATE = "SELECT * FROM CA_REVOKED_LIST REV, CA_LISTA CA WHERE CA.ISSUER_CN=? AND REV.SERIAL_NUMBER=? AND REV.CA_ID=CA.CA_ID ";

    private final static String SELECT_STATO_CRL_BY_CN = "SELECT CODICE_ERRORE FROM CA_CRL CRL, CA_LISTA CA WHERE CA.CA_ID=CRL.CA_ID AND ISSUER_CN=?";

    private final static String SELECT_CA_BY_ID = "SELECT * FROM CA_LISTA WHERE CA_ID = ?";

    private final static String SELECT_ALL_CA = "SELECT * FROM CA_LISTA";

    private final static String SELECT_CA_BY_CN = "SELECT * FROM CA_LISTA WHERE ISSUER_CN = ?";

    private final static String INSERT_CA = "INSERT INTO CA_LISTA (CA_ID, ISSUER_CN, VALIDO_DAL, VALIDO_AL ) VALUES ( ?, ?, ?, ?)";

    private final static String INSERT_CA_CRL = "INSERT INTO CA_CRL (ID , CA_ID, URL, TIPO, DATA_EMISSIONE) VALUES( ?, ?, ?, ?, ?)";

    private final static String INSERT_REVOKED_CERT_ENTRY = "INSERT INTO CA_REVOKED_LIST ( CA_ID, SERIAL_NUMBER, DATA_REVOCA) VALUES( ?, ?, ? )";

    private final static String SELECT_ALL_CRL_DP = "SELECT * FROM CA_CRL";

    private final static String SELECT_ALL_CRL_DP_BY_CA_ID = "SELECT * FROM CA_CRL WHERE CA_ID=?";

    private final static String DELETE_REVOKED_CERTIFICATES_BY_CA_ID = "DELETE FROM CA_REVOKED_LIST WHERE CA_ID=?";

    private final static String DELETE_ALL_REVOKED_CERTIFICATES_BY_CA_ID = "DELETE FROM CA_REVOKED_LIST WHERE CA_ID=?";

    private final static String DELETE_ALL_CA_CRL_BY_CA_ID = "DELETE FROM CA_CRL WHERE CA_ID=?";

    private final static String DELETE_CA_BY_ID = "DELETE FROM CA_LISTA WHERE CA_ID=?";

    private final static String DELETE_ALL_REVOKED_CERTIFICATES = "DELETE FROM CA_REVOKED_LIST";

    private final static String DELETE_ALL_CA_CRL = "DELETE FROM CA_CRL";

    private final static String DELETE_CA = "DELETE FROM CA_LISTA";

    private final static String UPDATE_STATO_CRL = "UPDATE CA_CRL SET CODICE_ERRORE=? WHERE ID=?";
};