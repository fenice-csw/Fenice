package it.finsiel.siged.mvc.presentation.actionform;

import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.mvc.presentation.helper.ReportType;

import java.util.Collection;
import java.util.HashMap;

import org.apache.struts.action.ActionForm;


public abstract class ParametriForm extends ActionForm {

  
	private static final long serialVersionUID = 1L;
	private HashMap<String,ReportType> reportFormats = new HashMap<String,ReportType>();

    public String getMozioneUscita() {
        return Parametri.LABEL_MOZIONE_USCITA;
    }

    public String getIngressoUscita() {
        return Parametri.LABEL_INGRESSO_USCITA;
    }
    
    public String getPostaInterna() {
        return Parametri.LABEL_POSTA_INTERNA;
    }
    
    public String getTutti() {
        return Parametri.TUTTI;
    }

    public String getFlagSospeso() {
        return Parametri.FLAG_STATO_SOSPESO;
    }

    public String getLabelSospeso() {
        return Parametri.LABEL_STATO_SOSPESO;
    }

    public String getAnnullato() {
        return Parametri.ANNULLATO;
    }

    public String getLabelAnnullato() {
        return Parametri.LABEL_ANNULLATO;
    }

    public void addReportType(ReportType type) {
        reportFormats.put(type.getTipoReport(), type);
    }

    public boolean removeReportType(String type) {
        return reportFormats.remove(type) != null;
    }

    public ReportType getReportType(String key) {
        return (ReportType) reportFormats.get(key);
    }

    public Collection<ReportType> getReportFormatsCollection() {
        return reportFormats.values();
    }

    /**
     * @return Returns the reportFormats.
     */
    public HashMap<String,ReportType> getReportFormats() {
        return reportFormats;
    }

    /**
     * @param reportFormats
     *            The reportFormats to set.
     */
    public void setReportFormats(HashMap<String,ReportType> reportFormats) {
        this.reportFormats = reportFormats;
    }
}