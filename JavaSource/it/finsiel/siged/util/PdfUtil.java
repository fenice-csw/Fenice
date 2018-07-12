package it.finsiel.siged.util;

import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.dao.digitalsignature.VerificaFirma;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.MessaggioEmailEntrata;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public final class PdfUtil {

	static Logger logger = Logger.getLogger(PdfUtil.class.getName());

	public static File creaPDFTimbrato(InputStream isPDF, String testo,
			int aooId, HttpServletRequest request) {
		File fileTimbrato=null;
		try {
			String folder = ServletUtil.getTempUserPath(request.getSession());
			File tempFile = File.createTempFile("tmp_", ".tmp", new File(folder));
			VerificaFirma.saveContentFromP7MInputStream(isPDF,tempFile.getAbsolutePath());
			fileTimbrato = PdfUtil.scriviTimbro(testo,tempFile.getAbsolutePath());	
		} catch (FileNotFoundException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		} catch (DataException e) {
			logger.error("", e);
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			FileUtil.closeIS(isPDF);
		}
		return fileTimbrato;
	}

	public static String creaPDFdaXML(String fileOut, String fileIn,
			String xsl, HttpServletRequest request, ActionMessages errors) {
		ByteArrayInputStream is = null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		String pathFileTemporaneo = null;
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tFactory.newTransformer(new StreamSource(xsl));
			transformer.transform(new StreamSource(fileIn),
					new StreamResult(os));
			is = new ByteArrayInputStream(os.toByteArray());
			pathFileTemporaneo = creaPDFdaHtml(fileOut + ".pdf", is, request,
					errors);
		} catch (TransformerConfigurationException tce) {
			logger.debug("Error create PDF from file name:" + fileIn);
			logger.error(tce.getMessage(), tce);
			errors.add("creaPDFdaXML", new ActionMessage(
					"Error create PDF from file name:" + fileIn));
			tce.printStackTrace();
		} catch (TransformerException te) {
			logger.debug("Error create PDF from file name:" + fileIn);
			logger.error(te.getMessage(), te);
			errors.add("creaPDFdaXML", new ActionMessage(
					"Error create PDF from file name:" + fileIn));
			te.printStackTrace();
		}
		return pathFileTemporaneo;
	}
	


	public static String creaPDFdaHtml(String fileName, InputStream is,
			HttpServletRequest request, ActionMessages errors) {
		String pathFileTemporaneo = null;
		String folder = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(false);
			dbf.setFeature("http://xml.org/sax/features/namespaces", false);
			dbf.setFeature("http://xml.org/sax/features/validation", false);
			dbf.setFeature(
					"http://apache.org/xml/features/nonvalidating/load-dtd-grammar",
					false);
			dbf.setFeature(
					"http://apache.org/xml/features/nonvalidating/load-external-dtd",
					false);

			folder = ServletUtil.getTempUserPath(request.getSession());
			File tempFile = File.createTempFile("temp_", ".upload", new File(
					folder));
			OutputStream bos = new BufferedOutputStream(new FileOutputStream(
					tempFile), FileConstants.BUFFER_SIZE);

			StringWriter writer = new StringWriter();

			Tidy tidy = new Tidy();
			tidy.setXHTML(true);
			tidy.setShowErrors(0);
			tidy.setInputEncoding("utf8");
			tidy.setOutputEncoding("utf8");
			tidy.setQuiet(false);
			tidy.setDropFontTags(true);
			tidy.setIndentContent(false);
			tidy.setIndentAttributes(false);
			tidy.setShowWarnings(false);
			tidy.parse(is, writer);
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(writer.toString());
			renderer.layout();
			renderer.createPDF(bos);
			renderer.finishPDF();
			pathFileTemporaneo = tempFile.getAbsolutePath();
			is.close();
			writer.close();

		} catch (FileNotFoundException fnfe) {
			logger.debug("Error uploading file to:" + folder + " - file name:"
					+ fileName);
			logger.error(fnfe.getMessage(), fnfe);
			errors.add("filePrincipaleUpload", new ActionMessage(
					"upload.error.filenotfound"));
		} catch (IOException ioe) {
			logger.debug("Error uploading file to:" + folder + " - file name:"
					+ fileName);
			logger.error(ioe.getMessage(), ioe);
			errors.add("filePrincipaleUpload", new ActionMessage(
					"upload.error.io"));
		} catch (Exception ioe) {
			logger.debug("Error uploading file to:" + folder + " - file name:"
					+ fileName);
			logger.error(ioe.getMessage(), ioe);
			errors.add("filePrincipaleUpload", new ActionMessage(
					"upload.error.io"));
		}
		return pathFileTemporaneo;
	}

	public static File salvaPdfDaImagesFormFile(File fileOut, Collection<FormFile> imagesList) {
	    try {
	    	int indentation = 0;

			  Document document = new Document(PageSize.A4, 0f, 0f, 0f, 0f);//new Document();
		      FileOutputStream fos = new FileOutputStream(fileOut);
		      PdfWriter writer = PdfWriter.getInstance(document, fos);
		      writer.open();
		      document.open();
		      for(FormFile ff:imagesList){
		    	  Image img=Image.getInstance(IOUtils.toByteArray(ff.getInputStream()));
		    	  float scaler = ((document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin() - indentation) / img.getWidth()) * 100;
		    	  img.scalePercent(scaler);
		    	  document.add(img);
		      }
		      document.close();
		      writer.close();
	    }catch (Exception e) {
	      e.printStackTrace();
	    }
		return fileOut;
	}
	
	public static File salvaPdfDaMail(File file, MessaggioEmailEntrata email) {
		try {
			FileOutputStream fileout = new FileOutputStream(file);
			Document document = new Document();
			PdfWriter.getInstance(document, fileout);
			document.addAuthor("Fenice");
			document.addTitle(email.getTipoEmail());
			document.open();
			Paragraph paragraph = new Paragraph();
			paragraph.add(email.getEmail().getTestoMessaggio());
			paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(paragraph);
			document.close();
		} catch (Exception e) {
			logger.debug("", e);
			logger.error("Impossibile creare il PDF: ", e);
		}
		return file;
	}

	public static void manipulatePdf(String src, String dest, String testo)
			throws IOException, DocumentException {
		try {
			PdfReader reader = new PdfReader(src);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
					dest));
			PdfContentByte cb = stamper.getOverContent(1);
			PdfTemplate xobject = cb
					.createTemplate(PageSize.A4.getHeight(), 30);
			ColumnText column = new ColumnText(xobject);
			column.setSimpleColumn(0, 0, PageSize.A4.getHeight(), 30);
			column.addElement(new Paragraph(testo.toUpperCase()));
			column.go();
			cb.addTemplate(xobject, 0, -1f, 1f, 0, PageSize.A4.getWidth() - 23,
					PageSize.A4.getHeight() - 30);
			stamper.close();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static File scriviTimbro(String testo, String inPdf)
			throws Exception {
		File tempFile = null;
		File inFile = new File(inPdf);
		File tempFolder = inFile.getParentFile();
		tempFile = File.createTempFile("timbro_", ".tmp", tempFolder);
		manipulatePdf(inPdf, tempFile.getAbsolutePath(), testo);
		return tempFile;
	}

}
