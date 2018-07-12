package it.finsiel.siged.mvc.presentation.action;

import it.compit.fenice.mvc.bo.CaricaBO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.RegistroBO;
import it.finsiel.siged.mvc.bo.UfficioBO;
import it.finsiel.siged.mvc.business.RegistroEmergenzeDelegate;
import it.finsiel.siged.mvc.presentation.actionform.SelezionaRegistroUfficioForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

/**
 * Implementation of <strong>Action </strong> to create a new E-Photo Utente.
 * 
 * @author Almaviva sud.
 * 
 */

public final class SelezionaRegistroUfficioAction extends Action {

	// ----------------------------------------------------- Instance Variables

	/**
	 * The <code>Log</code> instance for this application.
	 */
	static Logger logger = Logger
			.getLogger(SelezionaRegistroUfficioAction.class.getName());

	// --------------------------------------------------------- Public Methods

	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Utente utente = (Utente) request.getSession().getAttribute(
				Constants.UTENTE_KEY);
		ActionMessages errors = new ActionMessages();
		if (form == null)
			form = new SelezionaRegistroUfficioForm();
		SelezionaRegistroUfficioForm formSel = (SelezionaRegistroUfficioForm) form;
		// l'utente ha clickato sul pulsante submit
		if (formSel.getButtonSubmit() != null) {
			errors = formSel.validate(mapping, request);
			// ci sono errori? controllo sulla validit? degli id e se l'utente
			// ha accesso a tali id
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			} else {
				utente.setCaricaInUso(formSel.getCaricaId());
				if (utente.getCaricaInUso()==0)
					utente.setCaricaInUso((CaricaBO.getUnicaCaricaAttiva(utente.getCariche())).getCaricaId());
				utente.setUfficioInUso(utente.getCaricaVOInUso().getUfficioId());
				if (utente.getUfficioInUso()==0)
					utente.setUfficioInUso((UfficioBO.getUnicoUfficio(utente.getUfficiAttivi())).getId().intValue());
				utente.setNumPrt(formSel.getNumPrt());
				return (mapping.findForward("pagina_iniziale"));
			}
		}
		else if (request.getParameter("selectUtenteResponsabileAction") != null) {
			utente.setUtenteResponsabileConnesso(true);
			utente.setCaricaInUso(0);
			//utente.setUfficioInUso(Organizzazione.getInstance().getAreaOrganizzativa(utente.getAreaOrganizzativa().getId()).getUfficioCentrale().getValueObject().getId());
			utente.setUfficioInUso(0);
			utente.setNumPrt(formSel.getNumPrt());
			 return (mapping.findForward("pagina_iniziale_avvocato_generale"));
		 }
		formSel.setRegistri(RegistroBO.getRegistriOrdinatiByFlagUfficiale(utente.getRegistriCollection()));
		formSel.setCariche(CaricaBO.getCaricheOrdinate(utente.getCaricheCollection()));
		formSel.setUtenteResponsabile(utente.getValueObject().getId()==Organizzazione.getInstance().getAreaOrganizzativa(utente.getAreaOrganizzativa().getId()).getUtenteResponsabileId());
		formSel.setNumPrtZero(utente.getNumPrt() == 0);
		formSel.setNumPrt(1);
		formSel.setMultiCarica(utente.getCariche().values().size() > 1);
		if (!formSel.isMultiCarica()){
			formSel.setCaricaId((CaricaBO.getUnicaCaricaAttiva(utente.getCariche())).getCaricaId());
			
		}
		formSel.setTabellaVisibile(formSel.isNumPrtZero() || formSel.isMultiCarica());
		
		
		request.setAttribute(mapping.getAttribute(), formSel);
		int numProtocolliRegistroEmergenza = RegistroEmergenzeDelegate.getInstance().getNumeroProtocolliPrenotati(utente.getRegistroInUso());
		request.getSession().setAttribute("PROTOCOLLI_EMERGENZA",(numProtocolliRegistroEmergenza > 0 ? new Integer(numProtocolliRegistroEmergenza) : null));
		// fine modifica Registro Emergenza
		return mapping.findForward("input");
	}
	// ------------------------------------------------------ Protected Methods

}