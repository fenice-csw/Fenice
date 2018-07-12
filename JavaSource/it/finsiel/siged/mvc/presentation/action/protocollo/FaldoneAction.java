package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.Faldone;
import it.finsiel.siged.model.Fascicolo;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.AlberoUfficiBO;
import it.finsiel.siged.mvc.bo.TitolarioBO;
import it.finsiel.siged.mvc.business.FaldoneDelegate;
import it.finsiel.siged.mvc.business.FascicoloDelegate;
import it.finsiel.siged.mvc.business.ProcedimentoDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.FaldoneForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;
import it.finsiel.siged.mvc.vo.ImpiegatiVO;
import it.finsiel.siged.mvc.vo.ImpiegatoVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.protocollo.FaldoneVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.ProcedimentoVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

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

public class FaldoneAction extends Action {

    static Logger logger = Logger.getLogger(FaldoneAction.class.getName());

    final static int STATO_FALDONE_TRATTAZIONE = 0;

    final static int STATO_FALDONE_ATTI = 1;

    final static int STATO_FALDONE_EVIDENZA = 2;

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionMessages errors = new ActionMessages();
        HttpSession session = request.getSession();
        FaldoneForm faldoneForm = (FaldoneForm) form;
        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
        boolean ufficioCompleto = (utente.getUfficioVOInUso().getTipo().equals(
                UfficioVO.UFFICIO_CENTRALE) || utente.getUfficioVOInUso()
                .getTipo().equals(UfficioVO.UFFICIO_SEMICENTRALE));
        Organizzazione org = Organizzazione.getInstance();

        if (form == null
                || "true".equals(request.getParameter("annullaAction"))
                || request.getParameter("btnNuovoFaldone") != null) {
            // inizializzo form
            faldoneForm = new FaldoneForm();
            faldoneForm.inizializzaForm();
            MessageResources bundle = (MessageResources) request
                    .getAttribute(Globals.MESSAGES_KEY);
            faldoneForm.setCollocazioneLabel1(bundle
                    .getMessage("faldone.collocazione.label1"));
            faldoneForm.setCollocazioneLabel2(bundle
                    .getMessage("faldone.collocazione.label2"));
            faldoneForm.setCollocazioneLabel3(bundle
                    .getMessage("faldone.collocazione.label3"));
            faldoneForm.setCollocazioneLabel4(bundle
                    .getMessage("faldone.collocazione.label4"));

            int annoCorrente = utente.getRegistroVOInUso().getAnnoCorrente();
            faldoneForm.setAnnoCorrente(annoCorrente);
            faldoneForm.setAooId(utente.getRegistroVOInUso().getAooId());
            faldoneForm.setDipendenzaTitolarioUfficio(org.getAreaOrganizzativa(
                    utente.getRegistroVOInUso().getAooId()).getValueObject()
                    .getDipendenzaTitolarioUfficio());
            Ufficio uff = org.getUfficio(utente.getUfficioInUso());
            int titolarioId = 0;

            if (session.getAttribute("protocolloForm") != null) {
                ProtocolloForm pForm = (ProtocolloForm) session
                        .getAttribute("protocolloForm");
                if (pForm != null && pForm.getTitolario() != null) {
                    titolarioId = pForm.getTitolario().getId().intValue();
                }
            }
            impostaNuovoFaldoneForm(titolarioId, faldoneForm, utente);
            AlberoUfficiBO.impostaUfficio(utente, faldoneForm, ufficioCompleto);
            faldoneForm.setUfficioCorrente(uff.getValueObject());
            session.setAttribute(mapping.getAttribute(), faldoneForm);
        }

        // ------------------------------------- //
        if (faldoneForm.getUfficioCorrenteId() == 0) {
            AlberoUfficiBO.impostaUfficio(utente, faldoneForm, ufficioCompleto);
        }

        if (faldoneForm.getTitolario() == null) {
            impostaTitolario(faldoneForm, utente, 0);
        }

        if (request.getParameter("caricaFaldoneId") != null
                || request.getAttribute("caricaFaldoneId") != null) {
            int faldoneId = NumberUtil.getInt(request
                    .getParameter("caricaFaldoneId"));
            if (request.getAttribute("caricaFaldoneId") != null)
                faldoneId = NumberUtil.getInt((String) request
                        .getAttribute("caricaFaldoneId"));
            FaldoneVO faldoneVO = new FaldoneVO();
            faldoneVO = FaldoneDelegate.getInstance().getFaldone(faldoneId);
            //String statoFaldone = FaldoneDelegate.getInstance().getStatoFaldone(faldoneVO.getPosizioneSelezionata()).getDescription();
           // faldoneVO.setStato(statoFaldone);
            Collection ids = FaldoneDelegate.getInstance()
                    .getFaldoneFascicoliIds(faldoneId);
            Collection idp = FaldoneDelegate.getInstance()
                    .getFaldoneProcedimentiIds(faldoneId);
            faldoneForm.inizializzaForm();
            caricaDatiNelForm(faldoneForm, faldoneVO, utente);
            faldoneForm.setVersioneDefault(true);
            caricaFascicoliFaldone(faldoneForm, ids, errors);
            caricaProcedimentiFaldone(faldoneForm, idp, errors);
            AlberoUfficiBO.impostaUfficio(utente, faldoneForm, ufficioCompleto);

            TitolarioBO.impostaTitolario(faldoneForm, faldoneVO.getUfficioId(),
                    faldoneVO.getTitolarioId());
            // impostaTitolario(faldoneForm, utente, 0);
            saveErrors(request, errors);
            return (mapping.findForward("visualizzaFaldone"));

        } else if (request.getParameter("impostaUfficioAction") != null) {
            faldoneForm.setUfficioCorrenteId(faldoneForm
                    .getUfficioSelezionatoId());
            AlberoUfficiBO.impostaUfficio(utente, faldoneForm, ufficioCompleto);
            impostaTitolario(faldoneForm, utente, 0);
            return mapping.findForward("input");
        } else if (request.getParameter("ufficioPrecedenteAction") != null) {
            faldoneForm.setUfficioCorrenteId(faldoneForm.getUfficioCorrente()
                    .getParentId());
            AlberoUfficiBO.impostaUfficio(utente, faldoneForm, ufficioCompleto);
            impostaTitolario(faldoneForm, utente, 0);
        } else if (request.getParameter("impostaTitolarioAction") != null) {
            if (faldoneForm.getTitolario() != null) {
                faldoneForm.setTitolarioPrecedenteId(faldoneForm.getTitolario()
                        .getId().intValue());
            }
            TitolarioBO.impostaTitolario(faldoneForm, faldoneForm
                    .getUfficioCorrenteId(), faldoneForm
                    .getTitolarioSelezionatoId());
            return mapping.findForward("input");
        } else if (request.getParameter("titolarioPrecedenteAction") != null) {
            TitolarioBO.impostaTitolario(faldoneForm, faldoneForm
                    .getUfficioCorrenteId(), faldoneForm
                    .getTitolarioPrecedenteId());
            if (faldoneForm.getTitolario() != null) {
                faldoneForm.setTitolarioPrecedenteId(faldoneForm.getTitolario()
                        .getParentId());
            }
            return mapping.findForward("input");
        }// ---------------------------------------------------------------//
        // ======== CERCA FASCICOLI ============= //
        else if (request.getParameter("btnCercaFascicoliDaFaldoni") != null) {
            session.setAttribute("tornaFaldone", Boolean.TRUE);
            String nomeFascicolo = faldoneForm.getCercaFascicoloNome();
            faldoneForm.setCercaFascicoloNome("");
            request.setAttribute("cercaFascicoliDaFaldoni", nomeFascicolo);
            /* request.setAttribute("btnCercaFascicoli", "true"); */
            request.setAttribute("provenienza", "FascicoliDaFaldoni");
            session.setAttribute("cercaFascicoliDaFaldoni", nomeFascicolo);
            session.setAttribute("btnCercaFascicoli", "true");
            session.setAttribute("provenienza", "FascicoliDaFaldoni");
            session.removeAttribute("elencoProtocolliDaProcedimento");
            session.removeAttribute("indietroProtocolliDaProcedimento");
            session.removeAttribute("tornaProcedimento");
            session.removeAttribute("tornaProtocollo");
            return (mapping.findForward("cercaFascicoli"));
            // ------------------------------------- //
            // ======== RIMUOVI FASCICOLI SELEZIONATI ============= //
        } else if (request.getParameter("rimuoviFascicoli") != null) {
            String[] fascicoliSelezionati = faldoneForm
                    .getFascicoliSelezionati();
            for (int i = 0; fascicoliSelezionati != null
                    && i < fascicoliSelezionati.length; i++) {
                faldoneForm.rimuoviFascicolo(NumberUtil
                        .getInt(fascicoliSelezionati[i]));
            }
            faldoneForm.setFascicoliSelezionati(null);
            return (mapping.findForward("input"));
            // ------------------------------------- //
            // ======== NUOVO FASCICOLO ============= //
        } else if (request.getParameter("btnNuovoFascicolo") != null) {
            session.setAttribute("tornaFaldone", Boolean.TRUE);
            request.setAttribute("codiceLocaleFaldone", faldoneForm
                    .getCodiceLocale());
            request.setAttribute("oggettoFaldone", faldoneForm.getOggetto());
            request.setAttribute("btnNuovo", "true");
            return (mapping.findForward("nuovoFascicolo"));
            // ------------------------------------- //
            // ======== RIMUOVI PROCEDIMENTI SELEZIONATI ============= //
        } else if (request.getParameter("rimuoviProcedimenti") != null) {
            String[] procedimentiSelezionati = faldoneForm
                    .getProcedimentiSelezionati();
            for (int i = 0; procedimentiSelezionati != null
                    && i < procedimentiSelezionati.length; i++) {
                faldoneForm.rimuoviProcedimento(NumberUtil
                        .getInt(procedimentiSelezionati[i]));
            }
            faldoneForm.setProcedimentiSelezionati(null);
            return (mapping.findForward("input"));
        }// -----------------------------------------------------------//
        // ======== CERCA PROCEDIMENTI ============= //
        else if (request.getParameter("btnCercaProcedimentiDaFaldoni") != null) {
            session.setAttribute("tornaFaldone", Boolean.TRUE);
            String nome = faldoneForm.getCercaProcedimentoNome();
            faldoneForm.setCercaProcedimentoNome("");
            request.setAttribute("cercaProcedimentiDaFaldoni", nome);
            request.setAttribute("btnCercaProcedimentiDaFaldoni", "true");
            session.setAttribute("provenienza", "procedimentiDaFaldoni");
            session.setAttribute("indietroProcedimentiDaFaldoni", Boolean.TRUE);
            session.removeAttribute("risultatiProcedimentiDaProtocollo");
            session.removeAttribute("indietroProcedimentiDaProtocollo");
            session.removeAttribute("tornaFascicolo");

            return (mapping.findForward("cercaProcedimenti"));
            // ======== SALVA FALDONE ============= //
        } else if (request.getParameter("salvaAction") != null) {
            errors = faldoneForm.validate(mapping, request);
            FaldoneDelegate fd = FaldoneDelegate.getInstance();

            if (errors.isEmpty()) {
                Faldone faldone = new Faldone();
                // String statoFaldone = fd.getStatoFaldone(
                // faldoneForm.getPosizioneSelezionata()).getDescription();
                // faldoneForm.setStatoFaldone(statoFaldone);
                // if (faldoneForm.getPosizioneSelezionata() ==
                // STATO_FALDONE_ATTI) {
                // faldoneForm.setModificabile(false);
                // }
                // if (faldoneForm.getTitolario() == null) {
                // impostaTitolario(faldoneForm, utente, 0);
                // }
                if (faldoneForm.getFaldoneId() > 0) {
                    // modifica faldone
                    FaldoneVO fOld = fd.getFaldone(faldoneForm.getFaldoneId());
                    // Se lo stato del faldone � passato da evidenza in
                    // trattazione o in atti,
                    // azzerare la data evidenza
                    if ((fOld.getPosizioneSelezionata() == STATO_FALDONE_EVIDENZA)
                            && (faldoneForm.getPosizioneSelezionata() == STATO_FALDONE_TRATTAZIONE)) {
                        faldoneForm.setDataEvidenza("");
                    } else if ((fOld.getPosizioneSelezionata() == STATO_FALDONE_EVIDENZA)
                            && (faldoneForm.getPosizioneSelezionata() == STATO_FALDONE_ATTI)) {
                        faldoneForm.setDataEvidenza("");
                        if (faldoneForm.getDataScarico() == null
                                || "".equals(faldoneForm.getDataScarico())) {
                            boolean mod = faldoneComposizioneModificata(fOld,
                                    faldoneForm);
                            if (!mod) {
                                errors.add("dataScarico", new ActionMessage(
                                        "campo.obbligatorio", "Data scarico",
                                        ""));
                                saveErrors(request, errors);
                                return (mapping.findForward("input"));
                            }
                        }
                    }
                    // Se lo stato del faldone � passato da atti in trattazione,
                    // azzerare la data evidenza
                    else if ((fOld.getPosizioneSelezionata() == STATO_FALDONE_ATTI)
                            && (faldoneForm.getPosizioneSelezionata() == STATO_FALDONE_TRATTAZIONE)) {
                        faldoneForm.setDataEvidenza("");
                    }
                    // Se lo stato del faldone � passato da trattazione ad atti,
                    // testare e rendere obbligatoria la data scarico
                    else if ((fOld.getPosizioneSelezionata() == STATO_FALDONE_TRATTAZIONE)
                            && (faldoneForm.getPosizioneSelezionata() == STATO_FALDONE_ATTI)) {
                        if (faldoneForm.getDataScarico() == null
                                || "".equals(faldoneForm.getDataScarico())) {
                            errors.add("dataScarico", new ActionMessage(
                                    "campo.obbligatorio", "Data scarico", ""));
                            saveErrors(request, errors);
                            return (mapping.findForward("input"));
                        }
                    } else if ((fOld.getPosizioneSelezionata() == STATO_FALDONE_TRATTAZIONE)
                            && (faldoneForm.getPosizioneSelezionata() == STATO_FALDONE_EVIDENZA)) {
                        // Se lo stato del faldone � passato da trattazione ad
                        // evidenza,
                        // rendere obbligatorie la data scarico e quella di
                        // evidenza
                        if (faldoneForm.getDataScarico() == null
                                || "".equals(faldoneForm.getDataScarico())) {
                            boolean mod = faldoneComposizioneModificata(fOld,
                                    faldoneForm);
                            if (!mod) {
                                errors.add("dataScarico", new ActionMessage(
                                        "campo.obbligatorio", "Data scarico",
                                        ""));
                                saveErrors(request, errors);
                                return (mapping.findForward("input"));
                            }
                        }

                    }
                    if (faldoneForm.getPosizioneSelezionata() == STATO_FALDONE_EVIDENZA) {
                        if (faldoneForm.getDataEvidenza() == null
                                || "".equals(faldoneForm.getDataEvidenza())) {
                            errors.add("dataEvidenza", new ActionMessage(
                                    "campo.obbligatorio", "Data evidenza", ""));
                            saveErrors(request, errors);
                            return (mapping.findForward("input"));
                        }
                    }
                    // if (faldoneForm.getPosizioneSelezionataId() == 0
                    // && (!faldoneForm.getDataEvidenza().equals(""))) {
                    // faldoneForm.setDataEvidenza("");
                    // f.setDataEvidenza(null);
                    // faldone.setFaldoneVO(f);
                    // }

                    if (faldoneComposizioneModificata(fOld, faldoneForm)) {
                        faldoneForm.setDataMovimento(DateUtil
                                .formattaData(System.currentTimeMillis()));
                    }
                    if (faldoneModificato(fOld, faldoneForm)) {
                        if (faldoneForm.getDataScarico() == null
                                || "".equals(faldoneForm.getDataScarico())) {
                            errors.add("dataScarico", new ActionMessage(
                                    "campo.obbligatorio", "Data scarico",
                                    "(se si modifica almeno uno tra Ufficio, Titolario, Posizione, "
                                            + "Data evidenza, Oggetto)"));
                            saveErrors(request, errors);
                            return (mapping.findForward("input"));
                        }
                    } else {
                        if (faldoneForm.getDataScarico() != null
                                && !"".equals(faldoneForm.getDataScarico())) {
                            errors
                                    .add(
                                            "dataScarico",
                                            new ActionMessage(
                                                    "modifica.obbligatoria",
                                                    "Ufficio, Titolario, Posizione, Data evidenza, Oggetto",
                                                    "se si imposta la Data scarico"));
                            saveErrors(request, errors);
                            return (mapping.findForward("input"));
                        }
                    }
                    faldone = impostaFaldone(faldoneForm, utente);
                    FaldoneVO faldoneVO = fd.salvaFaldone(faldone, utente);
                    caricaDatiNelForm(faldoneForm, faldoneVO, utente);
                    if (faldoneVO.getReturnValue() == ReturnValues.SAVED) {
                        errors.add("faldone", new ActionMessage(
                                "faldone_modificato", "Modificato", ""));
                        saveErrors(request, errors);
                        return (mapping.findForward("visualizzaFaldone"));
                    }

                } else {
                    // inserimento nuovo faldone
                    faldone = impostaFaldone(faldoneForm, utente);
                    FaldoneVO faldoneVO = fd.salvaFaldone(faldone, utente);
                    caricaDatiNelForm(faldoneForm, faldoneVO, utente);

                    if (faldoneVO.getReturnValue() == ReturnValues.SAVED) {
                        errors.add("faldone", new ActionMessage(
                                "faldone_registrato", "Registrato", faldoneVO
                                        .getNumeroFaldone()));
                        saveErrors(request, errors);
                        faldoneForm.setVersioneDefault(true);
                        session.removeAttribute("tornaFaldone");
                        return (mapping.findForward("visualizzaFaldone"));
                    }

                }
            }
        } else if (request.getParameter("btnStoria") != null) {
            request.setAttribute("faldoneId", new Integer(faldoneForm
                    .getFaldoneId()));
            caricaFaldone(request, faldoneForm);
            return (mapping.findForward("storiaFaldone"));
            // ------------------------------------- //
            // ======== CARICA FALDONE ============= //
        } else if (request.getParameter("btnModificaFaldone") != null) {
            faldoneForm.setMatricola("");
            faldoneForm.setMatricole(null);
            return (mapping.findForward("input"));
        } else if (request.getParameter("btnRiapriFaldone") != null) {
            faldoneForm.setPosizioneSelezionata(STATO_FALDONE_TRATTAZIONE);
            /*
            String statoFaldone = FaldoneDelegate.getInstance()
                    .getStatoFaldone(faldoneForm.getPosizioneSelezionata())
                    .getDescription();
            faldoneForm.setStatoFaldone(statoFaldone);
*/
            Faldone faldone = impostaFaldone(faldoneForm, utente);
            faldone.setFaldoneVO(faldone.getFaldoneVO());
            faldone.setFascicoli(faldoneForm.getFascicoliFaldoneCollection());
            faldone.setProcedimenti(faldoneForm
                    .getProcedimentiFaldoneCollection());
            faldone.getFaldoneVO().setDataScarico(
                    new Date(System.currentTimeMillis()));
            FaldoneVO faldoneVO = FaldoneDelegate.getInstance().salvaFaldone(
                    faldone, utente);
            caricaDatiNelForm(faldoneForm, faldoneVO, utente);

            if (faldoneVO.getReturnValue() == ReturnValues.SAVED) {
                errors.add("faldone", new ActionMessage("faldone_modificato",
                        "Modificato", ""));
                saveErrors(request, errors);
                faldoneForm.setVersione(faldoneForm.getVersione() + 1);
                return (mapping.findForward("visualizzaFaldone"));
            }
            return (mapping.findForward("visualizzaFaldone"));
        } else if (request.getParameter("btnChiudiFaldone") != null) {
            faldoneForm.setPosizioneSelezionata(STATO_FALDONE_ATTI);
            /*
            String statoFaldone = FaldoneDelegate.getInstance()
                    .getStatoFaldone(faldoneForm.getPosizioneSelezionata())
                    .getDescription();
            faldoneForm.setStatoFaldone(statoFaldone);
            */
            faldoneForm.setDataEvidenza("");
            Faldone faldone = impostaFaldone(faldoneForm, utente);
            faldone.setFaldoneVO(faldone.getFaldoneVO());
            faldone.setFascicoli(faldoneForm.getFascicoliFaldoneCollection());
            faldone.setProcedimenti(faldoneForm
                    .getProcedimentiFaldoneCollection());
            // faldoneVO.setDataScarico(new Date(System.currentTimeMillis()));
            FaldoneVO faldoneVO = FaldoneDelegate.getInstance().salvaFaldone(
                    faldone, utente);
            caricaDatiNelForm(faldoneForm, faldoneVO, utente);

            if (faldoneVO.getReturnValue() == ReturnValues.SAVED) {
                errors.add("faldone", new ActionMessage("faldone_modificato",
                        "Modificato", ""));
                saveErrors(request, errors);
                faldoneForm.setVersione(faldoneForm.getVersione() + 1);
                return (mapping.findForward("visualizzaFaldone"));
            }
            return (mapping.findForward("visualizzaFaldone"));
        } else if (request.getParameter("visualizzaFascicoloId") != null) {
            if (NumberUtil.isInteger(request
                    .getParameter("visualizzaFascicoloId"))) {
                Integer fId = Integer.valueOf(request
                        .getParameter("visualizzaFascicoloId"));

                FascicoloDelegate delegate = FascicoloDelegate.getInstance();
                Ufficio uff = org.getUfficio(utente.getUfficioVOInUso().getId()
                        .intValue());
                if (delegate.isUtenteAbilitatoView(utente, uff, fId.intValue())) {
                    request.setAttribute("fascicoloId", fId);
                    return mapping.findForward("visualizzaFascicolo");
                } else {
                    errors.add("permessi.utenti.lettura", new ActionMessage(
                            "permessi.utenti.lettura", "", ""));
                    saveErrors(request, errors);
                    return (mapping.findForward("visualizzaFaldone"));
                }

            } else {
                logger.warn("Id Fascicolo non di formato numerico:"
                        + request.getParameter("visualizzaFascicoloId"));
            }
            return mapping.findForward("input");
        } else if (request.getParameter("btnRiapri") != null) {
            request.setAttribute("faldoneId", new Integer(faldoneForm
                    .getFaldoneId()));
            request.setAttribute("versioneId", new Integer(faldoneForm
                    .getVersione()));
            // caricaFaldone(request, faldoneForm);
            faldoneForm.setOperazione(FaldoneForm.RIAPERTURA_FALDONE);
            return (mapping.findForward("confermaOperazione"));
        } else if (request.getParameter("btnCancella") != null) {
            request.setAttribute("faldoneId", new Integer(faldoneForm
                    .getFaldoneId()));
            caricaFaldone(request, faldoneForm);
            faldoneForm.setOperazione(FaldoneForm.CANCELLAZIONE_FALDONE);
            return (mapping.findForward("confermaOperazione"));
        } else if (request.getParameter("btnCancellaFaldone") != null) {
            errors = faldoneForm.validate(mapping, request);
            if (errors.isEmpty()) {
                int fId = faldoneForm.getFaldoneId();
                int retVal = FaldoneDelegate.getInstance().cancellaFaldone(fId);
                // retVal � = a VALID se il faldone � stato cancellato
                if (retVal != ReturnValues.VALID) {
                    errors.add("faldone", new ActionMessage(
                            "record_non_cancellabile", " il Faldone ",
                            "selezionato"));
                } else {
                    return mapping.findForward("faldoneCancellato");
                }
            }
            saveErrors(request, errors);
            return mapping.findForward("visualizzaFaldone");
        } else if (request.getAttribute("faldoneId") != null) {
            caricaFaldone(request, faldoneForm);
            if (faldoneForm.getVersione() == 0) {
                faldoneForm.setVersione(1);
            }
            return (mapping.findForward("visualizzaFaldone"));
        } else if (request.getAttribute("btnAnnulla") != null) {
            caricaFaldone(request, faldoneForm);
            return (mapping.findForward("input"));
        } else if (request.getParameter("btnAnnullaRiapri") != null) {
            faldoneForm.setPosizioneSelezionata(STATO_FALDONE_ATTI);
/*
            String statoFaldone = FaldoneDelegate.getInstance()
                    .getStatoFaldone(faldoneForm.getPosizioneSelezionata())
                    .getDescription();
            faldoneForm.setStatoFaldone(statoFaldone);
  */          
            faldoneForm.setDataEvidenza("");
            Faldone faldone = impostaFaldone(faldoneForm, utente);
            faldone.setFaldoneVO(faldone.getFaldoneVO());
            caricaDatiNelForm(faldoneForm, faldone.getFaldoneVO(), utente);
            return (mapping.findForward("visualizzaFaldone"));
        } else if (request.getParameter("btnAnnullaChiudi") != null) {
            faldoneForm.setPosizioneSelezionata(STATO_FALDONE_TRATTAZIONE);
    /*
            String statoFaldone = FaldoneDelegate.getInstance()
                    .getStatoFaldone(faldoneForm.getPosizioneSelezionata())
                    .getDescription();
            faldoneForm.setStatoFaldone(statoFaldone);
      */      
            // faldoneForm.setDataEvidenza("");
            Faldone faldone = impostaFaldone(faldoneForm, utente);
            faldone.setFaldoneVO(faldone.getFaldoneVO());
            caricaDatiNelForm(faldoneForm, faldone.getFaldoneVO(), utente);
            // caricaFaldone(request, faldoneForm);
            return (mapping.findForward("input"));
        } else if (request.getParameter("btnAnnullaCancella") != null) {
            // caricaFaldone(request, faldoneForm);
            return (mapping.findForward("visualizzaFaldone"));
        } else if (request.getParameter("caricaMatricolaAction") != null) {

            faldoneForm.setMatricola(null);

            try {

                if (faldoneForm.getCodiceLocale().equals("")
                        && faldoneForm.getOggetto().equals("")) {
                    errors.add("faldone",
                            new ActionMessage("faldone_matricole"));
                    saveErrors(request, errors);
                    return (mapping.findForward("input"));
                }
                // Create a URL for the desired page
                MessageResources bundle = (MessageResources) request
                        .getAttribute(Globals.MESSAGES_KEY);

                String urlApp = bundle
                        .getMessage("faldone.URL.RICERCADELPERSONALE")
                        + "?";
                if ((faldoneForm.getCodiceLocale() != null)
                        && !(faldoneForm.getCodiceLocale().equals(""))) {
                    urlApp = urlApp + "matricola="
                            + faldoneForm.getCodiceLocale() + "";
                    if ((faldoneForm.getOggetto() != null)
                            && (!faldoneForm.getOggetto().equals(""))) {
                        urlApp = urlApp + "&cognomenome="
                                + faldoneForm.getOggetto() + "";
                    }
                } else {
                    if ((faldoneForm.getOggetto() != null)
                            && (!faldoneForm.getOggetto().equals(""))) {
                        urlApp = urlApp + "cognomenome="
                                + faldoneForm.getOggetto() + "";
                    }
                }
                logger.info("URL:  " + urlApp);
                URL url = new URL(urlApp);

                String str = "";
                String riga = "";
                // Apro connessione
                URLConnection urlConn = url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        urlConn.getInputStream()));

                while ((riga = in.readLine()) != null) {
                    logger.info("riga: " + riga);
                    str = str + riga;
                    logger.info("str: " + str);
                    // str is one line of text; readLine() strips the newline
                    // character(s)
                }

                logger.info("str finale: " + str);
                /* String str=""; */

                // in.close();
                if (str.equals("")) {
                    errors.add("faldone", new ActionMessage("file_xml_vuoto"));
                    saveErrors(request, errors);
                    return (mapping.findForward("input"));

                }
                ImpiegatiVO elenco = ImpiegatiVO.parse(str);
                faldoneForm.setNumeroMatricole(elenco.getListaImpiegato()
                        .toArray().length);
                for (Iterator ImpiegatoVO = elenco.getListaImpiegato()
                        .iterator(); ImpiegatoVO.hasNext();) {
                    ImpiegatoVO element = (ImpiegatoVO) ImpiegatoVO.next();
                    element.setNomeCompleto(element.getMatricola() + " - "
                            + element.getCognome() + " - " + element.getNome()
                            + " - " + element.getDataNascita() + " - "
                            + element.getComune() + "("
                            + element.getProvincia() + ")" + " - "
                            + element.getQualifica());
                    if (elenco.getListaImpiegato().toArray().length == 1) {
                        faldoneForm.setOggetto(element.getCognome() + " "
                                + element.getNome() + " "
                                + element.getQualifica());
                        faldoneForm.setCodiceLocale(element.getMatricola());

                    }
                }
                faldoneForm.setMatricole(elenco.getListaImpiegato());

                if (elenco.getListaImpiegato().toArray().length == 1) {
                    return (mapping.findForward("input"));
                }
             
                return (mapping.findForward("input"));
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("Errore:  " + e);
                errors.add("faldone",
                        new ActionMessage("file_xml_non_corretto"));
                saveErrors(request, errors);
                return (mapping.findForward("input"));

            }
        } else if (request.getParameter("impostaMatricolaAction") != null) {
            ImpiegatiVO elenco = new ImpiegatiVO();
            Collection el = new ArrayList();
            el = faldoneForm.getMatricole();

            elenco.setListaImpiegato(el);

            ImpiegatoVO impiegato = elenco.getImpiegato(faldoneForm
                    .getMatricola());
            faldoneForm.setOggetto(impiegato.getCognome() + " "
                    + impiegato.getNome() + " " + " - "
                    + impiegato.getQualifica());
            faldoneForm.setCodiceLocale(faldoneForm.getMatricola());
            return (mapping.findForward("input"));
        }

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }

        return (mapping.findForward("input"));
    }

    private Faldone impostaFaldone(FaldoneForm fForm, Utente utente) {
        Faldone faldone = new Faldone();
        faldone.setFaldoneVO(impostaFaldoneVO(fForm, utente));
        faldone.setProcedimenti(fForm.getProcedimentiFaldoneCollection());
        faldone.setFascicoli(fForm.getFascicoliFaldoneCollection());
        return faldone;
    }

    private FaldoneVO impostaFaldoneVO(FaldoneForm fForm, Utente utente) {
        FaldoneVO fVO = new FaldoneVO();
        fVO.setId(fForm.getFaldoneId());
        fVO.setAnno(fForm.getAnnoCorrente());
        fVO.setDataMovimento(DateUtil.toDate(fForm.getDataMovimento()));
        fVO.setCollocazioneLabel1(fForm.getCollocazioneLabel1());
        fVO.setCollocazioneLabel2(fForm.getCollocazioneLabel2());
        fVO.setCollocazioneLabel3(fForm.getCollocazioneLabel3());
        fVO.setCollocazioneLabel4(fForm.getCollocazioneLabel4());
        fVO.setCollocazioneValore1(fForm.getCollocazioneValore1());
        fVO.setCollocazioneValore2(fForm.getCollocazioneValore2());
        fVO.setCollocazioneValore3(fForm.getCollocazioneValore3());
        fVO.setCollocazioneValore4(fForm.getCollocazioneValore4());
        fVO.setPosizioneSelezionata(fForm.getPosizioneSelezionata());
        fVO.setCodiceLocale(fForm.getCodiceLocale());
        fVO.setId(fForm.getFaldoneId());
        fVO.setNota(fForm.getNota());
        fVO.setOggetto(fForm.getOggetto());
        /*
        fVO.setStato(FaldoneDelegate.getInstance().getStatoFaldone(
                fForm.getPosizioneSelezionata()).getDescription());
        */
        // titolario
        if (fForm.getTitolario() != null) {
            fVO.setTitolarioId(fForm.getTitolario().getId().intValue());
        }

        fVO.setUfficioId(fForm.getUfficioCorrenteId());

        if (fVO.getId().intValue() == 0) {
            fVO.setRowCreatedUser(utente.getValueObject().getUsername());
            fVO.setRowCreatedTime(new Date(System.currentTimeMillis()));
            fVO.setDataCarico(new Date(System.currentTimeMillis()));
            fVO.setNumeroFaldone(fForm.getNumeroFaldone());
            fVO.setNumero(fForm.getNumero());
            fVO.setAooId(utente.getUfficioVOInUso().getAooId());
        } else {
            fVO.setRowCreatedTime(fForm.getRowCreatedTime());
            fVO.setRowCreatedUser(fForm.getRowCreatedUser());
            fVO.setRowUpdatedUser(utente.getValueObject().getUsername());
            Date dataCarico = DateUtil.toDate(fForm.getDataCarico());
            if (dataCarico != null) {
                fVO.setDataCarico(new java.sql.Date(dataCarico.getTime()));
            } else {
                fVO.setDataCarico(null);
            }
        }
        fVO.setRowUpdatedTime(new Date(System.currentTimeMillis()));
        Date dataCreazione = DateUtil.toDate(fForm.getDataCarico());
        if (dataCreazione != null) {
            fVO.setDataCreazione(new java.sql.Date(dataCreazione.getTime()));
        } else {
            fVO.setDataCreazione(null);
        }
        Date dataEvidenza = DateUtil.toDate(fForm.getDataEvidenza());
        if (dataEvidenza != null) {
            fVO.setDataEvidenza(new java.sql.Date(dataEvidenza.getTime()));
        } else {
            fVO.setDataEvidenza(null);
        }
        Date dataScarico = DateUtil.toDate(fForm.getDataScarico());
        if (dataScarico != null) {
            fVO.setDataScarico(new java.sql.Date(dataScarico.getTime()));
        } else {
            fVO.setDataScarico(null);
        }
        fVO.setVersione(fForm.getVersione());
        return fVO;
    }

    private boolean faldoneModificato(FaldoneVO faldoneOld,
            FaldoneForm faldoneForm) {
        String dataOld = (faldoneOld.getDataEvidenza() == null ? "" : DateUtil
                .formattaData(faldoneOld.getDataEvidenza().getTime()));
        if (faldoneOld.getUfficioId() != faldoneForm.getUfficioCorrenteId()) {
            return true;
        } else if (!faldoneOld.getOggetto().equals(faldoneForm.getOggetto())) {
            return true;
        } else if (faldoneOld.getTitolarioId() != faldoneForm.getTitolarioId()) {
            return true;
        } else if (faldoneOld.getPosizioneSelezionata() != faldoneForm
                .getPosizioneSelezionata()) {
            return true;
        } else if (!(dataOld.equals(faldoneForm.getDataEvidenza()))) {
            return true;
        } else
            return false;
    }

    private boolean faldoneComposizioneModificata(FaldoneVO fVO,
            FaldoneForm faldoneForm) {
        FaldoneDelegate fd = FaldoneDelegate.getInstance();
        Collection procVO = fd
                .getFaldoneProcedimentiIds(fVO.getId().intValue());
        Collection fascVO = fd.getFaldoneFascicoliIds(fVO.getId().intValue());
        Collection fascForm = faldoneForm.getFascicoliFaldoneCollection();
        Collection procForm = faldoneForm.getProcedimentiFaldoneCollection();
        ArrayList fascFormList = new ArrayList();

        if (fascForm.size() > 0) {
            for (Iterator iter = fascForm.iterator(); iter.hasNext();) {
                Fascicolo fa = (Fascicolo) iter.next();
                FascicoloVO faVO = fa.getFascicoloVO();
                fascFormList.add(faVO.getId());
            }
        }

        ArrayList procFormList = new ArrayList();

        if (procForm.size() > 0) {
            for (Iterator iter = procForm.iterator(); iter.hasNext();) {
                ProcedimentoVO prVO = (ProcedimentoVO) iter.next();
                procFormList.add(prVO.getId());
            }
        }

        if (!(fascVO.equals(fascFormList))) {
            return true;
        } else if (!(procVO.equals(procFormList))) {
            return true;
        } else
            return false;
    }

    protected void caricaFaldone(HttpServletRequest request, FaldoneForm form) {
        Integer faldoneId = (Integer) request.getAttribute("faldoneId");
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
        FaldoneVO faldone = new FaldoneVO();
        Integer versioneId = (Integer) request.getAttribute("versioneId");
        if (faldoneId != null) {
            FaldoneDelegate fd = FaldoneDelegate.getInstance();
            if (versioneId == null) {
                faldone = fd.getFaldone(faldoneId.intValue());
                form.setVersioneDefault(true);
            } else {
                int versione = versioneId.intValue();
                int id = faldoneId.intValue();

                int versioneCorrente = fd.getFaldone(faldoneId.intValue())
                        .getVersione();
                if (versioneCorrente > versione) {
                    form.setVersioneDefault(false);
                    faldone = fd.getFaldoneByIdVersione(id, versione);
                } else {
                    form.setVersioneDefault(true);
                    faldone = fd.getFaldone(id);
                }

            }
            session.setAttribute(Constants.FALDONE, faldone.getId());

        } else {
            faldone = (FaldoneVO) session.getAttribute(Constants.FALDONE);
        }
        caricaDatiNelForm(form, faldone, utente);
    }

    private void aggiornaSatoFaldoneForm(FaldoneVO faldone, FaldoneForm fForm,
            Utente utente) {
        boolean modificabile = false;
        // if (faldone.getPosizioneSelezionata() !=
        // Parametri.STATO_FALDONE_AGLI_ATTI) {
        if (utente.getUfficioVOInUso().getParentId() == 0
                || utente.isUtenteAbilitatoSuUfficio(faldone.getUfficioId())) {
            modificabile = true;
        }
        // }
        fForm.setModificabile(modificabile);
    }

    public void caricaDatiNelForm(FaldoneForm form, FaldoneVO vo, Utente utente) {
        Organizzazione org = Organizzazione.getInstance();
        form.setAooId(vo.getAooId());
        form.setUfficioId(vo.getUfficioId());
        form.setTitolarioId(vo.getTitolarioId());
        form.setCodiceLocale(vo.getCodiceLocale());
        form.setFaldoneId(vo.getId().intValue());
        form.setNota(vo.getNota());
        form.setAnnoCorrente(vo.getAnno());
        form.setNumero(vo.getNumero());
        form.setNumeroFaldone(vo.getNumeroFaldone());
        form.setOggetto(vo.getOggetto());
        form.setSottoCategoria(vo.getSottoCategoria());
        form.setCollocazioneLabel1(vo.getCollocazioneLabel1());
        form.setCollocazioneLabel2(vo.getCollocazioneLabel2());
        form.setCollocazioneLabel3(vo.getCollocazioneLabel3());
        form.setCollocazioneLabel4(vo.getCollocazioneLabel4());
        form.setCollocazioneValore1(vo.getCollocazioneValore1());
        form.setCollocazioneValore2(vo.getCollocazioneValore2());
        form.setCollocazioneValore3(vo.getCollocazioneValore3());
        form.setCollocazioneValore4(vo.getCollocazioneValore4());
        form.setRowCreatedTime(vo.getRowCreatedTime());
        if (vo.getRowCreatedUser() != null) {
            if (org.getUtente(vo.getRowCreatedUser()) != null) {
                form.setRowCreatedUser(org.getUtente(vo.getRowCreatedUser())
                        .getValueObject().getFullName());
            } else {
                form.setRowCreatedUser(vo.getRowCreatedUser());
            }
        }
        form.setRowUpdatedTime(vo.getRowUpdatedTime());
        if (vo.getRowUpdatedUser() != null) {
            if (org.getUtente(vo.getRowUpdatedUser()) != null) {
                form.setRowUpdatedUser(org.getUtente(vo.getRowUpdatedUser())
                        .getValueObject().getFullName());
            } else {
                form.setRowUpdatedUser(vo.getRowUpdatedUser());
            }
        }
        form.setStatoFaldone(vo.getStato());
        form.setVersione(vo.getVersione());
        form.setPosizioneSelezionata(vo.getPosizioneSelezionata());
        //form.setStatoFaldone(FaldoneDelegate.getInstance().getStatoFaldone(vo.getPosizioneSelezionata()).getDescription());
        if (vo.getDataCreazione() != null) {
            form.setDataCreazione(DateUtil.formattaData(vo.getDataCreazione()
                    .getTime()));
        }
        if (vo.getDataCarico() != null) {
            form.setDataCarico(DateUtil.formattaData(vo.getDataCarico()
                    .getTime()));
        }
        if (vo.getDataScarico() != null) {
            form.setDataScarico(DateUtil.formattaData(vo.getDataScarico()
                    .getTime()));
        } else
            form.setDataScarico("");
        if (vo.getDataEvidenza() != null) {
            form.setDataEvidenza(DateUtil.formattaData(vo.getDataEvidenza()
                    .getTime()));
        }
        if (vo.getDataMovimento() != null) {
            form.setDataMovimento(DateUtil.formattaData(vo.getDataMovimento()
                    .getTime()));
        }
        form.setTitolarioSelezionatoId(vo.getTitolarioId());
        form.setUfficioCorrenteId(vo.getUfficioId());
        aggiornaSatoFaldoneForm(vo, form, utente);
    }

    private void impostaTitolario(FaldoneForm form, Utente utente,
            int titolarioId) {
        int ufficioId = utente.getUfficioInUso();
        if (utente.getAreaOrganizzativa().getDipendenzaTitolarioUfficio() == 1) {
            ufficioId = form.getUfficioCorrenteId();
        }
        TitolarioBO.impostaTitolario(form, ufficioId, titolarioId);
    }

    private void impostaNuovoFaldoneForm(int titolarioId, FaldoneForm faldone,
            Utente utente) {
        faldone.inizializzaForm();
        faldone.setFaldoneId(0);
        faldone.setUfficioId(utente.getUfficioInUso());
        faldone.setUtenteSelezionatoId(utente.getValueObject().getId()
                .intValue());
        faldone.setDataApertura(DateUtil.formattaData((new Date(System
                .currentTimeMillis())).getTime()));
        faldone.setDataCreazione(DateUtil.formattaData((new Date(System
                .currentTimeMillis())).getTime()));
        faldone.setDataCarico(DateUtil.formattaData((new Date(System
                .currentTimeMillis())).getTime()));
        faldone.setUfficioCorrenteId(utente.getUfficioInUso());
        faldone.setAooId(utente.getAreaOrganizzativa().getId().intValue());
        // Collection tipiDoc = LookupDelegate.getInstance().getTipiDocumento(
        // faldone.getAooId());
        impostaTitolario(faldone, utente, titolarioId);
    }

    private void caricaProcedimentiFaldone(FaldoneForm form, Collection ids,
            ActionMessages errors) {
        Iterator it = ids.iterator();
        while (it.hasNext()) {
            Integer id = (Integer) it.next();
            ProcedimentoVO p = new ProcedimentoVO();
            p = ProcedimentoDelegate.getInstance().getProcedimentoVO(
                    id.intValue());
            if (p.getReturnValue() == ReturnValues.FOUND) {
                form.aggiungiProcedimento(p);
            } else {
                errors.add("procedimenti", new ActionMessage(
                        "procedimento_non_trovato", String.valueOf(id
                                .intValue())));
            }
        }
    }

    private void caricaFascicoliFaldone(FaldoneForm form, Collection ids,
            ActionMessages errors) {
        Iterator it = ids.iterator();
        while (it.hasNext()) {
            Integer id = (Integer) it.next();
            Fascicolo f = new Fascicolo();
            FascicoloVO vo = FascicoloDelegate.getInstance()
                    .getFascicoloVOById(id.intValue());
            if (vo.getReturnValue() == ReturnValues.FOUND) {
                f.setFascicoloVO(vo);
                form.aggiungiFascicolo(f);
            } else {
                errors
                        .add("fascicoli", new ActionMessage(
                                "fascicolo_non_trovato", String.valueOf(id
                                        .intValue())));
            }
        }
    }

}