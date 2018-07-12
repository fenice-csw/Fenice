package it.finsiel.siged.mvc.presentation.actionform.report;

import it.finsiel.siged.mvc.presentation.actionform.protocollo.AlberoUfficiUtentiForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.util.NumberUtil;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class ReportAssegnatiForm extends ReportCommonForm implements
        AlberoUfficiUtentiForm {

	private String anno;
	
	private AssegnatarioView selezionato;

	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

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

	
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors(); 
		if (getSelezionato() == null) {
	            errors.add("Seleziona ufficio", new ActionMessage("campo.obbligatorio",
	                    "Assegnatario", ""));
	        }
		if(getAnno()!=null && !getAnno().trim().equals("")) 
			if(!NumberUtil.isInteger(getAnno()))
				errors.add("anno", new ActionMessage(
					"formato.numerico.errato", "Anno"));
		 return errors;
	}
	
}