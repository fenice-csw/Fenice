package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.presentation.actionform.documentale.EditorForm;
import it.compit.fenice.mvc.presentation.actionform.protocollo.PostaInternaForm;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.model.Fascicolo;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.AlberoUfficiBO;
import it.finsiel.siged.mvc.bo.TitolarioBO;
import it.finsiel.siged.mvc.business.DocumentoDelegate;
import it.finsiel.siged.mvc.business.FascicoloDelegate;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.documentale.DocumentoForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.FaldoneForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.FascicoloForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProcedimentoForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloIngressoForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloUscitaForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.RicercaFascicoliForm;
import it.finsiel.siged.mvc.presentation.helper.FascicoloView;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.report.protocollo.CommonReportDS;
import it.finsiel.siged.util.NumberUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class RicercaFascicoliAction extends Action {

	// ----------------------------------------------------- Instance Variables

	/**
	 * The <code>Log</code> instance for this application.
	 */
	static Logger logger = Logger.getLogger(RicercaFascicoliAction.class
			.getName());

	// --------------------------------------------------------- Public Methods

	public void stampaReport(HttpServletRequest request,
			HttpServletResponse response, Collection<FascicoloView> c, String subTitle)
			throws IOException, ServletException {
		ServletContext context = this.getServlet().getServletContext();
		//HttpSession session = request.getSession();
		OutputStream os = response.getOutputStream();

		try {
			File reportFile = new File(context.getRealPath("/")
					+ FileConstants.STAMPA_RICERCA_FASCICOLI_REPORT_TEMPLATE);
			JasperDesign jasperDesign = JRXmlLoader.load(context
					.getRealPath("/")
					+ FileConstants.STAMPA_RICERCA_FASCICOLI_REPORT_TEMPLATE);
			JasperReport jasperReport = JasperCompileManager
					.compileReport(jasperDesign);
			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("ReportTitle", "Report Ricerca Fascicoli");
			parameters.put("ReportSubTitle", subTitle);
			parameters.put("BaseDir", reportFile.getParentFile());
			//Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
			CommonReportDS ds = new CommonReportDS(c,FascicoloView.class);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			response.setHeader("Content-Disposition",
					"attachment;filename=Ricerca_Fascicoli.pdf");
			response.setHeader("Cache-control", "");
			response.setContentType("application/pdf");
			JRPdfExporter exporter = new JRPdfExporter();
			exporter
					.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
			exporter.exportReport();
		} catch (Exception e) {
			logger.error("", e);
			response.setContentType("text/plain");
			e.printStackTrace(new PrintStream(os));
		} finally {
			os.close();
		}
	}
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession();
		RicercaFascicoliForm ricercaFascicoliForm = (RicercaFascicoliForm) form;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		Organizzazione org = Organizzazione.getInstance();
		boolean preQuery = false;
		boolean ufficioCompleto = (utente.getUfficioVOInUso().getTipo()
				.equals(UfficioVO.UFFICIO_CENTRALE));
		session.setAttribute("ricercaFascicoliForm", ricercaFascicoliForm);
		if (Boolean.TRUE.equals(session.getAttribute("tornaProtocollo"))
				|| Boolean.TRUE.equals(session.getAttribute("tornaFascicolo"))
				|| Boolean.TRUE.equals(session.getAttribute("tornaDocumento"))
				|| Boolean.TRUE.equals(session.getAttribute("tornaFaldone"))
				|| Boolean.TRUE.equals(session.getAttribute("tornaProcedimento"))
				|| Boolean.TRUE.equals(session.getAttribute("tornaEditor"))
				|| Boolean.TRUE.equals(session.getAttribute("tornaTemplate"))) {
			ricercaFascicoliForm.setAggiungiFascicolo(true);

		}

		if (request.getAttribute("resetForm") != null) {
			request.removeAttribute("resetForm");
			ricercaFascicoliForm.inizializzaForm();
		}
		if (form == null) {
			logger.info(" Creating new RicercaFascicoliAction");
			form = new RicercaFascicoliForm();
			ricercaFascicoliForm.inizializzaForm();
			request.setAttribute(mapping.getAttribute(), form);
		}
		if (ricercaFascicoliForm.getUfficioCorrenteId() == 0) {
			ricercaFascicoliForm.setAooId(utente.getValueObject().getAooId());
			AlberoUfficiBO.impostaUfficioUtenti(utente, ricercaFascicoliForm,
					ufficioCompleto);
			impostaTitolario(ricercaFascicoliForm, utente, 0);
		}
		if (request.getParameter("impostaUfficioAction") != null) {
			ricercaFascicoliForm.setUfficioCorrenteId(ricercaFascicoliForm
					.getUfficioSelezionatoId());
			AlberoUfficiBO.impostaUfficioUtenti(utente, ricercaFascicoliForm,
					ufficioCompleto);
			impostaTitolario(ricercaFascicoliForm, utente, 0);
			return mapping.findForward("input");
		} else if (request.getParameter("ufficioPrecedenteAction") != null) {
			ricercaFascicoliForm.setUfficioCorrenteId(ricercaFascicoliForm
					.getUfficioCorrente().getParentId());
			AlberoUfficiBO.impostaUfficioUtenti(utente, ricercaFascicoliForm,
					ufficioCompleto);
			impostaTitolario(ricercaFascicoliForm, utente, 0);
			return mapping.findForward("input");
		} else if (request.getParameter("impostaTitolarioAction") != null) {
			if (ricercaFascicoliForm.getTitolario() != null) {
				ricercaFascicoliForm
						.setTitolarioPrecedenteId(ricercaFascicoliForm
								.getTitolario().getId().intValue());
			}
			if (ricercaFascicoliForm.isTuttiUffici())
				TitolarioBO.impostaTitolario(ricercaFascicoliForm, 0,
						ricercaFascicoliForm.getTitolarioSelezionatoId());
			else
				TitolarioBO.impostaTitolario(ricercaFascicoliForm,
						ricercaFascicoliForm.getUfficioCorrenteId(),
						ricercaFascicoliForm.getTitolarioSelezionatoId());
			return mapping.findForward("input");
		} else if (request.getParameter("titolarioPrecedenteAction") != null) {
			if (ricercaFascicoliForm.isTuttiUffici())
				TitolarioBO.impostaTitolario(ricercaFascicoliForm, 0,
						ricercaFascicoliForm.getTitolarioPrecedenteId());
			else
				TitolarioBO.impostaTitolario(ricercaFascicoliForm,
						ricercaFascicoliForm.getUfficioCorrenteId(),
						ricercaFascicoliForm.getTitolarioPrecedenteId());
			if (ricercaFascicoliForm.getTitolario() != null) {
				ricercaFascicoliForm
						.setTitolarioPrecedenteId(ricercaFascicoliForm
								.getTitolario().getParentId());
			}
			return mapping.findForward("input");
		} else if (request.getParameter("annullaAction") != null || preQuery) {
			ricercaFascicoliForm.inizializzaForm();
			Ufficio uff = org.getUfficio(utente.getUfficioInUso());
			ricercaFascicoliForm.setUfficioCorrenteId(utente.getUfficioInUso());
			AlberoUfficiBO.impostaUfficio(utente, ricercaFascicoliForm,
					ufficioCompleto);
			impostaTitolario(ricercaFascicoliForm, utente, 0);
			ricercaFascicoliForm.setUfficioCorrente(uff.getValueObject());
			session.setAttribute(mapping.getAttribute(), ricercaFascicoliForm);
			if (!(preQuery)) {
				return (mapping.findForward("input"));
			}
		} else if (request.getParameter("btnCercaFascicoli") != null
				|| preQuery == true
				|| request.getParameter("cercaFascicoliDaFaldoni") != null
				|| request.getParameter("cercaFascicoliDaProcedimento") != null) {
			// parametri di ricerca
			Date dataAperturaDa = null;
			Date dataAperturaA = null;
			Date dataEvidenzaDa = null;
			Date dataEvidenzaA = null;
			String progressivo = null;
			int anno = 0;
			int referenteId = 0;
			int istruttoreId = 0;
			String capitolo = null;
			String comune = null;
			String interessatoDelegato = null;
			String collocazioneValore1 = null;
			String collocazioneValore2 = null;
			String collocazioneValore3 = null;
			String collocazioneValore4 = null;
			StringBuffer rep = new StringBuffer();
			AlberoUfficiBO.impostaUfficio(utente, ricercaFascicoliForm,
					ufficioCompleto);
			if (request.getParameter("progressivo") != null && !"".equals(request.getParameter("progressivo").trim())){
				progressivo = request.getParameter("progressivo").trim();
				rep.append(" Progressivo="+progressivo+";");
			}
			if (request.getParameter("anno") != null && !"".equals(request.getParameter("anno"))) {
				anno = new Integer(request.getParameter("anno")).intValue();
				rep.append(" Anno="+progressivo+";");
			}
			String oggetto = getOggettoSearch(request, ricercaFascicoliForm);
			if(oggetto!=null && !oggetto.trim().equals(""))
				rep.append(" Oggetto="+oggetto+";");
			String note = request.getParameter("noteFascicolo");
			if(note!=null && !note.trim().equals(""))
				rep.append(" Note="+note+";");
			String stato = "-1";
			if (request.getParameter("stato") != null) {
				stato = request.getParameter("stato");
			}
			if (ricercaFascicoliForm.isAggiungiFascicolo()) {
				stato = "0";
			}
			if(stato.equals("-1"))
				rep.append(" Stato: Tutti;");
			else
				rep.append(" Stato:"+LookupDelegate.getStatiFascicolo().get(Integer.valueOf(stato))+";");
			//ricercaFascicoliForm.getStatiFascicolo()
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			if (request.getParameter("dataAperturaDa") != null
					&& !"".equals(request.getParameter("dataAperturaDa").trim())) {
				dataAperturaDa = df.parse(request.getParameter("dataAperturaDa"));
				rep.append(" Data apertura da:"+ricercaFascicoliForm.getDataAperturaDa()+";");

			}
			if (request.getParameter("dataAperturaA") != null
					&& !"".equals(request.getParameter("dataAperturaA").trim())) {
				dataAperturaA = new Date(df.parse(
						request.getParameter("dataAperturaA")).getTime()
						+ Constants.GIORNO_MILLISECONDS - 1);
				rep.append(" Data apertura da:"+ricercaFascicoliForm.getDataAperturaA()+";");
			}
			if (request.getParameter("dataEvidenzaDa") != null
					&& !"".equals(request.getParameter("dataEvidenzaDa"))) {
				dataEvidenzaDa = df.parse(request.getParameter("dataEvidenzaDa").trim());
				rep.append(" Data evidenza da:"+ricercaFascicoliForm.getDataEvidenzaDa()+";");
			}
			
			if (request.getParameter("dataEvidenzaA") != null
					&& !"".equals(request.getParameter("dataEvidenzaA").trim())) {
				dataEvidenzaA = new Date(df.parse(
						request.getParameter("dataEvidenzaA")).getTime()
						+ Constants.GIORNO_MILLISECONDS - 1);
				rep.append(" Data evidenza A:"+ricercaFascicoliForm.getDataEvidenzaA()+";");

			}
			int titolarioId = 0;
			if (ricercaFascicoliForm.getTitolario() != null) {
				titolarioId = ricercaFascicoliForm.getTitolario().getId().intValue();
				rep.append(" Titolario:"+ricercaFascicoliForm.getTitolario().getDescrizione()+";");

			}
			int ufficioId = 0;
			if (!ricercaFascicoliForm.isTuttiUffici() && ricercaFascicoliForm.getUfficioCorrenteId() > 0) {
				ufficioId = ricercaFascicoliForm.getUfficioCorrenteId();
				rep.append(" Ufficio:"+ricercaFascicoliForm.getUfficioCorrente().getDescription()+";");

			}else
				rep.append(" Ufficio: Tutti;");
			if (ricercaFascicoliForm.getUtenteSelezionatoId() > 0) {
				CaricaVO vo = CaricaDelegate.getInstance()
						.getCaricaByUtenteAndUfficio(
								ricercaFascicoliForm.getUtenteSelezionatoId(),
								ricercaFascicoliForm.getUfficioCorrenteId());
				referenteId = vo.getCaricaId();
				rep.append(" Referente:"+Organizzazione.getInstance().getUtente(ricercaFascicoliForm.getUtenteSelezionatoId()).getValueObject().getCognomeNome()+";");
			}
			if (ricercaFascicoliForm.getUtenteIstruttoreSelezionatoId() > 0) {
				CaricaVO vo = CaricaDelegate.getInstance()
						.getCaricaByUtenteAndUfficio(
								ricercaFascicoliForm
										.getUtenteIstruttoreSelezionatoId(),
								ricercaFascicoliForm.getUfficioCorrenteId());
				istruttoreId = vo.getCaricaId();
				rep.append(" Istruttore:"+Organizzazione.getInstance().getUtente(ricercaFascicoliForm.getUtenteIstruttoreSelezionatoId()).getValueObject().getCognomeNome()+";");
			}
			if (ricercaFascicoliForm.getDescrizioneInteressatoDelegato() != null
					&& !ricercaFascicoliForm
							.getDescrizioneInteressatoDelegato().trim().equals(
									"")) {
				interessatoDelegato=ricercaFascicoliForm.getDescrizioneInteressatoDelegato();
				rep.append(" Interessato/Delegato:"+ricercaFascicoliForm.getDescrizioneInteressatoDelegato()+";");

			}
			if (ricercaFascicoliForm.getComune() != null
					&& !ricercaFascicoliForm.getComune().trim().equals("")) {
				comune = ricercaFascicoliForm.getComune();
				rep.append(" Comune:"+comune+";");

			}
			if (ricercaFascicoliForm.getCapitolo() != null
					&& !ricercaFascicoliForm.getCapitolo().trim().equals("")) {
				capitolo = ricercaFascicoliForm.getCapitolo();
				rep.append(" Capitolo:"+capitolo+";");
			}
			if (ricercaFascicoliForm.getCollocazioneValore1() != null
					&& !ricercaFascicoliForm.getCollocazioneValore1().trim().equals("")) {
				collocazioneValore1 = ricercaFascicoliForm.getCollocazioneValore1();
				rep.append(" CollocazioneValore1:"+collocazioneValore1+";");
			}
			if (ricercaFascicoliForm.getCollocazioneValore2() != null
					&& !ricercaFascicoliForm.getCollocazioneValore2().trim().equals("")) {
				collocazioneValore2 = ricercaFascicoliForm.getCollocazioneValore2();
				rep.append(" CollocazioneValore2:"+collocazioneValore2+";");
			}
			if (ricercaFascicoliForm.getCollocazioneValore3() != null
					&& !ricercaFascicoliForm.getCollocazioneValore3().trim().equals("")) {
				collocazioneValore3 = ricercaFascicoliForm.getCollocazioneValore3();
				rep.append(" CollocazioneValore3:"+collocazioneValore3+";");
			}
			if (ricercaFascicoliForm.getCollocazioneValore4() != null
					&& !ricercaFascicoliForm.getCollocazioneValore4().trim().equals("")) {
				collocazioneValore4 = ricercaFascicoliForm.getCollocazioneValore4();
				rep.append(" CollocazioneValore4:"+collocazioneValore4+";");
			}
			int maxRighe=Integer.parseInt(utente.getAreaOrganizzativa().getMaxRighe());
			FascicoloDelegate fascicoloDelegate = FascicoloDelegate
					.getInstance();
			int conta = fascicoloDelegate.contaFascicoli(utente, progressivo,
					anno, oggetto, note, stato, titolarioId, dataAperturaDa,
					dataAperturaA, dataEvidenzaDa, dataEvidenzaA, ufficioId,
					referenteId, istruttoreId, interessatoDelegato, comune, capitolo, collocazioneValore1, collocazioneValore2, collocazioneValore3, collocazioneValore4);
			ricercaFascicoliForm.setFascicoli(null);
			if (conta == 0) {
				errors.add("nessun_dato", new ActionMessage("nessun_dato", "",
						""));
			} else if (conta <= maxRighe) {
				ricercaFascicoliForm.setFascicoli(FascicoloDelegate
						.getInstance().getFascicoli(utente, progressivo, anno,
								oggetto, note, stato, titolarioId,
								dataAperturaDa, dataAperturaA, dataEvidenzaDa,
								dataEvidenzaA, ufficioId, referenteId,
								istruttoreId, interessatoDelegato, comune, capitolo, collocazioneValore1, collocazioneValore2, collocazioneValore3, collocazioneValore4));
				ricercaFascicoliForm.setReportSubTitle(rep.toString());
				return (mapping.findForward("input"));
			} else {
				errors.add("controllo.maxrighe", new ActionMessage(
						"controllo.maxrighe", "" + conta, "fascicoli", ""
								+ maxRighe));
			}
			if (!errors.isEmpty())
				saveErrors(request, errors);
			return (mapping.findForward("input"));
		}
			else if (request.getParameter("btnStampa") != null) {
				stampaReport(request, response, ricercaFascicoliForm.getFascicoli(), ricercaFascicoliForm.getReportSubTitle());
				return null;
			}
		 else if (request.getParameter("btnAnnulla") != null) {
			if (Boolean.TRUE.equals(session.getAttribute("tornaFascicolo"))) {
				FascicoloForm f = (FascicoloForm) session
						.getAttribute("fascicoloFiglio");
				session.removeAttribute("tornaFascicolo");
				session.removeAttribute("fascicoloFiglio");
				session.setAttribute("fascicoloForm", f);
				return (mapping.findForward("tornaFascicolo"));
			} else if (Boolean.TRUE.equals(session
					.getAttribute("tornaFascicoloCollegato"))) {
				FascicoloForm f = (FascicoloForm) session
						.getAttribute("fascicoloAllaccio");
				session.removeAttribute("tornaFascicoloCollegato");
				session.removeAttribute("fascicoloAllaccio");
				session.setAttribute("fascicoloForm", f);
				return (mapping.findForward("tornaCollegato"));
			} else if (Boolean.TRUE.equals(session
					.getAttribute("tornaProtocollo"))) {
				session.removeAttribute("tornaProtocollo");
				ProtocolloForm pForm = (ProtocolloForm) session
						.getAttribute("protocolloForm");

				if (pForm instanceof ProtocolloIngressoForm) {
					ProtocolloIngressoForm piForm = (ProtocolloIngressoForm) pForm;
					if (piForm.isDaBtnModifica())
						return (mapping
								.findForward("tornaProtocolloIngressoNew"));
					else
						return (mapping.findForward("tornaProtocolloIngresso"));
					// return (mapping.findForward("tornaProtocolloIngresso"));
				} else if (pForm instanceof ProtocolloUscitaForm) {
					if (session.getAttribute("tornaFascicolaUscitaEdit") != null) {
						session.removeAttribute("tornaFascicolaUscitaEdit");
						return (mapping
								.findForward("tornaFascicolaProtocolloUscita"));
					} else
						return (mapping.findForward("tornaProtocolloUscita"));
				} else {
					PostaInternaForm postaForm = (PostaInternaForm) pForm;
					if (postaForm.getProtocolloId() == 0
							|| postaForm.isDaBtnModifica())
						return (mapping.findForward("tornaPostaInternaNew"));
					else
						return (mapping.findForward("tornaPostaInterna"));
				}
			} else if (Boolean.TRUE.equals(session.getAttribute("tornaEditor"))) {
				session.removeAttribute("tornaEditor");
				return (mapping.findForward("tornaInvioProtocolloEditor"));
			} else if (Boolean.TRUE.equals(session
					.getAttribute("tornaTemplate"))) {
				session.removeAttribute("tornaTemplate");
				return (mapping.findForward("tornaTemplate"));
			} else if (Boolean.TRUE.equals(session
					.getAttribute("tornaDocumento"))) {
				session.removeAttribute("tornaDocumento");
				return (mapping.findForward("tornaDocumento"));
			} else if (Boolean.TRUE
					.equals(session.getAttribute("tornaFaldone"))) {
				session.removeAttribute("tornaFaldone");
				return (mapping.findForward("tornaFaldone"));
			} else if (Boolean.TRUE.equals(session
					.getAttribute("tornaProcedimento"))) {
				session.removeAttribute("tornaProcedimento");
				return (mapping.findForward("tornaProcedimento"));
			}

		} else if (request.getParameter("btnReset") != null) {
			ricercaFascicoliForm.inizializzaForm();
			TitolarioBO.impostaTitolario(ricercaFascicoliForm, utente
					.getUfficioInUso(), ricercaFascicoliForm
					.getTitolarioSelezionatoId());
			return mapping.findForward("input");
		} else if (request.getParameter("btnAnnullaRicerca") != null) {
			session.removeAttribute("tornaProtocollo");
			session.removeAttribute("tornaEditor");
			session.removeAttribute("tornaTemplate");
			session.removeAttribute("tornaFascicolo");
			session.removeAttribute("tornaFascicoloCollegato");
			session.removeAttribute("tornaDocumento");
			session.removeAttribute("tornaFaldone");
			session.removeAttribute("cercaFascicoliDaFaldoni");
			session.removeAttribute("tornaProcedimento");
			session.removeAttribute("elencoProtocolliDaProcedimento");
			ricercaFascicoliForm.inizializzaForm();
			if (ricercaFascicoliForm.getTitolario() == null) {
				TitolarioBO.impostaTitolario(ricercaFascicoliForm, utente
						.getUfficioInUso(), 0);
			}
			return (mapping.findForward("input"));

		} else if (request.getParameter("protocolloSelezionato") != null) {
			Integer protId = new Integer(request
					.getParameter("protocolloSelezionato"));
			request.setAttribute("protocolloId", protId);
			ProtocolloDelegate pd = ProtocolloDelegate.getInstance();
			ProtocolloVO v = pd.getProtocolloVOById(protId.intValue());
			String tipoProt = v.getFlagTipo();

			Ufficio uff = org.getUfficio(utente.getUfficioVOInUso().getId()
					.intValue());
			if (pd.isUtenteAbilitatoView(utente, uff, protId.intValue())) {
				if ("I".equals(tipoProt)) {
					return (mapping.findForward("visualizzaProtocolloIngresso"));
				} else if ("U".equals(tipoProt)) {
					return (mapping.findForward("visualizzaProtocolloUscita"));
				} else {
					return (mapping.findForward("visualizzaPostaInterna"));
				}
			} else {
				errors.add("permessi.utenti.lettura", new ActionMessage(
						"permessi.utenti.lettura", "", ""));
				saveErrors(request, errors);
				return (mapping.findForward("fascicolo"));
			}

		} else if (request.getParameter("downloadDocumentoId") != null) {
			int idDoc = new Integer(request.getParameter("downloadDocumentoId"))
					.intValue();
			DocumentoVO d = DocumentoDelegate.getInstance().getDocumento(idDoc);
			response.setContentType(d.getContentType());
			response.setHeader("Content-Disposition", "attachment; filename="
					+ d.getFileName());
			response.setHeader("Cache-control", "");
			DocumentoDelegate.getInstance().writeDocumentToStream(idDoc,
					response.getOutputStream());
			return null;
		} else if (request.getParameter("btnSeleziona") != null) {
			String[] fascicoliSelezionati = ricercaFascicoliForm
					.getFascicoliSelezionati();
			int fascicoloId = NumberUtil.getInt(request
					.getParameter("fascicoloSelezionato"));
			FascicoloDelegate fd = FascicoloDelegate.getInstance();
			if (Boolean.TRUE.equals(session.getAttribute("tornaFascicolo"))) {
				session.setAttribute("fascicoloPadre", fascicoloId);
				FascicoloForm f = (FascicoloForm) session
						.getAttribute("fascicoloFiglio");
				session.removeAttribute("tornaFascicolo");
				session.setAttribute("fascicoloForm", f);
				session.setAttribute("fascicoloOggetto", f
						.getOggettoFascicolo());
				return (mapping.findForward("tornaFascicolo"));
			} else if (Boolean.TRUE.equals(session
					.getAttribute("tornaFascicoloCollegato"))) {
				Integer fId = (Integer) session
						.getAttribute("fascicoloAllegatoId");

				if (fascicoloId > 0 && fId > 0) {
					if (fascicoloId != fId)
						fd.aggiungiCollegamentoFascicolo(fId, fascicoloId,
								utente.getValueObject().getUsername());
					else {
						errors.add("fascicolo", new ActionMessage(
								"fascicolo_collegamenti_stesso_protocollo"));
						saveErrors(request, errors);
						return (mapping.findForward("input"));
					}

				} else {
					errors.add("fascicolo", new ActionMessage(
							"errore_nel_salvataggio"));
					saveErrors(request, errors);
					return (mapping.findForward("input"));
				}
				session.removeAttribute("fascicoloAllegatoId");
				session.removeAttribute("tornaFascicoloCollegato");
				return (mapping.findForward("tornaCollegato"));
			} else if (Boolean.TRUE.equals(session
					.getAttribute("tornaProtocollo"))) {
				session.removeAttribute("tornaProtocollo");
				ProtocolloForm pForm = (ProtocolloForm) session
						.getAttribute("protocolloForm");
				Fascicolo fascicolo = fd.getFascicoloById(fascicoloId);
				fascicolo.getFascicoloVO().setOwner(true);
				pForm.aggiungiFascicolo(fascicolo.getFascicoloVO());
				ricercaFascicoliForm.inizializzaForm();

				if (pForm instanceof ProtocolloIngressoForm) {
					ProtocolloIngressoForm piForm = (ProtocolloIngressoForm) pForm;
					if (piForm.isDaBtnModifica())
						return (mapping
								.findForward("tornaProtocolloIngressoNew"));
					else
						return (mapping.findForward("tornaProtocolloIngresso"));
				} else if (pForm instanceof ProtocolloUscitaForm) {
					if (session.getAttribute("tornaFascicolaUscitaEdit") != null) {
						session.removeAttribute("tornaFascicolaUscitaEdit");
						return (mapping
								.findForward("tornaFascicolaProtocolloUscita"));
					} else
						return (mapping.findForward("tornaProtocolloUscita"));
				} else {
					PostaInternaForm postaForm = (PostaInternaForm) pForm;
					if (postaForm.getProtocolloId() == 0
							|| postaForm.isDaBtnModifica())
						return (mapping.findForward("tornaPostaInternaNew"));
					else
						return (mapping.findForward("tornaPostaInterna"));
				}

			} else if (Boolean.TRUE.equals(session.getAttribute("tornaEditor"))) {
				session.removeAttribute("tornaEditor");
				EditorForm dForm = (EditorForm) session
						.getAttribute("editorForm");
				Fascicolo fascicolo = fd.getFascicoloById(fascicoloId);
				fascicolo.getFascicoloVO().setOwner(true);
				dForm.aggiungiFascicolo(fascicolo.getFascicoloVO());
				ricercaFascicoliForm.inizializzaForm();
				return (mapping.findForward("tornaInvioProtocolloEditor"));
			} else if (Boolean.TRUE.equals(session
					.getAttribute("tornaTemplate"))) {
				session.removeAttribute("tornaTemplate");
				EditorForm dForm = (EditorForm) session
						.getAttribute("editorForm");
				Fascicolo fascicolo = fd.getFascicoloById(fascicoloId);
				fascicolo.getFascicoloVO().setOwner(true);
				dForm.aggiungiFascicolo(fascicolo.getFascicoloVO());
				ricercaFascicoliForm.inizializzaForm();
				return (mapping.findForward("tornaTemplate"));
			} else if (Boolean.TRUE.equals(session
					.getAttribute("tornaDocumento"))) {
				session.removeAttribute("tornaDocumento");
				DocumentoForm pForm = (DocumentoForm) session
						.getAttribute("documentoForm");
				Fascicolo fascicolo = fd.getFascicoloById(fascicoloId);
				pForm.aggiungiFascicolo(fascicolo.getFascicoloVO());
				TitolarioBO.impostaTitolario(pForm, utente.getUfficioInUso(),
						fascicolo.getFascicoloVO().getTitolarioId());
				ricercaFascicoliForm.inizializzaForm();
				return (mapping.findForward("tornaDocumento"));

			} else if (Boolean.TRUE
					.equals(session.getAttribute("tornaFaldone"))) {
				FaldoneForm fForm = (FaldoneForm) session
						.getAttribute("faldoneForm");
				for (int i = 0; fascicoliSelezionati != null
						&& i < fascicoliSelezionati.length; i++) {
					Fascicolo fa = fd.getFascicoloById(NumberUtil
							.getInt(fascicoliSelezionati[i]));
					fForm.aggiungiFascicolo(fa);
				}
				ricercaFascicoliForm.inizializzaForm();
				session.removeAttribute("tornaFaldone");
				return (mapping.findForward("tornaFaldone"));
			} else if (Boolean.TRUE.equals(session
					.getAttribute("tornaProcedimento"))) {
				ProcedimentoForm fForm = (ProcedimentoForm) session
						.getAttribute("procedimentoForm");
				for (int i = 0; fascicoliSelezionati != null
						&& i < fascicoliSelezionati.length; i++) {
					FascicoloView fa = fd.getFascicoloViewById(NumberUtil
							.getInt(fascicoliSelezionati[i]));
					fForm.aggiungiFascicolo(fa);
				}
				ricercaFascicoliForm.inizializzaForm();
				session.removeAttribute("tornaProcedimento");
				return (mapping.findForward("tornaProcedimento"));
			}

			else {
				request.setAttribute("fascicoloId", new Integer(fascicoloId));
				return (mapping.findForward("fascicolo"));
			}
		} else if (ricercaFascicoliForm.isTuttiUffici()
				&& ricercaFascicoliForm.getTitolario() == null) {
			TitolarioBO.impostaAllTitolario(ricercaFascicoliForm);
			return mapping.findForward("input");
		}
		/*
		 * else { ricercaFascicoliForm.inizializzaForm(); }
		 */
		impostaTitolario(ricercaFascicoliForm, utente, 0);
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return (mapping.findForward("input"));
	}

	private void impostaTitolario(RicercaFascicoliForm form, Utente utente,
			int titolarioId) {
		int ufficioId = form.getUfficioCorrenteId();
		TitolarioBO.impostaTitolario(form, ufficioId, titolarioId);
	}

	private String getOggettoSearch(HttpServletRequest request,
			RicercaFascicoliForm ricercaFascicoliForm) {
		String oggettoFasc = null;
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		if (Boolean.TRUE.equals(session.getAttribute("tornaFaldone"))) {

			oggettoFasc = (String) request
					.getAttribute("cercaFascicoliDaFaldoni");

			if (request.getAttribute("cercaFascicoliDaFaldoni") == null) {
				ricercaFascicoliForm.setOggettoFascicolo(ricercaFascicoliForm
						.getOggettoFascicolo());
				oggettoFasc = ricercaFascicoliForm.getOggettoFascicolo();
				ricercaFascicoliForm.setOggettoFascicolo("");
			} else {
				ricercaFascicoliForm.setOggettoFascicolo(oggettoFasc);
				ricercaFascicoliForm.setOggettoFascicolo("");
			}
		} else if ("FascicoliDaProcedimento".equals(request
				.getAttribute("provenienza"))) {
			oggettoFasc = (String) request
					.getAttribute("cercaFascicoliDaProcedimento");
			ricercaFascicoliForm.setOggettoFascicolo("");
		} else if (Boolean.TRUE.equals(session.getAttribute("tornaProtocollo"))) {
			oggettoFasc = (String) request
					.getAttribute("cercaFascicoliDaProtocollo");
			if (request.getAttribute("cercaFascicoliDaProtocollo") == null) {
				oggettoFasc = ricercaFascicoliForm.getOggettoFascicolo();
				ricercaFascicoliForm.setOggettoFascicolo("");
				if (!utente.getUfficioVOInUso().getTipo().equals("C"))
					ricercaFascicoliForm.setUfficioCorrenteId(utente
							.getUfficioInUso());
			} else {
				ricercaFascicoliForm.setOggettoFascicolo("");
				request.removeAttribute("cercaFascicoliDaProtocollo");
			}
		} else if (Boolean.TRUE.equals(session.getAttribute("tornaEditor"))) {
			oggettoFasc = (String) request
					.getAttribute("cercaFascicoliDaEditor");
			if (request.getAttribute("cercaFascicoliDaEditor") == null) {
				oggettoFasc = ricercaFascicoliForm.getOggettoFascicolo();
				ricercaFascicoliForm.setOggettoFascicolo("");
				if (!utente.getUfficioVOInUso().getTipo().equals("C"))
					ricercaFascicoliForm.setUfficioCorrenteId(utente
							.getUfficioInUso());
			} else {
				ricercaFascicoliForm.setOggettoFascicolo("");
				request.removeAttribute("cercaFascicoliDaEditor");
			}
		} else if (Boolean.TRUE.equals(session.getAttribute("tornaTemplate"))) {
			oggettoFasc = (String) request
					.getAttribute("cercaFascicoliDaTemplate");
			if (request.getAttribute("cercaFascicoliDaTemplate") == null) {
				oggettoFasc = ricercaFascicoliForm.getOggettoFascicolo();
				ricercaFascicoliForm.setOggettoFascicolo("");
				if (!utente.getUfficioVOInUso().getTipo().equals("C"))
					ricercaFascicoliForm.setUfficioCorrenteId(utente
							.getUfficioInUso());
			} else {
				ricercaFascicoliForm.setOggettoFascicolo("");
				request.removeAttribute("cercaFascicoliDaTemplate");
			}
		} else if (Boolean.TRUE.equals(session.getAttribute("tornaFascicolo"))) {
			oggettoFasc = (String) request.getAttribute("cercaFascicoloPadre");
			if (request.getAttribute("cercaFascicoloPadre") == null) {
				ricercaFascicoliForm.setOggettoFascicolo(ricercaFascicoliForm
						.getOggettoFascicolo());
				oggettoFasc = ricercaFascicoliForm.getOggettoFascicolo();
				//request.removeAttribute("cercaFascicoloPadre");
			} else {
				ricercaFascicoliForm.setOggettoFascicolo(oggettoFasc);
				//request.removeAttribute("cercaFascicoloPadre");
			}
		} else if (Boolean.TRUE.equals(session
				.getAttribute("tornaFascicoloCollegato"))) {
			oggettoFasc = (String) request
					.getAttribute("cercaFascicoloCollegato");
			if (request.getAttribute("cercaFascicoloCollegato") == null) {
				ricercaFascicoliForm.setOggettoFascicolo(ricercaFascicoliForm
						.getOggettoFascicolo());
				oggettoFasc = ricercaFascicoliForm.getOggettoFascicolo();
				request.removeAttribute("cercaFascicoloCollegato");
			} else {
				ricercaFascicoliForm.setOggettoFascicolo(oggettoFasc);

			}
		} else {
			oggettoFasc = request.getParameter("oggettoFascicolo");
			ricercaFascicoliForm.setOggettoFascicolo("");
		}
		return oggettoFasc;

	}

}
