package it.finsiel.siged.mvc.vo.organizzazione;

import it.finsiel.siged.mvc.vo.IdentityVO;

public class ServizioVO extends IdentityVO {

    public static final String UFFICIO_NORMALE = "N";

    public static final String LABEL_UFFICIO_NORMALE = "Ufficio normale";

    public static final String UFFICIO_CENTRALE = "C";

    public static final String LABEL_UFFICIO_CENTRALE = "Ufficio centrale";

    public static final String UFFICIO_SEMICENTRALE = "S";

    public static final String LABEL_UFFICIO_SEMICENTRALE = "Ufficio semi-centrale";

    private String tipo;

    private boolean attivo;

    private boolean accettazioneAutomatica;

    private int aooId;

    private int parentId;

    public boolean isAccettazioneAutomatica() {
        return accettazioneAutomatica;
    }

    public void setAccettazioneAutomatica(boolean accettazioneAutomatica) {
        this.accettazioneAutomatica = accettazioneAutomatica;
    }

    public boolean isAttivo() {
        return attivo;
    }

    public void setAttivo(boolean attivo) {
        this.attivo = attivo;
    }

    public int getAooId() {
        return aooId;
    }

    public void setAooId(int aooId) {
        this.aooId = aooId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int menuId) {
        this.parentId = menuId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String toString() {
        return "UfficioVO - ID=" + String.valueOf(getId()) + " Nome="
                + getName();
    }
}