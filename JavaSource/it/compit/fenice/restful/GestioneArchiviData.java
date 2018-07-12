package it.compit.fenice.restful;

import java.io.Serializable;


public class GestioneArchiviData implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String descrizione;
	private String nomeFileZip;
	private String nomeFileCsv;
	private String tipoDoc;
	private byte[] fileCsv;
	private byte[] fileZip;
	
	
	public GestioneArchiviData(String descrizione, String nomeFileCsv, String nomeFileZip,
			 byte[] fileCsv, byte[] fileZip, String tipoDoc) {
		super();
		this.descrizione = descrizione;
		this.nomeFileZip = nomeFileZip;
		this.nomeFileCsv = nomeFileCsv;
		this.tipoDoc = tipoDoc;
		this.fileCsv = fileCsv;
		this.fileZip = fileZip;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getNomeFileZip() {
		return nomeFileZip;
	}
	public void setNomeFileZip(String nomeFileZip) {
		this.nomeFileZip = nomeFileZip;
	}
	public String getNomeFileCsv() {
		return nomeFileCsv;
	}
	public void setNomeFileCsv(String nomeFileCsv) {
		this.nomeFileCsv = nomeFileCsv;
	}
	public String getTipoDoc() {
		return tipoDoc;
	}
	public void setTipoDoc(String tipoDoc) {
		this.tipoDoc = tipoDoc;
	}
	public byte[] getFileCsv() {
		return fileCsv;
	}
	public void setFileCsv(byte[] fileCsv) {
		this.fileCsv = fileCsv;
	}
	public byte[] getFileZip() {
		return fileZip;
	}
	public void setFileZip(byte[] fileZip) {
		this.fileZip = fileZip;
	}
	
	
	
	
	
}
