package it.finsiel.siged.mvc.presentation.actionform.amministrazione.org.utenti;

import it.compit.fenice.mvc.vo.organizzazione.StoricoOrganigrammaVO;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.AlberoUfficiUtentiForm;
import it.finsiel.siged.mvc.presentation.helper.UtenteView;
import it.finsiel.siged.mvc.vo.organizzazione.TitolarioUfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class UfficioForm extends ActionForm implements
		AlberoUfficiUtentiForm {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(UfficioForm.class.getName());

	private int id;

	private Boolean attivo;
	
	private Boolean ufficioProtocollo;

	private String tipo;

	private Boolean accettazioneAutomatica;

	private int aooId;

	private int parentId;

	private String codice;

	private String name;

	private String description;

	private Collection<UtenteView> dipendentiUfficio = new ArrayList<UtenteView>();

	private Collection<UfficioVO> uffici = new ArrayList<UfficioVO>();

	// assegnatari
	private int ufficioCorrenteId;

	private int ufficioSelezionatoId;

	private String ufficioCorrentePath;

	private UfficioVO ufficioCorrente;

	private Collection<UfficioVO> pathUffici = new ArrayList<UfficioVO>();

	private Collection<UfficioVO> ufficiDipendenti = new ArrayList<UfficioVO>();

	private String impostaUfficioAction;

	private int ufficiAssegnatiSelezionatiId;

	private Collection<UfficioVO> ufficiAssegnati;

	private String[] referentiId;

	private Collection<UtenteVO> utenti;

	private int utenteSelezionatoId;

	private Map<Integer, UtenteVO> utentiResponsabile = new HashMap<Integer, UtenteVO>();

	private int utenteResponsabileSelezionatoId;

	private int utenteResponsabileId;

	private boolean utenteResponsabileModificabile;

	private String piano;

	private String stanza;

	private String telefono;

	private String fax;

	private String email;

	private String emailUsername;

	private String emailPassword;

	private int caricaDirigenteId;
	
	private int caricaResponsabileUfficioProtocolloId;

	private Ufficio ufficioDaSpostare;
	
    private Collection<StoricoOrganigrammaVO> storicoOrganigramma;
    
    private boolean storicoSaved;
    
	public Boolean getUfficioProtocollo() {
		return ufficioProtocollo;
	}

	public void setUfficioProtocollo(Boolean ufficioProtocollo) {
		this.ufficioProtocollo = ufficioProtocollo;
	}

	public boolean isStoricoSaved() {
		return storicoSaved;
	}

	public void setStoricoSaved(boolean storicoSaved) {
		this.storicoSaved = storicoSaved;
	}

	public Collection<StoricoOrganigrammaVO> getStoricoOrganigramma() {
		return storicoOrganigramma;
	}

	public void setStoricoOrganigramma(Collection<StoricoOrganigrammaVO> storicoOrganigramma) {
		this.storicoOrganigramma = storicoOrganigramma;
	}

	public boolean isSpostabile() {
		if (this.ufficioDaSpostare != null)
			if (this.ufficioCorrenteId == this.ufficioDaSpostare.getValueObject().getId())
				return false;
		return true;
	}
	
	public Ufficio getUfficioDaSpostare() {
		return ufficioDaSpostare;
	}

	public void setUfficioDaSpostare(Ufficio ufficioDaSpostare) {
		this.ufficioDaSpostare = ufficioDaSpostare;
	}

	public String getEmailUsername() {
		return emailUsername;
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailUsername(String emailUsername) {
		this.emailUsername = emailUsername;
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	public int getCaricaDirigenteId() {
		return caricaDirigenteId;
	}

	public void setCaricaDirigenteId(int caricaDirigenteId) {
		this.caricaDirigenteId = caricaDirigenteId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPiano() {
		return piano;
	}

	public String getStanza() {
		return stanza;
	}

	public String getTelefono() {
		return telefono;
	}

	public String getFax() {
		return fax;
	}

	public void setPiano(String piano) {
		this.piano = piano;
	}

	public void setStanza(String stanza) {
		this.stanza = stanza;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	private Collection<TitolarioUfficioVO> vociTitolario = new ArrayList<TitolarioUfficioVO>();

	public Collection<TitolarioUfficioVO> getVociTitolario() {
		return vociTitolario;
	}

	public void setVociTitolario(Collection<TitolarioUfficioVO> vociTitolario) {
		this.vociTitolario = vociTitolario;
	}

	public Map<Integer, UtenteVO> getUtentiResponsabile() {
		return utentiResponsabile;
	}

	public boolean isUtenteResponsabileModificabile() {
		return utenteResponsabileModificabile;
	}

	public void setUtenteResponsabileModificabile(
			boolean utenteResponsabileModificabile) {
		this.utenteResponsabileModificabile = utenteResponsabileModificabile;
	}

	public Collection<UtenteVO> getUtentiResponsabileCollection() {
		return utentiResponsabile.values();
	}

	public void setUtentiResponsabile(Collection<UtenteVO> utenti) {
		for (UtenteVO u : utenti)
			this.utentiResponsabile.put(u.getId(), u);
	}

	public int getUtenteResponsabileSelezionatoId() {
		return utenteResponsabileSelezionatoId;
	}

	public void setUtenteResponsabileSelezionatoId(int utenteSelezionatoId) {
		this.utenteResponsabileSelezionatoId = utenteSelezionatoId;
	}

	public int getUtenteResponsabileId() {
		return utenteResponsabileId;
	}

	public void setUtenteResponsabileId(int utenteSelezionatoId) {
		this.utenteResponsabileId = utenteSelezionatoId;
	}

	public UtenteVO getUtenteResponsabile() {
		if (this.utenteResponsabileSelezionatoId != 0)
			return (UtenteVO) this.utentiResponsabile
					.get(this.utenteResponsabileSelezionatoId);
		else
			return null;
	}

	public void setUtente(UtenteVO utenteCorrente) {
	}

	//

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImpostaUfficioAction() {
		return impostaUfficioAction;
	}

	/**
	 * @return Returns the ufficioCorrentePath.
	 */
	public String getUfficioCorrentePath() {
		return ufficioCorrentePath;
	}

	/**
	 * @param ufficioCorrentePath
	 *            The ufficioCorrentePath to set.
	 */
	public void setUfficioCorrentePath(String ufficioCorrentePath) {
		this.ufficioCorrentePath = ufficioCorrentePath;
	}

	/**
	 * @param impostaUfficioAction
	 *            The impostaUfficioAction to set.
	 */
	public void setImpostaUfficioAction(String impostaUfficioAction) {
		this.impostaUfficioAction = impostaUfficioAction;
	}

	/**
	 * @return Returns the pathUffici.
	 */
	public Collection<UfficioVO> getPathUffici() {
		return pathUffici;
	}

	/**
	 * @param pathUffici
	 *            The pathUffici to set.
	 */
	public void setPathUffici(Collection<UfficioVO> pathUffici) {
		this.pathUffici = pathUffici;
	}


	public Collection<UfficioVO> getUfficiDipendenti() {
		return ufficiDipendenti;
	}


	

	public void setUfficiDipendenti(Collection<UfficioVO> ufficiDipendenti) {
		this.ufficiDipendenti = ufficiDipendenti;
	}

	public Ufficio getUfficioPadre() {
		Organizzazione org = Organizzazione.getInstance();
		if (parentId > 0)
			return org.getUfficio(parentId);
		else
			return null;
	}

	/**
	 * @return Returns the ufficioCorrenteId.
	 */
	public int getUfficioCorrenteId() {
		return ufficioCorrenteId;
	}

	/**
	 * @param ufficioCorrenteId
	 *            The ufficioCorrenteId to set.
	 */
	public void setUfficioCorrenteId(int ufficioCorrenteId) {
		this.ufficioCorrenteId = ufficioCorrenteId;
	}

	/**
	 * @return Returns the ufficioSelezionatoId.
	 */
	public int getUfficioSelezionatoId() {
		return ufficioSelezionatoId;
	}

	/**
	 * @param ufficioSelezionatoId
	 *            The ufficioSelezionatoId to set.
	 */
	public void setUfficioSelezionatoId(int ufficioSelezionatoId) {
		this.ufficioSelezionatoId = ufficioSelezionatoId;
	}

	/**
	 * @return Returns the logger.
	 */
	public static Logger getLogger() {
		return logger;
	}

	/**
	 * @param logger
	 *            The logger to set.
	 */
	public static void setLogger(Logger logger) {
		UfficioForm.logger = logger;
	}

	/**
	 * @return Returns the accettazioneAutomatica.
	 */
	public Boolean getAccettazioneAutomatica() {
		return accettazioneAutomatica;
	}

	/**
	 * @param accettazioneAutomatica
	 *            The accettazioneAutomatica to set.
	 */
	public void setAccettazioneAutomatica(Boolean accettazioneAutomatica) {
		this.accettazioneAutomatica = accettazioneAutomatica;
	}

	/**
	 * @return Returns the aooId.
	 */
	public int getAooId() {
		return aooId;
	}

	/**
	 * @param aooId
	 *            The aooId to set.
	 */
	public void setAooId(int aooId) {
		this.aooId = aooId;
	}

	/**
	 * @return Returns the attivo.
	 */
	public Boolean getAttivo() {
		return attivo;
	}

	/**
	 * @param attivo
	 *            The attivo to set.
	 */
	public void setAttivo(Boolean attivo) {
		this.attivo = attivo;
	}

	/**
	 * @return Returns the codice.
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @param codice
	 *            The codice to set.
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the parentId.
	 */
	public int getParentId() {
		return parentId;
	}

	/**
	 * @param parentId
	 *            The parentId to set.
	 */
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return Returns the ufficioCorrente.
	 */
	public UfficioVO getUfficioCorrente() {
		return ufficioCorrente;
	}

	/**
	 * @param ufficioCorrente
	 *            The ufficioCorrente to set.
	 */
	public void setUfficioCorrente(UfficioVO ufficioCorrente) {
		this.ufficioCorrente = ufficioCorrente;
	}

	/**
	 * @return Returns the dipendentiUfficio.
	 */
	public Collection<UtenteView> getDipendentiUfficio() {
		return dipendentiUfficio;
	}

	/**
	 * @param dipendentiUfficio
	 *            The dipendentiUfficio to set.
	 */
	public void setDipendentiUfficio(Collection<UtenteView> dipendentiUfficio) {
		this.dipendentiUfficio = dipendentiUfficio;
	}

	/**
	 * @return Returns the ufficiAssegnati.
	 */
	public Collection<UfficioVO> getUfficiAssegnati() {
		return ufficiAssegnati;
	}

	/**
	 * @param ufficiAssegnati
	 *            The ufficiAssegnati to set.
	 */
	public void setUfficiAssegnati(Collection<UfficioVO> ufficiAssegnati) {
		this.ufficiAssegnati = ufficiAssegnati;
	}

	/**
	 * @return Returns the ufficiAssegnatiSelezionatiId.
	 */
	public int getUfficiAssegnatiSelezionatiId() {
		return ufficiAssegnatiSelezionatiId;
	}

	/**
	 * @param ufficiAssegnatiSelezionatiId
	 *            The ufficiAssegnatiSelezionatiId to set.
	 */
	public void setUfficiAssegnatiSelezionatiId(int ufficiAssegnatiSelezionatiId) {
		this.ufficiAssegnatiSelezionatiId = ufficiAssegnatiSelezionatiId;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		setReferentiId(null);
	}

	public void inizializzaForm() {
		setId(0);
		setDescription(null);
		setAttivo(Boolean.TRUE);
		setUfficioProtocollo(Boolean.FALSE);
		setAccettazioneAutomatica(Boolean.TRUE);
		setDipendentiUfficio(null);
		setReferentiId(null);
		setStanza(null);
		setPiano(null);
		setTelefono(null);
		setFax(null);
		setEmail(null);

	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		if (request.getParameter("btnSalva") != null) {
			if (getDescription() == null || "".equals(getDescription().trim())) {
				errors.add("descrizione", new ActionMessage(
						"campo.obbligatorio", "Descrizione Ufficio", ""));
			}

		} else if (request.getParameter("impostaUfficioAction") != null
				&& ufficioSelezionatoId == 0) {
			errors.add("ufficioSelezionatoId", new ActionMessage(
					"campo.obbligatorio", "Ufficio", ""));

		}

		return errors;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Collection<UfficioVO> getUffici() {
		return uffici;
	}

	public void setUffici(Collection<UfficioVO> uffici) {
		this.uffici = uffici;
	}

	public void setUfficioPadre(Ufficio ufficioPadre) {
	}

	public String[] getReferentiId() {
		return referentiId;
	}

	public void setReferentiId(String[] referentiId) {
		this.referentiId = referentiId;
	}

	public Collection<UtenteVO> getUtenti() {
		return utenti;
	}

	public UtenteVO getUtente(int utenteId) {
		for (Iterator<UtenteVO> i = getUtenti().iterator(); i.hasNext();) {
			UtenteVO ute = i.next();
			if (ute.getId().intValue() == utenteId) {
				return ute;
			}
		}
		return null;
	}

	public void setUtenti(Collection<UtenteVO> utenti) {
		this.utenti = utenti;
	}

	public int getUtenteSelezionatoId() {
		return utenteSelezionatoId;
	}

	public void setUtenteSelezionatoId(int utenteCorrenteId) {
		this.utenteSelezionatoId = utenteCorrenteId;
	}

	public int getCaricaResponsabileUfficioProtocolloId() {
		return caricaResponsabileUfficioProtocolloId;
	}

	public void setCaricaResponsabileUfficioProtocolloId(
			int caricaResponsabileUfficioProtocolloId) {
		this.caricaResponsabileUfficioProtocolloId = caricaResponsabileUfficioProtocolloId;
	}

}