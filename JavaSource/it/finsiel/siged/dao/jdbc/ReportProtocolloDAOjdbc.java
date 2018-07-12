package it.finsiel.siged.dao.jdbc;

import it.compit.fenice.mvc.presentation.helper.ReportCheckPostaInternaView;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.integration.ReportProtocolloDAO;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.presentation.helper.RubricaView;
import it.finsiel.siged.mvc.vo.lookup.DestinatarioAllaccioVO;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.StringUtil;
import it.flosslab.mvc.business.MittentiDelegate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class ReportProtocolloDAOjdbc implements ReportProtocolloDAO {

	static Logger logger = Logger.getLogger(ReportProtocolloDAOjdbc.class
			.getName());

	private JDBCManager jdbcMan = new JDBCManager();

	public Collection<ReportProtocolloView> getRegistroReport(Utente utente, String tipoProtocollo,
			Date dataInizio, Date dataFine, int ufficioId) throws DataException {
		ArrayList<ReportProtocolloView> protocolli = new ArrayList<ReportProtocolloView>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int allaccio_num = 0;
		String destinatario = "";
		String strQuery = ProtocolloDAOjdbc.SELECT_LISTA_PROTOCOLLI_ADD_ALLACCI
				+ " AND p.DATA_REGISTRAZIONE BETWEEN ? AND ? AND NOT p.FLAG_TIPO  ='R' ";
		if (tipoProtocollo != null && !tipoProtocollo.trim().equals("")) {

			if (FLAG_TIPO_PROTOCOLLO_USCITA.equals(tipoProtocollo.trim())) {
				strQuery += " AND p.FLAG_TIPO = '"
						+ FLAG_TIPO_PROTOCOLLO_USCITA + "' ";
			} else if (FLAG_TIPO_PROTOCOLLO_INGRESSO.equals(tipoProtocollo
					.trim())) {
				strQuery += " AND p.FLAG_TIPO = '"
						+ FLAG_TIPO_PROTOCOLLO_INGRESSO + "'";
			} else {
				strQuery += " AND p.FLAG_TIPO = 'P' ";
			}
		} else {
			strQuery += " AND p.FLAG_TIPO IN ('I','U')";
		}
		if (ufficioId != 0) {
			strQuery += " AND p.ufficio_protocollatore_id = " + ufficioId;
		}
		strQuery = strQuery
				+ " ORDER BY p.ANNO_REGISTRAZIONE asc, p.NUME_PROTOCOLLO asc";
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, utente.getRegistroInUso());
			pstmt.setInt(2, utente.getRegistroPostaInterna());
			pstmt.setDate(3, new java.sql.Date(dataInizio.getTime()));
			pstmt.setTimestamp(4, new java.sql.Timestamp(dataFine.getTime()
					+ Constants.GIORNO_MILLISECONDS - 1));
			rs = pstmt.executeQuery();
			ReportProtocolloView protocollo = null;
			StringBuffer dest_ass = null;
			StringBuffer allaccio = null;
			while (rs.next()) {
				if (protocollo == null
						|| protocollo.getProtocolloId() != rs
								.getInt("protocollo_id")) {
					if (protocollo != null) {
						protocollo.setDestinatario(dest_ass.toString());
						protocollo.setAllaccio(allaccio.toString());
					}
					dest_ass = new StringBuffer();
					allaccio = new StringBuffer();
					protocollo = new ReportProtocolloView();
					protocollo.setProtocolloId(rs.getInt("protocollo_id"));
					protocollo.setAnnoProtocollo(rs
							.getInt("anno_registrazione"));
					protocollo.setNumeroProtocollo(rs.getInt("nume_protocollo"));
					protocollo.setTipoProtocollo(rs.getString("flag_tipo"));
					if(rs.getDate("data_protocollo_mittente")!=null){
						protocollo.setDataProtocolloMittente(DateUtil.formattaData(rs.getDate("data_protocollo_mittente").getTime()));
						protocollo.setNumeroProtocolloMittente(rs.getString("numero_protocollo_mittente"));
					}
					if (rs.getBoolean("flag_riservato")) {
						protocollo.setOggetto(Parametri.PROTOCOLLO_RISERVATO);
						protocollo.setMittente(Parametri.PROTOCOLLO_RISERVATO);

					} else {
						protocollo.setOggetto(rs.getString("text_oggetto")
								+ "\r\n");
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
							List<SoggettoVO> mittenti = MittentiDelegate
									.getInstance().getMittenti(
											rs.getInt("protocollo_id"));
							for (SoggettoVO mitt : mittenti) {
								mittente.append(mitt.getDescrizione() + "\r\n");
							}
						} else {
							mittente.append(rs
									.getString("desc_denominazione_mittente"));
						}
						protocollo.setMittente(mittente.toString());
					}
					protocollo.setDataProtocollo(DateUtil.formattaData(rs
							.getDate("data_registrazione").getTime()));

					protocollo.setPdf(rs.getInt("documento_id") > 0);
					protocollo.setImpronta(rs.getString("impronta"));
					protocollo.setStatoProtocollo(rs
							.getString("stato_protocollo"));
					protocolli.add(protocollo);
				}
				if (!rs.getBoolean("flag_riservato")) {
					if (!"U".equals(protocollo.getTipoProtocollo())) {
							dest_ass.append(rs
									.getString("ufficio_assegnatario"));
							if (rs.getString("cognome_assegnatario") != null) {
								dest_ass.append(" / ").append(
										rs.getString("cognome_assegnatario"));
								if (rs.getString("nome_assegnatario") != null) {
									dest_ass.append(' ').append(
											rs.getString("nome_assegnatario"));
								}
							}
							if (rs.getBoolean("flag_competente")) {
								dest_ass.append(" *");
							}
						
					} else {
						if (destinatario != null) {
								dest_ass.append(rs.getString("destinatario"));
								if (rs.getInt("flag_conoscenza") == 0) {
									dest_ass.append(" *");
								}
						}
					}
					dest_ass.append("\r\n");
				} else {
					dest_ass = new StringBuffer(Parametri.PROTOCOLLO_RISERVATO);
				}
				
				if (allaccio_num != rs.getInt("allaccio_numero")) {
					allaccio_num = rs.getInt("allaccio_numero");
					if (rs.getInt("allaccio_numero") != 0) {
						allaccio.append("" + rs.getInt("allaccio_numero"));
						if (rs.getInt("allaccio_anno") != 0)
							allaccio.append("/" + rs.getInt("allaccio_anno"));
						if (rs.getString("allaccio_tipo") != null)
							allaccio.append("(" + rs.getString("allaccio_tipo")
									+ ")");
						if (rs.getString("allaccio_data") != null)
							allaccio.append("-"
									+ DateUtil.formattaData(rs.getDate(
											"allaccio_data").getTime()) + ")");
						allaccio.append("\r\n");

					}
				}
			}
			if (protocollo != null) {
				protocollo.setDestinatario(dest_ass.toString());
				protocollo.setAllaccio(allaccio.toString());
			}

		} catch (Exception e) {
			logger.error("getRegistro", e);
			throw new DataException("Cannot load getRegistro");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return protocolli;

	}
	
	
	
	public Collection<ReportProtocolloView> getProtocolliModificatiReport(Utente utente, String tipoProtocollo,
			Date dataInizio, Date dataFine, int ufficioId) throws DataException {
		ArrayList<ReportProtocolloView> protocolli = new ArrayList<ReportProtocolloView>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int allaccio_num = 0;
		String destinatario = "";
		String strQuery = ProtocolloDAOjdbc.SELECT_LISTA_PROTOCOLLI_ADD_ALLACCI
				+ " AND p.row_created_time BETWEEN ? AND ? AND NOT p.flag_tipo ='R' AND (NOT p.text_estremi_autorizzazione IS NULL OR NOT p.text_provvedimento_annullament IS NULL) ";
		if (tipoProtocollo != null && !tipoProtocollo.trim().equals("")) {
			if (FLAG_TIPO_PROTOCOLLO_USCITA.equals(tipoProtocollo.trim())) {
				strQuery += " AND p.FLAG_TIPO = '" + FLAG_TIPO_PROTOCOLLO_USCITA + "' ";
			} else if (FLAG_TIPO_PROTOCOLLO_INGRESSO.equals(tipoProtocollo.trim())) {
				strQuery += " AND p.FLAG_TIPO = '"
						+ FLAG_TIPO_PROTOCOLLO_INGRESSO + "'";
			} else {
				strQuery += " AND p.FLAG_TIPO = 'P' ";
			}
		} else {
			strQuery += " AND p.FLAG_TIPO IN ('I','U')";
		}
		if (ufficioId != 0) {
			strQuery += " AND p.ufficio_protocollatore_id = " + ufficioId;
		}
		strQuery = strQuery
				+ " ORDER BY p.ANNO_REGISTRAZIONE asc, p.NUME_PROTOCOLLO asc";
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, utente.getRegistroInUso());
			pstmt.setInt(2, utente.getRegistroPostaInterna());
			pstmt.setDate(3, new java.sql.Date(dataInizio.getTime()));
			pstmt.setTimestamp(4, new java.sql.Timestamp(dataFine.getTime()+ Constants.GIORNO_MILLISECONDS - 1));
			rs = pstmt.executeQuery();
			ReportProtocolloView protocollo = null;
			StringBuffer dest_ass = null;
			StringBuffer allaccio = null;
			while (rs.next()) {
				if (protocollo == null|| protocollo.getProtocolloId() != rs.getInt("protocollo_id")) {
					if (protocollo != null) {
						protocollo.setDestinatario(dest_ass.toString());
						protocollo.setAllaccio(allaccio.toString());
					}
					dest_ass = new StringBuffer();
					allaccio = new StringBuffer();
					protocollo = new ReportProtocolloView();
					protocollo.setProtocolloId(rs.getInt("protocollo_id"));
					protocollo.setAnnoProtocollo(rs.getInt("anno_registrazione"));
					protocollo.setNumeroProtocollo(rs.getInt("nume_protocollo"));
					protocollo.setTipoProtocollo(rs.getString("flag_tipo"));
					protocollo.setDataProtocolloMittente(rs.getString("data_protocollo_mittente"));
					protocollo.setNumeroProtocolloMittente(rs.getString("numero_protocollo_mittente"));
					
					if(rs.getString("text_estremi_autorizzazione")!=null){
						protocollo.setEstremi(rs.getString("text_estremi_autorizzazione"));
						protocollo.setStatoConservazione("Modificato");
					}
					else{
						protocollo.setEstremi(rs.getString("text_provvedimento_annullament"));
						protocollo.setStatoConservazione("Annullato");
					}
					if (rs.getBoolean("flag_riservato")) {
						protocollo.setOggetto(Parametri.PROTOCOLLO_RISERVATO);
						protocollo.setMittente(Parametri.PROTOCOLLO_RISERVATO);
					} else {
						protocollo.setOggetto(rs.getString("text_oggetto")
								+ "\r\n");
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
							List<SoggettoVO> mittenti = MittentiDelegate
									.getInstance().getMittenti(
											rs.getInt("protocollo_id"));
							for (SoggettoVO mitt : mittenti) {
								mittente.append(mitt.getDescrizione() + "\r\n");
							}
						} else {
							mittente.append(rs
									.getString("desc_denominazione_mittente"));
						}
						protocollo.setMittente(mittente.toString());
					}
					protocollo.setDataProtocollo(DateUtil.formattaData(rs
							.getDate("data_registrazione").getTime()));

					protocollo.setPdf(rs.getInt("documento_id") > 0);
					protocollo.setImpronta(rs.getString("impronta"));
					protocollo.setStatoProtocollo(rs
							.getString("stato_protocollo"));
					protocolli.add(protocollo);
				}
				if (!rs.getBoolean("flag_riservato")) {
					if (!"U".equals(protocollo.getTipoProtocollo())) {
							dest_ass.append(rs
									.getString("ufficio_assegnatario"));
							if (rs.getString("cognome_assegnatario") != null) {
								dest_ass.append(" / ").append(
										rs.getString("cognome_assegnatario"));
								if (rs.getString("nome_assegnatario") != null) {
									dest_ass.append(' ').append(
											rs.getString("nome_assegnatario"));
								}
							}
							if (rs.getBoolean("flag_competente")) {
								dest_ass.append(" *");
							}
						
					} else {
						if (destinatario != null) {
								dest_ass.append(rs.getString("destinatario"));
								if (rs.getInt("flag_conoscenza") == 0) {
									dest_ass.append(" *");
								}
						}
					}
					dest_ass.append("\r\n");
				} else {
					dest_ass = new StringBuffer(Parametri.PROTOCOLLO_RISERVATO);
				}
				
				if (allaccio_num != rs.getInt("allaccio_numero")) {
					allaccio_num = rs.getInt("allaccio_numero");
					if (rs.getInt("allaccio_numero") != 0) {
						allaccio.append("" + rs.getInt("allaccio_numero"));
						if (rs.getInt("allaccio_anno") != 0)
							allaccio.append("/" + rs.getInt("allaccio_anno"));
						if (rs.getString("allaccio_tipo") != null)
							allaccio.append("(" + rs.getString("allaccio_tipo")
									+ ")");
						if (rs.getString("allaccio_data") != null)
							allaccio.append("-"
									+ DateUtil.formattaData(rs.getDate(
											"allaccio_data").getTime()) + ")");
						allaccio.append("\r\n");

					}
				}
			}
			if (protocollo != null) {
				protocollo.setDestinatario(dest_ass.toString());
				protocollo.setAllaccio(allaccio.toString());
			}

		} catch (Exception e) {
			logger.error("getRegistro", e);
			throw new DataException("Cannot load getRegistro");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return protocolli;

	}



	public int countStampaRegistroReport(Utente utente, String tipoProtocollo,
			Date dataInizio, Date dataFine, int ufficioId) throws DataException {
		int total = 0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String strQuery = "SELECT COUNT(protocollo_id) FROM PROTOCOLLI WHERE registro_id IN (?,?) AND DATA_REGISTRAZIONE BETWEEN ? AND ? AND NOT FLAG_TIPO ='R' ";
		if (tipoProtocollo != null && !tipoProtocollo.trim().equals("")) {
			if (FLAG_TIPO_PROTOCOLLO_USCITA.equals(tipoProtocollo.trim())) {
				strQuery += " AND FLAG_TIPO = '" + FLAG_TIPO_PROTOCOLLO_USCITA+ "' ";
			}
			else if (FLAG_TIPO_PROTOCOLLO_INGRESSO.equals(tipoProtocollo.trim())) {
				strQuery += " AND FLAG_TIPO = '"+ FLAG_TIPO_PROTOCOLLO_INGRESSO + "' ";
			} else {
				strQuery += " AND FLAG_TIPO = 'P'";
			}
		}else {
			strQuery += " AND FLAG_TIPO IN ('I','U')";
		}
		if (ufficioId != 0) {
			strQuery += " AND ufficio_protocollatore_id = " + ufficioId;
		}
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, utente.getRegistroInUso());
			pstmt.setInt(2, utente.getRegistroPostaInterna());
			pstmt.setDate(3, new java.sql.Date(dataInizio.getTime()));
			pstmt.setTimestamp(4, new java.sql.Timestamp(dataFine.getTime()
					+ Constants.GIORNO_MILLISECONDS - 1));
			rs = pstmt.executeQuery();
			rs.next();
			total = rs.getInt(1);
		} catch (Exception e) {
			logger.error("getCountRegistro", e);
			throw new DataException("Cannot load getRegistro");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return total;
	}
	

	public int countProtocolliAnnullatiReport(Utente utente, String tipoProtocollo,
			Date dataInizio, Date dataFine, int ufficioId) throws DataException {
		int total = 0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String strQuery = "SELECT COUNT(protocollo_id) FROM PROTOCOLLI WHERE registro_id IN (?,?) AND row_created_time BETWEEN ? AND ? AND NOT flag_tipo ='R' AND NOT text_provvedimento_annullament IS NULL";
		if (tipoProtocollo != null && !tipoProtocollo.trim().equals("")) {
			if (FLAG_TIPO_PROTOCOLLO_USCITA.equals(tipoProtocollo.trim())) {
				strQuery += " AND FLAG_TIPO = '" + FLAG_TIPO_PROTOCOLLO_USCITA			+ "' ";
			}
			else if (FLAG_TIPO_PROTOCOLLO_INGRESSO.equals(tipoProtocollo.trim())) {
				strQuery += " AND FLAG_TIPO = '"+ FLAG_TIPO_PROTOCOLLO_INGRESSO + "' ";
			} else {
				strQuery += " AND FLAG_TIPO = 'P'";
			}
		}else {
			strQuery += " AND FLAG_TIPO IN ('I','U')";
		}
		if (ufficioId != 0) {
			strQuery += " AND ufficio_protocollatore_id = " + ufficioId;
		}
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, utente.getRegistroInUso());
			pstmt.setInt(2, utente.getRegistroPostaInterna());
			pstmt.setDate(3, new java.sql.Date(dataInizio.getTime()));
			pstmt.setTimestamp(4, new java.sql.Timestamp(dataFine.getTime()
					+ Constants.GIORNO_MILLISECONDS - 1));
			rs = pstmt.executeQuery();
			rs.next();
			total = rs.getInt(1);
		} catch (Exception e) {
			logger.error("getCountRegistro", e);
			throw new DataException("Cannot load getRegistro");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return total;
	}
	

	public int countProtocolliModificatiReport(Utente utente, String tipoProtocollo,
			Date dataInizio, Date dataFine, int ufficioId) throws DataException {
		int total = 0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String strQuery = "SELECT COUNT(protocollo_id) FROM PROTOCOLLI WHERE registro_id IN (?,?) AND row_created_time BETWEEN ? AND ? AND NOT flag_tipo ='R' AND (NOT text_estremi_autorizzazione IS NULL OR NOT text_provvedimento_annullament IS NULL)";
		if (tipoProtocollo != null && !tipoProtocollo.trim().equals("")) {
			if (FLAG_TIPO_PROTOCOLLO_USCITA.equals(tipoProtocollo.trim())) {
				strQuery += " AND FLAG_TIPO = '" + FLAG_TIPO_PROTOCOLLO_USCITA + "' ";
			}
			else if (FLAG_TIPO_PROTOCOLLO_INGRESSO.equals(tipoProtocollo.trim())) {
				strQuery += " AND FLAG_TIPO = '"+ FLAG_TIPO_PROTOCOLLO_INGRESSO + "' ";
			} else {
				strQuery += " AND FLAG_TIPO = 'P'";
			}
		} else {
			strQuery += " AND FLAG_TIPO IN ('I','U')";
		}
		if (ufficioId != 0) {
			strQuery += " AND ufficio_protocollatore_id = " + ufficioId;
		}
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, utente.getRegistroInUso());
			pstmt.setInt(2, utente.getRegistroPostaInterna());
			pstmt.setDate(3, new java.sql.Date(dataInizio.getTime()));
			pstmt.setTimestamp(4, new java.sql.Timestamp(dataFine.getTime()
					+ Constants.GIORNO_MILLISECONDS - 1));
			rs = pstmt.executeQuery();
			rs.next();
			total = rs.getInt(1);
		} catch (Exception e) {
			logger.error("getCountRegistro", e);
			throw new DataException("Cannot load getRegistro");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return total;
	}

	public Collection<ReportProtocolloView> getProtocolliInLavorazione(int registroId,
			Date dataInizio, Date dataFine, UfficioVO ufficio, int assegnatario)
			throws DataException {

		ArrayList<ReportProtocolloView> protocolli = new ArrayList<ReportProtocolloView>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String strQuery = "SELECT a.*, b.desc_tipo_documento, c.utente_assegnatario_id "
				+ sqlProtocollByStati(FLAG_PROTOCOLLI_IN_LAVORAZIONE,
						dataInizio, dataFine, ufficio, assegnatario)
				+ " ORDER BY a.anno_registrazione DESC, a.nume_protocollo DESC";
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, registroId);
			if (dataInizio != null && dataFine != null) {
				pstmt.setDate(2, new java.sql.Date(dataInizio.getTime()));
				pstmt.setTimestamp(3, new java.sql.Timestamp(dataFine.getTime()
						+ Constants.GIORNO_MILLISECONDS - 1));

			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ReportProtocolloView protocollo = new ReportProtocolloView();
				protocollo.setAnnoProtocollo(rs.getInt("ANNO_REGISTRAZIONE"));
				protocollo.setNumeroProtocollo(rs.getInt("NUME_PROTOCOLLO"));
				protocollo.setTipoProtocollo(rs.getString("FLAG_TIPO"));
				protocollo.setDataProtocollo(DateUtil.formattaData(rs.getDate(
						"DATA_REGISTRAZIONE").getTime()));
				if (rs.getInt("FLAG_RISERVATO") == 1) {
					protocollo.setOggetto(Parametri.PROTOCOLLO_RISERVATO);
					protocollo.setMittente(Parametri.PROTOCOLLO_RISERVATO);

				} else {
					protocollo.setOggetto(rs.getString("TEXT_OGGETTO"));
					if ("F".equals(rs.getString("FLAG_TIPO_MITTENTE"))) {
						protocollo.setMittente(rs
								.getString("DESC_COGNOME_MITTENTE")
								+ " "
								+ StringUtil.getStringa(rs
										.getString("DESC_NOME_MITTENTE")));
					} else if ("M".equals(rs.getString("flag_tipo_mittente"))) {
						protocollo.setTipoMittente("M");
						StringBuffer mittente = new StringBuffer();

						List<SoggettoVO> mittenti = MittentiDelegate
								.getInstance().getMittenti(
										rs.getInt("protocollo_id"));
						for (SoggettoVO mitt : mittenti) {
							mittente.append(mitt.getDescrizione() + "\r\n");
						}
						protocollo.setMittente(mittente.toString());
					} else if ("G".equals(rs.getString("FLAG_TIPO_MITTENTE"))) {
						protocollo.setMittente(rs
								.getString("DESC_DENOMINAZIONE_MITTENTE"));
					}
				}
				protocollo.setUtenteAssegnatario(""
						+ rs.getInt("utente_assegnatario_id"));
				protocollo
						.setTipoDocumento(rs.getString("DESC_TIPO_DOCUMENTO"));
				protocolli.add(protocollo);
			}
		} catch (Exception e) {
			logger.error("getProtocolliInLavorazione", e);
			throw new DataException("Cannot load getProtocolliInLavorazione");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return protocolli;

	}

	public Collection<ReportCheckPostaInternaView> getNotifichePostaInterna(int caricaId,
			Date dataInizio, Date dataFine)
			throws DataException {

		ArrayList<ReportCheckPostaInternaView> checkCollection = new ArrayList<ReportCheckPostaInternaView>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String strQuery = "SELECT c.check_id,c.data_operazione,c.protocollo_id,c.flag_competente,uf.descrizione AS descrizione_uff,ut.cognome||' '||ut.nome AS assegnatario,p.nume_protocollo,p.anno_registrazione " 
			+ " FROM check_posta_interna c" 
			+ " LEFT JOIN uffici uf ON(c.ufficio_assegnatario_id=uf.ufficio_id)"
			+ " LEFT JOIN cariche car ON(c.carica_assegnatario_id=car.carica_id)"
			+ " LEFT JOIN utenti ut ON(car.utente_id=ut.utente_id)"
			+ " LEFT JOIN protocolli p ON(c.protocollo_id=p.protocollo_id)"
			+ " WHERE c.carica_assegnante_id=? ";
		if (dataInizio != null) 
			strQuery += " AND c.data_operazione >= ?";
		if (dataFine != null) 
			strQuery += " AND c.data_operazione <= ?";
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			int contatore = 1;
			pstmt.setInt(contatore++, caricaId);
			if (dataInizio != null ) {
				pstmt.setDate(contatore++, new java.sql.Date(dataInizio.getTime()));
				//pstmt.setTimestamp(3, new java.sql.Timestamp(dataFine.getTime()+ Constants.GIORNO_MILLISECONDS - 1));
			}
			if (dataFine != null ) {
				//stmt.setDate(2, new java.sql.Date(dataInizio.getTime()));
				pstmt.setTimestamp(contatore++, new java.sql.Timestamp(dataFine.getTime()+ Constants.GIORNO_MILLISECONDS - 1));
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ReportCheckPostaInternaView checkPI = new ReportCheckPostaInternaView();
				checkPI.setCheckId(rs.getInt("check_id"));
				checkPI.setProtocolloId(rs.getInt("protocollo_id"));
				checkPI.setProtocolloDescrizione(rs.getInt("nume_protocollo")+"/"+rs.getInt("anno_registrazione"));
				checkPI.setDataOperazione(DateUtil.formattaDataOra(rs.getTimestamp("data_operazione").getTime()));
				checkPI.setAssegnatario(rs.getString("descrizione_uff")+"/"+rs.getString("assegnatario"));
				checkPI.setCompetente(rs.getBoolean("flag_competente"));
				checkCollection.add(checkPI);
			}
		} catch (Exception e) {
			logger.error("getNotifichePostaInterna", e);
			throw new DataException("Cannot load getNotifichePostaInterna");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return checkCollection;

	}
	
	public int countNotifichePostaInterna(int caricaId, Date dataInizio,
			Date dataFine)
			throws DataException {

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int total = 0;

		String strQuery = "SELECT count(check_id) " 
			+ " FROM check_posta_interna" 
			+ " WHERE carica_assegnante_id=? ";
		if (dataInizio != null) 
			strQuery += " AND data_operazione >= ?";
		if (dataFine != null) 
			strQuery += " AND data_operazione <= ?";
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			int contatore=1;
			pstmt.setInt(contatore++, caricaId);
			if (dataInizio != null ) {
				pstmt.setDate(contatore++, new java.sql.Date(dataInizio.getTime()));
				//pstmt.setTimestamp(3, new java.sql.Timestamp(dataFine.getTime()+ Constants.GIORNO_MILLISECONDS - 1));
			}
			if (dataFine != null ) {
				//stmt.setDate(2, new java.sql.Date(dataInizio.getTime()));
				pstmt.setTimestamp(contatore++, new java.sql.Timestamp(dataFine.getTime()+ Constants.GIORNO_MILLISECONDS - 1));
			}
			rs = pstmt.executeQuery();
			rs.next();
			total = rs.getInt(1);
		} catch (Exception e) {
			logger.error("countNotifichePostaInterna", e);
			throw new DataException("Cannot load countNotifichePostaInterna");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return total;

	}
	
	public int countProtocolliInLavorazione(int registroId, Date dataInizio,
			Date dataFine, UfficioVO ufficio, int assegnatario)
			throws DataException {

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int total = 0;

		String strQuery = "SELECT COUNT(a.protocollo_id) "
				+ sqlProtocollByStati(FLAG_PROTOCOLLI_IN_LAVORAZIONE,
						dataInizio, dataFine, ufficio, assegnatario);

		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, registroId);
			if (dataInizio != null && dataFine != null) {
				pstmt.setDate(2, new java.sql.Date(dataInizio.getTime()));
				pstmt.setTimestamp(3, new java.sql.Timestamp(dataFine.getTime()
						+ Constants.GIORNO_MILLISECONDS - 1));
			}
			rs = pstmt.executeQuery();
			rs.next();
			total = rs.getInt(1);
		} catch (Exception e) {
			logger.error("countProtocolliInLavorazione", e);
			throw new DataException("Cannot load countProtocolliInLavorazione");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return total;

	}

	public Collection<ReportProtocolloView> getProtocolliScaricati(int registroId, Date dataInizio,
			Date dataFine, UfficioVO ufficio, int assegnatario)
			throws DataException {

		ArrayList<ReportProtocolloView> protocolli = new ArrayList<ReportProtocolloView>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String strQuery = "SELECT a.*, b.desc_tipo_documento, c.ufficio_assegnatario_id "
				+ sqlProtocollByStati(FLAG_PROTOCOLLI_SCARICATI, dataInizio,
						dataFine, ufficio, assegnatario)
				+ " ORDER BY a.anno_registrazione DESC, a.nume_protocollo DESC";

		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, registroId);
			if (dataInizio != null && dataFine != null) {
				pstmt.setDate(2, new java.sql.Date(dataInizio.getTime()));
				pstmt.setTimestamp(3, new java.sql.Timestamp(dataFine.getTime()
						+ Constants.GIORNO_MILLISECONDS - 1));
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
				ReportProtocolloView protocollo = new ReportProtocolloView();
				protocollo.setAnnoProtocollo(rs.getInt("ANNO_REGISTRAZIONE"));
				protocollo.setNumeroProtocollo(rs.getInt("NUME_PROTOCOLLO"));
				protocollo.setTipoProtocollo(rs.getString("FLAG_TIPO"));
				if ("R".equals(rs.getString("STATO_PROTOCOLLO"))) {
					int protocolloId = rs.getInt("PROTOCOLLO_ID");
					DestinatarioAllaccioVO destinatarioAllaccioVO = new DestinatarioAllaccioVO();
					destinatarioAllaccioVO = getProtocolloRisposta(registroId,
							protocolloId);
					protocollo
							.setAllaccio(destinatarioAllaccioVO.getAllaccio());
					protocollo.setDestinatario(destinatarioAllaccioVO
							.getDestinatario());
				}

				protocollo.setStatoProtocollo(rs.getString("STATO_PROTOCOLLO"));
				protocollo.setDataScarico(DateUtil.formattaData(rs.getDate(
						"DATA_SCARICO").getTime()));
				if (rs.getInt("FLAG_RISERVATO") == 1) {
					protocollo.setOggetto(Parametri.PROTOCOLLO_RISERVATO);
					protocollo.setMittente(Parametri.PROTOCOLLO_RISERVATO);

				} else {
					protocollo.setOggetto(rs.getString("TEXT_OGGETTO"));
					if ("F".equals(rs.getString("FLAG_TIPO_MITTENTE"))) {
						protocollo.setMittente(rs
								.getString("DESC_COGNOME_MITTENTE")
								+ " "
								+ StringUtil.getStringa(rs
										.getString("DESC_NOME_MITTENTE")));
					} else if ("M".equals(rs.getString("flag_tipo_mittente"))) {
						protocollo.setTipoMittente("M");
						StringBuffer mittente = new StringBuffer();

						List<SoggettoVO> mittenti = MittentiDelegate
								.getInstance().getMittenti(
										rs.getInt("protocollo_id"));
						for (SoggettoVO mitt : mittenti) {
							mittente.append(mitt.getDescrizione() + "\r\n");
						}
						protocollo.setMittente(mittente.toString());
					} else if ("G".equals(rs.getString("FLAG_TIPO_MITTENTE"))) {
						protocollo.setMittente(rs
								.getString("DESC_DENOMINAZIONE_MITTENTE"));
					}
				}
				protocollo
						.setUfficio("" + rs.getInt("ufficio_assegnatario_id"));
				protocolli.add(protocollo);
			}
		} catch (Exception e) {
			logger.error("getProtocolliScaricati", e);
			throw new DataException("Cannot load getProtocolliScaricati");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return protocolli;

	}

	public int countProtocolliScaricati(int registroId, Date dataInizio,
			Date dataFine, UfficioVO ufficio, int assegnatario)
			throws DataException {
		int total = 0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String strQuery = "SELECT COUNT(a.protocollo_id) "
				+ sqlProtocollByStati(FLAG_PROTOCOLLI_SCARICATI, dataInizio,
						dataFine, ufficio, assegnatario);

		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, registroId);
			if (dataInizio != null && dataFine != null) {
				pstmt.setDate(2, new java.sql.Date(dataInizio.getTime()));
				pstmt.setTimestamp(3, new java.sql.Timestamp(dataFine.getTime()
						+ Constants.GIORNO_MILLISECONDS - 1));
			}

			rs = pstmt.executeQuery();
			rs.next();
			total = rs.getInt(1);
		} catch (Exception e) {
			logger.error("countProtocolliScaricati", e);
			throw new DataException("Cannot load countProtocolliScaricati");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return total;

	}

	public int countProtocolliAssegnati(int registroId, UfficioVO ufficio,
			int ufficioAssegnatario, int caricaAssegnatario, int anno)
			throws DataException {

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int total = 0;

		String strQuery = "SELECT COUNT(a.protocollo_id) "
				+ sqlProtocolliAssegnati(FLAG_PROTOCOLLI_ASSEGNATI, ufficio,
						ufficioAssegnatario);
		if (caricaAssegnatario != 0)
			strQuery += " AND c.carica_assegnatario_id=" + caricaAssegnatario;
		if (anno != 0)
			strQuery += " AND a.anno_registrazione=" + anno;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, registroId);
			rs = pstmt.executeQuery();
			rs.next();
			total = rs.getInt(1);
		} catch (Exception e) {
			logger.error("countProtocolliAssegnati", e);
			throw new DataException("Cannot load countProtocolliAssegnati");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return total;

	}

	public Collection<ReportProtocolloView> getProtocolliAssegnati(int registroId, UfficioVO ufficio,
			int ufficioAssegnatario, int caricaAssegnatario, int anno)
			throws DataException {

		ArrayList<ReportProtocolloView> protocolli = new ArrayList<ReportProtocolloView>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String strQuery = "SELECT a.*, c.ufficio_assegnatario_id, c.carica_assegnatario_id  "
				+ sqlProtocolliAssegnati(FLAG_PROTOCOLLI_ASSEGNATI, ufficio,
						ufficioAssegnatario);
		if (caricaAssegnatario != 0)
			strQuery += " AND c.carica_assegnatario_id=" + caricaAssegnatario;
		if (anno != 0)
			strQuery += " AND a.anno_registrazione=" + anno;
		strQuery += " ORDER BY a.anno_registrazione DESC, a.nume_protocollo DESC";
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, registroId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ReportProtocolloView protocollo = new ReportProtocolloView();
				protocollo.setAnnoProtocollo(rs.getInt("ANNO_REGISTRAZIONE"));
				protocollo.setNumeroProtocollo(rs.getInt("NUME_PROTOCOLLO"));
				protocollo.setTipoProtocollo(rs.getString("FLAG_TIPO"));
				protocollo.setDataProtocollo(DateUtil.formattaData(rs.getDate(
						"DATA_REGISTRAZIONE").getTime()));
				if (rs.getInt("FLAG_RISERVATO") == 1) {
					protocollo.setOggetto(Parametri.PROTOCOLLO_RISERVATO);
				} else {
					protocollo.setOggetto(rs.getString("TEXT_OGGETTO"));
				}
				protocollo.setUfficioAssegnatario(""
						+ rs.getInt("ufficio_assegnatario_id"));
				protocollo.setUtenteAssegnatario(""
						+ rs.getInt("carica_assegnatario_id"));
				// protocollo.setTipoDocumento(rs.getString("DESC_TIPO_DOCUMENTO"));
				protocolli.add(protocollo);
			}
		} catch (Exception e) {
			logger.error("getProtocolliAssegnati", e);
			throw new DataException("Cannot load getProtocolliAssegnati");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return protocolli;

	}

	public int countProtocolliAnnullati(int registroId, UfficioVO ufficio,
			int ufficioId) throws DataException {

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int total = 0;

		String strQuery = "SELECT COUNT(p.protocollo_id) "
				+ sqlProtocolliAnnullati(FLAG_PROTOCOLLO_ANNULLATO);

		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, registroId);
			pstmt.setInt(2, ufficioId);
			rs = pstmt.executeQuery();
			rs.next();
			total = rs.getInt(1);
		} catch (Exception e) {
			logger.error("countProtocolliAnnullati", e);
			throw new DataException("Cannot load countProtocolliAnnullati");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return total;

	}

	public int countProtocolliDaScartare(Utente utente, int ufficioId,
			java.util.Date dataInizio, java.util.Date dataFine)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int total = 0;

		StringBuffer strQuery = new StringBuffer(
				COUNT_LISTA_PROTOCOLLI_DA_SCARTARE);
		// String strQuery = "SELECT COUNT(p.protocollo_id) "
		// + sqlProtocolliAnnullati(FLAG_PROTOCOLLO_ANNULLATO, null, null,
		// ufficio, ufficioId);
		if (dataInizio != null) {
			strQuery.append(" AND (anno_registrazione>?) ");

		}
		if (dataFine != null) {
			strQuery.append("AND (anno_registrazione<(?-t.massimario))");
		}

		int indiceQuery = 1;

		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery.toString());
			pstmt.setInt(indiceQuery++, utente.getValueObject().getId()
					.intValue());
			pstmt.setInt(indiceQuery++, ufficioId);
			if (dataInizio != null) {
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(dataInizio);
				int annoRegDa = calendar.get(Calendar.YEAR);
				// pstmt.setDate(indiceQuery++, new
				// java.sql.Date(dataRegDa.getTime()));
				pstmt.setInt(indiceQuery++, annoRegDa);
			}
			if (dataFine != null) {
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(dataFine);
				int annoRegA = calendar.get(Calendar.YEAR);
				// pstmt.setDate(indiceQuery++, new
				// java.sql.Date(dataRegA.getTime()) );
				pstmt.setInt(indiceQuery++, annoRegA);
			}
			rs = pstmt.executeQuery();
			rs.next();
			total = rs.getInt(1);
		} catch (Exception e) {
			logger.error("countProtocolliDaScartare", e);
			throw new DataException("Cannot load countProtocolliDaScartare");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return total;

	}

	public Collection<ReportProtocolloView> getProtocolliAnnullati(int registroId, UfficioVO ufficio,
			int ufficioId) throws DataException {

		ArrayList<ReportProtocolloView> protocolli = new ArrayList<ReportProtocolloView>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String strQuery = "SELECT p.*, b.desc_tipo_documento "
				+ sqlProtocolliAnnullati(FLAG_PROTOCOLLO_ANNULLATO)
				+ " ORDER BY p.anno_registrazione DESC, p.nume_protocollo DESC";
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, registroId);
			pstmt.setInt(2, ufficioId);
			/*
			 * if (!ufficio.getTipo().equals(UfficioVO.UFFICIO_CENTRALE)) {
			 * pstmt.setInt(2, ufficioId); pstmt.setInt(3, ufficioId); }
			 */
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ReportProtocolloView protocollo = new ReportProtocolloView();
				protocollo.setAnnoProtocollo(rs.getInt("ANNO_REGISTRAZIONE"));
				protocollo.setNumeroProtocollo(rs.getInt("NUME_PROTOCOLLO"));
				protocollo.setTipoProtocollo(rs.getString("FLAG_TIPO"));

				protocollo
						.setTipoDocumento(rs.getString("DESC_TIPO_DOCUMENTO"));
				protocollo.setDataProtocollo(DateUtil.formattaData(rs.getDate(
						"DATA_REGISTRAZIONE").getTime()));
				if (rs.getInt("FLAG_RISERVATO") == 1) {
					protocollo.setOggetto(Parametri.PROTOCOLLO_RISERVATO);
					protocollo.setMittente(Parametri.PROTOCOLLO_RISERVATO);

				} else {
					protocollo.setOggetto(rs.getString("TEXT_OGGETTO"));
					if ("F".equals(rs.getString("FLAG_TIPO_MITTENTE"))) {
						protocollo.setMittente(rs
								.getString("DESC_COGNOME_MITTENTE")
								+ " "
								+ StringUtil.getStringa(rs
										.getString("DESC_NOME_MITTENTE")));
					} else if ("M".equals(rs.getString("flag_tipo_mittente"))) {
						protocollo.setTipoMittente("M");
						StringBuffer mittente = new StringBuffer();

						List<SoggettoVO> mittenti = MittentiDelegate
								.getInstance().getMittenti(
										rs.getInt("protocollo_id"));
						for (SoggettoVO mitt : mittenti) {
							mittente.append(mitt.getDescrizione() + "\r\n");
						}
						protocollo.setMittente(mittente.toString());
					} else if ("G".equals(rs.getString("FLAG_TIPO_MITTENTE"))
							|| FLAG_TIPO_PROTOCOLLO_USCITA.equals(rs
									.getString("FLAG_TIPO"))) {
						protocollo.setMittente(rs
								.getString("DESC_DENOMINAZIONE_MITTENTE"));
					}
				}
				if (rs.getDate("data_annullamento") != null)
					protocollo.setDataAnnullamento(DateUtil.formattaData(rs
							.getDate("data_annullamento").getTime()));
				if (rs.getString("text_nota_annullamento") != null)
					protocollo.setNotaAnnullamento(rs
							.getString("text_nota_annullamento"));
				if (rs.getString("text_provvedimento_annullament") != null)
					protocollo.setProvvedimentoAnnullamento(rs
							.getString("text_provvedimento_annullament"));
				protocolli.add(protocollo);
			}
		} catch (Exception e) {
			logger.error("getProtocolliAssegnati", e);
			throw new DataException("Cannot load getProtocolliAssegnati");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return protocolli;

	}

	private String sqlProtocolliAnnullati(String stato) {

		String strQuery = " FROM protocolli p LEFT JOIN tipi_documento b ON (p.tipo_documento_id=b.tipo_documento_id )";
		strQuery += " WHERE p.stato_protocollo="
				+ stato
				+ " AND p.registro_id=? "
				+ "AND p.row_created_user IN (SELECT user_name from utenti where utente_id IN (select utente_id from cariche where ufficio_id=?)) ";
		return strQuery;
	}

	public Collection<ReportProtocolloView> getProtocolliSpediti(int registroId, Date dataInizio,
			Date dataFine, int ufficioId, String mezzoSpedizione,
			int mezzoSpedizioneId) throws DataException {

		ArrayList<ReportProtocolloView> protocolli = new ArrayList<ReportProtocolloView>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
				String strQuery = "SELECT a.*, b.mezzo_spedizione, b.mezzo_spedizione_id, b.data_spedizione, b.destinatario,b.citta,b.indirizzo "
				+ sqlProtocolliSpediti(mezzoSpedizione, mezzoSpedizioneId,
						dataInizio, dataFine, ufficioId)
				+ " ORDER BY a.anno_registrazione DESC, a.nume_protocollo DESC";
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			int contatore = 1;
			pstmt.setInt(contatore++, registroId);
			// pstmt.setInt(contatore++, ufficioId);
			// pstmt.setInt(contatore++, ufficioId);

			if (mezzoSpedizioneId > 0) {
				pstmt.setInt(contatore++, mezzoSpedizioneId);
			}

			// if (mezzoSpedizione != null && !"".equals(mezzoSpedizione)) {
			// pstmt.setString(contatore++, mezzoSpedizione);
			// }
			if (dataInizio != null && dataFine != null) {
				pstmt.setDate(contatore++, new java.sql.Date(dataInizio
						.getTime()));
				pstmt.setDate(contatore++,
						new java.sql.Date(dataFine.getTime()));
			}
			rs = pstmt.executeQuery();
			ReportProtocolloView protocollo = null;
			while (rs.next()) {
				if (protocollo != null
						&& protocollo.getProtocolloId() == rs
								.getInt("protocollo_id")) {
					if (rs.getInt("FLAG_RISERVATO") == 0) {
						logger.info("flag riservato vale 0.");
						protocollo.setDestinatario(protocollo.getDestinatario()
								+ "\r\n" + rs.getString("destinatario"));
						protocollo.setMezzoSpedizione(protocollo
								.getMezzoSpedizione()
								+ "\r\n"
								+ StringUtil.getStringa(rs
										.getString("mezzo_spedizione")));
						protocollo.setMezzoSpedizioneId(rs
								.getInt("mezzo_spedizione_id"));
						logger.info(protocollo.getMezzoSpedizioneId()
								+ " é l'id del mezzo.");
						if (rs.getDate("data_spedizione") != null) {
							protocollo.setDataSpedizione(protocollo
									.getDataSpedizione()
									+ "\r\n"
									+ DateUtil.formattaData(rs.getDate(
											"data_spedizione").getTime()));
						} else {
							protocollo.setDataSpedizione(protocollo
									.getDataSpedizione()
									+ "\r\n");
						}

					}
				} else {
					protocollo = new ReportProtocolloView();
					protocollo.setProtocolloId(rs.getInt("protocollo_id"));
					protocollo.setAnnoProtocollo(rs
							.getInt("ANNO_REGISTRAZIONE"));
					protocollo.setTipoProtocollo(rs.getString("FLAG_TIPO"));
					protocollo.setDataProtocollo(DateUtil.formattaData(rs
							.getDate("DATA_REGISTRAZIONE").getTime()));
					// mittente
					protocollo.setUfficioAssegnatario(""
							+ rs.getInt("ufficio_mittente_id"));
					protocollo.setUtenteAssegnatario(""
							+ rs.getInt("utente_mittente_id"));
					protocollo
							.setNumeroProtocollo(rs.getInt("NUME_PROTOCOLLO"));
					if (rs.getInt("FLAG_RISERVATO") == 0) {
						protocollo
								.setDestinatario(rs.getString("destinatario"));
						protocollo.setMezzoSpedizione(StringUtil.getStringa(rs
								.getString("mezzo_spedizione")));
						protocollo.setCitta(StringUtil.getStringa(rs
								.getString("citta")));
						protocollo.setToponimo(StringUtil.getStringa(rs
								.getString("indirizzo")));
						if (rs.getDate("data_spedizione") != null) {
							protocollo.setDataSpedizione(DateUtil
									.formattaData(rs.getDate("data_spedizione")
											.getTime()));
						} else {
							protocollo.setDataSpedizione("");
						}

					} else {
						protocollo
								.setDestinatario(Parametri.PROTOCOLLO_RISERVATO);
					}

					protocolli.add(protocollo);

				}

			}
		} catch (Exception e) {
			logger.error("getProtocolliSpediti", e);
			throw new DataException("Cannot load getProtocolliSpediti");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return protocolli;

	}

	private String sqlProtocolliSpediti(String mezzoSpedizione,
			int mezzoSpedizioneId, Date dataInizio, Date dataFine,
			int ufficioMittenteId) {
		String sql = " FROM protocolli a, protocollo_destinatari b "
				+ " WHERE a.registro_id=? AND a.flag_tipo ='U' AND a.protocollo_id=b.protocollo_id ";
				// +
				// " AND (a.ufficio_mittente_id =? OR a.ufficio_protocollatore_id=?)"
				//+ " AND EXISTS(SELECT * FROM protocollo_destinatari d"
				//+ " WHERE d.protocollo_id=a.protocollo_id ";
				//+ " AND d.data_spedizione is not null)";

		if (mezzoSpedizioneId > 0) {
			sql += " AND b.mezzo_spedizione_id=?";
		}

		if (dataInizio != null && dataFine != null) {
			sql += " AND a.data_registrazione BETWEEN ? AND ?";
		}

		return sql;
	}

	private String sqlProtocolliAssegnati(String stati, UfficioVO ufficio,
			int assegnatari) {
		String sql = " FROM protocolli a,  protocollo_assegnatari c "
				+ " WHERE a.registro_id=? AND FLAG_TIPO ='I' "
				+ " AND c.protocollo_id=a.protocollo_id AND c.ufficio_assegnatario_id ="
				+ assegnatari + " AND c.flag_competente=1 "
				+ "AND NOT a.stato_protocollo ='C'";
		// + " AND a.stato_protocollo IN (" + stati + ")";
		return sql;
	}

	private String sqlProtocollByStati(String stati, Date dataInizio,
			Date dataFine, UfficioVO ufficio, int assegnatario) {
		String sql = " FROM protocolli a, tipi_documento b, protocollo_assegnatari c "
				+ " WHERE a.registro_id=? AND c.protocollo_id=a.protocollo_id "
				+ " AND ufficio_assegnatario_id="
				+ assegnatario
				+ " AND a.tipo_documento_id=b.tipo_documento_id AND c.flag_competente=1 "
				+ " AND a.stato_protocollo IN (" + stati + ")";

		if (dataInizio != null && dataFine != null) {
			sql += " AND a.data_registrazione BETWEEN ? AND ?";
		}
		return sql;
	}

	public int countProtocolliSpediti(int registroId, Date dataInizio,
			Date dataFine, int ufficioId, String mezzoSpedizione,
			int mezzoSpedizioneId) throws DataException {

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int total = 0;
		//int mSpId=
		String strQuery = "SELECT COUNT(a.protocollo_id)  "
				+ sqlProtocolliSpediti(mezzoSpedizione, mezzoSpedizioneId,
						dataInizio, dataFine, ufficioId);
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			int contatore = 1;
			pstmt.setInt(contatore++, registroId);
			// pstmt.setInt(contatore++, ufficioId);
			// pstmt.setInt(contatore++, ufficioId);

			if (mezzoSpedizioneId != 0) {
				pstmt.setInt(contatore++, mezzoSpedizioneId);
			}

			if (dataInizio != null && dataFine != null) {
				pstmt.setDate(contatore++, new java.sql.Date(dataInizio
						.getTime()));
				pstmt.setDate(contatore++,
						new java.sql.Date(dataFine.getTime()));
			}
			rs = pstmt.executeQuery();
			rs.next();
			total = rs.getInt(1);
		} catch (Exception e) {
			logger.error("countProtocolliSpediti", e);
			throw new DataException("Cannot load countProtocolliSpediti");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return total;

	}

	public double getPrezzoSpedizione(int registroId, Date dataInizio,
			Date dataFine, int ufficioId, String mezzoSpedizione,
			int mezzoSpedizioneId) throws DataException {

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		double total = 0;
		String strQuery = "SELECT b.prezzo_spedizione  "
				+ sqlProtocolliSpediti(mezzoSpedizione, mezzoSpedizioneId,
						dataInizio, dataFine, ufficioId);
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			int contatore = 1;
			pstmt.setInt(contatore++, registroId);
			// pstmt.setInt(contatore++, ufficioId);
			// pstmt.setInt(contatore++, ufficioId);

			if (mezzoSpedizioneId != 0) {
				pstmt.setInt(contatore++, mezzoSpedizioneId);
			}

			if (dataInizio != null && dataFine != null) {
				pstmt.setDate(contatore++, new java.sql.Date(dataInizio
						.getTime()));
				pstmt.setDate(contatore++,
						new java.sql.Date(dataFine.getTime()));
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
				String p = rs.getString(1);
				if (p != null && !p.trim().equals("")) {
					Double dp = Double.valueOf(p);
					total =total + dp;
				}
			}
		} catch (Exception e) {
			logger.error("countProtocolliSpediti", e);
			throw new DataException("Cannot load countProtocolliSpediti");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return total;

	}

	public int countRubrica(String tipoPersona, int aooId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int total = 0;

		String strQuery = "SELECT COUNT(rubrica_id) FROM rubrica "
				+ " WHERE aoo_id=? AND flag_tipo_rubrica=? ";

		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, aooId);
			pstmt.setString(2, tipoPersona);
			rs = pstmt.executeQuery();
			rs.next();
			total = rs.getInt(1);
		} catch (Exception e) {
			logger.error("countRubrica", e);
			throw new DataException("Cannot load countRubrica");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return total;

	}

	public Collection<RubricaView> getRubrica(String flagTipo, int aooId)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<RubricaView> rubrica = new ArrayList<RubricaView>();
		RubricaView soggetto = null;

		String strQuery = "SELECT * FROM rubrica "
				+ " WHERE aoo_id=? AND flag_tipo_rubrica=? ORDER BY PERS_COGNOME, PERS_NOME,DESC_DITTA ";

		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, aooId);
			pstmt.setString(2, flagTipo);
			rs = pstmt.executeQuery();
			StringBuffer indirizzo;
			if (flagTipo.equals("G")) {
				while (rs.next()) {
					soggetto = new RubricaView();
					soggetto.setNominativo(rs.getString("DESC_DITTA"));
					soggetto.setPartitaIva(StringUtil.getStringa(rs
							.getString("CODI_PARTITA_IVA")));
					soggetto.setTelefonoReferente(rs
							.getString("TELE_REFERENTE"));
					soggetto.setTelefonoFax(StringUtil.getStringa(rs
							.getString("TELE_TELEFONO"))
							+ " "
							+ StringUtil.getStringa(rs.getString("TELE_FAX")));
					soggetto.setEmail(StringUtil.getStringa(rs
							.getString("INDI_EMAIL")));
					indirizzo = new StringBuffer();
					if (rs.getString("INDI_COMUNE") != null)
						indirizzo.append(rs.getString("INDI_COMUNE"));
					if (rs.getInt("provincia_id") != 0)
						indirizzo.append(" ("
								+ getProvincia(rs.getInt("provincia_id"),
										connection) + ")");

					if (rs.getString("INDI_CAP") != null)
						indirizzo.append(" " + rs.getString("INDI_CAP"));
					if (rs.getString("INDI_TOPONIMO") != null)
						indirizzo.append(" " + rs.getString("INDI_TOPONIMO"));
					if (rs.getString("INDI_CIVICO") != null)
						indirizzo.append(", " + rs.getString("INDI_CIVICO"));
					soggetto.setIndirizzo(indirizzo.toString());
					rubrica.add(soggetto);
				}

			} else {
				while (rs.next()) {
					soggetto = new RubricaView();
					soggetto.setNominativo(rs.getString("PERS_COGNOME") + " "
							+ StringUtil.getStringa(rs.getString("PERS_NOME")));
					soggetto.setCodiceFiscale(rs.getString("CODI_FISCALE"));
					soggetto.setTelefonoFax(StringUtil.getStringa(rs
							.getString("TELE_TELEFONO"))
							+ " "
							+ StringUtil.getStringa(rs.getString("TELE_FAX")));
					soggetto.setEmail(rs.getString("INDI_EMAIL"));
					soggetto.setQualifica(rs.getString("DESC_QUALIFICA"));
					soggetto.setComuneNascita(rs
							.getString("DESC_COMUNE_NASCITA"));
					if (rs.getDate("DATA_NASCITA") != null)
						soggetto.setDataNascita(DateUtil.formattaData(rs
								.getDate("DATA_NASCITA").getTime()));
					indirizzo = new StringBuffer();
					if (rs.getString("INDI_COMUNE") != null)
						indirizzo.append(rs.getString("INDI_COMUNE"));
					if (rs.getInt("provincia_id") != 0)
						indirizzo.append(" ("
								+ getProvincia(rs.getInt("provincia_id"),
										connection) + ")");

					if (rs.getString("INDI_CAP") != null)
						indirizzo.append(" " + rs.getString("INDI_CAP"));
					if (rs.getString("INDI_TOPONIMO") != null)
						indirizzo.append(" " + rs.getString("INDI_TOPONIMO"));
					if (rs.getString("INDI_CIVICO") != null)
						indirizzo.append(", " + rs.getString("INDI_CIVICO"));
					soggetto.setIndirizzo(indirizzo.toString());
					rubrica.add(soggetto);
				}
			}
		} catch (Exception e) {
			logger.error("getRubrica", e);
			throw new DataException("Cannot load getRubrica");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return rubrica;

	}

	private String getProvincia(int provinciaId, Connection connection)
			throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String strQuery = "SELECT codi_provincia FROM province WHERE provincia_id=?";
		String descrizioneProvincia = "";
		try {
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, provinciaId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				descrizioneProvincia = rs.getString("codi_provincia");
			}
		} catch (Exception e) {
			logger.error("getRubrica", e);
			throw new DataException("Cannot load getRubrica");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}

		return descrizioneProvincia;
	}

	public int getNumeroProtocolli(int ufficioAssegnatarioId,
			Integer utenteAssegnatario, String statoProtocollo,
			String statoAssegnazione, Date dataInizio, Date dataFine,
			Utente utente) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int total = 0;

		String strQuery = "SELECT count(p.protocollo_id) "
				+ getSqlStatistiche(ufficioAssegnatarioId, utenteAssegnatario,
						statoProtocollo, statoAssegnazione, dataInizio,
						dataFine);
		// logger.debug(strQuery);
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			int contatore = 1;
			pstmt.setInt(contatore++, utente.getRegistroInUso());
			if (statoProtocollo != null) {
				pstmt.setString(contatore++, statoProtocollo);
			}
			if (statoAssegnazione != null) {
				pstmt.setString(contatore++, statoAssegnazione);
			}
			if (dataInizio != null && dataFine != null) {
				pstmt.setDate(contatore++, new java.sql.Date(dataInizio
						.getTime()));
				pstmt.setTimestamp(contatore++, new java.sql.Timestamp(dataFine
						.getTime()
						+ Constants.GIORNO_MILLISECONDS - 1));
			}
			rs = pstmt.executeQuery();
			rs.next();
			total = rs.getInt(1);
		} catch (Exception e) {
			logger.error("getNumeroProtocolli", e);
			throw new DataException("Cannot load getNumeroProtocolli");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return total;

	}

	public Collection<ReportProtocolloView> getDettaglioStatisticheProtocolli(
			int ufficioAssegnatarioId, Integer utenteAssegnatario,
			String statoProtocollo, String statoAssegnazione, Date dataInizio,
			Date dataFine, Utente utente) throws DataException {
		Connection connection = null;
		Collection<ReportProtocolloView> protocolli = new ArrayList<ReportProtocolloView>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String strQuery = "SELECT p.*, t.desc_tipo_documento"
				+ getSqlStatistiche(ufficioAssegnatarioId, utenteAssegnatario,
						statoProtocollo, statoAssegnazione, dataInizio,
						dataFine);

		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			int contatore = 1;
			pstmt.setInt(contatore++, utente.getRegistroInUso());
			if (statoProtocollo != null) {
				pstmt.setString(contatore++, statoProtocollo);
			}
			if (statoAssegnazione != null) {
				pstmt.setString(contatore++, statoAssegnazione);
			}
			if (dataInizio != null && dataFine != null) {
				pstmt.setDate(contatore++, new java.sql.Date(dataInizio
						.getTime()));
				pstmt.setTimestamp(contatore++, new java.sql.Timestamp(dataFine
						.getTime()
						+ Constants.GIORNO_MILLISECONDS - 1));
			}

			rs = pstmt.executeQuery();
			ReportProtocolloView rpw;
			while (rs.next()) {
				rpw = new ReportProtocolloView();
				rpw.setProtocolloId(rs.getInt("PROTOCOLLO_ID"));
				rpw.setAnnoProtocollo(rs.getInt("ANNO_REGISTRAZIONE"));
				rpw.setNumeroProtocollo(rs.getInt("NUME_PROTOCOLLO"));
				rpw.setDataProtocollo(DateUtil.formattaData(rs.getDate(
						"DATA_REGISTRAZIONE").getTime()));
				if (rs.getInt("FLAG_RISERVATO") == 1) {
					rpw.setOggetto(Parametri.PROTOCOLLO_RISERVATO);

				} else {
					rpw.setOggetto(rs.getString("TEXT_OGGETTO"));
				}
				rpw.setTipoDocumento(rs.getString("DESC_TIPO_DOCUMENTO"));
				protocolli.add(rpw);

			}
		} catch (Exception e) {
			logger.error("getNumeroProtocolli", e);
			throw new DataException("Cannot load getNumeroProtocolli");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return protocolli;

	}

	private String getSqlStatistiche(int ufficioAssegnatarioId,
			Integer caricaAssegnatario, String statoProtocollo,
			String statoAssegnazione, Date dataInizio, Date dataFine) {

		String strQuery = " FROM protocolli p, protocollo_assegnatari a, tipi_documento t"
				+ " where registro_id=? AND flag_tipo='I' and flag_competente=1 and p.protocollo_id=a.protocollo_id"
				+ " and p.tipo_documento_id=t.tipo_documento_id"
				+ " and ufficio_assegnatario_id=" + ufficioAssegnatarioId;
		if (caricaAssegnatario != null && caricaAssegnatario.intValue() > 0) {
			strQuery += " and carica_assegnatario_id ="
					+ caricaAssegnatario.intValue();
		}
		if (statoProtocollo != null) {
			strQuery += " and stato_protocollo =?";
		}
		if (statoAssegnazione != null) {
			strQuery += " and a.stat_assegnazione =?";
		}
		if (dataInizio != null && dataFine != null) {
			strQuery += " AND p.data_registrazione BETWEEN ? AND ?";
		}

		return strQuery;
	}

	public Collection<ReportProtocolloView> cercaProtocolliDaScartare(Utente utente, int servizioId,
			java.util.Date dataRegDa, java.util.Date dataRegA)
			throws DataException {
		Collection<ReportProtocolloView> protocolli = new ArrayList<ReportProtocolloView>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer strQuery = new StringBuffer(
				SELECT_LISTA_PROTOCOLLI_DA_SCARTARE);
		if (dataRegDa != null) {
			strQuery.append(" AND (anno_registrazione>?) ");

		}
		if (dataRegA != null) {
			strQuery.append("AND (anno_registrazione<(?-t.massimario))");
		}

		strQuery.append("ORDER BY p.data_registrazione DESC");

		int indiceQuery = 1;

		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery.toString());
			pstmt.setInt(indiceQuery++, utente.getValueObject().getId()
					.intValue());
			pstmt.setInt(indiceQuery++, servizioId);
			if (dataRegDa != null) {
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(dataRegDa);
				int annoRegDa = calendar.get(Calendar.YEAR);
				// pstmt.setDate(indiceQuery++, new
				// java.sql.Date(dataRegDa.getTime()));
				pstmt.setInt(indiceQuery++, annoRegDa);
			}
			if (dataRegA != null) {
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(dataRegA);
				int annoRegA = calendar.get(Calendar.YEAR);
				// pstmt.setDate(indiceQuery++, new
				// java.sql.Date(dataRegA.getTime()) );
				pstmt.setInt(indiceQuery++, annoRegA);
			}
			rs = pstmt.executeQuery();
			ReportProtocolloView protocollo = null;
			StringBuffer dest_ass = null;
			while (rs.next()) {
				if (protocollo == null
						|| protocollo.getProtocolloId() != rs
								.getInt("protocollo_id")) {
					if (protocollo != null) {
						// dest_ass.append("</ul>");
						dest_ass.append(" ");
						protocollo.setDestinatario(dest_ass.toString());
					}
					// dest_ass = new StringBuffer("<ul>");
					dest_ass = new StringBuffer(" ");
					protocollo = new ReportProtocolloView();
					protocollo.setProtocolloId(rs.getInt("protocollo_id"));
					protocollo.setAnnoProtocollo(rs
							.getInt("anno_registrazione"));
					protocollo
							.setNumeroProtocollo(rs.getInt("nume_protocollo"));
					protocollo.setTipoProtocollo(rs.getString("flag_tipo"));
					protocollo.setOggetto(rs.getString("text_oggetto"));
					StringBuffer mittente = new StringBuffer();
					if ("M".equals(rs.getString("flag_tipo_mittente"))) {
						if (rs.getString("desc_denominazione_mittente") != null)
							mittente.append(rs
									.getString("desc_denominazione_mittente"));
						if (rs.getString("desc_cognome_mittente") != null)
							mittente.append(rs
									.getString("desc_cognome_mittente"));
						if (rs.getString("desc_nome_mittente") != null)
							mittente.append(rs.getString("desc_nome_mittente"));
					} else if ("M".equals(rs.getString("flag_tipo_mittente"))) {
						protocollo.setTipoMittente("M");
						mittente = new StringBuffer();

						List<SoggettoVO> mittenti = MittentiDelegate
								.getInstance().getMittenti(
										rs.getInt("protocollo_id"));
						for (SoggettoVO mitt : mittenti) {
							mittente.append(mitt.getDescrizione() + "\r\n");
						}
						protocollo.setMittente(mittente.toString());
					}
					protocollo.setMittente(mittente.toString());
					protocollo.setDataProtocollo(DateUtil.formattaData(rs
							.getDate("data_registrazione").getTime()));
					protocollo.setStatoProtocollo(rs
							.getString("stato_protocollo"));
					protocollo.setMassimario(rs.getInt("massimario"));
					// protocolli.put(new
					// Integer(protocollo.getProtocolloId()),protocollo);
					protocolli.add(protocollo);

				}
				if (!rs.getBoolean("flag_riservato")) {
					// dest_ass.append("<li>");
					dest_ass.append(" ");
					if ("I".equals(protocollo.getTipoProtocollo())) {
						if (rs.getBoolean("flag_competente")) {
							// dest_ass.append("<em>");
							dest_ass.append(" - ");
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
							// dest_ass.append("</em>");
							dest_ass.append(";");
						}
					} else {
						if (rs.getInt("flag_conoscenza") == 0) {
							// dest_ass.append("<em>");
							dest_ass.append(" - ");
						}
						dest_ass.append(rs.getString("destinatario"));
						if (rs.getInt("flag_conoscenza") == 0) {
							// dest_ass.append("</em>");
							dest_ass.append(";");
						}
					}
					// dest_ass.append("</li>");
					dest_ass.append(" ");

				} else {
					dest_ass = new StringBuffer(Parametri.PROTOCOLLO_RISERVATO);
				}

			}
			if (protocollo != null) {
				// dest_ass.append("</ul>");
				dest_ass.append(" ; ");

				protocollo.setDestinatario(dest_ass.toString());
			}

		} catch (Exception e) {
			logger.error("cercaScartoProtocolli", e);
			throw new DataException("Cannot load cercaScartoProtocolli");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return protocolli;

	}

	public DestinatarioAllaccioVO getProtocolloRisposta(int registroId,
			int protocolloAllacciatoId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DestinatarioAllaccioVO destinatarioAllaccioVO = new DestinatarioAllaccioVO();
		String strQuery = "select p.protocollo_id, p.anno_registrazione,p.nume_protocollo, destinatario"
				+ " from protocolli p, protocollo_allacci a , protocollo_destinatari d"
				+ " where p.flag_tipo='U' and registro_id=? and p.protocollo_id=a.protocollo_allacciato_id"
				+ " and p.protocollo_id=d.protocollo_id and a.flag_principale=1 and a.protocollo_id=?";

		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, registroId);
			pstmt.setInt(2, protocolloAllacciatoId);
			rs = pstmt.executeQuery();
			StringBuffer strDestinatari = new StringBuffer();
			strDestinatari.append("");
			while (rs.next()) {
				strDestinatari.append(rs.getString("destinatario") + "\r\n");
				destinatarioAllaccioVO.setAllaccio(StringUtil
						.formattaNumeroProtocollo(rs
								.getString("nume_protocollo"), 7)
						+ "/" + rs.getString("anno_registrazione"));
			}
			destinatarioAllaccioVO.setDestinatario(strDestinatari.toString());
		} catch (Exception e) {
			logger.error("getProtocolloRisposta", e);
			throw new DataException("Cannot load getProtocolloRisposta");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return destinatarioAllaccioVO;

	}

	public final static String FLAG_PROTOCOLLO_ANNULLATO = "'C'";

	private final static String FLAG_PROTOCOLLI_ASSEGNATI = "'N','R'";

	private final static String FLAG_PROTOCOLLI_IN_LAVORAZIONE = "'N'";

	private final static String FLAG_PROTOCOLLI_SCARICATI = "'A', 'R'";

	public final static String FLAG_TIPO_PROTOCOLLO_MOZIONE = "M";

	public final static String FLAG_TIPO_PROTOCOLLO_USCITA = "U";

	public final static String FLAG_TIPO_PROTOCOLLO_INGRESSO = "I";

	public final static String COUNT_LISTA_PROTOCOLLI_DA_SCARTARE = "SELECT COUNT (p.protocollo_id) "
			+ " FROM protocolli p"
			+ " LEFT JOIN protocollo_assegnatari a ON a.protocollo_id=p.protocollo_id"
			+ " LEFT JOIN uffici uf ON a.ufficio_assegnatario_id=uf.ufficio_id"
			+ " LEFT JOIN cariche c ON a.carica_assegnatario_id=c.carica_id"
			+ " LEFT JOIN utenti ut ON c.utente_id=ut.utente_id"
			+ " LEFT JOIN protocollo_destinatari d ON d.protocollo_id=p.protocollo_id "
			+ " JOIN titolario t ON (p.titolario_id=t.titolario_id) "
			+ " WHERE (utente_protocollatore_id=?) "
			+ " AND (p.titolario_id=?) ";

	public final static String SELECT_LISTA_PROTOCOLLI_DA_SCARTARE = "SELECT p.protocollo_id, "
			+ " p.anno_registrazione, p.nume_protocollo, p.flag_tipo, p.flag_riservato, p.text_oggetto,"
			+ " p.data_registrazione, p.documento_id, p.stato_protocollo, p.flag_tipo_mittente, "
			+ " p.desc_cognome_mittente, p.desc_nome_mittente, p.desc_denominazione_mittente, "
			+ " t.massimario, ut.nome as nome_assegnatario, uf.descrizione as ufficio_assegnatario, "
			+ " ut.cognome as cognome_assegnatario, a.flag_competente, d.destinatario, d.flag_conoscenza"
			+ " FROM protocolli p"
			+ " LEFT JOIN protocollo_assegnatari a ON a.protocollo_id=p.protocollo_id"
			+ " LEFT JOIN uffici uf ON a.ufficio_assegnatario_id=uf.ufficio_id"
			+ " LEFT JOIN cariche c ON a.carica_assegnatario_id=c.carica_id"
			+ " LEFT JOIN utenti ut ON c.utente_id=ut.utente_id"
			+ " LEFT JOIN protocollo_destinatari d ON d.protocollo_id=p.protocollo_id "
			+ " JOIN titolario t ON (p.titolario_id=t.titolario_id) "
			+ " WHERE (utente_protocollatore_id=?) "
			+ " AND (p.titolario_id=?) ";

	public final static String SELECT_VELINE = "SELECT "
			+ "p.protocollo_id,p.anno_registrazione,p.nume_protocollo, p.registro_id,"
			+ "p.flag_tipo,p.flag_riservato,p.text_oggetto,p.data_registrazione,"
			+ "p.documento_id,p.stato_protocollo,p.flag_tipo_mittente,"
			+ "p.desc_cognome_mittente,p.desc_nome_mittente,p.titolario_id, p.versione,"
			+ "p.desc_denominazione_mittente,ut.nome as nome_assegnatario,"
			+ "uf.descrizione as ufficio_assegnatario,"
			+ "ut.cognome as cognome_assegnatario,"
			+ "a.flag_competente,d.destinatario,d.flag_conoscenza "
			+ "FROM protocolli p"
			+ " LEFT JOIN protocollo_assegnatari a ON a.protocollo_id=p.protocollo_id"
			+ " LEFT JOIN uffici uf ON a.ufficio_assegnatario_id=uf.ufficio_id"
			+ " LEFT JOIN cariche c ON a.carica_assegnatario_id=c.carica_id"
			+ " LEFT JOIN utenti ut ON c.utente_id=ut.utente_id"
			+ " LEFT JOIN protocollo_destinatari d ON d.protocollo_id=p.protocollo_id "
			+ " WHERE NOT p.stato_protocollo IN ('C','F') AND ";

	public final static String GET_ASSEGNATARI_VELINE = "SELECT "
			+ "distinct ufficio_assegnatario_id,carica_assegnatario_id "
			+ "FROM protocollo_assegnatari a"
			+ ""
			+ " LEFT JOIN protocolli p ON a.protocollo_id=p.protocollo_id"
			+ " WHERE NOT p.stato_protocollo IN ('C','V','P','A','F') AND ";
	
	public final static String GET_STORICO_ASSEGNATARI_VELINE = "SELECT "
			+ "distinct ufficio_assegnatario_id,carica_assegnatario_id "
			+ "FROM storia_protocollo_assegnatari a"
			+ " LEFT JOIN storia_protocolli p ON (a.protocollo_id=p.protocollo_id AND a.versione=p.versione)"
			+ " WHERE NOT p.stato_protocollo IN ('C','V','P','A','F') AND NOT p.versione=0 AND ";
	
	public final static String SELECT_RIFIUTATO = "SELECT "
			+ "p.protocollo_id,p.anno_registrazione,p.nume_protocollo, p.registro_id,"
			+ "p.flag_tipo,p.flag_riservato,p.text_oggetto,p.data_registrazione,"
			+ "p.documento_id,p.stato_protocollo,p.flag_tipo_mittente,"
			+ "p.desc_cognome_mittente,p.desc_nome_mittente,p.titolario_id, p.versione,"
			+ "p.desc_denominazione_mittente,ut.nome as nome_assegnatario,"
			+ "uf.descrizione as ufficio_assegnatario,"
			+ "ut.cognome as cognome_assegnatario,"
			+ "a.flag_competente,a.messaggio,d.destinatario,d.flag_conoscenza "
			+ "FROM protocolli p"
			+ " LEFT JOIN protocollo_assegnatari a ON a.protocollo_id=p.protocollo_id"
			+ " LEFT JOIN uffici uf ON a.ufficio_assegnatario_id=uf.ufficio_id"
			+ " LEFT JOIN cariche c ON a.carica_assegnatario_id=c.carica_id"
			+ " LEFT JOIN utenti ut ON c.utente_id=ut.utente_id"
			+ " LEFT JOIN protocollo_destinatari d ON d.protocollo_id=p.protocollo_id "
			+ " WHERE ";

	public final static String SELECT_STORICO_VELINE = "SELECT "
			+ "p.protocollo_id,p.anno_registrazione,p.nume_protocollo, p.registro_id,"
			+ "p.flag_tipo,p.flag_riservato,p.text_oggetto,p.data_registrazione,"
			+ "p.documento_id,p.stato_protocollo,p.flag_tipo_mittente,"
			+ "p.desc_cognome_mittente,p.desc_nome_mittente,p.titolario_id, p.versione,"
			+ "p.desc_denominazione_mittente,ut.nome as nome_assegnatario,"
			+ "uf.descrizione as ufficio_assegnatario,"
			+ "ut.cognome as cognome_assegnatario,"
			+ "a.flag_competente,d.destinatario,d.flag_conoscenza "
			+ "FROM storia_protocolli p"
			+ " LEFT JOIN storia_protocollo_assegnatari a ON (a.protocollo_id=p.protocollo_id AND a.versione=p.versione)"
			+ " LEFT JOIN uffici uf ON a.ufficio_assegnatario_id=uf.ufficio_id"
			+ " LEFT JOIN cariche c ON a.carica_assegnatario_id=c.carica_id"
			+ " LEFT JOIN utenti ut ON c.utente_id=ut.utente_id"
			+ " LEFT JOIN storia_protocollo_destinatari d ON (d.protocollo_id=p.protocollo_id AND d.versione=p.versione) "
			+ " WHERE NOT p.stato_protocollo IN ('C','F') AND NOT p.versione=0 AND ";

	public int countVeline(Utente utente, Date dataInizio, Date dataFine,
			int ufficioId, int caricaId, int numInizio, int numFine, String tipo, int conoscenza)
			throws DataException {
		Connection connection = null;
		int count;
		try {
			connection = jdbcMan.getConnection();
			count=countVeline(connection,utente, dataInizio, dataFine, ufficioId, caricaId,numInizio, numFine, tipo,conoscenza);
			count+=countStoricoVeline(connection, utente, dataInizio, dataFine, ufficioId, caricaId,numInizio, numFine, tipo,conoscenza);
		} catch (Exception e) {
			throw new DataException("Cannot load countVeline");
		} finally {
			jdbcMan.close(connection);
		}
		return count;
	}

	private int countVeline(Connection connection, Utente utente, Date dataInizio, Date dataFine,
			int ufficioId, int caricaId, int numInizio, int numFine, String tipo,int conoscenza)
			throws DataException {
		int total = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String strQuery = "SELECT COUNT(p.protocollo_id) FROM PROTOCOLLI p " +
				"LEFT JOIN protocollo_assegnatari a ON a.protocollo_id=p.protocollo_id " +
				"WHERE NOT p.stato_protocollo IN ('C','F') AND ";

		if (ufficioId != 0)
			strQuery += "a.ufficio_assegnante_id=? AND flag_tipo IN ('" + tipo
					+ "') AND a.ufficio_assegnatario_id = " + ufficioId
					+ " AND DATA_REGISTRAZIONE BETWEEN ? AND ?";
		else
			strQuery += "a.ufficio_assegnante_id=? AND flag_tipo IN ('" + tipo
					+ "') AND DATA_REGISTRAZIONE BETWEEN ? AND ?";
		if (numInizio != 0)
			strQuery += " AND p.nume_protocollo >= " + numInizio;
		if (numFine != 0)
			strQuery += " AND p.nume_protocollo <= " + numFine;
		if (caricaId != 0)
			strQuery += " AND a.carica_assegnatario_id=" + caricaId;
		strQuery+=" AND a.flag_competente="+conoscenza;
		try {
			if (connection == null) {
				logger.warn("countVeline() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, utente.getUfficioInUso());
			pstmt.setDate(2, new java.sql.Date(dataInizio.getTime()));
			pstmt.setTimestamp(3, new java.sql.Timestamp(dataFine.getTime()
					+ Constants.GIORNO_MILLISECONDS - 1));
			rs = pstmt.executeQuery();
			rs.next();
			total = rs.getInt(1);
		} catch (Exception e) {
			logger.error("getCountRegistro", e);
			throw new DataException("Cannot load getRegistro");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return total;
	}

	private int countStoricoVeline(Connection connection,Utente utente, Date dataInizio, Date dataFine,
			int ufficioId, int caricaId, int numInizio, int numFine, String tipo, int conoscenza)
			throws DataException {
		int total = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String strQuery = "SELECT COUNT(p.protocollo_id) " +
				"FROM STORIA_PROTOCOLLI p " +
				"LEFT JOIN storia_protocollo_assegnatari a ON (a.protocollo_id=p.protocollo_id AND a.versione=p.versione) " +
				"WHERE NOT p.stato_protocollo IN ('C','F')AND NOT p.versione=0 AND ";

		if (ufficioId != 0)
			strQuery += "a.ufficio_assegnante_id=? AND flag_tipo IN ('" + tipo
					+ "') AND a.ufficio_assegnatario_id = " + ufficioId
					+ " AND DATA_REGISTRAZIONE BETWEEN ? AND ?";
		else
			strQuery += "a.ufficio_assegnante_id=? AND flag_tipo IN ('" + tipo
					+ "') AND DATA_REGISTRAZIONE BETWEEN ? AND ?";
		if (numInizio != 0)
			strQuery += " AND p.nume_protocollo >= " + numInizio;
		if (numFine != 0)
			strQuery += " AND p.nume_protocollo <= " + numFine;
		if (caricaId != 0)
			strQuery += " AND a.carica_assegnatario_id=" + caricaId;
		strQuery+=" AND a.flag_competente="+conoscenza;		
		try {
			if (connection == null) {
				logger.warn("countStoricoVeline() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, utente.getUfficioInUso());
			pstmt.setDate(2, new java.sql.Date(dataInizio.getTime()));
			pstmt.setTimestamp(3, new java.sql.Timestamp(dataFine.getTime()
					+ Constants.GIORNO_MILLISECONDS - 1));
			rs = pstmt.executeQuery();
			rs.next();
			total = rs.getInt(1);
		} catch (Exception e) {
			logger.error("getCountRegistro", e);
			throw new DataException("Cannot load getRegistro");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return total;
	}
	
	public Collection<AssegnatarioView> getStampaAssegnatariVeline(Utente utente, Date dataInizio, Date dataFine,
			int numInizio, int numFine, String tipo,int conoscenza)
			throws DataException {
		Connection connection = null;
		Map<String, AssegnatarioView> assegnatari= new HashMap<String, AssegnatarioView>();
		Map<String, AssegnatarioView> storico=new HashMap<String, AssegnatarioView>();
		try {
			connection = jdbcMan.getConnection();
			assegnatari=getAssegnatariVeline(connection, utente, dataInizio, dataFine, numInizio, numFine, tipo,conoscenza);
			//storico=getStoricoAssegnatariVeline(connection, utente, dataInizio, dataFine, numInizio, numFine, tipo,conoscenza);
			for(String key: storico.keySet()){
				if(!assegnatari.containsKey(key))
					assegnatari.put(key, storico.get(key));
			}
				
		} catch (Exception e) {
			throw new DataException("Cannot load getVeline");
		} finally {
			jdbcMan.close(connection);
		}		
		return assegnatari.values();
	}

	
	
	
	private Map<String, AssegnatarioView> getAssegnatariVeline(Connection connection, Utente utente, Date dataInizio,
			Date dataFine, int numInizio,
			int numFine, String tipo,int conoscenza) throws DataException {
		Map<String, AssegnatarioView> assegnatari = new HashMap<String, AssegnatarioView>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String strQuery = GET_ASSEGNATARI_VELINE;
		strQuery += " a.ufficio_assegnante_id=? AND flag_tipo IN ('" + tipo
					+ "') AND DATA_REGISTRAZIONE BETWEEN ? AND ?";
		if (numInizio != 0)
			strQuery += " AND p.nume_protocollo >= " + numInizio;
		if (numFine != 0)
			strQuery += " AND p.nume_protocollo <= " + numFine;
		strQuery+=" AND a.flag_competente="+conoscenza;
		try {
			if (connection == null) {
				logger.warn("getAssegnatariVeline() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, utente.getUfficioInUso());
			pstmt.setDate(2, new java.sql.Date(dataInizio.getTime()));
			pstmt.setTimestamp(3, new java.sql.Timestamp(dataFine.getTime()
					+ Constants.GIORNO_MILLISECONDS - 1));
			rs = pstmt.executeQuery();
			//pstmt.
			while (rs.next()) {
				AssegnatarioView ass=new AssegnatarioView();
				ass.setUfficioId(rs.getInt("ufficio_assegnatario_id"));
				ass.setCaricaId(rs.getInt("carica_assegnatario_id"));
				assegnatari.put(ass.getKey(),ass);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getAssegnatariVeline", e);
			throw new DataException("Cannot load getAssegnatariVeline");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return assegnatari;
	}
	
	
	public Map<String, AssegnatarioView> getStoricoAssegnatariVeline(Connection connection, Utente utente, Date dataInizio,
			Date dataFine, int numInizio,
			int numFine, String tipo, int conoscenza) throws DataException {
		Map<String, AssegnatarioView> assegnatari = new HashMap<String, AssegnatarioView>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String strQuery = GET_STORICO_ASSEGNATARI_VELINE;
		strQuery += " a.flag_competente=1 AND a.ufficio_assegnante_id=? AND flag_tipo IN ('" + tipo
					+ "') AND DATA_REGISTRAZIONE BETWEEN ? AND ?";
		if (numInizio != 0)
			strQuery += " AND p.nume_protocollo >= " + numInizio;
		if (numFine != 0)
			strQuery += " AND p.nume_protocollo <= " + numFine;
		strQuery+=" AND a.flag_competente="+conoscenza;
		try {
			if (connection == null) {
				logger.warn("getStoricoAssegnatariVeline() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, utente.getUfficioInUso());
			pstmt.setDate(2, new java.sql.Date(dataInizio.getTime()));
			pstmt.setTimestamp(3, new java.sql.Timestamp(dataFine.getTime()
					+ Constants.GIORNO_MILLISECONDS - 1));
			rs = pstmt.executeQuery();
			while (rs.next()) {
				AssegnatarioView ass=new AssegnatarioView();
				ass.setUfficioId(rs.getInt("ufficio_assegnatario_id"));
				ass.setCaricaId(rs.getInt("carica_assegnatario_id"));
				assegnatari.put(ass.getKey(), ass);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getVeline", e);
			throw new DataException("Cannot load getStoricoAssegnatariVeline");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return assegnatari;
	}
	
	
	public Collection<ReportProtocolloView> getVeline(Utente utente, Date dataInizio, Date dataFine,
			int ufficioId, int caricaId, int numInizio, int numFine, String tipo,int conoscenza)
			throws DataException {
		Connection connection = null;
		Map<String, ReportProtocolloView> veline= new HashMap<String, ReportProtocolloView>();
		Map<String, ReportProtocolloView> storico=new HashMap<String, ReportProtocolloView>();
		try {
			connection = jdbcMan.getConnection();
			veline=getVeline(connection, utente, dataInizio, dataFine, ufficioId,caricaId, numInizio, numFine, tipo,conoscenza);
			storico=getStoricoVeline(connection, utente, dataInizio, dataFine, ufficioId,caricaId, numInizio, numFine, tipo,conoscenza);
			for(String annoNumeroStorico: storico.keySet()){
				if(!veline.containsKey(annoNumeroStorico))
					veline.put(annoNumeroStorico, storico.get(annoNumeroStorico));
			}	
		} catch (Exception e) {
			throw new DataException("Cannot load getVeline");
		} finally {
			jdbcMan.close(connection);
		}
		List<ReportProtocolloView> list=new ArrayList<ReportProtocolloView>(veline.values());
		Collections.sort(list);
		return list;
	}

	
	private Map<String, ReportProtocolloView> getVeline(Connection connection, Utente utente, Date dataInizio,
			Date dataFine, int ufficioId, int caricaId, int numInizio,
			int numFine, String tipo,int conoscenza) throws DataException {
		Map<String, ReportProtocolloView> protocolli = new HashMap<String, ReportProtocolloView>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String strQuery = SELECT_VELINE;
		if (ufficioId != 0)
			strQuery += " a.ufficio_assegnante_id=? AND flag_tipo IN ('" + tipo
					+ "') AND a.ufficio_assegnatario_id = " + ufficioId
					+ " AND DATA_REGISTRAZIONE BETWEEN ? AND ?";
		else
			strQuery += "a.ufficio_assegnante_id=? AND flag_tipo IN ('" + tipo
					+ "') AND DATA_REGISTRAZIONE BETWEEN ? AND ?";
		if (numInizio != 0)
			strQuery += " AND p.nume_protocollo >= " + numInizio;
		if (numFine != 0)
			strQuery += " AND p.nume_protocollo <= " + numFine;
		if (caricaId != 0)
			strQuery += " AND a.carica_assegnatario_id=" + caricaId;
		strQuery+=" AND a.flag_competente="+conoscenza;
		strQuery = strQuery
				+ " ORDER BY ANNO_REGISTRAZIONE desc,registro_id,NUME_PROTOCOLLO desc";
		try {
			if (connection == null) {
				logger.warn("getVeline() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, utente.getUfficioInUso());
			pstmt.setDate(2, new java.sql.Date(dataInizio.getTime()));
			pstmt.setTimestamp(3, new java.sql.Timestamp(dataFine.getTime()
					+ Constants.GIORNO_MILLISECONDS - 1));
			rs = pstmt.executeQuery();
			ReportProtocolloView protocollo = null;
			StringBuffer dest_ass = null;
			while (rs.next()) {
				if (protocollo == null
						|| protocollo.getProtocolloId() != rs
								.getInt("protocollo_id")) {
					if (protocollo != null) {
						protocollo.setDestinatario(dest_ass.toString());
					}
					dest_ass = new StringBuffer();
					protocollo = new ReportProtocolloView();
					protocollo.setProtocolloId(rs.getInt("protocollo_id"));
					protocollo.setAnnoProtocollo(rs
							.getInt("anno_registrazione"));
					protocollo.setVersione(rs.getInt("versione"));
					protocollo
							.setNumeroProtocollo(rs.getInt("nume_protocollo"));
					protocollo.setTipoProtocollo(rs.getString("flag_tipo"));

					if (rs.getBoolean("flag_riservato")) {
						protocollo.setOggetto(Parametri.PROTOCOLLO_RISERVATO);
						protocollo.setMittente(Parametri.PROTOCOLLO_RISERVATO);

					} else {
						protocollo.setOggetto(rs.getString("text_oggetto")
								+ "\r\n");
						StringBuffer mittente = new StringBuffer();
						if ("F".equals(rs.getString("flag_tipo_mittente"))) {
							mittente.append(rs
									.getString("desc_cognome_mittente"));
							if (rs.getString("desc_nome_mittente") != null) {
								mittente.append(' ').append(
										rs.getString("desc_nome_mittente"));
							}

							if (mittente != null && mittente.length() > 30)
								mittente.delete(30, mittente.length());
						} else if ("M".equals(rs
								.getString("flag_tipo_mittente"))) {
							protocollo.setTipoMittente("M");
							List<SoggettoVO> mittenti = MittentiDelegate
									.getInstance().getMittenti(
											rs.getInt("protocollo_id"));
							for (SoggettoVO mitt : mittenti) {
								mittente.append(mitt.getDescrizione() + "\r\n");
							}
						} else {
							if (rs.getString("desc_denominazione_mittente") != null)
								mittente
										.append(rs
												.getString("desc_denominazione_mittente"));
						}
						protocollo.setMittente(mittente.toString());
					}
					protocollo.setDataProtocollo(DateUtil.formattaData(rs
							.getDate("data_registrazione").getTime()));

					protocollo.setPdf(rs.getInt("documento_id") > 0);
					protocollo.setStatoProtocollo(rs
							.getString("stato_protocollo"));
					protocolli.put(protocollo.getAnnoNumeroProtocollo(), protocollo);
				}
				if (!rs.getBoolean("flag_riservato")) {
					if (!"U".equals(protocollo.getTipoProtocollo())) {
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
							dest_ass.append(" *");
						}
					} else {
						dest_ass.append(rs.getString("destinatario"));
						if (rs.getInt("flag_conoscenza") == 0) {
							dest_ass.append(" *");
						}
					}
					dest_ass.append("\r\n");
				} else {
					dest_ass = new StringBuffer(Parametri.PROTOCOLLO_RISERVATO);
				}
			}
			if (protocollo != null) {
				protocollo.setDestinatario(dest_ass.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getVeline", e);
			throw new DataException("Cannot load getRegistro");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return protocolli;
	}

	
	private Map<String, ReportProtocolloView> getStoricoVeline(Connection connection, Utente utente, Date dataInizio,
			Date dataFine, int ufficioId, int caricaId, int numInizio,
			int numFine, String tipo, int conoscenza) throws DataException {
		Map<String, ReportProtocolloView> protocolli = new HashMap<String, ReportProtocolloView>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String strQuery = SELECT_STORICO_VELINE;
		if (ufficioId != 0)
			strQuery += " a.ufficio_assegnante_id=? AND flag_tipo IN ('" + tipo
					+ "') AND a.ufficio_assegnatario_id = " + ufficioId
					+ " AND DATA_REGISTRAZIONE BETWEEN ? AND ?";
		else
			strQuery += " a.ufficio_assegnante_id=? AND flag_tipo IN ('" + tipo
					+ "') AND DATA_REGISTRAZIONE BETWEEN ? AND ?";
		if (numInizio != 0)
			strQuery += " AND p.nume_protocollo >= " + numInizio;
		if (numFine != 0)
			strQuery += " AND p.nume_protocollo <= " + numFine;
		if (caricaId != 0)
			strQuery += " AND a.carica_assegnatario_id=" + caricaId;
		strQuery+=" AND a.flag_competente="+conoscenza;

		try {
			if (connection == null) {
				logger.warn("getStoricoVeline() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, utente.getUfficioInUso());
			pstmt.setDate(2, new java.sql.Date(dataInizio.getTime()));
			pstmt.setTimestamp(3, new java.sql.Timestamp(dataFine.getTime()
					+ Constants.GIORNO_MILLISECONDS - 1));
			rs = pstmt.executeQuery();
			ReportProtocolloView protocollo = null;
			StringBuffer dest_ass = null;
			while (rs.next()) {
				if (protocollo == null
						|| protocollo.getProtocolloId() != rs
								.getInt("protocollo_id")) {
					if (protocollo != null) {
						protocollo.setDestinatario(dest_ass.toString());
					}
					dest_ass = new StringBuffer();
					protocollo = new ReportProtocolloView();
					protocollo.setProtocolloId(rs.getInt("protocollo_id"));
					protocollo.setAnnoProtocollo(rs
							.getInt("anno_registrazione"));
					protocollo.setVersione(rs.getInt("versione"));
					protocollo
							.setNumeroProtocollo(rs.getInt("nume_protocollo"));
					protocollo.setTipoProtocollo(rs.getString("flag_tipo"));

					if (rs.getBoolean("flag_riservato")) {
						protocollo.setOggetto(Parametri.PROTOCOLLO_RISERVATO);
						protocollo.setMittente(Parametri.PROTOCOLLO_RISERVATO);

					} else {
						protocollo.setOggetto(rs.getString("text_oggetto")
								+ "\r\n");
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
							List<SoggettoVO> mittenti = MittentiDelegate
									.getInstance().getMittenti(
											rs.getInt("protocollo_id"));
							for (SoggettoVO mitt : mittenti) {
								mittente.append(mitt.getDescrizione() + "\r\n");
							}
						} else {
							mittente.append(rs
									.getString("desc_denominazione_mittente"));
						}
						protocollo.setMittente(mittente.toString());
					}
					protocollo.setDataProtocollo(DateUtil.formattaData(rs
							.getDate("data_registrazione").getTime()));

					protocollo.setPdf(rs.getInt("documento_id") > 0);
					protocollo.setStatoProtocollo(rs
							.getString("stato_protocollo"));
					protocolli.put(protocollo.getAnnoNumeroProtocollo(),protocollo);
				}
				if (!rs.getBoolean("flag_riservato")) {
					if ("I".equals(protocollo.getTipoProtocollo())) {
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
							dest_ass.append(" *");
						}
					} else {
						dest_ass.append(rs.getString("destinatario"));
						if (rs.getInt("flag_conoscenza") == 0) {
							dest_ass.append(" *");
						}
					}
					dest_ass.append("\r\n");
				} else {
					dest_ass = new StringBuffer(Parametri.PROTOCOLLO_RISERVATO);
				}

			}
			if (protocollo != null) {
				protocollo.setDestinatario(dest_ass.toString());
			}

		} catch (Exception e) {
			logger.error("getStoricoVeline", e);
			throw new DataException("Cannot load getRegistro");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return protocolli;
	}
	
	public Collection<ReportProtocolloView> getProtocolloRofiutato(int protocolloId)
			throws DataException {
		ArrayList<ReportProtocolloView> protocolli = new ArrayList<ReportProtocolloView>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String strQuery = SELECT_RIFIUTATO;
		strQuery += "p.protocollo_id=?";
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, protocolloId);
			rs = pstmt.executeQuery();
			ReportProtocolloView protocollo = null;
			StringBuffer dest_ass = null;
			while (rs.next()) {
				if (protocollo == null
						|| protocollo.getProtocolloId() != rs
								.getInt("protocollo_id")) {
					if (protocollo != null) {
						protocollo.setDestinatario(dest_ass.toString());
					}
					dest_ass = new StringBuffer();
					protocollo = new ReportProtocolloView();
					protocollo.setProtocolloId(rs.getInt("protocollo_id"));
					protocollo.setAnnoProtocollo(rs
							.getInt("anno_registrazione"));
					protocollo.setMessaggio(rs.getString("messaggio"));
					protocollo
							.setNumeroProtocollo(rs.getInt("nume_protocollo"));
					protocollo.setTipoProtocollo(rs.getString("flag_tipo"));

					if (rs.getBoolean("flag_riservato")) {
						protocollo.setOggetto(Parametri.PROTOCOLLO_RISERVATO);
						protocollo.setMittente(Parametri.PROTOCOLLO_RISERVATO);
					} else {
						protocollo.setOggetto(rs.getString("text_oggetto")
								+ "\r\n");
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
							List<SoggettoVO> mittenti = MittentiDelegate
									.getInstance().getMittenti(
											rs.getInt("protocollo_id"));
							for (SoggettoVO mitt : mittenti) {
								mittente.append(mitt.getDescrizione() + "\r\n");
							}
						} else {
							mittente.append(rs
									.getString("desc_denominazione_mittente"));
						}
						protocollo.setMittente(mittente.toString());
					}
					protocollo.setDataProtocollo(DateUtil.formattaData(rs
							.getDate("data_registrazione").getTime()));

					protocollo.setPdf(rs.getInt("documento_id") > 0);
					protocollo.setStatoProtocollo(rs
							.getString("stato_protocollo"));
					protocolli.add(protocollo);
				}
				if (!rs.getBoolean("flag_riservato")) {

					if ("I".equals(protocollo.getTipoProtocollo())) {
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
							dest_ass.append(" *");
						}
					} else {
						dest_ass.append(rs.getString("destinatario"));
						if (rs.getInt("flag_conoscenza") == 0) {
							dest_ass.append(" *");
						}
					}
					dest_ass.append("\r\n");
				} else {
					dest_ass = new StringBuffer(Parametri.PROTOCOLLO_RISERVATO);
				}
			}
			if (protocollo != null) {
				protocollo.setDestinatario(dest_ass.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getVeline", e);
			throw new DataException("Cannot load getRegistro");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return protocolli;
	}


};
