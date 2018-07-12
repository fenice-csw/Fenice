package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.exception.EmailException;
import it.finsiel.siged.model.InvioFascicolo;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.ProtocolloUscita;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.business.DocumentaleDelegate;
import it.finsiel.siged.mvc.business.FascicoloDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.FascicoliArchivioForm;
import it.finsiel.siged.mvc.presentation.helper.ProtocolloFascicoloView;
import it.finsiel.siged.mvc.vo.documentale.FileVO;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.InvioFascicoliVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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

public class FascicoliArchivioAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    static Logger logger = Logger.getLogger(FascicoliArchivioAction.class
            .getName());

    // --------------------------------------------------------- Public Methods

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionMessages errors = new ActionMessages();
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
        FascicoliArchivioForm fascicoliArchivioForm = (FascicoliArchivioForm) form;

        if (fascicoliArchivioForm == null) {
            logger.info(" Creating new FascicoliArchivioForm");
            fascicoliArchivioForm = new FascicoliArchivioForm();
            request.setAttribute(mapping.getAttribute(), fascicoliArchivioForm);
        }

        if (request.getParameter("cancella") != null) {
            errors = fascicoliArchivioForm.validate(mapping, request);
            if (errors.isEmpty()) {
            }

        } else if (request.getParameter("protocolla") != null) {
            errors = fascicoliArchivioForm.validate(mapping, request);
            if (errors.isEmpty()) {
                try {
                    preparaProtocollo(request, fascicoliArchivioForm, session,
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
            fascicoliArchivioForm.setFascicoliInviati(FascicoloDelegate
                    .getInstance().getFascicoliArchivioInvio(
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
            FascicoliArchivioForm form, HttpSession session, Utente utente)
            throws EmailException {

        ProtocolloUscita pu = ProtocolloBO.getDefaultProtocolloUscita(utente);

        try {
            ProtocolloVO protocollo = pu.getProtocollo();
            protocollo
                    .setTipoDocumentoId(ProtocolloDelegate.getInstance()
                            .getDocumentoDefault(
                                    utente.getRegistroVOInUso().getAooId()));
            int fascicoloId = form.getFascicoloSelezionatoId();
            pu.setFascicoloInvioId(fascicoloId);
            InvioFascicolo iFascicolo = FascicoloDelegate.getInstance()
                    .getFascicoloInviatoById(fascicoloId);

            // imposto gli allacci
            Collection<AllaccioVO> allacciInvioProtocollo = new ArrayList<AllaccioVO>();
            Collection<ProtocolloFascicoloView> allacci = FascicoloDelegate.getInstance()
                    .getProtocolliFascicolo(fascicoloId, utente);
            Iterator<ProtocolloFascicoloView> iterator = allacci.iterator();
            while (iterator.hasNext()) {
                AllaccioVO allaccio = new AllaccioVO();
                ProtocolloFascicoloView p =iterator.next();
                allaccio.setProtocolloAllacciatoId(p.getProtocolloId());
                allaccio.setPrincipale(false);
                allaccio.setAllaccioDescrizione(p.getNumeroProtocollo() + "/"
                        + p.getDataProtocollo().substring(6, 10) + " ("
                        + p.getTipoProtocollo() + ")");
                allacciInvioProtocollo.add(allaccio);
            }
            pu.setAllacci(allacciInvioProtocollo);

            // destinatari fascicolo
            pu.setDestinatari(iFascicolo.getDestinatari());

            Iterator<InvioFascicoliVO> i = iFascicolo.getDocumenti().iterator();
            FileVO f = null;
            int utenteMittenteId = 0;
            int ufficioMittenteId = 0;
            while (i.hasNext()) {
                InvioFascicoliVO ifVO =i.next();
                f = DocumentaleDelegate.getInstance().getDocumentoById(
                        ifVO.getDocumentoId()).getFileVO();
                ufficioMittenteId = ifVO.getUfficioMittenteId();
                utenteMittenteId = ifVO.getUtenteMittenteId();
                if (ifVO.getFlagDocumentoPrincipale().equals("1")) {
                    protocollo.setOggetto(f.getOggetto());
                    protocollo.setDataDocumento(f.getDataDocumento());
                    pu.setDocumentoPrincipale(f.getDocumentoVO());
                } else {
                    if (f.getDocumentoVO().getDescrizione() == null
                            || f.getDocumentoVO().getDescrizione().trim()
                                    .equals(""))
                        f.getDocumentoVO().setDescrizione(
                                f.getDocumentoVO().getFileName());
                    pu.allegaDocumento(f.getDocumentoVO());
                }

            }

            Iterator<DocumentoVO> ite = pu.getAllegati().values().iterator();
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

            while (ite.hasNext()) {
                doc = ite.next();
                doc.setMustCreateNew(true);
                tempFile = File.createTempFile("tmp_doc", ".fascicolo",
                        new File(tempFolder));
                os = new FileOutputStream(tempFile.getAbsolutePath());
                DocumentaleDelegate.getInstance().writeDocumentToStream(
                        doc.getId().intValue(), os);
                os.close();
                doc.setPath(tempFile.getAbsolutePath());
            }

            AssegnatarioVO mittente = new AssegnatarioVO();
            mittente.setUfficioAssegnatarioId(ufficioMittenteId);
            mittente.setUtenteAssegnatarioId(utenteMittenteId);
            pu.setMittente(mittente);
            pu.setProtocollo(protocollo);

            request.setAttribute(Constants.PROTOCOLLO_USCITA_ARCHIVIO, pu);
        } catch (Exception e) {
            throw new EmailException("Errore nella generazione del Protocollo");
        }
    }

}