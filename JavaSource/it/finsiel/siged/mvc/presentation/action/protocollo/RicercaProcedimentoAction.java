package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.Fascicolo;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.TitolarioBO;
import it.finsiel.siged.mvc.business.AmministrazioneDelegate;
import it.finsiel.siged.mvc.business.FascicoloDelegate;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.business.ProcedimentoDelegate;
import it.finsiel.siged.mvc.business.SoggettoDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.FaldoneForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloIngressoForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloUscitaForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.RicercaProcedimentoForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.ProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloProcedimentoVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

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

public class RicercaProcedimentoAction extends Action {

	// ----------------------------------------------------- Instance Variables

	/**
	 * The <code>Log</code> instance for this application.
	 */
	static Logger logger = Logger.getLogger(RicercaProcedimentoAction.class
			.getName());

	public static void impostaUtenti(
			RicercaProcedimentoForm form, Utente utente) {
		Organizzazione org = Organizzazione.getInstance();
		Ufficio ufficioCorrente = org.getUfficio(utente.getUfficioInUso());
		List<UtenteVO> list = new ArrayList<UtenteVO>();
		for (Iterator<Utente> i = ufficioCorrente.getUtenti().iterator(); i.hasNext();) {
			
			Utente ute = (Utente) i.next();
			if(ute.getValueObject().isAbilitato())
				ute.getValueObject().setCarica(ute.getCaricaUfficioVO(utente.getUfficioInUso()).getNome());
				list.add(ute.getValueObject());
		}

		Comparator<UtenteVO> c = new Comparator<UtenteVO>() {
			public int compare(UtenteVO ute1,UtenteVO ute2) {
				return ute1.getFullName().compareToIgnoreCase(
						ute2.getFullName());
			}
		};
		
		Collections.sort(list, c);
		form.setUtenti(list);
	
	}
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();// Report any errors we

		HttpSession session = request.getSession();
		RicercaProcedimentoForm ricercaForm = (RicercaProcedimentoForm) form;
		boolean indietroVisibile = false;
		ricercaForm.setIndietroVisibile(indietroVisibile);
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		boolean preQuery = request.getAttribute("cercaProcedimentiDaFaldoni") != null;
		String cercaOggetto = (String) request.getAttribute("cercaProcedimentiDaProtocollo");
		/**/
		ricercaForm.setAooId(utente.getValueObject().getAooId());
		ricercaForm.setStatiProcedimento(LookupDelegate.getStatiProcedimento());
		ricercaForm.setPosizioniProcedimento(LookupDelegate.getPosizioniProcedimento());
		impostaUtenti(ricercaForm, utente);
		impostaTitolario(ricercaForm, utente, 0);
		impostaTipiProcedimento(ricercaForm, utente);
		/**/
		if (request.getAttribute("cercaProcedimentiDaProtocollo") == null) {
			ricercaForm.setOggettoProcedimento(ricercaForm.getOggettoProcedimento());
		} else {
			ricercaForm.setOggettoProcedimento(cercaOggetto);
		}

		String cercaOggettoDaFaldoni = (String) request.getAttribute("cercaProcedimentiDaFaldoni");
		if (request.getAttribute("cercaProcedimentiDaFaldoni") == null) {
			ricercaForm.setOggettoProcedimento(ricercaForm
					.getOggettoProcedimento());
		} else {
			ricercaForm.setOggettoProcedimento(cercaOggettoDaFaldoni);
		}

		String oggettoProcedimento = ricercaForm.getOggettoProcedimento();

		if (ricercaForm == null|| request.getParameter("annullaAction") != null) {
			if (Boolean.TRUE.equals(session.getAttribute("tornaFaldone"))) {
				indietroVisibile = true;
				ricercaForm.setIndietroVisibile(indietroVisibile);
			}
			ricercaForm.inizializzaForm();
			if (preQuery)
				ricercaForm.setOggettoProcedimento((String) request
						.getAttribute("cercaProcedimentiDaFaldoni"));
			session.setAttribute(mapping.getAttribute(), ricercaForm);
			if ("fascicoloProcedimenti".equals(session
					.getAttribute("provenienza"))) {
				indietroVisibile = true;
				ricercaForm.setIndietroVisibile(indietroVisibile);
			} else if (Boolean.TRUE.equals(session
					.getAttribute("tornaProtocollo"))) {
				indietroVisibile = true;
				ricercaForm.setIndietroVisibile(indietroVisibile);
			} else if (Boolean.TRUE.equals(session
					.getAttribute("indietroProcedimentiDaProtocollo"))) {
				indietroVisibile = true;
				ricercaForm.setIndietroVisibile(indietroVisibile);
			} else if (Boolean.TRUE.equals(session
					.getAttribute("indietroProcedimentiDaFaldoni"))) {
				indietroVisibile = true;
				ricercaForm.setIndietroVisibile(indietroVisibile);
			} else if ("".equals(cercaOggettoDaFaldoni)) {
				indietroVisibile = true;
				ricercaForm.setIndietroVisibile(indietroVisibile);
				return (mapping.findForward("input"));
			}
			else {
				indietroVisibile = false;
				ricercaForm.setIndietroVisibile(indietroVisibile);
			}
			return mapping.findForward("input");
		} if (request.getParameter("impostaTitolarioAction") != null) {
			if (ricercaForm.getTitolario() != null) {
				ricercaForm.setTitolarioPrecedenteId(ricercaForm.getTitolario()
						.getId().intValue());
			}
			TitolarioBO.impostaTitolario(ricercaForm, utente.getUfficioInUso(), ricercaForm.getTitolarioSelezionatoId());
			return mapping.findForward("input");
		} else if (request.getParameter("titolarioPrecedenteAction") != null) {
			TitolarioBO.impostaTitolario(ricercaForm, utente.getUfficioInUso(), ricercaForm.getTitolarioPrecedenteId());
			if (ricercaForm.getTitolario() != null) {
				ricercaForm.setTitolarioPrecedenteId(ricercaForm.getTitolario()
						.getParentId());
			}
			return mapping.findForward("input");
		} else if (request.getParameter("ricercaIniziale") != null) {
			ricercaForm.inizializzaForm();
			ricercaForm.setAooId(utente.getValueObject().getAooId());
			indietroVisibile = false;
			ricercaForm.setIndietroVisibile(indietroVisibile);
			session.removeAttribute("cercaProcedimentiDaProtocollo");
			session.removeAttribute("tornaFaldone");
			session.removeAttribute("tornaProtocollo");
			session.removeAttribute("btnCercaProcedimentiDaFaldoni");
			session.removeAttribute("tornaFascicolo");
			session.removeAttribute("provenienza");
			request.removeAttribute("cercaProcedimentiDaProtocollo");
			session.removeAttribute("procedimentiDaProtocollo");
			session.removeAttribute("indietroProcedimentiDaProtocollo");
			session.removeAttribute("risultatiProcedimentiDaProtocollo");
			session.removeAttribute("indietroProcedimentiDaFaldoni");
			session.setAttribute("ricercaSemplice", Boolean.TRUE);
		} else if (request.getParameter("btnRicerca") != null) {
			session.removeAttribute("btnCercaFascicoliDaFaldoni");
			session.setAttribute("ricercaSemplice", Boolean.TRUE);
			session.removeAttribute("provenienza");
			if (Boolean.TRUE.equals(session.getAttribute("tornaFaldone"))) {
				session.removeAttribute("tornaFaldone");
				return (mapping.findForward("tornaFaldone"));
			} else {
				ricercaForm.resetForm();
				return mapping.findForward("input");
			}
		} else if ("".equals(cercaOggetto)) {
			session.removeAttribute("procedimentiDaProtocollo");
			indietroVisibile = true;
			ricercaForm.setIndietroVisibile(indietroVisibile);
			return (mapping.findForward("input"));
		} else if ("".equals(cercaOggettoDaFaldoni)) {
			session.removeAttribute("cercaProcedimentiDaFaldoni");
			session.removeAttribute("procedimentiDaProtocollo");
			session.removeAttribute("tornaFaldone");
			ricercaForm.inizializzaForm();
			indietroVisibile = true;
			ricercaForm.setIndietroVisibile(indietroVisibile);
			return (mapping.findForward("input"));
		} else if (request.getParameter("btnCerca") != null
				|| preQuery
				|| Boolean.TRUE.equals(session
						.getAttribute("procedimentiDaProtocollo"))
				|| Boolean.TRUE.equals(session.getAttribute("tornaFaldone"))) {
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return (mapping.findForward("input"));
			}

			ricercaForm.setProcedimenti(null);
			if (Boolean.TRUE.equals(session
					.getAttribute("procedimentiDaProtocollo"))) {
				String oggetto = (String) request
						.getAttribute("cercaProcedimentiDaProtocollo");
				ricercaForm.setOggettoProcedimento(oggetto);
			} else {
				ricercaForm.setOggettoProcedimento(oggettoProcedimento);
				;
			}
			int maxRighe=Integer.parseInt(utente.getAreaOrganizzativa().getMaxRighe());
			ProcedimentoDelegate procedimentoDelegate = ProcedimentoDelegate
					.getInstance();
			HashMap hashMap = getParametriRicerca(ricercaForm, request);
			String soggettiId = null;
			if (ricercaForm.getDescrizioneInteressatoDelegato() != null
					&& !ricercaForm.getDescrizioneInteressatoDelegato().trim()
							.equals("")) {
				soggettiId = SoggettoDelegate
						.getInstance()
						.getPersonaFisicaListFromCognomeNome(
								ricercaForm.getDescrizioneInteressatoDelegato());
			}
			int contaRighe = procedimentoDelegate.contaProcedimenti(utente,
					hashMap, soggettiId);

			if (contaRighe > 0 || contaRighe <= maxRighe) {
				SortedMap procedimenti = new TreeMap();
				procedimenti = ProcedimentoDelegate.getInstance()
						.cercaProcedimenti(utente, hashMap, soggettiId);

				if (contaRighe == 0 || procedimenti == null
						|| procedimenti.size() == 0) {
					errors.add("nessun_dato", new ActionMessage("nessun_dato",
							"", ""));
				} else {
					ricercaForm.setProcedimenti(procedimenti.values());
				}
				if ("fascicoloProcedimenti".equals(session
						.getAttribute("provenienza"))) {
					indietroVisibile = true;
					ricercaForm.setIndietroVisibile(indietroVisibile);
					session.removeAttribute("ricercaSemplice");
					session.removeAttribute("tornaFaldone");
					session.removeAttribute("tornaProtocollo");
					session.removeAttribute("btnCercaProcedimentiDaFaldoni");
					session.setAttribute("tornaFascicolo", Boolean.TRUE);
				}
				if ("procedimentiDaFaldoni".equals(session
						.getAttribute("provenienza"))) {
					indietroVisibile = true;
					ricercaForm.setIndietroVisibile(indietroVisibile);
					session.removeAttribute("ricercaSemplice");
					session.removeAttribute("tornaFaldone");
					session.removeAttribute("tornaProtocollo");
					session.removeAttribute("btnCercaProcedimentiDaFaldoni");
					session.setAttribute("tornaFascicolo", Boolean.TRUE);
				}
				session.removeAttribute("procedimentiDaProtocollo");
				return (mapping.findForward("risultati"));

			} else {
				if (Boolean.TRUE.equals(session
						.getAttribute("procedimentiDaProtocollo"))
						|| Boolean.TRUE.equals(session
								.getAttribute("tornaFaldone"))) {
					indietroVisibile = true;
					ricercaForm.setIndietroVisibile(indietroVisibile);
					ricercaForm.setOggettoProcedimento("");
					errors.add("controllo.maxrighe", new ActionMessage(
							"controllo.maxrighe", "" + contaRighe,
							"procedimenti", "" + maxRighe));
				} else {
					errors.add("controllo.maxrighe", new ActionMessage(
							"controllo.maxrighe", "" + contaRighe,
							"procedimenti", "" + maxRighe));
				}
			}
		}

		else if (request.getParameter("btnAnnulla") != null) {
			if ("fascicoloProcedimenti".equals(session
					.getAttribute("provenienza"))) {
				indietroVisibile = true;
				ricercaForm.setIndietroVisibile(indietroVisibile);
				return (mapping.findForward("fascicoloProcedimento"));
			}
			if ("procedimentiDaFaldoni".equals(session
					.getAttribute("provenienza"))) {
				session.removeAttribute("provenienza");
				return (mapping.findForward("tornaFaldone"));
			}

			if (Boolean.TRUE.equals(session.getAttribute("tornaFaldone"))) {
				session.removeAttribute("tornaFaldone");
				return (mapping.findForward("tornaFaldone"));

			}
			if (Boolean.TRUE.equals(session.getAttribute("tornaProtocollo"))) {
				Object pForm = session.getAttribute("protocolloForm");
				session.removeAttribute("tornaProtocollo");
				ProtocolloForm pf = (ProtocolloForm) pForm;
				if (pForm instanceof ProtocolloIngressoForm) {
					if (pf.isDaScarico())
						return (mapping.findForward("tornaPresaInCarico"));
					else
						return (mapping.findForward("tornaProtocolloIngresso"));
				} else if (pForm instanceof ProtocolloUscitaForm) {
					return (mapping.findForward("tornaProtocolloUscita"));
				} else {
					if (pf.isDaScarico())
						return (mapping.findForward("tornaPostaPresaInCarico"));
					else
						return (mapping.findForward("tornaPostaInterna"));
				}
			}
			if (Boolean.TRUE.equals(session
					.getAttribute("indietroProcedimentiDaProtocollo"))) {
				Object pForm = session.getAttribute("protocolloForm");
				session.removeAttribute("tornaProtocollo");
				ProtocolloForm pf = (ProtocolloForm) pForm;
				if (pForm instanceof ProtocolloIngressoForm) {
					if (pf.isDaScarico())
						return (mapping.findForward("tornaPresaInCarico"));
					else
						return (mapping.findForward("tornaProtocolloIngresso"));
				} else if (pForm instanceof ProtocolloUscitaForm) {
					return (mapping.findForward("tornaProtocolloUscita"));
				} else {
					if (pf.isDaScarico())
						return (mapping.findForward("tornaPostaPresaInCarico"));
					else
						return (mapping.findForward("tornaPostaInterna"));
				}
			}
			if (Boolean.TRUE.equals(session
					.getAttribute("indietroProcedimentiDaFaldoni"))) {
				session.removeAttribute("indietroProcedimentiDaFaldoni");
				return (mapping.findForward("tornaFaldone"));
			}

			else {
				ricercaForm.resetForm();
				Organizzazione org = Organizzazione.getInstance();
				return mapping.findForward("input");
			}
		}

		else if (request.getParameter("indietro") != null) {

			if ("procedimentiDaFaldoni".equals(session
					.getAttribute("provenienza"))) {
				return mapping.findForward("tornaFaldone");
			} else if ("fascicoloProcedimenti".equals(session
					.getAttribute("provenienza"))) {
				indietroVisibile = true;
				ricercaForm.setIndietroVisibile(indietroVisibile);
				return mapping.findForward("input");
				
			}
		}

		else if (request.getParameter("btnSeleziona") != null) {
			String[] proSel = ricercaForm.getProcedimentiSelezionati();
			if ("procedimentiDaFaldoni".equals(session
					.getAttribute("provenienza"))) {
				FaldoneForm fo = (FaldoneForm) session
						.getAttribute("faldoneForm");
				for (int i = 0; proSel != null && i < proSel.length; i++) {
					ProcedimentoVO vo = ProcedimentoDelegate.getInstance()
							.getProcedimentoVO(NumberUtil.getInt(proSel[i]));
					if (vo != null && vo.getReturnValue() == ReturnValues.FOUND)
						fo.aggiungiProcedimento(vo);
				}
				ricercaForm.inizializzaForm();
				session.removeAttribute("tornaFaldone");
				return (mapping.findForward("tornaFaldone"));
			}
			if (session.getAttribute("tornaFascicolo") == Boolean.TRUE) {
				Fascicolo fascicolo = (Fascicolo) session
						.getAttribute(Constants.FASCICOLO);
				FascicoloDelegate.getInstance().salvaProcedimentiFascicolo(
						fascicolo.getFascicoloVO(), proSel,
						utente.getValueObject().getUsername());
				request.setAttribute("fascicoloId", fascicolo.getFascicoloVO()
						.getId());
				session.removeAttribute("tornaFascicolo");
				return (mapping.findForward("tornaFascicolo"));
			} else if (session.getAttribute("tornaProtocollo") == Boolean.TRUE) {
				indietroVisibile = true;
				ricercaForm.setIndietroVisibile(indietroVisibile);
				ProtocolloForm pf = (ProtocolloForm) session
						.getAttribute("protocolloForm");
				for (int i = 0; proSel != null && i < proSel.length; i++) {
					ProcedimentoVO pVO = ProcedimentoDelegate.getInstance()
							.getProcedimentoVO(NumberUtil.getInt(proSel[i]));
					if (pVO != null
							&& pVO.getReturnValue() == ReturnValues.FOUND) {

						if (pf.getProtocolloId() == 0
								&& pf.getFlagTipo().equals("I"))
							assegnaAdUfficioIngresso(pf, pVO.getUfficioId(),
									utente);
						else {
							ProtocolloProcedimentoVO ppVO = new ProtocolloProcedimentoVO();
							ppVO.setProtocolloId(pf.getProtocolloId());
							ppVO.setNumeroProtocollo(pf.getNumero());
							ppVO.setProcedimentoId(pVO.getId().intValue());
							ppVO.setNumeroProcedimento(pVO.getNumeroProcedimento());
							ppVO.setOggetto(pVO.getOggetto());
							ppVO.setFascicoloId(pVO.getFascicoloId());
							pf.aggiungiProcedimento(ppVO);
						}

					}
				}
				pf.setSezioneVisualizzata("Procedimenti");
				ricercaForm.inizializzaForm();
				session.removeAttribute("tornaProtocollo");
				session.setAttribute("protocolloForm", pf);

				if ("I".equals(pf.getFlagTipo()))
					if (pf.isDaScarico())
						return (mapping.findForward("tornaPresaInCarico"));
					else
						return (mapping.findForward("tornaProtocolloIngresso"));
				else if ("U".equals(pf.getFlagTipo())) {
					return (mapping.findForward("tornaProtocolloUscita"));
				} else {
					if (pf.isDaScarico())
						return (mapping.findForward("tornaPostaPresaInCarico"));
					else
						return (mapping.findForward("tornaPostaInterna"));
				}
			} else if (session
					.getAttribute("risultatiProcedimentiDaProtocollo") == Boolean.TRUE) {
				indietroVisibile = true;
				ricercaForm.setIndietroVisibile(indietroVisibile);
				ProtocolloForm pf = (ProtocolloForm) session.getAttribute("protocolloForm");
				for (int i = 0; proSel != null && i < proSel.length; i++) {
					ProcedimentoVO pVO = ProcedimentoDelegate.getInstance()
							.getProcedimentoVO(NumberUtil.getInt(proSel[i]));
					if (pVO != null
							&& pVO.getReturnValue() == ReturnValues.FOUND) {

						if (pf.getProtocolloId() == 0
								&& pf.getFlagTipo().equals("I"))
							assegnaAdUfficioIngresso(pf, pVO.getUfficioId(),
									utente);

						else {
							ProtocolloProcedimentoVO ppVO = new ProtocolloProcedimentoVO();
							ppVO.setProtocolloId(pf.getProtocolloId());
							ppVO.setNumeroProtocollo(pf.getNumero());
							ppVO.setProcedimentoId(pVO.getId().intValue());
							ppVO.setNumeroProcedimento(pVO
									.getNumeroProcedimento());
							ppVO.setOggetto(pVO.getOggetto());
							ppVO.setFascicoloId(pVO.getFascicoloId());
							pf.aggiungiProcedimento(ppVO);
						}

					}
				}
				pf.setSezioneVisualizzata("Procedimenti");
				ricercaForm.inizializzaForm();
				session.removeAttribute("tornaProtocollo");
				session.setAttribute("protocolloForm", pf);

				if ("I".equals(pf.getFlagTipo()))
					if (pf.isDaScarico())
						return (mapping.findForward("tornaPresaInCarico"));
					else
						return (mapping.findForward("tornaProtocolloIngresso"));
				else if ("U".equals(pf.getFlagTipo())) {
					return (mapping.findForward("tornaProtocolloUscita"));
				} else {
					if (pf.isDaScarico())
						return (mapping.findForward("tornaPostaPresaInCarico"));
					else
						return (mapping.findForward("tornaPostaInterna"));
				}
			}

		} else if (request.getParameter("visualizzaProcedimento") != null) {
			return (mapping.findForward("visualizzaProcedimento"));
		} else if ("fascicoloProcedimenti".equals(session
				.getAttribute("provenienza"))) {
			indietroVisibile = true;
			ricercaForm.setIndietroVisibile(indietroVisibile);
			return mapping.getInputForward();

		} else if (session.getAttribute("tornaProtocollo") == Boolean.TRUE) {
			indietroVisibile = true;
			ricercaForm.setIndietroVisibile(indietroVisibile);
			return mapping.getInputForward();

		} else if (session.getAttribute("procedimentiDaProtocollo") == Boolean.TRUE) {
			indietroVisibile = false;
			ricercaForm.setIndietroVisibile(indietroVisibile);
			return mapping.getInputForward();

		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		session.removeAttribute("btnCercaProcedimentiDaFaldoni");
		session.removeAttribute("procedimentiDaProtocollo");
		session.removeAttribute("tornaFaldone");
		ricercaForm.setOggettoProcedimento("");
		return mapping.getInputForward();
	}

	protected void assegnaAdUfficioIngresso(ProtocolloForm form, int ufficioId,
			Utente utente) {
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(ufficioId);
		Organizzazione org = Organizzazione.getInstance();
		Ufficio uff = org.getUfficio(ufficioId);
		ass.setNomeUfficio(uff.getValueObject().getDescription());
		ass.setDescrizioneUfficio(uff.getPath());
		ProtocolloIngressoForm pForm = (ProtocolloIngressoForm) form;
		ass.setStato('S');
		ass.setCompetente(true);
		ass.setTitolareProcedimento(true);
		ass.setCaricaAssegnanteId(utente.getCaricaInUso());
		ass.setUfficioAssegnanteId(utente.getUfficioInUso());
		pForm.aggiungiAssegnatario(ass);
		pForm.setAssegnatarioCompetente(ass.getKey());
		pForm.setTitolareProcedimento(ass.getKey());
		form.setUfficioSelezionatoId(0);
		if (form.isDipTitolarioUfficio())
			form.setTitolario(null);
	}

	private void impostaTitolario(RicercaProcedimentoForm form, Utente utente,
			int titolarioId) {
		int ufficioId = utente.getUfficioInUso();
		TitolarioBO.impostaTitolario(form, ufficioId, titolarioId);
	}

	private void impostaTipiProcedimento(RicercaProcedimentoForm form, Utente utente){
		form.setTipiProcedimento(AmministrazioneDelegate.getInstance().getTipiProcedimentoByUfficio(utente.getUfficioInUso()));
	}
	
	public static HashMap getParametriRicerca(RicercaProcedimentoForm form,
			HttpServletRequest request) throws Exception {
		Date dataAvvioInizio;
		Date dataAvvioFine;
		Date dataScadenzaInizio;
		Date dataScadenzaFine;
		HashMap sqlDB = new HashMap();
		sqlDB.clear();

		if (DateUtil.isData(form.getDataAvvioInizio())) {
			dataAvvioInizio = DateUtil.toDate(form.getDataAvvioInizio());
			sqlDB.put("data_avvio >= ?", dataAvvioInizio);
		}
		if (DateUtil.isData(form.getDataAvvioFine())) {
			dataAvvioFine = DateUtil.toDate(form.getDataAvvioFine());
			sqlDB.put("data_avvio <= ?", dataAvvioFine);
		}
		if (DateUtil.isData(form.getDataScadenzaInizio())) {
			dataScadenzaInizio = DateUtil.toDate(form.getDataScadenzaInizio());
			sqlDB.put("data_scadenza >= ?", dataScadenzaInizio);
		}
		if (DateUtil.isData(form.getDataScadenzaFine())) {
			dataScadenzaFine = DateUtil.toDate(form.getDataScadenzaFine());
			sqlDB.put("data_scadenza <= ?", dataScadenzaFine);
		}
		if (NumberUtil.isInteger(form.getAnno())) {
			Integer anno = new Integer(form.getAnno());
			sqlDB.put("anno = ?", anno);
		}
		if (form.getNumero()!=null && !"".equals(form.getNumero()) ) {
			sqlDB.put(" upper(numero_procedimento) LIKE ?",  "%" + form.getNumero().toUpperCase() + "%");
		}
		if (form.getTipoProcedimentoSelezionatoId() > 0) {
			sqlDB.put("procedimenti.tipo_procedimento_id = ?", form.getTipoProcedimentoSelezionatoId());
		} else{
			sqlDB.put("procedimenti.tipo_procedimento_id IN("+form.getIdTipiProcedimento()+")", null);
		}
		if (form.getOggettoProcedimento() != null
				&& !"".equals(form.getOggettoProcedimento())) {
			sqlDB.put(" upper(oggetto) LIKE ?", "%" + form.getOggettoProcedimento().toUpperCase() + "%");
		}
		if (form.getNote() != null && !"".equals(form.getNote())) {
			sqlDB.put(" upper(note) LIKE ?", "%" + form.getNote().toUpperCase()
					+ "%");
		}
		if (form.getStatoId() > 0) {
			Integer stato = new Integer(form.getStatoId());
			sqlDB.put("stato_id = ?", stato);
		}
		if (!"ALL".equals(form.getPosizione()) && form.getPosizione() != null) {
			sqlDB.put("posizione_id = ?", form.getPosizione());
		}
		if (sqlDB.isEmpty())
			return null;
		return sqlDB;
	}

}