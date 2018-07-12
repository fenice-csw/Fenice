package it.finsiel.siged.mvc.presentation.actionform.protocollo;

import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public final class ProtocolloIngressoForm extends ProtocolloForm {
	
	private static final long serialVersionUID = 1L;

	private Map<String,AssegnatarioView> assegnatari; // di AssegnatarioView

	private Collection<AssegnatarioView> assegnatariPrecedenti; // di AssegnatarioView

	private String[] assegnatariSelezionatiId;

	private String[] mittentiSelezionatiId;

	private String assegnatarioCompetente;

	private String[] assegnatariCompetenti;

	private String dataProtocolloMittente;

	private String numProtocolloMittente;

	private Collection<ReportProtocolloView> protocolliMittente;

	private SoggettoVO mittente;

	private SoggettoVO multiMittenteCorrente;

	private List<SoggettoVO> mittenti;

	private List<SoggettoVO> storiaMittenti;

	private String msgAssegnatarioCompetente;

	private String returnPage;

	private boolean fattura;

	private String titolareProcedimento;
	
	private boolean titolareProcedimentoCompetente;

	private boolean inUfficioAssegnatario;
	
	private Integer caricaCruscottoId;
	
	private Integer ufficioCruscottoId;
	
	public boolean isInUfficioAssegnatario() {
		return inUfficioAssegnatario;
	}

	public void setInUfficioAssegnatario(boolean inUfficioAssegnatario) {
		this.inUfficioAssegnatario = inUfficioAssegnatario;
	}
	
	public Integer getCaricaCruscottoId() {
		return caricaCruscottoId;
	}

	public void setCaricaCruscottoId(Integer caricaCruscottoId) {
		this.caricaCruscottoId = caricaCruscottoId;
	}

	public Integer getUfficioCruscottoId() {
		return ufficioCruscottoId;
	}

	public void setUfficioCruscottoId(Integer ufficioCruscottoId) {
		this.ufficioCruscottoId = ufficioCruscottoId;
	}

	public boolean isFattura() {
		return fattura;
	}

	public void setFattura(boolean fattura) {
		this.fattura = fattura;
	}

	public String getReturnPage() {
		return returnPage;
	}

	public void setReturnPage(String retPage) {
		this.returnPage = retPage;
	}

	public ProtocolloIngressoForm() {
		super();
		inizializzaForm();
	}

	public List<SoggettoVO> getStoriaMittenti() {
		return storiaMittenti;
	}

	public String[] getMittentiSelezionatiId() {
		return mittentiSelezionatiId;
	}

	public boolean fisicaToAdd;

	public boolean isFisicaToAdd() {
		return fisicaToAdd;
	}

	public void setFisicaToAdd(boolean fisicaToAdd) {
		this.fisicaToAdd = fisicaToAdd;
	}

	public void setMittentiSelezionatiId(String[] mittenti) {
		this.mittentiSelezionatiId = mittenti;
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

	public String[] getAssegnatariCompetenti() {
		return assegnatariCompetenti;
	}

	public boolean isNotificaWritable(){
		if(getFatturaElettronica() && isDocumentFatturaElettronica() && isInUfficioAssegnatario())
			return true;
		return false;
	}
	
	public boolean isDocumentFatturaElettronica(){
		if(getDocumentoPrincipale().getFileName()!=null)
			if(!(getDocumentoPrincipale().getFileName().contains("_DT_") 
					|| getDocumentoPrincipale().getFileName().contains("_EC_") 
					|| getDocumentoPrincipale().getFileName().contains("_SE_")))
				return true;
		return false;
	}
	
	public void setAssegnatarioCompetente(String assegnatarioCompetente) {
		this.assegnatarioCompetente = assegnatarioCompetente;
		for (Iterator<AssegnatarioView> i = getAssegnatari().iterator(); i.hasNext();) {
			AssegnatarioView ass =i.next();
			if (ass.getKey().equals(assegnatarioCompetente)) {
				ass.setCompetente(ass.getKey().equals(assegnatarioCompetente));
			}
		}
	}

	public void setAssegnatariCompetenti(String[] assCompetenti) {
		this.assegnatariCompetenti = assCompetenti;
		for (Iterator<AssegnatarioView> i = getAssegnatari().iterator(); i.hasNext();) {
			AssegnatarioView ass =i.next();
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

	public void setTitolareProcedimento(String titolareProcedimento) {
		this.titolareProcedimento = titolareProcedimento;
		for (Iterator<AssegnatarioView> i = getAssegnatari().iterator(); i.hasNext();) {
			AssegnatarioView ass = i.next();
			if (titolareProcedimento.equals(ass.getKey())) {
				ass.setTitolareProcedimento(true);
				if(ass.isCompetente())
					setTitolareProcedimentoCompetente(true);
			} else
				ass.setTitolareProcedimento(false);
		}
	}

	public boolean isTitolareProcedimentoCompetente() {
		return titolareProcedimentoCompetente;
	}

	public void setTitolareProcedimentoCompetente(boolean tpc) {
		this.titolareProcedimentoCompetente = tpc;
	}

	public String getTitolareProcedimento() {
		return titolareProcedimento;
	}

	public String getDataProtocolloMittente() {
		return dataProtocolloMittente;
	}

	public void setDataProtocolloMittente(String dataProtocolloMittente) {
		this.dataProtocolloMittente = dataProtocolloMittente;
	}

	public SoggettoVO getMittente() {
		return mittente;
	}

	public void setMittente(SoggettoVO mittente) {
		this.mittente = mittente;
	}

	
	public String getNumProtocolloMittente() {
		return numProtocolloMittente;
	}

	public void setNumProtocolloMittente(String numProtocolloMittente) {
		this.numProtocolloMittente = numProtocolloMittente;
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

	public Collection<ReportProtocolloView>  getProtocolliMittente() {
		return protocolliMittente;
	}

	public int getNumeroProtocolliMittente() {
		return protocolliMittente.size();
	}

	public void setProtocolliMittente(Collection<ReportProtocolloView> protocolliMittente) {
		this.protocolliMittente = protocolliMittente;
	}

	public void inizializzaForm() {
		super.inizializzaForm();
		super.setFlagTipo("I");
		assegnatari = new HashMap<String,AssegnatarioView>();
		assegnatariPrecedenti = new ArrayList<AssegnatarioView>();
		mittenti = new ArrayList<SoggettoVO>();
		storiaMittenti = new ArrayList<SoggettoVO>();
		setAssegnatarioCompetente(null);
		setAssegnatariSelezionatiId(null);
		setDataProtocolloMittente(null);
		setMittente(new SoggettoVO('G'));
		setMultiMittenteCorrente(new SoggettoVO('G'));
		setNumProtocolloMittente(null);
		setProtocolliMittente(null);
		setMsgAssegnatarioCompetente(null);
		getElencoSezioni().add(0, new Sezione("Mittente", mittenti, true));
		getElencoSezioni().add(1, new Sezione("Assegnatari", assegnatari, true));
		setOggettoSelezionato(0);
		setInUfficioAssegnatario(false);
	}

	public void inizializzaRipetiForm() {
		super.inizializzaRipetiForm();
		super.setFlagTipo("I");
		getElencoSezioni().add(0, new Sezione("Mittente", mittenti, true));
		getElencoSezioni().add(1, new Sezione("Assegnatari", assegnatari, true));
		if ((getAssegnatarioCompetente() == null)
				&& (getAssegnatariSelezionatiId() == null)) {
			assegnatari = new HashMap<String,AssegnatarioView>();
		}
		setDataProtocolloMittente(null);
		setMittente(new SoggettoVO('G'));
		setNumProtocolloMittente(null);
		setProtocolliMittente(null);
		setMsgAssegnatarioCompetente(null);
		setInUfficioAssegnatario(false);
		
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = super.validate(mapping, request);

		if (isDipTitolarioUfficio()
				&& "Titolario".equals(getSezioneVisualizzata())) {
			if (getAssegnatari().size() == 0) {
				errors.add("mittenteDenominazione", new ActionMessage(
						"selezione.obbligatoria",
						"un assegnatario di competenza",
						"per la gestione del titolario"));
				setSezioneVisualizzata("Assegnatari");
			}

		} else if (request.getParameter("btnCercaProtMitt") != null) {
			if (getNumProtocolloMittente() == null
					|| "".equals(getNumProtocolloMittente())) {
				errors.add("numProtocolloMittente", new ActionMessage(
						"cerca_protocollo_mittente"));
			}

		} else if (request.getParameter("salvaAction") != null) {
			if (getFlagTipo().equals("R")) {
				if (getNumProtocolloEmergenza() <= 0)
					errors.add("numProtocolloEmergenza", new ActionMessage(
							"campo.maggiore.zero",
							"Numero protocollo d'emergenza"));
			}
			if (getProtocolloId() != 0 && getVersione() != 0) {
				if (!request.getParameter("salvaAction").equals("Fascicola") && !request.getParameter("salvaAction").equals("Aggiungi il Procedimento")) {
					if (getEstremiAutorizzazione() == null
							|| "".equals(getEstremiAutorizzazione().trim())) {
						errors.add("estremiAutorizzazione", new ActionMessage(
								"campo.obbligatorio", "Estremi autorizzazione",
								""));
					}
				} else if (request.getParameter("salvaAction").equals(
						"Fascicola")) {
					if (getFascicoliProtocollo().size() == 0
							&& getFascicoliProtocolloOld().size() == 0) {
						errors.add("fascicolo", new ActionMessage(
								"fascicolo_obbligatorio"));
					}
				}

			}

			if (getMittenti().size() == 0 || getMittente() == null) {
                errors.add("mittente", new ActionMessage(
                        "mittente_obbligatorio"));
            } 
			if (getAssegnatari().size() == 0) {
				errors.add("assegnatari", new ActionMessage(
						"assegnatari_obbligatorio"));
			}

			
			if (getAssegnatarioCompetente() == null
					|| "".equals(getAssegnatarioCompetente())) {
				errors.add("assegnatarioCompetente", new ActionMessage(
						"assegnatario_competente_obbligatorio"));
			}
		}
		return errors;
	}

	
	
	public void validateRiassegnazione(ActionMapping mapping,
			HttpServletRequest request, ActionMessages errors) {
		
		for(AssegnatarioView assegnatario:getAssegnatari()){
			if(getAssegnatariPrecedenti().contains(assegnatario))
				errors.add("assegnatari", new ActionMessage(
						"assegnatario_gia_presente",assegnatario.getNomeAssegnatario()));
		}
		
		if (getRiservato()) {
			if (getAssegnatari().size() == 1) {
				Iterator<AssegnatarioView> i = getAssegnatari().iterator();
				AssegnatarioView assegnatario =  i.next();
				if (assegnatario.getUtenteId() == 0) {
					errors.add("assegnatari", new ActionMessage(
							"assegnatario_non_utente"));
				}
			} else {
				errors.add("assegnatari", new ActionMessage(
						"assegnatari_da_rimuovere"));
			}
		}
		if (getAssegnatari().size() == 0) {
			errors.add("assegnatari", new ActionMessage(
					"assegnatari_obbligatorio"));
		} else if (getAssegnatarioCompetente() == null
				|| "".equals(getAssegnatarioCompetente())) {
			errors.add("assegnatarioCompetente", new ActionMessage(
					"assegnatario_competente_obbligatorio"));
		} 
		
		
		if(isModificabile() && isTitolareProcedimentoCompetente()){
			errors.add("assegnatarioCompetente", new ActionMessage(
			"assegnatario_titolare_obbligatorio_competente"));
		}
		
	}

	public String getMsgAssegnatarioCompetente() {
		return msgAssegnatarioCompetente;
	}

	public void setMsgAssegnatarioCompetente(String messaggio) {
		this.msgAssegnatarioCompetente = messaggio;
	}

	public List<SoggettoVO> getMittenti() {
		return mittenti;
	}

	public void setMittenti(List<SoggettoVO> mittenti) {
		this.mittenti = mittenti;
	}

	public SoggettoVO getMultiMittenteCorrente() {
		return multiMittenteCorrente;
	}

	public void setMultiMittenteCorrente(SoggettoVO multiMittenteCorrente) {
		this.multiMittenteCorrente = multiMittenteCorrente;
	}

	public void rimuoviMittente(int id) {
		SoggettoVO removed = (SoggettoVO) mittenti.get(id);
		mittenti.remove(String.valueOf(removed.getId()));
	}

	public void rimuoviDestinatari() {
		if (mittenti != null) {
			this.mittenti.clear();
		}
	}

	public static Map<Integer,SoggettoVO> mittentiCollectionToMap(Collection<SoggettoVO> coll) {
		Map<Integer,SoggettoVO> retval = new HashMap<Integer,SoggettoVO>(2);
		for (SoggettoVO elem : coll) {
			retval.put(elem.getId(), elem);
		}
		return retval;
	}

	public Collection<AssegnatarioView> getAssegnatariPrecedenti() {
		return assegnatariPrecedenti;
	}

	public void setAssegnatariPrecedenti(Collection<AssegnatarioView> assPrec) {
		assegnatariPrecedenti = assPrec;
	}

	public void aggiungiAssegnatarioPrecedente(AssegnatarioView ass) {
		assegnatariPrecedenti.add(ass);
	}

	public void rimuoviAssegnatarioPrecedente(AssegnatarioView ass) {
		assegnatariPrecedenti.removeAll(Collections.singleton(ass));
	}

	public void resetAssegnatariPrecedenti() {
		assegnatariPrecedenti.clear();
	}

}
