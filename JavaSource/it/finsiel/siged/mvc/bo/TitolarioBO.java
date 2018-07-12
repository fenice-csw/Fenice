package it.finsiel.siged.mvc.bo;

import it.finsiel.siged.model.organizzazione.AreaOrganizzativa;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.TitolarioDelegate;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.TipoProcedimentoForm;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.TitolarioForm;
import it.finsiel.siged.mvc.presentation.actionform.documentale.DocumentoForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ConfigurazioneUtenteForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.FaldoneForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.FascicoloForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.RicercaEvidenzaForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.RicercaFaldoneForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.RicercaFascicoliForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.RicercaForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.RicercaProcedimentoForm;
import it.finsiel.siged.mvc.vo.lookup.TitolarioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.util.NumberUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TitolarioBO {

	public static void impostaTitolario(ProtocolloForm form, int ufficioId,
			int titolarioId) {

		TitolarioDelegate td = TitolarioDelegate.getInstance();
		TitolarioVO tVO = td.getTitolario(ufficioId, titolarioId,
				getAooId(ufficioId));
		form.setTitolario(tVO);
		if (tVO != null) {
			form.getTitolario().setDescrizione(td.getPathName(tVO));
		}
		form.setTitolariFigli(td.getTitolariByParent(ufficioId, titolarioId,
				getAooId(ufficioId)));
	}

	public static void impostaTitolario(RicercaForm form, int ufficioId,
			int titolarioId) {

		TitolarioDelegate td = TitolarioDelegate.getInstance();
		TitolarioVO tVO = td.getTitolario(ufficioId, titolarioId,
				getAooId(ufficioId));
		form.setTitolario(tVO);
		if (tVO != null) {
			form.getTitolario().setDescrizione(td.getPathName(tVO));
		}
		form.setTitolariFigli(td.getTitolariByParent(ufficioId, titolarioId,
				getAooId(ufficioId)));
	}

	public static void impostaTitolario(FaldoneForm form, int ufficioId,
			int titolarioId) {
		TitolarioDelegate td = TitolarioDelegate.getInstance();
		TitolarioVO tVO = td.getTitolario(ufficioId, titolarioId,
				getAooId(ufficioId));
		form.setTitolario(tVO);
		if (tVO != null) {
			form.getTitolario().setDescrizione(td.getPathName(tVO));
		}
		form.setTitolariFigli(td.getTitolariByParent(ufficioId, titolarioId,
				getAooId(ufficioId)));
	}

	public static void impostaTitolario(RicercaFaldoneForm form, int ufficioId,
			int titolarioId) {
		TitolarioDelegate td = TitolarioDelegate.getInstance();
		TitolarioVO tVO = td.getTitolario(ufficioId, titolarioId,
				getAooId(ufficioId));
		form.setTitolario(tVO);
		if (tVO != null) {
			form.getTitolario().setDescrizione(td.getPathName(tVO));
		}
		form.setTitolariFigli(td.getTitolariByParent(ufficioId, titolarioId,
				getAooId(ufficioId)));
	}

	public static void impostaTitolario(RicercaProcedimentoForm form,
			int ufficioId, int titolarioId) {

		TitolarioDelegate td = TitolarioDelegate.getInstance();
		TitolarioVO tVO = td.getTitolario(ufficioId, titolarioId,
				getAooId(ufficioId));
		form.setTitolario(tVO);
		if (tVO != null) {
			form.getTitolario().setDescrizione(td.getPathName(tVO));
		}
		form.setTitolariFigli(td.getTitolariByParent(ufficioId, titolarioId,
				getAooId(ufficioId)));
	}

	public static void impostaTitolario(RicercaEvidenzaForm form,
			int ufficioId, int titolarioId) {

		TitolarioDelegate td = TitolarioDelegate.getInstance();
		TitolarioVO tVO = td.getTitolario(ufficioId, titolarioId,
				getAooId(ufficioId));
		form.setTitolario(tVO);
		if (tVO != null) {
			form.getTitolario().setDescrizione(td.getPathName(tVO));
		}
		form.setTitolariFigli(td.getTitolariByParent(ufficioId, titolarioId,
				getAooId(ufficioId)));
	}

	public static void impostaTitolario(TitolarioForm form, int ufficioId,
			int titolarioId) {
		TitolarioDelegate td = TitolarioDelegate.getInstance();
		TitolarioVO tVO = td.getTitolario(ufficioId, titolarioId,
				getAooId(ufficioId));
		form.setTitolario(tVO);
		if (tVO != null) {
			form.getTitolario().setDescrizione(td.getPathName(tVO));
		}
		form.setTitolariFigli(td.getTitolariByParent(ufficioId, titolarioId,
				getAooId(ufficioId)));

	}

	public static void impostaTitolario(FascicoloForm form, int ufficioId,
			int titolarioId) {
		TitolarioDelegate td = TitolarioDelegate.getInstance();
		TitolarioVO tVO = td.getTitolario(titolarioId);
		if (titolarioId != 0 && tVO != null) {
			if (tVO.getResponsabileUfficioId() != 0)
				tVO.setNomeResponsabile(getNomeResponsabile(tVO));
			form.setTitolario(tVO);
			form.getTitolario().setDescrizione(td.getPathName(tVO));
			form.setTitolariFigli(td.getTitolariByParent(ufficioId,
					titolarioId, getAooId(ufficioId)));
			form.setTitolarioPrecedenteId(tVO.getParentId());
		} else {
			form.setTitolario(null);
			form.setTitolariFigli(td.getTitolariByParent(ufficioId,
					titolarioId, getAooId(ufficioId)));
			form.setTitolarioPrecedenteId(0);
		}
	}

	public static void impostaTitolario(TipoProcedimentoForm form,
			int ufficioId, int titolarioId) {
		TitolarioDelegate td = TitolarioDelegate.getInstance();
		TitolarioVO tVO = td.getTitolario(titolarioId);
		if (tVO != null)
			if (titolarioId != 0) {
				if (tVO.getResponsabileUfficioId() != 0)
					tVO.setNomeResponsabile(getNomeResponsabile(tVO));
			}
		form.setTitolario(tVO);
		if (tVO != null) {
			form.getTitolario().setDescrizione(td.getPathName(tVO));
		}
		//form.setTitolariFigli(td.getTitolariByParent(ufficioId, titolarioId,getAooId(ufficioId)));
		form.setTitolariFigli(td.getTitolariByParent(titolarioId, getAooId(ufficioId)));
	}

	public static void impostaTitolario(DocumentoForm form, int ufficioId,
			int titolarioId) {

		TitolarioDelegate td = TitolarioDelegate.getInstance();
		TitolarioVO tVO = td.getTitolario(ufficioId, titolarioId,
				getAooId(ufficioId));
		form.setTitolario(tVO);
		if (tVO != null) {
			form.getTitolario().setDescrizione(td.getPathName(tVO));
		}
		form.setTitolariFigli(td.getTitolariByParent(ufficioId, titolarioId,
				getAooId(ufficioId)));
	}

	public static void impostaTitolario(RicercaFascicoliForm form,
			int ufficioId, int titolarioId) {
		TitolarioDelegate td = TitolarioDelegate.getInstance();
		TitolarioVO tVO = td.getTitolario(ufficioId, titolarioId,form.getAooId());
		form.setTitolario(tVO);
		if (tVO != null) {
			form.getTitolario().setDescrizione(td.getPathName(tVO));
		}
		form.setTitolariFigli(td.getTitolariByParent(ufficioId, titolarioId,
				form.getAooId()));
	}

	public static void impostaAllTitolario(RicercaFascicoliForm form) {
		TitolarioDelegate td = TitolarioDelegate.getInstance();
		TitolarioVO tVO = null;
		form.setTitolario(tVO);
		form.setTitolariFigli(td.getTitolariByParent(0, form.getAooId()));
	}
	
	public static void impostaTitolario(ConfigurazioneUtenteForm form,
			int ufficioId, int titolarioId) {

		TitolarioDelegate td = TitolarioDelegate.getInstance();
		TitolarioVO tVO = td.getTitolario(ufficioId, titolarioId,
				getAooId(ufficioId));
		form.setTitolario(tVO);
		if (tVO != null) {
			form.getTitolario().setDescrizione(td.getPathName(tVO));
		}
		form.setTitolariFigli(td.getTitolariByParent(ufficioId, titolarioId,
				getAooId(ufficioId)));
	}

	private static String getNomeResponsabile(TitolarioVO vo) {
		String nomeresp;
		Organizzazione org = Organizzazione.getInstance();
		Ufficio ufficio = org.getUfficio(vo.getResponsabileUfficioId());
		nomeresp = ufficio.getPath();
		if (vo.getResponsabileId() != 0) {
			Utente responsabile = org.getUtente(vo.getResponsabileId());
			nomeresp = nomeresp + "/"
					+ responsabile.getValueObject().getCognome() + " "
					+ responsabile.getValueObject().getNome();
		}
		return nomeresp;
	}

	public static String getPathDescrizioneTitolario(int titolarioId) {

		if (titolarioId > 0) {
			TitolarioDelegate td = TitolarioDelegate.getInstance();
			TitolarioVO tVO = td.getTitolario(titolarioId);
			if (tVO != null) {
				return td.getPathName(tVO);
			}
		}
		return null;

	}

	private static int getAooId(int ufficioId) {
		int aooId=0;
		Organizzazione org = Organizzazione.getInstance();
		Ufficio uff=org.getUfficio(ufficioId);
		if(uff!=null)
			aooId=uff.getValueObject().getAooId();
		return aooId;
	}

	public static void putAllegato(DocumentoVO doc, Map<String,DocumentoVO> documenti) {
		int idx = doc.getIdx();
		if (idx == 0) {
			idx = getNextDocIdx(documenti);
		}
		doc.setIdx(idx);
		documenti.put(String.valueOf(idx), doc);
	}

	private static int getNextDocIdx(Map<String,DocumentoVO> allegati) {
		int max = 0;
		Iterator<String> it = allegati.keySet().iterator();
		while (it.hasNext()) {
			String id =  it.next();
			int cur = NumberUtil.getInt(id);
			if (cur > max)
				max = cur;
		}
		return max + 1;
	}

	public static void impostaUfficioUtentiResponsabile(Utente utente,
			TitolarioForm form, boolean ufficioCompleto) {
		Organizzazione org = Organizzazione.getInstance();
		impostaUfficio(utente, form, ufficioCompleto);
		Ufficio ufficioCorrente = org.getUfficio(form
				.getUfficioResponsabileCorrenteId());
		List<UtenteVO> list = new ArrayList<UtenteVO>();
		for (Iterator<Utente> i = ufficioCorrente.getUtenti().iterator(); i.hasNext();) {
			Utente ute =  i.next();
			if (ute.getValueObject().isAbilitato()) {
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

	public static void impostaUfficio(Utente utente, TitolarioForm form,
			boolean alberoCompleto) {
		int ufficioId = form.getUfficioResponsabileCorrenteId();
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
		form.setUfficioResponsabileCorrenteId(ufficioId);
		form.setUfficioResponsabileCorrentePath(ufficioCorrente.getPath());
		form.setUfficioCorrenteResponsabile(ufficioCorrente.getValueObject());
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
		form.setUfficiDipendentiResponsabile(list);
	}

	public static String getPath(int titolarioId) {
		if (titolarioId > 0) {
			TitolarioDelegate td = TitolarioDelegate.getInstance();
			TitolarioVO tVO = td.getTitolario(titolarioId);
			if (tVO != null)

				return tVO.getPath();

		}
		return null;
	}
}