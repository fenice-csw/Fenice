package it.compit.fenice.mvc.presentation.actionform.documentale;

import it.compit.fenice.mvc.presentation.helper.EditorView;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.presentation.actionform.ParametriForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.AlberoUfficiUtentiForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.presentation.helper.DestinatarioView;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.SpedizioneVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class EditorForm extends ParametriForm implements AlberoUfficiUtentiForm{

	private static final long serialVersionUID = 1L;

	private int documentoId;

	private int caricaId;

	private String testo;

	private String oggetto;
	
	private String nomeFile;

	private Collection<EditorView> documenti;
	
	private int flagStato;

	private int versione;

	private boolean modificabile;

	private Map<String,DestinatarioView> destinatari = new HashMap<String,DestinatarioView>(2);

	private Map<String,Integer> destinatariIds = new HashMap<String,Integer>(2);

	private String tipoDestinatario = "F";

	private String destinatarioMezzoId;

	private int destinatarioId;

	private String[] destinatarioSelezionatoId;

	private String nominativoDestinatario;

	private String citta;

	private int mezzoSpedizione;

	private String emailDestinatario;

	private String indirizzoDestinatario;

	private String dataSpedizione;

	private boolean flagConoscenza;
	
	private boolean flagPresso;
	
	private boolean flagPEC;

	private String capDestinatario;

	private int idx;

	private int aooId;

	private String titoloDestinatario;

	private int mezzoSpedizioneId;

	private int titoloId;

	private String tipoProtocollo;

	public String cercaFascicoloNome;
	
	private Map<Integer,FascicoloVO> fascicoliProtocollo = new HashMap<Integer,FascicoloVO>();

	private String[] fascicoloSelezionatoId;
	
	private AllaccioVO allaccio = new AllaccioVO();
	
	private String allaccioProtocolloId;

	private String allaccioAnnoProtocollo;

	private String allaccioNumProtocollo;
	
	private int ufficioCorrenteId;

	private String ufficioCorrentePath;

	private int ufficioSelezionatoId;

	private int utenteSelezionatoId;

	private UfficioVO ufficioCorrente;

	private Collection<UfficioVO> ufficiDipendenti;

	private Collection<UtenteVO> utenti;

	private String tipo;
	
	private boolean dirigente;
	
	private String[] assegnatariCompetenti;
	
	private String assegnatarioCompetente;

	private Map<String,AssegnatarioView> assegnatari= new HashMap<String,AssegnatarioView>(); 
	
	private String[] assegnatariSelezionatiId;
	
	private boolean caricaDocumento;
	
	private String msgCarica;
	
	private  int procedimentoId;

	private String textScadenza;

	private String dataScadenza;

	private boolean ull;
	
	private boolean responsabileEnte;

	private int statoProcedimentoULL;

	private String procedimentoMsg;

	
	public String getProcedimentoMsg() {
		return procedimentoMsg;
	}

	public void setProcedimentoMsg(String procedimentoMsg) {
		this.procedimentoMsg = procedimentoMsg;
	}

	public int getStatoProcedimentoULL() {
		return statoProcedimentoULL;
	}

	public void setStatoProcedimentoULL(int statoProcedimentoULL) {
		this.statoProcedimentoULL = statoProcedimentoULL;
	}

	public boolean isResponsabileEnte() {
		return responsabileEnte;
	}

	public void setResponsabileEnte(boolean responsabileEnte) {
		this.responsabileEnte = responsabileEnte;
	}

	public boolean isUll() {
		return ull;
	}

	public void setUll(boolean ull) {
		this.ull = ull;
	}

	public String getTextScadenza() {
		return textScadenza;
	}

	public String getDataScadenza() {
		return dataScadenza;
	}

	public void setTextScadenza(String textScadenza) {
		this.textScadenza = textScadenza;
	}

	public void setDataScadenza(String dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public int getProcedimentoId() {
		return procedimentoId;
	}

	public void setProcedimentoId(int procedimentoId) {
		this.procedimentoId = procedimentoId;
	}

	public String getMsgCarica() {
		return msgCarica;
	}

	public void setMsgCarica(String msgCarica) {
		this.msgCarica = msgCarica;
	}

	public boolean isCaricaDocumento() {
		return caricaDocumento;
	}

	public void setCaricaDocumento(boolean caricaDocumento) {
		this.caricaDocumento = caricaDocumento;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public String getAssegnatarioCompetente() {
		return assegnatarioCompetente;
	}

	public String[] getAssegnatariCompetenti() {
		return assegnatariCompetenti;
	}

	public void setAssegnatarioCompetente(String assegnatarioCompetente) {
		this.assegnatarioCompetente = assegnatarioCompetente;
		for (Iterator<AssegnatarioView> i = getAssegnatari().iterator(); i.hasNext();) {
			AssegnatarioView ass = (AssegnatarioView) i.next();
			if (ass.getKey().equals(assegnatarioCompetente)) {
				ass.setCompetente(ass.getKey().equals(assegnatarioCompetente));
			}
		}
	}

	public void setAssegnatariCompetenti(String[] assCompetenti) {
		this.assegnatariCompetenti = assCompetenti;
		for (Iterator<AssegnatarioView> i = getAssegnatari().iterator(); i.hasNext();) {
			AssegnatarioView ass = (AssegnatarioView) i.next();
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
	
	public String[] getAssegnatariSelezionatiId() {
		return assegnatariSelezionatiId;
	}

	public void setAssegnatariSelezionatiId(String[] assegnatari) {
		this.assegnatariSelezionatiId = assegnatari;
	}

	public void removeAssegnatari() {
		if (assegnatari != null)
			assegnatari.clear();
	}

	public Collection<AssegnatarioView> getAssegnatari() {
		return assegnatari.values();
	}

	public void aggiungiAssegnatario(AssegnatarioView ass) {
		
		assegnatari.put(ass.getKey(), ass);
	}

	public void rimuoviAssegnatario(String key) {
		assegnatari.remove(key);
	}
	
	public Collection<UfficioVO> getUfficiDipendenti() {
		return ufficiDipendenti;
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
	
	public int getUtenteSelezionatoId() {
		return utenteSelezionatoId;
	}

	public void setUtenteSelezionatoId(int utenteCorrenteId) {
		this.utenteSelezionatoId = utenteCorrenteId;
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

	public boolean isDirigente() {
		return dirigente;
	}

	public void setDirigente(boolean dirigente) {
		this.dirigente = dirigente;
	}
	
	public String getOggetto() {
		return oggetto;
	}

	public AllaccioVO getAllaccio() {
		return allaccio;
	}

	public void setAllaccio(AllaccioVO allaccio) {
		this.allaccio = allaccio;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public String getAllaccioNumProtocollo() {
		return allaccioNumProtocollo;
	}

	public void setAllaccioNumProtocollo(String allaccioNumProtocollo) {
		this.allaccioNumProtocollo = allaccioNumProtocollo;
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

	public String[] getFascicoloSelezionatoId() {
		return fascicoloSelezionatoId;
	}

	public void setFascicoloSelezionatoId(String[] fascicoloSelezionatoId) {
		this.fascicoloSelezionatoId = fascicoloSelezionatoId;
	}

	public void aggiungiFascicolo(FascicoloVO fascicolo) {

		if (fascicolo != null) {

			if (fascicolo.getId() != null) {
				this.fascicoliProtocollo.put(fascicolo.getId(), fascicolo);

			}
		}

	}

	public void rimuoviFascicolo(int fascicoloId) {
		Integer key = new Integer(fascicoloId);
		this.fascicoliProtocollo.remove(key);
	}

	public Collection<FascicoloVO> getFascicoliProtocollo() {
		return fascicoliProtocollo.values();
	}

	public String getCercaFascicoloNome() {
		return cercaFascicoloNome;
	}

	public void setCercaFascicoloNome(String cercaFascicoloNome) {
		this.cercaFascicoloNome = cercaFascicoloNome;
	}

	public boolean isModificabile() {
		return modificabile;
	}

	public void setModificabile(boolean modificabile) {
		this.modificabile = modificabile;
	}

	public int getVersione() {
		return versione;
	}

	public void setVersione(int versione) {
		this.versione = versione;
	}

	public String getTipoProtocollo() {
		return tipoProtocollo;
	}

	public void setTipoProtocollo(String tipoProtocollo) {
		this.tipoProtocollo = tipoProtocollo;
	}

	public int getDestinatarioId() {
		return destinatarioId;
	}

	public void setDestinatarioId(int destinatarioId) {
		this.destinatarioId = destinatarioId;
	}

	public int getAooId() {
		return aooId;
	}

	public void setAooId(int aooId) {
		this.aooId = aooId;
	}

	public boolean getFlagConoscenza() {
		return flagConoscenza;
	}

	
	
	public boolean getFlagPresso() {
		return flagPresso;
	}

	public boolean getFlagPEC() {
		return flagPEC;
	}

	public void setFlagPresso(boolean flagPresso) {
		this.flagPresso = flagPresso;
	}

	public void setFlagPEC(boolean flagPEC) {
		this.flagPEC = flagPEC;
	}

	public Collection<SpedizioneVO> getMezziSpedizione() {
		return LookupDelegate.getInstance().getMezziSpedizione(getAooId());
	}

	public int getTitoloId() {
		return titoloId;
	}

	public void setTitoloId(int titoloId) {
		this.titoloId = titoloId;
	}

	public ActionErrors validateDestinatario(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		if (nominativoDestinatario == null
				|| "".equals(nominativoDestinatario.trim())) {
			errors.add("nominativoDestinatario", new ActionMessage(
					"destinatario_nome_obbligatorio"));
		} else if (!"".equals(dataSpedizione)
				&& !DateUtil.isData(dataSpedizione)) {
			errors.add("dataSpedizione", new ActionMessage(
					"formato.data.errato", "data spedizione", ""));
		} else if (!"".equals(dataSpedizione) && (getMezzoSpedizioneId() == 0)) {
			errors.add("dataSpedizione", new ActionMessage(
					"selezione.obbligatoria", "il mezzo spedizione",
					"in presenza della data spedizione"));
		} else if (getMezzoSpedizioneId() != 0
				&& (getDataSpedizione() == null || ""
						.equals(getDataSpedizione().trim()))) {
			errors.add("dataSpedizione", new ActionMessage(
					"selezione.obbligatoria", "la data spedizione",
					"in presenza del mezzo spedizione"));
		}
		return errors;
	}

	public int getIdx() {
		return idx;
	}

	public String getTitoloDestinatario() {
		return titoloDestinatario;
	}

	public int getMezzoSpedizioneId() {
		return mezzoSpedizioneId;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public void setTitoloDestinatario(String titoloDestinatario) {
		this.titoloDestinatario = titoloDestinatario;
	}

	public void setMezzoSpedizioneId(int mezzoSpedizioneId) {
		this.mezzoSpedizioneId = mezzoSpedizioneId;
	}

	private static int getNextId(Map<String,Integer> m) {
		int max = 0;
		Iterator<String> it = m.keySet().iterator();
		while (it.hasNext()) {
			String id = (String) it.next();
			int cur = NumberUtil.getInt(id);
			if (cur > max)
				max = cur;
		}
		return max + 1;
	}

	public void inizializzaDestinatarioForm() {
		setDataSpedizione(null);
		setEmailDestinatario(null);
		setCitta(null);
		setIndirizzoDestinatario(null);
		setCapDestinatario(null);
		setFlagConoscenza(false);
		setFlagPresso(false);
		setFlagPEC(false);
		setMezzoSpedizione(0);
		setNominativoDestinatario(null);
		setTipoDestinatario("F");
		setIdx(0);
		setMezzoSpedizioneId(0);
		setTitoloDestinatario(null);
	}

	public void rimuoviDestinatario(String id) {
		DestinatarioView removed = (DestinatarioView) destinatari.get(id);
		int idx = removed.getIdx();
		destinatari.remove(String.valueOf(removed.getIdx()));
		destinatariIds.remove(String.valueOf(idx));
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

	public void setDestinatari(Map<String,DestinatarioView> destinatari) {
		this.destinatari = destinatari;
	}

	public void rimuoviDestinatari() {

		if (destinatari != null) {
			this.destinatari.clear();
			this.destinatariIds.clear();
		}
	}

	public void rimuoviFascicoli() {

		if (fascicoliProtocollo != null) {
			this.fascicoliProtocollo.clear();
			this.fascicoloSelezionatoId = null;
		}
	}

	public String getTipoDestinatario() {
		return tipoDestinatario;
	}

	public String getDestinatarioMezzoId() {
		return destinatarioMezzoId;
	}

	public String[] getDestinatarioSelezionatoId() {
		return destinatarioSelezionatoId;
	}

	public String getNominativoDestinatario() {
		return nominativoDestinatario;
	}

	public String getCitta() {
		return citta;
	}

	public int getMezzoSpedizione() {
		return mezzoSpedizione;
	}

	public String getEmailDestinatario() {
		return emailDestinatario;
	}

	public String getIndirizzoDestinatario() {
		return indirizzoDestinatario;
	}

	public String getDataSpedizione() {
		return dataSpedizione;
	}

	public boolean isFlagConoscenza() {
		return flagConoscenza;
	}

	public String getCapDestinatario() {
		return capDestinatario;
	}

	public Collection<DestinatarioView> getDestinatari() {
		return destinatari.values();
	}

	public DestinatarioView getDestinatario(String nomeDestinatario) {
		return (DestinatarioView) destinatari.get(nomeDestinatario);
	}

	public void setTipoDestinatario(String tipoDestinatario) {
		this.tipoDestinatario = tipoDestinatario;
	}

	public void setDestinatarioMezzoId(String destinatarioMezzoId) {
		this.destinatarioMezzoId = destinatarioMezzoId;
	}

	public void setDestinatarioSelezionatoId(String[] destinatarioSelezionatoId) {
		this.destinatarioSelezionatoId = destinatarioSelezionatoId;
	}

	public void setNominativoDestinatario(String nominativoDestinatario) {
		this.nominativoDestinatario = nominativoDestinatario;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public void setMezzoSpedizione(int mezzoSpedizione) {
		this.mezzoSpedizione = mezzoSpedizione;
	}

	public void setEmailDestinatario(String emailDestinatario) {
		this.emailDestinatario = emailDestinatario;
	}

	public void setIndirizzoDestinatario(String indirizzoDestinatario) {
		this.indirizzoDestinatario = indirizzoDestinatario;
	}

	public void setDataSpedizione(String dataSpedizione) {
		this.dataSpedizione = dataSpedizione;
	}

	public void setFlagConoscenza(boolean flagConoscenza) {
		this.flagConoscenza = flagConoscenza;
	}

	public void setCapDestinatario(String capDestinatario) {
		this.capDestinatario = capDestinatario;
	}

	public int getFlagStato() {
		return flagStato;
	}

	public void setFlagStato(int flagStato) {
		this.flagStato = flagStato;
	}

	public int getCaricaId() {
		return caricaId;
	}

	public Collection<EditorView> getDocumenti() {
		return documenti;
	}

	public void setCaricaId(int caricaId) {
		this.caricaId = caricaId;
	}

	public void setDocumenti(Collection<EditorView> documenti) {
		this.documenti = documenti;
	}

	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public int getDocumentoId() {
		return documentoId;
	}

	public String getTesto() {
		return testo;
	}

	public void setDocumentoId(int documentoId) {
		this.documentoId = documentoId;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public void inizializzaForm(int caricaId) {
		this.documentoId = 0;
		this.caricaId = caricaId;
		this.testo = "";
		this.nomeFile = "";
	}
	
	public void inizializzaForm() {
		this.documentoId = 0;
		this.testo = "";
		this.nomeFile = "";
		this.fascicoliProtocollo=new HashMap<Integer,FascicoloVO>();
		this.allaccio=new AllaccioVO();
		this.destinatari=new HashMap<String,DestinatarioView>();
		this.assegnatari=new HashMap<String,AssegnatarioView>();
		this.oggetto="";
	}
	
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		
			if (getNomeFile() == null || getNomeFile().trim().equals("")) {
				errors.add("nomeFile", new ActionMessage("campo.obbligatorio",
						"Nome File", ""));
			}
			if (getTesto() == null || getTesto().trim().equals("")) {
				errors.add("txt", new ActionMessage("campo.obbligatorio",
						"Testo", ""));
			}
		return errors;
	}

	public ActionErrors validateStampa(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		
			if (getOggetto() == null || getOggetto().trim().equals("")) {
				errors.add("oggetto", new ActionMessage("campo.obbligatorio",
						"Oggetto", ""));
			}
			if (getTesto() == null || getTesto().trim().equals("")) {
				errors.add("txt", new ActionMessage("campo.obbligatorio",
						"Testo", ""));
			}
			if(getDestinatari()==null || getDestinatari().size()==0)
				 errors.add("destinatari", new ActionMessage("destinatari_obbligatorio"));
		return errors;
	}
	
	public ActionErrors validateFirma(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		
			if (getOggetto() == null || getOggetto().trim().equals("")) {
				errors.add("oggetto", new ActionMessage("campo.obbligatorio",
						"Oggetto", ""));
			}
			if (getTesto() == null || getTesto().trim().equals("")) {
				errors.add("txt", new ActionMessage("campo.obbligatorio",
						"Testo", ""));
			}
		return errors;
	}
	
	public ActionErrors validateAllaccio(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		
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
		return errors;
	}
	
	public ActionErrors validateDestinatari(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		if (request.getParameter("btnInvioProtocollo") != null) {
			if (destinatari.isEmpty()) {
				errors
						.add("destinatari", new ActionMessage(
								"selezione.obbligatoria",
								"almeno un destinatario", ""));
			}

		} else if (request.getParameter("aggiungiDestinatario") != null) {
			if (getNominativoDestinatario() == null
					|| "".equals(getNominativoDestinatario().trim())) {
				errors.add("destinatari", new ActionMessage(
						"selezione.obbligatoria", "almeno un destinatario",
						"utilizzando la funzione Aggiungi"));
			}
		}
		return errors;
	}

	public String getStato() {
		if (flagStato == 0)
			return "in lavorazione";
		if (flagStato == 1)
			return "Protocollato";
		if (flagStato == 2)
			return "Inviato al Protocollo";
		if (flagStato == 3)
			return "Inviato al altro utente";
		else
			return " ";
	}

	public String getIntestatario() {
		Organizzazione org = Organizzazione.getInstance();
		String responsabile = "";
		if (getCaricaId() != 0) {
			CaricaVO carica = org.getCarica(getCaricaId());
			if (carica != null) {
				Ufficio uff = org.getUfficio(carica.getUfficioId());
				if (uff != null)
					responsabile = uff.getPath();
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

	public void resetAllaccio() {
		this.allaccio=new AllaccioVO();
		
	}

	public void setUfficiDipendenti(Collection<UfficioVO> ufficiDipendenti) {
		this.ufficiDipendenti=ufficiDipendenti;
		
	}

	public void setUtenti(Collection<UtenteVO> utenti) {
		this.utenti= utenti;		
	}


}