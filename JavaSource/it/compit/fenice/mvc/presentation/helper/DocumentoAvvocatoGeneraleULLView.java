package it.compit.fenice.mvc.presentation.helper;

import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.util.DateUtil;

import java.sql.Timestamp;

public class DocumentoAvvocatoGeneraleULLView{

	private int documentoId;

	private int caricaId;

	private String txt;

	private String nome;

	private String oggetto;

	private int flagStato;

	private int flagTipo;
	
	private int versione;

	private Timestamp dataCreazione;
	
	private String msgCarica;
	
	private String mittente;
	
	private int procedimentoId;

	private int statoProcedimento;

	private String numeroProcedimento;

	
	public DocumentoAvvocatoGeneraleULLView() {
	}
	
	public int getProcedimentoId() {
		return procedimentoId;
	}



	public void setProcedimentoId(int procedimentoId) {
		this.procedimentoId = procedimentoId;
	}


	public int getStatoProcedimento() {
		return statoProcedimento;
	}


	public void setStatoProcedimento(int statoProcedimento) {
		this.statoProcedimento = statoProcedimento;
	}


	public String getNumeroProcedimento() {
		return numeroProcedimento;
	}


	public void setNumeroProcedimento(String numeroProcedimento) {
		this.numeroProcedimento = numeroProcedimento;
	}


	public String getMittente() {
		return mittente;
	}

	public void setMittente(String mittente) {
		this.mittente = mittente;
	}

	public String getMsgCarica() {
		return msgCarica;
	}

	public void setMsgCarica(String msgCarica) {
		this.msgCarica = msgCarica;
	}
	
    
	public int getFlagTipo() {
		return flagTipo;
	}

	public void setFlagTipo(int flagTipo) {
		this.flagTipo = flagTipo;
	}


	public void removeDestinatario(int destinatarioId) {
		removeDestinatario(new Integer(destinatarioId));
	}

	public void removeDestinatario(DestinatarioVO destinatario) {
		if (destinatario != null) {
			removeDestinatario(destinatario.getId());
		}
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public int getDocumentoId() {
		return documentoId;
	}

	public int getCaricaId() {
		return caricaId;
	}

	public String getTxt() {
		return txt;
	}

	public String getNome() {
		return nome;
	}

	public int getFlagStato() {
		return flagStato;
	}

	public String getStato() {
		if (flagStato == 0)
			return "in lavorazione";
		if (flagStato == 1)
			return "Protocollato";
		if (flagStato == 2)
			return "Inviato al Protocollo";
		if (flagStato == 3)
			return "Inviato al altro utente";
		else
			return " ";
	}

	public int getVersione() {
		return versione;
	}

	public void setDocumentoId(int documentoId) {
		this.documentoId = documentoId;
	}

	public void setCaricaId(int caricaId) {
		this.caricaId = caricaId;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public void setNome(String nomeFile) {
		this.nome = nomeFile;
	}

	public void setFlagStato(int flagStato) {
		this.flagStato = flagStato;
	}

	public void setVersione(int versione) {
		this.versione = versione;
	}

	public String getIntestatario() {
		Organizzazione org = Organizzazione.getInstance();
		String responsabile = "";
		if (getCaricaId() != 0) {
			CaricaVO carica = org.getCarica(getCaricaId());
			if (carica != null) {
				Ufficio uff = org.getUfficio(carica.getUfficioId());
				if (uff != null)
					responsabile = uff.getPath();
				Utente ute = org.getUtente(carica.getUtenteId());
				if (ute == null)
					return uff.getPath() + carica.getNome();
				ute.getValueObject().setCarica(carica.getNome());
				if (carica.isAttivo())
					responsabile = uff.getPath()
							+ ute.getValueObject().getCaricaFullName();
				else
					responsabile = uff.getPath()
							+ ute.getValueObject().getCaricaFullNameNonAttivo();
			}

		}
		return responsabile;
	}

	public Timestamp getDataCreazione() {
		return dataCreazione;
	}

	public String getData() {
		return DateUtil.formattaDataOra(dataCreazione.getTime());
	}

	public void setDataCreazione(Timestamp dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	@Override
	public String toString() {
		return "DocumentoAvvocatoGeneraleULLView [documentoId=" + documentoId
				+ ", flagStato=" + flagStato + ", procedimentoId="
				+ procedimentoId + ", statoProcedimento=" + statoProcedimento
				+ ", numeroProcedimento=" + numeroProcedimento + "]";
	}


}
