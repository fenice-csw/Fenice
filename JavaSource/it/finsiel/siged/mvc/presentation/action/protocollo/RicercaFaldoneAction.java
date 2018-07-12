package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.AlberoUfficiBO;
import it.finsiel.siged.mvc.bo.TitolarioBO;
import it.finsiel.siged.mvc.business.FaldoneDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProcedimentoForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.RicercaFaldoneForm;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.protocollo.FaldoneVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

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

public class RicercaFaldoneAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    static Logger logger = Logger.getLogger(RicercaFaldoneAction.class
            .getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionMessages errors = new ActionMessages();
        HttpSession session = request.getSession();
        RicercaFaldoneForm ricercaForm = (RicercaFaldoneForm) form;
        Organizzazione org = Organizzazione.getInstance();
        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);

        boolean ufficioCompleto = (utente.getUfficioVOInUso().getTipo().equals(
                UfficioVO.UFFICIO_CENTRALE) || utente.getUfficioVOInUso()
                .getTipo().equals(UfficioVO.UFFICIO_SEMICENTRALE));
        boolean preQuery = request.getAttribute("cercaFaldoneDaProcedimento") != null;
        boolean indietroVisibile = false;
        ricercaForm.setIndietroVisibile(indietroVisibile);
        // boolean preQuery =false;

        if (form == null) {
            ricercaForm = new RicercaFaldoneForm();
            ricercaForm.inizializzaForm();
        }

        if (request.getParameter("annullaAction") != null || preQuery
                || request.getParameter("btnCercaFascicoli") != null) {

            ricercaForm.inizializzaForm();
            Ufficio uff = org.getUfficio(utente.getUfficioInUso());
            ricercaForm.setAooId(utente.getValueObject().getAooId());
            // Inseriemnto Luigi 9/3
            ricercaForm.setUfficioCorrenteId(utente.getUfficioInUso());
            // Fine inserimento Luigi 9/3
            AlberoUfficiBO.impostaUfficio(utente, ricercaForm, ufficioCompleto);
            impostaTitolario(ricercaForm, utente, 0);
            ricercaForm.setUfficioCorrente(uff.getValueObject());
            session.setAttribute(mapping.getAttribute(), ricercaForm);
            if (!(preQuery)) {
                return (mapping.findForward("input"));
            }
        }

        if (ricercaForm.getUfficioCorrenteId() == 0) {
            AlberoUfficiBO.impostaUfficio(utente, ricercaForm, ufficioCompleto);
        }

        if (ricercaForm.getTitolario() == null) {
            impostaTitolario(ricercaForm, utente, 0);
        }
        if (request.getParameter("impostaUfficioAction") != null) {
            ricercaForm.setUfficioCorrenteId(ricercaForm
                    .getUfficioSelezionatoId());
            AlberoUfficiBO.impostaUfficio(utente, ricercaForm, ufficioCompleto);
            impostaTitolario(ricercaForm, utente, 0);
            return mapping.findForward("input");
        } else if (request.getParameter("ufficioPrecedenteAction") != null) {
            ricercaForm.setUfficioCorrenteId(ricercaForm.getUfficioCorrente()
                    .getParentId());
            AlberoUfficiBO.impostaUfficio(utente, ricercaForm, ufficioCompleto);
            impostaTitolario(ricercaForm, utente, 0);
        } else if (request.getParameter("impostaTitolarioAction") != null) {
            if (ricercaForm.getTitolario() != null) {
                ricercaForm.setTitolarioPrecedenteId(ricercaForm.getTitolario()
                        .getId().intValue());
            }
            TitolarioBO.impostaTitolario(ricercaForm, ricercaForm
                    .getUfficioCorrenteId(), ricercaForm
                    .getTitolarioSelezionatoId());
            return mapping.findForward("input");
        } else if (request.getParameter("titolarioPrecedenteAction") != null) {
            TitolarioBO.impostaTitolario(ricercaForm, ricercaForm
                    .getUfficioCorrenteId(), ricercaForm
                    .getTitolarioPrecedenteId());
            if (ricercaForm.getTitolario() != null) {
                ricercaForm.setTitolarioPrecedenteId(ricercaForm.getTitolario()
                        .getParentId());
            }
            return mapping.findForward("input");
        }
        if (request.getParameter("cercaFaldoniDaProcedimento") != null) {
            indietroVisibile = true;
            ricercaForm.setIndietroVisibile(indietroVisibile);
            String oggetto = (String) request
                    .getAttribute("cercaFaldoneDaProcedimento");
            ricercaForm.setOggetto(oggetto);
            preQuery = true;
        }
        // ---------------------------------------------------------------
        // //faldoniDaProcedimento
        if (request.getParameter("btnCerca") != null || preQuery == true) {
            if ("faldoniDaProcedimento".equals(session
                    .getAttribute("provenienza"))) {
                indietroVisibile = true;
                ricercaForm.setIndietroVisibile(indietroVisibile);

            }

            // controllo numero righe di ritorno lista protocolli
            int maxRighe=Integer.parseInt(utente.getAreaOrganizzativa().getMaxRighe());
            FaldoneDelegate faldoneDelegate = FaldoneDelegate.getInstance();
            HashMap hashMap = getParametriRicerca(ricercaForm, request);
            Ufficio uff = org.getUfficio(utente.getUfficioVOInUso().getId()
                    .intValue());
            String ufficiUtenti = uff.getListaUfficiDiscendentiIdStr(utente);
            int contaRighe = faldoneDelegate.contaFaldoni(utente, ufficiUtenti,
                    hashMap);
            if (contaRighe <= maxRighe) {
                SortedMap faldoni = new TreeMap();
                faldoni = FaldoneDelegate.getInstance()
                        .cercaFaldoni(utente, ufficiUtenti,
                                getParametriRicerca(ricercaForm, request));

                if (faldoni == null || faldoni.size() == 0) {
                    errors.add("nessun_dato", new ActionMessage("nessun_dato",
                            "", ""));
                    ricercaForm.reset();
                } else {

                    ricercaForm.setFaldoni(faldoni.values());
                }

            } else {
                errors.add("controllo.maxrighe", new ActionMessage(
                        "controllo.maxrighe", "" + contaRighe, "faldoni", ""
                                + maxRighe));
            }

        } else if (request.getParameter("btnSeleziona") != null) {
            String[] faldoniSelezionati = ricercaForm.getFaldoniSelezionati();
            if ("procedimentiDaFaldoni".equals(session
                    .getAttribute("provenienza"))) {
                indietroVisibile = true;
            }
            if (Boolean.TRUE.equals(session.getAttribute("tornaProcedimento"))) {
                ProcedimentoForm fForm = (ProcedimentoForm) session
                        .getAttribute("procedimentoForm");
                for (int i = 0; faldoniSelezionati != null
                        && i < faldoniSelezionati.length; i++) {
                    FaldoneVO fa = FaldoneDelegate.getInstance().getFaldone(
                            NumberUtil.getInt(faldoniSelezionati[i]));
                    fForm.aggiungiFaldone(fa);
                }
                ricercaForm.inizializzaForm();
                session.removeAttribute("tornaProcedimento");
                return (mapping.findForward("tornaProcedimento"));
            }
        } else if (request.getParameter("btnAnnulla") != null) {
            ricercaForm.inizializzaForm();
            AlberoUfficiBO.impostaUfficio(utente, ricercaForm, ufficioCompleto);
            Ufficio uff = org.getUfficio(utente.getUfficioInUso());
            ricercaForm.setUfficioCorrente(uff.getValueObject());
            if (Boolean.TRUE.equals(session.getAttribute("tornaProcedimento"))) {
                session.removeAttribute("tornaProcedimento");
                return (mapping.findForward("tornaProcedimento"));
            }
        }

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }

        return (mapping.findForward("input"));
    }

    private void impostaTitolario(RicercaFaldoneForm form, Utente utente,
            int titolarioId) {
        /*
    	int ufficioId;
        if (utente.getAreaOrganizzativa().getDipendenzaTitolarioUfficio() == 1) {
            ufficioId = form.getUfficioCorrenteId();
        } else {
            ufficioId = utente.getUfficioInUso();
        }
        */
    	int ufficioId = utente.getUfficioInUso();
        TitolarioBO.impostaTitolario(form, ufficioId, titolarioId);
    }

    public static HashMap getParametriRicerca(RicercaFaldoneForm form,
            HttpServletRequest request) throws Exception {
        Date dataCreazioneInizio;
        Date dataCreazioneFine;

        HashMap sqlDB = new HashMap();
        sqlDB.clear();

        if (DateUtil.isData(form.getDataCreazioneInizio())) {
            dataCreazioneInizio = DateUtil
                    .toDate(form.getDataCreazioneInizio());
            sqlDB.put("data_creazione >= ?", dataCreazioneInizio);
        }
        if (DateUtil.isData(form.getDataCreazioneFine())) {
            dataCreazioneFine = DateUtil.toDate(form.getDataCreazioneFine());
            sqlDB.put("data_creazione <= ?", dataCreazioneFine);
        }
        if (NumberUtil.isInteger(form.getAnno())) {
            Integer anno = new Integer(form.getAnno());
            sqlDB.put("anno = ?", anno);
        }
        if (NumberUtil.isInteger(form.getNumero())) {
            Integer numero = new Integer(form.getNumero());
            sqlDB.put("numero = ?", numero);
        }

        if (form.getTitolario() != null) {
            Integer titolarioId = new Integer(form.getTitolario().getId()
                    .intValue());
            sqlDB.put("titolario_id = ?", titolarioId);
        }
        if (form.getUfficioRicercaId() > 0) {
            Integer uffId = new Integer(form.getUfficioRicercaId());
            sqlDB.put("ufficio_id = ?", uffId);
        }
        if (form.getOggetto() != null && !"".equals(form.getOggetto())) {
            sqlDB
                    .put(" upper(oggetto) LIKE ?", form.getOggetto()
                            .toUpperCase());
        }
        if (form.getNota() != null && !"".equals(form.getNota())) {
            sqlDB.put(" upper(nota) LIKE ?", form.getNota().toUpperCase());
        }
        if (form.getSottocategoria() != null
                && !"".equals(form.getSottocategoria())) {
            sqlDB.put(" upper(sotto_categoria) LIKE ?", form
                    .getSottocategoria().toUpperCase());
        }
        if (form.getCodiceLocale() != null
                && !"".equals(form.getCodiceLocale())) {
            sqlDB.put(" upper(codice_locale) LIKE ?", form.getCodiceLocale()
                    .toUpperCase());
        }

        if (sqlDB.isEmpty())
            return null;
        return sqlDB;
    }

}