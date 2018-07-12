package it.compit.fenice.mvc.dao.jdbc;

import it.compit.fenice.mvc.integration.CdsDAO;
import it.compit.fenice.mvc.integration.JobScheduledLogDAO;
import it.compit.fenice.mvc.vo.cds.UtenteCdsVO;
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

public class CdsDAOjdbc implements CdsDAO {

	static Logger logger = Logger.getLogger(CdsDAOjdbc.class.getName());

	private JDBCManager jdbcMan = new JDBCManager();

	public final static String SELECT_USER = "SELECT * FROM utenti_cds";

	@Override
	public UtenteCdsVO getUtenteCds()throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UtenteCdsVO vo = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_USER);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				vo = new UtenteCdsVO();
				vo.setId(rs.getInt("cds_id"));
				vo.setUtenteId(rs.getInt("utente_id"));
				vo.setUfficioId(rs.getInt("ufficio_id"));
				vo.setCaricaId(rs.getInt("carica_id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getUtenteCds", e);
			throw new DataException("Cannot load getUtenteCds");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return vo;
	}

}
