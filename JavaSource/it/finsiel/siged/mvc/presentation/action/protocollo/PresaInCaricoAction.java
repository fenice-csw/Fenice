package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.DocumentoDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.PresaInCaricoForm;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.util.NumberUtil;
import it.flosslab.mvc.presentation.action.protocollo.helper.file.ProtocolloFileUtility;

import java.util.Calendar;
import java.util.GregorianCalendar;

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

public class PresaInCaricoAction extends Action {

	// ----------------------------------------------------- Instance Variables

	/**
	 * The <code>Log</code> instance for this application.
	 */
	static Logger logger = Logger
			.getLogger(PresaInCaricoAction.class.getName());

	public final static String PROTOCOLLI_SOSPESI = "S";

	public final static String STATO_TIPO_SCARICO = "N";

	
	
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession();
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		PresaInCaricoForm presaInCarico = (PresaInCaricoForm) form;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		int maxRighe=Integer.parseInt(utente.getAreaOrganizzativa().getMaxRighe());
		if (form == null) {
			logger.info(" Creating new PresaInCaricoAction");
			form = new PresaInCaricoForm();
			request.setAttribute(mapping.getAttribute(), form);
		}
		int numeroProtocolloDa = 0;
		if (!"".equals(presaInCarico.getDataRegistrazioneDa())) {
		}
		if (!"".equals(presaInCarico.getDataRegistrazioneA())) {
		}
		if (!"".equals(presaInCarico.getNumeroProtocolloDa())) {
			numeroProtocolloDa = NumberUtil.getInt(presaInCarico
					.getNumeroProtocolloDa());
		}
		if (!"".equals(presaInCarico.getNumeroProtocolloA())) {
		}
		if (!"".equals(presaInCarico.getAnnoProtocolloDa())) {
		}
		if (!"".equals(presaInCarico.getAnnoProtocolloA())) {
		}
	
		
		 if(request.getParameter("daRifiutare")!=null){
			 logger.info("ProtocolloDaRifiutare: " + request.getParameter("daRifiutare"));	
			 request.setAttribute("protocolloId", new Integer(request.getParameter("daRifiutare")));
					return mapping.findForward("rifiutaProtocollo");
			}
		else if (request.getParameter("daRiassegnare") != null) {
			int protId = Integer
					.parseInt(request.getParameter("daRiassegnare"));
			logger.info("ProtocolloDaRifiutare: " + protId);
			request.setAttribute("protocolloId", new Integer(protId));
			return (mapping.findForward("riassegnaProtocollo"));

		} 
		else if(request.getParameter("assegnaProcedimento")!=null){
			session.setAttribute("PresaInCarico", true);
			request.setAttribute("protocolloId", new Integer(request.getParameter("assegnaProcedimento")));
			return mapping.findForward("aggiungiProcedimento");
		}
		else if (request.getParameter("lavoratoProcedimento") != null) {
			request.setAttribute("protocolloId", new Integer(request.getParameter("lavoratoProcedimento")));
			session.setAttribute("PAGINA_RITORNO", "SCARICO_UFFICIO");
			return (mapping.findForward("lavoratoProcedimento"));
		}
		else if (request.getParameter("btnRiassegna") != null) {
			session.setAttribute("SCARICO_UFFICIO", "SCARICO_UFFICIO");
			String[] protocolliAccettati = presaInCarico.getProtocolliSelezionati();
			if (protocolliAccettati != null) {
				request.setAttribute("protocolloId", new Integer(protocolliAccettati[0]));
				session.setAttribute("protocolloIdSelezionati",protocolliAccettati);
				return (mapping.findForward("riassegnazioneMultiplaProtocollo"));
			}
		} else if (request.getParameter("daFascicolare") != null) {
			request.setAttribute("protocolloId", new Integer(request
					.getParameter("daFascicolare")));
			session.setAttribute("PAGINA_RITORNO", "SCARICO_UFFICIO");
			return (mapping.findForward("fascicolaProtocollo"));
		} else if (presaInCarico.getBtnFascicolaSelezionati() != null) {
			session.setAttribute("PAGINA_RITORNO", "SCARICO_UFFICIO");
			presaInCarico.setBtnFascicolaSelezionati(null);
			String[] protocolliDaFascicolare = presaInCarico
					.getProtocolliSelezionati();
			if (protocolliDaFascicolare != null) {
				session.setAttribute("protocolliIds", protocolliDaFascicolare);
				request.setAttribute("protocolloId", new Integer(protocolliDaFascicolare[0]));
				return (mapping.findForward("fascicolazioneMultiplaProtocolli"));
			}
		}
		else if (presaInCarico.getBtnCerca() != null) {
			presaInCarico.setBtnCerca(null);
			presaInCarico.setProtocolliSelezionati(null);
			GregorianCalendar today = new GregorianCalendar();
			int numProtocolliAssegnati=0;
			if(!presaInCarico.getTipoProtocollo().equals("posta")){	
				numProtocolliAssegnati = delegate.contaProtocolliAssegnati(
				utente, today.get(Calendar.YEAR)-1, today.get(Calendar.YEAR), numeroProtocolloDa, "U");
			}else{
				numProtocolliAssegnati=delegate.contaPostaInternaAssegnataPerNumero(utente,numeroProtocolloDa, "U");
			}
			if (numProtocolliAssegnati == 0) {
				presaInCarico.setProtocolliInCarico(null);
				errors.add("nessun_dato", new ActionMessage("nessun_dato", "",
						""));
			} else if (numProtocolliAssegnati <= maxRighe) {
				if(!presaInCarico.getTipoProtocollo().equals("posta")){
				presaInCarico.setProtocolliInCarico(
						delegate.getProtocolliAssegnati(utente, today.get(Calendar.YEAR)-1, today.get(Calendar.YEAR), numeroProtocolloDa, "U"));
				}else{
					presaInCarico.setProtocolliInCarico(
							delegate.getPostaInternaAssegnataPerNumero(utente,numeroProtocolloDa, "U"));
				}
				return (mapping.findForward("input"));
			} else {
				errors.add("controllo.maxrighe", new ActionMessage(
						"controllo.maxrighe", "" + numProtocolliAssegnati,
						"protocolli", "" + maxRighe));
			}
		}
		 //////
		else if (request.getParameter("protocolloSelezionato") != null) {
			request.setAttribute("protocolloId", new Integer(request
					.getParameter("protocolloSelezionato")));
			return (mapping.findForward("visualizzaProtocolloIngresso"));
		} else if (request.getParameter("downloadDocprotocolloSelezionato") != null) {
			String param = request.getParameter("downloadDocprotocolloSelezionato");
			if(param!=null){
				long annoNumero=Long.valueOf(param);
				int aooId=utente.getAreaOrganizzativa().getId();
				ReportProtocolloView prot = (ReportProtocolloView) presaInCarico.getProtocolliInCarico().get(annoNumero);
				return downloadDocumento(mapping, prot, request, response,aooId);
			}
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("input"));
		}
		//presaInCarico.removeProtocolliInCarico();
		return (mapping.findForward("input"));
	}
	
	public ActionForward downloadDocumento(ActionMapping mapping,
			ReportProtocolloView prot, HttpServletRequest request,
			HttpServletResponse response,int aooId) throws Exception {
		DocumentoVO doc = DocumentoDelegate.getInstance().getDocumento(prot.getDocumentoId());
		ActionMessages errors = ProtocolloFileUtility.downloadFileProtocollo(doc,
				response,aooId);
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return mapping.findForward("erroreDownload");
		}
		return null;
	}
	
}
