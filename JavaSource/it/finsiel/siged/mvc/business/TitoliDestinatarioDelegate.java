package it.finsiel.siged.mvc.business;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.NomiTabelle;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.integration.TitoliDestinatarioDAO;
import it.finsiel.siged.mvc.vo.IdentityVO;
import it.finsiel.siged.rdb.JDBCManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.log4j.Logger;

public class TitoliDestinatarioDelegate {

    private static Logger logger = Logger
            .getLogger(TitoliDestinatarioDelegate.class.getName());

    private TitoliDestinatarioDAO titoloDAO = null;

    //private ServletConfig config = null;

    private static TitoliDestinatarioDelegate delegate = null;

    private TitoliDestinatarioDelegate() {
        // Connect to DAO
        try {
            if (titoloDAO == null) {
                titoloDAO = (TitoliDestinatarioDAO) DAOFactory
                        .getDAO(Constants.TITOLIDESTINATARIO_DAO_CLASS);

                logger.debug("titoliDestinatarioDAO instantiated:"
                        + Constants.TITOLIDESTINATARIO_DAO_CLASS);

            }
        } catch (Exception e) {
            logger
                    .error(
                            "Exception while connecting to TitoliDestinatarioDAOjdbc!!",
                            e);
        }

    }

    public static TitoliDestinatarioDelegate getInstance() {
        if (delegate == null)
            delegate = new TitoliDestinatarioDelegate();
        return delegate;
    }

    public static String getIdentifier() {
        return Constants.TITOLIDESTINATARIO_DELEGATE;
    }

    public Collection<IdentityVO> getElencoTitoliDestinatario() {
        try {
            logger.info("Siamo nel delegate.");
            return titoloDAO.getElencoTitoliDestinatario();
        } catch (DataException de) {
            logger
                    .error("TitoliDestinatarioDelegate: failed getting getElencoTitoliDestinatario: ");
            return null;
        }
    }

    public IdentityVO salvaTitoloDestinatario(Connection conn,
            IdentityVO titoloVO) throws DataException {
        if (titoloVO.getId() != null && titoloVO.getId().intValue() > 0) {
            titoloVO = titoloDAO.updateTitolo(conn, titoloVO);
        } else {
            titoloVO.setId(IdentificativiDelegate.getInstance().getNextId(conn,
                    NomiTabelle.TITOLI_DESTINATARI));
            titoloDAO.newTitoloDestinatario(conn, titoloVO);
        }
        return titoloVO;
    }

    public IdentityVO salvaTitoloDestinatario(IdentityVO titoloVO)
            throws Exception {
        JDBCManager jdbcMan = null;
        Connection connection = null;
        try {
            jdbcMan = new JDBCManager();
            connection = jdbcMan.getConnection();
            connection.setAutoCommit(false);
            titoloVO = salvaTitoloDestinatario(connection, titoloVO);
            connection.commit();
        } catch (DataException de) {
            jdbcMan.rollback(connection);
            logger
                    .warn(
                            "Salvataggio Titolo fallito, rolling back transction..",
                            de);

        } catch (SQLException se) {
            jdbcMan.rollback(connection);
            logger
                    .warn(
                            "Salvataggio Titolo fallito, rolling back transction..",
                            se);

        } catch (Exception e) {
            jdbcMan.rollback(connection);
            logger
                    .warn(
                            "Salvataggio Titolo - si e' verificata un eccezione non gestita.",
                            e);

        } finally {
            jdbcMan.close(connection);
        }
        return titoloVO;
    }

    public IdentityVO getTitoloDestinatario(int id) {
        try {
            return titoloDAO.getTitoloDestinatario(id);
        } catch (DataException de) {
            logger
                    .error("TitoliDestinatarioDelegate: failed getting getTitoloDestinatario: ");
            return null;
        }
    }

    public IdentityVO getTitoloDestinatarioDaTitolo(String titolo) {
        try {
            return titoloDAO.getTitoloDestinatarioDaTitolo(titolo);
        } catch (DataException de) {
            logger
                    .error("TitoliDestinatarioDelegate: failed getting getTitoloDestinatario: ");
            return null;
        }
    }

    public boolean esisteTitolo(String descrizione, int id) {
        boolean esiste = false;
        try {
            esiste = titoloDAO.esisteTitolo(descrizione);

        } catch (Exception de) {
            logger.error("TitoloDelegate: failed nuovoTitolo: ");
        }
        return esiste;
    }

    public void deleteTitolo(int titoloId) {
        try {
            titoloDAO.deleteTitolo(titoloId);
        } catch (DataException de) {
            logger
                    .error("TitoliDestinatarioDelegate: failed getting deleteTitolo: ");
        }
    }

}