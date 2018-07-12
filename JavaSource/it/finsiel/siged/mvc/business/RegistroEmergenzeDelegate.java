package it.finsiel.siged.mvc.business;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.integration.RegistroEmergenzaDAO;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.presentation.helper.SessioniEmergenzaView;

import java.util.Collection;

import org.apache.log4j.Logger;

public class RegistroEmergenzeDelegate implements ComponentStatus {

    private static Logger logger = Logger
            .getLogger(RegistroEmergenzeDelegate.class.getName());

    private int status;

    private RegistroEmergenzaDAO registroEmergenzaDAO = null;

   // private ServletConfig config = null;

    private static RegistroEmergenzeDelegate delegate = null;

    private RegistroEmergenzeDelegate() {
        try {
            if (registroEmergenzaDAO == null) {
                registroEmergenzaDAO = (RegistroEmergenzaDAO) DAOFactory
                        .getDAO(Constants.REGISTRO_EMERGENZE_DAO_CLASS);

                logger.debug("RegistroEmergenzaDAO instantiated:"
                        + Constants.REGISTRO_EMERGENZE_DAO_CLASS);
                status = STATUS_OK;
            }
        } catch (Exception e) {
            status = STATUS_ERROR;
            logger.error("", e);
        }

    }

    public static RegistroEmergenzeDelegate getInstance() {
        if (delegate == null)
            delegate = new RegistroEmergenzeDelegate();
        return delegate;
    }

    public static String getIdentifier() {
        return Constants.REGISTRO_EMERGENZE_DELEGATE;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int s) {
        this.status = s;
    }

    public Collection<ReportProtocolloView> getProtocolliPrenotati(int registroId)
            throws DataException {
        return registroEmergenzaDAO.getProtocolliPrenotati(registroId);
    }

    public Collection<SessioniEmergenzaView> getSessioniAperte(int registroId)
    		throws DataException {
    	return registroEmergenzaDAO.getSessioniAperte(registroId);
    }
    
    public int getNumeroProtocolliPrenotati(int registroId)
            throws DataException {
        return registroEmergenzaDAO.getNumeroProtocolliPrenotati(registroId);
    }

}
