package it.finsiel.siged.mvc.presentation.actionform.amministrazione.firmadigitale;

import it.finsiel.siged.mvc.presentation.actionform.UploaderForm;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class AggiornaCRLForm extends UploaderForm {

    static Logger logger = Logger.getLogger(AggiornaCRLForm.class.getName());

    private DocumentoVO documentoP7M = null;

    public DocumentoVO getDocumentoP7M() {
        return documentoP7M;
    }

    public void setDocumentoP7M(DocumentoVO documentoP7M) {
        this.documentoP7M = documentoP7M;
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        // validazione in caso di import del DB da p7m o zip.p7m
        if (request.getParameter("importaCa") != null) {
            if ("".equals(getFormFileUpload().getFileName())) {
                errors.add("formFileUpload", new ActionMessage(
                        "campo.obbligatorio", "File Importa CA", ""));
            }
        }
        return errors;

    }

}