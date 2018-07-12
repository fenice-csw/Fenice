package it.finsiel.siged.mvc.presentation.helper;

public class AssegnatarioView {
	private int ufficioId;

	private int utenteId;

	private int caricaId;

	private String nomeUfficio;

	private String descrizioneUfficio;

	private String nomeUtente;

	private String nomeCarica;

	private char stato;

	private boolean competente = false;

	private int caricaAssegnanteId;

	private int ufficioAssegnanteId;

	private String msg;

	private boolean presaVisione;

	private boolean lavorato;

	private boolean titolareProcedimento;

	
	public boolean isTitolareProcedimento() {
		return titolareProcedimento;
	}

	public void setTitolareProcedimento(boolean titolareProcedimento) {
		this.titolareProcedimento = titolareProcedimento;
	}

	public boolean isLavorato() {
		return lavorato;
	}

	public void setLavorato(boolean lavorato) {
		this.lavorato = lavorato;
	}

	public boolean isPresaVisione() {
		return presaVisione;
	}

	public void setPresaVisione(boolean presaVisione) {
		this.presaVisione = presaVisione;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCaricaAssegnanteId() {
		return caricaAssegnanteId;
	}

	public int getUfficioAssegnanteId() {
		return ufficioAssegnanteId;
	}

	public void setCaricaAssegnanteId(int assegnanteCaricaId) {
		this.caricaAssegnanteId = assegnanteCaricaId;
	}

	public void setUfficioAssegnanteId(int assegnanteUfficioId) {
		this.ufficioAssegnanteId = assegnanteUfficioId;
	}

	public char getStato() {
		return stato;
	}

	public void setStato(char stato) {
		this.stato = stato;
	}

	public String getKey() {
		StringBuffer key = new StringBuffer();
		key.append(getUfficioId());
		if (getUtenteId() > 0) {
			key.append('_').append(getUtenteId());
		}
		if (getCaricaId() > 0) {
			key.append('_').append(getCaricaId());
		}
		return key.toString();
	}

	public String getNomeUfficio() {
		return nomeUfficio;
	}

	public void setNomeUfficio(String descrizione) {
		this.nomeUfficio = descrizione;
	}

	public int getUfficioId() {
		return ufficioId;
	}

	public void setUfficioId(int ufficioId) {
		this.ufficioId = ufficioId;
	}

	public int getUtenteId() {
		return utenteId;
	}

	public void setUtenteId(int utenteId) {
		this.utenteId = utenteId;
	}

	public String getNomeUtente() {
		return nomeUtente;
	}

	public void setNomeUtente(String descrizioneUtente) {
		this.nomeUtente = descrizioneUtente;
	}

	public int getCaricaId() {
		return caricaId;
	}

	public void setCaricaId(int caricaId) {
		this.caricaId = caricaId;
	}

	public String getNomeCarica() {
		return nomeCarica;
	}

	public void setNomeCarica(String nomeCarica) {
		this.nomeCarica = nomeCarica;
	}

	public String getDescrizioneUfficio() {
		return descrizioneUfficio;
	}

	public void setDescrizioneUfficio(String descrizioneUfficio) {
		this.descrizioneUfficio = descrizioneUfficio;
	}

	public boolean isCompetente() {
		return competente;
	}

	public void setCompetente(boolean competente) {
		this.competente = competente;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AssegnatarioView other = (AssegnatarioView) obj;
		if (ufficioId != other.ufficioId)
			return false;
		if (utenteId != other.utenteId)
			return false;
		if (caricaId != other.caricaId)
			return false;
		return true;
	}
	
	public String getNomeAssegnatario() {
		if(nomeUtente!=null && !nomeUtente.trim().equals(""))
			return nomeUfficio+"/"+nomeUtente;
		else
			return nomeUfficio;
	}

}