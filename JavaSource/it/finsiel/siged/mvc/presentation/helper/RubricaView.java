package it.finsiel.siged.mvc.presentation.helper;

public class RubricaView {

    public RubricaView() {
    }

    private String nominativo;

    private String indirizzo;

    private String codiceFiscale;

    private String partitaIva;

    private String email;

    private String telefonoFax;

    private String telefonoReferente;

    private String qualifica;

    private String comuneNascita;

    private String dataNascita;

    public String getQualifica() {
        return qualifica;
    }

    public void setQualifica(String qualifica) {
        this.qualifica = qualifica;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getNominativo() {
        return nominativo;
    }

    public void setNominativo(String nominativo) {
        this.nominativo = nominativo;
    }

    public String getPartitaIva() {
        return partitaIva;
    }

    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }

    public String getTelefonoFax() {
        return telefonoFax;
    }

    public void setTelefonoFax(String telefonoFax) {
        this.telefonoFax = telefonoFax;
    }

    public String getTelefonoReferente() {
        return telefonoReferente;
    }

    public void setTelefonoReferente(String telefonoReferente) {
        this.telefonoReferente = telefonoReferente;
    }

    public String getComuneNascita() {
        return comuneNascita;
    }

    public void setComuneNascita(String comuneNascita) {
        this.comuneNascita = comuneNascita;
    }

    public String getDataNascita() {
        if (dataNascita != null)
            return dataNascita;
        else
            return "";
    }

    public void setDataNascita(String dataNascita) {
        this.dataNascita = dataNascita;
    }
}