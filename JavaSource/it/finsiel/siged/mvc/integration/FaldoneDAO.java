package it.finsiel.siged.mvc.integration;

import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.presentation.helper.FaldoneView;
import it.finsiel.siged.mvc.vo.MigrazioneVO;
import it.finsiel.siged.mvc.vo.protocollo.FaldoneFascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.FaldoneVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.SortedMap;

public interface FaldoneDAO {

    public Collection<FaldoneVO> getFaldoniPerAoo(int aoo_id) throws DataException;

    public int getMaxNumFaldone(Connection connection, int aooId,
            int annoCorrente) throws DataException;

    public FaldoneVO newFaldone(Connection connection, FaldoneVO faldoneVO)
            throws DataException;

    // public FaldoneVO aggiornaFaldone(Connection connection, FaldoneVO faldoneVO)throws DataException;
    
    public FaldoneVO updateFaldone(Connection connection, FaldoneVO faldoneVO)
            throws DataException;

    public FaldoneVO getFaldone(int faldoneId) throws DataException;

    //public Collection getStatiFaldone() throws DataException;

    //public IdentityVO getStatoFaldone(int statoId) throws DataException;

    public FaldoneVO getFaldone(Connection connection, int faldoneId)
            throws DataException;

    public void cancellaFaldoneFascicoli(Connection connection, int faldoneId)
            throws DataException;

    public void insertFaldoneFascicolo(Connection connection,
            FascicoloVO fascicolo, int faldoneId, String utente)
            throws DataException;

    public Collection<Integer> getFaldoneFascicoliIds(Connection connection,
            int faldoneId) throws DataException;

    public int contaFaldoni(Utente utente, String ufficiUtenti, HashMap<String,String> sqlDB)
            throws DataException;

    public  SortedMap<Integer,FaldoneView> cercaFaldoni(Utente utente, String ufficiUtenti,
            HashMap<String,String>  sqlDB) throws DataException;

    public Collection<Integer> getFaldoneProcedimentiIds(Connection connection,
            int faldoneId) throws DataException;

    public void insertFaldoneProcedimento(Connection connection,
            int procedimentoId, int faldoneId, String utente)
            throws DataException;

    public void cancellaFaldoneProcedimenti(Connection connection, int faldoneId)
            throws DataException;

    public int cancellaFaldone(int faldoneId);

    public Collection<FaldoneView> getStoriaFaldone(int faldoneId) throws DataException;

    public FaldoneVO getFaldoneByIdVersione(int faldoneId, int versione)
            throws DataException;

    public void insertFaldoneFascicolo(Connection connection,
            FaldoneFascicoloVO faldonefascicoloVO, int versioneFascicolo)
            throws DataException;

    public FaldoneVO getFaldone(Connection connection, int anno, int numero)
            throws DataException;

    public Collection<MigrazioneVO> getFaldoniAnnoNumero(Connection connection)
            throws DataException;
}