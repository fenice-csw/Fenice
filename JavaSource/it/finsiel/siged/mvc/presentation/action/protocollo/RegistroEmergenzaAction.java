package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.business.RegistroEmergenzeDelegate;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.ScartoProtocolliForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.RegistroEmergenzaForm;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.presentation.helper.SessioniEmergenzaView;

import java.util.ArrayList;
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

public class RegistroEmergenzaAction extends Action {

	// ----------------------------------------------------- Instance Variables

	/**
	 * The <code>Log</code> instance for this application.
	 */
	static Logger logger = Logger.getLogger(RegistroEmergenzaAction.class
			.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();// Report any errors we

		HttpSession session = request.getSession();

		RegistroEmergenzaForm emergenzaForm = (RegistroEmergenzaForm) form;

		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);

		//String tipoProtocollo = null;
		
		if (form == null) {
			logger.info(" Creating new RegistroEmergenzaAction");
			form = new ScartoProtocolliForm();
			session.setAttribute(mapping.getAttribute(), form);
		}

		if (request.getParameter("protocolloSelezionato") == null) {
			aggiungiSessioni(emergenzaForm, utente);

		}
		if (request.getParameter("ingressoAction") != null) {
			emergenzaForm.setInserisciIngresso(true);

		}
		if (request.getParameter("uscitaAction") != null) {
			emergenzaForm.setInserisciIngresso(false);
		}

		if (request.getParameter("salvaEmergenzaAction") != null) {
			int returnValues;
			errors = emergenzaForm.validateDatiInserimento(mapping, request);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return (mapping.findForward("input"));

			} else {
				int protIng = emergenzaForm.getNumeroProtocolliIngresso();
				ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
				returnValues = delegate.registraProtocolliEmergenza(protIng,
						utente);

				// inserire il metodo delegato
			}

			// operazione andata a buon fine
			ActionMessages msg = new ActionMessages();
			if (returnValues == ReturnValues.SAVED) {
				msg.add("operazioneEffettuata", new ActionMessage(
						"msg.operazione.effettuata"));
				saveMessages(request, msg);
				RegistroEmergenzeDelegate protocolliDelegate = RegistroEmergenzeDelegate
						.getInstance();
				aggiungiSessioni(emergenzaForm, utente);

				Collection<ReportProtocolloView>  p = protocolliDelegate.getProtocolliPrenotati(utente
						.getRegistroVOInUso().getId().intValue());
				/*
				Date tmp = null;
				for (SessioniEmergenzaView sSel : sessioniAperte) {
					if (sessioniAperte.size() == 1)
						emergenzaForm.setSessioneSelezionata(sSel);
					if (tmp == null)
						tmp = DateUtil.getDataOra(sSel.getDataProtocollo());
					else if (DateUtil.getDataOra(sSel.getDataProtocollo())
							.after(tmp))
						emergenzaForm.setSessioneSelezionata(sSel);
				}
				//
				 
				 */
				request.getSession().setAttribute("PROTOCOLLI_EMERGENZA",
						new Integer(p.size()));
			} else {
				errors.add("errore_nel_salvataggio", new ActionMessage(
						"errore_nel_salvataggio"));
				saveErrors(request, errors);
			}
			emergenzaForm.reset(mapping, request);
			//return (mapping.findForward("lista"));
			return (mapping.findForward("input"));
		} else if (request.getParameter("protocolloSelezionato") != null) {
			request.setAttribute("protocolloId", new Integer(request
					.getParameter("protocolloSelezionato")));
			session.setAttribute("daEmergenza", request
					.getParameter("intervallo"));
			/*
			if ("I".equals(tipoProtocollo)) {
				return (mapping.findForward("visualizzaProtocolloIngresso"));
			} else {
				return (mapping.findForward("visualizzaProtocolloUscita"));
			}
			 */
			return (mapping.findForward("visualizzaProtocollo"));
		} else if (request.getParameter("listaProtocolliPrenotati") != null) {
			RegistroEmergenzeDelegate protocolliDelegate = RegistroEmergenzeDelegate
					.getInstance();
			Collection<ReportProtocolloView>  p = new ArrayList<ReportProtocolloView> ();
			p = protocolliDelegate.getProtocolliPrenotati(utente
					.getRegistroVOInUso().getId().intValue());
			if (p != null && p.size() > 0) {
				emergenzaForm.setProtocolliPrenotati(p);
			} else {
				request.getSession().setAttribute("PROTOCOLLI_EMERGENZA", null);
				errors.add("nessun_dato", new ActionMessage("nessun_dato"));
				saveErrors(request, errors);
			}
			return (mapping.findForward("lista"));
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		logger.info("Execute RegistroEmergenzaAction");

		return (mapping.findForward("input"));
	}

	private void aggiungiSessioni(RegistroEmergenzaForm emergenzaForm,
			Utente utente) {
		RegistroEmergenzeDelegate pDelegate = RegistroEmergenzeDelegate
				.getInstance();
		Collection<SessioniEmergenzaView>  pInit = new ArrayList<SessioniEmergenzaView> ();
		try {
			pInit = pDelegate.getSessioniAperte(utente.getRegistroVOInUso()
					.getId().intValue());
		} catch (DataException e) {
			e.printStackTrace();
		}
		if (pInit != null && pInit.size() > 0)
			emergenzaForm.setSessioniAperte(pInit);
	}
}