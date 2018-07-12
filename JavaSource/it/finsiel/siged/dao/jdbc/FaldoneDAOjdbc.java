package it.finsiel.siged.dao.jdbc;

import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.integration.FaldoneDAO;
import it.finsiel.siged.mvc.presentation.helper.FaldoneView;
import it.finsiel.siged.mvc.vo.MigrazioneVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.protocollo.FaldoneFascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.FaldoneVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.finsiel.siged.util.DateUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public class FaldoneDAOjdbc implements FaldoneDAO {
   
	//private static final String SELECT_FALDONI_PER_AOO = "SELECT * FROM FALDONI WHERE aoo_id=?";

    //private static final String SELECT_FALDONI_PER_UFFICIO = "SELECT * FROM FALDONI WHERE ufficio_id=?";

    public final static String SELECT_FALDONE_BY_ID_VERSIONE = "SELECT f.* "
            + "FROM STORIA_FALDONI f "
            + "WHERE faldone_id = ? and f.versione = ?";

    //public final static String SELECT_STATI_FALDONE = "SELECT * FROM STATI_FALDONE ORDER BY stato_faldone_id";

    //public final static String SELECT_STATO_FALDONE_BY_ID = "SELECT * FROM STATI_FALDONE WHERE stato_faldone_id=?";

    private static final String INSERT_FALDONE = "INSERT INTO FALDONI (faldone_id, aoo_id, "
            + "numero_faldone, oggetto, ufficio_id, row_created_time, "
            + "row_created_user, codice_locale, sotto_categoria, nota, "
            + "titolario_id, anno, numero, data_creazione, data_carico, "
            + "data_scarico, data_evidenza, data_movimento, stato_id,collocazione_label1,collocazione_label2, "
            + " collocazione_label3,collocazione_label4,collocazione_valore1,collocazione_valore2,collocazione_valore3,"
            + "collocazione_valore4, row_updated_user) "
            + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

    private static final String UPDATE_FALDONE = "UPDATE FALDONI set "
            + "oggetto=?, ufficio_id=?, titolario_id=?, row_updated_user=?, "
            + "row_updated_time=?, " + "codice_locale=?, "
            + "sotto_categoria=?, " + "nota=?, data_carico=?, "
            + "data_scarico=?, " + "data_evidenza=?, " + "data_movimento=?, "
            + "stato_id=?, " + "collocazione_label1=?, "
            + "collocazione_label2=?, " + "collocazione_label3=?, "
            + "collocazione_label4=?, " + "collocazione_valore1=?, "
            + "collocazione_valore2=?, " + "collocazione_valore3=?, "
            + "collocazione_valore4=? " + "where faldone_id=?";

    private static final String UPDATE_DATA_SCARICO_SU_STORIA_FALDONE = "UPDATE STORIA_FALDONI set "
            + "data_scarico=? " + "where faldone_id=? AND versione=?";

    public final static String INSERT_FALDONE_FASCICOLO = "INSERT INTO faldone_fascicoli "
            + " (faldone_id, fascicolo_id, versione, row_updated_user, row_updated_time ) VALUES (?, ?, ?, ?, ?)";

    // public final static String INSERT_FALDONE_PROCEDIMENTO = "INSERT INTO
    // faldone_procedimenti "
    // + " (faldone_id, procedimento_id ) VALUES (?, ?)";

    public final static String INSERT_FALDONE_PROCEDIMENTO = "INSERT INTO procedimenti_faldone "
            + " (procedimento_id, faldone_id) VALUES (?, ?)";

    public final static String SELECT_ULTIMO_FALDONE = "SELECT MAX(numero) FROM faldoni WHERE aoo_id = ? and anno=?";

    public final static String SELECT_FALDONE = "SELECT * FROM faldoni "
            + "WHERE faldone_id =?";

    public final static String SELECT_FALDONE_BY_ANNO_NUMERO = "SELECT * FROM faldoni "
            + "WHERE anno =? and numero= ?";

    public final static String DELETE_FALDONE_FASCICOLI = "DELETE FROM faldone_fascicoli WHERE faldone_id =?";

    public final static String SELECT_FALDONE_FASCICOLI = "SELECT * FROM faldone_fascicoli WHERE faldone_id =? order by fascicolo_id desc";

    public final static String DELETE_FALDONE_PROCEDIMENTI = "DELETE FROM procedimenti_faldone WHERE faldone_id =?";

    public final static String SELECT_FALDONE_PROCEDIMENTI = "SELECT * FROM procedimenti_faldone WHERE faldone_id =?";

    public final static String CANCELLA_FALDONE = "DELETE FROM faldoni WHERE faldone_id=?";

    static Logger logger = Logger.getLogger(FaldoneDAOjdbc.class.getName());

    private JDBCManager jdbcMan = new JDBCManager();

    public FaldoneVO aggiornaFaldone(Connection connection, FaldoneVO faldoneVO)
            throws DataException {
        faldoneVO.setReturnValue(ReturnValues.UNKNOWN);
        PreparedStatement pstmt = null;
        try {
            if (connection == null) {
                logger.warn("aggiornaFaldone() - Invalid Connection :"
                        + connection);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }

            int faldoneId = faldoneVO.getId().intValue();
            pstmt = connection.prepareStatement(UPDATE_FALDONE);

            int n = 0;

            pstmt.setInt(1, faldoneVO.getAooId());
            pstmt.setString(2, faldoneVO.getOggetto());
            pstmt.setInt(3, faldoneVO.getUfficioId());
            pstmt.setInt(4, faldoneVO.getTitolarioId());
            pstmt.setDate(5, new Date(faldoneVO.getRowCreatedTime().getTime()));
            pstmt.setString(6, faldoneVO.getRowCreatedUser());
            pstmt.setString(7, faldoneVO.getRowUpdatedUser());
            pstmt.setDate(8, new Date(System.currentTimeMillis()));
            pstmt.setString(9, faldoneVO.getCodiceLocale());
            pstmt.setString(10, faldoneVO.getSottoCategoria());
            pstmt.setString(11, faldoneVO.getNota());
            pstmt.setString(12, faldoneVO.getNumeroFaldone());
            pstmt.setInt(13, faldoneVO.getAnno());
            pstmt.setInt(14, faldoneVO.getNumero());

            if (faldoneVO.getDataCreazione() != null) {
                pstmt.setDate(15, new Date(faldoneVO.getDataCreazione()
                        .getTime()));
            } else
                pstmt.setNull(15, Types.DATE);
            if (faldoneVO.getDataCarico() != null) {
                pstmt
                        .setDate(16, new Date(faldoneVO.getDataCarico()
                                .getTime()));
            } else
                pstmt.setNull(16, Types.DATE);
            if (faldoneVO.getDataScarico() != null) {
                pstmt.setDate(17,
                        new Date(faldoneVO.getDataScarico().getTime()));
            } else
                pstmt.setNull(17, Types.DATE);
            if (faldoneVO.getDataEvidenza() != null) {
                pstmt.setDate(18, new Date(faldoneVO.getDataEvidenza()
                        .getTime()));
            } else
                pstmt.setNull(18, Types.DATE);
            if (faldoneVO.getDataMovimento() != null) {
                pstmt.setDate(19, new Date(faldoneVO.getDataMovimento()
                        .getTime()));
            } else
                pstmt.setNull(19, Types.DATE);
            pstmt.setInt(20, faldoneVO.getPosizioneSelezionata());
            pstmt.setString(21, faldoneVO.getCollocazioneLabel1());
            pstmt.setString(22, faldoneVO.getCollocazioneLabel2());
            pstmt.setString(23, faldoneVO.getCollocazioneLabel3());
            pstmt.setString(24, faldoneVO.getCollocazioneLabel4());
            pstmt.setString(25, faldoneVO.getCollocazioneValore1());
            pstmt.setString(26, faldoneVO.getCollocazioneValore2());
            pstmt.setString(27, faldoneVO.getCollocazioneValore3());
            pstmt.setString(28, faldoneVO.getCollocazioneValore4());

            pstmt.setInt(29, faldoneVO.getId().intValue());

            pstmt.executeUpdate();

            n = pstmt.executeUpdate();

            if (n == 1) {
                faldoneVO = getFaldone(connection, faldoneId);
                if (faldoneVO.getReturnValue() == ReturnValues.FOUND) {
                    faldoneVO.setReturnValue(ReturnValues.SAVED);
                    logger.info("Fascicolo aggiornato:" + faldoneId);
                } else {
                    logger
                            .warn("Errore nella lettura del faldone dopo l'aggiornamento! Id:"
                                    + faldoneId);
                    throw new DataException(
                            "Errore nell'aggiornamento del faldone.");
                }
            } else {
                faldoneVO.setReturnValue(ReturnValues.OLD_VERSION);
                throw new DataException("Errore nella gestione delle versioni");
            }

        } catch (SQLException e) {
            logger.error("Aggiorna Faldone", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
        return faldoneVO;
    }

    public FaldoneVO aggiornaDataScaricoSuStoria(Connection connection,
            FaldoneVO faldoneVO) throws DataException {
        faldoneVO.setReturnValue(ReturnValues.UNKNOWN);
        PreparedStatement pstmt = null;
        try {
            if (connection == null) {
                logger
                        .warn("aggiornaDataScaricoSuStoria() - Invalid Connection :"
                                + connection);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }

            int faldoneId = faldoneVO.getId().intValue();
            pstmt = connection
                    .prepareStatement(UPDATE_DATA_SCARICO_SU_STORIA_FALDONE);

            int n = 0;

            pstmt.setDate(1, new Date(faldoneVO.getDataScarico().getTime()));
            pstmt.setInt(2, faldoneVO.getId().intValue());
            pstmt.setInt(3, faldoneVO.getVersione());
            pstmt.executeUpdate();

            n = pstmt.executeUpdate();

            if (n == 1) {
                faldoneVO = getFaldone(connection, faldoneId);
                if (faldoneVO.getReturnValue() == ReturnValues.FOUND) {
                    faldoneVO.setReturnValue(ReturnValues.SAVED);
                    logger.info("Faldone aggiornato:" + faldoneId);
                } else {
                    logger
                            .warn("Errore nella lettura del faldone dopo l'aggiornamento! Id:"
                                    + faldoneId);
                    throw new DataException(
                            "Errore nell'aggiornamento del faldone.");
                }
            } else {
                faldoneVO.setReturnValue(ReturnValues.OLD_VERSION);
                throw new DataException("Errore nella gestione delle versioni");
            }

        } catch (SQLException e) {
            logger.error("Aggiorna Faldone", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
        return faldoneVO;
    }

    public Collection<MigrazioneVO> getFaldoniAnnoNumero(Connection connection)
            throws DataException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Collection<MigrazioneVO>  faldoni = new ArrayList<MigrazioneVO>();

        try {
            if (connection == null) {
                logger.warn("getFaldoniAnnoNumero() - Invalid Connection :"
                        + connection);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            String sql = "SELECT ANNO,Max(NUMERO)AS Numero  FROM faldoni GROUP BY ANNO";
            pstmt = connection.prepareStatement(sql);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                MigrazioneVO migrazione = new MigrazioneVO();
                migrazione.setAnno(rs.getInt(1));
                migrazione.setNumero(rs.getInt(2));
                faldoni.add(migrazione);
            }

        } catch (Exception e) {
            logger.error("Load getFaldoniAnnoNumero ", e);
            throw new DataException("Cannot load Faldoni by ID");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
        }
        return faldoni;
    }

    public Collection<FaldoneVO> getFaldoniPerAoo(int aoo_id) throws DataException {
        //PreparedStatement pstmt = null;
        //ResultSet rs = null;
        //Connection connection = null;
        Collection<FaldoneVO> faldoni = new ArrayList<FaldoneVO>();
        /*
        try {
            if (connection == null) {
                logger.warn("getFaldoniPerAoo() - Invalid Connection :"
                        + connection);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            
            pstmt = connection.prepareStatement(SELECT_FALDONI_PER_AOO);
            pstmt.setInt(1, aoo_id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                // Aggiornare il metodo query
            }

        } catch (Exception e) {
            logger.error("Load getFaldoniPerAoo", e);
            throw new DataException("Cannot load FaldoniPerAoo");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
        }
        */
        return faldoni;
    }

    public FaldoneVO getFaldone(int faldoneId) throws DataException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection connection = null;
        FaldoneVO fVO = new FaldoneVO();
        try {
            connection = jdbcMan.getConnection();
            fVO = getFaldone(connection, faldoneId);
        } catch (Exception e) {
            logger.error("Load getFaldone", e);
            throw new DataException("Cannot load getFaldone");
        } finally {
        	jdbcMan.closeAll(rs, pstmt, connection);
        }
        return fVO;
    }

    // STORIA FALDONE
    public Collection<FaldoneView> getStoriaFaldone(int faldoneId) throws DataException {
        ArrayList<FaldoneView> storiaFaldone = new ArrayList<FaldoneView>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sqlStoriaFaldone = "SELECT faldone_id, STORIA_FALDONI.aoo_id, "
                    + "STORIA_FALDONI.oggetto, STORIA_FALDONI.ufficio_id, STORIA_FALDONI.titolario_id, "
                    + "STORIA_FALDONI.row_created_time, STORIA_FALDONI.row_created_user, "
                    + "STORIA_FALDONI.row_updated_user, "
                    + "STORIA_FALDONI.row_updated_time, STORIA_FALDONI.codice_locale, "
                    + "STORIA_FALDONI.sotto_categoria, STORIA_FALDONI.nota, STORIA_FALDONI.numero_faldone, "
                    + "STORIA_FALDONI.anno, STORIA_FALDONI.numero, STORIA_FALDONI.data_creazione,"
                    + "STORIA_FALDONI.data_carico, STORIA_FALDONI.data_scarico, "
                    + "STORIA_FALDONI.data_evidenza, STORIA_FALDONI.data_movimento,"
                    + "STORIA_FALDONI.stato_id, STORIA_FALDONI.versione,  "
                    + "uffici.descrizione AS ufficio "
                    + "FROM STORIA_FALDONI,  uffici WHERE faldone_id=? "
                    + "AND STORIA_FALDONI.ufficio_id=Uffici.ufficio_id "
                    + "ORDER BY VERSIONE desc";
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(sqlStoriaFaldone);
            pstmt.setInt(1, faldoneId);
            rs = pstmt.executeQuery();
            FaldoneView faldone;
            while (rs.next()) {
                faldone = new FaldoneView();
                faldone.setId(rs.getInt("faldone_id"));
                faldone.setAooId(rs.getInt("aoo_id"));
                faldone.setOggetto(rs.getString("oggetto"));
                faldone.setUfficioId(rs.getInt("ufficio_id"));
                faldone.setTitolarioId(rs.getInt("titolario_id"));
                faldone.setRowCreatedTime(rs.getDate("row_created_time"));
                faldone.setRowCreatedUser(rs.getString("row_created_user"));
                faldone.setRowUpdatedUser(rs.getString("row_updated_user"));
                faldone.setRowUpdatedTime(rs.getDate("row_updated_time"));
                faldone.setCodiceLocale(rs.getString("codice_locale"));
                faldone.setSottoCategoria(rs.getString("sotto_categoria"));
                faldone.setNota(rs.getString("nota"));
                faldone.setNumeroFaldone(rs.getString("numero_faldone"));
                faldone.setAnno(rs.getInt("anno"));
                faldone.setNumero(rs.getInt("numero"));
                if (rs.getDate("data_creazione") != null)
                    faldone.setDataCreazione(DateUtil.formattaData(rs.getDate(
                            "data_creazione").getTime()));
                if (rs.getDate("data_carico") != null)
                    faldone.setDataCarico(DateUtil.formattaData(rs.getDate(
                            "data_carico").getTime()));
                if (rs.getDate("data_scarico") != null)
                    faldone.setDataScarico(DateUtil.formattaData(rs.getDate(
                            "data_scarico").getTime()));
                if (rs.getDate("data_evidenza") != null)
                    faldone.setDataEvidenza(DateUtil.formattaData(rs.getDate(
                            "data_evidenza").getTime()));
                if (rs.getDate("data_movimento") != null)
                    faldone.setDataMovimento(DateUtil.formattaData(rs.getDate(
                            "data_movimento").getTime()));
                faldone.setStatoId(rs.getInt("stato_id"));
                faldone.setVersione(rs.getInt("versione"));
                faldone.setPosizioneSelezionata(rs.getString("stato_faldone"));
                faldone.setUfficio(rs.getString("ufficio"));
                storiaFaldone.add(faldone);
            }
        } catch (Exception e) {
            logger.error("getStoriaFaldone", e);
            throw new DataException("Cannot load getStoriaFaldone");
        } finally {
        	jdbcMan.closeAll(rs, pstmt, connection);
        }
        return storiaFaldone;

    }

    public FaldoneVO getFaldoneByIdVersione(int id, int versione)
            throws DataException {
        FaldoneVO faldoneVO = new FaldoneVO();
        Connection connection = null;
        try {
            connection = jdbcMan.getConnection();
            faldoneVO = getFaldoneByIdVersione(connection, id, versione);
        } catch (Exception e) {
            throw new DataException(e.getMessage() + " FaldoneId :" + id
                    + " Versione :" + versione);
        } finally {
            jdbcMan.close(connection);
        }
        return faldoneVO;
    }

    public FaldoneVO getFaldoneByIdVersione(Connection connection, int id,
            int versione) throws DataException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FaldoneVO faldone = new FaldoneVO();
        try {
            if (connection == null) {
                logger.warn("getFascicoloByIdVersione() - Invalid Connection :"
                        + connection);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            pstmt = connection.prepareStatement(SELECT_FALDONE_BY_ID_VERSIONE);
            pstmt.setInt(1, id);
            pstmt.setInt(2, versione);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                faldone = getFaldone(rs);
                faldone.setReturnValue(ReturnValues.FOUND);
            } else {
                faldone.setReturnValue(ReturnValues.NOT_FOUND);
            }

        } catch (Exception e) {
            logger.error("Load Versione faldone by ID", e);
            throw new DataException("Cannot load Versione faldone by ID");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
        }
        return faldone;
    }

    private FaldoneVO getFaldone(ResultSet rs) throws DataException {
        FaldoneVO faldone = new FaldoneVO();
        try {
            faldone.setId(rs.getInt("faldone_id"));
            faldone.setAooId(rs.getInt("aoo_id"));
            faldone.setAnno(rs.getInt("anno"));
            faldone.setNumero(rs.getInt("numero"));
            faldone.setCollocazioneLabel1(rs.getString("collocazione_label1"));
            faldone.setCollocazioneLabel2(rs.getString("collocazione_label2"));
            faldone.setCollocazioneLabel3(rs.getString("collocazione_label3"));
            faldone.setCollocazioneLabel4(rs.getString("collocazione_label4"));
            faldone
                    .setCollocazioneValore1(rs
                            .getString("collocazione_valore1"));
            faldone
                    .setCollocazioneValore2(rs
                            .getString("collocazione_valore2"));
            faldone
                    .setCollocazioneValore3(rs
                            .getString("collocazione_valore3"));
            faldone
                    .setCollocazioneValore4(rs
                            .getString("collocazione_valore4"));
            faldone.setUfficioId(rs.getInt("ufficio_id"));
            faldone.setCodiceLocale(rs.getString("codice_locale"));
            faldone.setSottoCategoria(rs.getString("sotto_categoria"));
            faldone.setNota(rs.getString("nota"));
            faldone.setNumeroFaldone(rs.getString("numero_faldone"));
            faldone.setOggetto(rs.getString("oggetto"));
            faldone.setTitolarioId(rs.getInt("titolario_id"));
            faldone.setRowCreatedTime(rs.getDate("row_created_time"));
            faldone.setRowCreatedUser(rs.getString("row_created_user"));
            faldone.setRowUpdatedUser(rs.getString("row_updated_user"));
            faldone.setRowUpdatedTime(rs.getDate("row_updated_time"));
            faldone.setDataCreazione(rs.getDate("data_creazione"));
            faldone.setDataCarico(rs.getDate("data_carico"));
            faldone.setDataScarico(rs.getDate("data_scarico"));
            faldone.setDataEvidenza(rs.getDate("data_evidenza"));
            faldone.setDataMovimento(rs.getDate("data_movimento"));
            faldone.setPosizioneSelezionata(rs.getInt("stato_id"));
            faldone.setStato(rs.getString("stato"));
            faldone.setVersione(rs.getInt("versione"));
        } catch (SQLException e) {
            logger.error("Load getFaldone()", e);
            throw new DataException("Cannot load getFaldone()");
        }
        return faldone;
    }
/*
    public IdentityVO getStatoFaldone(int statoId) throws DataException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection connection = null;
        IdentityVO fVO = new IdentityVO();
        try {
            connection = jdbcMan.getConnection();
            fVO = getStatoFaldone(connection, statoId);
        } catch (Exception e) {
            logger.error("Load getStatoFaldone", e);
            throw new DataException("Cannot load getStatoFaldone");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
            jdbcMan.close(connection);
        }
        return fVO;
    }
*/
    public Collection<Integer> getFaldoneFascicoliIds(Connection connection,
            int faldoneId) throws DataException {
        PreparedStatement pstmt = null;
        ArrayList<Integer> list = new ArrayList<Integer>();
        ResultSet rs = null;
        try {
            if (connection == null) {
                logger.warn(" - Invalid Connection :" + connection);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            pstmt = connection.prepareStatement(SELECT_FALDONE_FASCICOLI);
            pstmt.setInt(1, faldoneId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Integer(rs.getInt("fascicolo_id")));

            }
        } catch (Exception e) {
            logger.error("getFaldoneFascicoliIds", e);
            throw new DataException("getFaldoneFascicoliIds");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
        }
        return list;
    }

    public Collection<Integer> getFaldoneProcedimentiIds(Connection connection,
            int faldoneId) throws DataException {
        PreparedStatement pstmt = null;
        ArrayList<Integer> list = new ArrayList<Integer>();
        ResultSet rs = null;
        try {
            if (connection == null) {
                logger.warn(" - Invalid Connection :" + connection);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            pstmt = connection.prepareStatement(SELECT_FALDONE_PROCEDIMENTI);
            pstmt.setInt(1, faldoneId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Integer(rs.getInt("procedimento_id")));
            }
        } catch (Exception e) {
            logger.error("getFaldoneProcedimentiIds", e);
            throw new DataException("getFaldoneProcedimentiIds");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
        }
        return list;
    }

    /*
    public Collection getStatiFaldone() throws DataException {
        Collection stati = new ArrayList();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(SELECT_STATI_FALDONE);
            rs = pstmt.executeQuery();
            StatoFaldoneVO stato;
            while (rs.next()) {
                stato = new StatoFaldoneVO();
                stato.setId(rs.getInt("stato_faldone_id"));
                stato.setDescrizione(rs.getString("descrizione"));
                stati.add(stato);
            }
        } catch (Exception e) {
            logger.error("Load getStatiFaldone", e);
            throw new DataException("Cannot load getStatiFaldone");
        } finally {
        	jdbcMan.closeAll(rs, pstmt, connection);
        }
        return stati;
    }
*/
    public SortedMap<Integer,FaldoneView> cercaFaldoni(Utente utente, String ufficiUtenti,
            HashMap<String,String>  sqlDB) throws DataException {
        SortedMap<Integer,FaldoneView>  faldoni = new TreeMap<Integer,FaldoneView> (new Comparator<Integer> () {
            public int compare(Integer i1, Integer i2) {
               return i2.intValue() - i1.intValue();
            }
        });
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        StringBuffer strQuery = new StringBuffer(
                "SELECT * FROM faldoni f where aoo_id=? ");

        if (utente.getUfficioVOInUso().getTipo().equals(
                UfficioVO.UFFICIO_NORMALE)) {
            strQuery.append(" AND (ufficio_id IN (").append(ufficiUtenti)
                    .append(")");
            strQuery
                    .append(
                            " OR EXISTS (SELECT ufficio_id from storia_faldoni S WHERE "
                                    + " f.faldone_id =s.faldone_id  AND s.ufficio_id IN (")
                    .append(ufficiUtenti).append(")))");

        }

        if (sqlDB != null) {
            for (Iterator<Entry<String, String>> it = sqlDB.entrySet().iterator(); it.hasNext();) {
                Map.Entry<String, String> entry = it.next();
                Object key = entry.getKey();
                strQuery.append(" AND ").append(key.toString());
            }
        }

        int indiceQuery = 1;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(strQuery.toString());
            int aooId = utente.getUfficioVOInUso().getAooId();
            pstmt.setInt(indiceQuery, aooId);
            indiceQuery = indiceQuery + 1;
            if (sqlDB != null) {
                for (Iterator<Entry<String, String>> it = sqlDB.entrySet().iterator(); it.hasNext();) {
                    Map.Entry<String, String> entry =  it.next();
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    if (value instanceof Integer) {
                        pstmt.setInt(indiceQuery, ((Integer) value).intValue());
                    } else if (value instanceof String) {
                        if (key.toString().indexOf("LIKE") > 0) {
                            pstmt
                                    .setString(indiceQuery, value.toString()
                                            + "%");
                        } else {
                            pstmt.setString(indiceQuery, value.toString());
                        }
                    } else if (value instanceof java.util.Date) {
                        java.util.Date d = (java.util.Date) value;
                        pstmt.setDate(indiceQuery, new java.sql.Date(d
                                .getTime()));
                    } else if (value instanceof Boolean) {
                        pstmt.setBoolean(indiceQuery, ((Boolean) value)
                                .booleanValue());
                    }
                    indiceQuery++;
                }
            }
            logger.info(strQuery);
            rs = pstmt.executeQuery();
            FaldoneView faldone;
            while (rs.next()) {
                faldone = new FaldoneView();
                faldone.setId(rs.getInt("faldone_id"));
                faldone.setNumeroFaldone(rs.getString("numero_faldone"));
                faldone.setOggetto(rs.getString("oggetto"));
                faldone.setCodiceLocale(rs.getString("codice_locale"));
                faldone.setUfficioId(rs.getInt("ufficio_id"));
                faldone.setTitolarioId(rs.getInt("titolario_id"));
                faldone.setDataCreazione(DateUtil.formattaData(rs.getDate(
                        "data_creazione").getTime()));
                faldoni.put(new Integer(faldone.getId()), faldone);
            }
        } catch (Exception e) {
            logger.error("Load getFaldoni", e);
            throw new DataException("Cannot load getFaldoni");
        } finally {
        	jdbcMan.closeAll(rs, pstmt, connection);
        }
        return faldoni;
    }

    public int contaFaldoni(Utente utente, String ufficiUtenti, HashMap<String, String> sqlDB)
            throws DataException {
        int numeroFaldoni = 0;
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        StringBuffer strQuery = new StringBuffer(
                "SELECT count (faldone_id) FROM faldoni f where aoo_id=? ");
        if (utente.getUfficioVOInUso().getTipo().equals(
                UfficioVO.UFFICIO_NORMALE)) {
            strQuery.append(" AND (ufficio_id IN (").append(ufficiUtenti)
                    .append(")");
            strQuery
                    .append(
                            " OR EXISTS (SELECT ufficio_id from storia_faldoni S WHERE "
                                    + " f.faldone_id =s.faldone_id  AND s.ufficio_id IN (")
                    .append(ufficiUtenti).append(")))");
        }

        if (sqlDB != null) {
            for (Iterator<Entry<String, String>> it = sqlDB.entrySet().iterator(); it.hasNext();) {
                Map.Entry<String, String> entry = it.next();
                Object key = entry.getKey();
                strQuery.append(" AND ").append(key.toString());
            }
        }

        int indiceQuery = 1;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(strQuery.toString());
            int aooId = utente.getUfficioVOInUso().getAooId();
            pstmt.setInt(indiceQuery, aooId);
            indiceQuery = indiceQuery + 1;
            if (sqlDB != null) {
                for (Iterator<Entry<String, String>> it = sqlDB.entrySet().iterator(); it.hasNext();) {
                    Map.Entry<String, String> entry = it.next();
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    if (value instanceof Integer) {
                        pstmt.setInt(indiceQuery, ((Integer) value).intValue());
                    } else if (value instanceof String) {
                        if (key.toString().indexOf("LIKE") > 0) {
                            pstmt
                                    .setString(indiceQuery, value.toString()
                                            + "%");
                        } else {
                            pstmt.setString(indiceQuery, value.toString());
                        }
                    } else if (value instanceof java.util.Date) {
                        java.util.Date d = (java.util.Date) value;
                        pstmt.setDate(indiceQuery, new java.sql.Date(d
                                .getTime()));
                    } else if (value instanceof Boolean) {
                        pstmt.setBoolean(indiceQuery, ((Boolean) value)
                                .booleanValue());
                    }
                    indiceQuery++;
                }
            }

            logger.info(strQuery);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                numeroFaldoni = rs.getInt(1);
            }
        } catch (Exception e) {
            logger.error("Load contaFaldoni", e);
            throw new DataException("Cannot load contaFaldoni");
        } finally {
            jdbcMan.closeAll(rs, pstmt, connection);
        }
        return numeroFaldoni;
    }

    public FaldoneVO newFaldone(Connection connection, FaldoneVO faldoneVO)
            throws DataException {

        PreparedStatement pstmt = null;

        try {
            if (connection == null) {
                logger.warn("newFaldone - Invalid Connection :" + connection);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            pstmt = connection.prepareStatement(INSERT_FALDONE);
            pstmt.setInt(1, faldoneVO.getId().intValue());
            pstmt.setInt(2, faldoneVO.getAooId());
            pstmt.setString(3, faldoneVO.getNumeroFaldone());
            pstmt.setString(4, faldoneVO.getOggetto());
            pstmt.setInt(5, faldoneVO.getUfficioId());
            pstmt.setDate(6, new Date(faldoneVO.getRowCreatedTime().getTime()));
            pstmt.setString(7, faldoneVO.getRowCreatedUser());
            pstmt.setString(8, faldoneVO.getCodiceLocale());
            pstmt.setString(9, faldoneVO.getSottoCategoria());
            pstmt.setString(10, faldoneVO.getNota());
            if (faldoneVO.getTitolarioId() > 0) {
                pstmt.setInt(11, faldoneVO.getTitolarioId());
            } else {
                pstmt.setNull(11, Types.INTEGER);
            }
            pstmt.setInt(12, faldoneVO.getAnno());
            pstmt.setInt(13, faldoneVO.getNumero());
            if (faldoneVO.getDataCreazione() != null)
                pstmt.setDate(14, new Date(faldoneVO.getDataCreazione()
                        .getTime()));
            else
                pstmt.setNull(14, Types.DATE);
            if (faldoneVO.getDataCarico() != null)
                pstmt
                        .setDate(15, new Date(faldoneVO.getDataCarico()
                                .getTime()));
            else
                pstmt.setNull(15, Types.DATE);
            if (faldoneVO.getDataScarico() != null)
                pstmt.setDate(16,
                        new Date(faldoneVO.getDataScarico().getTime()));
            else
                pstmt.setNull(16, Types.DATE);
            if (faldoneVO.getDataEvidenza() != null)
                pstmt.setDate(17, new Date(faldoneVO.getDataEvidenza()
                        .getTime()));
            else
                pstmt.setNull(17, Types.DATE);
            if (faldoneVO.getDataMovimento() != null)
                pstmt.setDate(18, new Date(faldoneVO.getDataMovimento()
                        .getTime()));
            else
                pstmt.setNull(18, Types.DATE);
            pstmt.setInt(19, faldoneVO.getPosizioneSelezionata());
            pstmt.setString(20, faldoneVO.getCollocazioneLabel1());
            pstmt.setString(21, faldoneVO.getCollocazioneLabel2());
            pstmt.setString(22, faldoneVO.getCollocazioneLabel3());
            pstmt.setString(23, faldoneVO.getCollocazioneLabel4());
            pstmt.setString(24, faldoneVO.getCollocazioneValore1());
            pstmt.setString(25, faldoneVO.getCollocazioneValore2());
            pstmt.setString(26, faldoneVO.getCollocazioneValore3());
            pstmt.setString(27, faldoneVO.getCollocazioneValore4());
            pstmt.setString(28, faldoneVO.getRowCreatedUser());
            pstmt.executeUpdate();
            faldoneVO.setReturnValue(ReturnValues.SAVED);
        } catch (Exception e) {
            logger.error("Save Faldone", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
        return faldoneVO;
    }

    public FaldoneVO updateFaldone(Connection connection, FaldoneVO faldoneVO)
            throws DataException {
        PreparedStatement pstmt = null;

        try {
            if (connection == null) {
                logger
                        .warn("updateFaldone - Invalid Connection :"
                                + connection);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            archiviaVersione(connection, faldoneVO.getId().intValue(),
                    faldoneVO.getVersione() + 1);
            faldoneVO.setDataScarico(null);
            pstmt = connection.prepareStatement(UPDATE_FALDONE);
            pstmt.setString(1, faldoneVO.getOggetto());
            pstmt.setInt(2, faldoneVO.getUfficioId());
            pstmt.setInt(3, faldoneVO.getTitolarioId());
            pstmt.setString(4, faldoneVO.getRowUpdatedUser());
            pstmt.setDate(5, new Date(System.currentTimeMillis()));
            pstmt.setString(6, faldoneVO.getCodiceLocale());
            pstmt.setString(7, faldoneVO.getSottoCategoria());
            pstmt.setString(8, faldoneVO.getNota());
            if (faldoneVO.getDataCarico() != null) {
                pstmt.setDate(9, new Date(faldoneVO.getDataCarico().getTime()));
            } else {
                pstmt.setNull(9, Types.DATE);
            }
            if (faldoneVO.getDataScarico() != null) {
                pstmt.setDate(10,
                        new Date(faldoneVO.getDataScarico().getTime()));
            } else {
                pstmt.setNull(10, Types.DATE);
            }
            if (faldoneVO.getDataEvidenza() != null) {
                pstmt.setDate(11, new Date(faldoneVO.getDataEvidenza()
                        .getTime()));
            } else {
                pstmt.setNull(11, Types.DATE);
            }
            if (faldoneVO.getDataMovimento() != null) {
                pstmt.setDate(12, new Date(faldoneVO.getDataMovimento()
                        .getTime()));
            } else {
                pstmt.setNull(12, Types.DATE);
            }
            pstmt.setInt(13, faldoneVO.getPosizioneSelezionata());
            pstmt.setString(14, faldoneVO.getCollocazioneLabel1());
            pstmt.setString(15, faldoneVO.getCollocazioneLabel2());
            pstmt.setString(16, faldoneVO.getCollocazioneLabel3());
            pstmt.setString(17, faldoneVO.getCollocazioneLabel4());
            pstmt.setString(18, faldoneVO.getCollocazioneValore1());
            pstmt.setString(19, faldoneVO.getCollocazioneValore2());
            pstmt.setString(20, faldoneVO.getCollocazioneValore3());
            pstmt.setString(21, faldoneVO.getCollocazioneValore4());
            pstmt.setInt(22, faldoneVO.getId().intValue());
            pstmt.executeUpdate();
            faldoneVO = getFaldone(connection, faldoneVO.getId().intValue());
            logger.info("Eseguito Update Faldone");
            faldoneVO.setReturnValue(ReturnValues.SAVED);
        } catch (Exception e) {
            logger.error("Update Faldone", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
        return faldoneVO;
    }

    public FaldoneVO getFaldone(Connection connection, int faldoneId)
            throws DataException {

        PreparedStatement pstmt = null;
        FaldoneVO vo = new FaldoneVO();
        ResultSet rs = null;
        try {
            if (connection == null) {
                logger.warn("getFaldone - Invalid Connection :" + connection);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }

            pstmt = connection.prepareStatement(SELECT_FALDONE);
            pstmt.setInt(1, faldoneId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                setCampiFaldone(vo, rs);
                vo.setReturnValue(ReturnValues.FOUND);
            } else
                vo.setReturnValue(ReturnValues.NOT_FOUND);

        } catch (Exception e) {
            logger.error("getFaldone", e);
            throw new DataException("getFaldone");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
        }
        return vo;
    }

    public FaldoneVO getFaldone(Connection connection, int anno, int numero)
            throws DataException {

        PreparedStatement pstmt = null;
        FaldoneVO vo = new FaldoneVO();
        ResultSet rs = null;
        try {
            if (connection == null) {
                logger.warn("getFaldone - Invalid Connection :" + connection);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }

            pstmt = connection.prepareStatement(SELECT_FALDONE_BY_ANNO_NUMERO);
            pstmt.setInt(1, anno);
            pstmt.setInt(2, numero);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                setCampiFaldone(vo, rs);
                vo.setReturnValue(ReturnValues.FOUND);
            } else
                vo.setReturnValue(ReturnValues.NOT_FOUND);

        } catch (Exception e) {
            logger.error("getFaldone", e);
            throw new DataException("getFaldone");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
        }
        return vo;
    }

    /*
    public IdentityVO getStatoFaldone(Connection connection, int statoId)
            throws DataException {

        PreparedStatement pstmt = null;
        IdentityVO vo = new IdentityVO();
        ResultSet rs = null;
        try {
            if (connection == null) {
                logger.warn("getStatoFaldone - Invalid Connection :"
                        + connection);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }

            pstmt = connection.prepareStatement(SELECT_STATO_FALDONE_BY_ID);
            pstmt.setInt(1, statoId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                vo.setId(rs.getInt("stato_faldone_id"));
                vo.setDescription(rs.getString("descrizione"));
                vo.setReturnValue(ReturnValues.FOUND);
            } else
                vo.setReturnValue(ReturnValues.NOT_FOUND);

        } catch (Exception e) {
            logger.error("getFaldone", e);
            throw new DataException("getFaldone");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
        }
        return vo;
    }
*/
    public void cancellaFaldoneFascicoli(Connection connection, int faldoneId)
            throws DataException {

        PreparedStatement pstmt = null;

        try {
            if (connection == null) {
                logger.warn("Invalid Connection :" + connection);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            pstmt = connection.prepareStatement(DELETE_FALDONE_FASCICOLI);
            pstmt.setInt(1, faldoneId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            logger.error("Elimina Faldone-Fascicoli", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
    }

    public int cancellaFaldone(int faldoneId) {
        PreparedStatement pstmt = null;
        Connection connection = null;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(CANCELLA_FALDONE);
            pstmt.setInt(1, faldoneId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            logger.error("Load getFaldoni", e);
            return ReturnValues.UNKNOWN;
        } finally {
            jdbcMan.close(pstmt);
            jdbcMan.close(connection);
        }
        return ReturnValues.VALID;
    }

    public void cancellaFaldoneProcedimenti(Connection connection, int faldoneId)
            throws DataException {

        PreparedStatement pstmt = null;

        try {
            if (connection == null) {
                logger.warn("Invalid Connection :" + connection);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            pstmt = connection.prepareStatement(DELETE_FALDONE_PROCEDIMENTI);
            pstmt.setInt(1, faldoneId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            logger.error("Elimina Faldone-Procedimenti", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
    }

    public void insertFaldoneFascicolo(Connection connection,
            FascicoloVO fascicolo, int faldoneId, String utente)
            throws DataException {
        PreparedStatement pstmt = null;
        try {
            if (connection == null) {
                logger.warn("salvaProtocolloFascicolo() - Invalid Connection :"
                        + connection);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }

            pstmt = connection.prepareStatement(INSERT_FALDONE_FASCICOLO);
            pstmt.setInt(1, faldoneId);
            pstmt.setInt(2, fascicolo.getId().intValue());
            pstmt.setInt(3, fascicolo.getVersione());
            pstmt.setString(4, utente);
            pstmt.setDate(5, new Date(System.currentTimeMillis()));
            pstmt.executeUpdate();
            logger.info("Inserito Faldone Fascicolo: FascicoloId "
                    + fascicolo.getId().intValue() + ", FaldoneId " + faldoneId
                    + ".");

        } catch (Exception e) {
            logger.error("salvaProtocolloFascicolo", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
    }

    public void insertFaldoneFascicolo(Connection connection,
            FaldoneFascicoloVO faldonefascicoloVO, int versioneFascicolo)
            throws DataException {
        PreparedStatement pstmt = null;
        try {
            if (connection == null) {
                logger.warn("salvaProtocolloFascicolo() - Invalid Connection :"
                        + connection);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            pstmt = connection.prepareStatement(INSERT_FALDONE_FASCICOLO);
            pstmt.setInt(1, faldonefascicoloVO.getFaldoneId());
            pstmt.setInt(2, faldonefascicoloVO.getFascicoloId());
            pstmt.setInt(3, versioneFascicolo);
            pstmt.setString(4, null);
            pstmt.setDate(5, new Date(System.currentTimeMillis()));
            pstmt.executeUpdate();
            logger.info("Inserito Faldone Fascicolo: FascicoloId "
                    + faldonefascicoloVO.getFascicoloId() + ", FaldoneId "
                    + faldonefascicoloVO.getFaldoneId() + ".");

        } catch (Exception e) {
            logger.error("salvaFaldoneFascicolo", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
    }

    public void insertFaldoneProcedimento(Connection connection,
            int procedimentoId, int faldoneId, String utente)
            throws DataException {
        PreparedStatement pstmt = null;
        try {
            if (connection == null) {
                logger.warn("salvaFaldoneProcedimento() - Invalid Connection :"
                        + connection);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            pstmt = connection.prepareStatement(INSERT_FALDONE_PROCEDIMENTO);
            pstmt.setInt(1, procedimentoId);
            pstmt.setInt(2, faldoneId);
            pstmt.executeUpdate();
            logger.info("Inserito Faldone Procedimento: ProcedimentoId "
                    + procedimentoId + ", FaldoneId " + faldoneId + ".");

        } catch (Exception e) {
            logger.error("salvaFaldoneProcedimento", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
    }

    private void archiviaVersione(Connection connection, int faldoneId,
            int versione) throws DataException {
        PreparedStatement pstmt = null;
        try {
            if (connection == null) {
                logger
                        .warn("storia Faldoni- Invalid Connection :"
                                + connection);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }

            String sql = "INSERT INTO storia_faldoni SELECT * FROM  faldoni"
                    + " WHERE faldone_id = ?";

            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, faldoneId);
            int r = pstmt.executeUpdate();
            logger.info(sql + "record inseriti in storia_faldoni: " + r);
            //jdbcMan.close(pstmt);

            // aggiorno data scarico
            sql = "UPDATE storia_faldoni SET data_scarico = ? WHERE faldone_id = ? and versione=?";
            pstmt = connection.prepareStatement(sql);
            pstmt.setDate(1, new Date(System.currentTimeMillis()));
            pstmt.setInt(2, faldoneId);
            pstmt.setInt(3, versione - 1);
            logger.info(sql);
            pstmt.executeUpdate();

            sql = "UPDATE faldoni SET versione = ? WHERE faldone_id = ?";
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, versione);
            pstmt.setInt(2, faldoneId);
            logger.info(sql);
            pstmt.executeUpdate();
           // jdbcMan.close(pstmt);

        } catch (Exception e) {
            logger.error("storia faldoni" + faldoneId, e);
            throw new DataException("Cannot insert Storia Faldoni");

        } finally {
            jdbcMan.close(pstmt);
        }
    }

    private void setCampiFaldone(FaldoneVO f, ResultSet rs) throws SQLException {
        f.setId(rs.getInt("faldone_id"));
        f.setAooId(rs.getInt("aoo_id"));
        f.setOggetto(rs.getString("oggetto"));
        f.setUfficioId(rs.getInt("ufficio_id"));
        f.setTitolarioId(rs.getInt("titolario_id"));
        f.setRowCreatedTime(rs.getTimestamp("row_created_time"));
        f.setRowCreatedUser(rs.getString("row_created_user"));
        f.setRowUpdatedUser(rs.getString("row_updated_user"));
        f.setRowUpdatedTime(rs.getTimestamp("row_updated_time"));
        f.setCodiceLocale(rs.getString("codice_locale"));
        f.setSottoCategoria(rs.getString("sotto_categoria"));
        f.setNota(rs.getString("nota"));
        f.setNumeroFaldone(rs.getString("numero_faldone"));
        f.setAnno(rs.getInt("anno"));
        f.setNumero(rs.getInt("numero"));
        f.setCollocazioneLabel1(rs.getString("collocazione_label1"));
        f.setCollocazioneLabel2(rs.getString("collocazione_label2"));
        f.setCollocazioneLabel3(rs.getString("collocazione_label3"));
        f.setCollocazioneLabel4(rs.getString("collocazione_label4"));
        f.setCollocazioneValore1(rs.getString("collocazione_valore1"));
        f.setCollocazioneValore2(rs.getString("collocazione_valore2"));
        f.setCollocazioneValore3(rs.getString("collocazione_valore3"));
        f.setCollocazioneValore4(rs.getString("collocazione_valore4"));
        f.setDataCreazione(rs.getDate("data_creazione"));
        f.setDataCarico(rs.getDate("data_carico"));
        f.setDataScarico(rs.getDate("data_scarico"));
        f.setDataEvidenza(rs.getDate("data_evidenza"));
        f.setDataMovimento(rs.getDate("data_movimento"));
        f.setPosizioneSelezionata(rs.getInt("stato_id"));
        f.setStato(String.valueOf(rs.getInt("stato_id")));
        f.setVersione(rs.getInt("versione"));
    }

    public int getMaxNumFaldone(Connection connection, int aooId, int anno)
            throws DataException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            if (connection == null) {
                logger.warn("getMaxNumProtocollo() - Invalid Connection :"
                        + connection);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            pstmt = connection.prepareStatement(SELECT_ULTIMO_FALDONE);
            pstmt.setInt(1, aooId);
            pstmt.setInt(2, anno);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) + 1;
            } else
                return 1;
        } catch (Exception e) {
            logger.error("getMaxNumFaldone", e);
            throw new DataException("Cannot load getMaxNumFaldone");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
        }
    }

    public int getUltimoFaldone(int anno, int registro) throws DataException {
        int ultimoFaldone = 0;
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(SELECT_ULTIMO_FALDONE);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                ultimoFaldone = rs.getInt("NUME_FALDONE");
            }
        } catch (Exception e) {
            logger.error("Load Ultimo faldone", e);
            throw new DataException("Cannot load Ultimo faldone");
        } finally {
        	jdbcMan.closeAll(rs, pstmt, connection);
        }
        return ultimoFaldone;
    }

}