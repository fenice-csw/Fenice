package it.compit.fenice.mvc.business;

import it.compit.fenice.mvc.integration.CaricaDAO;
import it.compit.fenice.mvc.presentation.helper.VersioneCaricaView;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.NomiTabelle;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.DocumentaleDelegate;
import it.finsiel.siged.mvc.business.IdentificativiDelegate;
import it.finsiel.siged.mvc.vo.documentale.CartellaVO;
import it.finsiel.siged.mvc.vo.organizzazione.PermessoMenuVO;
import it.finsiel.siged.rdb.JDBCManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.log4j.Logger;

public final class CaricaDelegate {

	private static Logger logger = Logger.getLogger(CaricaDelegate.class
			.getName());

	private CaricaDAO caricaDAO = null;

	private static CaricaDelegate delegate = null;

	private CaricaDelegate() {
		try {
			if (caricaDAO == null) {
				caricaDAO = (CaricaDAO) DAOFactory
						.getDAO(Constants.CARICA_DAO_CLASS);

				logger.debug("UserDAO instantiated:"
						+ Constants.CARICA_DAO_CLASS);
			}
		} catch (Exception e) {
			logger.error("Exception while connecting to CaricaDAOjdbc!!", e);
		}

	}

	public static CaricaDelegate getInstance() {
		if (delegate == null)
			delegate = new CaricaDelegate();
		return delegate;
	}

	public static String getIdentifier() {
		return Constants.CARICA_DELEGATE;
	}

	public CaricaVO getCarica(int id) {
		CaricaVO c = new CaricaVO();
		try {
			c = caricaDAO.getCarica(id);
			logger.info("getting carica id: " + c.getCaricaId());
		} catch (DataException de) {
			logger.error("CaricaDelegate failed getting Carica: " + id + "/");
		}
		return c;
	}

	public boolean isNomeUsed(CaricaVO carica) {
		return false;
	}

	public CaricaVO newCarica(Connection connection, CaricaVO vo,
			String[] menu, String ufficio, int aooId) throws DataException {
		CaricaVO newVO = new CaricaVO();
		newVO.setReturnValue(ReturnValues.UNKNOWN);
		vo.setCaricaId(IdentificativiDelegate.getInstance().getNextId(
				connection, NomiTabelle.CARICHE));
		newVO = caricaDAO.newCarica(connection, vo);
		cancellaPermessiCarica(connection, newVO.getCaricaId());
		aggiungiPermessiCarica(connection, newVO.getCaricaId(), menu);
		CartellaVO rootCartella = new CartellaVO();
		rootCartella.setAooId(aooId);
		rootCartella.setNome(newVO.getNome());
		rootCartella.setRoot(true);
		rootCartella.setCaricaId(newVO.getCaricaId());
		DocumentaleDelegate.getInstance().creaCartellaUtente(connection,
				rootCartella);
		return newVO;
	}

    public Collection<VersioneCaricaView> getStoriaCarica(int caricaId) {
        try {
            return caricaDAO.getStoriaCarica(caricaId);
        } catch (DataException de) {
            logger.error("CaricaDelegate: failed getting getStoriaCarica: "+de);
            return null;
        }
    }

	
	public CaricaVO newCarica(CaricaVO vo, String[] menu, String ufficio,
			int aooId) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		CaricaVO newVO = new CaricaVO();
		newVO.setReturnValue(ReturnValues.UNKNOWN);
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			newVO = newCarica(connection, vo, menu, ufficio, aooId);
			connection.commit();
			newVO.setReturnValue(ReturnValues.SAVED);
		} catch (Exception se) {
			jdbcMan.rollback(connection);
			newVO.setReturnValue(ReturnValues.UNKNOWN);
			logger.error("", se);
		} finally {
			jdbcMan.close(connection);

		}
		return newVO;
	}

	public CaricaVO updateCarica(CaricaVO vo, String[] menu, String ufficio,
			int aooId) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		CaricaVO newVO = new CaricaVO();
		newVO.setReturnValue(ReturnValues.UNKNOWN);
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			newVO = caricaDAO.updateCarica(connection, vo);
			cancellaPermessiCarica(connection, newVO.getCaricaId());
			aggiungiPermessiCarica(connection, newVO.getCaricaId(), menu);
			CartellaVO verificaCartella = DocumentaleDelegate.getInstance().getCartellaVOByCaricaId(connection, newVO.getCaricaId());
			if (verificaCartella.getReturnValue() == ReturnValues.NOT_FOUND) {
				CartellaVO rootCartella = new CartellaVO();
				rootCartella.setAooId(aooId);
				rootCartella.setNome(ufficio + " - " + newVO.getNome());
				rootCartella.setRoot(true);
				rootCartella.setCaricaId(newVO.getCaricaId());
				DocumentaleDelegate.getInstance().creaCartellaUtente(
						connection, rootCartella);
			}
			connection.commit();
			vo.setReturnValue(ReturnValues.SAVED);
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.error("", de);
		} catch (Exception se) {
			jdbcMan.rollback(connection);
			logger.error("", se);
		} finally {
			jdbcMan.close(connection);
		}
		return newVO;
	}

	public void updatePermessiCarica(Connection connection, int caricaId,
			String[] menu) throws DataException {
		cancellaPermessiCarica(connection, caricaId);
		aggiungiPermessiCarica(connection, caricaId, menu);
	}

	private void cancellaPermessiCarica(Connection conn, int caricaId)
			throws DataException {
		caricaDAO.cancellaPermessi(conn, caricaId);
	}

	private void aggiungiPermessiCarica(Connection connection, int caricaId,
			String[] menu) throws DataException {

		for (int i = 0; i < menu.length; i++) {
			PermessoMenuVO permesso = new PermessoMenuVO();
			permesso.setCaricaId(caricaId);
			permesso.setMenuId(Integer.parseInt(menu[i]));
			caricaDAO.nuovoPermessoMenu(connection, permesso);
		}

	}

	public Collection<CaricaVO> getCariche() {
		Collection<CaricaVO> cariche = null;
		try {
			cariche = caricaDAO.getCariche();
			logger.info("getting cariche");
		} catch (DataException de) {
			logger.error("UserDelegate failed getting Cariche");
		}
		return cariche;
	}

	public CaricaVO getCaricaByUtenteAndUfficio(int utenteId, int ufficioId) {
		CaricaVO c = null;
		try {
			c = caricaDAO.getCarica(utenteId, ufficioId);
			logger.info("getting carica utenteId ufficioId: " + c.getCaricaId());
		} catch (DataException de) {
			logger.error("CaricaDelegate failed getting Carica: " + utenteId
					+ "/" + ufficioId);
		}
		return c;
	}

	public boolean isCaricaAttiva(int uffId, int uteId) {
		try {
			return caricaDAO.isCaricaAttiva(uffId, uteId);

		} catch (DataException de) {
			logger.error("getCaricheUtenteIds failed.", de);
		}
		return true;
	}

	public void cancellaCarica(Integer caricaId) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			cancellaPermessiCarica(connection, caricaId);
			caricaDAO.cancellaCarica(connection, caricaId);
			connection.commit();
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.error("", de);
		} catch (Exception se) {
			jdbcMan.rollback(connection);
			logger.error("", se);
		} finally {
			jdbcMan.close(connection);
		}
	}

	public CaricaVO updateCarica(CaricaVO vo) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		CaricaVO newVO = new CaricaVO();
		newVO.setReturnValue(ReturnValues.UNKNOWN);
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			newVO = caricaDAO.updateCarica(connection, vo);
			vo.setReturnValue(ReturnValues.SAVED);
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.error("", de);
		} catch (Exception se) {
			jdbcMan.rollback(connection);
			logger.error("", se);
		} finally {
			jdbcMan.close(connection);
		}
		return newVO;
	}
	
	public int contaAssegnazioni(CaricaVO carica,int annoProtocolloDa, int annoProtocolloA,Utente utente) {
		try {
			return caricaDAO.contaAssegnazioni(carica, annoProtocolloDa, annoProtocolloA, utente);
		} catch (DataException de) {
			logger
					.error("CaricaDelegate: failed getting contaAssegnazioni: ");
			return 0;
		}
	}

	public boolean removeResponsabileEnte() {
		boolean removed = false;
		try {
			removed= caricaDAO.removeResponsabileEnte();
		} catch (DataException de) {
			logger.error("CaricaDelegate: failed getting removeResponsabileEnte: ");
			
		}
		return removed;
	}

	public CaricaVO setResponsabileEnte(int caricaId) {
		CaricaVO newVO = new CaricaVO();
		newVO.setReturnValue(ReturnValues.UNKNOWN);
		try {
			newVO= caricaDAO.setResponsabileEnte(caricaId);
		} catch (DataException de) {
			logger.error("CaricaDelegate: failed getting setResponsabileEnte: ");
		}
		return newVO;
	}
	
	public boolean removeResponsabileProtocollo() {
		boolean removed = false;
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			removed= caricaDAO.removeResponsabileProtocollo(connection);
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.error("CaricaDelegate: failed getting removeResponsabileProtocollo");	
		} catch (SQLException e) {
			logger.error("CaricaDelegate: failed getting removeResponsabileProtocollo");
		}finally {
			jdbcMan.close(connection);
		}
		return removed;
	}

	public CaricaVO setResponsabileProtocollo(int caricaId) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		CaricaVO newVO = new CaricaVO();
		newVO.setReturnValue(ReturnValues.UNKNOWN);
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			newVO= caricaDAO.setResponsabileProtocollo(connection, caricaId);
		} catch (DataException de) {
			logger.error("CaricaDelegate: failed getting setResponsabileProtocollo");
		} catch (SQLException e) {
			logger.error("CaricaDelegate: failed getting setResponsabileProtocollo");
		}finally {
			jdbcMan.close(connection);
		}
		return newVO;
	}
	
	public CaricaVO setResponsabileProtocollo(Connection connection, int caricaId) {
		CaricaVO newVO = new CaricaVO();
		newVO.setReturnValue(ReturnValues.UNKNOWN);
		try {
			caricaDAO.removeResponsabileProtocollo(connection);
			newVO= caricaDAO.setResponsabileProtocollo(connection, caricaId);
		} catch (DataException de) {
			logger.error("CaricaDelegate: failed getting setResponsabileProtocollo: ");
		}
		return newVO;
	}
	
}
