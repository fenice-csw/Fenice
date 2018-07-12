package it.finsiel.siged.mvc.vo.protocollo;

import it.finsiel.siged.mvc.vo.VersioneVO;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;

import java.util.Date;
import java.util.List;


public class ProtocolloVO extends VersioneVO{
	
  
	private static final long serialVersionUID = 1L;

	public final static String FLAG_TIPO_PROTOCOLLO_INGRESSO = "I";

    public final static String FLAG_TIPO_PROTOCOLLO_USCITA = "U";
    
    public final static String FLAG_TIPO_POSTA_INTERNA = "P";
    
    public final static String FLAG_TIPO_REG_EM = "R";
    
    public final static String FLAG_TIPO_MITTENTE_PERS_FISICA = "F";

    public final static String FLAG_TIPO_MITTENTE_PERS_GIURIDICA = "G";
    
    public final static String FLAG_TIPO_MITTENTE_MULTI = "M";

    public final static String FLAG_STATO_SCARICO_DEFAULT = "S";

    private String destinatario;

    private String assegnatario;

    private int annoRegistrazione;

    private int numProtocollo;

    private Date dataRegistrazione;

    private String oggetto;

    private String flagTipo;

    private Date dataDocumento;

    private boolean riservato = false;
    
    private boolean fatturaElettronica = false;
    
    private boolean flagAnomalia = false;
    
    private boolean flagRepertorio = false;

    private boolean mozione;

    private Date dataScadenza;

    private Date dataEffettivaRegistrazione;

    private Date dataRicezione;

    private Date dataProtocolloMittente;

    private String nomeMittente;

    private String cognomeMittente;

    private String denominazioneMittente;

    private String flagTipoMittente;

    private String numProtocolloMittente;

    private String mittenteIndirizzo;

    private String mittenteCap;

    private String mittenteComune;

    private int mittenteProvinciaId;

    private String mittenteNazione;
    
    private List<SoggettoVO> mittenti;

    private Date dataAnnullamento;

    private Date dataScarico;

    private String statoProtocollo;

    private String notaAnnullamento;

    private String provvedimentoAnnullamento;

    private String codDocumentoDoc;

    private Integer documentoPrincipaleId = null;

    private int registroId;

    private int tipoDocumentoId;

    private int titolarioId;

    private int ufficioProtocollatoreId;

    private int caricaProtocollatoreId;
    
    private int utenteProtocollatoreId;

    private int aooId;

    private String chiaveAnnotazione;

    private String posizioneAnnotazione;

    private FascicoloVO fascicoloVO;

    private String descrizioneAnnotazione;

    private int ufficioMittenteId;

    private int utenteMittenteId;

    private int rispostaId;

    private int numProtocolloEmergenza;

    private String estermiAutorizzazione;
    
    private int giorniAlert;
    
    private String intervalloNumProtocolloEmergenza;
    
    private int emailId;
    
    private int emailUfficioId;
        
	public int getEmailUfficioId() {
		return emailUfficioId;
	}

	public void setEmailUfficioId(int emailUfficioId) {
		this.emailUfficioId = emailUfficioId;
	}

	public int getEmailId() {
		return emailId;
	}

	public void setEmailId(int numEmail) {
		this.emailId = numEmail;
	}


	public int getGiorniAlert() {
		return giorniAlert;
	}

	
	public void setGiorniAlert(int giorniAlert) {
		this.giorniAlert = giorniAlert;
	}

	public String getIntervalloNumProtocolloEmergenza() {
		return intervalloNumProtocolloEmergenza;
	}

	public void setIntervalloNumProtocolloEmergenza(
			String intervalloNumProtocolloEmergenza) {
		this.intervalloNumProtocolloEmergenza = intervalloNumProtocolloEmergenza;
	}

	public String getEstermiAutorizzazione() {
		return estermiAutorizzazione;
	}

	public void setEstermiAutorizzazione(String estermiAutorizzazione) {
		this.estermiAutorizzazione = estermiAutorizzazione;
	}

    public int getRispostaId() {
        return rispostaId;
    }

    public void setRispostaId(int rispostaId) {
        this.rispostaId = rispostaId;
    }

    public int getUfficioMittenteId() {
        return ufficioMittenteId;
    }

    public void setUfficioMittenteId(int ufficioMittenteId) {
        this.ufficioMittenteId = ufficioMittenteId;
    }

    public int getUtenteMittenteId() {
        return utenteMittenteId;
    }

    public void setUtenteMittenteId(int utenteMittenteId) {
        this.utenteMittenteId = utenteMittenteId;
    }

    public int getAnnoRegistrazione() {
        return annoRegistrazione;
    }

    public void setAnnoRegistrazione(int annoRegistrazione) {
        this.annoRegistrazione = annoRegistrazione;
    }

    public int getAooId() {
        return aooId;
    }

    public void setAooId(int aooId) {
        this.aooId = aooId;
    }

    public String getChiaveAnnotazione() {
        return chiaveAnnotazione;
    }

    public void setChiaveAnnotazione(String chiaveAnnotazione) {
        this.chiaveAnnotazione = chiaveAnnotazione;
    }

    public String getCodDocumentoDoc() {
        return codDocumentoDoc;
    }

    public void setCodDocumentoDoc(String codDocumentoDoc) {
        this.codDocumentoDoc = codDocumentoDoc;
    }

    public String getCognomeMittente() {
        return cognomeMittente;
    }

    public void setCognomeMittente(String cognomeMittente) {
        this.cognomeMittente = cognomeMittente;
    }

    public Date getDataAnnullamento() {
        return dataAnnullamento;
    }

    public void setDataAnnullamento(Date dataAnnullamento) {
        this.dataAnnullamento = dataAnnullamento;
    }

    public Date getDataDocumento() {
        return dataDocumento;
    }

    public void setDataDocumento(Date dataDocumento) {
        this.dataDocumento = dataDocumento;
    }

    public Date getDataEffettivaRegistrazione() {
        return dataEffettivaRegistrazione;
    }

    public void setDataEffettivaRegistrazione(Date dataEffettivaRegistrazione) {
        this.dataEffettivaRegistrazione = dataEffettivaRegistrazione;
    }

    public Date getDataProtocolloMittente() {
        return dataProtocolloMittente;
    }

    public void setDataProtocolloMittente(Date dataProtocolloMittente) {
        this.dataProtocolloMittente = dataProtocolloMittente;
    }

    public Date getDataRegistrazione() {
        return dataRegistrazione;
    }

    public void setDataRegistrazione(Date dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    public Date getDataRicezione() {
        return dataRicezione;
    }

    public void setDataRicezione(Date dataRicezione) {
        this.dataRicezione = dataRicezione;
    }

    public Date getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(Date dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public Date getDataScarico() {
        return dataScarico;
    }

    public void setDataScarico(Date dataScarico) {
        this.dataScarico = dataScarico;
    }

    public String getDenominazioneMittente() {
        return denominazioneMittente;
    }

    public void setDenominazioneMittente(String denominazioneMittente) {
        this.denominazioneMittente = denominazioneMittente;
    }

    public String getDescrizioneAnnotazione() {
        return descrizioneAnnotazione;
    }

    public void setDescrizioneAnnotazione(String descrizioneAnnotazione) {
        this.descrizioneAnnotazione = descrizioneAnnotazione;
    }

    public Integer getDocumentoPrincipaleId() {
        return documentoPrincipaleId;
    }

    public void setDocumentoPrincipaleId(int documentoId) {
        if (documentoId == 0) {
            setDocumentoPrincipaleId(null);
        } else {
            setDocumentoPrincipaleId(new Integer(documentoId));
        }
    }

    private void setDocumentoPrincipaleId(Integer documentoPrincipaleId) {
        this.documentoPrincipaleId = documentoPrincipaleId;
    }

    public boolean isMozione() {
        return this.mozione;
    }

    public void setMozione(boolean mozione) {
        this.mozione = mozione;
    }

    public boolean isRiservato() {
        return riservato;
    }

    public void setRiservato(boolean riservato) {
        this.riservato = riservato;
    }

    public boolean isFatturaElettronica() {
		return fatturaElettronica;
	}

	public void setFatturaElettronica(boolean fatturaElettronica) {
		this.fatturaElettronica = fatturaElettronica;
	}
	
    public boolean isFlagAnomalia() {
		return flagAnomalia;
	}

	public void setFlagAnomalia(boolean flagAnomalia) {
		this.flagAnomalia = flagAnomalia;
	}

	public boolean isFlagRepertorio() {
		return flagRepertorio;
	}

	public void setFlagRepertorio(boolean flagRepertorio) {
		this.flagRepertorio = flagRepertorio;
	}


	public String getFlagTipo() {
        return flagTipo;
    }

    public void setFlagTipo(String flagTipo) {
        this.flagTipo = flagTipo;
    }

    public String getFlagTipoMittente() {
        return flagTipoMittente;
    }

    public void setFlagTipoMittente(String flagTipoMittente) {
        this.flagTipoMittente = flagTipoMittente;
    }

    public String getMittenteCap() {
        return mittenteCap;
    }

    public void setMittenteCap(String mittenteCap) {
        this.mittenteCap = mittenteCap;
    }

    public String getMittenteComune() {
        return mittenteComune;
    }

    public void setMittenteComune(String mittenteComune) {
        this.mittenteComune = mittenteComune;
    }

    public String getMittenteIndirizzo() {
        return mittenteIndirizzo;
    }

    public void setMittenteIndirizzo(String mittenteIndirizzo) {
        this.mittenteIndirizzo = mittenteIndirizzo;
    }

    public String getMittenteNazione() {
        return mittenteNazione;
    }

    public void setMittenteNazione(String mittenteNazione) {
        this.mittenteNazione = mittenteNazione;
    }

    public int getMittenteProvinciaId() {
        return mittenteProvinciaId;
    }

    public void setMittenteProvinciaId(int mittenteProvinciaId) {
        this.mittenteProvinciaId = mittenteProvinciaId;
    }

    public String getNomeMittente() {
        return nomeMittente;
    }

    public void setNomeMittente(String nomeMittente) {
        this.nomeMittente = nomeMittente;
    }

    public String getNotaAnnullamento() {
        return notaAnnullamento;
    }

    public void setNotaAnnullamento(String notaAnnullamento) {
        this.notaAnnullamento = notaAnnullamento;
    }

    public int getNumProtocollo() {
        return numProtocollo;
    }

    public void setNumProtocollo(int numProtocollo) {
        this.numProtocollo = numProtocollo;
    }

    public String getNumProtocolloMittente() {
        return numProtocolloMittente;
    }

    public void setNumProtocolloMittente(String numProtocolloMittente) {
        this.numProtocolloMittente = numProtocolloMittente;
    }

    public int getNumProtocolloEmergenza() {
        return numProtocolloEmergenza;
    }

    public void setNumProtocolloEmergenza(int numProtocolloEmergenza) {
        this.numProtocolloEmergenza = numProtocolloEmergenza;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public String getPosizioneAnnotazione() {
        return posizioneAnnotazione;
    }

    public void setPosizioneAnnotazione(String posizioneAnnotazione) {
        this.posizioneAnnotazione = posizioneAnnotazione;
    }

    public String getProvvedimentoAnnullamento() {
        return provvedimentoAnnullamento;
    }

    public void setProvvedimentoAnnullamento(String provvedimentoAnnullamento) {
        this.provvedimentoAnnullamento = provvedimentoAnnullamento;
    }

    public int getRegistroId() {
        return registroId;
    }

    public void setRegistroId(int registroId) {
        this.registroId = registroId;
    }

    public String getStatoProtocollo() {
        if (statoProtocollo == null) {
            return FLAG_STATO_SCARICO_DEFAULT;
        }
        return statoProtocollo;
    }

    public void setStatoProtocollo(String statoTipoScarico) {
        this.statoProtocollo = statoTipoScarico;
    }

    public int getTipoDocumentoId() {
        return tipoDocumentoId;
    }

    public void setTipoDocumentoId(int tipoDocumentoId) {
        this.tipoDocumentoId = tipoDocumentoId;
    }

    public int getTitolarioId() {
        return titolarioId;
    }

    public void setTitolarioId(int titolarioId) {
        this.titolarioId = titolarioId;
    }

    public int getUfficioProtocollatoreId() {
        return ufficioProtocollatoreId;
    }

    public void setUfficioProtocollatoreId(int ufficioProtocollatoreId) {
        this.ufficioProtocollatoreId = ufficioProtocollatoreId;
    }

    public int getCaricaProtocollatoreId() {
        return caricaProtocollatoreId;
    }

    public void setCaricaProtocollatoreId(int utenteProtocollatoreId) {
        this.caricaProtocollatoreId = utenteProtocollatoreId;
    }

    public int getUtenteProtocollatoreId() {
		return utenteProtocollatoreId;
	}

	public void setUtenteProtocollatoreId(int utenteProtocollatoreId) {
		this.utenteProtocollatoreId = utenteProtocollatoreId;
	}

	public FascicoloVO getFascicoloVO() {
        return fascicoloVO;
    }

    public void setFascicoloVO(FascicoloVO fascicoloVO) {
        this.fascicoloVO = fascicoloVO;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getAssegnatario() {
        return assegnatario;
    }

    public void setAssegnatario(String assegnatario) {
        this.assegnatario = assegnatario;
    }

    public long getKey() {
        return getKey(getRegistroId(), getAnnoRegistrazione(),
                getNumProtocollo());
    }

    public static long getKey(int registro, int anno, int numero) {
        return registro * 100000000000l + anno * 10000000l + numero;
    }

    
	public void setMittenti(List<SoggettoVO> mittenti) {
		this.mittenti = mittenti;
	}
	
	public List<SoggettoVO> getMittenti(){
		return this.mittenti;
	}
	
}