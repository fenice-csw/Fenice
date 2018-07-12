package it.compit.fenice.mvc.presentation.helper;

public class VersioneCaricaView {

	private int caricaId;

	private int versione;
	
    private String carica;

    private String ufficio;
    
    private String profilo;

    private String utente;
    
    private boolean attivo;
    
    private boolean dirigente;
    
    private boolean referente;
    
    private boolean responsabileEnte;

    private String dataOperazione;
    
    private String autore;

	public boolean isResponsabileEnte() {
		return responsabileEnte;
	}

	public void setResponsabileEnte(boolean responsabileEnte) {
		this.responsabileEnte = responsabileEnte;
	}

	public String getDataOperazione() {
		return dataOperazione;
	}

	public String getAutore() {
		return autore;
	}

	public void setDataOperazione(String dataOperazione) {
		this.dataOperazione = dataOperazione;
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

	public String getUtente() {
		return utente;
	}

	public boolean isAttivo() {
		return attivo;
	}

	public boolean isDirigente() {
		return dirigente;
	}

	public String getReferente() {
		if(referente)
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

	public void setUtente(String utente) {
		this.utente = utente;
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
