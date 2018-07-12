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
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.flosslab.mvc.presentation.integration.MittentiDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

public class MittentiDelegate {

	private static Logger logger = Logger.getLogger(MittentiDelegate.class
			.getName());

	private MittentiDAO mittentiDAO = null;

	private static MittentiDelegate delegate = null;

	private MittentiDelegate() {
		// Connect to DAO
		try {
			if (mittentiDAO == null) {
				mittentiDAO = (MittentiDAO) DAOFactory
						.getDAO(Constants.MITTENTI_DAO_CLASS);

				logger.debug("oggettoDAO instantiated:"
						+ Constants.MITTENTI_DAO_CLASS);

			}
		} catch (Exception e) {
			logger.error("Exception while connecting to UserDAOjdbc!!", e);
		}

	}

	public static MittentiDelegate getInstance() {
		if (delegate == null)
			delegate = new MittentiDelegate();
		return delegate;
	}

	public static String getIdentifier() {
		return Constants.MITTENTI_DELEGATE;
	}

	public void salvaMittenti1(Connection conn, ProtocolloVO protocollo)
			throws DataException, SQLException {
		int idProtocollo = new Integer(protocollo.getId());

		List<SoggettoVO> mittenti = protocollo.getMittenti();
		if (mittenti != null) {
			for (SoggettoVO mittente : mittenti) {
				int idMittente = IdentificativiDelegate.getInstance()
						.getNextId(conn, NomiTabelle.PROTOCOLLO_MITTENTI);
				mittentiDAO.saveMittente(conn, mittente, idProtocollo,
						idMittente, protocollo.getVersione());
			}
		}

	}

	public List<SoggettoVO> getMittenti(int protocolloId) {
		JDBCManager manager = null;
		Connection conn = null;
		List<SoggettoVO> mittenti = null;
		try {
			manager = new JDBCManager();
			conn = manager.getConnection();
			mittenti = mittentiDAO.getMittenti(conn, protocolloId);
		} catch (Exception e) {
			logger.error("errore getMittenti:" + e.getMessage());
			e.printStackTrace();
		} finally {
			manager.close(conn);
		}
		return mittenti;

	}

	public List<SoggettoVO> getStoriaMittenti(int protocolloId, int versione)
			throws SQLException, DataException {
		JDBCManager manager = new JDBCManager();
		Connection conn = manager.getConnection();
		List<SoggettoVO> mittenti = mittentiDAO.getStoriaMittenti(conn, protocolloId, versione);
		manager.close(conn);
		return mittenti;
	}

	public void salvaMittente(Connection conn, SoggettoVO mittente,
			ProtocolloVO protocollo, int idMittente, int versione)
			throws DataException, SQLException {
		int idProtocollo = new Integer(protocollo.getId());
		mittentiDAO.saveMittente(conn, mittente, idProtocollo, idMittente, versione);
	}
}