package it.finsiel.siged.mvc.integration;

import it.finsiel.siged.exception.CannotDeleteException;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.presentation.helper.DocumentoView;
import it.finsiel.siged.mvc.vo.documentale.CartellaVO;
import it.finsiel.siged.mvc.vo.documentale.FileVO;
import it.finsiel.siged.mvc.vo.documentale.PermessoFileVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.InvioClassificatiDestinatariVO;
import it.finsiel.siged.mvc.vo.protocollo.InvioClassificatiVO;

import java.io.OutputStream;
import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;



public interface DocumentaleDAO {

    public Collection<CartellaVO> getSottoCartelle(int cartellaId) throws DataException;

    public Collection<CartellaVO> getSottoCartelle(Connection connection, int cartellaId)
            throws DataException;

    public CartellaVO getCartellaVOByCaricaNome(Connection connection,
            int caricaId, String nome, Integer id)
            throws DataException;
    
    public CartellaVO getCartellaVOByCaricaId(Connection connection,
    		int caricaId) throws DataException;
    
    public CartellaVO getCartellaVOByUfficioUtenteNome(Connection connection,
            int ufficioId, int utenteId, String nome, Integer id)
            throws DataException;

    public CartellaVO getCartellaVOByUfficioUtenteId(Connection connection,
            int ufficioId, int utenteId) throws DataException;

    public HashMap<Integer,FileVO> getFileCondivisi(String ufficiIds, int utenteId)
            throws DataException;

    public HashMap<Integer,FileVO> getFileCondivisiC(String ufficiIds, int utenteId)
    throws DataException;
    
    public CartellaVO newCartellaVO(CartellaVO cartella) throws DataException;

    public CartellaVO newCartellaVO(Connection connection, CartellaVO cartella)
            throws DataException;

    public void deleteFile(int dfaId, int dfrId) throws DataException;

    public boolean deleteFile(Connection connection, int dfaId, int dfrId)
            throws DataException;

    public CartellaVO updateNomeCartellaVO(Connection connection,
            CartellaVO cartella) throws DataException;

    public CartellaVO updateNomeCartellaVO(CartellaVO cartella)
            throws DataException;

    public CartellaVO getCartellaVO(int id) throws DataException;

    public CartellaVO getCartellaVO(Connection connection, int id)
            throws DataException;

    public DocumentoVO newDocumentoVO(Connection connection,
            DocumentoVO documento) throws DataException;

    public DocumentoVO newDocumentoVO(DocumentoVO documento)
            throws DataException;

    public DocumentoVO getDocumento(Connection connection, int id)
            throws DataException;

    public DocumentoVO getDocumento(int id) throws DataException;

    public Collection<DocumentoView> getVersioniDocumento(int dfaId) throws DataException;

    public FileVO newFileVO(FileVO file) throws DataException;

    public FileVO newFileVO(Connection connection, FileVO file)
            throws DataException;

    public FileVO getStoriaFileVO(Connection connection, int id, int versione)
            throws DataException;

    public FileVO updateFileVO(Connection connection, FileVO file,
            boolean ripristino) throws DataException;

    public void cancellaPermessi(Connection conn, int dfaId)
            throws DataException;

    public FileVO getFileVO(int id) throws DataException;

  
    public FileVO getFileVO(Connection connection, int id) throws DataException;

  
    public void salvaPermessiFile(Connection connection,
            Collection<PermessoFileVO> permessiFile, int versione) throws DataException;

   
    public void cancellaAlberoUtentePerUfficio(Connection connection,
            int utenteId, int ufficioId, int aooId) throws DataException;

    public int deleteCartella(int cartellaId) throws DataException,
            CannotDeleteException;

    public HashMap<Integer,FileVO> getFiles(int cartellaId) throws DataException;
    
    public HashMap<Integer,FileVO> getFilesLista(int cartellaId) throws DataException;

    public void salvaPermesso(Connection connection, PermessoFileVO permesso,
            int versione) throws DataException;

    public Collection<PermessoFileVO> getPermessiDocumento(int documentoId)
            throws DataException;

    public Collection<PermessoFileVO> getPermessiDocumento(Connection connection,
            int documentoId) throws DataException;

    public Collection<PermessoFileVO> getStoriaPermessiDocumento(Connection connection,
            int documentoId, int versione) throws DataException;

    public void writeFileToStream(int repFileId, OutputStream os)
            throws DataException;

    public SortedMap<Integer,DocumentoView> cercaDocumenti(Utente utente, HashMap<String,String> sqlDB, String testo)
            throws DataException;
    
    public int contaDocumenti(Utente utente, HashMap<String,String> sqlDB, String testo)
    throws DataException;

    public boolean hasAccessToFolder(int cartellaId, int caricaId)
            throws DataException;

    public boolean isOwnerDocumento(int documentoId, int utenteId)
            throws DataException;

    public int getTipoPermessoSuDocumento(int documentoId, int utenteId,
            String ufficiIds) throws DataException;

    public int classificaDocumento(Connection connection, int dfrId,
            int titolarioId) throws DataException;

    public int classificaDocumento(int dfrId, int titolarioId)
            throws DataException;

    public boolean spostaInLavorazione(Connection connection, int docId)
            throws DataException;

    public boolean spostaDocumento(Connection connection,
            int cartellaDestinazioneId, int docId) throws DataException;

    public boolean spostaDocumento(int cartellaDestinazioneId, int docId)
            throws DataException;

    public boolean spostaInLavorazione(int docId) throws DataException;

    public Map<String,DestinatarioVO> getDestinatariDocumentiInvio(Connection connection,
            int documentoId) throws DataException;

    public Map<String,DestinatarioVO> getDestinatariDocumentiInvio(int documentoId)
            throws DataException;

    public Collection<Integer> getFascicoliDocumentoInvio(int documentoId)
    		throws DataException;
    
    public Collection<DocumentoView> getDocumentiArchivioInvio(int aooId) throws DataException;

    public InvioClassificatiVO getInvioClassificatiVO(int documentoId)
            throws DataException;

    public int eliminaCodaInvioDocumento(Connection connection, int documentoId)
            throws DataException;

    public void archiviaVersione(Connection connection, int fileId)
            throws DataException;

    public int checkoutDocumento(Connection connection, int docId, int utenteId)
            throws DataException;

    public int checkoutDocumento(int docId, int utenteId) throws DataException;

    public boolean checkinDocumento(Connection connection, int docId)
            throws DataException;

    public boolean checkinDocumento(int docId) throws DataException;

    public void salvaInvioClassificati(Connection connection,
            InvioClassificatiVO icVO) throws DataException;

    public void salvaDestinatariInvioClassificati(Connection connection,
            InvioClassificatiDestinatariVO icdVO) throws DataException;

    public int invioDocumento(int docId) throws DataException;

    public int invioDocumento(Connection connection, int docId)
            throws DataException;

    public int getFileId(Connection connection, int dfaId) throws DataException;

	public boolean aggiornaStatoArchivio(int documentoId, String stato) throws DataException;;

}