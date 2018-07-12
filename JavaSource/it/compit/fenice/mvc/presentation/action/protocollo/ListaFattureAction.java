package it.compit.fenice.mvc.presentation.action.protocollo;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.DocumentoDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ScaricoForm;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;
import it.flosslab.mvc.presentation.action.protocollo.helper.file.ProtocolloFileUtility;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

public class ListaFattureAction extends Action {

	static Logger logger = Logger.getLogger(ListaFattureAction.class.getName());

	public final static String PROTOCOLLI_ASSEGNATI = "A";

	public final static String PROTOCOLLI_AGLI_ATTI = "A";

	public final static String PROTOCOLLI_IN_LAVORAZIONE = "N";

	public final static String PROTOCOLLI_IN_RISPOSTA = "R";

	public ActionForward downloadDocumento(ActionMapping mapping,
			ReportProtocolloView prot, HttpServletRequest request,
			HttpServletResponse response,int aooId) throws Exception {
		DocumentoVO doc = DocumentoDelegate.getInstance().getDocumento(
				prot.getDocumentoId());
		ActionMessages errors = ProtocolloFileUtility.downloadFileProtocollo(doc,
				response,aooId);
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
		//ActionMessages messages = new ActionMessages();
		HttpSession session = request.getSession(true);
		ScaricoForm scarico = (ScaricoForm) form;
		if (form == null) {
			logger.info(" Creating new ScaricoAction");
			form = new ScaricoForm();
			session.setAttribute(mapping.getAttribute(), form);
		}
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);

		if (request.getParameter("rifiutaProtocollo") != null) {
			request.setAttribute("protocolloId", new Integer(request
					.getParameter("rifiutaProtocollo")));
			return mapping.findForward("rifiutaProtocollo");
		}
		else if (request.getParameter("btnRiassegna") != null) {
			String[] protocolliAccettati = scarico.getProtocolloScaricoChkBox();
			request.setAttribute("protocolloId", new Integer(
					protocolliAccettati[0]));
			session
					.setAttribute("protocolloIdSelezionati",
							protocolliAccettati);
			return (mapping.findForward("riassegnazioneMultiplaProtocollo"));
		} else if (request.getParameter("btnFascicola") != null) {
			String[] protocolliDaFascicolare = scarico
					.getProtocolloScaricoChkBox();
			if (protocolliDaFascicolare != null) {
				session.setAttribute("protocolliIds", protocolliDaFascicolare);
				request.setAttribute("protocolloId", new Integer(protocolliDaFascicolare[0]));
				return (mapping.findForward("fascicolazioneMultiplaProtocolli"));
			}
		} else if (request.getParameter("riassegnaProtocollo") != null) {
			request.setAttribute("protocolloId", new Integer(request
					.getParameter("riassegnaProtocollo")));
			return (mapping.findForward("riassegnaProtocollo"));

		}
		else if (request.getParameter("protocolloSelezionato") != null) {
			request.setAttribute("protocolloId", new Integer(request
					.getParameter("protocolloSelezionato")));
			return (mapping.findForward("visualizzaProtocolloIngresso"));
		} else if (request.getParameter("daFascicolare") != null) {
			request.setAttribute("protocolloId", new Integer(request
					.getParameter("daFascicolare")));
			session.setAttribute("PAGINA_RITORNO", "SCARICO_FATTURE");
			return (mapping.findForward("fascicolaProtocolloIngresso"));
		}
		else if (request.getParameter("downloadDocprotocolloSelezionato") != null) {
			String id = request
			.getParameter("downloadDocprotocolloSelezionato");
			int aooId=utente.getAreaOrganizzativa().getId();
			ReportProtocolloView prot = (ReportProtocolloView) scarico.getProtocolliScarico().get(id);
			return downloadDocumento(mapping, prot, request, response,aooId);
			
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		caricaLista(scarico,utente);
		logger.info("Execute ScaricoAction");
		return (mapping.findForward("input"));
	}


	private void caricaLista(ScaricoForm scarico,Utente utente) {
		ProtocolloDelegate delegate=ProtocolloDelegate.getInstance();
		Map<Long,ReportProtocolloView> protocolli = new HashMap<Long,ReportProtocolloView>();
		RegistroVO fatt=Organizzazione.getInstance().getRegistroByCod("Fatt");
		if (scarico.getTipoUtenteUfficio().equals("T")) {
			protocolli = delegate.getFatture(utente,
					fatt.getId(), "T");
		}
		if (scarico.getTipoUtenteUfficio().equals("U")) {
			protocolli = delegate.getFatture(utente,
					fatt.getId(), "U");
		}if (scarico.getTipoUtenteUfficio().equals("R")) {
			protocolli = delegate.getFatture(utente,
					fatt.getId(), "R");
		}
		scarico.setProtocolliScarico(protocolli);
	}
	

}
