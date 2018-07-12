package it.finsiel.siged.mvc.presentation.actionform.amministrazione;

import it.finsiel.siged.mvc.vo.IdentityVO;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class TitoloDestinatarioForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private int id;

    private String codice;

    private String descrizione;

    private Collection<IdentityVO> titoli;

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Collection<IdentityVO> getTitoli() {
        return titoli;
    }

    public void setTitoli(Collection<IdentityVO> titoli) {
        this.titoli = titoli;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (request.getParameter("btnConferma") != null) {
            if (descrizione == null || "".equals(descrizione.trim())) {
                errors.add("codice", new ActionMessage("campo.obbligatorio",
                        "Codice Titolario", ""));
            } else if (descrizione == null || "".equals(descrizione.trim())) {
                errors.add("descrizione", new ActionMessage(
                        "campo.obbligatorio", "Descrizione Titolario", ""));
            }

        }
        return errors;

    }

}