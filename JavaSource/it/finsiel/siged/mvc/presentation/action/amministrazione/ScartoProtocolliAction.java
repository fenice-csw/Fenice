package it.finsiel.siged.mvc.presentation.action.amministrazione;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.StoriaProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.ScartoProtocolliForm;

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

public class ScartoProtocolliAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    static Logger logger = Logger.getLogger(ScartoProtocolliAction.class
            .getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionMessages errors = new ActionMessages();// Report any errors we

        HttpSession session = request.getSession();

        ScartoProtocolliForm scartoForm = (ScartoProtocolliForm) form;

        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);

        if (form == null) {
            logger.info(" Creating new ScartoProtocolliAction");
            form = new ScartoProtocolliForm();
            session.setAttribute(mapping.getAttribute(), form);
        }
        if (request.getParameter("btnConferma") != null) {
            int annoDaScartare = new Integer(request.getParameter("anno"))
                    .intValue();
            int numProt = StoriaProtocolloDelegate.getInstance()
                    .getNumProcolliNonScartabili(utente.getRegistroInUso(),
                            annoDaScartare);
            if (numProt > 0) {
                errors.add("scartoNonPossibile", new ActionMessage(
                        "scarto_non_eseguibile", "" + numProt, ""));
            } else {
                int protocolliScartati = StoriaProtocolloDelegate.getInstance()
                        .scarto(utente, annoDaScartare);
                if (protocolliScartati == 0) {
                    errors.add("scarto_Errore", new ActionMessage(
                            "errore_nel_salvataggio", "", ""));

                } else {
                    errors.add("scarto_OK", new ActionMessage("operazione_ok",
                            "", ""));
                }
                scartoForm.setRisultatiScarto(protocolliScartati);

            }
        }
        scartoForm.setAnniScartabili(StoriaProtocolloDelegate.getInstance()
                .getAnniScartabili(utente.getRegistroInUso()));
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        logger.info("Execute ScartoProtocolliAction");

        return (mapping.findForward("input"));
    }
}