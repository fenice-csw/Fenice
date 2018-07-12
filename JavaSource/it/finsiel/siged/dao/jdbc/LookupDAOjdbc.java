package it.finsiel.siged.dao.jdbc;

import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.AreaOrganizzativa;
import it.finsiel.siged.mvc.integration.LookupDAO;
import it.finsiel.siged.mvc.vo.IdentityVO;
import it.finsiel.siged.mvc.vo.lookup.ProvinciaVO;
import it.finsiel.siged.mvc.vo.lookup.TipoDocumentoVO;
import it.finsiel.siged.mvc.vo.lookup.TipoProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.SpedizioneVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.flosslab.mvc.vo.OggettoVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

public class LookupDAOjdbc implements LookupDAO {
    static Logger logger = Logger.getLogger(LookupDAOjdbc.class.getName());

    private JDBCManager jdbcMan = new JDBCManager();

    public Collection<ProvinciaVO> getProvince() throws DataException {
        ArrayList<ProvinciaVO> province = new ArrayList<ProvinciaVO>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(SELECT_PROVINCE);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                province.add(new ProvinciaVO(rs.getLong("provincia_id"), rs
                        .getString("DESC_PROVINCIA")));
            }
        } catch (Exception e) {
            logger.error("Load Province", e);
            throw new DataException("Cannot load Province");
        } finally {
        	jdbcMan.closeAll(rs, pstmt, connection);
        }
        return province;
    }

    public int getProvinciaIdFromCodiProv(String codice) throws DataException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(SELECT_PROVINCIA_BY_COD);
            pstmt.setString(1, codice);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            logger.error("Load Province", e);
            throw new DataException("Cannot load Province");
        } finally {
        	jdbcMan.closeAll(rs, pstmt, connection);
        }
		return 0;
    }

    
    public Map<Integer,ArrayList<TipoDocumentoVO>> getTipiDocumento(Collection<AreaOrganizzativa> aoo) throws DataException {
        ArrayList<TipoDocumentoVO> tipiDocAoo;
        Map<Integer,ArrayList<TipoDocumentoVO>> tipiDoc = new HashMap<Integer,ArrayList<TipoDocumentoVO>>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = jdbcMan.getConnection();
            Iterator<AreaOrganizzativa> it = aoo.iterator();
            while (it.hasNext()) {
                tipiDocAoo = new ArrayList<TipoDocumentoVO>();
                AreaOrganizzativa areaOrg = (AreaOrganizzativa) it.next();

                pstmt = connection.prepareStatement(SELECT_TIPI_DOCUMENTI);
                pstmt.setInt(1, areaOrg.getValueObject().getId().intValue());
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    tipiDocAoo.add(new TipoDocumentoVO(rs
                            .getInt("tipo_documento_id"), rs
                            .getString("DESC_TIPO_DOCUMENTO")));
                }
                jdbcMan.close(pstmt);
                if (tipiDocAoo != null && tipiDocAoo.size() > 0){
                    tipiDoc.put(areaOrg.getValueObject().getId(), tipiDocAoo);
                }
            }
        } catch (Exception e) {
            logger.error("Load Tipi documenti", e);
            throw new DataException("Cannot load the Tipi documenti");
        } finally {
            jdbcMan.close(rs);

            jdbcMan.close(connection);
        }
        return tipiDoc;
    }

    public Map<Integer,ArrayList<TipoProcedimentoVO>> getTipiProcedimento(Collection<AreaOrganizzativa> aoo) throws DataException {
        ArrayList<TipoProcedimentoVO> tipiProcAoo;
        Map<Integer,ArrayList<TipoProcedimentoVO>> tipiProc = new HashMap<Integer,ArrayList<TipoProcedimentoVO>>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = jdbcMan.getConnection();
            Iterator<AreaOrganizzativa> it = aoo.iterator();
            while (it.hasNext()) {
                tipiProcAoo = new ArrayList<TipoProcedimentoVO>();
                AreaOrganizzativa areaOrg = (AreaOrganizzativa) it.next();

                pstmt = connection.prepareStatement(SELECT_TIPI_PROCEDIMENTO);
                
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    tipiProcAoo.add(new TipoProcedimentoVO(rs
                            .getInt("tipo_procedimenti_id"),0, rs
                            .getString("tipo")));
                }
                jdbcMan.close(pstmt);
                if (tipiProcAoo != null && tipiProcAoo.size() > 0)
                    tipiProc.put(areaOrg.getValueObject().getId(), tipiProcAoo);
            }
        } catch (Exception e) {
            logger.error("Load Tipi procediumenti", e);
            throw new DataException("Cannot load the Tipi procedimenti");
        } finally {
            jdbcMan.close(rs);

            jdbcMan.close(connection);
        }
        return tipiProc;
    }

    public SpedizioneVO getMezzoSpedizione(int mezzoId) throws DataException {
    	SpedizioneVO mezzo = new SpedizioneVO();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(SELECT_MEZZO_SPEDIZIONE_DA_ID);
            pstmt.setInt(1, mezzoId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                mezzo.setId(rs.getInt("spedizioni_id"));
                mezzo.setCodiceSpedizione(rs.getString("codi_spedizione"));
                mezzo.setDescrizioneSpedizione(rs.getString("desc_spedizione"));
                mezzo.setPrezzo(rs.getString("prezzo"));
            }
            jdbcMan.close(pstmt);
        } catch (Exception e) {
            logger.error("Load Mezzo di Spedizione", e);
            throw new DataException("Cannot load Mezzo di Spedizione");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(connection);
        }
        return mezzo;

    }

    public SpedizioneVO getMezzoSpedizioneDaMezzo(String mezzo)
            throws DataException {
    	SpedizioneVO mezzoVO = new SpedizioneVO();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection
                    .prepareStatement(SELECT_MEZZO_SPEDIZIONE_DA_STRING);
            pstmt.setString(1, mezzo);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                mezzoVO.setId(rs.getInt("spedizioni_id"));
                mezzoVO.setCodiceSpedizione(rs.getString("codi_spedizione"));
                mezzoVO.setDescrizioneSpedizione(rs.getString("desc_spedizione"));
                mezzoVO.setPrezzo(rs.getString("prezzo"));
            }
            jdbcMan.close(pstmt);
        } catch (Exception e) {
            logger.error("Load Mezzo di Spedizione", e);
            throw new DataException("Cannot load Mezzo di Spedizione");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(connection);
        }
        return mezzoVO;

    }

    public Map<Integer,ArrayList<SpedizioneVO>> getMezziSpedizione(Collection<AreaOrganizzativa> aoo) throws DataException {
        ArrayList<SpedizioneVO> mezziAoo;
        Map<Integer,ArrayList<SpedizioneVO>> mezzi = new HashMap<Integer,ArrayList<SpedizioneVO>>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = jdbcMan.getConnection();
            Iterator<AreaOrganizzativa> it = aoo.iterator();
            while (it.hasNext()) {
                mezziAoo = new ArrayList<SpedizioneVO>();
                AreaOrganizzativa areaOrg = (AreaOrganizzativa) it.next();

                pstmt = connection.prepareStatement(SELECT_MEZZI_SPEDIZIONE);
                pstmt.setInt(1, areaOrg.getValueObject().getId().intValue());
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    SpedizioneVO sped = new SpedizioneVO();
                    sped.setId(rs.getInt("spedizioni_id"));
                    sped.setCodiceSpedizione(rs.getString("codi_spedizione"));
                    sped.setDescrizioneSpedizione(rs.getString("desc_spedizione"));
                    sped.setPrezzo(rs.getString("prezzo"));
                    mezziAoo.add(sped);
                }
                jdbcMan.close(pstmt);
                if (mezziAoo != null && mezziAoo.size() > 0)
                    mezzi.put(areaOrg.getValueObject().getId(), mezziAoo);
            }
        } catch (Exception e) {
            logger.error("Load Mezzi di Spedizione", e);
            throw new DataException("Cannot load the Mezzi di Spedizione");
        } finally {
            jdbcMan.close(rs);
            jdbcMan.close(connection);
        }
        return mezzi;
    }

    public Collection<IdentityVO> getTitoliDestinatario() throws DataException {

        Collection<IdentityVO> listaTitoli = new ArrayList<IdentityVO>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = jdbcMan.getConnection();
            pstmt = connection.prepareStatement(SELECT_TITOLIDESTINATARI);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                IdentityVO titoloVO = new IdentityVO();
                titoloVO.setId(rs.getInt("id"));
                titoloVO.setDescription(rs.getString("descrizione"));
                listaTitoli.add(titoloVO);
            }
        } catch (Exception e) {
            logger.error("Load getTitoliDestinatari", e);
            throw new DataException("Cannot load getElencoTitoliDestinatari");
        } finally {
        	jdbcMan.closeAll(rs, pstmt, connection);
        }
        return listaTitoli;

    }

	public Map<String,OggettoVO> getOggetti(int aooId) throws DataException {
		 Map<String,OggettoVO> oggetti = new HashMap<String,OggettoVO>();
	        Connection connection = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        try {
	            connection = jdbcMan.getConnection();
	            pstmt = connection.prepareStatement(SELECT_OGGETTI);
	            pstmt.setInt(1, aooId);
	            rs = pstmt.executeQuery();
	            while (rs.next()) {
	                OggettoVO oggettoVO = new OggettoVO();
	                oggettoVO.setOggettoId(rs.getInt("id"));
	                oggettoVO.setAooId(aooId);
	                oggettoVO.setGiorniAlert(rs.getInt("giorni_alert"));
	                oggettoVO.setDescrizione(rs.getString("descrizione"));
	                oggetti.put(oggettoVO.getDescrizione(),oggettoVO);
	            }
	        } catch (Exception e) {
	            logger.error("Load getTitoliDestinatari", e);
	            throw new DataException("Cannot load getElencoTitoliDestinatari");
	        } finally {
	        	jdbcMan.closeAll(rs, pstmt, connection);
	        }
	        return oggetti;
	}

    
    private final static String SELECT_TITOLIDESTINATARI = "SELECT * FROM titoli_destinatari "
            + " ORDER BY descrizione";

    public final static String SELECT_MEZZI_SPEDIZIONE = "SELECT * FROM SPEDIZIONI where aoo_id=? and flag_abilitato=1 ORDER BY desc_spedizione";

    public final static String SELECT_MEZZO_SPEDIZIONE_DA_ID = "SELECT * FROM SPEDIZIONI where spedizioni_id=?";

    public final static String SELECT_MEZZO_SPEDIZIONE_DA_STRING = "SELECT * FROM SPEDIZIONI where desc_spedizione=?";

    public final static String SELECT_TIPI_DOCUMENTI = "SELECT tipo_documento_id, DESC_TIPO_DOCUMENTO, aoo_id FROM TIPI_DOCUMENTO WHERE aoo_id=? ORDER BY aoo_id, DESC_TIPO_DOCUMENTO";

    public final static String SELECT_TIPI_PROCEDIMENTO = "SELECT * FROM TIPI_PROCEDIMENTO ORDER BY tipo";

    public final static String SELECT_PROVINCE = "SELECT provincia_id, DESC_PROVINCIA FROM PROVINCE ORDER BY DESC_PROVINCIA";
    
    public final static String SELECT_PROVINCIA_BY_COD = "SELECT provincia_id FROM PROVINCE where codi_provincia=?";

    public final static String SELECT_OGGETTI = "SELECT * FROM oggetti WHERE aoo_id=?;";

	
};