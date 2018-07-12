package it.compit.fenice.mvc.presentation.action.protocollo;

import it.compit.fenice.enums.ReturnPageEnum;
import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.AlberoUfficiBO;
import it.finsiel.siged.mvc.bo.TitolarioBO;
import it.finsiel.siged.mvc.business.FascicoloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.FascicoloForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

public class RiassegnaFascicoloAction extends Action {

	static Logger logger = Logger.getLogger(RiassegnaFascicoloAction.class
			.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward actionForward = super.execute(mapping, form, request,
				response);
		if (actionForward != null) {
			return actionForward;
		}
		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession(true);
		FascicoloDelegate delegate = FascicoloDelegate.getInstance();
		CaricaDelegate caricaDelegate = CaricaDelegate.getInstance();
		FascicoloForm fascicoloForm = (FascicoloForm) form;
		boolean ufficioCompleto = true;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		if (form == null) {
			logger.info(" Creating new RiassegnaFascicoloAction");
			form = new FascicoloForm();
			session.setAttribute(mapping.getAttribute(), form);
		}
		getInputPage(request, fascicoloForm);
		if (fascicoloForm.getUfficioCorrenteId() == 0) {
			AlberoUfficiBO.impostaUfficioUtenti(utente, fascicoloForm,
					ufficioCompleto);
			fascicoloForm.setUtenteAbilitatoSuUfficio(utente
					.isUtenteAbilitatoSuUfficio(fascicoloForm
							.getUfficioCorrenteId()));
		} else if (request.getParameter("impostaUfficioAction") != null) {
			fascicoloForm.setUfficioCorrenteId(fascicoloForm
					.getUfficioSelezionatoId());
			AlberoUfficiBO.impostaUfficioUtenti(utente, fascicoloForm,
					ufficioCompleto);
			assegnaAdUfficio(fascicoloForm,fascicoloForm.getUfficioSelezionatoId());
			impostaTitolario(fascicoloForm, utente, 0);
			return (mapping.findForward("input"));
		} else if (request.getParameter("ufficioPrecedenteAction") != null) {
			if (fascicoloForm.getUfficioCorrente().getParentId() > 0
					&& (utente.getUfficioVOInUso().getTipo()
							.equals(UfficioVO.UFFICIO_SEMICENTRALE) || utente
							.isUtenteAbilitatoSuUfficio(fascicoloForm
									.getUfficioCorrente().getParentId()))) {
				fascicoloForm.setUfficioCorrenteId(fascicoloForm
						.getUfficioCorrente().getParentId());
				AlberoUfficiBO.impostaUfficioUtenti(utente, fascicoloForm,
						ufficioCompleto);
				assegnaAdUfficio(fascicoloForm,
						fascicoloForm.getUfficioCorrenteId());
				impostaTitolario(fascicoloForm, utente, 0);
			}
			return (mapping.findForward("input"));
		} else if (request.getParameter("assegnaUtenteAction") != null) {
			assegnaAdUtente(fascicoloForm);
			return mapping.findForward("input");
		} else if (request.getParameter("impostaTitolarioAction") != null) {
			impostaTitolario(fascicoloForm, utente,
					fascicoloForm.getTitolarioSelezionatoId());
			return (mapping.findForward("input"));
		} else if (request.getParameter("titolarioPrecedenteAction") != null) {
			impostaTitolario(fascicoloForm, utente,
					fascicoloForm.getTitolarioPrecedenteId());
			fascicoloForm.setTitolario(null);

			return (mapping.findForward("input"));
		}
		
		else if (request.getParameter("riassegnaAction") != null) {
			errors = fascicoloForm.validateRiassegna(mapping, request);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return (mapping.findForward("input"));
			}
			String[] ids = (String[]) session
					.getAttribute("fascicoloIdSelezionati");

			for (String id : ids) {
				Integer fascicoloId = Integer.valueOf(id);
				FascicoloVO fascicolo = delegate
						.getFascicoloVOById(fascicoloId);
				if (fascicoloForm.getTitolario() != null) {
					fascicolo.setTitolarioId(fascicoloForm.getTitolario()
							.getId().intValue());
				}
				fascicolo.setUfficioResponsabileId(fascicoloForm.getUfficioCorrenteId());
				fascicolo.setUfficioIntestatarioId(fascicoloForm.getUfficioCorrenteId());
				fascicolo.setUtenteIntestatarioId(fascicoloForm.getUtenteSelezionatoId());
				if (fascicoloForm.getUtenteSelezionatoId() != 0) {
					CaricaVO car = caricaDelegate.getCaricaByUtenteAndUfficio(
							fascicoloForm.getUtenteSelezionatoId(),
							fascicoloForm.getUfficioCorrenteId());
					fascicolo.setCaricaIntestatarioId(car.getCaricaId());
				}
				
				
				fascicolo.setUtenteIstruttoreId(0);
				fascicolo.setCaricaIstruttoreId(0);
				delegate.salvaFascicolo(fascicolo);
			}
			session.removeAttribute("fascicoloIdSelezionati");

			if (fascicoloForm.getInputPage() == ReturnPageEnum.OFFICE_PANEL) {
				return (mapping.findForward("cruscottoStruttura"));
			} else if (fascicoloForm.getInputPage() == ReturnPageEnum.ADMINISTRATION_PANEL) {
				return (mapping.findForward("cruscottoAmministrazione"));
			}

			return (mapping.findForward("input"));
		}
		if (request.getParameter("indietroAction") != null) {
			if (fascicoloForm.getInputPage() == ReturnPageEnum.OFFICE_PANEL) {
				return (mapping.findForward("cruscottoStruttura"));
			} else if (fascicoloForm.getInputPage() == ReturnPageEnum.ADMINISTRATION_PANEL) {
				return (mapping.findForward("cruscottoAmministrazione"));
			}
		}

		return mapping.findForward("input");

	}

	private static void getInputPage(HttpServletRequest request,
			FascicoloForm form) {
		if (request.getParameter("CRUSCOTTO_STRUTTURA") != null)
			form.setInputPage(ReturnPageEnum.OFFICE_PANEL);
		else if (request.getParameter("CRUSCOTTO_AMMINISTRAZIONE") != null)
			form.setInputPage(ReturnPageEnum.ADMINISTRATION_PANEL);
		else if (request.getParameter("resetIstruttoreStrutturaAction") != null)
			form.setInputPage(ReturnPageEnum.OFFICE_PANEL);
		else if (request.getParameter("resetIstruttoreAmministrazioneAction") != null)
			form.setInputPage(ReturnPageEnum.ADMINISTRATION_PANEL);
	}

	private void impostaTitolario(FascicoloForm form, Utente utente,
			int titolarioId) {
		int ufficioId = form.getUfficioCorrenteId();
		TitolarioBO.impostaTitolario(form, ufficioId, titolarioId);
		if (form.getTitolario() != null) {
			form.setGiorniAlert(String.valueOf(form.getTitolario()
					.getGiorniAlert()));
			form.setGiorniMax(String
					.valueOf(form.getTitolario().getGiorniMax()));
		} else {
			form.setGiorniAlert(null);
			form.setGiorniMax(null);
		}
	}

	protected void assegnaAdUfficio(FascicoloForm form, int ufficioId) {
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(ufficioId);
		Organizzazione org = Organizzazione.getInstance();
		Ufficio uff = org.getUfficio(ufficioId);
		ass.setNomeUfficio(uff.getValueObject().getDescription());
		ass.setDescrizioneUfficio(uff.getPath());
		form.setMittente(ass);
	}

	protected void assegnaAdUtente(FascicoloForm form) {
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(form.getUfficioCorrenteId());
		ass.setNomeUfficio(form.getUfficioCorrente().getDescription());
		ass.setDescrizioneUfficio(form.getUfficioCorrentePath());
		ass.setUtenteId(form.getUtenteSelezionatoId());
		UtenteVO ute = form.getUtente(form.getUtenteSelezionatoId());
		ass.setNomeUtente(ute.getFullName());
		form.setMittente(ass);
	}

}
