package it.finsiel.siged.mvc.presentation.actionform.protocollo;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.UtenteDelegate;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.vo.lookup.TitolarioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class ConfigurazioneUtenteForm extends ActionForm implements
        AlberoUfficiUtentiForm {

    static Logger logger = Logger.getLogger(ConfigurazioneUtenteForm.class
            .getName());

    private String tipoDocumento;

    private String tipoMittente;

    private Map assegnatari = new HashMap(); // di AssegnatarioView

    private String[] assegnatariSelezionatiId;

    private String assegnatarioCompetente;

    private TitolarioVO titolario;

    private int utenteId;

    private int titolarioId;

    private int titolarioSelezionatoId;

    private Collection tipiDocumento;

    private String dataDocumento;

    private String dataRicezione;

    private int tipoDocumentoId;

    private UtenteVO utente;

    private Boolean checkTipoDocumento;

    private Boolean checkDataDocumento;

    private Boolean checkRicevuto;

    private Boolean checkTipoMittente;

    private Boolean checkMittente;

    private Boolean checkAssegnatari;

    private Boolean checkDestinatari;

    private Boolean checkTitolario;

    private Boolean checkOggetto;

    private String username;

    private String assegnatario;

    private int assegnatarioUfficioId;

    private int assegnatarioUtenteId;

    private String destinatario;

    private int titolarioPrecedenteId;

    private Collection titolariFigli;

    private int ufficioCorrenteId;

    private int utenteSelezionatoId;

    private Collection utenti;

    private Collection ufficiDipendenti;

    private UfficioVO ufficioCorrente;

    private String ufficioCorrentePath;

    private int ufficioSelezionatoId;

    private String oggetto;

    private String mittente;

    private String parametriStampante;

    public String getMittente() {
        return mittente;
    }

    public void setMittente(String mittente) {
        this.mittente = mittente;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public UtenteVO getUtente() {
        return utente;
    }

    public int getAssegnatarioUfficioId() {
        return assegnatarioUfficioId;
    }

    public void setAssegnatarioUfficioId(int assegnatarioUfficioId) {
        this.assegnatarioUfficioId = assegnatarioUfficioId;
    }

    public int getAssegnatarioUtenteId() {
        return assegnatarioUtenteId;
    }

    public void setAssegnatarioUtenteId(int assegnatarioUtenteId) {
        this.assegnatarioUtenteId = assegnatarioUtenteId;
    }

    public void aggiungiAssegnatario(AssegnatarioView ass) {
        assegnatari.put(ass.getKey(), ass);
    }

    public void rimuoviAssegnatario(String key) {
        assegnatari.remove(key);
    }

    public UtenteVO getUtente(int utenteId) {
        return UtenteDelegate.getInstance().getUtente(utenteId);
    }

    public void setUtente(UtenteVO utente) {
        this.utente = utente;
    }

    public int getTitolarioSelezionatoId() {
        return titolarioSelezionatoId;
    }

    public void setTitolarioSelezionatoId(int titolarioSelezionatoId) {
        this.titolarioSelezionatoId = titolarioSelezionatoId;
    }

    public String getUfficioCorrentePath() {
        return ufficioCorrentePath;
    }

    public void setUfficioCorrentePath(String ufficioCorrentePath) {
        this.ufficioCorrentePath = ufficioCorrentePath;
    }

    public int getUfficioCorrenteId() {
        return ufficioCorrenteId;
    }

    public void setUfficioCorrenteId(int ufficioCorrenteId) {
        this.ufficioCorrenteId = ufficioCorrenteId;
    }

    public int getTitolarioId() {
        return titolarioId;
    }

    public void setTitolarioId(int titolarioId) {
        this.titolarioId = titolarioId;
    }

    public Collection getTitolariFigli() {
        return titolariFigli;
    }

    public void setTitolariFigli(Collection titolariDiscendenti) {
        this.titolariFigli = titolariDiscendenti;
    }

    public int getTitolarioPrecedenteId() {
        return titolarioPrecedenteId;
    }

    public void setTitolarioPrecedenteId(int titolarioPrecedenteId) {
        this.titolarioPrecedenteId = titolarioPrecedenteId;
    }

    public String getAssegnatario() {
        return assegnatario;
    }

    public void setAssegnatario(String assegnatario) {
        this.assegnatario = assegnatario;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getDataRicezione() {
        return dataRicezione;
    }

    public void setDataRicezione(String dataRicezione) {
        this.dataRicezione = dataRicezione;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTipoDocumentoId() {
        return tipoDocumentoId;
    }

    public void setTipoDocumentoId(int tipoDocumentoId) {
        this.tipoDocumentoId = tipoDocumentoId;
    }

    public Collection getTipiDocumento() {
        return tipiDocumento;
    }

    public void setTipiDocumento(Collection tipiDocumento) {
        this.tipiDocumento = tipiDocumento;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        ConfigurazioneUtenteForm.logger = logger;
    }

    public String getDataDocumento() {
        return dataDocumento;
    }

    public void setDataDocumento(String dataDocumento) {
        this.dataDocumento = dataDocumento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getTipoMittente() {
        return tipoMittente;
    }

    public void setTipoMittente(String tipoMittente) {
        this.tipoMittente = tipoMittente;
    }

    public TitolarioVO getTitolario() {
        return titolario;
    }

    public void setTitolario(TitolarioVO titolario) {
        this.titolario = titolario;
    }

    public int getUtenteId() {
        return utenteId;
    }

    public void setUtenteId(int utenteId) {
        this.utenteId = utenteId;
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

    public int getUfficioSelezionatoId() {
        return ufficioSelezionatoId;
    }

    public void setUfficioSelezionatoId(int ufficioSelezionatoId) {
        this.ufficioSelezionatoId = ufficioSelezionatoId;
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

    public String[] getAssegnatariSelezionatiId() {
        return assegnatariSelezionatiId;
    }

    public void setAssegnatariSelezionatiId(String[] assegnatari) {
        this.assegnatariSelezionatiId = assegnatari;
    }

    public String getAssegnatarioCompetente() {
        return assegnatarioCompetente;
    }

    public Boolean getCheckAssegnatari() {
        return checkAssegnatari;
    }

    public void setCheckAssegnatari(Boolean checkAssegnatari) {
        this.checkAssegnatari = checkAssegnatari;
    }

    public Boolean getCheckDataDocumento() {
        return checkDataDocumento;
    }

    public void setCheckDataDocumento(Boolean checkDataDocumento) {
        this.checkDataDocumento = checkDataDocumento;
    }

    public Boolean getCheckDestinatari() {
        return checkDestinatari;
    }

    public void setCheckDestinatari(Boolean checkDestinatari) {
        this.checkDestinatari = checkDestinatari;
    }

    public Boolean getCheckMittente() {
        return checkMittente;
    }

    public void setCheckMittente(Boolean checkMittente) {
        this.checkMittente = checkMittente;
    }

    public Boolean getCheckOggetto() {
        return checkOggetto;
    }

    public void setCheckOggetto(Boolean checkOggetto) {
        this.checkOggetto = checkOggetto;
    }

    public Boolean getCheckRicevuto() {
        return checkRicevuto;
    }

    public void setCheckRicevuto(Boolean checkRicevuto) {
        this.checkRicevuto = checkRicevuto;
    }

    public Boolean getCheckTipoDocumento() {
        return checkTipoDocumento;
    }

    public void setCheckTipoDocumento(Boolean checkTipoDocumento) {
        this.checkTipoDocumento = checkTipoDocumento;
    }

    public Boolean getCheckTipoMittente() {
        return checkTipoMittente;
    }

    public void setCheckTipoMittente(Boolean checkTipoMittente) {
        this.checkTipoMittente = checkTipoMittente;
    }

    public Boolean getCheckTitolario() {
        return checkTitolario;
    }

    public void setCheckTitolario(Boolean checkTitolario) {
        this.checkTitolario = checkTitolario;
    }

    public String getParametriStampante() {
        return parametriStampante;
    }

    public void setParametriStampante(String parametriStampante) {
        this.parametriStampante = parametriStampante;
    }

    public void inizializzaForm() {
        setOggetto(null);
        setDataDocumento(null);
        setDestinatario(null);
        setMittente(null);
        setTipoDocumento(null);
        setTipoDocumentoId(0);
        setTipoMittente("F");
        setTitolario(null);
        setTitolarioId(0);
        setCheckAssegnatari(Boolean.FALSE);
        setCheckDataDocumento(Boolean.FALSE);
        setCheckDestinatari(Boolean.FALSE);
        setCheckMittente(Boolean.FALSE);
        setCheckOggetto(Boolean.FALSE);
        setCheckRicevuto(Boolean.FALSE);
        setCheckTipoDocumento(Boolean.TRUE);
        setCheckTipoMittente(Boolean.FALSE);
        setCheckTitolario(Boolean.FALSE);
        setParametriStampante(null);
    }

    public Collection getAssegnatari() {
        return assegnatari.values();
    }

    public void setAssegnatari(Map assegnatari) {
        this.assegnatari = assegnatari;
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (request.getParameter("btnSalva") != null) {
            if (getOggetto() != null && !"".equals(getOggetto().trim())
                    && getCheckOggetto() == Boolean.FALSE) {
                errors.add("check", new ActionMessage("selezione.obbligatoria",
                        "il check oggetto", " in presenza del valore"));
            }
            // if (getDataDocumento() != null
            // && getCheckDataDocumento() == Boolean.FALSE) {
            // errors.add("check", new ActionMessage("selezione.obbligatoria",
            // "il check data documento", " in presenza del valore"));
            // }
            // if (getDataRicezione() != null
            // && getCheckRicevuto() == Boolean.FALSE) {
            // errors.add("check", new ActionMessage("selezione.obbligatoria",
            // "il check data ricevuto", " in presenza del valore"));
            // }
            if (getMittente() != null && !"".equals(getMittente().trim())
                    && getCheckMittente() == Boolean.FALSE) {
                errors.add("check", new ActionMessage("selezione.obbligatoria",
                        "il check mittente", " in presenza del valore"));
            }
            if (getDestinatario() != null
                    && !"".equals(getDestinatario().trim())
                    && getCheckDestinatari() == Boolean.FALSE) {
                errors.add("check", new ActionMessage("selezione.obbligatoria",
                        "il check destinatario", " in presenza del valore"));
            }

            if ((getAssegnatarioUfficioId() > 0 || getAssegnatarioUtenteId() > 0)
                    && getCheckAssegnatari() == Boolean.FALSE) {
                errors.add("check", new ActionMessage("selezione.obbligatoria",
                        "il check Assegnatari",
                        " in presenza dell'assegnatario"));
            }

            Utente utente = (Utente) request.getSession().getAttribute(
                    Constants.UTENTE_KEY);
            if (getTitolario() != null) {
                if (getCheckTitolario() == Boolean.FALSE) {
                    errors.add("check", new ActionMessage(
                            "selezione.obbligatoria", "il check Titolario",
                            " in presenza del Titolario"));
                } else if (utente.getAreaOrganizzativa()
                        .getDipendenzaTitolarioUfficio() == 1) {
                    errors.add("titolario", new ActionMessage(
                            "campo.obbligatorio", "Titolario", ""));
                }

                if (getTitolario().getParentId() == 0
                        && utente.getAreaOrganizzativa()
                                .getTitolarioLivelloMinimo() > 1) {
                    errors.add("titolario", new ActionMessage(
                            "fascicolo.titolario.livello", "", ""));
                }

            } else {
                if (getCheckTitolario() == Boolean.TRUE) {
                    errors.add("titolario", new ActionMessage(
                            "campo.obbligatorio", "Titolario", ""));
                }
            }

        }
        return errors;
    }

    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        setCheckAssegnatari(Boolean.FALSE);
        setCheckDataDocumento(Boolean.FALSE);
        setCheckDestinatari(Boolean.FALSE);
        setCheckMittente(Boolean.FALSE);
        setCheckOggetto(Boolean.FALSE);
        setCheckRicevuto(Boolean.FALSE);
        setCheckTipoDocumento(Boolean.FALSE);
        setCheckTipoMittente(Boolean.FALSE);
        setCheckTitolario(Boolean.FALSE);

    }

}