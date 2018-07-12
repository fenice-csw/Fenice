package it.finsiel.siged.mvc.presentation.actionform.protocollo;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class FascicoliArchivioForm extends ActionForm {
    static Logger logger = Logger.getLogger(FascicoliArchivioForm.class
            .getName());

    private SortedMap fascicoliInviati = new TreeMap();;

    private int fascicoloSelezionatoId;

    public FascicoliArchivioForm() {
    }

    public SortedMap getFascicoliInviati() {
        return fascicoliInviati;
    }

    public Collection getFascicoliInviatiCollection() {
        if (fascicoliInviati != null) {
            return fascicoliInviati.values();
        } else
            return null;
    }

    public void setFascicoliInviati(SortedMap fascicoliInviati) {
        this.fascicoliInviati = fascicoliInviati;
    }

    public int getFascicoloSelezionatoId() {
        return fascicoloSelezionatoId;
    }

    public void setFascicoloSelezionatoId(int fascicoloSelezionatoId) {
        this.fascicoloSelezionatoId = fascicoloSelezionatoId;
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (request.getParameter("cancella") != null
                && getFascicoloSelezionatoId() == 0) {
            errors.add("fascicolo", new ActionMessage("selezione.obbligatoria",
                    "il fascicolo", "da eliminare dalla coda invio"));
        } else if (request.getParameter("visualizza") != null
                && getFascicoloSelezionatoId() == 0) {
            errors.add("nome", new ActionMessage("selezione.obbligatoria",
                    "il fascicolo", "da visualizzare"));
        } else if (request.getParameter("protocolla") != null
                && getFascicoloSelezionatoId() == 0) {
            errors.add("fascicolo", new ActionMessage("selezione.obbligatoria",
                    "il fascicolo", "da protocollare"));
        }

        return errors;
    }

}