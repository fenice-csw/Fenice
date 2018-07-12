/*
 * Created on 14-mar-2005
 *
 */
package it.finsiel.siged.mvc.integration;

import it.compit.fenice.mvc.presentation.helper.CodaInvioView;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.presentation.helper.EmailView;
import it.finsiel.siged.mvc.vo.log.EventoVO;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.posta.CodaInvioVO;
import it.finsiel.siged.mvc.vo.posta.EmailVO;
import it.finsiel.siged.mvc.vo.posta.PecDestVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Almaviva sud
 * 
 */
public interface EmailDAO {

    public void salvaMessaggioDestinatariPerInvio(Connection connection, int id,
            int aooId, int protocolloId, Collection<DestinatarioVO> destinatari)
            throws DataException;
        
    public void salvaEmailCodaInvioDestinatari(Connection connection, int id, Collection<DestinatarioVO> destinatari) throws DataException;
    
    public void aggiornaMessaggioDestinataroPerInvio(Connection connection, int id, DestinatarioVO d) throws DataException;
    
    public void salvaMessaggioDestinatariPerInvioErsu(Connection connection, int id,
            int aooId, int protocolloId, SoggettoVO mittente)
            throws DataException;

    public Collection<Integer> getListaMessaggiUscita(int aooId) throws DataException;

    public Collection<EventoVO> getListaLog(int aooId, int tipoLog) throws DataException;

    public Collection<PecDestVO> getDestinatariMessaggioUscita(int msgId)
            throws DataException;

    public CodaInvioVO getMessaggioDaInviare(int id) throws DataException;

    public void segnaMessaggioComeInviato(int msgId) throws DataException;

    public void salvaAllegato(Connection connection, DocumentoVO documento,
            int email_id) throws DataException;

    public void salvaAllegatoUfficio(Connection connection, DocumentoVO documento,
            int email_id) throws DataException;
    
    public void salvaEmail(EmailVO email, Connection connection)
            throws DataException;

    public void salvaEmailUfficio(EmailVO email, Connection connection,int ufficioId)
    throws DataException;
    
    public void aggiornaFlagAnomalia(int emailId, Connection connection)
    throws DataException;
    
    public void salvaEmailLog(Connection connection,int id,
			String errore, int tipoLog, int aooId)
            throws DataException;

    public EmailVO getEmailEntrata(int emailId) throws DataException;

    public EmailVO getEmailEntrata(Connection connection, int emailId)
            throws DataException;

    public EmailVO getEmailUfficioEntrata(Connection connection, int emailId)
    throws DataException;
    
    public ArrayList<DocumentoVO> getAllegatiEmailEntrata(Connection connection, int emailId)
            throws DataException;

    public ArrayList<DocumentoVO> getAllegatiEmailUfficioEntrata(Connection connection, int emailId)
    throws DataException;
    
    public DocumentoVO getAllegatoEmailEntrata(int docId)
    throws DataException;
    
    public DocumentoVO getAllegatoEmailUfficio(int docId)
    throws DataException;
    
    public void writeDocumentoToStream(Connection connection, int docId,
            OutputStream os) throws DataException;

    public void writeAllegatoUfficioToStream(Connection connection, int docId,
            OutputStream os) throws DataException;

    public void writeAllegatoUfficioToStream(int docId,
            OutputStream os) throws DataException;
    
    public void writeDocumentoToStream(int docId, OutputStream os)
            throws DataException;

    public InputStream writeDocumentoToInputStream(int docId)
            throws DataException;

    
    public void aggiornaStatoEmailIngresso(Connection connection,
            int messaggioId, int stato) throws DataException;
    
    public void aggiornaStatoEmailUfficioIngresso(Connection connection,
            int messaggioId, int stato) throws DataException;

    public Collection<EmailView> getMessaggiDaProtocollare(Connection connection, int aooId)
            throws DataException;

    public Collection<EmailView> getMessaggiDaProtocollare(int aooId) throws DataException;
    
    public Collection<EmailView> getEmailIngressoByStato(int aooId, int stato) throws DataException;

    
    public Collection<EmailView> getMessaggiUfficioDaProtocollare(int ufficioId) throws DataException;
    
    public int countMessaggiUfficioDaProtocollare(int ufficioId) throws DataException;

    public void eliminaEmail(Connection connection, int emailId)
            throws DataException;

    public void eliminaEmailAllegati(Connection connection, int emailId)
    		throws DataException;
    
    public void eliminaEmailUfficioAllegati(Connection connection, int emailId)
	throws DataException;
    
    public void eliminaEmailLog(Connection connection, String[] ids) throws DataException;

	public boolean isMailPresent(Connection connection, EmailVO email) throws DataException;
	
	public boolean isMailLogPresent(Connection connection, int aooId) throws DataException;
	
	public boolean isMailUfficioPresent(Connection connection, EmailVO email,int ufficioId) throws DataException;
		
    public List<CodaInvioView> getListaMessaggiUscitaView(int aooId) throws DataException;
    
    public Map<Integer, CodaInvioView> getListaMessaggiUscitaView(int aooId, Date dataInizio, Date dataFine,String statoMail) throws DataException;
    
    public int countListaMessaggiUscita(int aooId, Date dataInizio, Date dataFine,String statoMail) throws DataException;

    public int countEmailIngresso(int aooId, Date dataInizio, Date dataFine,String statoMail) throws DataException;
    
    public Collection<EmailView> cercaEmailIngresso(int aooId, Date dataInizio, Date dataFine,String statoMail) throws DataException;

    public void eliminaEmailCodaInvioDestinatari(Connection connection, int emailId) throws DataException;
    
    public void eliminaEmailCodaInvio(Connection connection, int emailId) throws DataException;
    
    public CodaInvioView getMessaggioUscitaView(int emailId) throws DataException;
    
    public CodaInvioView getMessaggioUscitaViewByProtocolloId(int protocolloId) throws DataException;

	public void aggiornaEmailCodaInvio(Connection connection, int emailId, int stato) throws DataException;
	
	public void aggiornaEmailCodaInvioDestinatario(Connection connection, int emailId, String nominativo, String indirizzoEmail) throws DataException;

	public void aggiornaEmailLog(Connection connection, String errore, int tipoLog, int aooId) throws DataException;

	public EventoVO getMailLog(int aooId) throws DataException;
	
	EmailVO cercaEmailIngressoByMessageHeader(int aooId, String messageId, String generatedId) throws DataException;

}