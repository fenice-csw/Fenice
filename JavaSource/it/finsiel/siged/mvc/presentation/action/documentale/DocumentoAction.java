package it.finsiel.siged.mvc.presentation.action.documentale;

import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.compit.fenice.mvc.vo.protocollo.DocumentoFascicoloVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.constant.PermessiConst;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.dao.digitalsignature.VerificaFirma;
import it.finsiel.siged.exception.CRLNonAggiornataException;
import it.finsiel.siged.exception.CertificatoNonValidoException;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.exception.FirmaNonValidaException;
import it.finsiel.siged.model.Fascicolo;
import it.finsiel.siged.model.documentale.Documento;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.AlberoUfficiBO;
import it.finsiel.siged.mvc.bo.DocumentaleBO;
import it.finsiel.siged.mvc.bo.TitolarioBO;
import it.finsiel.siged.mvc.business.DocumentaleDelegate;
import it.finsiel.siged.mvc.business.FascicoloDelegate;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.business.ProcedimentoDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.documentale.DocumentoForm;
import it.finsiel.siged.mvc.presentation.helper.PermessoDocumentoView;
import it.finsiel.siged.mvc.vo.IdentityVO;
import it.finsiel.siged.mvc.vo.documentale.CartellaVO;
import it.finsiel.siged.mvc.vo.documentale.FileVO;
import it.finsiel.siged.mvc.vo.documentale.PermessoFileVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.ProcedimentoVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.FileUtil;
import it.finsiel.siged.util.NumberUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;


public class DocumentoAction extends Action {

	static Logger logger = Logger.getLogger(DocumentoAction.class.getName());

	private void uploadDocumento(DocumentoForm form,
			HttpServletRequest request, ActionMessages errors) {
		FormFile file = form.getFilePrincipaleUpload();
		String fileName = file.getFileName();
		String contentType = file.getContentType();
		logger.info(contentType);
		int size = file.getFileSize();
		if (!"".equals(fileName) && fileName.length() > 100) {
			errors.add("documento", new ActionMessage("error.nomefile.lungo",
					"", ""));
		} else if (size > 0 && !"".equals(fileName)) {
			String tempFilePath = FileUtil.leggiFormFilePrincipale(form,
					request, errors);
			String impronta = FileUtil.calcolaDigest(tempFilePath, errors);
			if (errors.isEmpty()) {
				try {
					VerificaFirma
							.verificaFileFirmato(tempFilePath, contentType);
				} catch (DataException e) {
					errors.add("allegati", new ActionMessage(
							"database.cannot.load"));
				} catch (CertificatoNonValidoException e) {
					errors.add("allegati", new ActionMessage(
							"errore.verificafirma.doc.non_valido", e
									.getMessage()));
				} catch (FirmaNonValidaException e) {
					errors.add("allegati", new ActionMessage(
							"errore.verificafirma.doc.non_valido", e
									.getMessage()));
				} catch (CRLNonAggiornataException e) {
					errors.add("allegati", new ActionMessage(
							"errore.verificafirma.crl_non_aggiornata"));
				}
				String username = ((Utente) request.getSession().getAttribute(
						Constants.UTENTE_KEY)).getValueObject().getUsername();
				DocumentoVO documento = new DocumentoVO();
				documento.setDescrizione(null);
				documento.setFileName(fileName);
				documento.setPath(tempFilePath);
				documento.setImpronta(impronta);
				documento.setSize(size);
				documento.setContentType(contentType);
				documento
						.setRowCreatedTime(new Date(System.currentTimeMillis()));
				documento
						.setRowUpdatedTime(new Date(System.currentTimeMillis()));
				documento.setRowCreatedUser(username);
				documento.setRowUpdatedUser(username);
				form.setDocumentoPrincipale(documento);
			}
		} else {
			errors.add("documento", new ActionMessage("campo.obbligatorio",
					"File", ""));
		}

	}

	public void aggiornaForm(Documento documento, DocumentoForm form,
			HttpSession session) {
		FileVO fileVO = documento.getFileVO();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);

		form.inizializzaForm();

		form.setAooId(utente.getUfficioVOInUso().getAooId());
		form.setUtenteCorrenteId(utente.getValueObject().getId().intValue());
		form.setVersioneDefault(documento.isVersioneDefault());
		// dati generali
		aggiornaDatiGeneraliForm(documento.getFileVO(), form);

		// allegato principale
		form.setDocumentoPrincipale(documento.getFileVO().getDocumentoVO());

		// permessi
		aggiornaPermessiForm(documento, form);

		// argomento titolario
		aggiornaTitolarioForm(fileVO, form, utente);

		// Fascicolo
		aggiornaFascicoloForm(fileVO, form, utente);

		form.setProcedimentoId(fileVO.getProcedimentoId());

	}

	private void aggiornaDatiGeneraliForm(FileVO documento, DocumentoForm form) {
		Integer id = documento.getId();
		if (id != null) {
			form.setDocumentoId(id.intValue());
		} else {
			form.setDocumentoId(0);
		}
		Date dataDoc = documento.getDataDocumento();
		form.setDataDocumento(dataDoc == null ? null : DateUtil
				.formattaData(dataDoc.getTime()));
		form.setDescrizione(documento.getDescrizione());
		form.setDescrizioneArgomento(documento.getDescrizioneArgomento());
		form.setTipoDocumentoId(documento.getTipoDocumentoId());
		form.setOggetto(documento.getOggetto());
		form.setNomeFile(documento.getNomeFile());
		form.setNote(documento.getNote());
		form.setVersione(documento.getVersione());
		form.setStatoArchivio(documento.getStatoArchivio());
		form.setStatoDocumento(documento.getStatoDocumento());
		form.setUserLavId(documento.getCaricaLavId());
		form.setUsernameLav(documento.getCaricaLav());
		form.setCartellaId(documento.getCartellaId());
		form.setOwner(documento.getOwnerCarica());
		form.setAssegnatoDa(documento.getAssegnatario());
		form.setMessaggio(documento.getMessaggio());
		form.setRepositoryFileId(documento.getRepositoryFileId());
	}

	private void aggiornaTitolarioForm(FileVO documento, DocumentoForm form,
			Utente utente) {
		int titolarioId = documento.getTitolarioId();
		TitolarioBO.impostaTitolario(form, utente.getUfficioInUso(),
				titolarioId);
		form.setDescrizioneArgomento(documento.getDescrizioneArgomento());
	}

	private void aggiornaFascicoloForm(FileVO documento, DocumentoForm form,
			Utente utente) {
		if (documento != null && documento.getFascicoli() != null) {
			for (Iterator<FascicoloVO> i = documento.getFascicoli().iterator(); i.hasNext();) {
				FascicoloVO fascicolo =  i.next();
				form.aggiungiFascicolo(fascicolo);
			}
		}
	}

	public ActionForward downloadDocumento(ActionMapping mapping,
			DocumentoVO doc, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		InputStream is = null;
		OutputStream os = null;
		ActionMessages errors = new ActionMessages();
		try {

			if (doc != null) {
				os = response.getOutputStream();
				response.setContentType(doc.getContentType());
				response.setHeader("Content-Disposition",
						"attachment;filename=" + doc.getFileName());
				response.setHeader("Cache-control", "");
				if (doc.getId() != null) { // documento esistente
					// prendiamo i dati dal Repository
					DocumentaleDelegate.getInstance().writeDocumentToStream(
							doc.getId().intValue(), os);
				} else { // documento ancora da sa?vare: i file sono nella
					// cartella temporanea
					is = new FileInputStream(doc.getPath());
					FileUtil.writeFile(is, os);
				}

			}
		} catch (FileNotFoundException e) {
			logger.error("", e);
			errors.add("download", new ActionMessage("error.notfound"));
		} catch (IOException e) {
			logger.error("", e);
			errors.add("download", new ActionMessage("error.cannot.read"));
		} catch (DataException e) {
			logger.error("", e);
			errors.add("download", new ActionMessage("error.cannot.read"));
		} finally {
			FileUtil.closeIS(is);
			FileUtil.closeOS(os);
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return mapping.findForward("erroreDownload");
		}
		return null;
	}

	public void aggiornaModel(DocumentoForm form, Documento documento,
			Utente utente) {
		FileVO fileVO = documento.getFileVO();
		// documento principale
		documento.getFileVO().setDocumentoVO(form.getDocumentoPrincipale());

		aggiornaDatiGeneraliModel(form, fileVO);
		// titolario
		if (form.getTitolario() != null) {
			fileVO.setTitolarioId(form.getTitolario().getId().intValue());
		} else {
			fileVO.setTitolarioId(0);
		}
		fileVO.setFascicoli(form.getFascicoliDocumento());
		aggiornaPermessiModel(form, documento, utente);

	}

	
	private void aggiornaDatiGeneraliModel(DocumentoForm form, FileVO documento) {
		documento.setId(form.getDocumentoId());
		documento.setTipoDocumentoId(form.getTipoDocumentoId());
		Date dataDoc = DateUtil.toDate(form.getDataDocumento());
		if (dataDoc != null) {
			documento.setDataDocumento(new java.sql.Date(dataDoc.getTime()));
		} else {
			documento.setDataDocumento(null);
		}
		documento.setRepositoryFileId(form.getRepositoryFileId());
		documento.setCartellaId(form.getCartellaId());
		if (form.getDocumentoId() > 0) { // se esiste imposto i campi se no
			documento.setStatoDocumento(form.getStatoDocumento());
			documento.setStatoArchivio(form.getStatoArchivio());
		}
		documento.setOggetto(form.getOggetto());
		documento.setDescrizione(form.getDescrizione());
		documento.setDescrizioneArgomento(form.getDescrizioneArgomento());
		documento.setNote(form.getNote());
		if (form.getDocumentoPrincipale().getId() == null
				|| form.getDocumentoPrincipale().getId().intValue() == 0)
			documento.setNomeFile(form.getDocumentoPrincipale().getFileName());
		else
			documento.setNomeFile(form.getNomeFile());
		documento.setRowCreatedTime(new java.sql.Date(System
				.currentTimeMillis()));
		documento.setVersione(form.getVersione());
		documento.setCaricaLavId(form.getUserLavId());
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession(true);
		DocumentoForm pForm = (DocumentoForm) form;
		DocumentaleDelegate delegate = DocumentaleDelegate.getInstance();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		boolean ufficioCompleto = (utente.getUfficioVOInUso().getTipo().equals(
				UfficioVO.UFFICIO_CENTRALE) || utente.getUfficioVOInUso()
				.getTipo().equals(UfficioVO.UFFICIO_SEMICENTRALE));
		String cartellaSelezionataId = (String) request
				.getAttribute("cartellaSelezionataId");
		if (cartellaSelezionataId != null) {
			pForm.setCartellaId(NumberUtil.getInt(cartellaSelezionataId));
			if (session.getAttribute("tornaProcedimento") != null) {
				pForm.setFromProcedimento(true);
				pForm.setProcedimentoId((Integer) session
						.getAttribute("tornaProcedimento"));
				session.removeAttribute("tornaProcedimento");
			}
		}
		caricaDocumento(request, pForm, errors);
		if (pForm.getTitolario() == null) {
			TitolarioBO.impostaTitolario(pForm, utente.getUfficioInUso(), 0);
		}

		if (pForm.getUfficioCorrenteId() == 0) {
			AlberoUfficiBO.impostaUfficioUtentiAbilitati(utente, pForm,
					ufficioCompleto);
		}
		pForm.setAooId(utente.getUfficioVOInUso().getAooId());

		if (request.getParameter("visualizzaClassificaDocumentoAction") != null) {
			return mapping.findForward("visualizzaClassificaDocumento");
		} else if (request.getParameter("classificaDocumentoAction") != null) {
			// classifica
			errors = pForm.validate(mapping, request);
			if (errors.isEmpty()) {
				try {
					DocumentaleDelegate.getInstance().classificaDocumento(
							pForm.getDocumentoId(),
							pForm.getTitolario().getId().intValue());
					ActionMessages msg = new ActionMessages();
					msg.add("operazioneEffettuata", new ActionMessage(
							"msg.operazione.effettuata"));
					saveMessages(request, msg);
					pForm.setStatoArchivio(Parametri.STATO_CLASSIFICATO);
				} catch (DataException e) {
					errors.add("generale", new ActionMessage(
							"errore_nel_salvataggio"));
				}
			}
			saveErrors(request, errors);
			if (!errors.isEmpty())
				return mapping.findForward("classificaDocumento");
			else
				return mapping.findForward("viewDocumento");
		} else if (request.getParameter("spostaDocumentoAction") != null) {
			request.setAttribute("gotoCartellaId", String.valueOf(pForm
					.getCartellaId()));
			request.setAttribute("spostaDocumentoId", String.valueOf(pForm
					.getDocumentoId()));
			return mapping.findForward("spostaDocumento");
		} else if (request.getParameter("inviaDocumentoAction") != null) {
			request.setAttribute("gotoCartellaId", String.valueOf(pForm
					.getCartellaId()));
			request.setAttribute("spostaDocumentoId", String.valueOf(pForm
					.getDocumentoId()));
			if (pForm.isFromProcedimento())
				request.setAttribute("fromProcedimento", pForm
						.getProcedimentoId());
			return mapping.findForward("inviaDocumentoUtente");
		} else if (request.getParameter("spostaDocumentoInLavorazioneAction") != null) {
			errors = pForm.validate(mapping, request);
			if (errors.isEmpty()) {
				try {
					DocumentaleDelegate.getInstance().spostaInLavorazione(
							pForm.getDocumentoId());
					pForm.setStatoArchivio(Parametri.STATO_LAVORAZIONE);
					pForm.setStatoDocumento(Parametri.CHECKED_IN);
					TitolarioBO.impostaTitolario(pForm, utente
							.getUfficioInUso(), 0);
					ActionMessages msg = new ActionMessages();
					msg.add("operazioneEffettuata", new ActionMessage(
							"msg.operazione.effettuata"));
					saveMessages(request, msg);
					return mapping.findForward("viewDocumento");
				} catch (DataException e) {
					errors.add("generale", new ActionMessage(
							"errore_nel_salvataggio"));
				}
			}
			saveErrors(request, errors);
			return mapping.findForward("input");
		} else if (request.getParameter("checkoutDocumentoAction") != null) {
			errors = pForm.validate(mapping, request);
			if (errors.isEmpty()) {
				try {
					int retVal = DocumentaleDelegate.getInstance()
							.checkoutDocumento(pForm.getDocumentoId(),
									utente.getValueObject().getId().intValue());
					if (retVal == ReturnValues.VALID) {
						pForm.setStatoDocumento(Parametri.CHECKED_OUT);
						pForm.setUserLavId(utente.getValueObject().getId()
								.intValue());
						pForm.setUtenteCorrenteId(utente.getValueObject()
								.getId().intValue());
						pForm.setUsernameLav(utente.getValueObject()
								.getFullName());
						ActionMessages msg = new ActionMessages();
						msg.add("operazioneEffettuata", new ActionMessage(
								"msg.operazione.effettuata"));
						saveMessages(request, msg);
						return mapping.findForward("viewDocumento");
					} else if (retVal == ReturnValues.OLD_VERSION) {
						errors.add("generale", new ActionMessage(
								"errore.stato.versione_vecchia"));
					} else if (retVal == ReturnValues.NOT_FOUND) {
						errors.add("generale", new ActionMessage(
								"errore.documento.nontrovato"));
					} else {
						errors.add("generale", new ActionMessage(
								"database.cannot.load"));
					}
				} catch (DataException e) {
					errors.add("generale", new ActionMessage(
							"errore_nel_salvataggio"));
				}
			}
			saveErrors(request, errors);
			return mapping.findForward("input");
		} else if (request.getParameter("checkinDocumentoAction") != null) {
			errors = pForm.validate(mapping, request);
			if (errors.isEmpty()) {
				try {
					DocumentaleDelegate.getInstance().checkinDocumento(
							pForm.getDocumentoId());
					pForm.setStatoDocumento(Parametri.CHECKED_IN);
					ActionMessages msg = new ActionMessages();
					msg.add("operazioneEffettuata", new ActionMessage(
							"msg.operazione.effettuata"));
					saveMessages(request, msg);
					return mapping.findForward("viewDocumento");
				} catch (DataException e) {
					errors.add("generale", new ActionMessage(
							"errore_nel_salvataggio"));
				}
			}
			saveErrors(request, errors);
			return mapping.findForward("input");
		} else if (request.getParameter("eliminaDocumentoAction") != null) {
			errors = pForm.validate(mapping, request);
			if (errors.isEmpty()) {
				try {
					DocumentaleDelegate.getInstance()
							.eliminaDocumento(pForm.getDocumentoId(),
									pForm.getRepositoryFileId());

					ActionMessages msg = new ActionMessages();
					msg.add("operazioneEffettuata", new ActionMessage(
							"cancellazione_ok"));
					saveMessages(request, msg);
					return mapping.findForward("paginaMessaggio");
				} catch (DataException e) {
					errors.add("generale", new ActionMessage(
							"errore_nel_salvataggio"));
				}
			}
		} else if (request.getParameter("viewStoriaDocumentoAction") != null) {
			errors = pForm.validate(mapping, request);
			if (errors.isEmpty()) {
				try {
					request.setAttribute("documentaleStoriaDocumenti",
							DocumentaleDelegate.getInstance()
									.getVersioniDocumento(
											pForm.getDocumentoId()));
				} catch (DataException e) {
					errors.add("generale", new ActionMessage(
							"errore_nel_salvataggio"));
				}
			} else {
				saveErrors(request, errors);
			}
			return mapping.findForward("storiaDocumento");
		} else if (request.getParameter("ripristinaVersioneDocumentoAction") != null) {
			Documento documento = new Documento();
			aggiornaModel(pForm, documento, utente);
			documento = DocumentaleDelegate.getInstance().aggiornaDocumento(
					documento, utente, true);
			FileVO fileVO = documento.getFileVO();
			if (fileVO.getReturnValue() == ReturnValues.SAVED) {
				// operazione avvenuta con successonomeUtente
				request.setAttribute("documentoId", fileVO.getId());
				caricaDocumento(request, pForm, errors);
				return mapping.findForward("viewDocumento");
			} else if (fileVO.getReturnValue() == ReturnValues.OLD_VERSION) {
				errors.add("generale", new ActionMessage("versione_vecchia"));
				saveErrors(request, errors);
			} else {
				errors.add("generale", new ActionMessage(
						"errore_nel_salvataggio"));
				saveErrors(request, errors);
			}
			return mapping.findForward("storiaDocumento");
		} else if (request.getParameter("visualizzaFascicolaDocumentoAction") != null) {
			// visualizza pagina per la fascicolazione del documento
			return mapping.findForward("visualizzaFascicolaDocumento");
		} else if (request.getParameter("fascicolaDocumentoAction") != null) {
			if(pForm.getFascicoliDocumento().size() == 0){
				errors.add("generale", new ActionMessage("documento.fascicoli_obbligatorio"));
				saveErrors(request, errors);
				return mapping.findForward("fascicolaDocumento");

			}
			Documento documento = new Documento();
			aggiornaModel(pForm, documento, utente);
			if (pForm.getTitolario() == null && pForm.getFascicoliDocumento().size() == 1 && pForm.getFascicoloDocumentoUnico()!=null) {
				documento.getFileVO().setTitolarioId(pForm.getFascicoloDocumentoUnico().getTitolarioId());
				documento.getFileVO().setStatoArchivio(Parametri.STATO_CLASSIFICATO);
				documento.getFileVO().setStatoDocumento(Parametri.CHECKED_IN);
			}
			documento = DocumentaleDelegate.getInstance().aggiornaDocumento(documento, utente, true);

			FileVO fileVO = documento.getFileVO();
			if (fileVO.getReturnValue() == ReturnValues.SAVED) {
				request.setAttribute("documentoId", fileVO.getId());
				caricaDocumento(request, pForm, errors);
				if (Boolean.TRUE.equals(request.getSession().getAttribute("tornaArchivio"))) {
					request.getSession().removeAttribute("tornaArchivio");
					return mapping.findForward("tornaArchivio");

				}
				return mapping.findForward("viewDocumento");
			} else if (fileVO.getReturnValue() == ReturnValues.OLD_VERSION) {
				errors.add("generale", new ActionMessage("versione_vecchia"));
				saveErrors(request, errors);
			} else {
				errors.add("generale", new ActionMessage(
						"errore_nel_salvataggio"));
				saveErrors(request, errors);
			}

			return mapping.findForward("fascicolaDocumento");
		} else if (request.getParameter("downloadDocumentoPrincipale") != null) {
			DocumentoVO doc = pForm.getDocumentoPrincipale();
			return downloadDocumento(mapping, doc, request, response);

		} else if (request.getParameter("annullaAction") != null) {
			pForm.inizializzaForm();
			pForm.setAooId(utente.getRegistroVOInUso().getAooId());
			pForm.setTipoDocumentoId(ProtocolloDelegate.getInstance().getDocumentoDefault(utente.getRegistroVOInUso().getAooId()));
			TitolarioBO.impostaTitolario(pForm, utente.getUfficioInUso(), 0);
			if (Boolean.TRUE.equals(request.getSession().getAttribute(
			"tornaFascicolo"))) {
				request.getSession().removeAttribute("tornaFascicolo");
				return mapping.findForward("tornaFascicolo");
	}
			return mapping.findForward("input");
		} else if (request.getParameter("rimuoviPermessiAction") != null) {
			rimuoviPermessi(pForm);

		} else if (request.getParameter("modificaDocumentoAction") != null
				&& request.getAttribute("documentoId") == null) {
			request.setAttribute("documentoId", new Integer(pForm
					.getDocumentoId()));
			return mapping.findForward("modificaDocumento");

		} else if (request.getParameter("allegaDocumentoPrincipaleAction") != null) {
			uploadDocumento(pForm, request, errors);
			if (!errors.isEmpty())
				saveErrors(request, errors);
			return mapping.findForward("input");

		} else if (request.getParameter("rimuoviDocumentoPrincipaleAction") != null) {
			pForm.rimuoviDocumentoPrincipale();
			return mapping.findForward("input");

		} else if (request.getParameter("impostaUfficioAction") != null) {
			pForm.setUfficioCorrenteId(pForm.getUfficioSelezionatoId());
			AlberoUfficiBO.impostaUfficioUtentiAbilitati(utente, pForm,
					ufficioCompleto);
			return mapping.findForward("input");

		} else if (request.getParameter("ufficioPrecedenteAction") != null) {
			pForm
					.setUfficioCorrenteId(pForm.getUfficioCorrente()
							.getParentId());
			AlberoUfficiBO.impostaUfficioUtentiAbilitati(utente, pForm,
					ufficioCompleto);

		} else if (request.getParameter("assegnaUfficioCorrenteAction") != null) {
			assegnaAdUfficio(pForm, pForm.getUfficioCorrenteId());
			pForm.setMsgPermesso(null);
			return mapping.findForward("input");

		} else if (request.getParameter("assegnaUfficioSelezionatoAction") != null) {
			assegnaAdUfficio(pForm, pForm.getUfficioSelezionatoId());
			return mapping.findForward("input");

		} else if (request.getParameter("assegnaUtenteAction") != null) {
			assegnaAdUtente(pForm);
			pForm.setMsgPermesso(null);
			return mapping.findForward("input");

		} else if (request.getParameter("impostaTitolarioAction") != null) {
			if (pForm.getTitolario() != null) {
				pForm.setTitolarioPrecedenteId(pForm.getTitolario().getId()
						.intValue());
			}
			TitolarioBO.impostaTitolario(pForm, utente.getUfficioInUso(), pForm
					.getTitolarioSelezionatoId());
			return mapping.findForward("classificaDocumento");

		} else if (request.getParameter("impostaFascicoloAction") != null) {
			return mapping.findForward("fascicolaDocumento");
		} else if (request.getParameter("titolarioPrecedenteAction") != null) {
			TitolarioBO.impostaTitolario(pForm, utente.getRegistroInUso(),
					pForm.getTitolarioPrecedenteId());
			if (pForm.getTitolario() != null) {
				pForm.setTitolarioPrecedenteId(pForm.getTitolario()
						.getParentId());
			}
			return mapping.findForward("classificaDocumento");

		} else if (request.getParameter("titolarioPrecedenteAction") != null) {
			Utente ute = (Utente) session.getAttribute(Constants.UTENTE_KEY);
			TitolarioBO.impostaTitolario(pForm, ute.getRegistroInUso(), pForm
					.getTitolarioPrecedenteId());
			if (pForm.getTitolario() != null) {
				pForm.setTitolarioPrecedenteId(pForm.getTitolario()
						.getParentId());
			}
			return mapping.findForward("input");

		} else if (request.getParameter("dettaglioAction") != null) {
			return mapping.findForward("dettaglio_documento");
		} else if (request.getParameter("inviaProtocolloAction") != null) {
			return mapping.findForward("inviaDocumento");

		} else if (request.getParameter("inviaRepertorioAction") != null) {
			return mapping.findForward("inviaRepertorio");
		} else if (request.getParameter("protocolla") != null) {

			int id = pForm.getDocumentoId();

			request.setAttribute("documentoSelezionatoId", id + "");
			return mapping.findForward("protocollazione");

		}
		else if (request.getParameter("btnCercaFascicoli") != null) {
			session.setAttribute("tornaDocumento", Boolean.TRUE);
			return mapping.findForward("cercaFascicolo");

		} else if (request.getParameter("rimuoviFascicoli") != null) {
			String[] fascicoli = pForm.getFascicoloSelezionatoId();
			if (fascicoli != null) {
				for (int i = 0; i < fascicoli.length; i++) {
					pForm.rimuoviFascicolo(Integer.parseInt(fascicoli[i]));
					fascicoli[i] = null;
				}
			}
			return mapping.findForward("fascicolaDocumento");
		} else if (request.getParameter("btnNuovoFascicolo") != null) {
			session.setAttribute("tornaDocumento", Boolean.TRUE);
			return mapping.findForward("nuovoFascicolo");
		} else if (request.getParameter("ripetiDatiAction") != null) {
			return mapping.findForward("ripetiDati");

		} else if (request.getParameter("salvaAction") != null
				&& errors.isEmpty()) {
			// aggiorno il modello
			errors = pForm.validate(mapping, request);
			if (errors.isEmpty()) {
				Utente ute = (Utente) session
						.getAttribute(Constants.UTENTE_KEY);
				Documento documento = null;

				if (pForm.getDocumentoId() == 0) {

					if (Boolean.TRUE.equals(request.getSession().getAttribute(
							"tornaFascicolo"))) {
						Fascicolo fascicolo = (Fascicolo) session.getAttribute(Constants.FASCICOLO);
						pForm.aggiungiFascicolo(fascicolo.getFascicoloVO());
						documento = DocumentaleBO.getDefaultDocumento(ute);
						aggiornaModelFromFascicolo(pForm, documento, utente,
								fascicolo.getFascicoloVO());
						documento = delegate.salvaDocumento(documento, ute);
						FileVO fileVO = documento.getFileVO();
						if (fileVO.getReturnValue() == ReturnValues.SAVED) {
							request.getSession().removeAttribute(
									"tornaFascicolo");
							
							DocumentoFascicoloVO vo=new DocumentoFascicoloVO();
							vo.setDocumentoId(documento.getFileVO().getId());
							vo.setFascicoloId(fascicolo.getFascicoloVO().getId());
							vo.setUfficioProprietarioId(utente.getUfficioInUso());
							fascicolo.getDocumenti().add(vo);
							
							session.setAttribute(Constants.FASCICOLO,fascicolo);
							
							pForm.inizializzaForm();
							pForm.setAooId(utente.getRegistroVOInUso().getAooId());
							pForm.setTipoDocumentoId(ProtocolloDelegate.getInstance().getDocumentoDefault(utente.getRegistroVOInUso().getAooId()));
							TitolarioBO.impostaTitolario(pForm, utente.getUfficioInUso(), 0);
							
							//
							return mapping.findForward("tornaFascicolo");
						} else {
							errors.add("generale", new ActionMessage(
									"errore_nel_salvataggio"));
						}
						saveErrors(request, errors);
					}
					// 
					if (pForm.isFromProcedimento()) {
						ProcedimentoVO proc = ProcedimentoDelegate
								.getInstance().getProcedimentoVO(
										pForm.getProcedimentoId());
						Fascicolo fascicolo = FascicoloDelegate.getInstance()
								.getFascicoloById(proc.getFascicoloId());
						pForm.aggiungiFascicolo(fascicolo.getFascicoloVO());
						documento = DocumentaleBO.getDefaultDocumento(ute);
						aggiornaModelFromFascicolo(pForm, documento, utente,
								fascicolo.getFascicoloVO());
						documento.getFileVO().setProcedimentoId(
								pForm.getProcedimentoId());
						documento = delegate.salvaDocumento(documento, ute);
						FileVO fileVO = documento.getFileVO();
						if (fileVO.getReturnValue() == ReturnValues.SAVED) {
							caricaDocumento(request, pForm, errors);
							pForm.setVersioneDefault(true);
							pForm.setStatoArchivio(Parametri.STATO_CLASSIFICATO);
							pForm.setPermessoCorrente(PermessiConst.OWNER);
							// pForm.setProcedimentoId(procedimentoId);
							request.setAttribute("documentoId", fileVO.getId());
							// inizializza form
							pForm.inizializzaForm();
							pForm.setAooId(utente.getRegistroVOInUso().getAooId());
							pForm.setTipoDocumentoId(ProtocolloDelegate.getInstance().getDocumentoDefault(utente.getRegistroVOInUso().getAooId()));
							TitolarioBO.impostaTitolario(pForm, utente.getUfficioInUso(), 0);
							
							//
							return mapping.findForward("viewDocumentoProcedimento");
						} else {
							errors.add("generale", new ActionMessage(
									"errore_nel_salvataggio"));
						}
						saveErrors(request, errors);
					}
					//
					else {
						documento = DocumentaleBO.getDefaultDocumento(ute);
						aggiornaModel(pForm, documento, utente);
						documento = delegate.salvaDocumento(documento, ute);
					}
				} else {
					documento = new Documento();
					aggiornaModel(pForm, documento, utente);
					documento = delegate.aggiornaDocumento(documento, utente,
							false);
				}
				FileVO fileVO = documento.getFileVO();
				if (fileVO.getReturnValue() == ReturnValues.SAVED) {
					request.setAttribute("documentoId", fileVO.getId());
					caricaDocumento(request, pForm, errors);
					return mapping.findForward("visualizzaDocumento");
				} else if (fileVO.getReturnValue() == ReturnValues.OLD_VERSION) {
					errors.add("generale",
							new ActionMessage("versione_vecchia"));
				} else {
					errors.add("generale", new ActionMessage(
							"errore_nel_salvataggio"));
				}
			}
			saveErrors(request, errors);
		}
		// demanda alle sottoclassi
		return mapping.findForward("input");
	}
	
	 public  void aggiornaModelFromFascicolo(DocumentoForm form,
				Documento documento, Utente utente, FascicoloVO fasc) {
			FileVO fileVO = documento.getFileVO();
			documento.getFileVO().setDocumentoVO(form.getDocumentoPrincipale());
			aggiornaDatiGeneraliModelFromFascicolo(form, fileVO, utente);
			fileVO.setTitolarioId(fasc.getTitolarioId());
			fileVO.setStatoDocumento(Parametri.CHECKED_IN);
			fileVO.setStatoArchivio(Parametri.STATO_CLASSIFICATO);
			fileVO.setFascicoli(form.getFascicoliDocumento());

		}

		private  void aggiornaDatiGeneraliModelFromFascicolo(DocumentoForm form,
				FileVO documento, Utente utente) {
			documento.setId(form.getDocumentoId());
			documento.setTipoDocumentoId(form.getTipoDocumentoId());
			Date dataDoc = DateUtil.toDate(form.getDataDocumento());
			if (dataDoc != null) {
				documento.setDataDocumento(new java.sql.Date(dataDoc.getTime()));
			} else {
				documento.setDataDocumento(null);
			}
			documento.setRepositoryFileId(form.getRepositoryFileId());
			CartellaVO root = null;
			try {
				root = DocumentaleDelegate.getInstance().getCartellaVOByCaricaId(
						utente.getCaricaInUso());
			} catch (DataException e) {
				e.printStackTrace();
			}
			documento.setCartellaId(root.getId());
			documento.setOggetto(form.getOggetto());
			documento.setDescrizione(form.getDescrizione());
			documento.setDescrizioneArgomento(form.getDescrizioneArgomento());
			documento.setNote(form.getNote());
			if (form.getDocumentoPrincipale().getId() == null
					|| form.getDocumentoPrincipale().getId().intValue() == 0)
				documento.setNomeFile(form.getDocumentoPrincipale().getFileName());
			else
				documento.setNomeFile(form.getNomeFile());
			documento.setRowCreatedTime(new java.sql.Date(System
					.currentTimeMillis()));
			documento.setVersione(0);
			documento.setCaricaLavId(form.getUserLavId());
		}

	private void aggiornaPermessiForm(Documento documento, DocumentoForm form) {
		form.setPermessoCorrente(documento.getPermessoCorrente());
		Organizzazione org = Organizzazione.getInstance();
		for (Iterator<PermessoFileVO> i = documento.getPermessi().iterator(); i.hasNext();) {
			PermessoFileVO permesso =  i.next();
			PermessoDocumentoView pdoc = new PermessoDocumentoView();
			int uffId = permesso.getUfficioId();
			pdoc.setUfficioId(uffId);
			Ufficio uff = org.getUfficio(uffId);
			if (uff != null) {
				pdoc.setNomeUfficio(uff.getValueObject().getDescription());
			}
			int caricaId = permesso.getCaricaId();
			CaricaVO carica = org.getCarica(caricaId);
			if (carica != null) {
				Utente ute = org.getUtente(carica.getUtenteId());
				pdoc.setCaricaId(caricaId);
				if (ute != null) {
					pdoc.setNomeUtente(ute.getValueObject().getFullName());
				}
			}
			pdoc.setTipoPermesso(permesso.getTipoPermesso());
			pdoc.setDescrizioneTipoPermesso(((IdentityVO) LookupDelegate
					.getInstance().getTipiPermessiDocumentiBusiness().get(
							String.valueOf(permesso.getTipoPermesso())))
					.getDescription());
			pdoc.setMsgPermesso(permesso.getMsgPermesso());
			form.aggiungiPermesso(pdoc);
		}
	}

	private void aggiornaPermessiModel(DocumentoForm form, Documento documento,
			Utente utente) {
		documento.removePermessi();
		Collection<PermessoDocumentoView> permessi = form.getPermessi();
		if (permessi != null) {
			for (Iterator<PermessoDocumentoView> i = permessi.iterator(); i.hasNext();) {
				PermessoDocumentoView ass =  i.next();
				PermessoFileVO permesso = new PermessoFileVO();
				if (ass.getUtenteId() != 0) {
					CaricaVO carica = CaricaDelegate.getInstance()
							.getCaricaByUtenteAndUfficio(ass.getUtenteId(),
									ass.getUfficioId());
					ass.setCaricaId(carica.getCaricaId());
				}
				permesso.setTipoPermesso(ass.getTipoPermesso());
				permesso.setUfficioId(ass.getUfficioId());
				permesso.setCaricaId(ass.getCaricaId());
				permesso.setMsgPermesso(ass.getMsgPermesso());
				documento.aggiungiPermesso(permesso);
			}
		}
	}

	private void rimuoviPermessi(DocumentoForm form) {
		String[] permessi = form.getPermessiSelezionatiId();
		if (permessi != null) {
			for (int i = 0; i < permessi.length; i++) {
				String permesso = permessi[i];
				if (permesso != null) {
					form.rimuoviPermesso(permesso);
				}
			}
		}
	}

	protected void caricaDocumento(HttpServletRequest request,
			DocumentoForm form, ActionMessages errors) {
		Integer documentoId = (Integer) request.getAttribute("documentoId");
		String dfaId = request.getParameter("dfaId");
		String versione = request.getParameter("versione");

		if (documentoId != null || (dfaId != null && versione != null)) {
			HttpSession session = request.getSession();
			if (session.getAttribute("tornaProcedimento") != null) {
				form.setFromProcedimento(true);
				form.setProcedimentoId((Integer) session
						.getAttribute("tornaProcedimento"));
				session.removeAttribute("tornaProcedimento");
			}

			Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
			Documento documento = null;

			if (documentoId != null) {
				request.removeAttribute("documentoId");
				try {
					int id = documentoId.intValue();
					documento = DocumentaleDelegate.getInstance()
							.getDocumentoById(id);
				} catch (DataException e) {
					errors.add("generale", new ActionMessage(
							"database.cannot.load"));
				}
			} else {
				try {
					int versioneId = NumberUtil.getInt(versione);
					int docId = NumberUtil.getInt(dfaId);
					documento = DocumentaleDelegate.getInstance()
							.getDocumentoStoriaById(docId, versioneId);
					form.setVersioneDefault(false);
				} catch (DataException e) {
					errors.add("generale", new ActionMessage(
							"database.cannot.load"));
				}
			}
			try {
				if (documento.getFileVO().getOwnerCaricaId() == utente
						.getCaricaInUso()) {
					// assegna permesso owner
					documento.setPermessoCorrente(PermessiConst.OWNER);
				} else {
					// prelievo il permesso dalla base dati
					documento.setPermessoCorrente(DocumentaleDelegate
							.getInstance().getTipoPermessoSuDocumento(
									documento.getFileVO().getId().intValue(),
									utente.getCaricaInUso(),
									Organizzazione.getInstance().getUfficio(
											utente.getUfficioInUso())
											.getListaUfficiDiscendentiIdStr()));
				}
			} catch (DataException e) {
				errors.add("generale",
						new ActionMessage("database.cannot.load"));
			}
			if (errors.isEmpty()) {
				aggiornaForm(documento, form, session);
			}
		}
	}

	protected void assegnaAdUfficio(DocumentoForm form, int ufficioId) {

		PermessoDocumentoView ass = new PermessoDocumentoView();
		ass.setUfficioId(ufficioId);
		Organizzazione org = Organizzazione.getInstance();
		Ufficio uff = org.getUfficio(ufficioId);
		ass.setNomeUfficio(uff.getValueObject().getDescription());
		ass.setDescrizioneUfficio(uff.getPath());
		ass.setTipoPermesso(form.getTipoPermessoSelezionato());
		ass.setDescrizioneTipoPermesso(((IdentityVO) LookupDelegate
				.getInstance().getTipiPermessiDocumentiBusiness().get(
						String.valueOf(form.getTipoPermessoSelezionato())))
				.getDescription());

		ass.setMsgPermesso(form.getMsgPermesso());
		form.aggiungiPermesso(ass);
		form.setUfficioSelezionatoId(0);
	}

	protected void assegnaAdUtente(DocumentoForm form) {
		PermessoDocumentoView ass = new PermessoDocumentoView();
		ass.setUfficioId(form.getUfficioCorrenteId());
		ass.setNomeUfficio(form.getUfficioCorrente().getDescription());
		ass.setDescrizioneUfficio(form.getUfficioCorrentePath());
		ass.setUtenteId(form.getUtenteSelezionatoId());
		ass.setTipoPermesso(form.getTipoPermessoSelezionato());
		ass.setDescrizioneTipoPermesso(((IdentityVO) LookupDelegate
				.getInstance().getTipiPermessiDocumentiBusiness().get(
						String.valueOf(form.getTipoPermessoSelezionato())))
				.getDescription());
		UtenteVO ute = form.getUtente(form.getUtenteSelezionatoId());
		ass.setNomeUtente(ute.getFullName());
		ass.setMsgPermesso(form.getMsgPermesso());
		form.aggiungiPermesso(ass);
	}

}