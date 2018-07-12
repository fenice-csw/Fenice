package it.finsiel.siged.dao.jdbc;

import it.compit.fenice.mvc.presentation.helper.CodaInvioView;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.EmailConstants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.integration.EmailDAO;
import it.finsiel.siged.mvc.presentation.helper.EmailView;
import it.finsiel.siged.mvc.vo.log.EventoVO;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.posta.CodaInvioVO;
import it.finsiel.siged.mvc.vo.posta.EmailVO;
import it.finsiel.siged.mvc.vo.posta.PecDestVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.FileUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class EmailDAOjdbc implements EmailDAO {

	static Logger logger = Logger.getLogger(EmailDAOjdbc.class.getName());

	public static String INSERT_ALLEGATO = "insert into email_ingresso_allegati ("
			+ "id, email_id, descrizione, filename, content_type, dimensione, impronta, data, row_created_time, row_created_user ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public static String INSERT_ALLEGATO_UFFICIO = "insert into email_ingresso_ufficio_allegati ("
			+ "id, email_id, descrizione, filename, content_type, dimensione, impronta, data, row_created_time, row_created_user ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public static String INSERT_EMAIL_CODA_INVIO = "INSERT INTO EMAIL_CODA_INVIO ( ID, AOO_ID, PROTOCOLLO_ID, DATA_CREAZIONE ) VALUES ( ?, ?, ?, ? )";

	public static String INSERT_DESTINATARI_EMAIL_CODA_INVIO = "INSERT INTO EMAIL_CODA_INVIO_DESTINATARI ( MSG_ID, EMAIL, NOMINATIVO, TIPO ) VALUES ( ?, ?, ?, ? )";

	public static String UPDATE_EMAIL_CODA_INVIO = "UPDATE EMAIL_CODA_INVIO SET STATO=?, data_invio=now() WHERE ID=?";

	public static String INSERT_EMAIL_UFFICIO = "insert into email_ingresso_ufficio ( email_id , descrizione, filename, content_type, dimensione, impronta, testo_messaggio, row_created_time, row_created_user, email_mittente, nome_mittente, email_oggetto, data_spedizione, data_ricezione, segnatura, aoo_id,message_header_id,ufficio_id) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

	public static String INSERT_EMAIL = "insert into email_ingresso ( email_id , descrizione, filename, content_type, dimensione, impronta, testo_messaggio, row_created_time, row_created_user, email_mittente, nome_mittente, email_oggetto, data_spedizione, data_ricezione, segnatura, aoo_id,message_header_id) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public static String IS_EMAIL_PRESENT = "SELECT COUNT(email_id) FROM email_ingresso WHERE message_header_id=?";

	public static String IS_EMAIL_UFFICIO_PRESENT = "SELECT COUNT(email_id) FROM email_ingresso_ufficio WHERE message_header_id=? AND ufficio_id=?";

	public static String IS_EMAIL_LOG_PRESENT = "SELECT COUNT(email_id) FROM EMAIL_LOGS WHERE AOO_ID=?";

	public static String INSERT_EMAIL_LOG = "INSERT INTO EMAIL_LOGS ( EMAIL_ID, ERRORE, TIPO_LOG, AOO_ID ) VALUES ( ?, ?, ?, ?)";

	public static String UPDATE_EMAIL_LOG = "UPDATE email_logs SET errore=?, tipo_log=?, data_log=now() WHERE aoo_id=?";

	public static String SELECT_EMAIL_CODA_INVIO = "SELECT ID FROM EMAIL_CODA_INVIO WHERE AOO_ID=? AND STATO="
			+ EmailConstants.EMAIL_USCITA_NON_INVIATA;

	public static String SELECT_EMAIL_LOG = "SELECT * FROM EMAIL_LOGS WHERE AOO_ID=?";

	public static String SELECT_LIST_EMAIL_LOG = "SELECT * FROM EMAIL_LOGS";

	public static String SELECT_MESSAGGIO_USCITA = "SELECT EM.*, P.NUME_PROTOCOLLO, P.VERSIONE FROM EMAIL_CODA_INVIO EM, PROTOCOLLI P WHERE ID=? AND P.PROTOCOLLO_ID=EM.PROTOCOLLO_ID";

	public static String SELECT_MESSAGGIO_USCITA_BY_PROTOCOLLO_ID = "SELECT * FROM EMAIL_CODA_INVIO WHERE PROTOCOLLO_ID=?";

	public static String SELECT_MESSAGGI_USCITA_BY_AOO_ID = "SELECT em.*, p.nume_protocollo,p.anno_registrazione, p.text_oggetto FROM email_coda_invio em LEFT JOIN protocolli p ON (p.protocollo_id=em.protocollo_id) WHERE em.aoo_id=? ";

	public static String SELECT_MESSAGGI_INGRESSO_BY_AOO_ID = "SELECT e.email_id,e.email_oggetto, e.data_spedizione, e.data_ricezione, e.email_mittente,e.flag_anomalia,"
			+ "e.nome_mittente, (select count(*) from email_ingresso_allegati al where al.email_id=e.email_id) as allegati  FROM EMAIL_INGRESSO e "
			+ "where e.aoo_id =? ";
	
	public static String SELECT_MESSAGGI_WMH_INGRESSO_BY_AOO_ID = "SELECT e.email_id,e.email_oggetto, e.data_spedizione, e.data_ricezione, e.email_mittente,e.flag_anomalia,"
			+ "e.nome_mittente, message_header_id FROM email_ingresso e "
			+ "where e.aoo_id =? ";

	public static String COUNT_MESSAGGI_USCITA_BY_AOO_ID = "SELECT COUNT(*) FROM email_coda_invio WHERE aoo_id=? ";

	public static String COUNT_MESSAGGI_INGRESSO_BY_AOO_ID = "SELECT COUNT(*) FROM email_ingresso WHERE aoo_id=? ";

	public static String SELECT_DESTINATARI_MESSAGGIO = "SELECT * FROM EMAIL_CODA_INVIO_DESTINATARI WHERE MSG_ID=?";

	public static String SELECT_MESSAGGIO_ENTRATA = "SELECT * FROM EMAIL_INGRESSO WHERE EMAIL_ID = ?";

	public static String SELECT_MAIL_UFFICIO_ENTRATA = "SELECT * FROM EMAIL_INGRESSO_UFFICIO WHERE EMAIL_ID = ?";

	public static String SELECT_MESSAGGI_ENTRATA_NON_PROTOCOLLATI = "SELECT e.email_id,e.email_oggetto, e.data_spedizione, e.data_ricezione, e.email_mittente,e.flag_anomalia,"
			+ "e.nome_mittente, (select count(*) from email_ingresso_allegati al where al.email_id=e.email_id) as allegati  FROM EMAIL_INGRESSO e "
			+ "where e.aoo_id = ? and e.flag_stato="
			+ EmailConstants.EMAIL_INGRESSO_NON_PROTOCOLLATA
			+ " order by e.data_spedizione desc";

	public static String SELECT_EMAIL_BY_STATO = "SELECT e.email_id,e.email_oggetto, e.data_spedizione, e.data_ricezione, e.email_mittente,e.flag_anomalia,"
			+ "e.nome_mittente, (select count(*) from email_ingresso_allegati al where al.email_id=e.email_id) as allegati  FROM EMAIL_INGRESSO e "
			+ "where e.aoo_id =? and e.flag_stato=? order by e.data_spedizione desc";

	public static String SELECT_MAIL_UFFICIO_ENTRATA_NON_PROTOCOLLATI = "SELECT e.email_id,e.email_oggetto, e.data_spedizione,e.email_mittente,"
			+ "e.nome_mittente, (select count(*) from email_ingresso_ufficio_allegati al where al.email_id=e.email_id) as allegati  FROM EMAIL_INGRESSO_UFFICIO e "
			+ "where e.ufficio_id = ? and e.flag_stato="
			+ EmailConstants.EMAIL_INGRESSO_NON_PROTOCOLLATA
			+ " order by e.data_spedizione desc";

	public static String SELECT_COUNT_MAIL_UFFICIO_ENTRATA_NON_PROTOCOLLATI = "SELECT count(e.email_id) FROM EMAIL_INGRESSO_UFFICIO e "
			+ "where e.ufficio_id = ? and e.flag_stato="
			+ EmailConstants.EMAIL_INGRESSO_NON_PROTOCOLLATA;

	public static String SELECT_ALLEGATI_MAIL_UFFICIO_ENTRATA = "SELECT * FROM EMAIL_INGRESSO_UFFICIO_ALLEGATI WHERE EMAIL_ID = ?";

	public static String SELECT_ALLEGATI_MESSAGGIO_ENTRATA = "SELECT * FROM EMAIL_INGRESSO_ALLEGATI WHERE EMAIL_ID = ?";

	public static String SELECT_DESCRIZIONE_ALLEGATI = "SELECT id,descrizione FROM EMAIL_INGRESSO_ALLEGATI WHERE EMAIL_ID = ?";

	public static String SELECT_DESCRIZIONE_ALLEGATI_UFFICIO = "SELECT id,descrizione FROM EMAIL_INGRESSO_UFFICIO_ALLEGATI WHERE EMAIL_ID = ?";

	public static String SELECT_ALLEGATO_MESSAGGIO_ENTRATA = "SELECT * FROM EMAIL_INGRESSO_ALLEGATI WHERE ID = ?";

	public static String SELECT_ALLEGATO_MESSAGGIO_UFFICIO = "SELECT * FROM email_ingresso_ufficio_allegati WHERE ID = ?";

	public static String SELECT_ALLEGATO_MAIL_UFFICIO_ENTRATA = "SELECT * FROM email_ingresso_ufficio_allegati WHERE ID = ?";

	public static String UPDATE_STATO_MESSAGGIO_ENTRATA = "UPDATE EMAIL_INGRESSO SET FLAG_STATO=? WHERE EMAIL_ID=?";

	public static String UPDATE_STATO_MESSAGGIO_ENTRATA_UFFICIO = "UPDATE EMAIL_INGRESSO_UFFICIO SET FLAG_STATO=? WHERE EMAIL_ID=?";

	public static String UPDATE_FLAG_ANOMALIA = "UPDATE EMAIL_INGRESSO SET FLAG_ANOMALIA=? WHERE EMAIL_ID=?";

	private static final String UPDATE_STATO_MESSAGGIO_USCITA = "UPDATE email_coda_invio SET stato=?, data_invio=null WHERE id=?";

	private static final String UPDATE_MESSAGGIO_USCITA_DESTINATARIO = "UPDATE email_coda_invio_destinatari SET email=?  WHERE msg_id=? AND nominativo LIKE ?";

	public final static String UPDATE_MAIL_LOG = "UPDATE mail_config SET pec_log=? WHERE aoo_id=?";

	private JDBCManager jdbcMan = new JDBCManager();

	public void salvaMessaggioDestinatariPerInvioErsu(Connection connection,
			int id, int aooId, int protocolloId, SoggettoVO mittente)
			throws DataException {

		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non e' valida.");
			}

			pstmt = connection.prepareStatement(INSERT_EMAIL_CODA_INVIO);
			pstmt.setInt(1, id);
			pstmt.setInt(2, aooId);
			pstmt.setInt(3, protocolloId);
			pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			pstmt.executeUpdate();
			jdbcMan.close(pstmt);
			pstmt = connection
					.prepareStatement(INSERT_DESTINATARI_EMAIL_CODA_INVIO);
			pstmt.setInt(1, id);
			pstmt.setString(2, mittente.getIndirizzoEMail());
			pstmt.setString(3, mittente.getDescrizione());
			pstmt.setString(4, "F");
			pstmt.executeUpdate();
			jdbcMan.close(pstmt);
			logger.debug("Email Accodata per invio, protocollo Id:\n"
					+ protocolloId);

		} catch (Exception e) {
			logger.error("salvaMessaggioPerInvio", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void salvaMessaggioDestinatariPerInvio(Connection connection,
			int id, int aooId, int protocolloId,
			Collection<DestinatarioVO> destinatari) throws DataException {

		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non e' valida.");
			}

			pstmt = connection.prepareStatement(INSERT_EMAIL_CODA_INVIO);
			pstmt.setInt(1, id);
			pstmt.setInt(2, aooId);
			pstmt.setInt(3, protocolloId);
			pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			pstmt.executeUpdate();
			jdbcMan.close(pstmt);

			Iterator<DestinatarioVO> it = destinatari.iterator();
			while (it.hasNext()) {
				DestinatarioVO d = it.next();
				pstmt = connection
						.prepareStatement(INSERT_DESTINATARI_EMAIL_CODA_INVIO);
				pstmt.setInt(1, id);
				pstmt.setString(2, d.getEmail());
				pstmt.setString(3, d.getDestinatario());
				pstmt.setString(4, d.getFlagTipoDestinatario());
				pstmt.executeUpdate();
				jdbcMan.close(pstmt);
			}

			logger.debug("Email Accodata per invio, protocollo Id:\n"
					+ protocolloId);

		} catch (Exception e) {
			logger.error("salvaMessaggioPerInvio", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void salvaEmailCodaInvioDestinatari(Connection connection,
			int id, Collection<DestinatarioVO> destinatari) throws DataException {

		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non e' valida.");
			}

			Iterator<DestinatarioVO> it = destinatari.iterator();
			while (it.hasNext()) {
				DestinatarioVO d = it.next();
				pstmt = connection
						.prepareStatement(INSERT_DESTINATARI_EMAIL_CODA_INVIO);
				pstmt.setInt(1, id);
				pstmt.setString(2, d.getEmail());
				pstmt.setString(3, d.getDestinatario());
				pstmt.setString(4, d.getFlagTipoDestinatario());
				pstmt.executeUpdate();
				jdbcMan.close(pstmt);
			}

			logger.debug("Email Accodata per invio, mail Id:\n"
					+ id);

		} catch (Exception e) {
			logger.error("salvaEmailCodaInvioDestinatari", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}
	
	public void aggiornaMessaggioDestinataroPerInvio(Connection connection,
			int id, DestinatarioVO d) throws DataException {

		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non e' valida.");
			}

			pstmt = connection
					.prepareStatement(INSERT_DESTINATARI_EMAIL_CODA_INVIO);
			pstmt.setInt(1, id);
			pstmt.setString(2, d.getEmail());
			pstmt.setString(3, d.getDestinatario());
			pstmt.setString(4, d.getFlagTipoDestinatario());
			pstmt.executeUpdate();
			logger.debug("Email Accodata per invio, messaggio Id:\n" + id);

		} catch (Exception e) {
			logger.error("salvaMessaggioPerInvio", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public Collection<Integer> getListaMessaggiUscita(int aooId)
			throws DataException {
		ArrayList<Integer> lista = new ArrayList<Integer>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_EMAIL_CODA_INVIO);
			pstmt.setInt(1, aooId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				lista.add(new Integer(rs.getInt("id")));
			}
		} catch (Exception e) {
			logger.error("getListaMessaggiUscita", e);
			throw new DataException("Cannot load getListaMessaggiUscita");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return lista;
	}

	public Collection<EventoVO> getListaLog(int aooId, int tipoLog)
			throws DataException {
		ArrayList<EventoVO> lista = new ArrayList<EventoVO>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_LIST_EMAIL_LOG);
			// pstmt.setInt(1, aooId);
			// pstmt.setInt(2, tipoLog);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				EventoVO ev = new EventoVO();
				ev.setData(rs.getTimestamp("data_log"));
				ev.setMessage(rs.getString("errore"));
				ev.setEventoId(rs.getInt("email_id"));
				ev.setTipoLog(rs.getInt("tipo_log"));
				lista.add(ev);
			}
		} catch (Exception e) {
			logger.error("getListaLog", e);
			throw new DataException("Cannot load getListaLog");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return lista;
	}

	public EventoVO getMailLog(int aooId) throws DataException {
		EventoVO ev = new EventoVO();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_EMAIL_LOG);
			pstmt.setInt(1, aooId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ev.setData(rs.getTimestamp("data_log"));
				ev.setMessage(rs.getString("errore"));
				ev.setEventoId(rs.getInt("email_id"));
				ev.setTipoLog(rs.getInt("tipo_log"));
			}
		} catch (Exception e) {
			logger.error("getListaLog", e);
			throw new DataException("Cannot load getListaLog");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return ev;
	}

	public Collection<PecDestVO> getDestinatariMessaggioUscita(int msgId)
			throws DataException {
		ArrayList<PecDestVO> lista = new ArrayList<PecDestVO>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_DESTINATARI_MESSAGGIO);
			pstmt.setInt(1, msgId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				PecDestVO d = new PecDestVO();
				d.setTipo(rs.getString("tipo"));
				d.setEmail(rs.getString("email"));
				d.setNominativo(rs.getString("nominativo"));
				lista.add(d);
			}
		} catch (Exception e) {
			logger.error("getDestinatariMessaggioUscita", e);
			throw new DataException("Cannot load getDestinatariMessaggioUscita");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return lista;
	}

	public List<PecDestVO> getDestinatariMessaggioUscita(Connection connection,
			int msgId) throws DataException {
		List<PecDestVO> lista = new ArrayList<PecDestVO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non e' valida.");
			}
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_DESTINATARI_MESSAGGIO);
			pstmt.setInt(1, msgId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				PecDestVO d = new PecDestVO();
				d.setTipo(rs.getString("tipo"));
				d.setEmail(rs.getString("email"));
				d.setNominativo(rs.getString("nominativo"));
				d.setKey(msgId + "" + d.getNominativo());
				lista.add(d);
			}
		} catch (Exception e) {
			logger.error("getDestinatariMessaggioUscita", e);
			throw new DataException("Cannot load getDestinatariMessaggioUscita");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return lista;
	}

	public void segnaMessaggioComeInviato(int msgId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(UPDATE_EMAIL_CODA_INVIO);

			pstmt.setInt(1, EmailConstants.EMAIL_USCITA_INVIATA);
			pstmt.setInt(2, msgId);

			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("segnaMessaggioComeInviato", e);
			throw new DataException(
					"Impossibile aggiornare lo stato del messaggio sulla base dati.");
		} finally {
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
	}

	public CodaInvioVO getMessaggioDaInviare(int id) throws DataException {
		CodaInvioVO vo = new CodaInvioVO();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_MESSAGGIO_USCITA);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			vo.setReturnValue(ReturnValues.INVALID);
			if (rs.next()) {
				vo.setId(id);
				vo.setAooId(rs.getInt("aoo_id"));
				vo.setDataCreazione(rs.getDate("data_creazione"));
				vo.setDataInvio(rs.getDate("data_invio"));
				vo.setStato(rs.getInt("stato"));
				vo.setProtocolloId(rs.getInt("protocollo_id"));
				vo.setNumeroProtocollo(rs.getInt("nume_protocollo"));
				if (rs.getInt("versione") > 0)
					vo.setReturnValue(ReturnValues.FOUND);
			} else {
				vo.setReturnValue(ReturnValues.NOT_FOUND);
			}
		} catch (Exception e) {
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return vo;
	}

	public List<CodaInvioView> getListaMessaggiUscitaView(int aooId)
			throws DataException {
		List<CodaInvioView> list = new ArrayList<CodaInvioView>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection
					.prepareStatement(SELECT_MESSAGGI_USCITA_BY_AOO_ID);
			pstmt.setInt(1, aooId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				CodaInvioView vo = new CodaInvioView();
				vo.setMailId(rs.getInt("id"));
				vo.setDataCreazione(rs.getDate("data_creazione"));
				vo.setDataInvio(rs.getDate("data_invio"));
				vo.setStato(rs.getInt("stato"));
				vo.setProtocolloId(rs.getInt("protocollo_id"));
				vo.setNumeroProtocollo(rs.getInt("nume_protocollo"));
				vo.setAnnoRegistrazione(rs.getInt("anno_registrazione"));
				vo.setOggettoProtocollo(rs.getString("text_oggetto"));
				vo.setDestinatari(getDestinatariMessaggioUscita(connection,
						vo.getMailId()));
				list.add(vo);
			}
		} catch (Exception e) {
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return list;
	}

	public int countEmailIngresso(int aooId, Date dataInizio, Date dataFine,
			String statoMail) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int total = 0;
		try {
			connection = jdbcMan.getConnection();
			String strQuery = COUNT_MESSAGGI_INGRESSO_BY_AOO_ID
					+ " AND data_spedizione BETWEEN ? AND ? ";
			if (statoMail != null && !statoMail.equals(""))
				strQuery += "AND flag_stato=" + statoMail;
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, aooId);
			pstmt.setDate(2, new java.sql.Date(dataInizio.getTime()));
			pstmt.setTimestamp(3, new java.sql.Timestamp(dataFine.getTime()
					+ Constants.GIORNO_MILLISECONDS - 1));
			rs = pstmt.executeQuery();
			rs.next();
			total = rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return total;
	}

	public int countListaMessaggiUscita(int aooId, Date dataInizio,
			Date dataFine, String statoMail) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int total = 0;
		try {
			connection = jdbcMan.getConnection();
			String strQuery = COUNT_MESSAGGI_USCITA_BY_AOO_ID
					+ " AND data_creazione BETWEEN ? AND ? ";
			if (statoMail != null && !statoMail.equals(""))
				strQuery += "AND stato=" + statoMail;
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, aooId);
			pstmt.setDate(2, new java.sql.Date(dataInizio.getTime()));
			pstmt.setTimestamp(3, new java.sql.Timestamp(dataFine.getTime()
					+ Constants.GIORNO_MILLISECONDS - 1));
			rs = pstmt.executeQuery();
			rs.next();
			total = rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return total;
	}

	public Map<Integer, CodaInvioView> getListaMessaggiUscitaView(int aooId,
			Date dataInizio, Date dataFine, String statoMail)
			throws DataException {
		Map<Integer, CodaInvioView> list = new HashMap<Integer, CodaInvioView>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			String strQuery = SELECT_MESSAGGI_USCITA_BY_AOO_ID
					+ " AND em.data_creazione BETWEEN ? AND ? ";
			if (statoMail != null && !statoMail.equals(""))
				strQuery += "AND stato=" + statoMail;
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, aooId);
			pstmt.setDate(2, new java.sql.Date(dataInizio.getTime()));
			pstmt.setTimestamp(3, new java.sql.Timestamp(dataFine.getTime()
					+ Constants.GIORNO_MILLISECONDS - 1));
			rs = pstmt.executeQuery();

			while (rs.next()) {
				CodaInvioView vo = new CodaInvioView();
				vo.setMailId(rs.getInt("id"));
				vo.setDataCreazione(rs.getDate("data_creazione"));
				vo.setDataInvio(rs.getDate("data_invio"));
				vo.setStato(rs.getInt("stato"));
				vo.setProtocolloId(rs.getInt("protocollo_id"));
				vo.setNumeroProtocollo(rs.getInt("nume_protocollo"));
				vo.setAnnoRegistrazione(rs.getInt("anno_registrazione"));
				vo.setOggettoProtocollo(rs.getString("text_oggetto"));
				vo.setDestinatari(getDestinatariMessaggioUscita(connection,
						vo.getMailId()));
				list.put(vo.getMailId(), vo);
			}
		} catch (Exception e) {
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return list;
	}
	
	public EmailVO cercaEmailIngressoByMessageHeader(int aooId, String messageId, String generatedId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		EmailVO email = null;
		try {
			connection = jdbcMan.getConnection();
			String strQuery = SELECT_MESSAGGI_WMH_INGRESSO_BY_AOO_ID;
			strQuery += " and ( message_header_id = ? or message_header_id = ? "
					+ " or md5( to_char(data_spedizione, 'DD/MM/YYYY - HH24:MI') || ' ' || email_oggetto || ' ' || email_mittente ) = ? )";
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, aooId);
			pstmt.setString(2, messageId);
			pstmt.setString(3, generatedId);
			pstmt.setString(4, generatedId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				email = new EmailVO();
				email.setAooId(aooId);
				email.setId(rs.getInt("email_id"));
				email.setOggetto(rs.getString("email_oggetto"));
				if (rs.getTimestamp("data_spedizione") != null) {
					email.setDataSpedizione(new Date(rs.getTimestamp("data_spedizione").getTime()));
				}
				if (rs.getTimestamp("data_ricezione") != null) {
					email.setDataRicezione(new Date(rs.getTimestamp("data_ricezione").getTime()));
				}
				email.setEmailMittente(rs.getString("email_mittente"));
				email.setNomeMittente(rs.getString("nome_mittente"));
				email.setFlagAnomalia(rs.getInt("flag_anomalia"));
				email.setMessageIdHeader(rs.getString("message_header_id"));
			}
		} catch (Exception e) {
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return email;
	}

	public Collection<EmailView> cercaEmailIngresso(int aooId, Date dataInizio,
			Date dataFine, String statoMail) throws DataException {
		List<EmailView> list = new ArrayList<EmailView>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			String strQuery = SELECT_MESSAGGI_INGRESSO_BY_AOO_ID
					+ " AND e.data_spedizione BETWEEN ? AND ? ";
			if (statoMail != null && !statoMail.equals(""))
				strQuery += "AND flag_stato=" + statoMail;
			strQuery += " ORDER BY e.data_spedizione desc";
			pstmt = connection.prepareStatement(strQuery);
			pstmt.setInt(1, aooId);
			pstmt.setDate(2, new java.sql.Date(dataInizio.getTime()));
			pstmt.setTimestamp(3, new java.sql.Timestamp(dataFine.getTime()
					+ Constants.GIORNO_MILLISECONDS - 1));
			rs = pstmt.executeQuery();

			while (rs.next()) {
				EmailView email = new EmailView();
				email.setId(rs.getInt("email_id"));
				email.setTestoMessaggio(rs.getString("email_oggetto"));
				if (rs.getTimestamp("data_spedizione") != null)
					email.setDataSpedizione(DateUtil.formattaDataOra(rs
							.getTimestamp("data_spedizione").getTime()));
				else
					email.setDataRicezione(null);
				if (rs.getTimestamp("data_ricezione") != null)
					email.setDataRicezione(DateUtil.formattaDataOra(rs
							.getTimestamp("data_ricezione").getTime()));
				else
					email.setDataRicezione(null);
				email.setEmailMittente(rs.getString("email_mittente"));
				email.setNomeMittente(rs.getString("nome_mittente"));
				email.setNumeroAllegati(rs.getInt("allegati"));
				email.setFlagAnomalia(rs.getInt("flag_anomalia"));
				if (email.getNumeroAllegati() != 0)
					email.setDescrizioneAllegati(getDescrizioneAllegati(
							connection, email.getId()));
				list.add(email);
			}
		} catch (Exception e) {
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return list;
	}

	public CodaInvioView getMessaggioUscitaView(int mailId)
			throws DataException {
		CodaInvioView vo = new CodaInvioView();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// int id=1;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_MESSAGGIO_USCITA);
			pstmt.setInt(1, mailId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				vo.setMailId(rs.getInt("id"));
				vo.setDataCreazione(rs.getDate("data_creazione"));
				vo.setDataInvio(rs.getDate("data_invio"));
				vo.setStato(rs.getInt("stato"));
				vo.setNumeroProtocollo(rs.getInt("nume_protocollo"));
				vo.setDestinatari(getDestinatariMessaggioUscita(connection,
						vo.getMailId()));
			}
		} catch (Exception e) {
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return vo;
	}

	public CodaInvioView getMessaggioUscitaViewByProtocolloId(int protocolloId)
			throws DataException {
		CodaInvioView vo = new CodaInvioView();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_MESSAGGIO_USCITA_BY_PROTOCOLLO_ID);
			pstmt.setInt(1, protocolloId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				vo.setMailId(rs.getInt("id"));
				vo.setDataCreazione(rs.getDate("data_creazione"));
				vo.setDataInvio(rs.getDate("data_invio"));
				vo.setStato(rs.getInt("stato"));
			}
		} catch (Exception e) {
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return vo;
	}

	public void salvaAllegato(Connection connection, DocumentoVO documento,
			int email_id) throws DataException {

		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non e' valida.");
			}

			File in = new File(documento.getPath());
			FileInputStream fis = new FileInputStream(in);

			pstmt = connection.prepareStatement(INSERT_ALLEGATO);
			pstmt.setInt(1, documento.getId().intValue());
			pstmt.setInt(2, email_id);
			pstmt.setString(3, documento.getDescrizione());
			pstmt.setString(4, documento.getFileName());
			if (documento.getContentType() != null)
				if (documento.getContentType().length() > 100) {
					String contentType = documento.getContentType().substring(
							0, 99);
					logger.debug("salvaAllegato "
							+ documento.getId().intValue()
							+ ": Content Type length > 100: "
							+ documento.getContentType().length());
					documento.setContentType(contentType);
				}
			pstmt.setString(5, documento.getContentType());
			pstmt.setInt(6, documento.getSize());
			pstmt.setString(7, documento.getImpronta());
			pstmt.setBinaryStream(8, fis, (int) in.length());
			pstmt.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
			pstmt.setString(10, documento.getRowCreatedUser());
			pstmt.executeUpdate();
			fis.close();
			logger.debug("Allegato Email inserito - id="
					+ documento.getId().intValue());

		} catch (Exception e) {
			logger.error("Salva Allegato", e);
			e.printStackTrace();
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void salvaAllegatoUfficio(Connection connection,
			DocumentoVO documento, int email_id) throws DataException {

		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non e' valida.");
			}

			File in = new File(documento.getPath());
			FileInputStream fis = new FileInputStream(in);

			pstmt = connection.prepareStatement(INSERT_ALLEGATO_UFFICIO);
			pstmt.setInt(1, documento.getId().intValue());
			pstmt.setInt(2, email_id);
			pstmt.setString(3, documento.getDescrizione());
			pstmt.setString(4, documento.getFileName());
			if (documento.getContentType() != null)
				if (documento.getContentType().length() > 100) {
					String contentType = documento.getContentType().substring(
							0, 99);
					logger.debug("salvaAllegatoUfficio "
							+ documento.getId().intValue()
							+ ": Content Type length > 100: "
							+ documento.getContentType().length());
					documento.setContentType(contentType);
				}
			pstmt.setString(5, documento.getContentType());
			pstmt.setInt(6, documento.getSize());
			pstmt.setString(7, documento.getImpronta());
			pstmt.setBinaryStream(8, fis, (int) in.length());
			pstmt.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
			pstmt.setString(10, documento.getRowCreatedUser());
			pstmt.executeUpdate();
			fis.close();
			logger.debug("Allegato Email inserito - id="
					+ documento.getId().intValue());

		} catch (Exception e) {
			logger.error("Salva Allegato", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void salvaEmail(EmailVO email, Connection connection)
			throws DataException {
		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non valida.");
			}

			InputStream isSegnatura = null;
			int segnaturaSize = 0;
			if (email.getSegnatura() != null) {
				isSegnatura = new ByteArrayInputStream(email.getSegnatura()
						.getBytes());
				segnaturaSize = email.getSegnatura().length();

			} else {
				isSegnatura = new ByteArrayInputStream("".getBytes());
			}

			InputStream isTesto = null;

			int testoSize = 0;
			if (email.getTestoMessaggio() != null) {
				isTesto = new ByteArrayInputStream(email.getTestoMessaggio()
						.getBytes());
				testoSize = email.getTestoMessaggio().length();
			} else {
				isTesto = new ByteArrayInputStream("".getBytes());
			}

			pstmt = connection.prepareStatement(INSERT_EMAIL);

			pstmt.setInt(1, email.getId().intValue());
			pstmt.setString(2, email.getOggetto());
			pstmt.setString(3, "  ");
			if (email.getContentType() != null)
				if (email.getContentType().length() > 100) {
					String contentType = email.getContentType()
							.substring(0, 99);
					logger.debug("salvaEmail " + email.getId().intValue()
							+ ": Content Type length > 100: "
							+ email.getContentType().length());

					email.setContentType(contentType);
				}
			pstmt.setString(4, email.getContentType());
			pstmt.setInt(5, testoSize);
			pstmt.setString(6, "  ");
			pstmt.setBinaryStream(7, isTesto, testoSize);
			pstmt.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
			pstmt.setString(9, "auto");
			pstmt.setString(10, email.getEmailMittente());
			pstmt.setString(11, email.getNomeMittente());
			String oggetto = email.getOggetto();
			if (email.getOggetto() != null) {
				if (email.getOggetto().length() > 200)
					oggetto = email.getOggetto().substring(1, 199);
			}
			pstmt.setString(12, oggetto);

			if (email.getDataSpedizione() == null)
				pstmt.setNull(13, Types.DATE);
			else
				pstmt.setTimestamp(13, new Timestamp(email.getDataSpedizione()
						.getTime()));
			if (email.getDataRicezione() == null)
				pstmt.setNull(14, Types.DATE);
			else
				pstmt.setTimestamp(14, new Timestamp(email.getDataRicezione()
						.getTime()));

			pstmt.setBinaryStream(15, isSegnatura, segnaturaSize);
			pstmt.setInt(16, email.getAooId());
			pstmt.setString(17, email.getMessageIdHeader());
			pstmt.executeUpdate();
			isTesto.close();
			isSegnatura.close();

		} catch (Exception e) {
			logger.error("Salva Email", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void salvaEmailUfficio(EmailVO email, Connection connection,
			int ufficioId) throws DataException {
		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non valida.");
			}

			InputStream isSegnatura = null;
			int segnaturaSize = 0;
			if (email.getSegnatura() != null) {

				isSegnatura = new ByteArrayInputStream(email.getSegnatura()
						.getBytes());
				segnaturaSize = email.getSegnatura().length();

			} else {
				isSegnatura = new ByteArrayInputStream("".getBytes());
			}

			InputStream isTesto = null;

			int testoSize = 0;
			if (email.getTestoMessaggio() != null) {
				isTesto = new ByteArrayInputStream(email.getTestoMessaggio()
						.getBytes());
				testoSize = email.getTestoMessaggio().length();
			} else {
				isTesto = new ByteArrayInputStream("".getBytes());
			}

			pstmt = connection.prepareStatement(INSERT_EMAIL_UFFICIO);

			pstmt.setInt(1, email.getId().intValue());
			pstmt.setString(2, email.getOggetto());
			pstmt.setString(3, "  ");
			if (email.getContentType() != null)
				if (email.getContentType().length() > 100) {
					String contentType = email.getContentType()
							.substring(0, 99);
					logger.debug("salvaAllegatoUfficio "
							+ email.getId().intValue()
							+ ": Content Type length > 100: "
							+ email.getContentType().length());

					email.setContentType(contentType);
				}
			pstmt.setString(4, email.getContentType());
			pstmt.setInt(5, testoSize);
			pstmt.setString(6, "  ");
			pstmt.setBinaryStream(7, isTesto, testoSize);
			pstmt.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
			pstmt.setString(9, "auto");
			pstmt.setString(10, email.getEmailMittente());
			pstmt.setString(11, email.getNomeMittente());
			String oggetto = email.getOggetto();
			if (email.getOggetto() != null) {
				if (email.getOggetto().length() > 200)
					oggetto = email.getOggetto().substring(1, 199);
			}
			pstmt.setString(12, oggetto);

			if (email.getDataSpedizione() == null)
				pstmt.setNull(13, Types.DATE);
			else
				pstmt.setTimestamp(13, new Timestamp(email.getDataSpedizione()
						.getTime()));

			if (email.getDataRicezione() == null)
				pstmt.setNull(14, Types.DATE);
			else
				pstmt.setDate(14, new java.sql.Date(email.getDataRicezione()
						.getTime()));

			pstmt.setBinaryStream(15, isSegnatura, segnaturaSize);
			pstmt.setInt(16, email.getAooId());
			pstmt.setString(17, email.getMessageIdHeader());
			pstmt.setInt(18, ufficioId);
			pstmt.executeUpdate();
			isTesto.close();
			isSegnatura.close();

		} catch (Exception e) {
			logger.error("Salva Email", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void salvaEmailLog(Connection connection, int id, String errore,
			int tipoLog, int aooId) throws DataException {
		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non valida.");
			}

			pstmt = connection.prepareStatement(INSERT_EMAIL_LOG);

			pstmt.setInt(1, id);
			pstmt.setString(2, errore);
			pstmt.setInt(3, tipoLog);
			pstmt.setInt(4, aooId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("Salva Email Log", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void aggiornaEmailLog(Connection connection, String errore,
			int tipoLog, int aooId) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non valida.");
			}
			pstmt = connection.prepareStatement(UPDATE_EMAIL_LOG);
			pstmt.setString(1, errore);
			pstmt.setInt(2, tipoLog);
			pstmt.setInt(3, aooId);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("Salva Email Log", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void aggiornaFlagAnomalia(int messaggioId, Connection connection)
			throws DataException {

		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non valida.");
			}
			pstmt = connection.prepareStatement(UPDATE_FLAG_ANOMALIA);
			pstmt.setInt(1, 1);
			pstmt.setInt(2, messaggioId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("aggiornaStatoEmailIngresso", e);
			throw new DataException(
					"Impossibile aggiornare lo stato del messaggio sulla base dati.");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void aggiornaStatoEmailIngresso(Connection connection,
			int messaggioId, int stato) throws DataException {

		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non valida.");
			}
			pstmt = connection.prepareStatement(UPDATE_STATO_MESSAGGIO_ENTRATA);
			pstmt.setInt(1, stato);
			pstmt.setInt(2, messaggioId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("aggiornaStatoEmailIngresso", e);
			throw new DataException(
					"Impossibile aggiornare lo stato del messaggio sulla base dati.");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void aggiornaStatoEmailUfficioIngresso(Connection connection,
			int messaggioId, int stato) throws DataException {

		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non valida.");
			}
			pstmt = connection
					.prepareStatement(UPDATE_STATO_MESSAGGIO_ENTRATA_UFFICIO);
			pstmt.setInt(1, stato);
			pstmt.setInt(2, messaggioId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("aggiornaStatoEmailIngresso", e);
			throw new DataException(
					"Impossibile aggiornare lo stato del messaggio sulla base dati.");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public EmailVO getEmailEntrata(int emailId) throws DataException {
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			return getEmailEntrata(connection, emailId);
		} catch (Exception e) {
			logger.error("", e);
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(connection);
		}
	}

	public EmailVO getEmailEntrata(Connection connection, int emailId)
			throws DataException {
		EmailVO vo = new EmailVO();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_MESSAGGIO_ENTRATA);
			pstmt.setInt(1, emailId);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				vo.setId(emailId);
				vo.setOggetto(rs.getString("email_oggetto"));
				vo.setContentType(rs.getString("content_type"));
				vo.setDataRicezione(rs.getTimestamp("data_ricezione"));
				vo.setDataSpedizione(rs.getTimestamp("data_spedizione"));
				vo.setEmailMittente(rs.getString("email_mittente"));
				vo.setNomeMittente(rs.getString("nome_mittente"));
				vo.setFlagEliminato(rs.getInt("flag_stato"));
				vo.setFlagAnomalia(rs.getInt("flag_anomalia"));
				vo.setRowCreatedTime(rs.getDate("row_created_time"));
				vo.setRowCreatedUser(rs.getString("row_created_user"));
				InputStream isTesto = rs.getBinaryStream("testo_messaggio");
				ByteArrayOutputStream bosTesto = new ByteArrayOutputStream();
				FileUtil.writeFile(isTesto, bosTesto);
				isTesto.close();
				vo.setTestoMessaggio(new String(bosTesto.toByteArray()));
				bosTesto.close();
				InputStream isSegnatura = rs.getBinaryStream("segnatura");
				ByteArrayOutputStream bosSegnatura = new ByteArrayOutputStream();
				FileUtil.writeFile(isSegnatura, bosSegnatura);
				isSegnatura.close();
				vo.setSegnatura(new String(bosSegnatura.toByteArray()));
				bosSegnatura.close();
				vo.setReturnValue(ReturnValues.FOUND);
			} else {
				vo.setReturnValue(ReturnValues.NOT_FOUND);
			}
		} catch (Exception e) {
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return vo;
	}

	public EmailVO getEmailUfficioEntrata(Connection connection, int emailId)
			throws DataException {
		EmailVO vo = new EmailVO();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_MAIL_UFFICIO_ENTRATA);
			pstmt.setInt(1, emailId);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				vo.setId(emailId);
				vo.setOggetto(rs.getString("email_oggetto"));
				vo.setContentType(rs.getString("content_type"));
				vo.setDataRicezione(rs.getTimestamp("data_ricezione"));
				vo.setDataSpedizione(rs.getTimestamp("data_spedizione"));
				vo.setEmailMittente(rs.getString("email_mittente"));
				vo.setNomeMittente(rs.getString("nome_mittente"));
				vo.setFlagEliminato(rs.getInt("flag_stato"));
				vo.setRowCreatedTime(rs.getDate("row_created_time"));
				vo.setRowCreatedUser(rs.getString("row_created_user"));
				InputStream isTesto = rs.getBinaryStream("testo_messaggio");
				ByteArrayOutputStream bosTesto = new ByteArrayOutputStream();
				FileUtil.writeFile(isTesto, bosTesto);
				isTesto.close();
				vo.setTestoMessaggio(new String(bosTesto.toByteArray()));
				bosTesto.close();
				InputStream isSegnatura = rs.getBinaryStream("segnatura");
				ByteArrayOutputStream bosSegnatura = new ByteArrayOutputStream();
				FileUtil.writeFile(isSegnatura, bosSegnatura);
				isSegnatura.close();
				vo.setSegnatura(new String(bosSegnatura.toByteArray()));
				bosSegnatura.close();
				vo.setReturnValue(ReturnValues.FOUND);
			} else {
				vo.setReturnValue(ReturnValues.NOT_FOUND);
			}
		} catch (Exception e) {
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return vo;
	}

	public ArrayList<DocumentoVO> getAllegatiEmailEntrata(
			Connection connection, int emailId) throws DataException {
		ArrayList<DocumentoVO> allegati = new ArrayList<DocumentoVO>(1);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non valida.");
			}
			pstmt = connection
					.prepareStatement(SELECT_ALLEGATI_MESSAGGIO_ENTRATA);
			pstmt.setInt(1, emailId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				DocumentoVO doc = new DocumentoVO();
				doc.setReturnValue(ReturnValues.FOUND);
				doc.setId(rs.getInt("id"));
				doc.setDescrizione(rs.getString("descrizione"));
				doc.setFileName(rs.getString("filename"));
				doc.setContentType(rs.getString("content_type"));
				doc.setSize(rs.getInt("dimensione"));
				doc.setImpronta(rs.getString("impronta"));
				doc.setRowCreatedTime(rs.getDate("row_created_time"));
				doc.setRowCreatedUser("row_created_user");
				allegati.add(doc);
			}
		} catch (Exception e) {
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return allegati;
	}

	public ArrayList<DocumentoVO> getAllegatiEmailUfficioEntrata(
			Connection connection, int emailId) throws DataException {
		ArrayList<DocumentoVO> allegati = new ArrayList<DocumentoVO>(1);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non valida.");
			}
			pstmt = connection
					.prepareStatement(SELECT_ALLEGATI_MAIL_UFFICIO_ENTRATA);
			pstmt.setInt(1, emailId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				DocumentoVO doc = new DocumentoVO();
				doc.setReturnValue(ReturnValues.FOUND);
				doc.setId(rs.getInt("id"));
				doc.setDescrizione(rs.getString("descrizione"));
				doc.setFileName(rs.getString("filename"));
				doc.setContentType(rs.getString("content_type"));
				doc.setSize(rs.getInt("dimensione"));
				doc.setImpronta(rs.getString("impronta"));
				doc.setRowCreatedTime(rs.getDate("row_created_time"));
				doc.setRowCreatedUser("row_created_user");
				allegati.add(doc);
			}
		} catch (Exception e) {
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return allegati;
	}

	public DocumentoVO getAllegatoEmailEntrata(int docId) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DocumentoVO doc = new DocumentoVO();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection
					.prepareStatement(SELECT_ALLEGATO_MESSAGGIO_ENTRATA);
			pstmt.setInt(1, docId);
			rs = pstmt.executeQuery();
			while (rs.next()) {

				doc.setReturnValue(ReturnValues.FOUND);
				doc.setId(rs.getInt("id"));
				doc.setDescrizione(rs.getString("descrizione"));
				doc.setFileName(rs.getString("filename"));
				doc.setContentType(rs.getString("content_type"));
				doc.setSize(rs.getInt("dimensione"));
				doc.setImpronta(rs.getString("impronta"));
				doc.setRowCreatedTime(rs.getDate("row_created_time"));
				doc.setRowCreatedUser("row_created_user");
				// doc.set
			}
		} catch (Exception e) {
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);

		}
		return doc;
	}

	public DocumentoVO getAllegatoEmailUfficio(int docId) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DocumentoVO doc = new DocumentoVO();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection
					.prepareStatement(SELECT_ALLEGATO_MESSAGGIO_UFFICIO);
			pstmt.setInt(1, docId);
			rs = pstmt.executeQuery();
			while (rs.next()) {

				doc.setReturnValue(ReturnValues.FOUND);
				doc.setId(rs.getInt("id"));
				doc.setDescrizione(rs.getString("descrizione"));
				doc.setFileName(rs.getString("filename"));
				doc.setContentType(rs.getString("content_type"));
				doc.setSize(rs.getInt("dimensione"));
				doc.setImpronta(rs.getString("impronta"));
				doc.setRowCreatedTime(rs.getDate("row_created_time"));
				doc.setRowCreatedUser("row_created_user");
				// doc.set
			}
		} catch (Exception e) {
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);

		}
		return doc;
	}

	private ArrayList<DocumentoVO> getDescrizioneAllegati(
			Connection connection, int emailId) throws DataException {
		ArrayList<DocumentoVO> allegati = new ArrayList<DocumentoVO>(1);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non valida.");
			}

			pstmt = connection.prepareStatement(SELECT_DESCRIZIONE_ALLEGATI);
			pstmt.setInt(1, emailId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				DocumentoVO doc = new DocumentoVO();
				doc.setReturnValue(ReturnValues.FOUND);
				doc.setId(rs.getInt("id"));
				doc.setDescrizione(rs.getString("descrizione"));
				allegati.add(doc);
			}
		} catch (Exception e) {
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return allegati;
	}

	private ArrayList<DocumentoVO> getDescrizioneAllegatiUfficio(
			Connection connection, int emailId) throws DataException {
		ArrayList<DocumentoVO> allegati = new ArrayList<DocumentoVO>(1);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non valida.");
			}

			pstmt = connection
					.prepareStatement(SELECT_DESCRIZIONE_ALLEGATI_UFFICIO);
			pstmt.setInt(1, emailId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				DocumentoVO doc = new DocumentoVO();
				doc.setReturnValue(ReturnValues.FOUND);
				doc.setId(rs.getInt("id"));
				doc.setDescrizione(rs.getString("descrizione"));
				allegati.add(doc);
			}
		} catch (Exception e) {
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return allegati;
	}

	public void writeDocumentoToStream(Connection connection, int docId,
			OutputStream os) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non valida.");
			}
			pstmt = connection
					.prepareStatement(SELECT_ALLEGATO_MESSAGGIO_ENTRATA);
			pstmt.setInt(1, docId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				InputStream in = rs.getBinaryStream("data");
				byte[] buffer = new byte[32768];
				int n = 0;
				while ((n = in.read(buffer)) != -1)
					os.write(buffer, 0, n);
				in.close();
			} else {
				throw new DataException("Documento non trovato.");
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
	}

	private InputStream writeDocumentoToInputStream(Connection connection,
			int docId) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		InputStream is = null;
		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non valida.");
			}
			pstmt = connection
					.prepareStatement(SELECT_ALLEGATO_MESSAGGIO_ENTRATA);
			pstmt.setInt(1, docId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				is = rs.getBinaryStream("data");
			} else {
				throw new DataException("Documento non trovato.");
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return is;
	}

	public void writeAllegatoUfficioToStream(Connection connection, int docId,
			OutputStream os) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non valida.");
			}
			pstmt = connection
					.prepareStatement(SELECT_ALLEGATO_MAIL_UFFICIO_ENTRATA);
			pstmt.setInt(1, docId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				InputStream in = rs.getBinaryStream("data");
				byte[] buffer = new byte[32768];
				int n = 0;
				while ((n = in.read(buffer)) != -1)
					os.write(buffer, 0, n);
				in.close();
			} else {
				throw new DataException("Documento non trovato.");
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
	}

	public void writeDocumentoToStream(int docId, OutputStream os)
			throws DataException {
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			writeDocumentoToStream(connection, docId, os);
		} catch (Exception e) {
			logger.error("", e);
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(connection);
		}
	}

	public InputStream writeDocumentoToInputStream(int docId)
			throws DataException {
		Connection connection = null;
		InputStream is = null;
		try {
			connection = jdbcMan.getConnection();
			is = writeDocumentoToInputStream(connection, docId);
		} catch (Exception e) {
			logger.error("", e);
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(connection);
		}
		return is;
	}

	public void writeAllegatoUfficioToStream(int docId, OutputStream os)
			throws DataException {
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			writeAllegatoUfficioToStream(connection, docId, os);
		} catch (Exception e) {
			logger.error("", e);
			throw new DataException(e.getMessage());
		} finally {
			jdbcMan.close(connection);
		}
	}

	public Collection<EmailView> getMessaggiDaProtocollare(int aooId)
			throws DataException {

		Collection<EmailView> email = new ArrayList<EmailView>();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			email = getMessaggiDaProtocollare(connection, aooId);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " getEmail");
		} finally {
			jdbcMan.close(connection);
		}
		return email;
	}

	public Collection<EmailView> getEmailIngressoByStato(int aooId, int stato)
			throws DataException {

		Collection<EmailView> email = new ArrayList<EmailView>();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			email = getEmailIngressoByStato(connection, aooId, stato);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " getMessaggiByStato");
		} finally {
			jdbcMan.close(connection);
		}
		return email;
	}

	public Collection<EmailView> getMessaggiUfficioDaProtocollare(int ufficioId)
			throws DataException {

		Collection<EmailView> email = new ArrayList<EmailView>();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			email = getMessaggiUfficioDaProtocollare(connection, ufficioId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException(e.getMessage() + " getEmail");
		} finally {
			jdbcMan.close(connection);
		}
		return email;
	}

	public int countMessaggiUfficioDaProtocollare(int ufficioId)
			throws DataException {
		int count = 0;
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			count = countMessaggiUfficioDaProtocollare(connection, ufficioId);
		} catch (Exception e) {
			throw new DataException(e.getMessage()
					+ " countMessaggiUfficioDaProtocollare");
		} finally {
			jdbcMan.close(connection);
		}
		return count;
	}

	public Collection<EmailView> getMessaggiDaProtocollare(
			Connection connection, int aooId) throws DataException {
		ArrayList<EmailView> listaEmail = new ArrayList<EmailView>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			pstmt = connection
					.prepareStatement(SELECT_MESSAGGI_ENTRATA_NON_PROTOCOLLATI);
			pstmt.setInt(1, aooId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				EmailView email = new EmailView();
				email.setId(rs.getInt("email_id"));
				email.setTestoMessaggio(rs.getString("email_oggetto"));
				if (rs.getTimestamp("data_spedizione") != null)
					email.setDataSpedizione(DateUtil.formattaDataOra(rs
							.getTimestamp("data_spedizione").getTime()));
				else
					email.setDataRicezione(null);
				if (rs.getTimestamp("data_ricezione") != null)
					email.setDataRicezione(DateUtil.formattaDataOra(rs
							.getTimestamp("data_ricezione").getTime()));
				else
					email.setDataRicezione(null);
				email.setEmailMittente(rs.getString("email_mittente"));
				email.setNomeMittente(rs.getString("nome_mittente"));
				email.setNumeroAllegati(rs.getInt("allegati"));
				email.setFlagAnomalia(rs.getInt("flag_anomalia"));
				if (email.getNumeroAllegati() != 0)
					email.setDescrizioneAllegati(getDescrizioneAllegati(
							connection, email.getId()));
				listaEmail.add(email);
			}
		} catch (Exception e) {
			logger.error("getListaEmail", e);
			throw new DataException("Cannot load getListaEmail");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return listaEmail;
	}

	public Collection<EmailView> getEmailIngressoByStato(Connection connection,
			int aooId, int stato) throws DataException {
		ArrayList<EmailView> listaEmail = new ArrayList<EmailView>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			pstmt = connection.prepareStatement(SELECT_EMAIL_BY_STATO);
			pstmt.setInt(1, aooId);
			pstmt.setInt(2, stato);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				EmailView email = new EmailView();
				email.setId(rs.getInt("email_id"));
				email.setTestoMessaggio(rs.getString("email_oggetto"));
				if (rs.getTimestamp("data_spedizione") != null)
					email.setDataSpedizione(DateUtil.formattaDataOra(rs
							.getTimestamp("data_spedizione").getTime()));
				else
					email.setDataRicezione(null);
				if (rs.getTimestamp("data_ricezione") != null)
					email.setDataRicezione(DateUtil.formattaDataOra(rs
							.getTimestamp("data_ricezione").getTime()));
				else
					email.setDataRicezione(null);
				email.setEmailMittente(rs.getString("email_mittente"));
				email.setNomeMittente(rs.getString("nome_mittente"));
				email.setNumeroAllegati(rs.getInt("allegati"));
				email.setFlagAnomalia(rs.getInt("flag_anomalia"));
				if (email.getNumeroAllegati() != 0)
					email.setDescrizioneAllegati(getDescrizioneAllegati(
							connection, email.getId()));
				listaEmail.add(email);
			}
		} catch (Exception e) {
			logger.error("getListaEmail", e);
			throw new DataException("Cannot load getListaEmail");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return listaEmail;
	}

	public Collection<EmailView> getMessaggiUfficioDaProtocollare(
			Connection connection, int ufficioId) throws DataException {
		ArrayList<EmailView> listaEmail = new ArrayList<EmailView>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			pstmt = connection
					.prepareStatement(SELECT_MAIL_UFFICIO_ENTRATA_NON_PROTOCOLLATI);
			pstmt.setInt(1, ufficioId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				EmailView email = new EmailView();
				email.setId(rs.getInt("email_id"));
				email.setTestoMessaggio(rs.getString("email_oggetto"));
				if (rs.getTimestamp("data_spedizione") != null)
					email.setDataSpedizione(DateUtil.formattaData(rs
							.getTimestamp("data_spedizione").getTime()));
				else
					email.setDataSpedizione(null);
				email.setEmailMittente(rs.getString("email_mittente"));
				email.setNomeMittente(rs.getString("nome_mittente"));
				email.setNumeroAllegati(rs.getInt("allegati"));
				if (email.getNumeroAllegati() != 0)
					email.setDescrizioneAllegati(getDescrizioneAllegatiUfficio(
							connection, email.getId()));
				listaEmail.add(email);
			}
		} catch (Exception e) {
			logger.error("getListaEmail", e);
			throw new DataException("Cannot load getListaEmail");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return listaEmail;
	}

	public int countMessaggiUfficioDaProtocollare(Connection connection,
			int ufficioId) throws DataException {
		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection
					.prepareStatement(SELECT_COUNT_MAIL_UFFICIO_ENTRATA_NON_PROTOCOLLATI);
			pstmt.setInt(1, ufficioId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("getListaEmail", e);
			throw new DataException("Cannot load getListaEmail");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return count;
	}

	public void eliminaEmail(Connection connection, int emailId)
			throws DataException {
		PreparedStatement pstmt = null;

		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non valida.");
			}
			if (emailId > 0) {
				pstmt = connection
						.prepareStatement("DELETE FROM email_ingresso_allegati WHERE email_id=?");
				pstmt.setInt(1, emailId);
				pstmt.executeUpdate();
				pstmt.close();

				pstmt = connection
						.prepareStatement("DELETE FROM email_ingresso WHERE email_id=?");
				pstmt.setInt(1, emailId);
				pstmt.executeUpdate();
			}

		} catch (Exception e) {
			logger.error("elimina Email", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}

	}

	public void eliminaEmailCodaInvioDestinatari(Connection connection,
			int emailId) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non valida.");
			}
			if (emailId > 0) {
				pstmt = connection
						.prepareStatement("DELETE FROM email_coda_invio_destinatari WHERE msg_id=?");
				pstmt.setInt(1, emailId);
				pstmt.executeUpdate();
			}

		} catch (Exception e) {
			logger.error("elimina Email Uscita Destinatari", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}

	}

	public void eliminaEmailCodaInvio(Connection connection, int emailId)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non valida.");
			}
			if (emailId > 0) {
				pstmt = connection
						.prepareStatement("DELETE FROM email_coda_invio WHERE id=?");
				pstmt.setInt(1, emailId);
				pstmt.executeUpdate();
			}

		} catch (Exception e) {
			logger.error("elimina Email Uscita", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}

	}

	public void eliminaEmailAllegati(Connection connection, int emailId)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non valida.");
			}
			if (emailId > 0) {
				pstmt = connection
						.prepareStatement("DELETE FROM email_ingresso_allegati WHERE email_id=?");
				pstmt.setInt(1, emailId);
				pstmt.executeUpdate();
			}

		} catch (Exception e) {
			logger.error("elimina Email", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}

	}

	public void eliminaEmailUfficioAllegati(Connection connection, int emailId)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non valida.");
			}
			if (emailId > 0) {
				pstmt = connection
						.prepareStatement("DELETE FROM email_ingresso_ufficio_allegati WHERE email_id=?");
				pstmt.setInt(1, emailId);
				pstmt.executeUpdate();
			}

		} catch (Exception e) {
			logger.error("elimina Email", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}

	}

	public void eliminaEmailLog(Connection connection, String[] ids)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("Connection is " + connection);
				throw new DataException("La connessione fornita non valida.");
			}
			if (ids != null) {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < ids.length; i++) {
					sb.append(ids[i]);
					if (i < ids.length - 1)
						sb.append(",");
				}
				pstmt = connection
						.prepareStatement("DELETE FROM email_logs WHERE email_id in("
								+ sb.toString() + ")");
				pstmt.executeUpdate();
			}

		} catch (Exception e) {
			logger.error("elimina email log ", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public boolean isMailPresent(Connection connection, EmailVO email)
			throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean used = false;
		try {
			if (connection == null) {
				logger.warn("isDescrizioneUsed - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");

			}
			pstmt = connection.prepareStatement(IS_EMAIL_PRESENT);
			pstmt.setString(1, email.getMessageIdHeader());
			rs = pstmt.executeQuery();
			rs.next();
			used = rs.getInt(1) > 0;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("isMailPresent:" + email.getOggetto(), e);
			throw new DataException("error.database.missing");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return used;
	}

	public boolean isMailLogPresent(Connection connection, int aooId)
			throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean used = false;
		try {
			if (connection == null) {
				logger.warn("isDescrizioneUsed - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");

			}
			pstmt = connection.prepareStatement(IS_EMAIL_LOG_PRESENT);
			pstmt.setInt(1, aooId);
			rs = pstmt.executeQuery();
			rs.next();
			used = rs.getInt(1) > 0;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("isMailLogPresent:" + aooId, e);
			throw new DataException("error.database.missing");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return used;
	}

	public boolean isMailUfficioPresent(Connection connection, EmailVO email,
			int ufficioId) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean used = false;
		try {
			if (connection == null) {
				logger.warn("isDescrizioneUsed - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");

			}
			pstmt = connection.prepareStatement(IS_EMAIL_UFFICIO_PRESENT);
			pstmt.setString(1, email.getMessageIdHeader());
			pstmt.setInt(2, ufficioId);
			rs = pstmt.executeQuery();
			rs.next();
			used = rs.getInt(1) > 0;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("isMailPresent:" + email.getOggetto(), e);
			throw new DataException("error.database.missing");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return used;
	}

	public void aggiornaEmailCodaInvio(Connection connection, int messaggioId,
			int stato) throws DataException {

		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non valida.");
			}
			pstmt = connection.prepareStatement(UPDATE_STATO_MESSAGGIO_USCITA);
			pstmt.setInt(1, stato);
			pstmt.setInt(2, messaggioId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("aggiornaStatoEmailIngresso", e);
			throw new DataException(
					"Impossibile aggiornare lo stato del messaggio sulla base dati.");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void aggiornaEmailCodaInvioDestinatario(Connection connection,
			int messaggioId, String nominativo, String indirizzoEmail)
			throws DataException {

		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("Connection is:" + connection);
				throw new DataException("La connessione fornita non valida.");
			}
			pstmt = connection
					.prepareStatement(UPDATE_MESSAGGIO_USCITA_DESTINATARIO);
			pstmt.setString(1, indirizzoEmail);
			pstmt.setInt(2, messaggioId);
			pstmt.setString(3, nominativo);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("aggiornaStatoEmailIngresso", e);
			throw new DataException(
					"Impossibile aggiornare lo stato del messaggio sulla base dati.");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

}