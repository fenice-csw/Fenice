/*

 */
package it.finsiel.siged.dao.mail;

import it.compit.fenice.util.HtmlLocalParser;
import it.compit.fenice.util.XMLUtil;
import it.finsiel.siged.constant.EmailConstants;
import it.finsiel.siged.exception.MessageParsingException;
import it.finsiel.siged.model.MessaggioEmailEntrata;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.FileUtil;
import it.finsiel.siged.util.HtmlParser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;

public class MimeMessageParser {

	static Logger logger = Logger.getLogger(MimeMessageParser.class.getName());

	// ========= ENTRATA ============= //
	public static void getProtocolloEmail(MimeMessage msg,
			MessaggioEmailEntrata pe) throws MessageParsingException {
		String subj = "";
		String from = "";

		try {
			// getting general infos
			if (msg.getFrom() != null && msg.getFrom().length > 0) {
				from = ((InternetAddress) msg.getFrom()[0]).getAddress();
				pe.getEmail().setEmailMittente(from);
				pe.getEmail().setNomeMittente(
						((InternetAddress) msg.getFrom()[0]).getPersonal());
			}
			subj = msg.getSubject();
			pe.getEmail().setOggetto(subj);
			pe.getEmail().setDataSpedizione(msg.getSentDate());
			pe.getEmail().setDataRicezione(msg.getReceivedDate());
			if (msg.getMessageID() != null
					&& !msg.getMessageID().trim().equals(""))
				pe.getEmail().setMessageIdHeader(msg.getMessageID());
			else
				pe.getEmail().setMessageIdHeader(
						calcolaDigest(DateUtil.formattaDataOra(msg.getSentDate().getTime()) + " "
								+ msg.getSubject() + " " + from));
			// ======= P A R S I N G - M E S S A G E ======= //
			Object content = msg.getContent();
			if (content instanceof Multipart) {
				gestisciMultiPart((MimeMultipart) content, pe);
			} else if (content instanceof Part) {
				gestisciPart((Part) content, pe);
			} else if (content instanceof String
					&& msg.isMimeType("text/plain")) {
				gestisciBodyAsText((String) content, pe);
			} else if (content instanceof String && msg.isMimeType("text/html")) {
				gestisciBodyAsText((String) content, pe);
			} else {
				logger.warn("MimeMessage part di tipo non gestito:" + content);
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new MessageParsingException(
					"Errore nell'elaborazione del messaggio.\nErrore:"
							+ e.getMessage());
		}
	}

	public static String calcolaDigest(String input) {
		String result = "";
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(input.getBytes("UTF8"));
			byte s[] = m.digest();
			for (int i = 0; i < s.length; i++) {
				result += Integer.toHexString((0x000000ff & s[i]) | 0xffffff00).substring(6);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return result;
	}

	private static void gestisciPart(Part part, MessaggioEmailEntrata pe)
			throws MessagingException, IOException {

		if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())
				&& part.getContent() instanceof String) {
			gestisciAllegatoTesto(part, pe);
		} else if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())
				&& part.getContent() instanceof InputStream) {
			gestisciAllegato(part, pe);
		} else if (Part.INLINE.equalsIgnoreCase(part.getDisposition())
				&& part.getContent() instanceof String) {
			if (part.isMimeType("text/html")) {
				gestisciBodyAsText((String) part.getContent(), pe);
			} else {
				gestisciBodyAsText((String) part.getContent(), pe);
			}
		} else {
			logger.warn("Part di tipo sconosciuta:" + part.getClass());
		}
	}

	private static void gestisciMultiPart(MimeMultipart multipart,
			MessaggioEmailEntrata pe) throws MessagingException, IOException {
		if (multipart != null) {
			for (int i = 0, n = multipart.getCount(); i < n; i++) {
				Object content = multipart.getBodyPart(i);
				if (content instanceof Multipart) {
					gestisciMultiPart((MimeMultipart) content, pe);
				} else if (content instanceof MimeBodyPart) {
					gestisciBodyPart((MimeBodyPart) content, pe);
				} else {
					gestisciPart((Part) content, pe);
				}
			}
		}
	}

	public static void gestisciBodyRicRit(MimeMultipart multipart,
			MessaggioEmailEntrata pe) throws MessagingException, IOException {

		for (int i = 0, n = multipart.getCount(); i < n; i++) {
			Object content = multipart.getBodyPart(i);
			if (content instanceof Multipart) {
				gestisciBodyRicRit((MimeMultipart) content, pe);
			} else if (content instanceof MimeBodyPart) {
				gestisciBodyPart((MimeBodyPart) content, pe);
			} else {
				gestisciPart((Part) content, pe);
			}
		}
	}

	private static void gestisciBodyPart(MimeBodyPart part,
			MessaggioEmailEntrata pe) throws MessagingException, IOException {
		Object content = part.getContent();
		if (content instanceof MimeMessage) {
			if (!EmailConstants.TIPO_ACCETTAZIONE.equalsIgnoreCase(pe
					.getTipoEmail())
					&& !EmailConstants.TIPO_CONSEGNA.equalsIgnoreCase(pe
							.getTipoEmail())
					&& !EmailConstants.TIPO_MANCATA_CONSEGNA
							.equalsIgnoreCase(pe.getTipoEmail()))
				gestisciAllegatoEML(part, pe);
		} else if (content instanceof MimeMultipart) {
			gestisciMultiPart((MimeMultipart) content, pe);
		} else if (content instanceof InputStream) {
			gestisciAllegato(part, pe);
		} else if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())
				&& content instanceof String) {
			gestisciAllegatoTesto(part, pe);
		} else if (part.isMimeType("text/plain")) {
			if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
				gestisciAllegato(part, pe);
			} else if (content instanceof String) {
				gestisciBodyAsText((String) content, pe);
			} else {
				logger.warn("Parte del BodyPart di tipo non gestito:" + content);
			}
		} else if (part.isMimeType("text/html")) {
			gestisciBodyAsText((String) content, pe);
		} else if (content instanceof String) {
			gestisciBodyAsText((String) content, pe);
		}

	}

	private static void gestisciBodyAsText(String part, MessaggioEmailEntrata pe) {
		String txtPart = HtmlLocalParser.htmlToText(part);
		pe.getEmail().setTestoMessaggio(txtPart);
	}

	private static void gestisciAllegato(Part part, MessaggioEmailEntrata pe)
			throws MessagingException, IOException {
		if (part != null) {
			File tmpAtt = null;
			tmpAtt = File.createTempFile("tmp_att_", ".email",
					new File(pe.getTempFolder()));

			FileUtil.writeFile((InputStream) part.getContent(),
					tmpAtt.getAbsolutePath());
			if (part.getFileName() == null
					|| MimeUtility.decodeText(part.getFileName()).equals("")) {
				part.setFileName("no_name");
			}
			DocumentoVO doc = new DocumentoVO();
			doc.setContentType(part.getContentType());
			doc.setDescrizione(MimeUtility.decodeText(part.getFileName()));
			doc.setPath(tmpAtt.getAbsolutePath());
			doc.setSize((int) tmpAtt.length());
			doc.setFileName(MimeUtility.decodeText(part.getFileName()));
			pe.addAllegato(doc);
		}
	}
	
	
	
	private static void gestisciAllegatoTesto(Part part,
			MessaggioEmailEntrata pe) throws IOException, MessagingException {
		if (XMLUtil.isXMLFile(part) && XMLUtil.isSegnatura(part)) {
			pe.getEmail().setSegnatura((String) part.getContent());
		} else {
			if (part != null) {				
				File tmpAtt=new File(salvaFile(pe.getTempFolder(), MimeUtility.decodeText(part.getFileName()), part.getInputStream()));
				DocumentoVO doc = new DocumentoVO();
				doc.setContentType(part.getContentType());

				if (part.getFileName() == null || part.getFileName().equals("")) {
					part.setFileName("no_name");
				}
				doc.setDescrizione(MimeUtility.decodeText(part.getFileName()));
				doc.setPath(tmpAtt.getAbsolutePath());
				doc.setSize((int) tmpAtt.length());
				doc.setFileName(MimeUtility.decodeText(part.getFileName()));
				pe.addAllegato(doc);

			}
		}

	}

	private static void gestisciAllegatoEML(Part part, MessaggioEmailEntrata pe)
			throws MessagingException, IOException {
		if (part != null) {
			MimeMessage msgAllegato = (MimeMessage) part.getContent();
			Object content = msgAllegato.getContent();
			if (content instanceof Multipart) {
				gestisciMultiPart((MimeMultipart) content, pe);
			} else if (content instanceof Part) {
				gestisciPart((Part) content, pe);
			} else if (content instanceof String
					&& msgAllegato.isMimeType("text/plain")) {
				gestisciBodyAsText((String) content, pe);
			} else if (content instanceof String
					&& msgAllegato.isMimeType("text/html")) {
				gestisciBodyAsText((String) content, pe);
			} else {
				logger.warn("MimeMessage part di tipo non gestito:" + content);
			}
		}

	}

	private static String salvaFile(String tempFolder, String filename,
			InputStream input) throws IOException {
		File file = null;
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		try {
			file = File.createTempFile("tmp_", ".email", new File(tempFolder));

			logger.debug("Saving file:" + file.getAbsolutePath());
			bos = new BufferedOutputStream(new FileOutputStream(file));
			bis = new BufferedInputStream(input);
			FileUtil.writeFile(bis, bos);
		} catch (FileNotFoundException e) {
			throw new IOException(e.getMessage());
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		} finally {
			FileUtil.closeOS(bos);
			FileUtil.closeIS(bis);
		}
		return file.getAbsolutePath();
	}

	/*
	private static String salvaFile(String folder, String filename,
			String content, String mimetype, Charset charset)
			throws IOException, MessagingException {
		BufferedInputStream bis = null;
		logger.debug("Saving file:" + filename);
		if (charset != null)
			bis = new BufferedInputStream(new ByteArrayInputStream(
					content.getBytes(charset)));
		else {
			charset = guessEncoding(content.getBytes());
			//content.get
			if (charset != null){
				bis = new BufferedInputStream(new ByteArrayInputStream(content.getBytes(charset)));
			}
			else {
				if (mimetype.equals("text/html"))
					bis = new BufferedInputStream(new ByteArrayInputStream(
							content.getBytes("ISO-8859-1")));
				else if (mimetype.equals("text/plain"))
					bis = new BufferedInputStream(new ByteArrayInputStream(
							content.getBytes("US-ASCII")));
			}
		}
		return salvaFile(folder, filename, bis);
	}
	*/

	/*
	public static Charset guessEncoding(byte[] bytes) throws IOException {
		CharsetDetector charsetDetector = new CharsetDetector();
		charsetDetector.setText(bytes);
		charsetDetector.enableInputFilter(true);
		CharsetMatch cm = charsetDetector.detect();
		return selectAvaiableCharset(cm);
	}
	
	public static Charset guessEncoding(InputStream in) throws IOException {
		CharsetDetector charsetDetector = new CharsetDetector();
		charsetDetector.setText(in);
		charsetDetector.enableInputFilter(true);
		CharsetMatch cm = charsetDetector.detect();
		return selectAvaiableCharset(cm);
	}

	public static Charset selectAvaiableCharset(CharsetMatch cm) {
		for (Charset c : Charset.availableCharsets().values()) {
			if (c.name().toUpperCase().contains(cm.getName().toUpperCase())|| cm.getName().toUpperCase().contains(c.name().toUpperCase()))
				if (Charset.isSupported(c.name()))
					return c;
		}
		return null;
	}
	*/

}