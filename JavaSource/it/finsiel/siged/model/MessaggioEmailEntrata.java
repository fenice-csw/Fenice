package it.finsiel.siged.model;

import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.mvc.vo.posta.EmailVO;
import it.finsiel.siged.mvc.vo.posta.PecDaticertVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;

import java.util.ArrayList;
import java.util.Collection;


public class MessaggioEmailEntrata {

    private EmailVO email = new EmailVO();
    
    private ArrayList<DocumentoVO> allegati = new ArrayList<DocumentoVO>(2);

    private String tempFolder;

    private String tipoEmail;

    private String messaggioErrore;

    private PecDaticertVO daticertXML = new PecDaticertVO();

    
    
    public MessaggioEmailEntrata() {
    }

    public Collection<DocumentoVO> getAllegati() {
        return allegati;
    }

    public void setAllegati(ArrayList<DocumentoVO> allegati) {
        this.allegati = allegati;
    }

    public void addAllegato(DocumentoVO d) {
        allegati.add(d);
    }

    public String getTempFolder() {
        return tempFolder;
    }

    public void setTempFolder(String tempFolder) {
        this.tempFolder = tempFolder;
    }

    public EmailVO getEmail() {
        return email;
    }

    public void setEmail(EmailVO email) {
        this.email = email;
    }

    public String getTipoEmail() {
        if (daticertXML.getReturnValue() == ReturnValues.VALID)
            return daticertXML.getTipo();
        else
            return tipoEmail;
    }

    public void setTipoEmail(String tipoEmail) {
        this.tipoEmail = tipoEmail;
    }

    public String getMessaggioErrore() {
        if (daticertXML.getReturnValue() == ReturnValues.VALID)
            return daticertXML.getErrore();
        else
            return messaggioErrore;
    }

    public void setMessaggioErrore(String messaggioErrore) {
        this.messaggioErrore = messaggioErrore;
    }

    public PecDaticertVO getDaticertXML() {
        return daticertXML;
    }

    public void setDaticertXML(PecDaticertVO daticertXML) {
        this.daticertXML = daticertXML;
    }

}
