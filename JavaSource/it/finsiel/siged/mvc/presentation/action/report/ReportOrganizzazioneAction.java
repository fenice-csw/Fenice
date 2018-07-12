package it.finsiel.siged.mvc.presentation.action.report;

import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.model.organizzazione.AreaOrganizzativa;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.presentation.actionform.report.ReportOrganizzazioneForm;
import it.finsiel.siged.mvc.presentation.helper.ReportType;
import it.finsiel.siged.mvc.presentation.helper.StatisticheView;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.report.protocollo.CommonReportDS;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

public final class ReportOrganizzazioneAction extends Action {

	static Logger logger = Logger.getLogger(ReportOrganizzazioneAction.class
			.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		MessageResources messages = getResources(request);
		HttpSession session = request.getSession();

		ReportOrganizzazioneForm reportForm = (ReportOrganizzazioneForm) form;
		session.setAttribute("reportForm", reportForm);
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		if (form == null) {
			logger.info(" Creating new ReportCommonForm Form");
			form = new ReportOrganizzazioneForm();
			request.setAttribute(mapping.getAttribute(), form);
		}

		if ("true".equals(request.getParameter("print"))) {
			stampaReport(request, response);
			return null;
		} else {
			reportForm.setBtnStampa(null);
			reportForm.setReportFormats(new HashMap<String, ReportType>(1));
			// carica report parameter
			HashMap<String,String> common = new HashMap<String,String>();
			common.put("ReportTitle", messages
					.getMessage("report.title.stampa_organizzazione"));
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
		ServletContext context = getServlet().getServletContext();
		HttpSession session = request.getSession();
		OutputStream os = response.getOutputStream();

		try {
			File reportFile = new File(context.getRealPath("/") + FileConstants.STAMPA_ORGANIZZAZIONE_REPORT_TEMPLATE);
			JasperDesign jasperDesign = JRXmlLoader.load(context.getRealPath("/") + FileConstants.STAMPA_ORGANIZZAZIONE_REPORT_TEMPLATE);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("ReportTitle", request.getParameter("ReportTitle"));
			parameters.put("ReportSubTitle", request
					.getParameter("ReportSubTitle"));
			parameters.put("BaseDir", reportFile.getParentFile());

			Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
			Organizzazione org = Organizzazione.getInstance();
			AreaOrganizzativa aoo = org.getAreaOrganizzativa(utente
					.getRegistroVOInUso().getAooId());
			Ufficio ufficioCorrente = aoo.getUfficioCentrale();
			Collection<Ufficio> uffici = new ArrayList<Ufficio>();
			uffici.add(ufficioCorrente);
			selezionaUffici(ufficioCorrente, uffici);
			Collection<StatisticheView> c = new ArrayList<StatisticheView>();
			for (Iterator<Ufficio> i = uffici.iterator(); i.hasNext();) {
				int ufficioId = i.next().getValueObject().getId()
						.intValue();
				Ufficio uff = org.getUfficio(ufficioId);
				StatisticheView stw = new StatisticheView();
				stw.setUfficio(uff.getPath());
				stw.setUtente(getCaricheUtenti(uff));
				c.add(stw);
			}
			CommonReportDS ds = new CommonReportDS(c, StatisticheView.class);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);

			String exportFormat = request.getParameter("ReportFormat");
			response.setHeader("Content-Disposition",
					"attachment;filename=Organigramma." + exportFormat);
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
				Map<String,Object> imagesMap = new HashMap<String,Object>();
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

	private String getCaricheUtenti(Ufficio uff) {
		List<UtenteVO> list = new ArrayList<UtenteVO>();
		int ufficioId = uff.getValueObject().getId().intValue();
		for (Iterator<Utente> i = uff.getUtenti().iterator(); i.hasNext();) {
			Utente ute = i.next();
			CaricaVO carica = CaricaDelegate.getInstance().getCaricaByUtenteAndUfficio(ute.getValueObject().getId(),ufficioId);
			
			if (carica.isAttivo()) {
				ute.getValueObject().setCarica(carica.getNome());
				
				list.add(ute.getValueObject());
			}
		}
		Comparator<Object> c = new Comparator<Object>() {
			public int compare(Object obj1, Object obj2) {
				UtenteVO ute1 = (UtenteVO) obj1;
				UtenteVO ute2 = (UtenteVO) obj2;
				return ute1.getCaricaFullName().compareToIgnoreCase(
						ute2.getCaricaFullName());
			}
		};
		Collections.sort(list, c);
		StringBuffer utenti = new StringBuffer();
		for (Iterator<UtenteVO> i = list.iterator(); i.hasNext();) {
			UtenteVO ute =i.next();
			utenti.append(ute.getCaricaFullName() + "\n");
		}

		return utenti.toString();
	}

	private void selezionaUffici(Ufficio uff, Collection<Ufficio> uffici) {
		try {
			for (Iterator<Ufficio> i = uff.getUfficiDipendenti().iterator(); i.hasNext();) {
				Ufficio u =  i.next();
				uffici.add(u);
				selezionaUffici(u, uffici);

			}
		} catch (Exception de) {
			logger
					.error("ReportOrganizzazioneAction: failed selezionaUffici: ");
		}
	}

}