package it.finsiel.siged.mvc.business;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.NomiTabelle;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.integration.AnnotazioneDAO;
import it.finsiel.siged.mvc.vo.protocollo.AnnotazioneVO;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class AnnotazioneDelegate implements ComponentStatus {

    private static Logger logger = Logger.getLogger(AnnotazioneDelegate.class
            .getName());

    private int status;

    private AnnotazioneDAO annotazioneDAO = null;

    private static AnnotazioneDelegate delegate = null;

    private AnnotazioneDelegate() {
        try {
            if (annotazioneDAO == null) {
                annotazioneDAO = (AnnotazioneDAO) DAOFactory
                        .getDAO(Constants.ANNOTAZIONE_DAO_CLASS);

                logger.debug("UserDAO instantiated: "
                        + Constants.ANNOTAZIONE_DAO_CLASS);
                status = STATUS_OK;
            }
        } catch (Exception e) {
            status = STATUS_ERROR;
            logger.error("", e);
        }

    }

    public static AnnotazioneDelegate getInstance() {
        if (delegate == null)
            delegate = new AnnotazioneDelegate();
        return delegate;
    }

    public static String getIdentifier() {
        return Constants.ANNOTAZIONE_DELEGATE;
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

    public AnnotazioneVO newAnnotazioneVO(AnnotazioneVO c) throws DataException {
        return annotazioneDAO.newAnnotazioneVO(c);

    }

    public AnnotazioneVO getAnnotazione(int id) {
        AnnotazioneVO ann = null;
        try {
            ann = annotazioneDAO.getAnnotazione(id);
            logger.info("getting annotazione id: " + ann.getId());
        } catch (DataException de) {
            logger.error("Failed getting Annotazione: " + id);
        }
        return ann;
    }

    public AnnotazioneVO save(AnnotazioneVO u) throws DataException {
        AnnotazioneVO[] vos = new AnnotazioneVO[1];
        return vos[0];
    }

    public HashMap<Integer,AnnotazioneVO> salvaAnnotazioni(Connection connection, int protocolloId,
            HashMap<Integer,AnnotazioneVO> annotazioni) throws DataException {

        HashMap<Integer,AnnotazioneVO> newAnnotazioni = new HashMap<Integer,AnnotazioneVO>(2);

        if (annotazioni == null || annotazioni.size() == 0)
            return newAnnotazioni;

        // salvo o creo documenti sulla base dati
        Iterator<AnnotazioneVO> iterator = annotazioni.values().iterator();
        while (iterator.hasNext()) {
            AnnotazioneVO ann = iterator.next();
            if (ann.getId() != null) { // update
                annotazioneDAO.updateAnnotazioneVO(connection, ann);
            } else { // new
                ann.setId(IdentificativiDelegate.getInstance().getNextId(
                        connection, NomiTabelle.ANNOTAZIONI));
                annotazioneDAO.newAnnotazioneVO(connection, protocolloId, ann);
            }
            // newAnnotazioni.put(newAnn.getPath(),newAnn);
        }
        return newAnnotazioni;

    }

    public ArrayList<AnnotazioneVO> getAnnotazioni(int protocolloId) {

        try {
            return annotazioneDAO.getArrayAnnotazioneVO(protocolloId);
        } catch (DataException de) {
            logger
                    .error("AnnotazioneDelegate: failed getting getAnnotazioni: ");
            return null;
        }
    }

}