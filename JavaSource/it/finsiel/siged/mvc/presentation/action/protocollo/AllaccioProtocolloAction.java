package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.AllaccioProtocolloForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloIngressoForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloUscitaForm;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;

import java.util.ArrayList;

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

public class AllaccioProtocolloAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    static Logger logger = Logger.getLogger(AllaccioProtocolloAction.class
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
        // Extract attributes we will need

        ActionMessages errors = new ActionMessages();// Report any errors we

        HttpSession session = request.getSession(true); // we create one if does
        ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
        AllaccioProtocolloForm allacci = (AllaccioProtocolloForm) form;

        if (form == null) {
            logger.info(" Creating new AllacioProtocolloAction");
            form = new AllaccioProtocolloForm();
            session.setAttribute(mapping.getAttribute(), form);
        }
        if (allacci.getBtnCercaAllacci() != null) {
            int allaccioProtocolloDa = 0;
            int allaccioProtocolloA = 0;
            int allaccioProtocolloAnno = 0;
            ProtocolloForm pForm = (ProtocolloForm) session
                    .getAttribute("protocolloForm");

            if (!"".equals(request.getParameter("allaccioProtocolloDa"))) {
                allaccioProtocolloDa = Integer.parseInt(request
                        .getParameter("allaccioProtocolloDa"));
            }
            if (!"".equals(request.getParameter("allaccioProtocolloA"))) {
                allaccioProtocolloA = Integer.parseInt(request
                        .getParameter("allaccioProtocolloA"));
            }
            if (!"".equals(request.getParameter("allaccioProtocolloAnno"))) {
                allaccioProtocolloAnno = Integer.parseInt(request
                        .getParameter("allaccioProtocolloAnno"));
            }
            Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
            int maxRighe=Integer.parseInt(utente.getAreaOrganizzativa().getMaxRighe());
            int contaRighe= delegate.contaProtocolliAllacciabili(utente,
                    allaccioProtocolloDa, allaccioProtocolloA,
                    allaccioProtocolloAnno, pForm.getProtocolloId());
            if (contaRighe <= maxRighe) {
            allacci.setAllacciabili(delegate.getProtocolliAllacciabili(utente,
                    allaccioProtocolloDa, allaccioProtocolloA,
                    allaccioProtocolloAnno, pForm.getProtocolloId()));
            return (mapping.findForward("input"));
            }else {
                errors.add("controllo.maxrighe", new ActionMessage(
                        "controllo.maxrighe","" + contaRighe, "allacci", "" + maxRighe));
                saveErrors(request, errors);
                return (mapping.findForward("input"));
            }

        } else if (allacci.getBtnAnnulla() != null) {
            return (mapping.findForward("input"));

        } else if (allacci.getBtnSelezionaAllacci() != null) {
            String[] allacciabili = request.getParameterValues("checkAllaccio");
            ProtocolloForm pForm = (ProtocolloForm) session
                    .getAttribute("protocolloForm");
            if (allacciabili != null) {
                for (int i = 0; i < allacciabili.length; i++) {
                    AllaccioVO allaccio = new AllaccioVO();
                    int j = allacciabili[i].indexOf("_");
                    allaccio.setProtocolloAllacciatoId(Integer
                            .parseInt(allacciabili[i].substring(0, j)));
                    allaccio.setAllaccioDescrizione(allacciabili[i]
                            .substring(j + 1));
                    pForm.allacciaProtocollo(allaccio);
                }
            }
            ArrayList navBar = (ArrayList) session
                    .getAttribute(Constants.NAV_BAR);
            if (navBar.size() > 1) {
                navBar.remove(navBar.size() - 1);
            }
            if (pForm instanceof ProtocolloIngressoForm) {
                return (mapping.findForward("tornaProtocolloIngresso"));
            } else  if (pForm instanceof ProtocolloUscitaForm){
                return (mapping.findForward("tornaProtocolloUscita"));
            }else{
            	 return (mapping.findForward("tornaPostaInterna"));
            }
        } else if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return (mapping.findForward("input"));
        }

        if (mapping.getAttribute() != null) {
            session.removeAttribute(mapping.getAttribute());
        }
        logger.info("Execute AllacciProtocollo");
        return (mapping.findForward("input")); // 

    }
}
