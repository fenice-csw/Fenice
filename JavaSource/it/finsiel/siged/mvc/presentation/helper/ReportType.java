package it.finsiel.siged.mvc.presentation.helper;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts.util.MessageResources;

/**
 * @author Almaviva sud
 * 
 */

public class ReportType {

    private String tipoReport;

    private String descReport;

    private Map parameters = new HashMap();

    public ReportType() {
    }
    
    
    @SuppressWarnings("unchecked")
	public static ReportType getIstanceByType(String type, HashMap common, MessageResources messages){
    	ReportType report = new ReportType();
    	report.setDescReport(messages.getMessage("report.type.desc"+type));
    	report.setTipoReport(type);
        HashMap pars = new HashMap(common);
        pars.put("ReportFormat", type);
        report.setParameters(pars);
        return report;
    }

    /**
     * @return Returns the descReport.
     */
    public String getDescReport() {
        return descReport;
    }

    /**
     * @param descReport
     *            The descReport to set.
     */
    public void setDescReport(String descReport) {
        this.descReport = descReport;
    }

    /**
     * @return Returns the tipoReport.
     */
    public String getTipoReport() {
        return tipoReport;
    }

    /**
     * @param tipoReport
     *            The tipoReport to set.
     */
    public void setTipoReport(String tipoReport) {
        this.tipoReport = tipoReport;
    }

    /**
     * @return Returns the parameters.
     */
    public Map getParameters() {
        return parameters;
    }

    /**
     * @param parameters
     *            The parameters to set.
     */
    public void setParameters(Map parameters) {
        this.parameters = parameters;
    }
}