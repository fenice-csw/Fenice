package it.finsiel.siged.mvc.vo.firma;

import it.finsiel.siged.mvc.vo.BaseVO;

import java.util.Date;

public class CrlUrlVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	private int caId;

    private String url;

    private String tipo;

    private Date dataEmissione;

    public int getCaId() {
        return caId;
    }

    public void setCaId(int caId) {
        this.caId = caId;
    }

    public Date getDataEmissione() {
        return dataEmissione;
    }

    public void setDataEmissione(Date dataEmissione) {
        this.dataEmissione = dataEmissione;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}