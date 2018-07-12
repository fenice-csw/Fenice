package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.compit.fenice.model.protocollo.PostaInterna;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.DocumentoDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ScaricoForm;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.flosslab.mvc.presentation.action.protocollo.helper.file.ProtocolloFileUtility;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

public class ScaricoAction extends Action {

	static Logger logger = Logger.getLogger(ScaricoAction.class.getName());

	public final static String PROTOCOLLI_ASSEGNATI = "A";

	public final static String PROTOCOLLI_AGLI_ATTI = "A";

	public final static String PROTOCOLLI_IN_LAVORAZIONE = "N";

	public final static String PROTOCOLLI_IN_RISPOSTA = "R";

	public ActionForward downloadDocumento(ActionMapping mapping,
			ReportProtocolloView prot, HttpServletRequest request,
			HttpServletResponse response, int aooId) throws Exception {
		DocumentoVO doc = DocumentoDelegate.getInstance().getDocumento(
				prot.getDocumentoId());
		ActionMessages errors = ProtocolloFileUtility.downloadFileProtocollo(
				doc, response, aooId);
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return mapping.findForward("erroreDownload");
		}
		return null;
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		HttpSession session = request.getSession(true);
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		ScaricoForm scarico = (ScaricoForm) form;
		int numeroProtocolloDa = 0;
		int annoProtocolloDa = 0;
		int annoProtocolloA = 0;
		String tipoUtenteUfficio = "U"; // tipo utente
		if (request.getParameter("numeroProtocolloDa") != null
				&& !"".equals(request.getParameter("numeroProtocolloDa"))) {
			numeroProtocolloDa = Integer.parseInt(request
					.getParameter("numeroProtocolloDa"));
		}
		if (request.getParameter("annoProtocolloDa") != null
				&& !"".equals(request.getParameter("annoProtocolloDa"))) {
			annoProtocolloDa = Integer.parseInt(request
					.getParameter("annoProtocolloDa"));
		}
		if (request.getParameter("annoProtocolloA") != null
				&& !"".equals(request.getParameter("annoProtocolloA"))) {
			annoProtocolloA = Integer.parseInt(request
					.getParameter("annoProtocolloA"));
		}
		if (form == null) {
			logger.info(" Creating new ScaricoAction");
			form = new ScaricoForm();
			session.setAttribute(mapping.getAttribute(), form);
		}
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		if (request.getParameter("checkPostaInterna") != null) {
			Integer checkId = new Integer(
					request.getParameter("checkPostaInterna"));
			if (checkId != null) {
				boolean updated = delegate.updateNotificaPostaInterna(checkId);
				if (updated)
					scarico.removeCheckPI(checkId);
				return (mapping.findForward("checkPostaInterna"));
			}
		} else if (request.getParameter("btnLeggiTutteNotifiche") != null) {
			delegate.updateNotifichePostaInternaUtente(utente.getCaricaInUso());
			scarico.setCheckPI(delegate.getNotifichePostaInternaView(
					utente.getCaricaInUso(), 0));
			return (mapping.findForward("checkPostaInterna"));

		} else if (request.getParameter("rispondiProtocollo") != null) {
			Integer protocolloId = new Integer(
					request.getParameter("rispondiProtocollo"));
			if (protocolloId != null) {
				request.setAttribute("risposta", Boolean.TRUE);
				request.setAttribute("protocolloId", protocolloId);
				return (mapping.findForward("creaProtocolloRisposta"));
			}
		} else if (request.getParameter("rispondiPosta") != null) {
			Integer protocolloId = new Integer(
					request.getParameter("rispondiPosta"));
			if (protocolloId != null) {
				request.setAttribute("risposta", Boolean.TRUE);
				request.setAttribute("protocolloId", protocolloId);
				return (mapping.findForward("creaProtocolloRisposta"));
			}
		} else if (request.getParameter("rifiutaProtocollo") != null) {
			request.setAttribute("protocolloId",
					new Integer(request.getParameter("rifiutaProtocollo")));
			return mapping.findForward("rifiutaProtocollo");
		} else if (request.getParameter("assegnaProcedimento") != null) {
			request.setAttribute("protocolloId",
					new Integer(request.getParameter("assegnaProcedimento")));
			session.setAttribute("PresaInCarico", true);
			return mapping.findForward("aggiungiProcedimento");
		} else if (request.getParameter("rifiutaPosta") != null) {
			Integer protocolloId = new Integer(
					request.getParameter("rifiutaPosta"));
			if (protocolloId != null) {
				PostaInterna pi = delegate.getPostaInternaById(Integer
						.parseInt(request.getParameter("rifiutaPosta")));
				ProtocolloVO protocolloVO = pi.getProtocollo();
				protocolloVO.setDataScarico(null);
				if (scarico.getMsgAssegnatarioCompetente() != null
						&& !"".equals(scarico.getMsgAssegnatarioCompetente()
								.trim())) {
					aggiornaMsgRifiutoAssegnatari(
							scarico.getMsgAssegnatarioCompetente(), pi);
				}
				delegate.rifiutaPosta(pi, "R", "F", utente);
			}
			scarico.setProtocolloScarico(null);
			scarico.removeProtocolliScarico();
			scarico.setProtocolliScarico(delegate.getProtocolliAssegnati(
					utente, annoProtocolloDa, annoProtocolloA,
					numeroProtocolloDa, tipoUtenteUfficio));
			messages.add("operazione_ok", new ActionMessage("operazione_ok"));
			saveMessages(request, messages);
		} else if (request.getParameter("btnRiassegna") != null) {
			String[] protocolliAccettati = scarico.getProtocolloScaricoChkBox();
			if (protocolliAccettati != null) {
				request.setAttribute("protocolloId", new Integer(
						protocolliAccettati[0]));
				session.setAttribute("protocolloIdSelezionati",
						protocolliAccettati);
				return (mapping.findForward("riassegnazioneMultiplaProtocollo"));
			}
		} else if (request.getParameter("btnFascicola") != null) {
			session.setAttribute("PAGINA_RITORNO", "SCARICO_UTENTE");
			String[] protocolliDaFascicolare = scarico
					.getProtocolloScaricoChkBox();
			if (protocolliDaFascicolare != null) {
				session.setAttribute("protocolliIds", protocolliDaFascicolare);
				request.setAttribute("protocolloId", new Integer(
						protocolliDaFascicolare[0]));
				return (mapping.findForward("fascicolazioneMultiplaProtocolli"));
			}
		} else if (request.getParameter("riassegnaPosta") != null) {
			request.setAttribute("protocolloId",
					new Integer(request.getParameter("riassegnaPosta")));
			return (mapping.findForward("riassegnaProtocollo"));

		} else if (request.getParameter("riassegnaProtocollo") != null) {
			request.setAttribute("protocolloId",
					new Integer(request.getParameter("riassegnaProtocollo")));
			return (mapping.findForward("riassegnaProtocollo"));

		} else if (request.getParameter("presaVisionePostaInterna") != null) {
			request.setAttribute(
					"protocolloId",
					new Integer(request
							.getParameter("presaVisionePostaInterna")));
			request.setAttribute("presaVisione", true);
			if (scarico.isUfficio())
				session.setAttribute("PAGINA_RITORNO", "SCARICO_UFFICIO");
			else
				session.setAttribute("PAGINA_RITORNO", "SCARICO_UTENTE");
			return (mapping.findForward("presaVisionePostaInterna"));
		} else if (request.getParameter("presaVisione") != null) {
			request.setAttribute("protocolloId",
					new Integer(request.getParameter("presaVisione")));
			request.setAttribute("presaVisione", true);
			if (scarico.isUfficio())
				session.setAttribute("PAGINA_RITORNO", "CONOSCENZA_UFFICIO");
			else
				session.setAttribute("PAGINA_RITORNO", "CONOSCENZA_UTENTE");
			return (mapping.findForward("fascicolaProtocolloIngresso"));
		} else if (request.getParameter("visionaProtocollo") != null) {
			request.setAttribute("protocolloId",new Integer(request.getParameter("visionaProtocollo")));
			request.setAttribute("presaVisione", true);
			if (scarico.isUfficio())
				session.setAttribute("PAGINA_RITORNO", "CONOSCENZA_UFFICIO");
			else
				session.setAttribute("PAGINA_RITORNO", "CONOSCENZA_UTENTE");
			return (mapping.findForward("visionaProtocollo"));
		} else if (request.getParameter("lavoratoProcedimento") != null) {
			request.setAttribute("protocolloId",
					new Integer(request.getParameter("lavoratoProcedimento")));
			session.setAttribute("PAGINA_RITORNO", "SCARICO_UTENTE");
			return (mapping.findForward("lavoratoProcedimento"));
		} else if (request.getParameter("protocolloSelezionato") != null) {
			request.setAttribute("protocolloId",
					new Integer(request.getParameter("protocolloSelezionato")));
			return (mapping.findForward("visualizzaProtocolloIngresso"));
		} else if (request.getParameter("daFascicolare") != null) {
			request.setAttribute("protocolloId",
					new Integer(request.getParameter("daFascicolare")));
			session.setAttribute("PAGINA_RITORNO", "SCARICO_UTENTE");
			return (mapping.findForward("fascicolaProtocolloIngresso"));
		} else if (request.getParameter("postaSelezionato") != null) {
			request.setAttribute("protocolloId",
					new Integer(request.getParameter("postaSelezionato")));
			return (mapping.findForward("visualizzaPostaInterna"));
		} else if (request.getParameter("postaDaFascicolare") != null) {
			request.setAttribute("protocolloId",
					new Integer(request.getParameter("postaDaFascicolare")));
			if (scarico.isUfficio())
				session.setAttribute("PAGINA_RITORNO", "SCARICO_UFFICIO");
			else
				session.setAttribute("PAGINA_RITORNO", "SCARICO_UTENTE");
			return (mapping.findForward("fascicolaPostaInterna"));
		} else if (request.getParameter("btnFascicolaPosta") != null) {
			if (scarico.isUfficio())
				session.setAttribute("PAGINA_RITORNO", "SCARICO_UFFICIO");
			else
				session.setAttribute("PAGINA_RITORNO", "SCARICO_UTENTE");
			String[] protocolliAccettati = scarico.getProtocolloScaricoChkBox();
			request.setAttribute("protocolloId", new Integer(
					protocolliAccettati[0]));
			session.setAttribute("protocolliIds", protocolliAccettati);
			return (mapping.findForward("fascicolazioneMultiplaProtocolli"));
		} else if (request.getParameter("btnInviaRepertorio") != null) {
			request.setAttribute("protocolloId", new Integer(request.getParameter("btnInviaRepertorio")));
			return (mapping.findForward("repertorio"));
		}
		else if (scarico.getBtnCerca() != null) {
			GregorianCalendar today = new GregorianCalendar();
			scarico.setBtnCerca(null);
			scarico.setProtocolloScarico(null);
			scarico.setMsgAssegnatarioCompetente(null);

			int maxRighe=Integer.parseInt(utente.getAreaOrganizzativa().getMaxRighe());
			int contaRighe = 0;
			if (!scarico.getTipoProtocollo().equals("posta")) {
				contaRighe = delegate.contaProtocolliAssegnati(utente,
						today.get(Calendar.YEAR) - 1, today.get(Calendar.YEAR),
						numeroProtocolloDa, "T");
			} else {
				if (!scarico.isUfficio())
					contaRighe = delegate.contaPostaInternaAssegnataPerNumero(
							utente, numeroProtocolloDa, "T");
				else
					contaRighe = delegate.contaPostaInternaAssegnataPerNumero(
							utente, numeroProtocolloDa, "U");

			}
			if (contaRighe == 0) {
				errors.add("nessun_dato", new ActionMessage("nessun_dato", "",
						""));
			} else if (contaRighe <= maxRighe) {
				Map<Long,ReportProtocolloView> protocolli = new HashMap<Long,ReportProtocolloView>();
				if (!scarico.getTipoProtocollo().equals("posta")) {
					protocolli = delegate.getProtocolliAssegnati(utente,
							today.get(Calendar.YEAR) - 1,
							today.get(Calendar.YEAR), numeroProtocolloDa, "T");
				} else {
					if (!scarico.isUfficio())
						protocolli = delegate
								.getPostaInternaAssegnataPerNumero(utente,
										numeroProtocolloDa, "T");
					else
						protocolli = delegate
								.getPostaInternaAssegnataPerNumero(utente,
										numeroProtocolloDa, "U");
				}

				scarico.setProtocolliScarico(protocolli);
				return (mapping.findForward("input"));

			} else {
				errors.add("controllo.maxrighe", new ActionMessage(
						"controllo.maxrighe", "" + contaRighe,
						"protocolli scaricati/riassegnati", "" + maxRighe));
			}
		} else if (request.getParameter("downloadDocprotocolloSelezionato") != null) {
			String param = request
					.getParameter("downloadDocprotocolloSelezionato");
			if (param != null) {
				long annoNumero = Long.valueOf(param);
				int aooId = utente.getAreaOrganizzativa().getId();
				ReportProtocolloView prot = (ReportProtocolloView) scarico
						.getProtocolliScarico().get(annoNumero);
				return downloadDocumento(mapping, prot, request, response,
						aooId);
			}
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		scarico.removeProtocolliScarico();
		logger.info("Execute ScaricoAction");
		return (mapping.findForward("input"));
	}

	private void aggiornaMsgRifiutoAssegnatari(
			String msgAssegnatarioCompetente, PostaInterna protocollo) {
		Collection<AssegnatarioVO> assegnatari = protocollo.getDestinatari();
		if (assegnatari != null) {
			for (Iterator<AssegnatarioVO> i = assegnatari.iterator(); i.hasNext();) {
				AssegnatarioVO assegnatario = i.next();
				if (assegnatario.isCompetente()) {
					assegnatario
							.setMsgAssegnatarioCompetente(msgAssegnatarioCompetente);
					protocollo
							.setMsgAssegnatarioCompetente(msgAssegnatarioCompetente);
				}
			}
		}
	}

}
