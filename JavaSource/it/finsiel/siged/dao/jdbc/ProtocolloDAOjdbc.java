package it.finsiel.siged.dao.jdbc;

import it.compit.fenice.model.protocollo.PostaInterna;
import it.compit.fenice.mvc.presentation.helper.ProtocolloProcedimentoView;
import it.compit.fenice.mvc.presentation.helper.ReportCheckPostaInternaView;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.NomiTabelle;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.ProtocolloIngresso;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.business.DocumentoDelegate;
import it.finsiel.siged.mvc.business.IdentificativiDelegate;
import it.finsiel.siged.mvc.integration.ProtocolloDAO;
import it.finsiel.siged.mvc.presentation.helper.AllaccioView;
import it.finsiel.siged.mvc.presentation.helper.MittenteView;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.mvc.vo.protocollo.SegnaturaVO;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.StringUtil;
import it.flosslab.dao.helper.ProtocolloDaoHelper;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public class ProtocolloDAOjdbc implements ProtocolloDAO {

	static Logger logger = Logger.getLogger(ProtocolloDAOjdbc.class.getName());

	private JDBCManager jdbcMan = new JDBCManager();

	public Map<String,ProtocolloProcedimentoView> getProtocolliEvidenza(Utente utente) throws DataException {

		Map<String,ProtocolloProcedimentoView> evidenze = new HashMap<String,ProtocolloProcedimentoView>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String strQuery = "SELECT proc.procedimento_id,proc.numero_procedimento,proc.anno as anno_procedimento, "
				+ " prot.protocollo_id,prot.nume_protocollo,prot.anno_registrazione,prot.documento_id,prot.text_oggetto,"
				+ " storia.text_estremi_autorizzazione,prot.data_scadenza"
				+ " FROM PROTOCOLLI prot "
				+ " LEFT JOIN protocollo_procedimenti prot_proc ON (prot.protocollo_id=prot_proc.protocollo_id)"
				+ " LEFT JOIN procedimenti proc ON (prot_proc.procedimento_id=proc.procedimento_id)"
				+ " LEFT JOIN procedimento_istruttori istr ON (proc.procedimento_id=istr.procedimento_id)"
				+ " LEFT JOIN storia_protocolli storia ON (prot.protocollo_id=storia.protocollo_id)"
				+ " WHERE prot.data_scadenza >= now() AND prot.flag_tipo ='U' AND storia.versione=0 AND istr.carica_id=? ";
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, utente.getCaricaInUso());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ProtocolloProcedimentoView p = new ProtocolloProcedimentoView();
				p.setProtocolloId(rs.getInt("protocollo_id"));
				p.setNumeroProtocollo(rs.getInt("NUME_PROTOCOLLO"));
				p.setAnnoProtocollo(rs.getInt("ANNO_REGISTRAZIONE"));
				p.setDataScadenza(DateUtil.formattaData(rs.getDate(
						"data_scadenza").getTime()));
				p.setPdf(rs.getInt("DOCUMENTO_ID") > 0 ? true : false);
				p.setDocumentoId(rs.getInt("DOCUMENTO_ID"));
				p.setMotivazioni(rs.getString("text_estremi_autorizzazione"));
				p.setOggetto(rs.getString("text_oggetto"));
				p.setProcedimentoId(rs.getInt("procedimento_id"));
				p.setNumeroProcedimento(rs.getString("numero_procedimento"));
				p.setAnnoProcedimento(rs.getInt("anno_procedimento"));
				evidenze.put(String.valueOf(p.getProtocolloId()), p);
			}
		} catch (Exception e) {
			logger.error("getProtocolliEvidenze", e);
			throw new DataException("Cannot load getProtocolliEvidenze");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return evidenze;
	}

	public Map<String,ReportProtocolloView> getProtocolliAlert(Utente utente) throws DataException {

		SortedMap<String,ReportProtocolloView> protocolli = new TreeMap<String,ReportProtocolloView>(new Comparator<String>() {
			public int compare(String i1, String i2) {
				Integer o1= Integer.valueOf(i1);
				Integer o2= Integer.valueOf(i2);
				return o2.compareTo(o1);
			}
		});
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String strQuery = "SELECT p.*, a.ufficio_assegnante_id, a.ufficio_assegnatario_id, "
				+ " a.carica_assegnante_id, a.carica_assegnatario_id, a.messaggio "
				+ " from PROTOCOLLI p, PROTOCOLLO_ASSEGNATARI a "
				+ "where date(now())-date(p.data_registrazione) >= p.giorni_alert AND p.protocollo_id =a.protocollo_id "
				+ "AND stato_protocollo IN ('S','R','F','N','V') AND registro_id=? AND ufficio_assegnatario_id=? ";

		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, utente.getRegistroInUso());
			pstmt.setInt(2, utente.getUfficioInUso());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ReportProtocolloView protocollo = new ReportProtocolloView();
				protocollo.setProtocolloId(rs.getInt("protocollo_id"));
				protocollo.setNumeroProtocollo(rs.getInt("NUME_PROTOCOLLO"));
				protocollo.setAnnoProtocollo(rs.getInt("ANNO_REGISTRAZIONE"));
				protocollo.setGiorniAlert(rs.getInt("giorni_alert"));
				if ("M".equals(rs.getString("flag_tipo_mittente"))) {
					protocollo.setTipoMittente("M");
					protocollo.setMittente(Parametri.MULTI_MITTENTE);
				}
				protocollo.setDataProtocollo(DateUtil.formattaData(rs.getDate(
						"DATA_REGISTRAZIONE").getTime()));

				if (rs.getInt("FLAG_RISERVATO") == 1) {
					protocollo.setOggetto(Parametri.PROTOCOLLO_RISERVATO);
					protocollo.setMittente(Parametri.PROTOCOLLO_RISERVATO);

				} else {
					protocollo.setOggetto(rs.getString("TEXT_OGGETTO"));
					protocollo.setMittente(StringUtil.getStringa(rs
							.getString("DESC_COGNOME_MITTENTE"))
							+ " "
							+ StringUtil.getStringa(rs
									.getString("DESC_NOME_MITTENTE"))
							+ StringUtil.getStringa(rs
									.getString("DESC_DENOMINAZIONE_MITTENTE")));
				}
				protocollo.setUfficio(rs.getString("ufficio_assegnante_id"));
				protocollo.setUtenteAssegnante(rs
						.getString("carica_assegnante_id"));
				protocollo.setUfficioAssegnatario(rs
						.getString("ufficio_assegnatario_id"));
				protocollo.setUtenteAssegnatario(rs
						.getString("carica_assegnatario_id"));
				protocollo.setPdf(rs.getInt("DOCUMENTO_ID") > 0 ? true : false);
				protocollo.setDocumentoId(rs.getInt("DOCUMENTO_ID"));
				protocollo.setMessaggio(StringUtil.getStringa(rs
						.getString("messaggio")));
				if (rs.getDate("data_scarico") != null)
					protocollo.setDataScarico(DateUtil.formattaData(rs.getDate(
							"data_scarico").getTime()));
				protocolli.put(String.valueOf(protocollo.getProtocolloId()),
						protocollo);
			}
		} catch (Exception e) {
			logger.error("getProtocolliAlert", e);
			throw new DataException("Cannot load getProtocolliAlert");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return protocolli;
	}

	public ProtocolloVO getProtocolloById(int id) throws DataException {
		ProtocolloVO protOut = new ProtocolloVO();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			protOut = getProtocolloById(connection, id);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " ProtocolloId :" + id);
		} finally {
			jdbcMan.close(connection);
		}
		return protOut;
	}

	public ReportProtocolloView getProtocolloView(int id) throws DataException {
		ReportProtocolloView protOut = new ReportProtocolloView();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			protOut = getProtocolloView(connection, id);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " ProtocolloId :" + id);
		} finally {
			jdbcMan.close(connection);
		}
		return protOut;
	}

	public ReportProtocolloView getProtocolloView(Connection connection, int id)
			throws DataException {
		ReportProtocolloView protocollo = new ReportProtocolloView();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("getProtocolloView() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_PROTOCOLLO_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				ProtocolloDaoHelper.fillReportProtocolloViewFromRecord(
						protocollo, rs);
			} else {
				protocollo = null;
			}
		} catch (Exception e) {
			logger.error("Load ProtocolloView by ID", e);
			throw new DataException("Cannot load ProtocolloView by ID");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return protocollo;
	}

	public ProtocolloVO getProtocolloById(Connection connection, int id)
			throws DataException {
		ProtocolloVO protocollo = new ProtocolloVO();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("getProtocolloById() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_PROTOCOLLO_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				ProtocolloDaoHelper.fillProtocolloVOFromRecord(protocollo, rs);
			} else {
				protocollo.setReturnValue(ReturnValues.NOT_FOUND);
			}

		} catch (Exception e) {
			logger.error("Load Protocollo by ID", e);
			throw new DataException("Cannot load Protocollo by ID");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return protocollo;
	}

	private void archiviaVersione(Connection connection, int protocolloId)
			throws DataException {
		String[] tables = { "protocolli", "protocollo_allacci",
				"protocollo_allegati", "protocollo_annotazioni",
				"protocollo_assegnatari", "protocollo_destinatari",
				"protocollo_mittenti" };
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("storia - Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			for (int i = 0; i < tables.length; i++) {
				String sql = "INSERT INTO storia_" + tables[i]
						+ " SELECT * FROM " + tables[i]
						+ " WHERE protocollo_id = ?";
				pstmt = connection.prepareStatement(sql);
				pstmt.setInt(1, protocolloId);
				pstmt.executeUpdate();
				jdbcMan.close(pstmt);
				sql = "UPDATE " + tables[i]
						+ " SET versione = versione+1 WHERE protocollo_id = ?";
				pstmt = connection.prepareStatement(sql);
				pstmt.setInt(1, protocolloId);
				pstmt.executeUpdate();
				jdbcMan.close(pstmt);
			}
		} catch (Exception e) {
			logger.error("storia protocollo" + protocolloId, e);
			throw new DataException("Cannot insert Storia Protocollo");

		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public ProtocolloVO newProtocollo(Connection connection,
			ProtocolloVO protocollo) throws DataException {
		ProtocolloVO newProtocollo = new ProtocolloVO();
		newProtocollo.setReturnValue(ReturnValues.UNKNOWN);
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("newProtocollo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(INSERT_PROTOCOLLO);
			ProtocolloDaoHelper.createStatementForNewProtocollo(protocollo,
					pstmt);
			pstmt.executeUpdate();

			newProtocollo = getProtocolloById(connection, protocollo.getId()
					.intValue());
			if (newProtocollo.getReturnValue() == ReturnValues.FOUND) {
				newProtocollo.setReturnValue(ReturnValues.SAVED);
			} else {
				logger.warn("Errore nella lettura del ProtocolloVO dopo il salvataggio! Id:"
						+ protocollo.getId().intValue());
				throw new DataException(
						"Errore nel salvataggio del Protocollo.");
			}

		} catch (SQLException e) {
			logger.error("Save Protocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return newProtocollo;
	}

	public ProtocolloVO newStoriaProtocollo(Connection connection,
			ProtocolloVO protocollo) throws DataException {
		ProtocolloVO newProtocollo = new ProtocolloVO();
		newProtocollo.setReturnValue(ReturnValues.UNKNOWN);
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("newProtocollo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(INSERT_STORIA_PROTOCOLLO);
			ProtocolloDaoHelper.createStatementForNewProtocollo(protocollo,
					pstmt);
			pstmt.executeUpdate();

			newProtocollo = getProtocolloById(connection, protocollo.getId()
					.intValue());
			if (newProtocollo.getReturnValue() == ReturnValues.FOUND) {
				newProtocollo.setReturnValue(ReturnValues.SAVED);
				// logger.debug("Protocollo inserito:" +
				// protocollo.getId().intValue());
			} else {
				logger.warn("Errore nella lettura del ProtocolloVO dopo il salvataggio! Id:"
						+ protocollo.getId().intValue());
				throw new DataException(
						"Errore nel salvataggio del Protocollo.");
			}
		} catch (SQLException e) {
			logger.error("Save Protocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return newProtocollo;
	}

	public ProtocolloVO aggiornaProtocollo(Connection connection,
			ProtocolloVO protocollo) throws DataException {
		protocollo.setReturnValue(ReturnValues.UNKNOWN);
		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("aggiornaProtocollo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			int protocolloId = protocollo.getId().intValue();
			// archivio la versione precedente
			ProtocolloVO p = getProtocolloById(connection, protocolloId);
			if (p.getVersione() == protocollo.getVersione()) {

				archiviaVersione(connection, protocolloId);

				pstmt = connection.prepareStatement(UPDATE_PROTOCOLLO);

				ProtocolloDaoHelper.createStatementToUpdateProtocollo(
						protocollo, pstmt);

				int result = pstmt.executeUpdate();

				if (result == 1) {
					protocollo = getProtocolloById(connection, protocolloId);
					if (protocollo.getReturnValue() == ReturnValues.FOUND) {
						protocollo.setReturnValue(ReturnValues.SAVED);

					} else {
						logger.warn("Errore nella lettura del ProtocolloVO dopo l'aggiornamento! Id:"
								+ protocolloId);
						throw new DataException(
								"Errore nell'aggiornamento del Protocollo.");
					}
				} else {
					protocollo.setReturnValue(ReturnValues.OLD_VERSION);
				}
			} else {
				protocollo.setReturnValue(ReturnValues.OLD_VERSION);
				// throw new DataException(
				// "Errore nell'aggiornamento del Protocollo.");
			}
		} catch (SQLException e) {
			logger.error("Aggiorna Protocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return protocollo;
	}

	public int getMaxNumProtocollo(Connection connection, int anno,
			int registro, int numeroProtocollo) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("getMaxNumProtocollo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_ULTIMO_PROTOCOLLO);
			pstmt.setInt(1, registro);
			pstmt.setInt(2, anno);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt(1) == 0) {
					if (numeroProtocollo != 0)
						return numeroProtocollo;
					else
						return 1;
				} else
					return rs.getInt(1) + 1;
			} else {
				return numeroProtocollo;
			}
		} catch (Exception e) {
			logger.error("getMaxNumProtocollo", e);
			throw new DataException("Cannot load getMaxNumProtocollo");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
	}
	
	public void newProgressivoNotifica(Connection connection, int id, String progressivo,
			int anno, int aooId) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("getMaxNumProtocollo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(INSERT_PROGRESSIVO_NOTIFICA);
			pstmt.setInt(1, id);
			pstmt.setInt(2, aooId);
			pstmt.setString(3, progressivo);
			pstmt.setInt(4, anno);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("Save salvaStoricoOrganigramma", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);		}
	};

	public void updateProgressivoNotifica(Connection connection, String progressivo,
			int anno, int aooId) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("getMaxNumProtocollo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(UPDATE_PROGRESSIVO_NOTIFICA);
			pstmt.setString(1, progressivo);
			pstmt.setInt(2, aooId);
			pstmt.setInt(3, anno);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("Save salvaStoricoOrganigramma", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);		}
	};
	
	public String getProgressivoNotifica(int aooId, int anno) throws DataException {
		String progressivoNotifica = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_PROGRESSIVO_NOTIFICA);
			pstmt.setInt(1, anno);
			pstmt.setInt(2, aooId);
			rs = pstmt.executeQuery();
			if (rs.next())
				progressivoNotifica = rs.getString(1);
		} catch (Exception e) {
			logger.error("Load Progressivo Notifica", e);
			throw new DataException("Cannot load Ultimo protocollo Registro");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return progressivoNotifica;
	}

	public int getUltimoProtocollo(int anno, int registro) throws DataException {
		int ultimoProtocollo = 0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_ULTIMO_PROTOCOLLO);
			pstmt.setInt(1, registro);
			pstmt.setInt(2, anno);
			rs = pstmt.executeQuery();
			if (rs.next())
				ultimoProtocollo = rs.getInt(1);
		} catch (Exception e) {
			logger.error("Load Ultimo protocollo Registro", e);
			throw new DataException("Cannot load Ultimo protocollo Registro");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return ultimoProtocollo;
	}

	public String getTipoProtocollo(int protocolloId) throws DataException {
		String tipoProtocollo = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_TIPO_PROTOCOLLO);
			pstmt.setInt(1, protocolloId);
			rs = pstmt.executeQuery();
			if (rs.next())
				tipoProtocollo = rs.getString(1);
		} catch (Exception e) {
			logger.error("Load Tipo Protocollo Registro", e);
			throw new DataException("Cannot load Tipo Protocollo Registro");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return tipoProtocollo;
	}
	
	public RegistroVO getRegistroByProtocolloId(int protocolloId)
			throws DataException {
		Connection connection = null;
		RegistroVO reg = new RegistroVO();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection
					.prepareStatement(SELECT_REGISTRO_BY_PROTOCOLLO_ID);
			pstmt.setInt(1, protocolloId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				reg.setReturnValue(ReturnValues.UNKNOWN);
				reg.setId(rs.getString("registro_id"));
				reg.setCodRegistro(rs.getString("codi_registro"));
				reg.setDescrizioneRegistro(rs.getString("desc_registro"));
				reg.setNumUltimoProgressivo(rs
						.getInt("nume_ultimo_progressivo"));
				reg.setNumUltimoProgrInterno(rs
						.getInt("nume_ultimo_progr_interno"));
				reg.setNumUltimoFascicolo(rs.getInt("nume_ultimo_fascicolo"));
				reg.setDataAperturaRegistro(rs
						.getDate("data_apertura_registro"));
				reg.setApertoUscita(rs.getBoolean("flag_aperto_uscita"));
				reg.setApertoIngresso(rs.getBoolean("flag_aperto_ingresso"));
				reg.setUfficiale(rs.getBoolean("flag_ufficiale"));
				reg.setDataBloccata(rs.getBoolean("flag_data_bloccata"));
				reg.setAooId(rs.getInt("aoo_id"));
				reg.setRowUpdatedUser(rs.getString("row_updated_user"));
				reg.setRowUpdatedTime(rs.getDate("row_updated_time"));
				reg.setVersione(rs.getInt("versione"));
				reg.setReturnValue(ReturnValues.FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Load Ultimo protocollo Registro", e);
			throw new DataException("Cannot load Ultimo protocollo Registro");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return reg;
	}

	public ProtocolloVO getProtocolloByNumero(int anno, int registro,
			int numProtocollo) throws DataException {
		ProtocolloVO protocollo = new ProtocolloVO();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			protocollo = getProtocolloByNumero(connection, anno, registro,
					numProtocollo);

		} catch (Exception e) {
			logger.error("Load Protocollo by ID", e);
			throw new DataException("Cannot load Protocollo by ID");
		} finally {
			jdbcMan.close(connection);
		}
		return protocollo;
	}

	public int getProtocolloIdByAooNumeroAnno(int anno, int aooId,
			int numProtocollo) throws DataException {
		int id = 0;
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			id = getProtocolloByAooNumeroAnno(connection, anno, aooId,
					numProtocollo);

		} catch (Exception e) {
			logger.error("Load Protocollo by AooNumeroAnno", e);
			throw new DataException("Cannot load Protocollo by AooNumeroAnno");
		} finally {
			jdbcMan.close(connection);
		}
		return id;
	}

	public ProtocolloVO getProtocolloByNumero(Connection connection, int anno,
			int registro, int numProtocollo) throws DataException {
		ProtocolloVO protocollo = new ProtocolloVO();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("getProtocolloByNumero (Connection connection, int anno, int registro,"
						+ "int numProtocollo) - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_PROTOCOLLO_BY_NUMERO);
			pstmt.setInt(1, anno);
			pstmt.setInt(2, registro);
			pstmt.setInt(3, numProtocollo);
			rs = pstmt.executeQuery();
			int id;
			if (rs.next()) {
				id = rs.getInt(1);
				protocollo = getProtocolloById(connection, id);
			}

		} catch (Exception e) {
			logger.error("Load Protocollo by ID", e);
			throw new DataException("Cannot load Protocollo by ID");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return protocollo;
	}

	public int getProtocolloByAooNumeroAnno(Connection connection, int anno,
			int aooId, int numProtocollo) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int id = 0;
		try {
			if (connection == null) {
				logger.warn("getProtocolloByAooNumeroAnno (Connection connection, int anno, int registro,"
						+ "int numProtocollo) - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection
					.prepareStatement(SELECT_PROTOCOLLO_BY_AOO_ANNO_NUMERO);
			pstmt.setInt(1, anno);
			pstmt.setInt(2, aooId);
			pstmt.setInt(3, numProtocollo);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				id = rs.getInt(1);
			}

		} catch (Exception e) {
			logger.error("Load Protocollo by ID", e);
			throw new DataException("Cannot load Protocollo by AooNumeroAnno");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return id;
	}

	public void aggiornaDocumentoPrincipaleId(Connection connection,
			int protocolloId, int documentoId) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("aggiornaDocumentoPrincipaleId() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(UPDATE_DOCUMENTO_ID);
			pstmt.setInt(1, documentoId);
			pstmt.setInt(2, protocolloId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("Aggiornamento documento_id", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void salvaAllegato(Connection connection,
			int protocollo_allegati_id, int protocolloId, int documentoId,
			int versione) throws DataException {
		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("salvaAllegato() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(INSERT_PROTOCOLLO_ALLEGATI);
			pstmt.setInt(1, protocollo_allegati_id);
			pstmt.setInt(2, protocolloId);
			pstmt.setInt(3, documentoId);
			pstmt.setInt(4, versione);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("Save Allegato-Protocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void salvaAllaccio(Connection connection, AllaccioVO allaccio)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("salvaAllaccio() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			if (allaccio != null) {
				pstmt = connection.prepareStatement(INSERT_PROTOCOLLO_ALLACCI);
				pstmt.setInt(1, allaccio.getId().intValue());
				pstmt.setInt(2, allaccio.getProtocolloId());
				pstmt.setInt(3, allaccio.isPrincipale() ? 1 : 0);
				pstmt.setInt(4, allaccio.getProtocolloAllacciatoId());
				pstmt.setInt(5, allaccio.getProtocolloId());
				pstmt.executeUpdate();
			}

			// logger.debug("Inserito Allaccio:" + allaccio);

		} catch (Exception e) {
			logger.error("Save Allaccio-Protocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public Collection<AllaccioView> getProtocolliAllacciabili(Utente utente,
			int numeroProtocolloDa, int numeroProtocolloA, int annoProtocollo,
			int protocolloId) throws DataException {
		ArrayList<AllaccioView> allacci = new ArrayList<AllaccioView>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer strQuery = new StringBuffer(SELECT_ALLACCI);
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
			strQuery.append(" ORDER BY p.ANNO_REGISTRAZIONE DESC, p.NUME_PROTOCOLLO DESC");
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery.toString());

			pstmt.setInt(1, utente.getRegistroInUso());
			pstmt.setInt(2, utente.getRegistroPostaInterna());
			pstmt.setInt(3, protocolloId);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				AllaccioView allaccio = new AllaccioView();
				allaccio.setProtAllacciatoId(rs.getInt("protocollo_id"));
				allaccio.setAnnoProtAllacciato(rs.getInt("ANNO_REGISTRAZIONE"));
				allaccio.setNumProtAllacciato(rs.getInt("NUME_PROTOCOLLO"));
				allaccio.setDataProtocollo(DateUtil.formattaData(rs.getDate(
						"data_registrazione").getTime()));
				allaccio.setTipoProtocollo(rs.getString("FLAG_TIPO"));
				allaccio.setOggetto(rs.getString("TEXT_OGGETTO"));
				if (rs.getString("FLAG_TIPO_MITTENTE").equals("F")) {
					allaccio.setMittente(rs.getString("DESC_COGNOME_MITTENTE")
							+ " "
							+ StringUtil.getStringa(rs
									.getString("DESC_NOME_MITTENTE")));
				} else {
					allaccio.setMittente(rs
							.getString("DESC_DENOMINAZIONE_MITTENTE"));
				}
				allacci.add(allaccio);
			}
		} catch (Exception e) {
			logger.error("getProtocolliAllacciabili", e);
			throw new DataException("Cannot load getProtocolliAllacciabili");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return allacci;

	}

	public AllaccioVO getProtocolloAllacciabile(Utente utente,
			int numeroProtocolloDa, int numeroProtocolloA, int annoProtocollo)
			throws DataException {
		AllaccioVO allaccio = new AllaccioVO();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer strQuery = new StringBuffer(SELECT_ALLACCI_EDITOR);
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
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery.toString());

			pstmt.setInt(1, utente.getRegistroInUso());
			// pstmt.setInt(2, utente.getRegistroPostaInterna());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				allaccio = new AllaccioVO();
				allaccio.setProtocolloAllacciatoId(rs.getInt("protocollo_id"));
				allaccio.setDataRegistrazione(rs.getDate("DATA_REGISTRAZIONE"));
				allaccio.setNumeroProtocollo(rs.getInt("NUME_PROTOCOLLO"));
				allaccio.setTipo(rs.getString("FLAG_TIPO"));
				allaccio.addDescrizioneTemplate();
			}
		} catch (Exception e) {
			logger.error("getProtocolliAllacciabili", e);
			throw new DataException("Cannot load getProtocolliAllacciabili");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return allaccio;

	}

	public Map<String,DocumentoVO> getAllegatiProtocollo(int protocolloId) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String,DocumentoVO> docs = new HashMap<String,DocumentoVO>(2);
		Connection connection = null;
		DocumentoDelegate documentoDelegate = DocumentoDelegate.getInstance();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(PROTOCOLLO_ALLEGATI);
			pstmt.setInt(1, protocolloId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("documento_id");
				DocumentoVO doc = documentoDelegate
						.getDocumento(connection, id);
				ProtocolloBO.putAllegato(doc, docs);
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new DataException("Errore nella lettura dei documenti.");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return docs;
	}

	public Collection<AllaccioVO> getAllacciProtocollo(int protocolloId)
			throws DataException {
		Collection<AllaccioVO> allacci = new ArrayList<AllaccioVO>();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			allacci = getAllacciProtocollo(connection, protocolloId);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " ProtocolloId :"
					+ protocolloId);
		} finally {
			jdbcMan.close(connection);
		}
		return allacci;
	}

	public Collection<AllaccioVO> getAllacciProtocollo(Connection connection,
			int protocolloId) throws DataException {
		ArrayList<AllaccioVO> allacci = new ArrayList<AllaccioVO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(PROTOCOLLO_ALLACCI);
			pstmt.setInt(1, protocolloId);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				AllaccioVO allaccio = new AllaccioVO();
				allaccio.setId(rs.getInt("allaccio_id"));
				allaccio.setProtocolloId(rs.getInt("protocollo_id"));
				allaccio.setProtocolloAllacciatoId(rs
						.getInt("protocollo_allacciato_id"));
				allaccio.setPrincipale(rs.getBoolean("flag_principale"));
				allaccio.setAllaccioDescrizione(rs.getInt("nume_protocollo")
						+ "/" + rs.getInt("anno_registrazione") + " ("
						+ rs.getString("flag_tipo") + ")");
				allacci.add(allaccio);
			}
		} catch (Exception e) {
			logger.error("getAllacci", e);
			throw new DataException("Cannot load getAllacci");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return allacci;
	}

	public AssegnatarioVO getAssegnatarioPerCompetenza(int protocolloId)
			throws DataException {
		AssegnatarioVO assegnatario = new AssegnatarioVO();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			assegnatario = getAssegnatarioPerCompetenza(connection,
					protocolloId);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " ProtocolloId :"
					+ protocolloId);
		} finally {
			jdbcMan.close(connection);
		}
		return assegnatario;
	}

	public AssegnatarioVO getAssegnatarioPerCompetenza(Connection connection,
			int protocolloId) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		AssegnatarioVO assegnatario = new AssegnatarioVO();
		try {
			pstmt = connection.prepareStatement(ASSEGNATARIO_PER_COMPETENZA);
			pstmt.setInt(1, protocolloId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				assegnatario.setId(rs.getInt("assegnatario_id"));
				assegnatario.setProtocolloId(rs.getInt("protocollo_id"));
				assegnatario.setDataAssegnazione(rs
						.getDate("data_assegnazione"));
				assegnatario.setDataOperazione(rs.getDate("data_operazione"));
				assegnatario.setUfficioAssegnanteId(rs
						.getInt("ufficio_assegnante_id"));
				assegnatario.setUfficioAssegnatarioId(rs
						.getInt("ufficio_assegnatario_id"));
				// assegnatario.setUtenteAssegnatarioId(rs.getInt("utente_assegnatario_id"));
				assegnatario.setCompetente(rs.getBoolean("flag_competente"));
				assegnatario.setUtenteAssegnanteId(rs
						.getInt("utente_assegnante_id"));
			}
		} catch (Exception e) {
			logger.error("getAssegnatarioPerCompetenzaProtocollo", e);
			throw new DataException(
					"Cannot load getAssegnatarioPerCompetenzaProtocollo");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return assegnatario;
	}

	public Collection<AssegnatarioVO> getAssegnatariProtocollo(int protocolloId)
			throws DataException {
		ArrayList<AssegnatarioVO> assegnatari = new ArrayList<AssegnatarioVO>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(PROTOCOLLO_ASSEGNATARI);
			pstmt.setInt(1, protocolloId);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				AssegnatarioVO assegnatario = new AssegnatarioVO();
				assegnatario.setId(rs.getInt("assegnatario_id"));
				assegnatario.setProtocolloId(rs.getInt("protocollo_id"));
				assegnatario.setDataAssegnazione(rs
						.getDate("data_assegnazione"));
				assegnatario.setDataOperazione(rs.getDate("data_operazione"));
				assegnatario.setUfficioAssegnanteId(rs
						.getInt("ufficio_assegnante_id"));
				assegnatario.setUfficioAssegnatarioId(rs
						.getInt("ufficio_assegnatario_id"));

				assegnatario.setCaricaAssegnatarioId(rs
						.getInt("carica_assegnatario_id"));
				assegnatario.setCompetente(rs.getBoolean("flag_competente"));
				assegnatario.setCaricaAssegnanteId(rs
						.getInt("carica_assegnante_id"));

				assegnatario.setStatoAssegnazione(rs.getString(
						"stat_assegnazione").charAt(0));
				assegnatario.setMsgAssegnatarioCompetente(rs
						.getString("messaggio"));
				assegnatario.setPresaVisione(rs
						.getBoolean("check_presa_visione"));
				assegnatario.setLavorato(rs.getBoolean("check_lavorato"));
				assegnatario.setTitolareProcedimento(rs
						.getBoolean("flag_titolare_procedimento"));
				assegnatari.add(assegnatario);
			}
		} catch (Exception e) {
			logger.error("getAssegnatariProtocollo", e);
			throw new DataException("Cannot load getAssegnatari");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return assegnatari;
	}

	public AssegnatarioVO getAssegnatarioProtocollo(int protocolloId,
			int ufficioId, int caricaId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		AssegnatarioVO assegnatario = null;
		try {
			connection = jdbcMan.getConnection();
			StringBuffer query = new StringBuffer(
					"SELECT * FROM protocollo_assegnatari WHERE protocollo_id =? ");
			if (caricaId != 0)
				query.append("AND carica_assegnatario_id=?");
			else
				query.append("AND ufficio_assegnatario_id=?");
			pstmt = connection.prepareStatement(query.toString());
			pstmt.setInt(1, protocolloId);
			if (caricaId != 0)
				pstmt.setInt(2, caricaId);
			else
				pstmt.setInt(2, ufficioId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				assegnatario = new AssegnatarioVO();
				assegnatario.setId(rs.getInt("assegnatario_id"));
				assegnatario.setProtocolloId(rs.getInt("protocollo_id"));
				assegnatario.setDataAssegnazione(rs
						.getDate("data_assegnazione"));
				assegnatario.setDataOperazione(rs.getDate("data_operazione"));
				assegnatario.setUfficioAssegnanteId(rs
						.getInt("ufficio_assegnante_id"));
				assegnatario.setUfficioAssegnatarioId(rs
						.getInt("ufficio_assegnatario_id"));

				assegnatario.setCaricaAssegnatarioId(rs
						.getInt("carica_assegnatario_id"));
				assegnatario.setCompetente(rs.getBoolean("flag_competente"));
				assegnatario.setCaricaAssegnanteId(rs
						.getInt("carica_assegnante_id"));

				assegnatario.setStatoAssegnazione(rs.getString(
						"stat_assegnazione").charAt(0));
				assegnatario.setMsgAssegnatarioCompetente(rs
						.getString("messaggio"));
				assegnatario.setPresaVisione(rs
						.getBoolean("check_presa_visione"));
				assegnatario.setLavorato(rs.getBoolean("check_lavorato"));
				assegnatario.setTitolareProcedimento(rs
						.getBoolean("flag_titolare_procedimento"));
			}
		} catch (Exception e) {
			logger.error("getAssegnatarioProtocollo", e);
			throw new DataException("Cannot load getAssegnatarioProtocollo");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return assegnatario;
	}

	public boolean isUfficioAssegnatario(int protocolloId,
			int ufficioId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean isAssegnatario = false;
		try {
			connection = jdbcMan.getConnection();
			String query = "SELECT assegnatario_id FROM protocollo_assegnatari WHERE protocollo_id =? AND ufficio_assegnatario_id=?";
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, protocolloId);
			pstmt.setInt(2, ufficioId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				isAssegnatario=true;
			}
		} catch (Exception e) {
			logger.error("getAssegnatarioProtocollo", e);
			throw new DataException("Cannot load getAssegnatarioProtocollo");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return isAssegnatario;
	}
	
	public void registraLavoratoProtocolloIngresso(AssegnatarioVO assegnatario,
			String username) throws DataException {
		PreparedStatement pstmt = null;
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			// int recIns = 0;
			archiviaVersione(connection, assegnatario.getProtocolloId());
			pstmt = connection.prepareStatement(SET_PROCEDIMENTO_LAVORATO);
			pstmt.setInt(1, assegnatario.getId().intValue());
			pstmt.executeUpdate();
			jdbcMan.close(pstmt);
			pstmt = connection.prepareStatement(UPDATE_STATO_PROTOCOLLO);
			pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			pstmt.setString(2, username);
			pstmt.setInt(3, assegnatario.getProtocolloId());
			// recIns = pstmt.executeUpdate();
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("registraLavoratoProtocolloIngresso", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}

	}

	public void salvaAssegnatario(Connection connection,
			AssegnatarioVO assegnatario, int versione) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("salvaAssegnatario() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			if (assegnatario != null) {
				pstmt = connection
						.prepareStatement(INSERT_PROTOCOLLO_ASSEGNATARI);

				java.sql.Date now = new java.sql.Date(
						(new java.util.Date()).getTime());
				pstmt.setInt(1, assegnatario.getId().intValue());
				pstmt.setInt(2, assegnatario.getProtocolloId());
				pstmt.setDate(3, now);
				pstmt.setDate(4, now);
				pstmt.setInt(5, assegnatario.isCompetente() ? 1 : 0);
				pstmt.setInt(6, assegnatario.getUfficioAssegnatarioId());
				if (assegnatario.getCaricaAssegnatarioId() == 0)
					pstmt.setNull(7, Types.INTEGER);
				else
					pstmt.setInt(7, assegnatario.getCaricaAssegnatarioId());
				if (assegnatario.getUtenteAssegnatarioId() == 0)
					pstmt.setNull(8, Types.INTEGER);
				else
					pstmt.setInt(8, assegnatario.getUtenteAssegnatarioId());
				pstmt.setInt(9, assegnatario.getUfficioAssegnanteId());
				if (assegnatario.getCaricaAssegnanteId() == 0)
					pstmt.setNull(10, Types.INTEGER);
				else
					pstmt.setInt(10, assegnatario.getCaricaAssegnanteId());
				pstmt.setString(11, assegnatario.getStatoAssegnazione() + "");
				pstmt.setString(12, assegnatario.getMsgAssegnatarioCompetente());
				if (assegnatario.isCompetente())
					pstmt.setNull(13, Types.INTEGER);
				else
					pstmt.setInt(13, assegnatario.isPresaVisione() ? 1 : 0);
				pstmt.setInt(14, versione);
				pstmt.setInt(15, assegnatario.isLavorato() ? 1 : 0);
				pstmt.setInt(16, assegnatario.isTitolareProcedimento() ? 1 : 0);
				pstmt.executeUpdate();
			}
		} catch (Exception e) {
			logger.error("Save Assegnatari-Protocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void salvaCheckPresaVisione(Connection connection,
			AssegnatarioVO assegnatario) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("salvaAssegnatario() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			if (assegnatario != null) {
				pstmt = connection.prepareStatement(INSERT_CHECK_PRESA_VISIONE);
				Timestamp now = new Timestamp(System.currentTimeMillis());
				pstmt.setInt(1, assegnatario.getId().intValue());
				pstmt.setInt(2, assegnatario.getProtocolloId());
				pstmt.setTimestamp(3, now);
				pstmt.setInt(4, assegnatario.getUfficioAssegnanteId());
				pstmt.setInt(5, assegnatario.getUfficioAssegnatarioId());
				pstmt.setInt(6, assegnatario.getCaricaAssegnatarioId());
				pstmt.setInt(7, assegnatario.getCaricaAssegnanteId());
				pstmt.setInt(8, assegnatario.isCompetente() ? 1 : 0);
				pstmt.executeUpdate();
			}
		} catch (Exception e) {
			logger.error("Save checkPresaVisione", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public int presaIncarico(Connection connection, ProtocolloVO protocolloVO,
			String tipoAzione, Utente utente) throws DataException {
		PreparedStatement pstmt = null;
		int fglagStatus = ReturnValues.SAVED;
		try {
			if (connection == null) {
				logger.warn("presaIncarico() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			StringBuffer sqlUpdateAssegnatarioCompetenza = new StringBuffer(
					"UPDATE PROTOCOLLO_ASSEGNATARI SET STAT_ASSEGNAZIONE=?");
			if ("R".equals(tipoAzione)) {
				sqlUpdateAssegnatarioCompetenza
						.append(", utente_assegnatario_id=utente_assegnante_id, "
								+ "ufficio_assegnatario_id=ufficio_assegnante_id,"
								+ "ufficio_assegnante_id="
								+ utente.getUfficioInUso()
								+ ","
								+ "utente_assegnante_id="
								+ utente.getValueObject().getId().intValue());
			} else {
				sqlUpdateAssegnatarioCompetenza
						.append(", utente_assegnatario_id=?");
			}
			sqlUpdateAssegnatarioCompetenza
					.append(" WHERE protocollo_id=? AND flag_competente=1 AND versione=?");

			pstmt = connection.prepareStatement(sqlUpdateAssegnatarioCompetenza
					.toString());
			// logger.debug(" presaIncarico: " + protocolloVO.getId());
			int indice = 1;
			pstmt.setString(indice++, tipoAzione);
			if (!"R".equals(tipoAzione)) {
				pstmt.setInt(indice++, utente.getValueObject().getId()
						.intValue());
			}
			pstmt.setInt(indice++, protocolloVO.getId().intValue());
			pstmt.setInt(indice++, protocolloVO.getVersione() + 1);
			int n = pstmt.executeUpdate();
			if (n == 0) {
				fglagStatus = ReturnValues.OLD_VERSION;
				throw new DataException("Versione incongruente.");
			}
		} catch (Exception e) {
			logger.error("presaIncarico", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return fglagStatus;
	}


	public Map<Long,ReportProtocolloView> getProtocolliAssegnati(Utente utente, int annoProtocolloDa,
			int annoProtocolloA, int numeroProtocollo, String tipoUtenteUfficio)
			throws DataException {

		SortedMap<Long,ReportProtocolloView>  protocolli = new TreeMap<Long,ReportProtocolloView> (new Comparator<Long>() {
			public int compare(Long i1, Long i2) {
				return i2.compareTo(i1);
				//return (i1 > i2) ? -1 : (i1 == i2 ? 0 : 1);
			}
		});

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String strQuery = "SELECT p.*, a.ufficio_assegnante_id, a.ufficio_assegnatario_id, "
				+ " a.flag_competente, a.carica_assegnante_id, a.carica_assegnatario_id, a.messaggio,a.flag_titolare_procedimento, ass_proc.assegnatario_id AS flag_procedimento"
				+ " FROM PROTOCOLLI p, PROTOCOLLO_ASSEGNATARI a "
				+ " LEFT JOIN protocollo_assegnatari ass_proc ON (ass_proc.protocollo_id=a.protocollo_id AND ass_proc.flag_titolare_procedimento=1)"
				+ " WHERE registro_id=? AND NOT flag_tipo ='P'"
				+ " AND p.protocollo_id =a.protocollo_id " + " AND ";

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
					+ " NOT stato_protocollo IN ('C','P') AND a.carica_assegnatario_id=?" 
					+ " AND NOT EXISTS (select assegnatario_id from protocollo_assegnatari WHERE protocollo_id = p.protocollo_id AND stat_assegnazione='R' AND carica_assegnatario_id="
					+ utente.getCaricaInUso() + ") AND a.check_lavorato=0 AND a.flag_competente=1";
		} else if ("U".equals(tipoUtenteUfficio)) {
			strQuery = strQuery
					+ " NOT stato_protocollo IN('C','P') AND a.ufficio_assegnatario_id = "
					+ utente.getUfficioInUso()
					+ " AND a.carica_assegnatario_id IS NULL AND a.check_lavorato=0 AND a.flag_competente=1"; //AND a.flag_competente=1 AND a.check_lavorato=0";
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
			while (rs.next()) {
				ReportProtocolloView protocollo = new ReportProtocolloView();
				protocollo.setProtocolloId(rs.getInt("protocollo_id"));
				protocollo.setNumeroProtocollo(rs.getInt("NUME_PROTOCOLLO"));
				protocollo.setRegistroAnnoNumero(rs
						.getLong("registro_anno_numero"));
				protocollo.setAnnoProtocollo(rs.getInt("ANNO_REGISTRAZIONE"));
				if ("M".equals(rs.getString("flag_tipo_mittente"))) {
					protocollo.setTipoMittente("M");
					protocollo.setMittente(Parametri.MULTI_MITTENTE);
				}
				protocollo.setDataProtocollo(DateUtil.formattaData(rs.getDate(
						"DATA_REGISTRAZIONE").getTime()));

				if (rs.getInt("FLAG_RISERVATO") == 1) {
					protocollo.setOggetto(Parametri.PROTOCOLLO_RISERVATO);
					protocollo.setMittente(Parametri.PROTOCOLLO_RISERVATO);

				} else {
					protocollo.setOggetto(rs.getString("TEXT_OGGETTO"));
					protocollo.setMittente(StringUtil.getStringa(rs
							.getString("DESC_COGNOME_MITTENTE"))
							+ " "
							+ StringUtil.getStringa(rs
									.getString("DESC_NOME_MITTENTE"))
							+ StringUtil.getStringa(rs
									.getString("DESC_DENOMINAZIONE_MITTENTE")));
				}
				if (rs.getString("ufficio_assegnante_id") != null)
					protocollo
							.setUfficio(rs.getString("ufficio_assegnante_id"));
				if (rs.getString("carica_assegnante_id") != null)
					protocollo.setUtenteAssegnante(rs
							.getString("carica_assegnante_id"));

				protocollo.setUfficioAssegnatario(rs
						.getString("ufficio_assegnatario_id"));
				protocollo.setUtenteAssegnatario(rs
						.getString("carica_assegnatario_id"));
				protocollo.setPdf(rs.getInt("DOCUMENTO_ID") > 0 ? true : false);
				protocollo.setDocumentoId(rs.getInt("DOCUMENTO_ID"));
				protocollo.setMessaggio(StringUtil.getStringa(rs
						.getString("messaggio")));
				protocollo.setTitolareProcedimento(rs.getBoolean("flag_titolare_procedimento"));
				protocollo.setCompetente(rs.getBoolean("flag_competente"));
				protocollo
						.setInProcedimento(rs.getInt("flag_procedimento") > 0);
				if (rs.getDate("data_scarico") != null)
					protocollo.setDataScarico(DateUtil.formattaData(rs.getDate(
							"data_scarico").getTime()));
				if (protocollo.isInProcedimento() && protocollo.getMessaggio().equals("")&& !protocollo.isTitolareProcedimento())

					protocollo
							.setMessaggio("Fascicolazione riservata al titolare del procedimento");
				protocolli.put(protocollo.getAnnoNumero(), protocollo);

			}
		} catch (Exception e) {
			logger.error("getProtocolliAssegnati", e);
			throw new DataException("Cannot load getProtocolliAssegnati");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return protocolli;

	}

	public Map<Long,ReportProtocolloView> getPostaInternaAssegnata(Utente utente, String tipoUtenteUfficio)
			throws DataException {
		SortedMap<Long,ReportProtocolloView>  protocolli = new TreeMap<Long,ReportProtocolloView> (new Comparator<Long>() {
			public int compare(Long i1, Long i2) {
				return i2.compareTo(i1);
			}
		});
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String strQuery = "SELECT p.*, a.ufficio_assegnante_id, a.ufficio_assegnatario_id, "
				+ " a.carica_assegnante_id, a.carica_assegnatario_id, a.messaggio,a.flag_competente,"
				+ " proc.procedimento_id  FROM PROTOCOLLI p"
				+ " left join PROTOCOLLO_ASSEGNATARI a ON (p.protocollo_id = a.protocollo_id)"
				+ " left join PROTOCOLLO_PROCEDIMENTI proc ON (p.protocollo_id = proc.protocollo_id) "
				+ " WHERE flag_tipo='P' AND NOT stato_protocollo ='C' AND flag_repertorio=0  AND ";
		if ("T".equals(tipoUtenteUfficio)) {
			strQuery = strQuery
					+ " carica_assegnatario_id="+utente.getCaricaInUso()+" AND check_lavorato=0";
		} else if (("U".equals(tipoUtenteUfficio))) {
			strQuery = strQuery + " ufficio_assegnatario_id = "
					+ utente.getUfficioInUso() + " ";
			strQuery = strQuery
					+ " AND carica_assegnatario_id IS NULL AND (check_presa_visione IS NULL OR check_presa_visione=0) AND check_lavorato=0";
		}
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ReportProtocolloView protocollo = new ReportProtocolloView();

				protocollo.setProtocolloId(rs.getInt("protocollo_id"));
				protocollo.setNumeroProtocollo(rs.getInt("NUME_PROTOCOLLO"));
				protocollo.setAnnoProtocollo(rs.getInt("ANNO_REGISTRAZIONE"));
				if ("M".equals(rs.getString("flag_tipo_mittente"))) {
					protocollo.setTipoMittente("M");
				}
				protocollo.setDataProtocollo(DateUtil.formattaData(rs.getDate(
						"DATA_REGISTRAZIONE").getTime()));

				if (rs.getInt("FLAG_RISERVATO") == 1) {
					protocollo.setOggetto(Parametri.PROTOCOLLO_RISERVATO);
					protocollo.setMittente(Parametri.PROTOCOLLO_RISERVATO);

				} else {
					protocollo.setOggetto(rs.getString("TEXT_OGGETTO"));
					protocollo.setMittente(StringUtil.getStringa(rs
							.getString("DESC_COGNOME_MITTENTE"))
							+ " "
							+ StringUtil.getStringa(rs
									.getString("DESC_NOME_MITTENTE"))
							+ StringUtil.getStringa(rs
									.getString("DESC_DENOMINAZIONE_MITTENTE")));
				}
				protocollo.setUfficio(rs.getString("ufficio_assegnante_id"));
				protocollo.setUtenteAssegnante(rs
						.getString("carica_assegnante_id"));
				protocollo.setUfficioAssegnatario(rs
						.getString("ufficio_assegnatario_id"));
				protocollo.setUtenteAssegnatario(rs
						.getString("carica_assegnatario_id"));
				protocollo.setPdf(rs.getInt("DOCUMENTO_ID") > 0 ? true : false);
				protocollo.setDocumentoId(rs.getInt("DOCUMENTO_ID"));
				protocollo.setMessaggio(StringUtil.getStringa(rs
						.getString("messaggio")));
				if (rs.getDate("data_scarico") != null)
					protocollo.setDataScarico(DateUtil.formattaData(rs.getDate(
							"data_scarico").getTime()));
				protocollo.setCompetente(rs.getBoolean("flag_competente"));
				protocollo.setTipoProtocollo(rs.getString("flag_tipo"));
				protocollo.setVersione(rs.getInt("versione"));
				protocollo.setRegistroAnnoNumero(rs.getLong("registro_anno_numero"));
				protocollo.setInProcedimento(rs.getInt("procedimento_id") > 0 ? true : false);
				protocolli.put(protocollo.getAnnoNumero(), protocollo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getPostaInternaAssegnata", e);
			throw new DataException("Cannot load getPostaInternaAssegnata");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return protocolli;
	}
	
	public Map<Long,ReportProtocolloView> getPostaInternaRepertorio(Utente utente)
			throws DataException {
		
		SortedMap<Long,ReportProtocolloView>  protocolli = new TreeMap<Long,ReportProtocolloView> (new Comparator<Long>() {
			public int compare(Long i1, Long i2) {
				return i2.compareTo(i1);
			}
		});
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String strQuery = "SELECT p.*, a.ufficio_assegnante_id, a.ufficio_assegnatario_id, "
				+ " a.carica_assegnante_id, a.carica_assegnatario_id, a.messaggio,a.flag_competente,"
				+ " proc.procedimento_id  FROM PROTOCOLLI p"
				+ " left join PROTOCOLLO_ASSEGNATARI a ON (p.protocollo_id = a.protocollo_id)"
				+ " left join PROTOCOLLO_PROCEDIMENTI proc ON (p.protocollo_id = proc.protocollo_id) "
				+ " WHERE flag_tipo='P' AND NOT stato_protocollo ='C' AND flag_repertorio=1 "
				//+ "AND carica_assegnatario_id="+utente.getCaricaInUso()+" AND check_lavorato=0";
				+ " AND ufficio_assegnatario_id = " + utente.getUfficioInUso()
				+ " AND carica_assegnatario_id IS NULL AND check_lavorato=0";
				
		
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ReportProtocolloView protocollo = new ReportProtocolloView();

				protocollo.setProtocolloId(rs.getInt("protocollo_id"));
				protocollo.setNumeroProtocollo(rs.getInt("NUME_PROTOCOLLO"));
				protocollo.setAnnoProtocollo(rs.getInt("ANNO_REGISTRAZIONE"));
				if ("M".equals(rs.getString("flag_tipo_mittente"))) {
					protocollo.setTipoMittente("M");
				}
				protocollo.setDataProtocollo(DateUtil.formattaData(rs.getDate(
						"DATA_REGISTRAZIONE").getTime()));

				if (rs.getInt("FLAG_RISERVATO") == 1) {
					protocollo.setOggetto(Parametri.PROTOCOLLO_RISERVATO);
					protocollo.setMittente(Parametri.PROTOCOLLO_RISERVATO);

				} else {
					protocollo.setOggetto(rs.getString("TEXT_OGGETTO"));
					protocollo.setMittente(StringUtil.getStringa(rs
							.getString("DESC_COGNOME_MITTENTE"))
							+ " "
							+ StringUtil.getStringa(rs
									.getString("DESC_NOME_MITTENTE"))
							+ StringUtil.getStringa(rs
									.getString("DESC_DENOMINAZIONE_MITTENTE")));
				}
				protocollo.setUfficio(rs.getString("ufficio_assegnante_id"));
				protocollo.setUtenteAssegnante(rs
						.getString("carica_assegnante_id"));
				protocollo.setUfficioAssegnatario(rs
						.getString("ufficio_assegnatario_id"));
				protocollo.setUtenteAssegnatario(rs
						.getString("carica_assegnatario_id"));
				protocollo.setPdf(rs.getInt("DOCUMENTO_ID") > 0 ? true : false);
				protocollo.setDocumentoId(rs.getInt("DOCUMENTO_ID"));
				protocollo.setMessaggio(StringUtil.getStringa(rs
						.getString("messaggio")));
				if (rs.getDate("data_scarico") != null)
					protocollo.setDataScarico(DateUtil.formattaData(rs.getDate(
							"data_scarico").getTime()));
				protocollo.setCompetente(rs.getBoolean("flag_competente"));
				protocollo.setTipoProtocollo(rs.getString("flag_tipo"));
				protocollo.setVersione(rs.getInt("versione"));
				protocollo.setRegistroAnnoNumero(rs.getLong("registro_anno_numero"));
				protocollo.setInProcedimento(rs.getInt("procedimento_id") > 0 ? true : false);
				protocolli.put(protocollo.getAnnoNumero(), protocollo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getPostaInternaAssegnata", e);
			throw new DataException("Cannot load getPostaInternaAssegnata");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return protocolli;
	}

	public Map<Integer,ReportCheckPostaInternaView> getCheckPostaInternaView(int caricaId, int flagNotifica)
			throws DataException {
		SortedMap<Integer,ReportCheckPostaInternaView>  checkMap = new TreeMap<Integer,ReportCheckPostaInternaView> (new Comparator<Integer>() {
			public int compare(Integer i1, Integer i2) {
				return i2.compareTo(i1);
			}
		});
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String strQuery = "SELECT c.check_id,c.data_operazione,c.protocollo_id,c.flag_competente,uf.descrizione AS descrizione_uff,ut.cognome||' '||ut.nome AS assegnatario,p.nume_protocollo,p.anno_registrazione "
				+ " FROM check_posta_interna c"
				+ " LEFT JOIN uffici uf ON(c.ufficio_assegnatario_id=uf.ufficio_id)"
				+ " LEFT JOIN cariche car ON(c.carica_assegnatario_id=car.carica_id)"
				+ " LEFT JOIN utenti ut ON(car.utente_id=ut.utente_id)"
				+ " LEFT JOIN protocolli p ON(c.protocollo_id=p.protocollo_id)"
				+ " WHERE c.carica_assegnante_id=? AND c.check_presa_visione=?";
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, caricaId);
			pstmt.setInt(2, flagNotifica);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ReportCheckPostaInternaView checkPI = new ReportCheckPostaInternaView();
				checkPI.setCheckId(rs.getInt("check_id"));
				checkPI.setProtocolloId(rs.getInt("protocollo_id"));
				checkPI.setProtocolloDescrizione(rs.getInt("nume_protocollo")
						+ "/" + rs.getInt("anno_registrazione"));
				checkPI.setDataOperazione(DateUtil.formattaDataOra(rs
						.getTimestamp("data_operazione").getTime()));
				checkPI.setAssegnatario(rs.getString("descrizione_uff") + "/"
						+ rs.getString("assegnatario"));
				checkPI.setCompetente(rs.getBoolean("flag_competente"));
				checkMap.put(checkPI.getCheckId(), checkPI);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getCheckPostaInternaView", e);
			throw new DataException("Cannot load getCheckPostaInternaView");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return checkMap;
	}

	public Map<Long,ReportProtocolloView> getFatture(Utente utente, int registro, String tipoUtenteUfficio)
			throws DataException {
		SortedMap<Long,ReportProtocolloView> protocolli = new TreeMap<Long,ReportProtocolloView>(new Comparator<Long>() {
			public int compare(Long i1, Long i2) {
				return i2.compareTo(i1);
			}
		});

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String strQuery = "SELECT p.*, a.ufficio_assegnante_id, a.ufficio_assegnatario_id, "
				+ " a.carica_assegnante_id, a.carica_assegnatario_id, a.messaggio,a.flag_competente"
				+ " FROM PROTOCOLLI p, PROTOCOLLO_ASSEGNATARI a"
				+ " WHERE aoo_id=? AND registro_id=?"
				+ " AND p.protocollo_id = a.protocollo_id AND ";
		if ("T".equals(tipoUtenteUfficio)) {
			strQuery = strQuery
					+ " NOT stat_assegnazione='R' AND carica_assegnatario_id="
					+ utente.getCaricaInUso() + " AND check_lavorato=0;";
		} else if (("U".equals(tipoUtenteUfficio))) {
			strQuery = strQuery + " ufficio_assegnatario_id = "
					+ utente.getUfficioInUso() + " ";
			strQuery = strQuery
					+ " AND carica_assegnatario_id IS NULL AND (check_presa_visione IS NULL OR check_presa_visione=0) AND check_lavorato=0";

		} else if ("R".equals(tipoUtenteUfficio)) {
			strQuery = strQuery + " flag_competente=1"
					+ " AND stat_assegnazione='R' "
					+ " AND carica_assegnatario_id =" + utente.getCaricaInUso();
		}
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);

			int currPstmt = 1;
			pstmt.setInt(currPstmt++, utente.getAreaOrganizzativa().getId());
			pstmt.setInt(currPstmt++, registro);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ReportProtocolloView protocollo = new ReportProtocolloView();

				protocollo.setProtocolloId(rs.getInt("protocollo_id"));
				protocollo.setNumeroProtocollo(rs.getInt("NUME_PROTOCOLLO"));
				protocollo.setAnnoProtocollo(rs.getInt("ANNO_REGISTRAZIONE"));
				if ("M".equals(rs.getString("flag_tipo_mittente"))) {
					protocollo.setTipoMittente("M");
				}
				protocollo.setDataProtocollo(DateUtil.formattaData(rs.getDate(
						"DATA_REGISTRAZIONE").getTime()));

				if (rs.getInt("FLAG_RISERVATO") == 1) {
					protocollo.setOggetto(Parametri.PROTOCOLLO_RISERVATO);
					protocollo.setMittente(Parametri.PROTOCOLLO_RISERVATO);

				} else {
					protocollo.setOggetto(rs.getString("TEXT_OGGETTO"));
					protocollo.setMittente(StringUtil.getStringa(rs
							.getString("DESC_COGNOME_MITTENTE"))
							+ " "
							+ StringUtil.getStringa(rs
									.getString("DESC_NOME_MITTENTE"))
							+ StringUtil.getStringa(rs
									.getString("DESC_DENOMINAZIONE_MITTENTE")));
				}
				protocollo.setUfficio(rs.getString("ufficio_assegnante_id"));
				protocollo.setUtenteAssegnante(rs
						.getString("carica_assegnante_id"));
				protocollo.setUfficioAssegnatario(rs
						.getString("ufficio_assegnatario_id"));
				protocollo.setUtenteAssegnatario(rs
						.getString("carica_assegnatario_id"));
				protocollo.setPdf(rs.getInt("DOCUMENTO_ID") > 0 ? true : false);
				protocollo.setDocumentoId(rs.getInt("DOCUMENTO_ID"));
				protocollo.setMessaggio(StringUtil.getStringa(rs
						.getString("messaggio")));
				if (rs.getDate("data_scarico") != null)
					protocollo.setDataScarico(DateUtil.formattaData(rs.getDate(
							"data_scarico").getTime()));
				protocollo.setCompetente(rs.getBoolean("flag_competente"));
				protocollo.setTipoProtocollo(rs.getString("flag_tipo"));
				protocollo.setVersione(rs.getInt("versione"));
				protocollo.setRegistroAnnoNumero(rs
						.getLong("registro_anno_numero"));
				if (rs.getInt("carica_protocollatore_id") == utente
						.getCaricaInUso())
					protocollo.setRifiutabile(false);
				protocolli.put(protocollo.getAnnoNumero(), protocollo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getProtocolliAssegnati", e);
			throw new DataException("Cannot load getProtocolliAssegnati");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return protocolli;

	}

	public Map<Long,ReportProtocolloView> getPostaInternaAssegnataPerNumero(Utente utente, int numero,
			String tipoUtenteUfficio) throws DataException {

		SortedMap<Long,ReportProtocolloView> protocolli = new TreeMap<Long,ReportProtocolloView>(new Comparator<Long>() {
			public int compare(Long i1, Long i2) {
				return i2.compareTo(i1);
			}
		});

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String strQuery = "SELECT p.*, a.ufficio_assegnante_id, a.ufficio_assegnatario_id, "
				+ " a.carica_assegnante_id, a.carica_assegnatario_id, a.messaggio,a.flag_competente"
				+ " FROM PROTOCOLLI p, PROTOCOLLO_ASSEGNATARI a"
				+ " WHERE flag_tipo='P'"
				+ " AND p.protocollo_id = a.protocollo_id AND ";
		if ("T".equals(tipoUtenteUfficio)) {
			strQuery = strQuery
					+ " carica_assegnatario_id=? AND check_lavorato=0 AND ";
		} else if (("U".equals(tipoUtenteUfficio))) {
			strQuery = strQuery + " ufficio_assegnatario_id IN ("
					+ utente.getUfficiIds() + ") ";
			strQuery = strQuery
					+ " AND carica_assegnatario_id IS NULL AND (check_presa_visione IS NULL OR check_presa_visione=0) "
					+ " AND check_lavorato=0 AND ";
		}
		strQuery = strQuery + " nume_protocollo=? ";
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			int currPstmt = 1;
			// pstmt.setInt(currPstmt++, registro);
			if ("T".equals(tipoUtenteUfficio))
				pstmt.setInt(currPstmt++, utente.getCaricaInUso());
			pstmt.setInt(currPstmt++, numero);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ReportProtocolloView protocollo = new ReportProtocolloView();
				protocollo.setProtocolloId(rs.getInt("protocollo_id"));
				protocollo.setNumeroProtocollo(rs.getInt("NUME_PROTOCOLLO"));
				protocollo.setAnnoProtocollo(rs.getInt("ANNO_REGISTRAZIONE"));
				if ("M".equals(rs.getString("flag_tipo_mittente"))) {
					protocollo.setTipoMittente("M");
				}
				protocollo.setDataProtocollo(DateUtil.formattaData(rs.getDate(
						"DATA_REGISTRAZIONE").getTime()));

				if (rs.getInt("FLAG_RISERVATO") == 1) {
					protocollo.setOggetto(Parametri.PROTOCOLLO_RISERVATO);
					protocollo.setMittente(Parametri.PROTOCOLLO_RISERVATO);

				} else {
					protocollo.setOggetto(rs.getString("TEXT_OGGETTO"));
					protocollo.setMittente(StringUtil.getStringa(rs
							.getString("DESC_COGNOME_MITTENTE"))
							+ " "
							+ StringUtil.getStringa(rs
									.getString("DESC_NOME_MITTENTE"))
							+ StringUtil.getStringa(rs
									.getString("DESC_DENOMINAZIONE_MITTENTE")));
				}
				protocollo.setUfficio(rs.getString("ufficio_assegnante_id"));
				protocollo.setUtenteAssegnante(rs
						.getString("carica_assegnante_id"));
				protocollo.setUfficioAssegnatario(rs
						.getString("ufficio_assegnatario_id"));
				protocollo.setUtenteAssegnatario(rs
						.getString("carica_assegnatario_id"));
				protocollo.setPdf(rs.getInt("DOCUMENTO_ID") > 0 ? true : false);
				protocollo.setDocumentoId(rs.getInt("DOCUMENTO_ID"));
				protocollo.setMessaggio(StringUtil.getStringa(rs
						.getString("messaggio")));
				if (rs.getDate("data_scarico") != null)
					protocollo.setDataScarico(DateUtil.formattaData(rs.getDate(
							"data_scarico").getTime()));
				protocollo.setCompetente(rs.getBoolean("flag_competente"));
				protocollo.setTipoProtocollo(rs.getString("flag_tipo"));
				protocollo.setVersione(rs.getInt("versione"));
				protocollo.setRegistroAnnoNumero(rs
						.getLong("registro_anno_numero"));
				protocolli.put(protocollo.getAnnoNumero(), protocollo);
			}
		} catch (Exception e) {
			logger.error("getProtocolliAssegnati", e);
			throw new DataException("Cannot load getProtocolliAssegnati");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return protocolli;

	}

	public Map<Long,ReportProtocolloView> getProtocolliRespinti(Utente utente, int annoProtocolloDa,
			int annoProtocolloA, int numeroProtocolloDa, int numeroProtocolloA,
			java.util.Date dataDa, java.util.Date dataA) throws DataException {

		SortedMap<Long,ReportProtocolloView> protocolli = new TreeMap<Long,ReportProtocolloView>(new Comparator<Long>() {
			public int compare(Long i1, Long i2) {
				return i2.compareTo(i1);
			}
		});

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String strQuery = "SELECT P.*, ufficio_assegnatario_id, carica_assegnatario_id, "
				+ " ufficio_assegnante_id, carica_assegnante_id, messaggio"
				+ " FROM PROTOCOLLI P, PROTOCOLLO_ASSEGNATARI A"
				+ " WHERE p.registro_id=? AND flag_competente=1 AND A.protocollo_id = P.protocollo_id "
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
			while (rs.next()) {
				ReportProtocolloView protocollo = new ReportProtocolloView();
				protocollo.setProtocolloId(rs.getInt("protocollo_id"));
				protocollo.setRegistroAnnoNumero(rs
						.getLong("registro_anno_numero"));
				protocollo.setAnnoProtocollo(rs.getInt("ANNO_REGISTRAZIONE"));
				protocollo.setNumeroProtocollo(rs.getInt("NUME_PROTOCOLLO"));
				protocollo.setDataProtocollo(DateUtil.formattaData(rs.getDate(
						"data_registrazione").getTime()));
				if (rs.getInt("FLAG_RISERVATO") == 1) {
					protocollo.setOggetto(Parametri.PROTOCOLLO_RISERVATO);
					protocollo.setMittente(Parametri.PROTOCOLLO_RISERVATO);

				} else {
					protocollo.setOggetto(rs.getString("TEXT_OGGETTO"));
					protocollo.setMittente(StringUtil.getStringa(rs
							.getString("DESC_COGNOME_MITTENTE"))
							+ " "
							+ StringUtil.getStringa(rs
									.getString("DESC_NOME_MITTENTE"))
							+ StringUtil.getStringa(rs
									.getString("DESC_DENOMINAZIONE_MITTENTE")));
				}

				protocollo.setUfficioAssegnatario(""
						+ rs.getInt("ufficio_assegnante_id"));
				protocollo.setUtenteAssegnatario(""
						+ rs.getInt("carica_assegnante_id"));

				protocollo.setPdf(rs.getInt("DOCUMENTO_ID") > 0 ? true : false);
				protocollo.setDocumentoId(rs.getInt("DOCUMENTO_ID"));
				protocollo.setMessaggio(StringUtil.getStringa(rs
						.getString("messaggio")));
				if ("M".equals(rs.getString("flag_tipo_mittente"))) {
					protocollo.setTipoMittente("M");
					protocollo.setMittente(Parametri.MULTI_MITTENTE);
				}
				if (rs.getInt("carica_protocollatore_id") == utente
						.getCaricaInUso())
					protocollo.setRifiutabile(false);
				else
					protocollo.setRifiutabile(true);
				protocolli.put(protocollo.getAnnoNumero(), protocollo);
			}
		} catch (Exception e) {
			logger.error("getProtocolliRespinti", e);
			throw new DataException("Cannot load getProtocolliRespinti");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return protocolli;

	}

	public int updateScarico(Connection connection, ProtocolloVO protocolloVO,
			String flagScarico, Utente utente, boolean titolareProcedimento)
			throws DataException {
		PreparedStatement pstmt = null;
		int fglagStatus = ReturnValues.SAVED;
		Date toDay = new Date(System.currentTimeMillis());
		try {
			if (connection == null) {
				logger.warn("updateScarico() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			archiviaVersione(connection, protocolloVO.getId().intValue());
			pstmt = connection.prepareStatement(UPDATE_SCARICO);
			pstmt.setString(1, flagScarico);
			if ("R".equals(flagScarico) || "A".equals(flagScarico)) {
				pstmt.setDate(2, toDay);
			} else {
				pstmt.setNull(2, Types.DATE);
			}
			pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			pstmt.setString(4, utente.getValueObject().getUsername());
			pstmt.setInt(5, protocolloVO.getId().intValue());
			pstmt.setInt(6, protocolloVO.getVersione() + 1);
			int n = pstmt.executeUpdate();
			if (n == 0) {
				fglagStatus = ReturnValues.OLD_VERSION;
				throw new DataException("Versione incongruente.");
			} else {
				jdbcMan.close(pstmt);
				int indice = 1;
				String sqlUpdate = "UPDATE protocollo_assegnatari SET carica_assegnatario_id=?,flag_titolare_procedimento=?"
						+ " WHERE protocollo_id=? AND flag_competente=1";
				pstmt = connection.prepareStatement(sqlUpdate);
				if ("R".equals(flagScarico) || "A".equals(flagScarico)) {
					pstmt.setNull(indice++, Types.INTEGER);
					pstmt.setInt(indice++, titolareProcedimento ? 1 : 0);
				} else if ("N".equals(flagScarico)) {
					pstmt.setInt(indice++, utente.getCaricaInUso());
					pstmt.setInt(indice++, titolareProcedimento ? 1 : 0);
				} else if ("F".equals(flagScarico)) {
					String versione = "(SELECT MAX(v.versione) FROM storia_protocollo_assegnatari v "
							+ " WHERE v.protocollo_id=p.protocollo_id"
							+ " AND ((v.ufficio_assegnatario_id=p.ufficio_assegnante_id AND v.carica_assegnatario_id=p.carica_assegnante_id)"
							+ " OR (v.ufficio_assegnatario_id=p.ufficio_assegnante_id AND v.carica_assegnatario_id IS NULL)))";

					String ufficioAss = "SELECT DISTINCT (a.ufficio_assegnante_id) FROM storia_protocollo_assegnatari a "
							+ "WHERE a.protocollo_id=p.protocollo_id AND a.versione="
							+ versione
							+ " AND ((a.ufficio_assegnatario_id=p.ufficio_assegnante_id AND a.carica_assegnatario_id=p.carica_assegnante_id)"
							+ " OR (a.ufficio_assegnatario_id=p.ufficio_assegnante_id AND a.carica_assegnatario_id IS NULL))";

					String caricaAss = "SELECT DISTINCT (a.carica_assegnante_id) FROM storia_protocollo_assegnatari a "
							+ "WHERE a.protocollo_id=p.protocollo_id AND a.versione="
							+ versione
							+ " AND ((a.ufficio_assegnatario_id=p.ufficio_assegnante_id AND a.carica_assegnatario_id=p.carica_assegnante_id)"
							+ " OR (a.ufficio_assegnatario_id=p.ufficio_assegnante_id AND a.carica_assegnatario_id IS NULL))";

					sqlUpdate = "UPDATE PROTOCOLLO_ASSEGNATARI p set"
							+ " ufficio_assegnatario_id=p.ufficio_assegnante_id,"
							+ " carica_assegnatario_id=p.carica_assegnante_id,flag_titolare_procedimento="
							+ String.valueOf(titolareProcedimento ? 1 : 0)
							+ "," + " stat_assegnazione='R',"
							+ " flag_competente=1,"
							+ " ufficio_assegnante_id=(" + ufficioAss + ") ,"
							+ " carica_assegnante_id=(" + caricaAss + ")"
							+ " WHERE p.protocollo_id=?"
							+ " AND ((ufficio_assegnatario_id="
							+ utente.getUfficioInUso()
							+ " AND carica_assegnatario_id IS NULL)"
							+ " OR (ufficio_assegnatario_id="
							+ utente.getUfficioInUso()
							+ " AND carica_assegnatario_id ="
							+ utente.getCaricaInUso() + "))";
					pstmt = connection.prepareStatement(sqlUpdate);
				}
				pstmt.setInt(indice++, protocolloVO.getId().intValue());
				pstmt.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("updateScarico:" + e.getMessage(), e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return fglagStatus;
	}

	public SortedMap<Long,ReportProtocolloView> cercaProtocolli(Utente utente, Ufficio uff, LinkedHashMap<String,Object> sqlDB,
			boolean isTabula) throws DataException {

		SortedMap<Long,ReportProtocolloView> protocolli = new TreeMap<Long,ReportProtocolloView>(new Comparator<Long>() {
			public int compare(Long i1, Long i2) {
				return i2.compareTo(i1);
			}
		});

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer strQuery = new StringBuffer(SELECT_LISTA_PROTOCOLLI);

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
							pstmt.setString(indiceQuery, value.toString() + "%");
						} else {
							pstmt.setString(indiceQuery, value.toString());
						}
					} else if (value instanceof java.util.Date) {
						java.util.Date d = (java.util.Date) value;
						pstmt.setTimestamp(indiceQuery, new java.sql.Timestamp(
								d.getTime()));
					} else if (value instanceof Boolean) {
						pstmt.setBoolean(indiceQuery,
								((Boolean) value).booleanValue());
					} else if (value instanceof Long) {
						pstmt.setLong(indiceQuery, ((Long) value).longValue());
					}
					indiceQuery++;
				}
			}
			rs = pstmt.executeQuery();
			ReportProtocolloView protocollo = null;
			StringBuffer dest_ass = null;
			while (rs.next()) {
				if (protocollo == null
						|| protocollo.getProtocolloId() != rs
								.getInt("protocollo_id")) {
					if (protocollo != null) {
						dest_ass.append("</ul>");
						protocollo.setDestinatario(dest_ass.toString());
					}
					dest_ass = new StringBuffer("<ul>");
					protocollo = new ReportProtocolloView();
					protocollo.setProtocolloId(rs.getInt("protocollo_id"));
					protocollo.setRegistroAnnoNumero(rs
							.getLong("registro_anno_numero"));
					protocollo.setVersione(rs.getInt("versione"));
					protocollo.setAnnoProtocollo(rs
							.getInt("anno_registrazione"));
					protocollo
							.setNumeroProtocollo(rs.getInt("nume_protocollo"));
					protocollo.setTipoProtocollo(rs.getString("flag_tipo"));

					if (rs.getBoolean("flag_riservato")) {
						protocollo.setOggetto(Parametri.PROTOCOLLO_RISERVATO);
						protocollo.setMittente(Parametri.PROTOCOLLO_RISERVATO);
						protocollo.setModificabile(false);
					} else {
						protocollo.setOggetto(rs.getString("text_oggetto"));
						if (rs.getInt("num_prot_emergenza") != 0) {
							protocollo.setOggetto(protocollo.getOggetto()
									.concat(" (R)"));
						}
						StringBuffer mittente = new StringBuffer();
						if ("F".equals(rs.getString("flag_tipo_mittente"))) {
							mittente.append(rs
									.getString("desc_cognome_mittente"));
							if (rs.getString("desc_nome_mittente") != null) {
								mittente.append(' ').append(
										rs.getString("desc_nome_mittente"));
							}
						} else if ("M".equals(rs
								.getString("flag_tipo_mittente"))) {
							protocollo.setTipoMittente("M");
							mittente.append(Parametri.MULTI_MITTENTE);
						} else {
							mittente.append(rs
									.getString("desc_denominazione_mittente"));
						}
						protocollo.setMittente(mittente.toString());
						protocollo.setModificabile(true);
					}
					protocollo.setDataProtocollo(DateUtil.formattaData(rs
							.getDate("data_registrazione").getTime()));
					protocollo.setPdf(rs.getInt("documento_id") > 0);
					protocollo.setDocumentoId(rs.getInt("documento_id"));
					//
					protocollo.setFilename(getDocId(rs.getInt("documento_id")));
					if (isTabula) {
						int docId = getDocTabulaId(rs.getInt("protocollo_id"));
						protocollo.setPdf(docId > 0);
						protocollo.setDocumentoId(docId);
						protocollo.setFilename(String.valueOf(docId));
					}

					protocollo.setTitolarioId(rs.getInt("titolario_id"));
					protocollo.setStatoProtocollo(rs
							.getString("stato_protocollo"));
					// protocollo.getRegistroAnnoNumero();
					protocolli.put(protocollo.getAnnoNumero(), protocollo);
				}
				if (!rs.getBoolean("flag_riservato")) {
					dest_ass.append("<li>");
					if ("I".equals(protocollo.getTipoProtocollo())) {
						if (rs.getBoolean("flag_competente")) {
							dest_ass.append("<em>");
						}
						dest_ass.append(rs.getString("ufficio_assegnatario"));
						if (rs.getString("cognome_assegnatario") != null) {
							dest_ass.append(" / ").append(
									rs.getString("cognome_assegnatario"));
							if (rs.getString("nome_assegnatario") != null) {
								dest_ass.append(' ').append(
										rs.getString("nome_assegnatario"));
							}
							if (rs.getString("carica_assegnatario") != null) {
								dest_ass.append("(").append(
										rs.getString("carica_assegnatario")).append(")");
							}
						}
						
						if (rs.getBoolean("flag_competente")) {
							dest_ass.append("</em>");
						}
					} else if ("P".equals(protocollo.getTipoProtocollo())) {
						if (rs.getBoolean("flag_competente")) {
							dest_ass.append("<em>");
						}
						dest_ass.append(rs.getString("ufficio_assegnatario"));
						if (rs.getString("cognome_assegnatario") != null) {
							dest_ass.append(" / ").append(
									rs.getString("cognome_assegnatario"));
							if (rs.getString("nome_assegnatario") != null) {
								dest_ass.append(' ').append(
										rs.getString("nome_assegnatario"));
							}
						}
						if (rs.getBoolean("flag_competente")) {
							dest_ass.append("</em>");
						}
					} else {
						if (rs.getInt("flag_conoscenza") == 0) {
							dest_ass.append("<em>");
						}
						dest_ass.append(rs.getString("destinatario"));
						if (rs.getInt("flag_conoscenza") == 0) {
							dest_ass.append("</em>");
						}
					}
					dest_ass.append("</li>");
				} else {
					dest_ass = new StringBuffer(Parametri.PROTOCOLLO_RISERVATO);
				}

			}
			if (protocollo != null) {
				dest_ass.append("</ul>");
				protocollo.setDestinatario(dest_ass.toString());
			}

		} catch (Exception e) {
			logger.error("cercaProtocolli", e);
			throw new DataException("Cannot load cercaProtocolli");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return protocolli;

	}

	public Collection<MittenteView> getMittenti(String mittente) throws DataException {
		ArrayList<MittenteView> mittenti = new ArrayList<MittenteView>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(MITTENTI_PROTOCOLLI);
			pstmt.setString(1, mittente.toLowerCase() + "%");
			pstmt.setString(2, mittente.toLowerCase() + "%");

			rs = pstmt.executeQuery();
			while (rs.next()) {
				MittenteView mittenteView = new MittenteView();
				mittenteView.setTipo(rs.getString("FLAG_TIPO_MITTENTE"));
				mittenteView.setCognome(rs.getString("DESC_COGNOME_MITTENTE"));
				mittenteView.setNome(StringUtil.getStringa(rs
						.getString("DESC_NOME_MITTENTE")));
				mittenteView.setDenominazione(rs
						.getString("DESC_DENOMINAZIONE_MITTENTE"));
				mittenti.add(mittenteView);
			}
		} catch (Exception e) {
			logger.error("getMittenti", e);
			throw new DataException("Cannot load getMittenti");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return mittenti;
	}

	public Map<String,DestinatarioVO> getDestinatariProtocollo(int protocolloId) throws DataException {
		HashMap<String,DestinatarioVO> destinatari = new HashMap<String,DestinatarioVO>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(PROTOCOLLO_DESTINATARI);
			pstmt.setInt(1, protocolloId);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				DestinatarioVO destinatarioVO = new DestinatarioVO();
				destinatarioVO.setId(rs.getInt("destinatario_id"));
				destinatarioVO.setFlagTipoDestinatario(rs
						.getString("flag_tipo_destinatario"));
				destinatarioVO.setIndirizzo(rs.getString("indirizzo"));
				destinatarioVO.setEmail(rs.getString("email"));
				destinatarioVO.setDestinatario(rs.getString("DESTINATARIO"));
				destinatarioVO.setTitoloId(rs.getInt("titolo_id"));
				destinatarioVO.setCitta(rs.getString("citta"));
				destinatarioVO.setNote(rs.getString("note"));
				destinatarioVO.setDataSpedizione(rs.getDate("data_spedizione"));
				destinatarioVO.setFlagConoscenza("1".equals(rs
						.getString("flag_conoscenza")));
				destinatarioVO.setFlagPresso("1".equals(rs
						.getString("flag_presso")));
				destinatarioVO.setFlagPEC("1".equals(rs.getString("flag_pec")));
				destinatarioVO.setProtocolloId(rs.getInt("protocollo_id"));
				destinatarioVO.setDataEffettivaSpedizione(rs
						.getDate("data_effettiva_spedizione"));
				destinatarioVO.setVersione(rs.getInt("versione"));
				destinatarioVO.setMezzoSpedizioneId(rs
						.getInt("mezzo_spedizione_id"));
				destinatarioVO.setMezzoDesc(rs.getString("desc_spedizione"));
				destinatarioVO.setCodicePostale(rs.getString("codice_postale"));
				destinatarioVO.setPrezzo(rs.getString("prezzo_spedizione"));
				destinatari.put(String.valueOf(destinatarioVO.getId()),
						destinatarioVO);
			}
		} catch (Exception e) {
			logger.error("getDestinatariProtocollo", e);
			throw new DataException("Cannot load getDestinatariProtocollo");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return destinatari;
	}

	public Collection<DestinatarioVO> getDestinatari(String destinatario) throws DataException {
		ArrayList<DestinatarioVO> destinatari = new ArrayList<DestinatarioVO>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(DESTINATARI_PROTOCOLLI);
			pstmt.setString(1, destinatario + "%");

			rs = pstmt.executeQuery();
			while (rs.next()) {
				DestinatarioVO destinatarioVO = new DestinatarioVO();
				destinatarioVO.setDestinatario(rs.getString("DESTINATARIO"));
				destinatari.add(destinatarioVO);
			}
		} catch (Exception e) {
			logger.error("getDestinatari", e);
			throw new DataException("Cannot load getDestinatari");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return destinatari;
	}

	public int annullaProtocollo(Connection connection,
			ProtocolloVO protocolloVO, Utente utente) throws DataException {
		PreparedStatement pstmt = null;
		int fglagStatus = ReturnValues.SAVED;
		try {
			Date toDay = new Date(System.currentTimeMillis());
			if (connection == null) {
				logger.warn("annullaProtocollo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			archiviaVersione(connection, protocolloVO.getId().intValue());
			pstmt = connection.prepareStatement(ANNULLA_PROTOCOLLO);
			pstmt.setString(1, protocolloVO.getStatoProtocollo());
			pstmt.setDate(2, toDay);
			pstmt.setString(3, protocolloVO.getNotaAnnullamento());
			pstmt.setString(4, protocolloVO.getProvvedimentoAnnullamento());
			pstmt.setString(5, utente.getValueObject().getUsername());
			pstmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
			pstmt.setInt(7, protocolloVO.getId().intValue());
			pstmt.setInt(8, protocolloVO.getVersione() + 1);
			int n = pstmt.executeUpdate();
			if (n == 0) {
				fglagStatus = ReturnValues.OLD_VERSION;
				throw new DataException("Versione incongruente.");
			}
		} catch (Exception e) {
			logger.error("annullaProtocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return fglagStatus;
	}

	public int salvaSegnatura(Connection connection, SegnaturaVO segnaturaVO)
			throws DataException {
		PreparedStatement pstmt = null;
		int fglagStatus = ReturnValues.SAVED;
		try {
			if (connection == null) {
				logger.warn("salvaSignature() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(INSERT_SEGNATURE);
			pstmt.setInt(1, segnaturaVO.getId().intValue());
			pstmt.setInt(2, segnaturaVO.getFkProtocolloId());
			pstmt.setString(3, segnaturaVO.getTipoProtocollo());
			pstmt.setString(4, segnaturaVO.getTextSegnatura());
			pstmt.setString(5, segnaturaVO.getRowCreatedUser());
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("salvaSignature", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return fglagStatus;
	}

	public SegnaturaVO getSegnatura(int id) throws DataException {
		SegnaturaVO sVO = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection connection = null;
		try {

			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_SEGNATURE);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				sVO = new SegnaturaVO();
				sVO.setId(rs.getInt("segnature_id"));
				sVO.setTextSegnatura(rs.getString("text_segnatura"));
			}
		} catch (Exception e) {
			logger.error("getProtocolliToExport", e);
			throw new DataException("Cannot load getSegnatura");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return sVO;
	}

	public boolean updateCheckPostaInterna(int checkId) throws DataException {
		boolean updated = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		// ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(UPDATE_CHECK_POSTA_INTERNA);
			pstmt.setInt(1, checkId);
			pstmt.executeUpdate();
			updated = true;
		} catch (Exception e) {
			logger.error("updateCheckPostaInterna", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(connection);
			jdbcMan.close(pstmt);
		}
		return updated;
	}

	public boolean updateCheckPostaInternaUtente(int caricaId)
			throws DataException {
		boolean updated = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection
					.prepareStatement(UPDATE_CHECK_POSTA_INTERNA_UTENTE);
			pstmt.setInt(1, caricaId);
			pstmt.executeUpdate();
			updated = true;
		} catch (Exception e) {
			logger.error("updateCheckPostaInterna", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(connection);
			jdbcMan.close(pstmt);
		}
		return updated;
	}

	public void eliminaDestinatariProtocollo(Connection connection,
			int protocolloId) throws DataException {
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(DELETE_DESTINATARI);
			pstmt.setInt(1, protocolloId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("eliminaDestinatariProtocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			// jdbcMan.close(connection);
			jdbcMan.close(pstmt);
		}
	}

	public void eliminaDocumentoPrincipale(Connection connection,
			int protocolloId) throws DataException {
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(DELETE_DOCUMENTO_PRINCIPALE);
			pstmt.setInt(1, protocolloId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("eliminaDestinatariProtocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void salvaDestinatario(Connection connection,
			DestinatarioVO destinatario, int versione) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("salvaDestinatario() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			if (destinatario != null) {
				pstmt = connection
						.prepareStatement(INSERT_PROTOCOLLO_DESTINATARI);
				java.sql.Date now = new java.sql.Date(
						(new java.util.Date()).getTime());
				pstmt.setInt(1, destinatario.getId().intValue());
				pstmt.setString(2, destinatario.getFlagTipoDestinatario());
				pstmt.setString(3, destinatario.getIndirizzo());
				pstmt.setString(4, destinatario.getEmail());
				pstmt.setString(5, destinatario.getDestinatario());
				pstmt.setString(6, destinatario.getCitta());
				if (destinatario.getDataSpedizione() != null) {
					pstmt.setDate(7, new java.sql.Date(destinatario
							.getDataSpedizione().getTime()));
				} else {
					pstmt.setNull(7, Types.DATE);
				}
				pstmt.setString(8, destinatario.getFlagConoscenza() ? "1" : "0");
				pstmt.setInt(9, destinatario.getProtocolloId());
				pstmt.setDate(10, now);
				pstmt.setInt(11, versione);

				if (destinatario.getMezzoSpedizioneId() != 0)
					pstmt.setInt(12, destinatario.getMezzoSpedizioneId());
				else
					pstmt.setNull(12, Types.INTEGER);
				if (destinatario.getTitoloId() == 0) {
					destinatario.setTitoloId(999);
				}
				pstmt.setInt(13, destinatario.getTitoloId());
				pstmt.setString(14, destinatario.getMezzoDesc());
				pstmt.setString(15, destinatario.getNote());
				pstmt.setString(16, destinatario.getCodicePostale());
				pstmt.setString(17, destinatario.getFlagPresso() ? "1" : "0");
				pstmt.setString(18, destinatario.getFlagPEC() ? "1" : "0");
				pstmt.setString(19, destinatario.getPrezzo());
				pstmt.executeUpdate();
			}
		} catch (Exception e) {
			logger.error("Save Destinatari-Protocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public Collection<ReportProtocolloView> getProtocolliByProtMittente(Utente utente,
			String protMittente) throws DataException {
		ArrayList<ReportProtocolloView> protocolli = new ArrayList<ReportProtocolloView>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String protAllacciabili = "SELECT protocollo_id, ANNO_REGISTRAZIONE, NUME_PROTOCOLLO,"
					+ " data_registrazione, flag_tipo_mittente, desc_denominazione_mittente, desc_cognome_mittente,"
					+ " desc_nome_mittente, FLAG_TIPO, text_oggetto FROM PROTOCOLLI WHERE"
					+ " registro_id=? AND nume_protocollo_mittente =? AND aoo_id=?"
					+ "	ORDER BY ANNO_REGISTRAZIONE desc, NUME_PROTOCOLLO desc";
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(protAllacciabili);
			pstmt.setInt(1, utente.getRegistroInUso());
			pstmt.setString(2, protMittente);
			pstmt.setInt(3, utente.getAreaOrganizzativa().getId().intValue());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ReportProtocolloView protocolloView = new ReportProtocolloView();
				protocolloView.setAnnoProtocollo(rs
						.getInt("ANNO_REGISTRAZIONE"));
				protocolloView
						.setNumeroProtocollo(rs.getInt("NUME_PROTOCOLLO"));
				protocolloView.setDataProtocollo(DateUtil.formattaData(rs
						.getDate("data_registrazione").getTime()));
				protocolloView.setTipoProtocollo(rs.getString("FLAG_TIPO"));
				protocolloView.setOggetto(rs.getString("text_oggetto"));
				if (rs.getString("flag_tipo_mittente").equals("F")) {
					protocolloView.setMittente(rs
							.getString("desc_cognome_mittente")
							+ " "
							+ StringUtil.getStringa(rs
									.getString("desc_nome_mittente")));
				} else {
					protocolloView.setMittente(rs
							.getString("DESC_DENOMINAZIONE_MITTENTE"));
				}
				protocolli.add(protocolloView);
			}
		} catch (Exception e) {
			logger.error("getProtocolliByProtMittente", e);
			throw new DataException("Cannot load getProtocolliByProtMittente");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return protocolli;
	}

	public String getDocId(int documentoId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String desc = new String();
		try {
			String queryDocId = "SELECT * FROM DOCUMENTI where documento_id=?";
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(queryDocId);
			pstmt.setInt(1, documentoId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				desc = rs.getString(3);
			}
		} catch (Exception e) {
			logger.error("getMezziSpedizione", e);
			throw new DataException("Cannot load getMezziSpedizione");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return desc;
	}

	public int getDocTabulaId(int protocolloId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int desc = 0;
		try {
			String queryDocId = "SELECT documento_id FROM DOCUMENTI_TABULA where protocollo_id=?";
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(queryDocId);
			pstmt.setInt(1, protocolloId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				desc = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("getMezziSpedizione", e);
			throw new DataException("Cannot load getMezziSpedizione");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return desc;
	}

	public boolean esisteAllaccio(Connection connection, int protocolloId,
			int protocolloAllacciatoId) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean trovato = false;
		final String SELECT_ALLACCIO = "select count(protocollo_id) FROM protocollo_allacci "
				+ "WHERE protocollo_id = ? and protocollo_allacciato_id = ?";

		try {
			pstmt = connection.prepareStatement(SELECT_ALLACCIO);
			pstmt.setInt(1, protocolloId);
			pstmt.setInt(2, protocolloAllacciatoId);
			rs = pstmt.executeQuery();
			rs.next();
			trovato = (rs.getInt(1) > 0);

		} catch (Exception e) {
			logger.error("esisteAllaccio", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		return trovato;
	}

	public void eliminaAllacciProtocollo(Connection connection, int protocolloId)
			throws DataException {
		PreparedStatement pstmt = null;

		final String DELETE_ALLACCI = "DELETE FROM protocollo_allacci WHERE protocollo_id = ?";

		try {
			pstmt = connection.prepareStatement(DELETE_ALLACCI);
			pstmt.setInt(1, protocolloId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("eliminaAllacciProtocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void eliminaAllegatiProtocollo(Connection connection,
			int protocolloId) throws DataException {
		PreparedStatement pstmt = null;

		final String DELETE_ALLACCI = "DELETE FROM protocollo_allegati WHERE protocollo_id = ?";

		try {
			pstmt = connection.prepareStatement(DELETE_ALLACCI);
			pstmt.setInt(1, protocolloId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("eliminaAllegatiProtocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void eliminaAllaccioProtocollo(Connection connection,
			int protocolloId, int protocolloAllacciatoId) throws DataException {
		PreparedStatement pstmt = null;

		final String DELETE_ALLACCIO = "DELETE FROM protocollo_allacci WHERE "
				+ " protocollo_id = ?  AND protocollo_allacciato_id=? OR "
				+ " protocollo_id = ?  AND protocollo_allacciato_id=? ";

		try {
			pstmt = connection.prepareStatement(DELETE_ALLACCIO);
			pstmt.setInt(1, protocolloId);
			pstmt.setInt(2, protocolloAllacciatoId);
			pstmt.setInt(3, protocolloAllacciatoId);
			pstmt.setInt(4, protocolloId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("eliminaAllaccioProtocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void eliminaAssegnatariProtocollo(Connection connection,
			int protocolloId) throws DataException {
		PreparedStatement pstmt = null;

		final String DELETE_ASSEGNATARI = "DELETE FROM protocollo_assegnatari WHERE protocollo_id = ?";

		try {
			pstmt = connection.prepareStatement(DELETE_ASSEGNATARI);
			pstmt.setInt(1, protocolloId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("eliminaAllacciProtocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public int riassegnaProtocollo(ProtocolloIngresso protocollo, Utente utente)
			throws DataException {
		Connection connection = null;

		int statusFlag = ReturnValues.UNKNOWN;
		try {
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			Collection<AssegnatarioVO> assegnatari = protocollo.getAssegnatari();
			int protocolloId = protocollo.getProtocollo().getId().intValue();
			int versione = protocollo.getProtocollo().getVersione() + 1;
			protocollo.getProtocollo().setRowUpdatedUser(
					utente.getValueObject().getUsername());
			if (assegnatari != null) {
				aggiornaProtocollo(connection, protocollo.getProtocollo());
				eliminaAssegnatariProtocollo(connection, protocolloId);
				for (Iterator<AssegnatarioVO> i = assegnatari.iterator(); i.hasNext();) {
					AssegnatarioVO assegnatario = (AssegnatarioVO) i.next();
					assegnatario.setProtocolloId(protocolloId);
					assegnatario.setId(IdentificativiDelegate.getInstance()
							.getNextId(connection,
									NomiTabelle.PROTOCOLLO_ASSEGNATARI));
					salvaAssegnatario(connection, assegnatario, versione);
				}
			}
			connection.commit();
			statusFlag = ReturnValues.SAVED;
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.error("failed riassegnaProtocollo: ");
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
		} finally {
			jdbcMan.close(connection);
		}
		return statusFlag;
	}

	public int getDocumentoDefault(int aooId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int documentoDefaultId = 0;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_DOCUMENTO_DEFAULT);
			pstmt.setInt(1, aooId);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				documentoDefaultId = rs.getInt("tipo_documento_id");
			}
		} catch (Exception e) {
			logger.error("Load getDocumentoDefault", e);
			throw new DataException("Cannot load getDocumentoDefault");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return documentoDefaultId;
	}

	public Collection<ProtocolloVO> getProtocolliToExport(Connection connection,
			int registroId) throws DataException {
		Collection<ProtocolloVO> protocolli = new ArrayList<ProtocolloVO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String strQuery = "SELECT * FROM PROTOCOLLI WHERE registro_id=? and (num_prot_emergenza=0 "
					+ "OR num_prot_emergenza IS NULL) ORDER BY PROTOCOLLO_ID DESC";
			pstmt = connection.prepareStatement(strQuery.toString());
			pstmt.setInt(1, registroId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ProtocolloVO protocollo = new ProtocolloVO();
				protocollo.setId(rs.getInt("protocollo_id"));
				protocollo
						.setAnnoRegistrazione(rs.getInt("ANNO_REGISTRAZIONE"));
				protocollo.setNumProtocollo(rs.getInt("NUME_PROTOCOLLO"));
				protocollo.setOggetto(rs.getString("TEXT_OGGETTO"));
				protocollo.setFlagTipo(rs.getString("FLAG_TIPO"));
				protocollo.setDataRegistrazione(rs
						.getDate("data_registrazione"));
				protocolli.add(protocollo);
			}
		} catch (Exception e) {
			logger.error("getProtocolliToExport", e);
			throw new DataException("Cannot load getProtocolliToExport");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return protocolli;
	}

	public void updateRegistroEmergenza(Connection connection)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("updateRegistroEmergenza() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection
					.prepareStatement("UPDATE PROTOCOLLI SET num_prot_emergenza = nume_protocollo WHERE num_prot_emergenza=0 OR num_prot_emergenza IS NULL");
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("updateScarico", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void updateMsgAssegnatarioCompetenteByIdProtocollo(
			Connection connection, String msgAssegnatarioCompetente,
			int protocolloId) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("updateMsgAssegnatarioCompetenteByIdProtocollo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			if (msgAssegnatarioCompetente != null && protocolloId > 0) {
				pstmt = connection
						.prepareStatement(UPDATE_MSG_ASSEGNATARIO_COMPETENTE);
				pstmt.setString(1, msgAssegnatarioCompetente);
				pstmt.setInt(2, protocolloId);
				pstmt.executeUpdate();
			}

		} catch (Exception e) {
			logger.error(
					"Error: updateMsgAssegnatarioCompetenteByIdProtocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void updateMsgAssegnatario(Connection connection,
			String msgAssegnatarioCompetente, int protocolloId, Utente utente)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("updateMsgAssegnatarioCompetenteByIdProtocollo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			if (msgAssegnatarioCompetente != null && protocolloId > 0) {

				String sql = "UPDATE protocollo_assegnatari"
						+ " set messaggio=? WHERE protocollo_id= ?";
				sql += " AND ((ufficio_assegnatario_id="
						+ utente.getUfficioInUso()
						+ " AND carica_assegnatario_id IS NULL)"
						+ " OR (ufficio_assegnatario_id="
						+ utente.getUfficioInUso()
						+ " AND carica_assegnatario_id ="
						+ utente.getCaricaInUso() + "))";
				pstmt = connection.prepareStatement(sql);
				pstmt.setString(1, msgAssegnatarioCompetente);
				pstmt.setInt(2, protocolloId);
				pstmt.executeUpdate();
			}

		} catch (Exception e) {
			logger.error(
					"Error: updateMsgAssegnatarioCompetenteByIdProtocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void aggiornaAssegnanteId(Connection connection, ProtocolloVO prt)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("updateMsgAssegnatarioCompetenteByIdProtocollo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			String sql = "UPDATE protocollo_assegnatari"
					+ " set ufficio_assegnante_id=?, carica_assegnante_id=? WHERE protocollo_id= ? AND ufficio_assegnante_id IS NULL";
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, prt.getUfficioProtocollatoreId());
			pstmt.setInt(2, prt.getCaricaProtocollatoreId());
			pstmt.setInt(3, prt.getId().intValue());
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error(
					"Error: updateMsgAssegnatarioCompetenteByIdProtocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public Collection<ProtocolloProcedimentoVO> getProcedimentiProtocollo(int protocolloId)
			throws DataException {
		ArrayList<ProtocolloProcedimentoVO> procedimenti = new ArrayList<ProtocolloProcedimentoVO>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();

			pstmt = connection.prepareStatement(PROTOCOLLO_PROCEDIMENTI);
			pstmt.setInt(1, protocolloId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ProtocolloProcedimentoVO procedimento = new ProtocolloProcedimentoVO();
				procedimento.setProtocolloId(protocolloId);
				procedimento.setProcedimentoId(rs.getInt("procedimento_id"));
				procedimento.setNumeroProcedimento(rs
						.getString("numero_procedimento"));
				procedimento.setOggetto(rs.getString("oggetto"));
				procedimento.setModificabile(true);
				procedimento.setAggiunto(true);
				procedimenti.add(procedimento);
			}

			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		} catch (Exception e) {
			logger.error("getProcedimentiProtocollo", e);
			throw new DataException("Cannot load procedimenti");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return procedimenti;
	}

	public Map<Long,ReportProtocolloView> getProtocolliPerConoscenza(Utente utente,
			String tipoUtenteUfficio) throws DataException {

		SortedMap<Long,ReportProtocolloView> protocolli = new TreeMap<Long,ReportProtocolloView>(new Comparator<Long>() {
			public int compare(Long i1, Long i2) {
				return i2.compareTo(i1);
			}
		});

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String strQuery = "SELECT p.*, a.ufficio_assegnante_id, a.ufficio_assegnatario_id, "
				+ " a.carica_assegnante_id, a.carica_assegnatario_id, a.messaggio,a.flag_competente "
				+ " FROM PROTOCOLLI p, PROTOCOLLO_ASSEGNATARI a"
				+ " WHERE registro_id=?"
				+ " AND p.protocollo_id = a.protocollo_id AND";
		
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
			while (rs.next()) {
				ReportProtocolloView protocollo = new ReportProtocolloView();
				protocollo.setProtocolloId(rs.getInt("protocollo_id"));
				protocollo.setNumeroProtocollo(rs.getInt("NUME_PROTOCOLLO"));
				protocollo.setRegistroAnnoNumero(rs
						.getLong("registro_anno_numero"));
				protocollo.setAnnoProtocollo(rs.getInt("ANNO_REGISTRAZIONE"));
				if ("M".equals(rs.getString("flag_tipo_mittente"))) {
					protocollo.setTipoMittente("M");
					protocollo.setMittente(Parametri.MULTI_MITTENTE);
				}
				protocollo.setDataProtocollo(DateUtil.formattaData(rs.getDate(
						"DATA_REGISTRAZIONE").getTime()));

				if (rs.getInt("FLAG_RISERVATO") == 1) {
					protocollo.setOggetto(Parametri.PROTOCOLLO_RISERVATO);
					protocollo.setMittente(Parametri.PROTOCOLLO_RISERVATO);

				} else {
					protocollo.setOggetto(rs.getString("TEXT_OGGETTO"));
					protocollo.setMittente(StringUtil.getStringa(rs
							.getString("DESC_COGNOME_MITTENTE"))
							+ " "
							+ StringUtil.getStringa(rs
									.getString("DESC_NOME_MITTENTE"))
							+ StringUtil.getStringa(rs
									.getString("DESC_DENOMINAZIONE_MITTENTE")));
				}
				if (rs.getString("ufficio_assegnante_id") != null)
					protocollo
							.setUfficio(rs.getString("ufficio_assegnante_id"));
				if (rs.getString("carica_assegnante_id") != null)
					protocollo.setUtenteAssegnante(rs
							.getString("carica_assegnante_id"));

				protocollo.setUfficioAssegnatario(rs
						.getString("ufficio_assegnatario_id"));
				protocollo.setUtenteAssegnatario(rs
						.getString("carica_assegnatario_id"));
				protocollo.setPdf(rs.getInt("DOCUMENTO_ID") > 0 ? true : false);
				protocollo.setDocumentoId(rs.getInt("DOCUMENTO_ID"));
				protocollo.setMessaggio(StringUtil.getStringa(rs
						.getString("messaggio")));
				if (rs.getDate("data_scarico") != null)
					protocollo.setDataScarico(DateUtil.formattaData(rs.getDate(
							"data_scarico").getTime()));
				protocolli.put(protocollo.getAnnoNumero(), protocollo);
			}
		} catch (Exception e) {
			logger.error("getProtocolliPerConoscenza", e);
			throw new DataException("Cannot load getProtocolliPerConoscenza");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return protocolli;
	}

	public boolean isUtenteAbilitatoView(Utente utente, Ufficio uff,
			int protocolloId) throws DataException {

		boolean abilitato = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = jdbcMan.getConnection();
			StringBuffer strQuery = new StringBuffer();
			strQuery.append("SELECT count(protocollo_id) FROM protocolli p WHERE aoo_id = ?");
			strQuery.append(" AND protocollo_id=").append(protocolloId);
			if (!uff.getValueObject().getTipo()
					.equals(UfficioVO.UFFICIO_CENTRALE)) {
				String ufficiUtenti = uff.getListaUfficiDiscendentiIdStr(utente);
				strQuery.append(" AND (EXISTS (SELECT * FROM ")
						.append("protocollo_assegnatari ass ")
						.append("WHERE ass.protocollo_id=p.protocollo_id ")
						.append("AND ass.ufficio_assegnatario_id IN (")
						.append(ufficiUtenti).append(")) OR")
						.append(" p.ufficio_protocollatore_id IN (")
						.append(ufficiUtenti).append(") OR")
						.append(" p.ufficio_mittente_id IN (")
						.append(ufficiUtenti).append("))");
			}

			pstmt = connection.prepareStatement(strQuery.toString());
			pstmt.setInt(1, utente.getAreaOrganizzativa().getId().intValue());
			// logger.debug(strQuery);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				abilitato = (rs.getInt(1) > 0);
			}

		} catch (Exception e) {
			logger.error("isUtenteAbilitatoView", e);
			throw new DataException("Cannot load isUtenteAbilitatoView");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return abilitato;

	}

	public int riassegnaPosta(PostaInterna protocollo, Utente utente)
			throws DataException {
		Connection connection = null;
		int statusFlag = ReturnValues.UNKNOWN;
		try {
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			Collection<AssegnatarioVO> assegnatari = protocollo.getDestinatari();
			int protocolloId = protocollo.getProtocollo().getId().intValue();
			int versione = protocollo.getProtocollo().getVersione() + 1;
			protocollo.getProtocollo().setRowUpdatedUser(
					utente.getValueObject().getUsername());
			if (assegnatari != null) {
				aggiornaProtocollo(connection, protocollo.getProtocollo());
				eliminaAssegnatariProtocollo(connection, protocolloId);
				for (Iterator<AssegnatarioVO> i = assegnatari.iterator(); i.hasNext();) {
					AssegnatarioVO assegnatario =i.next();
					assegnatario.setProtocolloId(protocolloId);
					assegnatario.setId(IdentificativiDelegate.getInstance()
							.getNextId(connection,
									NomiTabelle.PROTOCOLLO_ASSEGNATARI));

					salvaAssegnatario(connection, assegnatario, versione);
				}
			}
			connection.commit();
			statusFlag = ReturnValues.SAVED;
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.error("failed riassegnaProtocollo: ");
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
		} finally {
			jdbcMan.close(connection);
		}
		return statusFlag;
	}

	public Map<Integer,ReportProtocolloView> getProtocolliAssegnatiPerCruscotti(int id, int annoProtocolloDa,
			int annoProtocolloA, String tipo) throws DataException {
		SortedMap<Integer,ReportProtocolloView> protocolli = new TreeMap<Integer,ReportProtocolloView>(new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				return o2.compareTo(o1);
			}
		});
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String strQuery = "SELECT p.*, a.ufficio_assegnante_id, a.ufficio_assegnatario_id, "
				+ " a.carica_assegnante_id, a.carica_assegnatario_id, a.messaggio"
				+ " FROM PROTOCOLLI p, PROTOCOLLO_ASSEGNATARI a "
				+ " WHERE p.protocollo_id =a.protocollo_id AND";
		if (annoProtocolloDa > 0) {
			strQuery += " ANNO_REGISTRAZIONE>=" + annoProtocolloDa + " AND";
		}
		if (annoProtocolloA > 0) {
			strQuery += " ANNO_REGISTRAZIONE<=" + annoProtocolloA + " AND";
		}
		if (tipo.equals("U")) {
			strQuery += " ufficio_assegnatario_id ="
					+ id
					+ " AND carica_assegnatario_id IS NULL AND NOT stato_protocollo IN('C','P') AND flag_competente=1 AND check_lavorato=0";

		}
		if (tipo.equals("T"))
			strQuery += " carica_assegnatario_id ="
					+ id
					+ " AND NOT stato_protocollo IN('C','P')  AND flag_competente=1 AND check_lavorato=0";

		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ReportProtocolloView protocollo = new ReportProtocolloView();
				protocollo.setProtocolloId(rs.getInt("protocollo_id"));
				protocollo.setRegistroAnnoNumero(rs
						.getLong("registro_anno_numero"));
				protocollo.setTipoProtocollo(rs.getString("FLAG_TIPO"));
				protocollo.setNumeroProtocollo(rs.getInt("NUME_PROTOCOLLO"));
				protocollo.setAnnoProtocollo(rs.getInt("ANNO_REGISTRAZIONE"));
				protocollo.setGiorniAlert(rs.getInt("giorni_alert"));
				if ("M".equals(rs.getString("flag_tipo_mittente"))) {
					protocollo.setTipoMittente("M");
					protocollo.setMittente(Parametri.MULTI_MITTENTE);
				}
				protocollo.setDataProtocollo(DateUtil.formattaData(rs.getDate(
						"DATA_REGISTRAZIONE").getTime()));

				if (rs.getInt("FLAG_RISERVATO") == 1) {
					protocollo.setOggetto(Parametri.PROTOCOLLO_RISERVATO);
					protocollo.setMittente(Parametri.PROTOCOLLO_RISERVATO);

				} else {
					protocollo.setOggetto(rs.getString("TEXT_OGGETTO"));
					protocollo.setMittente(StringUtil.getStringa(rs
							.getString("DESC_COGNOME_MITTENTE"))
							+ " "
							+ StringUtil.getStringa(rs
									.getString("DESC_NOME_MITTENTE"))
							+ StringUtil.getStringa(rs
									.getString("DESC_DENOMINAZIONE_MITTENTE")));
				}
				protocollo.setUfficio(rs.getString("ufficio_assegnante_id"));
				protocollo.setUtenteAssegnante(rs
						.getString("carica_assegnante_id"));
				protocollo.setUfficioAssegnatario(rs
						.getString("ufficio_assegnatario_id"));
				protocollo.setUtenteAssegnatario(rs
						.getString("carica_assegnatario_id"));
				protocollo.setPdf(rs.getInt("DOCUMENTO_ID") > 0 ? true : false);
				protocollo.setDocumentoId(rs.getInt("DOCUMENTO_ID"));
				protocollo.setMessaggio(StringUtil.getStringa(rs
						.getString("messaggio")));
				if (rs.getDate("data_scarico") != null)
					protocollo.setDataScarico(DateUtil.formattaData(rs.getDate(
							"data_scarico").getTime()));
				protocolli.put(new Integer(protocollo.getProtocolloId()),
						protocollo);
			}
		} catch (Exception e) {
			logger.error("getProtocolliAssegnati", e);
			throw new DataException("Cannot load getProtocolliAssegnati");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return protocolli;

	}

	public void eliminaMittentiProtocollo(Connection connection,
			int protocolloId) throws DataException {
		PreparedStatement pstmt = null;

		final String DELETE_MITTENTI = "DELETE FROM protocollo_mittenti WHERE protocollo_id = ?";

		try {
			pstmt = connection.prepareStatement(DELETE_MITTENTI);
			pstmt.setInt(1, protocolloId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("eliminaMittentiProtocollo", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}

	}

	

	public final static String SELECT_DOCUMENTO_DEFAULT = "SELECT tipo_documento_id FROM tipi_documento WHERE flag_default='1' AND aoo_id=?";

	public final static String SELECT_ULTIMO_PROTOCOLLO = "SELECT MAX(nume_protocollo) FROM protocolli WHERE registro_id = ? AND anno_registrazione = ?";
	
	public final static String SELECT_PROGRESSIVO_NOTIFICA = "SELECT progressivo FROM notifiche_fattura_elettronica WHERE anno = ? AND aoo_id=?";
	
	public final static String INSERT_PROGRESSIVO_NOTIFICA = "INSERT INTO notifiche_fattura_elettronica(nfe_id, aoo_id, progressivo, anno)VALUES (?, ?, ?, ?)";

	public final static String UPDATE_PROGRESSIVO_NOTIFICA = "UPDATE notifiche_fattura_elettronica SET progressivo=? WHERE aoo_id=? AND anno=?";
	
	public final static String SELECT_TIPO_PROTOCOLLO = "SELECT flag_tipo FROM protocolli WHERE protocollo_id = ?";

	public final static String SELECT_REGISTRO_BY_PROTOCOLLO_ID = "SELECT r.* FROM registri r LEFT JOIN protocolli p ON(r.registro_id=p.registro_id) WHERE protocollo_id = ? ";

	private final static String INSERT_PROTOCOLLO_DESTINATARI = "INSERT INTO protocollo_destinatari"
			+ " (destinatario_id, flag_tipo_destinatario, indirizzo, email, destinatario, "
			+ " citta, data_spedizione, flag_conoscenza, protocollo_id, data_effettiva_spedizione,versione, mezzo_spedizione_id"
			+ ", titolo_id, mezzo_spedizione,note,codice_postale,flag_presso,flag_pec,prezzo_spedizione)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?"
			+ ", ?"
			+ ", ?,?,?,?,?,?)";

	// salva SEGNATURE
	public final static String INSERT_SEGNATURE = "INSERT INTO segnature"
			+ " (segnature_id, protocollo_id, tipo_protocollo, text_segnatura, row_created_user) VALUES (?, ?, ?, ?, ?)";

	public final static String SELECT_SEGNATURE = "SELECT segnature_id, protocollo_id,text_segnatura FROM segnature where protocollo_id=?;";

	// Annullamento protocollo
	public final static String ANNULLA_PROTOCOLLO = "UPDATE PROTOCOLLI SET stato_protocollo=?, data_annullamento=?, "
			+ "text_nota_annullamento=?, text_provvedimento_annullament=?, row_created_user=?, row_created_time=?"
			+ " WHERE protocollo_id=? AND versione=?";

	// Selezione dei destinatari di un protocollo in uscita
	public final static String PROTOCOLLO_DESTINATARI = "SELECT PD.*, S.DESC_SPEDIZIONE FROM PROTOCOLLO_DESTINATARI PD left outer join spedizioni s on (PD.mezzo_spedizione_id=s.spedizioni_id)"
			+ " WHERE pd.protocollo_id=?  ORDER BY DESTINATARIO";

	// Selezione dei destinatari dei protocolli per destianatario
	public final static String DESTINATARI_PROTOCOLLI = "SELECT DISTINCT DESTINATARIO FROM PROTOCOLLO_DESTINATARI"
			+ " WHERE DESTINATARIO LIKE ?  ORDER BY DESTINATARIO";

	// Selezione dei mittenti inputati nei protocolli per nome mittente
	public final static String MITTENTI_PROTOCOLLI = "SELECT DISTINCT flag_tipo_mittente,desc_denominazione_mittente,"
			+ "desc_cognome_mittente, desc_nome_mittente FROM PROTOCOLLI "
			+ " WHERE lower(desc_denominazione_mittente) LIKE ? OR lower(desc_cognome_mittente) LIKE ?"
			+ " ORDER BY desc_denominazione_mittente,desc_cognome_mittente ";

	public final static String SELECT_MAX_NUM_PROTOCOLLO = "SELECT MAX(NUME_PROTOCOLLO) FROM PROTOCOLLI "
			+ "WHERE ANNO_REGISTRAZIONE=? AND registro_id=?)";

	public final static String INSERT_PROTOCOLLO = "INSERT INTO protocolli ("
			+ " protocollo_id, anno_registrazione, nume_protocollo, data_registrazione,"
			+ " flag_tipo_mittente, text_oggetto, flag_tipo, data_documento,"
			+ " tipo_documento_id, aoo_id, registro_id ,utente_protocollatore_id, carica_protocollatore_id,"
			+ " ufficio_protocollatore_id, desc_denominazione_mittente,"
			+ " desc_cognome_mittente, desc_nome_mittente, indi_mittente, indi_cap_mittente,"
			+ " indi_localita_mittente, indi_provincia_mittente, annotazione_chiave,"
			+ " annotazione_posizione, annotazione_descrizione, titolario_id, row_created_user,"
			+ " flag_riservato, ufficio_mittente_id, utente_mittente_id,"
			+ " nume_protocollo_mittente, data_ricezione, documento_id,"
			+ " stato_protocollo, flag_mozione,num_prot_emergenza,data_annullamento, text_provvedimento_annullament,"
			+ " text_nota_annullamento,data_effettiva_registrazione, row_created_time, versione, registro_anno_numero,intervallo_emergenza,giorni_alert,data_protocollo_mittente,numero_email, data_scadenza, text_estremi_autorizzazione,flag_fattura_elettronica, flag_repertorio, flag_anomalia) "
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?)";

	public final static String INSERT_STORIA_PROTOCOLLO = "INSERT INTO storia_protocolli ("
			+ " protocollo_id, anno_registrazione, nume_protocollo, data_registrazione,"
			+ " flag_tipo_mittente, text_oggetto, flag_tipo, data_documento,"
			+ " tipo_documento_id, aoo_id, registro_id, utente_protocollatore_id, carica_protocollatore_id,"
			+ " ufficio_protocollatore_id, desc_denominazione_mittente,"
			+ " desc_cognome_mittente, desc_nome_mittente, indi_mittente, indi_cap_mittente,"
			+ " indi_localita_mittente, indi_provincia_mittente, annotazione_chiave,"
			+ " annotazione_posizione, annotazione_descrizione, titolario_id, row_created_user,"
			+ " flag_riservato, ufficio_mittente_id, utente_mittente_id,"
			+ " nume_protocollo_mittente, data_ricezione, documento_id,"
			+ " stato_protocollo, flag_mozione,num_prot_emergenza,data_annullamento, text_provvedimento_annullament,"
			+ " text_nota_annullamento,data_effettiva_registrazione, row_created_time, versione, registro_anno_numero,flag_fattura_elettronica, flag_repertorio) "
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private final static String UPDATE_PROTOCOLLO = "UPDATE protocolli SET"
			+ " flag_tipo_mittente = ?, text_oggetto = ?, "
			+ " desc_denominazione_mittente = ?, desc_cognome_mittente = ?,"
			+ " desc_nome_mittente = ?, indi_mittente = ?, indi_cap_mittente = ?,"
			+ " indi_localita_mittente = ?, indi_provincia_mittente = ?,"
			+ " annotazione_chiave = ?, annotazione_posizione = ?, annotazione_descrizione = ?,"
			+ " tipo_documento_id=?,titolario_id = ?,row_created_user = ?, row_created_time = ?,"
			+ " ufficio_mittente_id = ?, utente_mittente_id = ?, nume_protocollo_mittente = ?,"
			+ " data_ricezione = ?, data_documento = ?, documento_id = ?, stato_protocollo = ?, data_scarico=?,"
			+ " num_prot_emergenza = ?, text_estremi_autorizzazione=?, flag_tipo=?, data_registrazione=?,data_protocollo_mittente=?,data_scadenza=?,flag_riservato=?, flag_fattura_elettronica=?, flag_repertorio=?, flag_anomalia=? "
			+ " WHERE protocollo_id = ? AND versione = ?";

	public final static String SELECT_PROTOCOLLO = "SELECT * FROM PROTOCOLLI "
			+ "WHERE ANNO_REGISTRAZIONE=? AND registro_id=? AND protocollo_id=?";

	public final static String SELECT_PROTOCOLLO_BY_NUMERO = "SELECT protocollo_id FROM PROTOCOLLI "
			+ "WHERE ANNO_REGISTRAZIONE=? AND registro_id=? AND NUME_PROTOCOLLO=?";

	public final static String SELECT_PROTOCOLLO_BY_AOO_ANNO_NUMERO = "SELECT protocollo_id FROM PROTOCOLLI "
			+ "WHERE ANNO_REGISTRAZIONE=? AND aoo_id=? AND NUME_PROTOCOLLO=?";

	public final static String SELECT_PROTOCOLLO_BY_ID = "SELECT * FROM PROTOCOLLI WHERE protocollo_id =?";

	public final static String INSERT_ALLACCI = "INSERT INTO protocollo_allacci "
			+ "(allaccio_id, protocollo_id, flag_principale, protocollo_allacciato_id, row_created_user, versione)"
			+ " VALUES (?, ?, ?, ?, ?, ?)";

	public final static String PROTOCOLLO_ALLEGATI = "SELECT documento_id FROM protocollo_allegati WHERE protocollo_id = ?";

	public final static String PROTOCOLLO_ALLACCI = "SELECT a.*, p.nume_protocollo, p.anno_registrazione, p.flag_tipo"
			+ " FROM protocolli p, protocollo_allacci a"
			+ " WHERE a.protocollo_allacciato_id = p.protocollo_id and a.protocollo_id = ?";

	private final static String UPDATE_DOCUMENTO_ID = "UPDATE protocolli SET documento_id=? WHERE protocollo_id=?";

	public final static String INSERT_PROTOCOLLO_ALLEGATI = "INSERT INTO protocollo_allegati"
			+ " (allegato_id, protocollo_id, documento_id, versione) VALUES (?, ?, ?, ?)";

	public final static String INSERT_PROTOCOLLO_ALLACCI = "INSERT INTO protocollo_allacci"
			+ " (allaccio_id, protocollo_id, flag_principale, protocollo_allacciato_id, versione) VALUES (?, ?, ?, ?,(select versione from protocolli where protocollo_id=?))";

	private final static String ASSEGNATARIO_PER_COMPETENZA = "SELECT * FROM protocollo_assegnatari"
			+ " WHERE flag_competente=1 AND protocollo_id = ?";

	private final static String PROTOCOLLO_ASSEGNATARI = "SELECT * FROM protocollo_assegnatari"
			+ " WHERE protocollo_id = ?";

	private final static String INSERT_PROTOCOLLO_ASSEGNATARI = "INSERT INTO protocollo_assegnatari"
			+ " (assegnatario_id, protocollo_id, data_assegnazione, data_operazione, "
			+ "flag_competente, ufficio_assegnatario_id,carica_assegnatario_id, utente_assegnatario_id, ufficio_assegnante_id, carica_assegnante_id, "
			+ "stat_assegnazione, messaggio,check_presa_visione, versione, check_lavorato,flag_titolare_procedimento) VALUES (?, ?, ? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	public final static String SET_PROCEDIMENTO_LAVORATO = "UPDATE protocollo_assegnatari SET check_lavorato=1 WHERE assegnatario_id=?";

	public final static String UPDATE_STATO_PROTOCOLLO = "UPDATE protocolli SET stato_protocollo='L',row_created_time=?,row_created_user=? WHERE protocollo_id=?";

	private final static String INSERT_CHECK_PRESA_VISIONE = "INSERT INTO check_posta_interna(check_id, protocollo_id, data_operazione,"
			+ " ufficio_assegnante_id, ufficio_assegnatario_id, carica_assegnatario_id, carica_assegnante_id,flag_competente) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

	private final static String UPDATE_MSG_ASSEGNATARIO_COMPETENTE = "UPDATE protocollo_assegnatari"
			+ " set messaggio=? WHERE protocollo_id= ? AND flag_competente=1";

	public final static String UPDATE_SCARICO = "UPDATE protocolli SET stato_protocollo=?, data_scarico=?, row_created_time=?, row_created_user=?"
			+ " WHERE protocollo_id=? AND versione=?";

	public final static String UPDATE_CHECK_POSTA_INTERNA = "UPDATE check_posta_interna SET check_presa_visione=1 WHERE check_id=?";

	public final static String UPDATE_CHECK_POSTA_INTERNA_UTENTE = "UPDATE check_posta_interna SET check_presa_visione=1 WHERE carica_assegnante_id=? AND check_presa_visione=0";

	public final static String SELECT_MITTENTI_INGRESSO_BY_ID = "SELECT descrizione FROM protocollo_mittenti WHERE protocollo_id= ?";

	public final static String SELECT_LISTA_PROTOCOLLI = "SELECT "
			+ "p.protocollo_id,p.anno_registrazione,p.nume_protocollo, p.registro_id,"
			+ "p.flag_tipo,p.flag_riservato,p.text_oggetto,p.data_registrazione,"
			+ "p.documento_id,p.stato_protocollo,p.flag_tipo_mittente,"
			+ "p.desc_cognome_mittente,p.desc_nome_mittente,p.titolario_id, p.versione,p.registro_anno_numero,"
			+ "p.desc_denominazione_mittente,p.num_prot_emergenza,ut.nome as nome_assegnatario, c.denominazione as carica_assegnatario,"
			+ "uf.descrizione as ufficio_assegnatario,"
			+ "ut.cognome as cognome_assegnatario,"
			+ "a.flag_competente,d.destinatario,d.flag_conoscenza "
			+ "FROM protocolli p"
			+ " LEFT JOIN protocollo_assegnatari a ON a.protocollo_id=p.protocollo_id"
			+ " LEFT JOIN uffici uf ON a.ufficio_assegnatario_id=uf.ufficio_id"
			+ " LEFT JOIN cariche c ON a.carica_assegnatario_id=c.carica_id"
			+ " LEFT JOIN utenti ut ON c.utente_id=ut.utente_id"
			+ " LEFT JOIN protocollo_destinatari d ON d.protocollo_id=p.protocollo_id "
			+ " LEFT JOIN protocollo_mittenti m ON m.protocollo_id=p.protocollo_id"
			+ " WHERE p.aoo_id=? ";

	public final static String SELECT_LISTA_PROTOCOLLI_ADD_ALLACCI = "SELECT "
			+ "p.protocollo_id AS protocollo_id,p.anno_registrazione AS anno_registrazione,p.nume_protocollo AS nume_protocollo, "
			+ "p.registro_id AS registro_id,p.text_estremi_autorizzazione AS text_estremi_autorizzazione, p.text_provvedimento_annullament AS text_provvedimento_annullament,"
			+ "p.flag_tipo AS flag_tipo,p.flag_riservato AS flag_riservato,p.text_oggetto AS text_oggetto,"
			+ "p.data_registrazione AS data_registrazione, p.data_protocollo_mittente AS data_protocollo_mittente, p.nume_protocollo_mittente AS numero_protocollo_mittente,"
			+ "p.documento_id AS documento_id, doc.impronta AS impronta, p.stato_protocollo AS stato_protocollo,p.flag_tipo_mittente AS flag_tipo_mittente,"
			+ "p.desc_cognome_mittente AS desc_cognome_mittente,p.desc_nome_mittente AS desc_nome_mittente,"
			+ "p.titolario_id AS titolario_id, p.versione AS versione,p.registro_anno_numero AS registro_anno_numero,"
			+ "p.desc_denominazione_mittente AS desc_denominazione_mittente,p.num_prot_emergenza AS num_prot_emergenza,ut.nome as nome_assegnatario,"
			+ "uf.descrizione as ufficio_assegnatario,"
			+ "ut.cognome as cognome_assegnatario, c.denominazione as carica_assegnatario,"
			+ "a.flag_competente,d.destinatario,d.flag_conoscenza,"
			+ "p_all.nume_protocollo AS allaccio_numero,p_all.flag_tipo AS allaccio_tipo,p_all.anno_registrazione AS allaccio_anno,p_all.data_registrazione AS allaccio_data"
			+ " FROM protocolli p"
			+ " LEFT JOIN documenti doc ON p.documento_id=doc.documento_id"
			+ " LEFT JOIN protocollo_assegnatari a ON a.protocollo_id=p.protocollo_id"
			+ " LEFT JOIN protocollo_allacci allacci ON allacci.protocollo_id=p.protocollo_id"
			+ " LEFT JOIN protocolli p_all ON allacci.protocollo_allacciato_id=p_all.protocollo_id"
			+ " LEFT JOIN uffici uf ON a.ufficio_assegnatario_id=uf.ufficio_id"
			+ " LEFT JOIN cariche c ON a.carica_assegnatario_id=c.carica_id"
			+ " LEFT JOIN utenti ut ON c.utente_id=ut.utente_id"
			+ " LEFT JOIN protocollo_destinatari d ON d.protocollo_id=p.protocollo_id "
			+ "WHERE p.registro_id IN(?,?)";

	public final static String SELECT_ALLACCI = "SELECT DISTINCT "
			+ "p.protocollo_id,p.anno_registrazione,p.nume_protocollo,"
			+ "p.flag_tipo,p.flag_riservato,p.text_oggetto,p.data_registrazione,"
			+ "p.flag_tipo_mittente,p.desc_cognome_mittente,p.desc_nome_mittente,"
			+ "p.desc_denominazione_mittente "
			+ "FROM protocolli p"
			+ " LEFT JOIN protocollo_assegnatari a ON a.protocollo_id=p.protocollo_id"
			+ " LEFT JOIN uffici uf ON a.ufficio_assegnatario_id=uf.ufficio_id"
			+ " LEFT JOIN cariche c ON a.carica_assegnatario_id=c.carica_id"
			+ " LEFT JOIN utenti ut ON c.utente_id=ut.utente_id"
			+ " LEFT JOIN protocollo_destinatari d ON d.protocollo_id=p.protocollo_id "
			+ "WHERE p.registro_id IN (?,?) AND p.protocollo_id!=? ";

	public final static String SELECT_ALLACCI_EDITOR = "SELECT DISTINCT "
			+ "p.protocollo_id,p.nume_protocollo,"
			+ "p.flag_tipo,p.data_registrazione "
			+ "FROM protocolli p"
			+ " LEFT JOIN protocollo_assegnatari a ON a.protocollo_id=p.protocollo_id"
			+ " LEFT JOIN uffici uf ON a.ufficio_assegnatario_id=uf.ufficio_id"
			+ " LEFT JOIN cariche c ON a.carica_assegnatario_id=c.carica_id"
			+ " LEFT JOIN utenti ut ON c.utente_id=ut.utente_id"
			+ " LEFT JOIN protocollo_destinatari d ON d.protocollo_id=p.protocollo_id "
			+ "WHERE p.registro_id =? AND flag_tipo='I' ";

	public final static String DELETE_DESTINATARI = "DELETE FROM protocollo_destinatari WHERE protocollo_id = ?";

	public final static String DELETE_DOCUMENTO_PRINCIPALE = "UPDATE protocolli SET documento_id=null WHERE protocollo_id = ?";

	private final static String PROTOCOLLO_PROCEDIMENTI = "SELECT p.*, r.anno, r.numero,r.oggetto, r.numero_procedimento FROM protocollo_procedimenti p, procedimenti r "
			+ "WHERE p.procedimento_id =r.procedimento_id and p.protocollo_id = ?";


}