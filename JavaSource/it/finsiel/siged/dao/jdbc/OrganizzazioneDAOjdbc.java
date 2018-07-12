package it.finsiel.siged.dao.jdbc;

import it.compit.fenice.enums.UnitaAmministrativaEnum;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.integration.OrganizzazioneDAO;
import it.finsiel.siged.mvc.vo.lookup.ParametriLdapVO;
import it.finsiel.siged.mvc.vo.organizzazione.AmministrazioneVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.rdb.JDBCManager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

public class OrganizzazioneDAOjdbc implements OrganizzazioneDAO {

	private final static String SELECT_AMMINISTRAZIONE = "SELECT * FROM amministrazione";

	private final static String SELECT_UFFICI = "SELECT * FROM uffici";

	private final static String SELECT_UFFICI_IDS_BY_UTENTE = "SELECT DISTINCT(ufficio_id) FROM cariche WHERE utente_id = ?";

	private final static String SELECT_CARICHE_IDS_BY_UTENTE = "SELECT DISTINCT(carica_id) FROM cariche WHERE utente_id = ? AND flag_attivo=1";

	private final static String SELECT_CARICHE_BY_UTENTE_UFFICIO = "SELECT denominazione FROM cariche WHERE utente_id = ? AND ufficio_id=?";

	private final static String SELECT_CARICA_RESPONSABILE = "SELECT * FROM cariche WHERE flag_responsabile_ente=1";

	private final static String SELECT_CARICA_RESPONSABILE_UFFICIO_PROTOCOLLO = "SELECT * FROM cariche WHERE flag_referente_ufficio_protocollo=1";

	private final static String UPDATE_AMMINISTRAZIONE = "UPDATE amministrazione set codi_amministrazione=?, desc_amministrazione=?,"
			+ "flag_ldap=?, row_updated_user=?, row_updated_time=?, versione=versione+1, "
			+ "ldap_versione=?, ldap_porta=?, ldap_use_ssl=?, ldap_host=?, ldap_dn=?, path_doc =?,path_doc_protocollo =?,flag_reg_separato=?, ftp_user=?, ftp_pass=?,"
			+ "ftp_port=?, ftp_host=?, ftp_folder=?, flag_web_socket=? WHERE amministrazione_id=?";

	private final static String UPDATE_PARAMETRI_FTP = "UPDATE amministrazione set ftp_user=?, ftp_pass=?,"
			+ "ftp_port=?, ftp_host=?, ftp_folder=? WHERE amministrazione_id=?";
	static Logger logger = Logger.getLogger(OrganizzazioneDAOjdbc.class
			.getName());

	private JDBCManager jdbcMan = new JDBCManager();

	public AmministrazioneVO getAmministrazione()
			throws DataException {
		AmministrazioneVO amm = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_AMMINISTRAZIONE);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				amm = new AmministrazioneVO();
				amm.setId(rs.getInt("amministrazione_id"));
				amm.setCodice(rs.getString("codi_amministrazione"));
				amm.setDescription(rs.getString("desc_amministrazione"));
				amm.setFlagLdap(rs.getString("flag_ldap"));
				ParametriLdapVO pVO = new ParametriLdapVO();
				pVO.setVersione(rs.getInt("ldap_versione"));
				pVO.setPorta(rs.getInt("ldap_porta"));
				pVO.setUse_ssl(rs.getString("ldap_use_ssl"));
				pVO.setHost(rs.getString("ldap_host"));
				pVO.setDn(rs.getString("ldap_dn"));
				amm.setParametriLdap(pVO);
				amm.setPathDocAquisMassiva(rs.getString("path_doc"));
				amm.setPathDocumentiProtocollo(rs.getString("path_doc_protocollo"));
				amm.setFlagRegistroPostaSeparato(rs.getString("flag_reg_separato"));
				amm.setWebSocketEnabled(rs.getBoolean("flag_web_socket"));
				amm.setVersioneFenice(rs.getString("versione_corrente_fenice"));
				amm.setUnitaAmministrativa(UnitaAmministrativaEnum.findByValue(rs.getInt("id_unita_amministrativa")));
				if(!amm.getUnitaAmministrativa().equals(UnitaAmministrativaEnum.BBCCAA_PA)){
					amm.setHostFtp(rs.getString("ftp_host"));
					amm.setPassFtp(rs.getString("ftp_pass"));
					amm.setUserFtp(rs.getString("ftp_user"));
					amm.setPortaFtp(rs.getInt("ftp_port"));
					amm.setFolderFtp(rs.getString("ftp_folder"));
				}
				else{
					amm.setHostFtp("ftp.regione.sicilia.it");
					amm.setPassFtp("Palermo2012!");
					amm.setUserFtp("FTP_Franco.Fidelio");
					amm.setPortaFtp(21);
					amm.setFolderFtp("Dirbenicult/database/Decreti");
				}
				logger.debug("Caricata Amministrazione: " + amm);
			}
		} catch (Exception e) {
			amm = null;
			logger.error("Caricamento Amministrazione ", e);
			throw new DataException("Cannot load Amministrazione");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return amm;
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
				UfficioVO uff = new UfficioVO();
				uff.setId(rs.getInt("ufficio_id"));
				uff.setAttivo(rs.getBoolean("flag_attivo"));
				uff.setUfficioProtocollo(rs.getBoolean("flag_ufficio_protocollo"));
				uff.setTipo(rs.getString("tipo"));
				uff.setAccettazioneAutomatica(rs
						.getBoolean("flag_accettazione_automatica"));
				uff.setAooId(rs.getInt("aoo_id"));
				uff.setParentId(rs.getInt("parent_id"));
				uff.setDescription(rs.getString("descrizione"));
				uff.setPiano(rs.getString("piano"));
				uff.setStanza(rs.getString("stanza"));
				uff.setTelefono(rs.getString("telefono"));
				uff.setFax(rs.getString("fax"));
				uff.setEmail(rs.getString("indi_email"));
				uff.setEmailUsername(rs.getString("email_username"));
				uff.setEmailPassword(rs.getString("email_password"));
				uff.setDataUltimaMailRicevuta(rs.getTimestamp("data_ultima_mail"));
				uffici.add(uff);
			}
		} catch (Exception e) {
			uffici.clear();
			logger.error("Load Uffici ", e);
			throw new DataException("Cannot load Uffici");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return uffici;
	}

	public ArrayList<Integer> getIdentificativiUffici(int utenteId) throws DataException {
		ArrayList<Integer> uffici = new ArrayList<Integer>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_UFFICI_IDS_BY_UTENTE);
			pstmt.setInt(1, utenteId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				uffici.add(new Integer(rs.getInt("ufficio_id")));
			}
		} catch (Exception e) {
			logger.error("getIdentificativiUffici", e);
			throw new DataException(
					"Impossibile leggere gli uffici dell'utente.");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return uffici;
	}

	public ArrayList<Integer> getIdentificativiCariche(int utenteId)
			throws DataException {
		ArrayList<Integer> uffici = new ArrayList<Integer>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_CARICHE_IDS_BY_UTENTE);
			pstmt.setInt(1, utenteId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				uffici.add(new Integer(rs.getInt("carica_id")));
			}
		} catch (Exception e) {
			logger.error("getIdentificativiCariche", e);
			throw new DataException(
					"Impossibile leggere le cariche dell'utente.");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return uffici;
	}

	public AmministrazioneVO updateParametriFTP(AmministrazioneVO ammVO)
			throws DataException {
		AmministrazioneVO ammSalvata = new AmministrazioneVO();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			ammSalvata = updateParametriFTP(connection, ammVO);
		} catch (Exception e) {
			throw new DataException(e.getMessage()
					+ " updateAmministrazione Amministrazione Id :"
					+ ammVO.getId().intValue());
		} finally {
			jdbcMan.close(connection);
		}
		return ammSalvata;
	}

	private AmministrazioneVO updateParametriFTP(Connection conn,
			AmministrazioneVO ammVO) throws DataException {
		PreparedStatement pstmt = null;
		ammVO.setReturnValue(ReturnValues.UNKNOWN);
		try {
			if (conn == null) {
				logger.warn("updateAmministrazione() - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(UPDATE_PARAMETRI_FTP);
			pstmt.setString(1, ammVO.getUserFtp());
			pstmt.setString(2, ammVO.getPassFtp());
			pstmt.setInt(3, ammVO.getPortaFtp());
			pstmt.setString(4, ammVO.getHostFtp());
			pstmt.setString(5, ammVO.getFolderFtp());
			pstmt.setInt(6, ammVO.getId().intValue());
			pstmt.executeUpdate();
			ammVO.setReturnValue(ReturnValues.SAVED);
		} catch (Exception e) {
			logger.error("Update updateAmministrazione", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return ammVO;
	}

	public AmministrazioneVO updateAmministrazione(AmministrazioneVO ammVO)
			throws DataException {
		AmministrazioneVO ammSalvata = new AmministrazioneVO();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			//archiviaVersione(connection);
			ammSalvata = updateAmministrazione(connection, ammVO);
			connection.commit();
		} catch (Exception e) {
			//connection.rollback();
			throw new DataException(e.getMessage()
					+ " updateAmministrazione Amministrazione Id :"
					+ ammVO.getId().intValue());
		} finally {
			jdbcMan.close(connection);
		}
		return ammSalvata;
	}

	private AmministrazioneVO updateAmministrazione(Connection conn,
			AmministrazioneVO ammVO) throws DataException {
		PreparedStatement pstmt = null;
		ammVO.setReturnValue(ReturnValues.UNKNOWN);
		try {
			if (conn == null) {
				logger.warn("updateAmministrazione() - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(UPDATE_AMMINISTRAZIONE);
			pstmt.setString(1, ammVO.getCodice());
			pstmt.setString(2, ammVO.getDescription());
			pstmt.setString(3, ammVO.getFlagLdap());
			pstmt.setString(4, ammVO.getRowUpdatedUser());
			pstmt.setDate(5, new Date(System.currentTimeMillis()));
			pstmt.setInt(6, ammVO.getParametriLdap().getVersione());
			pstmt.setInt(7, ammVO.getParametriLdap().getPorta());
			pstmt.setString(8, ammVO.getParametriLdap().getUse_ssl());
			pstmt.setString(9, ammVO.getParametriLdap().getHost());
			pstmt.setString(10, ammVO.getParametriLdap().getDn());
			pstmt.setString(11, ammVO.getPathDocAquisMassiva());
			pstmt.setString(12, ammVO.getPathDocumentiProtocollo());
			pstmt.setString(13, ammVO.getFlagRegistroPostaSeparato());
			pstmt.setString(14, ammVO.getUserFtp());
			pstmt.setString(15, ammVO.getPassFtp());
			pstmt.setInt(16, ammVO.getPortaFtp());
			pstmt.setString(17, ammVO.getHostFtp());
			pstmt.setString(18, ammVO.getFolderFtp());
			pstmt.setInt(19, ammVO.isWebSocketEnabled()? 1:0);
			pstmt.setInt(20, ammVO.getId().intValue());
			pstmt.executeUpdate();
			ammVO.setReturnValue(ReturnValues.SAVED);
		} catch (Exception e) {
			logger.error("Update updateAmministrazione", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return ammVO;
	}

	public String getNomeCarica(int uffId, int uteId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection
					.prepareStatement(SELECT_CARICHE_BY_UTENTE_UFFICIO);
			pstmt.setInt(1, uteId);
			pstmt.setInt(2, uffId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			logger.error("getNomeCarica", e);
			throw new DataException(
					"Impossibile leggere gli uffici della carica.");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return null;
	}
	

	public CaricaVO getCaricaResponsabile() throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CaricaVO c=null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_CARICA_RESPONSABILE);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				c=new CaricaVO();
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
			}
		} catch (Exception e) {
			logger.error("getCaricaResponsabile", e);
			throw new DataException(
					"Impossibile trovare la Carica Responsabile.");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return c;
	}
	
	public CaricaVO getCaricaResponsabileUfficioProtocollo() throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CaricaVO c=null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_CARICA_RESPONSABILE_UFFICIO_PROTOCOLLO);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				c=new CaricaVO();
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

			}
		} catch (Exception e) {
			logger.error("getCaricaResponsabile", e);
			throw new DataException(
					"Impossibile trovare la Carica Responsabile Ufficio Protocollo.");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return c;
	}
};