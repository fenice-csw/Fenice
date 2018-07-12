package it.compit.fenice.mvc.presentation.action.amministrazione;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.AreaOrganizzativa;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.flosslab.mvc.business.OggettarioDelegate;
import it.flosslab.mvc.presentation.action.amministrazione.OggettarioAction;
import it.flosslab.mvc.presentation.actionform.amministrazione.OggettiActionForm;
import it.flosslab.mvc.vo.OggettoVO;

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

public class OggettoAction extends Action {

	static Logger logger = Logger.getLogger(OggettarioAction.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		
		OggettiActionForm oForm = (OggettiActionForm) form;
		if (form == null) {
			logger.info("Creating new Oggetto Form");
			form = new OggettiActionForm();
			session.setAttribute(mapping.getAttribute(), form);
		}
		if (oForm.getUfficioCorrenteId() == 0) {
			impostaUfficio(utente, oForm);
		}
		if (request.getParameter("rimuoviAssegnatariAction") != null) {
			rimuoviAssegnatari(oForm);
			return mapping.findForward("input");
		} else if (request.getParameter("impostaUfficioAction") != null) {
			oForm.setUfficioCorrenteId(oForm.getUfficioSelezionatoId());
			impostaUfficio(utente, oForm);
			return mapping.findForward("input");
		} else if (request.getParameter("ufficioPrecedenteAction") != null) {
			oForm
					.setUfficioCorrenteId(oForm.getUfficioCorrente()
							.getParentId());
			impostaUfficio(utente, oForm);
			return mapping.findForward("input");
		} else if (request.getParameter("assegnaUfficioCorrenteAction") != null) {
			aggiungiAssegnatario(oForm, oForm.getUfficioCorrenteId());
			impostaUfficio(utente, oForm);
			return mapping.findForward("input");
		} else if (request.getParameter("salvaAction") != null) {
			
			
			OggettoVO oggetto = aggiornaOggettoModel(oForm,utente.getAreaOrganizzativa().getId());
			try {
				OggettarioDelegate delegate = OggettarioDelegate.getInstance();
				if (delegate.isDescrizioneUsed(oggetto)) {
					errors.add("descrizione", new ActionMessage("oggetto.descrizione_usata"));
					saveErrors(request, errors);
					return mapping.findForward("input");
				} else {
					OggettarioDelegate.getInstance().salvaOggetto(oggetto);
					messages.add("msg", new ActionMessage("operazione_ok"));
					saveMessages(request, messages);
				}
				form = new OggettiActionForm();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mapping.findForward("success");
		} else if (request.getParameter("indietroAction") != null) {
			return mapping.findForward("success");
		}
		return mapping.findForward("input");
	}

	private void rimuoviAssegnatari(OggettiActionForm form) {
		String[] assegnatari = form.getAssegnatariSelezionatiId();
		if (assegnatari != null) {
			for (int i = 0; i < assegnatari.length; i++) {
				String assegnatario = assegnatari[i];
				if (assegnatario != null)
					form.rimuoviAssegnatario(assegnatario);
			}
		}
	}

	private void aggiungiAssegnatario(OggettiActionForm form, int ufficioId) {
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(ufficioId);
		Organizzazione org = Organizzazione.getInstance();
		Ufficio uff = org.getUfficio(ufficioId);
		ass.setNomeUfficio(uff.getValueObject().getDescription());
		ass.setDescrizioneUfficio(uff.getPath());
		form.aggiungiAssegnatario(ass);
		form.setUfficioSelezionatoId(0);

	}

	private static void impostaUfficio(Utente utente, OggettiActionForm form) {
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

	private OggettoVO aggiornaOggettoModel(OggettiActionForm form,int aooId) {
		OggettoVO oggetto = null;
		oggetto = new OggettoVO(form.getOggettoId(), form.getDescrizione());
		if(form.getGiorniAlert()!=null && !form.getGiorniAlert().trim().equals(""))
			oggetto.setGiorniAlert(Integer.valueOf(form.getGiorniAlert()));
		aggiornaAssegnatariModel(form, oggetto);
		oggetto.setAooId(aooId);
		return oggetto;
	}

	private static void aggiornaAssegnatariModel(OggettiActionForm form,
			OggettoVO vo) {
		vo.removeAssegnatari();
		Collection<AssegnatarioView> assegnatari = form.getAssegnatari();
		if (assegnatari != null) {
			for (AssegnatarioView ass : assegnatari) {
				AssegnatarioVO assegnatario = new AssegnatarioVO();
				assegnatario.setUfficioAssegnatarioId(ass.getUfficioId());
				vo.aggiungiAssegnatario(assegnatario);

			}
		}

	}

}
