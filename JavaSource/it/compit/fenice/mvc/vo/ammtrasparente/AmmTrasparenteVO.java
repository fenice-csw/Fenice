package it.compit.fenice.mvc.vo.ammtrasparente;

import it.compit.fenice.mvc.bo.AmmTrasparenteBO;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.vo.VersioneVO;

public class AmmTrasparenteVO extends VersioneVO {

	private static final long serialVersionUID = 1L;
	
	public static final int SEZIONE_INTERNA=0;
	
	public static final int SEZIONE_WEB=1;

	private int sezioneId;

	private int aooId;
	
	private String descrizione;
	
	private int responsabileId;
	
	private int flagWeb;
	
	public AmmTrasparenteVO() {
	}

		
	public int getFlagWeb() {
		return flagWeb;
	}

	public void setFlagWeb(int flagWeb) {
		this.flagWeb = flagWeb;
	}

	public int getSezioneId() {
		return sezioneId;
	}

	public void setSezioneId(int sezioneId) {
		this.sezioneId = sezioneId;
	}

	public int getAooId() {
		return aooId;
	}

	public void setAooId(int aooId) {
		this.aooId = aooId;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public int getResponsabileId() {
		return responsabileId;
	}

	public void setResponsabileId(int responsabileId) {
		this.responsabileId = responsabileId;
	}
	
	public String getNomeResponsabile() {
		Organizzazione org = Organizzazione.getInstance();
		if (responsabileId != 0) {
			Ufficio uff = org.getUfficio(responsabileId);
			AssegnatarioView ass=AmmTrasparenteBO.newResponsabile(uff);
			return ass.getDescrizioneUfficio();
		}
		return "";
	}
}
