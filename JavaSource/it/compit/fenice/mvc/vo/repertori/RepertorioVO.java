package it.compit.fenice.mvc.vo.repertori;

import it.compit.fenice.mvc.bo.RepertorioBO;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.vo.VersioneVO;

public class RepertorioVO extends VersioneVO {

	private static final long serialVersionUID = 1L;
	
	public static final int REPERTORIO_INTERNO=0;
	
	public static final int REPERTORIO_WEB=1;

	private int repertorioId;

	private int aooId;
	
	private String descrizione;
	
	private int responsabileId;
	
	private int flagWeb;
	
	public RepertorioVO() {
	}

		
	public int getFlagWeb() {
		return flagWeb;
	}

	public void setFlagWeb(int flagWeb) {
		this.flagWeb = flagWeb;
	}

	public int getRepertorioId() {
		return repertorioId;
	}

	public void setRepertorioId(int repertorioId) {
		this.repertorioId = repertorioId;
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
			AssegnatarioView ass=RepertorioBO.newResponsabile(uff);
			return ass.getDescrizioneUfficio();
		}
		return "";
	}
}
