package it.finsiel.siged.mvc.business;

import it.compit.fenice.mvc.business.DomandaDelegate;
import it.compit.fenice.mvc.presentation.helper.CodaInvioView;
import it.compit.fenice.mvc.vo.protocollo.DomandaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.EmailConstants;
import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.constant.NomiTabelle;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.dao.mail.MimeMessageParser;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.MessaggioEmailEntrata;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.ProtocolloIngresso;
import it.finsiel.siged.model.protocollo.ProtocolloUscita;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.integration.EmailDAO;
import it.finsiel.siged.mvc.presentation.helper.EmailView;
import it.finsiel.siged.mvc.vo.log.EventoVO;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.posta.CodaInvioVO;
import it.finsiel.siged.mvc.vo.posta.EmailVO;
import it.finsiel.siged.mvc.vo.posta.PecDestVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.FileUtil;
import it.finsiel.siged.util.PdfUtil;
import it.finsiel.siged.util.StringUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class EmailDelegate implements ComponentStatus {

	private static Logger logger = Logger.getLogger(EmailDelegate.class
			.getName());

	private int status;

	private EmailDAO emailDAO = null;

	// private AreaOrganizzativaDAO

	private static EmailDelegate delegate = null;

	private EmailDelegate() {
		try {
			if (emailDAO == null) {
				emailDAO = (EmailDAO) DAOFactory
						.getDAO(Constants.EMAIL_DAO_CLASS);
				logger.debug("EmailDAO instantiated:"
						+ Constants.EMAIL_DAO_CLASS);
				status = STATUS_OK;
			}
		} catch (Exception e) {
			status = STATUS_ERROR;
			logger.error("", e);
		}

	}

	public static EmailDelegate getInstance() {
		if (delegate == null)
			delegate = new EmailDelegate();
		return delegate;
	}

	public static String getIdentifier() {
		return Constants.ANNOTAZIONE_DELEGATE;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int s) {
		this.status = s;
	}

	public void writeDocumentToStream(int docId, OutputStream os)
			throws DataException {
		emailDAO.writeDocumentoToStream(docId, os);
	}
	
	public InputStream writeDocumentToInputStream(int docId)
			throws DataException {
		return emailDAO.writeDocumentoToInputStream(docId);
	}

	public void writeAllegatoUfficioToStream(int docId, OutputStream os)
			throws DataException {
		emailDAO.writeAllegatoUfficioToStream(docId, os);
	}

	public boolean eliminaEmail(int emailId) throws Exception {
		JDBCManager jdbcMan = null;
		boolean cancellata = false;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);

			emailDAO.aggiornaStatoEmailIngresso(connection, emailId, 2);
			emailDAO.eliminaEmailAllegati(connection, emailId);
			connection.commit();
			cancellata = true;
		} catch (SQLException e) {
			jdbcMan.rollback(connection);
			logger.error("", e);
		} catch (DataException e) {
			jdbcMan.rollback(connection);
			logger.error("", e);
		} finally {
			jdbcMan.close(connection);
		}
		return cancellata;
	}

	public boolean eliminaEmailUfficio(int emailId) throws Exception {
		JDBCManager jdbcMan = null;
		boolean cancellata = false;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);

			emailDAO.aggiornaStatoEmailUfficioIngresso(connection, emailId, 2);
			emailDAO.eliminaEmailUfficioAllegati(connection, emailId);
			connection.commit();
			cancellata = true;
		} catch (SQLException e) {
			jdbcMan.rollback(connection);
			logger.error("", e);
		} catch (DataException e) {
			jdbcMan.rollback(connection);
			logger.error("", e);
		} finally {
			jdbcMan.close(connection);
		}
		return cancellata;
	}

	public boolean eliminaEmailAllegati(int emailId) throws Exception {
		JDBCManager jdbcMan = null;
		boolean cancellata = false;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			emailDAO.eliminaEmailAllegati(connection, emailId);
			connection.commit();
			cancellata = true;
		} catch (SQLException e) {
			jdbcMan.rollback(connection);
			logger.error("", e);
		} catch (DataException e) {
			jdbcMan.rollback(connection);
			logger.error("", e);
		} finally {
			jdbcMan.close(connection);
		}
		return cancellata;
	}

	public boolean eliminaEmailUfficioAllegati(int emailId) throws Exception {
		JDBCManager jdbcMan = null;
		boolean cancellata = false;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			emailDAO.eliminaEmailUfficioAllegati(connection, emailId);
			connection.commit();
			cancellata = true;
		} catch (SQLException e) {
			jdbcMan.rollback(connection);
			logger.error("", e);
		} catch (DataException e) {
			jdbcMan.rollback(connection);
			logger.error("", e);
		} finally {
			jdbcMan.close(connection);
		}
		return cancellata;
	}

	public boolean eliminaEmailLog(String[] ids) {
		JDBCManager jdbcMan = null;
		boolean cancellati = false;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			emailDAO.eliminaEmailLog(connection, ids);
			connection.commit();
			cancellati = true;
		} catch (DataException e) {
			jdbcMan.rollback(connection);
			logger.error("", e);
		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.error("", e);
		} finally {
			jdbcMan.close(connection);
		}
		return cancellati;
	}

	public void salvaMessaggioPerInvio(Connection connection, int id,
			int aooId, int protocolloId, Collection<DestinatarioVO> destinatari)
			throws DataException {
		emailDAO.salvaMessaggioDestinatariPerInvio(connection, id, aooId, protocolloId,
				destinatari);
	}
	
	public void aggiornaMessaggioPerInvio(Connection connection, 
			int aooId, int protocolloId, Collection<DestinatarioVO> destinatari)
			throws DataException {
		CodaInvioView vo=emailDAO.getMessaggioUscitaViewByProtocolloId(protocolloId);
		if(vo.getMailId()!=0){
			if(vo.getStato()==0){
				emailDAO.eliminaEmailCodaInvioDestinatari(connection, vo.getMailId());
				emailDAO.salvaEmailCodaInvioDestinatari(connection,vo.getMailId(), destinatari);
			}
		}else{
			emailDAO.salvaMessaggioDestinatariPerInvio(connection, IdentificativiDelegate.getInstance().getNextId(connection,NomiTabelle.EMAIL_CODA_INVIO), aooId, protocolloId, destinatari);
		}
		
	}
	
	public void salvaMessaggioPerInvioErsu(Connection connection, int id,
			int aooId, int protocolloId, SoggettoVO mittente)
			throws DataException {
		emailDAO.salvaMessaggioDestinatariPerInvioErsu(connection, id, aooId,
				protocolloId, mittente);
	}

	public Collection<PecDestVO> getDestinatariMessaggioUscita(int msgId)
			throws DataException {
		return emailDAO.getDestinatariMessaggioUscita(msgId);
	}

	public Collection<Integer> getListaMessaggiUscita(int aooId)
			throws DataException {
		return emailDAO.getListaMessaggiUscita(aooId);
	}

	public Collection<EventoVO> getListaLog(int aooId, int tipoLog)
			throws DataException {
		return emailDAO.getListaLog(aooId, tipoLog);
	}

	public void segnaMessaggioComeInviato(int msgId) throws DataException {
		emailDAO.segnaMessaggioComeInviato(msgId);
	}

	public void inviaProtocolloEmail(Session session, int id,
			String tempFolder, String host, String username, String password,
			String mittenteEmail) throws DataException {
		MimeMessage messaggio = null;
		int numeroProtocollo = 0;
		try {
			// get msg
			CodaInvioVO rec = emailDAO.getMessaggioDaInviare(id);
			if (rec != null)
				numeroProtocollo = rec.getNumeroProtocollo();
			if (ReturnValues.FOUND == rec.getReturnValue()) {
				Collection<PecDestVO> destinatari = getDestinatariMessaggioUscita(id);
				if (!destinatari.isEmpty()) {
					String tipoProt = ProtocolloDelegate.getInstance()
							.getTipoProtocollo(rec.getProtocolloId());
					if (tipoProt.equals("U")) {
						ProtocolloUscita pu = ProtocolloDelegate.getInstance()
								.getProtocolloUscitaById(rec.getProtocolloId());
						messaggio = new MimeMessage(session);
						MimeMultipart multipart = new MimeMultipart();
						// oggetto
						messaggio.setSubject(ProtocolloBO.getTimbroUscita(
								Organizzazione.getInstance(),
								pu.getProtocollo()));
						// body
						MimeBodyPart messageBody = new MimeBodyPart();
						messageBody.setContent(pu.getProtocollo().getOggetto(),
								"text/plain");
						multipart.addBodyPart(messageBody);
						// segnatura (allegato)
						/*
						if(!pu.getProtocollo().isFatturaElettronica()){
							MimeBodyPart messageSegn = new MimeBodyPart();
							messageSegn.setContent(ProtocolloBO.getSignature(pu),"text/plain");
							messageSegn.setDisposition(Part.ATTACHMENT);
							messageSegn.setFileName("segnatura.xml");
							multipart.addBodyPart(messageSegn);
						}
						*/
						messaggio.setContent(multipart);
						messaggio.saveChanges();
						//allegati + doc principale
						DocumentoVO mainDoc = pu.getDocumentoPrincipale();
                        Map<String, DocumentoVO> allegati = pu.getAllegati();
                        if (mainDoc != null && mainDoc.getId() != null)
                        	ProtocolloBO.putAllegato(mainDoc, allegati);
                     for (DocumentoVO doc:allegati.values()) {
                    	 	String path = Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo()+ "/" + "aoo_" + pu.getProtocollo().getAooId() + "/" + doc.getPath();
                            File tempFile=new File(path);
                            MimeBodyPart messageBodyPart = new MimeBodyPart();
                            FileDataSource source = new FileDataSource(tempFile);
                            messageBodyPart.setDataHandler(new DataHandler(
                                    source));
                            messageBodyPart.setFileName(doc.getFileName());
                            multipart.addBodyPart(messageBodyPart);
                        }
                        messaggio.setContent(multipart);
						messaggio.saveChanges();
					} else if (tipoProt.equals("I")) {
						ProtocolloIngresso pi = ProtocolloDelegate
								.getInstance().getProtocolloIngressoById(
										rec.getProtocolloId());
						DomandaVO domanda=DomandaDelegate.getInstance().getDomandaById(pi.getProtocollo().getNumProtocolloMittente());
						messaggio = new MimeMessage(session);
						MimeMultipart multipart = new MimeMultipart();

						// oggetto
						messaggio.setSubject(ProtocolloBO.getTimbroDomandeProtocollate());
						// body
						MimeBodyPart messageBody = new MimeBodyPart();
						messageBody.setContent(ProtocolloBO.getCorpoMailDomandeProtocollate(pi.getProtocollo(),domanda),"text/html");
						multipart.addBodyPart(messageBody);
						// segnatura : allegato
						MimeBodyPart messageSegn = new MimeBodyPart();
						messageSegn.setContent(ProtocolloBO.getSignature(pi),
								"text/plain");
						messageSegn.setDisposition(Part.ATTACHMENT);
						messageSegn.setFileName("segnatura.xml");
						multipart.addBodyPart(messageSegn);

						messaggio.setContent(multipart);
						messaggio.saveChanges();
					}
					// mittente
					InternetAddress addressFrom = new InternetAddress(
							mittenteEmail);
					messaggio.setFrom(addressFrom);
					// destinatari
					Iterator<PecDestVO> dest = destinatari.iterator();
					while (dest.hasNext()) {
						PecDestVO d = dest.next();
						messaggio.addRecipient(
								MimeMessage.RecipientType.TO,
								new InternetAddress(d.getEmail(), d
										.getNominativo()));
					}
					Transport transport = session.getTransport("smtp");
					transport.connect(host, username, password);
					transport.sendMessage(messaggio,
							messaggio.getAllRecipients());
					delegate.segnaMessaggioComeInviato(id);

				} else {
					throw new DataException(
							"Il messaggio non ha destinatari. Protocollo Numero:"
									+ numeroProtocollo);
				}
			} else {
				throw new DataException(
						"Messaggio non trovato sulla base dati. Protocollo Numero:"
								+ numeroProtocollo);
			}
		} 
		catch (AddressException e) {
			logger.debug("", e);
			throw new DataException(
					" AddressException Errore nell'invio del messaggio - Protocollo Numero:"
							+ numeroProtocollo + " Messaggio Id=" + id + "\n"
							+ e.getMessage());
		} catch (UnsupportedEncodingException e) {
			logger.debug("", e);
			throw new DataException(
					"UnsupportedEncodingException Errore nell'invio del messaggio - Protocollo Numero:"
							+ numeroProtocollo
							+ " Messaggio Id="
							+ id
							+ "\n"
							+ e.getMessage());
		} catch (MessagingException e) {
			logger.debug("", e);
			throw new DataException(
					"MessagingException Errore nell'invio del messaggio - Protocollo Numero:"
							+ numeroProtocollo + " Messaggio Id=" + id + "\n"
							+ e.getMessage());
		}
	}

	public int salvaEmailIngresso(MessaggioEmailEntrata email, Date msgSentDate) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		IdentificativiDelegate idDelegate = IdentificativiDelegate
				.getInstance();
		int retVal = ReturnValues.UNKNOWN;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			int emailId = idDelegate.getNextId(connection,
					NomiTabelle.EMAIL_INGRESSO);
			EmailVO emailVO = email.getEmail();
			emailVO.setId(emailId);
			emailDAO.salvaEmail(emailVO, connection);
			if (EmailConstants.TIPO_ANOMALIA.equalsIgnoreCase(email
					.getTipoEmail()))
				emailDAO.aggiornaFlagAnomalia(emailId, connection);
			Iterator<DocumentoVO> iterator = email.getAllegati().iterator();
			while (iterator.hasNext()) {
				DocumentoVO allegato = iterator.next();
				allegato.setId(idDelegate.getNextId(connection,
						NomiTabelle.EMAIL_INGRESSO_ALLEGATI));
				emailDAO.salvaAllegato(connection, allegato, emailId);
			}
			
			Date dateControlPec = Organizzazione.getInstance().getAreaOrganizzativa(emailVO.getAooId()).getMailConfig().getDataUltimaPecRicevuta();
			if(msgSentDate.after(dateControlPec)){
				AreaOrganizzativaDelegate.getInstance().aggiornaAOODataUltimaPecRicevuta(connection,emailVO.getAooId(), msgSentDate);
				Organizzazione.getInstance().getAreaOrganizzativa(emailVO.getAooId()).getMailConfig().setDataUltimaPecRicevuta(msgSentDate);
			}
			connection.commit();
			retVal = ReturnValues.SAVED;
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio EmailIngresso fallito, rolling back transction..",
					de);

		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio EmailIngresso fallito, rolling back transction..",
					se);

		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);

		} finally {
			jdbcMan.close(connection);
		}
		return retVal;
	}

	public int salvaEmailUfficioIngresso(MessaggioEmailEntrata email,
			int ufficioId, Date msgSentDate) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		IdentificativiDelegate idDelegate = IdentificativiDelegate
				.getInstance();
		int retVal = ReturnValues.UNKNOWN;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			// if (!emailDAO.isMailUfficioPresent(connection,
			// email.getEmail(),ufficioId)) {
			int emailId = idDelegate.getNextId(connection,
					NomiTabelle.EMAIL_INGRESSO_UFFICIO);
			EmailVO emailVO = email.getEmail();
			emailVO.setId(emailId);
			emailDAO.salvaEmailUfficio(emailVO, connection, ufficioId);
			// salva allegati
			Iterator<DocumentoVO> iterator = email.getAllegati().iterator();
			while (iterator.hasNext()) {
				DocumentoVO allegato = iterator.next();
				allegato.setId(idDelegate.getNextId(connection,
						NomiTabelle.EMAIL_INGRESSO_UFFICIO_ALLEGATI));
				emailDAO.salvaAllegatoUfficio(connection, allegato, emailId);
			}
			UfficioDelegate.getInstance()
					.aggiornaUfficioDataUltimaMailRicevuta(connection,
							ufficioId, msgSentDate);
			Organizzazione.getInstance().getUfficio(ufficioId).getValueObject()
					.setDataUltimaMailRicevuta(msgSentDate);
			connection.commit();
			retVal = ReturnValues.SAVED;
			// } else
			// retVal = ReturnValues.FOUND;
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio EmailIngresso fallito, rolling back transction..",
					de);

		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio EmailIngresso fallito, rolling back transction..",
					se);

		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);

		} finally {
			jdbcMan.close(connection);
		}
		return retVal;
	}

	public boolean isMailPresent(EmailVO email) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		boolean present = false;
		try {
			// email.get
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			present = emailDAO.isMailPresent(connection, email);
		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);

		} finally {
			jdbcMan.close(connection);
		}
		return present;
	}

	public boolean isMailUfficioPresent(EmailVO email, int ufficioId) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		boolean present = false;
		try {
			// email.get
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			present = emailDAO.isMailUfficioPresent(connection, email,
					ufficioId);
		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);

		} finally {
			jdbcMan.close(connection);
		}
		return present;
	}
	
	public void allegaEmailProtocollo(MessaggioEmailEntrata email, Date msgSentDate) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			Date data = null;
			int numero = 0;
			String aoo = "";
			String result[] = StringUtil.parseOggettoMail(email.getEmail().getOggetto());
			aoo = result[2];
			numero = StringUtil.parseNumeroprotocollo(result[0]);
			data = DateUtil.toDate(result[1]);
			if (data != null && !aoo.equals("") && numero != 0) {
				int anno = DateUtil.getYear(data);
				int aooId = AreaOrganizzativaDelegate.getInstance().getAreaOrganizzativaIdByDesc(aoo);
				int protId = ProtocolloDelegate.getInstance().getProtocolloIdByAooNumeroAnno(anno, aooId,numero);
				if(protId!=0){
					ProtocolloUscita protocollo = ProtocolloDelegate.getInstance().getProtocolloUscitaById(protId);
					DocumentoVO doc = getDocFromMail(email, protocollo.getProtocollo().getKey(),protocollo.getAllegati().size());
//					TODO: ANDREA FIXING COMMENT PART 
//					Date dateControlPec = Organizzazione.getInstance().getAreaOrganizzativa(aooId).getMailConfig().getDataUltimaPecRicevuta();
//					boolean saveDocument = true;
//					if(msgSentDate.before(dateControlPec)) {
//						String fName = protocollo.getProtocollo().getKey() + "_%_" + email.getTipoEmail() + ".pdf";
//						DocumentoVO dbDoc = DocumentoDelegate.getInstance().findDocumentoByInfo(fName, doc.getImpronta(), doc.getSize());
//						if(dbDoc != null && dbDoc.getReturnValue() == ReturnValues.FOUND) { // Document already Exist!
//							logger.debug("Escluso documento '"+fName+"' poiche' risulta gia' acquisito.");
//							saveDocument = false;
//						}
//					}
//					if(saveDocument) {
						protocollo.allegaDocumento(doc);
						ProtocolloDelegate.getInstance().salvaAllegati(connection,protocollo.getProtocollo(),protocollo.getAllegati());
//					}
					Date dateControlPec = Organizzazione.getInstance().getAreaOrganizzativa(aooId).getMailConfig().getDataUltimaPecRicevuta();
					if(msgSentDate.after(dateControlPec)){
						AreaOrganizzativaDelegate.getInstance().aggiornaAOODataUltimaPecRicevuta(connection,aooId, msgSentDate);
						Organizzazione.getInstance().getAreaOrganizzativa(aooId).getMailConfig().setDataUltimaPecRicevuta(msgSentDate);
					}
					connection.commit();
				}
			}
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Allega Email a Protocollo fallito, rolling back transction..",
					de);

		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Allega Email a Protocollo fallito, rolling back transction..",
					se);

		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);

		} finally {
			jdbcMan.close(connection);
		}
	}
	
	private DocumentoVO getDocFromMail(MessaggioEmailEntrata email, long key, int size) {
		File file;
		DocumentoVO docBody = new DocumentoVO();
		try {
			file = File.createTempFile("temp_", ".upload", FileUtil.createTempDir());
			OutputStream os = new FileOutputStream(file);
			file = PdfUtil.salvaPdfDaMail(file, email);
			os.close();
			String impronta = FileUtil.calcolaDigest(file.getAbsolutePath(),FileConstants.SHA);
			docBody.setDescrizione(key + "_"+ (size+1) + "_" + email.getTipoEmail());
			docBody.setFileName(key + "_"+ (size+1) + "_" + email.getTipoEmail() + ".pdf");
			docBody.setImpronta(impronta);
			docBody.setPath(file.getAbsolutePath());
			docBody.setSize((int) file.length());
			docBody.setContentType("application/pdf");
			docBody.setRowCreatedTime(new Date(System.currentTimeMillis()));
			docBody.setRowUpdatedTime(new Date(System.currentTimeMillis()));
			docBody.setRowCreatedUser("fenice");
			docBody.setRowUpdatedUser("fenice");
		} catch (Exception e) {
			logger.error("getDocFromMail fallito:", e);
		}
		return docBody;
	}
	
	public int initEmailLog(String errore,
			int tipoLog, int aooId) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		IdentificativiDelegate idDelegate = IdentificativiDelegate.getInstance();
		int retVal = ReturnValues.UNKNOWN;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			if(!emailDAO.isMailLogPresent(connection,aooId))
				emailDAO.salvaEmailLog(connection,idDelegate.getNextId(connection, NomiTabelle.EMAIL_INGRESSO_LOGS), errore, tipoLog, aooId);
			connection.commit();
			retVal = ReturnValues.SAVED;
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn("Salvataggio EmailIngresso fallito, rolling back transaction..",de);
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn("Salvataggio EmailIngresso fallito, rolling back transaction..",se);
		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);
		} finally {
			jdbcMan.close(connection);
		}
		return retVal;
	}

	public int aggiornaEmailLog(String errore,
			int tipoLog, int aooId) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		int retVal = ReturnValues.UNKNOWN;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			emailDAO.aggiornaEmailLog(connection, errore, tipoLog, aooId);
			connection.commit();
			retVal = ReturnValues.SAVED;
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn("Salvataggio EmailIngresso fallito, rolling back transaction..",de);
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn("Salvataggio EmailIngresso fallito, rolling back transaction..",se);
		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);
		} finally {
			jdbcMan.close(connection);
		}
		return retVal;
	}
	
	public void aggiornaStatoEmailIngresso(Connection connection,
			int messaggioId, int stato) throws DataException {
		emailDAO.aggiornaStatoEmailIngresso(connection, messaggioId, stato);
	}

	public void aggiornaStatoEmailUfficioIngresso(Connection connection,
			int messaggioId, int stato) throws DataException {
		emailDAO.aggiornaStatoEmailUfficioIngresso(connection, messaggioId,
				stato);
	}

	public MessaggioEmailEntrata getMessaggioEntrata(int emailId, Utente utente)
			throws DataException {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		MessaggioEmailEntrata msg = new MessaggioEmailEntrata();
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();

			msg.setEmail(emailDAO.getEmailEntrata(connection, emailId));

			String tempFolder = utente.getValueObject().getTempFolder();
			File tempFile = null;
			OutputStream os = null;
			msg.setAllegati(emailDAO.getAllegatiEmailEntrata(connection,
					emailId));
			Iterator<DocumentoVO> it = msg.getAllegati().iterator();
			while (it.hasNext()) {
				DocumentoVO doc = it.next();
				tempFile = File.createTempFile("msg_email_", ".att", new File(
						tempFolder));
				os = new FileOutputStream(tempFile.getAbsolutePath());
				emailDAO.writeDocumentoToStream(connection, doc.getId()
						.intValue(), os);
				os.close();
				doc.setPath(tempFile.getAbsolutePath());
			}
		} catch (Exception e) {
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(connection);
		}

		return msg;
	}

	public MessaggioEmailEntrata getMailUfficio(int emailId, Utente utente)
			throws DataException {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		MessaggioEmailEntrata msg = new MessaggioEmailEntrata();
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();

			msg.setEmail(emailDAO.getEmailUfficioEntrata(connection, emailId));

			String tempFolder = utente.getValueObject().getTempFolder();
			File tempFile = null;
			OutputStream os = null;
			msg.setAllegati(emailDAO.getAllegatiEmailUfficioEntrata(connection,
					emailId));
			Iterator<DocumentoVO> it = msg.getAllegati().iterator();
			while (it.hasNext()) {
				DocumentoVO doc = it.next();
				tempFile = File.createTempFile("msg_email_", ".att", new File(
						tempFolder));
				os = new FileOutputStream(tempFile.getAbsolutePath());
				emailDAO.writeAllegatoUfficioToStream(connection, doc.getId()
						.intValue(), os);
				os.close();
				doc.setPath(tempFile.getAbsolutePath());
			}
		} catch (Exception e) {
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(connection);
		}

		return msg;
	}

	public EmailVO getEmailVO(int emailId) throws DataException {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		EmailVO msg = new EmailVO();
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();

			msg = emailDAO.getEmailEntrata(connection, emailId);

		} catch (Exception e) {
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(connection);
		}

		return msg;
	}

	public Collection<EmailView> getEmailIngressoByStato(int aooId, int stato)
			throws DataException {
		return emailDAO.getEmailIngressoByStato(aooId,stato);
	}

	public Collection<EmailView> getMessaggiUfficioDaProtocollare(int ufficioId) {
		Collection<EmailView> messaggi = null;
		try {
			messaggi = emailDAO.getMessaggiUfficioDaProtocollare(ufficioId);
		} catch (DataException e) {
			e.printStackTrace();
		}
		return messaggi;
	}

	public int countMessaggiUfficioDaProtocollare(int ufficioId) {
		int countMessaggi = 0;
		try {
			countMessaggi = emailDAO
					.countMessaggiUfficioDaProtocollare(ufficioId);
		} catch (DataException e) {
			e.printStackTrace();
		}
		return countMessaggi;
	}

	public DocumentoVO getAllegato(int docId) {
		DocumentoVO doc = new DocumentoVO();
		try {
			return emailDAO.getAllegatoEmailEntrata(docId);
		} catch (Exception e) {
			logger.warn(
					"getMessaggiDaProtocollare: Si e' verificata un eccezione non gestita.",
					e);
		}
		return doc;
	}

	public DocumentoVO getAllegatoUfficio(int docId) {
		DocumentoVO doc = new DocumentoVO();
		try {
			return emailDAO.getAllegatoEmailUfficio(docId);
		} catch (Exception e) {
			logger.warn(
					"getMessaggiDaProtocollare: Si e' verificata un eccezione non gestita.",
					e);
		}
		return doc;
	}

	public Collection<CodaInvioView> getListaMessaggiUscitaView(int aooId) {
		List<CodaInvioView> messaggi = new ArrayList<CodaInvioView>();
		try {
			messaggi = emailDAO.getListaMessaggiUscitaView(aooId);
		} catch (DataException e) {
			e.printStackTrace();
		}
		
		Comparator<CodaInvioView> c = new Comparator<CodaInvioView>() {
			public int compare(CodaInvioView cod1, CodaInvioView cod2) {
				return cod2.getProtocolloId()-cod1.getProtocolloId();
			}
		};
		
		Collections.sort(messaggi,c);
		return messaggi;
	}
	
	public Map<Integer, CodaInvioView> getListaMessaggiUscitaView(int aooId, Date dataInizio, Date dataFine,String statoMail) {
		Map<Integer, CodaInvioView> messaggi = new HashMap<Integer, CodaInvioView>();
		try {
			messaggi = emailDAO.getListaMessaggiUscitaView(aooId, dataInizio,  dataFine, statoMail);
		} catch (DataException e) {
			e.printStackTrace();
		}

		return messaggi;
	}
	
	public Collection<EmailView> cercaEmailIngresso(int aooId, Date dataInizio, Date dataFine,String statoMail) throws DataException{
		return emailDAO.cercaEmailIngresso(aooId, dataInizio,  dataFine, statoMail);
	}
	
	public int countListaMessaggiUscita(int aooId, Date dataInizio, Date dataFine,String statoMail) {
		try {
			return emailDAO.countListaMessaggiUscita(aooId, dataInizio,dataFine, statoMail);
		} catch (DataException de) {
			logger.error("EmailDelegate: failed getting countListaMessaggiUscita: "+de.getMessage());
			return 0;
		}
	}
	
	public int countEmailIngresso(int aooId, Date dataInizio, Date dataFine,String statoMail) {
		try {
			return emailDAO.countEmailIngresso(aooId, dataInizio,dataFine, statoMail);
		} catch (DataException de) {
			logger.error("EmailDelegate: failed getting countEmailIngresso: "+de.getMessage());
			return 0;
		}
	}
	
	public CodaInvioView getMessaggioUscitaView(int mailId) {
		CodaInvioView messaggio = null;
		try {
			messaggio = emailDAO.getMessaggioUscitaView(mailId);
		} catch (DataException e) {
			e.printStackTrace();
		}
		return messaggio;
	}

	public boolean eliminaEmailUscita(int mailId) {
		JDBCManager jdbcMan = null;
		boolean cancellata = false;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			emailDAO.eliminaEmailCodaInvioDestinatari(connection, mailId);
			emailDAO.eliminaEmailCodaInvio(connection, mailId);
			connection.commit();
			cancellata = true;
		} catch (SQLException e) {
			jdbcMan.rollback(connection);
			logger.error("", e);
		} catch (DataException e) {
			jdbcMan.rollback(connection);
			logger.error("", e);
		} finally {
			jdbcMan.close(connection);
		}
		return cancellata;
	}

	public boolean aggiornaMessaggioUscita(int emailId, List<PecDestVO> destinatari) throws Exception{
			JDBCManager jdbcMan = null;
			boolean aggiornata = false;
			Connection connection = null;
			try {
				jdbcMan = new JDBCManager();
				connection = jdbcMan.getConnection();
				connection.setAutoCommit(false);
				emailDAO.aggiornaEmailCodaInvio(connection, emailId, 0);
				for (PecDestVO vo : destinatari) {
					emailDAO.aggiornaEmailCodaInvioDestinatario(connection, emailId, vo.getNominativo(), vo.getEmail());
				}
				connection.commit();
				aggiornata = true;
			} catch (SQLException e) {
				jdbcMan.rollback(connection);
				logger.error("", e);
			} catch (DataException e) {
				jdbcMan.rollback(connection);
				logger.error("", e);
			} finally {
				jdbcMan.close(connection);
			}
			return aggiornata;
		
		
	}

	public EventoVO getMailLog(int aooId) {
		EventoVO messaggio = null;
		try {
			messaggio = emailDAO.getMailLog(aooId);
		} catch (DataException e) {
			e.printStackTrace();
		}
		return messaggio;
	}

	public EmailVO cercaEmailIngressoByMessageHeader(int aooId, MimeMessage msg) throws DataException {
		EmailVO evo = null;
		try {
			String from  = ((InternetAddress) msg.getFrom()[0]).getAddress();
			String msgCheck = DateUtil.formattaDataOra(msg.getSentDate().getTime()) + " " + msg.getSubject() + " " + from;
			String generatedId = MimeMessageParser.calcolaDigest(msgCheck);
			evo = emailDAO.cercaEmailIngressoByMessageHeader(aooId, msg.getMessageID(), generatedId);
		} catch (DataException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return evo;
	}
}