package it.compit.fenice.mvc.presentation.action.protocollo;

import it.compit.fenice.mvc.presentation.action.protocollo.helper.CaricaAllaccio;
import it.compit.fenice.mvc.presentation.actionform.protocollo.VisualizzaProtocolloForm;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
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

/**
 * Implementation of <strong>Action </strong> to create a new E-Photo User.
 * 
 * @author Almaviva sud.
 * 
 */

public class VisualizzaProtocolloAction extends Action {

	static Logger logger = Logger.getLogger(VisualizzaProtocolloAction.class.getName());
	
	public final static String FLAG_PROTOCOLLO_ANNULLATO = "C";

	public ActionForward downloadDocumento(ActionMapping mapping,
			DocumentoVO doc, HttpServletRequest request,
			HttpServletResponse response, int aooId) throws Exception {
		ActionMessages errors = ProtocolloFileUtility.downloadFileProtocollo(
				doc, response, aooId);
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return mapping.findForward("erroreDownload");
		}
		ActionForward actionForward = new ActionForward();
		actionForward.setName("none");
		return actionForward;
	}

	protected void caricaAllaccio(HttpServletRequest request,
			VisualizzaProtocolloForm form) {
		String type=(String) request.getAttribute("type");
		if (type == null || type.equals("I")) {
			CaricaAllaccio.caricaProtocolloIngresso(request, form);
		} else if (type.equals("U")) {
			CaricaAllaccio.caricaProtocolloUscita(request, form);
		} else if (type.equals("P")) {
			CaricaAllaccio.caricaPostaInterna(request, form);
		}
	}

	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession(true);
		VisualizzaProtocolloForm vaForm = (VisualizzaProtocolloForm) form;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		if(request.getAttribute("tornaType")!=null)
			vaForm.setProtocolloAllacciatoType((String)request.getAttribute("tornaType"));
		if(request.getAttribute("tornaProtocolloId")!=null)
			vaForm.setProtocolloAllacciatoId((Integer) request.getAttribute("tornaProtocolloId"));
		
		if(request.getParameter("protocolloId")!=null){
			Integer id=Integer.valueOf(request.getParameter("protocolloId"));
			String type=String.valueOf(request.getParameter("type"));
			request.setAttribute("type", type);
			request.setAttribute("protocolloId", id);
		}
		
		caricaAllaccio(request, vaForm);
		if (request.getParameter("downloadAllegatoId") != null) {
			int docId = NumberUtil.getInt((String) request
					.getParameter("downloadAllegatoId"));
			int aooId = utente.getAreaOrganizzativa().getId();
			DocumentoVO doc = vaForm.getDocumentoAllegato(String.valueOf(docId));
			return downloadDocumento(mapping, doc, request, response, aooId);

		} else if (request.getParameter("downloadDocumentoPrincipale") != null) {
			DocumentoVO doc = vaForm.getDocumentoPrincipale();
			int aooId = utente.getAreaOrganizzativa().getId();
			return downloadDocumento(mapping, doc, request, response, aooId);

		}
		else if (request.getParameter("btnIndietro") != null) {
			if(vaForm.getProtocolloAllacciatoType()==null)
				return mapping.findForward("indietro");
			if(vaForm.getProtocolloAllacciatoId()!=0){
				request.setAttribute("protocolloId",vaForm.getProtocolloAllacciatoId());
				if(vaForm.getProtocolloAllacciatoType().equals("P"))
					return mapping.findForward("tornaPostaView");
				else if(vaForm.getProtocolloAllacciatoType().equals("I"))
					return mapping.findForward("tornaIngressoView");
				if(vaForm.getProtocolloAllacciatoType().equals("U"))
					return mapping.findForward("tornaUscitaView");
			}else{
				if(vaForm.getProtocolloAllacciatoType().equals("P"))
					return mapping.findForward("tornaPosta");
				else if(vaForm.getProtocolloAllacciatoType().equals("I"))
					return mapping.findForward("tornaIngresso");
				if(vaForm.getProtocolloAllacciatoType().equals("U"))
					return mapping.findForward("tornaUscita");
			}
		}
		
		
		return mapping.findForward("input");
	}
	
}