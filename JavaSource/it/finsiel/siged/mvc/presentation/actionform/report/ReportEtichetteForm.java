package it.finsiel.siged.mvc.presentation.actionform.report;

import it.finsiel.siged.mvc.presentation.actionform.protocollo.AlberoUfficiUtentiForm;
import it.finsiel.siged.util.DateUtil;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class ReportEtichetteForm extends ReportCommonForm implements
        AlberoUfficiUtentiForm {

    private String assegnatario;

    private Collection statistiche;

    private Collection dettaglioStatistiche;

    private String titoloDettaglioStatistiche;

    private Boolean visualizzaDettagli;

    public Collection getStatistiche() {
        return statistiche;
    }

    public void setStatistiche(Collection statistiche) {
        this.statistiche = statistiche;
    }

    public Collection getDettaglioStatistiche() {
        return dettaglioStatistiche;
    }

    public void setDettaglioStatistiche(Collection dettaglioStatistiche) {
        this.dettaglioStatistiche = dettaglioStatistiche;
    }

    /**
     * @return Returns the assegnatario.
     */
    public String getAssegnatario() {
        return assegnatario;
    }

    public String getTitoloDettaglioStatistiche() {
        return titoloDettaglioStatistiche;
    }

    public void setTitoloDettaglioStatistiche(String titoloDettaglioStatistiche) {
        this.titoloDettaglioStatistiche = titoloDettaglioStatistiche;
    }

    public Boolean getVisualizzaDettagli() {
        return visualizzaDettagli;
    }

    public void setVisualizzaDettagli(Boolean visualizzaDettagli) {
        this.visualizzaDettagli = visualizzaDettagli;
    }

    /**
     * @param assegnatario
     *            The assegnatario to set.
     */
    public void setAssegnatario(String assegnatario) {
        this.assegnatario = assegnatario;
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (request.getParameter("Cerca") != null
                || request.getParameter("btnStampa") != null) {

            if (getDataInizio() == null || "".equals(getDataInizio())) {
                errors.add("dataInizio", new ActionMessage(
                        "campo.obbligatorio", "Data inizio", ""));
            } else if (getDataFine() == null || "".equals(getDataFine())) {
                errors.add("dataFine", new ActionMessage("campo.obbligatorio",
                        "Data fine", ""));
            } else if (getDataInizio() != null
                    && !"".equals(getDataInizio().trim())
                    && !DateUtil.isData(getDataInizio())) {
                errors.add("dataInizio", new ActionMessage(
                        "formato.data.errato", "Data inizio", ""));
            } else if (getDataFine() != null
                    && !"".equals(getDataFine().trim())
                    && !DateUtil.isData(getDataFine())) {
                errors.add("dataFine", new ActionMessage("formato.data.errato",
                        "Data fine", ""));
            } else if (getDataFine() != null
                    && !"".equals(getDataFine().trim())
                    && getDataInizio() != null
                    && !"".equals(getDataInizio().trim())
                    && DateUtil.toDate(getDataFine()).before(
                            DateUtil.toDate(getDataInizio()))) {
                errors
                        .add("dataInizio", new ActionMessage(
                                "date_incongruenti"));
            }
        }
        return errors;
    }

}