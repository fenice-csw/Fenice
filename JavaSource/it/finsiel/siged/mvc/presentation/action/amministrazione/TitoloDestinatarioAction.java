package it.finsiel.siged.mvc.presentation.action.amministrazione;

import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.mvc.business.AmministrazioneDelegate;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.TitoloDestinatarioForm;
import it.finsiel.siged.mvc.vo.IdentityVO;

import java.util.Collection;

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

/**
 * Implementation of <strong>Action </strong> to create a new
 * TitoloDestinatario.
 * 
 * 
 * 
 */

public final class TitoloDestinatarioAction extends Action {

    static Logger logger = Logger.getLogger(TitoloDestinatarioAction.class
            .getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionMessages errors = new ActionMessages();

        HttpSession session = request.getSession(true);

        IdentityVO titoloVO = new IdentityVO();

        TitoloDestinatarioForm titoloDestinatarioForm = (TitoloDestinatarioForm) form;

        if (form == null) {
            logger.info("Creating new TitoloDestinatarioForm");
            form = new TitoloDestinatarioForm();
            session.setAttribute(mapping.getAttribute(), form);
        }

        logger.info("Siamo entrati in TitoloDestinatarioAction");

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            logger.info("Ci sono errori... Riga 56");
            return (mapping.findForward("input"));
        }

        AmministrazioneDelegate td = AmministrazioneDelegate.getInstance();
        Collection<IdentityVO> titoli = td.getElencoTitoliDestinatario();
        if (titoli != null) {
            titoloDestinatarioForm.setTitoli(titoli);
        }

        if (request.getParameter("btnModifica") != null
                && request.getParameter("id") == null) {
            errors.add("titolo", new ActionMessage("selezione.obbligatoria",
                    "il titolo", ""));

        } else if (request.getParameter("btnModifica") != null
                && request.getParameter("id") != null) {
            int id = Integer.parseInt(request.getParameter("id"));
            titoloVO = td.getTitoloDestinatario(id);
            titoloDestinatarioForm.setId(id);
            titoloDestinatarioForm.setDescrizione(titoloVO.getDescription());
            return (mapping.findForward("titolo"));
        } else if (request.getParameter("btnCancella") != null
                && request.getParameter("id") != null) {
            errors = titoloDestinatarioForm.validate(mapping, request);
            int id = Integer.parseInt(request.getParameter("id"));
            boolean esiste = esisteInProtocolloDestinatari(id);

            if (esiste) {
                errors
                        .add(
                                "titolo",
                                new ActionMessage("record_non_cancellabile",
                                        "il titolo",
                                        ": esiste almeno un destinatario protocollato che fa riferimento ad esso."));
                saveErrors(request, errors);
                return (mapping.findForward("input"));
            } else {
                // Verificare l'esistenza di vincoli nella tabella
                // Storia_Protocollo_Destinatari
                esiste = esisteInStoriaProtocolloDestinatari(id);
                if (esiste) {
                    errors
                            .add(
                                    "titolo",
                                    new ActionMessage(
                                            "record_non_cancellabile",
                                            "il titolo",
                                            ": nell'archivio storico destinatari esiste almeno un destinatario protocollato che fa riferimento ad esso."));
                    saveErrors(request, errors);
                    return (mapping.findForward("input"));
                } else {
                    // Verificare l'esistenza di vincoli nella tabella
                    // Invio_classificati_destinatari
                    esiste = esisteInInvioClassificatiDestinatari(id);
                    if (esiste) {
                        errors
                                .add(
                                        "titolo",
                                        new ActionMessage(
                                                "record_non_cancellabile",
                                                "il titolo",
                                                ": nella tabella di invio classificati destinatari esiste almeno un destinatario protocollato che fa riferimento ad esso."));
                        saveErrors(request, errors);
                        return (mapping.findForward("input"));
                    } else {
                        // Verificare l'esistenza di vincoli nella tabella
                        // Invio_fascicoli_destinatari
                        esiste = esisteInInvioFascicoliDestinatari(id);
                        if (esiste) {
                            errors
                                    .add(
                                            "titolo",
                                            new ActionMessage(
                                                    "record_non_cancellabile",
                                                    "il titolo",
                                                    ": nella tabella di invio fascicoli destinatari esiste almeno un destinatario protocollato che fa riferimento ad esso."));
                            saveErrors(request, errors);
                            return (mapping.findForward("input"));
                        } else {
                            // Posso eliminare il titolo, poich� non ci sono
                            // vincoli
                            boolean eliminato = false;
                            eliminato = td.deleteTitolo(id);
                            if (eliminato) {
                                errors
                                        .add(
                                                "titolo",
                                                new ActionMessage(
                                                        "cancellazione_ok",
                                                        "il titolo",
                                                        ": nella tabella di invio fascicoli destinatari esiste almeno un destinatario protocollato che fa riferimento ad esso."));
                                saveErrors(request, errors);

                            }
                            titoloDestinatarioForm.setTitoli(td
                                    .getElencoTitoliDestinatario());
                            titoloDestinatarioForm.setId(0);

                        }
                    }
                }

            }

            return (mapping.findForward("input"));
        } else if (request.getParameter("btnSalva") != null) {
            // validazione dei dati
            errors = titoloDestinatarioForm.validate(mapping, request);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("input");
            }
            // fine validazioni
            titoloVO.setId(titoloDestinatarioForm.getId());
            titoloVO.setDescription(titoloDestinatarioForm.getDescrizione());
            titoloVO = td.salvaTitoloDestinatario(titoloVO);
            // titoloDestinatarioForm.setTitoli(td.getElencoTitoliDestinatario());

            if (titoloVO.getReturnValue() == ReturnValues.SAVED) {
                titoloDestinatarioForm.setTitoli(td
                        .getElencoTitoliDestinatario());
                titoloDestinatarioForm.setId(0);
                return (mapping.findForward("input"));
            } else {
                errors.add("registrazione_titolo", new ActionMessage(
                        "record_non_inseribile", "il titolo",
                        ": ne esiste gi� uno identico"));
                saveErrors(request, errors);
                return (mapping.findForward("titolo"));
            }

        } else if (request.getParameter("btnNuovo") != null) {
            titoloDestinatarioForm.setId(0);
            return (mapping.findForward("titolo"));
        }

        logger.info("Execute TitoliDestinatario");

        return mapping.getInputForward();

    }

    private boolean esisteInProtocolloDestinatari(int id) {
        boolean esiste = false;
        AmministrazioneDelegate ad = AmministrazioneDelegate.getInstance();
        esiste = ad.esisteTitoloInProtocolloDestinatari(id);
        return esiste;
    }

    private boolean esisteInStoriaProtocolloDestinatari(int id) {
        boolean esiste = false;
        AmministrazioneDelegate ad = AmministrazioneDelegate.getInstance();
        esiste = ad.esisteTitoloInStoriaProtDest(id);
        return esiste;
    }

    private boolean esisteInInvioClassificatiDestinatari(int id) {
        boolean esiste = false;
        AmministrazioneDelegate ad = AmministrazioneDelegate.getInstance();
        esiste = ad.esisteInInvioClassificatiProtDest(id);
        return esiste;
    }

    private boolean esisteInInvioFascicoliDestinatari(int id) {
        boolean esiste = false;
        AmministrazioneDelegate ad = AmministrazioneDelegate.getInstance();
        esiste = ad.esisteInInvioFascicoliDestinatari(id);
        return esiste;
    }

}