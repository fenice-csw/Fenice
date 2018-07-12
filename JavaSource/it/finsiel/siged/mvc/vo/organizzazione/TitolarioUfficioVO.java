package it.finsiel.siged.mvc.vo.organizzazione;

import it.finsiel.siged.mvc.vo.VersioneVO;

public class TitolarioUfficioVO extends VersioneVO {
    private int ufficioId;
    
    private int titolarioId;

    private String path;
    
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getTitolarioId() {
        return titolarioId;
    }

    public void setTitolarioId(int titolarioId) {
        this.titolarioId = titolarioId;
    }

    public int getUfficioId() {
        return ufficioId;
    }

    public void setUfficioId(int ufficioId) {
        this.ufficioId = ufficioId;
    }

}