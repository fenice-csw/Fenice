package it.finsiel.siged.mvc.integration;

import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.compit.fenice.mvc.vo.protocollo.UfficioPartecipanteVO;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.vo.IdentityVO;
import it.finsiel.siged.mvc.vo.log.LogAcquisizioneMassivaVO;
import it.finsiel.siged.mvc.vo.lookup.AmministrazionePartecipanteVO;
import it.finsiel.siged.mvc.vo.lookup.ProfiloVO;
import it.finsiel.siged.mvc.vo.lookup.TipoDocumentoVO;
import it.finsiel.siged.mvc.vo.lookup.TipoProcedimentoVO;
import it.finsiel.siged.mvc.vo.organizzazione.MenuVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.SpedizioneVO;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

// import java.sql.Connection;

/*
 * @author G.Calli.
 */

public interface AmministrazioneDAO {
    public ArrayList<SpedizioneVO>  getSpedizioni(String descrizioneSpedizione, int aooId)
            throws DataException;

    public SpedizioneVO getMezzoSpedizione(int id) throws DataException;

    public void newMezzoSpedizione(Connection conn, SpedizioneVO spedizioneVO)
            throws DataException;

    public SpedizioneVO aggiornaMezzoSpedizione(Connection conn,
            SpedizioneVO spedizioneVO) throws DataException;

    public boolean cancellaMezzoSpedizione(Connection conn, int spedizioneId)
            throws DataException;

    public ArrayList<MenuVO> getFunzioniMenu() throws DataException;
    
    public ArrayList<MenuVO> getFunzioniMenuByFunction(int function) throws DataException;

    public ProfiloVO getProfilo(int profiloId) throws DataException;

    public ProfiloVO newProfilo(Connection conn, ProfiloVO profiloVO)
            throws DataException;

    public ArrayList<ProfiloVO> getProfili(int aooId) throws DataException;

    public ProfiloVO updateProfilo(Connection conn, ProfiloVO profiloVO)
            throws DataException;

    public void cancellaProfilo(int profiloId) throws DataException;

    public Collection<String> getMenuByProfilo(int profiloId) throws DataException;

    public TipoDocumentoVO getTipoDocumento(int tipoDocumentoId)
            throws DataException;

    public ArrayList<TipoDocumentoVO> getTipiDocumento(int aooId) throws DataException;

    public TipoDocumentoVO newTipoDocumento(Connection conn,
            TipoDocumentoVO tipoDocumentoVO) throws DataException;

    public TipoDocumentoVO updateTipoDocumento(Connection conn,
            TipoDocumentoVO tipoDocumentoVO) throws DataException;

    public boolean isTipoDocumentoUtilizzato(Connection connection,
            int tipoDocumentoId) throws DataException;

    public boolean cancellaTipoDocumento(Connection conn, int tipoDocumentoId)
            throws DataException;

    public TipoProcedimentoVO getTipoProcedimento(int tipoProcedimentoId)
            throws DataException;

    public ArrayList<TipoProcedimentoVO> getTipiProcedimento(int aooId ) throws DataException;
    
    public ArrayList<TipoProcedimentoVO> getTipiProcedimentoByUfficio(int ufficioCorrente) throws DataException;
    
    public ArrayList<TipoProcedimentoVO> getTipiProcedimentoByUfficioPrincipale(int ufficioCorrente) throws DataException;

    public TipoProcedimentoVO newTipoProcedimento(Connection conn,
            TipoProcedimentoVO tipoProcedimentoVO) throws DataException;

    public TipoProcedimentoVO updateTipoProcedimento(Connection conn,
            TipoProcedimentoVO tipoProcedimentoVO) throws DataException;

    public boolean cancellaTipoProcedimento(Connection conn,
            TipoProcedimentoVO tipoProcedimentoVO) throws DataException;

    public ArrayList<String> getUfficiPerTipoProcedimento(Connection conn,
            int tipoProcedimentoId) throws DataException;

    public boolean isTipoProcedimentoUtilizzato(Connection connection,
            String tipo, int tipoProcedimentoId, int ufficioId)
            throws DataException;

    public ArrayList<TipoProcedimentoVO> getTipiProcedimentoByTitolario(int titolarioId)
    throws DataException;
    
    public boolean cancellaTipoProcedimento(Connection conn,
            int tipoProcedimentoId, int idUfficio) throws DataException;
    
    public boolean cancellaTipoProcedimento(Connection conn,
            int tipoProcedimentoId) throws DataException;

    public ArrayList<LogAcquisizioneMassivaVO> getLogsAcquisizioneMassiva(int aooId) throws DataException;

    public boolean cancellaLogsAcquisizioneMassiva(int aooId)
            throws DataException;

    public LogAcquisizioneMassivaVO newLogAcquisizioneMassivaVO(
            Connection conn, LogAcquisizioneMassivaVO logVO)
            throws DataException;

    public Collection<IdentityVO> getElencoTitoliDestinatario() throws DataException;

    public IdentityVO newTitoloDestinatario(Connection conn, IdentityVO titoloVO)
            throws DataException;

    public IdentityVO getTitoloDestinatario(int id) throws DataException;

    public IdentityVO updateTitolo(Connection conn, IdentityVO titoloVO)
            throws DataException;

    public boolean deleteTitolo(int titoloId) throws DataException;

    public boolean esisteTitolo(String descrizione) throws DataException;

    public IdentityVO getTitoloDestinatarioDaTitolo(String titolo)
            throws DataException;

    public boolean esisteTitoloInProtocolloDestinatari(int id)
            throws DataException;

    public boolean esisteTitoloInStoriaProtDest(int id) throws DataException;

    public boolean esisteTitoloInInvioClassificatiProtDest(int id)
            throws DataException;

    public boolean esisteTitoloInInvioFascicoliDestinatari(int id)
            throws DataException;
    
    public ArrayList<TipoProcedimentoVO> getTipiProcedimento(int aooId, String descrizione)
    throws DataException;

	public Collection<UtenteVO> getUtentiByProfilo(Connection connection,
			Integer id)throws DataException ;

	public Collection<CaricaVO> getCaricheByProfilo(Connection connection,
			Integer id)throws DataException ;

	public Map<Integer,AmministrazionePartecipanteVO> getAmministrazioniPartecipanti(int id)throws DataException ;

	public Map<String,DocumentoVO> getRiferimenti(int id)throws DataException ;

	public void eliminaAmministrazioniPartecipanti(Connection connection,
			int intValue)throws DataException ;

	public void salvaAmministrazionePartecipante(Connection connection,
			AmministrazionePartecipanteVO amm)throws DataException ;

	public void deleteRiferimenti(Connection connection, int tipoProcedimentoId)throws DataException ;

	public void salvaRiferimenti(Connection connection, int nextId,
			int tipoProcedimentoId, int intValue, int versione)throws DataException ;

	public TipoProcedimentoVO getTipoProcedimentoByProcedimento(
			int procedimentoId)throws DataException ;
	
	public void deleteUfficiPartecipanti(Connection connection, int tipoProcedimentoId)throws DataException ;

	public void salvaUfficioPartecipante(Connection connection, int nextId, int tipoProcedimentoId, int ufficioId, boolean principale, int versione)throws DataException;
	
	public List<UfficioPartecipanteVO> getUfficiPartecipanti(int id)throws DataException ;

	public Map<String, UfficioPartecipanteVO> getUfficiPartecipantiForProcedimento(int id) throws DataException;
	
}