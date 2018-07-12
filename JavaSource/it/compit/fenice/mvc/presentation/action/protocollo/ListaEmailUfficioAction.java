package it.compit.fenice.mvc.presentation.action.protocollo;

import it.compit.fenice.mvc.presentation.action.protocollo.helper.file.EmailFileUtility;
import it.compit.fenice.util.XMLUtil;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.dao.digitalsignature.VerificaFirma;
import it.finsiel.siged.exception.CRLNonAggiornataException;
import it.finsiel.siged.exception.CertificatoNonValidoException;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.exception.EmailException;
import it.finsiel.siged.exception.FirmaNonValidaException;
import it.finsiel.siged.model.MessaggioEmailEntrata;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.ProtocolloIngresso;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.business.EmailDelegate;
import it.finsiel.siged.mvc.business.SoggettoDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ListaEmailForm;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.FileUtil;
import it.finsiel.siged.util.NumberUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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
import org.jsoup.Jsoup;

public class ListaEmailUfficioAction extends Action {

	static Logger logger = Logger.getLogger(ListaEmailUfficioAction.class.getName());

	public ActionForward downloadDocumento(ActionMapping mapping,
			DocumentoVO doc, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionMessages errors = EmailFileUtility.downloadFileMailUfficio(doc,
				response);
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return mapping.findForward("erroreDownload");
		}
		return null;
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages messages = new ActionMessages();
		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		ListaEmailForm listaEmailForm = (ListaEmailForm) form;
		SoggettoDelegate soggettoDelegate = SoggettoDelegate.getInstance();
		if (listaEmailForm == null) {
			logger.info(" Creating new ListaEmailAction");
			listaEmailForm = new ListaEmailForm();
			request.setAttribute(mapping.getAttribute(), listaEmailForm);
		}

		if (request.getParameter("downloadAllegatoId") != null) {
			int docId = NumberUtil.getInt((String) request
					.getParameter("downloadAllegatoId"));
			DocumentoVO doc = EmailDelegate.getInstance().getAllegatoUfficio(docId);
			return downloadDocumento(mapping, doc, request, response);

		}

		if (request.getParameter("cancella") != null) {
			errors = listaEmailForm.validate(mapping, request);
			if (errors.isEmpty()) {
				int emailSelezionataId = listaEmailForm.getEmailSelezionataId();
				int emailId = listaEmailForm.getEmailId();
				int id = 0;
				if (emailId != 0)
					id = emailId;
				else
					id = emailSelezionataId;
				if (EmailDelegate.getInstance().eliminaEmailUfficio(id)) {
					messages.add("email", new ActionMessage("cancellazione_ok","", ""));
				}
			}

		} else if (request.getParameter("visualizza") != null) {

			if (errors.isEmpty()) {
				try {
					if (request.getParameter("emailSelezionataId") != null) {
						int emailId = new Integer(request
								.getParameter("emailSelezionataId")).intValue();
						MessaggioEmailEntrata messaggio = EmailDelegate
								.getInstance().getMailUfficio(emailId,
										utente);
						listaEmailForm.setMsg(messaggio);
						aggiornaForm(listaEmailForm, listaEmailForm.getMsg(),
								errors);
						request.setAttribute("listaEmailForm", listaEmailForm);
						return mapping.findForward("edit");
					}
				} catch (Exception e) {
					errors.add("general", new ActionMessage(
							"error.database.cannotload"));
				}
			}
		} else if (request.getParameter("cercaMittente") != null) {
			session.setAttribute("tornaEmailUfficio", true);
			if (soggettoDelegate.isMailUsed(utente.getAreaOrganizzativa()
					.getId(), listaEmailForm.getEmailMittente())) {
				SoggettoVO sogg = soggettoDelegate.getSoggettoByMail(utente
						.getAreaOrganizzativa().getId(), listaEmailForm
						.getEmailMittente());
				session.setAttribute("perId", String.valueOf(sogg.getId()));
				request.setAttribute("listaEmailForm", listaEmailForm);
				if (sogg.getTipo().equals("F"))
					return (mapping.findForward("cercaPersonaFisica"));
				else
					return (mapping.findForward("cercaPersonaGiuridica"));
			} else {
				listaEmailForm.setNuovoMittente(true);
				return mapping.findForward("edit");
			}
		} else if (request.getParameter("nuovoSoggettoFisico") != null) {
			listaEmailForm.setNuovoMittente(false);
			request.setAttribute("emailMittente", listaEmailForm
					.getEmailMittente());
			request.setAttribute("descrizioneMittente", listaEmailForm
					.getNomeMittente());
			return (mapping.findForward("nuovaPersonaFisica"));
		} else if (request.getParameter("nuovoSoggettoGiuridico") != null) {
			listaEmailForm.setNuovoMittente(false);
			request.setAttribute("emailMittente", listaEmailForm
					.getEmailMittente());
			request.setAttribute("descrizioneMittente", listaEmailForm
					.getNomeMittente());
			return (mapping.findForward("nuovaPersonaGiuridica"));
		} else if (request.getParameter("protocolla") != null) {
			try {
				Integer mittenteId = (Integer) session.getAttribute("mittenteId");
				if (mittenteId != null)
					listaEmailForm.setMittente(soggettoDelegate.getSoggettoById(mittenteId));
				preparaProtocollo(request, listaEmailForm, session, utente);
				return (mapping.findForward("protocollazione"));
			} catch (Exception e) {
				e.printStackTrace();
				errors.add("general", new ActionMessage(
						"errore.prepare.protocollo_da_email"));
			}
			listaEmailForm.setAllegatiEmail(listaEmailForm.getMsg()
					.getAllegati());
			saveErrors(request, errors);
			return mapping.findForward("edit");
		}
		listaEmailForm.setListaEmail(EmailDelegate.getInstance().getMessaggiUfficioDaProtocollare(utente.getUfficioInUso()));
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		saveMessages(request, messages);
		request.setAttribute("listaEmailForm", listaEmailForm);
		return (mapping.findForward("input"));
	}

	public void aggiornaForm(ListaEmailForm form,
			MessaggioEmailEntrata messaggio, ActionMessages errors) {
		Date dataRic = messaggio.getEmail().getDataRicezione();
		form.setEmailId(messaggio.getEmail().getId().intValue());
		form.setDataRicezione(dataRic == null ? null : DateUtil
				.formattaData(dataRic.getTime()));
		Date dataSped = messaggio.getEmail().getDataSpedizione();
		form.setDataSpedizione(dataSped == null ? null : DateUtil
				.formattaData(dataSped.getTime()));
		
		form.setOggetto(messaggio.getEmail().getOggetto());
		form.setEmailMittente(messaggio.getEmail().getEmailMittente());
		form.setNomeMittente(messaggio.getEmail().getNomeMittente());
		if(messaggio.getEmail().getTestoMessaggio()!=null){
			String text=html2text(messaggio.getEmail().getTestoMessaggio());
			form.setTestoMessaggio(text);
		}
		Iterator<DocumentoVO> it = messaggio.getAllegati().iterator();
		while (it.hasNext()) {
			DocumentoVO doc = it.next();
			try {
				VerificaFirma.verificaFileFirmato(doc.getPath(), doc
						.getContentType());
			} catch (DataException e) {
				errors.add("allegati",
						new ActionMessage("database.cannot.load"));
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
		}
		form.setAllegatiEmail(messaggio.getAllegati());
	}

	public void preparaProtocollo(HttpServletRequest request,
			ListaEmailForm form, HttpSession session, Utente utente)
			throws EmailException {
		MessaggioEmailEntrata msg = form.getMsg();
		ProtocolloIngresso pi = ProtocolloBO
				.getDefaultProtocolloIngresso(utente);
		try {
			File file = File.createTempFile("tmp_prot_ing_", ".att", new File(
					utente.getValueObject().getTempFolder()));
			InputStream bais = new ByteArrayInputStream(msg.getEmail()
					.getTestoMessaggio().getBytes());
			OutputStream os = new FileOutputStream(file);
			FileUtil.writeFile(bais, os);
			bais.close();
			os.close();
			DocumentoVO docBody = new DocumentoVO();
			docBody.setContentType("text/plain");
			docBody.setDescrizione("Body");
			docBody.setPath(file.getAbsolutePath());
			docBody.setSize((int) file.length());
			docBody.setFileName("body messaggio.txt");
			docBody.setMustCreateNew(true);
			int docId = form.getDocPrincipaleId();
			boolean bodyAsDocPrincipale = "BODY".equals(form
					.getTipoDocumentoPrincipale());
			if (bodyAsDocPrincipale) {
				pi.setDocumentoPrincipale(docBody);
			} else {
				pi.allegaDocumento(docBody);
			}
			/**/
			preparaSegnatura(pi, form.getMsg().getEmail().getSegnatura());
			/**/
			Iterator<DocumentoVO> iterator = msg.getAllegati().iterator();
			while (iterator.hasNext()) {
				DocumentoVO d = iterator.next();
				d.setMustCreateNew(true);
				if (d.getId().intValue() == docId && !bodyAsDocPrincipale) {
					pi.setDocumentoPrincipale(d);
				} else {
					pi.allegaDocumento(d);
				}
			}
			ProtocolloVO protocollo = pi.getProtocollo();
			if (form.getDataSpedizione() != null
					&& !form.getDataSpedizione().trim().equals(""))
				protocollo.setDataDocumento(DateUtil.toDate(form
						.getDataSpedizione()));
			if (form.getDataRicezione() != null
					&& !form.getDataRicezione().trim().equals(""))
				protocollo.setDataRicezione(DateUtil.toDate(form
						.getDataRicezione()));
			protocollo.setOggetto(form.getOggetto());
			protocollo.setMittenti(new ArrayList<SoggettoVO>());
			protocollo.setFlagTipoMittente("M");
			protocollo.getMittenti().add(form.getMittente());
			protocollo.setEmailUfficioId(msg.getEmail().getId());
			pi.setProtocollo(protocollo);
			request.setAttribute(Constants.PROTOCOLLO_INGRESSO_DA_EMAIL, pi);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EmailException("Errore nella generazione del Protocollo");
		}
	}

	public static void preparaSegnatura(ProtocolloIngresso pi, String segnatura) {
		if (segnatura != null && !segnatura.equals("")) {
			XMLUtil.XMLParseSegnatura(segnatura,pi.getProtocollo());
		}
	}

	public static String salvaFile(String folder, String filename, Object part)
			throws Exception {
		BufferedInputStream bis = new BufferedInputStream(
				new ByteArrayInputStream(((String) part).getBytes()));
		return salvaFile(folder, filename, bis);
	}

	public static String salvaFile(String tempFolder, String filename,
			InputStream input) throws Exception {
		File file = null;
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		try {
			if (filename == null) {
				file = File.createTempFile("tmp_", ".email", new File(
						tempFolder));
			} else {
				String name = new File(filename).getName();
				file = new File(tempFolder + File.separator + name);
			}

			logger.debug("Saving file:" + file.getAbsolutePath());
			bos = new BufferedOutputStream(new FileOutputStream(file));
			bis = new BufferedInputStream(input);
			FileUtil.writeFile(bis, bos);
		} catch (FileNotFoundException e) {
			throw new Exception(e);
		} catch (IOException e) {
			throw new Exception(e);
		} finally {
			FileUtil.closeOS(bos);
			FileUtil.closeIS(bis);
		}
		return file.getAbsolutePath();
	}

	public void removeTempObject(HttpSession session) {
		session.removeAttribute(Constants.TMP_MSG_ENTRATA_OBJ);
	}
	
	public String html2text(String html) {
	    return Jsoup.parse(html).text();
	}

}
