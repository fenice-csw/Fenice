package it.finsiel.siged.mvc.presentation.action.soggetto;

import it.compit.fenice.mvc.presentation.actionform.documentale.EditorForm;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.SoggettoDelegate;
import it.finsiel.siged.mvc.presentation.actionform.documentale.DocumentoForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.FascicoloForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloIngressoForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloUscitaForm;
import it.finsiel.siged.mvc.presentation.actionform.soggetto.PersonaFisicaForm;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.StringUtil;

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

public class PersonaFisicaAction extends Action {

	static Logger logger = Logger
			.getLogger(PersonaFisicaAction.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm pfForm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		HttpSession session = request.getSession();
		SoggettoDelegate delegate = SoggettoDelegate.getInstance();
		SoggettoVO personaFisica = new SoggettoVO('F');
		PersonaFisicaForm personaFisicaForm = (PersonaFisicaForm) pfForm;
		boolean indietroVisibile = false;
		personaFisicaForm.setIndietroVisibile(indietroVisibile);
		
		if (pfForm == null) {
			logger.info(" Creating new Persona Fisica Form");
			pfForm = new PersonaFisicaForm();
			request.setAttribute(mapping.getAttribute(), pfForm);
		}

		if (Boolean.TRUE.equals(session.getAttribute("tornaEmail"))) {
			setNuovoSoggetto(personaFisicaForm, request);
			personaFisicaForm.setIndietroVisibile(true);
		}
		if (Boolean.TRUE.equals(session.getAttribute("tornaEmailUfficio"))) {
			setNuovoSoggetto(personaFisicaForm, request);
			personaFisicaForm.setIndietroVisibile(true);
		}
		if (Boolean.TRUE.equals(session.getAttribute("tornaProtocollo"))) {
			personaFisicaForm.setIndietroVisibile(true);
		} if (Boolean.TRUE.equals(session.getAttribute("tornaProcedimento"))) {
			personaFisicaForm.setIndietroVisibile(true);
		}

		if (request.getParameter("parId") != null
				|| session.getAttribute("perId") != null) {
			if (request.getParameter("parId") != null)
				personaFisica = delegate.getPersonaFisica(Integer
						.parseInt(request.getParameter("parId")));
			else {
				personaFisica = delegate.getPersonaFisica(Integer
						.parseInt((String) session.getAttribute("perId")));
				session.removeAttribute("perId");
			}
			if (Boolean.TRUE.equals(session.getAttribute("tornaFascicolo"))) {
				session.removeAttribute("tornaFascicolo");
				if (session.getAttribute("isInteressato") != null) {
					session.setAttribute("interessato", personaFisica);
					session.removeAttribute("isInteressato");
				} else
					session.setAttribute("delegato", personaFisica);
				return (mapping.findForward("tornaFascicolo"));

			} else if (Boolean.TRUE.equals(session
					.getAttribute("tornaProcedimento"))) {
				session.removeAttribute("tornaProcedimento");
				if (session.getAttribute("isInteressato") != null) {
					session.setAttribute("interessato", personaFisica);
					session.removeAttribute("isInteressato");
				} else{
					session.removeAttribute("isDelegato");
					session.setAttribute("delegato", personaFisica);
				}
				return (mapping.findForward("tornaProcedimento"));

			} else if (Boolean.TRUE.equals(session
					.getAttribute("tornaProtocollo"))) {
				session.removeAttribute("tornaProtocollo");
				Object pForm = session.getAttribute("protocolloForm");
				if (pForm != null) {
					ArrayList navBar = (ArrayList) session.getAttribute(Constants.NAV_BAR);
					if (navBar.size() > 1) {
						navBar.remove(navBar.size() - 1);
					}
					if (pForm instanceof ProtocolloIngressoForm) {

						ProtocolloIngressoForm protForm = (ProtocolloIngressoForm) pForm;
						protForm.setMittente(personaFisica);					
						if (protForm.isFattura())
							return (mapping.findForward("tornaFattura"));
						else
							return (mapping
									.findForward("tornaProtocolloIngresso"));
					} else {

						ProtocolloUscitaForm protForm = (ProtocolloUscitaForm) pForm;

						protForm.setCognomeDestinatario(personaFisica
								.getCognome());
						if (personaFisica.getNome() != null
								&& !"".equals(personaFisica.getNome())) {

							protForm.setNomeDestinatario(personaFisica
									.getNome());
						}
						protForm.setEmailDestinatario(personaFisica
								.getIndirizzoEMail());
						protForm.setCitta(personaFisica.getIndirizzo()
								.getComune());
						StringBuffer indirizzo = new StringBuffer();
						if (personaFisica.getIndirizzo().getToponimo() != null) {
							indirizzo.append(personaFisica.getIndirizzo()
									.getToponimo());
						}
						if (personaFisica.getIndirizzo().getCivico() != null
								&& !"".equals(personaFisica.getIndirizzo()
										.getCivico())) {
							indirizzo.append(", "
									+ personaFisica.getIndirizzo().getCivico());
						}
						String capDestinatario = "";
						if (personaFisica.getIndirizzo().getCap() != null
								&& !"".equals(personaFisica.getIndirizzo()
										.getCap())) {
							capDestinatario = personaFisica.getIndirizzo()
									.getCap();

						}
						protForm.setCapDestinatario(capDestinatario);
						protForm.setIndirizzoDestinatario(indirizzo.toString());

						return (mapping.findForward("tornaProtocolloUscita"));
					}
				}
			} else if (Boolean.TRUE.equals(session
					.getAttribute("tornaInvioFascicolo"))) {
				session.removeAttribute("tornaInvioFascicolo");
				FascicoloForm fForm = (FascicoloForm) session
						.getAttribute("fascicoloForm");
				fForm.setDestinatarioId(personaFisica.getId().intValue());
				fForm.setNominativoDestinatario(personaFisica.getCognome()
						+ " " + StringUtil.getStringa(personaFisica.getNome()));
				// soggetto selezionato
				fForm.setNominativoDestinatario(personaFisica.getCognome());
				if (personaFisica.getNome() != null
						&& !"".equals(personaFisica.getNome())) {
					fForm.setNominativoDestinatario(fForm
							.getNominativoDestinatario()
							+ personaFisica.getNome());
				}
				fForm.setEmailDestinatario(personaFisica.getIndirizzoEMail());
				fForm.setCitta(personaFisica.getIndirizzo().getComune());
				StringBuffer indirizzo = new StringBuffer();
				if (personaFisica.getIndirizzo().getToponimo() != null) {
					indirizzo
							.append(personaFisica.getIndirizzo().getToponimo());
				}
				if (personaFisica.getIndirizzo().getCivico() != null
						&& !"".equals(personaFisica.getIndirizzo().getCivico())) {
					indirizzo.append(", "
							+ personaFisica.getIndirizzo().getCivico());
				}

				fForm.setIndirizzoDestinatario(indirizzo.toString());

				return (mapping.findForward("tornaInvioFascicolo"));
			} else if (Boolean.TRUE.equals(session
					.getAttribute("tornaInvioDocumento"))) {
				session.removeAttribute("tornaInvioDocumento");
				DocumentoForm fForm = (DocumentoForm) session
						.getAttribute("documentoForm");
				fForm.setDestinatarioId(personaFisica.getId().intValue());
				fForm.setNominativoDestinatario(personaFisica.getCognome()
						+ " " + StringUtil.getStringa(personaFisica.getNome()));
				// soggetto selezionato
				fForm.setNominativoDestinatario(personaFisica.getCognome());
				if (personaFisica.getNome() != null
						&& !"".equals(personaFisica.getNome())) {
					fForm.setNominativoDestinatario(fForm
							.getNominativoDestinatario()
							+ personaFisica.getNome());
				}
				fForm.setEmailDestinatario(personaFisica.getIndirizzoEMail());
				fForm.setCitta(personaFisica.getIndirizzo().getComune());
				StringBuffer indirizzo = new StringBuffer();
				if (personaFisica.getIndirizzo().getToponimo() != null) {
					indirizzo
							.append(personaFisica.getIndirizzo().getToponimo());
				}
				if (personaFisica.getIndirizzo().getCivico() != null
						&& !"".equals(personaFisica.getIndirizzo().getCivico())) {
					indirizzo.append(", "
							+ personaFisica.getIndirizzo().getCivico());
				}

				String capDestinatario = "";
				if (personaFisica.getIndirizzo().getCap() != null
						&& !"".equals(personaFisica.getIndirizzo().getCap())) {
					capDestinatario = personaFisica.getIndirizzo().getCap();

				}
				fForm.setCapDestinatario(capDestinatario);
				fForm.setIndirizzoDestinatario(indirizzo.toString());

				return (mapping.findForward("tornaInvioDocumento"));
			} else if (Boolean.TRUE.equals(session
					.getAttribute("tornaInvioDocumentoEditor"))) {
				session.removeAttribute("tornaInvioDocumentoEditor");
				EditorForm fForm = (EditorForm) session
						.getAttribute("editorForm");
				fForm.setDestinatarioId(personaFisica.getId().intValue());
				fForm.setNominativoDestinatario(StringUtil.getStringa(personaFisica.getCognome())+ " " + StringUtil.getStringa(personaFisica.getNome()));
				fForm.setEmailDestinatario(personaFisica.getIndirizzoEMail());
				fForm.setCitta(personaFisica.getIndirizzo().getComune());
				StringBuffer indirizzo = new StringBuffer();
				if (personaFisica.getIndirizzo().getToponimo() != null) {
					indirizzo
							.append(personaFisica.getIndirizzo().getToponimo());
				}
				if (personaFisica.getIndirizzo().getCivico() != null
						&& !"".equals(personaFisica.getIndirizzo().getCivico())) {
					indirizzo.append(", "
							+ personaFisica.getIndirizzo().getCivico());
				}

				String capDestinatario = "";
				if (personaFisica.getIndirizzo().getCap() != null
						&& !"".equals(personaFisica.getIndirizzo().getCap())) {
					capDestinatario = personaFisica.getIndirizzo().getCap();

				}
				fForm.setCapDestinatario(capDestinatario);
				fForm.setIndirizzoDestinatario(indirizzo.toString());

				return (mapping.findForward("tornaInvioDocumentoEditor"));
			}else if (Boolean.TRUE.equals(session
					.getAttribute("tornaDocumentoTemplate"))) {
				session.removeAttribute("tornaDocumentoTemplate");
				EditorForm fForm = (EditorForm) session
						.getAttribute("editorForm");
				fForm.setDestinatarioId(personaFisica.getId().intValue());				
				fForm.setNominativoDestinatario(StringUtil.getStringa(personaFisica.getCognome())+ " " + StringUtil.getStringa(personaFisica.getNome()));
				fForm.setEmailDestinatario(personaFisica.getIndirizzoEMail());
				fForm.setCitta(personaFisica.getIndirizzo().getComune());
				StringBuffer indirizzo = new StringBuffer();
				if (personaFisica.getIndirizzo().getToponimo() != null) {
					indirizzo
							.append(personaFisica.getIndirizzo().getToponimo());
				}
				if (personaFisica.getIndirizzo().getCivico() != null
						&& !"".equals(personaFisica.getIndirizzo().getCivico())) {
					indirizzo.append(", "
							+ personaFisica.getIndirizzo().getCivico());
				}

				String capDestinatario = "";
				if (personaFisica.getIndirizzo().getCap() != null
						&& !"".equals(personaFisica.getIndirizzo().getCap())) {
					capDestinatario = personaFisica.getIndirizzo().getCap();

				}
				fForm.setCapDestinatario(capDestinatario);
				fForm.setIndirizzoDestinatario(indirizzo.toString());
				return (mapping.findForward("tornaDocumentoTemplate"));
			} 
			else {
				writeForm(personaFisicaForm, personaFisica);
				return (mapping.findForward("tornaSoggetto"));
			}
		}

		// l'utente vuole salvare un Soggetto
		if (personaFisicaForm.getSalvaAction() != null) {
			personaFisicaForm.setSalvaAction(null);
			readForm(personaFisica, personaFisicaForm);
			errors = personaFisicaForm
					.validateDatiInserimento(mapping, request);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return (mapping.findForward("input"));
			}

			Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
			int returnValue = delegate
					.salvaPersonaFisica(personaFisica, utente);
			if (returnValue == ReturnValues.SAVED) {
				//
				if (Boolean.TRUE.equals(session.getAttribute("tornaEmail"))) {
					session.removeAttribute("tornaEmail");
					session.setAttribute("mittenteId", personaFisica.getId());
					return (mapping.findForward("tornaEmail"));

				}
				if (Boolean.TRUE.equals(session.getAttribute("tornaEmailUfficio"))) {
					session.removeAttribute("tornaEmailUfficio");
					session.setAttribute("mittenteId", personaFisica.getId());
					return (mapping.findForward("tornaEmailUfficio"));

				}
				if (session.getAttribute("nuovoInCerca") != null) {

					messages.add("operazione_ok", new ActionMessage(
							"operazione_ok", "", ""));
					saveMessages(request, messages);
					session.setAttribute("perId", String.valueOf(personaFisica
							.getId()));
					return (mapping.findForward("salvaPerProtocollo"));
				}
				//
				messages.add("operazione_ok", new ActionMessage(
						"operazione_ok", "", ""));
				saveMessages(request, messages);
				request.setAttribute("parId", personaFisica.getId());
				return (mapping.findForward("input"));

			} else {
				errors.add("errore_nel_salvataggio", new ActionMessage(
						"errore_nel_salvataggio", "", ""));
				saveErrors(request, errors);
				return (mapping.findForward("input"));
			}

		} else if (personaFisicaForm.getDeleteAction() != null) {
			readForm(personaFisica, personaFisicaForm);

			int returnValue = delegate.cancellaSoggetto(personaFisica.getId()
					.intValue());
			if (returnValue != ReturnValues.SAVED) {
				errors.add("record_non_cancellabile", new ActionMessage(
						"record_non_cancellabile", "il soggetto",
						"perch� � contenuto in una lista di distribuzione"));
				saveErrors(request, errors);
				return (mapping.findForward("input"));
			}
			return (mapping.findForward("cerca"));

		}

		String cognomeF = (String) request.getAttribute("cognome");
		String nomeF = (String) request.getAttribute("nome");
		boolean preQuery = (!"".equals(cognomeF) && cognomeF != null)
				|| (!"".equals(nomeF) && nomeF != null);

		// l'utente vuole effettuare una ricerca
		if (personaFisicaForm.getCerca() != null || preQuery) {
			personaFisicaForm.setListaPersone(null);
			if (!"".equals(cognomeF) && cognomeF != null) {
				personaFisicaForm.setCognome(cognomeF);
			}
			if (!"".equals(nomeF) && nomeF != null) {
				personaFisicaForm.setNome(nomeF);
			}
			errors = personaFisicaForm.validate(mapping, request);

			// Ci sono errori
			if (session.getAttribute("provenienza") == null) {
				indietroVisibile = false;
				personaFisicaForm.setIndietroVisibile(indietroVisibile);
				if (!errors.isEmpty()) {
					saveErrors(request, errors);
					return mapping.findForward("input");
				}
			} else if (session.getAttribute("provenienza").equals(
					"personaFisicaFascicolo")
					|| session.getAttribute("provenienza").equals(
							"personaFisicaDocumento")
					|| session.getAttribute("provenienza").equals(
							"personaFisicaEditor")
					|| session.getAttribute("provenienza").equals(
							"personaFisicaTemplate")
					|| session.getAttribute("provenienza").equals(
							"personaFisicaProtocollo")
					|| session.getAttribute("provenienza").equals(
							"personaFisicaProtocolloI")
					|| session.getAttribute("provenienza").equals(
							"personaFisicaNuovoFascicolo")) {
				indietroVisibile = true;
				personaFisicaForm.setIndietroVisibile(indietroVisibile);
				if (!errors.isEmpty()) {
					saveErrors(request, errors);
					return mapping.findForward("input");
				}
			}
			Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
			personaFisicaForm.setListaPersone(delegate.getListaPersonaFisica(
					utente.getAreaOrganizzativa().getId().intValue(),
					personaFisicaForm.getCognome(),
					personaFisicaForm.getNome(), personaFisicaForm
							.getCodiceFiscale(),personaFisicaForm.getSoggetto().getIndirizzo().getComune()));
			if (personaFisicaForm.getListaPersone() != null
					&& personaFisicaForm.getListaPersone().size() == 0) {
				errors.add("nessun_dato", new ActionMessage("nessun_dato", "",
						""));
				saveErrors(request, errors);
			}
			personaFisicaForm.setCognome("");

			return (mapping.findForward("input"));

		}  else if (request.getParameter("indietroPF") != null) {
						String provenienza = null;
			if (session.getAttribute("provenienza") != null) {
				provenienza = (String) session.getAttribute("provenienza");
				session.removeAttribute("provenienza");
			}
			if (provenienza != null) {
				if (provenienza.equals("personaFisicaProtocollo")) {
					return mapping.findForward("tornaProtocolloUscita");

				} else if (provenienza.equals("personaFisicaFascicolo")) {
					return mapping.findForward("tornaInvioFascicolo");
				} else if (provenienza.equals("personaFisicaNuovoFascicolo")) {
					return mapping.findForward("indietroFascicolo");

				} else if (provenienza.equals("personaFisicaDocumento")) {
					return mapping.findForward("tornaInvioDocumento");

				} else if (provenienza.equals("personaFisicaEditor")) {
					return mapping.findForward("tornaInvioDocumentoEditor");

				} else if (provenienza.equals("personaFisicaTemplate")) {
					return mapping.findForward("tornaDocumentoTemplate");

				}else if (provenienza.equals("personaFisicaProtocolloI")) {
					return mapping.findForward("tornaProtocolloIngresso");
				} else if (provenienza.equals("personaGiuridicaProtocolloF")) {
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

		}

		else if (request.getParameter("nuovaPF") != null) {
			session.setAttribute("nuovoInCerca", true);
			return mapping.findForward("nuova");
		}

		else if (request.getParameter("annulla") != null) {
			session.removeAttribute("tornaProtocollo");
			session.removeAttribute("tornaInvioFascicolo");
			session.removeAttribute("provenienza");
			indietroVisibile = false;
			personaFisicaForm.setIndietroVisibile(indietroVisibile);
		}

		else if (request.getParameter("nuova") != null) {
			return (mapping.findForward("nuova"));
		}

		else if ("personaFisicaProtocollo".equals(session
				.getAttribute("provenienza"))) {
			indietroVisibile = true;
			personaFisicaForm.setNuovoVisibile(true);
			personaFisicaForm.setIndietroVisibile(indietroVisibile);
			return (mapping.findForward("input"));
		}

		else if ("personaFisicaFascicolo".equals(session
				.getAttribute("provenienza"))) {
			indietroVisibile = true;
			personaFisicaForm.setIndietroVisibile(indietroVisibile);
			return (mapping.findForward("input"));
		}

		else if ("personaFisicaDocumento".equals(session
				.getAttribute("provenienza"))) {
			indietroVisibile = true;
			personaFisicaForm.setIndietroVisibile(indietroVisibile);
			return (mapping.findForward("input"));
		}

		else if ("personaFisicaEditor".equals(session
				.getAttribute("provenienza"))) {
			indietroVisibile = true;
			personaFisicaForm.setIndietroVisibile(indietroVisibile);
			return (mapping.findForward("input"));
		}
		else if ("personaFisicaTemplate".equals(session
				.getAttribute("provenienza"))) {
			indietroVisibile = true;
			personaFisicaForm.setIndietroVisibile(indietroVisibile);
			return (mapping.findForward("input"));
		}
		else if ("personaFisicaProtocolloI".equals(session
				.getAttribute("provenienza"))) {
			indietroVisibile = true;
			personaFisicaForm.setIndietroVisibile(indietroVisibile);
			personaFisicaForm.setNuovoVisibile(true);
			return (mapping.findForward("input"));
		}

		else if ("personaFisicaNuovoFascicolo".equals(session
				.getAttribute("provenienza"))) {
			indietroVisibile = true;
			personaFisicaForm.setIndietroVisibile(indietroVisibile);
			personaFisicaForm.setNuovoVisibile(true);
			return (mapping.findForward("input"));
		}
		return (mapping.findForward("input"));

	}

	private void setNuovoSoggetto(PersonaFisicaForm form,
			HttpServletRequest request) {
		if (request.getAttribute("emailMittente") != null)
			form.getSoggetto().setIndirizzoEMail(
					(String) request.getAttribute("emailMittente"));
		if (request.getAttribute("descrizioneMittente") != null)
			form.setCognome((String) request
					.getAttribute("descrizioneMittente"));
	}

	private static void readForm(SoggettoVO personaFisica,
			PersonaFisicaForm form) {
		personaFisica.setId(form.getSoggettoId());
		personaFisica.setCognome(form.getCognome());
		personaFisica.setNome(form.getNome());
		personaFisica.setCodiceFiscale(form.getCodiceFiscale());
		personaFisica.setCodiceStatoCivile(form.getStatoCivile());
		personaFisica.setCodMatricola(form.getCodMatricola());
		personaFisica.setComuneNascita(form.getComuneNascita());
		if (DateUtil.isData(form.getDataNascita())) {
			personaFisica
					.setDataNascita(DateUtil.toDate(form.getDataNascita()));
		}
		personaFisica.setIndirizzo(form.getSoggetto().getIndirizzo());
		personaFisica.setIndirizzoEMail(form.getSoggetto().getIndirizzoEMail());
		personaFisica.setIndirizzoWeb(form.getSoggetto().getIndirizzoWeb());
		personaFisica.setNote(form.getSoggetto().getNote());
		if (form.getProvinciaNascitaId() != null) {
			personaFisica.setProvinciaNascitaId(Integer.parseInt(form
					.getProvinciaNascitaId()));
		}
		personaFisica.setQualifica(form.getQualifica());
		personaFisica.setCodiceStatoCivile(form.getStatoCivile());
		personaFisica.setSesso(form.getSesso());
		personaFisica.setStatoCivile(form.getStatoCivile());
		personaFisica.setTeleFax(form.getSoggetto().getTeleFax());
		personaFisica.setTelefono(form.getSoggetto().getTelefono());

	}

	private static void writeForm(PersonaFisicaForm form,
			SoggettoVO personaFisica) {
		form.setSoggettoId(personaFisica.getId().intValue());
		form.setCognome(personaFisica.getCognome());
		form.setNome(personaFisica.getNome());
		form.setCodiceFiscale(personaFisica.getCodiceFiscale());
		form.setCodMatricola(personaFisica.getCodMatricola());
		form.setComuneNascita(personaFisica.getComuneNascita());
		if (personaFisica.getDataNascita() != null) {
			form.setDataNascita(it.finsiel.siged.util.DateUtil
					.formattaData(personaFisica.getDataNascita().getTime()));
		}
		form.setProvinciaNascitaId(String.valueOf(personaFisica
				.getProvinciaNascitaId()));
		form.setQualifica(personaFisica.getQualifica());
		form.setSesso(personaFisica.getSesso());
		form.setStatoCivile(personaFisica.getStatoCivile());

		form.getSoggetto().setIndirizzoEMail(personaFisica.getIndirizzoEMail());
		form.getSoggetto().setIndirizzoWeb(personaFisica.getIndirizzoWeb());
		form.getSoggetto().getIndirizzo().setCap(
				personaFisica.getIndirizzo().getCap());
		form.getSoggetto().getIndirizzo().setCivico(
				personaFisica.getIndirizzo().getCivico());
		form.getSoggetto().getIndirizzo().setComune(
				personaFisica.getIndirizzo().getComune());
		form.getSoggetto().getIndirizzo().setProvinciaId(
				personaFisica.getIndirizzo().getProvinciaId());
		form.getSoggetto().getIndirizzo().setToponimo(
				personaFisica.getIndirizzo().getToponimo());
		form.getSoggetto().setTelefono(personaFisica.getTelefono());
		form.getSoggetto().setTeleFax(personaFisica.getTeleFax());
	}

}
