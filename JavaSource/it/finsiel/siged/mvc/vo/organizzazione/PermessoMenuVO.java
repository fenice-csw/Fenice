package it.finsiel.siged.mvc.vo.organizzazione;

import it.finsiel.siged.mvc.vo.VersioneVO;

/**
 * @author Marcello Spadafora.
 */

public final class PermessoMenuVO extends VersioneVO {
    
	private int caricaId;
	
	private int utenteId;

    private int ufficioId;

    private int menuId;

    
    
    public int getCaricaId() {
		return caricaId;
	}

	public void setCaricaId(int caricaId) {
		this.caricaId = caricaId;
	}

	public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

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