package it.compit.fenice.mvc.presentation.action.amministrazione.helper.file;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.dao.digitalsignature.VerificaFirma;
import it.finsiel.siged.exception.CRLNonAggiornataException;
import it.finsiel.siged.exception.CertificatoNonValidoException;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.exception.FirmaNonValidaException;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.DocumentoDelegate;
import it.finsiel.siged.mvc.presentation.action.amministrazione.TipoProcedimentoAction;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.TipoProcedimentoForm;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.util.FileUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

public class TipoProcedimentoFileUtility {
	
static Logger logger = Logger.getLogger(TipoProcedimentoAction.class.getName());
	
    public static void uploadFile(TipoProcedimentoForm form, HttpServletRequest request,
            ActionMessages errors) {
        String username = ((Utente) request.getSession().getAttribute(
                Constants.UTENTE_KEY)).getValueObject().getUsername();
        String descrizione = form.getNomeFileUpload();
        FormFile file = form.getFormFileUpload();
        String fileName = file.getFileName();
        if ("".equals(descrizione))
            descrizione = fileName;
        String contentType = file.getContentType();
        int size = file.getFileSize();     
        if (size > 0 && !"".equals(fileName)) {  
            String tempFilePath = FileUtil.leggiFormFile(form, request, errors);
            String impronta = FileUtil.calcolaDigest(tempFilePath, errors);
           if (errors.isEmpty()) {
                try {
                    VerificaFirma
                            .verificaFileFirmato(tempFilePath, contentType);  
                } catch (DataException e) {
                    errors.add("allegati", new ActionMessage(
                            "database.cannot.load"));
                } catch (CertificatoNonValidoException e) {
                    errors.add("allegati", new ActionMessage(
                            "errore.verificafirma.doc.non_valido", e
                                    .getMessage()));
                } catch (FirmaNonValidaException e) {
                    errors.add("allegati", new ActionMessage(
                            "errore.verificafirma.doc.non_valido", e
                                    .getMessage()));
                } catch (CRLNonAggiornataException e) {
                    errors.add("allegati", new ActionMessage(
                            "errore.verificafirma.crl_non_aggiornata"));
                }
                DocumentoVO documento = new DocumentoVO();
                documento.setDescrizione(descrizione);
                documento.setFileName(fileName);
                documento.setImpronta(impronta);
                documento.setPath(tempFilePath);
                documento.setSize(size);
                documento.setContentType(contentType);
                documento
                        .setRowCreatedTime(new Date(System.currentTimeMillis()));
                documento
                        .setRowUpdatedTime(new Date(System.currentTimeMillis()));
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
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + doc.getFileName().replaceAll ("[ \\p{Punct}&&[^\\.]]", ""));
                response.setHeader("Cache-control", "");
                if (doc.getId() != null && !doc.isMustCreateNew()) { 
                    DocumentoDelegate.getInstance().writeDocumentToStream(
                            doc.getId().intValue(), os);
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
