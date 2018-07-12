package it.finsiel.siged.mvc.integration;

import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.vo.IdentityVO;

import java.sql.Connection;
import java.util.Collection;

/*
 * @author G.Calli.
 */

public interface TitoliDestinatarioDAO {

    // public Collection getListaTitolario(int aoo, String codice,
    // String descrizione) throws DataException;

    // public Collection getTitolariByParent(int ufficioId, int parentId)
    // throws DataException;

    public Collection<IdentityVO> getElencoTitoliDestinatario() throws DataException;

    public IdentityVO newTitoloDestinatario(Connection conn, IdentityVO titoloVO)
            throws DataException;

    public IdentityVO getTitoloDestinatario(int id) throws DataException;

    public IdentityVO updateTitolo(Connection conn, IdentityVO titoloVO)
            throws DataException;

    public void deleteTitolo(int titoloId) throws DataException;

    public boolean esisteTitolo(String descrizione) throws DataException;

    public IdentityVO getTitoloDestinatarioDaTitolo(String titolo)
            throws DataException;

    // public void deleteArgomentoUffici(Connection conn, int titolarioId)
    // throws DataException;
    //    
    // public boolean getArgomentoProtocolli(int titolarioId) throws
    // DataException;
    //
    // public Collection getTitolariByParent(int parentId) throws DataException;
    //
    // public IdentityVO getTitolo(int titoloId) throws DataException;
    //
    // public String[] getUfficiTitolario(int titolarioId) throws DataException;

    // public void associaArgomentoUffici(Connection conn,
    // TitolarioVO titolarioVO, String[]uffici) throws DataException;

}