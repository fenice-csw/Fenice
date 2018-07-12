package it.finsiel.siged.mvc.vo.protocollo;

import it.finsiel.siged.mvc.vo.VersioneVO;

public class ConfigurazioneUtenteVO extends VersioneVO {

    public ConfigurazioneUtenteVO() {

    }

    private String checkTipoDocumento;

    private String checkDataDocumento;

    private String checkRicevutoIl;

    private String checkTipoMittente;

    private String checkMittente;

    private String checkAssegnatari;

    private String checkDestinatari;

    private String checkTitolario;

    private String checkOggetto;

    private int assegnatarioUtenteId;

    private int assegnatarioUfficioId;

    private String destinatario;

    private int titolario;

    private int titolarioId;

    private int utenteId;

    private int tipoDocumentoId;

    private String oggetto;

    private String dataDocumento;

    private String dataRicezione;

    private String tipoMittente;

    private String mittente;

    private String parametriStampante;

    public String getDataDocumento() {
        return dataDocumento;
    }

    public void setDataDocumento(String dataDocumento) {
        this.dataDocumento = dataDocumento;
    }

    public String getMittente() {
        return mittente;
    }

    public void setMittente(String mittente) {
        this.mittente = mittente;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public String getTipoMittente() {
        return tipoMittente;
    }

    public void setTipoMittente(String tipoMittente) {
        this.tipoMittente = tipoMittente;
    }

    public String getCheckAssegnatari() {
        return checkAssegnatari;
    }

    public void setCheckAssegnatari(String checkAssegnatari) {
        this.checkAssegnatari = checkAssegnatari;
    }

    public String getCheckDataDocumento() {
        return checkDataDocumento;
    }

    public void setCheckDataDocumento(String checkDataDocumento) {
        this.checkDataDocumento = checkDataDocumento;
    }

    public String getCheckMittente() {
        return checkMittente;
    }

    public void setCheckMittente(String checkMittente) {
        this.checkMittente = checkMittente;
    }

    public String getCheckRicevutoIl() {
        return checkRicevutoIl;
    }

    public void setCheckRicevutoIl(String checkRicevutoIl) {
        this.checkRicevutoIl = checkRicevutoIl;
    }

    public String getCheckTipoDocumento() {
        return checkTipoDocumento;
    }

    public void setCheckTipoDocumento(String checkTipoDocumento) {
        this.checkTipoDocumento = checkTipoDocumento;
    }

    public String getCheckTipoMittente() {
        return checkTipoMittente;
    }

    public void setCheckTipoMittente(String checkTipoMittente) {
        this.checkTipoMittente = checkTipoMittente;
    }

    public int getAssegnatarioUfficioId() {
        return assegnatarioUfficioId;
    }

    public void setAssegnatarioUfficioId(int assegnatarioUfficioId) {
        this.assegnatarioUfficioId = assegnatarioUfficioId;
    }

    public int getAssegnatarioUtenteId() {
        return assegnatarioUtenteId;
    }

    public void setAssegnatarioUtenteId(int assegnatarioUtenteId) {
        this.assegnatarioUtenteId = assegnatarioUtenteId;
    }

    public String getCheckDestinatari() {
        return checkDestinatari;
    }

    public void setCheckDestinatari(String checkDestinatari) {
        this.checkDestinatari = checkDestinatari;
    }

    public String getCheckTitolario() {
        return checkTitolario;
    }

    public void setCheckTitolario(String checkTitolario) {
        this.checkTitolario = checkTitolario;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public int getTipoDocumentoId() {
        return tipoDocumentoId;
    }

    public void setTipoDocumentoId(int tipoDocumentoId) {
        this.tipoDocumentoId = tipoDocumentoId;
    }

    public int getTitolario() {
        return titolario;
    }

    public void setTitolario(int titolario) {
        this.titolario = titolario;
    }

    public int getTitolarioId() {
        return titolarioId;
    }

    public void setTitolarioId(int titolarioId) {
        this.titolarioId = titolarioId;
    }

    public int getUtenteId() {
        return utenteId;
    }

    public void setUtenteId(int utenteId) {
        this.utenteId = utenteId;
    }

    public String getCheckOggetto() {
        return checkOggetto;
    }

    public void setCheckOggetto(String checkOggetto) {
        this.checkOggetto = checkOggetto;
    }

    public String getDataRicezione() {
        return dataRicezione;
    }

    public void setDataRicezione(String dataRicezione) {
        this.dataRicezione = dataRicezione;
    }

    public String getParametriStampante() {
        return parametriStampante;
    }

    public void setParametriStampante(String parametriStampante) {
        this.parametriStampante = parametriStampante;
    }

}