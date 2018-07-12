package it.finsiel.siged.mvc.presentation.actionform.amministrazione.org.utenti;

import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class CercaUtentiForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private String username;

    private String cognome;

    private String nome;

    private String btnCerca;

    private String btnAnnulla;

    private Collection<UtenteVO> risultatoRicerca;

    public String getBtnAnnulla() {
        return btnAnnulla;
    }

    public void setBtnAnnulla(String btnAnnulla) {
        this.btnAnnulla = btnAnnulla;
    }

    public String getBtnCerca() {
        return btnCerca;
    }

    public void setBtnCerca(String btnCerca) {
        this.btnCerca = btnCerca;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Collection<UtenteVO> getRisultatoRicerca() {
        return risultatoRicerca;
    }

    public void setRisultatoRicerca(Collection<UtenteVO> risultatoRicerca) {
        this.risultatoRicerca = risultatoRicerca;
    }

    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        username = "";
        cognome = "";
        nome = "";
    }

  
    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }
}