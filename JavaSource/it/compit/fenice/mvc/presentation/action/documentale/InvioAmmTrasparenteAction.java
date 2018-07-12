package it.compit.fenice.mvc.presentation.action.documentale;

import it.compit.fenice.mvc.bo.AmmTrasparenteBO;
import it.compit.fenice.mvc.business.AmmTrasparenteDelegate;
import it.compit.fenice.mvc.vo.ammtrasparente.DocumentoAmmTrasparenteVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.documentale.Documento;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.DocumentaleDelegate;
import it.finsiel.siged.mvc.presentation.actionform.documentale.DocumentoForm;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.util.DateUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Date;
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class InvioAmmTrasparenteAction extends Action {

	// ----------------------------------------------------- Instance Variables

	/**
	 * The <code>Log</code> instance for this application.
	 */
	static Logger logger = Logger.getLogger(InvioAmmTrasparenteAction.class
			.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		HttpSession session = request.getSession();
		DocumentoForm rForm = (DocumentoForm) form;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		session.setAttribute("dForm", rForm);
		AmmTrasparenteDelegate delegate = AmmTrasparenteDelegate.getInstance();
		rForm.setRepertori(delegate.getSezioni(utente.getAreaOrganizzativa()
				.getId()));

		if (form == null) {
			logger.info(" Creating new InvioAmmTrasparenteAction");
			form = new DocumentoForm();
			request.setAttribute(mapping.getAttribute(), form);
		}
		if (request.getParameter("sezId") != null) {
			rForm.setRepertorioId(Integer.valueOf(
					request.getParameter("sezId")).intValue());
		} else if (request.getParameter("btnInvioSezione") != null) {
			if (rForm.getRepertorioId() == 0)
				errors.add("invio_sez_amm_trasparente", new ActionMessage(
						"invio_sez_amm_trasparente.errore_seleziona_sezione"));
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			} else {
				if (!errors.isEmpty()) {
					saveErrors(request, errors);
				} else {
					boolean saved = false;
					DocumentoAmmTrasparenteVO docVO = new DocumentoAmmTrasparenteVO();
					aggiornaInvioSezioneModel(rForm, docVO, utente);
					saved = delegate
							.salvaDocumentoSezionale(docVO, utente, rForm
									.getDocumentoId(), rForm
									.getRepositoryFileId());
					if (saved) {
						messages.add("invio_sez_amm_trasparente", new ActionMessage(
								"documentale_esito_operazione", "Inviato",
								"alla sezione"));
					} else {
						errors.add("invio_sez_amm_trasparente", new ActionMessage(
								"errore_nel_salvataggio"));
					}
					saveMessages(request, messages);
					saveErrors(request, errors);
					request.setAttribute("documentoId", new Integer(rForm
							.getDocumentoId()));
					return mapping.findForward("visualizzaCartella");
				}
			}

		} else if (request.getParameter("btnAnnullaAction") != null) {
			rForm.setRepertorioId(0);
		}
		return (mapping.findForward("input"));
	}

	private void aggiornaInvioSezioneModel(DocumentoForm form,
			DocumentoAmmTrasparenteVO vo, Utente utente) {
		Map<String, DocumentoVO> docs = new HashMap<String, DocumentoVO>(2);
		if (form.getDataDocumento() != null
				&& !form.getDataDocumento().equals(""))
			vo.setDataSezione(new Date(DateUtil.getData(
					form.getDataDocumento()).getTime()));
		vo.setOggetto(form.getOggetto());
		vo.setNote(form.getNote());
		vo.setSezId(form.getRepertorioId());
		vo.setNumeroDocumentoSezione(0);

		try {
			Documento doc = DocumentaleDelegate.getInstance().getDocumentoById(
					form.getDocumentoId());
			DocumentoVO docVO = doc.getFileVO().getDocumentoVO();

			String tempFolder = utente.getValueObject().getTempFolder();
			File tempFile = null;
			OutputStream os = null;
			tempFile = File.createTempFile("tmp_doc", ".fascicolo", new File(
					tempFolder));
			os = new FileOutputStream(tempFile.getAbsolutePath());
			DocumentaleDelegate.getInstance().writeDocumentToStream(
					docVO.getId().intValue(), os);
			os.close();
			docVO.setPath(tempFile.getAbsolutePath());
			docVO.setMustCreateNew(true);
			if (form.getDescrizione() != null
					&& !form.getDescrizione().trim().equals(""))
				docVO.setDescrizione(form.getDescrizione());
			else
				docVO.setDescrizione(docVO.getFileName());
			AmmTrasparenteBO.putAllegato(docVO, docs);
			vo.setDocumenti(docs);
		} catch (Exception e) {
			logger
					.error("InvioAmmTrasparenteAction failed aggiornaInvioSezioneModel");
			e.printStackTrace();
		}
	}

}
