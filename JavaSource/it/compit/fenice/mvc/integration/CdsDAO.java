package it.compit.fenice.mvc.integration;

import it.compit.fenice.mvc.vo.cds.UtenteCdsVO;
import it.finsiel.siged.exception.DataException;

public interface CdsDAO {

	
	UtenteCdsVO getUtenteCds()throws DataException;

		
}
