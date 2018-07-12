
package it.finsiel.siged.model.protocollo;

import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public abstract class Protocollo {
	
	private String estremiAutorizzazione;
	
	private boolean visionato;
	
	private boolean lavorato;

	public boolean isLavorato() {
		return lavorato;
	}

	public void setLavorato(boolean lavorato) {
		this.lavorato = lavorato;
	}

	public boolean isVisionato() {
		return visionato;
	}

	public void setVisionato(boolean visionato) {
		this.visionato = visionato;
	}

	public String getEstremiAutorizzazione() {
		return estremiAutorizzazione;
	}

	public void setEstremiAutorizzazione(String estremiAutorizzazione) {
		this.estremiAutorizzazione = estremiAutorizzazione;
	}

	
	private boolean addOggetto;
	private boolean addFisica;
	private boolean addGiuridica;
    /**
	 * @return the addGiuridica
	 */
	public boolean isAddGiuridica() {
		return addGiuridica;
	}

	/**
	 * @param addGiuridica the addGiuridica to set
	 */
	public void setAddGiuridica(boolean addGiuridica) {
		this.addGiuridica = addGiuridica;
	}

	/**
	 * @return the addOggetto
	 */
	public boolean isAddOggetto() {
		return addOggetto;
	}

	/**
	 * @param addOggetto the addOggetto to set
	 */
	public void setAddOggetto(boolean addOggetto) {
		this.addOggetto = addOggetto;
	}
	
	

	/**
	 * @return the addFisica
	 */
	public boolean isAddFisica() {
		return addFisica;
	}

	/**
	 * @param addFisica the addFisica to set
	 */
	public void setAddFisica(boolean addFisica) {
		this.addFisica = addFisica;
	}

	private ProtocolloVO protocollo = new ProtocolloVO();

    private Collection<AllaccioVO> allacci = new ArrayList<AllaccioVO>(); 

    private Collection<ProtocolloProcedimentoVO> procedimenti = new ArrayList<ProtocolloProcedimentoVO>(); 
    
    private Map<String,DocumentoVO> allegati = new HashMap<String,DocumentoVO> (2);

    private Map<Integer,DocumentoVO>  documentiRimossi = new HashMap<Integer,DocumentoVO> (2); // 

    private DocumentoVO documentoPrincipale = new DocumentoVO();
    
    private Collection<Integer> fascicoliEliminatiId = new ArrayList<Integer>();

    private Collection<Integer> procedimentiEliminatiId = new ArrayList<Integer>();
    
    private Collection<FascicoloVO> fascicoli = new ArrayList<FascicoloVO>();
    
    public ProtocolloVO getProtocollo() {
        return protocollo;
    }

    public void setProtocollo(ProtocolloVO protocollo) {
        this.protocollo = protocollo;
    }

    public Collection<AllaccioVO> getAllacci() {
        return new ArrayList<AllaccioVO>(allacci);
    }

    public void allacciaProtocollo(AllaccioVO allaccio) {
        if (allaccio != null) {
            allacci.add(allaccio);
        }
    }

	public Collection<FascicoloVO> getFascicoli() {
		return fascicoli;
	}

	public void setFascicoli(Collection<FascicoloVO> fascicoli) {
		this.fascicoli = fascicoli;
	}
	
    public void aggiungiFascicolo(FascicoloVO f) {
        if (fascicoli == null) {
        	fascicoli=new ArrayList<FascicoloVO>();
        }
    	fascicoli.add(f);
    }

	public Collection<Integer> getFascicoliEliminatiId() {
		return fascicoliEliminatiId;
	}

	
	public void setFascicoliEliminatiId(Collection<Integer> fascicoliEliminatiId) {
		this.fascicoliEliminatiId = fascicoliEliminatiId;
	}

	public Collection<Integer> getProcedimentiEliminatiId() {
		return procedimentiEliminatiId;
	}

	
	public void setProcedimentiEliminatiId(Collection<Integer> procedimentiEliminatiId) {
		this.procedimentiEliminatiId = procedimentiEliminatiId;
	}
	
	public void setAllacci(Collection<AllaccioVO> allacci) {
        this.allacci.clear();
        this.allacci.addAll(allacci);
    }

	
	
    public Map<String,DocumentoVO> getAllegati() {
        return allegati;
    }

    public void allegaDocumento(DocumentoVO documento) {
        if (documento != null) {
            ProtocolloBO.putAllegato(documento, allegati);
        }
    }

    public void removeDocumento(Object key) {
        if (allegati.containsKey(key))
            allegati.remove(key);
    }

    public void setAllegati(Map<String,DocumentoVO> a) {
        this.allegati = a;
    }

    public Collection<DocumentoVO> getAllegatiCollection() {
        return allegati.values();
    }

    
    public DocumentoVO getDocumentoPrincipale() {
        return documentoPrincipale;
    }

    public void setDocumentoPrincipale(DocumentoVO documentoPrincipale) {
        this.documentoPrincipale = documentoPrincipale;
    }

    public Map<Integer,DocumentoVO> getDocumentiRimossi() {
        return documentiRimossi;
    }

    public void setDocumentiRimossi(Map<Integer,DocumentoVO> documentiRimossi) {
        this.documentiRimossi = documentiRimossi;
    }

    public Collection<ProtocolloProcedimentoVO> getProcedimenti() {
        return procedimenti;
    }

    public void setProcedimenti(Collection<ProtocolloProcedimentoVO> procedimenti) {
        this.procedimenti = procedimenti;
    }
}