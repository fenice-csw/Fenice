package it.compit.fenice.mvc.presentation.helper;

public class ReportCheckPostaInternaView {

	private String protocolloDescrizione;
	
	private int protocolloId;
	
	private int checkId;
	
	private String dataOperazione;
	
	private String assegnatario;

	private boolean competente;

	public String getCompetenteDescr(){
		return competente?"SI":"NO";
	}
	
	public boolean isCompetente() {
		return competente;
	}

	public void setCompetente(boolean competente) {
		this.competente = competente;
	}

	public int getProtocolloId() {
		return protocolloId;
	}

	public void setProtocolloId(int protocolloId) {
		this.protocolloId = protocolloId;
	}

	public String getProtocolloDescrizione() {
		return protocolloDescrizione;
	}

	public int getCheckId() {
		return checkId;
	}

	public String getDataOperazione() {
		return dataOperazione;
	}

	public String getAssegnatario() {
		return assegnatario;
	}

	public void setProtocolloDescrizione(String protocolloDescrizione) {
		this.protocolloDescrizione = protocolloDescrizione;
	}

	public void setCheckId(int checkId) {
		this.checkId = checkId;
	}

	public void setDataOperazione(String dataOperazione) {
		this.dataOperazione = dataOperazione;
	}

	public void setAssegnatario(String assegnatario) {
		this.assegnatario = assegnatario;
	}
}
