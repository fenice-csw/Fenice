package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.AlberoUfficiBO;
import it.finsiel.siged.mvc.bo.TitolarioBO;
import it.finsiel.siged.mvc.business.AmministrazioneDelegate;
import it.finsiel.siged.mvc.business.ConfigurazioneUtenteDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ConfigurazioneUtenteForm;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.protocollo.ConfigurazioneUtenteVO;

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

public class ConfigurazioneUtenteAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    static Logger logger = Logger.getLogger(ConfigurazioneUtenteAction.class
            .getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionMessages errors = new ActionMessages();
        HttpSession session = request.getSession();
        AmministrazioneDelegate delegate = AmministrazioneDelegate
                .getInstance();

        ConfigurazioneUtenteForm configurazioneForm = (ConfigurazioneUtenteForm) form;

        if (form == null) {
            logger.info("Creating new ConfigurazioneUtenteAction");
            form = new ConfigurazioneUtenteForm();
            request.setAttribute(mapping.getAttribute(), form);
        }
        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
        int utenteId = utente.getValueObject().getId().intValue();
        boolean ufficioCompleto = (utente.getUfficioVOInUso().getTipo().equals(
                UfficioVO.UFFICIO_CENTRALE) || utente.getUfficioVOInUso()
                .getTipo().equals(UfficioVO.UFFICIO_SEMICENTRALE));

        configurazioneForm.setTipiDocumento(delegate.getTipiDocumento(utente
                .getRegistroVOInUso().getAooId()));

        if (configurazioneForm.getUfficioCorrenteId() == 0) {
            AlberoUfficiBO.impostaUfficioUtentiAbilitati(utente, configurazioneForm,
                    ufficioCompleto);
            TitolarioBO.impostaTitolario(configurazioneForm, utente
                    .getUfficioInUso(), 0);
        }

        ConfigurazioneUtenteVO configurazioneVO = new ConfigurazioneUtenteVO();

        ConfigurazioneUtenteDelegate cud = ConfigurazioneUtenteDelegate
                .getInstance();
        if (request.getParameter("impostaUfficioAction") != null) {
            configurazioneForm.setUfficioCorrenteId(configurazioneForm
                    .getUfficioSelezionatoId());
           // AlberoUfficiBO.impostaUfficioUtentiReferenti(utente,configurazioneForm, ufficioCompleto);
            TitolarioBO.impostaTitolario(configurazioneForm, utente
                    .getUfficioInUso(), 0);
            return (mapping.findForward("input"));
        } else if (request.getParameter("ufficioPrecedenteAction") != null) {
            if (configurazioneForm.getUfficioCorrente().getParentId() > 0) {
                configurazioneForm.setUfficioCorrenteId(configurazioneForm
                        .getUfficioCorrente().getParentId());
               // AlberoUfficiBO.impostaUfficioUtentiReferenti(utente,configurazioneForm, ufficioCompleto);
                TitolarioBO.impostaTitolario(configurazioneForm, utente
                        .getUfficioInUso(), 0);
            }
            return (mapping.findForward("input"));

        } else if (request.getParameter("impostaTitolarioAction") != null) {
            if (configurazioneForm.getTitolario() != null) {
                configurazioneForm.setTitolarioPrecedenteId(configurazioneForm
                        .getTitolario().getId().intValue());
            }
            TitolarioBO.impostaTitolario(configurazioneForm, utente
                    .getUfficioInUso(), configurazioneForm
                    .getTitolarioSelezionatoId());
            return (mapping.findForward("input"));
        } else if (request.getParameter("titolarioPrecedenteAction") != null) {
            TitolarioBO.impostaTitolario(configurazioneForm, utente
                    .getUfficioInUso(), configurazioneForm
                    .getTitolarioPrecedenteId());
            if (configurazioneForm.getTitolario() != null) {
                configurazioneForm.setTitolarioPrecedenteId(configurazioneForm
                        .getTitolario().getParentId());
            }
            return (mapping.findForward("input"));
        } else if (request.getParameter("btnCancella") != null) {
            session.removeAttribute("CONFIGURAZIONE_UTENTE_PROTOCOLLO");
            configurazioneForm.inizializzaForm();
            caricaDatiNelVO(configurazioneVO, configurazioneForm);
            configurazioneVO = cud.salvaConfigurazione(configurazioneVO,
                    utenteId);
            if (configurazioneVO != null) {
                caricaDatiNelForm(configurazioneForm, configurazioneVO);
            }
            session.setAttribute("CONFIGURAZIONE_UTENTE_PROTOCOLLO",
                    configurazioneVO);
            return (mapping.findForward("input"));

        } else if (request.getParameter("btnSalva") != null) {
            session.removeAttribute("CONFIGURAZIONE_UTENTE_PROTOCOLLO");
            caricaDatiNelVO(configurazioneVO, configurazioneForm);
            configurazioneVO = cud.salvaConfigurazione(configurazioneVO,
                    utenteId);
            if (configurazioneVO != null) {
                errors.add("CONFIGURAZIONE_UTENTE", new ActionMessage(
                        "operazione_ok", "", ""));

                caricaDatiNelForm(configurazioneForm, configurazioneVO);
            }
            session.setAttribute("CONFIGURAZIONE_UTENTE_PROTOCOLLO",
                    configurazioneVO);
        } else {

            if (session.getAttribute("CONFIGURAZIONE_UTENTE_PROTOCOLLO") != null) {
                configurazioneVO = (ConfigurazioneUtenteVO) session
                        .getAttribute("CONFIGURAZIONE_UTENTE_PROTOCOLLO");
            } else {

                configurazioneVO = cud.getConfigurazione(utenteId);
                session.setAttribute("CONFIGURAZIONE_UTENTE_PROTOCOLLO",
                        configurazioneVO);

            }
            if (configurazioneVO != null) {
                caricaDatiNelForm(configurazioneForm, configurazioneVO);
            } else {
                configurazioneForm.inizializzaForm();
            }

        }
        TitolarioBO.impostaTitolario(configurazioneForm, utente
                .getUfficioInUso(), configurazioneForm.getTitolarioId());
        AlberoUfficiBO.impostaUfficioUtentiAbilitati(utente, configurazioneForm,
                ufficioCompleto);
        // if (configurazioneVO != null &&
        // configurazioneVO.getAssegnatarioUtenteId() > 0) {
        // configurazioneForm.setUfficioSelezionatoId(configurazioneVO
        // .getAssegnatarioUfficioId());
        // configurazioneForm.setUtenteSelezionatoId(configurazioneVO
        // .getAssegnatarioUtenteId());
        // }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        return (mapping.findForward("input"));
    }

    public void caricaDatiNelVO(ConfigurazioneUtenteVO configurazioneVO,
            ConfigurazioneUtenteForm configurazioneForm) {

        configurazioneVO.setCheckAssegnatari(configurazioneForm
                .getCheckAssegnatari() == Boolean.TRUE ? "1" : "0");
        configurazioneVO.setCheckDataDocumento(configurazioneForm
                .getCheckDataDocumento() == Boolean.TRUE ? "1" : "0");
        configurazioneVO.setCheckDestinatari(configurazioneForm
                .getCheckDestinatari() == Boolean.TRUE ? "1" : "0");
        configurazioneVO
                .setCheckMittente(configurazioneForm.getCheckMittente() == Boolean.TRUE ? "1"
                        : "0");
        configurazioneVO
                .setCheckOggetto(configurazioneForm.getCheckOggetto() == Boolean.TRUE ? "1"
                        : "0");
        configurazioneVO.setCheckRicevutoIl(configurazioneForm
                .getCheckRicevuto() == Boolean.TRUE ? "1" : "0");
        configurazioneVO.setCheckTipoDocumento(configurazioneForm
                .getCheckTipoDocumento() == Boolean.TRUE ? "1" : "0");
        configurazioneVO.setCheckTipoMittente(configurazioneForm
                .getCheckTipoMittente() == Boolean.TRUE ? "1" : "0");
        configurazioneVO.setCheckTitolario(configurazioneForm
                .getCheckTitolario() == Boolean.TRUE ? "1" : "0");

        configurazioneVO
                .setDataDocumento(configurazioneForm.getDataDocumento());
        configurazioneVO.setDestinatario(configurazioneForm.getDestinatario());

        configurazioneVO.setMittente(configurazioneForm.getMittente());
        configurazioneVO
                .setDataRicezione(configurazioneForm.getDataRicezione());
        configurazioneVO.setTipoDocumentoId(configurazioneForm
                .getTipoDocumentoId());
        configurazioneVO.setAssegnatarioUfficioId(configurazioneForm
                .getUfficioCorrenteId());
        configurazioneVO.setAssegnatarioUtenteId(configurazioneForm
                .getUtenteSelezionatoId());
        configurazioneVO.setTipoMittente(configurazioneForm.getTipoMittente());
        if (configurazioneForm.getTitolario() != null) {
            configurazioneVO.setTitolarioId(configurazioneForm.getTitolario()
                    .getId().intValue());
        }
        configurazioneVO.setOggetto(configurazioneForm.getOggetto());
        configurazioneVO.setUtenteId(configurazioneForm.getUtenteId());
        configurazioneVO.setRowCreatedTime(new java.util.Date());
        configurazioneVO.setRowCreatedUser(configurazioneForm.getUsername());
        configurazioneVO.setRowUpdatedTime(new java.util.Date());
        configurazioneVO.setRowUpdatedUser(configurazioneForm.getUsername());

    }

    public void caricaDatiNelForm(ConfigurazioneUtenteForm configurazioneForm,
            ConfigurazioneUtenteVO configurazioneVO) {
        configurazioneForm.inizializzaForm();
        if (configurazioneVO != null) {
            if ("1".equals(configurazioneVO.getCheckOggetto())) {
                configurazioneForm.setCheckOggetto(Boolean.TRUE);
            }
            if ("1".equals(configurazioneVO.getCheckAssegnatari())) {
                configurazioneForm.setCheckAssegnatari(Boolean.TRUE);
            }
            if ("1".equals(configurazioneVO.getCheckDataDocumento())) {
                configurazioneForm.setCheckDataDocumento(Boolean.TRUE);
            }
            if ("1".equals(configurazioneVO.getCheckDestinatari())) {
                configurazioneForm.setCheckDestinatari(Boolean.TRUE);
            }
            if ("1".equals(configurazioneVO.getCheckMittente())) {
                configurazioneForm.setCheckMittente(Boolean.TRUE);
            }
            if ("1".equals(configurazioneVO.getCheckRicevutoIl())) {
                configurazioneForm.setCheckRicevuto(Boolean.TRUE);
            }
            if ("1".equals(configurazioneVO.getCheckTipoDocumento())) {
                configurazioneForm.setCheckTipoDocumento(Boolean.TRUE);
            }
            if ("1".equals(configurazioneVO.getCheckTipoMittente())) {
                configurazioneForm.setCheckTipoMittente(Boolean.TRUE);
            }
            if ("1".equals(configurazioneVO.getCheckTitolario())) {
                configurazioneForm.setCheckTitolario(Boolean.TRUE);
            }
            configurazioneForm.setUfficioSelezionatoId(configurazioneVO
                    .getAssegnatarioUfficioId());
            configurazioneForm.setUfficioCorrenteId(configurazioneVO
                    .getAssegnatarioUfficioId());
            configurazioneForm.setUtenteSelezionatoId(configurazioneVO
                    .getAssegnatarioUtenteId());
            configurazioneForm.setDataDocumento(configurazioneVO
                    .getDataDocumento());
            configurazioneForm.setDataRicezione(configurazioneVO
                    .getDataRicezione());
            configurazioneForm.setDestinatario(configurazioneVO
                    .getDestinatario());
            configurazioneForm.setMittente(configurazioneVO.getMittente());
            configurazioneForm.setOggetto(configurazioneVO.getOggetto());
            configurazioneForm.setTipoDocumentoId(configurazioneVO
                    .getTipoDocumentoId());
            configurazioneForm.setTipoMittente(configurazioneVO
                    .getTipoMittente());
            configurazioneForm
                    .setTitolarioId(configurazioneVO.getTitolarioId());

            configurazioneForm.setUtenteId(configurazioneVO.getUtenteId());
            configurazioneForm
                    .setTitolarioId(configurazioneVO.getTitolarioId());

        }
    }

}