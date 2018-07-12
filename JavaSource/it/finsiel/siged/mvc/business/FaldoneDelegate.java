package it.finsiel.siged.mvc.business;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.NomiTabelle;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.Faldone;
import it.finsiel.siged.model.Fascicolo;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.integration.FaldoneDAO;
import it.finsiel.siged.mvc.integration.FascicoloDAO;
import it.finsiel.siged.mvc.integration.ProcedimentoDAO;
import it.finsiel.siged.mvc.presentation.helper.FaldoneView;
import it.finsiel.siged.mvc.vo.protocollo.FaldoneVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.ProcedimentoVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.StringUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedMap;

import org.apache.log4j.Logger;

public class FaldoneDelegate {

    private static Logger logger = Logger.getLogger(FaldoneDelegate.class
            .getName());

    private int status;

    private FaldoneDAO faldoneDAO = null;

    private FascicoloDAO fascicoloDAO = null;

    private ProcedimentoDAO procedimentoDAO = null;


    private static FaldoneDelegate delegate = null;

    private FaldoneDelegate() {
        // Connect to DAO
        try {
            if (faldoneDAO == null) {
                faldoneDAO = (FaldoneDAO) DAOFactory
                        .getDAO(Constants.FALDONE_DAO_CLASS);
                logger.debug("faldoneDAO instantiated:"
                        + Constants.FALDONE_DAO_CLASS);
            }
        } catch (Exception e) {
            logger.error("Exception while connecting to UserDAOjdbc!!", e);
        }
        try {
            if (fascicoloDAO == null) {
                fascicoloDAO = (FascicoloDAO) DAOFactory
                        .getDAO(Constants.FASCICOLO_DAO_CLASS);
                logger.debug("fascicoloDAO instantiated:"
                        + Constants.FASCICOLO_DAO_CLASS);
            }
        } catch (Exception e) {
            logger.error("Exception while connecting to UserDAOjdbc!!", e);
        }
        try {
            if (procedimentoDAO == null) {
                procedimentoDAO = (ProcedimentoDAO) DAOFactory
                        .getDAO(Constants.PROCEDIMENTO_DAO_CLASS);
                logger.debug("procedimentoDAO instantiated:"
                        + Constants.PROCEDIMENTO_DAO_CLASS);
            }
        } catch (Exception e) {
            logger.error("Exception while connecting to UserDAOjdbc!!", e);
        }

    }

    public static FaldoneDelegate getInstance() {
        if (delegate == null)
            delegate = new FaldoneDelegate();
        return delegate;
    }

    public static String getIdentifier() {
        return Constants.FALDONE_DELEGATE;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int s) {
        this.status = s;
    }

    public Collection<FaldoneVO> getFaldoniPerAoo(int aoo_id) {
        try {
            return faldoneDAO.getFaldoniPerAoo(aoo_id);
        } catch (Exception de) {
            logger.error("FaldoneDelegate: failed getting getFaldoniPerAoo: ");
            return null;
        }
    }
/*
    public Collection getStatiFaldone() {
        try {
            return faldoneDAO.getStatiFaldone();
        } catch (Exception de) {
            logger.error("FaldoneDelegate: failed getting getStatiFaldone: ");

            return null;
        }
    }

    public IdentityVO getStatoFaldone(int statoId) {
        try {
            return faldoneDAO.getStatoFaldone(statoId);
        } catch (Exception de) {
            logger.error("FaldoneDelegate: failed getting getStatiFaldone: ");

            return null;
        }
    }
*/
    public synchronized FaldoneVO salvaFaldone(Faldone faldone, Utente utente) {
        FaldoneVO faldoneSalvato = new FaldoneVO();
        JDBCManager jdbcMan = null;
        Connection connection = null;
        try {
            jdbcMan = new JDBCManager();
            connection = jdbcMan.getConnection();
            connection.setAutoCommit(false);
            FaldoneVO faldoneVO = faldone.getFaldoneVO();
            if (faldoneVO.getId() != null && faldoneVO.getId().intValue() > 0) {
                faldoneVO.setRowUpdatedUser(utente.getValueObject()
                        .getUsername());
                faldoneSalvato = faldoneDAO
                        .updateFaldone(connection, faldoneVO);
                if (faldoneSalvato.getReturnValue() == ReturnValues.SAVED) {
                    faldoneDAO.cancellaFaldoneFascicoli(connection, faldoneVO
                            .getId().intValue());
                    faldoneDAO.cancellaFaldoneProcedimenti(connection,
                            faldoneVO.getId().intValue());
                }

            } else {
                faldoneVO.setAnno(DateUtil.getYear(new Date(System
                        .currentTimeMillis())));
                faldoneVO.setRowCreatedUser(utente.getValueObject()
                        .getUsername());
                faldoneVO
                        .setRowCreatedTime(new Date(System.currentTimeMillis()));
                faldoneVO.setId(IdentificativiDelegate.getInstance().getNextId(
                        connection, NomiTabelle.FALDONI));

                Date dataCorrente = new Date(System.currentTimeMillis());
                int nextNum = IdentificativiDelegate.getInstance().getNextId(
                        connection,
                        NomiTabelle.FALDONI
                                + String.valueOf(faldoneVO.getAooId())
                                + +DateUtil.getYear(dataCorrente));
                faldoneVO.setNumero(nextNum);
                faldoneVO.setNumeroFaldone(String.valueOf(DateUtil
                        .getYear(new Date(System.currentTimeMillis())))
                        + StringUtil.formattaNumeroFaldone(String
                                .valueOf(nextNum), 6));

                faldoneSalvato = faldoneDAO.newFaldone(connection, faldoneVO);

            }
            if (!faldone.getFascicoli().isEmpty()) {
                Iterator<Fascicolo> it = faldone.getFascicoli().iterator();
                while (it.hasNext()) {
                    Fascicolo f = (Fascicolo) it.next();
                    FascicoloVO fascicoloVO = fascicoloDAO.getFascicoloById(
                            connection, f.getFascicoloVO().getId().intValue());
                    faldoneDAO.insertFaldoneFascicolo(connection, fascicoloVO,
                            faldoneVO.getId().intValue(), utente
                                    .getValueObject().getUsername());
                }
            }
            if (!faldone.getProcedimenti().isEmpty()) {
                Iterator<ProcedimentoVO> it = faldone.getProcedimenti().iterator();
                while (it.hasNext()) {
                    ProcedimentoVO p = (ProcedimentoVO) it.next();
                    faldoneDAO.insertFaldoneProcedimento(connection, p.getId()
                            .intValue(), faldoneVO.getId().intValue(), utente
                            .getValueObject().getUsername());
                }
            }
            connection.commit();
            faldoneSalvato.setReturnValue(ReturnValues.SAVED);
        } catch (DataException de) {
            faldoneSalvato.setReturnValue(ReturnValues.UNKNOWN);
            jdbcMan.rollback(connection);
            logger.warn(
                    "Salvataggio Faldone fallito, rolling back transction..",
                    de);

        } catch (SQLException se) {
            faldoneSalvato.setReturnValue(ReturnValues.UNKNOWN);
            jdbcMan.rollback(connection);
            logger.warn(
                    "Salvataggio Faldone fallito, rolling back transction..",
                    se);

        } catch (Exception e) {
            faldoneSalvato.setReturnValue(ReturnValues.UNKNOWN);
            jdbcMan.rollback(connection);
            logger.warn("Si e' verificata un eccezione non gestita.", e);

        } finally {
            jdbcMan.close(connection);
        }
        return faldoneSalvato;
    }

    public SortedMap<Integer,FaldoneView> cercaFaldoni(Utente utente, String ufficiUtenti,
            HashMap<String,String> sqlDB) {
        try {
            return faldoneDAO.cercaFaldoni(utente, ufficiUtenti, sqlDB);
        } catch (DataException e) {
            logger.error("", e);
        }
        return null;
    }

    public int contaFaldoni(Utente utente, String ufficiUtenti, HashMap<String,String> sqlDB) {
        try {

            return faldoneDAO.contaFaldoni(utente, ufficiUtenti, sqlDB);
        } catch (DataException de) {
            logger
                    .error("ProtocolloDelegate: failed getting contaProtocolli: ");
            return 0;
        }

    }

    public FaldoneVO getFaldone(int faldoneId) {
        try {
            return faldoneDAO.getFaldone(faldoneId);
        } catch (Exception de) {
            logger.error("FaldoneDelegate: failed getting getFaldone: ", de);
            return null;
        }
    }

    public FaldoneVO getFaldone(Connection connection, int faldoneId) {
        try {
            return faldoneDAO.getFaldone(connection, faldoneId);
        } catch (Exception de) {
            logger.error("FaldoneDelegate: failed getting getFaldone: ", de);
            return null;
        }
    }

    public Collection<FaldoneView> getStoriaFaldone(int faldoneId) {
        try {
            return faldoneDAO.getStoriaFaldone(faldoneId);
        } catch (DataException de) {
            logger
                    .error("StoriaFaldoneDelegate: failed getting getStoriaFaldone: ");
            return null;
        }
    }

    public FaldoneVO getFaldoneByIdVersione(int faldoneId, int versione) {

        try {
            FaldoneVO f = new FaldoneVO();
            f = faldoneDAO.getFaldoneByIdVersione(faldoneId, versione);
            return f;
        } catch (Exception de) {
            logger.error("FaldoneDelegate: failed getting getFaldoneById: ");
            return null;
        }
    }

    public Collection<Integer> getFaldoneFascicoliIds(int faldoneId) {
        Collection<Integer> lista = new ArrayList<Integer>();
        JDBCManager jdbcMan = null;
        Connection connection = null;
        try {
            jdbcMan = new JDBCManager();
            connection = jdbcMan.getConnection();
            lista = faldoneDAO.getFaldoneFascicoliIds(connection, faldoneId);
        } catch (DataException de) {
            jdbcMan.rollback(connection);
            logger.warn(
                    "Salvataggio Faldone fallito, rolling back transction..",
                    de);
        } catch (SQLException se) {
            jdbcMan.rollback(connection);
            logger.warn(
                    "Salvataggio Faldone fallito, rolling back transction..",
                    se);
        } catch (Exception e) {
            jdbcMan.rollback(connection);
            logger.warn("Si e' verificata un eccezione non gestita.", e);
        } finally {
            jdbcMan.close(connection);
        }
        return lista;
    }

    public Collection<Integer> getFaldoneProcedimentiIds(int faldoneId) {
        Collection<Integer> lista = new ArrayList<Integer>();
        JDBCManager jdbcMan = null;
        Connection connection = null;
        try {
            jdbcMan = new JDBCManager();
            connection = jdbcMan.getConnection();
            lista = faldoneDAO.getFaldoneProcedimentiIds(connection, faldoneId);
        } catch (DataException de) {
            jdbcMan.rollback(connection);
            logger.warn(
                    "Salvataggio Faldone fallito, rolling back transction..",
                    de);
        } catch (SQLException se) {
            jdbcMan.rollback(connection);
            logger.warn(
                    "Salvataggio Faldone fallito, rolling back transction..",
                    se);
        } catch (Exception e) {
            jdbcMan.rollback(connection);
            logger.warn("Si e' verificata un eccezione non gestita.", e);
        } finally {
            jdbcMan.close(connection);
        }
        return lista;
    }

    public int cancellaFaldone(int faldoneId) {
        return faldoneDAO.cancellaFaldone(faldoneId);
    }

}