package it.finsiel.siged.mvc.presentation.helper;

public class PermessoDocumentoView extends AssegnatarioView {
    private int tipoPermesso;

    private String descrizioneTipoPermesso;

    private String msgPermesso;

    public String getDescrizioneTipoPermesso() {
        return descrizioneTipoPermesso;
    }

    public void setDescrizioneTipoPermesso(String descrizioneTipoPermesso) {
        this.descrizioneTipoPermesso = descrizioneTipoPermesso;
    }

    public int getTipoPermesso() {
        return tipoPermesso;
    }

    public void setTipoPermesso(int tipoPermesso) {
        this.tipoPermesso = tipoPermesso;
    }

    public String getMsgPermesso() {
        return msgPermesso;
    }

    public void setMsgPermesso(String msgPermesso) {
        this.msgPermesso = msgPermesso;
    }
    
}