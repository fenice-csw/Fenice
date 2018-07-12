package it.compit.fenice.mvc.presentation.actionform.protocollo;

import it.compit.fenice.mvc.presentation.helper.DomandaView;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.vo.IdentityVO;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public final class ListaDomandeForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private String numeroDomanda;

	private String domanda;

	private String[] domandaChkBox;

	private Map<String[],DomandaView> domande = new HashMap<String[],DomandaView>();

	private String domandeIds;

	private String stato;

	private LookupDelegate getLookupDelegate() {
		return LookupDelegate.getInstance();
	}

	public Collection<IdentityVO> getStatiDomanda() {
		return getLookupDelegate().getStatiDomanda();
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public int getStatoId() {
		if (stato == null || stato.equals("") || stato.equals("Tutti"))
			return 0;
		else if (stato.equals("In Attesa"))
			return 1;
		else if (stato.equals("Eliminati"))
			return 2;
		else if (stato.equals("Protocollati"))
			return 3;
		return 0;

	}

	public Map<String[],DomandaView> getDomande() {
		return domande;
	}

	public Collection<DomandaView> getDomandeCollection() {
		if (domande != null) {
			return domande.values();
		} else
			return null;
	}

	/*
	public void addDomanda(DomandaView domanda) {
		if (domande != null) {
			domande.put(domanda.getDomandaId(), domanda);
		}
	}
	*/

	public void removeDomanda(Integer domandaId) {
		domande.remove(domandaId);
	}

	public String[] getDomandaChkBox() {
		return domandaChkBox;
	}

	public void setDomandaChkBoxChkBox(String[] domandaChkBox) {
		this.domandaChkBox = domandaChkBox;
	}

	public void removeDomanda(int domandaId) {
		removeDomanda(new Integer(domandaId));
	}

	public void removeDomande() {
		if (domande != null) {
			domande.clear();
		}
	}

	public void addDomanda(DomandaView d) {
		domande.put(d.getKey(), d);
	}
	
	public DomandaView getDomandaView(String domandaId) {
		return (DomandaView) domande.get(domandaId);
	}

	public void setDomande(Map<String[],DomandaView> domande) {
		this.domande = domande;
	}

	public String getNumeroDomanda() {
		return numeroDomanda;
	}

	public String getDomandeIds() {
		return domandeIds;
	}

	public void setNumeroDomanda(String numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	public void setDomandaChkBox(String[] domandaChkBox) {
		this.domandaChkBox = domandaChkBox;
	}

	public void setDomandeIds(String domandeIds) {
		this.domandeIds = domandeIds;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {

	}

	public String getDomanda() {
		return domanda;
	}

	public void setDomanda(String domanda) {
		this.domanda = domanda;
	}

}