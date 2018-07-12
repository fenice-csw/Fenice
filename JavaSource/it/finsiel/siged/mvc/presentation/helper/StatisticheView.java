package it.finsiel.siged.mvc.presentation.helper;

import java.io.Serializable;

public class StatisticheView implements Serializable{

	private static final long serialVersionUID = 1L;

	public StatisticheView() {
    }

    private String ufficio;

    private String utente;

    private int ufficioId;

    private int utenteId;

    private String numProt;

    private String numProtSospesi;

    private String numProtLavorazione;

    private String numProtAtti;

    private String numProtRisposta;

    private String numProtAnnullati;

    private String numProtRifiutati;

    private String numProtProcedimento;

    public String getNumProt() {
        return numProt;
    }

    public void setNumProt(String numProt) {
        this.numProt = numProt;
    }

    public String getNumProtAnnullati() {
        return numProtAnnullati;
    }

    public void setNumProtAnnullati(String numProtAnnullati) {
        this.numProtAnnullati = numProtAnnullati;
    }

    public int getUfficioId() {
        return ufficioId;
    }

    public void setUfficioId(int ufficioId) {
        this.ufficioId = ufficioId;
    }

    public int getUtenteId() {
        return utenteId;
    }

    public void setUtenteId(int utenteId) {
        this.utenteId = utenteId;
    }

    public String getNumProtAtti() {
        return numProtAtti;
    }

    public void setNumProtAtti(String numProtAtti) {
        this.numProtAtti = numProtAtti;
    }

    public String getNumProtLavorazione() {
        return numProtLavorazione;
    }

    public void setNumProtLavorazione(String numProtLavorazione) {
        this.numProtLavorazione = numProtLavorazione;
    }

    public String getNumProtRisposta() {
        return numProtRisposta;
    }

    public void setNumProtRisposta(String numProtRisposta) {
        this.numProtRisposta = numProtRisposta;
    }

    public String getNumProtSospesi() {
        return numProtSospesi;
    }

    public void setNumProtSospesi(String numProtSospesi) {
        this.numProtSospesi = numProtSospesi;
    }

    public String getUfficio() {
        return ufficio;
    }

    public void setUfficio(String ufficio) {
        this.ufficio = ufficio;
    }

    public String getUtente() {
        return utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }

    public String getNumProtRifiutati() {
        return numProtRifiutati;
    }

    public void setNumProtRifiutati(String numProtRifiutati) {
        this.numProtRifiutati = numProtRifiutati;
    }

    public String getNumProtProcedimento() {
        return numProtProcedimento;
    }

    public void setNumProtProcedimento(String numProtProcedimento) {
        this.numProtProcedimento = numProtProcedimento;
    }
    
}