package it.compit.fenice.mvc.presentation.actionform.repertori;

import it.compit.fenice.enums.UnitaAmministrativaEnum;
import it.compit.fenice.mvc.bo.RepertorioBO;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.mvc.presentation.actionform.UploaderForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class DocumentoRepertorioForm extends UploaderForm {

	private static final long serialVersionUID = 1L;

	private int docRepertorioId;

	private int repertorioId;

	private String oggetto;

	private String capitolo;

	private String importo;

	private AssegnatarioView ufficioResponsabile;

	private UfficioVO ufficioCorrente;

	private Collection<UfficioVO> ufficiDipendenti;

	private int ufficioCorrenteId;

	private int ufficioPrecedenteId;

	private String ufficioCorrentePath;

	private int ufficioSelezionatoId;

	private String note;

	private String dataRepertorio;

	private String dataValiditaInizio;

	private String dataValiditaFine;

	private Map<String,DocumentoVO>  documentiAllegati = new HashMap<String,DocumentoVO> (2);
	
	//private Map<String,DocumentoVO>  documentiAllegatiTimbrati = new HashMap<String,DocumentoVO> (2);

	private String[] documentiAllegatiId;

	private boolean riservato;
	
	private String numeroDocumentoRepertorio;
	
	private String descrizione;
	
	private String numeroDocumento;
	
	private boolean tipoAllegato;
	
	private boolean principale;
	
	private String settoreProponente;
	
	private UfficioVO settoreCorrente;

	private Collection<UfficioVO> ufficiSettoriDipendenti;

	private int settoreCorrenteId;

	private int settorePrecedenteId;

	private int settoreSelezionatoId;
	
	private String settoreCorrentePath;
	
	private boolean btnProtocollaVisibile;
	
	private boolean btnPubblicaVisibile;
	
	private boolean documentoPrincipalePresent;
	
	private int protocolloId;

	private int flagStato;
	
	private String[] documentiPubblicabili;
	
	public boolean isDocumentoPubblicabile(DocumentoVO doc) {
		//QUANDO UN SOLO ELEMENTO NON FUNZIONA
		if(this.documentiPubblicabili!=null && this.documentiPubblicabili.length!=0)
			for (String pubbl : this.documentiPubblicabili) {
				if (pubbl.equals(String.valueOf(doc.getIdx()))) {
					return true;
				}
			}
		return false;
	}
	
	public String[] getDocumentiPubblicabili() {
		return documentiPubblicabili;
	}

	public void setDocumentiPubblicabili(String[] documentiPubblicabili) {
		this.documentiPubblicabili = documentiPubblicabili;
	}
	
	public void resetDocumentiPubblicabili() { 
		documentiPubblicabili = new String[0]; 
	} 

	public int getFlagStato() {
		return flagStato;
	}

	public void setFlagStato(int flagStato) {
		this.flagStato = flagStato;
	}

	public boolean isDocumentoPrincipalePresent() {
		return documentoPrincipalePresent;
	}

	public void setDocumentoPrincipalePresent(boolean documentoPrincipalePresent) {
		this.documentoPrincipalePresent = documentoPrincipalePresent;
	}

	public boolean isBtnProtocollaVisibile() {
		return btnProtocollaVisibile;
	}

	public void setBtnProtocollaVisibile(boolean btnProtocollaVisibile) {
		this.btnProtocollaVisibile = btnProtocollaVisibile;
	}

	public boolean isPrincipale() {
		return principale;
	}

	public void setPrincipale(boolean principale) {
		this.principale = principale;
	}
	
	
	public String getNumeroDocumentoRepertorio() {
		return numeroDocumentoRepertorio;
	}

	public void setNumeroDocumentoRepertorio(String numeroDocumentoRepertorio) {
		this.numeroDocumentoRepertorio = numeroDocumentoRepertorio;
	}
	
	public boolean getRiservato() {
		return riservato;
	}

	public void setRiservato(boolean riservato) {
		this.riservato = riservato;
	}

	public int getDocRepertorioId() {
		return docRepertorioId;
	}

	public void setDocRepertorioId(int docRepertorioId) {
		this.docRepertorioId = docRepertorioId;
	}

	public void setImporto(String importo) {
		this.importo = importo;
	}

	public int getRepertorioId() {
		return repertorioId;
	}

	public String getImporto() {
		return importo;
	}

	public void setRepertorioId(int repertorioId) {
		this.repertorioId = repertorioId;
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

	public String getDataRepertorio() {
		return dataRepertorio;
	}

	public void setDataRepertorio(String dataRepertorio) {
		this.dataRepertorio = dataRepertorio;
	}

	public String getDataValiditaInizio() {
		return dataValiditaInizio;
	}

	public void setDataValiditaInizio(String dataValiditaInizio) {
		this.dataValiditaInizio = dataValiditaInizio;
	}

	public String getDataValiditaFine() {
		return dataValiditaFine;
	}

	public void setDataValiditaFine(String dataValiditaFine) {
		this.dataValiditaFine = dataValiditaFine;
	}

	public String[] getDocumentiAllegatiId() {
		return documentiAllegatiId;
	}

	public void setDocumentiAllegatiId(String[] documentiAllegatiId) {
		this.documentiAllegatiId = documentiAllegatiId;
	}

	public Collection<DocumentoVO>  getDocumentiAllegatiCollection() {
		return documentiAllegati.values();
	}

	public void setDocumentiAllegati(Map<String,DocumentoVO>  documenti) {
		this.documentiAllegati = documenti;
	}

	public Map<String,DocumentoVO>  getDocumentiAllegati() {
		return documentiAllegati;
	}

	public void allegaDocumento(DocumentoVO doc) {
		RepertorioBO.putAllegato(doc, this.documentiAllegati);
	}

	public void rimuoviAllegato(String allegatoId) {
		if(documentiAllegati.get(allegatoId).getPrincipale()==true)
			setDocumentoPrincipalePresent(false);
		documentiAllegati.remove(allegatoId);
	}
	
	public void resetAllegati() {
		documentiAllegati.clear();
	}

	public DocumentoVO getDocumentoAllegato(Object key) {
		return (DocumentoVO) this.documentiAllegati.get(key);
	}

	public DocumentoVO getDocumentoAllegato(int idx) {
		return (DocumentoVO) this.documentiAllegati.get(String.valueOf(idx));
	}

	public String[] getAllegatiSelezionatiId() {
		return documentiAllegatiId;
	}

	public void setAllegatiSelezionatiId(String[] allegatiSelezionatoId) {
		this.documentiAllegatiId = allegatiSelezionatoId;
	}

	public Collection<UfficioVO> getUfficiDipendenti() {
		return ufficiDipendenti;
	}

	public void setUfficiDipendenti(Collection<UfficioVO> ufficiDipendenti) {
		this.ufficiDipendenti = ufficiDipendenti;
	}

	public int getUfficioCorrenteId() {
		return ufficioCorrenteId;
	}

	public void setUfficioCorrenteId(int ufficioCorrenteId) {
		this.ufficioCorrenteId = ufficioCorrenteId;
	}

	public int getUfficioSelezionatoId() {
		return ufficioSelezionatoId;
	}

	public void setUfficioSelezionatoId(int ufficioCorrenteId) {
		this.ufficioSelezionatoId = ufficioCorrenteId;
	}

	public UfficioVO getUfficioCorrente() {
		return ufficioCorrente;
	}

	public void setUfficioCorrente(UfficioVO ufficioCorrente) {
		this.ufficioCorrente = ufficioCorrente;
	}

	public String getUfficioCorrentePath() {
		return ufficioCorrentePath;
	}

	public void setUfficioCorrentePath(String ufficioCorrentePath) {
		this.ufficioCorrentePath = ufficioCorrentePath;
	}

	public AssegnatarioView getUfficioResponsabile() {
		return ufficioResponsabile;
	}

	public void setUfficioResponsabile(AssegnatarioView responsabile) {
		this.ufficioResponsabile = responsabile;
	}

	public int getUfficioPrecedenteId() {
		return ufficioPrecedenteId;
	}

	public void setUfficioPrecedenteId(int ufficioPrecedenteId) {
		this.ufficioPrecedenteId = ufficioPrecedenteId;
	}

	
	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	public boolean isTipoAllegato() {
		return tipoAllegato;
	}

	public void setTipoAllegato(boolean tipoAllegato) {
		this.tipoAllegato = tipoAllegato;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	
	public String getSettoreProponente() {
		return settoreProponente;
	}

	public void setSettoreProponente(String settoreProponente) {
		this.settoreProponente = settoreProponente;
	}

	public UfficioVO getSettoreCorrente() {
		return settoreCorrente;
	}

	public void setSettoreCorrente(UfficioVO settoreCorrente) {
		this.settoreCorrente = settoreCorrente;
	}

	public Collection<UfficioVO> getUfficiSettoriDipendenti() {
		return ufficiSettoriDipendenti;
	}

	public void setUfficiSettoriDipendenti(
			Collection<UfficioVO> ufficiSettoriDipendenti) {
		this.ufficiSettoriDipendenti = ufficiSettoriDipendenti;
	}

	public int getSettoreCorrenteId() {
		return settoreCorrenteId;
	}

	public void setSettoreCorrenteId(int settoreCorrenteId) {
		this.settoreCorrenteId = settoreCorrenteId;
	}

	public int getSettorePrecedenteId() {
		return settorePrecedenteId;
	}

	public void setSettorePrecedenteId(int settorePrecedenteId) {
		this.settorePrecedenteId = settorePrecedenteId;
	}

	public int getSettoreSelezionatoId() {
		return settoreSelezionatoId;
	}

	public void setSettoreSelezionatoId(int settoreSelezionatoId) {
		this.settoreSelezionatoId = settoreSelezionatoId;
	}
	
	public String getSettoreCorrentePath() {
		return settoreCorrentePath;
	}

	public void setSettoreCorrentePath(String settoreCorrentePath) {
		this.settoreCorrentePath = settoreCorrentePath;
	}
	
	public UnitaAmministrativaEnum getUnitaAmministrativa(){
		return Organizzazione.getInstance().getValueObject().getUnitaAmministrativa();
	}

	public int getProtocolloId() {
		return protocolloId;
	}

	public void setProtocolloId(int protocolloId) {
		this.protocolloId = protocolloId;
	}
	
	/*
	public Collection<DocumentoVO>  getDocumentiAllegatiTimbratiCollection() {
		return documentiAllegatiTimbrati.values();
	}

	public void setDocumentiAllegatiTimbrati(Map<String,DocumentoVO>  documenti) {
		this.documentiAllegatiTimbrati = documenti;
	}

	public Map<String,DocumentoVO>  getDocumentiAllegatiTimbrati() {
		return documentiAllegatiTimbrati;
	}

	public void allegaDocumentoTimbrato(DocumentoVO doc) {
		RepertorioBO.putAllegato(doc, this.documentiAllegatiTimbrati);
	}

	public void rimuoviAllegatoTimbrato(String allegatoId) {
		documentiAllegatiTimbrati.remove(allegatoId);
	}
	
	public void resetAllegatiTimbrati() {
		documentiAllegatiTimbrati.clear();
	}

	public DocumentoVO getDocumentoAllegatoTimbrato(Object key) {
		return (DocumentoVO) this.documentiAllegatiTimbrati.get(key);
	}

	public DocumentoVO getDocumentoAllegatoTimbrato(int idx) {
		return (DocumentoVO) this.documentiAllegatiTimbrati.get(String.valueOf(idx));
	}
 	*/

	public void inizializza() {
		this.docRepertorioId = 0;
		this.numeroDocumentoRepertorio=null;
		this.numeroDocumento=null;
		this.oggetto = null;
		this.capitolo = null;
		this.importo = null;
		this.descrizione = null;
		this.ufficioCorrenteId = 0;
		this.ufficioCorrentePath = null;
		this.ufficioSelezionatoId = 0;
		this.note = null;
		setDataRepertorio(null);
		setDataValiditaFine(null);
		setDataValiditaInizio(null);
		setAllegatiSelezionatiId(null);
		documentiAllegati.clear();
		//documentiAllegatiTimbrati.clear();
		this.settoreProponente = null;
		this.settoreCorrenteId = 0;
		this.settoreSelezionatoId = 0;
		this.settoreCorrentePath=null;
		this.setBtnProtocollaVisibile(false);
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {

		ActionErrors errors = new ActionErrors();

		if (request.getParameter("btnConferma") != null) {
			String dataDoc = getDataRepertorio();
			String dataInizio = getDataValiditaInizio();
			String dataFine = getDataValiditaFine();
			if (getOggetto() == null || "".equals(getOggetto().trim())) {
				errors.add("oggetto", new ActionMessage("campo.obbligatorio",
						"Oggetto", ""));
			}
			if (getNumeroDocumentoRepertorio() == null || "".equals(getNumeroDocumentoRepertorio().trim())) {
				errors.add("oggetto", new ActionMessage("campo.obbligatorio",
						"Numero Documento Repertorio", ""));
			}
			
			if ((dataFine == null || "".equals(dataFine.trim()))|| dataInizio == null || "".equals(dataInizio.trim())) {
				if(dataFine == null || "".equals(dataFine.trim()))
					errors.add("Data validita' da", new ActionMessage("campo.obbligatorio", "Data validita' da", ""));
				if(dataInizio == null || "".equals(dataInizio.trim()))
					errors.add("Data validita' a", new ActionMessage("campo.obbligatorio", "Data validita' a", ""));
			} else if (!DateUtil.isData(dataFine)|| !DateUtil.isData(dataInizio)) {
				if(!DateUtil.isData(dataFine))
					errors.add("dataFine", new ActionMessage("formato.data.errato", "Data validita' a"));
				if(!DateUtil.isData(dataInizio))
					errors.add("dataFine", new ActionMessage("formato.data.errato", "Data validita' da"));
			} else if (DateUtil.toDate(dataFine).before(
					DateUtil.toDate(dataInizio))) {
				errors.add("dataRegistrazione", new ActionMessage("data_fine.minore.data_inizio"));
			}

			if (dataDoc != null && !"".equals(dataDoc.trim())){
				if (!DateUtil.isData(dataDoc)) {
					errors.add("dataDocumento", new ActionMessage("formato.data.errato", "Data"));
				}
			}else
				errors.add("dataDoc", new ActionMessage("campo.obbligatorio","Data", ""));
			
			if (getImporto() != null && !"".equals(getImporto().trim()))
				if (!NumberUtil.isDouble(getImporto())) {
					errors.add("Importo", new ActionMessage("formato.importo.errato", "Importo"));
				}

			if (getNumeroDocumentoRepertorio() != null && !"".equals(getNumeroDocumentoRepertorio().trim()))
				if (!NumberUtil.isInteger(getNumeroDocumentoRepertorio())) {
					errors.add("Numero", new ActionMessage("formato.numerico.errato", "Importo"));
				}
			
		} else if (request.getParameter("downloadAllegatoId") != null) {
			if (!NumberUtil.isInteger(request
					.getParameter("downloadAllegatoId"))) {
				errors.add("downloadAllegatoId", new ActionMessage("formato.numerico.errato", "Identificativo Documento"));
			}

		} else if(request.getParameter("allegaDocumentoAction") != null){
			if(isPrincipale() && isDocumentoPrincipalePresent()){
				errors.add("Principale", new ActionMessage("repertorio.documento.principale.esistente", "Principale"));
				setPrincipale(false);
			}
		}
		return errors;
	}

	public boolean isBtnPubblicaVisibile() {
		return btnPubblicaVisibile;
	}

	public void setBtnPubblicaVisibile(boolean btnPubblicaVisibile) {
		this.btnPubblicaVisibile = btnPubblicaVisibile;
	}
	
}
