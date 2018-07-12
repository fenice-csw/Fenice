package it.finsiel.siged.mvc.vo.evidenza;

import it.finsiel.siged.mvc.vo.VersioneVO;

import java.util.Date;

public class EvidenzaVO extends VersioneVO {

    private int idEvidenza;

    private int numero;

    private Date dataEvidenza;

    private String ufficio;

    private String oggetto;

    // private String referente;

    public Date getDataEvidenza() {
        return dataEvidenza;
    }

    public void setDataEvidenza(Date dataEvidenza) {
        this.dataEvidenza = dataEvidenza;
    }

    public int getIdEvidenza() {
        return idEvidenza;
    }

    public void setIdEvidenza(int idEvidenza) {
        this.idEvidenza = idEvidenza;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    // public String getReferente() {
    // return referente;
    // }
    //
    // public void setReferente(String referente) {
    // this.referente = referente;
    // }

    public String getUfficio() {
        return ufficio;
    }

    public void setUfficio(String ufficio) {
        this.ufficio = ufficio;
    }

}