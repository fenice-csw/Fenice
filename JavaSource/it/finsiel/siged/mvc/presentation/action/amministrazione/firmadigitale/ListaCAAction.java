package it.finsiel.siged.mvc.presentation.action.amministrazione.firmadigitale;

import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.business.FirmaDigitaleDelegate;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.firmadigitale.ListaCAForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

public class ListaCAAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    static Logger logger = Logger.getLogger(ListaCAAction.class.getName());

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

        ActionMessages errors = new ActionMessages();
        ListaCAForm caForm = (ListaCAForm) form;

        if (form == null) {
            caForm = new ListaCAForm();
            request.setAttribute(mapping.getAttribute(), caForm);
        }

        try {
            caForm.setListaCa(FirmaDigitaleDelegate.getInstance().getAllCA());
        } catch (DataException e) {
            errors.add("generale", new ActionMessage("database.cannot.load"));
        }
        request.setAttribute(mapping.getAttribute(), caForm);
        saveErrors(request, errors);
        return (mapping.findForward("input"));
    }

}
