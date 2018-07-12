package it.finsiel.siged.mvc.business;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.NomiTabelle;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.integration.SoggettoDAO;
import it.finsiel.siged.mvc.vo.IdentityVO;
import it.finsiel.siged.mvc.vo.ListaDistribuzioneVO;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.lookup.StatoCivileVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.rdb.JDBCManager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class SoggettoDelegate {

    private static Logger logger = Logger.getLogger(SoggettoDelegate.class
            .getName());

    private SoggettoDAO soggettoDAO = null;

    //private ServletConfig config = null;

    private static SoggettoDelegate delegate = null;

    private SoggettoDelegate() {
        // Connect to DAO
        try {
            if (soggettoDAO == null) {
                soggettoDAO = (SoggettoDAO) DAOFactory
                        .getDAO(Constants.SOGGETTO_DAO_CLASS);
                logger.debug("soggettoDAO instantiated:"
                        + Constants.SOGGETTO_DAO_CLASS);

            }
        } catch (Exception e) {
            logger.error("Exception while connecting to UserDAOjdbc!!", e);
        }

    }

    public static SoggettoDelegate getInstance() {
        if (delegate == null)
            delegate = new SoggettoDelegate();
        return delegate;
    }

    public static String getIdentifier() {
        return Constants.SOGGETTO_DELEGATE;
    }

    public ArrayList<SoggettoVO> getListaPersonaFisica(int aooId, String cognome,
            String nome, String codiceFiscale,String comune) {

        try {
            if (cognome == null)
                cognome = "";
            if (nome == null)
                nome = "";
            if (codiceFiscale == null)
                codiceFiscale = "";
            if (comune == null)
            	comune = "";
            return soggettoDAO.getListaPersonaFisica(aooId, cognome, nome,
                    codiceFiscale,comune);
        } catch (DataException de) {
            logger
                    .error("SoggettoDelegate: failed getting getListaPersonaFisica: ");
            return null;
        }
    }

    public ArrayList<SoggettoVO> getListaPersonaGiuridica(int aooId, String denominazione,
            String pIva,String comune) {

        try {
            if (denominazione == null)
                denominazione = "";
            if (pIva == null)
                pIva = "";
            if (comune == null)
            	comune = "";
            return soggettoDAO.getListaPersonaGiuridica(aooId, denominazione,
                    pIva,comune);
        } catch (DataException de) {
            logger
                    .error("SoggettoDelegate: failed getting getListaPersonaGiuridica: ");
            return null;
        }
    }


    
    public SoggettoVO getPersona(int id) {
    	JDBCManager jdbcMan = null;
        Connection connection = null;
        logger.info("SoggettoDelegate:salvaSoggetto");
        //SoggettoVO sVO = new SoggettoVO("F");
        try {
            jdbcMan = new JDBCManager();
            connection = jdbcMan.getConnection();
        	String tipo=soggettoDAO.getTipoPersona(connection,id);
        	if(tipo.equals("F"))
        		return soggettoDAO.getPersonaFisica(connection,id);
        	else
        		return soggettoDAO.getPersonaGiuridica(connection,id);
        } catch (DataException de) {
            logger.error("SoggettoDelegate: failed getting getPersona: ");
            return null;
        } catch (SQLException e) {
        	logger.error("SoggettoDelegate: failed getting getPersona: ");
            return null;
		} finally {
            jdbcMan.close(connection);
        } 
        //return sVO;
    }
    
    public String getMailFormPersonaId(int id) {
    	JDBCManager jdbcMan = null;
        Connection connection = null;
        logger.info("SoggettoDelegate:salvaSoggetto");
        try {
        	jdbcMan = new JDBCManager();
            connection = jdbcMan.getConnection();
        	return soggettoDAO.getMailFormPersonaId(connection,id);
        } catch (DataException de) {
            logger.error("SoggettoDelegate: failed getting getPersona: ");
            return null;
        } catch (SQLException e) {
        	logger.error("SoggettoDelegate: failed getting getPersona: ");
            return null;
		} finally {
            jdbcMan.close(connection);
        } 
        //return sVO;
    }
    
    public SoggettoVO getPersonaFisica(int id) {

        try {
            return soggettoDAO.getPersonaFisica(id);
        } catch (DataException de) {
            logger.error("SoggettoDelegate: failed getting getPersonaFisica: ");
            return null;
        }
    }

    public SoggettoVO getPersonaGiuridica(int id) {

        try {
            return soggettoDAO.getPersonaGiuridica(id);
        } catch (DataException de) {
            logger.error("SoggettoDelegate: failed getting getPersonaFisica: ");
            return null;
        }
    }

    public ListaDistribuzioneVO getListaDistribuzione(int id) {

        try {
            return soggettoDAO.getListaDistribuzione(id);
        } catch (DataException de) {
            logger
                    .error("SoggettoDelegate: failed getting getListaDistribuzione: ");
            return null;
        }
    }

    public ArrayList<IdentityVO> getElencoListaDistribuzioneEsterna(String descrizione, int aooId) {

        try {
            return soggettoDAO.getElencoListaDistribuzione(descrizione, aooId,"O");
        } catch (DataException de) {
            logger
                    .error("SoggettoDelegate: failed getting getElencoListaDistribuzione: ");
            return null;
        }
    }

    public ArrayList<IdentityVO> getElencoListaDistribuzioneInterna(String descrizione, int aooId) {

        try {
            return soggettoDAO.getElencoListaDistribuzione(descrizione, aooId,"I");
        } catch (DataException de) {
            logger
                    .error("SoggettoDelegate: failed getting getElencoListaDistribuzione: ");
            return null;
        }
    }
    
    public ArrayList<SoggettoVO> getDestinatariListaDistribuzione(int listaId) {

        try {
            return soggettoDAO.getDestinatariListaDistribuzione(listaId);
        } catch (DataException de) {
            logger
                    .error("SoggettoDelegate: failed getting getDestinatariListaDistribuzione");
            return null;
        }
    }

    public Collection<AssegnatarioVO> getAssegnatariListaDistribuzione(int listaId) {

        try {
            return soggettoDAO.getAssegnatariListaDistribuzione(listaId);
        } catch (DataException de) {
            logger
                    .error("SoggettoDelegate: failed getting getDestinatariListaDistribuzione");
            return null;
        }
    }
    
    public ArrayList<IdentityVO> getElencoListeDistribuzioneEsterna() {

        try {
            return soggettoDAO.getElencoListeDistribuzione("O");
        } catch (DataException de) {
            logger
                    .error("SoggettoDelegate: failed getting getElencoListeDistribuzione(): ");
            return null;
        }
    }

    public ArrayList<IdentityVO> getElencoListeDistribuzioneInterna() {

        try {
            return soggettoDAO.getElencoListeDistribuzione("I");
        } catch (DataException de) {
            logger
                    .error("SoggettoDelegate: failed getting getElencoListeDistribuzione(): ");
            return null;
        }
    }
    
    public Collection<StatoCivileVO> getLstStatoCivile() {
        ArrayList<StatoCivileVO> list = new ArrayList<StatoCivileVO>(6);
        list.add(new StatoCivileVO("C", "Coniugato/a"));
        list.add(new StatoCivileVO("B", "Celibe"));
        list.add(new StatoCivileVO("N", "Nubile"));
        list.add(new StatoCivileVO("L", "Stato Libero"));
        list.add(new StatoCivileVO("V", "Vedovo/a"));
        list.add(new StatoCivileVO("S", "Sconosciuto"));

        return list;
    }

    public int salvaPersonaFisica(SoggettoVO personaFisica, Utente utente) {
        JDBCManager jdbcMan = null;
        Connection connection = null;
        logger.info("SoggettoDelegate:salvaSoggetto");
        SoggettoVO sVO = new SoggettoVO("F");
        try {
            jdbcMan = new JDBCManager();
            connection = jdbcMan.getConnection();
            connection.setAutoCommit(false);
            personaFisica.setAoo(utente.getValueObject().getAooId());
            if (personaFisica.getId() == null
                    || personaFisica.getId().intValue() == 0) {
                personaFisica.setRowCreatedTime(new Date(System
                        .currentTimeMillis()));
                personaFisica.setRowCreatedUser(utente.getValueObject()
                        .getUsername());
                personaFisica.setId(IdentificativiDelegate.getInstance()
                        .getNextId(connection, NomiTabelle.RUBRICA));
                sVO = soggettoDAO.newPersonaFisica(connection, personaFisica);
            } else {
                personaFisica.setRowUpdatedTime(new Date(System
                        .currentTimeMillis()));
                personaFisica.setRowUpdatedUser(utente.getValueObject()
                        .getUsername());
                sVO = soggettoDAO.editPersonaFisica(connection, personaFisica);
            }
            connection.commit();
        } catch (DataException de) {
            jdbcMan.rollback(connection);
            logger.error("SoggettoDelegate: failed salvaPersonaFisica: ");
        } catch (SQLException se) {
            jdbcMan.rollback(connection);
        } finally {
            jdbcMan.close(connection);
        }
        return sVO.getReturnValue();
    }

    public int salvaPersonaGiuridica(SoggettoVO personaGiuridica, Utente utente) {
        JDBCManager jdbcMan = null;
        Connection connection = null;
        logger.info("SoggettoDelegate:salvaPersonaGiuridica");
        SoggettoVO sVO = new SoggettoVO("G");

        try {
            jdbcMan = new JDBCManager();
            connection = jdbcMan.getConnection();
            connection.setAutoCommit(false);
            personaGiuridica.setAoo(utente.getValueObject().getAooId());
            if (personaGiuridica.getId() == null
                    || personaGiuridica.getId().intValue() == 0) {
                personaGiuridica.setRowCreatedTime(new Date(System
                        .currentTimeMillis()));
                personaGiuridica.setRowCreatedUser(utente.getValueObject()
                        .getUsername());
                personaGiuridica.setId(IdentificativiDelegate.getInstance()
                        .getNextId(connection, NomiTabelle.RUBRICA));
                sVO = soggettoDAO.newPersonaGiuridica(connection,
                        personaGiuridica);
            } else {
                personaGiuridica.setRowUpdatedTime(new Date(System
                        .currentTimeMillis()));
                personaGiuridica.setRowUpdatedUser(utente.getValueObject()
                        .getUsername());
                sVO = soggettoDAO.editPersonaGiuridica(connection,
                        personaGiuridica);
            }
            connection.commit();
        } catch (DataException de) {
            jdbcMan.rollback(connection);
            logger.error("SoggettoDelegate: failed salvaPersonaGiuridica: ");
        } catch (SQLException se) {
            jdbcMan.rollback(connection);
        } finally {
            jdbcMan.close(connection);
        }
        return sVO.getReturnValue();
    }

    public ListaDistribuzioneVO salvaListaDistribuzioneEsterna(
            ListaDistribuzioneVO listaDistribuzione, Utente utente,
            Map<Integer,SoggettoVO> elencoSoggetti) {
        JDBCManager jdbcMan = null;
        Connection connection = null;

        listaDistribuzione.setReturnValue(ReturnValues.UNKNOWN);
        try {
            jdbcMan = new JDBCManager();
            connection = jdbcMan.getConnection();
            connection.setAutoCommit(false);
            if (listaDistribuzione.getId() == null
                    || listaDistribuzione.getId().intValue() == 0) {
                listaDistribuzione.setRowCreatedTime(new Date(System
                        .currentTimeMillis()));
                listaDistribuzione.setRowCreatedUser(utente.getValueObject()
                        .getUsername());
                listaDistribuzione
                        .setId(IdentificativiDelegate.getInstance().getNextId(
                                connection, NomiTabelle.LISTA_DISTRIBUZIONE));
                listaDistribuzione=soggettoDAO.newListaDistribuzione(connection, listaDistribuzione,"O");
                if (listaDistribuzione.getReturnValue() == (ReturnValues.SAVED)) {
                    Iterator<SoggettoVO> it = elencoSoggetti.values().iterator();
                    while (it.hasNext()) {
                        SoggettoVO s = (SoggettoVO) it.next();
                        soggettoDAO.inserisciSoggettoLista(connection,
                                listaDistribuzione.getId().intValue(), s
                                        .getId().intValue(), s.getTipo(),
                                utente.getValueObject().getUsername());
                    }
                }
            } else {
                listaDistribuzione.setRowUpdatedTime(new Date(System
                        .currentTimeMillis()));
                listaDistribuzione.setRowUpdatedUser(utente.getValueObject()
                        .getUsername());
                listaDistribuzione=soggettoDAO.editListaDistribuzione(connection,
                        listaDistribuzione);
                soggettoDAO.deleteRubricaListaDistribuzione(connection,
                        listaDistribuzione.getId().intValue());
                if (listaDistribuzione.getReturnValue() == (ReturnValues.SAVED)
                        || (listaDistribuzione.getReturnValue() == (ReturnValues.EXIST_DESCRIPTION))) {
                    Iterator<SoggettoVO> it = elencoSoggetti.values().iterator();
                    while (it.hasNext()) {
                        SoggettoVO s = (SoggettoVO) it.next();
                        soggettoDAO.inserisciSoggettoLista(connection,
                                listaDistribuzione.getId().intValue(), s
                                        .getId().intValue(), s.getTipo(),
                                utente.getValueObject().getUsername());
                    }

                    // listaDistribuzione.setReturnValue(ReturnValues.SAVED);
                }
               
            }
            connection.commit();
        } catch (DataException de) {
            jdbcMan.rollback(connection);
            logger.error("SoggettoDelegate: failed salvaListaDistribuzione: ",
                    de);
        } catch (SQLException se) {
            logger.error("SoggettoDelegate: failed salvaListaDistribuzione: ",
                    se);
            jdbcMan.rollback(connection);
        } finally {
            jdbcMan.close(connection);
        }
        // listaDistribuzione.setReturnValue(ReturnValues.SAVED);
        return listaDistribuzione;
    }

    public ListaDistribuzioneVO salvaListaDistribuzioneInterna(
            ListaDistribuzioneVO listaDistribuzione, Utente utente,
            Collection<AssegnatarioVO> elencoCariche) {
        JDBCManager jdbcMan = null;
        Connection connection = null;

        listaDistribuzione.setReturnValue(ReturnValues.UNKNOWN);
        try {
            jdbcMan = new JDBCManager();
            connection = jdbcMan.getConnection();
            connection.setAutoCommit(false);
            if (listaDistribuzione.getId() == null
                    || listaDistribuzione.getId().intValue() == 0) {
                listaDistribuzione.setRowCreatedTime(new Date(System
                        .currentTimeMillis()));
                listaDistribuzione.setRowCreatedUser(utente.getValueObject()
                        .getUsername());
                listaDistribuzione
                        .setId(IdentificativiDelegate.getInstance().getNextId(
                                connection, NomiTabelle.LISTA_DISTRIBUZIONE));
                listaDistribuzione = soggettoDAO.newListaDistribuzione(connection,
                        listaDistribuzione,"I");
                
            } else {
                listaDistribuzione.setRowUpdatedTime(new Date(System
                        .currentTimeMillis()));
                listaDistribuzione.setRowUpdatedUser(utente.getValueObject()
                        .getUsername());
                listaDistribuzione =  soggettoDAO.editListaDistribuzione(connection,
                        listaDistribuzione);
            }
               
                if  (listaDistribuzione.getReturnValue() == (ReturnValues.SAVED)
                        || (listaDistribuzione.getReturnValue() == (ReturnValues.EXIST_DESCRIPTION))) {
                	soggettoDAO.eliminaAssegnatariListaDistribuzione(connection,listaDistribuzione.getId());
                	// salva assegnatari
            		if (elencoCariche != null) {
            			for (Iterator<AssegnatarioVO> i = elencoCariche.iterator(); i.hasNext();) {
            				AssegnatarioVO ass= (AssegnatarioVO) i.next();
            				ass.setId(IdentificativiDelegate.getInstance().getNextId(connection,NomiTabelle.ASSEGNATARI_LISTA_DISTRIBUZIONE));
            				soggettoDAO.salvaAssegnatarioListaDistribuzione(connection, listaDistribuzione.getId(),ass,utente.getValueObject().getUsername());
            				
            			}
            		}

                   
                }
               
            
            connection.commit();
        } catch (DataException de) {
            jdbcMan.rollback(connection);
            logger.error("SoggettoDelegate: failed salvaListaDistribuzione: ",
                    de);
        } catch (SQLException se) {
            logger.error("SoggettoDelegate: failed salvaListaDistribuzione: ",
                    se);
            jdbcMan.rollback(connection);
        } finally {
            jdbcMan.close(connection);
        }
        // listaDistribuzione.setReturnValue(ReturnValues.SAVED);
        return listaDistribuzione;
    }
    
    
    public int cancellaSoggetto(long id) {
        JDBCManager jdbcMan = null;
        Connection connection = null;
        logger.info("SoggettoDelegate:cancellaSoggetto");
        int returnValue = ReturnValues.SAVED;

        try {
            jdbcMan = new JDBCManager();
            connection = jdbcMan.getConnection();
            connection.setAutoCommit(false);
            soggettoDAO.deleteSoggetto(connection, id);
            connection.commit();
        } catch (DataException de) {
            returnValue = ReturnValues.INVALID;
            jdbcMan.rollback(connection);
            logger.error("SoggettoDelegate: failed cancellaSoggetto: ");
        } catch (SQLException se) {
            jdbcMan.rollback(connection);
        } finally {
            jdbcMan.close(connection);
        }
        return returnValue;
    }

    public int cancellaListaDistribuzione(long id) {
        JDBCManager jdbcMan = null;
        Connection connection = null;
        logger.info("SoggettoDelegate:cancellalistaDistribuzione");

        try {
            jdbcMan = new JDBCManager();
            connection = jdbcMan.getConnection();
            connection.setAutoCommit(false);
            soggettoDAO.deleteAssegnatariListaDistribuzione(connection, id);
            soggettoDAO.deleteRubricaListaDistribuzione(connection, id);
            soggettoDAO.deleteListaDistribuzione(connection, id);
            connection.commit();
        } catch (DataException de) {
            jdbcMan.rollback(connection);
            logger
                    .error("SoggettoDelegate: failed cancellaListaDistribuzione: ");
        } catch (SQLException se) {
            jdbcMan.rollback(connection);
        } finally {
            jdbcMan.close(connection);
        }
        return 0;

    }

	public String getPersonaFisicaListFromCognome(String descrizioneInteressatoDelegato) {
		String ids=null;
        try {
            List<SoggettoVO> soggetti=new ArrayList<SoggettoVO>();
           
        	soggetti=soggettoDAO.getListaPersonaFisicaByCognome(descrizioneInteressatoDelegato);
        	if(soggetti.size()==0)
        		return "0";
        	for(SoggettoVO s:soggetti){
        		if(ids==null)
        			ids=String.valueOf(s.getId().intValue());
        		else
        			ids=ids+","+String.valueOf(s.getId().intValue());
        	}
        	return ids;
        } catch (DataException de) {
            logger.error("SoggettoDelegate: failed getting getPersonaFisica: ");
            return null;
        }
	}

	public String getPersonaFisicaListFromCognomeNome(String cognomeNome) {
		String ids=null;
        try {
            List<SoggettoVO> soggetti=new ArrayList<SoggettoVO>();
           
        	soggetti=soggettoDAO.getListaPersonaFisicaByCognomeNome(cognomeNome);
        	if(soggetti.size()==0)
        		return "0";
        	for(SoggettoVO s:soggetti){
        		if(ids==null)
        			ids=String.valueOf(s.getId().intValue());
        		else
        			ids=ids+","+String.valueOf(s.getId().intValue());
        	}
        	return ids;
        } catch (DataException de) {
            logger.error("SoggettoDelegate: failed getting getPersonaFisica: ");
            return null;
        }
	}
	
	public SoggettoVO getSoggettoByMail(Integer aooId, String email) {
		SoggettoVO s=null;
        try {
           
        	s=soggettoDAO.getSoggettoByMail(aooId,email);
      
        } catch (DataException de) {
            logger.error("SoggettoDelegate: failed getting getPersonaFisica: ");
            return null;
        }
		return s;
	}

	public boolean isMailUsed(Integer aooId, String email) {
		boolean present=false;
        try {
           
        	present=soggettoDAO.isMailUsed(aooId,email);
      
        } catch (DataException de) {
            logger.error("SoggettoDelegate: failed getting getPersonaFisica: ");
            return false;
        }
		return present;
	}

	public SoggettoVO getSoggettoById(Integer id) {
		SoggettoVO s=null;
        try {
           
        	s=soggettoDAO.getSoggettoById(id);
      
        } catch (DataException de) {
            logger.error("SoggettoDelegate: failed getting getPersonaFisica: ");
            return null;
        }
		return s;
	}
	
}
