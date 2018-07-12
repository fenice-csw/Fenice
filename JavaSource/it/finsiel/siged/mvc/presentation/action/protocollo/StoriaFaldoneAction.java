package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.finsiel.siged.mvc.business.FaldoneDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.FaldoneForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.StoriaFaldoneForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

public class StoriaFaldoneAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    static Logger logger = Logger.getLogger(StoriaFascicoloAction.class
            .getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionMessages errors = new ActionMessages();// Report any errors we

        HttpSession session = request.getSession();

        StoriaFaldoneForm storiaFaldoneForm = (StoriaFaldoneForm) form;

        if (form == null) {
            logger.info(" Creating new StoriaFaldoneAction");
            form = new StoriaFaldoneForm();
            session.setAttribute(mapping.getAttribute(), form);
        }
        FaldoneForm faldoneForm = new FaldoneForm();
        faldoneForm = (FaldoneForm) session.getAttribute("faldoneForm");
        if (request.getParameter("versioneSelezionata") != null) {
            faldoneForm.setModificabile(false);
            request.setAttribute("faldoneId", new Integer(faldoneForm
                    .getFaldoneId()));
            request.setAttribute("versioneId", new Integer(request
                    .getParameter("versioneSelezionata")));
            return (mapping.findForward("faldone"));

        } else if (request.getParameter("versioneCorrente") != null) {
            faldoneForm.setModificabile(true);
            request.setAttribute("faldoneId", new Integer(faldoneForm
                    .getFaldoneId()));
            return (mapping.findForward("faldone"));
        } else if (request.getParameter("btnFaldone") != null) {
            request.setAttribute("faldoneId", new Integer(faldoneForm
                    .getFaldoneId()));
            return (mapping.findForward("faldone"));
        } else {
            storiaFaldoneForm.setVersioniFaldone(FaldoneDelegate.getInstance()
                    .getStoriaFaldone(faldoneForm.getFaldoneId()));
        }

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        logger.info("Execute StoriaFaldoneAction");

        return mapping.getInputForward();

    }

}