package it.compit.fenice.mvc.dao.jdbc;

import it.compit.fenice.mvc.bo.AmmTrasparenteBO;
import it.compit.fenice.mvc.integration.AmmTrasparenteDAO;
import it.compit.fenice.mvc.presentation.helper.DocumentoAmmTrasparenteView;
import it.compit.fenice.mvc.vo.ammtrasparente.AmmTrasparenteVO;
import it.compit.fenice.mvc.vo.ammtrasparente.DocumentoAmmTrasparenteVO;
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

public class AmmTrasparenteDAOjdbc implements AmmTrasparenteDAO {

	static Logger logger = Logger.getLogger(AmmTrasparenteDAOjdbc.class.getName());

	private JDBCManager jdbcMan = new JDBCManager();


	public final static String INSERT_ALLEGATI = "INSERT INTO allegati_doc_amm_trasparente(dc_id, doc_sezione_id, flag_riservato, tipo, flag_principale, flag_pubblicabile) VALUES (?, ?, ?, ?, ?, ?)";

	public final static String SELECT_ALLEGATI = "SELECT * FROM allegati_doc_amm_trasparente WHERE doc_sezione_id=?";

	private final static String INSERT_SEZIONI = "INSERT INTO amm_trasparente"
			+ " (sezione_id, aoo_id, descrizione,ufficio_responsabile_id,flag_web) VALUES(?,?,?,?,?)";
	
	private final static String UPDATE_SEZIONI = "UPDATE amm_trasparente"
			+ " SET descrizione=?,ufficio_responsabile_id=?,flag_web=? WHERE sezione_id=?";

	private final static String INSERT_DOCUMENTI_SEZIONI = "INSERT INTO amm_trasparente_documenti"
			+ " (doc_sezione_id, sez_id, note, oggetto, capitolo, importo, data_validita_inizio, "
			+ "data_validita_fine, row_created_user, ufficio_id, data_creazione,numero_documento_sezione,numero_documento,descrizione,settore_proponente,protocollo_id) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	private final static String UPDATE_DOCUMENTI_SEZIONI = "UPDATE amm_trasparente_documenti "
			+ " SET note=?, oggetto=?, capitolo=?, importo=?, data_validita_inizio=?, "
			+ " data_validita_fine =?, row_updated_user=?,ufficio_id=?,data_creazione=?,numero_documento_sezione=?,flag_stato=?,numero_documento=?,descrizione=?,settore_proponente=?, protocollo_id=? WHERE doc_sezione_id=?";

	private final static String SELECT_DOC_SEZIONE_BY_ID = "SELECT * FROM amm_trasparente_documenti"
			+ " WHERE doc_sezione_id = ?";

	private static final String SELECT_DOCUMENTI_SEZIONI = "SELECT *,date_part('year',data_creazione) AS anno FROM amm_trasparente_documenti WHERE sez_id=? and NOT flag_stato =1 ORDER BY anno,numero_documento_sezione ASC";

	private static final String SELECT_DOCUMENTI_DA_SEZIONALE = "SELECT * FROM amm_trasparente_documenti WHERE sez_id=? and flag_stato=1 ORDER BY data_creazione ASC";

	private static final String SELECT_DOCUMENTI_SEZIONI_SCADUTI = "SELECT doc_sezione_id FROM amm_trasparente_documenti WHERE data_validita_fine < now()";

	private static final String SELECT_SEZIONI = "SELECT * FROM amm_trasparente WHERE aoo_id=? order by sezione_id";
	
	private static final String SELECT_SEZIONI_BY_FLAG_WEB = "SELECT * FROM amm_trasparente WHERE aoo_id=? AND flag_web=? order by sezione_id";
	
	private static final String COUNT_SEZIONI_BY_FLAG_WEB = "SELECT count(sezione_id) FROM amm_trasparente WHERE aoo_id=? AND flag_web=? ";

	private static final String SELECT_SEZIONI_BY_UFFICIO_RESPONSABILE = "SELECT * FROM amm_trasparente WHERE ufficio_responsabile_id=? order by sezione_id";

	private static final String SELECT_SEZIONE = "SELECT * FROM amm_trasparente WHERE sezione_id=?";
	
	private static final String SELECT_SEZIONE_FLAG_WEB = "SELECT flag_web FROM amm_trasparente WHERE sezione_id=?";


	private static final String IS_NUMERO_USED = "SELECT doc_sezione_id FROM amm_trasparente_documenti WHERE sez_id=? AND numero_documento_sezione=? AND date_part('year',data_creazione)=? AND NOT doc_sezione_id=?";

	private static final String GET_MAX_NUMERO_SEZIONE = "SELECT max(numero_documento_sezione) FROM amm_trasparente_documenti WHERE sez_id=? AND date_part('year',data_creazione)=? ";

	private static final String UPDATE_STATO = "UPDATE amm_trasparente_documenti SET flag_stato=? WHERE doc_sezione_id=?";

	private final static String SELECT_DOCUMENTO_BY_ID = "SELECT data FROM documenti WHERE documento_id = ?";
	
	public Collection<Integer> getDocumentiSezioneScaduti(Connection connection)
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
					.prepareStatement(SELECT_DOCUMENTI_SEZIONI_SCADUTI);
			rs = pstmt.executeQuery();
			while (rs.next())
				ids.add(rs.getInt("doc_sezione_id"));
		} catch (Exception e) {
			logger.error("getDocumentiSezioniScaduti", e);
			throw new DataException("Cannot get DocumentiSezioni Scaduti");
		} finally {
			jdbcMan.close(pstmt);
		}
		return ids;
	}

	public void archiviaDocumentoSezione(Connection connection,
			int docSezioneId) throws DataException {
		String[] tables = { "amm_trasparente_documenti", "allegati_doc_amm_trasparente" };
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
						+ " WHERE doc_sezione_id = ?";
				pstmt = connection.prepareStatement(sql);
				pstmt.setInt(1, docSezioneId);
				pstmt.executeUpdate();
				jdbcMan.close(pstmt);
			}
			for (int i = 1; i >= 0; i--) {
				String del = "DELETE FROM " + tables[i]
						+ " WHERE doc_sezione_id = ?";
				pstmt = connection.prepareStatement(del);
				pstmt.setInt(1, docSezioneId);
				pstmt.executeUpdate();
				jdbcMan.close(pstmt);
			}
		} catch (Exception e) {
			logger.error("archiviaDocumentoSezione" + docSezioneId, e);
			throw new DataException("Cannot insert Storia Protocollo");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public Collection<DocumentoAmmTrasparenteView> getDocumentiSezione(int sezioneId)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DocumentoAmmTrasparenteView c = null;
		ArrayList<DocumentoAmmTrasparenteView> sezione = new ArrayList<DocumentoAmmTrasparenteView>();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_DOCUMENTI_SEZIONI);
			pstmt.setInt(1, sezioneId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				c = new DocumentoAmmTrasparenteView();
				c.setDocSezioneId(rs.getInt("doc_sezione_id"));
				c.setDataValiditaInizio(rs.getDate("data_validita_inizio"));
				c.setDataValiditaFine(rs.getDate("data_validita_fine"));
				c.setDataDocumento(rs.getDate("data_creazione"));
				c.setUfficioId(rs.getInt("ufficio_id"));
				c.setOggetto(rs.getString("oggetto"));
				c.setCapitolo(rs.getString("capitolo"));
				c.setDescrizione(rs.getString("descrizione"));
				c.setNumeroDocumento(rs.getString("numero_documento"));
				c.setNumeroDocumentoSezione(rs.getInt("numero_documento_sezione"));
				c.setImporto(rs.getBigDecimal("importo"));
				c.setNote(rs.getString("note"));
				c.setSettoreProponente(rs.getString("settore_proponente"));
				c.setStato(rs.getInt("flag_stato"));
				if(c.getStato()==DocumentoAmmTrasparenteVO.PUBBLICATO){
					c.setPubblicato(true);
				}else if(c.getStato()==DocumentoAmmTrasparenteVO.PROTOCOLLATO){
					c.setProtocollato(true);
				}else if(c.getStato()==DocumentoAmmTrasparenteVO.PUBBLICATO_PROTOCOLLATO){
					c.setPubblicato(true);
					c.setProtocollato(true);
				}
				sezione.add(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getSezioni", e);
			throw new DataException("Cannot load getSezioni");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		Collections.reverse(sezione);
		return sezione;
	}

	public Collection<DocumentoAmmTrasparenteView> getDocumentiDaSezionale(int sezioneId)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DocumentoAmmTrasparenteView c = null;
		ArrayList<DocumentoAmmTrasparenteView> sezione = new ArrayList<DocumentoAmmTrasparenteView>();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection
					.prepareStatement(SELECT_DOCUMENTI_DA_SEZIONALE);
			pstmt.setInt(1, sezioneId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				c = new DocumentoAmmTrasparenteView();
				c.setDocSezioneId(rs.getInt("doc_sezione_id"));
				c.setDataValiditaInizio(rs.getDate("data_validita_inizio"));
				c.setDataValiditaFine(rs.getDate("data_validita_fine"));
				c.setDataDocumento(rs.getDate("data_creazione"));
				c.setUfficioId(rs.getInt("ufficio_id"));
				c.setOggetto(rs.getString("oggetto"));
				c.setCapitolo(rs.getString("capitolo"));
				c.setNumeroDocumentoSezione(rs
						.getInt("numero_documento_sezione"));
				c.setImporto(rs.getBigDecimal("importo"));
				c.setNote(rs.getString("note"));
				c.setDescrizione(rs.getString("descrizione"));
				c.setNumeroDocumento(rs.getString("numero_documento"));
				c.setSettoreProponente(rs.getString("settore_proponente"));
				sezione.add(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getSezioni", e);
			throw new DataException("Cannot load getSezioni");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		Collections.reverse(sezione);
		return sezione;
	}

	public Collection<AmmTrasparenteVO> getSezioni(int aooId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		AmmTrasparenteVO c = null;
		ArrayList<AmmTrasparenteVO> sezione = new ArrayList<AmmTrasparenteVO>();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_SEZIONI);
			pstmt.setInt(1, aooId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				c = new AmmTrasparenteVO();
				c.setSezioneId(rs.getInt("sezione_id"));
				c.setDescrizione(rs.getString("descrizione"));
				c.setResponsabileId(rs.getInt("ufficio_responsabile_id"));
				c.setFlagWeb(rs.getInt("flag_web"));
				sezione.add(c);
			}
		} catch (Exception e) {
			logger.error("getSezioni", e);
			e.printStackTrace();
			throw new DataException("Cannot load getSezioni");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return sezione;
	}

	public Collection<AmmTrasparenteVO> getSezioniByFlagWeb(int aooId, int flagWeb) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		AmmTrasparenteVO c = null;
		ArrayList<AmmTrasparenteVO> sezione = new ArrayList<AmmTrasparenteVO>();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_SEZIONI_BY_FLAG_WEB);
			pstmt.setInt(1, aooId);
			pstmt.setInt(2, flagWeb);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				c = new AmmTrasparenteVO();
				c.setSezioneId(rs.getInt("sezione_id"));
				c.setDescrizione(rs.getString("descrizione"));
				c.setResponsabileId(rs.getInt("ufficio_responsabile_id"));
				c.setFlagWeb(rs.getInt("flag_web"));
				sezione.add(c);
			}
		} catch (Exception e) {
			logger.error("getSezioni", e);
			e.printStackTrace();
			throw new DataException("Cannot load getSezioni");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return sezione;
	}

	public int contaSezioniByFlagWeb(int aooId, int flagWeb) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int countSezioni = 0;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(COUNT_SEZIONI_BY_FLAG_WEB);
			pstmt.setInt(1, aooId);
			pstmt.setInt(2, flagWeb);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				countSezioni=rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("getSezioni", e);
			e.printStackTrace();
			throw new DataException("Cannot load getSezioni");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return countSezioni;
	}
	
	public Collection<AmmTrasparenteVO> getSezioniByUfficio(int ufficioId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		AmmTrasparenteVO c = null;
		ArrayList<AmmTrasparenteVO> sezione = new ArrayList<AmmTrasparenteVO>();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection
					.prepareStatement(SELECT_SEZIONI_BY_UFFICIO_RESPONSABILE);
			pstmt.setInt(1, ufficioId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				c = new AmmTrasparenteVO();
				c.setSezioneId(rs.getInt("sezione_id"));
				c.setDescrizione(rs.getString("descrizione"));
				c.setResponsabileId(rs.getInt("ufficio_responsabile_id"));
				c.setFlagWeb(rs.getInt("flag_web"));
				sezione.add(c);
			}
		} catch (Exception e) {
			logger.error("getSezioni", e);
			e.printStackTrace();
			throw new DataException("Cannot load getSezioni");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return sezione;
	}

	public void deleteAllegati(Connection connection, int sezioneId)
			throws DataException {
		PreparedStatement pstmt = null;

		final String DELETE_DOC = "DELETE FROM allegati_doc_amm_trasparente WHERE doc_sezione_id = ?";
		try {
			pstmt = connection.prepareStatement(DELETE_DOC);
			pstmt.setInt(1, sezioneId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("deleteRiferimenti", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}

	}

	public DocumentoAmmTrasparenteVO newDocumentoSezione(Connection conn,
			DocumentoAmmTrasparenteVO vo) throws DataException {
		PreparedStatement pstmt = null;
		vo.setReturnValue(ReturnValues.UNKNOWN);
		try {
			if (conn == null) {
				logger.warn("newArgomento() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(INSERT_DOCUMENTI_SEZIONI);
			pstmt.setInt(1, vo.getDocSezioneId());
			pstmt.setInt(2, vo.getSezId());
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
			pstmt.setDate(11, vo.getDataSezione());
			pstmt.setInt(12, vo.getNumeroDocumentoSezione());
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

	public AmmTrasparenteVO newSezione(Connection conn, AmmTrasparenteVO vo)
			throws DataException {
		PreparedStatement pstmt = null;
		vo.setReturnValue(ReturnValues.UNKNOWN);
		try {
			if (conn == null) {
				logger.warn("newArgomento() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(INSERT_SEZIONI);
			pstmt.setInt(1, vo.getSezioneId());
			pstmt.setInt(2, vo.getAooId());
			pstmt.setString(3, vo.getDescrizione());
			pstmt.setInt(4, vo.getResponsabileId());
			pstmt.setInt(5, vo.getFlagWeb());
			pstmt.executeUpdate();
			vo.setReturnValue(ReturnValues.SAVED);
		} catch (Exception e) {
			logger.error("Save Sezione", e);
			e.printStackTrace();
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return vo;
	}

	public AmmTrasparenteVO updateSezione(Connection conn, AmmTrasparenteVO vo)
			throws DataException {
		PreparedStatement pstmt = null;
		vo.setReturnValue(ReturnValues.UNKNOWN);
		try {
			if (conn == null) {
				logger.warn("newArgomento() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(UPDATE_SEZIONI);
			pstmt.setString(1, vo.getDescrizione());
			pstmt.setInt(2, vo.getResponsabileId());
			pstmt.setInt(3, vo.getFlagWeb());
			pstmt.setInt(4, vo.getSezioneId());
			pstmt.executeUpdate();
			vo.setReturnValue(ReturnValues.SAVED);
		} catch (Exception e) {
			logger.error("update Sezione", e);
			e.printStackTrace();
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return vo;
	}
	
	public void salvaAllegati(Connection connection, int sezioneId,
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
			pstmt.setInt(2, sezioneId);
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

	public DocumentoAmmTrasparenteVO updateDocumentoSezione(Connection conn,
			DocumentoAmmTrasparenteVO vo) throws DataException {
		PreparedStatement pstmt = null;
		vo.setReturnValue(ReturnValues.UNKNOWN);
		try {
			if (conn == null) {
				logger.warn("updateArgomento() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(UPDATE_DOCUMENTI_SEZIONI);
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
			pstmt.setDate(9, vo.getDataSezione());
			pstmt.setInt(10, vo.getNumeroDocumentoSezione());
			pstmt.setInt(11, 0);
			pstmt.setString(12, vo.getNumeroDocumento());
			pstmt.setString(13, vo.getDescrizione());
			pstmt.setString(14, vo.getSettoreProponente());
			if(vo.getProtocolloId()!=0){
				pstmt.setInt(15, vo.getProtocolloId());
			}else{
				pstmt.setNull(15, Types.INTEGER);
			}
			pstmt.setInt(16, vo.getDocSezioneId());
			pstmt.executeUpdate();
			vo = getDocumentoSezione(conn, vo.getDocSezioneId());
			vo.setReturnValue(ReturnValues.SAVED);

		} catch (Exception e) {
			logger.error("Update Argomento Titolario", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return vo;
	}

	private DocumentoAmmTrasparenteVO getDocumentoSezione(Connection connection,
			int sezioneId) throws DataException {
		DocumentoAmmTrasparenteVO sezione = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("getDocumentoSezione() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_DOC_SEZIONE_BY_ID);
			pstmt.setInt(1, sezioneId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				sezione = getDocumentoSezione(rs);
			}
		} catch (Exception e) {
			logger.error("Load getTitolarioById", e);
			throw new DataException("Cannot load getTitolarioById");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return sezione;
	}

	private DocumentoAmmTrasparenteVO getDocumentoSezione(ResultSet rs)
			throws SQLException {
		DocumentoAmmTrasparenteVO sezione = new DocumentoAmmTrasparenteVO();
		sezione.setDocSezioneId(rs.getInt("doc_sezione_id"));
		sezione.setSezId(rs.getInt("sez_id"));
		sezione.setOggetto(rs.getString("oggetto"));
		sezione.setCapitolo(rs.getString("capitolo"));
		sezione.setImporto(rs.getBigDecimal("importo"));
		sezione.setDescrizione(rs.getString("descrizione"));
		sezione.setNumeroDocumento(rs.getString("numero_documento"));
		sezione.setDataValiditaInizio(rs.getDate("data_validita_inizio"));
		sezione.setDataValiditaFine(rs.getDate("data_validita_fine"));
		sezione.setDataSezione(rs.getDate("data_creazione"));
		sezione.setUfficioId(rs.getInt("ufficio_id"));
		sezione.setNumeroDocumentoSezione(rs.getInt("numero_documento_sezione"));
		sezione.setNote(rs.getString("note"));
		sezione.setSettoreProponente(rs.getString("settore_proponente"));
		sezione.setProtocolloId(rs.getInt("protocollo_id"));
		sezione.setFlagStato(rs.getInt("flag_stato"));
		return sezione;
	}

	public AmmTrasparenteVO getSezione(int sezioneId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		AmmTrasparenteVO c = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_SEZIONE);
			pstmt.setInt(1, sezioneId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				c = new AmmTrasparenteVO();
				c.setSezioneId(rs.getInt("sezione_id"));
				c.setDescrizione(rs.getString("descrizione"));
				c.setResponsabileId(rs.getInt("ufficio_responsabile_id"));
				c.setFlagWeb(rs.getInt("flag_web"));
			}
		} catch (Exception e) {
			logger.error("getSezioni", e);
			e.printStackTrace();
			throw new DataException("Cannot load getSezioni");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return c;
	}
	
	public boolean getSezioneFlagWeb(int sezioneId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean web = false;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_SEZIONE_FLAG_WEB);
			pstmt.setInt(1, sezioneId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				web=rs.getInt("flag_web")==1? true : false;
			}
		} catch (Exception e) {
			logger.error("getSezioni", e);
			e.printStackTrace();
			throw new DataException("Cannot load getSezioni");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return web;
	}

	public DocumentoAmmTrasparenteVO getDocumentoSezione(int docId)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DocumentoAmmTrasparenteVO c = null;
		try {
			connection = jdbcMan.getConnection();
			c = getDocumentoSezione(connection, docId);
			c.setDocumenti(getAllegati(connection, docId));
			
		} catch (Exception e) {
			logger.error("getSezioni", e);
			e.printStackTrace();
			throw new DataException("Cannot load getSezioni");
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
				AmmTrasparenteBO.putAllegato(doc, docs);
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

	public boolean isNumeroDocumentoSezioneUsed(DocumentoAmmTrasparenteVO vo)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(IS_NUMERO_USED);
			pstmt.setInt(1, vo.getSezId());
			pstmt.setInt(2, vo.getNumeroDocumentoSezione());
			pstmt.setInt(3, DateUtil.getYear(vo.getDataValiditaInizio()));
			pstmt.setInt(4, vo.getDocSezioneId());
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

	public int getMaxNumeroSezione(int sezId, int annoCorrente)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(GET_MAX_NUMERO_SEZIONE);
			pstmt.setInt(1, sezId);
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
