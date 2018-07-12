package it.compit.fenice.mvc.presentation.helper;

import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.util.DateUtil;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.NumberFormat;
import java.util.Locale;

public class DocumentoAmmTrasparenteView implements Comparable<DocumentoAmmTrasparenteView>{

	private int docSezioneId;

	private String oggetto;

	private String capitolo;

	private BigDecimal importo;
	
	private String note;
	
	private Date dataValiditaInizio;

	private Date dataValiditaFine;

	private int ufficioId;

	private int numeroDocumentoSezione;
	
	private Date dataDocumento;
	
	private String numeroDocumento;
	
	private String descrizione;
	
	private String settoreProponente;
	
	private int stato;

	private boolean pubblicato;
	
	private boolean protocollato;
	

	public DocumentoAmmTrasparenteView() {
		
	}
	
	public int getStato() {
		return stato;
	}

	public void setStato(int stato) {
		this.stato = stato;
	}

	public boolean isPubblicato() {
		return pubblicato;
	}

	public void setPubblicato(boolean pubblicato) {
		this.pubblicato = pubblicato;
	}

	public boolean isProtocollato() {
		return protocollato;
	}

	public void setProtocollato(boolean protocollato) {
		this.protocollato = protocollato;
	}
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}


	public Date getDataDocumento() {
		return dataDocumento;
	}

	public String getAnnoNumero() {
		return DateUtil.getYear(dataDocumento)+"-"+numeroDocumentoSezione;
	}
	
	public String getOrderedAnnoNumero() {
		long ran = getAnno() * 100000000l + Integer.valueOf(numeroDocumentoSezione);
		return String.valueOf(ran);
	}
	
	public int getAnno() {
		return DateUtil.getYear(dataDocumento);
	}
	
	public void setDataDocumento(Date dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	public int getNumeroDocumentoSezione() {
		return numeroDocumentoSezione;
	}

	public void setNumeroDocumentoSezione(int numeroDocumentoSezione) {
		this.numeroDocumentoSezione = numeroDocumentoSezione;
	}

	
	public int getDocSezioneId() {
		return docSezioneId;
	}

	public void setDocSezioneId(int sezioneId) {
		this.docSezioneId = sezioneId;
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public String getCapitolo() {
		return capitolo;
	}

	public void setCapitolo(String capitolo) {
		this.capitolo = capitolo;
	}

	public Date getDataValiditaInizio() {
		return dataValiditaInizio;
	}

	public void setDataValiditaInizio(Date dataValiditaInizio) {
		this.dataValiditaInizio = dataValiditaInizio;
	}

	public Date getDataValiditaFine() {
		return dataValiditaFine;
	}

	public void setDataValiditaFine(Date dataValiditaFine) {
		this.dataValiditaFine = dataValiditaFine;
	}

	public int getUfficioId() {
		return ufficioId;
	}

	public void setUfficioId(int ufficioId) {
		this.ufficioId = ufficioId;
	}

	public String getUfficio() {
		if (ufficioId != 0) {
			Organizzazione org = Organizzazione.getInstance();
			return org.getUfficio(ufficioId).getValueObject().getDescription();
		} else
			return null;
	}
	
	public String getValidita() {
		return "dal "+dataValiditaInizio+" al "+dataValiditaFine;
	}

	public String getNumero() {
		return String.valueOf(numeroDocumentoSezione);
	}
	
	public BigDecimal getImporto() {
		return importo;
	}

	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	
	public String getImportoEuro() {
		Locale locale = new Locale("it", "IT");      
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
		return currencyFormatter.format(this.importo);
	}
	
	public String getResponsabile() {
		if (ufficioId != 0) {
			Organizzazione org = Organizzazione.getInstance();
			return org.getUfficio(ufficioId).getValueObject().getDescription();
		} else
			return null;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	
	public String getSettoreProponente() {
		return settoreProponente;
	}

	public void setSettoreProponente(String settoreProponente) {
		this.settoreProponente = settoreProponente;
	}

	public int compareTo(DocumentoAmmTrasparenteView d) {
		return getOrderedAnnoNumero().compareTo(d.getOrderedAnnoNumero());
		
	}
	
}
