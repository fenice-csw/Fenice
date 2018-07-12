package it.compit.fenice.mvc.presentation.actionform.amministrazione.org.utenti;

import it.compit.fenice.mvc.bo.UtenteBO;
import it.compit.fenice.mvc.presentation.helper.VersioneCaricaView;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.mvc.vo.lookup.ProfiloVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;

public final class CaricaForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(CaricaForm.class.getName());

	private int caricaId;

	private String nome;

	private int utenteId;

	private int utenteSelezionatoId;

	private UfficioVO ufficioCorrente;

	private int profiloId;

	private int profiloSelezionatoId;

	private int ufficioId;

	private Map<Integer,ProfiloVO> profili = new HashMap<Integer,ProfiloVO>();

	private Map<Integer,UtenteVO> utenti = new HashMap<Integer,UtenteVO>();

	private boolean attivo;

	private int versione;

	private Collection<VersioneCaricaView> versioniCarica;

	private String dataOperazione;

	private String autore;

	private boolean referente;
	
	private boolean responsabileEnte;
	
	private boolean responsabileUfficioProtocollo;
	
	public String getResponsabileUfficioProtocollo() {
		if (responsabileUfficioProtocollo)
			return "SI";
		else
			return "NO";
	}
	
	public boolean isResponsabileUfficioProtocollo() {
		return responsabileUfficioProtocollo;
	}

	public void setResponsabileUfficioProtocollo(
			boolean responsabileUfficioProtocollo) {
		this.responsabileUfficioProtocollo = responsabileUfficioProtocollo;
	}

	public String getNomeUfficio() {
		if (ufficioId != 0)
			return Organizzazione.getInstance().getUfficio(ufficioId)
					.getValueObject().getDescription();
		else
			return "";
	}

	public String getPathUfficio() {
		if (ufficioId != 0)
			return Organizzazione.getInstance().getUfficio(ufficioId).getPath();
		else
			return "";
	}
	
	public String getReferente() {
		if (referente)
			return "SI";
		else
			return "NO";
	}

	public void setReferente(boolean referente) {
		this.referente = referente;
	}

	public String getResponsabileEnte() {
		if (responsabileEnte)
			return "SI";
		else
			return "NO";
	}

	public boolean isResponsabileEnte() {
		return responsabileEnte;
		
	}
	
	public void setResponsabileEnte(boolean responsabileEnte) {
		this.responsabileEnte = responsabileEnte;
	}
	
	public String getDataOperazione() {
		return dataOperazione;
	}

	public String getAutore() {
		return autore;
	}

	public void setDataOperazione(String dataOperazione) {
		this.dataOperazione = dataOperazione;
	}

	public void setAutore(String autore) {
		this.autore = autore;
	}

	public int getVersione() {
		return versione;
	}

	public Collection<VersioneCaricaView> getVersioniCarica() {
		return versioniCarica;
	}

	public void setVersione(int versione) {
		this.versione = versione;
	}

	public void setVersioniCarica(Collection<VersioneCaricaView> versioniCarica) {
		this.versioniCarica = versioniCarica;
	}

	public boolean isAttivo() {
		return attivo;
	}

	public void setAttivo(boolean attivo) {
		this.attivo = attivo;
	}

	public UfficioVO getUfficioCorrente() {
		return ufficioCorrente;
	}

	public void setUfficioCorrente(UfficioVO ufficioCorrente) {
		this.ufficioCorrente = ufficioCorrente;
	}

	public ProfiloVO getProfilo() {
		if (this.profiloSelezionatoId != 0)
			return (ProfiloVO) this.profili.get(this.profiloSelezionatoId);
		else
			return null;
	}

	public void setProfilo(ProfiloVO profilo) {
		
	}

	public int getProfiloSelezionatoId() {
		return profiloSelezionatoId;
	}

	public void setProfiloSelezionatoId(int profiloSelezionatoId) {
		this.profiloSelezionatoId = profiloSelezionatoId;
	}

	public Map<Integer,UtenteVO> getUtenti() {
		return utenti;
	}

	public Collection<UtenteVO> getUtentiCollection() {
		return UtenteBO.getUtentiOrdinati(getUtenti().values());
	}

	public void setUtenti(Collection<UtenteVO> utenti) {
		for (UtenteVO u : utenti)
			this.utenti.put(u.getId(), u);
	}

	public Collection<ProfiloVO> getProfiliCollection() {
		return profili.values();
	}

	public Map<Integer,ProfiloVO> getProfili() {
		return profili;
	}

	public void setProfili(Collection<ProfiloVO> profili) {
		for (ProfiloVO p : profili)
			this.profili.put(p.getId(), p);
	}

	public int getCaricaId() {
		return caricaId;
	}

	public void setCaricaId(int caricaId) {
		this.caricaId = caricaId;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getUtenteId() {
		return utenteId;
	}

	public void setUtenteId(int utenteCorrenteId) {
		this.utenteId = utenteCorrenteId;
	}

	public int getUtenteSelezionatoId() {
		return utenteSelezionatoId;
	}

	public void setUtenteSelezionatoId(int utenteSelezionatoId) {
		this.utenteSelezionatoId = utenteSelezionatoId;
	}

	public UtenteVO getUtente() {
		if (this.utenteSelezionatoId != 0)
			return (UtenteVO) this.utenti.get(this.utenteSelezionatoId);
		else
			return null;
	}

	public void setUtente(UtenteVO utenteCorrente) {
		
	}

	public int getProfiloId() {
		return profiloId;
	}

	public void setProfiloId(int profiloId) {
		this.profiloId = profiloId;
	}

	public int getUfficioId() {
		return ufficioId;
	}

	public void setUfficioId(int ufficioId) {
		this.ufficioId = ufficioId;
	}

	public void inizializzaForm() {
		setNome(null);
		setAttivo(true);
		setCaricaId(0);
		setProfiloId(0);
		setUfficioId(0);
		setUtenteId(0);
		setUtenteSelezionatoId(0);
		setProfiloSelezionatoId(0);
	}

}
