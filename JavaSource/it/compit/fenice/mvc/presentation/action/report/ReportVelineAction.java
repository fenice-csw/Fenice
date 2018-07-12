package it.compit.fenice.mvc.presentation.action.report;

import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.AlberoUfficiBO;
import it.finsiel.siged.mvc.business.ReportProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.report.ReportRegistroForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.presentation.helper.ReportType;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.report.protocollo.CommonReportDS;
import it.finsiel.siged.util.DateUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintPage;
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

public class ReportVelineAction extends Action {

	static Logger logger = Logger.getLogger(ReportVelineAction.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		MessageResources messages = getResources(request);
		MessageResources bundle = (MessageResources) request
				.getAttribute(Globals.MESSAGES_KEY);
		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		boolean ufficioCompleto = (utente.getUfficioVOInUso().getTipo().equals(UfficioVO.UFFICIO_CENTRALE));
		ReportRegistroForm reportForm = (ReportRegistroForm) form;
		if (form == null) {
			logger.info(" Creating new ReportRegistroForm Form");
			form = new ReportRegistroForm();
		}
		reportForm.inizialize(true);
		if (reportForm.getUfficioCorrenteId() == 0) {
			reportForm.setUfficioCorrenteId(utente.getUfficioInUso());
			AlberoUfficiBO.impostaUfficioUtenti(utente, reportForm, ufficioCompleto);
		}
		if (request.getParameter("assegnaUfficioAction") != null) {
			reportForm.setUtenteSelezionatoId(0);
			reportForm.setSelezionato(AlberoUfficiBO
					.impostaSelected(reportForm));
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
		} else if (reportForm.getBtnStampa() != null) {
			errors = reportForm.validate(mapping, request);
			reportForm.setBtnStampa(null);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			} else {
				int numDa = 0;
				if (reportForm.getNumInizio() != null
						&& !reportForm.getNumInizio().trim().equals(""))
					numDa = Integer.valueOf(reportForm.getNumInizio());
				int numA = 0;
				if (reportForm.getNumFine() != null
						&& !reportForm.getNumFine().trim().equals(""))
					numA = Integer.valueOf(reportForm.getNumFine());
				int maxRighe = Integer.parseInt(bundle
						.getMessage("report.max.righe.lista"));
				int totalRecords = ReportProtocolloDelegate.getInstance()
						.countStampaVeline(utente,
								DateUtil.toDate(reportForm.getDataInizio()),
								DateUtil.toDate(reportForm.getDataFine()),
								reportForm.getSelezionato().getUfficioId(),
								reportForm.getSelezionato().getCaricaId(),
								numDa, numA,reportForm.getTipoProtocollo(),reportForm.getFlagConoscenza());
				if (totalRecords < 1) {
					ActionMessages msg = new ActionMessages();
					msg.add("recordNotFound", new ActionMessage("nessun_dato"));
					reportForm.getReportFormatsCollection().clear();
					reportForm.setFlagConoscenza(false);
					saveMessages(request, msg);
				} else {
					if (totalRecords > maxRighe) {
						errors.add("controllo.maxrighe", new ActionMessage(
								"controllo.maxrighe", "" + totalRecords,
								"registro protocolli", "" + maxRighe));
					} else {
						HashMap<String,String> common = new HashMap<String,String>();
						common.put("DataInizio", reportForm.getDataInizio());
						common.put("DataFine", reportForm.getDataFine());
						common.put("ReportTitle", messages
								.getMessage("report.title.stampa_veline"));
						common.put("ReportSubTitle", utente.getAreaOrganizzativa().getDescription());
						common.put("Destinatario", reportForm.getSelezionatoDescription());
						common.put("TipoProtocollo", reportForm.getTipoProtocollo());
						common.put("UfficioId", String.valueOf(reportForm.getSelezionato().getUfficioId()));
						common.put("CaricaId", String.valueOf(reportForm.getSelezionato().getCaricaId()));
						common.put("numInizio", String.valueOf(numDa));
						common.put("numFine", String.valueOf(numA));
						common.put("tipoProtocollo", reportForm.getTipoProtocollo());
						common.put("print", "true");
						common.put("conoscenza", String.valueOf(reportForm.getFlagConoscenza()));
						reportForm.addReportType(ReportType.getIstanceByType(Parametri.FORMATO_HTML, common, messages));
						reportForm.addReportType(ReportType.getIstanceByType(
								Parametri.FORMATO_PDF, common, messages));
						reportForm.addReportType(ReportType.getIstanceByType(
								Parametri.FORMATO_XLS, common, messages));

					}
				}
			}
			reportForm.setFlagConoscenza(false);
			return (mapping.findForward("input"));
		} else if (request.getParameter("btnStampaTutti") != null) {
			errors = reportForm.validate(mapping, request);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			} else {
				int numDa = 0;
				if (reportForm.getNumInizio() != null
						&& !reportForm.getNumInizio().trim().equals(""))
					numDa = Integer.valueOf(reportForm.getNumInizio());
				int numA = 0;
				if (reportForm.getNumFine() != null
						&& !reportForm.getNumFine().trim().equals(""))
					numA = Integer.valueOf(reportForm.getNumFine());
				Collection<AssegnatarioView> assegnatari = ReportProtocolloDelegate.getInstance()
						.getAssegnatariVeline(utente,
								DateUtil.toDate(reportForm.getDataInizio()),
								DateUtil.toDate(reportForm.getDataFine()),
								numDa, numA,reportForm.getTipoProtocollo(),reportForm.getFlagConoscenza());
				if (assegnatari.size()==0) {
					ActionMessages msg = new ActionMessages();
					msg.add("recordNotFound", new ActionMessage("nessun_dato"));
					reportForm.getReportFormatsCollection().clear();
					saveMessages(request, msg);
				} else {		
						stampaTutte(reportForm, response, assegnatari, messages, utente);
						reportForm.setFlagConoscenza(false);
						return null;
				}
			}
			reportForm.setFlagConoscenza(false);
			return (mapping.findForward("input"));
		}
		else if ("true".equals(request.getParameter("print"))) {
			reportForm.setFlagConoscenza(false);
			stampaVeline(request, response);
			return null;
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return (mapping.findForward("input"));
	}

	public void stampaTutte(ReportRegistroForm reportForm,
			HttpServletResponse response,Collection<AssegnatarioView> assegnatari,MessageResources messages,Utente utente) throws IOException, ServletException {
		ServletContext context = this.getServlet().getServletContext();
		Organizzazione org=Organizzazione.getInstance();
		OutputStream os = response.getOutputStream();
		try {
			JasperDesign jasperDesign = JRXmlLoader.load(context.getRealPath("/")+ FileConstants.STAMPA_VELINE_REPORT_TEMPLATE);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			Map<String,Object> parameters = new HashMap<String,Object>();
			Date dataInizio = DateUtil.toDate(reportForm.getDataInizio());
			Date dataFine = DateUtil.toDate(reportForm.getDataFine());
			int numDa = 0;
			if (reportForm.getNumInizio() != null
					&& !reportForm.getNumInizio().trim().equals(""))
				numDa = Integer.valueOf(reportForm.getNumInizio());
			int numA = 0;
			if (reportForm.getNumFine() != null
					&& !reportForm.getNumFine().trim().equals(""))
				numA = Integer.valueOf(reportForm.getNumFine());
			parameters.put("ReportTitle", messages.getMessage("report.title.stampa_veline"));
			parameters.put("ReportSubTitle", utente.getAreaOrganizzativa().getDescription());
			parameters.put("DataInizio", DateUtil.formattaData(dataInizio.getTime()));
			parameters.put("DataFine", DateUtil.formattaData(dataFine.getTime()));
			String tipoProtocollo = reportForm.getTipoProtocollo();
			int conoscenza=reportForm.getFlagConoscenza();
			JasperPrint jasperPrintPrincipale=null;
			for(AssegnatarioView ass:assegnatari){
				if (ass.getCaricaId() != 0) {
					String nomeUtente="";
					int caricaId = ass.getCaricaId();
					CaricaVO carica = org.getCarica(caricaId);
					Utente ute = org.getUtente(carica.getUtenteId());
					if (ute != null) {
						if (carica.isAttivo())
							nomeUtente=ute.getValueObject().getCaricaFullName();
						else
							nomeUtente=ute.getValueObject().getCaricaFullNameNonAttivo();
					} else
						nomeUtente=carica.getNome();
					parameters.put("Destinatario",org.getUfficio(ass.getUfficioId()).getPath()+""+nomeUtente);
				} else{
					parameters.put("Destinatario",org.getUfficio(ass.getUfficioId()).getPath());
				}
				Collection<ReportProtocolloView> c = ReportProtocolloDelegate.getInstance().stampaVeline(
						utente, dataInizio, dataFine, ass.getUfficioId(), ass.getCaricaId(), numDa,
						numA,tipoProtocollo,conoscenza);
				CommonReportDS ds = new CommonReportDS(c);
				JasperPrint jasperPrint = JasperFillManager.fillReport(
						jasperReport, parameters, ds);
				if(jasperPrintPrincipale==null)
					jasperPrintPrincipale=jasperPrint;
				else{
					for (Object obj: jasperPrint .getPages()) {
		            JRPrintPage pag = (JRPrintPage)obj;
		            jasperPrintPrincipale.addPage(pag);   
		    }
				}
				
			}
			response.setHeader("Content-Disposition",
					"attachment;filename=Veline.PDF");
			response.setHeader("Cache-control", "");
			//if ("PDF".equals(exportFormat)) {
				response.setContentType("application/pdf");
				JRPdfExporter exporter = new JRPdfExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT,
						jasperPrintPrincipale);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
				exporter.exportReport();
			//} 		
			} catch (Exception e) {
			logger.error("", e);
			response.setContentType("text/plain");
			e.printStackTrace(new PrintStream(os));
		} finally {
			os.close();
		}

	}
	
	public void stampaVeline(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		ServletContext context = this.getServlet().getServletContext();
		HttpSession session = request.getSession();
		OutputStream os = response.getOutputStream();
		try {
			JasperDesign jasperDesign = JRXmlLoader.load(context.getRealPath("/")+ FileConstants.STAMPA_VELINE_REPORT_TEMPLATE);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			Map<String,Object> parameters = new HashMap<String,Object>();
			Date dataInizio = DateUtil.toDate(request
					.getParameter("DataInizio"));
			Date dataFine = DateUtil.toDate(request.getParameter("DataFine"));
			int numDa = Integer.parseInt(request.getParameter("numInizio"));
			int numA = Integer.parseInt(request.getParameter("numFine"));
			String tipo = request.getParameter("tipoProtocollo");
			int ufficioId = Integer.parseInt(request.getParameter("UfficioId"));
			int caricaId = Integer.parseInt(request.getParameter("CaricaId"));
			int conoscenza = Integer.parseInt(request.getParameter("conoscenza"));
			parameters.put("ReportTitle", request.getParameter("ReportTitle"));
			parameters.put("ReportSubTitle", request
					.getParameter("ReportSubTitle"));
			parameters.put("DataInizio", DateUtil.formattaData(dataInizio
					.getTime()));
			parameters.put("DataFine", DateUtil
					.formattaData(dataFine.getTime()));
			parameters.put("Destinatario", (String) request
					.getParameter("Destinatario"));
			Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
			Collection<ReportProtocolloView> c = ReportProtocolloDelegate.getInstance().stampaVeline(
					utente, dataInizio, dataFine, ufficioId, caricaId, numDa,
					numA,tipo,conoscenza);
			CommonReportDS ds = new CommonReportDS(c);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			String exportFormat = request.getParameter("ReportFormat");
			response.setHeader("Content-Disposition",
					"attachment;filename=Veline." + exportFormat);
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
