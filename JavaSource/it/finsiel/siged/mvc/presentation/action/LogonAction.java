package it.finsiel.siged.mvc.presentation.action;

import it.compit.fenice.mvc.bo.CaricaBO;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.AuthenticationException;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.OrganizzazioneBO;
import it.finsiel.siged.mvc.bo.RegistroBO;
import it.finsiel.siged.mvc.bo.UfficioBO;
import it.finsiel.siged.mvc.business.OrganizzazioneDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.business.RegistroDelegate;
import it.finsiel.siged.mvc.business.RegistroEmergenzeDelegate;
import it.finsiel.siged.mvc.business.UtenteDelegate;
import it.finsiel.siged.mvc.presentation.actionform.LogonForm;
import it.finsiel.siged.mvc.vo.lookup.ParametriLdapVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;
import it.finsiel.siged.servlet.SessionTimeoutNotifier;
import it.finsiel.siged.util.ServletUtil;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
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
 * @author Almaviva sud.
 */

public final class LogonAction extends Action {

	// ----------------------------------------------------- Instance Variables

	/**
	 * The <code>Log</code> instance for this application.
	 */
	static Logger logger = Logger.getLogger(LogonAction.class.getName());

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

		ServletContext context = getServlet().getServletContext();
		LogonForm lForm = (LogonForm) form;
		HttpSession session = request.getSession();
		Utente utente = null;
		Organizzazione organizzazione = Organizzazione.getInstance();
		if ("/logoff".equals(mapping.getPath())) {
			utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
			disconnectUtente(utente);
			logger.info("Invalidate Session ID:" + session.getId());
			session.invalidate();
			session = request.getSession(true);// errore ???
			return (mapping.findForward("logon"));
		}

		if ("".equals(lForm.getLogin())) {
			return (mapping.findForward("input"));
		}

		// Validate the request parameters specified by the user
		ActionMessages errors = new ActionMessages();
		// la validazione ci assicura che username e password non sono vuoti
		String username = lForm.getUsername();
		String password = lForm.getPassword();

		utente = organizzazione.getUtente(username);
		// int numeroUltimoProtocollo = ;

		if (utente == null) {
			// utente
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"error.authentication.failed"));
		} else if (utente.getSessionId() != null) {
			// l'utente ha gia' un'altra sessione
			if (Boolean.TRUE.equals(lForm.getForzatura())) {
				// se ha gia' deciso di forzare, forziamo il
				// login
				if ("".equals(password) || "".equals(username)) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage(
									"error.authentication.failed.passins"));
					if (!errors.isEmpty()) {
						request.setAttribute("mostra_forzatura", Boolean.TRUE);
						saveErrors(request, errors);
						return (mapping.findForward("input"));
					}
				} else if (!password.equals(utente.getValueObject()
						.getPassword())
						|| !username.equals(utente.getValueObject()
								.getUsername())) {

					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("error.authentication.failed"));
					if (!errors.isEmpty()) {
						request.setAttribute("mostra_forzatura", Boolean.TRUE);
						saveErrors(request, errors);
						return (mapping.findForward("input"));
					}

				} else {

					try {
						// rimuovo la sessioneId precedente dal context...
						Organizzazione.getInstance().removeSessionIdUtente(
								utente.getSessionId());
						// distruggo la sessione dal servlet container
						session.invalidate();
						// chiedo al container una nuova sessione
						session = request.getSession(true);
						logger.info("Session just created:" + session.getId());
						connectUtente(session, context, utente);
					} catch (DataException de) {
						logger.error("connectUtente", de);
						errors.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage(
										"error.authentication.connect"));
					}
				}
			} else {
				// altrimenti si propone la forzatura
				request.setAttribute("mostra_forzatura", Boolean.TRUE);

				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"error.authentication.already_connected"));
			}
		} else {
			// primo accesso da parte dell'utente, autentichiamo.
			logger.info("Authenticating user:" + username);
			UtenteDelegate utenteDelegate = UtenteDelegate.getInstance();
			UtenteVO uteVO = null;
			if (utenteDelegate == null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"error.delegate.missing"));
			} else {
				// LDAP?
				boolean useLdap = organizzazione.getValueObject().getFlagLdap()
						.equals("1");
				ParametriLdapVO ldapSettings = organizzazione.getValueObject()
						.getParametriLdap();
				if (useLdap) {
					try {
						uteVO = utenteDelegate.getUtente(username, password,
								ldapSettings);
					} catch (AuthenticationException e) {
						errors
								.add(ActionMessages.GLOBAL_MESSAGE,
										new ActionMessage(
												"error.authentication.failed"));
					}
				} else {
					uteVO = utenteDelegate.getUtente(username, password);
				}

				if (uteVO == null
						|| uteVO.getReturnValue() != ReturnValues.FOUND) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("error.authentication.failed"));
				} else if (!uteVO.isAbilitato()) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage(
									"error.authentication.non_abilitato"));
				} else {
					try {
						session = request.getSession(true);
						connectUtente(session, context, utente);
					} catch (DataException de) {
						logger.error("connectUtente", de);
						errors.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage(
										"error.authentication.connect"));
					}
				}
			}
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("input"));
		}

		if (mapping.getAttribute() != null) {
			if ("request".equals(mapping.getScope()))
				request.removeAttribute(mapping.getAttribute());
			else
				request.getSession().removeAttribute(mapping.getAttribute());
		}
		if (utente.getValueObject().getUsername().equals("admin")) {
			return (mapping.findForward("gestisci_aoo"));
		}
		if (utente == null || utente.getRegistri().size() == 0
				|| utente.getCariche().size() == 0) {
			errors.add("registro", new ActionMessage(
					"error.authentication.non_abilitato"));
			saveErrors(request, errors);
			disconnectUtente(utente);
			return (mapping.findForward("input"));

		}
		else if (utente.getCariche().size() > 1 || getNumUltimoPrt(utente) == 0) {
			utente.setRegistroInUso(RegistroBO.getRegistroUfficialeId(utente
					.getRegistri().values()));
			if(Organizzazione.getInstance().getValueObject().getFlagRegistroPostaSeparato().equals("0"))
				utente.setRegistroPostaInterna(RegistroBO.getRegistroUfficialeId(utente.getRegistri().values()));
			else
				utente.setRegistroPostaInterna(RegistroBO.getRegistroPostaInternaId(utente.getRegistri().values()));
			
			utente.setNumPrt(getNumUltimoPrt(utente));
			return (mapping.findForward("scelta_registro"));

		} else {
			utente.setRegistroInUso(RegistroBO.getRegistroUfficialeId(utente
					.getRegistri().values()));
			
			if(Organizzazione.getInstance().getValueObject().getFlagRegistroPostaSeparato().equals("0"))
				utente.setRegistroPostaInterna(RegistroBO.getRegistroUfficialeId(utente.getRegistri().values()));
			else
				utente.setRegistroPostaInterna(RegistroBO.getRegistroPostaInternaId(utente.getRegistri().values()));

			utente.setUfficioInUso((UfficioBO.getUnicoUfficio(utente
					.getUfficiAttivi())).getId().intValue());
			utente.setCaricaInUso((CaricaBO.getUnicaCaricaAttiva(utente
					.getCariche())).getCaricaId());
			utente.setNumPrt(0);
			int numProtocolliRegistroEmergenza = RegistroEmergenzeDelegate
					.getInstance().getNumeroProtocolliPrenotati(
							utente.getRegistroInUso());
			request.getSession().setAttribute(
					"PROTOCOLLI_EMERGENZA",
					(numProtocolliRegistroEmergenza > 0 ? new Integer(
							numProtocolliRegistroEmergenza) : null));
			return (mapping.findForward("pagina_predefinita"));
		}
	}

	// ------------------------------------------------------ Private Methods

	private int getNumUltimoPrt(Utente utente) {
		Calendar c = Calendar.getInstance();
		int anno = c.get(Calendar.YEAR);
		int registroId = RegistroBO.getRegistroUfficialeId(utente.getRegistri()
				.values());
		return ProtocolloDelegate.getInstance().getUltimoProtocollo(anno,
				registroId);
	}

	private void connectUtente(HttpSession session, ServletContext context,
			Utente utente) throws ServletException, IOException, DataException {
		utente.setSessionId(session.getId());
		RegistroDelegate registroDelegate = RegistroDelegate.getInstance();
		OrganizzazioneDelegate organizzazioneDelegate = OrganizzazioneDelegate
				.getInstance();
		UtenteVO utenteVO = utente.getValueObject();
		Map<Integer,RegistroVO> registri = registroDelegate.getRegistriUtente(utenteVO.getId()
				.intValue());
		utente.setRegistri(registri);
		utente.setRegistroUfficialeId(RegistroBO
				.getRegistroUfficialeId(registri.values()));
		Organizzazione organizzazione = Organizzazione.getInstance();
		//
		HashMap<Integer,UfficioVO> uffici = OrganizzazioneBO
				.getUfficiUtente(organizzazioneDelegate
						.getIdentificativiUffici(utente.getValueObject()
								.getId().intValue()));
		utente.setUffici(uffici);
		//
		HashMap<Integer,CaricaVO> cariche = OrganizzazioneBO.getCaricheUtente(
				organizzazioneDelegate.getIdentificativiCariche(utente
						.getValueObject().getId().intValue()), uffici);
		// if()
		utente.setCariche(cariche);
		//
		initializeUserSession(session, utente);
		organizzazione.aggiungiUtenteConnesso(utente);
		session.setAttribute(Constants.UTENTE_KEY, utente);

	}

	/*
	 * Create a session for this request, used to save temp file in unique way
	 * on the server, avoiding the possibility of multiple thread to overwrite
	 * the same file exception if cannot be creaated.
	 */

	public void initializeUserSession(HttpSession session, Utente utente)
			throws ServletException, IOException {
		SessionTimeoutNotifier stn = new SessionTimeoutNotifier(ServletUtil
				.getContextPath(session)
				+ session.getServletContext().getInitParameter(
						Constants.TEMP_FILE_PATH), utente.getValueObject()
				.getUsername(), session);
		utente.getValueObject().setTempFolder(stn.getTempPath());
		session.setAttribute(Constants.SESSION_NOTIFIER, stn);
	}

	/*
	 * Spostare questo metodo nel LogoutAction. Remove any temp file from the
	 * server cache for that request/session
	 */
	public void disconnectUtente(Utente utente) {
		if (utente != null) {
			Organizzazione organizzazione = Organizzazione.getInstance();
			organizzazione.disconnettiUtente(utente);
		}
	}
}