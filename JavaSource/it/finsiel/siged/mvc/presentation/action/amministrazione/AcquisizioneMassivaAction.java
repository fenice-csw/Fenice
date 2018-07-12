package it.finsiel.siged.mvc.presentation.action.amministrazione;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.AmministrazioneDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.AcquisizioneMassivaForm;
import it.finsiel.siged.mvc.vo.log.LogAcquisizioneMassivaVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.util.FileUtil;
import it.finsiel.siged.util.NumberUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
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

public class AcquisizioneMassivaAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    static Logger logger = Logger.getLogger(AcquisizioneMassivaAction.class
            .getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // l'algoritmo EAN-13 tiene conto del CODICE_SELF_CHECKING e genera un
        // codice a barre numerico di lunghezza
        // 13 di cui l'ultimo carattere � di controllo quindi va escluso nel
        // momento in cui si va a calcolare il
        // nome del file a partire dal codice a barre:
        // final int CODICE_SELF_CHECKING = 0 TENGO CONTO ANCHE DELL'ULTIMA
        // CIFRA NUMERICA PER ID_PROTOCOLLO
        // final int CODICE_SELF_CHECKING = 1;ELIMINO L'ULTIMA CIFRA NUMERICA
        // PER IL CALCOLO DELL'ID_PROTOCOLLO
        final int CODICE_SELF_CHECKING = 0;

        final String DOCUMENTO_SCARTATO_NO_MATCHING = "Non esiste il protocollo per il documento";
        final String DOCUMENTO_SCARTATO_ERRORE_NOME_FILE = "Il nome del file non � convenzionale";
        final String DOCUMENTO_SCARTATO_ESISTE_IL_DOCUMENTO = "Il protocollo cui fa riferimento ha gi� un documento";

        ActionMessages errors = new ActionMessages();// Report any errors we

        HttpSession session = request.getSession();

        AcquisizioneMassivaForm acquisizioneMassivaForm = (AcquisizioneMassivaForm) form;

        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);

        Organizzazione org = Organizzazione.getInstance();
        String dirDocAoo = org.getValueObject().getPathDocAquisMassiva() + "/aoo_"
                + String.valueOf(utente.getRegistroVOInUso().getAooId());

        ProtocolloDelegate pd = ProtocolloDelegate.getInstance();

        if (form == null) {
            logger.info(" Creating new AcquisizioneMassivaAction");
            form = new AcquisizioneMassivaForm();
            session.setAttribute(mapping.getAttribute(), form);
        }
        if (request.getParameter("btnConferma") != null) {
            String userName = utente.getValueObject().getUsername();
            Collection<File> listaDocumenti = new ArrayList<File>(FileUtil.getFilePathDoc(dirDocAoo));
            Collection<LogAcquisizioneMassivaVO> documentiScartati = new ArrayList<LogAcquisizioneMassivaVO>();
            HashMap<Integer,DocumentoVO> documenti = new HashMap<Integer,DocumentoVO>(2);
            String fileName;
            String absoluteName;
            int protocolloId;
            try {
                for (Iterator<File> i = listaDocumenti.iterator(); i.hasNext();) {
                    File f = i.next();
                    fileName = f.getName();
                    // tipoFile = fileName
                    // .substring(fileName.lastIndexOf(".")+1);
                    absoluteName = fileName.substring(0, fileName
                            .lastIndexOf(".")
                            - CODICE_SELF_CHECKING);
                    if (NumberUtil.isInteger(absoluteName)) {
                        protocolloId = Integer.parseInt(absoluteName);
                        ProtocolloVO pVO = pd.getProtocolloVOById(protocolloId);
                        if (pVO.getDocumentoPrincipaleId() != null) {
                            LogAcquisizioneMassivaVO logVO = getLog(utente,
                                    DOCUMENTO_SCARTATO_ESISTE_IL_DOCUMENTO,
                                    fileName);
                            documentiScartati.add(logVO);
                            AmministrazioneDelegate.getInstance()
                                    .newLogAcquisizioneMassiva(logVO);               
                        } else if (pVO.getReturnValue() == ReturnValues.FOUND) {
                           
                            DocumentoVO doc = new DocumentoVO();
                            doc.setFileName(fileName);
                            doc.setImpronta(FileUtil.calcolaDigest(dirDocAoo
                                    + "/" + fileName, errors));
                            doc.setPath(dirDocAoo + "/" + fileName);
                            doc.setRowCreatedTime(new Date(System
                                    .currentTimeMillis()));
                            doc.setRowUpdatedTime(new Date(System
                                    .currentTimeMillis()));
                            doc.setRowCreatedUser(userName);
                            doc.setRowUpdatedUser(userName);
                            doc.setSize((new Long(f.length())).intValue());
                            documenti.put(new Integer(protocolloId), doc);

                        } else {
                            LogAcquisizioneMassivaVO logVO = getLog(utente,
                                    DOCUMENTO_SCARTATO_NO_MATCHING, fileName);
                            documentiScartati.add(logVO);
                            AmministrazioneDelegate.getInstance()
                                    .newLogAcquisizioneMassiva(logVO);

                            
                        }
                    } else {
                        LogAcquisizioneMassivaVO logVO = getLog(utente,
                                DOCUMENTO_SCARTATO_ERRORE_NOME_FILE, fileName);
                        documentiScartati.add(logVO);
                        AmministrazioneDelegate.getInstance()
                                .newLogAcquisizioneMassiva(logVO);

                  
                    }
                }

                if (documenti.size() > 0) {
                    if (pd.acquisizioneMassiva(utente, documenti) == ReturnValues.SAVED) {
                        errors.add("logs", new ActionMessage("operazione_ok",
                                "", ""));
                        cancellaFile(documenti.values(), dirDocAoo);
                    } else {
                        errors.add("logs", new ActionMessage(
                                "errore_nel_salvataggio", "", ""));

                    }
                }
                acquisizioneMassivaForm.setLogsAcquisizione(documentiScartati);
                acquisizioneMassivaForm.setDocumentiAcquisiti(new Integer(
                        documenti.size()));
                return (mapping.findForward("logs"));

            } catch (NumberFormatException e) {
                errors.add("logs", new ActionMessage("errore_nel_salvataggio",
                        "", ""));
                saveErrors(request, errors);

                e.printStackTrace();
            } catch (Exception e) {
                errors.add("logs", new ActionMessage("errore_nel_salvataggio",
                        "", ""));
                saveErrors(request, errors);

                e.printStackTrace();
            }
        } else if (request.getParameter("btnAnnulla") != null) {
            return (mapping.findForward("listaDocumenti"));

        } else if (request.getParameter("btnLogs") != null) {
            acquisizioneMassivaForm.setDocumentiAcquisiti(null);
            acquisizioneMassivaForm.setLogsAcquisizione(AmministrazioneDelegate
                    .getInstance().getLogsAcquisizioneMassiva(
                            utente.getRegistroVOInUso().getAooId()));

            return (mapping.findForward("logs"));

        } else if (request.getParameter("btnCancellaFiles") != null) {
            String[] documentiDaEliminare = acquisizioneMassivaForm
                    .getDocumentiDaEliminare();
            if (documentiDaEliminare != null) {
                for (int i = 0; i < documentiDaEliminare.length; i++) {
                    if (documentiDaEliminare[i] != null) {
                        File f = new File(dirDocAoo + "/"
                                + documentiDaEliminare[i]);
                        f.delete();
                    }
                }
            }
        } else if (request.getParameter("btnCancellaLogs") != null) {
            if (AmministrazioneDelegate.getInstance()
                    .cancellaLogsAcquisizioneMassiva(
                            utente.getRegistroVOInUso().getAooId())) {
                errors.add("cancellaLogs", new ActionMessage(
                        "cancellazione_ok", "", ""));
                saveErrors(request, errors);
            }

            return (mapping.findForward("listaDocumenti"));
        }
        if (FileUtil.gestionePathDoc(dirDocAoo) != ReturnValues.INVALID) {
            if (FileUtil.getFilePathDoc(dirDocAoo) != null) {
                cancellaFileTMP(FileUtil.getFilePathDoc(dirDocAoo));
                acquisizioneMassivaForm.setDocumentiAcquisizione(FileUtil
                        .getFilePathDoc(dirDocAoo));
            } else {
                errors.add("acquisizioneMassiva", new ActionMessage(
                        "errore_nel_salvataggio", "", ""));
            }
        } else {

            errors.add("acquisizioneMassiva", new ActionMessage(
                    "error.directory.acquisizione.massiva", "", ""));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        logger.info("Execute AcquisizioneMassivaAction");

        return (mapping.findForward("input"));

    }

    private LogAcquisizioneMassivaVO getLog(Utente utente, String Errore,
            String nomeFile) {
        LogAcquisizioneMassivaVO logVO = new LogAcquisizioneMassivaVO();
        logVO.setAooId(utente.getRegistroVOInUso().getAooId());
        logVO.setErrore(Errore);
        logVO.setFileName(nomeFile);
        logVO.setData(new Date(System.currentTimeMillis()));
        return logVO;
    }

    private void cancellaFile(Collection<DocumentoVO> listaDocumenti, String dirDocAoo) {
        boolean cancellato = false;
        for (Iterator<DocumentoVO> i = listaDocumenti.iterator(); i.hasNext();) {
            DocumentoVO doc = i.next();
            File f = new File(dirDocAoo + "/" + doc.getFileName());
            cancellato = f.delete();
            logger.info("Cancellazione file " + doc.getFileName() + ": "
                    + cancellato);
        }
    }

    private void cancellaFileTMP(Collection<File> listaDocumenti) {
        for (Iterator<File> i = listaDocumenti.iterator(); i.hasNext();) {
            File f = i.next();
            if ("tmp".equalsIgnoreCase(f.getName().substring(
                    f.getName().lastIndexOf(".") + 1)))
                f.delete();
        }

    }
}