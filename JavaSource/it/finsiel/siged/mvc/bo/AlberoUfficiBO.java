package it.finsiel.siged.mvc.bo;

import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.model.organizzazione.AreaOrganizzativa;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.AlberoUfficiForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.AlberoUfficiUtentiForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.RicercaForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class AlberoUfficiBO {

	public static void impostaUfficioRoot(Utente utente, AlberoUfficiForm form,
			boolean alberoCompleto) {
		int ufficioId = form.getUfficioCorrenteId();
		Organizzazione org = Organizzazione.getInstance();
		Ufficio ufficioCorrente = org.getUfficio(utente.getUfficioInUso());
		Ufficio ufficioRoot = org.getUfficio(utente.getUfficioInUso());
		if (alberoCompleto) {
			AreaOrganizzativa aoo = org.getAreaOrganizzativa(ufficioRoot
					.getValueObject().getAooId());
			ufficioRoot = aoo.getUfficioCentrale();
			ufficioCorrente = ufficioRoot;
		}
		ufficioId = ufficioCorrente.getValueObject().getId().intValue();

		form.setUfficioCorrenteId(ufficioId);
		form.setUfficioCorrentePath(ufficioCorrente.getPath());
		form.setUfficioCorrente(ufficioCorrente.getValueObject());
		List<UfficioVO> list = new ArrayList<UfficioVO>();
		Ufficio uff = null;
		for (Iterator<Ufficio> i = ufficioCorrente.getUfficiDipendenti().iterator(); i
				.hasNext();) {
			uff = i.next();
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
	
	public static void impostaUfficioProtocollatore(Utente utente, RicercaForm form,
			boolean alberoCompleto) {
		int ufficioId = form.getUfficioProtCorrenteId();
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
		form.setUfficioProtCorrenteId(ufficioId);
		form.setUfficioProtCorrentePath(ufficioCorrente.getPath());
		form.setUfficioProtCorrente(ufficioCorrente.getValueObject());
		List<UfficioVO> list = new ArrayList<UfficioVO>();
		for (Iterator<Ufficio> i = ufficioCorrente.getUfficiDipendenti().iterator(); i
				.hasNext();) {
			uff = (Ufficio) i.next();
			list.add(uff.getValueObject());
		}
		Comparator<UfficioVO> c = new Comparator<UfficioVO>() {
			public int compare(UfficioVO uff1, UfficioVO uff2) {
				return uff1.getDescription().compareToIgnoreCase(
						uff2.getDescription());
			}
		};
		Collections.sort(list, c);
		form.setUfficiProtDipendenti(list);
	}
	
	public static void impostaUfficio(Utente utente, AlberoUfficiForm form,
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
			uff = (Ufficio) i.next();
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

	public static void impostaUfficioPIDest(Utente utente, RicercaForm form,
			boolean alberoCompleto) {
		int ufficioId = form.getUfficioCorrentePIDestId();
		Organizzazione org = Organizzazione.getInstance();
		Ufficio ufficioRoot = org.getUfficio(utente.getUfficioInUso());
		if (alberoCompleto) { // ufficio centrale
			AreaOrganizzativa aoo = org.getAreaOrganizzativa(ufficioRoot
					.getValueObject().getAooId());
			ufficioRoot = aoo.getUfficioCentrale();
		}
		Ufficio ufficioCorrente = org.getUfficio(ufficioId);
		if (ufficioCorrente == null) {
			ufficioCorrente = org.getUfficio(utente.getUfficioInUso());;
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
		form.setUfficioCorrentePIDestId(ufficioId);
		form.setUfficioCorrentePathPIDest(ufficioCorrente.getPath());
		form.setUfficioCorrentePIDest(ufficioCorrente.getValueObject());
		List<UfficioVO> list = new ArrayList<UfficioVO>();
		for (Iterator<Ufficio> i = ufficioCorrente.getUfficiDipendenti().iterator(); i
				.hasNext();) {
			uff = i.next();
			list.add(uff.getValueObject());
		}
		Comparator<UfficioVO> c = new Comparator<UfficioVO>() {
			public int compare(UfficioVO uff1, UfficioVO uff2) {
				return uff1.getDescription().compareToIgnoreCase(
						uff2.getDescription());
			}
		};
		Collections.sort(list, c);
		form.setUfficiDipendentiPIDest(list);
	}

	public static void impostaUfficioUtenti(Utente utente,
			AlberoUfficiUtentiForm form, boolean alberoCompleto) {
		Organizzazione org = Organizzazione.getInstance();
		impostaUfficio(utente, form, alberoCompleto);
		Ufficio ufficioCorrente = org.getUfficio(form.getUfficioCorrenteId());
		List<UtenteVO> list = new ArrayList<UtenteVO>();
		for (Iterator<Utente> i = ufficioCorrente.getUtenti().iterator(); i.hasNext();) {
			
			Utente ute = (Utente) i.next();
			if(ute.getValueObject().isAbilitato())
				ute.getValueObject().setCarica(ute.getCaricaUfficioVO(form.getUfficioCorrenteId()).getNome());
				list.add(ute.getValueObject());
		}

		Comparator<UtenteVO> c = new Comparator<UtenteVO>() {
			public int compare(UtenteVO ute1,UtenteVO ute2) {
				return ute1.getFullName().compareToIgnoreCase(
						ute2.getFullName());
			}
		};
		
		Collections.sort(list, c);
		form.setUtenti(list);
	
	}
	
	public static void impostaUfficioUtentiProtocollatore(Utente utente, RicercaForm form,
			boolean alberoCompleto) {
		Organizzazione org = Organizzazione.getInstance();
		impostaUfficioProtocollatore(utente, form, alberoCompleto);
		Ufficio ufficioCorrente = org.getUfficio(form.getUfficioProtCorrenteId());
		List<UtenteVO> list = new ArrayList<UtenteVO>();
		for (Iterator<Utente>  i = ufficioCorrente.getUtenti().iterator(); i.hasNext();) {
			Utente ute = i.next();
			list.add(ute.getValueObject());
		}
		Comparator<UtenteVO> c = new Comparator<UtenteVO>() {
			public int compare(UtenteVO ute1, UtenteVO ute2) {
				return ute1.getFullName().compareToIgnoreCase(
						ute2.getFullName());
			}
		};
		Collections.sort(list, c);
		form.setUtentiProt(list);
	}

	public static void impostaUfficioUtentiPIDest(Utente utente,
			RicercaForm form, boolean alberoCompleto) {
		Organizzazione org = Organizzazione.getInstance();
		impostaUfficioPIDest(utente, form, alberoCompleto);
		Ufficio ufficioCorrente = org.getUfficio(form.getUfficioCorrentePIDestId());
		List<UtenteVO> list = new ArrayList<UtenteVO>();
		for (Iterator<Utente> i = ufficioCorrente.getUtenti().iterator(); i.hasNext();) {
			Utente ute = i.next();
			ute.getValueObject().setCarica(ute.getCaricaUfficioVO(form.getUfficioCorrentePIDestId()).getNome());
			list.add(ute.getValueObject());
		}
		Comparator<UtenteVO> c = new Comparator<UtenteVO>() {
			public int compare(UtenteVO ute1, UtenteVO ute2) {
				return ute1.getFullName().compareToIgnoreCase(
						ute2.getFullName());
			}
		};
		Collections.sort(list, c);
		form.setUtentiPIDest(list);
	}

	public static void impostaUfficioUtentiAbilitati(Utente utente,
			AlberoUfficiUtentiForm form, boolean alberoCompleto) {
		Organizzazione org = Organizzazione.getInstance();
		impostaUfficio(utente, form, alberoCompleto);
		Ufficio ufficioCorrente = org.getUfficio(form.getUfficioCorrenteId());
		List<UtenteVO> list = new ArrayList<UtenteVO>();
		for (Iterator<Utente> i = ufficioCorrente.getUtenti().iterator(); i.hasNext();) {
			Utente ute = (Utente) i.next();
			if (ute.getValueObject().isAbilitato()) {
				ute.getValueObject().setCarica(ute.getCaricaUfficioVO(form.getUfficioCorrenteId()).getNome());
				list.add(ute.getValueObject());
			}
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

	
	
	public static void impostaUfficioUtentiAbilitatiRoot(Utente utente,
			AlberoUfficiUtentiForm form, boolean alberoCompleto) {
		Organizzazione org = Organizzazione.getInstance();
		impostaUfficioRoot(utente, form, alberoCompleto);
		Ufficio ufficioCorrente = org.getUfficio(form.getUfficioCorrenteId());
		List<UtenteVO> list = new ArrayList<UtenteVO>();
		for (Iterator<Utente> i = ufficioCorrente.getUtenti().iterator(); i.hasNext();) {
			Utente ute =  i.next();
			if (ute.getValueObject().isAbilitato()) {
				ute.getValueObject().setCarica(ute.getCaricaUfficioVO(form.getUfficioCorrenteId()).getNome());
				list.add(ute.getValueObject());
			}
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

	public static AssegnatarioView impostaSelected(
			AlberoUfficiUtentiForm form) {
		Organizzazione org = Organizzazione.getInstance();
		AssegnatarioView resp = new AssegnatarioView();
		Ufficio uff=org.getUfficio(form.getUfficioCorrenteId());
		resp.setUfficioId(form.getUfficioCorrenteId());
		resp.setNomeUfficio(uff.getValueObject().getName());
		resp.setDescrizioneUfficio(uff.getPath());
		resp.setUtenteId(form.getUtenteSelezionatoId());
		if (form.getUtenteSelezionatoId() != 0) {
			Utente ute = org.getUtente(form.getUtenteSelezionatoId());
			resp.setNomeUtente(ute.getValueObject().getFullName());
			CaricaVO carica = CaricaDelegate.getInstance()
					.getCaricaByUtenteAndUfficio(form.getUtenteSelezionatoId(),
							form.getUfficioCorrenteId());
			resp.setNomeCarica(carica.getNome());
			resp.setCaricaId(carica.getCaricaId());
		}else{
			resp.setNomeUtente(null);
			resp.setNomeCarica(null);
			resp.setCaricaId(0);
		}
		return resp;
	}

}