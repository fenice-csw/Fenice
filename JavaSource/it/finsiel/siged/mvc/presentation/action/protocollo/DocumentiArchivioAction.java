package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.exception.EmailException;
import it.finsiel.siged.model.InvioClassificati;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.ProtocolloUscita;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.business.DocumentaleDelegate;
import it.finsiel.siged.mvc.business.ProcedimentoDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.DocumentiArchivioForm;
import it.finsiel.siged.mvc.vo.documentale.FileVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.InvioClassificatiVO;
import it.finsiel.siged.mvc.vo.protocollo.ProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;

import java.io.File;
import java.io.FileOutputStream;
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

public class DocumentiArchivioAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    static Logger logger = Logger.getLogger(DocumentiArchivioAction.class
            .getName());

    // --------------------------------------------------------- Public Methods

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionMessages errors = new ActionMessages();
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
        DocumentiArchivioForm documentiArchivioForm = (DocumentiArchivioForm) form;

        if (documentiArchivioForm == null) {
            logger.info(" Creating new DocumentiArchivioForm");
            documentiArchivioForm = new DocumentiArchivioForm();
            request.setAttribute(mapping.getAttribute(), documentiArchivioForm);
        }

        if (request.getParameter("cancella") != null) {
            errors = documentiArchivioForm.validate(mapping, request);
            if (errors.isEmpty()) {
               
            }

        } else if (request.getParameter("protocolla") != null) {
            if (request.getAttribute("documentoSelezionatoId") != null) {
                documentiArchivioForm.setDocumentoSelezionatoId(Integer
                        .parseInt((String) request
                                .getAttribute("documentoSelezionatoId")));
            }
            errors = documentiArchivioForm.validate(mapping, request);
            if (errors.isEmpty()) {
                try {
                    preparaProtocollo(request, documentiArchivioForm, session,
                            utente);
                    saveToken(request);
                    return (mapping.findForward("protocollazione"));
                } catch (Exception e) {
                    errors.add("general", new ActionMessage(
                            "errore_nel_salvataggio"));
                }
            }
            saveErrors(request, errors);

        }

        try {
            documentiArchivioForm.setDocumentiInviati(DocumentaleDelegate
                    .getInstance().getDocumentiArchivioInvio(
                            utente.getValueObject().getAooId()));
        } catch (Exception e) {
            errors.add("general",
                    new ActionMessage("error.database.cannotload"));
        }

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        return (mapping.findForward("input"));
    }

    public void preparaProtocollo(HttpServletRequest request,
            DocumentiArchivioForm form, HttpSession session, Utente utente)
            throws EmailException {

        ProtocolloUscita pu = ProtocolloBO.getDefaultProtocolloUscita(utente);

        try {
            ProtocolloVO protocollo = pu.getProtocollo();
            int documentoId = form.getDocumentoSelezionatoId();
            InvioClassificati invioClass = DocumentaleDelegate.getInstance()
                    .getDocumentoClassificatoById(documentoId);
           // pu.
            pu.setDocumentoInvioId(documentoId);
            pu.setDestinatari(invioClass.getDestinatari());
            pu.setFascicoli(invioClass.getFascicoliCollection());
            //pu.setP
            //protocollo.
            InvioClassificatiVO icVO = invioClass.getIcVO();
            int utenteMittenteId = icVO.getUtenteMittenteId();
            int ufficioMittenteId = icVO.getUfficioMittenteId();
            if ((ufficioMittenteId == 0) && (utenteMittenteId == 0)) {
                utenteMittenteId = utente.getValueObject().getId().intValue();
                ufficioMittenteId = utente.getUfficioInUso();
            }

            FileVO f = null;
            f = DocumentaleDelegate.getInstance().getDocumentoById(documentoId)
                    .getFileVO();
            protocollo.setOggetto(f.getOggetto());
            protocollo.setDataDocumento(f.getDataDocumento());
            pu.setDocumentoPrincipale(f.getDocumentoVO());

            String tempFolder = utente.getValueObject().getTempFolder();
            File tempFile = null;
            OutputStream os = null;

            DocumentoVO doc = pu.getDocumentoPrincipale();
            tempFile = File.createTempFile("tmp_doc", ".fascicolo", new File(
                    tempFolder));
            os = new FileOutputStream(tempFile.getAbsolutePath());
            DocumentaleDelegate.getInstance().writeDocumentToStream(
                    doc.getId().intValue(), os);
            os.close();
            doc.setPath(tempFile.getAbsolutePath());
            doc.setMustCreateNew(true);

            AssegnatarioVO mittente = new AssegnatarioVO();
            mittente.setUfficioAssegnatarioId(ufficioMittenteId);
            mittente.setUtenteAssegnatarioId(utenteMittenteId);
            pu.setMittente(mittente);
            pu.setProtocollo(protocollo);
            pu.getProtocollo().setStatoProtocollo("P");
            if(invioClass.getIcVO().getProcedimentoId()!=0)
            	preparaProtocolloProcedimento(pu,invioClass.getIcVO().getProcedimentoId());
            if(invioClass.getIcVO().getDataScadenza()!=null)
            	pu.getProtocollo().setDataScadenza(invioClass.getIcVO().getDataScadenza());
            if(invioClass.getIcVO().getTextScadenza()!=null && !invioClass.getIcVO().getTextScadenza().equals(""))
            	pu.getProtocollo().setEstermiAutorizzazione(invioClass.getIcVO().getTextScadenza());
            request.setAttribute(Constants.PROTOCOLLO_USCITA_ARCHIVIO, pu);
        } catch (Exception e) {
        	e.printStackTrace();
            throw new EmailException("Errore nella generazione del Protocollo");
        }
    }

	private static void preparaProtocolloProcedimento(ProtocolloUscita pu,
			int procedimentoId) {
		ProcedimentoVO newVo=ProcedimentoDelegate.getInstance().getProcedimentoVO(procedimentoId);
		ProtocolloProcedimentoVO proc = new ProtocolloProcedimentoVO();
		proc.setProcedimentoId(newVo.getId().intValue());
		proc.setFascicoloId(newVo.getFascicoloId());
		//proc.setProtocolloId(pu.getProtocollo().getId());
		proc.setNumeroProcedimento(newVo.getNumeroProcedimento());
		proc.setOggetto(newVo.getOggetto());
		pu.getProcedimenti().add(proc);
		/*
		Fascicolo fascicolo = FascicoloDelegate.getInstance().getFascicoloById(
				proc.getFascicoloId());
		pu.getProtocollo().getFascicoli().add(fascicolo.getFascicoloVO());
		*/
	}
}