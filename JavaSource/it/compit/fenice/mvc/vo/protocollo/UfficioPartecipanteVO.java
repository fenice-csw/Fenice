package it.compit.fenice.mvc.vo.protocollo;

import it.finsiel.siged.mvc.vo.VersioneVO;

public class UfficioPartecipanteVO extends VersioneVO {

	private static final long serialVersionUID = 1L;

    private boolean visibilita;

    private int ufficioId;
    
    private String nomeUfficio;
    
    private boolean principale;

	public boolean isVisibilita() {
		return visibilita;
	}

	public void setVisibilita(boolean visibilita) {
		this.visibilita = visibilita;
	}

	public boolean isPrincipale() {
		return principale;
	}

	public void setPrincipale(boolean principale) {
		this.principale = principale;
	}

	public int getUfficioId() {
		return ufficioId;
	}

	public void setUfficioId(int ufficioId) {
		this.ufficioId = ufficioId;
	}

	public String getNomeUfficio() {
		return nomeUfficio;
	}

	public void setNomeUfficio(String nomeUfficio) {
		this.nomeUfficio = nomeUfficio;
	}
    
}
