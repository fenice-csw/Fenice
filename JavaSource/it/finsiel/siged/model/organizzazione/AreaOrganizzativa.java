package it.finsiel.siged.model.organizzazione;

import it.finsiel.siged.mvc.vo.organizzazione.AreaOrganizzativaVO;
import it.finsiel.siged.mvc.vo.organizzazione.MailConfigVO;

public class AreaOrganizzativa {
    
	AreaOrganizzativaVO valueObject;

    private Organizzazione amministrazione;

    private Ufficio ufficioCentrale;

    private int utenteResponsabileId;
    
    MailConfigVO mailConfig;
    
    public int getUtenteResponsabileId() {
		return utenteResponsabileId;
	}

	public void setUtenteResponsabileId(int utenteResponsabileId) {
		this.utenteResponsabileId = utenteResponsabileId;
	}

    public AreaOrganizzativa() {
    	valueObject=new AreaOrganizzativaVO();
    	mailConfig=new MailConfigVO();
    }

    public AreaOrganizzativaVO getValueObject() {
        return valueObject;
    }

    public void setValueObject(AreaOrganizzativaVO aooVO) {
        this.valueObject = aooVO;
    }

    public Organizzazione getAmministrazione() {
        return amministrazione;
    }

    void setAmministrazione(Organizzazione amministrazione) {
        this.amministrazione = amministrazione;
    }

    public Ufficio getUfficioCentrale() {
        return ufficioCentrale;
    }

    public void setUfficioCentrale(Ufficio ufficio) {
        this.ufficioCentrale = ufficio;
    }

	public MailConfigVO getMailConfig() {
		return mailConfig;
	}

	public void setMailConfig(MailConfigVO mailConfig) {
		this.mailConfig = mailConfig;
	}
	
    public String getTelFaxMail() {
		String tel= ((valueObject.getTelefono() == null || valueObject.getTelefono().trim().equals("") ) ?"":"Tel:"+valueObject.getTelefono()+"; ");
		String fax= ((valueObject.getFax() == null || valueObject.getFax().trim().equals("") ) ?"":"Fax:"+valueObject.getFax()+"; ");
		String mail="";
		if(mailConfig.getPecIndirizzo() != null && !mailConfig.getPecIndirizzo().trim().equals(""))
			mail= "PEC:"+mailConfig.getPecIndirizzo()+";";
		else if (mailConfig.getPnIndirizzo() != null && !mailConfig.getPnIndirizzo().trim().equals("") )
			mail= "Email:"+mailConfig.getPnIndirizzo()+";";
		return tel+fax+mail;
	}
    
    
}