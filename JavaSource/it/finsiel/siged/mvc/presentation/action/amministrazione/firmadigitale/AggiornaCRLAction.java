package it.finsiel.siged.mvc.presentation.action.amministrazione.firmadigitale;

import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.business.FirmaDigitaleDelegate;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.firmadigitale.AggiornaCRLForm;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.util.FileUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

/**
 * Implementation of <strong>Action </strong> to create a new E-Photo Utente.
 * 
 * @author Almaviva sud.
 * 
 */

public class AggiornaCRLAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    static Logger logger = Logger.getLogger(AggiornaCRLAction.class.getName());

    // --------------------------------------------------------- Public Methods

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     * 
     * @param mapping
     *            The ActionMapping used to select this instance
     * @param form
     *            The optional ActionForm bean for this request (if any)
     * @param request
     *            The HTTP request we are processing
     * @param response
     *            The HTTP response we are creating
     * 
     * @exception Exception
     *                if business logic throws an exception
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionMessages errors = new ActionMessages();
        AggiornaCRLForm crlForm = (AggiornaCRLForm) form;

        if (form == null) {
            crlForm = new AggiornaCRLForm();
            request.setAttribute(mapping.getAttribute(), crlForm);
        }
        if (request.getParameter("aggiorna") != null) {
            errors = FirmaDigitaleDelegate.getInstance()
                    .aggiornaListaCertificatiRevocati();
            ActionMessages msg = new ActionMessages();
            msg.add("salvato",
                    new ActionMessage("firmadigitale.crl.aggiornate"));
            saveMessages(request, msg);
        } else if (request.getParameter("importaCa") != null) {
            errors = crlForm.validate(mapping, request);
            if (errors.isEmpty()) {
                // upload del file ed estrazione in chiaro
                uploadDocumentoPrincipale(crlForm, request, errors);
                if (errors.isEmpty()) {
                    try {
                        // elimino tutti i dati presenti
                        FirmaDigitaleDelegate.getInstance().cancellaTutteCA();
                        // inserisco i nuovi
                        errors = FirmaDigitaleDelegate.getInstance()
                                .aggiornaListaCA(
                                        crlForm.getDocumentoP7M().getPath());
                    } catch (DataException e) {
                        errors.add("generale", new ActionMessage(
                                "database.cannot.save"));
                    }
                }
            }
        } else {
            
        }
        saveErrors(request, errors);

        return (mapping.findForward("input"));
    }

    private void uploadDocumentoPrincipale(AggiornaCRLForm form,
            HttpServletRequest request, ActionMessages errors) {
        FormFile file = form.getFormFileUpload();
        String fileName = file.getFileName();
        String contentType = file.getContentType();
        int size = file.getFileSize();

        // controllo: l'utente avrebbe potuto premere il pulsante senza
        // selezionare un file
        if (size > 0 && !"".equals(fileName)) {
            // salva il file in uno temporaneo e ritorna il nome dello
            // stesso
            String tempFilePath = FileUtil.leggiFormFileP7M(form, request,
                    errors);
            if (errors.isEmpty()) {
                DocumentoVO documento = new DocumentoVO();
                documento.setFileName(fileName);
                documento.setPath(tempFilePath);
                documento.setSize(size);
                documento.setContentType(contentType);
                form.setDocumentoP7M(documento);
            }
        }

    }

}
