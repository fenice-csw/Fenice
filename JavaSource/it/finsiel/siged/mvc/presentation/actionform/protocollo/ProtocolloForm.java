package it.finsiel.siged.mvc.presentation.actionform.protocollo;

import it.compit.fenice.enums.ReturnPageEnum;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.model.Fascicolo;
import it.finsiel.siged.model.organizzazione.AreaOrganizzativa;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.business.FascicoloDelegate;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.presentation.actionform.UploaderForm;
import it.finsiel.siged.mvc.vo.IdentityVO;
import it.finsiel.siged.mvc.vo.lookup.ProvinciaVO;
import it.finsiel.siged.mvc.vo.lookup.TipoDocumentoVO;
import it.finsiel.siged.mvc.vo.lookup.TitolarioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloProcedimentoVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;
import it.finsiel.siged.util.StringUtil;
import it.flosslab.mvc.vo.OggettoVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class ProtocolloForm extends UploaderForm implements
		AlberoUfficiUtentiForm {
	
	private static final long serialVersionUID = 1L;

	public class Sezione {

		private String nome;

		private boolean obbligatorio;

		private Map mappa;

		private Collection list;

		protected Sezione(String nome) {
			this.nome = nome;
		}

		public Sezione(String nome, boolean obbligatorio) {
			this.nome = nome;
			this.obbligatorio = obbligatorio;
		}

		protected Sezione(String nome, Map mappa) {
			this(nome);
			this.mappa = mappa;
		}

		protected Sezione(String nome, Collection collection) {
			this(nome);
			this.list = collection;
		}

		public Sezione(String nome, Map mappa, boolean obbligatorio) {
			this(nome, obbligatorio);
			this.mappa = mappa;
		}

		public Sezione(String nome, Collection collection, boolean obbligatorio) {
			this(nome, obbligatorio);
			this.list = collection;
		}

		public String getNome() {
			String nome = this.nome;

			if (mappa != null && mappa.size() > 0) {
				nome += " (" + mappa.size() + ")";
			}

			if (list != null && list.size() > 0) {
				nome += " (" + list.size() + ")";
			}

			if (nome.equals("Fascicoli")) {
				int count = fascicoliProtocollo.size()
						+ fascicoliProtocolloOld.size();
				if (count > 0)
					nome += " (" + count + ")";
			}
			return nome;
		}

		public boolean isCorrente() {
			String nome = sezioneVisualizzata;

			int i = nome.indexOf(" (");
			if (i > 0) {
				nome = nome.substring(0, i);
			}

			return this.nome.equals(nome);
		}

		public boolean isObbligatorio() {
			return obbligatorio;
		}

	}

	private List<Sezione> elencoSezioni;

	private String sezioneVisualizzata;

	private int aooId;

	private int protocolloId;

	private int numero;

	private int versione;

	private String flagTipo;

	private String stato;

	private String numeroProtocollo;

	private int ufficioCorrenteId;

	private String ufficioCorrentePath;

	private int ufficioSelezionatoId;

	private int utenteSelezionatoId;

	private UfficioVO ufficioCorrente;

	private Collection<UfficioVO> ufficiDipendenti;

	private Collection<UtenteVO> utenti;

	private boolean riservato;
	
	private boolean fatturaElettronica;
	
	private boolean flagAnomalia;
	
	private boolean flagRepertorio;

	private boolean oggettoToAdd;

	private boolean fisicaToAdd;

	private boolean giuridicaToAdd;

	boolean domandaErsu;
	
	boolean documentoReadable;

	private String[] allegatiSelezionatiId;

	private String descrizioneAnnotazione;

	private String posizioneAnnotazione;

	private String chiaveAnnotazione;

	private String[] allaccioSelezionatoId;

	private String allaccioProtocolloId;

	private String allaccioAnnoProtocollo;

	private String allaccioNumProtocollo;

	private Map<Integer,AllaccioVO> protocolliAllacciati;

	private int tipoDocumentoId;

	private String dataDocumento;

	private String dataRicezione;

	private String dataRegistrazione;

	private String oggetto;

	private String oggettoGenerico;

	private String cognomeMittente;

	private Integer documentoId;

	private DocumentoVO documentoPrincipale;

	private String nomeFilePrincipaleUpload;

	private Map<String,DocumentoVO> documentiAllegati;

	private TitolarioVO titolario;

	private int titolarioPrecedenteId;

	private int titolarioSelezionatoId;

	private Collection<TitolarioVO> titolariFigli;

	private String[] fascicoloSelezionatoId;

	private String[] fascicoloSelezionatoOldId;

	private String[] procedimentoSelezionatoId;

	private boolean modificabile;

	// dati protocollo annullato
	private String dataAnnullamento;

	private String notaAnnullamento;

	private String provvedimentoAnnullamento;

	private Map<Integer,FascicoloVO> fascicoliProtocollo;

	private Map<Integer,FascicoloVO> fascicoliProtocolloOld;

	private Collection<Integer> fascicoliEliminatiId;

	// dati procedimento
	private String oggettoProcedimento;

	private Map<Integer,ProtocolloProcedimentoVO> procedimentiProtocollo;

	private Collection<Integer> procedimentiRimossi;

	private boolean versioneDefault = true;

	private int numProtocolloEmergenza;

	private int utenteProtocollatoreId;
	
	private int caricaProtocollatoreId;

	private int ufficioProtocollatoreId;

	private int numeroProtocolliRegistroEmergenza;

	private boolean dipTitolarioUfficio;

	private boolean isUtenteAbilitatoSuUfficio;

	public String cercaFascicoloNome;

	public String cercaOggettoProcedimento;

	private String estremiAutorizzazione;

	private boolean documentoVisibile;

	private String autore;

	private int giorniAlert;

	private int oggettoSelezionato;

	private boolean visionato;

	private boolean allegatoScansionato;

	private boolean daBtnModifica;

	private boolean lavorato;
	
	private boolean daRicerca;
	
	private boolean daScarico;
	
	private Integer messaggioEmailId;

	private Integer messaggioEmailUfficioId;
	
	private ReturnPageEnum inputPage;
	
	public boolean isWebSocketEnabled() {
		return Organizzazione.getInstance().getValueObject().isWebSocketEnabled()
				;
	}
	
	public boolean isDocumentoReadable() {
		return documentoReadable;
	}

	public void setDocumentoReadable(boolean documentoReadable) {
		this.documentoReadable = documentoReadable;
	}

	public boolean isDomandaErsu() {
		return domandaErsu;
	}

	public void setDomandaErsu(boolean domandaErsu) {
		this.domandaErsu = domandaErsu;
	}

	public boolean isGiuridicaToAdd() {
		return giuridicaToAdd;
	}

	public void setGiuridicaToAdd(boolean giuridicaToAdd) {
		this.giuridicaToAdd = giuridicaToAdd;
	}

	public boolean isFisicaToAdd() {
		return fisicaToAdd;
	}

	public void setFisicaToAdd(boolean fisicaToAdd) {
		this.fisicaToAdd = fisicaToAdd;
	}

	public boolean isOggettoToAdd() {
		return oggettoToAdd;
	}

	public void setOggettoToAdd(boolean oggettoToAdd) {
		this.oggettoToAdd = oggettoToAdd;
	}
	
	public boolean isDaScarico() {
		return daScarico;
	}

	public void setDaScarico(boolean daScarico) {
		this.daScarico = daScarico;
	}

	public boolean isDaRicerca() {
		return daRicerca;
	}

	public void setDaRicerca(boolean daRicerca) {
		this.daRicerca = daRicerca;
	}

	public boolean isLavorato() {
		return lavorato;
	}

	public void setLavorato(boolean lavorato) {
		this.lavorato = lavorato;
	}

	public boolean isDaBtnModifica() {
		return daBtnModifica;
	}

	public void setDaBtnModifica(boolean daBtnModifica) {
		this.daBtnModifica = daBtnModifica;
	}

	public Collection<Integer> getFascicoliEliminatiId() {
		return fascicoliEliminatiId;
	}

	public void setFascicoliEliminatiId(Collection<Integer> fascicoliEliminatiId) {
		this.fascicoliEliminatiId = fascicoliEliminatiId;
	}

	public boolean isAllegatoScansionato() {
		return allegatoScansionato;
	}

	public void setAllegatoScansionato(boolean allegatoScansionato) {
		this.allegatoScansionato = allegatoScansionato;
	}

	public String getNomeFilePrincipaleUpload() {
		return nomeFilePrincipaleUpload;
	}

	public void setNomeFilePrincipaleUpload(String nomeFilePrincipaleUpload) {
		this.nomeFilePrincipaleUpload = nomeFilePrincipaleUpload;
	}

	public boolean isVisionato() {
		return visionato;
	}

	public void setVisionato(boolean visionato) {
		this.visionato = visionato;
	}

	public int getOggettoSelezionato() {
		return oggettoSelezionato;
	}

	public void setOggettoSelezionato(int oggettoSelezionato) {
		this.oggettoSelezionato = oggettoSelezionato;
	}

	public int getGiorniAlert() {
		return giorniAlert;
	}

	public void setGiorniAlert(int giorniAlert) {
		this.giorniAlert = giorniAlert;
	}

	public String getAutore() {
		return autore;
	}

	public void setAutore(String autore) {
		this.autore = autore;
	}

	public String getEstremiAutorizzazione() {
		return estremiAutorizzazione;
	}

	public void setEstremiAutorizzazione(String estremiAutorizzazione) {
		this.estremiAutorizzazione = estremiAutorizzazione;
	}

	public String getCercaFascicoloNome() {
		return cercaFascicoloNome;
	}

	public void setCercaFascicoloNome(String cercaFascicoloNome) {
		this.cercaFascicoloNome = cercaFascicoloNome;
	}

	public boolean isDipTitolarioUfficio() {
		return dipTitolarioUfficio;
	}

	public ProtocolloForm() {
		inizializzaForm();
	}

	public List<Sezione> getElencoSezioni() {
		return elencoSezioni;
	}

	public Collection<UfficioVO> getUfficiDipendenti() {
		return ufficiDipendenti;
	}

	public void setUfficiDipendenti(Collection<UfficioVO> ufficiDipendenti) {
		this.ufficiDipendenti = ufficiDipendenti;
	}

	public String getFlagTipo() {
		return flagTipo;
	}

	public void setFlagTipo(String flagTipo) {
		this.flagTipo = flagTipo;
	}

	public Collection<UtenteVO> getUtenti() {
		return utenti;
	}

	public UtenteVO getUtente(int utenteId) {
		for (Iterator<UtenteVO> i = getUtenti().iterator(); i.hasNext();) {
			UtenteVO ute = (UtenteVO) i.next();
			if (ute.getId().intValue() == utenteId) {
				return ute;
			}
		}
		return null;
	}

	public void setUtenti(Collection<UtenteVO> utenti) {
		this.utenti = utenti;
	}

	public String getSezioneVisualizzata() {
		return sezioneVisualizzata;
	}

	public void setSezioneVisualizzata(String displaySection) {
		this.sezioneVisualizzata = displaySection;
	}

	public int getUfficioCorrenteId() {
		return ufficioCorrenteId;
	}

	public void setUfficioCorrenteId(int ufficioCorrenteId) {
		this.ufficioCorrenteId = ufficioCorrenteId;
	}

	/**
	 * @return Returns the modificabile.
	 */
	public boolean isModificabile() {
		return modificabile;
	}

	/**
	 * @param modificabile
	 *            The modificabile to set.
	 */
	public void setModificabile(boolean modificabile) {
		this.modificabile = modificabile;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public int getUfficioSelezionatoId() {
		return ufficioSelezionatoId;
	}

	public void setUfficioSelezionatoId(int ufficioCorrenteId) {
		this.ufficioSelezionatoId = ufficioCorrenteId;
	}

	/**
	 * @return Returns the allaccioAnnoProtocollo.
	 */
	public String getAllaccioAnnoProtocollo() {
		return allaccioAnnoProtocollo;
	}

	/**
	 * @param allaccioAnnoProtocollo
	 *            The allaccioAnnoProtocollo to set.
	 */
	public void setAllaccioAnnoProtocollo(String allaccioAnnoProtocollo) {
		this.allaccioAnnoProtocollo = allaccioAnnoProtocollo;
	}

	/**
	 * @return Returns the allaccioProtocolloId.
	 */
	public String getAllaccioProtocolloId() {
		return allaccioProtocolloId;
	}

	/**
	 * @param allaccioProtocolloId
	 *            The allaccioProtocolloId to set.
	 */
	public void setAllaccioProtocolloId(String allaccioProtocolloId) {
		this.allaccioProtocolloId = allaccioProtocolloId;
	}

	/**
	 * @return Returns the tipoDocumentoId.
	 */

	public int getTipoDocumentoId() {
		return tipoDocumentoId;
		
	}

	/**
	 * @param tipoDocumentoId
	 *            The tipoDocumentoId to set.
	 */
	public void setTipoDocumentoId(int tipoDocumentoId) {
		this.tipoDocumentoId = tipoDocumentoId;
	}

	/**
	 * @return Returns the dataDocumento.
	 */
	public String getDataDocumento() {
		return dataDocumento;
	}

	/**
	 * @param dataDocumento
	 *            The dataDocumento to set.
	 */
	public void setDataDocumento(String dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	/**
	 * @return Returns the dataRicezione.
	 */
	public String getDataRicezione() {
		return dataRicezione;
	}

	/**
	 * @param dataRicezione
	 *            The dataRicezione to set.
	 */
	public void setDataRicezione(String dataRicezione) {
		this.dataRicezione = dataRicezione;
	}

	public boolean isDocumentoRiservato() {
		if (!getRiservato() || isModificabile())
			return false;
		else
			return true;
	}

	public String getOggetto() {
		if (!getRiservato() || isModificabile() || isDocumentoVisibile()) {
			if ("Altro".equals(oggetto)) {
				return oggettoGenerico;
			} else {
				return oggetto;
			}
		} else {
			return Parametri.PROTOCOLLO_RISERVATO;
		}
	}

	public String getOggettoGenerico() {
		return this.oggettoGenerico;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public void setOggettoGenerico(String oggettoGenerico) {
		this.oggettoGenerico = oggettoGenerico;
	}

	public Collection<AllaccioVO> getProtocolliAllacciati() {
		return protocolliAllacciati.values();
	}

	public void allacciaProtocollo(AllaccioVO allaccio) {
		if (allaccio != null) {
			this.protocolliAllacciati.put(new Integer(allaccio
					.getProtocolloAllacciatoId()), allaccio);
		}
	}

	public void rimuoviAllaccio(int allaccioId) {
		this.protocolliAllacciati.remove(new Integer(allaccioId));
	}

	public Collection<DocumentoVO> getDocumentiAllegatiCollection() {
		return documentiAllegati.values();
	}

	public void setDocumentiAllegati(Map<String,DocumentoVO> documenti) {
		this.documentiAllegati = documenti;
	}

	public Map<String,DocumentoVO> getDocumentiAllegati() {
		return documentiAllegati;
	}

	public DocumentoVO getUltimoDocumentoAllegato() {
		return ProtocolloBO.getUltimoDocumentoAllegato(this.documentiAllegati);
	}

	public void allegaDocumento(DocumentoVO doc) {
		ProtocolloBO.putAllegato(doc, this.documentiAllegati);
	}

	public void rimuoviAllegato(String allegatoId) {
		documentiAllegati.remove(allegatoId);
	}

	public void rimuoviDocumentoPrincipale() {
		setDocumentoPrincipale(new DocumentoVO());
	}

	public DocumentoVO getDocumentoAllegato(Object key) {
		return (DocumentoVO) this.documentiAllegati.get(key);
	}

	public DocumentoVO getDocumentoAllegato(int idx) {
		return (DocumentoVO) this.documentiAllegati.get(String.valueOf(idx));
	}

	/**
	 * @return Returns the titolario.
	 */
	public TitolarioVO getTitolario() {
		return titolario;
	}

	/**
	 * @param titolario
	 *            The titolario to set.
	 */
	public void setTitolario(TitolarioVO titolario) {
		this.titolario = titolario;
	}

	public String[] getFascicoloSelezionatoId() {
		return fascicoloSelezionatoId;
	}

	public void setFascicoloSelezionatoId(String[] fascicoloSelezionatoId) {
		this.fascicoloSelezionatoId = fascicoloSelezionatoId;
	}

	public String[] getFascicoloSelezionatoOldId() {
		return fascicoloSelezionatoOldId;
	}

	public void setFascicoloSelezionatoOldId(String[] fascicoloSelezionatoId) {
		this.fascicoloSelezionatoOldId = fascicoloSelezionatoId;
	}

	public Collection<ProvinciaVO> getProvince() {
		return getLookupDelegateDelegate().getProvince();
	}

	public Collection<TipoDocumentoVO> getTipiDocumento() {
		return getLookupDelegateDelegate().getTipiDocumento(getAooId());
	}

	public Collection<OggettoVO> getOggettario() {
		return getLookupDelegateDelegate().getOggetti(getAooId()).values();
	}

	public OggettoVO getOggettoFromOggettario() {
		return (OggettoVO) getLookupDelegateDelegate().getOggetti(getAooId())
				.get(getOggetto());
	}

	public Collection<IdentityVO> getTitoliDestinatario() {
		return getLookupDelegateDelegate().getTitoliDestinatario();
	}

	private LookupDelegate getLookupDelegateDelegate() {
		return LookupDelegate.getInstance();
	}

	/**
	 * @return Returns the id.
	 */
	public int getProtocolloId() {
		return protocolloId;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setProtocolloId(int id) {
		this.protocolloId = id;
	}

	/**
	 * @return Returns the allaccioNumProtocollo.
	 */
	public String getAllaccioNumProtocollo() {
		return allaccioNumProtocollo;
	}

	/**
	 * @param allaccioNumProtocollo
	 *            The allaccioNumProtocollo to set.
	 */
	public void setAllaccioNumProtocollo(String allaccioNumProtocollo) {
		this.allaccioNumProtocollo = allaccioNumProtocollo;
	}

	/**
	 * @return Returns the allaccioSelezionatoId.
	 */
	public String[] getAllaccioSelezionatoId() {
		return allaccioSelezionatoId;
	}

	/**
	 * @param allaccioSelezionatoId
	 *            The allaccioSelezionatoId to set.
	 */
	public void setAllaccioSelezionatoId(String[] allaccioSelezionatoId) {
		this.allaccioSelezionatoId = allaccioSelezionatoId;
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

	public boolean getRiservato() {
		return riservato;
	}

	public void setRiservato(boolean riservato) {
		this.riservato = riservato;
	}
	
	public boolean getFatturaElettronica() {
		return fatturaElettronica;
	}

	public void setFatturaElettronica(boolean fatturaElettronica) {
		this.fatturaElettronica = fatturaElettronica;
	}
	
	public boolean isFlagAnomalia() {
		return flagAnomalia;
	}

	public void setFlagAnomalia(boolean flagAnomalia) {
		this.flagAnomalia = flagAnomalia;
	}
	
	public boolean getFlagRepertorio() {
		return flagRepertorio;
	}

	public void setFlagRepertorio(boolean flagRepertorio) {
		this.flagRepertorio = flagRepertorio;
	}

	public String[] getAllegatiSelezionatiId() {
		return allegatiSelezionatiId;
	}

	public void setAllegatiSelezionatiId(String[] allegatiSelezionatoId) {
		this.allegatiSelezionatiId = allegatiSelezionatoId;
	}

	public DocumentoVO getDocumentoPrincipale() {
		return documentoPrincipale;
	}

	public void setDocumentoPrincipale(DocumentoVO documentoPrincipale) {
		this.documentoPrincipale = documentoPrincipale;
	}

	public int getUtenteSelezionatoId() {
		return utenteSelezionatoId;
	}

	public void setUtenteSelezionatoId(int utenteCorrenteId) {
		this.utenteSelezionatoId = utenteCorrenteId;
	}

	public Collection<TitolarioVO> getTitolariFigli() {
		return titolariFigli;
	}

	public void setTitolariFigli(Collection<TitolarioVO> titolariDiscendenti) {
		this.titolariFigli = titolariDiscendenti;
	}

	public int getTitolarioSelezionatoId() {
		return titolarioSelezionatoId;
	}

	public void setTitolarioSelezionatoId(int titolarioSelezionatoId) {
		this.titolarioSelezionatoId = titolarioSelezionatoId;
	}

	public int getTitolarioPrecedenteId() {
		return titolarioPrecedenteId;
	}

	public void setTitolarioPrecedenteId(int titolarioPrecedenteId) {
		this.titolarioPrecedenteId = titolarioPrecedenteId;
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

	public String getNumeroProtocollo() {
		return numeroProtocollo;
	}

	public String getNumeroProtocolloFormattato() {
		String formatted = ProtocolloBO
				.formattaNumeroProtocollo(numeroProtocollo);
		if (formatted == null)
			return "";
		else
			return formatted;
	}

	public String getNumeroProtocolloScanner() {
		AreaOrganizzativa aoo=Organizzazione.getInstance().getAreaOrganizzativa(
				getAooId());
		String descrizioneAoo =aoo.getValueObject().getDescription()+" "+aoo.getValueObject().getCodi_aoo();

		return getNumeroProtocolloFormattato() + "/" + getFlagTipo() + " del "
				+ getDataRegistrazione() + " " + descrizioneAoo;
	}

	public void setNumeroProtocollo(String numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}

	public String getDataRegistrazione() {
		return dataRegistrazione;
	}

	public String getDataRegistrazioneToEtichetta() {
		if (dataRegistrazione != null)
			return dataRegistrazione.substring(0, 10);
		else
			return dataRegistrazione;
	}

	public void setDataRegistrazione(String dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	public String getProtocolloRiservato() {
		return Parametri.PROTOCOLLO_RISERVATO;
	}

	public int getNumero() {
		return numero;
	}

	public String getNumeroEtichetta() {
		return StringUtil.formattaNumeroProtocollo(numero + "", 7);
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	/**
	 * @return Returns the dataAnnullamento.
	 */
	public String getDataAnnullamento() {
		return dataAnnullamento;
	}

	/**
	 * @param dataAnnullamento
	 *            The dataAnnullamento to set.
	 */
	public void setDataAnnullamento(String dataAnnullamento) {
		this.dataAnnullamento = dataAnnullamento;
	}

	/**
	 * @return Returns the notaAnnullamento.
	 */
	public String getNotaAnnullamento() {
		return notaAnnullamento;
	}

	/**
	 * @param notaAnnullamento
	 *            The notaAnnullamento to set.
	 */
	public void setNotaAnnullamento(String notaAnnullamento) {
		this.notaAnnullamento = notaAnnullamento;
	}

	/**
	 * @return Returns the provvedimentoAnnullamento.
	 */
	public String getProvvedimentoAnnullamento() {
		return provvedimentoAnnullamento;
	}

	/**
	 * @param provvedimentoAnnullamento
	 *            The provvedimentoAnnullamento to set.
	 */
	public void setProvvedimentoAnnullamento(String provvedimentoAnnullamento) {
		this.provvedimentoAnnullamento = provvedimentoAnnullamento;
	}

	public int getVersione() {
		return versione;
	}

	public void setVersione(int versione) {
		this.versione = versione;
	}

	public FascicoloVO getFascicolo() {
		FascicoloVO fasc = null;
		if (getFascicoliProtocollo().size() == 1) {
			for (FascicoloVO o : getFascicoliProtocollo())
				fasc = o;
		}
		return fasc;
	}
	
	public FascicoloVO getFascicoloOld() {
		FascicoloVO fasc = null;
		if (getFascicoliProtocolloOld().size() == 1) {
			for (FascicoloVO o : getFascicoliProtocolloOld())
				fasc = o;
		}
		return fasc;
	}

	public Collection<FascicoloVO> getFascicoliProtocollo() {
		return fascicoliProtocollo.values();
	}

	public Collection<FascicoloVO> getFascicoliProtocolloOld() {
		return fascicoliProtocolloOld.values();
	}

	public Map<Integer,FascicoloVO> getFascicoliProtocolloOldMap() {
		return fascicoliProtocolloOld;
	}

	public void copyFascicoliOldToNew() {
		for (FascicoloVO f : getFascicoliProtocolloOld())
			if (f.getStato() == 0) {
				f.setOwner(true);
				this.fascicoliProtocollo.put(f.getId(), f);
				setLavorato(true);
			}
		this.fascicoliProtocolloOld.clear();
	}

	public void aggiungiFascicolo(FascicoloVO fascicolo) {
		if (fascicolo != null && fascicolo.getId() != null
				&& !fascicoliProtocolloOld.containsKey(fascicolo.getId()))
			this.fascicoliProtocollo.put(fascicolo.getId(), fascicolo);
		setLavorato(true);

	}

	public void aggiungiFascicoloOld(FascicoloVO fascicolo) {
		if (fascicolo != null) {
			if (fascicolo.getId() != null) {
				this.fascicoliProtocolloOld.put(fascicolo.getId(), fascicolo);
			}
		}

	}

	public void removeFascicoliOld() {
		if (!this.fascicoliProtocolloOld.isEmpty())
			fascicoliProtocolloOld.clear();
	}

	public void setFascicoli(Map<Integer,FascicoloVO> fascicoliProtocollo) {
		this.fascicoliProtocollo = fascicoliProtocollo;
	}

	public void rimuoviFascicolo(int fascicoloId) {
		Integer key = new Integer(fascicoloId);
		this.fascicoliProtocollo.remove(key);
	}

	public void rimuoviFascicoloOld(int fascicoloId) {
		Integer key = new Integer(fascicoloId);
		this.fascicoliProtocolloOld.remove(key);
		this.fascicoliEliminatiId.add(key);
	}

	public String getDescrizioneStatoProtocollo() {
		/*
		 * possibili stati del protocollo-> A -> cambio in ASSOCIATO ALL'UTENTE
		 * C -> ANNULLATO R -> cambio in RIASSEGNATO F -> RESPINTO N -> cambio
		 * in ASSOCIATO ALL'UFFICIO S -> FASCICOLATO P -> associato al
		 * Procedimento
		 */
		if (!this.getStato().equals("N") && !this.getStato().equals("S"))
			return ProtocolloBO.getStatoProtocollo(this.getFlagTipo(), this
					.getStato());
		if (getVersione() <= 1)
			return "Registrato";
		if (getVersione() > 1)
			return "Modificato";
		return "non classificato";

		// return ProtocolloBO.getStatoProtocollo(getFlagTipo(), getStato());
	}

	public boolean getVersioneDefault() {
		return versioneDefault;
	}

	public void setVersioneDefault(boolean versioneDefault) {
		this.versioneDefault = versioneDefault;
	}

	public int getAooId() {
		return aooId;
	}

	public void setAooId(int aooId) {
		this.aooId = aooId;
	}

	public int getNumProtocolloEmergenza() {
		return numProtocolloEmergenza;
	}

	public void setNumProtocolloEmergenza(int numProtocolloEmergenza) {
		this.numProtocolloEmergenza = numProtocolloEmergenza;
	}

	public int getUfficioProtocollatoreId() {
		return ufficioProtocollatoreId;
	}

	public void setUfficioProtocollatoreId(int ufficioProtocollatoreId) {
		this.ufficioProtocollatoreId = ufficioProtocollatoreId;
	}

	public int getUtenteProtocollatoreId() {
		return utenteProtocollatoreId;
	}

	public void setUtenteProtocollatoreId(int utenteProtocollatoreId) {
		this.utenteProtocollatoreId = utenteProtocollatoreId;
	}
	
	public int getCaricaProtocollatoreId() {
		return caricaProtocollatoreId;
	}

	public void setCaricaProtocollatoreId(int caricaProtocollatoreId) {
		this.caricaProtocollatoreId = caricaProtocollatoreId;
	}

	public String getProtocollatore() {
		Organizzazione org = Organizzazione.getInstance();
		Ufficio ufficio = org.getUfficio(getUfficioProtocollatoreId());
		CaricaVO carica = org.getCarica(getCaricaProtocollatoreId());
		Utente utente=org.getUtente(getUtenteProtocollatoreId());
		String protocollatore = "";
		if (ufficio != null)
			protocollatore = ufficio.getValueObject().getDescription();
		if (utente != null)
			protocollatore += "/" +utente.getValueObject().getFullName();
		if (carica != null)
			protocollatore += "("+carica.getNome()+")";
		return protocollatore;
	}

	public int getNumeroProtocolliRegistroEmergenza() {
		return numeroProtocolliRegistroEmergenza;
	}

	public void setNumeroProtocolliRegistroEmergenza(
			int numeroProtocolliRegistroEmergenza) {
		this.numeroProtocolliRegistroEmergenza = numeroProtocolliRegistroEmergenza;
	}

	public void inizializzaForm() {
		setAllaccioAnnoProtocollo(null);
		setAllaccioNumProtocollo(null);
		setAllaccioProtocolloId(null);
		setAllaccioSelezionatoId(null);
		setAllegatiSelezionatiId(null);
		setChiaveAnnotazione(null);
		setDataDocumento(null);
		setDataRicezione(null);
		setDescrizioneAnnotazione(null);
		documentiAllegati = new HashMap<String, DocumentoVO> (2);
		setDocumentoPrincipale(new DocumentoVO());
		setFascicoloSelezionatoId(null);
		setRiservato(false);
		setFatturaElettronica(false);
		setFlagRepertorio(false);
		setProtocolloId(0);
		setOggetto(null);
		setPosizioneAnnotazione(null);
		protocolliAllacciati = new HashMap<Integer, AllaccioVO> ();
		setSezioneVisualizzata("Mittente");
		setTitolariFigli(null);
		setTitolario(null);
		setTitolarioPrecedenteId(0);
		setTitolarioSelezionatoId(0);
		setUfficioSelezionatoId(0);
		setUtenteSelezionatoId(0);
		setModificabile(true);
		setOggettoProcedimento(null);
		setLavorato(false);
		fascicoliProtocollo = new HashMap<Integer,FascicoloVO>();
		fascicoliProtocolloOld = new HashMap<Integer,FascicoloVO>();
		fascicoliEliminatiId = new ArrayList<Integer>();
		procedimentiProtocollo = new HashMap<Integer, ProtocolloProcedimentoVO> ();
		setProvvedimentoAnnullamento(null);
		setNotaAnnullamento(null);
		setNumProtocolloEmergenza(0);
		setVersione(0);
		setOggettoGenerico(null);
		aggiornaSezioni();
	}

	public void aggiornaSezioni() {
		elencoSezioni = new ArrayList<Sezione>();
		elencoSezioni.add(new Sezione("Allegati", documentiAllegati));
		elencoSezioni.add(new Sezione("Collegati", protocolliAllacciati));
		elencoSezioni.add(new Sezione("Annotazioni"));
		// elencoSezioni.add(new Sezione("Fascicoli", fascicoliProtocolloOld,
		// true));
		elencoSezioni.add(new Sezione("Fascicoli", true));
		elencoSezioni.add(new Sezione("Procedimenti", procedimentiProtocollo));
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		Utente utente = (Utente) request.getSession().getAttribute(
				Constants.UTENTE_KEY);
		if (utente != null) {
			dipTitolarioUfficio = utente.getAreaOrganizzativa()
					.getDipendenzaTitolarioUfficio() == 1;
		}
	}

	public void inizializzaRipetiForm() {
		setAllaccioAnnoProtocollo(null);
		setAllaccioNumProtocollo(null);
		setAllaccioProtocolloId(null);
		setAllaccioSelezionatoId(null);
		setAllegatiSelezionatiId(null);
		setChiaveAnnotazione(null);
		if (dataDocumento == null) {
			setDataDocumento(null);
		}
		if (dataRicezione == null) {
			setDataRicezione(null);
		}
		setDescrizioneAnnotazione(null);
		documentiAllegati = new HashMap<String, DocumentoVO>();
		setDocumentoPrincipale(new DocumentoVO());
		setFatturaElettronica(false);
		setFlagRepertorio(false);
		setRiservato(false);
		setProtocolloId(0);
		setPosizioneAnnotazione(null);
		protocolliAllacciati = new HashMap<Integer, AllaccioVO> ();
		setSezioneVisualizzata("Mittente");
		setTitolariFigli(null);
		if (titolario == null) {
			setTitolario(null);
			setTitolarioPrecedenteId(0);
			setTitolarioSelezionatoId(0);
		}
		setUfficioSelezionatoId(0);
		setUtenteSelezionatoId(0);
		setModificabile(true);
		// fascicolo = new FascicoloVO();
		setFascicoloSelezionatoId(null);
		// setFascicolo(new FascicoloVO());
		// setFascicoli(null);
		setOggettoProcedimento(null);
		// fascicoliProtocollo = new HashMap();
		procedimentiProtocollo = new HashMap<Integer, ProtocolloProcedimentoVO> ();
		setProvvedimentoAnnullamento(null);
		setNotaAnnullamento(null);
		setNumProtocolloEmergenza(0);
		setVersione(0);
		//**ANDREA ADD**//
		setMessaggioEmailId(null);
		//**END**//
		aggiornaSezioni();
	}

	public void inizializzaFormToCopyProtocollo() {
		documentiAllegati = new HashMap<String, DocumentoVO>(2);
		setDocumentoPrincipale(new DocumentoVO());
		setNomeFilePrincipaleUpload("");
		setProtocolloId(0);
		setNumeroProtocollo(null);
		setVersione(0);
		//for()
		if(!getFlagTipo().equals("I"))
			copyFascicoliOldToNew();
	}

	public ActionErrors validateAnnullamentoProtocollo(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		if (request.getParameter("btnConfermaAnnullamento") != null
				&& (getProvvedimentoAnnullamento() == null || ""
						.equals(getProvvedimentoAnnullamento()))) {
			errors.add("annullaProtocollo", new ActionMessage(
					"campo.obbligatorio", "Provvedimento annullamento", ""));
		}
		return errors;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		if (request.getParameter("allacciaProtocolloAction") != null) {
			if ("".equals(getAllaccioAnnoProtocollo())) {
				errors.add("allaccioAnnoProtocollo", new ActionMessage(
						"campo.obbligatorio", "Anno allaccio", ""));
			} else if (!NumberUtil.isInteger(getAllaccioAnnoProtocollo())) {
				errors.add("allaccioAnnoProtocollo", new ActionMessage(
						"formato.numerico.errato", "Anno allaccio"));
			}
			if ("".equals(getAllaccioNumProtocollo())) {
				errors.add("allaccioNumProtocollo", new ActionMessage(
						"campo.obbligatorio", "Numero allaccio", ""));
			} else if (!NumberUtil.isInteger(getAllaccioNumProtocollo())) {
				errors.add("allaccioNumProtocollo", new ActionMessage(
						"formato.numerico.errato", "Numero allaccio"));
			}

		} else if (request.getParameter("downloadAllegatoId") != null) {
			if (!NumberUtil.isInteger(request
					.getParameter("downloadAllegatoId"))) {
				errors.add("downloadAllegatoId", new ActionMessage(
						"formato.numerico.errato", "Identificativo Documento"));
			}

		} else if (request.getParameter("salvaAction") != null) {
			String dataDoc = getDataDocumento();
			String dataRic = getDataRicezione();
			String dataReg = getDataRegistrazione();

			if (dataDoc != null && !"".equals(dataDoc)) {
				if (!DateUtil.isData(dataDoc)) {
					errors.add("dataDocumento", new ActionMessage(
							"formato.data.errato", "Data"));
				} else {
					if (DateUtil.toDate(dataReg).before(
							DateUtil.toDate(dataDoc))) {
						errors.add("dataRegistrazione", new ActionMessage(
								"data_registrazione.minore.data_documento"));
					}
				}
			}
			if (dataRic != null && !"".equals(dataRic)) {
				if (!DateUtil.isData(dataRic)) {
					errors.add("dataRicezione", new ActionMessage(
							"formato.data.errato", "Ricevuto il"));
				} else {
					if (DateUtil.toDate(dataReg).before(
							DateUtil.toDate(dataRic))) {
						errors.add("dataRegistrazione", new ActionMessage(
								"data_registrazione.minore.data_ricezione"));
					}
				}
			}
			if (dataDoc != null
					&& !"".equals(dataDoc)
					&& DateUtil.isData(dataDoc)
					&& dataRic != null
					&& !"".equals(dataRic)
					&& DateUtil.isData(dataRic)
					&& DateUtil.toDate(dataRic)
							.before(DateUtil.toDate(dataDoc))) {
				errors.add("dataRicezione", new ActionMessage(
						"data_ricezione.minore.data_documento"));
			}
			if (getOggetto() == null || "".equals(getOggetto().trim())) {
				if (!getFlagTipo().equals("R"))
					errors.add("oggetto", new ActionMessage(
							"campo.obbligatorio", "Oggetto", ""));

			} else if ("Altro".equals(getOggetto())
					&& (getOggettoGenerico() == null || ""
							.equals(getOggettoGenerico()))) {

				errors.add("oggetto", new ActionMessage("campo.obbligatorio",
						"Oggetto", ""));
			}

		}
		return errors;
	}

	public String getCognomeMittente() {
		return cognomeMittente;
	}

	public void setCognomeMittente(String cognomeMittente) {
		this.cognomeMittente = cognomeMittente;
	}

	public Integer getDocumentoId() {
		return documentoId;
	}

	public void setDocumentoId(Integer documentoId) {
		this.documentoId = documentoId;
	}

	public String getOggettoProcedimento() {
		return oggettoProcedimento;
	}

	public void setOggettoProcedimento(String oggettoProcedimento) {
		this.oggettoProcedimento = oggettoProcedimento;
	}

	public String[] getProcedimentoSelezionatoId() {
		return procedimentoSelezionatoId;
	}

	public void setProcedimentoSelezionatoId(String[] procedimentoSelezionatoId) {
		this.procedimentoSelezionatoId = procedimentoSelezionatoId;
	}

	public Collection<ProtocolloProcedimentoVO> getProcedimentiProtocollo() {
		return procedimentiProtocollo.values();
	}

	public void aggiungiProcedimento(ProtocolloProcedimentoVO procedimento) {
		this.procedimentiProtocollo.put(new Integer(procedimento
				.getProcedimentoId()), procedimento);
		Fascicolo fascicolo = FascicoloDelegate.getInstance().getFascicoloById(
				procedimento.getFascicoloId());
		aggiungiFascicolo(fascicolo.getFascicoloVO());
	}

	public void aggiungiProcedimentoDaRimuovere(
			int procedimentoId) {
		if (this.procedimentiRimossi == null)
			this.procedimentiRimossi = new ArrayList<Integer>();
		this.procedimentiRimossi.add( procedimentoId);
	}

	public Collection<Integer> getProcedimentiRimossi(){
		return procedimentiRimossi;
	}
	
	public void rimuoviProcedimento(int procedimentoId) {
		this.procedimentiProtocollo.remove(new Integer(procedimentoId));
		aggiungiProcedimentoDaRimuovere(procedimentoId);
	}

	public boolean getUtenteAbilitatoSuUfficio() {
		return isUtenteAbilitatoSuUfficio;
	}

	public void setUtenteAbilitatoSuUfficio(boolean isUtenteAbilitatoSuUfficio) {
		this.isUtenteAbilitatoSuUfficio = isUtenteAbilitatoSuUfficio;
	}

	public String getCercaOggettoProcedimento() {
		return cercaOggettoProcedimento;
	}

	public void setCercaOggettoProcedimento(String cercaOggettoProcedimento) {
		this.cercaOggettoProcedimento = cercaOggettoProcedimento;
	}

	public boolean isDocumentoVisibile() {
		return documentoVisibile;
	}

	public void setDocumentoVisibile(boolean documentoVisibile) {
		this.documentoVisibile = documentoVisibile;
	}

	public ReturnPageEnum getInputPage() {
		return inputPage;
	}

	public void setInputPage(ReturnPageEnum inputPage) {
		this.inputPage = inputPage;
	}

	public Integer getMessaggioEmailId() {
		return messaggioEmailId;
	}

	public void setMessaggioEmailId(Integer messaggioEmailId) {
		this.messaggioEmailId = messaggioEmailId;
	}

	public Integer getMessaggioEmailUfficioId() {
		return messaggioEmailUfficioId;
	}

	public void setMessaggioEmailUfficioId(Integer messaggioEmailUfficioId) {
		this.messaggioEmailUfficioId = messaggioEmailUfficioId;
	}

	public String getTitolarioFascicolo() {
		if (getFlagTipo().equals("U")) {
			String cod = "/";
			FascicoloVO f = getFascicoloOld();
			if (f != null)
				cod = f.getAnnoProgressivo();
			return cod;
		} else
			return "/";
	}

}