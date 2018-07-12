package it.compit.fenice.mvc.business;

import it.compit.fenice.mvc.integration.DomandaDAO;
import it.compit.fenice.mvc.presentation.helper.DomandaView;
import it.compit.fenice.mvc.vo.protocollo.DomandaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.rdb.JDBCManager;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public final class DomandaDelegate {

	private static Logger logger = Logger.getLogger(DomandaDelegate.class
			.getName());

	private DomandaDAO domandaDAO = null;


	private static DomandaDelegate delegate = null;

	private DomandaDelegate() {
		try {
			if (domandaDAO == null) {
				domandaDAO = (DomandaDAO) DAOFactory
						.getDAO(Constants.DOMANDA_DAO_CLASS);

				logger.debug("UserDAO instantiated:"
						+ Constants.DOMANDA_DAO_CLASS);
			}
		} catch (Exception e) {
			logger.error("Exception while connecting to DomandaDAOjdbc!!", e);
		}

	}

	public static DomandaDelegate getInstance() {
		if (delegate == null)
			delegate = new DomandaDelegate();
		return delegate;
	}

	public static String getIdentifier() {
		return Constants.DOMANDA_DELEGATE;
	}

	public Map<String[], DomandaView> getDomande(int stato,int ufficioId) {
		Map<String[], DomandaView> domande = null;
		try {
			if(stato==0)
				domande = domandaDAO.getDomande(ufficioId);
			else 
				domande=domandaDAO.getDomandeByStato(stato, ufficioId);
		} catch (DataException de) {
			logger.error("DomandeDelegate failed getting domande");
		} catch (Exception de) {
			logger.error("DomandeDelegate failed getting domande");
		}
		return domande;
	}

	public List<DomandaVO> getDomandeVOByStato(int stato) {
		List<DomandaVO> domande = null;
		try {
			domande=domandaDAO.getDomandeVOByStato(stato);
		} catch (DataException de) {
			logger.error("DomandeDelegate failed getting domandeVO");
		} catch (Exception de) {
			logger.error("DomandeDelegate failed getting domandeVO");
		}
		return domande;
	}
	
	public void updateStato(int stato, String domandaId) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			domandaDAO.updateStato(connection,stato, domandaId);
		}catch (Exception de) {
			logger.error("DomandeDelegate failed getting domande");
		}finally {
			jdbcMan.close(connection);
		}
	}

	public void updateStato(Connection connection, int stato, String domandaId) {
		try {
			domandaDAO.updateStato(connection,stato, domandaId);
		} catch (DataException de) {
			logger.error("DomandeDelegate failed getting domande");
		} catch (Exception de) {
			logger.error("DomandeDelegate failed getting domande");
		}
	}

	public void newIscrizioneProtocollata(Connection connection,
			String domandaId, int protocolloId, int numeroprotocollo,
			Date dataProtocollo) {
		try {
			domandaDAO.newIscrizioneProtocollata(connection,domandaId, protocolloId,
					numeroprotocollo, dataProtocollo);
		} catch (DataException de) {
			logger.error("DomandeDelegate failed newIscrizioneProtocollata");
		} catch (Exception de) {
			logger.error("DomandeDelegate failed newIscrizioneProtocollata");
		}
	}

	public DomandaVO getDomandaById(String domandaId) {
		DomandaVO d = new DomandaVO();
		try {
			d = domandaDAO.getDomandaById(domandaId);
		} catch (DataException de) {
			logger.error("DomandeDelegate failed get Domanda By Id");
		} catch (Exception de) {
			logger.error("DomandeDelegate failed get Domanda By Id");
		}
		return d;
	}

	public Integer getProtocolloIdByDomandaId(String domandaId) {
		Integer protocolloId = null;
		try {
			protocolloId = domandaDAO.getProtocolloIdByDomandaId(domandaId);
		} catch (DataException de) {
			logger.error("DomandeDelegate failed get Protocollo Id");
		} catch (Exception de) {
			logger.error("DomandeDelegate failed failed get Protocollo Id");
		}
		return protocolloId;
	}

	public int getStato(Connection connection,String domandaId) {
		int protocolloId = 0;
		try {
			protocolloId = domandaDAO.getStato(connection,domandaId);
		} catch (DataException de) {
			logger.error("DomandeDelegate failed get Protocollo Id");
		} catch (Exception de) {
			logger.error("DomandeDelegate failed failed get Protocollo Id");
		}
		return protocolloId;
	}

	public DomandaView getDomandaViewById(String domandaId) {
		DomandaView d = new DomandaView();
		try {
			d = domandaDAO.getDomandaViewById(domandaId);
		} catch (DataException de) {
			logger.error("DomandeDelegate failed get Domanda By Id");
		} catch (Exception de) {
			logger.error("DomandeDelegate failed get Domanda By Id");
		}
		return d;
	}
}
