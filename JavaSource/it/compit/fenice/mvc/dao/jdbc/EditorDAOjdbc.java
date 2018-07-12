package it.compit.fenice.mvc.dao.jdbc;

import it.compit.fenice.mvc.integration.EditorDAO;
import it.compit.fenice.mvc.presentation.helper.DocumentoAvvocatoGeneraleULLView;
import it.compit.fenice.mvc.presentation.helper.EditorView;
import it.compit.fenice.mvc.vo.documentale.EditorVO;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.InvioClassificatiDestinatariVO;
import it.finsiel.siged.mvc.vo.protocollo.InvioClassificatiVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.finsiel.siged.util.DateUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hsqldb.Types;

public class EditorDAOjdbc implements EditorDAO {

	static Logger logger = Logger.getLogger(EditorDAOjdbc.class.getName());

	private JDBCManager jdbcMan = new JDBCManager();

	private static final String GET_DOC_AVVOCATO_GENERALE_ULL = "SELECT de.*, ut.nome AS nome_mittente,ut.cognome,p.numero_procedimento,p.procedimento_id,p.stato_id AS stato_procedimento "
			+ "FROM documenti_editor de left join fenice.utenti ut ON(ut.user_name=de.row_created_user) "
			+ "LEFT JOIN fascicolo_documenti_editor fa ON(fa.documento_id=de.documento_id) "
			+ "LEFT JOIN procedimenti_fascicolo pf ON(pf.fascicolo_id=fa.fascicolo_id) "
			+ "LEFT JOIN procedimenti p ON(pf.procedimento_id=p.procedimento_id) "
			+ "LEFT JOIN tipi_procedimento tp ON (p.tipo_procedimento_id=tp.tipo_procedimenti_id) "
			+ "WHERE de.carica_id=? AND de.flag_tipo=1 AND tp.flag_ull=1 AND p.posizione_id='A' ";

	private static final String COUNT_DOC_AVVOCATO_GENERALE_ULL = "SELECT count(de.documento_id) "
			+ "FROM documenti_editor de left join fenice.utenti ut ON(ut.user_name=de.row_created_user) "
			+ "LEFT JOIN fascicolo_documenti_editor fa ON(fa.documento_id=de.documento_id) "
			+ "LEFT JOIN procedimenti_fascicolo pf ON(pf.fascicolo_id=fa.fascicolo_id) "
			+ "LEFT JOIN procedimenti p ON(pf.procedimento_id=p.procedimento_id) "
			+ "LEFT JOIN tipi_procedimento tp ON (p.tipo_procedimento_id=tp.tipo_procedimenti_id) "
			+ "WHERE de.carica_id=? AND de.flag_tipo=1 AND tp.flag_ull=1 AND p.posizione_id='A' ";

	private static final String COUNT_DOC_BY_CARICA = "SELECT count(documento_id) FROM documenti_editor WHERE carica_id=? AND flag_tipo=?";

	private static final String SELECT_DOC_BY_CARICA = "SELECT * FROM documenti_editor WHERE carica_id=? AND flag_tipo=?";

	/*
	 public final static String SELECT_FASCICOLI = "SELECT f.*, d.row_created_user AS utente_fascicolatore FROM fascicoli f, fascicolo_documenti_editor d"
			+ " WHERE f.fascicolo_id = d.fascicolo_id AND d.documento_id=?"; 
	 */
	
	private static final String SELECT_DOC_TEMPLATE_BY_CARICA = "SELECT de.*, ut.nome AS nome_mittente,ut.cognome, t.path_titolario||'/'||f.path_progressivo||'-'||f.anno_riferimento||' - '||f.oggetto as fascicolo FROM documenti_editor de " 
			+"LEFT JOIN utenti ut ON(ut.user_name=de.row_created_user) "
			+"LEFT JOIN fascicolo_documenti_editor fd ON(fd.documento_id=de.documento_id) "
			+"LEFT JOIN fascicoli f ON(f.fascicolo_id=fd.fascicolo_id) "
			+"LEFT JOIN titolario t ON(t.titolario_id=f.titolario_id) "
			+"WHERE de.carica_id=? AND de.flag_tipo=?";

	private static final String SELECT_DOC_BY_ID = "SELECT * FROM documenti_editor WHERE documento_id=?";

	private static final String INSERT_DOCUMENTO = "INSERT INTO documenti_editor(documento_id,nome,txt, row_created_user, carica_id,flag_tipo,oggetto) VALUES (?, ?, ?, ?, ?,?,?)";

	private static final String UPDATE_DOCUMENTO = "UPDATE documenti_editor SET  txt=?, nome=?, row_created_time=?, row_created_user=?, flag_stato=0, oggetto=? WHERE documento_id=?";

	private final static String AGGIORNA_STATO = "UPDATE documenti_editor SET flag_stato=? WHERE documento_id=?";

	private final static String AGGIORNA_CARICA_FIRMA = "UPDATE documenti_editor SET carica_id=?,msg_carica=? WHERE documento_id=?";

	private final static String RITORNA_A_CARICA = "UPDATE documenti_editor SET msg_carica=?,carica_id=(select carica_id FROM storia_documenti_editor WHERE documento_id=? AND versione=0) WHERE documento_id=?";

	public static final String INVIO_CLASSIFICATI = "INSERT INTO invio_classificati "
			+ " (id, dfa_id, aoo_id, ufficio_mittente_id, utente_mittente_id, procedimento_id, data_scadenza, text_scadenza) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

	public static final String INVIO_CLASSIFICATI_DESTINATARI = "INSERT INTO invio_classificati_destinatari (id, dfa_id, flag_tipo_destinatario, indirizzo,  email ,  destinatario ,"
			+ " mezzo_spedizione, citta ,  data_spedizione ,  flag_conoscenza ,  data_effettiva_spedizione,"
			+ "versione) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public static final String INVIO_CLASSIFICATI_FASCICOLO = "INSERT INTO invio_classificati_fascicoli (id, dfa_id, fascicolo_id) VALUES (?, ?, ?)";

	public final static String SELECT_DOCUMENTO_BY_ID_VERSIONE = "SELECT * FROM storia_documenti_editor WHERE documento_id = ? and versione = ?";

	public final static String SELECT_FLAG_TIPO_BY_ID = "SELECT flag_tipo FROM documenti_editor WHERE documento_id = ?";

	private final static String INSERT_ASSEGNATARI = "INSERT INTO documenti_editor_assegnatari"
			+ " (assegnatario_id, documento_id, data_assegnazione, data_operazione, stat_assegnazione, ufficio_assegnante_id, ufficio_assegnatario_id,  versione, flag_competente, carica_assegnatario_id, carica_assegnante_id, check_presa_visione) VALUES (?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?)";

	private static final String INSERT_ALLACCI = "INSERT INTO documenti_editor_allacci( documento_id, protocollo_allacciato_id, versione) VALUES (?, ?, ?);";

	private static final String INSERT_FASCICOLI = "INSERT INTO fascicolo_documenti_editor(documento_id, fascicolo_id, row_created_user, versione)VALUES (?, ?, ?, ?);";

	private final static String SELECT_ASSEGNATARI = "SELECT * FROM documenti_editor_assegnatari"
			+ " WHERE documento_id = ?";

	public final static String SELECT_ALLACCI = "SELECT a.*, p.protocollo_id,p.nume_protocollo,"
			+ "p.flag_tipo,p.data_registrazione"
			+ " FROM protocolli p, documenti_editor_allacci a"
			+ " WHERE a.protocollo_allacciato_id = p.protocollo_id and a.documento_id = ?";

	public final static String SELECT_FASCICOLI = "SELECT f.*, d.row_created_user AS utente_fascicolatore FROM fascicoli f, fascicolo_documenti_editor d"
			+ " WHERE f.fascicolo_id = d.fascicolo_id AND d.documento_id=?";

	public Collection<FascicoloVO> getFascicoli(int id) throws DataException {
		Collection<FascicoloVO> fascicoli = new ArrayList<FascicoloVO>();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			fascicoli = getFascicoli(connection, id);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " documentoId :" + id);
		} finally {
			jdbcMan.close(connection);
		}
		return fascicoli;
	}

	public Collection<FascicoloVO> getFascicoli(Connection connection, int id)
			throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Collection<FascicoloVO> fascicoli = new ArrayList<FascicoloVO>();
		FascicoloVO fascicolo = new FascicoloVO();
		try {
			if (connection == null) {
				logger.warn("getFascicoli() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_FASCICOLI);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				fascicolo = getFascicolo(rs);
				fascicoli.add(fascicolo);
			}

		} catch (Exception e) {
			logger.error("Load getFascicoli by ID", e);
			throw new DataException("Cannot load Fascicolo by ID");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return fascicoli;
	}

	private FascicoloVO getFascicolo(ResultSet rs) throws DataException {
		FascicoloVO fascicolo = new FascicoloVO();
		try {
			fascicolo.setId(rs.getInt("fascicolo_id"));
			fascicolo.setAooId(rs.getInt("aoo_id"));
			fascicolo.setRegistroId(rs.getInt("registro_id"));
			fascicolo.setCodice(rs.getString("codice"));
			fascicolo.setDescrizione(rs.getString("descrizione"));
			fascicolo.setNome(rs.getString("nome"));
			fascicolo.setProgressivo(rs.getLong("progressivo"));
			fascicolo.setUfficioResponsabileId(rs
					.getInt("ufficio_responsabile_id"));
			fascicolo.setCaricaIstruttoreId(rs.getInt("carica_istruttore_id"));
			
			fascicolo.setInteressato(rs.getString("interessato"));
			fascicolo.setDelegato(rs.getString("delegato"));
			fascicolo.setIndiInteressato(rs.getString("indi_interessato"));
			fascicolo.setIndiDelegato(rs.getString("indi_delegato"));
			
			fascicolo.setGiorniMax(rs.getInt("giorni_max"));
			fascicolo.setGiorniAlert(rs.getInt("giorni_alert"));
			fascicolo.setParentId(rs.getInt("parent_id"));
			fascicolo.setCaricaResponsabileId(rs
					.getInt("carica_responsabile_id"));
			fascicolo.setUfficioIntestatarioId(rs
					.getInt("ufficio_intestatario_id"));
			fascicolo.setCaricaIntestatarioId(rs
					.getInt("carica_intestatario_id"));
			fascicolo.setNote(rs.getString("note"));
			fascicolo.setProcessoId(rs.getInt("processo_id"));
			fascicolo.setOggetto(rs.getString("oggetto"));
			fascicolo.setDataApertura(rs.getDate("data_apertura"));
			fascicolo.setDataAperturaStr(DateUtil.formattaData(rs.getDate(
					"data_apertura").getTime()));
			fascicolo.setDataChiusura(rs.getDate("data_chiusura"));
			fascicolo.setStato(rs.getInt("stato"));
			fascicolo.setDescrizione(rs.getString("descrizione"));
			fascicolo.setTitolarioId(rs.getInt("titolario_id"));
			fascicolo.setRowCreatedTime(rs.getDate("row_created_time"));
			fascicolo.setRowCreatedUser(rs.getString("utente_fascicolatore"));
			fascicolo.setTipoFascicolo(rs.getInt("tipo"));
			fascicolo.setAnnoRiferimento(rs.getInt("anno_riferimento"));
			fascicolo.setDataEvidenza(rs.getDate("data_evidenza"));
			fascicolo.setDataUltimoMovimento(rs
					.getDate("data_ultimo_movimento"));
			fascicolo.setDataScarto(rs.getDate("data_scarto"));
			fascicolo.setDataCarico(rs.getDate("data_carico"));
			fascicolo.setDataScarico(rs.getDate("data_scarico"));
			fascicolo.setVersione(rs.getInt("versione"));
			fascicolo
					.setCollocazioneLabel1(rs.getString("collocazione_label1"));
			fascicolo
					.setCollocazioneLabel2(rs.getString("collocazione_label2"));
			fascicolo
					.setCollocazioneLabel3(rs.getString("collocazione_label3"));
			fascicolo
					.setCollocazioneLabel4(rs.getString("collocazione_label4"));
			fascicolo.setCollocazioneValore1(rs
					.getString("collocazione_valore1"));
			fascicolo.setCollocazioneValore2(rs
					.getString("collocazione_valore2"));
			fascicolo.setCollocazioneValore3(rs
					.getString("collocazione_valore3"));
			fascicolo.setCollocazioneValore4(rs
					.getString("collocazione_valore4"));
			fascicolo.setCapitolo(rs.getString("capitolo"));
			fascicolo.setComune(rs.getString("comune"));
			fascicolo.setPathProgressivo(rs.getString("path_progressivo"));

		} catch (SQLException e) {
			logger.error("Load getFascicoloById()", e);
			throw new DataException("Cannot load getFascicoloById()");
		}

		return fascicolo;
	}

	public boolean eliminaDocumentoTemplate(int id) throws DataException {
		boolean deleted = false;
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			deleted = eliminaDocumentoTemplate(connection, id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException(e.getMessage() + " documentoId :" + id);
		} finally {
			jdbcMan.close(connection);
		}
		return deleted;
	}

	public boolean eliminaDocumentoTemplate(Connection connection, int id)
			throws DataException {
		boolean deleted = false;
		String[] tables = { "documenti_editor_allacci",
				"documenti_editor_assegnatari", "fascicolo_documenti_editor",
				"documenti_editor" };
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("storia - Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			for (int i = 0; i < tables.length; i++) {
				String sql = "DELETE FROM storia_" + tables[i]
						+ " WHERE documento_id = ?";
				pstmt = connection.prepareStatement(sql);
				pstmt.setInt(1, id);
				pstmt.executeUpdate();
				jdbcMan.close(pstmt);
			}

			for (int i = 0; i < tables.length; i++) {
				String sql = "DELETE FROM " + tables[i]
						+ " WHERE documento_id = ?";
				pstmt = connection.prepareStatement(sql);
				pstmt.setInt(1, id);
				pstmt.executeUpdate();
				jdbcMan.close(pstmt);
			}
			deleted = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("eliminaDocumentoTemplate" + id, e);
			throw new DataException("Cannot delete Documento Editor Template");
		} finally {
			jdbcMan.close(pstmt);
		}
		return deleted;
	}

	public Collection<AllaccioVO> getAllacci(int id) throws DataException {
		Collection<AllaccioVO> allacci = new ArrayList<AllaccioVO>();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			allacci = getAllacci(connection, id);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " documentoId :" + id);
		} finally {
			jdbcMan.close(connection);
		}
		return allacci;
	}

	private Collection<AllaccioVO> getAllacci(Connection connection, int id)
			throws DataException {
		ArrayList<AllaccioVO> allacci = new ArrayList<AllaccioVO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(SELECT_ALLACCI);
			pstmt.setInt(1, id);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				AllaccioVO allaccio = new AllaccioVO();
				allaccio.setProtocolloAllacciatoId(rs.getInt("protocollo_id"));
				allaccio.setDataRegistrazione(rs.getDate("DATA_REGISTRAZIONE"));
				allaccio.setNumeroProtocollo(rs.getInt("NUME_PROTOCOLLO"));
				allaccio.setTipo(rs.getString("FLAG_TIPO"));
				allaccio.addDescrizioneTemplate();
				allacci.add(allaccio);
			}
		} catch (Exception e) {
			logger.error("getAllacci", e);
			throw new DataException("Cannot load getAllacci");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return allacci;
	}

	public Collection<AssegnatarioVO> getAssegnatari(int id)
			throws DataException {
		ArrayList<AssegnatarioVO> assegnatari = new ArrayList<AssegnatarioVO>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_ASSEGNATARI);
			pstmt.setInt(1, id);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				AssegnatarioVO assegnatario = new AssegnatarioVO();
				assegnatario.setId(rs.getInt("assegnatario_id"));
				assegnatario.setDataAssegnazione(rs
						.getDate("data_assegnazione"));
				assegnatario.setDataOperazione(rs.getDate("data_operazione"));
				assegnatario.setUfficioAssegnanteId(rs
						.getInt("ufficio_assegnante_id"));
				assegnatario.setUfficioAssegnatarioId(rs
						.getInt("ufficio_assegnatario_id"));

				assegnatario.setCaricaAssegnatarioId(rs
						.getInt("carica_assegnatario_id"));
				assegnatario.setCompetente(rs.getBoolean("flag_competente"));
				assegnatario.setCaricaAssegnanteId(rs
						.getInt("carica_assegnante_id"));

				assegnatario.setStatoAssegnazione(rs.getString(
						"stat_assegnazione").charAt(0));
				assegnatario.setPresaVisione(rs
						.getBoolean("check_presa_visione"));
				assegnatari.add(assegnatario);
			}
		} catch (Exception e) {
			logger.error("getAssegnatari", e);
			throw new DataException("Cannot load getAssegnatari");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return assegnatari;
	}

	public void salvaAssegnatario(Connection connection, int docId,
			AssegnatarioVO assegnatario, int versione) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("salvaAssegnatario() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			if (assegnatario != null) {
				pstmt = connection.prepareStatement(INSERT_ASSEGNATARI);

				java.sql.Date now = new java.sql.Date(
						(new java.util.Date()).getTime());
				pstmt.setInt(1, assegnatario.getId().intValue());
				pstmt.setInt(2, docId);
				pstmt.setDate(3, now);
				pstmt.setDate(4, now);
				pstmt.setString(5, assegnatario.getStatoAssegnazione() + "");
				pstmt.setInt(6, assegnatario.getUfficioAssegnanteId());
				pstmt.setInt(7, assegnatario.getUfficioAssegnatarioId());
				pstmt.setInt(8, versione);
				pstmt.setInt(9, assegnatario.isCompetente() ? 1 : 0);

				if (assegnatario.getCaricaAssegnatarioId() == 0)
					pstmt.setNull(10, Types.INTEGER);
				else
					pstmt.setInt(10, assegnatario.getCaricaAssegnatarioId());

				if (assegnatario.getCaricaAssegnanteId() == 0)
					pstmt.setNull(11, Types.INTEGER);
				else
					pstmt.setInt(11, assegnatario.getCaricaAssegnanteId());

				if (assegnatario.isCompetente())
					pstmt.setNull(12, Types.INTEGER);
				else
					pstmt.setInt(12, assegnatario.isPresaVisione() ? 1 : 0);
				pstmt.executeUpdate();
			}
		} catch (Exception e) {
			logger.error("Save Assegnatari-Protocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void eliminaAssegnatari(Connection connection, int docId)
			throws DataException {
		PreparedStatement pstmt = null;
		final String DELETE_ASSEGNATARI = "DELETE FROM documenti_editor_assegnatari WHERE documento_id = ?";
		try {
			pstmt = connection.prepareStatement(DELETE_ASSEGNATARI);
			pstmt.setInt(1, docId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("eliminaAssegnatari", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void eliminaAllacci(Connection connection, int docId)
			throws DataException {
		PreparedStatement pstmt = null;
		final String DELETE_ALLACCI = "DELETE FROM documenti_editor_allacci WHERE documento_id = ?";
		try {
			pstmt = connection.prepareStatement(DELETE_ALLACCI);
			pstmt.setInt(1, docId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("eliminaAllacci", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void eliminaFascicoli(Connection connection, int docId)
			throws DataException {
		PreparedStatement pstmt = null;
		final String DELETE_FASCICOLI = "DELETE FROM fascicolo_documenti_editor WHERE documento_id = ?";
		try {
			pstmt = connection.prepareStatement(DELETE_FASCICOLI);
			pstmt.setInt(1, docId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("eliminaFascicoli", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void salvaAllaccio(Connection connection, int docId,
			AllaccioVO allaccio, int versione) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("salvaAllaccio() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			if (allaccio != null) {
				pstmt = connection.prepareStatement(INSERT_ALLACCI);
				pstmt.setInt(1, docId);
				pstmt.setInt(2, allaccio.getProtocolloAllacciatoId());
				pstmt.setInt(3, versione);
				pstmt.executeUpdate();
			}
		} catch (Exception e) {
			logger.error("salvaAllaccio", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void salvaFascicolo(Connection connection, int docId,
			FascicoloVO fascicolo, int versione) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("salvaFascicolo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			if (fascicolo != null) {
				pstmt = connection.prepareStatement(INSERT_FASCICOLI);
				pstmt.setInt(1, docId);
				pstmt.setInt(2, fascicolo.getId());
				pstmt.setString(3, fascicolo.getRowCreatedUser());
				pstmt.setInt(4, versione);
				pstmt.executeUpdate();
			}
		} catch (Exception e) {
			logger.error("salvaFascicolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public EditorVO getDocumentoByIdVersione(int id, int versione)
			throws DataException {
		EditorVO eVO = new EditorVO();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			eVO = getDocumentoByIdVersione(connection, id, versione);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " DocumentoId :" + id
					+ " Versione :" + versione);
		} finally {
			jdbcMan.close(connection);
		}
		return eVO;
	}

	private EditorVO getDocumentoByIdVersione(Connection connection, int id,
			int versione) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		EditorVO editor = new EditorVO();
		try {
			if (connection == null) {
				logger.warn("getDocumentoByIdVersione() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection
					.prepareStatement(SELECT_DOCUMENTO_BY_ID_VERSIONE);
			pstmt.setInt(1, id);
			pstmt.setInt(2, versione);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				editor = getDocumento(rs);
				editor.setReturnValue(ReturnValues.FOUND);
			} else {
				editor.setReturnValue(ReturnValues.NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("Load Versione Documento by ID", e);
			throw new DataException("Cannot load Versione Documento by ID");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return editor;
	}

	public int getFlagTipoById(Connection connection, int id)
			throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int flag = 0;
		try {
			if (connection == null) {
				logger.warn("getDocumentoByIdVersione() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_FLAG_TIPO_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				flag = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Load getFlagTipoById", e);
			throw new DataException("Cannot load getFlagTipoById");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return flag;
	}

	public Collection<EditorVO> getStoriaDocumento(int documentoId)
			throws DataException {
		ArrayList<EditorVO> storiaEditor = new ArrayList<EditorVO>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sqlStoriaEditor = "SELECT * FROM storia_documenti_editor WHERE"
					+ " documento_id=? ORDER BY VERSIONE desc";
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(sqlStoriaEditor);
			pstmt.setInt(1, documentoId);

			rs = pstmt.executeQuery();
			EditorVO documento;
			while (rs.next()) {
				documento = new EditorVO();
				documento.setDocumentoId(rs.getInt("documento_id"));
				documento.setCaricaId(rs.getInt("carica_id"));
				documento.setTxt(rs.getString("txt"));
				documento.setNome(rs.getString("nome"));
				documento.setDataCreazione(rs.getTimestamp("row_created_time"));
				documento.setVersione(rs.getInt("versione"));
				documento.setFlagStato(rs.getInt("flag_stato"));
				storiaEditor.add(documento);

			}
		} catch (Exception e) {
			logger.error("getStoriaEditor", e);
			throw new DataException("Cannot load getStoriaEditor");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return storiaEditor;

	}

	public void salvaInvioClassificati(Connection connection,
			InvioClassificatiVO icVO) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("salvaInvioClassificati() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(INVIO_CLASSIFICATI);
			pstmt.setInt(1, icVO.getId().intValue());
			pstmt.setInt(2, icVO.getDocumentoId());
			pstmt.setInt(3, icVO.getAooId());
			pstmt.setInt(4, icVO.getUfficioMittenteId());
			pstmt.setInt(5, icVO.getUtenteMittenteId());
			pstmt.setInt(6, icVO.getProcedimentoId());

			if (icVO.getDataScadenza() != null) {
				pstmt.setDate(7, new Date(icVO.getDataScadenza().getTime()));
			} else {
				pstmt.setNull(7, Types.DATE);
			}
			pstmt.setString(8, icVO.getTextScadenza());
			pstmt.executeUpdate();
			logger.info("Invio Classificati :" + icVO.getDocumentoId());
		} catch (Exception e) {
			logger.error("salvaInvioClassificati", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void salvaDestinatariInvioClassificati(Connection connection,
			InvioClassificatiDestinatariVO icdVO) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("salvaDestinatariInvioClassificati() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(INVIO_CLASSIFICATI_DESTINATARI);
			pstmt.setInt(1, icdVO.getId().intValue());
			pstmt.setInt(2, icdVO.getDocumentoId());
			DestinatarioVO d = icdVO.getDestinatario();
			pstmt.setString(3, d.getFlagTipoDestinatario());
			pstmt.setString(4, d.getIndirizzo());
			pstmt.setString(5, d.getEmail());
			pstmt.setString(6, d.getDestinatario());
			pstmt.setInt(7, d.getMezzoSpedizioneId());
			pstmt.setString(8, d.getCitta());
			if (d.getDataSpedizione() != null) {
				pstmt.setDate(9, new Date(d.getDataSpedizione().getTime()));
			} else {
				pstmt.setNull(9, Types.DATE);
			}

			pstmt.setString(10, d.getFlagConoscenza() ? "1" : "0");
			if (d.getDataEffettivaSpedizione() != null) {
				pstmt.setDate(11, new Date(d.getDataEffettivaSpedizione()
						.getTime()));
			} else {
				pstmt.setNull(11, Types.DATE);
			}
			pstmt.setInt(12, d.getVersione());
			pstmt.executeUpdate();
			logger.info("salvaDestinatariInvioClassificati:"
					+ icdVO.getDocumentoId() + "," + d.getId());

		} catch (Exception e) {
			logger.error("salvaDestinatariInvioClassificati", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void salvaFascicoloInvioClassificati(Connection connection, int id,
			int documentoId, int fascicoloId) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("salvaDestinatariInvioClassificati() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(INVIO_CLASSIFICATI_FASCICOLO);
			pstmt.setInt(1, id);
			pstmt.setInt(2, documentoId);
			pstmt.setInt(3, fascicoloId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("salvaDestinatariInvioClassificati", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public int aggiornaStato(int docId, int stato) throws DataException {
		int ret = ReturnValues.UNKNOWN;
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			ret = aggiornaStato(connection, docId, stato);
			connection.commit();
		} catch (Exception e) {
			jdbcMan.rollback(connection);
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(connection);
		}
		return ret;
	}

	public int aggiornaStato(Connection connection, int docId, int stato)
			throws DataException {
		int ret = ReturnValues.UNKNOWN;
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("aggiornaStato() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(AGGIORNA_STATO);
			pstmt.setInt(1, stato);
			pstmt.setInt(2, docId);
			pstmt.executeUpdate();
			ret = ReturnValues.SAVED;

		} catch (SQLException e) {
			logger.error("aggiornaStato", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return ret;
	}

	public int inviaPerFirma(EditorVO eVO, int dirigenteId)
			throws DataException {
		int ret = ReturnValues.UNKNOWN;
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			ret = inviaPerFirma(connection, eVO, dirigenteId);
			connection.commit();
		} catch (Exception e) {
			jdbcMan.rollback(connection);
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(connection);
		}
		return ret;
	}

	public int inviaPerFirma(Connection connection, EditorVO eVO,
			int dirigenteId) throws DataException {
		int ret = ReturnValues.UNKNOWN;
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("inviaPerFirma() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(AGGIORNA_CARICA_FIRMA);
			pstmt.setInt(1, dirigenteId);
			pstmt.setString(2, eVO.getMsgCarica());
			pstmt.setInt(3, eVO.getDocumentoId());
			pstmt.executeUpdate();
			ret = ReturnValues.SAVED;

		} catch (SQLException e) {
			logger.error("inviaPerFirma", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return ret;
	}

	public boolean rifiutaDocumentoTemplate(Connection connection, EditorVO eVO)
			throws DataException {
		boolean ret = false;
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("ritornaDocumentoTemplate() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			pstmt = connection.prepareStatement(RITORNA_A_CARICA);
			pstmt.setString(1, eVO.getMsgCarica());
			pstmt.setInt(2, eVO.getDocumentoId());
			pstmt.setInt(3, eVO.getDocumentoId());
			pstmt.executeUpdate();
			ret = true;
		} catch (SQLException e) {
			logger.error("ritornaDocumentoTemplate", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return ret;
	}

	public void archiviaDocumento(Connection connection, int docId)
			throws DataException {
		String[] tables = { "documenti_editor" };
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
						+ " WHERE documento_id = ?";
				pstmt = connection.prepareStatement(sql);
				pstmt.setInt(1, docId);
				pstmt.executeUpdate();
				jdbcMan.close(pstmt);
			}

			for (int i = 0; i < tables.length; i++) {
				String sql = "UPDATE " + tables[i] + " SET versione=versione+1"
						+ " WHERE documento_id = ?";
				pstmt = connection.prepareStatement(sql);
				pstmt.setInt(1, docId);
				pstmt.executeUpdate();
				jdbcMan.close(pstmt);
			}

		} catch (Exception e) {
			logger.error("archiviaDocumento" + docId, e);
			throw new DataException("Cannot insert Storia Documento Editor");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void archiviaDocumentoTemplate(Connection connection, int docId)
			throws DataException {
		String[] tables = { "documenti_editor", "documenti_editor_allacci",
				"documenti_editor_assegnatari", "fascicolo_documenti_editor" };
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
						+ " WHERE documento_id = ?";
				pstmt = connection.prepareStatement(sql);
				pstmt.setInt(1, docId);
				pstmt.executeUpdate();
				jdbcMan.close(pstmt);
			}

			for (int i = 0; i < tables.length; i++) {
				String sql = "UPDATE " + tables[i] + " SET versione=versione+1"
						+ " WHERE documento_id = ?";
				pstmt = connection.prepareStatement(sql);
				pstmt.setInt(1, docId);
				pstmt.executeUpdate();
				jdbcMan.close(pstmt);
			}
		} catch (Exception e) {
			logger.error("archiviaDocumento" + docId, e);
			throw new DataException("Cannot insert Storia Documento Editor");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public int cancellaDocumento(int id) throws DataException {
		Connection connection = null;
		int retVal = ReturnValues.UNKNOWN;
		try {
			connection = jdbcMan.getConnection();
			cancellaDocumento(connection, id);
			retVal = ReturnValues.SAVED;
		} catch (Exception e) {
			throw new DataException("cancellaDocumento :" + id + " "
					+ e.getMessage());
		} finally {
			jdbcMan.close(connection);
		}
		return retVal;
	}

	private void cancellaDocumento(Connection connection, int id)
			throws DataException, SQLException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("storia - Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			connection.setAutoCommit(false);
			String del = "DELETE FROM documenti_editor  WHERE documento_id = ?";
			pstmt = connection.prepareStatement(del);
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
			jdbcMan.close(pstmt);
			del = "DELETE FROM storia_documenti_editor  WHERE documento_id = ?";
			pstmt = connection.prepareStatement(del);
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
			connection.commit();
			

		} catch (Exception e) {
			connection.rollback();
			logger.error("cancellaDocumento" + id, e);
			throw new DataException("Cannot insert Storia Documento Editor");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public EditorVO getDocumento(int id) throws DataException {
		Connection connection = null;
		EditorVO vo = new EditorVO();
		try {
			connection = jdbcMan.getConnection();
			vo = getDocumento(connection, id);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " id :" + id);
		} finally {
			jdbcMan.close(connection);
		}
		return vo;
	}

	public int salvaDocumento(EditorVO vo) throws DataException {
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			return salvaDocumento(connection, vo);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " id :" + vo);
		} finally {
			jdbcMan.close(connection);
		}
	}

	public int aggiornaDocumento(EditorVO vo) throws DataException {
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			return aggiornaDocumento(connection, vo);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " id :" + vo);
		} finally {
			jdbcMan.close(connection);
		}
	}

	public int salvaDocumento(Connection conn, EditorVO vo)
			throws DataException {
		PreparedStatement pstmt = null;
		vo.setReturnValue(ReturnValues.UNKNOWN);
		try {
			if (conn == null) {
				logger.warn("salvaDocumento() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(INSERT_DOCUMENTO);
			pstmt.setInt(1, vo.getDocumentoId());
			pstmt.setString(2, vo.getNome());
			pstmt.setString(3, vo.getTxt());
			pstmt.setString(4, vo.getRowCreatedUser());
			pstmt.setInt(5, vo.getCaricaId());
			pstmt.setInt(6, vo.getFlagTipo());
			pstmt.setString(7, vo.getOggetto());
			pstmt.executeUpdate();
			vo.setReturnValue(ReturnValues.SAVED);
		} catch (Exception e) {
			logger.error("Save Documento", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return vo.getReturnValue();
	}

	public int aggiornaDocumento(Connection conn, EditorVO vo)
			throws DataException {
		PreparedStatement pstmt = null;
		vo.setReturnValue(ReturnValues.UNKNOWN);
		try {
			if (conn == null) {
				logger.warn("updateArgomento() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(UPDATE_DOCUMENTO);
			pstmt.setString(1, vo.getTxt());
			pstmt.setString(2, vo.getNome());
			pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			pstmt.setString(4, vo.getRowCreatedUser());
			pstmt.setString(5, vo.getOggetto());
			pstmt.setInt(6, vo.getDocumentoId());
			pstmt.executeUpdate();
			vo.setReturnValue(ReturnValues.SAVED);

		} catch (Exception e) {
			logger.error("aggiornaDocumento", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return vo.getReturnValue();
	}

	public EditorVO getDocumento(Connection connection, int documentoId)
			throws DataException {
		EditorVO ed = new EditorVO();
		ed.setReturnValue(ReturnValues.NOT_FOUND);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("getDocumento() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_DOC_BY_ID);
			pstmt.setInt(1, documentoId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				ed = getDocumento(rs);
			}
		} catch (Exception e) {
			logger.error("Load getTitolarioById", e);
			throw new DataException("Cannot load getTitolarioById");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return ed;
	}

	private EditorVO getDocumento(ResultSet rs) throws SQLException {
		EditorVO ed = new EditorVO();
		ed.setDocumentoId(rs.getInt("documento_id"));
		ed.setFlagStato(rs.getInt("flag_stato"));
		ed.setTxt(rs.getString("txt"));
		ed.setNome(rs.getString("nome"));
		ed.setCaricaId(rs.getInt("carica_id"));
		ed.setVersione(rs.getInt("versione"));
		ed.setRowCreatedUser(rs.getString("row_created_user"));
		ed.setOggetto(rs.getString("oggetto"));
		ed.setFlagTipo(rs.getInt("flag_tipo"));
		ed.setReturnValue(ReturnValues.FOUND);
		return ed;
	}

	public Collection<EditorView> getDocumentiByCarica(int caricaId, int tipo)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		EditorView ed = null;
		ArrayList<EditorView> documenti = new ArrayList<EditorView>();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_DOC_BY_CARICA);
			pstmt.setInt(1, caricaId);
			pstmt.setInt(2, tipo);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ed = new EditorView();
				ed.setDocumentoId(rs.getInt("documento_id"));
				ed.setFlagStato(rs.getInt("flag_stato"));
				ed.setNome(rs.getString("nome"));
				documenti.add(ed);
			}
		} catch (Exception e) {
			logger.error("getDocumentiByCarica", e);
			throw new DataException("Cannot load getDocumentiByCarica");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return documenti;
	}

	public int contaDocumentiAvvocatoGeneraleULL(int caricaId,
			Integer statoProcedimento) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int c = 0;
		try {
			connection = jdbcMan.getConnection();
			StringBuffer query = new StringBuffer(
					COUNT_DOC_AVVOCATO_GENERALE_ULL);
			if (statoProcedimento != null)
				query.append(" AND p.stato_id=" + statoProcedimento.intValue());
			pstmt = connection.prepareStatement(query.toString());
			pstmt.setInt(1, caricaId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				c = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("contaDocumentiAvvocatoGeneraleULL", e);
			throw new DataException(
					"Cannot load contaDocumentiAvvocatoGeneraleULL");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return c;
	}

	public Map<Integer, DocumentoAvvocatoGeneraleULLView> getDocumentiAvvocatoGeneraleULL(
			int caricaId, Integer statoProcedimento) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DocumentoAvvocatoGeneraleULLView ed = null;
		Map<Integer, DocumentoAvvocatoGeneraleULLView> documenti = new HashMap<Integer, DocumentoAvvocatoGeneraleULLView>();
		try {
			connection = jdbcMan.getConnection();
			StringBuffer query = new StringBuffer(GET_DOC_AVVOCATO_GENERALE_ULL);
			if (statoProcedimento != null)
				query.append(" AND p.stato_id=" + statoProcedimento.intValue());
			pstmt = connection.prepareStatement(query.toString());
			pstmt.setInt(1, caricaId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ed = new DocumentoAvvocatoGeneraleULLView();
				ed.setDocumentoId(rs.getInt("documento_id"));
				ed.setFlagStato(rs.getInt("flag_stato"));
				ed.setOggetto(rs.getString("oggetto"));
				ed.setTxt(rs.getString("txt"));
				ed.setMsgCarica(rs.getString("msg_carica"));
				ed.setMittente((rs.getString("cognome") != null ? rs
						.getString("cognome") + " " : "")
						+ (rs.getString("nome_mittente") != null ? rs
								.getString("nome_mittente") : ""));
				ed.setProcedimentoId(rs.getInt("procedimento_id"));
				ed.setStatoProcedimento(rs.getInt("stato_procedimento"));
				ed.setNumeroProcedimento(rs.getString("numero_procedimento"));
				documenti.put(ed.getDocumentoId(), ed);
			}
		} catch (Exception e) {
			logger.error("getDocumentiAvvocatoGeneraleULL", e);
			throw new DataException(
					"Cannot load getDocumentiAvvocatoGeneraleULL");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return documenti;
	}

	public Collection<EditorView> getDocumentiTemplateByCarica(int caricaId,
			int tipo) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		EditorView ed = null;
		ArrayList<EditorView> documenti = new ArrayList<EditorView>();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_DOC_TEMPLATE_BY_CARICA);
			pstmt.setInt(1, caricaId);
			pstmt.setInt(2, tipo);
			rs = pstmt.executeQuery();
			
			StringBuffer fasc = null;
			while (rs.next()) {
				if (ed == null
						|| ed.getDocumentoId() != rs.getInt("documento_id")) {
					if (ed != null) {
						fasc.append("</ul>");
						ed.setFascicolo(fasc.toString());
					}
					fasc = new StringBuffer("<ul>");
					ed = new EditorView();
					ed.setDocumentoId(rs.getInt("documento_id"));
					ed.setFlagStato(rs.getInt("flag_stato"));
					ed.setOggetto(rs.getString("oggetto"));
					ed.setMsgCarica(rs.getString("msg_carica"));
					ed.setMittente((rs.getString("cognome") != null ? rs.getString("cognome") + " " : "")+ (rs.getString("nome_mittente") != null ? rs.getString("nome_mittente") : ""));
					documenti.add(ed);
				}
				if(rs.getString("fascicolo")!=null)
					fasc.append("<li>"+rs.getString("fascicolo")+"</li>");
				
			}
			if (ed != null) {
				fasc.append("</ul>");
				ed.setFascicolo(fasc.toString());
			}
		} catch (Exception e) {
			logger.error("getDocumentiTemplateByCarica", e);
			throw new DataException("Cannot load getDocumentiTemplateByCarica");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return documenti;
	}

	public int contaDocumentiTemplateByCarica(int caricaId, int tipo)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int c = 0;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(COUNT_DOC_BY_CARICA);
			pstmt.setInt(1, caricaId);
			pstmt.setInt(2, tipo);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				c = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("countDocumentiTemplateByCarica", e);
			throw new DataException(
					"Cannot load countDocumentiTemplateByCarica");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return c;
	}
}
