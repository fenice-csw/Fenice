package it.finsiel.siged.dao;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class DAOFactory {
    private static Logger logger = Logger.getLogger(DAOFactory.class.getName());

    private static ResourceBundle bundle;
    
    private static String DAO_CONFIG = "DAOConfig";

    /**
	 * @return the dAO_CONFIG
	 */
	public static String getDAO_CONFIG() {
		return DAO_CONFIG;
	}

	/**
	 * @param dao_config the dAO_CONFIG to set
	 */
	public static void setConfig(String dao_config) {
		DAO_CONFIG = dao_config;
	}

	static {
        try {
            bundle = ResourceBundle.getBundle(DAO_CONFIG);
        } catch (RuntimeException e) {
            logger.error("DAOConfig.properties non trovato.", e);
        }
    }

    public static Object getDAO(String className) throws DAOException {
        Object dao = null;
        String daoClassName = bundle.getString(className);
        try {
            dao = (Object) Class.forName(daoClassName).newInstance();
        } catch (Throwable e) {
            String errorMessage = "Errore nella creazione del DAO: "
                    + className;
            logger.error(errorMessage, e);
            throw new DAOException(errorMessage, e);
        }
        return dao;
    }
}
