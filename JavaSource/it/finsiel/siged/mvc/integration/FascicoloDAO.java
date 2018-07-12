package it.finsiel.siged.mvc.integration;

import it.compit.fenice.enums.TipoVisibilitaUfficioEnum;
import it.compit.fenice.mvc.vo.protocollo.DocumentoFascicoloVO;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.presentation.helper.FascicoloInvioView;
import it.finsiel.siged.mvc.presentation.helper.FascicoloView;
import it.finsiel.siged.mvc.presentation.helper.ProtocolloFascicoloView;
import it.finsiel.siged.mvc.vo.MigrazioneVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.InvioFascicoliDestinatariVO;
import it.finsiel.siged.mvc.vo.protocollo.InvioFascicoliVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloFascicoloVO;

import java.sql.Connection;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

public interface FascicoloDAO {
    public FascicoloVO newFascicolo(Connection connection, FascicoloVO fascicolo)
            throws DataException;

    public FascicoloVO getFascicoloById(Connection connection, int id)
            throws DataException;

    public Map<Integer,FascicoloView> getFascicoliArchivioDeposito(Utente utente,int stato) throws DataException;
    
    public FascicoloVO getFascicoloById(int id) throws DataException;
    
    public FascicoloVO getFascicoloByCodice(String codice) throws DataException;
    
    public Integer getTitolarioByFascicoloId(int id) throws DataException;

    public Collection<FascicoloVO> getFascicoloByAooId(int id) throws DataException;

    public void salvaDocumentoFascicolo(Connection connection,
            FascicoloVO fascicolo, int documentoId, String utente, int ufficioProprietarioId)
            throws DataException;

    public void salvaProtocolloFascicolo(Connection connection,
            int fascicoloId, int protocolloId, String utente, int ufficioProprietarioId, int versione)
            throws DataException;

    public FascicoloVO aggiornaFascicolo(FascicoloVO fascicolo)
            throws DataException;

    public FascicoloVO aggiornaFascicolo(Connection connection,
            FascicoloVO fascicolo) throws DataException;

    public int deleteFascicolo(int fascicoloId) throws DataException;

    public void deleteDocumentoFascicolo(Connection conn, int fascicoloId,
            int documentoId, int versione) throws DataException;

    public void deleteFascicoliProtocollo(Connection conn, int protocolloId)
            throws DataException;

    public void deleteFascicoliDocumento(Connection conn, int documentoId)
            throws DataException;

    public Collection<FascicoloVO> getFascicoliByProtocolloId(Connection connection,
            int protocolloId) throws DataException;

    public Collection<FascicoloVO> getFascicoliByProtocolloId(int protocolloId)
            throws DataException;

    public Collection<Integer> getCollegatiIdByFascicoloId(int fascicoloId)
    throws DataException;
    
    public Collection<Integer> getSottoFascicoliIdByFascicoloId(int fascicoloId)
    throws DataException;
    
    public Collection<FascicoloVO> getFascicoliByDocumentoId(Connection connection,
            int protocolloId) throws DataException;

    public Collection<FascicoloVO> getStoriaFascicoliByDocumentoId(Connection connection,
            int documentoId, int versione) throws DataException;

    public Collection<FascicoloVO> getFascicoliByDocumentoId(int protocolloId)
            throws DataException;

    public Collection<ProtocolloFascicoloView> getProtocolliFascicolo(int fascicoloId, Utente utente)
            throws DataException;

    public Collection<ProtocolloFascicoloVO> getProtocolliFascicoloById(int fascicoloId)
            throws DataException;

    public Collection<Integer> getProtocolloFascicoloByIdAndNumeroProtocollo(int fascicoloId,int numProt)
    throws DataException;
    
    public Collection<DocumentoFascicoloVO> getDocumentiFascicoloById(int fascicoloId)
            throws DataException;

    public void deleteDocumentoFascicolo(int fascicoloId, int documentoId,
            int versione) throws DataException;

    public void annullaInvioFascicolo(Connection conn, int fascicoloId,
            int versione) throws DataException;

    public void annullaInvioFascicolo(int fascicoloId, int versione)
            throws DataException;

    public void salvaDestinatariInvioFascicolo(Connection connection,
            InvioFascicoliDestinatariVO ifdVO) throws DataException;

    public void salvaDocumentiInvioFascicolo(Connection connection,
            InvioFascicoliVO ifVO) throws DataException;

    public int aggiornaStatoFascicolo(Connection connection, int fascicoloId,
            int stato, String userName, int versione) throws DataException;

    public boolean aggiungiCollegamentoFascicolo(int fascicoloId,
            int allaccioId, String userName) throws DataException;
    
    public boolean aggiungiCollegamentoFascicolo(Connection conn,int fascicoloId,
            int allaccioId, String userName) throws DataException;
    
    public boolean esisteCollegamento(Connection connection, int fascicoloId,
			int allaccioId) throws DataException;
    
    public int scartaFascicolo(Connection connection, int fascicoloId,
            String destinazioneScarto, String userName, int versione)
            throws DataException;

    public SortedMap<Integer,FascicoloInvioView> getFascicoliArchivioInvio(int aooId) throws DataException;

    public Collection<InvioFascicoliVO> getDocumentiFascicoliInvio(int fascicoloId)
            throws DataException;

    public Map<String,DestinatarioVO> getDestinatariFascicoliInvio(Connection connection,
            int fascicoloId) throws DataException;

    public Map<String,DestinatarioVO> getDestinatariFascicoliInvio(int fascicoloId)
            throws DataException;

    public int eliminaCodaInvioFascicolo(Connection connection, int fascicoloId)
            throws DataException;

    public boolean esisteFascicoloInCodaInvio(int fascicoloId)
            throws DataException;

    public int getMaxProgrFascicolo(Connection connection, int aooId, int anno)
            throws DataException;

    public FascicoloView getFascicoloViewById(int id) throws DataException;

    public FascicoloView getFascicoloViewById(Connection connection, int id)
            throws DataException;

    public Collection<Integer> getFaldoniFascicoloById(int fascicoloId)
            throws DataException;

    public Collection<Integer> getProcedimentiFascicoloById(int fascicoloId)
            throws DataException;

    public void deleteFascicoloProtocollo(Connection conn, int protocolloId,
            int fascicoloId) throws DataException;

    public Collection<FascicoloView> getStoriaFascicolo(int fascicoloId) throws DataException;

    public Collection<Integer> getFaldoniFascicoloByIdVersione(int fascicoloId,
            int versione) throws DataException;

    public Collection<Integer> getProcedimentiFascicoloByIdVersione(int fascicoloId,
            int versione) throws DataException;

    public Collection<Integer> getDocumentiFascicoloByIdVersione(int fascicoloId,
            int versione) throws DataException;

    public Collection<Integer> getProtocolliFascicoloByIdVersione(int fascicoloId,
            int versione) throws DataException;

    public FascicoloVO getFascicoloByIdVersione(int fascicoloId, int versione)
            throws DataException;

    public void salvaProtocolloFascicolo(Connection connection,
            ProtocolloFascicoloVO protocollofascicoloVO) throws DataException;

    public FascicoloVO getFascicoloByProgressivo(Connection connection,
            int anno, int progressivo) throws DataException;

    public FascicoloVO newFascicoloStoria(Connection connection,
            FascicoloVO fascicolo) throws DataException;

    public Collection<MigrazioneVO> getFascicoliAnnoNumero(Connection connection)
            throws DataException;

    public Collection<FascicoloView> getFascicoli(Utente utente, String progressivo, int anno,
            String oggetto, String note, String stato, int titolarioId,
            java.util.Date dataAperturaDa, java.util.Date dataAperturaA,
            Date dataEvidenzaDa, Date dataEvidenzaA, int ufficioId,int referenteId,int istruttoreId,String ids,String comune,String capitolo,String collocazioneValore1, String collocazioneValore2, String collocazioneValore3, String collocazioneValore4)
            throws DataException;

    public int contaFascicoli(Utente utente, String progressivo, int anno,
            String oggetto, String note, String stato, int titolarioId,
            Date dataAperturaDa, Date dataAperturaA, Date dataEvidenzaDa,
            Date dataEvidenzaA, int ufficioId,int referenteId,int istruttoreId,String ids,String comune,String capitolo,String collocazioneValore1, String collocazioneValore2, String collocazioneValore3, String collocazioneValore4) throws DataException;

    public int contaFascicoliPerDeposito(Utente utente,Date dataDa, Date dataA) throws DataException;

    public Map<Integer,FascicoloView>  getFascicoliPerDeposito(Utente utente,java.util.Date dataDa, java.util.Date dataA)throws DataException;
    
    public boolean isUtenteAbilitatoView(Utente utente, Ufficio uff, int fascicoloId) throws DataException;
    
    public boolean isParent(int fascicoloId) throws DataException;
    
    public boolean isTitolarioChanged(int fascicoloId,int titolarioId) throws DataException;
    
    public void aggiornaTitolarioInSottofascicoli(Connection connection,
            int fascicoloId,int titolarioId) throws DataException;
   
	public Map<String,FascicoloView> getFascicoliAlert(int referenteId) throws DataException;
	
	public int contaFascicoliAlert(int referenteId) throws DataException;

	public int getMaxProgressivo(Connection conn,int titolarioId,int parentId) throws DataException;

	public void cancellaCollegamento(Connection connection, int id,int collegamentoId)throws DataException;
	
	boolean isUfficioInProcedimentoAssegnatari(Integer fascicoloId,int ufficioId)throws DataException;

	public Map<Integer, FascicoloView> getFascicoliReferentePerCruscotti(Integer caricaId) throws DataException;
	
	public Map<Integer, FascicoloView> getFascicoliIstruttorePerCruscotti(Integer caricaId) throws DataException;

	public TipoVisibilitaUfficioEnum getVisibilitaUfficioFascicolo(int ufficioId, int fascicoloId) throws DataException;
	
	public boolean isUfficioAbilitatoSuFascicoloProtocollo(int ufficioId, int fascicoloId, int protocolloId) throws DataException;

	public boolean isUfficioAbilitatoSuFascicoloDocumento(int ufficioId, int fascicoloId, int documentoIf) throws DataException;

	
	}
