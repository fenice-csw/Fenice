package it.compit.fenice.mvc.presentation.action.protocollo;

import it.compit.fenice.mvc.presentation.actionform.protocollo.RifiutoForm;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.ProtocolloIngresso;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.business.ReportProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloIngressoForm;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.presentation.helper.ReportType;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.report.protocollo.CommonReportDS;
import it.finsiel.siged.util.DateUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

public class RifiutaProtocolloAction extends Action {

	static Logger logger = Logger.getLogger(RifiutaProtocolloAction.class
			.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward actionForward = super.execute(mapping, form, request,
				response);
		if (actionForward != null) {
			return actionForward;
		}
		HttpSession session = request.getSession(true); 
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		RifiutoForm rForm = (RifiutoForm) form;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		if (form == null) {
			logger.info(" Creating new rifiutaAction");
			form = new ProtocolloIngressoForm();
			session.setAttribute(mapping.getAttribute(), form);
		}
		getInputPage(request, session);
		Integer protocolloId = (Integer) request.getAttribute("protocolloId");
		if (protocolloId != null) {
			int id = protocolloId.intValue();
			ProtocolloIngresso protocollo = ProtocolloDelegate.getInstance()
					.getProtocolloIngressoById(id);
			aggiornaDatiGeneraliForm(protocollo, rForm,utente);
			return mapping.findForward("input");
		}
		if (request.getParameter("rifiutaAction") != null) {
			if (rForm.getProtocolloId() != null) {
				ProtocolloIngresso pi = delegate
						.getProtocolloIngressoById(rForm.getProtocolloId());
				ProtocolloVO protocolloVO = pi.getProtocollo();
				protocolloVO.setDataScarico(null);
				if (rForm.getMsg() != null && !"".equals(rForm.getMsg().trim()))
					aggiornaMsg(rForm.getMsg(), pi,utente);
				delegate.rifiutaProtocollo(pi, "R", "F", utente,rForm.isTitolareProcedimento());
				caricaReportParameter(String.valueOf(rForm.getProtocolloId()), request, utente,
						rForm);
			}
		} else if (request.getParameter("annullaAction") != null) {
			rForm.inizializzaForm();
			if (session.getAttribute("TORNA_SCARICO_UTENTE") != null) {
				session.removeAttribute("TORNA_SCARICO_UTENTE");
				return (mapping.findForward("ritornaAssegnatiUtente"));
			} else if (session.getAttribute("TORNA_SCARICO_UFFICIO") != null) {
				session.removeAttribute("TORNA_SCARICO_UFFICIO");
				return (mapping.findForward("ritornaAssegnatiUtente"));
			} else if (session.getAttribute("TORNA_RESPINTI") != null) {
				session.removeAttribute("TORNA_RESPINTI");
				return (mapping.findForward("ritornaRespinti"));
			}else if (session.getAttribute("TORNA_SCARICO_FATTURE") != null) {
				session.removeAttribute("TORNA_SCARICO_FATTURE");
				return (mapping.findForward("ritornaFatture"));
			}
		} else if (request.getParameter("indietroAction") != null) {
			rForm.inizializzaForm();
			if (session.getAttribute("TORNA_SCARICO_UTENTE") != null) {
				session.removeAttribute("TORNA_SCARICO_UTENTE");
				return (mapping.findForward("ritornaAssegnatiUtente"));
			} else if (session.getAttribute("TORNA_SCARICO_UFFICIO") != null) {
				session.removeAttribute("TORNA_SCARICO_UFFICIO");
				return (mapping.findForward("ritornaAssegnatiUfficio"));
			} else if (session.getAttribute("TORNA_RESPINTI") != null) {
				session.removeAttribute("TORNA_RESPINTI");
				return (mapping.findForward("ritornaRespinti"));
			}else if (session.getAttribute("TORNA_SCARICO_FATTURE") != null) {
				session.removeAttribute("TORNA_SCARICO_FATTURE");
				return (mapping.findForward("ritornaFatture"));
			}
		} else if ("true".equals(request.getParameter("print"))) {
			stampaRifiutato(request, response);
			return null;
		}
		return mapping.findForward("input");
	}

	private static void getInputPage(HttpServletRequest request,
			HttpSession session) {
		if (request.getParameter("SCARICO_UTENTE") != null)
			session.setAttribute("TORNA_SCARICO_UTENTE", "TRUE");
		else if (request.getParameter("RESPINTI") != null)
			session.setAttribute("TORNA_RESPINTI", "TRUE");
		else if (request.getParameter("SCARICO_FATTURE") != null)
			session.setAttribute("TORNA_SCARICO_FATTURE", "TRUE");
		else
			session.setAttribute("TORNA_SCARICO_UFFICIO", "TRUE");
	}

	private void caricaReportParameter(String id, HttpServletRequest request,
			Utente utente, RifiutoForm form) {
		MessageResources messages = getResources(request);
		HashMap<String,String> common = new HashMap<String,String>();
		common.put("ReportTitle", messages
				.getMessage("report.title.stampa_rifiutato"));
		common.put("ReportSubTitle", utente.getRegistroVOInUso()
				.getDescrizioneRegistro());
		common.put("ProtocolloId", id);
		common.put("UfficioProtocollatore", utente.getUfficioVOInUso()
				.getDescription()
				+ "/" + utente.getValueObject().getFullName());
		common.put("print", "true");
		form.addReportType(ReportType.getIstanceByType(Parametri.FORMATO_HTML,
				common, messages));
		form.addReportType(ReportType.getIstanceByType(Parametri.FORMATO_PDF,
				common, messages));
		form.addReportType(ReportType.getIstanceByType(Parametri.FORMATO_XLS,
				common, messages));
	}

	public void stampaRifiutato(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		ServletContext context = this.getServlet().getServletContext();
		OutputStream os = response.getOutputStream();
		try {
			JasperDesign jasperDesign = JRXmlLoader.load(context
					.getRealPath("/")
					+ FileConstants.STAMPA_RIFIUTATO_REPORT_TEMPLATE);
			JasperReport jasperReport = JasperCompileManager
					.compileReport(jasperDesign);
			Calendar now = Calendar.getInstance();
			Map<String,Object> parameters = new HashMap<String,Object>();
			int protocolloId = Integer.parseInt(request
					.getParameter("ProtocolloId"));
			parameters.put("ReportTitle", request.getParameter("ReportTitle"));
			parameters.put("ReportSubTitle", request
					.getParameter("ReportSubTitle"));
			parameters.put("UfficioProtocollatore", request
					.getParameter("UfficioProtocollatore"));
			parameters.put("DataCorrente", DateUtil.formattaData(now.getTime()
					.getTime()));
			Collection<ReportProtocolloView> c = ReportProtocolloDelegate.getInstance()
					.stampaProtocolloRifiutato(protocolloId);
			CommonReportDS ds = new CommonReportDS(c);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			String exportFormat = request.getParameter("ReportFormat");
			response.setHeader("Content-Disposition",
					"attachment;filename=Rifiutato." + exportFormat);
			response.setHeader("Cache-control", "");
			if ("PDF".equals(exportFormat)) {
				response.setContentType("application/pdf");
				JRPdfExporter exporter = new JRPdfExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT,
						jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
				exporter.exportReport();
			} else if ("XLS".equals(exportFormat)) {
				response.setContentType("application/vnd.ms-excel");
				JRCsvExporter exporter = new JRCsvExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT,
						jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
				exporter.setParameter(
						JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
						Boolean.FALSE);
				exporter.exportReport();
			} else if ("TXT".equals(exportFormat)) {
			} else if ("XML".equals(exportFormat)) {
				response.setContentType("text/html");
				JRXmlExporter exporter = new JRXmlExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT,
						jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
				exporter.exportReport();
			} else if ("CSV".equals(exportFormat)) {
				JRCsvExporter exporter = new JRCsvExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT,
						jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
				exporter.exportReport();

			} else if ("".equals(exportFormat) || "HTML".equals(exportFormat)) {
				JRHtmlExporter exporter = new JRHtmlExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT,
						jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
				//exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP,imagesMap);
				exporter.setParameter(
						JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,
						new Boolean(false));
				exporter.exportReport();
			}
		} catch (Exception e) {
			logger.error("", e);
			response.setContentType("text/plain");
			e.printStackTrace(new PrintStream(os));
		} finally {
			os.close();
		}
	}

	private void aggiornaMsg(String msgAssegnatarioCompetente,
			ProtocolloIngresso protocollo, Utente utente) {
		Collection<AssegnatarioVO> assegnatari = protocollo.getAssegnatari();
		protocollo.setMsgAssegnatarioCompetente(msgAssegnatarioCompetente);
		if (assegnatari != null) {
			for (Iterator<AssegnatarioVO> i = assegnatari.iterator(); i.hasNext();) {
				AssegnatarioVO assegnatario = i.next();
				if ((assegnatario.getCaricaAssegnatarioId() == utente
						.getCaricaInUso() && assegnatario
						.getUfficioAssegnatarioId() == utente.getUfficioInUso())
						|| (assegnatario.getCaricaAssegnatarioId() == 0 && assegnatario
								.getUfficioAssegnatarioId() == utente
								.getUfficioInUso())){
					assegnatario.setMsgAssegnatarioCompetente(msgAssegnatarioCompetente);
				}
			}
		}
	}

	private void aggiornaDatiGeneraliForm(ProtocolloIngresso protocolloIngresso,
			RifiutoForm form,Utente utente) {
		ProtocolloVO protocollo=protocolloIngresso.getProtocollo();
		Integer id = protocollo.getId();
		form.setProtocolloId(id);
		form.setDataRegistrazione(DateUtil.formattaData(protocollo
				.getDataRegistrazione().getTime()));
		form.setNumeroProtocollo(protocollo.getNumProtocollo() + "/"
				+ protocollo.getAnnoRegistrazione());
		Date dataDoc = protocollo.getDataDocumento();
		form.setDataDocumento(dataDoc == null ? null : DateUtil
				.formattaData(dataDoc.getTime()));
		Date dataRic = protocollo.getDataRicezione();
		form.setDataRicezione(dataRic == null ? null : DateUtil
				.formattaData(dataRic.getTime()));
		form.setRiservato(protocollo.isRiservato());
		form.setStato(protocollo.getStatoProtocollo());
		form.setOggetto(protocollo.getOggetto());
		form.setUfficioProtocollatoreId(protocollo.getUfficioProtocollatoreId());
		form.setUtenteProtocollatoreId(protocollo.getCaricaProtocollatoreId());
		form.setTipoDocumentoId(protocollo.getTipoDocumentoId());
		form.setTitolareProcedimento(isTitolareProcedimento(utente,protocolloIngresso));

	}

	private boolean isTitolareProcedimento(Utente utente,
			ProtocolloIngresso protocollo) {
		boolean titolareProcedimento=false;
		Collection<AssegnatarioVO> assegnatari=protocollo.getAssegnatari();
		for(AssegnatarioVO ass:assegnatari){
			if(ass.getUfficioAssegnatarioId()==utente.getUfficioInUso()){
				if(ass.getCaricaAssegnatarioId()==utente.getCaricaInUso() || ass.getCaricaAssegnatarioId()==0){
					if(ass.isTitolareProcedimento())
						titolareProcedimento=true;
				}
			}
		}
		return titolareProcedimento;
	}
}
