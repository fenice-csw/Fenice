package it.finsiel.siged.mvc.business;

import it.compit.fenice.mvc.presentation.helper.ReportCheckPostaInternaView;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.integration.ReportProtocolloDAO;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.presentation.helper.RubricaView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

public class ReportProtocolloDelegate {

	private static Logger logger = Logger
			.getLogger(ReportProtocolloDelegate.class.getName());

	private ReportProtocolloDAO reportProtocolloDAO = null;

	//private ServletConfig config = null;

	private static ReportProtocolloDelegate delegate = null;

	private ReportProtocolloDelegate() {
		try {
			if (reportProtocolloDAO == null) {
				reportProtocolloDAO = (ReportProtocolloDAO) DAOFactory
						.getDAO(Constants.REPORT_PROTOCOLLO_DAO_CLASS);

				logger.debug("reportProtocolloDAO instantiated:"
						+ Constants.REPORT_PROTOCOLLO_DAO_CLASS);
			}
		} catch (Exception e) {
			logger.error("Exception while connecting "
					+ Constants.REPORT_PROTOCOLLO_DAO_CLASS, e);
		}
	}

	public static ReportProtocolloDelegate getInstance() {
		if (delegate == null)
			delegate = new ReportProtocolloDelegate();
		return delegate;
	}

	public static String getIdentifier() {
		return Constants.REPORT_PROTOCOLLO_DELEGATE;
	}
	
	// REPORTS
	public int countStampaRegistroReport(Utente utente, String tipoprotocollo,
			Date dataInizio, Date dataFine, int ufficioId) {
		try {
			return reportProtocolloDAO.countStampaRegistroReport(utente,
					tipoprotocollo, dataInizio, dataFine, ufficioId);
		} catch (DataException de) {
			logger
					.error("ReportProtocolloDelegate: failed getting countStampaRegistroReport: ");
			return 0;
		}
	}
	
	public int countProtocolliModificatiReport(Utente utente, String tipoprotocollo,
			Date dataInizio, Date dataFine, int ufficioId) {
		try {
			return reportProtocolloDAO.countProtocolliModificatiReport(utente,
					tipoprotocollo, dataInizio, dataFine, ufficioId);
		} catch (DataException de) {
			logger
					.error("ReportProtocolloDelegate: failed getting countStampaRegistroReport: ");
			return 0;
		}
	}
	
	public int countProtocolliAnnullatiReport(Utente utente, String tipoprotocollo,
			Date dataInizio, Date dataFine, int ufficioId) {
		try {
			return reportProtocolloDAO.countProtocolliAnnullatiReport(utente,
					tipoprotocollo, dataInizio, dataFine, ufficioId);
		} catch (DataException de) {
			logger
					.error("ReportProtocolloDelegate: failed getting countStampaRegistroReport: ");
			return 0;
		}
	}


	public Collection<ReportProtocolloView>  stampaRegistro(Utente utente, String tipoprotocollo,
			java.util.Date dataInizio, java.util.Date dataFine, int ufficioId)
			throws DataException {
		try {
			return reportProtocolloDAO.getRegistroReport(utente,
					tipoprotocollo, dataInizio, dataFine, ufficioId);
		} catch (DataException de) {
			logger
					.error("ReportProtocolloDelegate: failed getting stampaRegistro: ");
			throw new DataException("Errore nella lettura dei dati");
		}
	}
	
	public Collection<ReportProtocolloView>  getProtocolliModificatiReport(Utente utente, String tipoprotocollo,
			java.util.Date dataInizio, java.util.Date dataFine, int ufficioId)
			throws DataException {
		try {
			return reportProtocolloDAO.getProtocolliModificatiReport(utente,
					tipoprotocollo, dataInizio, dataFine, ufficioId);
		} catch (DataException de) {
			logger
					.error("ReportProtocolloDelegate: failed getting getProtocolliModificatiReport: ");
			throw new DataException("Errore nella lettura dei dati");
		}
	}

	public int countProtocolliInLavorazione(Utente utente, Date dataInizio,
			Date dataFine, int assegnatario) {
		try {
			Organizzazione org = Organizzazione.getInstance();
			Ufficio uff = org.getUfficio(assegnatario);
			return reportProtocolloDAO.countProtocolliInLavorazione(utente
					.getRegistroInUso(), dataInizio, dataFine, uff
					.getValueObject(), assegnatario);
		} catch (DataException de) {
			logger
					.error("ReportProtocolloDelegate: failed getting countProtocolliInLavorazione: ");
			return 0;
		}
	}

	public int countProtocolliDaScartare(Utente utente, Date dataInizio,
			Date dataFine, int ufficioId) {
		try {
			return reportProtocolloDAO.countProtocolliDaScartare(utente,
					ufficioId, dataInizio, dataFine);
		} catch (DataException de) {
			logger
					.error("ReportProtocolloDelegate: failed getting countProtocolliDaScartare: ");
			return 0;
		}
	}

	public Collection<ReportProtocolloView>  stampaProtocolliInLavorazione(Utente utente,
			java.util.Date dataInizio, java.util.Date dataFine, int assegnatario)
			throws DataException {
		try {
			Organizzazione org = Organizzazione.getInstance();
			Ufficio uff = org.getUfficio(assegnatario);
			return reportProtocolloDAO.getProtocolliInLavorazione(utente
					.getRegistroInUso(), dataInizio, dataFine, uff
					.getValueObject(), assegnatario);
		} catch (DataException de) {
			logger
					.error("ReportProtocolloDelegate: failed getting getProtocolliInLavorazione: ");
			throw new DataException("Errore nella lettura dei dati");
		}
	}

	public Collection<ReportProtocolloView>  cercaProtocolliDaScartare(Utente utente, int titolarioId,
			Date dataInizio, Date dataFine) throws DataException {
		try {
			return reportProtocolloDAO.cercaProtocolliDaScartare(utente,
					titolarioId, dataInizio, dataFine);
		} catch (DataException de) {
			logger
					.error("ReportProtocolloDelegate: failed getting cercaProtocolliDaScartare: ");
			throw new DataException("Errore nella lettura dei dati");
		}
	}

	public int countProtocolliScaricati(Utente utente, Date dataInizio,
			Date dataFine, int assegnatario) {
		try {
			Organizzazione org = Organizzazione.getInstance();
			Ufficio uff = org.getUfficio(assegnatario);
			return reportProtocolloDAO.countProtocolliScaricati(utente
					.getRegistroInUso(), dataInizio, dataFine, uff
					.getValueObject(), assegnatario);
		} catch (DataException de) {
			logger
					.error("ReportProtocolloDelegate: failed getting countProtocolliScaricati: ");
			return 0;
		}
	}

	public Collection<ReportProtocolloView>  stampaProtocolliScaricati(int registroId,
			java.util.Date dataInizio, java.util.Date dataFine, int assegnatario)
			throws DataException {
		try {
			Organizzazione org = Organizzazione.getInstance();
			Ufficio uff = org.getUfficio(assegnatario);
			return reportProtocolloDAO.getProtocolliScaricati(registroId,
					dataInizio, dataFine, uff.getValueObject(), assegnatario);
		} catch (DataException de) {
			logger
					.error("ReportProtocolloDelegate: failed getting getProtocolliScaricati: ");
			throw new DataException("Errore nella lettura dei dati");
		}
	}

	public int countProtocolliAssegnati(Utente utente, int ufficioId,int caricaId,int anno)
			throws Exception {
		try {
			Organizzazione org = Organizzazione.getInstance();
			Ufficio uff = org.getUfficio(ufficioId);
			return reportProtocolloDAO.countProtocolliAssegnati(utente
					.getRegistroInUso(), uff.getValueObject(), ufficioId,caricaId,anno);
		} catch (DataException de) {
			logger.error("ReportProtocolloDelegate: failed getting countProtocolliAssegnati: ");
			throw new Exception("Errore nella lettura dei dati");
		}
	}

	public Collection<ReportProtocolloView>  stampaProtocolliAssegnati(Utente utente, int ufficioId,int caricaId,int anno)
			throws Exception {
		try {
			Organizzazione org = Organizzazione.getInstance();
			Ufficio uff = org.getUfficio(ufficioId);
			return reportProtocolloDAO.getProtocolliAssegnati(utente
					.getRegistroInUso(), uff.getValueObject(), ufficioId,caricaId,anno);
		} catch (DataException de) {
			logger
					.error("ReportProtocolloDelegate: failed getting stampaProtocolliAssegnati: ");
			throw new Exception("Errore nella lettura dei dati");
		}
	}

	public int countProtocolliAnnullati(Utente utente, int ufficioId)
			throws Exception {
		try {
			Organizzazione org = Organizzazione.getInstance();
			Ufficio uff = org.getUfficio(ufficioId);
			return reportProtocolloDAO.countProtocolliAnnullati(utente
					.getRegistroInUso(), uff.getValueObject(), ufficioId);
		} catch (DataException de) {
			logger
					.error("ReportProtocolloDelegate: failed getting countProtocolliAnnullati: ");
			throw new Exception("Errore nella lettura dei dati");

		}
	}

	public Collection<ReportProtocolloView> stampaProtocolliAnnullati(Utente utente, int ufficioId)
			throws Exception {

		try {
			Organizzazione org = Organizzazione.getInstance();
			Ufficio uff = org.getUfficio(ufficioId);
			return reportProtocolloDAO.getProtocolliAnnullati(utente
					.getRegistroInUso(), uff.getValueObject(), ufficioId);
		} catch (Exception de) {
			logger
					.error("ReportProtocolloDelegate: failed getting stampaProtocolliAnnullati: ");
			throw new DataException("Errore nella lettura dei dati");
		}
	}

	public Collection<ReportProtocolloView> stampaProtocolliSpediti(Utente utente, Date dataInizio,
			Date dataFine, int ufficioId, String mezzoSpedizione,
			int mezzoSpedizioneId) throws Exception {
		try {
			return reportProtocolloDAO.getProtocolliSpediti(utente
					.getRegistroInUso(), dataInizio, dataFine, ufficioId,
					mezzoSpedizione, mezzoSpedizioneId);
		} catch (DataException de) {
			logger
					.error("ReportProtocolloDelegate: failed getting stampaProtocolliSpediti: ");
			throw new DataException("Errore nella lettura dei dati");
		}

	}

	public Collection<ReportCheckPostaInternaView> stampaNotifichePostaInterna(Utente utente, Date dataInizio,
			Date dataFine) throws Exception {
		try {
			return reportProtocolloDAO.getNotifichePostaInterna(utente
					.getCaricaInUso(), dataInizio, dataFine);
		} catch (DataException de) {
			logger
					.error("ReportProtocolloDelegate: failed getting stampaProtocolliSpediti: ");
			throw new DataException("Errore nella lettura dei dati");
		}

	}
	
	public int countProtocolliSpediti(Utente utente, Date dataInizio,
			Date dataFine, int ufficioId, String mezzoSpedizione,
			int mezzoSpedizioneId) throws Exception {
		try {
			return reportProtocolloDAO.countProtocolliSpediti(utente
					.getRegistroInUso(), dataInizio, dataFine, ufficioId,
					mezzoSpedizione, mezzoSpedizioneId);
		} catch (DataException de) {
			logger
					.error("ReportProtocolloDelegate: failed getting countProtocolliAnnullati: ");
			throw new DataException("Errore nella lettura dei dati");
		}
	}

	public int countNotifichePostaInterna(Utente utente, Date dataInizio,
			Date dataFine) throws Exception {
		try {
			return reportProtocolloDAO.countNotifichePostaInterna(utente.getCaricaInUso(), dataInizio, dataFine);
		} catch (DataException de) {
			logger
					.error("ReportProtocolloDelegate: failed getting countProtocolliAnnullati: ");
			throw new DataException("Errore nella lettura dei dati");
		}
	}
	
	public double getPrezzoSpedizione(Utente utente, Date dataInizio,
			Date dataFine, int ufficioId, String mezzoSpedizione,
			int mezzoSpedizioneId) throws Exception {
		double prezzo=0;
		try {
			prezzo=reportProtocolloDAO.getPrezzoSpedizione(utente
					.getRegistroInUso(), dataInizio, dataFine, ufficioId,
					mezzoSpedizione, mezzoSpedizioneId);
		} catch (DataException de) {
			logger
					.error("ReportProtocolloDelegate: failed getting countProtocolliAnnullati: ");
			throw new DataException("Errore nella lettura dei dati");
		}
		return prezzo;
	}
	
	public int countRubrica(int aooId, String flagTipo) throws Exception {
		try {
			return reportProtocolloDAO.countRubrica(flagTipo, aooId);
		} catch (DataException de) {
			logger
					.error("ReportProtocolloDelegate: failed getting countRubrica: ");
			throw new DataException("Errore nella lettura dei dati");
		}
	}

	public String getNumeroProtocolli(int ufficioAssegnatarioId,
			Integer utenteAssegnatario, String statoProtocollo,
			String statoAssegnazione, Date dataDa, Date dataA, Utente utente)
			throws Exception {
		try {
			return String.valueOf(reportProtocolloDAO.getNumeroProtocolli(
					ufficioAssegnatarioId, utenteAssegnatario, statoProtocollo,
					statoAssegnazione, dataDa, dataA, utente));
		} catch (DataException de) {
			logger
					.error("ReportProtocolloDelegate: failed getting getNumeroProtocolli: ");
			throw new DataException("Errore nella lettura dei dati");
		}
	}

	public Collection<ReportProtocolloView> getDettaglioStatisticheProtocolli(
			int ufficioAssegnatarioId, Integer utenteAssegnatario,
			String statoProtocollo, String statoAssegnazione, Date dataDa,
			Date dataA, Utente utente) throws Exception {
		Collection<ReportProtocolloView> protocolli = new ArrayList<ReportProtocolloView>();
		try {
			protocolli = reportProtocolloDAO.getDettaglioStatisticheProtocolli(
					ufficioAssegnatarioId, utenteAssegnatario, statoProtocollo,
					statoAssegnazione, dataDa, dataA, utente);
		} catch (DataException de) {
			logger
					.error("ReportProtocolloDelegate: failed getting getNumeroProtocolli: ");
			throw new DataException("Errore nella lettura dei dati");
		}
		return protocolli;
	}

	public Collection<RubricaView> stampaRubrica(int aooId, String flagTipo)
			throws Exception {
		try {
			return reportProtocolloDAO.getRubrica(flagTipo, aooId);
		} catch (DataException de) {
			logger
					.error("ReportProtocolloDelegate: failed getting stampaRubrica: ");
			throw new DataException("Errore nella lettura dei dati");
		}

	}

	public int countStampaVeline(Utente utente, Date dataInizio, Date dataFine,
			int ufficioId,int caricaId, int numInizio,int numFine,String tipoProtocollo,int conoscenza) {
		try {
			return reportProtocolloDAO.countVeline(utente, dataInizio,
					dataFine, ufficioId,caricaId,numInizio,numFine,tipoProtocollo,conoscenza);
		} catch (DataException de) {
			logger
					.error("ReportProtocolloDelegate: failed getting countStampaVeline: ");
			return 0;
		}
	}

	public Collection<ReportProtocolloView> stampaVeline(Utente utente, Date dataInizio,
			Date dataFine, int ufficioId,int caricaId,int numInizio,int numFine,String tipoProtocollo,int conoscenza) throws DataException {
		try {
			
			return reportProtocolloDAO.getVeline(utente, dataInizio, dataFine,
					ufficioId,caricaId,numInizio,numFine,tipoProtocollo,conoscenza);
		} catch (DataException de) {
			logger.error("ReportProtocolloDelegate: failed getting stampaVeline: ");
			throw new DataException("Errore nella lettura dei dati");
		}

	}

	public Collection<AssegnatarioView> getAssegnatariVeline(Utente utente, Date dataInizio,
			Date dataFine,int numInizio,int numFine,String tipoProtocollo,int conoscenza) throws DataException {
		try {
			return reportProtocolloDAO.getStampaAssegnatariVeline(utente, dataInizio, dataFine,
					numInizio,numFine,tipoProtocollo,conoscenza);
		} catch (DataException de) {
			logger.error("ReportProtocolloDelegate: failed getting stampaVeline: ");
			throw new DataException("Errore nella lettura dei dati");
		}

	}
	
	public Collection<ReportProtocolloView> stampaProtocolloRifiutato(int protocolloId) throws DataException{
		try {
			return reportProtocolloDAO.getProtocolloRofiutato(protocolloId);
		} catch (DataException de) {
			logger.error("ReportProtocolloDelegate: failed getting stampaProtocolloRifiutato: ");
			throw new DataException("Errore nella lettura dei dati");
		}
	}
}