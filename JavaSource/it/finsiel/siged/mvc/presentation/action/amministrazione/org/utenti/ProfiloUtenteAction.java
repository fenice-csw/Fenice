package it.finsiel.siged.mvc.presentation.action.amministrazione.org.utenti;

import it.compit.fenice.enums.UnitaAmministrativaEnum;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Menu;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.AlberoUfficiBO;
import it.finsiel.siged.mvc.bo.MenuBO;
import it.finsiel.siged.mvc.bo.RegistroBO;
import it.finsiel.siged.mvc.business.AmministrazioneDelegate;
import it.finsiel.siged.mvc.business.OrganizzazioneDelegate;
import it.finsiel.siged.mvc.business.RegistroDelegate;
import it.finsiel.siged.mvc.business.UtenteDelegate;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.org.utenti.ProfiloUtenteForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.presentation.helper.MenuView;
import it.finsiel.siged.mvc.vo.organizzazione.MenuVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

public class ProfiloUtenteAction extends Action {

	// ----------------------------------------------------- Instance Variables

	/**
	 * The <code>Log</code> instance for this application.
	 */
	static Logger logger = Logger
			.getLogger(ProfiloUtenteAction.class.getName());

	// --------------------------------------------------------- Public Methods

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();

		HttpSession session = request.getSession();
		ProfiloUtenteForm profiloForm = (ProfiloUtenteForm) form;
		UtenteVO utenteVO = new UtenteVO();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);

		if (form == null) {
			form = new ProfiloUtenteForm();
			request.setAttribute(mapping.getAttribute(), form);
		}
		if (profiloForm.getUfficioCorrenteId() == 0) {
			AlberoUfficiBO.impostaUfficio(utente, profiloForm, true);
		}

		String parId = request.getParameter("parId");
		if (parId != null) {
			if (NumberUtil.isInteger(parId)) {
				int id = NumberUtil.getInt(parId);
				utenteVO = UtenteDelegate.getInstance().getUtente(id);
				if (utenteVO != null
						&& utenteVO.getReturnValue() == ReturnValues.FOUND) {
					caricaDatiNelForm(profiloForm, utenteVO, utente);
				} else {
					logger.warn("Utente non trovato. id=" + parId);
				}
				request.setAttribute(mapping.getAttribute(), profiloForm);
				return mapping.findForward("input");
			} else {
				logger.warn("Utente non trovato. id=" + parId);
			}
		} 
		else if (request.getParameter("btnSalva") != null) {
			// validazione dei dati
			errors = profiloForm.validate(mapping, request);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return mapping.findForward("input");
			}

			try {
				if (UtenteDelegate.getInstance().isUsernameUsed(
						profiloForm.getUserName(), profiloForm.getId())) {
					errors.add("userName", new ActionMessage(
							"username_gia_utilizzato"));
					saveErrors(request, errors);
					return mapping.findForward("input");
				}
			} catch (Exception e) {
				logger.error("", e);
				errors.add("generale", new ActionMessage(
						"errore_nel_salvataggio"));
				saveErrors(request, errors);
				return mapping.findForward("input");
			}
			// fine validazioni

			caricaDatiNelVO(utenteVO, profiloForm, utente);
			String[] aRegistri = profiloForm.getRegistriSelezionatiId();
			if (profiloForm.getId() == 0) {
				utenteVO.setRowCreatedUser(utente.getValueObject()
						.getUsername());

				utenteVO = UtenteDelegate.getInstance().newUtenteVO(utenteVO,
						aRegistri, utente);
			} else {
				// update
				utenteVO.setRowUpdatedUser(utente.getValueObject()
						.getUsername());
				utenteVO = UtenteDelegate.getInstance().updateUtenteVO(
						utenteVO, aRegistri, utente);
				if (utenteVO.getReturnValue() != ReturnValues.SAVED) {
					errors.add("generale", new ActionMessage(
							"errore_nel_salvataggio"));
				}
			}

			if (utenteVO.getReturnValue() == ReturnValues.SAVED) {
				aggiornaUtenteOrganizzazione(utenteVO);
				// profiloForm.inizializzaForm();
				// profiloForm.

				return mapping.findForward("success");
			}

			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return mapping.findForward("input");
			}
		} else if (request.getParameter("btnStoriaCariche") != null) {
			profiloForm.setVersioniCarica(UtenteDelegate.getInstance().getStoriaCarica(profiloForm.getId()));
			return mapping.findForward("storia");
		} else if (request.getParameter("btnIndietroStoria") != null) {
			return mapping.findForward("input");
		} 
		else if (request.getParameter("impostaUfficioAction") != null) {
			profiloForm.setUfficioCorrenteId(profiloForm
					.getUfficioSelezionatoId());
			AlberoUfficiBO.impostaUfficio(utente, profiloForm, true);

		} else if (request.getParameter("ufficioPrecedenteAction") != null) {
			profiloForm.setUfficioCorrenteId(profiloForm.getUfficioCorrente()
					.getParentId());
			AlberoUfficiBO.impostaUfficio(utente, profiloForm, true);

		} else if (request.getParameter("assegnaUfficioCorrenteAction") != null) {
			assegnaAdUfficio(profiloForm, profiloForm.getUfficioCorrenteId());
			return mapping.findForward("input");
		} else if (request.getParameter("rimuoviAssegnatariAction") != null) {
			rimuoviUffici(profiloForm);

		} else if (request.getParameter("assegnaUfficioSelezionatoAction") != null) {
			assegnaAdUfficio(profiloForm, profiloForm.getUfficioSelezionatoId());
			return mapping.findForward("input");
		} else if (request.getParameter("btnIndietro") != null) {
			profiloForm.inizializzaForm();
			return (mapping.findForward("indietro"));

		} else if (request.getParameter("btnAnnulla") != null) {
			profiloForm.inizializzaForm();
			profiloForm.setProfiliMenu(AmministrazioneDelegate.getInstance()
					.getProfili(utente.getRegistroVOInUso().getAooId()));
			profiloForm
					.setRegistri(getRegistri(RegistroDelegate.getInstance()
							.getRegistriByAooId(
									utente.getRegistroVOInUso().getAooId())));

			AlberoUfficiBO.impostaUfficio(utente, profiloForm, true);

		} else if (request.getParameter("btnCancella") != null) {
			if (profiloForm.getUfficiAssegnati().size() != 0) {
				UtenteDelegate.getInstance()
						.cancellaUtente(profiloForm.getId());
				// profiloForm.inizializzaForm();
				return mapping.findForward("success");
			} else {
				errors.add("utente_non_cancellabile", new ActionMessage(
						"errore_nel_salvataggio"));
				saveErrors(request, errors);
				return mapping.findForward("input");
			}

		} else if (request.getParameter("btnPermessiUffici") != null) {
			Organizzazione org = Organizzazione.getInstance();
			int utenteId = profiloForm.getId();
			int ufficioCorrenteId;
			if (request.getParameter("ufficioId") != null) {
				ufficioCorrenteId = new Integer(request
						.getParameter("ufficioId")).intValue();
			} else {
				ufficioCorrenteId = profiloForm.getUfficioCorrenteId();
			}
			profiloForm.setUfficioCorrenteId(ufficioCorrenteId);
			profiloForm.setUfficioCorrentePath(org
					.getUfficio(ufficioCorrenteId).getPath());
			profiloForm.setFunzioniMenuSelezionate(UtenteDelegate.getInstance()
					.getFunzioniByUfficioUtente(utenteId, ufficioCorrenteId));
			Collection<MenuView> menuLista = new ArrayList<MenuView>();
			Menu rootMenu = (Menu) getServlet().getServletContext()
					.getAttribute(Constants.MENU_ROOT);
			MenuBO.aggiungiMenu("", rootMenu, menuLista);

			profiloForm.setFunzioniMenu(menuLista);
			return (mapping.findForward("permessi"));

		} 
		else if (request.getParameter("btnAbilitaTutto") != null
				|| request.getParameter("assegnaAmministrazione") != null
				|| request.getParameter("assegnaDocumentale") != null
				|| request.getParameter("assegnaReport") != null
				|| request.getParameter("assegnaProtocollazione") != null) {
			int utenteId = profiloForm.getId();
			int ufficioCorrenteId = profiloForm.getUfficioCorrenteId();
			Collection<MenuVO> funzioni = new ArrayList<MenuVO>();
			if (request.getParameter("assegnaAmministrazione") != null) {
				funzioni = AmministrazioneDelegate.getInstance()
						.getFunzioniAmministrazioneMenu();

			} else if (request.getParameter("assegnaDocumentale") != null) {
				funzioni = AmministrazioneDelegate.getInstance()
						.getFunzioniDocumentaleMenu();

			} else if (request.getParameter("assegnaReport") != null) {
				funzioni = AmministrazioneDelegate.getInstance()
						.getFunzioniReportMenu();

			} else if (request.getParameter("assegnaProtocollazione") != null) {
				funzioni = AmministrazioneDelegate.getInstance()
						.getFunzioniProtocollazioneMenu();

			} else {
				funzioni = AmministrazioneDelegate.getInstance()
						.getFunzioniMenu();
			}

			int i = 0;
			String[] aFunzioni = new String[funzioni.size()];
			for (Iterator<MenuVO> iter = funzioni.iterator(); iter.hasNext();) {
				MenuVO element =  iter.next();
				aFunzioni[i++] = (element.getId()).toString();
			}
			Organizzazione org = Organizzazione.getInstance();
			if (request.getParameter("ufficioId") != null) {
				ufficioCorrenteId = new Integer(request
						.getParameter("ufficioId")).intValue();
			} else {
				ufficioCorrenteId = profiloForm.getUfficioCorrenteId();
			}
			profiloForm.setUfficioCorrenteId(ufficioCorrenteId);
			profiloForm.setUfficioCorrentePath(org
					.getUfficio(ufficioCorrenteId).getPath());
			profiloForm.setFunzioniMenuSelezionate(UtenteDelegate.getInstance()
					.getFunzioniByUfficioUtente(utenteId, ufficioCorrenteId));
			Collection<MenuView> menuLista = new ArrayList<MenuView>();
			addPermessiToForm(profiloForm, funzioni);
			Menu rootMenu = (Menu) getServlet().getServletContext()
					.getAttribute(Constants.MENU_ROOT);
			MenuBO.aggiungiMenu("", rootMenu, menuLista);
			aggiornaForm(profiloForm, funzioni);

			profiloForm.setUfficiAssegnati(UtenteDelegate.getInstance()
					.getUfficiByUtente(utenteId));
			return mapping.findForward("permessi");

		} 
		return (mapping.findForward("input"));

	}

	private void addPermessiToForm(ProfiloUtenteForm profiloForm,
			Collection<MenuVO> funzioni) {
		Set<String> permessiSet = new HashSet<String>();
		ArrayList<String> toAdd = new ArrayList<String>();
		for (Iterator<MenuVO> iter = funzioni.iterator(); iter.hasNext();) {
			MenuVO element = iter.next();
			toAdd.add((element.getId()).toString());
		}
		for (String elemento : profiloForm.getFunzioniMenuSelezionate()) {
			permessiSet.add(elemento);
		}
		permessiSet.addAll(toAdd);
		String[] permessiNew = new String[permessiSet.size()];

		permessiSet.toArray(permessiNew);
		profiloForm.setFunzioniMenuSelezionate(permessiNew);

	}

	private void aggiornaForm(ProfiloUtenteForm profiloForm, Collection<MenuVO> funzioni) {
		Set<String> permessiSet = new HashSet<String>();
		ArrayList<String> toAdd = new ArrayList<String>();
		for (Iterator<MenuVO> iter = funzioni.iterator(); iter.hasNext();) {
			MenuVO element = iter.next();
			toAdd.add((element.getId()).toString());
		}
		for (Object elemento : profiloForm.getFunzioniMenuSelezionate()) {
			permessiSet.add((String) elemento);
		}
		permessiSet.addAll(toAdd);
		profiloForm.setFunzioniMenu(permessiSet);

	}

	public void caricaDatiNelVO(UtenteVO vo, ProfiloUtenteForm form,
			Utente utente) {
		vo.setId(form.getId());
		vo.setUsername(form.getUserName());
		vo.setPassword(form.getPassWord());
		vo.setCognome(form.getCognome());
		vo.setNome(form.getNome());
		vo.setCodiceFiscale(form.getCodiceFiscale());
		vo.setEmailAddress(form.getEmailAddress());
		vo.setMatricola(form.getMatricola());
		vo.setAbilitato(form.getAbilitato());
		vo.setDataFineAttivita(DateUtil.toDate(form.getDataFineAttivita()));
		vo.setAooId(utente.getValueObject().getAooId());
	}

	public void caricaDatiNelForm(ProfiloUtenteForm form, UtenteVO vo,
			Utente utente) {
		form.setUserName(vo.getUsername());
		form.setId(vo.getId().intValue());
		form.setPassWord(vo.getPassword());
		form.setConfermaPassword(vo.getPassword());
		form.setCognome(vo.getCognome());
		form.setNome(vo.getNome());
		form.setCodiceFiscale(vo.getCodiceFiscale());
		form.setEmailAddress(vo.getEmailAddress());
		form.setMatricola(vo.getMatricola());
		form.setAbilitato(vo.isAbilitato());
		if (vo.getDataFineAttivita() != null)
			form.setDataFineAttivita(DateUtil.formattaData(vo
					.getDataFineAttivita().getTime()));
		String[] permessiRegistri = UtenteDelegate.getInstance()
				.getPermessiRegistri(vo.getId().intValue());
		if (permessiRegistri != null && permessiRegistri.length > 0) {
			form.setRegistriSelezionatiId(UtenteDelegate.getInstance()
					.getPermessiRegistri(vo.getId().intValue()));
		}
		form.setProfiliMenu(AmministrazioneDelegate.getInstance().getProfili(
				utente.getRegistroVOInUso().getAooId()));
		form.setUfficiAssegnati(UtenteDelegate.getInstance().getUfficiByUtente(
				form.getId()));
		// Collection registri=;
		form.setRegistri(getRegistri(RegistroDelegate.getInstance()
				.getRegistriByAooId(utente.getRegistroVOInUso().getAooId())));

	}

	private Collection<RegistroVO> getRegistri(Collection<RegistroVO> registriIN){
		List<RegistroVO> registriOut=new ArrayList<RegistroVO>();
		if(Organizzazione.getInstance().getValueObject().getUnitaAmministrativa()==UnitaAmministrativaEnum.POLICLINICO_CT)
			registriOut=(List<RegistroVO>) registriIN;
		else{
		for(RegistroVO r:registriIN){
			if(!r.getCodRegistro().equals("Fatt"))
				registriOut.add(r);
		}
		}
		return registriOut;
	}

	private void rimuoviUffici(ProfiloUtenteForm form) {
		String[] uffici = form.getUfficiSelezionatiId();
		if (uffici != null) {
			for (int i = 0; i < uffici.length; i++) {
				String ufficio = uffici[i];
				if (ufficio != null) {
					form.rimuoviUfficio(ufficio);
				}
			}
		}
	}

	protected void assegnaAdUfficio(ProfiloUtenteForm form, int ufficioId) {
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(ufficioId);
		Organizzazione org = Organizzazione.getInstance();
		Ufficio uff = org.getUfficio(ufficioId);
		ass.setNomeUfficio(uff.getValueObject().getDescription());
		ass.setDescrizioneUfficio(uff.getPath());
		ProfiloUtenteForm pForm = (ProfiloUtenteForm) form;
		pForm.aggiungiUfficio(ass);
		form.setUfficioSelezionatoId(0);
	}

	private void aggiornaUtenteOrganizzazione(UtenteVO utenteVO)
			throws DataException {
		Organizzazione org = Organizzazione.getInstance();
		OrganizzazioneDelegate organizzazioneDelegate = OrganizzazioneDelegate
				.getInstance();
		int utenteId = utenteVO.getId().intValue();
		Utente utente = org.getUtente(utenteId);
		if (utente == null) {
			utente = new Utente(utenteVO);
		} else {
			utente.setValueObject(utenteVO);
		}
		RegistroDelegate registroDelegate = RegistroDelegate.getInstance();

		Map<Integer,RegistroVO> registri = registroDelegate.getRegistriUtente(utenteId);
		utente.setRegistri(registri);
		utente.setRegistroUfficialeId(RegistroBO
				.getRegistroUfficialeId(registri.values()));

		Collection<Integer> ids = organizzazioneDelegate
				.getIdentificativiUffici(utenteId);
		for (Iterator<Integer> i = ids.iterator(); i.hasNext();) {
			int uffId = i.next().intValue();
			// logger.info(org.getUfficio(uffId));
			if (org.getUfficio(uffId).getValueObject().getParentId() > 0) {
				org.getUfficio(uffId).addUtente(utente);
				/*
				 * if (UfficioDelegate.getInstance().isUtenteReferenteUfficio(
				 * uffId, utenteId))
				 * org.getUfficio(uffId).addUtenteReferente(utente);
				 */
			}
		}

		org.addUtente(utente);
	}

}