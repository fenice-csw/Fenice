
package it.finsiel.siged.test;

import it.finsiel.siged.model.organizzazione.AreaOrganizzativa;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.ProtocolloIngresso;
import it.finsiel.siged.mvc.bo.OrganizzazioneBO;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.bo.RegistroBO;
import it.finsiel.siged.mvc.bo.UfficioBO;
import it.finsiel.siged.mvc.business.OrganizzazioneDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.business.RegistroDelegate;
import it.finsiel.siged.mvc.business.UtenteDelegate;
import it.finsiel.siged.mvc.presentation.action.protocollo.ProtocolloIngressoAction;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.util.FileUtil;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

public class TestProtocollazione extends ProtocolloIngressoAction {

    static Logger logger = Logger.getLogger(ProtocolloIngressoAction.class
            .getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        PrintWriter ps = response.getWriter();
        try {
            ActionMessages errors = new ActionMessages();
            UtenteVO utenteVO = UtenteDelegate.getInstance().getUtente(
                    "admin", "111111");
            Utente utente = new Utente(utenteVO);
            RegistroDelegate registroDelegate = RegistroDelegate.getInstance();
            OrganizzazioneDelegate organizzazioneDelegate = OrganizzazioneDelegate
                    .getInstance();
            Map registri = registroDelegate.getRegistriUtente(utenteVO.getId()
                    .intValue());
            utente.setRegistri(registri);
            utente.setRegistroUfficialeId(RegistroBO
                    .getRegistroUfficialeId(registri.values()));
            Organizzazione organizzazione = Organizzazione.getInstance();
            AreaOrganizzativa aoo = organizzazione
                    .getAreaOrganizzativa(utenteVO.getAooId());
            HashMap uffici = OrganizzazioneBO
                    .getUfficiUtente(organizzazioneDelegate
                            .getIdentificativiUffici(utente.getValueObject()
                                    .getId().intValue()));
            utente.setUffici(uffici);
            utente.setRegistroInUso((RegistroBO.getUnicoRegistro(utente
                    .getRegistri())).getId().intValue());
            utente.setUfficioInUso((UfficioBO.getUnicoUfficio(utente
                    .getUffici())).getId().intValue());
            ProtocolloIngresso protocolloIngresso = ProtocolloBO
                    .getDefaultProtocolloIngresso(utente);
            ProtocolloVO protocollo = protocolloIngresso.getProtocollo();
            protocollo.setTipoDocumentoId(1);
            protocollo.setOggetto("test oggetto");
            SoggettoVO soggetto = new SoggettoVO("F");
            soggetto.setCognome("Rossi");
            soggetto.setNome("Giuseppe");
            soggetto.setAoo(protocollo.getAooId());
            protocollo.setCognomeMittente("Rossi");
            protocollo.setNomeMittente("Giuseppe");
            protocolloIngresso.setMittente(soggetto);
            AssegnatarioVO assegnatario = new AssegnatarioVO();
            assegnatario.setId(103);
            assegnatario.setUfficioAssegnanteId(1);
            assegnatario.setUtenteAssegnanteId(1);
            assegnatario.setUfficioAssegnatarioId(1);
            assegnatario.setCompetente(true);
            protocolloIngresso.aggiungiAssegnatario(assegnatario);

            DocumentoVO documento = uploadFile(request, response, errors,
                    utente.getValueObject().getUsername());
            if (documento != null)
                protocolloIngresso.setDocumentoPrincipale(documento);
            ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
            ProtocolloVO newProtocollo = delegate.registraProtocollo(
                    protocolloIngresso, utente, false);
            // ProtocolloVO newProtocollo =
            // delegate.registraProtocolloIngresso(protocolloIngresso, utente);
            logger.info("Protocollo Salvato:" + newProtocollo.getId());

        } catch (Exception e) {
            logger.error("", e);
            ps.print("Errore.");
        }

        return null;
    }

    private DocumentoVO uploadFile(HttpServletRequest request,
            HttpServletResponse response, ActionMessages errors, String username) {
        DocumentoVO documento = new DocumentoVO();
        try {
            // boolean isMultipart = DiskFileUpload.isMultipartContent(request);
            // logger.info("Multipart="+isMultipart);
            // DiskFileUpload upload = new DiskFileUpload();
            // upload.setRepositoryPath("C:\\tmp_upload\\");
            // List items = upload.parseRequest(request);
            // Iterator iter = items.iterator();
            // while (iter.hasNext()) {
            // DefaultFileItem item = (DefaultFileItem) iter.next();
            File tempFile = File.createTempFile("temp_", ".upload", new File(
                    "C:\\temp\\"));
            // item.write(tempFile);
            FileUtil.copyFile(new File("C:\\test.pdf"), tempFile);
            documento.setDescrizione(null);
            documento.setFileName(tempFile.getName());
            documento.setPath(tempFile.getAbsolutePath());
            documento.setImpronta(FileUtil.calcolaDigest(tempFile
                    .getAbsolutePath(), errors));
            documento.setSize((int) tempFile.length());
            documento.setContentType("application/pdf");
            documento.setRowCreatedTime(new Date(System.currentTimeMillis()));
            documento.setRowUpdatedTime(new Date(System.currentTimeMillis()));
            documento.setRowCreatedUser(username);
            documento.setRowUpdatedUser(username);
            // item.delete();
            // }
        } catch (Exception e) {
            logger.error("", e);
            documento = null;
        }
        return documento;
    }
}
