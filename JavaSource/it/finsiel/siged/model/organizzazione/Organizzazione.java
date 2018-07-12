package it.finsiel.siged.model.organizzazione;

import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.mvc.vo.organizzazione.AmministrazioneVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Organizzazione {
    private static Organizzazione organizzazione;

    private AmministrazioneVO valueObject;

    private Map<Integer,Menu> menuMap = new HashMap<Integer,Menu>();

    private Map<String,Menu> menuMapByLink = new HashMap<String,Menu>();

    private Map<Integer,AreaOrganizzativa> areeOrganizzative = new HashMap<Integer,AreaOrganizzativa>();

    private Map<Integer,Ufficio> uffici = new HashMap<Integer,Ufficio>();
    
    private Map<Integer,CaricaVO> cariche = new HashMap<Integer,CaricaVO>();

    private Map<Integer,Utente> utentiById = new HashMap<Integer,Utente>();

    private Map<Integer,Utente> utentiByAooId = new HashMap<Integer,Utente>();

    private Map<String,Utente> utentiByUsername = new HashMap<String,Utente>();

    private Map<Integer,RegistroVO> registri = new HashMap<Integer,RegistroVO>(4);

    private Map<String,Utente> utentiConnessi = new HashMap<String,Utente>(10);
    
    private CaricaVO caricaResponsabile = new CaricaVO();
    
    private CaricaVO caricaResponsabileUfficioProtocollo = new CaricaVO();

    
    private Organizzazione() {
    }

    public static Organizzazione getInstance() {
        if (organizzazione == null) {
            organizzazione = new Organizzazione();
        }
        return organizzazione;
    }

	public void setValueObject(AmministrazioneVO vo) {
        this.valueObject = vo;
    }

    public AmministrazioneVO getValueObject() {
        return valueObject;
    }

    
	public CaricaVO getCaricaResponsabile() {
		return caricaResponsabile;
	}

	public void setCaricaResponsabile(CaricaVO caricaResponsabile) {
		this.caricaResponsabile = caricaResponsabile;
	}
	
	public CaricaVO getCaricaResponsabileUfficioProtocollo() {
		return caricaResponsabileUfficioProtocollo;
	}

	public void setCaricaResponsabileUfficioProtocollo(
			CaricaVO caricaResponsabileUfficioProtocollo) {
		this.caricaResponsabileUfficioProtocollo = caricaResponsabileUfficioProtocollo;
	}

	public Collection<Menu> getMenuList() {
        return menuMap.values();
    }

    public Menu getMenu(Integer menuId) {
        return (Menu) menuMap.get(menuId);
    }

    public Menu getMenu(int menuId) {
        return getMenu(new Integer(menuId));
    }

    public Menu getMenu(String link) {
        return (Menu) menuMapByLink.get(link);
    }

    public void addMenu(Menu menu) {
        this.menuMap.put(menu.getValueObject().getId(), menu);
        String link = menu.getValueObject().getLink();
        if (link != null) {
            int i = link.indexOf('?');
            if (i >= 0) {
                link = link.substring(0, i);
            }
            this.menuMapByLink.put(link, menu);
        }
    }

    public Collection<AreaOrganizzativa> getAreeOrganizzative() {
        return areeOrganizzative.values();
    }

    public AreaOrganizzativa getAreaOrganizzativa(int areaId) {
        return (AreaOrganizzativa) areeOrganizzative.get(new Integer(areaId));
    }

    public void addAreaOrganizzativa(AreaOrganizzativa aoo) {
        if (aoo != null) {
        	 aoo.setAmministrazione(this);
        	areeOrganizzative.put(aoo.getValueObject().getId(), aoo);
        }
    }

    public void removeAreaOrganizzativa(Integer aooId) {
        if (areeOrganizzative.containsKey(aooId)) {
            areeOrganizzative.remove(aooId);
        }
    }

    
      public Utente getUtenteConnesso(String sessionId) { return (Utente)
    	  utentiConnessi.get(sessionId); }
     

    
    public int getNumeroUtentiConnessi() {
        return utentiConnessi.size();
    }

  
    public Collection<Utente> getUtentiConnessi() {
        return utentiConnessi.values();
    }

   

    public boolean existUtenteBySessionId(String id) {
        return utentiConnessi.containsKey(id);
    }

    public void removeSessionIdUtente(String id) {
        utentiConnessi.remove(id);
    }

 
    public void aggiungiUtenteConnesso(Utente u) {
        utentiConnessi.put(u.getSessionId(), u);
    }

    public void disconnettiUtente(Utente u) {
        if (u != null) {
            utentiConnessi.remove(u.getSessionId());
            u.setSessionId(null);
        }
    }

    public void addUfficio(Ufficio ufficio) {
        uffici.put(ufficio.getValueObject().getId(), ufficio);
    }

    public void removeUfficio(Integer ufficioId) {
        if (uffici.containsKey(ufficioId))
            uffici.remove(ufficioId);
    }

    public void addCarica(CaricaVO carica) {
        cariche.put(carica.getCaricaId(), carica);
    }

    public void removeCarica(Integer caricaId) {
        if (cariche.containsKey(caricaId))
        	cariche.remove(caricaId);
    }
    
    public void addUtente(Utente utente) {
        UtenteVO ute = utente.getValueObject();
        utentiById.put(ute.getId(), utente);
        utentiByUsername.put(ute.getUsername(), utente);
    }

    public void addUtenteByAooId(Utente utente) {
        UtenteVO ute = utente.getValueObject();
        utentiByAooId.put(ute.getId(), utente);
        
    }
    
    public void addRegistro(RegistroVO registro) {
        registri.put(registro.getId(), registro);
    }

    public void removeRegistro(Integer registroId) {
        registri.remove(registroId);
    }

    public Collection<Ufficio> getUffici() {
        return uffici.values();
    }

    public Ufficio getUfficio(int id) {
        return uffici.get(new Integer(id));
    }
    
    public void removeUfficioProtocollo() {
        for(Ufficio uff: uffici.values()){
        	uff.getValueObject().setUfficioProtocollo(false);
        }
        caricaResponsabileUfficioProtocollo=null;
    }

    public Collection<CaricaVO> getCariche() {
        return cariche.values();
    }

    public CaricaVO getCarica(int id) {
        return  cariche.get(id);
    }

    
    public Utente getUtente(int id) {
        return utentiById.get(new Integer(id));
    }

    public Utente getUtente(String username) {
        return  utentiByUsername.get(username);
    }

    public Collection<Utente> getUtentiById() {
        return utentiById.values();
    }

    public Collection<Utente> getUtentiAooById() {
        return utentiByAooId.values();
    }
    
    public RegistroVO getRegistro(Integer id) {
        return registri.get(id);
    }

    public RegistroVO getRegistroByCod(String cod) {
        RegistroVO reg=null;
        for(Object o:registri.values()){
        	RegistroVO r=(RegistroVO)o;
        	if(r.getCodRegistro().equals(cod))
        		reg=r;
        }
    	return reg;
    }
    
    public RegistroVO getRegistro(int id) {
        return getRegistro(new Integer(id));
    }

    public Collection<RegistroVO> getRegistri() {
        return registri.values();
    }

    public void resetOrganizzazione() {
        organizzazione.menuMap.clear();
        organizzazione.areeOrganizzative.clear();
        organizzazione.menuMapByLink.clear();
        organizzazione.registri.clear();
        organizzazione.uffici.clear();
        organizzazione.cariche.clear();
        organizzazione.utentiById.clear();
        organizzazione.utentiByAooId.clear();
        organizzazione.utentiByUsername.clear();
        organizzazione.utentiConnessi.clear();
    }
    
}