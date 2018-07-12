package it.finsiel.siged.mvc.vo.organizzazione;

import it.finsiel.siged.mvc.vo.VersioneVO;

public class ReferenteUfficioVO extends VersioneVO {
    private int utenteId;

    private int ufficioId;

    public int getUfficioId() {
        return ufficioId;
    }

    public void setUfficioId(int ufficioId) {
        this.ufficioId = ufficioId;
    }

    public int getUtenteId() {
        return utenteId;
    }

    public void setUtenteId(int utenteId) {
        this.utenteId = utenteId;
    }
       
}