package it.finsiel.siged.mvc.presentation.actionform.amministrazione;

import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;

public class ReportProtocolliDaScartareForm extends ActionForm {

    private Collection uffici;

    private int ufficioSelezionatoId;

    private String ufficioSelezionato;

    private String ufficioCorrentePath;

    private Collection servizi;

    private int servizioSelezionatoId;

    private int ufficioCorrenteId;

    private String servizioSelezionato;

    private int anno;

    private int massimario;

    private int aooId;

    private Utente utenteCorrente;

    private Collection protocolliDaScartare;

    private Collection anniScartabili;

    private int risultatiScarto;

    private String dataRegistrazioneDa;

    private String dataRegistrazioneA;

    static Logger logger = Logger.getLogger(ScartoProtocolliForm.class
            .getName());

    // tipo protocollo e stato
    private String tipoProtocollo = "";

    private Collection statiProtocollo;

    private String statoProtocollo = "";

    private String riservato;

    // liste

    private SortedMap protocolli = new TreeMap();

    public SortedMap getProtocolli() {
        return protocolli;
    }

    public int getUfficioCorrenteId() {
        return ufficioCorrenteId;
    }

    public void setUfficioCorrenteId(int ufficioCorrenteId) {
        this.ufficioCorrenteId = ufficioCorrenteId;
    }

    public String getUfficioCorrentePath() {
        return ufficioCorrentePath;
    }

    public void setUfficioCorrentePath(String ufficioCorrentePath) {
        this.ufficioCorrentePath = ufficioCorrentePath;
    }

    public int getMassimario() {
        return massimario;
    }

    public void setMassimario(int massimario) {
        this.massimario = massimario;
    }

    public Collection getProtocolliCollection() {
        if (protocolli != null) {
            return protocolli.values();
        } else
            return null;
    }

    public String getNumeroProtocolli() {
        if (protocolli != null) {
            return protocolli.values().size() + "";
        } else
            return "0";

    }

    public ReportProtocolloView getProtocolloView(Integer protocolloId) {
        return (ReportProtocolloView) protocolli.get(protocolloId);
    }

    /**
     * @param protocolliInCarico
     *            The protocolliInCarico to set.
     */
    public void setProtocolli(SortedMap protocolli) {
        this.protocolli = protocolli;
    }

    /**
     * @return Returns the tipoProtocollo.
     */
    public String getTipoProtocollo() {
        return tipoProtocollo;
    }

    /**
     * @param tipoProtocollo
     *            The tipoProtocollo to set.
     */
    public void setTipoProtocollo(String tipoProtocollo) {
        this.tipoProtocollo = tipoProtocollo;
    }

    public String getDataRegistrazioneA() {
        return dataRegistrazioneA;
    }

    public void setDataRegistrazioneA(String dataRegistrazioneA) {
        this.dataRegistrazioneA = dataRegistrazioneA;
    }

    public String getDataRegistrazioneDa() {
        return dataRegistrazioneDa;
    }

    public void setDataRegistrazioneDa(String dataRegistrazioneDa) {
        this.dataRegistrazioneDa = dataRegistrazioneDa;
    }

    public Utente getUtenteCorrente() {
        return utenteCorrente;
    }

    public void setUtenteCorrente(Utente utenteCorrente) {
        this.utenteCorrente = utenteCorrente;
    }

    public int getAooId() {
        return aooId;
    }

    public void setAooId(int aooId) {
        this.aooId = aooId;
    }

    public Collection getServizi() {
        return servizi;
    }

    public void setServizi(Collection servizi) {
        this.servizi = servizi;
    }

    public int getServizioSelezionatoId() {
        return servizioSelezionatoId;
    }

    public void setServizioSelezionatoId(int servizioSelezionatoId) {
        this.servizioSelezionatoId = servizioSelezionatoId;
    }

    public Collection getUffici() {
        return uffici;
    }

    public void setUffici(Collection uffici) {
        this.uffici = uffici;
    }

    public int getUfficioSelezionatoId() {
        return ufficioSelezionatoId;
    }

    public void setUfficioSelezionatoId(int ufficioSelezionatoId) {
        this.ufficioSelezionatoId = ufficioSelezionatoId;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public Collection getAnniScartabili() {
        return anniScartabili;
    }

    public void setAnniScartabili(Collection anniScartabili) {
        this.anniScartabili = anniScartabili;
    }

    public Collection getProtocolliDaScartare() {
        return protocolliDaScartare;
    }

    public void setProtocolliDaScartare(Collection protocolliDaScartare) {
        this.protocolliDaScartare = protocolliDaScartare;
    }

    public int getRisultatiScarto() {
        return risultatiScarto;
    }

    public void setRisultatiScarto(int risultatiScarto) {
        this.risultatiScarto = risultatiScarto;
    }

    public void inizializzaForm() {
        setAooId(0);
        setUffici(null);
        setUfficioSelezionatoId(0);
        setUtenteCorrente(null);
        setServizioSelezionatoId(0);
        setServizi(new ArrayList());
    }

    public String getServizioSelezionato() {
        return servizioSelezionato;
    }

    public void setServizioSelezionato(String servizioSelezionato) {
        this.servizioSelezionato = servizioSelezionato;
    }

    public Collection getStatiProtocollo() {
        return statiProtocollo;
    }

    public void setStatiProtocollo(Collection statiProtocollo) {
        this.statiProtocollo = statiProtocollo;
    }

    public String getStatoProtocollo() {
        return statoProtocollo;
    }

    public void setStatoProtocollo(String statoProtocollo) {
        this.statoProtocollo = statoProtocollo;
    }

    public String getUfficioSelezionato() {
        return ufficioSelezionato;
    }

    public void setUfficioSelezionato(String ufficioSelezionato) {
        this.ufficioSelezionato = ufficioSelezionato;
    }
}