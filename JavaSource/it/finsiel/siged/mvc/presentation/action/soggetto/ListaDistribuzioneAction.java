package it.finsiel.siged.mvc.presentation.action.soggetto;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.SoggettoDelegate;
import it.finsiel.siged.mvc.presentation.actionform.soggetto.ListaDistribuzioneForm;
import it.finsiel.siged.mvc.vo.ListaDistribuzioneVO;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.util.NumberUtil;

import java.util.ArrayList;
import java.util.Iterator;

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
 * Implementation of <strong>Action </strong> to create a new E-Photo User.
 * 
 * @author
 * 
 */

public class ListaDistribuzioneAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    static Logger logger = Logger.getLogger(ListaDistribuzioneAction.class
            .getName());

    // --------------------------------------------------------- Public Methods
    public ActionForward execute(ActionMapping mapping, ActionForm ldForm,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionMessages errors = new ActionMessages();// Report any errors we
        SoggettoDelegate delegate = SoggettoDelegate.getInstance();
        HttpSession session = request.getSession(); // we create one if does
        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);

        ListaDistribuzioneVO listaDistribuzione = new ListaDistribuzioneVO(
                utente.getAreaOrganizzativa().getId().intValue());
        ListaDistribuzioneForm listaDistribuzioneForm = (ListaDistribuzioneForm) ldForm;

        String p_descrizioneDitta = (String) request
                .getAttribute("descrizioneDitta");

        boolean preQuery = (!"".equals(p_descrizioneDitta) && p_descrizioneDitta != null);

        if (ldForm == null) {
            listaDistribuzioneForm = new ListaDistribuzioneForm();
            listaDistribuzioneForm.setTipo("F");
            session.setAttribute(mapping.getAttribute(),listaDistribuzioneForm);
        } else if (request.getParameter("annullaAction") != null) {
            if ("true".equals(request.getParameter("annullaAction"))
                    || listaDistribuzioneForm.getCodice() == 0) {
                session.removeAttribute("tornaProtocollo");
                listaDistribuzioneForm.inizializzaForm();
            }
            if (Boolean.TRUE.equals(session.getAttribute("tornaProtocollo"))) {
                session.removeAttribute("tornaProtocollo");
                return (mapping.findForward("tornaProtocollo"));
            }
            saveToken(request);
            return mapping.findForward("input");
        }

        if (request.getParameter("cercaAction") != null) {
            String desc = listaDistribuzioneForm.getDescrizione();
            ArrayList elenco = null;
            if (desc == null || "".equals(desc)) {
                // mostra tutto
                elenco = delegate.getElencoListeDistribuzioneEsterna();
            } else {
                // mostra con filtro su descrizione
                elenco = delegate.getElencoListaDistribuzioneEsterna(desc, utente
                        .getAreaOrganizzativa().getId().intValue());
            }
            if (elenco != null) {
                listaDistribuzioneForm.setElencoListaDistribuzione(elenco);

            } else {
                listaDistribuzioneForm
                        .setElencoListaDistribuzione(new ArrayList());

            }
            session
                    .setAttribute(mapping.getAttribute(),
                            listaDistribuzioneForm);
            return (mapping.findForward("input"));
        }
         else if (request.getParameter("nuovaAction") != null) {
    			//session.setAttribute("nuovoInCerca", true);
    			return mapping.findForward("nuova");
    		
        } else if (request.getParameter("cercaDaProtocolloAction") != null
                || request.getAttribute("nomeLista") != null) {
            String desc = listaDistribuzioneForm.getDescrizione();
            String descLista = (String) request.getAttribute("nomeLista");
            if (descLista != null)
                desc = descLista;
            ArrayList elenco = null;
            elenco = delegate.getElencoListaDistribuzioneEsterna(desc, utente
                    .getAreaOrganizzativa().getId().intValue());
            if (elenco != null) {
                listaDistribuzioneForm.setElencoListaDistribuzione(elenco);
            } else {
                listaDistribuzioneForm
                        .setElencoListaDistribuzione(new ArrayList());
            }
            listaDistribuzioneForm.setDescrizione("");
            session
                    .setAttribute(mapping.getAttribute(),
                            listaDistribuzioneForm);
            return (mapping.findForward("inputTornaProtocollo"));
        } else if (request.getParameter("indietroLD") != null) {
            {
                if (session.getAttribute("provenienza").equals(
                        "listaDistribuzioneProtocollo"))

                    return (mapping.findForward("tornaProtocollo"));
            }
        }

        else if (request.getParameter("salvaAction") != null) {
            readLdForm(listaDistribuzione, listaDistribuzioneForm);
            errors = listaDistribuzioneForm.validateDatiInserimento(mapping,
                    request);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return (mapping.findForward("input"));
            }

            ListaDistribuzioneVO listaSalvata = delegate
                    .salvaListaDistribuzioneEsterna(listaDistribuzione, utente,
                            listaDistribuzioneForm
                                    .getElencoSoggettiListaDistribuzione());
           
            if (listaSalvata.getReturnValue() != ReturnValues.SAVED
                    && listaSalvata.getReturnValue() != ReturnValues.EXIST_DESCRIPTION) {

                // // show error
                errors.add("registrazione_tipo", new ActionMessage(
                        "record_non_inseribile", "la lista di distribuzione,",
                        " esiste gi� con lo stesso nome "));
                saveErrors(request, errors);
                return (mapping.findForward("input"));

            } else {
                request.setAttribute("operazioneEffettuata", "true");

            }
            listaDistribuzioneForm.setCodice(listaSalvata.getId().intValue());
            return (mapping.findForward("input"));
        }

        else if (request.getParameter("cercaSoggettiAction") != null) {
            listaDistribuzioneForm.setListaPersoneLD(null);
            String tipoSogg = listaDistribuzioneForm.getTipo();
            if ("F".equals(tipoSogg)) {
                request.setAttribute("cognome", listaDistribuzioneForm
                        .getCognome());
                request.setAttribute("nome", "");
                return (mapping.findForward("cercaSoggettoPF"));
            } else {
                // session.setAttribute("tornaListaPG", Boolean.TRUE);
                request.setAttribute("descrizioneDitta", listaDistribuzioneForm
                        .getDescrizioneDitta());
                return (mapping.findForward("cercaSoggettoPG"));
            }
        } else if (request.getParameter("parId") != null) {
            int parId = (new Integer(request.getParameter("parId"))).intValue();
            listaDistribuzione = delegate.getListaDistribuzione(parId);
            ArrayList destinatari = null;
            if (listaDistribuzione != null) {
                destinatari = delegate
                        .getDestinatariListaDistribuzione(listaDistribuzione
                                .getId().intValue());
            }
            writeLdForm(listaDistribuzioneForm, listaDistribuzione, destinatari);

            return (mapping.findForward("input"));
        } else if (request.getParameter("indietroPF") != null) {

            return (mapping.findForward("tornaLista"));
        } else if (request.getParameter("indietroPG") != null) {

            return (mapping.findForward("tornaLista"));
        }

        else if (request.getParameter("rimuoviSoggetti") != null) {
            String[] soggetti = listaDistribuzioneForm
                    .getSoggettiSelezionatiId();
            if (soggetti != null) {
                for (int i = 0; i < soggetti.length; i++) {
                    listaDistribuzioneForm.rimuoviSoggetto(soggetti[i]);
                }
            } else {
                errors.add("rimuoviSoggetti", new ActionMessage(
                        "soggetto_rimuovi"));
                saveErrors(request, errors);
            }
            listaDistribuzioneForm.setSoggettiSelezionatiId(null);
        } else if (request.getParameter("parIdPG") != null) {
            int pgId = NumberUtil.getInt(request.getParameter("parIdPG"));
            listaDistribuzioneForm.aggiungiSoggetto(delegate
                    .getPersonaGiuridica(pgId));
            return (mapping.findForward("tornaLista"));
        } else if (request.getParameter("parIdPF") != null) {
            int pgId = NumberUtil.getInt(request.getParameter("parIdPF"));
            listaDistribuzioneForm.aggiungiSoggetto(delegate
                    .getPersonaFisica(pgId));
            return (mapping.findForward("tornaLista"));
        } else if (request.getParameter("cercaPG") != null || preQuery) {
            p_descrizioneDitta = listaDistribuzioneForm.getDescrizioneDitta();
            if (!"".equals(p_descrizioneDitta) && p_descrizioneDitta != null) {
                listaDistribuzioneForm.setDescrizioneDitta(p_descrizioneDitta);
                listaDistribuzioneForm.setListaPersoneLD(delegate
                        .getListaPersonaGiuridica(utente.getAreaOrganizzativa()
                                .getId().intValue(), listaDistribuzioneForm
                                .getDescrizioneDitta(), listaDistribuzioneForm
                                .getPartitaIva(),null));
            }
            errors = listaDistribuzioneForm.validateLDPG(mapping, request);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("cercaSoggettoPG");
            }
            return (mapping.findForward("cercaSoggettoPG"));
        } else if (request.getParameter("cercaPF") != null || preQuery) {
            String cognome = listaDistribuzioneForm.getCognome();
            if (!"".equals(cognome) && cognome != null) {
                listaDistribuzioneForm.setListaPersoneLD(delegate
                        .getListaPersonaFisica(utente.getAreaOrganizzativa()
                                .getId().intValue(), listaDistribuzioneForm
                                .getCognome(),
                                listaDistribuzioneForm.getNome(), null,null));
            }
            errors = listaDistribuzioneForm.validateLDPG(mapping, request);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("cercaSoggettoPF");
            }
            return (mapping.findForward("cercaSoggettoPF"));

        } else if (request.getParameter("annulla") != null) {
            session.removeAttribute("tornaProtocollo");
            session.removeAttribute("tornaInvioFascicolo");
        } else if (request.getParameter("nuova") != null) {
            return (mapping.findForward("nuova"));

        } else if (listaDistribuzioneForm.getDeleteAction() != null) {
            
            delegate.cancellaListaDistribuzione(listaDistribuzioneForm
                    .getCodice());
            listaDistribuzioneForm.inizializzaForm();
            return (mapping.findForward("cerca"));
        } else if (request.getParameter("annulla") != null) {
            session.removeAttribute("tornaProtocollo");
            session.removeAttribute("tornaInvioFascicolo");
        } else if (request.getParameter("cercaSoggettiAction") != null) {
            return (mapping.findForward("input"));
        }
        return (mapping.findForward("input"));
    }

    public static void readLdForm(ListaDistribuzioneVO listaDistribuzione,
            ListaDistribuzioneForm ldForm) {
        listaDistribuzione.setId(ldForm.getCodice());
        listaDistribuzione.setDescription(ldForm.getDescrizione());
    }

    public static void writeLdForm(ListaDistribuzioneForm ldForm,
            ListaDistribuzioneVO listaDistribuzione, ArrayList destinatari) {
        ldForm.setCodice(listaDistribuzione.getId().intValue());
        ldForm.setDescrizione(listaDistribuzione.getDescription());
        if (destinatari != null) {
            Iterator it = destinatari.iterator();
            while (it.hasNext()) {
                SoggettoVO s = (SoggettoVO) it.next();
                ldForm.aggiungiSoggetto(s);
            }
        }

    }

    public void caricaDatiNelVO(ListaDistribuzioneVO vo,
            ListaDistribuzioneForm ldForm, Utente utente) {
        vo.setId(ldForm.getCodice());
        vo.setDescription(ldForm.getDescrizione());

    }

}
