package it.finsiel.siged.mvc.integration;

import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.presentation.helper.SessioniEmergenzaView;

import java.util.Collection;

public interface RegistroEmergenzaDAO {

    public Collection<ReportProtocolloView> getProtocolliPrenotati(int registroId)
            throws DataException;

    public int getNumeroProtocolliPrenotati(int registroId)
            throws DataException;

	public Collection<SessioniEmergenzaView> getSessioniAperte(int registroId)  
			throws DataException;

}
