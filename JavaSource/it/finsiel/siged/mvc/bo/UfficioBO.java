package it.finsiel.siged.mvc.bo;

import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class UfficioBO {

    public static UfficioVO getUnicoUfficio(Map<Integer,UfficioVO> uffici) {
        Iterator<UfficioVO> i = uffici.values().iterator();
        if (i.hasNext())
            return  i.next();
        return null;
    }

    public static boolean controllaPermessoUfficio(int ufficioId, Utente utente) {
        return (utente.getUfficioVOInUso() != null 
        		&& utente.getUfficioVOInUso().getTipo().equals(UfficioVO.UFFICIO_CENTRALE))
        		|| utente.getUffici().get(new Integer(ufficioId)) != null;
    }

    public static Collection<UfficioVO> getUfficiOrdinati(Collection<UfficioVO> ufficiSource) {
        List<UfficioVO> list = new ArrayList<UfficioVO>();
        Ufficio uff;
        for (Iterator<UfficioVO> i = ufficiSource.iterator(); i.hasNext();) {
            uff = new Ufficio(i.next());
            list.add(uff.getValueObject());
        }
        Comparator<UfficioVO> c = new Comparator<UfficioVO>() {
            public int compare(UfficioVO uff1, UfficioVO uff2) {
               return uff1.getDescription().compareToIgnoreCase(
                        uff2.getDescription());
            }
        };
        Collections.sort(list, c);
        return list;
    }
    
    public static Collection<Integer> getAllDipendenti(Ufficio uff){
    	List<Integer> list = new ArrayList<Integer>();
    	list.add(uff.getValueObject().getId());
    	for(Ufficio child:uff.getUfficiDipendenti())
    		list.addAll(getAllDipendenti(child));
    	return list;
    };

}
