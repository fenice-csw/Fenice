/*
 * Created on 23-mar-2005
 *
 */
package it.finsiel.siged.util;

import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.ProtocolloUscita;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;

import java.io.InputStream;
import java.security.Security;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Folder;
import javax.mail.IllegalWriteException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.threadpool.DefaultThreadPool;
import org.apache.commons.threadpool.ThreadPool;
import org.apache.log4j.Logger;

/**
 * @author Almaviva sud
 * 
 */
public class EmailUtil {

    static Logger logger = Logger.getLogger(EmailUtil.class.getName());

    static ThreadPool threadPool = new DefaultThreadPool(10);

    // =========== USCITA ============== //

    public static MimeMessage getEmailProtocolloUscita(ProtocolloUscita pu,
            Utente utente, String segnaturaXML) throws Exception {

        try {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            session.setDebug(true);
            MimeMessage message = new MimeMessage(session);

            message.setSubject(ProtocolloBO.getTimbroUscita(Organizzazione
                    .getInstance(), pu.getProtocollo()));

            Collection<DocumentoVO> allegatiColl = pu.getAllegatiCollection();
            Iterator<DocumentoVO> allegati = allegatiColl.iterator();

            MimeMultipart multipart = new MimeMultipart();
            MimeBodyPart messageBody = new MimeBodyPart();
            messageBody.setContent(pu.getProtocollo().getOggetto(),
                    "text/plain");
            multipart.addBodyPart(messageBody);

            while (allegati.hasNext()) {
                DocumentoVO doc = (DocumentoVO) allegati.next();
                String fileName = doc.getPath();
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                FileDataSource source = new FileDataSource(fileName);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(doc.getFileName());
                multipart.addBodyPart(messageBodyPart);
            }
            // segnatura.xml
            MimeBodyPart messageSegn = new MimeBodyPart();
            messageSegn.setContent(segnaturaXML, "text/plain");
            messageSegn.setDisposition(Part.ATTACHMENT);
            messageSegn.setFileName("segnatura.xml");
            multipart.addBodyPart(messageSegn);

            message.setContent(multipart);
            message.saveChanges();
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }

    }

    // ========== UTIL ============== //

    public static MimeMessage getMessageFromStream(InputStream is)
            throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage msg = new MimeMessage(session, is);

        return msg;
    }

    /*
     * Use this method to send one email without attachement
     */
    public static void sendNoAttachement(final String host,
            final String username, final String password, final String sender,
            final String recipient, final String subject, final String htmlMsg) {

        threadPool.invokeLater(new Runnable() {
            public void run() {
                try {
                    boolean debug = false;
                    Properties props = new Properties();
                    props.put("mail.smtp.host", host);
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.sendpartial", "true");
                    Session session = Session.getDefaultInstance(props, null);
                    session.setDebug(debug);
                    InternetAddress addressFrom = new InternetAddress(sender);
                    InternetAddress addressTo = new InternetAddress(recipient);
                    InternetAddress[] adds = new InternetAddress[1];
                    adds[0] = addressTo;
                    // set the html part
                    MimeBodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setContent(htmlMsg, "text/html");
                    MimeMultipart multipart = new MimeMultipart("related");
                    multipart.addBodyPart(messageBodyPart);
                    Message message = new MimeMessage(session);
                    message.setFrom(addressFrom);
                    message.setRecipients(Message.RecipientType.TO, adds);
                    message.setSubject(subject);
                    message.setContent(multipart);
                    Transport transport = session.getTransport("smtp");
                    transport.connect(host, username, password);
                    message.saveChanges();
                    transport.sendMessage(message, message.getAllRecipients());
                } catch (AddressException e) {
                    logger.warn("", e);
                } catch (NoSuchProviderException e) {
                    logger.warn("", e);
                } catch (MessagingException e) {
                    logger.warn("", e);
                }
            }
        });
    }

    public static void riceviTramitePostaCertificata(String host,
            String username, String password) throws MessagingException,
            AddressException, IllegalWriteException {
                // System.setProperty("javax.net.ssl.trustStore",
        // "C:\\Sun\\j2sdk1.4.2_02\\jre\\lib\\security\\cacerts");
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

        // String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        String SSL_FACTORY = "it.finsiel.siged.util.ssl.EprotSSLSocketFactory";

        Properties props = new Properties();
        props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.pop3.socketFactory.fallback", "false");
        props.setProperty("mail.pop3.port", "995");
        props.setProperty("mail.pop3.socketFactory.port", "995");

        Session session = Session.getInstance(props);
        session.setDebug(true);
        Store store = session.getStore("pop3");
        store.connect(host, username, password);
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_WRITE);
        Message[] message = folder.getMessages();
        int iCount;
        iCount = message.length;
        for (int i = 0; i < (iCount); i++) {
            MimeMessage msg = (MimeMessage) message[i];
            System.out.print(msg.getSubject());
        }
    }

    // ========= VALIDATION ============ //
    public static boolean isValidEmail(String address) {
        if (address == null)
            return false;
        int atPos = address.indexOf("@");
        if (atPos < 1)
            return false;
        boolean valid;
        String personName = address.substring(0, atPos);
        String domainName = address.substring(atPos + 1);

        valid = checkName(personName);
        if (valid)
            valid = checkDomain(domainName);
        else
            valid = false;
        return valid;
    }

    public static boolean checkName(String name) {
        if (name.length() == 0)
            return false;

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!(c == '.' || c == '_' || c == '-' || Character
                    .isLetterOrDigit(c)))
                return false;
        }
        return true; // valid
    }

    public static boolean checkDomain(String domainName) {

        if (domainName.indexOf("..") != -1)
            return false;

        int len = domainName.length();
        for (int i = 0; i < len; i++) {
            char c = domainName.charAt(i);
            if (!(c == '.' || c == '-' || Character.isLetterOrDigit(c)))
                return false;
        }

        int dotPos1 = domainName.indexOf("."); // first dot
        int dotPos2 = domainName.lastIndexOf("."); // last dot

        if (dotPos1 == -1 || dotPos1 == 0)
            return false;

        String tld = domainName.substring(dotPos2 + 1);
        if (tld.length() < 2)
            return false;

        for (int i = 0; i < tld.length(); i++) {
            char c = tld.charAt(i);
            if (!Character.isLetter(c))
                return false;
        }
        return true; // valid
    }

    public static void main(String[] args) {
        try {
            EmailUtil.riceviTramitePostaCertificata("mbox.cert.legalmail.it",
                    "A01L3J", "finsiel1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
