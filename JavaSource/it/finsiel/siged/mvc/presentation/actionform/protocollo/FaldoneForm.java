package it.finsiel.siged.mvc.presentation.actionform.protocollo;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.Fascicolo;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.FaldoneDelegate;
import it.finsiel.siged.mvc.vo.ImpiegatoVO;
import it.finsiel.siged.mvc.vo.lookup.TitolarioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.protocollo.ProcedimentoVO;
import it.finsiel.siged.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class FaldoneForm extends ActionForm implements AlberoUfficiUtentiForm {

    public final static String CANCELLAZIONE_FALDONE = "Cancellazione";

    public final static String CHIUSURA_FALDONE = "Chiusura";

    public final static String RIAPERTURA_FALDONE = "Riapertura";

    final static int STATO_FALDONE_TRATTAZIONE = 0;

    final static int STATO_FALDONE_ATTI = 1;

    final static int STATO_FALDONE_EVIDENZA = 2;

    static Logger logger = Logger.getLogger(FaldoneForm.class.getName());

    private int faldoneId;

    private int aooId;

    private int dipendenzaTitolarioUfficio;

    private int ufficioId;

    private int titolarioId;

    private String numeroFaldone;

    private String oggetto;

    private String codiceLocale;

    private String sottoCategoria;

    private String operazione;

    private String nota;

    private String notaFaldone;

    private Collection faldoni;

    private String description;

    private int annoCorrente;

    private int numero;

    private int numeroMatricole;

    // private Map fascicoliFaldone = new HashMap(2); // tipo V=Fascicolo

    SortedMap fascicoliFaldone = new TreeMap(new Comparator() {
        public int compare(Object o1, Object o2) {
            Integer i1 = (Integer) o1;
            Integer i2 = (Integer) o2;
            return i2.intValue() - i1.intValue();
        }
    });

    // private Map procedimentiFaldone = new HashMap(2); // tipo

    SortedMap procedimentiFaldone = new TreeMap(new Comparator() {
        public int compare(Object o1, Object o2) {
            Integer i1 = (Integer) o1;
            Integer i2 = (Integer) o2;
            return i2.intValue() - i1.intValue();
        }
    });

    // Modifiche 28/02/06

    private Map posizioniFaldone = new HashMap(3);

    private int posizioneSelezionata;

    private String posizioneSelezionataStoria;

    private String statoFaldone;

    private String dataCarico;

    private String dataScarico;

    private String dataEvidenza;

    private String dataApertura;

    private String dataChiusura;

    private String dataMovimento;

    private Date rowCreatedTime;

    private Date rowUpdatedTime;

    private String rowCreatedUser;

    private String rowUpdatedUser;

    int versione;

    private int annoProgressivo;

    private boolean versioneDefault;

    private Collection matricole;

    private String matricola;

    private ImpiegatoVO impegato;

    // Fine modifiche 28/02/06

    // V=ProcedimentoVO

    private String[] fascicoliSelezionati;

    private String[] procedimentiSelezionati;

    private String dataCreazione;

    // gestione uffici-titolario
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

    // ricerche:

    private String cercaFascicoloNome;

    private String cercaProcedimentoNome;

    private boolean modificabile = true;

    // inizio modifiche 23/03/2006
    private String collocazioneLabel1;

    private String collocazioneLabel2;

    private String collocazioneLabel3;

    private String collocazioneLabel4;

    private String collocazioneValore1;

    private String collocazioneValore2;

    private String collocazioneValore3;

    private String collocazioneValore4;

    // fine modifiche 23/03/2006

    public boolean isVersioneDefault() {
        return versioneDefault;
    }

    public void setVersioneDefault(boolean versioneDefault) {
        this.versioneDefault = versioneDefault;
    }

    public int getAnnoProgressivo() {
        return annoProgressivo;
    }

    public void setAnnoProgressivo(int annoProgressivo) {
        this.annoProgressivo = annoProgressivo;
    }

    /**
     * @param modificabile
     *            The modificabile to set.
     */
    public void setModificabile(boolean modificabile) {
        this.modificabile = modificabile;
    }

    /**
     * @return Returns the modificabile.
     */
    public boolean isModificabile() {
        return modificabile;
    }

    public int getVersione() {
        return versione;
    }

    public void setVersione(int versione) {
        this.versione = versione;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Date getRowCreatedTime() {
        return rowCreatedTime;
    }

    public void setRowCreatedTime(Date rowCreatedTime) {
        this.rowCreatedTime = rowCreatedTime;
    }

    public Date getRowUpdatedTime() {
        return rowUpdatedTime;
    }

    public void setRowUpdatedTime(Date rowUpdatedTime) {
        this.rowUpdatedTime = rowUpdatedTime;
    }

    public String getRowUpdatedUser() {
        return rowUpdatedUser;
    }

    public void setRowUpdatedUser(String rowUpdatedUser) {
        this.rowUpdatedUser = rowUpdatedUser;
    }

    public String getRowCreatedUser() {
        return rowCreatedUser;
    }

    public void setRowCreatedUser(String rowCreatedUser) {
        this.rowCreatedUser = rowCreatedUser;
    }

    public String getStatoFaldone() {
        return statoFaldone;
    }

    public void setStatoFaldone(String statoFaldone) {
        this.statoFaldone = statoFaldone;
    }

    public String getDataMovimento() {
        return dataMovimento;
    }

    public void setDataMovimento(String dataMovimento) {
        this.dataMovimento = dataMovimento;
    }

    public String getDataChiusura() {
        return dataChiusura;
    }

    public void setDataChiusura(String dataChiusura) {
        this.dataChiusura = dataChiusura;
    }

    public String getDataApertura() {
        return dataApertura;
    }

    public void setDataApertura(String dataApertura) {
        this.dataApertura = dataApertura;
    }

    public Collection getStatiFaldone() {
        return null;
    }

    public String getDataCarico() {
        return dataCarico;
    }

    public void setDataCarico(String dataCarico) {
        this.dataCarico = dataCarico;
    }

    public String getDataEvidenza() {
        return dataEvidenza;
    }

    public void setDataEvidenza(String dataEvidenza) {
        this.dataEvidenza = dataEvidenza;
    }

    public String getDataScarico() {
        return dataScarico;
    }

    public void setDataScarico(String dataScarico) {
        this.dataScarico = dataScarico;
    }

    public int getPosizioneSelezionata() {
        return posizioneSelezionata;
    }

    public void setPosizioneSelezionata(int posizioneSelezionata) {
        this.posizioneSelezionata = posizioneSelezionata;
    }

    public void setPosizioniFaldone(Map posizioniFaldone) {
        this.posizioniFaldone = posizioniFaldone;
    }

    public int getTitolarioId() {
        return titolarioId;
    }

    public void setTitolarioId(int titolarioId) {
        this.titolarioId = titolarioId;
    }

    public int getUfficioId() {
        return ufficioId;
    }

    public void setUfficioId(int ufficioId) {
        this.ufficioId = ufficioId;
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

    public String getCollocazioneLabel1() {
        return collocazioneLabel1;
    }

    public void setCollocazioneLabel1(String collocazioneLabel1) {
        this.collocazioneLabel1 = collocazioneLabel1;
    }

    public String getCollocazioneLabel2() {
        return collocazioneLabel2;
    }

    public void setCollocazioneLabel2(String collocazioneLabel2) {
        this.collocazioneLabel2 = collocazioneLabel2;
    }

    public String getCollocazioneLabel3() {
        return collocazioneLabel3;
    }

    public void setCollocazioneLabel3(String collocazioneLabel3) {
        this.collocazioneLabel3 = collocazioneLabel3;
    }

    public String getCollocazioneLabel4() {
        return collocazioneLabel4;
    }

    public void setCollocazioneLabel4(String collocazioneLabel4) {
        this.collocazioneLabel4 = collocazioneLabel4;
    }

    public String getCollocazioneValore1() {
        return collocazioneValore1;
    }

    public void setCollocazioneValore1(String collocazioneValore1) {
        this.collocazioneValore1 = collocazioneValore1;
    }

    public String getCollocazioneValore2() {
        return collocazioneValore2;
    }

    public void setCollocazioneValore2(String collocazioneValore2) {
        this.collocazioneValore2 = collocazioneValore2;
    }

    public String getCollocazioneValore3() {
        return collocazioneValore3;
    }

    public void setCollocazioneValore3(String collocazioneValore3) {
        this.collocazioneValore3 = collocazioneValore3;
    }

    public String getCollocazioneValore4() {
        return collocazioneValore4;
    }

    public void setCollocazioneValore4(String collocazioneValore4) {
        this.collocazioneValore4 = collocazioneValore4;
    }

    public Map getPosizioniFaldone() {
        return posizioniFaldone;
    }

    public String getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(String dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public String[] getFascicoliSelezionati() {
        return fascicoliSelezionati;
    }

    public void setFascicoliSelezionati(String[] fascicoliSelezionati) {
        this.fascicoliSelezionati = fascicoliSelezionati;
    }

    public SortedMap getFascicoliFaldone() {
        return fascicoliFaldone;
    }

    public Collection getFascicoliFaldoneCollection() {
        return fascicoliFaldone.values();
    }

    public int getAnnoCorrente() {
        return annoCorrente;
    }

    public void setAnnoCorrente(int annoCorrente) {
        this.annoCorrente = annoCorrente;
    }

    public Collection getFaldoni() {
        return faldoni;
    }

    public void setFaldoni(Collection faldoni) {
        this.faldoni = faldoni;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getFaldoneId() {
        return faldoneId;
    }

    public void setFaldoneId(int faldoneId) {
        this.faldoneId = faldoneId;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getNumeroFaldone() {
        return numeroFaldone;
    }

    public void setNumeroFaldone(String numeroFaldone) {
        this.numeroFaldone = numeroFaldone;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public String getSottoCategoria() {
        return sottoCategoria;
    }

    public void setSottoCategoria(String sottoCategoria) {
        this.sottoCategoria = sottoCategoria;
    }

    public void setFascicoliFaldone(SortedMap fascicoliFaldone) {
        this.fascicoliFaldone = fascicoliFaldone;
    }

    public void aggiungiFascicolo(Fascicolo fascicolo) {
        if (fascicolo != null) {
            this.fascicoliFaldone.put(fascicolo.getFascicoloVO().getId(),
                    fascicolo);
        }
    }

    public void rimuoviFascicolo(int fascicoloId) {
        this.fascicoliFaldone.remove(new Integer(fascicoloId));
    }

    public SortedMap getProcedimentiFaldone() {
        return procedimentiFaldone;
    }

    public void setProcedimentiFaldone(SortedMap procedimentiFaldone) {
        this.procedimentiFaldone = procedimentiFaldone;
    }

    public Collection getProcedimentiFaldoneCollection() {
        return this.procedimentiFaldone.values();
    }

    public void rimuoviProcedimento(int procedimentoId) {
        this.procedimentiFaldone.remove(String.valueOf(procedimentoId));
    }

    public void aggiungiProcedimento(ProcedimentoVO p) {
        if (p != null) {
            this.procedimentiFaldone.put(String.valueOf(p.getId().intValue()),
                    p);
        }
    }

    public String[] getProcedimentiSelezionati() {
        return procedimentiSelezionati;
    }

    public void setProcedimentiSelezionati(String[] procedimentiSelezionati) {
        this.procedimentiSelezionati = procedimentiSelezionati;
    }

    public String getCercaFascicoloNome() {
        return cercaFascicoloNome;
    }

    public void setCercaFascicoloNome(String cercaFascicoloNome) {
        this.cercaFascicoloNome = cercaFascicoloNome;
    }

    public String getCercaProcedimentoNome() {
        return cercaProcedimentoNome;
    }

    public void setCercaProcedimentoNome(String cercaProcedimentoNome) {
        this.cercaProcedimentoNome = cercaProcedimentoNome;
    }

    public String getOperazione() {
        return operazione;
    }

    public void setOperazione(String operazione) {
        this.operazione = operazione;
    }

    public void inizializzaForm() {
        setFaldoneId(0);
        setAooId(0);
        setCodiceLocale(null);
        setNota(null);
        setNumeroFaldone(null);
        setOggetto(null);
        setSottoCategoria(null);
        setTitolariFigli(null);
        setTitolario(null);
        setTitolarioId(0);
        setTitolarioPrecedenteId(0);
        setTitolarioSelezionatoId(0);
        setUfficiDipendenti(null);
        setUfficioCorrente(null);
        setUfficioCorrenteId(0);
        setUfficioCorrentePath(null);
        setUfficioId(0);
        setUfficioSelezionatoId(0);
        setUtenteSelezionatoId(0);
        setUtenti(null);
        setMatricole(null);
        setNumeroMatricole(0);
        fascicoliFaldone = new TreeMap();
        procedimentiFaldone = new TreeMap();
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (request.getParameter("salvaAction") != null) {
            if (!(getUfficioCorrenteId() > 0)) {
                errors.add("ufficio", new ActionMessage("campo.obbligatorio",
                        "Ufficio", ""));
            }
            if (getOggetto() == null || "".equals(getOggetto())) {
                errors.add("oggetto", new ActionMessage("campo.obbligatorio",
                        "Oggetto", ""));
            }
            if (getCodiceLocale() == null || "".equals(getCodiceLocale())) {
                errors.add("codice_locale", new ActionMessage(
                        "campo.obbligatorio", "Codice Locale", ""));
            }
            String dataCreazione = getDataCreazione();
            String dataChiusura = getDataChiusura();
            String dataEvidenza = getDataEvidenza();
            if ("".equals(dataCreazione) && (faldoneId > 0)) {
                errors.add("dataCreazione", new ActionMessage(
                        "campo.obbligatorio", "Data creazione", ""));
            } else if (!DateUtil.isData(dataCreazione)) {
                // la data di ricezione deve essere nel formato valido:
                // gg/mm/aaaa
                errors.add("dataCreazione", new ActionMessage(
                        "formato.data.errato", "Data creazione"));
            }

            if (dataChiusura != null && !"".equals(dataChiusura)) {
                if (!DateUtil.isData(dataChiusura)) {
                    // la data di ricezione deve essere nel formato valido:
                    // gg/mm/aaaa
                    errors.add("dataChiusura", new ActionMessage(
                            "formato.data.errato", "Data chiusura"));
                } else {
                    // la data di ricezione non deve essere successiva a quella
                    // di registrazione
                    if (DateUtil.toDate(dataChiusura).before(
                            DateUtil.toDate(dataApertura))) {
                        errors.add("dataAperturaDa", new ActionMessage(
                                "data_1.non.successiva.data_2",
                                "Data apertura", "Data chiusura"));
                    }
                }
            }
            if (!"".equals(dataEvidenza) && !DateUtil.isData(dataEvidenza)) {
                // la data di Evidenza deve essere nel formato valido:
                // gg/mm/aaaa
                errors.add("dataEvidenza", new ActionMessage(
                        "formato.data.errato", "Data evidenza"));
            }
            // modifica 24/10/2006

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dataCarico = getDataCarico();
            if (dataCarico.equals("")) {
                errors.add("dataCarico", new ActionMessage(
                        "campo.obbligatorio", "Data carico", ""));
            }
            if (getPosizioneSelezionata() == STATO_FALDONE_EVIDENZA) {
                if (getDataEvidenza() == null || "".equals(getDataEvidenza())) {
                    errors.add("dataEvidenza", new ActionMessage(
                            "campo.obbligatorio", "Data evidenza", ""));
                }
            }
            if (getPosizioneSelezionata() != STATO_FALDONE_EVIDENZA
                    && getDataEvidenza() == null) {
                errors.add("dataEvidenza", new ActionMessage(
                        "faldone.stato.evidenza", "", ""));
            }

            if (!"".equals(getDataCarico())) {
                if (!DateUtil.isData(getDataCarico())) {
                    errors.add("dataCarico", new ActionMessage(
                            "formato.data.errato", "Data carico"));
                } else if (DateUtil.toDate(getDataCarico()).after(
                        new Date(System.currentTimeMillis()))) {
                    errors.add("dataAperturaDa", new ActionMessage(
                            "data_1.non.successiva.data_2", "Data carico",
                            "Data odierna"));
                }
            }
            if (!"".equals(getDataScarico())) {
                if (!DateUtil.isData(getDataScarico())) {
                    errors.add("dataScarico", new ActionMessage(
                            "formato.data.errato", "Data scarico"));
                } else if (DateUtil.toDate(getDataCarico()).after(
                        DateUtil.toDate(getDataScarico()))) {
                    errors.add("dataAperturaDa", new ActionMessage(
                            "data_1.non.successiva.data_2", "Data carico",
                            "Data scarico"));
                }
            }

            // fine modifica 24/10/2006

            Utente utente = (Utente) request.getSession().getAttribute(
                    Constants.UTENTE_KEY);
            if (getTitolario() == null) {
                if (utente.getAreaOrganizzativa()
                        .getDipendenzaTitolarioUfficio() == 1) {
                    errors.add("titolario", new ActionMessage(
                            "campo.obbligatorio", "Titolario", ""));
                }
            } else {
                if (getTitolario().getParentId() == 0
                        && utente.getAreaOrganizzativa()
                                .getTitolarioLivelloMinimo() > 1) {
                    errors.add("titolario", new ActionMessage(
                            "faldone.titolario.livello", "", ""));

                }
            }

        } else if (request.getParameter("btnCancellaFaldone") != null) {
            if (getFascicoliFaldone().size() > 0) {
                errors.add("faldone", new ActionMessage(
                        "record_non_cancellabile", " il Faldone ", "contiene "
                                + getFascicoliFaldone().size()
                                + " fascicoli allacciati."));
            }
            if (getProcedimentiFaldone().size() > 0) {
                errors.add("faldone", new ActionMessage(
                        "record_non_cancellabile", " il Faldone ", "contiene "
                                + getProcedimentiFaldone().size()
                                + " procedimenti allacciati."));
            }
        }

        return errors;
    }

    public String getPosizioneSelezionataStoria() {
        return posizioneSelezionataStoria;
    }

    public void setPosizioneSelezionataStoria(String posizioneSelezionataStoria) {
        this.posizioneSelezionataStoria = posizioneSelezionataStoria;
    }

    public Collection getMatricole() {
        return matricole;
    }

    public void setMatricole(Collection matricole) {
        this.matricole = matricole;
    }

    public String getMatricola() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    public String getNotaFaldone() {
        return notaFaldone;
    }

    public void setNotaFaldone(String notaFaldone) {
        this.notaFaldone = notaFaldone;
    }

    public int getNumeroMatricole() {
        return numeroMatricole;
    }

    public void setNumeroMatricole(int numeroMatricole) {
        this.numeroMatricole = numeroMatricole;
    }

    public int getDipendenzaTitolarioUfficio() {
        return dipendenzaTitolarioUfficio;
    }

    public void setDipendenzaTitolarioUfficio(int dipendenzaTitolarioUfficio) {
        this.dipendenzaTitolarioUfficio = dipendenzaTitolarioUfficio;
    }

}