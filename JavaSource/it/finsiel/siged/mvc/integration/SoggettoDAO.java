package it.finsiel.siged.mvc.integration;

import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.vo.IdentityVO;
import it.finsiel.siged.mvc.vo.ListaDistribuzioneVO;
import it.finsiel.siged.mvc.vo.RubricaListaVO;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;

public interface SoggettoDAO {
    public ArrayList<SoggettoVO> getListaPersonaFisica(int aooId,String cognome, String nome,
            String codiceFiscale,String comune) throws DataException;

    public ArrayList<SoggettoVO> getListaPersonaGiuridica(int aooId,String denominazione, String pIva,String comune )
            throws DataException;

    public SoggettoVO getPersonaGiuridica(int id) throws DataException;

    public SoggettoVO getPersonaGiuridica(Connection connection, int id)
            throws DataException;
    
    public String getMailFormPersonaId(Connection connection, int id)
            throws DataException;

    public SoggettoVO getPersonaFisica(int id) throws DataException;

    public SoggettoVO getPersonaFisica(Connection connection, int id)
            throws DataException;

    public SoggettoVO newPersonaFisica(Connection conn, SoggettoVO personaFisica)
            throws DataException;

    public SoggettoVO newPersonaGiuridica(Connection conn,
            SoggettoVO personaGiuridica) throws DataException;

    public SoggettoVO editPersonaGiuridica(Connection conn,
            SoggettoVO personaGiuridica) throws DataException;

    public SoggettoVO editPersonaFisica(Connection conn,
            SoggettoVO personaFisica) throws DataException;

    public int deleteSoggetto(Connection conn, long id) throws DataException;

    /* Lista Distribuzione */

    public ListaDistribuzioneVO newListaDistribuzione(Connection conn,
            ListaDistribuzioneVO listaDistribuzione,String flagTipo) throws DataException;

    public ListaDistribuzioneVO editListaDistribuzione(Connection conn,
            ListaDistribuzioneVO ListaDistribuzione) throws DataException;

    public int deleteListaDistribuzione(Connection conn, long id)
            throws DataException;

    public ArrayList<IdentityVO> getElencoListaDistribuzione(String descrizione, int aooId,String flagTipo)
            throws DataException;

    public ListaDistribuzioneVO getListaDistribuzione(int id)
            throws DataException;

    public ArrayList<IdentityVO> getElencoListeDistribuzione(String flagTipo) throws DataException;

    public ArrayList<SoggettoVO> getDestinatariListaDistribuzione(int lista_id)
            throws DataException;

    public Collection<AssegnatarioVO> getAssegnatariListaDistribuzione(int lista_id)
    throws DataException;
    
    public void inserisciSoggettoLista(Connection connection, int listaId,
            int soggettoId, String tipoSoggetto, String utente)
            throws DataException;

    public int deleteRubricaListaDistribuzione(Connection connection, long id)
            throws DataException;

    public int deleteAssegnatariListaDistribuzione(Connection connection, long id)
    throws DataException;
    
    public void inserisciSoggettoLista(Connection connection,
            RubricaListaVO rubricaLista) throws DataException;
 
    public ArrayList<SoggettoVO> getListaPersonaFisicaByCognome(String cognome) throws DataException;

    public ArrayList<SoggettoVO> getListaPersonaFisicaByCognomeNome(String cognomeNome) throws DataException;
    
	public SoggettoVO getSoggettoByMail(Integer aooId, String email) throws DataException;
	
	public boolean isMailUsed(Integer aooId, String email) throws DataException;

	public SoggettoVO getSoggettoById(Integer id)throws DataException;
	
	public void eliminaAssegnatariListaDistribuzione(Connection connection,
            int listaId) throws DataException;
	
	 public void salvaAssegnatarioListaDistribuzione(Connection connection, int listaId,
			 AssegnatarioVO ass,String utente) throws DataException;

	public String getTipoPersona(Connection connection,int id) throws DataException;


}