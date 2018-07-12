package it.finsiel.siged.mvc.integration;

import it.compit.fenice.mvc.presentation.helper.ReportCheckPostaInternaView;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.presentation.helper.RubricaView;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;

import java.util.Collection;
import java.util.Date;

/*
 * @author G.Calli.
 */

public interface ReportProtocolloDAO {

	public int countStampaRegistroReport(Utente utente, String tipoProtocollo,
			Date dataInizio, Date dataFine, int ufficioId) throws DataException;

	public Collection<ReportProtocolloView> getRegistroReport(Utente utente, String tipoprotocollo,
			Date dataInizio, Date dataFine, int ufficioId) throws DataException;
	
	public int countProtocolliModificatiReport(Utente utente, String tipoProtocollo,
			Date dataInizio, Date dataFine, int ufficioId) throws DataException;
	
	public int countProtocolliAnnullatiReport(Utente utente, String tipoProtocollo,
			Date dataInizio, Date dataFine, int ufficioId) throws DataException;
	
	public Collection<ReportProtocolloView> getProtocolliModificatiReport(Utente utente, String tipoprotocollo,
			Date dataInizio, Date dataFine, int ufficioId) throws DataException;


	public Collection<ReportProtocolloView> getProtocolliScaricati(int registroId, Date dataInizio,
			Date dataFine, UfficioVO ufficio, int assegnatario)
			throws DataException;

	public Collection<ReportProtocolloView> getProtocolliInLavorazione(int registroId,
			Date dataInizio, Date dataFine, UfficioVO ufficio, int assegnatario)
			throws DataException;

	public int countProtocolliInLavorazione(int registroId, Date dataInizio,
			Date dataFine, UfficioVO ufficio, int assegnatario)
			throws DataException;

	public int countProtocolliDaScartare(Utente utente, int ufficioId,
			java.util.Date dataInizio, java.util.Date dataFine)
			throws DataException;

	public Collection<ReportProtocolloView> cercaProtocolliDaScartare(Utente utente, int ufficioId,
			java.util.Date dataInizio, java.util.Date dataFine)
			throws DataException;

	public int countProtocolliScaricati(int registroId, Date dataInizio,
			Date dataFine, UfficioVO ufficio, int assegnatario)
			throws DataException;

	public Collection<ReportProtocolloView> getProtocolliAssegnati(int registroId, UfficioVO ufficio,
			int ufficioAssegnatario,int caricaAssegnatario,int anno) throws DataException;

	public int countProtocolliAssegnati(int registroId, UfficioVO ufficio,
			int ufficioAssegnatario,int caricaAssegnatario,int anno) throws DataException;

	public Collection<ReportProtocolloView> getProtocolliAnnullati(int registroId, UfficioVO ufficio,
			int ufficioId) throws DataException;

	public int countProtocolliAnnullati(int registroId, UfficioVO ufficio,
			int ufficioId) throws DataException;

	public Collection<ReportCheckPostaInternaView> getNotifichePostaInterna(int caricaId, Date dataInizio,
			Date dataFine) throws DataException;

	public Collection<ReportProtocolloView> getProtocolliSpediti(int registroId, Date dataInizio,
			Date dataFine, int ufficioId, String mezzoSpedizione,
			int mezzoSpedizioneId) throws DataException;
	
	public int countProtocolliSpediti(int registroId, Date dataInizio,
			Date dataFine, int ufficioId, String mezzoSpedizione,
			int mezzoSpedizioneId) throws DataException;
	
	public int countNotifichePostaInterna(int caricaId, Date dataInizio,
			Date dataFine) throws DataException;
	
	public double getPrezzoSpedizione(int registroId, Date dataInizio,
			Date dataFine, int ufficioId, String mezzoSpedizione,
			int mezzoSpedizioneId) throws DataException;

	public Collection<RubricaView> getRubrica(String flagTipo, int aooId)
			throws DataException;

	public int countRubrica(String tipoPersona, int aooId) throws DataException;

	public int getNumeroProtocolli(int ufficioAssegnatarioId,
			Integer utenteAssegnatario, String statoProtocollo,
			String statoAssegnazione, Date dataDa, Date dataA, Utente utente)
			throws DataException;

	public Collection<ReportProtocolloView> getDettaglioStatisticheProtocolli(
			int ufficioAssegnatarioId, Integer utenteAssegnatario,
			String statoProtocollo, String statoAssegnazione, Date dataDa,
			Date dataA, Utente utente) throws DataException;

	public Collection<ReportProtocolloView> getProtocolloRofiutato(int protocolloId)throws DataException;
	
	//VELINE
	
	public int countVeline(Utente utente, Date dataInizio, Date dataFine, int ufficioId,int caricaId,int numInizio,int numFine,String tipoProtocollo, int conoscenza) throws DataException;
		
	public Collection<ReportProtocolloView> getVeline(Utente utente, Date dataInizio, Date dataFine, int ufficioId,int caricaId,int numInizio,int numFine,String tipoProtocollo, int conoscenza) throws DataException;

	public Collection<AssegnatarioView> getStampaAssegnatariVeline(Utente utente, Date dataInizio, Date dataFine, int numInizio,int numFine,String tipoProtocollo,int conoscenza) throws DataException;

	
}