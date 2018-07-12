package it.finsiel.siged.mvc.vo.protocollo;

import it.compit.fenice.mvc.vo.protocollo.UfficioPartecipanteVO;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.mvc.vo.VersioneVO;
import it.finsiel.siged.util.DateUtil;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProcedimentoVO extends VersioneVO {

	private static final long serialVersionUID = 1L;

	private Date dataAvvio;
    
    private int aooId;

    private int ufficioId;

    private int statoId;
    
    private int tipoFinalitaId;
    
    private String tipoFinalita;

    private String oggetto;

    private int tipoProcedimentoId;

    private int referenteId;

    private int responsabileId;

    private String posizione;

    private Date dataEvidenza;

    private String note;

    private int protocolloId;

    private String numeroProcedimento;

    private int anno;

    private int numero;

    private String numeroProtovollo;
    
    private String tipoProcedimentoDesc;
    
    private String posizioneSelezionata;
    
    private int posizioneSelezionataId;

    //
    private int giorniMax;
    
    private int giorniAlert;

    private Map<String, DocumentoVO> riferimentiLegislativi = new HashMap<String, DocumentoVO>(2);
    
    private Collection<AssegnatarioVO> istruttori;
    
    private int fascicoloId;
    
    private String interessato;
    
    private String delegato;
    
    private String indiInteressato;
    
    private String indiDelegato;
    
    private String autoritaEmanante;
    
    private String indicazioni;
    
    private String estremiProvvedimento;
    
    private int caricLavId;
    
    private Date dataScadenza;
    
    private Date dataSospensione;
    
    private boolean sospeso;

    private String estremiSospensione;

    private boolean visualizzabile;
    
	private String[] visibilitaUfficiPartecipantiId = null;
	
	private Map<String,UfficioPartecipanteVO> ufficiPartecipanti = new HashMap<String,UfficioPartecipanteVO>(2);

    public ProcedimentoVO() {
    	
    }
    


	public void removeUfficiPartecipanti() {
		if (ufficiPartecipanti != null)
			ufficiPartecipanti.clear();
	}

	public Collection<UfficioPartecipanteVO> getUfficiPartecipantiValues() {
		return ufficiPartecipanti.values();
	}
	
	public Map<String,UfficioPartecipanteVO> getUfficiPartecipanti() {
		return ufficiPartecipanti;
	}

    public void setUfficiPartecipanti(
			Map<String, UfficioPartecipanteVO> ufficiPartecipanti) {
		this.ufficiPartecipanti = ufficiPartecipanti;
	}

	public String[] getVisibilitaUfficiPartecipantiId() {
		return visibilitaUfficiPartecipantiId;
	}

	public void setVisibilitaUfficiPartecipantiId(
			String[] visibilitaUfficiPartecipantiId) {
		this.visibilitaUfficiPartecipantiId = visibilitaUfficiPartecipantiId;
	}

	public boolean isVisualizzabile() {
		return visualizzabile;
	}

	public void setVisualizzabile(boolean visualizzabile) {
		this.visualizzabile = visualizzabile;
	}
    
    public String getEstremiSospensione() {
		return estremiSospensione;
	}

	public void setEstremiSospensione(String estremiSospensione) {
		this.estremiSospensione = estremiSospensione;
	}

	public Date getDataScadenza() {
		return dataScadenza;
	}

	public Date getDataSospensione() {
		return dataSospensione;
	}

	public boolean isSospeso() {
		return sospeso;
	}

	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public void setDataSospensione(Date dataSospensione) {
		this.dataSospensione = dataSospensione;
	}

	public void setSospeso(boolean sospeso) {
		this.sospeso = sospeso;
	}

	public int getCaricLavId() {
		return caricLavId;
	}

	public void setCaricLavId(int caricLavId) {
		this.caricLavId = caricLavId;
	}

	public void setCaricLavId(String posizione) {
		if(posizione!=null && !posizione.trim().equals("")){
		if(posizione.equals("V"))
			this.caricLavId=0;
		else if(posizione.equals("I")){
			AssegnatarioVO ass=(AssegnatarioVO) this.getIstruttori().iterator().next();
			this.caricLavId=ass.getCaricaAssegnatarioId();
		}
		else if(posizione.equals("F"))
			this.caricLavId=this.referenteId;
		else if(posizione.equals("D"))
			this.caricLavId=this.responsabileId;
		}
	}

	public String getEstremiProvvedimento() {
		return estremiProvvedimento;
	}

	
	public void setEstremiProvvedimento(String estremiProvvedimento) {
		this.estremiProvvedimento = estremiProvvedimento;
	}
    
	public String getIndicazioni() {
		return indicazioni;
	}
	
	public String getAutoritaEmanante() {
		return autoritaEmanante;
	}

	public void setAutoritaEmanante(String autoritaEmanante) {
		this.autoritaEmanante = autoritaEmanante;
	}

	public void setIndicazioni(String indicazioni) {
		this.indicazioni = indicazioni;
	}

	public Collection<AssegnatarioVO> getIstruttori() {
		return istruttori;
	}
	
	public String getInteressato() {
		return interessato;
	}

	public void setInteressato(String interessato) {
		this.interessato = interessato;
	}

	public String getDelegato() {
		return delegato;
	}

	public void setDelegato(String delegato) {
		this.delegato = delegato;
	}

	public String getIndiInteressato() {
		return indiInteressato;
	}

	public void setIndiInteressato(String indiInteressato) {
		this.indiInteressato = indiInteressato;
	}

	public String getIndiDelegato() {
		return indiDelegato;
	}

	public void setIndiDelegato(String indiDelegato) {
		this.indiDelegato = indiDelegato;
	}

	public int getFascicoloId() {
		return fascicoloId;
	}

	
	public void setFascicoloId(int fascicoloId) {
		this.fascicoloId = fascicoloId;
	}

	public void setIstruttori(Collection<AssegnatarioVO> istruttori) {
		this.istruttori = istruttori;
	}

    
    public DocumentoVO getRiferimento(Object key) {
		return (DocumentoVO) this.riferimentiLegislativi.get(key);
	}

	
	public DocumentoVO getRiferimento(int idx) {
		return (DocumentoVO) this.riferimentiLegislativi.get(String.valueOf(idx));
	}
    
	public Map<String, DocumentoVO> getRiferimentiLegislativi() {
		return riferimentiLegislativi;
	}

	public void setRiferimentiLegislativi(Map<String, DocumentoVO> riferimentiLegislativi) {
		this.riferimentiLegislativi = riferimentiLegislativi;
	}

	public int getGiorniMax() {
		return giorniMax;
	}

	public void setGiorniMax(int giorniMax) {
		this.giorniMax = giorniMax;
	}

	public int getGiorniAlert() {
		return giorniAlert;
	}

	public void setGiorniAlert(int giorniAlert) {
		this.giorniAlert = giorniAlert;
	}
	
    //
    
    public int getAooId() {
        return aooId;
    }

    public void setAooId(int aooId) {
        this.aooId = aooId;
    }

    public String getNumeroProtovollo() {
        return numeroProtovollo;
    }

    public void setNumeroProtovollo(String numeroProtovollo) {
        this.numeroProtovollo = numeroProtovollo;
    }

    public Date getDataAvvio() {
        return dataAvvio;
    }

    public String getDataAvvioView() {
    	if(dataAvvio!=null)
    		return DateUtil.formattaData(dataAvvio.getTime());
    	else
    		return "";
    }
    
    public void setDataAvvio(Date dataAvvio) {
        this.dataAvvio = dataAvvio;
    }

    public Date getDataEvidenza() {
        return dataEvidenza;
    }

    public void setDataEvidenza(Date dataEvidenza) {
        this.dataEvidenza = dataEvidenza;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public String getPosizione() {
        return posizione;
    }

    public void setPosizione(String posizione) {
        this.posizione = posizione;
    }

    public int getReferenteId() {
        return referenteId;
    }

    
    public void setReferenteId(int responsabileId) {
        this.referenteId = responsabileId;
    }
     
    
    public int getStatoId() {
        return statoId;
    }

    public String getStato() {
        if(statoId==0)
        	return "Aperto";
        if(statoId==1)
        	return "Chiuso";
        if(statoId==2)
        	return "Archiviato";
        else return "--";
        
    }
    
    public void setStatoId(int stato) {
        this.statoId = stato;
    }

    public int getTipoFinalitaId() {
        return tipoFinalitaId;
    }

    public void setTipoFinalitaId(int tipoFinalita) {
        this.tipoFinalitaId = tipoFinalita;
    }

    
    public int getUfficioId() {
        return ufficioId;
    }

    public void setUfficioId(int ufficioId) {
        this.ufficioId = ufficioId;
    }

    public int getTipoProcedimentoId() {
        return tipoProcedimentoId;
    }

    public void setTipoProcedimentoId(int tipoProcedimentoId) {
        this.tipoProcedimentoId = tipoProcedimentoId;
    }

    public int getResponsabileId() {
        return responsabileId;
    }

    public void setResponsabileId(int responsabileId) {
        this.responsabileId = responsabileId;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNumeroProcedimento() {
        return numeroProcedimento;
    }

    public void setNumeroProcedimento(String numeroProcedimento) {
        this.numeroProcedimento = numeroProcedimento;
    }

    public int getProtocolloId() {
        return protocolloId;
    }

    public void setProtocolloId(int protocolloId) {
        this.protocolloId = protocolloId;
    }
 
    public String getDescUfficio() {
        Ufficio u = Organizzazione.getInstance().getUfficio(getUfficioId());
        if (u != null && u.getValueObject() != null)
            return u.getValueObject().getDescription();
        else
            return "";
    }

    public void setTipoProcedimentoDesc(String tipoProcedimentoDesc) {
        this.tipoProcedimentoDesc = tipoProcedimentoDesc;
    }

    public String getTipoProcedimentoDesc() {
       
        if (this.tipoProcedimentoDesc != null)
            return this.tipoProcedimentoDesc;
        else
            return "-";
    }

    public String getPosizioneSelezionata() {
        return posizioneSelezionata;
    }

    public void setPosizioneSelezionata(String posizioneSelezionata) {
        this.posizioneSelezionata = posizioneSelezionata;
    }

    public int getPosizioneSelezionataId() {
        return posizioneSelezionataId;
    }

    public void setPosizioneSelezionataId(int posizioneSelezionataId) {
        this.posizioneSelezionataId = posizioneSelezionataId;
    }

    public String getTipoFinalita() {
        return tipoFinalita;
    }

    public void setTipoFinalita(String tipoFinalita) {
        this.tipoFinalita = tipoFinalita;
    }


  
}