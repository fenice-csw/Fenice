package it.finsiel.siged.mvc.vo;

import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;

import java.util.ArrayList;
import java.util.Collection;

public class ListaDistribuzioneVO extends VersioneVO {
  
	private static final long serialVersionUID = 1L;

	private int aooId;

    private String description;
    
    private Collection<AssegnatarioVO> assegnatari;

    public ListaDistribuzioneVO() {
    	this.assegnatari=new ArrayList<AssegnatarioVO>();
    }
    
    public ListaDistribuzioneVO(int aooId) {
        this.aooId = aooId;
        this.assegnatari=new ArrayList<AssegnatarioVO>();
    }
    
	public Collection<AssegnatarioVO> getAssegnatari() {
		return assegnatari;
	}

	public void setAssegnatari(Collection<AssegnatarioVO> assegnatari) {
		this.assegnatari = assegnatari;
	}

	public void aggiungiAssegnatario(AssegnatarioVO assegnatario) {
        if (assegnatario != null) {
        	assegnatari.add(assegnatario);
        }
    }

	 public void removeCaricheAssegnatari() {
		 assegnatari.clear();
	    }
	
	

    public int getAooId() {
        return aooId;
    }

    public void setAooId(int aooId) {
        this.aooId = aooId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}