package it.finsiel.siged.mvc.presentation.actionform.amministrazione;

import it.finsiel.siged.mvc.presentation.action.amministrazione.SpedizioneAction;
import it.finsiel.siged.util.NumberUtil;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class SpedizioneForm extends ActionForm {

    static Logger logger = Logger.getLogger(SpedizioneAction.class.getName());

    private int id;

    private String codiceSpedizione;

    private String descrizioneSpedizione;

    private String prezzo;
    
    private boolean flagAbilitato;

    private boolean flagCancellabile;

    private Collection mezziSpedizione;

    
    
    /**
	 * @return the prezzo
	 */
	public String getPrezzo() {
		return prezzo;
	}


	/**
	 * @param prezzo the prezzo to set
	 */
	public void setPrezzo(String prezzo) {
		this.prezzo = prezzo;
	}


	public Collection getMezziSpedizione() {
        return mezziSpedizione;
    }

    
    public void setMezziSpedizione(Collection mezziSpedizione) {
        this.mezziSpedizione = mezziSpedizione;
    }

    /**
     * @return Returns the codiceSpedizione.
     */
    public String getCodiceSpedizione() {
        return codiceSpedizione;
    }

    /**
     * @param codiceSpedizione
     *            The codiceSpedizione to set.
     */
    public void setCodiceSpedizione(String codiceSpedizione) {
        this.codiceSpedizione = codiceSpedizione;
    }

    /**
     * @return Returns the descrizioneSpedizione.
     */
    public String getDescrizioneSpedizione() {
        return descrizioneSpedizione;
    }

    /**
     * @param descrizioneSpedizione
     *            The descrizioneSpedizione to set.
     */
    public void setDescrizioneSpedizione(String descrizioneSpedizione) {
        this.descrizioneSpedizione = descrizioneSpedizione;
    }

    public boolean getFlagAbilitato() {
        return flagAbilitato;
    }

    public void setFlagAbilitato(boolean abilitato) {
        this.flagAbilitato = abilitato;
    }

    public boolean getFlagCancellabile() {
        return flagCancellabile;
    }

    public void setFlagCancellabile(boolean cancellabile) {
        this.flagCancellabile = cancellabile;
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (codiceSpedizione == null || "".equals(codiceSpedizione)) {
            errors.add("codiceSpedizione", new ActionMessage(
                    "campo.obbligatorio", "Codice spedizione", ""));
        } else if (descrizioneSpedizione == null
                || "".equals(descrizioneSpedizione)) {
            errors.add("descrizioneSpedizione", new ActionMessage(
                    "campo.obbligatorio", "Descrizione spedizione", ""));
        }else if (prezzo != null
                && !NumberUtil.isDouble(prezzo)) {
        	errors.add("prezzo", new ActionMessage("formato.numerico.errato", "Importo"));
        }
        return errors;

    }

    public void inizializzaForm() {
        setFlagAbilitato(true);
        setFlagCancellabile(true);
        setCodiceSpedizione(null);
        setDescrizioneSpedizione(null);
        setId(0);
        setMezziSpedizione(null);
        setPrezzo(null);
    }

    /**
     * @return Returns the id.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(int id) {
        this.id = id;
    }
}