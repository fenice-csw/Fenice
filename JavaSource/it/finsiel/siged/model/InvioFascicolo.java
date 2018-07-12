
package it.finsiel.siged.model;

import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.InvioFascicoliVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class InvioFascicolo {
    private int fascicoloId;

    private Collection<InvioFascicoliVO> documenti = new ArrayList<InvioFascicoliVO>(); // di tipo FileVO

    private Map<String,DestinatarioVO> destinatari = new HashMap<String,DestinatarioVO>(); // di tipo DestinatarioVO

    private Collection<ProtocolloVO> protocolli = new ArrayList<ProtocolloVO>();

    public Collection<DestinatarioVO> getDestinatariCollection() {
        return destinatari.values();
    }

    public Map<String,DestinatarioVO> getDestinatari() {
        return destinatari;
    }

    public void addDestinatari(DestinatarioVO destinatario) {
        if (destinatario != null) {
            destinatari.put(destinatario.getDestinatario()+destinatario.getMezzoDesc(), destinatario);
        }
    }

    public void removeDestinatario(String destinatario) {
        destinatari.remove(destinatario);
    }

    public void removeDestinatari() {
        if (destinatari != null) {
            destinatari.clear();
        }
    }

    public void setDestinatari(Map<String,DestinatarioVO> destinatari) {
        this.destinatari = destinatari;
    }

    public Collection<InvioFascicoliVO> getDocumenti() {
        return documenti;
    }

    public void setDocumenti(Collection<InvioFascicoliVO> documenti) {
        this.documenti = documenti;
    }

    public int getFascicoloId() {
        return fascicoloId;
    }

    public void setFascicoloId(int fascicoloId) {
        this.fascicoloId = fascicoloId;
    }

    public Collection<ProtocolloVO> getProtocolli() {
        return protocolli;
    }

    public void setProtocolli(Collection<ProtocolloVO> protocolli) {
        this.protocolli = protocolli;
    }
}