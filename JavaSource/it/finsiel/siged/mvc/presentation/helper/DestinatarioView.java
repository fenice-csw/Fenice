package it.finsiel.siged.mvc.presentation.helper;

import it.finsiel.siged.mvc.vo.VersioneVO;

public class DestinatarioView extends VersioneVO {

	private static final long serialVersionUID = 1L;

	public DestinatarioView() {
    }
    private String nome;
    
    private String cognome;
    
   
	public String getNome() {
		return nome;
	}

	
	public String getCognome() {
		return cognome;
	}

	
	public void setNome(String nome) {
		this.nome = nome;
	}

	
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	// temp
    private int idx;

    private int mezzoSpedizioneId;

    private String dataSpedizione;

    private String mezzoDesc; // da lookupdelegate o outer join su spedizioni

    private String flagTipoDestinatario;

    private String indirizzo;
    
    private String capDestinatario;

    private String email;

    private String destinatario;

    private String titoloDestinatario;

    private String citta;

    private boolean flagConoscenza;
    
    private boolean flagPresso;
    
    private boolean flagPEC;

    private String dataEffettivaSpedizione;

    private String note;

    private int titoloId;
    
    private String prezzoSpedizione;
   
    public String getDestinatarioBBCCAATemplate(){
    return getDestinatario()
	+ (getIndirizzo() == null
			|| getIndirizzo().trim().equals("") ? "" : "\r\n"
			+ getIndirizzo())
	+ (getCapDestinatario() == null
			|| getCapDestinatario().trim().equals("") ? ""
			: "\r\n" + getCapDestinatario())
	+ (getCitta() == null || getCitta().trim().equals("") ? ""
			: "\r\n" + getCitta());
    }
    
    public String getIndirizzoView(){
    	return getDestinatario()
		+ (getIndirizzo() == null|| getIndirizzo().trim().equals("") ? "" : ","+ getIndirizzo())
		+ (getCapDestinatario() == null|| getCapDestinatario().trim().equals("") ? "": " " + getCapDestinatario())
		+ (getCitta() == null || getCitta().trim().equals("") ? "": " - " + getCitta());
    }
    /**
	 * @return the prezzoSpedizione
	 */
	public String getPrezzoSpedizione() {
		return prezzoSpedizione;
	}


	/**
	 * @param prezzoSpedizione the prezzoSpedizione to set
	 */
	public void setPrezzoSpedizione(String prezzoSpedizione) {
		this.prezzoSpedizione = prezzoSpedizione;
	}


	/**
     * @return Returns the dataEffettivaSpedizione.
     */
    public String getDataEffettivaSpedizione() {
        return dataEffettivaSpedizione;
    }

    /**
     * @param dataEffettivaSpedizione
     *            The dataEffettivaSpedizione to set.
     */
    public void setDataEffettivaSpedizione(String dataEffettivaSpedizione) {
        this.dataEffettivaSpedizione = dataEffettivaSpedizione;
    }

    /**
     * @return Returns the dataSpedizione.
     */
    public String getDataSpedizione() {
        return dataSpedizione;
    }

    /**
     * @param dataSpedizione
     *            The dataSpedizione to set.
     */
    public void setDataSpedizione(String dataSpedizione) {
        this.dataSpedizione = dataSpedizione;
    }

    /**
     * @return Returns the destinatario.
     */
    public String getDestinatario() {
    	 if("F".equals(flagTipoDestinatario) && null != nome && null != cognome){
        	return nome + " " + cognome;
        }else{
        	return destinatario;
        }
    }

    /**
     * @param destinatario
     *            The destinatario to set.
     */
    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    /**
     * @return Returns the email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     *            The email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public String getConoscenza() {
        if (flagConoscenza) {
            return "SI";
        } else {
            return "NO";
        }
    }

    public String getPresso() {
        if (flagPresso) {
            return "SI";
        } else {
            return "NO";
        }
    }

    
   
	public String getPEC() {
		if (flagPEC) {
            return "SI";
        } else {
            return "NO";
        }
	}

	public boolean getFlagPEC() {
		return flagPEC;
	}
	
	public void setFlagPEC(boolean flagPEC) {
		this.flagPEC = flagPEC;
	}

	/**
     * @return Returns the flagPerConoscenza.
     */
    public boolean getFlagConoscenza() {
        return flagConoscenza;
    }

    /**
     * @param flagConoscenza
     *            The flagPerConoscenza to set.
     */
    public void setFlagConoscenza(boolean flagConoscenza) {
        this.flagConoscenza = flagConoscenza;
    }

    
    public void setFlagPresso(boolean flagPresso) {
        this.flagPresso = flagPresso;
    }

    public boolean getFlagPresso() {
        return flagPresso;
    }
    
    
    /**
     * @return Returns the flagTipoDestinatario.
     */
    public String getFlagTipoDestinatario() {
        return flagTipoDestinatario;
    }

    /**
     * @param flagTipoDestinatario
     *            The flagTipoDestinatario to set.
     */
    public void setFlagTipoDestinatario(String flagTipoDestinatario) {
        this.flagTipoDestinatario = flagTipoDestinatario;
    }

    /**
     * @return Returns the indirizzo.
     */
    public String getIndirizzo() {
        return indirizzo;
    }

    /**
     * @param indirizzo
     *            The indirizzo to set.
     */
    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    /**
     * @return Returns the citta.
     */
    public String getCitta() {
        return citta;
    }

    /**
     * @param citta
     *            The citta to set.
     */
    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getTitoloDestinatario() {
        return titoloDestinatario;
    }

    public void setTitoloDestinatario(String titoloDestinatario) {
        this.titoloDestinatario = titoloDestinatario;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getTitoloId() {
        return titoloId;
    }

    public void setTitoloId(int titoloId) {
        this.titoloId = titoloId;
    }

    public int getMezzoSpedizioneId() {
        return mezzoSpedizioneId;
    }

    public void setMezzoSpedizioneId(int mezzoSpedizioneId) {
        this.mezzoSpedizioneId = mezzoSpedizioneId;
    }

    public String getMezzoDesc() {
        return mezzoDesc;
    }

    public void setMezzoDesc(String mezziDest) {
        this.mezzoDesc = mezziDest;
    }

    public String getDestinatarioMezzoId() {
        return String.valueOf(getIdx()) + "_"
                + String.valueOf(getMezzoSpedizioneId());
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getCapDestinatario() {
        return capDestinatario;
    }

    public void setCapDestinatario(String capDestinatario) {
        this.capDestinatario = capDestinatario;
    }

}