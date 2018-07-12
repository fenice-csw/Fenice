package it.finsiel.siged.mvc.vo.protocollo;

import it.finsiel.siged.mvc.vo.VersioneVO;

public class ProcedimentoFaldoneVO extends VersioneVO {
    private int procedimentoId;

    private int faldoneId;

    private String utente;

    public int getProcedimentoId() {
        return procedimentoId;
    }

    public void setProcedimentoId(int procedimentoId) {
        this.procedimentoId = procedimentoId;
    }

    public int getFaldoneId() {
        return faldoneId;
    }

    public void setFaldoneId(int faldoneId) {
        this.faldoneId = faldoneId;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }

    
       
}