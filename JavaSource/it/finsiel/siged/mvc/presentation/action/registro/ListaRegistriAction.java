package it.finsiel.siged.mvc.presentation.action.registro;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.RegistroDelegate;
import it.finsiel.siged.mvc.presentation.actionform.registro.RegistroForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * Implementation of <strong>Action </strong> that validates a user logon.
 * 
 * @author G. Calli
 */

public final class ListaRegistriAction extends Action {

    static Logger logger = Logger
            .getLogger(ListaRegistriAction.class.getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionMessages errors = new ActionMessages();
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
        RegistroForm registroForm = (RegistroForm) form;

        if (form == null) {
            registroForm = new RegistroForm();
            request.setAttribute(mapping.getAttribute(), registroForm);
        }

        registroForm.setRegistri(RegistroDelegate.getInstance()
                .getRegistriByAooId(utente.getRegistroVOInUso().getAooId()));

        if (request.getParameter("btnModifica") != null
                && request.getParameter("id") == null) {
            errors.add("registro", new ActionMessage("selezione.obbligatoria",
                    "il registro", ""));

        } else if (request.getParameter("btnNuovo") != null
                || request.getParameter("btnModifica") != null) {
            return (mapping.findForward("registro"));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }

        return (mapping.findForward("input"));
    }
}