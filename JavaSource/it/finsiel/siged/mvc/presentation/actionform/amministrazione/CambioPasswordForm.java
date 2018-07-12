package it.finsiel.siged.mvc.presentation.actionform.amministrazione;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class CambioPasswordForm extends ActionForm {

    private String username;

    private String oldPassword;

    private String newPassword;

    private String confirmNewPassword;

    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        newPassword = "";
        confirmNewPassword = "";
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ActionErrors validate(ActionMapping arg0, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        UtenteVO utenteVO = ((Utente) request.getSession().getAttribute(
                Constants.UTENTE_KEY)).getValueObject();
        if (request.getParameter("btnConferma") != null) {
            if (newPassword == null || "".equals(newPassword))
                errors.add("nuovaPWD", new ActionMessage("errors.required",
                        "Nuova Password", ""));
            if (confirmNewPassword == null || "".equals(confirmNewPassword))
                errors.add("confermaNuovaPWD", new ActionMessage(
                        "errors.required", "Conferma Nuova Password", ""));
            if (newPassword != null && utenteVO.getPassword().equals(newPassword))
                errors.add("newPassword", new ActionMessage(
                        "error.cambio.password.equal.old", "", ""));
            if (!newPassword.equals(confirmNewPassword))
                errors.add("newPassword", new ActionMessage(
                        "error.cambio.password.notequal.confirm", "", ""));
            if (newPassword.length() < 6)
                errors.add("newPassword", new ActionMessage("errors.minlength",
                        "Nuova password", "6"));
            if (confirmNewPassword.length() < 6)
                errors.add("newPassword", new ActionMessage("errors.minlength",
                        "Conferma nuova password", "6"));
        }
        return errors;
    }
}