package it.compit.fenice.mvc.presentation.helper;


public class DomandaView {

	
	private String domandaId;

	private String nome;

	private String cognome;

	private String oggetto;

	private int stato;
	
	private String comune;
	
	private String dataIscrizione;

	private String dataNascita;
	
	private String comuneNascita;
	
	private String matricola;
	
	private int ufficioId;
	
	public DomandaView() {
	}

	public String getDescrizioneStato(){
		if(stato==1)
			return "IN ATTESA";
		else if(stato==2)
			return "ELIMINATO";
		else if(stato==3)
			return "PROTOCOLLATO";
		else return "";
	}
	
	public String getDomandaId() {
		return domandaId;
	}

	public String[] getKey() {
		String[] key={dataIscrizione,domandaId};
		return key;
	}

	public String getDataNascita() {
		return dataNascita;
	}

	public String getComuneNascita() {
		return comuneNascita;
	}

	public String getMatricola() {
		return matricola;
	}

	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}

	public void setComuneNascita(String comuneNascita) {
		this.comuneNascita = comuneNascita;
	}

	public void setMatricola(String matricola) {
		this.matricola = matricola;
	}

	public String getNome() {
		return nome;
	}

	public String getCognome() {
		return cognome;
	}

	
	public String getOggetto() {
		return oggetto;
	}

	public int getStato() {
		return stato;
	}

	public String getComune() {
		return comune;
	}

	public String getDataIscrizione() {
		return dataIscrizione;
	}

	public void setDomandaId(String domandaId) {
		this.domandaId = domandaId;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public void setStato(int stato) {
		this.stato = stato;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public void setDataIscrizione(String dataIscrizione) {
		this.dataIscrizione = dataIscrizione;
	}

	public int getUfficioId() {
		return ufficioId;
	}

	public void setUfficioId(int ufficioId) {
		this.ufficioId = ufficioId;
	}
	
	
}