package it.finsiel.siged.mvc.integration;

import it.compit.fenice.model.protocollo.PostaInterna;
import it.compit.fenice.mvc.presentation.helper.ProtocolloProcedimentoView;
import it.compit.fenice.mvc.presentation.helper.ReportCheckPostaInternaView;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.ProtocolloIngresso;
import it.finsiel.siged.mvc.presentation.helper.AllaccioView;
import it.finsiel.siged.mvc.presentation.helper.MittenteView;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.mvc.vo.protocollo.SegnaturaVO;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;

import java.sql.Connection;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;

/*
 * @author G.Calli.
 */

public interface ProtocolloDAO {

	public ProtocolloVO getProtocolloById(int id) throws DataException;

	ProtocolloVO getProtocolloByNumero(Connection connection, int anno,
			int registro, int numProtocollo) throws DataException;

	public ProtocolloVO getProtocolloById(Connection connection, int id)
			throws DataException;

	public RegistroVO getRegistroByProtocolloId(int protocolloId)
			throws DataException;

	public ReportProtocolloView getProtocolloView(Connection connection, int id)
			throws DataException;

	public ReportProtocolloView getProtocolloView(int id) throws DataException;

	public String getTipoProtocollo(int id) throws DataException;

	public int getUltimoProtocollo(int anno, int registro) throws DataException;

	public int getMaxNumProtocollo(Connection connection, int anno,
			int registro, int numeroProtocollo) throws DataException;

	public Map<Long, ReportProtocolloView> getPostaInternaAssegnata(
			Utente utente, String tipo) throws DataException;
	
	public Map<Long, ReportProtocolloView> getPostaInternaRepertorio(Utente utente) throws DataException;

	public Map<Integer, ReportCheckPostaInternaView> getCheckPostaInternaView(
			int caricaId, int flagNotifica) throws DataException;

	public Map<Long, ReportProtocolloView> getFatture(Utente utente,
			int registro, String tipo) throws DataException;

	public Map<Long, ReportProtocolloView> getPostaInternaAssegnataPerNumero(
			Utente utente, int numero, String tipo) throws DataException;

	public ProtocolloVO newProtocollo(Connection conn, ProtocolloVO protocollo)
			throws DataException;

	public ProtocolloVO newStoriaProtocollo(Connection conn,
			ProtocolloVO protocollo) throws DataException;

	public ProtocolloVO aggiornaProtocollo(Connection connection,
			ProtocolloVO protocollo) throws DataException;

	public Map<String, DocumentoVO> getAllegatiProtocollo(int protocolloId)
			throws DataException;

	public String getDocId(int documentoId) throws DataException;

	public Collection<AllaccioVO> getAllacciProtocollo(int protocolloId)
			throws DataException;

	public Collection<AllaccioVO> getAllacciProtocollo(Connection connection,
			int protocolloId) throws DataException;

	public AssegnatarioVO getAssegnatarioPerCompetenza(int protocolloId)
			throws DataException;

	public AssegnatarioVO getAssegnatarioPerCompetenza(Connection connection,
			int protocolloId) throws DataException;

	public Collection<AssegnatarioVO> getAssegnatariProtocollo(int protocolloId)
			throws DataException;

	public Collection<AllaccioView> getProtocolliAllacciabili(Utente utente,
			int annoProtocolo, int numeroProtocolloDa, int numeroProtocolloA,
			int protocolloId) throws DataException;

	public AllaccioVO getProtocolloAllacciabile(Utente utente,
			int annoProtocolo, int numeroProtocolloDa, int numeroProtocolloA)
			throws DataException;

	public void aggiornaDocumentoPrincipaleId(Connection connection,
			int protocolloId, int documentoId) throws DataException;

	public void eliminaDocumentoPrincipale(Connection connection,
			int protocolloId) throws DataException;

	public void salvaAllegato(Connection connection,
			int protocollo_allegati_id, int protocolloId, int documentoId,
			int versione) throws DataException;

	public void salvaAllaccio(Connection connection, AllaccioVO allaccio)
			throws DataException;

	public void salvaAssegnatario(Connection connection,
			AssegnatarioVO assegnatario, int versione) throws DataException;

	public void salvaCheckPresaVisione(Connection connection,
			AssegnatarioVO assegnatario) throws DataException;

	public void eliminaAllacciProtocollo(Connection connection, int protocolloId)
			throws DataException;

	public void eliminaAllegatiProtocollo(Connection connection,
			int protocolloId) throws DataException;

	public void eliminaAllaccioProtocollo(Connection connection,
			int protocolloId, int protocolloAllacciatoId) throws DataException;

	public boolean esisteAllaccio(Connection connection, int protocolloId,
			int protocolloAllacciatoId) throws DataException;

	public void eliminaAssegnatariProtocollo(Connection connection,
			int protocolloId) throws DataException;

	public ProtocolloVO getProtocolloByNumero(int anno, int registro,
			int numProtocollo) throws DataException;

	public Map<Long, ReportProtocolloView> getProtocolliAssegnati(
			Utente utente, int annoProtocolloDa, int annoProtocolloA,
			int numeroProtocollo, String tipoUtenteUfficio)
			throws DataException;

	public int presaIncarico(Connection connection, ProtocolloVO protocolloVO,
			String tipoAzione, Utente utente) throws DataException;

	public int updateScarico(Connection connection, ProtocolloVO protocolloVO,
			String flagScarico, Utente utente, boolean titolareProcedimento)
			throws DataException;

	public SortedMap<Long, ReportProtocolloView> cercaProtocolli(Utente utente,
			Ufficio ufficio, LinkedHashMap<String, Object> sqlDB, boolean isTabula)
			throws DataException;

	public Collection<MittenteView> getMittenti(String mittente)
			throws DataException;

	public Collection<DestinatarioVO> getDestinatari(String destinatario)
			throws DataException;

	public Map<String, DestinatarioVO> getDestinatariProtocollo(int protocolloId)
			throws DataException;

	public int annullaProtocollo(Connection connection,
			ProtocolloVO protocolloVO, Utente utente) throws DataException;

	public void updateProgressivoNotifica(Connection connection, String progressivo, int anno, int aooId) throws DataException;

	public void newProgressivoNotifica(Connection connection, int id, String progressivo, int anno, int aooId) throws DataException;

	public String getProgressivoNotifica(int aooId, int anno) throws DataException;

	public int salvaSegnatura(Connection connection, SegnaturaVO segnaturaVO)
			throws DataException;

	public void eliminaDestinatariProtocollo(Connection connection,
			int protocolloId) throws DataException;

	public void salvaDestinatario(Connection connection,
			DestinatarioVO destinatario, int versione) throws DataException;

	public Collection<ReportProtocolloView> getProtocolliByProtMittente(
			Utente utente, String protMittente) throws DataException;

	public Map<Long, ReportProtocolloView> getProtocolliRespinti(Utente utente,
			int annoProtocolloDa, int annoProtocolloA, int numeroProtocolloDa,
			int numeroProtocolloA, java.util.Date dataDa, java.util.Date dataA)
			throws DataException;

	public int riassegnaProtocollo(ProtocolloIngresso protocollo, Utente utente)
			throws DataException;

	public int getDocumentoDefault(int aooId) throws DataException;

	public Collection<ProtocolloVO> getProtocolliToExport(Connection connLocal,
			int registroId) throws DataException;

	public void updateRegistroEmergenza(Connection connection)
			throws DataException;

	public void updateMsgAssegnatarioCompetenteByIdProtocollo(
			Connection connection, String msgAssegnatarioCompetente,
			int protocolloId) throws DataException;

	public Collection<ProtocolloProcedimentoVO> getProcedimentiProtocollo(
			int protocolloId) throws DataException;

	public boolean isUtenteAbilitatoView(Utente utente, Ufficio uff,
			int protocolloId) throws DataException;

	public boolean updateCheckPostaInterna(int checkId) throws DataException;

	public boolean updateCheckPostaInternaUtente(int caricaId)
			throws DataException;

	public int riassegnaPosta(PostaInterna postaInterna, Utente utente)
			throws DataException;

	public Map<Long, ReportProtocolloView> getProtocolliPerConoscenza(
			Utente utente, String tipo) throws DataException;

	public Map<Integer, ReportProtocolloView> getProtocolliAssegnatiPerCruscotti(
			int id, int annoProtocolloDa, int annoProtocolloA, String tipo)
			throws DataException;

	public void eliminaMittentiProtocollo(Connection connection,
			int protocolloId) throws DataException;

	public void updateMsgAssegnatario(Connection connection,
			String msgDestinatarioCompetente, int intValue, Utente utente)
			throws DataException;

	public void aggiornaAssegnanteId(Connection connection,
			ProtocolloVO protocolloVO) throws DataException;

	public Map<String, ReportProtocolloView> getProtocolliAlert(Utente utente)
			throws DataException;

	public Map<String, ProtocolloProcedimentoView> getProtocolliEvidenza(
			Utente utente) throws DataException;

	public int getProtocolloIdByAooNumeroAnno(int anno, int aooId,
			int numProtocollo) throws DataException;

	public SegnaturaVO getSegnatura(int id) throws DataException;

	public void registraLavoratoProtocolloIngresso(AssegnatarioVO assegnatario,
			String username) throws DataException;

	public AssegnatarioVO getAssegnatarioProtocollo(int protocolloId,
			int ufficioId, int caricaId) throws DataException;

	public boolean isUfficioAssegnatario(int protocolloId, int ufficioInUso)
			throws DataException;

}