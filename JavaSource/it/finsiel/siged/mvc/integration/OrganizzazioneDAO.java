package it.finsiel.siged.mvc.integration;

import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.vo.organizzazione.AmministrazioneVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;

import java.util.ArrayList;
import java.util.Collection;

public interface OrganizzazioneDAO {
    public AmministrazioneVO getAmministrazione() throws DataException;

    public Collection<UfficioVO> getUffici() throws DataException;

    public ArrayList<Integer> getIdentificativiUffici(int utenteId) throws DataException;
    
    public ArrayList<Integer> getIdentificativiCariche(int utenteId) throws DataException;

    public AmministrazioneVO updateAmministrazione(AmministrazioneVO ammVO)
            throws DataException;

    public AmministrazioneVO updateParametriFTP(AmministrazioneVO ammVO)
    throws DataException;

	public CaricaVO getCaricaResponsabile() throws DataException;

	public CaricaVO getCaricaResponsabileUfficioProtocollo() throws DataException;
	
	public String getNomeCarica(int uffId, int uteId) throws DataException;
	
}