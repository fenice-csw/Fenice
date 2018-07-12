
package it.finsiel.siged.model.protocollo;

import it.compit.fenice.mvc.presentation.helper.ProtocolloProcedimentoView;
import it.finsiel.siged.mvc.presentation.helper.FascicoloView;
import it.finsiel.siged.mvc.vo.protocollo.FaldoneVO;
import it.finsiel.siged.mvc.vo.protocollo.ProcedimentoVO;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Paolo Spadadafora - digital highway
 * 
 */
public class Procedimento {

    private ProcedimentoVO procedimentoVO = new ProcedimentoVO();

    private Map<String,ProtocolloProcedimentoView> protocolli = new HashMap<String,ProtocolloProcedimentoView>(2);

    private Map<String,FascicoloView> fascicoli = new HashMap<String,FascicoloView>(2);

    private Map<String,FaldoneVO> faldoni = new HashMap<String,FaldoneVO>(2);

    public Map<String,FaldoneVO> getFaldoni() {
        return faldoni;
    }

    public void setFaldoni(Map<String,FaldoneVO> faldoni) {
        this.faldoni = faldoni;
    }

    public Map<String,FascicoloView> getFascicoli() {
        return fascicoli;
    }

    public void setFascicoli(Map<String,FascicoloView> fascicoli) {
        this.fascicoli = fascicoli;
    }

    public ProcedimentoVO getProcedimentoVO() {
        return procedimentoVO;
    }

    public void setProcedimentoVO(ProcedimentoVO procedimentoVO) {
        this.procedimentoVO = procedimentoVO;
    }

    public Map<String,ProtocolloProcedimentoView> getProtocolli() {
        return protocolli;
    }

    public void setProtocolli(Map<String,ProtocolloProcedimentoView> protocolli) {
        this.protocolli = protocolli;
    }

}