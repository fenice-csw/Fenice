package it.finsiel.siged.mvc.presentation.action.report;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.ReportProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.report.ReportAnagraficaForm;
import it.finsiel.siged.mvc.presentation.helper.ReportType;
import it.finsiel.siged.mvc.presentation.helper.RubricaView;
import it.finsiel.siged.report.protocollo.CommonReportDS;
import it.flosslab.report.utility.ReportHelper;

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

import net.sf.jasperreports.engine.JRException;
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

public final class ReportAnagraficaAction extends Action {

    static Logger logger = Logger.getLogger(ReportAnagraficaAction.class
            .getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        MessageResources messages = getResources(request);
        ActionMessages errors = new ActionMessages();
        HttpSession session = request.getSession();

        ReportAnagraficaForm reportForm = (ReportAnagraficaForm) form;

        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
        session.setAttribute("reportForm", reportForm);
        if (request.getParameter("btnStampa") != null) {
            reportForm.setReportFormats(new HashMap<String,ReportType> (1));
            int totalRecords = ReportProtocolloDelegate.getInstance()
                    .countRubrica(utente.getRegistroVOInUso().getAooId(),
                            reportForm.getFlagTipo());
            if (totalRecords < 1) {
                ActionMessages msg = new ActionMessages();
                msg.add("recordNotFound", new ActionMessage("nessun_dato"));
                saveMessages(request, msg);
                return (mapping.findForward("input"));
            } else {
                // carica report parameter
                HashMap<String,String> common = new HashMap<String,String>();
                common.put("ReportTitle", messages.getMessage("report.title.rubrica"));
                common.put("FlagTipo", reportForm.getFlagTipo());
                if (reportForm.getFlagTipo().equals("F")) {
                    common.put("ReportSubTitle", "Persone fisiche");
                } else {
                    common.put("ReportSubTitle", "Persone giuridiche");
                }

                common.put("print", "true");

                reportForm.addReportType(ReportType.getIstanceByType(Parametri.FORMATO_HTML, common, messages));
                reportForm.addReportType(ReportType.getIstanceByType(Parametri.FORMATO_PDF, common, messages));
                reportForm.addReportType(ReportType.getIstanceByType(Parametri.FORMATO_XLS, common, messages));


                // remove form from memory
                // session.removeAttribute(mapping.getAttribute());
                // forward to page for print choices

                return (mapping.findForward("input"));
            }
        } else if ("true".equals(request.getParameter("print"))) {
            stampaReport(request, response);
            return null;
        } else {
            reportForm = new ReportAnagraficaForm();
            session.setAttribute(mapping.getAttribute(), reportForm);
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
        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
        try {
            File reportFile;
        	JasperDesign jasperDesign;
			
        	if ("F".equals(request.getParameter("FlagTipo"))) {
                reportFile = new File(context.getRealPath("/")
                        + FileConstants.STAMPA_PERSONE_FISICHE_REPORT_TEMPLATE);
                //jasperDesign = JRXmlLoader.load(new LegacyJasperInputStream(new FileInputStream(context.getRealPath("/") + FileConstants.STAMPA_PERSONE_FISICHE_REPORT_TEMPLATE)));
    			jasperDesign = JRXmlLoader.load(context.getRealPath("/") + FileConstants.STAMPA_PERSONE_FISICHE_REPORT_TEMPLATE);

            } else {
                reportFile = new File(
                        context.getRealPath("/")
                                + FileConstants.STAMPA_PERSONE_GIURIDICHE_REPORT_TEMPLATE);
                //jasperDesign = JRXmlLoader.load(new LegacyJasperInputStream(new FileInputStream(context.getRealPath("/") + FileConstants.STAMPA_PERSONE_GIURIDICHE_REPORT_TEMPLATE)));
    			jasperDesign = JRXmlLoader.load(context.getRealPath("/") + FileConstants.STAMPA_PERSONE_GIURIDICHE_REPORT_TEMPLATE);

            }

        	JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            //JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportFile.getPath());
            Map<String,Object> parameters = new HashMap<String,Object>();
            parameters.put("FlagTipo", request.getParameter("ReportTitle"));
            parameters.put("ReportTitle", request.getParameter("ReportTitle"));
            parameters.put("BaseDir", reportFile.getParentFile());
            parameters.put("ReportSubTitle", request
                    .getParameter("ReportSubTitle"));

            Collection<RubricaView> c = ReportProtocolloDelegate.getInstance()
                    .stampaRubrica(utente.getRegistroVOInUso().getAooId(),
                            request.getParameter("FlagTipo"));
            CommonReportDS ds = new CommonReportDS(c, RubricaView.class);
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport, parameters, ds);

            String exportFormat = request.getParameter("ReportFormat");
            response.setHeader("Content-Disposition",
                    "attachment;filename=anagrafica." + exportFormat);
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
                Map<String, Object> imagesMap = new HashMap<String, Object>();
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

    /*
     * Compila il report template leggendolo dal file system
     */
    public void compile(ServletContext context, String flagTipo)
            throws JRException {
          if (flagTipo.equals("F")) {
            ReportHelper.compile(context, FileConstants.STAMPA_PERSONE_FISICHE_REPORT_TEMPLATE);
                   
        } else {
        	ReportHelper.compile(context, FileConstants.STAMPA_PERSONE_GIURIDICHE_REPORT_TEMPLATE);         
        }

    }

}