package it.compit.fenice.mvc.presentation.action.protocollo;

import it.compit.fenice.model.protocollo.PostaInterna;
import it.compit.fenice.mvc.presentation.action.protocollo.helper.form.AggiornaPostaInternaForm;
import it.compit.fenice.mvc.presentation.action.protocollo.helper.model.AggiornaPostaInternaModel;
import it.compit.fenice.mvc.presentation.actionform.protocollo.PostaInternaForm;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.business.RegistroDelegate;
import it.finsiel.siged.mvc.business.SoggettoDelegate;
import it.finsiel.siged.mvc.presentation.action.protocollo.ProtocolloAction;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm.Sezione;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;
import it.finsiel.siged.util.NumberUtil;
import it.flosslab.mvc.presentation.action.protocollo.helper.CaricaProtocollo;
import it.flosslab.mvc.presentation.action.protocollo.helper.file.ProtocolloFileUtility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class PostaInternaAction extends ProtocolloAction {

	static Logger logger = Logger.getLogger(PostaInternaAction.class.getName());

	private static void getInputPage(PostaInternaForm form, HttpSession session) {
		if (session.getAttribute("PAGINA_RITORNO") != null) {
			String pag = (String) session.getAttribute("PAGINA_RITORNO");
			if (pag.equals("SCARICO_UFFICIO"))
				form.setReturnPage("scarico_ufficio");
			else if (pag.equals("SCARICO_UTENTE"))
				form.setReturnPage("scarico_utente");
			else if (pag.equals("FASCICOLO"))
				form.setReturnPage("fascicolo");
			else if (pag.equals("SCARICO_PROCEDIMENTO"))
				form.setReturnPage("scaricoProcedimento");
			session.removeAttribute("PAGINA_RITORNO");
		}
	}

	protected void resetForm(ProtocolloForm form, Utente utente,
			HttpServletRequest request) {
		form.inizializzaForm();
		HttpSession session = request.getSession();
		session.removeAttribute("tornaDocumento");
		form.setAooId(utente.getAreaOrganizzativa().getId());
		form.setTipoDocumentoId(0);
		form.setVersioneDefault(true);
	}

	protected void assegnaUfficioMittente(ProtocolloForm form, int ufficioId) {
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(ufficioId);
		Organizzazione org = Organizzazione.getInstance();
		Ufficio uff = org.getUfficio(ufficioId);
		ass.setNomeUfficio(uff.getValueObject().getDescription());
		ass.setDescrizioneUfficio(uff.getPath());
		((PostaInternaForm) form).setMittente(ass);
		form.setTitolario(null);
	}

	protected void assegnaUtenteMittente(ProtocolloForm form) {
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(form.getUfficioCorrenteId());
		ass.setNomeUfficio(form.getUfficioCorrente().getDescription());
		ass.setDescrizioneUfficio(form.getUfficioCorrentePath());
		ass.setUtenteId(form.getUtenteSelezionatoId());
		UtenteVO ute = form.getUtente(form.getUtenteSelezionatoId());
		ass.setNomeUtente(ute.getFullName());
		((PostaInternaForm) form).setMittente(ass);
		form.setTitolario(null);
	}

	protected void assegnaAdUfficio(ProtocolloForm form, int ufficioId,
			Utente utente) {
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(ufficioId);
		Organizzazione org = Organizzazione.getInstance();
		Ufficio uff = org.getUfficio(ufficioId);
		ass.setNomeUfficio(uff.getValueObject().getDescription());
		ass.setDescrizioneUfficio(uff.getPath());
		PostaInternaForm pForm = (PostaInternaForm) form;
		ass.setCompetente(true);
		ass.setCaricaAssegnanteId(utente.getCaricaInUso());
		ass.setUfficioAssegnanteId(utente.getUfficioInUso());
		pForm.aggiungiDestinatario(ass);
		pForm.setDestinatarioCompetente(ass.getKey());
		form.setUfficioSelezionatoId(0);
		if (form.isDipTitolarioUfficio()) {
			form.setTitolario(null);
		}
		updateDestinatariCompetenti(pForm);
	}

	protected void assegnaAdUtente(ProtocolloForm form, Utente utente) {
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(form.getUfficioCorrenteId());
		ass.setNomeUfficio(form.getUfficioCorrente().getDescription());
		ass.setDescrizioneUfficio(form.getUfficioCorrentePath());
		ass.setUtenteId(form.getUtenteSelezionatoId());
		UtenteVO ute = form.getUtente(form.getUtenteSelezionatoId());
		ass.setNomeUtente(ute.getCaricaFullName());
		PostaInternaForm pForm = (PostaInternaForm) form;
		ass.setCompetente(true);
		ass.setCaricaAssegnanteId(utente.getCaricaInUso());
		ass.setUfficioAssegnanteId(utente.getUfficioInUso());
		pForm.aggiungiDestinatario(ass);
		pForm.setDestinatarioCompetente(ass.getKey());
		if (form.isDipTitolarioUfficio()) {
			form.setTitolario(null);
		}
		updateDestinatariCompetenti(pForm);
	}

	private void rimuoviDestinatari(PostaInternaForm form) {
		String[] assegnatari = form.getDestinatariSelezionatiId();
		String assegnatarioCompetente = form.getDestinatarioCompetente();
		if (assegnatari != null) {
			for (int i = 0; i < assegnatari.length; i++) {
				String assegnatario = assegnatari[i];
				if (assegnatario != null) {
					form.rimuoviDestinatario(assegnatario);
					if (assegnatario.equals(assegnatarioCompetente)) {
						form.setDestinatarioCompetente(null);
					}
				}
			}
			if (form.getDestinatarioCompetente() == null) {
				Iterator<AssegnatarioView> i = form.getDestinatari().iterator();
				if (i.hasNext()) {
					AssegnatarioView ass =  i.next();
					form.setDestinatarioCompetente(ass.getKey());
				}
			}
		}
	}

	private void updateDestinatariCompetenti(PostaInternaForm form) {
		List<String> assCompetenti = new ArrayList<String>();
		for (Iterator<AssegnatarioView> i = form.getDestinatari().iterator(); i.hasNext();) {
			AssegnatarioView ass =  i.next();
			if (ass.isCompetente()) {
				assCompetenti.add(ass.getKey());
			}
		}
		String[] assCompArray = new String[assCompetenti.size()];
		int index = 0;
		for (String assString : assCompetenti) {
			assCompArray[index] = assString;
			index++;
		}
		form.setDestinatariCompetenti(assCompArray);
	}

	protected void assegnaMittente(ProtocolloForm form, Utente utente) {
		AssegnatarioView ass = new AssegnatarioView();
		UfficioVO ufficio = utente.getUfficioVOInUso();
		ass.setUfficioId(ufficio.getId());
		ass.setNomeUfficio(ufficio.getDescription());
		ass.setDescrizioneUfficio(ufficio.getName());
		ass.setUtenteId(utente.getValueObject().getId());
		UtenteVO ute = utente.getValueObject();
		ass.setNomeUtente(ute.getFullName());
		PostaInternaForm pForm = (PostaInternaForm) form;
		pForm.setMittente(ass);
		if (form.isDipTitolarioUfficio())
			form.setTitolario(null);

	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		super.setType(CaricaProtocollo.POSTA_INTERNA);
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);

		PostaInternaForm pForm = (PostaInternaForm) form;
		getInputPage(pForm, session);
		if (request.getParameter("impostaOggettoAction") != null) {
			if (pForm.getOggettoFromOggettario() != null) {
				pForm.setOggettoGenerico(pForm.getOggetto());
				pForm.setGiorniAlert(pForm.getOggettoFromOggettario()
						.getGiorniAlert());
				pForm.setOggettoSelezionato(1);
				List<Integer> uffIds = ProtocolloBO
						.getUfficiAssegnatariOggetto(pForm);
				if (uffIds != null)
					for (int uffId : uffIds)
						assegnaAdUfficio(pForm, uffId, utente);
			} else {
				pForm.setOggettoSelezionato(0);
				pForm.setGiorniAlert(0);
			}
		}
		updateDestinatariCompetenti(pForm);
		if (pForm.getUtenti() == null) {
			Organizzazione org = Organizzazione.getInstance();
			Collection<Ufficio> totUff = org.getUffici();
			for (Ufficio u : totUff)
				if (u.getValueObject().getParentId() == 0) {
					pForm.setUfficioCorrente(u.getValueObject());
					Collection<Ufficio> child = u.getUfficiDipendenti();
					Collection<UfficioVO> trf = new ArrayList<UfficioVO>();
					for (Ufficio c : child) {
						trf.add(c.getValueObject());
					}
					pForm.setUfficiDipendenti(trf);

				}
		}
		
		RegistroVO registro = RegistroDelegate.getInstance().getRegistroById(
				utente.getRegistroPostaInterna());

		if (pForm.getMittente() == null)
			assegnaMittente(pForm, utente);
		session.setAttribute("protocolloForm", pForm);
		
		if (!registro.getApertoIngresso()) {
			errors.add("apertoIngresso", new ActionMessage("registro_chiuso"));
			saveErrors(request, errors);
		}
		if (pForm.getTitolario() == null) {
			AggiornaPostaInternaForm.impostaTitolario(pForm, utente, 0);
		}

		ActionForward actionForward = super.execute(mapping, form, request,
				response);
		if (actionForward != null && "none".equals(actionForward.getName())) {
			return null;
		} else if (actionForward != null) {
			return actionForward;
		}

		if (request.getParameter("downloadAllegatoId") != null) {
			int docId = NumberUtil.getInt((String) request
					.getParameter("downloadAllegatoId"));
			int aooId = utente.getAreaOrganizzativa().getId();
			DocumentoVO doc = pForm.getDocumentoAllegato(String.valueOf(docId));
			if(ProtocolloFileUtility.isDocumentoReadable(pForm.getProtocolloId(), pForm.getFlagTipo(), pForm.getUfficioProtocollatoreId(),pForm.getMittente().getUfficioId(),pForm.getFascicoliProtocolloOld(), utente))
				return downloadDocumento(mapping, doc, request, response, aooId);
			else{
				errors.add("documento",new ActionMessage("download_doc_non_abilitato","errors"));
				saveErrors(request, errors);
			}
			return mapping.findForward("input");
		} else if (request.getParameter("downloadDocumentoPrincipale") != null) {
			DocumentoVO doc = pForm.getDocumentoPrincipale();
			int aooId = utente.getAreaOrganizzativa().getId();
			if(ProtocolloFileUtility.isDocumentoReadable(pForm.getProtocolloId(), pForm.getFlagTipo(), pForm.getUfficioProtocollatoreId(),pForm.getMittente().getUfficioId(),pForm.getFascicoliProtocolloOld(), utente))
				return downloadDocumento(mapping, doc, request, response, aooId);
			else{
				errors.add("documento",new ActionMessage("download_doc_non_abilitato","errors"));
				saveErrors(request, errors);
			}
			return mapping.findForward("input");

		} 
		if (request.getParameter("listaDistribuzione") != null) {
			request.setAttribute("nomeLista", "");
			session.setAttribute("tornaProtocollo", Boolean.TRUE);
			session.setAttribute("provenienza", "listaDistribuzioneProtocollo");
			return mapping.findForward("cercaListaDistribuzione");
		} else if (request.getParameter("rimuoviAssegnatariAction") != null)
			rimuoviDestinatari(pForm);
		else if (request.getParameter("impostaTitolarioAction") != null) {
			if (pForm.getTitolario() != null) {
				pForm.setTitolarioPrecedenteId(pForm.getTitolario().getId()
						.intValue());
			}
			Utente ute = (Utente) session.getAttribute(Constants.UTENTE_KEY);
			AggiornaPostaInternaForm.impostaTitolario(pForm, ute,
					pForm.getTitolarioSelezionatoId());
			return mapping.findForward("input");
		} else if (request.getParameter("btnRipetiDatiI") != null) {
			for(AssegnatarioView view:pForm.getDestinatari()){
				view.setLavorato(false);
				view.setPresaVisione(false);
			}
			pForm.inizializzaFormToCopyProtocollo();
			saveToken(request);
			return mapping.findForward("nuovoProtocolloRipeti");
		} else if (request.getParameter("titolarioPrecedenteAction") != null) {
			Utente ute = (Utente) session.getAttribute(Constants.UTENTE_KEY);
			AggiornaPostaInternaForm.impostaTitolario(pForm, ute,
					pForm.getTitolarioPrecedenteId());
			if (pForm.getTitolario() != null) {
				pForm.setTitolarioPrecedenteId(pForm.getTitolario()
						.getParentId());
			}
			return mapping.findForward("input");
		} else if (request.getParameter("parCaricaListaId") != null) {
			int listaId = NumberUtil.getInt(request
					.getParameter("parCaricaListaId"));
			Collection<AssegnatarioVO> lista = SoggettoDelegate.getInstance()
					.getAssegnatariListaDistribuzione(listaId);
			AggiornaPostaInternaForm.aggiungiDestinatariListaDistribuzioneForm(
					lista, pForm, utente);
			session.removeAttribute("tornaProtocollo");
			updateDestinatariCompetenti(pForm);
		} else if (request.getParameter("salvaAction") != null
				&& errors.isEmpty()) {
			return saveProtocollo(mapping, request, errors, session, pForm,
					registro);
		} else if (request.getParameter("fascicolazioneMultiplaAction") != null
				&& errors.isEmpty()) {

			if (pForm.getFascicoliProtocollo().size() == 0)
				errors.add("fascicolo", new ActionMessage(
						"fascicolo_obbligatorio"));
			else if (session.getAttribute("protocolliIds") != null) {
				String[] ids = (String[]) session.getAttribute("protocolliIds");
				fascicolazioneMultiplaPostaInterna(request, ids, errors,
						session, pForm);
				session.removeAttribute("protocolliIds");
			} else
				errors.add("fascicolazioneMultiplaErrore", new ActionMessage(
						"protocolloIds_vuoto"));
			if (errors.isEmpty()) {
				messages.add("fascicolazioneMultiplaOK", new ActionMessage(
						"protocolli_fascicolati"));
				saveMessages(request, messages);
			}
			saveErrors(request, errors);
			saveToken(request);
			return mapping.findForward("input");

		} else if (request.getParameter("indietroAction") != null) {
			return getMappingForward(pForm.getReturnPage(), mapping);
		} else if (request.getParameter("btnPresaVisione") != null) {
			return presaVisione(mapping, request,errors, utente, pForm);
		} else if (request.getParameter("assegnaUfficioMittenteAction") != null) {
			assegnaUfficioMittente(pForm, pForm.getUfficioSelezionatoId());
			return mapping.findForward("input");

		} else if (request.getParameter("assegnaUtenteMittenteAction") != null) {
			assegnaUtenteMittente(pForm);
			return mapping.findForward("input");

		} else if (request.getParameter("cercaProcedimenti") != null) {
			session.setAttribute("tornaPostaInterna", Boolean.TRUE);
			pForm.setOggettoProcedimento("");
			return mapping.findForward("cercaProcedimenti");
		} else if (request.getParameter("btnAggiungiProcedimenti") != null) {
			if(session.getAttribute("PresaInCarico")!=null){
				pForm.setDaScarico(true);
				session.removeAttribute("PresaInCarico");
			}
			return mapping.findForward("aggiungiProcedimento");
		} else if (request.getParameter("postaDaFascicolare") != null) {
			List<Sezione> elencoSezioni = pForm.getElencoSezioni();
			Iterator<Sezione> iterator = elencoSezioni.iterator();
			while (iterator.hasNext()) {
				String sez = "" + ( iterator.next()).getNome();
				if ((sez).startsWith("Fascicoli"))
					pForm.setSezioneVisualizzata(sez);
			}
		}
		return mapping.findForward("input");
	}

	private void fascicolazioneMultiplaPostaInterna(HttpServletRequest request,
			String[] ids, ActionMessages errors, HttpSession session,
			PostaInternaForm pForm) {
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		PostaInterna postaInterna = null;
		ProtocolloVO protocollo = null;
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		for (String protId : ids) {
			postaInterna = delegate.getPostaInternaById(Integer
					.parseInt(protId));
			postaInterna.getProtocollo().setStatoProtocollo("A");
			postaInterna.setLavorato(pForm.isLavorato());
			postaInterna.setFascicoli(
					pForm.getFascicoliProtocollo());
			postaInterna.setFascicoliEliminatiId(pForm.getFascicoliEliminatiId());
			protocollo = delegate.aggiornaPostaInterna(postaInterna, utente);
			if (protocollo.getReturnValue() != ReturnValues.SAVED) {
				errors.add("fascicolazioneMultipla", new ActionMessage(
						"operazione_fallita"));
			}
		}
	}

	private ActionForward presaVisione(ActionMapping mapping,
			HttpServletRequest request, ActionMessages errors,
			Utente utente, PostaInternaForm pForm) {
		ProtocolloDelegate delegate=ProtocolloDelegate.getInstance();
		PostaInterna postaInterna = null;
		ProtocolloVO protocollo = null;
		postaInterna = delegate.getPostaInternaById(pForm.getProtocolloId());
		postaInterna.getProtocollo().setStatoProtocollo("V");
		postaInterna.getFascicoli().clear();
		postaInterna.setVisionato(true);
		postaInterna.setLavorato(true);
		protocollo = delegate.aggiornaPostaInterna(postaInterna, utente);
		if (protocollo.getReturnValue() == ReturnValues.SAVED) {
			delegate.registraCheckPostaInterna(protocollo, utente, pForm.isVisionato());
			return getMappingForward(pForm.getReturnPage(), mapping);
		}else{
			errors.add("presaVisione", new ActionMessage("operazione_fallita"));
			saveErrors(request, errors);
			saveToken(request);
			return mapping.findForward("input");
		}
	}

	private ActionForward saveProtocollo(ActionMapping mapping,
			HttpServletRequest request, ActionMessages errors,
			HttpSession session, PostaInternaForm pForm, RegistroVO registro) {
		Utente ute = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		if (pForm.getDestinatari().size() > 0) {
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				saveToken(request);
				return mapping.findForward("input");
			}
		}
		PostaInterna postaInterna = null;
		ProtocolloVO protocollo = null;
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		if (pForm.getProtocolloId() == 0) {
			postaInterna = ProtocolloBO.getDefaultPostaInterna(ute);
			AggiornaPostaInternaModel.aggiorna(pForm, postaInterna, ute);
			protocollo = delegate.registraProtocolloPostaInterna(postaInterna,
					ute, registro);
		} else {
			postaInterna = (PostaInterna) session.getAttribute("postaInterna");
			AggiornaPostaInternaModel.aggiorna(pForm, postaInterna, ute);
			protocollo = delegate.aggiornaPostaInterna(postaInterna, ute);

		}
		if (protocollo == null) {
			errors.add("errore_nel_salvataggio", new ActionMessage(
					"errore_nel_salvataggio", "", ""));
			saveErrors(request, errors);
		} else if (protocollo.getReturnValue() == ReturnValues.SAVED) {
			request.setAttribute("protocolloId", protocollo.getId());
			caricaProtocollo(request, pForm, CaricaProtocollo.POSTA_INTERNA);

			if (session.getAttribute("PROTOCOLLI_EMERGENZA") != null) {
				pForm.setNumeroProtocolliRegistroEmergenza(((Integer) session
						.getAttribute("PROTOCOLLI_EMERGENZA")).intValue());
				return mapping.findForward("visualizzaProtocollo");
			}
			if (request.getParameter("salvaAction").equals("Fascicola")) {
				delegate.registraCheckPostaInterna(protocollo, ute, pForm.isVisionato());
				return getMappingForward(pForm.getReturnPage(), mapping);
			} else if (request.getParameter("salvaAction").equals("Salva")
					|| request.getParameter("salvaAction").equals(
							"Aggiungi il Procedimento"))
				return getMappingForward(pForm.getReturnPage(), mapping);
			else
				return mapping.findForward("allegaDocProtocollo");
		} else if (protocollo.getReturnValue() == ReturnValues.OLD_VERSION) {
			errors.add("generale", new ActionMessage("versione_vecchia"));
			saveErrors(request, errors);
			request.setAttribute("protocolloId", protocollo.getId());
		} else {
			errors.add("generale", new ActionMessage("errore_nel_salvataggio"));
			saveErrors(request, errors);
		}
		resetToken(request);
		pForm.setOggettoProcedimento("");
		return mapping.findForward("input");
	}

	private ActionForward getMappingForward(String returnPage, ActionMapping mapping) {
		if (returnPage != null) {
			if (returnPage.equals("scarico_ufficio"))
				return mapping.findForward("tornaUfficio");
			else if (returnPage.equals("scarico_utente"))
				return mapping.findForward("tornaUtente");
			else if (returnPage.equals("fascicolo"))
				return mapping.findForward("fascicolo");
			else if (returnPage.equals("scaricoProcedimento"))
				return mapping.findForward("scarico_procedimento");
		}
		return mapping.findForward("visualizzaProtocollo");
	}
}
