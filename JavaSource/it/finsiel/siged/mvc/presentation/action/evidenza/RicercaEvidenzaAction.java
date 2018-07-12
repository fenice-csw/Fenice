package it.finsiel.siged.mvc.presentation.action.evidenza;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.AlberoUfficiBO;
import it.finsiel.siged.mvc.bo.TitolarioBO;
import it.finsiel.siged.mvc.business.EvidenzaDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.RicercaEvidenzaForm;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.util.DateUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

public final class RicercaEvidenzaAction extends Action {

    static Logger logger = Logger.getLogger(RicercaEvidenzaAction.class
            .getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionMessages errors = new ActionMessages();
        HttpSession session = request.getSession();
        RicercaEvidenzaForm ricercaEvidenzaForm = (RicercaEvidenzaForm) form;

        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
        Organizzazione org = Organizzazione.getInstance();
        boolean preQuery = false;

        boolean ufficioCompleto = (utente.getUfficioVOInUso().getTipo().equals(
                UfficioVO.UFFICIO_CENTRALE) || utente.getUfficioVOInUso()
                .getTipo().equals(UfficioVO.UFFICIO_SEMICENTRALE));

        if (form == null) {
            logger.info(" Creating new RicercaEvidenzaAction");
            form = new RicercaEvidenzaForm();
            ricercaEvidenzaForm.inizializzaForm();
            request.setAttribute(mapping.getAttribute(), form);
        }

        if (ricercaEvidenzaForm.getUfficioCorrenteId() == 0) {
            AlberoUfficiBO.impostaUfficio(utente, ricercaEvidenzaForm,
                    ufficioCompleto);
        }

        if (ricercaEvidenzaForm.getTitolario() == null) {
            impostaTitolario(ricercaEvidenzaForm, utente, 0);
        }

        if (request.getParameter("impostaUfficioAction") != null) {

            ricercaEvidenzaForm.setUfficioCorrenteId(ricercaEvidenzaForm
                    .getUfficioSelezionatoId());
            AlberoUfficiBO.impostaUfficio(utente, ricercaEvidenzaForm,
                    ufficioCompleto);
            impostaTitolario(ricercaEvidenzaForm, utente, 0);
            return mapping.findForward("input");
        } else if (request.getParameter("ufficioPrecedenteAction") != null) {
            ricercaEvidenzaForm.setUfficioCorrenteId(ricercaEvidenzaForm
                    .getUfficioCorrente().getParentId());
            AlberoUfficiBO.impostaUfficio(utente, ricercaEvidenzaForm,
                    ufficioCompleto);
            impostaTitolario(ricercaEvidenzaForm, utente, 0);
        } else if (request.getParameter("impostaTitolarioAction") != null) {
            if (ricercaEvidenzaForm.getTitolario() != null) {
                ricercaEvidenzaForm
                        .setTitolarioPrecedenteId(ricercaEvidenzaForm
                                .getTitolario().getId().intValue());
            }
            TitolarioBO.impostaTitolario(ricercaEvidenzaForm,
                    ricercaEvidenzaForm.getUfficioCorrenteId(),
                    ricercaEvidenzaForm.getTitolarioSelezionatoId());
            return mapping.findForward("input");
        } else if (request.getParameter("titolarioPrecedenteAction") != null) {
            TitolarioBO.impostaTitolario(ricercaEvidenzaForm,
                    ricercaEvidenzaForm.getUfficioCorrenteId(),
                    ricercaEvidenzaForm.getTitolarioPrecedenteId());
            if (ricercaEvidenzaForm.getTitolario() != null) {
                ricercaEvidenzaForm
                        .setTitolarioPrecedenteId(ricercaEvidenzaForm
                                .getTitolario().getParentId());
            }
            return mapping.findForward("input");
        } else if (request.getParameter("annullaAction") != null || preQuery) {
            // inizializzo form
            Ufficio uff = org.getUfficio(utente.getUfficioInUso());
            ricercaEvidenzaForm.setAooId(utente.getValueObject().getAooId());
            ricercaEvidenzaForm.setUfficioCorrenteId(utente.getUfficioInUso());
            ricercaEvidenzaForm.setEvidenzeProcedimenti(null);
            ricercaEvidenzaForm.setEvidenzeFascicoli(null);
            AlberoUfficiBO.impostaUfficio(utente, ricercaEvidenzaForm,
                    ufficioCompleto);
            impostaTitolario(ricercaEvidenzaForm, utente, 0);
            ricercaEvidenzaForm.setUfficioCorrente(uff.getValueObject());
            session.setAttribute(mapping.getAttribute(), ricercaEvidenzaForm);
            if (!(preQuery)) {
                return (mapping.findForward("input"));
            }
        } else if (request.getParameter("impostaTitolarioAction") != null) {
            if (ricercaEvidenzaForm.getTitolario() != null) {
                ricercaEvidenzaForm
                        .setTitolarioPrecedenteId(ricercaEvidenzaForm
                                .getTitolario().getId().intValue());
            }
            TitolarioBO.impostaTitolario(ricercaEvidenzaForm, utente
                    .getUfficioInUso(), ricercaEvidenzaForm
                    .getTitolarioSelezionatoId());

        } else if (request.getParameter("titolarioPrecedenteAction") != null) {
            TitolarioBO.impostaTitolario(ricercaEvidenzaForm, utente
                    .getRegistroInUso(), ricercaEvidenzaForm
                    .getTitolarioPrecedenteId());
            if (ricercaEvidenzaForm.getTitolario() != null) {
                ricercaEvidenzaForm
                        .setTitolarioPrecedenteId(ricercaEvidenzaForm
                                .getTitolario().getParentId());
            }
        } else if (request.getParameter("btnCercaEvidenze") != null
                || preQuery == true) {
            EvidenzaDelegate ed = EvidenzaDelegate.getInstance();
            ricercaEvidenzaForm.setEvidenzeProcedimenti(null);
            ricercaEvidenzaForm.setEvidenzeFascicoli(null);
            if (ricercaEvidenzaForm.getFascicoliProcedimenti().equals("F")) {

                HashMap hashMapFascicoli = getParametriRicercaFascicoli(
                        ricercaEvidenzaForm, request);
                int maxRighe=Integer.parseInt(utente.getAreaOrganizzativa().getMaxRighe());

                int contaRigheFascicoli = ed.contaEvidenzeFascicoli(utente,
                        hashMapFascicoli);
               
                if (contaRigheFascicoli <= maxRighe) {
                    if (contaRigheFascicoli == 0) {
                        errors.add("nessun_dato", new ActionMessage(
                                "nessun_dato"));
                        saveErrors(request, errors);
                        return mapping.findForward("input");
                    } else {
                        Collection evidenzeFascicoli = new ArrayList();
                        evidenzeFascicoli = ed.getEvidenzeFascicoli(utente,
                                hashMapFascicoli);
                        ricercaEvidenzaForm
                                .setEvidenzeFascicoli(evidenzeFascicoli);
                    }
                } else {
                    errors.add("controllo.maxrighe", new ActionMessage(
                            "controllo.maxrighe", "" + contaRigheFascicoli,
                            "evidenze", "" + maxRighe));

                }

            } else if (ricercaEvidenzaForm.getFascicoliProcedimenti().equals(
                    "P")) {

                HashMap hashMapProcedimenti = getParametriRicercaProcedimenti(
                        ricercaEvidenzaForm, request);
                int maxRighe=Integer.parseInt(utente.getAreaOrganizzativa().getMaxRighe());
                int contaRigheProcedimenti = ed.contaEvidenzeProcedimenti(
                        utente, hashMapProcedimenti);

                if (contaRigheProcedimenti <= maxRighe) {
                    if (contaRigheProcedimenti == 0) {
                        errors.add("nessun_dato", new ActionMessage(
                                "nessun_dato"));
                        saveErrors(request, errors);
                        return mapping.findForward("input");
                    } else {
                        Collection evidenzeProcedimenti = new ArrayList();
                        evidenzeProcedimenti = ed.getEvidenzeProcedimenti(
                                utente, hashMapProcedimenti);
                        ricercaEvidenzaForm
                                .setEvidenzeProcedimenti(evidenzeProcedimenti);
                    }
                } else {
                    errors.add("controllo.maxrighe", new ActionMessage(
                            "controllo.maxrighe", "" + contaRigheProcedimenti,
                            "evidenze", "" + maxRighe));

                }

            }

        } else if (request.getParameter("btnReset") != null) {
            ricercaEvidenzaForm.inizializzaForm();
            TitolarioBO.impostaTitolario(ricercaEvidenzaForm, utente
                    .getUfficioInUso(), ricercaEvidenzaForm
                    .getTitolarioSelezionatoId());

        }

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        return (mapping.findForward("input"));
    }

    private void impostaTitolario(RicercaEvidenzaForm form, Utente utente,
            int titolarioId) {
        int ufficioId;
        if (utente.getAreaOrganizzativa().getDipendenzaTitolarioUfficio() == 1) {
            ufficioId = form.getUfficioCorrenteId();
        } else {
            ufficioId = utente.getUfficioInUso();
        }
        TitolarioBO.impostaTitolario(form, ufficioId, titolarioId);
    }

    public static HashMap getParametriRicercaProcedimenti(
            RicercaEvidenzaForm form, HttpServletRequest request)
            throws Exception {
        Date dataEvidenzaDa;
        Date dataEvidenzaA;

        HashMap sqlDB = new HashMap();
        sqlDB.clear();

        if (DateUtil.isData(form.getDataEvidenzaDa())) {
            dataEvidenzaDa = DateUtil.toDate(form.getDataEvidenzaDa());
            sqlDB.put("P.DATA_EVIDENZA >=?", dataEvidenzaDa);
        }
        if (DateUtil.isData(form.getDataEvidenzaA())) {
            dataEvidenzaA = DateUtil.toDate(form.getDataEvidenzaA());
            sqlDB.put("P.DATA_EVIDENZA  <= ?", dataEvidenzaA);
        }
        if (form.getUfficioSelezionatoId() > 0) {
            Integer ufficio = new Integer(form.getUfficioCorrenteId());
            sqlDB.put("P.UFFICIO_ID=?", ufficio);
        }

        if (form.getTitolario() != null) {
            Integer titolarioId = new Integer(form.getTitolario().getId()
                    .intValue());
            sqlDB.put("P.titolario_id = ?", titolarioId);
        }

        if (sqlDB.isEmpty())
            return null;
        return sqlDB;
    }

    public static HashMap getParametriRicercaFascicoli(
            RicercaEvidenzaForm form, HttpServletRequest request)
            throws Exception {
        Date dataEvidenzaDa;
        Date dataEvidenzaA;

        HashMap sqlDB = new HashMap();
        sqlDB.clear();

        if (DateUtil.isData(form.getDataEvidenzaDa())) {
            dataEvidenzaDa = DateUtil.toDate(form.getDataEvidenzaDa());
            sqlDB.put("F.DATA_EVIDENZA >= ?", dataEvidenzaDa);
        }
        if (DateUtil.isData(form.getDataEvidenzaA())) {
            dataEvidenzaA = DateUtil.toDate(form.getDataEvidenzaA());
            sqlDB.put("F.DATA_EVIDENZA  <= ?", dataEvidenzaA);
        }
        if (form.getUfficioSelezionatoId() > 0) {
            Integer ufficio = new Integer(form.getUfficioCorrenteId());
            sqlDB.put("F.UFFICIO_INTESTATARIO_ID=?", ufficio);
        }

        if (form.getTitolario() != null) {
            Integer titolarioId = new Integer(form.getTitolario().getId()
                    .intValue());
            sqlDB.put("F.titolario_id = ?", titolarioId);
        }

        if (sqlDB.isEmpty())
            return null;
        return sqlDB;
    }

}
