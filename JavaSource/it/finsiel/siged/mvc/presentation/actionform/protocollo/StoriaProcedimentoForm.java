package it.finsiel.siged.mvc.presentation.actionform.protocollo;

import java.util.Collection;

import org.apache.struts.action.ActionForm;

public class StoriaProcedimentoForm extends ActionForm{

	
	private String dataAvvio;

	private int procedimentoId;
	
	private String numeroProcedimento;

	private int versione;

	private String oggettoProcedimento;
	
	private Collection versioniProcedimento;
	
	
    public String getDataAvvio() {
		return dataAvvio;
	}

	public int getProcedimentoId() {
		return procedimentoId;
	}

	public String getNumeroProcedimento() {
		return numeroProcedimento;
	}

	public int getVersione() {
		return versione;
	}

	public String getOggettoProcedimento() {
		return oggettoProcedimento;
	}

	public void setDataAvvio(String dataAvvio) {
		this.dataAvvio = dataAvvio;
	}

	public void setProcedimentoId(int procedimentoId) {
		this.procedimentoId = procedimentoId;
	}

	public void setNumeroProcedimento(String numeroProcedimento) {
		this.numeroProcedimento = numeroProcedimento;
	}

	public void setVersione(int versione) {
		this.versione = versione;
	}

	public void setOggettoProcedimento(String oggettoProcedimento) {
		this.oggettoProcedimento = oggettoProcedimento;
	}

	

    public Collection getVersioniProcedimento() {
        return versioniProcedimento;
    }

    public void setVersioniProcedimento(Collection versioniProcedimento) {
        this.versioniProcedimento = versioniProcedimento;
    }

}