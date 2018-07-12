
package it.finsiel.siged.mvc.vo.lookup;

import it.finsiel.siged.mvc.vo.VersioneVO;

import java.util.Date;


public class SoggettoVO extends VersioneVO {
	
	private static final long serialVersionUID = 1L;

	private String tipo;
		
	private String note;

	private String telefono;

	private String teleFax;

	private String indirizzoWeb;

	private String indirizzoEMail;

	private int aoo;
	
	private String codiceFiscale;

	private String codMatricola;

	private String cognome;

	private String comuneNascita;

	private Date dataNascita;

	private String nome;

	private int provinciaNascitaId;

	private String qualifica;

	private String sesso;

	private String statoCivile;
	
	private String descrizioneDitta;

	private String dug;

	private long flagSettoreAppartenenza;

	private String partitaIva;

	private String referente;

	private String telefonoReferente;

	private String descrizione;
	
	private Indirizzo indirizzo = new Indirizzo();

	public SoggettoVO(String tipo) {
		setTipo(tipo);
	}

	public SoggettoVO(char tipo) {
		setTipo(tipo + "");
	}
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setIndirizzo(Indirizzo indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getIndirizzoNumCivico() {
		if (getIndirizzo().getToponimo() == null || getIndirizzo().getToponimo().trim().equals(""))
			return "";
		return getIndirizzo().getToponimo()
				+ ((getIndirizzo().getCivico() == null || getIndirizzo().getCivico().trim().equals("") ) ? 
						"":","+ getIndirizzo().getCivico() );
	}

	public String getIndirizzoCompleto() {
		if (getIndirizzo().getComune() == null || getIndirizzo().getComune().trim().equals(""))
			return "";
		
		return ((getIndirizzoNumCivico() == null || getIndirizzoNumCivico().trim().equals("") ) ? 
				"": getIndirizzoNumCivico() )+((getIndirizzo().getCap() == null || getIndirizzo().getCap().trim().equals("") ) ? 
						"":" - "+ getIndirizzo().getCap())+" "+getIndirizzo().getComune();
	}
	
	public Indirizzo getIndirizzo() {
		return indirizzo;
	}

	public int getAoo() {
		return aoo;
	}

	public void setAoo(int aoo) {
		this.aoo = aoo;
	}

	public String getIndirizzoEMail() {
		return indirizzoEMail;
	}

	public void setIndirizzoEMail(String indirizzoEMail) {
		this.indirizzoEMail = indirizzoEMail;
	}

	public String getIndirizzoWeb() {
		return indirizzoWeb;
	}

	public void setIndirizzoWeb(String indirizzoWeb) {
		this.indirizzoWeb = indirizzoWeb;
	}

	public String getTeleFax() {
		return teleFax;
	}

	public void setTeleFax(String teleFax) {
		this.teleFax = teleFax;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public String getCodiceStatoCivile() {
		return statoCivile;
	}

	public String getCodMatricola() {
		return codMatricola;
	}

	public String getCognome() {
		return cognome;
	}

	public String getComuneNascita() {
		return comuneNascita;
	}

	public Date getDataNascita() {
		return dataNascita;
	}

	public String getNome() {
		return nome;
	}

	public int getProvinciaNascitaId() {
		return provinciaNascitaId;
	}

	public String getQualifica() {
		return qualifica;
	}

	public String getSesso() {
		return sesso;
	}

	public String getStatoCivile() {
		return statoCivile;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public void setCodiceStatoCivile(String codiceStatoCivile) {
		this.statoCivile = codiceStatoCivile;
	}

	public void setCodMatricola(String codMatricola) {
		this.codMatricola = codMatricola;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public void setComuneNascita(String descrizioneComuneNascita) {
		this.comuneNascita = descrizioneComuneNascita;
	}

	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setProvinciaNascitaId(int provinciaNascitaId) {
		this.provinciaNascitaId = provinciaNascitaId;
	}

	public void setQualifica(String descrizioneQualifica) {
		this.qualifica = descrizioneQualifica;
	}

	public void setSesso(String sesso) {
		this.sesso = sesso;
	}

	public void setStatoCivile(String statoCivile) {
		this.statoCivile = statoCivile;
	}

	public String getDescrizioneDitta() {
		return descrizioneDitta;
	}

	public String getDug() {
		return dug;
	}

	public long getFlagSettoreAppartenenza() {
		return flagSettoreAppartenenza;
	}

	public String getPartitaIva() {
		return partitaIva;
	}

	public String getReferente() {
		return referente;
	}

	public String getTelefonoReferente() {
		return telefonoReferente;
	}

	public void setDescrizioneDitta(String descrizioneDitta) {
		this.descrizioneDitta = descrizioneDitta;
	}

	public void setDug(String dug) {
		this.dug = dug;
	}

	public void setFlagSettoreAppartenenza(long flagSettoreAppartenenza) {
		this.flagSettoreAppartenenza = flagSettoreAppartenenza;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	public void setReferente(String referente) {
		this.referente = referente;
	}

	public void setTelefonoReferente(String telefonoReferente) {
		this.telefonoReferente = telefonoReferente;
	}

	public String getDescrizione() {
		if (descrizione == null || descrizione.trim().equals(""))
			if (getTipo().equals("F"))
				return getCognome() + " " + getNome();
			else
				return getDescrizioneDitta();
		else
			return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	public class Indirizzo {

		private String comune;
		
		private String cap;

		private String dug;

		private String toponimo;

		private String civico;

		private int provinciaId;

		private Indirizzo() {
			
		}

		public String getComune() {
			return comune;
		}

		public void setComune(String comune) {
			this.comune = comune;
		}

		public String getCap() {
			return cap;
		}

		public void setCap(String cap) {
			this.cap = cap;
		}

		public String getCivico() {
			return civico;
		}

		public void setCivico(String civico) {
			this.civico = civico;
		}

		public String getDug() {
			return dug;
		}

		public void setDug(String dug) {
			this.dug = dug;
		}

		public int getProvinciaId() {
			return provinciaId;
		}

		public void setProvinciaId(int provinciaId) {
			this.provinciaId = provinciaId;
		}

		public String getToponimo() {
			return toponimo;
		}

		public void setToponimo(String toponimo) {
			this.toponimo = toponimo;
		}
	}

}