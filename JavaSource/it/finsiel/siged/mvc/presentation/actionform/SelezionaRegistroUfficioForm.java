package it.finsiel.siged.mvc.presentation.actionform;

import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.UfficioBO;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/*
 * @author Almaviva sud.
 */

public final class SelezionaRegistroUfficioForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private Collection<CaricaVO> cariche = new ArrayList<CaricaVO>();

	private Collection<RegistroVO> registri = new ArrayList<RegistroVO>();

	private int caricaId;

	private int registroId;

	private int numPrt;

	private String buttonSubmit;

	private boolean multiCarica;

	private boolean numPrtZero;
	
	private boolean tabellaVisibile;
	
	private boolean utenteResponsabile;



	public boolean isTabellaVisibile() {
		return tabellaVisibile;
	}

	public void setTabellaVisibile(boolean tabellaVisibile) {
		this.tabellaVisibile = tabellaVisibile;
	}

	public boolean isUtenteResponsabile() {
		return utenteResponsabile;
	}

	public void setUtenteResponsabile(boolean utenteResponsabile) {
		this.utenteResponsabile = utenteResponsabile;
	}

	public boolean isMultiCarica() {
		return multiCarica;
	}

	public void setMultiCarica(boolean multiCarica) {
		this.multiCarica = multiCarica;
	}

	public boolean isNumPrtZero() {
		return numPrtZero;
	}

	public void setNumPrtZero(boolean numPrtZero) {
		this.numPrtZero = numPrtZero;
	}

	public int getNumPrt() {
		return numPrt;
	}

	public void setNumPrt(int numPrt) {
		this.numPrt = numPrt;
	}

	/**
	 * @return Returns the buttonSubmit.
	 */
	public String getButtonSubmit() {
		return buttonSubmit;
	}

	/**
	 * @param buttonSubmit
	 *            The buttonSubmit to set.
	 */
	public void setButtonSubmit(String buttonSubmit) {
		this.buttonSubmit = buttonSubmit;
	}

	/**
	 * @return Returns the registri.
	 */
	public Collection<RegistroVO> getRegistri() {
		return registri;
	}

	/**
	 * @param registri
	 *            The registri to set.
	 */
	public void setRegistri(Collection<RegistroVO> registri) {
		this.registri = registri;
	}

	/**
	 * @return Returns the registroId.
	 */
	public int getRegistroId() {
		return registroId;
	}

	/**
	 * @param registroId
	 *            The registroId to set.
	 */
	public void setRegistroId(int registroId) {
		this.registroId = registroId;
	}

	/**
	 * @return Returns the uffici.
	 */
	public Collection<CaricaVO> getCariche() {
		return cariche;
	}

	/**
	 * @param uffici
	 *            The uffici to set.
	 */
	public void setCariche(Collection<CaricaVO> cariche) {
		this.cariche = cariche;
	}

	/**
	 * @return Returns the ufficioId.
	 */
	public int getCaricaId() {
		return caricaId;
	}

	/**
	 * @param ufficioId
	 *            The ufficioId to set.
	 */
	public void setCaricaId(int ufficioId) {
		this.caricaId = ufficioId;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		caricaId = 0;
		registroId = 0;
		cariche = new ArrayList<CaricaVO>();
		registri = new ArrayList<RegistroVO>();
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		Utente utente = (Utente) request.getSession().getAttribute(
				Constants.UTENTE_KEY);
		if (isMultiCarica()) {
			if (caricaId == 0)
				errors.add("ufficioId", new ActionMessage(
						"error.ufficioId.required"));
			if (!UfficioBO.controllaPermessoUfficio(caricaId, utente))
				errors.add("ufficioId", new ActionMessage(
						"error.ufficioId.non_autorizzato"));
		}
		if (numPrt <= 0 && isNumPrtZero())
			errors.add("numPrt", new ActionMessage(
					"error.numPrt.maggiore_di_zero"));
		return errors;
	}

}