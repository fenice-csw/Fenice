
package it.finsiel.siged.mvc.presentation.actionform.documentale;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.struts.action.ActionForm;

/**
 * @author Almaviva sud
 */
public class DocumentiCondivisiForm extends ActionForm {

    private Collection fileCondivisi = new ArrayList();

    public Collection getFileCondivisi() {
        return fileCondivisi;
    }

    public void setFileCondivisi(Collection fileCondivisi) {
        this.fileCondivisi = fileCondivisi;
    }
}
