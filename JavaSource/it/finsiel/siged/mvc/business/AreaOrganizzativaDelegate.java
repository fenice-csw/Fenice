package it.finsiel.siged.mvc.business;

import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.NomiTabelle;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.AreaOrganizzativa;
import it.finsiel.siged.model.organizzazione.Menu;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.integration.AreaOrganizzativaDAO;
import it.finsiel.siged.mvc.vo.lookup.TipoDocumentoVO;
import it.finsiel.siged.mvc.vo.organizzazione.AreaOrganizzativaVO;
import it.finsiel.siged.mvc.vo.organizzazione.MailConfigVO;
import it.finsiel.siged.mvc.vo.organizzazione.MenuVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.SpedizioneVO;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.finsiel.siged.util.FileUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

public class AreaOrganizzativaDelegate implements ComponentStatus {

	private static Logger logger = Logger
			.getLogger(AreaOrganizzativaDelegate.class.getName());

	private int status;

	private AreaOrganizzativaDAO areaorganizzativaDAO = null;


	private static AreaOrganizzativaDelegate delegate = null;

	private AreaOrganizzativaDelegate() {
		try {
			if (areaorganizzativaDAO == null) {
				areaorganizzativaDAO = (AreaOrganizzativaDAO) DAOFactory
						.getDAO(Constants.AREAORGANIZZATIVA_DAO_CLASS);

				logger.debug("AreaOrganizzativaDAO instantiated: "
						+ Constants.AREAORGANIZZATIVA_DAO_CLASS);
				status = STATUS_OK;
			}
		} catch (Exception e) {
			status = STATUS_ERROR;
			logger.error("", e);
		}

	}

	public static AreaOrganizzativaDelegate getInstance() {
		if (delegate == null)
			delegate = new AreaOrganizzativaDelegate();
		return delegate;
	}

	public static String getIdentifier() {
		return Constants.AREAORGANIZZATIVA_DELEGATE;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int s) {
		this.status = s;
	}


	public Map<Integer,AreaOrganizzativa> getAreeOrganizzative() throws DataException {
		Map<Integer,AreaOrganizzativa> aree = new HashMap<Integer,AreaOrganizzativa>();
		try {
			Collection<Integer> areeId=areaorganizzativaDAO.getAreeOrganizzativeId();
			for(Integer id:areeId){
				AreaOrganizzativa areaOrganizzativa = new AreaOrganizzativa();
				areaOrganizzativa.setValueObject(areaorganizzativaDAO.getAreaOrganizzativa(id));
				areaOrganizzativa.setMailConfig(areaorganizzativaDAO.getMailConfigByAooId(id));
				aree.put(areaOrganizzativa.getValueObject().getId(),areaOrganizzativa);
			}
		}catch (Exception e) {
			logger.error("AreaOrganizzativaDelegate: failed getAreeOrganizzative.", e);
			e.printStackTrace();
		}
		return aree;

	}

	public MailConfigVO getMailConfig(int areaorganizzativaId) {
		try {
			return areaorganizzativaDAO.getMailConfigByAooId(areaorganizzativaId);
		} catch (DataException de) {
			logger
					.error("AreaOrganizzativaDelegate: failed getting getAreaOrganizzativa: ");
			return null;
		}
	}
	
	public AreaOrganizzativa getAreaOrganizzativa(int id) {
		AreaOrganizzativa areaOrganizzativa = new AreaOrganizzativa();
		try {
			areaOrganizzativa.setValueObject(areaorganizzativaDAO.getAreaOrganizzativa(id));
			areaOrganizzativa.setMailConfig(areaorganizzativaDAO.getMailConfigByAooId(id));	
		}catch (Exception e) {
			logger.error("AreaOrganizzativaDelegate: failed getAreeOrganizzative.", e);
		} 
		return areaOrganizzativa;
	}

	public Collection<UfficioVO> getUffici(int areaorganizzativaId) {
		try {
			return areaorganizzativaDAO.getUffici(areaorganizzativaId);
		} catch (DataException de) {
			logger
					.error("AreaOrganizzativaDelegate: failed getting getUffici: ");
			return null;
		}
	}

	public Collection<UtenteVO> getUtenti(int areaorganizzativaId) {
		try {
			return areaorganizzativaDAO.getUtenti(areaorganizzativaId);
		} catch (DataException de) {
			logger
					.error("AreaOrganizzativaDelegate: failed getting getUtenti: ");
			return null;
		}
	}

	public Collection<UtenteVO> getUtentiCarica(int areaorganizzativaId) {
		try {
			return areaorganizzativaDAO.getUtentiCarica(areaorganizzativaId);
		} catch (DataException de) {
			logger
					.error("AreaOrganizzativaDelegate: failed getting getUtenti: ");
			return null;
		}
	}

	public AreaOrganizzativa salvaAreaOrganizzativa(
			AreaOrganizzativa areaorganizzativa, Utente utente) {
		AreaOrganizzativa aooSalvata = new AreaOrganizzativa();
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);

			if (areaorganizzativa.getValueObject().getId() != null
					&& areaorganizzativa.getValueObject().getId().intValue() > 0) {
				aooSalvata.setValueObject(areaorganizzativaDAO.updateAreaOrganizzativa(
						connection, areaorganizzativa.getValueObject()));
				aooSalvata.setMailConfig(areaorganizzativaDAO.updateMailConfig(
						connection, areaorganizzativa.getMailConfig()));
			} else {
				areaorganizzativa.getValueObject().setId(IdentificativiDelegate.getInstance()
						.getNextId(connection, NomiTabelle.AREE_ORGANIZZATIVE));
				int ammId = Organizzazione.getInstance().getValueObject()
						.getId().intValue();
				areaorganizzativa.getValueObject().setAmministrazione_id(ammId);

				aooSalvata.setValueObject(areaorganizzativaDAO.newAreaOrganizzativa(
						connection, areaorganizzativa.getValueObject()));
				areaorganizzativa.getMailConfig().setAooId(aooSalvata.getValueObject().getId());
				aooSalvata.setMailConfig(areaorganizzativaDAO.newMailConfig(
						connection, areaorganizzativa.getMailConfig()));

				RegistroVO newReg = RegistroDelegate.getInstance()
						.salvaRegistro(connection,
								getRegistroUfficialeAOO(areaorganizzativa.getValueObject()));
				RegistroVO newRegPI = RegistroDelegate
						.getInstance()
						.salvaRegistro(connection,
								getRegistroPostaInternaAOO(areaorganizzativa.getValueObject()));
				String[] aRegistri = new String[2];
				aRegistri[0] = newReg.getId().toString();
				aRegistri[1] = newRegPI.getId().toString();

				// Ufficio root della AOO
				UfficioVO newUff = UfficioDelegate.getInstance().salvaUfficio(
						connection, getUfficioRootAOO(areaorganizzativa.getValueObject()));
				String[] aUffici = new String[1];
				aUffici[0] = newUff.getId().toString();

				// Funzioni di menu
				int i = 0;
				Organizzazione org = Organizzazione.getInstance();
				String[] aMenu = new String[org.getMenuList().size()];
				for (Iterator<Menu> iter = org.getMenuList().iterator(); iter
						.hasNext();) {
					MenuVO menu = ((Menu) iter.next()).getValueObject();
					aMenu[i++] = menu.getId().toString();
				}

				UtenteVO ute = UtenteDelegate.getInstance().newUtenteVO(
						connection, getUtenteAmm(areaorganizzativa.getValueObject()),
						aRegistri, utente);

				CaricaDelegate.getInstance().newCarica(
						connection,
						getCaricaAmm(areaorganizzativa.getValueObject(), ute.getId(), newUff
								.getId()), aMenu, newUff.getDescription(),
						newUff.getAooId());

				AmministrazioneDelegate.getInstance().salvaMezzoSpedizione(
						connection, getMezzoEmail(areaorganizzativa.getValueObject()));

				AmministrazioneDelegate.getInstance().salvaTipoDocumento(
						connection, getTipoDocumento(areaorganizzativa.getValueObject()));

			}
			Organizzazione org = Organizzazione.getInstance();
			/**/
			String dirDocAcquisizioneMassiva = org.getValueObject()
					.getPathDocAquisMassiva()
					+ "/" + "aoo_" + String.valueOf(aooSalvata.getValueObject().getId());
			String dirDocProtocollo = org.getValueObject()
					.getPathDocumentiProtocollo()
					+ "/" + "aoo_" + String.valueOf(aooSalvata.getValueObject().getId());
			/**/
			if (FileUtil.gestionePathDoc(dirDocProtocollo) != ReturnValues.INVALID) {
				if (FileUtil.gestionePathDoc(dirDocAcquisizioneMassiva) != ReturnValues.INVALID) {
					aooSalvata.getValueObject().setReturnValue(ReturnValues.SAVED);
					connection.commit();
				}
			}
		} catch (DataException de) {
			aooSalvata.getValueObject().setReturnValue(ReturnValues.UNKNOWN);
			jdbcMan.rollback(connection);
			logger.warn("Salvataggio AOO fallito, rolling back transction..",
					de);

		} catch (SQLException se) {
			aooSalvata.getValueObject().setReturnValue(ReturnValues.UNKNOWN);
			jdbcMan.rollback(connection);
			logger.warn("Salvataggio AOO fallito, rolling back transction..",
					se);

		} catch (Exception e) {
			aooSalvata.getValueObject().setReturnValue(ReturnValues.UNKNOWN);
			jdbcMan.rollback(connection);
			logger.warn("Si e' verificata un eccezione non gestita.", e);

		} finally {
			jdbcMan.close(connection);
		}
		return aooSalvata;
	}

	private CaricaVO getCaricaAmm(AreaOrganizzativaVO areaorganizzativaVO,
			Integer utenteId, Integer ufficioId) {
		CaricaVO carica = new CaricaVO();
		carica.setCaricaId(0);
		carica.setNome("ROOT" + areaorganizzativaVO.getId());
		carica.setUtenteId(utenteId);
		carica.setUfficioId(ufficioId);
		carica.setRowCreatedUser(areaorganizzativaVO.getRowCreatedUser());
		carica.setRowCreatedTime(new Date(System.currentTimeMillis()));
		return carica;
	}

	public boolean isModificabileDipendenzaTitolarioUfficio(int aooId) {
		boolean esiste = false;
		try {
			esiste = areaorganizzativaDAO
					.isModificabileDipendenzaTitolarioUfficio(aooId);

		} catch (Exception de) {
			logger.error("AreaOrganizzativaDelegate: failed cancellaUfficio: ");
		}
		return esiste;
	}

	public boolean esisteAreaOrganizzativa(String descrizioneAoo, int aooId) {
		boolean esiste = false;
		try {
			esiste = areaorganizzativaDAO.esisteAreaOrganizzativa(
					descrizioneAoo, aooId);

		} catch (Exception de) {
			logger.error("AreaOrganizzativaDelegate: failed cancellaUfficio: ");
		}
		return esiste;
	}

	public boolean cancellaAreaOrganizzativa(int aoo_Id) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		boolean cancellato = false;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);

			if (aoo_Id > 0
					&& areaorganizzativaDAO
							.isAreaOrganizzativaCancellabile(aoo_Id)) {
				areaorganizzativaDAO.cancellaAreaOrganizzativa(connection,
						aoo_Id);
				areaorganizzativaDAO.cancellaMailConfig(connection,
						aoo_Id);
				connection.commit();
				cancellato = true;
			}

		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger.error("AreaOrganizzativaDelegate: failed cancellaUfficio: ");
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
		} finally {
			jdbcMan.close(connection);
		}
		return cancellato;
	}

	private RegistroVO getRegistroUfficialeAOO(
			AreaOrganizzativaVO areaorganizzativaVO) {
		RegistroVO regUff = new RegistroVO();
		regUff.setId(0);
		regUff.setAooId(areaorganizzativaVO.getId().intValue());
		regUff.setDataAperturaRegistro(new Date(System.currentTimeMillis()));
		regUff.setCodRegistro("RegUff");
		regUff.setDescrizioneRegistro("Registro Ufficiale");
		regUff.setDataBloccata(false);
		regUff.setUfficiale(true);
		regUff.setApertoIngresso(true);
		regUff.setApertoUscita(true);
		regUff.setRowCreatedUser(areaorganizzativaVO.getRowCreatedUser());
		regUff.setRowUpdatedTime(new Date(System.currentTimeMillis()));
		return regUff;
	}

	private RegistroVO getRegistroPostaInternaAOO(
			AreaOrganizzativaVO areaorganizzativaVO) {
		RegistroVO posInt = new RegistroVO();
		posInt.setId(0);
		posInt.setAooId(areaorganizzativaVO.getId().intValue());
		posInt.setDataAperturaRegistro(new Date(System.currentTimeMillis()));
		posInt.setCodRegistro(Parametri.COD_REGISTRO_POSTA_INTERNA);
		posInt.setDescrizioneRegistro("Posta Interna");
		posInt.setDataBloccata(false);
		posInt.setUfficiale(false);
		posInt.setApertoIngresso(true);
		posInt.setApertoUscita(true);
		posInt.setRowCreatedUser(areaorganizzativaVO.getRowCreatedUser());
		posInt.setRowUpdatedTime(new Date(System.currentTimeMillis()));
		return posInt;
	}

	private UfficioVO getUfficioRootAOO(AreaOrganizzativaVO areaorganizzativaVO) {

		UfficioVO uff = new UfficioVO();
		uff.setId(0);
		uff.setAooId(areaorganizzativaVO.getId().intValue());
		uff.setDescription("Ufficio root - "
				+ areaorganizzativaVO.getDescription());
		uff.setAttivo(true);
		uff.setTipo("C");
		uff.setAccettazioneAutomatica(false);
		uff.setAooId(areaorganizzativaVO.getId().intValue());
		uff.setRowCreatedUser(areaorganizzativaVO.getRowCreatedUser());
		uff.setRowUpdatedTime(new Date(System.currentTimeMillis()));
		return uff;
	}

	private UtenteVO getUtenteAmm(AreaOrganizzativaVO areaorganizzativaVO) {

		UtenteVO ute = new UtenteVO();
		ute.setId(0);
		ute.setAooId(areaorganizzativaVO.getId().intValue());
		ute.setUsername("admin" + areaorganizzativaVO.getId());
		ute.setPassword("admin" + areaorganizzativaVO.getId());
		ute.setCognome(areaorganizzativaVO.getDescription());
		ute.setNome(areaorganizzativaVO.getDescription());
		ute.setCodiceFiscale("1");
		ute.setEmailAddress(null);
		ute.setMatricola(null);
		ute.setAbilitato(true);
		ute.setDataFineAttivita(null);
		ute.setRowCreatedUser(areaorganizzativaVO.getRowCreatedUser());
		ute.setRowUpdatedTime(new Date(System.currentTimeMillis()));
		return ute;
	}

	private SpedizioneVO getMezzoEmail(AreaOrganizzativaVO areaorganizzativaVO) {

		SpedizioneVO mezzoEmail = new SpedizioneVO();
		mezzoEmail.setAooId(areaorganizzativaVO.getId().intValue());
		mezzoEmail.setCodiceSpedizione("Email");
		mezzoEmail.setDescrizioneSpedizione("Email");
		mezzoEmail.setFlagAbilitato(true);
		mezzoEmail.setFlagCancellabile(false);
		mezzoEmail.setRowCreatedUser(areaorganizzativaVO.getRowCreatedUser());
		mezzoEmail.setRowUpdatedTime(new Date(System.currentTimeMillis()));
		return mezzoEmail;
	}

	private TipoDocumentoVO getTipoDocumento(
			AreaOrganizzativaVO areaorganizzativaVO) {

		TipoDocumentoVO tipoDoc = new TipoDocumentoVO();
		tipoDoc.setId(0);
		tipoDoc.setAooId(areaorganizzativaVO.getId().intValue());
		tipoDoc.setFlagAttivazione("1");
		tipoDoc.setFlagDefault("1");
		tipoDoc.setDescrizioneDoc("Lettera");
		tipoDoc.setRowCreatedUser(areaorganizzativaVO.getRowCreatedUser());
		tipoDoc.setRowUpdatedTime(new Date(System.currentTimeMillis()));
		return tipoDoc;
	}

	public void salvaUtenteResponsabile(int aooId, Integer id) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);

			areaorganizzativaDAO.salvaUtenteResponsabile(connection, aooId, id);
			connection.commit();
		} catch (DataException de) {
			jdbcMan.rollback(connection);
			logger
					.error("AreaOrganizzativaDelegate: failed salvaUtenteResponsabile ");
		} catch (SQLException se) {
			jdbcMan.rollback(connection);
		} finally {
			jdbcMan.close(connection);
		}
	}

	public int getUtenteResponsabile(int aooId) {
		try {
			return areaorganizzativaDAO.getUtenteResponsabile(aooId);
		} catch (Exception se) {
			logger
					.error("AreaOrganizzativaDelegate: failed salvaUtenteResponsabile ");
		}
		return 0;
	}

	public int getAreaOrganizzativaIdByDesc(String desc) {
		int id = 0;
		Collection<AreaOrganizzativa> aree = Organizzazione.getInstance().getAreeOrganizzative();
		for (AreaOrganizzativa aoo : aree) {
			if (aoo.getValueObject().getDescription().equals(desc))
				id = aoo.getValueObject().getId();
		}
		return id;
	}

	public void aggiornaAOODataUltimaPecRicevuta(Connection connection,
			int aooId,Date dataSpedizione) throws DataException {
		areaorganizzativaDAO.aggiornaAOODataUltimaPecRicevuta(connection, aooId, dataSpedizione);
		
	}

}