package it.compit.fenice.mvc.integration;

import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.vo.log.EventoVO;

import java.sql.Connection;
import java.util.Collection;
import java.util.Date;

public interface JobScheduledLogDAO {

	Collection<EventoVO> getJobScheduledLogs(int aooId)throws DataException;
	
	EventoVO getJobScheduledLogById(int eventoId)throws DataException;
	
	Collection<EventoVO> getJobScheduledLogsByStatus(int aooId, int status)throws DataException;
	
	int countJobScheduledLogsByStatus(int aooId, int status)throws DataException;
	
	EventoVO newJobScheduledLog(Connection connection, EventoVO vo)throws DataException;
	
	boolean aggiornaStatusJobScheduledLog(Connection connection, int djsId, int status, String message)throws DataException;

	boolean isStatusJobScheduledLogPresent(Date dataLog)throws DataException;

}
