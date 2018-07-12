package it.finsiel.siged.mvc.presentation.actionform.protocollo;

import it.compit.fenice.mvc.bo.TipoProcedimentoBO;
import it.compit.fenice.mvc.presentation.helper.ProtocolloProcedimentoView;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.compit.fenice.mvc.vo.protocollo.UfficioPartecipanteVO;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.presentation.actionform.UploaderForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.presentation.helper.FascicoloView;
import it.finsiel.siged.mvc.vo.IdentityVO;
import it.finsiel.siged.mvc.vo.lookup.TipoProcedimentoVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.FaldoneVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class ProcedimentoForm extends UploaderForm implements
AlberoUfficiUtentiForm{

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(ProcedimentoForm.class.getName());

	private String dataAvvio;

	private int procedimentoId;

	private int ufficioId;

	private int statoId;
	
	private int statoPrecedenteId;

	private Map<String,IdentityVO> statiProcedimento = new HashMap<String,IdentityVO>(2);

	private int tipoFinalitaId;

	private Map<String,IdentityVO> tipiFinalita;

	private String oggettoProcedimento;

	private int tipoProcedimentoId;

	private TipoProcedimentoVO tipoProcedimento;

	private ArrayList<TipoProcedimentoVO> tipiProcedimento;

	private ArrayList<TipoProcedimentoVO> tipiProcedimentoByUfficio;

	private int referenteId;

	private Map<String,UtenteVO> referenti = new HashMap<String,UtenteVO> (2);

	private String[] istruttoriSelezionatiId = null;

	private Map<String,AssegnatarioView> istruttori = new HashMap<String,AssegnatarioView>(2);

	private int utenteSelezionatoId;

	private String nomeReferente;

	private int ufficioResponsabileId;

	private int caricaResponsabileId;

	private String posizione;

	private Map<String,IdentityVO> posizioniProcedimento = new HashMap<String,IdentityVO>(2);

	private String dataEvidenza;

	private String note;

	private int protocolloId;

	private String numeroProtocollo;

	private String numeroProcedimento;

	private int anno;

	private int numero;

	private int aooId;

	private int ufficioCorrenteId;

	private String ufficioCorrentePath;

	private int ufficioSelezionatoId;

	private UfficioVO ufficioCorrente;

	private Collection<UfficioVO> ufficiDipendenti;

	private Collection<UtenteVO> utenti;

	private boolean fromStoria;

	private int versione;

	private String giorniAlert;

	private String giorniMax;

	private Map<String,DocumentoVO> riferimentiLegislativi = new HashMap<String,DocumentoVO>(2);

	private String[] riferimentiLegislativiId;


	// ------- allacci ----------- //
	private Map<String,ProtocolloProcedimentoView> protocolli = new HashMap<String,ProtocolloProcedimentoView>(2);

	private Map<String,FascicoloView> fascicoli = new HashMap<String,FascicoloView>(2);

	private Map<String,FaldoneVO> faldoni = new HashMap<String,FaldoneVO>(2);

	private String[] fascicoliSelezionati = null;

	private String[] faldoniSelezionati = null;

	private String[] protocolliSelezionati = null;

	private int progressivo;

	private int fascicoloId;

	private Date dataRegistrazioneProtocollo;

	private String interessato;

	private String delegato;

	private String indiInteressato;

	private String indiDelegato;

	private String autoritaEmanante;

	private String indicazioni;

	private String estremiProvvedimento;

	private boolean modificabile;

	// -------- campi di ricerca --------- //

	private String cercaFascicoloNome = null;

	private String cercaFaldoneOggetto = null;

	private String cercaProtocolloOggetto = null;

	private boolean indietroVisibile;

	private boolean sospeso;

	private String dataSospensione;
	
	private String dataScadenza;

	private String estremiSospensione;
	
	private String allaccioAnnoProtocollo;

	private String allaccioNumProtocollo;
	
	private AllaccioVO protocolloAllacciato;
	
	private ProtocolloProcedimentoView protocolloSospensione;

	//private Map<String,AssegnatarioView> assegnatari;

	private AssegnatarioView assegnatarioPrincipale;
	
	private String[] visibilitaUfficiPartecipantiId = null;

	private Map<String,UfficioPartecipanteVO> ufficiPartecipanti = new HashMap<String,UfficioPartecipanteVO>(2);


	public String[] getVisibilitaUfficiPartecipantiId() {
		return visibilitaUfficiPartecipantiId;
	}

	public void setVisibilitaUfficiPartecipantiId(
			String[] visibilitaUfficiPartecipantiId) {
		this.visibilitaUfficiPartecipantiId = visibilitaUfficiPartecipantiId;
		for (UfficioPartecipanteVO uff : getUfficiPartecipanti().values()) {
			uff.setVisibilita(this.isDefaultVisibilita(uff));
		}
	}

	private boolean isDefaultVisibilita(UfficioPartecipanteVO uff) {
		if (this.visibilitaUfficiPartecipantiId == null && this.ufficiPartecipanti.size() == 1)
			return true;
		for (String visibilitaUff : this.visibilitaUfficiPartecipantiId) {
			if (visibilitaUff.equals(String.valueOf(uff.getUfficioId()))) {
				return true;
			}
		}
		return false;
	}

	public Collection<UfficioPartecipanteVO> getUfficiPartecipantiValues() {
		return ufficiPartecipanti.values();
	}
	
	public Map<String, UfficioPartecipanteVO> getUfficiPartecipanti() {
		return ufficiPartecipanti;
	}

	public void setUfficiPartecipanti(
			Map<String, UfficioPartecipanteVO> ufficiPartecipanti) {
		this.ufficiPartecipanti = ufficiPartecipanti;
	}

	public int getStatoPrecedenteId() {
		return statoPrecedenteId;
	}

	public void setStatoPrecedenteId(int statoPrecedenteId) {
		this.statoPrecedenteId = statoPrecedenteId;
	}

	public AssegnatarioView getAssegnatarioPrincipale() {
		return assegnatarioPrincipale;
	}

	public void setAssegnatarioPrincipale(AssegnatarioView assegnatarioPrincipale) {
		this.assegnatarioPrincipale = assegnatarioPrincipale;
	}

	/*
	public void removeAssegnatari() {
		if (assegnatari != null)
			assegnatari.clear();
	}

	public Collection<AssegnatarioView> getAssegnatari() {
		return assegnatari.values();
	}

	public void aggiungiAssegnatario(AssegnatarioView ass) {
		if(!assegnatari.containsKey(ass.getKey()))
			assegnatari.put(ass.getKey(), ass);
	}
	
	public void rimuoviAssegnatario(String key) {
		assegnatari.remove(key);
	}
	*/
	
	public ProtocolloProcedimentoView getProtocolloSospensione() {
		return protocolloSospensione;
	}

	public String getProtocolloSospensioneDesc() {
		if(protocolloSospensione!=null)
			return protocolloSospensione.getAnnoNumeroProtocollo();
		else
			return null;
	}
	
	public void setProtocolloSospensione( Collection<ProtocolloProcedimentoView> protocolli) {
		for(Object o:protocolli){
			ProtocolloProcedimentoView p=(ProtocolloProcedimentoView)o;
			if(p.getSospeso()==1)
				this.protocolloSospensione = p;
			
		}
	}

	public void setProtocolloSospensione(
			ProtocolloProcedimentoView p) {
				this.protocolloSospensione = p;
	}
	
	public void resetProtocolloSospensione() {
				this.protocolloSospensione = null;
	}
	
	public AllaccioVO getProtocolloAllacciato() {
		return protocolloAllacciato;
	}

	public void setProtocolloAllacciato(AllaccioVO protocolloAllacciato) {
		this.protocolloAllacciato = protocolloAllacciato;
	}


	public String getAllaccioAnnoProtocollo() {
		return allaccioAnnoProtocollo;
	}

	public String getAllaccioNumProtocollo() {
		return allaccioNumProtocollo;
	}

	public void setAllaccioAnnoProtocollo(String allaccioAnnoProtocollo) {
		this.allaccioAnnoProtocollo = allaccioAnnoProtocollo;
	}

	public void setAllaccioNumProtocollo(String allaccioNumProtocollo) {
		this.allaccioNumProtocollo = allaccioNumProtocollo;
	}

	public String getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(String dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public String getEstremiSospensione() {
		return estremiSospensione;
	}

	public void setEstremiSospensione(String estremiSospensione) {
		this.estremiSospensione = estremiSospensione;
	}

	public boolean isSospeso() {
		return sospeso;
	}

	public String getDataSospensione() {
		return dataSospensione;
	}

	public void setSospeso(boolean sospeso) {
		this.sospeso = sospeso;
	}

	public void setDataSospensione(String dataSospensione) {
		this.dataSospensione = dataSospensione;
	}

	public boolean isModificabile() {
		return modificabile;
	}

	public void setModificabile(boolean modificabile) {
		this.modificabile = modificabile;
	}

	public String getEstremiProvvedimento() {
		return estremiProvvedimento;
	}

	public void setEstremiProvvedimento(String estremiProvvedimento) {
		this.estremiProvvedimento = estremiProvvedimento;
	}

	public String getIndicazioni() {
		return indicazioni;
	}

	public void setIndicazioni(String indicazioni) {
		this.indicazioni = indicazioni;
	}
	
	public String getInteressato() {
		return interessato;
	}

	public void setInteressato(String interessato) {
		this.interessato = interessato;
	}

	public String getDelegato() {
		return delegato;
	}

	public void setDelegato(String delegato) {
		this.delegato = delegato;
	}

	public String getIndiInteressato() {
		return indiInteressato;
	}

	public void setIndiInteressato(String indiInteressato) {
		this.indiInteressato = indiInteressato;
	}

	public String getIndiDelegato() {
		return indiDelegato;
	}

	public void setIndiDelegato(String indiDelegato) {
		this.indiDelegato = indiDelegato;
	}

	public String getAutoritaEmanante() {
		return autoritaEmanante;
	}

	public void setAutoritaEmanante(String autoritaEmanante) {
		this.autoritaEmanante = autoritaEmanante;
	}

	public Date getDataRegistrazioneProtocollo() {
		if (dataRegistrazioneProtocollo != null)
			return DateUtil.convertDate(dataRegistrazioneProtocollo);
		else
			return DateUtil.toDate(dataAvvio);
	}

	public void setDataRegistrazioneProtocollo(Date dataRegistrazioneProtocollo) {
		this.dataRegistrazioneProtocollo = dataRegistrazioneProtocollo;
	}

	public int getFascicoloId() {
		return fascicoloId;
	}

	public void setFascicoloId(int fascicoloId) {
		this.fascicoloId = fascicoloId;
	}

	public Collection<DocumentoVO> getRiferimentiCollection() {
		return riferimentiLegislativi.values();
	}

	public void setRiferimentiLegislativi(Map<String,DocumentoVO> documenti) {
		this.riferimentiLegislativi = documenti;
	}

	public Map<String,DocumentoVO> getRiferimentiLegislativi() {
		return riferimentiLegislativi;
	}

	public void allegaRiferimento(DocumentoVO doc) {
		TipoProcedimentoBO.putAllegato(doc, this.riferimentiLegislativi);
	}

	public void rimuoviRiferimento(String allegatoId) {
		riferimentiLegislativi.remove(allegatoId);
	}

	public DocumentoVO getRiferimento(Object key) {
		return (DocumentoVO) this.riferimentiLegislativi.get(key);
	}

	public DocumentoVO getRiferimento(int idx) {
		return (DocumentoVO) this.riferimentiLegislativi.get(String
				.valueOf(idx));
	}

	public String[] getRiferimentiLegislativiId() {
		return riferimentiLegislativiId;
	}

	public void setRiferimentiLegislativiId(String[] allegatiSelezionatoId) {
		this.riferimentiLegislativiId = allegatiSelezionatoId;
	}

	public String getGiorniAlert() {
		return giorniAlert;
	}

	public void setGiorniAlert(String giorniAlert) {
		this.giorniAlert = giorniAlert;
	}

	public String getGiorniMax() {
		return giorniMax;
	}

	public void setGiorniMax(String giorniMax) {
		this.giorniMax = giorniMax;
	}

	public String[] getIstruttoriSelezionatiId() {
		return istruttoriSelezionatiId;
	}

	public void setIstruttoriSelezionatiId(String[] istruttori) {
		this.istruttoriSelezionatiId = istruttori;
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

	public int getUtenteSelezionatoId() {
		return utenteSelezionatoId;
	}

	public void setUtenteSelezionatoId(int utenteSelezionatoId) {
		this.utenteSelezionatoId = utenteSelezionatoId;
	}

	public void aggiungiIstruttore(AssegnatarioView istr) {
		istruttori.put(istr.getKey(), istr);
	}

	public void removeIstruttori() {
		if (istruttori != null)
			istruttori.clear();
	}

	public Collection<AssegnatarioView> getIstruttori() {
		return istruttori.values();
	}

	public void rimuoviIstruttore(String key) {
		istruttori.remove(key);
	}

	public int getUfficioResponsabileId() {
		return ufficioResponsabileId;
	}

	public int getCaricaResponsabileId() {
		return caricaResponsabileId;
	}

	public void setUfficioResponsabileId(int ufficioResponsabileId) {
		this.ufficioResponsabileId = ufficioResponsabileId;
	}

	public void setCaricaResponsabileId(int caricaResponsabileId) {
		this.caricaResponsabileId = caricaResponsabileId;
	}

	public String getNote() {
		return note;
	}

	public int getAooId() {
		return aooId;
	}

	public void setAooId(int aooId) {
		this.aooId = aooId;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getOggettoProcedimento() {
		return oggettoProcedimento;
	}

	public void setOggettoProcedimento(String oggetto) {
		this.oggettoProcedimento = oggetto;
	}

	public int getReferenteId() {
		return referenteId;
	}

	public void setReferenteId(int responsabileId) {
		this.referenteId = responsabileId;
	}

	public int getUfficioId() {
		return ufficioId;
	}

	public void setUfficioId(int ufficioId) {
		this.ufficioId = ufficioId;
	}

	public Collection<UtenteVO> getUtenti() {
		return utenti;
	}

	public void setUtenti(Collection<UtenteVO> utenti) {
		this.utenti = utenti;
	}

	public UfficioVO getUfficioCorrente() {
		return ufficioCorrente;
	}

	public void setUfficioCorrente(UfficioVO ufficioCorrente) {
		this.ufficioCorrente = ufficioCorrente;
	}

	public int getUfficioCorrenteId() {
		return ufficioCorrenteId;
	}

	public void setUfficioCorrenteId(int ufficioCorrenteId) {
		this.ufficioCorrenteId = ufficioCorrenteId;
	}

	public String getUfficioCorrentePath() {
		return ufficioCorrentePath;
	}

	public void setUfficioCorrentePath(String ufficioCorrentePath) {
		this.ufficioCorrentePath = ufficioCorrentePath;
	}

	public int getAnno() {
		return anno;
	}

	public void setAnno(int anno) {
		this.anno = anno;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getNumeroProcedimento() {
		return numeroProcedimento;
	}

	public void setNumeroProcedimento(String numeroProcedimento) {
		this.numeroProcedimento = numeroProcedimento;
	}

	public int getProcedimentoId() {
		return procedimentoId;
	}

	public void setProcedimentoId(int procedimentoId) {
		this.procedimentoId = procedimentoId;
	}

	public int getProtocolloId() {
		return protocolloId;
	}

	public void setProtocolloId(int protocolloId) {
		this.protocolloId = protocolloId;
	}

	public String getResponsabile() {
		Organizzazione org = Organizzazione.getInstance();
		String responsabile = "";
		if (getUfficioResponsabileId() != 0) {
			Ufficio uff = org.getUfficio(getUfficioResponsabileId());
			responsabile = uff.getPath();

			if (getCaricaResponsabileId() != 0) {
				CaricaVO carica = org.getCarica(getCaricaResponsabileId());
				Utente ute = org.getUtente(carica.getUtenteId());
				if (ute == null)
					return uff.getPath() + carica.getNome();
				ute.getValueObject().setCarica(carica.getNome());
				if (carica.isAttivo())
					responsabile = uff.getPath()
							+ ute.getValueObject().getCaricaFullName();
				else
					responsabile = uff.getPath()
							+ ute.getValueObject().getCaricaFullNameNonAttivo();
			}
		}
		return responsabile;
	}

	public int getStatoId() {
		return statoId;
	}

	public void setStatoId(int statoId) {
		this.statoId = statoId;
	}

	public int getTipoFinalitaId() {
		return tipoFinalitaId;
	}

	public void setTipoFinalitaId(int tipoFinalitaId) {
		this.tipoFinalitaId = tipoFinalitaId;
	}

	public int getTipoProcedimentoId() {
		return tipoProcedimentoId;
	}

	public void setTipoProcedimentoId(int tipoProcedimentoId) {
		this.tipoProcedimentoId = tipoProcedimentoId;
	}

	public void setDataAvvio(String dataAvvio) {
		this.dataAvvio = dataAvvio;
	}

	public void setDataEvidenza(String dataEvidenza) {
		this.dataEvidenza = dataEvidenza;
	}

	public void setPosizione(String posizione) {
		this.posizione = posizione;
	}

	public String getDataAvvio() {
		return dataAvvio;
	}

	public String getDataEvidenza() {
		return dataEvidenza;
	}

	public String getPosizione() {
		return posizione;
	}

	public String getNomeReferente() {
		return nomeReferente;
	}

	public void setNomeReferente(String nomeResponsabile) {
		this.nomeReferente = nomeResponsabile;
	}

	public Map<String,IdentityVO> getStatiProcedimento() {
		return statiProcedimento;
	}

	public Map<String,IdentityVO> getStatiProcedimentoULL() {
		Map<String,IdentityVO> statiProcedimentoULL = new HashMap<String,IdentityVO>(2);
		if (procedimentoId == 0)
			statiProcedimentoULL.put("3", new IdentityVO(3,
					"vai Fase Istruttoria"));
		if (procedimentoId != 0) {
			
			if (statoId == 0)
				statiProcedimentoULL.put("3", new IdentityVO(3,
						"vai alla Fase Istruttoria"));
			
			if (statoId == 3) {
				statiProcedimentoULL.put("3", new IdentityVO(3,
						"continua la Fase Istruttoria"));
				statiProcedimentoULL.put("4", new IdentityVO(4,
						"chiudi la Fase Istruttoria"));
			}
			if (statoId == 4) {
				statiProcedimentoULL.put("3", new IdentityVO(3,
						"torna alla Fase Istruttoria"));
				statiProcedimentoULL.put("4", new IdentityVO(4,
						"continua la Fase Relatoria"));
				statiProcedimentoULL.put("5", new IdentityVO(5,
						"chiudi la Fase Relatoria"));
			}
			if (statoId == 5) {
				statiProcedimentoULL.put("3", new IdentityVO(3,
						"torna alla Fase Istruttoria"));
				statiProcedimentoULL.put("5", new IdentityVO(5,
						"attendi il Parere Consiglio"));
				statiProcedimentoULL.put("6", new IdentityVO(6,
						"Prepara il Decreto Presidente"));
			}
			if (statoId == 6) {
				statiProcedimentoULL.put("6", new IdentityVO(6,
						"Prepara il Decreto Presidente"));
			}
			if (statoId == 7) {
				statiProcedimentoULL.put("7", new IdentityVO(6,
						"Attendi il Decreto Presidente"));
			}
		}
		return statiProcedimentoULL;
	}

	public Collection<IdentityVO> getStatiProcedimentoCollection() {
		return statiProcedimento.values();
	}

	public void setStatiProcedimento(Map<String,IdentityVO> statiProcedimento) {
		this.statiProcedimento = statiProcedimento;
	}

	public Map<String,IdentityVO> getTipiFinalita() {
		return tipiFinalita;
	}

	public Collection<IdentityVO> getTipiFinalitaCollection() {
		return tipiFinalita.values();
	}

	public void setTipiFinalita(Map<String,IdentityVO> tipiFinalita) {
		this.tipiFinalita = tipiFinalita;
	}

	public void setTipiProcedimento(ArrayList<TipoProcedimentoVO> tipiProcedimento) {
		this.tipiProcedimento = tipiProcedimento;
	}

	public ArrayList<TipoProcedimentoVO> getTipiProcedimento() {
		return tipiProcedimento;
	}

	public String getNumeroProtocollo() {
		return numeroProtocollo;
	}

	public void setNumeroProtocollo(String numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}

	public Map<String,IdentityVO> getPosizioniProcedimento() {
		return posizioniProcedimento;
	}

	public void setPosizioniProcedimento(Map<String,IdentityVO> posizioniProcedimento) {
		this.posizioniProcedimento = posizioniProcedimento;
	}

	public Collection<IdentityVO> getPosizioniProcedimentoCollection() {
		return posizioniProcedimento.values();
	}

	public Map<String,FaldoneVO> getFaldoni() {
		return faldoni;
	}

	public Collection<FaldoneVO> getFaldoniCollection() {
		return faldoni.values();
	}

	public void setFaldoni(Map<String,FaldoneVO> faldoni) {
		this.faldoni = faldoni;
	}

	public Map<String,FascicoloView> getFascicoli() {
		return fascicoli;
	}

	public Collection<FascicoloView> getFascicoliCollection() {
		return fascicoli.values();
	}

	public void setFascicoli(Map<String,FascicoloView> fascicoli) {
		this.fascicoli = fascicoli;
	}

	public Map<String,ProtocolloProcedimentoView> getProtocolli() {
		return protocolli;
	}

	public Collection<ProtocolloProcedimentoView> getProtocolliCollection() {
		return protocolli.values();
	}

	public void setProtocolli(Map<String,ProtocolloProcedimentoView> protocolli) {
		this.protocolli = protocolli;
	}

	public String[] getFaldoniSelezionati() {
		return faldoniSelezionati;
	}

	public void setFaldoniSelezionati(String[] faldoniSelezionati) {
		this.faldoniSelezionati = faldoniSelezionati;
	}

	public String[] getFascicoliSelezionati() {
		return fascicoliSelezionati;
	}

	public void setFascicoliSelezionati(String[] fascicoliSelezionati) {
		this.fascicoliSelezionati = fascicoliSelezionati;
	}

	public String[] getProtocolliSelezionati() {
		return protocolliSelezionati;
	}

	public void setProtocolliSelezionati(String[] protocolliSelezionati) {
		this.protocolliSelezionati = protocolliSelezionati;
	}

	public void rimuoviFascicolo(String id) {
		this.fascicoli.remove(id);
	}

	public void aggiungiFascicolo(FascicoloView f) {
		this.fascicoli.put(String.valueOf(f.getId()), f);
	}

	public void rimuoviFaldone(String id) {
		this.faldoni.remove(id);
	}

	public void aggiungiFaldone(FaldoneVO f) {
		this.faldoni.put(String.valueOf(f.getId()), f);
	}

	public void rimuoviProtocollo(String id) {
		this.protocolli.remove(id);
	}

	public void aggiungiProtocollo(ProtocolloProcedimentoView p) {
		this.protocolli.put(String.valueOf(p.getProtocolloId()), p);
	}

	public void aggiungiReferente(UtenteVO u) {
		this.referenti.put(String.valueOf(u.getId().intValue()), u);

	}

	public String getCercaFascicoloNome() {
		return cercaFascicoloNome;
	}

	public void setCercaFascicoloNome(String carcaFascicoloNome) {
		this.cercaFascicoloNome = carcaFascicoloNome;
	}

	public String getCercaFaldoneOggetto() {
		return cercaFaldoneOggetto;
	}

	public void setCercaFaldoneOggetto(String cercaFaldoneOggetto) {
		this.cercaFaldoneOggetto = cercaFaldoneOggetto;
	}

	public String getCercaProtocolloOggetto() {
		return cercaProtocolloOggetto;
	}

	public void setCercaProtocolloOggetto(String cercaProtocolloOggetto) {
		this.cercaProtocolloOggetto = cercaProtocolloOggetto;
	}

	public Map<String,UtenteVO>  getReferenti() {
		return referenti;
	}

	public Collection<UtenteVO> getReferentiCollection() {
		return referenti.values();
	}

	public void setReferenti(Map<String,UtenteVO> referenti) {
		this.referenti = referenti;
	}

	public void inizializzaForm() {
		setAnno(0);
		setDataAvvio(null);
		setDataEvidenza(null);
		setNote(null);
		setNumero(0);
		setNumeroProcedimento(null);
		setOggettoProcedimento(null);
		setPosizione("T");
		setProcedimentoId(0);
		setProtocolloId(0);
		setReferenteId(0);
		setStatoId(0);
		setTipoFinalitaId(0);
		setTipoProcedimentoId(0);
		setUfficioCorrente(null);
		setUfficioCorrenteId(0);
		setUfficioCorrentePath(null);
		setUfficioId(0);
		setUtenti(null);
		setFaldoni(new HashMap<String,FaldoneVO>(2));
		setFaldoniSelezionati(null);
		setFascicoli(new HashMap<String,FascicoloView>(2));
		setFaldoniSelezionati(null);
		setProtocolli(new HashMap<String,ProtocolloProcedimentoView>(2));
		setProtocolliSelezionati(null);
		setVersione(0);
		//assegnatari = new HashMap<String,AssegnatarioView>();
		setDelegato(null);
		setInteressato(null);
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		if (getTipoProcedimentoId() == 0) {
			errors.add("tipoProcedimento", new ActionMessage(
					"campo.obbligatorio", "Tipo Procedimento", ""));
		}
		if (getDataAvvio() == null || "".equals(getDataAvvio())) {
			errors.add("dataAvvio", new ActionMessage("campo.obbligatorio",
					"Data Produzione Effetti", ""));
		} else {
			if (!DateUtil.isData(getDataAvvio())) {
				errors.add("dataAvvio", new ActionMessage(
						"formato.data.errato", "Data Avvio"));
			} else if (DateUtil.toDate(getDataAvvio()).before(
					getDataRegistrazioneProtocollo())) {
				errors.add("dataAvvio", new ActionMessage(
						"data_avvio.minore.data_registrazione", DateUtil
								.formattaData(getDataRegistrazioneProtocollo()
										.getTime())));
			}
		}
		if (!(getUfficioCorrenteId() > 0)) {
			errors.add("ufficio", new ActionMessage("campo.obbligatorio",
					"Ufficio", ""));
		}
		if (!(getReferenteId() > 0)) {
			errors.add("referente", new ActionMessage("campo.obbligatorio",
					"Referente", ""));
		}
		if (getOggettoProcedimento() == null
				|| getOggettoProcedimento().equals("")) {
			errors.add("oggetto", new ActionMessage("campo.obbligatorio",
					"Oggetto", ""));
		}
		if (request.getParameter("sospensioneAction") != null
				|| request.getParameter("annullaSospensioneAction") != null
				|| request.getParameter("riavviaProcedimentoAction") != null) {
			if (getEstremiSospensione() == null
					|| getEstremiSospensione().equals("")) {
				errors.add("estremi", new ActionMessage("campo.obbligatorio",
						"Estremi", ""));
			}
		}
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

		}
		return errors;
	}

	public ActionErrors validateRicorsoULL(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		if (getTipoProcedimentoId() == 0) {
			errors.add("tipoProcedimento", new ActionMessage(
					"campo.obbligatorio", "Tipo Procedimento", ""));
		}
		if (getDataAvvio() == null || "".equals(getDataAvvio())) {
			errors.add("dataAvvio", new ActionMessage("campo.obbligatorio",
					"Data Produzione Effetti", ""));
		} else {
			if (!DateUtil.isData(getDataAvvio())) {
				errors.add("dataAvvio", new ActionMessage(
						"formato.data.errato", "Data Avvio"));
			} else if (DateUtil.toDate(getDataAvvio()).before(
					getDataRegistrazioneProtocollo())) {
				errors.add("dataAvvio", new ActionMessage(
						"data_avvio.minore.data_registrazione", DateUtil
								.formattaData(getDataRegistrazioneProtocollo()
										.getTime())));
			}
		}
		if (!(getUfficioCorrenteId() > 0)) {
			errors.add("ufficio", new ActionMessage("campo.obbligatorio",
					"Ufficio", ""));
		}
		if (!(getReferenteId() > 0)) {
			errors.add("referente", new ActionMessage("campo.obbligatorio",
					"Funzionario", ""));
		}
		if (getOggettoProcedimento() == null
				|| getOggettoProcedimento().equals("")) {
			errors.add("oggetto", new ActionMessage("campo.obbligatorio",
					"Oggetto", ""));
		}
		if (getIstruttori().size() == 0)
			errors.add("istruttori", new ActionMessage(
					"istruttori_obbligatorio"));
		if (procedimentoId == 0) {
			if (getIndicazioni() == null || getIndicazioni().equals(""))
				errors.add("indicazioni", new ActionMessage(
						"campo.obbligatorio", "Indicazioni Lavoro", ""));
		}
		if (procedimentoId != 0) {
			if (statoId == 2) {
				if (getEstremiProvvedimento() == null
						|| getEstremiProvvedimento().equals(""))
					errors.add("estremiProvvedimento", new ActionMessage(
							"campo.obbligatorio", "Estremi Provvedimento", ""));
				if (getNote() == null || getNote().equals(""))
					errors.add("note", new ActionMessage("campo.obbligatorio",
							"Note", ""));
				if (getDelegato() == null)
					errors.add("delegato", new ActionMessage(
							"campo.obbligatorio", "Legale Ricorrente", ""));
				if (getInteressato() == null)
					errors.add("interessato", new ActionMessage(
							"campo.obbligatorio", "Ricorrente", ""));
				if (getAutoritaEmanante() == null)
					errors.add("autoritaEmanante", new ActionMessage(
							"campo.obbligatorio", "Autorita Emanante", ""));
			}
		}
		return errors;
	}

	public ArrayList<TipoProcedimentoVO> getTipiProcedimentoByUfficio() {
		return tipiProcedimentoByUfficio;
	}

	public void setTipiProcedimentoByUfficio(ArrayList<TipoProcedimentoVO> tipiProcedimentoByUfficio) {
		this.tipiProcedimentoByUfficio = tipiProcedimentoByUfficio;
	}

	public int getVersione() {
		return versione;
	}

	public void setVersione(int versione) {
		this.versione = versione;
	}

	public int getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(int progressivo) {
		this.progressivo = progressivo;
	}

	public boolean isFromStoria() {
		return fromStoria;
	}

	public void setFromStoria(boolean fromStoria) {
		this.fromStoria = fromStoria;
	}

	public TipoProcedimentoVO getTipoProcedimento() {
		return tipoProcedimento;
	}

	public TipoProcedimentoVO getTipoProcedimentoFromArray(int id) {
		if (tipiProcedimento.isEmpty())
			return null;
		else
			return (TipoProcedimentoVO) tipiProcedimento.get(id);
	}

	public void setTipoProcedimento(TipoProcedimentoVO tipoProcedimento) {
		this.tipoProcedimento = tipoProcedimento;
	}

	public boolean isIndietroVisibile() {
		return indietroVisibile;
	}

	public void setIndietroVisibile(boolean indietroVisibile) {
		this.indietroVisibile = indietroVisibile;
	}

	public int getUfficioSelezionatoId() {
		return ufficioSelezionatoId;
	}

	public void setUfficioSelezionatoId(int ufficioSelezionatoId) {
		this.ufficioSelezionatoId = ufficioSelezionatoId;
	}

	public Collection<UfficioVO> getUfficiDipendenti() {
		return ufficiDipendenti;
	}

	public void setUfficiDipendenti(Collection<UfficioVO> ufficiDipendenti) {
		this.ufficiDipendenti = ufficiDipendenti;
	}
	
	

}