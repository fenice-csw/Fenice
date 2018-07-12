package it.compit.fenice.mvc.presentation.actionform.repertori;

import it.compit.fenice.enums.UnitaAmministrativaEnum;
import it.compit.fenice.mvc.presentation.helper.DocumentoRepertorioView;
import it.compit.fenice.mvc.vo.repertori.RepertorioVO;
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

public class RepertorioForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private String descrizione;

	private int repertorioId;

	private Collection<RepertorioVO> repertori;

	private Collection<DocumentoRepertorioView> documentiRepertorio;

	private Collection<DocumentoRepertorioView> documentiDaRepertoriale;
	
	private AssegnatarioView responsabile;

	private UfficioVO ufficioCorrenteResponsabile;

	private Collection<UfficioVO> ufficiDipendentiResponsabile;

	private Collection<UtenteVO> utenti;

	private int ufficioResponsabileCorrenteId;

	private String ufficioResponsabileCorrentePath;

	private int ufficioResponsabileSelezionatoId;

	private int utenteResponsabileSelezionatoId;

	private int flagWeb;

	
	public RepertorioForm() {
		this.repertori = new ArrayList<RepertorioVO>();
		this.descrizione = null;
		this.repertorioId = 0;
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
	
	
	public Collection<DocumentoRepertorioView> getDocumentiDaRepertoriale() {
		return documentiDaRepertoriale;
	}

	public void setDocumentiDaRepertoriale(Collection<DocumentoRepertorioView> documentiDaRepertoriale) {
		this.documentiDaRepertoriale = documentiDaRepertoriale;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Collection<RepertorioVO> getRepertori() {
		return repertori;
	}

	public void setRepertori(Collection<RepertorioVO> repertori) {
		this.repertori = repertori;
	}

	public Collection<DocumentoRepertorioView> getDocumentiRepertorio() {
		return documentiRepertorio;
	}

	public void setDocumentiRepertorio(Collection<DocumentoRepertorioView> documentiRepertori) {
		this.documentiRepertorio = documentiRepertori;
	}

	public int getRepertorioId() {
		return repertorioId;
	}

	public void setRepertorioId(int repertorioId) {
		this.repertorioId = repertorioId;
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
						"Responsabile del Repertorio", ""));
			}

		}
		return errors;
	}

	public void resetForm() {
		this.descrizione = null;
		this.repertorioId = 0;
		setResponsabile(null);
		this.ufficioResponsabileCorrenteId=0;
		this.flagWeb=0;
		
	}

}
