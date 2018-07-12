package it.finsiel.siged.mvc.presentation.actionform.documentale;

import it.compit.fenice.mvc.business.RepertorioDelegate;
import it.compit.fenice.mvc.vo.repertori.RepertorioVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.presentation.actionform.UploaderForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.AlberoUfficiUtentiForm;
import it.finsiel.siged.mvc.presentation.helper.DestinatarioView;
import it.finsiel.siged.mvc.presentation.helper.PermessoDocumentoView;
import it.finsiel.siged.mvc.vo.IdentityVO;
import it.finsiel.siged.mvc.vo.lookup.TipoDocumentoVO;
import it.finsiel.siged.mvc.vo.lookup.TitolarioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.SpedizioneVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;

public class DocumentoForm extends UploaderForm implements
		AlberoUfficiUtentiForm {

	private static final long serialVersionUID = 1L;

	private int aooId;

	private int documentoId;

	private int cartellaId;

	private int repositoryFileId;

	private String nomeFile;

	private String note;

	private String descrizione;

	private String descrizioneArgomento;

	private String statoDocumento;

	private String statoArchivio = Parametri.STATO_LAVORAZIONE;

	private int userLavId;

	private String usernameLav;

	private int ownerId;

	private String owner;

	private int versione;

	private String assegnatoDa;

	private String messaggio;

	// gestione uffici & utenti
	private int ufficioCorrenteId;

	private String ufficioCorrentePath;

	private int ufficioSelezionatoId;

	private int utenteSelezionatoId;

	private UfficioVO ufficioCorrente;

	private Collection ufficiDipendenti;

	private Collection<UtenteVO> utenti;

	private int tipoDocumentoId;

	private String dataDocumento;

	private String oggetto;

	private DocumentoVO documentoPrincipale;

	private TitolarioVO titolario;

	private int titolarioPrecedenteId;

	private int titolarioSelezionatoId;

	private Collection titolariFigli;

	private String[] fascicoloSelezionatoId;

	private boolean modificabile;

	private int cartellaCorrenteId;

	private Map<String,PermessoDocumentoView> permessi;

	private String[] permessiSelezionatiId;

	private int permessoCorrente = -1;

	private int utenteCorrenteId;

	// dati fascicolo
	private FascicoloVO fascicolo;

	private Collection fascicoli;

	private Map<Integer,FascicoloVO> fascicoliDocumento;

	private Map<Integer, DocumentoVO> documentiRimossi = new HashMap<Integer, DocumentoVO>(2);

	private int tipoPermessoSelezionato;

	// dati per la ricerca
	private String dataDocumentoDa;

	private String dataDocumentoA;

	private String testo;

	// oggetti utilizzati nella ricerca documenti
	private int documentoSelezionato;

	private Collection listaDocumenti;

	// DESTINATARIO
	private Map<String,DestinatarioView> destinatari = new HashMap<String,DestinatarioView>(2);

	private Map<String,Integer> destinatariIds = new HashMap<String,Integer>(2);

	private String tipoDestinatario = "F";

	private String destinatarioMezzoId;

	private String[] destinatarioSelezionatoId;

	private String nominativoDestinatario;

	private String citta;

	private int mezzoSpedizione;

	private String emailDestinatario;

	private String indirizzoDestinatario;

	private String dataSpedizione;

	private boolean flagConoscenza;

	private String capDestinatario;

	private Collection tipiDocumento = new ArrayList();

	private String msgPermesso;

	private boolean fromProcedimento;

	private boolean ricercaFullText;

	private Collection owners;

	private Utente utenteCorrente;

	private int idx;

	private String titoloDestinatario;

	private int mezzoSpedizioneId;

	private int titoloId;

	private int procedimentoId;

	private boolean indietroVisibile;

	private Collection repertori;

	private int repertorioId;

	public int getRepertorioId() {
		return repertorioId;
	}

	public void setRepertorioId(int repertorioId) {
		this.repertorioId = repertorioId;
	}

	public Collection getRepertori() {
		return repertori;
	}

	public void setRepertori(Collection repertori) {
		this.repertori = repertori;
	}

	public String getNomeRepertorio() {
		if (repertorioId != 0) {
			RepertorioVO rep = RepertorioDelegate.getInstance().getRepertorio(
					repertorioId);
			return (rep != null ? rep.getDescrizione() : "");
		}
		return "";
	}

	public boolean isIndietroVisibile() {
		return indietroVisibile;
	}

	public void setIndietroVisibile(boolean indietroVisibile) {
		this.indietroVisibile = indietroVisibile;
	}

	public int getProcedimentoId() {
		return procedimentoId;
	}

	public void setProcedimentoId(int procedimentoId) {
		this.procedimentoId = procedimentoId;
	}

	public int getTitoloId() {
		return titoloId;
	}

	public void setTitoloId(int titoloId) {
		this.titoloId = titoloId;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public Collection getOwners() {
		return owners;
	}

	public void setOwners(Collection owners) {
		this.owners = owners;
	}

	public boolean isFromProcedimento() {
		return fromProcedimento;
	}

	public void setFromProcedimento(boolean indietroVisibile) {
		this.fromProcedimento = indietroVisibile;
	}

	public DocumentoForm() {
		inizializzaForm();
	}

	public int getUtenteCorrenteId() {
		return utenteCorrenteId;
	}

	public void setUtenteCorrenteId(int utenteCorrenteId) {
		this.utenteCorrenteId = utenteCorrenteId;
	}

	public int getCartellaCorrenteId() {
		return cartellaCorrenteId;
	}

	public void setCartellaCorrenteId(int cartellaCorrenteId) {
		this.cartellaCorrenteId = cartellaCorrenteId;
	}

	public int getDocumentoId() {
		return documentoId;
	}

	public void setDocumentoId(int documentoId) {
		this.documentoId = documentoId;
	}

	public Collection getUfficiDipendenti() {
		return ufficiDipendenti;
	}

	public void setUfficiDipendenti(Collection ufficiDipendenti) {
		this.ufficiDipendenti = ufficiDipendenti;
	}

	public Collection<UtenteVO> getUtenti() {
		return utenti;
	}

	public UtenteVO getUtente(int utenteId) {
		for (Iterator<UtenteVO> i = getUtenti().iterator(); i.hasNext();) {
			UtenteVO ute = (UtenteVO) i.next();
			if (ute.getId().intValue() == utenteId) {
				return ute;
			}
		}
		return null;
	}

	public void setUtenti(Collection<UtenteVO> utenti) {
		this.utenti = utenti;
	}

	public int getUfficioCorrenteId() {
		return ufficioCorrenteId;
	}

	public void setUfficioCorrenteId(int ufficioCorrenteId) {
		this.ufficioCorrenteId = ufficioCorrenteId;
	}

	public String getTitoloDestinatario() {
		return titoloDestinatario;
	}

	/**
	 * @param titoloDestinatario
	 *            The titoloDestinatario to set.
	 */
	public void setTitoloDestinatario(String titoloDestinatario) {
		this.titoloDestinatario = titoloDestinatario;
	}

	/**
	 * @return Returns the modificabile.
	 */
	public boolean isModificabile() {
		return modificabile;
	}

	/**
	 * @param modificabile
	 *            The modificabile to set.
	 */
	public void setModificabile(boolean modificabile) {
		this.modificabile = modificabile;
	}

	public int getUfficioSelezionatoId() {
		return ufficioSelezionatoId;
	}

	public void setUfficioSelezionatoId(int ufficioCorrenteId) {
		this.ufficioSelezionatoId = ufficioCorrenteId;
	}

	/**
	 * @return Returns the tipoDocumentoId.
	 */

	public int getTipoDocumentoId() {
		return tipoDocumentoId;
	}

	/**
	 * @param tipoDocumentoId
	 *            The tipoDocumentoId to set.
	 */
	public void setTipoDocumentoId(int tipoDocumentoId) {
		this.tipoDocumentoId = tipoDocumentoId;
	}

	/**
	 * @return Returns the dataDocumento.
	 */
	public String getDataDocumento() {
		return dataDocumento;
	}

	/**
	 * @param dataDocumento
	 *            The dataDocumento to set.
	 */
	public void setDataDocumento(String dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	/**
	 * @return Returns the oggetto.
	 */
	public String getOggetto() {
		return oggetto;
	}

	/**
	 * @param oggetto
	 *            The oggetto to set.
	 */
	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public void rimuoviDocumentoPrincipale() {
		if (getDocumentoPrincipale() != null
				&& getDocumentoPrincipale().getId() != null) {
			getDocumentiRimossi().put(getDocumentoPrincipale().getId(),
					getDocumentoPrincipale());
		}
		setDocumentoPrincipale(new DocumentoVO());
	}

	/**
	 * @return Returns the titolario.
	 */
	public TitolarioVO getTitolario() {
		return titolario;
	}

	/**
	 * @param titolario
	 *            The titolario to set.
	 */
	public void setTitolario(TitolarioVO titolario) {
		this.titolario = titolario;
	}

	/**
	 * @return Returns the fascicolazione.
	 */
	public String[] getFascicoloSelezionatoId() {
		return fascicoloSelezionatoId;
	}

	/**
	 * @param fascicolazione
	 *            The fascicolazione to set.
	 */
	public void setFascicoloSelezionatoId(String[] fascicoloSelezionatoId) {
		this.fascicoloSelezionatoId = fascicoloSelezionatoId;
	}

	public DocumentoVO getDocumentoPrincipale() {
		return documentoPrincipale;
	}

	public void setDocumentoPrincipale(DocumentoVO documentoPrincipale) {
		this.documentoPrincipale = documentoPrincipale;
	}

	public int getUtenteSelezionatoId() {
		return utenteSelezionatoId;
	}

	public void setUtenteSelezionatoId(int utenteCorrenteId) {
		this.utenteSelezionatoId = utenteCorrenteId;
	}

	public Collection getTitolariFigli() {
		return titolariFigli;
	}

	public void setTitolariFigli(Collection titolariDiscendenti) {
		this.titolariFigli = titolariDiscendenti;
	}

	public int getTitolarioSelezionatoId() {
		return titolarioSelezionatoId;
	}

	public void setTitolarioSelezionatoId(int titolarioSelezionatoId) {
		this.titolarioSelezionatoId = titolarioSelezionatoId;
	}

	public int getTitolarioPrecedenteId() {
		return titolarioPrecedenteId;
	}

	public void setTitolarioPrecedenteId(int titolarioPrecedenteId) {
		this.titolarioPrecedenteId = titolarioPrecedenteId;
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

	public Map<Integer, DocumentoVO> getDocumentiRimossi() {
		return documentiRimossi;
	}

	public void setDocumentiRimossi(HashMap<Integer, DocumentoVO> documentiRimossi) {
		this.documentiRimossi = documentiRimossi;
	}

	public int getVersione() {
		return versione;
	}

	public void setVersione(int versione) {
		this.versione = versione;
	}

	public int getCartellaId() {
		return cartellaId;
	}

	public void setCartellaId(int cartellaId) {
		this.cartellaId = cartellaId;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getDescrizioneArgomento() {
		return descrizioneArgomento;
	}

	public void setDescrizioneArgomento(String descrizioneArgomento) {
		this.descrizioneArgomento = descrizioneArgomento;
	}

	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getStatoArchivio() {
		return statoArchivio;
	}

	public void setStatoArchivio(String statoArc) {
		this.statoArchivio = statoArc;
	}

	public String getStatoDocumento() {
		return statoDocumento;
	}

	public void setStatoDocumento(String statoDocumento) {
		this.statoDocumento = statoDocumento;
	}

	public void setDocumentiRimossi(Map<Integer, DocumentoVO> documentiRimossi) {
		this.documentiRimossi = documentiRimossi;
	}

	public void setFascicoliDocumento(Map<Integer, FascicoloVO> fascicoliProtocollo) {
		this.fascicoliDocumento = fascicoliProtocollo;
	}

	public FascicoloVO getFascicolo() {
		return fascicolo;
	}

	public void setFascicolo(FascicoloVO fascicolo) {
		this.fascicolo = fascicolo;
	}

	public Collection getFascicoli() {
		return fascicoli;
	}

	public void setFascicoli(Collection fascicoli) {
		this.fascicoli = fascicoli;
	}

	/**
	 * @return Returns the protocolliAllacciati.
	 */
	public Collection<FascicoloVO> getFascicoliDocumento() {
		return fascicoliDocumento.values();
	}

	public FascicoloVO getFascicoloDocumentoUnico() {
		if (fascicoliDocumento.size() == 1){
			Object fd[] = fascicoliDocumento.values().toArray();
			FascicoloVO f = (FascicoloVO)fd[0];
			return f;

		} 
		else
			return null;
	}

	public void aggiungiFascicolo(FascicoloVO fascicolo) {
		if (fascicolo != null) {
			this.fascicoliDocumento.put(fascicolo.getId(), fascicolo);
		}
	}

	public void rimuoviFascicolo(int fascicoloId) {
		this.fascicoliDocumento.remove(new Integer(fascicoloId));
	}

	/*
	 * public String getDescrizioneStatoProtocollo() { return
	 * ProtocolloBO.getStatoProtocollo(getFlagTipo(), getStato()); }
	 */

	public void inizializzaForm() {
		setDataDocumento(null);
		setDataDocumentoDa(null);
		setDataDocumentoA(null);
		setDescrizione(null);
		setDescrizioneArgomento(null);
		setTesto(null);
		setDocumentoPrincipale(new DocumentoVO());
		setFascicoloSelezionatoId(null);
		setDocumentoId(0);
		setOggetto(null);
		setNote(null);
		setNomeFile(null);
		fascicolo = new FascicoloVO();
		setTitolariFigli(null);
		setTitolario(null);
		setTitolarioPrecedenteId(0);
		setTitolarioSelezionatoId(0);
		setUfficioSelezionatoId(0);
		setUtenteSelezionatoId(0);
		setModificabile(true);
		setTipoDocumentoId(-1);
		setFascicolo(new FascicoloVO());
		setFascicoli(null);
		fascicoliDocumento = new HashMap<Integer,FascicoloVO>();
		permessi = new HashMap<String, PermessoDocumentoView>();
		setPermessiSelezionatiId(null);
		setVersione(0);
		setVersioneDefault(true);
		setStatoArchivio(Parametri.STATO_LAVORAZIONE);
		setStatoDocumento(Parametri.CHECKED_IN);
		setTipiDocumento(LookupDelegate.getInstance().getTipiDocumento(
				getAooId()));
		setMsgPermesso(null);
		setOwnerId(0);
		setOwners(null);
		setUfficioCorrenteId(0);
		setUfficioSelezionatoId(0);
		setUtenteCorrenteId(0);
		setUtenteSelezionatoId(0);
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		if (request.getParameter("allegaDocumentoPrincipaleAction") != null) {
			FormFile file = getFilePrincipaleUpload();
			String fileName = file.getFileName();
			if (file.getFileSize() == 0
					&& (fileName == null || "".equals(fileName))) {
				errors.add("documento", new ActionMessage("campo.obbligatorio",
						"File", ""));
			}

		} else if (request.getParameter("salvaAction") != null) {
			DocumentoVO documentoPrincipale = getDocumentoPrincipale();
			String fileName = documentoPrincipale.getFileName();
			if (documentoPrincipale.getSize() == 0
					&& (fileName == null || "".equals(fileName))) {
				errors.add("documento", new ActionMessage("campo.obbligatorio",
						"File", ""));
			}

			String dataDoc = getDataDocumento();
			// l'oggetto e' obbligatorio
			if (dataDoc == null || "".equals(dataDoc)) {
				errors.add("dataDoc", new ActionMessage("campo.obbligatorio",
						"Data documento", ""));
			} else {
				if (dataDoc != null && !"".equals(dataDoc)) {
					if (!DateUtil.isData(dataDoc)) {
						// la data del documento deve essere nel formato valido:
						// gg/mm/aaaa
						errors.add("dataDocumento", new ActionMessage(
								"formato.data.errato", "Data"));
					}
				}
			}
			if (oggetto == null || "".equals(oggetto)) {
				errors.add("oggetto", new ActionMessage("campo.obbligatorio",
						"Oggetto", ""));
			}
			if (getFascicoliDocumento().size() > 0 && getTitolario() == null) {
				errors.add("fascicolo",
						new ActionMessage("campo.obbligatorio", "Titolario",
								", se � stato impostato almeno fascicolo,"));
			}
			// if (getPermessi().size() == 0) {
			// // ci deve essere almeno un assegnatario
			// errors.add("permessi", new ActionMessage("campo.obbligatorio",
			// "Permessi documento", ""));
			// }

		} else if (request.getParameter("classificaDocumentoAction") != null) {
			if (getTitolario() == null) {
				errors.add("titolario", new ActionMessage("campo.obbligatorio",
						"Titolario", ""));
			}// deve essere in Lavorazione
			if (Parametri.STATO_CLASSIFICATO.equals(getStatoArchivio())) {
				errors.add("generale", new ActionMessage(
						"errore.classificazione", ", e' gi� classificato"));
			} else if (Parametri.STATO_INVIATO_PROTOCOLLO
					.equals(getStatoArchivio())) {
				errors.add("generale", new ActionMessage(
						"errore.classificazione",
						", e' in attesa di protocollazione"));
			}// deve avere permesso di modifica
			if (getPermessoCorrente() != Parametri.PERM_MODIFICA
					&& getPermessoCorrente() != Parametri.PERM_MODIFICA_CANCELLA
					&& getPermessoCorrente() != Parametri.PERM_OWNER) {
				errors.add("permessi", new ActionMessage(
						"error.documento.no_permission"));
			}
		} else if (request.getParameter("spostaDocumentoInLavorazioneAction") != null) {

			// deve essere classificato
			if (!Parametri.STATO_CLASSIFICATO.equals(getStatoArchivio())) {
				errors
						.add(
								"generale",
								new ActionMessage(
										"errore.sposta",
										(Parametri.STATO_LAVORAZIONE
												.equals(getStatoArchivio()) ? " perche' gia' in lavorazione."
												: (Parametri.STATO_INVIATO_PROTOCOLLO
														.equals(getStatoArchivio()) ? " perche' inviato al protocollo"
														: " motivo sconosciuto "))));
			}
			// deve avere il permesso di modifica o owner
			if (getPermessoCorrente() != Parametri.PERM_MODIFICA
					&& getPermessoCorrente() != Parametri.PERM_MODIFICA_CANCELLA
					&& getPermessoCorrente() != Parametri.PERM_OWNER) {
				errors.add("permessi", new ActionMessage(
						"error.documento.no_permission"));
			}
		} else if (request.getParameter("checkoutDocumentoAction") != null) {
			// deve essere in lavorazione
			if (Parametri.STATO_CLASSIFICATO.equals(getStatoArchivio())) {
				errors.add("generale", new ActionMessage(
						"errore.classificazione", ", e' gi� classificato"));
			} else if (Parametri.STATO_INVIATO_PROTOCOLLO
					.equals(getStatoArchivio())) {
				errors.add("generale", new ActionMessage(
						"errore.classificazione",
						", e' in attesa di protocollazione"));
			}// deve avere il permesso di modifica o owner
			if (getPermessoCorrente() != Parametri.PERM_MODIFICA
					&& getPermessoCorrente() != Parametri.PERM_MODIFICA_CANCELLA
					&& getPermessoCorrente() != Parametri.PERM_OWNER) {
				errors.add("permessi", new ActionMessage(
						"error.documento.no_permission"));
			}// deve essere in stato "Checked-in
			if (Parametri.CHECKED_OUT.equals(getStatoDocumento())) {
				errors.add("generale", new ActionMessage(
						"errore.stato.checkout", Organizzazione.getInstance()
								.getUtente(getUserLavId()).getValueObject()
								.getFullName()));
			}
		} else if (request.getParameter("eliminaDocumentoAction") != null) {
			// deve essere in lavorazione o classificato
			if (Parametri.STATO_INVIATO_PROTOCOLLO.equals(getStatoArchivio())) {
				errors.add("generale", new ActionMessage(
						"errore.classificazione",
						", e' in attesa di protocollazione"));
			}// deve avere il permesso di cancellazione o owner
			if (getPermessoCorrente() != Parametri.PERM_MODIFICA_CANCELLA
					&& getPermessoCorrente() != Parametri.PERM_OWNER) {
				errors.add("permessi", new ActionMessage(
						"error.documento.no_permission"));
			}// deve essere in stato "Checked-in
			if (Parametri.CHECKED_OUT.equals(getStatoDocumento())) {
				errors.add("generale", new ActionMessage(
						"errore.stato.checkout", Organizzazione.getInstance()
								.getUtente(getUserLavId()).getValueObject()
								.getFullName()));
			}
		} else if (request.getParameter("viewStoriaDocumentoAction") != null) {
			if (getPermessoCorrente() == Parametri.PERM_LETTURA) {
				errors.add("permessi", new ActionMessage(
						"error.documento.no_permission"));
			}

		} else if (request.getParameter("ripristinaVersioneDocumentoAction") != null) {
			if (getPermessoCorrente() != Parametri.PERM_MODIFICA_CANCELLA
					&& getPermessoCorrente() != Parametri.PERM_OWNER) {
				errors.add("permessi", new ActionMessage(
						"error.documento.no_permission"));
			}
		}
		return errors;
	}

	public ActionErrors validateDestinatario(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		// LookupDelegate.getInstance().getMezziSpedizione(aooId).iterator();
		// String codiceEmail = "";
		// if (getMezzoSpedizione() > 0) {
		// Collection mezzi = LookupDelegate.getInstance().getMezziSpedizione(
		// aooId);
		// Iterator i = mezzi.iterator();
		// while (i.hasNext()) {
		// IdentityVO m = (IdentityVO) i.next();
		// if (m.getId().intValue() == mezzoSpedizione) {
		// codiceEmail = m.getCodice();
		// break;
		// }
		// }
		// }
		// ------------
		/*
		 * if (EmailConstants.DESTINATARIO_TIPO_EMAIL.equals(codiceEmail)) { if
		 * (!EmailUtil.isValidEmail(emailDestinatario)) {
		 * errors.add("emailDestinatario", new ActionMessage(
		 * "destinatario_email")); } } else
		 */if (nominativoDestinatario == null
				|| "".equals(nominativoDestinatario.trim())) {
			errors.add("nominativoDestinatario", new ActionMessage(
					"destinatario_nome_obbligatorio"));
		} else if (!"".equals(dataSpedizione)
				&& !DateUtil.isData(dataSpedizione)) {
			errors.add("dataSpedizione", new ActionMessage(
					"formato.data.errato", "data spedizione", ""));
			/*
			 * } else if (!"".equals(dataSpedizione) && getMezzoSpedizione() ==
			 * 0) { errors.add("dataSpedizione", new ActionMessage(
			 * "selezione.obbligatoria", "il mezzo spedizione",
			 * "in presenza della data spedizione")); } else if
			 * (getMezzoSpedizione() != 0 && (getDataSpedizione() == null || ""
			 * .equals(getDataSpedizione().trim()))) {
			 * errors.add("dataSpedizione", new ActionMessage(
			 * "selezione.obbligatoria", "la data spedizione",
			 * "in presenza del mezzo spedizione"));
			 */
		} else if (!"".equals(dataSpedizione) && (getMezzoSpedizioneId() == 0)) {
			errors.add("dataSpedizione", new ActionMessage(
					"selezione.obbligatoria", "il mezzo spedizione",
					"in presenza della data spedizione"));
		} else if (getMezzoSpedizioneId() != 0
				&& (getDataSpedizione() == null || ""
						.equals(getDataSpedizione().trim()))) {
			errors.add("dataSpedizione", new ActionMessage(
					"selezione.obbligatoria", "la data spedizione",
					"in presenza del mezzo spedizione"));
		}

		return errors;
	}

	public ActionErrors validateParametriRicerca(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		if (request.getParameter("btnCerca") != null) {
			if (dataDocumentoDa != null && !"".equals(dataDocumentoDa)
					&& !DateUtil.isData(dataDocumentoDa)) {
				errors.add("dataDocumentoDa", new ActionMessage(
						"formato.data.errato", "Data documento da"));
			} else if (dataDocumentoA != null && !"".equals(dataDocumentoA)
					&& !DateUtil.isData(dataDocumentoA)) {
				errors.add("dataDocumentoA", new ActionMessage(
						"formato.data.errato", "Data documento A"));
			} else if (dataDocumentoA != null && !"".equals(dataDocumentoDa)
					&& DateUtil.isData(dataDocumentoA)
					&& dataDocumentoA != null && !"".equals(dataDocumentoA)
					&& DateUtil.isData(dataDocumentoA)) {
				if (DateUtil.toDate(dataDocumentoA).before(
						DateUtil.toDate(dataDocumentoDa))) {
					errors.add("dataDocumentoDa", new ActionMessage(
							"date_incongruenti"));
				}
			}

		} else if (request.getParameter("btnRicercaFullText") != null) {
			if (testo == null || "".equals(testo.trim())) {
				errors.add("testo", new ActionMessage("campo.obbligatorio",
						"testo", "(da ricercare nei documenti)"));
			}
		}
		return errors;
	}

	public ActionErrors validateAggiungiDoc(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		if (request.getParameter("btnAggiungi") != null) {
			if (request.getParameter("documentoSelezionato") == null) {
				errors.add("doc", new ActionMessage("selezione.obbligatoria",
						"il documento", ""));
			}
		}
		return errors;
	}

	public void aggiungiPermesso(PermessoDocumentoView ass) {
		permessi.put(ass.getKey(), ass);
	}

	public Collection<PermessoDocumentoView> getPermessi() {
		return permessi.values();
	}

	public String[] getPermessiSelezionatiId() {
		return permessiSelezionatiId;
	}

	public void rimuoviPermesso(String key) {
		permessi.remove(key);
	}

	public void setPermessiSelezionatiId(String[] permessi) {
		this.permessiSelezionatiId = permessi;
	}

	private boolean versioneDefault = true;

	public boolean isVersioneDefault() {
		return versioneDefault;
	}

	public void setVersioneDefault(boolean versioneDefault) {
		this.versioneDefault = versioneDefault;
	}

	public int getTipoPermessoSelezionato() {
		return tipoPermessoSelezionato;
	}

	public void setTipoPermessoSelezionato(int tipoPermessoSelezionato) {
		this.tipoPermessoSelezionato = tipoPermessoSelezionato;
	}

	public Collection getListaDocumenti() {
		return listaDocumenti;
	}

	public void setListaDocumenti(Collection listaDocumenti) {
		this.listaDocumenti = listaDocumenti;
	}

	public int getDocumentoSelezionato() {
		return documentoSelezionato;
	}

	public void setDocumentoSelezionato(int documentoSelezionato) {
		this.documentoSelezionato = documentoSelezionato;
	}

	public String getDataDocumentoA() {
		return dataDocumentoA;
	}

	public void setDataDocumentoA(String dataDocumentoA) {
		this.dataDocumentoA = dataDocumentoA;
	}

	public String getDataDocumentoDa() {
		return dataDocumentoDa;
	}

	public void setDataDocumentoDa(String dataDocumentoDa) {
		this.dataDocumentoDa = dataDocumentoDa;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public Collection<TipoDocumentoVO>  getTipiDocumento() {
		return LookupDelegate.getInstance().getTipiDocumento(getAooId());
	}

	public int getAooId() {
		return aooId;
	}

	public void setAooId(int aooId) {
		this.aooId = aooId;
	}

	public int getUserLavId() {
		return userLavId;
	}

	public void setTipiDocumento(Collection tipiDocumumenti) {
		this.tipiDocumento = tipiDocumumenti;
	}

	public void setUserLavId(int userLavId) {
		this.userLavId = userLavId;
	}

	public int getPermessoCorrente() {
		return permessoCorrente;
	}

	public void setPermessi(Map<String, PermessoDocumentoView>  permessi) {
		this.permessi = permessi;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public void setPermessoCorrente(int permessoCorrente) {
		this.permessoCorrente = permessoCorrente;
	}

	public int getRepositoryFileId() {
		return repositoryFileId;
	}

	public void setRepositoryFileId(int repositoryFileId) {
		this.repositoryFileId = repositoryFileId;
	}

	public String getUsernameLav() {
		return usernameLav;
	}

	public void setUsernameLav(String usernameLav) {
		this.usernameLav = usernameLav;
	}

	public ActionErrors validateDestinatari(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		if (request.getParameter("btnInvioProtocollo") != null) {
			if (destinatari.isEmpty()) {
				errors
						.add("destinatari", new ActionMessage(
								"selezione.obbligatoria",
								"almeno un destinatario", ""));
			}

		} else if (request.getParameter("aggiungiDestinatario") != null) {
			if (getNominativoDestinatario() == null
					|| "".equals(getNominativoDestinatario().trim())) {
				errors.add("destinatari", new ActionMessage(
						"selezione.obbligatoria", "almeno un destinatario",
						"utilizzando la funzione Aggiungi"));
			}
		}
		return errors;
	}

	public String getTipoDestinatario() {
		return tipoDestinatario;
	}

	public void setTipoDestinatario(String tipoDestinatario) {
		this.tipoDestinatario = tipoDestinatario;
	}

	public String getNominativoDestinatario() {
		return nominativoDestinatario;
	}

	public void setNominativoDestinatario(String nominativoDestinatario) {
		this.nominativoDestinatario = nominativoDestinatario;
	}

	private int destinatarioId;

	public int getDestinatarioId() {
		return destinatarioId;
	}

	public void setDestinatarioId(int destinatarioId) {
		this.destinatarioId = destinatarioId;
	}

	public Collection<DestinatarioView> getDestinatari() {
		return destinatari.values();
	}

	public DestinatarioView getDestinatario(String nomeDestinatario) {
		return (DestinatarioView) destinatari.get(nomeDestinatario);
	}

	private static int getNextId(Map<String,Integer>  m) {
		int max = 0;
		Iterator<String> it = m.keySet().iterator();
		while (it.hasNext()) {
			String id = it.next();
			int cur = NumberUtil.getInt(id);
			if (cur > max)
				max = cur;
		}
		return max + 1;
	}

	public void rimuoviDestinatari() {
		if (destinatari != null) {
			this.destinatari.clear();
			this.destinatariIds.clear();
		}
	}

	public String[] getDestinatarioSelezionatoId() {
		return destinatarioSelezionatoId;
	}

	public void setDestinatarioSelezionatoId(String[] destinatarioSelezionatoId) {
		this.destinatarioSelezionatoId = destinatarioSelezionatoId;
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public String getDataSpedizione() {
		return dataSpedizione;
	}

	public void setDataSpedizione(String dataSpedizione) {
		this.dataSpedizione = dataSpedizione;
	}

	public String getEmailDestinatario() {
		return emailDestinatario;
	}

	public void setEmailDestinatario(String emailDestinatario) {
		this.emailDestinatario = emailDestinatario;
	}

	public boolean getFlagConoscenza() {
		return flagConoscenza;
	}

	public void setFlagConoscenza(boolean flagConoscenza) {
		this.flagConoscenza = flagConoscenza;
	}

	public String getIndirizzoDestinatario() {
		return indirizzoDestinatario;
	}

	public void setIndirizzoDestinatario(String indirizzoDestinatario) {
		this.indirizzoDestinatario = indirizzoDestinatario;
	}

	public Collection<SpedizioneVO> getMezziSpedizione() {
		return getLookupDelegateDelegate().getMezziSpedizione(getAooId());
	}

	private LookupDelegate getLookupDelegateDelegate() {
		return LookupDelegate.getInstance();
	}

	public int getMezzoSpedizione() {
		return mezzoSpedizione;
	}

	public void setMezzoSpedizione(int mezzoSpedizione) {
		this.mezzoSpedizione = mezzoSpedizione;
	}

	public void setDestinatari(Map<String, DestinatarioView>  destinatari) {
		this.destinatari = destinatari;
	}

	public boolean isOwner() {
		return getUserLavId() == getUtenteCorrenteId();
	}

	public void inizializzaDestinatarioForm() {
		setDataSpedizione(null);
		setEmailDestinatario(null);
		setCitta(null);
		setIndirizzoDestinatario(null);
		setCapDestinatario(null);
		setFlagConoscenza(false);
		setMezzoSpedizione(0);
		setNominativoDestinatario(null);
		setTipoDestinatario("F");
		setIdx(0);
		setMezzoSpedizioneId(0);
		setTitoloDestinatario(null);
	}

	public void aggiungiDestinatario(DestinatarioView destinatario) {
		if (destinatario != null) {
			if (destinatario.getIdx() == 0) {
				int idx = getNextId(destinatariIds);
				destinatario.setIdx(idx);
				destinatari.put(String.valueOf(idx), destinatario);
				destinatariIds.put(String.valueOf(idx), new Integer(idx));
			} else {
				destinatari.put(String.valueOf(destinatario.getIdx()),
						destinatario);
			}

		}
	}

	public void rimuoviDestinatario(String id) {
		DestinatarioView removed = (DestinatarioView) destinatari.get(id);
		int idx = removed.getIdx();
		destinatari.remove(String.valueOf(removed.getIdx()));
		destinatariIds.remove(String.valueOf(idx));
	}

	public Collection<IdentityVO> getStatiDocumentoCollection() {
		return LookupDelegate.getStatiDocumento().values();
	}

	public String getDestinatarioMezzoId() {
		return destinatarioMezzoId;
	}

	public void setDestinatarioMezzoId(String destinatarioMezzoId) {
		this.destinatarioMezzoId = destinatarioMezzoId;
	}

	public String getAssegnatoDa() {
		return assegnatoDa;
	}

	public void setAssegnatoDa(String assegnatoDa) {
		this.assegnatoDa = assegnatoDa;
	}

	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getCapDestinatario() {
		return capDestinatario;
	}

	public void setCapDestinatario(String capDestinatario) {
		this.capDestinatario = capDestinatario;
	}

	public String getMsgPermesso() {
		return msgPermesso;
	}

	public void setMsgPermesso(String msgPermesso) {
		this.msgPermesso = msgPermesso;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.utenteCorrente = (Utente) request.getSession().getAttribute(
				Constants.UTENTE_KEY);
		setFlagConoscenza(false);
	}

	public boolean isTuttiUffici() {
		if (getUfficioCorrente() == null)
			return false;
		boolean tutti = getUfficioCorrente().getParentId() == 0;
		if (!tutti) {
			tutti = getUfficioCorrente().getId().equals(
					utenteCorrente.getUfficioVOInUso().getId());
		}
		return tutti;
	}

	public boolean isRicercaFullText() {
		return ricercaFullText;
	}

	public void setRicercaFullText(boolean ricercaFullText) {
		this.ricercaFullText = ricercaFullText;
	}

	public int getMezzoSpedizioneId() {
		return mezzoSpedizioneId;
	}

	public void setMezzoSpedizioneId(int mezzoSpedizioneId) {
		this.mezzoSpedizioneId = mezzoSpedizioneId;
	}

}