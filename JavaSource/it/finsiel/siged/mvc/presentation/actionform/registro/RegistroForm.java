package it.finsiel.siged.mvc.presentation.actionform.registro;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.util.DateUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class RegistroForm extends ActionForm {
    private int id;

    private String codice;

    private String descrizione;

    private Collection registri;

    private Map utentiAbilitati;

    private Map utentiNonAbilitati;

    private String[] utentiSelezionati;

    private boolean ufficiale;

    private boolean apertoIngresso;

    private boolean apertoUscita;

    private boolean dataBloccata;

    private String dataApertura;

    private int versione;

    private int aooId;

    public boolean getUfficiale() {
        return ufficiale;
    }

    public void setUfficiale(boolean ufficiale) {
        this.ufficiale = ufficiale;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getApertoIngresso() {
        return apertoIngresso;
    }

    public void setApertoIngresso(boolean apertoIngresso) {
        this.apertoIngresso = apertoIngresso;
    }

    public boolean getApertoUscita() {
        return apertoUscita;
    }

    public void setApertoUscita(boolean apertoUscita) {
        this.apertoUscita = apertoUscita;
    }

    public boolean getDataBloccata() {
        return dataBloccata;
    }

    public void setDataBloccata(boolean bloccato) {
        this.dataBloccata = bloccato;
    }

    public String getDataApertura() {
        return dataApertura;
    }

    public void setDataApertura(String dataApertura) {
        this.dataApertura = dataApertura;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Collection getRegistri() {
        return registri;
    }

    public void setRegistri(Collection registri) {
        this.registri = registri;
    }

    public int getAooId() {
        return aooId;
    }

    public void setAooId(int aooId) {
        this.aooId = aooId;
    }

    public Collection getUtentiAbilitati() {
        if (utentiAbilitati != null) {
            Collection c = utentiAbilitati.values();
            return getListaUtentiOrdinata(c);
        } else
            return null;
    }

    public void aggiungiUtenteAbilitato(UtenteVO ute) {
        if (ute != null) {
            if (this.utentiAbilitati == null)
                this.utentiAbilitati = new HashMap();

            this.utentiAbilitati.put(ute.getId(), ute);
        }
    }

    public void aggiungiUtenteNonAbilitato(UtenteVO ute) {
        if (ute != null) {
            if (this.utentiNonAbilitati == null)
                this.utentiNonAbilitati = new HashMap();

            this.utentiNonAbilitati.put(ute.getId(), ute);
        }
    }

    public void rimuoviUtenteAbilitato(Integer id) {
        if (utentiAbilitati != null)
            this.utentiAbilitati.remove(id);
    }

    public void rimuoviUtenteNonAbilitato(Integer id) {
        if (utentiNonAbilitati != null)
            this.utentiNonAbilitati.remove(id);
    }

    public Collection getUtentiNonAbilitati() {
        if (utentiNonAbilitati != null) {
            Collection c = utentiNonAbilitati.values();
            return getListaUtentiOrdinata(c);
        } else
            return null;
    }

    private Collection getListaUtentiOrdinata(Collection col) {
        List list = new ArrayList();
        for (Iterator i = col.iterator(); i.hasNext();) {
            UtenteVO ute = (UtenteVO) i.next();
            list.add(ute);
        }
        Comparator c = new Comparator() {
            public int compare(Object obj1, Object obj2) {
                UtenteVO ute1 = (UtenteVO) obj1;
                UtenteVO ute2 = (UtenteVO) obj2;
                return ute1.getFullName().compareToIgnoreCase(
                        ute2.getFullName());
            }
        };
        Collections.sort(list, c);
        return list;
    }

    public String[] getUtentiSelezionati() {
        return utentiSelezionati;
    }

    public void setUtentiSelezionati(String[] utentiSelezionati) {
        this.utentiSelezionati = utentiSelezionati;
    }

    public int getVersione() {
        return versione;
    }

    public void setVersione(int versione) {
        this.versione = versione;
    }

    public void inizializzaForm() {
        setId(0);
        setDescrizione(null);
        setCodice(null);
        setUfficiale(false);
        setDataBloccata(false);
        setApertoIngresso(true);
        setApertoUscita(true);
        setRegistri(null);
        setUtentiSelezionati(null);
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (request.getParameter("salvaAction") != null) {
            if (getDataApertura() == null || getDataApertura().equals("")) {
                errors.add("dataApertura", new ActionMessage(
                        "campo.obbligatorio", "Data apertura", ""));
            } else if (!DateUtil.isData(getDataApertura())) {
                errors.add("dataApertura", new ActionMessage(
                        "formato.data.errato", "Data apertura"));
            } else {
                Date oggi = new Date();
                Date dataApertura = DateUtil.toDate(getDataApertura());
                Utente ute = (Utente) request.getSession().getAttribute(
                        Constants.UTENTE_KEY);
                Date dataAperturaAttuale = ute.getRegistroVOInUso()
                        .getDataAperturaRegistro();
                if (dataApertura.before(dataAperturaAttuale)) {
                    errors.add("dataApertura",
                            new ActionMessage("data_apertura.min",
                                    DateUtil.formattaData(dataAperturaAttuale
                                            .getTime())));
                }
                if (dataApertura.after(oggi)) {
                    errors.add("dataApertura", new ActionMessage(
                            "data_apertura.max", DateUtil.formattaData(oggi
                                    .getTime())));
                }
            }
        } else if (request.getParameter("btnSalva") != null) {
            if (getCodice() == null || "".equals(getCodice())) {
                errors.add("codice", new ActionMessage("campo.obbligatorio",
                        "Codice registro", ""));
            }
            if (getDescrizione() == null || "".equals(getDescrizione())) {
                errors.add("descrizione", new ActionMessage(
                        "campo.obbligatorio", "Descrizione registro", ""));
            }
        } else if ((request.getParameter("aggiungiUtenti") != null || request
                .getParameter("rimuoviUtenti") != null)
                && utentiSelezionati == null) {
            errors.add("registro_utenti", new ActionMessage(
                    "selezione.obbligatoria", "almeno un utente", ""));
        }

        return errors;
    }
}