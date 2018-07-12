package it.compit.fenice.mvc.presentation.helper;

import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;

import java.sql.Timestamp;

public class EditorView{

	private int documentoId;

	private int caricaId;

	private String txt;

	private String nome;

	private String oggetto;

	private int flagStato;

	private int flagTipo;
	
	private int versione;

	private Timestamp dataCreazione;

	private String fascicolo;
		
	private String msgCarica;
	
	private String mittente;

	
	public EditorView() {
	}


	public int getDocumentoId() {
		return documentoId;
	}


	public void setDocumentoId(int documentoId) {
		this.documentoId = documentoId;
	}


	public int getCaricaId() {
		return caricaId;
	}


	public void setCaricaId(int caricaId) {
		this.caricaId = caricaId;
	}


	public String getTxt() {
		return txt;
	}


	public void setTxt(String txt) {
		this.txt = txt;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getOggetto() {
		return oggetto;
	}


	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}


	public int getFlagStato() {
		return flagStato;
	}


	public void setFlagStato(int flagStato) {
		this.flagStato = flagStato;
	}


	public int getFlagTipo() {
		return flagTipo;
	}


	public void setFlagTipo(int flagTipo) {
		this.flagTipo = flagTipo;
	}


	public int getVersione() {
		return versione;
	}


	public void setVersione(int versione) {
		this.versione = versione;
	}


	public Timestamp getDataCreazione() {
		return dataCreazione;
	}


	public void setDataCreazione(Timestamp dataCreazione) {
		this.dataCreazione = dataCreazione;
	}


	public String getFascicolo() {
		return fascicolo;
	}


	public void setFascicolo(String fascicolo) {
		this.fascicolo = fascicolo;
	}


	public String getMsgCarica() {
		return msgCarica;
	}


	public void setMsgCarica(String msgCarica) {
		this.msgCarica = msgCarica;
	}


	public String getMittente() {
		return mittente;
	}


	public void setMittente(String mittente) {
		this.mittente = mittente;
	}
	
	public String getStato() {
		if (flagStato == 0)
			return "in lavorazione";
		if (flagStato == 1)
			return "Protocollato";
		if (flagStato == 2)
			return "Inviato al Protocollo";
		if (flagStato == 3)
			return "Inviato al altro utente";
		else
			return " ";
	}
	
	public String getIntestatario() {
		Organizzazione org = Organizzazione.getInstance();
		String responsabile = "";
		if (getCaricaId() != 0) {
			CaricaVO carica = org.getCarica(getCaricaId());
			if (carica != null) {
				Ufficio uff = org.getUfficio(carica.getUfficioId());
				if (uff != null)
					responsabile = uff.getPath();
				Utente ute = org.getUtente(carica.getUtenteId());
				if (ute == null)
					return uff.getPath() + carica.getNome();
				ute.getValueObject().setCarica(carica.getNome());
				if (carica.isAttivo())
					responsabile = uff.getPath()
							+ ute.getValueObject().getCaricaFullName();
				else
					responsabile = uff.getPath()
							+ ute.getValueObject().getCaricaFullNameNonAttivo();
			}

		}
		return responsabile;
	}

}
