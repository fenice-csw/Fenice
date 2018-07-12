
package it.flosslab.mvc.presentation.actionform.amministrazione;

import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.util.NumberUtil;
import it.flosslab.mvc.vo.OggettoVO;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class OggettiActionForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	
	private String descrizione;
	private String giorniAlert;
	private String oggettoId;
	private Map<String,AssegnatarioView> assegnatari;
	private String[] assegnatariSelezionatiId;
	private int ufficioCorrenteId;
	private String ufficioCorrentePath;
	private int ufficioSelezionatoId;
	private UfficioVO ufficioCorrente;
	private Collection<UfficioVO> ufficiDipendenti;
	private Collection<OggettoVO> oggettiCollection;

	public OggettiActionForm() {
		oggettoId = null;
		descrizione = null;
		giorniAlert = null;
		assegnatari = new HashMap<String,AssegnatarioView>(2);
	}

	public String getGiorniAlert() {
		return giorniAlert;
	}

	public void setGiorniAlert(String giorniAlert) {
		this.giorniAlert = giorniAlert;
	}

	public String getOggettoId() {
		return oggettoId;
	}

	public void setOggettoId(String oggettoId) {
		this.oggettoId = oggettoId;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String[] getAssegnatariSelezionatiId() {
		return assegnatariSelezionatiId;
	}

	public void setAssegnatariSelezionatiId(String[] assegnatari) {
		this.assegnatariSelezionatiId = assegnatari;
	}

	public void removeAssegnatari() {
		if (assegnatari != null)
			assegnatari.clear();
	}

	public Collection<AssegnatarioView> getAssegnatari() {
		return assegnatari.values();
	}

	public void aggiungiAssegnatario(AssegnatarioView ass) {
		assegnatari.put(ass.getKey(), ass);
	}

	public void rimuoviAssegnatario(String key) {
		assegnatari.remove(key);
	}

	public Collection<UfficioVO> getUfficiDipendenti() {
		return ufficiDipendenti;
	}

	public void setUfficiDipendenti(Collection<UfficioVO> ufficiDipendenti) {
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

	public void setUfficioSelezionatoId(int ufficioCorrenteId) {
		this.ufficioSelezionatoId = ufficioCorrenteId;
	}

	public UfficioVO getUfficioCorrente() {
		return ufficioCorrente;
	}

	public void setUfficioCorrente(UfficioVO ufficioCorrente) {
		this.ufficioCorrente = ufficioCorrente;
	}

	public String getUfficioCorrentePath() {
		return ufficioCorrentePath;
	}

	public void setUfficioCorrentePath(String ufficioCorrentePath) {
		this.ufficioCorrentePath = ufficioCorrentePath;
	}
	
	public Collection<OggettoVO> getOggettiCollection() {
		return oggettiCollection;
	}

	public void setOggettiCollection(Collection<OggettoVO> oggettiCollection) {
		this.oggettiCollection = oggettiCollection;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		if (request.getParameter("salvaAction") != null) {
			if (getDescrizione() == null || getDescrizione().equals("")) {
				errors.add("oggetto", new ActionMessage("campo.obbligatorio",
						"Descrizione", ""));
			}
			if (getGiorniAlert() != null && !"".equals(getGiorniAlert())
					&& !(NumberUtil.isInteger(getGiorniAlert()))) {
				errors.add("giorniAlert", new ActionMessage(
						"formato.numerico.errato", "Numero di giorni (alert)"));
			}
		}
		return errors;
	}
}
