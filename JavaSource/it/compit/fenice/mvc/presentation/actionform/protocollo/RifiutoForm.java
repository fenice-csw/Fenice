package it.compit.fenice.mvc.presentation.actionform.protocollo;

import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.mvc.presentation.actionform.report.ReportCommonForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class RifiutoForm extends ReportCommonForm {

	private static final long serialVersionUID = 1L;

	String msg;
	
	private Integer protocolloId;

	private String stato;

	private String numeroProtocollo;

	private String dataDocumento;

	private String dataRicezione;

	private String dataRegistrazione;

	private String oggetto;

	private boolean riservato;
	
	private int utenteProtocollatoreId;

	private int ufficioProtocollatoreId;
	
	private int tipoDocumentoId;
	
	private boolean titolareProcedimento;

	public boolean isTitolareProcedimento() {
		return titolareProcedimento;
	}

	public void setTitolareProcedimento(boolean titolareProcedimento) {
		this.titolareProcedimento = titolareProcedimento;
	}

	public int getTipoDocumentoId() {
		return tipoDocumentoId;
	}

	public void setTipoDocumentoId(int tipoDocumentoId) {
		this.tipoDocumentoId = tipoDocumentoId;
	}

	public String getProtocollatore() {
		Organizzazione org = Organizzazione.getInstance();
		Ufficio ufficio=org.getUfficio(getUfficioProtocollatoreId());
		CaricaVO carica=org.getCarica(getUtenteProtocollatoreId());
		String protocollatore="";
		if(ufficio!=null)
			protocollatore=ufficio.getValueObject().getDescription();
		if(carica!=null)
			protocollatore+="/"+carica.getNome();
		return protocollatore;
	}
	
	public int getUtenteProtocollatoreId() {
		return utenteProtocollatoreId;
	}

	public void setUtenteProtocollatoreId(int utenteProtocollatoreId) {
		this.utenteProtocollatoreId = utenteProtocollatoreId;
	}

	public int getUfficioProtocollatoreId() {
		return ufficioProtocollatoreId;
	}

	public void setUfficioProtocollatoreId(int ufficioProtocollatoreId) {
		this.ufficioProtocollatoreId = ufficioProtocollatoreId;
	}

	public Integer getProtocolloId() {
		return protocolloId;
	}

	public void setProtocolloId(Integer protocolloId) {
		this.protocolloId = protocolloId;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getNumeroProtocollo() {
		return numeroProtocollo;
	}

	public void setNumeroProtocollo(String numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}

	public String getDataDocumento() {
		return dataDocumento;
	}

	public void setDataDocumento(String dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	public String getDataRicezione() {
		return dataRicezione;
	}

	public void setDataRicezione(String dataRicezione) {
		this.dataRicezione = dataRicezione;
	}

	public String getDataRegistrazione() {
		return dataRegistrazione;
	}

	public void setDataRegistrazione(String dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public boolean isRiservato() {
		return riservato;
	}

	public void setRiservato(boolean riservato) {
		this.riservato = riservato;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		return null;
	}

	public void inizializzaForm() {
		msg=null;
		getReportFormatsCollection().clear();
	}

}
