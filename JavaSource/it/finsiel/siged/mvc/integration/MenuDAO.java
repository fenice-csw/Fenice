package it.finsiel.siged.mvc.integration;

import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.vo.organizzazione.MenuVO;

import java.util.Collection;

public interface MenuDAO {
    public Collection<MenuVO> getAllMenu() throws DataException;

    public boolean isUserEnabled(int utenteId, int ufficioId, int menuId)
            throws DataException;

	public boolean isChargeEnabled( int caricaInUso, int menuId)throws DataException;
	
	public boolean isChargeEnabled( int caricaInUso, String title)throws DataException;
	
}