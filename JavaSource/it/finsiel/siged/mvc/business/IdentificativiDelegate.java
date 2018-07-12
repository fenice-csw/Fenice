package it.finsiel.siged.mvc.business;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.integration.IdentificativiDAO;

import java.sql.Connection;

import org.apache.log4j.Logger;

public class IdentificativiDelegate implements ComponentStatus {

    private static Logger logger = Logger
            .getLogger(IdentificativiDelegate.class.getName());

    private int status;

    private IdentificativiDAO identificativiDAO = null;

    //private ServletConfig config = null;

    private static IdentificativiDelegate delegate = null;

    private IdentificativiDelegate() {
        try {
            if (identificativiDAO == null) {
                identificativiDAO = (IdentificativiDAO) DAOFactory
                        .getDAO(Constants.IDENTIFICATIVI_DAO_CLASS);

                logger.debug("IdentificativiDAO instantiated:"
                        + Constants.IDENTIFICATIVI_DAO_CLASS);
                status = STATUS_OK;
            }
        } catch (Exception e) {
            status = STATUS_ERROR;
            logger.error("", e);
        }

    }

    public static IdentificativiDelegate getInstance() {
        if (delegate == null)
            delegate = new IdentificativiDelegate();
        return delegate;
    }

    public static String getIdentifier() {
        return Constants.IDENTIFICATI_DELEGATE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.finsiel.siged.mvc.business.ComponentStatus#getStatus()
     */
    public int getStatus() {
        return status;
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.finsiel.siged.mvc.business.ComponentStatus#setStatus(int)
     */
    public void setStatus(int s) {
        this.status = s;
    }

    // fine metodi interfaccia

    public int getNextId(Connection connection, String tableName)
            throws DataException {
        return identificativiDAO.getNextId(connection, tableName);
    }

    public int getCurrentId(String tableName) throws DataException {
        return identificativiDAO.getCurrentId(tableName);
    }

}