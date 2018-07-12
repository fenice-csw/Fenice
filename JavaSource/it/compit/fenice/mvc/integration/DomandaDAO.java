package it.compit.fenice.mvc.integration;

import it.compit.fenice.mvc.presentation.helper.DomandaView;
import it.compit.fenice.mvc.vo.protocollo.DomandaVO;
import it.finsiel.siged.exception.DataException;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DomandaDAO {

	void updateStato(Connection connection, int stato, String domandaId)throws DataException;

	Map<String[],DomandaView> getDomande()throws DataException;
	
	Map<String[],DomandaView> getDomande(int ufficioId)throws DataException;
	
	DomandaVO getDomandaById(String domandaId)throws DataException;
	
	List<DomandaVO> getDomandeVOByStato(int stato) throws DataException;

	Integer getProtocolloIdByDomandaId(String domandaId)throws DataException;
	
	void newIscrizioneProtocollata(Connection connection,String domandaId,int protocolloId,int numeroprotocollo,Date dataProtocollo)throws DataException;

	int getStato(Connection connection, String domandaId) throws DataException;

	DomandaView getDomandaViewById(String domandaId)throws DataException;
	
	Map<String[],DomandaView> getDomandeByStato(int stato)throws DataException;
	
	Map<String[],DomandaView> getDomandeAndUfficioByStato(int stato)throws DataException;
	
	Map<String[],DomandaView> getDomandeByStato(int stato,int ufficioId)throws DataException;

}