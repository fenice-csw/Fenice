package it.finsiel.siged.mvc.presentation.action.amministrazione;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.AmministrazioneDelegate;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.TipoDocumentoForm;
import it.finsiel.siged.mvc.vo.lookup.TipoDocumentoVO;

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
 * Implementation of <strong>Action </strong> to create a new E-Photo Utente.
 * 
 * @author Almaviva sud.
 * 
 */

public class TipoDocumentoAction extends Action {

    static Logger logger = Logger
            .getLogger(TipoDocumentoAction.class.getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionMessages errors = new ActionMessages();// Report any errors we

        HttpSession session = request.getSession(true); // we create one if does
        AmministrazioneDelegate delegate = AmministrazioneDelegate
                .getInstance();
        TipoDocumentoForm tipoDocumentoForm = (TipoDocumentoForm) form;

        TipoDocumentoVO tipoDocumentoVO = new TipoDocumentoVO();
        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);

        if (form == null) {
            logger.info(" Creating new TipoDocumentoAction");
            form = new TipoDocumentoForm();
            session.setAttribute(mapping.getAttribute(), form);
        }
        tipoDocumentoForm.setTipiDocumento(delegate.getTipiDocumento(utente
                .getRegistroVOInUso().getAooId()));

        if (request.getParameter("btnNuovoTipoDocumento") != null) {
            tipoDocumentoForm.inizializzaForm();
            return (mapping.findForward("edit"));

        } else if (request.getParameter("btnAnnulla") != null) {
            tipoDocumentoForm.inizializzaForm();
            return (mapping.findForward("input"));

        } else if (request.getParameter("btnConferma") != null) {
            caricaDatiNelVO(tipoDocumentoVO, tipoDocumentoForm, utente);
            AmministrazioneDelegate.getInstance().salvaTipoDocumento(tipoDocumentoVO);
            if (tipoDocumentoVO.getReturnValue() == ReturnValues.SAVED) {
                aggiornaLookupTipiDocumento(tipoDocumentoForm, utente
                        .getRegistroVOInUso().getAooId());
            } else {
                errors.add("registrazione_tipo", new ActionMessage(
                        "record_non_inseribile", "il tipo Documento",
                        " ne esiste gi� con lo stesso nome per la AOO"));
                saveErrors(request, errors);
                return (mapping.findForward("edit"));
            }

        } else if (request.getParameter("btnCancella") != null) {
            int tipoDocumentoId = tipoDocumentoForm.getId();
            if (tipoDocumentoForm.getFlagDefault().equals("1")) {
                errors.add("cancellazione_tipo", new ActionMessage(
                        "record_non_cancellabile",
                        "il tipo Documento di default", ""));

            } else if (AmministrazioneDelegate.getInstance()
                    .cancellaTipoDocumento(tipoDocumentoId)) {
                aggiornaLookupTipiDocumento(tipoDocumentoForm, utente
                        .getRegistroVOInUso().getAooId());

                errors.add("cancellazione_tipo", new ActionMessage(
                        "cancellazione_ok"));
            } else {
                errors.add("cancellazione_tipo", new ActionMessage(
                        "record_non_cancellabile", "il tipo Documento", ""));
                saveErrors(request, errors);
                return (mapping.findForward("edit"));
            }

        } else if (request.getParameter("parId") != null) {
            tipoDocumentoVO = delegate.getTipoDocumento(Integer
                    .parseInt(request.getParameter("parId")));
            caricaDatiNelForm(tipoDocumentoForm, tipoDocumentoVO);

            return (mapping.findForward("edit"));
        }

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }

        logger.info("Execute TipoDocumentoAction");
        tipoDocumentoForm.inizializzaForm();
        return (mapping.findForward("input"));
    }

    public void caricaDatiNelVO(TipoDocumentoVO vo, TipoDocumentoForm form,
            Utente utente) {
        vo.setAooId(utente.getValueObject().getAooId());
        vo.setId(form.getId());
        vo.setDescrizioneDoc(form.getDescrizione());
        vo.setFlagAttivazione(form.getFlagAttivazione());
        vo.setFlagDefault(form.getFlagDefault());
        vo.setNumGGScadenza(form.getNumGGScadenza());
        if (vo.getId().intValue() == 0) {
            vo.setRowCreatedUser(utente.getValueObject().getUsername());
        }
        vo.setRowUpdatedUser(utente.getValueObject().getUsername());
       
    }

    public void caricaDatiNelForm(TipoDocumentoForm form, TipoDocumentoVO vo) {
        form.setId(vo.getId().intValue());
        form.setDescrizione(vo.getDescrizione());
        form.setFlagAttivazione(vo.getFlagAttivazione());
        form.setFlagDefault(vo.getFlagDefault());
        form.setNumGGScadenza(vo.getNumGGScadenza());
       
    }

    private void aggiornaLookupTipiDocumento(TipoDocumentoForm form, int aooId) {
        form.setTipiDocumento(AmministrazioneDelegate.getInstance()
                .getTipiDocumento(aooId));
        LookupDelegate.getInstance().caricaTipiDocumento();
        logger.info("AmministrazioneDelegate: caricaTipiDocumento");
    }

}
