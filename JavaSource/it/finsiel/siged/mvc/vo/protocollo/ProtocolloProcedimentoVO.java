package it.finsiel.siged.mvc.vo.protocollo;

import it.finsiel.siged.mvc.vo.VersioneVO;

public class ProtocolloProcedimentoVO extends VersioneVO {
    
	private static final long serialVersionUID = 1L;

	private int protocolloId;

    private int procedimentoId;

    private int fascicoloId;
    
    private int numeroProtocollo;

    private String numeroProcedimento;
    
    private String oggetto;

    private boolean modificabile = true;

    private int sospeso;
    
    private boolean aggiunto;

    
    
	public boolean isAggiunto() {
		return aggiunto;
	}

	public void setAggiunto(boolean aggiunto) {
		this.aggiunto = aggiunto;
	}

	public int getSospeso() {
		return sospeso;
	}

	public void setSospeso(int sospeso) {
		this.sospeso = sospeso;
	}

	public int getFascicoloId() {
		return fascicoloId;
	}

	public void setFascicoloId(int fascicoloId) {
		this.fascicoloId = fascicoloId;
	}

	public String getNumeroProcedimento() {
        return numeroProcedimento;
    }

    public void setNumeroProcedimento(String numeroProcedimento) {
        this.numeroProcedimento = numeroProcedimento;
    }

    public int getNumeroProtocollo() {
        return numeroProtocollo;
    }

    public void setNumeroProtocollo(int numeroProtocollo) {
        this.numeroProtocollo = numeroProtocollo;
    }

    public int getProcedimentoId() {
        return procedimentoId;
    }

    public void setProcedimentoId(int procedimentoId) {
        this.procedimentoId = procedimentoId;
    }

    public int getProtocolloId() {
        return protocolloId;
    }

    public void setProtocolloId(int protocolloId) {
        this.protocolloId = protocolloId;
    }

    public boolean getModificabile() {
        return modificabile;
    }

    public void setModificabile(boolean modificabile) {
        this.modificabile = modificabile;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    
    
    
}