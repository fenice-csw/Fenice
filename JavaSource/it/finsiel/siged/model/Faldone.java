
package it.finsiel.siged.model;

import it.finsiel.siged.mvc.vo.protocollo.FaldoneVO;
import it.finsiel.siged.mvc.vo.protocollo.ProcedimentoVO;

import java.util.ArrayList;
import java.util.Collection;

public class Faldone {
    private FaldoneVO faldoneVO = new FaldoneVO();

    private Collection<Fascicolo> fascicoli = new ArrayList<Fascicolo>(); // di tipo Fascicolo

    private Collection<ProcedimentoVO> procedimenti = new ArrayList<ProcedimentoVO>(); // di ProcedimentoVO

    public FaldoneVO getFaldoneVO() {
        return faldoneVO;
    }

    public void setFaldoneVO(FaldoneVO faldoneVO) {
        this.faldoneVO = faldoneVO;
    }

    public Collection<Fascicolo> getFascicoli() {
        return fascicoli;
    }

    public void setFascicoli(Collection<Fascicolo> fascicoli) {
        this.fascicoli = fascicoli;
    }

    public Collection<ProcedimentoVO> getProcedimenti() {
        return procedimenti;
    }

    public void setProcedimenti(Collection<ProcedimentoVO> provvedimenti) {
        this.procedimenti = provvedimenti;
    }

}