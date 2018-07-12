package it.finsiel.siged.mvc.presentation.action.amministrazione.org;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Menu;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.business.MenuDelegate;
import it.finsiel.siged.mvc.business.OrganizzazioneDelegate;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.org.utenti.ProfiloUtenteForm;

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

public class RicaricaOrganizzazioneAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    static Logger logger = Logger.getLogger(RicaricaOrganizzazioneAction.class
            .getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        HttpSession session = request.getSession();

        if (form == null) {
            form = new ProfiloUtenteForm();
            request.setAttribute(mapping.getAttribute(), form);
        }

        if (request.getParameter("btnRicaricaOrganizzazione") != null) {
            OrganizzazioneDelegate.getInstance().loadOrganizzazione();
            servlet.getServletContext()
                    .setAttribute(Constants.ORGANIZZAZIONE_ROOT,
                            Organizzazione.getInstance());
            Menu rm = MenuDelegate.getInstance().getRootMenu();
            servlet.getServletContext().setAttribute(Constants.MENU_ROOT, rm);
            OrganizzazioneDelegate.getInstance().caricaServiziSchedulati(session.getServletContext());

            LookupDelegate ld = LookupDelegate.getInstance();
            servlet.getServletContext().setAttribute(
                    LookupDelegate.getIdentifier(), ld);
            ld.caricaTabelle(servlet.getServletConfig().getServletContext());
            session.invalidate();
            return (mapping.findForward("logon"));
        } else {
            return (mapping.findForward("input"));
        }

    }

}