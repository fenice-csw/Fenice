package it.compit.fenice.mvc.presentation.action.protocollo;

import it.compit.fenice.mvc.presentation.actionform.protocollo.AlertProcedimentoForm;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.DocumentoDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.util.NumberUtil;
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

public class AlertProcedimentoAction extends Action{

	static Logger logger = Logger.getLogger(AlertProcedimentoAction.class
			.getName());
	
	public ActionForward downloadDocumento(ActionMapping mapping,
			DocumentoVO doc, HttpServletRequest request,
			HttpServletResponse response, int aooId) throws Exception {
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
		ActionForward actionForward = super.execute(mapping, form, request,
				response);
		if (actionForward != null) {
			return actionForward;
		}
		HttpSession session = request.getSession(true); 
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		if (form == null) {
			logger.info(" Creating new AlertProcedimentoAction");
			form = new AlertProcedimentoForm();
			session.setAttribute(mapping.getAttribute(), form);
		}
		if (request.getParameter("visualizzaProcedimentoId") != null) {
			if (NumberUtil.isInteger(request
					.getParameter("visualizzaProcedimentoId"))) {
				Integer fId = Integer.valueOf(request.getParameter("visualizzaProcedimentoId"));
				request.setAttribute("visualizzaProcedimentoId", fId);
				return mapping.findForward("visualizzaProcedimento");
			} else {
				logger.warn("Id procedimento non di formato numerico:"
						+ request.getParameter("visualizzaProcedimentoId"));
			}
			return mapping.findForward("input");
		}else if (request.getParameter("downloadDocprotocolloSelezionato") != null) {
			Integer id = new Integer(Integer.parseInt(request.getParameter("downloadDocprotocolloSelezionato")));
			DocumentoVO doc = null;
				ProtocolloVO p = ProtocolloDelegate.getInstance().getProtocolloVOById(id.intValue());
				int docId = p.getDocumentoPrincipaleId();
				doc = DocumentoDelegate.getInstance().getDocumento(docId);
			int aooId = utente.getAreaOrganizzativa().getId();
			return downloadDocumento(mapping, doc, request, response, aooId);

		}
		return mapping.findForward("input");
	}

	
}
