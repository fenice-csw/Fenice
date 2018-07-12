package it.compit.fenice.mvc.presentation.action.protocollo;

import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.compit.fenice.mvc.vo.protocollo.DocumentoFascicoloVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.model.Fascicolo;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.TitolarioBO;
import it.finsiel.siged.mvc.business.DocumentaleDelegate;
import it.finsiel.siged.mvc.business.FascicoloDelegate;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.business.SoggettoDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.FascicoloForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.presentation.helper.FascicoloView;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.vo.documentale.FileVO;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.lookup.TipoDocumentoVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloFascicoloVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;
import it.finsiel.siged.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Implementation of <strong>Action </strong> to create a new E-Photo User.
 * 
 * @author Almaviva sud.
 * 
 */

public class VisualizzaCollegamentoFascicoloAction extends Action {

	static Logger logger = Logger
			.getLogger(VisualizzaCollegamentoFascicoloAction.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		FascicoloForm fascicoloForm = (FascicoloForm) form;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		int aooId = utente.getAreaOrganizzativa().getId().intValue();
		fascicoloForm.setAooId(aooId);
		if (form == null) {
			logger.info(" Creating new FascicoloAction");
			form = new FascicoloForm();
			session.setAttribute(mapping.getAttribute(), form);
		} else if (request.getAttribute("fascicoloId") != null) {
			fascicoloForm.setReturnId(NumberUtil.getInt(request.getAttribute("ritornoFascicoloId")));
			caricaFascicolo(request, fascicoloForm);
			return (mapping.findForward("input"));
		} else if (request.getParameter("btnIndietro") != null) {
			if (fascicoloForm.getRegistroId() > 0) {
				request.setAttribute("fascicoloId", fascicoloForm.getReturnId());
				return (mapping.findForward("indietro"));
			}
		}
		caricaFascicolo(request, fascicoloForm);
		Collection<TipoDocumentoVO> tipiDocumento = LookupDelegate.getInstance()
				.getTipiDocumento(aooId);
		fascicoloForm.setTipiDocumento(tipiDocumento);
		return (mapping.findForward("input"));
	}

	protected void caricaFascicolo(HttpServletRequest request,
			FascicoloForm form) {
		Integer fascicoloId = (Integer) request.getAttribute("fascicoloId");
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		Fascicolo fascicolo;
		Integer versioneId = (Integer) request.getAttribute("versioneId");
		if (fascicoloId != null) {
			FascicoloDelegate fd = FascicoloDelegate.getInstance();
			int id = fascicoloId.intValue();
			if (versioneId == null) {
				fascicolo = fd.getFascicoloById(id);
				form.setVersioneDefault(true);
			} else {
				int versione = versioneId.intValue();
				fascicolo = fd.getFascicoloByIdVersione(id, versione);
				form.setVersioneDefault(false);
			}
			session.setAttribute(Constants.FASCICOLO, fascicolo);
		} else {
			fascicolo = (Fascicolo) session.getAttribute(Constants.FASCICOLO);
		}
		aggiornaFascicoloForm(fascicolo, form, utente);
	}

	private void aggiornaStatoFascicoloForm(FascicoloVO fascicolo,
			FascicoloForm form, Utente utente) {
		boolean modificabile = false;
		int statoFascicolo = fascicolo.getStato();
		if (statoFascicolo != Parametri.STATO_FASCICOLO_SCARTATO) {
			if (utente.getUfficioVOInUso().getParentId() == 0
					|| utente.isUtenteAbilitatoSuUfficio(fascicolo
							.getUfficioIntestatarioId())
					|| utente.isUtenteAbilitatoSuUfficio(fascicolo
							.getUfficioResponsabileId())) {
				modificabile = true;
			}

		}
		form.setModificabile(modificabile);
	}

	private void aggiornaFascicoloForm(Fascicolo fascicolo,
			FascicoloForm fForm, Utente utente) {
		impostaFascicoloForm(fascicolo.getFascicoloVO(), fForm, utente);
		aggiornaStatoFascicoloForm(fascicolo.getFascicoloVO(), fForm, utente);
		
		Collection<FileVO> documentiFascicolo = new ArrayList<FileVO>();
		Iterator<DocumentoFascicoloVO> itDF = fascicolo.getDocumenti().iterator();
		DocumentaleDelegate dd = DocumentaleDelegate.getInstance();
		
		while (itDF.hasNext()) {
			DocumentoFascicoloVO dfVO = (DocumentoFascicoloVO) itDF.next();
			FileVO vo=dd.getFileVOById(dfVO.getDocumentoId());
			documentiFascicolo.add(vo);
		}
		fForm.setDocumentiFascicolo(documentiFascicolo);
		
		Iterator<ProtocolloFascicoloVO> it = fascicolo.getProtocolli().iterator();
		SortedMap<String,ReportProtocolloView> protocolliView = new TreeMap<String,ReportProtocolloView>(new Comparator<String>() {
			public int compare(String s1, String s2) {
				Long i1 = Long.decode(s1.substring(1));
				Long i2 = Long.decode(s2.substring(1));
				if (Integer.valueOf(s1.charAt(0)) == Integer.valueOf(s2
						.charAt(0)))
					return (int) (i2.longValue() - i1.longValue());
				else
					return Integer.valueOf(s1.charAt(0))
							- Integer.valueOf(s2.charAt(0));
			}
		});
		while (it.hasNext()) {
			ProtocolloFascicoloVO pf = it.next();
			ReportProtocolloView rp = ProtocolloDelegate.getInstance()
					.getProtocolloView(pf.getProtocolloId());
			protocolliView.put(String.valueOf(rp.getRegistroAnnoNumero()), rp);
		}
		fForm.setProtocolliFascicolo(protocolliView);
		Collection<Integer> fascicoliCollegatiId = FascicoloDelegate
				.getInstance().getCollegatiIdByFascicoloId(
						fascicolo.getFascicoloVO().getId());
		fForm.initFascicoliCollegati();
		for (Integer fascId : fascicoliCollegatiId) {
			FascicoloView fc = FascicoloDelegate.getInstance()
					.getFascicoloViewById(fascId);
			fForm.collegaFascicolo(fc);
		}
		Collection<Integer> sottoFascicoliId = FascicoloDelegate.getInstance().getSottoFascicoliIdByFascicoloId(
						fascicolo.getFascicoloVO().getId());
		fForm.initSottoFascicoli();
		for (Integer fascId : sottoFascicoliId) {
			FascicoloView fc = FascicoloDelegate.getInstance()
					.getFascicoloViewById(fascId);
			fForm.addSottoFascicolo(fc);
		}

		fForm.setSearch(false);
		fForm.setProtocolliFascicoloSearch(new HashMap<Integer,ReportProtocolloView>());

		fForm.setProcedimentiFascicolo(fascicolo.getProcedimenti());
		fForm.setProcedimenti(fascicolo.getProcedimenti());
		FascicoloVO fVO = fascicolo.getFascicoloVO();
		fForm.setCodice(fVO.getAnnoRiferimento()
				+ StringUtil.formattaNumeroProtocollo(String.valueOf(fVO
						.getProgressivo()), 6));
		fForm.aggiornaSezioni();
	}

	private void impostaTitolario(FascicoloForm form, Utente utente,
			int titolarioId) {
		int ufficioId = form.getUfficioCorrenteId();
		TitolarioBO.impostaTitolario(form, ufficioId, titolarioId);
		if (form.getTitolario() != null) {
			form.setGiorniAlert(String.valueOf(form.getTitolario()
					.getGiorniAlert()));
			form.setGiorniMax(String
					.valueOf(form.getTitolario().getGiorniMax()));
		} else {
			form.setGiorniAlert(null);
			form.setGiorniMax(null);
		}
	}

	private void aggiornaParentForm(FascicoloForm form, int fascicoloId) {
		FascicoloDelegate fd = FascicoloDelegate.getInstance();
		Fascicolo parent = fd.getFascicoloById(fascicoloId);
		form.setPadre(parent.getFascicoloVO().getOggetto());
		form.setPadreId(fascicoloId);
	}

	private void impostaFascicoloForm(FascicoloVO fascicoloVO,
			FascicoloForm fascicoloForm, Utente utente) {
		fascicoloForm.setId(fascicoloVO.getId());
		fascicoloForm.setAooId(fascicoloVO.getAooId());
		fascicoloForm.setCodice(fascicoloVO.getCodice());
		impostaTitolario(fascicoloForm, utente, fascicoloVO.getTitolarioId());
		aggiornaParentForm(fascicoloForm, fascicoloVO.getParentId());
		if (fascicoloVO.getGiorniAlert() == 0)
			fascicoloForm.setGiorniAlert(null);
		else
			fascicoloForm.setGiorniAlert(String.valueOf(fascicoloVO
					.getGiorniAlert()));
		if (fascicoloVO.getGiorniMax() == 0)
			fascicoloForm.setGiorniMax(null);
		else
			fascicoloForm.setGiorniMax(String.valueOf(fascicoloVO
					.getGiorniMax()));
		fascicoloForm.setRiferimentiLegislativi(fascicoloVO
				.getRiferimentiLegislativi());
		fascicoloForm.setDataApertura(DateUtil.formattaData(fascicoloVO
				.getDataApertura().getTime()));
		if (fascicoloVO.getDataChiusura() != null) {
			fascicoloForm.setDataChiusura(DateUtil.formattaData(fascicoloVO
					.getDataChiusura().getTime()));
		}

		fascicoloForm.setInteressato(fascicoloVO.getInteressato());
		fascicoloForm.setIndiInteressato(fascicoloVO.getIndiInteressato());
		fascicoloForm.setDelegato(fascicoloVO.getDelegato());
		fascicoloForm.setIndiDelegato(fascicoloVO.getIndiDelegato());
		
		fascicoloForm.setDescrizione(fascicoloVO.getDescrizione());
		fascicoloForm.setNome(fascicoloVO.getNome());
		fascicoloForm.setNote(fascicoloVO.getNote());
		fascicoloForm.setOggettoFascicolo(fascicoloVO.getOggetto());
		fascicoloForm.setProcessoId(fascicoloVO.getProcessoId());
		fascicoloForm.setProgressivo(fascicoloVO.getProgressivo());
		fascicoloForm.setRegistroId(fascicoloVO.getRegistroId());
		fascicoloForm.setUfficioResponsabileId(fascicoloVO
				.getUfficioResponsabileId());
		fascicoloForm.setCaricaResponsabileId(fascicoloVO
				.getCaricaResponsabileId());
		fascicoloForm.setStatoFascicolo(fascicoloVO.getStato());
		fascicoloForm.setUfficioCorrenteId(fascicoloVO
				.getUfficioIntestatarioId());
		if (fascicoloVO.getCaricaIstruttoreId() > 0) {
			Organizzazione org = Organizzazione.getInstance();
			CaricaVO carica = org
					.getCarica(fascicoloVO.getCaricaIstruttoreId());

			Utente ute = org.getUtente(carica.getUtenteId());
			if (ute != null) {
				ute.getValueObject().setCarica(carica.getNome());
				fascicoloForm.setIstruttore(newIstruttore(ute, carica
						.isAttivo()));
			}
			fascicoloForm
					.setUtenteIstruttoreSelezionatoId(carica.getUtenteId());

		} else
			fascicoloForm.setUtenteIstruttoreSelezionatoId(0);
		fascicoloForm.setVersione(fascicoloVO.getVersione());
		if (fascicoloVO.getUfficioIntestatarioId() > 0) {
			Organizzazione org = Organizzazione.getInstance();
			Ufficio uff = org
					.getUfficio(fascicoloVO.getUfficioIntestatarioId());
			CaricaVO carica = org.getCarica(fascicoloVO
					.getCaricaIntestatarioId());
			if (carica != null) {
				if (carica.getUtenteId() != 0) {
					fascicoloForm.setUtenteSelezionatoId(carica.getUtenteId());
					Utente ute = org.getUtente(carica.getUtenteId());
					ute.getValueObject().setCarica(carica.getNome());
					fascicoloForm.setMittente(newMittente(uff, ute, carica
							.isAttivo()));
				} else {
					fascicoloForm.setUtenteSelezionatoId(0);
					fascicoloForm.setMittente(newMittente(uff, null, carica
							.isAttivo()));
				}

			} else {
				fascicoloForm.setUtenteSelezionatoId(0);
				fascicoloForm.setMittente(newMittente(uff, null, true));
			}
		}
		fascicoloForm.setAnnoRiferimento(fascicoloVO.getAnnoRiferimento());
		fascicoloForm.setTipoFascicolo(fascicoloVO.getTipoFascicolo());
		if (fascicoloVO.getDataEvidenza() != null) {
			fascicoloForm.setDataEvidenza(DateUtil.formattaData(fascicoloVO
					.getDataEvidenza().getTime()));
		} else {
			fascicoloForm.setDataEvidenza(null);
		}
		if (fascicoloVO.getDataUltimoMovimento() != null) {
			fascicoloForm.setDataUltimoMovimento(DateUtil
					.formattaData(fascicoloVO.getDataUltimoMovimento()
							.getTime()));
		} else {
			fascicoloForm.setDataUltimoMovimento(null);
		}
		if (fascicoloVO.getDataCarico() != null) {
			fascicoloForm.setDataCarico(DateUtil.formattaData(fascicoloVO
					.getDataCarico().getTime()));
		} else {
			fascicoloForm.setDataCarico(null);
		}
		if (fascicoloVO.getDataScarto() != null) {
			fascicoloForm.setDataScarto(DateUtil.formattaData(fascicoloVO
					.getDataScarto().getTime()));
		} else {
			fascicoloForm.setDataScarto(null);
		}

		if (fascicoloVO.getDataScarico() != null) {
			fascicoloForm.setDataScarico(DateUtil.formattaData(fascicoloVO
					.getDataScarico().getTime()));
		} else {
			fascicoloForm.setDataScarico(DateUtil.formattaData(System
					.currentTimeMillis()));
		}

		fascicoloForm.setPosizioneSelezionataId(fascicoloVO
				.getPosizioneFascicolo());
		fascicoloForm.setPosizioneSelezionata(""
				+ fascicoloVO.getPosizioneFascicolo());

		fascicoloForm.setVersione(fascicoloVO.getVersione());
		fascicoloForm
				.setCollocazioneLabel1(fascicoloVO.getCollocazioneLabel1());
		fascicoloForm
				.setCollocazioneLabel2(fascicoloVO.getCollocazioneLabel2());
		fascicoloForm
				.setCollocazioneLabel3(fascicoloVO.getCollocazioneLabel3());
		fascicoloForm
				.setCollocazioneLabel4(fascicoloVO.getCollocazioneLabel4());
		fascicoloForm.setCollocazioneValore1(fascicoloVO
				.getCollocazioneValore1());
		fascicoloForm.setCollocazioneValore2(fascicoloVO
				.getCollocazioneValore2());
		fascicoloForm.setCollocazioneValore3(fascicoloVO
				.getCollocazioneValore3());
		fascicoloForm.setCollocazioneValore4(fascicoloVO
				.getCollocazioneValore4());
		fascicoloForm.setComune(fascicoloVO.getComune());
		fascicoloForm.setCapitolo(fascicoloVO.getCapitolo());
	}

	private AssegnatarioView newIstruttore(Utente utente, boolean attivo) {
		AssegnatarioView istruttore = new AssegnatarioView();
		UtenteVO uteVO = utente.getValueObject();
		istruttore.setUtenteId(uteVO.getId().intValue());
		if (attivo)
			istruttore.setNomeUtente(uteVO.getCaricaFullName());
		else
			istruttore.setNomeUtente(uteVO.getCaricaFullNameNonAttivo());
		return istruttore;
	}

	private AssegnatarioView newMittente(Ufficio ufficio, Utente utente,
			boolean attivo) {
		UfficioVO uffVO = ufficio.getValueObject();
		AssegnatarioView mittente = new AssegnatarioView();
		mittente.setUfficioId(uffVO.getId().intValue());
		mittente.setDescrizioneUfficio(ufficio.getPath());
		mittente.setNomeUfficio(uffVO.getDescription());
		if (utente != null) {
			UtenteVO uteVO = utente.getValueObject();
			mittente.setUtenteId(uteVO.getId().intValue());
			if (attivo)
				mittente.setNomeUtente(uteVO.getCaricaFullName());
			else
				mittente.setNomeUtente(uteVO.getCaricaFullNameNonAttivo());
		}
		return mittente;
	}

}