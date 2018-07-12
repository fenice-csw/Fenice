package it.finsiel.siged.mvc.presentation.actionform.protocollo;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.vo.lookup.TitolarioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.util.DateUtil;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class RicercaEvidenzaForm extends ActionForm implements
        AlberoUfficiUtentiForm {
    static Logger logger = Logger
            .getLogger(RicercaEvidenzaForm.class.getName());

    private int ufficioResponsabileId;

    private int utenteResponsabileId;

    private String dataEvidenzaDa;

    private String dataEvidenzaA;

    private TitolarioVO titolario;

    private int titolarioPrecedenteId;

    private int titolarioSelezionatoId;

    private Collection titolariFigli;

    private int ufficioCorrenteId;

    private String ufficioCorrentePath;

    private int ufficioSelezionatoId;

    private int utenteSelezionatoId;

    private Collection ufficiDipendenti;

    private UfficioVO ufficioCorrente;

    private Collection utenti;

    private int aooId;

    private int ufficioRicercaId;

    private Utente utenteCorrente;

    private String fascicoliProcedimenti = "F";

    // private String referente;

    // public String getReferente() {
    // return referente;
    // }
    //
    // public void setReferente(String referente) {
    // this.referente = referente;
    // }

    private Collection evidenzeProcedimenti;

    private Collection evidenzeFascicoli;

    private ArrayList elencoEvidenze = new ArrayList();

    public Utente getUtenteCorrente() {
        return utenteCorrente;
    }

    public void setUtenteCorrente(Utente utenteCorrente) {
        this.utenteCorrente = utenteCorrente;
    }

    public int getUfficioRicercaId() {
        return ufficioRicercaId;
    }

    public void setUfficioRicercaId(int ufficioRicercaId) {
        this.ufficioRicercaId = ufficioRicercaId;
    }

    public int getAooId() {
        return aooId;
    }

    public void setAooId(int aooId) {
        this.aooId = aooId;
    }

    public void inizializzaForm() {
        setDataEvidenzaA(null);
        setDataEvidenzaDa(null);
        setTitolariFigli(null);
        setTitolario(null);
        setTitolarioPrecedenteId(0);
        setTitolarioSelezionatoId(0);
        setUfficioCorrenteId(0);
        setFascicoliProcedimenti("F");
    }

    public Collection getUtenti() {
        return utenti;
    }

    public void setUtenti(Collection utenti) {
        this.utenti = utenti;
    }

    public Collection getTitolariFigli() {
        return titolariFigli;
    }

    public void setTitolariFigli(Collection titolariFigli) {
        this.titolariFigli = titolariFigli;
    }

    public TitolarioVO getTitolario() {
        return titolario;
    }

    public void setTitolario(TitolarioVO titolario) {
        this.titolario = titolario;
    }

    public int getTitolarioPrecedenteId() {
        return titolarioPrecedenteId;
    }

    public void setTitolarioPrecedenteId(int titolarioPrecedenteId) {
        this.titolarioPrecedenteId = titolarioPrecedenteId;
    }

    public int getTitolarioSelezionatoId() {
        return titolarioSelezionatoId;
    }

    public void setTitolarioSelezionatoId(int titolarioSelezionatoId) {
        this.titolarioSelezionatoId = titolarioSelezionatoId;
    }

    public int getUfficioResponsabileId() {
        return ufficioResponsabileId;
    }

    public void setUfficioResponsabileId(int ufficioResponsabileId) {
        this.ufficioResponsabileId = ufficioResponsabileId;
    }

    public int getUtenteResponsabileId() {
        return utenteResponsabileId;
    }

    public void setUtenteResponsabileId(int utenteResponsabileId) {
        this.utenteResponsabileId = utenteResponsabileId;
    }

    public String getUfficioCorrentePath() {
        return ufficioCorrentePath;
    }

    public void setUfficioCorrentePath(String ufficioCorrentePath) {
        this.ufficioCorrentePath = ufficioCorrentePath;
    }

    public int getUfficioSelezionatoId() {
        return ufficioSelezionatoId;
    }

    public void setUfficioSelezionatoId(int ufficioCorrenteId) {
        this.ufficioSelezionatoId = ufficioCorrenteId;
    }

    public int getUtenteSelezionatoId() {
        return utenteSelezionatoId;
    }

    public void setUtenteSelezionatoId(int utenteCorrenteId) {
        this.utenteSelezionatoId = utenteCorrenteId;
    }

    public UfficioVO getUfficioCorrente() {
        return ufficioCorrente;
    }

    public void setUfficioCorrente(UfficioVO ufficioCorrente) {
        this.ufficioCorrente = ufficioCorrente;
    }

    public String getIntestatario() {
        Organizzazione org = Organizzazione.getInstance();
        Ufficio uff = org.getUfficio(getUfficioCorrenteId());
        Utente ute = org.getUtente(getUtenteSelezionatoId());
        return uff.getPath() + ute.getValueObject().getFullName();
    }

    public int getUfficioCorrenteId() {
        return ufficioCorrenteId;
    }

    public void setUfficioCorrenteId(int ufficioCorrenteId) {
        this.ufficioCorrenteId = ufficioCorrenteId;
    }

    public String getDataEvidenzaDa() {
        return dataEvidenzaDa;
    }

    public void setDataEvidenzaDa(String dataEvidenzaDa) {
        this.dataEvidenzaDa = dataEvidenzaDa;
    }

    public String getDataEvidenzaA() {
        return dataEvidenzaA;
    }

    public void setDataEvidenzaA(String dataEvidenzaA) {
        this.dataEvidenzaA = dataEvidenzaA;
    }

    public Collection getUfficiDipendenti() {
        return ufficiDipendenti;
    }

    public void setUfficiDipendenti(Collection ufficiDipendenti) {
        this.ufficiDipendenti = ufficiDipendenti;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.utenteCorrente = (Utente) request.getSession().getAttribute(
                Constants.UTENTE_KEY);
    }

    public boolean isTuttiUffici() {
        if (getUfficioCorrente() == null)
            return false;
        boolean tutti = getUfficioCorrente().getParentId() == 0;
        if (!tutti) {
            tutti = getUfficioCorrente().getId().equals(
                    utenteCorrente.getUfficioVOInUso().getId());
        }
        return tutti;
    }

    public ArrayList getElencoEvidenze() {
        return elencoEvidenze;
    }

    public void setElencoEvidenze(ArrayList elencoEvidenze) {
        this.elencoEvidenze = elencoEvidenze;
    }

    public Collection getEvidenzeProcedimenti() {
        return evidenzeProcedimenti;
    }

    public void setEvidenzeProcedimenti(Collection evidenzeProcedimenti) {
        this.evidenzeProcedimenti = evidenzeProcedimenti;
    }

    public Collection getEvidenzeFascicoli() {
        return evidenzeFascicoli;
    }

    public void setEvidenzeFascicoli(Collection evidenzeFascicoli) {
        this.evidenzeFascicoli = evidenzeFascicoli;
    }

    public String getFascicoliProcedimenti() {
        return fascicoliProcedimenti;
    }

    public void setFascicoliProcedimenti(String fascicoliProcedimenti) {
        this.fascicoliProcedimenti = fascicoliProcedimenti;
    }

    public void reset(ActionMapping arg0, ServletRequest arg1) {
        setFascicoliProcedimenti("F");
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        String dataEvidenzaDa = getDataEvidenzaDa();
        String dataEvidenzaA = getDataEvidenzaA();

        if (dataEvidenzaDa != null && !"".equals(dataEvidenzaDa)) {
            if (!DateUtil.isData(dataEvidenzaDa)) {
                errors.add("dataEvidenzaDa", new ActionMessage(
                        "formato.data.errato", "Data Evidenza Da"));
            }
        }
        if (dataEvidenzaA != null && !"".equals(dataEvidenzaA)) {
            if (!DateUtil.isData(dataEvidenzaA)) {
                errors.add("dataEvidenzaA", new ActionMessage(
                        "formato.data.errato", "Data Evidenza A"));
            }
        }
        if (dataEvidenzaDa != null && !"".equals(dataEvidenzaDa)
                && dataEvidenzaA != null && !"".equals(dataEvidenzaA)) {
            // la data di ricezione non deve essere successiva a quella
            // di registrazione
            if (DateUtil.toDate(dataEvidenzaA).before(
                    DateUtil.toDate(dataEvidenzaDa))) {
                errors.add("dataEvidenzaDa", new ActionMessage(
                        "data_1.non.successiva.data_2", "Data Evidenza A",
                        "Data Evidenza Da"));
            }
        }

        return errors;
    }

}