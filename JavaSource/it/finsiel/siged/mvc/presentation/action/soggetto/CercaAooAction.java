package it.finsiel.siged.mvc.presentation.action.soggetto;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloUscitaForm;
import it.finsiel.siged.mvc.presentation.actionform.soggetto.CercaAooForm;
import it.finsiel.siged.mvc.vo.lookup.ParametriLdapVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.util.ldap.LdapUtil;

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

import com.novell.ldap.LDAPConnection;

/**
 * Implementation of <strong>Action </strong> to create a new E-Photo User.
 * 
 * @author Paolo Spadafora
 * 
 */

public class CercaAooAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    static Logger logger = Logger.getLogger(CercaAooAction.class.getName());

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
     * @param pfForm
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
        HttpSession session = request.getSession();

        CercaAooForm aooForm = (CercaAooForm) form;

        if (form == null) {
            logger.info(" Creating new Cerca AOO Form");
            form = new CercaAooForm();
            request.setAttribute(mapping.getAttribute(), form);
        }

        String codice = (String) request.getAttribute("nominativoDestinatario");
        boolean preQuery = (!"".equals(codice) && codice != null);
        ParametriLdapVO par = LookupDelegate.getInstance().getIndicePAParams();

        if (request.getParameter("parId") != null) {
            // l'utente ha selezionato un'amministrazione, mostriamo le AOO
            String ammCN = (String) request.getParameter("parId");
            aooForm.setListaAoo(LdapUtil.listaAOO(par.getHost(),
                    par.getPorta(), "o=" + ammCN + "," + par.getDn(),
                    "(aoo=*)", LDAPConnection.SCOPE_SUB, 100));
            return (mapping.findForward("listaAoo"));
        } else if (aooForm.getCerca() != null || preQuery) {
            // l'utente vuole effettuare una ricerca
            aooForm.setListaAmm(null);
            if (!"".equals(codice) && codice != null) {
                aooForm.setNome(codice);
            }

            errors = aooForm.validate(mapping, request);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("input");
            }

            try {
                aooForm.setListaAmm(LdapUtil.cercaAmministrazione(
                        par.getHost(), par.getPorta(), par.getDn(),
                        "(&(description=*" + aooForm.getNome() + "*)"
                                + "(objectClass=amministrazione)(tipoAmm="
                                + aooForm.getCategoriaId() + "))",
                        LDAPConnection.SCOPE_SUB, 100));
            } catch (Exception e) {
                errors.add("general", new ActionMessage("errore.ricerca.ldap"));
            }

        } else if (request.getParameter("annulla") != null) {
            session.removeAttribute("tornaProtocollo");
            return (mapping.findForward("tornaProtocolloUscita"));

        } else if (request.getParameter("codiceAoo") != null) {
            // l'utente ha selezionato un'aoo
            String dn = (String) request.getParameter("codiceAoo");
            DestinatarioVO aoo = LdapUtil.getAOO(par.getHost(), par.getPorta(),
                    dn);
            if (ReturnValues.NOT_FOUND == aoo.getReturnValue()) {
                errors.add("general", new ActionMessage("aoo.notfound"));
                saveErrors(request, errors);
                return mapping.findForward("input");
            }
            session.removeAttribute("tornaProtocollo");
            Object pForm = session.getAttribute("protocolloForm");
            if (pForm != null) {
                ArrayList navBar = (ArrayList) session
                        .getAttribute(Constants.NAV_BAR);
                if (navBar.size() > 1) {
                    navBar.remove(navBar.size() - 1);
                }
                ProtocolloUscitaForm protForm = (ProtocolloUscitaForm) pForm;
                // soggetto selezionato
                protForm.setNominativoDestinatario(aoo.getDestinatario());
                protForm.setEmailDestinatario(aoo.getEmail());
                protForm.setCitta(aoo.getCitta());
                protForm.setIndirizzoDestinatario(aoo.getIndirizzo());
                return (mapping.findForward("tornaProtocolloUscita"));
            }
            // carica i dati nel protocollo form
            return (mapping.findForward("input"));
        }
        return (mapping.findForward("input"));

    }

}
