/*
 * Created on 21-dic-2004
 *
 * 
 */
package it.finsiel.siged.mvc.integration;

import it.finsiel.siged.dao.fs.RepositoryException;
import it.finsiel.siged.mvc.plugin.DataProvider;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Almaviva sud
 * 
 */
public interface DocumentManagerDAO extends DataProvider {

    public Object startTransaction() throws IOException;

    public void commitTransaction(Object txId) throws IOException;

    public void rollbackTransaction(Object txId) throws IOException;

    public void saveDocumentsRepository(DocumentoVO[] documenti, Object txId)
            throws IOException;

    public InputStream getDocumentInputStreamRepository(Integer id)
            throws RepositoryException;

}
