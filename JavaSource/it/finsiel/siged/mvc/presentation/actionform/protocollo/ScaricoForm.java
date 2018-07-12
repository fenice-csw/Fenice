package it.finsiel.siged.mvc.presentation.actionform.protocollo;

import it.compit.fenice.mvc.presentation.helper.ReportCheckPostaInternaView;
import it.finsiel.siged.mvc.presentation.action.protocollo.ScaricoAction;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class ScaricoForm extends ActionForm {

	private String dataRegistrazioneDa;

	private String dataRegistrazioneA;

	private String numeroProtocolloDa;

	private String numeroProtocolloA;

	private String annoProtocolloDa;

	private String annoProtocolloA;

	private String statoProtocollo;
	
	private boolean ufficio;

	private String btnCerca;

	private String btnAtti;

	private String btnLavorazione;

	private String btnRisposta;

	private String btnRifiuta;

	private String btnViewProtocollo;

	private String protocolloScarico;

	private String[] protocolloScaricoChkBox;

	private String tipoUtenteUfficio;

	static Logger logger = Logger.getLogger(ScaricoAction.class.getName());

	private Map<Long,ReportProtocolloView>  protocolliScarico = new HashMap<Long,ReportProtocolloView> ();
	
	private Map checkPI = new HashMap();

	private String msgAssegnatarioCompetente;

	private String prtIds;
	
	private String tipoProtocollo;
	
	
	
	
	
	public boolean isUfficio() {
		return ufficio;
	}

	
	public void setUfficio(boolean ufficio) {
		this.ufficio = ufficio;
	}

	public String getTipoProtocollo() {
		return tipoProtocollo;
	}

	public void setTipoProtocollo(String tipoProtocollo) {
		this.tipoProtocollo = tipoProtocollo;
	}

	public String getPrtIds() {
		return prtIds;
	}

	public void setPrtIds(String prtIds) {
		this.prtIds = prtIds;
	}

	public Map<Long, ReportProtocolloView> getProtocolliInCarico() {
		return protocolliScarico;
	}

	public Collection<ReportProtocolloView> getProtocolliScaricoCollection() {
		if (protocolliScarico != null) {
			return protocolliScarico.values();
		} else
			return null;
	}

	/*
	public void addProtocolliScarico(ReportProtocolloView protocollo) {
		if (protocollo != null) {
			protocolliScarico.put(protocollo.getProtocolloId(), protocollo);
		}
	}
	*/
	
	
	public void setCheckPI(Map checkPI) {
		this.checkPI = checkPI;
	}


	public Collection getCheckPICollection() {
		if (checkPI != null) {
			return checkPI.values();
		} else
			return null;
	}

	public void addCheckPI(ReportCheckPostaInternaView protocollo) {
		if (protocollo != null) {
			checkPI.put(protocollo.getCheckId(), protocollo);
		}
	}
	
	public void removeCheckPI(Integer chkId) {
		checkPI.remove(chkId);
	}
	
	/*
	public void removeProtocolliScarico(Integer protocolloId) {
		protocolliScarico.remove(protocolloId);
	}
	*/

	public String[] getProtocolloScaricoChkBox() {
		return protocolloScaricoChkBox;
	}

	public void setProtocolloScaricoChkBox(String[] protocolloScaricoChkBox) {
		this.protocolloScaricoChkBox = protocolloScaricoChkBox;
	}

	/*
	public void removeProtocolliScarico(int protocolloId) {
		removeProtocolliScarico(new Integer(protocolloId));
	}

	public void removeProtocolliScarico(ProtocolloVO protocolloVO) {
		if (protocolloVO != null) {
			removeProtocolliScarico(protocolloVO.getId());
		}
	}
	*/
	
	/**
	 * @return Returns the btnViewProtocollo.
	 */
	public String getBtnViewProtocollo() {
		return btnViewProtocollo;
	}

	/**
	 * @param btnViewProtocollo
	 *            The btnViewProtocollo to set.
	 */
	public void setBtnViewProtocollo(String btnViewProtocollo) {
		this.btnViewProtocollo = btnViewProtocollo;
	}

	public void removeProtocolliScarico() {
		if (protocolliScarico != null) {
			protocolliScarico.clear();
		}
	}

	public ReportProtocolloView getProtocolloScaricoView(Integer protocolloId) {
		return (ReportProtocolloView) protocolliScarico.get(protocolloId);
	}

	/**
	 * @param protocolliInCarico
	 *            The protocolliInCarico to set.
	 */
	public void setProtocolliScarico(Map<Long, ReportProtocolloView> protocolliScarico) {
		this.protocolliScarico = protocolliScarico;
	}

	/**
	 * @return Returns the logger.
	 */
	public static Logger getLogger() {
		return logger;
	}

	/**
	 * @param logger
	 *            The logger to set.
	 */
	public static void setLogger(Logger logger) {
		ScaricoForm.logger = logger;
	}

	/**
	 * @return Returns the annoProtocolloA.
	 */
	public String getAnnoProtocolloA() {
		return annoProtocolloA;
	}

	/**
	 * @param annoProtocolloA
	 *            The annoProtocolloA to set.
	 */
	public void setAnnoProtocolloA(String annoProtocolloA) {
		this.annoProtocolloA = annoProtocolloA;
	}

	/**
	 * @return Returns the annoProtocolloDa.
	 */
	public String getAnnoProtocolloDa() {
		return annoProtocolloDa;
	}

	/**
	 * @param annoProtocolloDa
	 *            The annoProtocolloDa to set.
	 */
	public void setAnnoProtocolloDa(String annoProtocolloDa) {
		this.annoProtocolloDa = annoProtocolloDa;
	}

	/**
	 * @return Returns the btnCerca.
	 */
	public String getBtnCerca() {
		return btnCerca;
	}

	/**
	 * @param btnCerca
	 *            The btnCerca to set.
	 */
	public void setBtnCerca(String btnCerca) {
		this.btnCerca = btnCerca;
	}

	public String getBtnRifiuta() {
		return btnRifiuta;
	}

	public void setBtnRifiuta(String btnRifiuta) {
		this.btnRifiuta = btnRifiuta;
	}

	/**
	 * @return Returns the dataRegistrazioneA.
	 */
	public String getDataRegistrazioneA() {
		return dataRegistrazioneA;
	}

	/**
	 * @param dataRegistrazioneA
	 *            The dataRegistrazioneA to set.
	 */
	public void setDataRegistrazioneA(String dataRegistrazioneA) {
		this.dataRegistrazioneA = dataRegistrazioneA;
	}

	/**
	 * @return Returns the dataRegistrazioneDa.
	 */
	public String getDataRegistrazioneDa() {
		return dataRegistrazioneDa;
	}

	/**
	 * @param dataRegistrazioneDa
	 *            The dataRegistrazioneDa to set.
	 */
	public void setDataRegistrazioneDa(String dataRegistrazioneDa) {
		this.dataRegistrazioneDa = dataRegistrazioneDa;
	}

	/**
	 * @return Returns the numeroProtocolloA.
	 */
	public String getNumeroProtocolloA() {
		return numeroProtocolloA;
	}

	/**
	 * @param numeroProtocolloA
	 *            The numeroProtocolloA to set.
	 */
	public void setNumeroProtocolloA(String numeroProtocolloA) {
		this.numeroProtocolloA = numeroProtocolloA;
	}

	/**
	 * @return Returns the numeroProtocolloDa.
	 */
	public String getNumeroProtocolloDa() {
		return numeroProtocolloDa;
	}

	/**
	 * @param numeroProtocolloDa
	 *            The numeroProtocolloDa to set.
	 */
	public void setNumeroProtocolloDa(String numeroProtocolloDa) {
		this.numeroProtocolloDa = numeroProtocolloDa;
	}

	/**
	 * @return Returns the btnAccettaTutti.
	 */
	public String getBtnAtti() {
		return btnAtti;
	}

	/**
	 * @param btnAccettaTutti
	 *            The btnAccettaTutti to set.
	 */
	public void setBtnAtti(String btnAccettaTutti) {
		this.btnAtti = btnAccettaTutti;
	}

	/**
	 * @return Returns the statoProtocollo.
	 */
	public String getStatoProtocollo() {
		return statoProtocollo;
	}

	/**
	 * @param statoProtocollo
	 *            The statoProtocollo to set.
	 */
	public void setStatoProtocollo(String statoProtocollo) {
		this.statoProtocollo = statoProtocollo;
	}

	/**
	 * @return Returns the btnRespingiTutti.
	 */
	public String getBtnLavorazione() {
		return btnLavorazione;
	}

	/**
	 * @param btnRespingiTutti
	 *            The btnRespingiTutti to set.
	 */
	public void setBtnLavorazione(String btnRespingiTutti) {
		this.btnLavorazione = btnRespingiTutti;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {

	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		if (btnCerca != null) {
			protocolliScarico = null;
			if (dataRegistrazioneDa != null && !"".equals(dataRegistrazioneDa)
					&& !DateUtil.isData(dataRegistrazioneDa)) {
				errors.add("dataRegistrazioneDa", new ActionMessage(
						"formato.data.errato", "Data Da"));
			}
			if (dataRegistrazioneA != null && !"".equals(dataRegistrazioneA)
					&& !DateUtil.isData(dataRegistrazioneA)) {
				errors.add("dataRegistrazioneA", new ActionMessage(
						"formato.data.errato", "Data A"));
			}
			if (dataRegistrazioneDa != null && !"".equals(dataRegistrazioneDa)
					&& DateUtil.isData(dataRegistrazioneDa)
					&& dataRegistrazioneA != null
					&& !"".equals(dataRegistrazioneA)
					&& DateUtil.isData(dataRegistrazioneA)) {
				if (DateUtil.toDate(dataRegistrazioneA).before(
						DateUtil.toDate(dataRegistrazioneDa))) {
					errors.add("dataRegistrazioneDa", new ActionMessage(
							"date_incongruenti"));
				}
			}
			if (numeroProtocolloDa != null && !"".equals(numeroProtocolloDa)
					&& !(NumberUtil.isInteger(numeroProtocolloDa))) {
				errors.add("numeroProtocolloDa", new ActionMessage(
						"formato.numerico.errato", "Numero Da"));
			}
			if (numeroProtocolloA != null && !"".equals(numeroProtocolloA)
					&& !(NumberUtil.isInteger(numeroProtocolloA))) {
				errors.add("numeroProtocolloA", new ActionMessage(
						"formato.numerico.errato", "Numero A"));
			}
			if (annoProtocolloDa != null && !"".equals(annoProtocolloDa)
					&& !(NumberUtil.isInteger(annoProtocolloDa))) {
				errors.add("annoProtocolloDa", new ActionMessage(
						"formato.numerico.errato", "Anno Da"));
			}
			if (annoProtocolloA != null && !"".equals(annoProtocolloA)
					&& !(NumberUtil.isInteger(annoProtocolloA))) {
				errors.add("annoProtocolloA", new ActionMessage(
						"formato.numerico.errato", "Anno A"));
			}

		}

		else if ((btnAtti != null || btnLavorazione != null
				|| btnRisposta != null
				|| request.getParameter("btnRiassegna") != null || btnRifiuta != null)
				&& protocolloScaricoChkBox == null) {
			errors.add("protocolloScarico", new ActionMessage(
					"scarico_selezione_protocollo"));
		}

		if (!errors.isEmpty()) {
			setBtnCerca(null);
			setBtnAtti(null);
			setBtnLavorazione(null);
			setBtnAtti(null);
		}
		return errors;
	}

	/**
	 * @return Returns the protocolloScarico.
	 */
	public String getProtocolloScarico() {
		return protocolloScarico;
	}

	public void setProtocolloScarico(String protocolloInCarico) {
		this.protocolloScarico = protocolloInCarico;
	}

	/**
	 * @return Returns the btnRisposta.
	 */
	public String getBtnRisposta() {
		return btnRisposta;
	}

	/**
	 * @param btnRisposta
	 *            The btnRisposta to set.
	 */
	public void setBtnRisposta(String btnRisposta) {
		this.btnRisposta = btnRisposta;
	}

	/**
	 * @return Returns the tipoUtenteUfficio.
	 */
	public String getTipoUtenteUfficio() {
		return tipoUtenteUfficio;
	}

	/**
	 * @param tipoUtenteUfficio
	 *            The tipoUtenteUfficio to set.
	 */
	public void setTipoUtenteUfficio(String tipoUtenteUfficio) {
		this.tipoUtenteUfficio = tipoUtenteUfficio;
	}

	/**
	 * @return Returns the protocolliScarico.
	 */
	public Map getProtocolliScarico() {
		return protocolliScarico;
	}

	public String getMsgAssegnatarioCompetente() {
		return msgAssegnatarioCompetente;
	}

	public void setMsgAssegnatarioCompetente(String msgAssegnatarioCompetente) {
		this.msgAssegnatarioCompetente = msgAssegnatarioCompetente;
	}

}