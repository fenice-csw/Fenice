package it.finsiel.siged.dao.jdbc;

import it.compit.fenice.mvc.presentation.helper.ProtocolloProcedimentoView;
import it.compit.fenice.mvc.vo.protocollo.UfficioPartecipanteVO;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.TitolarioBO;
import it.finsiel.siged.mvc.business.DocumentoDelegate;
import it.finsiel.siged.mvc.integration.ProcedimentoDAO;
import it.finsiel.siged.mvc.presentation.helper.AllaccioView;
import it.finsiel.siged.mvc.presentation.helper.ProcedimentoView;
import it.finsiel.siged.mvc.vo.MigrazioneVO;
import it.finsiel.siged.mvc.vo.lookup.TipoProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProcedimentoFaldoneVO;
import it.finsiel.siged.mvc.vo.protocollo.ProcedimentoFascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.ProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloProcedimentoVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.StringUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public class ProcedimentoDAOjdbc implements ProcedimentoDAO {
	static Logger logger = Logger.getLogger(ProcedimentoDAOjdbc.class.getName());

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
	
	public void salvaUfficioPartecipante(Connection connection, int tpu_id,
			int tipoId, int ufficioId, boolean visibilita, int versione, String username) throws DataException {
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
			pstmt.setInt(4, visibilita? 1 : 0);
			pstmt.setString(5, username);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("salvaUfficioPartecipante", e);
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
				uff.setVisibilita(rs.getInt("flag_visibilita")==1?true:false);
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
	
	public Map<String, UfficioPartecipanteVO> getUfficiPartecipantiMap(int id)
			throws DataException {
		Map<String, UfficioPartecipanteVO> uffici = new HashMap<String, UfficioPartecipanteVO>(2);		
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
				uff.setVisibilita(rs.getInt("flag_visibilita")==1?true:false);
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

	public boolean isProcedimentoVisualizzabile(int procedimentoId, int caricaId)
			throws DataException {
		boolean visualizzabile = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(PROCEDIMENTO_VISUALIZZABILE);
			pstmt.setInt(1, caricaId);
			pstmt.setInt(2, caricaId);
			pstmt.setInt(3, caricaId);
			pstmt.setInt(4, caricaId);
			pstmt.setInt(5, procedimentoId);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				visualizzabile = true;
			}
		} catch (Exception e) {
			logger.error("isProcedimentoVisualizzabile", e);
			throw new DataException("Cannot load isProcedimentoVisualizzabile");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return visualizzabile;
	}

	public AllaccioView getProtocolloAllacciabile(Utente utente,
			int annoProtocollo, int numeroProtocollo, int procedimentoId)
			throws DataException {
		AllaccioView allaccio = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer strQuery = new StringBuffer(SELECT_ALLACCIO);
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery.toString());
			pstmt.setInt(1, utente.getRegistroInUso());
			pstmt.setInt(2, procedimentoId);
			pstmt.setInt(3, numeroProtocollo);
			pstmt.setInt(4, annoProtocollo);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				allaccio = new AllaccioView();
				allaccio.setProtAllacciatoId(rs.getInt("protocollo_id"));
				allaccio.setAnnoProtAllacciato(rs.getInt("ANNO_REGISTRAZIONE"));
				allaccio.setNumProtAllacciato(rs.getInt("NUME_PROTOCOLLO"));
				allaccio.setDataProtocollo(DateUtil.formattaData(rs.getDate(
						"data_registrazione").getTime()));
				allaccio.setTipoProtocollo(rs.getString("FLAG_TIPO"));
				allaccio.setOggetto(rs.getString("TEXT_OGGETTO"));
				if (rs.getString("FLAG_TIPO_MITTENTE").equals("F")) {
					allaccio.setMittente(rs.getString("DESC_COGNOME_MITTENTE")
							+ " "
							+ StringUtil.getStringa(rs
									.getString("DESC_NOME_MITTENTE")));
				} else {
					allaccio.setMittente(rs
							.getString("DESC_DENOMINAZIONE_MITTENTE"));
				}
			}
		} catch (Exception e) {
			logger.error("getProtocolliAllacciabili", e);
			throw new DataException("Cannot load getProtocolliAllacciabili");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return allaccio;

	}

	private ProcedimentoView getRicorso(ResultSet rs,ProcedimentoView proc) {
		try {
			proc.setProcedimentoId(rs.getInt("procedimento_id"));
			proc.setNumeroProcedimento(rs.getString("numero_procedimento"));
			proc.setStatoId(rs.getInt("stato_id"));
			proc.setOggetto(rs.getString("oggetto"));
			proc.setDataAvvio(rs.getDate("data_avvio"));
			proc.setInteressato(rs.getString("interessato"));
		} catch (Exception e) {
			logger.error("getRicorso", e);
		}
		return proc;
	}

	public Map<String,ProcedimentoView> getRicorsi(int caricaId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		Map<String,ProcedimentoView> ricorsi = new HashMap<String,ProcedimentoView>(2);
		ProcedimentoView proc = null;
		ResultSet rs = null;
		try {
			
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_RICORSO_RIASSEGNATO);
			pstmt.setInt(1, caricaId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				proc=new ProcedimentoView();
				proc.setRiassegnato(true);
				proc = getRicorso(rs,proc);
				ricorsi.put(String.valueOf(proc.getProcedimentoId()), proc);
			}
			jdbcMan.closeResultSetAndStatement(rs, pstmt);
			pstmt = connection.prepareStatement(SELECT_RICORSO_FASE_ISTRUTTORIA);
			pstmt.setInt(1, caricaId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				proc=new ProcedimentoView();
				proc = getRicorso(rs,proc);
				ricorsi.put(String.valueOf(proc.getProcedimentoId()), proc);
			}
			jdbcMan.closeResultSetAndStatement(rs, pstmt);
			pstmt = connection.prepareStatement(SELECT_RICORSO_FASE_RELATORIA);
			pstmt.setInt(1, caricaId);
			pstmt.setInt(2, caricaId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				proc=new ProcedimentoView();
				proc = getRicorso(rs,proc);
				ricorsi.put(String.valueOf(proc.getProcedimentoId()), proc);
			}
			jdbcMan.closeResultSetAndStatement(rs, pstmt);
			pstmt = connection.prepareStatement(SELECT_RICORSO_PARERE_CONSIGLIO);
			pstmt.setInt(1, caricaId);
			pstmt.setInt(2, caricaId);
			pstmt.setInt(3, caricaId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				proc=new ProcedimentoView();
				proc = getRicorso(rs,proc);
				ricorsi.put(String.valueOf(proc.getProcedimentoId()), proc);
			}
			jdbcMan.closeResultSetAndStatement(rs, pstmt);
			pstmt = connection.prepareStatement(SELECT_RICORSO_DECRETO_PRESIDENTE);
			pstmt.setInt(1, caricaId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				proc=new ProcedimentoView();
				proc = getRicorso(rs,proc);
				ricorsi.put(String.valueOf(proc.getProcedimentoId()), proc);
			}
			
			pstmt = connection.prepareStatement(SELECT_ATTENDI_RICORSO_DECRETO_PRESIDENTE);
			pstmt.setInt(1, caricaId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				proc=new ProcedimentoView();
				proc = getRicorso(rs,proc);
				ricorsi.put(String.valueOf(proc.getProcedimentoId()), proc);
			}

		} catch (Exception e) {
			logger.error("getRicorsi", e);
			throw new DataException("getRicorsi");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return ricorsi;
	}

	
	public Collection<ProcedimentoView> getElencoDecreti(Collection<Integer> ids) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		List<ProcedimentoView> ricorsi = new ArrayList<ProcedimentoView>();
		ProcedimentoView proc = null;
		ResultSet rs = null;
		try {
			StringBuilder query=new StringBuilder("SELECT p.* FROM fenice.procedimenti p "
					+ " WHERE");
			query.append(" p.procedimento_id IN (");
			for(Integer id:ids){
				query.append(id+",");
			}
			query.append("0)");
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(query.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				proc=new ProcedimentoView();
				proc = getRicorso(rs,proc);
				ricorsi.add(proc);
			}
		} catch (Exception e) {
			logger.error("getRicorsi", e);
			throw new DataException("getRicorsi");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return ricorsi;
	}
	
	public void cancellaProcedimentoFascicolo(Connection conn,
			int procedimentoId, int fascicoloId) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (conn == null) {
				logger.warn("deleteFascicoloProcedimento() - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(DELETE_FASCICOLO_PROCEDIMENTO);
			pstmt.setInt(1, fascicoloId);
			pstmt.setInt(2, procedimentoId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("Error deleteFascicoloProcedimento", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void salvaProcedimentoFascicolo(Connection connection,
			int procedimentoId, int fascicoloId, String utente, int versione)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("salvaProcedimentoFascicolo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			pstmt = connection.prepareStatement(INSERT_FASCICOLO_PROCEDIMENTO);
			pstmt.setInt(1, fascicoloId);
			pstmt.setInt(2, procedimentoId);
			pstmt.setInt(3, versione);
			pstmt.setString(4, utente);
			pstmt.executeUpdate();
			logger.debug("Inserito Procedimento Fascicolo:" + fascicoloId + ","
					+ procedimentoId);

		} catch (Exception e) {
			logger.error("salvaProcedimentoFascicolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public int contaRicorsi(int caricaId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		int count = 0;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();

			pstmt = connection.prepareStatement(COUNT_RICORSO_RIASSEGNATO);
			pstmt.setInt(1, caricaId);
			rs = pstmt.executeQuery();
			if (rs.next())
				count += rs.getInt(1);
			jdbcMan.closeResultSetAndStatement(rs, pstmt);
			pstmt = connection.prepareStatement(COUNT_RICORSO_FASE_ISTRUTTORIA);
			pstmt.setInt(1, caricaId);
			rs = pstmt.executeQuery();
			if (rs.next())
				count += rs.getInt(1);
			jdbcMan.closeResultSetAndStatement(rs, pstmt);
			pstmt = connection.prepareStatement(COUNT_RICORSO_FASE_RELATORIA);
			pstmt.setInt(1, caricaId);
			pstmt.setInt(2, caricaId);
			rs = pstmt.executeQuery();
			if (rs.next())
				count += rs.getInt(1);
			jdbcMan.closeResultSetAndStatement(rs, pstmt);
			pstmt = connection.prepareStatement(COUNT_RICORSO_PARERE_CONSIGLIO);
			pstmt.setInt(1, caricaId);
			pstmt.setInt(2, caricaId);
			pstmt.setInt(3, caricaId);
			rs = pstmt.executeQuery();
			if (rs.next())
				count += rs.getInt(1);
			jdbcMan.closeResultSetAndStatement(rs, pstmt);
			pstmt = connection.prepareStatement(COUNT_RICORSO_DECRETO_PRESIDENTE);
			pstmt.setInt(1, caricaId);
			rs = pstmt.executeQuery();
			if (rs.next())
				count += rs.getInt(1);
			jdbcMan.closeResultSetAndStatement(rs, pstmt);
			pstmt = connection.prepareStatement(COUNT_ATTENDI_RICORSO_DECRETO_PRESIDENTE);
			pstmt.setInt(1, caricaId);
			rs = pstmt.executeQuery();
			if (rs.next())
				count += rs.getInt(1);

		} catch (Exception e) {
			logger.error("contaRicorsi", e);
			throw new DataException("contaRicorsi");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return count;
	}

	public int contaProcedimentiAlert(int referenteId) throws DataException {
		Connection connection = null;
		String query = "select count(procedimento_id) from procedimenti where date_part('day',data_scadenza-now())<=giorni_alert AND responsabile_id=? AND giorni_alert>0  AND stato_id=0";
		PreparedStatement pstmt = null;
		int count = 0;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, referenteId);
			rs = pstmt.executeQuery();
			if (rs.next())
				count = rs.getInt(1);
		} catch (Exception e) {
			logger.error("contaProcedimentiAlert", e);
			throw new DataException("contaProcedimentiAlert");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return count;
	}

	public int contaProcedimentiIstruttore(int caricaId)
			throws DataException {
		Connection connection = null;
		String query = "select count(i.procedimento_id) " +
				"FROM procedimento_istruttori i LEFT JOIN procedimenti p ON (p.procedimento_id=i.procedimento_id) WHERE p.stato_id=0 AND i.flag_lavorato=0 AND i.carica_id=?";
		PreparedStatement pstmt = null;
		int count = 0;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, caricaId);
			rs = pstmt.executeQuery();
			if (rs.next())
				count = rs.getInt(1);
		} catch (Exception e) {
			logger.error("contaProcedimentiIstruttore", e);
			throw new DataException("contaProcedimentiIstruttore");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return count;
	}
	
	public int contaProcedimentiUfficioPartecipante(int ufficioId)
			throws DataException {
		Connection connection = null;
		String query = "select count(pup.procedimento_id) " +
				"FROM procedimento_uffici_partecipanti pup LEFT JOIN procedimenti p ON (p.procedimento_id=pup.procedimento_id) WHERE p.stato_id=0 AND pup.ufficio_id=?";
		PreparedStatement pstmt = null;
		int count = 0;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, ufficioId);
			rs = pstmt.executeQuery();
			if (rs.next())
				count = rs.getInt(1);
		} catch (Exception e) {
			logger.error("contaProcedimentiUfficioPartecipante", e);
			throw new DataException("contaProcedimentiUfficioPartecipante");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return count;
	}

	public Map<String,ProcedimentoView> getProcedimentiAlert(int referenteId) throws DataException {
		Connection connection = null;
		String query = "SELECT *,date_part('day',data_scadenza-now()) "
				+ "as giorni_mancanti from procedimenti "
				+ "WHERE date_part('day',data_scadenza-now())<=giorni_alert AND responsabile_id=? AND giorni_alert>0 AND stato_id=0";
		PreparedStatement pstmt = null;
		Map<String,ProcedimentoView> ids = new HashMap<String,ProcedimentoView>(2);
		ProcedimentoView proced = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, referenteId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				proced = new ProcedimentoView();
				proced.setProcedimentoId(rs.getInt("procedimento_id"));
				if (rs.getDate("data_avvio") != null)
					proced.setDataAvvio(rs.getDate("data_avvio"));
				proced.setOggetto(rs.getString("oggetto"));
				proced.setNumeroProcedimento(rs
						.getString("numero_procedimento"));
				proced.setResponsabileId(rs.getInt("responsabile_id"));
				proced.setGiorniRimanenti(rs.getInt("giorni_mancanti"));
				ids.put(String.valueOf(proced.getProcedimentoId()), proced);
			}

		} catch (Exception e) {
			logger.error("getProcedimentiAlert", e);
			throw new DataException("getProcedimentiAlert");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return ids;
	}

	public Map<String,ProcedimentoView> getProcedimentiIstruttore(int caricaId)
			throws DataException {
		Connection connection = null;
		Map<String, ProcedimentoView> procedimenti = new TreeMap<String, ProcedimentoView>();
		String query = "select p.procedimento_id, p.numero, p.data_avvio,p.oggetto,p.responsabile_id,p.numero_procedimento,proto.documento_id FROM procedimenti p LEFT JOIN procedimento_istruttori a ON (p.procedimento_id=a.procedimento_id) LEFT JOIN protocolli proto ON (p.protocollo_id=proto.protocollo_id) WHERE p.stato_id=0 AND a.flag_lavorato=0 AND a.carica_id=?";
		query += " ORDER BY data_avvio, numero DESC";
		PreparedStatement pstmt = null;
		ProcedimentoView proced = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, caricaId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				proced = new ProcedimentoView();
				proced.setProcedimentoId(rs.getInt("procedimento_id"));
				if (rs.getDate("data_avvio") != null)
					proced.setDataAvvio(rs.getDate("data_avvio"));
				proced.setOggetto(rs.getString("oggetto"));
				proced.setNumeroProcedimento(rs
						.getString("numero_procedimento"));
				proced.setResponsabileId(rs.getInt("responsabile_id"));
				proced.setPdf(rs.getInt("DOCUMENTO_ID") > 0 ? true : false);
				proced.setDocumentoId(rs.getInt("DOCUMENTO_ID"));
				procedimenti.put(proced.getNumeroProcedimento(), proced);
			}
		} catch (Exception e) {
			logger.error("getProcedimentiIstruttore", e);
			throw new DataException("getProcedimentiIstruttore");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return procedimenti;
	}
	
	public Map<String,ProcedimentoView> getProcedimentiUfficioPartecipante(int ufficioId)
			throws DataException {
		Connection connection = null;
		Map<String, ProcedimentoView> procedimenti = new TreeMap<String, ProcedimentoView>();
		String query = "select p.procedimento_id, p.numero, p.data_avvio,p.oggetto,p.responsabile_id,p.numero_procedimento,proto.documento_id FROM procedimenti p LEFT JOIN procedimento_uffici_partecipanti pup ON (p.procedimento_id=pup.procedimento_id) LEFT JOIN protocolli proto ON (p.protocollo_id=proto.protocollo_id) WHERE p.stato_id=0 AND pup.ufficio_id=?";
		query += " ORDER BY data_avvio, numero DESC";
		PreparedStatement pstmt = null;
		ProcedimentoView proced = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, ufficioId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				proced = new ProcedimentoView();
				proced.setProcedimentoId(rs.getInt("procedimento_id"));
				if (rs.getDate("data_avvio") != null)
					proced.setDataAvvio(rs.getDate("data_avvio"));
				proced.setOggetto(rs.getString("oggetto"));
				proced.setNumeroProcedimento(rs
						.getString("numero_procedimento"));
				proced.setResponsabileId(rs.getInt("responsabile_id"));
				proced.setPdf(rs.getInt("DOCUMENTO_ID") > 0 ? true : false);
				proced.setDocumentoId(rs.getInt("DOCUMENTO_ID"));
				procedimenti.put(proced.getNumeroProcedimento(), proced);
			}
		} catch (Exception e) {
			logger.error("getProcedimentiUfficioPartecipante", e);
			throw new DataException("getProcedimentiUfficioPartecipante");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return procedimenti;
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

	public Map<String,DocumentoVO> getRiferimenti(int id) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String,DocumentoVO>  docs = new HashMap<String,DocumentoVO> (2);
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
			logger.error("Save Allegato-Protocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void deleteIstruttori(Connection connection, int id)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(DELETE_ISTRUTTORI);
			pstmt.setInt(1, id);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("deleteRiferimenti", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public Collection<AssegnatarioVO> getIstruttori(int id) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<AssegnatarioVO> assegnatari = new ArrayList<AssegnatarioVO>();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_ISTRUTTORI);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				AssegnatarioVO assegnatario = new AssegnatarioVO();
				assegnatario.setCaricaAssegnatarioId(rs.getInt("carica_id"));
				assegnatario.setLavorato(rs.getBoolean("flag_lavorato"));
				assegnatario.setRowCreatedUser(rs.getString("row_created_user"));
				assegnatari.add(assegnatario);
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new DataException("Errore nella lettura degli istruttori.");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return assegnatari;
	}

	public Collection<AssegnatarioVO> getStoriaIstruttori(int id, int versione)
			throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<AssegnatarioVO> assegnatari = new ArrayList<AssegnatarioVO>();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_STORIA_ISTRUTTORI);
			pstmt.setInt(1, id);
			pstmt.setInt(2, versione);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				AssegnatarioVO assegnatario = new AssegnatarioVO();
				assegnatario.setCaricaAssegnatarioId(rs.getInt("carica_id"));
				assegnatario.setLavorato(rs.getBoolean("flag_lavorato"));
				assegnatario.setRowCreatedUser(rs.getString("row_created_user"));
				assegnatari.add(assegnatario);
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new DataException("Errore nella lettura degli isruttori.");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return assegnatari;
	}
	
	public void salvaIstruttori(Connection connection, int procedimentoId,
			int caricaId, boolean lavorato, int versione,String username) throws DataException {
		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("salvaRiferimenti() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(INSERT_ISTRUTTORI);
			pstmt.setInt(1, procedimentoId);
			pstmt.setInt(2, caricaId);
			pstmt.setInt(3, versione);
			pstmt.setInt(4, lavorato ? 1 : 0);
			pstmt.setString(5, username);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("Save Allegato-Protocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public ProcedimentoVO newProcedimento(Connection connection,
			ProcedimentoVO vo) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			vo.setRowCreatedTime(new Date(System.currentTimeMillis()));

			pstmt = connection.prepareStatement(INSERT_PROCEDIMENTO);
			pstmt.setInt(1, vo.getId().intValue());
			if (vo.getDataAvvio() != null) {
				pstmt.setDate(2, new Date(vo.getDataAvvio().getTime()));
			} else {
				pstmt.setNull(2, Types.DATE);
			}
			pstmt.setInt(3, vo.getUfficioId());
			pstmt.setInt(4, vo.getStatoId());
			pstmt.setInt(5, vo.getTipoFinalitaId());
			pstmt.setString(6, vo.getOggetto());
			pstmt.setInt(7, vo.getTipoProcedimentoId());
			pstmt.setInt(8, vo.getReferenteId());
			pstmt.setInt(9, vo.getResponsabileId());
			pstmt.setString(10, vo.getPosizione());
			if (vo.getDataEvidenza() != null) {
				pstmt.setDate(11, new Date(vo.getDataEvidenza().getTime()));
			} else {
				pstmt.setNull(11, Types.DATE);
			}
			pstmt.setString(12, vo.getNote());
			if (vo.getProtocolloId() == 0) {
				pstmt.setNull(13, Types.INTEGER);
			} else {
				pstmt.setInt(13, vo.getProtocolloId());
			}
			pstmt.setString(14, vo.getNumeroProcedimento());
			pstmt.setInt(15, vo.getAnno());
			pstmt.setInt(16, vo.getNumero());
			if (vo.getRowCreatedTime() != null) {
				pstmt.setDate(17, new Date(vo.getRowCreatedTime().getTime()));
			} else {
				pstmt.setNull(17, Types.DATE);
			}
			pstmt.setString(18, vo.getRowCreatedUser());
			pstmt.setString(19, vo.getRowUpdatedUser());
			pstmt.setNull(20, Types.DATE);
			pstmt.setInt(21, 0);
			pstmt.setInt(22, vo.getAooId());
			pstmt.setInt(23, vo.getGiorniMax());
			pstmt.setInt(24, vo.getGiorniAlert());
			pstmt.setInt(25, vo.getFascicoloId());
			
			pstmt.setString(26, vo.getDelegato());
			pstmt.setString(27, vo.getInteressato());
			pstmt.setString(28, vo.getIndiDelegato());
			pstmt.setString(29, vo.getIndiInteressato());
			pstmt.setString(30, vo.getIndicazioni());
			pstmt.setString(31, vo.getAutoritaEmanante());
			
			pstmt.setString(32, vo.getEstremiProvvedimento());
			if (vo.getDataScadenza() != null) {
				pstmt.setDate(33, new Date(vo.getDataScadenza().getTime()));
			} else {
				pstmt.setNull(33, Types.DATE);
			}
			pstmt.executeUpdate();
			vo.setReturnValue(ReturnValues.SAVED);
		} catch (Exception e) {
			logger.error("newProcedimento", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return vo;
	}

	public Collection<ProcedimentoView> getStoriaProcedimenti(int procedimentoId)
			throws DataException {
		ArrayList<ProcedimentoView> storiaProcedimento = new ArrayList<ProcedimentoView>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			String sqlStoriaProcedimento = "  SELECT procedimento_id, STORIA_PROCEDIMENTI.aoo_id, "
					+ "STORIA_PROCEDIMENTI.oggetto, STORIA_PROCEDIMENTI.ufficio_id,  "
					+ "STORIA_PROCEDIMENTI.row_created_time, STORIA_PROCEDIMENTI.row_created_user, "
					+ "STORIA_PROCEDIMENTI.row_updated_user,"
					+ "STORIA_PROCEDIMENTI.row_updated_time, "
					+ "STORIA_PROCEDIMENTI.note, STORIA_PROCEDIMENTI.numero_procedimento, "
					+ "STORIA_PROCEDIMENTI.anno, STORIA_PROCEDIMENTI.numero, STORIA_PROCEDIMENTI.data_avvio,"
					+ "STORIA_PROCEDIMENTI.data_evidenza, "
					+ "STORIA_PROCEDIMENTI.stato_id, STORIA_PROCEDIMENTI.versione, STORIA_PROCEDIMENTI.posizione_id AS posizione,"
					+ "uffici.descrizione AS ufficio "
					+ "FROM STORIA_PROCEDIMENTI , uffici WHERE    procedimento_id=? "
					+ "AND STORIA_PROCEDIMENTI.ufficio_id=Uffici.ufficio_id "
					+ "ORDER BY VERSIONE desc";

			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(sqlStoriaProcedimento);
			pstmt.setInt(1, procedimentoId);
			rs = pstmt.executeQuery();
			ProcedimentoView procedimento;
			while (rs.next()) {
				procedimento = new ProcedimentoView();
				procedimento.setProcedimentoId(rs.getInt("procedimento_id"));
				procedimento.setAooId(rs.getInt("aoo_id"));
				procedimento.setOggetto(rs.getString("oggetto"));
				procedimento.setDescUfficioId(rs.getString("ufficio"));
				procedimento.setNote(rs.getString("note"));
				procedimento.setNumeroProcedimento(rs
						.getString("numero_procedimento"));
				procedimento.setAnno(rs.getInt("anno"));
				procedimento.setNumero(rs.getInt("numero"));

				if (rs.getDate("data_avvio") != null)
					procedimento.setDataAvvio(rs.getDate("data_avvio"));
				if (rs.getDate("data_evidenza") != null)
					procedimento.setDataEvidenza(DateUtil.formattaData(rs
							.getDate("data_evidenza").getTime()));
				procedimento.setVersione(rs.getInt("versione"));

				storiaProcedimento.add(procedimento);
			}
		} catch (Exception e) {
			logger.error("getStoriaProcedimento", e);
			throw new DataException("Cannot load getStoriaProcedimento");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return storiaProcedimento;

	}

	public ProcedimentoVO getProcedimentoByIdVersione(int id, int versione)
			throws DataException {
		ProcedimentoVO procedimentoVO = new ProcedimentoVO();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			procedimentoVO = getProcedimentoByIdVersione(connection, id,
					versione);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " ProcedimentoId :" + id
					+ " Versione :" + versione);
		} finally {
			jdbcMan.close(connection);
		}
		return procedimentoVO;
	}

	public ProcedimentoVO getProcedimentoByIdVersione(Connection connection,
			int id, int versione) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ProcedimentoVO procedimento = new ProcedimentoVO();
		try {
			if (connection == null) {
				logger.warn("getProcedimentoByIdVersione() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			pstmt = connection
					.prepareStatement(SELECT_PROCEDIMENTO_BY_ID_VERSIONE);
			pstmt.setInt(1, id);
			pstmt.setInt(2, versione);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				procedimento = getProcedimento(rs);
				procedimento.setReturnValue(ReturnValues.FOUND);
			} else {
				procedimento.setReturnValue(ReturnValues.NOT_FOUND);
			}

		} catch (Exception e) {
			logger.error("Load Versione faldone by ID", e);
			throw new DataException("Cannot load Versione procedimento by ID");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return procedimento;
	}

	public ProcedimentoVO getProcedimentoVODaFascicolo(Connection connection,
			int fascicoloId) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ProcedimentoVO procedimento = new ProcedimentoVO();
		try {
			if (connection == null) {
				logger.warn("getProcedimentoVODaFascicolo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			pstmt = connection
					.prepareStatement(SELECT_PROCEDIMENTO_BY_ID_FASCICOLO);
			pstmt.setInt(1, fascicoloId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				procedimento = getProcedimento(rs);
				procedimento.setReturnValue(ReturnValues.FOUND);
			} else {
				procedimento.setReturnValue(ReturnValues.NOT_FOUND);
			}

		} catch (Exception e) {
			logger.error("Load Versione faldone by ID", e);
			throw new DataException("Cannot load Versione procedimento by ID");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return procedimento;
	}

	public int getProcedimentoIdDaFascicolo(Connection connection,
			int fascicoloId) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int procedimentoId = 0;
		try {
			if (connection == null) {
				logger.warn("getProcedimentoIdDaFascicolo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			pstmt = connection
					.prepareStatement(SELECT_PROCEDIMENTO_ID_BY_ID_FASCICOLO);
			pstmt.setInt(1, fascicoloId);
			rs = pstmt.executeQuery();
			if (rs.next()) 
				procedimentoId =rs.getInt("procedimento_id");
			

		} catch (Exception e) {
			logger.error("getProcedimentoIdDaFascicolo", e);
			throw new DataException("getProcedimentoIdDaFascicolo");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return procedimentoId;
	}
	
	public int getFascicoloIdDaProcedimento(Connection connection,
			int pId) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int fId = 0;
		try {
			if (connection == null) {
				logger.warn("getProcedimentoIdDaFascicolo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			pstmt = connection
					.prepareStatement(SELECT_FASCICOLO_ID_BY_ID_PROCEDIMENTO);
			pstmt.setInt(1, pId);
			rs = pstmt.executeQuery();
			if (rs.next()) 
				fId =rs.getInt("fascicolo_id");
			

		} catch (Exception e) {
			logger.error("getFascicoloIdDaProcedimento", e);
			throw new DataException("getFascicoloIdDaProcedimento");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return fId;
	}
	
	private ProcedimentoVO getProcedimento(ResultSet rs) throws DataException {
		ProcedimentoVO vo = new ProcedimentoVO();
		try {
			vo.setId(rs.getInt("procedimento_id"));
			vo.setDataAvvio(rs.getDate("data_avvio"));
			vo.setUfficioId(rs.getInt("ufficio_id"));
			vo.setGiorniAlert(rs.getInt("giorni_alert"));
			vo.setGiorniMax(rs.getInt("giorni_max"));
			vo.setResponsabileId(rs.getInt("responsabile_id"));
			vo.setStatoId(rs.getInt("stato_id"));
			vo.setTipoFinalitaId(rs.getInt("tipo_finalita_id"));
			vo.setOggetto(rs.getString("oggetto"));
			vo.setTipoProcedimentoId(rs.getInt("tipo_procedimento_id"));
			vo.setTipoProcedimentoDesc(rs.getString("tipo"));
			vo.setReferenteId(rs.getInt("referente_id"));
			vo.setDataEvidenza(rs.getDate("data_evidenza"));
			vo.setPosizione(rs.getString("posizione_id"));
			vo.setDataEvidenza(rs.getDate("data_evidenza"));
			vo.setNote(rs.getString("note"));
			vo.setProtocolloId(rs.getInt("protocollo_id"));
			vo.setNumeroProcedimento(rs.getString("numero_procedimento"));

			vo.setAnno(rs.getInt("anno"));
			vo.setNumero(rs.getInt("numero"));
			vo.setRowCreatedTime(rs.getDate("row_created_time"));
			vo.setRowCreatedUser(rs.getString("row_created_user"));
			vo.setRowUpdatedUser(rs.getString("row_updated_user"));
			vo.setRowUpdatedTime(rs.getDate("row_updated_time"));
			vo.setVersione(rs.getInt("versione"));
			vo.setFascicoloId(rs.getInt("fascicolo_id"));
			vo.setDelegato(rs.getString("delegato"));
			vo.setIndiInteressato(rs.getString("indi_interessato"));
			vo.setIndiDelegato(rs.getString("indi_delegato"));
			vo.setInteressato(rs.getString("interessato"));
			vo.setAutoritaEmanante(rs.getString("autorita"));
			vo.setIndicazioni(rs.getString("indicazioni"));
			vo.setEstremiProvvedimento(rs.getString("estremi_provvedimento"));
			vo.setDataScadenza(rs.getDate("data_scadenza"));
			vo.setDataSospensione(rs.getDate("data_sospensione"));
			vo.setSospeso(rs.getBoolean("flag_sospeso"));
			vo.setDataScadenza(rs.getDate("data_scadenza"));
			vo.setEstremiSospensione(rs.getString("estremi_sospensione"));

		} catch (SQLException e) {
			logger.error("Load getFaldone()", e);
			throw new DataException("Cannot load getProcedimento()");
		}
		return vo;
	}

	public ProcedimentoVO newStoriaProcedimento(Connection connection,
			ProcedimentoVO vo) throws DataException {

		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			vo.setRowCreatedTime(new Date(System.currentTimeMillis()));

			pstmt = connection.prepareStatement(INSERT_STORIA_PROCEDIMENTO);
			pstmt.setInt(1, vo.getId().intValue());
			pstmt.setInt(1, vo.getId().intValue());
			if (vo.getDataAvvio() != null) {
				pstmt.setDate(2, new Date(vo.getDataAvvio().getTime()));
			} else {
				pstmt.setNull(2, Types.DATE);
			}
			pstmt.setInt(3, vo.getUfficioId());
			pstmt.setInt(4, vo.getStatoId());
			pstmt.setInt(5, vo.getTipoFinalitaId());
			pstmt.setString(6, vo.getOggetto());
			pstmt.setInt(7, vo.getTipoProcedimentoId());
			pstmt.setInt(8, vo.getReferenteId());
			pstmt.setInt(9, vo.getResponsabileId());
			pstmt.setString(10, vo.getPosizione());
			if (vo.getDataEvidenza() != null) {
				pstmt.setDate(11, new Date(vo.getDataEvidenza().getTime()));
			} else {
				pstmt.setNull(11, Types.DATE);
			}
			pstmt.setString(12, vo.getNote());
			if (vo.getProtocolloId() == 0) {
				pstmt.setNull(13, Types.INTEGER);
			} else {
				pstmt.setInt(13, vo.getProtocolloId());
			}
			pstmt.setString(14, vo.getNumeroProcedimento());
			pstmt.setInt(15, vo.getAnno());
			pstmt.setInt(16, vo.getNumero());
			if (vo.getRowCreatedTime() != null) {
				pstmt.setDate(17, new Date(vo.getRowCreatedTime().getTime()));
			} else {
				pstmt.setNull(17, Types.DATE);
			}
			pstmt.setString(18, vo.getRowCreatedUser());
			pstmt.setString(19, vo.getRowUpdatedUser());
			pstmt.setNull(20, Types.DATE);
			pstmt.setInt(21, 0);
			pstmt.setInt(22, vo.getAooId());
			pstmt.setInt(23, vo.getGiorniMax());
			pstmt.setInt(24, vo.getGiorniAlert());
			pstmt.executeUpdate();
			vo = getProcedimentoById(connection, vo.getId().intValue());
			vo.setReturnValue(ReturnValues.SAVED);
		} catch (Exception e) {
			logger.error("newProcedimento", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return vo;
	}

	public Collection<MigrazioneVO> getProcedimentiAnnoNumero(Connection connection)
			throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Collection<MigrazioneVO> procedimenti = new ArrayList<MigrazioneVO>();

		try {
			if (connection == null) {
				logger.warn("getProcedimentiAnnoNumero() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			String sql = "SELECT ANNO,Max(NUMERO)AS Numero  FROM procedimenti GROUP BY ANNO";
			pstmt = connection.prepareStatement(sql);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				MigrazioneVO migrazione = new MigrazioneVO();
				migrazione.setAnno(rs.getInt(1));
				migrazione.setNumero(rs.getInt(2));
				procedimenti.add(migrazione);
			}

		} catch (Exception e) {
			logger.error("Load getProcedimentiAnnoNumero ", e);
			throw new DataException("Cannot load Procedimenti by ID");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return procedimenti;
	}

	public void salvaProcedimentoProtocollo(Connection connection,
			ProtocolloProcedimentoVO ppVO) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("salvaProcedimentoProtocollo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(INSERT_PROTOCOLLI_PROCEDIMENTI);
			pstmt.setInt(1, ppVO.getProcedimentoId());
			pstmt.setInt(2, ppVO.getProtocolloId());
			pstmt.setDate(3, new Date(ppVO.getRowCreatedTime().getTime()));
			pstmt.setString(4, ppVO.getRowCreatedUser());
			pstmt.setString(5, ppVO.getRowUpdatedUser());
			pstmt.setDate(6, new Date(ppVO.getRowUpdatedTime().getTime()));
			pstmt.setInt(7, ppVO.getVersione());
			pstmt.setInt(8, ppVO.getSospeso());
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("salvaProcedimentoProtocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void cancellaProcedimentoProtocollo(Connection connection,
			ProtocolloProcedimentoVO ppVO) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("cancellaProcedimentiProtocollo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(DELETE_PROTOCOLLI_PROCEDIMENTI);
			pstmt.setInt(1, ppVO.getProtocolloId());
			pstmt.setInt(2, ppVO.getProcedimentoId());
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("cancellaProcedimentiProtocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public int getVersione(Connection connection, int procedimentoId)
			throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int version = 1;
		try {
			if (connection == null) {
				logger.warn("getVersione() - Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			String sql = "SELECT versione FROM procedimenti WHERE procedimento_id = ?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, procedimentoId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				version = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("getVersione", e);
			throw new DataException("Cannot load getVersione");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return version;
	}

	public void archiviaVersione(Connection connection, int procedimentoId, String username)
			throws DataException {
		String[] tables = { "procedimenti", "protocollo_procedimenti",
				"procedimenti_fascicolo", "procedimento_istruttori"};
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("storia Procedimenti- Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			for (int i = 0; i < tables.length; i++) {
				String sql = "INSERT INTO storia_" + tables[i]
						+ " SELECT * FROM " + tables[i]
						+ " WHERE procedimento_id = ?";

				pstmt = connection.prepareStatement(sql);
				pstmt.setInt(1, procedimentoId);
				int r = pstmt.executeUpdate();
				logger.info(sql + "record inseriti in storia_" + tables[i]
						+ ": " + r);
				sql = "UPDATE " + tables[i] + " SET versione = versione+1, row_created_user='"+username+"'  WHERE procedimento_id = ?";
				jdbcMan.close(pstmt);
				pstmt = connection.prepareStatement(sql);
				pstmt.setInt(1, procedimentoId);
				pstmt.executeUpdate();
			}

		} catch (Exception e) {
			logger.error("storia procedimento" + procedimentoId, e);
			throw new DataException("Cannot insert Storia Procedimento");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public ProcedimentoVO aggiornaProcedimento(Connection connection,
			ProcedimentoVO vo) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			archiviaVersione(connection, vo.getId(),vo.getRowUpdatedUser());
			vo.setRowCreatedTime(new Date(System.currentTimeMillis()));
			pstmt = connection.prepareStatement(UPDATE_PROCEDIMENTO);
			if (vo.getDataAvvio() != null) {
				pstmt.setDate(1, new Date(vo.getDataAvvio().getTime()));
			} else {
				pstmt.setNull(1, Types.DATE);
			}
			pstmt.setInt(2, vo.getUfficioId());
			pstmt.setInt(3, vo.getStatoId());
			pstmt.setInt(4, vo.getTipoFinalitaId());
			pstmt.setString(5, vo.getOggetto());
			pstmt.setInt(6, vo.getTipoProcedimentoId());
			pstmt.setInt(7, vo.getReferenteId());
			pstmt.setInt(8, vo.getResponsabileId());

			if (vo.getDataEvidenza() != null) {
				pstmt.setDate(9, new Date(vo.getDataEvidenza().getTime()));
			} else {
				pstmt.setNull(9, Types.DATE);
			}
			pstmt.setString(10, vo.getNote());
			pstmt.setInt(11, vo.getProtocolloId());
			pstmt.setString(12, vo.getRowUpdatedUser());
			if (vo.getRowUpdatedTime() != null) {
				pstmt.setDate(13, new Date(vo.getRowUpdatedTime().getTime()));
			} else {
				pstmt.setNull(13, Types.DATE);
			}
			pstmt.setInt(14, vo.getGiorniMax());
			pstmt.setInt(15, vo.getGiorniAlert());
			pstmt.setString(16, vo.getDelegato());
			pstmt.setString(17, vo.getInteressato());
			pstmt.setString(18, vo.getIndiDelegato());
			pstmt.setString(19, vo.getIndiInteressato());
			pstmt.setString(20, vo.getIndicazioni());
			pstmt.setString(21, vo.getAutoritaEmanante());
			
			pstmt.setString(22, vo.getEstremiProvvedimento());
			pstmt.setString(23,vo.getPosizione());
			pstmt.setInt(24, vo.getId().intValue());
			pstmt.executeUpdate();
			vo = getProcedimentoById(connection, vo.getId().intValue());
			vo.setReturnValue(ReturnValues.SAVED);
		} catch (Exception e) {
			logger.error("updateProcedimento", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return vo;
	}

	public int getMaxNumProcedimento(Connection connection, int aooId, int anno)
			throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int max = 1;
		try {
			if (connection == null) {
				logger.warn("getMaxNumProcedimento() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_ULTIMO_PROCEDIMENTO);
			pstmt.setInt(1, aooId);
			pstmt.setInt(2, anno);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				max = rs.getInt(1) + 1;
			}
		} catch (Exception e) {
			logger.error("getMaxNumProcedimento", e);
			throw new DataException("Cannot load getMaxNumProcedimento");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return max;
	}

	public void inserisciFaldoni(Connection connection, Integer[] ids,
			int procedimentoId) throws DataException {

		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			for (int i = 0; i < ids.length; i++) {
				pstmt = connection
						.prepareStatement(INSERT_PROCEDIMENTO_FALDONI);
				pstmt.setInt(1, procedimentoId);
				pstmt.setInt(2, ids[i].intValue());
				pstmt.executeUpdate();
				jdbcMan.close(pstmt);
			}
		} catch (Exception e) {
			logger.error("inserisciFaldoni", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void inserisciProcedimentoFaldone(Connection connection,
			ProcedimentoFaldoneVO procedimentoFaldoneVO) throws DataException {

		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			pstmt = connection.prepareStatement(INSERT_PROCEDIMENTO_FALDONI);
			pstmt.setInt(1, procedimentoFaldoneVO.getProcedimentoId());
			pstmt.setInt(2, procedimentoFaldoneVO.getFaldoneId());
			pstmt.executeUpdate();
			jdbcMan.close(pstmt);

		} catch (Exception e) {
			logger.error("inserisciFaldoni", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void inserisciFascicoli(Connection connection,
			Collection<ProcedimentoFascicoloVO> fascicoliProcedimenti)
			throws DataException {

		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			for (ProcedimentoFascicoloVO procedimentofascicolo : fascicoliProcedimenti) {
				pstmt = connection
						.prepareStatement(INSERT_PROCEDIMENTO_FASCICOLI);
				pstmt.setInt(1, procedimentofascicolo.getProcedimentoId());
				pstmt.setInt(2, procedimentofascicolo.getFascicoloId());
				if (procedimentofascicolo.getRowCreatedTime() != null)
					pstmt.setDate(3, new Date(procedimentofascicolo
							.getRowCreatedTime().getTime()));
				else
					pstmt.setNull(3, Types.DATE);
				pstmt.setString(4, procedimentofascicolo.getRowCreatedUser());
				pstmt.setString(5, procedimentofascicolo.getRowUpdatedUser());
				if (procedimentofascicolo.getRowUpdatedTime() != null)
					pstmt.setDate(6, new Date(procedimentofascicolo
							.getRowUpdatedTime().getTime()));
				else
					pstmt.setNull(6, Types.DATE);
				pstmt.setInt(7, procedimentofascicolo.getVersione());
				pstmt.executeUpdate();
			}
		} catch (Exception e) {
			logger.error("inserisciFascicoli", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void inserisciFascicoloProcedimento(Connection connection,
			ProcedimentoFascicoloVO procedimentofascicolo) throws DataException {

		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(INSERT_PROCEDIMENTO_FASCICOLI);
			pstmt.setInt(1, procedimentofascicolo.getProcedimentoId());
			pstmt.setInt(2, procedimentofascicolo.getFascicoloId());
			if (procedimentofascicolo.getRowCreatedTime() != null)
				pstmt.setDate(3, new Date(procedimentofascicolo
						.getRowCreatedTime().getTime()));
			else
				pstmt.setNull(3, Types.DATE);
			pstmt.setString(4, procedimentofascicolo.getRowCreatedUser());
			pstmt.setString(5, procedimentofascicolo.getRowUpdatedUser());
			if (procedimentofascicolo.getRowUpdatedTime() != null)
				pstmt.setDate(6, new Date(procedimentofascicolo
						.getRowUpdatedTime().getTime()));
			else
				pstmt.setNull(6, Types.DATE);
			pstmt.setInt(7, procedimentofascicolo.getVersione());
			pstmt.executeUpdate();
			jdbcMan.close(pstmt);

		} catch (Exception e) {
			logger.error("inserisciFascicoli", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void inserisciProtocolli(Connection connection, ArrayList<ProtocolloProcedimentoVO> ids)
			throws DataException {

		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			for (Iterator<ProtocolloProcedimentoVO> it = ids.iterator(); it.hasNext();) {
				ProtocolloProcedimentoVO ppVO =  it
						.next();
				pstmt = connection
						.prepareStatement(INSERT_PROCEDIMENTO_PROTOCOLLI);
				pstmt.setInt(1, ppVO.getProcedimentoId());
				pstmt.setInt(2, ppVO.getProtocolloId());
				pstmt.setDate(3, new Date(ppVO.getRowCreatedTime().getTime()));
				pstmt.setString(4, ppVO.getRowCreatedUser());
				pstmt.setString(5, ppVO.getRowUpdatedUser());
				pstmt.setDate(6, new Date(ppVO.getRowUpdatedTime().getTime()));
				pstmt.setInt(7, ppVO.getVersione());
				pstmt.executeUpdate();
				jdbcMan.close(pstmt);
			}
		} catch (Exception e) {
			logger.error("inserisciProtocolli(Connection, Integer[], int)", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void cancellaFascicoli(Connection connection, int procedimentoId)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(DELETE_PROCEDIMENTO_FASCICOLI);
			pstmt.setInt(1, procedimentoId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("cancellaFascicoli", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void cancellaProtocolli(Connection connection, int procedimentoId)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(DELETE_PROCEDIMENTO_PROTOCOLLI);
			pstmt.setInt(1, procedimentoId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("cancellaProtocolli", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void cancellaFaldoni(Connection connection, int procedimentoId)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(DELETE_PROCEDIMENTO_FALDONI);
			pstmt.setInt(1, procedimentoId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("cancellaFaldoni", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public ProcedimentoVO getProcedimentoById(int procedimentoId)
			throws DataException {
		ProcedimentoVO vo = new ProcedimentoVO();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			vo = getProcedimentoById(connection, procedimentoId);
		} catch (Exception e) {
			logger.error("getProvedimentoById", e);
			throw new DataException("getProvedimentoById fallito!");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return vo;
	}

	public ProcedimentoVO getProcedimentoById(Connection connection,
			int procedimentoId) throws DataException {

		PreparedStatement pstmt = null;
		ProcedimentoVO vo = new ProcedimentoVO();
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("getProvedimentoById - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			pstmt = connection.prepareStatement(SELECT_PROCEDIMENTO);
			pstmt.setInt(1, procedimentoId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				vo.setId(rs.getInt("procedimento_id"));
				vo.setDataAvvio(rs.getDate("data_avvio"));
				vo.setUfficioId(rs.getInt("ufficio_id"));
				vo.setGiorniAlert(rs.getInt("giorni_alert"));
				vo.setGiorniMax(rs.getInt("giorni_max"));
				vo.setResponsabileId(rs.getInt("responsabile_id"));
				vo.setStatoId(rs.getInt("stato_id"));
				vo.setTipoFinalitaId(rs.getInt("tipo_finalita_id"));
				vo.setOggetto(rs.getString("oggetto"));
				vo.setTipoProcedimentoId(rs.getInt("tipo_procedimento_id"));
				vo.setTipoProcedimentoDesc(rs.getString("tipo"));
				vo.setReferenteId(rs.getInt("referente_id"));
				vo.setDataEvidenza(rs.getDate("data_evidenza"));
				vo.setPosizione(rs.getString("posizione_id"));
				vo.setDataEvidenza(rs.getDate("data_evidenza"));
				vo.setNote(rs.getString("note"));
				vo.setProtocolloId(rs.getInt("protocollo_id"));
				vo.setNumeroProcedimento(rs.getString("numero_procedimento"));
				vo.setAnno(rs.getInt("anno"));
				vo.setNumero(rs.getInt("numero"));
				vo.setRowCreatedTime(rs.getDate("row_created_time"));
				vo.setRowCreatedUser(rs.getString("row_created_user"));
				vo.setRowUpdatedUser(rs.getString("row_updated_user"));
				vo.setRowUpdatedTime(rs.getDate("row_updated_time"));
				vo.setVersione(rs.getInt("versione"));
				vo.setFascicoloId(rs.getInt("fascicolo_id"));
				vo.setDelegato(rs.getString("delegato"));
				vo.setIndiInteressato(rs.getString("indi_interessato"));
				vo.setIndiDelegato(rs.getString("indi_delegato"));
				vo.setInteressato(rs.getString("interessato"));
				vo.setAutoritaEmanante(rs.getString("autorita"));
				vo.setIndicazioni(rs.getString("indicazioni"));
				vo.setEstremiProvvedimento(rs
						.getString("estremi_provvedimento"));
				vo.setDataScadenza(rs.getDate("data_scadenza"));
				vo.setDataSospensione(rs.getDate("data_sospensione"));
				vo.setSospeso(rs.getBoolean("flag_sospeso"));
				vo.setDataScadenza(rs.getDate("data_scadenza"));
				vo.setEstremiSospensione(rs.getString("estremi_sospensione"));
				vo.setReturnValue(ReturnValues.FOUND);
			} else {
				vo.setReturnValue(ReturnValues.NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("getProvedimentoById", e);
			throw new DataException("getProvedimentoById");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return vo;
	}

	public ProcedimentoVO getProcedimentoByAnnoNumero(Connection connection,
			int anno, int numero) throws DataException {

		PreparedStatement pstmt = null;
		ProcedimentoVO vo = new ProcedimentoVO();
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("getProvedimentoById - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			pstmt = connection
					.prepareStatement(SELECT_PROCEDIMENTO_BY_ANNO_NUMERO);
			pstmt.setInt(1, anno);
			pstmt.setInt(2, numero);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				vo.setId(rs.getInt("procedimento_id"));
				vo.setDataAvvio(rs.getDate("data_avvio"));
				vo.setUfficioId(rs.getInt("ufficio_id"));
				vo.setGiorniAlert(rs.getInt("giorni_alert"));
				vo.setGiorniMax(rs.getInt("giorni_max"));
				vo.setResponsabileId(rs.getInt("responsabile_id"));
				vo.setStatoId(rs.getInt("stato_id"));
				vo.setTipoFinalitaId(rs.getInt("tipo_finalita_id"));
				vo.setOggetto(rs.getString("oggetto"));
				vo.setTipoProcedimentoId(rs.getInt("tipo_procedimento_id"));
				vo.setReferenteId(rs.getInt("referente_id"));
				vo.setPosizione(rs.getString("posizione_id"));
				vo.setDataEvidenza(rs.getDate("data_evidenza"));
				vo.setNote("note");
				vo.setProtocolloId(rs.getInt("protocollo_id"));
				vo.setNumeroProcedimento(rs.getString("numero_procedimento"));
				vo.setAnno(rs.getInt("anno"));
				vo.setNumero(rs.getInt("numero"));
				vo.setRowCreatedTime(rs.getDate("row_created_time"));
				vo.setRowCreatedUser(rs.getString("row_created_user"));
				vo.setRowUpdatedUser(rs.getString("row_updated_user"));
				vo.setRowUpdatedTime(rs.getDate("row_updated_time"));
				vo.setVersione(rs.getInt("versione"));
				vo.setFascicoloId(rs.getInt("fascicolo_id"));
				vo.setReturnValue(ReturnValues.FOUND);
			} else {
				vo.setReturnValue(ReturnValues.NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("getProvedimentoById", e);
			throw new DataException("getProvedimentoById");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return vo;
	}

	public Collection<Integer> getProcedimentoFaldoni(Connection connection,
			int procedimentoId) throws DataException {

		PreparedStatement pstmt = null;
		ArrayList<Integer> ids = new ArrayList<Integer>();
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("getProvedimentoFaldoni - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			pstmt = connection.prepareStatement(SELECT_PROCEDIMENTO_FALDONI);
			pstmt.setInt(1, procedimentoId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Integer id = new Integer(rs.getInt("faldone_id"));
				ids.add(id);
			}
		} catch (Exception e) {
			logger.error("getProvedimentoFaldoni", e);
			throw new DataException("getProvedimentoFaldoni");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return ids;
	}

	public Collection<Integer> getProcedimentoFascicoli(Connection connection,
			int procedimentoId) throws DataException {

		PreparedStatement pstmt = null;
		ArrayList<Integer> ids = new ArrayList<Integer>();
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("getProvedimentoFascicoli - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			pstmt = connection.prepareStatement(SELECT_PROCEDIMENTO_FASCICOLI);
			pstmt.setInt(1, procedimentoId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Integer id = new Integer(rs.getInt("fascicolo_id"));
				ids.add(id);
			}
		} catch (Exception e) {
			logger.error("getProvedimentoFascicoli", e);
			throw new DataException("getProvedimentoFascicoli");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return ids;
	}

	public Collection<Integer> getStoriaProcedimentoFascicoli(Connection connection,
			int procedimentoId, int versione) throws DataException {

		PreparedStatement pstmt = null;
		ArrayList<Integer> ids = new ArrayList<Integer>();
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("getStoriaProcedimentoFascicoli - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			pstmt = connection
					.prepareStatement(SELECT_STORIA_PROCEDIMENTO_FASCICOLI);
			pstmt.setInt(1, procedimentoId);
			pstmt.setInt(2, versione);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Integer id = new Integer(rs.getInt("fascicolo_id"));
				ids.add(id);
			}
		} catch (Exception e) {
			logger.error("getProvedimentoFascicoli", e);
			throw new DataException("getProvedimentoFascicoli");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return ids;
	}

	public Map<String,ProtocolloProcedimentoView> getProcedimentoProtocolli(Connection connection,
			int procedimentoId) throws DataException {

		Map<String,ProtocolloProcedimentoView> protocolli = new HashMap<String,ProtocolloProcedimentoView>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("getProcedimentoProtocolli - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			pstmt = connection.prepareStatement(SELECT_PROCEDIMENTO_PROTOCOLLI);
			pstmt.setInt(1, procedimentoId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ProtocolloProcedimentoView p = new ProtocolloProcedimentoView();
				p.setProtocolloId(rs.getInt("protocollo_id"));
				p.setSospeso(rs.getInt("flag_sospeso"));
				p.setNumeroProtocollo(rs.getInt("NUME_PROTOCOLLO"));
				p.setAnnoProtocollo(rs.getInt("ANNO_REGISTRAZIONE"));
				p.setPdf(rs.getInt("DOCUMENTO_ID") > 0 ? true : false);
				p.setDocumentoId(rs.getInt("DOCUMENTO_ID"));
				p.setOggetto(rs.getString("text_oggetto"));
				p.setProcedimentoId(rs.getInt("procedimento_id"));
				protocolli.put(String.valueOf(p.getProtocolloId()), p);
			}
		} catch (Exception e) {
			logger.error("getProtocolliEvidenze", e);
			throw new DataException("Cannot load getProtocolliEvidenze");
		} finally {
			//jdbcMan.closeAll(rs, pstmt, connection);
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return protocolli;
	}

	public Map<String,ProtocolloProcedimentoView> getStoriaProcedimentoProtocolli(Connection connection,
			int procedimentoId, int versione) throws DataException {

		Map<String,ProtocolloProcedimentoView> protocolli = new HashMap<String,ProtocolloProcedimentoView>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("getProcedimentoProtocolli - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			pstmt = connection
					.prepareStatement(SELECT_STORIA_PROCEDIMENTO_PROTOCOLLI);
			pstmt.setInt(1, procedimentoId);
			pstmt.setInt(2, versione);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ProtocolloProcedimentoView p = new ProtocolloProcedimentoView();
				p.setProtocolloId(rs.getInt("protocollo_id"));
				p.setSospeso(rs.getInt("flag_sospeso"));
				p.setNumeroProtocollo(rs.getInt("NUME_PROTOCOLLO"));
				p.setAnnoProtocollo(rs.getInt("ANNO_REGISTRAZIONE"));
				p.setPdf(rs.getInt("DOCUMENTO_ID") > 0 ? true : false);
				p.setDocumentoId(rs.getInt("DOCUMENTO_ID"));
				p.setOggetto(rs.getString("text_oggetto"));
				p.setProcedimentoId(rs.getInt("procedimento_id"));
				protocolli.put(String.valueOf(p.getProtocolloId()), p);
			}
		} catch (Exception e) {
			logger.error("getProtocolliEvidenze", e);
			throw new DataException("Cannot load getProtocolliEvidenze");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return protocolli;
	}

	public void setStatoProtocolloAssociato(Connection connection,
			int protocolloId, String stato) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("getProvedimentoFascicoli - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(UPDATE_STATO_PROTOCOLLO);
			pstmt.setString(1, stato);
			pstmt.setInt(2, protocolloId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("setStatoProtocolloAssociato", e);
			throw new DataException("setStatoProtocolloAssociato");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public int contaProcedimenti(Utente utente, HashMap<String,String> sqlDB, String soggettiId)
			throws DataException {

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int risultato = 0;
		StringBuffer strQuery = new StringBuffer(CONTA_LISTA_PROCEDIMENTI);

		if (sqlDB != null) {
			for (Iterator<Entry<String, String>> it = sqlDB.entrySet().iterator(); it.hasNext();) {
				Map.Entry<String,String> entry =it.next();
				Object key = entry.getKey();
				strQuery.append(" AND ").append(key.toString());
			}
		}
		//
		if (soggettiId != null && !soggettiId.trim().equals("")) {
			strQuery.append(" AND (delegato IN(" + soggettiId
					+ ") OR interessato IN(" + soggettiId + "))");
		}
		//
		int indiceQuery = 1;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery.toString());
			pstmt.setInt(indiceQuery++, utente.getAreaOrganizzativa().getId()
					.intValue());
			;
			if (sqlDB != null) {
				for (Iterator<Entry<String, String>> it = sqlDB.entrySet().iterator(); it.hasNext();) {
					Map.Entry<String,String> entry =it.next();
					Object key = entry.getKey();
					Object value = entry.getValue();
					if (value instanceof Integer) {
						pstmt.setInt(indiceQuery++,
								((Integer) value).intValue());
					} else if (value instanceof String) {
						if (key.toString().indexOf("LIKE") > 0) {
							pstmt.setString(indiceQuery++, value.toString()
									+ "%");
						}

						else {
							pstmt.setString(indiceQuery++, value.toString());
						}
					} else if (value instanceof java.util.Date) {
						java.util.Date d = (java.util.Date) value;
						pstmt.setDate(indiceQuery++,
								new java.sql.Date(d.getTime()));
					} else if (value instanceof Boolean) {
						pstmt.setBoolean(indiceQuery++,
								((Boolean) value).booleanValue());
					}
				}
			}
			// analogo. qui l'interessato e il delegato
			rs = pstmt.executeQuery();
			if (rs.next()) {

				risultato = rs.getInt(1);

			}

		} catch (Exception e) {
			logger.error("contaProcedimenti", e);
			throw new DataException("errore nella conta Procedimenti");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return risultato;

	}

	public SortedMap<Integer,ProcedimentoVO> cerca(Utente utente, HashMap<String,String> sqlDB, String soggettiId)
			throws DataException {
		SortedMap<Integer,ProcedimentoVO> procedimenti = new TreeMap<Integer,ProcedimentoVO>(new Comparator<Integer>() {
			public int compare(Integer i1, Integer i2) {
				return i2.intValue() - i1.intValue();
			}
		});
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer strQuery = new StringBuffer(SELECT_LISTA_PROCEDIMENTI);

		if (sqlDB != null) {
			for (Iterator<Entry<String, String>> it = sqlDB.entrySet().iterator(); it.hasNext();) {
				Map.Entry<String, String> entry = it.next();
				Object key = entry.getKey();
				strQuery.append(" AND ").append(key.toString());
			}
		}
		//
		if (soggettiId != null && !soggettiId.trim().equals("")) {
			strQuery.append(" AND (delegato IN(" + soggettiId
					+ ") OR interessato IN(" + soggettiId + "))");
		}
		//
		int indiceQuery = 1;

		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery.toString());
			pstmt.setInt(indiceQuery++, utente.getAreaOrganizzativa().getId()
					.intValue());

			if (sqlDB != null) {
				for (Iterator<Entry<String, String>> it = sqlDB.entrySet().iterator(); it.hasNext();) {
					Map.Entry<String,String> entry = it.next();
					Object key = entry.getKey();
					Object value = entry.getValue();
					if (value instanceof Integer) {
						pstmt.setInt(indiceQuery++,
								((Integer) value).intValue());
					} else if (value instanceof String) {
						if (key.toString().indexOf("LIKE") > 0) {
							pstmt.setString(indiceQuery++, value.toString()
									+ "%");
						} else {
							pstmt.setString(indiceQuery++, value.toString());
						}
					} else if (value instanceof java.util.Date) {
						java.util.Date d = (java.util.Date) value;
						pstmt.setDate(indiceQuery++,
								new java.sql.Date(d.getTime()));
					} else if (value instanceof Boolean) {
						pstmt.setBoolean(indiceQuery++,
								((Boolean) value).booleanValue());
					}
					// indiceQuery++;
				}
			}

			rs = pstmt.executeQuery();
			ProcedimentoVO vo = null;
			while (rs.next()) {
				vo = new ProcedimentoVO();
				vo.setId(rs.getInt("procedimento_id"));
				vo.setDataAvvio(rs.getDate("data_avvio"));
				vo.setUfficioId(rs.getInt("ufficio_id"));
				vo.setStatoId(rs.getInt("stato_id"));
				vo.setTipoFinalitaId(rs.getInt("tipo_finalita_id"));
				vo.setOggetto(rs.getString("oggetto"));
				vo.setTipoProcedimentoId(rs.getInt("tipo_procedimento_id"));
				vo.setTipoProcedimentoDesc(rs.getString("tipo"));
				vo.setReferenteId(rs.getInt("referente_id"));
				vo.setPosizione(rs.getString("posizione_id"));
				vo.setDataEvidenza(rs.getDate("data_evidenza"));
				vo.setNote("note");
				vo.setProtocolloId(rs.getInt("protocollo_id"));
				vo.setNumeroProcedimento(rs.getString("numero_procedimento"));
				vo.setAnno(rs.getInt("anno"));
				vo.setNumero(rs.getInt("numero"));
				vo.setRowCreatedTime(rs.getDate("row_created_time"));
				vo.setRowCreatedUser(rs.getString("row_created_user"));
				vo.setRowUpdatedUser(rs.getString("row_updated_user"));
				vo.setRowUpdatedTime(rs.getDate("row_updated_time"));
				vo.setVersione(rs.getInt("versione"));
				vo.setFascicoloId(rs.getInt("fascicolo_id"));
				procedimenti.put(vo.getId(), vo);
			}

		} catch (Exception e) {
			logger.error("cerca", e);
			throw new DataException("errore nella ricerca");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return procedimenti;

	}

	public void salvaTipoProcedimento(Connection connection,
			TipoProcedimentoVO tipoProcedimentoVO) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("salvaTipoProcedimento() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(INSERT_TIPO_PROCEDIMENTO);
			pstmt.setInt(1, tipoProcedimentoVO.getIdTipo());
			// pstmt.setInt(2, tipoProcedimentoVO.getIdUfficio());
			pstmt.setString(2, tipoProcedimentoVO.getDescrizione());
			pstmt.executeUpdate();
			logger.info("Inserito Tipo Procedimento: TipoProcedimentoId "
					+ tipoProcedimentoVO.getIdTipo() + ".");

		} catch (Exception e) {
			logger.error("salvaTipoProcedimento", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public boolean setProcedimentoSospeso(int procedimentoId, int protocolloId,
			String estremi, String username) throws DataException {
		boolean procedimentoSalvato = false;
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			setProcedimentoSospeso(connection, procedimentoId, estremi,
					username);
			aggiornaFlagSospesoProtocollo(connection, procedimentoId,
					protocolloId, 1);
			connection.commit();
			procedimentoSalvato = true;
		} catch (Exception e) {
			jdbcMan.rollback(connection);
			throw new DataException(e.getMessage()
					+ " setProcedimentoSospeso ProcedimentoId:"
					+ procedimentoId);
		} finally {
			jdbcMan.close(connection);
		}
		return procedimentoSalvato;
	}

	public boolean setProcedimentoIstruttoreLavorato(
			int procedimentoId, int caricaId,String username) throws DataException {
		boolean procedimentoSalvato = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			archiviaVersione(connection, procedimentoId, username);
			pstmt = connection
					.prepareStatement(SET_PROCEDIMENTO_ISTRUTTORE_LAVORATO);
			pstmt.setInt(1, procedimentoId);
			pstmt.setInt(2, caricaId);
			pstmt.executeUpdate();
			connection.commit();
			procedimentoSalvato = true;
		} catch (Exception e) {
			jdbcMan.rollback(connection);
			throw new DataException(
					e.getMessage()
							+ " setProcedimentoAssegnatarioIstruttoreLavorato ProcedimentoId:"
							+ procedimentoId);
		} finally {
			jdbcMan.close(connection);
			jdbcMan.close(pstmt);
		}
		return procedimentoSalvato;
	}

	public boolean setProcedimentoChiuso(int procedimentoId, String username)
			throws DataException {
		boolean procedimentoSalvato = false;
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			procedimentoSalvato = setProcedimentoChiuso(connection,
					procedimentoId, username);
		} catch (Exception e) {
			throw new DataException(e.getMessage()
					+ " setProcedimentoChiuso ProcedimentoId:" + procedimentoId);
		} finally {
			jdbcMan.close(connection);
		}
		return procedimentoSalvato;
	}

	public boolean setProcedimentoChiuso(Connection connection,
			int procedimentoId, String username) throws DataException {
		boolean chiuso = false;
		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			archiviaVersione(connection, procedimentoId,username);
			pstmt = connection.prepareStatement(SET_PROCEDIMENTO_CHIUSO);
			pstmt.setString(1, username);
			pstmt.setInt(2, procedimentoId);
			pstmt.executeUpdate();
			chiuso = true;
		} catch (Exception e) {
			logger.error("setProcedimentoChiuso", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return chiuso;
	}

	public boolean cambiaReferenteProcedimento(Connection connection, int procedimentoId, int ufficioId, int caricaId, String username) throws DataException {
		boolean riassegnato = false;
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			archiviaVersione(connection, procedimentoId,username);
			pstmt = connection.prepareStatement(CAMBIA_REFERENTE_PROCEDIMENTO);
			pstmt.setString(1, username);
			pstmt.setInt(2, ufficioId);
			pstmt.setInt(3, caricaId);
			pstmt.setInt(4, procedimentoId);
			pstmt.executeUpdate();
			riassegnato = true;
		} catch (Exception e) {
			logger.error("setProcedimentoChiuso", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return riassegnato;
	}
	
	public boolean inviaProcedimento(Connection connection,
			int procedimentoId, String posizione,String username) throws DataException {
		boolean inviato = false;
		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			archiviaVersione(connection, procedimentoId,username);
			pstmt = connection.prepareStatement(INVIA_PROCEDIMENTO);
			pstmt.setString(1, username);
			pstmt.setString(2, posizione);
			pstmt.setInt(3, procedimentoId);
			pstmt.executeUpdate();
			inviato = true;
		} catch (Exception e) {
			logger.error("inviaProcedimento", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return inviato;
	}
	
	public boolean cambiaStatoProcedimento(Connection connection,
			int procedimentoId, int stato, String posizione,String username) throws DataException {
		boolean inviato = false;
		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			archiviaVersione(connection, procedimentoId,username);
			pstmt = connection.prepareStatement(CAMBIA_STATO_PROCEDIMENTO);
			pstmt.setString(1, username);
			pstmt.setInt(2, stato);
			pstmt.setString(3, posizione);
			pstmt.setInt(4, procedimentoId);
			pstmt.executeUpdate();
			inviato = true;
		} catch (Exception e) {
			logger.error("inviaProcedimento", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return inviato;
	}
	
	public boolean setProcedimentoAperto(Connection connection,
			int procedimentoId, String username) throws DataException {
		boolean aperto = false;
		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			archiviaVersione(connection, procedimentoId,username);
			pstmt = connection.prepareStatement(SET_PROCEDIMENTO_APERTO);
			pstmt.setString(1, username);
			pstmt.setInt(2, procedimentoId);
			pstmt.executeUpdate();
			aperto = true;
		} catch (Exception e) {
			logger.error("setProcedimentoAperto", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return aperto;
	}

	public boolean setProcedimentoArchiviato(Connection connection,
			int procedimentoId, String username) throws DataException {
		boolean archiviato = false;
		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			archiviaVersione(connection, procedimentoId,username);
			pstmt = connection.prepareStatement(SET_PROCEDIMENTO_ARCHIVIATO);
			pstmt.setString(1, username);
			pstmt.setInt(2, procedimentoId);
			pstmt.executeUpdate();
			archiviato = true;
		} catch (Exception e) {
			logger.error("setProcedimentoArchiviato", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return archiviato;
	}

	public boolean setProcedimentoSospeso(int procedimentoId, String estremi,
			String username) throws DataException {
		boolean procedimentoSalvato = false;
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			procedimentoSalvato = setProcedimentoSospeso(connection,
					procedimentoId, estremi, username);
		} catch (Exception e) {
			throw new DataException(e.getMessage()
					+ " setProcedimentoSospeso ProcedimentoId:"
					+ procedimentoId);
		} finally {
			jdbcMan.close(connection);
		}
		return procedimentoSalvato;
	}

	public boolean setProcedimentoSospeso(Connection connection,
			int procedimentoId, String estremi, String username)
			throws DataException {
		boolean sospeso = false;
		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			archiviaVersione(connection, procedimentoId,username);
			pstmt = connection.prepareStatement(SET_PROCEDIMENTO_SOSPESO);
			pstmt.setString(1, estremi);
			pstmt.setString(2, username);
			pstmt.setInt(3, procedimentoId);
			pstmt.executeUpdate();
			sospeso = true;
		} catch (Exception e) {
			logger.error("setProcedimentoSospeso", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return sospeso;
	}

	public boolean setProcedimentoRiavviato(int procedimentoId, String estremi,
			String dataScadenza, String username) throws DataException {
		boolean procedimentoSalvato = false;
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			procedimentoSalvato = setProcedimentoRiavviato(connection,
					procedimentoId, estremi, dataScadenza, username);
		} catch (Exception e) {
			throw new DataException(e.getMessage()
					+ " setProcedimentoRiavviato ProcedimentoId:"
					+ procedimentoId);
		} finally {
			jdbcMan.close(connection);
		}
		return procedimentoSalvato;
	}

	public boolean setProcedimentoRiavviato(int procedimentoId,
			int protocolloId, String estremi, String dataScadenza,
			String username) throws DataException {
		boolean procedimentoSalvato = false;
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			setProcedimentoRiavviato(connection, procedimentoId, estremi,
					dataScadenza, username);
			aggiornaFlagSospesoProtocollo(connection, procedimentoId,
					protocolloId, 2);
			connection.commit();
			procedimentoSalvato = true;
		} catch (Exception e) {
			jdbcMan.rollback(connection);
			throw new DataException(e.getMessage()
					+ " setProcedimentoRiavviato ProcedimentoId:"
					+ procedimentoId);
		} finally {
			jdbcMan.close(connection);
		}
		return procedimentoSalvato;
	}

	public void aggiornaFlagSospesoProtocollo(Connection connection,
			int procedimentoId, int protocolloId, int stato)
			throws DataException {
		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			azzeraFlagSospesoProtocollo(connection, procedimentoId);
			pstmt = connection
					.prepareStatement(UPDATE_PROTOCOLLO_PROCEDIMENTO_SOSPESO);
			pstmt.setInt(1, stato);
			pstmt.setInt(2, protocolloId);
			pstmt.setInt(3, procedimentoId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("setProcedimentoRiavviato", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void azzeraFlagSospesoProtocollo(Connection connection,
			int procedimentoId) throws DataException {
		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection
					.prepareStatement(UPDATE_ALL_PROTOCOLLO_PROCEDIMENTO_SOSPESO);
			pstmt.setInt(1, procedimentoId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("setProcedimentoRiavviato", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public boolean setProcedimentoRiavviato(Connection connection,
			int procedimentoId, String estremi, String dataScadenza,
			String username) throws DataException {
		boolean riavviato = false;
		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			archiviaVersione(connection, procedimentoId,username);
			pstmt = connection.prepareStatement(SET_PROCEDIMENTO_RIAVVIATO);
			if (dataScadenza != null)
				pstmt.setDate(1, new Date(DateUtil.getData(dataScadenza)
						.getTime()));
			else
				pstmt.setNull(1, Types.DATE);
			pstmt.setString(2, estremi);
			pstmt.setString(3, username);
			pstmt.setInt(4, procedimentoId);
			pstmt.executeUpdate();
			riavviato = true;
		} catch (Exception e) {
			logger.error("setProcedimentoRiavviato", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return riavviato;
	}

	public boolean annullaProcedimentoSospeso(int procedimentoId,
			String estremi, String username) throws DataException {
		boolean procedimentoSalvato = false;
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			annullaProcedimentoSospeso(connection, procedimentoId, estremi,
					username);
			azzeraFlagSospesoProtocollo(connection, procedimentoId);
			connection.commit();
			procedimentoSalvato = true;
		} catch (Exception e) {
			jdbcMan.rollback(connection);
			throw new DataException(e.getMessage()
					+ " annullaProcedimentoSospeso ProcedimentoId:"
					+ procedimentoId);
		} finally {
			jdbcMan.close(connection);
		}
		return procedimentoSalvato;
	}

	public boolean annullaProcedimentoSospeso(Connection connection,
			int procedimentoId, String estremi, String username)
			throws DataException {
		boolean annulla_sospensione = false;
		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			// connection.setAutoCommit(false);
			archiviaVersione(connection, procedimentoId,username);
			pstmt = connection.prepareStatement(ANNULLA_PROCEDIMENTO_SOSPESO);
			pstmt.setString(1, estremi);
			pstmt.setString(2, username);
			pstmt.setInt(3, procedimentoId);
			pstmt.executeUpdate();
			annulla_sospensione = true;
			// connection.commit();
		} catch (Exception e) {
			// connection.rollback();
			logger.error("annullaProcedimentoSospeso", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return annulla_sospensione;
	}

	public int getIntervalloTempoSospensione(int procedimentoId)
			throws DataException {
		int intervallo = 0;
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			intervallo = getIntervalloTempoSospensione(connection,
					procedimentoId);
		} catch (Exception e) {
			throw new DataException(e.getMessage()
					+ " getIntervalloTempoSospensione ProcedimentoId:"
					+ procedimentoId);
		} finally {
			jdbcMan.close(connection);
		}
		return intervallo;
	}

	public int getIntervalloTempoSospensione(Connection connection,
			int procedimentoId) throws DataException {
		int intervallo = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection
					.prepareStatement(GET_INTERVALLO_PROCEDIMENTO_SOSPESO);
			pstmt.setInt(1, procedimentoId);
			rs = pstmt.executeQuery();
			if (rs.next())
				intervallo = rs.getInt(1);
		} catch (Exception e) {
			logger.error("getIntervalloTempoSospensione", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);

		}
		return intervallo;
	}

	// riferimenti e istruttori
	public final static String INSERT_RIFERIMENTI = "INSERT INTO riferimenti_legislativi"
			+ " ( riferimento_id,procedimento_id,documento_id) VALUES (?, ?, ?)";

	public final static String SELECT_RIFERIMENTI = "SELECT documento_id FROM riferimenti_legislativi WHERE procedimento_id = ?";

	public final String DELETE_RIFERIMENTI = "DELETE FROM riferimenti_legislativi WHERE procedimento_id = ?";

	public final static String INSERT_ISTRUTTORI = "INSERT INTO procedimento_istruttori"
			+ " ( procedimento_id,carica_id,versione, flag_lavorato,row_created_user) VALUES (?, ?, ?, ?, ?)";

	public final static String SELECT_ISTRUTTORI = "SELECT * FROM procedimento_istruttori WHERE procedimento_id = ?";

	public final static String SELECT_STORIA_ISTRUTTORI = "SELECT * FROM storia_procedimento_istruttori WHERE procedimento_id=? AND versione=?";

	public final String DELETE_ISTRUTTORI = "DELETE FROM procedimento_istruttori WHERE procedimento_id = ?";
	//

	private static final String INSERT_PROCEDIMENTO = "insert into procedimenti"
			+ "(procedimento_id ,data_avvio ,ufficio_id ,stato_id ,tipo_finalita_id ,oggetto ,tipo_procedimento_id ,referente_id ,responsabile_id,"
			+ "posizione_id ,data_evidenza ,note ,protocollo_id ,"
			+ "numero_procedimento ,anno ,numero ,row_created_time ,row_created_user ,row_updated_user ,row_updated_time ,versione , aoo_id, giorni_max,giorni_alert,fascicolo_id,delegato, interessato, indi_delegato, indi_interessato, indicazioni, autorita, estremi_provvedimento, data_scadenza) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?)";

	private static final String INSERT_STORIA_PROCEDIMENTO = "insert into storia_procedimenti(procedimento_id ,data_avvio ,ufficio_id ,stato_id ,tipo_finalita_id ,oggetto ,tipo_procedimento_id ,referente_id ,responsabile_id,"
			+ "posizione_id ,data_evidenza ,note ,protocollo_id ,"
			+ "numero_procedimento ,anno ,numero ,row_created_time ,row_created_user ,row_updated_user ,row_updated_time ,versione , aoo_id, giorni_max,giorni_alert) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	
	private static final String SET_PROCEDIMENTO_SOSPESO = "UPDATE procedimenti set data_sospensione=now(), estremi_sospensione=?, row_created_user=?, flag_sospeso=1, row_created_time=now() where procedimento_id=?";

	private static final String SET_PROCEDIMENTO_CHIUSO = "UPDATE procedimenti set row_created_user=?, stato_id=1, row_created_time=now() where procedimento_id=?";

	private static final String INVIA_PROCEDIMENTO= "UPDATE procedimenti set row_created_user=?, posizione_id=?, row_created_time=now() where procedimento_id=?";

	private static final String CAMBIA_STATO_PROCEDIMENTO= "UPDATE procedimenti set row_created_user=?, stato_id=?, posizione_id=?, row_created_time=now() where procedimento_id=?";

	
	private static final String CAMBIA_REFERENTE_PROCEDIMENTO = "UPDATE procedimenti set row_created_user=?, ufficio_id=?, responsabile_id=?, referente_id=null, posizione_id=null, row_created_time=now() where procedimento_id=?";

	private static final String SET_PROCEDIMENTO_APERTO = "UPDATE procedimenti set row_created_user=?, stato_id=0, row_created_time=now() where procedimento_id=?";

	private static final String SET_PROCEDIMENTO_ARCHIVIATO = "UPDATE procedimenti set row_created_user=?, stato_id=2, row_created_time=now() where procedimento_id=?";

	private static final String SET_PROCEDIMENTO_ISTRUTTORE_LAVORATO = "UPDATE procedimento_istruttori set flag_lavorato=1 where procedimento_id=? AND carica_id=?";

	private static final String SET_PROCEDIMENTO_RIAVVIATO = "UPDATE procedimenti set data_sospensione=NULL, data_scadenza=?, estremi_sospensione=?, row_created_user=?, flag_sospeso=0, row_created_time=now() where procedimento_id=?";

	private static final String UPDATE_PROTOCOLLO_PROCEDIMENTO_SOSPESO = "UPDATE protocollo_procedimenti set flag_sospeso=? WHERE protocollo_id=? AND procedimento_id=?";

	private static final String UPDATE_ALL_PROTOCOLLO_PROCEDIMENTO_SOSPESO = "UPDATE protocollo_procedimenti set flag_sospeso=0 WHERE procedimento_id=?";

	private static final String ANNULLA_PROCEDIMENTO_SOSPESO = "UPDATE procedimenti set data_sospensione=NULL, estremi_sospensione=?, row_created_user=?, flag_sospeso=0, row_created_time=now() where procedimento_id=?";

	private static final String GET_INTERVALLO_PROCEDIMENTO_SOSPESO = "SELECT date_part('day',now()-data_sospensione) FROM procedimenti WHERE procedimento_id=?";

	private static final String UPDATE_PROCEDIMENTO = "update procedimenti set data_avvio=? ,ufficio_id=? ,stato_id=? ,tipo_finalita_id=? ,"
			+ "oggetto=? ,tipo_procedimento_id=? ,referente_id=? ,responsabile_id=?, "
			+ "data_evidenza=? ,note=? ,protocollo_id=? ,row_updated_user=? ,row_updated_time=? ,  giorni_max=?,giorni_alert=?,delegato=?,interessato=?, indi_delegato=?, indi_interessato=?, indicazioni=?,autorita=?,estremi_provvedimento=?,posizione_id=? where procedimento_id=?";

	private static final String INSERT_PROCEDIMENTO_FALDONI = "insert into procedimenti_faldone (procedimento_id,faldone_id) values (?,?)";

	private static final String INSERT_TIPO_PROCEDIMENTO = "insert into tipi_procedimento (tipo_procedimenti_id,titolario_id,tipo) values (?,?,?)";

	private static final String INSERT_PROCEDIMENTO_FASCICOLI = "insert into procedimenti_fascicolo (procedimento_id,fascicolo_id,row_created_time, row_created_user, "
			+ "row_updated_user, row_updated_time, versione) values (?,?,?,?,?,?,?)";

	private static final String INSERT_PROCEDIMENTO_PROTOCOLLI = "insert into protocollo_procedimenti ("
			+ "procedimento_id, protocollo_id, row_created_time, row_created_user, "
			+ "row_updated_user, row_updated_time, versione) values (?,?,?,?,?,?,?)";

	private static final String DELETE_PROCEDIMENTO_FASCICOLI = "delete from procedimenti_fascicolo where procedimento_id=?";

	private static final String DELETE_PROCEDIMENTO_PROTOCOLLI = "delete from protocollo_procedimenti where procedimento_id=?";

	private static final String DELETE_PROCEDIMENTO_FALDONI = "delete from procedimenti_faldone where procedimento_id=?";

	private final static String SELECT_ULTIMO_PROCEDIMENTO = "SELECT MAX(numero) FROM procedimenti WHERE aoo_id = ? and anno=?";

	private final static String SELECT_PROCEDIMENTO = " SELECT  distinct tipo_procedimenti_id, tipo, procedimenti.*"
			+ "FROM  procedimenti  left outer join TIPI_PROCEDIMENTO on (procedimenti.tipo_procedimento_id=tipi_procedimento.tipo_procedimenti_id)"
			+ " where procedimento_id=?";

	public final static String SELECT_PROCEDIMENTO_BY_ID_VERSIONE = "SELECT  distinct tipo_procedimenti_id, tipo, storia_procedimenti.* "
			+ " FROM  storia_procedimenti left outer join TIPI_PROCEDIMENTO on (storia_procedimenti.tipo_procedimento_id=tipi_procedimento.tipo_procedimenti_id) "
			+ " WHERE procedimento_id = ? and versione = ? ";

	public final static String SELECT_PROCEDIMENTO_BY_ID_FASCICOLO = "SELECT  distinct tipo_procedimenti_id, tipo, procedimenti.* "
			+ " FROM  procedimenti left outer join TIPI_PROCEDIMENTO on (procedimenti.tipo_procedimento_id=tipi_procedimento.tipo_procedimenti_id) "
			+ " WHERE fascicolo_id = ?  ";

	public final static String SELECT_PROCEDIMENTO_ID_BY_ID_FASCICOLO = "SELECT procedimento_id FROM procedimenti WHERE fascicolo_id = ?";
	
	public final static String SELECT_FASCICOLO_ID_BY_ID_PROCEDIMENTO = "SELECT fascicolo_id FROM procedimenti WHERE procedimento_id = ?";

	
	private final static String SELECT_PROCEDIMENTO_BY_ANNO_NUMERO = "select * from procedimenti where anno=? and numero=?";

	private final static String SELECT_PROCEDIMENTO_FALDONI = "select * from procedimenti_faldone where procedimento_id=?";

	private final static String SELECT_PROCEDIMENTO_FASCICOLI = "select * from procedimenti_fascicolo where procedimento_id=?";

	private final static String SELECT_STORIA_PROCEDIMENTO_FASCICOLI = "select * from storia_procedimenti_fascicolo where procedimento_id=? AND versione=?";

	private final static String SELECT_PROCEDIMENTO_PROTOCOLLI = "SELECT prot_proc.procedimento_id,prot_proc.flag_sospeso, "
			+ " prot.protocollo_id,prot.nume_protocollo,prot.anno_registrazione,prot.documento_id,prot.text_oggetto"
			+ " FROM PROTOCOLLI prot LEFT JOIN protocollo_procedimenti prot_proc ON (prot.protocollo_id=prot_proc.protocollo_id)"
			+ " WHERE prot_proc.procedimento_id=?  ";

	private final static String SELECT_STORIA_PROCEDIMENTO_PROTOCOLLI = "SELECT prot_proc.procedimento_id,prot_proc.flag_sospeso, "
			+ " prot.protocollo_id,prot.nume_protocollo,prot.anno_registrazione,prot.documento_id,prot.text_oggetto"
			+ " FROM PROTOCOLLI prot LEFT JOIN storia_protocollo_procedimenti prot_proc ON (prot.protocollo_id=prot_proc.protocollo_id)"
			+ " WHERE prot_proc.procedimento_id=? AND prot_proc.versione=?";

	private final static String UPDATE_STATO_PROTOCOLLO = "update protocolli set stato_protocollo=? where protocollo_id=?";

	private final static String CONTA_LISTA_PROCEDIMENTI = " SELECT  COUNT(*)"
			+ "FROM  procedimenti left outer join TIPI_PROCEDIMENTO on (procedimenti.tipo_procedimento_id=tipi_procedimento.tipo_procedimenti_id) WHERE aoo_id=? ";

	private final static String SELECT_LISTA_PROCEDIMENTI = " SELECT  distinct tipo_procedimenti_id, tipo, procedimenti.* "
			+ "FROM  procedimenti left outer join TIPI_PROCEDIMENTO on (procedimenti.tipo_procedimento_id=tipi_procedimento.tipo_procedimenti_id) WHERE aoo_id=? ";

	//
	public final static String SELECT_RICORSO_RIASSEGNATO = "SELECT p.*  FROM fenice.procedimenti p "
			+ "LEFT JOIN fenice.tipi_procedimento t ON p.tipo_procedimento_id=t.tipo_procedimenti_id "
			
			+ "WHERE t.flag_ull=1 AND p.responsabile_id=? AND p.referente_id IS NULL";

	public final static String COUNT_RICORSO_RIASSEGNATO = "SELECT count(p.procedimento_id) FROM fenice.procedimenti p "
			+ "LEFT JOIN fenice.tipi_procedimento t ON p.tipo_procedimento_id=t.tipo_procedimenti_id "
			+ "WHERE t.flag_ull=1 AND p.responsabile_id=? AND p.referente_id IS NULL";
	
	public final static String SELECT_RICORSO_FASE_ISTRUTTORIA = "SELECT p.* "
			+ " FROM fenice.procedimenti p "
			+ "LEFT JOIN fenice.procedimento_istruttori i ON p.procedimento_id=i.procedimento_id "
			+ "LEFT JOIN fenice.tipi_procedimento t ON p.tipo_procedimento_id=t.tipo_procedimenti_id "
			+ "WHERE p.stato_id=3 AND i.carica_id=? AND t.flag_ull=1 AND NOT p.referente_id IS NULL";
	
	public final static String COUNT_RICORSO_FASE_ISTRUTTORIA = "SELECT count(p.procedimento_id) FROM fenice.procedimenti p "
			+ "LEFT JOIN fenice.procedimento_istruttori i ON p.procedimento_id=i.procedimento_id "
			+ "LEFT JOIN fenice.tipi_procedimento t ON p.tipo_procedimento_id=t.tipo_procedimenti_id "
			+ "WHERE p.stato_id=3 AND i.carica_id=? AND t.flag_ull=1 AND NOT p.referente_id IS NULL";

	public final static String SELECT_RICORSO_FASE_RELATORIA = "SELECT p.*  FROM fenice.procedimenti p "
			+ "LEFT JOIN fenice.tipi_procedimento t ON p.tipo_procedimento_id=t.tipo_procedimenti_id "
			+ "LEFT JOIN fenice.procedimento_istruttori i ON p.procedimento_id=i.procedimento_id "
			+ "WHERE p.stato_id=4  AND t.flag_ull=1 AND ((p.referente_id=? AND p.posizione_id IS NULL) OR (p.posizione_id='I' AND i.carica_id=?))";

	public final static String COUNT_RICORSO_FASE_RELATORIA = "SELECT count(p.procedimento_id) FROM fenice.procedimenti p "
			+ "LEFT JOIN fenice.tipi_procedimento t ON p.tipo_procedimento_id=t.tipo_procedimenti_id "
			+ "LEFT JOIN fenice.procedimento_istruttori i ON p.procedimento_id=i.procedimento_id "
			+ "WHERE p.stato_id=4  AND t.flag_ull=1 AND ((p.referente_id=? AND p.posizione_id IS NULL) OR (p.posizione_id='I' AND i.carica_id=?))";

	public final static String SELECT_RICORSO_PARERE_CONSIGLIO = "SELECT p.* FROM fenice.procedimenti p "
			+ "LEFT JOIN fenice.procedimento_istruttori i ON p.procedimento_id=i.procedimento_id "
			+ "LEFT JOIN fenice.tipi_procedimento t ON p.tipo_procedimento_id=t.tipo_procedimenti_id "
			+ "WHERE p.stato_id=5  AND t.flag_ull=1 AND NOT p.referente_id IS NULL AND ((p.responsabile_id=? AND p.posizione_id IS NULL) OR (p.posizione_id='I' AND i.carica_id=?) OR (p.posizione_id='F' AND p.referente_id=?))";

	public final static String COUNT_RICORSO_PARERE_CONSIGLIO = "SELECT count(p.procedimento_id) FROM fenice.procedimenti p "
			+ "LEFT JOIN fenice.procedimento_istruttori i ON p.procedimento_id=i.procedimento_id "
			+ "LEFT JOIN fenice.tipi_procedimento t ON p.tipo_procedimento_id=t.tipo_procedimenti_id "
			+ "WHERE p.stato_id=5  AND t.flag_ull=1 AND NOT p.referente_id IS NULL AND ((p.responsabile_id=? AND p.posizione_id IS NULL) OR (p.posizione_id='I' AND i.carica_id=?) OR (p.posizione_id='F' AND p.referente_id=?))";

	public final static String SELECT_RICORSO_DECRETO_PRESIDENTE = "SELECT p.* FROM fenice.procedimenti p "
			+ "LEFT JOIN fenice.procedimento_istruttori i ON p.procedimento_id=i.procedimento_id "
			+ "LEFT JOIN fenice.tipi_procedimento t ON p.tipo_procedimento_id=t.tipo_procedimenti_id "
			+ "WHERE p.stato_id=6  AND t.flag_ull=1 AND NOT p.referente_id IS NULL AND i.carica_id=? AND p.posizione_id IS NULL";

	public final static String COUNT_RICORSO_DECRETO_PRESIDENTE = "SELECT count(p.procedimento_id) FROM fenice.procedimenti p "
			+ "LEFT JOIN fenice.procedimento_istruttori i ON p.procedimento_id=i.procedimento_id "
			+ "LEFT JOIN fenice.tipi_procedimento t ON p.tipo_procedimento_id=t.tipo_procedimenti_id "
			+ "WHERE p.stato_id=6  AND t.flag_ull=1 AND NOT p.referente_id IS NULL AND i.carica_id=? AND p.posizione_id IS NULL";

	public final static String SELECT_ATTENDI_RICORSO_DECRETO_PRESIDENTE = "SELECT p.* FROM fenice.procedimenti p "
			+ "LEFT JOIN fenice.procedimento_istruttori i ON p.procedimento_id=i.procedimento_id "
			+ "LEFT JOIN fenice.tipi_procedimento t ON p.tipo_procedimento_id=t.tipo_procedimenti_id "
			+ "WHERE p.stato_id=7  AND t.flag_ull=1 AND NOT p.referente_id IS NULL AND i.carica_id=? AND p.posizione_id IS NULL";

	public final static String COUNT_ATTENDI_RICORSO_DECRETO_PRESIDENTE = "SELECT count(p.procedimento_id) FROM fenice.procedimenti p "
			+ "LEFT JOIN fenice.procedimento_istruttori i ON p.procedimento_id=i.procedimento_id "
			+ "LEFT JOIN fenice.tipi_procedimento t ON p.tipo_procedimento_id=t.tipo_procedimenti_id "
			+ "WHERE p.stato_id=7  AND t.flag_ull=1 AND NOT p.referente_id IS NULL AND i.carica_id=? AND p.posizione_id IS NULL";

	
	private static final String INSERT_PROTOCOLLI_PROCEDIMENTI = "INSERT INTO protocollo_procedimenti ("
			+ "procedimento_id, protocollo_id, row_created_time, row_created_user, "
			+ "row_updated_user, row_updated_time, versione,flag_sospeso) values (?,?,?,?,?,?,?,?)";

	private static final String DELETE_PROTOCOLLI_PROCEDIMENTI = "DELETE FROM protocollo_procedimenti WHERE protocollo_id=? AND procedimento_id=?";

	public final static String INSERT_FASCICOLO_PROCEDIMENTO = "INSERT INTO procedimenti_fascicolo"
			+ " (fascicolo_id, procedimento_id, versione, row_created_user) VALUES (?, ?, ?, ?)";

	public final static String DELETE_FASCICOLO_PROCEDIMENTO = "DELETE FROM procedimenti_fascicolo WHERE fascicolo_id = ? AND procedimento_id = ?";

	public final static String SELECT_ALLACCIO = "SELECT DISTINCT "
			+ " p.protocollo_id,p.anno_registrazione,p.nume_protocollo,"
			+ " p.flag_tipo,p.flag_riservato,p.text_oggetto,p.data_registrazione,"
			+ " p.flag_tipo_mittente,p.desc_cognome_mittente,p.desc_nome_mittente,"
			+ " p.desc_denominazione_mittente "
			+ " FROM protocolli p"
			+ " LEFT JOIN protocollo_procedimenti a ON (a.protocollo_id=p.protocollo_id)"
			+ " WHERE p.registro_id = ? AND a.procedimento_id=? AND a.flag_sospeso=0 AND p.nume_protocollo=? AND p.anno_registrazione=?";

	private final static String PROCEDIMENTO_VISUALIZZABILE = "SELECT  procedimenti.procedimento_id FROM  procedimenti left outer join procedimento_istruttori on (procedimenti.procedimento_id=procedimento_istruttori.procedimento_id)  WHERE (carica_id=? OR referente_id=? OR responsabile_id=? OR carica_lav_id=?) AND procedimenti.procedimento_id=? AND procedimenti.stato_id=0";

	public final static String DELETE_UFFICI_PARTECIPANTI = "DELETE FROM procedimento_uffici_partecipanti WHERE procedimento_id = ?";

	public final static String UFFICI_PARTECIPANTI = "SELECT tp.ufficio_id, u.descrizione, tp.flag_visibilita FROM procedimento_uffici_partecipanti tp LEFT JOIN uffici u ON(tp.ufficio_id=u.ufficio_id) WHERE tp.procedimento_id=? ";
	
	public final static String INSERT_UFFICIO_PARTECIPANTE = "INSERT INTO procedimento_uffici_partecipanti (puv_id, procedimento_id, ufficio_id, flag_visibilita, row_created_user) VALUES (?, ?, ?, ?, ?)";



}