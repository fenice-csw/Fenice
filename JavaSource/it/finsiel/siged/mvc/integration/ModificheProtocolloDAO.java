package it.finsiel.siged.mvc.integration;

import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.vo.protocollo.ModificaProtocolloVO;

import java.util.List;

/*
 * @author Marcello Spadafora.
 */

public interface ModificheProtocolloDAO {
    /**
     * @author Marcello Spadafora
     * @param dataInizio
     *            Data di registrazione minima
     * @param dataFine
     *            Data di registrazione massima
     * @param ufficioId
     *            Identificativo dell'ufficio
     * @return - una lista di
     * @link it.finsiel.siged.mvc.vo.protocollo.ModificaProtocolloVO
     */
    public List<ModificaProtocolloVO> getModifiche(int registroId, int ufficioId, long dataInizio,
            long dataFine) throws DataException;
    
    public int contaModifiche(int registroId, int ufficioId, long dataInizio,
            long dataFine) throws DataException;
}