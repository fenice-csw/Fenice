package it.compit.fenice.mvc.presentation.actionform.protocollo;

import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts.action.ActionForm;

public class AlertProtocolloForm extends ActionForm{
	
	private static final long serialVersionUID = 1L;
	
	private Map<String,ReportProtocolloView> protocollo = new HashMap<String,ReportProtocolloView>(2);

    public Map<String,ReportProtocolloView> getProtocolli() {
        return protocollo;
    }

    public Collection<ReportProtocolloView> getProtocolliCollection() {
        return protocollo.values();
    }

    public void setProtocolli(Map<String,ReportProtocolloView> protocolli) {
        this.protocollo = protocolli;
    }
    
        
    public void rimuoviProtocollo(String id) {
        this.protocollo.remove(id);
    }

    public void aggiungiProtocollo(ReportProtocolloView f) {
        this.protocollo.put(String.valueOf(f.getProtocolloId()), f);
    }
}
