package it.compit.fenice.mvc.presentation.action.documentale;

import it.compit.fenice.mvc.business.EditorDelegate;
import it.compit.fenice.mvc.presentation.actionform.documentale.AvvocatoGeneraleForm;
import it.compit.fenice.mvc.presentation.helper.DocumentoAvvocatoGeneraleULLView;
import it.compit.fenice.util.VelocityTemplateUtils;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.ProcedimentoDelegate;
import it.finsiel.siged.mvc.presentation.helper.ProcedimentoView;
import it.finsiel.siged.report.protocollo.CommonReportDS;
import it.finsiel.siged.util.PdfUtil;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

public class AvvocatoGeneraleAction extends Action {

	static Logger logger = Logger.getLogger(AvvocatoGeneraleAction.class
			.getName());

	public boolean stampaReport(HttpServletRequest request,
			HttpServletResponse response, Collection<Integer> ids)
			throws IOException, ServletException {
		ServletContext context = getServlet().getServletContext();
		OutputStream os = response.getOutputStream();
		boolean pdf=false;
		try {
			JasperDesign jasperDesign = JRXmlLoader.load(context.getRealPath("/")+ FileConstants.STAMPA_PROCEDIMENTI_AVVOCATO_GENERALE_TEMPLATE);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("ReportTitle", "Elenco Decreti");
			Collection<ProcedimentoView> c = ProcedimentoDelegate.getInstance().getElencoDecreti(ids);
			CommonReportDS ds = new CommonReportDS(c,ProcedimentoView.class);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ds);
			response.setHeader("Content-Disposition", "attachment;filename=ElencoDecreti.pdf");
			response.setHeader("Cache-control", "");
			response.setContentType("application/pdf");
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
			exporter.exportReport();
			pdf=true;
		} catch (Exception e) {
			logger.debug("Errore di compilazione", e);
			response.setContentType("text/plain");
			e.printStackTrace(new PrintStream(os));
		} finally {
			os.close();
		}
		return pdf;
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		AvvocatoGeneraleForm dForm = (AvvocatoGeneraleForm) form;
		ActionMessages errors = new ActionMessages();
		session.setAttribute("avvocatoGeneraleForm", dForm);
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		if (form == null) {
			logger.info(" Creating new InvioDocumentoAction");
			request.setAttribute(mapping.getAttribute(), form);
		} else if (request.getParameter("stampa") != null) {
			int docId = 0;
			docId = new Integer(request.getParameter("stampa"));
			DocumentoAvvocatoGeneraleULLView view = dForm.getDocumenti().get(
					docId);
			stampaPDF(view, request, response, errors);
			return null;
		} else if (request.getParameter("lavorato") != null) {
			int docId = 0;
			docId = new Integer(request.getParameter("lavorato"));
			DocumentoAvvocatoGeneraleULLView view = dForm.getDocumenti().get(
					docId);
			EditorDelegate.getInstance().aggiornaStatoByFlagTipo(view.getDocumentoId(), 1);
			ProcedimentoDelegate.getInstance().inviaProcedimento(view.getProcedimentoId(), "I", utente);
			dForm.getDocumenti().remove(Integer.valueOf(docId));
			return mapping.findForward("lista");
		} else if (request.getParameter("btnStampaElencoDecreti") != null) {
			if(dForm.getChkBox().length>0)
				stampaReport(request, response, dForm.getProcedimentiIdChkBox());
			return null;
		}else if (request.getParameter("btnElencoDecretiLavorati") != null) {
			for (String id:dForm.getChkBox()) {
				DocumentoAvvocatoGeneraleULLView view=dForm.getDocumenti().get(Integer.valueOf(id));
				EditorDelegate.getInstance().setElencoDecretiLavorati(view.getDocumentoId(), view.getProcedimentoId(), 1, utente);
				dForm.getDocumenti().remove(Integer.valueOf(id));
			}
			return mapping.findForward("lista");
		}
		return null;
	}

	public static boolean stampaPDF(DocumentoAvvocatoGeneraleULLView view,
			HttpServletRequest request, HttpServletResponse response,
			ActionMessages errors) {
		boolean pdf=false;
		try {
			Utente utente = (Utente) request.getSession().getAttribute(
					Constants.UTENTE_KEY);
			String ctxPath="http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath();
			String text = VelocityTemplateUtils.createULLDocument(
					view.getTxt(), utente,ctxPath);
			InputStream is = new ByteArrayInputStream(text.getBytes("UTF-8"));
			String tmpPath = "";
			if (view.getStatoProcedimento() == 4)
				tmpPath = PdfUtil.creaPDFdaHtml("relazione", is, request,
						errors);
			else if (view.getStatoProcedimento() == 6)
				tmpPath = PdfUtil.creaPDFdaHtml("decreto", is, request, errors);
			else
				tmpPath = PdfUtil.creaPDFdaHtml("lettera", is, request, errors);
			response.setContentType("application/pdf");
			OutputStream bos = response.getOutputStream();
			byte[] buff = new byte[2048];
			int bytesRead;
			InputStream stream = new FileInputStream(tmpPath);
			while (-1 != (bytesRead = stream.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			response.setContentType("application/pdf");
			if (view.getStatoProcedimento() == 4)
				response.setHeader("Content-disposition","attachment; filename=relazione.PDF");
			else if (view.getStatoProcedimento() == 6)
				response.setHeader("Content-disposition","attachment; filename=decreto.PDF");
			else
				response.setHeader("Content-disposition","attachment; filename=lettera.PDF");
			stream.close();
			bos.close();
			pdf=true;
		} catch (Exception ex) {
			logger.debug("Errore di compilazione", ex);
			response.setContentType("text/plain");
			ex.printStackTrace();
		}
		return pdf;

	}
}
