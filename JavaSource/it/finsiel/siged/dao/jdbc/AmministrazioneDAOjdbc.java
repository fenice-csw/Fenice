package it.finsiel.siged.dao.jdbc;

import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.compit.fenice.mvc.vo.protocollo.UfficioPartecipanteVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.bo.TitolarioBO;
import it.finsiel.siged.mvc.business.DocumentoDelegate;
import it.finsiel.siged.mvc.integration.AmministrazioneDAO;
import it.finsiel.siged.mvc.integration.IdentificativiDAO;
import it.finsiel.siged.mvc.vo.IdentityVO;
import it.finsiel.siged.mvc.vo.log.LogAcquisizioneMassivaVO;
import it.finsiel.siged.mvc.vo.lookup.AmministrazionePartecipanteVO;
import it.finsiel.siged.mvc.vo.lookup.ProfiloVO;
import it.finsiel.siged.mvc.vo.lookup.TipoDocumentoVO;
import it.finsiel.siged.mvc.vo.lookup.TipoProcedimentoVO;
import it.finsiel.siged.mvc.vo.organizzazione.MenuVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.SpedizioneVO;
import it.finsiel.siged.rdb.JDBCManager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class AmministrazioneDAOjdbc implements AmministrazioneDAO {
	static Logger logger = Logger.getLogger(AmministrazioneDAOjdbc.class
			.getName());

	private JDBCManager jdbcMan = new JDBCManager();

	public void deleteUfficiPartecipanti(Connection connection, int id)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(DELETE_UFFICI_PARTECIPANTI);
			pstmt.setInt(1, id);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("deleteUfficiPartecipanti", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}
	
	public List<UfficioPartecipanteVO> getUfficiPartecipanti(int id)
			throws DataException {
		List<UfficioPartecipanteVO> uffici = new ArrayList< UfficioPartecipanteVO>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(UFFICI_PARTECIPANTI);
			pstmt.setInt(1, id);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				UfficioPartecipanteVO uff = new UfficioPartecipanteVO();
				uff.setUfficioId(rs.getInt("ufficio_id"));
				uff.setNomeUfficio(rs.getString("descrizione"));
				uff.setPrincipale(rs.getInt("flag_principale")==1?true:false);
				uff.setVisibilita(true);
				uffici.add( uff);
			}
		} catch (Exception e) {
			logger.error("getUfficiPartecipanti", e);
			throw new DataException("Cannot load getUfficiPartecipanti");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return uffici;
	}
	
	public Map<String, UfficioPartecipanteVO> getUfficiPartecipantiForProcedimento(int id)
			throws DataException {
		Map<String, UfficioPartecipanteVO> uffici = new HashMap<String, UfficioPartecipanteVO>(2);		
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(UFFICI_PARTECIPANTI_PROCEDIMENTO);
			pstmt.setInt(1, id);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				UfficioPartecipanteVO uff = new UfficioPartecipanteVO();
				uff.setUfficioId(rs.getInt("ufficio_id"));
				uff.setNomeUfficio(rs.getString("descrizione"));
				uff.setPrincipale(false);
				uff.setVisibilita(true);
				uffici.put(String.valueOf(uff.getUfficioId()), uff);
			}
		} catch (Exception e) {
			logger.error("getUfficiPartecipanti", e);
			throw new DataException("Cannot load getUfficiPartecipanti");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return uffici;
	}
	
	public void salvaUfficioPartecipante(Connection connection, int tpu_id,
			int tipoId, int ufficioId, boolean principale,int versione) throws DataException {
		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("salvaUfficioPartecipante() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(INSERT_UFFICIO_PARTECIPANTE);
			pstmt.setInt(1, tpu_id);
			pstmt.setInt(2, tipoId);
			pstmt.setInt(3, ufficioId);
			pstmt.setInt(4, principale?1:0);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("salvaUfficioPartecipante", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	
	public void deleteRiferimenti(Connection connection, int id)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(DELETE_RIFERIMENTI);
			pstmt.setInt(1, id);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("deleteRiferimenti", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public Map<String, DocumentoVO> getRiferimenti(int id) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, DocumentoVO> docs = new HashMap<String, DocumentoVO>(2);
		Connection connection = null;
		DocumentoDelegate documentoDelegate = DocumentoDelegate.getInstance();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_RIFERIMENTI);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {

				int docId = rs.getInt("documento_id");
				DocumentoVO doc = documentoDelegate.getDocumento(connection,
						docId);
				doc.setMustCreateNew(false);
				TitolarioBO.putAllegato(doc, docs);
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new DataException("Errore nella lettura dei documenti.");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return docs;
	}

	public void salvaRiferimenti(Connection connection, int riferimenti_id,
			int id, int documentoId, int versione) throws DataException {
		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("salvaRiferimenti() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(INSERT_RIFERIMENTI);
			pstmt.setInt(1, riferimenti_id);
			pstmt.setInt(2, id);
			pstmt.setInt(3, documentoId);

			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("salvaRiferimenti", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}



	public Map<Integer, AmministrazionePartecipanteVO> getAmministrazioniPartecipanti(int id)
			throws DataException {
		HashMap<Integer, AmministrazionePartecipanteVO> amministrazioni = new HashMap<Integer, AmministrazionePartecipanteVO>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(AMMINISTRAZIONI_PARTECIPANTI);
			pstmt.setInt(1, id);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				AmministrazionePartecipanteVO amm = new AmministrazionePartecipanteVO();
				amm.setDenominazione(rs.getString("nominativo"));
				amm.setId(rs.getInt("rubrica_id"));
				amm.setIdx(rs.getInt("amministrazioni_id"));
				amministrazioni.put(amm.getIdx(), amm);
			}
		} catch (Exception e) {
			logger.error("getAmministrazioni", e);
			throw new DataException("Cannot load getAmministrazioni");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return amministrazioni;
	}

	public void eliminaAmministrazioniPartecipanti(Connection connection, int id)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(DELETE_ASSEGNATARI);
			pstmt.setInt(1, id);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("eliminaAllacciProtocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void salvaAmministrazionePartecipante(Connection connection,
			AmministrazionePartecipanteVO amministrazione) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("salvaAmministrazione() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			if (amministrazione != null) {
				pstmt = connection
						.prepareStatement(INSERT_AMMINISTRAZIONI_PARTECIPANTI);
				pstmt.setInt(1, amministrazione.getId().intValue());
				pstmt.setInt(2, amministrazione.getRubricaId());
				pstmt.setInt(3, amministrazione.getTipoProcedimentoId());
				pstmt.setString(4, amministrazione.getDenominazione());
				pstmt.executeUpdate();
			}
		} catch (Exception e) {
			logger.error("Save Assegnatari-Protocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public ArrayList<SpedizioneVO> getSpedizioni(String descrizioneSpedizione,
			int aooId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		SpedizioneVO mezzoSpedizione;
		ResultSet rs = null;
		ArrayList<SpedizioneVO> lista = new ArrayList<SpedizioneVO>();
		try {
			connection = jdbcMan.getConnection();

			pstmt = connection.prepareStatement(SELECT_MEZZI_SPEDIZIONE);
			pstmt.setString(1, descrizioneSpedizione.toLowerCase() + "%");
			pstmt.setInt(2, aooId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				mezzoSpedizione = new SpedizioneVO();
				mezzoSpedizione.setId(rs.getInt("spedizioni_id"));
				mezzoSpedizione.setAooId(rs.getInt("aoo_id"));
				mezzoSpedizione.setFlagAbilitato(rs
						.getBoolean("flag_abilitato"));
				mezzoSpedizione.setFlagCancellabile(rs
						.getBoolean("flag_cancellabile"));
				mezzoSpedizione.setCodiceSpedizione(rs
						.getString("codi_spedizione"));
				mezzoSpedizione.setDescrizioneSpedizione(rs
						.getString("desc_spedizione"));
				mezzoSpedizione.setPrezzo(rs.getString("prezzo"));
				lista.add(mezzoSpedizione);
			}

		} catch (Exception e) {
			logger.error("Amministrazione: getSpedizioni", e);
			throw new DataException(
					"Cannot load Amministrazione: getSpedizioni");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return lista;
	}

	private SpedizioneVO getMezzoSpedizione(Connection connection, int id)
			throws Exception {
		PreparedStatement pstmt = null;
		SpedizioneVO mezzoSpedizione = new SpedizioneVO();
		ResultSet rs = null;
		pstmt = connection.prepareStatement(SELECT_MEZZO_SPEDIZIONE);
		pstmt.setInt(1, id);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			mezzoSpedizione = new SpedizioneVO();
			mezzoSpedizione.setId(rs.getInt("spedizioni_id"));
			mezzoSpedizione
					.setCodiceSpedizione(rs.getString("codi_spedizione"));
			mezzoSpedizione.setDescrizioneSpedizione(rs
					.getString("desc_spedizione"));
			mezzoSpedizione.setFlagAbilitato(rs.getBoolean("flag_abilitato"));
			mezzoSpedizione.setFlagCancellabile(rs
					.getBoolean("flag_cancellabile"));
			mezzoSpedizione.setPrezzo(rs.getString("prezzo"));
		}

		return mezzoSpedizione;
	}

	public SpedizioneVO getMezzoSpedizione(int id) {
		Connection connection = null;
		SpedizioneVO mezzoSpedizione = new SpedizioneVO();
		try {
			connection = jdbcMan.getConnection();
			mezzoSpedizione = getMezzoSpedizione(connection, id);
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn(
					"getMezzoSpedizione fallito, rolling back transction..", de);

		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn(
					"getMezzoSpedizione fallito, rolling back transction..", se);
		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);

		} finally {
			jdbcMan.close(connection);
		}
		return mezzoSpedizione;
	}

	public void newMezzoSpedizione(Connection conn, SpedizioneVO spedizioneVO)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (conn == null) {
				logger.warn("newMezzoSpedizione() - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(INSERT_MEZZO_SPEDIZIONE);
			pstmt.setInt(1, spedizioneVO.getId().intValue());
			pstmt.setInt(2, spedizioneVO.getAooId());
			pstmt.setInt(3, spedizioneVO.getFlagAbilitato() ? 1 : 0);
			pstmt.setInt(4, spedizioneVO.getFlagCancellabile() ? 1 : 0);
			pstmt.setString(5, spedizioneVO.getCodiceSpedizione());
			pstmt.setString(6, spedizioneVO.getDescrizioneSpedizione());
			pstmt.setString(7, spedizioneVO.getRowCreatedUser());
			pstmt.setString(8, spedizioneVO.getRowUpdatedUser());
			pstmt.setString(9, spedizioneVO.getPrezzo());
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("Save MezzoSpedizione", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}

	}

	public SpedizioneVO aggiornaMezzoSpedizione(Connection conn,
			SpedizioneVO spedizioneVO) throws DataException {
		PreparedStatement pstmt = null;
		spedizioneVO.setReturnValue(ReturnValues.UNKNOWN);
		try {
			if (conn == null) {
				logger.warn("aggiornaMezzoSpedizione() - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			SpedizioneVO spVO = new SpedizioneVO();
			spVO = getMezzoSpedizione(conn, spedizioneVO.getId().intValue());
			if (spedizioneVO
					.getDescrizioneSpedizione()
					.toUpperCase()
					.trim()
					.equals(spVO.getDescrizioneSpedizione().toUpperCase()
							.trim())
					|| !isMezzoSpedizioneUtilizzato(conn, spVO

					.getDescrizioneSpedizione().toUpperCase().trim())) {
				pstmt = conn.prepareStatement(UPDATE_MEZZO_SPEDIZIONE);
				pstmt.setString(1, spedizioneVO.getDescrizioneSpedizione());
				pstmt.setString(2, spedizioneVO.getRowUpdatedUser());
				pstmt.setInt(3, spedizioneVO.getFlagAbilitato() ? 1 : 0);
				pstmt.setInt(4, spedizioneVO.getFlagCancellabile() ? 1 : 0);
				pstmt.setString(5, spedizioneVO.getPrezzo());
				pstmt.setInt(6, spedizioneVO.getId().intValue());
				pstmt.executeUpdate();
				spedizioneVO.setReturnValue(ReturnValues.SAVED);
			} else {
				spedizioneVO.setReturnValue(ReturnValues.FOUND);
			}
		} catch (Exception e) {
			logger.error("Update MezzoSpedizione", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return spedizioneVO;

	}

	public boolean cancellaMezzoSpedizione(Connection conn, int spedizioneId)
			throws DataException {
		PreparedStatement pstmt = null;
		boolean cancellato = false;
		try {
			if (conn == null) {
				logger.warn("cancellaMezzoSpedizione() - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			SpedizioneVO spVO = new SpedizioneVO();
			spVO = getMezzoSpedizione(conn, spedizioneId);
			if (!isMezzoSpedizioneUtilizzato(conn,
					spVO.getDescrizioneSpedizione())) {
				pstmt = conn.prepareStatement(DELETE_MEZZO_SPEDIZIONE);
				pstmt.setInt(1, spedizioneId);
				pstmt.executeUpdate();
				cancellato = true;
			}

		} catch (Exception e) {
			logger.error("cancellaMezzoSpedizione MezzoSpedizione", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return cancellato;
	}

	public ArrayList<ProfiloVO> getProfili(int aooId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ProfiloVO profilo;
		ResultSet rs = null;
		ArrayList<ProfiloVO> lista = new ArrayList<ProfiloVO>();
		try {
			connection = jdbcMan.getConnection();

			pstmt = connection.prepareStatement(SELECT_PROFILI);
			pstmt.setInt(1, aooId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				profilo = new ProfiloVO();
				profilo.setId(rs.getInt("profilo_id"));
				profilo.setCodProfilo(rs.getString("codi_profilo"));
				profilo.setDescrizioneProfilo(rs.getString("desc_profilo"));
				lista.add(profilo);
			}

		} catch (Exception e) {
			logger.error("Amministrazione: getProfili", e);
			throw new DataException("Cannot load Amministrazione: getProfili");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return lista;
	}

	public ArrayList<MenuVO> getFunzioniMenu() throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		MenuVO menu;
		ResultSet rs = null;
		ArrayList<MenuVO> lista = new ArrayList<MenuVO>();
		try {
			connection = jdbcMan.getConnection();

			pstmt = connection.prepareStatement(SELECT_FUNZIONI_MENU);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				menu = new MenuVO();
				menu.setId(rs.getInt("menu_id"));
				menu.setCodice(rs.getString("titolo"));
				menu.setDescription(rs.getString("descrizione"));
				menu.setUniqueName(rs.getString("unique_name"));
				lista.add(menu);
			}

		} catch (Exception e) {
			logger.error("Amministrazione: getFunzioniMenu", e);
			throw new DataException(
					"Cannot load Amministrazione: getFunzioniMenu");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return lista;
	}

	public ArrayList<MenuVO> getFunzioniMenuByFunction(int function)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		MenuVO menu;
		ResultSet rs = null;
		ArrayList<MenuVO> lista = new ArrayList<MenuVO>();
		try {
			connection = jdbcMan.getConnection();

			pstmt = connection
					.prepareStatement(SELECT_FUNZIONI_MENU_BY_FUNCTION);
			pstmt.setInt(1, function);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				menu = new MenuVO();
				menu.setId(rs.getInt("menu_id"));
				menu.setCodice(rs.getString("titolo"));
				menu.setDescription(rs.getString("descrizione"));
				menu.setUniqueName(rs.getString("unique_name"));
				if (menu.getId() != 49 && menu.getId() != 50) {
					lista.add(menu);
				}
			}

		} catch (Exception e) {
			logger.error("Amministrazione: getFunzioniMenu", e);
			throw new DataException(
					"Cannot load Amministrazione: getFunzioniMenu");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return lista;
	}

	public ProfiloVO newProfilo(Connection conn, ProfiloVO profiloVO)
			throws DataException {
		PreparedStatement pstmt = null;
		profiloVO.setReturnValue(ReturnValues.UNKNOWN);
		try {
			if (conn == null) {
				logger.warn("newProfilo() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(INSERT_PROFILO);
			pstmt.setInt(1, profiloVO.getId().intValue());
			pstmt.setString(2, profiloVO.getCodProfilo());
			pstmt.setString(3, profiloVO.getDescrizioneProfilo());
			if (profiloVO.getDataInizioValidita() == null) {
				pstmt.setDate(4, new Date(System.currentTimeMillis()));
			} else {
				pstmt.setDate(4, new Date(profiloVO.getDataInizioValidita()
						.getTime()));
			}
			if (profiloVO.getDataFineValidita() == null) {
				pstmt.setNull(5, Types.DATE);
			} else {
				pstmt.setDate(5, new Date(profiloVO.getDataFineValidita()
						.getTime()));
			}

			pstmt.setString(6, profiloVO.getRowCreatedUser());
			pstmt.setString(7, profiloVO.getRowUpdatedUser());
			pstmt.setInt(8, profiloVO.getAooId());
			pstmt.executeUpdate();
			String[] menuProfilo = profiloVO.getMenuProfili();
			if (menuProfilo != null) {
				for (int i = 0; i < menuProfilo.length; i++) {
					if (menuProfilo[i] != null) {
						pstmt = conn.prepareStatement(INSERT_MENU_PROFILO);
						pstmt.setInt(1, profiloVO.getId().intValue());
						pstmt.setInt(2,
								(new Integer(menuProfilo[i])).intValue());
						pstmt.setString(3, profiloVO.getRowCreatedUser());
						pstmt.setString(4, profiloVO.getRowUpdatedUser());
						pstmt.executeUpdate();
					}
				}

			}
			profiloVO.setReturnValue(ReturnValues.SAVED);
		} catch (Exception e) {
			logger.error("Save Profilo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return profiloVO;
	}

	public ProfiloVO updateProfilo(Connection conn, ProfiloVO profiloVO)
			throws DataException {
		PreparedStatement pstmt = null;
		profiloVO.setReturnValue(ReturnValues.UNKNOWN);
		try {
			if (conn == null) {
				logger.warn("updateProfilo() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(UPDATE_PROFILO);
			pstmt.setString(1, profiloVO.getCodProfilo());
			pstmt.setString(2, profiloVO.getDescrizioneProfilo());
			if (profiloVO.getDataInizioValidita() == null) {
				pstmt.setDate(3, new Date(System.currentTimeMillis()));
			} else {
				pstmt.setDate(3, new Date(profiloVO.getDataInizioValidita()
						.getTime()));
			}
			if (profiloVO.getDataFineValidita() == null) {
				pstmt.setNull(4, Types.DATE);
			} else {
				pstmt.setDate(4, new Date(profiloVO.getDataFineValidita()
						.getTime()));
			}

			pstmt.setString(5, profiloVO.getRowUpdatedUser());
			pstmt.setInt(6, profiloVO.getId().intValue());
			pstmt.executeUpdate();
			jdbcMan.close(pstmt);
			pstmt = conn.prepareStatement(DELETE_MENU_PROFILO);
			pstmt.setInt(1, profiloVO.getId().intValue());
			pstmt.executeUpdate();

			// INSCERISCE I MENU ASSOCIATI AL PROFILO

			String[] menuProfilo = profiloVO.getMenuProfili();
			if (menuProfilo != null) {
				for (int i = 0; i < menuProfilo.length; i++) {
					if (menuProfilo[i] != null) {
						pstmt = conn.prepareStatement(INSERT_MENU_PROFILO);
						pstmt.setInt(1, profiloVO.getId().intValue());
						pstmt.setInt(2,
								(new Integer(menuProfilo[i])).intValue());
						pstmt.setString(3, profiloVO.getRowCreatedUser());
						pstmt.setString(4, profiloVO.getRowUpdatedUser());
						pstmt.executeUpdate();
					}
				}

			}
			profiloVO.setReturnValue(ReturnValues.SAVED);
		} catch (Exception e) {
			logger.error("updateProfilo Profilo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return profiloVO;
	}

	public Collection<String> getMenuByProfilo(int profiloId)
			throws DataException {
		Collection<String> funzioni = new ArrayList<String>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_MENU_BY_PROFILO);
			pstmt.setInt(1, profiloId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				funzioni.add(rs.getString("menu_id"));
			}

		} catch (Exception e) {
			logger.error("Load getMenuByProfilo", e);
			throw new DataException("Cannot load getMenuByProfilo");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return funzioni;
	}

	public ProfiloVO getProfilo(int profiloId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ProfiloVO profilo = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();

			pstmt = connection.prepareStatement(SELECT_PROFILO);
			pstmt.setInt(1, profiloId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				profilo = new ProfiloVO();
				profilo.setId(rs.getInt("profilo_id"));
				profilo.setCodProfilo(rs.getString("codi_profilo"));
				profilo.setDescrizioneProfilo(rs.getString("desc_profilo"));
				jdbcMan.closeResultSetAndStatement(rs, pstmt);
				pstmt = connection.prepareStatement(SELECT_MENU_PROFILO);
				pstmt.setInt(1, profiloId);
				rs = pstmt.executeQuery();
				Collection<String> menu = new ArrayList<String>();
				while (rs.next()) {
					menu.add(rs.getString("menu_id"));
				}
				String[] ar = new String[menu.size()];
				menu.toArray(ar);
				profilo.setMenuProfili(ar);
			}
		} catch (Exception e) {
			logger.error("Amministrazione: getMenuProfilo", e);
			throw new DataException(
					"Cannot load Amministrazione: getMenuProfilo");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return profilo;
	}

	public void cancellaProfilo(int profiloId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = jdbcMan.getConnection();

			pstmt = connection.prepareStatement(DELETE_MENU_PROFILO);
			pstmt.setInt(1, profiloId);
			pstmt.executeUpdate();
			jdbcMan.close(pstmt);

			pstmt = connection.prepareStatement(DELETE_PROFILO);
			pstmt.setInt(1, profiloId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("Amministrazione: deleteProfilo", e);
			throw new DataException(
					"Cannot load Amministrazione: deleteProfilo");
		} finally {
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
	}

	public ArrayList<TipoDocumentoVO> getTipiDocumento(int aooId)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		TipoDocumentoVO tipoDocumento;
		ResultSet rs = null;
		ArrayList<TipoDocumentoVO> lista = new ArrayList<TipoDocumentoVO>();
		try {
			connection = jdbcMan.getConnection();

			pstmt = connection.prepareStatement(SELECT_TIPI_DOCUMENTO);
			pstmt.setInt(1, aooId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tipoDocumento = new TipoDocumentoVO();
				tipoDocumento.setId(rs.getInt("tipo_documento_id"));
				tipoDocumento.setDescrizioneDoc(rs
						.getString("desc_tipo_documento"));
				tipoDocumento.setFlagAttivazione(rs
						.getString("flag_attivazione"));
				tipoDocumento.setFlagDefault(rs.getString("flag_default"));
				tipoDocumento.setProtocolli(rs.getInt("contatore"));
				tipoDocumento.setNumGGScadenza(rs.getInt("nume_gg_scadenza"));
				lista.add(tipoDocumento);
			}

		} catch (Exception e) {
			logger.error("Amministrazione: getTipiDocumento", e);
			throw new DataException(
					"Cannot load Amministrazione: getTipiDocumento");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return lista;
	}

	public TipoDocumentoVO getTipoDocumento(int tipoDocumentoId)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		TipoDocumentoVO tipoDocumento = new TipoDocumentoVO();
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();

			pstmt = connection.prepareStatement(SELECT_TIPO_DOCUMENTO);
			pstmt.setInt(1, tipoDocumentoId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				tipoDocumento = new TipoDocumentoVO();
				tipoDocumento.setId(rs.getInt("tipo_documento_id"));
				tipoDocumento.setDescrizioneDoc(rs
						.getString("desc_tipo_documento"));
				tipoDocumento.setFlagDefault(rs.getString("flag_default"));
				tipoDocumento.setFlagAttivazione(rs
						.getString("flag_attivazione"));
				tipoDocumento.setNumGGScadenza(rs.getInt("nume_gg_scadenza"));

			}

		} catch (Exception e) {
			logger.error("Amministrazione: getTipoDocumento", e);
			throw new DataException(
					"Cannot load Amministrazione: getTipoDocumento");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return tipoDocumento;

	}

	public TipoDocumentoVO newTipoDocumento(Connection conn,
			TipoDocumentoVO tipoDocumentoVO) throws DataException {
		PreparedStatement pstmt = null;
		try {
			tipoDocumentoVO.setReturnValue(ReturnValues.UNKNOWN);
			if (conn == null) {
				logger.warn("newTipoDocumento() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			if ("1".equals(tipoDocumentoVO.getFlagDefault())) {
				pstmt = conn.prepareStatement(UPDATE_TIPO_DOCUMENTO_DEFAULT);
				// Inserimento Luigi 17/02/06 per risolvere un problema
				// segnalato da Alessandro Petrangeli
				pstmt.setInt(1, tipoDocumentoVO.getAooId());
				// Fine inserimento 17/02/06
				pstmt.executeUpdate();
				jdbcMan.close(pstmt);
			}

			if (!isDescriptionUsed(conn, tipoDocumentoVO.getDescrizione(), 0,
					tipoDocumentoVO.getAooId())) {

				pstmt = conn.prepareStatement(INSERT_TIPO_DOCUMENTO);
				pstmt.setInt(1, tipoDocumentoVO.getId().intValue());
				pstmt.setString(2, tipoDocumentoVO.getDescrizione());
				pstmt.setInt(3, tipoDocumentoVO.getAooId());
				pstmt.setString(4, tipoDocumentoVO.getFlagAttivazione());
				pstmt.setString(5, tipoDocumentoVO.getFlagDefault());
				pstmt.setLong(6, tipoDocumentoVO.getNumGGScadenza());
				pstmt.setString(7, tipoDocumentoVO.getRowCreatedUser());
				pstmt.setString(8, tipoDocumentoVO.getRowUpdatedUser());
				// pstmt.setString(9, tipoDocumentoVO.getPrezzo());
				pstmt.executeUpdate();
				tipoDocumentoVO.setReturnValue(ReturnValues.SAVED);
			}
		} catch (Exception e) {
			logger.error("Save TipoDocumento", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return tipoDocumentoVO;
	}

	public TipoDocumentoVO updateTipoDocumento(Connection conn,
			TipoDocumentoVO tipoDocumentoVO) throws DataException {
		PreparedStatement pstmt = null;
		tipoDocumentoVO.setReturnValue(ReturnValues.UNKNOWN);
		try {
			if (conn == null) {
				logger.warn("aggiornaTipoDocumento() - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			if ("1".equals(tipoDocumentoVO.getFlagDefault())) {
				pstmt = conn.prepareStatement(UPDATE_TIPO_DOCUMENTO_DEFAULT);
				pstmt.setInt(1, tipoDocumentoVO.getAooId());
				pstmt.executeUpdate();
				jdbcMan.close(pstmt);
			}

			if (!isDescriptionUsed(conn, tipoDocumentoVO.getDescrizione(),
					tipoDocumentoVO.getId().intValue(),
					tipoDocumentoVO.getAooId())) {
				pstmt = conn.prepareStatement(UPDATE_TIPO_DOCUMENTO);
				pstmt.setString(1, tipoDocumentoVO.getDescrizione());
				pstmt.setString(2, tipoDocumentoVO.getFlagDefault());
				pstmt.setString(3, tipoDocumentoVO.getFlagAttivazione());
				pstmt.setInt(4, tipoDocumentoVO.getNumGGScadenza());
				pstmt.setString(5, tipoDocumentoVO.getRowUpdatedUser());
				pstmt.setInt(6, tipoDocumentoVO.getId().intValue());

				pstmt.executeUpdate();
				tipoDocumentoVO.setReturnValue(ReturnValues.SAVED);
			}

		} catch (Exception e) {
			logger.error("Update TipoDocumento", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return tipoDocumentoVO;
	}

	public boolean isTipoDocumentoUtilizzato(Connection connection,
			int tipoDocumentoId) throws DataException {
		PreparedStatement pstmt = null;
		boolean utilizzato = false;
		ResultSet rs = null;
		try {
			pstmt = connection
					.prepareStatement(SELECT_TIPO_DOCUMENTO_IN_PROTOCOLLI);
			pstmt.setInt(1, tipoDocumentoId);
			rs = pstmt.executeQuery();
			rs.next();
			utilizzato = rs.getInt(1) > 0;

		} catch (Exception e) {
			logger.error("Amministrazione: isTipoDocumentoUtilizzato", e);
			throw new DataException(
					"Cannot load Amministrazione: isTipoDocumentoUtilizzato");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return utilizzato;

	}

	public boolean cancellaTipoDocumento(Connection conn, int tipoDocumentoId)
			throws DataException {
		PreparedStatement pstmt = null;
		boolean cancellato = false;
		try {
			if (conn == null) {
				logger.warn("cancellatIPOdOCUMENTO() - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			if (!isTipoDocumentoUtilizzato(conn, tipoDocumentoId)) {
				pstmt = conn.prepareStatement(DELETE_TIPO_DOCUMENTO);
				pstmt.setInt(1, tipoDocumentoId);
				pstmt.executeUpdate();
				cancellato = true;
			}

		} catch (Exception e) {
			logger.error("cancellaTipoDocumento TipoDocumento", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return cancellato;
	}

	public TipoProcedimentoVO getTipoProcedimento(int tipoProcedimentoId)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		TipoProcedimentoVO tipoProcedimento = new TipoProcedimentoVO();
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();

			pstmt = connection.prepareStatement(SELECT_TIPO_PROCEDIMENTO);
			pstmt.setInt(1, tipoProcedimentoId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				tipoProcedimento = new TipoProcedimentoVO();
				tipoProcedimento.setIdTipo(rs.getInt("tipo_procedimenti_id"));
				tipoProcedimento.setDescrizione(rs.getString("tipo"));
				tipoProcedimento.setTitolarioId(rs.getInt("titolario_id"));
				tipoProcedimento.setGiorniMax(rs.getInt("giorni_max"));
				tipoProcedimento.setGiorniAlert(rs.getInt("giorni_alert"));
				tipoProcedimento.setUll(rs.getBoolean("flag_ull"));
			}

		} catch (Exception e) {
			logger.error("Amministrazione: getTipoProcedimento", e);
			throw new DataException(
					"Cannot load Amministrazione: getTipoProcedimento");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return tipoProcedimento;
	}

	public TipoProcedimentoVO getTipoProcedimentoByProcedimento(int procedimentoId)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		TipoProcedimentoVO tipoProcedimento = new TipoProcedimentoVO();
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_TIPO_PROCEDIMENTO_BY_PROCEDIMENTO);
			pstmt.setInt(1, procedimentoId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				tipoProcedimento = new TipoProcedimentoVO();
				tipoProcedimento.setIdTipo(rs.getInt("tipo_procedimenti_id"));
				tipoProcedimento.setDescrizione(rs.getString("tipo"));
				tipoProcedimento.setTitolarioId(rs.getInt("titolario_id"));
				tipoProcedimento.setGiorniMax(rs.getInt("giorni_max"));
				tipoProcedimento.setGiorniAlert(rs.getInt("giorni_alert"));
				tipoProcedimento.setUll(rs.getBoolean("flag_ull"));
			}

		} catch (Exception e) {
			logger.error("Amministrazione: getTipoProcedimento", e);
			throw new DataException(
					"Cannot load Amministrazione: getTipoProcedimento");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return tipoProcedimento;
	}
	
	public ArrayList<TipoProcedimentoVO> getTipiProcedimento(int aooId)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		TipoProcedimentoVO tipo;
		ResultSet rs = null;
		ArrayList<TipoProcedimentoVO> lista = new ArrayList<TipoProcedimentoVO>();
		try {
			connection = jdbcMan.getConnection();

			pstmt = connection.prepareStatement(SELECT_TIPI_PROCEDIMENTO);
			pstmt.setInt(1, aooId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tipo = new TipoProcedimentoVO();
				tipo.setIdTipo(rs.getInt("tipo_procedimenti_id"));
				tipo.setTitolarioId(rs.getInt("titolario_id"));
				tipo.setDescrizione(rs.getString("tipo"));
				tipo.setGiorniMax(rs.getInt("giorni_max"));
				tipo.setGiorniAlert(rs.getInt("giorni_alert"));
				tipo.setUll(rs.getBoolean("flag_ull"));
				lista.add(tipo);
			}

		} catch (Exception e) {
			logger.error("Amministrazione: getTipiProcedimento", e);
			throw new DataException(
					"Cannot load Amministrazione: getTipiProcedimento");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return lista;
	}

	public ArrayList<TipoProcedimentoVO> getTipiProcedimentoByUfficio(
			int ufficioCorrente) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		TipoProcedimentoVO tipo;
		ResultSet rs = null;
		ArrayList<TipoProcedimentoVO> lista = new ArrayList<TipoProcedimentoVO>();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection
					.prepareStatement(SELECT_TIPI_PROCEDIMENTO_BY_UFFICIO);
			pstmt.setInt(1, ufficioCorrente);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tipo = new TipoProcedimentoVO();
				tipo.setIdTipo(rs.getInt("tipo_procedimenti_id"));
				tipo.setDescrizione(rs.getString("tipo"));
				tipo.setTitolarioId(rs.getInt("titolario_id"));
				tipo.setGiorniMax(rs.getInt("giorni_max"));
				tipo.setGiorniAlert(rs.getInt("giorni_alert"));
				tipo.setUll(rs.getBoolean("flag_ull"));
				lista.add(tipo);
			}
		} catch (Exception e) {
			logger.error("Amministrazione: getTipiProcedimento", e);
			throw new DataException(
					"Cannot load Amministrazione: getTipiProcedimento");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return lista;
	}
	
	public ArrayList<TipoProcedimentoVO> getTipiProcedimentoByUfficioPrincipale(
			int ufficioCorrente) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		TipoProcedimentoVO tipo;
		ResultSet rs = null;
		ArrayList<TipoProcedimentoVO> lista = new ArrayList<TipoProcedimentoVO>();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection
					.prepareStatement(SELECT_TIPI_PROCEDIMENTO_BY_UFFICIO_PRINCIPALE);
			pstmt.setInt(1, ufficioCorrente);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tipo = new TipoProcedimentoVO();
				tipo.setIdTipo(rs.getInt("tipo_procedimenti_id"));
				tipo.setDescrizione(rs.getString("tipo"));
				tipo.setTitolarioId(rs.getInt("titolario_id"));
				tipo.setGiorniMax(rs.getInt("giorni_max"));
				tipo.setGiorniAlert(rs.getInt("giorni_alert"));
				tipo.setUll(rs.getBoolean("flag_ull"));
				lista.add(tipo);
			}
		} catch (Exception e) {
			logger.error("Amministrazione: getTipiProcedimento", e);
			throw new DataException(
					"Cannot load Amministrazione: getTipiProcedimento");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return lista;
	}

	public ArrayList<TipoProcedimentoVO> getTipiProcedimentoByTitolario(
			int titolarioId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		TipoProcedimentoVO tipo;
		ResultSet rs = null;
		ArrayList<TipoProcedimentoVO> lista = new ArrayList<TipoProcedimentoVO>();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection
					.prepareStatement(SELECT_TIPI_PROCEDIMENTO_BY_TITOLARIO);
			pstmt.setInt(1, titolarioId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tipo = new TipoProcedimentoVO();
				tipo.setIdTipo(rs.getInt("tipo_procedimenti_id"));
				tipo.setDescrizione(rs.getString("tipo"));
				tipo.setTitolarioId(rs.getInt("titolario_id"));
				tipo.setGiorniMax(rs.getInt("giorni_max"));
				tipo.setGiorniAlert(rs.getInt("giorni_alert"));
				tipo.setUll(rs.getBoolean("flag_ull"));
				lista.add(tipo);
			}

		} catch (Exception e) {
			logger.error("Amministrazione: getTipiProcedimento", e);
			throw new DataException(
					"Cannot load Amministrazione: getTipiProcedimento");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return lista;
	}

	public TipoProcedimentoVO newTipoProcedimento(Connection conn,
			TipoProcedimentoVO tipo) throws DataException {
		PreparedStatement pstmt = null;
		try {
			tipo.setReturnValue(ReturnValues.UNKNOWN);
			if (conn == null) {
				logger.warn("newTipoDocumento() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			if (!isTipoProcedimentoUtilizzato(conn, tipo.getDescrizione())) {
				int id = tipo.getIdTipo();
				pstmt = conn.prepareStatement(INSERT_TIPO_PROCEDIMENTO);
				pstmt.setInt(1, id++);
				pstmt.setInt(2, tipo.getTitolarioId());
				pstmt.setString(3, tipo.getDescrizione());
				if (tipo.getGiorniMax() != 0)
					pstmt.setInt(4, tipo.getGiorniMax());
				else
					pstmt.setNull(4, Types.INTEGER);
				if (tipo.getGiorniAlert() != 0)
					pstmt.setInt(5, tipo.getGiorniAlert());
				else
					pstmt.setNull(5, Types.INTEGER);
				pstmt.executeUpdate();
				tipo.setReturnValue(ReturnValues.SAVED);
				IdentificativiDAO idenDao = (IdentificativiDAO) DAOFactory
						.getDAO(Constants.IDENTIFICATIVI_DAO_CLASS);
				idenDao.updateId(conn, "TIPI_PROCEDIMENTO", id,
						tipo.getIdTipo());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Save TipoProcedimento", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return tipo;

	}

	public ArrayList<String> getUfficiPerTipoProcedimento(Connection conn,
			int tipoProcedimentoId) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<String> ufficiUtilizzati = new ArrayList<String>();
		try {
			if (conn == null) {
				logger.warn("aggiornaTipoProcedimento() - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			pstmt = conn.prepareStatement(SELECT_UFFICI_PERTIPI_PROCEDIMENTO);
			pstmt.setInt(1, tipoProcedimentoId);
			rs = pstmt.executeQuery();
			while (rs.next()) {

				ufficiUtilizzati.add("" + rs.getInt("ufficio_id"));
			}
		} catch (Exception e) {
			logger.error("getUfficiPerTipoProcedimento", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return ufficiUtilizzati;
	}

	public boolean isTipoProcedimentoDescrizione(Connection connection,
			String tipo, int tipoProcedimentoId) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean used = true;
		try {
			if (connection == null) {
				logger.warn("isTipoProcedimentoUsed - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(CHECK_DESCRIZIONE_PROC_MOD);
			pstmt.setString(1, tipo.trim().toUpperCase());
			pstmt.setInt(2, tipoProcedimentoId);
			rs = pstmt.executeQuery();
			rs.next();
			used = rs.getInt(1) > 0;
		} catch (Exception e) {
			logger.error("isTipoUsed:" + tipo, e);
			throw new DataException("error.database.missing");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return used;
	}

	public TipoProcedimentoVO updateTipoProcedimento(Connection conn,
			TipoProcedimentoVO tipoProcedimentoVO) throws DataException {
		PreparedStatement pstmt = null;
		tipoProcedimentoVO.setReturnValue(ReturnValues.UNKNOWN);
		try {
			if (conn == null) {
				logger.warn("aggiornaTipoProcedimento() - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			if (!isTipoProcedimentoDescrizione(conn,
					tipoProcedimentoVO.getDescrizione(),
					tipoProcedimentoVO.getIdTipo())) {

				pstmt = conn.prepareStatement(UPDATE_TIPO_PROCEDIMENTO);
				pstmt.setString(1, tipoProcedimentoVO.getDescrizione());
				if (tipoProcedimentoVO.getGiorniMax() != 0)
					pstmt.setInt(2, tipoProcedimentoVO.getGiorniMax());
				else
					pstmt.setNull(2, Types.INTEGER);
				if (tipoProcedimentoVO.getGiorniAlert() != 0)
					pstmt.setInt(3, tipoProcedimentoVO.getGiorniAlert());
				else
					pstmt.setNull(3, Types.INTEGER);
				if (tipoProcedimentoVO.getTitolarioId() != 0)
					pstmt.setInt(4, tipoProcedimentoVO.getTitolarioId());
				else
					pstmt.setNull(4, Types.INTEGER);
				pstmt.setInt(5, tipoProcedimentoVO.getIdTipo());
				pstmt.executeUpdate();
				tipoProcedimentoVO.setReturnValue(ReturnValues.SAVED);

			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Update TipoProcedimento", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return tipoProcedimentoVO;
	}

	public boolean isTipoProcedimentoUtilizzato(Connection connection,
			String tipo, String[] ufficioId) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean used = false;
		try {
			if (connection == null) {
				logger.warn("isTipoProcedimentoUsed - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			for (int i = 0; i < ufficioId.length; i++) {
				pstmt = connection
						.prepareStatement(CHECK_DESCRIZIONE_PROC_UFFICI);
				pstmt.setString(1, tipo.trim().toUpperCase());
				pstmt.setInt(2, Integer.valueOf(ufficioId[i]).intValue());
				rs = pstmt.executeQuery();
				rs.next();
				if (rs.getInt(1) > 0) {
					used = rs.getInt(1) > 0;
					break;
				}
			}

		} catch (Exception e) {
			logger.error("isTipoUsed:" + tipo, e);
			throw new DataException("error.database.missing");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return used;
	}

	public boolean isTipoProcedimentoUtilizzato(Connection connection,
			String tipo) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean used = false;
		try {
			if (connection == null) {
				logger.warn("isTipoProcedimentoUsed - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			pstmt = connection.prepareStatement(CHECK_DESCRIZIONE_PROC);
			pstmt.setString(1, tipo.trim().toUpperCase());
			rs = pstmt.executeQuery();
			rs.next();
			if (rs.getInt(1) > 0) {
				used = rs.getInt(1) > 0;

			}

		} catch (Exception e) {
			logger.error("isTipoUsed:" + tipo, e);
			throw new DataException("error.database.missing");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return used;
	}

	public boolean cancellaTipoProcedimento(Connection conn,
			TipoProcedimentoVO tipoProcedimentoVO) throws DataException {
		PreparedStatement pstmt = null;
		boolean cancellato = false;
		try {
			if (conn == null) {
				logger.warn("cancellaTipoProcedimento() - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			pstmt = conn.prepareStatement(DELETE_TIPO_PROCEDIMENTO);
			pstmt.setInt(1, tipoProcedimentoVO.getIdTipo());
			pstmt.executeUpdate();
			cancellato = true;
			// }

		} catch (Exception e) {
			logger.error("cancellaTipoProcedimento TipoProcedimento", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return cancellato;
	}

	public boolean cancellaTipoProcedimento(Connection conn,
			int tipoProcedimentoId, String[] ufficiId) throws DataException {
		PreparedStatement pstmt = null;
		boolean cancellato = false;
		try {
			if (conn == null) {
				logger.warn("cancellaTipoProcedimento() - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(CANCELLA_TIPO_PROCEDIMENTO);
			pstmt.setInt(1, tipoProcedimentoId);
			pstmt.executeUpdate();
			cancellato = true;
		} catch (Exception e) {
			logger.error("cancellaTipoProcedimento TipoProcedimento", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return cancellato;
	}

	public boolean cancellaTipoProcedimento(Connection conn,
			int tipoProcedimentoId) throws DataException {
		PreparedStatement pstmt = null;
		boolean cancellato = false;
		try {
			if (conn == null) {
				logger.warn("cancellaTipoProcedimento() - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(DELETE_TIPO_PROCEDIMENTO);
			pstmt.setInt(1, tipoProcedimentoId);
			pstmt.executeUpdate();
			cancellato = true;
		} catch (Exception e) {
			logger.error("cancellaTipoProcedimento TipoProcedimento", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return cancellato;
	}

	public boolean isDescriptionUsed(Connection connection, String descrizione,
			int tipoDocumentoId, int aooId) throws DataException {
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
			pstmt = connection.prepareStatement(CHECK_DESCRIZIONE);
			pstmt.setString(1, descrizione.trim().toUpperCase());
			pstmt.setInt(2, tipoDocumentoId);
			pstmt.setInt(3, aooId);
			rs = pstmt.executeQuery();
			rs.next();
			used = rs.getInt(1) > 0;
		} catch (Exception e) {
			logger.error("isUsernameUsed:" + descrizione, e);
			throw new DataException("error.database.missing");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return used;
	}

	public boolean isMezzoSpedizioneUtilizzato(Connection connection,
			String mezzoSpedizione) throws DataException {
		PreparedStatement pstmt = null;
		boolean utilizzato = false;
		ResultSet rs = null;
		try {
			pstmt = connection
					.prepareStatement(SELECT_MEZZO_SPEDIZIONE_IN_PROTOCOLLI_DESTINATARI);
			pstmt.setString(1, mezzoSpedizione.toUpperCase().trim());
			rs = pstmt.executeQuery();
			rs.next();
			utilizzato = rs.getInt(1) > 0;

		} catch (Exception e) {
			logger.error("Amministrazione: isMezzoSpedizioneUtilizzato", e);
			throw new DataException(
					"Cannot load Amministrazione: isMezzoSpedizioneUtilizzato");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return utilizzato;

	}

	public ArrayList<LogAcquisizioneMassivaVO> getLogsAcquisizioneMassiva(int aooId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		LogAcquisizioneMassivaVO log;
		ResultSet rs = null;
		ArrayList<LogAcquisizioneMassivaVO> lista = new ArrayList<LogAcquisizioneMassivaVO>();
		try {
			connection = jdbcMan.getConnection();

			pstmt = connection
					.prepareStatement(SELECT_LOGS_ACQUISIZIONE_MASSIVA);
			pstmt.setInt(1, aooId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				log = new LogAcquisizioneMassivaVO();
				log.setAooId(rs.getInt("aoo_id"));
				log.setFileName(rs.getString("nome_file"));
				log.setErrore(rs.getString("errore"));
				log.setData(rs.getDate("data_log"));
				lista.add(log);
			}
		} catch (Exception e) {
			logger.error("Amministrazione: getLogsAcquisizioneMassiva", e);
			throw new DataException(
					"Cannot load Amministrazione: getLogsAcquisizioneMassiva");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return lista;
	}

	public boolean cancellaLogsAcquisizioneMassiva(int aooId)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = jdbcMan.getConnection();

			pstmt = connection
					.prepareStatement(DELETE_LOGS_ACQUISIZIONE_MASSIVA_BY_AOO);
			pstmt.setInt(1, aooId);
			return (pstmt.executeUpdate() > 0);

		} catch (Exception e) {
			logger.error("Amministrazione: cancellaLogsAcquisizioneMassiva", e);
			throw new DataException(
					"Cannot load Amministrazione: cancellaLogsAcquisizioneMassiva");
		} finally {
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
	}

	public LogAcquisizioneMassivaVO newLogAcquisizioneMassivaVO(
			Connection conn, LogAcquisizioneMassivaVO logVO) {
		PreparedStatement pstmt = null;
		try {
			if (conn == null) {
				logger.warn("newLogAcquisizioneMassivaVO() - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(INSERT_LOG_ACQUISIZIONE_MASSIVA);
			pstmt.setString(1, logVO.getFileName());
			pstmt.setString(2, logVO.getErrore());
			pstmt.setInt(3, logVO.getAooId());
			pstmt.setTimestamp(4, new Timestamp(logVO.getData().getTime()));
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("Save newLogAcquisizioneMassivaVO", e);
		} finally {
			jdbcMan.close(pstmt);
		}
		return logVO;
	}

	public Collection<IdentityVO> getElencoTitoliDestinatario() throws DataException {

		Collection<IdentityVO> listaTitoli = new ArrayList<IdentityVO>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_TITOLIDESTINATARI);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				IdentityVO titoloVO = new IdentityVO();
				titoloVO.setId(rs.getInt("id"));
				titoloVO.setDescription(rs.getString("descrizione"));
				listaTitoli.add(titoloVO);
			}
		} catch (Exception e) {
			logger.error("Load getElencoTitoliDestinatari", e);
			throw new DataException("Cannot load getElencoTitoliDestinatari");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return listaTitoli;

	}

	public IdentityVO getTitoloDestinatario(int id) throws DataException {

		IdentityVO titoloVO = new IdentityVO();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = jdbcMan.getConnection();
			pstmt = conn.prepareStatement(GET_TITOLO);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				titoloVO.setId(rs.getInt("id"));
				titoloVO.setDescription(rs.getString("descrizione"));
			}
		} catch (Exception e) {
			logger.error("Load getTitoloDestinatario", e);
			throw new DataException("Cannot load getTitoloDestinatario");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(conn);
		}

		return titoloVO;

	}

	public IdentityVO getTitoloDestinatarioDaTitolo(String titolo)
			throws DataException {

		IdentityVO titoloVO = new IdentityVO();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = jdbcMan.getConnection();
			pstmt = conn.prepareStatement(GET_TITOLO_DA_TITOLO);
			pstmt.setString(1, titolo);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				titoloVO.setId(rs.getInt("id"));
				titoloVO.setDescription(rs.getString("descrizione"));
			}
		} catch (Exception e) {
			logger.error("Load getTitoloDestinatarioDaTitolo", e);
			throw new DataException("Cannot load getTitoloDestinatarioDaTitolo");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(conn);
		}

		return titoloVO;

	}

	public IdentityVO newTitoloDestinatario(Connection conn, IdentityVO titoloVO)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (conn == null) {
				logger.warn("newTitolo() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			if (!esisteTitolo(titoloVO.getDescription())) {
				pstmt = conn.prepareStatement(INSERT_TITOLO);
				pstmt.setInt(1, titoloVO.getId().intValue());
				pstmt.setString(2, titoloVO.getDescription());
				pstmt.executeUpdate();
				titoloVO.setReturnValue(ReturnValues.SAVED);
			} else {
				titoloVO.setReturnValue(ReturnValues.FOUND);
			}

		} catch (Exception e) {
			logger.error("Save Titolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return titoloVO;
	}

	public boolean esisteTitolo(String titolo) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean esiste = false;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_TITOLI_BY_DESC);
			pstmt.setString(1, titolo.toUpperCase());

			rs = pstmt.executeQuery();
			rs.next();
			if (rs.getInt(1) > 0) {
				esiste = true;
			}
		} catch (Exception e) {
			logger.error("esisteTitolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return esiste;
	}

	public boolean esisteTitoloInProtocolloDestinatari(int id)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean esiste = false;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_TITOLO_IN_PROTDEST);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			rs.next();
			if (rs.getInt(1) > 0) {
				esiste = true;
			}
		} catch (Exception e) {
			logger.error("esisteTitolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return esiste;
	}

	public boolean esisteTitoloInStoriaProtDest(int id) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean esiste = false;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection
					.prepareStatement(SELECT_TITOLO_IN_STORIAPROTDEST);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			rs.next();
		} catch (Exception e) {
			logger.error("esisteTitolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return esiste;
	}

	public boolean esisteTitoloInInvioClassificatiProtDest(int id)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean esiste = false;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection
					.prepareStatement(SELECT_TITOLO_IN_INVIOCLASSIFICATIDESTINATARI);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			rs.next();
		} catch (Exception e) {
			logger.error("esisteTitolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return esiste;
	}

	public boolean esisteTitoloInInvioFascicoliDestinatari(int id)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean esiste = false;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection
					.prepareStatement(SELECT_TITOLO_IN_INVIOFASCICOLIDESTINATARI);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			rs.next();
		} catch (Exception e) {
			logger.error("esisteTitolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return esiste;
	}

	public IdentityVO updateTitolo(Connection conn, IdentityVO titoloVO)
			throws DataException {
		PreparedStatement pstmt = null;
		titoloVO.setReturnValue(ReturnValues.UNKNOWN);
		try {
			if (conn == null) {
				logger.warn("aggiornaMezzoSpedizione() - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			if (!esisteTitolo(titoloVO.getDescription())) {
				pstmt = conn.prepareStatement(UPDATE_TITOLO);
				pstmt.setString(1, titoloVO.getDescription());
				pstmt.setInt(2, titoloVO.getId().intValue());
				pstmt.executeUpdate();
				titoloVO.setReturnValue(ReturnValues.SAVED);
			} else {
				titoloVO.setReturnValue(ReturnValues.FOUND);
			}
		} catch (Exception e) {
			logger.error("Update Titolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return titoloVO;

	}

	public boolean deleteTitolo(int id) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		boolean eliminato = false;
		try {
			connection = jdbcMan.getConnection();
			if (connection == null) {
				logger.warn("deleteTitolo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			pstmt = connection.prepareStatement(DELETE_TITOLO);
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
			eliminato = true;
		} catch (Exception e) {
			logger.error("Delete Titolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return eliminato;
	}

	public final static String INSERT_LOG_ACQUISIZIONE_MASSIVA = "INSERT INTO acquisizione_massiva_logs"
			+ " (nome_file, errore, aoo_id, data_log) VALUES(?, ?, ?, ?)";

	public final static String DELETE_LOGS_ACQUISIZIONE_MASSIVA_BY_AOO = "DELETE FROM acquisizione_massiva_logs WHERE aoo_id=?";

	public final static String SELECT_LOGS_ACQUISIZIONE_MASSIVA = "SELECT * FROM acquisizione_massiva_logs WHERE aoo_id=?";

	public final static String SELECT_MEZZO_SPEDIZIONE_IN_PROTOCOLLI_DESTINATARI = "SELECT COUNT(DESTINATARIO_ID) FROM PROTOCOLLO_DESTINATARI "
			+ "WHERE UPPER(MEZZO_SPEDIZIONE) =?";

	// Tipo Documento

	protected final static String CHECK_DESCRIZIONE = "SELECT count(tipo_documento_id) FROM TIPI_DOCUMENTO "
			+ "WHERE upper(desc_tipo_documento) =? and tipo_documento_id !=? and aoo_id=?";

	public final static String SELECT_TIPO_DOCUMENTO_IN_PROTOCOLLI = "SELECT COUNT(protocollo_id) FROM PROTOCOLLI "
			+ "WHERE tipo_documento_id =?";

	public final static String SELECT_TIPI_DOCUMENTO = "SELECT a.*, (select count(protocollo_id) from PROTOCOLLI b "
			+ " where a.tipo_documento_id = b.tipo_documento_id) as contatore FROM TIPI_DOCUMENTO a"
			+ " where aoo_id=? ORDER BY desc_tipo_documento";

	public final static String SELECT_TIPO_DOCUMENTO = "SELECT * FROM TIPI_DOCUMENTO "
			+ "WHERE tipo_documento_id =?";

	public final static String INSERT_TIPO_DOCUMENTO = "INSERT INTO TIPI_DOCUMENTO"
			+ " (tipo_documento_id, desc_tipo_documento, aoo_id, flag_attivazione, flag_default,  nume_gg_scadenza,"
			+ " row_created_user, row_updated_user)"
			+ " VALUES(?, ?, ?, ?, ?, ? ,?, ?)";

	public final static String UPDATE_TIPO_DOCUMENTO = "UPDATE TIPI_DOCUMENTO "
			+ " SET desc_tipo_documento=?, flag_default=?, flag_attivazione=?, nume_gg_scadenza=?, row_updated_user=? "
			+ "WHERE tipo_documento_id=?";

	public final static String UPDATE_TIPO_DOCUMENTO_DEFAULT = "UPDATE TIPI_DOCUMENTO  SET flag_default='0' "
			+ " WHERE aoo_id =?";

	public final static String DELETE_TIPO_DOCUMENTO = "DELETE FROM TIPI_DOCUMENTO "
			+ " WHERE tipo_documento_id=?";

	// Tipo Procedimento

	protected final static String CHECK_DESCRIZIONE_PROC_UFFICI = "SELECT count(tipo_procedimenti_id) FROM TIPI_PROCEDIMENTO "
			+ "WHERE upper(tipo) =? and  ufficio_id=?";

	protected final static String CHECK_DESCRIZIONE_PROC = "SELECT count(tipo_procedimenti_id) FROM TIPI_PROCEDIMENTO "
			+ "WHERE upper(tipo) =?";

	public final static String CHECK_PROCEDIMENTO_USATO = "SELECT count(tipo_procedimento) FROM PROCEDIMENTI "
			+ "WHERE tipo_procedimento=?";

	protected final static String CHECK_DESCRIZIONE_PROC_MOD = "SELECT count(tipo_procedimenti_id) FROM TIPI_PROCEDIMENTO "
			+ "WHERE upper(tipo) =? and tipo_procedimenti_id !=?";
	

	public final static String SELECT_TIPI_PROCEDIMENTO = "SELECT tipo_procedimenti_id, tipi_procedimento.tipo, tipi_procedimento.titolario_id ,giorni_max,giorni_alert,flag_ull "
			+ " FROM TIPI_PROCEDIMENTO tipi_procedimento, TITOLARIO titolario"
			+ " WHERE titolario.titolario_id=tipi_procedimento.titolario_id"
			+ " AND titolario.aoo_id=?" + " ORDER BY tipo";

	public final static String SELECT_TIPO_PROCEDIMENTO = "SELECT tipo_procedimenti_id, tipo, titolario_id, giorni_max,giorni_alert,flag_ull"
			+ " FROM TIPI_PROCEDIMENTO " + " WHERE  tipo_procedimenti_id =?";

	public final static String SELECT_TIPO_PROCEDIMENTO_BY_PROCEDIMENTO = "SELECT t.tipo_procedimenti_id, t.tipo, t.titolario_id, t.giorni_max, t.giorni_alert, t.flag_ull"
			+ " FROM tipi_procedimento t LEFT JOIN procedimenti p ON(t.tipo_procedimenti_id=p.tipo_procedimento_id)" 
			+ " WHERE  p.procedimento_id =?";

	
	public final static String SELECT_TIPI_PROCEDIMENTO_BY_UFFICIO = "SELECT tp.tipo_procedimenti_id, tp.tipo, tit.titolario_id, tp.giorni_max, tp.giorni_alert, tp.flag_ull"
			+ " FROM tipi_procedimento tp LEFT JOIN tipi_procedimento_uffici tpu ON (tp.tipo_procedimenti_id=tpu.tipo_procedimenti_id)"
			+ " LEFT JOIN titolario tit ON (tit.titolario_id=tp.titolario_id) WHERE tpu.ufficio_id=? ORDER BY tp.tipo";
	
	public final static String SELECT_TIPI_PROCEDIMENTO_BY_UFFICIO_PRINCIPALE = "SELECT tp.tipo_procedimenti_id, tp.tipo, tit.titolario_id, tp.giorni_max, tp.giorni_alert, tp.flag_ull"
			+ " FROM tipi_procedimento tp LEFT JOIN tipi_procedimento_uffici tpu ON (tp.tipo_procedimenti_id=tpu.tipo_procedimenti_id)"
			+ " LEFT JOIN titolario tit ON (tit.titolario_id=tp.titolario_id) WHERE tpu.ufficio_id=? AND tpu.flag_principale=1 ORDER BY tp.tipo";

	public final static String SELECT_TIPI_PROCEDIMENTO_BY_TITOLARIO = "SELECT tipo_procedimenti_id, tipi_procedimento.tipo, titolario.titolario_id, giorni_max,giorni_alert,flag_ull "
			+ " FROM TIPI_PROCEDIMENTO tipi_procedimento, TITOLARIO titolario"
			+ " WHERE titolario.titolario_id=tipi_procedimento.titolario_id"
			+ " AND titolario.titolario_id=? ORDER BY tipo";

	public final static String CONTA_UFFICI = "SELECT count(*) FROM TIPI_PROCEDIMENTO "
			+ "WHERE tipo_procedimenti_id =?";

	public final static String INSERT_TIPO_PROCEDIMENTO = "INSERT INTO TIPI_PROCEDIMENTO"
			+ " (tipo_procedimenti_id, titolario_id, tipo,giorni_max,giorni_alert"
			+ " )" + " VALUES(?,?,?,?,?)";

	public final static String UPDATE_TIPO_PROCEDIMENTO = "UPDATE TIPI_PROCEDIMENTO "
			+ " SET tipo=?,giorni_max=?,giorni_alert=?,titolario_id=? WHERE tipo_procedimenti_id=?";

	public final static String DELETE_TIPO_PROCEDIMENTO = "DELETE FROM TIPI_PROCEDIMENTO "
			+ " WHERE tipo_procedimenti_id=? AND flag_ull=0";

	public final static String CANCELLA_TIPO_PROCEDIMENTO = "DELETE FROM TIPI_PROCEDIMENTO "
			+ " WHERE tipo_procedimenti_id=? and ufficio_id=? AND flag_ull=0";

	// Fine Tipo Procedimento

	public final static String DELETE_PROFILO = "DELETE FROM PROFILI WHERE profilo_id=?";

	public final static String DELETE_MENU_PROFILO = "DELETE FROM PROFILI$MENU WHERE profilo_id=?";

	public final static String UPDATE_PROFILO = "UPDATE PROFILI"
			+ " SET codi_profilo=?,  desc_profilo =?, data_inizio_validita=?, data_fine_validita=?, row_updated_user=? WHERE profilo_id=?";

	public final static String SELECT_PROFILO = "SELECT * FROM PROFILI WHERE profilo_id=? ORDER BY desc_profilo";

	public final static String SELECT_PROFILI = "SELECT * FROM PROFILI where aoo_id=? ORDER BY desc_profilo";

	public final static String INSERT_MENU_PROFILO = "INSERT INTO PROFILI$MENU"
			+ " (profilo_id, menu_id, row_created_user, row_updated_user) VALUES(?, ?, ?, ?)";

	public final static String INSERT_PROFILO = "INSERT INTO PROFILI"
			+ " (profilo_id, codi_profilo, desc_profilo, data_inizio_validita, data_fine_validita,"
			+ "row_created_user, row_updated_user, aoo_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

	public final static String SELECT_FUNZIONI_MENU = "SELECT * FROM MENU WHERE LINK IS NOT NULL ORDER BY parent_id, posizione, descrizione";

	public final static String SELECT_FUNZIONI_MENU_BY_FUNCTION = "SELECT * FROM MENU WHERE LINK IS NOT NULL AND root_function=? ORDER BY parent_id, posizione, descrizione";

	public final static String SELECT_MENU_PROFILO = "SELECT menu_id, profilo_id FROM PROFILI$MENU "
			+ " WHERE profilo_id=?";

	public final static String DELETE_MEZZO_SPEDIZIONE = "DELETE FROM SPEDIZIONI "
			+ " WHERE spedizioni_id=?";

	public final static String UPDATE_MEZZO_SPEDIZIONE = "UPDATE SPEDIZIONI "
			+ " SET desc_spedizione=?, row_updated_user=?, flag_abilitato=?, flag_cancellabile=?, prezzo=?  WHERE spedizioni_id=?";

	public final static String INSERT_MEZZO_SPEDIZIONE = "INSERT INTO SPEDIZIONI"
			+ " (spedizioni_id,aoo_id, flag_abilitato, flag_cancellabile, codi_spedizione, desc_spedizione, row_created_user, row_updated_user,prezzo)"
			+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?,?)";

	public final static String SELECT_MEZZI_SPEDIZIONE = "SELECT * FROM SPEDIZIONI "
			+ " WHERE LOWER(desc_spedizione) LIKE ? and aoo_id=?"
			+ " ORDER BY desc_spedizione";

	private static final String SELECT_CARICHE = "SELECT * FROM cariche WHERE profilo_id=?";

	public final static String SELECT_MEZZO_SPEDIZIONE = "SELECT * FROM SPEDIZIONI "
			+ " WHERE spedizioni_id= ?";

	protected final static String SELECT_MENU_BY_PROFILO = "SELECT * FROM profili$menu WHERE profilo_id=?";

	// Titoli Destinatario

	private final static String SELECT_TITOLIDESTINATARI = "SELECT * FROM titoli_destinatari WHERE id<>999"
			+ " ORDER BY descrizione ";

	private final static String GET_TITOLO = "SELECT * FROM titoli_destinatari WHERE id=?";

	private final static String GET_TITOLO_DA_TITOLO = "SELECT * FROM titoli_destinatari WHERE descrizione=?";

	private final static String UPDATE_TITOLO = "UPDATE titoli_destinatari "
			+ " SET descrizione=?" + " WHERE id=?";

	private final static String INSERT_TITOLO = "INSERT INTO titoli_destinatari "
			+ " (id, descrizione) " + " VALUES(?,?)";

	private final static String SELECT_TITOLI_BY_DESC = "SELECT count(*) FROM titoli_destinatari where UPPER(descrizione)=?";

	private final static String SELECT_TITOLO_IN_PROTDEST = "SELECT count(*) FROM protocollo_destinatari where titolo_id=?";

	private final static String SELECT_TITOLO_IN_STORIAPROTDEST = "SELECT count(*) FROM storia_protocollo_destinatari where titolo_id=?";

	private final static String SELECT_TITOLO_IN_INVIOCLASSIFICATIDESTINATARI = "SELECT count(*) FROM invio_classificati_destinatari where titolo_id=?";

	private final static String SELECT_TITOLO_IN_INVIOFASCICOLIDESTINATARI = "SELECT count(*) FROM invio_fascicoli_destinatari where titolo_id=?";

	private final static String DELETE_TITOLO = "DELETE FROM titoli_destinatari WHERE id=?";

	private final static String SELECT_UFFICI_PERTIPI_PROCEDIMENTO = "SELECT ufficio_id FROM TIPI_PROCEDIMENTO WHERE tipo_procedimenti_id =?";

	public final static String INSERT_RIFERIMENTI = "INSERT INTO riferimenti_legislativi"
			+ " ( riferimento_id,tipo_procedimenti_id,documento_id) VALUES (?, ?, ?)";

	private final static String INSERT_AMMINISTRAZIONI_PARTECIPANTI = "INSERT INTO amministrazioni_partecipanti"
			+ " (amministrazioni_id, rubrica_id, tipo_procedimenti_id, nominativo)  VALUES (?, ?, ?, ?)";

	public final static String AMMINISTRAZIONI_PARTECIPANTI = "SELECT * FROM amministrazioni_partecipanti WHERE tipo_procedimenti_id=?";

	public final static String SELECT_RIFERIMENTI = "SELECT documento_id FROM riferimenti_legislativi WHERE tipo_procedimenti_id = ?";

	public final static String DELETE_RIFERIMENTI = "DELETE FROM riferimenti_legislativi WHERE tipo_procedimenti_id = ?";

	public final static String DELETE_ASSEGNATARI = "DELETE FROM amministrazioni_partecipanti WHERE tipo_procedimenti_id = ?";
	
	public final static String DELETE_UFFICI_PARTECIPANTI = "DELETE FROM tipi_procedimento_uffici WHERE tipo_procedimenti_id = ?";

	public final static String UFFICI_PARTECIPANTI = "SELECT tu.ufficio_id, tu.flag_principale, u.descrizione FROM tipi_procedimento_uffici tu LEFT JOIN uffici u ON(tu.ufficio_id=u.ufficio_id) WHERE tu.tipo_procedimenti_id=? ";
	
	public final static String UFFICI_PARTECIPANTI_PROCEDIMENTO = "SELECT tu.ufficio_id, u.descrizione FROM tipi_procedimento_uffici tu LEFT JOIN uffici u ON(tu.ufficio_id=u.ufficio_id) WHERE tu.tipo_procedimenti_id=? AND tu.flag_principale=0";

	
	public final static String INSERT_UFFICIO_PARTECIPANTE = "INSERT INTO tipi_procedimento_uffici (tpu_id,tipo_procedimenti_id,ufficio_id,flag_principale) VALUES (?, ?, ?, ?)";

	public boolean isTipoProcedimentoUtilizzato(Connection connection,
			String tipo, int tipoProcedimentoId, int ufficioId)
			throws DataException {
		return false;
	}

	public boolean isTipoProcedimentoUsato(Connection connection,
			int tipoProcedimentoId) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean used = false;
		try {
			if (connection == null) {
				logger.warn("isTipoProcedimentoUsed - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			pstmt = connection.prepareStatement(CHECK_PROCEDIMENTO_USATO);
			pstmt.setInt(1, tipoProcedimentoId);
			rs = pstmt.executeQuery();
			rs.next();
			used = rs.getInt(1) > 0;
		} catch (Exception e) {
			logger.error("isTipoUsed:" + tipoProcedimentoId, e);
			throw new DataException("error.database.missing");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return used;
	}

	public boolean cancellaTipoProcedimento(Connection conn,
			int tipoProcedimentoId, int idUfficio) throws DataException {
		PreparedStatement pstmt = null;
		boolean cancellato = false;
		try {
			if (conn == null) {
				logger.warn("cancellaTipoProcedimento() - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			// int ufficioId = ;
			if (!isTipoProcedimentoUsato(conn, tipoProcedimentoId)) {
				pstmt = conn.prepareStatement(CANCELLA_TIPO_PROCEDIMENTO);
				pstmt.setInt(1, tipoProcedimentoId);
				pstmt.setInt(2, idUfficio);
				pstmt.executeUpdate();
				cancellato = true;
			}

		} catch (Exception e) {
			logger.error("cancellaTipoProcedimento TipoProcedimento", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return cancellato;
	}

	public ArrayList<TipoProcedimentoVO> getTipiProcedimento(int aooId, String descrizione)
			throws DataException {
		try {
			Connection connection = null;
			PreparedStatement pstmt = null;
			TipoProcedimentoVO tipoProcedimento;
			ResultSet rs = null;
			ArrayList<TipoProcedimentoVO> lista = new ArrayList<TipoProcedimentoVO>();
			try {
				connection = jdbcMan.getConnection();
				String query = "SELECT tipo_procedimenti_id, tipi_procedimento.tipo, "
						+ "titolario.titolario_id "
						+ "FROM TIPI_PROCEDIMENTO tipi_procedimento, TITOLARIO titolario "
						+ " WHERE titolario.titolario_id=tipi_procedimento.titolario_id "
						+ " AND titolario.aoo_id=? ";

				if (descrizione != null && !"".equals(descrizione.trim())) {
					query += "and upper(tipi_procedimento.TIPO) LIKE ?";
				}
				pstmt = connection.prepareStatement(query);
				logger.debug(query);
				pstmt.setInt(1, aooId);
				if (descrizione != null && !"".equals(descrizione.trim())) {
					pstmt.setString(2, descrizione.toUpperCase() + "%");
				}

				rs = pstmt.executeQuery();
				while (rs.next()) {
					tipoProcedimento = new TipoProcedimentoVO();
					tipoProcedimento.setIdTipo(rs
							.getInt("tipo_procedimenti_id"));
					tipoProcedimento.setDescrizione(rs.getString("tipo"));
					tipoProcedimento.setTitolarioId(rs.getInt("titolario_id"));
					lista.add(tipoProcedimento);
				}

			} catch (Exception e) {
				logger.error("get tipoProcedimento", e);
				throw new DataException("Cannot load tipoProcedimento");
			} finally {
				jdbcMan.close(rs);
				jdbcMan.close(pstmt);
				jdbcMan.close(connection);
			}
			return lista;
		} catch (DataException de) {
			logger.error("AmministrazioneDAOjdbc: failed getting getElencoListaDistribuzione: ");
			return null;
		}
	}

	public Collection<UtenteVO> getUtentiByProfilo(Connection connection, Integer id)
			throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Collection<UtenteVO>  utenti = new ArrayList<UtenteVO>();
		try {
			if (connection == null) {
				logger.warn("getUtentiByProfilo - Invalid Connection :"+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			UtenteVO c;
			pstmt = connection.prepareStatement("provazza");
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				c = new UtenteVO();
				// come in getUtente
				c.setId(rs.getInt("utente_id"));
				c.setUsername(rs.getString("user_name"));
				c.setEmailAddress(rs.getString("email"));
				c.setCognome(rs.getString("cognome"));
				c.setNome(rs.getString("nome"));
				c.setCodiceFiscale(rs.getString("codicefiscale"));
				c.setMatricola(rs.getString("matricola"));
				c.setPassword(rs.getString("passwd"));
				c.setDataFineAttivita(rs.getDate("data_fine_attivita"));
				c.setAooId(rs.getInt("aoo_id"));
				c.setRowCreatedTime(rs.getDate("row_created_time"));
				c.setRowCreatedUser(rs.getString("row_created_user"));
				c.setRowUpdatedUser(rs.getString("row_updated_user"));
				c.setRowUpdatedTime(rs.getDate("row_updated_time"));
				c.setVersione(rs.getInt("versione"));
				c.setAbilitato(rs.getBoolean("flag_abilitato"));
				c.setReturnValue(ReturnValues.FOUND);
				utenti.add(c);

			}
		} catch (Exception e) {
			throw new DataException("error.database.missing");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return utenti;
	}

	public Collection<CaricaVO> getCaricheByProfilo(Connection conn, Integer id)
			throws DataException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CaricaVO c = null;
		ArrayList<CaricaVO> cariche = new ArrayList<CaricaVO>();
		try {
			if (conn == null) {
				logger.warn("getCaricheByProfilo - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(SELECT_CARICHE);
			pstmt.setInt(1, id);
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
				c.setReturnValue(ReturnValues.FOUND);
				cariche.add(c);

			}
		} catch (Exception e) {
			logger.error("getCariche", e);
			throw new DataException("Cannot load getCariche");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return cariche;
	}

}