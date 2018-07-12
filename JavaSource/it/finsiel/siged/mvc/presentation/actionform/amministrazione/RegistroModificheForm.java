package it.finsiel.siged.mvc.presentation.actionform.amministrazione;

import it.finsiel.siged.mvc.presentation.actionform.protocollo.AlberoUfficiUtentiForm;
import it.finsiel.siged.mvc.presentation.actionform.report.ReportCommonForm;
import it.finsiel.siged.util.DateUtil;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class RegistroModificheForm extends ReportCommonForm implements
        AlberoUfficiUtentiForm {
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if ((getBtnStampa() != null) || (getBtnVisualizza() != null)) {
            if (getDataInizio() == null || "".equals(getDataInizio())) {
                errors.add("dataInizio", new ActionMessage(
                        "campo.obbligatorio", "data inizio", ""));
            } else if (!DateUtil.isData(getDataInizio())) {
                errors.add("dataInizio", new ActionMessage(
                        "formato.data.errato", "data inizio", ""));
            }
            if (getDataFine() == null || "".equals(getDataFine())) {
                errors.add("dataFine", new ActionMessage("campo.obbligatorio",
                        "data fine", ""));
            } else if (!DateUtil.isData(getDataFine())) {
                errors.add("dataFine", new ActionMessage("formato.data.errato",
                        "data fine", ""));
            } else if (getDataInizio() != null
                    && !"".equals(getDataInizio().trim())
                    && DateUtil.toDate(getDataFine()).before(
                            DateUtil.toDate(getDataInizio()))) {
                errors
                        .add("dataInizio", new ActionMessage(
                                "date_incongruenti"));
            }
            if (errors.size() > 0) {
                setBtnStampa(null);
                setBtnVisualizza(null);
            }
        }
        return errors;
    }

}