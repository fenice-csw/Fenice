package it.compit.fenice.mvc.presentation.helper;

public class VersioneUtenteView {

	private int caricaId;

	private int versione;

	private String carica;

	private String ufficio;

	private String profilo;

	private boolean attivo;

	private boolean dirigente;

	private boolean referente;

	private String dataInizio;
	
	private String dataFine;

	private String autore;

	public String getDataFine() {
		return dataFine;
	}

	public void setDataFine(String dataFine) {
		this.dataFine = dataFine;
	}

	public String getDataInizio() {
		return dataInizio;
	}

	public String getAutore() {
		return autore;
	}

	public void setDataInizio(String dataInizio) {
		this.dataInizio = dataInizio;
	}

	public void setAutore(String autore) {
		this.autore = autore;
	}

	public int getCaricaId() {
		return caricaId;
	}

	public int getVersione() {
		return versione;
	}

	public String getCarica() {
		return carica;
	}

	public String getUfficio() {
		return ufficio;
	}

	public String getProfilo() {
		return profilo;
	}

	public String getAttivo() {
		if (attivo)
			return "SI";
		else
			return "NO";
	}

	public String getDirigente() {
		if (dirigente)
			return "SI";
		else
			return "NO";
	}

	public String getReferente() {
		if (referente)
			return "SI";
		else
			return "NO";
	}

	public void setCaricaId(int caricaId) {
		this.caricaId = caricaId;
	}

	public void setVersione(int versione) {
		this.versione = versione;
	}

	public void setCarica(String carica) {
		this.carica = carica;
	}

	public void setUfficio(String ufficio) {
		this.ufficio = ufficio;
	}

	public void setProfilo(String profilo) {
		this.profilo = profilo;
	}

	public void setAttivo(boolean attivo) {
		this.attivo = attivo;
	}

	public void setDirigente(boolean dirigente) {
		this.dirigente = dirigente;
	}

	public void setReferente(boolean referente) {
		this.referente = referente;
	}

}
