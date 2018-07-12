package it.compit.fenice.mvc.presentation.helper;

import it.finsiel.siged.mvc.vo.posta.PecDestVO;

import java.util.Date;
import java.util.List;


public class CodaInvioView {

	
	private int mailId;

    private int protocolloId;
    
    private int numeroProtocollo;

    private int stato;
    
    private List<PecDestVO> destinatari;
    
    private Date dataCreazione;
    
    private Date dataInvio;
    
    private String oggettoProtocollo;
    
    private int annoRegistrazione;

    
	public CodaInvioView() {
	}

	public String getDescrizioneStato(){
		if(stato==0)
			return "Da Inviare";
		else if(stato==1)
			return "Inviata";
		else return "";
	}

	public int getMailId() {
		return mailId;
	}

	public void setMailId(int mailId) {
		this.mailId = mailId;
	}

	public int getProtocolloId() {
		return protocolloId;
	}

	public void setProtocolloId(int protocolloId) {
		this.protocolloId = protocolloId;
	}

	public int getStato() {
		return stato;
	}

	public void setStato(int stato) {
		this.stato = stato;
	}

	public  List<PecDestVO> getDestinatari() {
		return destinatari;
	}
	
	

	public void setDestinatari(List<PecDestVO> destinatari) {
		this.destinatari = destinatari;
	}

	public Date getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Date getDataInvio() {
		return dataInvio;
	}

	public void setDataInvio(Date dataInvio) {
		this.dataInvio = dataInvio;
	}

	public String getOggettoProtocollo() {
		return oggettoProtocollo;
	}

	public void setOggettoProtocollo(String oggettoProtocollo) {
		this.oggettoProtocollo = oggettoProtocollo;
	}

	public int getNumeroProtocollo() {
		return numeroProtocollo;
	}

	public void setNumeroProtocollo(int numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}

	public int getAnnoRegistrazione() {
		return annoRegistrazione;
	}

	public void setAnnoRegistrazione(int annoRegistrazione) {
		this.annoRegistrazione = annoRegistrazione;
	}
	
	public String getNumeroAnnoProtocollo(){
		return String.valueOf(this.numeroProtocollo)+"/"+String.valueOf(this.annoRegistrazione);
	}
	
}