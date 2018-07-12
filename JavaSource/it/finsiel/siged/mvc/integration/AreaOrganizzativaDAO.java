package it.finsiel.siged.mvc.integration;

import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.vo.organizzazione.AreaOrganizzativaVO;
import it.finsiel.siged.mvc.vo.organizzazione.MailConfigVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;

import java.sql.Connection;
import java.util.Collection;
import java.util.Date;

/*
 * @author G.Calli.
 */

public interface AreaOrganizzativaDAO {

    //public Collection<AreaOrganizzativaVO> getAreeOrganizzative() throws DataException;
	
	public Collection<Integer> getAreeOrganizzativeId() throws DataException;

    public AreaOrganizzativaVO getAreaOrganizzativa(int areaorganizzativaId)
            throws DataException;

    public AreaOrganizzativaVO getAreaOrganizzativa(Connection connection,
            int areaorganizzativaId) throws DataException;

    public MailConfigVO getMailConfigByAooId(int areaorganizzativaId) throws DataException;
    
    public AreaOrganizzativaVO newAreaOrganizzativa(Connection conn,
            AreaOrganizzativaVO areaorganizzativaVO) throws DataException;
    
    public MailConfigVO newMailConfig(Connection conn,
    		MailConfigVO mailConfigVO) throws DataException;

    public AreaOrganizzativaVO updateAreaOrganizzativa(Connection conn,
            AreaOrganizzativaVO areaorganizzativaVO) throws DataException;

    public MailConfigVO updateMailConfig(Connection conn,
    		MailConfigVO mailConfigVO) throws DataException;
    
    public void cancellaAreaOrganizzativa(Connection conn,
            int areaorganizzativaId) throws DataException;

    public void cancellaMailConfig(Connection conn,
            int areaorganizzativaId) throws DataException;
    
    public boolean isAreaOrganizzativaCancellabile(int aooId)
            throws DataException;

    public boolean esisteAreaOrganizzativa(String aooDescrizione, int aooId)
            throws DataException;

    public boolean isModificabileDipendenzaTitolarioUfficio(int aooId)
            throws DataException;

    public Collection<UfficioVO> getUffici(int areaorganizzativaId) throws DataException;

	public Collection<UtenteVO> getUtenti(int areaorganizzativaId)throws DataException;

	public Collection<UtenteVO> getUtentiCarica(int areaorganizzativaId)throws DataException;
	
	public void cancellaUtenteResponsabile(Connection connection,int aooId)throws DataException;

	public void salvaUtenteResponsabile(Connection connection,int aooId, Integer id)throws DataException;
	
	public int getUtenteResponsabile(int aooId)throws DataException;

	public void aggiornaAOODataUltimaPecRicevuta(Connection connection, int aooId, Date dataSpedizione)throws DataException;
}