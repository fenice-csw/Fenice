package it.finsiel.siged.mvc.presentation.actionform.protocollo;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.vo.lookup.TitolarioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class RicercaFaldoneForm extends ActionForm implements
        AlberoUfficiUtentiForm {
    static Logger logger = Logger.getLogger(RicercaFaldoneForm.class.getName());

    private String dataCreazioneInizio;

    private String dataCreazioneFine;

    private String sottocategoria;

    private String anno;

    private String numero;

    private String nota;

    private String oggetto;

    private int aooId;

    private String codiceLocale;

    private Collection faldoni = new ArrayList();

    private String[] faldoniSelezionati = null;

    private int ufficioCorrenteId;

    private String ufficioCorrentePath;

    private int ufficioSelezionatoId;

    private int utenteSelezionatoId;

    private UfficioVO ufficioCorrente;

    private Collection ufficiDipendenti;

    private Collection utenti;

    private TitolarioVO titolario;

    private int titolarioPrecedenteId;

    private int titolarioSelezionatoId;

    private Collection titolariFigli;
    
    private boolean indietroVisibile;
    
    /* Modifiche Greco del 28 marzo */
    private int ufficioRicercaId;
    private Utente utenteCorrente;

    
    public int getUfficioRicercaId() {
        return ufficioRicercaId;
    }

    public void setUfficioRicercaId(int ufficioRicercaId) {
        this.ufficioRicercaId = ufficioRicercaId;
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
    
    /* ************************* */


    public RicercaFaldoneForm() {
    }

    public String getAnno() {
        return anno;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public int getAooId() {
        return aooId;
    }

    public void setAooId(int aooId) {
        this.aooId = aooId;
    }

    public String getCodiceLocale() {
        return codiceLocale;
    }

    public void setCodiceLocale(String codiceLocale) {
        this.codiceLocale = codiceLocale;
    }

    public String getDataCreazioneFine() {
        return dataCreazioneFine;
    }

    public void setDataCreazioneFine(String dataCreazioneFine) {
        this.dataCreazioneFine = dataCreazioneFine;
    }

    public String getDataCreazioneInizio() {
        return dataCreazioneInizio;
    }

    public void setDataCreazioneInizio(String dataCreazioneInizio) {
        this.dataCreazioneInizio = dataCreazioneInizio;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public String getSottocategoria() {
        return sottocategoria;
    }

    public void setSottocategoria(String sottocategoria) {
        this.sottocategoria = sottocategoria;
    }

    public Collection getFaldoni() {
        return faldoni;
    }

    public void setFaldoni(Collection faldoni) {
        this.faldoni = faldoni;
    }

    public int getUtenteSelezionatoId() {
        return utenteSelezionatoId;
    }

    public void setUtenteSelezionatoId(int utenteSelezionatoId) {
        this.utenteSelezionatoId = utenteSelezionatoId;
    }

    public Collection getUtenti() {
        return utenti;
    }

    public void setUtenti(Collection utenti) {
        this.utenti = utenti;
    }

    public Collection getUfficiDipendenti() {
        return ufficiDipendenti;
    }

    public void setUfficiDipendenti(Collection ufficiDipendenti) {
        this.ufficiDipendenti = ufficiDipendenti;
    }

    public UfficioVO getUfficioCorrente() {
        return ufficioCorrente;
    }

    public void setUfficioCorrente(UfficioVO ufficioCorrente) {
        this.ufficioCorrente = ufficioCorrente;
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

    public int getUfficioSelezionatoId() {
        return ufficioSelezionatoId;
    }

    public void setUfficioSelezionatoId(int ufficioSelezionatoId) {
        this.ufficioSelezionatoId = ufficioSelezionatoId;
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

    public void inizializzaForm() {
        setTitolariFigli(null);
        setTitolario(null);
        setTitolarioPrecedenteId(0);
        setTitolarioSelezionatoId(0);
        setUfficiDipendenti(null);
        setUfficioCorrente(null);
        setUfficioCorrenteId(0);
        setUfficioCorrentePath(null);
        setUfficioSelezionatoId(0);
        setUtenteSelezionatoId(0);
        setUtenti(null);
        setFaldoni(null);
        setFaldoniSelezionati(null);
        setAnno(null);
        setNumero(null);
        setOggetto(null);
        setCodiceLocale(null);
        setSottocategoria(null);
        setNota(null);
        setDataCreazioneInizio(null);
        setDataCreazioneFine(null);
        
    }
    public void reset() {
        setFaldoni(null);
        setAnno(null);
        setNumero(null);
        setOggetto(null);
        setCodiceLocale(null);
        setSottocategoria(null);
        setNota(null);
        setDataCreazioneInizio(null);
        setDataCreazioneFine(null);
        
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (request.getParameter("btnCerca") != null) {

        }
        return errors;
    }

    public String[] getFaldoniSelezionati() {
        return faldoniSelezionati;
    }

    public void setFaldoniSelezionati(String[] faldoniSelezionati) {
        this.faldoniSelezionati = faldoniSelezionati;
    }

    public boolean isIndietroVisibile() {
        return indietroVisibile;
    }

    public void setIndietroVisibile(boolean indietroVisibile) {
        this.indietroVisibile = indietroVisibile;
    }

}