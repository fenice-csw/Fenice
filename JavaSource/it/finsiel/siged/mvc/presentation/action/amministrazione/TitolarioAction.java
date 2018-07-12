package it.finsiel.siged.mvc.presentation.action.amministrazione;

import it.compit.fenice.mvc.presentation.action.amministrazione.helper.file.TitolarioFileUtility;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.TitolarioBO;
import it.finsiel.siged.mvc.business.TitolarioDelegate;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.TitolarioForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.presentation.helper.DestinatarioView;
import it.finsiel.siged.mvc.vo.lookup.TitolarioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.util.NumberUtil;
import it.flosslab.mvc.presentation.action.protocollo.helper.file.ProtocolloFileUtility;

import java.util.ArrayList;
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

/**
 * Implementation of <strong>Action </strong> to create a new E-Photo Utente.
 * 
 * @author Almaviva sud.
 * 
 */

public final class TitolarioAction extends Action {

	static Logger logger = Logger.getLogger(TitolarioAction.class.getName());

	protected void assegnaAdUtente(TitolarioForm form) {
		AssegnatarioView resp = new AssegnatarioView();
		resp.setUfficioId(form.getUfficioResponsabileCorrenteId());
		resp.setNomeUfficio(form.getUfficioCorrenteResponsabile()
				.getDescription());
		resp.setDescrizioneUfficio(form.getUfficioResponsabileCorrentePath());
		resp.setUtenteId(form.getUtenteResponsabileSelezionatoId());
		UtenteVO ute = form
				.getUtente(form.getUtenteResponsabileSelezionatoId());
		resp.setNomeUtente(ute.getFullName());
		((TitolarioForm) form).setResponsabile(resp);
	}

	protected void assegnaAdUfficio(TitolarioForm form, int ufficioId) {
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(ufficioId);
		Organizzazione org = Organizzazione.getInstance();
		Ufficio uff = org.getUfficio(ufficioId);
		ass.setNomeUfficio(uff.getValueObject().getDescription());
		ass.setDescrizioneUfficio(uff.getPath());
		((TitolarioForm) form).setResponsabile(ass);

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
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages messages = new ActionMessages();
		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession(true);
		TitolarioVO titolario = new TitolarioVO();
		TitolarioForm titolarioForm = (TitolarioForm) form;
		boolean ufficioCompleto = true;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		if (form == null) {
			logger.info("Creating new Titolario Form");
			form = new TitolarioForm();
			session.setAttribute(mapping.getAttribute(), form);
		}
		if (request.getParameter("downloadAllegatoId") != null) {
			int docId = NumberUtil.getInt((String) request
					.getParameter("downloadAllegatoId"));
			DocumentoVO doc = titolarioForm.getDocumentoAllegato(String.valueOf(docId));
			return downloadDocumento(mapping, doc, request, response);
		}
		if (titolarioForm.getUfficioResponsabileCorrenteId() == 0) {
			TitolarioBO.impostaUfficioUtentiResponsabile(utente, titolarioForm,
					ufficioCompleto);
		}
		if (request.getParameter("allegaDocumentoAction") != null) {
			TitolarioFileUtility.uploadFile(titolarioForm, request, errors);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			}
			return mapping.findForward("aggiorna");
		} else if (request.getParameter("rimuoviAllegatiAction") != null) {
			String[] allegati = titolarioForm.getAllegatiSelezionatiId();
			if (allegati != null) {
				for (int i = 0; i < allegati.length; i++) {
					titolarioForm.rimuoviAllegato(allegati[i]);
					allegati[i] = null;
				}
			}
			return mapping.findForward("aggiorna");
		} else if (request.getParameter("impostaUfficioAction") != null) {
			titolarioForm.setUfficioResponsabileCorrenteId(titolarioForm
					.getUfficioResponsabileSelezionatoId());
			TitolarioBO.impostaUfficioUtentiResponsabile(utente, titolarioForm,
					ufficioCompleto);
			return mapping.findForward("aggiorna");
		} else if (request.getParameter("ufficioPrecedenteAction") != null) {
			titolarioForm.setUfficioResponsabileCorrenteId(titolarioForm
					.getUfficioCorrenteResponsabile().getParentId());
			TitolarioBO.impostaUfficioUtentiResponsabile(utente, titolarioForm,
					ufficioCompleto);
			return mapping.findForward("aggiorna");
		} else if (request.getParameter("assegnaUtenteAction") != null) {
			assegnaAdUtente(titolarioForm);
			return mapping.findForward("aggiorna");
		} else if (request.getParameter("assegnaUfficioCorrenteAction") != null) {
			assegnaAdUfficio(titolarioForm, titolarioForm
					.getUfficioResponsabileCorrenteId());
			return mapping.findForward("aggiorna");
		} else if (request.getParameter("cercaAmministrazioni") != null) {
			session.setAttribute("tornaTitolario", Boolean.TRUE);
			return mapping.findForward("cercaAmministrazione");
		} else if (request.getParameter("aggiungiAmministrazione") != null) {
			aggiungiAmministrazione(titolarioForm, session);
			return mapping.findForward("aggiorna");
		} else if (request.getParameter("rimuoviAmministrazione") != null) {
			String[] amministrazioni = titolarioForm
					.getAmministrazioniSelezionateId();
			if (amministrazioni != null) {
				for (int i = 0; i < amministrazioni.length; i++) {
					titolarioForm.rimuoviAmministrazione(amministrazioni[i]);
					amministrazioni[i] = null;
				}
			} else {
				errors.add("rimuoviDestinatari", new ActionMessage(
						"destinatario_rimuovi"));
				saveErrors(request, errors);
			}
			return mapping.findForward("aggiorna");
		} else if (request.getParameter("impostaTitolarioAction") != null) {
			if (titolarioForm.getTitolario() != null) {
				titolarioForm.setTitolarioPrecedenteId(titolarioForm
						.getTitolario().getId().intValue());
			}
			Utente ute = (Utente) session.getAttribute(Constants.UTENTE_KEY);
			impostaTitolario(titolarioForm, ute.getUfficioInUso(),
					titolarioForm.getTitolarioSelezionatoId(), utente
							.getUfficioVOInUso().getAooId());
			
			return mapping.findForward("input");

		} else if (request.getParameter("titolarioPrecedenteAction") != null) {
			impostaTitolario(titolarioForm, utente.getRegistroInUso(),
					titolarioForm.getTitolarioPrecedenteId(), utente
							.getUfficioVOInUso().getAooId());
			if (titolarioForm.getTitolario() != null) {
				titolarioForm.setTitolarioPrecedenteId(titolarioForm
						.getTitolario().getParentId());
			}
			return mapping.findForward("input");

		} else if (request.getParameter("btnModifica") != null
				|| request.getParameter("btnSposta") != null) {
			if(request.getParameter("titolarioId") != null){
				Integer titolarioId=Integer.valueOf(request.getParameter("titolarioId"));
			Utente ute = (Utente) session.getAttribute(Constants.UTENTE_KEY);
			impostaTitolario(titolarioForm, ute.getUfficioInUso(),titolarioId, utente.getUfficioVOInUso().getAooId());
			}
			titolarioForm.setId(titolarioForm.getTitolario().getId().intValue());
			titolarioForm.setCodice(titolarioForm.getTitolario().getCodice());
			titolarioForm.setDescrizione(titolarioForm.getTitolario().getDescrizione());
			titolarioForm.setMassimario(String.valueOf(titolarioForm.getTitolario().getMassimario()));
			titolarioForm.setParentId(titolarioForm.getTitolario().getParentId());
			TitolarioDelegate td = TitolarioDelegate.getInstance();
			TitolarioVO tPadre = td.getTitolario(titolarioForm.getTitolario()
					.getParentId());
			if (tPadre != null) {
				titolarioForm.setParentPath(tPadre.getPath());
				titolarioForm.setParentDescrizione(tPadre.getDescrizione());
			}
			if (request.getParameter("btnSposta") != null) {
				return mapping.findForward("sposta");
			}
			return mapping.findForward("aggiorna");

		} else if (request.getParameter("btnNuovo") != null) {

			if (titolarioForm.getTitolario() != null) {
				titolarioForm.setParentPath(titolarioForm.getTitolario()
						.getPath());
				titolarioForm.setParentDescrizione(titolarioForm.getTitolario()
						.getDescrizione());
			}
			titolarioForm.inizializzaForm();
			if (titolarioForm.getTitolario() != null) {
				titolarioForm.setParentId(titolarioForm.getTitolario().getId()
						.intValue());
			} else {
				titolarioForm.setParentId(0);
			}
			return mapping.findForward("aggiorna");
		} else if (request.getParameter("btnAnnullaSposta") != null) {
			return mapping.findForward("annulla");
		} else if (request.getParameter("btnConferma") != null) {
			aggiornaTitolarioModel(titolario, titolarioForm, utente);
			TitolarioDelegate td = TitolarioDelegate.getInstance();
			td.salvaArgomento(titolario);
		} else if (request.getParameter("btnConfermaSposta") != null) {
			TitolarioDelegate td = TitolarioDelegate.getInstance();
			titolario = td.getTitolario(titolarioForm.getId());
			titolario.setFullPathDescription(td.getPathName(titolario));
			titolario.setParentId(titolarioForm.getTitolario().getId()
					.intValue());
			td.salvaArgomento(titolario);
			return (mapping.findForward("annulla"));

		} else if (request.getParameter("btnStoria") != null) {
			TitolarioDelegate td = TitolarioDelegate.getInstance();
			Collection<TitolarioVO> storia = new ArrayList<TitolarioVO>();
			storia = td.getStoriaTitolario(titolarioForm.getTitolario().getId()
					.intValue());
			titolarioForm.setStoriaTitolario(storia);
			return (mapping.findForward("storia"));

		} else if (request.getParameter("btnCancella") != null) {
			TitolarioDelegate td = TitolarioDelegate.getInstance();
			if (!td.cancellaArgomento(titolarioForm.getId())) {
				errors.add("argomentoNonCancellabile", new ActionMessage(
						"record_non_cancellabile", " il Titolario", ""));
				saveErrors(request, errors);
				return (mapping.findForward("aggiorna"));
			}
			messages.add("argomentoCancellato", new ActionMessage("cancellazione_ok"));
			saveMessages(request, messages);
			saveErrors(request, errors);
			impostaTitolario(titolarioForm, utente.getRegistroInUso(),
					titolarioForm.getTitolarioPrecedenteId(), utente
							.getUfficioVOInUso().getAooId());
			if (titolarioForm.getTitolario() != null) {
				titolarioForm.setTitolarioPrecedenteId(titolarioForm
						.getTitolario().getParentId());
			}
			return (mapping.findForward("input"));

		} else if (request.getParameter("btnAssocia") != null) {
			TitolarioDelegate td = TitolarioDelegate.getInstance();
			Organizzazione org = Organizzazione.getInstance();
			Ufficio ufficioRoot = org.getAreaOrganizzativa(
					utente.getRegistroVOInUso().getAooId())
					.getUfficioCentrale();
			Ufficio uff = org.getUfficio(ufficioRoot.getValueObject().getId()
					.intValue());
			Collection<Ufficio> uffici = new ArrayList<Ufficio>();
			uffici.add(uff);
			selezionaUffici(uff, uffici);
			titolarioForm.setUfficiDipendenti(uffici);
			titolarioForm
					.setId(titolarioForm.getTitolario().getId().intValue());
			titolarioForm.setCodice(titolarioForm.getTitolario().getCodice());
			titolarioForm.setDescrizione(titolarioForm.getTitolario()
					.getDescrizione());
			titolarioForm.setParentId(titolarioForm.getTitolario()
					.getParentId());
			titolarioForm.setUfficiTitolario(td
					.getUfficiTitolario(titolarioForm.getTitolario().getId()
							.intValue()));
			return (mapping.findForward("associa"));
		} else if (request.getParameter("btnConfermaAssociazione") != null) {
			TitolarioDelegate td = TitolarioDelegate.getInstance();
			int returnValue = td.associaTitolarioUffici(titolarioForm
					.getTitolario(), request
					.getParameterValues("ufficiTitolario"), utente
					.getUfficioVOInUso().getAooId());
			if (returnValue == ReturnValues.SAVED) {
				  messages.add("associazioneUdffici", new ActionMessage("operazione_ok"));
	    			saveMessages(request, messages);

			} else if (returnValue == ReturnValues.UNKNOWN) {
				errors.add("asociazioneUffici", new ActionMessage(
						"errore_nel_salvataggio"));
			} else if (returnValue == ReturnValues.FOUND) {
				errors
						.add(
								"record_non_cancellabile",
								new ActionMessage(
										"record_non_cancellabile",
										"l'associazione titolario uffici.",
										" Il titolario è stato referenziato in Protocolli, Fascicoli, Faldoni Procedimenti "));
			}
			saveErrors(request, errors);
			return (mapping.findForward("input"));
		} else if (request.getParameter("btnAssociaTuttiUffici") != null) {
			TitolarioDelegate td = TitolarioDelegate.getInstance();
			int returnValue = td.associaTuttiGliUfficiTitolario(titolarioForm
					.getTitolario(), utente.getUfficioVOInUso().getAooId());
			if (returnValue == ReturnValues.SAVED) {
				messages.add("associazioneUdffici", new ActionMessage("operazione_ok"));
    			saveMessages(request, messages);
			} else if (returnValue == ReturnValues.UNKNOWN) {
				errors.add("asociazioneUffici", new ActionMessage(
						"errore_nel_salvataggio"));
			} else if (returnValue == ReturnValues.FOUND) {
				errors
						.add(
								"record_non_cancellabile",
								new ActionMessage(
										"record_non_cancellabile",
										"l'associazione titolario uffici.",
										" Il titolario � stato referenziato in Protocolli, Fascicoli, Faldoni Procedimenti "));
			}
			saveErrors(request, errors);
			return (mapping.findForward("input"));
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("edit"));
		}
		logger.info("Execute Titolario");
		if (titolarioForm.getTitolario() != null) {
			impostaTitolario(titolarioForm, utente.getUfficioInUso(),
					titolarioForm.getTitolario().getId().intValue(), utente
							.getUfficioVOInUso().getAooId());
		} else {
			impostaTitolario(titolarioForm, utente.getUfficioInUso(), 0, utente
					.getUfficioVOInUso().getAooId());
		}
		return (mapping.findForward("input"));
	}

	
	private static void aggiornaResponsabileForm(TitolarioForm form) {
		int uffId = form.getTitolario().getResponsabileUfficioId();
		int respId = form.getTitolario().getResponsabileId();
		Organizzazione org = Organizzazione.getInstance();
		Utente ute = null;
		if (respId != 0)
			ute = org.getUtente(respId);
		Ufficio uff = org.getUfficio(uffId);
		form.setResponsabile(newResponsabile(ute, uff));
	}

	public static AssegnatarioView newResponsabile(Utente utente,
			Ufficio ufficio) {
		AssegnatarioView responsabile = new AssegnatarioView();
		UfficioVO uffVO = ufficio.getValueObject();
		responsabile.setUfficioId(uffVO.getId().intValue());
		responsabile.setDescrizioneUfficio(ufficio.getPath());
		responsabile.setNomeUfficio(uffVO.getDescription());
		if (utente != null) {
			UtenteVO uteVO = utente.getValueObject();
			responsabile.setUtenteId(uteVO.getId().intValue());
			responsabile.setNomeUtente(uteVO.getFullName());
		}
		return responsabile;
	}

	private void impostaTitolario(TitolarioForm form, int ufficioId,
			int titolarioId, int aooId) {
		TitolarioDelegate td = TitolarioDelegate.getInstance();
		form.setTitolario(td.getTitolario(titolarioId));
		form.setTitolariFigli(td.getTitolariByParent(titolarioId, aooId));
		if (form.getTitolario() != null)
			if (form.getTitolario().getResponsabileUfficioId() != 0)
				aggiornaResponsabileForm(form);
	}

	private void aggiornaTitolarioModel(TitolarioVO titolarioVO,
			TitolarioForm form, Utente utente) {
		titolarioVO.setId(form.getId());
		titolarioVO.setAooId(utente.getRegistroVOInUso().getAooId());
		titolarioVO.setCodice(form.getCodice());
		titolarioVO.setDescrizione(form.getDescrizione());
		if (form.getMassimario()==null || form.getMassimario().trim().equals(""))
			titolarioVO.setMassimario(0);
		else
			titolarioVO.setMassimario(Integer.parseInt(form.getMassimario()));
		if (form.getGiorniMax()==null || form.getGiorniMax().trim().equals(""))
			titolarioVO.setGiorniMax(0);
		else
			titolarioVO.setGiorniMax(Integer.parseInt(form.getGiorniMax()));
		if (form.getGiorniAlert()==null || form.getGiorniAlert().trim().equals(""))titolarioVO.setGiorniAlert(0);
		else
			titolarioVO.setGiorniAlert(Integer.parseInt(form.getGiorniAlert()));
		aggiornaResponsabile(form, titolarioVO);
		if (titolarioVO.getId().intValue() == 0 && form.getParentId() > 0) {
			titolarioVO.setPath(form.getParentPath() + "."
					+ titolarioVO.getCodice());
		} else {
			if (form.getParentId() > 0) {
				titolarioVO.setPath(form.getParentPath() + "."+ titolarioVO.getCodice());
			} else {
				titolarioVO.setPath(form.getCodice());
			}
		}
		titolarioVO.setParentId(form.getParentId());
		if (form.getTitolario() == null) {
			titolarioVO.setVersione(0);
		} else {
			titolarioVO.setVersione(form.getTitolario().getVersione());
		}

	}

	private static void aggiornaResponsabile(TitolarioForm form, TitolarioVO vo) {
		AssegnatarioView responsabile = form.getResponsabile();
		if (responsabile != null) {
			vo.setResponsabileId(responsabile.getUtenteId());
			vo.setResponsabileUfficioId(responsabile.getUfficioId());
		}
	}

	private void selezionaUffici(Ufficio uff, Collection<Ufficio> uffici) {
		try {
			for (Iterator<Ufficio> i = uff.getUfficiDipendenti().iterator(); i.hasNext();) {
				Ufficio u = i.next();
				selezionaUffici(u, uffici);
				if(u.getValueObject().isAttivo())
					uffici.add(u);
			}
		} catch (Exception de) {
			logger.error("TitolarioAction: failed selezionaUffici: ");
		}
	}

	private void aggiungiAmministrazione(TitolarioForm form, HttpSession session) {
		DestinatarioView amministrazione = new DestinatarioView();
		amministrazione.setFlagTipoDestinatario("G");
		amministrazione.setId(form.getAmministrazioneId());
		amministrazione.setDestinatario(form.getNominativoAmministrazione()
				.trim());
		amministrazione.setIdx(form.getIdx());
		form.aggiungiAmministrazione(amministrazione);
	}

}