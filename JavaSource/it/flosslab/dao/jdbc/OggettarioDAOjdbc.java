/*
 *
 * Copyright 2008 Flosslab s.r.l. (http://www.flosslab.it) All rights reserved.
 *
 * This file is part of e-prot 1.1 software.
 * e-prot 1.1 is a free software; 
 * you can redistribute it and/or modify it
 * under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
 * 
 * See the file License for the specific language governing permissions   
 * and limitations under the License
 * 
 * 
 * 
 * Copyright 2008 Flosslab s.r.l. (http://www.flosslab.it) All rights reserved.
 * Version: e-prot 1.1
 */
package it.flosslab.dao.jdbc;

import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.flosslab.mvc.presentation.integration.OggettarioDAO;
import it.flosslab.mvc.vo.OggettoVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class OggettarioDAOjdbc implements OggettarioDAO {
	static Logger logger = Logger.getLogger(OggettarioDAOjdbc.class.getName());

	private JDBCManager jdbcMan = new JDBCManager();

	private static final String INSERT_OGGETTO = "INSERT INTO oggetti (id, descrizione,giorni_alert,aoo_id) VALUES (?,?,?,?);";
	private static final String DELETE_OGGETTO = "DELETE FROM oggetti WHERE id=?;";
	private static final String UPDATE_OGGETTO = "UPDATE oggetti set descrizione=?,giorni_alert=? WHERE id= ?";
	private static final String GET_OGGETTI = "SELECT * FROM oggetti WHERE descrizione=? AND aoo_id=?;";
	private static final String GET_OGGETTI_UPDATE = "SELECT * FROM oggetti WHERE descrizione=? AND id!=?;";
	private static final String GET_OGGETTO = "SELECT * FROM oggetti WHERE id=?;";

	public OggettoVO newOggetto(Connection conn, OggettoVO oggettoVO)
			throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int id = new Integer(oggettoVO.getOggettoId());
		try {
			if (conn == null) {
				logger.warn("newOggetto() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(GET_OGGETTI);
			pstmt.setString(1, oggettoVO.getDescrizione());
			pstmt.setInt(2,oggettoVO.getAooId());
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				pstmt.close();
				pstmt = conn.prepareStatement(INSERT_OGGETTO);
				pstmt.setInt(1, id);
				pstmt.setString(2, oggettoVO.getDescrizione());
				pstmt.setInt(3, oggettoVO.getGiorniAlert());
				pstmt.setInt(4,oggettoVO.getAooId());
				pstmt.executeUpdate();
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Save Oggetto", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return oggettoVO;
	}

	public void deleteOggetto(Connection conn, int id) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (conn == null) {
				logger.warn("deleteOggetto() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(DELETE_OGGETTO);
			pstmt.setInt(1, id);
			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Delete Titolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public OggettoVO getOggetto(int id) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OggettoVO ogg = new OggettoVO();
		Connection conn = null;
		try {
			conn = jdbcMan.getConnection();
			if (conn == null) {
				logger.warn("getOggetto() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(GET_OGGETTO);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ogg.setDescrizione(rs.getString("descrizione"));
				ogg.setOggettoId(rs.getString("id"));
				ogg.setGiorniAlert(rs.getInt("giorni_alert"));
			}
			return ogg;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("get Titolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.closeAll(rs, pstmt, conn);
		}
	}

	
	public void eliminaAssegnatari(Connection conn, String id)
			throws DataException {
		PreparedStatement pstmt = null;
		final String DELETE_ASSEGNATARI = "DELETE FROM oggetto_assegnatari WHERE oggetto_id = ?";
		try {
			pstmt = conn.prepareStatement(DELETE_ASSEGNATARI);
			pstmt.setInt(1, Integer.valueOf(id));
			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("eliminaAssegnatari", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	
	public void salvaAssegnatario(Connection connection,
			AssegnatarioVO assegnatario) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("salvaAssegnatario() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			final String INSERT_ASSEGNATARIO = "INSERT INTO oggetto_assegnatari"
					+ " (oggetto_id, ufficio_id) VALUES (?, ?)";
			if (assegnatario != null) {
				pstmt = connection.prepareStatement(INSERT_ASSEGNATARIO);
				pstmt.setInt(1, assegnatario.getId().intValue());
				pstmt.setInt(2, assegnatario.getUfficioAssegnatarioId());
				pstmt.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Save Assegnatari", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}

	}

	
	public OggettoVO updateOggetto(Connection conn, OggettoVO oggettoVO)
			throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int id = new Integer(oggettoVO.getOggettoId());
		try {
			if (conn == null) {
				logger.warn("newOggetto() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement(GET_OGGETTI_UPDATE);
			pstmt.setString(1, oggettoVO.getDescrizione());
			pstmt.setInt(2, id);
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				pstmt.close();
				pstmt = conn.prepareStatement(UPDATE_OGGETTO);
				pstmt.setString(1, oggettoVO.getDescrizione());
				pstmt.setInt(2, oggettoVO.getGiorniAlert());
				pstmt.setInt(3, id);
				pstmt.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Update Titolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return oggettoVO;
	}

	
	public Map<Integer,AssegnatarioVO> getAssegnatari(int id) throws DataException {
		Map<Integer,AssegnatarioVO> assegnatari = new HashMap<Integer,AssegnatarioVO>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection
					.prepareStatement("SELECT * FROM oggetto_assegnatari"
							+ " WHERE oggetto_id = ?");
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				AssegnatarioVO assegnatario = new AssegnatarioVO();
				assegnatario.setId(rs.getInt("oggetto_id"));
				assegnatario.setUfficioAssegnatarioId(rs.getInt("ufficio_id"));
				assegnatari.put(assegnatario.getUfficioAssegnatarioId(),assegnatario);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getAssegnatari", e);
			throw new DataException("Cannot load getAssegnatari");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return assegnatari;
	}

	public List<Integer> getUfficiAssegnatariId(int id) throws DataException {
		List<Integer> ids = new ArrayList<Integer>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection
					.prepareStatement("SELECT * FROM oggetto_assegnatari"
							+ " WHERE oggetto_id = ?");
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ids.add(rs.getInt("ufficio_id"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getAssegnatari", e);
			throw new DataException("Cannot load getAssegnatari");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return ids;
	}
	
	public OggettoVO getOggettoByDescrizione(String oggetto)
			throws DataException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OggettoVO ogg = new OggettoVO();
		try {
			conn = jdbcMan.getConnection();
			if (conn == null) {
				logger.warn("getOggetto() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn
					.prepareStatement("SELECT * FROM oggetti WHERE descrizione=?");
			pstmt.setString(1, oggetto);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ogg.setDescrizione(rs.getString("descrizione"));
				ogg.setOggettoId(rs.getString("id"));
				ogg.setGiorniAlert(rs.getInt("giorni_alert"));
			}
			return ogg;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("get Titolo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.closeAll(rs, pstmt, conn);
		}
	}

	public boolean isDescrizioneUsed(int id,String oggetto,int aooId) throws DataException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = jdbcMan.getConnection();
			if (conn == null) {
				logger.warn("getOggetto() - Invalid Connection :" + conn);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = conn.prepareStatement("SELECT * FROM oggetti WHERE descrizione=? AND aoo_id=? AND NOT id=?" );
			pstmt.setString(1, oggetto);
			pstmt.setInt(2, aooId);
			pstmt.setInt(3, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
			else 
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("isDescrizioneUsed", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.closeAll(rs, pstmt, conn);
		}
	}
	
	public boolean isOggettoAssegnatariPresent(int id) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection
					.prepareStatement("SELECT * FROM oggetto_assegnatari"
							+ " WHERE oggetto_id = ?");
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
			else 
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("isOggettoAssegnatariPresent", e);
			throw new DataException("Cannot load getAssegnatari");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
	}

};