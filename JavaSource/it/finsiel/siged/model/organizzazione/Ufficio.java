package it.finsiel.siged.model.organizzazione;

import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Ufficio {
    private UfficioVO valueObject;

    private Ufficio ufficioDiAppartenenza;

    private Map<Integer,Ufficio> ufficiDipendenti = new HashMap<Integer,Ufficio>(5);

    private Map<Integer,Utente> utenti = new HashMap<Integer,Utente>();

    
    private Map<Integer,CaricaVO> caricheReferenti=new HashMap<Integer,CaricaVO>();

	public Ufficio(UfficioVO vo) {
        this.setValueObject(vo);
    }

    public void setValueObject(UfficioVO vo) {
        this.valueObject = vo;
    }

    public UfficioVO getValueObject() {
        return valueObject;
    }

    public Collection<Ufficio> getUfficiDipendenti() {
        return ufficiDipendenti.values();
    }

    public Ufficio getUfficioDipendente(int ufficioId) {
        return (Ufficio) ufficiDipendenti.get(new Integer(ufficioId));
    }

    public Ufficio getUfficioDiAppartenenza() {
        return ufficioDiAppartenenza;
    }

    public void setUfficioDiAppartenenza(Ufficio ufficioDiAppartenenza) {
        this.ufficioDiAppartenenza = ufficioDiAppartenenza;
        if (ufficioDiAppartenenza != null) {
            ufficioDiAppartenenza.ufficiDipendenti.put(
                    getValueObject().getId(), this);
        }
    }

    public void removeUfficioDipendente(Integer ufficioId) {
        if (ufficiDipendenti.containsKey(ufficioId))
            ufficiDipendenti.remove(ufficioId);
    }

    public void addUfficioDipendente(Ufficio uff) {
            ufficiDipendenti.put(uff.getValueObject().getId(), uff);
    }
    
    public Map<Integer,Utente> getUtentiMap() {
		return utenti;
	}
    
    public Collection<Utente> getUtenti() {    	
        return getUtentiMap().values();
    }

    
    
    public Utente getUtente(int utenteId) {
        return (Utente) utenti.get(new Integer(utenteId));
    }

    public void addUtente(Utente utente ) {
    	utenti.put(utente.getValueObject().getId(), utente);
    }
    
    public Collection<CaricaVO> getCaricheReferenti() {
        return caricheReferenti.values();
    }

    public CaricaVO getCaricaReferente(int caricaId) {
        return (CaricaVO) caricheReferenti.get(new Integer(caricaId));
    }

    public boolean isCaricaReferente(int caricaId) {
    	return caricheReferenti.containsKey(new Integer(caricaId));
    }
    
    public void addCaricaReferente(CaricaVO carica) {
    	caricheReferenti.put(carica.getCaricaId(), carica);
    }
    
    public List<Integer> getListaUfficiDiscendentiId() {
    	List<Integer> ids=new ArrayList<Integer>();
    	ids.add(getValueObject().getId());
        for (Iterator<Ufficio> i = getUfficiDipendenti().iterator(); i.hasNext();) {
            Ufficio uff = (Ufficio) i.next();
            ids.addAll(uff.getListaUfficiDiscendentiId());
        }
        return ids;
    }
    
    public String getListaUfficiDiscendentiIdStr() {
        String ids = getValueObject().getId().toString();
        for (Iterator<Ufficio> i = getUfficiDipendenti().iterator(); i.hasNext();) {
            Ufficio uff = (Ufficio) i.next();
            ids += ',' + uff.getListaUfficiDiscendentiIdStr();
        }
        return ids;
    }
    
    public String getListaUfficiDiscendentiIdStr(Utente utente) {
        String ids = getValueObject().getId().toString();
        Organizzazione org = Organizzazione.getInstance();
        for (Iterator<UfficioVO> x = utente.getUffici().values().iterator(); x.hasNext();) {
            Ufficio uffPadre = org.getUfficio(((UfficioVO) x.next()).getId()
                    .intValue());
            ids += ',' + String.valueOf(uffPadre.getValueObject().getId());
            for (Iterator<Ufficio> i = uffPadre.getUfficiDipendenti().iterator(); i
                    .hasNext();) {
                Ufficio uff = (Ufficio) i.next();
                ids += ',' + uff.getListaUfficiDiscendentiIdStr();
            }
        }
        return ids;
    }

 
    public String getListaUfficiPadriId() {
        String ids = getValueObject().getId().toString();
        Ufficio uff = getUfficioDiAppartenenza();
        while (uff != null) {
            ids += ',' + uff.getValueObject().getId().toString();
            uff = uff.getUfficioDiAppartenenza();
        }
        return ids;
    }

    public String getPath() {
        Ufficio uff = this;
        StringBuffer path = new StringBuffer();
        while (uff != null) {
            path.insert(0, '/').insert(0, uff.getValueObject().getDescription());
            uff = uff.getUfficioDiAppartenenza();
        }
       
        return path.toString();
    }

    public String toString() {
        return valueObject.toString();
    }

    public void removeReferenti() {
        if (caricheReferenti != null)
        	caricheReferenti.clear();

    }
   
}