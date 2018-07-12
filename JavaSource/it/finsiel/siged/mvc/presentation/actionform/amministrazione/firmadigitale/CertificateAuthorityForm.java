package it.finsiel.siged.mvc.presentation.actionform.amministrazione.firmadigitale;

import it.finsiel.siged.constant.ProtocolConstants;
import it.finsiel.siged.dao.digitalsignature.VerificaFirma;
import it.finsiel.siged.mvc.presentation.actionform.UploaderForm;
import it.finsiel.siged.mvc.vo.firma.CrlUrlVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class CertificateAuthorityForm extends UploaderForm {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(CertificateAuthorityForm.class
            .getName());

    private int id;

    private String addCrlUrl;

    private String issuerCN;

    private String validoDal;

    private String validoAl;

    private String[] crlSelezionatiId;

    private Map<String,CrlUrlVO> crls = new HashMap<String,CrlUrlVO>(1);

    public String getAddCrlUrl() {
        return addCrlUrl;
    }

    public void setAddCrlUrl(String crlUrl) {
        this.addCrlUrl = crlUrl;
    }

    public String[] getCrlSelezionatiId() {
        return crlSelezionatiId;
    }

    public void setCrlSelezionatiId(String[] crlSelezionatiId) {
        this.crlSelezionatiId = crlSelezionatiId;
    }

    public Map<String,CrlUrlVO> getCrls() {
        return crls;
    }

    public Collection<CrlUrlVO> getCrlsCollection() {
        return crls.values();
    }

    public void setCrls(Map<String,CrlUrlVO> crls) {
        this.crls = crls;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIssuerCN() {
        return issuerCN;
    }

    public void setIssuerCN(String issuerCN) {
        this.issuerCN = issuerCN;
    }

    public String getValidoAl() {
        return validoAl;
    }

    public void setValidoAl(String validoAl) {
        this.validoAl = validoAl;
    }

    public String getValidoDal() {
        return validoDal;
    }

    public void setValidoDal(String validoDal) {
        this.validoDal = validoDal;
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (request.getParameter("caSelezionata") != null) {
            if (!NumberUtil.isInteger(request.getParameter("caSelezionata")))
                errors.add("generale", new ActionMessage(
                        "firmadigitale.errore.ca_selezionata.tipoint"));
        } else if (request.getParameter("importa") != null) {
            if (getFormFileUpload().getFileSize() <= 0) {
                errors.add("formFileUpload", new ActionMessage(
                        "campo.obbligatorio", "File del Certificato", ""));
            }
        } else if (request.getParameter("aggiungiCRL") != null) {
            if (getAddCrlUrl() == null || "".equals(getAddCrlUrl())) {
                errors.add("addCrlUrl", new ActionMessage("campo.obbligatorio",
                        "l'URL", ""));
            } else if (VerificaFirma.getTipoProtocollo(getAddCrlUrl()) == null) {
                errors.add("addCrlUrl", new ActionMessage(
                        "firmadigitale.protocollo.non_supportato",
                        ProtocolConstants.HTTP + "," + ProtocolConstants.HTTPS
                                + "," + ProtocolConstants.LDAP));
            }
        } else if (request.getParameter("salva") != null) {
            if ("".equals(getIssuerCN())) {
                errors.add("issuerCN", new ActionMessage("campo.obbligatorio",
                        "Issuer CN", ""));
            }
            if ("".equals(getValidoDal())) {
                errors.add("validoDal", new ActionMessage("campo.obbligatorio",
                        "Valido dal", ""));
            } else if (!DateUtil.isData(getValidoDal())) {
                errors.add("validoDal", new ActionMessage(
                        "firmadigitale.ca.data_non_valida"));
            }
            if ("".equals(getValidoAl())) {
                errors.add("validoAl", new ActionMessage("campo.obbligatorio",
                        "Valido al", ""));
            } else if (!DateUtil.isData(getValidoDal())) {
                errors.add("validoAl", new ActionMessage(
                        "firmadigitale.ca.data_non_valida"));
            }
        }
        return errors;

    }

}