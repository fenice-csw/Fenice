package it.finsiel.siged.mvc.presentation.action.amministrazione.firmadigitale;

import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.dao.digitalsignature.VerificaFirma;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.business.FirmaDigitaleDelegate;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.firmadigitale.CertificateAuthorityForm;
import it.finsiel.siged.mvc.vo.firma.CaVO;
import it.finsiel.siged.mvc.vo.firma.CrlUrlVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

/**
 * Implementation of <strong>Action </strong> to create a new E-Photo Utente.
 * 
 * @author Almaviva sud.
 * 
 */

public class CertificateAuthorityAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    static Logger logger = Logger.getLogger(CertificateAuthorityAction.class
            .getName());

    // --------------------------------------------------------- Public Methods

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     * 
     * @param mapping
     *            The ActionMapping used to select this instance
     * @param form
     *            The optional ActionForm bean for this request (if any)
     * @param request
     *            The HTTP request we are processing
     * @param response
     *            The HTTP response we are creating
     * 
     * @exception Exception
     *                if business logic throws an exception
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionMessages errors = new ActionMessages();
        CertificateAuthorityForm caForm = (CertificateAuthorityForm) form;

        if (form == null) {
            caForm = new CertificateAuthorityForm();
            request.setAttribute(mapping.getAttribute(), caForm);
        }
        errors = caForm.validate(mapping, request);
        if (errors.isEmpty()) {
            if (request.getParameter("caSelezionata") != null) {
                // visualizza Ca
                try {
                    CaVO ca = FirmaDigitaleDelegate.getInstance().getCaById(
                            NumberUtil.getInt(request
                                    .getParameter("caSelezionata")));
                    if (ReturnValues.FOUND == ca.getReturnValue()) {
                        caricaForm(caForm, ca);
                    } else {
                        errors.add("generale", new ActionMessage(
                                "firmadigitale.errore.ca_non_trovata"));
                    }
                } catch (DataException e) {
                    errors.add("generale", new ActionMessage(
                            "database.cannot.load"));
                }
            } else if (request.getParameter("importa") != null) {
                uploadCertificate(caForm, errors);
            } else if (request.getParameter("nuovo") != null) {
                caricaForm(caForm, new CaVO());
            } else if (request.getParameter("rimuoviCRL") != null) {
                rimuoviSelezionati(caForm);
            } else if (request.getParameter("aggiungiCRL") != null) {
                aggiungiCRL(caForm);
            } else if (request.getParameter("salva") != null) {
                try {
                    CaVO ca = new CaVO();
                    caricaModel(caForm, ca);
                    FirmaDigitaleDelegate.getInstance()
                            .salvaCertificateAuthority(ca);
                    ActionMessages msg = new ActionMessages();
                    msg.add("salvato", new ActionMessage(
                            "firmadigitale.ca.salvata"));
                    saveMessages(request, msg);
                } catch (DataException e) {
                    errors.add("generale", new ActionMessage(
                            "database.cannot.save"));
                }
            }
        }
        saveErrors(request, errors);
        return (mapping.findForward("input"));
    }

    private void uploadCertificate(CertificateAuthorityForm form,
            ActionMessages errors) {
        FormFile file = form.getFormFileUpload();
        String fileName = file.getFileName();
        int size = file.getFileSize();

        if (size > 0 && !"".equals(fileName)) {
            try {
                InputStream stream = new BufferedInputStream(file
                        .getInputStream(), FileConstants.BUFFER_SIZE);
                CaVO ca = VerificaFirma.getCaFromCertificate(stream);
                caricaForm(form, ca);
            } catch (Exception e) {
                logger.error("", e);
                errors.add("formFileUpload", new ActionMessage(
                        "firmadigitale.errore.conversione", fileName,
                        "."
 //                               + e.getMessage()
                                ));
            }
        }
    }

    public void rimuoviSelezionati(CertificateAuthorityForm caForm) {
        String[] ids = caForm.getCrlSelezionatiId();
        if (ids != null) {
            for (int i = 0; i < ids.length; i++)
                caForm.getCrls().remove(ids[i]);
        }
    }

    public void aggiungiCRL(CertificateAuthorityForm caForm) {
        CrlUrlVO u = new CrlUrlVO();
        u.setUrl(caForm.getAddCrlUrl());
        u.setTipo(VerificaFirma.getTipoProtocollo(caForm.getAddCrlUrl()));
        caForm.getCrls().put(u.getUrl(), u);
    }

    public void caricaForm(CertificateAuthorityForm caForm, CaVO ca) {
        if (ca.getIssuerCN() != null)
            caForm.setIssuerCN(ca.getIssuerCN());
        else
            caForm.setIssuerCN("");
        if (ca.getValidoDal() != null)
            caForm.setValidoDal(DateUtil.formattaData(ca.getValidoDal()
                    .getTime()));
        else
            caForm.setValidoDal("");
        if (ca.getValidoAl() != null)
            caForm.setValidoAl(DateUtil
                    .formattaData(ca.getValidoAl().getTime()));
        else
            caForm.setValidoAl("");
        caForm.setCrls(ca.getCrlUrls());
        caForm.setId(ca.getId() != null ? ca.getId().intValue() : 0);
    }

    public void caricaModel(CertificateAuthorityForm caForm, CaVO ca) {
        ca.setIssuerCN(caForm.getIssuerCN());
        ca.setValidoDal(DateUtil.toDate(caForm.getValidoDal()));
        ca.setValidoAl(DateUtil.toDate(caForm.getValidoAl()));
        ca.setCrlUrls(caForm.getCrls());
    }
}
