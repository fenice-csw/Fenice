package it.finsiel.siged.dao.jdbc;

import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.integration.TitoliDestinatarioDAO;
import it.finsiel.siged.mvc.vo.IdentityVO;
import it.finsiel.siged.mvc.vo.lookup.TitolarioVO;
import it.finsiel.siged.rdb.JDBCManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

public class TitoliDestinatarioDAOjdbc implements TitoliDestinatarioDAO {
    static Logger logger = Logger.getLogger(TitoliDestinatarioDAOjdbc.class
            .getName());

    private JDBCManager jdbcMan = new JDBCManager();

    public Collection<IdentityVO> getElencoTitoliDestinatario() throws DataException {

        Collection<IdentityVO> listaTitoli = new ArrayList<IdentityVO>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(SELECT_TITOLIDESTINATARI);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                IdentityVO titoloVO = new IdentityVO();
                titoloVO.setId(rs.getInt("id"));
                titoloVO.setDescription(rs.getString("descrizione"));
                listaTitoli.add(titoloVO);
            }
        } catch (Exception e) {
            logger.error("Load getElencoTitoliDestinatari", e);
            throw new DataException("Cannot load getElencoTitoliDestinatari");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
            jdbcMan.close(connection);
        }
        return listaTitoli;

    }

    public IdentityVO getTitoloDestinatario(int id) throws DataException {

        IdentityVO titoloVO = new IdentityVO();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = jdbcMan.getConnection();
            pstmt = conn.prepareStatement(GET_TITOLO);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                titoloVO.setId(rs.getInt("id"));
                titoloVO.setDescription(rs.getString("descrizione"));
            }
        } catch (Exception e) {
            logger.error("Load getTitoloDestinatario", e);
            throw new DataException("Cannot load getTitoloDestinatario");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
            jdbcMan.close(conn);
        }

        return titoloVO;

    }

    public IdentityVO getTitoloDestinatarioDaTitolo(String titolo)
            throws DataException {

        IdentityVO titoloVO = new IdentityVO();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = jdbcMan.getConnection();
            pstmt = conn.prepareStatement(GET_TITOLO_DA_TITOLO);
            pstmt.setString(1, titolo);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                titoloVO.setId(rs.getInt("id"));
                titoloVO.setDescription(rs.getString("descrizione"));
            }
        } catch (Exception e) {
            logger.error("Load getTitoloDestinatarioDaTitolo", e);
            throw new DataException("Cannot load getTitoloDestinatarioDaTitolo");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
            jdbcMan.close(conn);
        }

        return titoloVO;

    }

    public IdentityVO newTitoloDestinatario(Connection conn, IdentityVO titoloVO)
            throws DataException {
        PreparedStatement pstmt = null;
        try {
            if (conn == null) {
                logger.warn("newTitolo() - Invalid Connection :" + conn);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            if (!esisteTitolo(titoloVO.getDescription())) {
                pstmt = conn.prepareStatement(INSERT_TITOLO);
                pstmt.setInt(1, titoloVO.getId().intValue());
                pstmt.setString(2, titoloVO.getDescription());
                pstmt.executeUpdate();
                titoloVO.setReturnValue(ReturnValues.SAVED);
            } else {
                titoloVO.setReturnValue(ReturnValues.FOUND);
            }

        } catch (Exception e) {
            logger.error("Save Titolo", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
        return titoloVO;
    }

    public boolean esisteTitolo(String titolo) throws DataException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean esiste = false;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(SELECT_TITOLI_BY_DESC);
            pstmt.setString(1, titolo.toUpperCase());

            rs = pstmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                esiste = true;
            }
        } catch (Exception e) {
            logger.error("esisteTitolo", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
            jdbcMan.close(connection);
        }
        return esiste;
    }

    public IdentityVO updateTitolo(Connection conn, IdentityVO titoloVO)
            throws DataException {
        PreparedStatement pstmt = null;
        titoloVO.setReturnValue(ReturnValues.UNKNOWN);
        try {
            if (conn == null) {
                logger.warn("aggiornaMezzoSpedizione() - Invalid Connection :"
                        + conn);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            if (!esisteTitolo(titoloVO.getDescription())) {
                pstmt = conn.prepareStatement(UPDATE_TITOLO);
                pstmt.setString(1, titoloVO.getDescription());
                pstmt.setInt(2, titoloVO.getId().intValue());
                pstmt.executeUpdate();
                titoloVO.setReturnValue(ReturnValues.SAVED);
            } else {
                titoloVO.setReturnValue(ReturnValues.FOUND);
            }
        } catch (Exception e) {
            logger.error("Update Titolo", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
        return titoloVO;

    }

    public void deleteTitolo(int id) throws DataException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            connection = jdbcMan.getConnection();
            if (connection == null) {
                logger.warn("deleteTitolo() - Invalid Connection :"
                        + connection);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }

            pstmt = connection.prepareStatement(DELETE_TITOLO);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            logger.error("Delete Titolo", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }

    }

    private TitolarioVO getTitolario(ResultSet rsTitolario) throws SQLException {
        TitolarioVO titolario = new TitolarioVO();
        titolario.setId(rsTitolario.getInt("titolario_id"));
        titolario.setAooId(rsTitolario.getInt("aoo_id"));
        titolario.setCodice(rsTitolario.getString("codi_titolario"));
        titolario.setDescrizione(rsTitolario.getString("desc_titolario"));
        titolario.setPath(rsTitolario.getString("path_titolario"));
        titolario.setParentId(rsTitolario.getInt("parent_id"));
        return titolario;
    }

    public Collection<TitolarioVO> getListaTitolario(int aoo, String codice,
            String descrizione) throws DataException {
        Collection<TitolarioVO> listaTitolario = new ArrayList<TitolarioVO>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(SELECT_TITOLARIO);
            pstmt.setInt(1, aoo);
            pstmt.setString(2, "%" + codice + "%");
            pstmt.setString(3, "%" + descrizione + "%");

            rs = pstmt.executeQuery();
            while (rs.next()) {
                listaTitolario.add(getTitolario(rs));
            }
        } catch (Exception e) {
            logger.error("Load Province", e);
            throw new DataException("Cannot load Province");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
            jdbcMan.close(connection);
        }
        return listaTitolario;
    }

    private TitolarioVO getTitolario(Connection connection, int ufficioId,
            int titolarioId) throws DataException {
        TitolarioVO titolario = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(SELECT_TITOLARIO_BY_ID_UFF);
            pstmt.setInt(1, ufficioId);
            pstmt.setInt(2, titolarioId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                titolario = getTitolario(rs);
            }
        } catch (Exception e) {
            logger.error("Load Titolario", e);
            throw new DataException("Cannot load Titolario");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
        }
        return titolario;
    }

    public TitolarioVO getTitolario(int ufficioId, int titolarioId)
            throws DataException {
        TitolarioVO titolario = null;
        Connection connection = null;
        try {
            connection = jdbcMan.getConnection();
            titolario = getTitolario(connection, ufficioId, titolarioId);
        } catch (Exception e) {
            logger.error("Load Titolario", e);
            throw new DataException("Cannot load Titolario");
        } finally {
            jdbcMan.close(connection);
        }
        return titolario;
    }

    public String[] getUfficiTitolario(int titolarioId) throws DataException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String[] ar;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(SELECT_UFFICI_ARGOMENTO);

            pstmt.setInt(1, titolarioId);
            rs = pstmt.executeQuery();
            Collection<String> tit = new ArrayList<String>();
            while (rs.next()) {
                tit.add(rs.getString("ufficio_id"));
            }
            ar = new String[tit.size()];
            tit.toArray(ar);

        } catch (Exception e) {
            logger.error("Load getUfficiTitolario", e);
            throw new DataException("Cannot load getUfficiTitolario");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
            jdbcMan.close(connection);
        }
        return ar;
    }

    public TitolarioVO getTitolario(int titolarioId) throws DataException {
        TitolarioVO titolario = null;
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(SELECT_TITOLARIO_BY_ID);

            pstmt.setInt(1, titolarioId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                titolario = getTitolario(rs);
            }

        } catch (Exception e) {
            logger.error("Load getTitolarioById", e);
            throw new DataException("Cannot load getTitolarioById");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
            jdbcMan.close(connection);
        }
        return titolario;
    }

    public Collection<TitolarioVO> getTitolariByParent(int ufficioId, int parentId)
            throws DataException {
        Collection<TitolarioVO> titolari = new ArrayList<TitolarioVO>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = jdbcMan.getConnection();
            if (parentId == 0) {
                pstmt = connection.prepareStatement(SELECT_TITOLARI_ROOT_UFF);
            } else {
                pstmt = connection
                        .prepareStatement(SELECT_TITOLARI_BY_PARENT_UFF);
                pstmt.setInt(2, parentId);
            }
            pstmt.setInt(1, ufficioId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                titolari.add(getTitolario(rs));
            }
        } catch (Exception e) {
            logger.error("Load Titolario", e);
            throw new DataException("Cannot load Titolario");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
            jdbcMan.close(connection);
        }
        return titolari;
    }

    public Collection<TitolarioVO> getTitolariByParent(int parentId) throws DataException {
        Collection<TitolarioVO> titolari = new ArrayList<TitolarioVO>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = jdbcMan.getConnection();
            if (parentId == 0) {
                pstmt = connection.prepareStatement(SELECT_TITOLARI_ROOT);
            } else {
                pstmt = connection.prepareStatement(SELECT_TITOLARI_BY_PARENT);
                pstmt.setInt(1, parentId);
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                titolari.add(getTitolario(rs));
            }
        } catch (Exception e) {
            logger.error("Load getTitolariParent", e);
            throw new DataException("Cannot load getTitolariParent");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
            jdbcMan.close(connection);
        }
        return titolari;
    }

    public void associaArgomentoUffici(Connection conn,
            TitolarioVO titolarioVO, String[] uffici) throws DataException {
        PreparedStatement pstmt = null;
        titolarioVO.setReturnValue(ReturnValues.UNKNOWN);
        try {
            if (conn == null) {
                logger.warn("newArgomento() - Invalid Connection :" + conn);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            if (uffici != null) {
                for (int i = 0; i < uffici.length; i++) {
                    if (uffici[i] != null) {
                        int ufficioId = new Integer(uffici[i]).intValue();
                        pstmt = conn.prepareStatement(INSERT_ARGOMENTO_UFFICIO);
                        pstmt.setInt(1, ufficioId);
                        pstmt.setInt(2, titolarioVO.getId().intValue());
                        pstmt.setString(3, titolarioVO.getRowCreatedUser());
                        pstmt.setString(4, titolarioVO.getRowUpdatedUser());
                        pstmt.executeUpdate();
                    }
                }
            }

            titolarioVO.setReturnValue(ReturnValues.SAVED);
        } catch (Exception e) {
            logger.error("Save Argomento Titolario", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
    }

    // private boolean getArgomentoUfficio(int titolarioId, int ufficioId)
    // throws DataException {
    // Connection connection = null;
    // PreparedStatement pstmt = null;
    // ResultSet rs = null;
    // boolean trovato = false;
    // try {
    // connection = jdbcMan.getConnection();
    // pstmt = connection.prepareStatement(SELECT_UFFICI_ARGOMENTO_BY_ID);
    // pstmt.setInt(1, titolarioId);
    // pstmt.setInt(2, ufficioId);
    // rs = pstmt.executeQuery();
    // rs.next();
    // if (rs.next()) {
    // trovato = true;
    // }
    // } catch (Exception e) {
    // logger.error("Load getArgomentoProtocolli", e);
    // throw new DataException("Cannot load getArgomentoProtocolli");
    // } finally {
    // jdbcMan.close(rs);
    // jdbcMan.close(pstmt);
    // jdbcMan.close(connection);
    // }
    // return trovato;
    // }

    public TitolarioVO newArgomento(Connection conn, TitolarioVO titolarioVO)
            throws DataException {
        PreparedStatement pstmt = null;
        titolarioVO.setReturnValue(ReturnValues.UNKNOWN);
        try {
            if (conn == null) {
                logger.warn("newArgomento() - Invalid Connection :" + conn);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            pstmt = conn.prepareStatement(INSERT_ARGOMENTO);
            pstmt.setInt(1, titolarioVO.getId().intValue());
            pstmt.setString(2, titolarioVO.getCodice());
            pstmt.setString(3, titolarioVO.getDescrizione());
            if (titolarioVO.getParentId() == 0) {
                pstmt.setNull(4, Types.INTEGER);
            } else {
                pstmt.setInt(4, titolarioVO.getParentId());
            }

            pstmt.setInt(5, titolarioVO.getAooId());
            pstmt.setString(6, titolarioVO.getPath());
            pstmt.setString(7, titolarioVO.getRowCreatedUser());
            pstmt.setString(8, titolarioVO.getRowUpdatedUser());
            pstmt.executeUpdate();

            titolarioVO.setReturnValue(ReturnValues.SAVED);
        } catch (Exception e) {
            logger.error("Save Argomento Titolario", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
        return titolarioVO;
    }

    public TitolarioVO updateArgomento(Connection conn, TitolarioVO titolarioVO)
            throws DataException {
        PreparedStatement pstmt = null;
        titolarioVO.setReturnValue(ReturnValues.UNKNOWN);
        try {
            if (conn == null) {
                logger.warn("updateArgomento() - Invalid Connection :" + conn);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            pstmt = conn.prepareStatement(UPDATE_ARGOMENTO);
            pstmt.setString(1, titolarioVO.getCodice());
            pstmt.setString(2, titolarioVO.getDescrizione());
            pstmt.setString(3, titolarioVO.getPath());
            pstmt.setString(4, titolarioVO.getRowUpdatedUser());
            pstmt.setInt(5, titolarioVO.getId().intValue());
            pstmt.executeUpdate();

            titolarioVO.setReturnValue(ReturnValues.SAVED);

        } catch (Exception e) {
            logger.error("Update Argomento Titolario", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
        return titolarioVO;
    }

    public boolean getArgomentoProtocolli(int titolarioId) throws DataException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean protocolliTrovati = false;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(SELECT_ARGOMENTO_PROTOCOLLI);
            pstmt.setInt(1, titolarioId);
            rs = pstmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                protocolliTrovati = true;
            }
        } catch (Exception e) {
            logger.error("Load getArgomentoProtocolli", e);
            throw new DataException("Cannot load getArgomentoProtocolli");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
            jdbcMan.close(connection);
        }
        return protocolliTrovati;
    }

    public void deleteArgomentoUffici(Connection conn, int titolarioId)
            throws DataException {
        PreparedStatement pstmt = null;
        try {
            if (conn == null) {
                logger.warn("deleteArgomentoUffici() - Invalid Connection :"
                        + conn);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            pstmt = conn.prepareStatement(DELETE_ARGOMENTO_UFFICIO);
            pstmt.setInt(1, titolarioId);
            pstmt.executeUpdate();

        } catch (Exception e) {
            logger.error("delete relazione Argomento Uffici Titolario", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
    }

    public void deleteArgomento(Connection conn, int titolarioId)
            throws DataException {
        PreparedStatement pstmt = null;
        try {
            if (conn == null) {
                logger.warn("deleteArgomento() - Invalid Connection :" + conn);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            pstmt = conn.prepareStatement(DELETE_ARGOMENTO_UFFICIO);
            pstmt.setInt(1, titolarioId);
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement(DELETE_ARGOMENTO);
            pstmt.setInt(1, titolarioId);
            pstmt.executeUpdate();

        } catch (Exception e) {
            logger.error("delete Argomento Titolario", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
    }

    // private final static String SELECT_UFFICI_ARGOMENTO_BY_ID = "SELECT *
    // FROM titolario$uffici WHERE titolario_id=? AND ufficio_id=?";

    private final static String SELECT_UFFICI_ARGOMENTO = "SELECT * FROM titolario$uffici WHERE titolario_id=?";

    private final static String SELECT_ARGOMENTO_PROTOCOLLI = "SELECT count(protocollo_id) FROM protocolli"
            + " WHERE titolario_id=?";

    private final static String DELETE_ARGOMENTO_UFFICIO = "DELETE FROM titolario$uffici WHERE titolario_id=?";

    private final static String DELETE_ARGOMENTO = "DELETE FROM titolario WHERE titolario_id=?";

    private final static String UPDATE_ARGOMENTO = "UPDATE titolario "
            + " SET codi_titolario=?, desc_titolario=?, path_titolario=?, row_updated_user=?"
            + " WHERE titolario_id=?";

    private final static String INSERT_ARGOMENTO_UFFICIO = "INSERT INTO titolario$uffici "
            + " (ufficio_id, titolario_id, row_created_user, row_updated_user) VALUES(?,?,?,?)";

    private final static String INSERT_ARGOMENTO = "INSERT INTO titolario "
            + " (titolario_id, codi_titolario, desc_titolario, parent_id, aoo_id, "
            + "path_titolario, row_created_user, row_updated_user) VALUES(?,?,?,?,?,?,?,?)";

    private final static String SELECT_TITOLARIO_BY_ID_UFF = "SELECT t.* FROM titolario t, titolario$uffici u"
            + " WHERE t.titolario_id = u.titolario_id AND"
            + " u.ufficio_id = ? AND t.titolario_id = ?";

    private final static String SELECT_TITOLARI_ROOT_UFF = "SELECT t.* FROM titolario t, titolario$uffici u"
            + " WHERE t.titolario_id = u.titolario_id AND"
            + " u.ufficio_id = ? AND t.parent_id IS NULL";

    private final static String SELECT_TITOLARI_BY_PARENT_UFF = "SELECT t.* FROM titolario t, titolario$uffici u"
            + " WHERE t.titolario_id = u.titolario_id AND"
            + " u.ufficio_id = ? AND t.parent_id = ?";

    private final static String SELECT_TITOLARIO_BY_ID = "SELECT * FROM titolario"
            + " WHERE titolario_id = ?";

    private final static String SELECT_TITOLARI_ROOT = "SELECT * FROM titolario"
            + " WHERE parent_id IS NULL";

    private final static String SELECT_TITOLARI_BY_PARENT = "SELECT * FROM titolario"
            + " WHERE parent_id = ?";

    // private final static String SELECT_TITOLARI_ROOT = "SELECT * FROM
    // titolario WHERE parent_id IS NULL";

    // private final static String SELECT_TITOLARI_BY_PARENT = "SELECT * FROM
    // titolario WHERE parent_id = ?";

    private final static String SELECT_TITOLARIO = "SELECT * FROM titolario"
            + " WHERE aoo_id = ? AND lower(codi_titolario) LIKE lower(?) AND"
            + " lower(desc_titolario) LIKE lower(?)" + " ORDER BY parent_id";

    private final static String SELECT_TITOLIDESTINATARI = "SELECT * FROM titoli_destinatari "
            + " ORDER BY descrizione";

    private final static String GET_TITOLO = "SELECT * FROM titoli_destinatari WHERE id=?";

    private final static String GET_TITOLO_DA_TITOLO = "SELECT * FROM titoli_destinatari WHERE descrizione=?";

    private final static String UPDATE_TITOLO = "UPDATE titoli_destinatari "
            + " SET descrizione=?" + " WHERE id=?";

    private final static String INSERT_TITOLO = "INSERT INTO titoli_destinatari "
            + " (id, descrizione) " + " VALUES(?,?)";

    private final static String SELECT_TITOLI_BY_DESC = "SELECT count(*) FROM titoli_destinatari where UPPER(descrizione)=?";

    private final static String DELETE_TITOLO = "DELETE FROM titoli_destinatari WHERE id=?";

};