package it.finsiel.siged.mvc.presentation.actionform.amministrazione;

import it.finsiel.siged.mvc.presentation.actionform.ParametriForm;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class RicercaScartoProtocolliForm extends ParametriForm {

    static Logger logger = Logger.getLogger(RicercaScartoProtocolliForm.class
            .getName());

    private String dataRegistrazioneDa;

    private String dataRegistrazioneA;

    private String numeroProtocolloDa;

    private String numeroProtocolloA;

    private String annoProtocolloDa;

    private String annoProtocolloA;

    private String protocolloSelezionato;

    // tipo protocollo e stato
    private String tipoProtocollo = "";

    private Collection statiProtocollo;

    private String statoProtocollo = "";

    private String riservato;

    private String numeroProtocolloMittente;
    
    private boolean flagPEC;
    
    private SortedMap protocolli = new TreeMap();

    
    
	public String getNumeroProtocolloMittente() {
		return numeroProtocolloMittente;
	}

	
	public boolean isFlagPEC() {
		return flagPEC;
	}

	
	public void setNumeroProtocolloMittente(String numeroProtocolloMittente) {
		this.numeroProtocolloMittente = numeroProtocolloMittente;
	}

	
	public void setFlagPEC(boolean flagPEC) {
		this.flagPEC = flagPEC;
	}

	public SortedMap getProtocolli() {
        return protocolli;
    }

    public Collection getProtocolliCollection() {
        if (protocolli != null) {
            return protocolli.values();
        } else
            return null;
    }

    public String getNumeroProtocolli() {
        if (protocolli != null) {
            return protocolli.values().size() + "";
        } else
            return "0";

    }

    public ReportProtocolloView getProtocolloView(Integer protocolloId) {
        return (ReportProtocolloView) protocolli.get(protocolloId);
    }

    /**
     * @param protocolliInCarico
     *            The protocolliInCarico to set.
     */
    public void setProtocolli(SortedMap protocolli) {
        this.protocolli = protocolli;
    }

    /**
     * @return Returns the tipoProtocollo.
     */
    public String getTipoProtocollo() {
        return tipoProtocollo;
    }

    /**
     * @param tipoProtocollo
     *            The tipoProtocollo to set.
     */
    public void setTipoProtocollo(String tipoProtocollo) {
        this.tipoProtocollo = tipoProtocollo;
    }

    /**
     * @return Returns the annoProtocolloA.
     */
    public String getAnnoProtocolloA() {
        return annoProtocolloA;
    }

    /**
     * @param annoProtocolloA
     *            The annoProtocolloA to set.
     */
    public void setAnnoProtocolloA(String annoProtocolloA) {
        this.annoProtocolloA = annoProtocolloA;
    }

    /**
     * @return Returns the annoProtocolloDa.
     */
    public String getAnnoProtocolloDa() {
        return annoProtocolloDa;
    }

    /**
     * @param annoProtocolloDa
     *            The annoProtocolloDa to set.
     */
    public void setAnnoProtocolloDa(String annoProtocolloDa) {
        this.annoProtocolloDa = annoProtocolloDa;
    }

    /**
     * @return Returns the statoProtocollo.
     */
    public String getStatoProtocollo() {
        return statoProtocollo;
    }

    /**
     * @param statoProtocollo
     *            The statoProtocollo to set.
     */
    public void setStatoProtocollo(String statoProtocollo) {
        this.statoProtocollo = statoProtocollo;
    }

    /**
     * @return Returns the dataRegistrazioneA.
     */
    public String getDataRegistrazioneA() {
        return dataRegistrazioneA;
    }

    /**
     * @param dataRegistrazioneA
     *            The dataRegistrazioneA to set.
     */
    public void setDataRegistrazioneA(String dataRegistrazioneA) {
        this.dataRegistrazioneA = dataRegistrazioneA;
    }

    /**
     * @return Returns the dataRegistrazioneDa.
     */
    public String getDataRegistrazioneDa() {
        return dataRegistrazioneDa;
    }

    /**
     * @param dataRegistrazioneDa
     *            The dataRegistrazioneDa to set.
     */
    public void setDataRegistrazioneDa(String dataRegistrazioneDa) {
        this.dataRegistrazioneDa = dataRegistrazioneDa;
    }

    /**
     * @return Returns the numeroProtocolloA.
     */
    public String getNumeroProtocolloA() {
        return numeroProtocolloA;
    }

    /**
     * @param numeroProtocolloA
     *            The numeroProtocolloA to set.
     */
    public void setNumeroProtocolloA(String numeroProtocolloA) {
        this.numeroProtocolloA = numeroProtocolloA;
    }

    /**
     * @return Returns the numeroProtocolloDa.
     */
    public String getNumeroProtocolloDa() {
        return numeroProtocolloDa;
    }

    /**
     * @param numeroProtocolloDa
     *            The numeroProtocolloDa to set.
     */
    public void setNumeroProtocolloDa(String numeroProtocolloDa) {
        this.numeroProtocolloDa = numeroProtocolloDa;
    }

    /**
     * @return Returns the protocolloInCarico.
     */
    public String getProtocolloSelezionato() {
        return protocolloSelezionato;
    }

    /**
     * @param protocolloInCarico
     *            The protocolloInCarico to set.
     */
    public void setProtocolloSelezionato(String protocolloInCarico) {
        this.protocolloSelezionato = protocolloInCarico;
    }

    /**
     * @return Returns the flagRiservato.
     */
    public String getRiservato() {
        return riservato;
    }

    /**
     * @param flagRiservato
     *            The flagRiservato to set.
     */
    public void setRiservato(String flagRiservato) {
        this.riservato = flagRiservato;
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (request.getParameter("cerca") != null) {
            if (numeroProtocolloDa != null && !"".equals(numeroProtocolloDa)
                    && !(NumberUtil.isInteger(numeroProtocolloDa))) {
                errors.add("numeroProtocolloDa", new ActionMessage(
                        "formato.numerico.errato", "Numero protocollo da"));
            } else if (numeroProtocolloA != null
                    && !"".equals(numeroProtocolloA)
                    && !(NumberUtil.isInteger(numeroProtocolloA))) {
                errors.add("numeroProtocolloA", new ActionMessage(
                        "formato.numerico.errato", "Numero protocollo a"));
            } else if (annoProtocolloDa != null && !"".equals(annoProtocolloDa)
                    && !(NumberUtil.isInteger(annoProtocolloDa))) {
                errors.add("annoProtocolloDa", new ActionMessage(
                        "formato.numerico.errato", "Anno protocollo da"));
            } else if (annoProtocolloA != null && !"".equals(annoProtocolloA)
                    && !(NumberUtil.isInteger(annoProtocolloA))) {
                errors.add("annoProtocolloA", new ActionMessage(
                        "formato.numerico.errato", "Anno protocollo a"));
            } else if (dataRegistrazioneDa != null
                    && !"".equals(dataRegistrazioneDa)
                    && !DateUtil.isData(dataRegistrazioneDa)) {
                errors.add("dataRegistrazioneDa", new ActionMessage(
                        "formato.data.errato", "data Registrazione Da"));
            } else if (dataRegistrazioneA != null
                    && !"".equals(dataRegistrazioneA)
                    && !DateUtil.isData(dataRegistrazioneA)) {
                errors.add("dataRegistrazioneDa", new ActionMessage(
                        "formato.data.errato", "data Registrazione A"));
            } else if (dataRegistrazioneDa != null
                    && !"".equals(dataRegistrazioneDa)
                    && DateUtil.isData(dataRegistrazioneDa)
                    && dataRegistrazioneA != null
                    && !"".equals(dataRegistrazioneA)
                    && DateUtil.isData(dataRegistrazioneA)) {
                if (DateUtil.toDate(dataRegistrazioneA).before(
                        DateUtil.toDate(dataRegistrazioneDa))) {
                    errors.add("dataRegistrazioneDa", new ActionMessage(
                            "date_incongruenti"));
                }
            }
        }

        return errors;
    }

    public String getVisualizzaDestinatariAssegnatari() {

        if ("I".equals(getTipoProtocollo())) {
            return "I";
        } else if ("U".equals(getTipoProtocollo())) {
            return "U";
        } else if ("P".equals(getTipoProtocollo())) {
            return "P";
        }else if ("F".equals(getTipoProtocollo())) {
            return "I";
        }  
        else {
            return null;
        }

    }
    
    public void inizializzaForm() {
        setAnnoProtocolloA(null);
        setAnnoProtocolloDa(null);
        setDataRegistrazioneA(null);
        setDataRegistrazioneDa(null);
        setRiservato(null);
        setNumeroProtocolloA(null);
        setNumeroProtocolloDa(null);
        setStatoProtocollo("");
        //visuali
    }

    public Collection getStatiProtocollo() {
        return statiProtocollo;
    }

    public void setStatiProtocollo(Collection statiProtocollo) {
        this.statiProtocollo = statiProtocollo;
    }
}