package it.finsiel.siged.mvc.vo.protocollo;

import it.finsiel.siged.mvc.vo.VersioneVO;

public class ProcedimentoFascicoloVO extends VersioneVO {
    private int procedimentoId;

    private int fascicoloId;

    private String utente;

    public int getProcedimentoId() {
        return procedimentoId;
    }

    public void setProcedimentoId(int procedimentoId) {
        this.procedimentoId = procedimentoId;
    }

    public int getFascicoloId() {
        return fascicoloId;
    }

    public void setFascicoloId(int fascicoloId) {
        this.fascicoloId = fascicoloId;
    }

    public String getUtente() {
        return utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }

    
       
}