package it.finsiel.siged.dao.jdbc;

import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.integration.AnnotazioneDAO;
import it.finsiel.siged.mvc.vo.protocollo.AnnotazioneVO;
import it.finsiel.siged.rdb.JDBCManager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

public class AnnotazioneDAOjdbc implements AnnotazioneDAO {

    static Logger logger = Logger.getLogger(AnnotazioneDAOjdbc.class.getName());

    private JDBCManager jdbcMan = new JDBCManager();

    public AnnotazioneVO newAnnotazioneVO(int protocolloId,
            AnnotazioneVO annotazione) throws DataException {
        AnnotazioneVO annOut;
        Connection connection = null;
        try {
            connection = jdbcMan.getConnection();
            connection.setAutoCommit(false);
            annOut = newAnnotazioneVO(connection, protocolloId, annotazione);
            connection.commit();
        } catch (Exception e) {
            jdbcMan.rollback(connection);
            throw new DataException("??");
        } finally {
            jdbcMan.close(connection);
        }
        return annOut;
    }

    public AnnotazioneVO newAnnotazioneVO(AnnotazioneVO annotazione)
            throws DataException {
        AnnotazioneVO annOut;
        Connection connection = null;
        try {
            connection = jdbcMan.getConnection();
            connection.setAutoCommit(false);
            int idProtocollo = annotazione.getFkProtocollo();
            annOut = newAnnotazioneVO(connection, idProtocollo, annotazione);
            connection.commit();
        } catch (Exception e) {
            jdbcMan.rollback(connection);
            throw new DataException("??");
        }
        return annOut;
    }

    public AnnotazioneVO newAnnotazioneVO(Connection connection,
            int protocolloId, AnnotazioneVO annotazione) throws DataException {

        AnnotazioneVO newAnnotazioneVO = new AnnotazioneVO();
        newAnnotazioneVO.setReturnValue(ReturnValues.UNKNOWN);

        PreparedStatement pstmt = null;

        try {
            if (connection == null) {
                logger.warn("Connection is:" + connection);
                throw new DataException("La connessione fornita ? invalida.");
            }

            pstmt = connection.prepareStatement(INSERT_ANNOTAZIONE_PROTOCOLLO);

            pstmt.setInt(1, annotazione.getId().intValue());
            pstmt.setInt(2, protocolloId);
            pstmt.setInt(3, annotazione.getCodiceAnnotazione());
            pstmt.setString(4, annotazione.getDescrizione());
            pstmt.setInt(5, annotazione.getCodiceUserId());
            pstmt.setDate(6, new Date(annotazione.getDataAnnotazione()
                    .getTime()));
            pstmt.setString(7, annotazione.getRowCreatedUser());
            pstmt.setString(8, annotazione.getRowUpdatedUser());
            pstmt.setDate(9,
                    new Date(annotazione.getRowCreatedTime().getTime()));
            pstmt.setDate(10, new Date(annotazione.getRowUpdatedTime()
                    .getTime()));
            pstmt.setInt(11, annotazione.getVersione());

            pstmt.executeUpdate();
            logger.debug("DocumentoVO inserito - id="
                    + annotazione.getId().intValue());

        } catch (Exception e) {
            logger.error("Save Annotazione", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }

        newAnnotazioneVO = getAnnotazione(connection, annotazione.getId()
                .intValue());
        newAnnotazioneVO.setReturnValue(ReturnValues.SAVED);
        return newAnnotazioneVO;

    }

   

    public AnnotazioneVO updateAnnotazioneVO(Connection connection,
            AnnotazioneVO doc) throws DataException {
    	        return doc;
    }

    public AnnotazioneVO getAnnotazione(int id) throws DataException {
        AnnotazioneVO annOut;
        Connection connection = null;
        try {
            connection = jdbcMan.getConnection();
            annOut = getAnnotazione(connection, id);
        } catch (Exception e) {
            throw new DataException("??");
        } finally {
            jdbcMan.close(connection);
        }
        return annOut;
    }

    public AnnotazioneVO getAnnotazione(Connection connection, int id)
            throws DataException {

        AnnotazioneVO ann = new AnnotazioneVO();
        ann.setReturnValue(ReturnValues.UNKNOWN);

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(SELECT_DOCUMENTO_BY_ID);
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                // Imposta Campi Oggetto
                /*
                 * doc.setId(id);
                 * doc.setFkProtocollo(rs.getInt("protocollo_id"));
                 * doc.setDescrizione(rs.getString("descrizione"));
                 * doc.setPath(rs.getString("path"));
                 * doc.setRowCreatedUser(rs.getString("row_created_user"));
                 * doc.setRowUpdatedUser(rs.getString("row_updated_user"));
                 * doc.setRowCreatedTime(rs.getDate("row_created_time"));
                 * doc.setRowUpdatedTime(rs.getDate("row_updated_time"));
                 * doc.setVersione(rs.getInt("versione"));
                 * doc.setContentType(rs.getString("content_type"));
                 * 
                 * doc.setActionStatus(ActionStatus.FOUND);
                 */
                logger.debug("Load Documento" + ann);
            } else {
                ann.setReturnValue(ReturnValues.NOT_FOUND);
            }

        } catch (Exception e) {
            logger.error("Load Documento", e);
            throw new DataException("Cannot load the Documento");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
        }

        return ann;

    }

    public ArrayList<AnnotazioneVO> getArrayAnnotazioneVO(int idProtocollo)
            throws DataException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<AnnotazioneVO> arrayAnnotazioneVO = new ArrayList<AnnotazioneVO>();
        Connection connection = null;
        try {

            connection = jdbcMan.getConnection();
            pstmt = connection
                    .prepareStatement(SELECT_ANNOTAZIONI_BY_IDPROTOCOLLO);
            pstmt.setInt(1, idProtocollo);
            rs = pstmt.executeQuery();

            AnnotazioneVO annVO;

            while (rs.next()) {
                annVO = new AnnotazioneVO();
                annVO.setId(rs.getInt("ANNOTAZIONI_ID"));
                annVO.setFkProtocollo(rs.getInt("protocollo_id"));
                annVO.setCodiceAnnotazione(rs.getInt("CODI_ANNOTAZIONE"));
                annVO.setDescrizione(rs.getString("DESC_ANNOTAZIONE"));
                annVO.setDataAnnotazione(rs.getDate("DATA_ANNOTAZIONE"));
                annVO.setRowCreatedUser(rs.getString("ROW_CREATED_USER"));
                annVO.setRowUpdatedUser(rs.getString("ROW_UPDATED_USER"));
                annVO.setRowCreatedTime(rs.getDate("ROW_CREATED_TIME"));
                annVO.setRowUpdatedTime(rs.getDate("ROW_UPDATED_TIME"));
                annVO.setVersione(rs.getInt("VERSIONE"));
                annVO.setReturnValue(ReturnValues.FOUND);
                arrayAnnotazioneVO.add(annVO);
            }

            return arrayAnnotazioneVO;

        }
        catch (Exception e) {
            logger.error("", e);
            throw new DataException("Errore nella lettura delle annotazioni.");

        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
            jdbcMan.close(connection);
        }

    }

    public final static String INSERT_ANNOTAZIONE_PROTOCOLLO = "INSERT INTO ANNOTAZIONI "
            + "(annotazione_id, protocollo_id, codi_annotazione, DESC_ANNOTAZIONE, CODI_USERID, data_annotazione, ROW_CREATED_USER , ROW_UPDATED_USER, ROW_CREATED_TIME, ROW_UPDATED_TIME, VERSIONE)"
            + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

    public final static String SELECT_ANNOTAZIONI_BY_IDPROTOCOLLO = "SELECT * FROM protocollo_annotazioni where protocollo_id = ?";

    // Seleziona la versione dell'oggetto avente l'id che gli viene passato
    public final static String SELECT_VERSIONE = "SELECT VERSIONE FROM protocollo_annotazioni WHERE annotazioe_id ? ";

    public final static String SELECT_DOCUMENTO_BY_ID = " select documento_id, protocollo_id, descrizione, path, row_created_user, row_updated_user, row_created_time, row_updated_time, versione, content_type "
            + " from protocollo_documenti " + " where documento_id = ? ";

    /*
     * (non-Javadoc)
     * 
     * @see it.finsiel.siged.mvc.integration.AnnotazioneDAO#getAnnotazioni(int)
     */
    public Collection<AnnotazioneVO> getAnnotazioni(int idProtocollo) throws DataException {
        return null;
    }
};