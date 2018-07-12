package it.finsiel.siged.mvc.presentation.helper;

import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.util.DateUtil;

import java.io.Serializable;
import java.util.Date;

public class ProcedimentoView implements Serializable,Comparable<ProcedimentoView>{


	private static final long serialVersionUID = 1L;
	
	private String posizione;
	private String descUfficioId;
	private Date dataProtocollo;
	private int procedimentoId;
	private String numeroProcedimento;
	private Date dataAvvio;
	private int tipoId;
	private int responsabileId;
	private int posizioneId;
	private int statoId;
	private String dataEvidenza;
	private String oggetto;
	private String note;
	private Date dataAnnullamento;
	private int protocolloId;
	private String numeroRifDoc;
	private String sottoCategoria;
	private int ufficioId;
	private int utenteId;
	private int servizioTitolarioId;
	private int categoriaTitolarioId;
	private int keySicurezza;
	private int versione;
	private int aooId;
	private int anno;
	private int numero;
	private int giorniRimanenti;
	private String interessato;
	private int documentoId;
	private boolean pdf;
	private boolean titolareProcedimento;
	private boolean documentoVisionato;
	private boolean riassegnato;
	public final static String STR_SI = "SI";
	
	
	public boolean isRiassegnato() {
		return riassegnato;
	}

	public void setRiassegnato(boolean riassegnato) {
		this.riassegnato = riassegnato;
	}

	public boolean isDocumentoVisionato() {
		return documentoVisionato;
	}

	public void setDocumentoVisionato(boolean documentoVisionato) {
		this.documentoVisionato = documentoVisionato;
	}

	public int getDocumentoId() {
		return documentoId;
	}

	public void setDocumentoId(int documentoId) {
		this.documentoId = documentoId;
	}
	
	public String getTitolareProcedimento() {
		return this.titolareProcedimento ? STR_SI : null;
	}

	public boolean isTitolareProcedimento() {
		return this.titolareProcedimento;
	}
	
	public void setTitolareProcedimento(boolean pdf) {
		this.titolareProcedimento = pdf;
	}
	
	public String getPdf() {
		return this.pdf ? STR_SI : null;
	}

	public void setPdf(boolean pdf) {
		this.pdf = pdf;
	}
	
	public String getInteressato() {
		return interessato;
	}

	public void setInteressato(String interessato) {
		this.interessato = interessato;
	}

	public String getGiorniRimanenti() {
		if (giorniRimanenti < 0)
			return "/";
		else
			return String.valueOf(giorniRimanenti);
	}

	public void setGiorniRimanenti(int giorniRimanenti) {
		this.giorniRimanenti = giorniRimanenti;
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

	public int getVersione() {
		return versione;
	}

	public void setVersione(int versione) {
		this.versione = versione;
	}

	public Date getDataAnnullamento() {
		return dataAnnullamento;
	}

	public void setDataAnnullamento(Date dataAnnullamento) {
		this.dataAnnullamento = dataAnnullamento;
	}

	public Date getDataAvvio() {
		return dataAvvio;
	}

	public String getDataAvvioView() {
		if (dataAvvio != null)
			return DateUtil.formattaData(dataAvvio.getTime());
		else
			return "";
	}

	public void setDataAvvio(Date dataAvvio) {
		this.dataAvvio = dataAvvio;
	}

	public String getDataEvidenza() {
		return dataEvidenza;
	}

	public void setDataEvidenza(String dataEvidenza) {
		this.dataEvidenza = dataEvidenza;
	}

	public Date getDataProtocollo() {
		return dataProtocollo;
	}

	public void setDataProtocollo(Date dataProtocollo) {
		this.dataProtocollo = dataProtocollo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNumeroRifDoc() {
		return numeroRifDoc;
	}

	public void setNumeroRifDoc(String numeroRifDoc) {
		this.numeroRifDoc = numeroRifDoc;
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public int getPosizioneId() {
		return posizioneId;
	}

	public void setPosizioneId(int posizioneId) {
		this.posizioneId = posizioneId;
	}

	public int getProtocolloId() {
		return protocolloId;
	}

	public void setProtocolloId(int protocolloId) {
		this.protocolloId = protocolloId;
	}

	public int getResponsabileId() {
		return responsabileId;
	}

	public void setResponsabileId(int responsabileId) {
		this.responsabileId = responsabileId;
	}

	public String getResponsabile() {
		if(responsabileId!=0){
			Organizzazione org=Organizzazione.getInstance();
			CaricaVO c=org.getCarica(responsabileId);
			return org.getUfficio(c.getUfficioId()).getPath()+org.getUtente(c.getUtenteId()).getValueObject().getFullName();
			
		}
		return null;
	}

	public String getSottoCategoria() {
		return sottoCategoria;
	}

	public void setSottoCategoria(String sottoCategoria) {
		this.sottoCategoria = sottoCategoria;
	}

	public int getStatoId() {
		return statoId;
	}

	public String getStato() {
		if(statoId==0)
			return "In trattazione";
		if(statoId==1)
			return "Chiuso";
		if(statoId==2)
			return "Archiviato";
		if(statoId==3)
			return "Fase Istruttoria";
		if(statoId==4)
			return "Fase Relatoria";
		if(statoId==5)
			return "Attendi Parere del consiglio";
		if(statoId==6)
			return "Prepara decreto del presidente";
		else
			return "/";
		
	}
	
	public void setStatoId(int statoId) {
		this.statoId = statoId;
	}

	public int getTipoId() {
		return tipoId;
	}

	public void setTipoId(int tipoId) {
		this.tipoId = tipoId;
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

	public int getCategoriaTitolarioId() {
		return categoriaTitolarioId;
	}

	public void setCategoriaTitolarioId(int categoriaTitolarioId) {
		this.categoriaTitolarioId = categoriaTitolarioId;
	}

	public int getServizioTitolarioId() {
		return servizioTitolarioId;
	}

	public void setServizioTitolarioId(int servizioTitolarioId) {
		this.servizioTitolarioId = servizioTitolarioId;
	}

	public int getKeySicurezza() {
		return keySicurezza;
	}

	public void setKeySicurezza(int keySicurezza) {
		this.keySicurezza = keySicurezza;
	}

	public int getProcedimentoId() {
		return procedimentoId;
	}

	public void setProcedimentoId(int procedimentoId) {
		this.procedimentoId = procedimentoId;
	}

	public String getNumeroProcedimento() {
		return numeroProcedimento;
	}

	public void setNumeroProcedimento(String numeroProcedimento) {
		this.numeroProcedimento = numeroProcedimento;
	}

	public String getDescUfficioId() {
		return descUfficioId;
	}

	public void setDescUfficioId(String descUfficioId) {
		this.descUfficioId = descUfficioId;
	}

	public int getAooId() {
		return aooId;
	}

	public void setAooId(int aooId) {
		this.aooId = aooId;
	}

	public String getPosizione() {
		return posizione;
	}

	public void setPosizione(String posizione) {
		this.posizione = posizione;
	}

	
	
	public int compareTo(ProcedimentoView p) {
	      return (procedimentoId < p.getProcedimentoId()) ? -1 : (procedimentoId == p.getProcedimentoId() ? 0 : 1);
	   }
	
	

}