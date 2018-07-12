package it.finsiel.siged.mvc.presentation.actionform.evidenza;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.struts.action.ActionForm;

public class EvidenzaForm extends ActionForm {

    private int numero;

    private Timestamp data;

    private String ufficio;

    private String oggetto;
    
    
    // private String referente;

    private int id;

    private Collection evidenzeProcedimenti;

    private Collection evidenzeFascicoli;

    private ArrayList elencoEvidenze = new ArrayList();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    // public String getReferente() {
    // return referente;
    // }
    //
    // public void setReferente(String referente) {
    // this.referente = referente;
    // }

    public String getUfficio() {
        return ufficio;
    }

    public void setUfficio(String ufficio) {
        this.ufficio = ufficio;
    }

    public void inizializzaForm() {
        setNumero(0);
        setData(null);
        setUfficio(null);
        setOggetto(null);
        // setReferente(null);
        setId(0);

    }

    public ArrayList getElencoEvidenze() {
        return elencoEvidenze;
    }

    public void setElencoEvidenze(ArrayList elencoEvidenze) {
        this.elencoEvidenze = elencoEvidenze;
    }

    public Collection getEvidenzeProcedimenti() {
        return evidenzeProcedimenti;
    }

    public void setEvidenzeProcedimenti(Collection evidenzeProcedimenti) {
        this.evidenzeProcedimenti = evidenzeProcedimenti;
    }

    public Collection getEvidenzeFascicoli() {
        return evidenzeFascicoli;
    }

    public void setEvidenzeFascicoli(Collection evidenzeFascicoli) {
        this.evidenzeFascicoli = evidenzeFascicoli;
    }
   
}