package it.finsiel.siged.mvc.business;

import it.compit.fenice.mvc.presentation.helper.TitolarioView;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.NomiTabelle;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.integration.TitolarioDAO;
import it.finsiel.siged.mvc.vo.lookup.TitolarioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.rdb.JDBCManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class TitolarioDelegate {

	private static Logger logger = Logger.getLogger(TitolarioDelegate.class
			.getName());

	private TitolarioDAO titolarioDAO = null;

	//private ServletConfig config = null;

	private static TitolarioDelegate delegate = null;

	private TitolarioDelegate() {
		// Connect to DAO
		try {
			if (titolarioDAO == null) {
				titolarioDAO = (TitolarioDAO) DAOFactory
						.getDAO(Constants.TITOLARIO_DAO_CLASS);

				logger.debug("soggettoDAO instantiated:"
						+ Constants.TITOLARIO_DAO_CLASS);

			}
		} catch (Exception e) {
			logger.error("Exception while connecting to UserDAOjdbc!!", e);
		}

	}

	public static TitolarioDelegate getInstance() {
		if (delegate == null)
			delegate = new TitolarioDelegate();
		return delegate;
	}

	public static String getIdentifier() {
		return Constants.TITOLARIO_DELEGATE;
	}

	public Collection<TitolarioVO> getListaTitolario(int aoo, String codice,
			String descrizione) {
		try {
			return titolarioDAO.getListaTitolario(aoo, codice, descrizione);
		} catch (DataException de) {
			logger
					.error("TitolarioDelegate: failed getting getListaTitolario: ");
			return null;
		}
	}

	public Collection<TitolarioVO> getTitolariByParent(int ufficioId, int parentId, int aooId) {
		try {
			if(ufficioId==0)
				return titolarioDAO.getTitolariByParent(parentId, aooId);
			else
				return titolarioDAO.getTitolariByParent(ufficioId, parentId, aooId);
		} catch (DataException de) {
			logger
					.error("TitolarioDelegate: failed getting getTitolariByParent");
			return null;
		}
	}

	public Collection<TitolarioVO> getTitolariByParent(int parentId, int aooId) {
		try {
			return titolarioDAO.getTitolariByParent(parentId, aooId);
		} catch (DataException de) {
			logger
					.error("TitolarioDelegate: failed getting getTitolariByParent");
			return null;
		}
	}

	public String[] getUfficiTitolario(int titolarioId) throws DataException {
		try {
			return titolarioDAO.getUfficiTitolario(titolarioId);
		} catch (DataException de) {
			logger
					.error("TitolarioDelegate: failed getting getUfficiTitolario: ");
			return null;
		}

	}

	public TitolarioVO getTitolario(int ufficioId, int titolarioId, int aooId) {
		try {
			return titolarioDAO.getTitolario(ufficioId, titolarioId, aooId);
		} catch (DataException de) {
			logger.error("TitolarioDelegate: failed getting getTitolario: ");
			return null;
		}
	}

	public TitolarioVO getTitolario(int titolarioId) {
		try {
			return titolarioDAO.getTitolario(titolarioId);
		} catch (DataException de) {
			logger.error("TitolarioDelegate: failed getting getTitolario: ");
			return null;
		}
	}
	
	public TitolarioVO getTitolarioByUfficio(int ufficioId) {
		try {
			return titolarioDAO.getTitolarioByUfficio(ufficioId);
		} catch (DataException de) {
			logger.error("TitolarioDelegate: failed getting getTitolarioByUfficio: ");
			return null;
		}
	}

	public int controlloTitolarioByDescrizioneEdUfficio(int ufficioId,
			String descrizione) throws DataException {
		try {
			return titolarioDAO.controlloTitolarioByDescrizioneEdUfficio(
					ufficioId, descrizione);
		} catch (DataException de) {
			logger.error("TitolarioDelegate: failed getting getTitolario: ");
			return 0;
		}
	}

	public boolean getArgomentoProtocolli(int titolarioId) throws DataException {
		try {
			return titolarioDAO.getArgomentoProtocolli(titolarioId);
		} catch (DataException de) {
			logger.error("TitolarioDelegate: failed getting getTitolario: ");
			return false;
		}
	}

	public void salvaArgomento(TitolarioVO titolarioVO) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		//TitolarioVO titolarioSalvato = new TitolarioVO();
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);

			if (titolarioVO.getId() != null
					&& titolarioVO.getId().intValue() > 0) {
				titolarioVO.setFullPathDescription(getPathName(titolarioVO));
				titolarioDAO.updateArgomento(connection,
						titolarioVO);
			} else {
				titolarioVO.setId(IdentificativiDelegate.getInstance()
						.getNextId(connection, NomiTabelle.TITOLARIO));
				titolarioDAO.newArgomento(connection,
						titolarioVO);
			}
			connection.commit();
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.error("TitolarioDelegate: failed salvaArgomento: ");
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
		} catch (Exception e) {
			jdbcMan.rollback(connection);
			logger.error("TitolarioDelegate: failed salvaArgomento: ");
		} finally {
			jdbcMan.close(connection);
		}
	}

	/*
	 * private Map salvaRiferimenti(Connection connection, TitolarioVO
	 * titolario, Map allegati) throws Exception { DocumentoDelegate
	 * documentoDelegate = DocumentoDelegate.getInstance(); int titolarioId =
	 * titolario.getId().intValue(); Iterator iterator =
	 * allegati.values().iterator(); HashMap docs = new HashMap(2);
	 * titolarioDAO.deleteRiferimenti(connection, titolarioId); while
	 * (iterator.hasNext()) { DocumentoVO doc = (DocumentoVO) iterator.next();
	 * int idx = doc.getIdx();// salvo l'indice corrente if (doc != null) { //
	 * il documento se nuovo non ha Id o ha il flag su true if (doc.getId() ==
	 * null || doc.isMustCreateNew()) { // salvo il documento doc =
	 * documentoDelegate.salvaDocumento(connection, doc); } doc.setIdx(idx);//
	 * ripristino l'indice chiave TitolarioBO.putAllegato(doc, docs); // salvo
	 * l'allegato nella tabella delle referenze
	 * titolarioDAO.salvaRiferimenti(connection, IdentificativiDelegate
	 * .getInstance().getNextId(connection,
	 * NomiTabelle.RIFERIMENTI_LEGISLATIVI), titolarioId,
	 * doc.getId().intValue(), titolario.getVersione()); } } return docs; }
	 */

	public boolean cancellaArgomento(int titolarioId) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		boolean cancellato = false;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);

			if (titolarioId > 0) {
				if (removeAlberoArgomento(connection, titolarioId)) {
					connection.commit();
					cancellato = true;
				}
			}

		} catch (SQLException se) {
			jdbcMan.rollback(connection);
		} finally {
			jdbcMan.close(connection);
		}
		return cancellato;
	}

	private boolean removeAlberoArgomento(Connection connection, int titolarioId) {
		boolean eseguito = false;
		try {
			if (!getArgomentoProtocolli(titolarioId)) {
				Collection<TitolarioVO> titolariFigli = getTitolariByParent(titolarioId, 0);
				for (Iterator<TitolarioVO> i = titolariFigli.iterator(); i.hasNext();) {
					TitolarioVO children = (TitolarioVO) i.next();
					int id = children.getId().intValue();
					removeAlberoArgomento(connection, id);
					if (!getArgomentoProtocolli(id)) {
						titolarioDAO.deleteArgomento(connection, children
								.getId().intValue());
					} else {
						throw new DataException(
								"argomento non cancellabile perch� associato ad un protocollo.");

					}
				}
				titolarioDAO.deleteArgomento(connection, titolarioId);
				eseguito = true;
			} else {
				throw new DataException(
						"argomento non cancellabile perch� associato ad un protocollo.");
			}

		} catch (DataException de) {
			logger.error("TitolarioDelegate: failed removeAlberoArgomento: ");
		}
		return eseguito;
	}

	public int associaTitolarioUffici(TitolarioVO titolario, String[] uffici,
			int aooId) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		int returnValues = ReturnValues.UNKNOWN;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			
			//Collection ufficiDaCancellare = ciSonoUfficiDaCancellare(connection, titolario.getId().intValue(), uffici);
			/*
			if (!ufficiDaCancellare.isEmpty()) {
				if (!titolarioDAO.isTitolarioCancellabileUfficio(connection,
						titolario.getId().intValue(), ufficiDaCancellare))
					returnValues = ReturnValues.FOUND;
				else
					returnValues = associaTitolarioUffici(connection,
							titolario, uffici, aooId);
			} else */
				returnValues = associaTitolarioUffici(connection, titolario,
						uffici, aooId);
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.error("TitolarioDelegate: failed associaTitolarioUffici: ");
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
		} finally {
			jdbcMan.close(connection);
		}
		return returnValues;
	}

	public int associaTitolarioUffici(Connection connection,
			TitolarioVO titolario, String[] uffici, int aooId)
			throws DataException, SQLException {
		int returnValues = ReturnValues.UNKNOWN;
		try {
			connection.setAutoCommit(false);
			titolarioDAO.deleteArgomentoUffici(connection, titolario.getId()
					.intValue());
			if (uffici != null) {
				for (int i = 0; i < uffici.length; i++) {
					if (uffici[i] != null) {
						int ufficioId = new Integer(uffici[i]).intValue();
						titolarioDAO.deleteArgomentoUfficio(connection,
								ufficioId, titolario.getId().intValue());
						titolarioDAO.associaArgomentoUfficio(connection,
								titolario, ufficioId);
						//TitolarioVO titolarioPadre = titolario;
						/*
						 * while (titolarioPadre.getParentId() > 0) {
						 * titolarioPadre =
						 * getTitolario(titolarioPadre.getParentId());
						 * titolarioDAO.deleteArgomentoUfficio(connection,
						 * ufficioId, titolarioPadre.getId().intValue());
						 * titolarioDAO.associaArgomentoUfficio(connection,
						 * titolarioPadre, ufficioId); }
						 */
					}
				}
			} else {
			}

			connection.commit();
		} catch (DataException de) {
			logger.error("TitolarioDelegate: failed associaTitolarioUffici: ");
			throw new DataException(
					"TitolarioDelegate: failed associaTitolarioUffici.");
		} catch (SQLException se) {
			logger.error("TitolarioDelegate: failed associaTitolarioUffici: ");
			throw new SQLException(
					"TitolarioDelegate: failed associaTitolarioUffici.");
		}

		returnValues = ReturnValues.SAVED;
		return returnValues;
	}

	public int associaUfficiArgomento(Connection connection,
			TitolarioVO titolario, String[] uffici) throws DataException,
			SQLException {
		int returnValues = ReturnValues.UNKNOWN;
		try {
			connection.setAutoCommit(false);

			titolarioDAO.deleteArgomentoUffici(connection, titolario.getId()
					.intValue());

			titolarioDAO.associaArgomentoUffici(connection, titolario, uffici);

			while (titolario.getParentId() > 0) {
				titolario = getTitolario(titolario.getParentId());
				titolarioDAO.deleteArgomentoUffici(connection, titolario
						.getId().intValue());
				titolarioDAO.associaArgomentoUffici(connection, titolario,
						uffici);
			}
			connection.commit();
		} catch (DataException de) {
			logger.error("TitolarioDelegate: failed associaUfficiArgomento: ");
			throw new DataException(
					"TitolarioDelegate: failed associaUfficiArgomento.");
		} catch (SQLException se) {
			logger.error("TitolarioDelegate: failed associaUfficiArgomento: ");
			throw new SQLException(
					"TitolarioDelegate: failed associaUfficiArgomento.");
		}

		returnValues = ReturnValues.SAVED;
		return returnValues;
	}

	public int associaTuttiGliUfficiTitolario(TitolarioVO titolario, int aooId) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		int returnValues = ReturnValues.UNKNOWN;

		Collection<UfficioVO> ufficiAoo = AreaOrganizzativaDelegate
				.getInstance().getUffici(aooId);
		String[] uffici = new String[ufficiAoo.size()];
		Iterator<UfficioVO> iterator = ufficiAoo.iterator();

		int ind = 0;
		for (Iterator<UfficioVO> i = iterator; i.hasNext();) {
			UfficioVO uff = (UfficioVO) i.next();
			uffici[ind] = String.valueOf(uff.getId().intValue());
			ind = ind + 1;
		}
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			// titolarioDAO.deleteArgomentoUffici(connection, titolario.getId()
			// .intValue());
			TitolarioVO titolarioPadre = titolario;
			for (int i = 0; i < uffici.length; i++) {
				if (uffici[i] != null) {
					int ufficioId = new Integer(uffici[i]).intValue();
					titolarioDAO.deleteArgomentoUfficio(connection, ufficioId,
							titolario.getId().intValue());
					titolarioDAO.associaArgomentoUfficio(connection, titolario,
							ufficioId);

					while (titolario.getParentId() > 0) {
						titolario = getTitolario(titolario.getParentId());
						titolarioDAO.deleteArgomentoUfficio(connection,
								ufficioId, titolario.getId().intValue());
						titolarioDAO.associaArgomentoUfficio(connection,
								titolario, ufficioId);
					}
					titolario = titolarioPadre;
				}
			}
			returnValues = ReturnValues.SAVED;
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger
					.error("TitolarioDelegate: failed associaTuttiGliUfficiTitolario: ");
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
		} finally {
			jdbcMan.close(connection);
		}
		return returnValues;

	}

	public int associaUfficiArgomento(TitolarioVO titolario, String[] uffici) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		int returnValues = ReturnValues.UNKNOWN;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			Collection<Integer> ufficiDaCancellare = ciSonoUfficiDaCancellare(
					connection, titolario.getId().intValue(), uffici);
			if (!ufficiDaCancellare.isEmpty()) {
				if (!titolarioDAO.isTitolarioCancellabileUfficio(connection,
						titolario.getId().intValue(), ufficiDaCancellare))
					returnValues = ReturnValues.FOUND;
				else
					returnValues = associaUfficiArgomento(connection,
							titolario, uffici);
			} else
				returnValues = associaUfficiArgomento(connection, titolario,
						uffici);
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.error("TitolarioDelegate: failed associaUfficiArgomento: ");
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
		} finally {
			jdbcMan.close(connection);
		}
		return returnValues;
	}

	private Collection<Integer> ciSonoUfficiDaCancellare(Connection connection,
			int titolarioId, String[] ufficiSelezionati) {
		Collection<Integer> ufficiDaCancellare = null;
		String uffici = "";
		if (ufficiSelezionati != null) {
			for (int i = 0; i < ufficiSelezionati.length; i++) {
				int ufficioSelezionatoId = new Integer(ufficiSelezionati[i])
						.intValue();
				uffici += "" + ufficioSelezionatoId + ",";
			}
			uffici = uffici.substring(0, uffici.length() - 1);
			uffici.replaceAll("null", "");
		}
		try {
			ufficiDaCancellare = titolarioDAO.getUfficiDaCancellare(connection,
					titolarioId, uffici);
		} catch (DataException e) {
			logger
					.error("TitolarioDelegate: errore in ciSonoUfficiDaCancellare(): ");
		}

		return ufficiDaCancellare;
	}

	public Collection<TitolarioVO> getCategorie(int servizioId) {
		try {
			return titolarioDAO.getCategorie(servizioId);
		} catch (DataException de) {
			logger.error("TitolarioDelegate: failed getting getCategorie: ");
			return null;
		}
	}

	public int getUfficioByServizio(int servizioId) {
		try {
			return titolarioDAO.getUfficioByServizio(servizioId);
		} catch (DataException de) {
			logger
					.error("TitolarioDelegate: failed getting getUfficioByservizio: ");
			return 0;
		}
	}

	public String getPathName(TitolarioVO titolario) {
		String titolarioPath = titolario.getDescrizione();
		while (titolario.getParentId() > 0) {
			titolario = getTitolario(titolario.getParentId());
			titolarioPath = titolario.getDescrizione() + "/" + titolarioPath;
		}
		return titolarioPath;
	}

	public Collection<TitolarioVO> getStoriaTitolario(int titolarioId) {
		try {
			return titolarioDAO.getStoriaTitolarioById(titolarioId);
		} catch (DataException de) {
			logger
					.error("TitolarioDelegate: failed getting getStoriaTitolario: ");
			return null;
		}
	}

	public boolean controlloPermessiUffici(int parentIdOld, int parentIdNew,
			int aooId) {
		boolean returnValue = true;
		try {
			String[] ufficiAssociati = titolarioDAO
					.getUfficiTitolario(parentIdOld);

			for (int i = 0; i < ufficiAssociati.length; i++) {
				if (ufficiAssociati[i] != null) {
					int ufficioId = new Integer(ufficiAssociati[i]).intValue();
					if (getTitolario(ufficioId, parentIdNew, aooId) == null) {
						returnValue = false;
						break;
					}
				}

			}

		} catch (DataException de) {
			logger
					.error("TitolarioDelegate: failed getting controlloPermessiUffici: ");

		}
		return returnValue;
	}

	public boolean salvaArgomenti(List<TitolarioVO> toSave) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		jdbcMan = new JDBCManager();
		boolean result = true;
		try {

			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			titolarioDAO.deleteAll(connection);
			titolarioDAO.deleteReferenceTitolario(connection);
			for (TitolarioVO titolario : toSave) {
				titolario.setId(IdentificativiDelegate.getInstance().getNextId(
						connection, NomiTabelle.TITOLARIO));
				titolarioDAO.newArgomento(connection, titolario);
			}
			connection.commit();
		} catch (SQLException e) {
			jdbcMan.rollback(connection);
			e.printStackTrace();
			result = false;
		} catch (DataException e) {
			jdbcMan.rollback(connection);
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	public Collection<TitolarioView> stampaTitolario(int aooId) throws Exception{
		try {
			return titolarioDAO.stampaTitolario(aooId);
		} catch (DataException de) {
			logger
					.error("TitolarioDelegate: failed getting stampaTitolario: ");
			throw new Exception("Errore nella lettura dei dati");
		}
	}

	

}