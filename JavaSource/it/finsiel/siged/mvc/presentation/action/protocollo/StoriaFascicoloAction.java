package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.finsiel.siged.mvc.business.FascicoloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.FascicoloForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.StoriaFascicoloForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StoriaFascicoloAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    static Logger logger = Logger.getLogger(StoriaFascicoloAction.class
            .getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HttpSession session = request.getSession();
        StoriaFascicoloForm storiaFascicoloForm = (StoriaFascicoloForm) form;
        if (form == null) {
            logger.info(" Creating new StoriaFascicoloAction");
            form = new StoriaFascicoloForm();
            session.setAttribute(mapping.getAttribute(), form);
        }
        FascicoloForm fascicoloForm = new FascicoloForm();
        fascicoloForm = (FascicoloForm) session.getAttribute("fascicoloForm");

        if (request.getParameter("versioneSelezionata") != null) {
            fascicoloForm.setModificabile(false);
            request.setAttribute("fascicoloId", new Integer(fascicoloForm
                    .getId()));
            request.setAttribute("versioneId", new Integer(request
                    .getParameter("versioneSelezionata")));
            return (mapping.findForward("fascicolo"));

        } else if (request.getParameter("versioneCorrente") != null) {
            fascicoloForm.setModificabile(false);
            request.setAttribute("fascicoloId", new Integer(fascicoloForm
                    .getId()));
            return (mapping.findForward("fascicolo"));
        } else if (request.getParameter("btnFascicolo") != null) {
            request.setAttribute("fascicoloId", new Integer(fascicoloForm
                    .getId()));
            return (mapping.findForward("fascicolo"));
        } else {
            storiaFascicoloForm.setProgressivo(fascicoloForm.getProgressivo());
            storiaFascicoloForm.setVersioniFascicolo(FascicoloDelegate
                    .getInstance().getStoriaFascicolo(fascicoloForm.getId()));
        }
        logger.info("Execute StoriaFascicoloAction");
        return mapping.getInputForward();

    }

}