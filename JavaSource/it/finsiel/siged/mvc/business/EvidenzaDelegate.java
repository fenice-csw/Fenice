package it.finsiel.siged.mvc.business;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.integration.EvidenzaDAO;
import it.finsiel.siged.mvc.presentation.helper.FascicoloView;
import it.finsiel.siged.mvc.presentation.helper.ProcedimentoView;

import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * @author G.Calli Intersiel Spa
 */
public class EvidenzaDelegate {

    private static Logger logger = Logger.getLogger(EvidenzaDelegate.class
            .getName());

    private EvidenzaDAO evidenzaDAO = null;


    private static EvidenzaDelegate delegate = null;

    private EvidenzaDelegate() {
        try {
            if (evidenzaDAO == null) {
                evidenzaDAO = (EvidenzaDAO) DAOFactory
                        .getDAO(Constants.EVIDENZA_DAO_CLASS);
                logger.debug("EvidenzaDAO instantiated:"
                        + Constants.EVIDENZA_DAO_CLASS);

            }
        } catch (Exception e) {
            logger.error("Exception while connecting to UserDAOjdbc!!", e);
        }

    }

    public static EvidenzaDelegate getInstance() {
        if (delegate == null)
            delegate = new EvidenzaDelegate();
        return delegate;
    }

    public static String getIdentifier() {
        return Constants.EVIDENZA_DELEGATE;
    }

    public Collection<ProcedimentoView> getEvidenzeProcedimenti(Utente utente, HashMap<String,String> sqlDB) {
        try {
            return evidenzaDAO.getEvidenzeProcedimenti(utente, sqlDB);
        } catch (DataException e) {
            logger.error("EvidenzaDelegate: failed getting getEvidenze: ", e);
        }
        return null;
    }

    public Collection<FascicoloView> getEvidenzeFascicoli(Utente utente, HashMap<String,String> sqlDB) {
        try {
            return evidenzaDAO.getEvidenzeFascicoli(utente, sqlDB);
        } catch (DataException e) {
            logger.error(
                    "EvidenzaDelegate: failed getting getEvidenzeFascicoli: ",
                    e);
        }
        return null;
    }

    public int contaEvidenzeProcedimenti(Utente utente, HashMap<String,String> sqlDB) {
        try {

            return evidenzaDAO.contaEvidenzeProcedimenti(utente, sqlDB);
        } catch (DataException de) {
            logger
                    .error("ProtocolloDelegate: failed getting contaEvidenzeProcedimenti: ");
            return 0;
        }

    }

    public int contaEvidenzeFascicoli(Utente utente, HashMap<String,String> sqlDB) {
        try {

            return evidenzaDAO.contaEvidenzeFascicoli(utente, sqlDB);
        } catch (DataException de) {
            logger
                    .error("ProtocolloDelegate: failed getting contaEvidenzeFascicoli: ");
            return 0;
        }

    }
}