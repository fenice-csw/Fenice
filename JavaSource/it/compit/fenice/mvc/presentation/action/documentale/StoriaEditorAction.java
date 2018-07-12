package it.compit.fenice.mvc.presentation.action.documentale;

import it.compit.fenice.mvc.business.EditorDelegate;
import it.compit.fenice.mvc.presentation.actionform.documentale.EditorForm;
import it.compit.fenice.mvc.presentation.actionform.documentale.StoriaEditorForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.StoriaFascicoloForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StoriaEditorAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    static Logger logger = Logger.getLogger(StoriaEditorAction.class
            .getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HttpSession session = request.getSession();
        StoriaEditorForm sForm = (StoriaEditorForm) form;
        if (form == null) {
            logger.info(" Creating new StoriaEditorAction");
            form = new StoriaFascicoloForm();
            session.setAttribute(mapping.getAttribute(), form);
        }
        EditorForm editorForm = new EditorForm();
        editorForm = (EditorForm) session.getAttribute("editorForm");
        sForm.setVersioniEditor(EditorDelegate
                .getInstance().getStoriaDocumento(editorForm.getDocumentoId()));
        logger.info("Execute StoriaEditorAction");

        return mapping.getInputForward();

    }

}