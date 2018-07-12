package it.finsiel.siged.mvc.presentation.action.documentale;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.InvioClassificati;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.DocumentaleDelegate;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.business.TitoliDestinatarioDelegate;
import it.finsiel.siged.mvc.presentation.actionform.documentale.DestinatariInvioForm;
import it.finsiel.siged.mvc.presentation.actionform.documentale.DocumentoForm;
import it.finsiel.siged.mvc.presentation.helper.DestinatarioView;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.InvioClassificatiVO;
import it.finsiel.siged.mvc.vo.protocollo.SpedizioneVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.StringUtil;

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

public class InvioDocumentoAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    static Logger logger = Logger.getLogger(InvioDocumentoAction.class
            .getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionMessages errors = new ActionMessages();
        HttpSession session = request.getSession();
        DocumentoForm dForm = (DocumentoForm) form;
        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
        session.setAttribute("dForm", dForm);
        if (form == null) {
            logger.info(" Creating new InvioDocumentoAction");
            form = new DestinatariInvioForm();
            request.setAttribute(mapping.getAttribute(), form);
        }
        if (request.getParameter("aggiungiDestinatario") != null) {

            errors = dForm.validateDestinatario(mapping, request);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
            } else {
                aggiungiDestinatario(dForm, session);
            }

        } else if (request.getParameter("rimuoviDestinatari") != null) {
            String[] destinatari = dForm.getDestinatarioSelezionatoId();
            if (destinatari != null) {
                for (int i = 0; i < destinatari.length; i++) {
                    dForm.rimuoviDestinatario(destinatari[i]);
                    destinatari[i] = null;
                }
            } else {
                errors.add("rimuoviDestinatari", new ActionMessage(
                        "destinatario_rimuovi"));
                saveErrors(request, errors);
            }
        } else if (request.getParameter("cercaDestinatari") != null) {
            session.setAttribute("tornaInvioDocumento", Boolean.TRUE);
            if ("F".equals(dForm.getTipoDestinatario())) {
                request.setAttribute("cognome", dForm
                        .getNominativoDestinatario());
                session.setAttribute("provenienza","personaFisicaDocumento");
                return mapping.findForward("cercaPersonaFisica");
            } else {
                request.setAttribute("descrizioneDitta", dForm
                        .getNominativoDestinatario());
                session.setAttribute("provenienza","personaGiuridicaDocumento");
                return mapping.findForward("cercaPersonaGiuridica");
            }
        } else if (request.getParameter("destinatarioId") != null) {
            String nomeDestinatario = (String) request
                    .getParameter("destinatarioId");
            aggiornaDestinatarioForm(nomeDestinatario, dForm);

        } else if (request.getParameter("btnInvioProtocollo") != null) {
            errors = dForm.validateDestinatari(mapping, request);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
            } else {
                InvioClassificati invioC = new InvioClassificati();
                aggiornaInvioClassificatiModel(dForm, invioC, utente);
                DocumentaleDelegate dd = DocumentaleDelegate.getInstance();

                if (!errors.isEmpty()) {
                    saveErrors(request, errors);
                } else {

                    if (dd.invioClassificati(invioC) == ReturnValues.SAVED) {
                        errors.add("invio_classificati", new ActionMessage(
                                "documentale_esito_operazione", "Inviato",
                                "al protocollo"));
                    } else {
                        errors.add("invio_classificati", new ActionMessage(
                                "errore_nel_salvataggio"));
                    }

                    saveErrors(request, errors);
                    request.setAttribute("documentoId", new Integer(dForm
                            .getDocumentoId()));
                    return mapping.findForward("visualizzaDocumento");
                }
            }

        } else if (request.getParameter("btnAnnullaAction") != null) {
            dForm.setDestinatarioSelezionatoId(null);
            dForm.rimuoviDestinatari();
        }
        return (mapping.findForward("input"));
    }

    private void aggiornaInvioClassificatiModel(DocumentoForm form,
            InvioClassificati invioClassificati, Utente utente) {
        int ufficioMittenteId = utente.getUfficioInUso();
        int utenteMittenteId = utente.getValueObject().getId().intValue();
        int aooId = utente.getUfficioVOInUso().getAooId();
        InvioClassificatiVO icVO = new InvioClassificatiVO();
        icVO.setAooId(aooId);
        icVO.setDocumentoId(form.getDocumentoId());
        icVO.setUfficioMittenteId(ufficioMittenteId);
        icVO.setUtenteMittenteId(utenteMittenteId);
        icVO.setProcedimentoId(form.getProcedimentoId());
        invioClassificati.setIcVO(icVO);
        aggiornaDestinatariModel(form, invioClassificati, utente);
    }
    
    private void aggiornaDestinatariModel(DocumentoForm form,
            InvioClassificati invioC, Utente utente) {
        invioC.removeDestinatari();
        Collection<DestinatarioView> destinatari = form.getDestinatari();
        if (destinatari != null) {
            for (Iterator<DestinatarioView> i = destinatari.iterator(); i.hasNext();) {
                DestinatarioView dest =  i.next();
                DestinatarioVO destinatario = new DestinatarioVO();
                destinatario.setDestinatario(dest.getDestinatario());
                destinatario.setFlagTipoDestinatario(dest
                        .getFlagTipoDestinatario());
                destinatario.setEmail(dest.getEmail());
                destinatario.setCodicePostale(dest.getCapDestinatario());
                if (dest.getCapDestinatario() != null
                        && !(dest.getCapDestinatario().equals(""))) {
                    destinatario.setIndirizzo(dest.getCapDestinatario());
                }
                if (dest.getIndirizzo() != null
                        && !(dest.getIndirizzo().equals(""))) {
                    destinatario.setIndirizzo(destinatario.getIndirizzo() + ' '
                            + dest.getIndirizzo());
                }

                destinatario.setCitta(dest.getCitta());
                destinatario.setMezzoSpedizioneId(dest.getMezzoSpedizioneId());
                
                
                destinatario.setDataSpedizione(DateUtil.toDate(dest
                        .getDataSpedizione()));
                destinatario.setFlagConoscenza(dest.getFlagConoscenza());
                destinatario.setTitoloId(dest.getTitoloId());
                destinatario.setNote(dest.getNote());
                destinatario.setMezzoDesc(dest.getMezzoDesc());
                invioC.addDestinatari(destinatario);
            }
        }
    }


   /* private void aggiornaDestinatariModel(DocumentoForm form,
            InvioClassificati invioC, Utente utente) {
        invioC.removeDestinatari();
        Collection destinatari = form.getDestinatari();
        // boolean spedito = false;
        if (destinatari != null) {
            for (Iterator i = destinatari.iterator(); i.hasNext();) {
                DestinatarioView dest = (DestinatarioView) i.next();
                DestinatarioVO destinatario = new DestinatarioVO();
                destinatario.setDestinatario(dest.getDestinatario().trim());
                destinatario.setFlagTipoDestinatario(dest
                        .getFlagTipoDestinatario());
                destinatario.setEmail(dest.getEmail());
                destinatario.setIndirizzo(dest.getIndirizzo());
                destinatario.setCitta(dest.getCitta());
                destinatario.setMezzoSpedizioneId(dest.getMezzoSpedizioneId());
                // if (dest.getDataSpedizione() != null) {
                // spedito = true;
                // }
                destinatario
                        .setTitoloDestinatario(dest.getTitoloDestinatario());
                destinatario.setDataSpedizione(DateUtil.toDate(dest
                        .getDataSpedizione()));
                destinatario.setFlagConoscenza(dest.getFlagConoscenza());
                invioC.addDestinatari(destinatario);
            }
        }
    }*/

    private void aggiornaDestinatarioForm(String id, DocumentoForm form) {
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

    
   /* private void aggiornaDestinatarioForm(String id, DocumentoForm form) {
        DestinatarioView destinatario = new DestinatarioView();
        destinatario = form.getDestinatario(id);
        form.setTipoDestinatario(destinatario.getFlagTipoDestinatario());
        form.setNominativoDestinatario(destinatario.getDestinatario());
        form.setCitta(destinatario.getCitta());
        form.setEmailDestinatario(destinatario.getEmail());
        form.setMezzoSpedizione(destinatario.getMezzoSpedizioneId());
        form.setIndirizzoDestinatario(destinatario.getIndirizzo());
        form.setCapDestinatario(destinatario.getCapDestinatario());
        form.setDataSpedizione(destinatario.getDataSpedizione());
        form.setFlagConoscenza(destinatario.getFlagConoscenza());
        form.setNote(destinatario.getNote());
        form.setDestinatarioMezzoId(destinatario.getDestinatarioMezzoId());
    }*/

    private void aggiungiDestinatario(DocumentoForm form, HttpSession session) {
        DestinatarioView destinatario = new DestinatarioView();

        String destinatarioMezzoId = form.getDestinatarioMezzoId();
        String idx = "0";
        if (destinatarioMezzoId != null && !destinatarioMezzoId.equals(""))
            idx = destinatarioMezzoId.substring(0, destinatarioMezzoId
                    .indexOf('_'));
        destinatario.setIdx(Integer.parseInt(idx));
        destinatario.setFlagTipoDestinatario(form.getTipoDestinatario());

    /*    destinatario.setFlagTipoDestinatario(form.getTipoDestinatario());*/

        if (form.getDataSpedizione() != null
                && !"".equals(form.getDataSpedizione())) {
            destinatario.setDataSpedizione(form.getDataSpedizione());
        }

        destinatario.setDestinatario(form.getNominativoDestinatario().trim());
        destinatario.setEmail(form.getEmailDestinatario());
        destinatario.setCitta(form.getCitta());
        destinatario.setIndirizzo(form.getIndirizzoDestinatario());
        destinatario.setCapDestinatario(form.getCapDestinatario());
        destinatario.setFlagConoscenza(form.getFlagConoscenza());
        destinatario.setMezzoSpedizioneId(form.getMezzoSpedizione());
        
        if (form.getMezzoSpedizione() >= 0) {
            Collection<SpedizioneVO> mezzi = LookupDelegate.getInstance().getMezziSpedizione(
                    form.getAooId());
            Iterator<SpedizioneVO> i = mezzi.iterator();
            while (i.hasNext()) {
                SpedizioneVO m = i.next();
                if (m.getId().intValue() == form.getMezzoSpedizione()) {
                    destinatario.setMezzoDesc(m.getDescrizioneSpedizione());
                    destinatario.setPrezzoSpedizione(m.getPrezzo());
                    break;
                }
            }
        }
        form.aggiungiDestinatario(destinatario);
        form.inizializzaDestinatarioForm();
    }

}
