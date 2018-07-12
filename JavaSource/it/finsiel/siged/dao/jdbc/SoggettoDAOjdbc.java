package it.finsiel.siged.dao.jdbc;

import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.integration.SoggettoDAO;
import it.finsiel.siged.mvc.vo.IdentityVO;
import it.finsiel.siged.mvc.vo.ListaDistribuzioneVO;
import it.finsiel.siged.mvc.vo.RubricaListaVO;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO.Indirizzo;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.rdb.JDBCManager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

public class SoggettoDAOjdbc implements SoggettoDAO {
	static Logger logger = Logger.getLogger(SoggettoDAOjdbc.class.getName());

	private JDBCManager jdbcMan = new JDBCManager();

	public int deleteSoggetto(Connection connection, long id)
			throws DataException {
		PreparedStatement pstmt = null;
		int recDel = 0;
		try {
			if (connection == null) {
				connection = jdbcMan.getConnection();
				connection.setAutoCommit(false);
			}
			pstmt = connection.prepareStatement(DELETE_SOGGETTO);
			pstmt.setLong(1, id);
			recDel = pstmt.executeUpdate();
			logger.info("Numero Soggetti eliminati dalla rubrica:" + recDel);

		} catch (Exception e) {
			logger.error("deleteSoggetto ", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}

		return recDel;
	}

	public SoggettoVO newPersonaFisica(Connection connection,
			SoggettoVO personaFisica) throws DataException {
		SoggettoVO newPF = new SoggettoVO('F');
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("newPersonaFisica() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(INSERT_PERSONA_FISICA);
			pstmt.setInt(1, personaFisica.getId().intValue());
			pstmt.setString(2, personaFisica.getCognome());
			pstmt.setString(3, personaFisica.getNome());
			pstmt.setString(4, personaFisica.getNote());
			pstmt.setString(5, personaFisica.getTelefono());
			pstmt.setString(6, personaFisica.getTeleFax());
			pstmt.setString(7, personaFisica.getIndirizzoWeb());
			pstmt.setString(8, personaFisica.getIndirizzoEMail());
			pstmt.setString(9, personaFisica.getIndirizzo().getCap());
			pstmt.setString(10, personaFisica.getIndirizzo().getComune());
			pstmt.setString(11, personaFisica.getCodMatricola());
			pstmt.setInt(12, personaFisica.getAoo());
			pstmt.setString(13, personaFisica.getQualifica());
			pstmt.setString(14, personaFisica.getIndirizzo().getToponimo());
			pstmt.setString(15, personaFisica.getIndirizzo().getCivico());
			if (personaFisica.getDataNascita() != null) {
				pstmt.setDate(16, new Date(personaFisica.getDataNascita()
						.getTime()));
			} else {
				pstmt.setDate(16, null);
			}
			pstmt.setString(17, personaFisica.getSesso());
			pstmt.setString(18, personaFisica.getComuneNascita());
			pstmt.setString(19, personaFisica.getCodiceFiscale());
			pstmt.setString(20, personaFisica.getStatoCivile());
			pstmt.setInt(21, personaFisica.getProvinciaNascitaId());
			pstmt.setInt(22, personaFisica.getIndirizzo().getProvinciaId());
			pstmt.setString(23, "F");
			pstmt.setString(24, personaFisica.getRowCreatedUser());
			if (personaFisica.getRowCreatedTime() != null) {
				pstmt.setDate(25, new Date(personaFisica.getRowCreatedTime()
						.getTime()));
			} else {
				pstmt.setDate(25, null);
			}
			pstmt.setString(26, personaFisica.getRowUpdatedUser());
			if (personaFisica.getRowUpdatedTime() != null) {
				pstmt.setDate(27, new Date(personaFisica.getRowUpdatedTime()
						.getTime()));
			} else {
				pstmt.setDate(27, null);
			}
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("Save Rubrica", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		newPF.setReturnValue(ReturnValues.SAVED);
		return newPF;

	}

	public SoggettoVO newPersonaGiuridica(Connection connection,
			SoggettoVO personaGiuridica) throws DataException {
		SoggettoVO newPG = new SoggettoVO('G');
		PreparedStatement pstmt = null;
		int recIns;
		try {
			if (connection == null) {
				logger.warn("newPersonaFisica() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}

			pstmt = connection.prepareStatement(INSERT_PERSONA_GIURIDICA);
			pstmt.setInt(1, personaGiuridica.getId().intValue());
			pstmt.setString(2, personaGiuridica.getDescrizioneDitta());
			pstmt.setString(3, personaGiuridica.getNote());
			pstmt.setString(4, personaGiuridica.getTelefono());
			pstmt.setString(5, personaGiuridica.getTeleFax());
			pstmt.setString(6, personaGiuridica.getIndirizzoWeb());
			pstmt.setString(7, personaGiuridica.getIndirizzoEMail());
			pstmt.setString(8, personaGiuridica.getIndirizzo().getCap());
			pstmt.setString(9, personaGiuridica.getIndirizzo().getComune());
			pstmt.setInt(10, personaGiuridica.getAoo());
			pstmt.setLong(11, personaGiuridica.getFlagSettoreAppartenenza());
			pstmt.setString(12, personaGiuridica.getDug());
			pstmt.setString(13, personaGiuridica.getIndirizzo().getToponimo());
			pstmt.setString(14, personaGiuridica.getIndirizzo().getCivico());
			pstmt.setString(15, personaGiuridica.getPartitaIva());
			pstmt.setString(16, personaGiuridica.getReferente());
			pstmt.setString(17, personaGiuridica.getTelefonoReferente());
			pstmt.setInt(18, personaGiuridica.getIndirizzo().getProvinciaId());
			pstmt.setString(19, "G");
			if (personaGiuridica.getRowCreatedTime() != null) {
				pstmt.setDate(21, new Date(personaGiuridica.getRowCreatedTime()
						.getTime()));
			} else {
				pstmt.setDate(21, null);
			}
			if (personaGiuridica.getRowUpdatedTime() != null) {
				pstmt.setDate(23, new Date(personaGiuridica.getRowUpdatedTime()
						.getTime()));
			} else {
				pstmt.setDate(23, null);
			}
			pstmt.setString(20, personaGiuridica.getRowCreatedUser());

			pstmt.setString(22, personaGiuridica.getRowUpdatedUser());
			recIns = pstmt.executeUpdate();
			logger.info("Numero Persone Giuridiche inserite:" + recIns);

		} catch (Exception e) {
			logger.error("Save Rubrica", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		newPG.setReturnValue(ReturnValues.SAVED);
		return newPG;

	}

	public SoggettoVO getPersonaGiuridica(int id) throws DataException {
		Connection connection = null;
		SoggettoVO s = new SoggettoVO('G');
		try {
			connection = jdbcMan.getConnection();
			s = getPersonaGiuridica(connection, id);
		} catch (Exception e) {
			logger.error("getPersonaGiuridica:", e);
			throw new DataException("error.database.missing");
		} finally {
			jdbcMan.close(connection);
		}
		return s;
	}
	
	public String getMailFormPersonaId(Connection connection, int id)
			throws DataException {
		String mail=null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("getMailFormPersonaId() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_MAIL_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				mail=rs.getString(1);	
			}

		} catch (Exception e) {
			logger.error("get Lista Persona Fisica", e);
			throw new DataException("Cannot load Lista Persona Fisica");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return mail;

	}

	public SoggettoVO getPersonaGiuridica(Connection connection, int id)
			throws DataException {
		PreparedStatement pstmt = null;
		SoggettoVO pg = new SoggettoVO('G');
		pg.setReturnValue(ReturnValues.UNKNOWN);
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("getPersonaFisica() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_SOGGETTO_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			Indirizzo indirizzo = pg.getIndirizzo();
			if (rs.next()) {
				pg.setId(id);
				if (rs.getString("DESC_DITTA") == null) {
					pg.setDescrizioneDitta("");
				} else {
					pg.setDescrizioneDitta(rs.getString("DESC_DITTA"));
				}
				pg.setPartitaIva(rs.getString("CODI_PARTITA_IVA"));
				pg.setFlagSettoreAppartenenza(rs.getLong("FLAG_SETTORE"));
				pg.setIndirizzoEMail(rs.getString("INDI_EMAIL"));
				pg.setIndirizzoWeb(rs.getString("INDI_WEB"));
				if (rs.getString("TEXT_NOTE") == null) {
					pg.setNote("");
				} else {
					pg.setNote(rs.getString("TEXT_NOTE"));
				}
				pg.setReferente(rs.getString("PERS_REFERENTE"));
				pg.setTelefonoReferente(rs.getString("TELE_REFERENTE"));
				pg.setTeleFax(rs.getString("TELE_FAX"));
				pg.setTelefono(rs.getString("TELE_TELEFONO"));
				pg.setDug(rs.getString("INDI_DUG"));
				if (rs.getString("INDI_COMUNE") == null) {
					indirizzo.setComune("");
				} else {
					indirizzo.setComune(rs.getString("INDI_COMUNE"));
				}
				if (rs.getString("INDI_CAP") == null) {
					indirizzo.setCap("");
				} else {
					indirizzo.setCap(rs.getString("INDI_CAP"));
				}
				if (rs.getString("INDI_CIVICO") == null) {
					indirizzo.setCivico("");
				} else {
					indirizzo.setCivico(rs.getString("INDI_CIVICO"));
				}
				indirizzo.setProvinciaId(rs.getInt("provincia_id"));
				indirizzo.setToponimo(rs.getString("INDI_TOPONIMO"));
				pg.setIndirizzo(indirizzo);
				pg.setReturnValue(ReturnValues.FOUND);
			}

		} catch (Exception e) {
			logger.error("get Lista Persona Fisica", e);
			throw new DataException("Cannot load Lista Persona Fisica");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return pg;

	}

	public SoggettoVO getPersonaFisica(int id) throws DataException {
		Connection connection = null;
		SoggettoVO s = new SoggettoVO('F');
		try {
			connection = jdbcMan.getConnection();
			s = getPersonaFisica(connection, id);
		} catch (Exception e) {
			logger.error("getPersonaFisica:", e);
			throw new DataException("error.database.missing");
		} finally {
			jdbcMan.close(connection);
		}
		return s;
	}

	public String getTipoPersona(Connection connection, int id)
			throws DataException {
		PreparedStatement pstmt = null;
		String pf = null;
		
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("getPersonaFisica() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_TIPO_SOGGETTO_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				pf=rs.getString(1);
			}

		} catch (Exception e) {
			logger.error("get Lista Persona Fisica", e);
			throw new DataException("Cannot load Lista Persona Fisica");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return pf;

	}
	
	
	public SoggettoVO getPersonaFisica(Connection connection, int id)
			throws DataException {
		PreparedStatement pstmt = null;
		SoggettoVO pf = new SoggettoVO('F');
		pf.setReturnValue(ReturnValues.UNKNOWN);
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("getPersonaFisica() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(SELECT_SOGGETTO_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				pf.setId(id);
				pf.setCodiceFiscale(rs.getString("CODI_FISCALE"));
				if (rs.getString("PERS_COGNOME") == null) {
					pf.setCognome("");
				} else {
					pf.setCognome(rs.getString("PERS_COGNOME"));
				}
				if (rs.getString("PERS_NOME") == null) {
					pf.setNome("");
				} else {
					pf.setNome(rs.getString("PERS_NOME"));
				}
				pf.setCodiceStatoCivile(rs.getString("CODI_STATO_CIVILE"));
				pf.setComuneNascita(rs.getString("DESC_COMUNE_NASCITA"));
				pf.setCodMatricola(rs.getString("CODI_CODICE"));
				pf.setSesso(rs.getString("SESSO"));
				pf.setQualifica(rs.getString("DESC_QUALIFICA"));
				pf.setDataNascita(rs.getDate("DATA_NASCITA"));
				pf.setComuneNascita(rs.getString("DESC_COMUNE_NASCITA"));
				pf.setCodiceStatoCivile(rs.getString("CODI_STATO_CIVILE"));
				pf.setProvinciaNascitaId(rs.getInt("provincia_nascita_id"));
				pf.setIndirizzoEMail(rs.getString("INDI_EMAIL"));
				pf.setIndirizzoWeb(rs.getString("INDI_WEB"));
				if (rs.getString("TEXT_NOTE") == null) {
					pf.setNote("");
				} else {
					pf.setNote(rs.getString("TEXT_NOTE"));
				}
				pf.setTeleFax(rs.getString("TELE_FAX"));
				pf.setTelefono(rs.getString("TELE_TELEFONO"));
				Indirizzo indirizzo = pf.getIndirizzo();
				if (rs.getString("INDI_COMUNE") == null) {
					indirizzo.setComune("");
				} else {
					indirizzo.setComune(rs.getString("INDI_COMUNE"));
				}
				if (rs.getString("INDI_CAP") == null) {
					indirizzo.setCap("");
				} else {
					indirizzo.setCap(rs.getString("INDI_CAP"));
				}
				if (rs.getString("INDI_CIVICO") == null) {
					indirizzo.setCivico("");
				} else {
					indirizzo.setCivico(rs.getString("INDI_CIVICO"));
				}
				indirizzo.setProvinciaId(rs.getInt("provincia_id"));
				if (rs.getString("INDI_TOPONIMO") == null) {
					indirizzo.setToponimo("");
				} else {
					indirizzo.setToponimo(rs.getString("INDI_TOPONIMO"));
				}

				pf.setIndirizzo(indirizzo);
				pf.setReturnValue(ReturnValues.FOUND);
			}

		} catch (Exception e) {
			logger.error("get Lista Persona Fisica", e);
			throw new DataException("Cannot load Lista Persona Fisica");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return pf;

	}

	public ArrayList<SoggettoVO> getListaPersonaFisicaByCognomeNome(String cognomeNome)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		SoggettoVO pf;
		ResultSet rs = null;
		ArrayList<SoggettoVO> lista = new ArrayList<SoggettoVO>();
		try {
			connection = jdbcMan.getConnection();
			String SqlPersoneFisiche = "SELECT * FROM RUBRICA "
					+ " WHERE FLAG_TIPO_RUBRICA='F'";
			if (cognomeNome != null && !"".equals(cognomeNome.trim()))
				SqlPersoneFisiche += " AND lower((PERS_COGNOME||' '||PERS_NOME)) LIKE ?";

			SqlPersoneFisiche += " ORDER BY PERS_COGNOME, PERS_NOME";
			int i = 1;
			pstmt = connection.prepareStatement(SqlPersoneFisiche);
			if (cognomeNome != null && !"".equals(cognomeNome.trim())) {
				pstmt.setString(i++, "%" + cognomeNome.toLowerCase() + "%");
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
				pf = new SoggettoVO('F');
				pf.setId(rs.getInt("RUBRICA_ID"));
				pf.setCognome(rs.getString("PERS_COGNOME"));
				pf.setNome(rs.getString("PERS_NOME"));
				pf.setCodiceFiscale(rs.getString("CODI_FISCALE"));
				pf.setDug(rs.getString("INDI_DUG"));
				if (rs.getString("INDI_COMUNE") == null) {
					pf.getIndirizzo().setComune("");
				} else {
					pf.getIndirizzo().setComune(rs.getString("INDI_COMUNE"));
				}
				if (rs.getString("INDI_CAP") == null) {
					pf.getIndirizzo().setCap("");
				} else {
					pf.getIndirizzo().setCap(rs.getString("INDI_CAP"));
				}
				if (rs.getString("INDI_CIVICO") == null) {
					pf.getIndirizzo().setCivico("");
				} else {
					pf.getIndirizzo().setCivico(rs.getString("INDI_CIVICO"));
				}
				pf.getIndirizzo().setProvinciaId(rs.getInt("provincia_id"));
				pf.getIndirizzo().setToponimo(rs.getString("INDI_TOPONIMO"));
				pf.setIndirizzoEMail(rs.getString("INDI_EMAIL"));
				lista.add(pf);
			}

		} catch (Exception e) {
			logger.error("get Lista Persona Fisica", e);
			throw new DataException("Cannot load Lista Persona Fisica");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return lista;
	}

	public ArrayList<SoggettoVO> getListaPersonaFisica(int aooId, String cognome,
			String nome, String codiceFiscale, String comune)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		SoggettoVO pf;
		ResultSet rs = null;
		ArrayList<SoggettoVO> lista = new ArrayList<SoggettoVO>();
		try {
			connection = jdbcMan.getConnection();
			String SqlPersoneFisiche = "SELECT * FROM RUBRICA "
					+ " WHERE FLAG_TIPO_RUBRICA='F' and AOO_ID=" + aooId;
			if (cognome != null && !"".equals(cognome.trim()))
				SqlPersoneFisiche += " AND lower(PERS_COGNOME) LIKE ?";
			if (nome != null && !"".equals(nome.trim()))
				SqlPersoneFisiche += " AND lower(PERS_NOME) LIKE ?";
			if (codiceFiscale != null && !"".equals(codiceFiscale.trim()))
				SqlPersoneFisiche += " AND lower(CODI_FISCALE) LIKE ?";
			if (comune != null && !"".equals(comune.trim()))
				SqlPersoneFisiche += " AND lower(INDI_COMUNE) LIKE ?";

			SqlPersoneFisiche += " ORDER BY PERS_COGNOME, PERS_NOME";
			int i = 1;
			pstmt = connection.prepareStatement(SqlPersoneFisiche);
			if (cognome != null && !"".equals(cognome.trim())) {
				pstmt.setString(i++, "%" + cognome.toLowerCase() + "%");
			}
			if (nome != null && !"".equals(nome.trim())) {
				pstmt.setString(i++, "%" + nome.toLowerCase() + "%");
			}
			if (codiceFiscale != null && !"".equals(codiceFiscale.trim())) {
				pstmt.setString(i++, codiceFiscale.toLowerCase() + "%");
			}
			if (comune != null && !"".equals(comune.trim()))
				pstmt.setString(i++, comune.toLowerCase() + "%");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				pf = new SoggettoVO('F');
				pf.setId(rs.getInt("RUBRICA_ID"));
				pf.setCognome(rs.getString("PERS_COGNOME"));
				pf.setNome(rs.getString("PERS_NOME"));
				pf.setCodiceFiscale(rs.getString("CODI_FISCALE"));
				pf.setDug(rs.getString("INDI_DUG"));
				if (rs.getString("INDI_COMUNE") == null) {
					pf.getIndirizzo().setComune("");
				} else {
					pf.getIndirizzo().setComune(rs.getString("INDI_COMUNE"));
				}
				if (rs.getString("INDI_CAP") == null) {
					pf.getIndirizzo().setCap("");
				} else {
					pf.getIndirizzo().setCap(rs.getString("INDI_CAP"));
				}
				if (rs.getString("INDI_CIVICO") == null) {
					pf.getIndirizzo().setCivico("");
				} else {
					pf.getIndirizzo().setCivico(rs.getString("INDI_CIVICO"));
				}
				pf.getIndirizzo().setProvinciaId(rs.getInt("provincia_id"));
				pf.getIndirizzo().setToponimo(rs.getString("INDI_TOPONIMO"));
				pf.setIndirizzoEMail(rs.getString("INDI_EMAIL"));
				lista.add(pf);
			}

		} catch (Exception e) {
			logger.error("get Lista Persona Fisica", e);
			throw new DataException("Cannot load Lista Persona Fisica");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return lista;
	}

	public ArrayList<SoggettoVO> getListaPersonaFisicaByCognome(String cognome)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		SoggettoVO pf;
		ResultSet rs = null;
		ArrayList<SoggettoVO> lista = new ArrayList<SoggettoVO>();
		try {
			connection = jdbcMan.getConnection();
			String SqlPersoneFisiche = "SELECT * FROM RUBRICA "
					+ " WHERE FLAG_TIPO_RUBRICA='F'";
			if (cognome != null && !"".equals(cognome.trim()))
				SqlPersoneFisiche += " AND lower(PERS_COGNOME) LIKE ?";

			SqlPersoneFisiche += " ORDER BY PERS_COGNOME, PERS_NOME";
			int i = 1;
			pstmt = connection.prepareStatement(SqlPersoneFisiche);
			if (cognome != null && !"".equals(cognome.trim())) {
				pstmt.setString(i++, "%" + cognome.toLowerCase() + "%");
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
				pf = new SoggettoVO('F');
				pf.setId(rs.getInt("RUBRICA_ID"));
				pf.setCognome(rs.getString("PERS_COGNOME"));
				pf.setNome(rs.getString("PERS_NOME"));
				pf.setCodiceFiscale(rs.getString("CODI_FISCALE"));
				pf.setDug(rs.getString("INDI_DUG"));
				if (rs.getString("INDI_COMUNE") == null) {
					pf.getIndirizzo().setComune("");
				} else {
					pf.getIndirizzo().setComune(rs.getString("INDI_COMUNE"));
				}
				if (rs.getString("INDI_CAP") == null) {
					pf.getIndirizzo().setCap("");
				} else {
					pf.getIndirizzo().setCap(rs.getString("INDI_CAP"));
				}
				if (rs.getString("INDI_CIVICO") == null) {
					pf.getIndirizzo().setCivico("");
				} else {
					pf.getIndirizzo().setCivico(rs.getString("INDI_CIVICO"));
				}
				pf.getIndirizzo().setProvinciaId(rs.getInt("provincia_id"));
				pf.getIndirizzo().setToponimo(rs.getString("INDI_TOPONIMO"));
				pf.setIndirizzoEMail(rs.getString("INDI_EMAIL"));
				lista.add(pf);
			}

		} catch (Exception e) {
			logger.error("get Lista Persona Fisica", e);
			throw new DataException("Cannot load Lista Persona Fisica");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return lista;
	}

	public ArrayList<SoggettoVO> getListaPersonaGiuridica(int aooId, String denominazione,
			String pIva, String comune) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		SoggettoVO pg;
		ResultSet rs = null;
		ArrayList<SoggettoVO> lista = new ArrayList<SoggettoVO>();
		try {
			connection = jdbcMan.getConnection();
			String SqlPersoneGiuridiche = "SELECT * FROM RUBRICA "
					+ " WHERE FLAG_TIPO_RUBRICA='G' and AOO_ID=" + aooId;
			if (denominazione != null && !"".equals(denominazione.trim()))
				SqlPersoneGiuridiche += " AND lower(DESC_DITTA) LIKE ?";
			if (pIva != null && !"".equals(pIva.trim()))
				SqlPersoneGiuridiche += " AND lower(CODI_PARTITA_IVA) LIKE ?";
			if (comune != null && !"".equals(comune.trim()))
				SqlPersoneGiuridiche += " AND lower(INDI_COMUNE) LIKE ?";
			SqlPersoneGiuridiche += " ORDER BY DESC_DITTA";
			pstmt = connection.prepareStatement(SqlPersoneGiuridiche);
			int i = 1;
			if (denominazione != null && !"".equals(denominazione.trim())) {
				pstmt.setString(i++, "%" + denominazione.toLowerCase() + "%");
			}
			if (pIva != null && !"".equals(pIva.trim())) {
				pstmt.setString(i++, pIva.toLowerCase() + "%");
			}
			if (comune != null && !"".equals(comune.trim())) {
				pstmt.setString(i++, comune.toLowerCase() + "%");
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				pg = new SoggettoVO('G');
				pg.setId(rs.getInt("RUBRICA_ID"));
				pg.setDescrizioneDitta(rs.getString("DESC_DITTA"));
				pg.setPartitaIva(rs.getString("CODI_PARTITA_IVA"));
				// pg.getIndirizzo().setComune(rs.getString("INDI_COMUNE"));
				pg.setDug(rs.getString("INDI_DUG"));
				if (rs.getString("INDI_COMUNE") == null) {
					pg.getIndirizzo().setComune("");
				} else {
					pg.getIndirizzo().setComune(rs.getString("INDI_COMUNE"));
				}
				if (rs.getString("INDI_CAP") == null) {
					pg.getIndirizzo().setCap("");
				} else {
					pg.getIndirizzo().setCap(rs.getString("INDI_CAP"));
				}
				if (rs.getString("INDI_CIVICO") == null) {
					pg.getIndirizzo().setCivico("");
				} else {
					pg.getIndirizzo().setCivico(rs.getString("INDI_CIVICO"));
				}
				pg.getIndirizzo().setProvinciaId(rs.getInt("provincia_id"));
				pg.getIndirizzo().setToponimo(rs.getString("INDI_TOPONIMO"));
				pg.setIndirizzoEMail(rs.getString("indi_email"));
				lista.add(pg);
			}

		} catch (Exception e) {
			logger.error("get getListaPersonaGiuridica", e);
			throw new DataException("Cannot load getListaPersonaGiuridica");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
			jdbcMan.close(connection);
		}
		return lista;
	}

	public SoggettoVO editPersonaFisica(Connection connection,
			SoggettoVO personaFisica) throws DataException {
		SoggettoVO newPF = new SoggettoVO('F');
		PreparedStatement pstmt = null;
		int recIns;
		try {
			if (connection == null) {
				connection = jdbcMan.getConnection();
				connection.setAutoCommit(false);
			}
			pstmt = connection.prepareStatement(UPDATE_PERSONA_FISICA);
			pstmt.setString(1, personaFisica.getCognome());
			pstmt.setString(2, personaFisica.getNome());
			pstmt.setString(3, personaFisica.getNote());
			pstmt.setString(4, personaFisica.getTelefono());
			pstmt.setString(5, personaFisica.getTeleFax());
			pstmt.setString(6, personaFisica.getIndirizzoWeb());
			pstmt.setString(7, personaFisica.getIndirizzoEMail());
			pstmt.setString(8, personaFisica.getIndirizzo().getCap());
			pstmt.setString(9, personaFisica.getIndirizzo().getComune());
			pstmt.setString(10, personaFisica.getCodMatricola());
			pstmt.setInt(11, personaFisica.getAoo());
			pstmt.setString(12, personaFisica.getQualifica());
			pstmt.setString(13, personaFisica.getIndirizzo().getToponimo());
			pstmt.setString(14, personaFisica.getIndirizzo().getCivico());
			if (personaFisica.getDataNascita() != null) {
				pstmt.setDate(15, new Date(personaFisica.getDataNascita()
						.getTime()));
			} else {
				pstmt.setDate(15, null);
			}
			pstmt.setString(16, personaFisica.getSesso());
			pstmt.setString(17, personaFisica.getComuneNascita());
			pstmt.setString(18, personaFisica.getCodiceFiscale());
			pstmt.setString(19, personaFisica.getStatoCivile());
			pstmt.setInt(20, personaFisica.getProvinciaNascitaId());
			pstmt.setInt(21, personaFisica.getIndirizzo().getProvinciaId());
			pstmt.setString(22, "F");
			pstmt.setString(23, personaFisica.getRowUpdatedUser());
			pstmt.setDate(24, new Date(personaFisica.getRowUpdatedTime()
					.getTime()));
			pstmt.setInt(25, personaFisica.getId().intValue());
			recIns = pstmt.executeUpdate();
			logger.info("Numero Persone Fisiche modificate:" + recIns);

		} catch (Exception e) {
			logger.error("Update Rubrica", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		newPF.setReturnValue(ReturnValues.SAVED);
		return newPF;

	}

	public SoggettoVO editPersonaGiuridica(Connection connection,
			SoggettoVO personaGiuridica) throws DataException {
		SoggettoVO newPG = new SoggettoVO('G');
		PreparedStatement pstmt = null;
		int recUpd;
		try {
			if (connection == null) {
				connection = jdbcMan.getConnection();
				connection.setAutoCommit(false);
			}

			pstmt = connection.prepareStatement(UPDATE_PERSONA_GIURIDICA);
			pstmt.setString(1, personaGiuridica.getDescrizioneDitta());
			pstmt.setString(2, personaGiuridica.getNote());
			pstmt.setString(3, personaGiuridica.getTelefono());
			pstmt.setString(4, personaGiuridica.getTeleFax());
			pstmt.setString(5, personaGiuridica.getIndirizzoWeb());
			pstmt.setString(6, personaGiuridica.getIndirizzoEMail());
			pstmt.setString(7, personaGiuridica.getIndirizzo().getCap());
			pstmt.setString(8, personaGiuridica.getIndirizzo().getComune());
			pstmt.setInt(9, personaGiuridica.getAoo());
			pstmt.setLong(10, personaGiuridica.getFlagSettoreAppartenenza());
			pstmt.setString(11, personaGiuridica.getDug());
			pstmt.setString(12, personaGiuridica.getIndirizzo().getToponimo());
			pstmt.setString(13, personaGiuridica.getIndirizzo().getCivico());
			pstmt.setString(14, personaGiuridica.getPartitaIva());
			pstmt.setString(15, personaGiuridica.getReferente());
			pstmt.setString(16, personaGiuridica.getTelefonoReferente());
			pstmt.setInt(17, personaGiuridica.getIndirizzo().getProvinciaId());
			pstmt.setString(18, "G");
			pstmt.setString(19, personaGiuridica.getRowUpdatedUser());
			pstmt.setDate(20, new Date(personaGiuridica.getRowUpdatedTime()
					.getTime()));
			pstmt.setInt(21, personaGiuridica.getId().intValue());
			recUpd = pstmt.executeUpdate();
			logger.info("Numero Persone Giuridiche modificate:" + recUpd);

		} catch (Exception e) {
			logger.error("Edit Rubrica", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
		newPG.setReturnValue(ReturnValues.SAVED);
		return newPG;

	}

	public boolean isDescriptionUsed(Connection connection, String descrizione,
			int id) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean used = true;
		try {
			if (connection == null) {
				logger.warn("isUsernameUsed - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			// pstmt = connection.prepareStatement(CHECK_DESCRIZIONE);
			pstmt = connection.prepareStatement(CHECK_DESCRIZIONE_MODIFICA);
			pstmt.setString(1, descrizione.trim().toUpperCase());
			// pstmt.setInt(2, id);
			rs = pstmt.executeQuery();
			rs.next();
			used = rs.getInt(1) > 0;
		} catch (Exception e) {
			logger.error("isUsernameUsed:" + descrizione, e);
			throw new DataException("error.database.missing");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}

		return used;
	}

	public ListaDistribuzioneVO newListaDistribuzione(Connection connection,
			ListaDistribuzioneVO listaDistribuzione, String flag)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("newListaDistribuzione() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			if (!isDescriptionUsed(connection, listaDistribuzione
					.getDescription(), 0)) {
				pstmt = connection.prepareStatement(INSERT_LISTA_DISTRIBUZIONE);
				pstmt.setInt(1, listaDistribuzione.getId().intValue());
				pstmt.setString(2, listaDistribuzione.getDescription());
				pstmt.setString(3, listaDistribuzione.getRowCreatedUser());
				if (listaDistribuzione.getRowCreatedTime() != null) {
					pstmt.setDate(4, new Date(listaDistribuzione
							.getRowCreatedTime().getTime()));
				} else {
					pstmt.setDate(4, null);
				}
				pstmt.setString(5, listaDistribuzione.getRowUpdatedUser());
				if (listaDistribuzione.getRowUpdatedTime() != null) {
					pstmt.setDate(6, new Date(listaDistribuzione
							.getRowUpdatedTime().getTime()));
				} else {
					pstmt.setDate(6, null);
				}
				pstmt.setInt(7, listaDistribuzione.getAooId());
				pstmt.setString(8, flag);
				pstmt.executeUpdate();
				listaDistribuzione.setReturnValue(ReturnValues.SAVED);
			}
		} catch (Exception e) {
			logger.error("Save Rubrica", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}

		return listaDistribuzione;
	}

	public void inserisciSoggettoLista(Connection connection,
			RubricaListaVO rubricaLista) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("inserisciSoggettoLista() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(INSERT_RUBRICA_LISTA_2);
			pstmt.setInt(1, rubricaLista.getIdLista());
			pstmt.setInt(2, rubricaLista.getIdRubrica());
			pstmt.setString(3, rubricaLista.getRowCreatedUser());
			pstmt.setString(4, rubricaLista.getRowUpdatedUser());
			pstmt.setString(5, rubricaLista.getTipoSoggetto());
			if (rubricaLista.getRowCreatedTime() != null) {
				pstmt.setDate(6, new Date(rubricaLista.getRowCreatedTime()
						.getTime()));
			} else {
				pstmt.setDate(6, null);
			}

			if (rubricaLista.getRowUpdatedTime() != null) {
				pstmt.setDate(7, new Date(rubricaLista.getRowUpdatedTime()
						.getTime()));
			} else {
				pstmt.setDate(7, null);
			}

			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("Save Rubrica Lista", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void inserisciSoggettoLista(Connection connection, int listaId,
			int soggettoId, String tipoSoggetto, String utente)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("inserisciSoggettoLista() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(INSERT_RUBRICA_LISTA);
			pstmt.setInt(1, listaId);
			pstmt.setInt(2, soggettoId);
			pstmt.setString(3, utente);
			pstmt.setString(4, utente);
			pstmt.setString(5, tipoSoggetto);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("Save Rubrica Lista", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public boolean isDescriptionModifica(Connection connection,
			String descrizione) throws DataException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean modifica = true;
		try {
			if (connection == null) {
				logger.warn("isUsernameUsed - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(CHECK_DESCRIZIONE_MODIFICA);
			pstmt.setString(1, descrizione.trim().toUpperCase());

			rs = pstmt.executeQuery();
			rs.next();
			modifica = rs.getInt(1) > 0;
		} catch (Exception e) {
			logger.error("isUsernameUsed:" + descrizione, e);
			throw new DataException("error.database.missing");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return modifica;
	}

	public ListaDistribuzioneVO editListaDistribuzione(Connection connection,
			ListaDistribuzioneVO listaDistribuzione) throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				connection = jdbcMan.getConnection();
				connection.setAutoCommit(false);
			}
			if (!isDescriptionModifica(connection, listaDistribuzione
					.getDescription())) {
				pstmt = connection.prepareStatement(UPDATE_LISTA_DISTRIBUZIONE);
				pstmt.setString(1, listaDistribuzione.getDescription());
				pstmt.setString(2, listaDistribuzione.getRowUpdatedUser());
				pstmt.setInt(3, listaDistribuzione.getId().intValue());

				pstmt.executeUpdate();
				listaDistribuzione.setReturnValue(ReturnValues.SAVED);

			} else {
				listaDistribuzione
						.setReturnValue(ReturnValues.EXIST_DESCRIPTION);
			}
		} catch (Exception e) {
			logger.error("Edit Rubrica", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}

		return listaDistribuzione;

	}

	public Collection<AssegnatarioVO> getAssegnatariListaDistribuzione(int listaId)
			throws DataException {
		List<AssegnatarioVO> assegnatari = new ArrayList<AssegnatarioVO>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(LISTA_ASSEGNATARI);
			pstmt.setInt(1, listaId);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				AssegnatarioVO ass = new AssegnatarioVO();
				ass.setCaricaAssegnatarioId(rs.getInt("id_carica"));
				ass.setUfficioAssegnatarioId(rs.getInt("id_ufficio"));
				assegnatari.add(ass);
			}
		} catch (Exception e) {
			logger.error("getAssegnatariListaDistribuzione", e);
			throw new DataException(
					"Cannot load getAssegnatariListaDistribuzione");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return assegnatari;
	}

	public ListaDistribuzioneVO getListaDistribuzione(int id)
			throws DataException {
		Connection connection = null;
		ListaDistribuzioneVO l = new ListaDistribuzioneVO();
		try {
			connection = jdbcMan.getConnection();
			l = getListaDistribuzione(connection, id);
		} catch (Exception e) {
			logger.error("getListaDistribuzione:", e);
			throw new DataException("error.database.missing");
		} finally {
			jdbcMan.close(connection);
		}
		return l;
	}

	public ListaDistribuzioneVO getListaDistribuzione(Connection connection,
			int id) throws DataException {
		PreparedStatement pstmt = null;
		ListaDistribuzioneVO ld = new ListaDistribuzioneVO();
		ld.setReturnValue(ReturnValues.UNKNOWN);
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("getListaDistribuzione() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection
					.prepareStatement(SELECT_LISTADISTRIBUZIONE_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				ld.setId(id);
				ld.setDescription(rs.getString("DESCRIZIONE"));
				ld.setReturnValue(ReturnValues.FOUND);
			}

		} catch (Exception e) {
			logger.error("get elenco Lista Distribuzione", e);
			throw new DataException("Cannot load elenco Lista Distribuzione");
		} finally {
			jdbcMan.close(rs);
			jdbcMan.close(pstmt);
		}
		return ld;

	}

	public ArrayList<IdentityVO> getElencoListaDistribuzione(String descrizione, int aooId,
			String flag) throws DataException {
		try {
			Connection connection = null;
			PreparedStatement pstmt = null;
			IdentityVO ld;
			ResultSet rs = null;
			ArrayList<IdentityVO> lista = new ArrayList<IdentityVO>();
			try {
				connection = jdbcMan.getConnection();
				String query = "SELECT * FROM LISTA_DISTRIBUZIONE WHERE AOO_ID=? AND FLAG_TIPO = ?";
				if (descrizione != null && !"".equals(descrizione.trim())) {
					query += " AND upper(DESCRIZIONE) LIKE ?";
				}

				pstmt = connection.prepareStatement(query);
				pstmt.setInt(1, aooId);
				pstmt.setString(2, flag);
				if (descrizione != null && !"".equals(descrizione.trim())) {
					pstmt.setString(3, descrizione.toUpperCase() + "%");
				}

				rs = pstmt.executeQuery();
				while (rs.next()) {
					ld = new IdentityVO();
					ld.setId(rs.getInt("ID"));
					ld.setDescription(rs.getString("DESCRIZIONE"));
					lista.add(ld);
				}

			} catch (Exception e) {
				logger.error("get Elenco Lista Distribuzione", e);
				throw new DataException(
						"Cannot load Elenco Lista Distribuzione");
			} finally {
				jdbcMan.close(rs);
				jdbcMan.close(pstmt);
				jdbcMan.close(connection);
			}
			return lista;
		} catch (DataException de) {
			logger
					.error("SoggettoDAOjdbc: failed getting getElencoListaDistribuzione: ");
			return null;
		}
	}

	public ArrayList<SoggettoVO> getDestinatariListaDistribuzione(int lista_id)
			throws DataException {
		try {
			Connection connection = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			ArrayList<SoggettoVO> lista = new ArrayList<SoggettoVO>();
			try {
				connection = jdbcMan.getConnection();
				String query = "SELECT * FROM RUBRICA_LISTA_DISTRIBUZIONE WHERE ID_LISTA=?";

				pstmt = connection.prepareStatement(query);
				pstmt.setInt(1, lista_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					int sogId = rs.getInt("id_rubrica");
					if ("F".equalsIgnoreCase(rs.getString("tipo_soggetto"))) {
						lista.add(getPersonaFisica(connection, sogId));
					} else {
						lista.add(getPersonaGiuridica(connection, sogId));
					}
				}

			} catch (Exception e) {
				logger.error("get getDestinatariListaDistribuzione", e);
				throw new DataException(
						"Cannot load getDestinatariListaDistribuzione");
			} finally {
				jdbcMan.close(rs);
				jdbcMan.close(pstmt);
				jdbcMan.close(connection);
			}
			return lista;
		} catch (DataException de) {
			logger
					.error("SoggettoDAOjdbc: failed getting getDestinatariListaDistribuzione: ");
			return null;
		}
	}

	public ArrayList<IdentityVO> getElencoListeDistribuzione(String flag)
			throws DataException {
		try {
			Connection connection = null;
			PreparedStatement pstmt = null;
			IdentityVO ld;
			ResultSet rs = null;
			ArrayList<IdentityVO> lista = new ArrayList<IdentityVO>();
			try {
				connection = jdbcMan.getConnection();
				String query = "SELECT * FROM LISTA_DISTRIBUZIONE WHERE FLAG_TIPO=?";
				pstmt = connection.prepareStatement(query);
				pstmt.setString(1, flag);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					ld = new IdentityVO();
					ld.setId(rs.getInt("ID"));
					ld.setDescription(rs.getString("DESCRIZIONE"));
					lista.add(ld);
				}

			} catch (Exception e) {
				logger.error("get Elenco Liste Distribuzione", e);
				throw new DataException(
						"Cannot load Elenco Liste Distribuzione");
			} finally {
				jdbcMan.close(rs);
				jdbcMan.close(pstmt);
				jdbcMan.close(connection);
			}
			return lista;
		} catch (DataException de) {
			logger
					.error("SoggettoDAOjdbc: failed getting getElencoListeDistribuzione: ");
			return null;
		}
	}

	public int deleteListaDistribuzione(Connection connection, long id)
			throws DataException {
		PreparedStatement pstmt = null;
		int recDel = 0;
		try {
			if (connection == null) {
				connection = jdbcMan.getConnection();
				connection.setAutoCommit(false);
			}
			pstmt = connection
					.prepareStatement(DELETE_LISTADISTRIBUZIONE_RUBRICA);
			pstmt.setLong(1, id);
			recDel = pstmt.executeUpdate();
			jdbcMan.close(pstmt);
			
			pstmt = connection.prepareStatement(DELETE_LISTADISTRIBUZIONE);
			pstmt.setLong(1, id);
			recDel = pstmt.executeUpdate();

			logger.info("Numero Liste distribuzione eliminate:" + recDel);

		} catch (Exception e) {
			logger.error("deleteListaDistribuzione ", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}

		return recDel;
	}

	public int deleteRubricaListaDistribuzione(Connection connection, long id)
			throws DataException {
		PreparedStatement pstmt = null;
		int recDel = 0;
		try {
			if (connection == null) {
				connection = jdbcMan.getConnection();
				connection.setAutoCommit(false);
			}
			pstmt = connection
					.prepareStatement(DELETE_LISTADISTRIBUZIONE_RUBRICA);
			pstmt.setLong(1, id);
			recDel = pstmt.executeUpdate();

			logger.info("Numero Liste distribuzione eliminate:" + recDel);

		} catch (Exception e) {
			logger.error("deleteListaDistribuzione ", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}

		return recDel;
	}

	public int deleteAssegnatariListaDistribuzione(Connection connection,
			long id) throws DataException {
		PreparedStatement pstmt = null;
		int recDel = 0;
		try {
			if (connection == null) {
				connection = jdbcMan.getConnection();
				connection.setAutoCommit(false);
			}
			pstmt = connection
					.prepareStatement(DELETE_LISTADISTRIBUZIONE_ASSEGNATARI);
			pstmt.setLong(1, id);
			recDel = pstmt.executeUpdate();

			logger.info("Numero Liste distribuzione eliminate:" + recDel);

		} catch (Exception e) {
			logger.error("deleteListaDistribuzione ", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}

		return recDel;
	}

	public SoggettoVO getSoggettoByMail(Integer aooId, String email)
			throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SoggettoVO s = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(GET_SOGGETTO_BY_MAIL);
			pstmt.setInt(1, aooId);
			pstmt.setString(2, email);
			rs = pstmt.executeQuery();
			rs.next();
			if (rs.getString(2).equals("F")) {
				s = new SoggettoVO('F');
				s = getPersonaFisica(connection, rs.getInt(1));
			} else {
				s = new SoggettoVO('G');
				s = getPersonaGiuridica(connection, rs.getInt(1));
			}
		} catch (Exception e) {
			logger.error("getPersonaFisica:", e);
			throw new DataException("error.database.missing");
		} finally {
			jdbcMan.close(connection);
		}
		return s;

	}

	public SoggettoVO getSoggettoById(Integer id) throws DataException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SoggettoVO s = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(GET_TIPO_BY_ID);
			pstmt.setInt(1, id);
			// pstmt.setString(2, email);
			rs = pstmt.executeQuery();
			rs.next();
			if (rs.getString(1).equals("F")) {
				s = new SoggettoVO('F');
				s = getPersonaFisica(connection, id);
			} else {
				s = new SoggettoVO('G');
				s = getPersonaGiuridica(connection, id);
			}
		} catch (Exception e) {
			logger.error("getPersonaFisica:", e);
			throw new DataException("error.database.missing");
		} finally {
			jdbcMan.close(connection);
		}
		return s;

	}

	public boolean isMailUsed(Integer aooId, String email) throws DataException {
		PreparedStatement pstmt = null;
		boolean used = false;
		ResultSet rs = null;
		Connection connection = null;
		try {
			connection = jdbcMan.getConnection();
			pstmt = connection.prepareStatement(IS_SOGGETTO_PRESENT);
			pstmt.setInt(1, aooId);
			pstmt.setString(2, email);
			rs = pstmt.executeQuery();
			rs.next();
			used = rs.getInt(1) > 0;
		} catch (Exception e) {
			logger.error("is Soggetto Present", e);
			throw new DataException("Cannot load is Soggetto Present");
		} finally {
			jdbcMan.closeAll(rs, pstmt, connection);
		}
		return used;
	}

	public void eliminaAssegnatariListaDistribuzione(Connection connection,
			int listaId) throws DataException {
		PreparedStatement pstmt = null;

		final String DELETE_ASSEGNATARI = "DELETE FROM assegnatari_lista_distribuzione WHERE id_lista = ?";

		try {
			pstmt = connection.prepareStatement(DELETE_ASSEGNATARI);
			pstmt.setInt(1, listaId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("eliminaAssegnatariListaDistribuzione", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public void salvaAssegnatarioListaDistribuzione(Connection connection,
			int listaId, AssegnatarioVO ass, String utente)
			throws DataException {
		PreparedStatement pstmt = null;
		try {
			if (connection == null) {
				logger.warn("inserisciSoggettoLista() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			pstmt = connection.prepareStatement(INSERT_CARICA_LISTA);
			pstmt.setInt(1, ass.getId());
			pstmt.setInt(2, listaId);
			if (ass.getCaricaAssegnatarioId() != 0)
				pstmt.setInt(3, ass.getCaricaAssegnatarioId());
			else
				pstmt.setNull(3, Types.INTEGER);
			pstmt.setInt(4, ass.getUfficioAssegnatarioId());
			pstmt.setString(5, utente);
			pstmt.setString(6, utente);
			pstmt.executeUpdate();

		} catch (Exception e) {
			logger.error("Save Rubrica Lista", e);
			throw new DataException("error.database.cannotsave");
		} finally {
			jdbcMan.close(pstmt);
		}
	}

	public final static String UPDATE_PERSONA_FISICA = "UPDATE RUBRICA SET "
			+ "PERS_COGNOME=?,  PERS_NOME=?, TEXT_NOTE=?, TELE_TELEFONO=?, TELE_FAX=?, INDI_WEB=?, INDI_EMAIL=?, INDI_CAP=?,"
			+ "INDI_COMUNE=?, CODI_CODICE=?, aoo_id=?, DESC_QUALIFICA=?, INDI_TOPONIMO=?, INDI_CIVICO=?, "
			+ "DATA_NASCITA=?, SESSO=?, DESC_COMUNE_NASCITA=?, CODI_FISCALE=?, CODI_STATO_CIVILE=?, provincia_nascita_id=?, "
			+ "provincia_id=?,FLAG_TIPO_RUBRICA=?, ROW_UPDATED_USER=?, ROW_UPDATED_TIME=? WHERE RUBRICA_ID=? ";

	public final static String INSERT_PERSONA_FISICA = "INSERT INTO RUBRICA ("
			+ "RUBRICA_ID, PERS_COGNOME,  PERS_NOME, TEXT_NOTE, TELE_TELEFONO, TELE_FAX, INDI_WEB, INDI_EMAIL, INDI_CAP,"
			+ "INDI_COMUNE, CODI_CODICE, aoo_id, DESC_QUALIFICA, INDI_TOPONIMO, INDI_CIVICO, "
			+ "DATA_NASCITA, SESSO, DESC_COMUNE_NASCITA, CODI_FISCALE, CODI_STATO_CIVILE, provincia_nascita_id, "
			+ "provincia_id,FLAG_TIPO_RUBRICA, ROW_CREATED_USER,ROW_CREATED_TIME,ROW_UPDATED_USER,ROW_UPDATED_TIME) "
			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public final static String UPDATE_PERSONA_GIURIDICA = "UPDATE RUBRICA SET "
			+ "DESC_DITTA=?, TEXT_NOTE=?, TELE_TELEFONO=?, TELE_FAX=?, INDI_WEB=?, INDI_EMAIL=?, INDI_CAP=?,"
			+ "INDI_COMUNE=?, aoo_id=?, FLAG_SETTORE=?, INDI_DUG=?, INDI_TOPONIMO=?, INDI_CIVICO=?, "
			+ "CODI_PARTITA_IVA=?, PERS_REFERENTE=?, TELE_REFERENTE=?, provincia_id=?,"
			+ "FLAG_TIPO_RUBRICA=?,ROW_UPDATED_USER=?, ROW_UPDATED_TIME=? WHERE RUBRICA_ID=? ";

	public final static String INSERT_PERSONA_GIURIDICA = "INSERT INTO RUBRICA ("
			+ "RUBRICA_ID, DESC_DITTA, TEXT_NOTE, TELE_TELEFONO, TELE_FAX, INDI_WEB, INDI_EMAIL, INDI_CAP,"
			+ "INDI_COMUNE, aoo_id, FLAG_SETTORE, INDI_DUG, INDI_TOPONIMO, INDI_CIVICO, "
			+ "CODI_PARTITA_IVA, PERS_REFERENTE, TELE_REFERENTE, provincia_id,"
			+ "FLAG_TIPO_RUBRICA,ROW_CREATED_USER,ROW_CREATED_TIME,ROW_UPDATED_USER,ROW_UPDATED_TIME) "
			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public final static String SELECT_SOGGETTO_BY_ID = "SELECT *  FROM rubrica  "
			+ " WHERE RUBRICA_ID=?";
	
	public final static String SELECT_MAIL_BY_ID = "SELECT indi_email FROM rubrica  "
			+ " WHERE rubrica_id=?";
	
	public final static String SELECT_TIPO_SOGGETTO_BY_ID = "SELECT FLAG_TIPO_RUBRICA  FROM rubrica  "
			+ " WHERE RUBRICA_ID=?";
	
	public final static String IS_SOGGETTO_PRESENT = "SELECT count(RUBRICA_ID)  FROM RUBRICA  "
			+ " WHERE  AOO_ID=? AND INDI_EMAIL=? ";

	public final static String GET_SOGGETTO_BY_MAIL = "SELECT RUBRICA_ID,FLAG_TIPO_RUBRICA FROM RUBRICA  "
			+ " WHERE  AOO_ID=? AND INDI_EMAIL=?";

	public final static String GET_TIPO_BY_ID = "SELECT FLAG_TIPO_RUBRICA FROM RUBRICA  "
			+ " WHERE RUBRICA_ID=?";

	public final static String DELETE_SOGGETTO = "DELETE FROM RUBRICA "
			+ " WHERE RUBRICA_ID=?";

	public final static String INSERT_LISTA_DISTRIBUZIONE = "INSERT INTO LISTA_DISTRIBUZIONE ("
			+ "ID, DESCRIZIONE,ROW_CREATED_USER,ROW_CREATED_TIME,ROW_UPDATED_USER,ROW_UPDATED_TIME, AOO_ID,FLAG_TIPO) "
			+ " VALUES(?,?,?,?,?,?,?,?)";

	public final static String INSERT_RUBRICA_LISTA = "INSERT INTO rubrica_lista_distribuzione ("
			+ " id_lista ,id_rubrica ,row_created_user ,row_updated_user ,tipo_soggetto) VALUES(?,?,?,?,?)";

	public final static String INSERT_CARICA_LISTA = "INSERT INTO assegnatari_lista_distribuzione (id_assegnatario_lista,"
			+ " id_lista ,id_carica,id_ufficio,row_created_user ,row_updated_user) VALUES(?,?,?,?,?,?)";

	public final static String INSERT_RUBRICA_LISTA_2 = "INSERT INTO rubrica_lista_distribuzione ("
			+ " id_lista ,id_rubrica ,row_created_user ,row_updated_user ,tipo_soggetto,row_created_time,row_updated_time) VALUES(?,?,?,?,?,?,?)";

	public final static String UPDATE_LISTA_DISTRIBUZIONE = "UPDATE LISTA_DISTRIBUZIONE SET "
			+ "DESCRIZIONE=?, ROW_UPDATED_USER=?  WHERE ID=?";

	public final static String SELECT_LISTADISTRIBUZIONE_BY_ID = "SELECT * FROM LISTA_DISTRIBUZIONE "
			+ " WHERE ID=?";

	public final static String DELETE_LISTADISTRIBUZIONE = "DELETE FROM LISTA_DISTRIBUZIONE "
			+ " WHERE ID=?";

	public final static String DELETE_LISTADISTRIBUZIONE_RUBRICA = "DELETE FROM rubrica_lista_distribuzione "
			+ " WHERE id_lista=?";

	public final static String DELETE_LISTADISTRIBUZIONE_ASSEGNATARI = "DELETE FROM assegnatari_lista_distribuzione "
			+ " WHERE id_lista=?";

	public final static String SELECT_ELENCO_SOGGETTILD = "SELECT * FROM RUBRICA ";

	protected final static String CHECK_DESCRIZIONE = "SELECT count(id) FROM lista_distribuzione "
			+ "WHERE upper(descrizione) =? and id !=?";

	protected final static String CHECK_DESCRIZIONE_MODIFICA = "SELECT count(id) FROM lista_distribuzione "
			+ "WHERE upper(descrizione) =?";

	private final static String LISTA_ASSEGNATARI = "SELECT id_carica,id_ufficio FROM assegnatari_lista_distribuzione"
			+ " WHERE id_lista = ?";

}
