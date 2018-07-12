package it.compit.fenice.mvc.presentation.action.amministrazione.org.utenti;

import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.presentation.actionform.amministrazione.org.utenti.CaricaForm;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.compit.fenice.mvc.vo.organizzazione.StoricoOrganigrammaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.Menu;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.OrganizzazioneBO;
import it.finsiel.siged.mvc.business.AmministrazioneDelegate;
import it.finsiel.siged.mvc.business.AreaOrganizzativaDelegate;
import it.finsiel.siged.mvc.business.UfficioDelegate;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.org.utenti.ProfiloUtenteForm;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;

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

public class CaricaAction extends Action {

	static Logger logger = Logger.getLogger(CaricaAction.class.getName());

	public void addStorico(Utente utente, String descrizioneModifica){
		StoricoOrganigrammaVO storgVO = new StoricoOrganigrammaVO();
		OrganizzazioneBO.setOrganigramma(Organizzazione.getInstance(), storgVO, utente, descrizioneModifica);
		UfficioDelegate.getInstance().salvaStoricoOrganigramma(storgVO);
	}
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession();
		CaricaForm cForm = (CaricaForm) form;
		CaricaVO caricaVO = new CaricaVO();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		int aooId = utente.getAreaOrganizzativa().getId();
		CaricaDelegate delegate = CaricaDelegate.getInstance();
		String desc=null;
		if (form == null) {
			form = new ProfiloUtenteForm();
			request.setAttribute(mapping.getAttribute(), form);
		} if (request.getParameter("btnNuovaCarica") != null) {
			cForm.inizializzaForm();
		} if (request.getParameter("btnDisattiva") != null) {
			Integer caricaId = Integer.valueOf(request.getParameter("parId"));
			caricaVO = CaricaDelegate.getInstance().getCarica(caricaId);
			int numberOfYear=Integer.valueOf(utente.getAreaOrganizzativa().getAnniVisibilitaBacheca());
			GregorianCalendar today = new GregorianCalendar();
			if (CaricaDelegate.getInstance().contaAssegnazioni(caricaVO, today
					.get(Calendar.YEAR) - numberOfYear, today.get(Calendar.YEAR),utente) == 0) {
				caricaVO.setAttivo(false);
				caricaVO=delegate.updateCarica(caricaVO);
				if(caricaVO.getReturnValue()==ReturnValues.SAVED){
					desc="Disattivata la carica "+caricaVO.getNome()+" dell'ufficio "+caricaVO.getPathUfficio();
					addStorico(utente, desc);
				}
			} else {
				errors.add("generale", new ActionMessage(
						"carica.assegnazioni_aperte"));
			}
			if (!errors.isEmpty())
				saveErrors(request, errors);
			session.setAttribute("ufficioId", caricaVO.getUfficioId());
			return mapping.findForward("success");
		} if (request.getParameter("btnAttiva") != null) {
			Integer caricaId = Integer.valueOf(request.getParameter("parId"));
			caricaVO = CaricaDelegate.getInstance().getCarica(caricaId);
			caricaVO.setAttivo(true);
			delegate.updateCarica(caricaVO);
			if(caricaVO.getReturnValue()==ReturnValues.SAVED){
				desc="Attivata la carica "+caricaVO.getNome()+" dell'ufficio "+caricaVO.getPathUfficio();
				addStorico(utente, desc);
			}
			session.setAttribute("ufficioId", caricaVO.getUfficioId());
			return mapping.findForward("success");
		} if (request.getParameter("btnRemoveResponsabileEnte") != null) {
			boolean removed=delegate.removeResponsabileEnte();
			if(removed){
				desc="Rimosso responsabile dell'ente";
				addStorico(utente, desc);
				cForm.setResponsabileEnte(false);
			} 
			return mapping.findForward("input");
		} if (request.getParameter("btnSetResponsabileEnte") != null) {
			boolean removed=delegate.removeResponsabileEnte();
			if(removed){
				desc="Rimosso responsabile dell'ente";
				addStorico(utente, desc);
				caricaVO=delegate.setResponsabileEnte(cForm.getCaricaId());
				if(caricaVO.getReturnValue()==ReturnValues.SAVED){
					desc="la carica "+caricaVO.getNome()+" dell'ufficio "+caricaVO.getPathUfficio()+" è responsabile dell'ente";
					addStorico(utente, desc);
					cForm.setResponsabileEnte(true);
				}
			}
			return mapping.findForward("input");
		} 
		/*
		if (request.getParameter("btnRemoveResponsabileProtocollo") != null) {
			boolean removed=delegate.removeResponsabileProtocollo();
			if(removed){
				desc="Rimosso responsabile dell'ufficio protocollo";
				addStorico(utente, desc);
				cForm.setResponsabileUfficioProtocollo(false);
			} 
			return mapping.findForward("input");
		} 
		if (request.getParameter("btnSetResponsabileProtocollo") != null) {
			boolean removed=delegate.removeResponsabileProtocollo();
			if(removed){
				desc="Rimosso responsabile dell'ufficio protocollo";
				addStorico(utente, desc);
				caricaVO=delegate.setResponsabileProtocollo(cForm.getCaricaId());
				if(caricaVO.getReturnValue()==ReturnValues.SAVED){
					desc="la carica "+caricaVO.getNome()+" dell'ufficio "+caricaVO.getPathUfficio()+" è responsabile dell'ufficio di protocollo";
					addStorico(utente, desc);
					cForm.setResponsabileUfficioProtocollo(true);
				}
			}
			return mapping.findForward("input");
		}
		*/ 
		if (request.getParameter("parId") != null) {
			int id = NumberUtil.getInt(request.getParameter("parId"));
			caricaVO = CaricaDelegate.getInstance().getCarica(id);
			if (caricaVO != null
					&& caricaVO.getReturnValue() == ReturnValues.FOUND) {
				caricaDatiNelForm(cForm, caricaVO, utente);
			} else {
				logger.warn("Carica non trovata. id=" + id);
			}
			request.setAttribute(mapping.getAttribute(), cForm);
			return mapping.findForward("input");
		} if (cForm.getUfficioId() == 0) {
			Integer ufficioId = (Integer) session.getAttribute("ufficioId");
			cForm.setUfficioId(ufficioId);
			impostaUfficioUtenti(cForm);
			cForm.setProfili(AmministrazioneDelegate.getInstance().getProfili(
					utente.getRegistroVOInUso().getAooId()));
			cForm.setUtenti(AreaOrganizzativaDelegate.getInstance()
					.getUtentiCarica(aooId));
		} else if (request.getParameter("btnSalva") != null) {
			if (cForm.getNome() == null || "".equals(cForm.getNome())) {
				errors.add("Denominazione", new ActionMessage(
						"campo.obbligatorio", "Denominazione", ""));
			}
			if (cForm.getProfiloSelezionatoId() == 0) {
				errors.add("Profilo", new ActionMessage("campo.obbligatorio",
						"Profilo", ""));
			}
			if (cForm.getUfficioId() == 0) {
				errors.add("generale", new ActionMessage(
						"errore_nel_salvataggio"));
			}
			if (cForm.getUtenteSelezionatoId() != 0) {
				Collection<CaricaVO> listC = Organizzazione.getInstance().getCariche();
				for (CaricaVO c : listC) {
					if (cForm.getUtenteSelezionatoId() == c.getUtenteId()
							&& cForm.getCaricaId() != c.getCaricaId()
							&& cForm.getUfficioId() == c.getUfficioId())
						errors.add("generale", new ActionMessage(
								"carica.utente_presente"));
				}

			}
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return mapping.findForward("input");
			}
			caricaDatiNelVO(caricaVO, cForm, utente);
			if (cForm.getCaricaId() == 0) {
				Collection<String> cMenu = new ArrayList<String>();
				cMenu = AmministrazioneDelegate.getInstance().getMenuByProfilo(
						cForm.getProfiloId());
				String[] aMenu = new String[cMenu.size()];
				cMenu.toArray(aMenu);
				caricaVO = CaricaDelegate.getInstance().newCarica(caricaVO,
						impostaFunzioniMenu(aMenu),
						cForm.getUfficioCorrente().getDescription(),
						cForm.getUfficioCorrente().getAooId());
				if (caricaVO.getReturnValue() != ReturnValues.SAVED) {
					errors.add("generale", new ActionMessage(
							"errore_nel_salvataggio"));
				}else{
					desc="Creata la carica "+cForm.getNome()+" appartentente all'ufficio "+cForm.getPathUfficio();
					addStorico(utente, desc);
				}

			} else {
				Collection<String> cMenu = new ArrayList<String>();
				cMenu = AmministrazioneDelegate.getInstance().getMenuByProfilo(
						cForm.getProfiloId());
				String[] aMenu = new String[cMenu.size()];
				cMenu.toArray(aMenu);
				caricaVO = CaricaDelegate.getInstance().updateCarica(caricaVO,
						impostaFunzioniMenu(aMenu),
						cForm.getUfficioCorrente().getDescription(),
						cForm.getUfficioCorrente().getAooId());
				if (caricaVO.getReturnValue() != ReturnValues.SAVED) {
					errors.add("generale", new ActionMessage(
							"errore_nel_salvataggio"));
				}else{
					if(desc==null)
						desc="Modificata la carica "+cForm.getNome()+" appartentente all'ufficio "+cForm.getPathUfficio();
					addStorico(utente, desc);
				}
			}
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return mapping.findForward("input");
			}
			session.setAttribute("ufficioId", cForm.getUfficioId());
			return mapping.findForward("success");
		} else if (request.getParameter("btnStoria") != null) {
			cForm.setVersioniCarica(CaricaDelegate.getInstance().getStoriaCarica(cForm.getCaricaId()));
			return mapping.findForward("storia");
		} else if (request.getParameter("btnIndietroStoria") != null) {
			return mapping.findForward("input");
		}  else if (request.getParameter("selectProfiloAction") != null) {
			cForm.setProfiloSelezionatoId(cForm.getProfiloId());
		} else if (request.getParameter("selectUtenteAction") != null) {
			if(cForm.getUtenteSelezionatoId()!=cForm.getUtenteId())
				desc="Modificato l'utente della carica "+cForm.getNome()+" appartenente all'ufficio "+cForm.getPathUfficio();
			cForm.setUtenteSelezionatoId(cForm.getUtenteId());
		} else if (request.getParameter("removeUtenteAction") != null) {
			desc="Rimosso l'utente della carica "+cForm.getNome()+" appartenente all'ufficio "+cForm.getPathUfficio();
			cForm.setUtenteSelezionatoId(0);
		} else if (request.getParameter("btnIndietro") != null) {
			session.setAttribute("ufficioId", cForm.getUfficioId());
			return mapping.findForward("success");
		} else if (request.getParameter("btnAnnulla") != null) {
			cForm.inizializzaForm();
			cForm.setProfili(AmministrazioneDelegate.getInstance().getProfili(utente.getRegistroVOInUso().getAooId()));
		}
		return (mapping.findForward("input"));

	}

	protected void rimuoviUtente(CaricaForm form) {
		form.setUtente(null);
		form.setUtenteSelezionatoId(0);
	}

	public void caricaDatiNelVO(CaricaVO vo, CaricaForm form, Utente utente) {
		vo.setCaricaId(form.getCaricaId());
		vo.setNome(form.getNome());
		vo.setProfiloId(form.getProfilo().getId());
		vo.setUfficioId(form.getUfficioId());
		vo.setAttivo(form.isAttivo());
		vo.setResponsabileEnte(form.isResponsabileEnte());
		vo.setResponsabileUfficioProtocollo(form.isResponsabileUfficioProtocollo());
		if (form.getUtente() != null)
			vo.setUtenteId(form.getUtente().getId());
		vo.setRowCreatedUser(utente.getValueObject().getUsername());
	}

	public void caricaDatiNelForm(CaricaForm form, CaricaVO vo, Utente utente) {
		int aooId = utente.getAreaOrganizzativa().getId();
		form.setCaricaId(vo.getCaricaId().intValue());
		form.setProfiloId(vo.getProfiloId());
		form.setUtenteId(vo.getUtenteId());
		form.setUfficioId(vo.getUfficioId());
		form.setVersione(vo.getVersione());
		form.setReferente(vo.isReferente());
		form.setAutore(vo.getRowCreatedUser());
		form.setDataOperazione(DateUtil.formattaDataOra(vo.getRowCreatedTime().getTime()));
		form.setNome(vo.getNome());
		form.setAttivo(vo.isAttivo());
		form.setResponsabileEnte(vo.isResponsabileEnte());
		form.setResponsabileUfficioProtocollo(vo.isResponsabileUfficioProtocollo());
		form.setProfili(AmministrazioneDelegate.getInstance().getProfili(
				utente.getRegistroVOInUso().getAooId()));
		impostaUfficioUtenti(form);
		form
				.setUtenti(AreaOrganizzativaDelegate.getInstance().getUtenti(
						aooId));
		form.setProfiloSelezionatoId(form.getProfiloId());
		form.setUtenteSelezionatoId(form.getUtenteId());

	}

	private String[] impostaFunzioniMenu(String[] funzioniSelezionate) {
		Organizzazione org = Organizzazione.getInstance();
		HashMap<Integer,String> mappaFunzioni = new HashMap<Integer,String>();
		if (funzioniSelezionate != null) {
			for (int i = 0; i < funzioniSelezionate.length; i++) {
				Integer funzione = new Integer(funzioniSelezionate[i]);
				if (funzione != null) {
					Menu menu = org.getMenu(funzione.intValue());
					while (menu.getValueObject().getId().intValue() > 0) {
						mappaFunzioni.put(menu.getValueObject().getId(), menu
								.getValueObject().getId().toString());
						menu = menu.getParent();
					}
				}
			}
		}
		Collection<String> cMenu = new ArrayList<String>();
		cMenu = mappaFunzioni.values();
		String[] aMenu = new String[cMenu.size()];
		cMenu.toArray(aMenu);
		return aMenu;
	}

	public static void impostaUfficioUtenti(CaricaForm form) {
		Organizzazione org = Organizzazione.getInstance();
		Ufficio ufficioCorrente = org.getUfficio(form.getUfficioId());
		// ufficioCorrente.get
		form.setUfficioCorrente(ufficioCorrente.getValueObject());
	}
}
