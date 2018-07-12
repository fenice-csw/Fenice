package it.finsiel.siged.mvc.vo.protocollo;

import it.finsiel.siged.mvc.vo.VersioneVO;

public class InvioFascicoliDestinatariVO extends VersioneVO {

    private int fascicoloId;

    private DestinatarioVO destinatario;

    public DestinatarioVO getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(DestinatarioVO destinatario) {
        this.destinatario = destinatario;
    }

    public int getFascicoloId() {
        return fascicoloId;
    }

    public void setFascicoloId(int fascicoloId) {
        this.fascicoloId = fascicoloId;
    }
}