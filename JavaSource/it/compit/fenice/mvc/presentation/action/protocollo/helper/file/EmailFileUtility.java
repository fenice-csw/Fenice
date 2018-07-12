package it.compit.fenice.mvc.presentation.action.protocollo.helper.file;

import it.compit.fenice.enums.TipoFatturaElettronicaEnum;
import it.compit.fenice.util.XMLUtil;
import it.finsiel.siged.dao.digitalsignature.VerificaFirma;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.mvc.business.EmailDelegate;
import it.finsiel.siged.mvc.presentation.action.protocollo.ProtocolloAction;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.util.FileUtil;
import it.finsiel.siged.util.ServletUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class EmailFileUtility {
	
	static Logger logger = Logger.getLogger(ProtocolloAction.class.getName());
	
	
	//TODO -> visualizza fattura elettronica
	public static ActionMessages visualizzaFatturaElettronica(DocumentoVO doc, HttpServletRequest request, HttpServletResponse response) {
		InputStream is = null;
		InputStream isXml = null;
		String xsl=null;
		String tipoDoc=null;
		OutputStream os = null;
		String path=null;
		ActionMessages errors = new ActionMessages();
		try {
				if (doc != null) {
				os = response.getOutputStream();
				response.setContentType("text/html");
				response.setHeader("Cache-control", "");
				if(!doc.getFileName().toLowerCase().endsWith(".p7m")){
					isXml=EmailDelegate.getInstance().writeDocumentToInputStream(doc.getId().intValue());
					String folder = ServletUtil.getTempUserPath(request.getSession());
					File tempFile = File.createTempFile("fpa_", ".tmp", new File(folder));
					path=tempFile.getAbsolutePath();
					FileUtil.writeFile(isXml, path);
					tipoDoc=FileUtil.getFatturaElettronicaType(doc.getFileName(), path);
					xsl=Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo()+TipoFatturaElettronicaEnum.valueOf(tipoDoc).getPath();
					is = XMLUtil.creaHTMLdaXML(path, xsl, errors);
				}else{
					isXml=EmailDelegate.getInstance().writeDocumentToInputStream(doc.getId().intValue());
					String folder = ServletUtil.getTempUserPath(request.getSession());
					File tempFile = File.createTempFile("fpa_", ".tmp", new File(folder));
					path=tempFile.getAbsolutePath();
					VerificaFirma.saveContentFromP7MInputStream(isXml, path);
					tipoDoc=FileUtil.getFatturaElettronicaType(doc.getFileName(), path);
					xsl=Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo()+TipoFatturaElettronicaEnum.valueOf(tipoDoc).getPath();
					is = XMLUtil.creaHTMLdaXML(path, xsl, errors);
				}
				FileUtil.writeFile(is, os);
				

			}
		} catch (FileNotFoundException e) {
			logger.error("", e);
			errors.add("visualizzaFatturaElettronica", new ActionMessage("error.notfound"));
		} catch (IOException e) {
			logger.error("", e);
			errors.add("visualizzaFatturaElettronica", new ActionMessage("error.cannot.read"));
		} catch (DataException e) {
			logger.error("", e);
			errors.add("visualizzaFatturaElettronica", new ActionMessage("error.visual", "DATA EXCEPTION"));
		}catch (Exception e) {
			logger.error("", e);
			errors.add("visualizzaFatturaElettronica", new ActionMessage("error.visual", "Il visualizzatore non restituisce un file formattato correttamente. Sicuro di aver scelto il file corretto?"));
		} 
		finally {
			FileUtil.closeIS(is);
			FileUtil.closeIS(isXml);
			FileUtil.closeOS(os);
		}
		return errors;
	}
	
	
	public static ActionMessages downloadFileMail(DocumentoVO doc,
			HttpServletResponse response) {
		InputStream is = null;
		OutputStream os = null;
		ActionMessages errors = new ActionMessages();
		try {

			if (doc != null) {
				os = response.getOutputStream();
				response.setContentType(doc.getContentType());
				response.setHeader("Content-Disposition",
						"attachment;filename=" + doc.getFileName().replace(" ", "_"));
				response.setHeader("Cache-control", "");

				if (doc.getId() != null && !doc.isMustCreateNew()) {
						EmailDelegate.getInstance().writeDocumentToStream(doc.getId().intValue(), os);
						
				} else {
					is = new FileInputStream(doc.getPath());
					FileUtil.writeFile(is, os);
				}

			}
		} catch (FileNotFoundException e) {
			logger.error("", e);
			errors.add("download", new ActionMessage("error.notfound"));
		} catch (IOException e) {
			logger.error("", e);
			errors.add("download", new ActionMessage("error.cannot.read"));
		} catch (DataException e) {
			logger.error("", e);
			errors.add("download", new ActionMessage("error.cannot.read"));
		} finally {
			FileUtil.closeIS(is);
			FileUtil.closeOS(os);
		}
		return errors;
	}
	
	public static ActionMessages downloadFileMailUfficio(DocumentoVO doc,
			HttpServletResponse response) {
		InputStream is = null;
		OutputStream os = null;
		ActionMessages errors = new ActionMessages();
		try {

			if (doc != null) {
				os = response.getOutputStream();
				response.setContentType(doc.getContentType());
				response.setHeader("Content-Disposition",
						"attachment;filename=" + doc.getFileName().replace(" ", "_"));
				response.setHeader("Cache-control", "");

				if (doc.getId() != null && !doc.isMustCreateNew()) {
						EmailDelegate.getInstance().writeAllegatoUfficioToStream(doc.getId().intValue(), os);						
				} else {
					is = new FileInputStream(doc.getPath());
					FileUtil.writeFile(is, os);
				}

			}
		} catch (FileNotFoundException e) {
			logger.error("", e);
			errors.add("download", new ActionMessage("error.notfound"));
		} catch (IOException e) {
			logger.error("", e);
			errors.add("download", new ActionMessage("error.cannot.read"));
		} catch (DataException e) {
			logger.error("", e);
			errors.add("download", new ActionMessage("error.cannot.read"));
		} finally {
			FileUtil.closeIS(is);
			FileUtil.closeOS(os);
		}
		return errors;
	}
	
}
