package it.finsiel.siged.mvc.presentation.action.registro;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.RegistroDelegate;
import it.finsiel.siged.mvc.presentation.actionform.registro.RegistroForm;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;
import it.finsiel.siged.util.NumberUtil;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
 * Implementation of <strong>Action </strong> that validates a user logon.
 * 
 * @author Almaviva sud.
 */

public final class RegistroAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    static Logger logger = Logger.getLogger(RegistroAction.class.getName());

    // --------------------------------------------------------- Public Methods

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     * 
     * @param mapping
     *            The ActionMapping used to select this instance
     * @param form
     *            The optional ActionForm bean for this request (if any)
     * @param request
     *            The HTTP request we are processing
     * @param response
     *            The HTTP response we are creating
     * 
     * @exception Exception
     *                if business logic throws an exception
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionMessages errors = new ActionMessages();
        HttpSession session = request.getSession();

        RegistroForm registroForm = (RegistroForm) form;
        RegistroDelegate rd = RegistroDelegate.getInstance();
        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);

        if (form == null) {
            registroForm = new RegistroForm();
            request.setAttribute(mapping.getAttribute(), registroForm);
        }
        int id = 0;

        if (request.getParameter("btnModifica") != null) {
            if (NumberUtil.isInteger(request.getParameter("id"))) {
                id = NumberUtil.getInt(request.getParameter("id"));
            }
        } else if (request.getParameter("btnSalva") != null) {
            errors = registroForm.validate(mapping, request);
            if (registroForm.getUfficiale()) {
                if (RegistroDelegate.getInstance()
                        .esisteRegistroUfficialeByAooId(registroForm.getId(),
                                utente.getRegistroVOInUso().getAooId())) {
                    errors.add("registro_ufficiale", new ActionMessage(
                            "registro_ufficiale", "", ""));
                }
            }
            if(RegistroDelegate.getInstance().esisteRegistroCodiceByAooId(registroForm.getId(),registroForm.getCodice(),
                            utente.getRegistroVOInUso().getAooId()))
            	 errors.add("registro_codice_utilizzato", new ActionMessage("registro_codice_utilizzato", "", ""));
            if (errors.isEmpty()) {
                RegistroVO registro = new RegistroVO();
                
                caricaDatiNelVO(registro, registroForm, utente);
                registro = RegistroDelegate.getInstance().salvaRegistro(
                        registro);
                if (registro.getReturnValue() == ReturnValues.SAVED) {
                    Organizzazione.getInstance().addRegistro(registro);
                    errors.add("registro", new ActionMessage("operazione_ok",
                            "", ""));
                    saveErrors(request, errors);
                }
                id = registro.getId().intValue();
            }

        } else if (request.getParameter("btnCancella") != null) {
            if (NumberUtil.isInteger(request.getParameter("id"))) {
                id = NumberUtil.getInt(request.getParameter("id"));
            }
            if (RegistroDelegate.getInstance().cancellaRegistro(id)) {
                Organizzazione.getInstance().removeRegistro(new Integer(id));
                errors.add("ILregistro", new ActionMessage("cancellazione_ok",
                        "", ""));
                if (!errors.isEmpty()) {
                    saveErrors(request, errors);
                }
                return (mapping.findForward("registri"));
            } else {
                errors.add("registro", new ActionMessage(
                        "record_non_cancellabile", "Registro",
                        "ci sono utenti collegati"));
            }

        } else if (request.getParameter("annulla") != null) {
            registroForm.inizializzaForm();
            return (mapping.findForward("registri"));

        } else if (request.getParameter("btnNuovo") != null) {
            registroForm.inizializzaForm();

        } else if (request.getParameter("aggiungiUtenti") != null) {
            errors = registroForm.validate(mapping, request);
            if (errors.isEmpty()) {
                String[] utenti = registroForm.getUtentiSelezionati();
                if (utenti != null) {
                    RegistroDelegate.getInstance().aggiungiPermessiUtenti(
                            utenti, registroForm.getId(), utente);
                }
                request.setAttribute(mapping.getAttribute(), registroForm);
            }
        } else if (request.getParameter("rimuoviUtenti") != null) {
            errors = registroForm.validate(mapping, request);
            if (errors.isEmpty()) {

                String[] utenti = registroForm.getUtentiSelezionati();
                if (utenti != null) {
                    int statusFlag = RegistroDelegate.getInstance()
                            .cancellaPermessiUtenti(utenti,
                                    registroForm.getId());
                    if (statusFlag == ReturnValues.FOUND) {
                        errors.add("registro", new ActionMessage(
                                "record_non_cancellabile", "i permessi utenti",
                                "esistono protocolli per cui l'utente � assegnatario o "
                                        + "mittente o protocollatore"));
                    }
                }
                request.setAttribute(mapping.getAttribute(), registroForm);
            }
        }
        if (registroForm != null && registroForm.getId() > 0) {
            id = registroForm.getId();
        }
        if (id > 0) {
            RegistroVO registroVO = rd.getRegistroById(id);
            caricaDatiNelForm(registroForm, registroVO);
            request.setAttribute(mapping.getAttribute(), registroForm);
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }

        return (mapping.findForward("input"));
    }

    public void caricaDatiNelVO(RegistroVO vo, RegistroForm form, Utente utente) {
        vo.setId(form.getId());
        if (form.getId() == 0) {
            vo.setAooId(utente.getRegistroVOInUso().getAooId());
        } else {
            vo.setAooId(form.getAooId());
        }
        vo.setDataAperturaRegistro(new Date(System.currentTimeMillis()));
        vo.setCodRegistro(form.getCodice());
        vo.setDescrizioneRegistro(form.getDescrizione());
        vo.setDataBloccata(form.getDataBloccata());
        vo.setUfficiale(form.getUfficiale());
        vo.setApertoIngresso(form.getApertoIngresso());
        vo.setApertoUscita(form.getApertoUscita());
        vo.setRowCreatedUser(utente.getValueObject().getUsername());
        vo.setRowUpdatedTime(new Date(System.currentTimeMillis()));
        vo.setVersione(form.getVersione());

    }

    public void caricaDatiNelForm(RegistroForm form, RegistroVO registroVO) {
        form.setAooId(registroVO.getAooId());
        form.setCodice(registroVO.getCodRegistro());
        form.setDescrizione(registroVO.getDescrizioneRegistro());
        form.setId(registroVO.getId().intValue());
        form.setVersione(registroVO.getVersione());
        form.setUfficiale(registroVO.getUfficiale());
        form.setDataBloccata(registroVO.getDataBloccata());
        form.setUtentiSelezionati(null);
        Organizzazione org = Organizzazione.getInstance();
        Collection<Utente> utenti = org.getUtentiById();
        Map<Integer,Integer> utentiAbilitati = new HashMap<Integer,Integer>(RegistroDelegate.getInstance()
                .getUtentiRegistro(registroVO.getId().intValue()));
        for (Iterator<Utente> y = utenti.iterator(); y.hasNext();) {
            Utente utente = y.next();
            if (utente.getValueObject().getAooId() == registroVO.getAooId()) {
                if (utentiAbilitati
                        .containsKey(utente.getValueObject().getId())) {
                    form.aggiungiUtenteAbilitato(utente.getValueObject());
                } else {
                    form.aggiungiUtenteNonAbilitato(utente.getValueObject());
                }
            }
        }
    }
}