package it.compit.fenice.mvc.integration;

import it.compit.fenice.mvc.presentation.helper.DocumentoRepertorioView;
import it.compit.fenice.mvc.vo.repertori.DocumentoRepertorioVO;
import it.compit.fenice.mvc.vo.repertori.RepertorioVO;
import it.finsiel.siged.exception.DataException;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Collection;

public interface RepertorioDAO {

	Collection<DocumentoRepertorioView> getDocumentiRepertorio(int repertorioId)throws DataException;

	Collection<DocumentoRepertorioView> getDocumentiDaRepertoriale(int repertorioId)throws DataException;
	
	DocumentoRepertorioVO updateDocumentoRepertorio(Connection connection, DocumentoRepertorioVO vo)throws DataException;

	DocumentoRepertorioVO newDocumentoRepertorio(Connection connection, DocumentoRepertorioVO vo)throws DataException;

	void deleteAllegati(Connection connection, int repertorioId)throws DataException;

	void salvaAllegati(Connection connection, int repertorioId, int dcId,boolean riservato,int tipo, boolean principale, boolean pubblicabile)throws DataException;

	Collection<RepertorioVO> getRepertori(int aooId)throws DataException;
	
	Collection<RepertorioVO> getRepertoriByFlagWeb(int aooId, int flagWeb)throws DataException;

	int contaRepertoriByFlagWeb(int aooId, int flagWeb)throws DataException;

	RepertorioVO getRepertorio(int repertorioId)throws DataException;
	
	boolean getRepertorioFlagWeb(int repertorioId)throws DataException;

	RepertorioVO newRepertorio(Connection connection, RepertorioVO repVO)throws DataException;
	
	RepertorioVO updateRepertorio(Connection connection, RepertorioVO repVO)throws DataException;

	DocumentoRepertorioVO getDocumentoRepertorio(int docId)throws DataException;

	InputStream getDocumentData(int docId)throws DataException;

	boolean isNumeroDocumentoRepertorioUsed(DocumentoRepertorioVO vo)throws DataException;
	
	Collection<RepertorioVO> getRepertoriByUfficio(int ufficioId)throws DataException;
	
	void archiviaDocumentoRepertorio(Connection connection, int docRepertorioId) throws DataException;
	
	Collection<Integer> getDocumentiRepertoriScaduti(Connection connection)throws DataException;

	int getMaxNumeroRepertorio(int repId, int annoCorrente)throws DataException;

	boolean aggiornaStato(int docRepertorioId, int i,Connection connection)throws DataException;
	
}
