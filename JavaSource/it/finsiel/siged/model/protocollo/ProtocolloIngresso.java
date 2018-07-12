
package it.finsiel.siged.model.protocollo;

import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;

import java.util.ArrayList;
import java.util.Collection;


public class ProtocolloIngresso extends Protocollo {

	
    private SoggettoVO mittente;

    private Collection<AssegnatarioVO> assegnatari = new ArrayList<AssegnatarioVO>();

    private String msgAssegnatarioCompetente;

    public SoggettoVO getMittente() {
        return mittente;
    }

    public void setMittente(SoggettoVO mittente) {
        this.mittente = mittente;
    }

    public Collection<AssegnatarioVO> getAssegnatari() {
        return new ArrayList<AssegnatarioVO>(assegnatari);
    }

    public void aggiungiAssegnatario(AssegnatarioVO assegnatario) {
        if (assegnatario != null) {
            assegnatari.add(assegnatario);
        }
    }

    public void aggiungiAssegnatari(Collection<AssegnatarioVO> assegnatari) {
        this.assegnatari.addAll(assegnatari);
    }

    public void removeAssegnatari() {
        assegnatari.clear();
    }
    public String getMsgAssegnatarioCompetente() {
        return msgAssegnatarioCompetente;
    }

    public void setMsgAssegnatarioCompetente(String msgAssegnatarioCompetente) {
        this.msgAssegnatarioCompetente = msgAssegnatarioCompetente;
    }

}