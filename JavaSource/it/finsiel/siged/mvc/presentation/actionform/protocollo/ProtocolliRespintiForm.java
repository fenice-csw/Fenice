package it.finsiel.siged.mvc.presentation.actionform.protocollo;

import it.finsiel.siged.mvc.presentation.action.protocollo.ProtocolliRespintiAction;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class ProtocolliRespintiForm extends ActionForm {

    private String dataRegistrazioneDa;

    private String dataRegistrazioneA;

    private String numeroProtocolloDa;

    private String numeroProtocolloA;

    private String annoProtocolloDa;

    private String annoProtocolloA;

    private String protocolloRifiutato;

    static Logger logger = Logger.getLogger(ProtocolliRespintiAction.class
            .getName());

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
        ProtocolliRespintiForm.logger = logger;
    }

    private Map protocolliRifiutati = new HashMap();

    public Map getProtocolliRifiutati() {
        return protocolliRifiutati;
    }

    public Collection getProtocolliRifiutatiCollection() {
        if (protocolliRifiutati != null) {
            return protocolliRifiutati.values();
        } else
            return null;
    }

    public void addProtocolliScarico(ReportProtocolloView protocollo) {
        if (protocollo != null) {
            protocolliRifiutati.put(new Integer(protocollo.getProtocolloId()),
                    protocollo);
        }
    }

    public void removeProtocolliScarico(Integer protocolloId) {
        protocolliRifiutati.remove(protocolloId);
    }

    public void removeProtocolliScarico(int protocolloId) {
        removeProtocolliScarico(new Integer(protocolloId));
    }

    public void removeProtocolliScarico(ReportProtocolloView protocollo) {
        if (protocollo != null) {
            removeProtocolliScarico(protocollo.getProtocolloId());
        }
    }

    public void removeProtocolliScarico() {
        if (protocolliRifiutati != null) {
            protocolliRifiutati.clear();
        }
    }

    public ReportProtocolloView getProtocollo(Integer protocolloId) {
        return (ReportProtocolloView) protocolliRifiutati.get(protocolloId);
    }

    /**
     * @param protocolliInCarico
     *            The protocolliInCarico to set.
     */
    public void setProtocolliRifiutati(Map protocolliRifiutati) {
        this.protocolliRifiutati = protocolliRifiutati;
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

    public void reset(ActionMapping mapping, HttpServletRequest request) {

    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (request.getParameter("btnCerca") != null) {
            protocolloRifiutato = null;
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

        } else if ((request.getParameter("btnRiassegna") != null || request
                .getParameter("btnViewProtocollo") != null)
                && request.getParameter("protocolloRifiutato") == null) {
            errors.add("protocolloRifiutato", new ActionMessage(
                    "riassegna_protocollo"));
        }
        return errors;
    }

    /**
     * @return Returns the protocolloRifiutato.
     */
    public String getProtocolloRifiutato() {
        return protocolloRifiutato;
    }

    /**
     * @param protocolloRifiutato
     *            The protocolloRifiutato to set.
     */
    public void setProtocolloRifiutato(String protocolloRifiutato) {
        this.protocolloRifiutato = protocolloRifiutato;
    }

}