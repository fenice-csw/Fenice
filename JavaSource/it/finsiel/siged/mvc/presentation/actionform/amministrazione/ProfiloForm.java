package it.finsiel.siged.mvc.presentation.actionform.amministrazione;

import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.business.AmministrazioneDelegate;
import it.finsiel.siged.mvc.presentation.action.amministrazione.ProfiloAction;
import it.finsiel.siged.mvc.presentation.helper.MenuView;
import it.finsiel.siged.mvc.vo.organizzazione.MenuVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class ProfiloForm extends ActionForm {

    static Logger logger = Logger.getLogger(ProfiloAction.class.getName());

    private int id;

    private String codiceProfilo;

    private String descrizioneProfilo;

    private String dataInizioValidita;

    private String dataFineValidita;

    private Collection funzioniMenu;

    private HashMap funzioniProfilo;

    private String[] profiliMenu;

    private Collection profili;

    private String nuovo;

    
    // Modifiche Flosslab
    // Author Daniele Sanna
    // Date 09/12/2008
    
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
     * @return Returns the codiceProfilo.
     */
    public String getCodiceProfilo() {
        return codiceProfilo;
    }

    /**
     * @param codiceProfilo
     *            The codiceProfilo to set.
     */
    public void setCodiceProfilo(String codiceProfilo) {
        this.codiceProfilo = codiceProfilo;
    }

    /**
     * @return Returns the dataFineValidita.
     */
    public String getDataFineValidita() {
        return dataFineValidita;
    }

    /**
     * @param dataFineValidita
     *            The dataFineValidita to set.
     */
    public void setDataFineValidita(String dataFineValidita) {
        this.dataFineValidita = dataFineValidita;
    }

    /**
     * @return Returns the dataInizioValidita.
     */
    public String getDataInizioValidita() {
        return dataInizioValidita;
    }

    /**
     * @param dataInizioValidita
     *            The dataInizioValidita to set.
     */
    public void setDataInizioValidita(String dataInizioValidita) {
        this.dataInizioValidita = dataInizioValidita;
    }

    /**
     * @return Returns the descrizioneProfilo.
     */
    public String getDescrizioneProfilo() {
        return descrizioneProfilo;
    }

    /**
     * @param descrizioneProfilo
     *            The descrizioneProfilo to set.
     */
    public void setDescrizioneProfilo(String descrizioneProfilo) {
        this.descrizioneProfilo = descrizioneProfilo;
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
     * @param funzioniProfilo
     *            The funzioniProfilo to set.
     */
    public void setFunzioniProfilo(HashMap funzioniProfilo) {
        this.funzioniProfilo = funzioniProfilo;
    }

    /**
     * @return Returns the id.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    public HashMap getFunzioniProfilo() {
        return funzioniProfilo;
    }

    public Collection getFunzioniProfiloCollection() {
        if (funzioniProfilo != null) {
            return funzioniProfilo.values();
        } else
            return null;
    }

    public void addFunzioniProfilo(ProtocolloVO protocolloVO) {
        if (protocolloVO != null) {
            funzioniProfilo.put(protocolloVO.getId(), protocolloVO);
        }
    }

    public void removeFunzioniProfilo(Integer protocolloId) {
        funzioniProfilo.remove(protocolloId);
    }

    public void removeFunzioniProfilo(int protocolloId) {
        removeFunzioniProfilo(new Integer(protocolloId));
    }

    public void removeFunzioniProfilo(ProtocolloVO protocolloVO) {
        if (protocolloVO != null) {
            removeFunzioniProfilo(protocolloVO.getId());
        }
    }

    public void removeFunzioniProfilo() {
        if (funzioniProfilo != null) {
            funzioniProfilo.clear();
        }
    }

    /**
     * @return Returns the profiloMenu.
     */
    public String[] getProfiliMenu() {
        return profiliMenu;
    }

    /**
     * @param profiloMenu
     *            The profiloMenu to set.
     */
    public void setProfiliMenu(String[] profiliMenu) {
        this.profiliMenu = profiliMenu;
    }

    public ProtocolloVO getProtocolloVO(Integer protocolloId) {
        return (ProtocolloVO) funzioniProfilo.get(protocolloId);
    }

    /**
     * @return Returns the btnNuovo.
     */
    public String getNuovo() {
        return nuovo;
    }

    /**
     * @param btnNuovo
     *            The btnNuovo to set.
     */
    public void setNuovo(String nuovo) {
        this.nuovo = nuovo;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        setProfiliMenu(null);
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (request.getParameter("btnConferma") != null) {
            if (codiceProfilo == null || "".equals(codiceProfilo)) {
                errors.add("codiceProfilo", new ActionMessage(
                        "campo.obbligatorio", "Codice Profilo", ""));
            } else if (descrizioneProfilo == null
                    || "".equals(descrizioneProfilo)) {
                errors.add("descrizioneProfilo", new ActionMessage(
                        "campo.obbligatorio", "Descrizione Profilo", ""));
            } else if (profiliMenu == null) {
                errors.add("profiliMenu", new ActionMessage(
                        "campo.obbligatorio", "voce di Menu", ""));
            }

        }
        return errors;

    }

    public void inizializzaForm(int aooId) {
        setCodiceProfilo(null);
        setDescrizioneProfilo(null);
        setId(0);
        setFunzioniProfilo(null);
        setProfiliMenu(null);
        setProfili(AmministrazioneDelegate.getInstance().getProfili(aooId));
    }

    /**
     * @return Returns the profili.
     */
    public Collection getProfili() {
        return profili;
    }

    /**
     * @param profili
     *            The profili to set.
     */
    public void setProfili(Collection profili) {
        this.profili = profili;
    }
}