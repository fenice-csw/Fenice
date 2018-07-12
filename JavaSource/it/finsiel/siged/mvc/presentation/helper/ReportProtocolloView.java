package it.finsiel.siged.mvc.presentation.helper;

import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.StringUtil;
import it.flosslab.mvc.business.MittentiDelegate;

import java.util.ArrayList;
import java.util.List;

public class ReportProtocolloView implements Comparable<ReportProtocolloView> {

	public ReportProtocolloView() {
	}

	private int protocolloId;

	private int numeroProtocollo;

	private int annoProtocollo;

	private long registroAnnoNumero;

	private String dataScarico;

	private String dataProtocollo;

	private String tipoProtocollo;

	private String mittente;

	private String tipoMittente;

	private String oggetto;

	private String destinatario;

	private String ufficio;

	private String utenteAssegnante;

	private String tipoDocumento;

	private int documentoId;

	private String filename;

	private boolean pdf;

	private String statoProtocollo;

	private String ufficioAssegnatario;

	private String utenteAssegnatario;

	private String dataAnnullamento;

	private String provvedimentoAnnullamento;

	private String notaAnnullamento;

	private String dataSpedizione;

	private String mezzoSpedizione;

	private int mezzoSpedizioneId;

	private String allaccio;

	private int versione;

	private int massimario;

	private String messaggio;

	private boolean modificabile;

	private boolean isRifiutabile;

	public final static String PDF_SI = "SI";

	private int titolarioId;

	private int giorniAlert;

	private boolean competente;

	private String toponimo;

	private String citta;

	private boolean titolareProcedimento;

	private boolean inProcedimento;
	
	private String impronta;
	
	private String numeroProtocolloMittente;

	private String dataProtocolloMittente;
	
	private String estremi;
	
	private String statoConservazione;

    private boolean isVisibileDaFascicolo;
    
    private int ufficioProprietarioId;
    

	public int getUfficioProprietarioId() {
		return ufficioProprietarioId;
	}

	public void setUfficioProprietarioId(int ufficioProprietarioId) {
		this.ufficioProprietarioId = ufficioProprietarioId;
	}

	public boolean isVisibileDaFascicolo() {
		return isVisibileDaFascicolo;
	}

	public void setVisibileDaFascicolo(boolean isVisibileDaFascicolo) {
		this.isVisibileDaFascicolo = isVisibileDaFascicolo;
	}

	public String getStatoConservazione() {
		return statoConservazione;
	}

	public void setStatoConservazione(String statoConservazione) {
		this.statoConservazione = statoConservazione;
	}

	public String getEstremi() {
		return estremi;
	}

	public void setEstremi(String estremi) {
		this.estremi = estremi;
	}

	public String getProtocolloMittente() {
		
		
		if ((dataProtocolloMittente!=null && !dataProtocolloMittente.equals(""))&& (numeroProtocolloMittente!=null && !numeroProtocolloMittente.equals("")))
			return numeroProtocolloMittente+" del "+dataProtocolloMittente;
		else
			return "";
		
	}
	
	public String getNumeroProtocolloMittente() {
		return numeroProtocolloMittente;
	}

	public void setNumeroProtocolloMittente(String numeroProtocolloMittente) {
		this.numeroProtocolloMittente = numeroProtocolloMittente;
	}

	public String getDataProtocolloMittente() {
		return dataProtocolloMittente;
	}

	public void setDataProtocolloMittente(String dataProtocolloMittente) {
		this.dataProtocolloMittente = dataProtocolloMittente;
	}

	public String getImpronta() {
		return impronta;
	}

	public void setImpronta(String impronta) {
		this.impronta = impronta;
	}

	public boolean isInProcedimento() {
		return inProcedimento;
	}

	public void setInProcedimento(boolean inProcedimento) {
		this.inProcedimento = inProcedimento;
	}

	public boolean isTitolareProcedimento() {
		return titolareProcedimento;
	}

	public void setTitolareProcedimento(boolean titolareProcedimento) {
		this.titolareProcedimento = titolareProcedimento;
	}

	public String getIndirizzo() {
		if (getCitta() == null || getCitta().trim().equals(""))
			return "";
		else
			return ((getToponimo() == null || getToponimo().trim().equals("")) ? ""
					: getToponimo())
					+ " " + getCitta();
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public String getToponimo() {
		return toponimo;
	}

	public void setToponimo(String toponimo) {
		this.toponimo = toponimo;
	}

	// lo so fa schifo
	public String getMittentiView() {
		if (tipoMittente != null && tipoMittente.equals("M")) {
			StringBuffer mittenti = new StringBuffer();
			for (SoggettoVO s : getMittenti()) {
				mittenti.append(s.getDescrizione());
				mittenti.append("\r\n");
			}
			String mitt = mittenti.toString();
			return mitt;

		} else
			return getMittente();

	}

	// lo so fa ancora più schifo
	public String getDestinatarioView() {
		String dest = getDestinatario().replaceAll("<ul>", "").replaceAll(
				"</ul>", "").replaceAll("<em>", "").replaceAll("</em>", "")
				.replaceAll("<li>", "").replaceAll("</li>", "\r\n");
		return dest;

	}

	public String getTipoProtocolloView() {
		if (this.tipoProtocollo.equals("I"))
			return "Ingresso";
		else if (this.tipoProtocollo.equals("U"))
			return "Uscita";
		else
			return "Posta Interna";
	}

	public boolean isCompetente() {
		return competente;
	}

	public void setCompetente(boolean competente) {
		this.competente = competente;
	}

	public int getGiorniAlert() {
		return giorniAlert;
	}

	public void setGiorniAlert(int giorniAlert) {
		this.giorniAlert = giorniAlert;
	}

	public String getGiorniPermanenza() {
		if (getGiorniAlert() != 0)
			return String.valueOf(DateUtil.getDiffDay(getDataProtocollo()));
		else
			return "/";
	}

	public long getRegistroAnnoNumero() {
		return registroAnnoNumero;
	}

	public void setRegistroAnnoNumero(long registroAnnoNumero) {
		this.registroAnnoNumero = registroAnnoNumero;
	}

	public boolean isRifiutabile() {
		return isRifiutabile;
	}

	public void setRifiutabile(boolean isRifiutabile) {
		this.isRifiutabile = isRifiutabile;
	}

	public String getTipoMittente() {
		return tipoMittente;
	}

	public void setTipoMittente(String tipoMittente) {
		this.tipoMittente = tipoMittente;
	}

	public List<SoggettoVO> getMittenti() {
		List<SoggettoVO> mittenti = new ArrayList<SoggettoVO>();
		MittentiDelegate delegate = MittentiDelegate.getInstance();
		mittenti = delegate.getMittenti(this.protocolloId);
		return mittenti;
	}

	public List<SoggettoVO> getStoriaMittenti() throws Exception {
		List<SoggettoVO> mittenti = new ArrayList<SoggettoVO>();
		MittentiDelegate delegate = MittentiDelegate.getInstance();
		mittenti = delegate.getStoriaMittenti(this.protocolloId, this.versione);
		return mittenti;
	}

	public int getMezzoSpedizioneId() {
		return mezzoSpedizioneId;
	}

	public void setMezzoSpedizioneId(int mezzoSpedizioneId) {
		this.mezzoSpedizioneId = mezzoSpedizioneId;
	}

	public int getMassimario() {
		return massimario;
	}

	public void setMassimario(int massimario) {
		this.massimario = massimario;
	}

	/**
	 * @return Returns the pdf.
	 */
	public String getPdf() {
		return this.pdf ? PDF_SI : null;
	}

	/**
	 * @param pdf
	 *            The pdf to set.
	 */
	public void setPdf(boolean pdf) {
		this.pdf = pdf;
	}

	/**
	 * @return Returns the statoProtocollo.
	 */
	public String getStatoProtocollo() {

		if (!getStato().equals("N") && !getStato().equals("S"))
			return ProtocolloBO.getStatoProtocollo(this.tipoProtocollo,
					this.statoProtocollo);
		if (getVersione() <= 1)
			return "Registrato";
		if (getVersione() > 1)
			return "Modificato";

		return "non classificato";
	}

	public String getStato() {
		return this.statoProtocollo;
	}

	/**
	 * @return Returns the protocolloId.
	 */
	public int getProtocolloId() {
		return protocolloId;
	}

	/**
	 * @param protocolloId
	 *            The protocolloId to set.
	 */
	public void setProtocolloId(int protocolloId) {
		this.protocolloId = protocolloId;
	}

	public void setStatoProtocollo(String statoProtocollo) {
		this.statoProtocollo = statoProtocollo;
	}

	public String getAnnoNumeroProtocollo() {

		if (numeroProtocollo != 0 && annoProtocollo != 0)
			return StringUtil.formattaNumeroProtocollo(String
					.valueOf(numeroProtocollo), 7);
		else
			return "";
	}

	public int getAnnoProtocollo() {
		return annoProtocollo;
	}

	public void setAnnoProtocollo(int annoProtocollo) {
		this.annoProtocollo = annoProtocollo;
	}

	public String getDataProtocollo() {
		return dataProtocollo;
	}

	public void setDataProtocollo(String dataProtocollo) {
		this.dataProtocollo = dataProtocollo;
	}

	public String getDataScarico() {
		return dataScarico;
	}

	public void setDataScarico(String dataScarico) {
		this.dataScarico = dataScarico;
	}

	public String getMittente() {
		return mittente;
	}

	public void setMittente(String mittente) {
		this.mittente = mittente;
	}

	public int getNumeroProtocollo() {
		return numeroProtocollo;
	}

	public void setNumeroProtocollo(int numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getTipoProtocollo() {
		return tipoProtocollo;

	}

	public void setTipoProtocollo(String tipoProtocollo) {
		this.tipoProtocollo = tipoProtocollo;
	}

	public String getUfficio() {
		return ufficio;
	}

	public String getUtenteAssegnante() {
		return utenteAssegnante;
	}

	public void setUfficio(String ufficio) {
		if (ufficio != null && !ufficio.trim().equals("")) {
			Organizzazione org = Organizzazione.getInstance();
			Ufficio uff = org.getUfficio(Integer.parseInt(ufficio));
			if (uff != null)
				this.ufficio = uff.getPath();
			else
				this.ufficio = "";
		}
	}

	public String getDestinatario() {
		if (destinatario.equals("<ul><em>null</em></ul>"))
			return "";
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getUfficioAssegnatario() {
		return ufficioAssegnatario;
	}

	public String getUtenteAssegnatario() {
		return utenteAssegnatario;
	}

	public void setUfficioAssegnatario(String ufficioAssegnatario) {

		if (ufficioAssegnatario != null
				&& !ufficioAssegnatario.trim().equals("")) {
			if (!Parametri.PROTOCOLLO_RISERVATO.equals(ufficioAssegnatario)) {
				Organizzazione org = Organizzazione.getInstance();
				this.ufficioAssegnatario = org.getUfficio(
						Integer.parseInt(ufficioAssegnatario)).getPath();
			} else {
				this.ufficioAssegnatario = Parametri.PROTOCOLLO_RISERVATO;
			}
		} else
			ufficioAssegnatario = "";
	}

	public void setUtenteAssegnatario(String caricaAssegnatario) {
		Organizzazione org = Organizzazione.getInstance();
		if (caricaAssegnatario != null && !"0".equals(caricaAssegnatario)) {
			CaricaVO carica = org.getCarica(Integer
					.parseInt(caricaAssegnatario));
			if (carica != null) {
				if (!Parametri.PROTOCOLLO_RISERVATO.equals(caricaAssegnatario)) {
					Utente ut = org.getUtente(carica.getUtenteId());
					if (ut != null)
						this.utenteAssegnatario = ut.getValueObject()
								.getFullName();
					else
						this.utenteAssegnatario = carica.getNome();
				} else {
					this.utenteAssegnatario = Parametri.PROTOCOLLO_RISERVATO;
				}
			} else {
				this.utenteAssegnatario = "";
			}
		} else {
			this.utenteAssegnatario = "";
		}
	}

	public void setUtenteAssegnante(String caricaAssegnante) {
		Organizzazione org = Organizzazione.getInstance();
		if (caricaAssegnante != null && !"0".equals(caricaAssegnante)) {
			CaricaVO carica = org.getCarica(Integer.parseInt(caricaAssegnante));
			if (carica != null) {
				if (!Parametri.PROTOCOLLO_RISERVATO.equals(caricaAssegnante)) {
					Utente ut = org.getUtente(carica.getUtenteId());
					if (ut != null)
						this.utenteAssegnante = ut.getValueObject()
								.getFullName();
					else
						this.utenteAssegnante = carica.getNome();
				} else {
					this.utenteAssegnante = Parametri.PROTOCOLLO_RISERVATO;
				}
			} else {
				this.utenteAssegnante = "";
			}
		} else {
			this.utenteAssegnante = "";
		}
	}

	public String getAssegnatario() {
		if (utenteAssegnatario != null && !"".equals(utenteAssegnatario)
				&& !Parametri.PROTOCOLLO_RISERVATO.equals(utenteAssegnatario)) {
			return ufficioAssegnatario + utenteAssegnatario;
		} else {
			return ufficioAssegnatario;
		}

	}

	public String getAssegnante() {
		if (utenteAssegnante != null && !"".equals(utenteAssegnante)
				&& !Parametri.PROTOCOLLO_RISERVATO.equals(utenteAssegnante)) {
			return ufficio + utenteAssegnante;
		} else {
			return ufficio;
		}
	}

	public String getDataAnnullamento() {
		return dataAnnullamento;
	}

	public void setDataAnnullamento(String dataAnnullamento) {
		this.dataAnnullamento = dataAnnullamento;
	}

	public String getNotaAnnullamento() {
		return notaAnnullamento;
	}

	public void setNotaAnnullamento(String notaAnnullamento) {
		this.notaAnnullamento = notaAnnullamento;
	}

	public String getProvvedimentoAnnullamento() {
		return provvedimentoAnnullamento;
	}

	public void setProvvedimentoAnnullamento(String provvedimentoAnnullamento) {
		this.provvedimentoAnnullamento = provvedimentoAnnullamento;
	}

	public String getDataSpedizione() {
		return dataSpedizione;
	}

	public void setDataSpedizione(String dataSpedizione) {
		this.dataSpedizione = dataSpedizione;
	}

	/**
	 * @return Returns the mezzoSpedizione.
	 */
	public String getMezzoSpedizione() {
		return mezzoSpedizione;
	}

	/**
	 * @param mezzoSpedizione
	 *            The mezzoSpedizione to set.
	 */
	public void setMezzoSpedizione(String mezzoSpedizione) {
		this.mezzoSpedizione = mezzoSpedizione;
	}

	public String getAllaccio() {
		return allaccio;
	}

	public void setAllaccio(String allaccio) {
		this.allaccio = allaccio;
	}

	public int getVersione() {
		return versione;
	}

	public void setVersione(int versione) {
		this.versione = versione;
	}

	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}

	public int getDocumentoId() {
		return documentoId;
	}

	public void setDocumentoId(int documentoId) {
		this.documentoId = documentoId;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public boolean isModificabile() {
		return modificabile;
	}

	public void setModificabile(boolean modificabile) {
		this.modificabile = modificabile;
	}

	public int getTitolarioId() {
		return titolarioId;
	}

	public void setTitolarioId(int titolarioId) {
		this.titolarioId = titolarioId;
	}

	
	public long getAnnoNumero() {
		long ran = 1 * 1000000000l + Integer.valueOf(numeroProtocollo);
		String annonumero=String.valueOf(annoProtocollo)+String.valueOf(ran).substring(1);
		return Long.valueOf(annonumero);
	}
	 
	
	public int compareTo(ReportProtocolloView r) {
		return (getAnnoNumero() > r.getAnnoNumero()) ? -1
				: (getAnnoNumero() == r.getAnnoNumero() ? 0 : 1);
	}

}