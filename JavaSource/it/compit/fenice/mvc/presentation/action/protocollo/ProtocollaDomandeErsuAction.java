package it.compit.fenice.mvc.presentation.action.protocollo;

import it.compit.fenice.mvc.business.DomandaDelegate;
import it.compit.fenice.mvc.vo.protocollo.DomandaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.ProtocolloIngresso;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.action.protocollo.ProtocolloAction;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloIngressoForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.util.NumberUtil;
import it.flosslab.mvc.presentation.action.protocollo.helper.CaricaProtocollo;
import it.flosslab.mvc.presentation.action.protocollo.helper.file.ProtocolloFileUtility;
import it.flosslab.mvc.presentation.action.protocollo.helper.model.AggiornaProtocolloIngressoModel;

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

public class ProtocollaDomandeErsuAction extends ProtocolloAction {

	static Logger logger = Logger.getLogger(ProtocollaDomandeErsuAction.class.getName());

	
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
		ProtocolloIngressoForm pForm = (ProtocolloIngressoForm) form;
		ass.setStato('S');
		ass.setCaricaAssegnanteId(utente.getCaricaInUso());
		ass.setUfficioAssegnanteId(utente.getUfficioInUso());
		pForm.aggiungiAssegnatario(ass);
		if (pForm.getAssegnatarioCompetente() == null) {
			pForm.setAssegnatarioCompetente(ass.getKey());
		}
		form.setUfficioSelezionatoId(0);
		if (form.isDipTitolarioUfficio()) {
			form.setTitolario(null);
		}
		ass.setPresaVisione(false);
	}

	protected void assegnaAdUtente(ProtocolloForm form,Utente utente) {
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(form.getUfficioCorrenteId());
		ass.setNomeUfficio(form.getUfficioCorrente().getDescription());
		ass.setDescrizioneUfficio(form.getUfficioCorrentePath());
		ass.setUtenteId(form.getUtenteSelezionatoId());
		UtenteVO ute = form.getUtente(form.getUtenteSelezionatoId());
		ass.setNomeUtente(ute.getCaricaFullName());
		ProtocolloIngressoForm pForm = (ProtocolloIngressoForm) form;
		ass.setStato('S');
		ass.setCaricaAssegnanteId(utente.getCaricaInUso());
		ass.setUfficioAssegnanteId(utente.getUfficioInUso());
		pForm.aggiungiAssegnatario(ass);
		if (pForm.getAssegnatarioCompetente() == null)
			pForm.setAssegnatarioCompetente(ass.getKey());
		if (form.isDipTitolarioUfficio())
			form.setTitolario(null);
		ass.setPresaVisione(false);
	}
	
	private void rimuoviAssegnatari(ProtocolloIngressoForm form) {
		String[] assegnatari = form.getAssegnatariSelezionatiId();
		String assegnatarioCompetente = form.getAssegnatarioCompetente();
		if (assegnatari != null) {
			for (int i = 0; i < assegnatari.length; i++) {
				String assegnatario = assegnatari[i];
				if (assegnatario != null) {
					form.rimuoviAssegnatario(assegnatario);
					if (assegnatario.equals(assegnatarioCompetente)) {
						form.setAssegnatarioCompetente(null);
					}
				}
			}
			if (form.getAssegnatarioCompetente() == null) {
				Iterator<AssegnatarioView> i = form.getAssegnatari().iterator();
				if (i.hasNext()) {
					AssegnatarioView ass = i.next();
					form.setAssegnatarioCompetente(ass.getKey());
				}
			}
		}
	}
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors = new ActionMessages();
		super.setType(CaricaProtocollo.DOMANDE_ERSU);
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		ProtocolloIngressoForm pForm = (ProtocolloIngressoForm) form;
		session.setAttribute("protocolloForm", pForm);
		ActionForward actionForward = super.execute(mapping, form, request,
				response);
		if (actionForward != null && "none".equals(actionForward.getName())) {
			return null;
		} else if (actionForward != null) {
			return actionForward;
		}
		if (request.getParameter("impostaOggettoAction") != null) {
			if (pForm.getOggettoFromOggettario() != null) {
				pForm.setOggettoGenerico(pForm.getOggetto());
				pForm.setOggettoSelezionato(1);
				pForm.setGiorniAlert(pForm.getOggettoFromOggettario().getGiorniAlert());
				List<Integer> uffIds=ProtocolloBO.getUfficiAssegnatariOggetto(pForm);
				if(uffIds!=null)
					for(int uffId:uffIds)
						assegnaAdUfficio(pForm, uffId, utente);
			} else {
				pForm.setGiorniAlert(0);
				pForm.setOggettoSelezionato(0);
			}
		}
		
		if (request.getParameter("downloadAllegatoId") != null) {
			int docId = NumberUtil.getInt((String) request
					.getParameter("downloadAllegatoId"));
			int aooId = utente.getAreaOrganizzativa().getId();
			DocumentoVO doc = pForm.getDocumentoAllegato(String.valueOf(docId));
			if(ProtocolloFileUtility.isDocumentoReadable(pForm.getProtocolloId(), pForm.getFlagTipo(), pForm.getUfficioProtocollatoreId(),0, pForm.getFascicoliProtocolloOld(),utente))
				return downloadDocumento(mapping, doc, request, response, aooId);
			else{
				errors.add("documento",new ActionMessage("download_doc_non_abilitato","errors"));
				saveErrors(request, errors);
			}
			return mapping.findForward("input");
		} if (request.getParameter("downloadDocumentoPrincipale") != null) {
			DocumentoVO doc = pForm.getDocumentoPrincipale();
			int aooId = utente.getAreaOrganizzativa().getId();
			if(ProtocolloFileUtility.isDocumentoReadable(pForm.getProtocolloId(), pForm.getFlagTipo(), pForm.getUfficioProtocollatoreId(),0, pForm.getFascicoliProtocolloOld(),utente))
				return downloadDocumento(mapping, doc, request, response, aooId);
			else{
				errors.add("documento",new ActionMessage("download_doc_non_abilitato","errors"));
				saveErrors(request, errors);
			}
			return mapping.findForward("input");

		}
		if (request.getParameter("indietroAction") != null) {
			return mapping.findForward("indietro");
		} else if (request.getParameter("salvaAction") != null
				&& errors.isEmpty()) {
			return saveProtocollo(mapping, request, errors, session, pForm);
		} else if (request.getParameter("rimuoviAssegnatariAction") != null) {
			rimuoviAssegnatari(pForm);
		}
		return mapping.findForward("input");
	}

	
	private ActionForward saveProtocollo(ActionMapping mapping,
			HttpServletRequest request, ActionMessages errors,
			HttpSession session, ProtocolloIngressoForm pForm) {
		Utente ute = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		ProtocolloIngresso protocolloIngresso = null;
		ProtocolloVO protocollo = null;
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		if (pForm.getProtocolloId() == 0) {
			protocolloIngresso = ProtocolloBO.getDefaultProtocolloIngresso(ute);
			AggiornaProtocolloIngressoModel.aggiorna(pForm, protocolloIngresso,
					ute);
			DomandaVO domanda=DomandaDelegate.getInstance().getDomandaById(protocolloIngresso.getProtocollo().getNumProtocolloMittente());
			protocollo = delegate.registraProtocolloIngressoDaDomandaErsu(protocolloIngresso, ute,domanda);
		} 
		if (protocollo == null) {
			errors.add("errore_nel_salvataggio", new ActionMessage(
					"errore_nel_salvataggio", "", ""));
			saveErrors(request, errors);
		} else if (protocollo.getReturnValue() == ReturnValues.SAVED) {
			request.setAttribute("protocolloId", protocollo.getId());
			caricaProtocollo(request, pForm,CaricaProtocollo.DOMANDE_ERSU);
			pForm.setEstremiAutorizzazione(null);
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

}
