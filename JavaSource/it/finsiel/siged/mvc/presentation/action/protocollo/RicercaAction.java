package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.compit.fenice.enums.UnitaAmministrativaEnum;
import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.model.Fascicolo;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.Protocollo;
import it.finsiel.siged.mvc.bo.AlberoUfficiBO;
import it.finsiel.siged.mvc.bo.TitolarioBO;
import it.finsiel.siged.mvc.bo.UfficioBO;
import it.finsiel.siged.mvc.business.DocumentoDelegate;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.business.TitolarioDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.RicercaForm;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.vo.lookup.TitolarioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;
import it.finsiel.siged.report.protocollo.CommonReportDS;
import it.finsiel.siged.util.NumberUtil;
import it.flosslab.mvc.presentation.action.protocollo.helper.file.ProtocolloFileUtility;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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

public class RicercaAction extends Action {

	// ----------------------------------------------------- Instance Variables

	/**
	 * The <code>Log</code> instance for this application.
	 */
	static Logger logger = Logger.getLogger(RicercaAction.class.getName());

	public final static String FLAG_PROTOCOLLO_ANNULLATO = "C";

	public final static Integer FLAG_PROTOCOLLO_RISERVATO = new Integer(1);

	// --------------------------------------------------------- Public Methods

	public void stampaReport(HttpServletRequest request, HttpServletResponse response, Collection c, String subTitle)
			throws IOException, ServletException {
		ServletContext context = this.getServlet().getServletContext();
		OutputStream os = response.getOutputStream();
		try {
			File reportFile = new File(context.getRealPath("/")	+ FileConstants.STAMPA_RICERCA_REPORT_TEMPLATE);
			JasperDesign jasperDesign = JRXmlLoader.load(context.getRealPath("/")+ FileConstants.STAMPA_RICERCA_REPORT_TEMPLATE);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("ReportTitle", "Report Ricerca");
			parameters.put("ReportSubTitle", subTitle);
			parameters.put("BaseDir", reportFile.getParentFile());
			CommonReportDS ds = new CommonReportDS(c);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ds);
			response.setHeader("Content-Disposition", "attachment;filename=Ricerca.pdf");
			response.setHeader("Cache-control", "");
			response.setContentType("application/pdf");
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
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

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession();
		ProtocolloDelegate protocolloDelegate = ProtocolloDelegate.getInstance();
		TitolarioDelegate titolarioDelegate = TitolarioDelegate.getInstance();

		RicercaForm ricercaForm = (RicercaForm) form;
		ricercaForm.setDestinatari(null);
		boolean indietroVisibile = false;
		ricercaForm.setIndietroVisibile(indietroVisibile);

		LookupDelegate lookupDelegate = LookupDelegate.getInstance();
		ricercaForm.setStatiProtocollo(lookupDelegate.getStatiProtocollo(ricercaForm.getTipoProtocollo()));

		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		boolean isTabula = Organizzazione.getInstance().getValueObject().getUnitaAmministrativa() == UnitaAmministrativaEnum.POLICLINICO_CT	&& utente.getAreaOrganizzativa().getId() != 1;
		setTipoUfficioRicerca(ricercaForm, utente);
		boolean preQuery = false;
		ricercaForm.setAooId(utente.getRegistroVOInUso().getAooId());
		if (form == null) {
			logger.info(" Creating new RicercaAction");
			form = new RicercaForm();
			session.setAttribute(mapping.getAttribute(), form);
		}
		if (request.getParameter("btnAnnulla") != null) {
			if ("fascicoloProtocollo".equals(session.getAttribute("provenienza"))) {
				indietroVisibile = true;
				ricercaForm.setIndietroVisibile(indietroVisibile);
			}
			if ("protocolliDaProcedimento".equals(session.getAttribute("provenienza"))) {
				indietroVisibile = true;
				ricercaForm.setIndietroVisibile(indietroVisibile);
			}
			if (session.getAttribute("indietroProtocolliDaProcedimento") == Boolean.TRUE) {
				indietroVisibile = true;
				ricercaForm.setIndietroVisibile(indietroVisibile);
			}
			ricercaForm.inizializzaForm();
			if (request.getAttribute("cercaProtocolliDaProcedimento") != null) {
				preQuery = true;
				ricercaForm.setOggetto((String) request.getAttribute("cercaProtocolliDaProcedimento"));
			}
			AlberoUfficiBO.impostaUfficio(utente, ricercaForm, true);
			AlberoUfficiBO.impostaUfficioProtocollatore(utente, ricercaForm, true);
			AlberoUfficiBO.impostaUfficioUtentiProtocollatore(utente, ricercaForm, true);
			TitolarioBO.impostaTitolario(ricercaForm, utente.getUfficioInUso(), 0);
		}

		if (request.getParameter("btnRipeti") != null) {
			AlberoUfficiBO.impostaUfficio(utente, ricercaForm, true);
			AlberoUfficiBO.impostaUfficioProtocollatore(utente, ricercaForm, true);
			AlberoUfficiBO.impostaUfficioUtentiProtocollatore(utente, ricercaForm, true);
			TitolarioBO.impostaTitolario(ricercaForm, utente.getUfficioInUso(), 0);
		}

		if (ricercaForm.getTitolario() == null) {
			TitolarioBO.impostaTitolario(ricercaForm, utente.getUfficioInUso(), 0);
		}

		if (ricercaForm.getUfficioCorrenteId() == 0) {
			AlberoUfficiBO.impostaUfficio(utente, ricercaForm, true);
		}

		if (ricercaForm.getUfficioProtCorrenteId() == 0) {
			AlberoUfficiBO.impostaUfficioProtocollatore(utente, ricercaForm, true);
		}
		
		if(ricercaForm.getUtenteProtSelezionatoId() == 0) {
			AlberoUfficiBO.impostaUfficioUtentiProtocollatore(utente, ricercaForm, true);
		}

		if (ricercaForm.getUfficioCorrentePIDestId() == 0) {
			AlberoUfficiBO.impostaUfficioPIDest(utente, ricercaForm, true);
		}

		if (request.getParameter("impostaUfficioAction") != null) {
			logger.info("impostaUfficioAction: " + request.getParameter("impostaUfficioAction"));
			ricercaForm.setUfficioCorrenteId(ricercaForm.getUfficioSelezionatoId());
			AlberoUfficiBO.impostaUfficioUtenti(utente, ricercaForm, true);
			return mapping.findForward("input");
		} else if (request.getParameter("ufficioPrecedenteAction") != null) {
			ricercaForm.setUfficioCorrenteId(ricercaForm.getUfficioCorrente().getParentId());
			AlberoUfficiBO.impostaUfficioUtenti(utente, ricercaForm, true);
		}
		if (request.getParameter("impostaUfficioActionPIDest") != null) {
			logger.info("impostaUfficioActionPIDest: "+ request.getParameter("impostaUfficioActionPIDest"));
			ricercaForm.setUfficioCorrentePIDestId(ricercaForm.getUfficioSelezionatoPIDestId());
			AlberoUfficiBO.impostaUfficioUtentiPIDest(utente, ricercaForm, true);
			return mapping.findForward("input");
		} else if (request.getParameter("ufficioPrecedenteActionPIDest") != null) {
			ricercaForm.setUfficioCorrentePIDestId(ricercaForm.getUfficioCorrentePIDest().getParentId());
			AlberoUfficiBO.impostaUfficioUtentiPIDest(utente, ricercaForm, true);
		} else if (ricercaForm.getBtnCerca() != null || preQuery) {
			session.removeAttribute("procedimentiDaFaldoni");
			session.removeAttribute("indietroProcedimentiDaFaldoni");
			if ("fascicoloProtocollo".equals(session.getAttribute("provenienza"))) {
				indietroVisibile = true;
				ricercaForm.setIndietroVisibile(indietroVisibile);
			}
			ricercaForm.setBtnCerca(null);
			ricercaForm.setProtocolloSelezionato(null);
			ricercaForm.setDocumentoSelezionato(null);

			int maxRighe = Integer.parseInt(utente.getAreaOrganizzativa().getMaxRighe());
			LinkedHashMap<String, Object> hashMap = getParametriRicerca(ricercaForm, utente);
			Organizzazione org = Organizzazione.getInstance();
			Ufficio uff = org.getUfficio(utente.getUfficioVOInUso().getId().intValue());
			int contaRighe = protocolloDelegate.contaProtocolli(utente, uff, hashMap);
			if (contaRighe <= maxRighe) {
				if (contaRighe > 0) {
					ricercaForm.setProtocolli(protocolloDelegate.cercaProtocolli(utente, uff, hashMap, isTabula));
					return (mapping.findForward("lista"));
				} else {
					errors.add("nessun_dato", new ActionMessage("nessun_dato"));
				}
			} else {
				errors.add("controllo.maxrighe", new ActionMessage(	"controllo.maxrighe", "" + contaRighe, "protocolli", ""	+ maxRighe));
			}
		}

		if (request.getParameter("btnRicerca") != null) {
			session.removeAttribute("protocolliDaProcedimento");
			ricercaForm.inizializzaForm();
			AlberoUfficiBO.impostaUfficio(utente, ricercaForm, true);
			AlberoUfficiBO.impostaUfficioProtocollatore(utente, ricercaForm, true);
			AlberoUfficiBO.impostaUfficioUtentiProtocollatore(utente, ricercaForm, true);
			TitolarioBO.impostaTitolario(ricercaForm, utente.getUfficioInUso(), 0);
		} else if (ricercaForm.getBtnCercaArgomento() != null) {
			ricercaForm.setBtnCercaArgomento(null);
			String codiceArgomento = (String) request.getParameter("codiceArgomento");
			String descrizioneArgomento = (String) request.getParameter("descrizioneArgomento");
			ricercaForm.setArgomenti(titolarioDelegate.getListaTitolario(utente.getUfficioVOInUso().getAooId(), codiceArgomento, descrizioneArgomento));
			return (mapping.findForward("listaArgomenti"));
		} else if (ricercaForm.getBtnCercaDestinatario() != null) {
			ricercaForm.setBtnCercaDestinatario(null);
			String destinatario = (String) request.getParameter("destinatario");
			ricercaForm.setDestinatari(protocolloDelegate.getDestinatari(destinatario));
			return (mapping.findForward("listaDestinatari"));
		} else if (ricercaForm.getBtnCercaMittente() != null) {
			ricercaForm.setBtnCercaMittente(null);
			String mittente = (String) request.getParameter("mittente");
			ricercaForm.setMittenti(protocolloDelegate.getMittenti(mittente));
			return (mapping.findForward("listaMittenti"));
		} else if (request.getParameter("protocolloSelezionato") != null) {
			ProtocolloVO v = ProtocolloDelegate.getInstance().getProtocolloVOById(NumberUtil.getInt(request.getParameter("protocolloSelezionato")));
			String tipoProt = v.getFlagTipo();
			request.setAttribute("protocolloId", new Integer(request.getParameter("protocolloSelezionato")));
			request.setAttribute("daRicerca", true);
			if ("I".equals(tipoProt)) {
				return (mapping.findForward("visualizzaProtocolloIngresso"));
			} else if ("P".equals(tipoProt)) {
				return (mapping.findForward("visualizzaPostaInterna"));
			} else {
				return (mapping.findForward("visualizzaProtocolloUscita"));
			}
		} else if (request.getParameter("downloadDocprotocolloSelezionato") != null) {
			Integer id = new Integer(Integer.parseInt(request.getParameter("downloadDocprotocolloSelezionato")));
			Protocollo proto = ProtocolloDelegate.getInstance().getProtocolloById(id.intValue());
			ProtocolloVO p = proto.getProtocollo();
			DocumentoVO doc = null;
			if (!isTabula) {
				int docId = p.getDocumentoPrincipaleId();
				doc = DocumentoDelegate.getInstance().getDocumento(docId);
			} else {
				doc = DocumentoDelegate.getInstance().getDocumentoTabula(id);
			}
			int aooId = utente.getAreaOrganizzativa().getId();
			if (ProtocolloFileUtility.isDocumentoReadable(p.getId(), p.getFlagTipo(), p.getUfficioProtocollatoreId(), p.getUfficioMittenteId(), proto.getFascicoli(), utente)) {
				downloadDocumento(mapping, doc, request, response, aooId);
			} else {
				errors.add("documento", new ActionMessage("download_doc_non_abilitato", "errors"));
				saveErrors(request, errors);
			}
			return mapping.findForward("lista");
		} else if ((request.getParameter("documentoId") != null) && (request.getParameter("tipoProt") != null)) {
			ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
			request.setAttribute("oggetto", (delegate.getDocId(Integer.parseInt(request.getParameter("documentoId")))));
			// String tipo = request.getAttribute("tipoProt").toString();
		} else if (request.getParameter("btnSelezionaProtocolli") != null) {
			String[] ids = ricercaForm.getProtocolliSelezionati();
			if (session.getAttribute("tornaFascicolo") == Boolean.TRUE) {
				Fascicolo f = (Fascicolo) session.getAttribute(Constants.FASCICOLO);
				if (ids != null) {
					Collection protocolliFascicolo = new ArrayList();
					for (int i = 0; i < ids.length; i++) {
						ReportProtocolloView p = ricercaForm.getProtocolloView(new Integer(ids[i]));
						protocolliFascicolo.add(p);
					}
					f.setProtocolli(protocolliFascicolo);
				}
				session.removeAttribute("tornaFascicolo");
				return mapping.findForward("tornaFascicolo");
			}
		} else if (request.getParameter("impostaTitolarioAction") != null) {
			if (ricercaForm.getTitolario() != null) {
				ricercaForm.setTitolarioPrecedenteId(ricercaForm.getTitolario().getId().intValue());
			}
			TitolarioBO.impostaTitolario(ricercaForm, utente.getUfficioInUso(),	ricercaForm.getTitolarioSelezionatoId());
			return mapping.findForward("input");
		} else if (request.getParameter("titolarioPrecedenteAction") != null) {
			TitolarioBO.impostaTitolario(ricercaForm, utente.getUfficioInUso(),	ricercaForm.getTitolarioPrecedenteId());
			if (ricercaForm.getTitolario() != null) {
				ricercaForm.setTitolarioPrecedenteId(ricercaForm.getTitolario().getParentId());
			}
			return mapping.findForward("input");
		} else if (ricercaForm.getBtnSeleziona() != null) {
			ricercaForm.setBtnSeleziona(null);
		} else if (request.getParameter("parMittente") != null) {
			ricercaForm.setMittente(request.getParameter("parMittente"));
		} else if (request.getParameter("parDestinatario") != null) {
			ricercaForm.setDestinatario(request.getParameter("parDestinatario"));
		} else if (request.getParameter("parArgomento") != null) {
			ricercaForm.setIdArgomento(request.getParameter("parArgomento"));
			TitolarioVO titolarioVO = titolarioDelegate.getTitolario(utente.getUfficioInUso(), (new Integer(request.getParameter("parArgomento"))).intValue(), utente.getUfficioVOInUso().getAooId());
			ricercaForm.setDescrizioneArgomento(titolarioVO.getDescrizione());
			ricercaForm.setPathArgomento(titolarioVO.getCodice());
		} else if (request.getParameter("impostaUfficioProtAction") != null) {
			logger.info("impostaUfficioProtAction: " + request.getParameter("impostaUfficioProtAction"));
			ricercaForm.setUfficioProtCorrenteId(ricercaForm.getUfficioProtSelezionatoId());
			AlberoUfficiBO.impostaUfficioProtocollatore(utente, ricercaForm, true);
			AlberoUfficiBO.impostaUfficioUtentiProtocollatore(utente, ricercaForm, true);
		} else if (request.getParameter("ufficioProtPrecedenteAction") != null) {
			ricercaForm.setUfficioProtCorrenteId(ricercaForm.getUfficioProtCorrente().getParentId());
			AlberoUfficiBO.impostaUfficioProtocollatore(utente, ricercaForm, true);
			AlberoUfficiBO.impostaUfficioUtentiProtocollatore(utente, ricercaForm, true);
		} else if (request.getParameter("btnStampa") != null) {
			stampaReport(request, response, ricercaForm.getProtocolliCollection(), ricercaForm.getReportSubTitle());
			return null;
		} else if (request.getParameter("indietro") != null) {
			if ("fascicoloProtocollo".equals(session.getAttribute("provenienza"))) {
				return mapping.findForward("tornaFascicolo");
			}
			if (session.getAttribute("indietroProtocolliDaProcedimento") == Boolean.TRUE) {
				session.removeAttribute("indietroProtocolliDaProcedimento");
				return mapping.findForward("tornaProcedimento");
			}
		} else if ("fascicoloProtocollo".equals(session.getAttribute("provenienza"))) {
			indietroVisibile = true;
			ricercaForm.setIndietroVisibile(indietroVisibile);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return mapping.findForward("input");
			}
		} else if ("protocolliDaProcedimento".equals(session.getAttribute("provenienza"))) {
			indietroVisibile = true;
			ricercaForm.inizializzaForm();
			ricercaForm.setIndietroVisibile(indietroVisibile);
			AlberoUfficiBO.impostaUfficio(utente, ricercaForm, true);
			AlberoUfficiBO.impostaUfficioProtocollatore(utente, ricercaForm, true);
			AlberoUfficiBO.impostaUfficioUtentiProtocollatore(utente, ricercaForm, true);
			TitolarioBO.impostaTitolario(ricercaForm, utente.getUfficioInUso(), 0);
			String cercaOggetto = (String) request.getAttribute("cercaProtocolliDaProcedimento");
			if (request.getAttribute("cercaProtocolliDaProcedimento") == null) {
				ricercaForm.setOggetto(ricercaForm.getOggetto());
			} else {
				ricercaForm.setOggetto(cercaOggetto);
			}
			session.removeAttribute("protocolliDaProcedimento");
			session.removeAttribute("provenienza");
			session.removeAttribute("tornaProcedimento");
			session.setAttribute("elencoProtocolliDaProcedimento", Boolean.TRUE);
			return mapping.findForward("input");
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		logger.info("Execute RicercaAction");
		session.removeAttribute("indietroProtocolliDaProcedimento ");
		return mapping.getInputForward();
	}

	private void setTipoUfficioRicerca(RicercaForm ricercaForm, Utente utente) {
		if (utente.getAreaOrganizzativa().isRicercaUfficiFull()) {
			ricercaForm.setTipoUfficioRicerca(UfficioVO.UFFICIO_CENTRALE);
		} else {
			ricercaForm.setTipoUfficioRicerca(utente.getUfficioVOInUso().getTipo());
		}
		if (!ricercaForm.getTipoUfficioRicerca().equals(UfficioVO.UFFICIO_CENTRALE)	&& ricercaForm.getTipoProtocollo() == null) {
			ricercaForm.setTipoProtocollo("I");
		}
	}

	public ActionForward downloadDocumento(ActionMapping mapping, DocumentoVO doc, HttpServletRequest request, HttpServletResponse response, int aooId) throws Exception {
		// if
		ActionMessages errors = ProtocolloFileUtility.downloadFileProtocollo(doc, response, aooId);
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return mapping.findForward("erroreDownload");
		}
		return null;
	}

	public static LinkedHashMap<String, Object> getParametriRicerca( RicercaForm rForm, Utente utente) throws Exception {
		StringBuffer rep = new StringBuffer();
		Organizzazione org = Organizzazione.getInstance();
		Date dataRegistrazioneDa;
		Date dataRegistrazioneA;
		Date dataDocumentoDa;
		Date dataDocumentoA;
		Date dataRicevutoDa;
		Date dataRicevutoA;
		int numeroProtocolloDa = 0;
		int numeroProtocolloA = 0;
		int annoProtocolloDa = 0;
		int annoProtocolloA = 0;
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		LinkedHashMap<String, Object> sqlDB = new LinkedHashMap<String, Object>();
		sqlDB.clear();
		if (rForm.getDataRegistrazioneDa() != null && !"".equals(rForm.getDataRegistrazioneDa())) {
			dataRegistrazioneDa = df.parse(rForm.getDataRegistrazioneDa());
			sqlDB.put("p.DATA_REGISTRAZIONE >= ?", dataRegistrazioneDa);
			rep.append(" Data Registrazione da: " + rForm.getDataRegistrazioneDa() + ";");
		}
		if (rForm.getDataRegistrazioneA() != null && !"".equals(rForm.getDataRegistrazioneA())) {
			dataRegistrazioneA = new Date(df.parse(rForm.getDataRegistrazioneA()).getTime() + Constants.GIORNO_MILLISECONDS - 1);
			sqlDB.put("p.DATA_REGISTRAZIONE <= ?", dataRegistrazioneA);
			rep.append(" Data Registrazione a: " + rForm.getDataRegistrazioneA() + ";");
		}
		if (rForm.getTipoDocumentoId() != 0) {
			sqlDB.put("p.tipo_documento_id = ?", rForm.getTipoDocumentoId());
			rep.append(" Tipo Documento: " + rForm.getTipoDocumento(rForm.getTipoDocumentoId()) + ";");
		}
		if (rForm.getNumeroProtocolloDa() != null && !"".equals(rForm.getNumeroProtocolloDa())) {
			numeroProtocolloDa = Integer.parseInt(rForm.getNumeroProtocolloDa());
			if (rForm.getNumeroProtocolloA() != null && !"".equals(rForm.getNumeroProtocolloA())) {
				sqlDB.put("p.nume_protocollo >= ?", numeroProtocolloDa);
				rep.append(" Numero di Protocollo >" + numeroProtocolloDa + ";");
			} else {
				sqlDB.put("p.nume_protocollo = ?", numeroProtocolloDa);
				rep.append(" Numero di Protocollo =" + numeroProtocolloDa + ";");
			}
		}
		if (rForm.getNumeroProtocolloA() != null && !"".equals(rForm.getNumeroProtocolloA())) {
			numeroProtocolloA = Integer.parseInt(rForm.getNumeroProtocolloA());
			sqlDB.put("p.nume_protocollo <= ?", numeroProtocolloA);
			rep.append(" Numero di Protocollo <" + numeroProtocolloA + ";");
		}
		if (rForm.getAnnoProtocolloDa() != null && !"".equals(rForm.getAnnoProtocolloDa())) {
			annoProtocolloDa = Integer.parseInt(rForm.getAnnoProtocolloDa());
			if (rForm.getAnnoProtocolloA() != null && !"".equals(rForm.getAnnoProtocolloA())) {
				sqlDB.put("p.anno_registrazione >= ?", new Integer(annoProtocolloDa));
				rep.append(" Anno di Registrazione>" + annoProtocolloDa + ";");
			} else {
				sqlDB.put("p.anno_registrazione = ?", new Integer(annoProtocolloDa));
				rep.append(" Anno di Registrazione =" + annoProtocolloDa + ";");
			}
		}
		if (rForm.getAnnoProtocolloA() != null && !"".equals(rForm.getAnnoProtocolloA())) {
			annoProtocolloA = Integer.parseInt(rForm.getAnnoProtocolloA());
			sqlDB.put("p.anno_registrazione <= ?", new Integer(annoProtocolloA));
			rep.append(" Anno di Registrazione <" + annoProtocolloDa + ";");
		}
		if (rForm.getTipoProtocollo() != null && !"".equals(rForm.getTipoProtocollo())) {
			if ("F".equals(rForm.getTipoProtocollo())) {
				RegistroVO reg = Organizzazione.getInstance().getRegistroByCod("Fatt");
				sqlDB.put("p.registro_id = ? ", reg.getId());
				rep.append(" Tipo di Protocollo: Fatture;");
			} else {
				sqlDB.put("p.FLAG_TIPO = ? ", rForm.getTipoProtocollo());
				rep.append(" Tipo di Protocollo: "+ rForm.getTipoProtocolloReport() + ";");
			}
		} else {
			sqlDB.put("p.registro_id = " + utente.getRegistroInUso()+ " AND p.FLAG_TIPO IN('I',?) ", "U");
			rep.append(" Tipo di Protocollo: Ingresso + Uscita;");
		}
		if (rForm.getStatoProtocollo() != null && !"".equals(rForm.getStatoProtocollo())) {
			if ("N".equals(rForm.getStatoProtocollo())) {
				sqlDB.put("p.stato_protocollo IN ('S',?) AND p.versione>1", rForm.getStatoProtocollo());
				rep.append(" Stato del Protocollo: Modificato;");
			} else {
				sqlDB.put("p.stato_protocollo = ?", rForm.getStatoProtocollo());
				rep.append(" Stato del Protocollo: Annullato;");
			}
		}
		if ("on".equalsIgnoreCase(rForm.getRiservato())) {
			sqlDB.put("p.FLAG_RISERVATO = ?", FLAG_PROTOCOLLO_RISERVATO);
			rep.append(" È un Protocollo Riservato;");
			rForm.setRiservato("off");
		}
		if (rForm.getFlagPEC()) {
			sqlDB.put(" p.numero_email !=?", 0);
			rep.append(" Proviene da una mail Certificata;");
			rForm.setFlagPEC(false);
		}
		// DATI DOCUMENTO
		if (rForm.getDataDocumentoDa() != null && !"".equals(rForm.getDataDocumentoDa())) {
			dataDocumentoDa = df.parse(rForm.getDataDocumentoDa());
			sqlDB.put("p.DATA_DOCUMENTO >= ?", dataDocumentoDa);
			rep.append(" Data del Documento >" + rForm.getDataDocumentoDa() + ";");
		}
		if (rForm.getDataDocumentoA() != null && !"".equals(rForm.getDataDocumentoA())) {
			dataDocumentoA = df.parse(rForm.getDataDocumentoA());
			sqlDB.put("p.DATA_DOCUMENTO <= ?", dataDocumentoA);
			rep.append(" Data del Documento <" + rForm.getDataDocumentoA() + ";");
		}
		if (rForm.getDataRicevutoDa() != null && !"".equals(rForm.getDataRicevutoDa())) {
			dataRicevutoDa = df.parse(rForm.getDataRicevutoDa());
			sqlDB.put("p.DATA_RICEZIONE >= ?", dataRicevutoDa);
			rep.append(" Data di Ricezionde del Documento >" + rForm.getDataRicevutoDa() + ";");
		}
		if (rForm.getDataRicevutoA() != null && !"".equals(rForm.getDataRicevutoA())) {
			dataRicevutoA = df.parse(rForm.getDataRicevutoA());
			sqlDB.put("p.DATA_RICEZIONE <= ?", dataRicevutoA);
			rep.append(" Data di Ricezionde del Documento <" + rForm.getDataRicevutoA() + ";");
		}
		if (rForm.getOggetto() != null && !"".equals(rForm.getOggetto())) {
			sqlDB.put("UPPER(p.TEXT_OGGETTO) LIKE ?", "%" + rForm.getOggetto().toUpperCase() + "%");
			rep.append(" Oggetto: " + rForm.getOggetto() + ";");
		}
		if (rForm.getProgressivoFascicolo() != null && !"".equals(rForm.getProgressivoFascicolo())) {
			String strFascicoli = " EXISTS (SELECT * FROM fascicolo_protocolli fasc" + " WHERE fasc.protocollo_id=p.protocollo_id AND fasc.fascicolo_id=?)";
			sqlDB.put(strFascicoli, Integer.parseInt(rForm.getProgressivoFascicolo()));
		}
		// ANNOTAZIONE
		if (rForm.getChiaveAnnotazione() != null && !"".equals(rForm.getChiaveAnnotazione())) {
			sqlDB.put("upper(p.ANNOTAZIONE_CHIAVE) LIKE ?", rForm.getChiaveAnnotazione().toUpperCase());
			rep.append(" Chiave Annotazione: " + rForm.getChiaveAnnotazione() + ";");
		}
		if (rForm.getPosizioneAnnotazione() != null && !"".equals(rForm.getPosizioneAnnotazione())) {
			sqlDB.put("upper(p.ANNOTAZIONE_POSIZIONE) LIKE ?", rForm.getPosizioneAnnotazione().toUpperCase());
			rep.append(" Posizione Annotazione: " + rForm.getPosizioneAnnotazione() + ";");
		}
		if (rForm.getDescrizioneAnnotazione() != null && !"".equals(rForm.getDescrizioneAnnotazione())) {
			sqlDB.put("upper(p.ANNOTAZIONE_DESCRIZIONE) LIKE ?", rForm.getDescrizioneAnnotazione().toUpperCase());
			rep.append(" Descrizione Annotazione: " + rForm.getDescrizioneAnnotazione() + ";");
		}
		// TITOLARIO
		if (rForm.getTitolario() != null) {
			sqlDB.put("p.titolario_id = ?", rForm.getTitolario().getId());
		}
		// PROTOCOLLATORE
		if (rForm.getUfficioProtRicercaId() > 0) {
			if (rForm.getUtenteProtSelezionatoId() == 0) {
				sqlDB.put("p.ufficio_protocollatore_id = ?", new Integer(rForm.getUfficioProtRicercaId()));
				rep.append(" Ufficio Protocollatore: " + org.getUfficio(rForm.getUfficioProtRicercaId()).getPath() + ";");
			} else {
				CaricaVO vo = CaricaDelegate.getInstance().getCaricaByUtenteAndUfficio(rForm.getUtenteProtSelezionatoId(), rForm.getUfficioProtRicercaId());
				sqlDB.put("p.carica_protocollatore_id = ?", new Integer(vo.getCaricaId()));
				rep.append(" Carica Protocollatrice: " + vo.getNome() + ";");
			}
		} else if (rForm.getTipoUfficioRicerca().equals(UfficioVO.UFFICIO_NORMALE) && rForm.getUfficioRicercaId() == 0) {
			if ("I".equals(rForm.getTipoProtocollo())) {
				String strAssegnatari = "(p.ufficio_protocollatore_id = " + utente.getUfficioInUso()
						+ " OR EXISTS (SELECT assegnatario_id FROM protocollo_assegnatari ass"
						+ " WHERE ass.protocollo_id=p.protocollo_id AND ass.ufficio_assegnatario_id=?))";
				sqlDB.put(strAssegnatari, utente.getUfficioInUso());
				rep.append(" Assegnato all'ufficio " + org.getUfficio(utente.getUfficioInUso()).getPath() + ";");
				rep.append(" Ufficio Protocollatore: " + org.getUfficio(utente.getUfficioInUso()).getPath() + ";");
			}
			if ("U".equals(rForm.getTipoProtocollo())) {
				sqlDB.put( "(p.ufficio_protocollatore_id =" + utente.getUfficioInUso() + " OR ufficio_mittente_id=?) ", utente.getUfficioInUso());
				rep.append(" Spedito dall'ufficio " + org.getUfficio(utente.getUfficioInUso()).getPath() + ";");
				rep.append(" Ufficio Protocollatore: " + org.getUfficio(utente.getUfficioInUso()).getPath() + ";");
			}
			if ("P".equals(rForm.getTipoProtocollo()) && rForm.getUfficioRicercaPIDestId() == 0) {
				String strAssegnatari = "(p.ufficio_protocollatore_id = " + utente.getUfficioInUso()
						+ " OR EXISTS (SELECT assegnatario_id FROM protocollo_assegnatari ass"
						+ " WHERE ass.protocollo_id=p.protocollo_id AND ass.ufficio_assegnatario_id=?) OR ufficio_mittente_id="
						+ utente.getUfficioInUso() + ")";
				sqlDB.put(strAssegnatari, utente.getUfficioInUso());
				rep.append(" Spedito dall'ufficio " + org.getUfficio(utente.getUfficioInUso()).getPath() + ";");
				rep.append(" Spedito all'ufficio " + org.getUfficio(utente.getUfficioInUso()).getPath() + ";");
				rep.append(" Ufficio Protocollatore: " + org.getUfficio(utente.getUfficioInUso()).getPath() + ";");
			}
		} else if (rForm.getTipoUfficioRicerca().equals(UfficioVO.UFFICIO_SEMICENTRALE) && rForm.getUfficioRicercaId() == 0) {
			Collection<Integer> dipList=UfficioBO.getAllDipendenti(org.getUfficio(utente.getUfficioInUso()));
			StringBuffer dip = new StringBuffer();
			for(Integer i:dipList) {
				dip.append(i+",");
			}
			String uffici = dip.substring(0, dip.length()-1);
			if ("I".equals(rForm.getTipoProtocollo())) {
				String strAssegnatari = "(p.ufficio_protocollatore_id IN ("+uffici+") "
						+ " OR EXISTS (SELECT assegnatario_id FROM protocollo_assegnatari ass"
						+ " WHERE ass.protocollo_id=p.protocollo_id AND ass.ufficio_assegnatario_id IN ("+uffici+")))";
				sqlDB.put(strAssegnatari, null);
				rep.append(" Assegnato all'ufficio " + org.getUfficio(utente.getUfficioInUso()).getPath() + ";");
				rep.append(" Ufficio Protocollatore: " + org.getUfficio(utente.getUfficioInUso()).getPath() + ";");
			}
			if ("U".equals(rForm.getTipoProtocollo())) {
				sqlDB.put("(p.ufficio_protocollatore_id IN ("+uffici+")" + " OR ufficio_mittente_id IN ("+uffici+")) ", null);
				rep.append(" Spedito dall'ufficio " + org.getUfficio(utente.getUfficioInUso()).getPath() + ";");
				rep.append(" Ufficio Protocollatore: " + org.getUfficio(utente.getUfficioInUso()).getPath() + ";");
			}
			if ("P".equals(rForm.getTipoProtocollo()) && rForm.getUfficioRicercaPIDestId() == 0) {
				String strAssegnatari = "(p.ufficio_protocollatore_id IN ("+uffici+") "
						+ " OR EXISTS (SELECT assegnatario_id FROM protocollo_assegnatari ass"
						+ " WHERE ass.protocollo_id=p.protocollo_id AND ass.ufficio_assegnatario_id IN ("+uffici+")) OR ufficio_mittente_id IN ("+uffici+"))";
				sqlDB.put(strAssegnatari, null);
				rep.append(" Spedito dall'ufficio " + org.getUfficio(utente.getUfficioInUso()).getPath() + ";");
				rep.append(" Spedito all'ufficio " + org.getUfficio(utente.getUfficioInUso()).getPath() + ";");
				rep.append(" Ufficio Protocollatore: " + org.getUfficio(utente.getUfficioInUso()).getPath() + ";");
			}
		}
		if ("I".equals(rForm.getTipoProtocollo())) {
			if (rForm.getUfficioRicercaId() > 0) {
				if (rForm.getUtenteSelezionatoId() == 0) {
					String strAssegnatari = " EXISTS (SELECT * FROM protocollo_assegnatari ass" 
							+ " WHERE ass.protocollo_id=p.protocollo_id AND ass.ufficio_assegnatario_id=?)";
					sqlDB.put(strAssegnatari, new Integer(rForm.getUfficioRicercaId()));
					rep.append(" Assegnato all'ufficio " + org.getUfficio(rForm.getUfficioRicercaId()).getPath() + ";");
				} else {
					String strAssegnatari = " EXISTS (SELECT * FROM protocollo_assegnatari ass"
							+ " WHERE ass.protocollo_id=p.protocollo_id AND ass.ufficio_assegnatario_id="
							+ rForm.getUfficioRicercaId() + " AND ass.carica_assegnatario_id=?)";
					CaricaVO vo = CaricaDelegate.getInstance() .getCaricaByUtenteAndUfficio(rForm.getUtenteSelezionatoId(), rForm.getUfficioRicercaId());
					sqlDB.put(strAssegnatari, new Integer(vo.getCaricaId()));
					rep.append(" Assegnato alla carica " + vo.getDescrizioneCaricaUfficio() + ";");
				}
			}
			if (rForm.getNumeroProtocolloMittente() != null && !"".equals(rForm.getNumeroProtocolloMittente())) {
				sqlDB.put("p.nume_protocollo_mittente LIKE ?", rForm.getNumeroProtocolloMittente());
				rep.append(" Protocollo Mittente= " + rForm.getNumeroProtocolloMittente() + ";");
			}
			if (rForm.getMittente() != null && !"".equals(rForm.getMittente())) {
				String sqlMittente = "((upper(p.DESC_DENOMINAZIONE_MITTENTE) LIKE '%" + rForm.getMittente().toUpperCase() + "%'"
						+ " OR upper(p.DESC_COGNOME_MITTENTE) LIKE ?) OR "
						+ "  upper(m.descrizione) LIKE '%" + rForm.getMittente().toUpperCase() + "%')";
				sqlDB.put(sqlMittente, "%" + rForm.getMittente().toUpperCase() + "%");
				rep.append(" Spedito da " + rForm.getMittente() + ";");
			}
		} else if ("U".equals(rForm.getTipoProtocollo()) || "M".equals(rForm.getTipoProtocollo()) 
				|| Parametri.LABEL_MOZIONE_USCITA.equals(rForm.getTipoProtocollo())) {
			// MITTENTE protocollo uscita
			if (rForm.getUfficioRicercaId() > 0) {
				sqlDB.put("ufficio_mittente_id=? ",	new Integer(rForm.getUfficioRicercaId()));
				rep.append(" Spedito dall'ufficio " + org.getUfficio(rForm.getUfficioRicercaId()).getPath()+ ";");
				if (rForm.getUtenteSelezionatoId() > 0) {
					sqlDB.put("utente_mittente_id=?", new Integer(rForm.getUtenteSelezionatoId()));
					rep.append(" Spedito dall'utente " + org.getUtente(rForm.getUtenteSelezionatoId()).getValueObject().getCognomeNome()
							+ " dell'ufficio " + org.getUfficio(rForm.getUfficioRicercaId()).getPath() + ";");
				}
			}
			// DESTINATARIO
			if (rForm.getDestinatario() != null && !"".equals(rForm.getDestinatario()) && !"I".equals(rForm.getTipoProtocollo())) {
				sqlDB.put("upper(d.destinatario) LIKE ?", "%" + rForm.getDestinatario().toUpperCase() + "%");
				rep.append(" Spedito a " + rForm.getDestinatario() + ";");
			}
		} else if ("P".equals(rForm.getTipoProtocollo())) {
			if (rForm.getUfficioRicercaPIDestId() > 0) {
				if (rForm.getUtenteSelezionatoPIDestId() == 0) {
					String strAssegnatari = " EXISTS (SELECT * FROM protocollo_assegnatari ass"
							+ " WHERE ass.protocollo_id=p.protocollo_id AND ass.ufficio_assegnatario_id=? )";
					sqlDB.put(strAssegnatari, new Integer(rForm.getUfficioRicercaPIDestId()));
					rep.append(" Spedito all'ufficio " + org.getUfficio(rForm.getUfficioRicercaPIDestId()).getPath() + ";");
				} else {
					String strAssegnatari = " EXISTS (SELECT * FROM protocollo_assegnatari ass"
							+ " WHERE ass.protocollo_id=p.protocollo_id AND ass.ufficio_assegnatario_id="
							+ rForm.getUfficioRicercaPIDestId() + " AND ass.carica_assegnatario_id=?)";
					CaricaVO vo = CaricaDelegate.getInstance().getCaricaByUtenteAndUfficio(rForm.getUtenteSelezionatoPIDestId(), rForm.getUfficioRicercaPIDestId());
					sqlDB.put(strAssegnatari, new Integer(vo.getCaricaId()));
					rep.append(" Spedito alla carica " + vo.getDescrizioneCaricaUfficio() + ";");
				}
			}
			// MITTENTE posta interna
			if (rForm.getUfficioRicercaId() > 0) {
				sqlDB.put("ufficio_mittente_id=? ", new Integer(rForm.getUfficioRicercaId()));
				rep.append(" Spedito dall'ufficio " + org.getUfficio(rForm.getUfficioRicercaId()).getPath()	+ ";");
				if (rForm.getUtenteSelezionatoId() > 0) {
					sqlDB.put("utente_mittente_id=?", new Integer(rForm.getUtenteSelezionatoId()));
					rep.append(" Spedito dall'utente " + org.getUtente(rForm.getUtenteSelezionatoId()).getValueObject().getCognomeNome()
							+ " dell'ufficio " + org.getUfficio(rForm.getUfficioRicercaId()).getPath() + ";");
				}
			}
		} else if (rForm.getTipoProtocollo().equals("")) {
			if (rForm.getMittente() != null && !"".equals(rForm.getMittente())) {
				String sqlMittente = "((upper(p.DESC_DENOMINAZIONE_MITTENTE) LIKE '%" + rForm.getMittente().toUpperCase() + "%'"
						+ " OR upper(p.DESC_COGNOME_MITTENTE) LIKE ?) OR upper(d.destinatario) LIKE '%" + rForm.getMittente().toUpperCase() + "%' "
						+ " OR upper(m.descrizione) LIKE '%" + rForm.getMittente().toUpperCase() + "%')";
				sqlDB.put(sqlMittente, "%" + rForm.getMittente().toUpperCase() + "%");
				rep.append(" Spedito da " + rForm.getMittente() + ";");
			}
		}
		rForm.setReportSubTitle(rep.toString());
		return sqlDB;
	}
}