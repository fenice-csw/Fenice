package it.finsiel.siged.mvc.presentation.action.report;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.AlberoUfficiBO;
import it.finsiel.siged.mvc.business.ReportProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.report.ReportAssegnatiForm;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.presentation.helper.ReportType;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.report.protocollo.CommonReportDS;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
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
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

public final class ReportAssegnatiAction extends Action {

	static Logger logger = Logger.getLogger(ReportAssegnatiAction.class
			.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		MessageResources messages = getResources(request);
		MessageResources bundle = (MessageResources) request
				.getAttribute(Globals.MESSAGES_KEY);
		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession();

		ReportAssegnatiForm reportForm = (ReportAssegnatiForm) form;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		boolean ufficioCompleto = (utente.getUfficioVOInUso().getTipo().equals(UfficioVO.UFFICIO_CENTRALE));

		session.setAttribute("reportForm", reportForm);
		if (form == null) {
			logger.info(" Creating new ReportScaricatiForm Form");
			form = new ReportAssegnatiForm();
			request.setAttribute(mapping.getAttribute(), form);
		}

		if (reportForm.getUfficioCorrenteId() == 0) {
			reportForm.setUfficioCorrenteId(utente.getUfficioInUso());
			AlberoUfficiBO.impostaUfficioUtenti(utente, reportForm,ufficioCompleto);
			reportForm.setSelezionato(AlberoUfficiBO.impostaSelected(reportForm));
		}

		if (request.getParameter("assegnaUfficioAction") != null) {
			reportForm.setUtenteSelezionatoId(0);
			reportForm.setSelezionato(AlberoUfficiBO.impostaSelected(reportForm));
		} else if (reportForm.getImpostaUfficioAction() != null) {
			reportForm.setImpostaUfficioAction(null);
			reportForm.setUfficioCorrenteId(reportForm
					.getUfficioSelezionatoId());
			AlberoUfficiBO.impostaUfficioUtenti(utente, reportForm,
					ufficioCompleto);
		} else if (request.getParameter("ufficioPrecedenteAction") != null) {
			AlberoUfficiBO.impostaUfficioUtenti(utente, reportForm,
					ufficioCompleto);
			reportForm.setUfficioCorrenteId(reportForm.getUfficioCorrente()
					.getParentId());
		} else if (request.getParameter("assegnaUtenteAction") != null) {
			reportForm.setSelezionato(AlberoUfficiBO
					.impostaSelected(reportForm));
		}

		else if (reportForm.getBtnStampa() != null) {
			reportForm.setBtnStampa(null);
			reportForm.setReportFormats(new HashMap<String,ReportType> (1));
			AlberoUfficiBO.impostaUfficio(utente, reportForm, ufficioCompleto);
			errors = reportForm.validate(mapping, request);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return (mapping.findForward("edit"));
			}
			int annoProt = 0;
			if (reportForm.getAnno()!=null && !reportForm.getAnno().trim().equals(""))
				annoProt = Integer.valueOf(reportForm.getAnno());
			int maxRighe = Integer.parseInt(bundle
					.getMessage("report.max.righe.lista"));
			int totalRecords = ReportProtocolloDelegate
					.getInstance()
					.countProtocolliAssegnati(utente,
							reportForm.getSelezionato().getUfficioId(),
							reportForm.getSelezionato().getCaricaId(), annoProt);
			if (totalRecords < 1) {
				ActionMessages msg = new ActionMessages();
				msg.add("recordNotFound", new ActionMessage("nessun_dato"));
				saveMessages(request, msg);
				return (mapping.findForward("input"));
			}
			if (totalRecords > maxRighe) {

				errors.add("controllo.maxrighe", new ActionMessage(
						"controllo.maxrighe", "" + totalRecords,
						"registro protocolli assegnati", "" + maxRighe));
			} else {
				HashMap<String, String>  common = new HashMap<String, String>();
				common
						.put(
								"ReportTitle",
								messages
										.getMessage("report.title.stampa_protocolli_assegnati"));
				common.put("ReportSubTitle", reportForm
						.getSelezionatoDescription());
				common.put("UfficioAssegnatario", String.valueOf(reportForm
						.getSelezionato().getUfficioId()));
				common.put("CaricaAssegnatario", String.valueOf(reportForm
						.getSelezionato().getCaricaId()));
				common.put("Anno", String.valueOf(annoProt));
				common.put("print", "true");
				reportForm.addReportType(ReportType.getIstanceByType(
						Parametri.FORMATO_HTML, common, messages));
				reportForm.addReportType(ReportType.getIstanceByType(
						Parametri.FORMATO_PDF, common, messages));
				reportForm.addReportType(ReportType.getIstanceByType(
						Parametri.FORMATO_XLS, common, messages));

			}
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			}

			return (mapping.findForward("input"));
		} else if ("true".equals(request.getParameter("print"))) {
			stampaReport(request, response);
			return null;
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return (mapping.findForward("input"));
	}

	public void stampaReport(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		ServletContext context = getServlet().getServletContext();
		HttpSession session = request.getSession();
		OutputStream os = response.getOutputStream();

		try {

			File reportFile = new File(context.getRealPath("/")
					+ FileConstants.STAMPA_PROTOCOLLI_ASSEGNATI_REPORT_TEMPLATE);
			JasperDesign jasperDesign = JRXmlLoader
					.load(context.getRealPath("/")
							+ FileConstants.STAMPA_PROTOCOLLI_ASSEGNATI_REPORT_TEMPLATE);
			JasperReport jasperReport = JasperCompileManager
					.compileReport(jasperDesign);
			Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
			int ufficioAssegnatarioId = Integer.parseInt(request
					.getParameter("UfficioAssegnatario"));
			int anno = Integer.parseInt(request.getParameter("Anno"));
			int caricaAssegnatarioId = Integer.parseInt(request
					.getParameter("CaricaAssegnatario"));
			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("ReportTitle", request.getParameter("ReportTitle"));
			parameters.put("BaseDir", reportFile.getParentFile());
			parameters.put("ReportSubTitle", request
					.getParameter("ReportSubTitle"));
			Collection<ReportProtocolloView> c = ReportProtocolloDelegate.getInstance()
					.stampaProtocolliAssegnati(utente, ufficioAssegnatarioId,
							caricaAssegnatarioId, anno);

			CommonReportDS ds = new CommonReportDS(c);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			String exportFormat = request.getParameter("ReportFormat");
			response.setHeader("Content-Disposition",
					"attachment;filename=protocolliAssegnati." + exportFormat);
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
				// default format is HTML
			} else if ("".equals(exportFormat) || "HTML".equals(exportFormat)) {
				Map<String,Object> imagesMap = new HashMap<String,Object>();
				// session.setAttribute("IMAGES_MAP", imagesMap);
				JRHtmlExporter exporter = new JRHtmlExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT,
						jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
				exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP,
						imagesMap);
				exporter.setParameter(
						JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,
						new Boolean(false));
				/*
				 * exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,
				 * "image.jsp?image=");
				 */
				exporter.exportReport();
			}
		} catch (Exception e) {
			logger.debug("Errore di compilazione", e);
			response.setContentType("text/plain");
			e.printStackTrace(new PrintStream(os));
		} finally {
			os.close();
		}

	}

}