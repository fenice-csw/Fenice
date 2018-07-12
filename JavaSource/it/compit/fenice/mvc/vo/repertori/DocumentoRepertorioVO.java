package it.compit.fenice.mvc.vo.repertori;

import it.compit.fenice.mvc.bo.RepertorioBO;
import it.finsiel.siged.mvc.vo.VersioneVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DocumentoRepertorioVO extends VersioneVO {

	private static final long serialVersionUID = 1L;
	
	public final static Integer REGISTRATO = 0;

	public final static Integer DA_REPERTORIALE = 1;

	public final static Integer PUBBLICATO = 2;
	
	public final static Integer PROTOCOLLATO = 3;
	
	public final static Integer PUBBLICATO_PROTOCOLLATO = 4;


	private int docRepertorioId;
	
	private int repId;
	
	private String oggetto;

	private String capitolo;

	private String note;

	private Date dataValiditaInizio;

	private Date dataValiditaFine;

	private Date dataRepertorio;

	private BigDecimal importo;

	private int ufficioId;

	private Map<String,DocumentoVO> documenti = new HashMap<String,DocumentoVO>(2);
	
	private int numeroDocumentoRepertorio;
	
	private int flagStato;
	
	private String numeroDocumento;
	
	private String descrizione;
	
	private String settoreProponente;
	
	private int protocolloId;
	
	public DocumentoRepertorioVO() {
		
	}

	public int getFlagStato() {
		return flagStato;
	}

	public void setFlagStato(int flagStato) {
		this.flagStato = flagStato;
	}

	public int getNumeroDocumentoRepertorio() {
		return numeroDocumentoRepertorio;
	}

	public void setNumeroDocumentoRepertorio(int numeroDocumentoRepertorio) {
		this.numeroDocumentoRepertorio = numeroDocumentoRepertorio;
	}

	
	public int getDocRepertorioId() {
		return docRepertorioId;
	}

	public void setDocRepertorioId(int repertorioId) {
		this.docRepertorioId = repertorioId;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

	public Date getDataRepertorio() {
		return dataRepertorio;
	}

	public void setDataRepertorio(Date dataRepertorio) {
		this.dataRepertorio = dataRepertorio;
	}

	public BigDecimal getImporto() {
		return importo;
	}

	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	public int getUfficioId() {
		return ufficioId;
	}

	public void setUfficioId(int ufficioId) {
		this.ufficioId = ufficioId;
	}

	public DocumentoVO getDocumento(Object key) {
		return (DocumentoVO) this.documenti.get(key);
	}

	public DocumentoVO getDocumento(int idx) {
		return (DocumentoVO) this.documenti.get(String
				.valueOf(idx));
	}

	public Map<String,DocumentoVO> getDocumenti() {
		return documenti;
	}

    public Collection<DocumentoVO> getDocumentiCollection() {
        return documenti.values();
    }

	public void setDocumenti(Map<String,DocumentoVO> documenti) {
		this.documenti = documenti;
	}

	public int getRepId() {
		return repId;
	}

	public void setRepId(int aooId) {
		this.repId = aooId;
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

	public String getKey() {
		return "DOCREP_"+getDocRepertorioId();
	}

	public String getSettoreProponente() {
		return settoreProponente;
	}

	public void setSettoreProponente(String settoreProponente) {
		this.settoreProponente = settoreProponente;
	}
	
    public void allegaDocumento(DocumentoVO documento) {
        if (documento != null) {
            RepertorioBO.putAllegato(documento, documenti);
        }
    }

	public int getProtocolloId() {
		return protocolloId;
	}

	public void setProtocolloId(int protocolloId) {
		this.protocolloId = protocolloId;
	}
	
}
