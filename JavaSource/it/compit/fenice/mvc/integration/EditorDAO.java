package it.compit.fenice.mvc.integration;

import it.compit.fenice.mvc.presentation.helper.DocumentoAvvocatoGeneraleULLView;
import it.compit.fenice.mvc.presentation.helper.EditorView;
import it.compit.fenice.mvc.vo.documentale.EditorVO;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.InvioClassificatiDestinatariVO;
import it.finsiel.siged.mvc.vo.protocollo.InvioClassificatiVO;

import java.sql.Connection;
import java.util.Collection;
import java.util.Map;

public interface EditorDAO {

	void archiviaDocumento(Connection connection, int docId)
			throws DataException;

	void archiviaDocumentoTemplate(Connection connection, int docId)
	throws DataException;
	
	EditorVO getDocumentoByIdVersione(int id, int versione) throws DataException;
	
	Collection<EditorView> getDocumentiByCarica(int caricaId,int tipo) throws DataException;
	
	Collection<EditorView> getDocumentiTemplateByCarica(int caricaId,int tipo) throws DataException;
	
	int contaDocumentiTemplateByCarica(int caricaId,int tipo) throws DataException;
	
	Map<Integer, DocumentoAvvocatoGeneraleULLView> getDocumentiAvvocatoGeneraleULL(int caricaId,Integer statoProcedimento) throws DataException;
	
	int contaDocumentiAvvocatoGeneraleULL(int caricaId,Integer statoProcedimento) throws DataException;	
	
	Collection<EditorVO> getStoriaDocumento(int documentoId) throws DataException;

	int aggiornaDocumento(EditorVO eVO) throws DataException;

	int salvaDocumento(EditorVO eVO) throws DataException;

	int aggiornaDocumento(Connection conn, EditorVO eVO) throws DataException;

	int salvaDocumento(Connection conn, EditorVO eVO) throws DataException;

	EditorVO getDocumento(int id) throws DataException;
	
	EditorVO getDocumento(Connection conn,int id) throws DataException;

	int cancellaDocumento(int id) throws DataException;

	public void salvaInvioClassificati(Connection connection,
			InvioClassificatiVO icVO) throws DataException;

	public void salvaDestinatariInvioClassificati(Connection connection,
			InvioClassificatiDestinatariVO icdVO) throws DataException;
	
	public void salvaFascicoloInvioClassificati(Connection connection,
			int id,int documentoId,int fascicoloId) throws DataException;

	public int aggiornaStato(int docId,int stato) throws DataException;
	
	public int aggiornaStato(Connection connection, int docId,int stato)
      throws DataException;
	
	public int inviaPerFirma(EditorVO eVO,int dirigenteId) throws DataException;

	public void eliminaAssegnatari(Connection connection,
            int docId) throws DataException;
	
	public void eliminaAllacci(Connection connection,int docId) throws DataException;
	
	public void eliminaFascicoli(Connection connection,int docId) throws DataException;
	  
	public void salvaAssegnatario(Connection connection,int docId,
            AssegnatarioVO assegnatario, int versione) throws DataException;

	void salvaAllaccio(Connection connection, int docId, AllaccioVO allaccio,
			int versione)throws DataException;

	void salvaFascicolo(Connection connection, int docId, FascicoloVO fVO,
			int versione)throws DataException;

	Collection<AssegnatarioVO> getAssegnatari(int id)throws DataException;

	Collection<AllaccioVO> getAllacci(int id)throws DataException;

	Collection<FascicoloVO> getFascicoli(int id)throws DataException;
	
	public boolean eliminaDocumentoTemplate(Connection connection,int docId) throws DataException;
	
	public boolean eliminaDocumentoTemplate(int docId) throws DataException;

	int getFlagTipoById(Connection connection, int documentoId)throws DataException;

	int inviaPerFirma(Connection connection, EditorVO eVO, int dirigente)throws DataException;
	
	boolean rifiutaDocumentoTemplate(Connection connection, EditorVO eVO)throws DataException;


}
