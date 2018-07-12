package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.AnnotazioneDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.AnnotazioneProtocolloForm;
import it.finsiel.siged.mvc.vo.protocollo.AnnotazioneVO;

import java.sql.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

/**
 * Implementation of <strong>Action </strong> to create a new E-Photo Utente.
 * 
 * @author Almaviva sud.
 * 
 */

public class AnnotazioneProtocolloAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    static Logger logger = Logger.getLogger(AnnotazioneProtocolloAction.class
            .getName());

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
        AnnotazioneDelegate delegate = AnnotazioneDelegate.getInstance();

        String username = ((Utente) request.getSession().getAttribute(
                Constants.UTENTE_KEY)).getValueObject().getUsername();

        AnnotazioneProtocolloForm annotazioni = (AnnotazioneProtocolloForm) form;

        if (form == null) {
            logger.info(" Creating new Annotazione Form");
            form = new AnnotazioneProtocolloForm();
            session.setAttribute(mapping.getAttribute(), form);
        }
        if (annotazioni.getBtnCercaAnnotazioni() != null) {

        }

        /*
         * consente di visualizzare una lista di annotazioni da digita bottone
         * BtnAnnotazioni
         */
        if (annotazioni.getBtnAnnotazioni() != null) {

            annotazioni.setAnnotazioniCollection(delegate.getAnnotazioni(102));

            return (mapping.findForward("input"));

        }

        if (annotazioni.getBtnInserisciAnnotazione() != null) {

            if (request.getParameter("noteAnnotazione") != null) {

                String descrAnnotazione = request
                        .getParameter("noteAnnotazione");

                annotazioni.getAnnotazioneProtocolloId();

                AnnotazioneVO lnkAnn = new AnnotazioneVO();

                // inserire i dati in maniera fittizia con le set
                lnkAnn.setCodiceAnnotazione(112);
                lnkAnn.setCodiceUserId(654321);
                lnkAnn.setDataAnnotazione(new Date(System.currentTimeMillis()));
                lnkAnn.setDescrizione(descrAnnotazione);
                lnkAnn.setFkProtocollo(102);
                lnkAnn.setRowCreatedTime(new Date(System.currentTimeMillis()));
                lnkAnn.setRowUpdatedTime(new Date(System.currentTimeMillis()));
                lnkAnn.setRowCreatedUser(username);
                lnkAnn.setRowUpdatedUser(username);
                lnkAnn.setVersione(8);

                AnnotazioneDelegate annotazioneDelegate = AnnotazioneDelegate
                        .getInstance();
                annotazioneDelegate.newAnnotazioneVO(lnkAnn);

            }
            /*
             * in questo momento viene simulato il passaggio dell'idProtocollo
             * con valore 102
             */
            // annotazioni.setAnnotazioniCollection(delegate.getAnnotazioni(102));
            // return (mapping.findForward("input"));
        }

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            // saveToken(request);
            return (mapping.findForward("input"));
        }

        if (mapping.getAttribute() != null) {
            session.removeAttribute(mapping.getAttribute());
        }
        logger.info("Execute Annotazione");
        return (mapping.findForward("input")); // 

    }

    public static void readForm(AnnotazioneVO annotazioneProtocollo,
            AnnotazioneProtocolloForm form) {

    }

    public static void writeForm(AnnotazioneProtocolloForm form,
            AnnotazioneVO annotazioneProtocollo) {
    }
}