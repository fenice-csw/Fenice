package it.finsiel.siged.mvc.presentation.actionform.amministrazione.firmadigitale;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public final class ListaCAForm extends ActionForm {

    static Logger logger = Logger.getLogger(ListaCAForm.class.getName());

    private Collection listaCa = new ArrayList();

    public Collection getListaCa() {
        return listaCa;
    }

    public void setListaCa(Collection listaCa) {
        this.listaCa = listaCa;
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;

    }

}