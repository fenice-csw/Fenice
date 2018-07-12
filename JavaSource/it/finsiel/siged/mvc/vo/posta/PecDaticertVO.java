/*
 * Created on 13-mag-2005
 *
 */
package it.finsiel.siged.mvc.vo.posta;

import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.mvc.vo.BaseVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * @author Almaviva sud
 * 
 */
public class PecDaticertVO extends BaseVO {

    private String tipo;

    private String mittente;

    private ArrayList destinatari = new ArrayList();

    private String risposte;

    private String oggetto;

    private String gestoreEmittente;

    private Date data;

    private String errore;

    private String identificativo;

    public PecDaticertVO() {

    }

    public boolean isValid() {
        return ReturnValues.VALID == getReturnValue();
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void addDestinatario(PecDestVO d) {
        destinatari.add(d);
    }

    public ArrayList getDestinatari() {
        return destinatari;
    }

    public void setDestinatari(ArrayList destinatari) {
        this.destinatari = destinatari;
    }

    public String getDestinatariAsString() {
        StringBuffer sb = new StringBuffer();
        Iterator it = destinatari.iterator();
        while (it.hasNext()) {
            PecDestVO d = (PecDestVO) it.next();
            sb.append(" "
                    + (d.getNominativo() == null ? "" : d.getNominativo())
                    + " [" + d.getEmail() + "] ");
            if (it.hasNext())
                sb.append(",");
        }
        return sb.toString();
    }

    public String getGestoreEmittente() {
        return gestoreEmittente;
    }

    public void setGestoreEmittente(String gestoreEmittente) {
        this.gestoreEmittente = gestoreEmittente;
    }

    public String getIdentificativo() {
        return identificativo;
    }

    public void setIdentificativo(String identificativo) {
        this.identificativo = identificativo;
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

    public String getRisposte() {
        return risposte;
    }

    public void setRisposte(String risposte) {
        this.risposte = risposte;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getErrore() {
        return errore;
    }

    public void setErrore(String errore) {
        this.errore = errore;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PecDaticertVO [data=" + data + ", destinatari=" + getDestinatariAsString()
				+ ", errore=" + errore + ", gestoreEmittente="
				+ gestoreEmittente + ", identificativo=" + identificativo
				+ ", mittente=" + mittente + ", oggetto=" + oggetto
				+ ", risposte=" + risposte + ", tipo=" + tipo + "]";
	}
    
    
    
}
