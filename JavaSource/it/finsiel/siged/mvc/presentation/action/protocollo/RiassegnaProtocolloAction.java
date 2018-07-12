package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.ProtocolloIngresso;
import it.finsiel.siged.mvc.bo.AlberoUfficiBO;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloIngressoForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.util.DateUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

public class RiassegnaProtocolloAction extends ProtocolloAction {

	// ----------------------------------------------------- Instance Variables

	/**
	 * The <code>Log</code> instance for this application.
	 */
	static Logger logger = Logger.getLogger(RiassegnaProtocolloAction.class
			.getName());

	// --------------------------------------------------------- Public Methods
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward actionForward = super.execute(mapping, form, request,response);
		if (actionForward != null) {
			return actionForward;
		}
		ActionMessages errors = new ActionMessages();// Report any errors we
		HttpSession session = request.getSession(true); // we create one if does
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		ProtocolloIngressoForm pForm = (ProtocolloIngressoForm) form;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		boolean ufficioCompleto = (utente.getUfficioVOInUso().getTipo().equals(
				UfficioVO.UFFICIO_CENTRALE) || utente.getUfficioVOInUso()
				.getTipo().equals(UfficioVO.UFFICIO_SEMICENTRALE));
		
		Integer protocolloId = (Integer) request.getAttribute("protocolloId");
		
		session.setAttribute("protocolloForm", pForm);
		if (form == null) {
			logger.info(" Creating new riassegnazioneAction");
			form = new ProtocolloIngressoForm();
			session.setAttribute(mapping.getAttribute(), form);
		}
		getInputPage(request, session);
		if (pForm.getOggettoFromOggettario() != null) {
			pForm.setOggettoSelezionato(1);
		}
		
		if (pForm.getUfficioCorrenteId() == 0) {
			AlberoUfficiBO.impostaUfficioUtentiAbilitati(utente, pForm,
					ufficioCompleto);
			pForm.setUtenteAbilitatoSuUfficio(!utente.getUfficioVOInUso()
					.getTipo().equals(UfficioVO.UFFICIO_NORMALE));
		}

		if (protocolloId != null) {
			int id = protocolloId.intValue();
			ProtocolloIngresso protocollo = ProtocolloDelegate.getInstance()
					.getProtocolloIngressoById(id);
			
			pForm.setUfficioCruscottoId((Integer) request.getAttribute("ufficioAssegnatarioId"));
			pForm.setCaricaCruscottoId((Integer) request.getAttribute("caricaAssegnatarioId"));
			
			aggiornaForm(protocollo, pForm, session, utente);
			session.setAttribute("protocolloIngresso", protocollo);
			return mapping.findForward("input");
		}

		if (request.getParameter("salvaAction") != null) {
			pForm.validateRiassegnazione(mapping, request, errors);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return (mapping.findForward("input"));
			} else {
					if (request.getParameter("salvaAction").equals(
							"Riassegna i Protocolli")) {
						String[] pIds = (String[]) session
								.getAttribute("protocolloIdSelezionati");
						for (String id : pIds) {
							ProtocolloIngresso protocollo = ProtocolloDelegate
									.getInstance().getProtocolloIngressoById(
											Integer.valueOf(id).intValue());
							// aggiungi ad assegnatari precedenti
							impostaAssegnatariPrecedentiForm(protocollo, pForm, utente);
							aggiornaAssegnatariModel(pForm, protocollo,
									utente);
							delegate.riassegnaProtocollo(protocollo, utente);
							
						}
						session.removeAttribute("protocolloIdSelezionati");
					} else {
						ProtocolloIngresso protocolloIngresso = (ProtocolloIngresso) session
								.getAttribute("protocolloIngresso");
						aggiornaAssegnatariModel(pForm, protocolloIngresso,
								utente);
						delegate.riassegnaProtocollo(protocolloIngresso, utente);
					}
					if (session.getAttribute("TORNA_SCARICO_UTENTE") != null) {
						session.removeAttribute("TORNA_SCARICO_UTENTE");
						return (mapping.findForward("presaInCarico"));
					}else if (session.getAttribute("TORNA_SCARICO_FATTURE") != null) {
						session.removeAttribute("TORNA_SCARICO_FATTURE");
						return (mapping.findForward("ritornaFatture"));
					} 
					else if (session.getAttribute("TORNA_SCARICO_UFFICIO") != null) {
						session.removeAttribute("TORNA_SCARICO_UFFICIO");
						return (mapping.findForward("presaInCaricoUfficio"));
					} else if (session
							.getAttribute("TORNA_CRUSCOTTO_STRUTTURA") != null) {
						session.removeAttribute("TORNA_CRUSCOTTO_STRUTTURA");
						return (mapping.findForward("cruscottoStruttura"));
					} else if (session.getAttribute("TORNA_CRUSCOTTO_AMMINISTRAZIONE") != null) {
						session.removeAttribute("TORNA_CRUSCOTTO_AMMINISTRAZIONE");
						return (mapping.findForward("cruscottoAmministrazione"));
					} else if (session.getAttribute("TORNA_ALERT") != null) {
						session.removeAttribute("TORNA_ALERT");
						return (mapping.findForward("alert"));
					} else {
						return (mapping.findForward("listaRespinti"));
					}
			}

		} if (request.getParameter("rimuoviAssegnatariAction") != null) {
			rimuovoAssegnatario(pForm);
			return mapping.findForward("edit");

		}if (request.getParameter("indietroAction") != null) {			
			if (session.getAttribute("TORNA_SCARICO_UTENTE") != null) {
				session.removeAttribute("TORNA_SCARICO_UTENTE");
				return (mapping.findForward("presaInCarico"));
			}else if (session.getAttribute("TORNA_SCARICO_FATTURE") != null) {
				session.removeAttribute("TORNA_SCARICO_FATTURE");
				return (mapping.findForward("ritornaFatture"));
			} 
			else if (session.getAttribute("TORNA_SCARICO_UFFICIO") != null) {
				session.removeAttribute("TORNA_SCARICO_UFFICIO");
				return (mapping.findForward("presaInCaricoUfficio"));
			} else if (session.getAttribute("TORNA_CRUSCOTTO_STRUTTURA") != null) {
				session.removeAttribute("TORNA_CRUSCOTTO_STRUTTURA");
				return (mapping.findForward("cruscottoStruttura"));
			} else if (session.getAttribute("TORNA_CRUSCOTTO_AMMINISTRAZIONE") != null) {
				session.removeAttribute("TORNA_CRUSCOTTO_AMMINISTRAZIONE");
				return (mapping.findForward("cruscottoStruttura"));
			} else if (session.getAttribute("TORNA_ALERT") != null) {
				session.removeAttribute("TORNA_ALERT");
				return (mapping.findForward("alert"));
			} else {
				return (mapping.findForward("listaRespinti"));
			}
		}
		return mapping.getInputForward();
	}

	private static void getInputPage(HttpServletRequest request,
			HttpSession session) {
		if (request.getParameter("SCARICO_UTENTE") != null)
			session.setAttribute("TORNA_SCARICO_UTENTE", "TRUE");
		else if (request.getParameter("SCARICO_UFFICIO") != null)
			session.setAttribute("TORNA_SCARICO_UFFICIO", "TRUE");
		else if (request.getParameter("SCARICO_FATTURE") != null)
			session.setAttribute("TORNA_SCARICO_FATTURE", "TRUE");
		else if (request.getParameter("CRUSCOTTO_STRUTTURA") != null)
			session.setAttribute("TORNA_CRUSCOTTO_STRUTTURA", "TRUE");
		else if (request.getParameter("CRUSCOTTO_AMMINISTRAZIONE") != null)
			session.setAttribute("TORNA_CRUSCOTTO_AMMINISTRAZIONE", "TRUE");
		else if (request.getParameter("ALERT") != null)
			session.setAttribute("TORNA_ALERT", "TRUE");
	}

	public void aggiornaForm(ProtocolloIngresso protocollo,
			ProtocolloIngressoForm form, HttpSession session, Utente utente) {
		// dati generali
		aggiornaDatiGeneraliForm(protocollo.getProtocollo(), form);
		// assegnatari
		aggiornaAssegnatariForm(form);
		// aggiungi ad assegnatari precedenti
		impostaAssegnatariPrecedentiForm(protocollo, form, utente);
		
	}

	private void aggiornaDatiGeneraliForm(ProtocolloVO protocollo,
			ProtocolloForm form) {
		Integer id = protocollo.getId();
		if (id != null) {
			form.setProtocolloId(id.intValue());
			form.setDataRegistrazione(DateUtil.formattaData(protocollo
					.getDataRegistrazione().getTime()));
		} else {
			form.setProtocolloId(0);
		}
		form.setAooId(protocollo.getAooId());
		form.setNumeroProtocollo(protocollo.getNumProtocollo() + "/"
				+ protocollo.getAnnoRegistrazione());
		form.setTipoDocumentoId(protocollo.getTipoDocumentoId());
		Date dataDoc = protocollo.getDataDocumento();
		form.setDataDocumento(dataDoc == null ? null : DateUtil
				.formattaData(dataDoc.getTime()));
		Date dataRic = protocollo.getDataRicezione();
		form.setDataRicezione(dataRic == null ? null : DateUtil
				.formattaData(dataRic.getTime()));
		form.setRiservato(protocollo.isRiservato());
		form.setStato(protocollo.getStatoProtocollo());
		form.setOggetto(protocollo.getOggetto());
		form
				.setUfficioProtocollatoreId(protocollo
						.getUfficioProtocollatoreId());
		form.setUtenteProtocollatoreId(protocollo.getCaricaProtocollatoreId());
		form.setDocumentoVisibile(true);
	}

	private void aggiornaAssegnatariForm(ProtocolloIngressoForm form) {
		form.removeAssegnatari();
		form.setAssegnatariCompetenti(null);
		form.setTitolareProcedimentoCompetente(false);
	}

	private void impostaAssegnatariPrecedentiForm(
			ProtocolloIngresso protocollo, ProtocolloIngressoForm form,
			Utente utente) {
		form.resetAssegnatariPrecedenti();
		Organizzazione org = Organizzazione.getInstance();
		for (Iterator<AssegnatarioVO> i = protocollo.getAssegnatari().iterator(); i.hasNext();) {
			AssegnatarioVO assegnatario = i.next();
			AssegnatarioView ass = newAssegnatario(assegnatario.getUfficioAssegnatarioId(), assegnatario.getCaricaAssegnatarioId());
			ass.setStato(assegnatario.getStatoAssegnazione());
			ass.setPresaVisione(assegnatario.isPresaVisione());
			ass.setLavorato(assegnatario.isLavorato());
			ass.setMsg(assegnatario.getMsgAssegnatarioCompetente());
			ass.setCompetente(assegnatario.isCompetente());
			ass.setCaricaAssegnanteId(assegnatario.getCaricaAssegnanteId());
			ass.setUfficioAssegnanteId(assegnatario.getUfficioAssegnanteId());
			ass.setTitolareProcedimento(assegnatario.isTitolareProcedimento());
			if(assegnatario.getUfficioAssegnatarioId()==utente.getUfficioInUso()){
				if(assegnatario.getCaricaAssegnatarioId()==0 && assegnatario.isTitolareProcedimento())
					form.setTitolareProcedimentoCompetente(true);
				else if(assegnatario.getCaricaAssegnatarioId()==utente.getCaricaInUso() && assegnatario.isTitolareProcedimento())
					form.setTitolareProcedimentoCompetente(true);
			}
			form.aggiungiAssegnatarioPrecedente(ass);
		}
		
		if (org.getUfficio(utente.getUfficioInUso()).isCaricaReferente(utente.getCaricaInUso()))
			form.rimuoviAssegnatarioPrecedente(newAssegnatario(utente.getUfficioInUso(), 0));
		form.rimuoviAssegnatarioPrecedente(newAssegnatario(utente.getUfficioInUso(), utente.getCaricaInUso()));
		if (form.getUfficioCruscottoId() != null) {
			if (form.getCaricaCruscottoId() != null)
				form.rimuoviAssegnatarioPrecedente(newAssegnatario(form.getUfficioCruscottoId().intValue(), form.getCaricaCruscottoId().intValue()));
			else
				form.rimuoviAssegnatarioPrecedente(newAssegnatario(form.getUfficioCruscottoId().intValue(), 0));
		}
	}

	private AssegnatarioView newAssegnatario(int uffId, int caricaId) {
		Organizzazione org = Organizzazione.getInstance();
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(uffId);
		Ufficio uff = org.getUfficio(uffId);
		if (uff != null) {
			ass.setNomeUfficio(uff.getValueObject().getDescription());
		}
		if (caricaId != 0) {
			CaricaVO carica = org.getCarica(caricaId);
			ass.setUtenteId(carica.getUtenteId());
			Utente ute = org.getUtente(carica.getUtenteId());
			if (ute != null) {
				ute.getValueObject().setCarica(carica.getNome());
				if (carica.isAttivo())
					ass.setNomeUtente(ute.getValueObject().getCaricaFullName());
				else
					ass.setNomeUtente(ute.getValueObject()
							.getCaricaFullNameNonAttivo());
			} else
				ass.setNomeUtente(carica.getNome());
		}
		return ass;

	}

	private void updateAssegnatariCompetenti(ProtocolloIngressoForm form) {
		List<String> assCompetenti = new ArrayList<String>();
		for (Iterator<AssegnatarioView> i = form.getAssegnatari().iterator(); i.hasNext();) {
			AssegnatarioView ass = i.next();
			if (ass.isCompetente()) {
				assCompetenti.add(ass.getKey());
			}
		}
		form.setAssegnatariCompetenti(assCompetenti.toArray(new String[0]));
	}

	protected void caricaProtocollo(HttpServletRequest request,
			ProtocolloForm form) {
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
		ass.setTitolareProcedimento(false);
		ass.setCaricaAssegnanteId(utente.getCaricaInUso());
		ass.setUfficioAssegnanteId(utente.getUfficioInUso());
		pForm.aggiungiAssegnatario(ass);
		pForm.setAssegnatarioCompetente(ass.getKey());
		form.setUfficioSelezionatoId(0);
		if (form.isDipTitolarioUfficio()) 
			form.setTitolario(null);
		updateAssegnatariCompetenti(pForm);
		
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
		ass.setTitolareProcedimento(false);
		ass.setCaricaAssegnanteId(utente.getCaricaInUso());
		ass.setUfficioAssegnanteId(utente.getUfficioInUso());
		pForm.aggiungiAssegnatario(ass);
		pForm.setAssegnatarioCompetente(ass.getKey());
		if (form.isDipTitolarioUfficio())
			form.setTitolario(null);
		updateAssegnatariCompetenti(pForm);
	}
	
	private void rimuovoAssegnatario(ProtocolloIngressoForm form) {
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
		}
	}

	protected void resetForm(ProtocolloForm form, Utente utente,HttpServletRequest request) {

	}
	
	
	private static AssegnatarioVO aggiungiAssegnatarioModel(Utente utente,
			AssegnatarioView ass, ProtocolloIngressoForm form) {
		UtenteVO uteVO = utente.getValueObject();
		AssegnatarioVO assegnatario = new AssegnatarioVO();
		Date now = new Date();
		assegnatario.setDataAssegnazione(now);
		assegnatario.setDataOperazione(now);
		assegnatario.setRowCreatedUser(uteVO.getUsername());
		assegnatario.setRowUpdatedUser(uteVO.getUsername());
		assegnatario.setUfficioAssegnanteId(utente.getUfficioInUso());
		assegnatario.setCaricaAssegnanteId(utente.getCaricaInUso());
		assegnatario.setUfficioAssegnatarioId(ass.getUfficioId());
		assegnatario.setStatoAssegnazione('S');
		if (ass.getUtenteId() != 0)
			assegnatario.setCaricaAssegnatarioId(CaricaDelegate.getInstance()
					.getCaricaByUtenteAndUfficio(ass.getUtenteId(),
							ass.getUfficioId()).getCaricaId());
		else
			assegnatario.setCaricaAssegnatarioId(0);
		if (form.isCompetente(ass)) {
			assegnatario.setCompetente(true);
			assegnatario.setMsgAssegnatarioCompetente(form
					.getMsgAssegnatarioCompetente());

		}
		assegnatario.setTitolareProcedimento(ass.isTitolareProcedimento());
		assegnatario.setPresaVisione(false);
		return assegnatario;
	}

	private static AssegnatarioVO aggiungiAssegnatarioPrecedenteModel(
			Utente utente, AssegnatarioView ass, ProtocolloIngressoForm form) {
		UtenteVO uteVO = utente.getValueObject();
		AssegnatarioVO assegnatario = new AssegnatarioVO();
		Date now = new Date();
		assegnatario.setDataAssegnazione(now);
		assegnatario.setDataOperazione(now);
		assegnatario.setRowCreatedUser(uteVO.getUsername());
		assegnatario.setRowUpdatedUser(uteVO.getUsername());
		assegnatario.setUfficioAssegnanteId(ass.getUfficioAssegnanteId());
		assegnatario.setCaricaAssegnanteId(ass.getCaricaAssegnanteId());
		assegnatario.setUfficioAssegnatarioId(ass.getUfficioId());
		assegnatario.setStatoAssegnazione(ass.getStato());
		if (ass.getUtenteId() != 0)
			assegnatario.setCaricaAssegnatarioId(CaricaDelegate.getInstance()
					.getCaricaByUtenteAndUfficio(ass.getUtenteId(),
							ass.getUfficioId()).getCaricaId());
		else
			assegnatario.setCaricaAssegnatarioId(0);
		// if (form.isCompetente(ass)) {
		assegnatario.setCompetente(ass.isCompetente());
		assegnatario.setMsgAssegnatarioCompetente(ass.getMsg());

		// }
		assegnatario.setPresaVisione(ass.isPresaVisione());
		assegnatario.setLavorato(ass.isLavorato());
		assegnatario.setTitolareProcedimento(ass.isTitolareProcedimento());
		return assegnatario;
	}

	private void aggiornaAssegnatariModel(ProtocolloIngressoForm form,
			ProtocolloIngresso protocollo, Utente utente) {
		protocollo.removeAssegnatari();
		protocollo.setMsgAssegnatarioCompetente(form
				.getMsgAssegnatarioCompetente());
		//UtenteVO uteVO = utente.getValueObject();
		Collection<AssegnatarioView> assegnatariPrecedenti = form.getAssegnatariPrecedenti();
		if (assegnatariPrecedenti != null) {
			for (Iterator<AssegnatarioView> i = assegnatariPrecedenti.iterator(); i.hasNext();)
				protocollo.aggiungiAssegnatario(aggiungiAssegnatarioPrecedenteModel(utente, i.next(), form));
		}
		Collection<AssegnatarioView> assegnatari = form.getAssegnatari();
		if (assegnatari != null) {
			for (Iterator<AssegnatarioView> i = assegnatari.iterator(); i.hasNext();) {
				protocollo.getProtocollo().setStatoProtocollo("R");
				protocollo.aggiungiAssegnatario(aggiungiAssegnatarioModel(
						utente, i.next(), form));
			}
		}
	}

}
