
package it.flosslab.mvc.presentation.action.protocollo;

import it.compit.fenice.enums.UnitaAmministrativaEnum;
import it.compit.fenice.mvc.business.EditorDelegate;
import it.compit.fenice.mvc.business.LogDelegate;
import it.compit.fenice.mvc.business.RepertorioDelegate;
import it.compit.fenice.mvc.presentation.actionform.documentale.AvvocatoGeneraleForm;
import it.compit.fenice.mvc.presentation.actionform.documentale.EditorForm;
import it.compit.fenice.mvc.presentation.actionform.log.JobScheduledLogForm;
import it.compit.fenice.mvc.presentation.actionform.protocollo.AlertProcedimentoForm;
import it.compit.fenice.mvc.presentation.actionform.protocollo.AlertProtocolloForm;
import it.compit.fenice.mvc.presentation.actionform.repertori.RepertorioForm;
import it.compit.fenice.mvc.presentation.helper.DocumentoAvvocatoGeneraleULLView;
import it.compit.fenice.mvc.presentation.helper.EditorView;
import it.compit.fenice.mvc.presentation.helper.ProtocolloProcedimentoView;
import it.compit.fenice.mvc.vo.repertori.RepertorioVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.EmailDelegate;
import it.finsiel.siged.mvc.business.ProcedimentoDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.PresaInCaricoForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolliRespintiForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ScaricoForm;
import it.finsiel.siged.mvc.presentation.helper.ProcedimentoView;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.vo.log.EventoVO;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class DashboardAction extends Action {

	static Logger logger = Logger.getLogger(DashboardAction.class.getName());

	private static final String PROTOCOLLI_ASSEGNATI_UTENTE = "assegnati_utente";
	private static final String PROTOCOLLI_ASSEGNATI_UFFICIO = "assegnati_ufficio";
	private static final String CONOSCENZA_UTENTE = "conoscenza_utente";
	private static final String CONOSCENZA_UFFICIO = "conoscenza_ufficio";
	private static final String PROTOCOLLI_RESPINTI_UTENTE = "respinti_utente";
	private static final String POSTA_INTERNA_UTENTE = "posta_interna_utente";
	private static final String POSTA_INTERNA_UFFICIO = "posta_interna_ufficio";
	private static final String PROTOCOLLI_ALERT = "protocolli_alert";
	private static final String RICORSI = "ricorsi";
	private static final String CHECK_POSTA_INTERNA = "check_pi";
	private static final String REPERTORI = "repertori";
	private static final String DA_REPERTORIARE = "da_repertoriare";
	
	//log
	private static final String LOG_PEC = "log_pec";
	private static final String LOG_JOB = "log_job";

	// fatture
	private static final String FATTURE_UFFICIO = "fatture_ufficio";
	private static final String FATTURE_UTENTE = "fatture_utente";
	private static final String FATTURE_RESPINTE = "fatture_respinte";
	private static final String DA_FIRMARE = "da_firmare";
	private static final String RELAZIONI_DECRETI = "relazioni_decreti";

	// email ufficio
	private static final String EMAIL_UFFICIO = "email_ufficio";

	// procedimenti
	private static final String PROCEDIMENTI_ISTRUTTORE = "procedimenti_istruttore";
	private static final String PROCEDIMENTI_ALERT = "procedimenti_alert";
	private static final String PROCEDIMENTI_UFFICIO_PARTECIPANTE = "procedimenti_ufficio_partecipante";
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward myforward = null;
		String myaction = mapping.getParameter();
		if ("".equalsIgnoreCase(myaction)) {
			myforward = mapping.findForward("failure");
		} else if ("SHOW".equalsIgnoreCase(myaction)) {
			myforward = performShow(mapping, form, request, response);
		} else if ("RESPINTI".equalsIgnoreCase(myaction)) {
			myforward = performRespinti(mapping, form, request, response);
		} else if ("SCARICO".equalsIgnoreCase(myaction)) {
			myforward = performScaricoView(mapping, form, request, response);
		} else if ("ASSEGNATI".equalsIgnoreCase(myaction)) {
			myforward = performAssegnatiView(mapping, form, request, response);
		} else if ("POSTA_INTERNA".equalsIgnoreCase(myaction)) {
			myforward = performPostaInternaView(mapping, form, request,
					response);
		} else if ("DA_REPERTORIARE".equalsIgnoreCase(myaction)) {
			myforward = performPostaInternaRepertorioView(mapping, form, request,
					response);
		}  else if ("PER_CONOSCENZA".equalsIgnoreCase(myaction)) {
			myforward = performConoscenzaView(mapping, form, request, response);
		} else if ("PROCEDIMENTI_ALERT".equalsIgnoreCase(myaction)) {
			myforward = performProcedimentiAlertView(mapping, form, request,
					response);
		} else if ("PROTOCOLLI_ALERT".equalsIgnoreCase(myaction)) {
			myforward = performProtocolliAlertView(mapping, form, request,
					response);
		} else if ("RICORSI".equalsIgnoreCase(myaction)) {
			myforward = performRicorsiView(mapping, form, request, response);
		} else if ("FATTURE".equalsIgnoreCase(myaction)) {
			myforward = performFattureView(mapping, form, request, response);
		} else if ("DA_FIRMARE".equalsIgnoreCase(myaction)) {
			myforward = performDocumentiDaFirmareView(mapping, form, request,
					response);
		} else if ("RELAZIONI_DECRETI".equalsIgnoreCase(myaction)) {
			myforward = performRelazioniDecretiView(mapping, form, request,
					response);
		} else if ("CHECK_POSTA_INTERNA".equalsIgnoreCase(myaction)) {
			myforward = performCheckPostaInternaView(mapping, form, request,
					response);
		} else if ("PROCEDIMENTI_ISTRUTTORE".equalsIgnoreCase(myaction)) {
			myforward = performProcedimentiIstruttoreView(mapping, form, request,response);
		} else if ("PROCEDIMENTI_UFFICIO_PARTECIPANTE".equalsIgnoreCase(myaction)) {
			myforward = performProcedimentiUfficioPartecipanteView(mapping, form, request,
					response);
		}else if ("REPERTORI".equalsIgnoreCase(myaction)) {
			myforward = performRepertoriView(mapping, form, request,
					response);
		}else if ("LOG_JOB".equalsIgnoreCase(myaction)) {
			myforward = performLogJobView(mapping, form, request,
					response);
		}
		return myforward;
	}

	private ActionForward performAssegnatiView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		String type = request.getParameter("type");
		Map<Long, ReportProtocolloView> protocolli = new HashMap<Long, ReportProtocolloView>();
		Utente utente = (Utente) request.getSession().getAttribute(
				Constants.UTENTE_KEY);
		int numberOfYear=Integer.valueOf(utente.getAreaOrganizzativa().getAnniVisibilitaBacheca());
		GregorianCalendar today = new GregorianCalendar();
		if (type.equals("utente")) {
			protocolli = delegate.getProtocolliAssegnati(utente, today
					.get(Calendar.YEAR) - numberOfYear, today.get(Calendar.YEAR), 0, "T");
		} else if (type.equals("ufficio")) {
			protocolli = delegate.getProtocolliAssegnati(utente, today
					.get(Calendar.YEAR) - numberOfYear, today.get(Calendar.YEAR), 0, "U");
		}
		PresaInCaricoForm caricoForm = (PresaInCaricoForm) form;
		caricoForm.setTipoProtocollo("in+out");
		caricoForm.setProtocolliInCarico(protocolli);
		return mapping.findForward("success");
	}

	private ActionForward performProcedimentiAlertView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ProcedimentoDelegate delegate = ProcedimentoDelegate.getInstance();
		Map<String,ProcedimentoView> procedimenti = new HashMap<String,ProcedimentoView>();
		Utente utente = (Utente) request.getSession().getAttribute(
				Constants.UTENTE_KEY);
		procedimenti = delegate.getProcedimentiAlert(utente);
		AlertProcedimentoForm procedimentiAlert = (AlertProcedimentoForm) form;
		procedimentiAlert.reset();
		procedimentiAlert.setProcedimenti(procedimenti);
		return mapping.findForward("success");
	}

	private ActionForward performProcedimentiIstruttoreView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ProcedimentoDelegate delegate = ProcedimentoDelegate.getInstance();
		Utente utente = (Utente) request.getSession().getAttribute(
				Constants.UTENTE_KEY);
		AlertProcedimentoForm procedimentoAssegnatari = (AlertProcedimentoForm) form;
		procedimentoAssegnatari.reset();
		Map<String,ProcedimentoView> procedimentiIstruttore= delegate.getProcedimentiIstruttore(utente.getCaricaInUso());
		if(procedimentiIstruttore!=null){
			procedimentoAssegnatari.setProcedimenti(procedimentiIstruttore);
		}
		return mapping.findForward("success");
	}
	
	private ActionForward performProcedimentiUfficioPartecipanteView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ProcedimentoDelegate delegate = ProcedimentoDelegate.getInstance();
		Utente utente = (Utente) request.getSession().getAttribute(
				Constants.UTENTE_KEY);
		AlertProcedimentoForm procedimentoAssegnatari = (AlertProcedimentoForm) form;
		procedimentoAssegnatari.reset();
		Map<String,ProcedimentoView> procedimentiUfficioPartecipante= delegate.getProcedimentiUfficioPartecipante(utente.getUfficioInUso());
		if(procedimentiUfficioPartecipante!=null){
			procedimentoAssegnatari.setProcedimenti(procedimentiUfficioPartecipante);

		}
		return mapping.findForward("success");
	}
	
	private ActionForward performRepertoriView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		RepertorioDelegate delegate = RepertorioDelegate.getInstance();
		Utente utente = (Utente) request.getSession().getAttribute(
				Constants.UTENTE_KEY);
		Collection<RepertorioVO> repertori = delegate.getRepertoriByFlagWeb(utente.getAreaOrganizzativa().getId(),RepertorioVO.REPERTORIO_INTERNO);
		RepertorioForm repForm = (RepertorioForm) form;
		repForm.resetForm();
		repForm.setRepertori(repertori);
		return mapping.findForward("success");
	}
	
	private ActionForward performLogJobView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		LogDelegate delegate = LogDelegate.getInstance();
		Utente utente = (Utente) request.getSession().getAttribute(
				Constants.UTENTE_KEY);
		Collection<EventoVO> logs = delegate.getJobScheduledLogsByStatus(utente.getAreaOrganizzativa().getId(), EventoVO.NON_INVIATO);
		JobScheduledLogForm jsForm = (JobScheduledLogForm) form;
		jsForm.setLogs(logs);
		return mapping.findForward("success");
	}
	
	//TODO
	private ActionForward performRicorsiView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Utente utente = (Utente) request.getSession().getAttribute(
				Constants.UTENTE_KEY);
		Map<String,ProcedimentoView> procedimenti = ProcedimentoDelegate.getInstance()
				.getRicorsi(utente);
		Map<String,ProtocolloProcedimentoView> evidenze = ProtocolloDelegate.getInstance().getProtocolliEvidenza(
				utente);
		AlertProcedimentoForm ricorsi = (AlertProcedimentoForm) form;
		ricorsi.reset();
		if (procedimenti != null)
			ricorsi.setProcedimentiULL(procedimenti);
		if (evidenze != null)
			ricorsi.setProtocolliEvidenza(evidenze);
		return mapping.findForward("success");
	}

	private ActionForward performProtocolliAlertView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		Map<String,ReportProtocolloView> protocolli = new HashMap<String,ReportProtocolloView>();
		Utente utente = (Utente) request.getSession().getAttribute(
				Constants.UTENTE_KEY);
		protocolli = delegate.getProtocolliAlert(utente);
		AlertProtocolloForm protocolliAlert = (AlertProtocolloForm) form;
		protocolliAlert.setProtocolli(protocolli);
		return mapping.findForward("success");
	}

	private ActionForward performConoscenzaView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		String type = request.getParameter("type");
		Map<Long,ReportProtocolloView> protocolli = new HashMap<Long,ReportProtocolloView>();
		Utente utente = (Utente) request.getSession().getAttribute(
				Constants.UTENTE_KEY);
		ScaricoForm sform = (ScaricoForm) form;
		
		if (type.equals("utente")) {
			protocolli = delegate.getProtocolliPerConoscenza(utente, "T");
			sform.setUfficio(false);

		}
		if (type.equals("ufficio")) {
			protocolli = delegate.getProtocolliPerConoscenza(utente, "U");
			sform.setUfficio(true);			
		}
		sform.setProtocolliScarico(protocolli);
		return mapping.findForward("success");
	}

	private ActionForward performCheckPostaInternaView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		Utente utente = (Utente) request.getSession().getAttribute(
				Constants.UTENTE_KEY);
		ScaricoForm sform = (ScaricoForm) form;
		sform.setCheckPI(delegate.getNotifichePostaInternaView(utente
				.getCaricaInUso(), 0));
		return mapping.findForward("success");
	}

	private ActionForward performPostaInternaView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		String type = request.getParameter("type");
		Map<Long,ReportProtocolloView> protocolli = new HashMap<Long,ReportProtocolloView>();
		ScaricoForm sform = (ScaricoForm) form;
		Utente utente = (Utente) request.getSession().getAttribute(Constants.UTENTE_KEY);
		if (type.equals("utente")) {
			protocolli = delegate.getPostaInternaAssegnata(utente, "T");
			sform.setUfficio(false);
		}
		if (type.equals("ufficio")) {
			protocolli = delegate.getPostaInternaAssegnata(utente, "U");
			sform.setUfficio(true);
		}

		sform.setProtocolliScarico(protocolli);
		sform.setTipoProtocollo("posta");
		return mapping.findForward("success");
	}
	
	private ActionForward performPostaInternaRepertorioView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		Map<Long,ReportProtocolloView> protocolli = new HashMap<Long,ReportProtocolloView>();
		ScaricoForm sform = (ScaricoForm) form;
		Utente utente = (Utente) request.getSession().getAttribute(Constants.UTENTE_KEY);
		protocolli = delegate.getPostaInternaRepertorio(utente);
		sform.setUfficio(false);
		sform.setProtocolliScarico(protocolli);
		sform.setTipoProtocollo("posta");
		return mapping.findForward("success");
	}

	private ActionForward performFattureView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		String type = request.getParameter("type");
		Map<Long,ReportProtocolloView> protocolli = new HashMap<Long,ReportProtocolloView>();
		ScaricoForm sform = (ScaricoForm) form;
		Utente utente = (Utente) request.getSession().getAttribute(
				Constants.UTENTE_KEY);
		RegistroVO fatt = Organizzazione.getInstance().getRegistroByCod("Fatt");
		if (type.equals("utente")) {
			protocolli = delegate.getFatture(utente, fatt.getId(), "T");
			sform.setTipoUtenteUfficio("T");
		}
		if (type.equals("ufficio")) {
			protocolli = delegate.getFatture(utente, fatt.getId(), "U");
			sform.setTipoUtenteUfficio("U");
		}
		if (type.equals("respinte")) {
			protocolli = delegate.getFatture(utente, fatt.getId(), "R");
			sform.setTipoUtenteUfficio("R");
		}

		sform.setProtocolliScarico(protocolli);
		return mapping.findForward("success");
	}

	private ActionForward performScaricoView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		String type = request.getParameter("type");
		Map<Long,ReportProtocolloView> protocolli = new HashMap<Long,ReportProtocolloView>();
		Utente utente = (Utente) request.getSession().getAttribute(Constants.UTENTE_KEY);
		int numberOfYear=Integer.valueOf(utente.getAreaOrganizzativa().getAnniVisibilitaBacheca());
		
		GregorianCalendar today = new GregorianCalendar();
		ScaricoForm scaricoForm = (ScaricoForm) form;
		if (type.equals("carico")) {
			scaricoForm.setStatoProtocollo("N");
			protocolli = delegate.getProtocolliAssegnati(utente, today
					.get(Calendar.YEAR) - numberOfYear, today.get(Calendar.YEAR), 0, "T");

		}
		scaricoForm.setProtocolliScarico(protocolli);
		scaricoForm.setTipoProtocollo("in+out");
		return mapping.findForward("success");
	}

	private ActionForward performDocumentiDaFirmareView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		EditorDelegate delegate = EditorDelegate.getInstance();
		Utente utente = (Utente) request.getSession().getAttribute(
				Constants.UTENTE_KEY);
		Collection<EditorView> doc = delegate.getDocumentiTemplateByCarica(utente.getCaricaInUso());
		EditorForm eForm = (EditorForm) form;
		eForm.setDocumenti(doc);
		return mapping.findForward("success");
	}

	private ActionForward performRelazioniDecretiView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		EditorDelegate delegate = EditorDelegate.getInstance();
		Utente utente = (Utente) request.getSession().getAttribute(Constants.UTENTE_KEY);
		Map<Integer, DocumentoAvvocatoGeneraleULLView> doc= delegate.getDocumentiAvvocatoGeneraleULL(utente.getCaricaInUso(),null);
		AvvocatoGeneraleForm eForm = (AvvocatoGeneraleForm) form;
		eForm.setFaseRelatoria(delegate.contaDocumentiAvvocatoGeneraleULL(utente.getCaricaInUso(),4)>0?true:false);
		eForm.setFaseDecretoPresidente(delegate.contaDocumentiAvvocatoGeneraleULL(utente.getCaricaInUso(),6)>0?true:false);
		eForm.setDocumenti(doc);
		return mapping.findForward("success");
	}
	
	private ActionForward performRespinti(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		Utente utente = (Utente) request.getSession().getAttribute(
				Constants.UTENTE_KEY);
		int numberOfYear=Integer.valueOf(utente.getAreaOrganizzativa().getAnniVisibilitaBacheca());
		GregorianCalendar today = new GregorianCalendar();
		Map<Long, ReportProtocolloView> respinti = delegate.getProtocolliRespinti(utente, today
				.get(Calendar.YEAR) - numberOfYear, today.get(Calendar.YEAR), 0, 0, null,
				null);
		ProtocolliRespintiForm riassegnazione = (ProtocolliRespintiForm) form;
		riassegnazione.setProtocolliRifiutati(respinti);
		return mapping.findForward("success");
	}

	private ActionForward performShow(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		Utente utente = (Utente) request.getSession().getAttribute(
				Constants.UTENTE_KEY);
		GregorianCalendar today = new GregorianCalendar();
		int numberOfYear=Integer.valueOf(utente.getAreaOrganizzativa().getAnniVisibilitaBacheca());
		int protocolli_alert = 0;
		int posta_interna_ufficio = 0;
		int assegnati_ufficio = 0;
		int conoscenza_ufficio=0;
		
		int log_job=0;
		
		if (utente.getCaricaVOInUso().isReferente()) {
			posta_interna_ufficio = delegate.contaPostaInternaAssegnata(utente,
					"U");
			conoscenza_ufficio = delegate.contaPerConoscenza(utente,"U");
			assegnati_ufficio = delegate.contaProtocolliAssegnati(utente, today
					.get(Calendar.YEAR) - numberOfYear, today.get(Calendar.YEAR), 0, "U");
			protocolli_alert = ProtocolloDelegate.getInstance()
					.contaProtocolliAlert(utente);
		}
		int assegnati_utente = delegate.contaProtocolliAssegnati(utente, today
				.get(Calendar.YEAR) - numberOfYear, today.get(Calendar.YEAR), 0, "T");
		int respinti_utente = delegate.contaProtocolliRespintiUtente(utente,
				today.get(Calendar.YEAR) - numberOfYear, today.get(Calendar.YEAR), 0, 0,
				null, null);
		int posta_interna_utente = delegate.contaPostaInternaAssegnata(utente,
				"T");
		int da_repertoriare = delegate.contaPostaInternaRepertorio(utente);
		int conoscenza_utente = delegate.contaPerConoscenza(utente, "T");
		int procedimenti_alert = ProcedimentoDelegate.getInstance()
				.contaProcedimentiAlert(utente);

		int ricorsi = ProcedimentoDelegate.getInstance().contaRicorsi(utente) + delegate.contaProtocolliEvidenza(utente);

		// fatture
		int fatture_ufficio = 0;
		int fatture_utente = 0;
		int fatture_respinte = 0;
		// parametro distintivo per il policlinico
		if (Organizzazione.getInstance().getValueObject().getUnitaAmministrativa()==UnitaAmministrativaEnum.POLICLINICO_CT) {
			RegistroVO rFatture = Organizzazione.getInstance()
					.getRegistroByCod("Fatt");
			if (rFatture != null) {
				fatture_ufficio = delegate.contaFattureUfficio(utente, rFatture
						.getId(), "U");
				fatture_utente = delegate.contaFattureUtente(utente, rFatture
						.getId(), "T");
				fatture_respinte = delegate.contaFattureRespinte(utente,
						rFatture.getId(), "R");
			}
		}

		int da_firmare=0;
		int doc_avvocatoGenerale=0;
		
		if(utente.getCaricaVOInUso().isResponsabileEnte())
			doc_avvocatoGenerale = EditorDelegate.getInstance().contaDocumentiAvvocatoGeneraleULL(utente.getCaricaInUso(),null);
		else
			da_firmare = EditorDelegate.getInstance().contaDocumentiTemplateByCarica(utente.getCaricaInUso());
		
		int check_posta_interna = delegate.contaNotifichePostaInternaView(
				utente.getCaricaInUso(), 0);
		int email_ufficio = 0;
		if (utente.getMailConfig().isPnAbilitata())
			email_ufficio = EmailDelegate.getInstance().countMessaggiUfficioDaProtocollare(utente.getUfficioInUso());
		
		int procedimenti_istruttore = ProcedimentoDelegate.getInstance().contaProcedimentiIstruttore(utente.getCaricaInUso());
		int procedimenti_ufficio_partecipante = ProcedimentoDelegate.getInstance().contaProcedimentiUfficioPartecipante(utente.getUfficioInUso());
		
		int repertori=RepertorioDelegate.getInstance().contaRepertoriByFlagWeb(utente.getAreaOrganizzativa().getId(),RepertorioVO.REPERTORIO_INTERNO);
		EventoVO evento=EmailDelegate.getInstance().getMailLog(utente.getAreaOrganizzativa().getId());
		
		log_job=LogDelegate.getInstance().countJobScheduledLogsByStatus(utente.getAreaOrganizzativa().getId(), EventoVO.NON_INVIATO);
		
		
		request.setAttribute(PROTOCOLLI_ASSEGNATI_UFFICIO, Integer
				.toString(assegnati_ufficio));
		request.setAttribute(PROTOCOLLI_ASSEGNATI_UTENTE, Integer
				.toString(assegnati_utente));
		request.setAttribute(PROTOCOLLI_RESPINTI_UTENTE, Integer
				.toString(respinti_utente));
		
		request.setAttribute(POSTA_INTERNA_UTENTE, Integer
				.toString(posta_interna_utente));
		request.setAttribute(POSTA_INTERNA_UFFICIO, Integer
				.toString(posta_interna_ufficio));
		request.setAttribute(DA_REPERTORIARE, Integer.toString(da_repertoriare));
		
		request.setAttribute(CONOSCENZA_UTENTE, Integer
				.toString(conoscenza_utente));
		request.setAttribute(CONOSCENZA_UFFICIO, Integer
				.toString(conoscenza_ufficio));
		
		request.setAttribute(PROCEDIMENTI_ALERT, Integer
				.toString(procedimenti_alert));
		request.setAttribute(PROTOCOLLI_ALERT, Integer
				.toString(protocolli_alert));
		request.setAttribute(RICORSI, Integer.toString(ricorsi));
		request
				.setAttribute(FATTURE_UFFICIO, Integer
						.toString(fatture_ufficio));
		request.setAttribute(FATTURE_UTENTE, Integer.toString(fatture_utente));
		request.setAttribute(FATTURE_RESPINTE, Integer
				.toString(fatture_respinte));
		request.setAttribute(DA_FIRMARE, Integer.toString(da_firmare));
		request.setAttribute(RELAZIONI_DECRETI, Integer.toString(doc_avvocatoGenerale));
		
		request.setAttribute(EMAIL_UFFICIO, Integer.toString(email_ufficio));
		request.setAttribute(CHECK_POSTA_INTERNA, Integer
				.toString(check_posta_interna));
		request.setAttribute(PROCEDIMENTI_ISTRUTTORE, Integer
				.toString(procedimenti_istruttore));
		request.setAttribute(PROCEDIMENTI_UFFICIO_PARTECIPANTE, Integer
				.toString(procedimenti_ufficio_partecipante));
		request.setAttribute(REPERTORI, Integer
				.toString(repertori));
		request.setAttribute(LOG_PEC, evento);
		request.setAttribute(LOG_JOB, Integer.toString(log_job));
		return mapping.findForward("success");
	}

}