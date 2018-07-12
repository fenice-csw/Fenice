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
package it.flosslab.mvc.business;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.NomiTabelle;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.business.IdentificativiDelegate;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.flosslab.mvc.presentation.integration.OggettarioDAO;
import it.flosslab.mvc.vo.OggettoVO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class OggettarioDelegate {

	private static Logger logger = Logger.getLogger(OggettarioDelegate.class
			.getName());

	private OggettarioDAO oggettarioDAO = null;

	private static OggettarioDelegate delegate = null;

	private OggettarioDelegate() {
		// Connect to DAO
		try {
			if (oggettarioDAO == null) {
				oggettarioDAO = (OggettarioDAO) DAOFactory
						.getDAO(Constants.OGGETTARIO_DAO_CLASS);

				logger.debug("oggettoDAO instantiated:"
						+ Constants.OGGETTARIO_DAO_CLASS);

			}
		} catch (Exception e) {
			logger.error("Exception while connecting to UserDAOjdbc!!", e);
		}

	}

	public static OggettarioDelegate getInstance() {
		if (delegate == null)
			delegate = new OggettarioDelegate();
		return delegate;
	}

	public static String getIdentifier() {
		return Constants.OGGETTARIO_DELEGATE;
	}

	public OggettoVO salvaOggetto(OggettoVO oggettoVO) throws DataException,
			SQLException {
		JDBCManager jdbcManager = new JDBCManager();
		Connection conn = jdbcManager.getConnection();
		int id = 0;
		if (oggettoVO.getOggettoId() != null) {
			id = new Integer(oggettoVO.getOggettoId());
		}
		if (id > 0) {
			oggettoVO = oggettarioDAO.updateOggetto(conn, oggettoVO);
			oggettarioDAO.eliminaAssegnatari(conn, oggettoVO.getOggettoId());
			salvaAssegnatari(oggettoVO, conn);
		} else {
			oggettoVO.setOggettoId(IdentificativiDelegate.getInstance()
					.getNextId(conn, NomiTabelle.OGGETTI));
			oggettoVO = oggettarioDAO.newOggetto(conn, oggettoVO);
			salvaAssegnatari(oggettoVO, conn);
		}
		jdbcManager.close(conn);
		return oggettoVO;
	}

	public void salvaAssegnatari(OggettoVO oggettoVO, Connection conn) {
		Collection<AssegnatarioVO> assegnatari = oggettoVO.getAssegnatari();
		if (assegnatari != null) {
			for (Iterator<AssegnatarioVO> i = assegnatari.iterator(); i.hasNext();) {
				AssegnatarioVO assegnatario = i.next();
				assegnatario.setId(Integer.valueOf(oggettoVO.getOggettoId()));
				try {
					oggettarioDAO.salvaAssegnatario(conn, assegnatario);
				} catch (DataException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void deleteOggetto(OggettoVO oggettoVO) throws DataException,
			SQLException {
		JDBCManager jdbcManager = new JDBCManager();
		int id = new Integer(oggettoVO.getOggettoId());
		Connection conn = jdbcManager.getConnection();
		oggettarioDAO.eliminaAssegnatari(conn, String.valueOf(id));
		oggettarioDAO.deleteOggetto(conn, id);

	}

	public OggettoVO getOggetto(int id) throws DataException, SQLException {
		OggettoVO vo = oggettarioDAO.getOggetto(id);
		vo.aggiungiAssegnatari(getAssegnatari(id).values());
		return vo;

	}

	public Map<Integer,AssegnatarioVO> getAssegnatari(int id) {
		try {
			return oggettarioDAO.getAssegnatari(id);
		} catch (DataException de) {
			logger.error("OggetarioDelegate: failed getting getAssegnatari: ");
			return null;
		}
	}

	public List<Integer> getUfficiAssegnatariId(int id) {
		try {
			return oggettarioDAO.getUfficiAssegnatariId(id);
		} catch (DataException de) {
			logger.error("OggetarioDelegate: failed getting getAssegnatari: ");
			return null;
		}
	}
	
	
	public Map<Integer,AssegnatarioVO> getAssegnatari(String oggetto) {
		Map<Integer,AssegnatarioVO> assegnatari=new HashMap<Integer,AssegnatarioVO>();
		try {
			assegnatari= oggettarioDAO.getAssegnatari(Integer.valueOf(getOggettoByDescrizione(oggetto).getOggettoId()));
		} catch (DataException de) {
			logger.error("OggetarioDelegate: failed getting getAssegnatari: ");
			//return null;
		}
		return assegnatari;
	}

	public boolean isOggettoAssegnatariPresent(String oggetto) {
		boolean assegnatari=false;
		try {
			assegnatari= oggettarioDAO.isOggettoAssegnatariPresent(Integer.valueOf(getOggettoByDescrizione(oggetto).getOggettoId()));
		} catch (DataException de) {
			logger.error("OggetarioDelegate: failed getting getAssegnatari: ");
			//return null;
		}
		return assegnatari;
	}
	
	public OggettoVO getOggettoByDescrizione(String oggetto) {
		OggettoVO ogg = new OggettoVO();
		try {
			if(oggetto!=null && !oggetto.trim().equals(""))
				ogg = oggettarioDAO.getOggettoByDescrizione(oggetto);
		} catch (DataException de) {
			logger.error("OggetarioDelegate: failed getting getOggettoByDescrizione: ");

		}
		return ogg;
	}

	public boolean isDescrizioneUsed(OggettoVO oggetto) {
		try {
			int id = 0;
			if (oggetto.getOggettoId() != null)
				id = new Integer(oggetto.getOggettoId());
			return oggettarioDAO.isDescrizioneUsed(id,
					oggetto.getDescrizione(), oggetto.getAooId());
		} catch (DataException de) {
			logger.error("OggetariooDelegate: failed getting getAssegnatari: ");
			return false;
		}
	}

}