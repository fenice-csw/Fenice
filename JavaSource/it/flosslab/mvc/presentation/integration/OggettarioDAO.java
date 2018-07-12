/*
*
* Copyright 2008 Flosslab s.r.l. (http://www.flosslab.it) All rights reserved.
*
* This file is part of e-prot 1.1 software.
* e-prot 1.1 is a free software; 
* you can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2008 Flosslab s.r.l. (http://www.flosslab.it) All rights reserved.
* Version: e-prot 1.1
*/

package it.flosslab.mvc.presentation.integration;

import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.flosslab.mvc.vo.OggettoVO;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/*
 * @author G.Calli.
 */

public interface OggettarioDAO {

    public OggettoVO newOggetto(Connection conn, OggettoVO oggettoVO)
            throws DataException;

    public void deleteOggetto(Connection conn, int id)throws DataException;
    
    public OggettoVO getOggetto(int id)throws DataException;

	public OggettoVO updateOggetto(Connection conn, OggettoVO oggettoVO)throws DataException;

	public void eliminaAssegnatari(Connection conn, String id)throws DataException;

	public void salvaAssegnatario(Connection conn, AssegnatarioVO assegnatario)throws DataException;

	public Map<Integer,AssegnatarioVO> getAssegnatari(int id)throws DataException;
	
	public boolean isOggettoAssegnatariPresent(int id)throws DataException;

	public OggettoVO getOggettoByDescrizione(String oggetto)throws DataException;
	
	public boolean isDescrizioneUsed(int id,String oggetto,int aooId) throws DataException;
	
	public List<Integer> getUfficiAssegnatariId(int id)throws DataException;
}