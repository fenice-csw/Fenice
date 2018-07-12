package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloUscitaForm;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class EtichetteDestinatariAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        request.setAttribute("protocolloId", new Integer(request
                .getParameter("barcode_msg")));
        int protocolloId = Integer.parseInt(request.getAttribute("protocolloId").toString());
        ProtocolloUscitaForm protocolloForm = (ProtocolloUscitaForm) form;
        Map dest = ProtocolloDelegate.getInstance().getDestinatariProtocollo(protocolloId);
        protocolloForm.setDestinatari(dest);
        return mapping.findForward("input");
    }
}