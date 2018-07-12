package it.compit.fenice.mvc.integration;

import it.compit.fenice.mvc.presentation.helper.DocumentoAmmTrasparenteView;
import it.compit.fenice.mvc.vo.ammtrasparente.AmmTrasparenteVO;
import it.compit.fenice.mvc.vo.ammtrasparente.DocumentoAmmTrasparenteVO;
import it.finsiel.siged.exception.DataException;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Collection;

public interface AmmTrasparenteDAO {

	Collection<DocumentoAmmTrasparenteView> getDocumentiSezione(int sezId)throws DataException;

	Collection<DocumentoAmmTrasparenteView> getDocumentiDaSezionale(int sezId)throws DataException;
	
	DocumentoAmmTrasparenteVO updateDocumentoSezione(Connection connection, DocumentoAmmTrasparenteVO vo)throws DataException;

	DocumentoAmmTrasparenteVO newDocumentoSezione(Connection connection, DocumentoAmmTrasparenteVO vo)throws DataException;

	void deleteAllegati(Connection connection, int sezId)throws DataException;

	void salvaAllegati(Connection connection, int sezId, int dcId,boolean riservato,int tipo, boolean principale, boolean pubblicabile)throws DataException;

	Collection<AmmTrasparenteVO> getSezioni(int aooId)throws DataException;
	
	Collection<AmmTrasparenteVO> getSezioniByFlagWeb(int aooId, int flagWeb)throws DataException;

	int contaSezioniByFlagWeb(int aooId, int flagWeb)throws DataException;

	AmmTrasparenteVO getSezione(int sezId)throws DataException;
	
	boolean getSezioneFlagWeb(int sezId)throws DataException;

	AmmTrasparenteVO newSezione(Connection connection, AmmTrasparenteVO repVO)throws DataException;
	
	AmmTrasparenteVO updateSezione(Connection connection, AmmTrasparenteVO repVO)throws DataException;

	DocumentoAmmTrasparenteVO getDocumentoSezione(int docId)throws DataException;

	InputStream getDocumentData(int docId)throws DataException;

	boolean isNumeroDocumentoSezioneUsed(DocumentoAmmTrasparenteVO vo)throws DataException;
	
	Collection<AmmTrasparenteVO> getSezioniByUfficio(int ufficioId)throws DataException;
	
	void archiviaDocumentoSezione(Connection connection, int docSezioneId) throws DataException;
	
	Collection<Integer> getDocumentiSezioneScaduti(Connection connection)throws DataException;

	int getMaxNumeroSezione(int repId, int annoCorrente)throws DataException;

	boolean aggiornaStato(int docSezioneId, int i,Connection connection)throws DataException;
	
}
