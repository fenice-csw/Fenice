package it.compit.fenice.mvc.integration;

import it.compit.fenice.mvc.presentation.helper.VersioneCaricaView;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.vo.organizzazione.PermessoMenuVO;

import java.sql.Connection;
import java.util.Collection;

public interface CaricaDAO {

	CaricaVO getCarica(int id) throws DataException;

	CaricaVO newCarica(Connection connection, CaricaVO vo)throws DataException;

	void cancellaPermessi(Connection conn, int caricaId)throws DataException;

	void nuovoPermessoMenu(Connection connection, PermessoMenuVO permesso)throws DataException;
	
	CaricaVO updateCarica(Connection connection, CaricaVO vo)throws DataException;

	Collection<CaricaVO> getCariche()throws DataException;
	
	Collection<VersioneCaricaView> getStoriaCarica(int caricaId)throws DataException;

	CaricaVO getCarica(int utenteId, int ufficioId)throws DataException;

	boolean isCaricaAttiva(int uffId, int uteId)throws DataException;

	void cancellaCarica(Connection connection, Integer caricaId)throws DataException;

	int contaAssegnazioni(CaricaVO carica,int annoProtocolloDa, int annoProtocolloA, Utente utente)throws DataException;

	boolean removeResponsabileEnte()throws DataException;
	
	CaricaVO setResponsabileEnte(int caricaId)throws DataException;
	
	boolean removeResponsabileProtocollo(Connection connection)throws DataException;
	
	CaricaVO setResponsabileProtocollo(Connection connection, int caricaId)throws DataException;


}