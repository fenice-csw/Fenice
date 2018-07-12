package it.compit.fenice.mvc.presentation.action.protocollo;

import it.compit.fenice.enums.ReturnPageEnum;
import it.compit.fenice.model.protocollo.PostaInterna;
import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.presentation.actionform.protocollo.PostaInternaForm;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.AlberoUfficiBO;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.action.protocollo.ProtocolloAction;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class RiassegnaPostaInternaAction extends ProtocolloAction {

	static Logger logger = Logger.getLogger(RiassegnaPostaInternaAction.class
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
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		PostaInternaForm pForm = (PostaInternaForm) form;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		session.setAttribute("protocolloForm", pForm);
		if (form == null) {
			logger.info(" Creating new riassegnazioneAction");
			form = new PostaInternaForm();
			session.setAttribute(mapping.getAttribute(), form);
		}
		getInputPage(request, pForm);
		Integer protocolloId = (Integer) request.getAttribute("protocolloId");
		Integer ufficioId = (Integer) request
				.getAttribute("ufficioAssegnatarioId");

		Integer caricaId = (Integer) request
				.getAttribute("caricaAssegnatarioId");
		if (pForm.getUfficioCorrenteId() == 0) {
			boolean ufficioCompleto = true;
			AlberoUfficiBO.impostaUfficioUtentiAbilitati(utente, pForm,
					ufficioCompleto);
		}
		if (protocolloId != null) {
			int id = protocolloId.intValue();
			PostaInterna protocollo = ProtocolloDelegate.getInstance()
					.getPostaInternaById(id);
			aggiornaForm(protocollo, pForm, session, utente, ufficioId,
					caricaId);
			session.setAttribute("postaInterna", protocollo);
			return mapping.findForward("input");
		}
		if (request.getParameter("salvaAction") != null) {
			if (pForm.getDestinatari().size() == 0) {
				errors.add("assegnatari", new ActionMessage(
						"assegnatari_obbligatorio"));
			} else if (pForm.getDestinatarioCompetente() == null
					|| "".equals(pForm.getDestinatarioCompetente())) {
				errors.add("assegnatarioCompetente", new ActionMessage(
						"assegnatario_competente_obbligatorio"));
			}

			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return (mapping.findForward("input"));
			} else {
				if (request.getParameter("salvaAction").equals(
						"Riassegna la Posta")) {
					String[] pIds = (String[]) session
							.getAttribute("protocolloIdSelezionati");
					for (String id : pIds) {
						ProtocolloForm prtForm = (ProtocolloForm) form;
						super.caricaProtocollo(request, prtForm, id);
						PostaInternaForm postForm = (PostaInternaForm) prtForm;
						session.setAttribute("protocolloForm", postForm);
						PostaInterna protocollo = ProtocolloDelegate
								.getInstance().getPostaInternaById(
										Integer.valueOf(id).intValue());
						impostaDestinatariPrecedentiForm(protocollo, postForm,
								utente, null, null);
						aggiornaDestinatariModel(postForm, protocollo, utente);
						delegate.riassegnaPosta(protocollo, utente);
					}
					session.removeAttribute("protocolloIdSelezionati");
					
					if (pForm.getInputPage() == ReturnPageEnum.OFFICE_PANEL) {
						return (mapping.findForward("cruscottoStruttura"));
					} else if (pForm.getInputPage() == ReturnPageEnum.ADMINISTRATION_PANEL) {
						return (mapping.findForward("cruscottoAmministrazione"));
					}
					return (mapping.findForward("presaInCarico"));
				} else {
					PostaInterna postaInterna = (PostaInterna) session
							.getAttribute("postaInterna");
					aggiornaDestinatariModel(pForm, postaInterna, utente);
					delegate.riassegnaPosta(postaInterna, utente);
					if (pForm.getInputPage() == ReturnPageEnum.DASHBOARD_PI) {
						return (mapping.findForward("presaInCarico"));
					} else {
						return (mapping.findForward("listaRespinti"));
					}
				}
			}
		}
		if (request.getParameter("rimuoviAssegnatariAction") != null) {
			rimuovoDestinatari(pForm);
			return mapping.findForward("edit");

		}
		if (request.getParameter("indietroAction") != null) {
			if (pForm.getInputPage() == ReturnPageEnum.OFFICE_PANEL) {
				return (mapping.findForward("cruscottoStruttura"));
			} else if (pForm.getInputPage() == ReturnPageEnum.ADMINISTRATION_PANEL) {
				return (mapping.findForward("cruscottoAmministrazione"));
			} else if (pForm.getInputPage() == ReturnPageEnum.DASHBOARD_PI) {
				return (mapping.findForward("presaInCarico"));
			} else {
				return (mapping.findForward("listaRespinti"));
			}
		}

		return mapping.getInputForward();

	}

	public void aggiornaForm(PostaInterna protocollo, PostaInternaForm form,
			HttpSession session, Utente utente, Integer ufficioId,
			Integer caricaId) {
		form.inizializzaForm();
		aggiornaDatiGeneraliForm(protocollo.getProtocollo(), form);
		aggiornaDestinatariForm(protocollo, form);
		impostaDestinatariPrecedentiForm(protocollo, form, utente, ufficioId,
				caricaId);
	}

	private static void getInputPage(HttpServletRequest request,
			PostaInternaForm form) {
		if (request.getParameter("CRUSCOTTO_STRUTTURA") != null)
			form.setInputPage(ReturnPageEnum.OFFICE_PANEL);
		else if (request.getParameter("CRUSCOTTO_AMMINISTRAZIONE") != null)
			form.setInputPage(ReturnPageEnum.ADMINISTRATION_PANEL);
		else if (request.getParameter("POSTA_INTERNA") != null) {
			form.setInputPage(ReturnPageEnum.DASHBOARD_PI);
		}
	}

	private void impostaDestinatariPrecedentiForm(PostaInterna protocollo,
			PostaInternaForm form, Utente utente, Integer ufficioId,
			Integer caricaId) {
		form.resetDestinatariPrecedenti();
		Organizzazione org = Organizzazione.getInstance();
		for (Iterator<AssegnatarioVO> i = protocollo.getDestinatari().iterator(); i.hasNext();) {
			AssegnatarioVO assegnatario = i.next();
			AssegnatarioView ass = newAssegnatario(assegnatario
					.getUfficioAssegnatarioId(), assegnatario
					.getCaricaAssegnatarioId());
			ass.setStato(assegnatario.getStatoAssegnazione());
			ass.setPresaVisione(assegnatario.isPresaVisione());
			ass.setLavorato(assegnatario.isLavorato());
			ass.setMsg(assegnatario.getMsgAssegnatarioCompetente());
			ass.setCompetente(assegnatario.isCompetente());
			ass.setCaricaAssegnanteId(assegnatario.getCaricaAssegnanteId());
			ass.setUfficioAssegnanteId(assegnatario.getUfficioAssegnanteId());
			form.aggiungiDestinatarioPrecedente(ass);

		}
		if (org.getUfficio(utente.getUfficioInUso()).isCaricaReferente(
				utente.getCaricaInUso())) {
			form.rimuoviDestinatarioPrecedente(newAssegnatario(utente
					.getUfficioInUso(), 0));

		}
		form.rimuoviDestinatarioPrecedente(newAssegnatario(utente
				.getUfficioInUso(), utente.getCaricaInUso()));
		if (ufficioId != null) {
			if (caricaId != null) {
				form.rimuoviDestinatarioPrecedente(newAssegnatario(ufficioId
						.intValue(), caricaId.intValue()));
			} else {
				form.rimuoviDestinatarioPrecedente(newAssegnatario(ufficioId
						.intValue(), 0));
			}
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

	private void aggiornaDestinatariForm(PostaInterna protocollo,
			PostaInternaForm form) {
		form.setDestinatariCompetenti(null);
		form.removeDestinatari();

	}

	protected void caricaProtocollo(HttpServletRequest request,
			ProtocolloForm form) {
	}

	private static AssegnatarioVO aggiungiDestinatarioPrecedenteModel(
			Utente utente, AssegnatarioView ass, PostaInternaForm form) {
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
		if (ass.getUtenteId() != 0) {
			assegnatario.setCaricaAssegnatarioId(CaricaDelegate.getInstance()
					.getCaricaByUtenteAndUfficio(ass.getUtenteId(),
							ass.getUfficioId()).getCaricaId());
		} else {
			assegnatario.setCaricaAssegnatarioId(0);
		}
		assegnatario.setCompetente(ass.isCompetente());
		assegnatario.setMsgAssegnatarioCompetente(ass.getMsg());
		assegnatario.setPresaVisione(ass.isPresaVisione());
		assegnatario.setLavorato(ass.isLavorato());
		return assegnatario;
	}

	private void aggiornaDestinatariModel(PostaInternaForm form,
			PostaInterna protocollo, Utente utente) {
		protocollo.removeDestinatari();
		protocollo.setMsgAssegnatarioCompetente(form
				.getMsgDestinatarioCompetente());
		//UtenteVO uteVO = utente.getValueObject();
		Collection<AssegnatarioView> assegnatariPrecedenti = form.getDestinatariPrecedenti();
		if (assegnatariPrecedenti != null) {
			for (Iterator<AssegnatarioView> i = assegnatariPrecedenti.iterator(); i.hasNext();)
				protocollo
						.aggiungiDestinatario(aggiungiDestinatarioPrecedenteModel(
								utente, i.next(), form));
		}
		Collection<AssegnatarioView> assegnatari = form.getDestinatari();
		if (assegnatari != null) {
			for (Iterator<AssegnatarioView> i = assegnatari.iterator(); i.hasNext();) {
				protocollo.getProtocollo().setStatoProtocollo("R");
				protocollo.aggiungiDestinatario(aggiungiDestinatarioModel(form,
						 i.next(), utente));
			}
		}
	}


	protected void assegnaAdUfficio(ProtocolloForm form, int ufficioId,
			Utente utente) {
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(ufficioId);
		Organizzazione org = Organizzazione.getInstance();
		Ufficio uff = org.getUfficio(ufficioId);
		ass.setNomeUfficio(uff.getValueObject().getDescription());
		ass.setDescrizioneUfficio(uff.getPath());
		PostaInternaForm pForm = (PostaInternaForm) form;
		ass.setCompetente(true);
		ass.setCaricaAssegnanteId(utente.getCaricaInUso());
		ass.setUfficioAssegnanteId(utente.getUfficioInUso());
		pForm.aggiungiDestinatario(ass);
		pForm.setDestinatarioCompetente(ass.getKey());
		form.setUfficioSelezionatoId(0);
		if (form.isDipTitolarioUfficio()) {
			form.setTitolario(null);
		}
		updateDestinatariCompetenti(pForm);
	}

	protected void assegnaAdUtente(ProtocolloForm form, Utente utente) {
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(form.getUfficioCorrenteId());
		ass.setNomeUfficio(form.getUfficioCorrente().getDescription());
		ass.setDescrizioneUfficio(form.getUfficioCorrentePath());
		ass.setUtenteId(form.getUtenteSelezionatoId());
		UtenteVO ute = form.getUtente(form.getUtenteSelezionatoId());
		ass.setNomeUtente(ute.getCaricaFullName());
		PostaInternaForm pForm = (PostaInternaForm) form;
		ass.setCompetente(true);
		ass.setCaricaAssegnanteId(utente.getCaricaInUso());
		ass.setUfficioAssegnanteId(utente.getUfficioInUso());
		pForm.aggiungiDestinatario(ass);
		pForm.setDestinatarioCompetente(ass.getKey());
		if (form.isDipTitolarioUfficio()) {
			form.setTitolario(null);
		}
		updateDestinatariCompetenti(pForm);
	}

	protected void resetForm(ProtocolloForm form, Utente utente,
			HttpServletRequest request) {

	}

	private void updateDestinatariCompetenti(PostaInternaForm form) {
		List<String> assCompetenti = new ArrayList<String>();
		for (Iterator<AssegnatarioView> i = form.getDestinatari().iterator(); i.hasNext();) {
			AssegnatarioView ass = i.next();
			if (ass.isCompetente()) {
				assCompetenti.add(ass.getKey());
			}
		}
		form.setDestinatariCompetenti(assCompetenti.toArray(new String[0]));
	}

	private void rimuovoDestinatari(PostaInternaForm form) {
		String[] assegnatari = form.getDestinatariSelezionatiId();
		String assegnatarioCompetente = form.getDestinatarioCompetente();
		if (assegnatari != null) {
			for (int i = 0; i < assegnatari.length; i++) {
				String assegnatario = assegnatari[i];
				if (assegnatario != null) {
					form.rimuoviDestinatario(assegnatario);
					if (assegnatario.equals(assegnatarioCompetente)) {
						form.setDestinatarioCompetente(null);
					}
				}
			}
		}
	}

	private AssegnatarioVO aggiungiDestinatarioModel(PostaInternaForm form,
			AssegnatarioView ass, Utente utente) {
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
		if (ass.getUtenteId() != 0) {
			assegnatario.setCaricaAssegnatarioId(CaricaDelegate.getInstance()
					.getCaricaByUtenteAndUfficio(ass.getUtenteId(),
							ass.getUfficioId()).getCaricaId());
		} else {
			assegnatario.setCaricaAssegnatarioId(0);
		}
		if (form.isCompetente(ass)) {
			assegnatario.setCompetente(true);
			assegnatario.setMsgAssegnatarioCompetente(form
					.getMsgDestinatarioCompetente());

		}
		assegnatario.setPresaVisione(false);
		return assegnatario;
	}

}
