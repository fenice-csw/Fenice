package it.finsiel.siged.mvc.presentation.action.amministrazione;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.AmministrazioneDelegate;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.SpedizioneForm;
import it.finsiel.siged.mvc.vo.protocollo.SpedizioneVO;

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

public class SpedizioneAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    static Logger logger = Logger.getLogger(SpedizioneAction.class.getName());

    // --------------------------------------------------------- Public Methods

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     * 
     * @param mapping
     *            The ActionMapping used to select this instance
     * @param form
     *            The optional ActionForm bean for this request (if any)
     * @param request
     *            The HTTP request we are processing
     * @param response
     *            The HTTP response we are creating
     * 
     * @exception Exception
     *                if business logic throws an exception
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionMessages errors = new ActionMessages();// Report any errors we

        HttpSession session = request.getSession(true); // we create one if does
        AmministrazioneDelegate delegate = AmministrazioneDelegate
                .getInstance();
        SpedizioneForm spedizioneForm = (SpedizioneForm) form;

        SpedizioneVO spedizioneVO = new SpedizioneVO();
        Utente utente = (Utente) request.getSession().getAttribute(
                Constants.UTENTE_KEY);

        if (form == null) {
            logger.info(" Creating new SpedizioneAction");
            form = new SpedizioneForm();
            session.setAttribute(mapping.getAttribute(), form);
        }
        if (request.getParameter("btnCercaSpedizioni") != null) {
            spedizioneForm.setMezziSpedizione(delegate.getMezziSpedizione(
                    spedizioneForm.getDescrizioneSpedizione(), utente
                            .getRegistroVOInUso().getAooId()));
            return (mapping.findForward("input"));
        } else if (request.getParameter("btnNuovaSpedizione") != null) {
            spedizioneForm.inizializzaForm();
            return (mapping.findForward("edit"));
        } else if (request.getParameter("btnAnnulla") != null) {
            spedizioneForm.inizializzaForm();
            return (mapping.findForward("input"));
        } else if (request.getParameter("btnConferma") != null) {
            errors = spedizioneForm.validate(mapping, request);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return (mapping.findForward("edit"));
            }
            caricaDatiNelVO(spedizioneVO, spedizioneForm, utente);
            spedizioneVO = delegate.salvaMezzoSpedizione(spedizioneVO);
            if (spedizioneForm.getId() > 0
                    && spedizioneVO.getReturnValue() == ReturnValues.FOUND) {
                errors.add("mezzo", new ActionMessage(
                        "record_non_modificabile",
                        "la descrizione del mezzo di spedizione",
                        "poich� referenziato da protocolli in uscita"));
                saveErrors(request, errors);
                return (mapping.findForward("edit"));

            } else {
                spedizioneForm.setMezziSpedizione(delegate.getMezziSpedizione(
                        spedizioneForm.getDescrizioneSpedizione(), utente
                                .getRegistroVOInUso().getAooId()));
                aggiornaLookupMezziSpedizione(spedizioneForm, utente
                        .getRegistroVOInUso().getAooId());
                return (mapping.findForward("input"));
            }
        } else if (request.getParameter("btnCancella") != null) {
            if (spedizioneForm.getId() > 0) {
                if (delegate.cancellaMezzoSpedizione(spedizioneForm.getId())) {
                    spedizioneForm.setMezziSpedizione(delegate
                            .getMezziSpedizione(spedizioneForm
                                    .getDescrizioneSpedizione(), utente
                                    .getRegistroVOInUso().getAooId()));
                    aggiornaLookupMezziSpedizione(spedizioneForm, utente
                            .getRegistroVOInUso().getAooId());
                    errors.add("dataSpedizione", new ActionMessage(
                            "operazione_ok", "", ""));
                    saveErrors(request, errors);
                    return (mapping.findForward("input"));
                } else {
                    errors.add("dataSpedizione", new ActionMessage(
                            "record_non_cancellabile",
                            "il mezzo di spedizione",
                            "poich� referenziato da protocolli in uscita"));
                    saveErrors(request, errors);
                    return (mapping.findForward("edit"));

                }
            }

        } else if (request.getParameter("parId") != null) {
            spedizioneVO = delegate.getMezzoSpedizione(Integer.parseInt(request
                    .getParameter("parId")));
            caricaDatiNelForm(spedizioneForm, spedizioneVO);
            return (mapping.findForward("edit"));
        } else if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return (mapping.findForward("input"));
        }

        if (mapping.getAttribute() != null) {
            session.removeAttribute(mapping.getAttribute());
        }
        logger.info("Execute SpedizioneAction");
        return (mapping.findForward("input")); // 

    }

    public void caricaDatiNelVO(SpedizioneVO vo, SpedizioneForm form,
            Utente utente) {
        vo.setId(form.getId());
        vo.setCodiceSpedizione(form.getCodiceSpedizione());
        vo.setDescrizioneSpedizione(form.getDescrizioneSpedizione());
        if (form.getId() == 0) {
            vo.setRowCreatedUser(utente.getValueObject().getUsername());
        }
        vo.setRowUpdatedUser(utente.getValueObject().getUsername());
        vo.setFlagAbilitato(form.getFlagAbilitato());
        vo.setFlagCancellabile(form.getFlagCancellabile());
        vo.setAooId(utente.getValueObject().getAooId());
        vo.setPrezzo(form.getPrezzo());
    }

    public void caricaDatiNelForm(SpedizioneForm spedizioneForm,
            SpedizioneVO spedizioneVO) {
        spedizioneForm.setId(spedizioneVO.getId().intValue());
        spedizioneForm.setCodiceSpedizione(spedizioneVO.getCodiceSpedizione());
        spedizioneForm.setDescrizioneSpedizione(spedizioneVO
                .getDescrizioneSpedizione());
        spedizioneForm.setFlagAbilitato(spedizioneVO.getFlagAbilitato());
        spedizioneForm.setFlagCancellabile(spedizioneVO.getFlagCancellabile());
        spedizioneForm.setPrezzo(spedizioneVO.getPrezzo());
    }

    private void aggiornaLookupMezziSpedizione(SpedizioneForm form, int aooId) {
        form.setMezziSpedizione(AmministrazioneDelegate.getInstance()
                .getMezziSpedizione("", aooId));
        form.inizializzaForm();
        LookupDelegate.getInstance().caricaMezziSpedizione();
    }

}
