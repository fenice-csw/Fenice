package it.compit.fenice.mvc.presentation.action.repertori;

import it.compit.fenice.mvc.bo.RepertorioBO;
import it.compit.fenice.mvc.business.RepertorioDelegate;
import it.compit.fenice.mvc.presentation.actionform.repertori.RepertorioForm;
import it.compit.fenice.mvc.presentation.helper.DocumentoRepertorioView;
import it.compit.fenice.mvc.vo.repertori.RepertorioVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
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
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

public class RepertorioAction extends Action {

	private static void aggiornaResponsabileForm(RepertorioForm form,
			int ufficioId) {
		Organizzazione org = Organizzazione.getInstance();
		if (ufficioId != 0) {
			Ufficio uff = org.getUfficio(ufficioId);
			form.setResponsabile(RepertorioBO.newResponsabile(uff));
		}
	}

	private static void aggiornaResponsabile(RepertorioForm form,
			RepertorioVO vo) {
		AssegnatarioView responsabile = form.getResponsabile();
		if (responsabile != null) {
			vo.setResponsabileId(responsabile.getUfficioId());
		}
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession();
		RepertorioForm cForm = (RepertorioForm) form;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		RepertorioDelegate delegate = RepertorioDelegate.getInstance();
		if (cForm.getUfficioResponsabileCorrenteId() == 0) {
			RepertorioBO.impostaUfficioUtentiResponsabile(utente, cForm, true);
		}
		if (request.getParameter("assegnatiAction") != null) {
			Collection<RepertorioVO> repertori = delegate.getRepertoriByUfficio(utente.getUfficioInUso());
			cForm.setRepertori(repertori);
			request.setAttribute(mapping.getAttribute(), cForm);
			return mapping.findForward("listaUtente");
		} else if (request.getParameter("impostaUfficioAction") != null) {
			cForm.setUfficioResponsabileCorrenteId(cForm
					.getUfficioResponsabileSelezionatoId());
			RepertorioBO.impostaUfficioUtentiResponsabile(utente, cForm, true);
		} else if (request.getParameter("ufficioPrecedenteAction") != null) {
			cForm.setUfficioResponsabileCorrenteId(cForm
					.getUfficioCorrenteResponsabile().getParentId());
			RepertorioBO.impostaUfficioUtentiResponsabile(utente, cForm, true);
		} else if (request.getParameter("assegnaUtenteAction") != null) {
			RepertorioBO.impostaResponsabile(cForm);
		} else if (request.getParameter("assegnaUfficioCorrenteAction") != null) {
			RepertorioBO.impostaResponsabile(cForm);
		} else if (request.getParameter("btnNuovoDocumentoRepertorio") != null) {
			session.setAttribute("repertorioId", cForm.getRepertorioId());
			return mapping.findForward("nuovoDocumentoRepertorio");
		} else if (request.getParameter("btnStampa") != null) {
			stampaRepertorio(request, response, cForm);
		} else if (request.getParameter("repertorioId") != null
				|| request.getAttribute("repertorioId") != null) {
			int repertorioId = 0;
			if (request.getParameter("repertorioId") != null)
				repertorioId = Integer.valueOf(request.getParameter("repertorioId")).intValue();
			else
				repertorioId = (Integer) request.getAttribute("repertorioId");
			RepertorioVO rep = delegate.getRepertorio(repertorioId);
			cForm.setDescrizione(rep.getDescrizione());
			cForm.setFlagWeb(rep.getFlagWeb());
			cForm.setRepertorioId(rep.getRepertorioId());
			cForm.setDocumentiRepertorio(delegate.getDocumentiRepertorio(repertorioId));
			cForm.setDocumentiDaRepertoriale(delegate.getDocumentiDaRepertoriale(repertorioId));
			aggiornaResponsabileForm(cForm, rep.getResponsabileId());
			request.setAttribute(mapping.getAttribute(), cForm);
			return (mapping.findForward("input"));
		} else if (request.getParameter("btnConferma") != null) {
			RepertorioVO repVO = new RepertorioVO();
			repVO.setAooId(utente.getAreaOrganizzativa().getId());
			repVO.setDescrizione(cForm.getDescrizione());
			repVO.setFlagWeb(cForm.getFlagWeb());
			aggiornaResponsabile(cForm, repVO);
			if(cForm.getRepertorioId()==0){
				RepertorioDelegate.getInstance().salvaRepertorio(utente, repVO);
			}
			else{
				repVO.setRepertorioId(cForm.getRepertorioId());
				RepertorioDelegate.getInstance().updateRepertorio(utente, repVO);
			}
			cForm.resetForm();
		} else if (request.getParameter("modificaRepertorio") != null) {
			int repertorioId = Integer.valueOf(request.getParameter("modificaRepertorio")).intValue();
			RepertorioVO rep = delegate.getRepertorio(repertorioId);
			cForm.setDescrizione(rep.getDescrizione());
			cForm.setFlagWeb(rep.getFlagWeb());
			cForm.setRepertorioId(rep.getRepertorioId());
			aggiornaResponsabileForm(cForm, rep.getResponsabileId());
		} else if (request.getParameter("btnIndietro") != null) {
			return mapping.findForward("listaUtente");
		}
		Collection<RepertorioVO> repertori = delegate.getRepertori(utente
				.getAreaOrganizzativa().getId());
		cForm.setRepertori(repertori);
		request.setAttribute(mapping.getAttribute(), cForm);
		if (!errors.isEmpty())
			saveErrors(request, errors);
		return (mapping.findForward("lista"));
	}
	
	

	
	public void stampaRepertorio(HttpServletRequest request, HttpServletResponse response,
			RepertorioForm form) throws IOException, ServletException {
		ServletContext context = request.getSession().getServletContext();
		OutputStream os = response.getOutputStream();
		try {
			JasperDesign jasperDesign = JRXmlLoader
					.load(context.getRealPath("/")
							+ FileConstants.STAMPA_REPERTORIO_TEMPLATE);
				JasperReport jasperReport = JasperCompileManager
					.compileReport(jasperDesign);
			
			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("nome_repertorio", form.getDescrizione());
			CommonReportDS ds = new CommonReportDS(form.getDocumentiRepertorio(),DocumentoRepertorioView.class);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			response.setHeader("Content-Disposition", "attachment;filename="
					+ form.getDescrizione() + ".pdf");
			response.setHeader("Cache-control", "");
			response.setContentType("application/pdf");
			JRPdfExporter exporter = new JRPdfExporter();
			exporter
					.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
			exporter.exportReport();
		} catch (Exception e) {
			response.setContentType("text/plain");
			e.printStackTrace(new PrintStream(os));
		} finally {
			os.close();
		}
	}
}