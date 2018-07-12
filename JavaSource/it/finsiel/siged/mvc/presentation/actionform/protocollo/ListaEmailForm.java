package it.finsiel.siged.mvc.presentation.actionform.protocollo;

import it.finsiel.siged.model.MessaggioEmailEntrata;
import it.finsiel.siged.mvc.presentation.helper.EmailView;
import it.finsiel.siged.mvc.vo.log.EventoVO;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class ListaEmailForm extends ActionForm {
   
	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(ListaEmailForm.class.getName());

    private Collection<DocumentoVO> allegatiEmail = new ArrayList<DocumentoVO>();

    private int docPrincipaleId;

    private String oggetto;

    private String emailMittente;

    private String nomeMittente;

    private String dataRicezione;

    private String dataSpedizione;

    private String tipoDocumentoPrincipale;

    private int emailId;

    // --------------
    private int emailSelezionataId;

    private Collection<EmailView> listaEmail = new ArrayList<EmailView>();

    private SoggettoVO mittente;
    
    private boolean nuovoMittente;
    
    private MessaggioEmailEntrata msg;
    
    private String testoMessaggio;
    
    private EventoVO emailLog;
    
	private boolean fatturaElettronica;
	
	private boolean flagAnomalia;

	
	public String getTestoMessaggio() {
		return testoMessaggio;
	}

	public void setTestoMessaggio(String testoMessaggio) {
		this.testoMessaggio = testoMessaggio;
	}

	public MessaggioEmailEntrata getMsg() {
		return msg;
	}

	public void setMsg(MessaggioEmailEntrata msg) {
		this.msg = msg;
	}


	public ListaEmailForm() {
    	
    }
 
	public boolean isNuovoMittente() {
		return nuovoMittente;
	}

	public void setNuovoMittente(boolean nuovoMittente) {
		this.nuovoMittente = nuovoMittente;
	}

	public SoggettoVO getMittente() {
		return mittente;
	}

	public void setMittente(SoggettoVO mittente) {
		this.mittente = mittente;
	}

	public int getEmailSelezionataId() {
        return emailSelezionataId;
    }

    public void setEmailSelezionataId(int emailSelezionataId) {
        this.emailSelezionataId = emailSelezionataId;
    }

    public Collection<EmailView> getListaEmail() {
        return listaEmail;
    }

    public void setListaEmail(Collection<EmailView> listaEmail) {
        this.listaEmail = listaEmail;
    }

    public String getDataRicezione() {
        return dataRicezione;
    }

    public void setDataRicezione(String dataRicezione) {
        this.dataRicezione = dataRicezione;
    }

    public String getDataSpedizione() {
        return dataSpedizione;
    }

    public void setDataSpedizione(String dataSpedizione) {
        this.dataSpedizione = dataSpedizione;
    }

    public int getDocPrincipaleId() {
        return docPrincipaleId;
    }

    public void setDocPrincipaleId(int documentoPrincipaleId) {
        this.docPrincipaleId = documentoPrincipaleId;
    }

    public String getEmailMittente() {
        return emailMittente;
    }

    public void setEmailMittente(String emailMittente) {
        this.emailMittente = emailMittente;
    }

    public String getNomeMittente() {
        return nomeMittente;
    }

    public void setNomeMittente(String nomeMittente) {
        this.nomeMittente = nomeMittente;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public Collection<DocumentoVO> getAllegatiEmail() {
        return allegatiEmail;
    }

    public void setAllegatiEmail(Collection<DocumentoVO> allegati) {
        this.allegatiEmail = allegati;
    }

    public String getTipoDocumentoPrincipale() {
        return tipoDocumentoPrincipale;
    }

    public void setTipoDocumentoPrincipale(String documentoPrincipale) {
        this.tipoDocumentoPrincipale = documentoPrincipale;
    }

    public int getEmailId() {
        return emailId;
    }

    public void setEmailId(int emailId) {
        this.emailId = emailId;
    }
    
    public EventoVO getEmailLog() {
		return emailLog;
	}

	public void setEmailLog(EventoVO mailLog) {
		this.emailLog = mailLog;
	}

	public boolean isFatturaElettronica() {
		return fatturaElettronica;
	}

	public void setFatturaElettronica(boolean fatturaElettronica) {
		this.fatturaElettronica = fatturaElettronica;
	}
	
	public boolean isFlagAnomalia() {
		return flagAnomalia;
	}

	public void setFlagAnomalia(boolean flagAnomalia) {
		this.flagAnomalia = flagAnomalia;
	}

	public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (request.getParameter("cancella") != null
                && getEmailSelezionataId() == 0 && getEmailId() == 0) {
            errors.add("nome", new ActionMessage("selezione.obbligatoria",
                    "il messaggio", "da eliminare"));
        } else if (request.getParameter("visualizza") != null
                && getEmailSelezionataId() == 0) {
            errors.add("nome", new ActionMessage("selezione.obbligatoria",
                    "il messaggio", "da visualizzare"));
        } 
        return errors;
    }
	
	
}