package it.finsiel.siged.mvc.presentation.actionform.amministrazione;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;

public class ScartoProtocolliForm extends ActionForm {

    private int anno;

    private Collection scartiProtocollo;

    private Collection anniScartabili;

    private int risultatiScarto;

    static Logger logger = Logger.getLogger(ScartoProtocolliForm.class
            .getName());

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public Collection getAnniScartabili() {
        return anniScartabili;
    }

    public void setAnniScartabili(Collection anniScartabili) {
        this.anniScartabili = anniScartabili;
    }

    public Collection getScartiProtocollo() {
        return scartiProtocollo;
    }

    public void setScartiProtocollo(Collection scartiProtocollo) {
        this.scartiProtocollo = scartiProtocollo;
    }

    public int getRisultatiScarto() {
        return risultatiScarto;
    }

    public void setRisultatiScarto(int risultatiScarto) {
        this.risultatiScarto = risultatiScarto;
    }
}