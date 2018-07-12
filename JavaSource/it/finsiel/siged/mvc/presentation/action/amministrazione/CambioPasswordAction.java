package it.finsiel.siged.mvc.presentation.action.amministrazione;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.UtenteDelegate;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.CambioPasswordForm;
import it.finsiel.siged.mvc.presentation.helper.NavBarElement;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;

import java.util.ArrayList;

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

public final class CambioPasswordAction extends Action {

    static Logger logger = Logger.getLogger(CambioPasswordAction.class
            .getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        CambioPasswordForm cambioPasswordForm = (CambioPasswordForm) form;
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
        ActionMessages errors = new ActionMessages();

        ArrayList<NavBarElement> navBar = new ArrayList<NavBarElement>();
        // link = request.getContextPath() + link;
        NavBarElement elem = new NavBarElement();
        elem.setValue("Cambio password");
        elem.setTitle("Cambio password");
        elem.setLink(null);
        navBar.add(0, elem);
        session.setAttribute(Constants.NAV_BAR, navBar);

        if (utente == null) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "error.authentication.failed"));
        } else if (request.getParameter("btnConferma") != null) {
            utente.getValueObject().setPassword(
                    cambioPasswordForm.getNewPassword());
            UtenteDelegate delegate = UtenteDelegate.getInstance();
            UtenteVO utenteAggiornato = delegate.aggionaUtenteVO(utente
                    .getValueObject());
            if (utenteAggiornato.getReturnValue() == ReturnValues.SAVED) {
                Organizzazione org = Organizzazione.getInstance();
                org.addUtente(utente);
                session.setAttribute(Constants.UTENTE_KEY, utente);
            }
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "operazione_ok"));
            saveErrors(request, errors);
            cambioPasswordForm.reset(mapping, request);

        } else {
            //cambioPasswordForm.setOldPassword(utente.getValueObject().getPassword());
        	cambioPasswordForm.setOldPassword("");
            cambioPasswordForm.setUsername(utente.getValueObject()
                    .getUsername());
        }
        return (mapping.getInputForward());
    }
}