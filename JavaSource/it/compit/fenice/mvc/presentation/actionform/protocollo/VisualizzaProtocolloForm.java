package it.compit.fenice.mvc.presentation.actionform.protocollo;

import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.presentation.actionform.UploaderForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.presentation.helper.DestinatarioView;
import it.finsiel.siged.mvc.vo.IdentityVO;
import it.finsiel.siged.mvc.vo.lookup.ProvinciaVO;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.lookup.TipoDocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloProcedimentoVO;
import it.finsiel.siged.util.NumberUtil;
import it.finsiel.siged.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class VisualizzaProtocolloForm extends UploaderForm {

	private static final long serialVersionUID = 1L;

	private int protocolloId;

	private int aooId;

	private int numero;

	private int versione;

	private String flagTipo;

	private String stato;

	private String numeroProtocollo;

	private boolean riservato;

	private String[] allegatiSelezionatiId;

	private String descrizioneAnnotazione;

	private String posizioneAnnotazione;

	private String chiaveAnnotazione;

	private String[] allaccioSelezionatoId;

	private String allaccioProtocolloId;

	private String allaccioAnnoProtocollo;

	private String allaccioNumProtocollo;

	private Map<Integer,AllaccioVO> protocolliAllacciati = new HashMap<Integer,AllaccioVO>(2);

	private int tipoDocumentoId;

	private String dataDocumento;

	private String dataRicezione;

	private String dataRegistrazione;

	private String oggetto;

	private Integer documentoId;

	private DocumentoVO documentoPrincipale = new DocumentoVO();

	private Map<String,DocumentoVO> documentiAllegati = new HashMap<String,DocumentoVO>(2);

	private boolean modificabile;

	private String dataAnnullamento;

	private String notaAnnullamento;

	private String provvedimentoAnnullamento;

	private FascicoloVO fascicolo = new FascicoloVO();

	private Collection<FascicoloVO> fascicoli = new ArrayList<FascicoloVO>();

	private Map<Integer,FascicoloVO> fascicoliProtocollo = new HashMap<Integer,FascicoloVO>(2);

	private String oggettoProcedimento;

	private Map<Integer,ProtocolloProcedimentoVO> procedimentiProtocollo = new HashMap<Integer,ProtocolloProcedimentoVO> (2);

	private boolean versioneDefault = true;

	private int numProtocolloEmergenza;

	private int utenteProtocollatoreId;

	private int ufficioProtocollatoreId;

	private int numeroProtocolliRegistroEmergenza;

	private boolean dipTitolarioUfficio;

	private String estremiAutorizzazione;

	private boolean documentoVisibile;

	private String autore;

	private String numProtocolloMittente;

	private SoggettoVO mittenteIngresso = new SoggettoVO('F');

	private SoggettoVO multiMittenteCorrenteIngresso = new SoggettoVO('F');

	private List<SoggettoVO> mittentiIngresso = new ArrayList<SoggettoVO>();

	private List<SoggettoVO> storiaMittentiIngresso = new ArrayList<SoggettoVO>();

	private Map<String,AssegnatarioView> assegnatari = new HashMap<String,AssegnatarioView>(2);

	private String assegnatarioCompetente;

	private String[] assegnatariCompetenti;

	private AssegnatarioView mittenteUscita = new AssegnatarioView();

	private Map<String,DestinatarioView> destinatari = new HashMap<String,DestinatarioView>(2);

	private Map<String,Integer> destinatariIds = new HashMap<String,Integer>(2);

	private int protocolloAllacciatoId;

	private String protocolloAllacciatoType;

	public int getProtocolloAllacciatoId() {
		return protocolloAllacciatoId;
	}

	public String getProtocolloAllacciatoType() {
		return protocolloAllacciatoType;
	}

	public void setProtocolloAllacciatoId(int protocolloAllacciatoId) {
		this.protocolloAllacciatoId = protocolloAllacciatoId;
	}

	public void setProtocolloAllacciatoType(String protocolloAllacciatoType) {
		this.protocolloAllacciatoType = protocolloAllacciatoType;
	}

	public String getAssegnatarioCompetente() {
		return assegnatarioCompetente;
	}

	public int getAooId() {
		return aooId;
	}

	public void setAooId(int aooId) {
		this.aooId = aooId;
	}

	public String[] getAssegnatariCompetenti() {
		return assegnatariCompetenti;
	}

	public void setAssegnatarioCompetente(String assegnatarioCompetente) {
		this.assegnatarioCompetente = assegnatarioCompetente;
		for (Iterator<AssegnatarioView> i = getAssegnatari().iterator(); i.hasNext();) {
			AssegnatarioView ass = i.next();
			if (ass.getKey().equals(assegnatarioCompetente)) {
				ass.setCompetente(ass.getKey().equals(assegnatarioCompetente));
			}
		}
	}

	public void setAssegnatariCompetenti(String[] assegnatariCompetenti) {
		this.assegnatariCompetenti = assegnatariCompetenti;
		for (Iterator<AssegnatarioView> i = getAssegnatari().iterator(); i.hasNext();) {
			AssegnatarioView ass = i.next();
			ass.setCompetente(this.isCompetente(ass));
		}
	}

	public boolean isCompetente(AssegnatarioView ass) {
		if (this.assegnatariCompetenti == null && this.assegnatari.size() == 1)
			return true;
		for (String assCompetente : this.assegnatariCompetenti) {
			if (assCompetente.equals(ass.getKey())) {
				return true;
			}
		}
		return false;
	}

	public Collection<AssegnatarioView> getAssegnatari() {
		return assegnatari.values();
	}

	public void aggiungiAssegnatario(AssegnatarioView ass) {
		assegnatari.put(ass.getKey(), ass);
	}

	public Collection<DestinatarioView> getDestinatari() {
		return destinatari.values();
	}

	public DestinatarioView getDestinatario(String id) {
		return (DestinatarioView) destinatari.get(id);
	}

	public void aggiungiDestinatario(DestinatarioView destinatario) {
		if (destinatario != null) {
			if (destinatario.getIdx() == 0) {
				int idx = getNextId(destinatariIds);
				destinatario.setIdx(idx);
				destinatari.put(String.valueOf(idx), destinatario);
				destinatariIds.put(String.valueOf(idx), new Integer(idx));
			} else {
				destinatari.put(String.valueOf(destinatario.getIdx()),
						destinatario);
			}

		}
	}
	
	public void resetDestinatari(){
		destinatari.clear();
		destinatariIds.clear();
	}

	private static int getNextId(Map<String,Integer> m) {
		int max = 0;
		Iterator<String> it = m.keySet().iterator();
		while (it.hasNext()) {
			String id =it.next();
			int cur = NumberUtil.getInt(id);
			if (cur > max)
				max = cur;
		}
		return max + 1;
	}

	public String getAutore() {
		return autore;
	}

	public AssegnatarioView getMittenteUscita() {
		return mittenteUscita;
	}

	public void setMittenteUscita(AssegnatarioView mittenteUscita) {
		this.mittenteUscita = mittenteUscita;
	}

	public String getNumProtocolloMittente() {
		return numProtocolloMittente;
	}

	public SoggettoVO getMittenteIngresso() {
		return mittenteIngresso;
	}

	public SoggettoVO getMultiMittenteCorrenteIngresso() {
		return multiMittenteCorrenteIngresso;
	}

	public List<SoggettoVO> getMittentiIngresso() {
		return mittentiIngresso;
	}

	public List<SoggettoVO> getStoriaMittentiIngresso() {
		return storiaMittentiIngresso;
	}

	public void setNumProtocolloMittente(String numProtocolloMittente) {
		this.numProtocolloMittente = numProtocolloMittente;
	}

	public void setMittenteIngresso(SoggettoVO mittenteIN) {
		this.mittenteIngresso = mittenteIN;
	}

	public void setMultiMittenteCorrenteIngresso(
			SoggettoVO multiMittenteCorrenteIN) {
		this.multiMittenteCorrenteIngresso = multiMittenteCorrenteIN;
	}

	public void setMittentiIngresso(List<SoggettoVO> mittentiIN) {
		this.mittentiIngresso = mittentiIN;
	}

	public void setStoriaMittentiIngresso(List<SoggettoVO> storiaMittentiIN) {
		this.storiaMittentiIngresso = storiaMittentiIN;
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

	public boolean isDipTitolarioUfficio() {
		return dipTitolarioUfficio;
	}

	public VisualizzaProtocolloForm() {
		inizializzaForm();
	}

	public String getFlagTipo() {
		return flagTipo;
	}

	public void setFlagTipo(String flagTipo) {
		this.flagTipo = flagTipo;
	}

	public boolean isModificabile() {
		return modificabile;
	}

	public void setModificabile(boolean modificabile) {
		this.modificabile = modificabile;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getAllaccioAnnoProtocollo() {
		return allaccioAnnoProtocollo;
	}

	public void setAllaccioAnnoProtocollo(String allaccioAnnoProtocollo) {
		this.allaccioAnnoProtocollo = allaccioAnnoProtocollo;
	}

	public String getAllaccioProtocolloId() {
		return allaccioProtocolloId;
	}

	public void setAllaccioProtocolloId(String allaccioProtocolloId) {
		this.allaccioProtocolloId = allaccioProtocolloId;
	}

	public int getTipoDocumentoId() {
		return tipoDocumentoId;
	}

	public void setTipoDocumentoId(int tipoDocumentoId) {
		this.tipoDocumentoId = tipoDocumentoId;
	}

	public String getDataDocumento() {
		return dataDocumento;
	}

	public void setDataDocumento(String dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	public String getDataRicezione() {
		return dataRicezione;
	}

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
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
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

	public int getProtocolloId() {
		return protocolloId;
	}

	public void setProtocolloId(int id) {
		this.protocolloId = id;
	}

	public String getAllaccioNumProtocollo() {
		return allaccioNumProtocollo;
	}

	public void setAllaccioNumProtocollo(String allaccioNumProtocollo) {
		this.allaccioNumProtocollo = allaccioNumProtocollo;
	}

	public String[] getAllaccioSelezionatoId() {
		return allaccioSelezionatoId;
	}

	public void setAllaccioSelezionatoId(String[] allaccioSelezionatoId) {
		this.allaccioSelezionatoId = allaccioSelezionatoId;
	}

	public String getDescrizioneAnnotazione() {
		return descrizioneAnnotazione;
	}

	public void setDescrizioneAnnotazione(String descrizioneAnnotazione) {
		this.descrizioneAnnotazione = descrizioneAnnotazione;
	}

	public String getChiaveAnnotazione() {
		return chiaveAnnotazione;
	}

	public void setChiaveAnnotazione(String chiaveAnnotazione) {
		this.chiaveAnnotazione = chiaveAnnotazione;
	}

	public String getPosizioneAnnotazione() {
		return posizioneAnnotazione;
	}

	public void setPosizioneAnnotazione(String posizioneAnnotazione) {
		this.posizioneAnnotazione = posizioneAnnotazione;
	}

	public boolean getRiservato() {
		return riservato;
	}

	public void setRiservato(boolean riservato) {
		this.riservato = riservato;
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

	public String getNumeroProtocollo() {
		return numeroProtocollo;
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

	public String getDataAnnullamento() {
		return dataAnnullamento;
	}

	public void setDataAnnullamento(String dataAnnullamento) {
		this.dataAnnullamento = dataAnnullamento;
	}

	public String getNotaAnnullamento() {
		return notaAnnullamento;
	}

	public void setNotaAnnullamento(String notaAnnullamento) {
		this.notaAnnullamento = notaAnnullamento;
	}

	public String getProvvedimentoAnnullamento() {
		return provvedimentoAnnullamento;
	}

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
		return fascicolo;
	}

	public void setFascicolo(FascicoloVO fascicolo) {
		this.fascicolo = fascicolo;
	}

	public Collection<FascicoloVO> getFascicoli() {
		return fascicoli;
	}

	public void setFascicoli(Collection<FascicoloVO> fascicoli) {
		this.fascicoli = fascicoli;
	}

	public Collection<FascicoloVO> getFascicoliProtocollo() {
		return fascicoliProtocollo.values();
	}

	public void aggiungiFascicolo(FascicoloVO fascicolo) {
		if (fascicolo != null) {
			this.fascicoliProtocollo.put(fascicolo.getId(), fascicolo);
		}
	}

	public void rimuoviFascicolo(int fascicoloId) {
		this.fascicoliProtocollo.remove(new Integer(fascicoloId));
	}

	public String getDescrizioneStatoProtocollo() {
		if (!this.getStato().equals("N") && !this.getStato().equals("S"))
			return ProtocolloBO.getStatoProtocollo(this.getFlagTipo(), this
					.getStato());
		if (getVersione() <= 1)
			return "Registrato";
		if (getVersione() > 1)
			return "Modificato";
		return "non classificato";
	}

	public boolean getVersioneDefault() {
		return versioneDefault;
	}

	public void setVersioneDefault(boolean versioneDefault) {
		this.versioneDefault = versioneDefault;
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

	public String getProtocollatore() {
		Organizzazione org = Organizzazione.getInstance();
		Ufficio ufficio = org.getUfficio(getUfficioProtocollatoreId());
		CaricaVO carica = org.getCarica(getUtenteProtocollatoreId());
		String protocollatore = "";
		if (ufficio != null)
			protocollatore = ufficio.getValueObject().getDescription();
		if (carica != null)
			protocollatore += "/" + carica.getNome();
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
		documentiAllegati = new HashMap<String,DocumentoVO>(2);
		setDocumentoPrincipale(new DocumentoVO());

		setRiservato(false);
		setProtocolloId(0);
		setOggetto(null);
		setPosizioneAnnotazione(null);
		protocolliAllacciati = new HashMap<Integer,AllaccioVO>();

		fascicolo = new FascicoloVO();

		setModificabile(true);
		setFascicolo(new FascicoloVO());
		setFascicoli(null);
		setOggettoProcedimento(null);
		fascicoliProtocollo = new HashMap<Integer,FascicoloVO>();
		procedimentiProtocollo = new HashMap<Integer, ProtocolloProcedimentoVO> ();
		setProvvedimentoAnnullamento(null);
		setNotaAnnullamento(null);
		setNumProtocolloEmergenza(0);
		setVersione(0);

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
		documentiAllegati = new HashMap<String,DocumentoVO>();
		setDocumentoPrincipale(new DocumentoVO());
		setRiservato(false);
		setProtocolloId(0);
		setPosizioneAnnotazione(null);
		protocolliAllacciati = new HashMap<Integer,AllaccioVO>();
		setModificabile(true);
		setFascicolo(new FascicoloVO());
		setFascicoli(null);
		setOggettoProcedimento(null);
		procedimentiProtocollo = new HashMap<Integer, ProtocolloProcedimentoVO> ();
		setProvvedimentoAnnullamento(null);
		setNotaAnnullamento(null);
		setNumProtocolloEmergenza(0);
		setVersione(0);

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

	public Collection< ProtocolloProcedimentoVO>  getProcedimentiProtocollo() {
		return procedimentiProtocollo.values();
	}

	public void aggiungiProcedimento(ProtocolloProcedimentoVO procedimento) {
		this.procedimentiProtocollo.put(new Integer(procedimento
				.getProcedimentoId()), procedimento);
	}

	public void rimuoviProcedimento(int procedimentoId) {
		this.procedimentiProtocollo.remove(new Integer(procedimentoId));
	}

	public boolean isDocumentoVisibile() {
		return documentoVisibile;
	}

	public void setDocumentoVisibile(boolean documentoVisibile) {
		this.documentoVisibile = documentoVisibile;
	}

	public Collection<ProvinciaVO> getProvince() {
		return getLookupDelegateDelegate().getProvince();
	}

	public Collection<TipoDocumentoVO> getTipiDocumento() {
		return getLookupDelegateDelegate().getTipiDocumento(getAooId());
	}

	public Collection<IdentityVO> getTitoliDestinatario() {
		return getLookupDelegateDelegate().getTitoliDestinatario();
	}

	private LookupDelegate getLookupDelegateDelegate() {
		return LookupDelegate.getInstance();
	}

}