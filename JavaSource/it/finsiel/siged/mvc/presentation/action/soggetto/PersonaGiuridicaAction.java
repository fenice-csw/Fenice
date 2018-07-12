package it.finsiel.siged.mvc.presentation.action.soggetto;

import it.compit.fenice.mvc.presentation.actionform.documentale.EditorForm;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.SoggettoDelegate;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.TipoProcedimentoForm;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.TitolarioForm;
import it.finsiel.siged.mvc.presentation.actionform.documentale.DocumentoForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.FascicoloForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloIngressoForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloUscitaForm;
import it.finsiel.siged.mvc.presentation.actionform.soggetto.PersonaGiuridicaForm;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;

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

/**
 * Implementation of <strong>Action </strong> to create a new E-Photo User.
 * 
 * @author
 * 
 */

public class PersonaGiuridicaAction extends Action {

	static Logger logger = Logger.getLogger(PersonaGiuridicaAction.class
			.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors = new ActionMessages();// Report any errors we
		ActionMessages messages = new ActionMessages();
		SoggettoDelegate delegate = SoggettoDelegate.getInstance();
		HttpSession session = request.getSession(true); // we create one if does
		SoggettoVO personaGiuridica = new SoggettoVO('G');
		PersonaGiuridicaForm personaGiuridicaForm = (PersonaGiuridicaForm) form;
		boolean indietroVisibile = false;
		personaGiuridicaForm.setIndietroVisibile(indietroVisibile);

		if (form == null) {
			logger.info(" Creating new Persona Giuridica Form");
			form = new PersonaGiuridicaForm();
			session.setAttribute(mapping.getAttribute(), form);
		}
		if (Boolean.TRUE.equals(session.getAttribute("tornaEmail"))) {
			setNuovoSoggetto(personaGiuridicaForm, request);
			personaGiuridicaForm.setIndietroVisibile(true);
		}
		if (Boolean.TRUE.equals(session.getAttribute("tornaEmailUfficio"))) {
			setNuovoSoggetto(personaGiuridicaForm, request);
			personaGiuridicaForm.setIndietroVisibile(true);
		}
		if (Boolean.TRUE.equals(session.getAttribute("tornaProtocollo"))) {
			//setNuovoSoggetto(personaFisicaForm, request);
			personaGiuridicaForm.setIndietroVisibile(true);
		}
		if (Boolean.TRUE.equals(session.getAttribute("tornaProcedimento"))) {
			//setNuovoSoggetto(personaFisicaForm, request);
			personaGiuridicaForm.setIndietroVisibile(true);
		}
		
		if (request.getParameter("parId") != null
				|| session.getAttribute("perId") != null) {

			if (request.getParameter("parId") != null)
				personaGiuridica = delegate.getPersonaGiuridica(Integer
						.parseInt(request.getParameter("parId")));
			else {
				personaGiuridica = delegate.getPersonaGiuridica(Integer
						.parseInt((String) session.getAttribute("perId")));
				session.removeAttribute("perId");
			}
			if (Boolean.TRUE.equals(session.getAttribute("tornaProcedimento"))) {
				session.removeAttribute("tornaProcedimento");
				if (session.getAttribute("isInteressato") != null) {
					session.setAttribute("interessato", personaGiuridica);
					session.removeAttribute("isInteressato");
				} else if(session.getAttribute("isDelegato") != null){
					session.removeAttribute("isDelegato");
					session.setAttribute("delegato", personaGiuridica);
				}else
					session.setAttribute("autorita_emanante", personaGiuridica);
				return (mapping.findForward("tornaProcedimento"));

			} else if (Boolean.TRUE.equals(session
					.getAttribute("tornaProtocollo"))) {
				session.removeAttribute("tornaProtocollo");
				Object pForm = session.getAttribute("protocolloForm");
				if (pForm != null) {
					ArrayList navBar = (ArrayList) session
							.getAttribute(Constants.NAV_BAR);
					if (navBar.size() > 1) {
						navBar.remove(navBar.size() - 1);
					}
					if (pForm instanceof ProtocolloIngressoForm) {
						ProtocolloIngressoForm protForm = (ProtocolloIngressoForm) pForm;
						// soggetto selezionato
						protForm.setMittente(personaGiuridica);
						if(protForm.isFattura())
							return (mapping.findForward("tornaFattura"));
						else
							return (mapping.findForward("tornaProtocolloIngresso"));
					} 
					else {
						ProtocolloUscitaForm protForm = (ProtocolloUscitaForm) pForm;
						// soggetto selezionato
						// Modifiche Greco-Bosco su soggetto e indirizzo
						protForm.setNominativoDestinatario(personaGiuridica
								.getDescrizioneDitta());
						protForm.setEmailDestinatario(personaGiuridica
								.getIndirizzoEMail());
						protForm.setCitta(personaGiuridica.getIndirizzo()
								.getComune());
						protForm.setCapDestinatario(personaGiuridica
								.getIndirizzo().getCap());
						StringBuffer indirizzo = new StringBuffer();
						if (personaGiuridica.getIndirizzo().getToponimo() != null) {
							indirizzo.append(personaGiuridica.getIndirizzo()
									.getToponimo());
						}

						if (personaGiuridica.getIndirizzo().getCivico() != null
								&& !"".equals(personaGiuridica.getIndirizzo()
										.getCivico())) {
							indirizzo.append(", "
									+ personaGiuridica.getIndirizzo()
											.getCivico());
						}
						protForm.setIndirizzoDestinatario(indirizzo.toString());
						return (mapping.findForward("tornaProtocolloUscita"));
					}
				}
			} else if (Boolean.TRUE.equals(session
					.getAttribute("tornaTitolario"))) {
				session.removeAttribute("tornaTitolario");
				Object titForm = session.getAttribute("titolarioForm");
				if (titForm != null) {
					ArrayList navBar = (ArrayList) session
							.getAttribute(Constants.NAV_BAR);
					if (navBar.size() > 1) {
						navBar.remove(navBar.size() - 1);
					}
					TitolarioForm tForm = (TitolarioForm) titForm;
					tForm.setNominativoAmministrazione(personaGiuridica
							.getDescrizioneDitta());
					tForm.setAmministrazioneId(personaGiuridica.getId()
							.intValue());

					return (mapping.findForward("tornaTitolario"));
				}
			} else if (Boolean.TRUE.equals(session
					.getAttribute("tornaTipoProcedimento"))) {
				session.removeAttribute("tornaTipoProcedimento");
				Object tpForm = session.getAttribute("tipoProcedimentoForm");
				if (tpForm != null) {
					ArrayList navBar = (ArrayList) session
							.getAttribute(Constants.NAV_BAR);
					if (navBar.size() > 1) {
						navBar.remove(navBar.size() - 1);
					}
					TipoProcedimentoForm tForm = (TipoProcedimentoForm) tpForm;
					tForm.setNominativoAmministrazione(personaGiuridica
							.getDescrizioneDitta());
					tForm.setAmministrazioneId(personaGiuridica.getId()
							.intValue());
					return (mapping.findForward("tornaTipoProcedimento"));
				}
			} else if (Boolean.TRUE.equals(session
					.getAttribute("tornaInvioFascicolo"))) {
				session.removeAttribute("tornaInvioFascicolo");
				FascicoloForm fForm = (FascicoloForm) session
						.getAttribute("fascicoloForm");
				// soggetto selezionato
				fForm.setNominativoDestinatario(personaGiuridica
						.getDescrizioneDitta());
				fForm
						.setEmailDestinatario(personaGiuridica
								.getIndirizzoEMail());
				fForm.setCitta(personaGiuridica.getIndirizzo().getComune());
				StringBuffer indirizzo = new StringBuffer();
				if (personaGiuridica.getIndirizzo().getToponimo() != null) {
					indirizzo.append(personaGiuridica.getIndirizzo()
							.getToponimo());
				}

				if (personaGiuridica.getIndirizzo().getCivico() != null
						&& !"".equals(personaGiuridica.getIndirizzo()
								.getCivico())) {
					indirizzo.append(", "
							+ personaGiuridica.getIndirizzo().getCivico());
				}
				fForm.setIndirizzoDestinatario(indirizzo.toString());
				return (mapping.findForward("tornaInvioFascicolo"));
			} else if (Boolean.TRUE.equals(session
					.getAttribute("tornaInvioDocumento"))) {
				session.removeAttribute("tornaInvioDocumento");
				DocumentoForm fForm = (DocumentoForm) session
						.getAttribute("documentoForm");
				// soggetto selezionato
				fForm.setNominativoDestinatario(personaGiuridica
						.getDescrizioneDitta());
				fForm
						.setEmailDestinatario(personaGiuridica
								.getIndirizzoEMail());
				fForm.setCitta(personaGiuridica.getIndirizzo().getComune());
				StringBuffer indirizzo = new StringBuffer();
				if (personaGiuridica.getIndirizzo().getToponimo() != null) {
					indirizzo.append(personaGiuridica.getIndirizzo()
							.getToponimo());
				}

				if (personaGiuridica.getIndirizzo().getCivico() != null
						&& !"".equals(personaGiuridica.getIndirizzo()
								.getCivico())) {
					indirizzo.append(", "
							+ personaGiuridica.getIndirizzo().getCivico());
				}
				fForm.setIndirizzoDestinatario(indirizzo.toString());
				return (mapping.findForward("tornaInvioDocumento"));
			}else if (Boolean.TRUE.equals(session
					.getAttribute("tornaInvioDocumentoEditor"))) {
				session.removeAttribute("tornaInvioDocumentoEditor");
				EditorForm fForm = (EditorForm) session
						.getAttribute("editorForm");
				// soggetto selezionato
				fForm.setNominativoDestinatario(personaGiuridica
						.getDescrizioneDitta());
				fForm
						.setEmailDestinatario(personaGiuridica
								.getIndirizzoEMail());
				fForm.setCitta(personaGiuridica.getIndirizzo().getComune());
				StringBuffer indirizzo = new StringBuffer();
				if (personaGiuridica.getIndirizzo().getToponimo() != null) {
					indirizzo.append(personaGiuridica.getIndirizzo()
							.getToponimo());
				}

				if (personaGiuridica.getIndirizzo().getCivico() != null
						&& !"".equals(personaGiuridica.getIndirizzo()
								.getCivico())) {
					indirizzo.append(", "
							+ personaGiuridica.getIndirizzo().getCivico());
				}
				fForm.setIndirizzoDestinatario(indirizzo.toString());
				return (mapping.findForward("tornaInvioDocumentoEditor"));
			}  
			//
			else if (Boolean.TRUE.equals(session
					.getAttribute("tornaDocumentoTemplate"))) {
				session.removeAttribute("tornaDocumentoTemplate");
				EditorForm fForm = (EditorForm) session
						.getAttribute("editorForm");
				// soggetto selezionato
				fForm.setNominativoDestinatario(personaGiuridica
						.getDescrizioneDitta());
				fForm
						.setEmailDestinatario(personaGiuridica
								.getIndirizzoEMail());
				fForm.setCitta(personaGiuridica.getIndirizzo().getComune());
				StringBuffer indirizzo = new StringBuffer();
				if (personaGiuridica.getIndirizzo().getToponimo() != null) {
					indirizzo.append(personaGiuridica.getIndirizzo()
							.getToponimo());
				}

				if (personaGiuridica.getIndirizzo().getCivico() != null
						&& !"".equals(personaGiuridica.getIndirizzo()
								.getCivico())) {
					indirizzo.append(", "
							+ personaGiuridica.getIndirizzo().getCivico());
				}
				fForm.setIndirizzoDestinatario(indirizzo.toString());
				return (mapping.findForward("tornaDocumentoTemplate"));
			}  
			else {
				writeForm(personaGiuridicaForm, personaGiuridica);
				return (mapping.findForward("tornaSoggetto"));
			}
		}

		if (personaGiuridicaForm.getSalvaAction() != null) {
			readForm(personaGiuridica, personaGiuridicaForm);
			errors = personaGiuridicaForm.validateDatiInserimento(mapping,
					request);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return (mapping.findForward("input"));
			}
			personaGiuridicaForm.setSalvaAction(null);
			Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
			int returnValue = delegate.salvaPersonaGiuridica(personaGiuridica,
					utente);
			if (returnValue == ReturnValues.SAVED) {

				if (session.getAttribute("nuovoInCerca") != null) {
					messages.add("operazione_ok", new ActionMessage(
							"operazione_ok", "", ""));
					saveMessages(request, messages);
					session.setAttribute("perId", String
							.valueOf(personaGiuridica.getId()));
					return (mapping.findForward("salvaPerProtocollo"));
				}
				if (Boolean.TRUE.equals(session.getAttribute("tornaEmail"))) {
					session.removeAttribute("tornaEmail");
					session.setAttribute("mittenteId", personaGiuridica.getId());
					return (mapping.findForward("tornaEmail"));

				}
				if (Boolean.TRUE.equals(session.getAttribute("tornaEmailUfficio"))) {
					session.removeAttribute("tornaEmailUfficio");
					session.setAttribute("mittenteId", personaGiuridica.getId());
					return (mapping.findForward("tornaEmailUfficio"));

				}
				//
				messages.add("operazione_ok", new ActionMessage(
						"operazione_ok", "", ""));
				saveMessages(request, messages);
				request.setAttribute("parId", personaGiuridica.getId());
				return (mapping.findForward("input"));

			} else {
				errors.add("errore_nel_salvataggio", new ActionMessage(
						"errore_nel_salvataggio", "", ""));
				saveErrors(request, errors);
				return (mapping.findForward("input"));
			}
		}
		if (personaGiuridicaForm.getDeleteAction() != null) {
			readForm(personaGiuridica, personaGiuridicaForm);
			int returnValue = delegate.cancellaSoggetto(personaGiuridica
					.getId().intValue());
			if (returnValue != ReturnValues.SAVED) {
				errors.add("record_non_cancellabile", new ActionMessage(
						"record_non_cancellabile", "il soggetto",
						"perch� � contenuto in una lista di distribuzione"));
				saveErrors(request, errors);
				return (mapping.findForward("input"));
			}

			return (mapping.findForward("cerca"));
		}

		String p_descrizioneDitta = (String) request
				.getAttribute("descrizioneDitta");
		boolean preQuery = (!"".equals(p_descrizioneDitta) && p_descrizioneDitta != null);

		if (personaGiuridicaForm.getCerca() != null || preQuery) {
			if (!"".equals(p_descrizioneDitta) && p_descrizioneDitta != null)
				personaGiuridicaForm.setDescrizioneDitta(p_descrizioneDitta);

			errors = personaGiuridicaForm.validate(mapping, request);
			// Ci sono errori
			if (session.getAttribute("provenienza") == null) {
				indietroVisibile = false;
				personaGiuridicaForm.setIndietroVisibile(indietroVisibile);
				if (!errors.isEmpty()) {
					saveErrors(request, errors);
					return mapping.findForward("input");
				}
			} else if (session.getAttribute("provenienza").equals(
					"personaGiuridicaFascicolo")
					|| session.getAttribute("provenienza").equals(
							"personaGiuridicaDocumento")
					|| session.getAttribute("provenienza").equals(
							"personaGiuridicaEditor")
					|| session.getAttribute("provenienza").equals(
							"personaGiuridicaTemplate")
					|| session.getAttribute("provenienza").equals(
							"personaGiuridicaProtocollo")
					|| session.getAttribute("provenienza").equals(
							"personaGiuridicaProtocolloI")) {
				indietroVisibile = true;
				personaGiuridicaForm.setIndietroVisibile(indietroVisibile);
				if (!errors.isEmpty()) {
					saveErrors(request, errors);
					return mapping.findForward("input");
				}
			}

			Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
			personaGiuridicaForm.setListaPersone(delegate
					.getListaPersonaGiuridica(utente.getAreaOrganizzativa()
							.getId().intValue(), personaGiuridicaForm
							.getDescrizioneDitta(), personaGiuridicaForm
							.getPartitaIva(),personaGiuridicaForm.getSoggetto().getIndirizzo().getComune()));
			if (personaGiuridicaForm.getListaPersone() != null
					&& personaGiuridicaForm.getListaPersone().size() == 0) {
				errors.add("nessun_dato", new ActionMessage("nessun_dato", "",
						""));
				saveErrors(request, errors);
			}
			personaGiuridicaForm.setDescrizioneDitta("");
			return (mapping.findForward("input"));
		} else if (request.getParameter("indietroPG") != null) {
			String provenienza = null;
			if (session.getAttribute("provenienza") != null) {
				provenienza = (String) session.getAttribute("provenienza");
				session.removeAttribute("provenienza");
			}
			if (provenienza != null) {
				if (provenienza.equals(
						"personaGiuridicaProtocollo")) {
					return mapping.findForward("tornaProtocolloUscita");

				} else if (provenienza.equals(
						"personaGiuridicaFacicolo")) {
					return mapping.findForward("tornaInvioFascicolo");

				} else if (provenienza.equals(
						"personaGiuridicaDocumento")) {
					return mapping.findForward("tornaInvioDocumento");
				}else if (provenienza.equals(
						"personaGiuridicaEditor")) {
					return mapping.findForward("tornaInvioDocumentoEditor");
				}else if (provenienza.equals(
						"personaGiuridicaTemplate")) {
					return mapping.findForward("tornaDocumentoTemplate");
				}
				else if (provenienza.equals(
						"personaGiuridicaProtocolloI")) {
					return mapping.findForward("tornaProtocolloIngresso");
				}else if (provenienza.equals(
						"personaGiuridicaProtocolloF")) {
					return mapping.findForward("tornaFattura");
				}
			} else if (Boolean.TRUE.equals(session.getAttribute("tornaEmail"))) {
				session.removeAttribute("tornaEmail");
				return mapping.findForward("indietroEmail");
			} else if (Boolean.TRUE.equals(session.getAttribute("tornaEmailUfficio"))) {
				session.removeAttribute("tornaEmailUfficio");
				return mapping.findForward("indietroEmailUfficio");
			} else if (Boolean.TRUE.equals(session.getAttribute("tornaProcedimento"))) {
				session.removeAttribute("tornaProcedimento");
				return mapping.findForward("tornaProcedimento");
			}

		} else if (request.getParameter("nuovaPG") != null) {
			session.setAttribute("nuovoInCerca", true);
			return mapping.findForward("nuova");
		} else if (request.getParameter("annulla") != null) {
			session.removeAttribute("tornaProtocollo");
			session.removeAttribute("tornaInvioFascicolo");
			session.removeAttribute("provenienza");
			indietroVisibile = false;
			personaGiuridicaForm.setIndietroVisibile(indietroVisibile);
		} else if (request.getParameter("nuova") != null) {
			return (mapping.findForward("nuova"));
		} else if ("personaGiuridicaProtocollo".equals(session
				.getAttribute("provenienza"))) {
			indietroVisibile = true;
			personaGiuridicaForm.setNuovoVisibile(true);
			personaGiuridicaForm.setIndietroVisibile(indietroVisibile);
			personaGiuridicaForm.setDescrizioneDitta("");
			return (mapping.findForward("input"));
		} else if ("personaGiuridicaFacicolo".equals(session
				.getAttribute("provenienza"))) {
			indietroVisibile = true;
			personaGiuridicaForm.setIndietroVisibile(indietroVisibile);
			return (mapping.findForward("input"));
		} else if ("personaGiuridicaDocumento".equals(session
				.getAttribute("provenienza"))) {
			indietroVisibile = true;
			personaGiuridicaForm.setIndietroVisibile(indietroVisibile);
			return (mapping.findForward("input"));
		}else if ("personaGiuridicaEditor".equals(session
				.getAttribute("provenienza"))) {
			indietroVisibile = true;
			personaGiuridicaForm.setIndietroVisibile(indietroVisibile);
			return (mapping.findForward("input"));
		} else if ("personaGiuridicaTemplate".equals(session
				.getAttribute("provenienza"))) {
			indietroVisibile = true;
			personaGiuridicaForm.setIndietroVisibile(indietroVisibile);
			return (mapping.findForward("input"));
		} 
		else if ("personaGiuridicaProtocolloI".equals(session
				.getAttribute("provenienza"))) {
			indietroVisibile = true;
			personaGiuridicaForm.setNuovoVisibile(true);
			personaGiuridicaForm.setIndietroVisibile(indietroVisibile);
			return (mapping.findForward("input"));
		}

		//indietroVisibile = false;
		//personaGiuridicaForm.setIndietroVisibile(indietroVisibile);
		return (mapping.findForward("input"));

	}

	public static void readForm(SoggettoVO personaGiuridica,
			PersonaGiuridicaForm form) {

		// ===== Dati del Protocollo VO
		personaGiuridica.setId(form.getSoggettoId());
		personaGiuridica.setDescrizioneDitta(form.getDescrizioneDitta());
		personaGiuridica.setPartitaIva(form.getPartitaIva());
		personaGiuridica.setDug(form.getDug());
		personaGiuridica.setIndirizzo(form.getSoggetto().getIndirizzo());
		personaGiuridica.setIndirizzoEMail(form.getSoggetto()
				.getIndirizzoEMail());
		personaGiuridica.setIndirizzoWeb(form.getSoggetto().getIndirizzoWeb());
		personaGiuridica.setNote(form.getSoggetto().getNote());
		personaGiuridica.setTeleFax(form.getSoggetto().getTeleFax());
		personaGiuridica.setTelefono(form.getSoggetto().getTelefono());
		if (form.getFlagSettoreAppartenenza() != null
				&& !"".equals(form.getFlagSettoreAppartenenza())) {
			personaGiuridica.setFlagSettoreAppartenenza(Integer.parseInt(form
					.getFlagSettoreAppartenenza()));
		}
		personaGiuridica.setReferente(form.getReferente());
		personaGiuridica.setTelefonoReferente(form.getTelefonoReferente());
		personaGiuridica.setIndirizzoEMail(form.getEmailReferente());

	}

	public static void writeForm(PersonaGiuridicaForm form,
			SoggettoVO personaGiuridica) {
		form.setSoggettoId(personaGiuridica.getId().intValue());
		form.setDescrizioneDitta(personaGiuridica.getDescrizioneDitta());
		form.setPartitaIva(personaGiuridica.getPartitaIva());
		form.setDug(personaGiuridica.getDug());
		form.setEmailReferente(personaGiuridica.getIndirizzoEMail());
		form.setFlagSettoreAppartenenza(String.valueOf(personaGiuridica
				.getFlagSettoreAppartenenza()));
		form.setPartitaIva(personaGiuridica.getPartitaIva());
		form.setReferente(personaGiuridica.getReferente());
		form.setTelefonoReferente(personaGiuridica.getTelefonoReferente());
		form.getSoggetto().setIndirizzoEMail(
				personaGiuridica.getIndirizzoEMail());
		form.getSoggetto().setIndirizzoWeb(personaGiuridica.getIndirizzoWeb());
		form.getSoggetto().getIndirizzo().setCap(
				personaGiuridica.getIndirizzo().getCap());
		form.getSoggetto().getIndirizzo().setCivico(
				personaGiuridica.getIndirizzo().getCivico());
		form.getSoggetto().getIndirizzo().setComune(
				personaGiuridica.getIndirizzo().getComune());
		form.getSoggetto().getIndirizzo().setProvinciaId(
				personaGiuridica.getIndirizzo().getProvinciaId());
		form.getSoggetto().getIndirizzo().setToponimo(
				personaGiuridica.getIndirizzo().getToponimo());
		form.getSoggetto().setTelefono(personaGiuridica.getTelefono());
		form.getSoggetto().setTeleFax(personaGiuridica.getTeleFax());

	}

	private void setNuovoSoggetto(PersonaGiuridicaForm form,
			HttpServletRequest request) {
		if (request.getAttribute("emailMittente") != null)
			form.setEmailReferente((String) request
					.getAttribute("emailMittente"));
		if (request.getAttribute("descrizioneMittente") != null)
			form.setDescrizioneDitta((String) request
					.getAttribute("descrizioneMittente"));
	}

}
