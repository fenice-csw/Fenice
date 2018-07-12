package it.finsiel.siged.mvc.vo.protocollo;

import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.mvc.bo.TitolarioBO;
import it.finsiel.siged.mvc.vo.VersioneVO;

import java.util.Date;

public class FaldoneVO extends VersioneVO {

    private int aooId;

    private int anno;

    private int numero;

    private String numeroFaldone;

    private String oggetto;

    private int ufficioId;

    private int titolarioId;

    private String codiceLocale;

    private String sottoCategoria;

    private String stato;

    private String nota;

    private Date dataCreazione;

    private Date dataCarico;

    private Date dataScarico;

    private Date dataEvidenza;

    private Date dataMovimento;

    private int posizioneSelezionata;

    private String collocazioneLabel1;

    private String collocazioneLabel2;

    private String collocazioneLabel3;

    private String collocazioneLabel4;

    private String collocazioneValore1;

    private String collocazioneValore2;

    private String collocazioneValore3;

    private String collocazioneValore4;

    // fine modifiche 23/03/2006

    public FaldoneVO() {

    }

    public int getPosizioneSelezionata() {
        return posizioneSelezionata;
    }

    public void setPosizioneSelezionata(int posizioneSelezionata) {
        this.posizioneSelezionata = posizioneSelezionata;
    }

    public Date getDataMovimento() {
        return dataMovimento;
    }

    public void setDataMovimento(Date dataMovimento) {
        this.dataMovimento = dataMovimento;
    }

    public Date getDataCarico() {
        return dataCarico;
    }

    public void setDataCarico(Date dataCarico) {
        this.dataCarico = dataCarico;
    }

    public Date getDataScarico() {
        return dataScarico;
    }

    public void setDataScarico(Date dataScarico) {
        this.dataScarico = dataScarico;
    }

    public Date getDataEvidenza() {
        return dataEvidenza;
    }

    public void setDataEvidenza(Date dataEvidenza) {
        this.dataEvidenza = dataEvidenza;
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public String getCollocazioneLabel1() {
        return collocazioneLabel1;
    }

    public void setCollocazioneLabel1(String collocazioneLabel1) {
        this.collocazioneLabel1 = collocazioneLabel1;
    }

    public String getCollocazioneLabel2() {
        return collocazioneLabel2;
    }

    public void setCollocazioneLabel2(String collocazioneLabel2) {
        this.collocazioneLabel2 = collocazioneLabel2;
    }

    public String getCollocazioneLabel3() {
        return collocazioneLabel3;
    }

    public void setCollocazioneLabel3(String collocazioneLabel3) {
        this.collocazioneLabel3 = collocazioneLabel3;
    }

    public String getCollocazioneLabel4() {
        return collocazioneLabel4;
    }

    public void setCollocazioneLabel4(String collocazioneLabel4) {
        this.collocazioneLabel4 = collocazioneLabel4;
    }

    public String getCollocazioneValore1() {
        return collocazioneValore1;
    }

    public void setCollocazioneValore1(String collocazioneValore1) {
        this.collocazioneValore1 = collocazioneValore1;
    }

    public String getCollocazioneValore2() {
        return collocazioneValore2;
    }

    public void setCollocazioneValore2(String collocazioneValore2) {
        this.collocazioneValore2 = collocazioneValore2;
    }

    public String getCollocazioneValore3() {
        return collocazioneValore3;
    }

    public void setCollocazioneValore3(String collocazioneValore3) {
        this.collocazioneValore3 = collocazioneValore3;
    }

    public String getCollocazioneValore4() {
        return collocazioneValore4;
    }

    public void setCollocazioneValore4(String collocazioneValore4) {
        this.collocazioneValore4 = collocazioneValore4;
    }

    public int getAooId() {
        return aooId;
    }

    public void setAooId(int aooId) {
        this.aooId = aooId;
    }

    public int getTitolarioId() {
        return titolarioId;
    }

    public void setTitolarioId(int titolarioId) {
        this.titolarioId = titolarioId;
    }

    public String getCodiceLocale() {
        return codiceLocale;
    }

    public void setCodiceLocale(String codiceLocale) {
        this.codiceLocale = codiceLocale;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getNumeroFaldone() {
        return numeroFaldone;
    }

    public void setNumeroFaldone(String numeroFaldone) {
        this.numeroFaldone = numeroFaldone;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public String getSottoCategoria() {
        return sottoCategoria;
    }

    public void setSottoCategoria(String sottoCategoria) {
        this.sottoCategoria = sottoCategoria;
    }

    public int getUfficioId() {
        return ufficioId;
    }

    public void setUfficioId(int ufficioId) {
        this.ufficioId = ufficioId;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getPathTitolario() {
        return TitolarioBO.getPathDescrizioneTitolario(getTitolarioId());
    }

    public String getDescUfficio() {
        Ufficio u = Organizzazione.getInstance().getUfficio(getUfficioId());
        if (u != null && u.getValueObject() != null)
            return u.getValueObject().getDescription();
        else
            return "";
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

}