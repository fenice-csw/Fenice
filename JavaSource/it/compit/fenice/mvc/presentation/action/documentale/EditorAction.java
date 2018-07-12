package it.compit.fenice.mvc.presentation.action.documentale;

import it.compit.fenice.mvc.business.EditorDelegate;
import it.compit.fenice.mvc.presentation.actionform.documentale.EditorForm;
import it.compit.fenice.mvc.vo.documentale.EditorVO;
import it.compit.fenice.util.EditorUtil;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.util.PdfUtil;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class EditorAction extends Action {
	
	static Logger logger = Logger.getLogger(EditorAction.class.getName());

	public void stampaPdf(EditorForm form, HttpServletRequest request,
			HttpServletResponse response, ActionMessages errors) {
		try {
			InputStream is = new ByteArrayInputStream(form.getTesto()
					.getBytes());
			String tmpPath = PdfUtil.creaPDFdaHtml(form.getNomeFile(), is,
					request, errors);
			response.setContentType("application/pdf");
			OutputStream bos = response.getOutputStream();
			byte[] buff = new byte[2048];
			int bytesRead;
			InputStream stream = new FileInputStream(tmpPath);
			while (-1 != (bytesRead = stream.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename="
					+ form.getNomeFile() + ".pdf");
			stream.close();
			bos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession(true);
		EditorForm eForm = (EditorForm) form;
		EditorDelegate delegate = EditorDelegate.getInstance();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		if (session.getAttribute("tornaProcedimento") != null) {
			eForm.setProcedimentoId((Integer) session
					.getAttribute("tornaProcedimento"));
			eForm.setTipoProtocollo("U");
			EditorUtil.aggiornaFormDaProcedimento(eForm,request);
			session.removeAttribute("tornaProcedimento");
		}
		if (request.getParameter("btnNuovoDocumentoAction") != null) {
			eForm.inizializzaForm(utente.getCaricaInUso());
			eForm.setModificabile(true);
			return mapping.findForward("edit");
		}
		if (request.getParameter("btnDaTemplateAction") != null) {
			eForm.inizializzaForm(utente.getCaricaInUso());
			return mapping.findForward("template");
		} else if (request.getParameter("eliminaDoc") != null) {
			int docId = Integer.valueOf(request.getParameter("eliminaDoc"))
					.intValue();
			int val = delegate.cancellaDocumento(docId);
			if (val != ReturnValues.SAVED) {
				errors.add("errore_nel_salvataggio", new ActionMessage(
						"errore_nel_salvataggio", "", ""));
				saveErrors(request, errors);
			}
			request.setAttribute(mapping.getAttribute(), eForm);
			return mapping.findForward("input");

		} else if (request.getParameter("docId") != null) {
			int docId = 0;
			docId = Integer.valueOf(request.getParameter("docId")).intValue();
			EditorVO vo = delegate.getDocumento(docId);
			if (vo.getReturnValue() != ReturnValues.FOUND) {
				errors.add("nomeFile", new ActionMessage(
						"error.documento.non_caricato", docId, ""));
				saveErrors(request, errors);
				return mapping.findForward("input");
			}
			EditorUtil.aggiornaDocumentoForm(vo, eForm, utente);
			eForm.setModificabile(true);
			request.setAttribute(mapping.getAttribute(), eForm);
			return mapping.findForward("edit");
		} else if (request.getParameter("versioneSelezionata") != null) {
			int versione = 0;
			versione = Integer.valueOf(
					request.getParameter("versioneSelezionata")).intValue();
			EditorVO vo = delegate.getDocumentoByIdVersione(eForm
					.getDocumentoId(), versione);
			if (vo.getReturnValue() != ReturnValues.FOUND) {
				errors.add("nomeFile", new ActionMessage(
						"error.documento.storia_non_caricato", versione, ""));
				saveErrors(request, errors);
				return mapping.findForward("input");
			}
			EditorUtil.aggiornaDocumentoForm(vo, eForm, utente);
			eForm.setModificabile(false);
			request.setAttribute(mapping.getAttribute(), eForm);
			return mapping.findForward("edit");
		} else if (request.getParameter("btnStoria") != null) {
			return (mapping.findForward("storiaEditor"));
		} else if (request.getParameter("pdfAction") != null) {
			stampaPdf(eForm, request, response, errors);
		} else if (request.getParameter("annullaAction") != null) {
			eForm.inizializzaForm(utente.getCaricaInUso());
			return mapping.findForward("edit");
		} else if (request.getParameter("protocollaAction") != null) {
			errors = eForm.validate(mapping, request);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return mapping.findForward("edit");
			}
			if (eForm.getTipoProtocollo().equals("U")) {
				eForm.setCaricaDocumento(true);
				EditorUtil.preparaProtocolloUscita(request, eForm, utente,
						errors, 0);
				return (mapping.findForward("protocollazioneUscita"));
			} else {
				eForm.setCaricaDocumento(true);
				EditorUtil.preparaPostaInterna(request, eForm, utente, errors,
						0);
				return (mapping.findForward("protocollazionePostaInterna"));
			}
		} else if (request.getParameter("indietroAction") != null) {
			return mapping.findForward("indietro");
		} else if (request.getParameter("inviaProtocolloAction") != null) {
			errors = eForm.validate(mapping, request);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return mapping.findForward("edit");
			}
			return mapping.findForward("inviaDocumento");
		} else if (request.getParameter("saveEditorAction") != null) {
			errors = eForm.validate(mapping, request);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return mapping.findForward("edit");
			}
			EditorVO eVO = new EditorVO();
			EditorUtil.aggiornaModel(eForm, eVO, utente);
			int retVal = delegate.salvaDocumentoEditor(eVO);
			if (retVal != ReturnValues.SAVED) {
				errors.add("errore_nel_salvataggio", new ActionMessage(
						"errore_nel_salvataggio", "", ""));
				saveErrors(request, errors);
				return mapping.findForward("edit");
			}
			return mapping.findForward("indietro");
		} 
		eForm.setDocumenti(delegate.getDocumentiByCarica(utente
				.getCaricaInUso()));
		return mapping.findForward("input");
	}

}