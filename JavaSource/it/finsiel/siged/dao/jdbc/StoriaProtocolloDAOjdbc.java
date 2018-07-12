package it.finsiel.siged.dao.jdbc;

import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.DocumentoDelegate;
import it.finsiel.siged.mvc.business.TitoliDestinatarioDelegate;
import it.finsiel.siged.mvc.integration.StoriaProtocolloDAO;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.presentation.helper.VersioneProtocolloView;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.StringUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public class StoriaProtocolloDAOjdbc implements StoriaProtocolloDAO {
	static Logger logger = Logger.getLogger(StoriaProtocolloDAOjdbc.class
			.getName());

	private JDBCManager jdbcMan = new JDBCManager();

	public Collection<VersioneProtocolloView> getScartoProtocollo(Utente utente, int protocolloId)
			throws DataException {
		return getListaProtocolli(utente, protocolloId, "1");
	}

	public Collection<VersioneProtocolloView> getStoriaProtocollo(Utente utente, int protocolloId)
			throws DataException {
		return getListaProtocolli(utente, protocolloId, "0");
	}

	public Collection<VersioneProtocolloView> getStoriaProtocollo(Utente utente, int protocolloId,
			String flagTipo) throws DataException {
		return getListaProtocolli(utente, protocolloId, "0", flagTipo);
	}

	private Collection<VersioneProtocolloView> getListaProtocolli(Utente utente, int protocolloId,
			String flagScarto) throws DataException {
		ArrayList<VersioneProtocolloView> storiaProtocollo = new ArrayList<VersioneProtocolloView>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sqlProtocolli = "SELECT * FROM STORIA_PROTOCOLLI WHERE"
					+ " registro_id =? AND protocollo_id=? AND flag_scarto=? ORDER BY VERSIONE desc";
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(sqlProtocolli);
			pstmt.setInt(1, utente.getRegistroInUso());
			pstmt.setInt(2, protocolloId);
			pstmt.setString(3, flagScarto);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				VersioneProtocolloView versioneProt = new VersioneProtocolloView();
				versioneProt.setProtocolloId(rs.getInt("protocollo_id"));
				versioneProt.setTipoProtocollo(rs.getString("FLAG_TIPO"));
				if (rs.getBoolean("flag_riservato")) {
					versioneProt.setOggetto(Parametri.PROTOCOLLO_RISERVATO);
				} else {
					versioneProt.setOggetto(rs.getString("TEXT_OGGETTO"));
				}
				versioneProt.setStatoProtocollo(rs
						.getString("STATO_PROTOCOLLO"));
				versioneProt.setVersione(rs.getInt("VERSIONE"));
				versioneProt.setDateUpdated(DateUtil.formattaDataOra(rs
						.getTimestamp("row_created_time").getTime()));
				versioneProt.setUserUpdated(rs.getString("row_created_user"));
				versioneProt.setCognomeMittente(rs
						.getString("desc_cognome_mittente"));
				versioneProt.setDocumentoId(rs.getInt("documento_id"));
				versioneProt.setEstremiAutorizzazione(rs.getString("text_estremi_autorizzazione"));
				storiaProtocollo.add(versioneProt);
			}
		} catch (Exception e) {
			logger.error("getStoriaProtocollo", e);
			throw new DataException("Cannot load getStoriaProtocollo");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return storiaProtocollo;

	}

	private void addStoriaProtocollo(VersioneProtocolloView versioneProt,Collection<VersioneProtocolloView> storiaProtocolloS){
		if(versioneProt.getVersione()==1 && versioneProt.getStatoProtocollo().equals("Registrato")){
			storiaProtocolloS.clear();
		}
		storiaProtocolloS.add(versioneProt);
	}
	
	private Collection<VersioneProtocolloView> getListaProtocolli(Utente utente, int protocolloId,
			String flagScarto, String flagTipo) throws DataException {
		Collection<VersioneProtocolloView> storiaProtocolloS = new ArrayList<VersioneProtocolloView>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sqlProtocolli = "";
			if (flagTipo.equals("U")) {
				sqlProtocolli = "SELECT s.* FROM STORIA_PROTOCOLLI s";
				sqlProtocolli += " WHERE registro_id =? AND s.protocollo_id=? ";
			}
			else {
				sqlProtocolli = "SELECT s.*, a.*,ut.nome as nome_assegnatario,uf.descrizione as ufficio_assegnatario,ut.cognome as cognome_assegnatario FROM STORIA_PROTOCOLLI s";
				sqlProtocolli += " LEFT JOIN storia_protocollo_assegnatari a ON a.protocollo_id=s.protocollo_id";
				sqlProtocolli += " LEFT JOIN uffici uf ON a.ufficio_assegnatario_id=uf.ufficio_id";
				//sqlProtocolli += " LEFT JOIN cariche c ON a.carica_assegnatario_id=c.carica_id";
				sqlProtocolli += " LEFT JOIN utenti ut ON a.utente_assegnatario_id=ut.utente_id ";
				sqlProtocolli += "WHERE registro_id =? AND s.protocollo_id=? "
						+ "AND S.protocollo_id=A.protocollo_id AND S.versione= a.versione";
			} 
			sqlProtocolli += " ORDER BY s.VERSIONE asc";
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(sqlProtocolli);
			if (flagTipo.equals("P"))
				pstmt.setInt(1, utente.getRegistroPostaInterna());
			else
				pstmt.setInt(1, utente.getRegistroUfficialeId());
			pstmt.setInt(2, protocolloId);
			VersioneProtocolloView versioneProt = null;
			StringBuffer dest_ass = null;
			rs = pstmt.executeQuery();
			int i=0;
			while (rs.next()) {
				i=i+1;
				
					if (versioneProt == null|| versioneProt.getVersione() != rs.getInt("VERSIONE")) {
						if (versioneProt != null) {
							dest_ass.append("</ul>");
							versioneProt.setAssegnatario(dest_ass.toString());
							addStoriaProtocollo(versioneProt,storiaProtocolloS);
						}
						dest_ass = new StringBuffer("<ul>");
						versioneProt = new VersioneProtocolloView();
						versioneProt.setProtocolloId(rs.getInt("protocollo_id"));
						versioneProt.setTipoProtocollo(rs.getString("FLAG_TIPO"));
						if (rs.getBoolean("flag_riservato")) {
							versioneProt
									.setOggetto(Parametri.PROTOCOLLO_RISERVATO);
							versioneProt
									.setRiservato(Parametri.PROTOCOLLO_RISERVATO);
						} else {
							versioneProt.setRiservato(null);
							versioneProt.setOggetto(rs.getString("TEXT_OGGETTO"));
						}
						versioneProt.setStatoProtocollo(rs.getString("STATO_PROTOCOLLO"));
						versioneProt.setVersione(rs.getInt("VERSIONE"));
						versioneProt.setEstremiAutorizzazione(rs.getString("text_estremi_autorizzazione"));
						versioneProt.setDateUpdated(DateUtil.formattaDataOra(rs.getTimestamp("row_created_time").getTime()));
						versioneProt.setUserUpdated(rs.getString("row_created_user"));
						if (rs.getString("flag_tipo_mittente").equals("F")) {
							versioneProt.setCognomeMittente(rs.getString("desc_cognome_mittente")
									+ " "+ StringUtil.getStringa(rs.getString("desc_nome_mittente")));
						} else if (rs.getString("flag_tipo_mittente").equals("G")) {
							versioneProt.setCognomeMittente(rs
									.getString("desc_denominazione_mittente"));
						}
						versioneProt.setDocumentoId(rs.getInt("documento_id"));
					}if (!rs.getBoolean("flag_riservato")) {
						dest_ass.append("<li>");
						if ("I".equals(versioneProt.getTipoProtocollo())) {
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
						} else if ("P".equals(versioneProt.getTipoProtocollo())) {
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
						} 
						dest_ass.append("</li>");
					} else {
						dest_ass = new StringBuffer(Parametri.PROTOCOLLO_RISERVATO);
					}
				//}
				
			}
			
			if (versioneProt != null) {
				dest_ass.append("</ul>");
				versioneProt.setAssegnatario(dest_ass.toString());
				addStoriaProtocollo(versioneProt,storiaProtocolloS);			
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getStoriaProtocollo", e);
			throw new DataException("Cannot load getStoriaProtocollo");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		Collections.reverse((List<VersioneProtocolloView>)storiaProtocolloS);
		return storiaProtocolloS;
		

	}

	public ProtocolloVO getVersioneProtocollo(int id, int versione)
			throws DataException {
		ProtocolloVO protOut = new ProtocolloVO();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			protOut = getVersioneProtocollo(connection, id, versione);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " ProtocolloId :" + id);
		} finally {
			jdbcMan.close(connection);
		}
		return protOut;
	}

	private ProtocolloVO getVersioneProtocollo(Connection connection, int id,
			int versione) throws DataException {
		ProtocolloVO protocollo = new ProtocolloVO();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("getVersioneProtocollo() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_VERSIONE_PROTOCOLLO);
			pstmt.setInt(1, id);
			pstmt.setInt(2, versione);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				protocollo.setId(rs.getInt("protocollo_id"));
				protocollo.setRowCreatedTime(rs.getDate("row_created_time"));
				protocollo.setRowCreatedUser(rs.getString("row_created_user"));
				protocollo.setVersione(rs.getInt("versione"));
				protocollo
						.setAnnoRegistrazione(rs.getInt("anno_registrazione"));
				protocollo.setAooId(rs.getInt("aoo_id"));
				protocollo.setChiaveAnnotazione(rs
						.getString("annotazione_chiave"));
				protocollo.setCodDocumentoDoc(rs
						.getString("codi_documento_doc"));
				protocollo.setCognomeMittente(rs
						.getString("desc_cognome_mittente"));
				protocollo.setDataAnnullamento(rs.getDate("data_annullamento"));
				protocollo.setDataDocumento(rs.getDate("data_documento"));
				protocollo.setDataEffettivaRegistrazione(rs
						.getDate("data_effettiva_registrazione"));
				protocollo.setDataProtocolloMittente(rs
						.getDate("data_protocollo_mittente"));
				protocollo.setDataRegistrazione(rs
						.getTimestamp("data_registrazione"));
				protocollo.setDataRicezione(rs.getDate("data_ricezione"));
				protocollo.setDataScarico(rs.getDate("data_scarico"));
				protocollo.setDenominazioneMittente(rs
						.getString("desc_denominazione_mittente"));
				protocollo.setDescrizioneAnnotazione(rs
						.getString("annotazione_descrizione"));
				protocollo.setDocumentoPrincipaleId(rs.getInt("documento_id"));
				protocollo.setMozione(rs.getInt("flag_mozione") == 1);
				protocollo.setRiservato(rs.getInt("flag_riservato") == 1);
				protocollo.setFlagTipo(rs.getString("flag_tipo"));
				protocollo.setFlagTipoMittente(rs
						.getString("flag_tipo_mittente"));
				protocollo.setMittenteCap(rs.getString("indi_cap_mittente"));
				protocollo.setMittenteComune(rs
						.getString("indi_localita_mittente"));
				protocollo.setMittenteIndirizzo(rs.getString("indi_mittente"));
				protocollo.setMittenteNazione(rs
						.getString("indi_nazione_mittente"));
				protocollo.setMittenteProvinciaId(rs
						.getInt("indi_provincia_mittente"));
				protocollo.setNomeMittente(rs.getString("desc_nome_mittente"));
				protocollo.setNotaAnnullamento(rs
						.getString("text_nota_annullamento"));
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
				protocollo.setUfficioMittenteId(rs
						.getInt("ufficio_mittente_id"));
				protocollo.setUfficioProtocollatoreId(rs
						.getInt("ufficio_protocollatore_id"));
				protocollo.setUtenteProtocollatoreId(rs.getInt("utente_protocollatore_id"));
				protocollo.setUtenteMittenteId(rs.getInt("utente_mittente_id"));
				protocollo.setCaricaProtocollatoreId(rs
						.getInt("carica_protocollatore_id"));
				protocollo.setReturnValue(ReturnValues.FOUND);
			} else {
				protocollo.setReturnValue(ReturnValues.NOT_FOUND);
			}

		} catch (Exception e) {
			logger.error("Load getVersioneProtocollo by ID", e);
			throw new DataException("Cannot load getVersioneProtocollo by ID");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return protocollo;
	}

	public Map<String,DocumentoVO> getAllegatiVersioneProtocollo(int protocolloId, int versione)
			throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String,DocumentoVO> docs = new HashMap<String,DocumentoVO>(2);
		Connection connection = null;
		DocumentoDelegate documentoDelegate = DocumentoDelegate.getInstance();
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(PROTOCOLLO_ALLEGATI);
			pstmt.setInt(1, protocolloId);
			pstmt.setInt(2, versione);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("documento_id");
				DocumentoVO doc = documentoDelegate
						.getDocumento(connection, id);
				docs.put(doc.getDescrizione(), doc);
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new DataException("Errore nella lettura dei documenti.");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return docs;
	}

	public Collection<AssegnatarioVO> getAssegnatariVersioneProtocollo(int protocolloId,
			int versione) throws DataException {
		ArrayList<AssegnatarioVO> assegnatari = new ArrayList<AssegnatarioVO>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(PROTOCOLLO_ASSEGNATARI);
			pstmt.setInt(1, protocolloId);
			pstmt.setInt(2, versione);
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
				//assegnatario.setUtenteAssegnatarioId(rs.getInt("utente_assegnatario_id"));
				assegnatario.setCompetente(rs.getBoolean("flag_competente"));
				assegnatario.setUtenteAssegnanteId(rs
						.getInt("utente_assegnante_id"));
				assegnatario.setStatoAssegnazione(rs.getString(
						"stat_assegnazione").charAt(0));
				assegnatari.add(assegnatario);
			}
		} catch (Exception e) {
			logger.error("getAllacci", e);
			throw new DataException("Cannot load getAssegnatari");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return assegnatari;
	}

	public Map<String,DestinatarioVO> getDestinatariVersioneProtocollo(int protocolloId, int versione)
			throws DataException {
		HashMap<String,DestinatarioVO> destinatari = new HashMap<String,DestinatarioVO>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(PROTOCOLLO_DESTINATARI);
			pstmt.setInt(1, protocolloId);
			pstmt.setInt(2, versione);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				DestinatarioVO destinatarioVO = new DestinatarioVO();
				destinatarioVO.setFlagTipoDestinatario(rs
						.getString("flag_tipo_destinatario"));
				destinatarioVO.setIndirizzo(rs.getString("indirizzo"));
				destinatarioVO.setEmail(rs.getString("email"));
				destinatarioVO.setDestinatario(rs.getString("DESTINATARIO"));
				int titoloId = rs.getInt("titolo_id");
				destinatarioVO.setTitoloId(rs.getInt("titolo_id"));
				TitoliDestinatarioDelegate tdd = TitoliDestinatarioDelegate
						.getInstance();
				String titoloDestinatario = tdd.getTitoloDestinatario(titoloId)
						.getDescription();
				destinatarioVO.setTitoloDestinatario(titoloDestinatario);
				destinatarioVO.setCitta(rs.getString("citta"));
				destinatarioVO.setDataSpedizione(rs.getDate("data_spedizione"));
				destinatarioVO.setFlagConoscenza("1".equals(rs
						.getString("flag_conoscenza")));
				destinatarioVO.setProtocolloId(rs.getInt("protocollo_id"));
				destinatarioVO.setDataEffettivaSpedizione(rs
						.getDate("data_effettiva_spedizione"));
				//
				destinatarioVO.setFlagPresso("1".equals(rs.getString("flag_presso")));
				destinatarioVO.setFlagPEC("1".equals(rs.getString("flag_pec")));
				//
				destinatarioVO.setVersione(rs.getInt("versione"));
				destinatari.put(destinatarioVO.getDestinatario(),
						destinatarioVO);
			}
		} catch (Exception e) {
			logger.error("getDestinatariProtocollo", e);
			throw new DataException("Cannot load getDestinatariProtocollo");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return destinatari;
	}

	private Collection<AllaccioVO> getAllacciVersioneProtocollo(Connection connection,
			int protocolloId, int versione) throws DataException {
		ArrayList<AllaccioVO> allacci = new ArrayList<AllaccioVO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(PROTOCOLLO_ALLACCI);
			pstmt.setInt(1, protocolloId);
			pstmt.setInt(2, versione);
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

	public Collection<AllaccioVO> getAllacciVersioneProtocollo(int protocolloId,
			int versione) throws DataException {
		Collection<AllaccioVO> allacci = new ArrayList<AllaccioVO>();
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			allacci = getAllacciVersioneProtocollo(connection, protocolloId,
					versione);
		} catch (Exception e) {
			throw new DataException(e.getMessage() + " ProtocolloId :"
					+ protocolloId);
		} finally {
			jdbcMan.close(connection);
		}
		return allacci;
	}

	public Collection<String> getAnniScartabili(int registroId) throws DataException {
		ArrayList<String> anni = new ArrayList<String>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection connection = null;
		int annoCorrente = DateUtil.getYear(new java.util.Date(System
				.currentTimeMillis()));
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection
					.prepareStatement("SELECT DISTINCT to_char(DATA_REGISTRAZIONE,'YYYY') as anno FROM PROTOCOLLI"
							+ " where registro_id=? AND to_char(DATA_REGISTRAZIONE,'YYYY') < '"
							+ annoCorrente + "'");
			pstmt.setInt(1, registroId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				anni.add(rs.getString("anno"));
			}
		} catch (Exception e) {
			logger.error("getAnniScartabili", e);
			throw new DataException("Cannot load getAnniScartabili");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return anni;
	}

	public int getNumProcolliNonScartabili(int registroId, int anno)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int total = 0;
		try {
			connection = jdbcMan.getConnection();
			// controllo i protocolli dell'anno selezionato
			pstmt = connection
					.prepareStatement("select count(*) from protocolli where "
							+ " registro_id=? and to_char(data_registrazione, 'YYYY')="
							+ anno
							+ " AND STATO_PROTOCOLLO NOT IN ('A','R','C')");
			pstmt.setInt(1, registroId);
			rs = pstmt.executeQuery();
			rs.next();
			total = rs.getInt(1);
			if (total == 0) {
				jdbcMan.closeResultSetAndStatement(rs, pstmt);
				pstmt = connection
						.prepareStatement("select count(*) from protocolli p "
								+ " where P.STATO_PROTOCOLLO NOT IN ('A','R','C') "
								+ "and protocollo_id IN"
								+ " 	(SELECT protocollo_allacciato_id from protocollo_allacci "
								+ "			where protocollo_id in"
								+ " (select protocollo_id from protocolli where registro_id=? and "
								+ " to_char(data_registrazione, 'YYYY')="
								+ anno + " ))");
				pstmt.setInt(1, registroId);
				rs = pstmt.executeQuery();
				rs.next();
				total = rs.getInt(1);

			}
		} catch (Exception e) {
			logger.error("getNumProcolliNonScartabili", e);
			throw new DataException("Cannot load getNumProcolliNonScartabili");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return total;
	}

	// aggiornaProtocolliScarto: IMPOSTA AD '1' IL FLAG_SCARTO,
	// ROW_UPDATED_USER='UTENTE' ROW_UPDATED_TIME

	private int aggiornaProtocolliScarto(Connection connection, Utente utente,
			int anno) throws DataException {
		PreparedStatement pstmt = null;
		int recordsScarto = 0;
		try {
			pstmt = connection
					.prepareStatement("UPDATE PROTOCOLLI SET FLAG_SCARTO='1', DATA_SCADENZA=?  "
							+ " WHERE registro_id =? AND to_char(data_registrazione, 'YYYY')=?");

			pstmt.setDate(1, new Date(System.currentTimeMillis()));
			pstmt.setInt(2, utente.getRegistroInUso());
			pstmt.setInt(3, anno);
			recordsScarto = pstmt.executeUpdate();
			logger.info("Records scartati dalla tabella Protocolli:"
					+ recordsScarto);

		} catch (Exception e) {
			logger.error("aggiornaProtocolliScarto", e);
			throw new DataException("Cannot load aggiornaProtocolliScarto");
		} finally {
			jdbcMan.close(pstmt);
		}
		return recordsScarto;
	}

	private void cancellaProtocolliScartati(Connection connection,
			int registroId, int anno) throws DataException {
		PreparedStatement pstmt = null;
		int recordsCancellati = 0;
		String[] tables = { "protocollo_allacci", "protocollo_allegati",
				"protocollo_annotazioni", "protocollo_assegnatari",
				"protocollo_destinatari", "segnature" };

		try {
			for (int i = 0; i < tables.length; i++) {
				String sql = "DELETE FROM "
						+ tables[i]
						+ " WHERE protocollo_id IN ("
						+ " SELECT protocollo_id FROM  protocolli WHERE registro_id =?"
						+ " AND FLAG_SCARTO='1'"
						+ " AND to_char(data_registrazione, 'YYYY')=?)";

				logger.info(sql);
				pstmt = connection.prepareStatement(sql);
				pstmt.setInt(1, registroId);
				pstmt.setInt(2, anno);
				recordsCancellati = pstmt.executeUpdate();
				jdbcMan.close(pstmt);
				logger.info("Records cancellati dalla tabella storia_"
						+ tables[i] + ":" + recordsCancellati);
			}

			pstmt = connection
					.prepareStatement("DELETE FROM PROTOCOLLI WHERE FLAG_SCARTO='1' AND registro_id =?"
							+ " AND to_char(data_registrazione, 'YYYY')=?");
			pstmt.setInt(1, registroId);
			pstmt.setInt(2, anno);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("cancellaProtocolliScartati", e);
			throw new DataException("Cannot load cancellaProtocolliScartati");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	private void copiaVersioneCorrenteInStoria(Connection connection,
			int registroId, int anno) throws DataException {
		String[] tables = { "protocollo_allacci", "protocollo_allegati",
				"protocollo_annotazioni", "protocollo_assegnatari",
				"protocollo_destinatari" };
		int recordsCopiatiInStoria = 0;
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger
						.warn("copiaVersioneCorrenteInStoria - Invalid Connection :"
								+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			String sql = "INSERT INTO storia_protocolli"
					+ " SELECT * FROM  protocolli WHERE registro_id =?"
					+ " AND FLAG_SCARTO='1'"
					+ " AND to_char(data_registrazione, 'YYYY')=?";

			logger.info(sql);
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, registroId);
			pstmt.setInt(2, anno);
			recordsCopiatiInStoria = pstmt.executeUpdate();
			jdbcMan.close(pstmt);
			logger
					.info("Records copiati da protocolli alla tabella storia_protocolli:"
							+ recordsCopiatiInStoria);

			for (int i = 0; i < tables.length; i++) {
				sql = "INSERT INTO storia_"
						+ tables[i]
						+ " SELECT * FROM "
						+ tables[i]
						+ " WHERE protocollo_id IN ("
						+ " SELECT protocollo_id FROM  protocolli WHERE registro_id =?"
						+ " AND FLAG_SCARTO='1'"
						+ " AND to_char(data_registrazione, 'YYYY')=?)";

				logger.info(sql);
				pstmt = connection.prepareStatement(sql);
				pstmt.setInt(1, registroId);
				pstmt.setInt(2, anno);
				recordsCopiatiInStoria = pstmt.executeUpdate();
				jdbcMan.close(pstmt);
				logger.info("Records copiati da " + tables[i]
						+ " alla tabella storia_" + tables[i] + ":"
						+ recordsCopiatiInStoria);

			}

		} catch (Exception e) {
			logger.error("storia copiaVersioneCorrenteInStoria REGISTRO: "
					+ registroId + " ANNO: " + anno, e);
			throw new DataException("Cannot insert Storia Protocollo");

		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public int scarto(Utente utente, int anno) throws DataException {
		Connection connection = null;
		int porotocolliScartati = 0;
		try {
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			porotocolliScartati = aggiornaProtocolliScarto(connection, utente,
					anno);
			copiaVersioneCorrenteInStoria(connection,
					utente.getRegistroInUso(), anno);
			cancellaProtocolliScartati(connection, utente.getRegistroInUso(),
					anno);
			connection.commit();
		} catch (Exception e) {
			porotocolliScartati = 0;
			logger.error("scarto", e);
			throw new DataException("Cannot load scarto");
		} finally {
			jdbcMan.close(connection);
		}
		return porotocolliScartati;
	}

	// Luigi 13/02/06
	public SortedMap<Integer,ReportProtocolloView> cercaProtocolliDaScartare(Utente utente, int ufficioId,
			int servizioId, java.util.Date dataRegDa, java.util.Date dataRegA)
			throws DataException {
		SortedMap<Integer,ReportProtocolloView> protocolli = new TreeMap<Integer,ReportProtocolloView>(new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				return o2.intValue() - o1.intValue();
			}
		});
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer strQuery = new StringBuffer(
				SELECT_LISTA_PROTOCOLLI_DA_SCARTARE);
		// Organizzazione org = Organizzazione.getInstance();
		// Ufficio uff = org.getUfficio(utente.getUfficioVOInUso().getId()
		// .intValue());
		if (dataRegDa != null) {
			strQuery.append(" AND (anno_registrazione>?) ");

		}
		if (dataRegA != null) {
			strQuery.append("AND (anno_registrazione<(?-t.massimario))");
		}

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
						dest_ass.append("</ul>");
						protocollo.setDestinatario(dest_ass.toString());
					}
					dest_ass = new StringBuffer("<ul>");
					protocollo = new ReportProtocolloView();
					protocollo.setProtocolloId(rs.getInt("protocollo_id"));
					protocollo.setAnnoProtocollo(rs
							.getInt("anno_registrazione"));
					protocollo
							.setNumeroProtocollo(rs.getInt("nume_protocollo"));
					protocollo.setTipoProtocollo(rs.getString("flag_tipo"));
					protocollo.setOggetto(rs.getString("text_oggetto"));
					StringBuffer mittente = new StringBuffer();
					if (rs.getString("desc_denominazione_mittente") != null)
						mittente.append(rs
								.getString("desc_denominazione_mittente"));
					if (rs.getString("desc_cognome_mittente") != null)
						mittente.append(rs.getString("desc_cognome_mittente"));
					if (rs.getString("desc_nome_mittente") != null)
						mittente.append(rs.getString("desc_nome_mittente"));
					protocollo.setMittente(mittente.toString());
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
						}
						if (rs.getBoolean("flag_competente")) {
							dest_ass.append("</em>");
						}
					} else {
						// if (rs.getInt("flag_conoscenza") == 0) {
						// dest_ass.append("<em>");
						// }
						dest_ass.append(rs.getString("destinatario"));
						// if (rs.getInt("flag_conoscenza") == 0) {
						// dest_ass.append("</em>");
						// }
					}
					dest_ass.append("</li>");
					protocollo.setDestinatario(dest_ass.toString());
					protocollo.setDataProtocollo(DateUtil.formattaData(rs
							.getDate("data_registrazione").getTime()));
					protocollo.setStatoProtocollo(rs
							.getString("stato_protocollo"));
					protocollo.setMassimario(rs.getInt("massimario"));
					protocolli.put(new Integer(protocollo.getProtocolloId()),
							protocollo);
				}

			}
			// if (protocollo != null) {
			// dest_ass.append("</ul>");
			// protocollo.setDestinatario(dest_ass.toString());
			// }

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

	public SortedMap<Integer,ReportProtocolloView> cercaScartoProtocolli(Utente utente, String tipo,
			String mozione, String stato, String riservato,
			java.util.Date dataRegDa, java.util.Date dataRegA, int numeroDa,
			int numeroA, int annoDa, int annoA) throws DataException {

		SortedMap<Integer,ReportProtocolloView> protocolli = new TreeMap<Integer,ReportProtocolloView>(new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				return o2.intValue() - o1.intValue();
			}
		});
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer strQuery = new StringBuffer(SELECT_LISTA_PROTOCOLLI);
		// Organizzazione org = Organizzazione.getInstance();
		// Ufficio uff = org.getUfficio(utente.getUfficioVOInUso().getId()
		// .intValue());
		if (tipo != null && !"".equals(tipo)) {
			strQuery.append(" AND FLAG_TIPO=?");
		}
		if (mozione != null && !"".equals(mozione)) {
			strQuery.append(" AND FLAG_MOZIONE=?");
		}
		if (stato != null && !"".equals(stato)) {
			strQuery.append(" AND STATO_PROTOCOLLO=?");
		}
		if (riservato != null && !"0".equals(riservato)) {
			strQuery.append(" AND flag_riservato=?");
		}

		if (dataRegDa != null) {
			strQuery.append(" AND data_registrazione >=?");
		}
		if (dataRegA != null) {
			strQuery.append(" AND data_registrazione <=?");
		}

		if (annoDa > 0) {
			strQuery.append(" AND ANNO_REGISTRAZIONE>= ?");
		}
		if (annoA > 0) {
			strQuery.append(" AND ANNO_REGISTRAZIONE<= ?");
		}
		if (numeroDa > 0) {
			strQuery.append(" AND NUME_PROTOCOLLO>= ?");
		}
		if (numeroA > 0) {
			strQuery.append(" AND NUME_PROTOCOLLO<= ?");
		}

		int indiceQuery = 1;

		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(strQuery.toString());
			pstmt.setInt(indiceQuery++, utente.getRegistroInUso());
			if (tipo != null && !"".equals(tipo)) {
				pstmt.setString(indiceQuery++, tipo);
			}
			if (mozione != null && !"".equals(mozione)) {
				pstmt.setString(indiceQuery++, stato);
			}
			if (stato != null && !"".equals(stato)) {
				pstmt.setString(indiceQuery++, stato);
			}
			if (riservato != null && !"0".equals(riservato)) {
				pstmt.setString(indiceQuery++, riservato);
			}

			if (dataRegDa != null) {
				pstmt.setDate(indiceQuery++, new java.sql.Date(dataRegDa
						.getTime()));
			}
			if (dataRegA != null) {
				pstmt.setDate(indiceQuery++, new java.sql.Date(dataRegA
						.getTime()));
			}

			if (annoDa > 0) {
				pstmt.setInt(indiceQuery++, annoDa);
			}
			if (annoA > 0) {
				pstmt.setInt(indiceQuery++, annoA);
			}
			if (numeroDa > 0) {
				pstmt.setInt(indiceQuery++, numeroDa);
			}
			if (numeroA > 0) {
				pstmt.setInt(indiceQuery++, numeroA);
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
					protocollo.setAnnoProtocollo(rs
							.getInt("anno_registrazione"));
					protocollo
							.setNumeroProtocollo(rs.getInt("nume_protocollo"));
					protocollo.setTipoProtocollo(rs.getString("flag_tipo"));

					if (rs.getBoolean("flag_riservato")) {
						protocollo.setOggetto(Parametri.PROTOCOLLO_RISERVATO);
						protocollo.setMittente(Parametri.PROTOCOLLO_RISERVATO);

					} else {
						protocollo.setOggetto(rs.getString("text_oggetto"));
						StringBuffer mittente = new StringBuffer();
						if ("F".equals(rs.getString("flag_tipo_mittente"))) {
							mittente.append(rs
									.getString("desc_cognome_mittente"));
							if (rs.getString("desc_nome_mittente") != null) {
								mittente.append(' ').append(
										rs.getString("desc_nome_mittente"));
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
					protocollo.setVersione(rs.getInt("versione"));
					protocolli.put(new Integer(protocollo.getProtocolloId()),
							protocollo);
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
			logger.error("cercaScartoProtocolli", e);
			throw new DataException("Cannot load cercaScartoProtocolli");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return protocolli;

	}

	public boolean isScartato(int protocolloId) throws DataException {
		Connection connection = null;
		boolean scartato = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			if (connection == null) {
				logger.warn("isScartato() - Invalid Connection :" + connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection
					.prepareStatement("SELECT count(*) from storia_protocolli"
							+ " where flag_scarto='1' AND protocollo_id=?");
			pstmt.setInt(1, protocolloId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt(1) > 0)
					scartato = true;
			}

		} catch (Exception e) {
			logger.error("Load isScartato by ID", e);
			throw new DataException("Cannot load isScartato by ID");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return scartato;
	}

	public Collection<ProtocolloProcedimentoVO> getVersioneProcedimentiProtocollo(int protocolloId,
			int versione) throws DataException {
		ArrayList<ProtocolloProcedimentoVO> procedimenti = new ArrayList<ProtocolloProcedimentoVO>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();

			pstmt = connection.prepareStatement(PROTOCOLLO_PROCEDIMENTI);
			pstmt.setInt(1, protocolloId);
			pstmt.setInt(2, versione);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ProtocolloProcedimentoVO procedimento = new ProtocolloProcedimentoVO();
				procedimento.setProtocolloId(protocolloId);
				procedimento.setProcedimentoId(rs.getInt("procedimento_id"));
				procedimento.setNumeroProcedimento(rs.getString("numero_procedimento"));
				procedimento.setModificabile(true);
				procedimenti.add(procedimento);
			}

			jdbcMan.close(rs);
			jdbcMan.close(pstmt);

			pstmt = connection.prepareStatement(PROCEDIMENTI_PROTOCOLLO);
			pstmt.setInt(1, protocolloId);
			pstmt.setInt(2, versione);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ProtocolloProcedimentoVO procedimento = new ProtocolloProcedimentoVO();
				procedimento.setProtocolloId(protocolloId);
				procedimento.setProcedimentoId(rs.getInt("procedimento_id"));
				procedimento.setNumeroProcedimento(rs.getString("numero_procedimento"));
				procedimento.setModificabile(false);
				procedimenti.add(procedimento);
			}

		} catch (Exception e) {
			logger.error("getProcedimentiProtocollo", e);
			throw new DataException("Cannot load procedimenti");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return procedimenti;
	}

	private final static String PROCEDIMENTI_PROTOCOLLO = "SELECT procedimento_id, numero, anno "
			+ "FROM procedimenti WHERE protocollo_id = ? and versione =?";

	private final static String PROTOCOLLO_PROCEDIMENTI = "SELECT p.*, r.anno, r.numero FROM storia_protocollo_procedimenti p, procedimenti r "
			+ "WHERE p.procedimento_id =r.procedimento_id and p.protocollo_id = ? and p.versione =?";

	public final static String SELECT_VERSIONE_PROTOCOLLO = "SELECT * FROM STORIA_PROTOCOLLI WHERE protocollo_id = ? AND versione=?";

	public final static String PROTOCOLLO_ALLEGATI = "SELECT documento_id FROM storia_protocollo_allegati WHERE protocollo_id = ? AND versione=?";

	private final static String PROTOCOLLO_ASSEGNATARI = "SELECT * FROM storia_protocollo_assegnatari"
			+ " WHERE protocollo_id = ? AND versione=?";

	// Selezione dei destinatari di un protocollo in uscita
	public final static String PROTOCOLLO_DESTINATARI = "SELECT * FROM STORIA_PROTOCOLLO_DESTINATARI"
			+ " WHERE protocollo_id=? AND versione=? ORDER BY DESTINATARIO";

	public final static String PROTOCOLLO_ALLACCI = "SELECT a.*, p.nume_protocollo, p.anno_registrazione, p.flag_tipo"
			+ " FROM storia_protocolli p, storia_protocollo_allacci a"
			+ " WHERE a.protocollo_allacciato_id = p.protocollo_id and a.protocollo_id = ? and a.versione =?";

	public final static String SELECT_LISTA_PROTOCOLLI = "SELECT "
			+ "p.protocollo_id,p.anno_registrazione,p.nume_protocollo,"
			+ "p.flag_tipo,p.flag_riservato,p.text_oggetto,p.data_registrazione,"
			+ "p.documento_id,p.stato_protocollo,p.flag_tipo_mittente,"
			+ "p.desc_cognome_mittente,p.desc_nome_mittente,"
			+ "p.desc_denominazione_mittente,p.versione,ut.nome as nome_assegnatario,"
			+ "uf.descrizione as ufficio_assegnatario,"
			+ "ut.cognome as cognome_assegnatario,"
			+ "a.flag_competente,d.destinatario,d.flag_conoscenza "
			+ "FROM storia_protocolli p"
			+ " LEFT JOIN storia_protocollo_assegnatari a ON a.protocollo_id=p.protocollo_id AND a.versione=p.versione"
			+ " LEFT JOIN uffici uf ON a.ufficio_assegnatario_id=uf.ufficio_id"
			+ " LEFT JOIN utenti ut ON a.utente_assegnatario_id=ut.utente_id"
			+ " LEFT JOIN storia_protocollo_destinatari d ON d.protocollo_id=p.protocollo_id AND d.versione=p.versione"
			+ " WHERE p.registro_id=? AND flag_scarto='1' ";

	public final static String SELECT_LISTA_PROTOCOLLI_DA_SCARTARE = "SELECT p.protocollo_id, "
			+ " p.anno_registrazione, p.nume_protocollo, p.flag_tipo, p.flag_riservato, p.text_oggetto,"
			+ " p.data_registrazione, p.documento_id, p.stato_protocollo, p.flag_tipo_mittente, "
			+ " p.desc_cognome_mittente, p.desc_nome_mittente, p.desc_denominazione_mittente, "
			+ " t.massimario, ut.nome as nome_assegnatario, uf.descrizione as ufficio_assegnatario, "
			+ " ut.cognome as cognome_assegnatario, a.flag_competente, d.destinatario, d.flag_conoscenza"
			+ " FROM protocolli p"
			+ " LEFT JOIN protocollo_assegnatari a ON a.protocollo_id=p.protocollo_id"
			+ " LEFT JOIN uffici uf ON a.ufficio_assegnatario_id=uf.ufficio_id"
			+ " LEFT JOIN utenti ut ON a.utente_assegnatario_id=ut.utente_id"
			+ " LEFT JOIN protocollo_destinatari d ON d.protocollo_id=p.protocollo_id "
			+ " JOIN titolario t ON (p.titolario_id=t.titolario_id) "
			+ " WHERE (carica_protocollatore_id=?) "
			+ " AND (p.titolario_id=?) ";

	public final static String SELECT_MASSIMARIO = "SELECT massimario FROM titolario "
			+ "WHERE (titolario_id=?)";

}