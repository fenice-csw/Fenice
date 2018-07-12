package it.finsiel.siged.mvc.vo.protocollo;

import it.finsiel.siged.mvc.vo.VersioneVO;

public class SpedizioneDestinatarioVO extends VersioneVO {

    private int protocolloId;

    private int destinatarioId;

    private int spedizioneId;

    private String spedizioneDesc;

    private String dataSpedizione;

    public String getDataSpedizione() {
        return dataSpedizione;
    }

    public void setDataSpedizione(String dataSpedizione) {
        this.dataSpedizione = dataSpedizione;
    }

    public SpedizioneDestinatarioVO() {
    }

    public int getDestinatarioId() {
        return destinatarioId;
    }

    public void setDestinatarioId(int destinatarioId) {
        this.destinatarioId = destinatarioId;
    }

    public int getProtocolloId() {
        return protocolloId;
    }

    public void setProtocolloId(int protocolloId) {
        this.protocolloId = protocolloId;
    }

    public int getSpedizioneId() {
        return spedizioneId;
    }

    public void setSpedizioneId(int spedizioneId) {
        this.spedizioneId = spedizioneId;
    }

    public String getSpedizioneDesc() {
        return spedizioneDesc;
    }

    public void setSpedizioneDesc(String spedizioneDesc) {
        this.spedizioneDesc = spedizioneDesc;
    }

}