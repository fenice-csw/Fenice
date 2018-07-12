package it.compit.fenice.mvc.presentation.action.report;

import it.compit.fenice.mvc.presentation.actionform.report.ReportGenericForm;
import it.compit.fenice.mvc.presentation.helper.TitolarioView;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.TitolarioDelegate;
import it.finsiel.siged.mvc.presentation.helper.ReportType;
import it.finsiel.siged.report.protocollo.CommonReportDS;

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
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

public final class ReportTitolarioAction extends Action {

	static Logger logger = Logger.getLogger(ReportTitolarioAction.class
			.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		MessageResources messages = getResources(request);
		HttpSession session = request.getSession();

		ReportGenericForm reportForm = (ReportGenericForm) form;
		session.setAttribute("reportForm", reportForm);
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		if (form == null) {
			logger.info(" Creating new ReportTitolarioForm Form");
			form = new ReportGenericForm();
			request.setAttribute(mapping.getAttribute(), form);
		}

		if ("true".equals(request.getParameter("print"))) {
			stampaReport(request, response);
			return null;
		} else {
			reportForm.setBtnStampa(null);
			reportForm.setReportFormats(new HashMap<String,ReportType>(1));
			HashMap<String,String> common = new HashMap<String,String>();
			common.put("ReportTitle", messages.getMessage("report.title.stampa_titolario"));
			Organizzazione org = Organizzazione.getInstance();
			int aooId = utente.getRegistroVOInUso().getAooId();
			String areaOrganizzativa = org.getAreaOrganizzativa(aooId)
					.getValueObject().getDescription();
			common.put("ReportSubTitle", areaOrganizzativa);
			common.put("print", "true");
			reportForm.addReportType(ReportType.getIstanceByType(
					Parametri.FORMATO_HTML, common, messages));
			reportForm.addReportType(ReportType.getIstanceByType(
					Parametri.FORMATO_PDF, common, messages));
			reportForm.addReportType(ReportType.getIstanceByType(
					Parametri.FORMATO_XLS, common, messages));

		}
		return (mapping.findForward("input"));
	}

	public void stampaReport(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		ServletContext context = this.getServlet().getServletContext();
		HttpSession session = request.getSession();
		OutputStream os = response.getOutputStream();
		try {
			//File reportFile = new File(context.getRealPath("/")+ FileConstants.STAMPA_TITOLARIO_REPORT_TEMPLATE);
			JasperDesign jasperDesign = JRXmlLoader.load(context.getRealPath("/")+ FileConstants.STAMPA_TITOLARIO_REPORT_TEMPLATE);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("ReportTitle", request.getParameter("ReportTitle"));
			parameters.put("ReportSubTitle", request.getParameter("ReportSubTitle"));
			//parameters.put("BaseDir", reportFile.getParent());
			Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
			Collection<TitolarioView> c = TitolarioDelegate.getInstance().stampaTitolario(utente.getRegistroVOInUso().getAooId());
			CommonReportDS ds = new CommonReportDS(c, TitolarioView.class);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			String exportFormat = request.getParameter("ReportFormat");
			response.setHeader("Content-Disposition",
					"attachment;filename=Titolario." + exportFormat);
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
				//Map imagesMap = new HashMap();
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
}