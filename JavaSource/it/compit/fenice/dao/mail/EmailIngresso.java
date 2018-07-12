package it.compit.fenice.dao.mail;

import it.finsiel.siged.dao.mail.LegalmailMessageParser;
import it.finsiel.siged.exception.EmailException;
import it.finsiel.siged.exception.MessageParsingException;
import it.finsiel.siged.model.MessaggioEmailEntrata;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.mvc.business.EmailDelegate;

import java.util.Date;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.sun.mail.pop3.POP3Store;

/**
 * @author Almaviva sud
 * 
 */
public class EmailIngresso {

	static Logger logger = Logger.getLogger(EmailIngresso.class.getName());

	public static void preparaProtocolliMessaggiIngresso(int aooId,
			String host, String port, Ufficio uff, String authentication,
			String tempFolder) throws EmailException {
		try {
			Properties props = new Properties();
			Session emailSession = Session.getDefaultInstance(props);
			POP3Store store = (POP3Store) emailSession.getStore("pop3");
			String username = uff.getValueObject().getEmailUsername();
			String password = uff.getValueObject().getEmailPassword();
			Date dateControlPn=uff.getValueObject().getDataUltimaMailRicevuta();
			store.connect(host, username, password);
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);
			Message[] messages = folder.getMessages();
			for (int counter = 0; counter < messages.length; counter++) {
				MimeMessage msg = (MimeMessage) messages[counter];
				MessaggioEmailEntrata pe = new MessaggioEmailEntrata();
				pe.getEmail().setAooId(aooId);
				pe.setTempFolder(tempFolder);
				EmailDelegate delegate = EmailDelegate.getInstance();
				if (!msg.isSet(Flags.Flag.DELETED)
						&& !msg.isSet(Flags.Flag.SEEN)
						&& msg.getSentDate().after(dateControlPn)) {
					try {
						LegalmailMessageParser.parseMessage(msg, pe);
						delegate.salvaEmailUfficioIngresso(pe, uff
								.getValueObject().getId(),msg.getSentDate());
					} catch (MessageParsingException e) {
						e.printStackTrace();
						logger.error("", e);
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("", e);
					}
				}
			}
			try {
				folder.close(false);
				store.close();
			} catch (MessagingException e) {
				e.printStackTrace();
				logger.warn("", e);
			}

		} catch (NoSuchProviderException e1) {
			throw new EmailException("Impossibile contattare l'host:" + host);
		} catch (MessagingException e1) {
			e1.printStackTrace();
			throw new EmailException(
					"Impossibile leggere i messaggi dalla cartella Inbox dell'host:"
							+ host + e1.getStackTrace() + " "
							+ e1.getLocalizedMessage());
		}

	}

}
