package it.compit.fenice.mvc.vo.protocollo;

import java.util.Date;

import it.finsiel.siged.mvc.vo.VersioneVO;

public class DomandaVO extends VersioneVO{

	private static final long serialVersionUID = 1L;

	private String idDomanda;
	
	private String oggetto;
	
	private String nome;
	
	private String cognome;
	
	private String tipoIndirizzo;
	
	private String indirizzo;
	
	private String civico;
	
	private String cap;
	
	private String comune;
	
	private String provincia;
	
	private Date dataIscrizione;
	
	private int statoDomanda;
	
	private String annoIscrizione;

	private Date dataNascita;
	
	private String comuneNascita;
	
	private String matricola;
	
	private String path;
	
	private String mail;
	
	public String getComuneNascita() {
		return comuneNascita;
	}

	public String getMatricola() {
		return matricola;
	}

	public void setComuneNascita(String comuneNascita) {
		this.comuneNascita = comuneNascita;
	}

	public void setMatricola(String matricola) {
		this.matricola = matricola;
	}

	public Date getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}

	public int getStatoDomanda() {
		return statoDomanda;
	}

	public void setStatoDomanda(int statoDomanda) {
		this.statoDomanda = statoDomanda;
	}

	public String getIdDomanda() {
		return idDomanda;
	}

	public String getOggetto() {
		return oggetto;
	}

	public String getNome() {
		return nome;
	}

	public String getCognome() {
		return cognome;
	}

	public String getTipoIndirizzo() {
		return tipoIndirizzo;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public String getCivico() {
		return civico;
	}

	public String getCap() {
		return cap;
	}

	public String getComune() {
		return comune;
	}

	public String getProvincia() {
		return provincia;
	}

	public Date getDataIscrizione() {
		return dataIscrizione;
	}

	public String getAnnoIscrizione() {
		return annoIscrizione;
	}

	public void setIdDomanda(String idDomanda) {
		this.idDomanda = idDomanda;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public void setTipoIndirizzo(String tipoIndirizzo) {
		this.tipoIndirizzo = tipoIndirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public void setDataIscrizione(Date dataIscrizione) {
		this.dataIscrizione = dataIscrizione;
	}

	public void setAnnoIscrizione(String annoIscrizione) {
		this.annoIscrizione = annoIscrizione;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public String getIndirizzoNumCivico() {
		if (getIndirizzo() == null || getIndirizzo().trim().equals(""))
			return "";
		return getIndirizzo()
				+ ((getCivico() == null || getCivico().trim().equals("") || getCivico().trim().equals("-") ) ? 
						"":", "+ getCivico() );
	}

	public String getIndirizzoCompleto() {
		if (getComune() == null || getComune().trim().equals(""))
			return "";
		
		return ((getIndirizzoNumCivico() == null || getIndirizzoNumCivico().trim().equals("") ) ? 
				"": getIndirizzoNumCivico() )+((getCap() == null || getCap().trim().equals("") ) ? 
						"":" - "+ getCap())+" "+getComune();
	}
	
}
