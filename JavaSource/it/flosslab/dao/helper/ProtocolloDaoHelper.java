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

package it.flosslab.dao.helper;

import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.util.DateUtil;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

public class ProtocolloDaoHelper {

	/**
	 * @param protocollo
	 * @param rs
	 * @throws SQLException
	 */
	public static void fillProtocolloVOFromRecord(ProtocolloVO protocollo,
			ResultSet rs) throws SQLException {
		protocollo.setId(rs.getInt("protocollo_id"));
		protocollo.setRowCreatedTime(rs.getTimestamp("row_created_time"));
		protocollo.setRowCreatedUser(rs.getString("row_created_user"));
		protocollo.setVersione(rs.getInt("versione"));
		protocollo.setAnnoRegistrazione(rs.getInt("anno_registrazione"));
		protocollo.setAooId(rs.getInt("aoo_id"));
		protocollo.setChiaveAnnotazione(rs.getString("annotazione_chiave"));
		protocollo.setCodDocumentoDoc(rs.getString("codi_documento_doc"));
		protocollo.setCognomeMittente(rs.getString("desc_cognome_mittente"));
		protocollo.setDataAnnullamento(rs.getDate("data_annullamento"));
		protocollo.setDataDocumento(rs.getDate("data_documento"));
		protocollo.setDataEffettivaRegistrazione(rs
				.getDate("data_effettiva_registrazione"));
		protocollo.setDataProtocolloMittente(rs
				.getDate("data_protocollo_mittente"));
		protocollo.setDataRegistrazione(rs.getTimestamp("data_registrazione"));
		protocollo.setDataRicezione(rs.getDate("data_ricezione"));
		protocollo.setDataScadenza(rs.getDate("data_scadenza"));
		protocollo.setDataScarico(rs.getDate("data_scarico"));
		protocollo.setDenominazioneMittente(rs
				.getString("desc_denominazione_mittente"));
		protocollo.setDescrizioneAnnotazione(rs
				.getString("annotazione_descrizione"));
		protocollo.setDocumentoPrincipaleId(rs.getInt("documento_id"));
		protocollo.setMozione(rs.getInt("flag_mozione") == 1);
		protocollo.setRiservato(rs.getInt("flag_riservato") == 1);
		protocollo.setFatturaElettronica(rs.getInt("flag_fattura_elettronica") == 1);
		protocollo.setFlagAnomalia(rs.getInt("flag_anomalia") == 1);
		protocollo.setFlagRepertorio(rs.getInt("flag_repertorio") == 1);
		protocollo.setFlagTipo(rs.getString("flag_tipo"));
		protocollo.setFlagTipoMittente(rs.getString("flag_tipo_mittente"));
		protocollo.setMittenteCap(rs.getString("indi_cap_mittente"));
		protocollo.setMittenteComune(rs.getString("indi_localita_mittente"));
		protocollo.setMittenteIndirizzo(rs.getString("indi_mittente"));
		protocollo.setMittenteNazione(rs.getString("indi_nazione_mittente"));
		protocollo.setMittenteProvinciaId(rs.getInt("indi_provincia_mittente"));
		protocollo.setNomeMittente(rs.getString("desc_nome_mittente"));
		protocollo.setNotaAnnullamento(rs.getString("text_nota_annullamento"));
		protocollo.setNumProtocollo(rs.getInt("nume_protocollo"));
		protocollo.setNumProtocolloMittente(rs
				.getString("nume_protocollo_mittente"));
		protocollo.setOggetto(rs.getString("text_oggetto"));
		protocollo.setPosizioneAnnotazione(rs
				.getString("annotazione_posizione"));
		protocollo.setProvvedimentoAnnullamento(rs
				.getString("text_provvedimento_annullament"));
		protocollo.setRegistroId(rs.getInt("registro_id"));
		protocollo.setStatoProtocollo(rs.getString("stato_protocollo"));
		protocollo.setTipoDocumentoId(rs.getInt("tipo_documento_id"));
		protocollo.setTitolarioId(rs.getInt("titolario_id"));
		protocollo.setUfficioMittenteId(rs.getInt("ufficio_mittente_id"));
		protocollo.setUfficioProtocollatoreId(rs
				.getInt("ufficio_protocollatore_id"));
		protocollo.setUtenteMittenteId(rs.getInt("utente_mittente_id"));
		protocollo.setCaricaProtocollatoreId(rs.getInt("carica_protocollatore_id"));
		protocollo.setUtenteProtocollatoreId(rs.getInt("utente_protocollatore_id"));
		protocollo.setNumProtocolloEmergenza(rs.getInt("num_prot_emergenza"));
		protocollo.setRowUpdatedUser(rs.getString("row_created_user"));
		protocollo.setGiorniAlert(rs.getInt("giorni_alert"));
		protocollo.setEmailId(rs.getInt("numero_email"));
		if (protocollo.getVersione() != 0)
			protocollo.setEstermiAutorizzazione(rs
					.getString("text_estremi_autorizzazione"));
		protocollo.setReturnValue(ReturnValues.FOUND);
	}

	/**
	 * @param protocollo
	 * @param pstmt
	 * @throws SQLException
	 */
	public static void createStatementForNewProtocollo(ProtocolloVO protocollo,
			PreparedStatement pstmt) throws SQLException {
		pstmt.setInt(1, protocollo.getId().intValue()); // protocollo_id
		pstmt.setInt(2, protocollo.getAnnoRegistrazione()); // ANNO_REGISTRAZIONE
		pstmt.setLong(3, protocollo.getNumProtocollo()); // NUME_PROTOCOLLO
		pstmt.setTimestamp(4, new Timestamp(protocollo.getDataRegistrazione()
				.getTime())); // data_registrazione
		pstmt.setString(5, protocollo.getFlagTipoMittente()); // FLAG_TIPO_MITTENTE
		pstmt.setString(6, protocollo.getOggetto()); // TEXT_OGGETTO
		pstmt.setString(7, protocollo.getFlagTipo());// FLAG_TIPO
		if (protocollo.getDataDocumento() == null) {
			pstmt.setNull(8, Types.DATE); // DATA_DOCUMENTO
		} else {
			pstmt.setDate(8, new Date(protocollo.getDataDocumento().getTime())); // DATA_DOCUMENTO
		}
		if (protocollo.getTipoDocumentoId() == 0) {
			pstmt.setNull(9, Types.INTEGER);
		} else {
			pstmt.setLong(9, protocollo.getTipoDocumentoId()); // tipo_documento_id
		}
		pstmt.setInt(10, protocollo.getAooId()); // aoo_id
		pstmt.setInt(11, protocollo.getRegistroId()); // registro_id
		pstmt.setInt(12, protocollo.getUtenteProtocollatoreId()); // utente_protocollatore_id
		pstmt.setInt(13, protocollo.getCaricaProtocollatoreId()); // carica_protocollatore_id
		pstmt.setInt(14, protocollo.getUfficioProtocollatoreId()); // ufficio_protocollatore_id
		pstmt.setString(15, protocollo.getDenominazioneMittente()); // DESC_DENOMINAZIONE_MITTENTE
		pstmt.setString(16, protocollo.getCognomeMittente()); // DESC_COGNOME_MITTENTE
		pstmt.setString(17, protocollo.getNomeMittente()); // DESC_NOME_MITTENTE
		pstmt.setString(18, protocollo.getMittenteIndirizzo()); // INDI_MITTENTE
		pstmt.setString(19, protocollo.getMittenteCap()); // INDI_CAP_MITTENTE
		pstmt.setString(20, protocollo.getMittenteComune()); // INDI_LOCALITA_MITTENTE
		pstmt.setInt(21, protocollo.getMittenteProvinciaId()); // INDI_PROVINCIA_MITTENTE
		pstmt.setString(22, protocollo.getChiaveAnnotazione()); // ANNOTAZIONE_CHIAVE
		pstmt.setString(23, protocollo.getPosizioneAnnotazione()); // ANNOTAZIONE_POSIZIONE
		pstmt.setString(24, protocollo.getDescrizioneAnnotazione()); // ANNOTAZIONE_DESCRIZIONE
		if (protocollo.getTitolarioId() == 0) {
			pstmt.setNull(25, Types.INTEGER);
		} else {
			pstmt.setInt(25, protocollo.getTitolarioId());
		}
		pstmt.setString(26, protocollo.getRowCreatedUser()); // ROW_CREATED_USER
		pstmt.setInt(27, protocollo.isRiservato() ? 1 : 0); // FLAG_RISERVATO
		if (!"I".equals(protocollo.getFlagTipo()) && !"R".equals(protocollo.getFlagTipo())) {
			pstmt.setInt(28, protocollo.getUfficioMittenteId());
			if (protocollo.getUtenteMittenteId() == 0) {
				pstmt.setNull(29, Types.INTEGER);
			} else {
				pstmt.setInt(29, protocollo.getUtenteMittenteId());
			}
			pstmt.setNull(30, Types.VARCHAR);
		} else {
			pstmt.setNull(28, Types.INTEGER);
			pstmt.setNull(29, Types.INTEGER);
			pstmt.setString(30, protocollo.getNumProtocolloMittente());
		}
		if (protocollo.getDataRicezione() == null) {
			pstmt.setNull(31, Types.DATE); // DATA_RICEZIONE
		} else {
			pstmt
					.setDate(31, new Date(protocollo.getDataRicezione()
							.getTime())); // DATA_DOCUMENTO
		}
		if (protocollo.getDocumentoPrincipaleId() == null) {
			pstmt.setNull(32, Types.INTEGER); // DATA_DOCUMENTO
		} else {
			pstmt.setInt(32, protocollo.getDocumentoPrincipaleId().intValue()); // DOCUMENTO_ID
		}
		pstmt.setString(33, protocollo.getStatoProtocollo());
		pstmt.setInt(34, protocollo.isMozione() ? 1 : 0);
		pstmt.setInt(35, protocollo.getNumProtocolloEmergenza());
		if (protocollo.getNumProtocolloEmergenza() > 0) {
			if (protocollo.getDataAnnullamento() == null) {
				pstmt.setNull(36, Types.DATE); // data_Annullamento
			} else {
				pstmt.setDate(36, new Date(protocollo.getDataAnnullamento()
						.getTime())); // data_Annullamento
			}
			pstmt.setString(37, protocollo.getProvvedimentoAnnullamento());
			pstmt.setString(38, protocollo.getNotaAnnullamento());
		} else {
			pstmt.setNull(36, Types.DATE); // data_Annullamento
			pstmt.setString(37, null);
			pstmt.setString(38, null);
		}
		pstmt.setTimestamp(39, new Timestamp(System.currentTimeMillis())); // DATA_EFFETTIVA_REGISTRAZIONE
		pstmt.setTimestamp(40, new Timestamp(System.currentTimeMillis())); // ROW_CREATED_TIME
		pstmt.setInt(41, protocollo.getVersione());
		pstmt.setLong(42, protocollo.getKey());
		if (protocollo.getFlagTipo().equals("R"))
			pstmt.setString(43, protocollo
					.getIntervalloNumProtocolloEmergenza());
		else
			pstmt.setString(43, "");
		if (protocollo.getGiorniAlert() != 0)
			pstmt.setInt(44, protocollo.getGiorniAlert());
		else
			pstmt.setNull(44, Types.INTEGER);
		if ((!"I".equals(protocollo.getFlagTipo()) && !"R".equals(protocollo
				.getFlagTipo()))
				|| protocollo.getDataProtocolloMittente() == null)
			pstmt.setNull(45, Types.DATE); // DATA_DOCUMENTO
		else
			pstmt.setDate(45, new Date(protocollo.getDataProtocolloMittente()
					.getTime())); // DATA_DOCUMENTO MITTENTE
		if (protocollo.getEmailId() != 0)
			pstmt.setInt(46, protocollo.getEmailId());
		else
			pstmt.setNull(46, Types.INTEGER);
		if (protocollo.getDataScadenza() == null) {
			pstmt.setNull(47, Types.DATE); // data_Annullamento
		} else {
			pstmt.setDate(47, new Date(protocollo.getDataScadenza().getTime())); // data_Annullamento
		}
		pstmt.setString(48, protocollo.getEstermiAutorizzazione());
		pstmt.setInt(49, protocollo.isFatturaElettronica() ? 1 : 0); // FLAG_Anomalia Email
		pstmt.setInt(50, protocollo.isFlagRepertorio() ? 1 : 0); // FLAG_Repertorio
		pstmt.setInt(51, protocollo.isFlagAnomalia() ? 1 : 0); // FLAG_Fattura_Elettronica
	}

	/**
	 * @param protocollo
	 * @param rs
	 * @throws SQLException
	 */
	public static void fillReportProtocolloViewFromRecord(
			ReportProtocolloView protocollo, ResultSet rs) throws SQLException {
		protocollo.setProtocolloId(rs.getInt("protocollo_id"));
		protocollo.setAnnoProtocollo(rs.getInt("anno_registrazione"));
		protocollo.setRegistroAnnoNumero(rs.getLong("registro_anno_numero"));
		protocollo.setNumeroProtocollo(rs.getInt("nume_protocollo"));
		protocollo.setTipoProtocollo(rs.getString("flag_tipo"));
		if (rs.getBoolean("flag_riservato")) {
			protocollo.setOggetto(Parametri.PROTOCOLLO_RISERVATO);
			protocollo.setMittente(Parametri.PROTOCOLLO_RISERVATO);
		} else {
			protocollo.setOggetto(rs.getString("text_oggetto"));
			StringBuffer mittente = new StringBuffer();
			if ("F".equals(rs.getString("flag_tipo_mittente"))) {
				mittente.append(rs.getString("desc_cognome_mittente"));
				if (rs.getString("desc_nome_mittente") != null) {
					mittente.append(' ').append(
							rs.getString("desc_nome_mittente"));
				}
			} else if ("M".equals(rs.getString("flag_tipo_mittente"))) {
				protocollo.setTipoMittente("M");
				mittente.append(Parametri.MULTI_MITTENTE);
			} else {
				mittente.append(rs.getString("desc_denominazione_mittente"));
			}
			protocollo.setMittente(mittente.toString());
		}
		protocollo.setDataProtocollo(DateUtil.formattaData(rs.getDate(
				"data_registrazione").getTime()));
		protocollo.setPdf(rs.getInt("documento_id") > 0);
		protocollo.setDocumentoId(rs.getInt("documento_id"));
		protocollo.setStatoProtocollo(rs.getString("stato_protocollo"));
	}

	/**
	 * @param protocollo
	 * @param pstmt
	 * @return
	 * @throws SQLException
	 */
	public static void createStatementToUpdateProtocollo(
			ProtocolloVO protocollo, PreparedStatement pstmt)
			throws SQLException {
		int n = 0;
		pstmt.setString(++n, protocollo.getFlagTipoMittente()); // flag_tipo_mittente
		pstmt.setString(++n, protocollo.getOggetto()); // text_oggetto
		pstmt.setString(++n, protocollo.getDenominazioneMittente()); // desc_denominazione_mittente
		pstmt.setString(++n, protocollo.getCognomeMittente()); // desc_cognome_mittente
		pstmt.setString(++n, protocollo.getNomeMittente()); // desc_nome_mittente
		pstmt.setString(++n, protocollo.getMittenteIndirizzo()); // indi_mittente
		pstmt.setString(++n, protocollo.getMittenteCap()); // indi_cap_mittente
		pstmt.setString(++n, protocollo.getMittenteComune()); // indi_localita_mittente
		pstmt.setInt(++n, protocollo.getMittenteProvinciaId()); // indi_provincia_mittente
		pstmt.setString(++n, protocollo.getChiaveAnnotazione()); // annotazione_chiave
		pstmt.setString(++n, protocollo.getPosizioneAnnotazione()); // annotazione_posizione
		pstmt.setString(++n, protocollo.getDescrizioneAnnotazione()); // annotazione_descrizione
		// aggiunto
		if (protocollo.getTipoDocumentoId() == 0) {
			pstmt.setNull(++n, Types.INTEGER);
		} else {
			pstmt.setLong(++n, protocollo.getTipoDocumentoId()); // tipo_documento_id
		}
		//
		if (protocollo.getTitolarioId() == 0) { // titolario_id
			pstmt.setNull(++n, Types.INTEGER);
		} else {
			pstmt.setInt(++n, protocollo.getTitolarioId());
		}
		pstmt.setString(++n, protocollo.getRowUpdatedUser()); // row_created_user
		pstmt.setTimestamp(++n, new Timestamp(System.currentTimeMillis())); // row_created_user
		if (!"I".equals(protocollo.getFlagTipo())) {
			pstmt.setInt(++n, protocollo.getUfficioMittenteId());
			// ufficio_mittente_id
			if (protocollo.getUtenteMittenteId() == 0) {
				// utente_mittente_id
				pstmt.setNull(++n, Types.INTEGER);
			} else {
				pstmt.setInt(++n, protocollo.getUtenteMittenteId());
			}
			pstmt.setNull(++n, Types.VARCHAR);

		} else {
			pstmt.setNull(++n, Types.INTEGER);
			pstmt.setNull(++n, Types.INTEGER);
			pstmt.setString(++n, protocollo.getNumProtocolloMittente());
		}

		if (protocollo.getDataRicezione() == null) {
			pstmt.setNull(++n, Types.DATE); // data_ricezione
		} else {
			pstmt.setDate(++n,
					new Date(protocollo.getDataRicezione().getTime())); // data_ricezione
		}
		if (protocollo.getDataDocumento() == null) {
			pstmt.setNull(++n, Types.DATE); // data_documento
		} else {
			pstmt.setDate(++n,
					new Date(protocollo.getDataDocumento().getTime())); // data_documento
		}
		if (protocollo.getDocumentoPrincipaleId() == null) {
			pstmt.setNull(++n, Types.INTEGER); // documento_id
		} else {
			pstmt.setInt(++n, protocollo.getDocumentoPrincipaleId().intValue()); // documento_id
		}
		pstmt.setString(++n, protocollo.getStatoProtocollo()); // stato_protocollo
		if (protocollo.getDataScarico() == null) {
			pstmt.setNull(++n, Types.DATE); // data_scarico
		} else {
			pstmt.setTimestamp(++n, new Timestamp(protocollo
					.getDataRegistrazione().getTime())); // data_scarico
		}
		pstmt.setInt(++n, protocollo.getNumProtocolloEmergenza()); // Numero
		// Protocollo
		// Emergenza
		pstmt.setString(++n, protocollo.getEstermiAutorizzazione()); // Estremi
		// autorizzazione
		pstmt.setString(++n, protocollo.getFlagTipo()); // Flag Tipo
		pstmt.setTimestamp(++n, new Timestamp(protocollo.getDataRegistrazione()
				.getTime())); // Data Registrazione

		if (!"I".equals(protocollo.getFlagTipo())
				|| protocollo.getDataProtocolloMittente() == null)
			pstmt.setNull(++n, Types.DATE);
		else
			pstmt.setDate(++n, new Date(protocollo.getDataProtocolloMittente()
					.getTime()));
		if (protocollo.getDataScadenza() == null) {
			pstmt.setNull(++n, Types.DATE); // data_scarico
		} else {
			pstmt.setTimestamp(++n, new Timestamp(protocollo.getDataScadenza()
					.getTime())); // data_scadenza
		}
		pstmt.setInt(++n, protocollo.isRiservato() ? 1 : 0); // FLAG_RISERVATO
		pstmt.setInt(++n, protocollo.isFatturaElettronica() ? 1 : 0); // FLAG_Fattura_Elettronica
		pstmt.setInt(++n, protocollo.isFlagRepertorio() ? 1 : 0); // FLAG_Repertorio
		pstmt.setInt(++n, protocollo.isFlagAnomalia() ? 1 : 0); // FLAG_Anomalia Email
		pstmt.setInt(++n, protocollo.getId().intValue()); // protocollo_id
		pstmt.setInt(++n, protocollo.getVersione() + 1); // versione

	}

}
