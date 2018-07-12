package it.finsiel.siged.mvc.presentation.action.documentale;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.DocumentaleDelegate;
import it.finsiel.siged.mvc.presentation.actionform.documentale.CartelleForm;
import it.finsiel.siged.mvc.presentation.actionform.documentale.DocumentiCondivisiForm;
import it.finsiel.siged.mvc.vo.documentale.CartellaVO;
import it.finsiel.siged.util.NumberUtil;

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

/**
 * Implementation of <strong>Action </strong> to create a new E-Photo Utente.
 * 
 * @author Almaviva sud.
 * 
 */

public final class DocumentiCondivisiAction extends Action {

    static Logger logger = Logger.getLogger(DocumentiCondivisiAction.class
            .getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionMessages errors = new ActionMessages();
        HttpSession session = request.getSession(true);
        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
        int caricaId = utente.getCaricaInUso();
        DocumentaleDelegate delegate = DocumentaleDelegate.getInstance();
        DocumentiCondivisiForm cForm = (DocumentiCondivisiForm) form;

        if (request.getParameter("documentoSelezionatoId") != null) {
            int docId = NumberUtil.getInt(request
                    .getParameter("documentoSelezionatoId"));
            try {
                if (delegate.getTipoPermessoSuDocumento(docId, caricaId,
                        Organizzazione.getInstance().getUfficio(
                                utente.getUfficioInUso())
                                .getListaUfficiDiscendentiIdStr()) >= 0) {
                    request.setAttribute("documentoId", new Integer(docId));
                    return mapping.findForward("visualizzaDocumento");
                } else {
                    errors.add("permissi", new ActionMessage(
                            "error.documento.no_permission"));
                    saveErrors(request, errors);
                    return mapping.findForward("input");
                }
            } catch (DataException e1) {
                errors.add("generale",
                        new ActionMessage("database.cannot.load"));
            }
        }
        try {
            cForm.setFileCondivisi(DocumentaleDelegate.getInstance()
                    .getFileCondivisiC(
                            Organizzazione.getInstance().getUfficio(
                                    utente.getUfficioInUso())
                                    .getListaUfficiDiscendentiIdStr(),
                            utente.getValueObject().getId().intValue())
                    .values());
        } catch (DataException e) {
            errors.add("generale", new ActionMessage("database.cannot.load"));
        }
        if (!errors.isEmpty())
            saveErrors(request, errors);
        return mapping.findForward("input");
    }

    public CartellaVO preparaCartellaVO(CartelleForm cForm, Utente utente) {
        CartellaVO vo = new CartellaVO();
        vo.setAooId(utente.getValueObject().getAooId());
        vo.setNome(cForm.getNomeCartella());
        vo.setParentId(cForm.getCartellaCorrenteId());
        vo.setRoot(false);
        vo.setCaricaId(utente.getCaricaInUso());
        return vo;
    }

}