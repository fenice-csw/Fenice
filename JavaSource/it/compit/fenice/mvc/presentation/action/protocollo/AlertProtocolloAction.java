package it.compit.fenice.mvc.presentation.action.protocollo;

import it.compit.fenice.mvc.presentation.actionform.protocollo.AlertProtocolloForm;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.util.NumberUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AlertProtocolloAction extends Action{

	static Logger logger = Logger.getLogger(AlertProtocolloAction.class
			.getName());
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward actionForward = super.execute(mapping, form, request,
				response);
		if (actionForward != null) {
			return actionForward;
		}
		HttpSession session = request.getSession(true); 
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		AlertProtocolloForm alertForm = (AlertProtocolloForm) form;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		if (form == null) {
			logger.info(" Creating new AlertProtocolloAction");
			form = new AlertProtocolloForm();
			session.setAttribute(mapping.getAttribute(), form);
		}
		if (request.getParameter("visualizzaProtocolloId") != null) {
			if (NumberUtil.isInteger(request
					.getParameter("visualizzaProtocolloId"))) {
				Integer fId = Integer.valueOf(request.getParameter("visualizzaProtocolloId"));
				request.setAttribute("protocolloId", fId);
				return mapping.findForward("visualizzaProtocollo");
			} else {
				logger.warn("Id procedimento non di formato numerico:"
						+ request.getParameter("visualizzaProtocolloId"));
			}
			return mapping.findForward("input");
		}else if (request.getParameter("riassegnaProtocollo") != null) {
			request.setAttribute("protocolloId", new Integer(request
					.getParameter("riassegnaProtocollo")));
			return (mapping.findForward("riassegnaProtocollo"));

		}else if (request.getParameter("daFascicolare") != null) {
			request.setAttribute("protocolloId", new Integer(request
					.getParameter("daFascicolare")));
			return (mapping.findForward("fascicolaProtocollo"));
		}
		alertForm.setProtocolli(delegate.getProtocolliAlert(utente));
		return mapping.findForward("input");
	}

	
}
