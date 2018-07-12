package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.ProtocolloUscita;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.business.ConfigurazioneUtenteDelegate;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.business.SoggettoDelegate;
import it.finsiel.siged.mvc.business.TitoliDestinatarioDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloUscitaForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.presentation.helper.DestinatarioView;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.ConfigurazioneUtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;
import it.finsiel.siged.util.NumberUtil;
import it.flosslab.mvc.presentation.action.protocollo.helper.CaricaProtocollo;
import it.flosslab.mvc.presentation.action.protocollo.helper.file.ProtocolloFileUtility;
import it.flosslab.mvc.presentation.action.protocollo.helper.form.AggiornaProtocolloUscitaForm;
import it.flosslab.mvc.presentation.action.protocollo.helper.model.AggiornaProtocolloUscitaModel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ProtocolloUscitaAction extends ProtocolloAction {
	// ----------------------------------------------------- Instance Variables

	static Logger logger = Logger.getLogger(ProtocolloUscitaAction.class
			.getName());

	protected void resetForm(ProtocolloForm form, Utente utente,HttpServletRequest request) {
		form.inizializzaForm();
		HttpSession session=request.getSession();
		session.removeAttribute("tornaDocumento");
		form.setAooId(utente.getAreaOrganizzativa().getId());
		form.setTipoDocumentoId(0);
		form.setVersioneDefault(true);
	}
	
	
	protected void assegnaAdUfficio(ProtocolloForm form, int ufficioId,Utente utente) {
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(ufficioId);
		Organizzazione org = Organizzazione.getInstance();
		Ufficio uff = org.getUfficio(ufficioId);
		ass.setNomeUfficio(uff.getValueObject().getDescription());
		ass.setDescrizioneUfficio(uff.getPath());
		((ProtocolloUscitaForm) form).setMittente(ass);
		form.setTitolario(null);
	}

	
	protected void assegnaAdUtente(ProtocolloForm form,Utente utente) {
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(form.getUfficioCorrenteId());
		ass.setNomeUfficio(form.getUfficioCorrente().getDescription());
		ass.setDescrizioneUfficio(form.getUfficioCorrentePath());
		ass.setUtenteId(form.getUtenteSelezionatoId());
		UtenteVO ute = form.getUtente(form.getUtenteSelezionatoId());
		ass.setNomeUtente(ute.getFullName());
		((ProtocolloUscitaForm) form).setMittente(ass);
		form.setTitolario(null);
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		super.setType(CaricaProtocollo.PROTOCOLLO_USCITA);
		HttpSession session = request.getSession(true);
		ProtocolloUscitaForm pForm = (ProtocolloUscitaForm) form;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);

		if (pForm.getOggettoFromOggettario() != null) {
			pForm.setOggettoSelezionato(0);
		}
		RegistroVO registro = (RegistroVO) utente.getRegistroVOInUso();
		if (request.getParameter("impostaOggettoAction") != null) {
			if (pForm.getOggettoFromOggettario() != null) {
				pForm.setOggettoGenerico(pForm.getOggetto());
				pForm.setOggettoSelezionato(0);
				pForm.setGiorniAlert(pForm.getOggettoFromOggettario()
						.getGiorniAlert());
			} else {
				pForm.setGiorniAlert(0);
				pForm.setOggettoSelezionato(0);
			}
		}
		if (!registro.getApertoUscita()) {
			errors.add("apertoUscita", new ActionMessage("registro_chiuso"));
			saveErrors(request, errors);
		}

		if (request.getParameter("annullaAction") != null
				&& request.getParameter("btnCopiaProtocollo") == null) {
			Organizzazione org = Organizzazione.getInstance();
			Ufficio uff = org.getUfficio(utente.getUfficioInUso());
			pForm.setMittente(AggiornaProtocolloUscitaForm.newMittente(uff,
					null));
		}
		session.setAttribute("protocolloForm", pForm);
		if (pForm.getTitolario() == null) {
			AggiornaProtocolloUscitaForm.impostaTitolario(pForm, utente, 0);
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
			if(ProtocolloFileUtility.isDocumentoReadable(pForm.getProtocolloId(), pForm.getFlagTipo(), pForm.getUfficioProtocollatoreId(),pForm.getMittente().getUfficioId(), pForm.getFascicoliProtocolloOld(),utente))
				return downloadDocumento(mapping, doc, request, response, aooId);
			else{
				errors.add("documento",new ActionMessage("download_doc_non_abilitato","errors"));
				saveErrors(request, errors);
			}
			return mapping.findForward("input");

		} 
		if (request.getParameter("aggiungiDestinatario") != null) {
			if (isDestinatarioNull(pForm)) {
				errors.add("inserisciDestinatario", new ActionMessage(
						"destinatario_vuoto"));
			}  else if (invioPECtoNullMail(pForm)){
				errors.add("inserisciDestinatario", new ActionMessage(
						"mail_destinatario_vuota"));
			}
			else {
				aggiungiDestinatario(pForm, session);
			}

		} else if (request.getParameter("rimuoviDestinatari") != null) {
			String[] destinatari = pForm.getDestinatariSelezionatiId();
			if (destinatari != null) {
				for (int i = 0; i < destinatari.length; i++) {
					pForm.rimuoviDestinatario(destinatari[i]);
					destinatari[i] = null;
				}
			} else {
				errors.add("rimuoviDestinatari", new ActionMessage(
						"destinatario_rimuovi"));
				saveErrors(request, errors);
			}
			pForm.inizializzaDestinatarioForm();
		} else if (request.getParameter("listaDistribuzione") != null) {

			request
					.setAttribute("nomeLista", pForm
							.getNominativoDestinatario());
			session.setAttribute("tornaProtocollo", Boolean.TRUE);
			session.setAttribute("provenienza", "listaDistribuzioneProtocollo");
			return mapping.findForward("cercaListaDistribuzione");
		} else if (request.getParameter("cercaDestinatari") != null) {

			session.setAttribute("tornaProtocollo", Boolean.TRUE);
			if ("F".equals(pForm.getTipoDestinatario())) {
				request.setAttribute("cognome", "");
				request.setAttribute("nome", "");
				session.setAttribute("provenienza", "personaFisicaProtocollo");
				pForm.setCognomeMittente("");
				return mapping.findForward("cercaPersonaFisica");
			} else if ("G".equals(pForm.getTipoDestinatario())) {
				request.setAttribute("descrizioneDitta", pForm
						.getNominativoDestinatario());
				session.setAttribute("provenienza",
						"personaGiuridicaProtocollo");
				pForm.setNominativoDestinatario("");
				return mapping.findForward("cercaPersonaGiuridica");
			} else {
				request.setAttribute("codice", pForm
						.getNominativoDestinatario());
				return mapping.findForward("cercaAoo");
			}
		} else if (request.getParameter("impostaTitolarioAction") != null) {
			if (pForm.getTitolario() != null) {
				pForm.setTitolarioPrecedenteId(pForm.getTitolario().getId()
						.intValue());
			}
			Utente ute = (Utente) session.getAttribute(Constants.UTENTE_KEY);
			AggiornaProtocolloUscitaForm.impostaTitolario(pForm, ute, pForm
					.getTitolarioSelezionatoId());
			return mapping.findForward("input");

		} else if (request.getParameter("titolarioPrecedenteAction") != null) {
			Utente ute = (Utente) session.getAttribute(Constants.UTENTE_KEY);
			AggiornaProtocolloUscitaForm.impostaTitolario(pForm, ute, pForm
					.getTitolarioPrecedenteId());
			if (pForm.getTitolario() != null) {
				pForm.setTitolarioPrecedenteId(pForm.getTitolario()
						.getParentId());
			}
			return mapping.findForward("input");

		} else if (request.getParameter("btnStampaEtichette") != null) {
			Collection<DestinatarioView> destinatari = pForm.getDestinatari();
			int protocolloId = pForm.getProtocolloId();
			logger.info("Ci sono " + destinatari.size() + " destinatari");
			session.setAttribute("destinatari", destinatari);
			session.setAttribute("protocolloId", "" + protocolloId);
			return mapping.findForward("stampaEtichetteDestinatari");

		} else if (request.getParameter("parCaricaListaId") != null) {
			int listaId = NumberUtil.getInt(request
					.getParameter("parCaricaListaId"));
			ArrayList<SoggettoVO> lista = SoggettoDelegate.getInstance()
					.getDestinatariListaDistribuzione(listaId);
			AggiornaProtocolloUscitaForm
					.aggiungiDestinatariListaDistribuzioneForm(lista, pForm);
			session.removeAttribute("tornaProtocollo");
		} else if (request.getParameter("salvaAction") != null
				&& errors.isEmpty()) {
			
			Utente ute = (Utente) session.getAttribute(Constants.UTENTE_KEY);
			ProtocolloUscita protocolloUscita = null;
			ProtocolloVO protocollo = null;
			ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
			if (pForm.getProtocolloId() == 0) {
				protocolloUscita = ProtocolloBO.getDefaultProtocolloUscita(ute);
				AggiornaProtocolloUscitaModel.aggiorna(pForm, protocolloUscita,
						ute);
				protocollo = delegate.registraProtocollo(protocolloUscita, ute,
						true);
				pForm.setInoltrabile(true);
			} else {
				protocolloUscita = (ProtocolloUscita) session
						.getAttribute("protocolloUscita");
				if (protocolloUscita.getProtocollo().getFlagTipo().equals("R")) {
					protocolloUscita.getProtocollo().setFlagTipo("U");
					protocolloUscita.getProtocollo().setDataRegistrazione(
							new Timestamp(System.currentTimeMillis()));

				}
				AggiornaProtocolloUscitaModel.aggiorna(pForm, protocolloUscita,
						ute);
				protocollo = delegate.aggiornaProtocolloUscita(
						protocolloUscita, ute);
				
			}
			
			if (protocollo == null) {
				errors.add("errore_nel_salvataggio", new ActionMessage(
						"errore_nel_salvataggio", "", ""));
				saveErrors(request, errors);
			} else if (protocollo.getReturnValue() == ReturnValues.SAVED) {
				request.setAttribute("protocolloId", protocollo.getId());
				caricaProtocollo(request, pForm,CaricaProtocollo.PROTOCOLLO_USCITA);
				if (session.getAttribute("PROTOCOLLI_EMERGENZA") != null) {
					pForm.setNumeroProtocolliRegistroEmergenza(((Integer) session.getAttribute("PROTOCOLLI_EMERGENZA")).intValue());
				}
				pForm.setEstremiAutorizzazione(null);
				if (request.getParameter("salvaAction").equals("Fascicola")) {
					return mapping.findForward("fascicolo");
				}
				else if (request.getParameter("salvaAction").equals("Salva"))
					return mapping.findForward("visualizzaProtocollo");
				else
					return mapping.findForward("allegaDocProtocollo");
			} else if (protocollo.getReturnValue() == ReturnValues.OLD_VERSION) {
				pForm.setInoltrabile(false);
				errors.add("generale", new ActionMessage("versione_vecchia"));
				saveErrors(request, errors);
				request.setAttribute("protocolloId", protocollo.getId());
			} else {
				pForm.setInoltrabile(false);
				errors.add("generale", new ActionMessage(
						"errore_nel_salvataggio"));
				saveErrors(request, errors);
			}
			resetToken(request);

		} else if (request.getParameter("btnRipetiDatiU") != null) {
			int caricaId = utente.getCaricaInUso();
			ConfigurazioneUtenteVO configurazioneVO = null;
			if (session.getAttribute("CONFIGURAZIONE_UTENTE_PROTOCOLLO") != null) {
				configurazioneVO = (ConfigurazioneUtenteVO) session
						.getAttribute("CONFIGURAZIONE_UTENTE_PROTOCOLLO");
			} else {
				configurazioneVO = ConfigurazioneUtenteDelegate.getInstance()
						.getConfigurazione(caricaId);
				session.setAttribute("CONFIGURAZIONE_UTENTE_PROTOCOLLO",
						configurazioneVO);
			}
			pForm.inizializzaFormToCopyProtocollo();
			saveToken(request);
			return mapping.findForward("nuovoProtocolloRipeti");
		}
		else if (request.getParameter("btnInviaInterna") != null) {
			request.setAttribute("protocolloId", pForm.getProtocolloId());
			//saveToken(request);
			request.setAttribute("inoltraUscita", true);
			return mapping.findForward("inoltraInterna");

			
		} else if (request.getParameter("destinatarioId") != null
				&& !request.getParameter("destinatarioId").equals("")) {
			String destinatarioId = (String) request
					.getParameter("destinatarioId");
			AggiornaProtocolloUscitaForm.aggiornaDestinatarioForm(
					destinatarioId, pForm);

		} else if (request.getParameter("cercaProcedimenti") != null) {
			session.setAttribute("tornaProtocolloUscita", Boolean.TRUE);
			return mapping.findForward("cercaProcedimenti");
		}
		pForm.setOggettoProcedimento("");
		if (!errors.isEmpty()) 
			saveErrors(request, errors);
		return (mapping.findForward("input"));
	}

	private void aggiungiDestinatario(ProtocolloUscitaForm form,
			HttpSession session) {
		DestinatarioView destinatario = new DestinatarioView();
		destinatario.setFlagTipoDestinatario(form.getTipoDestinatario());
		if ("F".equals(form.getTipoDestinatario())) {
			destinatario.setNome(form.getNomeDestinatario());
			destinatario.setCognome(form.getCognomeDestinatario());
		} else {
			destinatario.setDestinatario(form.getNominativoDestinatario()
					.trim());
		}
		destinatario.setEmail(form.getEmailDestinatario());
		destinatario.setCapDestinatario(form.getCapDestinatario());
		destinatario.setCitta(form.getCitta());
		destinatario.setIndirizzo(form.getIndirizzoDestinatario());
		destinatario.setDataSpedizione(form.getDataSpedizione());
		destinatario.setMezzoSpedizioneId(form.getMezzoSpedizioneId());

		destinatario.setIdx(form.getIdx());
		String desc = LookupDelegate.getInstance().getDescMezzoSpedizione(
				form.getMezzoSpedizioneId()).getDescrizioneSpedizione();
		destinatario.setMezzoDesc(desc);
		destinatario.setPrezzoSpedizione(LookupDelegate.getInstance().getDescMezzoSpedizione(
				form.getMezzoSpedizioneId()).getPrezzo());
		destinatario.setTitoloDestinatario("--");
		destinatario.setTitoloId(form.getTitoloId());
		if (form.getTitoloId() > 0) {
			destinatario.setTitoloDestinatario(TitoliDestinatarioDelegate
					.getInstance().getTitoloDestinatario(form.getTitoloId())
					.getDescription());
		}
		destinatario.setNote(form.getNoteDestinatario());
		destinatario.setFlagConoscenza(form.getFlagConoscenza());
		destinatario.setFlagPresso(form.getFlagPresso());
		destinatario.setFlagPEC(form.getFlagPEC());
		form.aggiungiDestinatario(destinatario);
		form.inizializzaDestinatarioForm();
	}

	private boolean isDestinatarioNull(ProtocolloUscitaForm form) {
		boolean isNull = false;
		if (form.getTipoDestinatario().equals("G")) {
			if (form.getNominativoDestinatario() == null
					|| form.getNominativoDestinatario().trim().equals(""))
				isNull = true;
		} else if (form.getTipoDestinatario().equals("F")) {
			if (form.getCognomeDestinatario() == null
					|| form.getCognomeDestinatario().trim().equals(""))
				isNull = true;
		}
		return isNull;
	}
	
	private boolean invioPECtoNullMail(ProtocolloUscitaForm form) {
		boolean isNull = false;
		if (form.getFlagPEC()) {
			if (form.getEmailDestinatario() == null
					|| form.getEmailDestinatario().trim().equals(""))
				isNull = true;
		} 
		return isNull;
	}

}