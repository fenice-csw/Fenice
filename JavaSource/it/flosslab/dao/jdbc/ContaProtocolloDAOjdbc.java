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

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.flosslab.mvc.presentation.integration.ContaProtocolloDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

public class ContaProtocolloDAOjdbc implements ContaProtocolloDAO {
	static Logger logger = Logger.getLogger(ContaProtocolloDAOjdbc.class
			.getName());

	private JDBCManager jdbcMan = new JDBCManager();

	public int contaCheckPostaInternaView(int caricaId, int flagNotifica)
			throws DataException {

		int contaCheck = 0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String strQuery = "SELECT count(check_id) FROM check_posta_interna WHERE carica_assegnante_id=? AND check_presa_visione=?";
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, caricaId);
			pstmt.setInt(2, flagNotifica);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				contaCheck = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("contaCheckPostaInternaView", e);
			throw new DataException("Cannot load contaCheckPostaInternaView");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return contaCheck;
	}

	public int contaProtocolliAlert(Utente utente) throws DataException {
		Connection connection = null;
		String query = "select count(p.protocollo_id) from PROTOCOLLI p, PROTOCOLLO_ASSEGNATARI a "
				+ "where date(now())-date(p.data_registrazione) >= p.giorni_alert AND p.protocollo_id =a.protocollo_id"
				+ " AND stato_protocollo IN('S','R','F','N','V') AND registro_id=? AND ufficio_assegnatario_id=? ";
		PreparedStatement pstmt = null;
		int count = 0;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, utente.getRegistroInUso());
			pstmt.setInt(2, utente.getUfficioInUso());
			rs = pstmt.executeQuery();
			if (rs.next())
				count = rs.getInt(1);
		} catch (Exception e) {
			logger.error("contaProtocolliAlert", e);
			throw new DataException("contaProtocolliAlert");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return count;
	}

	public int contaProtocolliEvidenza(Utente utente) throws DataException {
		Connection connection = null;
		String query = "SELECT count(prot.protocollo_id) FROM PROTOCOLLI prot LEFT JOIN protocollo_procedimenti prot_proc ON (prot.protocollo_id=prot_proc.protocollo_id) LEFT JOIN procedimenti proc ON (prot_proc.procedimento_id=proc.procedimento_id) LEFT JOIN procedimento_istruttori istr ON (proc.procedimento_id=istr.procedimento_id) WHERE prot.data_scadenza >= now() AND prot.flag_tipo ='U' AND istr.carica_id=?";
		PreparedStatement pstmt = null;
		int count = 0;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, utente.getCaricaInUso());
			rs = pstmt.executeQuery();
			if (rs.next())
				count = rs.getInt(1);
		} catch (Exception e) {
			logger.error("contaProtocolliEvidenza", e);
			throw new DataException("contaProtocolliEvidenza");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return count;
	}

	public int contaProtocolliAllacciabili(Utente utente,
			int numeroProtocolloDa, int numeroProtocolloA, int annoProtocollo,
			int protocolloId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int numeroProtocolli = 0;
		StringBuffer strQuery = new StringBuffer(CONTA_ALLACCI);
		Organizzazione org = Organizzazione.getInstance();
		Ufficio uff = org.getUfficio(utente.getUfficioVOInUso().getId()
				.intValue());
		if (!uff.getValueObject().getTipo().equals(UfficioVO.UFFICIO_CENTRALE)) {
			String ufficiUtenti = uff.getListaUfficiDiscendentiIdStr(utente);
			strQuery.append(" AND (EXISTS (SELECT * FROM ").append(
					"protocollo_assegnatari ass ").append(
					"WHERE ass.protocollo_id=p.protocollo_id ").append(
					"AND ass.ufficio_assegnatario_id IN (")
					.append(ufficiUtenti).append(")) OR").append(
							" p.ufficio_protocollatore_id IN (").append(
							ufficiUtenti).append(") OR").append(
							" p.ufficio_mittente_id IN (").append(ufficiUtenti)
					.append("))");
		}
		try {
			if (numeroProtocolloDa > 0) {
				strQuery.append(" AND NUME_PROTOCOLLO>=" + numeroProtocolloDa);
			}
			if (numeroProtocolloA > 0) {
				strQuery.append(" AND NUME_PROTOCOLLO<=" + numeroProtocolloA);
			}
			if (annoProtocollo > 0) {
				strQuery.append(" AND ANNO_REGISTRAZIONE=" + annoProtocollo);
			}
			strQuery
					.append(" ORDER BY p.ANNO_REGISTRAZIONE DESC, p.NUME_PROTOCOLLO DESC");
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery.toString());
			pstmt.setInt(1, utente.getRegistroInUso());
			pstmt.setInt(2, utente.getRegistroPostaInterna());
			pstmt.setInt(3, protocolloId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				numeroProtocolli = rs.getInt(1);
			}

		} catch (Exception e) {
			logger.error("getProtocolliAllacciabili", e);
			throw new DataException("Cannot load getProtocolliAllacciabili");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return numeroProtocolli;
	}

	public int contaProtocolliAssegnati(Utente utente, int annoProtocolloDa,
			int annoProtocolloA, int numeroProtocollo, String tipoUtenteUfficio)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int numeroProtocolliAssegnati = 0;

		String strQuery = "SELECT count (distinct p.registro_anno_numero) "
				+ " FROM PROTOCOLLI p, PROTOCOLLO_ASSEGNATARI a "
				+ " WHERE registro_id=? AND NOT flag_tipo ='P' "
				+ " AND p.protocollo_id =a.protocollo_id AND ";

		if (annoProtocolloDa > 0) {
			strQuery = strQuery + " ANNO_REGISTRAZIONE>=" + annoProtocolloDa
					+ " AND ";
		}
		if (annoProtocolloA > 0) {
			strQuery = strQuery + " ANNO_REGISTRAZIONE<=" + annoProtocolloA
					+ " AND ";
		}
		if (numeroProtocollo > 0) {
			strQuery = strQuery + " NUME_PROTOCOLLO=" + numeroProtocollo
					+ " AND ";
		}
		if ("T".equals(tipoUtenteUfficio)) {
			strQuery = strQuery
					+ " NOT stato_protocollo IN ('C','P') AND carica_assegnatario_id=?" //AND flag_competente=1 "
					+ " AND NOT EXISTS (select assegnatario_id from protocollo_assegnatari WHERE protocollo_id = p.protocollo_id AND stat_assegnazione='R' AND carica_assegnatario_id="
					+ utente.getCaricaInUso() + ") AND a.check_lavorato=0 AND a.flag_competente=1";
		} else if ("U".equals(tipoUtenteUfficio)) {
			strQuery = strQuery
					+ " NOT stato_protocollo IN ('C','P') AND ufficio_assegnatario_id="
					+ utente.getUfficioInUso()
					+ " AND carica_assegnatario_id IS NULL AND a.check_lavorato=0 AND a.flag_competente=1";//AND flag_competente=1  and check_lavorato=0";
		}
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);

			int currPstmt = 1;
			pstmt.setInt(currPstmt++, utente.getRegistroInUso());
			if ("T".equals(tipoUtenteUfficio)) {
				pstmt.setInt(currPstmt++, utente.getCaricaInUso());
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				numeroProtocolliAssegnati = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("contaProtocolliAssegnati", e);
			throw new DataException("contaProtocolliAssegnati");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return numeroProtocolliAssegnati;

	}

	@Deprecated
	public int contaProtocolliAssegnati(Utente utente, int annoProtocolloDa,
			int annoProtocolloA, int numeroProtocolloDa, int numeroProtocolloA,
			java.util.Date dataDa, java.util.Date dataA,
			String statoProtocollo, String statoScarico,
			String tipoUtenteUfficio, String user) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int numeroProtocolliAssegnati = 0;

		String strQuery = "SELECT count (p.protocollo_id) "
				+ " FROM PROTOCOLLI p, PROTOCOLLO_ASSEGNATARI a "
				+ " WHERE registro_id=? "
				+ " AND p.protocollo_id =a.protocollo_id AND ";

		if (annoProtocolloDa > 0) {
			strQuery = strQuery + " ANNO_REGISTRAZIONE>=" + annoProtocolloDa
					+ " AND";
		}
		if (annoProtocolloA > 0) {
			strQuery = strQuery + " ANNO_REGISTRAZIONE<=" + annoProtocolloA
					+ " AND";
		}
		if (numeroProtocolloDa > 0) {
			strQuery = strQuery + " NUME_PROTOCOLLO>=" + numeroProtocolloDa
					+ " AND";
		}
		if (dataDa != null) {
			strQuery = strQuery + " data_registrazione >=? AND";
		}
		if (dataA != null) {
			strQuery = strQuery + " data_registrazione <=? AND";
		}
		if (numeroProtocolloA > 0) {
			strQuery = strQuery + " NUME_PROTOCOLLO<=" + numeroProtocolloA
					+ " AND";
		}
		if ("N".equals(statoScarico) && "T".equals(tipoUtenteUfficio)) {
			strQuery = strQuery
					+ " stato_protocollo IN('"
					+ statoScarico
					+ "','F','S','V') AND carica_assegnatario_id=? AND flag_competente=1 "
					+ " AND NOT EXISTS (select assegnatario_id from protocollo_assegnatari WHERE protocollo_id=p.protocollo_id AND stat_assegnazione='R' AND carica_assegnatario_id="
					+ utente.getCaricaInUso()
					+ ") "
					+ " AND NOT EXISTS "
					+ "(SELECT f.fascicolo_id FROM fascicolo_protocolli f WHERE f.protocollo_id=p.protocollo_id AND f.row_created_user='"
					+ user + "'); ";
		} else if ("S".equals(statoScarico) && "U".equals(tipoUtenteUfficio)) {
			strQuery = strQuery
					+ " stato_protocollo IN('"
					+ statoScarico
					+ "','R','F','N','V','A') AND ufficio_assegnatario_id IN ("
					+ utente.getUfficiIds()
					+ ") AND carica_assegnatario_id IS NULL AND flag_competente=1 AND EXISTS (SELECT r.carica_id FROM referenti_uffici_cariche r WHERE r.carica_id="
					+ utente.getCaricaInUso()
					+ " AND r.ufficio_id=a.ufficio_assegnatario_id)"
					+ " AND NOT EXISTS (SELECT carica_id from cariche where utente_id IN"
					+ "(select utente_id from utenti where user_name IN (select f.row_created_user from fascicolo_protocolli f WHERE f.protocollo_id=p.protocollo_id )) AND ufficio_id ="
					+ utente.getUfficioInUso() + ");";

		} else if ("A".equals(statoScarico) && "T".equals(tipoUtenteUfficio)) {
			strQuery = strQuery
					+ " stato_protocollo='"
					+ statoScarico
					+ "' AND carica_assegnatario_id=? AND flag_competente=1"
					+ " AND NOT EXISTS (select assegnatario_id from protocollo_assegnatari WHERE protocollo_id=p.protocollo_id AND stat_assegnazione='R' AND carica_assegnatario_id="
					+ utente.getCaricaInUso()
					+ ") "
					+ " AND NOT EXISTS "
					+ "(SELECT f.fascicolo_id FROM fascicolo_protocolli f WHERE f.protocollo_id=p.protocollo_id AND f.row_created_user='"
					+ user + "'); ";
		}
		if ("R".equals(statoScarico)) {
			strQuery = strQuery
					+ " stato_protocollo='"
					+ statoScarico
					+ "' AND carica_assegnatario_id=? AND flag_competente=1"
					+ " AND NOT EXISTS (select assegnatario_id from protocollo_assegnatari WHERE protocollo_id=p.protocollo_id AND stat_assegnazione='R' AND carica_assegnatario_id="
					+ utente.getCaricaInUso()
					+ ") "
					+ " AND NOT EXISTS "
					+ "(SELECT f.fascicolo_id FROM fascicolo_protocolli f WHERE f.protocollo_id=p.protocollo_id AND f.row_created_user='"
					+ user + "'); ";
		}
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);

			int currPstmt = 1;
			pstmt.setInt(currPstmt++, utente.getRegistroInUso());

			if (dataDa != null) {
				pstmt.setDate(currPstmt++, new java.sql.Date(dataDa.getTime()));
			}
			if (dataA != null) {
				pstmt.setTimestamp(currPstmt++, new Timestamp(dataA.getTime()
						+ Constants.GIORNO_MILLISECONDS - 1));
			}
			if (("N".equals(statoScarico) && "T".equals(tipoUtenteUfficio))
					|| "R".equals(statoScarico)
					|| ("A".equals(statoScarico) && "T"
							.equals(tipoUtenteUfficio))) {
				pstmt.setInt(currPstmt++, utente.getCaricaInUso());
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				numeroProtocolliAssegnati = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("contaProtocolliAssegnati", e);
			throw new DataException("contaProtocolliAssegnati");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return numeroProtocolliAssegnati;

	}

	public int contaProtocolliPerCruscotti(int id, int annoProtocolloDa,
			int annoProtocolloA, String param, String tipo)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int numeroProtocolliAssegnati = 0;
		String strQuery = "SELECT count (DISTINCT  p.protocollo_id) "
				+ " FROM PROTOCOLLI p LEFT JOIN protocollo_assegnatari a ON a.protocollo_id=p.protocollo_id "
				+ " WHERE ";
		if (annoProtocolloDa > 0) {
			strQuery = strQuery + " ANNO_REGISTRAZIONE>=" + annoProtocolloDa
					+ " AND";
		}
		if (annoProtocolloA > 0) {
			strQuery = strQuery + " ANNO_REGISTRAZIONE<=" + annoProtocolloA
					+ " AND";
		}
		// "A","C","F","N","R","S","V"

		if (tipo.equals("U")) {
			strQuery = strQuery
					+ " ufficio_assegnatario_id ="
					+ param
					+ " AND NOT stato_protocollo IN('C','P') AND flag_competente=1 AND check_lavorato=0";
			// strQuery +=
			// " AND NOT EXISTS (SELECT carica_id from cariche where utente_id IN"
			// +
			// "(select utente_id from utenti where user_name IN (select f.row_created_user from fascicolo_protocolli f WHERE f.protocollo_id=p.protocollo_id )) AND ufficio_id ="
			// + param + ");";
		}
		if (tipo.equals("T"))
			strQuery = strQuery
					+ " carica_assegnatario_id ="
					+ id
					+ " AND NOT stato_protocollo IN('C','P') AND flag_competente=1 AND check_lavorato=0";
		// +
		// " AND NOT EXISTS (SELECT * FROM fascicolo_protocolli f WHERE f.protocollo_id=p.protocollo_id AND f.row_created_user='"
		// + param + "') AND flag_competente=1";
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				numeroProtocolliAssegnati = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("contaProtocolliAssegnati", e);
			throw new DataException("contaProtocolliAssegnati");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return numeroProtocolliAssegnati;

	}

	public int contaAssegnatariPerCruscotti(int caricaId, int annoProtocolloDa,
			int annoProtocolloA, String uffId, String tipo)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int numeroProtocolliAssegnati = 0;
		String strQuery = "SELECT count (a.assegnatario_id) "
				+ " FROM PROTOCOLLI p LEFT JOIN protocollo_assegnatari a ON a.protocollo_id=p.protocollo_id "
				+ " WHERE ";
		if (annoProtocolloDa > 0) {
			strQuery = strQuery + " ANNO_REGISTRAZIONE>=" + annoProtocolloDa
					+ " AND";
		}
		if (annoProtocolloA > 0) {
			strQuery = strQuery + " ANNO_REGISTRAZIONE<=" + annoProtocolloA
					+ " AND";
		}
		if (tipo.equals("U")) {
			strQuery = strQuery
					+ " ufficio_assegnatario_id ="
					+ uffId
					+ " AND NOT stato_protocollo IN('C','P') AND flag_competente=1 AND check_lavorato=0";
			// strQuery +=
			// " AND NOT EXISTS (SELECT carica_id from cariche where utente_id IN"
			// +
			// "(select utente_id from utenti where user_name IN (select f.row_created_user from fascicolo_protocolli f WHERE f.protocollo_id=p.protocollo_id )) AND ufficio_id ="
			// + id + ");";
		}
		if (tipo.equals("T"))
			strQuery = strQuery
					+ " carica_assegnatario_id ="
					+ caricaId
					+ " AND NOT stato_protocollo IN('C','P') AND flag_competente=1";
		// "AND NOT EXISTS (SELECT * FROM fascicolo_protocolli f WHERE f.protocollo_id=p.protocollo_id AND f.row_created_user='"+
		// param + "') AND flag_competente=1";
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				numeroProtocolliAssegnati = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("contaProtocolliAssegnati", e);
			throw new DataException("contaProtocolliAssegnati");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return numeroProtocolliAssegnati;

	}

	public int contaProtocolliRespinti(Utente utente, int annoProtocolloDa,
			int annoProtocolloA, int numeroProtocolloDa, int numeroProtocolloA,
			java.util.Date dataDa, java.util.Date dataA) throws DataException {

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int numeroProtocolliRespinti = 0;
		String strQuery = "SELECT count (DISTINCT  p.protocollo_id)"
				+ " FROM PROTOCOLLI P LEFT JOIN protocollo_assegnatari a ON a.protocollo_id=p.protocollo_id"
				+ " WHERE p.registro_id=? AND flag_competente=1"
				+ " AND stat_assegnazione='R' "
				+ " AND carica_assegnatario_id =" + utente.getCaricaInUso();
		if (annoProtocolloDa > 0) {
			strQuery = strQuery + " AND ANNO_REGISTRAZIONE>="
					+ annoProtocolloDa + " ";
		}
		if (annoProtocolloA > 0) {
			strQuery = strQuery + " AND ANNO_REGISTRAZIONE<=" + annoProtocolloA;
		}
		if (numeroProtocolloDa > 0) {
			strQuery = strQuery + " AND  NUME_PROTOCOLLO>="
					+ numeroProtocolloDa;
		}
		if (dataDa != null) {
			strQuery = strQuery + " AND  data_registrazione >=? ";
		}
		if (dataA != null) {
			strQuery = strQuery + "  AND data_registrazione <=? ";
		}
		if (numeroProtocolloA > 0) {
			strQuery = strQuery + " AND  NUME_PROTOCOLLO<=" + numeroProtocolloA;
		}

		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			int currPstmt = 1;
			pstmt.setInt(currPstmt++, utente.getRegistroInUso());
			if (dataDa != null) {
				pstmt.setDate(currPstmt++, new java.sql.Date(dataDa.getTime()));
			}
			if (dataA != null) {
				pstmt.setTimestamp(currPstmt++, new Timestamp(dataA.getTime()
						+ Constants.GIORNO_MILLISECONDS - 1));
			}

			rs = pstmt.executeQuery();
			if (rs.next()) {
				numeroProtocolliRespinti = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("contaProtocolliRespinti", e);
			throw new DataException("Cannot load contaProtocolliRespinti");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return numeroProtocolliRespinti;

	}

	public int contaProtocolliRespintiUfficio(Utente utente,
			int annoProtocolloDa, int annoProtocolloA, int numeroProtocolloDa,
			int numeroProtocolloA, java.util.Date dataDa, java.util.Date dataA)
			throws DataException {

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int numeroProtocolliRespinti = 0;
		String strQuery = "SELECT count (DISTINCT  p.protocollo_id)"
				+ " FROM PROTOCOLLI p LEFT JOIN protocollo_assegnatari a ON a.protocollo_id=p.protocollo_id"
				+ " WHERE p.registro_id=? AND flag_competente=1"
				+ " AND stat_assegnazione='R' "
				+ " AND ufficio_assegnatario_id =" + utente.getUfficioInUso()
				+ " AND carica_assegnatario_id IS NULL";

		if (annoProtocolloDa > 0) {
			strQuery = strQuery + " AND ANNO_REGISTRAZIONE>="
					+ annoProtocolloDa + " ";
		}
		if (annoProtocolloA > 0) {
			strQuery = strQuery + " AND ANNO_REGISTRAZIONE<=" + annoProtocolloA;
		}
		if (numeroProtocolloDa > 0) {
			strQuery = strQuery + " AND  NUME_PROTOCOLLO>="
					+ numeroProtocolloDa;
		}
		if (dataDa != null) {
			strQuery = strQuery + " AND  data_registrazione >=? ";
		}
		if (dataA != null) {
			strQuery = strQuery + "  AND data_registrazione <=? ";
		}
		if (numeroProtocolloA > 0) {
			strQuery = strQuery + " AND  NUME_PROTOCOLLO<=" + numeroProtocolloA;
		}

		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			int currPstmt = 1;
			pstmt.setInt(currPstmt++, utente.getRegistroInUso());
			if (dataDa != null) {
				pstmt.setDate(currPstmt++, new java.sql.Date(dataDa.getTime()));
			}
			if (dataA != null) {
				pstmt.setTimestamp(currPstmt++, new Timestamp(dataA.getTime()
						+ Constants.GIORNO_MILLISECONDS - 1));
			}

			rs = pstmt.executeQuery();
			if (rs.next()) {
				numeroProtocolliRespinti = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("contaProtocolliRespintiUfficio", e);
			throw new DataException(
					"Cannot load contaProtocolliRespintiUfficio");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return numeroProtocolliRespinti;

	}

	/*
	 public static String getBooleOperator(String tipoProtocollo, String key){
		String boole=" AND ";
		if(key.contains("ufficio_protocollatore_id"))
				return" AND ( "+key;
		if(key.contains("ufficio_assegnatario_id"))
			if(tipoProtocollo.equals("I"))
				return " OR "+key+") ";
			else
				return " OR "+key+" ";
		if(key.contains("ufficio_mittente_id"))
			return " OR "+key+") ";
		return boole+key;
	}
	*/
	
	public int contaProtocolli(Utente utente, Ufficio uff, LinkedHashMap<String,Object> sqlDB)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int numeroProtocolli = 0;
		StringBuffer strQuery = new StringBuffer(SELECT_COUNT__PROTOCOLLI);

		if (sqlDB != null) {
			for (Iterator<Entry<String,Object>> it = sqlDB.entrySet().iterator(); it.hasNext();) {
				Map.Entry<String,Object> entry = it.next();
				String key = entry.getKey();
				strQuery.append(" AND ").append(key);
			}
		}
		
		int indiceQuery = 1;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery.toString());
			pstmt.setInt(indiceQuery++, utente.getAreaOrganizzativa().getId());
			if (sqlDB != null) {
				for (Iterator<Entry<String,Object>> it = sqlDB.entrySet().iterator(); it.hasNext();) {
					Map.Entry<String,Object> entry = it.next();
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
						pstmt.setTimestamp(indiceQuery, new java.sql.Timestamp(
								d.getTime()));
					} else if (value instanceof Boolean) {
						pstmt.setBoolean(indiceQuery, ((Boolean) value)
								.booleanValue());
					} else if (value instanceof Long) {
						pstmt.setLong(indiceQuery, ((Long) value).longValue());
					}
					indiceQuery++;
				}
			}
			rs = pstmt.executeQuery();
			if (rs.next()) {
				numeroProtocolli = rs.getInt(1);
			}

		} catch (Exception e) {
			logger.error("contaProtocolli", e);
			e.printStackTrace();
			throw new DataException("Cannot load contaProtocolli");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return numeroProtocolli;

	}

	
	public int contaPostaInternaRepertorio(Utente utente) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int numeroProtocolliAssegnati = 0;

		String strQuery = "SELECT count (DISTINCT  p.registro_anno_numero) "
				+ " FROM PROTOCOLLI p LEFT JOIN protocollo_assegnatari a ON a.protocollo_id=p.protocollo_id  "
				+ " WHERE flag_tipo='P' AND NOT stato_protocollo ='C' AND flag_repertorio=1 "
				//+ "AND carica_assegnatario_id=? AND check_lavorato=0";
				+ " AND ufficio_assegnatario_id = " + utente.getUfficioInUso()
				+ " AND carica_assegnatario_id IS NULL AND check_lavorato=0";
				
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				numeroProtocolliAssegnati = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("contaPostaInterna", e);
			throw new DataException("contaProtocolliAssegnati");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return numeroProtocolliAssegnati;
	}
	
	public int contaPostaInternaAssegnata(Utente utente,
			String tipoUtenteUfficio) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int numeroProtocolliAssegnati = 0;

		String strQuery = "SELECT count (DISTINCT  p.registro_anno_numero) "
				+ " FROM PROTOCOLLI p LEFT JOIN protocollo_assegnatari a ON a.protocollo_id=p.protocollo_id  "
				+ " WHERE flag_tipo='P' AND NOT stato_protocollo ='C' AND flag_repertorio=0 AND ";
		if ("T".equals(tipoUtenteUfficio)) {
			strQuery = strQuery
					+ " carica_assegnatario_id=? AND check_lavorato=0";
		} else if ("U".equals(tipoUtenteUfficio)) {
			strQuery = strQuery + " ufficio_assegnatario_id = "
					+ utente.getUfficioInUso() + " ";
			strQuery = strQuery
					+ " AND carica_assegnatario_id IS NULL AND (check_presa_visione IS NULL OR check_presa_visione=0) AND check_lavorato=0";
		}
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			int currPstmt = 1;
			if ("T".equals(tipoUtenteUfficio))
				pstmt.setInt(currPstmt++, utente.getCaricaInUso());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				numeroProtocolliAssegnati = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("contaPostaInterna", e);
			throw new DataException("contaProtocolliAssegnati");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return numeroProtocolliAssegnati;
	}

	public int contaFatture(Utente utente, int registro,
			String tipoUtenteUfficio) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int numeroProtocolliAssegnati = 0;
		String strQuery = "SELECT count (DISTINCT  p.registro_anno_numero) "
				+ " FROM PROTOCOLLI p LEFT JOIN protocollo_assegnatari a ON a.protocollo_id=p.protocollo_id  "
				+ " WHERE aoo_id=? AND registro_id=? " + " AND ";
		if ("T".equals(tipoUtenteUfficio)) {
			strQuery = strQuery
					+ " NOT stat_assegnazione='R' AND check_lavorato=0 AND carica_assegnatario_id="
					+ utente.getCaricaInUso();
		} else if ("U".equals(tipoUtenteUfficio)) {
			strQuery = strQuery
					+ " ufficio_assegnatario_id = "
					+ utente.getUfficioInUso()
					+ " AND carica_assegnatario_id IS NULL AND (check_presa_visione IS NULL OR check_presa_visione=0) AND check_lavorato=0";
		} else if ("R".equals(tipoUtenteUfficio)) {
			strQuery = strQuery
					+ " flag_competente=1 AND stat_assegnazione='R' AND carica_assegnatario_id ="
					+ utente.getCaricaInUso();
		}
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			int currPstmt = 1;
			pstmt.setInt(currPstmt++, utente.getAreaOrganizzativa().getId());
			pstmt.setInt(currPstmt++, registro);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				numeroProtocolliAssegnati = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("contaPostaInterna", e);
			throw new DataException("contaProtocolliAssegnati");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return numeroProtocolliAssegnati;
	}

	public int contaPostaInternaAssegnataPerNumero(Utente utente,
			int numero, String tipoUtenteUfficio) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int numeroProtocolliAssegnati = 0;

		String strQuery = "SELECT count (p.protocollo_id) "
				+ " FROM PROTOCOLLI p LEFT JOIN protocollo_assegnatari a ON a.protocollo_id=p.protocollo_id  "
				//+ " WHERE registro_id=? "
				+ " WHERE flag_tipo='P'"
				+ " AND NOT EXISTS (SELECT * FROM fascicolo_protocolli f WHERE f.protocollo_id=p.protocollo_id AND f.row_created_user='"
				+ utente.getValueObject().getUsername() + "') AND  ";
		if ("T".equals(tipoUtenteUfficio)) {
			strQuery = strQuery
					+ " carica_assegnatario_id=? AND check_lavorato=0 AND ";
			// +
			// " AND NOT EXISTS (SELECT * FROM fascicolo_protocolli f WHERE f.protocollo_id=p.protocollo_id AND f.row_created_user='"
			// + utente.getValueObject().getUsername() + "')  AND ";

		} else if ("U".equals(tipoUtenteUfficio)) {
			strQuery = strQuery + " ufficio_assegnatario_id IN ("
					+ utente.getUfficiIds() + ") ";
			strQuery = strQuery
					+ " AND carica_assegnatario_id IS NULL AND (check_presa_visione IS NULL OR check_presa_visione=0) "
					+ " AND check_lavorato=0 AND ";
			// +
			// " AND NOT EXISTS (SELECT carica_id from cariche where utente_id IN"
			// +
			// "(select utente_id from utenti where user_name IN (select f.row_created_user from fascicolo_protocolli f WHERE f.protocollo_id=p.protocollo_id )) AND ufficio_id ="
			// + utente.getUfficioInUso() + ") AND ";
		}
		strQuery = strQuery + " nume_protocollo=? ";
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			int currPstmt = 1;
			//pstmt.setInt(currPstmt++, registro);
			if ("T".equals(tipoUtenteUfficio))
				pstmt.setInt(currPstmt++, utente.getCaricaInUso());
			pstmt.setInt(currPstmt++, numero);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				numeroProtocolliAssegnati = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("contaPostaInterna", e);
			throw new DataException("contaProtocolliAssegnati");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return numeroProtocolliAssegnati;
	}

	public int getPerConoscenza(Utente utente, String tipoUtenteUfficio)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int numeroProtocolliPerConoscenza = 0;
		String strQuery = "SELECT count (p.protocollo_id) "
				+ " FROM PROTOCOLLI p LEFT JOIN protocollo_assegnatari a ON a.protocollo_id=p.protocollo_id"
				+ " WHERE registro_id=? AND";
		
		
		if ("T".equals(tipoUtenteUfficio)) {
			strQuery = strQuery
					+ " NOT stato_protocollo IN ('C','P') " 
					+ " AND carica_assegnatario_id=? AND flag_competente=0 AND (check_presa_visione IS NULL OR check_presa_visione=0)";
		} else if (("U".equals(tipoUtenteUfficio))) {
			strQuery = strQuery + " NOT stato_protocollo IN('C','P') AND a.ufficio_assegnatario_id = "
					+ utente.getUfficioInUso()
					+ " AND carica_assegnatario_id IS NULL AND flag_competente=0 AND (check_presa_visione IS NULL OR check_presa_visione=0)";
		}
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			int currPstmt = 1;
			pstmt.setInt(currPstmt++, utente.getRegistroInUso());
			if ("T".equals(tipoUtenteUfficio))
				pstmt.setInt(currPstmt++, utente.getCaricaInUso());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				numeroProtocolliPerConoscenza = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("contaPostaInterna", e);
			throw new DataException("contaProtocolliAssegnati");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return numeroProtocolliPerConoscenza;
	}

	public int getPerConoscenzaZonaLavoro(Utente utente,
			String tipoUtenteUfficio) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int numeroProtocolliPerConoscenza = 0;
		String strQuery = "SELECT count (p.protocollo_id) "
				+ " FROM PROTOCOLLI p LEFT JOIN protocollo_assegnatari a ON a.protocollo_id=p.protocollo_id"
				+ " WHERE registro_id=? AND check_lavorato=0 AND";
		// +
		// " NOT EXISTS (SELECT * FROM fascicolo_protocolli f WHERE f.protocollo_id=p.protocollo_id ) AND";
		if ("T".equals(tipoUtenteUfficio)) {
			strQuery = strQuery
					+ " carica_assegnatario_id=? AND flag_competente=0 AND (check_presa_visione IS NULL OR check_presa_visione=0)";
		} else if (("U".equals(tipoUtenteUfficio))) {
			strQuery = strQuery + " ufficio_assegnatario_id IN ("
					+ utente.getUfficiIds() + ") ";
			strQuery = strQuery
					+ " AND carica_assegnatario_id IS NULL AND flag_competente=0 AND (check_presa_visione IS NULL OR check_presa_visione=0)";
		}
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			int currPstmt = 1;
			pstmt.setInt(currPstmt++, utente.getRegistroInUso());
			if ("T".equals(tipoUtenteUfficio))
				pstmt.setInt(currPstmt++, utente.getCaricaInUso());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				numeroProtocolliPerConoscenza = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("contaPostaInterna", e);
			throw new DataException("contaProtocolliAssegnati");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return numeroProtocolliPerConoscenza;
	}

	public final static String CONTA_ALLACCI = "SELECT count( "
			+ "p.protocollo_id)"
			+ "FROM protocolli p"
			+ " LEFT JOIN protocollo_assegnatari a ON a.protocollo_id=p.protocollo_id"
			+ " LEFT JOIN uffici uf ON a.ufficio_assegnatario_id=uf.ufficio_id"
			+ " LEFT JOIN cariche ut ON a.carica_assegnatario_id=ut.carica_id"
			+ " LEFT JOIN protocollo_destinatari d ON d.protocollo_id=p.protocollo_id "
			+ "WHERE p.registro_id IN (?,?) AND p.protocollo_id!=? ";

	public final static String SELECT_COUNT__PROTOCOLLI = "SELECT COUNT(distinct p.protocollo_id)"
			+ " FROM protocolli p"
			+ " LEFT JOIN protocollo_assegnatari a ON a.protocollo_id=p.protocollo_id"
			+ " LEFT JOIN uffici uf ON a.ufficio_assegnatario_id=uf.ufficio_id"
			+ " LEFT JOIN cariche ut ON a.carica_assegnatario_id=ut.carica_id"
			+ " LEFT JOIN protocollo_destinatari d ON d.protocollo_id=p.protocollo_id "
			+ " LEFT JOIN protocollo_mittenti m ON m.protocollo_id=p.protocollo_id "
			+ " WHERE p.aoo_id=? ";

}
