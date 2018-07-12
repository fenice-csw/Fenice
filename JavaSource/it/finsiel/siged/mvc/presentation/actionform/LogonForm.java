package it.finsiel.siged.mvc.presentation.actionform;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class LogonForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private String username;

    private String password;

    private Boolean forzatura;
    
    private String login;

    /**
     * @return Returns the forzatura.
     */
    public Boolean getForzatura() {
        return forzatura;
    }

    /**
     * @param forzatura
     *            The forzatura to set.
     */
    public void setForzatura(Boolean forzatura) {
        this.forzatura = forzatura;
    }

    /**
     * @return Returns the login.
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login
     *            The login to set.
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return Returns the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return Returns the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     *            The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

	/*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest)
     */
    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        username = "";
        password = "";
        forzatura = Boolean.FALSE;
        login = "";
    }

    /*
     * (non-Javadoc)
     * extends ActionForm
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest)
     */
    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
        ActionErrors errors = new ActionErrors();
        if (username == null || username.length() < 1)
            errors
                    .add("username", new ActionMessage(
                            "error.username.required"));
        if (password == null || password.length() < 1)
            errors
                    .add("password", new ActionMessage(
                            "error.password.required"));
        return errors;
    }
}