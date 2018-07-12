package it.finsiel.siged.mvc.presentation.action.amministrazione;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.AreaOrganizzativaDelegate;
import it.finsiel.siged.mvc.business.StoriaProtocolloDelegate;
import it.finsiel.siged.mvc.business.TitolarioDelegate;
import it.finsiel.siged.mvc.business.UfficioDelegate;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.ReportProtocolliDaScartareForm;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.ScartoProtocolliForm;
import it.finsiel.siged.mvc.vo.lookup.TitolarioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;

import java.text.SimpleDateFormat;
import java.util.Collection;
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

public class ReportProtocolliDaScartareAction extends Action {

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

        ReportProtocolliDaScartareForm protocolloDaScartareForm = (ReportProtocolliDaScartareForm) form;

        StoriaProtocolloDelegate spd = StoriaProtocolloDelegate.getInstance();

        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);

        if (form == null) {
            logger.info(" Creating new ReportProtocolliDaScartareAction");
            form = new ScartoProtocolliForm();
            session.setAttribute(mapping.getAttribute(), form);
        }
        impostaUfficioServizio(protocolloDaScartareForm,utente);

        // ------------------------------------- //
        // --------- IMPOSTA UFFICIO ----------- //
        if (request.getParameter("impostaUfficioAction") != null) {
            int ufficioSelezionatoId = protocolloDaScartareForm
                    .getUfficioSelezionatoId();
            UfficioVO ufficioSelezionatoVO = UfficioDelegate.getInstance()
                    .getUfficioVO(ufficioSelezionatoId);
            protocolloDaScartareForm.setUfficioSelezionato(ufficioSelezionatoVO
                    .getDescription());
            // carico i sevizi associati
            Collection<TitolarioVO> servizi = TitolarioDelegate.getInstance()
                    .getTitolariByParent(ufficioSelezionatoId, 0, utente.getRegistroVOInUso().getAooId());
            protocolloDaScartareForm.setServizi(servizi);
            protocolloDaScartareForm.setServizioSelezionato("");
            protocolloDaScartareForm.setServizioSelezionatoId(0);
            return (mapping.findForward("input"));
            // ------------------------------------- //
            // ======== CAMBIA UFFICIO ============= //
        } else if (request.getParameter("cambiaUfficioAction") != null) {
            protocolloDaScartareForm.setUfficioSelezionatoId(0);
            protocolloDaScartareForm.setUfficioSelezionato("");
            Collection<UfficioVO> uffici = AreaOrganizzativaDelegate.getInstance()
                    .getUffici(utente.getValueObject().getAooId());
            protocolloDaScartareForm.setUffici(uffici);
            protocolloDaScartareForm.setServizi(null);
            protocolloDaScartareForm.setServizioSelezionato("");
            protocolloDaScartareForm.setServizioSelezionatoId(0);
            return (mapping.findForward("input"));
            // ------------------------------------- //
            // ======== IMPOSTA SERVIZIO ============= //
        } else if (request.getParameter("impostaServizioAction") != null) {
            int servizioSelezionatoId = protocolloDaScartareForm
                    .getServizioSelezionatoId();
            TitolarioDelegate td = TitolarioDelegate.getInstance();
            TitolarioVO servizioVO = td.getTitolario(servizioSelezionatoId);
            String servizioSelezionato = servizioVO.getDescrizione();
            protocolloDaScartareForm
                    .setServizioSelezionato(servizioSelezionato);
            // carico le categorie associate al servizio
            return (mapping.findForward("input"));
            // ------------------------------------- //
            // ======== CAMBIA SERVIZIO ============= //
        } else if (request.getParameter("cambiaServizioAction") != null) {
            protocolloDaScartareForm.setServizioSelezionatoId(0);
            return (mapping.findForward("input"));

        } else if (request.getParameter("btnConferma") != null) {
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
                // scartoForm.setRisultatiScarto(protocolliScartati);

            }
        } else if (request.getParameter("cercaAction") != null) {
            // protocolloDaScartareForm.setProtocolloSelezionato(null);
            Date dataRegistrazioneDa = null;
            Date dataRegistrazioneA = null;
            int idUfficio = 0;
            int idServizio = 0;
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
            idUfficio = protocolloDaScartareForm.getUfficioSelezionatoId();
            idServizio = protocolloDaScartareForm.getServizioSelezionatoId();
            protocolloDaScartareForm.setProtocolli(spd
                    .cercaProtocolliDaScartare(utente, idUfficio, idServizio,
                            dataRegistrazioneDa, dataRegistrazioneA));
            // return (mapping.findForward("risultati"));
            if (protocolloDaScartareForm.getProtocolliCollection().size() == 0) {
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
        } else {
            // inizializzo form
            if ((request.getAttribute("ufficioSelezionatoId") == null)
                    && (request.getAttribute("servizioSelezionatoId") == null))
                protocolloDaScartareForm.inizializzaForm();
            protocolloDaScartareForm.setUtenteCorrente(utente);
            protocolloDaScartareForm.setAooId(utente.getRegistroVOInUso()
                    .getAooId());
            Collection<UfficioVO> uffici = AreaOrganizzativaDelegate.getInstance()
                    .getUffici(utente.getValueObject().getAooId());
            protocolloDaScartareForm.setUffici(uffici);
            session.setAttribute(mapping.getAttribute(),
                    protocolloDaScartareForm);
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        logger.info("Execute ReportProtocolliDaScartareAction");
        impostaUfficioServizio(protocolloDaScartareForm,utente);
        request.setAttribute("ufficioSelezionatoId", ""
                + protocolloDaScartareForm.getUfficioSelezionatoId());
        request.setAttribute("servizioSelezionatoId", ""
                + protocolloDaScartareForm.getServizioSelezionatoId());
        return (mapping.findForward("input"));
    }

    private void impostaUfficioServizio(
            ReportProtocolliDaScartareForm protocolliDaScartareForm, Utente utente) {

        if (protocolliDaScartareForm.getUfficioSelezionatoId() > 0) {
            int ufficioSelezionatoId = protocolliDaScartareForm
                    .getUfficioSelezionatoId();
            UfficioVO ufficioSelezionatoVO = UfficioDelegate.getInstance()
                    .getUfficioVO(ufficioSelezionatoId);
            protocolliDaScartareForm.setUfficioSelezionato(ufficioSelezionatoVO
                    .getDescription());
            Collection<TitolarioVO> servizi = TitolarioDelegate.getInstance()
                    .getTitolariByParent(ufficioSelezionatoId, 0,utente.getRegistroVOInUso().getAooId());
            protocolliDaScartareForm.setServizi(servizi);
            if (protocolliDaScartareForm.getServizioSelezionatoId() == 0) {
                protocolliDaScartareForm.setServizioSelezionato("");
                protocolliDaScartareForm.setServizioSelezionatoId(0);
            } else {
                int servizioSelezionatoId = protocolliDaScartareForm
                        .getServizioSelezionatoId();
                TitolarioDelegate td = TitolarioDelegate.getInstance();
                TitolarioVO servizioVO = td.getTitolario(servizioSelezionatoId);
                String servizioSelezionato = servizioVO.getDescrizione();
                protocolliDaScartareForm
                        .setServizioSelezionato(servizioSelezionato);
            }
        } else if (protocolliDaScartareForm.getServizioSelezionatoId() > 0) {
            int servizioSelezionatoId = protocolliDaScartareForm
                    .getServizioSelezionatoId();
            TitolarioDelegate td = TitolarioDelegate.getInstance();
            TitolarioVO servizioVO = td.getTitolario(servizioSelezionatoId);
            String servizioSelezionato = servizioVO.getDescrizione();
            protocolliDaScartareForm
                    .setServizioSelezionato(servizioSelezionato);
        }
    }
}