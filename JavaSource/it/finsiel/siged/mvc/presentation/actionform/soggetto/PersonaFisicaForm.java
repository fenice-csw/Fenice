package it.finsiel.siged.mvc.presentation.actionform.soggetto;

import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.business.SoggettoDelegate;
import it.finsiel.siged.mvc.presentation.action.protocollo.ProtocolloIngressoAction;
import it.finsiel.siged.util.DateUtil;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class PersonaFisicaForm extends SoggettoForm {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(ProtocolloIngressoAction.class
			.getName());

	private ArrayList listaPersone = new ArrayList();

	private String cognome;

	private String nome;

	private String codiceFiscale;

	private String codMatricola;

	private String qualifica;

	private String dataNascita;

	private String sesso;

	private String comuneNascita;

	private String statoCivile;

	private String provinciaNascitaId;

	private String cerca;

	private String salvaAction;

	private String deleteAction;

	private Collection statiCivili;

	private boolean indietroVisibile;

	private boolean nuovoVisibile;

	public boolean isNuovoVisibile() {
		return nuovoVisibile;
	}

	public void setNuovoVisibile(boolean nuovoVisibile) {
		this.nuovoVisibile = nuovoVisibile;
	}

	public boolean isIndietroVisibile() {
		return indietroVisibile;
	}

	public void setIndietroVisibile(boolean indietroVisibile) {
		this.indietroVisibile = indietroVisibile;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		PersonaFisicaForm.logger = logger;
	}

	public String getCodMatricola() {
		return codMatricola;
	}

	public void setCodMatricola(String codMatricola) {
		this.codMatricola = codMatricola;
	}

	public String getComuneNascita() {
		return comuneNascita;
	}

	public void setComuneNascita(String comuneNascita) {
		this.comuneNascita = comuneNascita;
	}

	public String getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getProvinciaNascitaId() {
		return provinciaNascitaId;
	}

	public void setProvinciaNascitaId(String provinciaNascitaId) {
		this.provinciaNascitaId = provinciaNascitaId;
	}

	public String getQualifica() {
		return qualifica;
	}

	public void setQualifica(String qualifica) {
		this.qualifica = qualifica;
	}

	public String getSesso() {
		return sesso;
	}

	public void setSesso(String sesso) {
		this.sesso = sesso;
	}

	public String getStatoCivile() {
		return statoCivile;
	}

	public void setStatoCivile(String statoCivile) {
		this.statoCivile = statoCivile;
	}

	public Collection getProvince() {
		return LookupDelegate.getInstance().getProvince();
	}

	public Collection getListaPersone() {
		return listaPersone;
	}

	public Collection getStatiCivili() {
		return SoggettoDelegate.getInstance().getLstStatoCivile();
	}

	public void setListaPersone(ArrayList listaPersone) {
		this.listaPersone = listaPersone;
	}

	public String getListaSize() {
		if (listaPersone == null)
			return "0";
		else
			return "" + listaPersone.size();
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		if (cerca != null  && (getSoggetto().getIndirizzo().getComune() == null || "".equals(getSoggetto().getIndirizzo().getComune().trim())) && (cognome == null || "".equals(cognome.trim()))
				&& (nome == null || "".equals(nome.trim()))
				&& (codiceFiscale == null || "".equals(codiceFiscale.trim()))) {
			errors.add("cognome", new ActionMessage(
					"parametri_ricerca_persona_fisica"));
		}
		return errors;

	}

	public ActionErrors validateDatiInserimento(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		if (cognome == null || "".equals(cognome)) {
			errors.add("cognome", new ActionMessage("campo.obbligatorio",
					"Cognome", ""));
		} else if (nome == null || "".equals(nome)) {
			errors.add("nome", new ActionMessage("campo.obbligatorio", "Nome",
					""));
		} else if (dataNascita != null && !"".equals(dataNascita)
				&& !DateUtil.isData(dataNascita)) {
			errors.add("dataNascita", new ActionMessage("formato_data"));
		} else if (getSoggetto().getIndirizzo().getComune() == null
				|| "".equals(getSoggetto().getIndirizzo().getComune())) {
			errors.add("soggetto.indirizzo.comune", new ActionMessage(
					"campo.obbligatorio", "Località", ""));
		}else if (getSoggetto().getIndirizzoEMail()!=null && !getSoggetto().getIndirizzoEMail().equals("")) {
			if(SoggettoDelegate.getInstance().isMailUsed(getSoggetto().getAoo(), getSoggetto().getIndirizzoEMail()))
			errors.add("soggetto.indirizzo.email", new ActionMessage(
					"soggetto.email_presente"));
		}

		return errors;
	}

	public String getCerca() {
		return cerca;
	}

	public void setCerca(String cerca) {
		this.cerca = cerca;
	}

	public String getSalvaAction() {
		return salvaAction;
	}

	public void setSalvaAction(String confermaPFAction) {
		this.salvaAction = confermaPFAction;
	}

	public void setStatiCivili(Collection statiCivili) {
		this.statiCivili = statiCivili;
	}

	public String getDeleteAction() {
		return deleteAction;
	}

	public void setDeleteAction(String deletePFAction) {
		this.deleteAction = deletePFAction;
	}

}
