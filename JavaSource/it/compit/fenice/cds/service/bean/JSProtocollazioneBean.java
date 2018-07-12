package it.compit.fenice.cds.service.bean;

import java.io.Serializable;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class JSProtocollazioneBean  extends ActionForm implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer richiestaId;
	
	private String oggetto;

	private JSPersonaBean persona; 
	
	// I | U | N
	private String tipoProtocollo;
	
	private List<JSFileProtoBean> fileProtos; 

	public Integer getRichiestaId() {
		return richiestaId;
	}

	public void setRichiestaId(Integer richiestaId) {
		this.richiestaId = richiestaId;
	}	

	public JSPersonaBean getPersona() {
		return persona;
	}

	public void setPersona(JSPersonaBean persona) {
		this.persona = persona;
	}

	public String getTipoProtocollo() {
		return tipoProtocollo;
	}

	public void setTipoProtocollo(String tipoProtocollo) {
		this.tipoProtocollo = tipoProtocollo;
	}

	public List<JSFileProtoBean> getFileProtos() {
		return fileProtos;
	}

	public void setFileProtos(List<JSFileProtoBean> fileProtos) {
		this.fileProtos = fileProtos;
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}
	
}
