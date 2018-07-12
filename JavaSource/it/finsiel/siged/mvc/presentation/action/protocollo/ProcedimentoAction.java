package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.presentation.action.protocollo.helper.file.ProcedimentoFileUtility;
import it.compit.fenice.mvc.presentation.helper.ProtocolloProcedimentoView;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.compit.fenice.mvc.vo.protocollo.UfficioPartecipanteVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.Procedimento;
import it.finsiel.siged.mvc.bo.AlberoUfficiBO;
import it.finsiel.siged.mvc.business.AmministrazioneDelegate;
import it.finsiel.siged.mvc.business.FascicoloDelegate;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.business.ProcedimentoDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.business.UfficioDelegate;
import it.finsiel.siged.mvc.business.UtenteDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProcedimentoForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloIngressoForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloUscitaForm;
import it.finsiel.siged.mvc.presentation.helper.AllaccioView;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.presentation.helper.FascicoloView;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.lookup.TipoProcedimentoVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.report.protocollo.CommonReportDS;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;
import it.finsiel.siged.util.StringUtil;
import it.flosslab.mvc.presentation.action.protocollo.helper.file.ProtocolloFileUtility;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ProcedimentoAction extends Action {

	static Logger logger = Logger.getLogger(ProcedimentoAction.class.getName());

	public void stampaFrontespizio(HttpServletResponse response,
			ProcedimentoForm form, String aooName) throws IOException,
			ServletException {
		ServletContext context = this.getServlet().getServletContext();
		OutputStream os = response.getOutputStream();
		try {

			JasperDesign jasperDesign = JRXmlLoader.load(context
					.getRealPath("/")
					+ FileConstants.STAMPA_PROCEDIMENTO_TEMPLATE);
			JasperReport jasperReport = JasperCompileManager
					.compileReport(jasperDesign);
			Map<String, Object> parameters = new HashMap<String, Object>();
			Organizzazione org = Organizzazione.getInstance();
			AssegnatarioView istrView = (AssegnatarioView) form.getIstruttori()
					.iterator().next();
			Utente istr = org.getUtente(istrView.getUtenteId());
			Utente funz = org.getUtente(form.getReferenteId());
			Utente diri = org.getUtente(org.getCarica(
					form.getCaricaResponsabileId()).getUtenteId());
			String aoo = org.getAreaOrganizzativa(form.getAooId())
					.getValueObject().getDescription();
			parameters.put("aoo_name",
					aoo + " - " + form.getUfficioCorrentePath());
			parameters.put("progressivo",
					form.getNumero() + "/" + form.getAnno());
			if(form.getOggettoProcedimento().length()<100)
				parameters.put("oggetto", form.getOggettoProcedimento());
			else
				parameters.put("oggetto", form.getOggettoProcedimento().substring(0, 100)+"...");
			parameters.put("categoria", form.getTipoProcedimento()
					.getTitolario());
			if (diri != null)
				parameters.put("trattato_da", diri.getValueObject()
						.getCaricaFullName());
			if (funz != null)
				parameters.put("referente", funz.getValueObject()
						.getCaricaFullName());
			if (istr != null)
				parameters.put("istruttore", istr.getValueObject()
						.getCaricaFullName());

			parameters.put("delegato", form.getDelegato());
			parameters.put("interessato", form.getAutoritaEmanante());
			parameters.put("data_creazione", form.getDataAvvio());
			ArrayList<Integer> l = new ArrayList<Integer>();
			l.add(new Integer(1));
			CommonReportDS ds = new CommonReportDS(l);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			response.setHeader("Content-Disposition",
					"attachment;filename=Frontespizio_" + form.getDataAvvio()
							+ ".pdf");
			response.setHeader("Cache-control", "");
			response.setContentType("application/pdf");
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
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

	public void stampaComunicazioneAvvio(HttpServletResponse response,
			ProcedimentoForm form, String aooName) throws IOException,
			ServletException {
		ServletContext context = this.getServlet().getServletContext();
		OutputStream os = response.getOutputStream();
		try {

			JasperDesign jasperDesign = JRXmlLoader.load(context
					.getRealPath("/")
					+ FileConstants.STAMPA_COMUNICAZIONE_AVVIO);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			Map<String, Object> parameters = new HashMap<String, Object>();
			Organizzazione org = Organizzazione.getInstance();
			
			Utente istr=null;
			if(form.getIstruttori()!=null && form.getIstruttori().size()!=0){
				AssegnatarioView istrView = (AssegnatarioView) form.getIstruttori().iterator().next();
				istr = org.getUtente(istrView.getUtenteId());
			}
			Utente funz = org.getUtente(form.getReferenteId());
			Utente diri = org.getUtente(org.getCarica(
					form.getCaricaResponsabileId()).getUtenteId());
			String aoo = org.getAreaOrganizzativa(form.getAooId())
					.getValueObject().getDescription();
			parameters.put("aoo_name",
					aoo + " - " + form.getUfficioCorrentePath());
			parameters.put("progressivo",
					form.getNumero() + "/" + form.getAnno());
			parameters.put("oggetto", form.getOggettoProcedimento());
			parameters.put("categoria", form.getTipoProcedimento()
					.getTitolario());
			if (diri != null)
				parameters.put("trattato_da", diri.getValueObject()
						.getCaricaFullName());
			if (funz != null)
				parameters.put("referente", funz.getValueObject()
						.getCaricaFullName());
			if (istr != null)
				parameters.put("istruttore", istr.getValueObject()
						.getCaricaFullName());
			else
				parameters.put("istruttore", "/");
			
			parameters.put("delegato", form.getDelegato());
			parameters.put("interessato", form.getAutoritaEmanante());
			
			parameters.put("data_creazione", form.getDataAvvio());
			if(form.getDataScadenza()!=null)
				parameters.put("data_scadenza", form.getDataScadenza());
			else
				parameters.put("data_scadenza", "/");
			ReportProtocolloView protocollo=ProtocolloDelegate.getInstance().getProtocolloView(form.getProtocolloId());
			if(protocollo!=null)
				parameters.put("protocollo", protocollo.getAnnoNumeroProtocollo()+"/"+protocollo.getAnnoProtocollo());
			else
				parameters.put("protocollo", "/");
			FascicoloView fascicolo=FascicoloDelegate.getInstance().getFascicoloViewById(form.getFascicoloId());
			if(fascicolo!=null)
				parameters.put("fascicolo", fascicolo.getPathProgressivo()+"-"+fascicolo.getOggetto());
			else
				parameters.put("fascicolo", "/");
			ArrayList<Integer> l = new ArrayList<Integer>();
			l.add(new Integer(1));
			CommonReportDS ds = new CommonReportDS(l);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			response.setHeader("Content-Disposition",
					"attachment;filename=ComunicazioneAvvioProcedimento" + form.getNumeroProcedimento()+ ".pdf");
			response.setHeader("Cache-control", "");
			response.setContentType("application/pdf");
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
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
	
	private void allacciaProtocollo(ProcedimentoForm form, HttpSession session,
			ActionMessages errors) {
		ProcedimentoDelegate delegate = ProcedimentoDelegate.getInstance();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		AllaccioView allaccio = delegate.getProtocolloAllacciabile(utente,
				Integer.valueOf(form.getAllaccioAnnoProtocollo()),
				Integer.valueOf(form.getAllaccioNumProtocollo()),
				form.getProcedimentoId());
		if (allaccio != null) {
			AllaccioVO allaccioVO = new AllaccioVO();
			if (allaccio != null && allaccio.getNumProtAllacciato() > 0) {
				allaccioVO.setProtocolloAllacciatoId(allaccio
						.getProtAllacciatoId());
				allaccioVO.setAllaccioDescrizione(form
						.getAllaccioNumProtocollo()
						+ "/"
						+ form.getAllaccioAnnoProtocollo()
						+ " ("
						+ allaccio.getTipoProtocollo() + ")");
				form.setProtocolloAllacciato(allaccioVO);
			}
			// }
		} else {
			errors.add("allacci", new ActionMessage(
					"protocollo_non_allacciabile"));
		}
	}

	protected void assegnaAdUtente(ProcedimentoForm form) {
		AssegnatarioView istr = new AssegnatarioView();
		istr.setUfficioId(form.getUfficioCorrenteId());
		istr.setNomeUfficio(form.getUfficioCorrente().getDescription());
		istr.setDescrizioneUfficio(form.getUfficioCorrentePath());
		istr.setUtenteId(form.getUtenteSelezionatoId());
		UtenteVO ute = form.getUtente(form.getUtenteSelezionatoId());
		istr.setNomeUtente(ute.getCaricaFullName());
		form.aggiungiIstruttore(istr);
	}

	private static Collection<AssegnatarioVO> aggiornaIstruttoriModel(
			ProcedimentoForm form, Utente utente) {
		UtenteVO uteVO = utente.getValueObject();
		Collection<AssegnatarioView> istruttori = form.getIstruttori();
		Collection<AssegnatarioVO> istruttoriVO = new ArrayList<AssegnatarioVO>();
		if (istruttori != null) {
			for (AssegnatarioView ass : istruttori) {
				AssegnatarioVO assegnatario = new AssegnatarioVO();
				Date now = new Date();
				assegnatario.setDataAssegnazione(now);
				assegnatario.setDataOperazione(now);
				assegnatario.setRowCreatedUser(uteVO.getUsername());
				assegnatario.setRowUpdatedUser(uteVO.getUsername());
				assegnatario.setUfficioAssegnanteId(ass
						.getUfficioAssegnanteId());
				assegnatario.setCaricaAssegnanteId(ass.getCaricaAssegnanteId());
				assegnatario.setUfficioAssegnatarioId(ass.getUfficioId());
				if (ass.getUtenteId() != 0) {
					assegnatario.setCaricaAssegnatarioId(CaricaDelegate
							.getInstance()
							.getCaricaByUtenteAndUfficio(ass.getUtenteId(),
									ass.getUfficioId()).getCaricaId());
				} else {
					assegnatario.setCaricaAssegnatarioId(0);
				}
				assegnatario.setLavorato(ass.isLavorato());
				assegnatario.setTitolareProcedimento(ass
						.isTitolareProcedimento());
				istruttoriVO.add(assegnatario);
			}
		}
		return istruttoriVO;
	}

	private void aggiornaIstruttoriForm(Collection<AssegnatarioVO> istruttori,
			ProcedimentoForm form) {
		if (istruttori != null) {
			Organizzazione org = Organizzazione.getInstance();
			for (Iterator<AssegnatarioVO> i = istruttori.iterator(); i
					.hasNext();) {
				AssegnatarioVO assegnatario = (AssegnatarioVO) i.next();
				AssegnatarioView istr = new AssegnatarioView();
				istr.setUfficioId(form.getUfficioCorrenteId());
				istr.setNomeUfficio(form.getUfficioCorrente().getDescription());
				istr.setDescrizioneUfficio(form.getUfficioCorrentePath());
				istr.setUtenteId(org.getCarica(
						assegnatario.getCaricaAssegnatarioId()).getUtenteId());
				istr.setNomeUtente(form.getUtente(istr.getUtenteId())
						.getCaricaFullName());
				istr.setLavorato(assegnatario.isLavorato());
				form.aggiungiIstruttore(istr);
			}
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

	private void rimuoviIstruttori(ProcedimentoForm form) {
		String[] istruttori = form.getIstruttoriSelezionatiId();
		if (istruttori != null) {
			for (int i = 0; i < istruttori.length; i++) {
				String istruttore = istruttori[i];
				if (istruttore != null) {
					form.rimuoviIstruttore(istruttore);
				}
			}
		}
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		HttpSession session = request.getSession();
		ProcedimentoForm pForm = (ProcedimentoForm) form;
		ProcedimentoDelegate delegate = ProcedimentoDelegate.getInstance();
		Organizzazione org = Organizzazione.getInstance();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		boolean indietroVisibile = false;
		pForm.setIndietroVisibile(indietroVisibile);
		if (request.getParameter("annullaAction") != null
				|| request.getAttribute("procedimentoPrecaricato") != null
				|| request.getParameter("visualizzaProcedimentoId") != null) {
			pForm.inizializzaForm();
			pForm.setAooId(utente.getValueObject().getAooId());
			pForm.setTipiFinalita(LookupDelegate.getTipiFinalitaProcedimento());
			Ufficio uff = org.getUfficio(utente.getUfficioInUso());
			pForm.setTipiProcedimento(AmministrazioneDelegate.getInstance()
					.getTipiProcedimentoByUfficioPrincipale(uff.getValueObject().getId()));
			impostaTipoProcedimento(pForm, 0);
			updateVisibilitaUffici(pForm);
			pForm.setUfficioCorrente(utente.getUfficioVOInUso());
			caricaReferenti(utente.getUfficioInUso(), pForm, org);
			caricaListaIstruttori(uff, pForm, org);
			session.setAttribute(mapping.getAttribute(), pForm);
		}

		if (request.getAttribute("procedimentoPrecaricato") != null) {
			ProcedimentoVO pre = (ProcedimentoVO) request
					.getAttribute("procedimentoPrecaricato");
			request.removeAttribute("procedimentoPrecaricato");
			aggiornaProcedimentoForm(pre, pForm, utente);
			pForm.setTipiProcedimento(AmministrazioneDelegate.getInstance().getTipiProcedimentoByUfficioPrincipale(pForm.getUfficioCorrenteId()));
			pForm.setUfficioResponsabileId(utente.getUfficioInUso());
			if (utente.getUfficioVOInUso().getCaricaDirigenteId() != 0)
				pForm.setCaricaResponsabileId(utente.getUfficioVOInUso()
						.getCaricaDirigenteId());
			else
				pForm.setCaricaResponsabileId(utente.getCaricaInUso());
			if (Boolean.TRUE.equals(session.getAttribute("tornaProtocollo"))) {
				indietroVisibile = true;
				pForm.setIndietroVisibile(indietroVisibile);
			}
			return mapping.findForward("input");
		}

		if (request.getParameter("visualizzaProcedimentoId") != null) {
			if (NumberUtil.isInteger(request
					.getParameter("visualizzaProcedimentoId"))) {
				Procedimento pro = delegate.getProcedimentoById(NumberUtil
						.getInt(request
								.getParameter("visualizzaProcedimentoId")));
				if (pro != null) {
					caricaProcedimentoForm(pForm, pro.getProcedimentoVO(),
							utente);

					aggiornaIstruttoriForm(pro.getProcedimentoVO()
							.getIstruttori(), pForm);
					pForm.setRiferimentiLegislativi(pro.getProcedimentoVO()
							.getRiferimentiLegislativi());

					pForm.setTipiProcedimento(AmministrazioneDelegate.getInstance().getTipiProcedimentoByUfficioPrincipale(pForm.getUfficioCorrenteId()));

					pForm.setProtocolli(pro.getProtocolli());
					pForm.setProtocolloSospensione(pro.getProtocolli().values());
					pForm.setFaldoni(pro.getFaldoni());
					pForm.setFascicoli(pro.getFascicoli());
				} else {
					errors.add("general", new ActionMessage(
							"procedimento.non.trovato"));
					saveErrors(request, errors);
					return (mapping.findForward("input"));
				}
			}

		}

		if (request.getParameter("impostaTipoProcedimentoAction") != null) {
			impostaTipoProcedimento(pForm, pForm.getTipoProcedimentoId());
			updateVisibilitaUffici(pForm);
			return (mapping.findForward("input"));
		} else if (request.getParameter("downloadAllegatoId") != null) {
			int docId = NumberUtil.getInt((String) request
					.getParameter("downloadAllegatoId"));
			DocumentoVO doc = pForm.getRiferimento(String.valueOf(docId));
			return downloadDocumento(mapping, doc, request, response);
		} else if (request.getParameter("downloadAllegatoProcedimentoId") != null) {
			int docId = NumberUtil.getInt((String) request
					.getParameter("downloadAllegatoProcedimentoId"));
			DocumentoVO doc = pForm.getTipoProcedimento().getRiferimento(
					String.valueOf(docId));
			return downloadDocumento(mapping, doc, request, response);
		} else if (request.getParameter("allegaRiferimentoAction") != null) {
			ProcedimentoFileUtility.uploadFile(pForm, request, errors);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			}
			return mapping.findForward("input");
		} else if (request.getParameter("rimuoviRiferimentiAction") != null) {
			String[] allegati = pForm.getRiferimentiLegislativiId();
			if (allegati != null) {
				for (int i = 0; i < allegati.length; i++) {
					pForm.rimuoviRiferimento(allegati[i]);
					allegati[i] = null;
				}
			}
			return mapping.findForward("input");
		} else if (request.getParameter("rimuoviIstruttoriAction") != null) {
			rimuoviIstruttori(pForm);
			return (mapping.findForward("input"));
		} else if (request.getParameter("visualizzaFascicoloId") != null) {

			if (NumberUtil.isInteger(request
					.getParameter("visualizzaFascicoloId"))) {
				Integer fId = Integer.valueOf(request
						.getParameter("visualizzaFascicoloId"));
				request.setAttribute("fascicoloId", fId);
				session.setAttribute("tornaProcedimento",
						pForm.getProcedimentoId());
				return mapping.findForward("visualizzaFascicolo");
			} else {
				logger.warn("Id Fascicolo non di formato numerico:"
						+ request.getParameter("visualizzaFascicoloId"));
			}
			return mapping.findForward("input");
		} else if (request.getParameter("btnCercaFascicoliDaProcedimento") != null) {
			session.setAttribute("tornaProcedimento", Boolean.TRUE);
			String nomeFascicolo = pForm.getCercaFascicoloNome();
			pForm.setCercaFascicoloNome("");
			request.setAttribute("cercaFascicoliDaProcedimento", nomeFascicolo);
			request.setAttribute("provenienza", "FascicoliDaProcedimento");
			return mapping.findForward("cercaFascicoliDaProcedimento");
		} else if (request.getParameter("rimuoviFascicoli") != null) {
			String[] ids = pForm.getFascicoliSelezionati();
			rimuoviFascicoli(ids, pForm);
		} else if (request.getParameter("btnCercaProtocolliDaProcedimento") != null) {
			request.setAttribute("cercaProtocolliDaProcedimento",
					pForm.getCercaProtocolloOggetto());
			pForm.setCercaProtocolloOggetto("");
			session.setAttribute("provenienza", "protocolliDaProcedimento");
			session.setAttribute("indietroProtocolliDaProcedimento",
					Boolean.TRUE);
			session.setAttribute("tornaProcedimento", Boolean.TRUE);
			return mapping.findForward("cercaProtocolliDaProcedimento");
		} else if (request.getParameter("rimuoviProtocolli") != null) {
			String[] ids = pForm.getProtocolliSelezionati();
			rimuoviProtocolli(ids, pForm);
		} else if (request.getParameter("btnCercaFaldoniDaProcedimento") != null) {
			request.setAttribute("cercaFaldoneDaProcedimento",
					pForm.getCercaFaldoneOggetto());
			pForm.setCercaFaldoneOggetto("");
			session.setAttribute("provenienza", "faldoniDaProcedimento");
			session.setAttribute("tornaProcedimento", Boolean.TRUE);
			return mapping.findForward("cercaFaldoniDaProcedimento");
		} else if (request.getParameter("rimuoviFaldoni") != null) {
			String[] ids = pForm.getFaldoniSelezionati();
			rimuoviFaldoni(ids, pForm);
		} else if (request.getParameter("visualizzaFaldoneId") != null) {
			request.setAttribute("caricaFaldoneId",
					request.getParameter("visualizzaFaldoneId"));
			return mapping.findForward("visualizzaFaldone");
		} else if (request.getParameter("protocolloSelezionato") != null) {
			Integer protocolloId = new Integer(
					request.getParameter("protocolloSelezionato"));
			request.setAttribute("protocolloId", protocolloId);
			return (mapping.findForward("visualizzaProtocolloIngresso"));
		} else if (request.getParameter("btnChiudi") != null) {
			return mapping.findForward("visualizzaChiusura");
		} else if (request.getParameter("btnRiapri") != null) {
			return mapping.findForward("visualizzaRiapertura");
		} else if (request.getParameter("chiusuraAction") != null) {
			errors = pForm.validate(mapping, request);
			if (errors.isEmpty()) {
				boolean chiudi = delegate.chiudiProcedimento(
						pForm.getProcedimentoId(), pForm.getFascicoloId(),
						utente);
				if (chiudi) {
					pForm.setStatoId(1);
					return mapping.findForward("visualizzaProcedimento");
				} else
					errors.add("general", new ActionMessage(
							"errore_nel_salvataggio"));

				saveErrors(request, errors);
				return mapping.findForward("input");

			}
		} else if (request.getParameter("riaperturaAction") != null) {
			errors = pForm.validate(mapping, request);
			if (errors.isEmpty()) {
				boolean chiudi = delegate.riapriProcedimento(
						pForm.getProcedimentoId(), pForm.getFascicoloId(),
						utente);
				if (chiudi) {
					pForm.setStatoId(0);
					return mapping.findForward("visualizzaProcedimento");
				} else
					errors.add("general", new ActionMessage(
							"errore_nel_salvataggio"));

				saveErrors(request, errors);
				return mapping.findForward("input");

			}
		} else if (request.getParameter("impostaPosizioneAction") != null) {
			errors = pForm.validate(mapping, request);
			if (errors.isEmpty()) {
				boolean inviato = delegate
						.inviaProcedimento(pForm.getProcedimentoId(),
								pForm.getPosizione(), utente);
				if (inviato) {
					return mapping.findForward("visualizzaProcedimento");
				} else
					errors.add("general", new ActionMessage(
							"errore_nel_salvataggio"));

				saveErrors(request, errors);
				return mapping.findForward("input");

			}
		} else if (request.getParameter("annullaOperazioneAction") != null) {
			pForm.setUfficioCorrenteId(utente.getUfficioInUso());
			AlberoUfficiBO.impostaUfficioUtentiAbilitati(utente, pForm, true);
			return mapping.findForward("input");
		} else if (request.getParameter("allacciaProtocolloAction") != null) {
			errors = pForm.validate(mapping, request);
			if (errors.isEmpty())
				allacciaProtocollo(pForm, session, errors);
			if (!errors.isEmpty())
				saveErrors(request, errors);
			return mapping.findForward("visualizzaSospensione");

		} else if (request.getParameter("rimuoviAllaccioAction") != null) {
			pForm.setAllaccioAnnoProtocollo(null);
			pForm.setAllaccioNumProtocollo(null);
			pForm.setProtocolloAllacciato(null);
			return mapping.findForward("visualizzaSospensione");
		} else if (request.getParameter("visualizzaProtocolloAllacciatoId") != null) {
			int id = new Integer(
					request.getParameter("visualizzaProtocolloAllacciatoId"));
			ReportProtocolloView r = ProtocolloDelegate.getInstance()
					.getProtocolloView(id);
			request.setAttribute("protocolloId", id);
			request.setAttribute("type", r.getTipoProtocollo());
			return (mapping.findForward("visualizzaAllaccio"));
		} else if (request.getParameter("btnSospensione") != null) {
			pForm.setDataSospensione(DateUtil.formattaData(System
					.currentTimeMillis()));
			pForm.setEstremiSospensione(null);
			pForm.setProtocolloAllacciato(null);
			return mapping.findForward("visualizzaSospensione");
		} else if (request.getParameter("btnRiavvio") != null) {
			pForm.setEstremiSospensione(null);
			pForm.setProtocolloAllacciato(null);
			return mapping.findForward("visualizzaSospensione");
		} else if (request.getParameter("sospensioneAction") != null) {
			errors = pForm.validate(mapping, request);
			if (errors.isEmpty()) {
				boolean sospeso = delegate.setProcedimentoSospeso(
						pForm.getProcedimentoId(),
						pForm.getEstremiSospensione(),
						pForm.getProtocolloAllacciato(), utente);
				if (sospeso) {
					pForm.setSospeso(true);
					if (pForm.getProtocolloAllacciato() != null)
						pForm.setProtocolloSospensione((ProtocolloProcedimentoView) pForm
								.getProtocolli().get(
										pForm.getProtocolloAllacciato()
												.getProtocolloAllacciatoId()));
					return mapping.findForward("input");
				} else
					errors.add("general", new ActionMessage(
							"errore_nel_salvataggio"));
			}
			saveErrors(request, errors);
			return mapping.findForward("visualizzaSospensione");

		} else if (request.getParameter("annullaSospensioneAction") != null) {
			errors = pForm.validate(mapping, request);
			if (errors.isEmpty()) {
				boolean annullato = delegate.annullaProcedimentoSospeso(
						pForm.getProcedimentoId(),
						pForm.getEstremiSospensione(), utente);
				if (annullato) {
					pForm.setSospeso(false);
					pForm.setDataSospensione(null);
					pForm.resetProtocolloSospensione();
					return mapping.findForward("input");
				} else
					errors.add("general", new ActionMessage(
							"errore_nel_salvataggio"));
			}
			saveErrors(request, errors);
			return mapping.findForward("visualizzaSospensione");

		} else if (request.getParameter("riavviaProcedimentoAction") != null) {
			errors = pForm.validate(mapping, request);
			if (errors.isEmpty()) {
				if (pForm.getDataScadenza() != null) {
					int diffDay = delegate.getIntervalloTempoSospensione(pForm
							.getProcedimentoId());
					pForm.setDataScadenza(DateUtil.addDays(
							pForm.getDataScadenza(), diffDay));
				}
				boolean riavviato = delegate.setProcedimentoRiavviato(
						pForm.getProcedimentoId(),
						pForm.getEstremiSospensione(), pForm.getDataScadenza(),
						pForm.getProtocolloAllacciato(), utente);
				if (riavviato) {
					pForm.resetProtocolloSospensione();
					pForm.setSospeso(false);
					pForm.setDataSospensione(null);
					return mapping.findForward("input");
				} else
					errors.add("general", new ActionMessage(
							"errore_nel_salvataggio"));
			}
			saveErrors(request, errors);
			return mapping.findForward("visualizzaSospensione");
		} else if (request.getParameter("btnAnnulla") != null) {

			if (Boolean.TRUE.equals(session.getAttribute("tornaProtocollo"))) {
				session.removeAttribute("tornaProtocollo");
				pForm.setOggettoProcedimento("");
				ProtocolloForm protForm = (ProtocolloForm) session
						.getAttribute("protocolloForm");
				if (protForm instanceof ProtocolloIngressoForm) {
					return (mapping.findForward("tornaProtocolloIngresso"));
				} else if (protForm instanceof ProtocolloUscitaForm) {
					return (mapping.findForward("tornaProtocolloUscita"));
				} else {
					if (protForm.isDaScarico())
						return (mapping.findForward("tornaPostaPresaInCarico"));
					else
						return (mapping.findForward("tornaPostaInterna"));
				}
			}
		} else if (request.getParameter("annullaInteressatoAction") != null) {
			pForm.setInteressato(null);
			return (mapping.findForward("input"));
		} else if (request.getParameter("annullaAutoritaEmananteAction") != null) {
			pForm.setAutoritaEmanante(null);
			return (mapping.findForward("input"));
		} else if (request.getParameter("annullaDelegatoAction") != null) {
			pForm.setDelegato(null);
			return (mapping.findForward("input"));
		} else if (request.getParameter("assegnaUtenteAction") != null) {
			assegnaAdUtente(pForm);
			return (mapping.findForward("input"));
		} else if (session.getAttribute("interessato") != null) {
			SoggettoVO interessato = (SoggettoVO) session
					.getAttribute("interessato");
			session.removeAttribute("interessato");
			pForm.setInteressato(interessato.getCognome()+" "+interessato.getNome());
			pForm.setIndiInteressato(interessato.getIndirizzoCompleto());
			return (mapping.findForward("input"));
		} else if (session.getAttribute("autorita_emanante") != null) {
			SoggettoVO aut = (SoggettoVO) session
					.getAttribute("autorita_emanante");
			session.removeAttribute("autorita_emanante");
			pForm.setAutoritaEmanante(aut.getDescrizioneDitta());
			return (mapping.findForward("input"));
		} else if (session.getAttribute("delegato") != null) {
			SoggettoVO delegato = (SoggettoVO) session.getAttribute("delegato");
			session.removeAttribute("delegato");
			pForm.setDelegato(delegato.getCognome()+" "+delegato.getNome());
			pForm.setIndiDelegato(delegato.getIndirizzoCompleto());
			return (mapping.findForward("input"));

		}

		else if (request.getParameter("cercaInteressatoAction") != null) {
			session.setAttribute("tornaProcedimento", Boolean.TRUE);
			session.setAttribute("isInteressato", true);
			return mapping.findForward("cercaPersonaFisica");
			
		} else if (request.getParameter("cercaDelegatoAction") != null) {
			session.setAttribute("tornaProcedimento", Boolean.TRUE);
			session.setAttribute("isDelegato", true);
			return mapping.findForward("cercaPersonaFisica");
			
		} else if (request.getParameter("cercaAutoritaEmananteAction") != null) {
			session.setAttribute("tornaProcedimento", Boolean.TRUE);
			request.setAttribute("descrizioneDitta", "");
			session.setAttribute("provenienza",
					"personaFisicaNuovoProcedimento");
			return mapping.findForward("cercaPersonaGiuridica");

		} else if (request.getParameter("salvaProcedimentoAction") != null) {
			if (pForm.getTipoProcedimento().isUll())
				errors = pForm.validateRicorsoULL(mapping, request);
			else
				errors = pForm.validate(mapping, request);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			} else {
				if (request.getParameter("visibilitaUfficiPartecipantiId") == null) { 
					pForm.setVisibilitaUfficiPartecipantiId(new String[0]);
				} 
				ProcedimentoVO vo = new ProcedimentoVO();
				Procedimento pro = new Procedimento();
				caricaProcedimentoVO(pForm, vo, utente);
				pro.setProcedimentoVO(vo);
				pro.setFaldoni(pForm.getFaldoni());
				pro.setFascicoli(pForm.getFascicoli());
				pro.setProtocolli(pForm.getProtocolli());
				ProcedimentoVO newVo = new ProcedimentoVO();
				if (pForm.getProcedimentoId() > 0) {
					newVo = delegate.aggiornaProcedimento(pro, utente);
				} else {
					newVo = delegate.salvaProcedimento(pro, utente);
				}
				if (newVo.getReturnValue() != ReturnValues.SAVED) {
					errors.add("general", new ActionMessage(
							"errore_nel_salvataggio"));
					saveErrors(request, errors);
				} else {
					if (pForm.getProcedimentoId() == 0) {
						ProtocolloForm pf = (ProtocolloForm) session
								.getAttribute("protocolloForm");
						pForm.aggiungiFascicolo(FascicoloDelegate.getInstance()
								.getFascicoloViewById(newVo.getFascicoloId()));
						ProtocolloProcedimentoVO ppVO = new ProtocolloProcedimentoVO();
						ppVO.setProtocolloId(pf.getProtocolloId());
						ppVO.setNumeroProtocollo(pf.getNumero());
						ppVO.setProcedimentoId(newVo.getId().intValue());
						ppVO.setNumeroProcedimento(newVo
								.getNumeroProcedimento());
						ppVO.setOggetto(newVo.getOggetto());
						ppVO.setFascicoloId(newVo.getFascicoloId());
						ppVO.setVersione(0);
						pf.aggiungiProcedimento(ppVO);

						caricaProcedimentoForm(pForm, newVo, utente);
						messages.add("general", new ActionMessage("msg.salvato"));
						saveMessages(request, messages);
						return (mapping.findForward("stampaComunicazioneAvvio"));
					} 
					else {			
						pro = delegate.getProcedimentoById(pForm.getProcedimentoId());
						caricaProcedimentoForm(pForm, pro.getProcedimentoVO(),utente);
						aggiornaIstruttoriForm(pro.getProcedimentoVO().getIstruttori(), pForm);
						pForm.setRiferimentiLegislativi(pro.getProcedimentoVO().getRiferimentiLegislativi());

						pForm.setProtocolli(pro.getProtocolli());
						pForm.setProtocolloSospensione(pro.getProtocolli().values());
						pForm.setFaldoni(pro.getFaldoni());
						pForm.setFascicoli(pro.getFascicoli());
						messages.add("general",
								new ActionMessage("msg.salvato"));
						saveMessages(request, messages);
						return (mapping.findForward("visualizzaProcedimento"));
					}
				}
			}

		} else if (request.getParameter("stampaComunicazioneAvvioAction") != null ) {
			stampaComunicazioneAvvio(response, pForm, utente.getAreaOrganizzativa().getDescription());
			return null;
		} else if (request.getParameter("tornaProtocolloAction") != null) {
			ProtocolloForm pf = (ProtocolloForm) session.getAttribute("protocolloForm");
			if ("I".equals(pf.getFlagTipo())) 
				if (pf.isDaScarico())
					return (mapping.findForward("tornaScaricoProcedimento"));
				else
					return (mapping.findForward("tornaProtocolloIngresso"));
			if ("P".equals(pf.getFlagTipo()))
				if (pf.isDaScarico())
					return (mapping.findForward("tornaPostaPresaInCarico"));
				else
					return (mapping.findForward("tornaPostaInterna"));
		} else if (request.getParameter("btnStoria") != null) {
			request.setAttribute("procedimentoId",
					new Integer(pForm.getProcedimentoId()));
			caricaProcedimento(request, pForm);
			return (mapping.findForward("storiaProcedimento"));

		} else if (request.getParameter("btnModifica") != null) {
			return (mapping.findForward("modificaProcedimento"));
		} else if (request.getParameter("btnStampa") != null) {
			stampaFrontespizio(response, pForm, utente.getAreaOrganizzativa().getDescription());
			return null;
		} else if (request.getParameter("btnNuovoDocumento") != null) {
			session.setAttribute("tornaProcedimento", pForm.getProcedimentoId());
			return mapping.findForward("nuovoDocumento");
		} else if (request.getParameter("btnCompilaLettera") != null) {
			session.setAttribute("tornaProcedimento", pForm.getProcedimentoId());
			return mapping.findForward("compilaLettera");
		} else if (request.getParameter("btnEditor") != null) {
			request.setAttribute("tornaProcedimento", pForm.getProcedimentoId());
			return mapping.findForward("documentoEditor");
		} else if (request.getParameter("btnRiassegnaULL") != null) {
			AlberoUfficiBO.impostaUfficioUtentiAbilitatiRoot(utente, pForm,
					true);
			return mapping.findForward("riassegnaProcedimento");
		} else if (request.getParameter("rimuoviAssegnatariAction") != null) {
			pForm.setAssegnatarioPrincipale(null);
			return mapping.findForward("riassegnaProcedimento");
		} else if (request.getParameter("impostaUfficioAction") != null) {
			pForm.setUfficioCorrenteId(pForm.getUfficioSelezionatoId());
			AlberoUfficiBO.impostaUfficioUtentiAbilitati(utente, pForm, true);
			return mapping.findForward("riassegnaProcedimento");
		} else if (request.getParameter("ufficioPrecedenteAction") != null) {
			pForm.setUfficioCorrenteId(pForm.getUfficioCorrente().getParentId());
			AlberoUfficiBO.impostaUfficioUtentiAbilitati(utente, pForm, true);
			return mapping.findForward("riassegnaProcedimento");
		} else if (request.getParameter("assegnaUfficioSelezionatoAction") != null) {
			assegnaAdUfficio(pForm, pForm.getUfficioSelezionatoId(), utente);
			return mapping.findForward("riassegnaProcedimento");
		} else if (request.getParameter("confermaRiassegnazioneAction") != null) {
			errors = pForm.validate(mapping, request);
			if (errors.isEmpty()) {
				boolean riassegna = delegate.cambiaReferenteProcedimento(
						pForm.getProcedimentoId(),
						pForm.getAssegnatarioPrincipale(), utente);
				if (riassegna) {
					request.setAttribute("procedimentoId",
							pForm.getProcedimentoId());
					return mapping.findForward("ricaricaProcedimento");
				} else
					errors.add("general", new ActionMessage(
							"errore_nel_salvataggio"));

				saveErrors(request, errors);
				return mapping.findForward("input");

			}
		} else if (request.getAttribute("procedimentoId") != null) {
			caricaProcedimento(request, pForm);
			return (mapping.findForward("visualizzaProcedimento"));
		}
		return (mapping.findForward("input"));
	}

	private void rimuoviFascicoli(String[] ids, ProcedimentoForm form) {
		if (ids != null) {
			for (int i = 0; i < ids.length; i++) {
				form.rimuoviFascicolo(ids[i]);
			}
		}
	}

	private void rimuoviFaldoni(String[] ids, ProcedimentoForm form) {
		if (ids != null) {
			for (int i = 0; i < ids.length; i++) {
				form.rimuoviFaldone(ids[i]);
			}
		}
	}

	private void rimuoviProtocolli(String[] ids, ProcedimentoForm form) {
		if (ids != null) {
			for (int i = 0; i < ids.length; i++) {
				form.rimuoviProtocollo(ids[i]);
			}
		}
	}

	protected void assegnaAdUfficio(ProcedimentoForm form, int ufficioId,
			Utente utente) {
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(ufficioId);
		Organizzazione org = Organizzazione.getInstance();
		Ufficio uff = org.getUfficio(ufficioId);
		ass.setNomeUfficio(uff.getValueObject().getDescription());
		ass.setCaricaId(uff.getValueObject().getCaricaDirigenteId());
		ass.setDescrizioneUfficio(uff.getPath());
		ass.setCaricaAssegnanteId(utente.getCaricaInUso());
		ass.setUfficioAssegnanteId(utente.getUfficioInUso());
		form.setAssegnatarioPrincipale(ass);
		form.setUfficioSelezionatoId(0);
	}

	private void aggiornaProcedimentoForm(ProcedimentoVO pVO,
			ProcedimentoForm pForm, Utente utente) {
		if (pVO.getDataAvvio() != null)
			pForm.setDataAvvio(DateUtil.formattaData(pVO.getDataAvvio()
					.getTime()));
		if (pVO.getDataEvidenza() != null)
			pForm.setDataEvidenza(DateUtil.formattaData(pVO.getDataEvidenza()
					.getTime()));
		pForm.setNote(pVO.getNote());
		pForm.setProgressivo(pVO.getNumero());
		pForm.setNumeroProcedimento(pVO.getNumeroProcedimento());
		pForm.setNumeroProtocollo(pVO.getNumeroProtovollo());
		pForm.setOggettoProcedimento(pVO.getOggetto());
		pForm.setPosizione(pVO.getPosizione());
		pForm.setProtocolloId(pVO.getProtocolloId());
		pForm.setReferenteId(CaricaDelegate.getInstance()
				.getCarica(pVO.getReferenteId()).getUtenteId());
		pForm.setStatoId(pVO.getStatoId());
		pForm.setStatoPrecedenteId(pVO.getStatoId());
		pForm.setTipoFinalitaId(pVO.getTipoFinalitaId());
		pForm.setTipoProcedimentoId(pVO.getTipoProcedimentoId());
		pForm.setUfficioCorrenteId(pVO.getUfficioId());
		pForm.setVersione(pVO.getVersione());
		Organizzazione org = Organizzazione.getInstance();
		Ufficio ufficioCorrente = org.getUfficio(pVO.getUfficioId());
		Ufficio uff = ufficioCorrente;
		pForm.setUfficioCorrentePath(uff.getPath());
		UtenteDelegate referente = UtenteDelegate.getInstance();
		pForm.setNomeReferente(referente.getUtente(pForm.getReferenteId())
				.getUsername());
		ProtocolloDelegate protocollo = ProtocolloDelegate.getInstance();
		ProtocolloVO prt = protocollo
				.getProtocolloVOById(pVO.getProtocolloId());
		pForm.setNumeroProtocollo(pVO.getAnno()
				+ StringUtil.formattaNumeroProtocollo(prt.getNumProtocollo()
						+ "", 7));
		pForm.setDataRegistrazioneProtocollo(prt.getDataRicezione() != null ? prt
				.getDataRicezione() : prt.getDataRegistrazione());
		pForm.setFascicoloId(pVO.getFascicoloId());
		
		pForm.setInteressato(pVO.getInteressato());
		pForm.setDelegato(pVO.getDelegato());
		pForm.setIndiInteressato(pVO.getIndiInteressato());
		pForm.setIndiDelegato(pVO.getIndiDelegato());
		pForm.setAutoritaEmanante(pVO.getAutoritaEmanante());
		
		pForm.setModificabile(isModificabile(utente, pVO));
		pForm.setSospeso(pVO.isSospeso());
		if (pVO.getDataSospensione() != null)
			pForm.setDataSospensione(DateUtil.formattaData(pVO
					.getDataSospensione().getTime()));
		pForm.setEstremiSospensione(pVO.getEstremiSospensione());
		if (pVO.getDataScadenza() != null)
			pForm.setDataScadenza(DateUtil.formattaData(pVO.getDataScadenza()
					.getTime()));
	}

	private boolean isModificabile(Utente utente, ProcedimentoVO procedimento) {
		if (utente.getCaricaInUso() == procedimento.getResponsabileId()
				|| utente.getCaricaInUso() == procedimento.getReferenteId())
			return true;
		if (procedimento.getIstruttori() != null) {
			for (Object o : procedimento.getIstruttori()) {
				AssegnatarioVO assegnatario = (AssegnatarioVO) o;
				if (assegnatario.getCaricaAssegnatarioId() == utente
						.getCaricaInUso())
					return true;
			}
		}
		if (procedimento.getPosizione() != null
				&& procedimento.getPosizione().equals("A")
				&& utente.getCaricaVOInUso().isResponsabileEnte())
			return true;
		return false;
	}

	private void caricaProcedimentoVO(ProcedimentoForm form, ProcedimentoVO vo,
			Utente utente) {
		vo.setId(form.getProcedimentoId());
		vo.setAooId(form.getAooId());
		vo.setAnno(form.getAnno());
		if (DateUtil.isData(form.getDataAvvio()))
			vo.setDataAvvio(DateUtil.toDate(form.getDataAvvio()));
		if (DateUtil.isData(form.getDataEvidenza()))
			vo.setDataEvidenza(DateUtil.toDate(form.getDataEvidenza()));
		vo.setNote(form.getNote());
		vo.setNumero(form.getNumero());
		vo.setNumeroProcedimento(form.getNumeroProcedimento());
		vo.setNumeroProtovollo(form.getNumeroProtocollo());
		vo.setOggetto(form.getOggettoProcedimento());
		vo.setPosizione(form.getPosizione());
		vo.setProtocolloId(form.getProtocolloId());
		vo.setResponsabileId(form.getCaricaResponsabileId());
		vo.setReferenteId(CaricaDelegate
				.getInstance()
				.getCaricaByUtenteAndUfficio(form.getReferenteId(),
						form.getUfficioCorrente().getId()).getCaricaId());
		vo.setUfficioId(form.getUfficioCorrenteId());
		if (form.getGiorniMax() == null
				|| form.getGiorniMax().trim().equals(""))
			vo.setGiorniMax(0);
		else
			vo.setGiorniMax(Integer.valueOf(form.getGiorniMax()));
		if (form.getGiorniAlert() == null
				|| form.getGiorniAlert().trim().equals(""))
			vo.setGiorniAlert(0);
		else
			vo.setGiorniAlert(Integer.valueOf(form.getGiorniAlert()));
		vo.setUfficiPartecipanti(form.getUfficiPartecipanti());
		vo.setDelegato(form.getDelegato());
		vo.setInteressato(form.getInteressato());
		vo.setIndiDelegato(form.getIndiDelegato());
		vo.setIndiInteressato(form.getIndiInteressato());
		vo.setAutoritaEmanante(form.getAutoritaEmanante());
		vo.setRiferimentiLegislativi(form.getRiferimentiLegislativi());
		vo.setIstruttori(aggiornaIstruttoriModel(form, utente));
		vo.setStatoId(form.getStatoId());
		if (form.getStatoPrecedenteId() != form.getStatoId()) {
			vo.setPosizione(null);
		}
		vo.setTipoFinalitaId(form.getTipoFinalitaId());
		vo.setTipoProcedimentoId(form.getTipoProcedimentoId());
		vo.setVersione(form.getVersione());
		vo.setIndicazioni(form.getIndicazioni());
		vo.setEstremiProvvedimento(form.getEstremiProvvedimento());
		vo.setCaricLavId(vo.getPosizione());
		//aggiornaAssegnatariModel(form, vo);

	}

	protected void caricaProcedimento(HttpServletRequest request,
			ProcedimentoForm form) {
		Integer procedimentoId = (Integer) request
				.getAttribute("procedimentoId");
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		Procedimento procedimento = new Procedimento();
		Integer versioneId = (Integer) request.getAttribute("versioneId");
		if (procedimentoId != null) {
			ProcedimentoDelegate pd = ProcedimentoDelegate.getInstance();
			if (versioneId == null) {
				procedimento = pd
						.getProcedimentoById(procedimentoId.intValue());
			} else {
				int versione = versioneId.intValue();
				int id = procedimentoId.intValue();
				int versioneCorrente = pd.getProcedimentoVO(
						procedimentoId.intValue()).getVersione();
				if (versioneCorrente > versione) {
					form.setFromStoria(true);
					procedimento = pd.getProcedimentoByIdVersione(
							procedimentoId, versione);
				} else {
					procedimento = pd.getProcedimentoById(id);
				}
			}
			session.setAttribute(Constants.PROCEDIMENTO, procedimento);
		} else {
			procedimento = (Procedimento) session
					.getAttribute(Constants.PROCEDIMENTO);
		}
		if (procedimento != null) {
			caricaProcedimentoForm(form, procedimento.getProcedimentoVO(),
					utente);
			aggiornaIstruttoriForm(procedimento.getProcedimentoVO()
					.getIstruttori(), form);
			form.setRiferimentiLegislativi(procedimento.getProcedimentoVO()
					.getRiferimentiLegislativi());
			form.setTipiProcedimento(AmministrazioneDelegate.getInstance()
					.getTipiProcedimentoByUfficio(form.getUfficioCorrenteId()));
			form.setProtocolli(procedimento.getProtocolli());
			form.setProtocolloSospensione(procedimento.getProtocolli().values());
			form.setFascicoli(procedimento.getFascicoli());
		}
	}

	private void caricaProcedimentoForm(ProcedimentoForm form,
			ProcedimentoVO vo, Utente utente) {
		Organizzazione org = Organizzazione.getInstance();
		Ufficio uff = org.getUfficio(vo.getUfficioId());
		form.setProcedimentoId(vo.getId().intValue());
		form.setAnno(vo.getAnno());
		if (vo.getDataAvvio() != null)
			form.setDataAvvio(DateUtil
					.formattaData(vo.getDataAvvio().getTime()));
		if (vo.getDataEvidenza() != null)
			form.setDataEvidenza(DateUtil.formattaData(vo.getDataEvidenza()
					.getTime()));
		form.setNote(vo.getNote());
		form.setNumero(vo.getNumero());
		form.setNumeroProcedimento(vo.getNumeroProcedimento());
		form.setNumeroProtocollo(vo.getNumeroProtovollo());
		form.setOggettoProcedimento(vo.getOggetto());
		form.setPosizione(vo.getPosizione());
		form.setProtocolloId(vo.getProtocolloId());
		form.setReferenteId(CaricaDelegate.getInstance()
				.getCarica(vo.getReferenteId()).getUtenteId());
		form.setStatoId(vo.getStatoId());
		form.setStatoPrecedenteId(vo.getStatoId());
		form.setTipoFinalitaId(vo.getTipoFinalitaId());
		form.setTipoProcedimentoId(vo.getTipoProcedimentoId());
		form.setUfficioCorrenteId(vo.getUfficioId());
		form.setVersione(vo.getVersione());
		form.setUfficioCorrente(uff.getValueObject());
		form.setUfficioCorrentePath(uff.getValueObject().getDescription());
		caricaReferenti(vo.getUfficioId(), form, org);
		caricaListaIstruttori(uff, form, org);
		form.setCaricaResponsabileId(vo.getResponsabileId());
		form.setUfficioResponsabileId(vo.getUfficioId());
		form.setFascicoloId(vo.getFascicoloId());
		form.setInteressato(vo.getInteressato());
		form.setDelegato(vo.getDelegato());
		form.setIndiInteressato(vo.getIndiInteressato());
		form.setIndiDelegato(vo.getIndiDelegato());
		form.setAutoritaEmanante(vo.getAutoritaEmanante());
		form.setIndicazioni(vo.getIndicazioni());
		form.setEstremiProvvedimento(vo.getEstremiProvvedimento());
		
		impostaTipoProcedimento(form, form.getTipoProcedimentoId());
		form.setUfficiPartecipanti(vo.getUfficiPartecipanti());
		updateVisibilitaUffici(form);

		if (!form.isFromStoria())
			form.setModificabile(isModificabile(utente, vo));
		form.setSospeso(vo.isSospeso());
		if (vo.getDataSospensione() != null)
			form.setDataSospensione(DateUtil.formattaData(vo
					.getDataSospensione().getTime()));
		form.setEstremiSospensione(vo.getEstremiSospensione());
		if (vo.getDataScadenza() != null)
			form.setDataScadenza(DateUtil.formattaData(vo.getDataScadenza()
					.getTime()));
	}

	private void caricaReferenti(int ufficioId, ProcedimentoForm form,
			Organizzazione org) {
		String[] ids = UfficioDelegate.getInstance().getReferentiByUfficio(
				ufficioId);
		SortedMap<String, UtenteVO> referenti = new TreeMap<String, UtenteVO>();
		if (ids != null) {
			for (int i = 0; i < ids.length; i++) {
				CaricaVO car = org.getCarica(NumberUtil.getInt(ids[i]));
				Utente u = org.getUtente(car.getUtenteId());
				if (u != null)
					referenti.put(u.getValueObject().getCognomeNome(),
							u.getValueObject());

			}
			form.setReferenti(referenti);

		}
	}

	private void caricaListaIstruttori(Ufficio ufficioCorrente,
			ProcedimentoForm form, Organizzazione org) {
		List<UtenteVO> list = new ArrayList<UtenteVO>();
		for (Iterator<Utente> i = ufficioCorrente.getUtenti().iterator(); i
				.hasNext();) {
			Utente ute = (Utente) i.next();
			list.add(ute.getValueObject());
		}
		Comparator<UtenteVO> c = new Comparator<UtenteVO>() {
			public int compare(UtenteVO ute1, UtenteVO ute2) {
				return ute1.getFullName().compareToIgnoreCase(
						ute2.getFullName());
			}
		};
		Collections.sort(list, c);
		form.setUtenti(list);
	}

	private void impostaTipoProcedimento(ProcedimentoForm form,
			int tipoProcedimentoId) {
		AmministrazioneDelegate del = AmministrazioneDelegate.getInstance();
		TipoProcedimentoVO tp = new TipoProcedimentoVO();
		if (tipoProcedimentoId == 0) {
			tp = form.getTipoProcedimentoFromArray(0);
		} else
			tp = del.getTipoProcedimento(tipoProcedimentoId);
		if (tp != null) {
			tp.setAmministrazioni(del.getAmministrazioniPartecipanti(tp
					.getIdTipo()));
			tp.setRiferimentiLegislativi(del.getRiferimenti(tp.getIdTipo()));
			form.setUfficiPartecipanti(del.getUfficiPartecipantiForProcedimento(tp.getIdTipo()));
			form.setTipoProcedimento(tp);
			form.setGiorniAlert(String.valueOf(tp.getGiorniAlert()));
			form.setGiorniMax(String.valueOf(tp.getGiorniMax()));
		} else
			form.setTipoProcedimento(new TipoProcedimentoVO());
		form.setRiferimentiLegislativi(new HashMap<String, DocumentoVO>(2));

		if (form.getTipoProcedimento().getIdTipo() != 0)
			if (form.getTipoProcedimento().isUll()) {
				form.setStatiProcedimento(form.getStatiProcedimentoULL());
				form.setPosizioniProcedimento(LookupDelegate.getPosizioniProcedimentoULL(form.getStatoId()));
			} else
				form.setStatiProcedimento(LookupDelegate.getStatiProcedimento());
	}
	
	private void updateVisibilitaUffici(ProcedimentoForm form) {
		List<String> visibilitaUffici = new ArrayList<String>();
		for (UfficioPartecipanteVO ufficio: form.getUfficiPartecipantiValues()) {
			if (ufficio.isVisibilita()) {
				visibilitaUffici.add(String.valueOf(ufficio.getUfficioId()));
			}
		}
		String[] assCompArray = new String[visibilitaUffici.size()];
		int index = 0;
		for (String uffString : visibilitaUffici) {
			assCompArray[index] = uffString;
			index++;
		}
		form.setVisibilitaUfficiPartecipantiId(assCompArray);
	}

}