package it.finsiel.siged.dao.jdbc;

import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.integration.RegistroDAO;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;
import it.finsiel.siged.rdb.JDBCManager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class RegistroDAOjdbc implements RegistroDAO {
	static Logger logger = Logger.getLogger(RegistroDAOjdbc.class.getName());

	private JDBCManager jdbcMan = new JDBCManager();

	public RegistroVO newRegistro(RegistroVO registro) throws DataException {
		Connection connection = null;
		RegistroVO reg = new RegistroVO();
		try {
			connection = jdbcMan.getConnection();
			reg = newRegistro(connection, registro);
		} catch (Exception e) {
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(connection);
		}
		return reg;
	}

	public RegistroVO newRegistro(Connection connection, RegistroVO registro)
			throws DataException {

		RegistroVO newRegistro = new RegistroVO();
		PreparedStatement pstmt = null;
		int recIns;
		try {
			if (connection == null) {
				logger
						.warn("newRegistro() - Invalid Connection :"
								+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(INSERT_REGISTRO);
			pstmt.setInt(1, registro.getId().intValue());
			pstmt.setString(2, registro.getCodRegistro());
			pstmt.setString(3, registro.getDescrizioneRegistro());
			if (registro.getDataAperturaRegistro() != null) {
				pstmt.setDate(4, new Date(registro.getDataAperturaRegistro()
						.getTime()));
			} else {
				pstmt.setDate(4, new Date(System.currentTimeMillis()));
			}
			pstmt.setInt(5, registro.getApertoUscita() ? 1 : 0);
			pstmt.setInt(6, registro.getApertoIngresso() ? 1 : 0);
			pstmt.setInt(7, registro.getUfficiale() ? 1 : 0);
			pstmt.setInt(8, registro.getDataBloccata() ? 1 : 0);
			pstmt.setInt(9, registro.getAooId());
			pstmt.setString(10, registro.getRowUpdatedUser());
			if (registro.getRowUpdatedTime() != null) {
				pstmt.setDate(11, new Date(registro.getRowUpdatedTime()
						.getTime()));
			} else {
				pstmt.setDate(11, new Date(System.currentTimeMillis()));
			}
			recIns = pstmt.executeUpdate();
			logger.info("Numero Registri inseriti:" + recIns);
		} catch (Exception e) {
			logger.error("Save RegistroVO", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}

		newRegistro = getRegistro(connection, registro.getId().intValue());
		newRegistro.setReturnValue(ReturnValues.SAVED);
		return newRegistro;

	}

	private RegistroVO getRegistro(RegistroVO reg, ResultSet rs)
			throws SQLException {
		reg.setReturnValue(ReturnValues.UNKNOWN);
		reg.setId(rs.getString("registro_id"));
		reg.setCodRegistro(rs.getString("codi_registro"));
		reg.setDescrizioneRegistro(rs.getString("desc_registro"));
		reg.setNumUltimoProgressivo(rs.getInt("nume_ultimo_progressivo"));
		reg.setNumUltimoProgrInterno(rs.getInt("nume_ultimo_progr_interno"));
		reg.setNumUltimoFascicolo(rs.getInt("nume_ultimo_fascicolo"));
		reg.setDataAperturaRegistro(rs.getDate("data_apertura_registro"));
		reg.setApertoUscita(rs.getBoolean("flag_aperto_uscita"));
		reg.setApertoIngresso(rs.getBoolean("flag_aperto_ingresso"));
		reg.setUfficiale(rs.getBoolean("flag_ufficiale"));
		reg.setDataBloccata(rs.getBoolean("flag_data_bloccata"));
		reg.setAooId(rs.getInt("aoo_id"));
		reg.setRowUpdatedUser(rs.getString("row_updated_user"));
		reg.setRowUpdatedTime(rs.getDate("row_updated_time"));
		reg.setVersione(rs.getInt("versione"));
		reg.setReturnValue(ReturnValues.FOUND);

		return reg;
	}

	public RegistroVO getRegistro(int id) throws DataException {
		RegistroVO rVo = new RegistroVO();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			rVo = getRegistro(connection, id);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " registroId :" + id);
		} finally {
			jdbcMan.close(connection);
		}
		return rVo;
	}

	public RegistroVO getRegistroByCodAndAooId(String cod, int aooId)
			throws DataException {
		RegistroVO rVo = new RegistroVO();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			rVo = getRegistroByCodAndAooId(connection, cod, aooId);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " aooId :" + aooId+" cod:"+cod);
		} finally {
			jdbcMan.close(connection);
		}
		return rVo;
	}

	public boolean isScartato(int protocolloId) throws DataException {
		Connection connection = null;
		boolean scartato = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			if (connection == null) {
				logger.warn("isScartato() - Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection
					.prepareStatement("SELECT count(*) from storia_protocolli"
							+ " where flag_scarto='1' AND protocollo_id=?");
			pstmt.setInt(1, protocolloId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt(1) > 0)
					scartato = true;
			}

		} catch (Exception e) {
			logger.error("Load isScartato by ID", e);
			throw new DataException("Cannot load isScartato by ID");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return scartato;
	}

	private RegistroVO getRegistro(Connection connection, int id)
			throws DataException {
		RegistroVO reg = new RegistroVO();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			if (connection == null) {
				logger
						.warn("getRegistro() - Invalid Connection :"
								+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_REGISTRO_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				reg = getRegistro(reg, rs);
			} else {
				reg.setReturnValue(ReturnValues.NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("Update RegistroVO", e);
			throw new DataException("Cannot update Registro");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return reg;
	}

	private RegistroVO getRegistroByCodAndAooId(Connection connection,
			String cod, int aooId) throws DataException {
		RegistroVO reg = new RegistroVO();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			if (connection == null) {
				logger.warn("getRegistro() - Invalid Connection :"+ connection);
				throw new DataException("Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_REGISTRO_BY_COD_AND_AOOID);
			pstmt.setString(1, cod);
			pstmt.setInt(2, aooId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				reg = getRegistro(reg, rs);
			} else {
				reg.setReturnValue(ReturnValues.NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("getRegistroByCodAndAooId", e);
			throw new DataException("Cannot get Registro By Cod And AooId");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return reg;
	}

	public Collection<RegistroVO> getRegistri() throws DataException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<RegistroVO> registri = new ArrayList<RegistroVO>();
		try {
			connection = jdbcMan.getConnection();
			stmt = connection.prepareStatement(SELECT_REGISTRI);
			rs = stmt.executeQuery();

			while (rs.next()) {
				registri.add(getRegistro(new RegistroVO(), rs));
			}
		} catch (Exception e) {
			logger.error("getRegistri", e);
			throw new DataException("Cannot load Registri");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(stmt);
			jdbcMan.close(connection);
		}
		return registri;
	}

	public Collection<RegistroVO> getRegistriByAooId(int aooId) throws DataException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<RegistroVO> registri = new ArrayList<RegistroVO>();
		try {
			connection = jdbcMan.getConnection();
			stmt = connection.prepareStatement(SELECT_REGISTRI_BY_AOO_ID);
			stmt.setInt(1, aooId);
			rs = stmt.executeQuery();

			while (rs.next()) {
				registri.add(getRegistro(new RegistroVO(), rs));
			}
		} catch (Exception e) {
			logger.error("getRegistri", e);
			throw new DataException("Cannot load Registri");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(stmt);
			jdbcMan.close(connection);
		}
		return registri;
	}

	private void salvaStoriaRegistro(Connection connection, RegistroVO reg)
			throws DataException {
		final String SALVA_STORIA = "INSERT INTO storia_registri"
				+ " SELECT * FROM registri WHERE registro_id = ?";
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("storia - Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SALVA_STORIA);
			pstmt.setInt(1, reg.getId().intValue());
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("Insert storia RegistroVO", e);
			throw new DataException("Cannot insert Storia Registro");

		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public RegistroVO aggiornaStatoRegistro(RegistroVO reg)
			throws DataException {
		final String UPDATE_STATO_REGISTRO = "UPDATE registri SET "
				+ " data_apertura_registro = ?, "
				+ " flag_aperto_ingresso = ?, flag_aperto_uscita = ?,"
				+ " flag_data_bloccata = ?, "
				+ " row_updated_user = ?, row_updated_time = ?, "
				+ " versione = versione + 1"
				+ " WHERE registro_id = ? AND versione = ?";
		Connection connection = null;
		reg.setReturnValue(ReturnValues.UNKNOWN);
		PreparedStatement pstmt = null;
		try {
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			salvaStoriaRegistro(connection, reg);
			pstmt = connection.prepareStatement(UPDATE_STATO_REGISTRO);
			pstmt.setDate(1, new Date(reg.getDataAperturaRegistro().getTime()));
			pstmt.setInt(2, reg.getApertoIngresso() ? 1 : 0);
			pstmt.setInt(3, reg.getApertoUscita() ? 1 : 0);
			pstmt.setInt(4, reg.getDataBloccata() ? 1 : 0);
			pstmt.setString(5, reg.getRowUpdatedUser());
			if (reg.getRowUpdatedTime() != null) {
				pstmt.setDate(6, new Date(reg.getRowUpdatedTime().getTime()));
			} else {
				pstmt.setNull(6, Types.DATE);
			}
			pstmt.setInt(7, reg.getId().intValue());
			pstmt.setInt(8, reg.getVersione());
			int n = pstmt.executeUpdate();
			if (n == 0) {
				reg.setReturnValue(ReturnValues.OLD_VERSION);
				throw new DataException(
						"Cannot update the RegistroVO: version error");
			}
			reg.setVersione(reg.getVersione() + 1);
			reg.setReturnValue(ReturnValues.SAVED);
			connection.commit();
		} catch (Exception e) {
			logger.error("Update RegistroVO", e);
			throw new DataException("Cannot update the RegistroVO");

		} finally {
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return reg;
	}

	public RegistroVO updateRegistro(RegistroVO rg) throws DataException {
		Connection connection = null;
		RegistroVO newrg = new RegistroVO();
		try {
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			newrg = updateRegistro(connection, rg);
			connection.commit();
		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.error("getRegistro:", e);
			throw new DataException("error.database.missing");
		} finally {
			jdbcMan.close(connection);
		}
		return newrg;
	}

	/*
	 * Il metodo ha il compito di aggiornare il numero della versione del
	 * RegistroVO. Per fare ci?, onde evitare i problemi in fase di inserimento
	 * dovuti a ci? che vi ? nel DB e ci? che ? ancora in memoria, si deve
	 * eseguire il confronto tra la versione che si sta inserendo e quella
	 * precedente. Tale confronto ? necessario per aggiornare cos? l'elemento
	 * corretto.
	 */

	public RegistroVO updateRegistro(Connection connection, RegistroVO reg)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger
						.warn("newRegistro() - Invalid Connection :"
								+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(UPDATE_REGISTRO);
			pstmt.setString(1, reg.getCodRegistro());
			pstmt.setString(2, reg.getDescrizioneRegistro());
			pstmt.setInt(3, reg.getUfficiale() ? 1 : 0);
			pstmt.setInt(4, reg.getDataBloccata() ? 1 : 0);
			pstmt.setString(5, reg.getRowUpdatedUser());
			pstmt.setDate(6, new Date(reg.getRowUpdatedTime().getTime()));
			pstmt.setInt(7, reg.getId().intValue());
			pstmt.setInt(8, reg.getVersione());
			if (pstmt.executeUpdate() == 0) {
				throw new DataException("Cannot update the RegistroVO");
			}
			reg = getRegistro(connection, reg.getId().intValue());

		} catch (Exception e) {
			logger.error("Load RegistroVO", e);
			throw new DataException("Cannot update the RegistroVO");

		} finally {
			jdbcMan.close(pstmt);
		}

		return reg;

	}

	public Collection<Integer> getRegistriUtente(int utenteId) throws DataException {
		ArrayList<Integer> registri = new ArrayList<Integer>();

		// inserisci codice
		Connection connection = null;
		PreparedStatement pstmt = null;

		ResultSet rs = null;
		try {

			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_REGISTRI_BY_UTENTE_ID);
			pstmt.setInt(1, utenteId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				registri.add(new Integer(rs.getInt("registro_id")));
			}

		} catch (Exception e) {
			logger.error("Load RegistroId", e);
			throw new DataException("Cannot load the RegistroId");

		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}

		return registri;
	}

	public void setDataAperturaRegistro(int registroId, long data)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(UPDATE_DATA_APERTURA_REGISTRO);
			pstmt.setDate(1, new Date(data));
			pstmt.setInt(2, registroId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("Load RegistroId", e);
			throw new DataException("Cannot load the RegistroId");

		} finally {
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
	}

	public Collection<Integer> getUtentiNonUtilizzatiInRegistro(int registroId,
			String cognome) throws DataException {
		ArrayList<Integer> utenti = new ArrayList<Integer>();

		// inserisci codice
		Connection connection = null;
		PreparedStatement pstmt = null;

		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			String strQuery = SELECT_UTENTI_NON_UTILIZZATI_IN_REGISTRO_ID;
			if (cognome != null && !"".equals(cognome.trim())) {
				strQuery = strQuery + " AND cognome=?";
			}
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, registroId);
			if (cognome != null && !"".equals(cognome.trim())) {
				pstmt.setString(2, cognome);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				utenti.add(new Integer(rs.getInt("utente_id")));
			}

		} catch (Exception e) {
			logger.error("Load getUtentiRegistro", e);
			throw new DataException("Cannot load the getUtentiRegistro");

		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}

		return utenti;
	}

	public Map<Integer,Integer> getUtentiRegistro(int registroId) throws DataException {
		Map<Integer,Integer> utenti = new HashMap<Integer,Integer>();

		// inserisci codice
		Connection connection = null;
		PreparedStatement pstmt = null;

		ResultSet rs = null;
		try {

			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_UTENTI_REGISTRO_ID);
			pstmt.setInt(1, registroId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				utenti.put(new Integer(rs.getInt("utente_id")), new Integer(rs
						.getInt("utente_id")));
			}

		} catch (Exception e) {
			logger.error("Load getUtentiRegistro", e);
			throw new DataException("Cannot load the getUtentiRegistro");

		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}

		return utenti;
	}

	public void cancellaPermessiRegistroUtente(Connection conn, int registroId,
			String utenti) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (conn == null) {
				logger
						.warn("cancellaPermessiRegistroUtente - Invalid Connection :"
								+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			// cancello tutti i permessi utente
			String sqlStr = "DELETE FROM utenti$registri WHERE registro_id=? and utente_id IN "
					+ utenti;
			pstmt = conn.prepareStatement(sqlStr);
			pstmt.setInt(1, registroId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("cancellaPermessiRegistroUtente", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	/*
	 * Il metodo calcola il numero totale di protocolli del registro impostato e
	 * tali che l'utente � protocollatore, mittente oppure assegnatario
	 */

	public int getCountProtocolliUtenteRegistro(String utenti, int registro)
			throws DataException {
		int total = 0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			String strQuery = "select COUNT(protocollo_id) from protocolli p	where registro_id=?	and "
					+ "EXISTS (SELECT * FROM protocollo_assegnatari ass WHERE ass.protocollo_id=p.protocollo_id "
					+ "AND (ass.utente_assegnatario_id IN "
					+ utenti
					+ " OR p.utente_protocollatore_id IN "
					+ utenti
					+ " OR p.utente_mittente_id IN " + utenti + "))";

			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, registro);
			rs = pstmt.executeQuery();
			rs.next();
			total = rs.getInt(1);

		} catch (Exception e) {
			logger.error("Load getCountProtocolliUtenteRegistro", e);
			throw new DataException(
					"Cannot load getCountProtocolliUtenteRegistro");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return total;
	}

	public boolean cancellaRegistro(int registroId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean cancellato = false;
		int conta = 0;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_UTENTI_BY_REGISTRO_ID);
			pstmt.setInt(1, registroId);
			rs = pstmt.executeQuery();
			rs.next();
			conta = rs.getInt(1);
			if (conta == 0) {

				pstmt = connection.prepareStatement(DELETE_REGISTRO_BY_ID);
				pstmt.setInt(1, registroId);
				pstmt.executeUpdate();
				cancellato = true;
			}

		} catch (Exception e) {
			logger.error("Load RegistroId", e);
			throw new DataException("Cannot load the RegistroId");

		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return cancellato;
	}

	public boolean isAbilitatoRegistro(int registroId, int ufficioId,
			int utenteId) throws DataException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean abilitato = false;
		try {
			connection = jdbcMan.getConnection();
			StringBuffer strB = new StringBuffer(
					"select count(*) from utenti$registri where registro_id=? AND ");
			strB
					.append("utente_id in (select distinct utente_id from cariche where ufficio_id=?");

			if (utenteId > 0) {
				strB.append(" AND utente_id=? AND flag_attivo=1");
			}

			strB.append(")");
			stmt = connection.prepareStatement(strB.toString());

			stmt.setInt(1, registroId);
			stmt.setInt(2, ufficioId);
			if (utenteId > 0) {
				stmt.setInt(3, utenteId);
			}

			rs = stmt.executeQuery();

			if (rs.next()) {
				if (rs.getInt(1) > 0)
					abilitato = true;
			}
		} catch (Exception e) {
			logger.error("isAbilitatoRegistro", e);
			throw new DataException("Cannot load isAbilitatoRegistro");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(stmt);
			jdbcMan.close(connection);
		}
		return abilitato;
	}

	public boolean esisteRegistroUfficialeByAooId(int registroInModifica,
			int aooId) throws DataException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean trovato = false;
		try {
			connection = jdbcMan.getConnection();
			stmt = connection
					.prepareStatement("SELECT * FROM registri WHERE registro_id != ? and aoo_id=? AND flag_ufficiale=1");
			stmt.setInt(1, registroInModifica);
			stmt.setInt(2, aooId);
			rs = stmt.executeQuery();

			if (rs.next()) {
				if (rs.getInt(1) > 0)
					trovato = true;
			}
		} catch (Exception e) {
			logger.error("esisteRegistroUfficialeByAooId", e);
			throw new DataException(
					"Cannot load esisteRegistroUfficialeByAooId");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(stmt);
			jdbcMan.close(connection);
		}
		return trovato;
	}

	public boolean esisteCodiceRegistro(int id, String cod, int aooId)
			throws DataException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean trovato = false;
		try {
			connection = jdbcMan.getConnection();
			stmt = connection
					.prepareStatement("SELECT * FROM registri WHERE registro_id != ? and aoo_id=? AND codi_registro=?");
			stmt.setInt(1, id);
			stmt.setInt(2, aooId);
			stmt.setString(3, cod);
			rs = stmt.executeQuery();

			if (rs.next()) {
				if (rs.getInt(1) > 0)
					trovato = true;
			}
		} catch (Exception e) {
			logger.error("esisteCodiceRegistro", e);
			throw new DataException("Cannot load esisteCodiceRegistro");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(stmt);
			jdbcMan.close(connection);
		}
		return trovato;
	}

	private final static String DELETE_REGISTRO_BY_ID = "DELETE FROM registri WHERE registro_id = ?";

	private final static String SELECT_UTENTI_BY_REGISTRO_ID = "select count(utente_id) "
			+ " from utenti$registri where registro_id = ? ";

	private final static String UPDATE_DATA_APERTURA_REGISTRO = "UPDATE registri SET "
			+ " data_apertura_registro = ? WHERE registro_id = ?";

	// private final static String UPDATE_STATO_REGISTRO = "UPDATE registri SET
	// ("
	// + " data_apertura_registro = ?, flag_aperto_ingresso = ?,
	// flag_aperto_uscita = ?,"
	// + " flag_data_bloccata = ?, row_updated_user = ?, row_updated_time = ?,
	// versione = versione + 1"
	// + " WHERE registro_id = ? AND versione = ?";

	private final static String UPDATE_REGISTRO = " UPDATE registri SET "
			+ " codi_registro = ?, desc_registro = ?, flag_ufficiale = ?, flag_data_bloccata = ?, "
			+ "row_updated_user = ?, row_updated_time = ?, versione = versione + 1 "
			+ " WHERE registro_id = ? AND versione = ?";

	private final static String INSERT_REGISTRO = "INSERT INTO registri ("
			+ " registro_id, codi_registro, desc_registro, data_apertura_registro,"
			+ " flag_aperto_uscita, flag_aperto_ingresso, flag_ufficiale, flag_data_bloccata, aoo_id,"
			+ " row_updated_user, row_updated_time) "
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private final static String SELECT_REGISTRI = "SELECT * FROM registri";

	private final static String SELECT_REGISTRI_BY_AOO_ID = "SELECT * FROM registri WHERE aoo_id=?";

	private final static String SELECT_REGISTRO_BY_ID = "SELECT * FROM registri WHERE registro_id = ?";
	
	private final static String SELECT_REGISTRO_BY_COD_AND_AOOID = "SELECT * FROM registri WHERE codi_registro=? AND aoo_id = ?";

	
	private final static String SELECT_REGISTRI_BY_UTENTE_ID = "select registro_id "
			+ " from utenti$registri " + " where utente_id = ? ";

	private final static String SELECT_UTENTI_REGISTRO_ID = "select distinct utente_id "
			+ " from utenti$registri " + " where registro_id = ? ";

	private final static String SELECT_UTENTI_NON_UTILIZZATI_IN_REGISTRO_ID = ""
			+ "select utente_id from utenti where utente_id not in (select utente_id from "
			+ "utenti$registri where registro_id = ?)";

};