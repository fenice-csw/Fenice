package it.compit.fenice.mvc.dao.jdbc;

import it.compit.fenice.mvc.integration.CaricaDAO;
import it.compit.fenice.mvc.presentation.helper.VersioneCaricaView;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.vo.organizzazione.PermessoMenuVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.finsiel.siged.util.DateUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

public class CaricaDAOjdbc implements CaricaDAO {
	
	static Logger logger = Logger.getLogger(CaricaDAOjdbc.class.getName());

	private JDBCManager jdbcMan = new JDBCManager();

	private static final String SELECT_CARICA = "SELECT c.carica_id, c.denominazione, c.profilo_id, c.ufficio_id,c.utente_id,c.row_created_user,c.versione,c.flag_attivo,c.flag_dirigente,c.flag_referente,c.flag_responsabile_ente,c.flag_referente_ufficio_protocollo,c.row_created_time FROM cariche c WHERE c.carica_id=?";

	private static final String SELECT_STORIA_CARICA = "SELECT c.carica_id,c.versione,c.flag_attivo,c.row_created_time,c.row_created_user,c.flag_dirigente,c.flag_referente,c.flag_responsabile_ente,c.denominazione,p.desc_profilo AS desc_profilo,uff.descrizione AS desc_ufficio,ut.cognome||' '||ut.nome as desc_utente FROM storia_cariche c LEFT JOIN uffici uff ON (uff.ufficio_id=c.ufficio_id) LEFT JOIN utenti ut ON (ut.utente_id=c.utente_id) LEFT JOIN profili p ON (p.profilo_id=c.profilo_id) WHERE c.carica_id =? ORDER BY c.versione desc";

	private static final String SELECT_CARICA_BY_UFFICIO_UTENTE = "SELECT c.carica_id, c.denominazione, c.profilo_id, c.ufficio_id,c.utente_id,c.row_created_user,c.versione,c.flag_attivo,c.flag_dirigente,c.flag_referente,c.flag_responsabile_ente,c.flag_referente_ufficio_protocollo FROM cariche c WHERE c.utente_id=? AND c.ufficio_id=? AND c.flag_attivo=1";

	private static final String SELECT_FLAG_ATTIVO_BY_UFFICIO_AND_UTENTE = "SELECT flag_attivo  WHERE utente_id=? AND ufficio_id=?";

	private static final String INSERT_CARICA = "INSERT INTO cariche(carica_id, denominazione, ufficio_id, profilo_id,utente_id,row_created_user,flag_attivo) VALUES (?, ?, ?, ?, ?, ?, ?)";

	private final static String DELETE_CARICHE = "DELETE FROM cariche WHERE carica_id=?";

	private final static String DELETE_STORIA_CARICHE = "DELETE FROM storia_cariche WHERE carica_id=?";

	private final static String DELETE_DOC_CARICHE = "DELETE FROM doc_cartelle WHERE carica_id=?";

	private static final String CHECK_DENOMINAZIONE_UFFICIO = "SELECT COUNT(*) FROM cariche WHERE denominazione=? AND ufficio_id=? AND NOT carica_id=? AND flag_attivo=1";

	private static final String UPDATE_USER_PROFILE = "UPDATE cariche SET denominazione=?, ufficio_id=?, profilo_id=?, utente_id=?, row_created_user=?, flag_attivo=?,row_created_time=now() WHERE carica_id=?";

	private static final String UPDATE_RESPONSABILE_ENTE = "UPDATE cariche SET flag_responsabile_ente=1,row_created_time=now() WHERE carica_id=?";
	
	private static final String UPDATE_RESPONSABILE_UFFICIO_PROTOCOLLO = "UPDATE cariche SET flag_referente_ufficio_protocollo=1, row_created_time=now() WHERE carica_id=?";
	
	private static final String REMOVE_RESPONSABILE_ENTE = "UPDATE cariche SET flag_responsabile_ente=0";
	
	private static final String REMOVE_RESPONSABILE_UFFICIO_PROTOCOLLO = "UPDATE cariche SET flag_referente_ufficio_protocollo=0";
	
	private static final String SELECT_CARICHE = "SELECT c.* FROM cariche c ";

	private static final String ASSEGNATI_CARICA = "SELECT count (p.protocollo_id) FROM PROTOCOLLI p, PROTOCOLLO_ASSEGNATARI a "
			+ "WHERE registro_id IN (?,?) AND p.protocollo_id =a.protocollo_id AND carica_assegnatario_id=? AND flag_competente=1 AND check_lavorato=0 AND NOT stato_protocollo IN ('C','P') AND ANNO_REGISTRAZIONE>=?  AND ANNO_REGISTRAZIONE<=?";
	
	private static final String COUNT_FASCICOLI_ALERT = "select count(f.fascicolo_id) from fascicoli f where f.giorni_max-date_part('day',f.data_apertura)<=f.giorni_alert AND f.carica_responsabile_id=? AND f.stato=0";

	public void cancellaPermessi(Connection conn, int caricaId)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (conn == null) {
				logger.warn("cancellaPermessiCarica - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			String sqlStr = "DELETE FROM permessi_carica WHERE carica_id=?";
			pstmt = conn.prepareStatement(sqlStr);
			pstmt.setInt(1, caricaId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("cancellaPermessiCarica", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}

	}

	public CaricaVO getCarica(int id) throws DataException {
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			return getCarica(connection, id);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " id :" + id);
		} finally {
			jdbcMan.close(connection);
		}
	}

	public Collection<VersioneCaricaView> getStoriaCarica(int caricaId) throws DataException {
		ArrayList<VersioneCaricaView> storiaCarica = new ArrayList<VersioneCaricaView>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_STORIA_CARICA);
			pstmt.setInt(1, caricaId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				VersioneCaricaView v = new VersioneCaricaView();
				v.setCaricaId(rs.getInt("carica_id"));
				v.setVersione(rs.getInt("versione"));
				v.setCarica(rs.getString("denominazione"));
				v.setUfficio(rs.getString("desc_ufficio"));
				v.setProfilo(rs.getString("desc_profilo"));
				v.setUtente(rs.getString("desc_utente"));
				v.setAttivo(rs.getBoolean("flag_attivo"));
				v.setReferente(rs.getBoolean("flag_referente"));
				v.setDirigente(rs.getBoolean("flag_dirigente"));
				v.setResponsabileEnte(rs.getBoolean("flag_responsabile_ente"));
				v.setDataOperazione(DateUtil.formattaDataOra(rs.getTimestamp(
						"row_created_time").getTime()));
				v.setAutore(rs.getString("row_created_user"));
				storiaCarica.add(v);
			}
		} catch (Exception e) {
			logger.error("getStoriaCarica", e);
			throw new DataException("Cannot load getStoriaCarica");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return storiaCarica;

	}

	private CaricaVO getCarica(Connection connection, int id)
			throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CaricaVO c = new CaricaVO();
		c.setReturnValue(ReturnValues.UNKNOWN);
		try {
			if (connection == null) {
				logger.warn("getCarica() - Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_CARICA);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				c.setCaricaId(rs.getInt(1));
				c.setNome(rs.getString(2));
				c.setProfiloId(rs.getInt(3));
				c.setUfficioId(rs.getInt(4));
				c.setUtenteId(rs.getInt(5));
				c.setRowCreatedUser(rs.getString(6));
				c.setVersione(rs.getInt(7));
				c.setAttivo(rs.getBoolean(8));
				c.setDirigente(rs.getBoolean(9));
				c.setReferente(rs.getBoolean(10));
				c.setResponsabileEnte(rs.getBoolean(11));
				c.setResponsabileUfficioProtocollo(rs.getBoolean(12));
				c.setRowCreatedTime(rs.getTimestamp(13));
				c.setReturnValue(ReturnValues.FOUND);
			} else {
				c.setReturnValue(ReturnValues.NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("Load Carica ", e);
			throw new DataException("Cannot load the carica");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}

		return c;
	}

	public CaricaVO newCarica(Connection connection, CaricaVO caricaVO)
			throws DataException {
		PreparedStatement pstmt = null;
		if (isNameUsed(connection, caricaVO.getNome(), caricaVO.getUfficioId(),
				caricaVO.getCaricaId()))
			throw new DataException("denominazione_gia_utilizzata");
		try {
			if (connection == null) {
				logger.warn("newCarica - Invalid Connection :" + connection);
				throw new DataException( "Connessione alla base dati non valida." );
			}
			pstmt = connection.prepareStatement(INSERT_CARICA);
			pstmt.setInt(1, caricaVO.getCaricaId());
			pstmt.setString(2, caricaVO.getNome());
			pstmt.setInt(3, caricaVO.getUfficioId());
			if (caricaVO.getProfiloId() == null || caricaVO.getProfiloId() == 0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4, caricaVO.getProfiloId());
			if (caricaVO.getUtenteId() == null || caricaVO.getUtenteId() == 0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5, caricaVO.getUtenteId());
			pstmt.setString(6, caricaVO.getRowCreatedUser());
			pstmt.setInt(7, caricaVO.isAttivo() ? 1 : 0);
			pstmt.executeUpdate();

		} catch (Exception e) {

			logger.error("Save CARICA Profile", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		CaricaVO newVO = getCarica(connection, caricaVO.getCaricaId());
		newVO.setReturnValue(ReturnValues.SAVED);
		return newVO;
	}

	private boolean isNameUsed(Connection connection, String nome,
			int ufficioId, int caricaId) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean used = true;
		try {
			if (connection == null) {
				logger.warn("isUsernameUsed - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(CHECK_DENOMINAZIONE_UFFICIO);
			pstmt.setString(1, nome);
			pstmt.setInt(2, ufficioId);
			pstmt.setInt(3, caricaId);
			rs = pstmt.executeQuery();
			rs.next();
			used = rs.getInt(1) > 0;
		} catch (Exception e) {
			logger.error("isNameUsed:" + nome, e);
			throw new DataException("error.database.missing");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return used;
	}

	public void nuovoPermessoMenu(Connection conn, PermessoMenuVO permesso)
			throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (conn == null) {
				logger.warn("permessiCariche - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			String check = "SELECT carica_id FROM permessi_carica WHERE carica_id=? AND menu_id=?;";
			pstmt = conn.prepareStatement(check);
			pstmt.setInt(1, permesso.getCaricaId());
			pstmt.setInt(2, permesso.getMenuId());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return;
			}
			jdbcMan.close(pstmt);
			String sqlStr = "INSERT INTO permessi_carica "
					+ " (carica_id,menu_id) VALUES (?, ?)";
			pstmt = conn.prepareStatement(sqlStr);			
			pstmt.setInt(1, permesso.getCaricaId());
			pstmt.setInt(2, permesso.getMenuId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			logger.error("permessiUtente", e);
		} finally {
			jdbcMan.close(pstmt);
		}

	}

	public CaricaVO updateCarica(Connection connection, CaricaVO caricaVO)
			throws DataException {
		PreparedStatement pstmt = null;
		if (isNameUsed(connection, caricaVO.getNome(), caricaVO.getUfficioId(),
				caricaVO.getCaricaId()))
			throw new DataException("username_gia_utilizzato");
		try {
			if (connection == null) {
				logger.warn("updateUtenteVO - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			archiviaVersione(connection, caricaVO.getCaricaId());
			pstmt = connection.prepareStatement(UPDATE_USER_PROFILE);
			pstmt.setString(1, caricaVO.getNome());
			pstmt.setInt(2, caricaVO.getUfficioId());
			if (caricaVO.getProfiloId() == null || caricaVO.getProfiloId() == 0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3, caricaVO.getProfiloId());
			if (caricaVO.getUtenteId() == null || caricaVO.getUtenteId() == 0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4, caricaVO.getUtenteId());
			pstmt.setString(5, caricaVO.getRowCreatedUser());
			pstmt.setInt(6, caricaVO.isAttivo() ? 1 : 0);
			pstmt.setInt(7, caricaVO.getCaricaId());
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Save Utente Profile", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		CaricaVO newVO = getCarica(connection, caricaVO.getCaricaId());
		newVO.setReturnValue(ReturnValues.SAVED);
		return newVO;
	}
	
	public CaricaVO setResponsabileProtocollo(Connection connection, int caricaId)
			throws DataException {
		PreparedStatement pstmt = null;
		CaricaVO newVO=new CaricaVO();
		newVO.setReturnValue(ReturnValues.UNKNOWN);
		try {
			if (connection == null) {
				logger.warn("removeResponsabileProtocollo - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			archiviaVersione(connection, caricaId);
			pstmt = connection.prepareStatement(UPDATE_RESPONSABILE_UFFICIO_PROTOCOLLO);
			pstmt.setInt(1, caricaId);
			pstmt.executeUpdate();
			newVO = getCarica(connection, caricaId);
			newVO.setReturnValue(ReturnValues.SAVED);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("setResponsabileProtocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return newVO;
	}

	public CaricaVO setResponsabileEnte(int caricaId)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		CaricaVO newVO=new CaricaVO();
		newVO.setReturnValue(ReturnValues.UNKNOWN);
		try {
			connection = jdbcMan.getConnection();
			archiviaVersione(connection, caricaId);
			pstmt = connection.prepareStatement(UPDATE_RESPONSABILE_ENTE);
			pstmt.setInt(1, caricaId);
			pstmt.executeUpdate();
			newVO = getCarica(connection, caricaId);
			newVO.setReturnValue(ReturnValues.SAVED);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("setResponsabileEnte", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return newVO;
	}
	
	public boolean removeResponsabileEnte()
			throws DataException {
		boolean saved=false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(REMOVE_RESPONSABILE_ENTE);
			pstmt.executeUpdate();
			saved=true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("removeResponsabileEnte", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return saved;
	}
	
	public boolean removeResponsabileProtocollo(Connection connection)
			throws DataException {
		boolean saved=false;
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("removeResponsabileProtocollo - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(REMOVE_RESPONSABILE_UFFICIO_PROTOCOLLO);
			pstmt.executeUpdate();
			saved=true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("removeResponsabileProtocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return saved;
	}
	
	private void archiviaVersione(Connection connection, int caricaId)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("storia - Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			String sql = "INSERT INTO storia_cariche SELECT * FROM cariche"
					+ " WHERE carica_id = ?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, caricaId);
			pstmt.executeUpdate();
			jdbcMan.close(pstmt);
			sql = "UPDATE cariche SET versione = versione+1 WHERE carica_id = ?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, caricaId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("storia carica" + caricaId, e);
			throw new DataException("Cannot insert Storia Carica");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public Collection<CaricaVO> getCariche() throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CaricaVO c = null;
		ArrayList<CaricaVO> cariche = new ArrayList<CaricaVO>();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_CARICHE);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				c = new CaricaVO();
				c.setCaricaId(rs.getInt("carica_id"));
				c.setProfiloId(rs.getInt("profilo_id"));
				c.setUtenteId(rs.getInt("utente_id"));
				c.setUfficioId(rs.getInt("ufficio_id"));
				c.setNome(rs.getString("denominazione"));
				c.setRowCreatedTime(rs.getDate("row_created_time"));
				c.setRowCreatedUser(rs.getString("row_created_user"));
				c.setVersione(rs.getInt("versione"));
				c.setAttivo(rs.getBoolean("flag_attivo"));
				c.setReferente(rs.getBoolean("flag_referente"));
				c.setDirigente(rs.getBoolean("flag_dirigente"));
				c.setResponsabileEnte(rs.getBoolean("flag_responsabile_ente"));
				c.setResponsabileUfficioProtocollo(rs.getBoolean("flag_referente_ufficio_protocollo"));
				c.setReturnValue(ReturnValues.FOUND);
				cariche.add(c);
			}
		} catch (Exception e) {
			logger.error("getCariche", e);
			throw new DataException("Cannot load getCariche");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return cariche;
	}

	public CaricaVO getCarica(int utenteId, int ufficioId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CaricaVO c = new CaricaVO();
		c.setReturnValue(ReturnValues.UNKNOWN);
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection
					.prepareStatement(SELECT_CARICA_BY_UFFICIO_UTENTE);
			pstmt.setInt(1, utenteId);
			pstmt.setInt(2, ufficioId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				c.setCaricaId(rs.getInt(1));
				c.setNome(rs.getString(2));
				c.setProfiloId(rs.getInt(3));
				c.setUfficioId(rs.getInt(4));
				c.setUtenteId(rs.getInt(5));
				c.setRowCreatedUser(rs.getString(6));
				c.setVersione(rs.getInt(7));
				c.setAttivo(rs.getBoolean(8));
				c.setDirigente(rs.getBoolean(9));
				c.setReferente(rs.getBoolean(10));
				c.setResponsabileEnte(rs.getBoolean(11));
				c.setResponsabileUfficioProtocollo(rs.getBoolean(12));
				c.setReturnValue(ReturnValues.FOUND);
			} else {
				c.setReturnValue(ReturnValues.NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("Load Carica ", e);
			throw new DataException("Cannot load the carica");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return c;
	}

	public boolean isCaricaAttiva(int uffId, int uteId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection
					.prepareStatement(SELECT_FLAG_ATTIVO_BY_UFFICIO_AND_UTENTE);
			pstmt.setInt(1, uteId);
			pstmt.setInt(2, uffId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				return rs.getBoolean(1);
			}
		} catch (Exception e) {
			logger.error("getCariche", e);
			throw new DataException("Cannot load getCariche");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return true;
	}

	public void cancellaCarica(Connection connection, Integer caricaId)
			throws DataException {
		PreparedStatement pstmt = null;
		CaricaVO c = new CaricaVO();
		c.setReturnValue(ReturnValues.UNKNOWN);
		try {
			if (connection == null) {
				logger.warn("getCarica() - Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(DELETE_DOC_CARICHE);
			pstmt.setInt(1, caricaId);
			pstmt.executeUpdate();
			jdbcMan.close(pstmt);
			pstmt = connection.prepareStatement(DELETE_STORIA_CARICHE);
			pstmt.setInt(1, caricaId);
			pstmt.executeUpdate();
			jdbcMan.close(pstmt);
			pstmt = connection.prepareStatement(DELETE_CARICHE);
			pstmt.setInt(1, caricaId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("cancellaCarica", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public int contaAssegnazioni(CaricaVO carica, int annoProtocolloDa, int annoProtocolloA, Utente utente)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int numeroAssegnati = 0;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(ASSEGNATI_CARICA);
			pstmt.setInt(1, utente.getRegistroInUso());
			pstmt.setInt(2, utente.getRegistroPostaInterna());
			pstmt.setInt(3, carica.getCaricaId());
			pstmt.setInt(4, annoProtocolloDa);
			pstmt.setInt(5, annoProtocolloA);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				numeroAssegnati += rs.getInt(1);
			}
			jdbcMan.closeResultSetAndStatement(rs, pstmt);
			pstmt = connection.prepareStatement(COUNT_FASCICOLI_ALERT);
			pstmt.setInt(1, carica.getCaricaId());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				numeroAssegnati += rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("contaAssegnazioni", e);
			throw new DataException("contaAssegnazioni");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return numeroAssegnati;
	}
}
