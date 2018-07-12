package it.finsiel.siged.mvc.presentation.actionform.amministrazione;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;

public class AcquisizioneMassivaForm extends ActionForm {

    private Integer documentiAcquisiti;

    private Collection documentiAcquisizione;

    private Collection logsAcquisizione;

    private String[] documentiDaEliminare;

    static Logger logger = Logger.getLogger(AcquisizioneMassivaForm.class
            .getName());

    public Collection getDocumentiAcquisizione() {
        return documentiAcquisizione;
    }

    public void setDocumentiAcquisizione(Collection documenti) {
        this.documentiAcquisizione = documenti;
    }

    public Collection getLogsAcquisizione() {
        return logsAcquisizione;
    }

    public void setLogsAcquisizione(Collection logsAcquisizione) {
        this.logsAcquisizione = logsAcquisizione;
    }

    public Integer getDocumentiAcquisiti() {
        return documentiAcquisiti;
    }

    public void setDocumentiAcquisiti(Integer documentiAcquisiti) {
        this.documentiAcquisiti = documentiAcquisiti;
    }

    public String[] getDocumentiDaEliminare() {
        return documentiDaEliminare;
    }

    public void setDocumentiDaEliminare(String[] documentiDaEliminare) {
        this.documentiDaEliminare = documentiDaEliminare;
    }
}