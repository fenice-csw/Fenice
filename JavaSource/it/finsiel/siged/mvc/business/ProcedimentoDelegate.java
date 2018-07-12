package it.finsiel.siged.mvc.business;

import it.compit.fenice.mvc.bo.TipoProcedimentoBO;
import it.compit.fenice.mvc.presentation.helper.ProtocolloProcedimentoView;
import it.compit.fenice.mvc.vo.protocollo.UfficioPartecipanteVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.NomiTabelle;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.Fascicolo;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.Procedimento;
import it.finsiel.siged.mvc.integration.ProcedimentoDAO;
import it.finsiel.siged.mvc.presentation.helper.AllaccioView;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.presentation.helper.FascicoloView;
import it.finsiel.siged.mvc.presentation.helper.ProcedimentoView;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.FaldoneVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.ProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.finsiel.siged.util.DateUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.apache.log4j.Logger;

public class ProcedimentoDelegate {

	private static Logger logger = Logger.getLogger(ProcedimentoDelegate.class
			.getName());

	private ProcedimentoDAO procedimentoDAO = null;

	private static ProcedimentoDelegate delegate = null;

	private ProcedimentoDelegate() {
		try {
			if (procedimentoDAO == null) {
				procedimentoDAO = (ProcedimentoDAO) DAOFactory
						.getDAO(Constants.PROCEDIMENTO_DAO_CLASS);

				logger.debug("procedimentoDAO instantiated:"
						+ Constants.PROCEDIMENTO_DAO_CLASS);
			}
		} catch (Exception e) {
			logger.error("Exception while connecting to "
					+ Constants.PROCEDIMENTO_DAO_CLASS + "!!", e);
		}

	}

	public static ProcedimentoDelegate getInstance() {
		if (delegate == null)
			delegate = new ProcedimentoDelegate();
		return delegate;
	}

	public static String getIdentifier() {
		return Constants.PROCEDIMENTO_DELEGATE;
	}
	
	public List<UfficioPartecipanteVO> getUfficiPartecipanti(int id) {
		try {
			return procedimentoDAO.getUfficiPartecipanti(id);
		} catch (DataException de) {
			logger.error("ProcedimentoDelegate: failed getting getUfficiPartecipanti: ");
			return null;
		}
	}
	
	public Map<String, UfficioPartecipanteVO> getUfficiPartecipantiMap(int id) {
		try {
			return procedimentoDAO.getUfficiPartecipantiMap(id);
		} catch (DataException de) {
			logger.error("ProcedimentoDelegate: failed getting getUfficiPartecipantiForProcedimento: ");
			return null;
		}
	}

	public AllaccioView getProtocolloAllacciabile(Utente utente,
			int annoProtocollo, int numeroProtocollo, int procedimentoId) {
		try {
			return procedimentoDAO.getProtocolloAllacciabile(utente,
					annoProtocollo, numeroProtocollo, procedimentoId);
		} catch (DataException de) {
			logger.error("ProcedimentoDelegate: failed getting getProtocolloAllacciabile: ");
			return null;
		}
	}

	public ProcedimentoVO getProcedimentoVOByIdVersione(int procedimentoId,
			int versione) {

		try {
			ProcedimentoVO p = new ProcedimentoVO();
			p = procedimentoDAO.getProcedimentoByIdVersione(procedimentoId,
					versione);

			return p;
		} catch (Exception de) {
			logger.error("ProcedimentoDelegate: failed getting getProcedimentoById: ");
			return null;
		}
	}

	public void salvaProcedimentoFascicolo(Connection connection,
			int fascicoloId, int procedimentoId, String userName)
			throws DataException {
		ProcedimentoVO procedimentoVO = new ProcedimentoVO();
		try {
			procedimentoVO = procedimentoDAO
					.getProcedimentoById(procedimentoId);
			procedimentoVO = procedimentoDAO.aggiornaProcedimento(connection,
					procedimentoVO);
			procedimentoDAO.cancellaProcedimentoFascicolo(connection,
					procedimentoId, fascicoloId);
			procedimentoDAO.salvaProcedimentoFascicolo(connection,
					procedimentoId, fascicoloId, userName,
					procedimentoVO.getVersione());
		} catch (DataException e) {
			e.printStackTrace();
			throw new DataException(
					"Errore nel salvataggio del Procedimento-Fascicolo.");
		}
	}

	public void deleteFascicoloProcedimento(Connection connection,
			int procedimentoId, int fascicoloId) throws DataException {
		ProcedimentoVO procedimentoVO = new ProcedimentoVO();
		try {
			procedimentoVO = procedimentoDAO
					.getProcedimentoById(procedimentoId);
			procedimentoVO = procedimentoDAO.aggiornaProcedimento(connection,
					procedimentoVO);
			procedimentoDAO.cancellaProcedimentoFascicolo(connection,
					procedimentoId, fascicoloId);
		} catch (DataException e) {
			e.printStackTrace();
			throw new DataException(
					"Errore nella cancellazione del Procedimento-Fascicolo.");
		}
	}

	public void salvaProcedimentoProtocollo(Connection connection,
			ProtocolloProcedimentoVO vo) throws DataException {
		ProcedimentoVO procedimentoVO = new ProcedimentoVO();
		int procedimentoId = vo.getProcedimentoId();
		procedimentoVO = procedimentoDAO.getProcedimentoById(procedimentoId);
		procedimentoVO.setRowCreatedUser(vo.getRowCreatedUser());
		procedimentoVO.setRowUpdatedUser(vo.getRowUpdatedUser());
		procedimentoVO = procedimentoDAO.aggiornaProcedimento(connection,
				procedimentoVO);
		vo.setVersione(procedimentoVO.getVersione());
		procedimentoDAO.cancellaProcedimentoProtocollo(connection, vo);
		procedimentoDAO.salvaProcedimentoProtocollo(connection, vo);
	}

	public Procedimento getProcedimentoByIdVersione(int procedimentoId,
			int versione) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			Procedimento p = new Procedimento();
			ProcedimentoVO vo = procedimentoDAO.getProcedimentoByIdVersione(
					connection, procedimentoId, versione);
			p.setProcedimentoVO(vo);

			if (vo.getReturnValue() == ReturnValues.FOUND) {
				ReportProtocolloView pa = ProtocolloDelegate.getInstance()
						.getProtocolloView(vo.getProtocolloId());
				vo.setNumeroProtovollo(pa.getAnnoNumeroProtocollo());
				p.setProcedimentoVO(vo);
				// carico istruttori e riferimenti
				vo.setIstruttori(procedimentoDAO.getStoriaIstruttori(
						vo.getId(), versione));
				vo.setRiferimentiLegislativi(procedimentoDAO.getRiferimenti(vo.getId()));
				vo.setUfficiPartecipanti(procedimentoDAO.getUfficiPartecipantiMap(vo.getId()));

				// carico faldoni associati

				Collection<Integer> idf = procedimentoDAO.getProcedimentoFaldoni(
						connection, procedimentoId);
				Iterator<Integer> itf = idf.iterator();
				while (itf.hasNext()) {
					Integer id = (Integer) itf.next();
					FaldoneVO f = FaldoneDelegate.getInstance().getFaldone(
							connection, id.intValue());
					if (f.getReturnValue() == ReturnValues.FOUND)
						p.getFaldoni().put(String.valueOf(f.getId()), f);
				}
				// carico fascicoli associati
				Collection<Integer> idfa = procedimentoDAO
						.getStoriaProcedimentoFascicoli(connection,
								procedimentoId, versione);
				Iterator<Integer> itfa = idfa.iterator();
				while (itfa.hasNext()) {
					Integer id = (Integer) itfa.next();
					FascicoloView f = FascicoloDelegate.getInstance()
							.getFascicoloViewById(connection, id.intValue());
					if (f != null)
						p.getFascicoli().put(String.valueOf(f.getId()), f);
				}
				Map<String, ProtocolloProcedimentoView> proc = procedimentoDAO.getStoriaProcedimentoProtocolli(
						connection, procedimentoId, versione);
				if (!proc.isEmpty())
					p.getProtocolli().putAll(proc);
			}
			return p;
		} catch (Exception de) {
			logger.error("ProcedimentoDelegate: failed getting getProcedimentoById: ");
			return null;
		}
	}

	public ProcedimentoVO salvaProcedimento(Procedimento pro, Utente utente) {
		ProcedimentoVO newVO = new ProcedimentoVO();
		FascicoloDelegate fd = FascicoloDelegate.getInstance();
		newVO.setReturnValue(ReturnValues.UNKNOWN);
		JDBCManager jdbcMan = null;
		Connection connection = null;
		Date dataCorrente = new Date(System.currentTimeMillis());
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			ProcedimentoVO vo = pro.getProcedimentoVO();
			vo.setAnno(DateUtil.getYear(new Date(System.currentTimeMillis())));
			vo.setRowCreatedUser(utente.getValueObject().getUsername());
			vo.setRowCreatedTime(new Date(System.currentTimeMillis()));
			vo.setId(IdentificativiDelegate.getInstance().getNextId(connection,
					NomiTabelle.PROCEDIMENTI));

			int nextNum = IdentificativiDelegate.getInstance()
					.getNextId(
							connection,
							NomiTabelle.PROCEDIMENTI
									+ String.valueOf(pro.getProcedimentoVO()
											.getAooId())
									+ DateUtil.getYear(dataCorrente));
			vo.setNumero(nextNum);
			vo.setDataScadenza(setDataScadenza(vo));
			FascicoloVO fVO = createFascicoloFromProcedimento(connection, vo,
					utente);
			fVO = fd.nuovoFascicolo(connection, fVO);
			vo.setFascicoloId(fVO.getId().intValue());
			vo.setNumeroProcedimento(fVO.getAnnoProgressivo());
			newVO = procedimentoDAO.newProcedimento(connection, vo);
			newVO.setFascicoloId(fVO.getId().intValue());

			procedimentoDAO.salvaProcedimentoFascicolo(connection, newVO
					.getId(), fVO.getId(), utente.getValueObject()
					.getUsername(), newVO.getVersione());
			salvaRiferimenti(connection, newVO, vo.getRiferimentiLegislativi());
			salvaIstruttori(connection, newVO, vo.getIstruttori(), utente.getValueObject().getUsername());
			salvaVisibilitaUffici(connection, newVO, vo.getUfficiPartecipantiValues(), utente.getValueObject().getUsername());
			ProtocolloVO p = ProtocolloDelegate.getInstance()
					.getProtocolloVOById(vo.getProtocolloId());
			if (p.getFlagTipo().equals("I")) {
				procedimentoDAO.setStatoProtocolloAssociato(connection,
						vo.getProtocolloId(),
						Parametri.STATO_ASSOCIATO_A_PROCEDIMENTO);
			}

			ReportProtocolloView pa = ProtocolloDelegate.getInstance().getProtocolloView(connection, vo.getProtocolloId());
			newVO.setNumeroProtovollo(pa.getAnnoNumeroProtocollo());
			connection.commit();
			newVO.setReturnValue(ReturnValues.SAVED);

		} catch (DataException de) {
			newVO.setReturnValue(ReturnValues.UNKNOWN);
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Procedimento fallito, rolling back transction..",
					de);

		} catch (SQLException se) {
			newVO.setReturnValue(ReturnValues.UNKNOWN);
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Procedimento fallito, rolling back transction..",
					se);

		} catch (Exception e) {
			newVO.setReturnValue(ReturnValues.UNKNOWN);
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);

		} finally {
			jdbcMan.close(connection);
		}
		return newVO;
	}

	private Date setDataScadenza(ProcedimentoVO vo) {
		if (vo.getGiorniMax() > 0) {
			return DateUtil.addDays(vo.getDataAvvio(), vo.getGiorniMax());
		}
		return null;
	}

	private FascicoloVO createFascicoloFromProcedimento(Connection conn,
			ProcedimentoVO p, Utente utente) {
		AmministrazioneDelegate.getInstance().getTipoProcedimento(
				p.getTipoProcedimentoId());
		FascicoloVO fVO = new FascicoloVO();
		fVO.setAooId(p.getAooId());
		fVO.setRegistroId(utente.getRegistroInUso());
		fVO.setCodice("");
		fVO.setDataApertura(p.getDataAvvio());
		fVO.setDescrizione(p.getOggetto());
		fVO.setId(0);
		fVO.setNome(p.getOggetto());
		fVO.setOggetto(p.getOggetto());
		fVO.setStato(0);
		fVO.setTitolarioId(AmministrazioneDelegate.getInstance()
				.getTipoProcedimento(p.getTipoProcedimentoId())
				.getTitolarioId());
		fVO.setUfficioResponsabileId(p.getUfficioId());
		fVO.setCaricaResponsabileId(p.getResponsabileId());
		fVO.setUfficioIntestatarioId(p.getUfficioId());
		fVO.setCaricaIntestatarioId(p.getResponsabileId());
		fVO.setVersione(0);
		fVO.setRowCreatedUser(utente.getValueObject().getUsername());
		fVO.setRowCreatedTime(new Date(System.currentTimeMillis()));
		fVO.setDataCarico(new Date(System.currentTimeMillis()));
		fVO.setRowUpdatedTime(new Date(System.currentTimeMillis()));
		fVO.setAnnoRiferimento(p.getAnno());
		fVO.setTipoFascicolo(0);
		fVO.setDataEvidenza(null);
		fVO.setDataUltimoMovimento(null);
		fVO.setDataScarto(null);
		fVO.setDataScarico(null);
		fVO.setCollocazioneLabel1(null);
		fVO.setCollocazioneLabel2(null);
		fVO.setCollocazioneLabel3(null);
		fVO.setCollocazioneLabel4(null);
		fVO.setCollocazioneValore1(null);
		fVO.setCollocazioneValore2(null);
		fVO.setCollocazioneValore3(null);
		fVO.setCollocazioneValore4(null);
		fVO.setComune(null);
		fVO.setCapitolo(null);
		
		fVO.setDelegato(p.getDelegato());
		fVO.setInteressato(p.getInteressato());
		fVO.setIndiDelegato(p.getIndiDelegato());
		fVO.setIndiInteressato(p.getIndiInteressato());
		
		return fVO;
	}

	private Map<String,DocumentoVO> salvaRiferimenti(Connection connection, ProcedimentoVO vo,
			Map<String,DocumentoVO> allegati) throws Exception {
		DocumentoDelegate documentoDelegate = DocumentoDelegate.getInstance();
		int procedimentoId = vo.getId();
		Iterator<DocumentoVO> iterator = allegati.values().iterator();
		Map<String,DocumentoVO> docs = new HashMap<String,DocumentoVO>(2);
		procedimentoDAO.deleteRiferimenti(connection, procedimentoId);
		while (iterator.hasNext()) {
			DocumentoVO doc = (DocumentoVO) iterator.next();
			int idx = doc.getIdx();
			if (doc != null) {
				if (doc.getId() == null || doc.isMustCreateNew()) {

					doc = documentoDelegate.salvaDocumento(connection, doc);
				}
				doc.setIdx(idx);
				TipoProcedimentoBO.putAllegato(doc, docs);
				procedimentoDAO.salvaRiferimenti(
						connection,
						IdentificativiDelegate.getInstance()
								.getNextId(connection,
										NomiTabelle.RIFERIMENTI_LEGISLATIVI),
						procedimentoId, doc.getId().intValue(), vo
								.getVersione());
			}
		}
		return docs;
	}
	
    private void salvaVisibilitaUffici(Connection connection, ProcedimentoVO vo,
    		Collection<UfficioPartecipanteVO> ufficiPartecipanti, String username) throws Exception {
    	int procedimentoId = vo.getId();
    	procedimentoDAO.deleteUfficiPartecipanti(connection, procedimentoId);
    	for(UfficioPartecipanteVO uff: ufficiPartecipanti)
			procedimentoDAO.salvaUfficioPartecipante(connection, IdentificativiDelegate.getInstance().getNextId(connection,
								NomiTabelle.PROCEDIMENTO_UFFICI_PARTECIPANTI), procedimentoId,
						uff.getUfficioId(), uff.isVisibilita() ,vo.getVersione(), username);
		}

	private void salvaIstruttori(Connection connection, ProcedimentoVO vo,
			Collection<AssegnatarioVO> assegnatari, String username)
			throws Exception {
		int procedimentoId = vo.getId();
		procedimentoDAO.deleteIstruttori(connection, procedimentoId);
		for (AssegnatarioVO ass : assegnatari) {
			procedimentoDAO.salvaIstruttori(connection, procedimentoId,
					ass.getCaricaAssegnatarioId(), ass.isLavorato(),
					vo.getVersione(), username);
		}
	}

	// STORIA PROCEDIMENTO

	public Collection<ProcedimentoView> getStoriaProcedimento(int procedimentoId) {
		try {
			return procedimentoDAO.getStoriaProcedimenti(procedimentoId);
		} catch (DataException de) {
			logger.error("StoriaProcedimentoDelegate: failed getting getStoriaProcedimento: ");
			return null;
		}
	}

	public ProcedimentoVO aggiornaProcedimento(Procedimento pro, Utente utente) {
		ProcedimentoVO newVO = new ProcedimentoVO();
		newVO.setReturnValue(ReturnValues.UNKNOWN);
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			ProcedimentoVO vo = pro.getProcedimentoVO();
			vo.setRowUpdatedUser(utente.getValueObject().getUsername());
			vo.setRowUpdatedTime(new Date(System.currentTimeMillis()));
			newVO = procedimentoDAO.aggiornaProcedimento(connection, vo);
			salvaRiferimenti(connection, newVO, vo.getRiferimentiLegislativi());
			salvaVisibilitaUffici(connection, newVO, vo.getUfficiPartecipantiValues(), utente.getValueObject().getUsername());
			salvaIstruttori(connection, newVO, vo.getIstruttori(), utente.getValueObject().getUsername());
			procedimentoDAO.cancellaFaldoni(connection, vo.getId().intValue());
			procedimentoDAO.cancellaProtocolli(connection, vo.getId().intValue());
			procedimentoDAO.cancellaFascicoli(connection, vo.getId().intValue());
			if (!pro.getFascicoli().isEmpty()) {
				Iterator<FascicoloView> it = pro.getFascicoli().values().iterator();
				while (it.hasNext()) {
					FascicoloView fasc = it.next();
					procedimentoDAO.salvaProcedimentoFascicolo(connection, vo
							.getId().intValue(), fasc.getId(), utente
							.getValueObject().getUsername(), newVO
							.getVersione());
				}
			}
			if (!pro.getProtocolli().isEmpty()) {
				Iterator<ProtocolloProcedimentoView> it = pro.getProtocolli().values().iterator();
				while (it.hasNext()) {
					ProtocolloProcedimentoVO protocolloProcedimentoVO = new ProtocolloProcedimentoVO();
					ProtocolloProcedimentoView protocolloView = it
							.next();
					protocolloProcedimentoVO.setProcedimentoId(vo.getId()
							.intValue());
					protocolloProcedimentoVO.setProtocolloId(protocolloView
							.getProtocolloId());
					protocolloProcedimentoVO.setSospeso(protocolloView
							.getSospeso());
					protocolloProcedimentoVO.setVersione(newVO.getVersione());
					protocolloProcedimentoVO.setRowCreatedUser(utente
							.getValueObject().getUsername());
					protocolloProcedimentoVO.setRowUpdatedUser(utente
							.getValueObject().getUsername());
					protocolloProcedimentoVO.setRowCreatedTime(new Date(System
							.currentTimeMillis()));
					protocolloProcedimentoVO.setRowUpdatedTime(new Date(System
							.currentTimeMillis()));

					procedimentoDAO.salvaProcedimentoProtocollo(connection,
							protocolloProcedimentoVO);
				}
			}
			ProtocolloVO p = ProtocolloDelegate.getInstance().getProtocolloVOById(vo.getProtocolloId());
			if (p.getFlagTipo().equals("I")) {
				procedimentoDAO.setStatoProtocolloAssociato(connection,
						vo.getProtocolloId(),
						Parametri.STATO_ASSOCIATO_A_PROCEDIMENTO);
			}
			ReportProtocolloView pa = ProtocolloDelegate.getInstance()
					.getProtocolloView(connection, vo.getProtocolloId());
			newVO.setNumeroProtovollo(pa.getAnnoNumeroProtocollo());
			connection.commit();
			newVO.setReturnValue(ReturnValues.SAVED);
		} catch (DataException de) {
			newVO.setReturnValue(ReturnValues.UNKNOWN);
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Procedimento fallito, rolling back transction..",
					de);

		} catch (SQLException se) {
			newVO.setReturnValue(ReturnValues.UNKNOWN);
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Procedimento fallito, rolling back transction..",
					se);

		} catch (Exception e) {
			newVO.setReturnValue(ReturnValues.UNKNOWN);
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);

		} finally {
			jdbcMan.close(connection);
		}
		return newVO;
	}

	public void eliminaProtocolloProcedimento(Connection connection,
			ProtocolloProcedimentoVO vo) throws DataException {
		ProcedimentoVO procedimentoVO = new ProcedimentoVO();
		int procedimentoId = vo.getProcedimentoId();
		procedimentoVO = procedimentoDAO.getProcedimentoById(procedimentoId);
		procedimentoVO.setRowCreatedUser(vo.getRowCreatedUser());
		procedimentoVO.setRowUpdatedUser(vo.getRowUpdatedUser());
		procedimentoVO = procedimentoDAO.aggiornaProcedimento(connection,procedimentoVO);
		vo.setVersione(procedimentoVO.getVersione());
		procedimentoDAO.cancellaProcedimentoProtocollo(connection, vo);
		
			
	}
	
	/*
	public int eliminaProtocolloProcedimento(int procedimentoId, int protocolloId, String userName) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		int statusFlag = ReturnValues.UNKNOWN;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			ProtocolloProcedimentoVO vo = new ProtocolloProcedimentoVO();
			vo.setProtocolloId(protocolloId);
			vo.setProcedimentoId(procedimentoId);
			vo.setRowCreatedUser(userName);
			vo.setRowUpdatedUser(userName);
			vo.setRowCreatedTime(new Date(System.currentTimeMillis()));
			vo.setRowUpdatedTime(new Date(System.currentTimeMillis()));
			ProcedimentoVO procedimentoVO = procedimentoDAO.getProcedimentoById(procedimentoId);
			procedimentoVO.setRowCreatedUser(userName);
			procedimentoVO.setRowUpdatedUser(userName);
			procedimentoVO = procedimentoDAO.aggiornaProcedimento(connection,procedimentoVO, false);
			vo.setVersione(procedimentoVO.getVersione());
			procedimentoDAO.cancellaProcedimentoProtocollo(connection, vo);
			connection.commit();
			statusFlag = ReturnValues.SAVED;
		} catch (DataException de) {
			logger.error("ProcedimentoDelegate: failed eliminaProtocolloProcedimento: ");
			jdbcMan.rollback(connection);
		} catch (SQLException se) {
			logger.error("ProcedimentoDelegate: failed eliminaProtocolloProcedimento: ");
			jdbcMan.rollback(connection);

		} finally {
			jdbcMan.close(connection);
		}
		return statusFlag;	
		}
*/
	
	public boolean setProcedimentoSospeso(int procedimentoId,
			String estremiSospensione, AllaccioVO allaccioVO, Utente utente) {
		boolean sospeso = false;
		try {
			if (allaccioVO == null)
				sospeso = procedimentoDAO.setProcedimentoSospeso(
						procedimentoId, estremiSospensione, utente
								.getValueObject().getUsername());
			else
				sospeso = procedimentoDAO.setProcedimentoSospeso(
						procedimentoId, allaccioVO.getProtocolloAllacciatoId(),
						estremiSospensione, utente.getValueObject()
								.getUsername());
		} catch (Exception e) {
			logger.error("Si e' verificata un eccezione non gestita.", e);
		}
		return sospeso;
	}

	public boolean chiudiProcedimentoDaFascicolo(Connection connection,
			int fascicoloId, String username) throws DataException {
		int procedimentoId = procedimentoDAO.getProcedimentoIdDaFascicolo(
				connection, fascicoloId);
		return procedimentoDAO.setProcedimentoChiuso(connection,
				procedimentoId, username);
	}

	public boolean riapriProcedimentoDaFascicolo(Connection connection,
			int fascicoloId, String username) throws DataException {
		int procedimentoId = procedimentoDAO.getProcedimentoIdDaFascicolo(
				connection, fascicoloId);
		return procedimentoDAO.setProcedimentoAperto(connection,
				procedimentoId, username);
	}

	public boolean archiviaProcedimentoDaFascicolo(Connection connection,
			int fascicoloId, String username) throws DataException {
		int procedimentoId = procedimentoDAO.getProcedimentoIdDaFascicolo(
				connection, fascicoloId);
		return procedimentoDAO.setProcedimentoArchiviato(connection,
				procedimentoId, username);
	}

	public boolean riapriProcedimento(int procedimentoId, int fascicoloId,
			Utente utente) {
		boolean chiuso = false;
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			procedimentoDAO.setProcedimentoAperto(connection, procedimentoId,
					utente.getValueObject().getUsername());
			FascicoloDelegate delegate = FascicoloDelegate.getInstance();
			Fascicolo fascicolo = delegate.getFascicoloById(fascicoloId);
			fascicolo.getFascicoloVO().setDataChiusura(null);
			fascicolo.getFascicoloVO().setDataCarico(
					new Date(System.currentTimeMillis()));
			delegate.riapriFascicolo(connection, fascicolo, utente);
			connection.commit();
			chiuso = true;
		} catch (Exception e) {
			logger.error(
					"setProcedimentoChiuso:Si e' verificata un eccezione non gestita.",
					e);
		} finally {
			jdbcMan.close(connection);
		}
		return chiuso;
	}

	public boolean chiudiProcedimento(int procedimentoId, int fascicoloId,
			Utente utente) {
		boolean chiuso = false;
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			procedimentoDAO.setProcedimentoChiuso(connection, procedimentoId,
					utente.getValueObject().getUsername());
			FascicoloDelegate delegate = FascicoloDelegate.getInstance();
			Fascicolo fascicolo = delegate.getFascicoloById(fascicoloId);
			fascicolo.getFascicoloVO().setDataChiusura(
					new Date(System.currentTimeMillis()));
			delegate.chiudiFascicolo(connection, fascicolo, utente);
			connection.commit();
			chiuso = true;
		} catch (Exception e) {
			logger.error(
					"setProcedimentoChiuso:Si e' verificata un eccezione non gestita.",
					e);
		} finally {
			jdbcMan.close(connection);
		}
		return chiuso;
	}

	public boolean inviaProcedimento(int procedimentoId, String posizione,
			Utente utente) {
		boolean chiuso = false;
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			procedimentoDAO.inviaProcedimento(connection, procedimentoId,posizione,utente.getValueObject().getUsername());
			chiuso = true;
		} catch (Exception e) {
			logger.error(
					"inviaProcedimento: Si e' verificata un eccezione non gestita.",
					e);
		} finally {
			jdbcMan.close(connection);
		}
		return chiuso;
	}
	
	public boolean cambiaStatoProcedimento(Connection connection,int procedimentoId, int stato,String posizione,
			Utente utente) {
		boolean chiuso = false;
		try {
			procedimentoDAO.cambiaStatoProcedimento(connection, procedimentoId,stato,posizione,utente.getValueObject().getUsername());
			chiuso = true;
		} catch (Exception e) {
			logger.error(
					"inviaProcedimento: Si e' verificata un eccezione non gestita.",
					e);
		} 
		return chiuso;
	}
	
	public void inviaProcedimento(Connection connection, int procedimentoId, String posizione,
			Utente utente) throws DataException {
			try {
				procedimentoDAO.inviaProcedimento(connection, procedimentoId,posizione,utente.getValueObject().getUsername());
			} catch (DataException e) {
				e.printStackTrace();
				throw new DataException(
						"Errore nel invio Procedimento.");
			}
	}

	public boolean setProcedimentoIstruttoreLavorato(
			int procedimentoId, int caricaId, String username) {
		boolean chiuso = false;
		try {
			chiuso = procedimentoDAO
					.setProcedimentoIstruttoreLavorato(
							procedimentoId, caricaId, username);
		} catch (Exception e) {
			logger.error("Si e' verificata un eccezione non gestita.", e);
		}
		return chiuso;
	}

	public boolean setProcedimentoRiavviato(int procedimentoId,
			String estremiSospensione, String dataScadenza,
			AllaccioVO allaccioVO, Utente utente) {
		boolean sospeso = false;
		try {
			if (allaccioVO == null)
				sospeso = procedimentoDAO.setProcedimentoRiavviato(
						procedimentoId, estremiSospensione, dataScadenza,
						utente.getValueObject().getUsername());
			else
				sospeso = procedimentoDAO.setProcedimentoRiavviato(
						procedimentoId, allaccioVO.getProtocolloAllacciatoId(),
						estremiSospensione, dataScadenza, utente
								.getValueObject().getUsername());
		} catch (Exception e) {
			logger.error("Si e' verificata un eccezione non gestita.", e);
		}
		return sospeso;
	}

	public boolean annullaProcedimentoSospeso(int procedimentoId,
			String estremiSospensione, Utente utente) {
		boolean sospeso = false;
		try {
			sospeso = procedimentoDAO.annullaProcedimentoSospeso(
					procedimentoId, estremiSospensione, utente.getValueObject()
							.getUsername());
		} catch (Exception e) {
			logger.error("Si e' verificata un eccezione non gestita.", e);
		}
		return sospeso;
	}

	public int getIntervalloTempoSospensione(int procedimentoId) {
		int days = 0;
		try {
			days = procedimentoDAO
					.getIntervalloTempoSospensione(procedimentoId);
		} catch (Exception e) {
			logger.error("Si e' verificata un eccezione non gestita.", e);
		}
		return days;
	}

	public int contaProcedimenti(Utente utente, HashMap<String,String> sqlDB, String soggId) {
		try {

			return procedimentoDAO.contaProcedimenti(utente, sqlDB, soggId);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting contaProtocolli: ");
			return 0;
		}

	}

	public SortedMap<Integer, ProcedimentoVO> cercaProcedimenti(Utente utente, HashMap<String,String> sqlDB,
			String soggId) {
		try {
			SortedMap<Integer, ProcedimentoVO> procedimenti = procedimentoDAO
					.cerca(utente, sqlDB, soggId);
			for (Integer id : procedimenti.keySet()) {
				procedimenti.get(id).setVisualizzabile(
						procedimentoDAO.isProcedimentoVisualizzabile(id,
								utente.getCaricaInUso()));
			}
			return procedimenti;
		} catch (DataException de) {
			logger.error("failed getting cercaProcedimenti: ");
			return null;
		}

	}

	public ProcedimentoVO getProcedimentoVO(int id) {
		try {
			return procedimentoDAO.getProcedimentoById(id);
		} catch (DataException de) {
			logger.error("failed getting ProcedimentoVO: " + id);
			return null;
		}

	}

	public Procedimento getProcedimentoById(int procedimentoId) {
		Procedimento p = new Procedimento();
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			ProcedimentoVO vo = procedimentoDAO.getProcedimentoById(connection,
					procedimentoId);
			if (vo.getReturnValue() == ReturnValues.FOUND) {
				ReportProtocolloView pa = ProtocolloDelegate.getInstance()
						.getProtocolloView(connection, vo.getProtocolloId());
				vo.setNumeroProtovollo(pa.getAnnoNumeroProtocollo());
				p.setProcedimentoVO(vo);
				// carico istruttori, riferimenti e uffici Partecipanti
				vo.setIstruttori(procedimentoDAO.getIstruttori(vo.getId()));
				vo.setRiferimentiLegislativi(procedimentoDAO.getRiferimenti(vo.getId()));
				vo.setUfficiPartecipanti(procedimentoDAO.getUfficiPartecipantiMap(vo.getId()));
				// carico faldoni associati
				Collection<Integer> idf = procedimentoDAO.getProcedimentoFaldoni(
						connection, procedimentoId);
				Iterator<Integer> itf = idf.iterator();
				while (itf.hasNext()) {
					Integer id =  itf.next();
					FaldoneVO f = FaldoneDelegate.getInstance().getFaldone(
							connection, id.intValue());
					if (f.getReturnValue() == ReturnValues.FOUND)
						p.getFaldoni().put(String.valueOf(f.getId()), f);
				}
				// carico fascicoli associati
				Collection<Integer> idfa = procedimentoDAO.getProcedimentoFascicoli(
						connection, procedimentoId);
				Iterator<Integer> itfa = idfa.iterator();
				while (itfa.hasNext()) {
					Integer id =  itfa.next();
					FascicoloView f = FascicoloDelegate.getInstance()
							.getFascicoloViewById(connection, id.intValue());
					if (f != null)
						p.getFascicoli().put(String.valueOf(f.getId()), f);
				}
				Map<String, ProtocolloProcedimentoView> proc = procedimentoDAO.getProcedimentoProtocolli(
						connection, procedimentoId);
				if (!proc.isEmpty())
					p.getProtocolli().putAll(proc);

			} else {
				p = null;
			}
		} catch (Exception e) {
			logger.error("errore", e);
		} finally {
			jdbcMan.close(connection);
		}
		return p;
	}

	/*
	public Collection<AssegnatarioVO> getAssegnatariProcedimento(int procedimentoId) {
		try {
			return procedimentoDAO.getAssegnatariProcedimento(procedimentoId);
		} catch (Exception de) {
			logger.error("ProcedimentoDelegate: failed getting getAssegnatariProcedimento: ");
			return null;
		}
	}
	*/
	
	public Map<String,ProcedimentoView> getProcedimentiAlert(Utente utente) {
		try {
			return procedimentoDAO
					.getProcedimentiAlert(utente.getCaricaInUso());
		} catch (Exception de) {
			logger.error("ProcedimentoDelegate: failed getting getProcedimentiAlert: ");
			return null;
		}
	}

	public Map<String,ProcedimentoView> getRicorsi(Utente utente) {
		try {
			return procedimentoDAO.getRicorsi(utente.getCaricaInUso());
		} catch (Exception de) {
			logger.error("ProcedimentoDelegate: failed getting getRicorsi: ");
			return null;
		}
	}



	public Map<String,ProcedimentoView> getProcedimentiIstruttore(int caricaId) {
		try {
			return procedimentoDAO
					.getProcedimentiIstruttore(caricaId);
		} catch (Exception de) {
			logger.error("ProcedimentoDelegate: failed getting getProcedimentiIstruttore: ");
			return null;
		}
	}
	
	public Map<String,ProcedimentoView> getProcedimentiUfficioPartecipante(int ufficioId) {
		try {
			return procedimentoDAO
					.getProcedimentiUfficioPartecipante(ufficioId);
		} catch (Exception de) {
			logger.error("ProcedimentoDelegate: failed getting getProcedimentiUfficioPartecipante: ");
			return null;
		}
	}

	public int contaProcedimentiAlert(Utente utente) {
		try {

			return procedimentoDAO.contaProcedimentiAlert(utente
					.getCaricaInUso());
		} catch (Exception de) {
			logger.error("ProcedimentoDelegate: failed getting contaProcedimentiAlert: ");
			return 0;
		}
	}



	public int contaProcedimentiIstruttore(int caricaId) {
		try {
			return procedimentoDAO
					.contaProcedimentiIstruttore(caricaId);
		} catch (Exception de) {
			logger.error("ProcedimentoDelegate: failed getting contaProcedimentiIstruttore: ");
			return 0;
		}
	}
	
	public int contaProcedimentiUfficioPartecipante(int ufficioId) {
		try {
			return procedimentoDAO.contaProcedimentiUfficioPartecipante(ufficioId);
		} catch (Exception de) {
			logger.error("ProcedimentoDelegate: failed getting contaProcedimentiUfficioPartecipante: ");
			return 0;
		}
	}

	public int contaRicorsi(Utente utente) {
		try {

			return procedimentoDAO.contaRicorsi(utente.getCaricaInUso());
		} catch (Exception de) {
			logger.error("ProcedimentoDelegate: failed getting contaRicorsi: ");
			return 0;
		}
	}

	public boolean cambiaReferenteProcedimento(int procedimentoId,
			AssegnatarioView assegnatario, Utente utente) {
		boolean riassegnato = false;
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			procedimentoDAO.cambiaReferenteProcedimento(connection, procedimentoId,assegnatario.getUfficioId(),assegnatario.getCaricaId(),utente.getValueObject().getUsername());
			procedimentoDAO.deleteIstruttori(connection, procedimentoId);
			connection.commit();
			riassegnato=true;
		} catch (Exception e) {
			logger.error("Si e' verificata un eccezione non gestita.", e);
		} finally {
			jdbcMan.close(connection);
		}
		return riassegnato;
	}
	
	public int getProcedimentoByFascicoloId(int fascicoloId) {
		int procedimentoId = 0;
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			procedimentoId = procedimentoDAO.getProcedimentoIdDaFascicolo(connection, fascicoloId);
		} catch (Exception e) {
			logger.error("Si e' verificata un eccezione non gestita.", e);
		} finally {
			jdbcMan.close(connection);
		}
		return procedimentoId;
	}
	
	public int getFascicoloIdByProcedimento(int procedimentoId) {
		int fascicoloId = 0;
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			fascicoloId = procedimentoDAO.getFascicoloIdDaProcedimento(
					connection, procedimentoId);
		} catch (Exception e) {
			logger.error("Si e' verificata un eccezione non gestita.", e);
		} finally {
			jdbcMan.close(connection);
		}
		return fascicoloId;
	}

	public Collection<ProcedimentoView> getElencoDecreti(Collection<Integer> ids) {
		Collection<ProcedimentoView> proc = null;
		try {
			proc = procedimentoDAO.getElencoDecreti( ids);
			logger.info("getting getElencoDecreti");
		} catch (Exception de) {
			logger.error("EditorDelegate failed getElencoDecreti", de);
		}
		return proc;
	}
	
}