/* Generated by Together */

package it.finsiel.siged.mvc.presentation.helper;

import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.mvc.bo.TitolarioBO;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.business.UtenteDelegate;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.util.DateUtil;

public class FascicoloView {

    public FascicoloView() {
    }

    private int id;

    private String nome;

    private String stato;

    private String oggetto;

    private String note;

    private String dataApertura;
    
    private String dataChiusura;

    private int fascicoloId;

    private int ufficioIntestatarioId;

    private int utenteIntestatarioId;

    private int ufficioResponsabileId;
    
    private int utenteResponsabileId;
    
    private int caricaIntestatarioId;

    private int caricaResponsabileId;

    private String dataEvidenza;

    private String descUfficioIntestatarioId;

    private int titolarioId;

    private int progressivo;
    
    private String pathProgressivo;

    private int annoRiferimento;

    private int versione;

    private String dataCarico;

    private String dataScarico;
    
    private String dataUltimoMovimento;

    private int giorniRimanenti;
    
    
    
    
   
	public String getPathProgressivo() {
		return pathProgressivo;
	}


	public void setPathProgressivo(String pathProgressivo) {
		this.pathProgressivo = pathProgressivo;
	}


	public String getDataChiusura() {
		return dataChiusura;
	}

	
	public void setDataChiusura(String dataChiusura) {
		this.dataChiusura = dataChiusura;
	}

	public int getCaricaIntestatarioId() {
		return caricaIntestatarioId;
	}

	public void setCaricaIntestatarioId(int caricaIntestatarioId) {
		this.caricaIntestatarioId = caricaIntestatarioId;
	}

	public int getCaricaResponsabileId() {
		return caricaResponsabileId;
	}

	public void setCaricaResponsabileId(int caricaResponsabileId) {
		this.caricaResponsabileId = caricaResponsabileId;
	}

	public int getGiorniRimanenti() {
		return giorniRimanenti;
	}

	public void setGiorniRimanenti(int giorniRimanenti) {
		this.giorniRimanenti = giorniRimanenti;
	}

	public String getDataUltimoMovimento() {
		return dataUltimoMovimento;
	}

	public void setDataUltimoMovimento(String dataUltimoMovimento) {
		this.dataUltimoMovimento = dataUltimoMovimento;
	}

	public String getDescrizioneStato() {
        return LookupDelegate.getStatoFascicolo(new Integer(this.stato)).getDescription();
    }

    public String getDescrizioneUfficioIntestatario() {
    	 if (getUfficioIntestatarioId() > 0) {
    	Organizzazione org = Organizzazione.getInstance();
        return org.getUfficio(getUfficioIntestatarioId()).getPath();
    	 }else {
             return "";
         }
    }

    public String getDescrizioneUtenteIntestatario() {
        if (getUtenteIntestatarioId() > 0) {
            Organizzazione org = Organizzazione.getInstance();
            return org.getUtente(getUtenteIntestatarioId()).getValueObject()
                    .getFullName();
        } else {
            return "";
        }

    }

    public String getDescUfficioIntestatarioId() {
        return descUfficioIntestatarioId;
    }

    public void setDescUfficioIntestatarioId(String descUfficioIntestatarioId) {
        this.descUfficioIntestatarioId = descUfficioIntestatarioId;
    }

    public String getPathTitolario() {
        return TitolarioBO.getPathDescrizioneTitolario(getTitolarioId());
    }
     
    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getDataApertura() {
        return dataApertura;
    }

    public void setDataApertura(String dataApertura) {
        this.dataApertura = dataApertura;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDataEvidenza() {
        return dataEvidenza;
    }

    public void setDataEvidenza(String dataEvidenza) {
        this.dataEvidenza = dataEvidenza;
    }

    public int getFascicoloId() {
        return fascicoloId;
    }

    public void setFascicoloId(int fascicoloId) {
        this.fascicoloId = fascicoloId;
    }

    public int getUfficioIntestatarioId() {
        return ufficioIntestatarioId;
    }

    public void setUfficioIntestatarioId(int ufficioIntestatarioId) {
        this.ufficioIntestatarioId = ufficioIntestatarioId;
    }

    public int getTitolarioId() {
        return titolarioId;
    }

    public void setTitolarioId(int titolarioId) {
        this.titolarioId = titolarioId;
    }

    public int getAnnoRiferimento() {
        return annoRiferimento;
    }

    public void setAnnoRiferimento(int annoRiferimento) {
        this.annoRiferimento = annoRiferimento;
    }

    public int getProgressivo() {
        return progressivo;
    }

    public void setProgressivo(int progressivo) {
        this.progressivo = progressivo;
    }

    public String getAnnoProgressivo() {
        if (getDataApertura() != null && getProgressivo() > 0) {
        	String path=TitolarioBO.getPath(getTitolarioId());
        	return path+"/"+pathProgressivo + "-"+DateUtil.getYear(DateUtil.toDate(dataApertura));
        }
        return null;
    }

    public int getVersione() {
        return versione;
    }

    public void setVersione(int versione) {
        this.versione = versione;
    }

    public String getDataCarico() {
        return dataCarico;
    }

    public void setDataCarico(String dataCarico) {
        this.dataCarico = dataCarico;
    }

    public String getDataScarico() {
        return dataScarico;
    }

    public void setDataScarico(String dataScarico) {
        this.dataScarico = dataScarico;
    }

    public int getUtenteIntestatarioId() {
        return utenteIntestatarioId;
    }

    public void setUtenteIntestatarioId(int utenteIntestatarioId) {
        this.utenteIntestatarioId = utenteIntestatarioId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setUfficioResponsabileId(int ufficioResponsabileId) {
        this.ufficioResponsabileId = ufficioResponsabileId;
    }

    public void setUtenteResponsabileId(int utenteResponsabileId) {
        this.utenteResponsabileId = utenteResponsabileId;
    }

    public int getUfficioResponsabileId() {
        return ufficioResponsabileId;
    }

    public int getUtenteResponsabileId() {
        return utenteResponsabileId;
    }

    public String getDescrizioneUfficioResponsabile() {
    	if(getUfficioResponsabileId()>0){
        Organizzazione org = Organizzazione.getInstance();
        return org.getUfficio(getUfficioResponsabileId()).getPath();
    	}else
    		return "";
    }

    public String getDescrizioneUtenteResponsabile() {
        if (getUtenteResponsabileId() > 0) {
            Organizzazione org = Organizzazione.getInstance();
            return org.getUtente(getUtenteResponsabileId()).getValueObject()
                    .getFullName();
        } else {
            return "";
        }
    }

    public String getDescrizioneReferente(){
    	if(getCaricaIntestatarioId()!=0){
    		CaricaVO carica=CaricaDelegate.getInstance().getCarica(getCaricaIntestatarioId());
    		if(carica!=null){
    			if(carica.getUtenteId()!=0){
    				UtenteVO utente=UtenteDelegate.getInstance().getUtente(carica.getUtenteId());
    				if(utente!=null)
    					return utente.getCognomeNome()+" ("+carica.getNome()+")";
    				else
    					return carica.getNome();
    			}else
    				return carica.getNome();
    		}else
    			return "";
    	}else
    		return "";
    }
    
}