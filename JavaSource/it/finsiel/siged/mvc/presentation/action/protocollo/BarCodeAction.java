package it.finsiel.siged.mvc.presentation.action.protocollo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class BarCodeAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        request.setAttribute("protocolloId", new Integer(request
                .getParameter("barcode_msg")));
        return mapping.findForward("input");
    }
}