/* Generated by Together */

package it.finsiel.siged.mvc.vo.lookup;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.finsiel.siged.mvc.bo.TitolarioBO;
import it.finsiel.siged.mvc.vo.VersioneVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;

public class TitolarioVO extends VersioneVO {
    private String codice;

    private String descrizione;

    private String path;

    private int aooId;

    private int parentId;

    private int massimario;
    
    private int giorniMax;
    
    private int giorniAlert;

    private String fullPathDescription;

    private AssegnatarioVO responsabile;
    
    private int responsabileUfficioId;
    
    private int responsabileId;
    
    private Map amministrazioniPartecipanti = new HashMap(); // di tipo DestinatarioVO

    private Map riferimentiLegislativi = new HashMap(2);
    
    private String nomeResponsabile;
    
    public DocumentoVO getRiferimento(Object key) {
		return (DocumentoVO) this.riferimentiLegislativi.get(key);
	}

	public DocumentoVO getRiferimento(int idx) {
		return (DocumentoVO) this.riferimentiLegislativi.get(String.valueOf(idx));
	}
    
    public int getResponsabileUfficioId() {
		return responsabileUfficioId;
	}

	public void setResponsabileUfficioId(int responsabileUfficioId) {
		this.responsabileUfficioId = responsabileUfficioId;
	}

	public String getNomeResponsabile() {
		return nomeResponsabile;
	}

	public void setNomeResponsabile(String nomeResponsabile) {
		this.nomeResponsabile = nomeResponsabile;
	}

	public Map getRiferimentiLegislativi() {
		return riferimentiLegislativi;
	}

    public Collection getRiferimentiCollection() {
        return riferimentiLegislativi.values();
    }

	public void setRiferimentiLegislativi(Map riferimentiLegislativi) {
		this.riferimentiLegislativi = riferimentiLegislativi;
	}

	public int getResponsabileId() {
		return responsabileId;
	}

	public void setResponsabileId(int responsabileId) {
		this.responsabileId = responsabileId;
	}

	public Collection getAmministrazioni() {
        return amministrazioniPartecipanti.values();
    }

    public void addAmministrazioni(AmministrazionePartecipanteVO amministrazione) {
        if (amministrazione != null) 
        	amministrazioniPartecipanti.put(amministrazione.getIdx(), amministrazione);
    }

    public void removeAmministrazione(Integer amministrazioneId) {
    	amministrazioniPartecipanti.remove(amministrazioneId);
    }

    public void removeAmministrazione(int amministrazioneId) {
    	removeAmministrazione(new Integer(amministrazioneId));
    }

    public void removeAmministrazione(DestinatarioVO amministrazione) {
        if (amministrazione != null) {
        	removeAmministrazione(amministrazione.getId());
        }
    }

    public void removeAmministrazioni() {
        if (amministrazioniPartecipanti != null) {
        	amministrazioniPartecipanti.clear();
        }
    }

    /**
     * @param destinatari
     *            The destinatari to set.
     */
    public void setAmministrazioni(Map amministrazioniPartecipanti) {
        this.amministrazioniPartecipanti = amministrazioniPartecipanti;
    }

    public DestinatarioVO getAmministrazione(Integer amministrazioneId) {
        return (DestinatarioVO) amministrazioniPartecipanti.get(amministrazioneId);
    }

    public DestinatarioVO getAmministrazione(int amministrazioneId) {
        return getAmministrazione(new Integer(amministrazioneId));
    }

    /**
     * @return Returns the mittente.
     */
    public AssegnatarioVO getResponsabile() {
        return responsabile;
    }

    /**
     * @param mittente
     *            The mittente to set.
     */
    public void setResponsabile(AssegnatarioVO mittente) {
        this.responsabile = mittente;
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

	public int getAooId() {
        return aooId;
    }

    public void setAooId(int aooId) {
        this.aooId = aooId;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getMassimario() {
        return massimario;
    }

    public void setMassimario(int massimario) {
        this.massimario = massimario;
    }

    public String getFullPathDescription() {
        return fullPathDescription;
    }

    public void setFullPathDescription(String fullPathDescription) {
        this.fullPathDescription = fullPathDescription;
    }
    
    

}