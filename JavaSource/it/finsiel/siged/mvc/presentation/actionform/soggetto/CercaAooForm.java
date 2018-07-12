package it.finsiel.siged.mvc.presentation.actionform.soggetto;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class CercaAooForm extends ActionForm {

    static Logger logger = Logger.getLogger(CercaAooForm.class.getName());

    private ArrayList listaAoo = new ArrayList();

    private ArrayList listaAmm = new ArrayList();

    private String nome;

    private String categoriaId;

    private String cerca;

    /**
     * @return Returns the codice.
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param codiceFiscale
     *            The codice to set.
     */
    public void setNome(String codice) {
        this.nome = codice;
    }

    public ArrayList getListaAoo() {
        return listaAoo;
    }

    public void setListaAoo(ArrayList listaAoo) {
        this.listaAoo = listaAoo;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (cerca != null && (nome == null || nome.length() < 3)) {
            errors.add("nome", new ActionMessage("parametri_ricerca_aoo"));
        }
        return errors;

    }

    /**
     * @return Returns the btnCercaCognome.
     */
    public String getCerca() {
        return cerca;
    }

    /**
     * @param btnCercaCognome
     *            The btnCercaCognome to set.
     */
    public void setCerca(String cerca) {
        this.cerca = cerca;
    }

    public String getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(String categoriaId) {
        this.categoriaId = categoriaId;
    }

    public ArrayList getListaAmm() {
        return listaAmm;
    }

    public void setListaAmm(ArrayList listaAmm) {
        this.listaAmm = listaAmm;
    }
}
