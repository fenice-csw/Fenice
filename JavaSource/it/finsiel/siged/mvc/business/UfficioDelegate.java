package it.finsiel.siged.mvc.business;

import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.compit.fenice.mvc.vo.organizzazione.StoricoOrganigrammaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.NomiTabelle;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.mvc.integration.UfficioDAO;
import it.finsiel.siged.mvc.presentation.helper.UtenteView;
import it.finsiel.siged.mvc.vo.organizzazione.TitolarioUfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.rdb.JDBCManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * @author G.Calli Intersiel Spa
 */
public class UfficioDelegate {

	private static Logger logger = Logger.getLogger(UfficioDelegate.class
			.getName());

	private UfficioDAO ufficioDAO = null;

	//private ServletConfig config = null;

	private static UfficioDelegate delegate = null;

	private UfficioDelegate() {
		// Connect to DAO
		try {
			if (ufficioDAO == null) {
				ufficioDAO = (UfficioDAO) DAOFactory
						.getDAO(Constants.UFFICIO_DAO_CLASS);
				logger.debug("ufficioDAO instantiated:"
						+ Constants.UFFICIO_DAO_CLASS);

			}
		} catch (Exception e) {
			logger.error("Exception while connecting to UserDAOjdbc!!", e);
		}

	}

	public static UfficioDelegate getInstance() {
		if (delegate == null)
			delegate = new UfficioDelegate();
		return delegate;
	}

	public static String getIdentifier() {
		return Constants.UFFICIO_DELEGATE;
	}

	public UfficioVO salvaUfficio(Connection connection, UfficioVO ufficioVO)
			throws Exception {
		UfficioVO uffVO = new UfficioVO();

		if (ufficioVO.getId() != null && ufficioVO.getId().intValue() > 0) {
			uffVO = ufficioDAO.modificaUfficio(connection, ufficioVO);

		} else {
			ufficioVO.setId(IdentificativiDelegate.getInstance().getNextId(
					connection, NomiTabelle.UFFICI));
			uffVO = ufficioDAO.nuovoUfficio(connection, ufficioVO);
		}
		return uffVO;

	}

	public UfficioVO salvaUfficio(UfficioVO ufficioVO, String[] cariche) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		UfficioVO uffVO = new UfficioVO();
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);

			uffVO = salvaUfficio(connection, ufficioVO);
			if (uffVO.getReturnValue() == ReturnValues.SAVED) {
				ufficioDAO.cancellaCaricheReferenti(connection, ufficioVO
						.getId().intValue());
				if (cariche != null) {
					ufficioDAO.aggiornaCaricheReferenti(connection, cariche);
				}
				if (ufficioVO.getCaricaDirigenteId() > 0) {
					ufficioDAO.cancellaCaricheDirigenti(connection, ufficioVO
							.getId().intValue());
					ufficioDAO.aggiornaCaricheDirigenti(connection, ufficioVO);
				}
				if (ufficioVO.getCaricaResponsabileUfficioProtocolloId() > 0) {
					Organizzazione org=Organizzazione.getInstance();
					if(org.getCaricaResponsabileUfficioProtocollo()==null || org.getCaricaResponsabileUfficioProtocollo()
							.getCaricaId()!=ufficioVO.getCaricaResponsabileUfficioProtocolloId()){
						CaricaDelegate.getInstance().setResponsabileProtocollo(connection, ufficioVO.getCaricaResponsabileUfficioProtocolloId());
						org.setCaricaResponsabileUfficioProtocollo(org.getCarica(ufficioVO.getCaricaResponsabileUfficioProtocolloId()));
					}
				}
			}
			connection.commit();
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Ufficio fallito, rolling back transction..",
					de);

		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Ufficio fallito, rolling back transction..",
					se);

		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);

		} finally {
			jdbcMan.close(connection);
		}
		return uffVO;
	}

	public int getNumeroReferentiByUfficio(int ufficioId) {

		int risultato = 0;
		try {

			risultato = ufficioDAO.getNumeroReferentiByUfficio(ufficioId);
			logger.info("numero utenti per ufficio: " + risultato);
		} catch (DataException de) {
			logger.error("Errore getNumeroReferentiByUfficio ");
		}

		return risultato;

	}

	public boolean cancellaUfficio(int ufficioId) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		boolean cancellato = false;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);

			if (ufficioId > 0 && ufficioDAO.isUfficioCancellabile(ufficioId)
					&& (ufficioDAO.getUfficiByParent(ufficioId)).size() == 0) {
				Collection<CaricaVO> caricheList = ufficioDAO
						.getCaricheVOByUfficio(ufficioId);
				for (CaricaVO c : caricheList) {
					CaricaDelegate.getInstance()
							.cancellaCarica(c.getCaricaId());
				}
				ufficioDAO.cancellaUfficio(connection, ufficioId);
				connection.commit();
				cancellato = true;
			}

		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.error("UfficioDelegate: failed cancellaUfficio: ");
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
		} finally {
			jdbcMan.close(connection);
		}
		return cancellato;
	}

	public Collection<UfficioVO> getUfficiByParent(int ufficioId) {

		try {
			return ufficioDAO.getUfficiByParent(ufficioId);
		} catch (DataException de) {
			logger.error("UfficioDelegate: failed getting getUfficiByParent: ");
			return null;
		}
	}

	public UfficioVO getUfficioVO(int ufficioId) {
		UfficioVO u = new UfficioVO();
		try {
			u = ufficioDAO.getUfficioVO(ufficioId);
		} catch (DataException de) {
			logger.error("UfficioDelegate: failed getting getUfficioVO: "
					+ ufficioId);
		}
		return u;
	}
	
	public UfficioVO getUfficioVOByDescrizione(String descrizione) {
		UfficioVO u = new UfficioVO();
		try {
			u = ufficioDAO.getUfficioVOByDescrizione(descrizione);
		} catch (DataException de) {
			logger.error("UfficioDelegate: failed getting getUfficioVOByDescrizione: "
					+ descrizione);
		}
		return u;
	}

	public Collection<UtenteView> getUtentiByUfficio(int ufficioId) {
		try {
			if (ufficioId > 0) {
				return ufficioDAO.getUtentiByUfficio(ufficioId);
			} else {
				return null;
			}
		} catch (DataException de) {
			logger
					.error("UfficioDelegate: failed getting getUtentiByUfficio: ");
			return null;
		}
	}

	public Collection<UtenteView> getCaricheByUfficio(int ufficioId) {
		try {
			if (ufficioId > 0) {
				return ufficioDAO.getCaricheByUfficio(ufficioId);
			} else {
				return null;
			}
		} catch (DataException de) {
			logger
					.error("UfficioDelegate: failed getting getCaricheByUfficio: ");
			return null;
		}
	}

	public Collection<TitolarioUfficioVO> getTitolarioByUfficio(int ufficioId) {
		try {
			if (ufficioId > 0) {
				return ufficioDAO.getTitolarioByUfficio(ufficioId);
			} else {
				return null;
			}
		} catch (DataException de) {
			logger
					.error("UfficioDelegate: failed getting getTitolarioByUfficio: ");
			return null;
		}
	}

	public Collection<UfficioVO> getUfficiUtente(int utenteId) {
		try {
			if (utenteId > 0) {
				return ufficioDAO.getUfficiByUtente(utenteId);
			} else {
				return new ArrayList<UfficioVO>();
			}
		} catch (DataException de) {
			logger.error("UfficioDelegate: failed getting getUfficiUtente: "
					+ utenteId);
			return new ArrayList<UfficioVO>();
		}
	}

	public Collection<UfficioVO> getUffici() {
		try {
			{
				return ufficioDAO.getUffici();
			}
		} catch (DataException de) {
			logger.error("UfficioDelegate: failed getting getUffici");
			return new ArrayList<UfficioVO>();
		}
	}

	public Collection<UfficioVO> getUffici(int aooId) {
		try {
			{
				return ufficioDAO.getUffici(aooId);
			}
		} catch (DataException de) {
			logger.error("UfficioDelegate: failed getting getUffici");
			return new ArrayList<UfficioVO>();
		}
	}

	public Collection<StoricoOrganigrammaVO> getStoricoOrganigrammaCollection(int aooId) {
		try {
			return ufficioDAO.getStoricoOrganigrammaCollection(aooId);
		} catch (DataException de) {
			logger.error("UfficioDelegate: failed getting getStoricoOrganigramma");
			return new ArrayList<StoricoOrganigrammaVO>();
		}
	}
	
	public StoricoOrganigrammaVO getStoricoOrganigramma(int storgId) {
		StoricoOrganigrammaVO vo=new StoricoOrganigrammaVO();
		try {
			vo= ufficioDAO.getStoricoOrganigramma(storgId);
		} catch (DataException de) {
			logger.error("UfficioDelegate: failed getting getStoricoOrganigrammaCollection");
			
		}
		return vo;
	}
	
	public String[] getReferentiByUfficio(int ufficioId) {
		String[] referenti = null;
		try {
			referenti = ufficioDAO.getReferentiByUfficio(ufficioId);
		} catch (DataException de) {
			logger.error("", de);
		}
		return referenti;
	}

	public boolean isCaricaReferenteUfficio(int caricaId) throws DataException {
		boolean isReferente = false;
		try {
			isReferente = ufficioDAO.isCaricaReferenteUfficio(caricaId);
		} catch (DataException de) {
			logger.error("", de);
		}
		return isReferente;

	}

	public boolean isUfficioAttivo(int id) {
		boolean attivo = false;
		try {
			attivo = ufficioDAO.isUfficioAttivo(id);
		} catch (DataException de) {
			logger.error("", de);
		}
		return attivo;
	}

	public boolean removeUfficioProtocollo() {
		boolean removed = false;
		try {
			removed=CaricaDelegate.getInstance().removeResponsabileProtocollo();
			if(removed)
				removed= ufficioDAO.removeUfficioProtocollo();
		} catch (DataException de) {
			logger.error("UfficioDelegate: failed getting removeUfficioProtocollo ");
		}
		return removed;
	}

	public UfficioVO setUfficioProtocollo(int ufficioId) {
		UfficioVO newVO = new UfficioVO();
		newVO.setReturnValue(ReturnValues.UNKNOWN);
		try {
			newVO= ufficioDAO.setUfficioProtocollo(ufficioId);
		} catch (DataException de) {
			logger.error("UfficioDelegate: failed getting setUfficioProtocollo ");
		}
		return newVO;
	}
	
	public boolean setUfficioAttivo(int id, boolean attivo) {
		boolean updated=false;
		try {
			updated=ufficioDAO.setUfficioAttivo(id, attivo);
		} catch (DataException de) {
			logger.error("", de);
		}
		return updated;
	}

	public void aggiornaUfficioDataUltimaMailRicevuta(Connection connection,
			int ufficioId,Date dataSpedizione) throws DataException {
		ufficioDAO.aggiornaUfficioDataUltimaMailRicevuta(connection, ufficioId, dataSpedizione);
		
	}
	
	public void salvaStoricoOrganigramma(StoricoOrganigrammaVO vo) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			Integer id=IdentificativiDelegate.getInstance().getNextId(connection, NomiTabelle.STORICO_ORGANIGRAMMA);
			ufficioDAO.salvaStoricoOrganigramma(connection,id, vo);
		} catch (DataException de) {
			//jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Storico Organigramma fallito, rolling back transction..",
					de);

		} catch (SQLException se) {
			jdbcMan.rollback(connection);
			logger.warn(
					"Salvataggio Storico Organigramma fallito, rolling back transction..",
					se);

		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);

		} finally {
			jdbcMan.close(connection);
		}
	}
}