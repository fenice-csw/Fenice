package it.finsiel.siged.mvc.presentation.action.amministrazione;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.AlberoUfficiBO;
import it.finsiel.siged.mvc.business.ModificheProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.RegistroModificheForm;
import it.finsiel.siged.mvc.presentation.helper.ReportType;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.protocollo.ModificaProtocolloVO;
import it.finsiel.siged.util.DateUtil;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

public class RegistroModificheAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        HttpSession session = request.getSession();
        MessageResources messages = getResources(request);
        RegistroModificheForm reportForm = (RegistroModificheForm) form;
        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
        boolean ufficioCompleto = (utente.getUfficioVOInUso().getTipo()
                .equals(UfficioVO.UFFICIO_CENTRALE));
        ActionMessages errors = new ActionMessages();

        session.setAttribute("reportForm", reportForm);

        if (reportForm.getUfficioCorrenteId() == 0) {
            AlberoUfficiBO.impostaUfficio(utente, reportForm, ufficioCompleto);
        }

        if (request.getParameter("selezionaUfficio") != null) {
            reportForm.setUfficioCorrenteId(Integer.parseInt(request
                    .getParameter("selezionaUfficio")));

        } else if (reportForm.getImpostaUfficioAction() != null) {
            reportForm.setImpostaUfficioAction(null);
            reportForm.setUfficioCorrenteId(reportForm
                    .getUfficioSelezionatoId());

        } else if (request.getParameter("ufficioPrecedenteAction") != null) {
            AlberoUfficiBO.impostaUfficio(utente, reportForm, ufficioCompleto);
            reportForm.setUfficioCorrenteId(reportForm.getUfficioCorrente()
                    .getParentId());
        }
            /*
         else if ("true".equals(request.getParameter("print"))) {

            stampaReport(request, response);
            return null;

        }*/ 
        else if (reportForm.getBtnVisualizza() != null) {
            ModificheProtocolloDelegate delegate = ModificheProtocolloDelegate
                    .getInstance();
            long da = DateUtil.toDate(reportForm.getDataInizio()).getTime();
            long a = DateUtil.toDate(reportForm.getDataFine()).getTime();

            Collection<ModificaProtocolloVO> c  = delegate.getModificheProtocollo(utente
                    .getRegistroInUso(), reportForm.getUfficioCorrenteId(), da,
                    a);
            MessageResources bundle = (MessageResources) request
                    .getAttribute(Globals.MESSAGES_KEY);
            int maxRighe = Integer.parseInt(bundle
                    .getMessage("amministrazione.max.righe.lista"));
            int contaRighe = 0;
            if(c!=null && c.size()>0)
            contaRighe=c.size();
            if (contaRighe <= maxRighe) {
                request.setAttribute("REPORT_MODIFICHE",c );
                AlberoUfficiBO.impostaUfficio(utente, reportForm,
                        ufficioCompleto);
                return mapping.getInputForward();

            } else {
                errors.add("controllo.maxrighe", new ActionMessage(
                        "controllo.maxrighe", "" + contaRighe,
                        " registro modifiche documenti ", "" + maxRighe));
                AlberoUfficiBO.impostaUfficio(utente, reportForm,
                        ufficioCompleto);
                // return mapping.getInputForward();
            }

        } else if (request.getParameter("btnStampa") != null) {
            long da = DateUtil.toDate(reportForm.getDataInizio()).getTime();
            long a = DateUtil.toDate(reportForm.getDataFine()).getTime();
            ModificheProtocolloDelegate delegate = ModificheProtocolloDelegate
                    .getInstance();
            Collection<ModificaProtocolloVO> report = delegate.getModificheProtocollo(utente
                    .getRegistroInUso(), reportForm.getUfficioCorrenteId(), da,
                    a);
            session.setAttribute("REPORT_MODIFICHE", report);
            reportForm.setReportFormats(new HashMap<String, ReportType>(1));
            // carica report parameter
            HashMap<String, String> common = new HashMap<String, String>();
            common.put("DataInizio", reportForm.getDataInizio());
            common.put("DataFine", reportForm.getDataFine());
            common.put("ReportTitle", messages
                    .getMessage("report.title.stampa_protocolli_statistiche"));
            common.put("ReportSubTitle", Organizzazione.getInstance()
                    .getUfficio(reportForm.getUfficioCorrenteId())
                    .getValueObject().getDescription());
            common.put("Ufficio", String.valueOf(reportForm
                    .getUfficioCorrenteId()));
            common.put("print", "true");

            ReportType html = new ReportType();
            html.setDescReport(messages.getMessage("report.type.descHTML"));
            html.setTipoReport(Parametri.FORMATO_HTML);
            HashMap<String, String> parsHtml = new HashMap<String, String>(common);
            parsHtml.put("ReportFormat", Parametri.FORMATO_HTML);
            html.setParameters(parsHtml);
            reportForm.addReportType(html);

            ReportType pdf = new ReportType();
            pdf.setDescReport(messages.getMessage("report.type.descPDF"));
            pdf.setTipoReport(Parametri.FORMATO_PDF);
            HashMap<String, String> parsPdf = new HashMap<String, String>(common);
            parsPdf.put("ReportFormat", Parametri.FORMATO_PDF);
            pdf.setParameters(parsPdf);
            reportForm.addReportType(pdf);

            ReportType xls = new ReportType();
            xls.setDescReport(messages.getMessage("report.type.descXLS"));
            xls.setTipoReport(Parametri.FORMATO_XLS);
            HashMap<String, String> parsXls = new HashMap<String, String>(common);
            parsXls.put("ReportFormat", Parametri.FORMATO_XLS);
            xls.setParameters(parsXls);
            reportForm.addReportType(xls);

            return mapping.findForward("stampaReport");
        }

        AlberoUfficiBO.impostaUfficio(utente, reportForm, ufficioCompleto);
        request.removeAttribute("REPORT_MODIFICHE");
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        return mapping.getInputForward();
    }
    /*
    public void stampaReport(HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
        ServletContext context = getServlet().getServletContext();
        HttpSession session = request.getSession();
        OutputStream os = response.getOutputStream();

        try {

            File reportFile = new File(context.getRealPath("/")
                    + FileConstants.STAMPA_REGISTRO_MODIFICHE_REPORT_COMPILED);
            // Compile Report only if we are in debug mode
            if (logger.isDebugEnabled()) {
                compile(context);
            }

            JasperReport jasperReport = (JasperReport) JRLoader
                    .loadObject(reportFile.getPath());

            Map parameters = new HashMap();
            // String Ufficio = request.getParameter("Ufficio");
            // int UfficioId = Integer.parseInt(Ufficio);

            // int protocolloId =
            // Integer.parseInt(session.getAttribute("protocolloId").toString());
            parameters
                    .put("ReportTitle", "Report Registro Modifiche Documenti");
            parameters.put("ReportSubTitle", request
                    .getParameter("ReportSubTitle"));
            parameters.put("BaseDir", reportFile.getParentFile());

            Collection c = new ArrayList((Collection) session
                    .getAttribute("REPORT_MODIFICHE"));
            CommonReportDS ds = new CommonReportDS(c,
                    ModificaProtocolloVO.class);

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport, parameters, ds);

            String exportFormat = request.getParameter("ReportFormat");
            if ("PDF".equals(exportFormat)) {
                response.setContentType("application/pdf");
                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT,
                        jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
                exporter.exportReport();
            } else if ("XLS".equals(exportFormat)) {
                response.setContentType("application/vnd.ms-excel");
                JRXlsExporter exporter = new JRXlsExporter();
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
                Map imagesMap = new HashMap();
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
*/
    public void compile(ServletContext context) throws JRException {
        System.setProperty("jasper.reports.compile.class.path", context
                .getRealPath("/WEB-INF/lib/jasperreports-0.6.6.jar")
                + System.getProperty("path.separator")
                + context.getRealPath("/WEB-INF/classes/"));

        System.setProperty("jasper.reports.compile.temp", context
                .getRealPath("/")
                + "/WEB-INF/reports/");
        JasperCompileManager.compileReportToFile(context.getRealPath("/")
                + FileConstants.STAMPA_REGISTRO_MODIFICHE_REPORT_TEMPLATE);

    }

    // private Collection etichette(int protocolloId) throws Exception {
    // Organizzazione org = Organizzazione.getInstance();
    // Collection etichette = new ArrayList();
    //
    //        
    // try {
    // for (Iterator i = getDestinatari(protocolloId).iterator(); i.hasNext();)
    // {
    // DestinatarioVO dest = (DestinatarioVO) i.next();
    // DestinatarioView st = new DestinatarioView();
    // TitoliDestinatarioDelegate td = TitoliDestinatarioDelegate.getInstance();
    // int titoloId =dest.getTitoloId();
    // String titoloDestinatario =
    // td.getTitoloDestinatario(titoloId).getDescription();
    // if (titoloDestinatario==null) {
    // titoloDestinatario="";
    // }
    // st.setTitoloDestinatario(titoloDestinatario);
    // st.setTitoloId(dest.getTitoloId());
    // st.setDestinatario(dest.getDestinatario());
    // st.setIndirizzo(dest.getIndirizzo());
    // st.setCitta(dest.getCitta());
    // etichette.add(st);
    // }
    // } catch (Exception e) {
    // logger.error("", e);
    // }
    // return etichette;
    // }

}
