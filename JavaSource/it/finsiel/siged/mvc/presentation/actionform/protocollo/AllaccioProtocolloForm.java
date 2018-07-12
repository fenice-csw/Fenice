package it.finsiel.siged.mvc.presentation.actionform.protocollo;

/*
 * import it.finsiel.siged.mvc.vo.lookup.Capitolo; import
 * it.finsiel.siged.mvc.vo.lookup.Soggetto; import
 * it.finsiel.siged.mvc.vo.lookup.TipoDocumento; import
 * it.finsiel.siged.mvc.vo.lookup.Titolario; import
 * it.finsiel.siged.mvc.vo.organizzazione.Ufficio; import
 * it.finsiel.siged.mvc.vo.organizzazione.Utente; import
 * it.finsiel.siged.mvc.vo.registro.Registro;
 * 
 * import java.util.Date; import java.util.HashMap;
 */

import it.finsiel.siged.mvc.presentation.action.protocollo.AllaccioProtocolloAction;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.util.NumberUtil;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class AllaccioProtocolloForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(AllaccioProtocolloAction.class.getName());

    private Collection allacciabili = new ArrayList();

    private Collection allacciati = new ArrayList();

    private long allaccioProtocolloId;

    private String flagPrincipale;

    private String checkAllaccio;

    private ProtocolloVO fkProtocollo;

    private ProtocolloVO fkProtocolloAllacciato;

    private String allaccioAnnoProtocollo;

    private String allaccioProtocolloDa;

    private String allaccioProtocolloA;

    private String allaccioProtocolloAnno;

    private String btnCercaAllacci;

    private String btnSelezionaAllacci;

    private String btnAnnulla;

    /**
     * @return Returns the allaccioProtocolloA.
     */
    public String getAllaccioProtocolloA() {
        return allaccioProtocolloA;
    }

    /**
     * @param allaccioProtocolloA
     *            The allaccioProtocolloA to set.
     */
    public void setAllaccioProtocolloA(String allaccioProtocolloA) {
        this.allaccioProtocolloA = allaccioProtocolloA;
    }

    /**
     * @return Returns the allaccioProtocolloAnno.
     */
    public String getAllaccioProtocolloAnno() {
        return allaccioProtocolloAnno;
    }

    /**
     * @param allaccioProtocolloAnno
     *            The allaccioProtocolloAnno to set.
     */
    public void setAllaccioProtocolloAnno(String allaccioProtocolloAnno) {
        this.allaccioProtocolloAnno = allaccioProtocolloAnno;
    }

    public long getAllaccioProtocolloId() {
        return allaccioProtocolloId;
    }

    public void setAllaccioProtocolloId(long allaccioProtocolloId) {
        this.allaccioProtocolloId = allaccioProtocolloId;
    }

    public ProtocolloVO getFkProtocollo() {
        return fkProtocollo;
    }

    public void setFkProtocollo(ProtocolloVO fkProtocollo) {
        this.fkProtocollo = fkProtocollo;
    }

    public ProtocolloVO getFkProtocolloAllacciato() {
        return fkProtocolloAllacciato;
    }

    public void setFkProtocolloAllacciato(ProtocolloVO fkProtocolloAllacciato) {
        this.fkProtocolloAllacciato = fkProtocolloAllacciato;
    }

    public String getFlagPrincipale() {
        return flagPrincipale;
    }

    public void setFlagPrincipale(String flagPrincipale) {
        this.flagPrincipale = flagPrincipale;
    }

    public String getAllaccioProtocolloDa() {
        return allaccioProtocolloDa;
    }

    public void setAllaccioProtocolloDa(String allaccioProtocolloDa) {
        this.allaccioProtocolloDa = allaccioProtocolloDa;
    }

    public String getAllaccioAnnoProtocollo() {
        return allaccioAnnoProtocollo;
    }

    public void setAllaccioAnnoProtocollo(String allaccioAnnoProtocollo) {
        this.allaccioAnnoProtocollo = allaccioAnnoProtocollo;
    }

    public Collection getAllacciabili() {
        return allacciabili;
    }

    public void setAllacciabili(Collection allaccioProtocollo) {
        this.allacciabili = allaccioProtocollo;
    }

    public String getBtnCercaAllacci() {
        return btnCercaAllacci;
    }

    public void setBtnCercaAllacci(String btnListaAllacci) {
        this.btnCercaAllacci = btnListaAllacci;
    }

    public String getBtnAnnulla() {
        return btnAnnulla;
    }

    public void setBtnAnnulla(String btnAnnulla) {
        this.btnAnnulla = btnAnnulla;
    }

    public String getBtnSelezionaAllacci() {
        return btnSelezionaAllacci;
    }

    public void setBtnSelezionaAllacci(String btnSelezionaAllacci) {
        this.btnSelezionaAllacci = btnSelezionaAllacci;
    }

    public String getCheckAllaccio() {
        return checkAllaccio;
    }

    public void setCheckAllaccio(String checkAllaccio) {
        this.checkAllaccio = checkAllaccio;
    }

    public Collection getAllacciati() {
        return allacciati;
    }

    public void setAllacciati(Collection allacciati) {
        this.allacciati = allacciati;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {

    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (btnCercaAllacci != null) {
            if (allaccioProtocolloDa != null
                    && !"".equals(allaccioProtocolloDa)
                    && !(NumberUtil.isInteger(allaccioProtocolloDa))) {
                errors.add("allaccioProtocolloDa", new ActionMessage(
                        "formato_numerico"));
            } else if (allaccioProtocolloA != null
                    && !"".equals(allaccioProtocolloA)
                    && !(NumberUtil.isInteger(allaccioProtocolloA))) {
                errors.add("allaccioProtocolloA", new ActionMessage(
                        "formato_numerico"));
            } else if (allaccioProtocolloA != null
                    && !"".equals(allaccioProtocolloA)
                    && (NumberUtil.isInteger(allaccioProtocolloA))
                    && allaccioProtocolloDa != null
                    && !"".equals(allaccioProtocolloDa)
                    && (NumberUtil.isInteger(allaccioProtocolloDa))
                    && NumberUtil.getInt(allaccioProtocolloDa) > NumberUtil
                            .getInt(allaccioProtocolloDa)) {
                errors.add("allaccioIntervalloNumeri", new ActionMessage(
                        "numeri_protocollo_incongruenti"));

            } else if (allaccioProtocolloAnno != null
                    && !"".equals(allaccioProtocolloAnno)
                    && !(NumberUtil.isInteger(allaccioProtocolloAnno))) {
                errors.add("allaccioProtocolloAnno", new ActionMessage(
                        "formato_numerico"));
            }

        }
        return errors;
    }

}