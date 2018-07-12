package it.finsiel.siged.dao.jdbc;

import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.compit.fenice.mvc.vo.organizzazione.StoricoOrganigrammaVO;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.integration.UfficioDAO;
import it.finsiel.siged.mvc.presentation.helper.StatisticheView;
import it.finsiel.siged.mvc.presentation.helper.UtenteView;
import it.finsiel.siged.mvc.vo.organizzazione.TitolarioUfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.finsiel.siged.util.DateUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

public class UfficioDAOjdbc implements UfficioDAO {
	static Logger logger = Logger.getLogger(UfficioDAOjdbc.class.getName());

	private JDBCManager jdbcMan = new JDBCManager();

	protected final static String SELECT_CARICHE_BY_UFFICIO = "SELECT c.carica_id, u.utente_id,u.nome,u.cognome,c.denominazione,c.flag_attivo,u.flag_abilitato FROM cariche c LEFT JOIN utenti u ON (u.utente_id=c.utente_id) WHERE c.ufficio_id=?";

	protected final static String SELECT_TITOLARIO_BY_UFFICIO = "SELECT t.path_titolario,t.desc_titolario,t.titolario_id FROM titolario t, titolario$uffici u"
			+ " WHERE t.titolario_id = u.titolario_id AND"
			+ " u.ufficio_id = ? ORDER BY path_titolario, desc_titolario";

	protected final static String SELECT_CARICHE_VO_BY_UFFICIO = "SELECT * FROM cariche WHERE ufficio_id=?";

	protected final static String SELECT_UTENTI_BY_UFFICIO = "SELECT distinct u.*,c.denominazione FROM utenti U, cariche c"
			+ " WHERE U.utente_id=c.utente_id AND c.ufficio_id=? ORDER BY cognome,nome";

	private final static String SELECT_UFFICI_BY_PARENT = "SELECT * FROM uffici" + " WHERE parent_id=?";
	
	private final static String SELECT_UFFICIO_BY_ID = "SELECT * FROM UFFICI WHERE UFFICIO_ID=?";
	
	private final static String SELECT_UFFICIO_BY_DESCRIZIONE = "SELECT * FROM uffici WHERE descrizione=?";

	private final static String IS_UFFICIO_ATTIVO = "SELECT flag_attivo FROM UFFICI WHERE UFFICIO_ID=?";

	private final static String SELECT_UFFICIO_PROTOCOLLI = "SELECT count(protocollo_id) FROM protocolli"
			+ " WHERE ufficio_protocollatore_id=? OR ufficio_mittente_id=?";

	private final static String SELECT_UFFICIO_REFERENTE_FASCICOLI = "SELECT count(fascicolo_id) FROM fascicoli"
			+ " WHERE ufficio_intestatario_id=? OR ufficio_responsabile_id=?";

	private final static String SELECT_UFFICIO_ASSEGNATARI_PROTOCOLLI = "SELECT count(protocollo_id) FROM protocollo_assegnatari"
			+ " WHERE ufficio_assegnante_id=? OR ufficio_assegnatario_id=?";

	private final static String SELECT_PERMESSI_UTENTE = "SELECT count(ufficio_id) FROM cariche"
			+ " WHERE ufficio_id=? AND flag_attivo=1";

	protected final static String SELECT_UFFICI_UTENTE = "SELECT distinct uf.* FROM uffici uf , cariche c WHERE uf.ufficio_id=c.ufficio_id AND c.utente_id=? ";

	protected final static String SELECT_UFFICI = "SELECT distinct uf.* FROM uffici uf ";

	protected final static String SELECT_UFFICI_AOO = "SELECT distinct uf.* FROM uffici uf  WHERE aoo_id=?";

	private final static String INSERT_UFFICIO = "INSERT INTO uffici "
			+ " (ufficio_id, descrizione, parent_id, aoo_id, "
			+ "flag_attivo, tipo,flag_accettazione_automatica, "
			+ "row_created_user, row_updated_user,piano,stanza,indi_email,telefono,fax,email_username,email_password) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	private final static String INSERT_STORICO_ORGANIGRAMMA = "INSERT INTO storico_organigramma(st_org_id, descrizione, data, aoo_id, row_created_user) VALUES (?, ?, ?, ?, ?)";

	private final static String SELECT_ALL_STORICO_ORGANIGRAMMA = "SELECT * FROM storico_organigramma WHERE aoo_id=? ORDER BY row_created_time DESC";
	
	private final static String SELECT_STORICO_ORGANIGRAMMA = "SELECT * FROM storico_organigramma WHERE st_org_id=?";
	
	private final static String UPDATE_UFFICIO = "UPDATE uffici "
			+ " SET descrizione=?, parent_id=?, tipo=?, flag_accettazione_automatica=?, "
			+ "row_updated_user=?, piano=?,stanza=?,indi_email=?,telefono=?,fax=?,email_username=?,email_password=? WHERE ufficio_id=?";

	private final static String UPDATE_ATTIVO = "UPDATE uffici "
			+ " SET flag_attivo=? WHERE ufficio_id=?";
	
	private final static String UPDATE_UFFICIO_PROTOCOLLO = "UPDATE uffici "
			+ " SET flag_ufficio_protocollo=1 WHERE ufficio_id=?";
	
	private final static String REMOVE_UFFICIO_PROTOCOLLO = "UPDATE uffici "
			+ " SET flag_ufficio_protocollo=0";
	
	private final static String DELETE_UFFICIO = "DELETE FROM uffici WHERE ufficio_id=?";

	private final static String DELETE_TITOLARIO_UFFICIO = "DELETE FROM titolario$uffici WHERE ufficio_id=?";

	protected final static String INSERT_REFERENTI_UFFICI = "UPDATE cariche SET flag_referente=1 WHERE carica_id=?";

	protected final static String INSERT_TITOLARIO_UFFICI = "INSERT INTO titolario$uffici (ufficio_id,titolario_id,row_created_time,row_created_user,row_updated_user"
			+ ",row_updated_time,versione) VALUES (?,?,?,?,?,?,?)";

	private final static String SELECT_COUNT_UFFICI = "SELECT count(*) FROM uffici";

	protected final static String CHECK_NAME = "SELECT COUNT(*) FROM uffici WHERE upper(descrizione)=? and ufficio_id!=? and aoo_id=?";

	private static final String ASSEGNATI_UFFICIO = "SELECT count (p.protocollo_id) "
			+ " FROM PROTOCOLLI p, PROTOCOLLO_ASSEGNATARI a "
			+ "WHERE p.protocollo_id =a.protocollo_id AND "
			+ "NOT EXISTS (SELECT * FROM fascicolo_protocolli f WHERE f.protocollo_id=p.protocollo_id)"
			+ "AND ufficio_assegnatario_id =? AND carica_assegnatario_id IS NULL";

	private static final String ASSEGNATI_CARICA = "SELECT count (p.protocollo_id) "
			+ " FROM PROTOCOLLI p, PROTOCOLLO_ASSEGNATARI a "
			+ "WHERE p.protocollo_id =a.protocollo_id AND "
			+ "NOT EXISTS (SELECT * FROM fascicolo_protocolli f WHERE f.protocollo_id=p.protocollo_id AND f.row_created_user=(SELECT user_name FROM utenti WHERE utente_id=?)) "
			+ "AND carica_assegnatario_id=? AND flag_competente=1";

	public final static String UPDATE_DATA_ULTIMA_MAIL_UFFICIO = "UPDATE uffici SET data_ultima_mail=? WHERE ufficio_id=?";

	
	private UfficioVO getUfficioVO(ResultSet rsUfficio) throws SQLException {
		UfficioVO ufficioVO = new UfficioVO();
		ufficioVO.setId(rsUfficio.getInt("ufficio_id"));
		ufficioVO.setAooId(rsUfficio.getInt("aoo_id"));
		ufficioVO.setDescription(rsUfficio.getString("descrizione"));
		ufficioVO.setParentId(rsUfficio.getInt("parent_id"));
		ufficioVO.setAttivo(rsUfficio.getBoolean("flag_attivo"));
		ufficioVO.setUfficioProtocollo(rsUfficio.getBoolean("flag_ufficio_protocollo"));
		ufficioVO.setAccettazioneAutomatica(rsUfficio
				.getBoolean("flag_accettazione_automatica"));
		ufficioVO.setTipo(rsUfficio.getString("tipo"));
		ufficioVO.setVersione(rsUfficio.getInt("versione"));
		ufficioVO.setPiano(rsUfficio.getString("piano"));
		ufficioVO.setStanza(rsUfficio.getString("stanza"));
		ufficioVO.setTelefono(rsUfficio.getString("telefono"));
		ufficioVO.setFax(rsUfficio.getString("fax"));
		ufficioVO.setEmail(rsUfficio.getString("indi_email"));
		ufficioVO.setEmailUsername(rsUfficio.getString("email_username"));
		ufficioVO.setEmailPassword(rsUfficio.getString("email_password"));
		ufficioVO.setDataUltimaMailRicevuta(rsUfficio.getTimestamp("data_ultima_mail"));
		return ufficioVO;
	}

	public void aggiornaUfficioDataUltimaMailRicevuta(Connection connection, int ufficioId,
			java.util.Date dataSpedizione) throws DataException {
		        PreparedStatement pstmt = null;
		       	        try {
		            if (connection == null) {
		                logger.warn("aggiornaAOODataUltimaPecRicevuta() - Invalid Connection:"
		                        + connection);
		                throw new DataException(
		                        "Connessione alla base dati non valida.");
		            }
		            pstmt = connection.prepareStatement(UPDATE_DATA_ULTIMA_MAIL_UFFICIO);
		            pstmt.setTimestamp(1, new Timestamp(dataSpedizione.getTime()));
		        	pstmt.setInt(2, ufficioId);
		        	pstmt.executeUpdate();
		        } catch (Exception e) {
		            logger.error("aggiornaAOODataUltimaPecRicevuta: ", e);
		            throw new DataException("error.database.cannotsave");
		        } finally {
		            jdbcMan.close(pstmt);
		        }
		     
	}
	
	public Collection<StoricoOrganigrammaVO> getStoricoOrganigrammaCollection(int aooId) throws DataException {
		Collection<StoricoOrganigrammaVO> storicoOrganigramma = new ArrayList<StoricoOrganigrammaVO>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_ALL_STORICO_ORGANIGRAMMA);
			pstmt.setInt(1, aooId);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				StoricoOrganigrammaVO vo = new StoricoOrganigrammaVO();
				vo.setId(rs.getInt("st_org_id"));
				vo.setAooId(rs.getInt("aoo_id"));
				vo.setDescrizione(rs.getString("descrizione"));
				vo.setRowCreatedUser(rs.getString("row_created_user"));
				vo.setDataStorico(DateUtil.formattaDataOra(rs.getTimestamp("row_created_time").getTime()));
				storicoOrganigramma.add(vo);
			}
		} catch (Exception e) {
			logger.error("Load getStoricoOrganigrammaCollection", e);
			throw new DataException("Cannot load getStoricoOrganigrammaCollection");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return storicoOrganigramma;
	}
	
	public StoricoOrganigrammaVO getStoricoOrganigramma(int aooId) throws DataException {
		StoricoOrganigrammaVO vo = new StoricoOrganigrammaVO();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_STORICO_ORGANIGRAMMA);
			pstmt.setInt(1, aooId);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				vo.setId(rs.getInt("st_org_id"));
				vo.setAooId(rs.getInt("aoo_id"));
				vo.setDescrizione(rs.getString("descrizione"));
				vo.setRowCreatedUser(rs.getString("row_created_user"));
				vo.setDataStorico(DateUtil.formattaDataOra(rs.getTimestamp("row_created_time").getTime()));
				vo.setOrganigramma((Collection<StatisticheView>) read(rs, "data"));
			}
		} catch (Exception e) {
			logger.error("Load getStoricoOrganigramma", e);
			throw new DataException("Cannot load getStoricoOrganigramma");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return vo;
	}
	
	public void salvaStoricoOrganigramma(Connection conn, Integer id,
			StoricoOrganigrammaVO vo) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (conn == null) {
				logger.warn("salvaStoricoOrganigramma() - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(INSERT_STORICO_ORGANIGRAMMA);
			pstmt.setInt(1, id);
			pstmt.setString(2, vo.getDescrizione());
			write(vo.getOrganigramma(), pstmt, 3);
			pstmt.setInt(4, vo.getAooId());
			pstmt.setString(5, vo.getRowCreatedUser());
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("Save salvaStoricoOrganigramma", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	};

	//
	public static void write(Object obj, PreparedStatement ps,
			int parameterIndex) throws SQLException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oout = new ObjectOutputStream(baos);
		oout.writeObject(obj);
		oout.close();
		ps.setBytes(parameterIndex, baos.toByteArray());
	}

	public static Object read(ResultSet rs, String column) throws SQLException,
			IOException, ClassNotFoundException {
		byte[] buf = rs.getBytes(column);
		if (buf != null) {
			ObjectInputStream objectIn = new ObjectInputStream(
					new ByteArrayInputStream(buf));
			return objectIn.readObject();
		}
		return null;
	}

	//

	public UfficioVO nuovoUfficio(Connection conn, UfficioVO ufficioVO)
			throws DataException {
		PreparedStatement pstmt = null;
		ufficioVO.setReturnValue(ReturnValues.UNKNOWN);
		try {
			if (conn == null) {
				logger.warn("nuovoUfficio() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			if (isDescrizioneUsed(conn, ufficioVO.getDescription(), ufficioVO
					.getId().intValue(), ufficioVO.getAooId())) {
				ufficioVO.setReturnValue(ReturnValues.FOUND);
				logger.warn("descrizione ufficio già utilizzata");
			} else {
				pstmt = conn.prepareStatement(INSERT_UFFICIO);
				pstmt.setInt(1, ufficioVO.getId().intValue());
				pstmt.setString(2, ufficioVO.getDescription());
				if (ufficioVO.getParentId() == 0) {
					pstmt.setNull(3, Types.INTEGER);
				} else {
					pstmt.setInt(3, ufficioVO.getParentId());
				}
				pstmt.setInt(4, ufficioVO.getAooId());
				pstmt.setInt(5, ufficioVO.isAttivo() ? 1 : 0);
				pstmt.setString(6, ufficioVO.getTipo());
				pstmt.setInt(7, ufficioVO.isAccettazioneAutomatica() ? 1 : 0);
				pstmt.setString(8, ufficioVO.getRowCreatedUser());
				pstmt.setString(9, ufficioVO.getRowUpdatedUser());
				pstmt.setString(10, ufficioVO.getPiano());
				pstmt.setString(11, ufficioVO.getStanza());
				pstmt.setString(12, ufficioVO.getEmail());
				pstmt.setString(13, ufficioVO.getTelefono());
				pstmt.setString(14, ufficioVO.getFax());
				pstmt.setString(15, ufficioVO.getEmailUsername());
				pstmt.setString(16, ufficioVO.getEmailPassword());
				pstmt.executeUpdate();
				ufficioVO.setReturnValue(ReturnValues.SAVED);
			}
		} catch (Exception e) {
			logger.error("Save nuovoUfficio", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return ufficioVO;
	}

	public UfficioVO modificaUfficio(Connection conn, UfficioVO ufficioVO)
			throws DataException {
		PreparedStatement pstmt = null;
		ufficioVO.setReturnValue(ReturnValues.UNKNOWN);
		try {
			if (conn == null) {
				logger.warn("modificaUfficio() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			if (isDescrizioneUsed(conn, ufficioVO.getDescription(), ufficioVO
					.getId().intValue(), ufficioVO.getAooId())) {
				ufficioVO.setReturnValue(ReturnValues.FOUND);
				logger.warn("descrizione ufficio gi� utilizzata");
			} else {
				pstmt = conn.prepareStatement(UPDATE_UFFICIO);
				pstmt.setString(1, ufficioVO.getDescription());
				if (ufficioVO.getParentId() == 0) {
					pstmt.setNull(2, Types.INTEGER);
				} else {
					pstmt.setInt(2, ufficioVO.getParentId());
				}
				pstmt.setString(3, ufficioVO.getTipo());
				pstmt.setInt(4, ufficioVO.isAccettazioneAutomatica() ? 1 : 0);
				pstmt.setString(5, ufficioVO.getRowUpdatedUser());
				pstmt.setString(6, ufficioVO.getPiano());
				pstmt.setString(7, ufficioVO.getStanza());
				pstmt.setString(8, ufficioVO.getEmail());
				pstmt.setString(9, ufficioVO.getTelefono());
				pstmt.setString(10, ufficioVO.getFax());
				pstmt.setString(11, ufficioVO.getEmailUsername());
				pstmt.setString(12, ufficioVO.getEmailPassword());
				pstmt.setInt(13, ufficioVO.getId().intValue());
				pstmt.executeUpdate();

				ufficioVO.setReturnValue(ReturnValues.SAVED);
			}
		} catch (Exception e) {
			logger.error("Save modificaUfficio", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return ufficioVO;
	}

	public void cancellaUfficio(Connection conn, int ufficioId)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (conn == null) {
				logger.warn("cancellaUfficio() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(DELETE_TITOLARIO_UFFICIO);
			pstmt.setInt(1, ufficioId);
			pstmt.executeUpdate();
			jdbcMan.close(pstmt);
			pstmt = conn.prepareStatement(DELETE_UFFICIO);
			pstmt.setInt(1, ufficioId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("Error cancellaUfficio", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public boolean isUfficioCancellabile(int ufficioId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean cancellabile = true;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_PERMESSI_UTENTE);
			pstmt.setInt(1, ufficioId);
			rs = pstmt.executeQuery();
			rs.next();
			if (rs.getInt(1) > 0) {
				cancellabile = false;
			} else {
				jdbcMan.closeResultSetAndStatement(rs, pstmt);
				pstmt = connection.prepareStatement(SELECT_UFFICIO_ASSEGNATARI_PROTOCOLLI);
				pstmt.setInt(1, ufficioId);
				pstmt.setInt(2, ufficioId);
				rs = pstmt.executeQuery();
				rs.next();
				if (rs.getInt(1) > 0) {
					cancellabile = false;
				} else {
					jdbcMan.closeResultSetAndStatement(rs, pstmt);
					pstmt = connection.prepareStatement(SELECT_UFFICIO_PROTOCOLLI);
					pstmt.setInt(1, ufficioId);
					pstmt.setInt(2, ufficioId);
					rs = pstmt.executeQuery();
					rs.next();
					if (rs.getInt(1) > 0) {
						cancellabile = false;
					} else {
						jdbcMan.closeResultSetAndStatement(rs, pstmt);
						pstmt = connection.prepareStatement(SELECT_UFFICIO_REFERENTE_FASCICOLI);
						pstmt.setInt(1, ufficioId);
						pstmt.setInt(2, ufficioId);
						rs = pstmt.executeQuery();
						rs.next();
						if (rs.getInt(1) > 0) {
							cancellabile = false;
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Load isUfficioCancellabile", e);
			throw new DataException("Cannot load isUfficioCancellabile");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return cancellabile;
	}

	public Collection<UfficioVO> getUfficiByParent(int ufficioId) throws DataException {
		Collection<UfficioVO> uffici = new ArrayList<UfficioVO>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_UFFICI_BY_PARENT);
			pstmt.setInt(1, ufficioId);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				uffici.add(getUfficioVO(rs));
			}
		} catch (Exception e) {
			logger.error("Load getUfficiByParent", e);
			throw new DataException("Cannot load getUfficiByParent");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return uffici;
	}

	public UfficioVO getUfficioVO(int ufficioId) throws DataException {
		UfficioVO vo = new UfficioVO();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_UFFICIO_BY_ID);
			pstmt.setInt(1, ufficioId);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				vo = getUfficioVO(rs);
				vo.setReturnValue(ReturnValues.FOUND);
			} else {
				vo.setReturnValue(ReturnValues.NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("getUfficioVO", e);
			throw new DataException("Cannot load getUfficioVO");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return vo;
	}
	
	public UfficioVO getUfficioVOByDescrizione(String descrizione) throws DataException {
		UfficioVO vo = new UfficioVO();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_UFFICIO_BY_DESCRIZIONE);
			pstmt.setString(1, descrizione);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				vo = getUfficioVO(rs);
				vo.setReturnValue(ReturnValues.FOUND);
			} else {
				vo.setReturnValue(ReturnValues.NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("getUfficioVO", e);
			throw new DataException("Cannot load getUfficioVOByDescrizione");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return vo;
	}

	public boolean isUfficioAttivo(int ufficioId) throws DataException {
		boolean attivo = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(IS_UFFICIO_ATTIVO);
			pstmt.setInt(1, ufficioId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				attivo = rs.getBoolean(1);
			}
		} catch (Exception e) {
			logger.error("isUfficioAttivo", e);
			throw new DataException("Cannot load isUfficioAttivo");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return attivo;
	}

	public Collection<UfficioVO> getUfficiByUtente(int utenteId) throws DataException {
		Collection<UfficioVO> uffici = new ArrayList<UfficioVO>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_UFFICI_UTENTE);
			pstmt.setInt(1, utenteId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				UfficioVO vo = getUfficioVO(rs);
				vo.setReturnValue(ReturnValues.FOUND);
				uffici.add(vo);
			}

		} catch (Exception e) {
			logger.error("Load getUfficiByUtente", e);
			throw new DataException("Cannot load getUfficiByUtente");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return uffici;
	}

	public Collection<UfficioVO> getUffici() throws DataException {
		Collection<UfficioVO> uffici = new ArrayList<UfficioVO>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_UFFICI);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				UfficioVO vo = getUfficioVO(rs);
				vo.setReturnValue(ReturnValues.FOUND);
				uffici.add(vo);
			}

		} catch (Exception e) {
			logger.error("Load getUffici", e);
			throw new DataException("Cannot load getUffici");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return uffici;
	}

	public Collection<UfficioVO> getUffici(int aooId) throws DataException {
		Collection<UfficioVO> uffici = new ArrayList<UfficioVO>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_UFFICI_AOO);
			pstmt.setInt(1, aooId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				UfficioVO vo = getUfficioVO(rs);
				vo.setReturnValue(ReturnValues.FOUND);
				uffici.add(vo);
			}

		} catch (Exception e) {
			logger.error("Load getUffici", e);
			throw new DataException("Cannot load getUffici");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return uffici;
	}

	public Collection<UtenteView> getUtentiByUfficio(int ufficioId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UtenteView c = null;
		ArrayList<UtenteView> utenti = new ArrayList<UtenteView>();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_UTENTI_BY_UFFICIO);
			pstmt.setInt(1, ufficioId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				c = new UtenteView();
				// come in getUtente
				c.setId(rs.getInt("utente_id"));
				c.setCognome(rs.getString("cognome"));
				c.setNome(rs.getString("nome"));
				c.setCarica(rs.getString("denominazione"));
				// c.setReferente(rs.getBoolean("flag_abilitato"));
				c.setReferente(false);
				utenti.add(c);
			}
		} catch (Exception e) {
			logger.error("Load getUtentiByUfficio", e);
			throw new DataException("Cannot getUtentiByUfficio");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return utenti;
	}

	public Collection<UtenteView> getCaricheByUfficio(int ufficioId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UtenteView c = null;
		ArrayList<UtenteView> utenti = new ArrayList<UtenteView>();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_CARICHE_BY_UFFICIO);
			pstmt.setInt(1, ufficioId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				c = new UtenteView();
				// come in getUtente
				c.setId(rs.getInt("utente_id"));
				c.setCognome(rs.getString("cognome"));
				c.setNome(rs.getString("nome"));
				c.setCarica(rs.getString("denominazione"));
				c.setCaricaId(rs.getInt("carica_id"));
				c.setAttivo(rs.getBoolean("flag_attivo"));
				c.setAbilitato(rs.getBoolean("flag_abilitato"));
				c.setReferente(false);
				utenti.add(c);
			}
		} catch (Exception e) {
			logger.error("Load getCaricheByUfficio", e);
			throw new DataException("Cannot getCaricheByUfficio");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return utenti;
	}

	public Collection<TitolarioUfficioVO> getTitolarioByUfficio(int ufficioId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		TitolarioUfficioVO t = null;
		ArrayList<TitolarioUfficioVO> titolari = new ArrayList<TitolarioUfficioVO>();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_TITOLARIO_BY_UFFICIO);
			pstmt.setInt(1, ufficioId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				t = new TitolarioUfficioVO();
				t.setTitolarioId(rs.getInt("titolario_id"));
				t.setPath(rs.getString("path_titolario") + " - "
						+ rs.getString("desc_titolario"));
				titolari.add(t);
			}
		} catch (Exception e) {
			logger.error("Load getTitolarioByUfficio", e);
			throw new DataException("Cannot getTitolarioByUfficio");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return titolari;
	}

	public Collection<CaricaVO> getCaricheVOByUfficio(int ufficioId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CaricaVO c = null;
		ArrayList<CaricaVO> cariche = new ArrayList<CaricaVO>();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_CARICHE_VO_BY_UFFICIO);
			pstmt.setInt(1, ufficioId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				c = new CaricaVO();
				c.setUtenteId(rs.getInt("utente_id"));
				c.setUfficioId(rs.getInt("carica_id"));
				c.setNome(rs.getString("denominazione"));
				c.setCaricaId(rs.getInt("carica_id"));
				c.setAttivo(rs.getBoolean("flag_attivo"));
				cariche.add(c);
			}
		} catch (Exception e) {
			logger.error("Load getCaricheVOByUfficio", e);
			throw new DataException("Cannot getCaricheVOByUfficio");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return cariche;
	}

	public void inserisciUtentiReferenti(Connection conn, int caricaId)
			throws DataException {
		PreparedStatement pstmt = null;

		try {

			if (conn == null) {
				logger.warn("aggiornaUtentiReferenti - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(INSERT_REFERENTI_UFFICI);
			pstmt.setInt(1, caricaId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("Load aggiornaUtentiReferenti", e);
			throw new DataException("Cannot load inserisciUtentiReferenti");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void salvaUfficiTitolari(Connection conn,
			TitolarioUfficioVO titolarioufficioVO) throws DataException {
		PreparedStatement pstmt = null;

		try {

			if (conn == null) {
				logger
						.warn("salvaUfficiTitolari - Invalid Connection :"
								+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(INSERT_TITOLARIO_UFFICI);
			pstmt.setInt(1, titolarioufficioVO.getUfficioId());
			pstmt.setInt(2, titolarioufficioVO.getTitolarioId());
			if (titolarioufficioVO.getRowCreatedTime() == null) {
				pstmt.setNull(3, Types.DATE);
			} else {
				pstmt.setDate(3, new Date(titolarioufficioVO
						.getRowCreatedTime().getTime()));
			}
			pstmt.setString(4, titolarioufficioVO.getRowCreatedUser());
			pstmt.setString(5, titolarioufficioVO.getRowUpdatedUser());
			if (titolarioufficioVO.getRowUpdatedTime() == null) {
				pstmt.setNull(6, Types.DATE);
			} else {
				pstmt.setDate(6, new Date(titolarioufficioVO
						.getRowUpdatedTime().getTime()));
			}
			pstmt.setInt(7, titolarioufficioVO.getVersione());
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("Load salvaUfficiTitolari", e);
			throw new DataException("Cannot load salvaUfficiTitolari");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void aggiornaUtentiReferenti(Connection conn, String[] caricheId)
			throws DataException {
		PreparedStatement pstmt = null;

		try {

			if (conn == null) {
				logger.warn("aggiornaUtentiReferenti - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			int i = 0;
			while (i < caricheId.length) {
				pstmt = conn.prepareStatement(INSERT_REFERENTI_UFFICI);
				int utenteId = Integer.parseInt(caricheId[i]);
				pstmt.setInt(1, utenteId);
				// pstmt.setInt(2, ufficioId);
				pstmt.executeUpdate();
				i = i + 1;
			}
		} catch (Exception e) {
			logger.error("Load aggiornaUtentiReferenti", e);
			throw new DataException("Cannot load aggiornaUtentiReferenti");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public String[] getReferentiByUfficio(int ufficioId) throws DataException {
		Collection<String> referenti = new ArrayList<String>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String[] aFunzioni = null;
		try {
			connection = jdbcMan.getConnection();
			String sqlStr = "SELECT carica_id FROM cariche "
					+ " WHERE  ufficio_id=? AND flag_referente=1";
			pstmt = connection.prepareStatement(sqlStr);
			pstmt.setInt(1, ufficioId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				referenti.add(rs.getString("carica_id"));
			}
			aFunzioni = new String[referenti.size()];
			referenti.toArray(aFunzioni);

		} catch (Exception e) {
			logger.error("Load getReferentiByUfficioReferenti", e);
			throw new DataException(
					"Cannot load getReferentiByUfficioReferenti");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return aFunzioni;
	}

	public int getNumeroReferentiByUfficio(int ufficioId) throws DataException {

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int numeroUtenti;

		try {
			connection = jdbcMan.getConnection();
			String sqlStr = "SELECT COUNT(*) FROM cariche "
					+ " WHERE  ufficio_id=? and flag_referente=1";
			pstmt = connection.prepareStatement(sqlStr);
			pstmt.setInt(1, ufficioId);
			rs = pstmt.executeQuery();
			rs.next();
			numeroUtenti = rs.getInt(1);
		} catch (Exception e) {
			logger.error("Load getReferentiByUfficioReferenti", e);
			throw new DataException(
					"Cannot load getReferentiByUfficioReferenti");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return numeroUtenti;
	}

	public long getCountUffici() throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long num = -1;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_COUNT_UFFICI);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				num = rs.getLong(1);
			}
		} catch (Exception e) {
			logger.error("getCountUffici", e);
			throw new DataException("Cannot load getCountUffici");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return num;
	}

	public boolean isDescrizioneUsed(Connection connection, String descrizione,
			int ufficioId, int aooId) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean used = true;
		try {
			if (connection == null) {
				logger.warn("isDescrizioneUsed - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(CHECK_NAME);
			pstmt.setString(1, descrizione.toUpperCase().trim());
			pstmt.setInt(2, ufficioId);
			pstmt.setInt(3, aooId);
			rs = pstmt.executeQuery();
			rs.next();
			used = rs.getInt(1) > 0;
		} catch (Exception e) {
			logger.error("isDescrizioneUsed:" + descrizione, e);
			throw new DataException("error.database.missing");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return used;
	}

	public void aggiornaCaricheReferenti(Connection conn, String[] cariche)
			throws DataException {
		PreparedStatement pstmt = null;

		try {

			if (conn == null) {
				logger.warn("aggiornaUtentiReferenti - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			int i = 0;
			while (i < cariche.length) {
				pstmt = conn.prepareStatement(INSERT_REFERENTI_UFFICI);
				int caricaId = Integer.parseInt(cariche[i]);
				pstmt.setInt(1, caricaId);
				// pstmt.setInt(2, ufficioId);
				pstmt.executeUpdate();
				i = i + 1;
			}
		} catch (Exception e) {
			logger.error("Load aggiornaUtentiReferenti", e);
			throw new DataException("Cannot load aggiornaUtentiReferenti");
		} finally {
			jdbcMan.close(pstmt);
		}

	}

	public void cancellaCaricheReferenti(Connection conn, int ufficioId)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (conn == null) {
				logger.warn("cancellaUtentiReferenti - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			String sqlStr = "UPDATE cariche SET flag_referente=0 WHERE flag_referente=1 AND ufficio_id=?";
			pstmt = conn.prepareStatement(sqlStr);
			pstmt.setInt(1, ufficioId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("cancellaCaricheReferenti", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void cancellaCaricheDirigenti(Connection conn, int ufficioId)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (conn == null) {
				logger.warn("cancellaCaricheDirigenti - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			String sqlStr = "UPDATE cariche SET flag_dirigente=0 WHERE flag_dirigente=1 AND ufficio_id=?";
			pstmt = conn.prepareStatement(sqlStr);
			pstmt.setInt(1, ufficioId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("cancellaCaricheDirigenti", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void aggiornaCaricheDirigenti(Connection conn, UfficioVO ufficio)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (conn == null) {
				logger.warn("aggiornaCaricheDirigenti - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			String sqlStr = "UPDATE cariche SET flag_dirigente=1 WHERE carica_id=?";
			pstmt = conn.prepareStatement(sqlStr);
			pstmt.setInt(1, ufficio.getCaricaDirigenteId());
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("aggiornaCaricheDirigenti", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public boolean isCaricaReferenteUfficio(int caricaId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean isReferente = false;

		try {
			connection = jdbcMan.getConnection();
			String sqlStr = "SELECT COUNT(*) FROM cariche "
					+ " WHERE  flag_referente=1 AND carica_id=?";
			pstmt = connection.prepareStatement(sqlStr);
			// pstmt.setInt(1, ufficioId);
			pstmt.setInt(1, caricaId);
			rs = pstmt.executeQuery();
			rs.next();
			isReferente = (rs.getInt(1) > 0);
		} catch (Exception e) {
			logger.error("Load isUtenteReferenteUfficio", e);
			throw new DataException("Cannot load isUtenteReferenteUfficio");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return isReferente;
	}

	public int countAssegnazioni(Ufficio uff) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int numeroAssegnati = 0;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(ASSEGNATI_UFFICIO);
			pstmt.setInt(1, uff.getValueObject().getId());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				numeroAssegnati += rs.getInt(1);
			}
			for (Object o : uff.getUtenti()) {
				Utente ute = (Utente) o;
				pstmt = connection.prepareStatement(ASSEGNATI_CARICA);
				pstmt.setInt(1, ute.getValueObject().getId());
				pstmt.setInt(2, ute.getCaricaUfficioVO(uff.getValueObject().getId()).getCaricaId());
				rs = pstmt.executeQuery();
				if (rs.next()) {
					numeroAssegnati += rs.getInt(1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("contaAssegnazioni", e);
			throw new DataException("contaAssegnazioni");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return numeroAssegnati;
	}

	public boolean setUfficioAttivo(int id, boolean attivo) throws DataException {
		PreparedStatement pstmt = null;
		Connection conn = null;
		boolean updated=false;
		try {
			conn = jdbcMan.getConnection();
			pstmt = conn.prepareStatement(UPDATE_ATTIVO);
			pstmt.setInt(1, attivo ? 1 : 0);
			pstmt.setInt(2, id);
			pstmt.executeUpdate();
			updated=true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Save modificaUfficio", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
			jdbcMan.close(conn);
		}
		return updated;
	}
	
	public boolean removeUfficioProtocollo()
			throws DataException {
		boolean saved=false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(REMOVE_UFFICIO_PROTOCOLLO);
			pstmt.executeUpdate();
			saved=true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("removeUfficioProtocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return saved;
	}
	
	public UfficioVO setUfficioProtocollo(int id)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		UfficioVO newVO=new UfficioVO();
		newVO.setReturnValue(ReturnValues.UNKNOWN);
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(UPDATE_UFFICIO_PROTOCOLLO);
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
			newVO = getUfficioVO(id);
			newVO.setReturnValue(ReturnValues.SAVED);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("setUfficioProtocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return newVO;
	}
};