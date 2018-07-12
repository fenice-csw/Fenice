package it.compit.fenice.mvc.dao.jdbc;

import it.compit.fenice.mvc.integration.DomandaDAO;
import it.compit.fenice.mvc.presentation.helper.DomandaView;
import it.compit.fenice.mvc.vo.protocollo.DomandaVO;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.rdb.JDBCManager;
import it.finsiel.siged.util.DateUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public class DomandaDAOjdbc implements DomandaDAO {

	static Logger logger = Logger.getLogger(DomandaDAOjdbc.class.getName());

	private JDBCManager jdbcMan = new JDBCManager();
	
	private static final String SELECT_DOMANDE = "SELECT * FROM iscrizioni ";
	
	private static final String SELECT_DOMANDE_BY_UFFICIO_ID="SELECT i.* FROM iscrizioni i LEFT JOIN oggetti o ON(o.descrizione=i.oggetto) LEFT JOIN oggetto_assegnatari ass ON(o.id=ass.oggetto_id) WHERE ass.ufficio_id=?;";
	
	private static final String SELECT_DOMANDE_BY_STATO_AND_UFFICIO_ID = "SELECT i.* FROM iscrizioni i LEFT JOIN oggetti o ON(o.descrizione=i.oggetto) LEFT JOIN oggetto_assegnatari ass ON (o.id=ass.oggetto_id) WHERE ass.ufficio_id=? AND stato_domanda=?";
	
	private static final String SELECT_DOMANDE_BY_STATO = "SELECT * FROM iscrizioni where stato_domanda=?";
	
	private static final String SELECT_DOMANDE_AND_UFFICIO_BY_STATO="SELECT i.*, ass.ufficio_id FROM iscrizioni i LEFT JOIN oggetti o ON(o.descrizione=i.oggetto) LEFT JOIN oggetto_assegnatari ass ON (o.id=ass.oggetto_id) WHERE stato_domanda=?";
	
	private static final String SELECT_DOMANDA_BY_ID = "SELECT * FROM iscrizioni where id_domanda=?";

	private static final String SELECT_STATO_DOMANDA_BY_ID = "SELECT stato_domanda FROM iscrizioni where id_domanda=?";
	
	private static final String UPDATE_DOMANDA = "UPDATE iscrizioni SET stato_domanda=? WHERE id_domanda=?";

	private static final String SELECT_PROTOCOLLO_ID_BY_DOMANDA_ID = "select id_registrazione from iscrizioni_protocollate where id_domanda=?";

	private static final String INSERT_ISCRIZIONE_PROTOCOLLATA = "INSERT INTO iscrizioni_protocollate(id_domanda, id_registrazione, num_prot, data_prot)VALUES (?, ?, ?, ?);";

	

	public int getStato(Connection connection, String domandaId) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int stato=0;
		try {
			if (connection == null) {
				logger.warn("getStato - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_STATO_DOMANDA_BY_ID);
			pstmt.setString(1, domandaId);
			rs = pstmt.executeQuery();
			while (rs.next())
				stato = rs.getInt(1);
		} catch (Exception e) {
			logger.error("getStato:" + domandaId, e);
			throw new DataException("error.database.missing");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return stato;
	}
	
	
	public Map<String[],DomandaView> getDomande() throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DomandaView domanda = null;
		
		SortedMap<String[],DomandaView> lista = new TreeMap<String[],DomandaView>(new Comparator<String[]>() {
			public int compare(String[] i1, String[] i2) {
				if(i1[0].equals(i2[0]))
					return i2[1].compareTo(i1[1]);
				else
					return i2[0].compareTo(i1[0]);
			}
		});
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_DOMANDE);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				domanda = new DomandaView();
				domanda.setDomandaId(rs.getString("id_domanda"));
				domanda.setNome(rs.getString("nome"));
				domanda.setCognome(rs.getString("cognome"));
				domanda.setComune(rs.getString("comune"));
				domanda.setDataIscrizione(DateUtil.formattaData(rs.getDate("data_iscrizione").getTime()));
				domanda.setOggetto(rs.getString("oggetto"));
				domanda.setStato(rs.getInt("stato_domanda"));
				lista.put(domanda.getKey(), domanda);
			}
		} catch (Exception e) {
			logger.error("getDomande", e);
			throw new DataException("Cannot load getDomande");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return lista;
	}

	public Map<String[],DomandaView> getDomande(int ufficioId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DomandaView domanda = null;
		
		SortedMap<String[],DomandaView> lista = new TreeMap<String[],DomandaView>(new Comparator<String[]>() {
			public int compare(String[] i1, String[] i2) {
				if(i1[0].equals(i2[0]))
					return i2[1].compareTo(i1[1]);
				else
					return i2[0].compareTo(i1[0]);
			}
		});
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_DOMANDE_BY_UFFICIO_ID);
			pstmt.setInt(1, ufficioId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				domanda = new DomandaView();
				domanda.setDomandaId(rs.getString("id_domanda"));
				domanda.setNome(rs.getString("nome"));
				domanda.setCognome(rs.getString("cognome"));
				domanda.setComune(rs.getString("comune"));
				domanda.setDataIscrizione(DateUtil.formattaData(rs.getDate("data_iscrizione").getTime()));
				domanda.setOggetto(rs.getString("oggetto"));
				domanda.setStato(rs.getInt("stato_domanda"));
				lista.put(domanda.getKey(), domanda);
			}
		} catch (Exception e) {
			logger.error("getDomande", e);
			throw new DataException("Cannot load getDomande");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return lista;
	}
	
	public Map<String[],DomandaView> getDomandeByStato(int stato) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DomandaView domanda = null;
		SortedMap<String[],DomandaView> lista = new TreeMap<String[],DomandaView>(new Comparator<String[]>() {
			public int compare(String[] i1, String[] i2) {
				if(i1[0].equals(i2[0]))
					return i2[1].compareTo(i1[1]);
				else
					return i2[0].compareTo(i1[0]);
			}
		});
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_DOMANDE_BY_STATO);
			pstmt.setInt(1, stato);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				domanda = new DomandaView();
				domanda.setDomandaId(rs.getString("id_domanda"));
				domanda.setNome(rs.getString("nome"));
				domanda.setCognome(rs.getString("cognome"));
				domanda.setComune(rs.getString("comune"));
				domanda.setDataIscrizione(DateUtil.formattaData(rs.getDate("data_iscrizione").getTime()));
				domanda.setOggetto(rs.getString("oggetto"));
				domanda.setStato(rs.getInt("stato_domanda"));
				lista.put(domanda.getKey(), domanda);
			}
		} catch (Exception e) {
			logger.error("getDomande", e);
			throw new DataException("Cannot load getDomande");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return lista;
	}
	
	
	public Map<String[],DomandaView> getDomandeAndUfficioByStato(int stato) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DomandaView domanda = null;
		SortedMap<String[],DomandaView> lista = new TreeMap<String[],DomandaView>(new Comparator<String[]>() {
			public int compare(String[] i1, String[] i2) {
				if(i1[0].equals(i2[0]))
					return i2[1].compareTo(i1[1]);
				else
					return i2[0].compareTo(i1[0]);
			}
		});
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_DOMANDE_AND_UFFICIO_BY_STATO);
			pstmt.setInt(1, stato);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				domanda = new DomandaView();
				domanda.setDomandaId(rs.getString("id_domanda"));
				domanda.setNome(rs.getString("nome"));
				domanda.setCognome(rs.getString("cognome"));
				domanda.setComune(rs.getString("comune"));
				domanda.setDataIscrizione(DateUtil.formattaData(rs.getDate("data_iscrizione").getTime()));
				domanda.setOggetto(rs.getString("oggetto"));
				domanda.setStato(rs.getInt("stato_domanda"));
				domanda.setUfficioId(rs.getInt("ufficio_id"));
				lista.put(domanda.getKey(), domanda);
			}
		} catch (Exception e) {
			logger.error("getDomande", e);
			throw new DataException("Cannot load getDomande");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return lista;
	}
	
	public Map<String[],DomandaView> getDomandeByStato(int stato,int ufficioId) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DomandaView domanda = null;
		SortedMap<String[],DomandaView> lista = new TreeMap<String[],DomandaView>(new Comparator<String[]>() {
			public int compare(String[] i1, String[] i2) {
				if(i1[0].equals(i2[0]))
					return i2[1].compareTo(i1[1]);
				else
					return i2[0].compareTo(i1[0]);
			}
		});
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_DOMANDE_BY_STATO_AND_UFFICIO_ID);
			pstmt.setInt(1, ufficioId);
			pstmt.setInt(2, stato);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				domanda = new DomandaView();
				domanda.setDomandaId(rs.getString("id_domanda"));
				domanda.setNome(rs.getString("nome"));
				domanda.setCognome(rs.getString("cognome"));
				domanda.setComune(rs.getString("comune"));
				domanda.setDataIscrizione(DateUtil.formattaData(rs.getDate("data_iscrizione").getTime()));
				domanda.setOggetto(rs.getString("oggetto"));
				domanda.setStato(rs.getInt("stato_domanda"));
				lista.put(domanda.getKey(), domanda);
			}
		} catch (Exception e) {
			logger.error("getDomande", e);
			throw new DataException("Cannot load getDomande");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return lista;
	}
	
	
	public void newIscrizioneProtocollata(Connection connection,String domandaId,int protocolloId,int numeroprotocollo,Date dataProtocollo)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("newIscrizioneProtocollata - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(INSERT_ISCRIZIONE_PROTOCOLLATA);
			pstmt.setString(1, domandaId);
			pstmt.setInt(2, protocolloId);
			pstmt.setString(3, String.valueOf(numeroprotocollo));
			pstmt.setTimestamp(4,new Timestamp(dataProtocollo.getTime()));
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("new Iscrizione Protocollata", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}

	}

	public DomandaVO getDomandaById(String id) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DomandaVO domanda = new DomandaVO();
		domanda.setReturnValue(ReturnValues.UNKNOWN);
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_DOMANDA_BY_ID);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				domanda = new DomandaVO();
				domanda.setIdDomanda(rs.getString("id_domanda"));
				domanda.setNome(rs.getString("nome"));
				domanda.setCognome(rs.getString("cognome"));
				domanda.setComune(rs.getString("comune"));
				domanda.setDataIscrizione(rs.getDate("data_iscrizione"));
				domanda.setOggetto(rs.getString("oggetto"));
				domanda.setStatoDomanda(rs.getInt("stato_domanda"));
				domanda.setAnnoIscrizione(rs.getString("anno_iscrizione"));
				domanda.setCap(rs.getString("cap"));
				domanda.setCivico(rs.getString("civico"));
				domanda.setIndirizzo(rs.getString("indirizzo"));
				domanda.setProvincia(rs.getString("provincia"));
				domanda.setTipoIndirizzo(rs.getString("tipo_indirizzo"));
				domanda.setMatricola(rs.getString("matricola"));
				domanda.setDataNascita(rs.getDate("data_nascita"));
				domanda.setComuneNascita(rs.getString("comune_nascita"));
				domanda.setPath(rs.getString("path_documento"));
				domanda.setMail(rs.getString("email"));
				domanda.setReturnValue(ReturnValues.FOUND);
			} else {
				domanda.setReturnValue(ReturnValues.NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("Load Domanda ", e);
			throw new DataException("Cannot load the Domanda");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);

		}

		return domanda;
	}

	
	public List<DomandaVO> getDomandeVOByStato(int stato) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<DomandaVO> domandaList=new ArrayList<DomandaVO>();
		DomandaVO domanda = new DomandaVO();
		domanda.setReturnValue(ReturnValues.UNKNOWN);
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_DOMANDE_BY_STATO);
			pstmt.setInt(1, stato);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				domanda = new DomandaVO();
				domanda.setIdDomanda(rs.getString("id_domanda"));
				domanda.setNome(rs.getString("nome"));
				domanda.setCognome(rs.getString("cognome"));
				domanda.setComune(rs.getString("comune"));
				domanda.setDataIscrizione(rs.getDate("data_iscrizione"));
				domanda.setOggetto(rs.getString("oggetto"));
				domanda.setStatoDomanda(rs.getInt("stato_domanda"));
				domanda.setAnnoIscrizione(rs.getString("anno_iscrizione"));
				domanda.setCap(rs.getString("cap"));
				domanda.setCivico(rs.getString("civico"));
				domanda.setIndirizzo(rs.getString("indirizzo"));
				domanda.setProvincia(rs.getString("provincia"));
				domanda.setTipoIndirizzo(rs.getString("tipo_indirizzo"));
				domanda.setMatricola(rs.getString("matricola"));
				domanda.setDataNascita(rs.getDate("data_nascita"));
				domanda.setComuneNascita(rs.getString("comune_nascita"));
				domanda.setPath(rs.getString("path_documento"));
				domanda.setMail(rs.getString("email"));
				domandaList.add(domanda);
			} 
		} catch (Exception e) {
			logger.error("Load DomandaList ", e);
			throw new DataException("Cannot load the DomandaList");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);

		}

		return domandaList;
	}
	
	public DomandaView getDomandaViewById(String id) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DomandaView domanda = new DomandaView();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(SELECT_DOMANDA_BY_ID);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				domanda = new DomandaView();
				domanda.setDomandaId(rs.getString("id_domanda"));
				domanda.setNome(rs.getString("nome"));
				domanda.setCognome(rs.getString("cognome"));
				domanda.setComune(rs.getString("comune"));
				domanda.setDataIscrizione(DateUtil.formattaData(rs.getDate("data_iscrizione").getTime()));
				domanda.setOggetto(rs.getString("oggetto"));
				domanda.setStato(rs.getInt("stato_domanda"));
			} 
		} catch (Exception e) {
			logger.error("Load DomandaView ", e);
			throw new DataException("Cannot load the DomandaView");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);

		}

		return domanda;
	}
	
	public Integer getProtocolloIdByDomandaId(String id) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Integer protId = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection
					.prepareStatement(SELECT_PROTOCOLLO_ID_BY_DOMANDA_ID);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				protId=rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("Load Domanda ", e);
			throw new DataException("Cannot load the Domanda");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);

		}

		return protId;
	}

	public void updateStato(Connection connection,int stato, String domandaId) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("updateStato - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(UPDATE_DOMANDA);
			pstmt.setInt(1, stato);
			pstmt.setString(2, domandaId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("updateStato", e);
			throw new DataException("Cannot execute updateStato");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
	}
	
}
