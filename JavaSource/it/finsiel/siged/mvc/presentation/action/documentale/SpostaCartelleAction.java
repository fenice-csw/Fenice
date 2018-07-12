package it.finsiel.siged.mvc.presentation.action.documentale;

import it.finsiel.siged.mvc.presentation.actionform.documentale.CartelleForm;
import it.finsiel.siged.util.NumberUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Implementation of <strong>Action </strong> to create a new E-Photo Utente.
 * 
 * @author Almaviva sud.
 * 
 */

public class SpostaCartelleAction extends CartelleAction {

    static Logger logger = Logger.getLogger(SpostaCartelleAction.class
            .getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String cartellaInizialeId = (String) request.getAttribute("gotoCartellaId");
        String documentoId = (String) request.getAttribute("spostaDocumentoId");
        CartelleForm cForm = (CartelleForm) form;
        if (documentoId != null) {
            cForm.setDocumentoId(NumberUtil.getInt(documentoId));
            cForm.setCartellaCorrenteId(NumberUtil.getInt(cartellaInizialeId));
            if(request.getAttribute("fromProcedimento")!=null){
            	cForm.setFromProcedimento(true);
            	cForm.setProcedimentoId((Integer)request.getAttribute("fromProcedimento"));
            }
        }
        return super.execute(mapping, cForm, request, response);
    }

}