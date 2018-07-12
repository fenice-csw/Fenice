package it.finsiel.siged.mvc.presentation.actionform.protocollo;

import it.finsiel.siged.constant.EmailConstants;
import it.finsiel.siged.mvc.vo.log.EventoVO;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class ListaEmailLogForm extends ActionForm {
   
	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(ListaEmailLogForm.class.getName());

    private int tipoEvento;

    private String[] emailSelezionateId;

    private Collection<EventoVO> listaEmail = new ArrayList<EventoVO>();

    public ListaEmailLogForm() {
    }

    public Collection<EventoVO> getListaEmail() {
        return listaEmail;
    }

    public void setListaEmail(Collection<EventoVO> listaEmail) {
        this.listaEmail = listaEmail;
    }

    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        super.reset(arg0, arg1);
        tipoEvento = EmailConstants.ERROR_EMAIL_INGRESSO;
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (request.getParameter("elimina") != null) {
            String[] ids = getEmailSelezionateId();
            if (ids == null || ids.length == 0) {
                errors.add("emailSelezionateId", new ActionMessage(
                        "selezione.obbligatoria", "i log", "da eliminare"));
            }
        } else if (request.getParameter("visualizza") != null) {
            if (getTipoEvento() != EmailConstants.ERROR_EMAIL_INGRESSO
                    && getTipoEvento() != EmailConstants.ERROR_EMAIL_USCITA
                    && getTipoEvento() != EmailConstants.LOG_CRL)
                errors.add("tipoEvento", new ActionMessage(
                        "email.logs.selezionare"));
        }
        return errors;
    }

    public int getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(int tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String[] getEmailSelezionateId() {
        return emailSelezionateId;
    }

    public void setEmailSelezionateId(String[] emailSelezionateId) {
        this.emailSelezionateId = emailSelezionateId;
    }
}