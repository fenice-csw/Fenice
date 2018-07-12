package it.compit.fenice.mvc.bo;

import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.presentation.actionform.protocollo.CruscottoForm;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.model.organizzazione.AreaOrganizzativa;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

public class CruscottoBO {

	public static void impostaUfficio(Utente utente, CruscottoForm form,
			boolean alberoCompleto) {
		int ufficioId = form.getUfficioCorrenteId();
		Organizzazione org = Organizzazione.getInstance();
		Ufficio ufficioRoot = org.getUfficio(utente.getUfficioInUso());
		if (alberoCompleto) {
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
		ufficioCorrente.getValueObject().setNumeroProtocolliAssegnati(
				contaProtocolliUfficio(utente, ufficioCorrente),contaAssegnatariUfficio(utente, ufficioCorrente));
		form.setUfficioCorrente(ufficioCorrente.getValueObject());
		List<UfficioVO> list = new ArrayList<UfficioVO>();
		for (Iterator<Ufficio> i = ufficioCorrente.getUfficiDipendenti().iterator(); i
				.hasNext();) {
			uff = (Ufficio) i.next();
			uff.getValueObject().setNumeroProtocolliAssegnati(
					contaProtocolliUfficio(utente, uff),contaAssegnatariUfficio(utente, uff));
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

	public static void impostaUfficioUtenti(Utente utente, CruscottoForm form,
			boolean alberoCompleto) {
		Organizzazione org = Organizzazione.getInstance();
		impostaUfficio(utente, form, alberoCompleto);
		Ufficio ufficioCorrente = org.getUfficio(form.getUfficioCorrenteId());
		List<UtenteVO> list = new ArrayList<UtenteVO>();
		for (Iterator<Utente> i = ufficioCorrente.getUtenti().iterator(); i.hasNext();) {
			Utente ute = (Utente) i.next();
			ute.getValueObject().setCarica(ute.getCaricaUfficioVO(form.getUfficioCorrenteId()).getNome());
			ute.getValueObject().setNumeroProtocolliAssegnati(contaProtocolliUtenti(ute,ufficioCorrente));
			list.add(ute.getValueObject());
		}
		Comparator<UtenteVO> c = new Comparator<UtenteVO>() {
			public int compare(UtenteVO ute1, UtenteVO ute2) {
				return ute1.getFullName().compareToIgnoreCase(
						ute2.getFullName());
			}
		};
		Collections.sort(list, c);
		form.setUtenti(list);

	}

	public static void impostaUfficioUtentiRoot(Utente utente,
			CruscottoForm form, boolean alberoCompleto) {
		Organizzazione org = Organizzazione.getInstance();
		impostaUfficioRoot(utente, form, alberoCompleto);
		Ufficio ufficioCorrente = org.getUfficio(form.getUfficioCorrenteId());
		List<UtenteVO> list = new ArrayList<UtenteVO>();
		for (Iterator<Utente> i = ufficioCorrente.getUtenti().iterator(); i.hasNext();) {
			Utente ute = (Utente) i.next();
			ute.getValueObject().setCarica(ute.getCaricaUfficioVO(form.getUfficioCorrenteId()).getNome());
			ute.getValueObject().setNumeroProtocolliAssegnati(
					contaProtocolliUtenti(ute,ufficioCorrente));
			list.add(ute.getValueObject());
		}
		Comparator<UtenteVO> c = new Comparator<UtenteVO>() {
			public int compare(UtenteVO ute1, UtenteVO ute2) {
				return ute1.getFullName().compareToIgnoreCase(
						ute2.getFullName());
			}
		};
		Collections.sort(list, c);
		form.setUtenti(list);

	}

	public static void impostaUfficioRoot(Utente utente, CruscottoForm form,
			boolean alberoCompleto) {
		Organizzazione org = Organizzazione.getInstance();
		Ufficio uff = null;
		Ufficio ufficioRoot = null;
		if (alberoCompleto) { 
			AreaOrganizzativa aoo = org.getAreaOrganizzativa(org.getUfficio(
					utente.getUfficioInUso()).getValueObject().getAooId());
			ufficioRoot = aoo.getUfficioCentrale();
		}
		if (ufficioRoot == null)
			ufficioRoot = org.getUfficio(utente.getUfficioInUso());
		int ufficioId = ufficioRoot.getValueObject().getId().intValue();
		form.setUfficioCorrenteId(ufficioId);
		form.setUfficioCorrentePath(ufficioRoot.getPath());
		ufficioRoot.getValueObject().setNumeroProtocolliAssegnati(
				contaProtocolliUfficio(utente, ufficioRoot),contaAssegnatariUfficio(utente, ufficioRoot));
		form.setUfficioCorrente(ufficioRoot.getValueObject());
		List<UfficioVO> list = new ArrayList<UfficioVO>();
		for (Iterator<Ufficio> i = ufficioRoot.getUfficiDipendenti().iterator(); i
				.hasNext();) {
			uff = (Ufficio) i.next();
			uff.getValueObject().setNumeroProtocolliAssegnati(
					contaProtocolliUfficio(utente, uff),contaAssegnatariUfficio(utente, uff));
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

	public static int contaProtocolliUfficio(Utente utente, Ufficio ufficio) {
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		GregorianCalendar today = new GregorianCalendar();
		int count=delegate.contaProtocolliAssegnatiPerCruscotti(0, today
				.get(Calendar.YEAR)-1, today.get(Calendar.YEAR), String.valueOf(ufficio.getValueObject().getId()), "U");
		return count;
	}

	public static int contaAssegnatariUfficio(Utente utente, Ufficio ufficio) {
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		GregorianCalendar today = new GregorianCalendar();
		int count=delegate.contaAssegnatariPerCruscotti(0, today
				.get(Calendar.YEAR)-1, today.get(Calendar.YEAR), String.valueOf(ufficio.getValueObject().getId()), "U");
		return count;
	}
	
	public static int contaProtocolliUtenti(Utente utente, Ufficio ufficio) {
		CaricaVO carica=CaricaDelegate.getInstance().getCaricaByUtenteAndUfficio(utente.getValueObject().getId(), ufficio.getValueObject().getId());
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		GregorianCalendar today = new GregorianCalendar();
		return delegate.contaProtocolliAssegnatiPerCruscotti(carica.getCaricaId(), today.get(Calendar.YEAR)-1, today.get(Calendar.YEAR), utente.getValueObject().getUsername(), "T");
	}
}
