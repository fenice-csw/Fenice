package it.finsiel.siged.mvc.integration;

import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.presentation.helper.FascicoloView;
import it.finsiel.siged.mvc.presentation.helper.ProcedimentoView;

import java.util.Collection;
import java.util.HashMap;

/*
 * @author G.Calli.
 */

public interface EvidenzaDAO {

    public Collection<ProcedimentoView> getEvidenzeProcedimenti(Utente utente, HashMap<String,String> sqlDB)
            throws DataException;

    public Collection<FascicoloView> getEvidenzeFascicoli(Utente utente, HashMap<String,String> sqlDB)
            throws DataException;

    public int contaEvidenzeProcedimenti(Utente utente, HashMap<String,String> sqlDB)
            throws DataException;

    public int contaEvidenzeFascicoli(Utente utente, HashMap<String,String> sqlDB)
            throws DataException;

}