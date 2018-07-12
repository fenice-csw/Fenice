package it.compit.fenice.mvc.business;

import it.compit.fenice.mvc.integration.CdsDAO;
import it.compit.fenice.mvc.vo.cds.UtenteCdsVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.RegistroBO;
import it.finsiel.siged.mvc.business.RegistroDelegate;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;

import java.util.Map;

import org.apache.log4j.Logger;

public final class CdsDelegate {

	private static Logger logger = Logger.getLogger(CdsDelegate.class
			.getName());

	private CdsDAO cdsDAO = null;

	private static CdsDelegate delegate = null;

	private CdsDelegate() {
		try {
			if (cdsDAO == null) {
				cdsDAO = (CdsDAO) DAOFactory.getDAO(Constants.CDS_DAO);
				logger.debug("CdsDAO instantiated:"+ Constants.CDS_DAO_CLASS);
			}
		} catch (Exception e) {
			logger.error("Exception while connecting to UserDAOjdbc!!", e);
		}

	}

	public static CdsDelegate getInstance() {
		if (delegate == null)
			delegate = new CdsDelegate();
		return delegate;
	}

	public static String getIdentifier() {
		return Constants.UTENTE_DELEGATE;
	}


	public Utente getUtenteCDS() {
		Organizzazione org=Organizzazione.getInstance();
		Utente cdsUser=null;
		try {
			UtenteCdsVO cdsVO=cdsDAO.getUtenteCds();	
			cdsUser=org.getUtente(cdsVO.getUtenteId());
			cdsUser.setUfficioInUso(cdsVO.getUfficioId());
			cdsUser.setCaricaInUso(cdsVO.getCaricaId());
			Map<Integer,RegistroVO> registri = RegistroDelegate.getInstance().getRegistriUtente(cdsUser.getValueObject().getId());
			cdsUser.setRegistri(registri);
			cdsUser.setRegistroInUso(RegistroBO.getRegistroUfficialeId(registri.values()));
			cdsUser.setRegistroUfficialeId(RegistroBO.getRegistroUfficialeId(registri.values()));
			cdsUser.setRegistroPostaInterna(RegistroBO.getRegistroPostaInternaId(registri.values()));
			logger.info("CdsDelegate: getting User "+cdsUser.getValueObject().getUsername());
		} catch (DataException de) {
			logger.error("CdsDelegate: failed getting User");
		}
		return cdsUser;
	}
	
}