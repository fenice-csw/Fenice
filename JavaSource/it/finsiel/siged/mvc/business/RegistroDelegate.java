package it.finsiel.siged.mvc.business;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.NomiTabelle;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.integration.RegistroDAO;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;
import it.finsiel.siged.rdb.JDBCManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

public class RegistroDelegate {

    private static Logger logger = Logger.getLogger(RegistroDelegate.class
            .getName());

    private RegistroDAO registroDAO = null;

    //private ServletConfig config = null;

    private static RegistroDelegate delegate = null;

    private RegistroDelegate() {
        try {
            if (registroDAO == null) {
                registroDAO = (RegistroDAO) DAOFactory
                        .getDAO(Constants.REGISTRO_DAO_CLASS);
                logger.debug("UserDAO instantiated:"
                        + Constants.REGISTRO_DAO_CLASS);
            }
        } catch (Exception e) {
            logger.error("Exception while connecting to "
                    + Constants.REGISTRO_DAO_CLASS + "!!", e);
        }

    }

    public static RegistroDelegate getInstance() {
        if (delegate == null)
            delegate = new RegistroDelegate();
        return delegate;
    }

    public static String getIdentifier() {
        return Constants.REGISTRO_DELEGATE;
    }

    /**
     * Carica tutti i registri.
     */
    public Collection<RegistroVO> getRegistri() {
        Collection<RegistroVO> registri = null;
        try {
            registri = registroDAO.getRegistri();
            logger.info("getting registri");
        } catch (DataException de) {
            logger.error("Failed getting Registri");
        }
        return registri;
    }

    /**
     * Carica tutti i registri di una AOO.
     */

    public Collection<RegistroVO> getRegistriByAooId(int aooId) {
        Collection<RegistroVO> registri = null;
        try {
            registri = registroDAO.getRegistriByAooId(aooId);
            logger.info("getting registri della AOO");
        } catch (DataException de) {
            logger.error("Failed getting Registri della AOO");
        }
        return registri;
    }

    public Map<Integer,RegistroVO> getRegistriUtente(int utenteId) {
    	Map<Integer,RegistroVO> registri=null;
    	 try {
    		 registri = new HashMap<Integer,RegistroVO>(2);
    		 for (Iterator<Integer> i = registroDAO.getRegistriUtente(utenteId).iterator(); i.hasNext();) {
    			 Integer registroId = (Integer) i.next();
    			 RegistroVO reg = registroDAO.getRegistro(registroId.intValue());
    			 if (reg != null) {
    				 registri.put(registroId, reg);
    			 }
    		 }
    	 } catch (DataException de) {
             logger.error("Failed getRegistriUtente");
         }
        return registri;
    }

    public RegistroVO aggiornaStatoRegistro(RegistroVO reg) {
        RegistroVO registro = new RegistroVO();
        try {
            registro = registroDAO.aggiornaStatoRegistro(reg);
            logger.info("saving registro");
        } catch (DataException de) {
            logger.error("Failed save Registro");
        }
        return registro;
    }

    public void setDataAperturaRegistro(int registroId, long data) {
        try {
            registroDAO.setDataAperturaRegistro(registroId, data);
            logger.info("getting registri");
        } catch (DataException de) {
            logger.error("Failed getting Registri");
        }
    }

    /**
     * Carica i dati del registro selezionato.
     */

    public RegistroVO getRegistroById(int id) {
        RegistroVO registro = new RegistroVO();
        try {
            registro = registroDAO.getRegistro(id);
            logger.info("getting registri");
        } catch (DataException de) {
            logger.error("Failed getting Registri");
        }
        return registro;
    }

    public RegistroVO getRegistroByCodAndAooId(String cod,int aooId) {
        RegistroVO registro = new RegistroVO();
        ;
        try {
            registro = registroDAO.getRegistroByCodAndAooId(cod,aooId);
            logger.info("getting registri");
        } catch (DataException de) {
            logger.error("Failed getting Registri");
        }
        return registro;
    }
    
    /**
     * Carica tutti gli utenti abilitati al registri.
     */
    public Map<Integer,Integer> getUtentiRegistro(int id) {
    	Map<Integer,Integer> utenti = new HashMap<Integer,Integer>();
        try {
            utenti = registroDAO.getUtentiRegistro(id);
            logger.info("getting getUtentiRegistro");
        } catch (DataException de) {
            logger.error("Failed getting getUtentiRegistro");
        }
        return utenti;
    }

    /**
     * Carica tutti gli utenti non utilizzati nel registri.
     */
    public Collection<Integer> getUtentiNonUtilizzatiInRegistro(int id, String cognome) {
        Collection<Integer> utenti = new ArrayList<Integer>();
        try {
            utenti = registroDAO.getUtentiNonUtilizzatiInRegistro(id, cognome);
            logger.info("getting getUtentiNonUtilizzatiInRegistro");
        } catch (DataException de) {
            logger.error("Failed getting getUtentiNonUtilizzatiInRegistro");
        }
        return utenti;
    }

    public int aggiungiPermessiUtenti(String[] utenti, int registroId,
            Utente utente) {
        JDBCManager jdbcMan = null;
        Connection connection = null;
        int statusFlag = ReturnValues.UNKNOWN;
        int utenteId = 0;
        logger.info("RegistroDelegate:aggiungiPermessiUtenti");
        try {
            jdbcMan = new JDBCManager();
            connection = jdbcMan.getConnection();
            connection.setAutoCommit(false);

            for (int i = 0; i < utenti.length; i++) {
                utenteId = (new Integer(utenti[i])).intValue();
                UtenteDelegate.getInstance().permessiRegistriUtente(connection,
                        utenteId, registroId, utente);
            }
            connection.commit();
            statusFlag = ReturnValues.SAVED;
        } catch (DataException de) {
            jdbcMan.rollback(connection);
            logger.error("RegistroDelegate: failed aggiungiPermessiUtenti: ");
        } catch (SQLException se) {
            jdbcMan.rollback(connection);
        } finally {
            jdbcMan.close(connection);

        }
        return statusFlag;
    }

    public int cancellaPermessiUtenti(String[] utenti, int registroId) {
        JDBCManager jdbcMan = null;
        Connection connection = null;
        int statusFlag = ReturnValues.UNKNOWN;
        logger.info("RegistroDelegate:cancellaPermessiUtenti");
        try {
            jdbcMan = new JDBCManager();
            connection = jdbcMan.getConnection();
            connection.setAutoCommit(false);
            String strUte = "(" + utenti[0];
            for (int i = 1; i < utenti.length; i++) {
                strUte += ',' + utenti[i];
            }
            strUte += ')';
            if (getCountProtocolliUtenteRegistro(strUte, registroId) == 0) {
                registroDAO.cancellaPermessiRegistroUtente(connection,
                        registroId, strUte);
                connection.commit();
                statusFlag = ReturnValues.SAVED;
            } else {
                statusFlag = ReturnValues.FOUND;
            }
        } catch (DataException de) {
            jdbcMan.rollback(connection);
            logger.error("RegistroDelegate: failed cancellaPermessiUtenti: ");
        } catch (SQLException se) {
            jdbcMan.rollback(connection);
        } finally {
            jdbcMan.close(connection);

        }
        return statusFlag;
    }

    private int getCountProtocolliUtenteRegistro(String utenti, int registro)
            throws DataException {
        int totale = 0;
        try {
            totale = registroDAO.getCountProtocolliUtenteRegistro(utenti,
                    registro);
            logger.info("getting getCountProtocolliUtenteRegistro");

        } catch (DataException de) {
            logger.error("Failed getting getCountProtocolliUtenteRegistro");
        }
        return totale;

    }

    public RegistroVO salvaRegistro(Connection connection, RegistroVO registro)
            throws Exception {
        RegistroVO registroSalvato = null;
        if (registro.getId().intValue() == 0) {
            registro.setId(IdentificativiDelegate.getInstance().getNextId(
                    connection, NomiTabelle.REGISTRI));
            registroSalvato = registroDAO.newRegistro(connection, registro);
        } else {
            registroSalvato = registroDAO.updateRegistro(connection, registro);
        }
        return registroSalvato;
    }

    public RegistroVO salvaRegistro(RegistroVO registro) {
        JDBCManager jdbcMan = null;
        Connection connection = null;
        RegistroVO registroSalvato = null;
        try {
            jdbcMan = new JDBCManager();
            connection = jdbcMan.getConnection();
            connection.setAutoCommit(false);
            registroSalvato = salvaRegistro(connection, registro);
            connection.commit();

        } catch (DataException de) {
            jdbcMan.rollback(connection);
            logger.warn(
                    "Salvataggio Registro fallito, rolling back transction..",
                    de);

        } catch (SQLException se) {
            jdbcMan.rollback(connection);
            logger.warn(
                    "Salvataggio Registro fallito, rolling back transction..",
                    se);

        } catch (Exception e) {
            jdbcMan.rollback(connection);
            logger.warn("Si e' verificata un eccezione non gestita.", e);

        } finally {
            jdbcMan.close(connection);

        }
        return registroSalvato;
    }

    public boolean cancellaRegistro(int registroId) {
        try {
            return registroDAO.cancellaRegistro(registroId);
        } catch (Exception de) {
            logger.error("RegistroDelegate: failed cancellaPermessiUtenti: ");
        }
        return false;
    }

    public boolean isAbilitatoRegistro(int registroId, int ufficioId,
            int utenteId) {
        try {
            return registroDAO.isAbilitatoRegistro(registroId, ufficioId,
                    utenteId);
        } catch (Exception de) {
            logger.error("RegistroDelegate: failed isAbilitatoRegistro: ");
        }
        return false;

    }

    public boolean esisteRegistroUfficialeByAooId(int registroInModifica,
            int aooId) {
        try {
            return registroDAO.esisteRegistroUfficialeByAooId(
                    registroInModifica, aooId);
        } catch (Exception de) {
            logger
                    .error("RegistroDelegate: failed esisteRegistroUfficialeByAooId: ");
        }
        return false;
    }

	public boolean esisteRegistroCodiceByAooId(int id,String codice, int aooId) {
		 try {
	            return registroDAO.esisteCodiceRegistro(id,codice, aooId);
	        } catch (Exception de) {
	            logger
	                    .error("RegistroDelegate: failed esisteRegistroUfficialeByAooId: ");
	        }
	        return false;
	}

}