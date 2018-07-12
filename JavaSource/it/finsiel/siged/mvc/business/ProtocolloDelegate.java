package it.finsiel.siged.mvc.business;

import it.compit.fenice.enums.UnitaAmministrativaEnum;
import it.compit.fenice.model.protocollo.PostaInterna;
import it.compit.fenice.model.protocollo.ProtocolloRegistroEmergenza;
import it.compit.fenice.mvc.business.DomandaDelegate;
import it.compit.fenice.mvc.business.EditorDelegate;
import it.compit.fenice.mvc.business.RepertorioDelegate;
import it.compit.fenice.mvc.presentation.helper.ProtocolloProcedimentoView;
import it.compit.fenice.mvc.presentation.helper.ReportCheckPostaInternaView;
import it.compit.fenice.mvc.vo.protocollo.DomandaVO;
import it.compit.fenice.mvc.vo.repertori.DocumentoRepertorioVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.EmailConstants;
import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.constant.NomiTabelle;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.Protocollo;
import it.finsiel.siged.model.protocollo.ProtocolloIngresso;
import it.finsiel.siged.model.protocollo.ProtocolloUscita;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.integration.ProtocolloDAO;
import it.finsiel.siged.mvc.presentation.helper.AllaccioView;
import it.finsiel.siged.mvc.presentation.helper.MittenteView;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.organizzazione.AreaOrganizzativaVO;
import it.finsiel.siged.mvc.vo.organizzazione.MailConfigVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.mvc.vo.protocollo.SegnaturaVO;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.EmailUtil;
import it.finsiel.siged.util.FileUtil;
import it.finsiel.siged.util.StringUtil;
import it.flosslab.mvc.business.MittentiDelegate;
import it.flosslab.mvc.business.OggettarioDelegate;
import it.flosslab.mvc.presentation.integration.ContaProtocolloDAO;
import it.flosslab.mvc.vo.OggettoVO;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

import javax.servlet.ServletConfig;

import org.apache.log4j.Logger;

public class ProtocolloDelegate {

	private static Logger logger = Logger.getLogger(ProtocolloDelegate.class
			.getName());

	private ProtocolloDAO protocolloDAO = null;
	private ContaProtocolloDAO contaProtocolliDAO = null;
	private ServletConfig config = null;

	private static ProtocolloDelegate delegate = null;

	private ProtocolloDelegate() {

		try {
			if (protocolloDAO == null) {
				protocolloDAO = (ProtocolloDAO) DAOFactory
						.getDAO(Constants.PROTOCOLLO_DAO_CLASS);

				logger.debug("protocolloDAO instantiated:"
						+ Constants.PROTOCOLLO_DAO_CLASS);
			}
			if (contaProtocolliDAO == null) {
				contaProtocolliDAO = (ContaProtocolloDAO) DAOFactory
						.getDAO(Constants.CONTA_PROTOCOLLO_DAO_CLASS);

				logger.debug("contaProtocolliDAO instantiated:"
						+ Constants.CONTA_PROTOCOLLO_DAO_CLASS);
			}
		} catch (Exception e) {
			logger.error("Exception while connecting to UserDAOjdbc!!", e);
		}
	}

	public static ProtocolloDelegate getInstance() {
		if (delegate == null)
			delegate = new ProtocolloDelegate();
		return delegate;
	}

	public static String getIdentifier() {
		return Constants.PROTOCOLLO_DELEGATE;
	}

	public int getUltimoProtocollo(int anno, int registro) {
		try {
			return protocolloDAO.getUltimoProtocollo(anno, registro);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getUltimoProtocollo: ");
			return 0;
		}
	}

	public String getTipoProtocollo(int protocolloId) {
		try {
			return protocolloDAO.getTipoProtocollo(protocolloId);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getUltimoProtocollo: ");
			return "";
		}
	}

	private DocumentoVO salvaDocumentoPrincipale(Connection connection,
			ProtocolloVO protocollo, DocumentoVO documento, Utente utente)
			throws Exception {
		boolean isWrite = true;
		
		String fileBaseAOO = Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo() + "/" + "aoo_" + utente.getAreaOrganizzativa().getId() + "/";
		String currentYearDir = DateUtil.getAnnoCorrente() + "/";
		int response = FileUtil.createForCurrentYear(fileBaseAOO, currentYearDir);
		String path = currentYearDir + String.valueOf(protocollo.getKey());
		String docPath = fileBaseAOO + path;
		String newFile = null;
		if (documento != null && documento.getPath() != null
				&& documento.getSize() > 0) {
			if (documento.getId() == null || documento.isMustCreateNew()) {
				newFile = documento.getPath();
				File in = new File(newFile);
				FileInputStream fis = new FileInputStream(in);
				isWrite = FileUtil.writeFile(fis, docPath);
				if (isWrite) {
					documento.setPath(path);
					documento.setImpronta(FileUtil.calcolaDigest(docPath,FileConstants.SHA));
				}
				DocumentoDelegate documentoDelegate = DocumentoDelegate
						.getInstance();
				documento = documentoDelegate.salvaDocumentoPerProtocollo(
						connection, documento, docPath);

			}
			int protocolloId = protocollo.getId().intValue();
			protocolloDAO.aggiornaDocumentoPrincipaleId(connection,
					protocolloId, documento.getId().intValue());
		} else {
			protocolloDAO.eliminaDocumentoPrincipale(connection, protocollo
					.getId().intValue());
		}
		return documento;
	}

	public Map<String, DocumentoVO> salvaAllegati(Connection connection,
			ProtocolloVO protocollo, Map<String, DocumentoVO> allegati)
			throws Exception {
		DocumentoDelegate documentoDelegate = DocumentoDelegate.getInstance();
		int protocolloId = protocollo.getId().intValue();
		Iterator<DocumentoVO> iterator = allegati.values().iterator();
		HashMap<String, DocumentoVO> docs = new HashMap<String, DocumentoVO>(2);
		protocolloDAO.eliminaAllegatiProtocollo(connection, protocolloId);
		String fileBaseAOO = Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo() + "/" + "aoo_" + protocollo.getAooId() + "/";
		String currentYearDir = DateUtil.getAnnoCorrente() + "/";
		int response = FileUtil.createForCurrentYear(fileBaseAOO, currentYearDir);
		while (iterator.hasNext()) {
			DocumentoVO doc = (DocumentoVO) iterator.next();		
			String path = currentYearDir + String.valueOf(protocollo.getKey()) + "_" + doc.getFileName();
			String docPath = fileBaseAOO + path;
			int idx = doc.getIdx();
			if (doc != null) {
				if (doc.getId() == null || doc.isMustCreateNew()) {
					File in = new File(doc.getPath());
					FileInputStream fis = new FileInputStream(in);
					FileUtil.writeFile(fis, docPath);
					doc.setPath(path);
					doc = documentoDelegate.salvaDocumentoPerProtocollo(
							connection, doc, docPath);
				}
				doc.setIdx(idx);
				ProtocolloBO.putAllegato(doc, docs);
				protocolloDAO.salvaAllegato(
						connection,
						IdentificativiDelegate.getInstance().getNextId(
								connection, NomiTabelle.PROTOCOLLO_ALLEGATI),
						protocolloId, doc.getId().intValue(),
						protocollo.getVersione());
			}
		}
		return docs;
	}

	public Map<String, DocumentoVO> salvaAllegati(ProtocolloVO protocollo,
			Map<String, DocumentoVO> allegati) throws Exception {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			salvaAllegati(connection, protocollo, allegati);
			connection.commit();
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocollo fallito, rolling back transction..",
					de);
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocollo fallito, rolling back transction..",
					se);
		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);
		} finally {
			jdbcMan.close(connection);
		}
		return allegati;

	}

	public String getProgressivoNotifica(int aooId) throws Exception {
		GregorianCalendar today = new GregorianCalendar();
		return protocolloDAO.getProgressivoNotifica(aooId,
				today.get(Calendar.YEAR));
	}

	private void eliminaAllacci(Connection connection, int protocolloId,
			Utente utente) throws Exception {
		Collection<AllaccioVO> allacci = protocolloDAO.getAllacciProtocollo(
				connection, protocolloId);
		if (allacci != null) {
			for (Iterator<AllaccioVO> i = allacci.iterator(); i.hasNext();) {
				AllaccioVO allaccio = (AllaccioVO) i.next();
				if (allaccio.isPrincipale()
						&& allaccio.getAllaccioDescrizione().indexOf("I") > 0) {
					ProtocolloVO protIngresso = protocolloDAO
							.getProtocolloById(connection,
									allaccio.getProtocolloAllacciatoId());
					if (!protIngresso.getStatoProtocollo().equals("C")) {
						updateScarico(protIngresso, "N", utente, connection,
								false);
					}
				}
				protocolloDAO.eliminaAllaccioProtocollo(connection,
						allaccio.getProtocolloId(),
						allaccio.getProtocolloAllacciatoId());
			}
		}
	}

	private void salvaAllacci(Connection connection, int protocolloId,
			Collection<AllaccioVO> allacci, Utente utente, ProtocolloVO prt)
			throws Exception {
		if (allacci != null) {
			boolean flagPrincipale;
			for (Iterator<AllaccioVO> i = allacci.iterator(); i.hasNext();) {
				flagPrincipale = false;
				AllaccioVO allaccio = (AllaccioVO) i.next();
				allaccio.setProtocolloId(protocolloId);
				allaccio.setId(IdentificativiDelegate.getInstance().getNextId(
						connection, NomiTabelle.PROTOCOLLO_ALLACCI));
				AssegnatarioVO ass = getAssegnatarioPerCompetenza(connection,
						allaccio.getProtocolloAllacciatoId());

				if (!protocolloDAO.esisteAllaccio(connection,
						allaccio.getProtocolloId(),
						allaccio.getProtocolloAllacciatoId())) {
					allaccio.setPrincipale(flagPrincipale);
					protocolloDAO.salvaAllaccio(connection, allaccio);
				}
				AllaccioVO allaccioBilaterale = new AllaccioVO();
				allaccioBilaterale.setId(IdentificativiDelegate.getInstance()
						.getNextId(connection, NomiTabelle.PROTOCOLLO_ALLACCI));
				allaccioBilaterale.setProtocolloId(allaccio
						.getProtocolloAllacciatoId());
				allaccioBilaterale.setProtocolloAllacciatoId(protocolloId);

				if (!protocolloDAO.esisteAllaccio(connection,
						allaccioBilaterale.getProtocolloId(),
						allaccioBilaterale.getProtocolloAllacciatoId())) {
					allaccioBilaterale.setPrincipale(flagPrincipale);
					protocolloDAO.salvaAllaccio(connection, allaccioBilaterale);
				}
				if (prt.getFlagTipo().equals("U") && prt.getVersione() == 0) {
					ProtocolloVO pAll = getProtocolloById(connection,
							allaccio.getProtocolloAllacciatoId());
					if (pAll.getGiorniAlert() != 0
							&& pAll.getFlagTipo().equals("I")) {
						registraPostaInternaAllaccio(connection, ass, pAll,
								utente, prt);
					}

				}
			}
		}
	}

	private void registraPostaInternaAllaccio(Connection connection,
			AssegnatarioVO dest, ProtocolloVO protocolloAllacciato, Utente ute,
			ProtocolloVO protocollo) throws Exception {
		RegistroVO reg = RegistroDelegate.getInstance().getRegistroById(
				ute.getRegistroPostaInterna());
		PostaInterna posta = ProtocolloBO.getDefaultPostaInterna(ute);
		// oggetto-versione
		posta.getProtocollo().setVersione(1);
		posta.getProtocollo().setOggetto(
				"E' stata effettuata la registrazione in uscita n. "
						+ protocollo.getNumProtocollo()
						+ " allacciata alla n. "
						+ protocolloAllacciato.getNumProtocollo()
						+ " in ingresso");
		// mittente
		posta.getProtocollo().setUfficioMittenteId(ute.getUfficioInUso());
		posta.getProtocollo().setUtenteMittenteId(ute.getValueObject().getId());
		posta.getProtocollo().setCognomeMittente(
				ute.getValueObject().getFullName());
		posta.getProtocollo().setFlagTipoMittente("F");
		// destinatario
		dest.setCaricaAssegnatarioId(0);
		dest.setCompetente(false);
		posta.aggiungiDestinatario(dest);
		// allacci
		AllaccioVO protIN = new AllaccioVO();
		protIN.setId(IdentificativiDelegate.getInstance().getNextId(connection,
				NomiTabelle.PROTOCOLLO_ALLACCI));
		protIN.setProtocolloId(0);
		protIN.setProtocolloAllacciatoId(protocolloAllacciato.getId());
		posta.allacciaProtocollo(protIN);
		AllaccioVO protOUT = new AllaccioVO();
		protOUT.setId(IdentificativiDelegate.getInstance().getNextId(
				connection, NomiTabelle.PROTOCOLLO_ALLACCI));
		protOUT.setProtocolloId(0);
		protOUT.setProtocolloAllacciatoId(protocollo.getId());
		posta.allacciaProtocollo(protOUT);

		registraPostaInterna(connection, posta, ute, reg);
	}

	private void salvaProcedimenti(Connection connection,
			ProtocolloVO protocollo,
			Collection<ProtocolloProcedimentoVO> procedimenti, Utente utente)
			throws Exception {

		int protocolloId = protocollo.getId().intValue();
		ProcedimentoDelegate pd = ProcedimentoDelegate.getInstance();
		if (procedimenti != null) {
			for (Object i : procedimenti) {

				ProtocolloProcedimentoVO protocolloProcedimentoVO = (ProtocolloProcedimentoVO) i;
				if (!protocolloProcedimentoVO.isAggiunto()) {
					protocolloProcedimentoVO.setProtocolloId(protocolloId);
					protocolloProcedimentoVO.setRowCreatedUser(utente
							.getValueObject().getUsername());
					protocolloProcedimentoVO.setRowUpdatedUser(utente
							.getValueObject().getUsername());
					protocolloProcedimentoVO.setRowCreatedTime(new Date(System
							.currentTimeMillis()));
					protocolloProcedimentoVO.setRowUpdatedTime(new Date(System
							.currentTimeMillis()));
					pd.salvaProcedimentoProtocollo(connection,
							protocolloProcedimentoVO);
				}

			}
		}
	}

	private void eliminaFascicoliPrecedenti(Connection connection,
			Collection<Integer> fascicoliId, int protocolloId, Utente utente)
			throws Exception {

		String username = utente.getValueObject().getUsername();
		FascicoloDelegate fd = FascicoloDelegate.getInstance();
		if (fascicoliId != null) {
			for (int key : fascicoliId) {
				FascicoloVO fVO = fd.getFascicoloVOById(connection, key);
				fd.eliminaProtocolloFascicolo(connection, fVO, protocolloId,
						username);
			}
		}
	}

	private void eliminaProcedimentiPrecedenti(Connection conn,
			Collection<Integer> procedimentiId, int protocolloId, Utente utente)
			throws Exception {
		String username = utente.getValueObject().getUsername();
		ProcedimentoDelegate pd = ProcedimentoDelegate.getInstance();
		if (procedimentiId != null) {
			for (int procedimentoId : procedimentiId) {
				if (procedimentoId != 0) {
					ProtocolloProcedimentoVO vo = new ProtocolloProcedimentoVO();
					vo.setProtocolloId(protocolloId);
					vo.setProcedimentoId(procedimentoId);
					vo.setRowCreatedUser(username);
					vo.setRowUpdatedUser(username);
					vo.setRowCreatedTime(new Date(System.currentTimeMillis()));
					vo.setRowUpdatedTime(new Date(System.currentTimeMillis()));
					pd.eliminaProtocolloProcedimento(conn, vo);
				}
			}
		}
	}

	private void salvaFascicoli(Connection connection, Protocollo protocollo,
			Utente utente) throws Exception {
		int protocolloId = protocollo.getProtocollo().getId().intValue();
		FascicoloDelegate fd = FascicoloDelegate.getInstance();
		if (protocollo.getFascicoli() != null) {
			for (Object i : protocollo.getFascicoli()) {
				FascicoloVO fVO = (FascicoloVO) i;
				fd.salvaFascicoloProtocollo(connection, fVO, protocolloId,
						utente.getValueObject().getUsername(), utente.getUfficioInUso());
			}
		}
	}

	private ProtocolloVO registraProtocollo(Connection connection,
			Protocollo protocollo, Utente utente) throws Exception {
		GregorianCalendar today = new GregorianCalendar();
		int numeroProtocollo = 1;
		if (protocollo.getProtocollo().getFlagTipo().equals("P"))
			numeroProtocollo = protocolloDAO.getMaxNumProtocollo(connection,
					today.get(Calendar.YEAR), utente.getRegistroPostaInterna(),
					1);
		else
			numeroProtocollo = protocolloDAO.getMaxNumProtocollo(connection,
					today.get(Calendar.YEAR), protocollo.getProtocollo()
							.getRegistroId(), utente.getNumPrt());
		ProtocolloVO protocolloVO = protocollo.getProtocollo();
		protocolloVO.setNumProtocollo(numeroProtocollo);

		
		// salvo ProtocolloVO
		protocolloVO.setId(IdentificativiDelegate.getInstance().getNextId(connection, NomiTabelle.PROTOCOLLI));
		ProtocolloVO protocolloSalvato = protocolloDAO.newProtocollo(connection, protocolloVO);
		Integer protocolloId = protocolloSalvato.getId();
		// salvo il documento principale
		protocollo.setDocumentoPrincipale(salvaDocumentoPrincipale(connection,protocolloSalvato, protocollo.getDocumentoPrincipale(),utente));

		// salvo i documenti allegati
		protocollo.setAllegati(salvaAllegati(connection, protocolloSalvato,protocollo.getAllegati()));
		salvaAllacci(connection, protocolloId.intValue(),protocollo.getAllacci(), utente, protocolloVO);
		// salvo i dati dei fascicoli
		salvaFascicoli(connection, protocollo, utente);
		// salvo i dati dei procedimenti
		salvaProcedimenti(connection, protocolloVO,protocollo.getProcedimenti(), utente);

		return protocolloSalvato;
	}

	public ProtocolloVO registraProtocolloIngresso(
			ProtocolloIngresso protocollo, Utente utente) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		ProtocolloVO pVO = null;
		try {
			jdbcMan = new JDBCManager();

			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			pVO = registraProtocolloIngresso(connection, protocollo, utente);
			if (protocollo.getProtocollo().getEmailId() != 0) {
				EmailDelegate em = EmailDelegate.getInstance();
				em.aggiornaStatoEmailIngresso(connection, protocollo
						.getProtocollo().getEmailId(),
						EmailConstants.EMAIL_INGRESSO_PROTOCOLLATA);
				em.eliminaEmailAllegati(protocollo.getProtocollo().getEmailId());
			}
			if (protocollo.getProtocollo().getEmailUfficioId() != 0) {
				EmailDelegate em = EmailDelegate.getInstance();
				em.aggiornaStatoEmailUfficioIngresso(connection, protocollo
						.getProtocollo().getEmailUfficioId(),
						EmailConstants.EMAIL_INGRESSO_PROTOCOLLATA);
				em.eliminaEmailUfficioAllegati(protocollo.getProtocollo()
						.getEmailId());
			}
			connection.commit();

		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocollo fallito, rolling back transction..",
					de);

		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocollo fallito, rolling back transction..",
					se);

		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);

		} finally {
			jdbcMan.close(connection);
		}
		return pVO;

	}
	
	public ProtocolloVO registraProtocolloIngressoCDS(
			ProtocolloIngresso protocollo, Utente utente) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		ProtocolloVO pVO = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			pVO = registraProtocolloIngresso(connection, protocollo, utente);
			connection.commit();
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn("Salvataggio Protocollo fallito, rolling back transction..",de);
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn("Salvataggio Protocollo fallito, rolling back transction..",se);
		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);

		} finally {
			jdbcMan.close(connection);
		}
		return pVO;
	}
	
	public ProtocolloVO registraProtocolloUscitaCDS(
			ProtocolloUscita protocollo, Utente utente) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		ProtocolloVO pVO = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			pVO = registraProtocolloUscita(connection, protocollo, utente);
			connection.commit();
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn("Salvataggio Protocollo fallito, rolling back transction..",de);
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn("Salvataggio Protocollo fallito, rolling back transction..",se);
		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);
		} finally {
			jdbcMan.close(connection);
		}
		return pVO;
	}

	public ProtocolloVO registraProtocolloIngressoDaDomandaErsu(
			ProtocolloIngresso protocollo, Utente utente, DomandaVO domanda) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		ProtocolloVO pVO = null;
		DomandaDelegate domandaDelegate = DomandaDelegate.getInstance();
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			pVO = registraProtocolloIngresso(connection, protocollo, utente);
			for (SoggettoVO mittente : protocollo.getProtocollo().getMittenti())
				if (mittente.getIndirizzoEMail() != null
						&& !mittente.getIndirizzoEMail().trim().equals(""))
					EmailDelegate.getInstance().salvaMessaggioPerInvioErsu(
							connection,
							IdentificativiDelegate.getInstance().getNextId(
									connection, NomiTabelle.EMAIL_CODA_INVIO),
							pVO.getAooId(), pVO.getId(), mittente);
			if (domanda.getStatoDomanda() != 3) {
				domandaDelegate.updateStato(connection, 3,
						domanda.getIdDomanda());
				domandaDelegate.newIscrizioneProtocollata(connection,
						domanda.getIdDomanda(), pVO.getId(),
						pVO.getNumProtocollo(), pVO.getDataRegistrazione());
			}
			connection.commit();
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocollo fallito, rolling back transction..",
					de);

		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocollo fallito, rolling back transction..",
					se);

		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);

		} finally {
			jdbcMan.close(connection);
		}
		return pVO;
	}

	public boolean registraCheckPostaInterna(ProtocolloVO protocollo,
			Utente utente, boolean presaVisione) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		boolean pVO = false;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			AssegnatarioVO assegnatario = new AssegnatarioVO();
			assegnatario.setCaricaAssegnatarioId(utente.getCaricaInUso());
			assegnatario.setUfficioAssegnatarioId(utente.getUfficioInUso());
			assegnatario.setCaricaAssegnanteId(protocollo
					.getCaricaProtocollatoreId());
			assegnatario.setUfficioAssegnanteId(protocollo
					.getUfficioProtocollatoreId());
			assegnatario.setProtocolloId(protocollo.getId());
			assegnatario.setCompetente(!presaVisione);
			assegnatario.setId(IdentificativiDelegate.getInstance().getNextId(
					connection, NomiTabelle.CHECK_POSTA_INTERNA));
			protocolloDAO.salvaCheckPresaVisione(connection, assegnatario);
			connection.commit();
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocollo fallito, rolling back transction..",
					de);
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocollo fallito, rolling back transction..",
					se);
		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);
		} finally {
			jdbcMan.close(connection);
		}
		return pVO;
	}

	public boolean registraLavoratoProtocolloIngresso(
			AssegnatarioVO assegnatario, String username) {
		boolean pVO = false;
		try {
			if (assegnatario != null)
				protocolloDAO.registraLavoratoProtocolloIngresso(assegnatario,
						username);
		} catch (Exception e) {
			logger.warn("Si e' verificata un eccezione non gestita.", e);
		}
		return pVO;
	}

	private void setPostaInternaLavorata(Connection connection, Integer id,
			Utente utente) throws Exception {
		PostaInterna posta = getPostaInternaById(id);
		if (posta.getProcedimenti().size() != 0) {
			posta.setLavorato(true);
			posta.setFascicoli(null);
			ProtocolloVO protocolloSalvato = aggiornaProtocollo(connection,
					posta, utente);
			if (protocolloSalvato.getReturnValue() == ReturnValues.SAVED) {
				protocolloSalvato.setReturnValue(ReturnValues.UNKNOWN);
				int protocolloId = protocolloSalvato.getId().intValue();
				protocolloDAO.eliminaAssegnatariProtocollo(connection,
						protocolloId);
				Collection<AssegnatarioVO> assegnatari = posta.getDestinatari();
				if (assegnatari != null) {
					for (Iterator<AssegnatarioVO> i = assegnatari.iterator(); i
							.hasNext();) {
						AssegnatarioVO assegnatario = i.next();
						assegnatario.setProtocolloId(protocolloId);
						assegnatario.setId(IdentificativiDelegate.getInstance()
								.getNextId(connection,
										NomiTabelle.PROTOCOLLO_ASSEGNATARI));
						assegnatario.setLavorato(isLavorato(posta.isLavorato(),
								assegnatario, utente));
						protocolloDAO.salvaAssegnatario(connection,
								assegnatario, protocolloSalvato.getVersione());
					}
				}
			}

		}
	}

	public ProtocolloVO registraPostaInterna(PostaInterna protocollo,
			Utente utente, RegistroVO reg) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		ProtocolloVO pVO = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			pVO = registraPostaInterna(connection, protocollo, utente, reg);
			if (protocollo.getRispostaId() != 0) {
				setPostaInternaLavorata(connection, protocollo.getRispostaId(),
						utente);
			}
			//
			if (protocollo.getDocRepertorioId() != 0) {
				RepertorioDelegate repertorioDelegate=RepertorioDelegate.getInstance();
				DocumentoRepertorioVO dRepVO=repertorioDelegate.getDocumentoRepertorio(protocollo.getDocRepertorioId());
				if(dRepVO.getFlagStato()==DocumentoRepertorioVO.REGISTRATO || dRepVO.getFlagStato()==DocumentoRepertorioVO.PROTOCOLLATO)
					repertorioDelegate.aggiornaStato(dRepVO.getDocRepertorioId(), DocumentoRepertorioVO.PROTOCOLLATO);
				if(dRepVO.getFlagStato()==DocumentoRepertorioVO.PUBBLICATO || dRepVO.getFlagStato()==DocumentoRepertorioVO.PUBBLICATO_PROTOCOLLATO)
					repertorioDelegate.aggiornaStato(dRepVO.getDocRepertorioId(), DocumentoRepertorioVO.PUBBLICATO_PROTOCOLLATO);
			}
			//
			connection.commit();
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocollo fallito, rolling back transction..",
					de);
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocollo fallito, rolling back transction..",
					se);
		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);
		} finally {
			jdbcMan.close(connection);
		}
		return pVO;
	}

	private ProtocolloVO registraPostaInterna(Connection connection,
			PostaInterna protocollo, Utente utente, RegistroVO reg)
			throws Exception {
		ProtocolloVO protocolloSalvato = new ProtocolloVO();
		protocolloSalvato.setReturnValue(ReturnValues.UNKNOWN);
		logger.info("ProtocolloDelegate:registraPostaInterna");
		// protocollo
		utente.setRegistroInUso(reg.getId().intValue());
		protocollo.getProtocollo().setRegistroId(reg.getId().intValue());
		protocolloSalvato = registraProtocollo(connection, protocollo, utente);
		utente.setRegistroInUso(utente.getRegistroUfficialeId());
		protocolloSalvato.setReturnValue(ReturnValues.UNKNOWN);
		int protocolloId = protocolloSalvato.getId().intValue();

		// salva assegnatari
		Collection<AssegnatarioVO> assegnatari = protocollo.getDestinatari();
		if (assegnatari != null) {
			for (Iterator<AssegnatarioVO> i = assegnatari.iterator(); i
					.hasNext();) {
				AssegnatarioVO assegnatario = i.next();
				assegnatario.setProtocolloId(protocolloId);
				assegnatario.setId(IdentificativiDelegate.getInstance()
						.getNextId(connection,
								NomiTabelle.PROTOCOLLO_ASSEGNATARI));
				protocolloDAO.salvaAssegnatario(connection, assegnatario,
						protocolloSalvato.getVersione());
			}
		}

		protocolloSalvato.setReturnValue(ReturnValues.SAVED);

		if (protocolloSalvato.getReturnValue() == ReturnValues.SAVED) {
			Collection<DocumentoVO> docs = new ArrayList<DocumentoVO>(
					protocollo.getAllegati().values());
			docs.add(protocollo.getDocumentoPrincipale());
			for (Iterator<DocumentoVO> i = docs.iterator(); i.hasNext();) {
				DocumentoVO doc = i.next();
				if (doc != null && doc.getPath() != null) {
					File f = new File(doc.getPath());
					f.delete();
				}
			}
		}

		if (protocollo.getIntDocEditor() > 0) {
			EditorDelegate.getInstance().aggiornaStatoByFlagTipo(connection,
					protocollo.getIntDocEditor(), 1);
		}
		return protocolloSalvato;
	}

	public ProtocolloVO registraProtocolloIngresso(Connection connection,
			ProtocolloIngresso protocollo, Utente utente) throws Exception {
		ProtocolloVO protocolloSalvato = new ProtocolloVO();
		protocolloSalvato.setReturnValue(ReturnValues.UNKNOWN);
		logger.info("ProtocolloDelegate:registraProtocolloIngresso");
		protocolloSalvato = registraProtocollo(connection, protocollo, utente);
		int protocolloId = protocolloSalvato.getId().intValue();
		Collection<AssegnatarioVO> assegnatari = protocollo.getAssegnatari();
		if (assegnatari != null) {
			for (Iterator<AssegnatarioVO> i = assegnatari.iterator(); i
					.hasNext();) {
				AssegnatarioVO assegnatario = i.next();
				assegnatario.setProtocolloId(protocolloId);
				assegnatario.setId(IdentificativiDelegate.getInstance()
						.getNextId(connection,
								NomiTabelle.PROTOCOLLO_ASSEGNATARI));
				protocolloDAO.salvaAssegnatario(connection, assegnatario,
						protocolloSalvato.getVersione());
			}
		}

		List<SoggettoVO> mittenti = protocollo.getProtocollo().getMittenti();
		if (mittenti != null)
			for (SoggettoVO mittente : mittenti) {
				int idMittente = IdentificativiDelegate.getInstance()
						.getNextId(connection, NomiTabelle.PROTOCOLLO_MITTENTI);
				MittentiDelegate.getInstance().salvaMittente(connection,
						mittente, protocollo.getProtocollo(), idMittente,
						protocolloSalvato.getVersione());
			}
		SegnaturaVO segnaturaVO = new SegnaturaVO();
		segnaturaVO.setFkProtocolloId(protocolloId);
		segnaturaVO.setRowCreatedUser(utente.getValueObject().getUsername());
		segnaturaVO.setTipoProtocollo(protocollo.getProtocollo().getFlagTipo());
		segnaturaVO.setTextSegnatura(ProtocolloBO.getSignature(protocollo));
		segnaturaVO.setId(IdentificativiDelegate.getInstance().getNextId(
				connection, NomiTabelle.SEGNATURE));
		protocolloDAO.salvaSegnatura(connection, segnaturaVO);

		if (protocolloSalvato.getReturnValue() == ReturnValues.SAVED) {
			Collection<DocumentoVO> docs = new ArrayList<DocumentoVO>(
					protocollo.getAllegati().values());
			docs.add(protocollo.getDocumentoPrincipale());
			for (Iterator<DocumentoVO> i = docs.iterator(); i.hasNext();) {
				DocumentoVO doc = i.next();
				if (doc != null && doc.getPath() != null) {
					File f = new File(doc.getPath());
					f.delete();
				}
			}
		}
		List<Integer> assegnatarioId = ProtocolloBO
				.notificaAssegnatarioCompetenza(protocollo.getAssegnatari());
		if (assegnatarioId.size() > 0) {
			for (Integer id : assegnatarioId) {
				UtenteVO utenteVO = UtenteDelegate.getInstance().getUtente(id);
				MailConfigVO mailConfigVO = Organizzazione.getInstance()
						.getAreaOrganizzativa(utenteVO.getAooId())
						.getMailConfig();
				AreaOrganizzativaVO aoo = Organizzazione.getInstance()
						.getAreaOrganizzativa(utenteVO.getAooId())
						.getValueObject();
				if (mailConfigVO.getPnSmtp() != null
						&& !"".equals(mailConfigVO.getPnSmtp().trim())
						&& mailConfigVO.getPnUsername() != null
						&& !"".equals(mailConfigVO.getPnUsername().trim())
						&& mailConfigVO.getPnPwd() != null
						&& !"".equals(mailConfigVO.getPnPwd().trim())
						&& mailConfigVO.getPnIndirizzo() != null
						&& !"".equals(mailConfigVO.getPnIndirizzo().trim())
						&& utenteVO.getEmailAddress() != null
						&& !"".equals(utenteVO.getEmailAddress().trim())) {
					String subjectMsg = aoo.getDescription()
							+ ": Notifica assegnazione protocollo";
					String bodyMsg = prepareMessage(protocolloSalvato);
					try {
						EmailUtil
								.sendNoAttachement(mailConfigVO.getPnSmtp(),
										mailConfigVO.getPnUsername(),
										mailConfigVO.getPnPwd(),
										mailConfigVO.getPnIndirizzo(),
										utenteVO.getEmailAddress(), subjectMsg,
										bodyMsg);
					} catch (Exception e1) {
						logger.warn(
								"Non e' stato possibile inviare la mail di notifica assegnazione.",
								e1);
					}
				}
			}

		}
		return protocolloSalvato;
	}

	public ProtocolloVO aggiornaFascicoliProtocollo(
			Collection<FascicoloVO> fascicoli, Utente utente, int pId) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		// boolean saved;
		ProtocolloVO protocolloSalvato = new ProtocolloVO();
		protocolloSalvato.setReturnValue(ReturnValues.UNKNOWN);
		logger.info("ProtocolloDelegate:aggiornafascicoliProtocollo");

		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			ProtocolloIngresso protocollo = getProtocolloIngressoById(pId);
			protocollo.setFascicoli(fascicoli);
			protocollo.getProtocollo().setStatoProtocollo("A");
			protocolloSalvato = aggiornaProtocollo(connection, protocollo,
					utente);
			if (protocolloSalvato.getReturnValue() == ReturnValues.SAVED) {
				connection.commit();
				protocolloSalvato.setReturnValue(ReturnValues.SAVED);
			} else {
				jdbcMan.rollback(connection);
			}
		} catch (DataException de) {
			de.printStackTrace();
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocollo fallito, rolling back transction..",
					de);

		} catch (SQLException se) {
			se.printStackTrace();
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocollo fallito, rolling back transction..",
					se);

		} catch (Exception e) {
			e.printStackTrace();
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);
		} finally {
			jdbcMan.close(connection);
		}
		return protocolloSalvato;

	}

	public ProtocolloVO aggiornaFascicoliProtocollo(Connection connection,
			Collection<FascicoloVO> fascicoli, Utente utente, int pId)
			throws Exception {
		// boolean saved;
		ProtocolloVO protocolloSalvato = new ProtocolloVO();
		protocolloSalvato.setReturnValue(ReturnValues.UNKNOWN);

		try {
			ProtocolloIngresso protocollo = getProtocolloIngressoById(pId);
			protocollo.setFascicoli(fascicoli);
			protocolloSalvato = aggiornaProtocollo(connection, protocollo,
					utente);
			connection.commit();
			protocolloSalvato.setReturnValue(ReturnValues.SAVED);
		} catch (DataException de) {
			logger.warn(
					"Salvataggio Protocollo fallito, rolling back transction..",
					de);
			throw new DataException(
					"Errore nel salvataggio del Fascicolo_protocollo.");
		}
		return protocolloSalvato;

	}

	public ProtocolloVO aggiornaFascicoliPostaInterna(
			Collection<FascicoloVO> fascicoli, Utente utente, int pId) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		ProtocolloVO protocolloSalvato = new ProtocolloVO();
		protocolloSalvato.setReturnValue(ReturnValues.UNKNOWN);
		logger.info("ProtocolloDelegate:aggiornafascicoliPostaInterna");

		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			PostaInterna protocollo = getPostaInternaById(pId);
			protocollo.setFascicoli(fascicoli);
			protocollo.getProtocollo().setStatoProtocollo("A");
			protocolloSalvato = aggiornaProtocollo(connection, protocollo,
					utente);
			if (protocolloSalvato.getReturnValue() == ReturnValues.SAVED) {

				connection.commit();
				protocolloSalvato.setReturnValue(ReturnValues.SAVED);
			} else {
				jdbcMan.rollback(connection);
			}
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocollo fallito, rolling back transction..",
					de);

		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocollo fallito, rolling back transction..",
					se);

		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);

		} finally {
			jdbcMan.close(connection);
		}
		return protocolloSalvato;

	}

	private ProtocolloVO aggiornaProtocollo(Connection connection,
			Protocollo protocollo, Utente utente) throws Exception {
		ProtocolloVO protocolloVO = protocollo.getProtocollo();
		protocolloVO.setRowUpdatedTime(new Date(System.currentTimeMillis()));
		protocolloVO.setRowUpdatedUser(utente.getValueObject().getUsername());
		// salvo ProtocolloVO

		ProtocolloVO protocolloSalvato = protocolloDAO.aggiornaProtocollo(
				connection, protocolloVO);

		if (protocolloSalvato.getReturnValue() == ReturnValues.SAVED) {
			Integer protocolloId = protocolloSalvato.getId();

			// salvo il documento principale
			protocollo.setDocumentoPrincipale(salvaDocumentoPrincipale(
					connection, protocollo.getProtocollo(),
					protocollo.getDocumentoPrincipale(), utente));

			// salvo i documenti allegati
			protocollo.setAllegati(salvaAllegati(connection, protocolloSalvato,
					protocollo.getAllegati()));

			// salvo gli Allacci
			eliminaAllacci(connection, protocolloId.intValue(), utente);
			salvaAllacci(connection, protocolloId.intValue(),
					protocollo.getAllacci(), utente, protocolloSalvato);

			// fascicoli
			eliminaFascicoliPrecedenti(connection,
					protocollo.getFascicoliEliminatiId(), protocolloVO.getId()
							.intValue(), utente);
			salvaFascicoli(connection, protocollo, utente);

			// procedimenti
			eliminaProcedimentiPrecedenti(connection,
					protocollo.getProcedimentiEliminatiId(), protocolloVO
							.getId().intValue(), utente);
			salvaProcedimenti(connection, protocolloSalvato,
					protocollo.getProcedimenti(), utente);
		}
		return protocolloSalvato;
	}

	public ProtocolloVO aggiornaProtocolloIngresso(
			ProtocolloIngresso protocollo, Utente utente) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		ProtocolloVO protocolloSalvato = new ProtocolloVO();
		protocolloSalvato.setReturnValue(ReturnValues.UNKNOWN);
		logger.info("ProtocolloDelegate:aggiornaProtocolloIngresso");
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);

			protocolloSalvato = aggiornaProtocollo(connection, protocollo,
					utente);
			if (protocolloSalvato.getReturnValue() == ReturnValues.SAVED) {

				protocolloSalvato.setReturnValue(ReturnValues.UNKNOWN);
				int protocolloId = protocolloSalvato.getId().intValue();
				protocolloDAO.eliminaMittentiProtocollo(connection,
						protocolloId);
				List<SoggettoVO> mittenti = protocollo.getProtocollo()
						.getMittenti();
				if (mittenti != null)
					for (SoggettoVO mittente : mittenti) {
						int idMittente = IdentificativiDelegate.getInstance()
								.getNextId(connection,
										NomiTabelle.PROTOCOLLO_MITTENTI);
						MittentiDelegate.getInstance().salvaMittente(
								connection, mittente,
								protocollo.getProtocollo(), idMittente,
								protocolloSalvato.getVersione());
				}
				protocolloDAO.eliminaAssegnatariProtocollo(connection,
						protocolloId);
				Collection<AssegnatarioVO> assegnatari = protocollo
						.getAssegnatari();
				if (assegnatari != null) {
					for (Iterator<AssegnatarioVO> i = assegnatari.iterator(); i
							.hasNext();) {
						AssegnatarioVO assegnatario = i.next();
						assegnatario.setProtocolloId(protocolloId);
						assegnatario.setId(IdentificativiDelegate.getInstance()
								.getNextId(connection,
										NomiTabelle.PROTOCOLLO_ASSEGNATARI));

						assegnatario.setLavorato(isLavorato(
								protocollo.isLavorato(), assegnatario, utente));
						assegnatario
								.setPresaVisione(isVisionato(
										protocollo.isVisionato(), assegnatario,
										utente));
						protocolloDAO.salvaAssegnatario(connection,
								assegnatario, protocolloSalvato.getVersione());
					}
				}
				connection.commit();
				protocolloSalvato.setReturnValue(ReturnValues.SAVED);
			} else {
				jdbcMan.rollback(connection);
			}
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocollo fallito, rolling back transction..",
					de);

		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocollo fallito, rolling back transction..",
					se);

		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);

		} finally {
			jdbcMan.close(connection);
		}
		return protocolloSalvato;
	}

	private boolean isLavorato(boolean lavorato, AssegnatarioVO assegnatario,
			Utente utente) {
		boolean retLavorato = assegnatario.isLavorato();
		if (lavorato
				&& assegnatario.getUfficioAssegnatarioId() == utente
						.getUfficioInUso()) {
			if ((assegnatario.getCaricaAssegnatarioId() == 0 && utente
					.getCaricaVOInUso().isReferente())
					|| assegnatario.getCaricaAssegnatarioId() == utente
							.getCaricaInUso()) {
				retLavorato = true;
			}
		}
		return retLavorato;
	}

	private boolean isVisionato(boolean visionato, AssegnatarioVO assegnatario,
			Utente utente) {
		boolean retVisionato = assegnatario.isPresaVisione();
		if (visionato
				&& assegnatario.getUfficioAssegnatarioId() == utente
						.getUfficioInUso()) {
			if ((assegnatario.getCaricaAssegnatarioId() == 0 && utente
					.getCaricaVOInUso().isReferente())
					|| assegnatario.getCaricaAssegnatarioId() == utente
							.getCaricaInUso()) {
				retVisionato = true;
			}
		}
		return retVisionato;
	}

	public ProtocolloVO aggiornaProtocolloUscita(ProtocolloUscita protocollo,
			Utente utente) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		ProtocolloVO protocolloSalvato = new ProtocolloVO();
		protocolloSalvato.setReturnValue(ReturnValues.UNKNOWN);
		logger.info("ProtocolloDelegate:aggiornaProtocolloUscita");

		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);

			// salva dati comuni
			int protocolloId = protocollo.getProtocollo().getId().intValue();
			protocolloSalvato = aggiornaProtocollo(connection, protocollo,
					utente);
			if (protocolloSalvato.getReturnValue() == ReturnValues.SAVED) {
				protocolloSalvato.setReturnValue(ReturnValues.UNKNOWN);
				// salva destinatari
				protocolloDAO.eliminaDestinatariProtocollo(connection,
						protocolloId);

				Collection<DestinatarioVO> assegnatari = protocollo
						.getDestinatari();
				if (assegnatari != null) {
					for (Iterator<DestinatarioVO> i = assegnatari.iterator(); i
							.hasNext();) {
						DestinatarioVO destinatario = i.next();
						destinatario.setProtocolloId(protocolloId);
						destinatario.setId(IdentificativiDelegate.getInstance()
								.getNextId(connection,
										NomiTabelle.PROTOCOLLO_DESTINATARI));
						protocolloDAO.salvaDestinatario(connection,
								destinatario, protocolloSalvato.getVersione());
					}
				}
				
				Collection<DestinatarioVO> destinatariViaEmail = ProtocolloBO.getDestinatariViaEmail(protocollo.getDestinatari());
				
				
				if (!destinatariViaEmail.isEmpty()) {
						EmailDelegate.getInstance().aggiornaMessaggioPerInvio(connection,protocolloSalvato.getAooId(), protocolloId,destinatariViaEmail);	
				}
		
				
				connection.commit();
				protocolloSalvato.setReturnValue(ReturnValues.SAVED);
			} else {
				jdbcMan.rollback(connection);
			}

		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocollo fallito, rolling back transction..",
					de);

		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocollo fallito, rolling back transction..",
					se);

		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);

		} finally {
			jdbcMan.close(connection);
		}
		return protocolloSalvato;
	}

	public ProtocolloVO registraProtocolloUscita(ProtocolloUscita protocollo,
			Utente utente) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		ProtocolloVO pVO = null;
		OggettarioDelegate oggettario = OggettarioDelegate.getInstance();
		try {
			jdbcMan = new JDBCManager();

			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			pVO = registraProtocolloUscita(connection, protocollo, utente);
			if (protocollo.isAddOggetto()) {
				OggettoVO oggettoVO = new OggettoVO(null, pVO.getOggetto());
				oggettario.salvaOggetto(oggettoVO);

			}
			connection.commit();

		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocollo fallito, rolling back transction..",
					de);

		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocollo fallito, rolling back transction..",
					se);

		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);

		} finally {
			jdbcMan.close(connection);
		}
		return pVO;

	}

	public ProtocolloVO registraProtocolloUscita(Connection connection,
			ProtocolloUscita protocollo, Utente utente) throws Exception {
		ProtocolloVO protocolloSalvato = new ProtocolloVO();
		protocolloSalvato.setReturnValue(ReturnValues.UNKNOWN);
		logger.info("ProtocolloDelegate:registraProtocolloUscita");
		// salva dati comuni
		protocolloSalvato = registraProtocollo(connection, protocollo, utente);
		protocolloSalvato.setReturnValue(ReturnValues.UNKNOWN);
		int protocolloId = protocolloSalvato.getId().intValue();
		// salva destinatari
		Collection<DestinatarioVO> destinatari = protocollo.getDestinatari();
		if (destinatari != null) {
			for (Iterator<DestinatarioVO> i = destinatari.iterator(); i
					.hasNext();) {
				DestinatarioVO destinatario = i.next();
				destinatario.setProtocolloId(protocolloId);
				destinatario.setId(IdentificativiDelegate.getInstance()
						.getNextId(connection,
								NomiTabelle.PROTOCOLLO_DESTINATARI));
				protocolloDAO.salvaDestinatario(connection, destinatario,
						protocolloSalvato.getVersione());

			}
		}
		
		// Registro la segnatura
		SegnaturaVO segnaturaVO = new SegnaturaVO();
		segnaturaVO.setFkProtocolloId(protocolloId);
		segnaturaVO.setRowCreatedUser(utente.getValueObject().getUsername());
		segnaturaVO.setTipoProtocollo(protocollo.getProtocollo().getFlagTipo());
		segnaturaVO.setTextSegnatura(ProtocolloBO.getSignature(protocollo));
		segnaturaVO.setId(IdentificativiDelegate.getInstance().getNextId(
				connection, NomiTabelle.SEGNATURE));
		protocolloDAO.salvaSegnatura(connection, segnaturaVO);

		Collection<DestinatarioVO> destinatariViaEmail = ProtocolloBO
				.getDestinatariViaEmail(protocollo.getDestinatari());
		
		if (!destinatariViaEmail.isEmpty()) {
			EmailDelegate.getInstance().salvaMessaggioPerInvio(
					connection,
					IdentificativiDelegate.getInstance().getNextId(connection,
							NomiTabelle.EMAIL_CODA_INVIO),
					protocolloSalvato.getAooId(), protocolloId,
					destinatariViaEmail);
		}

		if (protocollo.getFascicoloInvioId() > 0) {
			FascicoloDelegate.getInstance().eliminaCodaInvioFascicolo(
					connection, protocollo.getFascicoloInvioId());
			int versioneFascicolo = FascicoloDelegate.getInstance()
					.getFascicoloVOById(protocollo.getFascicoloInvioId())
					.getVersione();
			FascicoloDelegate.getInstance().aggiornaStatoFascicolo(connection,
					protocollo.getFascicoloInvioId(),
					Parametri.STATO_FASCICOLO_APERTO,
					utente.getValueObject().getUsername(), versioneFascicolo);
		} else if (protocollo.getDocumentoInvioId() > 0) {
			DocumentaleDelegate.getInstance().eliminaCodaInvioDocumento(
					connection, protocollo.getDocumentoInvioId(), "1");
			int dfrId = DocumentaleDelegate.getInstance().getFileId(connection,
					protocollo.getDocumentoInvioId());
			FascicoloDelegate.getInstance().rimuoviFascicoliDocumento(
					connection, protocollo.getDocumentoInvioId());
			DocumentaleDelegate.getInstance().eliminaDocumento(connection,
					protocollo.getDocumentoInvioId(), dfrId);
		} else if (protocollo.getIntDocEditor() > 0) {
			EditorDelegate.getInstance().aggiornaStatoByFlagTipo(connection,
					protocollo.getIntDocEditor(), 1);
		}

		protocolloSalvato.setReturnValue(ReturnValues.SAVED);
		return protocolloSalvato;
	}

	public Collection<AllaccioVO> getAllacciProtocollo(int protocolloId) {
		try {
			return protocolloDAO.getAllacciProtocollo(protocolloId);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getAllacci: ");
			return null;
		}
	}

	public Collection<AssegnatarioVO> getAssegnatariProtocollo(int protocolloId) {
		try {
			return protocolloDAO.getAssegnatariProtocollo(protocolloId);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getAssegnatari: ");
			return null;
		}
	}

	public AssegnatarioVO getAssegnatarioProtocollo(int protocolloId,
			int ufficioId, int caricaId) {
		try {
			return protocolloDAO.getAssegnatarioProtocollo(protocolloId,
					ufficioId, caricaId);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getAssegnatari: ");
			return null;
		}
	}

	public AllaccioVO getProtocolloAllacciabile(Utente utente,
			int annoProtocolo, int numeroProtocolloDa, int numeroProtocolloA) {
		AllaccioVO all = new AllaccioVO();
		try {
			all = protocolloDAO.getProtocolloAllacciabile(utente,
					annoProtocolo, numeroProtocolloDa, numeroProtocolloA);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getProtocolliAllacciabili: ");
		}
		return all;
	}

	public Collection<AllaccioView> getProtocolliAllacciabili(Utente utente,
			int annoProtocolo, int numeroProtocolloDa, int numeroProtocolloA,
			int protocolloId) {
		try {
			return protocolloDAO.getProtocolliAllacciabili(utente,
					annoProtocolo, numeroProtocolloDa, numeroProtocolloA,
					protocolloId);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getProtocolliAllacciabili: ");
			return null;
		}
	}

	public int contaProtocolliAllacciabili(Utente utente, int annoProtocolo,
			int numeroProtocolloDa, int numeroProtocolloA, int protocolloId) {
		try {
			return contaProtocolliDAO.contaProtocolliAllacciabili(utente,
					annoProtocolo, numeroProtocolloDa, numeroProtocolloA,
					protocolloId);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting contaProtocolliAllacciabili: ");
			return 0;
		}
	}

	public ProtocolloVO getProtocollo_By_Numero(int anno, int registro,
			int numProtocollo) {
		try {
			return protocolloDAO.getProtocolloByNumero(anno, registro,
					numProtocollo);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getProtocolliAllacciabili: ");
			return null;
		}

	}

	public int getProtocolloIdByAooNumeroAnno(int anno, int aooId,
			int numProtocollo) {
		int numero = 0;
		try {
			numero = protocolloDAO.getProtocolloIdByAooNumeroAnno(anno, aooId,
					numProtocollo);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getProtocolliAllacciabili: ");

		}
		return numero;
	}

	public Map<Long, ReportProtocolloView> getProtocolliAssegnati(
			Utente utente, int annoProtocolloDa, int annoProtocolloA,
			int numeroProtocollo, String tipoUtenteUfficio) {
		try {
			return protocolloDAO.getProtocolliAssegnati(utente,
					annoProtocolloDa, annoProtocolloA, numeroProtocollo,
					tipoUtenteUfficio);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getProtocolliAssegnati: ");
			return null;
		}
	}

	public Map<Long, ReportProtocolloView> getPostaInternaAssegnata(
			Utente utente, String tipo) {
		try {
			return protocolloDAO.getPostaInternaAssegnata(utente, tipo);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getPostaAssegnata: ");
			return null;
		}
	}
	
	public Map<Long, ReportProtocolloView> getPostaInternaRepertorio(
			Utente utente) {
		try {
			return protocolloDAO.getPostaInternaRepertorio(utente);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getPostaInternaRepertorio: ");
			return null;
		}
	}

	public Map<Integer, ReportCheckPostaInternaView> getNotifichePostaInternaView(
			int caricaId, int flagNotifica) {
		try {
			return protocolloDAO.getCheckPostaInternaView(caricaId,
					flagNotifica);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getNotifichePostaInternaView: ");
			return null;
		}
	}

	public Map<Long, ReportProtocolloView> getFatture(Utente utente,
			int registro, String tipo) {
		try {
			return protocolloDAO.getFatture(utente, registro, tipo);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getFatture: ");
			return null;
		}
	}

	public int contaPostaInternaAssegnata(Utente utente, String tipo) {
		try {
			return contaProtocolliDAO.contaPostaInternaAssegnata(utente, tipo);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getPostaAssegnata: ");
			return 0;
		}
	}
	
	public int contaPostaInternaRepertorio(Utente utente) {
		try {
			return contaProtocolliDAO.contaPostaInternaRepertorio(utente);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting contaPostaInternaRepertorio: ");
			return 0;
		}
	}


	public Map<Long, ReportProtocolloView> getPostaInternaAssegnataPerNumero(
			Utente utente, int numeroProtocollo, String tipo) {
		try {
			return protocolloDAO.getPostaInternaAssegnataPerNumero(utente,
					numeroProtocollo, tipo);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getPostaAssegnata: ");
			return null;
		}
	}

	public int contaPostaInternaAssegnataPerNumero(Utente utente,
			int numeroProtocollo, String tipo) {
		try {
			return contaProtocolliDAO.contaPostaInternaAssegnataPerNumero(
					utente, numeroProtocollo, tipo);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getPostaAssegnata: ");
			return 0;
		}
	}

	public int contaProtocolliAssegnati(Utente utente, int annoProtocolloDa,
			int annoProtocolloA, int numeroProtocollo, String tipoUtenteUfficio) {
		try {
			return contaProtocolliDAO.contaProtocolliAssegnati(utente,
					annoProtocolloDa, annoProtocolloA, numeroProtocollo,
					tipoUtenteUfficio);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting contaProtocolli: ");
			return 0;
		}
	}

	public String getDocId(int documentoId) {
		try {
			return protocolloDAO.getDocId(documentoId);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getDocId: ");
			return null;
		}
	}

	public Map<Long, ReportProtocolloView> getProtocolliRespinti(Utente utente,
			int annoProtocolloDa, int annoProtocolloA, int numeroProtocolloDa,
			int numeroProtocolloA, java.util.Date dataDa, java.util.Date dataA) {
		try {
			return protocolloDAO.getProtocolliRespinti(utente,
					annoProtocolloDa, annoProtocolloA, numeroProtocolloDa,
					numeroProtocolloA, dataDa, dataA);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getProtocolliRespinti: ");
			return null;
		}

	}

	public int contaProtocolliRespintiUtente(Utente utente,
			int annoProtocolloDa, int annoProtocolloA, int numeroProtocolloDa,
			int numeroProtocolloA, java.util.Date dataDa, java.util.Date dataA) {
		try {

			return contaProtocolliDAO.contaProtocolliRespinti(utente,
					annoProtocolloDa, annoProtocolloA, numeroProtocolloDa,
					numeroProtocolloA, dataDa, dataA);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting contaProtocolliRespinti: ");
			return 0;
		}

	}

	public int contaProtocolliRespintiUfficio(Utente utente,
			int annoProtocolloDa, int annoProtocolloA, int numeroProtocolloDa,
			int numeroProtocolloA, java.util.Date dataDa, java.util.Date dataA) {
		try {

			return contaProtocolliDAO.contaProtocolliRespintiUfficio(utente,
					annoProtocolloDa, annoProtocolloA, numeroProtocolloDa,
					numeroProtocolloA, dataDa, dataA);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting contaProtocolliRespinti: ");
			return 0;
		}

	}

	public int rifiutaProtocollo(ProtocolloIngresso protocollo,
			String tipoAzione, String statoProtocollo, Utente utente,
			boolean titolareProcedimento) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		int statusFlag = ReturnValues.UNKNOWN;
		logger.info("ProtocolloDelegate:rifiutaProtocollo");
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			statusFlag = rifiutaProtocollo(connection, protocollo, tipoAzione,
					statoProtocollo, utente, titolareProcedimento);
			connection.commit();

		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn("rifiutaProtocollo fallito, rolling back transction..",
					de);

		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn("rifiutaProtocollo fallito, rolling back transction..",
					se);

		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);

		} finally {
			jdbcMan.close(connection);
		}
		return statusFlag;

	}

	private int rifiutaProtocollo(Connection connection,
			ProtocolloIngresso protocollo, String tipoAzione,
			String StatoProtocollo, Utente utente, boolean titolareProcedimento)
			throws Exception {
		int statusFlag = ReturnValues.UNKNOWN;
		logger.info("ProtocolloDelegate:rifiutaProtocollo");

		ProtocolloVO protocolloVO = protocollo.getProtocollo();
		protocolloDAO.updateMsgAssegnatario(connection, protocollo
				.getMsgAssegnatarioCompetente(), protocolloVO.getId()
				.intValue(), utente);
		statusFlag = protocolloDAO.updateScarico(connection, protocolloVO,
				StatoProtocollo, utente, titolareProcedimento);
		protocolloDAO.aggiornaAssegnanteId(connection, protocolloVO);
		statusFlag = ReturnValues.SAVED;
		return statusFlag;

	}

	public int rifiutaPosta(PostaInterna protocollo, String tipoAzione,
			String statoProtocollo, Utente utente) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		int statusFlag = ReturnValues.UNKNOWN;
		logger.info("ProtocolloDelegate:rifiutaPostaInterna");
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			statusFlag = rifiutaPosta(connection, protocollo, tipoAzione,
					statoProtocollo, utente);
			connection.commit();

		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn("rifiutaProtocollo fallito, rolling back transction..",
					de);

		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn("rifiutaProtocollo fallito, rolling back transction..",
					se);

		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);

		} finally {
			jdbcMan.close(connection);
		}
		return statusFlag;

	}

	private int rifiutaPosta(Connection connection, PostaInterna protocollo,
			String tipoAzione, String StatoProtocollo, Utente utente)
			throws Exception {
		int statusFlag = ReturnValues.UNKNOWN;
		logger.info("ProtocolloDelegate:rifiutaProtocollo");

		ProtocolloVO protocolloVO = protocollo.getProtocollo();
		protocolloDAO.updateMsgAssegnatario(connection, protocollo
				.getMsgDestinatarioCompetente(), protocolloVO.getId()
				.intValue(), utente);
		statusFlag = protocolloDAO.updateScarico(connection, protocolloVO,
				StatoProtocollo, utente, false);

		statusFlag = ReturnValues.SAVED;
		return statusFlag;

	}

	public int presaInCarico(Collection<ProtocolloVO> protocolli,
			String tipoAzione, Utente utente) {
		// tipoAzione A=Accetta, R=Respingi
		JDBCManager jdbcMan = null;
		Connection connection = null;
		int statusFlag = ReturnValues.UNKNOWN;
		logger.info("ProtocolloDelegate:presaInCarico");
		String StatoProtocollo = "";
		try {
			jdbcMan = new JDBCManager();

			if ("R".equals(tipoAzione)) {
				StatoProtocollo = "F";
			} else if ("A".equals(tipoAzione)) {
				StatoProtocollo = "N";
			}

			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			Iterator<ProtocolloVO> it = protocolli.iterator();
			while (it.hasNext()) {
				ProtocolloVO protocolloVO = it.next();
				statusFlag = protocolloDAO.updateScarico(connection,
						protocolloVO, StatoProtocollo, utente, false);
				if (statusFlag == ReturnValues.SAVED) {
					statusFlag = protocolloDAO.presaIncarico(connection,
							protocolloVO, tipoAzione, utente);
				}
			}
			connection.commit();
			statusFlag = ReturnValues.SAVED;
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.error("ProtocolloDelegate: failed presaInCarico: ");
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
		} finally {
			jdbcMan.close(connection);

		}
		return statusFlag;

	}

	public int rifiutaProtocolli(Collection<ProtocolloIngresso> protocolli,
			Utente utente) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		int statusFlag = ReturnValues.UNKNOWN;
		logger.info("ProtocolloDelegate:rifiutaProtocolli");
		try {
			jdbcMan = new JDBCManager();

			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			Iterator<ProtocolloIngresso> it = protocolli.iterator();
			while (it.hasNext()) {
				ProtocolloIngresso protocollo = it.next();
				statusFlag = rifiutaProtocollo(connection, protocollo, "R",
						"F", utente, false);
			}
			connection.commit();
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn("rifiutaProtocolli fallito, rolling back transction..",
					de);

		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn("rifiutaProtocolli fallito, rolling back transction..",
					se);

		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);

		} finally {
			jdbcMan.close(connection);
		}
		return statusFlag;

	}

	public int riassegnaProtocollo(ProtocolloIngresso protocollo, Utente utente)
			throws DataException {
		try {
			return protocolloDAO.riassegnaProtocollo(protocollo, utente);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting riassegnaProtocollo: ");
			return ReturnValues.UNKNOWN;
		}
	}

	public int updateScarico(ProtocolloVO protocolloVO, String flagScarico,
			Utente utente, boolean titolareProc) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		int statusFlag = ReturnValues.UNKNOWN;
		logger.info("ProtocolloDelegate:updateScarico");
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			statusFlag = updateScarico(protocolloVO, flagScarico, utente,
					connection, titolareProc);
			connection.commit();
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.error("ProtocolloDelegate: failed updateScarico: ");
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
		} finally {
			jdbcMan.close(connection);
		}
		return statusFlag;
	}

	public int updateScarico(ProtocolloVO protocolloVO, String flagScarico,
			Utente utente, Connection connection, boolean titProc)
			throws DataException {
		logger.info("ProtocolloDelegate:updateScarico");
		return protocolloDAO.updateScarico(connection, protocolloVO,
				flagScarico, utente, titProc);
	}

	public int annullaProtocollo(ProtocolloVO protocolloVO, Utente utente)
			throws DataException {
		// flagScarico N=in Lavorazione, A=agli Atti, R=in Risposta
		JDBCManager jdbcMan = null;
		Connection connection = null;
		int statusFlag = ReturnValues.UNKNOWN;
		logger.info("ProtocolloDelegate:annullaProtocollo");

		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			statusFlag = protocolloDAO.annullaProtocollo(connection,
					protocolloVO, utente);
			connection.commit();
			statusFlag = ReturnValues.SAVED;
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.error("ProtocolloDelegate: failed annullaProtocollo: ");
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
		} finally {
			jdbcMan.close(connection);
		}
		return statusFlag;
	}

	public int contaProtocolli(Utente utente, Ufficio ufficio,
			LinkedHashMap<String, Object> sqlDB) {
		try {
			return contaProtocolliDAO.contaProtocolli(utente, ufficio, sqlDB);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting contaProtocolli: ");
			return 0;
		}

	}

	public boolean updateNotificaPostaInterna(int checkId) {
		boolean updated = false;
		try {
			updated = protocolloDAO.updateCheckPostaInterna(checkId);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting updateCheckPostaInterna:"
					+ de);
		}
		return updated;
	}

	public boolean updateNotifichePostaInternaUtente(int caricaId) {
		boolean updated = false;
		try {
			updated = protocolloDAO.updateCheckPostaInternaUtente(caricaId);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting updateNotifichePostaInternaUtente:"
					+ de);
		}
		return updated;
	}

	public SortedMap<Long, ReportProtocolloView> cercaProtocolli(Utente utente,
			Ufficio ufficio, LinkedHashMap<String, Object> sqlDB, boolean isTabula) {
		try {
			return protocolloDAO.cercaProtocolli(utente, ufficio, sqlDB,
					isTabula);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting cercaProtocolli: ");
			return null;
		}
	}

	public Collection<ReportProtocolloView> getProtocolliByProtMittente(
			Utente utente, String protMittente) {
		try {
			return protocolloDAO.getProtocolliByProtMittente(utente,
					protMittente);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getProtocolliByProtMittente: ");
			return null;
		}

	}

	public Map<String, DestinatarioVO> getDestinatariProtocollo(int protocolloId) {
		try {
			return protocolloDAO.getDestinatariProtocollo(protocolloId);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getDestinatariProtocollo: ");
			return null;
		}
	}

	public Collection<DestinatarioVO> getDestinatari(String destinatario) {
		try {

			return protocolloDAO.getDestinatari(destinatario);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getDestinatari: ");
			return null;
		}
	}

	public Collection<MittenteView> getMittenti(String mittente) {
		try {
			return protocolloDAO.getMittenti(mittente);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getMittenti: ");
			return null;
		}
	}

	public boolean isUtenteAbilitatoView(Utente utente, Ufficio uff,
			int protocolloId) {
		try {
			return protocolloDAO.isUtenteAbilitatoView(utente, uff,
					protocolloId);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed isUtenteAbilitatoView: ");
			return false;
		}

	}

	public ProtocolloVO getProtocolloVOById(int id) {
		try {
			return protocolloDAO.getProtocolloById(id);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getProtocolloById: ");
			return null;
		}
	}

	public ReportProtocolloView getProtocolloView(int id) {
		try {
			return protocolloDAO.getProtocolloView(id);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getProtocolloView: ");
			return null;
		}
	}

	public ReportProtocolloView getProtocolloView(Connection connection, int id) {
		try {
			return protocolloDAO.getProtocolloView(connection, id);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getProtocolloView: ");
			return null;
		}
	}

	public ProtocolloVO getProtocolloById(Connection connection, int id) {
		logger.info("ProtocolloDelegate:getProtocolloById");
		try {
			return protocolloDAO.getProtocolloById(connection, id);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getProtocolloById: ");
			return null;
		}
	}

	public AssegnatarioVO getAssegnatarioPerCompetenza(int protocolloId) {
		logger.info("ProtocolloDelegate:getAssegnatarioPerCompetenza");
		try {
			return protocolloDAO.getAssegnatarioPerCompetenza(protocolloId);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getAssegnatarioPerCompetenza: ");
			return null;
		}
	}

	public AssegnatarioVO getAssegnatarioPerCompetenza(Connection connection,
			int protocolloId) {
		logger.info("ProtocolloDelegate:getAssegnatarioPerCompetenza");
		try {
			return protocolloDAO.getAssegnatarioPerCompetenza(connection,
					protocolloId);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getAssegnatarioPerCompetenza: ");
			return null;
		}
	}

	public int getDocumentoDefault(int aooId) {

		logger.info("ProtocolloDelegate:getDocumentoDefault");
		try {
			return protocolloDAO.getDocumentoDefault(aooId);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getDocumentoDefault: ");
			return 0;
		}

	}

	public Protocollo getProtocolloById(int id) {
		Protocollo pi = new ProtocolloIngresso();
		try {
			pi.setProtocollo(protocolloDAO.getProtocolloById(id));
			/*
			 * pi.setAllegati(protocolloDAO.getAllegatiProtocollo(id)); if
			 * (pi.getProtocollo().getDocumentoPrincipaleId() != null) {
			 * pi.setDocumentoPrincipale(DocumentoDelegate.getInstance()
			 * .getDocumento( pi.getProtocollo().getDocumentoPrincipaleId()
			 * .intValue()));
			 * 
			 * } else if (pi.getProtocollo().getAooId() != 1 &&
			 * Organizzazione.getInstance().isTabulaImport()) {
			 * pi.setDocumentoPrincipale(DocumentoDelegate.getInstance()
			 * .getDocumentoTabula(id)); }
			 * 
			 * pi.getProtocollo().setMittenti(MittentiDelegate.getInstance().
			 * getMittenti(id));
			 * pi.aggiungiAssegnatari(getAssegnatariProtocollo(id));
			 */
			pi.setAllacci(getAllacciProtocollo(id));
			pi.setProcedimenti(protocolloDAO.getProcedimentiProtocollo(id));
			setFascicoliProtocollo(pi, id);
			return pi;
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getProtocolloById: ");
			return null;
		}
	}

	public ProtocolloIngresso getProtocolloIngressoById(int id) {
		ProtocolloIngresso pi = new ProtocolloIngresso();
		try {
			pi.setProtocollo(protocolloDAO.getProtocolloById(id));
			pi.setAllegati(protocolloDAO.getAllegatiProtocollo(id));

			/**/
			if (pi.getProtocollo().getDocumentoPrincipaleId() != null) {
				pi.setDocumentoPrincipale(DocumentoDelegate.getInstance()
						.getDocumento(
								pi.getProtocollo().getDocumentoPrincipaleId()
										.intValue()));
			} else if (pi.getProtocollo().getAooId() != 1
					&& Organizzazione.getInstance().getValueObject()
							.getUnitaAmministrativa() == UnitaAmministrativaEnum.POLICLINICO_CT) {
				pi.setDocumentoPrincipale(DocumentoDelegate.getInstance()
						.getDocumentoTabula(id));
			}
			pi.getProtocollo().setMittenti(
					MittentiDelegate.getInstance().getMittenti(id));

			pi.aggiungiAssegnatari(getAssegnatariProtocollo(id));
			pi.setAllacci(getAllacciProtocollo(id));
			pi.setProcedimenti(protocolloDAO.getProcedimentiProtocollo(id));
			setFascicoliProtocollo(pi, id);
			return pi;
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getProtocolloById: ");
			return null;
		}
	}

	private void setFascicoliProtocollo(Protocollo p, int protocolloId) {
		p.setFascicoli(FascicoloDelegate.getInstance()
				.getFascicoliByProtocolloId(protocolloId));
	}

	public ProtocolloUscita getProtocolloUscitaById(int id) {
		ProtocolloUscita pu = new ProtocolloUscita();
		try {
			pu.setProtocollo(protocolloDAO.getProtocolloById(id));
			pu.setAllegati(protocolloDAO.getAllegatiProtocollo(id));
			if (pu.getProtocollo().getDocumentoPrincipaleId() != null) {
				pu.setDocumentoPrincipale(DocumentoDelegate.getInstance()
						.getDocumento(
								pu.getProtocollo().getDocumentoPrincipaleId()
										.intValue()));
			} else if (pu.getProtocollo().getAooId() != 1
					&& Organizzazione.getInstance().getValueObject()
							.getUnitaAmministrativa() == UnitaAmministrativaEnum.POLICLINICO_CT) {
				pu.setDocumentoPrincipale(DocumentoDelegate.getInstance()
						.getDocumentoTabula(id));
			}
			pu.setDestinatari(getDestinatariProtocollo(id));

			AssegnatarioVO assegnatario = new AssegnatarioVO();
			assegnatario.setUfficioAssegnatarioId(pu.getProtocollo()
					.getUfficioMittenteId());

			assegnatario.setUtenteAssegnatarioId(pu.getProtocollo()
					.getUtenteMittenteId());
			pu.setMittente(assegnatario);
			pu.setAllacci(getAllacciProtocollo(id));
			pu.setProcedimenti(protocolloDAO.getProcedimentiProtocollo(id));
			setFascicoliProtocollo(pu, id);
			return pu;
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getProtocolloById: ");
			return null;
		}
	}

	private String prepareMessage(ProtocolloVO protocollo) {
		StringBuffer stB = new StringBuffer();
		stB.append("Protocollo N. " + protocollo.getNumProtocollo() + "\r\n");
		stB.append("Tipo: " + protocollo.getFlagTipo() + "\r\n");
		stB.append("Data Registrazione "
				+ DateUtil.formattaData(protocollo.getDataRegistrazione()
						.getTime()) + "\r\n");
		stB.append("Oggetto: " + protocollo.getOggetto() + "\r\n");
		if (protocollo.getFlagTipoMittente().equals("F")) {
			stB.append("Mittente: " + protocollo.getCognomeMittente() + " "
					+ StringUtil.getStringa(protocollo.getNomeMittente())
					+ "\r\n");
		} else {
			stB.append("Mittente: " + protocollo.getDenominazioneMittente()
					+ "\r\n");
		}
		return stB.toString();
	}

	public Collection<ProtocolloVO> getProtocolliToExport(int registroId)
			throws Exception {
		JDBCManager jdbcLocal = null;
		Connection connLocal = null;
		Collection<ProtocolloVO> protocolli = new ArrayList<ProtocolloVO>();
		try {
			jdbcLocal = (JDBCManager) config.getServletContext().getAttribute(
					Constants.JDBCMAN_1);
			connLocal = jdbcLocal.getConnection();
			protocolli = protocolloDAO.getProtocolliToExport(connLocal,
					registroId);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getProtocolliToExport: ");
		} finally {
			jdbcLocal.close(connLocal);
		}
		return protocolli;
	}

	public synchronized ProtocolloVO registraProtocollo(Object protocollo,
			Utente utente, boolean uscita) {
		if (uscita) {
			return registraProtocolloUscita((ProtocolloUscita) protocollo,
					utente);
		} else {
			return registraProtocolloIngresso((ProtocolloIngresso) protocollo,
					utente);
		}
	}

	public synchronized ProtocolloVO registraProtocolloPostaInterna(
			Object protocollo, Utente utente, RegistroVO reg) {
		return registraPostaInterna((PostaInterna) protocollo, utente, reg);
	}

	public int acquisizioneMassiva(Utente utente,
			HashMap<Integer, DocumentoVO> documenti) throws Exception {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		int statusFlag = ReturnValues.UNKNOWN;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			int protocolloId = 0;
			for (Iterator<Entry<Integer, DocumentoVO>> it = documenti
					.entrySet().iterator(); it.hasNext();) {
				Map.Entry<Integer, DocumentoVO> entry = it.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				protocolloId = ((Integer) key).intValue();

				DocumentoVO doc = (DocumentoVO) value;
				ProtocolloVO pVO = ProtocolloDelegate.getInstance()
						.getProtocolloById(connection, protocolloId);
				ProtocolloDelegate.getInstance().salvaDocumentoPrincipale(
						connection, pVO, doc, utente);

			}
			connection.commit();
			statusFlag = ReturnValues.SAVED;

		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.error("ProtocolloDelegate: failed acquisizioneMassiva: ");
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
		} finally {
			jdbcMan.close(connection);
		}
		return statusFlag;
	}

	public int registraProtocolliEmergenza(int numProt, Utente utente) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		int returnValue = ReturnValues.UNKNOWN;
		try {
			Timestamp dataRegistrazione = new Timestamp(
					System.currentTimeMillis());
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			int ufficioId = utente.getUfficioVOInUso().getId().intValue();
			int aooId = utente.getUfficioVOInUso().getAooId();

			String intervallo = creaStringaIntervallo(connection, utente,
					numProt - 1);
			for (int i = 0; i < numProt; i++) {
				ProtocolloRegistroEmergenza protocolloRI = null;
				protocolloRI = ProtocolloBO
						.getDefaultProtocolloRegistroEmergenza(utente);
				ProtocolloVO pVO = protocolloRI.getProtocollo();
				pVO.setOggetto("");
				pVO.setDataRegistrazione(dataRegistrazione);
				pVO.setAooId(aooId);
				pVO.setUfficioProtocollatoreId(ufficioId);
				pVO.setCaricaProtocollatoreId(utente.getCaricaInUso());
				pVO.setIntervalloNumProtocolloEmergenza(intervallo);
				pVO.setNumProtocolloEmergenza(-1);
				pVO.setStatoProtocollo("S");
				pVO.setDescrizioneAnnotazione("protocollo da registro emergenza n "
						+ String.valueOf(i + 1));
				protocolloRI.setProtocollo(pVO);
				registraProtocolloRegistrazioneEmergenza(connection,
						protocolloRI, utente);
			}
			connection.commit();
			returnValue = ReturnValues.SAVED;
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocolli Registro emergenza fallito, rolling back transction..",
					de);
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocolli Registro emergenza fallito, rolling back transction..",
					se);
		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);
		} finally {
			jdbcMan.close(connection);
		}
		return returnValue;

	}

	private String creaStringaIntervallo(Connection connection, Utente utente,
			int numProt) {
		int numeroProtocollo = 0;
		try {
			numeroProtocollo = protocolloDAO.getMaxNumProtocollo(connection,
					utente.getRegistroVOInUso().getAnnoCorrente(), utente
							.getRegistroVOInUso().getId().intValue(),
					utente.getNumPrt());
		} catch (DataException e) {
			e.printStackTrace();
		}
		return "da "
				+ StringUtil.formattaNumeroProtocollo(
						String.valueOf(numeroProtocollo), 7)
				+ " a "
				+ StringUtil.formattaNumeroProtocollo(
						String.valueOf(numeroProtocollo + numProt), 7);
	}

	private ProtocolloVO registraProtocolloRegistrazioneEmergenza(
			Connection connection, ProtocolloRegistroEmergenza protocollo,
			Utente utente) throws Exception {
		ProtocolloVO protocolloSalvato = new ProtocolloVO();
		protocolloSalvato.setReturnValue(ReturnValues.UNKNOWN);
		logger.info("ProtocolloDelegate:registraProtocolloEmergenza");

		protocolloSalvato = registraProtocollo(connection, protocollo, utente);

		protocolloSalvato.setReturnValue(ReturnValues.UNKNOWN);
		int protocolloId = protocolloSalvato.getId().intValue();

		// Registro la segnatura
		SegnaturaVO segnaturaVO = new SegnaturaVO();
		segnaturaVO.setFkProtocolloId(protocolloId);
		segnaturaVO.setRowCreatedUser(utente.getValueObject().getUsername());
		segnaturaVO.setTipoProtocollo(protocollo.getProtocollo().getFlagTipo());
		segnaturaVO.setTextSegnatura(ProtocolloBO.getSignature(protocollo));
		segnaturaVO.setId(IdentificativiDelegate.getInstance().getNextId(
				connection, NomiTabelle.SEGNATURE));
		protocolloDAO.salvaSegnatura(connection, segnaturaVO);

		protocolloSalvato.setReturnValue(ReturnValues.SAVED);

		return protocolloSalvato;
	}

	public int registraProtocolliEmergenza(int numProtIngresso,
			int numProtUscita, Utente utente) {

		int tipoDocumentoId = ProtocolloDelegate.getInstance()
				.getDocumentoDefault(utente.getRegistroVOInUso().getAooId());

		JDBCManager jdbcMan = null;
		Connection connection = null;
		int returnValue = ReturnValues.UNKNOWN;

		try {
			Timestamp dataRegistrazione = new Timestamp(
					System.currentTimeMillis());
			jdbcMan = new JDBCManager();

			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);

			int ufficioId = utente.getUfficioVOInUso().getId().intValue();
			int utenteId = utente.getValueObject().getId().intValue();
			int aooId = utente.getUfficioVOInUso().getAooId();

			AssegnatarioVO assVO;
			assVO = new AssegnatarioVO();
			assVO.setCompetente(true);
			assVO.setUfficioAssegnanteId(ufficioId);
			assVO.setUtenteAssegnanteId(utenteId);
			assVO.setUfficioAssegnatarioId(ufficioId);
			assVO.setDataAssegnazione(dataRegistrazione);

			for (int i = 0; i < numProtIngresso; i++) {
				ProtocolloIngresso protocolloIngresso = null;
				protocolloIngresso = ProtocolloBO
						.getDefaultProtocolloIngresso(utente);
				ProtocolloVO pVO = protocolloIngresso.getProtocollo();
				pVO.setOggetto("protocollo da registro emergenza");
				pVO.setFlagTipoMittente("F");
				pVO.setCognomeMittente("Registro Emergenza");
				pVO.setDataRegistrazione(dataRegistrazione);
				pVO.setAooId(aooId);
				pVO.setUfficioProtocollatoreId(ufficioId);
				pVO.setCaricaProtocollatoreId(utente.getCaricaInUso());
				pVO.setNumProtocolloEmergenza(-1);
				pVO.setTipoDocumentoId(tipoDocumentoId);
				pVO.setStatoProtocollo("S");
				pVO.setDescrizioneAnnotazione("protocollo da registro emergenza n� "
						+ String.valueOf(i + 1));
				protocolloIngresso.setProtocollo(pVO);

				protocolloIngresso.aggiungiAssegnatario(assVO);
				registraProtocolloIngresso(connection, protocolloIngresso,
						utente);

			}
			DestinatarioVO destVO = new DestinatarioVO();
			destVO.setDestinatario("protocollo Emergenza");
			destVO.setFlagTipoDestinatario("F");
			destVO.setFlagConoscenza(false);
			for (int i = 0; i < numProtUscita; i++) {
				ProtocolloUscita protocolloUscita = null;
				protocolloUscita = ProtocolloBO
						.getDefaultProtocolloUscita(utente);

				ProtocolloVO pVO = protocolloUscita.getProtocollo();
				pVO.setOggetto("protocollo da registro emergenza");
				pVO.setNumProtocolloEmergenza(-1);
				pVO.setDataRegistrazione(dataRegistrazione);
				pVO.setAooId(aooId);
				pVO.setFlagTipoMittente("G");
				pVO.setDenominazioneMittente("Registro Emergenza");
				pVO.setUfficioMittenteId(ufficioId);
				pVO.setUfficioProtocollatoreId(ufficioId);
				pVO.setCaricaProtocollatoreId(utente.getCaricaInUso());
				pVO.setTipoDocumentoId(tipoDocumentoId);
				pVO.setMozione(true);
				pVO.setStatoProtocollo("N");
				pVO.setDescrizioneAnnotazione("protocollo da registro emergenza n� "
						+ String.valueOf(i + 1));
				protocolloUscita.setProtocollo(pVO);

				protocolloUscita.setMittente(assVO);
				protocolloUscita.addDestinatari(destVO);

				registraProtocolloUscita(connection, protocolloUscita, utente);

			}
			connection.commit();
			returnValue = ReturnValues.SAVED;
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocolli Registro emergenza fallito, rolling back transction..",
					de);

		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocolli Registro emergenza fallito, rolling back transction..",
					se);

		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);

		} finally {
			jdbcMan.close(connection);
		}
		return returnValue;

	}
	
	public ProtocolloVO setPostaInternaLavorata(Connection connection, PostaInterna protocollo,
			Utente utente) {
		ProtocolloVO protocolloSalvato = new ProtocolloVO();
		protocolloSalvato.setReturnValue(ReturnValues.UNKNOWN);
		logger.info("ProtocolloDelegate:aggiornaPostaInterna");
		try {
			protocolloSalvato = aggiornaProtocollo(connection, protocollo,
					utente);
			if (protocolloSalvato.getReturnValue() == ReturnValues.SAVED) {
				protocolloSalvato.setReturnValue(ReturnValues.UNKNOWN);
				int protocolloId = protocolloSalvato.getId().intValue();
				protocolloDAO.eliminaAssegnatariProtocollo(connection,
						protocolloId);
				Collection<AssegnatarioVO> assegnatari = protocollo
						.getDestinatari();
				if (assegnatari != null) {
					for (Iterator<AssegnatarioVO> i = assegnatari.iterator(); i
							.hasNext();) {
						AssegnatarioVO assegnatario = i.next();
						assegnatario.setProtocolloId(protocolloId);
						assegnatario.setId(IdentificativiDelegate.getInstance().getNextId(connection,NomiTabelle.PROTOCOLLO_ASSEGNATARI));
						assegnatario.setLavorato(true);
						protocolloDAO.salvaAssegnatario(connection,assegnatario, protocolloSalvato.getVersione());
					}
				}
				connection.commit();
				protocolloSalvato.setReturnValue(ReturnValues.SAVED);
			} else {
				throw new DataException("setPostaInternaLavorata fallita");
			}
		} catch (DataException de) {
			logger.warn(
					"setPostaInternaLavorata fallita, rolling back transction..",
					de);

		} catch (SQLException se) {
			logger.warn(
					"setPostaInternaLavorata fallita, rolling back transction..",
					se);

		} catch (Exception e) {
			logger.warn(
					"setPostaInternaLavorata fallita, rolling back transction..",
					e);
		} 
		return protocolloSalvato;
	}


	public ProtocolloVO aggiornaPostaInterna(PostaInterna protocollo,
			Utente utente) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		ProtocolloVO protocolloSalvato = new ProtocolloVO();
		protocolloSalvato.setReturnValue(ReturnValues.UNKNOWN);
		logger.info("ProtocolloDelegate:aggiornaPostaInterna");
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);

			protocolloSalvato = aggiornaProtocollo(connection, protocollo,
					utente);
			if (protocolloSalvato.getReturnValue() == ReturnValues.SAVED) {
				protocolloSalvato.setReturnValue(ReturnValues.UNKNOWN);
				int protocolloId = protocolloSalvato.getId().intValue();
				protocolloDAO.eliminaAssegnatariProtocollo(connection,
						protocolloId);
				Collection<AssegnatarioVO> assegnatari = protocollo
						.getDestinatari();
				if (assegnatari != null) {
					for (Iterator<AssegnatarioVO> i = assegnatari.iterator(); i
							.hasNext();) {
						AssegnatarioVO assegnatario = i.next();
						assegnatario.setProtocolloId(protocolloId);
						assegnatario.setId(IdentificativiDelegate.getInstance()
								.getNextId(connection,
										NomiTabelle.PROTOCOLLO_ASSEGNATARI));
						assegnatario.setLavorato(isLavorato(
								protocollo.isLavorato(), assegnatario, utente));
						protocolloDAO.salvaAssegnatario(connection,
								assegnatario, protocolloSalvato.getVersione());
					}
				}
				connection.commit();
				protocolloSalvato.setReturnValue(ReturnValues.SAVED);
			} else {
				jdbcMan.rollback(connection);
			}
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocollo fallito, rolling back transction..",
					de);

		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Protocollo fallito, rolling back transction..",
					se);

		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);

		} finally {
			jdbcMan.close(connection);
		}
		return protocolloSalvato;
	}

	public PostaInterna getPostaInternaById(int id) {
		PostaInterna pi = new PostaInterna();
		try {
			pi.setProtocollo(protocolloDAO.getProtocolloById(id));
			pi.setAllegati(protocolloDAO.getAllegatiProtocollo(id));
			if (pi.getProtocollo().getDocumentoPrincipaleId() != null) {
				pi.setDocumentoPrincipale(DocumentoDelegate.getInstance()
						.getDocumento(
								pi.getProtocollo().getDocumentoPrincipaleId()
										.intValue()));
			}
			AssegnatarioVO assegnatario = new AssegnatarioVO();
			assegnatario.setUfficioAssegnatarioId(pi.getProtocollo()
					.getUfficioMittenteId());
			assegnatario.setUtenteAssegnatarioId(pi.getProtocollo()
					.getUtenteMittenteId());
			pi.setMittente(assegnatario);
			pi.aggiungiDestinatari(getAssegnatariProtocollo(id));
			pi.setAllacci(getAllacciProtocollo(id));
			pi.setProcedimenti(protocolloDAO.getProcedimentiProtocollo(id));
			setFascicoliProtocollo(pi, id);
			return pi;
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getProtocolloById: ", de);
			return null;
		}
	}

	public int riassegnaPosta(PostaInterna postaInterna, Utente utente) {
		try {
			return protocolloDAO.riassegnaPosta(postaInterna, utente);
		} catch (DataException de) {
			logger.error(
					"ProtocolloDelegate: failed getting riassegnaPostaInterna: ",
					de);
			return ReturnValues.UNKNOWN;
		}

	}

	public Map<Long, ReportProtocolloView> getProtocolliPerConoscenza(
			Utente utente, String tipo) {
		try {
			return protocolloDAO.getProtocolliPerConoscenza(utente, tipo);
		} catch (DataException de) {
			logger.error(
					"ProtocolloDelegate: failed getting getPostaAssegnata: ",
					de);
			return null;
		}
	}

	public int contaPerConoscenza(Utente utente, String tipo) {
		try {
			return contaProtocolliDAO.getPerConoscenza(utente, tipo);
		} catch (DataException de) {
			logger.error(
					"ProtocolloDelegate: failed getting getPostaAssegnata: ",
					de);
			return 0;
		}
	}

	public int contaPerCompetenzaZonaLavoro(Utente utente, String tipo) {
		try {
			return contaProtocolliDAO.getPerConoscenzaZonaLavoro(utente, tipo);
		} catch (DataException de) {
			logger.error(
					"ProtocolloDelegate: failed getting getPostaAssegnata: ",
					de);
			return 0;
		}
	}

	public int contaProtocolliAssegnatiPerCruscotti(int id,
			int annoProtocolloDa, int annoProtocolloA, String statoProtocollo,
			String tipo) {
		try {

			return contaProtocolliDAO.contaProtocolliPerCruscotti(id,
					annoProtocolloDa, annoProtocolloA, statoProtocollo, tipo);
		} catch (Exception de) {
			logger.error(
					"ProtocolloDelegate: failed getting contaProtocolli: ", de);
			return 0;
		}

	}

	public int contaAssegnatariPerCruscotti(int id, int annoProtocolloDa,
			int annoProtocolloA, String statoProtocollo, String tipo) {
		try {
			return contaProtocolliDAO.contaAssegnatariPerCruscotti(id,
					annoProtocolloDa, annoProtocolloA, statoProtocollo, tipo);
		} catch (Exception de) {
			logger.error(
					"ProtocolloDelegate: failed getting contaProtocolli: ", de);
			return 0;
		}

	}

	public Map<Integer, ReportProtocolloView> getProtocolliAssegnatiPerCruscotti(
			int id, int annoProtocolloDa, int annoProtocolloA, String tipo) {
		try {
			return protocolloDAO.getProtocolliAssegnatiPerCruscotti(id,
					annoProtocolloDa, annoProtocolloA, tipo);
		} catch (Exception de) {
			logger.error(
					"ProtocolloDelegate: failed getting contaProtocolli: ", de);
			return null;
		}
	}

	public Map<String, ReportProtocolloView> getProtocolliAlert(Utente utente) {
		try {
			return protocolloDAO.getProtocolliAlert(utente);
		} catch (Exception de) {
			logger.error(
					"ProtocolloDelegate: failed getting getProtocolliAlert ",
					de);
			return null;
		}
	}

	public Map<String, ProtocolloProcedimentoView> getProtocolliEvidenza(
			Utente utente) {
		try {
			return protocolloDAO.getProtocolliEvidenza(utente);
		} catch (Exception de) {
			logger.error(
					"ProtocolloDelegate: failed getting getProtocolliAlert ",
					de);
			return null;
		}
	}

	public SegnaturaVO getSegnatura(int id) {
		try {
			return protocolloDAO.getSegnatura(id);
		} catch (Exception de) {
			logger.error(
					"ProtocolloDelegate: failed getting getProtocolliAlert ",
					de);
			return null;
		}
	}

	public int contaProtocolliAlert(Utente utente) {
		try {

			return contaProtocolliDAO.contaProtocolliAlert(utente);
		} catch (Exception de) {
			logger.error(
					"ProtocolloDelegate: failed getting contaProtocolliAlert ",
					de);
			return 0;
		}
	}

	public int contaProtocolliEvidenza(Utente utente) {
		try {
			return contaProtocolliDAO.contaProtocolliEvidenza(utente);
		} catch (Exception de) {
			logger.error(
					"ProtocolloDelegate: failed getting contaProtocolliAlert ",
					de);
			return 0;
		}
	}

	public int contaFattureUfficio(Utente utente, Integer registro, String tipo) {
		try {
			return contaProtocolliDAO.contaFatture(utente, registro, tipo);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting contaFattureUfficio: ");
			return 0;
		}
	}

	public int contaFattureUtente(Utente utente, Integer registro, String tipo) {
		try {
			return contaProtocolliDAO.contaFatture(utente, registro, tipo);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting contaFattureUtente: ");
			return 0;
		}
	}

	public int contaFattureRespinte(Utente utente, Integer registro, String tipo) {
		try {
			return contaProtocolliDAO.contaFatture(utente, registro, tipo);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting contaFattureRespinte: ");
			return 0;
		}
	}

	public int contaNotifichePostaInternaView(int caricaId, int flagNotifica) {
		try {
			return contaProtocolliDAO.contaCheckPostaInternaView(caricaId,
					flagNotifica);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting contaFattureRespinte: ");
			return 0;
		}
	}

	public RegistroVO getRegistroByProtocolloId(int protocolloId) {
		RegistroVO registro = new RegistroVO();
		try {
			registro = protocolloDAO.getRegistroByProtocolloId(protocolloId);
			logger.info("getting registri");
		} catch (DataException de) {
			logger.error("Failed getting Registri");
		}
		return registro;
	}

	public boolean isUfficioAssegnatario(int protocolloId, int ufficioInUso) {
		boolean isUfficioAssegnatario = false;
		try {
			isUfficioAssegnatario = protocolloDAO.isUfficioAssegnatario(
					protocolloId, ufficioInUso);
			logger.info("getting registri");
		} catch (DataException de) {
			logger.error("Failed getting Registri");
		}
		return isUfficioAssegnatario;
	}

	public void salvaProgressivoNotifica(int aooId, String progressivo)
			throws Exception {
		GregorianCalendar today = new GregorianCalendar();
		String prev = protocolloDAO.getProgressivoNotifica(aooId,
				today.get(Calendar.YEAR));
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			int id = IdentificativiDelegate.getInstance().getNextId(connection,
					NomiTabelle.NOTIFICA_FATTURA);
			if (prev != null && !prev.equals(""))
				protocolloDAO.updateProgressivoNotifica(connection,
						progressivo, today.get(Calendar.YEAR), aooId);
			else
				protocolloDAO.newProgressivoNotifica(connection, id,
						progressivo, today.get(Calendar.YEAR), aooId);
			connection.commit();
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn(
					"salvaProgressivoNotifica fallito, rolling back transction..",
					de);
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn(
					"salvaProgressivoNotifica fallito, rolling back transction..",
					se);
		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);
		} finally {
			jdbcMan.close(connection);
		}
	}

}