package it.finsiel.siged.mvc.presentation.actionform.amministrazione;

import it.finsiel.siged.mvc.presentation.action.amministrazione.TipoDocumentoAction;
import it.finsiel.siged.mvc.vo.IdentityVO;
import it.finsiel.siged.util.NumberUtil;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class TipoDocumentoForm extends ActionForm {

	static Logger logger = Logger
			.getLogger(TipoDocumentoAction.class.getName());

	// private Collection tipiDocumento;
	private int id;

	private ArrayList tipiDocumento = new ArrayList();

	private int aooId;

	private String descrizione;

	private String flagAttivazione;

	private int protocolli;

	private int numGGScadenza;

	private String flagDefault;

//	private String prezzo;

	public Collection getTipiDefault() {
		ArrayList list = new ArrayList(3);
		IdentityVO id;
		id = new IdentityVO();
		id.setCodice("0");
		id.setDescription("No");
		list.add(id);
		id = new IdentityVO();
		id.setCodice("1");
		id.setDescription("Si");
		list.add(id);
		return list;

	}

	

	public ArrayList getTipiDocumento() {
		return tipiDocumento;
	}

	public void setTipiDocumento(ArrayList tipiDocumento) {
		this.tipiDocumento = tipiDocumento;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		TipoDocumentoForm.logger = logger;
	}

	public int getAooId() {
		return aooId;
	}

	/**
	 * @param aooId
	 *            The aooId to set.
	 */
	public void setAooId(int aooId) {
		this.aooId = aooId;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getFlagAttivazione() {
		return flagAttivazione;
	}

	public void setFlagAttivazione(String flagAttivazione) {
		this.flagAttivazione = flagAttivazione;
	}

	public String getFlagDefault() {
		return flagDefault;
	}

	public void setFlagDefault(String flagDefault) {
		this.flagDefault = flagDefault;
	}

	public int getNumGGScadenza() {
		return numGGScadenza;
	}

	public void setNumGGScadenza(int numGGScadenza) {
		this.numGGScadenza = numGGScadenza;
	}

	public int getProtocolli() {
		return protocolli;
	}

	public void setProtocolli(int protocolli) {
		this.protocolli = protocolli;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {

	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		if (request.getParameter("btnConferma") != null) {
			if (descrizione == null || "".equals(descrizione.trim()))
				errors.add("descrizione", new ActionMessage("campo.obbligatorio", "Descrizione", ""));
		
		}

		return errors;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void inizializzaForm() {
		setId(0);
		setDescrizione(null);
		setFlagAttivazione(null);
		setProtocolli(0);
		setNumGGScadenza(0);
		setFlagDefault(null);
		//setPrezzo(null);

	}

}