package it.finsiel.siged.dao.jdbc;

import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.integration.IdentificativiDAO;
import it.finsiel.siged.rdb.JDBCManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

public class IdentificativiDAOjdbc implements IdentificativiDAO {
    static Logger logger = Logger.getLogger(IdentificativiDAOjdbc.class
            .getName());

    private JDBCManager jdbcMan = new JDBCManager();

    private final String SELECT_ID = "SELECT id_corrente FROM identificativi WHERE nome_tabella = ?";

    private final String UPDATE_ID = "UPDATE identificativi SET id_corrente = ? WHERE nome_tabella = ? AND id_corrente = ?";

    private final String INSERT_ID = "INSERT INTO identificativi (nome_tabella, id_corrente) VALUES (?, ?)";

    public int insertNewId(Connection connection, String tableName, int id)
            throws DataException {
        if (tableName != null && id > 0) {
            tableName = tableName.toUpperCase();
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                if (connection == null) {
                    logger.warn("insertNewId() - Invalid Connection :"
                            + connection);
                    throw new DataException(
                            "Connessione alla base dati non valida.");
                }
                pstmt = connection.prepareStatement(INSERT_ID);
                pstmt.setString(1, tableName);
                pstmt.setInt(2, id);
                int n = pstmt.executeUpdate();
                if (n == 0) {
                    throw new DataException("error.database.cuncurrency");
                }
            } catch (Exception e) {
                logger.error("insertNewId:", e);
                throw new DataException("error.database.missing");
            } finally {
                jdbcMan.close(rs);
                jdbcMan.close(pstmt);
            }
        } else {
            throw new DataException("insertNewId: Identificativo non valido.");
        }
        return id;
    }

    public int updateId(Connection connection, String tableName, int newId,
            int oldId) throws DataException {
        if (tableName != null && newId > oldId && newId > 0) {
            tableName = tableName.toUpperCase();
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                if (connection == null) {
                    logger.warn("updateId() - Invalid Connection :"
                            + connection);
                    throw new DataException(
                            "Connessione alla base dati non valida.");
                }
                pstmt = connection.prepareStatement(UPDATE_ID);
                pstmt.setInt(1, newId);
                pstmt.setString(2, tableName);
                pstmt.setInt(3, oldId);
                int n = pstmt.executeUpdate();
                if (n == 0) {
                    throw new DataException("error.database.cuncurrency");
                }
            } catch (Exception e) {
                logger.error("updateId:", e);
                throw new DataException("error.database.missing");
            } finally {
                jdbcMan.close(rs);
                jdbcMan.close(pstmt);
            }
        } else {
            throw new DataException("updateId: Identificativo non valido.");
        }
        return newId;
    }

    public int getCurrentId(String tableName) throws DataException {
        int id = -1;
        if (tableName != null) {
            tableName = tableName.toUpperCase();
            Connection connection = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                connection = jdbcMan.getConnection();
                pstmt = connection.prepareStatement(SELECT_ID);
                pstmt.setString(1, tableName);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            } catch (Exception e) {
                logger.error("getCurrentId:", e);
                throw new DataException("error.database.missing");
            } finally {
            	jdbcMan.closeAll(rs, pstmt, connection);
            }
        }
        return id;
    }

    public int getNextId(Connection connection, String tableName)
            throws DataException {
        int id = -1;
         if (tableName != null) {
            tableName = tableName.toUpperCase();
            PreparedStatement pstmtSelect = null;
            PreparedStatement pstmtUpdate = null;
            ResultSet rs = null;
            try {
                if (connection == null) {
                    logger.warn("getNextID() - Invalid Connection :"
                            + connection);
                    throw new DataException(
                            "Connessione alla base dati non valida.");
                }
                pstmtSelect = connection.prepareStatement(SELECT_ID);
                pstmtSelect.setString(1, tableName);
                rs = pstmtSelect.executeQuery();
                if (rs.next()) {
                    id = rs.getInt(1);
                   updateId(connection, tableName, id + 1, id);
                    id++;
                } else {
                    id = 1;
                   insertNewId(connection, tableName, id);
                }
            } catch (Exception e) {
                logger.error("getNextID:", e);
                throw new DataException("error.database.missing");
            } finally {
                jdbcMan.close(rs);
                jdbcMan.close(pstmtSelect);
                jdbcMan.close(pstmtUpdate);
            }
        }
        return id;
    }
}