package it.finsiel.siged.mvc.integration;

import it.compit.fenice.mvc.presentation.helper.ProtocolloProcedimentoView;
import it.compit.fenice.mvc.vo.protocollo.UfficioPartecipanteVO;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.presentation.helper.AllaccioView;
import it.finsiel.siged.mvc.presentation.helper.ProcedimentoView;
import it.finsiel.siged.mvc.vo.MigrazioneVO;
import it.finsiel.siged.mvc.vo.lookup.TipoProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProcedimentoFaldoneVO;
import it.finsiel.siged.mvc.vo.protocollo.ProcedimentoFascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.ProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloProcedimentoVO;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public interface ProcedimentoDAO {

	public AllaccioView getProtocolloAllacciabile(Utente utente, int annoProtocolo, int numeroProtocollo, int procedimentoId) throws DataException;
	
    public ProcedimentoVO newProcedimento(Connection connection, ProcedimentoVO vo) throws DataException;

    public ProcedimentoVO getProcedimentoById(Connection connection, int procedimentoId) throws DataException;
    
    public void salvaTipoProcedimento(Connection connection, TipoProcedimentoVO tipoProcedimentoVO) throws DataException;

    public ProcedimentoVO getProcedimentoById(int procedimentoId) throws DataException;

    public Collection<Integer> getProcedimentoFaldoni(Connection connection, int procedimentoId) throws DataException;

    public Map<String,ProtocolloProcedimentoView> getProcedimentoProtocolli(Connection connection, int procedimentoId) throws DataException;

    public Collection<Integer> getProcedimentoFascicoli(Connection connection, int procedimentoId) throws DataException;

    public Map<String,ProtocolloProcedimentoView> getStoriaProcedimentoProtocolli(Connection connection, int procedimentoId,int versione) throws DataException;

    public Collection<Integer> getStoriaProcedimentoFascicoli(Connection connection, int procedimentoId,int versione) throws DataException;
    
    public void inserisciFaldoni(Connection connection, Integer[] ids, int procedimentoId) throws DataException;

    public void inserisciFascicoli(Connection connection, Collection<ProcedimentoFascicoloVO> fascicoliProc) throws DataException;

    public void cancellaFaldoni(Connection connection, int procedimentoId) throws DataException;

    public void cancellaFascicoli(Connection connection, int procedimentoId) throws DataException;

    public int getMaxNumProcedimento(Connection connection, int aooId, int anno)throws DataException;

    public void inserisciProtocolli(Connection connection, ArrayList<ProtocolloProcedimentoVO> ids) throws DataException;

    public void cancellaProtocolli(Connection connection, int procedimentoId)throws DataException;

    public void setStatoProtocolloAssociato(Connection connection,int protocolloId, String stato) throws DataException;

    public SortedMap<Integer,ProcedimentoVO> cerca(Utente utente, HashMap<String,String> sqlDB,String soggettiId) throws DataException;
    
    public int contaProcedimenti(Utente utente, HashMap<String,String> sqlDB,String soggettiId) throws DataException ;
    
    public ProcedimentoVO getProcedimentoByAnnoNumero(Connection connection, int anno, int numero) throws DataException;
    
    public void inserisciFascicoloProcedimento(Connection connection, ProcedimentoFascicoloVO procedimentofascicolo) throws DataException;
    
    public void inserisciProcedimentoFaldone(Connection connection, ProcedimentoFaldoneVO procedimentoFaldoneVO) throws DataException;
    
    public Collection<MigrazioneVO> getProcedimentiAnnoNumero(Connection connection) throws DataException;
    
    public ProcedimentoVO newStoriaProcedimento(Connection connection,ProcedimentoVO vo) throws DataException;
    
    public ProcedimentoVO aggiornaProcedimento(Connection connection,  ProcedimentoVO vo) throws DataException;
    
    public Collection<ProcedimentoView> getStoriaProcedimenti(int procedimentoId) throws DataException;
    
    public ProcedimentoVO getProcedimentoByIdVersione(int id, int versione) throws DataException;
    
    public ProcedimentoVO getProcedimentoByIdVersione(Connection connection, int id, int versione) throws DataException;

    public Map<String,DocumentoVO> getRiferimenti(int id)throws DataException ;

	public void deleteRiferimenti(Connection connection, int procedimentoId)throws DataException ;

	public void salvaRiferimenti(Connection connection, int nextId, int procedimentoId, int intValue, int versione)throws DataException ;
	
	public Collection<AssegnatarioVO> getIstruttori(int id)throws DataException ;

	public void deleteIstruttori(Connection connection, int procedimentoId)throws DataException ;

	public void salvaIstruttori(Connection connection, int procedimentoId, int caricaId, boolean lavorato,int versione,String username)throws DataException ;
	
	public Map<String,ProcedimentoView> getProcedimentiAlert(int referenteId) throws DataException;
	
	public int contaProcedimentiAlert(int referenteId) throws DataException;
	
	public Map<String,ProcedimentoView> getRicorsi(int referenteId) throws DataException;
	
	public int contaRicorsi(int referenteId) throws DataException;
	
	public boolean setProcedimentoSospeso(int procedimentoId,int protocolloId, String estremiSospensione, String username) throws DataException;
	
	public boolean setProcedimentoSospeso(int procedimentoId, String estremiSospensione, String username) throws DataException;
	
	public boolean setProcedimentoChiuso(int procedimentoId, String username) throws DataException;

	public boolean setProcedimentoChiuso(Connection connection,int procedimentoId, String username) throws DataException;

	public boolean inviaProcedimento(Connection connection,int procedimentoId, String posizione,String username) throws DataException;
	
	public boolean cambiaStatoProcedimento(Connection connection,int procedimentoId, int stato,String posizione,String username) throws DataException;

	public boolean setProcedimentoAperto(Connection connection,int procedimentoId, String username) throws DataException;
	
	public boolean setProcedimentoArchiviato(Connection connection,int procedimentoId, String username) throws DataException;
	
	public boolean setProcedimentoRiavviato(int procedimentoId, String estremiSospensione, String dataScadenza,String username) throws DataException;

	public boolean setProcedimentoRiavviato(int procedimentoId,int protocolloId, String estremiSospensione, String dataScadenza,String username) throws DataException;

	public boolean annullaProcedimentoSospeso(int procedimentoId,String estremiSospensione,String username) throws DataException;
	
	public int getIntervalloTempoSospensione(int procedimentoId) throws DataException;

	public void salvaProcedimentoProtocollo(Connection connection, ProtocolloProcedimentoVO vo) throws DataException;
	
	public void cancellaProcedimentoProtocollo(Connection connection, ProtocolloProcedimentoVO vo) throws DataException;
	
	public void salvaProcedimentoFascicolo(Connection connection, int procedimentoId,int fascicoloId,String username,int versione) throws DataException;
	
	public void cancellaProcedimentoFascicolo(Connection connection, int procedimentoId,int fascicoloId) throws DataException;

	public Collection<AssegnatarioVO> getStoriaIstruttori(int procedimentoId, int versione)throws DataException;
	
	public boolean isProcedimentoVisualizzabile(int procedimentoId,int caricaId) throws DataException;
	
	public int contaProcedimentiIstruttore(int caricaId) throws DataException;
	
	public int contaProcedimentiUfficioPartecipante(int ufficioId) throws DataException;
	
	public Map<String,ProcedimentoView> getProcedimentiIstruttore(int caricaId) throws DataException;
	
	public Map<String,ProcedimentoView> getProcedimentiUfficioPartecipante(int ufficioId) throws DataException;

	public boolean setProcedimentoIstruttoreLavorato(int procedimentoId, int caricaId,String username)throws DataException;

	public void archiviaVersione(Connection connection, int procedimentoId, String username)throws DataException;
	
	public int getVersione(Connection connection, int procedimentoId) throws DataException;

	public ProcedimentoVO getProcedimentoVODaFascicolo(Connection connection, int fascicoloId)throws DataException;
	
	public int getProcedimentoIdDaFascicolo(Connection connection, int fascicoloId)throws DataException;

	public int getFascicoloIdDaProcedimento(Connection connection, int procedimentoId)throws DataException;

	public boolean cambiaReferenteProcedimento(Connection connection, int procedimentoId, int ufficioId, int caricaId,String username)throws DataException;

	public Collection<ProcedimentoView> getElencoDecreti(Collection<Integer> ids)throws DataException;
	
	public List<UfficioPartecipanteVO> getUfficiPartecipanti(int id)throws DataException ;

	public Map<String, UfficioPartecipanteVO> getUfficiPartecipantiMap(int id) throws DataException;
	
	public void deleteUfficiPartecipanti(Connection connection, int procedimentoId)throws DataException ;

	public void salvaUfficioPartecipante(Connection connection, int nextId, int tipoProcedimentoId, int ufficioId, boolean visibilita, int versione, String username)throws DataException;

}