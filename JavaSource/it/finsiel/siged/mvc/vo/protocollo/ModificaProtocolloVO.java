/* Generated by Together */

package it.finsiel.siged.mvc.vo.protocollo;

import it.finsiel.siged.mvc.vo.BaseVO;

import java.util.Date;

public class ModificaProtocolloVO extends BaseVO {
    private String numeroProtocollo;
    
  
    private String campo;

    private Date dataModifica;

    private String utente;

    private String valorePrecedente;

    public String getNumeroProtocollo() {
        return numeroProtocollo;
    }

    public void setNumeroProtocollo(String numeroProtocollo) {
        this.numeroProtocollo = numeroProtocollo;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public Date getDataModifica() {
        return dataModifica;
    }

    public void setDataModifica(Date dataModifica) {
        this.dataModifica = dataModifica;
    }

    public String getUtente() {
        return utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }

    public String getValorePrecedente() {
        return valorePrecedente;
    }

    public void setValorePrecedente(String valorePrecedente) {
        this.valorePrecedente = valorePrecedente;
    }

    

}