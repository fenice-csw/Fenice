package it.finsiel.siged.mvc.presentation.action.documentale;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.Fascicolo;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.AlberoUfficiBO;
import it.finsiel.siged.mvc.business.DocumentaleDelegate;
import it.finsiel.siged.mvc.business.FascicoloDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.business.UtenteDelegate;
import it.finsiel.siged.mvc.presentation.actionform.documentale.DocumentoForm;
import it.finsiel.siged.mvc.presentation.helper.DocumentoView;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.util.NumberUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

public class RicercaAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    static Logger logger = Logger.getLogger(RicercaAction.class.getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionMessages errors = new ActionMessages();// Report any errors we

        HttpSession session = request.getSession();
        DocumentaleDelegate delegate = DocumentaleDelegate.getInstance();

        DocumentoForm ricercaForm = (DocumentoForm) form;

        boolean indietroVisibile = false;
        ricercaForm.setIndietroVisibile(indietroVisibile);

        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
        boolean ufficioCompleto = (utente.getUfficioVOInUso().getTipo()
                .equals(UfficioVO.UFFICIO_CENTRALE));

        ricercaForm.setAooId(utente.getUfficioVOInUso().getAooId());
        MessageResources bundle = (MessageResources) request
                .getAttribute(Globals.MESSAGES_KEY);
        if (request.getParameter("btnCerca") != null) {
            errors = ricercaForm.validateParametriRicerca(mapping, request);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return (mapping.findForward("input"));
            }

            ricercaForm.setListaDocumenti(null);
            ricercaForm.setDocumentoSelezionato(0);
            if ("fascicoloDocumento".equals(session.getAttribute("provenienza"))) {
                indietroVisibile = true;
                ricercaForm.setIndietroVisibile(indietroVisibile);
            }
            int maxRighe = Integer.parseInt(bundle
                    .getMessage("documento.max.righe.lista"));
            HashMap hashMap = getParametriRicerca(request);
            int contaRighe = delegate.contaDocumenti(utente, hashMap, null);

            if (contaRighe <= maxRighe) {
                SortedMap documenti = new TreeMap();
                documenti = delegate.cercaDocumenti(utente, hashMap, null);

                if (documenti == null || documenti.size() == 0) {
                    if ("fascicoloDocumento".equals(session
                            .getAttribute("provenienza"))) {
                        indietroVisibile = true;
                        ricercaForm.setIndietroVisibile(indietroVisibile);
                    }
                    errors.add("nessun_dato", new ActionMessage("nessun_dato",
                            "", ""));
                } else {

                    ricercaForm.setListaDocumenti(documenti.values());
                }

            } else {
                errors.add("controllo.maxrighe", new ActionMessage(
                        "controllo.maxrighe", "" + contaRighe, "protocolli", ""
                                + maxRighe));
            }

        } else if (request.getParameter("btnAnnulla") != null) {
            session.removeAttribute("fascicoloDocumento");
            ricercaForm.setTipoDocumentoId(ProtocolloDelegate.getInstance()
                            .getDocumentoDefault(
                                    utente.getRegistroVOInUso().getAooId()));
            session.removeAttribute("tornaFascicolo");
            session.removeAttribute("fascicoloDocumento");
            session.removeAttribute("provenienza");
            ricercaForm.setListaDocumenti(null);
            ricercaForm.inizializzaForm();

        } else if (request.getParameter("btnCercaDaFascicolo") != null) {
            ricercaForm.setListaDocumenti(null);
            ricercaForm.inizializzaForm();
            indietroVisibile = true;
            ricercaForm.setIndietroVisibile(indietroVisibile);
        } else if (request.getParameter("impostaUfficioAction") != null) {
            logger.info("impostaUfficioAction: "
                    + request.getParameter("impostaUfficioAction"));
            ricercaForm.setUfficioCorrenteId(ricercaForm
                    .getUfficioSelezionatoId());
            AlberoUfficiBO.impostaUfficioUtenti(utente, ricercaForm,
                    ufficioCompleto);
            return mapping.findForward("input");

        } else if (request.getParameter("ufficioPrecedenteAction") != null) {
            ricercaForm.setUfficioCorrenteId(ricercaForm.getUfficioCorrente()
                    .getParentId());
            AlberoUfficiBO.impostaUfficioUtenti(utente, ricercaForm,
                    ufficioCompleto);

        } else if (request.getParameter("btnRicercaFullText") != null) {
            if ("fascicoloDocumento"
                    .equals(session.getAttribute("provenienza"))) {
                indietroVisibile = true;
                ricercaForm.setIndietroVisibile(indietroVisibile);
            }
            errors = ricercaForm.validateParametriRicerca(mapping, request);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return (mapping.findForward("input"));
            }
            String testo = request.getParameter("testo");
            SortedMap<Integer, DocumentoView>  documenti = new TreeMap<Integer, DocumentoView> ();
            ricercaForm.setListaDocumenti(null);
            ricercaForm.setDocumentoSelezionato(0);
            documenti = delegate.cercaDocumenti(utente,
                    getParametriRicerca(request), testo);

            if (documenti == null || documenti.size() == 0) {
                errors.add("nessun_dato", new ActionMessage("nessun_dato", "",
                        ""));
            } else {
                ricercaForm.setListaDocumenti(documenti.values());
            }
        } else if (request.getParameter("btnAggiungi") != null) {
            errors = ricercaForm.validateAggiungiDoc(mapping, request);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return (mapping.findForward("input"));
            }
            int docId = new Integer(request
                    .getParameter("documentoSelezionato")).intValue();
            Fascicolo fascicolo = (Fascicolo) session
                    .getAttribute(Constants.FASCICOLO);
            FascicoloDelegate.getInstance().salvaFascicoloDocumento(
                    fascicolo.getFascicoloVO(), docId,
                    utente.getValueObject().getUsername(), utente.getUfficioInUso());
            request.setAttribute("fascicoloId", fascicolo.getFascicoloVO()
                    .getId());
            session.removeAttribute("tornaFascicolo");
            return (mapping.findForward("tornaFascicolo"));

        } else if (request.getParameter("documentoSelezionato") != null) {
            request.setAttribute("documentoId", new Integer(request.getParameter("documentoSelezionato")));
            return (mapping.findForward("visualizzaDocumento"));
        } else if (request.getParameter("indietro") != null) {

            if (session.getAttribute("provenienza")
                    .equals("fascicoloDocumento")) {
                return mapping.findForward("tornaFascicolo");
            }
        } else if ("fascicoloDocumento".equals(session
                .getAttribute("provenienza"))) {
            indietroVisibile = true;
            ricercaForm.setIndietroVisibile(indietroVisibile);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("input");
            }
        }

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        ricercaForm.setRicercaFullText((Boolean.valueOf(bundle
                .getMessage("documentale.ricerca.fulltext"))).booleanValue());

        AlberoUfficiBO.impostaUfficioUtenti(utente, ricercaForm,
                ufficioCompleto);

        ricercaForm.setOwners(UtenteDelegate.getInstance().cercaUtenti(
                utente.getAreaOrganizzativa().getId().intValue(), null, null,
                null));
        logger.info("Execute RicercaAction");

        return mapping.getInputForward();

    } 

    public static HashMap getParametriRicerca(HttpServletRequest request)
            throws Exception {
        Date dataDocumentoDa;
        Date dataDocumentoA;

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        HashMap sqlDB = new HashMap();
        sqlDB.clear();

        if (request.getParameter("dataDocumentoDa") != null
                && !"".equals(request.getParameter("dataDocumentoDa"))) {
            dataDocumentoDa = df.parse(request.getParameter("dataDocumentoDa"));
            sqlDB.put("f.DATA_DOCUMENTO >= ?", dataDocumentoDa);
        }

        if (request.getParameter("dataDocumentoA") != null
                && !"".equals(request.getParameter("dataDocumentoA"))) {
            dataDocumentoA = df.parse(request.getParameter("dataDocumentoA"));
            sqlDB.put("f.DATA_DOCUMENTO <= ?", dataDocumentoA);
        }
        if (request.getParameter("nomeFile") != null
                && !"".equals(request.getParameter("nomeFile"))) {
            sqlDB.put("upper(f.NOME) LIKE ?", request.getParameter("nomeFile")
                    .toUpperCase());
        }
        if (request.getParameter("oggetto") != null
                && !"".equals(request.getParameter("oggetto"))) {
            sqlDB.put("upper(f.OGGETTO) LIKE ?", request
                    .getParameter("oggetto").toUpperCase());
        }

        if (request.getParameter("descrizioneArgomento") != null
                && !"".equals(request.getParameter("descrizioneArgomento"))) {
            sqlDB.put("upper(f.descrizione_argomento) LIKE ?", request
                    .getParameter("descrizioneArgomento").toUpperCase());
        }

        // todo: filtro Ricerca archivio Documentale
        if (request.getParameter("descrizione") != null
                && !"".equals(request.getParameter("descrizione"))) {
            sqlDB.put("upper(f.descrizione) LIKE ?", request.getParameter(
                    "descrizione").toUpperCase().trim());
        }

        if (request.getParameter("note") != null
                && !"".equals(request.getParameter("note"))) {
            sqlDB.put("upper(f.note) LIKE ?", request.getParameter("note")
                    .toUpperCase().trim());
        }

        if (request.getParameter("tipoDocumentoId") != null
                && !"".equals(request.getParameter("tipoDocumentoId"))) {
            if (NumberUtil.getInt(request.getParameter("tipoDocumentoId")) >= 0) {
                sqlDB.put("f.tipo_documento_id=?", request
                        .getParameter("tipoDocumentoId"));
            }
        }
        if (request.getParameter("statoDocumento") != null
                && !"".equals(request.getParameter("statoDocumento"))) {
            sqlDB.put("upper(f.STATO_ARC)=?", request.getParameter(
                    "statoDocumento").toUpperCase().trim());
        }

        if (Boolean.TRUE.equals(request.getSession().getAttribute(
                "tornaFascicolo"))) {

            Fascicolo fascicolo = (Fascicolo) request.getSession()
                    .getAttribute(Constants.FASCICOLO);
            // i documenti da cercare devono essere in archivio corrente
            sqlDB.put("f.stato_arc = ?", "C");
            // filtro i documenti da cercare escludendo quelli gi� collegati al
            // fascicolo
            if (fascicolo != null && fascicolo.getFascicoloVO() != null
                    && fascicolo.getFascicoloVO().getId() != null) {
                sqlDB.put("f.dfa_id NOT IN (SELECT dfa_id FROM "
                        + "fascicolo_documenti WHERE fascicolo_id =?)",
                        fascicolo.getFascicoloVO().getId());
            }

        }

        if (request.getParameter("ownerId") != null
                && !"".equals(request.getParameter("ownerId"))) {
            if (NumberUtil.getInt(request.getParameter("ownerId")) > 0) {
                sqlDB.put("f.OWNER_ID=?", request.getParameter("ownerId"));
            }
        }

        if (request.getParameter("ufficioCorrenteId") != null
                && !"".equals(request.getParameter("ufficioCorrenteId"))) {
            if (NumberUtil.getInt(request.getParameter("ufficioCorrenteId")) > 0) {

                if (request.getParameter("utenteSelezionatoId") != null
                        && !"".equals(request
                                .getParameter("utenteSelezionatoId"))
                        && NumberUtil.getInt(request
                                .getParameter("utenteSelezionatoId")) > 0) {
                    sqlDB.put("f.dfa_id IN (SELECT dfa_id FROM "
                            + "DOC_FILE_PERMESSI WHERE UFFICIO_ID ="
                            + NumberUtil.getInt(request
                                    .getParameter("ufficioCorrenteId"))
                            + " AND UTENTE_ID =?)", request
                            .getParameter("utenteSelezionatoId"));

                } else {
                    sqlDB.put("f.dfa_id IN (SELECT dfa_id FROM "
                            + "DOC_FILE_PERMESSI WHERE UFFICIO_ID =?)", request
                            .getParameter("ufficioCorrenteId"));

                }
            }
        }

        return sqlDB;
    }
}