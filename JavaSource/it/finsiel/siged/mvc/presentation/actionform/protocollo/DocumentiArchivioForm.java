package it.finsiel.siged.mvc.presentation.actionform.protocollo;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class DocumentiArchivioForm extends ActionForm {
    static Logger logger = Logger.getLogger(DocumentiArchivioForm.class
            .getName());

    private Collection documentiInviati = new ArrayList();

    private int documentoSelezionatoId;

    public DocumentiArchivioForm() {
    }

    public int getDocumentoSelezionatoId() {
        return documentoSelezionatoId;
    }

    public Collection getDocumentiInviati() {
        return documentiInviati;
    }

    public void setDocumentiInviati(Collection documentiInviati) {
        this.documentiInviati = documentiInviati;
    }

    public void setDocumentoSelezionatoId(int documentoSelezionatoId) {
        this.documentoSelezionatoId = documentoSelezionatoId;
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (request.getParameter("cancella") != null
                && getDocumentoSelezionatoId() == 0) {
            errors.add("documento", new ActionMessage("selezione.obbligatoria",
                    "il documento", "da eliminare dalla coda invio"));
        } else if (request.getParameter("protocolla") != null
                && getDocumentoSelezionatoId() == 0) {
            errors.add("documento", new ActionMessage("selezione.obbligatoria",
                    "il documento", "da protocollare"));
        }

        return errors;
    }
}