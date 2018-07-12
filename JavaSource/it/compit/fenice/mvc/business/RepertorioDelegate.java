package it.compit.fenice.mvc.business;

import it.compit.fenice.model.protocollo.PostaInterna;
import it.compit.fenice.mvc.bo.RepertorioBO;
import it.compit.fenice.mvc.integration.RepertorioDAO;
import it.compit.fenice.mvc.presentation.helper.DocumentoRepertorioView;
import it.compit.fenice.mvc.vo.repertori.DocumentoRepertorioVO;
import it.compit.fenice.mvc.vo.repertori.RepertorioVO;
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

public final class RepertorioDelegate {

	private static Logger logger = Logger.getLogger(RepertorioDelegate.class
			.getName());

	private RepertorioDAO repertorioDAO = null;

	private static RepertorioDelegate delegate = null;

	private RepertorioDelegate() {
		try {
			if (repertorioDAO == null) {
				repertorioDAO = (RepertorioDAO) DAOFactory
						.getDAO(Constants.REPERTORIO_DAO_CLASS);

				logger.debug("RepertorioDAO instantiated:"
						+ Constants.REPERTORIO_DAO_CLASS);
			}
		} catch (Exception e) {
			logger.error("Exception while connecting to CaricaDAOjdbc!!", e);
		}

	}

	public static RepertorioDelegate getInstance() {
		if (delegate == null)
			delegate = new RepertorioDelegate();
		return delegate;
	}

	public static String getIdentifier() {
		return Constants.REPERTORIO_DELEGATE;
	}

	public void sendMail(RepertorioVO repVO, Utente utente)
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
			messaggio.setSubject("NUOVO REPERTORIO");
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

	public Collection<DocumentoRepertorioView> getDocumentiRepertorio(int repId) {
		Collection<DocumentoRepertorioView> repertori = null;
		try {
			repertori = repertorioDAO.getDocumentiRepertorio(repId);
			logger.info("getting repertori");
		} catch (DataException de) {
			logger.error("RepertorioDelegate failed getRepertori");
		}
		return repertori;
	}

	public Collection<DocumentoRepertorioView> getDocumentiDaRepertoriale(int repId) {
		Collection<DocumentoRepertorioView> repertori = null;
		try {
			repertori = repertorioDAO.getDocumentiDaRepertoriale(repId);
			logger.info("getting repertori");
		} catch (DataException de) {
			logger.error("RepertorioDelegate failed getRepertori");
		}
		return repertori;
	}

	
	public int contaRepertoriByFlagWeb(int aooId, int flagWeb) {
		int countRepertori = 0;
		try {
			countRepertori = repertorioDAO.contaRepertoriByFlagWeb(aooId, flagWeb);
			logger.info("conta Repertori Interni");
		} catch (DataException de) {
			logger.error("RepertorioDelegate failed contaRepertoriInterni");
		}
		return countRepertori;
	}

	public Collection<RepertorioVO> getRepertoriByFlagWeb(int aooId, int flagWeb) {
		Collection<RepertorioVO> repertori = null;
		try {
			repertori = repertorioDAO.getRepertoriByFlagWeb(aooId, flagWeb);
			logger.info("getting repertori interni");
		} catch (DataException de) {
			logger.error("RepertorioDelegate failed getRepertoriInterni");
		}
		return repertori;
	}
	
	public Collection<RepertorioVO> getRepertori(int aooId) {
		Collection<RepertorioVO> repertori = null;
		try {
			repertori = repertorioDAO.getRepertori(aooId);
			logger.info("getting repertori");
		} catch (DataException de) {
			logger.error("RepertorioDelegate failed getRepertori");
		}
		return repertori;
	}
	

	public Collection<RepertorioVO> getRepertoriByUfficio(int ufficioId) {
		Collection<RepertorioVO> repertori = null;
		try {
			repertori = repertorioDAO.getRepertoriByUfficio(ufficioId);
			logger.info("getting repertori");
		} catch (DataException de) {
			logger.error("RepertorioDelegate failed getRepertori");
		}
		return repertori;
	}
	
	public RepertorioVO getRepertorio(int repertorioId) {
		try {
			logger.info("getting repertorio");
			return repertorioDAO.getRepertorio(repertorioId);
		} catch (DataException de) {
			logger.error("RepertorioDelegate failed getRepertori");
		}
		return null;
	}

	public boolean getRepertorioFlagWeb(int repertorioId) {
		try {
			logger.info("getting repertorioFlagWeb");
			return repertorioDAO.getRepertorioFlagWeb(repertorioId);
		} catch (DataException de) {
			logger.error("RepertorioDelegate failed repertorioFlagWeb");
		}
		return false;
	}
	
	public boolean salvaDocumentoRepertoriale(DocumentoRepertorioVO vo, Utente utente, int dfaId, int dfrId){
		JDBCManager jdbcMan = null;
		Connection connection = null;
		boolean saved=false;
		DocumentaleDelegate docDelegate=DocumentaleDelegate.getInstance();
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			vo=salvaDocumentoRepertorio(connection, utente, vo);
			if (vo.getReturnValue() == ReturnValues.SAVED) {
				delegate.aggiornaStato(vo.getDocRepertorioId(), DocumentoRepertorioVO.DA_REPERTORIALE,connection);
				docDelegate.eliminaDocumento(connection, dfaId, dfrId);
				connection.commit();
				saved=true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			jdbcMan.rollback(connection);
			logger.error("RepertorioDelegate: failed salvaDocumentoRepertorio: ");
		} finally {
			jdbcMan.close(connection);
			
		}
		return saved;
	}
	
	public DocumentoRepertorioVO salvaDocumentoRepertorio( Utente utente, DocumentoRepertorioVO vo) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		DocumentoRepertorioVO repSalvato = new DocumentoRepertorioVO();
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			repSalvato=salvaDocumentoRepertorio(connection, utente, vo);
			if(vo.getProtocolloId()!=0){
				PostaInterna pi=ProtocolloDelegate.getInstance().getPostaInternaById(vo.getProtocolloId());
				ProtocolloDelegate.getInstance().setPostaInternaLavorata(connection, pi, utente);
			}
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
			jdbcMan.rollback(connection);
			logger.error("RepertorioDelegate: failed salvaDocumentoRepertorio: ");
		} finally {
			jdbcMan.close(connection);
			
		}
		return repSalvato;
		
	}
	
	public DocumentoRepertorioVO salvaDocumentoRepertorio(Connection connection, Utente utente, DocumentoRepertorioVO vo) {
			DocumentoRepertorioVO repSalvato = new DocumentoRepertorioVO();
			try {
			if (vo.getDocRepertorioId() > 0) {
				vo.setRowUpdatedUser(utente.getValueObject().getUsername());
				repSalvato = repertorioDAO.updateDocumentoRepertorio(connection, vo);
			} else {
				vo.setRowCreatedUser(utente.getValueObject().getUsername());
				vo.setDocRepertorioId(IdentificativiDelegate.getInstance().getNextId(connection,NomiTabelle.DOCUMENTI_REPERTORI));
				repSalvato = repertorioDAO.newDocumentoRepertorio(connection,vo);
			}
			salvaAllegati(connection, repSalvato, vo.getDocumenti(),utente.getAreaOrganizzativa().getId());
			}catch (Exception e) {
				e.printStackTrace();
				logger.error("RepertorioDelegate: failed salvaDocumentoRepertorio connection: ");
			}
		return repSalvato;
	}

	private Map<String,DocumentoVO> salvaAllegati(Connection connection, DocumentoRepertorioVO vo,
			Map<String,DocumentoVO> allegati, int aooId) throws Exception {
		DocumentoDelegate documentoDelegate = DocumentoDelegate.getInstance();
		Iterator<DocumentoVO> iterator = allegati.values().iterator();
		HashMap<String,DocumentoVO> docs = new HashMap<String,DocumentoVO>(2);
		repertorioDAO.deleteAllegati(connection, vo.getDocRepertorioId());
		
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
				RepertorioBO.putAllegato(doc, docs);
				repertorioDAO.salvaAllegati(connection,
						vo.getDocRepertorioId(), doc.getId().intValue(), doc
								.getRiservato(),doc.getType(), doc.getPrincipale(), doc.getPubblicabile());
			}
		}
		return docs;
	}

	public void salvaRepertorio(Utente utente, RepertorioVO repVO) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			repVO.setRepertorioId(IdentificativiDelegate.getInstance()
					.getNextId(connection, NomiTabelle.REPERTORI));
			repertorioDAO.newRepertorio(connection, repVO);
			connection.commit();
		} catch (DataException de) {
			de.printStackTrace();
			jdbcMan.rollback(connection);
			logger.error("RepertorioDelegate: failed salvaRepertorio: ");
		} catch (SQLException se) {
			se.printStackTrace();
			jdbcMan.rollback(connection);
		} catch (Exception e) {
			e.printStackTrace();
			jdbcMan.rollback(connection);
			logger.error("RepertorioDelegate: failed salvaRepertorio: ");
		} finally {
			jdbcMan.close(connection);
		}

	}

	public void updateRepertorio(Utente utente, RepertorioVO repVO) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			repertorioDAO.updateRepertorio(connection, repVO);
			
		} catch (DataException de) {
			de.printStackTrace();
			jdbcMan.rollback(connection);
			logger.error("RepertorioDelegate: failed updateRepertorio: ");
		} catch (SQLException se) {
			se.printStackTrace();
			jdbcMan.rollback(connection);
		} catch (Exception e) {
			e.printStackTrace();
			jdbcMan.rollback(connection);
			logger.error("RepertorioDelegate: failed updateRepertorio: ");
		} finally {
			jdbcMan.close(connection);
		}

	}
	
	public DocumentoRepertorioVO getDocumentoRepertorio(int docId) {
		try {
			logger.info("getting repertorio");
			DocumentoRepertorioVO doc=repertorioDAO.getDocumentoRepertorio(docId);
			return doc;
		} catch (DataException de) {
			logger.error("RepertorioDelegate failed getRepertori");
		}
		return null;
	}

	public InputStream getDocumentData(int docId) {
		try {
			logger.info("getDocumentData");
			return repertorioDAO.getDocumentData(docId);
		} catch (DataException de) {
			logger.error("RepertorioDelegate failed getDocumentData");
		}
		return null;
	}

	public boolean isNumeroDocumentoRepertorioUsed(DocumentoRepertorioVO vo) {
		try {
			logger.info("isNumeroDocumentoRepertorioUsed");
			return repertorioDAO.isNumeroDocumentoRepertorioUsed(vo);
		} catch (DataException de) {
			logger.error("RepertorioDelegate failed isNumeroDocumentoRepertorioUsed");
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
			repertorioDAO.getDocumentiRepertoriScaduti(connection);
			connection.commit();
		} catch (Exception de) {
			de.printStackTrace();
			jdbcMan.rollback(connection);
			logger.error("RepertorioDelegate: failed archiviaDocumentiScaduti ");
		} finally {
			jdbcMan.close(connection);
		}
	}

	public int getMaxNumeroRepertorio(int repId, int annoCorrente) {
		int num=0;
		try {
			logger.info("getMaxNumeroRepertorio");
			num=repertorioDAO.getMaxNumeroRepertorio(repId,annoCorrente);
		} catch (DataException de) {
			logger.error("RepertorioDelegate failed getMaxNumeroRepertorio");
		}
		return num;
	}

	public boolean aggiornaStato(int docRepertorioId, int stato,Connection connection) {
		boolean saved=false;
		try {
			logger.info("getMaxNumeroRepertorio");
			saved=repertorioDAO.aggiornaStato(docRepertorioId, stato, connection);
		} catch (DataException de) {
			logger.error("RepertorioDelegate failed aggiornaStato");
		}
		return saved;
	}
	
	public boolean aggiornaStato(int docRepertorioId, int stato) {
		boolean saved=false;
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			saved=repertorioDAO.aggiornaStato(docRepertorioId, stato, connection);
		} catch (Exception de) {
			logger.error("RepertorioDelegate failed aggiornaStato");
		}finally {
			jdbcMan.close(connection);
		}
		return saved;
	}
}
