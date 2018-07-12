package it.finsiel.siged.mvc.presentation.action.amministrazione.org.utenti;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.AmministrazioneDelegate;
import it.finsiel.siged.mvc.business.UtenteDelegate;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.org.utenti.CercaUtentiForm;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.util.NumberUtil;

import java.util.Collection;

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
 * Implementation of <strong>Action </strong> to create a new E-Photo Utente.
 * 
 * @author Almaviva sud.
 * 
 */

public class CercaUtentiAction extends Action {

	// ----------------------------------------------------- Instance Variables

	/**
	 * The <code>Log</code> instance for this application.
	 */
	static Logger logger = Logger.getLogger(CercaUtentiAction.class.getName());

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
		ActionMessages messages = new ActionMessages();
		HttpSession session = request.getSession();
		CercaUtentiForm cercaForm = (CercaUtentiForm) form;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		if (cercaForm.getBtnCerca() != null) {
			errors = cercaForm.validate(mapping, request);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return mapping.findForward("input");
			}
			String cognome = cercaForm.getCognome();
			String nome = cercaForm.getNome();
			String username = cercaForm.getUsername();
			if ("".equals(cognome))
				cognome = null;
			if ("".equals(nome))
				nome = null;
			if ("".equals(username))
				username = null;

			Collection<UtenteVO> risultato = UtenteDelegate.getInstance()
					.cercaUtenti(utente.getValueObject().getAooId(), username,
							cognome, nome);
			cercaForm.setRisultatoRicerca(risultato);
			request.setAttribute(mapping.getAttribute(), cercaForm);

		} else if (request.getParameter("btnNuovo") != null) {
			AmministrazioneDelegate delegate = AmministrazioneDelegate
					.getInstance();
			if (delegate.getProfili(utente.getUfficioVOInUso().getAooId())
					.size() == 0) {
				errors.add("nuovoUtente", new ActionMessage(
						"tabella.profili.vuota", "", ""));
				saveErrors(request, errors);

			} else {
				return mapping.findForward("nuovo");
			}
		} else if (request.getParameter("btnAnnulla") != null) {
			cercaForm.setCognome(null);
			cercaForm.setNome(null);
			cercaForm.setUsername(null);
		} else if (request.getParameter("eliminaAction") != null) {
			int id = NumberUtil.getInt(request.getParameter("eliminaAction"));
			UtenteDelegate delegate = UtenteDelegate.getInstance();
			UtenteVO utenteVO = delegate.getUtente(id);
			if (utenteVO != null
					&& utenteVO.getReturnValue() == ReturnValues.FOUND) {
				if (delegate.isUtenteCancellabile(id)) {
					if (delegate.cancellaUtente(id)) {
						messages.add("cancellazione_ok", new ActionMessage(
								"cancellazione_ok"));
						saveMessages(request, errors);
					} else {
						errors.add("operazione_fallita", new ActionMessage(
								"operazione_fallita"));
						saveErrors(request, errors);
					}
					// profiloForm.inizializzaForm();
				} else {
					errors.add("utente_non_cancellabile", new ActionMessage(
							"utente_non_cancellabile"));
					logger.warn("Utente non cancellabile. id=" + id);
					saveErrors(request, errors);
				}

			} else {
				errors.add("utente_non_trovato", new ActionMessage(
						"utente_non_trovato"));
				logger.warn("Utente non trovato. id=" + id);
				saveErrors(request, errors);
			}
			// return mapping.findForward("input");

		}
		// if(errors.isEmpty())
		return (mapping.findForward("input"));

	}

}
