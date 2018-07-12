package it.finsiel.siged.dao.jdbc;

import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.integration.EvidenzaDAO;
import it.finsiel.siged.mvc.presentation.helper.FascicoloView;
import it.finsiel.siged.mvc.presentation.helper.ProcedimentoView;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.finsiel.siged.util.DateUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

public class EvidenzaDAOjdbc implements EvidenzaDAO {
    static Logger logger = Logger.getLogger(SoggettoDAOjdbc.class.getName());

    private JDBCManager jdbcMan = new JDBCManager();

    public int contaEvidenzeProcedimenti(Utente utente, HashMap<String,String> sqlDB)
            throws DataException {
        int numeroProcedimenti = 0;
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        StringBuffer strQuery = new StringBuffer(
                "SELECT count(P.procedimento_id) FROM PROCEDIMENTI P, UFFICI U"
                        + " WHERE P.AOO_ID=? AND P.DATA_EVIDENZA IS NOT NULL "
                        + "AND U.UFFICIO_ID=P.UFFICIO_ID ");
        if (sqlDB != null) {
            for (Iterator<Entry<String, String>> it = sqlDB.entrySet().iterator(); it.hasNext();) {
                Map.Entry<String, String> entry =  it.next();
                Object key = entry.getKey();
                strQuery.append(" AND ").append(key.toString());
            }
        }

        int indiceQuery = 1;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(strQuery.toString());
            int aooId = utente.getUfficioVOInUso().getAooId();
            pstmt.setInt(indiceQuery++, aooId);
            if (sqlDB != null) {
                for (Iterator<Entry<String, String>> it = sqlDB.entrySet().iterator(); it.hasNext();) {
                    Map.Entry<String, String> entry = it.next();
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    if (value instanceof Integer) {
                        pstmt.setInt(indiceQuery, ((Integer) value).intValue());
                    } else if (value instanceof String) {
                        if (key.toString().indexOf("LIKE") > 0) {
                            pstmt
                                    .setString(indiceQuery, value.toString()
                                            + "%");
                        } else {
                            pstmt.setString(indiceQuery, value.toString());
                        }
                    } else if (value instanceof java.util.Date) {
                        java.util.Date d = (java.util.Date) value;
                        pstmt.setDate(indiceQuery, new java.sql.Date(d
                                .getTime()));
                    } else if (value instanceof Boolean) {
                        pstmt.setBoolean(indiceQuery, ((Boolean) value)
                                .booleanValue());
                    }
                    indiceQuery++;
                }
            }

            logger.info(strQuery);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                numeroProcedimenti = rs.getInt(1);
            }
        } catch (Exception e) {
            logger.error("Load contaFaldoni", e);
            throw new DataException("Cannot load contaProcedimenti");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
            jdbcMan.close(connection);
        }
        return numeroProcedimenti;
    }

    public int contaEvidenzeFascicoli(Utente utente, HashMap<String, String> sqlDB)
            throws DataException {
        int numeroFascicoli = 0;
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        Organizzazione org = Organizzazione.getInstance();
        Ufficio uff = org.getUfficio(utente.getUfficioVOInUso().getId()
                .intValue());
        String ufficiUtenti = uff.getListaUfficiDiscendentiIdStr(utente);

        StringBuffer strQuery = new StringBuffer(
                "SELECT count(F.fascicolo_id) FROM FASCICOLI F, UFFICI U"
                        + " WHERE F.AOO_ID=? AND F.DATA_EVIDENZA IS NOT NULL "
                        + " AND U.UFFICIO_ID=F.UFFICIO_INTESTATARIO_ID");

        if (!uff.getValueObject().getTipo().equals(UfficioVO.UFFICIO_CENTRALE)
                && uff.getValueObject().getParentId() != 0) {
            strQuery.append(" AND (ufficio_responsabile_id IN (").append(
                    ufficiUtenti).append(") OR ufficio_intestatario_id IN (")
                    .append(ufficiUtenti).append(")");

            strQuery
                    .append(
                            " OR EXISTS (SELECT ufficio_intestatario_id from storia_fascicoli S WHERE "
                                    + "s.aoo_id = ? AND (s.ufficio_intestatario_id IN (")
                    .append(ufficiUtenti).append(
                            ") OR s.ufficio_responsabile_id IN (").append(
                            ufficiUtenti).append(
                            ")) AND f.fascicolo_id =s.fascicolo_id ))");
        }

        if (sqlDB != null) {
            for (Iterator<Entry<String, String>> it = sqlDB.entrySet().iterator(); it.hasNext();) {
                Map.Entry<String, String> entry = it.next();
                Object key = entry.getKey();
                strQuery.append(" AND ").append(key.toString());
            }
        }

        int indiceQuery = 1;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(strQuery.toString());
            int aooId = utente.getUfficioVOInUso().getAooId();
            pstmt.setInt(indiceQuery++, aooId);
            if (!uff.getValueObject().getTipo().equals(
                    UfficioVO.UFFICIO_CENTRALE)
                    && uff.getValueObject().getParentId() != 0) {
                pstmt.setInt(indiceQuery++, utente.getAreaOrganizzativa()
                        .getId().intValue());
            }

            if (sqlDB != null) {
                for (Iterator<Entry<String, String>> it = sqlDB.entrySet().iterator(); it.hasNext();) {
                    Map.Entry<String, String> entry = it.next();
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    if (value instanceof Integer) {
                        pstmt.setInt(indiceQuery, ((Integer) value).intValue());
                    } else if (value instanceof String) {
                        if (key.toString().indexOf("LIKE") > 0) {
                            pstmt
                                    .setString(indiceQuery, value.toString()
                                            + "%");
                        } else {
                            pstmt.setString(indiceQuery, value.toString());
                        }
                    } else if (value instanceof java.util.Date) {
                        java.util.Date d = (java.util.Date) value;
                        pstmt.setDate(indiceQuery, new java.sql.Date(d
                                .getTime()));
                    } else if (value instanceof Boolean) {
                        pstmt.setBoolean(indiceQuery, ((Boolean) value)
                                .booleanValue());
                    }
                    indiceQuery++;
                }
            }

            logger.info(strQuery);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                numeroFascicoli = rs.getInt(1);
            }
        } catch (Exception e) {
            logger.error("Load contaFaldoni", e);
            throw new DataException("Cannot load contaProcedimenti");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
            jdbcMan.close(connection);
        }
        return numeroFascicoli;
    }

    public Collection<ProcedimentoView> getEvidenzeProcedimenti(Utente utente, HashMap<String, String> sqlDB)
            throws DataException {
        Collection<ProcedimentoView> evidenze = new ArrayList<ProcedimentoView>();
       
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        StringBuffer strQuery = new StringBuffer(
                "SELECT * FROM PROCEDIMENTI P, UFFICI U"
                        + " WHERE P.AOO_ID=? AND P.DATA_EVIDENZA IS NOT NULL "
                        + "AND U.UFFICIO_ID=P.UFFICIO_ID ");
        if (sqlDB != null) {
            for (Iterator<Entry<String, String>> it = sqlDB.entrySet().iterator(); it.hasNext();) {
                Map.Entry<String, String> entry =it.next();
                Object key = entry.getKey();
                strQuery.append(" AND ").append(key.toString());
            }
        }

        int indiceQuery = 1;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(strQuery.toString());
            int aooId = utente.getUfficioVOInUso().getAooId();
            pstmt.setInt(indiceQuery++, aooId);

            if (sqlDB != null) {
                for (Iterator<Entry<String, String>> it = sqlDB.entrySet().iterator(); it.hasNext();) {
                    Map.Entry<String, String> entry = it.next();
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    if (value instanceof Integer) {
                        pstmt.setInt(indiceQuery, ((Integer) value).intValue());
                    } else if (value instanceof String) {
                        if (key.toString().indexOf("LIKE") > 0) {
                            pstmt
                                    .setString(indiceQuery, value.toString()
                                            + "%");
                        } else {
                            pstmt.setString(indiceQuery, value.toString());
                        }
                    } else if (value instanceof java.util.Date) {
                        java.util.Date d = (java.util.Date) value;
                        pstmt.setDate(indiceQuery, new java.sql.Date(d
                                .getTime()));
                    } else if (value instanceof Boolean) {
                        pstmt.setBoolean(indiceQuery, ((Boolean) value)
                                .booleanValue());
                    }
                    indiceQuery++;
                }
            }
            logger.info(strQuery);
            rs = pstmt.executeQuery();
            ProcedimentoView procedimento;
            while (rs.next()) {
                procedimento = new ProcedimentoView();
                procedimento.setProcedimentoId(rs.getInt("PROCEDIMENTO_ID"));
                procedimento.setNumeroProcedimento(rs.getString("NUMERO_PROCEDIMENTO"));
                procedimento.setOggetto(rs.getString("OGGETTO"));
                procedimento.setUfficioId(rs.getInt("UFFICIO_ID"));
                procedimento.setDescUfficioId(rs.getString("DESCRIZIONE"));
                procedimento.setDataEvidenza(DateUtil.formattaData(rs.getDate(
                        "DATA_EVIDENZA").getTime()));
                evidenze.add(procedimento);
            }
        } catch (Exception e) {
            logger.error("get elenco Evidenze", e);
            throw new DataException(
                    "Cannot load elenco getEvidenzeProcedimenti");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
            jdbcMan.close(connection);
        }
        return evidenze;

    }

    public Collection<FascicoloView> getEvidenzeFascicoli(Utente utente, HashMap<String, String> sqlDB)
            throws DataException {
        Collection<FascicoloView> evidenze = new ArrayList<FascicoloView>();
      
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        StringBuffer strQuery = new StringBuffer(
                "SELECT * FROM FASCICOLI F, UFFICI U "
                        + " WHERE F.AOO_ID=? AND F.DATA_EVIDENZA IS NOT NULL "
                        + " AND U.UFFICIO_ID=F.UFFICIO_INTESTATARIO_ID");
        if (sqlDB != null) {
            for (Iterator<Entry<String, String>> it = sqlDB.entrySet().iterator(); it.hasNext();) {
                Map.Entry<String, String> entry =  it.next();
                Object key = entry.getKey();
                strQuery.append(" AND ").append(key.toString());
            }
        }

        int indiceQuery = 1;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(strQuery.toString());
            int aooId = utente.getUfficioVOInUso().getAooId();
            pstmt.setInt(indiceQuery++, aooId);

            if (sqlDB != null) {
                for (Iterator<Entry<String, String>> it = sqlDB.entrySet().iterator(); it.hasNext();) {
                    Map.Entry<String, String> entry =  it.next();
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    if (value instanceof Integer) {
                        pstmt.setInt(indiceQuery, ((Integer) value).intValue());
                    } else if (value instanceof String) {
                        if (key.toString().indexOf("LIKE") > 0) {
                            pstmt
                                    .setString(indiceQuery, value.toString()
                                            + "%");
                        } else {
                            pstmt.setString(indiceQuery, value.toString());
                        }
                    } else if (value instanceof java.util.Date) {
                        java.util.Date d = (java.util.Date) value;
                        pstmt.setDate(indiceQuery, new java.sql.Date(d
                                .getTime()));
                    } else if (value instanceof Boolean) {
                        pstmt.setBoolean(indiceQuery, ((Boolean) value)
                                .booleanValue());
                    }
                    indiceQuery++;
                }
            }
            logger.info(strQuery);
            rs = pstmt.executeQuery();
            FascicoloView fascicolo;
            while (rs.next()) {
                fascicolo = new FascicoloView();
                fascicolo.setFascicoloId(rs.getInt("FASCICOLO_ID"));
                fascicolo.setProgressivo(rs.getInt("PROGRESSIVO"));
                fascicolo.setAnnoRiferimento(rs.getInt("anno_riferimento"));
                fascicolo.setOggetto(rs.getString("OGGETTO"));
                fascicolo.setUfficioIntestatarioId(rs
                        .getInt("UFFICIO_INTESTATARIO_ID"));
                fascicolo.setDescUfficioIntestatarioId(rs
                        .getString("DESCRIZIONE"));
                fascicolo.setDataApertura(DateUtil.formattaData(rs.getDate(
                        "DATA_APERTURA").getTime()));

                fascicolo.setDataEvidenza(DateUtil.formattaData(rs.getDate(
                        "DATA_EVIDENZA").getTime()));
                // da scommentare quando referente ok
                // fascicolo.setReferente(rs.getString("REFERENTE"));
                // // fascicolo.setdocumentoId(rs.getString(""));

                evidenze.add(fascicolo);
            }
        } catch (Exception e) {
            logger.error("get elenco Evidenze", e);
            throw new DataException("Cannot load elenco getEvidenzeFascicoli");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(pstmt);
            jdbcMan.close(connection);
        }
        return evidenze;

    }

}
