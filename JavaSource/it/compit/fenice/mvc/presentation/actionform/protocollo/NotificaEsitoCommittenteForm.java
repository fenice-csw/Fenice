package it.compit.fenice.mvc.presentation.actionform.protocollo;

import it.finsiel.siged.mvc.presentation.actionform.UploaderForm;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NotificaEsitoCommittenteForm extends UploaderForm {

	private static final long serialVersionUID = 1L;

	public enum EsitoFatturaEnum {

		EC01("Accettazione"),
		EC02("Rifiuto");

		private EsitoFatturaEnum(String descrizione) {
			this.descrizione = descrizione;
		}

		private String descrizione;

		public String getDescrizione() {
			return descrizione;
		}
		
		public String getCodice() {
			return name();
		}

	}

	private int protocolloId;

	private int numero;

	private String esito;

	private String identificativoSDI;
	
	private String idCommittente;

	private String descrizione;
	
	private String numeroFattura;

	private String anno;

	private Integer documentoId;

	private DocumentoVO documentoPrincipale = new DocumentoVO();
	
	private DocumentoVO notifica = new DocumentoVO();
	
	private String progressivo;

	private String autore;
	
    private List<SoggettoVO> destinatari = new ArrayList<SoggettoVO>();

	private AllaccioVO allaccio = new AllaccioVO();

	public int getProtocolloId() {
		return protocolloId;
	}

	public void setProtocolloId(int protocolloId) {
		this.protocolloId = protocolloId;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getIdentificativoSDI() {
		return identificativoSDI;
	}

	public void setIdentificativoSDI(String identificativoSDI) {
		this.identificativoSDI = identificativoSDI;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String oggetto) {
		this.descrizione = oggetto;
	}

	public Integer getDocumentoId() {
		return documentoId;
	}

	public void setDocumentoId(Integer documentoId) {
		this.documentoId = documentoId;
	}

	public DocumentoVO getDocumentoPrincipale() {
		return documentoPrincipale;
	}

	public void setDocumentoPrincipale(DocumentoVO documentoPrincipale) {
		this.documentoPrincipale = documentoPrincipale;
	}

	public String getAutore() {
		return autore;
	}

	public void setAutore(String autore) {
		this.autore = autore;
	}

	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}
	
	public String getIdCommittente() {
		return idCommittente;
	}

	public void setIdCommittente(String idCommittente) {
		this.idCommittente = idCommittente;
	}

	public String getNumeroFattura() {
		return numeroFattura;
	}

	public void setNumeroFattura(String numeroFattura) {
		this.numeroFattura = numeroFattura;
	}
	
	public List<EsitoFatturaEnum> getEsiti(){
		return Arrays.asList(EsitoFatturaEnum.values());
	}
	
	public String getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(String progressivo) {
		this.progressivo = progressivo;
	}

	public DocumentoVO getNotifica() {
		return notifica;
	}

	public AllaccioVO getAllaccio() {
		return allaccio;
	}

	public void setAllaccio(AllaccioVO allaccio) {
		this.allaccio = allaccio;
	}

	public void setNotifica(DocumentoVO notifica) {
		this.notifica = notifica;
	}

	public List<SoggettoVO> getDestinatari() {
		return destinatari;
	}

	public void setDestinatari(List<SoggettoVO> destinatari) {
		this.destinatari = destinatari;
	}

	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}
	
	

}