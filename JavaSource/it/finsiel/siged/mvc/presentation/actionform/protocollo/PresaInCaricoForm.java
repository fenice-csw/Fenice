package it.finsiel.siged.mvc.presentation.actionform.protocollo;

import it.finsiel.siged.mvc.presentation.action.protocollo.PresaInCaricoAction;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class PresaInCaricoForm extends ActionForm {

	private String dataRegistrazioneDa = "";

    private String dataRegistrazioneA = "";

    private String numeroProtocolloDa;

    private String numeroProtocolloA;

    private String annoProtocolloDa;

    private String annoProtocolloA;

    private String btnCerca;

    private String btnAccettaSelezionati;
    
    private String btnFascicolaSelezionati;

    private String btnRespingiSelezionati;

    private String[] protocolliSelezionati;

    private String tipoUtenteUfficio = "";

    private Map protocolliInCarico = new HashMap(2);

    private String msgAssegnatarioCompetente;

    static Logger logger = Logger
            .getLogger(PresaInCaricoAction.class.getName());

    
    private String tipoProtocollo;
	
	public String getTipoProtocollo() {
		return tipoProtocollo;
	}

	public void setTipoProtocollo(String tipoProtocollo) {
		this.tipoProtocollo = tipoProtocollo;
	}
    
    public String getBtnFascicolaSelezionati() {
		return btnFascicolaSelezionati;
	}

	public void setBtnFascicolaSelezionati(String btnFascicolaSelezionati) {
		this.btnFascicolaSelezionati = btnFascicolaSelezionati;
	}

	public Map getProtocolliInCarico() {
        return protocolliInCarico;
    }

    public Collection getProtocolliInCaricoCollection() {
        if (protocolliInCarico != null) {
            return protocolliInCarico.values();
        } else
            return null;
    }

    
    public void removeProtocolliInCarico() {
        if (protocolliInCarico != null) {
            protocolliInCarico.clear();
        }
    }

    public ProtocolloVO getProtocolloVO(Integer protocolloId) {
        ProtocolloVO p = (ProtocolloVO) protocolliInCarico.get(protocolloId);
        return p;
    }

    /**
     * @param protocolliInCarico
     *            The protocolliInCarico to set.
     */
    public void setProtocolliInCarico(Map protocolliInCarico) {
        this.protocolliInCarico = protocolliInCarico;
    }

    /**
     * @return Returns the logger.
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * @param logger
     *            The logger to set.
     */
    public static void setLogger(Logger logger) {
        PresaInCaricoForm.logger = logger;
    }

    /**
     * @return Returns the annoProtocolloA.
     */
    public String getAnnoProtocolloA() {
        return annoProtocolloA;
    }

    /**
     * @param annoProtocolloA
     *            The annoProtocolloA to set.
     */
    public void setAnnoProtocolloA(String annoProtocolloA) {
        this.annoProtocolloA = annoProtocolloA;
    }

    /**
     * @return Returns the annoProtocolloDa.
     */
    public String getAnnoProtocolloDa() {
        return annoProtocolloDa;
    }

    /**
     * @param annoProtocolloDa
     *            The annoProtocolloDa to set.
     */
    public void setAnnoProtocolloDa(String annoProtocolloDa) {
        this.annoProtocolloDa = annoProtocolloDa;
    }

    /**
     * @return Returns the btnCerca.
     */
    public String getBtnCerca() {
        return btnCerca;
    }

    /**
     * @param btnCerca
     *            The btnCerca to set.
     */
    public void setBtnCerca(String btnCerca) {
        this.btnCerca = btnCerca;
    }

    /**
     * @return Returns the dataRegistrazioneA.
     */
    public String getDataRegistrazioneA() {
        return dataRegistrazioneA;
    }

    /**
     * @param dataRegistrazioneA
     *            The dataRegistrazioneA to set.
     */
    public void setDataRegistrazioneA(String dataRegistrazioneA) {
        this.dataRegistrazioneA = dataRegistrazioneA;
    }

    /**
     * @return Returns the dataRegistrazioneDa.
     */
    public String getDataRegistrazioneDa() {
        return dataRegistrazioneDa;
    }

    /**
     * @param dataRegistrazioneDa
     *            The dataRegistrazioneDa to set.
     */
    public void setDataRegistrazioneDa(String dataRegistrazioneDa) {
        this.dataRegistrazioneDa = dataRegistrazioneDa;
    }

    /**
     * @return Returns the numeroProtocolloA.
     */
    public String getNumeroProtocolloA() {
        return numeroProtocolloA;
    }

    /**
     * @param numeroProtocolloA
     *            The numeroProtocolloA to set.
     */
    public void setNumeroProtocolloA(String numeroProtocolloA) {
        this.numeroProtocolloA = numeroProtocolloA;
    }

    /**
     * @return Returns the numeroProtocolloDa.
     */
    public String getNumeroProtocolloDa() {
        return numeroProtocolloDa;
    }

    /**
     * @param numeroProtocolloDa
     *            The numeroProtocolloDa to set.
     */
    public void setNumeroProtocolloDa(String numeroProtocolloDa) {
        this.numeroProtocolloDa = numeroProtocolloDa;
    }

    /**
     * @return Returns the btnAccettaTutti.
     */
    public String getBtnAccettaSelezionati() {
        return btnAccettaSelezionati;
    }

    /**
     * @param btnAccettaTutti
     *            The btnAccettaTutti to set.
     */
    public void setBtnAccettaSelezionati(String btnAccettaTutti) {
        this.btnAccettaSelezionati = btnAccettaTutti;
    }

    /**
     * @return Returns the btnRespingiTutti.
     */
    public String getBtnRespingiSelezionati() {
        return btnRespingiSelezionati;
    }

    /**
     * @param btnRespingiTutti
     *            The btnRespingiTutti to set.
     */
    public void setBtnRespingiSelezionati(String btnRespingiTutti) {
        this.btnRespingiSelezionati = btnRespingiTutti;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {

    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (btnCerca != null) {
            if (dataRegistrazioneDa != null && !"".equals(dataRegistrazioneDa)
                    && !DateUtil.isData(dataRegistrazioneDa)) {
                errors.add("dataRegistrazioneDa", new ActionMessage(
                        "formato.data.errato", "Data Da"));
            }
            if (dataRegistrazioneA != null && !"".equals(dataRegistrazioneA)
                    && !DateUtil.isData(dataRegistrazioneA)) {
                errors.add("dataRegistrazioneA", new ActionMessage(
                        "formato.data.errato", "Data A"));
            }
            if (dataRegistrazioneDa != null && !"".equals(dataRegistrazioneDa)
                    && DateUtil.isData(dataRegistrazioneDa)
                    && dataRegistrazioneA != null
                    && !"".equals(dataRegistrazioneA)
                    && DateUtil.isData(dataRegistrazioneA)) {
                if (DateUtil.toDate(dataRegistrazioneA).before(
                        DateUtil.toDate(dataRegistrazioneDa))) {
                    errors.add("dataRegistrazioneDa", new ActionMessage(
                            "date_incongruenti"));
                }
            }
            if (numeroProtocolloDa != null && !"".equals(numeroProtocolloDa)
                    && !(NumberUtil.isInteger(numeroProtocolloDa))) {
                errors.add("numeroProtocolloDa", new ActionMessage(
                        "formato.numerico.errato", "Numero Da"));
            }
            if (numeroProtocolloA != null && !"".equals(numeroProtocolloA)
                    && !(NumberUtil.isInteger(numeroProtocolloA))) {
                errors.add("numeroProtocolloA", new ActionMessage(
                        "formato.numerico.errato", "Numero A"));
            }
            if (annoProtocolloDa != null && !"".equals(annoProtocolloDa)
                    && !(NumberUtil.isInteger(annoProtocolloDa))) {
                errors.add("annoProtocolloDa", new ActionMessage(
                        "formato.numerico.errato", "Anno Da"));
            }
            if (annoProtocolloA != null && !"".equals(annoProtocolloA)
                    && !(NumberUtil.isInteger(annoProtocolloA))) {
                errors.add("annoProtocolloA", new ActionMessage(
                        "formato.numerico.errato", "Anno A"));
            }

        } else if ((btnAccettaSelezionati != null || btnRespingiSelezionati != null)
        		||btnFascicolaSelezionati!=null && protocolliSelezionati == null ) {
            errors.add("protocolliSelezionati", new ActionMessage(
                    "incarico_selezione_protocollo"));
        }

        if (!errors.isEmpty()) {
            setBtnCerca(null);
            setBtnAccettaSelezionati(null);
            setBtnRespingiSelezionati(null);
        }

        return errors;
    }

    /**
     * @return Returns the protocolloInCarico.
     */
    public String[] getProtocolliSelezionati() {
        return protocolliSelezionati;
    }

    /**
     * @param protocolloInCarico
     *            The protocolloInCarico to set.
     */
    public void setProtocolliSelezionati(String pCarico[]) {
        this.protocolliSelezionati = pCarico;
    }

    /**
     * @return Returns the tipoUtenteUfficio.
     */
    public String getTipoUtenteUfficio() {
        if (tipoUtenteUfficio == null) {
            tipoUtenteUfficio = "U";
        }
        return tipoUtenteUfficio;
    }

    /**
     * @param tipoUtenteUfficio
     *            The tipoUtenteUfficio to set.
     */
    public void setTipoUtenteUfficio(String tipoUtenteUfficio) {
        this.tipoUtenteUfficio = tipoUtenteUfficio;
    }

    public String getMsgAssegnatarioCompetente() {
        return msgAssegnatarioCompetente;
    }

    public void setMsgAssegnatarioCompetente(String msgAssegnatarioCompetente) {
        this.msgAssegnatarioCompetente = msgAssegnatarioCompetente;
    }

}