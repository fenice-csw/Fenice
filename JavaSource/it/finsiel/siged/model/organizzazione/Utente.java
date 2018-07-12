package it.finsiel.siged.model.organizzazione;

import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.business.RegistroDelegate;
import it.finsiel.siged.mvc.vo.organizzazione.AreaOrganizzativaVO;
import it.finsiel.siged.mvc.vo.organizzazione.MailConfigVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public final class Utente implements Comparable<Utente>{
	
	private UtenteVO valueObject;

	private Map<Integer,RegistroVO> registri = new HashMap<Integer,RegistroVO>(2);

	private int registroUfficialeId;

	private Map<Integer,UfficioVO> uffici = new HashMap<Integer,UfficioVO>(2);

	private Map<Integer,CaricaVO> cariche = new HashMap<Integer,CaricaVO>(2);

	private Map<Integer,CaricaVO> caricheUfficio = new HashMap<Integer,CaricaVO>(2);

	private String sessionId;

	private int ufficioInUso;

	private int registroInUso;

	private int registroPostaInterna;

	private int numPrt;

	private int caricaInUso;
	
	private boolean utenteResponsabileConnesso=false;
	
	public boolean isUtenteResponsabileConnesso() {
		return utenteResponsabileConnesso;
	}

	public void setUtenteResponsabileConnesso(boolean utenteResponsabileConnesso) {
		this.utenteResponsabileConnesso = utenteResponsabileConnesso;
	}
	
	public void addCaricaUfficio(CaricaVO caricaUfficio ) {
		caricheUfficio.put(caricaUfficio.getUfficioId(),caricaUfficio);
    }
	
	public Collection<CaricaVO> getCaricheUfficioCollection() {
		return caricheUfficio.values();
	}

	public Map<Integer,CaricaVO> getCaricheUfficio() {
		return caricheUfficio;
	}

	public CaricaVO getCaricaUfficioVO(int ufficioId) {
		return (CaricaVO) getCaricheUfficio().get(new Integer(ufficioId));
	}
	
	public void setCaricheUfficio(Map<Integer,CaricaVO> cariche) {
		this.caricheUfficio = cariche;
	}
	
	public Collection<CaricaVO> getCaricheCollection() {
		return cariche.values();
	}

	public Map<Integer,CaricaVO> getCariche() {
		return cariche;
	}

	public CaricaVO getCaricaVO(int id) {
		return (CaricaVO) getCariche().get(new Integer(id));
	}
	
	public void setCariche(Map<Integer,CaricaVO> cariche) {
		this.cariche = cariche;
	}

	public CaricaVO getCaricaVOInUso() {
		return (CaricaVO) getCariche().get(new Integer(getCaricaInUso()));
	}

	public int getCaricaInUso() {
		return caricaInUso;
	}
	
	public void setCaricaInUso(int caricaInUso) {
		this.caricaInUso = caricaInUso;
	}

	
	public Utente(UtenteVO vo) {
		this.valueObject = vo;
	}

	public int getNumPrt() {
		return numPrt;
	}

	public void setNumPrt(int numPrt) {
		this.numPrt = numPrt;
	}

	public UtenteVO getValueObject() {
		return valueObject;
	}

	public void setValueObject(UtenteVO valueObject) {
		this.valueObject = valueObject;
	}

	public AreaOrganizzativaVO getAreaOrganizzativa() {
		int aooId = getValueObject().getAooId();
		Organizzazione org = Organizzazione.getInstance();
		AreaOrganizzativaVO area=org.getAreaOrganizzativa(aooId).getValueObject();
		return area;
	}
	
	public MailConfigVO getMailConfig() {
		int aooId = getValueObject().getAooId();
		Organizzazione org = Organizzazione.getInstance();
		MailConfigVO mailConfig=org.getAreaOrganizzativa(aooId).getMailConfig();
		return mailConfig;
	}

	
	public int getRegistroUfficialeId() {
		return registroUfficialeId;
	}

	
	public void setRegistroUfficialeId(int registroUfficialeId) {
		this.registroUfficialeId = registroUfficialeId;
	}

	
	public Map<Integer,RegistroVO> getRegistri() {
		return registri;
	}

	public void setRegistri(Map<Integer,RegistroVO> registri) {
		this.registri = registri;
	}

	public RegistroVO getRegistroUfficiale() {
		if (registri.containsKey(new Integer(getRegistroUfficialeId())))
			return (RegistroVO) registri.get(new Integer(
					getRegistroUfficialeId()));
		else
			return null;
	}

	public Collection<RegistroVO> getRegistriCollection() {
		return registri.values();
	}

	public int getRegistroInUso() {
		return registroInUso;
	}

	public RegistroVO getRegistroVOInUso() {
		return (RegistroVO) getRegistri().get(new Integer(getRegistroInUso()));
	}

	public void setRegistroInUso(int registroInUso) {
		this.registroInUso = registroInUso;
	}

	public int getUfficioInUso() {
		return ufficioInUso;
	}

	public UfficioVO getUfficioVOInUso() {
		return (UfficioVO) getUffici().get(new Integer(getUfficioInUso()));
	}

	public void setUfficioInUso(int ufficioInUso) {
		this.ufficioInUso = ufficioInUso;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Map<Integer,UfficioVO> getUffici() {
		return uffici;
	}

	public String getUfficiIds() {
		String ids = String.valueOf(getUfficioInUso());
		for (Iterator<UfficioVO> i = getUffici().values().iterator(); i.hasNext();) {
			UfficioVO uff = (UfficioVO) i.next();
			if (uff.getId().intValue() != getUfficioInUso())
				ids += ',' + uff.getId().toString();
		}
		return ids;
	}

	public void setUffici(HashMap<Integer,UfficioVO> uffici) {
		this.uffici = uffici;
	}

	public Collection<UfficioVO> getUfficiCollection() {
		return uffici.values();
	}

	public String getUltimoProtocollo() {
		String numero = null;
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		RegistroVO registro = (RegistroVO) getRegistri().get(
				new Integer(getRegistroInUso()));
		if (registro != null) {
			int anno = registro.getAnnoCorrente();
			numero = delegate.getUltimoProtocollo(anno, registro.getId()
					.intValue())
					+ "/" + anno;
		}
		return numero;
	}

	public boolean isAbilitatoModifica(int ufficioId) {
		return (getUfficiAttivi().containsKey(new Integer(ufficioId)) && RegistroDelegate
				.getInstance().isAbilitatoRegistro(getRegistroInUso(),
						ufficioId, getValueObject().getId().intValue()));
	}

	public boolean isUtenteAbilitatoSuUfficio(int ufficioId) {
			return ((getUfficioVOInUso().getTipo()
				.equals(UfficioVO.UFFICIO_CENTRALE)) || (getUfficiAttivi()
				.containsKey(new Integer(ufficioId)) && RegistroDelegate
				.getInstance().isAbilitatoRegistro(getRegistroInUso(),
						ufficioId, getValueObject().getId().intValue())));
						
	}
	
	public int getRegistroPostaInterna() {
		return registroPostaInterna;
	}

	public void setRegistroPostaInterna(int registroPostaInterna) {
		this.registroPostaInterna = registroPostaInterna;
	}

	public Map<Integer,UfficioVO> getUfficiAttivi() {
		Map<Integer,UfficioVO> ufficiAttivi = new HashMap<Integer,UfficioVO>(2);
		Collection<CaricaVO> list = cariche.values();
		for (CaricaVO c : list)
			if (c.isAttivo())
				ufficiAttivi.put(c.getUfficioId(), uffici.get(c.getUfficioId()));
		return ufficiAttivi;
	}
	
	public Collection<UfficioVO> getUfficiAttiviCollection() {
		return getUfficiAttivi().values();
	}
	
	public int compareTo(Utente u) {
		return this.getValueObject().compareTo(u.getValueObject());
	}
}