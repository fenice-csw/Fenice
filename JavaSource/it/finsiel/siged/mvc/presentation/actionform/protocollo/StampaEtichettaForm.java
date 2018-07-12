package it.finsiel.siged.mvc.presentation.actionform.protocollo;

import it.compit.fenice.enums.ReturnPageEnum;
import it.finsiel.siged.mvc.vo.IdentityVO;
import it.finsiel.siged.util.NumberUtil;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class StampaEtichettaForm extends ActionForm {
    
	private static final long serialVersionUID = 1L;


    private String margineSinistro;

    private String margineSuperiore;

    private String larghezzaEtichetta;

    private String altezzaEtichetta;

    private String tipoStampa;

    private String modoStampaA4;

    private int deltaXMM;

    private int deltaYMM;

    private String stampaSuFoglioA4;

    private String rotazione;

    private ReturnPageEnum inputPage;
    
    private Collection<IdentityVO> modalitaStampaA4;

    public String getAltezzaEtichetta() {
        return altezzaEtichetta;
    }

    public void setAltezzaEtichetta(String altezzaEticheta) {
        this.altezzaEtichetta = altezzaEticheta;
    }

    public String getLarghezzaEtichetta() {
        return larghezzaEtichetta;
    }

    public void setLarghezzaEtichetta(String larghezzaEtichetta) {
        this.larghezzaEtichetta = larghezzaEtichetta;
    }

    public String getMargineSinistro() {
        return margineSinistro;
    }

    public void setMargineSinistro(String margineSinistro) {
        this.margineSinistro = margineSinistro;
    }

    public String getMargineSuperiore() {
        return margineSuperiore;
    }

    public void setMargineSuperiore(String margineSuperiore) {
        this.margineSuperiore = margineSuperiore;
    }

   /*
    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
    */
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    }

    public String getModoStampaA4() {
        return modoStampaA4;
    }

    public void setModoStampaA4(String modoStampaA4) {
        this.modoStampaA4 = modoStampaA4;
    }

    public Collection<IdentityVO> getModalitaStampaA4() {
        return modalitaStampaA4;
    }

    public void setModalitaStampaA4(Collection<IdentityVO> modalitaStampaA4) {
        this.modalitaStampaA4 = modalitaStampaA4;
    }

    public String getTipoStampa() {
        return tipoStampa;
    }

    public void setTipoStampa(String tipoStampa) {
        this.tipoStampa = tipoStampa;
    }

    public int getDeltaXMM() {
        return deltaXMM;
    }

    public void setDeltaXMM(int deltaXMM) {
        this.deltaXMM = deltaXMM;
    }

    public int getDeltaYMM() {
        return deltaYMM;
    }

    public void setDeltaYMM(int deltaYMM) {
        this.deltaYMM = deltaYMM;
    }

    public String getStampaSuFoglioA4() {
        return stampaSuFoglioA4;
    }

    public String getRotazione() {
        return rotazione;
    }

    public void setRotazione(String rotazione) {
        this.rotazione = rotazione;
    }

    public void setStampaSuFoglioA4(String stampaSuFoglioA4) {
        this.stampaSuFoglioA4 = stampaSuFoglioA4;
    }
    
    public ReturnPageEnum getInputPage() {
		return inputPage;
	}

	public void setInputPage(ReturnPageEnum inputPage) {
		this.inputPage = inputPage;
	}

	public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (request.getParameter("btnConfermaStampa") != null) {
            if (NumberUtil.getInt(getMargineSinistro()) == -1) {
                errors.add("margineSx", new ActionMessage(
                        "formato.numerico.errato", "Margine sinistro"));

            }
            if (NumberUtil.getInt(getMargineSuperiore()) == -1) {
                errors.add("margineSx", new ActionMessage(
                        "formato.numerico.errato", "Margine superiore"));

            }
            if (NumberUtil.getInt(getAltezzaEtichetta()) == -1) {
                errors.add("margineSx", new ActionMessage(
                        "formato.numerico.errato", "Altezza etichetta"));

            }
            if (NumberUtil.getInt(getLarghezzaEtichetta()) == -1) {
                errors.add("margineSx", new ActionMessage(
                        "formato.numerico.errato", "Larghezza etichetta"));

            }
            if (NumberUtil.getInt(getRotazione()) == -1) {
                errors.add("margineSx", new ActionMessage(
                        "formato.numerico.errato", "Rotazione"));

            }

        }
        return errors;
    }

}