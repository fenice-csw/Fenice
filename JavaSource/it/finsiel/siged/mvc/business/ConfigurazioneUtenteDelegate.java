package it.finsiel.siged.mvc.business;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.integration.ConfigurazioneUtenteDAO;
import it.finsiel.siged.mvc.vo.protocollo.ConfigurazioneUtenteVO;
import it.finsiel.siged.rdb.JDBCManager;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class ConfigurazioneUtenteDelegate {

    private static Logger logger = Logger.getLogger(FaldoneDelegate.class
            .getName());

    private int status;

    private ConfigurazioneUtenteDAO configurazioneUtenteDAO = null;


    private static ConfigurazioneUtenteDelegate delegate = null;

    private ConfigurazioneUtenteDelegate() {
        // Connect to DAO
        try {
            if (configurazioneUtenteDAO == null) {
                configurazioneUtenteDAO = (ConfigurazioneUtenteDAO) DAOFactory
                        .getDAO(Constants.CONFIGURAZIONEUTENTE_DAO_CLASS);
                logger.debug("configurazioneUtenteDAO instantiated:"
                        + Constants.CONFIGURAZIONEUTENTE_DAO_CLASS);
            }
        } catch (Exception e) {
            logger.error("Exception while connecting to UserDAOjdbc!!", e);
        }
    }

    public ConfigurazioneUtenteVO salvaConfigurazione(
            ConfigurazioneUtenteVO vo, int utenteId) throws Exception {
        JDBCManager jdbcMan = null;
        Connection connection = null;
        try {
            jdbcMan = new JDBCManager();
            connection = jdbcMan.getConnection();
            connection.setAutoCommit(false);
            vo = salvaConfigurazione(connection, vo, utenteId);
            connection.commit();
        } catch (DataException de) {
            jdbcMan.rollback(connection);
            logger
                    .warn(
                            "Salvataggio Configurazione fallito, rolling back transction..",
                            de);
        } catch (SQLException se) {
            jdbcMan.rollback(connection);
            logger
                    .warn(
                            "Salvataggio Configurazione fallito, rolling back transction..",
                            se);
        } catch (Exception e) {
            jdbcMan.rollback(connection);
            logger
                    .warn(
                            "Salvataggio Configurazione - si e' verificata un eccezione non gestita.",
                            e);
        } finally {
            jdbcMan.close(connection);
        }
        return vo;
    }

    public static ConfigurazioneUtenteDelegate getInstance() {
        if (delegate == null)
            delegate = new ConfigurazioneUtenteDelegate();
        return delegate;
    }

    public static String getIdentifier() {
        return Constants.CONFIGURAZIONE_UTENTE_DELEGATE;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int s) {
        this.status = s;
    }

    public ConfigurazioneUtenteVO getConfigurazione(int utenteId) {
        try {
            return configurazioneUtenteDAO.getConfigurazione(utenteId);
        } catch (Exception de) {
            logger
                    .error("ConfigurazioneUtenteDelegate: failed getting getConfigurazione: ");
            return null;
        }
    }

    public ConfigurazioneUtenteVO aggiornaParametriStampante(
            ConfigurazioneUtenteVO vo) throws Exception {
        JDBCManager jdbcMan = null;
        Connection connection = null;
        try {
            jdbcMan = new JDBCManager();
            connection = jdbcMan.getConnection();
            connection.setAutoCommit(false);
            vo = configurazioneUtenteDAO.aggiornaParametriStampante(connection,
                    vo);
            connection.commit();
        } catch (DataException de) {
            jdbcMan.rollback(connection);
            logger
                    .warn(
                            "Aggiorna Parametri Stampante fallito, rolling back transction..",
                            de);
        } catch (SQLException se) {
            jdbcMan.rollback(connection);
            logger
                    .warn(
                            "Aggiorna Parametri Stampante  fallito, rolling back transction..",
                            se);
        } catch (Exception e) {
            jdbcMan.rollback(connection);
            logger
                    .warn(
                            "Aggiorna Parametri Stampante  - si e' verificata un eccezione non gestita.",
                            e);
        } finally {
            jdbcMan.close(connection);
        }
        return vo;
    }

    public ConfigurazioneUtenteVO salvaConfigurazione(Connection connection,
            ConfigurazioneUtenteVO vo, int utenteId) throws Exception {
        if (vo != null && vo.getUtenteId() > 0) {
            vo = configurazioneUtenteDAO.aggiornaConfigurazione(connection, vo);
        } else {
            vo.setUtenteId(utenteId);
            configurazioneUtenteDAO.salvaConfigurazione(connection, vo);
            vo.setReturnValue(ReturnValues.FOUND);
        }
        return vo;
    }

}