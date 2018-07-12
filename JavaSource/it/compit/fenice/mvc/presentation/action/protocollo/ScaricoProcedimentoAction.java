package it.compit.fenice.mvc.presentation.action.protocollo;

import it.compit.fenice.mvc.bo.ProcedimentoBO;
import it.compit.fenice.mvc.presentation.actionform.protocollo.AlertProcedimentoForm;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.DocumentoDelegate;
import it.finsiel.siged.mvc.business.ProcedimentoDelegate;
import it.finsiel.siged.mvc.presentation.helper.ProcedimentoView;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProcedimentoVO;
import it.flosslab.mvc.presentation.action.protocollo.helper.file.ProtocolloFileUtility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

public class ScaricoProcedimentoAction extends Action {

	static Logger logger = Logger.getLogger(ScaricoProcedimentoAction.class
			.getName());

	public ActionForward downloadDocumento(ActionMapping mapping,
			int documentoId, HttpServletRequest request,
			HttpServletResponse response, int aooId) throws Exception {
		DocumentoVO doc = DocumentoDelegate.getInstance().getDocumento(
				documentoId);
		ActionMessages errors = ProtocolloFileUtility.downloadFileProtocollo(
				doc, response, aooId);
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return mapping.findForward("erroreDownload");
		}
		return null;
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession(true);
		AlertProcedimentoForm alertForm = (AlertProcedimentoForm) form;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		ProcedimentoDelegate delegate = ProcedimentoDelegate.getInstance();
		if (request.getParameter("rispondi") != null) {
			String numeroProcedimento = request.getParameter("rispondi");
			aggiornaScaricoProcedimentoForm(alertForm, numeroProcedimento, false);
			ProcedimentoVO procedimento = delegate.getProcedimentoVO(alertForm
					.getProcedimentoId());
			ProcedimentoBO.preparaPostaInterna(request, procedimento, utente);
			session.setAttribute("PAGINA_RITORNO", "SCARICO_PROCEDIMENTO");
			return (mapping.findForward("creaProtocolloRisposta"));
		} else if (request.getParameter("chiudi") != null) {
			String numeroProcedimento = request.getParameter("chiudi");
			aggiornaScaricoProcedimentoForm(alertForm, numeroProcedimento, false);
			return (mapping.findForward("confirmChiusura"));
		} else if (request.getParameter("visto") != null) {
			String numeroProcedimento = request.getParameter("visto");
			aggiornaScaricoProcedimentoForm(alertForm, numeroProcedimento, true);
			return (mapping.findForward("confirmChiusura"));
		} else if (request.getParameter("confermaChiusuraIstruttoriaAction") != null) {
			boolean chiuso = delegate
					.setProcedimentoIstruttoreLavorato(
							alertForm.getProcedimentoId(),
							utente.getCaricaInUso(),utente.getValueObject().getUsername());
			if (chiuso)
				alertForm.getProcedimenti().remove(
						String.valueOf(alertForm.getNumProcedimento()));
			return (mapping.findForward("input"));
		} else if (request.getParameter("annullaAction") != null) {
			return (mapping.findForward("input"));
		} else if (request.getParameter("downloadDocprotocolloSelezionato") != null) {
			String param = request
					.getParameter("downloadDocprotocolloSelezionato");
			if (param != null) {
				int documentoId = Integer.valueOf(param);
				int aooId = utente.getAreaOrganizzativa().getId();
				return downloadDocumento(mapping, documentoId, request,
						response, aooId);
			}
		} 
		else if (request.getParameter("chiudiDaRisposta") != null)
			return (mapping.findForward("confirmChiusura"));
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		logger.info("Execute ScaricoProcedimentoAction");
		return (mapping.findForward("input"));
	}

	private void aggiornaScaricoProcedimentoForm(
			AlertProcedimentoForm alertForm, String numeroProcedimento,
			boolean istruttore) {
		ProcedimentoView prView = null;
		if (istruttore) {
			prView = (ProcedimentoView) alertForm.getProcedimenti()
					.get(numeroProcedimento);
		} else
			prView = (ProcedimentoView) alertForm.getProcedimenti().get(
					numeroProcedimento);
		alertForm.setProcedimentoId(prView.getProcedimentoId());
		alertForm.setDataAvvio(prView.getDataAvvioView());
		alertForm.setResponsabile(prView.getResponsabile());
		alertForm.setOggettoProcedimento(prView.getOggetto());
		alertForm.setNumProcedimento(prView.getNumeroProcedimento());
		alertForm.setTitolareProcedimento(prView.isTitolareProcedimento());
		alertForm.setIstruttore(istruttore);
	}

}

