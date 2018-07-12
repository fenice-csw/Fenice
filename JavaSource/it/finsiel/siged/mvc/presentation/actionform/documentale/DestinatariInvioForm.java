package it.finsiel.siged.mvc.presentation.actionform.documentale;

import it.finsiel.siged.mvc.presentation.action.documentale.DocumentoAction;
import it.finsiel.siged.mvc.presentation.helper.DestinatarioView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class DestinatariInvioForm extends DocumentoForm {
    static Logger logger = Logger.getLogger(DocumentoAction.class.getName());

    public DestinatariInvioForm() {
    }

    public void inizializzaForm() {
        destinatari = new HashMap();
    }

    public ActionErrors validateDestinatari(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (request.getParameter("btnInvioProtocollo") != null) {
            if (destinatari.isEmpty()) {
                errors
                        .add("destinatari", new ActionMessage(
                                "selezione.obbligatoria",
                                "almeno un destinatario", ""));
            }

        } else if (request.getParameter("aggiungiDestinatario") != null) {
            if (getNominativoDestinatario() == null
                    || "".equals(getNominativoDestinatario().trim())) {
                errors.add("destinatari", new ActionMessage(
                        "selezione.obbligatoria", "almeno un destinatario",
                        "utilizzando la funzione Aggiungi"));
            }
        }
        return errors;
    }

    // DESTINATARIO

    private String tipoDestinatario = "F";

    public String getTipoDestinatario() {
        return tipoDestinatario;
    }

    public void setTipoDestinatario(String tipoDestinatario) {
        this.tipoDestinatario = tipoDestinatario;
    }

    private String nominativoDestinatario;

    public String getNominativoDestinatario() {
        return nominativoDestinatario;
    }

    public void setNominativoDestinatario(String nominativoDestinatario) {
        this.nominativoDestinatario = nominativoDestinatario;
    }

    private int destinatarioId;

    public int getDestinatarioId() {
        return destinatarioId;
    }

    public void setDestinatarioId(int destinatarioId) {
        this.destinatarioId = destinatarioId;
    }

    private Map destinatari = new HashMap(2);

    public Collection getDestinatari() {
        return destinatari.values();
    }

    public DestinatarioView getDestinatario(String nomeDestinatario) {
        return (DestinatarioView) destinatari.get(nomeDestinatario);
    }

    public void aggiungiDestinatario(DestinatarioView destinatario) {
        if (destinatario != null) {
            this.destinatari.put(destinatario.getDestinatario().trim(),
                    destinatario);
        }
    }

    public void rimuoviDestinatario(String destinatario) {
        this.destinatari.remove(destinatario);
    }

    public void rimuoviDestinatari() {
        if (destinatari != null) {
            this.destinatari.clear();
        }
    }

    private String[] destinatarioSelezionatoId;

    public String[] getDestinatarioSelezionatoId() {
        return destinatarioSelezionatoId;
    }

    public void setDestinatarioSelezionatoId(String[] destinatarioSelezionatoId) {
        this.destinatarioSelezionatoId = destinatarioSelezionatoId;
    }

    // DESTINATARIO

    private String citta;

    private String mezzoSpedizione;

    private String emailDestinatario;

    private String indirizzoDestinatario;
    
    private String capDestinatario;

    private String dataSpedizione;

    private boolean flagConoscenza;

    private Collection mezziDiSpedizione;

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getDataSpedizione() {
        return dataSpedizione;
    }

    public void setDataSpedizione(String dataSpedizione) {
        this.dataSpedizione = dataSpedizione;
    }

    public String getEmailDestinatario() {
        return emailDestinatario;
    }

    public void setEmailDestinatario(String emailDestinatario) {
        this.emailDestinatario = emailDestinatario;
    }

    public boolean getFlagConoscenza() {
        return flagConoscenza;
    }

    public void setFlagConoscenza(boolean flagConoscenza) {
        this.flagConoscenza = flagConoscenza;
    }

    public String getIndirizzoDestinatario() {
        return indirizzoDestinatario;
    }

    public void setIndirizzoDestinatario(String indirizzoDestinatario) {
        this.indirizzoDestinatario = indirizzoDestinatario;
    }

    public Collection getMezziDiSpedizione() {
        return mezziDiSpedizione;
    }

    public void setMezziDiSpedizione(Collection mezziDiSpedizione) {
        this.mezziDiSpedizione = mezziDiSpedizione;
    }

    public void setDestinatari(Map destinatari) {
        this.destinatari = destinatari;
    }

    public void inizializzaDestinatarioForm() {
        setDataSpedizione(null);
        setEmailDestinatario(null);
        setCitta(null);
        setIndirizzoDestinatario(null);
        setCapDestinatario(null);
        setFlagConoscenza(false);
        setNominativoDestinatario(null);
        setTipoDestinatario("F");
    }

    public String getCapDestinatario() {
        return capDestinatario;
    }

    public void setCapDestinatario(String capDestinatario) {
        this.capDestinatario = capDestinatario;
    }

}