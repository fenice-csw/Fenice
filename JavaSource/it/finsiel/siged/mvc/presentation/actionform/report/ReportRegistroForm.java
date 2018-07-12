package it.finsiel.siged.mvc.presentation.actionform.report;

import it.compit.fenice.enums.TagConservazioneEnum;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.AlberoUfficiUtentiForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;

import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class ReportRegistroForm extends ReportCommonForm implements
		AlberoUfficiUtentiForm {

	private static final long serialVersionUID = 1L;

	private Utente utenteCorrente;

	private String tipoProtocollo;

	private String ufficioSelezionato;

	private String numInizio;

	private String numFine;

	private AssegnatarioView selezionato;

	private boolean isVelina = false;
	
	private boolean flagConoscenza = false;

	private int totalReg; 
	
	private int totalMod;
	
	private boolean showReportDownload;
	
	private boolean recordNotFound;
	
	private int anno;
	
	//PER CONSERVAZIONE DATI -> GESTIONE ARCHIVI
	private TreeMap<TagConservazioneEnum,String> metadati;
	private boolean gaAbilitata;



	
	public void inizialize(boolean isVelina) {
		if (getDataInizio() == null)
			setDataInizio(DateUtil.formattaData(System.currentTimeMillis()));
		if (getDataFine() == null)
			setDataFine(DateUtil.formattaData(System.currentTimeMillis()));
		this.isVelina = isVelina;
		if (isVelina)
			if (getTipoProtocollo() == null || getTipoProtocollo().trim().equalsIgnoreCase(""))
				setTipoProtocollo("P','I");
	}

	
	public boolean isGaAbilitata() {
		return gaAbilitata;
	}

	public void setGaAbilitata(boolean gaAbilitata) {
		this.gaAbilitata = gaAbilitata;
	}

	public boolean isShowReportDownload() {
		return showReportDownload;
	}

	public void setShowReportDownload(boolean showReportDownload) {
		this.showReportDownload = showReportDownload;
	}

	public boolean isRecordNotFound() {
		return recordNotFound;
	}

	public void setRecordNotFound(boolean recordNotFound) {
		this.recordNotFound = recordNotFound;
	}

	public int getTotalReg() {
		return totalReg;
	}

	public void setTotalReg(int totalReg) {
		this.totalReg = totalReg;
	}

	public int getTotalMod() {
		return totalMod;
	}



	public void setTotalMod(int totalMod) {
		this.totalMod = totalMod;
	}

	public int getFlagConoscenza() {
		return flagConoscenza?0:1;
	}
	
	public boolean isFlagConoscenza() {
		return flagConoscenza;
	}

	public void setFlagConoscenza(boolean flagConoscenza) {
		this.flagConoscenza = flagConoscenza;
	}

	public String getUfficioSelezionato() {
		return ufficioSelezionato;
	}

	public AssegnatarioView getSelezionato() {
		return selezionato;
	}

	public String getSelezionatoDescription() {
		String desc = selezionato.getDescrizioneUfficio();
		if (selezionato.getUtenteId() != 0)
			desc +=selezionato.getNomeUtente();
		return desc;
	}

	public void setSelezionato(AssegnatarioView selezionato) {
		this.selezionato = selezionato;
	}

	public String getNumInizio() {
		return numInizio;
	}

	public String getNumFine() {
		return numFine;
	}

	public void setNumInizio(String numInizio) {
		this.numInizio = numInizio;
	}

	public void setNumFine(String numFine) {
		this.numFine = numFine;
	}

	public void setUfficioSelezionato(String ufficioSelezionato) {
		this.ufficioSelezionato = ufficioSelezionato;
	}

	public String getTipoProtocollo() {
		return tipoProtocollo;
	}

	public void setTipoProtocollo(String tipoProtocollo) {
		this.tipoProtocollo = tipoProtocollo;
	}

	public TreeMap<TagConservazioneEnum, String> getMetadati() {
		return metadati;
	}

	public void setMetadati(TreeMap<TagConservazioneEnum, String> metadati) {
		this.metadati = metadati;
	}

	public boolean getTuttiUffici() {
		if (getUfficioCorrente() == null)
			return false;
		boolean tutti = getUfficioCorrente().getParentId() == 0;
		if (!tutti) {
			tutti = getUfficioCorrente().getId().equals(
					utenteCorrente.getUfficioVOInUso().getId());
		}
		return tutti;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.utenteCorrente = (Utente) request.getSession().getAttribute(
				Constants.UTENTE_KEY);
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		if(getBtnStampa() != null)
			if (getSelezionato() == null && isVelina) {
					errors.add("Seleziona ufficio", new ActionMessage(
							"campo.obbligatorio", "Assegnatario", ""));
			}

		if (getDataInizio() == null || "".equals(getDataInizio())) {
			errors.add("dataInizio", new ActionMessage("campo.obbligatorio",
					"data inizio", ""));
		} else if (!DateUtil.isData(getDataInizio())) {
			errors.add("dataInizio", new ActionMessage("formato.data.errato",
					"data inizio", ""));
		}
		if (getDataFine() == null || "".equals(getDataFine())) {
			errors.add("dataFine", new ActionMessage("campo.obbligatorio",
					"data fine", ""));
		} else if (!DateUtil.isData(getDataFine())) {
			errors.add("dataFine", new ActionMessage("formato.data.errato",
					"data fine", ""));
		}
		if (getDataFine() != null
				&& !"".equals(getDataFine().trim())
				&& getDataInizio() != null
				&& !"".equals(getDataInizio().trim())
				&& DateUtil.toDate(getDataFine()).before(
						DateUtil.toDate(getDataInizio()))) {
			errors.add("dataInizio", new ActionMessage("date_incongruenti"));
		}

		return errors;
	}
	
	public ActionErrors validateConservazioneRegistroGiornaliero(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		if(getBtnStampa() != null)

		if (getDataInizio() == null || "".equals(getDataInizio())) {
			errors.add("dataInizio", new ActionMessage("campo.obbligatorio",
					"data inizio", ""));
		} else if (!DateUtil.isData(getDataInizio())) {
			errors.add("dataInizio", new ActionMessage("formato.data.errato",
					"data inizio", ""));
		}
		return errors;
	}

	public ActionErrors validateConservazioneRegistroAnnuale(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		if(getBtnStampa() != null)

		if (getAnno() == 0) {
			errors.add("anno", new ActionMessage("campo.obbligatorio",
					"Anno", ""));
		} else if (NumberUtil.isInteger(String.valueOf(getAnno())) && getAnno() < Parametri.ANNO_INIZIO_CONTROLLO) {
			errors.add("anno", new ActionMessage("formato.anno.errato",
					"Anno", ""));
		}
		return errors;
	}

	public int getAnno() {
		return anno;
	}


	public void setAnno(int anno) {
		this.anno = anno;
	}
	

}