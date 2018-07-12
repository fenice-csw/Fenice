
package it.finsiel.siged.mvc.vo.protocollo;

import it.finsiel.siged.mvc.vo.BaseVO;

public class StatoFascicoloVO extends BaseVO {
    
	public StatoFascicoloVO() {
    }

    private String descrizione;

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}