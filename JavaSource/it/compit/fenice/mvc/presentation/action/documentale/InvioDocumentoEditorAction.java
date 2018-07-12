package it.compit.fenice.mvc.presentation.action.documentale;

import it.compit.fenice.mvc.business.EditorDelegate;
import it.compit.fenice.mvc.presentation.actionform.documentale.EditorForm;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.InvioClassificati;
import it.finsiel.siged.model.documentale.Documento;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.business.TitoliDestinatarioDelegate;
import it.finsiel.siged.mvc.presentation.actionform.documentale.DestinatariInvioForm;
import it.finsiel.siged.mvc.presentation.helper.DestinatarioView;
import it.finsiel.siged.mvc.vo.documentale.FileVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.InvioClassificatiVO;
import it.finsiel.siged.mvc.vo.protocollo.SpedizioneVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.FileUtil;
import it.finsiel.siged.util.StringUtil;

import java.util.Collection;
import java.util.Iterator;

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

public class InvioDocumentoEditorAction extends Action {

	static Logger logger = Logger.getLogger(InvioDocumentoEditorAction.class
			.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages messages = new ActionMessages();
		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession();
		EditorForm dForm = (EditorForm) form;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		dForm.setAooId(utente.getAreaOrganizzativa().getId());
		session.setAttribute("editorForm", dForm);
		if (form == null) {
			logger.info(" Creating new InvioDocumentoAction");
			form = new DestinatariInvioForm();
			request.setAttribute(mapping.getAttribute(), form);
		}
		if (request.getParameter("aggiungiDestinatario") != null) {
			
			errors = dForm.validateDestinatario(mapping, request);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			} else {
				aggiungiDestinatario(dForm, session,utente.getAreaOrganizzativa().getId());
			}

		} else if (request.getParameter("rimuoviDestinatari") != null) {
			String[] destinatari = dForm.getDestinatarioSelezionatoId();
			if (destinatari != null) {
				for (int i = 0; i < destinatari.length; i++) {
					dForm.rimuoviDestinatario(destinatari[i]);
					destinatari[i] = null;
				}
			} else {
				errors.add("rimuoviDestinatari", new ActionMessage(
						"destinatario_rimuovi"));
				saveErrors(request, errors);
			}
		} else if (request.getParameter("cercaDestinatari") != null) {
			session.setAttribute("tornaInvioDocumentoEditor", Boolean.TRUE);
			if ("F".equals(dForm.getTipoDestinatario())) {
				request.setAttribute("cognome", dForm
						.getNominativoDestinatario());
				session.setAttribute("provenienza", "personaFisicaEditor");
				return mapping.findForward("cercaPersonaFisica");
			} else {
				request.setAttribute("descrizioneDitta", dForm
						.getNominativoDestinatario());
				session.setAttribute("provenienza","personaGiuridicaEditor");
				return mapping.findForward("cercaPersonaGiuridica");
			}
		} else if (request.getParameter("destinatarioId") != null) {
			String nomeDestinatario = (String) request
					.getParameter("destinatarioId");
			aggiornaDestinatarioForm(nomeDestinatario, dForm);

		} else if (request.getParameter("btnNuovoFascicolo") != null) {
			session.setAttribute("tornaEditor", Boolean.TRUE);
			return mapping.findForward("nuovoFascicolo");

		} else if (request.getParameter("btnCercaFascicoli") != null) {
			String nomeFascicolo = dForm.getCercaFascicoloNome();
			dForm.setCercaFascicoloNome("");
			request.setAttribute("cercaFascicoliDaEditor", nomeFascicolo);
			session.setAttribute("tornaEditor", Boolean.TRUE);
			return mapping.findForward("cercaFascicolo");

		} else if (request.getParameter("rimuoviFascicoli") != null) {
			if (dForm.getFascicoloSelezionatoId() != null) {
				String[] fascicoli = dForm.getFascicoloSelezionatoId();
				for (int i = 0; i < fascicoli.length; i++) {
					if (fascicoli[i] != null) {
						dForm.rimuoviFascicolo(Integer.parseInt(fascicoli[i]));
						fascicoli[i] = null;
					}
				}
			}		
			return mapping.findForward("input");
		}
		else if (request.getParameter("btnInvioProtocollo") != null) {
			errors = dForm.validateDestinatari(mapping, request);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			} else {
				Documento doc=new Documento();
				aggiornaDocumentoModel(dForm, doc, utente, request, errors);
				InvioClassificati invioC = new InvioClassificati();
				aggiornaInvioClassificatiModel(dForm, invioC, utente);
				EditorDelegate dd = EditorDelegate.getInstance();
				
				if (!errors.isEmpty()) {
					saveErrors(request, errors);
				} else {

					if (dd.invioClassificati(invioC,doc,utente,dForm.getDocumentoId()) == ReturnValues.SAVED) {
						messages.add("invio_classificati", new ActionMessage(
								"documentale_esito_operazione", "Inviato",
								"al protocollo"));
						saveMessages(request, messages);
					} else {
						errors.add("invio_classificati", new ActionMessage(
								"errore_nel_salvataggio"));
						saveErrors(request, errors);
						return mapping.findForward("input");
					}
					if(dForm.getProcedimentoId()>0){
						return mapping.findForward("indietroProcedimento");
					}else{
						request.setAttribute("documentoId", new Integer(dForm.getDocumentoId()));
						return mapping.findForward("indietro");
					}
				}
			}

		} else if (request.getParameter("btnAnnullaAction") != null) {
			dForm.setDestinatarioSelezionatoId(null);
			dForm.rimuoviDestinatari();
			dForm.rimuoviFascicoli();
			request.setAttribute("docId",String.valueOf(dForm.getDocumentoId()));
			return (mapping.findForward("indietro"));
		}
		return (mapping.findForward("input"));
	}

	public void aggiornaDocumentoModel(EditorForm form, Documento documento,
			Utente utente,HttpServletRequest request, ActionMessages errors) {
		FileVO fileVO = documento.getFileVO();
		documento.getFileVO().setDocumentoVO(FileUtil.uploadDocumento(form, request, errors));
		aggiornaDatiGeneraliDocumentoModel(form, fileVO);
	}
	
	
	
	private void aggiornaDatiGeneraliDocumentoModel(EditorForm form, FileVO documento){
		documento.setId(0);
		documento.setDataDocumento(new java.sql.Date(System.currentTimeMillis()));
		documento.setOggetto(form.getNomeFile());
		documento.setDescrizione(form.getNomeFile());
		documento.setNomeFile(form.getNomeFile()+ ".pdf");
		documento.setRowCreatedTime(new java.sql.Date(System.currentTimeMillis()));
		documento.setVersione(0);
		documento.setCaricaLavId(form.getCaricaId());
		documento.setOwnerCaricaId(form.getCaricaId());

	}
	
	private void aggiornaInvioClassificatiModel(EditorForm form,
			InvioClassificati invioClassificati, Utente utente) {
		int ufficioMittenteId = utente.getUfficioInUso();
		int utenteMittenteId = utente.getValueObject().getId().intValue();
		int aooId = utente.getUfficioVOInUso().getAooId();
		InvioClassificatiVO icVO = new InvioClassificatiVO();
		icVO.setAooId(aooId);
		icVO.setUfficioMittenteId(ufficioMittenteId);
		icVO.setUtenteMittenteId(utenteMittenteId);
		icVO.setProcedimentoId(form.getProcedimentoId());
		invioClassificati.setIcVO(icVO);
		aggiornaDestinatariModel(form, invioClassificati, utente);
		aggiornaFascicoliModel(form, invioClassificati, utente);
		//
		if(form.getDataScadenza()!=null && !form.getDataScadenza().equals(""))
			icVO.setDataScadenza(DateUtil.getData(form.getDataScadenza()));
		if(form.getTextScadenza()!=null && !form.getTextScadenza().equals(""))
			icVO.setTextScadenza(form.getTextScadenza());
	}

	private void aggiornaDestinatariModel(EditorForm form,
			InvioClassificati invioC, Utente utente) {
		invioC.removeDestinatari();
		Collection<DestinatarioView> destinatari = form.getDestinatari();
		if (destinatari != null) {
			for (Iterator<DestinatarioView> i = destinatari.iterator(); i.hasNext();) {
				DestinatarioView dest =i.next();
				DestinatarioVO destinatario = new DestinatarioVO();
				destinatario.setDestinatario(dest.getDestinatario());
				destinatario.setFlagTipoDestinatario(dest
						.getFlagTipoDestinatario());
				destinatario.setEmail(dest.getEmail());
				destinatario.setCodicePostale(dest.getCapDestinatario());
				if (dest.getCapDestinatario() != null
						&& !(dest.getCapDestinatario().equals(""))) {
					destinatario.setIndirizzo(dest.getCapDestinatario());
				}
				if (dest.getIndirizzo() != null
						&& !(dest.getIndirizzo().equals(""))) {
					destinatario.setIndirizzo(destinatario.getIndirizzo() + ' '
							+ dest.getIndirizzo());
				}

				destinatario.setCitta(dest.getCitta());
				destinatario.setMezzoSpedizioneId(dest.getMezzoSpedizioneId());

				destinatario.setDataSpedizione(DateUtil.toDate(dest
						.getDataSpedizione()));
				destinatario.setFlagConoscenza(dest.getFlagConoscenza());
				destinatario.setTitoloId(dest.getTitoloId());
				destinatario.setNote(dest.getNote());
				destinatario.setMezzoDesc(dest.getMezzoDesc());
				invioC.addDestinatari(destinatario);
			}
		}
	}

	private void aggiornaFascicoliModel(EditorForm form,
			InvioClassificati invioC, Utente utente) {
		invioC.removeFascicoli();
		Collection<FascicoloVO> fascicoli = form.getFascicoliProtocollo();
		if (fascicoli != null) {
			for (Iterator<FascicoloVO> i = fascicoli.iterator(); i.hasNext();) {
				FascicoloVO fasc =i.next();
				invioC.addFascicolo(fasc);
			}
		}
	}

	
	
	
	
	private void aggiornaDestinatarioForm(String id, EditorForm form) {
		DestinatarioView destinatario = new DestinatarioView();
		form.setFlagConoscenza(false);
		destinatario = form.getDestinatario(id);
		
		form.setIdx(Integer.parseInt(id));
		form.setTipoDestinatario(destinatario.getFlagTipoDestinatario());
		form.setNominativoDestinatario(destinatario.getDestinatario());
		String citta = "";
		if (destinatario.getCapDestinatario() != null) {
			citta = destinatario.getCapDestinatario();
		}
		if (destinatario.getCitta() != null) {
			if (citta.equals("")) {
				citta = destinatario.getCitta();
			} else {
				citta = citta + ' ' + destinatario.getCitta();
			}
		}
		form.setCitta(StringUtil.getStringa(citta));
		form.setEmailDestinatario(destinatario.getEmail());
		form.setIndirizzoDestinatario(destinatario.getIndirizzo());
		form.setCapDestinatario(destinatario.getCapDestinatario());
		form.setMezzoSpedizioneId(destinatario.getMezzoSpedizioneId());
		form.setTitoloDestinatario(TitoliDestinatarioDelegate.getInstance()
				.getTitoloDestinatario(destinatario.getTitoloId())
				.getDescription());
		form.setTitoloId(destinatario.getTitoloId());
		form.setDataSpedizione(destinatario.getDataSpedizione());
		form.setFlagConoscenza(destinatario.getFlagConoscenza());
		form.setDestinatarioMezzoId(destinatario.getDestinatarioMezzoId());
	}

	

	private void aggiungiDestinatario(EditorForm form, HttpSession session,int aooId) {
		DestinatarioView destinatario = new DestinatarioView();

		 String destinatarioMezzoId = form.getDestinatarioMezzoId();
	        String idx = "0";
	        if (destinatarioMezzoId != null && !destinatarioMezzoId.equals(""))
	            idx = destinatarioMezzoId.substring(0, destinatarioMezzoId
	                    .indexOf('_'));
	        destinatario.setIdx(Integer.parseInt(idx));
	        destinatario.setFlagTipoDestinatario(form.getTipoDestinatario());
	        if (form.getDataSpedizione() != null
	                && !"".equals(form.getDataSpedizione())) {
	            destinatario.setDataSpedizione(form.getDataSpedizione());
	        }
		destinatario.setDestinatario(form.getNominativoDestinatario().trim());
		destinatario.setEmail(form.getEmailDestinatario());
		destinatario.setCitta(form.getCitta());
		destinatario.setIndirizzo(form.getIndirizzoDestinatario());
		destinatario.setCapDestinatario(form.getCapDestinatario());
		destinatario.setFlagConoscenza(form.getFlagConoscenza());
		destinatario.setMezzoSpedizioneId(form.getMezzoSpedizione());
		if (form.getMezzoSpedizione() >= 0) {
			Collection<SpedizioneVO> mezzi = LookupDelegate.getInstance().getMezziSpedizione(
					aooId);
			Iterator<SpedizioneVO> i = mezzi.iterator();
			while (i.hasNext()) {
				SpedizioneVO m = i.next();
				if (m.getId().intValue() == form.getMezzoSpedizione()) {
					destinatario.setMezzoDesc(m.getDescrizioneSpedizione());
					destinatario.setPrezzoSpedizione(m.getPrezzo());
					break;
				}
			}
		}
		form.aggiungiDestinatario(destinatario);
		form.inizializzaDestinatarioForm();
	}

}
