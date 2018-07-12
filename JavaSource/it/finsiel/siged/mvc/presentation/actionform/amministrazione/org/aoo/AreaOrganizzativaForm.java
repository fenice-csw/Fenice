package it.finsiel.siged.mvc.presentation.actionform.amministrazione.org.aoo;

import it.finsiel.siged.model.organizzazione.AreaOrganizzativa;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.vo.IdentityVO;
import it.finsiel.siged.mvc.vo.lookup.ProvinciaVO;
import it.finsiel.siged.mvc.vo.organizzazione.AreaOrganizzativaVO;
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

public final class AreaOrganizzativaForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	public AreaOrganizzativaVO getAreaorganizzativa() {
        return areaorganizzativa;
    }

    public void setAreaorganizzativa(AreaOrganizzativaVO areaorganizzativa) {
        this.areaorganizzativa = areaorganizzativa;
    }

    static Logger logger = Logger.getLogger(AreaOrganizzativa.class.getName());

    private int id;

    private String codi_aoo;

    private String description;

    private String data_istituzione;

    private String responsabile_nome;

    private String responsabile_cognome;

    private String responsabile_email;

    private String responsabile_telefono;

    private String data_soppressione;

    private String telefono;

    private String fax;

    private String indi_dug;

    private String indi_toponimo;

    private String indi_civico;

    private String indi_cap;

    private String indi_comune;

    private String email;

    private String dipartimento_codice;

    private String dipartimento_descrizione;

    private String tipo_aoo;

    private int provincia_id;

    private String codi_documento_doc;

    private int amministrazione_id;

    private String desc_amministrazione;

    private int versione;

    private Collection<AreaOrganizzativa> areeOrganizzative;

    private AreaOrganizzativaVO areaorganizzativa;

    // campi dati posta elettronica normale e certificata
    private String pec_indirizzo;

    private String pec_username;

    private String pec_pwd;

    private boolean pecAbilitata;

    private String pec_ssl_port;

    private String pec_pop3;

    private String pec_smtp;

    private String pec_smtp_port;

    private String pn_indirizzo;

    private String pn_username;

    private String pn_pwd;

    private boolean pn_ssl;

    private String pn_ssl_port;

    private String pn_pop3;

    private String pn_smtp;
    
    private boolean pnAbilitata;

    private int pecTimer;
    
    private String prec_indirizzo_invio;
    
    private String prec_indirizzo_ricezione;

    private String prec_username;

    private String prec_pwd;

    private String prec_smtp;

    private String dirDocumenti;

    private int dipendenzaTitolarioUfficio;

    private int titolarioLivelloMinimo;

    private boolean modificabileDipendenzaTitolarioUfficio;
    
    private boolean documentoReadable;

    private boolean ricercaUfficiFull;
    
    private String idCommittenteFattura;
    
    private String gaUsername;

    private String gaPwd;

    private boolean gaAbilitata;
    
    private boolean gaFlagInvio;

    private String gaTimer;
    
    private String anniVisibilitaBacheca;
    
    private String maxRighe;
    
    private int flagPubblicazioneP7m;

    
    public void reset(ActionMapping mapping, HttpServletRequest request) {

    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (request.getParameter("btnSalva") != null) {
            if (getDescription() == null || "".equals(getDescription().trim())) {
                errors.add("description", new ActionMessage(
                        "campo.obbligatorio", "Descrizione", ""));
            }
            if (getCodi_aoo() == null || "".equals(getCodi_aoo().trim())) {
                errors.add("description", new ActionMessage(
                        "campo.obbligatorio", "Codice", ""));
            }
            if (getIndi_dug() == null) {
                errors.add("indi_dug", new ActionMessage("campo.obbligatorio",
                        "Data istituzione", ""));
            }
            String dataIst = getData_istituzione();
            if (dataIst != null && !"".equals(dataIst)) {
                if (!DateUtil.isData(dataIst)) {
                    errors.add("dataIst", new ActionMessage(
                            "formato.data.errato", "Data istituzione"));
                }
            } else {
                errors.add("description", new ActionMessage(
                        "campo.obbligatorio", "Data istituzione", ""));
            }
            if (getIndi_dug() == null || "".equals(getIndi_dug().trim())) {
                errors.add("Dug", new ActionMessage("campo.obbligatorio",
                        "Dug", ""));
            }
            if (getIndi_toponimo() == null
                    || "".equals(getIndi_toponimo().trim())) {
                errors.add("Toponimo", new ActionMessage("campo.obbligatorio",
                        "Toponimo", ""));
            }
            if (getIndi_civico() == null || "".equals(getIndi_civico().trim())) {
                errors.add("Toponimo", new ActionMessage("campo.obbligatorio",
                        "Civico", ""));
            }
            if (getIndi_cap() == null || "".equals(getIndi_cap().trim())) {
                errors.add("Toponimo", new ActionMessage("campo.obbligatorio",
                        "CAP", ""));
            }
            if (getIndi_comune() == null || "".equals(getIndi_comune().trim())) {
                errors.add("Toponimo", new ActionMessage("campo.obbligatorio",
                        "Comune", ""));
            }

            if (getDipartimento_codice() != null
                    && !NumberUtil.isInteger(getDipartimento_codice())) {
                errors.add("Codice dipartimento", new ActionMessage(
                        "formato.numerico.errato", "Codice dipartimento", ""));
            }
            if (getAnniVisibilitaBacheca() == null
                    || "".equals(getAnniVisibilitaBacheca().trim())) {
                errors.add("anniVisibilitaBacheca", new ActionMessage("campo.obbligatorio",
                        "N&deg; di anni visibili nella zona lavoro", ""));
            } 
            if (getAnniVisibilitaBacheca() != null
                    && !NumberUtil.isInteger(getAnniVisibilitaBacheca())) {
                errors.add("anniVisibilitaBacheca", new ActionMessage(
                        "formato.numerico.errato", "N&deg; di anni visibili nella zona lavoro", ""));
            }
            if (getMaxRighe() == null
                    || "".equals(getMaxRighe().trim())) {
                errors.add("maxRighe", new ActionMessage("campo.obbligatorio",
                        "N&deg; di record visibili nelle ricerche", ""));
            } 
            if (getMaxRighe() != null
                    && !NumberUtil.isInteger(getMaxRighe())) {
                errors.add("maxRighe", new ActionMessage(
                        "formato.numerico.errato", "N&deg; di record visibili nelle ricerche", ""));
            }
            
            if(isGaAbilitata()){
            	if (getGaUsername() == null || "".equals(getGaUsername().trim())) {
                    errors.add("Username Utenza Conservazione", new ActionMessage("campo.obbligatorio",
                            "Username Utenza Conservazione", ""));
                }
            	if (getGaPwd() == null || "".equals(getGaPwd().trim())) {
                    errors.add("Password Utenza Conservazione", new ActionMessage("campo.obbligatorio",
                            "Password Utenza Conservazione", ""));
                }
            	if (getGaFlagInvio() && (getGaTimer() == null || "".equals(getGaTimer().trim()))) {
                    errors.add("Invia alle", new ActionMessage("campo.obbligatorio",
                            "Invia alle", ""));
                }
            }

        } else if (request.getParameter("btnCancella") != null
                && (request.getParameter("id") == null)) {
            errors.add("id", new ActionMessage("campo.obbligatorio",
                    "AreaOrganizzativa", ""));
        }
        return errors;

    }

    public void inizializzaForm() {
        setId(0);
        setDescription(null);
        setResponsabile_nome(null);
        setAmministrazione_id(0);
        setCodi_aoo(null);
        setCodi_documento_doc(null);
        setData_istituzione(null);
        setData_soppressione(null);
        setDipartimento_codice(null);
        setDipartimento_descrizione(null);
        setEmail(null);
        setFax(null);
        setIndi_cap(null);
        setIndi_civico(null);
        setIndi_comune(null);
        setIndi_dug(null);
        setIndi_toponimo(null);
        setProvincia_id(0);
        setResponsabile_cognome(null);
        setResponsabile_email(null);
        setResponsabile_telefono(null);
        setTelefono(null);
        setTipo_aoo("L");
        setVersione(0);
        setPec_indirizzo(null);
        setPec_pop3(null);
        setPec_pwd(null);
        setPec_smtp(null);
        setPecAbilitata(false);
        setPec_ssl_port(null);
        setPec_username(null);
        setPrec_indirizzo_invio(null);
        setPrec_indirizzo_ricezione(null);
        setPrec_pwd(null);
        setPec_smtp(null);
        setPrec_username(null);
        setPnAbilitata(false);
        setPn_indirizzo(null);
        setPn_pop3(null);
        setPn_pwd(null);
        setPn_smtp(null);
        setPn_ssl(false);
        setPn_ssl_port(null);
        setPn_username(null);
        setMsgSuccess(null);
        setModificabileDipendenzaTitolarioUfficio(true);
        setIdCommittenteFattura(null);
    }
    
    public String getAnniVisibilitaBacheca() {
		return anniVisibilitaBacheca;
	}

	public void setAnniVisibilitaBacheca(String anniVisibilitaBacheca) {
		this.anniVisibilitaBacheca = anniVisibilitaBacheca;
	}
	
	public String getMaxRighe() {
		return maxRighe;
	}

	public void setMaxRighe(String maxRighe) {
		this.maxRighe = maxRighe;
	}

	public boolean getGaFlagInvio() {
		return gaFlagInvio;
	}

	public void setGaFlagInvio(boolean gaFlagInvio) {
		this.gaFlagInvio = gaFlagInvio;
	}
	
	public int getFlagPubblicazioneP7m() {
		return flagPubblicazioneP7m;
	}

	public void setFlagPubblicazioneP7m(int flagPubblicazioneP7m) {
		this.flagPubblicazioneP7m = flagPubblicazioneP7m;
	}

	public String getGaTimer() {
		return gaTimer;
	}

	public void setGaTimer(String gaTimer) {
		this.gaTimer = gaTimer;
	}

	public String getGaUsername() {
		return gaUsername;
	}

	public void setGaUsername(String gaUsername) {
		this.gaUsername = gaUsername;
	}

	public String getGaPwd() {
		return gaPwd;
	}

	public void setGaPwd(String gaPwd) {
		this.gaPwd = gaPwd;
	}

	public boolean isGaAbilitata() {
		return gaAbilitata;
	}

	public void setGaAbilitata(boolean gaAbilitata) {
		this.gaAbilitata = gaAbilitata;
	}

	public boolean isRicercaUfficiFull() {
		return ricercaUfficiFull;
	}

	public void setRicercaUfficiFull(boolean ricercaUfficiFull) {
		this.ricercaUfficiFull = ricercaUfficiFull;
	}

	public boolean isDocumentoReadable() {
		return documentoReadable;
	}

	public void setDocumentoReadable(boolean documentoReadable) {
		this.documentoReadable = documentoReadable;
	}

	public void setTipo_aoo(String tipo_aoo) {
        this.tipo_aoo = tipo_aoo;
    }

	public String getIdCommittenteFattura() {
		return idCommittenteFattura;
	}

	public void setIdCommittenteFattura(String idCommittenteFattura) {
		this.idCommittenteFattura = idCommittenteFattura;
	}

	public boolean isPnAbilitata() {
		return pnAbilitata;
	}

	public void setPnAbilitata(boolean pnAbilitata) {
		this.pnAbilitata = pnAbilitata;
	}

	
    public int getAmministrazione_id() {
        return amministrazione_id;
    }

    
    public void setAmministrazione_id(int amministrazione_id) {
        this.amministrazione_id = amministrazione_id;
    }

    public String getCodi_documento_doc() {
        return codi_documento_doc;
    }

    public void setCodi_documento_doc(String codi_documento_doc) {
        this.codi_documento_doc = codi_documento_doc;
    }

    public String getData_istituzione() {
        return data_istituzione;
    }

    public void setData_istituzione(String data_istituzione) {
        this.data_istituzione = data_istituzione;
    }

    public String getData_soppressione() {
        return data_soppressione;
    }

    public void setData_soppressione(String data_soppressione) {
        this.data_soppressione = data_soppressione;
    }

    public String getDipartimento_codice() {
        return dipartimento_codice;
    }

    public void setDipartimento_codice(String dipartimento_codice) {
        this.dipartimento_codice = dipartimento_codice;
    }

    public String getDipartimento_descrizione() {
        return dipartimento_descrizione;
    }

    public void setDipartimento_descrizione(String dipartimento_descrizione) {
        this.dipartimento_descrizione = dipartimento_descrizione;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIndi_cap() {
        return indi_cap;
    }

    public void setIndi_cap(String indi_cap) {
        this.indi_cap = indi_cap;
    }

    public String getIndi_civico() {
        return indi_civico;
    }
   
    public void setIndi_civico(String indi_civico) {
        this.indi_civico = indi_civico;
    }
  
    public String getIndi_comune() {
        return indi_comune;
    }

    public void setIndi_comune(String indi_comune) {
        this.indi_comune = indi_comune;
    }
 
    public String getIndi_dug() {
        return indi_dug;
    }
 
    public void setIndi_dug(String indi_dug) {
        this.indi_dug = indi_dug;
    }

    public String getIndi_toponimo() {
        return indi_toponimo;
    }

    public void setIndi_toponimo(String indi_toponimo) {
        this.indi_toponimo = indi_toponimo;
    }

    public int getProvincia_id() {
        return provincia_id;
    }

    public void setProvincia_id(int provincia_id) {
        this.provincia_id = provincia_id;
    }

    public String getResponsabile_cognome() {
        return responsabile_cognome;
    }

    public void setResponsabile_cognome(String responsabile_cognome) {
        this.responsabile_cognome = responsabile_cognome;
    }

    public String getResponsabile_email() {
        return responsabile_email;
    }

    public void setResponsabile_email(String responsabile_email) {
        this.responsabile_email = responsabile_email;
    }

    public String getResponsabile_nome() {
        return responsabile_nome;
    }

    public void setResponsabile_nome(String responsabile_nome) {
        this.responsabile_nome = responsabile_nome;
    }

    public String getResponsabile_telefono() {
        return responsabile_telefono;
    }

    public void setResponsabile_telefono(String responsabile_telefono) {
        this.responsabile_telefono = responsabile_telefono;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipo_aoo() {
        return tipo_aoo;
    }

    public int getVersione() {
        return versione;
    }

    public void setVersione(int versione) {
        this.versione = versione;
    }

    public Collection<AreaOrganizzativa> getAreeOrganizzative() {
        return areeOrganizzative;
    }

    public void setAreeOrganizzative(Collection<AreaOrganizzativa> areeOrganizzative) {
        this.areeOrganizzative = areeOrganizzative;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCodi_aoo() {
        return codi_aoo;
    }

    public void setCodi_aoo(String codi_aoo) {
        this.codi_aoo = codi_aoo;
    }

    public Collection<ProvinciaVO> getProvince() {
        return LookupDelegate.getInstance().getProvince();
    }

    public String getDesc_amministrazione() {
        return desc_amministrazione;
    }

    public void setDesc_amministrazione(String desc_amministrazione) {
        this.desc_amministrazione = desc_amministrazione;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        AreaOrganizzativaForm.logger = logger;
    }

    public String getPec_indirizzo() {
        return pec_indirizzo;
    }

    public void setPec_indirizzo(String pec_indirizzo) {
        this.pec_indirizzo = pec_indirizzo;
    }

    public String getPec_pop3() {
        return pec_pop3;
    }

    public void setPec_pop3(String pec_pop3) {
        this.pec_pop3 = pec_pop3;
    }

    public String getPec_pwd() {
        return pec_pwd;
    }

    public void setPec_pwd(String pec_pwd) {
        this.pec_pwd = pec_pwd;
    }

    public String getPec_smtp() {
        return pec_smtp;
    }

    public void setPec_smtp(String pec_smtp) {
        this.pec_smtp = pec_smtp;
    }

    public boolean getPecAbilitata() {
        return pecAbilitata;
    }

    public void setPecAbilitata(boolean pec_ssl) {
        this.pecAbilitata = pec_ssl;
    }

    public String getPec_ssl_port() {
        return pec_ssl_port;
    }

    public void setPec_ssl_port(String pec_ssl_port) {
        this.pec_ssl_port = pec_ssl_port;
    }

    public String getPec_username() {
        return pec_username;
    }

    public void setPec_username(String pec_username) {
        this.pec_username = pec_username;
    }

    public String getPn_indirizzo() {
        return pn_indirizzo;
    }

    public void setPn_indirizzo(String pn_indirizzo) {
        this.pn_indirizzo = pn_indirizzo;
    }

    public String getPn_pop3() {
        return pn_pop3;
    }

    public void setPn_pop3(String pn_pop3) {
        this.pn_pop3 = pn_pop3;
    }

    public String getPn_pwd() {
        return pn_pwd;
    }

    public void setPn_pwd(String pn_pwd) {
        this.pn_pwd = pn_pwd;
    }

    public String getPn_smtp() {
        return pn_smtp;
    }

    public void setPn_smtp(String pn_smtp) {
        this.pn_smtp = pn_smtp;
    }

    public boolean getPn_ssl() {
        return pn_ssl;
    }

    public void setPn_ssl(boolean pn_ssl) {
        this.pn_ssl = pn_ssl;
    }

    public String getPn_ssl_port() {
        return pn_ssl_port;
    }

    public void setPn_ssl_port(String pn_ssl_port) {
        this.pn_ssl_port = pn_ssl_port;
    }

    public String getPn_username() {
        return pn_username;
    }

    public void setPn_username(String pn_username) {
        this.pn_username = pn_username;
    }

    public Collection<IdentityVO> getTipiAoo() {
        Collection<IdentityVO> tipiAoo = new ArrayList<IdentityVO>();
        IdentityVO idVO;
        idVO = new IdentityVO();
        idVO.setCodice("L");
        idVO.setDescription("AOO Light");
        tipiAoo.add(idVO);
        idVO = new IdentityVO();
        idVO.setCodice("F");
        idVO.setDescription("AOO Full");
        tipiAoo.add(idVO);
        return tipiAoo;
    }

    public String getPec_smtp_port() {
        return pec_smtp_port;
    }

    public void setPec_smtp_port(String pec_smtp_port) {
        this.pec_smtp_port = pec_smtp_port;
    }

    private String msgSuccess;

    public String getMsgSuccess() {
        return msgSuccess;
    }

    public void setMsgSuccess(String msgSuccess) {
        this.msgSuccess = msgSuccess;
    }

    public int getPecTimer() {
        return pecTimer;
    }

    public void setPecTimer(int pecTimer) {
        this.pecTimer = pecTimer;
    }

    public String getDirDocumenti() {
        return dirDocumenti;
    }

    public void setDirDocumenti(String dirDocumenti) {
        this.dirDocumenti = dirDocumenti;
    }

    public int getDipendenzaTitolarioUfficio() {
        return dipendenzaTitolarioUfficio;
    }

    public void setDipendenzaTitolarioUfficio(int dipendenzaTitolarioUfficio) {
        this.dipendenzaTitolarioUfficio = dipendenzaTitolarioUfficio;
    }

    public int getTitolarioLivelloMinimo() {
        return titolarioLivelloMinimo;
    }

    public void setTitolarioLivelloMinimo(int titolarioLivelloMinimo) {
        this.titolarioLivelloMinimo = titolarioLivelloMinimo;
    }

    public boolean getModificabileDipendenzaTitolarioUfficio() {
        return modificabileDipendenzaTitolarioUfficio;
    }

    public void setModificabileDipendenzaTitolarioUfficio(
            boolean isModificabileDipendenzaTitolarioUfficio) {
        this.modificabileDipendenzaTitolarioUfficio = isModificabileDipendenzaTitolarioUfficio;
    }

	public String getPrec_indirizzo_invio() {
		return prec_indirizzo_invio;
	}

	public void setPrec_indirizzo_invio(String prec_indirizzo_invio) {
		this.prec_indirizzo_invio = prec_indirizzo_invio;
	}

	public String getPrec_indirizzo_ricezione() {
		return prec_indirizzo_ricezione;
	}

	public void setPrec_indirizzo_ricezione(String prec_indirizzo_ricezione) {
		this.prec_indirizzo_ricezione = prec_indirizzo_ricezione;
	}

	public String getPrec_username() {
		return prec_username;
	}

	public void setPrec_username(String prec_username) {
		this.prec_username = prec_username;
	}

	public String getPrec_pwd() {
		return prec_pwd;
	}

	public void setPrec_pwd(String prec_pwd) {
		this.prec_pwd = prec_pwd;
	}

	public String getPrec_smtp() {
		return prec_smtp;
	}

	public void setPrec_smtp(String prec_smtp) {
		this.prec_smtp = prec_smtp;
	}

}