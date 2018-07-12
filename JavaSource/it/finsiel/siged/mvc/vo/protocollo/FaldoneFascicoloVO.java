package it.finsiel.siged.mvc.vo.protocollo;

import it.finsiel.siged.mvc.vo.VersioneVO;

public class FaldoneFascicoloVO extends VersioneVO {
    private int faldoneId;

    private int fascicoloId;

    private String utente;

    public int getFaldoneId() {
        return faldoneId;
    }

    public void setFaldoneId(int faldoneId) {
        this.faldoneId = faldoneId;
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