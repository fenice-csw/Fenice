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
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;

import java.sql.Connection;
import java.util.List;


/*
 * @author G.Calli.
 */

public interface MittentiDAO {

	public void saveMittente(Connection conn, SoggettoVO mittente, int idProtocollo, int idMittente, int versione) throws DataException ;

	public List<SoggettoVO> getMittenti(Connection conn, int protocolloId) throws DataException;
   
	public List<SoggettoVO> getStoriaMittenti(Connection conn, int protocolloId,int versione) throws DataException;
}