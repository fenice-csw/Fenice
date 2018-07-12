package it.compit.fenice.cds.service.bean;

import java.util.List;


public class JSPersonaBean {

	private String cognome;
	
	private String nome;
	
	private String codiceFiscale;

	// dd/MM/yyyy
	private String  dataNascita;
    
	private String luogoNascita;
	
	private String provinciaNascita;
	
	private String denominazioneSocieta;
	
	private String partitaIva;
	
	private String sedePgComune;
	
	private String sedePgCap;
	
	private String sedePgProvincia;
	
	private String sedePgIndirizzo;
	
	private List<String> emails;

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getLuogoNascita() {
		return luogoNascita;
	}

	public void setLuogoNascita(String luogoNascita) {
		this.luogoNascita = luogoNascita;
	}

	public String getProvinciaNascita() {
		return provinciaNascita;
	}

	public void setProvinciaNascita(String provinciaNascita) {
		this.provinciaNascita = provinciaNascita;
	}

	public String getDenominazioneSocieta() {
		return denominazioneSocieta;
	}

	public void setDenominazioneSocieta(String denominazioneSocieta) {
		this.denominazioneSocieta = denominazioneSocieta;
	}

	public String getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	public String getSedePgComune() {
		return sedePgComune;
	}

	public void setSedePgComune(String sedePgComune) {
		this.sedePgComune = sedePgComune;
	}

	public String getSedePgCap() {
		return sedePgCap;
	}

	public void setSedePgCap(String sedePgCap) {
		this.sedePgCap = sedePgCap;
	}

	public String getSedePgProvincia() {
		return sedePgProvincia;
	}

	public void setSedePgProvincia(String sedePgProvincia) {
		this.sedePgProvincia = sedePgProvincia;
	}

	public String getSedePgIndirizzo() {
		return sedePgIndirizzo;
	}

	public void setSedePgIndirizzo(String sedePgIndirizzo) {
		this.sedePgIndirizzo = sedePgIndirizzo;
	}
}
