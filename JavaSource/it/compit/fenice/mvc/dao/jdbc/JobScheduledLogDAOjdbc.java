package it.compit.fenice.mvc.dao.jdbc;

import it.compit.fenice.mvc.integration.JobScheduledLogDAO;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.vo.log.EventoVO;
import it.finsiel.siged.rdb.JDBCManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

public class JobScheduledLogDAOjdbc implements JobScheduledLogDAO {

	static Logger logger = Logger.getLogger(JobScheduledLogDAOjdbc.class.getName());

	private JDBCManager jdbcMan = new JDBCManager();


	public final static String INSERT_LOG = "INSERT INTO job_scheduled_logs(js_id, message, status, aoo_id, data_log) VALUES (?, ?, ?, ?, ?);";

	public final static String SELECT_LOGS = "SELECT * FROM job_scheduled_logs WHERE aoo_id=?";
	
	public final static String SELECT_LOG_BY_ID = "SELECT * FROM job_scheduled_logs WHERE js_id=?";
	
	public final static String SELECT_LOGS_BY_STATUS = "SELECT * FROM job_scheduled_logs WHERE aoo_id=? AND status=? ORDER BY data_log DESC";

	public final static String COUNT_LOGS_BY_STATUS = "SELECT count(*) FROM job_scheduled_logs WHERE aoo_id=? AND status=?";
	
	private static final String UPDATE_STATUS = "UPDATE job_scheduled_logs SET  status=?, message=?  WHERE js_id=?";

	private final static String SELECT_DOCUMENTO_BY_DATE_LOG = "SELECT js_id FROM job_scheduled_logs WHERE data_log = ?";

	@Override
	public Collection<EventoVO> getJobScheduledLogs(int aooId)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		EventoVO vo = null;
		ArrayList<EventoVO> logs = new ArrayList<EventoVO>();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_LOGS);
			pstmt.setInt(1, aooId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vo = new EventoVO();
				vo.setEventoId(rs.getInt("js_id"));
				vo.setMessage(rs.getString("message"));
				vo.setData(rs.getDate("data_log"));
				vo.setStatus(rs.getInt("status"));
				vo.setAooId(rs.getInt("aoo_id"));
				logs.add(vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getJobScheduledLogs", e);
			throw new DataException("Cannot load getJobScheduledLogs");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return logs;
	}

	@Override
	public EventoVO getJobScheduledLogById(int id)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		EventoVO vo = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_LOG_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				vo = new EventoVO();
				vo.setEventoId(rs.getInt("js_id"));
				vo.setMessage(rs.getString("message"));
				vo.setData(rs.getDate("data_log"));
				vo.setStatus(rs.getInt("status"));
				vo.setAooId(rs.getInt("aoo_id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getJobScheduledLogById", e);
			throw new DataException("Cannot load getJobScheduledLogById");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return vo;
	}
	
	@Override
	public Collection<EventoVO> getJobScheduledLogsByStatus(int aooId, int status)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		EventoVO vo = null;
		ArrayList<EventoVO> logs = new ArrayList<EventoVO>();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_LOGS_BY_STATUS);
			pstmt.setInt(1, aooId);
			pstmt.setInt(2, status);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vo = new EventoVO();
				vo.setEventoId(rs.getInt("js_id"));
				vo.setMessage(rs.getString("message"));
				vo.setData(rs.getDate("data_log"));
				vo.setStatus(rs.getInt("status"));
				vo.setAooId(rs.getInt("aoo_id"));
				logs.add(vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getJobScheduledLogsByStatus", e);
			throw new DataException("Cannot load getJobScheduledLogsByStatus");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return logs;
	}
	
	@Override
	public int countJobScheduledLogsByStatus(int aooId, int status)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count=0;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(COUNT_LOGS_BY_STATUS);
			pstmt.setInt(1, aooId);
			pstmt.setInt(2, status);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("countJobScheduledLogsByStatus", e);
			throw new DataException("Cannot load countJobScheduledLogsByStatus");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return count;
	}
	
	@Override
	public EventoVO newJobScheduledLog(Connection conn,
			EventoVO vo) throws DataException {
		PreparedStatement pstmt = null;
		vo.setReturnValue(ReturnValues.UNKNOWN);
		try {
			if (conn == null) {
				logger.warn("newJobScheduledLog() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(INSERT_LOG);
			pstmt.setInt(1, vo.getEventoId());
			pstmt.setString(2, vo.getMessage());
			pstmt.setInt(3, vo.getStatus());
			pstmt.setInt(4, vo.getAooId());
			pstmt.setTimestamp(5, new Timestamp(vo.getData().getTime()));
			pstmt.executeUpdate();
			vo.setReturnValue(ReturnValues.SAVED);
		} catch (Exception e) {
			logger.error("Save JobScheduledLog", e);
			e.printStackTrace();
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return vo;
	}

	@Override
	public boolean aggiornaStatusJobScheduledLog(Connection conn, int djsId, int status, String message) throws DataException {
		PreparedStatement pstmt = null;
		boolean saved=false;
		try {
			if (conn == null) {
				logger.warn("newArgomento() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(UPDATE_STATUS);
			pstmt.setInt(1, status);
			pstmt.setString(2, message);
			pstmt.setInt(3, djsId);
			pstmt.executeUpdate();
			saved=true;
		} catch (Exception e) {
			logger.error("update JobScheduledLog Status", e);
			e.printStackTrace();
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return saved;
	}

	@Override
	public boolean isStatusJobScheduledLogPresent(Date dataLog)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean present=false;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_DOCUMENTO_BY_DATE_LOG);
			pstmt.setTimestamp(1, new Timestamp(dataLog.getTime()));
			rs = pstmt.executeQuery();
			if (rs.next()) {
				present=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("isStatusJobScheduledLogPresent", e);
			throw new DataException("Cannot load isStatusJobScheduledLogPresent");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return present;
	}
	
	
}
