package it.finsiel.siged.mvc.vo.organizzazione;

import it.finsiel.siged.mvc.vo.VersioneVO;

import java.util.Date;

/*
 * @author Almaviva sud.
 */

public final class UtenteVO extends VersioneVO implements Comparable<UtenteVO>{

	private static final long serialVersionUID = 1L;

	private int aooId;

	private String username;

	private String password;

	private String tempFolder;

	private String cognome;

	private String nome;

	private String emailAddress;

	private String codiceFiscale;

	private String matricola;

	private boolean abilitato;

	private Date dataFineAttivita;

	private int numeroProtocolliAssegnati;

	private boolean flagInOggetto;
	
	private String carica;

	public String getCarica() {
		return carica;
	}

	public void setCarica(String carica) {
		this.carica = carica;
	}

	
	public boolean getFlagInOggetto() {
		return flagInOggetto;
	}

	public void setFlagInOggetto(boolean flagInOggetto) {
		this.flagInOggetto = flagInOggetto;
	}

	// Constructors
	public UtenteVO() {
	}

	public boolean isSuperAdmin() {
		if (username.equals("admin") 
				&& cognome.equals("Amministratore AOO")
				&& nome.equals("Amministratore AOO"))
			return true;
		else
			return false;
	}

	public int getNumeroProtocolliAssegnati() {
		return numeroProtocolliAssegnati;
	}

	public void setNumeroProtocolliAssegnati(int numeroProtocolliAssegnati) {
		this.numeroProtocolliAssegnati = numeroProtocolliAssegnati;
	}

	// Getters
	public int getAooId() {
		return this.aooId;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	// Setters
	public void setAooId(int aooId) {
		this.aooId = aooId;
	}

	public void setUsername(String u) {
		this.username = u;
	}

	public void setPassword(String pwd) {
		this.password = pwd;
	}

	
	public String getCaricaFullNameNonAttivo() {
		return (getCognome() != null ? getCognome() : " ") + " "
				+ (getNome() != null ? getNome() : "") + " ("
				+ (getCarica() != null ? getCarica() : " ") + " - Inattivo)";
	}

	public String getCaricaFullName() {
		return (getCognome() != null ? getCognome() : " ") + " "
				+ (getNome() != null ? getNome() : "") + " ("
				+ (getCarica() != null ? getCarica() : " ") + ")";
	}

	
	public String getFullName() {
		return (getCognome() != null ? getCognome() : " ") + " "
				+ (getNome() != null ? getNome() : "");
	}

	
	public String getCognomeNome() {
		return (getCognome() != null ? getCognome() : "") + ' '
				+ (getNome() != null ? getNome() : " ");
	}

	public String getTempFolder() {
		return tempFolder;
	}

	public void setTempFolder(String tempFolder) {
		this.tempFolder = tempFolder;
	}

	public boolean isAbilitato() {
		return abilitato;
	}

	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public Date getDataFineAttivita() {
		return dataFineAttivita;
	}

	public void setDataFineAttivita(Date dataFineAttivita) {
		this.dataFineAttivita = dataFineAttivita;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getMatricola() {
		return matricola;
	}

	public void setMatricola(String matricola) {
		this.matricola = matricola;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cognome == null) ? 0 : cognome.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UtenteVO other = (UtenteVO) obj;
		if (cognome == null) {
			if (other.cognome != null)
				return false;
		} else if (!cognome.equals(other.cognome))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	// Utility
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append("Utente [Id: " + super.getId());
		str.append(" | Username: " + username);
		str.append(" | Password: " + password);
		str.append(" | Cognome Nome: " + getFullName());
		str.append("]");
		return str.toString();
	}

	@Override
	public int compareTo(UtenteVO u) {
		int lastCmp = this.cognome.compareTo(u.getCognome());
        return (lastCmp != 0 ? lastCmp :
                nome.compareTo(u.getNome()));
	}
}