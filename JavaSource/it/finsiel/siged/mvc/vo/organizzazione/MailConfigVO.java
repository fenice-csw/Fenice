package it.finsiel.siged.mvc.vo.organizzazione;

import it.finsiel.siged.mvc.vo.IdentityVO;

import java.util.Date;

public class MailConfigVO extends IdentityVO {

	private static final long serialVersionUID = 1L;

    private int aooId;

    private int mailTimer;
    
    private String pecIndirizzo;

    private String pecUsername;

    private String pecPwd;

    private boolean pecAbilitata = false;

    private String pecSslPort;

    private String pecPop3;

    private String pecSmtp;

    private String pecSmtpPort;
        
    private Date dataUltimaPecRicevuta;

    private String pnIndirizzo;

    private String pnUsername;

    private String pnPwd;

    private boolean pnSsl = false;

    private String pnSslPort;

    private String pnPop3;

    private String pnSmtp;
    
    private boolean pnAbilitata = false;
    
	private String precIndirizzoInvio;
	
	private String precIndirizzoRicezione;

    private String precUsername;

    private String precPwd;

    private String precSmtp;

	public MailConfigVO() {
	}

	public int getAooId() {
		return aooId;
	}

	public void setAooId(int aooId) {
		this.aooId = aooId;
	}

	public int getMailTimer() {
		return mailTimer;
	}

	public void setMailTimer(int mailTimer) {
		this.mailTimer = mailTimer;
	}

	public String getPecIndirizzo() {
		return pecIndirizzo;
	}

	public void setPecIndirizzo(String pecIndirizzo) {
		this.pecIndirizzo = pecIndirizzo;
	}

	public String getPecUsername() {
		return pecUsername;
	}

	public void setPecUsername(String pecUsername) {
		this.pecUsername = pecUsername;
	}

	public String getPecPwd() {
		return pecPwd;
	}

	public void setPecPwd(String pecPwd) {
		this.pecPwd = pecPwd;
	}

	public boolean isPecAbilitata() {
		return pecAbilitata;
	}

	public void setPecAbilitata(boolean pecAbilitata) {
		this.pecAbilitata = pecAbilitata;
	}

	public String getPecSslPort() {
		return pecSslPort;
	}

	public void setPecSslPort(String pecSslPort) {
		this.pecSslPort = pecSslPort;
	}

	public String getPecPop3() {
		return pecPop3;
	}

	public void setPecPop3(String pecPop3) {
		this.pecPop3 = pecPop3;
	}

	public String getPecSmtp() {
		return pecSmtp;
	}

	public void setPecSmtp(String pecSmtp) {
		this.pecSmtp = pecSmtp;
	}

	public String getPecSmtpPort() {
		return pecSmtpPort;
	}

	public void setPecSmtpPort(String pecSmtpPort) {
		this.pecSmtpPort = pecSmtpPort;
	}

	public Date getDataUltimaPecRicevuta() {
		return dataUltimaPecRicevuta;
	}

	public void setDataUltimaPecRicevuta(Date dataUltimaPecRicevuta) {
		this.dataUltimaPecRicevuta = dataUltimaPecRicevuta;
	}

	public String getPnIndirizzo() {
		return pnIndirizzo;
	}

	public void setPnIndirizzo(String pnIndirizzo) {
		this.pnIndirizzo = pnIndirizzo;
	}

	public String getPnUsername() {
		return pnUsername;
	}

	public void setPnUsername(String pnUsername) {
		this.pnUsername = pnUsername;
	}

	public String getPnPwd() {
		return pnPwd;
	}

	public void setPnPwd(String pnPwd) {
		this.pnPwd = pnPwd;
	}

	public boolean isPnSsl() {
		return pnSsl;
	}

	public void setPnSsl(boolean pnSsl) {
		this.pnSsl = pnSsl;
	}

	public String getPnSslPort() {
		return pnSslPort;
	}

	public void setPnSslPort(String pnSslPort) {
		this.pnSslPort = pnSslPort;
	}

	public String getPnPop3() {
		return pnPop3;
	}

	public void setPnPop3(String pnPop3) {
		this.pnPop3 = pnPop3;
	}

	public String getPnSmtp() {
		return pnSmtp;
	}

	public void setPnSmtp(String pnSmtp) {
		this.pnSmtp = pnSmtp;
	}

	public boolean isPnAbilitata() {
		return pnAbilitata;
	}

	public void setPnAbilitata(boolean pnAbilitata) {
		this.pnAbilitata = pnAbilitata;
	}

	public String getPrecIndirizzoInvio() {
		return precIndirizzoInvio;
	}

	public void setPrecIndirizzoInvio(String precIndirizzoInvio) {
		this.precIndirizzoInvio = precIndirizzoInvio;
	}

	public String getPrecIndirizzoRicezione() {
		return precIndirizzoRicezione;
	}

	public void setPrecIndirizzoRicezione(String precIndirizzoRicezione) {
		this.precIndirizzoRicezione = precIndirizzoRicezione;
	}

	public String getPrecUsername() {
		return precUsername;
	}

	public void setPrecUsername(String precUsername) {
		this.precUsername = precUsername;
	}

	public String getPrecPwd() {
		return precPwd;
	}

	public void setPrecPwd(String precPwd) {
		this.precPwd = precPwd;
	}

	public String getPrecSmtp() {
		return precSmtp;
	}

	public void setPrecSmtp(String precSmtp) {
		this.precSmtp = precSmtp;
	}

	public boolean isPrecConfigured() {
		if(precIndirizzoInvio!=null && precIndirizzoRicezione!=null
			&& precPwd!=null && precSmtp!=null && precUsername!=null
			&& !precIndirizzoInvio.equals("") && !precIndirizzoRicezione.equals("")
			&& !precPwd.equals("") && !precSmtp.equals("") && !precUsername.equals(""))
			return true;
		else
			return false;
	}
	
}