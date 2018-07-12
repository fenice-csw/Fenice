package it.finsiel.siged.mvc.integration;

import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.compit.fenice.mvc.vo.organizzazione.StoricoOrganigrammaVO;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.mvc.presentation.helper.UtenteView;
import it.finsiel.siged.mvc.vo.organizzazione.TitolarioUfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;

import java.sql.Connection;
import java.util.Collection;
import java.util.Date;

/*
 * @author G.Calli.
 */

public interface UfficioDAO {

    public UfficioVO nuovoUfficio(Connection conn, UfficioVO ufficioVO)
            throws DataException;

    public void cancellaUfficio(Connection conn, int ufficioId)
            throws DataException;

    public UfficioVO modificaUfficio(Connection conn, UfficioVO ufficioVO)
            throws DataException;

    public boolean isUfficioCancellabile(int ufficioId) throws DataException;

    public void salvaUfficiTitolari(Connection conn,
            TitolarioUfficioVO titolarioufficioVO) throws DataException;

    public Collection<UfficioVO> getUfficiByParent(int ufficioId) throws DataException;

    public Collection<UtenteView> getUtentiByUfficio(int ufficioId) throws DataException;

    public UfficioVO getUfficioVO(int ufficioId) throws DataException;
    
    public UfficioVO getUfficioVOByDescrizione(String descrizione) throws DataException;
    
    public Collection<UfficioVO> getUfficiByUtente(int utenteId) throws DataException;
    
    public Collection<UfficioVO> getUffici() throws DataException;
    
    public Collection<UfficioVO> getUffici(int aooId) throws DataException;

    public void aggiornaUtentiReferenti(Connection conn, String[] caricheId) throws DataException;

    public String[] getReferentiByUfficio(int ufficioId) throws DataException;

    public int getNumeroReferentiByUfficio(int ufficioId) throws DataException;

    public void inserisciUtentiReferenti(Connection conn, int caricaId) throws DataException;

    public long getCountUffici() throws DataException;
    
    public boolean isCaricaReferenteUfficio(int caricaId) throws DataException;

	public Collection<UtenteView> getCaricheByUfficio(int ufficioId)throws DataException;

	public Collection<TitolarioUfficioVO> getTitolarioByUfficio(int ufficioId)throws DataException;
	
	public void cancellaCaricheReferenti(Connection connection, int intValue)throws DataException;

	public void aggiornaCaricheReferenti(Connection connection, String[] cariche)throws DataException;

	public void cancellaCaricheDirigenti(Connection connection, int intValue)throws DataException;

	public void aggiornaCaricheDirigenti(Connection connection, UfficioVO ufficio)throws DataException;
	
	public Collection<CaricaVO> getCaricheVOByUfficio(int ufficioId)throws DataException;

	public int countAssegnazioni(Ufficio uff)throws DataException;

	public boolean setUfficioAttivo(int id, boolean attivo)throws DataException;

	public boolean isUfficioAttivo(int id)throws DataException;
	
	public boolean removeUfficioProtocollo()throws DataException;
	
	public UfficioVO setUfficioProtocollo(int id)throws DataException;

	public void salvaStoricoOrganigramma(Connection connection, Integer id, StoricoOrganigrammaVO vo)throws DataException;
	
	public Collection<StoricoOrganigrammaVO> getStoricoOrganigrammaCollection(int aooId) throws DataException;
	
	public StoricoOrganigrammaVO getStoricoOrganigramma(int aooId) throws DataException;
	
	public void aggiornaUfficioDataUltimaMailRicevuta(Connection connection, int aooId, Date dataSpedizione)throws DataException;

}