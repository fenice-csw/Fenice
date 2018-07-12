package it.compit.fenice.mvc.presentation.action.documentale;

import it.compit.fenice.mvc.business.EditorDelegate;
import it.compit.fenice.mvc.presentation.actionform.documentale.EditorForm;
import it.compit.fenice.mvc.vo.documentale.EditorVO;
import it.compit.fenice.util.EditorUtil;
import it.compit.fenice.util.VelocityTemplateUtils;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.AlberoUfficiBO;
import it.finsiel.siged.mvc.business.AmministrazioneDelegate;
import it.finsiel.siged.mvc.business.FascicoloDelegate;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.business.ProcedimentoDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.business.TitoliDestinatarioDelegate;
import it.finsiel.siged.mvc.presentation.actionform.documentale.DestinatariInvioForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.presentation.helper.DestinatarioView;
import it.finsiel.siged.mvc.vo.lookup.TipoProcedimentoVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.ProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.SpedizioneVO;
import it.finsiel.siged.util.PdfUtil;
import it.finsiel.siged.util.StringUtil;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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

public class CreaDocumentoDaTemplateAction extends Action {

	static Logger logger = Logger.getLogger(CreaDocumentoDaTemplateAction.class
			.getName());

	public static void stampaPDF(EditorForm form, HttpServletRequest request,
			HttpServletResponse response, ActionMessages errors) {
		try {
			Utente utente = (Utente) request.getSession().getAttribute(Constants.UTENTE_KEY);
			String text="";
			String ctxPath="http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath();
			if(form.isUll())
				text=VelocityTemplateUtils.createULLDocument(form.getTesto(), utente, ctxPath);
			else
				text=VelocityTemplateUtils.createBBCCAADocument(form, utente,ctxPath);
			InputStream is=new ByteArrayInputStream(text.getBytes("UTF-8"));
			String tmpPath = PdfUtil.creaPDFdaHtml("lettera", is,request, errors);
			response.setContentType("application/pdf");
			OutputStream bos = response.getOutputStream();
			byte[] buff = new byte[2048];
			int bytesRead;
			InputStream stream = new FileInputStream(tmpPath);
			while (-1 != (bytesRead = stream.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename=lettera.PDF");
			stream.close();
			bos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	private void rimuoviAssegnatari(EditorForm form) {
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
					AssegnatarioView ass = (AssegnatarioView) i.next();
					form.setAssegnatarioCompetente(ass.getKey());
				}
			}
		}
	}

	protected void assegnaAdUfficio(EditorForm form, int ufficioId,Utente utente) {
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(ufficioId);
		Organizzazione org = Organizzazione.getInstance();
		Ufficio uff = org.getUfficio(ufficioId);
		ass.setNomeUfficio(uff.getValueObject().getDescription());
		ass.setDescrizioneUfficio(uff.getPath());
		EditorForm pForm = (EditorForm) form;
		ass.setCaricaAssegnanteId(utente.getCaricaInUso());
		ass.setUfficioAssegnanteId(utente.getUfficioInUso());
		ass.setStato('S');
		ass.setCompetente(true);
		ass.setTitolareProcedimento(false);
		pForm.aggiungiAssegnatario(ass);
		pForm.setAssegnatarioCompetente(ass.getKey());
		updateAssegnatariCompetenti(pForm);
	}

	private void updateAssegnatariCompetenti(EditorForm form) {
		List<String> assCompetenti = new ArrayList<String>();
		for (Iterator<AssegnatarioView> i = form.getAssegnatari().iterator(); i.hasNext();) {
			AssegnatarioView ass = (AssegnatarioView) i.next();
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
		form.setAssegnatariCompetenti(assCompArray);

	}

	protected void assegnaAdUtente(EditorForm form,Utente utente) {
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(form.getUfficioCorrenteId());
		ass.setNomeUfficio(form.getUfficioCorrente().getDescription());
		ass.setDescrizioneUfficio(form.getUfficioCorrentePath());
		ass.setUtenteId(form.getUtenteSelezionatoId());
		UtenteVO ute = form.getUtente(form.getUtenteSelezionatoId());
		ass.setNomeUtente(ute.getFullName());
		EditorForm pForm = (EditorForm) form;
		ass.setCaricaAssegnanteId(utente.getCaricaInUso());
		ass.setUfficioAssegnanteId(utente.getUfficioInUso());
		ass.setStato('S');
		ass.setCompetente(true);
		ass.setTitolareProcedimento(false);
		pForm.aggiungiAssegnatario(ass);
		pForm.setAssegnatarioCompetente(ass.getKey());
		updateAssegnatariCompetenti(pForm);
	}

	private void allacciaProtocollo(EditorForm form, HttpSession session,
			ActionMessages errors) {
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		AllaccioVO all = delegate.getProtocolloAllacciabile(utente, 
				Integer.parseInt(form.getAllaccioNumProtocollo()), 
				Integer.parseInt(form.getAllaccioNumProtocollo()), 
				Integer.parseInt(form.getAllaccioAnnoProtocollo()));
		if (all.getProtocolloAllacciatoId() != 0)
			form.setAllaccio(all);
		else {
			errors.add("allacci", new ActionMessage(
					"protocollo_non_allacciabile"));
		}
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages messages = new ActionMessages();
		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession();
		EditorForm dForm = (EditorForm) form;
		EditorDelegate delegate = EditorDelegate.getInstance();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		
		dForm.setAooId(utente.getAreaOrganizzativa().getId());
		if(!dForm.isDirigente())
			dForm.setDirigente(utente.getUfficioVOInUso().getCaricaDirigenteId()==utente.getCaricaInUso());
		
		session.setAttribute("editorForm", dForm);
		if (form == null) {
			logger.info(" Creating new InvioDocumentoAction");
			form = new DestinatariInvioForm();
			request.setAttribute(mapping.getAttribute(), form);
		}
		if (request.getAttribute("tornaProcedimento") != null) {
			dForm.setProcedimentoId((Integer) request.getAttribute("tornaProcedimento"));
			setProcedimentiParameter(dForm);
			
		}
		if (dForm.getUfficioCorrenteId() == 0) {
			AlberoUfficiBO.impostaUfficioUtentiAbilitati(utente, dForm, true);
		}
		if (request.getParameter("impostaUfficioAction") != null) {
			dForm.setUfficioCorrenteId(dForm.getUfficioSelezionatoId());
			AlberoUfficiBO.impostaUfficioUtentiAbilitati(utente, dForm, true);
			return mapping.findForward("input");
		} else if (request.getParameter("rimuoviAssegnatariAction") != null) {
			rimuoviAssegnatari(dForm);
		} else if (request.getParameter("ufficioPrecedenteAction") != null) {
			dForm
					.setUfficioCorrenteId(dForm.getUfficioCorrente()
							.getParentId());
			AlberoUfficiBO.impostaUfficioUtentiAbilitati(utente, dForm, true);
		} else if (request.getParameter("assegnaUfficioCorrenteAction") != null) {
			assegnaAdUfficio(dForm, dForm.getUfficioSelezionatoId(),utente);
			return mapping.findForward("input");

		} else if (request.getParameter("assegnaUfficioSelezionatoAction") != null) {
			assegnaAdUfficio(dForm, dForm.getUfficioSelezionatoId(),utente);
			return mapping.findForward("input");

		} else if (request.getParameter("assegnaUtenteAction") != null) {
			assegnaAdUtente(dForm,utente);
			return mapping.findForward("input");

		} else if (request.getParameter("allacciaProtocolloAction") != null) {
			errors = dForm.validateAllaccio(mapping, request);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			} else
				allacciaProtocollo(dForm, session, errors);
			if (!errors.isEmpty())
				saveErrors(request, errors);
			return mapping.findForward("input");
		} else if (request.getParameter("rimuoviAllacciAction") != null) {
			dForm.resetAllaccio();
			return mapping.findForward("input");
		} else if (request.getParameter("aggiungiDestinatario") != null) {
			errors = dForm.validateDestinatario(mapping, request);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			} else
				aggiungiDestinatario(dForm, session, utente
						.getAreaOrganizzativa().getId());
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
			session.setAttribute("tornaDocumentoTemplate", Boolean.TRUE);
			if ("F".equals(dForm.getTipoDestinatario())) {
				request.setAttribute("cognome", dForm
						.getNominativoDestinatario());
				session.setAttribute("provenienza", "personaFisicaTemplate");
				return mapping.findForward("cercaPersonaFisica");
			} else {
				request.setAttribute("descrizioneDitta", dForm
						.getNominativoDestinatario());
				session.setAttribute("provenienza", "personaGiuridicaTemplate");
				return mapping.findForward("cercaPersonaGiuridica");
			}
		} else if (request.getParameter("destinatarioId") != null) {
			String nomeDestinatario = (String) request
					.getParameter("destinatarioId");
			aggiornaDestinatarioForm(nomeDestinatario, dForm);

		} else if (request.getParameter("btnNuovoFascicolo") != null) {
			session.setAttribute("tornaTemplate", Boolean.TRUE);
			return mapping.findForward("nuovoFascicolo");
		} else if (request.getParameter("btnCercaFascicoli") != null) {
			String nomeFascicolo = dForm.getCercaFascicoloNome();
			dForm.setCercaFascicoloNome("");
			request.setAttribute("cercaFascicoliDaTemplate", nomeFascicolo);
			session.setAttribute("tornaTemplate", Boolean.TRUE);
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
		} else if (request.getParameter("btnStampa") != null) {
			errors = dForm.validateStampa(mapping, request);
			if (!errors.isEmpty())
				saveErrors(request, errors);
			else {
				stampaPDF(dForm, request, response, errors);
				return null;
			}
		} else if (request.getParameter("btnProtocolla") != null) {
			dForm.setCaricaDocumento(false);
			EditorUtil.preparaProtocolloUscita(request, dForm, utente, errors,1);
			if(dForm.isResponsabileEnte()){
				if(dForm.getStatoProcedimentoULL()==4){
					ProcedimentoDelegate.getInstance().inviaProcedimento(dForm.getProcedimentoId(), "I",utente);
				}
			}
			return (mapping.findForward("protocollazioneUscita"));
		}
		else if (request.getParameter("btnProtocollaInterna") != null) {
			dForm.setCaricaDocumento(true);
			EditorUtil.preparaPostaInterna(request, dForm, utente, errors,1);
			if(dForm.isResponsabileEnte()){
				if(dForm.getStatoProcedimentoULL()==4){
					ProcedimentoDelegate.getInstance().inviaProcedimento(dForm.getProcedimentoId(), "I",utente);
				}
			}
			return (mapping.findForward("postaInterna"));
		}
		else if (request.getParameter("btnInviaFirma") != null) {
			errors = dForm.validateFirma(mapping, request);
			if (utente.getUfficioVOInUso().getCaricaDirigenteId() == 0)
				errors.add("error.documento.no_carica_dirigente",
						new ActionMessage(
								"error.documento.no_carica_dirigente", utente
										.getUfficioVOInUso().getDescription(),
								""));
			if (errors.isEmpty()) {
				EditorVO eVO = new EditorVO();
				EditorUtil.aggiornaModel(dForm, eVO, utente);
				eVO.setFlagTipo(1);
				eVO = delegate.salvaDocumentoEditorTemplate(eVO);
				if (eVO.getReturnValue() == ReturnValues.SAVED) {
					eVO.setMsgCarica(dForm.getMsgCarica());
					int retVal = delegate.inviaPerFirma(eVO, utente.getUfficioVOInUso().getCaricaDirigenteId());
					if (retVal == ReturnValues.SAVED) {
						dForm.inizializzaForm();
						messages.add("operazione_ok", new ActionMessage(
								"operazione_ok", "", ""));
						//if()
						return (mapping.findForward("success"));
					}
				} else
					errors.add("errore_nel_salvataggio", new ActionMessage(
							"errore_nel_salvataggio", "", ""));
			}
		} else if (request.getParameter("btnInviaAvvocatoGenerale") != null) {
			errors = dForm.validateFirma(mapping, request);
			if (Organizzazione.getInstance().getCaricaResponsabile()==null)
				errors.add("error.documento.no_carica_responsabile",
						new ActionMessage(
								"error.documento.no_carica_responsabile", "Avvocato Generale",
								""));
			if (errors.isEmpty()) {
				EditorVO eVO = new EditorVO();
				EditorUtil.aggiornaModel(dForm, eVO, utente);
				eVO.setFlagTipo(1);
				eVO = delegate.salvaDocumentoEditorTemplate(eVO);
				if (eVO.getReturnValue() == ReturnValues.SAVED) {
					int retVal = delegate.inviaPerFirma(eVO, Organizzazione.getInstance().getCaricaResponsabile().getCaricaId());
					if (retVal == ReturnValues.SAVED) {
						ProcedimentoDelegate.getInstance().inviaProcedimento(dForm.getProcedimentoId(), "A",utente);
						dForm.inizializzaForm();
						messages.add("operazione_ok", new ActionMessage(
								"operazione_ok", "", ""));
						return (mapping.findForward("success"));
					}
				} else
					errors.add("errore_nel_salvataggio", new ActionMessage(
							"errore_nel_salvataggio", "", ""));
			}
		} else if (request.getParameter("btnConferma") != null) {
			errors = dForm.validateFirma(mapping, request);
			if (errors.isEmpty()) {
				EditorVO eVO = new EditorVO();
				EditorUtil.aggiornaModel(dForm, eVO, utente);
				eVO.setFlagTipo(1);
				eVO = delegate.salvaDocumentoEditorTemplate(eVO);
				if (eVO.getReturnValue() != ReturnValues.SAVED) 
					errors.add("errore_nel_salvataggio", new ActionMessage("errore_nel_salvataggio", "", ""));
				else
					return (mapping.findForward("lista"));
			}
		} else if (request.getParameter("btnAnnullaAction") != null) {
			dForm.setDestinatarioSelezionatoId(null);
			dForm.rimuoviDestinatari();
			dForm.resetAllaccio();
			request.setAttribute("docId", String.valueOf(dForm.getDocumentoId()));
			return (mapping.findForward("indietro"));
		} else if (request.getParameter("btnIndietroProcedimento") != null) {
			dForm.setDestinatarioSelezionatoId(null);
			dForm.rimuoviDestinatari();
			dForm.resetAllaccio();
			return (mapping.findForward("indietroProcedimento"));
		}
		else if (request.getParameter("btnElimina") != null) {
			boolean deleted=delegate.eliminaDocumentoTemplate(dForm.getDocumentoId());
			if(deleted){
				messages.add("operazione_ok", new ActionMessage(
						"operazione_ok", "", ""));
				return (mapping.findForward("success"));
			}
			else
				errors.add("errore_nel_salvataggio", new ActionMessage("errore_nel_salvataggio", "", ""));
		}else if (request.getParameter("btnRifiuta") != null) {
			EditorVO eVO=new EditorVO();
			EditorUtil.aggiornaModel(dForm, eVO, utente);
			
			eVO.setMsgCarica(dForm.getMsgCarica());
			boolean deleted=delegate.rifiutaDocumentoTemplate(eVO);
			if(deleted)
				return (mapping.findForward("lista"));
			else
				errors.add("errore_nel_salvataggio", new ActionMessage("errore_nel_salvataggio", "", ""));

		}
		else if (request.getParameter("docId") != null) {
			int docId = 0;
			docId = Integer.valueOf(request.getParameter("docId")).intValue();
			EditorVO vo = delegate.getDocumentoTemplate(docId);
			if (vo.getReturnValue() != ReturnValues.FOUND) {
				errors.add("nomeFile", new ActionMessage(
						"error.documento.non_caricato", docId, ""));
				return mapping.findForward("input");
			}
			EditorUtil.aggiornaDocumentoForm(vo, dForm, utente);
			updateAssegnatariCompetenti(dForm);
			request.setAttribute(mapping.getAttribute(), dForm);
			return mapping.findForward("input");
		}
		if (!messages.isEmpty())
			saveMessages(request, messages);
		if (!errors.isEmpty())
			saveErrors(request, errors);
		return (mapping.findForward("input"));
	}

	private static void setProcedimentiParameter(EditorForm dForm) {
		ProcedimentoVO proc=ProcedimentoDelegate.getInstance().getProcedimentoVO(dForm.getProcedimentoId());
		FascicoloVO fascicolo=FascicoloDelegate.getInstance().getFascicoloVOById(proc.getFascicoloId());
		TipoProcedimentoVO tp=AmministrazioneDelegate.getInstance().getTipoProcedimento(proc.getTipoProcedimentoId());
		if(tp.isUll()){
			dForm.setUll(true);
		}
		dForm.aggiungiFascicolo(fascicolo);
		dForm.setTipo("IN");
	}

	private void aggiornaDestinatarioForm(String id, EditorForm form) {
		DestinatarioView destinatario = new DestinatarioView();
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
		form.setFlagPresso(destinatario.getFlagPresso());
		form.setFlagPEC(destinatario.getFlagPEC());
		form.setDestinatarioMezzoId(destinatario.getDestinatarioMezzoId());
	}

	private void aggiungiDestinatario(EditorForm form, HttpSession session,
			int aooId) {
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
		destinatario.setFlagPresso(form.getFlagPresso());
		destinatario.setFlagPEC(form.getFlagPEC());
		destinatario.setMezzoSpedizioneId(form.getMezzoSpedizione());
		if (form.getMezzoSpedizione() >= 0) {
			Collection<SpedizioneVO> mezzi = LookupDelegate.getInstance().getMezziSpedizione(
					aooId);
			Iterator<SpedizioneVO> i = mezzi.iterator();
			while (i.hasNext()) {
				SpedizioneVO m =i.next();
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
