package it.finsiel.siged.mvc.vo.posta;

import it.finsiel.siged.mvc.vo.BaseVO;

import java.util.Date;

public class CodaInvioVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	public CodaInvioVO() {
    }

    private int numeroProtocollo;

    private int aooId;

    private Date dataInvio;

    private int stato;

    private int protocolloId;

    private Date dataCreazione;

    public int getAooId() {
        return aooId;
    }

    public void setAooId(int aooId) {
        this.aooId = aooId;
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Date getDataInvio() {
        return dataInvio;
    }

    public void setDataInvio(Date dataInvio) {
        this.dataInvio = dataInvio;
    }

    public int getProtocolloId() {
        return protocolloId;
    }

    public void setProtocolloId(int protocolloId) {
        this.protocolloId = protocolloId;
    }

    public int getStato() {
        return stato;
    }

    public void setStato(int stato) {
        this.stato = stato;
    }

    public int getNumeroProtocollo() {
        return numeroProtocollo;
    }

    public void setNumeroProtocollo(int numeroProtocollo) {
        this.numeroProtocollo = numeroProtocollo;
    }
}