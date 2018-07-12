package it.finsiel.siged.mvc.vo.documentale;

import it.finsiel.siged.mvc.vo.BaseVO;

/**
 * @author Paolo Spadafora
 * 
 */
public class PermessoFileVO extends BaseVO {

    private int fileAttributeId;

    private int caricaId;

    private int ufficioId;

    private int tipoPermesso;
    
    private String msgPermesso;

    public PermessoFileVO() {

    }

    public int getFileAttributeId() {
        return fileAttributeId;
    }

    public void setFileAttributeId(int fileAttributeId) {
        this.fileAttributeId = fileAttributeId;
    }

    public int getTipoPermesso() {
        return tipoPermesso;
    }

    public void setTipoPermesso(int tipoPermesso) {
        this.tipoPermesso = tipoPermesso;
    }

    public int getUfficioId() {
        return ufficioId;
    }

    public void setUfficioId(int ufficioId) {
        this.ufficioId = ufficioId;
    }

   

    public int getCaricaId() {
		return caricaId;
	}

	public void setCaricaId(int caricaId) {
		this.caricaId = caricaId;
	}

	public String getMsgPermesso() {
        return msgPermesso;
    }

    public void setMsgPermesso(String msg) {
        this.msgPermesso = msg;
    }

}
