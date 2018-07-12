/* Generated by Together */

package it.finsiel.siged.mvc.vo.protocollo;

import it.finsiel.siged.mvc.vo.VersioneVO;

import java.util.Date;

public class DestinatarioVO extends VersioneVO {

	private static final long serialVersionUID = 1L;

	public DestinatarioVO() {
    }
    String nome;
    String cognome;
    
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
	
    private int idx;

    private int mezzoSpedizioneId;

    private Date dataSpedizione;

    private String mezzoDesc;

    private String flagTipoDestinatario;

    private String indirizzo;

    private String email;

    private String destinatario;

    private String titoloDestinatario;

    private int titoloId;

    private String codicePostale;

    private String codice;

    private String provinciaId;

    private String intestazione;

    private String citta;

    private boolean flagConoscenza;
    
    private boolean flagPresso;

    private boolean flagPEC;

    private Date dataEffettivaSpedizione;

    private int protocolloId;

    private String note;

    private String prezzo;
    
   
	public String getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(String prezzo) {
		this.prezzo = prezzo;
	}

	public boolean getFlagPEC() {
		return flagPEC;
	}
	
	public void setFlagPEC(boolean flagPEC) {
		this.flagPEC = flagPEC;
	}
    
    public Date getDataEffettivaSpedizione() {
        return dataEffettivaSpedizione;
    }

    
    public void setDataEffettivaSpedizione(Date dataEffettivaSpedizione) {
        this.dataEffettivaSpedizione = dataEffettivaSpedizione;
    }

    public Date getDataSpedizione() {
        return dataSpedizione;
    }

    public void setDataSpedizione(Date dataSpedizione) {
        this.dataSpedizione = dataSpedizione;
    }

    public String getIntestazione() {
        return intestazione;
    }

    public void setIntestazione(String intestazione) {
        this.intestazione = intestazione;
    }

   
    public String getDestinatario() {
        if("F".equals(flagTipoDestinatario) && null != nome && null != cognome){
        	return nome+" "+cognome;
        }else{
        	return destinatario;
        }
    	
    }


    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

   
    public String getEmail() {
        return email;
    }

   
    public void setEmail(String email) {
        this.email = email;
    }

    public int getProtocolloId() {
        return protocolloId;
    }

    public void setProtocolloId(int protocolloId) {
        this.protocolloId = protocolloId;
    }

   
    public boolean getFlagConoscenza() {
        return flagConoscenza;
    }

    public void setFlagConoscenza(boolean flagConoscenza) {
        this.flagConoscenza = flagConoscenza;
    }
    
    public boolean getFlagPresso() {
		return flagPresso;
	}

	public void setFlagPresso(boolean flagPresso) {
		this.flagPresso = flagPresso;
	}

    public String getFlagTipoDestinatario() {
        return flagTipoDestinatario;
    }

    public void setFlagTipoDestinatario(String flagTipoDestinatario) {
        this.flagTipoDestinatario = flagTipoDestinatario;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getCodicePostale() {
        return codicePostale;
    }

    public void setCodicePostale(String codicePostale) {
        this.codicePostale = codicePostale;
    }

    public String getProvinciaId() {
        return provinciaId;
    }

    public void setProvinciaId(String provinciaId) {
        this.provinciaId = provinciaId;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public int getTitoloId() {
        return titoloId;
    }

    public String getTitoloDestinatario() {
        return titoloDestinatario;
    }

    public void setTitoloDestinatario(String titoloDestinatario) {
        this.titoloDestinatario = titoloDestinatario;
    }

    public void setTitoloId(int titoloId) {
        this.titoloId = titoloId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public void setMezzoDesc(String mezzoDesc) {
        this.mezzoDesc = mezzoDesc;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public void aggiungiMezzospedizione(SpedizioneDestinatarioVO msv) {

    }

}