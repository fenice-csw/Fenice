package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.compit.fenice.mvc.presentation.actionform.protocollo.PostaInternaForm;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.AlberoUfficiBO;
import it.finsiel.siged.mvc.bo.RegistroBO;
import it.finsiel.siged.mvc.business.ProcedimentoDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;
import it.finsiel.siged.mvc.presentation.helper.AllaccioView;
import it.finsiel.siged.mvc.presentation.helper.NavBarElement;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.StringUtil;
import it.flosslab.mvc.presentation.action.protocollo.helper.CaricaProtocollo;
import it.flosslab.mvc.presentation.action.protocollo.helper.file.ProtocolloFileUtility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;


public abstract class ProtocolloAction extends Action {
	// ----------------------------------------------------- Instance Variables

	static Logger logger = Logger.getLogger(ProtocolloAction.class.getName());
	
	protected String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public final static String FLAG_PROTOCOLLO_ANNULLATO = "C";

	private void allacciaProtocollo(ProtocolloForm form, HttpSession session,
			ActionMessages errors) {
		ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		Collection<AllaccioView> c = delegate.getProtocolliAllacciabili(utente, Integer
				.parseInt(form.getAllaccioNumProtocollo()), Integer
				.parseInt(form.getAllaccioNumProtocollo()), Integer
				.parseInt(form.getAllaccioAnnoProtocollo()), form
				.getProtocolloId());
		if (c != null && c.size() > 0) {
			Iterator<AllaccioView> it = c.iterator();
			while (it.hasNext()) {
				AllaccioView allaccio =  it.next();
				AllaccioVO allaccioVO = new AllaccioVO();
				if (allaccio != null && allaccio.getNumProtAllacciato() > 0) {
					allaccioVO.setProtocolloAllacciatoId(allaccio
							.getProtAllacciatoId());
					allaccioVO.setAllaccioDescrizione(form
							.getAllaccioNumProtocollo()
							+ "/"
							+ form.getAllaccioAnnoProtocollo()
							+ " ("
							+ allaccio.getTipoProtocollo() + ")");
					form.allacciaProtocollo(allaccioVO);
				}
			}
		} else {
			errors.add("allacci", new ActionMessage(
					"protocollo_non_allacciabile"));
		}
	}

	public ActionForward downloadDocumento(ActionMapping mapping,
			DocumentoVO doc, HttpServletRequest request,
			HttpServletResponse response, int aooId) throws Exception {
		ActionMessages errors = ProtocolloFileUtility.downloadFileProtocollo(doc, response, aooId);
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return mapping.findForward("erroreDownload");
		}
		ActionForward actionForward = new ActionForward();
		actionForward.setName("none");
		return actionForward;
	}
	
	public ActionForward visualizzaFatturaElettronica(ActionMapping mapping,
			DocumentoVO doc, HttpServletRequest request,
			HttpServletResponse response, int aooId) throws Exception {
		ActionMessages errors = ProtocolloFileUtility.visualizzaFatturaElettronica(doc,request, response, aooId);
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return mapping.findForward("erroreDownload");
		}
		ActionForward actionForward = new ActionForward();
		actionForward.setName("none");
		return actionForward;
	}
		
	public ActionForward stampaFatturaPDF(ActionMapping mapping,
			DocumentoVO doc, HttpServletRequest request,
			HttpServletResponse response, int aooId) throws Exception {
		
		ActionMessages errors = ProtocolloFileUtility.stampaFatturaPDF(doc, request, response, aooId);
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return mapping.findForward("erroreDownload");
		}
		ActionForward actionForward = new ActionForward();
		actionForward.setName("none");
		return actionForward;
	}
	
	protected void caricaProtocollo(HttpServletRequest request,
			ProtocolloForm form, String type) {
		if (type == null || type.equals(CaricaProtocollo.PROTOCOLLO_INGRESSO)) {
			CaricaProtocollo.caricaProtocolloIngresso(request, form);
		} else if (type.equals(CaricaProtocollo.PROTOCOLLO_USCITA)) {
			CaricaProtocollo.caricaProtocolloUscita(request, form);
		} else if (type.equals(CaricaProtocollo.POSTA_INTERNA)) {
			CaricaProtocollo.caricaPostaInterna(request, form);
		} else if (type.equals(CaricaProtocollo.DOMANDE_ERSU)) {
			CaricaProtocollo.caricaDomandaErsu(request, form);
		} else if (type.equals(CaricaProtocollo.FATTURE)) {
			CaricaProtocollo.caricaFatture(request, form);
		}
	}

	protected abstract void assegnaAdUfficio(ProtocolloForm form,
			int ufficioId, Utente utente);

	protected abstract void resetForm(ProtocolloForm form, Utente utente,
			HttpServletRequest request);

	protected abstract void assegnaAdUtente(ProtocolloForm form, Utente utente);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession(true);
		if (form instanceof PostaInternaForm)
			type = CaricaProtocollo.POSTA_INTERNA;
		ProtocolloForm pForm = (ProtocolloForm) form;
		if (session.getAttribute("modifica") != null) {
			session.removeAttribute("modifica");
			pForm.setDaBtnModifica(true);
		}
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		boolean ufficioCompleto = true;
		if (type != CaricaProtocollo.POSTA_INTERNA)
			ufficioCompleto = utente.getUfficioVOInUso().getTipo().equals("C") ? true
					: false;
		caricaProtocollo(request, pForm, type);
		if (pForm.getProtocolloId() == 0
				|| request.getParameter("annullaAction") != null
				|| request.getParameter("btnCopiaProtocollo") != null) {
			RegistroVO registro = (RegistroVO) utente.getRegistroVOInUso();
			pForm.setDataRegistrazione(DateUtil.formattaData(RegistroBO
					.getDataAperturaRegistro(registro).getTime()));
		}
	
		if (request.getParameter("nomeFilePrincipaleUploadScanner") != null) {

			String nomeFile = request
					.getParameter("nomeFilePrincipaleUploadScanner");
			if (!nomeFile.trim().equals("")) {
				pForm.setNomeFilePrincipaleUpload(nomeFile);
				pForm.getDocumentoPrincipale().setFileName(nomeFile + ".pdf");
				pForm.getDocumentoPrincipale().setDescrizione(nomeFile);
			}

		}
		if (request.getParameter("nomeFileUploadScanner") != null) {
			pForm.setAllegatoScansionato(false);
			String nomeFile = request.getParameter("nomeFileUploadScanner");
			if (!nomeFile.trim().equals("")) {
				if (pForm.getDocumentiAllegatiCollection().size() != 0) {
					pForm.getUltimoDocumentoAllegato().setFileName(
							nomeFile + ".pdf");
					pForm.getUltimoDocumentoAllegato().setDescrizione(nomeFile);
				}

			}
		}
		if (pForm.getUfficioCorrenteId() == 0) {
			AlberoUfficiBO.impostaUfficioUtentiAbilitati(utente, pForm,
					ufficioCompleto);
			pForm.setUtenteAbilitatoSuUfficio(!utente.getUfficioVOInUso()
					.getTipo().equals(UfficioVO.UFFICIO_NORMALE));
			if (pForm.getOggettoFromOggettario() != null) {
				pForm.setOggettoGenerico(pForm.getOggetto());
			}
		}
		
		if (request.getParameter("btnNuovoProtocollo") != null) {
			RegistroVO registro = ProtocolloDelegate.getInstance()
					.getRegistroByProtocolloId(pForm.getProtocolloId());
			pForm.setDataRegistrazione(DateUtil.formattaData(RegistroBO
					.getDataAperturaRegistro(registro).getTime()));
			pForm.setNumeroProtocollo(null);
			resetForm(pForm, utente, request);
			saveToken(request);
			if (registro.getCodRegistro().equals("Fatt"))
				return mapping.findForward("nuovaFattura");
			else
				return mapping.findForward("nuovoProtocollo");

		} else if (request.getParameter("btnCopiaProtocollo") != null) {
			pForm.inizializzaFormToCopyProtocollo();
			saveToken(request);
			return mapping.findForward("nuovoProtocollo");

		} else if (request.getParameter("annullaAction") != null) {
			if ("true".equals(request.getParameter("annullaAction"))
					|| pForm.getProtocolloId() == 0) {
				newProtocollo(session, pForm, utente);
			} else {
				request.setAttribute("protocolloId", new Integer(pForm
						.getProtocolloId()));
				caricaProtocollo(request, pForm, type);
			}
			saveToken(request);

			return mapping.findForward("input");
		} else if (request.getParameter("annullaActionAllegato") != null) {
			request.setAttribute("protocolloId", new Integer(pForm
					.getProtocolloId()));
			caricaProtocollo(request, pForm, type);
			saveToken(request);
			return mapping.findForward("visualizzaProtocollo");
		} else if (request.getParameter("btnModificaProtocollo") != null
				&& request.getAttribute("protocolloId") == null) {
			request.setAttribute("protocolloId", new Integer(pForm
					.getProtocolloId()));
			pForm.setEstremiAutorizzazione(null);
			session.setAttribute("modifica", true);
			saveToken(request);
			return mapping.findForward("modificaProtocollo");
		} else if (request.getParameter("btnFascicola") != null
				&& request.getAttribute("protocolloId") == null) {
			request.setAttribute("protocolloId", new Integer(pForm
					.getProtocolloId()));
			saveToken(request);
			return mapping.findForward("modificaProtocollo");
		} else if (request.getParameter("allegaDocumentoAction") != null) {
			ProtocolloFileUtility.uploadAllegato(pForm, request, errors);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			}
			return mapping.findForward("input");
		} else if (request.getParameter("allegaDocumentoScannAction") != null) {
			if (!(ServletFileUpload.isMultipartContent(request))) {
				errors.add("allegati", new ActionMessage("no_multipart"));
			} else {
				String nomeFile = request.getParameter("fileName");
				FormFile image = (FormFile) form.getMultipartRequestHandler().getFileElements().get("formFileUpload");
				pForm.putImage(nomeFile, image);
				String filetotals = request.getParameter("filetotals");
				String fileNumber = request.getParameter("fileNumber");
				if(filetotals.equals(fileNumber)){
					pForm.setAllegatoScansionato(true);
					ProtocolloFileUtility.uploadAllegatoScanner(pForm, Constants.SCANNER_FILE_NAME, request, errors);
				}
			}
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			}
			return mapping.findForward("input");
		} else if (request.getParameter("allegaDocumentoPrincipaleScannAction") != null) {
			if (!(ServletFileUpload.isMultipartContent(request))) {
				errors.add("allegati", new ActionMessage("no_multipart"));
			} else {
				String nomeFile = request.getParameter("fileName");
				FormFile image = (FormFile) form.getMultipartRequestHandler().getFileElements().get("filePrincipaleUpload");
				pForm.putImage(nomeFile, image);
				String filetotals = request.getParameter("filetotals");
				String fileNumber = request.getParameter("fileNumber");
				if(filetotals.equals(fileNumber)){
					pForm.setAllegatoScansionato(true);
					ProtocolloFileUtility.uploadDocumentoPrincipaleScanner(pForm, Constants.SCANNER_FILE_NAME, request, errors);
				}
			}
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			}
			return mapping.findForward("input");
		}
		
		//TODO VECCHIO SCANNER
	 	else if (request.getParameter("allegaDocumentoScannOldAction") != null) {
			pForm.setFormFileUpload(pForm.getFilePrincipaleUpload());
			pForm.setFilePrincipaleUpload(null);
			pForm.setAllegatoScansionato(true);
			ProtocolloFileUtility.uploadAllegato(pForm, request, errors);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			}
			return mapping.findForward("input");
		} else if (request.getParameter("allegaDocumentoPrincipaleScannOldAction") != null) {
			ProtocolloFileUtility.uploadDocumentoPrincipale(pForm, request, errors);
			if (!errors.isEmpty())
				saveErrors(request, errors);
			return mapping.findForward("allegaDocProtocollo");
		}
		//
		
		else if (request.getParameter("allegaDocumentoPrincipaleAction") != null) {
			ProtocolloFileUtility.uploadDocumentoPrincipale(pForm, request, errors);
			if (!errors.isEmpty())
				saveErrors(request, errors);
			return mapping.findForward("input");
		} else if (request.getParameter("allacciaProtocolloAction") != null) {
			allacciaProtocollo(pForm, session, errors);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			}
			return mapping.findForward("input");

		} else if (request.getParameter("btnAllacci") != null) {
			ArrayList navBar = (ArrayList) session
					.getAttribute(Constants.NAV_BAR);
			NavBarElement elem = new NavBarElement();
			elem.setValue("Allacci");
			elem.setTitle("Cerca protocolli allacciabili");
			navBar.add(elem);
			return mapping.findForward("cercaAllacci");

		} else if (request.getParameter("rimuoviAllegatiAction") != null) {
			String[] allegati = pForm.getAllegatiSelezionatiId();
			if (allegati != null) {
				for (int i = 0; i < allegati.length; i++) {
					pForm.rimuoviAllegato(allegati[i]);
					allegati[i] = null;
				}
			}
			return mapping.findForward("input");

		} else if (request.getParameter("rimuoviDocumentoPrincipaleAction") != null) {
			pForm.rimuoviDocumentoPrincipale();
			return mapping.findForward("input");

		} else if (request
				.getParameter("rimuoviDocumentoPrincipaleScannAction") != null) {
			pForm.rimuoviDocumentoPrincipale();
			return mapping.findForward("allegaDocProtocollo");

		} else if (request.getParameter("rimuoviAllacciAction") != null) {
			removeAllacci(pForm);
			return mapping.findForward("input");

		} else if (request.getParameter("impostaUfficioAction") != null) {
			pForm.setUfficioCorrenteId(pForm.getUfficioSelezionatoId());
			pForm.setUtenteAbilitatoSuUfficio(!utente.getUfficioVOInUso()
					.getTipo().equals(UfficioVO.UFFICIO_NORMALE));
			AlberoUfficiBO.impostaUfficioUtentiAbilitati(utente, pForm,
					ufficioCompleto);
			return mapping.findForward("input");
		} else if (request.getParameter("ufficioPrecedenteAction") != null) {
			pForm
					.setUfficioCorrenteId(pForm.getUfficioCorrente()
							.getParentId());
			pForm.setUtenteAbilitatoSuUfficio(!utente.getUfficioVOInUso()
					.getTipo().equals(UfficioVO.UFFICIO_NORMALE));
			AlberoUfficiBO.impostaUfficioUtentiAbilitati(utente, pForm,
					ufficioCompleto);
		} else if (request.getParameter("assegnaUfficioCorrenteAction") != null) {
			assegnaAdUfficio(pForm, pForm.getUfficioSelezionatoId(), utente);
			return mapping.findForward("input");

		} else if (request.getParameter("assegnaUfficioSelezionatoAction") != null) {
			assegnaAdUfficio(pForm, pForm.getUfficioSelezionatoId(), utente);
			return mapping.findForward("input");

		} else if (request.getParameter("assegnaUtenteAction") != null) {
			assegnaAdUtente(pForm, utente);
			return mapping.findForward("input");

		} else if (request.getParameter("dettaglioAction") != null) {
			return mapping.findForward("dettaglio_documento");

		} else if (request.getParameter("btnAnnullaProtocollo") != null) {
			pForm.setNotaAnnullamento(null);
			pForm.setProvvedimentoAnnullamento(null);
			return mapping.findForward("annullaProtocollo");
		} else if (request.getParameter("btnConfermaAnnullamento") != null) {
			errors = pForm.validateAnnullamentoProtocollo(mapping, request);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return (mapping.findForward("annullaProtocollo"));
			}
			ProtocolloVO protocolloVO = ProtocolloDelegate.getInstance()
					.getProtocolloVOById(pForm.getProtocolloId());
			if (protocolloVO != null) {
				protocolloVO.setStatoProtocollo(FLAG_PROTOCOLLO_ANNULLATO);
				protocolloVO.setNotaAnnullamento(pForm.getNotaAnnullamento());
				protocolloVO.setProvvedimentoAnnullamento(pForm
						.getProvvedimentoAnnullamento());
				if (ProtocolloDelegate.getInstance().annullaProtocollo(
						protocolloVO, utente) == ReturnValues.SAVED) {
					pForm.setModificabile(false);
					pForm.setDataAnnullamento(DateUtil.formattaData((new Date(
							System.currentTimeMillis())).getTime()));
					pForm.setStato(FLAG_PROTOCOLLO_ANNULLATO);
					pForm.setAutore(utente.getValueObject().getUsername());
				}
				request.setAttribute("protocolloId", protocolloVO.getId());
				return (mapping.findForward("input"));
			}

		} else if (request.getParameter("ripetiAction") != null) {
			if ("true".equals(request.getParameter("ripetiAction"))
					|| pForm.getProtocolloId() == 0) {
				pForm.inizializzaRipetiForm();
				session.removeAttribute("tornaDocumento");
				pForm.setAooId(utente.getRegistroVOInUso().getAooId());
				pForm.setVersioneDefault(true);
			} else {
				request.setAttribute("protocolloId", new Integer(pForm
						.getProtocolloId()));
				caricaProtocollo(request, pForm, type);
			}
			saveToken(request);
			return mapping.findForward("input");

		} else if (request.getParameter("btnStoriaProtocollo") != null) {
			request.setAttribute("protocolloId", new Integer(pForm
					.getProtocolloId()));
			return mapping.findForward("storiaProtocollo");
		} else if (request.getParameter("btnIndietro") != null) {			
			return mapping.findForward("tornaRicerca");
		} else if (request.getParameter("btnCercaFascicoli") != null) {
			String nomeFascicolo = pForm.getCercaFascicoloNome();
			pForm.setCercaFascicoloNome("");
			request.setAttribute("cercaFascicoliDaProtocollo", nomeFascicolo);
			session.setAttribute("tornaProtocollo", Boolean.TRUE);
			session.removeAttribute("tornaFaldone");
			session.removeAttribute("provenienza");
			session.removeAttribute("cercaFascicoliDaFaldoni");
			session.removeAttribute("tornaFaldone");
			session.removeAttribute("FascicoliDaFaldoni");
			session.removeAttribute("btnCercaFascicoli");
			session.removeAttribute("tornaProcedimento");
			return mapping.findForward("cercaFascicolo");

		} else if (request.getParameter("btnCercaProcedimenti") != null) {
			String oggettoProcedimento = pForm.getOggettoProcedimento();
			pForm.setOggettoProcedimento("");
			request.setAttribute("cercaProcedimentiDaProtocollo",
					oggettoProcedimento);
			session.setAttribute("procedimentiDaProtocollo", Boolean.TRUE);
			session.setAttribute("indietroProcedimentiDaProtocollo",
					Boolean.TRUE);
			session.setAttribute("risultatiProcedimentiDaProtocollo",
					Boolean.TRUE);
			session.removeAttribute("tornaProtocollo");
			session.removeAttribute("ricercaSemplice");
			session.removeAttribute("tornaFaldone");
			session.removeAttribute("provenienza");
			session.removeAttribute("btnCercaProcedimentiDaFaldoni");
			session.removeAttribute("tornaFascicolo");
			return mapping.findForward("cercaProcedimento");

		} else if (request.getParameter("rimuoviFascicoli") != null) {
			ProcedimentoDelegate procedimentoDelegate=ProcedimentoDelegate.getInstance();
			if (pForm.getFascicoloSelezionatoId() != null) {
				String[] fascicoli = pForm.getFascicoloSelezionatoId();
				for (int i = 0; i < fascicoli.length; i++) {
					if (fascicoli[i] != null) {
						pForm.rimuoviFascicolo(Integer.parseInt(fascicoli[i]));
						pForm.rimuoviProcedimento(procedimentoDelegate.getProcedimentoByFascicoloId(Integer.parseInt(fascicoli[i])));
						fascicoli[i] = null;
					}
				}
			}
			if (pForm.getFascicoloSelezionatoOldId() != null) {
				String[] fascicoli = pForm.getFascicoloSelezionatoOldId();
				/*  */
				for (int i = 0; i < fascicoli.length; i++) {
					if (fascicoli[i] != null) {
						pForm.rimuoviFascicoloOld(Integer.parseInt(fascicoli[i]));
						pForm.rimuoviProcedimento(procedimentoDelegate.getProcedimentoByFascicoloId(Integer.parseInt(fascicoli[i])));
						fascicoli[i] = null;
					}
				}
			}
			return mapping.findForward("input");

		} else if (request.getParameter("rimuoviProcedimenti") != null) {
			ProcedimentoDelegate procedimentoDelegate=ProcedimentoDelegate.getInstance();
			String[] procedimenti = pForm.getProcedimentoSelezionatoId();
			if (procedimenti != null) {
				for (int i = 0; i < procedimenti.length; i++) {
					if (procedimenti[i] != null) {
						pForm.rimuoviProcedimento(Integer.parseInt(procedimenti[i]));
						pForm.rimuoviFascicoloOld(procedimentoDelegate.getFascicoloIdByProcedimento(Integer.parseInt(procedimenti[i])));
						pForm.rimuoviFascicolo(procedimentoDelegate.getFascicoloIdByProcedimento(Integer.parseInt(procedimenti[i])));
						procedimenti[i] = null;
					}
				}
			}
			return mapping.findForward("input");

		} else if (request.getParameter("btnNuovoFascicolo") != null) {
			session.setAttribute("tornaProtocollo", Boolean.TRUE);
			return mapping.findForward("nuovoFascicolo");

		} else if (request.getParameter("btnRimuoviFascicolo") != null) {
			return mapping.findForward("rimuoviFascicolo");

		} else if (request.getParameter("btnNuovoProcedimento") != null) {
			creaNuovoProcedimento(request, session, pForm, utente);
			return mapping.findForward("nuovoProcedimento");
		} else if (request.getParameter("fascicoloId") != null) {
			request.setAttribute("fascicoloId", new Integer(request.getParameter("fascicoloId")));
			session.setAttribute("tornaProtocollo", Boolean.TRUE);
			return (mapping.findForward("fascicolo"));
		}
		//
		else if (request.getParameter("visualizzaProtocolloAllacciatoId") != null) {
			int id = new Integer(request
					.getParameter("visualizzaProtocolloAllacciatoId"));
			ReportProtocolloView r = ProtocolloDelegate.getInstance()
					.getProtocolloView(id);
			request.setAttribute("protocolloId", id);
			request.setAttribute("type", r.getTipoProtocollo());
			request.setAttribute("tornaType", pForm.getFlagTipo());
			return (mapping.findForward("visualizzaAllaccio"));
		} else if (request.getParameter("visualizzaProtocolloAllacciatoViewId") != null) {
			int id = new Integer(request
					.getParameter("visualizzaProtocolloAllacciatoViewId"));
			ReportProtocolloView r = ProtocolloDelegate.getInstance()
					.getProtocolloView(id);
			request.setAttribute("protocolloId", id);
			request.setAttribute("type", r.getTipoProtocollo());
			request.setAttribute("tornaProtocolloId", pForm.getProtocolloId());
			request.setAttribute("tornaType", pForm.getFlagTipo());
			return (mapping.findForward("visualizzaAllaccio"));
		} else if (request.getParameter("btnStampaEtichettaProtocollo") != null) {
			request.setAttribute("protocolloId", new Integer(pForm.getProtocolloId()));
			return (mapping.findForward("stampaEtichetta"));
		} else if (request.getParameter("btnAllegaStampaEtichettaProtocollo") != null) {
			request.setAttribute("protocolloId", new Integer(pForm.getProtocolloId()));
			return (mapping.findForward("allegaStampaEtichetta"));
		} else if (request.getParameter("visualizzaFatturaBrowser") != null) {
			DocumentoVO doc = pForm.getDocumentoPrincipale();
			int aooId = utente.getAreaOrganizzativa().getId();
			if(ProtocolloFileUtility.isDocumentoReadable(pForm.getProtocolloId(), pForm.getFlagTipo(), pForm.getUfficioProtocollatoreId(), 0, pForm.getFascicoliProtocolloOld(), utente))
				return visualizzaFatturaElettronica(mapping, doc, request, response, aooId);
			else{
				errors.add("documento",new ActionMessage("download_doc_non_abilitato","errors"));
				saveErrors(request, errors);
			}
			return mapping.findForward("input");

		} else if (request.getParameter("scaricaFatturaPDF") != null) {
			DocumentoVO doc = pForm.getDocumentoPrincipale();
			int aooId = utente.getAreaOrganizzativa().getId();
			if(ProtocolloFileUtility.isDocumentoReadable(pForm.getProtocolloId(), pForm.getFlagTipo(), pForm.getUfficioProtocollatoreId(), 0, pForm.getFascicoliProtocolloOld(), utente))
				return stampaFatturaPDF(mapping, doc, request, response, aooId);
			else{
				errors.add("documento",new ActionMessage("download_doc_non_abilitato","errors"));
				saveErrors(request, errors);
			}
			return mapping.findForward("input");

		}

		return null;
	}

	/**
	 * @param session
	 * @param pForm
	 * @param utente
	 */
	private void newProtocollo(HttpSession session, ProtocolloForm pForm,
			Utente utente) {
		pForm.inizializzaForm();
		session.removeAttribute("tornaDocumento");
		pForm.setAooId(utente.getRegistroVOInUso().getAooId());
		pForm.setTipoDocumentoId(0);
		pForm.setVersioneDefault(true);
		if (session.getAttribute("PROTOCOLLI_EMERGENZA") != null) {
			pForm.setNumeroProtocolliRegistroEmergenza(((Integer) session
					.getAttribute("PROTOCOLLI_EMERGENZA")).intValue());

		} else {
			pForm.setNumeroProtocolliRegistroEmergenza(0);
		}
	}

	private void creaNuovoProcedimento(HttpServletRequest request,
			HttpSession session, ProtocolloForm pForm, Utente utente) {
		ProcedimentoVO newP = new ProcedimentoVO();
		newP.setUfficioId(utente.getUfficioInUso());
		newP.setOggetto(pForm.getOggetto());
		newP.setNumeroProtovollo(DateUtil.getYear(DateUtil.toDate(pForm
				.getDataRegistrazione()))
				+ StringUtil.formattaNumeroProtocollo(pForm
						.getNumeroProtocollo(), 7));
		newP.setDataAvvio(DateUtil.getData(pForm.getDataRegistrazione()));
		newP.setProtocolloId(pForm.getProtocolloId());
		
		request.setAttribute("procedimentoPrecaricato", newP);
		session.setAttribute("tornaProtocollo", Boolean.TRUE);
	}

	private void removeAllacci(ProtocolloForm pForm) {
		String[] allacci = pForm.getAllaccioSelezionatoId();
		if (allacci != null) {
			for (int i = 0; i < allacci.length; i++) {
				if (allacci[i] != null) {
					pForm.rimuoviAllaccio(Integer.parseInt(allacci[i]));
					allacci[i] = null;
				}
			}
		}
	}
}