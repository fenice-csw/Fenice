
package it.finsiel.siged.mvc.vo.lookup;

import it.compit.fenice.mvc.vo.protocollo.UfficioPartecipanteVO;
import it.finsiel.siged.mvc.business.TitolarioDelegate;
import it.finsiel.siged.mvc.vo.VersioneVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TipoProcedimentoVO extends VersioneVO {
    
	private static final long serialVersionUID = 1L;

    private ArrayList<TipoProcedimentoVO> tipiProcedimento = new ArrayList<TipoProcedimentoVO>();
    private int aooId;
    private int idTipo;
    private String descrizione;
    private int giorniMax;
    private int giorniAlert;
    private int titolarioId;    
    private Map<Integer,AmministrazionePartecipanteVO> amministrazioniPartecipanti = new HashMap<Integer,AmministrazionePartecipanteVO>();
    private Map<String, DocumentoVO> riferimentiLegislativi = new HashMap<String, DocumentoVO>(2); 
	private List<UfficioPartecipanteVO> ufficiPartecipanti = new ArrayList<UfficioPartecipanteVO>();
    private boolean ull;
	
	public TipoProcedimentoVO() {
    	idTipo=0;
    }

    public TipoProcedimentoVO (int idTipo, int idUfficio, String descrizione) {
        setIdTipo(idTipo);
        this.descrizione = descrizione;
    }

    public TipoProcedimentoVO (int idTipo, String[] idUfficio, String descrizione) {
        setIdTipo(idTipo);
        this.descrizione = descrizione;
    }
    
	public boolean isUll() {
		return ull;
	}

	
	public void setUll(boolean ull) {
		this.ull = ull;
	}

	public int getTitolarioId() {
		return titolarioId;
	}

	public void setTitolarioId(int titolarioId) {
		this.titolarioId = titolarioId;
	}

	public String getTitolario(){
		if(titolarioId!=0)
		return TitolarioDelegate.getInstance().getTitolario(titolarioId).getDescrizione();
		else 
			return null;
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

    public Collection<DocumentoVO> getRiferimentiCollection() {
        return riferimentiLegislativi.values();
    }

	public void setRiferimentiLegislativi(Map<String, DocumentoVO> riferimentiLegislativi) {
		this.riferimentiLegislativi = riferimentiLegislativi;
	}

	public Collection<AmministrazionePartecipanteVO> getAmministrazioni() {
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
	
    public void setAmministrazioni(Map<Integer,AmministrazionePartecipanteVO> amministrazioniPartecipanti) {
        this.amministrazioniPartecipanti = amministrazioniPartecipanti;
    }

    public AmministrazionePartecipanteVO getAmministrazione(Integer amministrazioneId) {
        return amministrazioniPartecipanti.get(amministrazioneId);
    }

    public AmministrazionePartecipanteVO getAmministrazione(int amministrazioneId) {
        return getAmministrazione(amministrazioneId);
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

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

   

    public void setTipiProcedimento(ArrayList<TipoProcedimentoVO> tipiProcedimento) {
        this.tipiProcedimento = tipiProcedimento;
    }

    public TipoProcedimentoVO[] getTipiProcedimento() {
        TipoProcedimentoVO[] tipiProc = new TipoProcedimentoVO[tipiProcedimento.size()];
        tipiProcedimento.toArray(tipiProc);
        return tipiProc;
    }

    public void addTipoProcedimento(TipoProcedimentoVO tipoProc) {
        tipiProcedimento.add(tipoProc);
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public List<UfficioPartecipanteVO> getUfficiPartecipanti() {
        return ufficiPartecipanti;
    }

	public void setUfficiPartecipanti(List<UfficioPartecipanteVO> ufficipartecipanti) {
		this.ufficiPartecipanti = ufficipartecipanti;
	}
	
    public void removeUfficiPartecipanti() {
        if (ufficiPartecipanti != null) {
        	ufficiPartecipanti.clear();
        }
    }

    public void addUfficiPartecipante(UfficioPartecipanteVO ufficioPartecipante) {
        if (ufficioPartecipante != null) 
        	ufficiPartecipanti.add(ufficioPartecipante);
    }
    

}