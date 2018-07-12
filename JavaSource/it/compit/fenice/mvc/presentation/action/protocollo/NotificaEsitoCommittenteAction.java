package it.compit.fenice.mvc.presentation.action.protocollo;

import it.compit.fenice.enums.TipoFatturaElettronicaEnum;
import it.compit.fenice.mvc.presentation.actionform.protocollo.NotificaEsitoCommittenteForm;
import it.compit.fenice.util.VelocityTemplateUtils;
import it.compit.fenice.util.XMLUtil;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.dao.digitalsignature.VerificaFirma;
import it.finsiel.siged.exception.CRLNonAggiornataException;
import it.finsiel.siged.exception.CertificatoNonValidoException;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.exception.FirmaNonValidaException;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.ProtocolloIngresso;
import it.finsiel.siged.model.protocollo.ProtocolloUscita;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.business.SoggettoDelegate;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.util.FileUtil;
import it.finsiel.siged.util.NumberUtil;
import it.finsiel.siged.util.ServletUtil;
import it.flosslab.mvc.presentation.action.protocollo.helper.file.ProtocolloFileUtility;

import java.io.File;
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

public class NotificaEsitoCommittenteAction extends Action {

	static Logger logger = Logger
			.getLogger(NotificaEsitoCommittenteAction.class.getName());

	//TODO -> visualizza fattura elettronica
	public static ActionMessages visualizzaFatturaElettronica(DocumentoVO doc,
			HttpServletRequest request, HttpServletResponse response, int aooId) {
		InputStream is = null;
		OutputStream os = null;
		String xsl = null;
		String tipoDoc = null;
		ActionMessages errors = new ActionMessages();
		try {
			if (doc != null) {
				os = response.getOutputStream();
				response.setContentType("text/html");
				response.setHeader("Cache-control", "");
				if (!doc.getFileName().toLowerCase().endsWith(".p7m")) {
					String path = Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo()+ "/" + "aoo_" + aooId + "/" + doc.getPath();
					tipoDoc=FileUtil.getFatturaElettronicaType(doc.getFileName(), path);
					xsl=Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo()+TipoFatturaElettronicaEnum.valueOf(tipoDoc).getPath();
					is = XMLUtil.creaHTMLdaXML(path, xsl, errors);
				} else {
					String path = Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo()+ "/" + "aoo_" + aooId + "/" + doc.getPath();
					String folder = ServletUtil.getTempUserPath(request.getSession());
					File tempFile = File.createTempFile("fpa_", ".tmp", new File(folder));
					path=tempFile.getAbsolutePath();
					tipoDoc=FileUtil.getFatturaElettronicaType(doc.getFileName(), path);
					xsl=Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo()+TipoFatturaElettronicaEnum.valueOf(tipoDoc).getPath();
					VerificaFirma.saveContentFromP7M(path, path);
					is = XMLUtil.creaHTMLdaXML(path, xsl, errors);
				}
				FileUtil.writeFile(is, os);
			}
		} catch (FileNotFoundException e) {
			logger.error("", e);
			errors.add("download", new ActionMessage("error.notfound"));
		} catch (IOException e) {
			logger.error("", e);
			errors.add("download", new ActionMessage("error.cannot.read"));
		} catch (Exception e) {
			logger.error("", e);
			errors.add("download", new ActionMessage("EXCEPTION"));
		} finally {
			FileUtil.closeIS(is);
			FileUtil.closeOS(os);
		}
		return errors;
	}

	public ActionForward visualizzaFatturaElettronica(ActionMapping mapping,
			DocumentoVO doc, HttpServletRequest request,
			HttpServletResponse response, int aooId) throws Exception {
		ActionMessages errors = ProtocolloFileUtility
				.visualizzaFatturaElettronica(doc, request, response, aooId);
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return mapping.findForward("erroreDownload");
		}
		ActionForward actionForward = new ActionForward();
		actionForward.setName("none");
		return actionForward;
	}

	//TODO -> visualizzaNotificaTmp
	public ActionForward visualizzaNotificaTmp(ActionMapping mapping,
			DocumentoVO doc, HttpServletRequest request,
			HttpServletResponse response, int aooId) throws Exception {
		InputStream is = null;
		OutputStream os = null;
		String xsl = null;
		ActionMessages errors = new ActionMessages();
		try {
			if (doc != null) {
				os = response.getOutputStream();
				response.setContentType("text/html");
				response.setHeader("Cache-control", "");
				xsl = Organizzazione.getInstance().getValueObject()
						.getPathDocumentiProtocollo()
						+ TipoFatturaElettronicaEnum.valueOf("EC").getPath();
				is = XMLUtil.creaHTMLdaXML(doc.getPath(), xsl, errors);
				FileUtil.writeFile(is, os);
			}
		} catch (FileNotFoundException e) {
			logger.error("", e);
			errors.add("download", new ActionMessage("error.notfound"));
		} catch (IOException e) {
			logger.error("", e);
			errors.add("download", new ActionMessage("error.cannot.read"));
		} catch (Exception e) {
			logger.error("", e);
			errors.add("download", new ActionMessage("EXCEPTION"));
		} finally {
			FileUtil.closeIS(is);
			FileUtil.closeOS(os);
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return mapping.findForward("erroreDownload");
		}
		ActionForward actionForward = new ActionForward();
		actionForward.setName("none");
		return actionForward;
	}

	public static void parseFatturaElettronica(
			NotificaEsitoCommittenteForm form, HttpServletRequest request,
			HttpServletResponse response, int aooId) {
		DocumentoVO doc = form.getDocumentoPrincipale();
		try {
			if (!doc.getFileName().toLowerCase().endsWith(".p7m")) {
				String path = Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo()+ "/" + "aoo_" + aooId + "/" + doc.getPath();
				XMLUtil.XMLParseFatturaElettronica(path, form);
			} else {
				String path = Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo() + "/" + "aoo_" + aooId + "/" + doc.getPath();
				String folder = ServletUtil.getTempUserPath(request.getSession());
				File tempFile = File.createTempFile("fpa_", ".tmp", new File( folder));
				VerificaFirma.saveContentFromP7M(path, tempFile.getAbsolutePath());
				XMLUtil.XMLParseFatturaElettronica(tempFile.getAbsolutePath(),form);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static DocumentoVO uploadNotifica(NotificaEsitoCommittenteForm form,
			HttpServletRequest request, ActionMessages errors) {
		DocumentoVO documento = new DocumentoVO();
		String fileName = "";
		String folder = "";
		String progressivo = NumberUtil.threeChars(form.getProgressivo());
		fileName = FileUtil.deleteEstensioneFileFatturaElettronica(form
				.getDocumentoPrincipale().getFileName())
				+ "_EC_"
				+ progressivo
				+ ".xml";
		form.setProgressivo(progressivo);
		String contentType = "text/xml";
		logger.info(contentType);
		try {
			String text = "";
			text = VelocityTemplateUtils.createNotificaEsitoCommittente(form);
			folder = ServletUtil.getTempUserPath(request.getSession());
			File tempFile = File.createTempFile("temp_", ".notifica", new File(
					folder));
			String tempFilePath = tempFile.getAbsolutePath();
			FileUtil.stringToDom(text, tempFilePath);
			String impronta = FileUtil.calcolaDigest(tempFilePath, errors);
			File file = new File(tempFilePath);
			int size = Long.valueOf(file.length()).intValue();
			if (!"".equals(fileName) && fileName.length() > 100) {
				errors.add("documento", new ActionMessage(
						"error.nomefile.lungo", "", ""));
			} else if (size > 0 && !"".equals(fileName)) {
				if (errors.isEmpty()) {
					VerificaFirma
							.verificaFileFirmato(tempFilePath, contentType);
					String username = ((Utente) request.getSession()
							.getAttribute(Constants.UTENTE_KEY))
							.getValueObject().getUsername();
					documento.setMustCreateNew(true);
					documento.setDescrizione(null);
					documento.setFileName(fileName);
					documento.setPath(tempFilePath);
					documento.setImpronta(impronta);
					documento.setSize(size);
					documento.setContentType(contentType);
					documento.setRowCreatedTime(new Date(System
							.currentTimeMillis()));
					documento.setRowUpdatedTime(new Date(System
							.currentTimeMillis()));
					documento.setRowCreatedUser(username);
					documento.setRowUpdatedUser(username);
				}
			} else {
				errors.add("documento", new ActionMessage("campo.obbligatorio",
						"File", ""));
			}
		} catch (IOException e) {
			errors.add("allegati", new ActionMessage("database.cannot.load"));
		} catch (DataException e) {
			errors.add("allegati", new ActionMessage("database.cannot.load"));
		} catch (CertificatoNonValidoException e) {
			errors.add("allegati", new ActionMessage(
					"errore.verificafirma.doc.non_valido", e.getMessage()));
		} catch (FirmaNonValidaException e) {
			errors.add("allegati", new ActionMessage(
					"errore.verificafirma.doc.non_valido", e.getMessage()));
		} catch (CRLNonAggiornataException e) {
			errors.add("allegati", new ActionMessage(
					"errore.verificafirma.crl_non_aggiornata"));
		}
		return documento;
	}

	protected void caricaNotificaForm(HttpServletRequest request,
			NotificaEsitoCommittenteForm form, Utente utente) {
		Integer protocolloId = (Integer) request.getAttribute("protocolloId");
		ProtocolloIngresso protocollo = new ProtocolloIngresso();
		if (protocolloId != null) {
			int id = protocolloId.intValue();
			protocollo = ProtocolloDelegate.getInstance()
					.getProtocolloIngressoById(id);
			ProtocolloVO protocolloVO = protocollo.getProtocollo();
			form.setDocumentoPrincipale(protocollo.getDocumentoPrincipale());
			form.setAutore(protocolloVO.getRowCreatedUser());
			form.setProtocolloId(id);			
			//form.setIdentificativoSDI(StringUtil.parseOggettoForIdentificativoSDI(protocolloVO.getOggetto()));
			form.setNumero(protocolloVO.getNumProtocollo());
			form.setIdCommittente(String.valueOf(protocolloVO.getKey()));
			form.setDocumentoId(protocolloVO.getDocumentoPrincipaleId());
			AllaccioVO allaccioVO = new AllaccioVO();
			allaccioVO.setProtocolloAllacciatoId(id);
			allaccioVO.setAllaccioDescrizione(protocolloVO.getNumProtocollo()
					+ "/" + protocolloVO.getAnnoRegistrazione() + " ("
					+ protocolloVO.getFlagTipo() + ")");
			form.setAllaccio(allaccioVO);
			form.setDestinatari(protocolloVO.getMittenti());
		}
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession(true);
		NotificaEsitoCommittenteForm ecForm = (NotificaEsitoCommittenteForm) form;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		int aooId = utente.getAreaOrganizzativa().getId();
		ActionMessages errors = new ActionMessages();
		caricaNotificaForm(request, ecForm, utente);
		if (request.getParameter("visualizzaFatturaBrowser") != null) {
			DocumentoVO doc = ecForm.getDocumentoPrincipale();
			return visualizzaFatturaElettronica(mapping, doc, request,
					response, aooId);
		} else if (request.getParameter("visualizzaNotificaBrowser") != null) {
			DocumentoVO doc = ecForm.getNotifica();
			return visualizzaNotificaTmp(mapping, doc, request, response, aooId);
		} else if (request.getParameter("btnCreaNotifica") != null) {
			ecForm.setProgressivo(ProtocolloDelegate.getInstance()
					.getProgressivoNotifica(aooId));
			parseFatturaElettronica(ecForm, request, response, utente
					.getAreaOrganizzativa().getId());
			ecForm.setNotifica(uploadNotifica(ecForm, request, errors));
		} else if (request.getParameter("btnProtocolla") != null) {
			ProtocolloDelegate.getInstance().salvaProgressivoNotifica(aooId,
					ecForm.getProgressivo());
			preparaProtocolloUscita(request, ecForm, utente, errors, 0);
			return (mapping.findForward("protocollazioneUscita"));

		} else if (request.getParameter("btnIndietro") != null) {
			request.setAttribute("protocolloId",ecForm.getProtocolloId());
			return (mapping.findForward("indietro"));
		}
		return mapping.findForward("input");
	}

	public static void preparaProtocolloUscita(HttpServletRequest request,
			NotificaEsitoCommittenteForm form, Utente utente,
			ActionMessages errors, int flagTipo) throws DataException {
		ProtocolloUscita pu = ProtocolloBO.getDefaultProtocolloUscita(utente);
		try {
			ProtocolloVO protocollo = pu.getProtocollo();
			protocollo.setDataDocumento(new Date(System.currentTimeMillis()));
			pu.setDocumentoPrincipale(form.getNotifica());
			AssegnatarioVO mittente = new AssegnatarioVO();
			mittente.setUfficioAssegnatarioId(utente.getUfficioInUso());
			mittente.setUtenteAssegnatarioId(utente.getValueObject().getId());
			pu.setMittente(mittente);
			pu.setProtocollo(protocollo);
			pu.getProtocollo().setStatoProtocollo("P");
			pu.allacciaProtocollo(form.getAllaccio());
			pu.getProtocollo().setFatturaElettronica(true);
			aggiornaDestinatariProtocolloModel(form, pu, utente);
			request.setAttribute(Constants.PROTOCOLLO_DA_NOTIFICA, pu);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException("Errore nella generazione del Protocollo");
		}
	}

	private static void aggiornaDestinatariProtocolloModel(
			NotificaEsitoCommittenteForm form, ProtocolloUscita protocollo,
			Utente utente) {
		protocollo.removeDestinatari();
		Collection<SoggettoVO> destinatari = form.getDestinatari();
		boolean spedito = false;
		if (destinatari != null) {
			for (Iterator<SoggettoVO> i = destinatari.iterator(); i.hasNext();) {
				SoggettoVO dest = i.next();
				DestinatarioVO destinatario = new DestinatarioVO();
				destinatario.setIdx(Integer.valueOf(dest.getId()));
				if ("F".equals(dest.getTipo())) {
					destinatario.setNome(dest.getNome());
					destinatario.setCognome(dest.getCognome());
					destinatario.setDestinatario(dest.getNome() + " "
							+ dest.getCognome());
				} else {
					destinatario.setDestinatario(dest.getDescrizione());
				}
				destinatario.setFlagTipoDestinatario(dest.getTipo());
				destinatario.setEmail(SoggettoDelegate.getInstance()
						.getMailFormPersonaId(dest.getId()));
				destinatario.setCodicePostale(dest.getIndirizzo().getCap());

				if (dest.getIndirizzo() != null
						&& !(dest.getIndirizzo().equals(""))) {
					destinatario.setIndirizzo(dest.getIndirizzoNumCivico());

				}
				if (dest.getIndirizzo().getComune() != null) {
					destinatario.setCitta(dest.getIndirizzo().getComune());

				} else {
					destinatario.setCitta("");
				}

				destinatario.setFlagConoscenza(false);
				destinatario.setFlagPresso(false);
				if (destinatario.getEmail() != null)
					destinatario.setFlagPEC(true);
				destinatario.setNote(dest.getNote());
				protocollo.addDestinatari(destinatario);
			}
		}
		if (spedito) {
			protocollo.getProtocollo().setStatoProtocollo("S");
		} else {
			protocollo.getProtocollo().setStatoProtocollo("N");
		}
	}

}