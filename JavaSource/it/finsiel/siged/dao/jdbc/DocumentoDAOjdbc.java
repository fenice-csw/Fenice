package it.finsiel.siged.dao.jdbc;

import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.integration.DocumentoDAO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.rdb.JDBCManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

public class DocumentoDAOjdbc implements DocumentoDAO {

	static Logger logger = Logger.getLogger(DocumentoDAOjdbc.class.getName());

	private JDBCManager jdbcMan = new JDBCManager();

	public DocumentoVO newDocumentoVO(DocumentoVO documento, String path)
			throws DataException {
		DocumentoVO docOut;
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			docOut = newDocumentoVO(connection, documento, path);
			connection.commit();
		} catch (Exception e) {
			jdbcMan.rollback(connection);
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(connection);
		}
		return docOut;
	}

	public DocumentoVO newDocumentoVO(Connection connection, DocumentoVO documento) throws DataException {

		DocumentoVO newDocumentoVO = new DocumentoVO();
		newDocumentoVO.setReturnValue(ReturnValues.UNKNOWN);
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita ? invalida.");
			}
			pstmt = connection.prepareStatement(INSERT_DOCUMENTO);
			File in = new File(documento.getPath());
			FileInputStream fis = new FileInputStream(in);
			pstmt.setInt(1, documento.getId().intValue());
			pstmt.setString(2, documento.getDescrizione());
			pstmt.setString(3, documento.getContentType());
			pstmt.setString(4, documento.getImpronta());
			pstmt.setString(5, documento.getFileName());
			pstmt.setBinaryStream(6, fis, (int) in.length());
			pstmt.setInt(7, documento.getSize());
			pstmt.setString(8, documento.getRowCreatedUser());
			pstmt.setDate(9, new Date(System.currentTimeMillis()));
			pstmt.executeUpdate();
			fis.close();
			logger.debug("DocumentoVO inserito - id=" + documento.getId().intValue());
		} catch (Exception e) {
			logger.error("Save Documento", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		newDocumentoVO = getDocumento(connection, documento.getId().intValue());
		newDocumentoVO.setPath(documento.getPath());
		newDocumentoVO.setReturnValue(ReturnValues.SAVED);
		return newDocumentoVO;
	}

	public DocumentoVO newDocumentoVO(Connection connection, DocumentoVO documento, String path) throws DataException {

		DocumentoVO newDocumentoVO = new DocumentoVO();
		newDocumentoVO.setReturnValue(ReturnValues.UNKNOWN);
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita ? invalida.");
			}
			pstmt = connection.prepareStatement(INSERT_DOCUMENTO_PATH);
			File in = new File(path);
			FileInputStream fis = new FileInputStream(in);
			pstmt.setInt(1, documento.getId().intValue());
			pstmt.setString(2, documento.getDescrizione());
			pstmt.setString(3, documento.getContentType());
			pstmt.setString(4, documento.getImpronta());
			if (isFileNameNotNull(documento.getFileName()))
				pstmt.setString(5, documento.getFileName());
			else
				pstmt.setString(5, "allegato.pdf");
			pstmt.setString(6, documento.getPath());
			// pstmt.setBinaryStream(6, fis, (int) in.length());
			pstmt.setInt(7, (int) in.length());
			pstmt.setString(8, documento.getRowCreatedUser());
			pstmt.setDate(9, new Date(System.currentTimeMillis()));
			pstmt.executeUpdate();
			fis.close();
			logger.debug("DocumentoVO inserito - id=" + documento.getId().intValue());
		} catch (Exception e) {
			logger.error("Save Documento", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		newDocumentoVO = getDocumento(connection, documento.getId().intValue());
		newDocumentoVO.setPath(documento.getPath());
		newDocumentoVO.setReturnValue(ReturnValues.SAVED);
		return newDocumentoVO;
	}

	private boolean isFileNameNotNull(String fileName) {
		if (fileName != null && !fileName.trim().equals("")
				&& !fileName.trim().equals("undefined"))
			return true;
		else
			return false;
	}

	public DocumentoVO getDocumento(int id) throws DataException {
		DocumentoVO docOut;
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			docOut = getDocumento(connection, id);
		} catch (Exception e) {
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(connection);
		}
		return docOut;
	}
	
	public DocumentoVO getDocumentoTabula(int pId) throws DataException {
		DocumentoVO doc = new DocumentoVO();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection
					.prepareStatement(SELECT_DOCUMENTO_TABULA_BY_PROTOCOLLO_ID);
			pstmt.setInt(1, pId);
			rs = pstmt.executeQuery();
			// if(rs.getI)
			if (rs.next()) {
				doc = new DocumentoVO();
				doc.setId(rs.getString("documento_id"));
				doc.setDescrizione(rs.getString("descrizione"));
				doc.setPath(rs.getString("path"));
				doc.setRowCreatedUser(rs.getString("row_created_user"));
				doc.setRowCreatedTime(rs.getDate("row_created_time"));
				doc.setContentType(rs.getString("content_type"));
				doc.setImpronta(rs.getString("impronta"));
				doc.setFileName(rs.getString("filename"));
				doc.setSize(rs.getInt("file_size"));
				doc.setReturnValue(ReturnValues.FOUND);

				logger.debug("Load DocumentoTabula" + doc);
			}
		} catch (Exception e) {
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return doc;
	}
	

	public DocumentoVO getDocumento(Connection connection, int id) throws DataException {

		DocumentoVO doc = new DocumentoVO();
		doc.setReturnValue(ReturnValues.UNKNOWN);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita ? invalida.");
			}
			pstmt = connection.prepareStatement(SELECT_DOCUMENTO_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				doc.setId(id);
				doc.setDescrizione(rs.getString("descrizione"));
				doc.setPath(rs.getString("path"));
				doc.setRowCreatedUser(rs.getString("row_created_user"));
				doc.setRowCreatedTime(rs.getDate("row_created_time"));
				doc.setContentType(rs.getString("content_type"));
				doc.setImpronta(rs.getString("impronta"));
				doc.setFileName(rs.getString("filename"));
				doc.setSize(rs.getInt("file_size"));
				doc.setReturnValue(ReturnValues.FOUND);
				logger.debug("Load Documento" + doc);
			} else {
				doc.setReturnValue(ReturnValues.NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("Load Documento", e);
			throw new DataException("Cannot load the Documento");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return doc;
	}

	/*
	 * Salva un documento in un file temporaneo 'destFile' utilizzando l'id del
	 * documento 'docId'. @return flag che indica l'esito dell'azione.
	 */
	public void writeDocumentToStream(int docId, OutputStream os) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_DOCUMENTO_BY_ID);
			pstmt.setInt(1, docId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				InputStream in = rs.getBinaryStream("data");
				byte[] buffer = new byte[1024];//1024
				int n = 0;
				while ((n = in.read(buffer)) != -1)
					os.write(buffer, 0, n);
				in.close();
			} else {
				throw new DataException("Documento non trovato.");
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
	}
	
	public InputStream writeDocumentToInputStream(int docId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		InputStream is = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_DOCUMENTO_BY_ID);
			pstmt.setInt(1, docId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				is = rs.getBinaryStream("data");
			} else {
				throw new DataException("Documento non trovato.");
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(connection);
		}
		return is;
	}
	
	public boolean isDataDocumentoNotNull(int docId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean isPresent = false;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(IS_DATA_DOCUMENTO_NOT_NULL);
			pstmt.setInt(1, docId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				isPresent = true;
			}
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return isPresent;
	}
	
	public DocumentoVO findDocumentoByInfo(String filename, String impronta, int fileSize) throws DataException {
		DocumentoVO doc = new DocumentoVO();
		doc.setReturnValue(ReturnValues.UNKNOWN);
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_DOCUMENTO_BY_INFO);
			pstmt.setString(1, filename);
			pstmt.setInt(2, fileSize);
			pstmt.setString(3, impronta);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				doc.setId(rs.getInt("documento_id"));
				doc.setDescrizione(rs.getString("descrizione"));
				doc.setPath(rs.getString("path"));
				doc.setRowCreatedUser(rs.getString("row_created_user"));
				doc.setRowCreatedTime(rs.getDate("row_created_time"));
				doc.setContentType(rs.getString("content_type"));
				doc.setImpronta(rs.getString("impronta"));
				doc.setFileName(rs.getString("filename"));
				doc.setSize(rs.getInt("file_size"));
				doc.setReturnValue(ReturnValues.FOUND);
				logger.debug("Load Documento" + doc);
			} else {
				doc.setReturnValue(ReturnValues.NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("Load Documento", e);
			throw new DataException("Cannot load the Documento");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return doc;

	}
	
	private final static String SELECT_DOCUMENTO_BY_ID = "SELECT * FROM documenti WHERE documento_id = ?";
	
	private final static String SELECT_DOCUMENTO_BY_INFO = "SELECT * FROM documenti WHERE filename like ? and file_size = ? and impronta = ? ";

	private final static String SELECT_DOCUMENTO_TABULA_BY_PROTOCOLLO_ID = "SELECT * FROM documenti_tabula WHERE protocollo_id = ? order by FILENAME ASC";

	private final static String IS_DATA_DOCUMENTO_NOT_NULL = "SELECT descrizione FROM documenti WHERE documento_id = ? AND NOT data IS NULL";

	private final static String INSERT_DOCUMENTO = "INSERT INTO DOCUMENTI "
			+ "(documento_id, DESCRIZIONE, CONTENT_TYPE, IMPRONTA,"
			+ " FILENAME, DATA, FILE_SIZE, ROW_CREATED_USER, ROW_CREATED_TIME)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private final static String INSERT_DOCUMENTO_PATH = "INSERT INTO DOCUMENTI "
			+ "(documento_id, DESCRIZIONE, CONTENT_TYPE, IMPRONTA,"
			+ " FILENAME, PATH, FILE_SIZE,ROW_CREATED_USER, ROW_CREATED_TIME)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

};