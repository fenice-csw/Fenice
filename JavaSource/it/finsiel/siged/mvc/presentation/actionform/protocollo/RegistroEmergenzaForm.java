package it.finsiel.siged.mvc.presentation.actionform.protocollo;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.RegistroBO;
import it.finsiel.siged.mvc.presentation.helper.SessioniEmergenzaView;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class RegistroEmergenzaForm extends ActionForm {

    private int numeroProtocolliIngresso;

    private int numeroProtocolliUscita;
    
    private boolean inserisciIngresso;

    private String dataRegistrazioneProtocollo;

    private Collection protocolliPrenotati = new ArrayList();

    private Collection protocolliToExport;
    
    private Collection sessioniAperte;
    
    //private Object sessioneSelezionata=new Object();
    
    static Logger logger = Logger.getLogger(RegistroEmergenzaForm.class
            .getName());

    

	public boolean isInserisciIngresso() {
		return inserisciIngresso;
	}

	public void setInserisciIngresso(boolean inserisciIngresso) {
		this.inserisciIngresso = inserisciIngresso;
	}

	/*
	public Object getSessioneSelezionata() {
		return sessioneSelezionata;
	}

	public void setSessioneSelezionata(Object sessioneSelezionata) {
		this.sessioneSelezionata = sessioneSelezionata;
	}
	*/

	public Collection getProtocolliToExport() {
        return protocolliToExport;
    }

    public void setProtocolliToExport(Collection protocolliToExport) {
        this.protocolliToExport = protocolliToExport;
    }

    public Collection getProtocolliPrenotati() {
        return this.protocolliPrenotati;
    }

    public Collection getProtocolliPrenotatiCollection() {
        if (protocolliPrenotati != null) {
            return protocolliPrenotati;
        } else
            return null;
    }

    public void setProtocolliPrenotati(Collection protocolliprenotati) {
        this.protocolliPrenotati = protocolliprenotati;
    }

    public String getDataRegistrazioneProtocollo() {
        return dataRegistrazioneProtocollo;
    }

    public void setDataRegistrazioneProtocollo(String dataRegistriEmergenza) {
        this.dataRegistrazioneProtocollo = dataRegistriEmergenza;
    }

    public int getNumeroProtocolliIngresso() {
        return numeroProtocolliIngresso;
    }

    public void setNumeroProtocolliIngresso(int numeroRegistriEmergenzaIngresso) {
        this.numeroProtocolliIngresso = numeroRegistriEmergenzaIngresso;
    }

    public int getNumeroProtocolliUscita() {
        return numeroProtocolliUscita;
    }

    public void setNumeroProtocolliUscita(int numeroRegistriEmergenzaUscita) {
        this.numeroProtocolliUscita = numeroRegistriEmergenzaUscita;
    }
    
	public void reset(ActionMapping mapping, HttpServletRequest request) {
        Utente utente = (Utente) request.getSession().getAttribute(
                Constants.UTENTE_KEY);
        RegistroVO registro = (RegistroVO) utente.getRegistroVOInUso();
        setDataRegistrazioneProtocollo(DateUtil.formattaData(RegistroBO
                .getDataAperturaRegistro(registro).getTime()));
        setNumeroProtocolliIngresso(0);
        setNumeroProtocolliUscita(0);

    }

    public ActionErrors validateDatiInserimento(ActionMapping mapping,
            HttpServletRequest request) throws DataException {
        ActionErrors errors = new ActionErrors();
        if (!NumberUtil
                .isInteger(String.valueOf(getNumeroProtocolliIngresso()))
                || getNumeroProtocolliIngresso() < 0) {
            errors.add("ProtocolliIngresso", new ActionMessage(
                    "Numero_Protocolli_Ingresso_Positivi"));
        }
        if (!NumberUtil.isInteger(String.valueOf(getNumeroProtocolliUscita()))
                || getNumeroProtocolliUscita() < 0) {
            errors.add("ProtocolliUscita", new ActionMessage(
                    "Numero_Protocolli_Uscita_Positivi"));
        }
        if ((NumberUtil.isInteger(String.valueOf(getNumeroProtocolliUscita())) && getNumeroProtocolliUscita() == 0)
                && (NumberUtil.isInteger(String
                        .valueOf(getNumeroProtocolliIngresso())) && getNumeroProtocolliIngresso() == 0)) {
            errors.add("ProtocolliMaggioredizero", new ActionMessage(
                    "NumeroProtocolli_Maggiore_Di_Zero"));
        }

        if (dataRegistrazioneProtocollo == null
                || "".equals(dataRegistrazioneProtocollo)
                && !DateUtil.isData(dataRegistrazioneProtocollo)) {
            errors.add("dataRegistrazioneProtocolli", new ActionMessage(
                    "data_registrazione_maggiore_ultima_data_registrazione"));
        }

        return errors;
    }

	public Collection<SessioniEmergenzaView> getSessioniAperte() {
		return sessioniAperte;
	}

	public void setSessioniAperte(Collection sessioniAperte) {
		this.sessioniAperte = sessioniAperte;
	}
    
    
}