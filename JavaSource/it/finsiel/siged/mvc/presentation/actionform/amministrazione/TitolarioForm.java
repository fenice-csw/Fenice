package it.finsiel.siged.mvc.presentation.actionform.amministrazione;

import it.finsiel.siged.mvc.bo.TitolarioBO;
import it.finsiel.siged.mvc.business.TitolarioDelegate;
import it.finsiel.siged.mvc.presentation.actionform.UploaderForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.presentation.helper.DestinatarioView;
import it.finsiel.siged.mvc.vo.lookup.TitolarioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.util.NumberUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 * @author chuwbacca
 * 
 */
public final class TitolarioForm extends UploaderForm {

	private int id;

	private String codice;

	private String descrizione;

	private String parentPath;

	private String parentDescrizione;

	private int aooId;

	private int parentId;

	private String massimario;

	private String giorniMax;

	private Collection titolariFigli;

	private TitolarioVO titolario;

	private int titolarioPrecedenteId;

	private int titolarioSelezionatoId;

	private Collection ufficiDipendenti;

	private Map documentiAllegati;

	private String giorniAlert;

	//

	private AssegnatarioView responsabile;

	private UfficioVO ufficioCorrenteResponsabile;

	private Collection ufficiDipendentiResponsabile;

	private Collection utenti;

	private int ufficioResponsabileCorrenteId;

	private String ufficioResponsabileCorrentePath;

	private int ufficioResponsabileSelezionatoId;

	private int utenteResponsabileSelezionatoId;

	//

	private String[] ufficiTitolario;

	private int versione;

	private Collection storiaTitolario;

	private String[] allegatiSelezionatiId;

	// private boolean spostabile;

	// Amministrazioni partecipanti
	private String[] amministrazioniSelezionateId;

	private Map amministrazioniPartecipanti;

	private Map amministrazioniIds;

	private String nominativoAmministrazione;

	private int idx;

	private int amministrazioneId;

	//

	public Collection getUfficiDipendentiResponsabile() {
		return ufficiDipendentiResponsabile;
	}

	public boolean isSpostabile() {
		if (this.titolario != null)
			if (this.id == this.titolario.getId())
				return false;
		return true;
	}

	public String getNominativoAmministrazione() {
		return nominativoAmministrazione;
	}

	public void setNominativoAmministrazione(String nominativoAmministrazione) {
		this.nominativoAmministrazione = nominativoAmministrazione;
	}

	public int getAmministrazioneId() {
		return amministrazioneId;
	}

	public void setAmministrazioneId(int amministrazioneId) {
		this.amministrazioneId = amministrazioneId;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public String getGiorniAlert() {
		return giorniAlert;
	}

	public void setGiorniAlert(String giorniAlert) {
		this.giorniAlert = giorniAlert;
	}

	public void setUfficiDipendentiResponsabile(Collection ufficiDipendenti) {
		this.ufficiDipendentiResponsabile = ufficiDipendenti;
	}

	public Collection getUtenti() {
		return utenti;
	}

	public UtenteVO getUtente(int utenteId) {
		for (Iterator i = getUtenti().iterator(); i.hasNext();) {
			UtenteVO ute = (UtenteVO) i.next();
			if (ute.getId().intValue() == utenteId) {
				return ute;
			}
		}
		return null;
	}

	public void setUtenti(Collection utenti) {
		this.utenti = utenti;
	}

	public int getUfficioResponsabileCorrenteId() {
		return ufficioResponsabileCorrenteId;
	}

	public void setUfficioResponsabileCorrenteId(int ufficioCorrenteId) {
		this.ufficioResponsabileCorrenteId = ufficioCorrenteId;
	}

	public int getUfficioResponsabileSelezionatoId() {
		return ufficioResponsabileSelezionatoId;
	}

	public void setUfficioResponsabileSelezionatoId(int ufficioCorrenteId) {
		this.ufficioResponsabileSelezionatoId = ufficioCorrenteId;
	}

	public int getUtenteResponsabileSelezionatoId() {
		return utenteResponsabileSelezionatoId;
	}

	public void setUtenteResponsabileSelezionatoId(int utenteCorrenteId) {
		this.utenteResponsabileSelezionatoId = utenteCorrenteId;
	}

	public UfficioVO getUfficioCorrenteResponsabile() {
		return ufficioCorrenteResponsabile;
	}

	public void setUfficioCorrenteResponsabile(UfficioVO ufficioCorrente) {
		this.ufficioCorrenteResponsabile = ufficioCorrente;
	}

	public String getUfficioResponsabileCorrentePath() {
		return ufficioResponsabileCorrentePath;
	}

	public void setUfficioResponsabileCorrentePath(String ufficioCorrentePath) {
		this.ufficioResponsabileCorrentePath = ufficioCorrentePath;
	}

	public Collection getAmministrazioni() {
		return amministrazioniPartecipanti.values();
	}

	public DestinatarioView getAmministrazione(String id) {
		return (DestinatarioView) amministrazioniPartecipanti.get(id);
	}

	public void aggiungiAmministrazione(DestinatarioView destinatario) {
		if (destinatario != null) {

			if (destinatario.getIdx() == 0) {
				int idx = getNextId(amministrazioniIds);
				destinatario.setIdx(idx);
				amministrazioniPartecipanti.put(String.valueOf(idx),
						destinatario);
				amministrazioniIds.put(String.valueOf(idx), new Integer(idx));
			} else {
				amministrazioniIds.put(String.valueOf(destinatario.getIdx()),
						destinatario);
			}

		}
	}

	private static int getNextId(Map m) {
		int max = 0;
		Iterator it = m.keySet().iterator();
		while (it.hasNext()) {
			String id = (String) it.next();
			int cur = NumberUtil.getInt(id);
			if (cur > max)
				max = cur;
		}
		return max + 1;
	}

	public void rimuoviAmministrazione(String id) {
		DestinatarioView removed = (DestinatarioView) amministrazioniPartecipanti
				.get(id);
		int idx = removed.getIdx();
		amministrazioniPartecipanti.remove(String.valueOf(removed.getIdx()));
		amministrazioniPartecipanti.remove(String.valueOf(idx));
	}

	public void rimuoviAmministrazioni() {
		if (amministrazioniPartecipanti != null) {
			this.amministrazioniPartecipanti.clear();
			this.amministrazioniPartecipanti.clear();
		}
	}

	public AssegnatarioView getResponsabile() {
		return responsabile;
	}

	public void setResponsabile(AssegnatarioView responsabile) {
		this.responsabile = responsabile;
	}

	public void setAmministrazioni(Map amministrazioni) {
		this.amministrazioniPartecipanti = amministrazioni;
	}

	public String[] getAmministrazioniSelezionateId() {
		return amministrazioniSelezionateId;
	}

	public void setAmministrazioniSelezionateId(
			String[] amministrazioniSelezionateId) {
		this.amministrazioniSelezionateId = amministrazioniSelezionateId;
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
	 * @return Returns the parentDescrizione.
	 */
	public String getParentDescrizione() {
		return parentDescrizione;
	}

	/**
	 * @param parentDescrizione
	 *            The parentDescrizione to set.
	 */
	public void setParentDescrizione(String parentDescrizione) {
		this.parentDescrizione = parentDescrizione;
	}

	/**
	 * @return Returns the parentPath.
	 */
	public String getParentPath() {
		return parentPath;
	}

	/**
	 * @param parentPath
	 *            The parentPath to set.
	 */
	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
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
	 * @return Returns the descrizione.
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @param descrizione
	 *            The descrizione to set.
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
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
	 * @return Returns the titolario.
	 */
	public TitolarioVO getTitolario() {
		return titolario;
	}

	/**
	 * @param titolario
	 *            The titolario to set.
	 */
	public void setTitolario(TitolarioVO titolario) {
		this.titolario = titolario;
	}

	public Collection getTitolariFigli() {
		return titolariFigli;
	}

	public void setTitolariFigli(Collection titolariDiscendenti) {
		this.titolariFigli = titolariDiscendenti;
	}

	public int getTitolarioSelezionatoId() {
		return titolarioSelezionatoId;
	}

	public void setTitolarioSelezionatoId(int titolarioSelezionatoId) {
		this.titolarioSelezionatoId = titolarioSelezionatoId;
	}

	public int getTitolarioPrecedenteId() {
		return titolarioPrecedenteId;
	}

	public void setTitolarioPrecedenteId(int titolarioPrecedenteId) {
		this.titolarioPrecedenteId = titolarioPrecedenteId;
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

	/**
	 * @return Returns the ufficiDipendenti.
	 */
	public Collection getUfficiDipendenti() {
		return ufficiDipendenti;
	}

	/**
	 * @param ufficiDipendenti
	 *            The ufficiDipendenti to set.
	 */
	public void setUfficiDipendenti(Collection ufficiDipendenti) {
		this.ufficiDipendenti = ufficiDipendenti;
	}

	/**
	 * @return Returns the ufficiTitolario.
	 */
	public String[] getUfficiTitolario() {
		return ufficiTitolario;
	}

	/**
	 * @param ufficiTitolario
	 *            The ufficiTitolario to set.
	 */
	public void setUfficiTitolario(String[] ufficiTitolario) {
		this.ufficiTitolario = ufficiTitolario;
	}

	public String getMassimario() {
		return massimario;
	}

	public void setMassimario(String massimario) {
		this.massimario = massimario;
	}

	public String getGiorniMax() {
		return giorniMax;
	}

	public void setGiorniMax(String giornimax) {
		this.giorniMax = giornimax;
	}

	public int getVersione() {
		return versione;
	}

	public void setVersione(int versione) {
		this.versione = versione;
	}

	public Collection getStoriaTitolario() {
		return storiaTitolario;
	}

	public void setStoriaTitolario(Collection storiaTitolario) {
		this.storiaTitolario = storiaTitolario;
	}

	public String getPathDescrizioneTitolario() {
		if (getTitolario().getId().intValue() > 0)
			return TitolarioBO.getPathDescrizioneTitolario(getTitolario()
					.getId().intValue());
		else
			return "";
	}

	public Collection getDocumentiAllegatiCollection() {
		return documentiAllegati.values();
	}

	public void setDocumentiAllegati(Map documenti) {
		this.documentiAllegati = documenti;
	}

	public Map getDocumentiAllegati() {
		return documentiAllegati;
	}

	public void allegaDocumento(DocumentoVO doc) {
		TitolarioBO.putAllegato(doc, this.documentiAllegati);
	}

	public void rimuoviAllegato(String allegatoId) {
		DocumentoVO d = (DocumentoVO) documentiAllegati.remove(allegatoId);
	}

	public DocumentoVO getDocumentoAllegato(Object key) {
		return (DocumentoVO) this.documentiAllegati.get(key);
	}

	public DocumentoVO getDocumentoAllegato(int idx) {
		return (DocumentoVO) this.documentiAllegati.get(String.valueOf(idx));
	}

	public String[] getAllegatiSelezionatiId() {
		return allegatiSelezionatiId;
	}

	public void setAllegatiSelezionatiId(String[] allegatiSelezionatoId) {
		this.allegatiSelezionatiId = allegatiSelezionatoId;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		setUfficiTitolario(null);
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		if (request.getParameter("btnConferma") != null) {
			if (codice == null || "".equals(codice.trim())) {
				errors.add("codice", new ActionMessage("campo.obbligatorio",
						"Codice Titolario", ""));
			} else if (descrizione == null || "".equals(descrizione.trim())) {
				errors.add("descrizione", new ActionMessage(
						"campo.obbligatorio", "Descrizione Titolario", ""));
			} else if (getMassimario() != null && !"".equals(getMassimario())
					&& !(NumberUtil.isInteger(getMassimario()))) {
				errors.add("massimario", new ActionMessage(
						"formato.numerico.errato", "Massimario di scarto"));
			}

		} else if (request.getParameter("btnConfermaSposta") != null) {
			if (getTitolario() == null) {
				errors.add("titolario_obbligatorio", new ActionMessage(
						"campo.obbligatorio", "Titolario",
						" nella sezione sposta in .."));
			} else if (getId() == getTitolario().getId().intValue()) {
				errors
						.add(
								"sposta",
								new ActionMessage(
										"titolario.sposta",
										"il livello superiore coincide con la voce selezionata",
										""));
			} 
/*			
			else {
				TitolarioDelegate td = TitolarioDelegate.getInstance();
				if (!td.controlloPermessiUffici(getId(), getTitolario().getId()
						.intValue(), getTitolario().getAooId())) {
					errors
							.add(
									"sposta",
									new ActionMessage(
											"titolario.sposta",
											"l'argomento selezionato non ha i permessi sugli uffici di quello di partenza",
											""));

				}

			}*/

		}
		return errors;
	}

	public TitolarioForm() {
		inizializzaForm();
	}

	public void inizializzaForm() {
		id = 0;
		codice = null;
		descrizione = null;
		massimario = null;
		giorniMax = null;
		giorniAlert = null;
		setAllegatiSelezionatiId(null);
		documentiAllegati = new HashMap(2);
		amministrazioniIds = new HashMap(2);
		amministrazioniPartecipanti = new HashMap();
		setResponsabile(null);
		ufficioResponsabileCorrenteId = 0;
	}

}