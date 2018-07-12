package it.finsiel.siged.mvc.bo;

import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.compit.fenice.mvc.vo.organizzazione.StoricoOrganigrammaVO;
import it.finsiel.siged.model.organizzazione.AreaOrganizzativa;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.presentation.helper.StatisticheView;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class OrganizzazioneBO {

	public static HashMap<Integer,UfficioVO> getUfficiUtente(ArrayList<Integer> ufficiIds) {
		Organizzazione org = Organizzazione.getInstance();
		Map<Integer,UfficioVO> uffici = new HashMap<Integer,UfficioVO>(2);
		ufficiIds.trimToSize();
		Iterator<Integer> i = ufficiIds.iterator();

		if (ufficiIds.size() == 0)
			return new HashMap<Integer,UfficioVO>(uffici);
		while (i.hasNext()) {
			Ufficio u = org.getUfficio(( i.next()).intValue());
			if (u.getValueObject().isAttivo())
				uffici.put(u.getValueObject().getId(), u.getValueObject());
		}
		return new HashMap<Integer,UfficioVO>(uffici);
	}

	public static HashMap<Integer,CaricaVO> getCaricheUtente(ArrayList<Integer> caricheIds, Map<Integer,UfficioVO> uffici) {
		Map<Integer,CaricaVO> cariche = new HashMap<Integer,CaricaVO>(2);
		if (uffici.size() != 0) {
			caricheIds.trimToSize();
			Iterator<Integer> i = caricheIds.iterator();
			if (caricheIds.size() == 0)
				return new HashMap<Integer,CaricaVO>(cariche);

			while (i.hasNext()) {
				CaricaVO c = CaricaDelegate.getInstance().getCarica(i.next());
				UfficioVO u = uffici.get(c.getUfficioId());
				if (u != null && c.isAttivo() && u.isAttivo())
					cariche.put(c.getCaricaId(), c);
			}
		}
		return new HashMap<Integer,CaricaVO>(cariche);
	}
	
	public static void setOrganigramma(Organizzazione org, StoricoOrganigrammaVO vo,Utente utente,String descrizione){
		vo.setAooId(utente.getAreaOrganizzativa().getId());
		vo.setRowCreatedUser(utente.getValueObject().getUsername());
		vo.setDescrizione(descrizione);
		AreaOrganizzativa aoo = org.getAreaOrganizzativa(utente.getAreaOrganizzativa().getId());
		Ufficio ufficioCorrente = aoo.getUfficioCentrale();
		Collection<Ufficio> uffici = new ArrayList<Ufficio>();
		uffici.add(ufficioCorrente);
		selezionaUffici(ufficioCorrente, uffici);
		for (Iterator<Ufficio> i = uffici.iterator(); i.hasNext();) {
			int ufficioId = (i.next()).getValueObject().getId()
					.intValue();
			Ufficio uff = org.getUfficio(ufficioId);
			StatisticheView stw = new StatisticheView();
			stw.setUfficio(uff.getPath());
			stw.setUtente(getCaricheUtenti(uff));
			vo.getOrganigramma().add(stw);
		}
		//UfficioDelegate.getInstance().salvaStoricoOrganigramma(vo);
	}
	
	private static String getCaricheUtenti(Ufficio uff) {
		List<UtenteVO> list = new ArrayList<UtenteVO>();
		int ufficioId = uff.getValueObject().getId().intValue();
		for (Iterator<Utente> i = uff.getUtenti().iterator(); i.hasNext();) {
			Utente ute = (i.next());
			CaricaVO carica = CaricaDelegate.getInstance().getCaricaByUtenteAndUfficio(ute.getValueObject().getId(),ufficioId);
			if (carica.isAttivo()) {
				ute.getValueObject().setCarica(carica.getNome());
				list.add(ute.getValueObject());
			}
		}
		Comparator<UtenteVO> c = new Comparator<UtenteVO>() {
			public int compare(UtenteVO ute1, UtenteVO ute2) {
				return ute1.getCaricaFullName().compareToIgnoreCase(
						ute2.getCaricaFullName());
			}
		};
		Collections.sort(list, c);
		StringBuffer utenti = new StringBuffer();
		for (Iterator<UtenteVO> i = list.iterator(); i.hasNext();) {
			UtenteVO ute = i.next();
			utenti.append(ute.getCaricaFullName() + "\n");
		}

		return utenti.toString();
	}
	
	private static void selezionaUffici(Ufficio uff, Collection<Ufficio> uffici) {
		if(uff.getUfficiDipendenti() != null) {
			for (Iterator<Ufficio> i = uff.getUfficiDipendenti().iterator(); i.hasNext();) {
				Ufficio u =  i.next();
				uffici.add(u);
				selezionaUffici(u, uffici);
			}
		}
	}
}
