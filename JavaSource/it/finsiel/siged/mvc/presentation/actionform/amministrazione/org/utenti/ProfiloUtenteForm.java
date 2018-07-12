package it.finsiel.siged.mvc.presentation.actionform.amministrazione.org.utenti;

import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.business.AmministrazioneDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.AlberoUfficiForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.presentation.helper.MenuView;
import it.finsiel.siged.mvc.vo.organizzazione.MenuVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.util.DateUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class ProfiloUtenteForm extends ActionForm implements
        AlberoUfficiForm {

    static Logger logger = Logger.getLogger(ProfiloUtenteForm.class.getName());

    private int id;

    private String userName;

    private String passWord;

    private String confermaPassword;

    private String cognome;

    private String nome;

    private String emailAddress;

    private String codiceFiscale;

    private String matricola;

    private boolean abilitato;

    private String dataFineAttivita;

    private String[] ufficiSelezionatiId;

    private Collection ufficiAssegnati;

    private int ufficioCorrenteId;

    private int ufficioSelezionatoId;

    private String ufficioCorrentePath;

    private UfficioVO ufficioCorrente;

    private Collection pathUffici = new ArrayList(); // di UfficioVO

    private Collection ufficiDipendenti = new ArrayList(); // di UfficioVO

    private String impostaUfficioAction;

    private int profiloId;

    private Collection profiliMenu;

    private String[] registriSelezionatiId;

    private Collection registri;

    private String[] funzioniMenuSelezionate;

    private Collection funzioniMenu;

    private Collection versioniCarica;

    
    public Collection getVersioniCarica() {
		return versioniCarica;
	}
    
    public void setVersioniCarica(Collection versioniCarica) {
		this.versioniCarica = versioniCarica;
	}
    
    public String[] getUfficiSelezionatiId() {
        return ufficiSelezionatiId;
    }
    
    public Collection getProtocolloMenu() {
    	List<MenuVO> protocolloMenu = new ArrayList<MenuVO>();
    	
    	try {
    		protocolloMenu = AmministrazioneDelegate.getInstance().getFunzioniProtocollazioneMenu();
		} catch (DataException e) {
			e.printStackTrace();
		}
		return protocolloMenu;
    }
    
    public Collection getHelpMenu() {
    	List<MenuVO> protocolloMenu = new ArrayList<MenuVO>();
    	try {
    		protocolloMenu = AmministrazioneDelegate.getInstance().getFunzioniHelpMenu();
		} catch (DataException e) {
			e.printStackTrace();
		}
		
    	return protocolloMenu;
    }
    
    public Collection getDocumentaleMenu() {
    	List<MenuVO> protocolloMenu = new ArrayList<MenuVO>();
    	try {
    		protocolloMenu = AmministrazioneDelegate.getInstance().getFunzioniDocumentaleMenu();
		} catch (DataException e) {
			e.printStackTrace();
		}
		
    	
    	return protocolloMenu;
    }
    
    public Collection getAmministrazioneMenu() {
    	List<MenuVO> protocolloMenu = new ArrayList<MenuVO>();
    	
    	try {
    		protocolloMenu = AmministrazioneDelegate.getInstance().getFunzioniAmministrazioneMenu();
		} catch (DataException e) {
			e.printStackTrace();
		}
		
    	
    	return protocolloMenu;
    }
    
    public Collection getReportMenu() {
    	List<MenuVO> protocolloMenu = new ArrayList<MenuVO>();
    	
    	try {
    		protocolloMenu = AmministrazioneDelegate.getInstance().getFunzioniReportMenu();
		} catch (DataException e) {
			e.printStackTrace();
		}
		
    	return protocolloMenu;
    }
    
    

    /**
     * @param ufficiSelezionatiId
     *            The ufficiSelezionatiId to set.
     */
    public void setUfficiSelezionatiId(String[] ufficiSelezionatiId) {
        this.ufficiSelezionatiId = ufficiSelezionatiId;
    }

    /**
     * @return Returns the ufficiAssegnati.
     */
    public Collection getUfficiAssegnati() {
        return ufficiAssegnati;
    }

    /**
     * @param ufficiAssegnati
     *            The ufficiAssegnati to set.
     */
    public void setUfficiAssegnati(Collection ufficiAssegnati) {
        this.ufficiAssegnati = ufficiAssegnati;
    }

    /**
     * @param assegnatari
     *            The assegnatari to set.
     */
    private Map uffici = new HashMap();;

    public void setUffici(Map uffici) {
        this.uffici = uffici;
    }

    public Collection getUffici() {
        return uffici.values();
    }

    public void aggiungiUfficio(AssegnatarioView ass) {
        uffici.put(ass.getKey(), ass);
    }

    public void rimuoviUfficio(String key) {
        uffici.remove(key);
    }

    public void rimuoviUffici() {
        if (!uffici.isEmpty())
            uffici.clear();
    }

    public boolean getAbilitato() {
        return abilitato;
    }

    public void setAbilitato(boolean abilitato) {
        this.abilitato = abilitato;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getConfermaPassword() {
        return confermaPassword;
    }

    public void setConfermaPassword(String confermaPassword) {
        this.confermaPassword = confermaPassword;
    }

    public String getDataFineAttivita() {
        return dataFineAttivita;
    }

    public void setDataFineAttivita(String dataFineAttivita) {
        this.dataFineAttivita = dataFineAttivita;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emial) {
        this.emailAddress = emial;
    }

    public String getMatricola() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String password) {
        this.passWord = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImpostaUfficioAction() {
        return impostaUfficioAction;
    }

    public String getUfficioCorrentePath() {
        return ufficioCorrentePath;
    }

    public void setUfficioCorrentePath(String ufficioCorrentePath) {
        this.ufficioCorrentePath = ufficioCorrentePath;
    }

    public void setImpostaUfficioAction(String impostaUfficioAction) {
        this.impostaUfficioAction = impostaUfficioAction;
    }

    public Collection getPathUffici() {
        return pathUffici;
    }

    public void setPathUffici(Collection pathUffici) {
        this.pathUffici = pathUffici;
    }

    public Collection getUfficiDipendenti() {
        return ufficiDipendenti;
    }

    public void setUfficiDipendenti(Collection ufficiDipendenti) {
        this.ufficiDipendenti = ufficiDipendenti;
    }

    public int getUfficioCorrenteId() {
        return ufficioCorrenteId;
    }

    public void setUfficioCorrenteId(int ufficioCorrenteId) {
        this.ufficioCorrenteId = ufficioCorrenteId;
    }

    public int getUfficioSelezionatoId() {
        return ufficioSelezionatoId;
    }

    public void setUfficioSelezionatoId(int ufficioSelezionatoId) {
        this.ufficioSelezionatoId = ufficioSelezionatoId;
    }

    public UfficioVO getUfficioCorrente() {
        return ufficioCorrente;
    }

    public void setUfficioCorrente(UfficioVO ufficioCorrente) {
        this.ufficioCorrente = ufficioCorrente;
    }

    /**
     * @return Returns the profiliMenu.
     */
    public Collection getProfiliMenu() {
        return profiliMenu;
    }

    /**
     * @param profiliMenu
     *            The profiliMenu to set.
     */
    public void setProfiliMenu(Collection profiliMenu) {
        this.profiliMenu = profiliMenu;
    }

    /**
     * @return Returns the profiloId.
     */
    public int getProfiloId() {
        return profiloId;
    }

    /**
     * @param profiloId
     *            The profiloId to set.
     */
    public void setProfiloId(int profiloId) {
        this.profiloId = profiloId;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        setFunzioniMenuSelezionate(null);
    }

    public void inizializzaForm() {
        setAbilitato(true);
        setCodiceFiscale(null);
        setCognome(null);
        setConfermaPassword(null);
        setDataFineAttivita(null);
        setEmailAddress(null);
        setId(0);
        setMatricola(null);
        setNome(null);
        setPassWord(null);
        setRegistriSelezionatiId(null);
        setUfficiSelezionatiId(null);
        setUserName(null);
        setUfficiAssegnati(null);
        rimuoviUffici();
    }
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (getUserName() == null || "".equals(getUserName())) {
            errors.add("username", new ActionMessage("campo.obbligatorio",
                    "Username", ""));
        }
        if (getPassWord() == null || "".equals(getPassWord())) {
            errors.add("password", new ActionMessage("campo.obbligatorio",
                    "Password", ""));
        }
        if (getConfermaPassword() == null || "".equals(getConfermaPassword())) {
            errors.add("confermaPassword", new ActionMessage(
                    "campo.obbligatorio", "Conferma Password", ""));
        }
        if (getConfermaPassword() != null && getPassWord() != null
                && !"".equals(getConfermaPassword())
                && !"".equals(getPassWord())
                && !getConfermaPassword().equals(getPassWord())) {
            errors.add("confermaPassword", new ActionMessage(
                    "password_mismatch"));
        }
        if (getCognome() == null || "".equals(getCognome())) {
            errors.add("cognome", new ActionMessage("campo.obbligatorio",
                    "Cognome", ""));
        }
        if (getNome() == null || "".equals(getNome())) {
            errors.add("nome", new ActionMessage("campo.obbligatorio", "Nome",
                    ""));
        }
        if (getCodiceFiscale() == null || "".equals(getCodiceFiscale())) {
            errors.add("codiceFiscale", new ActionMessage("campo.obbligatorio",
                    "Codice Fiscale", ""));
        }
        if (getDataFineAttivita() != null && !"".equals(getDataFineAttivita())
                && !DateUtil.isData(getDataFineAttivita())) {
            errors.add("formato.data.errato", new ActionMessage(
                    "formato.data.errato", "data fine attivit�"));
        }
        if (getRegistriSelezionatiId() == null) {
            errors.add("registriSelezionatiId", new ActionMessage(
                    "selezione.obbligatoria",
                    "almeno un Registro di competenza", ""));
        }

        return errors;
    }

    /**
     * @return Returns the registri.
     */
    public Collection getRegistri() {
        return registri;
    }

    /**
     * @param registri
     *            The registri to set.
     */
    public void setRegistri(Collection registri) {
        this.registri = registri;
    }

    /**
     * @return Returns the registriSelezionatiId.
     */
    public String[] getRegistriSelezionatiId() {
        return registriSelezionatiId;
    }

    /**
     * @param registriSelezionatiId
     *            The registriSelezionatiId to set.
     */
    public void setRegistriSelezionatiId(String[] registriSelezionatiId) {
        this.registriSelezionatiId = registriSelezionatiId;
    }

    /**
     * @return Returns the funzioniMenu.
     */
    public Collection getFunzioniMenu() {
        return funzioniMenu;
    }

    /**
     * @param funzioniMenu
     *            The funzioniMenu to set.
     */
    public void setFunzioniMenu(Collection funzioniMenu) {
        this.funzioniMenu = funzioniMenu;
    }

    /**
     * @return Returns the funzioniMenuSelezionate.
     */
    public String[] getFunzioniMenuSelezionate() {
        return funzioniMenuSelezionate;
    }

    /**
     * @param funzioniMenuSelezionate
     *            The funzioniMenuSelezionate to set.
     */
    public void setFunzioniMenuSelezionate(String[] funzioniMenuSelezionate) {
        this.funzioniMenuSelezionate = funzioniMenuSelezionate;
    }
}