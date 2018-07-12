package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.compit.fenice.mvc.business.DomandaDelegate;
import it.compit.fenice.mvc.vo.protocollo.DomandaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.ProtocolloIngresso;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloIngressoForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;
import it.finsiel.siged.report.protocollo.CommonReportDS;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;
import it.flosslab.mvc.presentation.action.protocollo.helper.CaricaProtocollo;
import it.flosslab.mvc.presentation.action.protocollo.helper.file.ProtocolloFileUtility;
import it.flosslab.mvc.presentation.action.protocollo.helper.form.AggiornaProtocolloIngressoForm;
import it.flosslab.mvc.presentation.action.protocollo.helper.model.AggiornaProtocolloIngressoModel;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * @author Almaviva sud.
 * 
 */

public class ProtocolloIngressoAction extends ProtocolloAction {
	// ----------------------------------------------------- Instance Variables

	/**
	 * The <code>Log</code> instance for this application.
	 */
	static Logger logger = Logger.getLogger(ProtocolloIngressoAction.class
			.getName());

	private static void getInputPage(ProtocolloIngressoForm form,
			HttpSession session) {
		if (session.getAttribute("PAGINA_RITORNO") != null) {
			String pag = (String) session.getAttribute("PAGINA_RITORNO");
			session.removeAttribute("PAGINA_RITORNO");
			if (pag.equals("SCARICO_UFFICIO"))
				form.setReturnPage("scarico_ufficio");
			else if (pag.equals("SCARICO_UTENTE"))
				form.setReturnPage("scarico_utente");
			else if (pag.equals("SCARICO_FATTURE"))
				form.setReturnPage("scarico_fatture");
			else if(pag.equals("FASCICOLO"))
				form.setReturnPage("fascicolo");
			else if(pag.equals("CONOSCENZA_UFFICIO"))
				form.setReturnPage("conoscenza_ufficio");
			else if(pag.equals("CONOSCENZA_UTENTE"))
				form.setReturnPage("conoscenza_utente");
		}
	}
	
	
	protected void resetForm(ProtocolloForm form, Utente utente,HttpServletRequest request) {
		form.inizializzaForm();
		HttpSession session=request.getSession();
		session.removeAttribute("tornaDocumento");
		form.setAooId(utente.getAreaOrganizzativa().getId());
		form.setTipoDocumentoId(0);
		form.setVersioneDefault(true);
	}
	
	protected void assegnaAdUfficio(ProtocolloForm form, int ufficioId,Utente utente) {
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(ufficioId);
		Organizzazione org = Organizzazione.getInstance();
		Ufficio uff = org.getUfficio(ufficioId);
		ass.setNomeUfficio(uff.getValueObject().getDescription());
		ass.setDescrizioneUfficio(uff.getPath());
		ProtocolloIngressoForm pForm = (ProtocolloIngressoForm) form;
		ass.setStato('S');
		ass.setCompetente(true);
		ass.setTitolareProcedimento(false);
		ass.setCaricaAssegnanteId(utente.getCaricaInUso());
		ass.setUfficioAssegnanteId(utente.getUfficioInUso());
		pForm.aggiungiAssegnatario(ass);
		pForm.setAssegnatarioCompetente(ass.getKey());
		form.setUfficioSelezionatoId(0);
		if (form.isDipTitolarioUfficio()) 
			form.setTitolario(null);
		updateAssegnatariCompetenti(pForm);
	}

	protected void assegnaAdUtente(ProtocolloForm form,Utente utente) {
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(form.getUfficioCorrenteId());
		ass.setNomeUfficio(form.getUfficioCorrente().getDescription());
		ass.setDescrizioneUfficio(form.getUfficioCorrentePath());
		ass.setUtenteId(form.getUtenteSelezionatoId());
		UtenteVO ute = form.getUtente(form.getUtenteSelezionatoId());
		ass.setNomeUtente(ute.getCaricaFullName());
		ProtocolloIngressoForm pForm = (ProtocolloIngressoForm) form;
		ass.setStato('S');
		ass.setCompetente(true);
		ass.setTitolareProcedimento(false);
		ass.setCaricaAssegnanteId(utente.getCaricaInUso());
		ass.setUfficioAssegnanteId(utente.getUfficioInUso());
		pForm.aggiungiAssegnatario(ass);
		pForm.setAssegnatarioCompetente(ass.getKey());
		if (form.isDipTitolarioUfficio())
			form.setTitolario(null);
		updateAssegnatariCompetenti(pForm);
	}

	private void rimuoviAssegnatari(ProtocolloIngressoForm form) {
		String[] assegnatari = form.getAssegnatariSelezionatiId();
		String assegnatarioCompetente = form.getAssegnatarioCompetente();
		if (assegnatari != null) {
			for (int i = 0; i < assegnatari.length; i++) {
				String assegnatario = assegnatari[i];
				if (assegnatario != null) {
					form.rimuoviAssegnatario(assegnatario);
					if (assegnatario.equals(assegnatarioCompetente)) {
						form.setAssegnatarioCompetente(null);
					}
				}
			}
			if (form.getAssegnatarioCompetente() == null) {
				Iterator<AssegnatarioView> i = form.getAssegnatari().iterator();
				if (i.hasNext()) {
					AssegnatarioView ass = i.next();
					form.setAssegnatarioCompetente(ass.getKey());
				}
			}
		}
	}

	private void rimuoviMultiMittenti(ProtocolloIngressoForm form) {
		String[] mittentiSelezionati = form.getMittentiSelezionatiId();
		List<SoggettoVO> mittenti = form.getMittenti();
		List<SoggettoVO> mittentiToRemove = new ArrayList<SoggettoVO>();
		if (mittentiSelezionati != null) {
			for (int i = 0; i < mittentiSelezionati.length; i++) {
				mittentiToRemove.add(mittenti.get(new Integer(
						mittentiSelezionati[i])));
			}
		}
		mittenti.removeAll(mittentiToRemove);
		form.setMittentiSelezionatiId(new String[] {});
	}
	
	public void stampaEtichettaErsu(HttpServletResponse response,
			ProtocolloIngressoForm form) throws IOException,
			ServletException {
		ServletContext context = this.getServlet().getServletContext();
		OutputStream os = response.getOutputStream();
		try {
			DomandaVO domanda=DomandaDelegate.getInstance().getDomandaById(form.getNumProtocolloMittente());
			SoggettoVO sogg=form.getMittenti().get(0);
			File reportFile = new File(context.getRealPath("/")
					+ FileConstants.STAMPA_ETICHETTA_ERSU_TEMPLATE);
			JasperDesign jasperDesign = JRXmlLoader.load(context
					.getRealPath("/")
					+ FileConstants.STAMPA_ETICHETTA_ERSU_TEMPLATE);
			JasperReport jasperReport = JasperCompileManager
					.compileReport(jasperDesign);
			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("anno", domanda.getAnnoIscrizione()!=null?domanda.getAnnoIscrizione():" ");
			parameters.put("matricola", domanda.getMatricola()!=null?domanda.getMatricola():" ");
			parameters.put("oggetto", form.getOggettoGenerico()!=null?form.getOggettoGenerico():" ");
			parameters.put("descrizioneNome", domanda.getCognome()+" "+domanda.getNome()!=null?domanda.getCognome()+" "+domanda.getNome():" ");
			parameters.put("comuneNascita", domanda.getComuneNascita()!=null?domanda.getComuneNascita():" ");
			parameters.put("dataNascita",domanda.getDataNascita()!=null?DateUtil.formattaData(domanda.getDataNascita().getTime()):" ");
			parameters.put("residenza", sogg.getIndirizzoCompleto()!=null?sogg.getIndirizzoCompleto():" ");
			parameters.put("protocollo",DateUtil.getYear(DateUtil.getDataOra(form.getDataRegistrazione()))+"/"+ProtocolloBO.formattaNumeroProtocollo(form.getNumeroProtocollo()));
			parameters.put("dataReg", form.getDataRegistrazione());
			parameters.put("npm", form.getNumProtocolloMittente()+"-"+form.getDataProtocolloMittente());
			parameters.put("BaseDir", reportFile.getParentFile());
			// lo devo cambiare -> cambiare il jasper i dati vanno nel header
			ArrayList<Integer> l = new ArrayList<Integer>();
			l.add(new Integer(1));
			CommonReportDS ds = new CommonReportDS(l);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			// fine dello cambiamento
			response.setHeader("Content-Disposition",
					"attachment;filename=Etichetta_"
							+ form.getNumeroProtocollo() + ".pdf");
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
		/* qui vanno eventuali errori */
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		super.setType(CaricaProtocollo.PROTOCOLLO_INGRESSO);
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		/* contenente i nostri dati provenienti dall'html form */
		ProtocolloIngressoForm pForm = (ProtocolloIngressoForm) form;
		getInputPage(pForm, session);
		updateAssegnatariCompetenti(pForm);
		session.setAttribute("protocolloForm", pForm);
		
		if (request.getParameter("impostaOggettoAction") != null) {
			if (pForm.getOggettoFromOggettario() != null) {
				pForm.setOggettoGenerico(pForm.getOggetto());
				pForm.setOggettoSelezionato(1);
				pForm.setGiorniAlert(pForm.getOggettoFromOggettario().getGiorniAlert());
				List<Integer> uffIds=ProtocolloBO.getUfficiAssegnatariOggetto(pForm);
				if(uffIds!=null)
					for(int uffId:uffIds)
						assegnaAdUfficio(pForm, uffId, utente);
				
			} else {
				pForm.setGiorniAlert(0);
				pForm.setOggettoSelezionato(0);
			}
		}

		if (!utente.getRegistroVOInUso().getApertoIngresso()) {
			errors.add("apertoIngresso", new ActionMessage("registro_chiuso"));
			saveErrors(request, errors);
		}

		if (pForm.getTitolario() == null) {
			AggiornaProtocolloIngressoForm.impostaTitolario(pForm, utente, 0);
		}

		ActionForward actionForward = super.execute(mapping, form, request,
				response);

		if (actionForward != null && "none".equals(actionForward.getName())) {
			return null;
		} else if (actionForward != null) {
			return actionForward;
		}
		if (request.getParameter("downloadAllegatoId") != null) {
			int docId = NumberUtil.getInt((String) request
					.getParameter("downloadAllegatoId"));
			int aooId = utente.getAreaOrganizzativa().getId();
			DocumentoVO doc = pForm.getDocumentoAllegato(String.valueOf(docId));
			if(ProtocolloFileUtility.isDocumentoReadable(pForm.getProtocolloId(), pForm.getFlagTipo(), pForm.getUfficioProtocollatoreId(), 0,pForm.getFascicoliProtocolloOld(), utente))
				return downloadDocumento(mapping, doc, request, response, aooId);
			else{
				errors.add("documento",new ActionMessage("download_doc_non_abilitato","errors"));
				saveErrors(request, errors);
			}
			return mapping.findForward("input");
		} else if (request.getParameter("downloadDocumentoPrincipale") != null) {
			DocumentoVO doc = pForm.getDocumentoPrincipale();
			int aooId = utente.getAreaOrganizzativa().getId();
			if(ProtocolloFileUtility.isDocumentoReadable(pForm.getProtocolloId(), pForm.getFlagTipo(), pForm.getUfficioProtocollatoreId(), 0, pForm.getFascicoliProtocolloOld(), utente))
				return downloadDocumento(mapping, doc, request, response, aooId);
			else{
				errors.add("documento",new ActionMessage("download_doc_non_abilitato","errors"));
				saveErrors(request, errors);
			}
			return mapping.findForward("input");

		} if (request.getParameter("btnAggiungiProcedimenti") != null) {
			if(session.getAttribute("PresaInCarico")!=null){
				pForm.setDaScarico(true);
				session.removeAttribute("PresaInCarico");
			}
			return mapping.findForward("aggiungiProcedimento");
		}
		// Selezione tipologia di mittente
		if (request.getParameter("cercaMittenteAction") != null) {
			session.setAttribute("tornaProtocollo", Boolean.TRUE);
			if ("F".equals(pForm.getMittente().getTipo())) {
				//elem.setTitle("Seleziona persona fisica");
				request.setAttribute("cognome", pForm.getMittente()
						.getCognome());
				request.setAttribute("nome", pForm.getMittente().getNome());
				session.setAttribute("provenienza", "personaFisicaProtocolloI");
				return mapping.findForward("cercaPersonaFisica");
			} else {
				//elem.setTitle("Seleziona persona giuridica");
				request.setAttribute("descrizioneDitta", pForm.getMittente()
						.getDescrizioneDitta());
				session.setAttribute("provenienza",
						"personaGiuridicaProtocolloI");
				return mapping.findForward("cercaPersonaGiuridica");
			}

		} else if (request.getParameter("rimuoviAssegnatariAction") != null) {
			rimuoviAssegnatari(pForm);
		} else if (request.getParameter("rimuoviMultiMittentiAction") != null) {
			rimuoviMultiMittenti(pForm);
		}

		if (request.getParameter("assegnaMittenteAction") != null) {
			aggiungiMittente(pForm, errors);
		}
		if (request.getParameter("impostaTitolarioAction") != null) {
			if (pForm.getTitolario() != null) {
				pForm.setTitolarioPrecedenteId(pForm.getTitolario().getId()
						.intValue());
			}
			Utente ute = (Utente) session.getAttribute(Constants.UTENTE_KEY);
			AggiornaProtocolloIngressoForm.impostaTitolario(pForm, ute, pForm
					.getTitolarioSelezionatoId());
			return mapping.findForward("input");
		} else if (request.getParameter("btnRipetiDatiI") != null) {
			RegistroVO reg = ProtocolloDelegate.getInstance().getRegistroByProtocolloId(pForm.getProtocolloId());
			for(AssegnatarioView view:pForm.getAssegnatari()){
				view.setLavorato(false);
				view.setPresaVisione(false);
			}
			pForm.inizializzaFormToCopyProtocollo();
			saveToken(request);
			if (reg.getCodRegistro().equals("Fatt"))
				return mapping.findForward("nuovaFatturaRipeti");
			else
				return mapping.findForward("nuovoProtocolloRipeti");
		} else if (request.getParameter("titolarioPrecedenteAction") != null) {
			Utente ute = (Utente) session.getAttribute(Constants.UTENTE_KEY);
			AggiornaProtocolloIngressoForm.impostaTitolario(pForm, ute, pForm
					.getTitolarioPrecedenteId());
			if (pForm.getTitolario() != null) {
				pForm.setTitolarioPrecedenteId(pForm.getTitolario()
						.getParentId());
			}
			return mapping.findForward("input");

		} else if (request.getParameter("btnCercaProtMitt") != null) {
			Utente ute = (Utente) session.getAttribute(Constants.UTENTE_KEY);
			ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
			pForm.setProtocolliMittente(delegate.getProtocolliByProtMittente(
					ute, pForm.getNumProtocolloMittente()));

			if (pForm.getProtocolliMittente().isEmpty()) {
				errors.add("numProtocolloMittente", new ActionMessage(
						"cerca_protocollo_mittente_empty"));
				saveErrors(request, errors);
			} else {
				return mapping.findForward("cercaProtocolloMittente");
			}
		} else if (request.getParameter("fascicolazioneMultiplaAction") != null
				&& errors.isEmpty()) {
			if(pForm.getFascicoliProtocollo().size()==0)
				errors.add("fascicolo", new ActionMessage("fascicolo_obbligatorio"));
			else if (session.getAttribute("protocolliIds") != null ) {
				String[] ids = (String[]) session.getAttribute("protocolliIds");
				fascicolazioneMultiplaProtocollo(request, ids, errors, session,
						pForm);
				session.removeAttribute("protocolliIds");
			} else
				errors.add("fascicolazioneMultiplaErrore", new ActionMessage(
						"protocolloIds_vuoto"));
			if (errors.isEmpty()) {
				messages.add("fascicolazioneMultiplaOK", new ActionMessage(
						"protocolli_fascicolati"));
				saveMessages(request, messages);
			}
			saveErrors(request, errors);
			saveToken(request);
			return mapping.findForward("input");

		} else if (request.getParameter("salvaAction") != null
				&& errors.isEmpty()) {
			return saveProtocollo(mapping, request, errors, session, pForm);
		} else if (request.getParameter("btnLavoratoProcedimento") != null) {
			setLavoratoProcedimento(pForm,utente);
			return getMappingForward(pForm.getReturnPage(), mapping);
		} else if (request.getParameter("btnPresaVisione") != null) {
			presaVisione(utente, errors, pForm.getProtocolloId());
			return getMappingForward(pForm.getReturnPage(), mapping);
			
		}
		else if (request.getParameter("indietroAction") != null) {
			return getMappingForward(pForm.getReturnPage(), mapping);
		} else if (request.getParameter("cercaProcedimenti") != null) {
			session.setAttribute("tornaProtocolloIngresso", Boolean.TRUE);
			return mapping.findForward("cercaProcedimenti");
		}else if (request.getParameter("btnStampaEtichettaErsu") != null) {
			stampaEtichettaErsu(response, pForm);
			return null;
		}else if (request.getParameter("btnEsitoCommittente") != null) {
			request.setAttribute("protocolloId", pForm.getProtocolloId());
			return mapping.findForward("notificaEsitoCommittente");
		}
		pForm.setOggettoProcedimento("");
		if(!errors.isEmpty())
			saveErrors(request, errors);
		return mapping.findForward("input");
	}

	private void setLavoratoProcedimento(ProtocolloIngressoForm form,Utente utente) {
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		AssegnatarioVO assegnatario=null;
		if(form.getReturnPage().equals("scarico_ufficio")){
			assegnatario = delegate.getAssegnatarioProtocollo(form.getProtocolloId(),utente.getUfficioInUso(),0);	
		}
		else if(form.getReturnPage().equals("scarico_utente")){
			assegnatario = delegate.getAssegnatarioProtocollo(form.getProtocolloId(),utente.getUfficioInUso(),utente.getCaricaInUso());
		}
		
			delegate.registraLavoratoProtocolloIngresso(assegnatario,utente.getValueObject().getUsername());
	}
	
	private void presaVisione(Utente utente, ActionMessages errors, int id) {
		ProtocolloIngresso pi = null;
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		pi = delegate.getProtocolloIngressoById(id);
		pi.getProtocollo().setStatoProtocollo("V");
		pi.getFascicoli().clear();
		pi.setVisionato(true);
		//pi.setLavorato(true);
		delegate.aggiornaProtocolloIngresso(pi, utente);
	}
	
	private ActionForward saveProtocollo(ActionMapping mapping,
			HttpServletRequest request, ActionMessages errors,
			HttpSession session, ProtocolloIngressoForm pForm) {
		Utente ute = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		ProtocolloIngresso protocolloIngresso = null;
		ProtocolloVO protocollo = null;
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		if (pForm.getProtocolloId() == 0) {
			protocolloIngresso = ProtocolloBO.getDefaultProtocolloIngresso(ute);
			AggiornaProtocolloIngressoModel.aggiorna(pForm, protocolloIngresso,
					ute);
			protocollo = delegate.registraProtocollo(protocolloIngresso, ute,
					false);
		} else {
			protocolloIngresso = (ProtocolloIngresso) session
					.getAttribute("protocolloIngresso");
			if (protocolloIngresso.getProtocollo().getFlagTipo().equals("R")) {
				protocolloIngresso.getProtocollo().setFlagTipo("I");
				protocolloIngresso.getProtocollo().setDataRegistrazione(
						new Timestamp(System.currentTimeMillis()));

			}
			AggiornaProtocolloIngressoModel.aggiorna(pForm, protocolloIngresso,
					ute);
			protocollo = delegate.aggiornaProtocolloIngresso(
					protocolloIngresso, ute);
		}
		if (protocollo == null) {
			errors.add("errore_nel_salvataggio", new ActionMessage(
					"errore_nel_salvataggio", "", ""));
			saveErrors(request, errors);
		} else if (protocollo.getReturnValue() == ReturnValues.SAVED) {
			request.setAttribute("protocolloId", protocollo.getId());
			caricaProtocollo(request, pForm,
					CaricaProtocollo.PROTOCOLLO_INGRESSO);

			if (session.getAttribute("PROTOCOLLI_EMERGENZA") != null) {
				pForm.setNumeroProtocolliRegistroEmergenza(((Integer) session
						.getAttribute("PROTOCOLLI_EMERGENZA")).intValue());
				return mapping.findForward("visualizzaProtocollo");
			}
			pForm.setEstremiAutorizzazione(null);
			if (request.getParameter("salvaAction").equals("Fascicola")) {
				return getMappingForward(pForm.getReturnPage(), mapping);
			} else if (request.getParameter("salvaAction").equals("Salva")
					|| request.getParameter("salvaAction").equals("Aggiungi il Procedimento"))
				return mapping.findForward("visualizzaProtocollo");
			else
				return mapping.findForward("allegaDocProtocollo");
			//
		} else if (protocollo.getReturnValue() == ReturnValues.OLD_VERSION) {
			errors.add("generale", new ActionMessage("versione_vecchia"));
			saveErrors(request, errors);
			request.setAttribute("protocolloId", protocollo.getId());
		} else {
			errors.add("generale", new ActionMessage("errore_nel_salvataggio"));
			saveErrors(request, errors);
		}
		resetToken(request);
		pForm.setOggettoProcedimento("");
		return mapping.findForward("input");
	}

	private ActionForward getMappingForward(String returnPage,
			ActionMapping mapping) {
		if (returnPage != null) {
			if (returnPage.equals("scarico_ufficio"))
				return mapping.findForward("tornaUfficio");
			else if (returnPage.equals("scarico_utente"))
				return mapping.findForward("tornaUtente");
			else if (returnPage.equals("scarico_utente"))
				return mapping.findForward("tornaFattura");
			else if (returnPage.equals("fascicolo"))
				return mapping.findForward("fascicolo");
			else if (returnPage.equals("conoscenza_ufficio"))
				return mapping.findForward("tornaConoscenzaUfficio");
			else if (returnPage.equals("conoscenza_utente"))
				return mapping.findForward("tornaConoscenzaUtente");
		}
		return mapping.findForward("visualizzaProtocollo");
	}

	private void fascicolazioneMultiplaProtocollo(HttpServletRequest request,
			String[] ids, ActionMessages errors, HttpSession session,
			ProtocolloIngressoForm pForm) {
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		ProtocolloIngresso protocolloIngresso = null;
		ProtocolloVO protocollo = null;
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		for (String protId : ids) {
			
			protocolloIngresso=delegate.getProtocolloIngressoById(Integer.parseInt(protId));
			protocolloIngresso.getProtocollo().setStatoProtocollo("A");
			protocolloIngresso.setLavorato(pForm.isLavorato());
			protocolloIngresso.setFascicoli(pForm.getFascicoliProtocollo());
			protocolloIngresso.setFascicoliEliminatiId(pForm.getFascicoliEliminatiId());
			protocollo=delegate.aggiornaProtocolloIngresso(protocolloIngresso, utente);
			
			if (protocollo.getReturnValue() != ReturnValues.SAVED) {
				errors.add("fascicolazioneMultipla", new ActionMessage("operazione_fallita"));
			}
		}
	}

	private void updateAssegnatariCompetenti(ProtocolloIngressoForm form) {
		List<String> assCompetenti = new ArrayList<String>();
		for (Iterator<AssegnatarioView> i = form.getAssegnatari().iterator(); i.hasNext();) {
			AssegnatarioView ass = i.next();
			if (ass.isCompetente()) {
				assCompetenti.add(ass.getKey());
			}

		}
		String[] assCompArray = new String[assCompetenti.size()];
		int index = 0;
		for (String assString : assCompetenti) {
			assCompArray[index] = assString;
			index++;
		}
		form.setAssegnatariCompetenti(assCompArray);

	}

	private void aggiungiMittente(ProtocolloIngressoForm form,ActionMessages errors) {
		if (isMittenteNull(form.getMittente())) {
			errors.add("inserisciMittente", new ActionMessage("mittente_vuoto"));
		} else {
			form.getMittenti().add(form.getMittente());
			form.setMittente(new SoggettoVO("G"));
		}
	}

	private boolean isMittenteNull(SoggettoVO sogg) {
		boolean isNull = false;
		if (sogg.getTipo().equals("G")) {
			if (sogg.getDescrizioneDitta() == null
					|| sogg.getDescrizioneDitta().trim().equals(""))
				isNull = true;
		} else if (sogg.getTipo().equals("F")) {
			if (sogg.getCognome() == null
					|| sogg.getCognome().trim().equals(""))
				isNull = true;
		}
		return isNull;
	}
}