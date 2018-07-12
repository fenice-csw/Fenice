/*
 * Created on 17-gen-2005
 *
 * 
 */
package it.finsiel.siged.dao.mail;

import it.compit.fenice.dao.mail.EmailUscita;
import it.finsiel.siged.constant.EmailConstants;
import it.finsiel.siged.exception.EmailException;
import it.finsiel.siged.exception.MessageParsingException;
import it.finsiel.siged.model.MessaggioEmailEntrata;
import it.finsiel.siged.mvc.business.EmailDelegate;
import it.finsiel.siged.mvc.vo.posta.EmailVO;
import it.finsiel.siged.util.DateUtil;

import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.AndTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;

import org.apache.log4j.Logger;

import com.sun.mail.util.MailSSLSocketFactory;

public class PecEmailIngresso {

	static Logger logger = Logger.getLogger(PecEmailIngresso.class.getName());
	
	public static void preparaProtocolliMessaggiIngresso(int aooId,
			String host, String port, String username, String password,
			String authentication, Date dateControlPec, String tempFolder, int intervallo) throws EmailException {
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		Properties props = new Properties();
		props.setProperty("mail.pop3.port", port);
		props.setProperty("mail.pop3.ssl.enable", "true");
		props.setProperty("mail.pop3.socketFactory.port", port);
		//props.setProperty("mail.debug", "true");
		MailSSLSocketFactory socketFactory=null;
		try {
			socketFactory = new MailSSLSocketFactory();
		} catch (GeneralSecurityException e3) {
			logger.error("", e3);
			throw new EmailException("Errore nella lettura/elaborazione dei messaggi di posta elettronica.\nErrore: " + e3.getMessage());
		}
		socketFactory.setTrustedHosts(new String[]{host});
		props.put("mail.pop3.ssl.socketFactory", socketFactory);
		Store store = null;
		EmailDelegate delegate = EmailDelegate.getInstance();
		try {
			Session session = Session.getInstance(props);
			//session.setDebug(true);
			store = session.getStore("pop3");
			store.connect(host, username, password);
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);
			FlagTerm unseenFlagTerm = new FlagTerm(new Flags(Flags.Flag.SEEN), false); // MESSAGGI NON LETTI (QUESTO FILTRO NON FUNZIONA CORRETTAMENTE IN POP3)
			FlagTerm undeleteFlagTerm = new FlagTerm(new Flags(Flags.Flag.DELETED), false);
			//GregorianCalendar gc = new GregorianCalendar();
			//gc.setTime(dateControlPec);
//			gc.add(GregorianCalendar.MINUTE, (intervallo*-1));
			//gc.add(GregorianCalendar.MINUTE, -1);
			//SentDateTerm sentDateTerm = new SentDateTerm(SentDateTerm.GT, gc.getTime());
			List<SearchTerm> searchAndTermsList = new ArrayList<SearchTerm>();
			searchAndTermsList.add(unseenFlagTerm);  // MESSAGGI NON LETTI (QUESTO CONTROLLO NON FUNZIONA IN POP3)
			searchAndTermsList.add(undeleteFlagTerm); // MESSAGGI NON ELIMINATI
			//searchAndTermsList.add(sentDateTerm); // DATA NON INFERIORE A <intervallo> MIN. PRIMA DELL'ULTIMA EMAIL
			SearchTerm searchTerm = new AndTerm(searchAndTermsList.toArray(new SearchTerm[]{}));
			Message[] messages = folder.search(searchTerm);
			Comparator<Message> messageComparator= new Comparator<Message>() {
				public int compare(Message m1, Message m2) {
					try {
						if (m1.getSentDate() == null && m2.getSentDate() == null) {
						        return 0;
						} else if (m1.getSentDate() == null && m2.getSentDate() != null) {
					        return -1;
						} else if (m1.getSentDate() != null && m2.getSentDate() == null) {
					        return 1;
						}
						return m1.getSentDate().compareTo(m2.getSentDate());
					} catch (MessagingException e) {
						logger.error("", e);
						return 0;
					}
				}
			};
			Arrays.sort(messages, messageComparator);
			for (int counter = 0; counter < messages.length; counter++) {
				try {
					MimeMessage msg = (MimeMessage) messages[counter];
					String from  = ((InternetAddress) msg.getFrom()[0]).getAddress();
//					boolean checkMessage = msg.getSentDate().before(dateControlPec);
//					boolean msgIsPresent = false;
//					if(checkMessage) {
//						EmailVO foundEmail = delegate.cercaEmailIngressoByMessageHeader(aooId, (MimeMessage) msg);
//						if(foundEmail != null) { // SE MESSAGGIO NON ESISTENTE DEVE ESSERE ACQUISITO
//							logger.debug("MAIL:Messaggio GIA' PRESENTE nel sistema, esistente con id '"+foundEmail.getId()+"' ");
//							msgIsPresent = true;
//						}
//					}
//					if(!checkMessage || !msgIsPresent) {
					if(msg.getSentDate().after(dateControlPec)) {
						String msgCheck = DateUtil.formattaDataOra(msg.getSentDate().getTime()) + " " + msg.getSubject() + " " + from;
						logger.debug("MAIL: Acquisizione Messggio => "+msgCheck);
						MessaggioEmailEntrata pe = new MessaggioEmailEntrata();
						pe.getEmail().setAooId(aooId);
						pe.setTempFolder(tempFolder);
						try {
							LegalmailMessageParser.parseCertMessage(msg, pe);
							if (EmailConstants.TIPO_POSTA_CERTIFICATA.equalsIgnoreCase(pe.getTipoEmail())) {
								delegate.salvaEmailIngresso(pe, msg.getSentDate());
							} else if (EmailConstants.TIPO_ANOMALIA.equalsIgnoreCase(pe.getTipoEmail())) {
								delegate.salvaEmailIngresso(pe, msg.getSentDate());
							} else if (EmailConstants.TIPO_ACCETTAZIONE.equalsIgnoreCase(pe.getTipoEmail())
									|| EmailConstants.TIPO_CONSEGNA.equalsIgnoreCase(pe.getTipoEmail())
									|| EmailConstants.TIPO_MANCATA_CONSEGNA.equalsIgnoreCase(pe.getTipoEmail())) {
								delegate.allegaEmailProtocollo(pe, msg.getSentDate());
							}
						} catch (MessageParsingException e) {
							logger.error("", e);
						} catch (Exception e) {
							logger.error("", e);
						}
					}
				} catch (Exception e2) {
					logger.error("", e2);
					throw new EmailException("Errore nella lettura/elaborazione dei messaggi di posta elettronica.\nErrore: " + e2.getMessage());
				}
			}
			try {
				folder.close(false);
				store.close();
			} catch (MessagingException e) {
				logger.error("", e);
			}
			delegate.aggiornaEmailLog("Host:" + host + " - Mail scaricate con successo", EmailConstants.SUCCESS_EMAIL_INGRESSO, aooId);
		} catch (NoSuchProviderException e1) {
			delegate.aggiornaEmailLog("Impossibile contattare l'host:" + host + " - " + e1.getLocalizedMessage(), EmailConstants.ERROR_EMAIL_INGRESSO, aooId);
			EmailUscita.preparaInvioAvviso(aooId, "Impossibile contattare l'host:" + host + " - " + e1.getLocalizedMessage());
			throw new EmailException("Impossibile contattare l'host:" + host);
		} catch (MessagingException e1) {
			delegate.aggiornaEmailLog("Impossibile leggere i messaggi dalla cartella Inbox dell'host:" + host + " - " + e1.getLocalizedMessage(), EmailConstants.ERROR_EMAIL_INGRESSO, aooId);
			EmailUscita.preparaInvioAvviso(aooId, "Impossibile leggere i messaggi dalla cartella Inbox dell'host:" + host + " - " + e1.getLocalizedMessage());
			throw new EmailException("Impossibile leggere i messaggi dalla cartella Inbox dell'host:" + host + "\n" + e1.getStackTrace() + " " + e1.getLocalizedMessage());
		}
	}
}
