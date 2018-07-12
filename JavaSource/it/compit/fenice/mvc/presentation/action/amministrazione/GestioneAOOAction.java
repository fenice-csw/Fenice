package it.compit.fenice.mvc.presentation.action.amministrazione;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Menu;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.business.MenuDelegate;
import it.finsiel.siged.mvc.business.OrganizzazioneDelegate;
import it.flosslab.mvc.presentation.action.amministrazione.OggettarioAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class GestioneAOOAction extends Action {

	static Logger logger = Logger.getLogger(GestioneAOOAction.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		  if (request.getParameter("ricaricaOrganizzazioneAction") != null) {
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
	        }
		if (utente.getValueObject().getUsername().equals("admin"))
			return mapping.findForward("input");
		else
			return mapping.findForward("error");
	}

}
