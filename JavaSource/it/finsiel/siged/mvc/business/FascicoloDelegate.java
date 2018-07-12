package it.finsiel.siged.mvc.business;

import it.compit.fenice.enums.TipoVisibilitaUfficioEnum;
import it.compit.fenice.mvc.bo.FascicoloBO;
import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.vo.protocollo.DocumentoFascicoloVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.NomiTabelle;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.Fascicolo;
import it.finsiel.siged.model.InvioFascicolo;
import it.finsiel.siged.model.documentale.Documento;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.integration.FascicoloDAO;
import it.finsiel.siged.mvc.presentation.helper.FascicoloInvioView;
import it.finsiel.siged.mvc.presentation.helper.FascicoloView;
import it.finsiel.siged.mvc.presentation.helper.ProtocolloFascicoloView;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.FaldoneVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.InvioFascicoliDestinatariVO;
import it.finsiel.siged.mvc.vo.protocollo.InvioFascicoliVO;
import it.finsiel.siged.mvc.vo.protocollo.ProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloFascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.finsiel.siged.util.NumberUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

import org.apache.log4j.Logger;


public class FascicoloDelegate {

	private static Logger logger = Logger.getLogger(FascicoloDelegate.class
			.getName());

	private FascicoloDAO fascicoloDAO = null;

	private static FascicoloDelegate delegate = null;

	private FascicoloDelegate() {
		try {
			if (fascicoloDAO == null) {
				fascicoloDAO = (FascicoloDAO) DAOFactory
						.getDAO(Constants.FASCICOLO_DAO_CLASS);

				logger.debug("fascicoloDAO instantiated:"
						+ Constants.FASCICOLO_DAO_CLASS);
			}
		} catch (Exception e) {
			logger.error("Exception while connecting to UserDAOjdbc!!", e);
		}
	}

	public static FascicoloDelegate getInstance() {
		if (delegate == null)
			delegate = new FascicoloDelegate();
		return delegate;
	}

	public static String getIdentifier() {
		return Constants.FASCICOLO_DELEGATE;
	}

	public SortedMap<Integer,FascicoloInvioView> getFascicoliArchivioInvio(int aooId) {
		try {

			return fascicoloDAO.getFascicoliArchivioInvio(aooId);
		} catch (DataException de) {
			logger.error("FascicoloDelegate: failed getting getFascicoliArchivioInvio: ");
			return null;
		}

	}

	public Collection<ProtocolloFascicoloView> getProtocolliFascicolo(int fascicoloId, Utente utente) {
		try {
			return fascicoloDAO.getProtocolliFascicolo(fascicoloId, utente);
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getting getProtocolliFascicolo: ");
			return null;
		}
	}

	public Collection<FascicoloView> getFascicoli(Utente utente, String progressivo, int anno,
			String oggetto, String note, String stato, int titolarioId,
			Date dataAperturaDa, Date dataAperturaA, Date dataEvidenzaDa,
			Date dataEvidenzaA, int ufficioId, int referenteId,
			int istruttoreId, String interessatoDelegato, String comune, String capitolo,String collocazioneValore1, String collocazioneValore2, String collocazioneValore3, String collocazioneValore4)

	{
		try {
			return fascicoloDAO.getFascicoli(utente, progressivo, anno,
					oggetto, note, stato, titolarioId, dataAperturaDa,
					dataAperturaA, dataEvidenzaDa, dataEvidenzaA, ufficioId,
					referenteId, istruttoreId, interessatoDelegato, comune, capitolo, collocazioneValore1, collocazioneValore2, collocazioneValore3, collocazioneValore4);
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getting getFascicoli: ");
			return null;
		}
	}

	public int contaFascicoli(Utente utente, String progressivo, int anno,
			String oggetto, String note, String stato, int titolarioId,
			Date dataAperturaDa, Date dataAperturaA, Date dataEvidenzaDa,
			Date dataEvidenzaA, int ufficioId, int referenteId,
			int istruttoreId, String interessatoDelegato, String comune, String capitolo,String collocazioneValore1, String collocazioneValore2, String collocazioneValore3, String collocazioneValore4)

	{
		try {
			return fascicoloDAO.contaFascicoli(utente, progressivo, anno,
					oggetto, note, stato, titolarioId, dataAperturaDa,
					dataAperturaA, dataEvidenzaDa, dataEvidenzaA, ufficioId,
					referenteId, istruttoreId, interessatoDelegato, comune, capitolo, collocazioneValore1, collocazioneValore2, collocazioneValore3, collocazioneValore4);
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getting contaFascicoli: ");
			return 0;
		}
	}

	public int contaFascicoliPerDeposito(Utente utente, Date dataDa, Date dataA)

	{
		try {
			return fascicoloDAO
					.contaFascicoliPerDeposito(utente, dataDa, dataA);
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getting contaFascicoliPerDeposito: ");
			return 0;
		}
	}

	public Map<Integer,FascicoloView> getFascicoliPerDeposito(Utente utente, Date dataDa, Date dataA)

	{
		try {
			return fascicoloDAO.getFascicoliPerDeposito(utente, dataDa, dataA);
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getting getFascicoliPerDeposito: ");
			return null;
		}
	}

	public Collection<FascicoloVO> getFascicoliByAooId(int aooId)

	{
		try {
			return fascicoloDAO.getFascicoloByAooId(aooId);
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getting getFascicoliByAooId: ");
			return null;
		}
	}

	public FascicoloVO getFascicoloVOById(int fascicoloId) {
		try {
			FascicoloVO f = fascicoloDAO.getFascicoloById(fascicoloId);
			return f;
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getting getFascicoloVOById: ");
			return null;
		}
	}
	
	public FascicoloVO getFascicoloVOByCodice(String codice) {
		try {
			FascicoloVO f = fascicoloDAO.getFascicoloByCodice(codice);
			return f;
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getting getFascicoloVOByCodice ");
			return null;
		}
	}
	
	public Integer getTitolarioByFascicoloId(int fascicoloId) {
		try {
			Integer t = fascicoloDAO.getTitolarioByFascicoloId(fascicoloId);
			return t;
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getting getTitolarioByFascicoloId: ");
			return null;
		}
	}

	public FascicoloVO getFascicoloVOById(Connection connection, int fascicoloId) {
		try {
			FascicoloVO f = fascicoloDAO.getFascicoloById(connection,
					fascicoloId);
			return f;
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getting getFascicoloVOById: ");
			return null;
		}
	}

	public FascicoloView getFascicoloViewById(int fascicoloId) {
		try {
			return fascicoloDAO.getFascicoloViewById(fascicoloId);
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getting getFascicoloViewById: ");
			return null;
		}
	}

	public FascicoloView getFascicoloViewById(Connection connection,
			int fascicoloId) {
		try {
			return fascicoloDAO.getFascicoloViewById(connection, fascicoloId);
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getting getFascicoloViewById: ");
			return null;
		}
	}

	public Collection<Integer> getProtocolloFascicoloByIdAndNumeroProtocollo(
			int fascicoloId, int numProt) {
		try {
			return fascicoloDAO.getProtocolloFascicoloByIdAndNumeroProtocollo(
					fascicoloId, numProt);
		} catch (DataException e) {
			logger.error("FascicoloDelegate: failed getting getProtocolloFascicoloByIdAndNumeroProtocollo: ");
			return null;
		}
	}

	public Fascicolo getFascicoloById(int fascicoloId) {

		try {
			Fascicolo f = new Fascicolo();
			f.setFascicoloVO(fascicoloDAO.getFascicoloById(fascicoloId));
			f.setProtocolli(fascicoloDAO.getProtocolliFascicoloById(fascicoloId));
			f.setDocumenti(fascicoloDAO.getDocumentiFascicoloById(fascicoloId));
			
			// gestione faldoni associati al fascicolo
			Collection<FaldoneVO> faldoniFascicolo = new ArrayList<FaldoneVO>();
			Iterator<Integer> itFal = fascicoloDAO.getFaldoniFascicoloById(fascicoloId)
					.iterator();
			FaldoneDelegate fal = FaldoneDelegate.getInstance();
			while (itFal.hasNext()) {
				Integer faldoneId = (Integer) itFal.next();
				FaldoneVO faldoneVO = fal.getFaldone(faldoneId.intValue());
				faldoniFascicolo.add(faldoneVO);
			}
			f.setFaldoni(faldoniFascicolo);

			Collection<ProcedimentoVO> procedimentiFascicolo = new ArrayList<ProcedimentoVO>();
			Iterator<Integer> itProc = fascicoloDAO.getProcedimentiFascicoloById(
					fascicoloId).iterator();
			ProcedimentoDelegate proc = ProcedimentoDelegate.getInstance();
			while (itProc.hasNext()) {
				Integer procedimentoId = (Integer) itProc.next();
				ProcedimentoVO procedimentoVO = proc
						.getProcedimentoVO(procedimentoId.intValue());
				procedimentiFascicolo.add(procedimentoVO);
			}
			f.setProcedimenti(procedimentiFascicolo);
			
			return f;
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getting getFascicoloById: ");
			return null;
		}
	}

	public Collection<FascicoloVO> getFascicoliByProtocolloId(int protocolloId) {
		try {
			return fascicoloDAO.getFascicoliByProtocolloId(protocolloId);
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getting getFascicoliByProtocolloId: ");
			return null;
		}
	}

	public Collection<Integer> getCollegatiIdByFascicoloId(int fascicoloId) {
		try {
			return fascicoloDAO.getCollegatiIdByFascicoloId(fascicoloId);
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getting getCollegatiIdByFascicoloId: ");
			return null;
		}
	}

	public Collection<Integer> getSottoFascicoliIdByFascicoloId(int fascicoloId) {
		try {
			return fascicoloDAO.getSottoFascicoliIdByFascicoloId(fascicoloId);
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getting getCollegatiIdByFascicoloId: ");
			return null;
		}
	}

	public Collection<FascicoloVO> getFascicoliByDocumentoId(int documentoId) {
		try {
			return fascicoloDAO.getFascicoliByDocumentoId(documentoId);
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getting getFascicoliByDocumentoId: ");
			return null;
		}
	}

	public Collection<FascicoloVO> getFascicoliByDocumentoId(Connection connection,
			int documentoId) throws DataException {
		return fascicoloDAO.getFascicoliByDocumentoId(connection, documentoId);
	}

	public Collection<FascicoloVO> getStoriaFascicoliByDocumentoId(Connection connection,
			int documentoId, int versione) throws DataException {
		return fascicoloDAO.getStoriaFascicoliByDocumentoId(connection,
				documentoId, versione);
	}

	public Map<String,DestinatarioVO> getDestinatariFascicoliInvio(int fascicoloId) {
		try {
			return fascicoloDAO.getDestinatariFascicoliInvio(fascicoloId);
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getting getDestinatariFascicoliInvio: ");
			return null;
		}
	}

	public Map<String,DestinatarioVO> getDestinatariFascicoliInvio(Connection connection,
			int fascicoloId) {
		try {
			return fascicoloDAO.getDestinatariFascicoliInvio(connection,
					fascicoloId);
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getting getDestinatariFascicoliInvio: ");
			return null;
		}
	}

	public void salvaFascicoloDocumento(FascicoloVO fascicoloVO,
			int documentoId, String userName, int ufficioProprietarioId) throws Exception {

		JDBCManager jdbcMan = null;
		Connection connection = null;

		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			salvaFascicoloDocumento(connection, fascicoloVO, documentoId,
					userName, ufficioProprietarioId);
			connection.commit();

		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio FascicoloDocumento fallito, rolling back transction..",
					de);

		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio FascicoloDocumento fallito, rolling back transction..",
					se);

		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);

		} finally {
			jdbcMan.close(connection);
		}

	}

	public int eliminaCodaInvioFascicolo(Connection connection, int fascicoloId)
			throws DataException {
		int recUpdate = 0;
		try {
			recUpdate = fascicoloDAO.eliminaCodaInvioFascicolo(connection,
					fascicoloId);
		} catch (DataException e) {
			e.printStackTrace();
			throw new DataException("Errore eliminaCodaInvioFascicolo.");
		}
		return recUpdate;
	}

	public int aggiornaStatoFascicolo(Connection connection, int fascicoloId,
			int stato, String userName, int versione) throws DataException {
		int recUpdate = 0;
		try {
			recUpdate = fascicoloDAO.aggiornaStatoFascicolo(connection,
					fascicoloId, stato, userName, versione);
		} catch (DataException e) {
			e.printStackTrace();
			throw new DataException("Errore aggiornaStatoFascicolo.");
		}
		return recUpdate;
	}

	public int setStatoFascicoloVersamentoDeposito(Connection connection,
			int fascicoloId, String userName, int versione)
			throws DataException {
		int recUpdate = 0;
		try {
			recUpdate = fascicoloDAO.aggiornaStatoFascicolo(connection,
					fascicoloId, 4, userName, versione);
			ProcedimentoDelegate.getInstance().archiviaProcedimentoDaFascicolo(
					connection, fascicoloId, userName);
		} catch (DataException e) {
			e.printStackTrace();
			throw new DataException(
					"Errore setStatoFascicoloVersamentoDeposito.");
		}
		return recUpdate;
	}

	public boolean aggiungiCollegamentoFascicolo(int fascicoloId,
			int fascicoloDaCollegare, String userName) throws DataException {
		boolean updated = false;
		JDBCManager jdbcMan = null;
		Connection connection = null;

		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			if (!fascicoloDAO.esisteCollegamento(connection, fascicoloId,
					fascicoloDaCollegare))
				fascicoloDAO.aggiungiCollegamentoFascicolo(connection,
						fascicoloId, fascicoloDaCollegare, userName);
			if (!fascicoloDAO.esisteCollegamento(connection,
					fascicoloDaCollegare, fascicoloId))
				fascicoloDAO.aggiungiCollegamentoFascicolo(connection,
						fascicoloDaCollegare, fascicoloId, userName);
			connection.commit();
			updated = true;
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.error(
					"Salvataggio FascicoloCollegamento fallito, rolling back transction..",
					de);
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.error(
					"Salvataggio FascicoloCollegamento fallito, rolling back transction..",
					se);
		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.error("Si e' verificata un eccezione non gestita.", e);
		} finally {
			jdbcMan.close(connection);
		}
		return updated;
	}

	public void salvaFascicoloDocumento(Connection connection,
			FascicoloVO fascicoloVO, int documentoId, String userName, int ufficioProprietarioId)
			throws Exception {
		try {
			if (fascicoloVO.getId().intValue() == 0) {
				fascicoloVO.setId(IdentificativiDelegate.getInstance()
						.getNextId(connection, NomiTabelle.FASCICOLI));
				int progressivo = fascicoloDAO
						.getMaxProgressivo(connection,
								fascicoloVO.getTitolarioId(),
								fascicoloVO.getParentId());
				if (fascicoloVO.getParentId() != 0)
					fascicoloVO.setPathProgressivo(FascicoloBO
							.getProgressivo(fascicoloVO.getParentId())
							+ "/"
							+ progressivo);
				else
					fascicoloVO.setPathProgressivo(String.valueOf(progressivo));
				fascicoloVO = fascicoloDAO
						.newFascicolo(connection, fascicoloVO);
			} else {
				fascicoloVO = fascicoloDAO.getFascicoloById(connection,
						fascicoloVO.getId().intValue());
			}
			fascicoloDAO.salvaDocumentoFascicolo(connection, fascicoloVO,
					documentoId, userName, ufficioProprietarioId);
		} catch (DataException e) {
			e.printStackTrace();
			throw new DataException(
					"Errore nel salvataggio del Fascicolo-Documento.");
		}
	}

	public void salvaFascicoloProtocollo(Connection connection,
			FascicoloVO fascicoloVO, int protocolloId, String userName, int ufficioProprietarioId)
			throws DataException {
		FascicoloVO fascicoloSalvato = new FascicoloVO();
		int fascicoloId;
		
			if (fascicoloVO.getId().intValue() == 0) {
				fascicoloVO.setId(IdentificativiDelegate.getInstance()
						.getNextId(connection, NomiTabelle.FASCICOLI));
				Date dataCorrente = new Date(System.currentTimeMillis());
				fascicoloVO.setDataUltimoMovimento(dataCorrente);
				int progressivo = fascicoloDAO
						.getMaxProgressivo(connection,
								fascicoloVO.getTitolarioId(),
								fascicoloVO.getParentId());
				fascicoloVO.setProgressivo(progressivo);
				if (fascicoloVO.getParentId() != 0)
					fascicoloVO.setPathProgressivo(FascicoloBO
							.getProgressivo(fascicoloVO.getParentId())
							+ "/"
							+ progressivo);
				else
					fascicoloVO.setPathProgressivo(String.valueOf(progressivo));
				fascicoloSalvato = fascicoloDAO.newFascicolo(connection,
						fascicoloVO);

				fascicoloId = fascicoloSalvato.getId().intValue();
			} else {
				fascicoloId = fascicoloVO.getId().intValue();
				// controllo la versione di fascicoloVO con quella presente in
				// base dati
				fascicoloVO = fascicoloDAO.getFascicoloById(connection,
						fascicoloId);
			}
			// salvo i dati della relazione tra fascicolo e protocollo
			fascicoloDAO.salvaProtocolloFascicolo(connection, fascicoloId,
					protocolloId, userName, ufficioProprietarioId, fascicoloVO.getVersione());

			// modifico la data movimentazione del fascicolo
			Date dataCorrente = new Date(System.currentTimeMillis());
			fascicoloVO.setDataUltimoMovimento(dataCorrente);
			fascicoloDAO.aggiornaFascicolo(connection, fascicoloVO);
	}

	public int cancellaFascicolo(int fascicoloId) throws Exception {
		try {
			if (fascicoloId > 0) {
				return fascicoloDAO.deleteFascicolo(fascicoloId);
			}
		} catch (DataException e) {
			e.printStackTrace();
			throw new DataException("Errore cancellaFascicolo.");
		}
		return ReturnValues.UNKNOWN;
	}

	public void rimuoviFascicoliProtocollo(Connection connection,
			int protocolloId) throws Exception {
		try {
			if (protocolloId > 0) {
				fascicoloDAO
						.deleteFascicoliProtocollo(connection, protocolloId);
			}
		} catch (DataException e) {
			e.printStackTrace();
			throw new DataException("Errore rimuoviFascicoliProtocollo.");
		}
	}

	public void rimuoviDocumentoDaFascicolo(int fascicoloId, int documentoId,
			int versione) throws Exception {
		try {
			if (documentoId > 0) {
				fascicoloDAO.deleteDocumentoFascicolo(fascicoloId, documentoId,
						versione);
			}
		} catch (DataException e) {
			e.printStackTrace();
			throw new DataException("Errore rimuoviDocumentoDaFascicolo.");
		}
	}

	public void rimuoviFascicoliDocumento(Connection connection, int documentoId)
			throws Exception {
		try {
			if (documentoId > 0) {
				fascicoloDAO.deleteFascicoliDocumento(connection, documentoId);
			}
		} catch (DataException e) {
			e.printStackTrace();
			throw new DataException("Errore rimuoviFascicoliDocumento.");
		}
	}

	public FascicoloVO salvaFascicolo(FascicoloVO fascicolo) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		FascicoloVO fascicoloSalvato = null;
		CaricaDelegate caricaDelegate = CaricaDelegate.getInstance();
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			
			if (fascicolo.getUtenteIstruttoreId() != 0){
				fascicolo.setCaricaIstruttoreId(caricaDelegate
						.getCaricaByUtenteAndUfficio(
								fascicolo.getUtenteIstruttoreId(),
								fascicolo.getUfficioIntestatarioId())
						.getCaricaId());
			}
			if (fascicolo.getUtenteIntestatarioId() != 0)
				fascicolo.setCaricaIntestatarioId(caricaDelegate
						.getCaricaByUtenteAndUfficio(
								fascicolo.getUtenteIntestatarioId(),
								fascicolo.getUfficioIntestatarioId())
						.getCaricaId());
			if (isParent(fascicolo.getId().intValue())
					&& isTitolarioChanged(fascicolo.getId().intValue(),
							fascicolo.getTitolarioId()))
				fascicoloDAO.aggiornaTitolarioInSottofascicoli(connection,
						fascicolo.getId().intValue(),
						fascicolo.getTitolarioId());
			fascicoloSalvato = fascicoloDAO.aggiornaFascicolo(connection,
					fascicolo);
			connection.commit();
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.error("FascicoloDelegate: failed salvaFascicolo: ");
			de.printStackTrace();
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
		} catch (Exception se) {
			jdbcMan.rollback(connection);
		} finally {
			jdbcMan.close(connection);
		}
		return fascicoloSalvato;
	}

	public Documento versamentoDeposito(Utente utente,
			Collection<FascicoloView> fascicoli, Documento doc) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			for (FascicoloView f : fascicoli)
				setStatoFascicoloVersamentoDeposito(connection, f.getId(),
						utente.getValueObject().getUsername(), f.getVersione());
			DocumentaleDelegate dd = DocumentaleDelegate.getInstance();
			int cartellaId = dd.getCartellaVOByCaricaId(
					doc.getFileVO().getCaricaLavId()).getId();
			doc.getFileVO().setCartellaId(cartellaId);
			doc = dd.salvaDocumento(connection, doc, utente);
			connection.commit();
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.error("FascicoloDelegate: failed versamentoDeposito: ");
			de.printStackTrace();
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
		} catch (Exception se) {
			jdbcMan.rollback(connection);
		} finally {
			jdbcMan.close(connection);
		}
		return doc;
	}

	public synchronized FascicoloVO nuovoFascicolo(FascicoloVO fascicolo) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		FascicoloVO fascicoloSalvato = new FascicoloVO();
		CaricaDelegate caricaDelegate = CaricaDelegate.getInstance();
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			if (fascicolo.getUtenteIstruttoreId() != 0)
				fascicolo.setCaricaIstruttoreId(caricaDelegate
						.getCaricaByUtenteAndUfficio(
								fascicolo.getUtenteIstruttoreId(),
								fascicolo.getUfficioIntestatarioId())
						.getCaricaId());
			if (fascicolo.getUtenteIntestatarioId() != 0)
				fascicolo.setCaricaIntestatarioId(caricaDelegate
						.getCaricaByUtenteAndUfficio(
								fascicolo.getUtenteIntestatarioId(),
								fascicolo.getUfficioIntestatarioId())
						.getCaricaId());
			fascicolo.setId(IdentificativiDelegate.getInstance().getNextId(
					connection, NomiTabelle.FASCICOLI));
			int progressivo = fascicoloDAO.getMaxProgressivo(connection,
					fascicolo.getTitolarioId(), fascicolo.getParentId());
			fascicolo.setProgressivo(progressivo);
			if (fascicolo.getParentId() != 0)
				fascicolo.setPathProgressivo(FascicoloBO
						.getProgressivo(fascicolo.getParentId())
						+ "/"
						+ progressivo);
			else
				fascicolo.setPathProgressivo(String.valueOf(progressivo));
			fascicoloSalvato = fascicoloDAO.newFascicolo(connection, fascicolo);
			connection.commit();
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.error("FascicoloDelegate: failed nuovoFascicolo: ");
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
		} catch (Exception se) {
			jdbcMan.rollback(connection);
		} finally {
			jdbcMan.close(connection);
		}
		return fascicoloSalvato;
	}

	public synchronized FascicoloVO nuovoFascicolo(Connection connection,
			FascicoloVO fascicolo) throws Exception {
		FascicoloVO fascicoloSalvato = new FascicoloVO();
		try {
			CaricaDelegate caricaDelegate = CaricaDelegate.getInstance();
			if (fascicolo.getUtenteIntestatarioId() != 0)
				fascicolo.setCaricaIntestatarioId(caricaDelegate
						.getCaricaByUtenteAndUfficio(
								fascicolo.getUtenteIntestatarioId(),
								fascicolo.getUfficioIntestatarioId())
						.getCaricaId());
			fascicolo.setId(IdentificativiDelegate.getInstance().getNextId(
					connection, NomiTabelle.FASCICOLI));
			int progressivo = fascicoloDAO.getMaxProgressivo(connection,
					fascicolo.getTitolarioId(), fascicolo.getParentId());
			if (fascicolo.getParentId() != 0)
				fascicolo.setPathProgressivo(FascicoloBO
						.getProgressivo(fascicolo.getParentId())
						+ "/"
						+ progressivo);
			else
				fascicolo.setPathProgressivo(String.valueOf(progressivo));
			fascicolo.setProgressivo(progressivo);
			fascicoloSalvato = fascicoloDAO.newFascicolo(connection, fascicolo);

		} catch (DataException de) {
			logger.error("FascicoloDelegate: failed nuovoFascicolo: ");
			throw new DataException("Errore nel salvataggio del Fascicolo.");
		}
		return fascicoloSalvato;
	}

	public int chiudiFascicolo(Fascicolo fascicolo, Utente utente)
			throws DataException {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		int statusFlag = ReturnValues.UNKNOWN;
		logger.info("FascicoloDelegate:chiudiFascicolo");

		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			fascicolo.getFascicoloVO().setStato(1);
			fascicolo.getFascicoloVO().setDataScarico(
					new Date(System.currentTimeMillis()));
			fascicoloDAO.aggiornaFascicolo(connection,
					fascicolo.getFascicoloVO());
			ProcedimentoDelegate.getInstance().chiudiProcedimentoDaFascicolo(
					connection, fascicolo.getFascicoloVO().getId(),
					utente.getValueObject().getUsername());
			connection.commit();
			statusFlag = ReturnValues.SAVED;
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			statusFlag = ReturnValues.INVALID;
			logger.error("FascicoloDelegate: failed chiudiFascicolo: ");
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
		} finally {
			jdbcMan.close(connection);
		}
		return statusFlag;
	}

	public int riapriFascicolo(Connection connection, Fascicolo fascicolo,
			Utente utente) throws DataException {
		int statusFlag = ReturnValues.UNKNOWN;
		logger.info("FascicoloDelegate:riapriFascicolo");

		try {
			fascicolo.getFascicoloVO().setStato(0);
			fascicolo.getFascicoloVO().setDataScarico(
					new Date(System.currentTimeMillis()));

			fascicoloDAO.aggiornaFascicolo(connection,
					fascicolo.getFascicoloVO());
			statusFlag = ReturnValues.SAVED;
		} catch (Exception se) {
			statusFlag = ReturnValues.INVALID;
			throw new DataException("Errore nella riapertura del Fascicolo.");
		}
		return statusFlag;
	}

	public int chiudiFascicolo(Connection connection, Fascicolo fascicolo,
			Utente utente) throws DataException {
		int statusFlag = ReturnValues.UNKNOWN;
		logger.info("FascicoloDelegate:chiudiFascicolo");
		try {
			fascicolo.getFascicoloVO().setStato(1);
			fascicolo.getFascicoloVO().setDataScarico(
					new Date(System.currentTimeMillis()));
			fascicoloDAO.aggiornaFascicolo(connection,
					fascicolo.getFascicoloVO());
			statusFlag = ReturnValues.SAVED;
		} catch (Exception de) {
			statusFlag = ReturnValues.INVALID;
			throw new DataException("Errore nella chiusura del Fascicolo.");
		}
		return statusFlag;
	}

	public int riapriFascicolo(Fascicolo fascicolo, Utente utente)
			throws DataException {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		int statusFlag = ReturnValues.UNKNOWN;
		logger.info("FascicoloDelegate:riapriFascicolo");

		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			fascicolo.getFascicoloVO().setStato(0);
			fascicolo.getFascicoloVO().setDataScarico(
					new Date(System.currentTimeMillis()));
			fascicoloDAO.aggiornaFascicolo(connection,
					fascicolo.getFascicoloVO());
			ProcedimentoDelegate.getInstance().riapriProcedimentoDaFascicolo(
					connection, fascicolo.getFascicoloVO().getId(),
					utente.getValueObject().getUsername());
			connection.commit();
			statusFlag = ReturnValues.SAVED;
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			statusFlag = ReturnValues.INVALID;
			logger.error("FascicoloDelegate: failed riapriFascicolo: ");
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
		} finally {
			jdbcMan.close(connection);
		}
		return statusFlag;
	}

	public int inviaFascicolo(InvioFascicolo invioFascicolo, String userName,
			int versione) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		int statusFlag = 0;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			for (Iterator<InvioFascicoliVO> i = invioFascicolo.getDocumenti().iterator(); i
					.hasNext();) {
				InvioFascicoliVO ifVO = (InvioFascicoliVO) i.next();
				ifVO.setId(IdentificativiDelegate.getInstance().getNextId(
						connection, NomiTabelle.INVIO_FASCICOLI));

				fascicoloDAO.salvaDocumentiInvioFascicolo(connection, ifVO);
			}
			for (Iterator<DestinatarioVO> y = invioFascicolo.getDestinatariCollection()
					.iterator(); y.hasNext();) {
				InvioFascicoliDestinatariVO ifdVO = new InvioFascicoliDestinatariVO();
				DestinatarioVO destinatario = (DestinatarioVO) y.next();
				ifdVO.setFascicoloId(invioFascicolo.getFascicoloId());
				ifdVO.setId(IdentificativiDelegate.getInstance().getNextId(
						connection, NomiTabelle.INVIO_FASCICOLI_DESTINATARI));
				ifdVO.setDestinatario(destinatario);

				fascicoloDAO.salvaDestinatariInvioFascicolo(connection, ifdVO);
			}
			statusFlag = fascicoloDAO.aggiornaStatoFascicolo(connection,
					invioFascicolo.getFascicoloId(),
					Parametri.STATO_FASCICOLO_INVIATO, userName, versione);
			connection.commit();
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.error("FascicoloDelegate: failed inviaFascicolo: ");
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
		} finally {
			jdbcMan.close(connection);
		}
		return statusFlag;
	}

	public int annullaInvioFascicolo(int fascicoloId, String userName,
			int versione) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		int statusFlag = 0;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);

			fascicoloDAO.annullaInvioFascicolo(connection, fascicoloId,
					versione);
			statusFlag = fascicoloDAO.aggiornaStatoFascicolo(connection,
					fascicoloId, Parametri.STATO_FASCICOLO_APERTO, userName,
					versione);
			connection.commit();
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.error("FascicoloDelegate: failed annullaInvioFascicolo: ");
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
		} finally {
			jdbcMan.close(connection);
		}
		return statusFlag;
	}

	public int scartaFascicolo(int fascicoloId, String destinazioneScarto,
			String userName, int versione) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		int statusFlag = 0;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			statusFlag = fascicoloDAO.scartaFascicolo(connection, fascicoloId,
					destinazioneScarto, userName, versione);
			connection.commit();
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			statusFlag = ReturnValues.INVALID;
			logger.error("FascicoloDelegate: failed scartaFascicolo: ");
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
		} finally {
			jdbcMan.close(connection);
		}
		return statusFlag;
	}

	public boolean esisteFascicoloInCodaInvio(int fascicoloId) {
		try {
			return fascicoloDAO.esisteFascicoloInCodaInvio(fascicoloId);
		} catch (DataException de) {
			logger.error("FascicoloDelegate: failed esisteFascicoloInCodaInvio: ");
		}
		return false;
	}

	public InvioFascicolo getFascicoloInviatoById(int fascicoloId) {
		try {
			InvioFascicolo fInviato = new InvioFascicolo();
			fInviato.setFascicoloId(fascicoloId);

			// gestione protocolli del fascicolo
			Collection<ProtocolloVO> protocolliFascicolo = new ArrayList<ProtocolloVO>();
			Iterator<ProtocolloFascicoloVO> itProt = fascicoloDAO.getProtocolliFascicoloById(
					fascicoloId).iterator();
			ProtocolloDelegate pd = ProtocolloDelegate.getInstance();
			while (itProt.hasNext()) {
				ProtocolloFascicoloVO vo = (ProtocolloFascicoloVO) itProt.next();
				ProtocolloVO pVO = pd.getProtocolloVOById(vo.getId());
				protocolliFascicolo.add(pVO);
			}
			fInviato.setProtocolli(protocolliFascicolo);

			// gestione documenti del fascicolo
			Collection<InvioFascicoliVO> documentiFascicolo = new ArrayList<InvioFascicoliVO>();
			Iterator<InvioFascicoliVO> itDoc = fascicoloDAO.getDocumentiFascicoliInvio(
					fascicoloId).iterator();
			while (itDoc.hasNext()) {
				InvioFascicoliVO ifVO = (InvioFascicoliVO) itDoc.next();
				documentiFascicolo.add(ifVO);
			}

			fInviato.setDocumenti(documentiFascicolo);

			// destinatari fascicolo
			Map<String, DestinatarioVO> destinatari = FascicoloDelegate.getInstance()
					.getDestinatariFascicoliInvio(fascicoloId);
			fInviato.setDestinatari(destinatari);

			return fInviato;
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getting getFascicoloInviatoById: ");
			return null;
		}
	}

	public Collection<FascicoloView> getStoriaFascicolo(int fascicoloId) {
		try {
			return fascicoloDAO.getStoriaFascicolo(fascicoloId);
		} catch (DataException de) {
			logger.error("FascicoloDelegate: failed getting getStoriaFascicolo: ");
			return null;
		}
	}

	public int salvaProtocolliFascicolo(FascicoloVO fascicoloVO,
			String[] protocolli, String userName,int ufficioProprietarioId) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		int statusFlag = ReturnValues.UNKNOWN;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			if (protocolli != null) {
				// salvo i dati della relazione tra fascicolo e protocollo
				fascicoloVO.setDataUltimoMovimento(new Date(System
						.currentTimeMillis()));
				fascicoloVO = fascicoloDAO.aggiornaFascicolo(connection,
						fascicoloVO);

				for (int i = 0; i < protocolli.length; i++) {
					fascicoloDAO.deleteFascicoloProtocollo(connection,
							(new Integer(protocolli[i])).intValue(),
							fascicoloVO.getId().intValue());
					fascicoloDAO.salvaProtocolloFascicolo(connection,
							fascicoloVO.getId().intValue(), (new Integer(
									protocolli[i])).intValue(), userName, ufficioProprietarioId,
							fascicoloVO.getVersione());
				}
			}
			connection.commit();
			statusFlag = ReturnValues.SAVED;
		} catch (DataException de) {
			logger.error("FascicoloDelegate: failed salvaProtocolliFascicolo: ");
			jdbcMan.rollback(connection);

		} catch (SQLException se) {
			logger.error("FascicoloDelegate: failed salvaProtocolliFascicolo: ");
			jdbcMan.rollback(connection);

		} finally {
			jdbcMan.close(connection);
		}
		return statusFlag;
	}

	public int eliminaProtocolliFascicolo(FascicoloVO fascicoloVO,
			String[] protocolli, String userName) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		int statusFlag = ReturnValues.UNKNOWN;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			if (protocolli != null) {
				// salvo i dati della relazione tra fascicolo e protocollo
				fascicoloVO.setDataUltimoMovimento(new Date(System
						.currentTimeMillis()));
				fascicoloDAO.aggiornaFascicolo(connection, fascicoloVO);

				for (int i = 0; i < protocolli.length; i++) {
					fascicoloDAO.deleteFascicoloProtocollo(connection,
							(new Integer(protocolli[i])).intValue(),
							fascicoloVO.getId().intValue());
				}
			}
			connection.commit();
			statusFlag = ReturnValues.SAVED;
		} catch (DataException de) {
			logger.error("FascicoloDelegate: failed eliminaProtocolliFascicolo: ");
			jdbcMan.rollback(connection);

		} catch (SQLException se) {
			logger.error("FascicoloDelegate: failed eliminaProtocolliFascicolo: ");
			jdbcMan.rollback(connection);

		} finally {
			jdbcMan.close(connection);
		}
		return statusFlag;
	}

	public int eliminaProtocolloFascicolo(Connection connection,
			FascicoloVO fascicoloVO, int protocolloId, String userName)
			throws DataException {
		int statusFlag = ReturnValues.UNKNOWN;
		if (protocolloId != 0) {
			fascicoloVO.setDataUltimoMovimento(new Date(System.currentTimeMillis()));
			fascicoloDAO.aggiornaFascicolo(connection, fascicoloVO);
			fascicoloDAO.deleteFascicoloProtocollo(connection, protocolloId,
					fascicoloVO.getId().intValue());
		}
		statusFlag = ReturnValues.SAVED;
		return statusFlag;
	}

	public int salvaProcedimentiFascicolo(FascicoloVO fascicoloVO,
			String[] procedimenti, String userName) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		int statusFlag = ReturnValues.UNKNOWN;
		ProcedimentoDelegate pd = ProcedimentoDelegate.getInstance();
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			if (procedimenti != null) {
				for (int i = 0; i < procedimenti.length; i++) {
					pd.salvaProcedimentoFascicolo(connection, fascicoloVO
							.getId().intValue(), (new Integer(procedimenti[i]))
							.intValue(), userName);
				}
			}
			connection.commit();
			statusFlag = ReturnValues.SAVED;
		} catch (DataException de) {
			logger.error("FascicoloDelegate: failed salvaProcedimentiFascicolo: ");
			jdbcMan.rollback(connection);

		} catch (SQLException se) {
			logger.error("FascicoloDelegate: failed salvaProcedimentiFascicolo: ");
			jdbcMan.rollback(connection);

		} finally {
			jdbcMan.close(connection);
		}
		return statusFlag;
	}

	
	public int eliminaProcedimentiFascicolo(FascicoloVO fascicoloVO,
			String[] procedimenti, String userName) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		int statusFlag = ReturnValues.UNKNOWN;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			if (procedimenti != null) {
				for (int i = 0; i < procedimenti.length; i++) {
					ProcedimentoDelegate.getInstance()
							.deleteFascicoloProcedimento(connection,
									(new Integer(procedimenti[i])).intValue(),
									fascicoloVO.getId().intValue());

				}
			}
			connection.commit();
			statusFlag = ReturnValues.SAVED;
		} catch (DataException de) {
			logger.error("FascicoloDelegate: failed eliminaProcedimentiFascicolo: ");
			jdbcMan.rollback(connection);

		} catch (SQLException se) {
			logger.error("FascicoloDelegate: failed eliminaProcedimentiFascicolo: ");
			jdbcMan.rollback(connection);

		} finally {
			jdbcMan.close(connection);
		}
		return statusFlag;
	}

	public int eliminaProcedimentoFascicolo(FascicoloVO fascicoloVO,
			int procedimentoId, String userName) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		int statusFlag = ReturnValues.UNKNOWN;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			ProcedimentoDelegate.getInstance().deleteFascicoloProcedimento(
					connection, procedimentoId, fascicoloVO.getId().intValue());
			connection.commit();
			statusFlag = ReturnValues.SAVED;
		} catch (DataException de) {
			logger.error("FascicoloDelegate: failed eliminaProcedimentiFascicolo: ");
			jdbcMan.rollback(connection);

		} catch (SQLException se) {
			logger.error("FascicoloDelegate: failed eliminaProcedimentiFascicolo: ");
			jdbcMan.rollback(connection);

		} finally {
			jdbcMan.close(connection);
		}
		return statusFlag;
	}

	public Fascicolo getFascicoloByIdVersione(int fascicoloId, int versione) {

		try {
			Fascicolo f = new Fascicolo();
			f.setFascicoloVO(fascicoloDAO.getFascicoloByIdVersione(fascicoloId,
					versione));
			// gestione protocolli del fascicolo
			Collection<ProtocolloFascicoloVO> protocolliFascicolo = new ArrayList<ProtocolloFascicoloVO>();
			Iterator<Integer> itProt = fascicoloDAO.getProtocolliFascicoloByIdVersione(
					fascicoloId, versione).iterator();
			while (itProt.hasNext()) {
				Integer protocolloId = (Integer) itProt.next();
				ProtocolloFascicoloVO pfVO = new ProtocolloFascicoloVO();
				pfVO.setFascicoloId(fascicoloId);
				pfVO.setProtocolloId(protocolloId.intValue());
				protocolliFascicolo.add(pfVO);
			}

			f.setProtocolli(protocolliFascicolo);

			// gestione documenti del fascicolo
			Collection<DocumentoFascicoloVO> documentiFascicolo = new ArrayList<DocumentoFascicoloVO>();
			Iterator<Integer> itDoc = fascicoloDAO.getDocumentiFascicoloByIdVersione(
					fascicoloId, versione).iterator();
			while (itDoc.hasNext()) {
				Integer documentoId = (Integer) itDoc.next();
				DocumentoFascicoloVO dfVO = new DocumentoFascicoloVO();
				dfVO.setFascicoloId(fascicoloId);
				dfVO.setDocumentoId(documentoId.intValue());
				documentiFascicolo.add(dfVO);
			}

			f.setDocumenti(documentiFascicolo);

			// gestione faldoni associati al fascicolo
			Collection<FaldoneVO> faldoniFascicolo = new ArrayList<FaldoneVO>();
			Iterator<Integer> itFal = fascicoloDAO.getFaldoniFascicoloByIdVersione(
					fascicoloId, versione).iterator();
			FaldoneDelegate fal = FaldoneDelegate.getInstance();
			while (itFal.hasNext()) {
				Integer faldoneId = (Integer) itFal.next();
				FaldoneVO faldoneVO = fal.getFaldone(faldoneId.intValue());
				faldoniFascicolo.add(faldoneVO);
			}

			f.setFaldoni(faldoniFascicolo);

			// gestione procedimenti associati al fascicolo
			Collection<ProcedimentoVO> procedimentiFascicolo = new ArrayList<ProcedimentoVO>();
			Iterator<Integer> itProc = fascicoloDAO
					.getProcedimentiFascicoloByIdVersione(fascicoloId, versione)
					.iterator();
			ProcedimentoDelegate proc = ProcedimentoDelegate.getInstance();
			while (itProc.hasNext()) {
				Integer procedimentoId = (Integer) itProc.next();
				ProcedimentoVO procedimentoVO = proc
						.getProcedimentoVO(procedimentoId.intValue());
				procedimentiFascicolo.add(procedimentoVO);
			}

			f.setProcedimenti(procedimentiFascicolo);

			return f;
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getting getFascicoloByIdVersione: ");
			return null;
		}
	}

	public boolean isUtenteAbilitatoView(Utente utente, Ufficio uff,
			int fascicoloId) {
		try {
			return fascicoloDAO.isUtenteAbilitatoView(utente, uff, fascicoloId);
		} catch (DataException de) {
			logger.error("FascicoloDelegate: failed getting isUtenteAbilitatoView: ");
			return false;
		}
	}
	
	public TipoVisibilitaUfficioEnum getVisibilitaUfficioFascicolo(int ufficioId, int fascicoloId) {
		try {
			return fascicoloDAO.getVisibilitaUfficioFascicolo(ufficioId, fascicoloId);
		} catch (DataException de) {
			logger.error("FascicoloDelegate: failed getting getVisibilitaUfficioFascicolo: ");
			return TipoVisibilitaUfficioEnum.NESSUNA;
		}
	}
	
	//TODO
	public boolean isUfficioAbilitatoSuFascicoloProtocollo(int ufficioId, int fascicoloId, int protocolloId) {
		try {
			return fascicoloDAO.isUfficioAbilitatoSuFascicoloProtocollo(ufficioId, fascicoloId, protocolloId);
		} catch (DataException de) {
			logger.error("FascicoloDelegate: failed getting isUfficioAbilitatoSuFascicoloProtocollo: ");
			return false;
		}
	}
	
	public boolean isUfficioAbilitatoSuFascicoloDocumento(int ufficioId, int fascicoloId, int documentoId) {
		try {
			return fascicoloDAO.isUfficioAbilitatoSuFascicoloDocumento(ufficioId, fascicoloId, documentoId);
		} catch (DataException de) {
			logger.error("FascicoloDelegate: failed getting isUfficioAbilitatoSuFascicoloDocumento: ");
			return false;
		}
	}



	public boolean isParent(int fascicoloId) {
		try {
			return fascicoloDAO.isParent(fascicoloId);
		} catch (DataException de) {
			logger.error("FascicoloDelegate: failed getting isParent: ");
			return false;
		}
	}

	public boolean isTitolarioChanged(int fascicoloId, int titolarioId) {
		try {
			return fascicoloDAO.isTitolarioChanged(fascicoloId, titolarioId);
		} catch (DataException de) {
			logger.error("FascicoloDelegate: failed getting isTitolarioChanged: ");
			return false;
		}
	}
	
	/*
	private Map salvaRiferimenti(Connection connection, FascicoloVO fascicolo,
			Map allegati) throws Exception {
		Iterator iterator = allegati.values().iterator();
		HashMap docs = new HashMap(2);
		return docs;
	}
	 */
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

	public Map<String,FascicoloView> getFascicoliAlert(Utente utente) {
		try {
			return fascicoloDAO.getFascicoliAlert(utente.getCaricaInUso());
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getting getFascicoliAlert: ");
			return null;
		}
	}

	public int contaFascicoliAlert(Utente utente) {
		try {

			return fascicoloDAO.contaFascicoliAlert(utente.getCaricaInUso());
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getting contaFascicoliAlert: ");
			return 0;
		}
	}

	public Map<Integer,FascicoloView> getFascicoliDepositoArchivio(Utente utente, int stato) {
		try {
			return fascicoloDAO.getFascicoliArchivioDeposito(utente, stato);
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getting getFascicoliAlert: ");
			return null;
		}
	}

	public void cancellaCollegamento(int id, int collegamentoId) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			fascicoloDAO.cancellaCollegamento(connection, id, collegamentoId);
			fascicoloDAO.cancellaCollegamento(connection, collegamentoId, id);
			connection.commit();

		} catch (DataException de) {
			logger.error("FascicoloDelegate: failed cancellaCollegamento: ");
			jdbcMan.rollback(connection);

		} catch (SQLException se) {
			logger.error("FascicoloDelegate: failed cancellaCollegamento: ");
			jdbcMan.rollback(connection);

		} finally {
			jdbcMan.close(connection);
		}
	}

	public boolean isUfficioInProcedimentoAssegnatari(Integer fascicoloId,
			int ufficioInUso) {
		try {
			return fascicoloDAO.isUfficioInProcedimentoAssegnatari(fascicoloId,
					ufficioInUso);
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed isProcedimentoAssegnatarioPresent: ");
			return false;
		}
	}

	public Map<Integer,FascicoloView> getFascicoliReferentePerCruscotti(
			Integer caricaId) {
		Map<Integer,FascicoloView> fascicoli = new HashMap<Integer,FascicoloView>();
		try {
			fascicoli=fascicoloDAO.getFascicoliReferentePerCruscotti(caricaId);
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getFascicoliReferentePerCruscotti: ");
		}
		return fascicoli;
		
	}
	
	public Map<Integer,FascicoloView> getFascicoliIstruttorePerCruscotti(
			Integer caricaId) {
		Map<Integer,FascicoloView> fascicoli = new HashMap<Integer,FascicoloView>();
		try {
			fascicoli=fascicoloDAO.getFascicoliIstruttorePerCruscotti(caricaId);
		} catch (Exception de) {
			logger.error("FascicoloDelegate: failed getFascicoliIstruttorePerCruscotti: ");
		}
		return fascicoli;
		
	}

}