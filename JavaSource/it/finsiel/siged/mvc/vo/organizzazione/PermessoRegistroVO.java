package it.finsiel.siged.mvc.vo.organizzazione;

import it.finsiel.siged.mvc.vo.VersioneVO;

/**
 * @author Marcello Spadafora.
 */

public final class PermessoRegistroVO extends VersioneVO {
    private int utenteId;

    private int registroId;

    public int getUtenteId() {
        return utenteId;
    }

    public void setUtenteId(int utenteId) {
        this.utenteId = utenteId;
    }

    public int getRegistroId() {
        return registroId;
    }

    public void setRegistroId(int registroId) {
        this.registroId = registroId;
    }

}