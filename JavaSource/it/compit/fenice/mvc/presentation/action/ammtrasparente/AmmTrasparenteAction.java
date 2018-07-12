package it.compit.fenice.mvc.presentation.action.ammtrasparente;

import it.compit.fenice.mvc.bo.AmmTrasparenteBO;
import it.compit.fenice.mvc.business.AmmTrasparenteDelegate;
import it.compit.fenice.mvc.presentation.actionform.ammtrasparente.AmmTrasparenteForm;
import it.compit.fenice.mvc.presentation.helper.DocumentoAmmTrasparenteView;
import it.compit.fenice.mvc.vo.ammtrasparente.AmmTrasparenteVO;
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

public class AmmTrasparenteAction extends Action {

	private static void aggiornaResponsabileForm(AmmTrasparenteForm form,
			int ufficioId) {
		Organizzazione org = Organizzazione.getInstance();
		if (ufficioId != 0) {
			Ufficio uff = org.getUfficio(ufficioId);
			form.setResponsabile(AmmTrasparenteBO.newResponsabile(uff));
		}
	}

	private static void aggiornaResponsabile(AmmTrasparenteForm form,
			AmmTrasparenteVO vo) {
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
		AmmTrasparenteForm cForm = (AmmTrasparenteForm) form;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		AmmTrasparenteDelegate delegate = AmmTrasparenteDelegate.getInstance();
		if (cForm.getUfficioResponsabileCorrenteId() == 0) {
			AmmTrasparenteBO.impostaUfficioUtentiResponsabile(utente, cForm, true);
		}
		if (request.getParameter("assegnatiAction") != null) {
			Collection<AmmTrasparenteVO> sezioni = delegate.getSezioniByUfficio(utente.getUfficioInUso());
			cForm.setSezioni(sezioni);
			request.setAttribute(mapping.getAttribute(), cForm);
			return mapping.findForward("listaUtente");
		} else if (request.getParameter("impostaUfficioAction") != null) {
			cForm.setUfficioResponsabileCorrenteId(cForm
					.getUfficioResponsabileSelezionatoId());
			AmmTrasparenteBO.impostaUfficioUtentiResponsabile(utente, cForm, true);
		} else if (request.getParameter("ufficioPrecedenteAction") != null) {
			cForm.setUfficioResponsabileCorrenteId(cForm
					.getUfficioCorrenteResponsabile().getParentId());
			AmmTrasparenteBO.impostaUfficioUtentiResponsabile(utente, cForm, true);
		} else if (request.getParameter("assegnaUtenteAction") != null) {
			AmmTrasparenteBO.impostaResponsabile(cForm);
		} else if (request.getParameter("assegnaUfficioCorrenteAction") != null) {
			AmmTrasparenteBO.impostaResponsabile(cForm);
		} else if (request.getParameter("btnNuovoDocumentoAmmTrasparente") != null) {
			session.setAttribute("sezioneId", cForm.getSezioneId());
			return mapping.findForward("nuovoDocumentoSezione");
		} else if (request.getParameter("btnStampa") != null) {
			stampaSezione(request, response, cForm);
		} else if (request.getParameter("sezioneId") != null
				|| request.getAttribute("sezioneId") != null) {
			int sezioneId = 0;
			if (request.getParameter("sezioneId") != null)
				sezioneId = Integer.valueOf(request.getParameter("sezioneId")).intValue();
			else
				sezioneId = (Integer) request.getAttribute("sezioneId");
			AmmTrasparenteVO rep = delegate.getSezione(sezioneId);
			cForm.setDescrizione(rep.getDescrizione());
			cForm.setFlagWeb(rep.getFlagWeb());
			cForm.setSezioneId(rep.getSezioneId());
			cForm.setDocumentiSezione(delegate.getDocumentiSezione(sezioneId));
			cForm.setDocumentiDaSezionale(delegate.getDocumentiDaSezionale(sezioneId));
			aggiornaResponsabileForm(cForm, rep.getResponsabileId());
			request.setAttribute(mapping.getAttribute(), cForm);
			return (mapping.findForward("input"));
		} else if (request.getParameter("btnConferma") != null) {
			AmmTrasparenteVO repVO = new AmmTrasparenteVO();
			repVO.setAooId(utente.getAreaOrganizzativa().getId());
			repVO.setDescrizione(cForm.getDescrizione());
			repVO.setFlagWeb(cForm.getFlagWeb());
			aggiornaResponsabile(cForm, repVO);
			if(cForm.getSezioneId()==0){
				AmmTrasparenteDelegate.getInstance().salvaSezione(utente, repVO);
			} else {
				repVO.setSezioneId(cForm.getSezioneId());
				AmmTrasparenteDelegate.getInstance().updateSezione(utente, repVO);
			}
			cForm.resetForm();
		} else if (request.getParameter("modificaSezione") != null) {
			int sezioneId = Integer.valueOf(request.getParameter("modificaSezione")).intValue();
			AmmTrasparenteVO rep = delegate.getSezione(sezioneId);
			cForm.setDescrizione(rep.getDescrizione());
			cForm.setFlagWeb(rep.getFlagWeb());
			cForm.setSezioneId(rep.getSezioneId());
			aggiornaResponsabileForm(cForm, rep.getResponsabileId());
		} else if (request.getParameter("btnIndietro") != null) {
			return mapping.findForward("listaUtente");
		}
		Collection<AmmTrasparenteVO> sezioni = delegate.getSezioni(utente.getAreaOrganizzativa().getId());
		cForm.setSezioni(sezioni);
		request.setAttribute(mapping.getAttribute(), cForm);
		if (!errors.isEmpty())
			saveErrors(request, errors);
		return (mapping.findForward("sezioni"));
	}
	
	

	
	public void stampaSezione(HttpServletRequest request, HttpServletResponse response,
			AmmTrasparenteForm form) throws IOException, ServletException {
		ServletContext context = request.getSession().getServletContext();
		OutputStream os = response.getOutputStream();
		try {
			JasperDesign jasperDesign = JRXmlLoader
					.load(context.getRealPath("/")
							+ FileConstants.STAMPA_AMM_TRASPARENTE_TEMPLATE);
				JasperReport jasperReport = JasperCompileManager
					.compileReport(jasperDesign);
			
			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("nome_sezione", form.getDescrizione());
			CommonReportDS ds = new CommonReportDS(form.getDocumentiSezione(),DocumentoAmmTrasparenteView.class);
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