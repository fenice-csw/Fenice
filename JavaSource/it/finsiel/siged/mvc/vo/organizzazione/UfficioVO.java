package it.finsiel.siged.mvc.vo.organizzazione;

import it.finsiel.siged.mvc.vo.IdentityVO;

import java.util.Date;

public class UfficioVO extends IdentityVO {


	private static final long serialVersionUID = 1L;

	public static final String UFFICIO_NORMALE = "N";

    public static final String LABEL_UFFICIO_NORMALE = "Ufficio normale";

    public static final String UFFICIO_CENTRALE = "C";

    public static final String LABEL_UFFICIO_CENTRALE = "Ufficio centrale";

    public static final String UFFICIO_SEMICENTRALE = "S";

    public static final String LABEL_UFFICIO_SEMICENTRALE = "Ufficio semi-centrale";

    private String tipo;

    private boolean attivo;
    
    private boolean ufficioProtocollo;

    private boolean accettazioneAutomatica;

    private int aooId;

    private int parentId;

    private int flagAccetazioneAutomatica;
    
    private String numeroProtocolliAssegnati;
    
    private boolean flagInOggetto;

    private String piano;
    
    private String stanza;
    
    private String telefono;
    
    private String fax;
    
    private String email;
    
    private String emailUsername;
    
    private String emailPassword;
    
    private Date dataUltimaMailRicevuta;
    
    private int caricaDirigenteId;
    
    private int caricaResponsabileUfficioProtocolloId;
        
   
	public boolean isUfficioProtocollo() {
		return ufficioProtocollo;
	}

	public void setUfficioProtocollo(boolean ufficioProtocollo) {
		this.ufficioProtocollo = ufficioProtocollo;
	}

	public Date getDataUltimaMailRicevuta() {
		return dataUltimaMailRicevuta;
	}

	public void setDataUltimaMailRicevuta(Date dataUltimaMailRicevuta) {
		this.dataUltimaMailRicevuta = dataUltimaMailRicevuta;
	}

	public String getEmailUsername() {
		return emailUsername;
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailUsername(String emailUsername) {
		this.emailUsername = emailUsername;
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	public int getCaricaDirigenteId() {
		return caricaDirigenteId;
	}

	public void setCaricaDirigenteId(int caricaDirigenteId) {
		this.caricaDirigenteId = caricaDirigenteId;
	}

	public String getTeleFax(){
		return (getTelefono() == null
				|| getTelefono().trim().equals("") ? "" :"Tel: "+getTelefono())
			+ (getFax() == null || getFax().trim().equals("") ? ""
					: " Fax: " + getFax());
	}
    
	public String getNomeUfficio() {
			return (getDescription().length()>60 ? getDescription().substring(0, 60) : getDescription());
			
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
    
	public String getPiano() {
		return piano;
	}

	public String getStanza() {
		return stanza;
	}

	public String getTelefono() {
		return telefono;
	}

	public String getFax() {
		return fax;
	}

	public void setPiano(String piano) {
		this.piano = piano;
	}

	public void setStanza(String stanza) {
		this.stanza = stanza;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

    
	public boolean getFlagInOggetto() {
		return flagInOggetto;
	}

	public void setFlagInOggetto(boolean flagInOggetto) {
		this.flagInOggetto = flagInOggetto;
	}

	public String getNumeroProtocolliAssegnati() {
		return numeroProtocolliAssegnati;
	}

	public void setNumeroProtocolliAssegnati(String numeroProtocolliAssegnati) {
		this.numeroProtocolliAssegnati = numeroProtocolliAssegnati;
	}

	public void setNumeroProtocolliAssegnati(int numeroProtocolli,int numeroAssegnatari) {
		this.numeroProtocolliAssegnati = ""+numeroProtocolli+" protocolli,"+numeroAssegnatari+" assegnatari";
	}
	
	public int getFlagAccetazioneAutomatica() {
        return flagAccetazioneAutomatica;
    }

    public void setFlagAccetazioneAutomatica(int flagAccetazioneAutomatica) {
        this.flagAccetazioneAutomatica = flagAccetazioneAutomatica;
    }


    public boolean isAccettazioneAutomatica() {
        return accettazioneAutomatica;
    }

    public void setAccettazioneAutomatica(boolean accettazioneAutomatica) {
        this.accettazioneAutomatica = accettazioneAutomatica;
    }

    public boolean isAttivo() {
        return attivo;
    }

    public void setAttivo(boolean attivo) {
        this.attivo = attivo;
    }

    public int getAooId() {
        return aooId;
    }

    public void setAooId(int aooId) {
        this.aooId = aooId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int menuId) {
        this.parentId = menuId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

	public int getCaricaResponsabileUfficioProtocolloId() {
		return caricaResponsabileUfficioProtocolloId;
	}

	public void setCaricaResponsabileUfficioProtocolloId(
			int caricaResponsabileUfficioProtocolloId) {
		this.caricaResponsabileUfficioProtocolloId = caricaResponsabileUfficioProtocolloId;
	}

}