package it.compit.fenice.mvc.presentation.actionform.protocollo;

import it.finsiel.siged.mvc.presentation.helper.FascicoloView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public final class ArchivioDepositoForm extends ActionForm {


	private static final long serialVersionUID = 1L;

	private String[] fascicoloChkBox;

	private Map<Integer,FascicoloView> fascicoli = new HashMap<Integer,FascicoloView>();
	
	private Map<Integer,FascicoloView> fascicoliSelezionati = new HashMap<Integer,FascicoloView>();

	private String[] fascicoliIds;
	
	private String dataChiusuraDa;

    private String dataChiusuraA;
    
	public String getDataChiusuraDa() {
		return dataChiusuraDa;
	}

	public String getDataChiusuraA() {
		return dataChiusuraA;
	}

	public void setDataChiusuraDa(String dataChiusuraDa) {
		this.dataChiusuraDa = dataChiusuraDa;
	}

	public void setDataChiusuraA(String dataChiusuraA) {
		this.dataChiusuraA = dataChiusuraA;
	}

	public Collection<FascicoloView> getFascicoliSelezionatiCollection() {
		if (fascicoliSelezionati != null) {
			return fascicoliSelezionati.values();
		} else
			return null;
	}
	
	public Collection<FascicoloView> getFascicoliCollection() {
		if (fascicoli != null) {
			return fascicoli.values();
		} else
			return null;
	}

	public void addFascicolo(FascicoloView fascicolo) {
		if (fascicoli != null) {
			fascicoli.put(fascicolo.getId(), fascicolo);
			
		}
	}

	public void addFascicoloSelezionato(FascicoloView fascicolo) {
		if (fascicoliSelezionati != null) {
			fascicoliSelezionati.put(fascicolo.getId(), fascicolo);
			
		}
	}
	
	public void removeFascicolo(Integer fascicoloId) {
		fascicoli.remove(fascicoloId);
	}

	public void removeFascicoloSelezionato(Integer fascicoloId) {
		fascicoliSelezionati.remove(fascicoloId);
	}
	
	public String[] getFascicoloChkBox() {
		return fascicoloChkBox;
	}

	public void setFascicoloChkBox(String[] fascicoloChkBox) {
		this.fascicoloChkBox = fascicoloChkBox;
	}

	public void removeFascicolo(int fascicoloId) {
		removeFascicolo(new Integer(fascicoloId));
	}

	public void removeFascicoli() {
		if (fascicoli != null) {
			fascicoli.clear();
		}
	}

	public void removeFascicoliSelezionati() {
		if (fascicoliSelezionati != null) {
			fascicoliSelezionati.clear();
		}
	}
	
	public FascicoloView getFascicoloView(int fascicoloId) {
		return (FascicoloView) fascicoli.get(fascicoloId);
	}

	public void setFascicoli(Map<Integer,FascicoloView> fascicoli) {
		this.fascicoli = fascicoli;
	}

	public void setFascicoliSelezionati(Map<Integer,FascicoloView> fascicoli) {
		this.fascicoliSelezionati = fascicoli;
	}

	public String[] getFascicoliIds() {
		return fascicoliIds;
	}

	public void setFascicoliIds(String[] fascicoliIds) {
		this.fascicoliIds = fascicoliIds;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {

	}

}