package it.compit.fenice.mvc.dao.jdbc;

import it.compit.fenice.mvc.bo.RepertorioBO;
import it.compit.fenice.mvc.integration.RepertorioDAO;
import it.compit.fenice.mvc.presentation.helper.DocumentoRepertorioView;
import it.compit.fenice.mvc.vo.repertori.DocumentoRepertorioVO;
import it.compit.fenice.mvc.vo.repertori.RepertorioVO;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.business.DocumentoDelegate;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.finsiel.siged.util.DateUtil;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hsqldb.Types;

public class RepertorioDAOjdbc implements RepertorioDAO {

	static Logger logger = Logger.getLogger(RepertorioDAOjdbc.class.getName());

	private JDBCManager jdbcMan = new JDBCManager();


	public final static String INSERT_ALLEGATI = "INSERT INTO allegati_doc_repertorio(dc_id, doc_repertorio_id, flag_riservato, tipo, flag_principale, flag_pubblicabile) VALUES (?, ?, ?, ?, ?, ?)";

	public final static String SELECT_ALLEGATI = "SELECT * FROM allegati_doc_repertorio WHERE doc_repertorio_id=?";

	private final static String INSERT_REPERTORI = "INSERT INTO repertori"
			+ " (repertorio_id, aoo_id, descrizione,ufficio_responsabile_id,flag_web) VALUES(?,?,?,?,?)";
	
	private final static String UPDATE_REPERTORI = "UPDATE repertori"
			+ " SET descrizione=?,ufficio_responsabile_id=?,flag_web=? WHERE repertorio_id=?";

	private final static String INSERT_DOCUMENTI_REPERTORI = "INSERT INTO documenti_repertori"
			+ " (doc_repertorio_id, rep_id, note, oggetto, capitolo, importo, data_validita_inizio, "
			+ "data_validita_fine, row_created_user, ufficio_id, data_creazione,numero_documento_repertorio,numero_documento,descrizione,settore_proponente,protocollo_id) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	private final static String UPDATE_DOCUMENTI_REPERTORI = "UPDATE documenti_repertori "
			+ " SET note=?, oggetto=?, capitolo=?, importo=?, data_validita_inizio=?, "
			+ " data_validita_fine =?, row_updated_user=?,ufficio_id=?,data_creazione=?,numero_documento_repertorio=?,flag_stato=?,numero_documento=?,descrizione=?,settore_proponente=?, protocollo_id=? WHERE doc_repertorio_id=?";

	private final static String SELECT_DOC_REPERTORIO_BY_ID = "SELECT * FROM documenti_repertori"
			+ " WHERE doc_repertorio_id = ?";

	private static final String SELECT_DOCUMENTI_REPERTORI = "SELECT *,date_part('year',data_creazione) AS anno FROM documenti_repertori WHERE rep_id=? and NOT flag_stato =1 ORDER BY anno,numero_documento_repertorio ASC";

	private static final String SELECT_DOCUMENTI_DA_REPERTORIALE = "SELECT * FROM documenti_repertori WHERE rep_id=? and flag_stato=1 ORDER BY data_creazione ASC";

	private static final String SELECT_DOCUMENTI_REPERTORI_SCADUTI = "SELECT doc_repertorio_id FROM documenti_repertori WHERE data_validita_fine < now()";

	private static final String SELECT_REPERTORI = "SELECT * FROM repertori WHERE aoo_id=? order by repertorio_id";
	
	private static final String SELECT_REPERTORI_BY_FLAG_WEB = "SELECT * FROM repertori WHERE aoo_id=? AND flag_web=? order by repertorio_id";
	
	private static final String COUNT_REPERTORI_BY_FLAG_WEB = "SELECT count(repertorio_id) FROM repertori WHERE aoo_id=? AND flag_web=? ";

	private static final String SELECT_REPERTORI_BY_UFFICIO_RESPONSABILE = "SELECT * FROM repertori WHERE ufficio_responsabile_id=? order by repertorio_id";

	private static final String SELECT_REPERTORIO = "SELECT * FROM repertori WHERE repertorio_id=?";
	
	private static final String SELECT_REPERTORIO_FLAG_WEB = "SELECT flag_web FROM repertori WHERE repertorio_id=?";


	private static final String IS_NUMERO_USED = "SELECT doc_repertorio_id FROM documenti_repertori WHERE rep_id=? AND numero_documento_repertorio=? AND date_part('year',data_creazione)=? AND NOT doc_repertorio_id=?";

	private static final String GET_MAX_NUMERO_REPERTORIO = "SELECT max(numero_documento_repertorio) FROM documenti_repertori WHERE rep_id=? AND date_part('year',data_creazione)=? ";

	private static final String UPDATE_STATO = "UPDATE documenti_repertori SET flag_stato=? WHERE doc_repertorio_id=?";

	private final static String SELECT_DOCUMENTO_BY_ID = "SELECT data FROM documenti WHERE documento_id = ?";
	
	public Collection<Integer> getDocumentiRepertoriScaduti(Connection connection)
			throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Integer> ids = new ArrayList<Integer>();
		try {
			if (connection == null) {
				logger.warn("storia - Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection
					.prepareStatement(SELECT_DOCUMENTI_REPERTORI_SCADUTI);
			rs = pstmt.executeQuery();
			while (rs.next())
				ids.add(rs.getInt("doc_repertorio_id"));
		} catch (Exception e) {
			logger.error("getDocumentiRepertoriScaduti", e);
			throw new DataException("Cannot get DocumentiRepertori Scaduti");
		} finally {
			jdbcMan.close(pstmt);
		}
		return ids;
	}

	public void archiviaDocumentoRepertorio(Connection connection,
			int docRepertorioId) throws DataException {
		String[] tables = { "documenti_repertori", "allegati_doc_repertorio" };
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("storia - Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			for (int i = 0; i < tables.length; i++) {
				String sql = "INSERT INTO storia_" + tables[i]
						+ " SELECT * FROM " + tables[i]
						+ " WHERE doc_repertorio_id = ?";
				pstmt = connection.prepareStatement(sql);
				pstmt.setInt(1, docRepertorioId);
				pstmt.executeUpdate();
				jdbcMan.close(pstmt);
			}
			for (int i = 1; i >= 0; i--) {
				String del = "DELETE FROM " + tables[i]
						+ " WHERE doc_repertorio_id = ?";
				pstmt = connection.prepareStatement(del);
				pstmt.setInt(1, docRepertorioId);
				pstmt.executeUpdate();
				jdbcMan.close(pstmt);
			}
		} catch (Exception e) {
			logger.error("archiviaDocumentoRepertorio" + docRepertorioId, e);
			throw new DataException("Cannot insert Storia Protocollo");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public Collection<DocumentoRepertorioView> getDocumentiRepertorio(int repertorioId)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DocumentoRepertorioView c = null;
		ArrayList<DocumentoRepertorioView> repertori = new ArrayList<DocumentoRepertorioView>();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_DOCUMENTI_REPERTORI);
			pstmt.setInt(1, repertorioId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				c = new DocumentoRepertorioView();
				c.setDocRepertorioId(rs.getInt("doc_repertorio_id"));
				c.setDataValiditaInizio(rs.getDate("data_validita_inizio"));
				c.setDataValiditaFine(rs.getDate("data_validita_fine"));
				c.setDataDocumento(rs.getDate("data_creazione"));
				c.setUfficioId(rs.getInt("ufficio_id"));
				c.setOggetto(rs.getString("oggetto"));
				c.setCapitolo(rs.getString("capitolo"));
				c.setDescrizione(rs.getString("descrizione"));
				c.setNumeroDocumento(rs.getString("numero_documento"));
				c.setNumeroDocumentoRepertorio(rs.getInt("numero_documento_repertorio"));
				c.setImporto(rs.getBigDecimal("importo"));
				c.setNote(rs.getString("note"));
				c.setSettoreProponente(rs.getString("settore_proponente"));
				c.setStato(rs.getInt("flag_stato"));
				if(c.getStato()==DocumentoRepertorioVO.PUBBLICATO){
					c.setPubblicato(true);
				}else if(c.getStato()==DocumentoRepertorioVO.PROTOCOLLATO){
					c.setProtocollato(true);
				}else if(c.getStato()==DocumentoRepertorioVO.PUBBLICATO_PROTOCOLLATO){
					c.setPubblicato(true);
					c.setProtocollato(true);
				}
				repertori.add(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getRepertori", e);
			throw new DataException("Cannot load getRepertori");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		Collections.reverse(repertori);
		return repertori;
	}

	public Collection<DocumentoRepertorioView> getDocumentiDaRepertoriale(int repertorioId)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DocumentoRepertorioView c = null;
		ArrayList<DocumentoRepertorioView> repertori = new ArrayList<DocumentoRepertorioView>();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection
					.prepareStatement(SELECT_DOCUMENTI_DA_REPERTORIALE);
			pstmt.setInt(1, repertorioId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				c = new DocumentoRepertorioView();
				c.setDocRepertorioId(rs.getInt("doc_repertorio_id"));
				c.setDataValiditaInizio(rs.getDate("data_validita_inizio"));
				c.setDataValiditaFine(rs.getDate("data_validita_fine"));
				c.setDataDocumento(rs.getDate("data_creazione"));
				c.setUfficioId(rs.getInt("ufficio_id"));
				c.setOggetto(rs.getString("oggetto"));
				c.setCapitolo(rs.getString("capitolo"));
				c.setNumeroDocumentoRepertorio(rs
						.getInt("numero_documento_repertorio"));
				c.setImporto(rs.getBigDecimal("importo"));
				c.setNote(rs.getString("note"));
				c.setDescrizione(rs.getString("descrizione"));
				c.setNumeroDocumento(rs.getString("numero_documento"));
				c.setSettoreProponente(rs.getString("settore_proponente"));
				repertori.add(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getRepertori", e);
			throw new DataException("Cannot load getRepertori");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		Collections.reverse(repertori);
		return repertori;
	}

	public Collection<RepertorioVO> getRepertori(int aooId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		RepertorioVO c = null;
		ArrayList<RepertorioVO> repertori = new ArrayList<RepertorioVO>();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_REPERTORI);
			pstmt.setInt(1, aooId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				c = new RepertorioVO();
				c.setRepertorioId(rs.getInt("repertorio_id"));
				c.setDescrizione(rs.getString("descrizione"));
				c.setResponsabileId(rs.getInt("ufficio_responsabile_id"));
				c.setFlagWeb(rs.getInt("flag_web"));
				repertori.add(c);
			}
		} catch (Exception e) {
			logger.error("getRepertori", e);
			e.printStackTrace();
			throw new DataException("Cannot load getRepertori");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return repertori;
	}

	public Collection<RepertorioVO> getRepertoriByFlagWeb(int aooId, int flagWeb) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		RepertorioVO c = null;
		ArrayList<RepertorioVO> repertori = new ArrayList<RepertorioVO>();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_REPERTORI_BY_FLAG_WEB);
			pstmt.setInt(1, aooId);
			pstmt.setInt(2, flagWeb);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				c = new RepertorioVO();
				c.setRepertorioId(rs.getInt("repertorio_id"));
				c.setDescrizione(rs.getString("descrizione"));
				c.setResponsabileId(rs.getInt("ufficio_responsabile_id"));
				c.setFlagWeb(rs.getInt("flag_web"));
				repertori.add(c);
			}
		} catch (Exception e) {
			logger.error("getRepertori", e);
			e.printStackTrace();
			throw new DataException("Cannot load getRepertori");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return repertori;
	}

	public int contaRepertoriByFlagWeb(int aooId, int flagWeb) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int countRepertori = 0;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(COUNT_REPERTORI_BY_FLAG_WEB);
			pstmt.setInt(1, aooId);
			pstmt.setInt(2, flagWeb);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				countRepertori=rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("getRepertori", e);
			e.printStackTrace();
			throw new DataException("Cannot load getRepertori");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return countRepertori;
	}
	
	public Collection<RepertorioVO> getRepertoriByUfficio(int ufficioId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		RepertorioVO c = null;
		ArrayList<RepertorioVO> repertori = new ArrayList<RepertorioVO>();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection
					.prepareStatement(SELECT_REPERTORI_BY_UFFICIO_RESPONSABILE);
			pstmt.setInt(1, ufficioId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				c = new RepertorioVO();
				c.setRepertorioId(rs.getInt("repertorio_id"));
				c.setDescrizione(rs.getString("descrizione"));
				c.setResponsabileId(rs.getInt("ufficio_responsabile_id"));
				c.setFlagWeb(rs.getInt("flag_web"));
				repertori.add(c);
			}
		} catch (Exception e) {
			logger.error("getRepertori", e);
			e.printStackTrace();
			throw new DataException("Cannot load getRepertori");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return repertori;
	}

	public void deleteAllegati(Connection connection, int repertorioId)
			throws DataException {
		PreparedStatement pstmt = null;

		final String DELETE_DOC = "DELETE FROM allegati_doc_repertorio WHERE doc_repertorio_id = ?";
		try {
			pstmt = connection.prepareStatement(DELETE_DOC);
			pstmt.setInt(1, repertorioId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("deleteRiferimenti", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}

	}

	public DocumentoRepertorioVO newDocumentoRepertorio(Connection conn,
			DocumentoRepertorioVO vo) throws DataException {
		PreparedStatement pstmt = null;
		vo.setReturnValue(ReturnValues.UNKNOWN);
		try {
			if (conn == null) {
				logger.warn("newArgomento() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(INSERT_DOCUMENTI_REPERTORI);
			pstmt.setInt(1, vo.getDocRepertorioId());
			pstmt.setInt(2, vo.getRepId());
			pstmt.setString(3, vo.getNote());
			pstmt.setString(4, vo.getOggetto());
			pstmt.setString(5, vo.getCapitolo());
			pstmt.setBigDecimal(6, vo.getImporto());
			
			pstmt.setDate(7, vo.getDataValiditaInizio());
			pstmt.setDate(8, vo.getDataValiditaFine());
			pstmt.setString(9, vo.getRowCreatedUser());
			if (vo.getUfficioId() == 0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10, vo.getUfficioId());
			pstmt.setDate(11, vo.getDataRepertorio());
			pstmt.setInt(12, vo.getNumeroDocumentoRepertorio());
			pstmt.setString(13, vo.getNumeroDocumento());
			pstmt.setString(14, vo.getDescrizione());
			pstmt.setString(15, vo.getSettoreProponente());
			//pstmt.setInt(16, vo.getProtocolloId());			
			if(vo.getProtocolloId()!=0){
				pstmt.setInt(16, vo.getProtocolloId());
			}else{
				pstmt.setNull(16, Types.INTEGER);
			}
			pstmt.executeUpdate();
			vo.setReturnValue(ReturnValues.SAVED);
		} catch (Exception e) {
			logger.error("Save Argomento Titolario", e);
			e.printStackTrace();
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return vo;
	}

	public RepertorioVO newRepertorio(Connection conn, RepertorioVO vo)
			throws DataException {
		PreparedStatement pstmt = null;
		vo.setReturnValue(ReturnValues.UNKNOWN);
		try {
			if (conn == null) {
				logger.warn("newArgomento() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(INSERT_REPERTORI);
			pstmt.setInt(1, vo.getRepertorioId());
			pstmt.setInt(2, vo.getAooId());
			pstmt.setString(3, vo.getDescrizione());
			pstmt.setInt(4, vo.getResponsabileId());
			pstmt.setInt(5, vo.getFlagWeb());
			pstmt.executeUpdate();
			vo.setReturnValue(ReturnValues.SAVED);
		} catch (Exception e) {
			logger.error("Save Repertorio", e);
			e.printStackTrace();
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return vo;
	}

	public RepertorioVO updateRepertorio(Connection conn, RepertorioVO vo)
			throws DataException {
		PreparedStatement pstmt = null;
		vo.setReturnValue(ReturnValues.UNKNOWN);
		try {
			if (conn == null) {
				logger.warn("newArgomento() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(UPDATE_REPERTORI);
			pstmt.setString(1, vo.getDescrizione());
			pstmt.setInt(2, vo.getResponsabileId());
			pstmt.setInt(3, vo.getFlagWeb());
			pstmt.setInt(4, vo.getRepertorioId());
			pstmt.executeUpdate();
			vo.setReturnValue(ReturnValues.SAVED);
		} catch (Exception e) {
			logger.error("update Repertorio", e);
			e.printStackTrace();
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return vo;
	}
	
	public void salvaAllegati(Connection connection, int repertorioId,
			int documentoId, boolean riservato,int type, boolean principale, boolean pubblicabile) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("salvaRiferimenti() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(INSERT_ALLEGATI);
			pstmt.setInt(1, documentoId);
			pstmt.setInt(2, repertorioId);
			pstmt.setInt(3, riservato ? 1 : 0);
			pstmt.setInt(4, type);
			pstmt.setInt(5, principale ? 1 : 0);
			pstmt.setInt(6, pubblicabile ? 1 : 0);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("Save Allegato-Protocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}

	}

	public DocumentoRepertorioVO updateDocumentoRepertorio(Connection conn,
			DocumentoRepertorioVO vo) throws DataException {
		PreparedStatement pstmt = null;
		vo.setReturnValue(ReturnValues.UNKNOWN);
		try {
			if (conn == null) {
				logger.warn("updateArgomento() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(UPDATE_DOCUMENTI_REPERTORI);
			pstmt.setString(1, vo.getNote());
			pstmt.setString(2, vo.getOggetto());
			pstmt.setString(3, vo.getCapitolo());
			pstmt.setBigDecimal(4, vo.getImporto());
			pstmt.setDate(5, vo.getDataValiditaInizio());
			pstmt.setDate(6, vo.getDataValiditaFine());
			pstmt.setString(7, vo.getRowUpdatedUser());
			if (vo.getUfficioId() != 0)
				pstmt.setInt(8, vo.getUfficioId());
			else
				pstmt.setNull(8, Types.INTEGER);
			pstmt.setDate(9, vo.getDataRepertorio());
			pstmt.setInt(10, vo.getNumeroDocumentoRepertorio());
			pstmt.setInt(11, 0);
			pstmt.setString(12, vo.getNumeroDocumento());
			pstmt.setString(13, vo.getDescrizione());
			pstmt.setString(14, vo.getSettoreProponente());
			if(vo.getProtocolloId()!=0){
				pstmt.setInt(15, vo.getProtocolloId());
			}else{
				pstmt.setNull(15, Types.INTEGER);
			}
			pstmt.setInt(16, vo.getDocRepertorioId());
			pstmt.executeUpdate();
			vo = getDocumentoRepertorio(conn, vo.getDocRepertorioId());
			vo.setReturnValue(ReturnValues.SAVED);

		} catch (Exception e) {
			logger.error("Update Argomento Titolario", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return vo;
	}

	private DocumentoRepertorioVO getDocumentoRepertorio(Connection connection,
			int repertorioId) throws DataException {
		DocumentoRepertorioVO repertorio = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("getDocumentoRepertorio() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_DOC_REPERTORIO_BY_ID);
			pstmt.setInt(1, repertorioId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				repertorio = getDocumentoRepertorio(rs);
			}
		} catch (Exception e) {
			logger.error("Load getTitolarioById", e);
			throw new DataException("Cannot load getTitolarioById");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return repertorio;
	}

	private DocumentoRepertorioVO getDocumentoRepertorio(ResultSet rs)
			throws SQLException {
		DocumentoRepertorioVO repertorio = new DocumentoRepertorioVO();
		repertorio.setDocRepertorioId(rs.getInt("doc_repertorio_id"));
		repertorio.setRepId(rs.getInt("rep_id"));
		repertorio.setOggetto(rs.getString("oggetto"));
		repertorio.setCapitolo(rs.getString("capitolo"));
		repertorio.setImporto(rs.getBigDecimal("importo"));
		repertorio.setDescrizione(rs.getString("descrizione"));
		repertorio.setNumeroDocumento(rs.getString("numero_documento"));
		repertorio.setDataValiditaInizio(rs.getDate("data_validita_inizio"));
		repertorio.setDataValiditaFine(rs.getDate("data_validita_fine"));
		repertorio.setDataRepertorio(rs.getDate("data_creazione"));
		repertorio.setUfficioId(rs.getInt("ufficio_id"));
		repertorio.setNumeroDocumentoRepertorio(rs.getInt("numero_documento_repertorio"));
		repertorio.setNote(rs.getString("note"));
		repertorio.setSettoreProponente(rs.getString("settore_proponente"));
		repertorio.setProtocolloId(rs.getInt("protocollo_id"));
		repertorio.setFlagStato(rs.getInt("flag_stato"));
		return repertorio;
	}

	public RepertorioVO getRepertorio(int repertorioId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		RepertorioVO c = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_REPERTORIO);
			pstmt.setInt(1, repertorioId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				c = new RepertorioVO();
				c.setRepertorioId(rs.getInt("repertorio_id"));
				c.setDescrizione(rs.getString("descrizione"));
				c.setResponsabileId(rs.getInt("ufficio_responsabile_id"));
				c.setFlagWeb(rs.getInt("flag_web"));
			}
		} catch (Exception e) {
			logger.error("getRepertori", e);
			e.printStackTrace();
			throw new DataException("Cannot load getRepertori");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return c;
	}
	
	public boolean getRepertorioFlagWeb(int repertorioId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean web = false;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_REPERTORIO_FLAG_WEB);
			pstmt.setInt(1, repertorioId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				web=rs.getInt("flag_web")==1? true : false;
			}
		} catch (Exception e) {
			logger.error("getRepertori", e);
			e.printStackTrace();
			throw new DataException("Cannot load getRepertori");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return web;
	}

	public DocumentoRepertorioVO getDocumentoRepertorio(int docId)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DocumentoRepertorioVO c = null;
		try {
			connection = jdbcMan.getConnection();
			c = getDocumentoRepertorio(connection, docId);
			c.setDocumenti(getAllegati(connection, docId));
			
		} catch (Exception e) {
			logger.error("getRepertori", e);
			e.printStackTrace();
			throw new DataException("Cannot load getRepertori");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return c;
	}

	private Map<String, DocumentoVO> getAllegati(Connection connection, int docId)
			throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, DocumentoVO> docs = new HashMap<String, DocumentoVO>(2);
		DocumentoDelegate documentoDelegate = DocumentoDelegate.getInstance();
		try {
			if (connection == null) {
				logger
						.warn("getAllegati() - Invalid Connection :"
								+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_ALLEGATI);
			pstmt.setInt(1, docId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("dc_id");
				DocumentoVO doc = documentoDelegate
						.getDocumento(connection, id);
				doc.setRiservato(rs.getBoolean("flag_riservato"));
				doc.setPrincipale(rs.getBoolean("flag_principale"));
				doc.setPubblicabile(rs.getBoolean("flag_pubblicabile"));
				doc.setType(rs.getInt("tipo"));
				doc.setMustCreateNew(false);
				RepertorioBO.putAllegato(doc, docs);
			}
		} catch (Exception e) {
			logger.error("Load getTitolarioById", e);
			throw new DataException("Cannot load getTitolarioById");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return docs;
	}
	
	public InputStream getDocumentData(int docId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_DOCUMENTO_BY_ID);
			pstmt.setInt(1, docId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getBinaryStream(1);
			} else {
				throw new DataException("Documento non trovato.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
	}

	public boolean isNumeroDocumentoRepertorioUsed(DocumentoRepertorioVO vo)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(IS_NUMERO_USED);
			pstmt.setInt(1, vo.getRepId());
			pstmt.setInt(2, vo.getNumeroDocumentoRepertorio());
			pstmt.setInt(3, DateUtil.getYear(vo.getDataValiditaInizio()));
			pstmt.setInt(4, vo.getDocRepertorioId());
			rs = pstmt.executeQuery();
			if (rs.next())
				return true;
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
	}

	public int getMaxNumeroRepertorio(int repId, int annoCorrente)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(GET_MAX_NUMERO_REPERTORIO);
			pstmt.setInt(1, repId);
			pstmt.setInt(2, annoCorrente);
			rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getInt(1) + 1;
			else
				return 1;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
	}

	public boolean aggiornaStato(int docId, int stato, Connection connection)
			throws DataException {
		PreparedStatement pstmt = null;
		boolean saved = false;
		try {
			if (connection == null) {
				logger.warn("aggiornaStato() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(UPDATE_STATO);
			pstmt.setInt(1, stato);
			pstmt.setInt(2, docId);
			pstmt.executeUpdate();
			saved = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(pstmt);
		}
		return saved;
	}
}
