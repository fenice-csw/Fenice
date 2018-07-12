package it.finsiel.siged.dao.jdbc;

import it.compit.fenice.enums.TipoVisibilitaUfficioEnum;
import it.compit.fenice.mvc.vo.protocollo.DocumentoFascicoloVO;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.integration.FascicoloDAO;
import it.finsiel.siged.mvc.presentation.helper.FascicoloInvioView;
import it.finsiel.siged.mvc.presentation.helper.FascicoloView;
import it.finsiel.siged.mvc.presentation.helper.ProtocolloFascicoloView;
import it.finsiel.siged.mvc.vo.MigrazioneVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.InvioFascicoliDestinatariVO;
import it.finsiel.siged.mvc.vo.protocollo.InvioFascicoliVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloFascicoloVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public class FascicoloDAOjdbc implements FascicoloDAO {

	static Logger logger = Logger.getLogger(FascicoloDAOjdbc.class.getName());

	private JDBCManager jdbcMan = new JDBCManager();

	public static void putAllegato(DocumentoVO doc, Map<String,DocumentoVO> documenti) {
		int idx = doc.getIdx();
		if (idx == 0) {
			idx = getNextDocIdx(documenti);
		}
		doc.setIdx(idx);
		documenti.put(String.valueOf(idx), doc);
	}

	private static int getNextDocIdx(Map<String,DocumentoVO> allegati) {
		int max = 0;
		Iterator<String> it = allegati.keySet().iterator();
		while (it.hasNext()) {
			String id = (String) it.next();
			int cur = NumberUtil.getInt(id);
			if (cur > max)
				max = cur;
		}
		return max + 1;
	}

	public FascicoloVO newFascicolo(Connection connection, FascicoloVO fascicolo)
			throws DataException {
		FascicoloVO newFascicolo = addFascicolo(connection, fascicolo,
				INSERT_FASCICOLO);
		return newFascicolo;
	}

	public FascicoloVO newFascicoloStoria(Connection connection,
			FascicoloVO fascicolo) throws DataException {
		FascicoloVO newFascicolo = addFascicolo(connection, fascicolo,
				INSERT_STORIA_FASCICOLO);
		return newFascicolo;
	}

	private FascicoloVO addFascicolo(Connection connection,
			FascicoloVO fascicolo, String sql) throws DataException {
		FascicoloVO newFascicolo = new FascicoloVO();
		newFascicolo.setReturnValue(ReturnValues.UNKNOWN);
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("addFascicolo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(sql);

			int n = 0;
			pstmt.setInt(++n, fascicolo.getId().intValue()); // fascicolo_id
			pstmt.setInt(++n, fascicolo.getAooId()); // aoo_id
			pstmt.setInt(++n, fascicolo.getRegistroId()); // registro_id
			pstmt.setString(++n, fascicolo.getCodice()); // codice
			pstmt.setString(++n, fascicolo.getDescrizione()); // descrizione
			pstmt.setString(++n, fascicolo.getNome()); // nome
			pstmt.setLong(++n, fascicolo.getProgressivo()); // progressivo
			pstmt.setInt(++n, fascicolo.getAnnoRiferimento()); // anno_riferimento
			pstmt.setInt(++n, fascicolo.getTipoFascicolo()); // tipo

			// responsabile
			pstmt.setInt(++n, fascicolo.getUfficioResponsabileId()); // ufficio
			if (fascicolo.getCaricaResponsabileId() > 0) {
				pstmt.setInt(++n, fascicolo.getCaricaResponsabileId()); // carica
			} else {
				pstmt.setNull(++n, Types.INTEGER); // utente
			}

			// intestatario
			pstmt.setInt(++n, fascicolo.getUfficioIntestatarioId()); // ufficio
			if (fascicolo.getCaricaIntestatarioId() > 0) {
				pstmt.setInt(++n, fascicolo.getCaricaIntestatarioId()); // carica
			} else {
				pstmt.setNull(++n, Types.INTEGER); // carica
			}
			// pstmt.setString(++n, fascicolo.getCollocazione()); //
			// collocazione
			pstmt.setString(++n, fascicolo.getNote()); // note
			pstmt.setInt(++n, fascicolo.getProcessoId()); // processo_id
			pstmt.setString(++n, fascicolo.getOggetto()); // oggetto

			pstmt.setDate(++n, new Date(fascicolo.getDataApertura().getTime())); // data_apertura
			pstmt.setInt(++n, fascicolo.getStato()); // stato
			pstmt.setString(++n, fascicolo.getRowCreatedUser()); // row_created_user
			if (fascicolo.getTitolarioId() > 0) {
				pstmt.setInt(++n, fascicolo.getTitolarioId()); // titolario_id
			} else {
				pstmt.setNull(++n, Types.INTEGER); // null
			}

			if (fascicolo.getDataEvidenza() != null) {
				pstmt.setDate(++n, new Date(fascicolo.getDataEvidenza()
						.getTime())); // data_evidenza
			} else {
				pstmt.setNull(++n, Types.DATE);
			}

			if (fascicolo.getDataUltimoMovimento() != null) {
				pstmt.setDate(++n, new Date(fascicolo.getDataUltimoMovimento()
						.getTime())); // data_ultimo_movimento
			} else {
				pstmt.setNull(++n, Types.DATE);
			}

			if (fascicolo.getDataScarto() != null) {
				pstmt.setDate(++n,
						new Date(fascicolo.getDataScarto().getTime())); // data
				// scarto
			} else {
				pstmt.setNull(++n, Types.DATE);
			}

			if (fascicolo.getDataCarico() != null) {
				pstmt.setDate(++n,
						new Date(fascicolo.getDataCarico().getTime())); // data
				// carico
			} else {
				pstmt.setNull(++n, Types.DATE);
			}

			if (fascicolo.getDataScarico() != null) {
				pstmt.setDate(++n, new Date(fascicolo.getDataScarico()
						.getTime())); // data scarico
			} else {
				pstmt.setNull(++n, Types.DATE);
			}

			pstmt.setInt(++n, fascicolo.getVersione()); // versione
			pstmt.setString(++n, fascicolo.getCollocazioneLabel1()); // CollocazioneLabel1
			pstmt.setString(++n, fascicolo.getCollocazioneLabel2()); // CollocazioneLabel2
			pstmt.setString(++n, fascicolo.getCollocazioneLabel3()); // CollocazioneLabel3
			pstmt.setString(++n, fascicolo.getCollocazioneLabel4()); // CollocazioneLabel4
			pstmt.setString(++n, fascicolo.getCollocazioneValore1());// CollocazioneValore1
			pstmt.setString(++n, fascicolo.getCollocazioneValore2());// CollocazioneValore2
			pstmt.setString(++n, fascicolo.getCollocazioneValore3());// CollocazioneValore3
			pstmt.setString(++n, fascicolo.getCollocazioneValore4());// CollocazioneValore4
			//
			if (fascicolo.getCaricaIstruttoreId() > 0) {
				pstmt.setInt(++n, fascicolo.getCaricaIstruttoreId()); // istruttore
			} else {
				pstmt.setNull(++n, Types.INTEGER); // istruttore
			}
			pstmt.setString(++n, fascicolo.getInteressato()); // interessato
			pstmt.setString(++n, fascicolo.getDelegato()); // delegato
			pstmt.setString(++n, fascicolo.getIndiInteressato()); // indi_interessato
			pstmt.setString(++n, fascicolo.getIndiDelegato()); // indi_delegato
			
			if (fascicolo.getParentId() > 0) {
				pstmt.setInt(++n, fascicolo.getParentId()); // padre
			} else {
				pstmt.setNull(++n, Types.INTEGER); // padre
			}
			if (fascicolo.getGiorniMax() > 0) {
				pstmt.setInt(++n, fascicolo.getGiorniMax()); // giorniMax
			} else {
				pstmt.setNull(++n, Types.INTEGER); // giorniMax
			}
			if (fascicolo.getGiorniAlert() > 0) {
				pstmt.setInt(++n, fascicolo.getGiorniAlert()); // giorniAlert
			} else {
				pstmt.setNull(++n, Types.INTEGER); // giorniAlert
			}
			pstmt.setString(++n, fascicolo.getComune());
			pstmt.setString(++n, fascicolo.getCapitolo());
			pstmt.setString(++n, fascicolo.getPathProgressivo());

			//
			pstmt.executeUpdate();
			newFascicolo = getFascicoloById(connection, fascicolo.getId()
					.intValue());
			if (newFascicolo.getReturnValue() == ReturnValues.FOUND) {
				newFascicolo.setReturnValue(ReturnValues.SAVED);
				logger.debug("Fascicolo inserito:"
						+ fascicolo.getId().intValue());
			} else {
				logger
						.warn("Errore nella lettura del FascicoloVO dopo il salvataggio! Id:"
								+ fascicolo.getId().intValue());
				throw new DataException("Errore nel salvataggio del Fascicolo.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Save Fascicolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return newFascicolo;
	}

	public Collection<FascicoloVO> getFascicoliByProtocolloId(int id) throws DataException {
		Collection<FascicoloVO> fascicoli = new ArrayList<FascicoloVO>();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			fascicoli = getFascicoliByProtocolloId(connection, id);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " ProtocolloId :" + id);
		} finally {
			jdbcMan.close(connection);
		}
		return fascicoli;
	}

	public Collection<Integer> getCollegatiIdByFascicoloId(int id) throws DataException {
		Collection<Integer> fascicoli = new ArrayList<Integer>();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			fascicoli = getCollegatiIdByFascicoloId(connection, id);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " fascicoloid :" + id);
		} finally {
			jdbcMan.close(connection);
		}
		return fascicoli;
	}

	public Collection<Integer> getSottoFascicoliIdByFascicoloId(int id)
			throws DataException {
		Collection<Integer> fascicoli = new ArrayList<Integer>();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			fascicoli = getSottoFascicoliIdByFascicoloId(connection, id);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " fascicoloid :" + id);
		} finally {
			jdbcMan.close(connection);
		}
		return fascicoli;
	}

	public Collection<FascicoloVO> getFascicoliByDocumentoId(int id) throws DataException {
		Collection<FascicoloVO> fascicoli = new ArrayList<FascicoloVO>();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			fascicoli = getFascicoliByDocumentoId(connection, id);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " DocumentoId :" + id);
		} finally {
			jdbcMan.close(connection);
		}
		return fascicoli;
	}

	public Collection<FascicoloVO> getFascicoliByDocumentoId(Connection connection,
			int documentoId) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Collection<FascicoloVO> fascicoli = new ArrayList<FascicoloVO>();
		FascicoloVO fascicolo = new FascicoloVO();
		try {
			if (connection == null) {
				logger
						.warn("getFascicoliByDocumentoId() - Invalid Connection :"
								+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection
					.prepareStatement(SELECT_FASCICOLI_BY_DOCUMENTO_ID);
			pstmt.setInt(1, documentoId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				fascicolo = getFascicolo(rs);
				fascicoli.add(fascicolo);
			}

		} catch (Exception e) {
			logger.error("Load getFascicoliByDocumentoId by ID", e);
			throw new DataException("Cannot load FascicoliByDocumentoId by ID");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return fascicoli;
	}

	public Collection<FascicoloVO> getFascicoloByAooId(int aooId) throws DataException {
		Collection<FascicoloVO> fascicoli = null;
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			fascicoli = getFascicoliByAooId(connection, aooId);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " AooId :" + aooId);
		} finally {
			jdbcMan.close(connection);
		}
		return fascicoli;
	}

	public Collection<FascicoloVO> getFascicoliByAooId(Connection connection, int aooId)
			throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Collection<FascicoloVO> fascicoli = new ArrayList<FascicoloVO>();
		FascicoloVO fascicolo = new FascicoloVO();
		try {
			if (connection == null) {
				logger.warn("getFascicoliByAooId() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_FASCICOLI_BY_AOO_ID);
			pstmt.setInt(1, aooId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				fascicolo = getFascicolo(rs);
				fascicoli.add(fascicolo);
			}

		} catch (Exception e) {
			logger.error("Load getFascicoliByAooId by ID", e);
			throw new DataException("Cannot load FascicoliByAooId by ID");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return fascicoli;
	}

	public Collection<FascicoloVO> getStoriaFascicoliByDocumentoId(Connection connection,
			int documentoId, int versione) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Collection<FascicoloVO> fascicoli = new ArrayList<FascicoloVO>();
		FascicoloVO fascicolo = new FascicoloVO();
		try {
			if (connection == null) {
				logger
						.warn("getFascicoliByDocumentoId() - Invalid Connection :"
								+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection
					.prepareStatement(SELECT_STORIA_FASCICOLI_BY_DOCUMENTO_ID);
			pstmt.setInt(1, documentoId);
			pstmt.setInt(2, versione);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				fascicolo = getFascicolo(rs);
				fascicoli.add(fascicolo);
			}

		} catch (Exception e) {
			logger.error("Load getFascicoliByDocumentoId by ID", e);
			throw new DataException("Cannot load FascicoliByDocumentoId by ID");
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
			fascicolo.setRowCreatedUser(rs.getString("row_created_user"));
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

	private FascicoloVO getFascicoloProtocolloId(ResultSet rs)
			throws DataException {
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
	
	public FascicoloVO getFascicoloByCodice(String codice) throws DataException {
		FascicoloVO fascicoloVO = new FascicoloVO();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			fascicoloVO = getFascicoloByCodice(connection, codice);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " codice :" + codice);
		} finally {
			jdbcMan.close(connection);
		}
		return fascicoloVO;
	}

	public FascicoloVO getFascicoloById(int id) throws DataException {
		FascicoloVO fascicoloVO = new FascicoloVO();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			fascicoloVO = getFascicoloById(connection, id);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " FascicoloId :" + id);
		} finally {
			jdbcMan.close(connection);
		}
		return fascicoloVO;
	}
	
	public FascicoloVO getFascicoloByCodice(Connection connection, String codice)
			throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		FascicoloVO fascicolo = new FascicoloVO();
		try {
			if (connection == null) {
				logger.warn("getFascicoloByCodice() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_FASCICOLO_BY_CODICE);
			pstmt.setString(1, codice);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				fascicolo = getFascicolo(rs);
				fascicolo.setReturnValue(ReturnValues.FOUND);
			} else {
				fascicolo.setReturnValue(ReturnValues.NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Load Fascicolo by Codice", e);
			throw new DataException("Cannot load Fascicolo by Codice");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return fascicolo;
	}

	public FascicoloVO getFascicoloById(Connection connection, int id)
			throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		FascicoloVO fascicolo = new FascicoloVO();
		try {
			if (connection == null) {
				logger.warn("getFascicoloById() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_FASCICOLO_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				fascicolo = getFascicolo(rs);
				fascicolo.setReturnValue(ReturnValues.FOUND);
			} else {
				fascicolo.setReturnValue(ReturnValues.NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Load Fascicolo by ID", e);
			throw new DataException("Cannot load Fascicolo by ID");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return fascicolo;
	}

	public FascicoloVO getFascicoloByProgressivo(Connection connection,
			int anno, int progressivo) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		FascicoloVO fascicolo = new FascicoloVO();
		try {
			if (connection == null) {
				logger.warn("getFascicoloById() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection
					.prepareStatement(SELECT_FASCICOLO_BY_ANNO_NUMERO);
			pstmt.setString(1, String.valueOf(anno));
			pstmt.setInt(2, progressivo);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				fascicolo = getFascicolo(rs);
				fascicolo.setReturnValue(ReturnValues.FOUND);
			} else {
				fascicolo.setReturnValue(ReturnValues.NOT_FOUND);
			}

		} catch (Exception e) {
			logger.error("Load Fascicolo by ID", e);
			throw new DataException("Cannot load Fascicolo by ID");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return fascicolo;
	}

	public FascicoloView getFascicoloViewById(int id) throws DataException {
		FascicoloView view = new FascicoloView();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			view = getFascicoloViewById(connection, id);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " FascicoloId :" + id);
		} finally {
			jdbcMan.close(connection);
		}
		return view;
	}

	public FascicoloView getFascicoloViewById(Connection connection, int id)
			throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		FascicoloView fascicolo = null;
		try {
			if (connection == null) {
				logger.warn("getFascicoloById() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_FASCICOLO_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				fascicolo = new FascicoloView();
				fascicolo.setId(rs.getInt("fascicolo_id"));
				fascicolo.setNome(rs.getString("nome"));
				fascicolo.setUfficioIntestatarioId(rs
						.getInt("ufficio_intestatario_id"));
				fascicolo.setOggetto(rs.getString("oggetto"));
				if (rs.getDate("data_apertura") != null)
					fascicolo.setDataApertura(DateUtil.formattaData(rs.getDate(
							"data_apertura").getTime()));
				if (rs.getDate("data_chiusura") != null)
					fascicolo.setDataChiusura(DateUtil.formattaData(rs.getDate(
							"data_chiusura").getTime()));
				if (rs.getDate("data_evidenza") != null)
					fascicolo.setDataEvidenza(DateUtil.formattaData(rs.getDate(
							"data_evidenza").getTime()));
				fascicolo.setTitolarioId(rs.getInt("titolario_id"));
				fascicolo.setAnnoRiferimento(rs.getInt("anno_riferimento"));
				fascicolo.setProgressivo(rs.getInt("progressivo"));
				fascicolo.setStato(rs.getString("stato"));
				fascicolo.setVersione(rs.getInt("versione"));
				fascicolo.setPathProgressivo(rs.getString("path_progressivo"));
			}

		} catch (Exception e) {
			logger.error("Load Fascicolo by ID", e);
			throw new DataException("Cannot load Fascicolo by ID");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return fascicolo;
	}

	public Collection<FascicoloVO> getFascicoliByProtocolloId(Connection connection,
			int protocolloId) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Collection<FascicoloVO> fascicoli = new ArrayList<FascicoloVO>();
		FascicoloVO fascicolo = new FascicoloVO();
		try {
			if (connection == null) {
				logger
						.warn("getFascicoliByProtocolloId() - Invalid Connection :"
								+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection
					.prepareStatement(SELECT_FASCICOLI_BY_PROTOCOLLO_ID);
			pstmt.setInt(1, protocolloId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				fascicolo = getFascicoloProtocolloId(rs);
				fascicoli.add(fascicolo);
			}

		} catch (Exception e) {
			logger.error("Load getFascicoliByProtocolloId by ID", e);
			throw new DataException("Cannot load Fascicolo by ID");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return fascicoli;
	}

	public Collection<Integer> getCollegatiIdByFascicoloId(Connection connection, int id)
			throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Collection<Integer> fascicoli = new ArrayList<Integer>();
		// FascicoloVO fascicolo = new FascicoloVO();
		try {
			if (connection == null) {
				logger
						.warn("getFascicoliCollegatiById() - Invalid Connection :"
								+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection
					.prepareStatement(SELECT_FASCICOLI_COLLEGATI_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int collegatoId = rs.getInt(1);
				// fascicolo = getFascicoloProtocolloId(rs);
				fascicoli.add(collegatoId);
			}

		} catch (Exception e) {
			logger.error("Load getFascicoliCollegatiById by ID", e);
			throw new DataException("Cannot load Fascicolo by ID");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return fascicoli;
	}

	public Collection<Integer> getSottoFascicoliIdByFascicoloId(Connection connection,
			int id) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Collection<Integer> fascicoli = new ArrayList<Integer>();
		// FascicoloVO fascicolo = new FascicoloVO();
		try {
			if (connection == null) {
				logger
						.warn("getSottoFascicoliIdByFascicoloId() - Invalid Connection :"
								+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_SOTTO_FASCICOLI_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int collegatoId = rs.getInt(1);
				// fascicolo = getFascicoloProtocolloId(rs);
				fascicoli.add(collegatoId);
			}

		} catch (Exception e) {
			logger.error("Load getSottoFascicoliIdByFascicoloId by ID", e);
			throw new DataException("Cannot load Fascicolo by ID");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return fascicoli;
	}

	public Collection<FascicoloVO> getFascicoliByParentId(Connection connection,
			int fascicoloId) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Collection<FascicoloVO> fascicoli = new ArrayList<FascicoloVO>();
		FascicoloVO fascicolo = new FascicoloVO();
		try {
			if (connection == null) {
				logger.warn("getFascicoliByParentId() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_FASCICOLI_BY_PARENT_ID);
			pstmt.setInt(1, fascicoloId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				fascicolo = getFascicolo(rs);
				fascicoli.add(fascicolo);
			}

		} catch (Exception e) {
			logger.error("Load getFascicoliByParentId by ID", e);
			throw new DataException("Cannot load Fascicolo by ID");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return fascicoli;
	}

	public Collection<MigrazioneVO> getFascicoliAnnoNumero(Connection connection)
			throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Collection<MigrazioneVO> fascicoli = new ArrayList<MigrazioneVO>();
		try {
			if (connection == null) {
				logger.warn("getFascicoliAnnoNumero() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			String sql = "SELECT a.anno_fascicolo, max(a.progressivo) "
					+ "FROM(SELECT to_char(data_apertura,'YYYY') as anno_fascicolo ,progressivo  FROM fascicoli ) a "
					+ "GROUP by a.anno_fascicolo";
			pstmt = connection.prepareStatement(sql);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				MigrazioneVO migrazione = new MigrazioneVO();
				migrazione.setAnno(rs.getInt(1));
				migrazione.setNumero(rs.getInt(2));
				fascicoli.add(migrazione);
			}
		} catch (Exception e) {
			logger.error("Load getFascicoliAnnoNumero ", e);
			throw new DataException("Cannot load Fascicolo by ID");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return fascicoli;
	}

	public void salvaDocumentoFascicolo(Connection connection,
			FascicoloVO fascicolo, int documentoId, String utente, int ufficioProprietarioId)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("salvaDocumentoFascicolo - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			archiviaVersione(connection, fascicolo.getId().intValue());
			pstmt = connection.prepareStatement(INSERT_FASCICOLO_DOCUMENTO);
			pstmt.setInt(1, fascicolo.getId().intValue());
			pstmt.setInt(2, documentoId);
			pstmt.setString(3, utente);
			pstmt.setInt(4, fascicolo.getVersione() + 1);
			pstmt.setInt(5, ufficioProprietarioId);
			pstmt.executeUpdate();
			logger.debug("Inserito Documento Fascicolo:"
					+ fascicolo.getId().intValue() + "," + documentoId);
		} catch (Exception e) {
			logger.error("salvaDocumentoFascicolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public boolean aggiungiCollegamentoFascicolo(int id, int allaccioId,
			String utente) throws DataException {
		Connection connection = null;
		boolean updated = false;
		try {
			connection = jdbcMan.getConnection();
			updated = aggiungiCollegamentoFascicolo(connection, id, allaccioId,
					utente);
		} catch (Exception e) {
			logger.error("aggiungiCollegamentoFascicolo", e);
			throw new DataException(e.getMessage()
					+ " aggiungiCollegamentoFascicolo Fascicolo Id :" + id
					+ " Fascicolo da allacciare:" + allaccioId);
		} finally {
			jdbcMan.close(connection);
		}
		return updated;
	}

	public boolean aggiungiCollegamentoFascicolo(Connection connection, int id,
			int allaccioId, String utente) throws DataException {

		PreparedStatement pstmt = null;
		boolean updated = false;
		try {
			if (connection == null) {
				logger.warn("aggiungiCollegamentoFascicolo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(INSERT_FASCICOLO_ALLACCIO);
			pstmt.setInt(1, id);
			pstmt.setInt(2, allaccioId);
			pstmt.setString(3, utente);
			// pstmt.setInt(4, fascicolo.getVersione() + 1);
			pstmt.executeUpdate();
			logger.debug("Inserito Collegamento Fascicolo:" + id + ","
					+ allaccioId);
			updated = true;
		} catch (Exception e) {
			logger.error("aggiungiCollegamentoFascicolo", e);
			throw new DataException(e.getMessage()
					+ " aggiungiCollegamentoFascicolo Fascicolo Id :" + id
					+ " Fascicolo da allacciare:" + allaccioId);
		} finally {
			jdbcMan.close(pstmt);
		}
		return updated;
	}

	public boolean esisteCollegamento(Connection connection, int fascicoloId,
			int allaccioId) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean trovato = false;
		final String SELECT_ALLACCIO = "select count(fascicolo_id) FROM fascicolo_allacci "
				+ "WHERE fascicolo_id = ? and fascicolo_allacciato_id = ?";

		try {
			pstmt = connection.prepareStatement(SELECT_ALLACCIO);
			pstmt.setInt(1, fascicoloId);
			pstmt.setInt(2, allaccioId);
			rs = pstmt.executeQuery();
			rs.next();
			trovato = (rs.getInt(1) > 0);

		} catch (Exception e) {
			logger.error("esisteCollegamento", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return trovato;
	}

	public void salvaProtocolloFascicolo(Connection connection,
			int fascicoloId, int protocolloId, String utente, int ufficioProprietarioId,int versione)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("salvaProtocolloFascicolo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(INSERT_FASCICOLO_PROTOCOLLO);
			pstmt.setInt(1, fascicoloId);
			pstmt.setInt(2, protocolloId);
			pstmt.setInt(3, versione);
			pstmt.setString(4, utente);
			pstmt.setInt(5, ufficioProprietarioId);
			pstmt.executeUpdate();
			logger.debug("Inserito Protocollo Fascicolo:" + fascicoloId + ","
					+ protocolloId);
		} catch (Exception e) {
			logger.error("salvaProtocolloFascicolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void salvaProtocolloFascicolo(Connection connection,
			ProtocolloFascicoloVO protocollofascicoloVO) throws DataException {
		int fascicoloId = protocollofascicoloVO.getFascicoloId();
		int protocolloId = protocollofascicoloVO.getProtocolloId();
		int ufficioId = protocollofascicoloVO.getUfficioProprietarioId();
		String utente = protocollofascicoloVO.getRowCreatedUser();
		int versione = protocollofascicoloVO.getVersione();
		salvaProtocolloFascicolo(connection, fascicoloId, protocolloId, utente, ufficioId,
				versione);
	}

	public FascicoloVO aggiornaFascicolo(FascicoloVO fascicolo)
			throws DataException {
		FascicoloVO fascicoloSalvato = new FascicoloVO();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			fascicoloSalvato = aggiornaFascicolo(connection, fascicolo);
		} catch (Exception e) {
			throw new DataException(e.getMessage()
					+ " aggiornaFascicolo Fascicolo Id :"
					+ fascicolo.getId().intValue());
		} finally {
			jdbcMan.close(connection);
		}
		return fascicoloSalvato;
	}

	public FascicoloVO aggiornaFascicolo(Connection connection,
			FascicoloVO fascicolo) throws DataException {
		fascicolo.setReturnValue(ReturnValues.UNKNOWN);
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("aggiornaFascicolo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			int fascicoloId = fascicolo.getId().intValue();

			archiviaVersione(connection, fascicoloId);
			pstmt = connection.prepareStatement(UPDATE_FASCICOLO);

			int n = 0;
			pstmt.setInt(++n, fascicolo.getAnnoRiferimento()); // Anno
			pstmt.setString(++n, fascicolo.getCodice()); // codice
			pstmt.setString(++n, fascicolo.getDescrizione()); // descrizione
			pstmt.setString(++n, fascicolo.getNome()); // nome
			pstmt.setInt(++n, fascicolo.getTipoFascicolo()); // Tipo

			pstmt.setInt(++n, fascicolo.getUfficioResponsabileId());
			if (fascicolo.getCaricaResponsabileId() > 0) {
				pstmt.setInt(++n, fascicolo.getCaricaResponsabileId());
			} else {
				pstmt.setNull(++n, Types.INTEGER);
			}
			pstmt.setInt(++n, fascicolo.getUfficioIntestatarioId());
			if (fascicolo.getCaricaIntestatarioId() > 0) {
				pstmt.setInt(++n, fascicolo.getCaricaIntestatarioId());
			} else {
				pstmt.setNull(++n, Types.INTEGER);
			}
			// pstmt.setString(++n, fascicolo.getCollocazione()); //
			// collocazione
			pstmt.setString(++n, fascicolo.getNote()); // note
			pstmt.setString(++n, fascicolo.getOggetto()); // oggetto
			pstmt.setInt(++n, fascicolo.getStato()); // stato
			// titolario
			if (fascicolo.getTitolarioId() > 0) {
				pstmt.setInt(++n, fascicolo.getTitolarioId()); // utente
			} else {
				pstmt.setNull(++n, Types.INTEGER); // utente
			}
			if (fascicolo.getDataChiusura() != null) {
				pstmt.setDate(++n, new Date(fascicolo.getDataChiusura()
						.getTime())); // data_chiusura
			} else {
				pstmt.setNull(++n, Types.DATE);
			}
			if (fascicolo.getDataEvidenza() != null) {
				pstmt.setDate(++n, new Date(fascicolo.getDataEvidenza()
						.getTime())); // data_evidenza
			} else {
				pstmt.setNull(++n, Types.DATE);
			}
			if (fascicolo.getDataUltimoMovimento() != null) {
				pstmt.setDate(++n, new Date(fascicolo.getDataUltimoMovimento()
						.getTime())); // data_ultimo_movimento
			} else {
				pstmt.setNull(++n, Types.DATE);
			}
			if (fascicolo.getDataScarto() != null) {
				pstmt.setDate(++n,
						new Date(fascicolo.getDataScarto().getTime())); // data_ultimo_movimento
			} else {
				pstmt.setNull(++n, Types.DATE);
			}
			if (fascicolo.getDataCarico() != null) {
				pstmt.setDate(++n,
						new Date(fascicolo.getDataCarico().getTime())); // data_carico
			} else {
				pstmt.setNull(++n, Types.DATE);
			}
			pstmt.setNull(++n, Types.DATE);
			pstmt.setString(++n, fascicolo.getRowUpdatedUser()); // row_updated_user
			if (fascicolo.getRowUpdatedTime() != null) {
				pstmt.setDate(++n, new Date(fascicolo.getRowUpdatedTime()
						.getTime())); // RowUpdatedTime
			} else {
				pstmt.setNull(++n, Types.DATE);
			}
			// pstmt.setInt(++n, fascicolo.getVersione() + 1); // versione
			pstmt.setString(++n, fascicolo.getCollocazioneLabel1());
			pstmt.setString(++n, fascicolo.getCollocazioneLabel2());
			pstmt.setString(++n, fascicolo.getCollocazioneLabel3());
			pstmt.setString(++n, fascicolo.getCollocazioneLabel4());
			pstmt.setString(++n, fascicolo.getCollocazioneValore1());
			pstmt.setString(++n, fascicolo.getCollocazioneValore2());
			pstmt.setString(++n, fascicolo.getCollocazioneValore3());
			pstmt.setString(++n, fascicolo.getCollocazioneValore4());
			if (fascicolo.getCaricaIstruttoreId() > 0) {
				pstmt.setInt(++n, fascicolo.getCaricaIstruttoreId());
			} else {
				pstmt.setNull(++n, Types.INTEGER);
			}
			
			pstmt.setString(++n, fascicolo.getInteressato()); // interessato
			pstmt.setString(++n, fascicolo.getDelegato()); // delegato
			pstmt.setString(++n, fascicolo.getIndiInteressato()); // indi_interessato
			pstmt.setString(++n, fascicolo.getIndiDelegato()); // indi_delegato
			
			if (fascicolo.getParentId() > 0) {
				pstmt.setInt(++n, fascicolo.getParentId());
			} else {
				pstmt.setNull(++n, Types.INTEGER);
			}
			if (fascicolo.getGiorniMax() > 0) {
				pstmt.setInt(++n, fascicolo.getGiorniMax());
			} else {
				pstmt.setNull(++n, Types.INTEGER);
			}
			if (fascicolo.getGiorniAlert() > 0) {
				pstmt.setInt(++n, fascicolo.getGiorniAlert());
			} else {
				pstmt.setNull(++n, Types.INTEGER);
			}
			pstmt.setString(++n, fascicolo.getComune());
			pstmt.setString(++n, fascicolo.getCapitolo());

			pstmt.setInt(++n, fascicolo.getId().intValue()); // protocollo_id
						n = pstmt.executeUpdate();
			fascicolo = getFascicoloById(connection, fascicoloId);
			if (n == 1) {
				if (fascicolo.getReturnValue() == ReturnValues.FOUND) {
					fascicolo.setReturnValue(ReturnValues.SAVED);
					logger.debug("Fascicolo aggiornato:" + fascicoloId);
				} else {
					logger
							.warn("Errore nella lettura del FascicoloVO dopo l'aggiornamento! Id:"
									+ fascicoloId);
					throw new DataException(
							"Errore nell'aggiornamento del Fascicolo.");
				}
			} else {
				fascicolo.setReturnValue(ReturnValues.OLD_VERSION);
				throw new DataException("Errore nella gestione delle versioni");
			}

		} catch (SQLException e) {
			logger.error("Aggiorna Fascicolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return fascicolo;
	}

	public int deleteFascicolo(int fascicoloId) throws DataException {
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			return cancellaFascicolo(connection, fascicoloId);
		} catch (Exception e) {
			throw new DataException(e.getMessage()
					+ " deleteFascicolo Fascicolo Id :" + fascicoloId);
		} finally {
			jdbcMan.close(connection);
		}
	}

	public void deleteDocumentoFascicolo(int fascicoloId, int documentoId,
			int versione) throws DataException {
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			deleteDocumentoFascicolo(connection, fascicoloId, documentoId,
					versione);
		} catch (Exception e) {
			throw new DataException(e.getMessage()
					+ " deleteDocumentoFascicolo Fascicolo Id : " + fascicoloId
					+ " documento Id" + documentoId);
		} finally {
			jdbcMan.close(connection);
		}
	}

	public void deleteDocumentoFascicolo(Connection conn, int fascicoloId,
			int documentoId, int versione) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (conn == null) {
				logger.warn("deleteDocumentoFascicolo() - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			archiviaVersione(conn, fascicoloId);

			pstmt = conn.prepareStatement(DELETE_DOCUMENTO_FASCICOLO);
			pstmt.setInt(1, fascicoloId);
			pstmt.setInt(2, documentoId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("Error deleteDocumentoFascicolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void deleteFascicoliDocumento(Connection conn, int documentoId)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (conn == null) {
				logger.warn("deleteFascicoliDocumento() - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(DELETE_FASCICOLI_DOCUMENTO);
			pstmt.setInt(1, documentoId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("Error deleteFascicoliDocumento", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void cancellaCollegamento(Connection conn, int fascicoloId,
			int collegatoId) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (conn == null) {
				logger.warn("cancellaCollegamento() - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(DELETE_FASCICOLI_ALLACCI);
			pstmt.setInt(1, fascicoloId);
			pstmt.setInt(2, collegatoId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("Error deleteFascicoliDocumento", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void deleteFascicoliProtocollo(Connection conn, int protocolloId)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (conn == null) {
				logger
						.warn("deleteFascicoliProtocollo() - Invalid Connection :"
								+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(DELETE_FASCICOLI_PROTOCOLLO);
			pstmt.setInt(1, protocolloId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("Error deleteFascicoliProtocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void deleteProtocolloFascicolo(Connection conn, int fascicoloId,
			int protocolloId) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (conn == null) {
				logger
						.warn("deleteProtocolloFascicolo() - Invalid Connection :"
								+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(DELETE_PROTOCOLLO_FASCICOLO);
			pstmt.setInt(1, fascicoloId);
			pstmt.setInt(2, protocolloId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("Error deleteProtocolloFascicolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public Collection<FascicoloView> getFascicoli(Utente utente, String progressivo, int anno,
			String oggetto, String note, String stato, int titolarioId,
			java.util.Date dataAperturaDa, java.util.Date dataAperturaA,
			java.util.Date dataEvidenzaDa, java.util.Date dataEvidenzaA,
			int ufficioId, int referenteId, int istruttoreId, String interessatoDelegato,
			String comune, String capitolo, String collocazioneValore1, String collocazioneValore2, String collocazioneValore3, String collocazioneValore4) throws DataException {

		Collection<FascicoloView>  fascicoli = new ArrayList<FascicoloView>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int aooId = utente.getAreaOrganizzativa().getId().intValue();
		try {
			connection = jdbcMan.getConnection();
			Organizzazione org = Organizzazione.getInstance();
			Ufficio uff = org.getUfficio(utente.getUfficioVOInUso().getId()
					.intValue());
			String ufficiUtenti = uff.getListaUfficiDiscendentiIdStr(utente);
			StringBuffer strQuery = new StringBuffer();
			strQuery.append("SELECT f.* ");
			strQuery.append(" FROM fascicoli f");
			strQuery.append(" WHERE f.aoo_id = ?");

			if (ufficioId != 0) {
				if (!uff.getValueObject().getTipo().equals(
						UfficioVO.UFFICIO_CENTRALE)
						&& uff.getValueObject().getParentId() != 0) {
					strQuery.append(" AND (ufficio_responsabile_id IN (")
							.append(ufficiUtenti).append(
									") OR ufficio_intestatario_id IN (")
							.append(ufficiUtenti).append(")");

					strQuery
							.append(
									" OR EXISTS (SELECT ufficio_intestatario_id from storia_fascicoli S WHERE "
											+ "s.aoo_id = ? AND (s.ufficio_intestatario_id IN (")
							.append(ufficiUtenti).append(
									") OR s.ufficio_responsabile_id IN (")
							.append(ufficiUtenti).append(
									")) AND f.fascicolo_id =s.fascicolo_id ))");
				}
			}

			if (progressivo != null && !"".equals(progressivo)) {
				strQuery
						.append(" and path_progressivo = '" + progressivo + "'");
			}
			if (anno > 0) {
				strQuery.append(" and to_char(f.data_apertura,'YYYY') = '"
						+ String.valueOf(anno) + "'");
			}
			if (oggetto != null && !"".equals(oggetto)) {
				strQuery.append(" and Upper(oggetto) LIKE ?");
			}
			if (note != null && !"".equals(note)) {
				strQuery.append(" and Upper(note) LIKE ?");
			}
			if (!stato.equals("-1")) {
				strQuery.append(" and f.stato=" + stato);
			}
			if (titolarioId > 0) {
				strQuery.append(" and f.titolario_id=" + titolarioId);
			}
			if (dataAperturaDa != null) {
				strQuery.append(" and f.data_apertura >=?");
			}
			if (dataAperturaA != null) {
				strQuery.append(" and f.data_apertura <=?");
			}
			if (dataEvidenzaDa != null) {
				strQuery.append(" and f.data_evidenza >=?");
			}
			if (dataEvidenzaA != null) {
				strQuery.append(" and f.data_evidenza <=?");
			}
			if (comune != null && !"".equals(comune)) {
				strQuery.append(" and Upper(comune) LIKE ?");
			}
			if (capitolo != null && !"".equals(capitolo)) {
				strQuery.append(" and Upper(capitolo) LIKE ?");
			}
			if (collocazioneValore1 != null && !"".equals(collocazioneValore1)) {
				strQuery.append(" and Upper(collocazione_valore1) LIKE ?");
			}
			if (collocazioneValore2 != null && !"".equals(collocazioneValore2)) {
				strQuery.append(" and Upper(collocazione_valore2) LIKE ?");
			}
			
			if (collocazioneValore3 != null && !"".equals(collocazioneValore3)) {
				strQuery.append(" and Upper(collocazione_valore3) LIKE ?");
			}
			if (collocazioneValore4 != null && !"".equals(collocazioneValore4)) {
				strQuery.append(" and Upper(collocazione_valore4) LIKE ?");
			}
			if (ufficioId > 0) {
				strQuery.append(" and f.ufficio_intestatario_id=" + ufficioId);
			}
			if (referenteId > 0) {
				strQuery.append(" and carica_intestatario_id=" + referenteId);
			}
			if (istruttoreId > 0) {
				strQuery.append(" and carica_istruttore_id=" + istruttoreId);
			}
			if (interessatoDelegato != null) {
				strQuery.append(" and ( Upper(delegato) LIKE '%" + interessatoDelegato.toUpperCase()
						+ "%' OR Upper(interessato) LIKE '%" + interessatoDelegato.toUpperCase() + "%')");
			}
			strQuery
					.append(" ORDER BY f.titolario_id DESC,f.anno_riferimento DESC,f.progressivo DESC");
			pstmt = connection.prepareStatement(strQuery.toString());
			int n = 1;

			pstmt.setInt(n++, aooId);
			if (ufficioId != 0) {
				if (!uff.getValueObject().getTipo().equals(
						UfficioVO.UFFICIO_CENTRALE)
						&& uff.getValueObject().getParentId() != 0) {
					pstmt.setInt(n++, aooId);
				}
			}
			if (oggetto != null && !"".equals(oggetto)) {
				pstmt.setString(n++, "%" + oggetto.toUpperCase() + "%");
			}
			if (note != null && !"".equals(note)) {
				pstmt.setString(n++, note.toUpperCase() + "%");
			}

			if (dataAperturaDa != null) {
				pstmt.setDate(n++, new java.sql.Date(dataAperturaDa.getTime()));
			}

			if (dataAperturaA != null) {
				pstmt.setDate(n++, new java.sql.Date(dataAperturaA.getTime()));
			}
			if (dataEvidenzaDa != null) {
				pstmt.setDate(n++, new java.sql.Date(dataEvidenzaDa.getTime()));
			}

			if (dataEvidenzaA != null) {
				pstmt.setDate(n++, new java.sql.Date(dataEvidenzaA.getTime()));
			}
			if (comune != null && !"".equals(comune)) {
				pstmt.setString(n++, "%" + comune.toUpperCase() + "%");
			}
			if (capitolo != null && !"".equals(capitolo)) {
				pstmt.setString(n++, "%" + capitolo.toUpperCase() + "%");
			}
			if (collocazioneValore1 != null && !"".equals(collocazioneValore1)) {
				pstmt.setString(n++, "%" + collocazioneValore1.toUpperCase() + "%");
			}
			if (collocazioneValore2 != null && !"".equals(collocazioneValore2)) {
				pstmt.setString(n++, "%" + collocazioneValore2.toUpperCase() + "%");
			}
			if (collocazioneValore3 != null && !"".equals(collocazioneValore3)) {
				pstmt.setString(n++, "%" + collocazioneValore3.toUpperCase() + "%");
			}
			if (collocazioneValore4 != null && !"".equals(collocazioneValore4)) {
				pstmt.setString(n++, "%" + collocazioneValore4.toUpperCase() + "%");
			}
			logger.debug(strQuery);
			rs = pstmt.executeQuery();
			FascicoloView fascicolo;
			while (rs.next()) {
				fascicolo = new FascicoloView();
				fascicolo.setId(rs.getInt("fascicolo_id"));
				fascicolo.setTitolarioId(rs.getInt("titolario_id"));
				fascicolo.setNome(rs.getString("nome"));
				fascicolo.setOggetto(rs.getString("oggetto"));
				fascicolo.setNote(rs.getString("note"));
				fascicolo.setStato(rs.getString("stato"));
				if (rs.getDate("data_apertura") != null)
					fascicolo.setDataApertura(DateUtil.formattaData(rs.getDate(
							"data_apertura").getTime()));
				fascicolo.setAnnoRiferimento(rs.getInt("anno_riferimento"));
				fascicolo.setProgressivo(rs.getInt("progressivo"));
				fascicolo.setUfficioIntestatarioId(rs
						.getInt("ufficio_intestatario_id"));
				fascicolo.setCaricaIntestatarioId(rs
						.getInt("carica_intestatario_id"));
				fascicolo.setPathProgressivo(rs.getString("path_progressivo"));
				fascicoli.add(fascicolo);
			}
		} catch (Exception e) {
			logger.error("Load getFascicoli", e);
			throw new DataException("Cannot load getFascicoli");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return fascicoli;
	}

	public Map<Integer,FascicoloView>  getFascicoliPerDeposito(Utente utente,
			java.util.Date dataChiusuraDa, java.util.Date dataChiusuraA)
			throws DataException {
		Map<Integer,FascicoloView>  fascicoli = new HashMap<Integer,FascicoloView> ();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int aooId = utente.getAreaOrganizzativa().getId().intValue();
		try {
			connection = jdbcMan.getConnection();
			StringBuffer strQuery = new StringBuffer();
			strQuery.append("SELECT f.* ");
			strQuery.append(" FROM fascicoli f");
			strQuery.append(" where f.aoo_id = ? ");
			strQuery.append(" and f.stato=1 ");
			if (dataChiusuraDa != null) {
				strQuery.append(" and f.data_chiusura >=?");
			}
			if (dataChiusuraA != null) {
				strQuery.append(" and f.data_chiusura <=?");
			}
			strQuery
					.append(" ORDER BY f.titolario_id DESC,f.anno_riferimento DESC,f.progressivo DESC");
			pstmt = connection.prepareStatement(strQuery.toString());
			int n = 1;
			pstmt.setInt(n++, aooId);

			if (dataChiusuraDa != null) {
				pstmt.setDate(n++, new java.sql.Date(dataChiusuraDa.getTime()));
			}

			if (dataChiusuraA != null) {
				pstmt.setDate(n++, new java.sql.Date(dataChiusuraA.getTime()));
			}

			logger.debug(strQuery);
			rs = pstmt.executeQuery();
			FascicoloView fascicolo;
			while (rs.next()) {
				fascicolo = new FascicoloView();
				fascicolo.setId(rs.getInt("fascicolo_id"));
				fascicolo.setTitolarioId(rs.getInt("titolario_id"));
				fascicolo.setNome(rs.getString("nome"));
				fascicolo.setOggetto(rs.getString("oggetto"));
				fascicolo.setNote(rs.getString("note"));
				fascicolo.setStato(rs.getString("stato"));
				if (rs.getDate("data_apertura") != null)
					fascicolo.setDataApertura(DateUtil.formattaData(rs.getDate(
							"data_apertura").getTime()));
				if (rs.getDate("data_chiusura") != null)
					fascicolo.setDataChiusura(DateUtil.formattaData(rs.getDate(
							"data_chiusura").getTime()));
				fascicolo.setAnnoRiferimento(rs.getInt("anno_riferimento"));
				fascicolo.setProgressivo(rs.getInt("progressivo"));
				fascicolo.setUfficioIntestatarioId(rs
						.getInt("ufficio_intestatario_id"));
				fascicolo.setCaricaIntestatarioId(rs
						.getInt("carica_intestatario_id"));
				fascicolo.setPathProgressivo(rs.getString("path_progressivo"));
				fascicoli.put(fascicolo.getId(), fascicolo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Load getFascicoli", e);
			throw new DataException("Cannot load getFascicoli");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return fascicoli;
	}

	public Map<Integer,FascicoloView>  getFascicoliArchivioDeposito(Utente utente, int stato)
			throws DataException {
		Map<Integer,FascicoloView>  fascicoli = new HashMap<Integer,FascicoloView> ();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int aooId = utente.getAreaOrganizzativa().getId().intValue();
		try {
			connection = jdbcMan.getConnection();
			StringBuffer strQuery = new StringBuffer();
			strQuery.append("SELECT f.*");
			strQuery.append(" FROM fascicoli f");
			strQuery.append(" where f.aoo_id = ? ");
			strQuery.append(" and f.stato=" + stato);
			strQuery
					.append(" ORDER BY f.titolario_id DESC,f.anno_riferimento DESC,f.progressivo DESC");
			pstmt = connection.prepareStatement(strQuery.toString());
			int n = 1;
			pstmt.setInt(n++, aooId);
			logger.debug(strQuery);
			rs = pstmt.executeQuery();
			FascicoloView fascicolo;
			while (rs.next()) {
				fascicolo = new FascicoloView();
				fascicolo.setId(rs.getInt("fascicolo_id"));
				fascicolo.setTitolarioId(rs.getInt("titolario_id"));
				fascicolo.setNome(rs.getString("nome"));
				fascicolo.setOggetto(rs.getString("oggetto"));
				fascicolo.setNote(rs.getString("note"));
				fascicolo.setStato(rs.getString("stato"));
				if (rs.getDate("data_apertura") != null)
					fascicolo.setDataApertura(DateUtil.formattaData(rs.getDate(
							"data_apertura").getTime()));
				if (rs.getDate("data_chiusura") != null)
					fascicolo.setDataChiusura(DateUtil.formattaData(rs.getDate(
							"data_chiusura").getTime()));
				fascicolo.setAnnoRiferimento(rs.getInt("anno_riferimento"));
				fascicolo.setProgressivo(rs.getInt("progressivo"));
				fascicolo.setUfficioIntestatarioId(rs
						.getInt("ufficio_intestatario_id"));
				fascicolo.setCaricaIntestatarioId(rs
						.getInt("carica_intestatario_id"));
				fascicolo.setPathProgressivo(rs.getString("path_progressivo"));
				fascicoli.put(fascicolo.getId(), fascicolo);
			}
		} catch (Exception e) {
			logger.error("Load getFascicoli", e);
			throw new DataException("Cannot load getFascicoli");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return fascicoli;
	}

	public int contaFascicoli(Utente utente, String progressivo, int anno,
			String oggetto, String note, String stato, int titolarioId,
			java.util.Date dataAperturaDa, java.util.Date dataAperturaA,
			java.util.Date dataEvidenzaDa, java.util.Date dataEvidenzaA,
			int ufficioId, int referenteId, int istruttoreId, String interessatoDelegato,
			String comune, String capitolo,String collocazioneValore1, String collocazioneValore2, String collocazioneValore3, String collocazioneValore4) throws DataException {

		int numeroFascicoli = 0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			Organizzazione org = Organizzazione.getInstance();
			Ufficio uff = org.getUfficio(utente.getUfficioVOInUso().getId()
					.intValue());
			String ufficiUtenti = uff.getListaUfficiDiscendentiIdStr(utente);
			StringBuffer strQuery = new StringBuffer();
			strQuery
					.append("SELECT count(fascicolo_id) FROM fascicoli f WHERE f.aoo_id = ?");

			if (ufficioId != 0) {
				if (!uff.getValueObject().getTipo().equals(
						UfficioVO.UFFICIO_CENTRALE)
						&& uff.getValueObject().getParentId() != 0) {
					strQuery.append(" AND (ufficio_responsabile_id IN (")
							.append(ufficiUtenti).append(
									") OR ufficio_intestatario_id IN (")
							.append(ufficiUtenti).append(")");

					strQuery
							.append(
									" OR EXISTS (SELECT ufficio_intestatario_id from storia_fascicoli S WHERE "
											+ "s.aoo_id = ? AND (s.ufficio_intestatario_id IN (")
							.append(ufficiUtenti).append(
									") OR s.ufficio_responsabile_id IN (")
							.append(ufficiUtenti).append(
									")) AND f.fascicolo_id =s.fascicolo_id ))");
				}
			}
			if (progressivo != null && !"".equals(progressivo)) {
				strQuery
						.append(" and path_progressivo = '" + progressivo + "'");
			}
			if (anno > 0) {
				strQuery.append(" and to_char(data_apertura,'YYYY') = '"
						+ String.valueOf(anno) + "'");
			}
			if (oggetto != null && !"".equals(oggetto)) {
				strQuery.append(" and Upper(oggetto) LIKE ?");
			}
			if (note != null && !"".equals(note)) {
				strQuery.append(" and Upper(note) LIKE ?");
			}

			if (!stato.equals("-1")) {
				strQuery.append(" and stato=" + stato);
			}

			if (titolarioId > 0) {
				strQuery.append(" and titolario_id=" + titolarioId);
			}

			if (dataAperturaDa != null) {
				strQuery.append(" and data_apertura >=?");
			}

			if (dataAperturaA != null) {
				strQuery.append(" and data_apertura <=?");
			}

			if (dataEvidenzaDa != null) {
				strQuery.append(" and data_evidenza >=?");
			}

			if (dataEvidenzaA != null) {
				strQuery.append(" and data_evidenza <=?");
			}

			if (comune != null && !"".equals(comune)) {
				strQuery.append(" and Upper(comune) LIKE ?");
			}

			if (capitolo != null && !"".equals(capitolo)) {
				strQuery.append(" and Upper(capitolo) LIKE ?");
			}
			if (collocazioneValore1 != null && !"".equals(collocazioneValore1)) {
				strQuery.append(" and Upper(collocazione_valore1) LIKE ?");
			}
			if (collocazioneValore2 != null && !"".equals(collocazioneValore2)) {
				strQuery.append(" and Upper(collocazione_valore2) LIKE ?");
			}
			
			if (collocazioneValore3 != null && !"".equals(collocazioneValore3)) {
				strQuery.append(" and Upper(collocazione_valore3) LIKE ?");
			}
			if (collocazioneValore4 != null && !"".equals(collocazioneValore4)) {
				strQuery.append(" and Upper(collocazione_valore4) LIKE ?");
			}
			
			if (ufficioId > 0) {
				strQuery.append(" and ufficio_intestatario_id=" + ufficioId);
			}
			if (referenteId > 0) {
				strQuery.append(" and carica_intestatario_id=" + referenteId);
			}
			if (istruttoreId > 0) {
				strQuery.append(" and carica_istruttore_id=" + istruttoreId);
			}

			if (interessatoDelegato != null) {
				strQuery.append(" and ( Upper(delegato) LIKE '%" + interessatoDelegato.toUpperCase()
						+ "%' OR Upper(interessato) LIKE '%" + interessatoDelegato.toUpperCase() + "%')");
			}
			
			pstmt = connection.prepareStatement(strQuery.toString());
			int n = 1;
			pstmt.setInt(n++, utente.getAreaOrganizzativa().getId().intValue());
			if (ufficioId != 0) {
				if (!uff.getValueObject().getTipo().equals(
						UfficioVO.UFFICIO_CENTRALE)
						&& uff.getValueObject().getParentId() != 0) {
					pstmt.setInt(n++, utente.getAreaOrganizzativa().getId()
							.intValue());
				}
			}
			if (oggetto != null && !"".equals(oggetto)) {
				pstmt.setString(n++, "%" + oggetto.toUpperCase() + "%");
			}
			if (note != null && !"".equals(note)) {
				pstmt.setString(n++, note.toUpperCase() + "%");
			}

			if (dataAperturaDa != null) {
				pstmt.setDate(n++, new java.sql.Date(dataAperturaDa.getTime()));
			}

			if (dataAperturaA != null) {
				pstmt.setDate(n++, new java.sql.Date(dataAperturaA.getTime()));
			}
			if (dataEvidenzaDa != null) {
				pstmt.setDate(n++, new java.sql.Date(dataEvidenzaDa.getTime()));
			}

			if (dataEvidenzaA != null) {
				pstmt.setDate(n++, new java.sql.Date(dataEvidenzaA.getTime()));
			}
			if (comune != null && !"".equals(comune)) {
				pstmt.setString(n++, "%" + comune.toUpperCase() + "%");
			}
			if (capitolo != null && !"".equals(capitolo)) {
				pstmt.setString(n++, "%" + capitolo.toUpperCase() + "%");
			}
			if (collocazioneValore1 != null && !"".equals(collocazioneValore1)) {
				pstmt.setString(n++, "%" + collocazioneValore1.toUpperCase() + "%");
			}
			if (collocazioneValore2 != null && !"".equals(collocazioneValore2)) {
				pstmt.setString(n++, "%" + collocazioneValore2.toUpperCase() + "%");
			}
			if (collocazioneValore3 != null && !"".equals(collocazioneValore3)) {
				pstmt.setString(n++, "%" + collocazioneValore3.toUpperCase() + "%");
			}
			if (collocazioneValore4 != null && !"".equals(collocazioneValore4)) {
				pstmt.setString(n++, "%" + collocazioneValore4.toUpperCase() + "%");
			}
			// logger.info(strQuery);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				numeroFascicoli = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("Load contaFascicoli", e);
			throw new DataException("Cannot load contaFascicoli");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return numeroFascicoli;
	}

	public int contaFascicoliPerDeposito(Utente utente,
			java.util.Date dataChiusuraDa, java.util.Date dataChiusuraA)
			throws DataException {
		int numeroFascicoli = 0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			StringBuffer strQuery = new StringBuffer();
			strQuery
					.append("SELECT count(fascicolo_id) FROM fascicoli f WHERE f.aoo_id = ?");
			strQuery.append(" and stato=1");
			if (dataChiusuraDa != null) {
				strQuery.append(" and data_chiusura >=?");
			}
			if (dataChiusuraA != null) {
				strQuery.append(" and data_chiusura <=?");
			}
			pstmt = connection.prepareStatement(strQuery.toString());
			int n = 1;
			pstmt.setInt(n++, utente.getAreaOrganizzativa().getId().intValue());
			if (dataChiusuraDa != null) {
				pstmt.setDate(n++, new java.sql.Date(dataChiusuraDa.getTime()));
			}
			if (dataChiusuraA != null) {
				pstmt.setDate(n++, new java.sql.Date(dataChiusuraA.getTime()));
			}
			rs = pstmt.executeQuery();
			if (rs.next()) {
				numeroFascicoli = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Load contaFascicoliPerDeposito", e);
			throw new DataException("Cannot load contaFascicoliPerDeposito");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return numeroFascicoli;
	}

	public Collection<ProtocolloFascicoloView>  getProtocolliFascicolo(int fascicoloId, Utente utente)
			throws DataException {

		Collection<ProtocolloFascicoloView> protocolli = new ArrayList<ProtocolloFascicoloView>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			StringBuffer strQuery = new StringBuffer();
			strQuery
					.append("SELECT p.protocollo_id,p.nume_protocollo, p.text_oggetto, p.data_registrazione, p.documento_id, ");
			strQuery
					.append(" t.path_titolario, p.flag_tipo,  d.filename, d.row_created_time as data_documento, td.desc_tipo_documento");

			strQuery.append(" FROM protocolli p");
			strQuery
					.append(" JOIN fascicolo_protocolli f ON f.protocollo_id=p.protocollo_id");
			strQuery
					.append(" JOIN tipi_documento td ON td.tipo_documento_id=p.tipo_documento_id");
			strQuery
					.append(" JOIN titolario t ON p.titolario_id=t.titolario_id");
			strQuery
					.append(" LEFT JOIN documenti d ON d.documento_id=p.documento_id");

			strQuery.append(" where p.registro_id=? AND fascicolo_id=?");

			strQuery.append(" ORDER BY p.registro_anno_numero DESC");

			pstmt = connection.prepareStatement(strQuery.toString());
			pstmt.setInt(1, utente.getRegistroInUso());
			pstmt.setInt(2, fascicoloId);

			rs = pstmt.executeQuery();
			ProtocolloFascicoloView protocollo;
			while (rs.next()) {
				protocollo = new ProtocolloFascicoloView();
				protocollo.setProtocolloId(rs.getInt("protocollo_id"));
				protocollo.setTipo("P");
				protocollo
						.setTipoDocumento(rs.getString("desc_tipo_documento"));
				protocollo.setId(rs.getInt("documento_id"));
				protocollo.setNome(rs.getString("filename"));
				// protocollo.set
				if (rs.getDate("data_documento") != null) {
					protocollo.setDataDocumento(DateUtil.formattaData(rs
							.getDate("data_documento").getTime()));
				}
				protocollo.setArgomento(rs.getString("path_titolario"));
				protocollo.setOggetto(rs.getString("text_oggetto"));
				protocollo.setNumeroProtocollo(rs.getInt("nume_protocollo"));
				protocollo.setTipoProtocollo(rs.getString("flag_tipo"));
				if (rs.getDate("data_registrazione") != null) {
					protocollo.setDataProtocollo(DateUtil.formattaData(rs
							.getDate("data_registrazione").getTime()));
				}

				protocolli.add(protocollo);
			}
		} catch (Exception e) {
			logger.error("Load getProtocolliFascicolo", e);
			throw new DataException("Cannot load getProtocolliFascicolo");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}

		return protocolli;
	}

	public boolean isUfficioInProcedimentoAssegnatari(Integer fascicoloId,int ufficioId)throws DataException{
		boolean ret=false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			String query = "SELECT COUNT(ass.assegnatario_id) FROM procedimenti p LEFT JOIN procedimento_assegnatari ass ON (p.procedimento_id=ass.procedimento_id) WHERE p.fascicolo_id=? AND ass.ufficio_assegnatario_id=?";
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, fascicoloId);
			pstmt.setInt(2, ufficioId);
			rs = pstmt.executeQuery();
			rs.next();
			ret=rs.getInt(1)>0?true:false;
		} catch (Exception e) {
			logger.error("Load isUfficioInProcedimentoAssegnatari", e);
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}

		return ret;
	}

	public Collection<DocumentoFascicoloVO> getDocumentiFascicoloById(int documentoId)
			throws DataException {
		Collection<DocumentoFascicoloVO> documentiFascicolo = new ArrayList<DocumentoFascicoloVO>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			String strQuery = "SELECT *  FROM fascicolo_documenti "
					+ "WHERE fascicolo_id= ?";
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, documentoId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				DocumentoFascicoloVO vo=new DocumentoFascicoloVO();
				vo.setDocumentoId(rs.getInt("dfa_id"));
				vo.setFascicoloId(rs.getInt("fascicolo_id"));
				vo.setUfficioProprietarioId(rs.getInt("ufficio_proprietario_id"));
				documentiFascicolo.add(vo);
			}
		} catch (Exception e) {
			logger.error("Load getDocumentiFascicoloById", e);
			throw new DataException("Cannot load getDocumentiFascicoloById");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return documentiFascicolo;
	}

	public Collection<Integer> getFaldoniFascicoloById(int fascicoloId)
			throws DataException {
		Collection<Integer> faldoni = new ArrayList<Integer>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			String strQuery = "SELECT faldone_id  FROM faldone_fascicoli "
					+ "WHERE fascicolo_id= ?";
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, fascicoloId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				faldoni.add(new Integer(rs.getInt("faldone_id")));
			}
		} catch (Exception e) {
			logger.error("Load getFaldoniFascicoloById", e);
			throw new DataException("Cannot load getFaldoniFascicoloById");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return faldoni;
	}

	public Collection<Integer> getProcedimentiFascicoloById(int fascicoloId)
			throws DataException {
		Collection<Integer> procedimenti = new ArrayList<Integer>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			String strQuery = "SELECT procedimento_id  FROM procedimenti_fascicolo "
					+ "WHERE fascicolo_id= ?";
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, fascicoloId);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				procedimenti.add(new Integer(rs.getInt("procedimento_id")));
			}
		} catch (Exception e) {
			logger.error("Load getProcedimentiFascicoloById", e);
			throw new DataException("Cannot load getProcedimentiFascicoloById");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return procedimenti;
	}

	public int eliminaCodaInvioFascicolo(Connection connection, int fascicoloId)
			throws DataException {
		PreparedStatement pstmt = null;
		int n = 0;
		try {
			if (connection == null) {
				logger
						.warn("eliminaCodaInvioFascicolo() - Invalid Connection :"
								+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection
					.prepareStatement(DELETE_CODA_INVIO_FASCICOLI_DESTINATARI);
			pstmt.setInt(1, fascicoloId);
			n = pstmt.executeUpdate();
			pstmt.close();
			
			pstmt = connection.prepareStatement(DELETE_CODA_INVIO_FASCICOLI);
			pstmt.setInt(1, fascicoloId);
			n = pstmt.executeUpdate();
		} catch (SQLException e) {
			logger.error("eliminaCodaInvioFascicolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return n;
	}

	public int scartaFascicolo(Connection connection, int fascicoloId,
			String destinazioneScarto, String userName, int versione)
			throws DataException {
		PreparedStatement pstmt = null;
		int n = 0;
		try {
			if (connection == null) {
				logger.warn("scartaFascicolo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			Date dataCorrente = new Date(System.currentTimeMillis());
			// archiviaVersione(connection, fascicoloId, versione,
			// dataCorrente);
			archiviaVersione(connection, fascicoloId);

			pstmt = connection.prepareStatement(SCARTA_FASCICOLO);
			pstmt.setInt(1, Parametri.STATO_FASCICOLO_SCARTATO);
			pstmt.setTimestamp(2, new Timestamp(dataCorrente.getTime())); // data
			pstmt.setTimestamp(3, new Timestamp(dataCorrente.getTime())); // data
			pstmt.setString(4, ""); // row_updated_user
			pstmt.setTimestamp(5, new Timestamp(dataCorrente.getTime())); // row_updated_time
			pstmt.setInt(6, fascicoloId);
			n = pstmt.executeUpdate();

		} catch (SQLException e) {
			logger.error("Aggiorna Fascicolo: scartaFascicolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return n;

	}

	public int aggiornaStatoFascicolo(Connection connection, int fascicoloId,
			int stato, String userName, int versione) throws DataException {
		PreparedStatement pstmt = null;
		int n = 0;
		try {
			if (connection == null) {
				logger.warn("aggiornaStatoFascicolo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			Date dataCorrente = new Date(System.currentTimeMillis());
			// archiviaVersione(connection, fascicoloId, versione +
			// 1,dataCorrente);
			archiviaVersione(connection, fascicoloId);
			pstmt = connection.prepareStatement(UPDATE_STATO_FASCICOLO);
			pstmt.setInt(1, stato);
			pstmt.setTimestamp(2, new Timestamp(dataCorrente.getTime())); // ultimo
			pstmt.setTimestamp(3, new Timestamp(dataCorrente.getTime())); // data
			pstmt.setString(4, ""); // row_updated_user
			pstmt.setTimestamp(5, new Timestamp(dataCorrente.getTime())); // row_updated_time
			pstmt.setInt(6, fascicoloId);
			n = pstmt.executeUpdate();

		} catch (SQLException e) {
			logger.error("Aggiorna Fascicolo: aggiornaStatoFascicolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return n;
	}

	public void salvaDocumentiInvioFascicolo(Connection connection,
			InvioFascicoliVO ifVO) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger
						.warn("salvaDocumentiInvioFascicolo() - Invalid Connection :"
								+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(INVIO_FASCICOLI);
			pstmt.setInt(1, ifVO.getId().intValue());
			pstmt.setInt(2, ifVO.getFascicoloId());
			pstmt.setInt(3, ifVO.getDocumentoId());
			pstmt.setString(4, ifVO.getFlagDocumentoPrincipale());
			pstmt.setInt(5, ifVO.getAooId());
			pstmt.setInt(6, ifVO.getUfficioMittenteId());
			pstmt.setInt(7, ifVO.getUtenteMittenteId());
			pstmt.executeUpdate();
			logger.debug("Invio Documento Fascicolo:" + ifVO.getFascicoloId()
					+ "," + ifVO.getDocumentoId());

		} catch (Exception e) {
			logger.error("salvaDocumentiInvioFascicolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void salvaDestinatariInvioFascicolo(Connection connection,
			InvioFascicoliDestinatariVO ifdVO) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger
						.warn("salvaDestinatariInvioFascicolo() - Invalid Connection :"
								+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(INVIO_FASCICOLI_DESTINATARI);
			pstmt.setInt(1, ifdVO.getId().intValue());
			pstmt.setInt(2, ifdVO.getFascicoloId());
			DestinatarioVO d = ifdVO.getDestinatario();
			pstmt.setString(3, d.getFlagTipoDestinatario());
			pstmt.setString(4, d.getIndirizzo());
			pstmt.setString(5, d.getEmail());
			pstmt.setString(6, d.getDestinatario());
			pstmt.setInt(7, d.getMezzoSpedizioneId());
			pstmt.setString(8, d.getCitta());
			if (d.getDataSpedizione() != null) {
				pstmt.setDate(9, new Date(d.getDataSpedizione().getTime())); // data
			} else {
				pstmt.setNull(9, Types.DATE);
			}

			pstmt.setString(10, d.getFlagConoscenza() ? "1" : "0");
			if (d.getDataEffettivaSpedizione() != null) {
				pstmt.setDate(11, new Date(d.getDataEffettivaSpedizione()
						.getTime())); // data spedizione
			} else {
				pstmt.setNull(11, Types.DATE);
			}
			pstmt.setInt(12, d.getVersione());
			pstmt.executeUpdate();
			logger.debug("salvaDestinatariInvioFascicolo:"
					+ ifdVO.getFascicoloId() + "," + d.getId());

		} catch (Exception e) {
			logger.error("salvaDestinatariInvioFascicolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void annullaInvioFascicolo(int fascicoloId, int versione)
			throws DataException {
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			annullaInvioFascicolo(connection, fascicoloId, versione);
		} catch (Exception e) {
			throw new DataException(e.getMessage()
					+ " annullaInvioFascicolo Fascicolo Id :" + fascicoloId);
		} finally {
			jdbcMan.close(connection);
		}
	}

	public void annullaInvioFascicolo(Connection conn, int fascicoloId,
			int versione) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (conn == null) {
				logger.warn("annullaInvioFascicolo() - Invalid Connection :"
						+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(DELETE_DESTINATARI_INVIO_FASCICOLI);
			pstmt.setInt(1, fascicoloId);
			pstmt.executeUpdate();
			pstmt.close();
			
			pstmt = conn.prepareStatement(DELETE_DOCUMENTI_INVIO_FASCICOLI);
			pstmt.setInt(1, fascicoloId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("Error annullaInvioFascicolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public SortedMap<Integer,FascicoloInvioView> getFascicoliArchivioInvio(int aooId) throws DataException {

		SortedMap<Integer,FascicoloInvioView> fascicoli = new TreeMap<Integer,FascicoloInvioView>(new Comparator<Integer>() {
			public int compare(Integer i1, Integer i2) {
				return i2.intValue() - i1.intValue();
			}
		});
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_FASCICOLI_INVIO);
			pstmt.setInt(1, aooId);

			rs = pstmt.executeQuery();
			FascicoloInvioView fascicolo = null;
			StringBuffer documenti = null;
			while (rs.next()) {
				if (fascicolo == null
						|| fascicolo.getId() != rs.getInt("fascicolo_id")) {
					if (fascicolo != null) {
						fascicolo.setDocumenti(documenti.toString());
					}

					documenti = new StringBuffer();
					fascicolo = new FascicoloInvioView();
					fascicolo.setId(rs.getInt("fascicolo_id"));
					fascicolo.setDataApertura(DateUtil.formattaData(rs.getDate(
							"data_apertura").getTime()));
					fascicolo.setAnnoRiferimento(rs.getInt("anno_riferimento"));
					fascicolo.setProgressivo(rs.getInt("progressivo"));
					fascicolo.setOggetto(rs.getString("oggetto"));

					fascicoli.put(new Integer(fascicolo.getId()), fascicolo);

				}
				documenti.append(rs.getString("nomefile") + "<br>");
			}
			if (fascicolo != null) {
				fascicolo.setDocumenti(documenti.toString());
			}

		} catch (Exception e) {
			logger.error("Load getFascicoli", e);
			throw new DataException("Cannot load getFascicoli");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return fascicoli;
	}

	public Collection<InvioFascicoliVO> getDocumentiFascicoliInvio(int fascicoloId)
			throws DataException {

		Collection<InvioFascicoliVO> documenti = new ArrayList<InvioFascicoliVO>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_DOCUMENTI_FASCICOLO);
			pstmt.setInt(1, fascicoloId);

			rs = pstmt.executeQuery();
			InvioFascicoliVO ifVO = null;
			while (rs.next()) {
				ifVO = new InvioFascicoliVO();
				ifVO.setId(rs.getInt("fascicolo_id"));
				ifVO.setDocumentoId(rs.getInt("dfa_id"));
				ifVO.setFlagDocumentoPrincipale(rs
						.getString("flag_documento_principale"));
				ifVO.setFascicoloId(rs.getInt("fascicolo_id"));
				ifVO.setUfficioMittenteId(rs.getInt("ufficio_mittente_id"));
				ifVO.setUtenteMittenteId(rs.getInt("utente_mittente_id"));
				documenti.add(ifVO);
			}

		} catch (Exception e) {
			logger.error("Load getDocumentiFascicoliInvio", e);
			throw new DataException("Cannot load getDocumentiFascicoliInvio");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return documenti;
	}

	public Map<String,DestinatarioVO> getDestinatariFascicoliInvio(int fascicoloId)
			throws DataException {
		HashMap<String,DestinatarioVO> destinatari = new HashMap<String,DestinatarioVO>();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			destinatari = (HashMap<String,DestinatarioVO>) getDestinatariFascicoliInvio(connection,
					fascicoloId);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " FascicoloId :"
					+ fascicoloId);
		} finally {
			jdbcMan.close(connection);
		}
		return destinatari;
	}

	public Map<String,DestinatarioVO> getDestinatariFascicoliInvio(Connection connection,
			int fascicoloId) throws DataException {

		HashMap<String,DestinatarioVO> destinatari = new HashMap<String,DestinatarioVO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(SELECT_DESTINATARI_FASCICOLO);
			pstmt.setInt(1, fascicoloId);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				DestinatarioVO destinatarioVO = new DestinatarioVO();
				destinatarioVO.setFlagTipoDestinatario(rs
						.getString("flag_tipo_destinatario"));
				destinatarioVO.setIndirizzo(rs.getString("indirizzo"));
				destinatarioVO.setEmail(rs.getString("email"));
				destinatarioVO.setDestinatario(rs.getString("DESTINATARIO"));
				destinatarioVO.setMezzoSpedizioneId(rs
						.getInt("mezzo_spedizione"));
				destinatarioVO.setCitta(rs.getString("citta"));
				destinatarioVO.setDataSpedizione(rs.getDate("data_spedizione"));
				destinatarioVO.setFlagConoscenza("1".equals(rs
						.getString("flag_conoscenza")));
				destinatarioVO.setDataEffettivaSpedizione(rs
						.getDate("data_effettiva_spedizione"));
				destinatarioVO.setVersione(rs.getInt("versione"));
				destinatari.put(destinatarioVO.getDestinatario(),
						destinatarioVO);
			}

		} catch (Exception e) {
			logger.error("Load getDestinatariFascicoliInvio", e);
			throw new DataException("Cannot load getDestinatariFascicoliInvio");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return destinatari;
	}

	public boolean esisteFascicoloInCodaInvio(int fascicoloId)
			throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int total = 0;
		boolean esiste = false;
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			String strQuery = "SELECT COUNT(fascicolo_id) FROM invio_fascicoli WHERE fascicolo_id=?";
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, fascicoloId);
			rs = pstmt.executeQuery();
			rs.next();
			total = rs.getInt(1);
			if (total > 0)
				esiste = true;

		} catch (Exception e) {
			logger.error("Load esisteFascicoloInCodaInvio by ID", e);
			throw new DataException(
					"Cannot load esisteFascicoloInCodaInvio by ID");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return esiste;
	}

	public int getMaxProgrFascicolo(Connection connection, int aooId, int anno)
			throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("getMaxProgrFascicolo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_ULTIMO_PROGRESSIVO);
			pstmt.setInt(1, aooId);
			pstmt.setString(2, String.valueOf(anno));
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) + 1;
			} else
				return 1;
		} catch (Exception e) {
			logger.error("getMaxProgrFascicolo", e);
			throw new DataException("Cannot load getMaxProgrFascicolo");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
	}

	public Collection<ProtocolloFascicoloVO> getProtocolliFascicoloById(int fascicoloId)
			throws DataException {
		Collection<ProtocolloFascicoloVO> fascicoloProtocolli = new ArrayList<ProtocolloFascicoloVO>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			String strQuery = "SELECT * FROM fascicolo_protocolli fp"
					+ " JOIN protocolli p ON fp.protocollo_id=p.protocollo_id"
					+ " WHERE fp.fascicolo_id= ? order by p.registro_anno_numero DESC";
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, fascicoloId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ProtocolloFascicoloVO vo=new ProtocolloFascicoloVO();
				vo.setProtocolloId(rs.getInt("protocollo_id"));
				vo.setFascicoloId(rs.getInt("fascicolo_id"));
				vo.setUfficioProprietarioId(rs.getInt("ufficio_proprietario_id"));
				fascicoloProtocolli.add(vo);
			}
		} catch (Exception e) {
			logger.error("Load getProtocolliFascicoloById", e);
			throw new DataException("Cannot load getProtocolliFascicoloById");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return fascicoloProtocolli;
	}

	public Collection<Integer> getProtocolloFascicoloByIdAndNumeroProtocollo(
			int fascicoloId, int numProt) throws DataException {
		Collection<Integer> protocolli = new ArrayList<Integer>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			String strQuery = "SELECT fp.protocollo_id FROM fascicolo_protocolli fp"
					+ " JOIN protocolli p ON fp.protocollo_id=p.protocollo_id"
					+ " WHERE fp.fascicolo_id= ? AND p.nume_protocollo=? order by p.registro_anno_numero DESC";
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, fascicoloId);
			pstmt.setInt(2, numProt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				protocolli.add(new Integer(rs.getInt("protocollo_id")));
			}
		} catch (Exception e) {
			logger.error("Load getProtocolliFascicoloById", e);
			throw new DataException("Cannot load getProtocolliFascicoloById");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return protocolli;
	}

	private void archiviaVersione(Connection connection, int fascicoloId)
			throws DataException {
		String[] tables = { "fascicoli", "fascicolo_documenti",
				"fascicolo_protocolli" };
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("storia Fascicoli- Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			for (int i = 0; i < tables.length; i++) {
				String sql = "INSERT INTO storia_" + tables[i]
						+ " SELECT * FROM " + tables[i]
						+ " WHERE fascicolo_id = ?";

				pstmt = connection.prepareStatement(sql);
				pstmt.setInt(1, fascicoloId);
				int r = pstmt.executeUpdate();
				logger.debug(sql + "record inseriti in storia_" + tables[i]
						+ ": " + r);

				jdbcMan.close(pstmt);
				sql = "UPDATE " + tables[i] + " SET versione = versione+1"
						+ " WHERE fascicolo_id = ?";
				pstmt = connection.prepareStatement(sql);
				pstmt.setInt(1, fascicoloId);
				pstmt.executeUpdate();
			}

		} catch (Exception e) {
			logger.error("storia fascicolo" + fascicoloId, e);
			throw new DataException("Cannot insert Storia Fascicolo");

		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public int cancellaFascicolo(Connection connection, int fascicoloId)
			throws DataException {
		String[] tables = { "fascicolo_allacci","storia_fascicolo_protocolli",
				"storia_fascicolo_documenti", "storia_procedimenti_fascicolo",
				"storia_faldone_fascicoli", "storia_fascicoli", "fascicoli" };
		int returnValue = ReturnValues.UNKNOWN;
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("cancellazione Fascicolo- Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			if (isCancellabileFascicolo(connection, fascicoloId)) {
				for (int i = 0; i < tables.length; i++) {
					String sql = "DELETE FROM  " + tables[i]
							+ " WHERE fascicolo_id = ?";

					pstmt = connection.prepareStatement(sql);
					pstmt.setInt(1, fascicoloId);
					int r = pstmt.executeUpdate();
					logger.debug(sql + "record CANCELLATI: " + r);
					jdbcMan.close(pstmt);
				}
				returnValue = ReturnValues.SAVED;
			} else {
				returnValue = ReturnValues.NOT_SAVED;
			}
		} catch (Exception e) {
			logger.error("cancellazione fascicolo" + fascicoloId, e);
			throw new DataException("Cannot delete Fascicolo");

		} finally {
			jdbcMan.close(pstmt);
		}
		return returnValue;
	}

	private boolean isCancellabileFascicolo(Connection connection,
			int fascicoloId) throws DataException {
		String[] tables = { "fascicolo_protocolli", "fascicolo_documenti",
				"procedimenti_fascicolo", "faldone_fascicoli" };
		boolean cancellabile = true;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("isCancellabileFascicolo- Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			for (int i = 0; i < tables.length; i++) {
				String strQuery = "select count(fascicolo_id) FROM  "
						+ tables[i] + " WHERE fascicolo_id = ?";
				pstmt = connection.prepareStatement(strQuery);
				pstmt.setInt(1, fascicoloId);
				rs = pstmt.executeQuery();
				rs.next();
				if (rs.getInt(1) > 0) {
					cancellabile = false;
					break;
				}
			}
			if (cancellabile) {
				pstmt.close();
				String strQuery = "select count(fascicolo_id) FROM fascicoli WHERE parent_id = ?";
				pstmt = connection.prepareStatement(strQuery);
				pstmt.setInt(1, fascicoloId);
				rs = pstmt.executeQuery();
				rs.next();
				if (rs.getInt(1) > 0) {
					cancellabile = false;
				}
			}

		} catch (Exception e) {
			logger.error("cancellazione fascicolo" + fascicoloId, e);
			throw new DataException("Cannot delete Fascicolo");

		} finally {
			jdbcMan.close(pstmt);
		}
		return cancellabile;
	}

	public Collection<FascicoloView> getStoriaFascicolo(int fascicoloId) throws DataException {
		ArrayList<FascicoloView> storiaFascicolo = new ArrayList<FascicoloView>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sqlStoriaFascicolo = "SELECT * FROM STORIA_FASCICOLI WHERE"
					+ " fascicolo_id=? ORDER BY VERSIONE desc";
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(sqlStoriaFascicolo);
			pstmt.setInt(1, fascicoloId);

			rs = pstmt.executeQuery();
			FascicoloView fascicolo;
			while (rs.next()) {
				fascicolo = new FascicoloView();
				fascicolo.setId(rs.getInt("fascicolo_id"));
				fascicolo.setTitolarioId(rs.getInt("titolario_id"));
				fascicolo.setOggetto(rs.getString("oggetto"));
				fascicolo.setStato(rs.getString("stato"));
				fascicolo.setDataApertura(DateUtil.formattaData(rs.getDate(
						"data_apertura").getTime()));
				if (rs.getDate("data_carico") != null)
					fascicolo.setDataCarico(DateUtil.formattaData(rs.getDate(
							"data_carico").getTime()));
				if (rs.getDate("data_scarico") != null)
					fascicolo.setDataScarico(DateUtil.formattaData(rs.getDate(
							"data_scarico").getTime()));
				if (rs.getDate("data_evidenza") != null)
					fascicolo.setDataEvidenza(DateUtil.formattaData(rs.getDate(
							"data_evidenza").getTime()));

				fascicolo.setAnnoRiferimento(rs.getInt("anno_riferimento"));
				fascicolo.setProgressivo(rs.getInt("progressivo"));
				fascicolo.setUfficioIntestatarioId(rs
						.getInt("ufficio_intestatario_id"));

				fascicolo.setUfficioResponsabileId(rs
						.getInt("ufficio_responsabile_id"));
				fascicolo.setCaricaResponsabileId(rs
						.getInt("carica_responsabile_id"));
				fascicolo.setVersione(rs.getInt("versione"));
				if (rs.getDate("data_ultimo_movimento") != null)
					fascicolo.setDataUltimoMovimento(DateUtil.formattaData(rs
							.getDate("data_ultimo_movimento").getTime()));
				fascicolo.setPathProgressivo(rs.getString("path_progressivo"));
				storiaFascicolo.add(fascicolo);

			}
		} catch (Exception e) {
			logger.error("getStoriaFascicolo", e);
			throw new DataException("Cannot load getStoriaFascicolo");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return storiaFascicolo;

	}

	public void deleteFascicoloProtocollo(Connection conn, int protocolloId,
			int fascicoloId) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (conn == null) {
				logger
						.warn("deleteFascicoloProtocollo() - Invalid Connection :"
								+ conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(DELETE_FASCICOLO_PROTOCOLLO);
			pstmt.setInt(1, fascicoloId);
			pstmt.setInt(2, protocolloId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("Error deleteFascicoloProtocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public FascicoloVO getFascicoloByIdVersione(int id, int versione)
			throws DataException {
		FascicoloVO fascicoloVO = new FascicoloVO();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			fascicoloVO = getFascicoloByIdVersione(connection, id, versione);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " FascicoloId :" + id
					+ " Versione :" + versione);
		} finally {
			jdbcMan.close(connection);
		}
		return fascicoloVO;
	}

	public FascicoloVO getFascicoloByIdVersione(Connection connection, int id,
			int versione) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		FascicoloVO fascicolo = new FascicoloVO();
		try {
			if (connection == null) {
				logger.warn("getFascicoloByIdVersione() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection
					.prepareStatement(SELECT_FASCICOLO_BY_ID_VERSIONE);
			pstmt.setInt(1, id);
			pstmt.setInt(2, versione);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				fascicolo = getFascicolo(rs);
				fascicolo.setReturnValue(ReturnValues.FOUND);
			} else {
				fascicolo.setReturnValue(ReturnValues.NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("Load Versione Fascicolo by ID", e);
			throw new DataException("Cannot load Versione Fascicolo by ID");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return fascicolo;
	}

	public Collection<Integer> getProtocolliFascicoloByIdVersione(int fascicoloId,
			int versione) throws DataException {
		Collection<Integer> protocolli = new ArrayList<Integer>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			String strQuery = "SELECT protocollo_id  FROM storia_fascicolo_protocolli "
					+ "WHERE fascicolo_id= ? and versione =?";
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, fascicoloId);
			pstmt.setInt(2, versione);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				protocolli.add(new Integer(rs.getInt("protocollo_id")));
			}
		} catch (Exception e) {
			logger.error("Load getProtocolliFascicoloById", e);
			throw new DataException("Cannot load getProtocolliFascicoloById");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return protocolli;
	}

	public Collection<Integer> getDocumentiFascicoloByIdVersione(int documentoId,
			int versione) throws DataException {

		Collection<Integer> documenti = new ArrayList<Integer>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			String strQuery = "SELECT dfa_id  FROM storia_fascicolo_documenti "
					+ "WHERE fascicolo_id= ? AND versione=?";
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, documentoId);
			pstmt.setInt(2, versione);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				documenti.add(new Integer(rs.getInt("dfa_id")));
			}
		} catch (Exception e) {
			logger.error("Load getDocumentiFascicoloByIdVersione", e);
			throw new DataException(
					"Cannot load getDocumentiFascicoloByIdVersione");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return documenti;
	}

	public Collection<Integer> getProcedimentiFascicoloByIdVersione(int fascicoloId,
			int versione) throws DataException {

		Collection<Integer> proc = new ArrayList<Integer>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			String strQuery = "SELECT procedimento_id  FROM storia_procedimenti_fascicolo "
					+ "WHERE fascicolo_id= ? AND versione=?";
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, fascicoloId);
			pstmt.setInt(2, versione);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				proc.add(new Integer(rs.getInt("procedimento_id")));
			}
		} catch (Exception e) {
			logger.error("Load getProcedimentiFascicoloByIdVersione", e);
			throw new DataException(
					"Cannot load getProcedimentiFascicoloByIdVersione");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return proc;
	}

	public Collection<Integer> getFaldoniFascicoloByIdVersione(int fascicoloId,
			int versione) throws DataException {

		Collection<Integer> faldoni = new ArrayList<Integer>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			String strQuery = "SELECT faldone_id  FROM storia_faldone_fascicoli "
					+ "WHERE fascicolo_id= ? AND versione=?";
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, fascicoloId);
			pstmt.setInt(2, versione);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				faldoni.add(new Integer(rs.getInt("faldone_id")));
			}
		} catch (Exception e) {
			logger.error("Load getFaldoniFascicoloByIdVersione", e);
			throw new DataException(
					"Cannot load getFaldoniFascicoloByIdVersione");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return faldoni;
	}

	public boolean isUtenteAbilitatoView(Utente utente, Ufficio uff,
			int fascicoloId) throws DataException {

		boolean abilitato = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			String ufficiUtenti = uff.getListaUfficiDiscendentiIdStr(utente);
			StringBuffer strQuery = new StringBuffer();
			strQuery
					.append("SELECT count(fascicolo_id) FROM fascicoli f WHERE aoo_id = ?");

			strQuery.append(" AND fascicolo_id=").append(fascicoloId);
			if (!uff.getValueObject().getTipo().equals(UfficioVO.UFFICIO_CENTRALE)
					&& uff.getValueObject().getParentId() != 0) {

				strQuery.append(" AND (ufficio_responsabile_id IN (");
				strQuery.append(ufficiUtenti);
				strQuery.append(") OR ufficio_intestatario_id IN (");
				strQuery.append(ufficiUtenti).append("))");
				strQuery.append(" OR EXISTS (SELECT ufficio_intestatario_id from storia_fascicoli S WHERE "
								+ " s.fascicolo_id=" + fascicoloId+ " AND s.fascicolo_id=f.fascicolo_id "
								+ " AND s.ufficio_intestatario_id IN (").append(ufficiUtenti).append("))");
			}

			pstmt = connection.prepareStatement(strQuery.toString());
			pstmt.setInt(1, utente.getAreaOrganizzativa().getId().intValue());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				abilitato = (rs.getInt(1) > 0);
			}
		} catch (Exception e) {
			logger.error("Load isUtenteAbilitatoView", e);
			throw new DataException("Cannot load isUtenteAbilitatoView");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return abilitato;
	}
	
	public TipoVisibilitaUfficioEnum getVisibilitaUfficioFascicolo(int ufficioId,
			int fascicoloId) throws DataException {

		TipoVisibilitaUfficioEnum visibilita = TipoVisibilitaUfficioEnum.NESSUNA;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			StringBuffer strQuery = new StringBuffer();
			strQuery.append("SELECT flag_visibilita FROM procedimento_uffici_partecipanti puv LEFT JOIN procedimenti p ON (puv.procedimento_id=p.procedimento_id) WHERE p.fascicolo_id=? AND puv.ufficio_id=?");
			pstmt = connection.prepareStatement(strQuery.toString());
			pstmt.setInt(1, fascicoloId);
			pstmt.setInt(2, ufficioId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				visibilita = (rs.getInt(1)==1?TipoVisibilitaUfficioEnum.COMPLETA:TipoVisibilitaUfficioEnum.PARZIALE);
			}
			
		} catch (Exception e) {
			logger.error("Load getVisibilitaUfficioFascicolo", e);
			throw new DataException("Cannot load getVisibilitaUfficioFascicolo");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return visibilita;
	}
	
	public boolean isUfficioAbilitatoSuFascicoloProtocollo(int ufficioId,
			int fascicoloId, int protocolloId) throws DataException {

		boolean abilitato = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			StringBuffer strQuery = new StringBuffer();
			strQuery.append("SELECT count(puv_id) FROM procedimento_uffici_partecipanti puv  LEFT JOIN procedimenti p ON (puv.procedimento_id=p.procedimento_id) LEFT JOIN fascicolo_protocolli fp ON (fp.fascicolo_id=p.fascicolo_id) WHERE p.fascicolo_id=? AND puv.ufficio_id=? AND fp.ufficio_proprietario_id=? AND fp.protocollo_id=? AND puv.flag_visibilita=0");
			pstmt = connection.prepareStatement(strQuery.toString());
			pstmt.setInt(1, fascicoloId);
			pstmt.setInt(2, ufficioId);
			pstmt.setInt(3, ufficioId);
			pstmt.setInt(4, protocolloId);
			rs = pstmt.executeQuery();
			rs.next();
			if (rs.getInt(1) > 0) {
				abilitato = true;
			} else {
				jdbcMan.closeResultSetAndStatement(rs, pstmt);
				pstmt = connection.prepareStatement("SELECT count(assegnatario_id) FROM protocollo_assegnatari WHERE protocollo_id=? AND ufficio_assegnatario_id=?");
				pstmt.setInt(1, protocolloId);
				pstmt.setInt(2, ufficioId);
				rs = pstmt.executeQuery();
				rs.next();
				if (rs.getInt(1) > 0) {
					abilitato = true;
				} 
			}
		} catch (Exception e) {
			logger.error("Load isUfficioAbilitatoSuFascicoloProtocollo", e);
			throw new DataException("Cannot load isUfficioAbilitatoSuFascicoloProtocollo");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return abilitato;
	}
	
	public boolean isUfficioAbilitatoSuFascicoloDocumento(int ufficioId,
			int fascicoloId, int documentoId) throws DataException {

		boolean abilitato = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			StringBuffer strQuery = new StringBuffer();
			strQuery.append("SELECT count(puv_id) FROM procedimento_uffici_partecipanti puv  LEFT JOIN procedimenti p ON (puv.procedimento_id=p.procedimento_id) LEFT JOIN fascicolo_documenti fp ON (fp.fascicolo_id=p.fascicolo_id) WHERE p.fascicolo_id=? AND puv.ufficio_id=? AND fp.ufficio_proprietario_id=? AND fp.dfa_id=? AND puv.flag_visibilita=0");
			pstmt = connection.prepareStatement(strQuery.toString());
			pstmt.setInt(1, fascicoloId);
			pstmt.setInt(2, ufficioId);
			pstmt.setInt(3, ufficioId);
			pstmt.setInt(4, documentoId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				abilitato = (rs.getInt(1) > 0);
			}
		} catch (Exception e) {
			logger.error("Load isUfficioAbilitatoSuFascicoloDocumento", e);
			throw new DataException("Cannot load isUfficioAbilitatoSuFascicoloDocumento");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return abilitato;
	}

	public void aggiornaTitolarioInSottofascicoli(Connection connection,
			int fascicoloId, int titolarioId) throws DataException {

		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger
						.warn("aggiornaTitolarioInSottofascicoli() - Invalid Connection :"
								+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			Collection<FascicoloVO> sottoFasc = getFascicoliByParentId(
					connection, fascicoloId);
			for (FascicoloVO f : sottoFasc) {
				aggiornaTitolarioInSottofascicoli(connection, f.getId(),
						titolarioId);
				f.setTitolarioId(titolarioId);
				aggiornaFascicolo(connection, f);
			}
		} catch (Exception e) {
			logger.error(" aggiornaTitolarioInSottofascicoli", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}

	}

	public boolean isParent(int fascicoloId) throws DataException {
		boolean isParent = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			String strQuery = "SELECT count(fascicolo_id) FROM fascicoli f WHERE parent_id = ?";
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, fascicoloId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				isParent = (rs.getInt(1) > 0);
			}
		} catch (Exception e) {
			logger.error("Load isParent", e);
			throw new DataException("Cannot load isParent");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return isParent;
	}

	public boolean isTitolarioChanged(int fascicoloId, int titolarioId)
			throws DataException {
		boolean isParent = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			String strQuery = "SELECT count(fascicolo_id) FROM fascicoli f WHERE fascicolo_id = ? AND (NOT titolario_id=?)";

			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, fascicoloId);
			pstmt.setInt(2, titolarioId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				isParent = (rs.getInt(1) > 0);
			}
		} catch (Exception e) {
			logger.error("Load isParent", e);
			throw new DataException("Cannot load isParent");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return isParent;
	}

	public final static String SELECT_FASCICOLO_BY_ID_VERSIONE = "SELECT f.* "
			+ "FROM STORIA_FASCICOLI f "
			+ "WHERE fascicolo_id = ? and f.versione = ?";
	
	public final static String SELECT_TITOLARIO_BY_FASCICOLO_ID = "SELECT titolario_id  FROM fascicoli WHERE fascicolo_id = ?";
	

	// FINE STORIA FASCICOLO

	public final static String DELETE_FASCICOLO_PROTOCOLLO = "DELETE FROM fascicolo_protocolli WHERE fascicolo_id = ? AND protocollo_id = ?";

	public final static String DELETE_FASCICOLO_PROCEDIMENTO = "DELETE FROM procedimenti_fascicolo WHERE fascicolo_id = ? AND procedimento_id = ?";

	public final static String UPDATE_STATO_FASCICOLO = "UPDATE FASCICOLI "
			+ "SET stato=?, data_ultimo_movimento=?, data_scarico=?, "
			+ " row_updated_user = ?, row_updated_time = ? "
			+ " WHERE fascicolo_id=?";

	public final static String SCARTA_FASCICOLO = "UPDATE FASCICOLI "
			+ "SET stato=?, data_ultimo_movimento=?, data_scarto=?,"
			+ " row_updated_user = ?, row_updated_time = ?"
			+ " WHERE fascicolo_id=?";

	public final static String DELETE_CODA_INVIO_FASCICOLI = "DELETE FROM INVIO_FASCICOLI WHERE FASCICOLO_ID=?";

	public final static String DELETE_CODA_INVIO_FASCICOLI_DESTINATARI = "DELETE FROM INVIO_FASCICOLI_DESTINATARI WHERE FASCICOLO_ID=?";

	public final static String DELETE_DOCUMENTI_INVIO_FASCICOLI = "DELETE FROM invio_fascicoli WHERE fascicolo_id=?";

	public final static String DELETE_DESTINATARI_INVIO_FASCICOLI = "DELETE FROM invio_fascicoli_destinatari WHERE fascicolo_id=?";

	public final static String INVIO_FASCICOLI = "INSERT INTO invio_fascicoli"
			+ " (id, fascicolo_id, dfa_id, flag_documento_principale, aoo_id, "
			+ "ufficio_mittente_id, utente_mittente_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

	public final static String INVIO_FASCICOLI_DESTINATARI = "INSERT INTO invio_fascicoli_destinatari"
			+ " (id,  fascicolo_id ,flag_tipo_destinatario, indirizzo,  email ,  destinatario ,"
			+ "mezzo_spedizione ,  citta ,  data_spedizione ,  flag_conoscenza ,  data_effettiva_spedizione,"
			+ "versione"
			// + ", titolo_id"
			+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?"
			// + ", ?"
			+ ")";

	public final static String DELETE_FASCICOLI_ALLACCI = "DELETE FROM fascicolo_allacci WHERE fascicolo_id=? AND fascicolo_allacciato_id=?";

	public final static String DELETE_FASCICOLI_DOCUMENTO = "DELETE FROM fascicolo_documenti WHERE dfa_id = ?";

	public final static String DELETE_FASCICOLI_PROTOCOLLO = "DELETE FROM fascicolo_protocolli  WHERE protocollo_id = ?";

	public final static String DELETE_DOCUMENTO_FASCICOLO = "DELETE FROM fascicolo_documenti"
			+ " WHERE fascicolo_id = ? AND dfa_id=?";

	public final static String DELETE_PROTOCOLLO_FASCICOLO = "DELETE FROM fascicolo_protocolli"
			+ " WHERE fascicolo_id = ? AND protocollo_id=?";

	public final static String INSERT_FASCICOLO_PROCEDIMENTO = "INSERT INTO procedimenti_fascicolo"
			+ " (fascicolo_id, procedimento_id, versione, row_created_user) VALUES (?, ?, ?, ?)";

	public final static String INSERT_FASCICOLO_PROTOCOLLO = "INSERT INTO fascicolo_protocolli"
			+ " (fascicolo_id, protocollo_id, versione, row_created_user, ufficio_proprietario_id) VALUES (?, ?, ?, ?, ?)";

	public final static String INSERT_FASCICOLO_DOCUMENTO = "INSERT INTO fascicolo_documenti"
			+ " (fascicolo_id, dfa_id, row_created_user, versione, ufficio_proprietario_id) VALUES (?, ?, ?, ?, ?)";

	public final static String INSERT_FASCICOLO_ALLACCIO = "INSERT INTO fascicolo_allacci"
			+ " (fascicolo_id, fascicolo_allacciato_id, row_created_user) VALUES (?, ?, ?)";

	private final static String UPDATE_FASCICOLO = "UPDATE fascicoli SET"
			+ " anno_riferimento = ?, codice = ?, descrizione = ?,  nome = ?, tipo=?, "
			+ " ufficio_responsabile_id = ?, carica_responsabile_id = ?, ufficio_intestatario_id=?, carica_intestatario_id=?,"
			+ " note = ?, oggetto = ?, stato = ?, titolario_id = ?,  data_chiusura = ?, "
			+ " data_evidenza =?, data_ultimo_movimento=?, data_scarto=?, data_carico=?, data_scarico=?, "
			+ " row_updated_user = ?, row_updated_time = ?,"
			+ " collocazione_label1=?,collocazione_label2=?, collocazione_label3=?, collocazione_label4=?,"
			+ " collocazione_valore1=?,collocazione_valore2 =?,collocazione_valore3 = ?,collocazione_valore4 = ?"
			+ " ,carica_istruttore_id= ?,interessato= ?,delegato= ?,indi_interessato= ?,indi_delegato= ?, parent_id=?,giorni_max=?,giorni_alert=?,comune=?,capitolo=? WHERE fascicolo_id = ?";

	private final static String ADD_FASCICOLO = "("
			+ "fascicolo_id, aoo_id, registro_id, codice, descrizione, nome, progressivo, anno_riferimento, tipo, "
			+ "ufficio_responsabile_id, carica_responsabile_id,ufficio_intestatario_id, carica_intestatario_id, "
			+ "note, processo_id,oggetto, data_apertura, stato, row_created_user ,titolario_id, "
			+ "data_evidenza, data_ultimo_movimento, data_scarto, data_carico, data_scarico, versione, "
			+ "collocazione_label1,collocazione_label2,collocazione_label3,collocazione_label4,collocazione_valore1,"
			+ "collocazione_valore2,collocazione_valore3,collocazione_valore4,carica_istruttore_id, interessato, delegato, indi_interessato, indi_delegato, parent_id,giorni_max,giorni_alert,comune,capitolo,path_progressivo)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public final static String INSERT_FASCICOLO = "INSERT INTO fascicoli "
			+ ADD_FASCICOLO;

	public final static String INSERT_STORIA_FASCICOLO = "INSERT INTO storia_fascicoli "
			+ ADD_FASCICOLO;

	public final static String SELECT_FASCICOLI_BY_PROTOCOLLO_ID = "SELECT f.*, p.row_created_user AS utente_fascicolatore FROM fascicoli f, fascicolo_protocolli p"
			+ " WHERE f.fascicolo_id = p.fascicolo_id AND p.protocollo_id=?";

	public final static String SELECT_FASCICOLI_COLLEGATI_BY_ID = "SELECT fascicolo_allacciato_id from fascicolo_allacci where fascicolo_id=? ";

	public final static String SELECT_SOTTO_FASCICOLI_BY_ID = "SELECT fascicolo_id from fascicoli where parent_id=? ";

	public final static String SELECT_FASCICOLI_BY_PARENT_ID = "SELECT * FROM fascicoli "
			+ " WHERE parent_id = ?";

	public final static String SELECT_FASCICOLI_BY_AOO_ID = "SELECT * FROM fascicoli "
			+ " WHERE aoo_id = ?";

	public final static String SELECT_FASCICOLI_BY_DOCUMENTO_ID = "SELECT f.* FROM fascicoli f, fascicolo_documenti d"
			+ " WHERE f.fascicolo_id = d.fascicolo_id AND d.dfa_id=?";

	public final static String SELECT_STORIA_FASCICOLI_BY_DOCUMENTO_ID = "SELECT f.* FROM fascicoli f, storia_fascicolo_documenti d"
			+ " WHERE f.fascicolo_id = d.fascicolo_id AND d.dfa_id=? and d.versione=?";

	public final static String SELECT_FASCICOLO_BY_ID = "SELECT f.* "
			+ "FROM FASCICOLI f " + "WHERE fascicolo_id = ? ";

	public final static String SELECT_FASCICOLO_BY_CODICE = "SELECT f.* "
			+ "FROM FASCICOLI f " + "WHERE codice = ? ";
	
	public final static String SELECT_FASCICOLO_BY_ANNO_NUMERO = "SELECT * "
			+ "FROM FASCICOLI WHERE to_char(data_apertura,'YYYY')=? and progressivo = ?";

	public final static String SELECT_FASCICOLO_VIEW_BY_ID = "SELECT F.*,U.DESCRIZIONE as desc_ufficio "
			+ "FROM FASCICOLI F LEFT OUTER JOIN UFFICI U ON(f.ufficio_intestatario_id=u.ufficio_id) WHERE f.fascicolo_id = ?";

	public final static String SELECT_FASCICOLI_INVIO = "select f.*,d.dfa_id, d.nome as nomefile from invio_fascicoli i"
			+ " left join fascicoli f on i.fascicolo_id=f.fascicolo_id"
			+ " left join doc_file_attr d on i.dfa_id=d.dfa_id"
			+ " where i.aoo_id=? order by f.fascicolo_id";

	public final static String SELECT_DOCUMENTI_FASCICOLO = "select * from invio_fascicoli"
			+ " where fascicolo_id=?";

	public final static String SELECT_DESTINATARI_FASCICOLO = "select * from invio_fascicoli_destinatari "
			+ "where fascicolo_id=?";

	public final static String SELECT_ULTIMO_PROGRESSIVO = "SELECT MAX(progressivo) FROM fascicoli WHERE aoo_id = ? and to_char(data_apertura,'YYYY')=?";

	public final static String RIFERIMENTI = "SELECT documento_id FROM riferimenti_legislativi WHERE fascicolo_id = ?";

	public final static String INSERT_RIFERIMENTI = "INSERT INTO riferimenti_legislativi"
			+ " ( riferimento_id,fascicolo_id,documento_id) VALUES (?, ?, ?)";
	
	public final static String SELECT_FASCICOLI_BY_ISTRUTTORE = "SELECT * FROM fascicoli f WHERE carica_istruttore_id =? AND stato=?";

	public final static String SELECT_FASCICOLI_BY_REFERENTE = "SELECT * FROM fascicoli f WHERE carica_intestatario_id =? AND stato=?";

	
	public int contaFascicoliAlert(int referenteId) throws DataException {
		Connection connection = null;
		String query = "select count(f.fascicolo_id) from fascicoli f where f.giorni_max-date_part('day',f.data_apertura)<=f.giorni_alert AND f.carica_responsabile_id=? AND f.stato=0";
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
			logger.error("contaFascicoliAlert", e);
			throw new DataException("contaFascicoliAlert");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return count;
	}

	public Map<String,FascicoloView> getFascicoliAlert(int referenteId) throws DataException {
		Connection connection = null;
		String query = "SELECT f.*, (f.giorni_max-date_part('day',f.data_apertura)) "
				+ "as giorni_mancanti from fascicoli f  "
				+ "WHERE f.giorni_max-date_part('day',f.data_apertura)<=f.giorni_alert AND f.carica_responsabile_id=? AND f.stato=0";
		PreparedStatement pstmt = null;
		Map<String,FascicoloView> ids = new HashMap<String,FascicoloView>(2);
		FascicoloView fascicolo = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, referenteId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				fascicolo = new FascicoloView();
				fascicolo.setId(rs.getInt("fascicolo_id"));
				fascicolo.setNome(rs.getString("nome"));
				fascicolo.setUfficioIntestatarioId(rs
						.getInt("ufficio_intestatario_id"));
				fascicolo.setOggetto(rs.getString("oggetto"));
				if (rs.getDate("data_apertura") != null)
					fascicolo.setDataApertura(DateUtil.formattaData(rs.getDate(
							"data_apertura").getTime()));
				if (rs.getDate("data_evidenza") != null)
					fascicolo.setDataEvidenza(DateUtil.formattaData(rs.getDate(
							"data_evidenza").getTime()));
				fascicolo.setTitolarioId(rs.getInt("titolario_id"));
				fascicolo.setAnnoRiferimento(rs.getInt("anno_riferimento"));
				fascicolo.setProgressivo(rs.getInt("progressivo"));
				fascicolo.setStato(rs.getString("stato"));
				fascicolo.setGiorniRimanenti(rs.getInt("giorni_mancanti"));
				fascicolo.setPathProgressivo(rs.getString("path_progressivo"));
				ids.put(String.valueOf(fascicolo.getId()), fascicolo);
			}

		} catch (Exception e) {
			logger.error("getFascicoliAlert", e);
			throw new DataException("getFascicoliAlert");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return ids;
	}

	public int getMaxProgressivo(Connection connection, int titolarioId,
			int parentId) throws DataException {
		PreparedStatement pstmt = null;
		String query = "SELECT MAX(progressivo) FROM fascicoli WHERE anno_riferimento=? AND titolario_id = ?";
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("getMaxProgressivo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			if (parentId != 0)
				query = query + " AND parent_id=" + parentId;

			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, DateUtil.getAnnoCorrente());
			pstmt.setInt(2, titolarioId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) + 1;

			} else {
				return 1;
			}
		} catch (Exception e) {
			logger.error("getMaxProgressivo", e);
			throw new DataException("Cannot load getMaxProgressivo");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
	}

	@Override
	public Map<Integer,FascicoloView> getFascicoliReferentePerCruscotti(Integer caricaId)
			throws DataException {
		Map<Integer,FascicoloView>  fascicoli = new HashMap<Integer,FascicoloView> ();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_FASCICOLI_BY_REFERENTE);
			pstmt.setInt(1, caricaId);
			pstmt.setInt(2, Parametri.STATO_FASCICOLO_APERTO);
			
			rs = pstmt.executeQuery();
			FascicoloView fascicolo;
			while (rs.next()) {
				fascicolo = new FascicoloView();
				fascicolo.setId(rs.getInt("fascicolo_id"));
				fascicolo.setTitolarioId(rs.getInt("titolario_id"));
				fascicolo.setNome(rs.getString("nome"));
				fascicolo.setOggetto(rs.getString("oggetto"));
				fascicolo.setNote(rs.getString("note"));
				fascicolo.setStato(rs.getString("stato"));
				if (rs.getDate("data_apertura") != null)
					fascicolo.setDataApertura(DateUtil.formattaData(rs.getDate(
							"data_apertura").getTime()));
				fascicolo.setAnnoRiferimento(rs.getInt("anno_riferimento"));
				fascicolo.setProgressivo(rs.getInt("progressivo"));
				fascicolo.setUfficioIntestatarioId(rs.getInt("ufficio_intestatario_id"));
				fascicolo.setCaricaIntestatarioId(rs.getInt("carica_intestatario_id"));
				
				fascicolo.setUfficioResponsabileId(rs.getInt("ufficio_responsabile_id"));
				fascicolo.setCaricaResponsabileId(rs.getInt("carica_responsabile_id"));
				
				fascicolo.setPathProgressivo(rs.getString("path_progressivo"));
				fascicoli.put(fascicolo.getId(), fascicolo);
			}
		} catch (Exception e) {
			logger.error("Load getProtocolliFascicoloById", e);
			throw new DataException("Cannot load getProtocolliFascicoloById");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return fascicoli;
	}
	
	//SELECT_FASCICOLO_BY_ID
	@Override
	public Integer getTitolarioByFascicoloId(int id) throws DataException{
		Integer titolarioId=null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_TITOLARIO_BY_FASCICOLO_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				titolarioId = rs.getInt(0);
			}
		}catch (Exception e) {
			logger.error("Load getProtocolliFascicoloById", e);
			throw new DataException("Cannot load getProtocolliFascicoloById");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return titolarioId;
	}
	
	@Override
	public Map<Integer,FascicoloView> getFascicoliIstruttorePerCruscotti(Integer caricaId)
			throws DataException {
		Map<Integer,FascicoloView>  fascicoli = new HashMap<Integer,FascicoloView> ();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_FASCICOLI_BY_ISTRUTTORE);
			pstmt.setInt(1, caricaId);
			pstmt.setInt(2, Parametri.STATO_FASCICOLO_APERTO);
			
			rs = pstmt.executeQuery();
			FascicoloView fascicolo;
			while (rs.next()) {
				fascicolo = new FascicoloView();
				fascicolo.setId(rs.getInt("fascicolo_id"));
				fascicolo.setTitolarioId(rs.getInt("titolario_id"));
				fascicolo.setNome(rs.getString("nome"));
				fascicolo.setOggetto(rs.getString("oggetto"));
				fascicolo.setNote(rs.getString("note"));
				fascicolo.setStato(rs.getString("stato"));
				if (rs.getDate("data_apertura") != null)
					fascicolo.setDataApertura(DateUtil.formattaData(rs.getDate(
							"data_apertura").getTime()));
				fascicolo.setAnnoRiferimento(rs.getInt("anno_riferimento"));
				fascicolo.setProgressivo(rs.getInt("progressivo"));
				fascicolo.setUfficioIntestatarioId(rs.getInt("ufficio_intestatario_id"));
				fascicolo.setCaricaIntestatarioId(rs.getInt("carica_intestatario_id"));
				
				fascicolo.setUfficioResponsabileId(rs.getInt("ufficio_responsabile_id"));
				fascicolo.setCaricaResponsabileId(rs.getInt("carica_responsabile_id"));
				
				fascicolo.setPathProgressivo(rs.getString("path_progressivo"));
				fascicoli.put(fascicolo.getId(), fascicolo);
			}
		} catch (Exception e) {
			logger.error("Load getProtocolliFascicoloById", e);
			throw new DataException("Cannot load getProtocolliFascicoloById");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return fascicoli;
		
	}
};