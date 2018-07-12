package it.compit.fenice.mvc.bo;

import it.compit.fenice.model.protocollo.PostaInterna;
import it.compit.fenice.mvc.business.AmmTrasparenteDelegate;
import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.presentation.actionform.ammtrasparente.AmmTrasparenteForm;
import it.compit.fenice.mvc.presentation.actionform.ammtrasparente.DocumentoAmmTrasparenteForm;
import it.compit.fenice.mvc.vo.ammtrasparente.AmmTrasparenteVO;
import it.compit.fenice.mvc.vo.ammtrasparente.DocumentoAmmTrasparenteVO;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.AreaOrganizzativa;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.util.NumberUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessages;

public class AmmTrasparenteBO {

	public static void preparaPostaInterna(HttpServletRequest request,
			DocumentoAmmTrasparenteVO vo, Utente utente, ActionMessages errors, int flagTipo)
			throws DataException {
		PostaInterna pi = ProtocolloBO.getDefaultPostaInterna(utente);
		try {
			ProtocolloVO protocollo = pi.getProtocollo();
			
			for(DocumentoVO doc: vo.getDocumentiCollection()){
				if(doc.getPrincipale())
					pi.setDocumentoPrincipale(doc);
				else
					pi.allegaDocumento(doc);
			}
			
			AssegnatarioVO mittente = new AssegnatarioVO();
			mittente.setUfficioAssegnatarioId(utente.getUfficioInUso());
			mittente.setUtenteAssegnatarioId(utente.getValueObject().getId());
			pi.setMittente(mittente);
			protocollo.setOggetto(vo.getOggetto());
			pi.setProtocollo(protocollo);
			pi.getProtocollo().setStatoProtocollo("P");
			request.setAttribute(Constants.PROTOCOLLO_DA_AMM_TRASPARENTE, pi);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException("Errore nella generazione del Protocollo");
		}
	}
	
	public static void preparaPostaInterna(HttpServletRequest request,
			DocumentoAmmTrasparenteForm form, Utente utente, ActionMessages errors, int flagTipo)
			throws DataException {
		PostaInterna pi = ProtocolloBO.getDefaultPostaInterna(utente);
		AmmTrasparenteVO repVO=AmmTrasparenteDelegate.getInstance().getSezione(form.getSezioneId());
		try {
			ProtocolloVO protocollo = pi.getProtocollo();
			
			for(DocumentoVO doc: form.getDocumentiAllegatiCollection()){
				if(doc.getPrincipale())
					pi.setDocumentoPrincipale(doc);
				else
					pi.allegaDocumento(doc);
			}
			
			AssegnatarioVO mittente = new AssegnatarioVO();
			mittente.setUfficioAssegnatarioId(utente.getUfficioInUso());
			mittente.setUtenteAssegnatarioId(utente.getValueObject().getId());
			pi.setMittente(mittente);
			protocollo.setOggetto(repVO.getDescrizione()+" - "+form.getNumeroDocumentoAmmTrasparente()+" - "+form.getOggetto());
			pi.setProtocollo(protocollo);
			pi.getProtocollo().setStatoProtocollo("P");
//			pi.setDocSezioneId(form.getDocSezioneId());
			if(form.getProtocolloId()!=0){
				ProtocolloVO piVO = ProtocolloDelegate.getInstance().getProtocolloVOById(form.getProtocolloId());
				AllaccioVO allaccioVO = new AllaccioVO();
				allaccioVO.setProtocolloAllacciatoId(piVO.getId());
				allaccioVO.setAllaccioDescrizione(piVO.getNumProtocollo()
						+ "/" + piVO.getAnnoRegistrazione() + " ("
						+ piVO.getFlagTipo() + ")");
				pi.allacciaProtocollo(allaccioVO);
				AssegnatarioVO destinatario = new AssegnatarioVO();
				destinatario.setUfficioAssegnatarioId(piVO
						.getUfficioProtocollatoreId());
				destinatario.setUtenteAssegnatarioId(piVO.getUtenteProtocollatoreId());
				CaricaVO c=CaricaDelegate.getInstance().getCaricaByUtenteAndUfficio(piVO.getUtenteProtocollatoreId(), piVO.getUfficioProtocollatoreId());
				if(c!=null)
					destinatario.setCaricaAssegnatarioId(c.getCaricaId());
				destinatario.setCaricaAssegnanteId(utente.getCaricaInUso());
				destinatario.setUfficioAssegnanteId(utente.getUfficioInUso());
				destinatario.setCompetente(true);
				pi.aggiungiDestinatario(destinatario);
			}
			request.setAttribute(Constants.PROTOCOLLO_DA_AMM_TRASPARENTE, pi);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException("Errore nella generazione del Protocollo");
		}
	}
	
	public static void putAllegato(DocumentoVO doc, Map<String, DocumentoVO> documenti) {
		int idx = doc.getIdx();
		if (idx == 0) {
			idx = getNextDocIdx(documenti);
		}
		doc.setIdx(idx);
		documenti.put(String.valueOf(idx), doc);
	}

	private static int getNextDocIdx(Map<String, DocumentoVO> allegati) {
		int max = 0;
		Iterator<String> it = allegati.keySet().iterator();
		while (it.hasNext()) {
			String id = (String) it.next();
			int cur = NumberUtil.getInt(id);
			if (cur > max)
				max = cur;
		}
		return max + 1;
	}

	public static void impostaUfficioUtentiResponsabile(Utente utente,
			AmmTrasparenteForm form, boolean ufficioCompleto) {
		Organizzazione org = Organizzazione.getInstance();
		impostaUfficioResponsabile(utente, form, ufficioCompleto);
		Ufficio ufficioCorrente = org.getUfficio(form
				.getUfficioResponsabileCorrenteId());
		List<UtenteVO> list = new ArrayList<UtenteVO> ();
		for (Iterator<Utente> i = ufficioCorrente.getUtenti().iterator(); i.hasNext();) {
			Utente ute = i.next();
			if (ute.getValueObject().isAbilitato()) {
				ute.getValueObject().setCarica(
						ute.getCaricaUfficioVO(
								form.getUfficioResponsabileCorrenteId())
								.getNome());
				list.add(ute.getValueObject());
			}
		}
		Comparator<UtenteVO>  c = new Comparator<UtenteVO> () {
			public int compare(UtenteVO ute1, UtenteVO ute2) {
				return ute1.getFullName().compareToIgnoreCase(
						ute2.getFullName());
			}
		};
		Collections.sort(list, c);
		form.setUtenti(list);
	}

	public static void impostaUfficioResponsabile(Utente utente,
			AmmTrasparenteForm form, boolean alberoCompleto) {
		int ufficioId = form.getUfficioResponsabileCorrenteId();
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
		form.setUfficioResponsabileCorrenteId(ufficioId);
		form.setUfficioResponsabileCorrentePath(ufficioCorrente.getPath());
		form.setUfficioCorrenteResponsabile(ufficioCorrente.getValueObject());
		List<UfficioVO> list = new ArrayList<UfficioVO> ();
		for (Iterator<Ufficio>i = ufficioCorrente.getUfficiDipendenti().iterator(); i
				.hasNext();) {
			uff = (Ufficio) i.next();
			list.add(uff.getValueObject());
		}
		Comparator<UfficioVO>c = new Comparator<UfficioVO>() {
			public int compare(UfficioVO uff1, UfficioVO uff2) {
				return uff1.getDescription().compareToIgnoreCase(
						uff2.getDescription());
			}
		};
		Collections.sort(list, c);
		form.setUfficiDipendentiResponsabile(list);
	}

	public static void impostaUfficio(Utente utente,
			DocumentoAmmTrasparenteForm form, boolean alberoCompleto) {
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
		form.setUfficioPrecedenteId(ufficioCorrente.getValueObject()
				.getParentId());
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

	public static void impostaSettore(Utente utente,
			DocumentoAmmTrasparenteForm form, boolean alberoCompleto) {
		int ufficioId = form.getSettoreCorrenteId();
		Organizzazione org = Organizzazione.getInstance();
		Ufficio ufficioRoot = org.getUfficio(utente.getUfficioInUso());
		if (alberoCompleto) { // ufficio centrale
			AreaOrganizzativa aoo = org.getAreaOrganizzativa(ufficioRoot
					.getValueObject().getAooId());
			ufficioRoot = aoo.getUfficioCentrale();
		}
		Ufficio ufficioCorrente = org.getUfficio(ufficioId);
		if (ufficioCorrente == null) {
			//ufficioCorrente = org.getUfficio(utente.getUfficioInUso());
			ufficioCorrente = ufficioRoot;
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
		form.setSettoreCorrenteId(ufficioId);
		form.setSettoreCorrentePath(ufficioCorrente.getPath());
		form.setSettoreCorrente(ufficioCorrente.getValueObject());
		form.setSettorePrecedenteId(ufficioCorrente.getValueObject()
				.getParentId());
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
		form.setUfficiSettoriDipendenti(list);
	}
	
	public static AssegnatarioView newResponsabile(Ufficio ufficio) {
		AssegnatarioView responsabile = new AssegnatarioView();
		UfficioVO uffVO = ufficio.getValueObject();
		responsabile.setUfficioId(uffVO.getId().intValue());
		responsabile.setDescrizioneUfficio(ufficio.getPath());
		responsabile.setNomeUfficio(uffVO.getDescription());
		return responsabile;
	}

	public static void impostaResponsabile(AmmTrasparenteForm form) {
		AssegnatarioView resp = new AssegnatarioView();
		resp.setUfficioId(form.getUfficioResponsabileSelezionatoId());
		Ufficio uff = Organizzazione.getInstance().getUfficio(
				form.getUfficioResponsabileSelezionatoId());
		resp.setNomeUfficio(uff.getValueObject().getDescription());
		resp.setDescrizioneUfficio(uff.getPath());
		((AmmTrasparenteForm) form).setResponsabile(resp);

	}
}
