package it.finsiel.siged.mvc.presentation.actionform.protocollo;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.presentation.action.protocollo.PresaInCaricoAction;
import it.finsiel.siged.mvc.presentation.actionform.ParametriForm;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.vo.lookup.TipoDocumentoVO;
import it.finsiel.siged.mvc.vo.lookup.TipoProtocolloVO;
import it.finsiel.siged.mvc.vo.lookup.TitolarioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;

import java.util.Collection;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class RicercaForm extends ParametriForm implements
		AlberoUfficiUtentiForm {
	
	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(PresaInCaricoAction.class.getName());

	private int aooId;

	private int tipoDocumentoId;

	private String dataRegistrazioneDa;

	private String dataRegistrazioneA;

	private String numeroProtocolloDa;

	private String numeroProtocolloA;

	private String annoProtocolloDa;

	private String annoProtocolloA;

	private String protocolloSelezionato;

	private String documentoSelezionato;

	private int documentoId;

	private String mittente;

	private String destinatario;

	private String tipoProtocollo;

	private Collection<TipoProtocolloVO> statiProtocollo;

	private String statoProtocollo;

	private String riservato;

	private String dataDocumentoDa;

	private String dataDocumentoA;

	private String dataRicevutoDa;

	private String dataRicevutoA;

	private String oggetto;

	private String chiaveAnnotazione;

	private String posizioneAnnotazione;

	private String descrizioneAnnotazione;

	private String idArgomento;

	private String pathArgomento;

	private String descrizioneArgomento;

	private String btnCerca;

	private String btnAnnulla;

	private String btnSeleziona;

	private String btnCercaMittente;

	private String btnCercaDestinatario;

	private String btnCercaArgomento;

	private String btnModificaProtocollo;

	private String btnAnnullaProtocollo;

	private String btnConfermaAnnullamento;

	private Collection mittenti;

	private Collection destinatari;

	private Collection argomenti;
	
	private Collection titolariFigli;

	private TitolarioVO titolario;

	private int titolarioPrecedenteId;

	private int titolarioSelezionatoId;

	private int utenteProtocollatoreId;

	private int ufficioProtocollatoreId;

	private SortedMap protocolli = new TreeMap();

	private String reportSubTitle;

	private String[] assegnatariSelezionatiId;

	private String assegnatarioCompetente;

	private int ufficioCorrenteId;

	private String ufficioCorrentePath;

	private int ufficioSelezionatoId;

	private int ufficioRicercaId;

	private int utenteSelezionatoId;

	private int ufficioRicercaPIDestId;

	private int utenteSelezionatoPIDestId;

	private int ufficioCorrentePIDestId;

	private Collection ufficiDipendentiPIDest;

	private int ufficioSelezionatoPIDestId;

	private Collection utentiPIDest;

	private UfficioVO ufficioCorrentePIDest;

	private String ufficioCorrentePathPIDest;

	private String numeroProtocolloMittente;

	private boolean flagPEC;

	private String tipoUfficioRicerca;

	public String getTipoProtocolloReport() {
		if (this.tipoProtocollo.equals("I"))
			return "Ingresso";
		else if (this.tipoProtocollo.equals("U"))
			return "Uscita";
		else
			return "Posta Interna";
	}

	public String getTipoUfficioRicerca() {
		return tipoUfficioRicerca;
	}

	public void setTipoUfficioRicerca(String tipoUfficioRicerca) {
		this.tipoUfficioRicerca = tipoUfficioRicerca;
	}

	public String getReportSubTitle() {
		return reportSubTitle;
	}

	public void setReportSubTitle(String reportSubTitle) {
		this.reportSubTitle = reportSubTitle;
	}

	public boolean getFlagPEC() {
		return flagPEC;
	}

	public void setFlagPEC(boolean flagPEC) {
		this.flagPEC = flagPEC;
	}

	public String getNumeroProtocolloMittente() {
		return numeroProtocolloMittente;
	}

	public void setNumeroProtocolloMittente(String numeroProtocolloMittente) {
		this.numeroProtocolloMittente = numeroProtocolloMittente;
	}

	public String getUfficioCorrentePathPIDest() {
		return ufficioCorrentePathPIDest;
	}

	public void setUfficioCorrentePathPIDest(String ufficioCorrentePathPIDest) {
		this.ufficioCorrentePathPIDest = ufficioCorrentePathPIDest;
	}

	private UfficioVO ufficioCorrente;

	private Collection ufficiDipendenti;

	private Collection utenti;

	private Utente utenteCorrente;

	private String[] protocolliSelezionati;

	private boolean isCercaDaFascicolo = false;

	private boolean indietroVisibile;

	// POSTA INTERNA

	//

	public boolean isCercaDaFascicolo() {
		return isCercaDaFascicolo;
	}

	public int getUfficioRicercaPIDestId() {
		return ufficioRicercaPIDestId;
	}

	public void setUfficioRicercaPIDestId(int ufficioRicercaPIDestId) {
		this.ufficioRicercaPIDestId = ufficioRicercaPIDestId;
	}

	public int getUtenteSelezionatoPIDestId() {
		return utenteSelezionatoPIDestId;
	}

	public void setUtenteSelezionatoPIDestId(int utenteSelezionatoPIDestId) {
		this.utenteSelezionatoPIDestId = utenteSelezionatoPIDestId;
	}

	public int getUfficioCorrentePIDestId() {
		return ufficioCorrentePIDestId;
	}

	public void setUfficioCorrentePIDestId(int ufficioCorrentePIDestId) {
		this.ufficioCorrentePIDestId = ufficioCorrentePIDestId;
	}

	public Collection getUfficiDipendentiPIDest() {
		return ufficiDipendentiPIDest;
	}

	public void setUfficiDipendentiPIDest(Collection ufficiDipendentiPIDest) {
		this.ufficiDipendentiPIDest = ufficiDipendentiPIDest;
	}

	public int getUfficioSelezionatoPIDestId() {
		return ufficioSelezionatoPIDestId;
	}

	public void setUfficioSelezionatoPIDestId(int ufficioSelezionatoPIDestId) {
		this.ufficioSelezionatoPIDestId = ufficioSelezionatoPIDestId;
	}

	public Collection getUtentiPIDest() {
		return utentiPIDest;
	}

	public void setUtentiPIDest(Collection utentiPIDest) {
		this.utentiPIDest = utentiPIDest;
	}

	public UfficioVO getUfficioCorrentePIDest() {
		return ufficioCorrentePIDest;
	}

	public void setUfficioCorrentePIDest(UfficioVO ufficioCorrentePIDest) {
		this.ufficioCorrentePIDest = ufficioCorrentePIDest;
	}

	public void setCercaDaFascicolo(boolean isCercaDaFascicolo) {
		this.isCercaDaFascicolo = isCercaDaFascicolo;
	}

	public Collection getUfficiDipendenti() {
		return ufficiDipendenti;
	}

	public void setUfficiDipendenti(Collection ufficiDipendenti) {
		this.ufficiDipendenti = ufficiDipendenti;
	}

	public Collection getUtenti() {
		return utenti;
	}

	public UtenteVO getUtente(int utenteId) {
		for (Iterator i = getUtenti().iterator(); i.hasNext();) {
			UtenteVO ute = (UtenteVO) i.next();
			if (ute.getId().intValue() == utenteId) {
				return ute;
			}
		}
		return null;
	}

	public int getUfficioRicercaId() {
		return ufficioRicercaId;
	}

	public void setUfficioRicercaId(int ufficioRicercaId) {
		this.ufficioRicercaId = ufficioRicercaId;
	}

	public void setUtenti(Collection utenti) {
		this.utenti = utenti;
	}

	public int getUfficioCorrenteId() {
		return ufficioCorrenteId;
	}

	public void setUfficioCorrenteId(int ufficioCorrenteId) {
		this.ufficioCorrenteId = ufficioCorrenteId;
	}

	public int getUfficioSelezionatoId() {
		return ufficioSelezionatoId;
	}

	public void setUfficioSelezionatoId(int ufficioCorrenteId) {
		this.ufficioSelezionatoId = ufficioCorrenteId;
	}

	public UfficioVO getUfficioCorrente() {
		return ufficioCorrente;
	}

	public void setUfficioCorrente(UfficioVO ufficioCorrente) {
		this.ufficioCorrente = ufficioCorrente;
	}

	public String getUfficioCorrentePath() {
		return ufficioCorrentePath;
	}

	public void setUfficioCorrentePath(String ufficioCorrentePath) {
		this.ufficioCorrentePath = ufficioCorrentePath;
	}

	public String[] getAssegnatariSelezionatiId() {
		return assegnatariSelezionatiId;
	}

	public void setAssegnatariSelezionatiId(String[] assegnatari) {
		this.assegnatariSelezionatiId = assegnatari;
	}

	public String getAssegnatarioCompetente() {
		return assegnatarioCompetente;
	}

	public void setAssegnatarioCompetente(String assegnatarioCompetente) {
		this.assegnatarioCompetente = assegnatarioCompetente;
	}

	public SortedMap getProtocolli() {
		return protocolli;
	}

	public Collection getProtocolliCollection() {
		if (protocolli != null) {
			return protocolli.values();
		} else
			return null;
	}

	public String getNumeroProtocolli() {
		if (protocolli != null) {
			return protocolli.values().size() + "";
		} else
			return "0";

	}

	public void addProtocolliInCarico(ProtocolloVO protocolloVO) {
		if (protocolloVO != null) {
			protocolli.put(protocolloVO.getId(), protocolloVO);
		}
	}

	public void removeProtocolli(Integer protocolloId) {
		protocolli.remove(protocolloId);
	}

	public void removeProtocolli(int protocolloId) {
		removeProtocolli(new Integer(protocolloId));
	}

	public void removeProtocolli(ProtocolloVO protocolloVO) {
		if (protocolloVO != null) {
			removeProtocolli(protocolloVO.getId());
		}
	}

	public void removeProtocolli() {
		if (protocolli != null) {
			protocolli.clear();
		}
	}

	public ReportProtocolloView getProtocolloView(Integer protocolloId) {
		return (ReportProtocolloView) protocolli.get(protocolloId);
	}

	/**
	 * @param protocolliInCarico
	 *            The protocolliInCarico to set.
	 */
	public void setProtocolli(SortedMap protocolli) {
		this.protocolli = protocolli;
	}

	/**
	 * @return Returns the tipoProtocollo.
	 */
	public String getTipoProtocollo() {
		/*
		if (tipoProtocollo == null)
			if (tipoUfficioRicerca.equals("C"))
				return null;
			else
				return "I";
		else
		*/
		return tipoProtocollo;
	}

	/**
	 * @param tipoProtocollo
	 *            The tipoProtocollo to set.
	 */
	public void setTipoProtocollo(String tipoProtocollo) {
		this.tipoProtocollo = tipoProtocollo;
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
		RicercaForm.logger = logger;
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

	/**
	 * @return Returns the btnCercaArgomento.
	 */
	public String getBtnCercaArgomento() {
		return btnCercaArgomento;
	}

	/**
	 * @param btnCercaArgomento
	 *            The btnCercaArgomento to set.
	 */
	public void setBtnCercaArgomento(String btnCercaArgomento) {
		this.btnCercaArgomento = btnCercaArgomento;
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
	 * @return Returns the protocolloInCarico.
	 */
	public String getProtocolloSelezionato() {
		return protocolloSelezionato;
	}

	/**
	 * @param protocolloInCarico
	 *            The protocolloInCarico to set.
	 */
	public void setProtocolloSelezionato(String protocolloInCarico) {
		this.protocolloSelezionato = protocolloInCarico;
	}

	/**
	 * @return Returns the btnAnnulla.
	 */
	public String getBtnAnnulla() {
		return btnAnnulla;
	}

	/**
	 * @param btnAnnulla
	 *            The btnAnnulla to set.
	 */
	public void setBtnAnnulla(String btnAnnulla) {
		this.btnAnnulla = btnAnnulla;
	}

	/**
	 * @return Returns the btnSeleziona.
	 */
	public String getBtnSeleziona() {
		return btnSeleziona;
	}

	/**
	 * @param btnSeleziona
	 *            The btnSeleziona to set.
	 */
	public void setBtnSeleziona(String btnSeleziona) {
		this.btnSeleziona = btnSeleziona;
	}

	/**
	 * @return Returns the btnCercaDestinatario.
	 */
	public String getBtnCercaDestinatario() {
		return btnCercaDestinatario;
	}

	/**
	 * @param btnCercaDestinatario
	 *            The btnCercaDestinatario to set.
	 */
	public void setBtnCercaDestinatario(String btnCercaDestinatario) {
		this.btnCercaDestinatario = btnCercaDestinatario;
	}

	/**
	 * @return Returns the btnCercaMittente.
	 */
	public String getBtnCercaMittente() {
		return btnCercaMittente;
	}

	/**
	 * @param btnCercaMittente
	 *            The btnCercaMittente to set.
	 */
	public void setBtnCercaMittente(String btnCercaMittente) {
		this.btnCercaMittente = btnCercaMittente;
	}

	/**
	 * @return Returns the btnAnnullaProtocollo.
	 */
	public String getBtnAnnullaProtocollo() {
		return btnAnnullaProtocollo;
	}

	/**
	 * @param btnAnnullaProtocollo
	 *            The btnAnnullaProtocollo to set.
	 */
	public void setBtnAnnullaProtocollo(String btnAnnullaProtocollo) {
		this.btnAnnullaProtocollo = btnAnnullaProtocollo;
	}

	/**
	 * @return Returns the btnModificaProtocollo.
	 */
	public String getBtnModificaProtocollo() {
		return btnModificaProtocollo;
	}

	/**
	 * @param btnModificaProtocollo
	 *            The btnModificaProtocollo to set.
	 */
	public void setBtnModificaProtocollo(String btnModificaProtocollo) {
		this.btnModificaProtocollo = btnModificaProtocollo;
	}

	/**
	 * @return Returns the btnConfermaAnnullamento.
	 */
	public String getBtnConfermaAnnullamento() {
		return btnConfermaAnnullamento;
	}

	/**
	 * @param btnConfermaAnnullamento
	 *            The btnConfermaAnnullamento to set.
	 */
	public void setBtnConfermaAnnullamento(String btnConfermaAnnullamento) {
		this.btnConfermaAnnullamento = btnConfermaAnnullamento;
	}

	/**
	 * @return Returns the chiaveAnnotazione.
	 */
	public String getChiaveAnnotazione() {
		return chiaveAnnotazione;
	}

	/**
	 * @param chiaveAnnotazione
	 *            The chiaveAnnotazione to set.
	 */
	public void setChiaveAnnotazione(String chiaveAnnotazione) {
		this.chiaveAnnotazione = chiaveAnnotazione;
	}

	/**
	 * @return Returns the codiceArgomento.
	 */
	public String getPathArgomento() {
		return pathArgomento;
	}

	/**
	 * @param pathArgomento
	 *            The pathArgomento to set.
	 */
	public void setPathArgomento(String pathArgomento) {
		this.pathArgomento = pathArgomento;
	}

	/**
	 * @return Returns the idArgomento.
	 */
	public String getIdArgomento() {
		return idArgomento;
	}

	/**
	 * @param idArgomento
	 *            The idArgomento to set.
	 */
	public void setIdArgomento(String idArgomento) {
		this.idArgomento = idArgomento;
	}

	/**
	 * @return Returns the dataDocumentoA.
	 */
	public String getDataDocumentoA() {
		return dataDocumentoA;
	}

	/**
	 * @param dataDocumentoA
	 *            The dataDocumentoA to set.
	 */
	public void setDataDocumentoA(String dataDocumentoA) {
		this.dataDocumentoA = dataDocumentoA;
	}

	/**
	 * @return Returns the dataDocumentoDA.
	 */
	public String getDataDocumentoDa() {
		return dataDocumentoDa;
	}

	/**
	 * @param dataDocumentoDA
	 *            The dataDocumentoDA to set.
	 */
	public void setDataDocumentoDa(String dataDocumentoDa) {
		this.dataDocumentoDa = dataDocumentoDa;
	}

	/**
	 * @return Returns the dataRicevuto.
	 */
	public String getDataRicevutoDa() {
		return dataRicevutoDa;
	}

	/**
	 * @param dataRicevuto
	 *            The dataRicevuto to set.
	 */
	public void setDataRicevutoDa(String dataRicevutoDa) {
		this.dataRicevutoDa = dataRicevutoDa;
	}

	/**
	 * @return Returns the dataRicevuto.
	 */
	public String getDataRicevutoA() {
		return dataRicevutoA;
	}

	/**
	 * @param dataRicevuto
	 *            The dataRicevuto to set.
	 */
	public void setDataRicevutoA(String dataRicevutoA) {
		this.dataRicevutoA = dataRicevutoA;
	}

	/**
	 * @return Returns the descrizioneAnnotazione.
	 */
	public String getDescrizioneAnnotazione() {
		return descrizioneAnnotazione;
	}

	/**
	 * @param descrizioneAnnotazione
	 *            The descrizioneAnnotazione to set.
	 */
	public void setDescrizioneAnnotazione(String descrizioneAnnotazione) {
		this.descrizioneAnnotazione = descrizioneAnnotazione;
	}

	/**
	 * @return Returns the descrizioneArgomento.
	 */
	public String getDescrizioneArgomento() {
		return descrizioneArgomento;
	}

	/**
	 * @param descrizioneArgomento
	 *            The descrizioneArgomento to set.
	 */
	public void setDescrizioneArgomento(String descrizioneArgomento) {
		this.descrizioneArgomento = descrizioneArgomento;
	}

	/**
	 * @return Returns the destinatario.
	 */
	public String getDestinatario() {

		return destinatario;
	}

	/**
	 * @param destinatario
	 *            The destinatario to set.
	 */
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	/**
	 * @return Returns the flagRiservato.
	 */
	public String getRiservato() {
		return riservato;
	}

	/**
	 * @param flagRiservato
	 *            The flagRiservato to set.
	 */
	public void setRiservato(String flagRiservato) {
		this.riservato = flagRiservato;
	}

	/**
	 * @return Returns the mittente.
	 */
	public String getMittente() {
		return mittente;
	}

	/**
	 * @param mittente
	 *            The mittente to set.
	 */
	public void setMittente(String mittente) {
		this.mittente = mittente;
	}

	/**
	 * @return Returns the oggetto.
	 */
	public String getOggetto() {
		return oggetto;
	}

	/**
	 * @param oggetto
	 *            The oggetto to set.
	 */
	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	/**
	 * @return Returns the posizioneAnnotazione.
	 */
	public String getPosizioneAnnotazione() {
		return posizioneAnnotazione;
	}

	/**
	 * @param posizioneAnnotazione
	 *            The posizioneAnnotazione to set.
	 */
	public void setPosizioneAnnotazione(String posizioneAnnotazione) {
		this.posizioneAnnotazione = posizioneAnnotazione;
	}

	/*
	 * public String getTipoDocumento() { return tipoDocumento; }
	 * 
	 * public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento =
	 * tipoDocumento; }
	 */

	public Collection getArgomenti() {
		return argomenti;
	}

	/**
	 * @param argomenti
	 *            The argomenti to set.
	 */
	public void setArgomenti(Collection argomenti) {
		this.argomenti = argomenti;
	}

	/**
	 * @return Returns the destinatari.
	 */
	public Collection getDestinatari() {
		return destinatari;
	}

	/**
	 * @param destinatari
	 *            The destinatari to set.
	 */
	public void setDestinatari(Collection destinatari) {
		this.destinatari = destinatari;
	}

	/**
	 * @return Returns the mittenti.
	 */
	public Collection getMittenti() {
		return mittenti;
	}

	/**
	 * @param mittenti
	 *            The mittenti to set.
	 */
	public void setMittenti(Collection mittenti) {
		this.mittenti = mittenti;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.utenteCorrente = (Utente) request.getSession().getAttribute(
				Constants.UTENTE_KEY);
	}

	public ActionErrors validateParametriModificaProtocollo(
			ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		if (btnAnnullaProtocollo != null && protocolloSelezionato == null) {
			errors.add("protocolloSelezionato", new ActionMessage(
					"selezione_protocollo_cancellazione"));
		} else if (btnModificaProtocollo != null
				&& protocolloSelezionato == null) {
			errors.add("protocolloSelezionato", new ActionMessage(
					"selezione_protocollo_modifica"));
		} else {
			String statoProt = getProtocolloView(
					new Integer(request.getParameter("protocolloSelezionato")))
					.getStatoProtocollo();
			if (!"Lavorazione".equalsIgnoreCase(statoProt)) {
				errors.add("protocolloSelezionato", new ActionMessage(
						"protocollo_non_modificabile"));
			}

		}
		if (!errors.isEmpty()) {
			setBtnAnnullaProtocollo(null);
			setBtnModificaProtocollo(null);
		}
		return errors;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {

		ActionErrors errors = new ActionErrors();
		if (btnCerca != null) {
			if (numeroProtocolloDa != null && !"".equals(numeroProtocolloDa)
					&& !(NumberUtil.isInteger(numeroProtocolloDa))) {
				errors.add("numeroProtocolloDa", new ActionMessage(
						"formato.numerico.errato", "Numero protocollo da"));
			} else if (numeroProtocolloA != null
					&& !"".equals(numeroProtocolloA)
					&& !(NumberUtil.isInteger(numeroProtocolloA))) {
				errors.add("numeroProtocolloA", new ActionMessage(
						"formato.numerico.errato", "Numero protocollo a"));
			} else if (annoProtocolloDa != null && !"".equals(annoProtocolloDa)
					&& !(NumberUtil.isInteger(annoProtocolloDa))) {
				errors.add("annoProtocolloDa", new ActionMessage(
						"formato.numerico.errato", "Anno protocollo da"));
			} else if (annoProtocolloA != null && !"".equals(annoProtocolloA)
					&& !(NumberUtil.isInteger(annoProtocolloA))) {
				errors.add("annoProtocolloA", new ActionMessage(
						"formato.numerico.errato", "Anno protocollo a"));
			} else if ((numeroProtocolloDa != null && !""
					.equals(numeroProtocolloDa))
					&& (annoProtocolloDa == null || "".equals(annoProtocolloDa))) {
				errors.add("annoProtocolloDa", new ActionMessage(
						"selezione.obbligatoria", "Anno Da",
						"in presenza del numero Da"));
			} else if ((numeroProtocolloA != null && !""
					.equals(numeroProtocolloA))
					&& (annoProtocolloA == null || "".equals(annoProtocolloA))) {
				errors.add("annoProtocolloA", new ActionMessage(
						"selezione.obbligatoria", "Anno A",
						"in presenza del numero A"));
			} else if ((annoProtocolloA != null && !"".equals(annoProtocolloA))
					&& (annoProtocolloDa != null && !""
							.equals(annoProtocolloDa))
					&& NumberUtil.getInt(annoProtocolloDa) > NumberUtil
							.getInt(annoProtocolloA)) {
				errors.add("annoProtocolloA", new ActionMessage(
						"anni_protocollo_incongruenti"));
			} else if (dataRegistrazioneDa != null
					&& !"".equals(dataRegistrazioneDa)
					&& !DateUtil.isData(dataRegistrazioneDa)) {
				errors.add("dataRegistrazioneDa", new ActionMessage(
						"formato.data.errato", "data Registrazione Da"));
			} else if (dataRegistrazioneA != null
					&& !"".equals(dataRegistrazioneA)
					&& !DateUtil.isData(dataRegistrazioneA)) {
				errors.add("dataRegistrazioneDa", new ActionMessage(
						"formato.data.errato", "data Registrazione A"));
			} else if (dataRegistrazioneDa != null
					&& !"".equals(dataRegistrazioneDa)
					&& DateUtil.isData(dataRegistrazioneDa)
					&& dataRegistrazioneA != null
					&& !"".equals(dataRegistrazioneA)
					&& DateUtil.isData(dataRegistrazioneA)) {
				if (DateUtil.toDate(dataRegistrazioneA).before(
						DateUtil.toDate(dataRegistrazioneDa))) {
					errors.add("dataRegistrazioneDa", new ActionMessage(
							"date_incongruenti"));
				}
			} else if (dataRicevutoDa != null && !"".equals(dataRicevutoDa)
					&& !DateUtil.isData(dataRicevutoDa)) {
				errors.add("dataRicevutoDa", new ActionMessage(
						"formato.data.errato", "Data ricevuto da"));
			} else if (dataRicevutoA != null && !"".equals(dataRicevutoA)
					&& !DateUtil.isData(dataRicevutoA)) {
				errors.add("dataDocumentoA", new ActionMessage(
						"formato.data.errato", "Data ricevuto A"));
			} else if (dataRicevutoA != null && !"".equals(dataRicevutoDa)
					&& DateUtil.isData(dataRicevutoA) && dataRicevutoA != null
					&& !"".equals(dataRicevutoA)
					&& DateUtil.isData(dataRicevutoA)) {
				if (DateUtil.toDate(dataRicevutoA).before(
						DateUtil.toDate(dataRicevutoDa))) {
					errors.add("dataDocumentoDa", new ActionMessage(
							"date_incongruenti"));
				}
			} else if (dataDocumentoDa != null && !"".equals(dataDocumentoDa)
					&& !DateUtil.isData(dataDocumentoDa)) {
				errors.add("dataDocumentoDa", new ActionMessage(
						"formato.data.errato", "Data documento da"));
			} else if (dataDocumentoA != null && !"".equals(dataDocumentoA)
					&& !DateUtil.isData(dataDocumentoA)) {
				errors.add("dataDocumentoA", new ActionMessage(
						"formato.data.errato", "Data documento A"));
			} else if (dataDocumentoA != null && !"".equals(dataDocumentoDa)
					&& DateUtil.isData(dataDocumentoA)
					&& dataDocumentoA != null && !"".equals(dataDocumentoA)
					&& DateUtil.isData(dataDocumentoA)) {
				if (DateUtil.toDate(dataDocumentoA).before(
						DateUtil.toDate(dataDocumentoDa))) {
					errors.add("dataDocumentoDa", new ActionMessage(
							"date_incongruenti"));
				}
			} else if (progressivoFascicolo != null
					&& !"".equals(progressivoFascicolo)
					&& !(NumberUtil.isInteger(progressivoFascicolo))) {
				errors.add("progressivoFascicolo", new ActionMessage(
						"formato.numerico.errato", "Progressivo fascicolo"));
			} else if (tipoProtocollo == null) {

			}
		}
		if (!errors.isEmpty()) {
			setBtnCerca(null);
		}

		return errors;
	}

	public int getUtenteSelezionatoId() {
		return utenteSelezionatoId;
	}

	public void setUtenteSelezionatoId(int utenteSelezionatoId) {
		this.utenteSelezionatoId = utenteSelezionatoId;
	}

	public void inizializzaForm() {
		setAnnoProtocolloA(null);
		setAnnoProtocolloDa(null);
		setArgomenti(null);
		setChiaveAnnotazione(null);
		setPathArgomento(null);
		setDataDocumentoA(null);
		setDataDocumentoDa(null);
		setDataRegistrazioneA(null);
		setDataRegistrazioneDa(null);
		setDataRicevutoDa(null);
		setDataRicevutoA(null);
		setDescrizioneAnnotazione(null);
		setDescrizioneArgomento(null);
		setDestinatario(null);
		setRiservato(null);
		setMittente(null);
		setNumeroProtocolloA(null);
		setNumeroProtocolloDa(null);
		setOggetto(null);
		setPosizioneAnnotazione(null);
		setStatoProtocollo("");
		setTitolario(null);
		setProgressivoFascicolo(null);
		setTitolarioSelezionatoId(0);
		setTitolariFigli(null);
		setUfficioCorrenteId(0);
		setUfficioCorrentePIDestId(0);
		setUfficioProtCorrenteId(0);
		if (tipoUfficioRicerca.equals("C"))
			setTipoProtocollo("");
		else
			setTipoProtocollo("I");
		setProtocolliSelezionati(null);
		setCercaDaFascicolo(false);
	}

	public Collection<TipoProtocolloVO> getStatiProtocollo() {
		return statiProtocollo;
	}

	public int getAooId() {
		return aooId;
	}

	public void setAooId(int aooId) {
		this.aooId = aooId;
	}

	public void setStatiProtocollo(Collection<TipoProtocolloVO> statiProtocollo) {
		this.statiProtocollo = statiProtocollo;
	}

	public Collection getTipiDocumento() {
		return LookupDelegate.getInstance().getTipiDocumento(getAooId());
	}

	public String getTipoDocumento(int id) {
		Collection<TipoDocumentoVO> tipi = LookupDelegate.getInstance()
				.getTipiDocumento(getAooId());
		for (TipoDocumentoVO tpVO : tipi)
			if (tpVO.getId() == id)
				return tpVO.getDescrizione();
		return "";
	}

	public int getTipoDocumentoId() {
		return tipoDocumentoId;
	}

	public void setTipoDocumentoId(int tipoDocumentoId) {
		this.tipoDocumentoId = tipoDocumentoId;
	}

	private String progressivoFascicolo;

	public Collection getTitolariFigli() {
		return titolariFigli;
	}

	public void setTitolariFigli(Collection titolariFigli) {
		this.titolariFigli = titolariFigli;
	}

	public TitolarioVO getTitolario() {
		return titolario;
	}

	public void setTitolario(TitolarioVO titolario) {
		this.titolario = titolario;
	}

	public int getTitolarioPrecedenteId() {
		return titolarioPrecedenteId;
	}

	public void setTitolarioPrecedenteId(int titolarioPrecedenteId) {
		this.titolarioPrecedenteId = titolarioPrecedenteId;
	}

	public int getTitolarioSelezionatoId() {
		return titolarioSelezionatoId;
	}

	public void setTitolarioSelezionatoId(int titolarioSelezionatoId) {
		this.titolarioSelezionatoId = titolarioSelezionatoId;
	}

	public String getProgressivoFascicolo() {
		return progressivoFascicolo;
	}

	public void setProgressivoFascicolo(String progressivoFascicolo) {
		this.progressivoFascicolo = progressivoFascicolo;
	}

	public int getUtenteProtocollatoreId() {
		return utenteProtocollatoreId;
	}

	public void setUtenteProtocollatoreId(int utenteProtocollatoreId) {
		this.utenteProtocollatoreId = utenteProtocollatoreId;
	}

	public int getUfficioProtocollatoreId() {
		return ufficioProtocollatoreId;
	}

	public void setUfficioProtocollatoreId(int ufficioProtocollatoreId) {
		this.ufficioProtocollatoreId = ufficioProtocollatoreId;
	}

	private int ufficioProtCorrenteId;

	private String ufficioProtCorrentePath;

	private int ufficioProtSelezionatoId;

	private int ufficioProtRicercaId;

	private int utenteProtSelezionatoId;

	private UfficioVO ufficioProtCorrente;

	private Collection<UfficioVO> ufficiProtDipendenti;

	private Collection<UtenteVO> utentiProt;

	public UfficioVO getUfficioProtCorrente() {
		return ufficioProtCorrente;
	}

	public void setUfficioProtCorrente(UfficioVO ufficioProtCorrente) {
		this.ufficioProtCorrente = ufficioProtCorrente;
	}

	public int getUfficioProtCorrenteId() {
		return ufficioProtCorrenteId;
	}

	public void setUfficioProtCorrenteId(int ufficioProtCorrenteId) {
		this.ufficioProtCorrenteId = ufficioProtCorrenteId;
	}

	public String getUfficioProtCorrentePath() {
		return ufficioProtCorrentePath;
	}

	public void setUfficioProtCorrentePath(String ufficioProtCorrentePath) {
		this.ufficioProtCorrentePath = ufficioProtCorrentePath;
	}

	public int getUfficioProtRicercaId() {
		return ufficioProtRicercaId;
	}

	public void setUfficioProtRicercaId(int ufficioProtRicercaId) {
		this.ufficioProtRicercaId = ufficioProtRicercaId;
	}

	public int getUfficioProtSelezionatoId() {
		return ufficioProtSelezionatoId;
	}

	public void setUfficioProtSelezionatoId(int ufficioProtSelezionatoId) {
		this.ufficioProtSelezionatoId = ufficioProtSelezionatoId;
	}

	public Collection<UfficioVO> getUfficiProtDipendenti() {
		return ufficiProtDipendenti;
	}

	public void setUfficiProtDipendenti(Collection<UfficioVO> ufficiProtDipendenti) {
		this.ufficiProtDipendenti = ufficiProtDipendenti;
	}

	public int getUtenteProtSelezionatoId() {
		return utenteProtSelezionatoId;
	}

	public void setUtenteProtSelezionatoId(int utenteProtSelezionatoId) {
		this.utenteProtSelezionatoId = utenteProtSelezionatoId;
	}

	public Collection<UtenteVO> getUtentiProt() {
		return utentiProt;
	}

	public void setUtentiProt(Collection<UtenteVO> utentiProt) {
		this.utentiProt = utentiProt;
	}

	public boolean isTuttiUffici() {
		if (getUfficioProtCorrente() == null)
			return false;
		boolean tutti = getUfficioProtCorrente().getParentId() == 0;
		if (!tutti) {
			tutti = getUfficioProtCorrente().getId().equals(
					utenteCorrente.getUfficioVOInUso().getId());
		}
		return tutti;
	}

	public boolean isTuttiUfficiAssegnatari() {
		if (getUfficioCorrente() == null)
			return false;
		boolean tutti = getUfficioCorrente().getParentId() == 0;
		if (!tutti) {
			tutti = getUfficioCorrente().getId().equals(
					utenteCorrente.getUfficioVOInUso().getId());
		}
		return tutti;
	}

	public boolean isTuttiUfficiAssegnatariPIDest() {
		if (getUfficioCorrentePIDest() == null)
			return false;
		boolean tutti = getUfficioCorrentePIDest().getParentId() == 0;
		if (!tutti) {
			tutti = getUfficioCorrentePIDest().getId().equals(
					utenteCorrente.getUfficioVOInUso().getId());
		}
		return tutti;
	}

	public String getLabelDestinatarioAssegnatario() {
		if (getTipoProtocollo().equals("I")) {
			return "Assegnatari";
		} else if (getTipoProtocollo().equals("U")) {
			return "Destinatari";
		} else if (getTipoProtocollo().equals("M")) {
			return "Destinatari";
		} else if (getTipoProtocollo().equals("P")) {
			return "Assegnatari";
		} else if (getTipoProtocollo().equals(getMozioneUscita())) {
			return "Destinatari";
		} else {
			return "Assegnatari/Destinatari";
		}

	}

	public String getVisualizzaDestinatariAssegnatari() {

		if ("I".equals(getTipoProtocollo())) {
			return "I";
		} else if ("U".equals(getTipoProtocollo())) {
			return "U";
		} else if ("P".equals(getTipoProtocollo())) {
			return "P";
		} else if ("F".equals(getTipoProtocollo())) {
			return "I";
		} else {
			if (tipoUfficioRicerca.equals("C"))
				return null;
			else
				return "I";
		}

	}

	public String[] getProtocolliSelezionati() {
		return protocolliSelezionati;
	}

	public void setProtocolliSelezionati(String[] protocolliSelezionati) {
		this.protocolliSelezionati = protocolliSelezionati;
	}

	public String getDocumentoSelezionato() {
		return documentoSelezionato;
	}

	public void setDocumentoSelezionato(String documentoSelezionato) {
		this.documentoSelezionato = documentoSelezionato;
	}

	public int getDocumentoId() {
		return documentoId;
	}

	public void setDocumentoId(int documentoId) {
		this.documentoId = documentoId;
	}

	public boolean isIndietroVisibile() {
		return indietroVisibile;
	}

	public void setIndietroVisibile(boolean indietroVisibile) {
		this.indietroVisibile = indietroVisibile;
	}

}