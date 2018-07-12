package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.compit.fenice.enums.ReturnPageEnum;
import it.compit.fenice.enums.TipoVisibilitaUfficioEnum;
import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.presentation.action.protocollo.helper.file.FascicoloFileUtility;
import it.compit.fenice.mvc.presentation.actionform.documentale.EditorForm;
import it.compit.fenice.mvc.presentation.actionform.protocollo.PostaInternaForm;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.compit.fenice.mvc.vo.protocollo.DocumentoFascicoloVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.Fascicolo;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.Protocollo;
import it.finsiel.siged.mvc.bo.AlberoUfficiBO;
import it.finsiel.siged.mvc.bo.TitolarioBO;
import it.finsiel.siged.mvc.business.DocumentaleDelegate;
import it.finsiel.siged.mvc.business.FascicoloDelegate;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.business.MenuDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.documentale.DocumentoForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.FaldoneForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.FascicoloForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloIngressoForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloUscitaForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.presentation.helper.FascicoloView;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.vo.documentale.FileVO;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.lookup.TipoDocumentoVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.ProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloFascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.report.protocollo.CommonReportDS;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;
import it.finsiel.siged.util.StringUtil;
import it.flosslab.mvc.presentation.action.protocollo.helper.file.ProtocolloFileUtility;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

public class FascicoloAction extends Action {

	// ----------------------------------------------------- Instance Variables

	static Logger logger = Logger.getLogger(FascicoloAction.class.getName());

	private static void getInputPage(HttpServletRequest request, FascicoloForm form) {
    	if (request.getParameter("DOCVIEW_IN") != null)
			form.setInputPage(ReturnPageEnum.DOCVIEW_IN);
		else if (request.getParameter("DOCVIEW_OUT") != null)
			form.setInputPage(ReturnPageEnum.DOCVIEW_OUT);
		else if (request.getParameter("DOCVIEW_PI") != null)
			form.setInputPage(ReturnPageEnum.DOCVIEW_PI);
		else if (request.getParameter("DOC_IN") != null)
			form.setInputPage(ReturnPageEnum.DOC_IN);
		else if (request.getParameter("DOC_OUT") != null)
			form.setInputPage(ReturnPageEnum.DOC_OUT);
		else if (request.getParameter("DOC_PI") != null)
			form.setInputPage(ReturnPageEnum.DOC_PI);
		else if (request.getParameter("SEARCH_FOLDER") != null)
			form.setInputPage(ReturnPageEnum.SEARCH_FOLDER);
	}

	public void stampaFrontespizio(HttpServletResponse response,
			FascicoloForm form, String aooName) throws IOException,
			ServletException {
		ServletContext context = this.getServlet().getServletContext();
		OutputStream os = response.getOutputStream();
		try {

			File reportFile = new File(context.getRealPath("/")
					+ FileConstants.STAMPA_FRONTESPIZIO_TEMPLATE);
			JasperDesign jasperDesign = JRXmlLoader.load(context
					.getRealPath("/")
					+ FileConstants.STAMPA_FRONTESPIZIO_TEMPLATE);
			JasperReport jasperReport = JasperCompileManager
					.compileReport(jasperDesign);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("aoo_name", aooName);
			parameters.put("progressivo", form.getAnnoProgressivo());
			parameters.put("oggetto", form.getOggettoFascicolo());
			parameters.put("categoria", form.getTitolario().getDescrizione());
			if (form.getPadre() != null)
				parameters.put("padre", form.getPadre());
			else
				parameters.put("padre", "");
			parameters.put("referente", form.getMittente().getNomeUtente());
			if (form.getIstruttore() != null)
				parameters.put("istruttore", form.getIstruttore().getNomeUtente());
			else
				parameters.put("istruttore", "");
			
			parameters.put("delegato", form.getDelegato());
			parameters.put("interessato", form.getInteressato());
			
			parameters.put("posizione", form.getDescrizioneStato());
			parameters.put("tipo", form.getDescrizioneTipoFascicolo());
			parameters.put("trattato_da", form.getResponsabile());
			parameters.put("anno", String.valueOf(form.getAnnoRiferimento()));
			parameters.put("data_creazione", form.getDataApertura());
			if (form.getDataUltimoMovimento() != null)
				parameters.put("data_ultimo", form.getDataUltimoMovimento());
			else
				parameters.put("data_ultimo", "");
			if (form.getNote() != null)
				parameters.put("note", form.getNote());
			else
				parameters.put("note", "");
			parameters.put("BaseDir", reportFile.getParentFile());
			// lo devo cambiare -> cambiare il jasper i dati vanno nel header
			ArrayList<Integer> l = new ArrayList<Integer>();
			l.add(new Integer(1));
			CommonReportDS ds = new CommonReportDS(l);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			// fine dello cambiamento
			response.setHeader("Content-Disposition",
					"attachment;filename=Frontespizio_"
							+ form.getAnnoProgressivo() + ".pdf");
			response.setHeader("Cache-control", "");
			response.setContentType("application/pdf");
			JRPdfExporter exporter = new JRPdfExporter();
			exporter
					.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
			exporter.exportReport();
		} catch (Exception e) {
			logger.error("", e);
			response.setContentType("text/plain");
			e.printStackTrace(new PrintStream(os));
		} finally {
			os.close();
		}
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
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		HttpSession session = request.getSession();
		FascicoloForm fascicoloForm = (FascicoloForm) form;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		int aooId = utente.getAreaOrganizzativa().getId().intValue();
		fascicoloForm.setAooId(aooId);
		boolean indietroVisibile = false;
		fascicoloForm.setIndietroVisibile(indietroVisibile);
		boolean ufficioCompleto = true;
		getInputPage(request, fascicoloForm);
		if (form == null) {
			logger.info(" Creating new FascicoloAction");
			form = new FascicoloForm();
			session.setAttribute(mapping.getAttribute(), form);
		}
		if (fascicoloForm.getUfficioCorrenteId() == 0) {
			AlberoUfficiBO.impostaUfficioUtenti(utente, fascicoloForm,ufficioCompleto);
			fascicoloForm.setUtenteAbilitatoSuUfficio(utente.isUtenteAbilitatoSuUfficio(fascicoloForm.getUfficioCorrenteId()));
		}
		if (request.getParameter("downloadAllegatoId") != null) {
			int docId = NumberUtil.getInt((String) request.getParameter("downloadAllegatoId"));
			DocumentoVO doc = fascicoloForm.getRiferimento(String.valueOf(docId));
			
			return downloadDocumento(mapping, doc, request, response);
		}
		if (request.getParameter("duplicaFascicoloProtocollo") != null) {
			int protocolloId = NumberUtil.getInt((String) request
					.getParameter("duplicaFascicoloProtocollo"));
			ReportProtocolloView r = ProtocolloDelegate.getInstance()
					.getProtocolloView(protocolloId);
			request.setAttribute("protocolloId", new Integer(protocolloId));
			
			if (r.getTipoProtocollo().equals("I")){
				session.setAttribute("PAGINA_RITORNO","FASCICOLO");
				return (mapping.findForward("rifascicolaProtocolloIngresso"));
			}
			else if (r.getTipoProtocollo().equals("P")){
				session.setAttribute("PAGINA_RITORNO","FASCICOLO");
				return (mapping.findForward("rifascicolaPostaInterna"));
			}
			else {
				session.setAttribute("tornaFascicolaUscitaEdit", true);
				return (mapping.findForward("rifascicolaProtocolloUscita"));
			}
		}
		if (request.getParameter("downloadAllegatoTitolarioId") != null) {
			int docId = NumberUtil.getInt((String) request
					.getParameter("downloadAllegatoTitolarioId"));
			DocumentoVO doc = fascicoloForm.getTitolario().getRiferimento(
					String.valueOf(docId));
			return downloadDocumento(mapping, doc, request, response);
		}
		if (request.getParameter("tornaFascicoloPadre") != null) {
			fascicoloForm = (FascicoloForm) session
					.getAttribute("fascicoloFiglio");
			fascicoloForm.setOggettoFascicolo((String) session
					.getAttribute("fascicoloOggetto"));
			Integer padreId = (Integer) session.getAttribute("fascicoloPadre");
			if (padreId != null)
				impostaParentForm(fascicoloForm, utente, padreId);
			session.removeAttribute("fascicoloPadre");
			session.removeAttribute("fascicoloFiglio");
			session.removeAttribute("fascicoloOggetto");
			return mapping.findForward("fascicolo");
		}

		if (request.getParameter("btnNuovo") != null) {
			fascicoloForm.setFaldoneOggetto(null);
			fascicoloForm.setFaldoneCodiceLocale(null);
			int titolarioId = 0;
			MessageResources bundle = (MessageResources) request
					.getAttribute(Globals.MESSAGES_KEY);
			fascicoloForm.setCollocazioneLabel1(bundle
					.getMessage("fascicolo.collocazione.label1"));
			fascicoloForm.setCollocazioneLabel2(bundle
					.getMessage("fascicolo.collocazione.label2"));
			fascicoloForm.setCollocazioneLabel3(bundle
					.getMessage("fascicolo.collocazione.label3"));
			fascicoloForm.setCollocazioneLabel4(bundle
					.getMessage("fascicolo.collocazione.label4"));
			if (session.getAttribute("protocolloForm") != null) {
				ProtocolloForm pForm = (ProtocolloForm) session
						.getAttribute("protocolloForm");
				if (pForm != null && pForm.getTitolario() != null) {
					titolarioId = pForm.getTitolario().getId().intValue();
				}
			}
			impostaNuovoFascicoloForm(titolarioId, fascicoloForm, utente,
					session);
			AlberoUfficiBO.impostaUfficio(utente, fascicoloForm,
					ufficioCompleto);
			assegnaAdUfficio(fascicoloForm, fascicoloForm
					.getUfficioCorrenteId());
			AlberoUfficiBO.impostaUfficioUtenti(utente, fascicoloForm,
					ufficioCompleto);
			if (Boolean.TRUE.equals(session.getAttribute("tornaFaldone"))) {
				fascicoloForm.setFaldoneCodiceLocale((String) request
						.getAttribute("codiceLocaleFaldone"));
				fascicoloForm.setFaldoneOggetto((String) request
						.getAttribute("oggettoFaldone"));
				indietroVisibile = true;
				fascicoloForm.setIndietroVisibile(indietroVisibile);
			}
			if (Boolean.TRUE.equals(session.getAttribute("tornaProtocollo"))) {
				indietroVisibile = true;
				fascicoloForm.setIndietroVisibile(indietroVisibile);
			}
			if (Boolean.TRUE.equals(session.getAttribute("tornaEditor"))) {
				indietroVisibile = true;
				fascicoloForm.setIndietroVisibile(indietroVisibile);
			}
			if (Boolean.TRUE.equals(session.getAttribute("tornaTemplate"))) {
				indietroVisibile = true;
				fascicoloForm.setIndietroVisibile(indietroVisibile);
			}
			return (mapping.findForward("fascicolo"));
		} else if (fascicoloForm.getUfficioCorrenteId() == 0) {
			AlberoUfficiBO.impostaUfficio(utente, fascicoloForm,
					ufficioCompleto);
			return (mapping.findForward("fascicolo"));
		} else if (request.getParameter("cercaFascicoloPadreAction") != null) {
			request.setAttribute("cercaFascicoloPadre", fascicoloForm
					.getOggettoFascicoloPadre());
			session.setAttribute("tornaFascicolo", Boolean.TRUE);
			session.setAttribute("fascicoloFiglio", fascicoloForm);
			return (mapping.findForward("cercaFascicolo"));
		} else if (request.getParameter("cercaFascicoloCollegatoAction") != null) {
			request.setAttribute("cercaFascicoloCollegato", fascicoloForm
					.getOggettoFascicoloCollegato());
			session.setAttribute("tornaFascicoloCollegato", Boolean.TRUE);
			session.setAttribute("fascicoloAllegatoId", fascicoloForm.getId());
			request.setAttribute("resetForm", true);
			return (mapping.findForward("cercaFascicolo"));
		} else if (request.getParameter("rimuoviCollegatiAction") != null) {
			removeCollegamenti(fascicoloForm);
			request.setAttribute("fascicoloId", new Integer(fascicoloForm
					.getId()));
		} else if (request.getParameter("visualizzaCollegamentoId") != null) {
			Integer fascicoloId = Integer.valueOf(request.getParameter("visualizzaCollegamentoId"));
			if (fascicoloId != null && fascicoloId > 0) {
				request.setAttribute("ritornoFascicoloId", new Integer(
						fascicoloForm.getId()));
				request.setAttribute("fascicoloId", fascicoloId);
				return (mapping.findForward("visualizzaCollegamento"));
			}
		} else if (request.getParameter("allegaRiferimentoAction") != null) {
			FascicoloFileUtility.uploadFile(fascicoloForm, request, errors);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			}
			return mapping.findForward("fascicolo");
		} else if (request.getParameter("rimuoviRiferimentiAction") != null) {
			String[] allegati = fascicoloForm.getRiferimentiLegislativiId();
			if (allegati != null) {
				for (int i = 0; i < allegati.length; i++) {
					fascicoloForm.rimuoviRiferimento(allegati[i]);
					allegati[i] = null;
				}
			}
			return mapping.findForward("fascicolo");
		} else if (request.getParameter("indietroCercaPersonaFisica") != null) {
			return (mapping.findForward("fascicolo"));
		} else if (request.getParameter("annullaPadreAction") != null) {
			impostaParentForm(fascicoloForm, utente, 0);
			return (mapping.findForward("fascicolo"));
		} else if (request.getParameter("annullaInteressatoAction") != null) {
			fascicoloForm.setInteressato(null);
			return (mapping.findForward("fascicolo"));
		} else if (request.getParameter("annullaDelegatoAction") != null) {
			fascicoloForm.setDelegato(null);
			return (mapping.findForward("fascicolo"));
		} else if (session.getAttribute("interessato") != null) {
			SoggettoVO interessato = (SoggettoVO) session
					.getAttribute("interessato");
			session.removeAttribute("interessato");
			fascicoloForm.setInteressato(interessato.getCognome()+" "+interessato.getNome());
			fascicoloForm.setIndiInteressato(interessato.getIndirizzoCompleto());
			return (mapping.findForward("fascicolo"));
		} else if (session.getAttribute("delegato") != null) {
			SoggettoVO delegato = (SoggettoVO) session.getAttribute("delegato");
			session.removeAttribute("delegato");
			fascicoloForm.setDelegato(delegato.getCognome()+" "+delegato.getNome());
			fascicoloForm.setIndiDelegato(delegato.getIndirizzoCompleto());
			return (mapping.findForward("fascicolo"));
		} else if (request.getParameter("cercaSoggettoAction") != null) {
			session.setAttribute("tornaFascicolo", Boolean.TRUE);
			request.setAttribute("cognome", "");
			request.setAttribute("nome", "");
			session.setAttribute("provenienza", "personaFisicaNuovoFascicolo");
			if (request.getParameter("cercaSoggettoAction").equals(
					"seleziona l'interessato"))
				session.setAttribute("isInteressato", true);
			return mapping.findForward("cercaPersonaFisica");
		} else if (request.getParameter("impostaUfficioAction") != null) {
			if (utente.getUfficioVOInUso().getTipo().equals(
					UfficioVO.UFFICIO_SEMICENTRALE)
					|| utente.isUtenteAbilitatoSuUfficio(fascicoloForm
							.getUfficioSelezionatoId())) {
				fascicoloForm.setUfficioCorrenteId(fascicoloForm
						.getUfficioSelezionatoId());
				AlberoUfficiBO.impostaUfficioUtenti(utente, fascicoloForm,
						ufficioCompleto);
				assegnaAdUfficio(fascicoloForm, fascicoloForm
						.getUfficioSelezionatoId());
				impostaTitolario(fascicoloForm, utente, 0);
			} else {
				errors.add("utente_non_abilitato_ufficio", new ActionMessage(
						"utente_non_abilitato_ufficio", utente.getValueObject()
								.getUsername(), ""));
				saveErrors(request, errors);
			}
			return (mapping.findForward("fascicolo"));
		} else if (request.getParameter("ufficioPrecedenteAction") != null) {
			if (fascicoloForm.getUfficioCorrente().getParentId() > 0
					&& (utente.getUfficioVOInUso().getTipo().equals(
							UfficioVO.UFFICIO_SEMICENTRALE) || utente
							.isUtenteAbilitatoSuUfficio(fascicoloForm
									.getUfficioCorrente().getParentId()))) {
				fascicoloForm.setUfficioCorrenteId(fascicoloForm
						.getUfficioCorrente().getParentId());
				AlberoUfficiBO.impostaUfficioUtenti(utente, fascicoloForm,
						ufficioCompleto);
				assegnaAdUfficio(fascicoloForm, fascicoloForm
						.getUfficioCorrenteId());
				impostaTitolario(fascicoloForm, utente, 0);
			}
			return (mapping.findForward("fascicolo"));
		} else if (request.getParameter("assegnaUtenteAction") != null) {
			assegnaAdUtente(fascicoloForm);
			return mapping.findForward("fascicolo");
		} else if (request.getParameter("impostaTitolarioAction") != null) {
			impostaTitolario(fascicoloForm, utente, fascicoloForm
					.getTitolarioSelezionatoId());
			return (mapping.findForward("fascicolo"));
		} else if (request.getParameter("titolarioPrecedenteAction") != null) {
			impostaTitolario(fascicoloForm, utente, 0);
			fascicoloForm.setTitolario(null);
			return (mapping.findForward("fascicolo"));
		} else if (request.getParameter("btnModifica") != null) {
			caricaFascicolo(request, fascicoloForm);
			FascicoloVO fVO = FascicoloDelegate.getInstance().getFascicoloVOById(Integer.parseInt(request.getParameter("id")));
			MessageResources bundle = (MessageResources) request
					.getAttribute(Globals.MESSAGES_KEY);
			if (fVO.getCollocazioneLabel1() == null) {
				fVO.setCollocazioneLabel1(bundle
						.getMessage("fascicolo.collocazione.label1"));
			}
			if (fVO.getCollocazioneLabel2() == null) {
				fVO.setCollocazioneLabel2(bundle
						.getMessage("fascicolo.collocazione.label2"));
			}
			if (fVO.getCollocazioneLabel3() == null) {
				fVO.setCollocazioneLabel3(bundle
						.getMessage("fascicolo.collocazione.label3"));
			}
			if (fVO.getCollocazioneLabel4() == null) {
				fVO.setCollocazioneLabel4(bundle
						.getMessage("fascicolo.collocazione.label4"));
			}
			impostaFascicoloForm(fVO, fascicoloForm, utente);
			AlberoUfficiBO.impostaUfficioUtenti(utente, fascicoloForm,
					ufficioCompleto);
			return (mapping.findForward("fascicolo"));

		} else if ((request.getParameter("btnConferma") != null)
				|| (request.getParameter("btnConfermaNew") != null)) {
			errors = fascicoloForm.validate(mapping, request);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return (mapping.findForward("fascicolo"));
			}
			FascicoloVO fVO = new FascicoloVO();
			if (Boolean.TRUE.equals(session.getAttribute("tornaProtocollo"))) {
				session.removeAttribute("tornaProtocollo");
				ProtocolloForm pForm = (ProtocolloForm) session
						.getAttribute("protocolloForm");
				fVO = impostaFascicoloVO(fascicoloForm, utente);
				fVO.setOwner(true);
				pForm.aggiungiFascicolo(fVO);
				impostaTitolario(pForm, utente, fVO.getTitolarioId());

				if (pForm instanceof ProtocolloIngressoForm) {
					ProtocolloIngressoForm piForm = (ProtocolloIngressoForm) pForm;
					if (piForm.isDaBtnModifica())
						return (mapping
								.findForward("tornaProtocolloIngressoNew"));
					else
						return (mapping.findForward("tornaProtocolloIngresso"));
				} else if (pForm instanceof ProtocolloUscitaForm) {
					if (session.getAttribute("tornaFascicolaUscitaEdit") != null) {
						session.removeAttribute("tornaFascicolaUscitaEdit");
						return (mapping
								.findForward("tornaFascicolaProtocolloUscita"));
					} else
						return (mapping.findForward("tornaProtocolloUscita"));
				} else {
					PostaInternaForm postaForm = (PostaInternaForm) pForm;
					if (postaForm.getProtocolloId() == 0
							|| postaForm.isDaBtnModifica())
						return (mapping.findForward("tornaPostaInternaNew"));
					else
						return (mapping.findForward("tornaPostaInterna"));
				}
			}
			// EDITOR
			else if (Boolean.TRUE.equals(session.getAttribute("tornaEditor"))) {
				session.removeAttribute("tornaEditor");
				EditorForm dForm = (EditorForm) session
						.getAttribute("editorForm");
				fVO = impostaFascicoloVO(fascicoloForm, utente);
				fVO.setOwner(true);
				dForm.aggiungiFascicolo(fVO);
				return (mapping.findForward("tornaInvioProtocolloEditor"));
			} else if (Boolean.TRUE.equals(session
					.getAttribute("tornaTemplate"))) {
				session.removeAttribute("tornaTemplate");
				EditorForm dForm = (EditorForm) session
						.getAttribute("editorForm");
				fVO = impostaFascicoloVO(fascicoloForm, utente);
				fVO.setOwner(true);
				dForm.aggiungiFascicolo(fVO);
				return (mapping.findForward("tornaTemplate"));
			} else if (Boolean.TRUE.equals(session
					.getAttribute("tornaDocumento"))) {
				session.removeAttribute("tornaDocumento");
				DocumentoForm pForm = (DocumentoForm) session
						.getAttribute("documentoForm");
				fVO = impostaFascicoloVO(fascicoloForm, utente);
				pForm.aggiungiFascicolo(fVO);
				impostaTitolario(pForm, utente, fVO.getTitolarioId());
				return (mapping.findForward("tornaDocumento"));
			} else if (Boolean.TRUE
					.equals(session.getAttribute("tornaFaldone"))) {
				FaldoneForm fForm = (FaldoneForm) session
						.getAttribute("faldoneForm");
				fVO = impostaFascicoloVO(fascicoloForm, utente);
				FascicoloVO nuovo = FascicoloDelegate.getInstance()
						.nuovoFascicolo(fVO);
				if (ReturnValues.SAVED == nuovo.getReturnValue()) {
					Fascicolo f = new Fascicolo();
					f.setFascicoloVO(nuovo);
					fForm.aggiungiFascicolo(f);
					session.removeAttribute("tornaFaldone");
					return (mapping.findForward("tornaFaldone"));
				} else {
					errors.add("fascicolo", new ActionMessage(
							"errore_nel_salvataggio"));
				}
			} else if (Boolean.TRUE.equals(session.getAttribute("tornaAlert"))) {
				session.removeAttribute("tornaAlert");
				return (mapping.findForward("tornaAlert"));

			} else {
				FascicoloDelegate fd = FascicoloDelegate.getInstance();
				if (fascicoloForm.getId() > 0) {
					fVO = impostaFascicoloVO(fascicoloForm, utente);
					fVO.setDataCarico((new Date(System.currentTimeMillis())));
					
					fVO = fd.salvaFascicolo(fVO);
					if (fVO == null) {
						errors.add("errore_nel_salvataggio", new ActionMessage(
								"errore_nel_salvataggio", "", ""));
						saveErrors(request, errors);
					} else if (fVO.getReturnValue() == ReturnValues.SAVED) {
						messages.add("fascicolo", new ActionMessage(
								"fascicolo_registrato", "Modificato", ""));
						saveMessages(request, messages);
					}

				} else {

					fVO = impostaFascicoloVO(fascicoloForm, utente);
					fVO = fd.nuovoFascicolo(fVO);
					if (fVO.getReturnValue() == ReturnValues.SAVED) {
						messages.add("fascicolo", new ActionMessage(
								"fascicolo_registrato", "Modificato", ""));
						saveMessages(request, messages);
					}

				}
			}
			request.setAttribute("fascicoloId", fVO.getId());
		} else if (request.getParameter("btnRimuovi") != null) {
			ProtocolloForm pForm = (ProtocolloForm) session
					.getAttribute("protocolloForm");
			if (pForm instanceof ProtocolloIngressoForm) {
				ProtocolloIngressoForm piForm = (ProtocolloIngressoForm) pForm;
				if (piForm.isDaBtnModifica())
					return (mapping.findForward("tornaProtocolloIngressoNew"));
				else
					return (mapping.findForward("tornaProtocolloIngresso"));
			} else if (pForm instanceof ProtocolloUscitaForm) {
				if (session.getAttribute("tornaFascicolaUscitaEdit") != null) {
					session.removeAttribute("tornaFascicolaUscitaEdit");
					return (mapping
							.findForward("tornaFascicolaProtocolloUscita"));
				} else
					return (mapping.findForward("tornaProtocolloUscita"));
			} else {
				PostaInternaForm postaForm = (PostaInternaForm) pForm;
				if (postaForm.getProtocolloId() == 0
						|| postaForm.isDaBtnModifica())
					return (mapping.findForward("tornaPostaInternaNew"));
				else
					return (mapping.findForward("tornaPostaInterna"));
			}
		} else if ((request.getParameter("btnAnnulla") != null)
				|| (request.getParameter("btnAnnullaNew") != null) || request.getParameter("btnIndietro")!=null) {
			if (Boolean.TRUE.equals(session.getAttribute("tornaProtocollo"))) {
				session.removeAttribute("tornaProtocollo");
				ProtocolloForm pForm = (ProtocolloForm) session
						.getAttribute("protocolloForm");
				if (pForm instanceof ProtocolloIngressoForm) {
					
					ProtocolloIngressoForm piForm = (ProtocolloIngressoForm) pForm;
					if (piForm.isDaBtnModifica())
						return (mapping.findForward("tornaProtocolloIngressoNew"));
					else
						return (mapping.findForward("tornaProtocolloIngresso"));
				} else if (pForm instanceof ProtocolloUscitaForm) {
					if (session.getAttribute("tornaFascicolaUscitaEdit") != null) {
						session.removeAttribute("tornaFascicolaUscitaEdit");
						return (mapping.findForward("tornaFascicolaProtocolloUscita"));
					} else
						return (mapping.findForward("tornaProtocolloUscita"));
				} else {
					PostaInternaForm postaForm = (PostaInternaForm) pForm;
					if (postaForm.getProtocolloId() == 0
							|| postaForm.isDaBtnModifica())
						return (mapping.findForward("tornaPostaInternaNew"));
					else
						return (mapping.findForward("tornaPostaInterna"));
				}
			} else if (Boolean.TRUE.equals(session.getAttribute("tornaEditor"))) {
				session.removeAttribute("tornaEditor");
				return (mapping.findForward("tornaInvioProtocolloEditor"));

			} else if (Boolean.TRUE.equals(session
					.getAttribute("tornaTemplate"))) {
				session.removeAttribute("tornaTemplate");
				return (mapping.findForward("tornaTemplate"));

			} else if (Boolean.TRUE.equals(session
					.getAttribute("tornaDocumento"))) {
				session.removeAttribute("tornaDocumento");
				return (mapping.findForward("tornaDocumento"));
			} else if (Boolean.TRUE
					.equals(session.getAttribute("tornaFaldone"))) {
				session.removeAttribute("tornaFaldone");
				return (mapping.findForward("tornaFaldone"));
			}

			else {
				caricaFascicolo(request, fascicoloForm);
				return (mapping.findForward("input"));
			}
		}
		else if (request.getParameter("btnCancella") != null) {
			caricaFascicolo(request, fascicoloForm);
			fascicoloForm.setOperazione(FascicoloForm.CANCELLAZIONE_FASCICOLO);
			return (mapping.findForward("confermaOperazione"));
		} else if (request.getParameter("btnStampa") != null) {
			stampaFrontespizio(response, fascicoloForm, utente
					.getAreaOrganizzativa().getDescription());
			return null;
		} else if (request.getParameter("btnChiudi") != null) {
			caricaFascicolo(request, fascicoloForm);
			if (isFascicoloClosable(fascicoloForm)) {
				fascicoloForm.setOperazione(FascicoloForm.CHIUSURA_FASCICOLO);
				return (mapping.findForward("confermaOperazione"));
			} else {
				errors.add("fascicolo_no_chiusura", new ActionMessage(
						"fascicolo_no_chiusura", "", ""));
			}

		} else if (request.getParameter("btnRiapri") != null) {
			caricaFascicolo(request, fascicoloForm);
			fascicoloForm.setOperazione(FascicoloForm.RIAPERTURA_FASCICOLO);
			return (mapping.findForward("confermaOperazione"));
		} else if (request.getParameter("btnScarta") != null) {
			caricaFascicolo(request, fascicoloForm);
			fascicoloForm.setOperazione(FascicoloForm.SCARTO_FASCICOLO);
			return (mapping.findForward("confermaOperazione"));
		} else if (request.getParameter("btnInvio") != null) {
			caricaFascicolo(request, fascicoloForm);
			errors = fascicoloForm.validate(mapping, request);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			} else {
				return (mapping.findForward("invioProtocollo"));
			}
		} else if (request.getParameter("btnAnnullaInvio") != null) {
			caricaFascicolo(request, fascicoloForm);
			if (!FascicoloDelegate.getInstance().esisteFascicoloInCodaInvio(
					fascicoloForm.getId())) {
				errors.add("fascicolo_no_annulla_invio", new ActionMessage(
						"fascicolo_no_annulla_invio", "", ""));
			} else {
				FascicoloDelegate fd = FascicoloDelegate.getInstance();
				if (fd.annullaInvioFascicolo(fascicoloForm.getId(), utente
						.getValueObject().getUsername(), fascicoloForm
						.getVersione()) == 1) {
					messages.add("invio_fascicolo", new ActionMessage(
							"operazione_ok", "", ""));
					saveMessages(request, messages);
					request.setAttribute("fascicoloId", new Integer(
							fascicoloForm.getId()));
				} else {
					errors.add("invio_fascicolo", new ActionMessage(
							"errore_nel_salvataggio"));
				}
				saveErrors(request, errors);
			}
		} else if (request.getParameter("btnOperazioni") != null) {
			if (fascicoloForm.getDataEvidenza() != null) {
				fascicoloForm.setDataEvidenza(null);
			}
			return setOperazione(fascicoloForm, mapping, request, response);

		} else if (request.getParameter("btnSelezionaDocumento") != null) {
			caricaFascicolo(request, fascicoloForm);
			errors = fascicoloForm.validate(mapping, request);
			if (errors.isEmpty()) {
				request.setAttribute("documentoId", new Integer(request
						.getParameter("documentoSelezionato")));
				return (mapping.findForward("visualizzaDocumento"));
			}

		} else if (request.getParameter("btnAggiungiDocumenti") != null) {
			caricaFascicolo(request, fascicoloForm);
			session.setAttribute("tornaFascicolo", Boolean.TRUE);
			return (mapping.findForward("documenti"));

		} else if (request.getParameter("btnRimuoviDocumento") != null) {
			// caricaFascicolo(request, fascicoloForm);
			errors = fascicoloForm.validate(mapping, request);
			if (errors.isEmpty()) {
				int documentoId = Integer.parseInt(request
						.getParameter("documentoSelezionato"));
				int fascicoloId = fascicoloForm.getId();
				FascicoloDelegate.getInstance().rimuoviDocumentoDaFascicolo(
						fascicoloId, documentoId, fascicoloForm.getVersione());
				request.setAttribute("fascicoloId", new Integer(fascicoloId));
			}

		}

		else if (request.getParameter("btnCercaDaFascicolo") != null) {
			fascicoloForm.setProtocolliFascicoloSearch(new HashMap<Integer,ReportProtocolloView>());
			if (fascicoloForm.getNumeroProtocolloDaFascicolo() == null
					|| fascicoloForm.getNumeroProtocolloDaFascicolo().trim()
							.equals("")
					|| !NumberUtil.isInteger(fascicoloForm
							.getNumeroProtocolloDaFascicolo())) {
				fascicoloForm.setSearch(false);
			} else {
				Iterator<Integer> itProt = FascicoloDelegate.getInstance()
						.getProtocolloFascicoloByIdAndNumeroProtocollo(
								fascicoloForm.getId(),
								Integer.valueOf(fascicoloForm
										.getNumeroProtocolloDaFascicolo()))
						.iterator();
				if (itProt != null) {
					while (itProt.hasNext()) {
						Integer protocolloId = (Integer) itProt.next();

						fascicoloForm.getProtocolliFascicoloSearch().put(
								protocolloId,
								ProtocolloDelegate.getInstance()
										.getProtocolloView(protocolloId));
					}

				} else {
					fascicoloForm.setProtocolliFascicoloSearch(new HashMap<Integer,ReportProtocolloView>());
				}
				fascicoloForm.setSearch(true);
			}
			return (mapping.findForward("input"));
		}

		else if (request.getParameter("btnStoria") != null) {
			request.setAttribute("fascicoloId", new Integer(fascicoloForm
					.getId()));
			caricaFascicolo(request, fascicoloForm);
			return (mapping.findForward("storiaFascicolo"));

		} else if (request.getAttribute("fascicoloId") != null) {
			caricaFascicolo(request, fascicoloForm);
			return (mapping.findForward("input"));
		} else if (request.getParameter("downloadDocprotocolloSelezionato") != null) {
			Integer protId = new Integer(request
					.getParameter("downloadDocprotocolloSelezionato"));
			Protocollo proto = ProtocolloDelegate.getInstance().getProtocolloById(protId.intValue());
			ProtocolloVO p=proto.getProtocollo();
			if(ProtocolloFileUtility.isDocumentoReadable(p.getId(), p.getFlagTipo(), p.getUfficioProtocollatoreId(),p.getUfficioMittenteId(),proto.getFascicoli(), utente)){
				request.setAttribute("downloadDocprotocolloSelezionato", protId);
				return (mapping.findForward("downloadDocumentoProtocollo"));
			}
			else{
				errors.add("documento",new ActionMessage("download_doc_non_abilitato","errors"));
				saveErrors(request, errors);
			}
			
		} else if (request.getParameter("btnAggiungiProtocolli") != null) {
			session.setAttribute("provenienza", "fascicoloProtocollo");
			session.setAttribute("tornaFascicolo", Boolean.TRUE);
			return (mapping.findForward("associaProtocolli"));
		} else if (request.getParameter("btnRimuoviProtocolli") != null) {
			String[] idf = fascicoloForm.getProtocolliSelezionati();
			FascicoloVO fVO = impostaFascicoloVO(fascicoloForm, utente);

			if (FascicoloDelegate.getInstance().eliminaProtocolliFascicolo(fVO,
					idf, utente.getValueObject().getUsername()) != ReturnValues.SAVED) {
				errors.add("errore_nel_salvataggio", new ActionMessage(
						"errore_nel_salvataggio", "", ""));
				saveErrors(request, errors);

			}
			request.setAttribute("fascicoloId", new Integer(fascicoloForm
					.getId()));

		} else if (request.getParameter("btnSelezionaProtocolli") != null) {
			String[] idf = fascicoloForm.getProtocolliSelezionati();
			FascicoloVO fVO = impostaFascicoloVO(fascicoloForm, utente);

			if (FascicoloDelegate.getInstance().salvaProtocolliFascicolo(fVO,
					idf, utente.getValueObject().getUsername(), utente.getUfficioInUso()) != ReturnValues.SAVED) {
				errors.add("errore_nel_salvataggio", new ActionMessage(
						"errore_nel_salvataggio", "", ""));
				saveErrors(request, errors);

			}
			request.setAttribute("fascicoloId", new Integer(fascicoloForm
					.getId()));

		} else if (request.getParameter("btnAggiungiProcedimenti") != null) {

			session.setAttribute("provenienza", "fascicoloProcedimenti");
			session.setAttribute("tornaFascicolo", Boolean.TRUE);
			session.removeAttribute("risultatiProcedimentiDaProtocollo");

			return (mapping.findForward("associaProcedimenti"));

		} else if (request.getParameter("btnSelezionaProcedimenti") != null) {
			String[] idp = fascicoloForm.getProcedimentiSelezionati();
			FascicoloVO fVO = impostaFascicoloVO(fascicoloForm, utente);

			if (FascicoloDelegate.getInstance().salvaProcedimentiFascicolo(fVO,
					idp, utente.getValueObject().getUsername()) != ReturnValues.SAVED) {
				errors.add("errore_nel_salvataggio", new ActionMessage(
						"errore_nel_salvataggio", "", ""));
				saveErrors(request, errors);

			}
			request.setAttribute("fascicoloId", new Integer(fascicoloForm
					.getId()));
		} else if (request.getParameter("btnRimuoviProcedimenti") != null) {
			String[] idp = fascicoloForm.getProcedimentiSelezionati();
			FascicoloVO fVO = impostaFascicoloVO(fascicoloForm, utente);

			if (FascicoloDelegate.getInstance().eliminaProcedimentiFascicolo(
					fVO, idp, utente.getValueObject().getUsername()) != ReturnValues.SAVED) {
				errors.add("errore_nel_salvataggio", new ActionMessage(
						"errore_nel_salvataggio", "", ""));
				saveErrors(request, errors);

			}
			request.setAttribute("fascicoloId", new Integer(fascicoloForm
					.getId()));
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		caricaFascicolo(request, fascicoloForm);
		Collection<TipoDocumentoVO> tipiDocumento = LookupDelegate.getInstance()
				.getTipiDocumento(aooId);
		fascicoloForm.setTipiDocumento(tipiDocumento);
		return (mapping.findForward("input"));
	}

	private void removeCollegamenti(FascicoloForm form) {
		FascicoloDelegate delegate = FascicoloDelegate.getInstance();
		String[] collegamenti = form.getCollegamentiSelezionatiId();
		if (collegamenti != null) {
			for (int i = 0; i < collegamenti.length; i++) {
				if (collegamenti[i] != null) {
					delegate.cancellaCollegamento(form.getId(), Integer
							.parseInt(collegamenti[i]));
					collegamenti[i] = null;
				}
			}
		}
	}

	private void impostaParentForm(FascicoloForm fascicoloForm, Utente utente,
			Integer padreId) {
		if (padreId != 0) {
			fascicoloForm.setPadreId(padreId);
			FascicoloDelegate fd = FascicoloDelegate.getInstance();
			Fascicolo fasc = fd.getFascicoloById(padreId);
			fascicoloForm.setPadre(fasc.getFascicoloVO().getOggetto());
			impostaTitolario(fascicoloForm, utente, fasc.getFascicoloVO()
					.getTitolarioId());
		} else {
			fascicoloForm.setPadreId(padreId);
			fascicoloForm.setOggettoFascicoloPadre(null);
			fascicoloForm.setPadre(null);
			impostaTitolario(fascicoloForm, utente, 0);
		}

	}

	private void impostaFascicoloForm(FascicoloVO fascicoloVO,
			FascicoloForm fascicoloForm, Utente utente) {
		fascicoloForm.setId(fascicoloVO.getId());
		fascicoloForm.setAooId(fascicoloVO.getAooId());
		fascicoloForm.setCodice(fascicoloVO.getCodice());

		// argomento titolario
		impostaTitolario(fascicoloForm, utente, fascicoloVO.getTitolarioId());
		aggiornaParentForm(fascicoloForm, fascicoloVO.getParentId());
		if (fascicoloVO.getGiorniAlert() == 0)
			fascicoloForm.setGiorniAlert(null);
		else
			fascicoloForm.setGiorniAlert(String.valueOf(fascicoloVO
					.getGiorniAlert()));
		if (fascicoloVO.getGiorniMax() == 0)
			fascicoloForm.setGiorniMax(null);
		else
			fascicoloForm.setGiorniMax(String.valueOf(fascicoloVO
					.getGiorniMax()));
		fascicoloForm.setRiferimentiLegislativi(fascicoloVO
				.getRiferimentiLegislativi());
		fascicoloForm.setDataApertura(DateUtil.formattaData(fascicoloVO
				.getDataApertura().getTime()));
		if (fascicoloVO.getDataChiusura() != null) {
			fascicoloForm.setDataChiusura(DateUtil.formattaData(fascicoloVO
					.getDataChiusura().getTime()));
		}

		fascicoloForm.setInteressato(fascicoloVO.getInteressato());
		fascicoloForm.setIndiInteressato(fascicoloVO.getIndiInteressato());
		fascicoloForm.setDelegato(fascicoloVO.getDelegato());
		fascicoloForm.setIndiDelegato(fascicoloVO.getIndiDelegato());
		
		fascicoloForm.setDescrizione(fascicoloVO.getDescrizione());
		fascicoloForm.setNome(fascicoloVO.getNome());
		fascicoloForm.setNote(fascicoloVO.getNote());
		fascicoloForm.setOggettoFascicolo(fascicoloVO.getOggetto());
		fascicoloForm.setProcessoId(fascicoloVO.getProcessoId());
		fascicoloForm.setProgressivo(fascicoloVO.getProgressivo());
		fascicoloForm.setPathProgressivo(fascicoloVO.getPathProgressivo());
		fascicoloForm.setRegistroId(fascicoloVO.getRegistroId());
		fascicoloForm.setUfficioResponsabileId(fascicoloVO
				.getUfficioResponsabileId());
		fascicoloForm.setCaricaResponsabileId(fascicoloVO
				.getCaricaResponsabileId());
		fascicoloForm.setStatoFascicolo(fascicoloVO.getStato());
		fascicoloForm.setUfficioCorrenteId(fascicoloVO
				.getUfficioIntestatarioId());
		if (fascicoloVO.getCaricaIstruttoreId() > 0) {
			Organizzazione org = Organizzazione.getInstance();
			CaricaVO carica = org
					.getCarica(fascicoloVO.getCaricaIstruttoreId());

			Utente ute = org.getUtente(carica.getUtenteId());
			if (ute != null) {
				ute.getValueObject().setCarica(carica.getNome());
				fascicoloForm.setIstruttore(newIstruttore(ute, carica));
			}
			fascicoloForm
					.setUtenteIstruttoreSelezionatoId(carica.getUtenteId());

		} else
			fascicoloForm.setUtenteIstruttoreSelezionatoId(0);
		fascicoloForm.setVersione(fascicoloVO.getVersione());
		
		if (fascicoloVO.getUfficioIntestatarioId() > 0) {
			Organizzazione org = Organizzazione.getInstance();
			Ufficio uff = org
					.getUfficio(fascicoloVO.getUfficioIntestatarioId());
			CaricaVO carica = org.getCarica(fascicoloVO
					.getCaricaIntestatarioId());
			if (carica != null) {
				if (carica.getUtenteId() != 0) {
					fascicoloForm.setUtenteSelezionatoId(carica.getUtenteId());
					Utente ute = org.getUtente(carica.getUtenteId());
					fascicoloForm.setMittente(newMittente(uff, carica, ute, carica
							.isAttivo()));
				} else {
					fascicoloForm.setUtenteSelezionatoId(0);
					fascicoloForm.setMittente(newMittente(uff, carica, null, carica.isAttivo()));
				}
			} else {
				fascicoloForm.setUtenteSelezionatoId(0);
				fascicoloForm.setMittente(newMittente(uff, carica, null, true));
			}
		}
		fascicoloForm.setAnnoRiferimento(fascicoloVO.getAnnoRiferimento());
		fascicoloForm.setTipoFascicolo(fascicoloVO.getTipoFascicolo());
		if (fascicoloVO.getDataEvidenza() != null) {
			fascicoloForm.setDataEvidenza(DateUtil.formattaData(fascicoloVO
					.getDataEvidenza().getTime()));
		} else {
			fascicoloForm.setDataEvidenza(null);
		}
		if (fascicoloVO.getDataUltimoMovimento() != null) {
			fascicoloForm.setDataUltimoMovimento(DateUtil
					.formattaData(fascicoloVO.getDataUltimoMovimento()
							.getTime()));
		} else {
			fascicoloForm.setDataUltimoMovimento(null);
		}
		if (fascicoloVO.getDataCarico() != null) {
			fascicoloForm.setDataCarico(DateUtil.formattaData(fascicoloVO
					.getDataCarico().getTime()));
		} else {
			fascicoloForm.setDataCarico(null);
		}
		if (fascicoloVO.getDataScarto() != null) {
			fascicoloForm.setDataScarto(DateUtil.formattaData(fascicoloVO
					.getDataScarto().getTime()));
		} else {
			fascicoloForm.setDataScarto(null);
		}

		if (fascicoloVO.getDataScarico() != null) {
			fascicoloForm.setDataScarico(DateUtil.formattaData(fascicoloVO
					.getDataScarico().getTime()));
		} else {
			fascicoloForm.setDataScarico(DateUtil.formattaData(System
					.currentTimeMillis()));
		}

		fascicoloForm.setPosizioneSelezionataId(fascicoloVO
				.getPosizioneFascicolo());
		fascicoloForm.setPosizioneSelezionata(""
				+ fascicoloVO.getPosizioneFascicolo());

		fascicoloForm.setVersione(fascicoloVO.getVersione());
		fascicoloForm
				.setCollocazioneLabel1(fascicoloVO.getCollocazioneLabel1());
		fascicoloForm
				.setCollocazioneLabel2(fascicoloVO.getCollocazioneLabel2());
		fascicoloForm
				.setCollocazioneLabel3(fascicoloVO.getCollocazioneLabel3());
		fascicoloForm
				.setCollocazioneLabel4(fascicoloVO.getCollocazioneLabel4());
		fascicoloForm.setCollocazioneValore1(fascicoloVO
				.getCollocazioneValore1());
		fascicoloForm.setCollocazioneValore2(fascicoloVO
				.getCollocazioneValore2());
		fascicoloForm.setCollocazioneValore3(fascicoloVO
				.getCollocazioneValore3());
		fascicoloForm.setCollocazioneValore4(fascicoloVO
				.getCollocazioneValore4());
		fascicoloForm.setComune(fascicoloVO.getComune());
		fascicoloForm.setCapitolo(fascicoloVO.getCapitolo());
		if(utente.getCaricaInUso()==fascicoloVO.getCaricaIntestatarioId() || MenuDelegate.getInstance().isChargeEnabledByUniqueName(utente, "modify_folders") )
			fascicoloForm.setReferente(true);
	}

	private void impostaNuovoFascicoloForm(int titolarioId,
			FascicoloForm fascicolo, Utente utente, HttpSession session) {
		fascicolo.inizializzaForm();
		impostaTitolario(fascicolo, utente, titolarioId);
		fascicolo.setId(0);
		fascicolo.setUfficioResponsabileId(utente.getUfficioInUso());
		fascicolo.setCaricaResponsabileId(utente.getCaricaInUso());
		fascicolo.setDataApertura(DateUtil.formattaData((new Date(System
				.currentTimeMillis())).getTime()));
		fascicolo.setAnnoRiferimento(DateUtil.getYear(new Date(System
				.currentTimeMillis())));
		fascicolo.setUfficioCorrenteId(utente.getUfficioInUso());
		fascicolo.setAooId(utente.getAreaOrganizzativa().getId().intValue());
		Collection<TipoDocumentoVO> tipiDoc = LookupDelegate.getInstance().getTipiDocumento(
				fascicolo.getAooId());
		fascicolo.setTipiDocumento(tipiDoc);
		fascicolo.setVersione(0);
		if (Boolean.TRUE.equals(session.getAttribute("tornaProtocollo"))) {
			ProtocolloForm pForm = (ProtocolloForm) session
					.getAttribute("protocolloForm");
			if (pForm.getProtocolloId() != 0) {
				Date dataCreazione = DateUtil.getDataOra(pForm
						.getDataRegistrazione());
				fascicolo.setDataApertura(DateUtil.formattaData(dataCreazione
						.getTime()));
			} else
				fascicolo.setDataApertura(DateUtil.formattaData(System
						.currentTimeMillis()));
		}

	}

	private boolean isFascicoloClosable(FascicoloForm fascicolo) {
		boolean closable = true;
		Iterator<FileVO> it = fascicolo.getDocumentiFascicolo().iterator();
		while (it.hasNext()) {
			FileVO d = it.next();
			if (Parametri.STATO_LAVORAZIONE.equals(d.getStatoArchivio())) {
				closable = false;
				break;
			}
		}
		Iterator<ProcedimentoVO> itProc = fascicolo.getProcedimentiFascicolo().iterator();
		while (itProc.hasNext()) {
			ProcedimentoVO p =  itProc.next();
			if (p.getStatoId() == 0) {
				closable = false;
				break;
			}
		}
		return closable;
	}

	private void aggiornaFascicoloForm(Fascicolo fascicolo,
			FascicoloForm fForm, Utente utente) {
		impostaFascicoloForm(fascicolo.getFascicoloVO(), fForm, utente);
		aggiornaStatoFascicoloForm(fascicolo.getFascicoloVO(), fForm, utente);
		aggiornaVisibilitaFascicoloForm(fascicolo, fForm, utente);
		Collection<FileVO> documentiFascicolo = new ArrayList<FileVO>();
		Iterator<DocumentoFascicoloVO> itDF = fascicolo.getDocumenti().iterator();
		DocumentaleDelegate dd = DocumentaleDelegate.getInstance();
		FascicoloDelegate ff = FascicoloDelegate.getInstance();
		while (itDF.hasNext()) {
			DocumentoFascicoloVO dfVO = (DocumentoFascicoloVO) itDF.next();
			FileVO vo=dd.getFileVOById(dfVO.getDocumentoId());
			vo.setUfficioProprietarioId(dfVO.getUfficioProprietarioId());
			if(fForm.getVisibilita()==TipoVisibilitaUfficioEnum.COMPLETA || 
					ff.isUfficioAbilitatoSuFascicoloDocumento(utente.getUfficioInUso(), dfVO.getFascicoloId(), dfVO.getDocumentoId()))
				vo.setVisibileDaFascicolo(true);
			documentiFascicolo.add(vo);
		}
		fForm.setDocumentiFascicolo(documentiFascicolo);

		Iterator<ProtocolloFascicoloVO> it = fascicolo.getProtocolli().iterator();
		SortedMap<String,ReportProtocolloView> protocolliView = new TreeMap<String,ReportProtocolloView>(new Comparator<String>() {
			public int compare(String s1, String s2) {
				Long i1 = Long.decode(s1.substring(1));
				Long i2 = Long.decode(s2.substring(1));
				if (Integer.valueOf(s1.charAt(0)) == Integer.valueOf(s2.charAt(0)))
					return (i1 > i2) ? -1 : (i1 == i2 ? 0 : 1);
				else
					return Integer.valueOf(s1.charAt(0)) - Integer.valueOf(s2.charAt(0));
			}
		});
		while (it.hasNext()) {
			ProtocolloFascicoloVO pfVO = (ProtocolloFascicoloVO) it.next();
			ReportProtocolloView rp = ProtocolloDelegate.getInstance().getProtocolloView(pfVO.getProtocolloId());
			rp.setUfficioProprietarioId(pfVO.getUfficioProprietarioId());
			if(fForm.getVisibilita()==TipoVisibilitaUfficioEnum.COMPLETA ||
					ff.isUfficioAbilitatoSuFascicoloProtocollo(utente.getUfficioInUso(), pfVO.getFascicoloId(), pfVO.getProtocolloId()))
				rp.setVisibileDaFascicolo(true);
			protocolliView.put(String.valueOf(rp.getRegistroAnnoNumero()), rp);
		}
		fForm.setProtocolliFascicolo(protocolliView);

		//fascicoli collegati
		Collection<Integer> fascicoliCollegatiId = FascicoloDelegate
				.getInstance().getCollegatiIdByFascicoloId(
						fascicolo.getFascicoloVO().getId());
		fForm.initFascicoliCollegati();
		for (Integer fascId : fascicoliCollegatiId) {
			FascicoloView fc = FascicoloDelegate.getInstance().getFascicoloViewById(fascId);
			fForm.collegaFascicolo(fc);
		}

		//sotto fascicoli
		Collection<Integer> sottoFascicoliId = FascicoloDelegate.getInstance().getSottoFascicoliIdByFascicoloId(
						fascicolo.getFascicoloVO().getId());
		fForm.initSottoFascicoli();
		for (Integer fascId : sottoFascicoliId) {
			FascicoloView fc = FascicoloDelegate.getInstance()
					.getFascicoloViewById(fascId);
			fForm.addSottoFascicolo(fc);
		}

		fForm.setSearch(false);
		fForm.setProtocolliFascicoloSearch(new HashMap<Integer,ReportProtocolloView>());

		fForm.setProcedimentiFascicolo(fascicolo.getProcedimenti());
		fForm.setProcedimenti(fascicolo.getProcedimenti());
		FascicoloVO fVO = fascicolo.getFascicoloVO();
		fForm.setCodice(fVO.getAnnoRiferimento()
				+ StringUtil.formattaNumeroProtocollo(String.valueOf(fVO
						.getProgressivo()), 6));
		fForm.aggiornaSezioni();
	}

	private FascicoloVO impostaFascicoloVO(FascicoloForm fForm, Utente utente) {
		FascicoloVO fVO = new FascicoloVO();
		CaricaDelegate caricaDelegate=CaricaDelegate.getInstance();
		fVO.setAooId(utente.getUfficioVOInUso().getAooId());
		fVO.setRegistroId(utente.getRegistroInUso());
		fVO.setCodice(fForm.getCodice());
		Date dataApertura = DateUtil.toDate(fForm.getDataApertura());
		if (dataApertura != null) {
			fVO.setDataApertura(new java.sql.Date(dataApertura.getTime()));
		} else {
			fVO.setDataApertura(null);
		}
		Date dataChiusura = DateUtil.toDate(fForm.getDataChiusura());
		if (dataChiusura != null) {
			fVO.setDataChiusura(new java.sql.Date(dataChiusura.getTime()));
		} else {
			fVO.setDataChiusura(null);
		}

		fVO.setDescrizione(fForm.getDescrizione());
		fVO.setId(fForm.getId());
		fVO.setNome(fForm.getNome());
		fVO.setNote(fForm.getNote());
		fVO.setOggetto(fForm.getOggettoFascicolo());
		if (fForm.getId() > 0)
			fVO.setStato(fForm.getStatoFascicolo());
		else
			fVO.setStato(0);
		fVO.setParentId(fForm.getPadreId());
		// titolario
		if (fForm.getTitolario() != null) {
			fVO.setTitolarioId(fForm.getTitolario().getId().intValue());
		}
		//referente
		fVO.setUtenteIntestatarioId(fForm.getUtenteSelezionatoId());
		if(fForm.getUtenteSelezionatoId()!=0){
			CaricaVO car=caricaDelegate.getCaricaByUtenteAndUfficio(fForm.getUtenteSelezionatoId(), fForm.getUfficioCorrenteId());
			fVO.setCaricaIntestatarioId(car.getCaricaId());
		}
		//istruttore
		fVO.setUtenteIstruttoreId(fForm.getUtenteIstruttoreSelezionatoId());
		if(fForm.getUtenteIstruttoreSelezionatoId()!=0){
			CaricaVO car=caricaDelegate.getCaricaByUtenteAndUfficio(fForm.getUtenteIstruttoreSelezionatoId(), fForm.getUfficioCorrenteId());
			fVO.setCaricaIstruttoreId(car.getCaricaId());
		}
		fVO.setInteressato(fForm.getInteressato());
		fVO.setIndiInteressato(fForm.getIndiInteressato());
		fVO.setDelegato(fForm.getDelegato());
		fVO.setIndiDelegato(fForm.getIndiDelegato());
		if (fForm.getGiorniAlert() == null
				|| "".equals(fForm.getGiorniAlert().trim()))
			fVO.setGiorniAlert(0);
		else
			fVO.setGiorniAlert(Integer.valueOf(fForm.getGiorniAlert())
					.intValue());
		if (fForm.getGiorniMax() == null
				|| "".equals(fForm.getGiorniMax().trim()))
			fVO.setGiorniMax(0);
		else
			fVO.setGiorniMax(Integer.valueOf(fForm.getGiorniMax()).intValue());
		fVO.setRiferimentiLegislativi(fForm.getRiferimentiLegislativi());
		// trattato da
		fVO.setUfficioResponsabileId(utente.getUfficioInUso());
		fVO.setCaricaResponsabileId(utente.getCaricaInUso());
		// ufficio
		fVO.setUfficioIntestatarioId(fForm.getUfficioCorrenteId());
		
		fVO.setVersione(fForm.getVersione());

		if (fVO.getId().intValue() == 0) {
			fVO.setRowCreatedUser(utente.getValueObject().getUsername());
			fVO.setRowCreatedTime(new Date(System.currentTimeMillis()));
			fVO.setDataCarico(new Date(System.currentTimeMillis()));
		} else {
			fVO.setRowUpdatedUser(utente.getValueObject().getUsername());
			Date dataCarico = DateUtil.toDate(fForm.getDataCarico());
			if (dataCarico != null) {
				fVO.setDataCarico(new java.sql.Date(dataCarico.getTime()));
			} else {
				fVO.setDataCarico(null);
			}
		}
		fVO.setRowUpdatedTime(new Date(System.currentTimeMillis()));
		fVO.setAnnoRiferimento(fForm.getAnnoRiferimento());
		fVO.setTipoFascicolo(fForm.getTipoFascicolo());
		
		Date dataUltimoMovimento = DateUtil.toDate(fForm
				.getDataUltimoMovimento());
		if (dataUltimoMovimento != null) {
			fVO.setDataUltimoMovimento(new java.sql.Date(dataUltimoMovimento
					.getTime()));
		} else {
			fVO.setDataUltimoMovimento(null);
		}

		Date dataScarto = DateUtil.toDate(fForm.getDataScarto());
		if (dataScarto != null) {
			fVO.setDataScarto(new java.sql.Date(dataScarto.getTime()));
		} else {
			fVO.setDataScarto(null);
		}

		Date dataScarico = DateUtil.toDate(fForm.getDataScarico());
		if (dataScarico != null) {
			fVO.setDataScarico(new java.sql.Date(dataScarico.getTime()));
		} else {
			fVO.setDataScarico(null);
		}

		fVO.setCollocazioneLabel1(fForm.getCollocazioneLabel1());
		fVO.setCollocazioneLabel2(fForm.getCollocazioneLabel2());
		fVO.setCollocazioneLabel3(fForm.getCollocazioneLabel3());
		fVO.setCollocazioneLabel4(fForm.getCollocazioneLabel4());
		fVO.setCollocazioneValore1(fForm.getCollocazioneValore1());
		fVO.setCollocazioneValore2(fForm.getCollocazioneValore2());
		fVO.setCollocazioneValore3(fForm.getCollocazioneValore3());
		fVO.setCollocazioneValore4(fForm.getCollocazioneValore4());
		fVO.setComune(fForm.getComune());
		fVO.setCapitolo(fForm.getCapitolo());
		return fVO;
	}

	private void aggiornaParentForm(FascicoloForm form, int fascicoloId) {
		FascicoloDelegate fd = FascicoloDelegate.getInstance();
		Fascicolo parent = fd.getFascicoloById(fascicoloId);
		form.setPadre(parent.getFascicoloVO().getOggetto());
		form.setPadreId(fascicoloId);
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

	private void impostaTitolario(ProtocolloForm form, Utente utente,
			int titolarioId) {
		int ufficioId = form.getUfficioCorrenteId();
		TitolarioBO.impostaTitolario(form, ufficioId, titolarioId);

	}

	private void impostaTitolario(DocumentoForm form, Utente utente,
			int titolarioId) {
		int ufficioId = form.getUfficioCorrenteId();
		TitolarioBO.impostaTitolario(form, ufficioId, titolarioId);
	}

	protected void caricaFascicolo(HttpServletRequest request,
			FascicoloForm form) {
		Integer fascicoloId = (Integer) request.getAttribute("fascicoloId");
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		Fascicolo fascicolo;
		Integer versioneId = (Integer) request.getAttribute("versioneId");
		FascicoloDelegate fd = FascicoloDelegate.getInstance();
		if (fascicoloId != null) {
			int id = fascicoloId.intValue();
			if (versioneId == null) {
				fascicolo = fd.getFascicoloById(id);
				form.setVersioneDefault(true);
			} else {
				int versione = versioneId.intValue();
				fascicolo = fd.getFascicoloByIdVersione(id, versione);
				form.setVersioneDefault(false);
			}
			session.setAttribute(Constants.FASCICOLO, fascicolo);
		} else {
			fascicolo = (Fascicolo) session.getAttribute(Constants.FASCICOLO);
		}
		
		fascicolo.setVisibilita(fd.getVisibilitaUfficioFascicolo(utente.getUfficioInUso(), fascicolo.getFascicoloVO().getId()));
		aggiornaFascicoloForm(fascicolo, form, utente);
	}

	private void aggiornaStatoFascicoloForm(FascicoloVO fascicolo,
			FascicoloForm form, Utente utente) {
		boolean modificabile = false;		
		if (fascicolo.getStato() != Parametri.STATO_FASCICOLO_SCARTATO && fascicolo.getUfficioIntestatarioId()==utente.getUfficioInUso()) {
			modificabile =true;
		}
		form.setModificabile(modificabile);
	}
	
	private void aggiornaVisibilitaFascicoloForm(Fascicolo fascicolo,
			FascicoloForm form, Utente utente) {
		if(fascicolo.getFascicoloVO().getUfficioIntestatarioId()==utente.getUfficioInUso())
			form.setVisibilita(TipoVisibilitaUfficioEnum.COMPLETA);
		else 
			form.setVisibilita(fascicolo.getVisibilita());

	}

	private AssegnatarioView newMittente(Ufficio ufficio, CaricaVO carica, Utente utente,
			boolean attivo) {
		UfficioVO uffVO = ufficio.getValueObject();
		AssegnatarioView mittente = new AssegnatarioView();
		mittente.setUfficioId(uffVO.getId().intValue());
		mittente.setDescrizioneUfficio(ufficio.getPath());
		mittente.setNomeUfficio(uffVO.getDescription());
		if (utente != null) {
			UtenteVO uteVO = utente.getValueObject();
			mittente.setUtenteId(uteVO.getId().intValue());
			uteVO.setCarica(carica.getNome());
			if (attivo)
				mittente.setNomeUtente(uteVO.getCaricaFullName());
			else
				mittente.setNomeUtente(uteVO.getCaricaFullNameNonAttivo());
		}
		return mittente;
	}

	private AssegnatarioView newIstruttore(Utente utente, CaricaVO carica) {
		AssegnatarioView istruttore = new AssegnatarioView();
		UtenteVO uteVO = utente.getValueObject();
		istruttore.setUtenteId(uteVO.getId().intValue());
		uteVO.setCarica(carica.getNome());
		if (carica.isAttivo())
			istruttore.setNomeUtente(uteVO.getCaricaFullName());
		else
			istruttore.setNomeUtente(uteVO.getCaricaFullNameNonAttivo());
		return istruttore;
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

	private ActionForward setOperazione(FascicoloForm fascicoloForm,
			ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		Fascicolo fascicolo = (Fascicolo) session
				.getAttribute(Constants.FASCICOLO);
		fascicolo.getFascicoloVO().setUfficioResponsabileId(
				utente.getUfficioInUso());
		fascicolo.getFascicoloVO().setCaricaResponsabileId(
				utente.getCaricaInUso());

		FascicoloDelegate fd = FascicoloDelegate.getInstance();
		int fascicoloId = fascicoloForm.getId();
		int returnValue = ReturnValues.UNKNOWN;
		fascicolo.getFascicoloVO().setDataEvidenza(null);
		if (fascicoloForm.getOperazione().equals(
				FascicoloForm.CHIUSURA_FASCICOLO)) {
			// chiusura fascicolo
			fascicolo.getFascicoloVO().setDataChiusura(
					new Date(System.currentTimeMillis()));
			returnValue = fd.chiudiFascicolo(fascicolo, utente);
		} else if (fascicoloForm.getOperazione().equals(
				FascicoloForm.CANCELLAZIONE_FASCICOLO)) {
			returnValue = fd.cancellaFascicolo(fascicoloId);
			if (returnValue == ReturnValues.UNKNOWN) {
				errors
						.add(
								"fascicolo",
								new ActionMessage(
										"record_non_cancellabile",
										"si � verificato un errore in fase di salvataggio dei dati",
										""));
				saveErrors(request, errors);
			} else if (returnValue == ReturnValues.NOT_SAVED) {
				errors
						.add(
								"fascicolo",
								new ActionMessage(
										"record_non_cancellabile",
										"il fascicolo",
										"Verificare se esistono collegamenti con protocolli, documenti, faldoni, procedimenti o se il fascicolo ha dei sotto fascicoli"));
				saveErrors(request, errors);
			} else if (returnValue == ReturnValues.SAVED) {
				messages.add("fascicolo", new ActionMessage("cancellazione_ok",
						"", ""));
				saveMessages(request, messages);
				return (mapping.findForward("lista"));

			}
		} else if (fascicoloForm.getOperazione().equals(
				FascicoloForm.RIAPERTURA_FASCICOLO)) {
			fascicolo.getFascicoloVO().setDataChiusura(null);
			fascicolo.getFascicoloVO().setDataCarico(
					new Date(System.currentTimeMillis()));

			returnValue = fd.riapriFascicolo(fascicolo, utente);
		} else if (fascicoloForm.getOperazione().equals(
				FascicoloForm.SCARTO_FASCICOLO)) {
			int massimario = (fascicoloForm.getTitolario() != null ? fascicoloForm
					.getTitolario().getMassimario()
					: 0);
			if (massimario > 0) {
				// controllo il massimario di scarto
				Date dataCreazione = DateUtil.toDate(fascicoloForm
						.getDataApertura());

				Date dataCorrente = new Date(System.currentTimeMillis());
				if (!dataCorrente.after(DateUtil.getDataFutura(dataCreazione
						.getTime(), massimario))) {
					errors.add("fascicolo_no_chiusura", new ActionMessage(
							"fascicollo.scarto_non_eseguibile",
							"" + massimario, ""));
					saveErrors(request, errors);

					return (mapping.findForward("confermaOperazione"));
				}

			}
			// scarto fascicolo
			returnValue = fd.scartaFascicolo(fascicoloForm.getId(),
					fascicoloForm.getPropostaScarto(), utente.getValueObject()
							.getUsername(), fascicoloForm.getVersione());
		}

		request.setAttribute("fascicoloId", new Integer(fascicoloId));
		caricaFascicolo(request, fascicoloForm);
		if (returnValue == ReturnValues.INVALID) {
			errors.add("errore_nel_salvataggio", new ActionMessage(
					"errore_nel_salvataggio", "", ""));
			saveErrors(request, errors);
			return (mapping.findForward("confermaOperazione"));
		}
		if (Boolean.TRUE.equals(session.getAttribute("tornaAlert"))) {
			session.removeAttribute("tornaAlert");
			return (mapping.findForward("tornaAlert"));
		}
		return (mapping.findForward("input"));

	}
}