package it.compit.fenice.dao.mail;

import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.mvc.vo.organizzazione.MailConfigVO;

import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class EmailUscita {

	static Logger logger = Logger.getLogger(EmailUscita.class.getName());

	public static void preparaInvioAvviso(int aooId, String object) {
		MailConfigVO mailVO = Organizzazione.getInstance()
				.getAreaOrganizzativa(aooId).getMailConfig();
		if (mailVO.isPrecConfigured()) {
			try {
				Properties props = new Properties();
				props.put("mail.smtp.host", mailVO.getPrecSmtp());
				props.setProperty("mail.user", mailVO.getPrecUsername());
				props.setProperty("mail.password", mailVO.getPrecPwd());
				InternetAddress to[] = InternetAddress.parse(mailVO.getPrecIndirizzoRicezione());
				InternetAddress from = new InternetAddress(mailVO.getPrecIndirizzoInvio());
				Session session = Session.getDefaultInstance(props);
				Message message = new MimeMessage(session);
				message.setFrom(from);
				message.setRecipients(Message.RecipientType.TO, to);
				message.setSubject("Avviso - PEC non funzionante");
				message.setSentDate(new Date());
				String html = "<h1>Avviso - PEC non funzionante</h1>" + object;
				
				BodyPart messageBodyPart1 = new MimeBodyPart();
				messageBodyPart1.setContent(html, "text/html");
				
				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart1);

				message.setContent(multipart);
				Transport.send(message);
				
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

}
