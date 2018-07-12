/*
 *
 * Copyright 2008 Flosslab s.r.l. (http://www.flosslab.it) All rights reserved.
 *
 * This file is part of e-prot 1.1 software.
 * e-prot 1.1 is a free software; 
 * you can redistribute it and/or modify it
 * under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
 * 
 * See the file License for the specific language governing permissions   
 * and limitations under the License
 * 
 * 
 * 
 * Copyright 2008 Flosslab s.r.l. (http://www.flosslab.it) All rights reserved.
 * Version: e-prot 1.1
 */

package it.flosslab.mvc.presentation.action.protocollo.helper.file;

import it.compit.fenice.enums.TipoFatturaElettronicaEnum;
import it.compit.fenice.util.XMLUtil;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.dao.digitalsignature.VerificaFirma;
import it.finsiel.siged.exception.CRLNonAggiornataException;
import it.finsiel.siged.exception.CertificatoNonValidoException;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.exception.FirmaNonValidaException;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.DocumentoDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.action.protocollo.ProtocolloAction;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.util.FileUtil;
import it.finsiel.siged.util.PdfUtil;
import it.finsiel.siged.util.ServletUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

public class ProtocolloFileUtility {

	static Logger logger = Logger.getLogger(ProtocolloAction.class.getName());

	public static void uploadDocumentoPrincipale(ProtocolloForm form,
			HttpServletRequest request, ActionMessages errors) {
		FormFile file = form.getFilePrincipaleUpload();
		String fileName = file.getFileName();
		
		if (form.getNomeFilePrincipaleUpload() != null
				&& !form.getNomeFilePrincipaleUpload().trim().equals(""))
			fileName = form.getNomeFilePrincipaleUpload();

		String contentType = file.getContentType();
		int size = file.getFileSize();
		if (size > 0 && !"".equals(fileName)) {
			String tempFilePath = FileUtil.leggiFormFilePrincipale(form,request, errors);
			String impronta = FileUtil.calcolaDigest(tempFilePath, errors);
			if (errors.isEmpty()) {
				
				try {
					VerificaFirma.verificaFileFirmato(tempFilePath, contentType);
				} catch (DataException e) {
					errors.add("documentoPrincipale", new ActionMessage(
							"database.cannot.load", e.getMessage()));
				} catch (CertificatoNonValidoException e) {
					errors.add(
							"documentoPrincipale",
							new ActionMessage(
									"errore.verificafirma.doc.non_valido", e
											.getMessage()));
				} catch (FirmaNonValidaException e) {
					errors.add(
							"documentoPrincipale",
							new ActionMessage(
									"errore.verificafirma.doc.non_valido", e
											.getMessage()));
				} catch (CRLNonAggiornataException e) {
					errors.add("documentoPrincipale", new ActionMessage(
							"errore.verificafirma.crl_non_aggiornata"));
				}
				String username = ((Utente) request.getSession().getAttribute(
						Constants.UTENTE_KEY)).getValueObject().getUsername();
				DocumentoVO documento = new DocumentoVO();
				documento.setDescrizione(null);
				documento.setFileName(fileName);
				documento.setPath(tempFilePath);
				documento.setImpronta(impronta);
				documento.setSize(size);
				documento.setContentType(contentType);
				documento.setRowCreatedTime(new Date(System.currentTimeMillis()));
				documento.setRowUpdatedTime(new Date(System.currentTimeMillis()));
				documento.setRowCreatedUser(username);
				documento.setRowUpdatedUser(username);
				form.setDocumentoPrincipale(documento);
			}
		}
	}
	
	public static boolean isDocumentoReadable(int protocolloId,String tipoProtocollo, int ufficioProtocollatore,int ufficioMittente,Collection<FascicoloVO> fascicoli,Utente utente){
		boolean readable=false;
		if(utente.getAreaOrganizzativa().isDocumentoReadable())
			readable=true;
		else if(utente.getUfficioVOInUso().getTipo().equals(UfficioVO.UFFICIO_CENTRALE)){
			readable=true;
		}else {
			if(utente.getUfficioVOInUso().getTipo().equals(UfficioVO.UFFICIO_NORMALE)){
				if(ufficioProtocollatore==utente.getUfficioInUso()|| ufficioMittente==utente.getUfficioInUso())
					readable=true;
				for(FascicoloVO f:fascicoli){
					if(f.getUfficioResponsabileId()==utente.getUfficioInUso())
						readable=true;
				}
			}else{
				Ufficio uff=Organizzazione.getInstance().getUfficio(utente.getUfficioInUso());
				if(uff.getListaUfficiDiscendentiId().contains(ufficioProtocollatore)|| uff.getListaUfficiDiscendentiId().contains(ufficioMittente))
					readable=true;
				for(FascicoloVO f:fascicoli){
					if(uff.getListaUfficiDiscendentiId().contains(f.getUfficioResponsabileId()))
						readable=true;
				}
			}
			if(!tipoProtocollo.equals("U") && !readable)
				readable=ProtocolloDelegate.getInstance().isUfficioAssegnatario(protocolloId,utente.getUfficioInUso());
		}
		return readable;
	}
	
	public boolean checkUfficio(int ufficioProtocollatore,int ufficioMittente,
			int ufficioInUso) {
		if(ufficioProtocollatore==ufficioInUso|| ufficioMittente==ufficioInUso)
			return true;
		else{
			Ufficio uff=Organizzazione.getInstance().getUfficio(ufficioProtocollatore);
			for(Ufficio u:uff.getUfficiDipendenti()){
				boolean check=checkUfficio(ufficioProtocollatore,ufficioMittente, u.getValueObject().getId());
				if(check)
					return true;
			}
		}
		return false;
	}
	
	//TODO UPLOAD FILE SCANNER
	public static void uploadDocumentoPrincipaleScanner(ProtocolloForm form, String fileName,
			HttpServletRequest request, ActionMessages errors) {
		try{
			String username = ((Utente) request.getSession().getAttribute(Constants.UTENTE_KEY)).getValueObject().getUsername();
			String descrizione = form.getNomeFilePrincipaleUpload();
			String contentType="application/pdf";
			String folder = ServletUtil.getTempUserPath(request.getSession());
			File file = File.createTempFile("temp_", ".upload", new File(folder) );
			OutputStream os = new FileOutputStream(file);
			file = PdfUtil.salvaPdfDaImagesFormFile(file, form.getImagesList());
			os.close();
			if (descrizione == null || "".equals(descrizione))
				descrizione = fileName;
			int size = (int) file.length();
			if (size > 0 && !"".equals(fileName)) {
				String impronta = FileUtil.calcolaDigest(file.getAbsolutePath(), errors);
				if (errors.isEmpty()) {
					DocumentoVO documento = new DocumentoVO();
					if (descrizione != null)
						documento.setDescrizione(descrizione);
					else
						documento.setDescrizione("-");
					documento.setFileName(fileName);
					documento.setImpronta(impronta);
					documento.setPath(file.getAbsolutePath());
					documento.setSize(size);
					documento.setContentType(contentType);
					documento.setRowCreatedTime(new Date(System.currentTimeMillis()));
					documento.setRowUpdatedTime(new Date(System.currentTimeMillis()));
					documento.setRowCreatedUser(username);
					documento.setRowUpdatedUser(username);
					form.setDocumentoPrincipale(documento);
					form.setNomeFilePrincipaleUpload("");
					form.clearImages();

				}
			}
		}catch (Exception e) {
			errors.add("allegati", new ActionMessage("File not found"));
			logger.error("uploadFileScanner fallito:", e);
			e.printStackTrace();
		} 
	}
	
	public static void uploadAllegatoScanner(ProtocolloForm form, String fileName,
			HttpServletRequest request, ActionMessages errors) {
		try {
			String username = ((Utente) request.getSession().getAttribute(Constants.UTENTE_KEY)).getValueObject().getUsername();
			String descrizione = form.getNomeFileUpload();
			String contentType="application/pdf";
			String folder = ServletUtil.getTempUserPath(request.getSession());
			File file = File.createTempFile("temp_", ".upload", new File(folder) );
			OutputStream os = new FileOutputStream(file);
			file = PdfUtil.salvaPdfDaImagesFormFile(file, form.getImagesList());
			os.close();
			if (descrizione == null || "".equals(descrizione))
				descrizione = fileName;
			int size = (int) file.length();
			if (size > 0 && !"".equals(fileName)) {
				String impronta = FileUtil.calcolaDigest(file.getAbsolutePath(), errors);
				if (errors.isEmpty()) {
					DocumentoVO documento = new DocumentoVO();
					if (descrizione != null)
						documento.setDescrizione(descrizione);
					else
						documento.setDescrizione("-");
					documento.setFileName(fileName);
					documento.setImpronta(impronta);
					documento.setPath(file.getAbsolutePath());
					documento.setSize(size);
					documento.setContentType(contentType);
					documento.setRowCreatedTime(new Date(System.currentTimeMillis()));
					documento.setRowUpdatedTime(new Date(System.currentTimeMillis()));
					documento.setRowCreatedUser(username);
					documento.setRowUpdatedUser(username);
					form.allegaDocumento(documento);
					form.setNomeFileUpload("");
					form.clearImages();
				}
			}
		} catch (Exception e) {
			errors.add("allegati", new ActionMessage("File not found"));
			logger.error("uploadFileScanner fallito:", e);
			e.printStackTrace();
		} 
	}


	
	public static void uploadAllegato(ProtocolloForm form,
			HttpServletRequest request, ActionMessages errors) {
		String username = ((Utente) request.getSession().getAttribute(
				Constants.UTENTE_KEY)).getValueObject().getUsername();
		String descrizione = form.getNomeFileUpload();
		FormFile file = form.getFormFileUpload();
		String fileName = file.getFileName();
		if (descrizione == null || "".equals(descrizione))
			descrizione = fileName;
		String contentType = file.getContentType();
		int size = file.getFileSize();
		if (size > 0 && !"".equals(fileName)) {
			String tempFilePath = FileUtil.leggiFormFile(form, request, errors);
			String impronta = FileUtil.calcolaDigest(tempFilePath, errors);
			if (errors.isEmpty()) {
				try {
					VerificaFirma.verificaFileFirmato(tempFilePath, contentType);
				} catch (DataException e) {
					errors.add("allegati", new ActionMessage(
							"database.cannot.load"));
				} catch (CertificatoNonValidoException e) {
					errors.add(
							"allegati",
							new ActionMessage(
									"errore.verificafirma.doc.non_valido", e
											.getMessage()));
				} catch (FirmaNonValidaException e) {
					errors.add(
							"allegati",
							new ActionMessage(
									"errore.verificafirma.doc.non_valido", e
											.getMessage()));
				} catch (CRLNonAggiornataException e) {
					errors.add("allegati", new ActionMessage(
							"errore.verificafirma.crl_non_aggiornata"));
				}
				DocumentoVO documento = new DocumentoVO();
				if (descrizione != null)
					documento.setDescrizione(descrizione);
				else
					documento.setDescrizione("-");
				documento.setFileName(fileName);
				documento.setImpronta(impronta);
				documento.setPath(tempFilePath);
				documento.setSize(size);
				documento.setContentType(contentType);
				documento.setRowCreatedTime(new Date(System.currentTimeMillis()));
				documento.setRowUpdatedTime(new Date(System.currentTimeMillis()));
				documento.setRowCreatedUser(username);
				documento.setRowUpdatedUser(username);
				form.allegaDocumento(documento);
				form.setNomeFileUpload("");
			}
		}
	}
	
	
	public static ActionMessages downloadFile(DocumentoVO doc,
			HttpServletResponse response) {
		InputStream is = null;
		OutputStream os = null;
		ActionMessages errors = new ActionMessages();
		try {

			if (doc != null) {
				os = response.getOutputStream();
				response.setContentType(doc.getContentType());
				response.setHeader(
						"Content-Disposition",
						"attachment;filename="
								+ doc.getFileName().replace(" ", "_"));
				response.setHeader("Cache-control", "");
				response.setHeader("target", "_blank");
				if (doc.getId() != null && !doc.isMustCreateNew()) {
					DocumentoDelegate.getInstance().writeDocumentToStream(doc.getId().intValue(), os);
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

	public static ActionMessages downloadFileProtocollo(DocumentoVO doc,
			HttpServletResponse response, int aooId) {
		InputStream is = null;
		OutputStream os = null;
		ActionMessages errors = new ActionMessages();
		try {

			if (doc != null) {
				os = response.getOutputStream();
				response.setContentType(doc.getContentType());
				response.setHeader("Content-Disposition","attachment;filename="+ doc.getFileName().replaceAll ("[ \\p{Punct}&&[^\\._]]", ""));
				response.setHeader("Cache-control", "");
				if (doc.getId() != null && !doc.isMustCreateNew()) {
					String path = Organizzazione.getInstance().getValueObject()
							.getPathDocumentiProtocollo()
							+ "/" + "aoo_" + aooId + "/" + doc.getPath();
					if (DocumentoDelegate.getInstance().isDataDocumentoNotNull(
							doc.getId().intValue())) {
						DocumentoDelegate.getInstance().writeDocumentToStream(
								doc.getId().intValue(), os);
					} else {
						is = new FileInputStream(path);
						FileUtil.writeFile(is, os);
					}
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
	
	public static ActionMessages stampaFatturaPDF(DocumentoVO doc,
			HttpServletRequest request, HttpServletResponse response, int aooId) {
		InputStream is = null;
		OutputStream os = null;
		String xsl=null;
		String tipoDoc=null;
		ActionMessages errors = new ActionMessages();
		try {
			if (doc != null) {
				os = response.getOutputStream();
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition","attachment;filename="+ (FileUtil.cambiaEstensioneFile(doc.getFileName(),"pdf")));
				response.setHeader("Cache-control", "");
				if(!doc.getFileName().toLowerCase().endsWith(".p7m")){
					String path = Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo()+ "/" + "aoo_" + aooId + "/" + doc.getPath();
					tipoDoc=FileUtil.getFatturaElettronicaType(doc.getFileName(), path);
					xsl=Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo()+TipoFatturaElettronicaEnum.valueOf(tipoDoc).getPath();
					String tmpPath = PdfUtil.creaPDFdaXML(doc.getFileName(),path, xsl, request, errors);
					is = new FileInputStream(tmpPath);
				} else{
					String path = Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo()+ "/" + "aoo_" + aooId + "/" + doc.getPath();
					String folder = ServletUtil.getTempUserPath(request.getSession());
					File tempFile = File.createTempFile("fpa_", ".tmp", new File(folder));
					path=tempFile.getAbsolutePath();
					VerificaFirma.saveContentFromP7M(path, path);
					tipoDoc=FileUtil.getFatturaElettronicaType(doc.getFileName(), path);
					xsl=Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo()+TipoFatturaElettronicaEnum.valueOf(tipoDoc).getPath();
					String tmpPath = PdfUtil.creaPDFdaXML(doc.getFileName(),path, xsl, request, errors);
					is = new FileInputStream(tmpPath);
				}
				FileUtil.writeFile(is, os);
			}
		} catch (FileNotFoundException e) {
			logger.error("", e);
			errors.add("download", new ActionMessage("error.notfound"));
		} catch (IOException e) {
			logger.error("", e);
			errors.add("download", new ActionMessage("error.cannot.read"));
		} finally {
			FileUtil.closeIS(is);
			FileUtil.closeOS(os);
		}
		return errors;
	}
	
	public static ActionMessages visualizzaFatturaElettronica(DocumentoVO doc,
			HttpServletRequest request, HttpServletResponse response, int aooId) {
		InputStream is = null;
		OutputStream os = null;
		String xsl=null;
		String tipoDoc=null;
		String path=null;
		ActionMessages errors = new ActionMessages();
		try {
			if (doc != null) {
				os = response.getOutputStream();
				response.setContentType("text/html");
				response.setHeader("Cache-control", "");
				if(!doc.getFileName().toLowerCase().endsWith(".p7m")){
					path = Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo()+ "/" + "aoo_" + aooId + "/" + doc.getPath();
					tipoDoc=FileUtil.getFatturaElettronicaType(doc.getFileName(), path);
					xsl=Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo()+TipoFatturaElettronicaEnum.valueOf(tipoDoc).getPath();
					is = XMLUtil.creaHTMLdaXML(path, xsl, errors);
				} else{
					String pathP7m = Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo()+ "/" + "aoo_" + aooId + "/" + doc.getPath();
					String folder = ServletUtil.getTempUserPath(request.getSession());
					File tempFile = File.createTempFile("fpa_", ".tmp", new File(folder));
					path=tempFile.getAbsolutePath();
					VerificaFirma.saveContentFromP7M(pathP7m, path);
					tipoDoc=FileUtil.getFatturaElettronicaType(doc.getFileName(), path);
					xsl=Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo()+TipoFatturaElettronicaEnum.valueOf(tipoDoc).getPath();
					is = XMLUtil.creaHTMLdaXML(path, xsl, errors);
				}
				FileUtil.writeFile(is, os);
			}
		} catch (FileNotFoundException e) {
			logger.error("", e);
			errors.add("download", new ActionMessage("error.notfound"));
		} catch (IOException e) {
			logger.error("", e);
			errors.add("download", new ActionMessage("error.cannot.read"));
		} catch (Exception e) {
			logger.error("", e);
			errors.add("download", new ActionMessage("EXCEPTION"));
		}
		finally {
			FileUtil.closeIS(is);
			FileUtil.closeOS(os);
		}
		return errors;
	}
	
	
	
}
