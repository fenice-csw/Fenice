package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.InvioFascicolo;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.FascicoloDelegate;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.business.TitoliDestinatarioDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.FascicoloForm;
import it.finsiel.siged.mvc.presentation.helper.DestinatarioView;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.InvioFascicoliVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
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

public class InvioFascicoloAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    static Logger logger = Logger.getLogger(InvioFascicoloAction.class
            .getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionMessages errors = new ActionMessages();
        ActionMessages messages = new ActionMessages();
        HttpSession session = request.getSession();
        FascicoloForm fascicoloForm = (FascicoloForm) form;

        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
        session.setAttribute("fascicoloForm", fascicoloForm);

        if (form == null) {
            logger.info(" Creating new InvioFascicoloAction");
            form = new FascicoloForm();
            request.setAttribute(mapping.getAttribute(), form);
        }
        if (request.getParameter("aggiungiDestinatario") != null) {

            errors = fascicoloForm.validateInvioProtocollo(mapping, request);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
            } else {
                aggiungiDestinatario(fascicoloForm, session);
            }

        } else if (request.getParameter("rimuoviDestinatari") != null) {
            String[] destinatari = fascicoloForm.getDestinatarioSelezionatoId();
            if (destinatari != null) {
                for (int i = 0; i < destinatari.length; i++) {
                    fascicoloForm.rimuoviDestinatario(destinatari[i]);
                    destinatari[i] = null;
                }
            } else {
                errors.add("rimuoviDestinatari", new ActionMessage(
                        "destinatario_rimuovi"));
                saveErrors(request, errors);
            }
        } else if (request.getParameter("cercaDestinatari") != null) {
            session.setAttribute("tornaInvioFascicolo", Boolean.TRUE);
            if ("F".equals(fascicoloForm.getTipoDestinatario())) {
                request.setAttribute("cognome", fascicoloForm
                        .getNominativoDestinatario());
                session.setAttribute("provenienza","personaFisicaFascicolo");
                return mapping.findForward("cercaPersonaFisica");
            } else {
                request.setAttribute("descrizioneDitta", fascicoloForm
                        .getNominativoDestinatario());
                session.setAttribute("provenienza","personaGiuridicaFacicolo");
                return mapping.findForward("cercaPersonaGiuridica");
            }
        } else if (request.getParameter("destinatarioId") != null
                && !request.getParameter("destinatarioId").equals("")) {
            String destinatarioId = (String) request
                    .getParameter("destinatarioId");
            aggiornaDestinatarioForm(destinatarioId, fascicoloForm);

        } else if (request.getParameter("btnAnnullaInvioProtocollo") != null) {
            return mapping.findForward("tornaFascicolo");
        } else if (request.getParameter("btnInvioProtocollo") != null) {
            errors = fascicoloForm.validateInvioProtocollo(mapping, request);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
            } else {
                InvioFascicolo invioF = new InvioFascicolo();
                aggiornaInvioFascicoloModel(fascicoloForm, invioF, utente);
                FascicoloDelegate fd = FascicoloDelegate.getInstance();

                String[] allegati = fascicoloForm
                        .getDocumentiAllegatiSelezionati();
                if (allegati != null) {
                    for (int i = 0; i < allegati.length; i++) {
                        if (Integer.parseInt(allegati[i]) == fascicoloForm
                                .getDocumentoPrincipaleSelezionato()) {
                            errors.add("documento", new ActionMessage(
                                    "selezione_allegati_invio_fascicolo", "",
                                    ""));
                            break;
                        }
                    }
                }
                if (!errors.isEmpty()) {
                    saveErrors(request, errors);
                } else {
                    if (fd.inviaFascicolo(invioF, utente.getValueObject()
                            .getUsername(),fascicoloForm.getVersione()) == 1) {
                        messages.add("invio_fascicolo", new ActionMessage(
                                "fascicolo_registrato", "Inviato",
                                "al protocollo"));
                        saveMessages(request, messages);
                    } else {
                        errors.add("invio_fascicolo", new ActionMessage(
                                "errore_nel_salvataggio"));
                    }
                    saveErrors(request, errors);
                    request.setAttribute("fascicoloId", new Integer(
                            fascicoloForm.getId()));
                    return mapping.findForward("tornaFascicolo");
                }
            }

        } else if (request.getParameter("btnAnnullaAction") != null) { 
            fascicoloForm.setDestinatarioSelezionatoId(null);
            fascicoloForm.setDocumentoPrincipaleSelezionato(0);
            fascicoloForm.rimuoviDestinatari();
        }
        return (mapping.findForward("input"));
    }

   

    private void aggiornaInvioFascicoloModel(FascicoloForm form,
            InvioFascicolo invioFascicolo, Utente utente) {
        invioFascicolo.setFascicoloId(form.getId());
        aggiornaDestinatariModel(form, invioFascicolo, utente);
        aggiornaDocumentiModel(form, invioFascicolo, utente);
    }

    private void aggiornaDocumentiModel(FascicoloForm form,
            InvioFascicolo invioFascicolo, Utente utente) {
        Collection documentiInvio = new ArrayList();
        int documentoPrincipaleId = form.getDocumentoPrincipaleSelezionato();
        documentiInvio.add(getDocumentoInvio(form, documentoPrincipaleId, "1",
                utente));
        String[] documenti = form.getDocumentiAllegatiSelezionati();
        if (documenti != null) {
            for (int i = 0; i < documenti.length; i++) {
                int documentoId = (new Integer(documenti[i])).intValue();
                documentiInvio.add(getDocumentoInvio(form, documentoId, "0",
                        utente));
            }
        }
        invioFascicolo.setDocumenti(documentiInvio);
    }

    private InvioFascicoliVO getDocumentoInvio(FascicoloForm form,
            int documentoId, String flagPrincipale, Utente utente) {
        int ufficioMittenteId = utente.getUfficioInUso();
        int utenteMittenteId = utente.getValueObject().getId().intValue();
        int aooId = utente.getUfficioVOInUso().getAooId();
        InvioFascicoliVO ifVO = new InvioFascicoliVO();
        ifVO.setAooId(aooId);
        ifVO.setDocumentoId(documentoId);
        ifVO.setFascicoloId(form.getId());
        ifVO.setFlagDocumentoPrincipale(flagPrincipale);
        ifVO.setUfficioMittenteId(ufficioMittenteId);
        ifVO.setUtenteMittenteId(utenteMittenteId);

        return ifVO;
    }

  

    private void aggiornaDestinatariModel(FascicoloForm form,
            InvioFascicolo invioF, Utente utente) {
        invioF.removeDestinatari();
        Collection destinatari = form.getDestinatari();
        // boolean spedito = false;
        if (destinatari != null) {
            for (Iterator i = destinatari.iterator(); i.hasNext();) {
                DestinatarioView dest = (DestinatarioView) i.next();
                DestinatarioVO destinatario = new DestinatarioVO();
                destinatario.setDestinatario(dest.getDestinatario());
                destinatario.setFlagTipoDestinatario(dest
                        .getFlagTipoDestinatario());
                destinatario.setEmail(dest.getEmail());
                destinatario.setIndirizzo(dest.getIndirizzo());
                destinatario.setCodicePostale(dest.getCapDestinatario());
                destinatario.setNote(dest.getNote());
                destinatario.setCitta(dest.getCitta());
                destinatario
                        .setTitoloDestinatario(dest.getTitoloDestinatario());
                // if (dest.getDataSpedizione() != null) {
                // spedito = true;
                // }
                destinatario.setDataSpedizione(DateUtil.toDate(dest
                        .getDataSpedizione()));
                destinatario.setFlagConoscenza(dest.getFlagConoscenza());
                invioF.addDestinatari(destinatario);
            }
        }
    }

 
    
    
    private void aggiornaDestinatarioForm(String id, FascicoloForm form) {
        DestinatarioView destinatario = new DestinatarioView();
        form.setFlagConoscenza(false);
        destinatario = form.getDestinatario(id);
        form.setIdx(Integer.parseInt(id));
        form.setTipoDestinatario(destinatario.getFlagTipoDestinatario());
        form.setNominativoDestinatario(destinatario.getDestinatario());
        // if (form.getIndirizzoDestinatario()== null &&
        // (form.getIndirizzoDestinatario().equals(""))) {
        // form.setCitta("_");
        // }
        // else{
        String citta = "";
        if (destinatario.getCapDestinatario() != null) {
            citta = destinatario.getCapDestinatario();
        }
        if (destinatario.getCitta() != null) {
            if (citta.equals("")) {
                citta = destinatario.getCitta();
            } else {
                citta = citta + ' ' + destinatario.getCitta();
            }
        }
        form.setCitta(StringUtil.getStringa(citta));
        // }
        form.setEmailDestinatario(destinatario.getEmail());
        form.setIndirizzoDestinatario(destinatario.getIndirizzo());
        form.setCapDestinatario(destinatario.getCapDestinatario());
        form.setMezzoSpedizioneId(destinatario.getMezzoSpedizioneId());
        form.setTitoloDestinatario(TitoliDestinatarioDelegate.getInstance()
                .getTitoloDestinatario(destinatario.getTitoloId())
                .getDescription());
        form.setTitoloId(destinatario.getTitoloId());
        form.setDataSpedizione(destinatario.getDataSpedizione());
        form.setFlagConoscenza(destinatario.getFlagConoscenza());
        form.setNote(destinatario.getNote());
        form.setDestinatarioMezzoId(destinatario.getDestinatarioMezzoId());
    }

    
    

    private void aggiungiDestinatario(FascicoloForm form,
            HttpSession session) {
        DestinatarioView destinatario = new DestinatarioView();
        destinatario.setFlagTipoDestinatario(form.getTipoDestinatario());
        destinatario.setDestinatario(form.getNominativoDestinatario().trim());
        destinatario.setEmail(form.getEmailDestinatario());
        destinatario.setCapDestinatario(form.getCapDestinatario());
        destinatario.setCitta(form.getCitta());
        destinatario.setIndirizzo(form.getIndirizzoDestinatario());
        destinatario.setDataSpedizione(form.getDataSpedizione());
        destinatario.setMezzoSpedizioneId(form.getMezzoSpedizioneId());

        destinatario.setIdx(form.getIdx());
        String desc = LookupDelegate.getInstance().getDescMezzoSpedizione(
                form.getMezzoSpedizioneId()).getDescrizioneSpedizione();
        destinatario.setMezzoDesc(desc);
        destinatario.setPrezzoSpedizione(LookupDelegate.getInstance().getDescMezzoSpedizione(
                form.getMezzoSpedizioneId()).getPrezzo());
        destinatario.setTitoloDestinatario("--");
        destinatario.setTitoloId(form.getTitoloId());
        if (form.getTitoloId() > 0) {
            destinatario.setTitoloDestinatario(TitoliDestinatarioDelegate
                    .getInstance().getTitoloDestinatario(form.getTitoloId())
                    .getDescription());
        }
        destinatario.setNote(form.getNote());
        destinatario.setFlagConoscenza(form.getFlagConoscenza());
        form.aggiungiDestinatario(destinatario);
        form.inizializzaDestinatarioForm();
    }
    
    
}