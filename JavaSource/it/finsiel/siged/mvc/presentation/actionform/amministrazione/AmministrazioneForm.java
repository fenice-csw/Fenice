package it.finsiel.siged.mvc.presentation.actionform.amministrazione;

import it.finsiel.siged.mvc.vo.lookup.ParametriLdapVO;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class AmministrazioneForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(AmministrazioneForm.class.getName());

    private int id;

    private int versione;

    private String codice;

    private String descrizione;

    private String flagLdap;

    private ParametriLdapVO parametriLDap = new ParametriLdapVO();

    private String pathDocAquisMassiva;

    private String pathDocumentiProtocollo;
    
    private String portaFtp;

    private String hostFtp;

    private String userFtp;

    private String passFtp;
    
    private String folderFtp;
    
    private boolean repertoriEnabled;
    
    private String webSocketEnabled;
    
    private String flagRegistroPostaSeparato;
    
	public String getFlagRegistroPostaSeparato() {
		return flagRegistroPostaSeparato;
	}

	public void setFlagRegistroPostaSeparato(String flagRegistroPostaSeparato) {
		this.flagRegistroPostaSeparato = flagRegistroPostaSeparato;
	}
	
	public String getWebSocketEnabled() {
		return webSocketEnabled;
	}

	public void setWebSocketEnabled(String webSocketEnabled) {
		this.webSocketEnabled = webSocketEnabled;
	}

	public String getPathDocumentiProtocollo() {
		return pathDocumentiProtocollo;
	}

	public void setPathDocumentiProtocollo(String pathDocumentiProtocollo) {
		this.pathDocumentiProtocollo = pathDocumentiProtocollo;
	}

	public String getFlagLdap() {
        return flagLdap;
    }

    public void setFlagLdap(String flagLdap) {
        this.flagLdap = flagLdap;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ParametriLdapVO getParametriLDap() {
        return parametriLDap;
    }

    public void setParametriLDap(ParametriLdapVO parametriLDap) {
        this.parametriLDap = parametriLDap;
    }

    public int getVersione() {
        return versione;
    }

    public void setVersione(int versione) {
        this.versione = versione;
    }

    public String getPathDocAquisMassiva() {
        return pathDocAquisMassiva;
    }

    public void setPathDocAquisMassiva(String pathDoc) {
        this.pathDocAquisMassiva = pathDoc;
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (request.getParameter("btnSalva") != null) {
            if (getDescrizione() == null || "".equals(getDescrizione().trim())) {
                errors.add("descrizione", new ActionMessage(
                        "campo.obbligatorio", "Descrizione", ""));
            } else if (getCodice() == null || "".equals(getCodice().trim())) {
                errors.add("codice", new ActionMessage("campo.obbligatorio",
                        "Codice", ""));

            }
            if ("1".equals(getFlagLdap())) {
                if (getParametriLDap().getPorta() == 0) {
                    errors.add("porta", new ActionMessage("campo.obbligatorio",
                            "Porta Server LDAP", ""));
                }
                if (getParametriLDap().getHost() == null
                        || "".equals(getParametriLDap().getHost().trim())) {
                    errors.add("host", new ActionMessage("campo.obbligatorio",
                            "Host LDAP", ""));
                }
                if (getParametriLDap().getDn() == null
                        || "".equals(getParametriLDap().getDn().trim())) {
                    errors.add("host", new ActionMessage("campo.obbligatorio",
                            "Dn", ""));
                }
            }
        }
        return errors;
    }

	
	public String getPortaFtp() {
		return portaFtp;
	}

	
	public String getHostFtp() {
		return hostFtp;
	}

	
	public String getUserFtp() {
		return userFtp;
	}

	public String getPassFtp() {
		return passFtp;
	}

	public boolean isRepertoriEnabled() {
		return repertoriEnabled;
	}

	public void setPortaFtp(String portaFtp) {
		this.portaFtp = portaFtp;
	}

	public void setHostFtp(String hostFtp) {
		this.hostFtp = hostFtp;
	}

	public void setUserFtp(String userFtp) {
		this.userFtp = userFtp;
	}

	public void setPassFtp(String passFtp) {
		this.passFtp = passFtp;
	}

	public void setRepertoriEnabled(boolean repertoriEnabled) {
		this.repertoriEnabled = repertoriEnabled;
	}

	public String getFolderFtp() {
		return folderFtp;
	}

	public void setFolderFtp(String folderFtp) {
		this.folderFtp = folderFtp;
	}
    
}