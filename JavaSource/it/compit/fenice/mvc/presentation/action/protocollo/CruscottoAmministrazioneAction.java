package it.compit.fenice.mvc.presentation.action.protocollo;

import it.compit.fenice.mvc.bo.CruscottoBO;
import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.presentation.actionform.protocollo.CruscottoForm;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.DocumentoDelegate;
import it.finsiel.siged.mvc.business.FascicoloDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.flosslab.mvc.presentation.action.protocollo.helper.file.ProtocolloFileUtility;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

public class CruscottoAmministrazioneAction extends Action {

	static Logger logger = Logger
			.getLogger(CruscottoAmministrazioneAction.class.getName());

	protected void assegnaAdUtente(CruscottoForm form) {
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(form.getUfficioCorrenteId());
		ass.setNomeUfficio(form.getUfficioCorrente().getDescription());
		ass.setDescrizioneUfficio(form.getUfficioCorrentePath());
		ass.setUtenteId(form.getUtenteSelezionatoId());
		UtenteVO ute = form.getUtente(form.getUtenteSelezionatoId());
		ass.setNomeUtente(ute.getFullName());
		((CruscottoForm) form).setSottoposto(ass);
	}

	protected void assegnaAdUfficio(CruscottoForm form, int ufficioId) {
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(ufficioId);
		Organizzazione org = Organizzazione.getInstance();
		Ufficio uff = org.getUfficio(ufficioId);
		ass.setNomeUfficio(uff.getValueObject().getDescription());
		ass.setDescrizioneUfficio(uff.getPath());
		((CruscottoForm) form).setSottoposto(ass);

	}

	protected void clearSottoposto(CruscottoForm form) {
		form.setSottoposto(null);
		form.getProtocolliCollection().clear();
	}

	protected static void caricaProtocolliUtente(CruscottoForm form,
			Utente utente, UfficioVO ufficio) {
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		CaricaVO carica = CaricaDelegate.getInstance()
				.getCaricaByUtenteAndUfficio(utente.getValueObject().getId(),
						ufficio.getId());
		Map<Integer, ReportProtocolloView> protocolli = new HashMap<Integer, ReportProtocolloView>();
		form.setPosta(new HashMap<Integer, ReportProtocolloView>());
		form.setProtocolli(new HashMap<Integer, ReportProtocolloView>());
		GregorianCalendar today = new GregorianCalendar();
		protocolli = delegate.getProtocolliAssegnatiPerCruscotti(
				carica.getCaricaId(), today.get(Calendar.YEAR) - 1,
				today.get(Calendar.YEAR), "T");
		Collection<ReportProtocolloView> val = protocolli.values();
		for (ReportProtocolloView r : val)
			if (r.getTipoProtocollo().equals("P"))
				form.getAllPosta().put(new Integer(r.getProtocolloId()), r);
			else
				form.getProtocolli().put(new Integer(r.getProtocolloId()), r);
	}

	protected static void caricaFascicoliUtente(CruscottoForm form,
			Utente utente, UfficioVO ufficio) {
		FascicoloDelegate delegate = FascicoloDelegate.getInstance();
		CaricaVO carica = CaricaDelegate.getInstance()
				.getCaricaByUtenteAndUfficio(utente.getValueObject().getId(),
						ufficio.getId());
		form.setFascicoliReferente(delegate.getFascicoliReferentePerCruscotti(carica
				.getCaricaId()));
		form.setFascicoliIstruttore(delegate.getFascicoliIstruttorePerCruscotti(carica
				.getCaricaId()));
	}

	protected static void caricaProtocolliUfficio(CruscottoForm form,
			int registroId, int ufficioId) {
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		Map<Integer, ReportProtocolloView> protocolli = new HashMap<Integer, ReportProtocolloView>();
		form.setPosta(new HashMap<Integer, ReportProtocolloView>());
		form.setProtocolli(new HashMap<Integer, ReportProtocolloView>());
		GregorianCalendar today = new GregorianCalendar();
		protocolli = delegate.getProtocolliAssegnatiPerCruscotti(ufficioId,
				today.get(Calendar.YEAR) - 1, today.get(Calendar.YEAR), "U");

		Collection<ReportProtocolloView> val = protocolli.values();
		for (ReportProtocolloView r : val)
			if (r.getTipoProtocollo().equals("P"))
				form.getAllPosta().put(new Integer(r.getProtocolloId()), r);
			else
				form.getProtocolli().put(new Integer(r.getProtocolloId()), r);
	}

	public ActionForward downloadDocumento(ActionMapping mapping,
			DocumentoVO doc, HttpServletRequest request,
			HttpServletResponse response, int aooId) throws Exception {
		ActionMessages errors = ProtocolloFileUtility.downloadFileProtocollo(
				doc, response, aooId);
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return mapping.findForward("erroreDownload");
		}
		return null;
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = request.getSession(true);
		CruscottoForm cForm = (CruscottoForm) form;
		Organizzazione org = Organizzazione.getInstance();
		boolean ufficioCompleto = true;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		if (form == null) {
			logger.info("Creating new Cruscotto Form");
			form = new CruscottoForm();
			session.setAttribute(mapping.getAttribute(), form);
		}

		if (cForm.getUfficioCorrenteId() == 0) {
			CruscottoBO
					.impostaUfficioUtentiRoot(utente, cForm, ufficioCompleto);
		}
		if (request.getParameter("impostaUfficioAction") != null) {
			cForm.setUfficioCorrenteId(cForm.getUfficioSelezionatoId());
			CruscottoBO.impostaUfficioUtenti(utente, cForm, ufficioCompleto);
			clearSottoposto(cForm);
		} else if (request.getParameter("ufficioPrecedenteAction") != null) {
			cForm.setUfficioCorrenteId(cForm.getUfficioCorrente().getParentId());
			CruscottoBO.impostaUfficioUtenti(utente, cForm, ufficioCompleto);
			clearSottoposto(cForm);
		} else if (request.getParameter("assegnaUtenteAction") != null) {
			assegnaAdUtente(cForm);
			Utente ute = org.getUtente(cForm.getSottoposto().getUtenteId());
			caricaProtocolliUtente(cForm, ute, cForm.getUfficioCorrente());
			caricaFascicoliUtente(cForm, ute, cForm.getUfficioCorrente());
		} else if (request.getParameter("assegnaUfficioCorrenteAction") != null) {
			assegnaAdUfficio(cForm, cForm.getUfficioCorrenteId());
			caricaProtocolliUfficio(cForm, utente.getRegistroInUso(), cForm
					.getSottoposto().getUfficioId());
		} else if (request.getParameter("riassegnaProtocollo") != null) {
			request.setAttribute("protocolloId",
					new Integer(request.getParameter("riassegnaProtocollo")));
			request.setAttribute("ufficioAssegnatarioId", cForm
					.getUfficioCorrente().getId());
			CaricaVO carica = CaricaDelegate.getInstance()
					.getCaricaByUtenteAndUfficio(
							cForm.getSottoposto().getUtenteId(),
							cForm.getUfficioCorrente().getId());
			request.setAttribute("caricaAssegnatarioId", carica.getCaricaId());
			return (mapping.findForward("riassegnaProtocollo"));
		} else if (request.getParameter("btnRiassegna") != null) {
			String[] protocolloIdSelezionati = cForm.getProtocolloChkBox();
			request.setAttribute("protocolloId", new Integer(
					protocolloIdSelezionati[0]));
			session.setAttribute("protocolloIdSelezionati",
					protocolloIdSelezionati);
			request.setAttribute("ufficioAssegnatarioId", cForm
					.getUfficioCorrente().getId());
			CaricaVO carica = CaricaDelegate.getInstance()
					.getCaricaByUtenteAndUfficio(
							cForm.getSottoposto().getUtenteId(),
							cForm.getUfficioCorrente().getId());
			request.setAttribute("caricaAssegnatarioId", carica.getCaricaId());
			return (mapping.findForward("riassegnazioneMultiplaProtocollo"));
		} else if (request.getParameter("btnInoltra") != null) {
			String[] protocolloIdSelezionati = cForm.getPostaChkBox();
			request.setAttribute("protocolloId", new Integer(
					protocolloIdSelezionati[0]));
			session.setAttribute("protocolloIdSelezionati",
					protocolloIdSelezionati);
			return (mapping.findForward("riassegnazioneMultiplaPosta"));
		} else if (request.getParameter("btnRiassegnaFascicoli") != null) {
			String[] fascicoloIdSelezionati = cForm.getFascicoloReferenteChkBox();
			session.setAttribute("fascicoloIdSelezionati",
					fascicoloIdSelezionati);
			return (mapping.findForward("riassegnazioneFascicoli"));
		} else if (request.getParameter("btnResetIstruttore") != null) {
			String[] fascicoloIdSelezionati = cForm.getFascicoloIstruttoreChkBox();
			for (String id : fascicoloIdSelezionati) {
				Integer fascicoloId = Integer.valueOf(id);
				FascicoloVO fascicolo = FascicoloDelegate.getInstance()
						.getFascicoloVOById(fascicoloId);
				fascicolo.setUtenteIstruttoreId(0);
				fascicolo.setCaricaIstruttoreId(0);
				FascicoloDelegate.getInstance().salvaFascicolo(fascicolo);
				cForm.getFascicoliIstruttore().remove(fascicoloId);
			}
		} else if (request.getParameter("riassegnaPosta") != null) {
			request.setAttribute("protocolloId",
					new Integer(request.getParameter("riassegnaPosta")));
			request.setAttribute("ufficioAssegnatarioId", cForm
					.getUfficioCorrente().getId());
			return (mapping.findForward("riassegnaPosta"));
		} else if (request.getParameter("protocolloSelezionato") != null) {
			request.setAttribute("protocolloId",
					new Integer(request.getParameter("protocolloSelezionato")));
			return (mapping.findForward("visualizzaProtocolloIngresso"));
		} else if (request.getParameter("postaSelezionato") != null) {
			request.setAttribute("protocolloId",
					new Integer(request.getParameter("postaSelezionato")));
			return (mapping.findForward("visualizzaPostaInterna"));
		} else if (request.getParameter("downloadDocprotocolloSelezionato") != null) {
			Integer id = new Integer(Integer.parseInt(request
					.getParameter("downloadDocprotocolloSelezionato")));
			DocumentoVO doc = null;
			ProtocolloVO p = ProtocolloDelegate.getInstance()
					.getProtocolloVOById(id.intValue());
			int docId = p.getDocumentoPrincipaleId();
			doc = DocumentoDelegate.getInstance().getDocumento(docId);
			int aooId = utente.getAreaOrganizzativa().getId();
			return downloadDocumento(mapping, doc, request, response, aooId);

		}
		return mapping.findForward("input");
	}
}
