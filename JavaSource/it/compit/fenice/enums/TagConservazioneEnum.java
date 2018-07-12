package it.compit.fenice.enums;

public enum TagConservazioneEnum {
	NOMEFILE("NomeFile"),
	CONSERVATORE("Conservatore"),
	ENTE("Ente"),
	CODICEENTE("CodiceEnte"),
	STRUTTURA("Struttura"), 
	USERID("UserID"),
	NUMERO("Numero"),
	ANNO("Anno"),
	TIPOLOGIAUNITADOCUMENTARIA("TipologiaUnitaDocumentaria"),
	CODICETIPOREGISTRO("CodiceTipoRegistro"),
	HASH("Hash"),
	CODIFICAHASH("CodificaHash"),
	OGGETTO("Oggetto"),
	DATA("Data"),
	TIPODOCUMENTO("TipoDocumento"),
	NUMEROINIZIALE("NumeroIniziale"),
	NUMEROFINALE("NumeroFinale"),
	DATAINIZIOREGISTRAZIONI("DataInizioRegistrazioni"),
	DATAFINEREGISTRAZIONI("DataFineRegistrazioni"),
	ORIGINATORE("Originatore"),
	RESPONSABILE("Responsabile"),
	OPERATORE("Operatore"),
	NUMERODOCUMENTIREGISTRATI("NumeroDocumentiRegistrati"),
	NUMERODOCUMENTIANNULLATI("NumeroDocumentiAnnullati"),
	DENOMINAZIONEAPPLICATIVO("DenominazioneApplicativo"),
	VERSIONEAPPLICATIVO("VersioneApplicativo"),
	PRODUTTOREAPPLICATIVO("ProduttoreApplicativo"),
	TEMPOCONSERVAZIONE("TempoConservazione");
	
	private TagConservazioneEnum(String tagName){
		this.tagName = tagName;
	}
	
	private String tagName;
	
	public String getTagName(){
		return tagName;
	};

}
