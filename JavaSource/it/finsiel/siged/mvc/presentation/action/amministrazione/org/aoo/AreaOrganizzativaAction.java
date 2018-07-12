package it.finsiel.siged.mvc.presentation.action.amministrazione.org.aoo;

import java.sql.Time;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.AreaOrganizzativa;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.AreaOrganizzativaDelegate;
import it.finsiel.siged.mvc.business.OrganizzazioneDelegate;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.org.aoo.AreaOrganizzativaForm;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;

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
 * Implementation of <strong>Action </strong> to create a new E-Photo Utente.
 * 
 * @author Almaviva sud.
 * 
 */

public class AreaOrganizzativaAction extends Action {

    static Logger logger = Logger.getLogger(AreaOrganizzativaAction.class
            .getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionMessages errors = new ActionMessages();
        ActionMessages messages = new ActionMessages();
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
        AreaOrganizzativaForm aooForm = (AreaOrganizzativaForm) form;
        AreaOrganizzativaDelegate aooDelegate = AreaOrganizzativaDelegate
                .getInstance();
        String username = ((Utente) session.getAttribute(Constants.UTENTE_KEY))
                .getValueObject().getUsername();

        if (form == null) {
            aooForm = new AreaOrganizzativaForm();
            request.setAttribute(mapping.getAttribute(), aooForm);
        }

        if (request.getParameter("btnModifica") != null) {
           
            if (NumberUtil.isInteger(request.getParameter("id"))) {
                int id = NumberUtil.getInt(request.getParameter("id"));
                AreaOrganizzativa aoo = aooDelegate.getAreaOrganizzativa(id);
                if (aoo != null) {
                    caricaDatiNelForm(aooForm, aoo);
                    return (mapping.findForward("edit"));
                } else {
                    errors.add("general", new ActionMessage("selezionare.aoo"));
                }
            } else {
                errors.add("general", new ActionMessage("selezionare.aoo"));
            }
        } else if (request.getParameter("btnSalva") != null) {
            errors = aooForm.validate(mapping, request);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("edit");
            }
            AreaOrganizzativa aoo = new AreaOrganizzativa();
            AreaOrganizzativa aooSalvata = new AreaOrganizzativa();
            int id = aooForm.getId();
            String descrizioneAoo = aooForm.getDescription();
            if (!aooDelegate.esisteAreaOrganizzativa(descrizioneAoo, id)) {
                caricaDati(aoo, aooForm, username);
                aooSalvata = aooDelegate.salvaAreaOrganizzativa(aoo, utente);
                if (aooSalvata != null
                        && aooSalvata.getValueObject().getReturnValue() == ReturnValues.SAVED) {
                    if (id == 0) {
                    	
                        aooForm.setAreeOrganizzative(OrganizzazioneDelegate
                                .getInstance().getAreeOrganizzative());
                        aggiornaAreaOrganizzative(aooSalvata, id);
                        request.setAttribute(mapping.getAttribute(), aooForm);
                        StringBuffer sB = new StringBuffer("Username: admin"
                                + aooSalvata.getValueObject().getId() + "\r\n");
                        sB.append("Password: admin" + aooSalvata.getValueObject().getId()
                                + "\r\n");
                        aooForm.setMsgSuccess(sB.toString());
                        return (mapping.findForward("success"));
                    } else {
                    	messages.add("aoo", new ActionMessage("operazione_ok"));
                        saveMessages(request, messages);
                    }
                    aggiornaAreaOrganizzative(aooSalvata, id);
                } else {
                    errors.add("aoo", new ActionMessage(
                            "errore_nel_salvataggio"));
                }
            } else {
            	errors.add("aoo", new ActionMessage("errore.save.aoo"));
                if(!errors.isEmpty())
                saveErrors(request, errors);
                return mapping.findForward("edit");

            }
        } else if (request.getParameter("btnNuovo") != null) {
            aooForm.inizializzaForm();
            request.setAttribute(mapping.getAttribute(), aooForm);
            return mapping.findForward("edit");

        } else if (request.getParameter("btnCancella") != null) {
            AreaOrganizzativa aoo = new AreaOrganizzativa();
            errors = aooForm.validate(mapping, request);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
            }
            if (request.getParameter("id") != null) {
                int id = Integer.parseInt(request.getParameter("id"));
                aoo = aooDelegate.getAreaOrganizzativa(id);
                caricaDati(aoo, aooForm, username);
                if (aooDelegate.cancellaAreaOrganizzativa(id)) {
                    rimuoviAreaOrganizzativa(id);
                    aooForm.setAreeOrganizzative(OrganizzazioneDelegate
                            .getInstance().getAreeOrganizzative());
                    request.setAttribute(mapping.getAttribute(), aooForm);
                } else {
                    errors.add("cancella_aoo", new ActionMessage(
                            "record_non_cancellabile", "la AOO", ""));
                    if (!errors.isEmpty()) {
                        saveErrors(request, errors);
                    }
                }
            }
        }
        try {
            aooForm.setAreeOrganizzative(OrganizzazioneDelegate.getInstance()
                    .getAreeOrganizzative());
        } catch (DataException e) {
            errors.add("general", new ActionMessage("errore.load.aooList"));
        }

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        return (mapping.findForward("input"));
    }

    public void caricaDati(AreaOrganizzativa aoo,
            AreaOrganizzativaForm form, String username) {
        aoo.getValueObject().setId(form.getId());
        aoo.getMailConfig().setAooId(form.getId());
        if (form.getId() == 0) {
        	aoo.getValueObject().setRowCreatedUser(username);
        } else {
        	aoo.getValueObject().setRowUpdatedUser(username);
        }

        aoo.getValueObject().setCodi_aoo(form.getCodi_aoo());
        aoo.getValueObject().setCodi_documento_doc(form.getCodi_documento_doc());
        aoo.getValueObject().setDescription(form.getDescription());
        if (form.getData_istituzione() != null) {
        	aoo.getValueObject().setData_istituzione(DateUtil.toDate(form.getData_istituzione()));
        } else {
        	aoo.getValueObject().setData_istituzione(DateUtil.toDate(""));
        }
        aoo.getValueObject().setResponsabile_nome(form.getResponsabile_nome());
        aoo.getValueObject().setResponsabile_cognome(form.getResponsabile_cognome());
        aoo.getValueObject().setResponsabile_email(form.getResponsabile_email());
        aoo.getValueObject().setResponsabile_telefono(form.getResponsabile_telefono());
        if (form.getData_soppressione() != null) {
        	aoo.getValueObject().setData_soppressione(DateUtil
                    .toDate(form.getData_soppressione()));
        } else {
        	aoo.getValueObject().setData_soppressione(DateUtil.toDate(""));
        }
        aoo.getValueObject().setTelefono(form.getTelefono());
        aoo.getValueObject().setFax(form.getFax());
        aoo.getValueObject().setIndi_dug(form.getIndi_dug());
        aoo.getValueObject().setIndi_toponimo(form.getIndi_toponimo());
        aoo.getValueObject().setIndi_civico(form.getIndi_civico());
        aoo.getValueObject().setIndi_cap(form.getIndi_cap());
        aoo.getValueObject().setIndi_comune(form.getIndi_comune());
        aoo.getValueObject().setEmail(form.getEmail());
        aoo.getValueObject().setDipartimento_codice(form.getDipartimento_codice());
        aoo.getValueObject().setDipartimento_descrizione(form.getDipartimento_descrizione());
        aoo.getValueObject().setTipo_aoo(form.getTipo_aoo());
        aoo.getValueObject().setProvincia_id(form.getProvincia_id());
        aoo.getValueObject().setCodi_documento_doc(form.getCodi_documento_doc());
        aoo.getValueObject().setAmministrazione_id(form.getAmministrazione_id());
        aoo.getValueObject().setVersione(form.getVersione());
        aoo.getValueObject().setDocumentoReadable(form.isDocumentoReadable());
        aoo.getValueObject().setRicercaUfficiFull(form.isRicercaUfficiFull());
        aoo.getValueObject().setDipendenzaTitolarioUfficio(form.getDipendenzaTitolarioUfficio());
        aoo.getValueObject().setTitolarioLivelloMinimo(form.getTitolarioLivelloMinimo());
        aoo.getValueObject().setAnniVisibilitaBacheca(form.getAnniVisibilitaBacheca());
        aoo.getValueObject().setMaxRighe(form.getMaxRighe());
        //dati Gestione Archivi
        aoo.getValueObject().setGaAbilitata(form.isGaAbilitata());
        aoo.getValueObject().setGaPwd(form.getGaPwd());
        aoo.getValueObject().setGaUsername(form.getGaUsername());
        aoo.getValueObject().setGaFlagInvio(form.getGaFlagInvio());
        if(form.getGaTimer()!=null && !form.getGaTimer().trim().equals(""))
        	aoo.getValueObject().setGaTimer(new Time(DateUtil.getOraMinuti(form.getGaTimer()).getTime()));
        aoo.getValueObject().setFlagPubblicazioneP7m(form.getFlagPubblicazioneP7m());

        // dati posta elettronica
        aoo.getMailConfig().setPecIndirizzo(form.getPec_indirizzo());
        aoo.getMailConfig().setPecPop3(form.getPec_pop3());
        aoo.getMailConfig().setPecPwd(form.getPec_pwd());
        aoo.getMailConfig().setPecSmtp(form.getPec_smtp());
        aoo.getMailConfig().setPecSmtpPort(form.getPec_smtp_port());
        aoo.getMailConfig().setPecAbilitata(form.getPecAbilitata());
        aoo.getMailConfig().setPecSslPort(form.getPec_ssl_port());
        aoo.getMailConfig().setPecUsername(form.getPec_username());
        aoo.getMailConfig().setMailTimer(form.getPecTimer());
        aoo.getMailConfig().setPnIndirizzo(form.getPn_indirizzo());
        aoo.getMailConfig().setPnPop3(form.getPn_pop3());
        aoo.getMailConfig().setPnPwd(form.getPn_pwd());
        aoo.getMailConfig().setPnSmtp(form.getPn_smtp());
        aoo.getMailConfig().setPnSsl(form.getPn_ssl());
        aoo.getMailConfig().setPnSslPort(form.getPn_ssl_port());
        aoo.getMailConfig().setPnUsername(form.getPn_username());
        aoo.getMailConfig().setPnAbilitata(form.isPnAbilitata());
        
        aoo.getMailConfig().setPrecIndirizzoInvio(form.getPrec_indirizzo_invio());
        aoo.getMailConfig().setPrecIndirizzoRicezione(form.getPrec_indirizzo_ricezione());
        aoo.getMailConfig().setPrecPwd(form.getPrec_pwd());
        aoo.getMailConfig().setPrecSmtp(form.getPrec_smtp());
        aoo.getMailConfig().setPrecUsername(form.getPrec_username());
    }

    public void caricaDatiNelForm(AreaOrganizzativaForm form,
            AreaOrganizzativa aoo) {
    	form.setId(aoo.getValueObject().getId().intValue());
        form.setCodi_aoo(aoo.getValueObject().getCodi_aoo());
        form.setDescription(aoo.getValueObject().getDescription());
        if (aoo.getValueObject().getData_istituzione() != null)
            form.setData_istituzione(DateUtil.formattaData(aoo.getValueObject()
                    .getData_istituzione().getTime()));
        form.setResponsabile_nome(aoo.getValueObject().getResponsabile_nome());
        form.setResponsabile_cognome(aoo.getValueObject().getResponsabile_cognome());
        form.setResponsabile_email(aoo.getValueObject().getResponsabile_email());
        form.setResponsabile_telefono(aoo.getValueObject().getResponsabile_telefono());
        if (aoo.getValueObject().getData_soppressione() != null)
            form.setData_soppressione(DateUtil.formattaData(aoo.getValueObject()
                    .getData_soppressione().getTime()));
        form.setTelefono(aoo.getValueObject().getTelefono());
        form.setFax(aoo.getValueObject().getFax());
        form.setIndi_dug(aoo.getValueObject().getIndi_dug());
        form.setIndi_toponimo(aoo.getValueObject().getIndi_toponimo());
        form.setIndi_civico(aoo.getValueObject().getIndi_civico());
        form.setIndi_cap(aoo.getValueObject().getIndi_cap());
        form.setIndi_comune(aoo.getValueObject().getIndi_comune());
        form.setEmail(aoo.getValueObject().getEmail());
        form.setDipartimento_codice(aoo.getValueObject().getDipartimento_codice());
        form.setDipartimento_descrizione(aoo.getValueObject().getDipartimento_descrizione());
        form.setTipo_aoo(aoo.getValueObject().getTipo_aoo());
        form.setProvincia_id(aoo.getValueObject().getProvincia_id());
        form.setCodi_documento_doc(aoo.getValueObject().getCodi_documento_doc());
        form.setAmministrazione_id(aoo.getValueObject().getAmministrazione_id());
        form.setDesc_amministrazione(Organizzazione.getInstance().getValueObject().getDescription());
        form.setTitolarioLivelloMinimo(aoo.getValueObject().getTitolarioLivelloMinimo());
        form.setDipendenzaTitolarioUfficio(aoo.getValueObject()
                .getDipendenzaTitolarioUfficio());
        form.setVersione(aoo.getValueObject().getVersione());
        form.setModificabileDipendenzaTitolarioUfficio(AreaOrganizzativaDelegate.getInstance().isModificabileDipendenzaTitolarioUfficio(aoo.getValueObject().getId().intValue()));
        form.setDocumentoReadable(aoo.getValueObject().isDocumentoReadable());
        form.setRicercaUfficiFull(aoo.getValueObject().isRicercaUfficiFull());
        form.setAnniVisibilitaBacheca(aoo.getValueObject().getAnniVisibilitaBacheca());
        form.setMaxRighe(aoo.getValueObject().getMaxRighe());
        //dati Gestione Archivi
        form.setGaAbilitata(aoo.getValueObject().isGaAbilitata());
        form.setGaPwd(aoo.getValueObject().getGaPwd());
        form.setGaUsername(aoo.getValueObject().getGaUsername());
        form.setGaFlagInvio(aoo.getValueObject().isGaFlagInvio());
        if(aoo.getValueObject().isGaFlagInvio())
        	form.setGaTimer(DateUtil.formattaOraMinuti(aoo.getValueObject().getGaTimer().getTime()));
        form.setFlagPubblicazioneP7m(aoo.getValueObject().getFlagPubblicazioneP7m());

        //dati MAIL
        form.setPec_indirizzo(aoo.getMailConfig().getPecIndirizzo());
        form.setPec_pop3(aoo.getMailConfig().getPecPop3());
        form.setPec_pwd(aoo.getMailConfig().getPecPwd());
        form.setPec_smtp(aoo.getMailConfig().getPecSmtp());
        form.setPecAbilitata(aoo.getMailConfig().isPecAbilitata());
        form.setPec_ssl_port(aoo.getMailConfig().getPecSslPort());
        form.setPec_username(aoo.getMailConfig().getPecUsername());
        form.setPec_smtp_port(aoo.getMailConfig().getPecSmtpPort());
        form.setPecTimer(aoo.getMailConfig().getMailTimer());
        form.setPn_indirizzo(aoo.getMailConfig().getPnIndirizzo());
        form.setPn_pop3(aoo.getMailConfig().getPnPop3());
        form.setPn_pwd(aoo.getMailConfig().getPnPwd());
        form.setPn_smtp(aoo.getMailConfig().getPnSmtp());
        form.setPnAbilitata(aoo.getMailConfig().isPnAbilitata());
        form.setPn_ssl(aoo.getMailConfig().isPnSsl());
        form.setPn_ssl_port(aoo.getMailConfig().getPnSslPort());
        form.setPn_username(aoo.getMailConfig().getPnUsername());
      
        form.setPrec_indirizzo_invio(aoo.getMailConfig().getPrecIndirizzoInvio());
        form.setPrec_indirizzo_ricezione(aoo.getMailConfig().getPrecIndirizzoRicezione());
        form.setPrec_pwd(aoo.getMailConfig().getPrecPwd());
        form.setPrec_smtp(aoo.getMailConfig().getPrecSmtp());
        form.setPrec_username(aoo.getMailConfig().getPrecUsername());
    }

    public void inizializzaForm(AreaOrganizzativaForm aooForm) {
        //Collection province = null;
        aooForm.setId(0);
        aooForm.setAmministrazione_id(0);
        aooForm.setCodi_aoo(null);
        aooForm.setCodi_documento_doc(null);
        aooForm.setData_istituzione(null);
        aooForm.setData_soppressione(null);
        aooForm.setDescription(null);
        aooForm.setDipartimento_codice(null);
        aooForm.setDipartimento_descrizione(null);
        aooForm.setEmail(null);
        aooForm.setFax(null);
        aooForm.setIndi_cap(null);
        aooForm.setIndi_civico(null);
        aooForm.setIndi_comune(null);
        aooForm.setIndi_dug(null);
        aooForm.setIndi_toponimo(null);
        aooForm.setResponsabile_cognome(null);
        aooForm.setResponsabile_email(null);
        aooForm.setResponsabile_nome(null);
        aooForm.setResponsabile_telefono(null);
        aooForm.setTelefono(null);
        aooForm.setTipo_aoo(null);
        aooForm.setTitolarioLivelloMinimo(0);
        aooForm.setDipendenzaTitolarioUfficio(0);
        aooForm.setVersione(0);

    }

    private void rimuoviAreaOrganizzativa(int aooId) {
        Organizzazione org = Organizzazione.getInstance();
        org.removeAreaOrganizzativa(new Integer(aooId));
    }

    private void aggiornaAreaOrganizzative(AreaOrganizzativa aoo, int aooId) {
        Organizzazione org = Organizzazione.getInstance();
        org.addAreaOrganizzativa(aoo);
    }
}