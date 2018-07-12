package it.finsiel.siged.mvc.presentation.action.amministrazione;

import it.compit.fenice.mvc.presentation.action.amministrazione.helper.file.TipoProcedimentoFileUtility;
import it.compit.fenice.mvc.vo.protocollo.UfficioPartecipanteVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.AreaOrganizzativa;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.TitolarioBO;
import it.finsiel.siged.mvc.business.AmministrazioneDelegate;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.TipoProcedimentoForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProcedimentoForm;
import it.finsiel.siged.mvc.vo.lookup.AmministrazionePartecipanteVO;
import it.finsiel.siged.mvc.vo.lookup.TipoProcedimentoVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.util.NumberUtil;
import it.flosslab.mvc.presentation.action.protocollo.helper.file.ProtocolloFileUtility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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



public class TipoProcedimentoAction extends Action {

	static Logger logger = Logger
			.getLogger(TipoDocumentoAction.class.getName());

	
	private void aggiornaUfficiPartecipantiForm(Collection<UfficioPartecipanteVO> ufficiPartecipanti,
			TipoProcedimentoForm form) {
		form.removeUfficiPartecipanti();
		if (ufficiPartecipanti != null) {
			for (UfficioPartecipanteVO ufficio : ufficiPartecipanti) {
				form.aggiungiUfficioPartecipante(ufficio);
				if(ufficio.isPrincipale())
					form.setUfficioPrincipaleId(ufficio.getUfficioId());
			}
		}
	}
	
	private void aggiornaUfficiPartecipantiModel(TipoProcedimentoForm form,
			TipoProcedimentoVO vo) {
		vo.removeUfficiPartecipanti();
		Collection<UfficioPartecipanteVO> ufficiPartecipanti = form.getUfficiPartecipanti();
		if (ufficiPartecipanti != null) {
			for (UfficioPartecipanteVO uff : ufficiPartecipanti) {
				if(form.getUfficioPrincipaleId()==uff.getUfficioId())
					uff.setPrincipale(true);
				vo.addUfficiPartecipante(uff);
			}
		}
	}
	
	protected void aggiungiUfficioPartecipante(TipoProcedimentoForm form, int ufficioId) {
		UfficioPartecipanteVO ufficioPartecipante= new UfficioPartecipanteVO();
		ufficioPartecipante.setUfficioId(ufficioId);
		Organizzazione org = Organizzazione.getInstance();
		Ufficio uff = org.getUfficio(ufficioId);
		ufficioPartecipante.setNomeUfficio(uff.getValueObject().getDescription());
		form.aggiungiUfficioPartecipante(ufficioPartecipante);
		form.setUfficioSelezionatoId(0);
	}
	
	public ActionForward downloadDocumento(ActionMapping mapping,
			DocumentoVO doc, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionMessages errors = ProtocolloFileUtility.downloadFile(doc,
				response);
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return mapping.findForward("erroreDownload");
		}
		ActionForward actionForward = new ActionForward();
		actionForward.setName("none");
		return actionForward;
	}
	
	private static void impostaUfficio(Utente utente, TipoProcedimentoForm form) {
		int ufficioId = form.getUfficioCorrenteId();
		Organizzazione org = Organizzazione.getInstance();
		AreaOrganizzativa aoo = org.getAreaOrganizzativa(utente
				.getUfficioVOInUso().getAooId());
		Ufficio ufficioRoot = aoo.getUfficioCentrale();
		ufficioRoot = aoo.getUfficioCentrale();
		Ufficio ufficioCorrente = org.getUfficio(ufficioId);
		if (ufficioCorrente == null) {
			ufficioCorrente = ufficioRoot;
			ufficioId = ufficioRoot.getValueObject().getId().intValue();
		}
		Ufficio uff = ufficioCorrente;
		while (uff != ufficioRoot) {
			if (uff == null) {
				ufficioCorrente = ufficioRoot;
				ufficioId = ufficioCorrente.getValueObject().getId().intValue();
				break;
			}
			uff = uff.getUfficioDiAppartenenza();
		}
		form.setUfficioCorrenteId(ufficioId);
		form.setUfficioCorrentePath(ufficioCorrente.getPath());
		form.setUfficioCorrente(ufficioCorrente.getValueObject());
		List<UfficioVO> list = new ArrayList<UfficioVO>();
		for (Iterator<Ufficio> i = ufficioCorrente.getUfficiDipendenti().iterator(); i
				.hasNext();) {
			uff = (Ufficio) i.next();
			list.add(uff.getValueObject());
		}
		Comparator<UfficioVO> c = new Comparator<UfficioVO>() {
			public int compare(UfficioVO uff1, UfficioVO uff2) {
				return uff1.getDescription().compareToIgnoreCase(
						uff2.getDescription());
			}
		};
		Collections.sort(list, c);
		form.setUfficiDipendenti(list);
	}
	
	

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession(true); 
		AmministrazioneDelegate delegate = AmministrazioneDelegate
				.getInstance();
		TipoProcedimentoForm tipoProcedimentoForm = (TipoProcedimentoForm) form;
		TipoProcedimentoVO tipoProcedimentoVO = new TipoProcedimentoVO();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		int utenteId = utente.getValueObject().getId().intValue();
		tipoProcedimentoForm.setTipiProcedimento(delegate.getTipiProcedimento(utente.getRegistroVOInUso().getAooId()));
		if (form == null) {
			logger.info("Creating new TipoProcedimentoAction");
			form = new TipoProcedimentoForm();
			session.setAttribute(mapping.getAttribute(), form);
		}
		if (tipoProcedimentoForm.getUfficioCorrenteId() == 0) {
			impostaUfficio(utente, tipoProcedimentoForm);
		} 
		if (request.getParameter("downloadAllegatoId") != null) {
			int docId = NumberUtil.getInt((String) request
					.getParameter("downloadAllegatoId"));
			DocumentoVO doc = tipoProcedimentoForm.getDocumentoAllegato(String
					.valueOf(docId));
			return downloadDocumento(mapping, doc, request, response);
		}
		if (request.getParameter("btnNuovoTipoProcedimento") != null) {
			tipoProcedimentoForm.inizializzaForm();
			impostaTitolario(tipoProcedimentoForm, utente, 0);
			request.setAttribute("utenteId", "" + utenteId);
			return (mapping.findForward("edit"));
		} else if (request.getParameter("btnAnnulla") != null) {
			tipoProcedimentoForm.inizializzaForm();
			tipoProcedimentoForm.setTipiProcedimento(delegate.getTipiProcedimento(utente.getRegistroVOInUso().getAooId()));
			return (mapping.findForward("input"));
		} else if (request.getParameter("btnConferma") != null) {
			readTipoProcedimentoForm(tipoProcedimentoVO, tipoProcedimentoForm);
			errors = tipoProcedimentoForm.validateDatiInserimento(mapping,
					request);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return (mapping.findForward("edit"));
			}
				caricaDatiNelVO(tipoProcedimentoVO, tipoProcedimentoForm,
						utente);
				AmministrazioneDelegate.getInstance().salvaTipoProcedimento(
						tipoProcedimentoVO);
				tipoProcedimentoForm.inizializzaForm();
				if (tipoProcedimentoVO.getReturnValue() == ReturnValues.SAVED) {
					aggiornaLookupTipiProcedimento(tipoProcedimentoForm, utente
							.getRegistroVOInUso().getAooId());
				} else {
					errors
							.add(
									"registrazione_tipo",
									new ActionMessage("record_non_inseribile",
											"il tipo Procedimento",
											": ne esiste gi� uno con lo stesso nome per l'ufficio"));
					saveErrors(request, errors);
					return (mapping.findForward("edit"));
				}
		} else if (request.getParameter("btnModifica") != null) {
			errors = tipoProcedimentoForm.validateDatiInserimento(mapping,
					request);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return (mapping.findForward("edit"));
			}
			modificaDatiNelVO(tipoProcedimentoVO, tipoProcedimentoForm, utente);
			AmministrazioneDelegate.getInstance().salvaTipoProcedimento(
					tipoProcedimentoVO);
			if (tipoProcedimentoVO.getReturnValue() == ReturnValues.SAVED) {
				aggiornaLookupTipiProcedimento(tipoProcedimentoForm, utente
						.getRegistroVOInUso().getAooId());
			} else {
				errors.add("registrazione_tipo", new ActionMessage(
						"record_non_inseribile", "il tipo Procedimento",
						": ne esiste gi� uno con lo stesso nome per la AOO"));
				saveErrors(request, errors);
				return (mapping.findForward("edit"));
			}
		} else if (request.getParameter("btnCancella") != null) {
			int tipoProcedimentoId = tipoProcedimentoForm.getIdTipo();
			tipoProcedimentoVO = delegate
					.getTipoProcedimento(tipoProcedimentoId);
			caricaDatiNelForm(tipoProcedimentoForm, tipoProcedimentoVO,delegate,utente);
			if (AmministrazioneDelegate.getInstance().cancellaTipoProcedimento(tipoProcedimentoId)) {
				aggiornaLookupTipiProcedimento(tipoProcedimentoForm, utente
						.getRegistroVOInUso().getAooId());
				errors.add("cancellazione_tipo", new ActionMessage(
						"cancellazione_ok"));
			} else {
				errors.add("cancellazione_tipo", new ActionMessage(
						"record_non_cancellabile", "il tipo Procedimento", ""));
				saveErrors(request, errors);
				return (mapping.findForward("edit"));
			}

		} else if (request.getParameter("tipoProcId") != null) {
			int id=Integer.parseInt(request.getParameter("tipoProcId"));
			tipoProcedimentoVO = delegate.getTipoProcedimento(id);
			caricaDatiNelForm(tipoProcedimentoForm, tipoProcedimentoVO,delegate,utente);
			return (mapping.findForward("edit"));
		} else if (request.getParameter("annullaAction") != null) {
			if ("true".equals(request.getParameter("annullaAction"))
					|| tipoProcedimentoForm.getIdTipo() == 0) {
				tipoProcedimentoForm.inizializzaForm();
			}
			saveToken(request);
			return mapping.findForward("input");
		} else if (request.getParameter("cercaAction") != null) {
			String desc = tipoProcedimentoForm.getDescrizione();
			ArrayList<TipoProcedimentoVO> elenco = null;
			if (desc == null || "".equals(desc)) {
				elenco = delegate.getTipiProcedimento(utente
						.getRegistroVOInUso().getAooId());
			} else {
				elenco = delegate.getTipiProcedimento(utente
						.getRegistroVOInUso().getAooId(), desc);
			}
			if (elenco != null) {
				tipoProcedimentoForm.setTipiProcedimento(elenco);
			}
			return (mapping.findForward("input"));
		} else if (request.getParameter("allegaDocumentoAction") != null) {
			TipoProcedimentoFileUtility.uploadFile(tipoProcedimentoForm,
					request, errors);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			}
			return mapping.findForward("edit");
		} else if (request.getParameter("rimuoviAllegatiAction") != null) {
			String[] allegati = tipoProcedimentoForm.getAllegatiSelezionatiId();
			if (allegati != null) {
				for (int i = 0; i < allegati.length; i++) {
					tipoProcedimentoForm.rimuoviAllegato(allegati[i]);
					allegati[i] = null;
				}
			}
			return mapping.findForward("edit");
		} else if (request.getParameter("cercaAmministrazioni") != null) {
			session.setAttribute("tornaTipoProcedimento", Boolean.TRUE);
			return mapping.findForward("cercaAmministrazione");
		} else if (request.getParameter("aggiungiAmministrazione") != null) {
			aggiungiAmministrazione(tipoProcedimentoForm, session);
			return mapping.findForward("edit");
		} else if (request.getParameter("rimuoviAmministrazione") != null) {
			String[] amministrazioni = tipoProcedimentoForm
					.getAmministrazioniSelezionateId();
			if (amministrazioni != null) {
				for (int i = 0; i < amministrazioni.length; i++) {
					tipoProcedimentoForm
							.rimuoviAmministrazione(amministrazioni[i]);
					amministrazioni[i] = null;
				}
			} else {
				errors.add("rimuoviAmministrazione", new ActionMessage("amministrazione_rimuovi"));
				saveErrors(request, errors);
			}
			return mapping.findForward("edit");
		} else if (request.getParameter("impostaTitolarioAction") != null) {
			if (tipoProcedimentoForm.getTitolario() != null) {
				tipoProcedimentoForm.setTitolarioPrecedenteId(tipoProcedimentoForm
						.getTitolario().getId().intValue());
			}
			impostaTitolario(tipoProcedimentoForm, utente, tipoProcedimentoForm
					.getTitolarioSelezionatoId());
			return (mapping.findForward("edit"));
		} else if (request.getParameter("titolarioPrecedenteAction") != null) {
			impostaTitolario(tipoProcedimentoForm, utente, tipoProcedimentoForm
					.getTitolarioPrecedenteId());
			if (tipoProcedimentoForm.getTitolario() != null) {
				tipoProcedimentoForm.setTitolarioPrecedenteId(tipoProcedimentoForm
						.getTitolario().getParentId());
			}
			return (mapping.findForward("edit"));
		} else if (request.getParameter("impostaUfficioAction") != null) {
			tipoProcedimentoForm.setUfficioCorrenteId(tipoProcedimentoForm.getUfficioSelezionatoId());
			impostaUfficio(utente, tipoProcedimentoForm);
			return mapping.findForward("edit");
		} else if (request.getParameter("ufficioPrecedenteAction") != null) {
			tipoProcedimentoForm.setUfficioCorrenteId(tipoProcedimentoForm.getUfficioCorrente().getParentId());
			impostaUfficio(utente, tipoProcedimentoForm);
			return mapping.findForward("edit");
		} else if (request.getParameter("assegnaUfficioCorrenteAction") != null) {
			aggiungiUfficioPartecipante(tipoProcedimentoForm, tipoProcedimentoForm.getUfficioCorrenteId());
			return mapping.findForward("edit");
		}else if (request.getParameter("rimuoviUfficiPartecipantiAction") != null) {
			String[] uffici = tipoProcedimentoForm.getUfficiPartecipantiId();
			if (uffici != null) {
				for (int i = 0; i < uffici.length; i++) {
					tipoProcedimentoForm.rimuoviUfficioPartecipante(uffici[i]);
					uffici[i] = null;
				}
			} else {
				errors.add("rimuoviUfficiPartecipanti", new ActionMessage("ufficio_rimuovi"));
				saveErrors(request, errors);
			}
			return mapping.findForward("edit");
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		logger.info("Execute TipoPAction");
		return (mapping.findForward("input"));
	}

	private void aggiungiAmministrazione(TipoProcedimentoForm form,
			HttpSession session) {
		AmministrazionePartecipanteVO amministrazione = new AmministrazionePartecipanteVO();
		amministrazione.setId(form.getAmministrazioneId());
		amministrazione.setDenominazione(form.getNominativoAmministrazione()
				.trim());
		amministrazione.setIdx(form.getIdx());
		form.aggiungiAmministrazione(amministrazione);
	}

	public void caricaDatiNelVO(TipoProcedimentoVO vo,
			TipoProcedimentoForm form, Utente utente) {
		vo.setAooId(utente.getValueObject().getAooId());
		vo.setIdTipo(form.getIdTipo());
		if (form.getTitolario() != null) {
			vo.setTitolarioId(form.getTitolario().getId().intValue());
		}
		vo.setDescrizione(form.getDescrizione());
		if (vo.getIdTipo() == 0) {
			vo.setRowCreatedUser(utente.getValueObject().getUsername());
		}
		vo.setRowUpdatedUser(utente.getValueObject().getUsername());
		if (form.getGiorniMax()==null || form.getGiorniMax().trim().equals(""))
			vo.setGiorniMax(0);
		else
			vo.setGiorniMax(Integer.parseInt(form.getGiorniMax()));
		if (form.getGiorniAlert()==null || form.getGiorniAlert().trim().equals(""))
			vo.setGiorniAlert(0);
		else
			vo.setGiorniAlert(Integer.parseInt(form.getGiorniAlert()));
		aggiornaAmministrazioniModel(form, vo);
		vo.setRiferimentiLegislativi(form.getDocumentiAllegati());
		aggiornaUfficiPartecipantiModel(form, vo);
		//vo.setUfficiPartecipanti(form.getUfficiPartecipanti());
	}

	public void modificaDatiNelVO(TipoProcedimentoVO vo,
			TipoProcedimentoForm form, Utente utente) {
		vo.setAooId(utente.getValueObject().getAooId());
		vo.setIdTipo(form.getIdTipo());
		if (form.getTitolario() != null) {
			vo.setTitolarioId(form.getTitolario().getId().intValue());
		}
		vo.setDescrizione(form.getDescrizione());
		if (vo.getIdTipo() == 0) {
			vo.setRowCreatedUser(utente.getValueObject().getUsername());
		}
		vo.setRowUpdatedUser(utente.getValueObject().getUsername());
		if (form.getGiorniMax()==null || form.getGiorniMax().trim().equals(""))
			vo.setGiorniMax(0);
		else
			vo.setGiorniMax(Integer.parseInt(form.getGiorniMax()));
		if (form.getGiorniAlert()==null || form.getGiorniAlert().trim().equals(""))
			vo.setGiorniAlert(0);
		else
			vo.setGiorniAlert(Integer.parseInt(form.getGiorniAlert()));
		aggiornaAmministrazioniModel(form, vo);
		vo.setRiferimentiLegislativi(form.getDocumentiAllegati());
		aggiornaUfficiPartecipantiModel(form, vo);
	}

	private void aggiornaAmministrazioniModel(TipoProcedimentoForm form,
			TipoProcedimentoVO vo) {
		vo.removeAmministrazioni();
		Collection<AmministrazionePartecipanteVO> amministrazioni = form.getAmministrazioni();
		if (amministrazioni != null) {
			for (Iterator<AmministrazionePartecipanteVO> i = amministrazioni.iterator(); i.hasNext();) {
				AmministrazionePartecipanteVO amm = i.next();
				AmministrazionePartecipanteVO amministrazione = new AmministrazionePartecipanteVO();
				amministrazione.setIdx(amm.getIdx());
				amministrazione.setDenominazione(amm.getDenominazione());
				amministrazione.setRubricaId(amm.getId());
				vo.addAmministrazioni(amministrazione);
			}
		}
	}
	
	/*
	private void aggiornaUfficiPartecipantiModel(TipoProcedimentoForm form,
			TipoProcedimentoVO vo) {
		vo.removeUfficiPartecipanti();
		
		Collection<UfficioPartecipanteVO> ufficiPartecipanti = form.getUfficiPartecipanti();
		if (ufficiPartecipanti != null) {
			for (Iterator<UfficioPartecipanteView> i = ufficiPartecipanti.iterator(); i.hasNext();) {
				UfficioPartecipanteView uff = i.next();
				UfficioPartecipanteVO ufficio = new UfficioPartecipanteVO();
				ufficio.setUfficioId(uff.getUfficioId());
				vo.addUfficiPartecipante(ufficio);
			}
		}
	}
	*/
	
	public void caricaDatiNelForm(TipoProcedimentoForm form,
			TipoProcedimentoVO vo,AmministrazioneDelegate delegate,Utente utente) {
		form.setIdTipo(vo.getIdTipo());
		form.setDescrizione(vo.getDescrizione());
		if(vo.getGiorniMax()!=0)
			form.setGiorniMax(String.valueOf(vo.getGiorniMax()));
		else
			form.setGiorniMax(null);
		if(vo.getGiorniAlert()!=0)
			form.setGiorniAlert(String.valueOf(vo.getGiorniAlert()));
		else
			form.setGiorniAlert(null);
		impostaTitolario(form,utente,vo.getTitolarioId());
		form.setAmministrazioni(delegate.getAmministrazioniPartecipanti(vo.getIdTipo()));
		form.setDocumentiAllegati(delegate.getRiferimenti(vo.getIdTipo()));
		aggiornaUfficiPartecipantiForm(delegate.getUfficiPartecipanti(vo.getIdTipo()), form);
	}

	private void aggiornaLookupTipiProcedimento(TipoProcedimentoForm form,
			int aooId) {
		form.setTipiProcedimento(AmministrazioneDelegate.getInstance()
				.getTipiProcedimento(aooId));
		LookupDelegate.getInstance().caricaTipiProcedimento();
		logger.info("AmministrazioneDelegate: caricaTipiProcedimento");
	}

	public static void readTipoProcedimentoForm(TipoProcedimentoVO vo,
			TipoProcedimentoForm form) {
		vo.setDescrizione(form.getDescrizione());
	}
	
	private void impostaTitolario(TipoProcedimentoForm form, Utente utente,
			int titolarioId) {
		int ufficioId = utente.getUfficioInUso();
		TitolarioBO.impostaTitolario(form, ufficioId, titolarioId);
	}

}
