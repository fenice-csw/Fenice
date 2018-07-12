package it.compit.fenice.mvc.presentation.actionform.report;

import it.finsiel.siged.mvc.presentation.actionform.report.ReportCommonForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public final class ReportGenericForm extends ReportCommonForm {

	private static final long serialVersionUID = 1L;

	public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        return errors;
    }
}