package it.finsiel.siged.mvc.presentation.actionform.soggetto;

import it.finsiel.siged.mvc.business.SoggettoDelegate;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class PersonaGiuridicaForm extends SoggettoForm {

    private String descrizioneDitta;

    private String partitaIva;

    private String flagSettoreAppartenenza;

    private String dug;

    private String referente;

    private String telefonoReferente;

    private String emailReferente;

    private String salvaAction;

    private Collection listaPersone;

    private String cerca;

    private String deleteAction;
    
    private boolean indietroVisibile;
    
    private boolean nuovoVisibile;

    public boolean isNuovoVisibile() {
		return nuovoVisibile;
	}

	public void setNuovoVisibile(boolean nuovoVisibile) {
		this.nuovoVisibile = nuovoVisibile;
	}

	/**
     * @return Returns the denominazione.
     */
    public String getDescrizioneDitta() {
        return descrizioneDitta;
    }

    /**
     * @param denominazione
     *            The denominazione to set.
     */
    public void setDescrizioneDitta(String descrizioneDitta) {
        this.descrizioneDitta = descrizioneDitta;
    }

    /**
     * @return Returns the pIva.
     */
    public String getPartitaIva() {
        if (partitaIva == null) {
            partitaIva = "";
        }
        return partitaIva;
    }

    /**
     * @param iva
     *            The pIva to set.
     */
    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }

    /**
     * @return Returns the dug.
     */
    public String getDug() {
        return dug;
    }

    /**
     * @param dug
     *            The dug to set.
     */
    public void setDug(String dug) {
        this.dug = dug;
    }

    /**
     * @return Returns the referente.
     */
    public String getReferente() {
        return referente;
    }

    /**
     * @param referente
     *            The referente to set.
     */
    public void setReferente(String referente) {
        this.referente = referente;
    }

    /**
     * @return Returns the telefonoReferente.
     */
    public String getTelefonoReferente() {
        return telefonoReferente;
    }

    /**
     * @param telefonoReferente
     *            The telefonoReferente to set.
     */
    public void setTelefonoReferente(String telefonoReferente) {
        this.telefonoReferente = telefonoReferente;
    }

    /**
     * @return Returns the emailReferente.
     */
    public String getEmailReferente() {
        return emailReferente;
    }

    /**
     * @param emailReferente
     *            The emailReferente to set.
     */
    public void setEmailReferente(String emailReferente) {
        this.emailReferente = emailReferente;
    }

    /**
     * @return Returns the flagSettoreAppartenenza.
     */
    public String getFlagSettoreAppartenenza() {
        return flagSettoreAppartenenza;
    }

    /**
     * @param flagSettoreAppartenenza
     *            The flagSettoreAppartenenza to set.
     */
    public void setFlagSettoreAppartenenza(String flagSettoreAppartenenza) {
        if (flagSettoreAppartenenza.equals("Privato")) {
            this.flagSettoreAppartenenza = "2";
        } else if (flagSettoreAppartenenza.equals("Pubblico")) {
            this.flagSettoreAppartenenza = "1";
        }

    }

    /**
     * @return Returns the btnCercaPersonaGiuridica.
     */
    public String getCerca() {
        return cerca;
    }

    /**
     * @param btnCercaPersonaGiuridica
     *            The btnCercaPersonaGiuridica to set.
     */
    public void setCerca(String cerca) {
        this.cerca = cerca;
    }

    /**
     * @param listaPersone
     *            The listaPersone to set.
     */
    public void setListaPersone(Collection listaPersone) {
        this.listaPersone = listaPersone;
    }

    public String getListaSize() {
        if (listaPersone == null)
            return "0";
        else
            return "" + listaPersone.size();
    }

    /**
     * @return Returns the listaPersone.
     */
    public Collection getListaPersone() {
        return listaPersone;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        descrizioneDitta = "";
        partitaIva = "";
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (cerca != null
        		&& (getSoggetto().getIndirizzo().getComune() == null || "".equals(getSoggetto().getIndirizzo().getComune().trim()))
                && (descrizioneDitta == null || "".equals(descrizioneDitta))
                && (partitaIva == null || "".equals(partitaIva))) {

            errors.add("descrizioneDitta", new ActionMessage(
                    "parametri_ricerca_persona_giuridica"));
        }
        return errors;

    }

    public ActionErrors validateDatiInserimento(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (descrizioneDitta == null || "".equals(descrizioneDitta)) {
        	errors.add("descrizioneDitta", new ActionMessage(
					"campo.obbligatorio", "Denominazione",
					""));
        }else if (getSoggetto().getIndirizzoEMail()!=null && !getSoggetto().getIndirizzoEMail().equals("")) {
			if(SoggettoDelegate.getInstance().isMailUsed(getSoggetto().getAoo(), getSoggetto().getIndirizzoEMail()))
				errors.add("soggetto.indirizzo.email", new ActionMessage(
						"soggetto.email_presente"));
			}
        if (getSoggetto().getIndirizzo().getComune() == null || "".equals(getSoggetto().getIndirizzo().getComune())) {
        	errors.add("soggetto.indirizzo.comune", new ActionMessage(
					"campo.obbligatorio", "Località",
					""));
        }
        
        return errors;
    }

    /**
     * @return Returns the confermaPGAction.
     */
    public String getSalvaAction() {
        return salvaAction;
    }

    /**
     * @param confermaPGAction
     *            The confermaPGAction to set.
     */
    public void setSalvaAction(String confermaAction) {
        this.salvaAction = confermaAction;
    }

    /**
     * @return Returns the deleteAction.
     */
    public String getDeleteAction() {
        return deleteAction;
    }

    /**
     * @param deleteAction
     *            The deleteAction to set.
     */
    public void setDeleteAction(String deleteAction) {
        this.deleteAction = deleteAction;
    }

    public boolean isIndietroVisibile() {
        return indietroVisibile;
    }

    public void setIndietroVisibile(boolean indietroVisibile) {
        this.indietroVisibile = indietroVisibile;
    }
}
