package it.compit.fenice.util;

import it.compit.fenice.mvc.presentation.actionform.protocollo.NotificaEsitoCommittenteForm;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.util.DateUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.Part;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLUtil {

	static Logger logger = Logger.getLogger(XMLUtil.class.getName());

	public static void XMLParseFatturaElettronica(String xml,
			NotificaEsitoCommittenteForm form) {
		try {
			File fXmlFile = new File(xml);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nlFatt = doc.getElementsByTagName("DatiGeneraliDocumento");
			for (int i = 0; i < nlFatt.getLength(); i++) {
				NodeList child = nlFatt.item(i).getChildNodes();
				for (int j = 0; j < child.getLength(); j++) {
					if (child.item(j).getNodeName().equals("Numero")) {
						form.setNumeroFattura(child.item(j).getTextContent());
					}
					if (child.item(j).getNodeName().equals("Data")) {
						Date d = DateUtil.getDataYYYYMMDD(child.item(j)
								.getTextContent());
						if (d != null)
							form.setAnno(String.valueOf(DateUtil.getYear(d)));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void XMLParseSegnatura(String xml, ProtocolloVO protocollo) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();
			NodeList nodeLst = doc.getElementsByTagName("Identificatore");
			for (int s = 0; s < nodeLst.getLength(); s++) {
				Node fstNode = nodeLst.item(s);
				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
					Element fstElmnt = (Element) fstNode;
					NodeList fstNmElmntLst = fstElmnt
							.getElementsByTagName("NumeroRegistrazione");
					Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
					NodeList fstNm = fstNmElmnt.getChildNodes();
					Node npm = (Node) fstNm.item(0);
					protocollo.setNumProtocolloMittente(npm.getNodeValue());
					NodeList lstNmElmntLst = fstElmnt
							.getElementsByTagName("DataRegistrazione");
					Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
					NodeList lstNm = lstNmElmnt.getChildNodes();
					Node dpm = (Node) lstNm.item(0);
					protocollo.setDataProtocolloMittente(DateUtil.getData(dpm
							.getNodeValue()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isXMLFile(Part part) {
		boolean ret = false;
		try {
			if (part.getFileName() != null && !part.getFileName().equals("")
					&& part.getFileName().toLowerCase().endsWith(".xml")) {
				ret = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static boolean isSegnatura(Part part) throws IOException,
			MessagingException {
		boolean ret = false;
		try {
			String xml = (String) part.getContent();
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();

			factory.setFeature(
					"http://apache.org/xml/features/nonvalidating/load-dtd-grammar",
					false);
			factory.setFeature(
					"http://apache.org/xml/features/nonvalidating/load-external-dtd",
					false);

			DocumentBuilder builder = factory.newDocumentBuilder();

			Document doc = builder
					.parse(new InputSource(new StringReader(xml)));
			NodeList nodes = doc.getChildNodes();
			for (int i = 0; nodes != null && i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				String nodeName = node.getNodeName();
				if ("Segnatura".equalsIgnoreCase(nodeName)) {
					ret = true;
					break;
				}
			}
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static InputStream creaHTMLdaXML(String fileIn, String xsl,
			ActionMessages errors) {
		ByteArrayInputStream is = null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tFactory.newTransformer(new StreamSource(xsl));
			transformer.transform(new StreamSource(fileIn),
					new StreamResult(os));
			is = new ByteArrayInputStream(os.toByteArray());
		} catch (TransformerConfigurationException tce) {
			logger.error("Error create PDF from file name:" + fileIn);
			logger.error(tce.getMessage(), tce);
			errors.add("creaHTMLdaXML", new ActionMessage(
					"Error create PDF from file name:" + fileIn));
			tce.printStackTrace();
		} catch (TransformerException te) {
			logger.error("Error create PDF from file name:" + fileIn);
			logger.error(te.getMessage(), te);
			errors.add("creaHTMLdaXML", new ActionMessage(
					"Error create PDF from file name:" + fileIn));
			te.printStackTrace();
		}
		return is;
	}

	public static InputStream creaHTMLdaXMLInputStream(InputStream isIN,
			String xsl, ActionMessages errors) {
		ByteArrayInputStream is = null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tFactory.newTransformer(new StreamSource(xsl));
			transformer.transform(new StreamSource(isIN), new StreamResult(os));
			is = new ByteArrayInputStream(os.toByteArray());
		} catch (TransformerConfigurationException tce) {
			logger.error("Error create PDF from InputStream");
			logger.error(tce.getMessage(), tce);
			errors.add("creaHTMLdaXML", new ActionMessage("Error create PDF from InputStream:"));
			tce.printStackTrace();
		} catch (TransformerException te) {
			logger.error("Error create PDF from InputStream");
			logger.error(te.getMessage(), te);
			errors.add("creaHTMLdaXML", new ActionMessage("Error create PDF from InputStream"));
			te.printStackTrace();
		}
		return is;
	}
}
