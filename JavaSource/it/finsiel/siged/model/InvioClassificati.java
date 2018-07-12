package it.finsiel.siged.model;

import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.InvioClassificatiVO;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InvioClassificati {

    private InvioClassificatiVO icVO = new InvioClassificatiVO(); // di tipo


    private Map<String,DestinatarioVO> destinatari = new HashMap<String,DestinatarioVO>(); // di tipo 

    private Map<Integer,FascicoloVO> fascicoli = new HashMap<Integer,FascicoloVO>();
    
    
    
    public Map<String,DestinatarioVO> getDestinatari() {
        return destinatari;
    }

    public Map<Integer,FascicoloVO> getFascicoli() {
        return fascicoli;
    }
    
    public Collection<DestinatarioVO> getDestinatariCollection() {
        return destinatari.values();
    }

    public void addDestinatari(DestinatarioVO destinatario) {
        if (destinatario != null) {
            destinatari.put(String.valueOf(destinatario.getIdx()), destinatario);
        }
    }

    public Collection<FascicoloVO> getFascicoliCollection() {
        return fascicoli.values();
    }

    public void addFascicolo(FascicoloVO fasc) {
        if (fasc != null) {
        	fascicoli.put(fasc.getId(), fasc);
        }
    }
    
    public void removeDestinatario(String destinatario) {
        destinatari.remove(destinatario);
    }

    public void removeDestinatari() {
        if (destinatari != null) {
            destinatari.clear();
        }
    }

    public void setDestinatari(Map<String,DestinatarioVO> destinatari) {
        this.destinatari = destinatari;
    }

    public void removeFascicolo(int fascicoloId) {
    	fascicoli.remove(fascicoloId);
    }

    public void removeFascicoli() {
        if (fascicoli != null) {
        	fascicoli.clear();
        }
    }

    public void setFascicoli(Map<Integer,FascicoloVO> fascicoli) {
        this.fascicoli = fascicoli;
    }
    
    public InvioClassificatiVO getIcVO() {
        return icVO;
    }

    public void setIcVO(InvioClassificatiVO icVO) {
        this.icVO = icVO;
    }
}