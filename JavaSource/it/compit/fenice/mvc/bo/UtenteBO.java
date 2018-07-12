package it.compit.fenice.mvc.bo;

import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class UtenteBO {

	public static Collection<UtenteVO> getUtentiOrdinati(Collection<UtenteVO> utentiSource) {
		List<UtenteVO> list = new ArrayList<UtenteVO>();
		UtenteVO ute;
		for (Iterator<UtenteVO> i = utentiSource.iterator(); i.hasNext();) {
			ute = (UtenteVO) i.next();
			list.add(ute);
		}
		Comparator<UtenteVO> c = new Comparator<UtenteVO>() {
			public int compare(UtenteVO ute1, UtenteVO ute2) {
				return ute1.getCognomeNome().toUpperCase().compareTo(
						ute2.getCognomeNome().toUpperCase());
			}
		};
		Collections.sort(list, c);
		return list;
	}
}
