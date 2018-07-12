package it.compit.fenice.mvc.presentation.action.protocollo;

import it.compit.fenice.model.protocollo.PostaInterna;
import it.compit.fenice.mvc.business.RepertorioDelegate;
import it.compit.fenice.mvc.presentation.actionform.protocollo.InviaRepertorioForm;
import it.compit.fenice.mvc.vo.repertori.DocumentoRepertorioVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.flosslab.mvc.presentation.action.protocollo.helper.file.ProtocolloFileUtility;
import it.flosslab.mvc.presentation.action.protocollo.helper.form.AggiornaProtocolloForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

public class InvioRepertorioAction extends Action {

	// ----------------------------------------------------- Instance Variables

	/**
	 * The <code>Log</code> instance for this application.
	 */
	static Logger logger = Logger.getLogger(InvioRepertorioAction.class
			.getName());
	
	
	
	public void aggiornaForm(PostaInterna protocollo, InviaRepertorioForm form,HttpSession session,Utente utente) {
		form.inizializzaForm();
		AggiornaProtocolloForm.aggiorna(protocollo, form, session, false, utente);
		form.setDocumentoVisibile(true);
		form.setDocumentoReadable(true);
	}
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession();	
		InviaRepertorioForm rForm = (InviaRepertorioForm) form;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		session.setAttribute("protocolloForm", rForm);
		Integer protocolloId = (Integer) request.getAttribute("protocolloId");
		if (protocolloId != null) {
			int id = protocolloId.intValue();
			PostaInterna protocollo = ProtocolloDelegate.getInstance()
					.getPostaInternaById(id);
			aggiornaForm(protocollo, rForm, session, utente);
		}
		RepertorioDelegate delegate = RepertorioDelegate.getInstance();
		rForm.setRepertori(delegate.getRepertoriByUfficio(utente.getUfficioInUso()));

		if (request.getParameter("repertorioId") != null) {
			rForm.setRepertorioId(Integer.valueOf(request.getParameter("repertorioId")).intValue());
		} else if (request.getParameter("btnInvioRepertorio") != null) {
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			} else {
				preparaDocumentoRepertorio(request, rForm, utente, errors);
				return mapping.findForward("inviaRepertorio");
			}

		} else if (request.getParameter("btnAnnullaAction") != null) {
			rForm.setRepertorioId(0);
		}else if (request.getParameter("downloadDocumentoPrincipale") != null) {
			DocumentoVO doc = rForm.getDocumentoPrincipale();
			int aooId = utente.getAreaOrganizzativa().getId();
			return downloadDocumento(mapping, doc, request, response, aooId);
		} 
		return (mapping.findForward("input"));
	}
	
	public ActionForward downloadDocumento(ActionMapping mapping,
			DocumentoVO doc, HttpServletRequest request,
			HttpServletResponse response, int aooId) throws Exception {
		ActionMessages errors = ProtocolloFileUtility.downloadFileProtocollo(doc, response, aooId);
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return mapping.findForward("erroreDownload");
		}
		ActionForward actionForward = new ActionForward();
		actionForward.setName("none");
		return actionForward;
	}
	
	public static void preparaDocumentoRepertorio(HttpServletRequest request,
			InviaRepertorioForm rForm, Utente utente, ActionMessages errors)
			throws DataException {
		DocumentoRepertorioVO vo=new DocumentoRepertorioVO();
		try {
			for(DocumentoVO doc: rForm.getDocumentiAllegatiCollection()){
				if (doc.getDescrizione()==null || "".equals(doc.getDescrizione()))
					doc.setDescrizione(doc.getFileName());
				doc.setPrincipale(false);
				vo.allegaDocumento(doc);
			}
			if(rForm.getDocumentoPrincipale().getId()!=null){
				DocumentoVO dp=rForm.getDocumentoPrincipale();
				if (dp.getDescrizione()==null || "".equals(dp.getDescrizione()))
					dp.setDescrizione(dp.getFileName());
				dp.setPrincipale(true);
				vo.allegaDocumento(dp);
			}
			vo.setProtocolloId(rForm.getProtocolloId());
			vo.setOggetto(rForm.getOggetto());
			vo.setRepId(rForm.getRepertorioId());
			request.setAttribute(Constants.DOCUMENTO_REPERTORIO, vo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException("Errore nella generazione del Documento di Repertorio");
		}
	}

}
