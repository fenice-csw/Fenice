package it.finsiel.siged.mvc.presentation.action.registro;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.RegistroDelegate;
import it.finsiel.siged.mvc.presentation.actionform.registro.RegistroForm;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;
import it.finsiel.siged.util.DateUtil;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * Implementation of <strong>Action </strong> that validates a user logon.
 * 
 * @author Almaviva sud.
 */

public final class GestioneRegistroAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    static Logger logger = Logger.getLogger(GestioneRegistroAction.class
            .getName());

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
        HttpSession session = request.getSession();

        RegistroForm rForm = (RegistroForm) form;
        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
        RegistroVO registro = utente.getRegistroVOInUso();

        if (request.getParameter("salvaAction") != null) {
            Date dataApertura = registro.getDataAperturaRegistro();
            boolean dataBloccata = registro.getDataBloccata();
            boolean apertoIngresso = registro.getApertoIngresso();
            boolean apertoUscita = registro.getApertoUscita();
            registro.setDataAperturaRegistro(DateUtil.toDate(rForm
                    .getDataApertura()));
            registro.setDataBloccata(rForm.getDataBloccata());
            registro.setApertoIngresso(rForm.getApertoIngresso());
            registro.setApertoUscita(rForm.getApertoUscita());
            RegistroDelegate delegate = RegistroDelegate.getInstance();
            delegate.aggiornaStatoRegistro(registro);
            if (registro.getReturnValue() != ReturnValues.SAVED) {
                registro.setDataAperturaRegistro(dataApertura);
                registro.setDataBloccata(dataBloccata);
                registro.setApertoIngresso(apertoIngresso);
                registro.setApertoUscita(apertoUscita);
                ActionMessages errors = new ActionErrors();
                errors.add("erroriRegistro", new ActionMessage(
                        "registro.nonsalvato"));
                saveErrors(request, errors);
            } else {
                Organizzazione.getInstance().addRegistro(registro);                
                ActionMessages msg = new ActionMessages();
                msg.add("registroSalvato", new ActionMessage(
                        "stato_registro_salvato"));
                saveMessages(request, msg);
            }

        } else {
            rForm.setDataBloccata(registro.getDataBloccata());
            Date dataAp = registro.getDataAperturaRegistro();
            rForm.setDataApertura(DateUtil.formattaData(dataAp.getTime()));
            rForm.setApertoIngresso(registro.getApertoIngresso());
            rForm.setApertoUscita(registro.getApertoUscita());
        }

        return (mapping.getInputForward());
    }

    // ------------------------------------------------------ Private Methods

}