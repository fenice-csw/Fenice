package it.finsiel.siged.mvc.bo;

import it.finsiel.siged.model.organizzazione.AreaOrganizzativa;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.presentation.actionform.documentale.CartelleForm;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class CartelleBO {

	public static void impostaUfficio(Utente utente, CartelleForm form,
			boolean alberoCompleto) {
		int ufficioId = form.getUfficioCorrenteId();
		Organizzazione org = Organizzazione.getInstance();
		Ufficio ufficioRoot = org.getUfficio(utente.getUfficioInUso());
		if (alberoCompleto) { // ufficio centrale
			AreaOrganizzativa aoo = org.getAreaOrganizzativa(ufficioRoot
					.getValueObject().getAooId());
			ufficioRoot = aoo.getUfficioCentrale();
		}
		Ufficio ufficioCorrente = org.getUfficio(ufficioId);
		if (ufficioCorrente == null) {
			ufficioCorrente = org.getUfficio(utente.getUfficioInUso());
			ufficioId = ufficioCorrente.getValueObject().getId().intValue();
		}
		Ufficio uff = ufficioCorrente;
		while (uff != ufficioRoot) {
			if (uff == null) {
				ufficioCorrente = ufficioRoot;
				ufficioId = ufficioCorrente.getValueObject().getId().intValue();
				break;
			}
			uff = uff.getUfficioDiAppartenenza();
		}
		form.setUfficioCorrenteId(ufficioId);
		form.setUfficioCorrentePath(ufficioCorrente.getPath());
		form.setUfficioCorrente(ufficioCorrente.getValueObject());
		List<UfficioVO> list = new ArrayList<UfficioVO>();
		for (Iterator<Ufficio> i = ufficioCorrente.getUfficiDipendenti().iterator(); i
				.hasNext();) {
			uff =i.next();
			list.add(uff.getValueObject());
		}
		Comparator<UfficioVO> c = new Comparator<UfficioVO>() {
			public int compare(UfficioVO uff1, UfficioVO uff2) {
				return uff1.getDescription().compareToIgnoreCase(
						uff2.getDescription());
			}
		};
		Collections.sort(list, c);
		form.setUfficiDipendenti(list);
	}
	
	public static void impostaUfficioUtenti(Utente utente,
			CartelleForm form, boolean alberoCompleto) {
		Organizzazione org = Organizzazione.getInstance();
		impostaUfficio(utente, form, alberoCompleto);
		Ufficio ufficioCorrente = org.getUfficio(form.getUfficioCorrenteId());
		List<UtenteVO> list = new ArrayList<UtenteVO>();
		for (Iterator<Utente> i = ufficioCorrente.getUtenti().iterator(); i.hasNext();) {
			Utente ute =i.next();
			ute.getValueObject().setCarica(ute.getCaricaUfficioVO(form.getUfficioCorrenteId()).getNome());
			list.add(ute.getValueObject());
		}
		Comparator<UtenteVO>c = new Comparator<UtenteVO>() {
			public int compare(UtenteVO ute1, UtenteVO ute2) {
				return ute1.getFullName().compareToIgnoreCase(
						ute2.getFullName());
			}
		};
		Collections.sort(list, c);
		form.setUtenti(list);
	}
}