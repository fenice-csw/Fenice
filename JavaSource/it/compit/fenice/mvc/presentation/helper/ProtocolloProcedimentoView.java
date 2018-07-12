package it.compit.fenice.mvc.presentation.helper;

import it.finsiel.siged.util.StringUtil;

public class ProtocolloProcedimentoView {

	private int protocolloId;
	
	private int procedimentoId;

	private int documentoId;
	
	private int numeroProtocollo;

	private int annoProtocollo;
	
	private String numeroProcedimento;

	private int annoProcedimento;
	
	private String dataScadenza;

	private String oggetto;

	private String motivazioni;

	private boolean pdf;

	private int sospeso;

	public int getSospeso() {
		return sospeso;
	}

	public void setSospeso(int sospeso) {
		this.sospeso = sospeso;
	}

	public final static String PDF_SI = "SI";

	
	public String getPdf() {
		return this.pdf ? PDF_SI : null;
	}
	
	public String getAnnoNumeroProtocollo() {

		if (numeroProtocollo != 0 && annoProtocollo != 0)
			return StringUtil.formattaNumeroProtocollo(String
					.valueOf(numeroProtocollo), 7);
		else
			return "";
	}
	
	public int getDocumentoId() {
		return documentoId;
	}

	public void setDocumentoId(int documentoId) {
		this.documentoId = documentoId;
	}

	public int getProtocolloId() {
		return protocolloId;
	}

	public int getProcedimentoId() {
		return procedimentoId;
	}

	public int getNumeroProtocollo() {
		return numeroProtocollo;
	}

	public int getAnnoProtocollo() {
		return annoProtocollo;
	}

	public String getNumeroProcedimento() {
		return numeroProcedimento;
	}

	public int getAnnoProcedimento() {
		return annoProcedimento;
	}

	public String getDataScadenza() {
		return dataScadenza;
	}

	public String getOggetto() {
		return oggetto;
	}

	public String getMotivazioni() {
		return motivazioni;
	}

	public void setProtocolloId(int protocolloId) {
		this.protocolloId = protocolloId;
	}

	public void setProcedimentoId(int procedimentoId) {
		this.procedimentoId = procedimentoId;
	}

	public void setNumeroProtocollo(int numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}

	public void setAnnoProtocollo(int annoProtocollo) {
		this.annoProtocollo = annoProtocollo;
	}

	public void setNumeroProcedimento(String numeroProcedimento) {
		this.numeroProcedimento = numeroProcedimento;
	}

	public void setAnnoProcedimento(int annoProcedimento) {
		this.annoProcedimento = annoProcedimento;
	}

	public void setDataScadenza(String dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public void setMotivazioni(String motivazioni) {
		this.motivazioni = motivazioni;
	}

	public void setPdf(boolean pdf) {
		this.pdf = pdf;
	}

}
