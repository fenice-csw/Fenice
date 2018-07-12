package it.finsiel.siged.mvc.presentation.action;

import it.finsiel.siged.mvc.presentation.actionform.HelpForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Implementation of <strong>Action </strong> to create a new E-Photo Utente.
 * 
 * @author Almaviva sud.
 * 
 */

public final class HelpAction extends Action {

    static Logger logger = Logger.getLogger(HelpAction.class.getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        HttpSession session = request.getSession(true); // we create one if does

        if (form == null) {
            logger.info(" Creating new HelpForm");
            form = new HelpForm();
            session.setAttribute(mapping.getAttribute(), form);
        }

        if (mapping.getAttribute() != null) {
            session.removeAttribute(mapping.getAttribute());
        }
        logger.info("Execute HelpAction");
        return (mapping.findForward("input")); // 

    }
}