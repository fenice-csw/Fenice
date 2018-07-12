package it.finsiel.siged.mvc.presentation.action.amministrazione;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.business.StoriaProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.RicercaScartoProtocolliForm;

import java.text.SimpleDateFormat;
import java.util.Date;

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

public class RicercaScartoProtocolliAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    static Logger logger = Logger.getLogger(RicercaScartoProtocolliAction.class
            .getName());

    public final static String FLAG_PROTOCOLLO_ANNULLATO = "C";

    public final static Integer FLAG_PROTOCOLLO_RISERVATO = new Integer(1);

    // --------------------------------------------------------- Public Methods

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Extract attributes we will need

        ActionMessages errors = new ActionMessages();// Report any errors we

        HttpSession session = request.getSession();
        StoriaProtocolloDelegate spd = StoriaProtocolloDelegate.getInstance();

        RicercaScartoProtocolliForm ricercaForm = (RicercaScartoProtocolliForm) form;
        session.setAttribute("ricercaForm", ricercaForm);
        LookupDelegate lookupDelegate = LookupDelegate.getInstance();
        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
        ricercaForm.setStatiProtocollo(lookupDelegate
                .getStatiProtocollo(ricercaForm.getTipoProtocollo()));

        if (form == null) {
            logger.info(" Creating new RicercaScartoProtocolliAction");
            form = new RicercaScartoProtocolliForm();
            session.setAttribute(mapping.getAttribute(), form);
        }
        if (request.getParameter("cerca") != null) {
            ricercaForm.setProtocolloSelezionato(null);
            Date dataRegistrazioneDa = null;
            Date dataRegistrazioneA = null;
            int numeroProtocolloDa = 0;
            int numeroProtocolloA = 0;
            int annoProtocolloDa = 0;
            int annoProtocolloA = 0;
            String tipo = "";
            String stato = "";
            String flagMozione = "";
            String riservato = "0";

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

            if (request.getParameter("dataRegistrazioneDa") != null
                    && !"".equals(request.getParameter("dataRegistrazioneDa"))) {
                dataRegistrazioneDa = df.parse(request
                        .getParameter("dataRegistrazioneDa"));
            }
            if (request.getParameter("dataRegistrazioneA") != null
                    && !"".equals(request.getParameter("dataRegistrazioneA"))) {
                dataRegistrazioneA = df.parse(request
                        .getParameter("dataRegistrazioneA"));
            }
            if ("on".equalsIgnoreCase(request.getParameter("riservato"))) {
                riservato = "1";
            }
            if (request.getParameter("numeroProtocolloDa") != null
                    && !"".equals(request.getParameter("numeroProtocolloDa"))) {
                numeroProtocolloDa = Integer.parseInt(request
                        .getParameter("numeroProtocolloDa"));
            }
            if (request.getParameter("numeroProtocolloA") != null
                    && !"".equals(request.getParameter("numeroProtocolloA"))) {
                numeroProtocolloA = Integer.parseInt(request
                        .getParameter("numeroProtocolloA"));
            }
            if (request.getParameter("annoProtocolloDa") != null
                    && !"".equals(request.getParameter("annoProtocolloDa"))) {
                annoProtocolloDa = Integer.parseInt(request
                        .getParameter("annoProtocolloDa"));
            }
            if (request.getParameter("annoProtocolloA") != null
                    && !"".equals(request.getParameter("annoProtocolloA"))) {
                annoProtocolloA = Integer.parseInt(request
                        .getParameter("annoProtocolloA"));
            }
            if (request.getParameter("tipoProtocollo") != null
                    && !"".equals(request.getParameter("tipoProtocollo"))) {
                tipo = request.getParameter("tipoProtocollo");
                if ("M".equals(request.getParameter("tipoProtocollo"))) {
                    flagMozione = "1";
                } else if ("U".equals(request.getParameter("tipoProtocollo"))) {
                    flagMozione = "0";
                } else {
                    flagMozione = "";
                }
            }
            if (request.getParameter("statoProtocollo") != null
                    && !"".equals(request.getParameter("statoProtocollo"))) {
                stato = request.getParameter("statoProtocollo");
            }

            ricercaForm.setProtocolli(spd.cercaScartoProtocolli(utente, tipo,
                    flagMozione, stato, riservato, dataRegistrazioneDa,
                    dataRegistrazioneA, numeroProtocolloDa, numeroProtocolloA,
                    annoProtocolloDa, annoProtocolloA));
            // return (mapping.findForward("risultati"));
            if (ricercaForm.getProtocolliCollection().size() == 0) {
                errors.add("nessun_dato", new ActionMessage("nessun_dato", "",
                        ""));
            }

        } else if (request.getParameter("protocolloSelezionato") != null) {
            request.setAttribute("protocolloId", new Integer(request
                    .getParameter("protocolloSelezionato")));
            request.setAttribute("versioneId", new Integer(request
                    .getParameter("versioneSelezionata")));
            if ("I".equals(request.getParameter("tipoProtocollo"))) {
                return (mapping.findForward("visualizzaProtocolloIngresso"));
            } else {
                return (mapping.findForward("visualizzaProtocolloUscita"));
            }
        }

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        logger.info("Execute RicercaScartoProtocolliAction");

        return (mapping.findForward("input"));

    }
}