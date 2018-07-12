package it.compit.fenice.mvc.presentation.actionform.documentale;

import it.compit.fenice.mvc.presentation.helper.DocumentoAvvocatoGeneraleULLView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts.action.ActionForm;

public class AvvocatoGeneraleForm extends ActionForm implements Serializable{

	private static final long serialVersionUID = 1L;

	private Map<Integer, DocumentoAvvocatoGeneraleULLView> documenti=new HashMap<Integer, DocumentoAvvocatoGeneraleULLView>();

	private String[] chkBox;
	
	private boolean faseRelatoria;
	
	private boolean faseDecretoPresidente;
	
	
	public boolean isFaseRelatoria() {
		return faseRelatoria;
	}

	public void setFaseRelatoria(boolean faseRelatoria) {
		this.faseRelatoria = faseRelatoria;
	}

	public boolean isFaseDecretoPresidente() {
		return faseDecretoPresidente;
	}

	public void setFaseDecretoPresidente(boolean faseDecretoPresidente) {
		this.faseDecretoPresidente = faseDecretoPresidente;
	}

	public Map<Integer, DocumentoAvvocatoGeneraleULLView> getDocumenti() {
		return documenti;
	}

	public Collection<DocumentoAvvocatoGeneraleULLView> getDocumentiCollection() {
		return documenti.values();
	}
	
	public void setDocumenti(Map<Integer, DocumentoAvvocatoGeneraleULLView> doc) {
		this.documenti = doc;
	}

	public String[] getChkBox() {
		return chkBox;
	}

	public Collection<Integer> getProcedimentiIdChkBox() {
		Collection<Integer> ids=new ArrayList<Integer>();
		for(String id:chkBox){
			DocumentoAvvocatoGeneraleULLView view=documenti.get(Integer.valueOf(id));
			ids.add(view.getProcedimentoId());
		}	
		return ids;
	}

	
	public void setChkBox(String[] chkBox) {
		this.chkBox = chkBox;
	}

}