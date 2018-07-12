package it.compit.fenice.mvc.business;

import it.compit.fenice.model.protocollo.PostaInterna;
import it.compit.fenice.mvc.bo.AmmTrasparenteBO;
import it.compit.fenice.mvc.integration.AmmTrasparenteDAO;
import it.compit.fenice.mvc.presentation.helper.DocumentoAmmTrasparenteView;
import it.compit.fenice.mvc.vo.ammtrasparente.AmmTrasparenteVO;
import it.compit.fenice.mvc.vo.ammtrasparente.DocumentoAmmTrasparenteVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.NomiTabelle;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.DocumentaleDelegate;
import it.finsiel.siged.mvc.business.DocumentoDelegate;
import it.finsiel.siged.mvc.business.IdentificativiDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public final class AmmTrasparenteDelegate {

	private static Logger logger = Logger.getLogger(AmmTrasparenteDelegate.class
			.getName());

	private AmmTrasparenteDAO ammTrasparenteDAO = null;

	private static AmmTrasparenteDelegate delegate = null;

	private AmmTrasparenteDelegate() {
		try {
			if (ammTrasparenteDAO == null) {
				ammTrasparenteDAO = (AmmTrasparenteDAO) DAOFactory
						.getDAO(Constants.AMM_TRASPARENTE_DAO_CLASS);

				logger.debug("AmmTrasparenteDAO instantiated:"
						+ Constants.AMM_TRASPARENTE_DAO_CLASS);
			}
		} catch (Exception e) {
			logger.error("Exception while connecting to CaricaDAOjdbc!!", e);
		}

	}

	public static AmmTrasparenteDelegate getInstance() {
		if (delegate == null)
			delegate = new AmmTrasparenteDelegate();
		return delegate;
	}

	public static String getIdentifier() {
		return Constants.AMM_TRASPARENTE_DELEGATE;
	}

	public void sendMail(AmmTrasparenteVO repVO, Utente utente)
			throws DataException {
		MimeMessage messaggio = null;
		Organizzazione org = Organizzazione.getInstance();
		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", utente.getMailConfig()
					.getPnSmtp());
			Session session = Session.getDefaultInstance(props);
			messaggio = new MimeMessage(session);
			MimeMultipart multipart = new MimeMultipart();
			messaggio.setSubject("NUOVO SEZ. AMM_TRASPARENTE");
			MimeBodyPart messageBody = new MimeBodyPart();
			messageBody.setContent(repVO.getDescrizione(), "text/plain");
			multipart.addBodyPart(messageBody);
			InternetAddress addressFrom = new InternetAddress(utente
					.getValueObject().getEmailAddress());
			messaggio.setFrom(addressFrom);
			Utente dest = org.getUtente(org
					.getCarica(repVO.getResponsabileId()).getUtenteId());
			messaggio
					.addRecipient(MimeMessage.RecipientType.TO,
							new InternetAddress(dest.getValueObject()
									.getEmailAddress()));
			messaggio.setContent(multipart);
			messaggio.saveChanges();
			Transport transport = session.getTransport("smtp");
			transport.connect(utente.getMailConfig().getPnSmtp(),
					utente.getMailConfig().getPnUsername(), utente
							.getMailConfig().getPnPwd());
			transport.sendMessage(messaggio, messaggio.getAllRecipients());
		} catch (AddressException e) {
			logger.debug("", e);
			throw new DataException("Errore nell'invio del messaggio -"
					+ e.getMessage());
		} catch (MessagingException e) {
			logger.debug("", e);
			throw new DataException("Errore nell'invio del messaggio -"
					+ e.getMessage());
		}
	}

	public Collection<DocumentoAmmTrasparenteView> getDocumentiSezione(int repId) {
		Collection<DocumentoAmmTrasparenteView> sezioni = null;
		try {
			sezioni = ammTrasparenteDAO.getDocumentiSezione(repId);
			logger.info("getting sezioni");
		} catch (DataException de) {
			logger.error("AmmTrasparenteDelegate failed getSezioni");
		}
		return sezioni;
	}

	public Collection<DocumentoAmmTrasparenteView> getDocumentiDaSezionale(int repId) {
		Collection<DocumentoAmmTrasparenteView> sezioni = null;
		try {
			sezioni = ammTrasparenteDAO.getDocumentiDaSezionale(repId);
			logger.info("getting sezioni");
		} catch (DataException de) {
			logger.error("AmmTrasparenteDelegate failed getSezioni");
		}
		return sezioni;
	}

	
	public int contaSezioniByFlagWeb(int aooId, int flagWeb) {
		int countSezioni = 0;
		try {
			countSezioni = ammTrasparenteDAO.contaSezioniByFlagWeb(aooId, flagWeb);
			logger.info("conta Sezioni Interni");
		} catch (DataException de) {
			logger.error("AmmTrasparenteDelegate failed contaSezioniInterni");
		}
		return countSezioni;
	}

	public Collection<AmmTrasparenteVO> getSezioniByFlagWeb(int aooId, int flagWeb) {
		Collection<AmmTrasparenteVO> sezioni = null;
		try {
			sezioni = ammTrasparenteDAO.getSezioniByFlagWeb(aooId, flagWeb);
			logger.info("getting sezioni interni");
		} catch (DataException de) {
			logger.error("AmmTrasparenteDelegate failed getSezioniInterni");
		}
		return sezioni;
	}
	
	public Collection<AmmTrasparenteVO> getSezioni(int aooId) {
		Collection<AmmTrasparenteVO> sezioni = null;
		try {
			sezioni = ammTrasparenteDAO.getSezioni(aooId);
			logger.info("getting sezioni");
		} catch (DataException de) {
			logger.error("AmmTrasparenteDelegate failed getSezioni");
		}
		return sezioni;
	}
	

	public Collection<AmmTrasparenteVO> getSezioniByUfficio(int ufficioId) {
		Collection<AmmTrasparenteVO> sezioni = null;
		try {
			sezioni = ammTrasparenteDAO.getSezioniByUfficio(ufficioId);
			logger.info("getting sezioni");
		} catch (DataException de) {
			logger.error("AmmTrasparenteDelegate failed getSezioni");
		}
		return sezioni;
	}
	
	public AmmTrasparenteVO getSezione(int sezioneId) {
		try {
			logger.info("getting sezione");
			return ammTrasparenteDAO.getSezione(sezioneId);
		} catch (DataException de) {
			logger.error("AmmTrasparenteDelegate failed getSezioni");
		}
		return null;
	}

	public boolean getSezioneFlagWeb(int sezioneId) {
		try {
			logger.info("getting sezioneFlagWeb");
			return ammTrasparenteDAO.getSezioneFlagWeb(sezioneId);
		} catch (DataException de) {
			logger.error("AmmTrasparenteDelegate failed sezioneFlagWeb");
		}
		return false;
	}
	
	public boolean salvaDocumentoSezionale(DocumentoAmmTrasparenteVO vo, Utente utente, int dfaId, int dfrId){
		JDBCManager jdbcMan = null;
		Connection connection = null;
		boolean saved=false;
		DocumentaleDelegate docDelegate=DocumentaleDelegate.getInstance();
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			vo=salvaDocumentoAmmTrasparente(connection, utente, vo);
			if (vo.getReturnValue() == ReturnValues.SAVED) {
				delegate.aggiornaStato(vo.getDocSezioneId(), DocumentoAmmTrasparenteVO.DA_SEZIONALE,connection);
				docDelegate.eliminaDocumento(connection, dfaId, dfrId);
				connection.commit();
				saved=true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			jdbcMan.rollback(connection);
			logger.error("AmmTrasparenteDelegate: failed salvaDocumentoAmmTrasparente: ");
		} finally {
			jdbcMan.close(connection);
			
		}
		return saved;
	}
	
	public DocumentoAmmTrasparenteVO salvaDocumentoAmmTrasparente( Utente utente, DocumentoAmmTrasparenteVO vo) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		DocumentoAmmTrasparenteVO repSalvato = new DocumentoAmmTrasparenteVO();
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			repSalvato=salvaDocumentoAmmTrasparente(connection, utente, vo);
			if(vo.getProtocolloId()!=0){
				PostaInterna pi=ProtocolloDelegate.getInstance().getPostaInternaById(vo.getProtocolloId());
				ProtocolloDelegate.getInstance().setPostaInternaLavorata(connection, pi, utente);
			}
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
			jdbcMan.rollback(connection);
			logger.error("AmmTrasparenteDelegate: failed salvaDocumentoAmmTrasparente: ");
		} finally {
			jdbcMan.close(connection);
			
		}
		return repSalvato;
		
	}
	
	public DocumentoAmmTrasparenteVO salvaDocumentoAmmTrasparente(Connection connection, Utente utente, DocumentoAmmTrasparenteVO vo) {
			DocumentoAmmTrasparenteVO repSalvato = new DocumentoAmmTrasparenteVO();
			try {
			if (vo.getDocSezioneId() > 0) {
				vo.setRowUpdatedUser(utente.getValueObject().getUsername());
				repSalvato = ammTrasparenteDAO.updateDocumentoSezione(connection, vo);
			} else {
				vo.setRowCreatedUser(utente.getValueObject().getUsername());
				vo.setDocSezioneId(IdentificativiDelegate.getInstance().getNextId(connection,NomiTabelle.DOCUMENTI_AMM_TRASPARENTE));
				repSalvato = ammTrasparenteDAO.newDocumentoSezione(connection,vo);
			}
			salvaAllegati(connection, repSalvato, vo.getDocumenti(),utente.getAreaOrganizzativa().getId());
			}catch (Exception e) {
				e.printStackTrace();
				logger.error("AmmTrasparenteDelegate: failed salvaDocumentoAmmTrasparente connection: ");
			}
		return repSalvato;
	}

	private Map<String,DocumentoVO> salvaAllegati(Connection connection, DocumentoAmmTrasparenteVO vo,
			Map<String,DocumentoVO> allegati, int aooId) throws Exception {
		DocumentoDelegate documentoDelegate = DocumentoDelegate.getInstance();
		Iterator<DocumentoVO> iterator = allegati.values().iterator();
		HashMap<String,DocumentoVO> docs = new HashMap<String,DocumentoVO>(2);
		ammTrasparenteDAO.deleteAllegati(connection, vo.getDocSezioneId());
		
		String fileBaseAOO = Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo() + "/" + "aoo_" + aooId + "/";
		String currentYearDir = DateUtil.getAnnoCorrente() + "/";
		int response = FileUtil.createForCurrentYear(fileBaseAOO, currentYearDir);
		while (iterator.hasNext()) {
			DocumentoVO doc = (DocumentoVO) iterator.next();
			String path = currentYearDir + String.valueOf(vo.getKey()) + "_" + doc.getFileName();
			String docPath = fileBaseAOO + path;
			int idx = doc.getIdx();
			if (doc != null) {
				if (doc.getId() == null || doc.isMustCreateNew()) {
					File in = new File(doc.getPath());
					FileInputStream fis = new FileInputStream(in);
					FileUtil.writeFile(fis, docPath);
					doc.setPath(path);
					documentoDelegate.salvaDocumentoPerProtocollo(
							connection, doc, docPath);
				}
				doc.setIdx(idx);
				AmmTrasparenteBO.putAllegato(doc, docs);
				ammTrasparenteDAO.salvaAllegati(connection,
						vo.getDocSezioneId(), doc.getId().intValue(), doc
								.getRiservato(),doc.getType(), doc.getPrincipale(), doc.getPubblicabile());
			}
		}
		return docs;
	}

	public void salvaSezione(Utente utente, AmmTrasparenteVO repVO) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			repVO.setSezioneId(IdentificativiDelegate.getInstance()
					.getNextId(connection, NomiTabelle.AMM_TRASPARENTE));
			ammTrasparenteDAO.newSezione(connection, repVO);
			connection.commit();
		} catch (DataException de) {
			de.printStackTrace();
			jdbcMan.rollback(connection);
			logger.error("AmmTrasparenteDelegate: failed salvaSezione: ");
		} catch (SQLException se) {
			se.printStackTrace();
			jdbcMan.rollback(connection);
		} catch (Exception e) {
			e.printStackTrace();
			jdbcMan.rollback(connection);
			logger.error("AmmTrasparenteDelegate: failed salvaSezione: ");
		} finally {
			jdbcMan.close(connection);
		}

	}

	public void updateSezione(Utente utente, AmmTrasparenteVO repVO) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			ammTrasparenteDAO.updateSezione(connection, repVO);
			
		} catch (DataException de) {
			de.printStackTrace();
			jdbcMan.rollback(connection);
			logger.error("AmmTrasparenteDelegate: failed updateSezione: ");
		} catch (SQLException se) {
			se.printStackTrace();
			jdbcMan.rollback(connection);
		} catch (Exception e) {
			e.printStackTrace();
			jdbcMan.rollback(connection);
			logger.error("AmmTrasparenteDelegate: failed updateSezione: ");
		} finally {
			jdbcMan.close(connection);
		}

	}
	
	public DocumentoAmmTrasparenteVO getDocumentoAmmTrasparente(int docId) {
		try {
			logger.info("getting sezione");
			DocumentoAmmTrasparenteVO doc=ammTrasparenteDAO.getDocumentoSezione(docId);
			return doc;
		} catch (DataException de) {
			logger.error("AmmTrasparenteDelegate failed getSezioni");
		}
		return null;
	}

	public InputStream getDocumentData(int docId) {
		try {
			logger.info("getDocumentData");
			return ammTrasparenteDAO.getDocumentData(docId);
		} catch (DataException de) {
			logger.error("AmmTrasparenteDelegate failed getDocumentData");
		}
		return null;
	}

	public boolean isNumeroDocumentoSezioneUsed(DocumentoAmmTrasparenteVO vo) {
		try {
			logger.info("isNumeroDocumentoSezioneUsed");
			return ammTrasparenteDAO.isNumeroDocumentoSezioneUsed(vo);
		} catch (DataException de) {
			logger.error("AmmTrasparenteDelegate failed isNumeroDocumentoSezioneUsed");
		}
		return false;
	}

	public void archiviaDocumentiScaduti() {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			ammTrasparenteDAO.getDocumentiSezioneScaduti(connection);
			connection.commit();
		} catch (Exception de) {
			de.printStackTrace();
			jdbcMan.rollback(connection);
			logger.error("AmmTrasparenteDelegate: failed archiviaDocumentiScaduti ");
		} finally {
			jdbcMan.close(connection);
		}
	}

	public int getMaxNumeroAmmTrasparente(int repId, int annoCorrente) {
		int num=0;
		try {
			logger.info("getMaxNumeroAmmTrasparente");
			num=ammTrasparenteDAO.getMaxNumeroSezione(repId,annoCorrente);
		} catch (DataException de) {
			logger.error("AmmTrasparenteDelegate failed getMaxNumeroAmmTrasparente");
		}
		return num;
	}

	public boolean aggiornaStato(int docSezioneId, int stato,Connection connection) {
		boolean saved=false;
		try {
			logger.info("aggiornaStato");
			saved=ammTrasparenteDAO.aggiornaStato(docSezioneId, stato, connection);
		} catch (DataException de) {
			logger.error("AmmTrasparenteDelegate failed aggiornaStato");
		}
		return saved;
	}
	
	public boolean aggiornaStato(int docSezioneId, int stato) {
		boolean saved=false;
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			saved=ammTrasparenteDAO.aggiornaStato(docSezioneId, stato, connection);
		} catch (Exception de) {
			logger.error("AmmTrasparenteDelegate failed aggiornaStato");
		}finally {
			jdbcMan.close(connection);
		}
		return saved;
	}
}
