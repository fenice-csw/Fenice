/*
*
* Copyright 2008 Flosslab s.r.l. (http://www.flosslab.it) All rights reserved.
*
* This file is part of e-prot 1.1 software.
* e-prot 1.1 is a free software; 
* you can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2008 Flosslab s.r.l. (http://www.flosslab.it) All rights reserved.
* Version: e-prot 1.1
*/
package it.flosslab.dao.jdbc;

import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.flosslab.mvc.presentation.integration.MittentiDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class MittentiDAOjdbc implements MittentiDAO {
    static Logger logger = Logger.getLogger(MittentiDAOjdbc.class
            .getName());

    private JDBCManager jdbcMan = new JDBCManager();

    
    private static final String INSERT_MITTENTE = "INSERT INTO protocollo_mittenti (mittente_id, indirizzo, email, descrizione,tipo, citta, provincia, protocollo_id, versione, codice_postale,civico,soggetto_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
    private static final String GET_MITTENTI = "SELECT * FROM protocollo_mittenti WHERE protocollo_id=?";
    private static final String GET_STORIA_MITTENTI = "SELECT * FROM storia_protocollo_mittenti WHERE protocollo_id=? AND versione=?";

   

	public void saveMittente(Connection conn, SoggettoVO mittente, int idProtocollo, int idMittente,int versione) throws DataException {
		PreparedStatement pstmt = null;
		 try {
            if (conn == null) {
                logger.warn("newOggetto() - Invalid Connection :" + conn);
                throw new DataException(
                        "Connessione alla base dati non valida.");
            }
                pstmt = conn.prepareStatement(INSERT_MITTENTE);
                pstmt.setInt(1, idMittente);
                pstmt.setString(2, mittente.getIndirizzo().getToponimo());
                pstmt.setString(3, mittente.getIndirizzoEMail());
                pstmt.setString(4, mittente.getDescrizione());
                pstmt.setString(5, mittente.getTipo());       
                pstmt.setString(6, mittente.getIndirizzo().getComune());
                pstmt.setInt(7,  mittente.getIndirizzo().getProvinciaId());
                pstmt.setInt(8, idProtocollo);
                pstmt.setInt(9, versione);
                pstmt.setString(10, mittente.getIndirizzo().getCap());
                pstmt.setString(11,  mittente.getIndirizzo().getCivico());
                if(mittente.getId()!=null && mittente.getId()!=0)
                	pstmt.setInt(12, mittente.getId());
                else
                	pstmt.setNull(12, Types.INTEGER);
                pstmt.executeUpdate();
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("Save Mittente", e);
            throw new DataException("error.database.cannotsave");
        } finally {
            jdbcMan.close(pstmt);
        }
        
		
	}

	public List<SoggettoVO> getMittenti(Connection conn, int protocolloId) throws DataException {
		List<SoggettoVO> mittenti = new ArrayList<SoggettoVO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
           if (conn == null) {
               logger.warn("newOggetto() - Invalid Connection :" + conn);
               throw new DataException(
                       "Connessione alla base dati non valida.");
           }
		   pstmt = conn.prepareStatement(GET_MITTENTI);
		   pstmt.setInt(1, protocolloId);
		   rs = pstmt.executeQuery();
		   while(rs.next()){
			   SoggettoVO mittente = new SoggettoVO(rs.getString("tipo"));
			   mittente.setDescrizione(rs.getString("descrizione"));
			   mittente.getIndirizzo().setToponimo(rs.getString("indirizzo"));
			   mittente.getIndirizzo().setCivico(rs.getString("civico"));
			   mittente.getIndirizzo().setComune(rs.getString("citta"));
			   mittente.getIndirizzo().setCap(rs.getString("codice_postale"));
			   mittente.getIndirizzo().setProvinciaId(rs.getInt("provincia"));
			   mittente.setIndirizzoEMail(rs.getString("email"));
			   mittente.setId(rs.getInt("soggetto_id"));
			   mittenti.add(mittente);
		   }
		   
		}catch (Exception e) {
		    logger.error("getMittenti", e);
		    throw new DataException("error.database.cannotsave");
		} finally {
		    jdbcMan.close(pstmt);
		}
		return mittenti;
	}

	public List<SoggettoVO> getStoriaMittenti(Connection conn, int protocolloId,int versione) throws DataException {
		List<SoggettoVO> mittenti = new ArrayList<SoggettoVO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
           if (conn == null) {
               logger.warn("newOggetto() - Invalid Connection :" + conn);
               throw new DataException(
                       "Connessione alla base dati non valida.");
           }
		   pstmt = conn.prepareStatement(GET_STORIA_MITTENTI);
		   pstmt.setInt(1, protocolloId);
		   pstmt.setInt(2, versione);
		   rs = pstmt.executeQuery();
		   while(rs.next()){
			   SoggettoVO mittente = new SoggettoVO(rs.getString("tipo"));
			   mittente.setDescrizione(rs.getString("descrizione"));
			   mittente.getIndirizzo().setToponimo(rs.getString("indirizzo"));
			   mittente.getIndirizzo().setCivico(rs.getString("civico"));
			   mittente.getIndirizzo().setComune(rs.getString("citta"));
			   mittente.getIndirizzo().setCap(rs.getString("codice_postale"));
			   mittente.getIndirizzo().setProvinciaId(rs.getInt("provincia"));
			   mittente.setIndirizzoEMail(rs.getString("email"));
			   mittente.setId(rs.getInt("soggetto_id"));
			   mittenti.add(mittente);
		   }
		   
		}catch (Exception e) {
		    logger.error("getStoriaMittenti", e);
		    throw new DataException("error.database.cannotsave");
		} finally {
		    jdbcMan.close(pstmt);
		}
		return mittenti;
	}
	
}