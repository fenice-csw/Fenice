package it.compit.fenice.mvc.presentation.actionform.protocollo;

import it.compit.fenice.mvc.presentation.helper.ProtocolloProcedimentoView;
import it.finsiel.siged.mvc.presentation.helper.ProcedimentoView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionForm;

public class AlertProcedimentoForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private Map<String, ProcedimentoView> procedimentiULL = new HashMap<String, ProcedimentoView>(2);
	
	private Map<String, ProcedimentoView> procedimenti = new HashMap<String, ProcedimentoView>(2);

	private Map<String,ProtocolloProcedimentoView> protocolliEvidenza = new HashMap<String,ProtocolloProcedimentoView>(2);
	
	private boolean riassegnato = false;
	
	private boolean statoIstruttoria = false;
	
	private boolean statoRelatoria = false;
	
	private boolean statoParereConsiglio = false;
	
	private boolean statoDecreto = false;
	
	private boolean statoAttendiDecreto = false;

	private String dataAvvio;
	
	private String responsabile;
	
	private String oggettoProcedimento;
	
	private String numProcedimento;
	
	private int procedimentoId;
	
	private boolean titolareProcedimento;
	
	private boolean istruttore;
	
	private String oggetto;

	private String numeroProtocollo;
	
	public boolean isRiassegnato() {
		return riassegnato;
	}

	public void setRiassegnato(boolean riassegnato) {
		this.riassegnato = riassegnato;
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public String getNumeroProtocollo() {
		return numeroProtocollo;
	}

	public void setNumeroProtocollo(String numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}

	public boolean isIstruttore() {
		return istruttore;
	}

	public void setIstruttore(boolean istruttore) {
		this.istruttore = istruttore;
	}
	
	public int getProcedimentoId() {
		return procedimentoId;
	}

	public void setProcedimentoId(int procedimentoId) {
		this.procedimentoId = procedimentoId;
	}

	public String getDataAvvio() {
		return dataAvvio;
	}

	public void setDataAvvio(String dataAvvio) {
		this.dataAvvio = dataAvvio;
	}

	public String getResponsabile() {
		return responsabile;
	}

	public void setResponsabile(String responsabile) {
		this.responsabile = responsabile;
	}

	public String getOggettoProcedimento() {
		return oggettoProcedimento;
	}

	public void setOggettoProcedimento(String oggettoProcedimento) {
		this.oggettoProcedimento = oggettoProcedimento;
	}

	public String getNumProcedimento() {
		return numProcedimento;
	}

	public void setNumProcedimento(String numProcedimento) {
		this.numProcedimento = numProcedimento;
	}

	public Map<String,ProtocolloProcedimentoView> getProtocolliEvidenza() {
		return protocolliEvidenza;
	}

	public void setProtocolliEvidenza(Map<String,ProtocolloProcedimentoView> protocolliEvidenza) {
		this.protocolliEvidenza = protocolliEvidenza;
	}

	public Collection<ProtocolloProcedimentoView> getProtocolliEvidenzaCollection() {
		return protocolliEvidenza.values();
	}

	public void rimuoviProtocolloEvidenza(String id) {
		this.protocolliEvidenza.remove(id);
	}

	public void aggiungiProtocolloEvidenza(ProtocolloProcedimentoView p) {
		this.protocolliEvidenza.put(String.valueOf(p.getProtocolloId()), p);
	}

	public void reset() {
		procedimentiULL = new HashMap<String, ProcedimentoView>(2);
	}

	

	public boolean isStatoAttendiDecreto() {
		return statoAttendiDecreto;
	}

	public void setStatoAttendiDecreto(boolean statoAttendiDecreto) {
		this.statoAttendiDecreto = statoAttendiDecreto;
	}

	public boolean isStatoIstruttoria() {
		return statoIstruttoria;
	}

	public void setStatoIstruttoria(boolean statoIstruttoria) {
		this.statoIstruttoria = statoIstruttoria;
	}

	public boolean isStatoRelatoria() {
		return statoRelatoria;
	}

	public void setStatoRelatoria(boolean statoRelatoria) {
		this.statoRelatoria = statoRelatoria;
	}

	public boolean isStatoParereConsiglio() {
		return statoParereConsiglio;
	}

	public void setStatoParereConsiglio(boolean statoParereConsiglio) {
		this.statoParereConsiglio = statoParereConsiglio;
	}

	public boolean isStatoDecreto() {
		return statoDecreto;
	}

	public void setStatoDecreto(boolean statoDecreto) {
		this.statoDecreto = statoDecreto;
	}

	public Map<String, ProcedimentoView> getProcedimentiULL() {
		return procedimentiULL;
	}

	public Collection<ProcedimentoView> getProcedimentiULLCollection() {
		List<ProcedimentoView> proList=new ArrayList<ProcedimentoView>(procedimentiULL.values());
		Collections.sort(proList);
		return proList;
	}

	public void setProcedimentiULL(Map<String, ProcedimentoView> procedimenti) {
		this.procedimentiULL = procedimenti;
		for (ProcedimentoView p : procedimenti.values()) {
			if(p.isRiassegnato())
				setRiassegnato(true);
			else if (p.getStatoId() == 3)
				setStatoIstruttoria(true);
			else if (p.getStatoId() == 4)
				setStatoRelatoria(true);
			else if (p.getStatoId() == 5)
				setStatoParereConsiglio(true);
			else if (p.getStatoId() == 6)
				setStatoDecreto(true);
			else if (p.getStatoId() == 7)
				setStatoAttendiDecreto(true);
		}
	}

	public void rimuoviProcedimentoULL(String id) {
		this.procedimentiULL.remove(id);
	}

	
	public Map<String, ProcedimentoView> getProcedimenti() {
		return procedimenti;
	}

	public Collection<ProcedimentoView> getProcedimentiCollection() {
		List<ProcedimentoView> proList=new ArrayList<ProcedimentoView>(procedimenti.values());
		Collections.sort(proList);
		return proList;
	}

	public void setProcedimenti(Map<String, ProcedimentoView> procedimenti) {
		this.procedimenti = procedimenti;
	}

	public void rimuoviProcedimento(String id) {
		this.procedimenti.remove(id);
	}
	
	public void aggiungiProcedimento(ProcedimentoView p) {
		this.procedimenti.put(p.getNumeroProcedimento(), p);
	}
	
	public void aggiungiProcedimentoULL(ProcedimentoView p) {
		this.procedimentiULL.put(p.getNumeroProcedimento(), p);
	}
	
	public boolean isTitolareProcedimento() {
		return titolareProcedimento;
	}

	public void setTitolareProcedimento(boolean titolareProcedimento) {
		this.titolareProcedimento = titolareProcedimento;
	}

}
