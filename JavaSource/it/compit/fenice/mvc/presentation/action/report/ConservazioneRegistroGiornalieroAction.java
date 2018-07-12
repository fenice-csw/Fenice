package it.compit.fenice.mvc.presentation.action.report;

import it.compit.fenice.enums.TagConservazioneEnum;
import it.compit.fenice.mvc.bo.ConservazioneBO;
import it.compit.fenice.mvc.presentation.helper.ResponseView;
import it.compit.fenice.restful.GestioneArchiviClient;
import it.compit.fenice.restful.GestioneArchiviData;
import it.compit.fenice.util.ZipUtil;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.AlberoUfficiBO;
import it.finsiel.siged.mvc.business.ReportProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.report.ReportRegistroForm;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.report.protocollo.CommonReportDS;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.fill.JRTemplatePrintText;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

public final class ConservazioneRegistroGiornalieroAction extends Action {

	static Logger logger = Logger.getLogger(ConservazioneRegistroGiornalieroAction.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		MessageResources resources = getResources(request);
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		boolean ufficioCompleto = (utente.getUfficioVOInUso().getTipo().equals(UfficioVO.UFFICIO_CENTRALE));
		ReportRegistroForm reportForm = (ReportRegistroForm) form;
		if (form == null) {
			logger.info(" Creating new ReportRegistroForm Form");
			form = new ReportRegistroForm();
		}
		session.setAttribute("reportForm", reportForm);
		if (reportForm.getUfficioCorrenteId() == 0) {
			reportForm.setUfficioCorrenteId(utente.getUfficioInUso());
			reportForm.setUtenteSelezionatoId(0);
			AlberoUfficiBO.impostaUfficioUtenti(utente, reportForm,
					ufficioCompleto);
			reportForm.setSelezionato(AlberoUfficiBO
					.impostaSelected(reportForm));
			reportForm.setDataInizio(DateUtil.getYesterdayDateString());
			reportForm.setTipoProtocollo("");
		}
		if (reportForm.getBtnStampa() != null) {
			reportForm.setBtnStampa(null);
			errors = reportForm.validateConservazioneRegistroGiornaliero(
					mapping, request);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			} else {
				reportForm.setTotalReg(ReportProtocolloDelegate.getInstance()
						.countStampaRegistroReport(utente,
								reportForm.getTipoProtocollo(),
								DateUtil.toDate(reportForm.getDataInizio()),
								DateUtil.toDate(reportForm.getDataInizio()), 0));

				reportForm.setTotalMod( ReportProtocolloDelegate.getInstance()
						.countProtocolliModificatiReport(utente,
								reportForm.getTipoProtocollo(),
								DateUtil.toDate(reportForm.getDataInizio()),
								DateUtil.toDate(reportForm.getDataInizio()), 0));
				if (reportForm.getTotalReg() < 1) {
					reportForm.setShowReportDownload(false);
					reportForm.setRecordNotFound(true);
				} else {
					reportForm.setShowReportDownload(true);
					reportForm.setGaAbilitata(utente.getAreaOrganizzativa().isGaAbilitata() && !utente.getAreaOrganizzativa().isGaFlagInvio());
					reportForm.setRecordNotFound(false);

				}
			}
		}
		if (request.getParameter("btnInviaGestioneArchivi") != null) {
			File directory=FileUtil.createTempDir();
			Date dataReg = DateUtil.toDate(reportForm.getDataInizio());
			
			byte[] zipFile= getReportAndZip(request, resources, reportForm, utente,directory);
			byte[] csvFile= FileUtil.leggiFileAsBytes(ConservazioneBO.getFileIndice(directory).getAbsolutePath());
			
			GestioneArchiviClient client=new GestioneArchiviClient(utente.getAreaOrganizzativa().getGaUsername(), utente.getAreaOrganizzativa().getGaPwd());
			GestioneArchiviData d=new GestioneArchiviData("Versamento registro di protocollo del "+reportForm.getDataInizio(), ConservazioneBO.getFileIndice(directory).getName(), "Registro"+DateUtil.getDataForIndex(dataReg.getTime())+".zip", csvFile, zipFile, "11");
			ResponseView view=client.uploadDocument(d);
			if(view.getStatusCode()!=200 && view.getStatusCode()!=204){
				errors.add("d", new ActionMessage("conservazione.registro.upload.error",view.getStatusCode(),view.getMessage()));
			} else{
				messages.add("d", new ActionMessage("conservazione.registro.upload.success"));
				saveMessages(request, messages);
			}
		}
		
		if (request.getParameter("btnDownload") != null) {
			stampaReport(request, response, resources, reportForm, utente);
			return null;
		}
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return (mapping.findForward("input"));
	}

	public byte[] getReportAndZip(HttpServletRequest request,
			MessageResources resources, ReportRegistroForm form, Utente utente, File directory)
			throws IOException, ServletException {
		ServletContext context = request.getSession().getServletContext();
		JasperPrint jasperPrint=null;
		JasperPrint jasperPrintMod=null;
		TreeMap<TagConservazioneEnum,String> metadati=new TreeMap<TagConservazioneEnum, String>();
		List<String> fileNames=new ArrayList<String>();
		try {
			File reportFile = new File(
					context.getRealPath("/")
							+ FileConstants.STAMPA_CONSERVAZIONE_REGISTRO_GIORNALIERO_TEMPLATE);
			File reportFileMod = new File(
					context.getRealPath("/")
							+ FileConstants.STAMPA_CONSERVAZIONE_REGISTRO_MODIFICHE_GIORNALIERO_TEMPLATE);

			Map<String, Object> parameters = new HashMap<String, Object>();
			Date dataReg = DateUtil.toDate(form.getDataInizio());
			parameters.put("ReportTitle", utente.getAreaOrganizzativa().getDescription());
			parameters.put("DataReg", DateUtil.formattaData(dataReg.getTime()));
			String tipoProtocollo = form.getTipoProtocollo();
			metadati=ConservazioneBO.getCommonMetadati(utente, Organizzazione.getInstance(), dataReg,dataReg);
			File indice=null;
			if(form.getTotalReg()!=0){
				parameters.put("BaseDir", reportFile.getParentFile());
				parameters.put("ReportSubTitle", resources.getMessage("report.title.stampa_registro_giornaliero"));
				JasperDesign jasperDesign = JRXmlLoader
					.load(context.getRealPath("/")
							+ FileConstants.STAMPA_CONSERVAZIONE_REGISTRO_GIORNALIERO_TEMPLATE);
				JasperReport jasperReport = JasperCompileManager
					.compileReport(jasperDesign);

				Collection<ReportProtocolloView> c = ReportProtocolloDelegate
						.getInstance().stampaRegistro(utente, tipoProtocollo,
								dataReg, dataReg, 0);
								CommonReportDS ds = new CommonReportDS(c);
				jasperPrint = JasperFillManager.fillReport(
						jasperReport, parameters, ds);
				long uniqueNumberReg=System.currentTimeMillis();
				File fileReg=new File(directory, "RegistroGiornaliero"+metadati.get(TagConservazioneEnum.CODICEENTE)+uniqueNumberReg+".pdf");
				JasperExportManager.exportReportToPdfFile(jasperPrint, fileReg.getAbsolutePath());
				ConservazioneBO.getCompleteRegistroGiornalieroMetadati(metadati, uniqueNumberReg, "REGISTRO GIORNALIERO", fileReg, "Registro giornaliero di protocollo", c,c.size(),0);
				indice=new File(directory, metadati.get(TagConservazioneEnum.NUMERO)+".csv");
				ConservazioneBO.writeCSV(metadati,indice, true);
				fileNames.add(fileReg.getName());
				
			}
			
			if(form.getTotalMod()!=0){
				parameters.put("BaseDir", reportFileMod.getParentFile());
				parameters.put("ReportSubTitle",resources.getMessage("report.title.stampa_registro_modifiche_giornaliero"));
				JasperDesign jasperDesignMod = JRXmlLoader
					.load(context.getRealPath("/")
							+ FileConstants.STAMPA_CONSERVAZIONE_REGISTRO_MODIFICHE_GIORNALIERO_TEMPLATE);
				JasperReport jasperReportMod = JasperCompileManager
					.compileReport(jasperDesignMod);
				
				Collection<ReportProtocolloView> cMod = ReportProtocolloDelegate
						.getInstance().getProtocolliModificatiReport(utente,
								tipoProtocollo, dataReg, dataReg, 0);
				CommonReportDS ds = new CommonReportDS(cMod);
				jasperPrintMod = JasperFillManager.fillReport(
						jasperReportMod, parameters, ds);
				
				long uniqueNumberMod=System.currentTimeMillis();
				File filemod=new File(directory, "RegistroModifiche"+metadati.get(TagConservazioneEnum.CODICEENTE)+uniqueNumberMod+".pdf");
				JasperExportManager.exportReportToPdfFile(jasperPrintMod, filemod.getAbsolutePath());
				
				int totalAnn=ReportProtocolloDelegate.getInstance().countProtocolliAnnullatiReport(utente,
						form.getTipoProtocollo(),
						DateUtil.toDate(form.getDataInizio()),
						DateUtil.toDate(form.getDataInizio()), 0);
				ConservazioneBO.getCompleteRegistroGiornalieroMetadati(metadati, uniqueNumberMod, "REGISTRO MODIFICHE", filemod, "Registro delle modifiche", cMod,0,totalAnn);
				ConservazioneBO.writeCSV(metadati,indice, false);
				fileNames.add(filemod.getName());
			}
			form.setMetadati(metadati);
			return ZipUtil.zipFiles(directory, fileNames);
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

	public void stampaReport(HttpServletRequest request,
			HttpServletResponse response, MessageResources resources, ReportRegistroForm form, Utente utente)
			throws IOException, ServletException {
		ServletContext context = this.getServlet().getServletContext();
		OutputStream os = response.getOutputStream();
		JasperPrint jasperPrint=null;
		JasperPrint jasperPrintMod=null;
		File directory=FileUtil.createTempDir(); 
		List<String> fileNames=new ArrayList<String>();
		TreeMap<TagConservazioneEnum,String> metadati=new TreeMap<TagConservazioneEnum, String>();
		try {
			File reportFile = new File(
					context.getRealPath("/")
							+ FileConstants.STAMPA_CONSERVAZIONE_REGISTRO_GIORNALIERO_TEMPLATE);
			File reportFileMod = new File(
					context.getRealPath("/")
							+ FileConstants.STAMPA_CONSERVAZIONE_REGISTRO_MODIFICHE_GIORNALIERO_TEMPLATE);

			Map<String, Object> parameters = new HashMap<String, Object>();
			Date dataReg = DateUtil.toDate(form.getDataInizio());
			parameters.put("ReportTitle", utente.getAreaOrganizzativa().getDescription());
			parameters.put("DataReg", DateUtil.formattaData(dataReg.getTime()));
			String tipoProtocollo = form.getTipoProtocollo();
			metadati=ConservazioneBO.getCommonMetadati(utente, Organizzazione.getInstance(), dataReg,dataReg);
			File indice=null;
			if(form.getTotalReg()!=0){
				parameters.put("BaseDir", reportFile.getParentFile());
				parameters.put("ReportSubTitle", resources.getMessage("report.title.stampa_registro_giornaliero"));
				JasperDesign jasperDesign = JRXmlLoader
					.load(context.getRealPath("/")
							+ FileConstants.STAMPA_CONSERVAZIONE_REGISTRO_GIORNALIERO_TEMPLATE);
				JasperReport jasperReport = JasperCompileManager
					.compileReport(jasperDesign);

				Collection<ReportProtocolloView> c = ReportProtocolloDelegate
						.getInstance().stampaRegistro(utente, tipoProtocollo,
								dataReg, dataReg, 0);
				CommonReportDS ds = new CommonReportDS(c);
				jasperPrint = JasperFillManager.fillReport(
						jasperReport, parameters, ds);
				long uniqueNumberReg=System.currentTimeMillis();
				File fileReg=new File(directory, "RegistroGiornaliero"+metadati.get(TagConservazioneEnum.CODICEENTE)+uniqueNumberReg+".pdf");
				JasperExportManager.exportReportToPdfFile(jasperPrint, fileReg.getAbsolutePath());
				ConservazioneBO.getCompleteRegistroGiornalieroMetadati(metadati, uniqueNumberReg, "REGISTRO GIORNALIERO", fileReg, "Registro giornaliero di protocollo", c,c.size(),0);
				indice=new File(directory, metadati.get(TagConservazioneEnum.NUMERO)+".csv");
				ConservazioneBO.writeCSV(metadati,indice, true);
				fileNames.add(fileReg.getName());
				
			}
			
			if(form.getTotalMod()!=0){
				parameters.put("BaseDir", reportFileMod.getParentFile());
				parameters.put("ReportSubTitle",resources.getMessage("report.title.stampa_registro_modifiche_giornaliero"));
				JasperDesign jasperDesignMod = JRXmlLoader
					.load(context.getRealPath("/")
							+ FileConstants.STAMPA_CONSERVAZIONE_REGISTRO_MODIFICHE_GIORNALIERO_TEMPLATE);
				JasperReport jasperReportMod = JasperCompileManager
					.compileReport(jasperDesignMod);
				
				Collection<ReportProtocolloView> cMod = ReportProtocolloDelegate
						.getInstance().getProtocolliModificatiReport(utente,
								tipoProtocollo, dataReg, dataReg, 0);
				CommonReportDS ds = new CommonReportDS(cMod);
				jasperPrintMod = JasperFillManager.fillReport(
						jasperReportMod, parameters, ds);
				
				long uniqueNumberMod=System.currentTimeMillis();
				File filemod=new File(directory, "RegistroModifiche"+metadati.get(TagConservazioneEnum.CODICEENTE)+uniqueNumberMod+".pdf");
				JasperExportManager.exportReportToPdfFile(jasperPrintMod, filemod.getAbsolutePath());
				
				int totalAnn=ReportProtocolloDelegate.getInstance().countProtocolliAnnullatiReport(utente,
						form.getTipoProtocollo(),
						DateUtil.toDate(form.getDataInizio()),
						DateUtil.toDate(form.getDataInizio()), 0);
				ConservazioneBO.getCompleteRegistroGiornalieroMetadati(metadati, uniqueNumberMod, "REGISTRO MODIFICHE", filemod, "Registro delle modifiche", cMod,0,totalAnn);
				ConservazioneBO.writeCSV(metadati,indice, false);
				fileNames.add(filemod.getName());
			}
			
			fileNames.add(indice.getName());
			response.setHeader("Content-Disposition","attachment;filename=Registro"+DateUtil.getDataForIndex(dataReg.getTime())+".zip");
			response.setHeader("Cache-control", "");
			os.write(ZipUtil.zipFiles(directory, fileNames));
			
		} catch (Exception e) {
			logger.error("", e);
			response.setContentType("text/plain");
			e.printStackTrace(new PrintStream(os));
		} finally {
			os.close();
		}
	}
	
	public void correctPageNumbers(JasperPrint jasperPrint) {
		List<JRPrintPage> listPages = jasperPrint.getPages();
		int numberOfPages = listPages.size();
		int currentPageIndex = 1;
		for (JRPrintPage currentPage : listPages) {
			List<JRPrintElement> listElements = currentPage.getElements();
			for (Object element : listElements) {
				if (element instanceof JRTemplatePrintText) {
					JRTemplatePrintText templatePrintText = (JRTemplatePrintText) element;
					// set current page
					if (templatePrintText.getKey() != null
							&& templatePrintText.getKey().equalsIgnoreCase(
									"textFieldCurrentPage")) {
						templatePrintText.setText("Pagina "
								+ String.valueOf(currentPageIndex++) + " di ");
					}

					// set total number of pages
					if (templatePrintText.getKey() != null
							&& templatePrintText.getKey().equalsIgnoreCase(
									"textFieldNumberOfPages")) {
						templatePrintText
								.setText(String.valueOf(numberOfPages));
					}
				}
			}
		}
	}
}