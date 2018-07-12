package it.compit.fenice.mvc.presentation.actionform.ammtrasparente;

import it.compit.fenice.enums.UnitaAmministrativaEnum;
import it.compit.fenice.mvc.presentation.helper.DocumentoAmmTrasparenteView;
import it.compit.fenice.mvc.vo.ammtrasparente.AmmTrasparenteVO;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class AmmTrasparenteForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private String descrizione;

	private int sezioneId;

	private Collection<AmmTrasparenteVO> sezioni;

	private Collection<DocumentoAmmTrasparenteView> documentiSezione;

	private Collection<DocumentoAmmTrasparenteView> documentiDaSezionale;
	
	private AssegnatarioView responsabile;

	private UfficioVO ufficioCorrenteResponsabile;

	private Collection<UfficioVO> ufficiDipendentiResponsabile;

	private Collection<UtenteVO> utenti;

	private int ufficioResponsabileCorrenteId;

	private String ufficioResponsabileCorrentePath;

	private int ufficioResponsabileSelezionatoId;

	private int utenteResponsabileSelezionatoId;

	private int flagWeb;

	public AmmTrasparenteForm() {
		this.sezioni = new ArrayList<AmmTrasparenteVO>();
		this.descrizione = null;
		this.sezioneId = 0;
		setResponsabile(null);
		this.ufficioResponsabileCorrenteId=0;
	    this.flagWeb=0;
	}

	public int getFlagWeb() {
		return flagWeb;
	}

	public void setFlagWeb(int flagWeb) {
		this.flagWeb = flagWeb;
	}

	public Collection<DocumentoAmmTrasparenteView> getDocumentiDaSezionale() {
		return documentiDaSezionale;
	}

	public void setDocumentiDaSezionale(Collection<DocumentoAmmTrasparenteView> documentiDaSezionale) {
		this.documentiDaSezionale = documentiDaSezionale;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Collection<AmmTrasparenteVO> getSezioni() {
		return sezioni;
	}

	public void setSezioni(Collection<AmmTrasparenteVO> sezioni) {
		this.sezioni = sezioni;
	}

	public Collection<DocumentoAmmTrasparenteView> getDocumentiSezione() {
		return documentiSezione;
	}

	public void setDocumentiSezione(Collection<DocumentoAmmTrasparenteView> documentiSezione) {
		this.documentiSezione = documentiSezione;
	}

	public int getSezioneId() {
		return sezioneId;
	}

	public void setSezioneId(int sezioneId) {
		this.sezioneId = sezioneId;
	}

	public Collection<UfficioVO> getUfficiDipendentiResponsabile() {
		return ufficiDipendentiResponsabile;
	}

	public void setUfficiDipendentiResponsabile(Collection<UfficioVO> ufficiDipendenti) {
		this.ufficiDipendentiResponsabile = ufficiDipendenti;
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

	public AssegnatarioView getResponsabile() {
		return responsabile;
	}

	public void setResponsabile(AssegnatarioView responsabile) {
		this.responsabile = responsabile;
	}
	
	public UnitaAmministrativaEnum getUnitaAmministrativa(){
		return Organizzazione.getInstance().getValueObject().getUnitaAmministrativa();
	}
	
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {

		ActionErrors errors = new ActionErrors();

		if (request.getParameter("btnConferma") != null) {

			if (getDescrizione() == null || "".equals(getDescrizione().trim())) {
				errors.add("Nome", new ActionMessage("campo.obbligatorio",
						"Nome", ""));
			}
			
			if (getResponsabile() == null) {
				errors.add("Responsabile", new ActionMessage("campo.obbligatorio",
						"Responsabile del Sezione", ""));
			}

		}
		return errors;
	}

	public void resetForm() {
		this.descrizione = null;
		this.sezioneId = 0;
		setResponsabile(null);
		this.ufficioResponsabileCorrenteId=0;
		this.flagWeb=0;
		
	}

}
