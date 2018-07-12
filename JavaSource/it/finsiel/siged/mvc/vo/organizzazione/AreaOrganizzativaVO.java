package it.finsiel.siged.mvc.vo.organizzazione;

import it.finsiel.siged.mvc.vo.IdentityVO;

import java.sql.Time;
import java.util.Date;

public class AreaOrganizzativaVO extends IdentityVO {

	private static final long serialVersionUID = 1L;
	
	public static final int PUBBLICA_ORIGINALI=0;
	
	public static final int PUBBLICA_COPIA=1;
	
	public static final int PUBBLICA_ENTRAMBI=2;

	private String codi_aoo;

    private Date data_istituzione;

    private String responsabile_nome;

    private String responsabile_cognome;

    private String responsabile_email;

    private String responsabile_telefono;

    private Date data_soppressione;

    private String telefono;

    private String fax;

    private String indi_dug;

    private String indi_toponimo;

    private String indi_civico;

    private String indi_cap;

    private String indi_comune;

    private int provincia_id;

    private String email;

    private String dipartimento_codice;

    private String dipartimento_descrizione;

    private String tipo_aoo;

    private String codi_documento_doc;

    private char flag_pdf;

    private int amministrazione_id;

    private boolean documentoReadable = false;
    
    private boolean ricercaUfficiFull = false;
    
    private String idCommittenteFattura;
    
    private String gaUsername;

    private String gaPwd;

    private boolean gaAbilitata;
	
    private boolean gaFlagInvio;

    private Time gaTimer;
    
    private String anniVisibilitaBacheca;
    
    private String maxRighe;
    
    private int flagPubblicazioneP7m;
    
    public int getFlagPubblicazioneP7m() {
		return flagPubblicazioneP7m;
	}

	public void setFlagPubblicazioneP7m(int flagPubblicazioneP7m) {
		this.flagPubblicazioneP7m = flagPubblicazioneP7m;
	}
    
    public String getAnniVisibilitaBacheca() {
		return anniVisibilitaBacheca;
	}

	public void setAnniVisibilitaBacheca(String anniVisibilitaBacheca) {
		this.anniVisibilitaBacheca = anniVisibilitaBacheca;
	}
	
	public String getMaxRighe() {
		return maxRighe;
	}

	public void setMaxRighe(String maxRighe) {
		this.maxRighe = maxRighe;
	}

	public boolean isGaFlagInvio() {
		return gaFlagInvio;
	}

	public void setGaFlagInvio(boolean gaFlagInvio) {
		this.gaFlagInvio = gaFlagInvio;
	}

	public Time getGaTimer() {
		return gaTimer;
	}

	public void setGaTimer(Time gaTimer) {
		this.gaTimer = gaTimer;
	}

	public String getGaUsername() {
		return gaUsername;
	}

	public void setGaUsername(String gaUsername) {
		this.gaUsername = gaUsername;
	}

	public String getGaPwd() {
		return gaPwd;
	}

	public void setGaPwd(String gaPwd) {
		this.gaPwd = gaPwd;
	}

	public boolean isGaAbilitata() {
		return gaAbilitata;
	}

	public void setGaAbilitata(boolean gaAbilitata) {
		this.gaAbilitata = gaAbilitata;
	}

	public String getIdCommittenteFattura() {
		return idCommittenteFattura;
	}

	public void setIdCommittenteFattura(String idCommittenteFattura) {
		this.idCommittenteFattura = idCommittenteFattura;
	}
	public boolean isRicercaUfficiFull() {
		return ricercaUfficiFull;
	}

	public void setRicercaUfficiFull(boolean ricercaUfficiFull) {
		this.ricercaUfficiFull = ricercaUfficiFull;
	}

	public boolean isDocumentoReadable() {
		return documentoReadable;
	}

	public void setDocumentoReadable(boolean documentoReadable) {
		this.documentoReadable = documentoReadable;
	}

	public String getIndirizzoNumCivico() {
    	if (getIndi_toponimo() == null || getIndi_toponimo().trim().equals(""))
			return "";
		return getIndi_dug()
				+ ((getIndi_toponimo() == null || getIndi_toponimo().trim().equals("") ) ? 
						"":" "+ getIndi_toponimo() ) + ((getIndi_civico() == null || getIndi_civico().trim().equals("") ) ? 
								"":", "+ getIndi_civico() );
	}
    
    public String getIndirizzoCompleto() {
		if (getIndi_comune() == null || getIndi_comune().trim().equals(""))
			return "";
		
		return ((getIndirizzoNumCivico() == null || getIndirizzoNumCivico().trim().equals("") ) ? 
				"": getIndirizzoNumCivico() )+((getIndi_cap() == null || getIndi_cap().trim().equals("") ) ? 
						"":" - "+ getIndi_cap())+" "+getIndi_comune();
	}
    
    private int dipendenzaTitolarioUfficio = 1;

    private int titolarioLivelloMinimo = 2;

   
    public int getProvincia_id() {
        return provincia_id;
    }

   
    public void setProvincia_id(int provincia_id) {
        this.provincia_id = provincia_id;
    }

   
    public String getCodi_aoo() {
        return codi_aoo;
    }


    public void setCodi_aoo(String codi_aoo) {
        this.codi_aoo = codi_aoo;
    }

 
    public String getCodi_documento_doc() {
        return codi_documento_doc;
    }


    public void setCodi_documento_doc(String codi_documento_doc) {
        this.codi_documento_doc = codi_documento_doc;
    }


    public char getFlag_pdf() {
        return flag_pdf;
    }


    public void setFlag_pdf(char flag_pdf) {
        this.flag_pdf = flag_pdf;
    }


    public Date getData_istituzione() {
        return data_istituzione;
    }

 
    public void setData_istituzione(Date data_istituzione) {
        this.data_istituzione = data_istituzione;
    }


    public Date getData_soppressione() {
        return data_soppressione;
    }

 
    public void setData_soppressione(Date data_soppressione) {
        this.data_soppressione = data_soppressione;
    }

  
    public String getDipartimento_codice() {
        return dipartimento_codice;
    }


    public void setDipartimento_codice(String dipartimento_codice) {
        this.dipartimento_codice = dipartimento_codice;
    }

 
    public String getDipartimento_descrizione() {
        return dipartimento_descrizione;
    }


    public void setDipartimento_descrizione(String dipartimento_descrizione) {
        this.dipartimento_descrizione = dipartimento_descrizione;
    }


    public String getEmail() {
        return email;
    }

 
    public void setEmail(String email) {
        this.email = email;
    }

 
    public String getFax() {
        return fax;
    }

  
    public void setFax(String fax) {
        this.fax = fax;
    }


    public String getIndi_cap() {
        return indi_cap;
    }


    public void setIndi_cap(String indi_cap) {
        this.indi_cap = indi_cap;
    }


    public String getIndi_civico() {
        return indi_civico;
    }


    public void setIndi_civico(String indi_civico) {
        this.indi_civico = indi_civico;
    }


    public String getIndi_comune() {
        return indi_comune;
    }


    public void setIndi_comune(String indi_comune) {
        this.indi_comune = indi_comune;
    }


    public String getIndi_dug() {
        return indi_dug;
    }


    public void setIndi_dug(String indi_dug) {
        this.indi_dug = indi_dug;
    }


    public String getIndi_toponimo() {
        return indi_toponimo;
    }


    public void setIndi_toponimo(String indi_toponimo) {
        this.indi_toponimo = indi_toponimo;
    }


    public String getResponsabile_cognome() {
        return responsabile_cognome;
    }


    public void setResponsabile_cognome(String responsabile_cognome) {
        this.responsabile_cognome = responsabile_cognome;
    }


    public String getResponsabile_email() {
        return responsabile_email;
    }

    /**
     * @param responsabile_email
     *            The responsabile_email to set.
     */
    public void setResponsabile_email(String responsabile_email) {
        this.responsabile_email = responsabile_email;
    }

    /**
     * @return Returns the responsabile_nome.
     */
    public String getResponsabile_nome() {
        return responsabile_nome;
    }

    /**
     * @param responsabile_nome
     *            The responsabile_nome to set.
     */
    public void setResponsabile_nome(String responsabile_nome) {
        this.responsabile_nome = responsabile_nome;
    }

    /**
     * @return Returns the responsabile_telefono.
     */
    public String getResponsabile_telefono() {
        return responsabile_telefono;
    }

    /**
     * @param responsabile_telefono
     *            The responsabile_telefono to set.
     */
    public void setResponsabile_telefono(String responsabile_telefono) {
        this.responsabile_telefono = responsabile_telefono;
    }

    /**
     * @return Returns the telefono.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param telefono
     *            The telefono to set.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return Returns the tipo_aoo.
     */
    public String getTipo_aoo() {
        return tipo_aoo;
    }

    /**
     * @param tipo_aoo
     *            The tipo_aoo to set.
     */
    public void setTipo_aoo(String tipo_aoo) {
        this.tipo_aoo = tipo_aoo;
    }

    /**
     * @return Returns the amministrazione_id.
     */
    public int getAmministrazione_id() {
        return amministrazione_id;
    }

    /**
     * @param amministrazione_id
     *            The amministrazione_id to set.
     */
    public void setAmministrazione_id(int amministrazione_id) {
        this.amministrazione_id = amministrazione_id;
    }
    
    public int getDipendenzaTitolarioUfficio() {
        return dipendenzaTitolarioUfficio;
    }

    public void setDipendenzaTitolarioUfficio(int dipendenzaTitolarioUfficio) {
        this.dipendenzaTitolarioUfficio = dipendenzaTitolarioUfficio;
    }

    public int getTitolarioLivelloMinimo() {
        return titolarioLivelloMinimo;
    }

    public void setTitolarioLivelloMinimo(int titolarioLivelloMinimo) {
        this.titolarioLivelloMinimo = titolarioLivelloMinimo;
    }

}