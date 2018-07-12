package it.finsiel.siged.mvc.integration;

import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;

/*
 * @author G.Calli.
 */

public interface DocumentoDAO {

    public DocumentoVO getDocumento(int id) throws DataException;
    
    public DocumentoVO getDocumentoTabula(int protocolloId) throws DataException;
    
    public DocumentoVO getDocumento(Connection connection, int id)
            throws DataException;

    public DocumentoVO newDocumentoVO(Connection connection,
            DocumentoVO documento,String path) throws DataException;
    
    public DocumentoVO newDocumentoVO(Connection connection,
            DocumentoVO documento) throws DataException;

    public void writeDocumentToStream(int docId, OutputStream os)
            throws DataException;
    
    public InputStream writeDocumentToInputStream(int docId) throws DataException;
    
    public boolean isDataDocumentoNotNull(int docId)throws DataException;

	public DocumentoVO findDocumentoByInfo(String filename, String impronta, int fileSize) throws DataException;

}