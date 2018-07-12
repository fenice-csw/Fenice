package it.flosslab.report.utility;

import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloIngressoForm;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.struts.action.ActionForm;

/**
 * @author roberto onnis
 * 
 */
public class ReportHelper {

	public static final String STAMPA_RICEVUTA_PROTOCOLLO_COMPILED = "/WEB-INF/reports/StampaRicevuta.jasper";
	public static final String STAMPA_RICEVUTA_PROTOCOLLO_TEMPLATE = "/WEB-INF/reports/StampaRicevuta.jrxml";
	private static final String LIBRERIA_JASPER_REPORT = "jasperreports-3.0.0.jar";

	public static void reportToOutputStream(ActionForm form,
			HttpServletResponse response, OutputStream os, File reportFile,String path)
			throws JRException {
		JasperPrint jasperPrint = getJasperPrint(form, reportFile,path);
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition","attachment;filename=ricevuta_protocollo.pdf");
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
		exporter.exportReport();
		
	}

	/**
	 * @param form
	 * @param reportFile
	 * @return
	 * @throws JRException
	 */
	public static JasperPrint getJasperPrint(ActionForm form, File reportFile,String path)
			throws JRException {
		try {
			JasperDesign jasperDesign = JRXmlLoader.load(path);
			JasperReport jasperReport = JasperCompileManager
			.compileReport(jasperDesign);
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			ReportGenerator reportGenerator = new ReportGenerator(
					(ProtocolloIngressoForm) form);
			JRDataSource ds = new JREmptyDataSource();
			parameters = reportGenerator.getParameters();
			JasperPrint jasperPrint=JasperFillManager.fillReport(jasperReport, parameters, ds);
			return jasperPrint;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public static void compile(ServletContext context, String file)
			throws JRException {
		System.setProperty("jasper.reports.compile.class.path", context
				.getRealPath("/WEB-INF/lib/" + LIBRERIA_JASPER_REPORT)
				+ System.getProperty("path.separator")
				+ context.getRealPath("/WEB-INF/classes/"));

		System.setProperty("jasper.reports.compile.temp", context
				.getRealPath("/")
				+ "/WEB-INF/reports/");
		JasperCompileManager.compileReportToFile(context.getRealPath("/")
				+ file);

	}
}
