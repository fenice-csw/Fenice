package it.compit.fenice.mvc.business;

import it.compit.fenice.mvc.integration.JobScheduledLogDAO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.NomiTabelle;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.IdentificativiDelegate;
import it.finsiel.siged.mvc.vo.log.EventoVO;
import it.finsiel.siged.rdb.JDBCManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

public final class LogDelegate {

	private static Logger logger = Logger.getLogger(LogDelegate.class
			.getName());

	private JobScheduledLogDAO jsLogDAO = null;

	private static LogDelegate delegate = null;

	private LogDelegate() {
		try {
			if (jsLogDAO == null) {
				jsLogDAO = (JobScheduledLogDAO) DAOFactory.getDAO(Constants.JOB_SCHEDULED_LOG_DAO);
				logger.debug("JobScheduledLogDAO instantiated:"+ Constants.JOB_SCHEDULED_LOG_DAO_CLASS);
			}
		} catch (Exception e) {
			logger.error("Exception while connecting to JobScheduledLogDAOjdbc!!", e);
		}

	}

	public static LogDelegate getInstance() {
		if (delegate == null)
			delegate = new LogDelegate();
		return delegate;
	}

	public static String getIdentifier() {
		return Constants.JOB_SCHEDULED_LOG_DELEGATE;
	}

	
	public void newJobScheduledLog(Utente utente, EventoVO vo) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			vo.setEventoId(IdentificativiDelegate.getInstance()
					.getNextId(connection, NomiTabelle.JOB_SCHEDULED_LOGS));
			jsLogDAO.newJobScheduledLog(connection, vo);
			connection.commit();
		} catch (DataException de) {
			de.printStackTrace();
			jdbcMan.rollback(connection);
			logger.error("LogDelegate: failed newJobScheduledLog: ");
		} catch (SQLException se) {
			se.printStackTrace();
			jdbcMan.rollback(connection);
		} catch (Exception e) {
			e.printStackTrace();
			jdbcMan.rollback(connection);
			logger.error("LogDelegate: failed newJobScheduledLog: ");
		} finally {
			jdbcMan.close(connection);
		}

	}
	
	public boolean isStatusJobScheduledLogPresent(Date dataLog) {
		boolean present=false;
		try {
			logger.info("isStatusJobScheduledLogPresent");
			present=jsLogDAO.isStatusJobScheduledLogPresent(dataLog);
		} catch (DataException de) {
			logger.error("LogDelegate failed isStatusJobScheduledLogPresent");
		}
		return present;
	}

	public boolean aggiornaStatusJobScheduledLog(int jsId, int status, String message) {
		boolean saved=false;
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			logger.info("aggiornaStatusJobScheduledLog");
			saved=jsLogDAO.aggiornaStatusJobScheduledLog(connection, jsId, status, message);
		}  catch (DataException de) {
			de.printStackTrace();
			jdbcMan.rollback(connection);
			logger.error("LogDelegate: failed aggiornaStatusJobScheduledLog: ");
		} catch (SQLException se) {
			se.printStackTrace();
			jdbcMan.rollback(connection);
		} catch (Exception e) {
			e.printStackTrace();
			jdbcMan.rollback(connection);
			logger.error("LogDelegate: failed aggiornaStatusJobScheduledLog: ");
		} finally {
			jdbcMan.close(connection);
		}
		return saved;
	}
	
	public Collection<EventoVO> getJobScheduledLogs(int aooId) {
		Collection<EventoVO> jobLogs = null;
		try {
			jobLogs = jsLogDAO.getJobScheduledLogs(aooId);
			logger.info("getJobScheduledLogs");
		} catch (DataException de) {
			logger.error("LogDelegate: failed getJobScheduledLogs");
		}
		return jobLogs;
	}
	
	public EventoVO getJobScheduledLogById(int id) {
		EventoVO log = null;
		try {
			log = jsLogDAO.getJobScheduledLogById(id);
			logger.info("getJobScheduledLogById");
		} catch (DataException de) {
			logger.error("LogDelegate: failed getJobScheduledLogById");
		}
		return log;
	}
	
	public Collection<EventoVO> getJobScheduledLogsByStatus(int aooId, int status) {
		Collection<EventoVO> jobLogs = null;
		try {
			jobLogs = jsLogDAO.getJobScheduledLogsByStatus(aooId, status);
			logger.info("getJobScheduledLogsByStatus");
		} catch (DataException de) {
			logger.error("LogDelegate: failed getJobScheduledLogsByStatus");
		}
		return jobLogs;
	}
	
	public int countJobScheduledLogsByStatus(int aooId, int status) {
		int count=0;
		try {
			count = jsLogDAO.countJobScheduledLogsByStatus(aooId, status);
			logger.info("countJobScheduledLogsByStatus");
		} catch (DataException de) {
			logger.error("LogDelegate: failed countJobScheduledLogsByStatus");
		}
		return count;
	}
}
