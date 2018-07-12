package it.finsiel.siged.mvc.presentation.actionform.report;

import it.finsiel.siged.mvc.presentation.actionform.ParametriForm;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public abstract class ReportCommonForm extends ParametriForm {

   
	private static final long serialVersionUID = 1L;

	private String dataInizio;

    private String dataFine;

    // button
    private String btnStampa;

    private String btnVisualizza;

    private String btnAnnulla;

    // assegnatari
    private int ufficioCorrenteId;

    private int ufficioSelezionatoId;

    private Collection<UfficioVO> pathUffici = new ArrayList<UfficioVO>();

    private Collection<UfficioVO> ufficiDipendenti = new ArrayList<UfficioVO>();

    private String impostaUfficioAction;

    private String ufficioCorrentePath;

    private int utenteSelezionatoId;

    private UfficioVO ufficioCorrente;

    private Collection<UtenteVO> utenti;

    /**
     * @return Returns the utenti.
     */
    public Collection<UtenteVO> getUtenti() {
        return utenti;
    }

    /**
     * @param utenti
     *            The utenti to set.
     */
    public void setUtenti(Collection<UtenteVO> utenti) {
        this.utenti = utenti;
    }

    /**
     * @return Returns the impostaUfficioAction.
     */
    public String getImpostaUfficioAction() {
        return impostaUfficioAction;
    }

    /**
     * @param impostaUfficioAction
     *            The impostaUfficioAction to set.
     */
    public void setImpostaUfficioAction(String impostaUfficioAction) {
        this.impostaUfficioAction = impostaUfficioAction;
    }

    /**
     * @return Returns the btnAnnulla.
     */
    public String getBtnAnnulla() {
        return btnAnnulla;
    }

    /**
     * @param btnAnnulla
     *            The btnAnnulla to set.
     */
    public void setBtnAnnulla(String btnAnnulla) {
        this.btnAnnulla = btnAnnulla;
    }

    public String getBtnVisualizza() {
        return btnVisualizza;
    }

    public void setBtnVisualizza(String btnVisualizza) {
        this.btnVisualizza = btnVisualizza;
    }

    /**
     * @return Returns the ufficioCorrente.
     */
    public UfficioVO getUfficioCorrente() {
        return ufficioCorrente;
    }

    /**
     * @param ufficioCorrente
     *            The ufficioCorrente to set.
     */
    public void setUfficioCorrente(UfficioVO ufficioCorrente) {
        this.ufficioCorrente = ufficioCorrente;
    }

    /**
     * @return Returns the ufficioCorrentePath.
     */
    public String getUfficioCorrentePath() {
        return ufficioCorrentePath;
    }

    /**
     * @param ufficioCorrentePath
     *            The ufficioCorrentePath to set.
     */
    public void setUfficioCorrentePath(String ufficioCorrentePath) {
        this.ufficioCorrentePath = ufficioCorrentePath;
    }

    /**
     * @return Returns the utenteSelezionatoId.
     */
    public int getUtenteSelezionatoId() {
        return utenteSelezionatoId;
    }

    /**
     * @param utenteSelezionatoId
     *            The utenteSelezionatoId to set.
     */
    public void setUtenteSelezionatoId(int utenteSelezionatoId) {
        this.utenteSelezionatoId = utenteSelezionatoId;
    }

    /**
     * @return Returns the btnStampa.
     */
    public String getBtnStampa() {
        return btnStampa;
    }

    /**
     * @param btnStampa
     *            The btnStampa to set.
     */
    public void setBtnStampa(String btnStampa) {
        this.btnStampa = btnStampa;
    }

    /**
     * @return Returns the dataFine.
     */
    public String getDataFine() {
        return dataFine;
    }

    /**
     * @param dataFine
     *            The dataFine to set.
     */
    public void setDataFine(String dataFine) {
        this.dataFine = dataFine;
    }

    /**
     * @return Returns the dataInizio.
     */
    public String getDataInizio() {
        return dataInizio;
    }

    /**
     * @param dataInizio
     *            The dataInizio to set.
     */
    public void setDataInizio(String dataInizio) {
        this.dataInizio = dataInizio;
    }

    /**
     * @return Returns the pathUffici.
     */
    public Collection<UfficioVO> getPathUffici() {
        return pathUffici;
    }

    /**
     * @param pathUffici
     *            The pathUffici to set.
     */
    public void setPathUffici(Collection<UfficioVO> pathUffici) {
        this.pathUffici = pathUffici;
    }

    /**
     * @return Returns the ufficiDipendenti.
     */
    public Collection<UfficioVO> getUfficiDipendenti() {
        return ufficiDipendenti;
    }

    /**
     * @param ufficiDipendenti
     *            The ufficiDipendenti to set.
     */
    public void setUfficiDipendenti(Collection<UfficioVO> ufficiDipendenti) {
        this.ufficiDipendenti = ufficiDipendenti;
    }

    /**
     * @return Returns the ufficioCorrenteId.
     */
    public int getUfficioCorrenteId() {
        return ufficioCorrenteId;
    }

    /**
     * @param ufficioCorrenteId
     *            The ufficioCorrenteId to set.
     */
    public void setUfficioCorrenteId(int ufficioCorrenteId) {
        this.ufficioCorrenteId = ufficioCorrenteId;
    }

    /**
     * @return Returns the ufficioSelezionatoId.
     */
    public int getUfficioSelezionatoId() {
        return ufficioSelezionatoId;
    }

    /**
     * @param ufficioSelezionatoId
     *            The ufficioSelezionatoId to set.
     */
    public void setUfficioSelezionatoId(int ufficioSelezionatoId) {
        this.ufficioSelezionatoId = ufficioSelezionatoId;
    }

    public abstract ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request);

}