package it.finsiel.siged.dao.jdbc;

import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.integration.AreaOrganizzativaDAO;
import it.finsiel.siged.mvc.vo.organizzazione.AreaOrganizzativaVO;
import it.finsiel.siged.mvc.vo.organizzazione.MailConfigVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.rdb.JDBCManager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

public class AreaOrganizzativaDAOjdbc implements AreaOrganizzativaDAO {
    static Logger logger = Logger.getLogger(AreaOrganizzativaDAOjdbc.class
            .getName());

    private JDBCManager jdbcMan = new JDBCManager();

    public void cancellaAreaOrganizzativa(Connection conn,
            int areaorganizzativaId) throws DataException {
        PreparedStatement pstmt = null;
        try {
            if (conn == null) {
                logger
                        .warn("cancellaAreaOrganizzativa() - Invalid Connection :"
                                + conn);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            pstmt = conn.prepareStatement(DELETE_AREA_ORGANIZZATIVA);
            pstmt.setInt(1, areaorganizzativaId);
            pstmt.executeUpdate();

        } catch (Exception e) {
            logger.error("cancellaAreaOrganizzativa", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
    }
    
    public void cancellaMailConfig(Connection conn,
            int areaorganizzativaId) throws DataException {
        PreparedStatement pstmt = null;
        try {
            if (conn == null) {
                logger
                        .warn("cancellaMailConfig() - Invalid Connection :"
                                + conn);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            pstmt = conn.prepareStatement(DELETE_MAIL_CONFIG);
            pstmt.setInt(1, areaorganizzativaId);
            pstmt.executeUpdate();

        } catch (Exception e) {
            logger.error("cancellaMailConfig", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
    }

    public boolean esisteAreaOrganizzativa(String aooDescrizione, int aooId)
            throws DataException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean esiste = false;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(SELECT_AOO_BY_DESC);
            pstmt.setString(1, aooDescrizione.toUpperCase());
            pstmt.setInt(2, aooId);

            rs = pstmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                esiste = true;
            }
        } catch (Exception e) {
            logger.error("esisteAreaOrganizzativa", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
            jdbcMan.close(connection);
        }
        return esiste;
    }

    public boolean isAreaOrganizzativaCancellabile(int aooId)
            throws DataException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean cancellabile = true;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(SELECT_AOO_REGISTRI);
            pstmt.setInt(1, aooId);
            rs = pstmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                cancellabile = false;
            } else {
                jdbcMan.close(rs);
                jdbcMan.close(pstmt);
                pstmt = connection.prepareStatement(SELECT_AOO_UFFICI);
                pstmt.setInt(1, aooId);
                rs = pstmt.executeQuery();
                rs.next();
                if (rs.getInt(1) > 0) {
                    cancellabile = false;
                } else {
                    jdbcMan.close(rs);
                    jdbcMan.close(pstmt);
                    pstmt = connection.prepareStatement(SELECT_AOO_UTENTI);
                    pstmt.setInt(1, aooId);
                    rs = pstmt.executeQuery();
                    rs.next();
                    if (rs.getInt(1) > 0) {
                        cancellabile = false;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("isUfficioCancellabile", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
            jdbcMan.close(connection);
        }
        return cancellabile;
    }

   
    public AreaOrganizzativaVO newAreaOrganizzativa(Connection conn,
            AreaOrganizzativaVO areaorganizzativaVO) throws DataException {
        PreparedStatement pstmt = null;
        areaorganizzativaVO.setReturnValue(ReturnValues.UNKNOWN);
        try {
            if (conn == null) {
                logger.warn("newAreaOrganizzativa() - Invalid Connection :"
                        + conn);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            pstmt = conn.prepareStatement(INSERT_AREA_ORGANIZZATIVA);
            int n = 0;
            pstmt.setInt(++n, areaorganizzativaVO.getId().intValue());
            pstmt.setString(++n, areaorganizzativaVO.getCodi_aoo());
            pstmt.setString(++n, areaorganizzativaVO.getDescription());
            if (areaorganizzativaVO.getData_istituzione() == null) {
                pstmt.setDate(++n, new Date(System.currentTimeMillis()));
            } else {
                pstmt.setDate(++n, new Date(areaorganizzativaVO.getData_istituzione().getTime()));
            }
            pstmt.setString(++n, areaorganizzativaVO.getResponsabile_nome());
            pstmt.setString(++n, areaorganizzativaVO.getResponsabile_cognome());
            pstmt.setString(++n, areaorganizzativaVO.getResponsabile_email());
            pstmt.setString(++n, areaorganizzativaVO.getResponsabile_telefono());
            if (areaorganizzativaVO.getData_soppressione() == null) {
                pstmt.setNull(++n, Types.DATE);
            } else {
                pstmt.setDate(++n, new Date(areaorganizzativaVO.getData_soppressione().getTime()));
            }
            pstmt.setString(++n, areaorganizzativaVO.getTelefono());
            pstmt.setString(++n, areaorganizzativaVO.getFax());
            pstmt.setString(++n, areaorganizzativaVO.getIndi_dug());
            pstmt.setString(++n, areaorganizzativaVO.getIndi_toponimo());
            pstmt.setString(++n, areaorganizzativaVO.getIndi_civico());
            pstmt.setString(++n, areaorganizzativaVO.getIndi_cap());
            pstmt.setString(++n, areaorganizzativaVO.getIndi_comune());
            pstmt.setString(++n, areaorganizzativaVO.getEmail());
            pstmt.setString(++n, areaorganizzativaVO.getDipartimento_codice());
            pstmt.setString(++n, areaorganizzativaVO.getDipartimento_descrizione());
            pstmt.setString(++n, areaorganizzativaVO.getTipo_aoo());
            pstmt.setInt(++n, areaorganizzativaVO.getProvincia_id());
            pstmt.setString(++n, areaorganizzativaVO.getCodi_documento_doc());
            pstmt.setInt(++n, areaorganizzativaVO.getFlag_pdf());
            pstmt.setInt(++n, areaorganizzativaVO.getAmministrazione_id());
            pstmt.setDate(++n, new Date(System.currentTimeMillis()));
            pstmt.setString(++n, areaorganizzativaVO.getRowCreatedUser());
            pstmt.setInt(++n, areaorganizzativaVO.getVersione());
            pstmt.setInt(++n, areaorganizzativaVO.getDipendenzaTitolarioUfficio());
            pstmt.setInt(++n, areaorganizzativaVO.getTitolarioLivelloMinimo());
            pstmt.setInt(++n, areaorganizzativaVO.isDocumentoReadable() ? 1 : 0);
            pstmt.setInt(++n, areaorganizzativaVO.isRicercaUfficiFull() ? 1 : 0);
            pstmt.setString(++n, areaorganizzativaVO.getIdCommittenteFattura());
            pstmt.setInt(++n, areaorganizzativaVO.isGaAbilitata() ? 1 : 0);
            pstmt.setString(++n, areaorganizzativaVO.getGaUsername());
            pstmt.setString(++n, areaorganizzativaVO.getGaPwd());
            pstmt.setInt(++n, areaorganizzativaVO.isGaFlagInvio() ? 1 : 0);
            pstmt.setTime(++n, areaorganizzativaVO.getGaTimer());
            pstmt.setString(++n, areaorganizzativaVO.getAnniVisibilitaBacheca());
            pstmt.setString(++n, areaorganizzativaVO.getMaxRighe());
            pstmt.setInt(++n, areaorganizzativaVO.getFlagPubblicazioneP7m());
            pstmt.executeUpdate();
        } catch (Exception e) {
            logger.error("Save Area Organizzativa", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
        areaorganizzativaVO = getAreaOrganizzativa(conn, areaorganizzativaVO
                .getId().intValue());
        areaorganizzativaVO.setReturnValue(ReturnValues.SAVED);
        return areaorganizzativaVO;
    }

    public MailConfigVO newMailConfig(Connection conn,
    		MailConfigVO mailConfigVO) throws DataException {
        PreparedStatement pstmt = null;
        mailConfigVO.setReturnValue(ReturnValues.UNKNOWN);
        try {
            if (conn == null) {
                logger.warn("newMailConfig() - Invalid Connection :"
                        + conn);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            pstmt = conn.prepareStatement(INSERT_MAIL_CONFIG);
            int n = 0;
            pstmt.setInt(++n, mailConfigVO.getAooId());
            pstmt.setInt(++n, mailConfigVO.getAooId());
            pstmt.setString(++n, mailConfigVO.getPecIndirizzo());
            pstmt.setString(++n, mailConfigVO.getPecUsername());
            pstmt.setString(++n, mailConfigVO.getPecPwd());
            pstmt.setInt(++n, mailConfigVO.isPecAbilitata() ? 1 : 0);
            pstmt.setString(++n, mailConfigVO.getPecSslPort());
            pstmt.setString(++n, mailConfigVO.getPecPop3());
            pstmt.setString(++n, mailConfigVO.getPecSmtp());
            pstmt.setString(++n, mailConfigVO.getPecSmtpPort());
            pstmt.setString(++n, mailConfigVO.getPnIndirizzo());
            pstmt.setString(++n, mailConfigVO.getPnUsername());
            pstmt.setString(++n, mailConfigVO.getPnPwd());
            pstmt.setString(++n, mailConfigVO.isPnSsl() ? "1" : "0");
            pstmt.setString(++n, mailConfigVO.getPnSslPort());
            pstmt.setString(++n, mailConfigVO.getPnPop3());
            pstmt.setString(++n, mailConfigVO.getPnSmtp());
            pstmt.setInt(++n, mailConfigVO.getMailTimer());
            pstmt.setInt(++n, mailConfigVO.isPnAbilitata() ? 1 : 0);
            
            pstmt.setString(++n, mailConfigVO.getPrecIndirizzoInvio());
            pstmt.setString(++n, mailConfigVO.getPrecIndirizzoRicezione());
            pstmt.setString(++n, mailConfigVO.getPrecUsername());
            pstmt.setString(++n, mailConfigVO.getPrecPwd());
            pstmt.setString(++n, mailConfigVO.getPrecSmtp());
            pstmt.executeUpdate();
        } catch (Exception e) {
            logger.error("Save Area Organizzativa", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
        mailConfigVO = getMailConfigByAooId(conn, mailConfigVO
                .getAooId());
        mailConfigVO.setReturnValue(ReturnValues.SAVED);
        return mailConfigVO;
    }
    
    public MailConfigVO updateMailConfig(Connection conn,
    		MailConfigVO mailConfigVO)throws DataException {
        PreparedStatement pstmt = null;
        mailConfigVO.setReturnValue(ReturnValues.UNKNOWN);

        try {
            if (conn == null) {
                logger.warn("updateMailConfig() - Invalid Connection :"
                        + conn);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            int n = 0;
            pstmt = conn.prepareStatement(UPDATE_MAIL_CONFIG);
            pstmt.setString(++n, mailConfigVO.getPecIndirizzo());
            pstmt.setString(++n, mailConfigVO.getPecUsername());
            pstmt.setString(++n, mailConfigVO.getPecPwd());
            pstmt.setInt(++n, mailConfigVO.isPecAbilitata() ? 1 : 0);
            pstmt.setString(++n, mailConfigVO.getPecSslPort());
            pstmt.setString(++n, mailConfigVO.getPecPop3());
            pstmt.setString(++n, mailConfigVO.getPecSmtp());
            pstmt.setString(++n, mailConfigVO.getPecSmtpPort());
            pstmt.setString(++n, mailConfigVO.getPnIndirizzo());
            pstmt.setString(++n, mailConfigVO.getPnUsername());
            pstmt.setString(++n, mailConfigVO.getPnPwd());
            pstmt.setString(++n, mailConfigVO.isPnSsl() ? "1" : "0");
            pstmt.setString(++n, mailConfigVO.getPnSslPort());
            pstmt.setString(++n, mailConfigVO.getPnPop3());
            pstmt.setString(++n, mailConfigVO.getPnSmtp());
            pstmt.setInt(++n, mailConfigVO.getMailTimer());
            pstmt.setInt(++n, mailConfigVO.isPnAbilitata() ? 1 : 0);
            
            pstmt.setString(++n, mailConfigVO.getPrecIndirizzoInvio());
            pstmt.setString(++n, mailConfigVO.getPrecIndirizzoRicezione());
            pstmt.setString(++n, mailConfigVO.getPrecUsername());
            pstmt.setString(++n, mailConfigVO.getPrecPwd());
            pstmt.setString(++n, mailConfigVO.getPrecSmtp());
            
            pstmt.setInt(++n, mailConfigVO.getAooId());
            pstmt.executeUpdate();
            mailConfigVO.setReturnValue(ReturnValues.SAVED);

        } catch (Exception e) {
            logger.error("updateAreaOrganizzativa AreaOrganizzativa", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
        return mailConfigVO;
    }
    
    
    public AreaOrganizzativaVO updateAreaOrganizzativa(Connection conn,
            AreaOrganizzativaVO aooVO)throws DataException {
        PreparedStatement pstmt = null;
        aooVO.setReturnValue(ReturnValues.UNKNOWN);

        try {
            if (conn == null) {
                logger.warn("updateAreaOrganizzativa() - Invalid Connection :"
                        + conn);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            int n = 0;

            pstmt = conn.prepareStatement(UPDATE_AREA_ORGANIZZATIVA);
            pstmt.setString(++n, aooVO.getCodi_aoo());
            pstmt.setString(++n, aooVO.getDescription());
            if (aooVO.getData_istituzione() == null) {
                pstmt.setDate(++n, new Date(System.currentTimeMillis()));
            } else {
                pstmt.setDate(++n, new Date(aooVO.getData_istituzione()
                        .getTime()));
            }
            pstmt.setString(++n, aooVO.getResponsabile_nome());
            pstmt.setString(++n, aooVO.getResponsabile_cognome());
            pstmt.setString(++n, aooVO.getResponsabile_email());
            pstmt.setString(++n, aooVO.getResponsabile_telefono());
            if (aooVO.getData_soppressione() == null) {
                pstmt.setNull(++n, Types.DATE);
            } else {
                pstmt.setDate(++n, new Date(aooVO.getData_soppressione()
                        .getTime()));
            }
            pstmt.setString(++n, aooVO.getTelefono());
            pstmt.setString(++n, aooVO.getFax());
            pstmt.setString(++n, aooVO.getIndi_dug());
            pstmt.setString(++n, aooVO.getIndi_toponimo());
            pstmt.setString(++n, aooVO.getIndi_civico());
            pstmt.setString(++n, aooVO.getIndi_cap());
            pstmt.setString(++n, aooVO.getIndi_comune());
            pstmt.setInt(++n, aooVO.getProvincia_id());
            pstmt.setString(++n, aooVO.getEmail());
            pstmt.setString(++n, aooVO.getDipartimento_codice());
            pstmt.setString(++n, aooVO.getDipartimento_descrizione());
            pstmt.setString(++n, aooVO.getTipo_aoo());
            pstmt.setString(++n, aooVO.getCodi_documento_doc());
            pstmt.setString(++n, aooVO.getRowUpdatedUser());
            if (aooVO.getRowUpdatedTime() == null) {
                pstmt.setDate(++n, new Date(System.currentTimeMillis()));
            } else {
                pstmt.setDate(++n,
                        new Date(aooVO.getRowUpdatedTime().getTime()));
            }
            pstmt.setInt(++n, aooVO.getVersione() + 1);
            pstmt.setInt(++n, aooVO.getDipendenzaTitolarioUfficio());
            pstmt.setInt(++n, aooVO.getTitolarioLivelloMinimo());
            pstmt.setInt(++n, aooVO.isDocumentoReadable() ? 1 : 0);
            pstmt.setInt(++n, aooVO.isRicercaUfficiFull() ? 1 : 0);
            pstmt.setString(++n, aooVO.getIdCommittenteFattura());
            
            pstmt.setInt(++n, aooVO.isGaAbilitata() ? 1 : 0);
            pstmt.setString(++n, aooVO.getGaUsername());
            pstmt.setString(++n, aooVO.getGaPwd());
            pstmt.setInt(++n, aooVO.isGaFlagInvio() ? 1 : 0);
            pstmt.setTime(++n, aooVO.getGaTimer());
            pstmt.setString(++n, aooVO.getAnniVisibilitaBacheca());
            pstmt.setString(++n, aooVO.getMaxRighe());
            pstmt.setInt(++n, aooVO.getFlagPubblicazioneP7m());

            
            pstmt.setInt(++n, aooVO.getId().intValue());
            pstmt.setInt(++n, aooVO.getVersione());

            pstmt.executeUpdate();
            aooVO.setReturnValue(ReturnValues.SAVED);

        } catch (Exception e) {
            logger.error("updateAreaOrganizzativa AreaOrganizzativa", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
        return aooVO;
    }
    
    public Collection<Integer> getAreeOrganizzativeId() throws DataException {
        List<Integer> areeId = new ArrayList<Integer>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(SELECT_AOO_ID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
            	//aooVO.setId(rs.getInt("aoo_id"));
            	areeId.add(rs.getInt("aoo_id"));
                logger.debug("Caricata Area Organizzativa ID: " + areeId);
            }
        } catch (Exception e) {
            //areeOrganizzative.clear();
            logger.error("Caricamento Aree Organizzative ", e);
            throw new DataException("Cannot load Aree Organizzative");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
            jdbcMan.close(connection);
        }
        return areeId;
    }

    public Collection<UfficioVO> getUffici(int areaorganizzativaId) throws DataException {
        List<UfficioVO> uffici = new ArrayList<UfficioVO>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(SELECT_UFFICI_BY_AOO);
            pstmt.setInt(1, areaorganizzativaId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                UfficioVO uVO = new UfficioVO();
                uVO.setAooId(rs.getInt("aoo_id"));
                uVO.setId(rs.getInt("ufficio_id"));
                uVO.setDescription(rs.getString("descrizione"));
                uffici.add(uVO);
                logger.debug("Caricata Lista Uffici Area Organizzativa: "
                        + areaorganizzativaId);
            }
        } catch (Exception e) {
            uffici.clear();
            logger.error("Caricamento Uffici", e);
            throw new DataException("Cannot load Uffici");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
            jdbcMan.close(connection);
        }
        return uffici;
    }

    public AreaOrganizzativaVO getAreaOrganizzativa(int areaorganizzativaId)
            throws DataException {
        AreaOrganizzativaVO aooVO;
        Connection connection = null;
        try {
            connection = jdbcMan.getConnection();
            aooVO = getAreaOrganizzativa(connection, areaorganizzativaId);
        } catch (Exception e) {
            throw new DataException(e.getMessage());
        } finally {
            jdbcMan.close(connection);
        }
        return aooVO;
    }
    
    public MailConfigVO getMailConfigByAooId(int areaorganizzativaId)
            throws DataException {
    	MailConfigVO mailConfigVO;
        Connection connection = null;
        try {
            connection = jdbcMan.getConnection();
            mailConfigVO = getMailConfigByAooId(connection, areaorganizzativaId);
        } catch (Exception e) {
        	
            throw new DataException(e.getMessage());
        } finally {
            jdbcMan.close(connection);
        }
        return mailConfigVO;
    }

    public AreaOrganizzativaVO getAreaOrganizzativa(Connection connection,
            int areaorganizzativaId) throws DataException {

        PreparedStatement pstmt = null;
        AreaOrganizzativaVO aooVO = new AreaOrganizzativaVO();
        ResultSet rs = null;
        try {
            if (connection == null) {
                logger.warn("getAreaOrganizzativa - Invalid Connection :"
                        + connection);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }

            pstmt = connection.prepareStatement(SELECT_AREE_ORGANIZZATIVA);
            pstmt.setInt(1, areaorganizzativaId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                setCampiAOO(aooVO, rs);
            }

        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("Area Organizzativa: getMenuProfilo", e);
            throw new DataException(
                    "Cannot load Area Organizzativa: getMenuProfilo");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
        }
        return aooVO;
    }

    private MailConfigVO getMailConfigByAooId(Connection connection,
            int areaorganizzativaId) throws DataException {

        PreparedStatement pstmt = null;
        MailConfigVO mailConfigVO = new MailConfigVO();
        ResultSet rs = null;
        try {
            if (connection == null) {
                logger.warn("getMailConfig - Invalid Connection :"
                        + connection);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }

            pstmt = connection.prepareStatement(SELECT_MAIL_CONFIG);
            pstmt.setInt(1, areaorganizzativaId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                setCampiMailConfig(mailConfigVO,rs);
            }

        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("Area Organizzativa: getMenuProfilo", e);
            throw new DataException(
                    "Cannot load Area Organizzativa: getMenuProfilo");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
        }
        return mailConfigVO;
    }
    
    private void setCampiAOO(AreaOrganizzativaVO aooVO, ResultSet rs)
            throws SQLException {
        aooVO.setId(rs.getInt("aoo_id"));
        aooVO.setCodi_aoo(rs.getString("codi_aoo"));
        aooVO.setDescription(rs.getString("desc_aoo"));
        aooVO.setData_istituzione(rs.getDate("data_istituzione"));
        aooVO.setResponsabile_nome(rs.getString("responsabile_nome"));
        aooVO.setResponsabile_cognome(rs.getString("responsabile_cognome"));
        aooVO.setResponsabile_email(rs.getString("responsabile_email"));
        aooVO.setResponsabile_telefono(rs.getString("responsabile_telefono"));
        aooVO.setData_soppressione(rs.getDate("data_soppressione"));
        aooVO.setTelefono(rs.getString("telefono"));
        aooVO.setFax(rs.getString("fax"));
        aooVO.setIndi_dug(rs.getString("indi_dug"));
        aooVO.setIndi_toponimo(rs.getString("indi_toponimo"));
        aooVO.setIndi_civico(rs.getString("indi_civico"));
        aooVO.setIndi_cap(rs.getString("indi_cap"));
        aooVO.setIndi_comune(rs.getString("indi_comune"));
        aooVO.setEmail(rs.getString("email"));
        aooVO.setDipartimento_codice(rs.getString("dipartimento_codice"));
        aooVO.setDipartimento_descrizione(rs
                .getString("dipartimento_descrizione"));
        aooVO.setTipo_aoo(rs.getString("tipo_aoo"));
        aooVO.setProvincia_id(rs.getInt("provincia_id"));
        aooVO.setCodi_documento_doc(rs.getString("codi_documento_doc"));
        aooVO.setFlag_pdf(rs.getString("flag_pdf").charAt(0));
        aooVO.setAmministrazione_id(rs.getInt("amministrazione_id"));
        aooVO.setRowCreatedTime(rs.getDate("row_created_time"));
        aooVO.setRowCreatedUser(rs.getString("row_created_user"));
        aooVO.setRowUpdatedUser(rs.getString("row_updated_user"));
        aooVO.setRowUpdatedTime(rs.getDate("row_updated_time"));
        aooVO.setDipendenzaTitolarioUfficio(rs
                .getInt("dipendenza_titolario_ufficio"));
        aooVO.setTitolarioLivelloMinimo(rs.getInt("titolario_livello_minimo"));
        aooVO.setVersione(rs.getInt("versione"));
        aooVO.setDocumentoReadable(rs.getBoolean("flag_documento_readable"));
        aooVO.setRicercaUfficiFull(rs.getBoolean("flag_ricerca_full"));
        aooVO.setAnniVisibilitaBacheca(rs.getString("anni_visibilita_bacheca"));
        aooVO.setMaxRighe(rs.getString("max_righe"));
        aooVO.setIdCommittenteFattura(rs.getString("fattura_id_committente"));
        //dati gestione archivi
        aooVO.setGaAbilitata(rs.getBoolean("ga_abilitata"));
        aooVO.setGaPwd(rs.getString("ga_pwd"));
        aooVO.setGaUsername(rs.getString("ga_username"));
        aooVO.setGaFlagInvio(rs.getBoolean("ga_flag_invio"));
        aooVO.setGaTimer(rs.getTime("ga_timer"));
        aooVO.setFlagPubblicazioneP7m(rs.getInt("flag_p7m"));

    }

    private void setCampiMailConfig(MailConfigVO mailConfigVO, ResultSet rs)
            throws SQLException {
    	mailConfigVO.setId(rs.getInt("config_id"));
    	mailConfigVO.setAooId(rs.getInt("aoo_id"));
    	mailConfigVO.setPecIndirizzo(rs.getString("pec_email"));
    	mailConfigVO.setPecUsername(rs.getString("pec_username"));
    	mailConfigVO.setPecPwd(rs.getString("pec_password"));
    	mailConfigVO.setPecAbilitata(rs.getBoolean("pec_abilitata"));
    	mailConfigVO.setPecSslPort(rs.getString("pec_ssl_port"));
    	mailConfigVO.setPecPop3(rs.getString("pec_pop3_host"));
    	mailConfigVO.setPecSmtp(rs.getString("pec_smtp_host"));
    	mailConfigVO.setPecSmtpPort(rs.getString("pec_smtp_port"));
    	mailConfigVO.setDataUltimaPecRicevuta(rs.getTimestamp("data_ultima_pec"));
    	mailConfigVO.setPnIndirizzo(rs.getString("pn_email"));
    	mailConfigVO.setPnUsername(rs.getString("pn_username"));
    	mailConfigVO.setPnPwd(rs.getString("pn_password"));
    	mailConfigVO.setPnSsl("1".equals(rs.getString("pn_use_ssl")) ? true : false);
    	mailConfigVO.setPnSslPort(rs.getString("pn_ssl_port"));
    	mailConfigVO.setPnPop3(rs.getString("pn_pop3_host"));
    	mailConfigVO.setPnSmtp(rs.getString("pn_smtp_host"));
    	mailConfigVO.setMailTimer(rs.getInt("mail_timer"));
    	mailConfigVO.setPnAbilitata(rs.getBoolean("pn_abilitata"));
    	
    	mailConfigVO.setPrecIndirizzoInvio(rs.getString("prec_email_invio"));
    	mailConfigVO.setPrecIndirizzoRicezione(rs.getString("prec_email_ricezione"));
    	mailConfigVO.setPrecUsername(rs.getString("prec_username"));
    	mailConfigVO.setPrecPwd(rs.getString("prec_password"));
    	mailConfigVO.setPrecSmtp(rs.getString("prec_smtp_host"));
    }
    
    
    public boolean isModificabileDipendenzaTitolarioUfficio(int aooId)
            throws DataException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean found = true;
        String sql;
        try {
            connection = jdbcMan.getConnection();
            if (connection == null) {
                logger
                        .warn("isModificabileDipendenzaTitolarioUfficio - Invalid Connection :"
                                + connection);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            sql = "SELECT count(*) from protocolli where aoo_id =?";
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, aooId);
            rs = pstmt.executeQuery();
            rs.next();
            found = rs.getInt(1) > 0;
            if (!found) {
                jdbcMan.close(pstmt);
                sql = "SELECT count(*) from fascicoli where aoo_id =?";
                pstmt = connection.prepareStatement(sql);
                pstmt.setInt(1, aooId);
                rs = pstmt.executeQuery();
                rs.next();
                found = rs.getInt(1) > 0;
                if (!found) {
                    jdbcMan.close(pstmt);
                    sql = "SELECT count(*) from faldoni where aoo_id =?";
                    pstmt = connection.prepareStatement(sql);
                    pstmt.setInt(1, aooId);
                    rs = pstmt.executeQuery();
                    rs.next();
                    found = rs.getInt(1) > 0;
                    if (!found) {
                        jdbcMan.close(pstmt);
                        sql = "SELECT count(*) from procedimenti where aoo_id =?";
                        pstmt = connection.prepareStatement(sql);
                        pstmt.setInt(1, aooId);
                        rs = pstmt.executeQuery();
                        rs.next();
                        found = rs.getInt(1) > 0;
                    }
                }

            }
        } catch (Exception e) {
            logger.error("isModificabileDipendenzaTitolarioUfficio:", e);
            throw new DataException("error.database.missing");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
            jdbcMan.close(connection);
        }
        return !found;
    }

   
    // versione=?

    public final static String DELETE_AREA_ORGANIZZATIVA = "DELETE FROM AREE_ORGANIZZATIVE "
            + " WHERE aoo_id=?";
    
    public final static String DELETE_MAIL_CONFIG = "DELETE FROM MAIL_CONFIG "
            + " WHERE aoo_id=?";

    private final static String SELECT_AOO_REGISTRI = "SELECT count(aoo_id) FROM REGISTRI where aoo_id=?";

    private final static String SELECT_AOO_UFFICI = "SELECT count(aoo_id) FROM UFFICI where aoo_id=?";

    private final static String SELECT_AOO_UTENTI = "SELECT count(aoo_id) FROM UTENTI where aoo_id=?";

    private final static String SELECT_AOO_BY_DESC = "SELECT count(aoo_id) FROM AREE_ORGANIZZATIVE where UPPER(desc_aoo)=? and aoo_id !=?";
    
    private final static String SELECT_UTENTI_BY_AOO_ID="SELECT * FROM utenti WHERE aoo_id=? ORDER BY COGNOME, NOME";
    
    private final static String SELECT_UTENTI_BY_AOO_ID_FLAG_ABILITATO="SELECT * FROM utenti WHERE aoo_id=? and flag_abilitato=1 ORDER BY COGNOME, NOME";
    
    
	public Collection<UtenteVO> getUtenti(int areaorganizzativaId) throws DataException {
		  Connection connection = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        UtenteVO c = null;
	       ArrayList<UtenteVO>  utenti = new ArrayList<UtenteVO>();
	        try {
	            connection = jdbcMan.getConnection();
	            pstmt = connection.prepareStatement(SELECT_UTENTI_BY_AOO_ID);
	            pstmt.setInt(1, areaorganizzativaId);
	            rs = pstmt.executeQuery();
	            while (rs.next()) {
	                c = new UtenteVO();
	                c.setId(rs.getInt("utente_id"));
	                c.setUsername(rs.getString("user_name"));
	                c.setEmailAddress(rs.getString("email"));
	                c.setCognome(rs.getString("cognome"));
	                c.setNome(rs.getString("nome"));
	                c.setCodiceFiscale(rs.getString("codicefiscale"));
	                c.setMatricola(rs.getString("matricola"));
	                c.setPassword(rs.getString("passwd"));
	                c.setDataFineAttivita(rs.getDate("data_fine_attivita"));
	                c.setAooId(rs.getInt("aoo_id"));
	                c.setRowCreatedTime(rs.getDate("row_created_time"));
	                c.setRowCreatedUser(rs.getString("row_created_user"));
	                c.setRowUpdatedUser(rs.getString("row_updated_user"));
	                c.setRowUpdatedTime(rs.getDate("row_updated_time"));
	                c.setVersione(rs.getInt("versione"));
	                c.setAbilitato(rs.getBoolean("flag_abilitato"));
	                c.setReturnValue(ReturnValues.FOUND);
	                utenti.add(c);

	            }
	        } catch (Exception e) {
	            logger.error("getUtenti", e);
	            throw new DataException("Cannot load getUtenti");
	        } finally {
	            jdbcMan.close(rs);
	            jdbcMan.close(pstmt);
	            jdbcMan.close(connection);
	        }
	        
	        return utenti;
	}

	public Collection<UtenteVO>  getUtentiCarica(int areaorganizzativaId) throws DataException {
		  Connection connection = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        UtenteVO c = null;
	       ArrayList<UtenteVO> utenti = new ArrayList<UtenteVO>();
	        try {
	            connection = jdbcMan.getConnection();
	            pstmt = connection.prepareStatement(SELECT_UTENTI_BY_AOO_ID_FLAG_ABILITATO);
	            pstmt.setInt(1, areaorganizzativaId);
	            rs = pstmt.executeQuery();
	            while (rs.next()) {
	                c = new UtenteVO();
	                c.setId(rs.getInt("utente_id"));
	                c.setUsername(rs.getString("user_name"));
	                c.setEmailAddress(rs.getString("email"));
	                c.setCognome(rs.getString("cognome"));
	                c.setNome(rs.getString("nome"));
	                c.setCodiceFiscale(rs.getString("codicefiscale"));
	                c.setMatricola(rs.getString("matricola"));
	                c.setPassword(rs.getString("passwd"));
	                c.setDataFineAttivita(rs.getDate("data_fine_attivita"));
	                c.setAooId(rs.getInt("aoo_id"));
	                c.setRowCreatedTime(rs.getDate("row_created_time"));
	                c.setRowCreatedUser(rs.getString("row_created_user"));
	                c.setRowUpdatedUser(rs.getString("row_updated_user"));
	                c.setRowUpdatedTime(rs.getDate("row_updated_time"));
	                c.setVersione(rs.getInt("versione"));
	                c.setAbilitato(rs.getBoolean("flag_abilitato"));
	                c.setReturnValue(ReturnValues.FOUND);
	                utenti.add(c);

	            }
	        } catch (Exception e) {
	            logger.error("getUtenti", e);
	            throw new DataException("Cannot load getUtenti");
	        } finally {
	            jdbcMan.close(rs);
	            jdbcMan.close(pstmt);
	            jdbcMan.close(connection);
	        }
	        
	        return utenti;
	}

	
	
	public void cancellaUtenteResponsabile(Connection conn, int aooId)
	throws DataException {
		PreparedStatement pstmt = null;
        try {
            if (conn == null) {
                logger.warn("cancellaUtenteResponsabile() - Invalid Connection :"+ conn);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
            pstmt = conn.prepareStatement("UPDATE AREE_ORGANIZZATIVE SET utente_responsabile_id=? WHERE aoo_id=?");
            pstmt.setNull(1, Types.INTEGER);
            pstmt.setInt(2, aooId);
            pstmt.executeUpdate();

        } catch (Exception e) {
            logger.error("cancellaUtenteResponsabile", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
}


public void salvaUtenteResponsabile(Connection conn, int aooId,
	Integer id) throws DataException {
	PreparedStatement pstmt = null;
    try {
        if (conn == null) {
            logger.warn("cancellaUtenteResponsabile() - Invalid Connection :"+ conn);
            throw new DataException(
                    "Connessione alla base dati non valida.");
        }
        pstmt = conn.prepareStatement("UPDATE AREE_ORGANIZZATIVE SET utente_responsabile_id=? WHERE aoo_id=?");
        pstmt.setInt(1, id);
        pstmt.setInt(2, aooId);
        pstmt.executeUpdate();

    } catch (Exception e) {
        logger.error("salvaUtenteResponsabile", e);
        throw new DataException("error.database.cannotsave");
    } finally {
        jdbcMan.close(pstmt);
    }
}

public int getUtenteResponsabile( int aooId) throws DataException {
	 Connection conn = null;
     PreparedStatement pstmt = null;
     ResultSet rs = null;
		try {
			conn = jdbcMan.getConnection();
	        pstmt = conn.prepareStatement("SELECT  utente_responsabile_id FROM aree_organizzative where aoo_id=?");
	        pstmt.setInt(1, aooId);
	        rs = pstmt.executeQuery();
            rs.next();
            return rs.getInt(1);
	    } catch (Exception e) {
	        logger.error("getUtenteResponsabile", e);
	        throw new DataException("error.database.cannotsave");
	    } finally {
	        jdbcMan.closeAll(rs,pstmt,conn);
	    }
	}
	
public void aggiornaAOODataUltimaPecRicevuta(Connection connection, int aooId,
		java.util.Date dataSpedizione) throws DataException {
	        PreparedStatement pstmt = null;
	       	        try {
	            if (connection == null) {
	                logger.warn("aggiornaAOODataUltimaPecRicevuta() - Invalid Connection:"
	                        + connection);
	                throw new DataException(
	                        "Connessione alla base dati non valida.");
	            }
	            pstmt = connection.prepareStatement(UPDATE_DATA_ULTIMA_PEC_AREA_ORGANIZZATIVA);
	            pstmt.setTimestamp(1, new Timestamp(dataSpedizione.getTime()));
	            pstmt.setInt(2, aooId);
	        	pstmt.executeUpdate();
	        } catch (Exception e) {
	            logger.error("aggiornaAOODataUltimaPecRicevuta: ", e);
	            throw new DataException("error.database.cannotsave");
	        } finally {
	            jdbcMan.close(pstmt);
	        }
	     
}

private final static String SELECT_AOO_ID = "SELECT aoo_id FROM aree_organizzative";


//private final static String SELECT_AOO = "SELECT * FROM aree_organizzative order by desc_aoo";

	public final static String SELECT_AREE_ORGANIZZATIVA = "SELECT * FROM AREE_ORGANIZZATIVE "
        + " WHERE aoo_id=?";
	
	public final static String SELECT_MAIL_CONFIG = "SELECT * FROM MAIL_CONFIG "
	        + " WHERE aoo_id=?";

public final static String SELECT_UFFICI_BY_AOO = "SELECT * FROM UFFICI "
        + " WHERE aoo_id=?";

public final static String INSERT_AREA_ORGANIZZATIVA = "INSERT INTO AREE_ORGANIZZATIVE"
        + " (aoo_id, codi_aoo, desc_aoo, data_istituzione, responsabile_nome, responsabile_cognome, "
        + " responsabile_email, responsabile_telefono, data_soppressione, telefono, fax, "
        + " indi_dug, indi_toponimo, indi_civico, indi_cap, indi_comune, email, "
        + " dipartimento_codice, dipartimento_descrizione, tipo_aoo, provincia_id, codi_documento_doc, flag_pdf, "
        + " amministrazione_id, row_created_time, row_created_user, versione, "
        + " dipendenza_titolario_ufficio, titolario_livello_minimo,flag_documento_readable,flag_ricerca_full,fattura_id_committente, "
        + " ga_abilitata, ga_username, ga_pwd, ga_flag_invio, ga_timer, anni_visibilita_bacheca, max_righe, flag_p7m)"
        + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

public final static String INSERT_MAIL_CONFIG = "INSERT INTO MAIL_CONFIG"
        + " (config_id, aoo_id, pec_email, pec_username, pec_password, pec_abilitata, pec_ssl_port, pec_pop3_host, pec_smtp_host, pec_smtp_port, "
        + " pn_email, pn_username, pn_password, pn_use_ssl, pn_ssl_port,  pn_pop3_host, pn_smtp_host, mail_timer, pn_abilitata," 
        + " prec_email_invio, prec_email_ricezione, prec_username, prec_password, prec_smtp_host)"
        + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

public final static String UPDATE_AREA_ORGANIZZATIVA = "UPDATE AREE_ORGANIZZATIVE"
        + " SET codi_aoo=?, desc_aoo=?, data_istituzione=?, responsabile_nome=?, "
        + " responsabile_cognome=?, responsabile_email=?, responsabile_telefono=?, data_soppressione=?, "
        + " telefono=?, fax=?, indi_dug=?, indi_toponimo=?, indi_civico=?, indi_cap=?, indi_comune=?, provincia_id=?, email=?,"
        + " dipartimento_codice=?, dipartimento_descrizione=?, tipo_aoo=?, codi_documento_doc=?, "
        + " row_updated_user=?, row_updated_time=?, versione=?, dipendenza_titolario_ufficio=?, titolario_livello_minimo=?, " 
        + "flag_documento_readable=?, flag_ricerca_full=?, fattura_id_committente=?, ga_abilitata=?, ga_username=?, ga_pwd=?, ga_flag_invio=?, ga_timer=?, anni_visibilita_bacheca=?, max_righe=?, flag_p7m=? WHERE aoo_id=? and versione=?";

public final static String UPDATE_MAIL_CONFIG = "UPDATE MAIL_CONFIG"
        + " SET   pec_email=?, pec_username=?, pec_password=?, pec_abilitata=?,pec_ssl_port=?, pec_pop3_host=?,"
        + " pec_smtp_host=?, pec_smtp_port=?, pn_email=?, pn_username=?, pn_password=?, pn_use_ssl=?, pn_ssl_port=?,"
        + " pn_pop3_host=?, pn_smtp_host=?, mail_timer=?, pn_abilitata=?, prec_email_invio=?, prec_email_ricezione=?, prec_username=?, prec_password=?,"
        + " prec_smtp_host=? WHERE aoo_id=?";

public final static String UPDATE_DATA_ULTIMA_PEC_AREA_ORGANIZZATIVA = "UPDATE MAIL_CONFIG SET data_ultima_pec=? WHERE aoo_id=?";


}