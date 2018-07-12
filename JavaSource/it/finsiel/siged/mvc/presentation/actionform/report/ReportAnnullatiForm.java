package it.finsiel.siged.mvc.presentation.actionform.report;

import it.finsiel.siged.mvc.presentation.actionform.protocollo.AlberoUfficiUtentiForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public final class ReportAnnullatiForm extends ReportCommonForm implements
        AlberoUfficiUtentiForm {

    private String assegnatario;

    private AssegnatarioView selezionato;
    
    public AssegnatarioView getSelezionato() {
		return selezionato;
	}

	public String getSelezionatoDescription() {
		String desc=selezionato.getDescrizioneUfficio();
		if(selezionato.getUtenteId()!=0)
			desc+=selezionato.getNomeUtente();
		return desc;
	}
	
	public void setSelezionato(AssegnatarioView selezionato) {
		this.selezionato = selezionato;
	}
    
    
    public String getAssegnatario() {
        return assegnatario;
    }

   
    public void setAssegnatario(String assegnatario) {
        this.assegnatario = assegnatario;
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        return errors;
    }
}