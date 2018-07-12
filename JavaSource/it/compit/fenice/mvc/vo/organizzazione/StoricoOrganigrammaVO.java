package it.compit.fenice.mvc.vo.organizzazione;

import it.finsiel.siged.mvc.presentation.helper.StatisticheView;
import it.finsiel.siged.mvc.vo.VersioneVO;

import java.util.ArrayList;
import java.util.Collection;

public class StoricoOrganigrammaVO extends VersioneVO {
	
	private static final long serialVersionUID = 1L;

	private Integer aooId;

	private String descrizione;
	
	Collection<StatisticheView> organigramma = new ArrayList<StatisticheView>();
	
	private String dataStorico;


	public String getDataStorico() {
		return dataStorico;
	}

	public void setDataStorico(String dataStorico) {
		this.dataStorico = dataStorico;
	}

	public Integer getAooId() {
		return aooId;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public Collection<StatisticheView> getOrganigramma() {
		return organigramma;
	}

	public void setAooId(Integer aooId) {
		this.aooId = aooId;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public void setOrganigramma(Collection<StatisticheView> organigramma) {
		this.organigramma = organigramma;
	}
}
