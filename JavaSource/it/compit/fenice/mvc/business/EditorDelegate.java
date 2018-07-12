package it.compit.fenice.mvc.business;

import it.compit.fenice.mvc.integration.EditorDAO;
import it.compit.fenice.mvc.presentation.helper.DocumentoAvvocatoGeneraleULLView;
import it.compit.fenice.mvc.presentation.helper.EditorView;
import it.compit.fenice.mvc.vo.documentale.EditorVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.NomiTabelle;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.InvioClassificati;
import it.finsiel.siged.model.documentale.Documento;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.DocumentaleDelegate;
import it.finsiel.siged.mvc.business.FascicoloDelegate;
import it.finsiel.siged.mvc.business.IdentificativiDelegate;
import it.finsiel.siged.mvc.business.ProcedimentoDelegate;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.InvioClassificatiDestinatariVO;
import it.finsiel.siged.mvc.vo.protocollo.InvioClassificatiVO;
import it.finsiel.siged.rdb.JDBCManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

public final class EditorDelegate {

	private static Logger logger = Logger.getLogger(EditorDelegate.class
			.getName());

	private EditorDAO editorDAO = null;

	private static EditorDelegate delegate = null;

	private EditorDelegate() {
		try {
			if (editorDAO == null) {
				editorDAO = (EditorDAO) DAOFactory
						.getDAO(Constants.EDITOR_DAO_CLASS);
				logger.debug("EditorDAO instantiated:"
						+ Constants.EDITOR_DAO_CLASS);
			}
		} catch (Exception e) {
			logger.error("Exception while connecting to EditorDAOjdbc!!", e);
		}

	}

	public static EditorDelegate getInstance() {
		if (delegate == null)
			delegate = new EditorDelegate();
		return delegate;
	}

	public static String getIdentifier() {
		return Constants.EDITOR_DELEGATE;
	}

	public Collection<EditorView> getDocumentiByCarica(int caricaId) {
		Collection<EditorView> doc = null;
		try {
			doc = editorDAO.getDocumentiByCarica(caricaId, 0);
			logger.info("getting repertori");
		} catch (Exception de) {
			logger.error("EditorDelegate failed getDocumentiByCarica", de);
		}
		return doc;
	}

	public Collection<EditorView> getDocumentiTemplateByCarica(int caricaId) {
		Collection<EditorView> doc = null;
		try {
			doc = editorDAO.getDocumentiTemplateByCarica(caricaId, 1);
			logger.info("getting repertori");
		} catch (Exception de) {
			logger.error("EditorDelegate failed getDocumentiTemplateByCarica", de);
		}
		return doc;
	}

	public Map<Integer, DocumentoAvvocatoGeneraleULLView> getDocumentiAvvocatoGeneraleULL(int caricaId,Integer statoProcedimento) {
		Map<Integer, DocumentoAvvocatoGeneraleULLView> doc = new HashMap<Integer, DocumentoAvvocatoGeneraleULLView>();
		try {
			doc = editorDAO.getDocumentiAvvocatoGeneraleULL(caricaId,statoProcedimento);
			logger.info("getting repertori");
		} catch (Exception de) {
			logger.error("EditorDelegate failed getDocumentiAvvocatoGeneraleULL", de);
		}
		return doc;
	}
	
	public int contaDocumentiTemplateByCarica(int caricaId) {
		int count = 0;
		try {
			count = editorDAO.contaDocumentiTemplateByCarica(caricaId, 1);
			logger.info("getting repertori");
		} catch (Exception de) {
			logger.error("EditorDelegate failed contaDocumentiTemplateByCarica", de);
		}
		return count;
	}

	public int contaDocumentiAvvocatoGeneraleULL(int caricaId,Integer statoProcedimento) {
		int count = 0;
		try {
			count = editorDAO.contaDocumentiAvvocatoGeneraleULL(caricaId,statoProcedimento);
			logger.info("getting repertori");
		} catch (Exception de) {
			logger.error("EditorDelegate failed contaDocumentiAvvocatoGeneraleULL", de);
		}
		return count;
	}
	
	public int salvaDocumentoEditor(EditorVO eVO) throws SQLException {
		int retVal = ReturnValues.UNKNOWN;
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			if (eVO.getDocumentoId() != 0) {
				editorDAO.archiviaDocumento(connection, eVO.getDocumentoId());
				retVal = editorDAO.aggiornaDocumento(connection, eVO);
			} else {
				eVO.setDocumentoId(IdentificativiDelegate.getInstance()
						.getNextId(connection, NomiTabelle.DOCUMENTI_EDITOR));
				retVal = editorDAO.salvaDocumento(connection, eVO);
			}
			connection.commit();
			logger.info("salvaDocumentoEditor");
		} catch (Exception de) {
			connection.rollback();
			logger.error("EditorDelegate failed salvaDocumentoEditor", de);
		} finally {
			jdbcMan.close(connection);
		}
		return retVal;

	}

	public EditorVO salvaDocumentoEditorTemplate(Connection connection,
			EditorVO eVO) throws SQLException {
		EditorVO docSalvato = new EditorVO();
		docSalvato.setReturnValue(ReturnValues.UNKNOWN);
		try {
			if (connection == null) {
				logger.warn("salvaDocumentoEditorTemplate() - Invalid Connection :"
						+ connection);
				throw new DataException(
						"Connessione alla base dati non valida.");
			}
			connection.setAutoCommit(false);
			if (eVO.getDocumentoId() != 0) {
				editorDAO.archiviaDocumentoTemplate(connection, eVO
						.getDocumentoId());
				editorDAO.aggiornaDocumento(connection, eVO);
			} else {
				eVO.setDocumentoId(IdentificativiDelegate.getInstance()
						.getNextId(connection, NomiTabelle.DOCUMENTI_EDITOR));
				editorDAO.salvaDocumento(connection, eVO);
			}
			docSalvato = editorDAO.getDocumento(connection, eVO
					.getDocumentoId());
			if (docSalvato.getReturnValue() == ReturnValues.FOUND) {
				editorDAO.eliminaAssegnatari(connection, eVO.getDocumentoId());
				salvaAssegnatari(connection, eVO.getDocumentoId(), eVO
						.getAssegnatari(), docSalvato.getVersione());
				editorDAO.eliminaAllacci(connection, eVO.getDocumentoId());
				salvaAllacci(connection, eVO.getDocumentoId(),
						eVO.getAllacci(), docSalvato.getVersione());
				editorDAO.eliminaFascicoli(connection, eVO.getDocumentoId());
				salvaFascicoli(connection, eVO.getDocumentoId(), eVO
						.getFascicoli(), docSalvato);
			}
			docSalvato.setReturnValue(ReturnValues.SAVED);
			connection.commit();
			logger.info("salvaDocumentoEditor");
		} catch (Exception de) {
			connection.rollback();
			logger.error("EditorDelegate failed salvaDocumentoEditor", de);
			de.printStackTrace();
		}
		return docSalvato;

	}

	private void salvaAssegnatari(Connection connection, int docId,
			Collection<AssegnatarioVO> assegnatari, int versione) throws Exception {
		if (assegnatari != null) {
			for (Iterator<AssegnatarioVO> i = assegnatari.iterator(); i.hasNext();) {
				AssegnatarioVO assegnatario =  i.next();
				
				assegnatario.setId(IdentificativiDelegate.getInstance()
						.getNextId(connection, NomiTabelle.EDITOR_ASSEGNATARI));
				editorDAO.salvaAssegnatario(connection, docId, assegnatario,
						versione);
			}
		}
	}

	private void salvaAllacci(Connection connection, int docId,
			Collection<AllaccioVO> allacci, int versione) throws Exception {
		if (allacci != null) {
			for (Iterator<AllaccioVO> i = allacci.iterator(); i.hasNext();) {
				AllaccioVO allaccio = (AllaccioVO) i.next();
				editorDAO.salvaAllaccio(connection, docId, allaccio, versione);
			}
		}
	}

	private void salvaFascicoli(Connection connection, int docId,
			Collection<FascicoloVO> fascicoli, EditorVO ed) throws Exception {
		if (fascicoli != null) {
			for (Object i : fascicoli) {
				FascicoloVO fVO = (FascicoloVO) i;
				fVO.setRowCreatedUser(ed.getRowCreatedUser());
				editorDAO.salvaFascicolo(connection, docId, fVO, ed
						.getVersione());
			}
		}
	}

	public boolean eliminaDocumentoTemplate(int docId) throws Exception {
		boolean deleted = false;
		try {
			deleted = editorDAO.eliminaDocumentoTemplate(docId);
		} catch (Exception de) {
			logger.error("EditorDelegate failed eliminadocumentoTemplate", de);
		}
		return deleted;
	}

	public int aggiornaStato(Connection connection, int documentoId, int stato)
			throws Exception {
		int statusFlag = ReturnValues.UNKNOWN;
		try {
			if (documentoId > 0) {
				editorDAO.archiviaDocumento(connection, documentoId);
				statusFlag = editorDAO.aggiornaStato(connection, documentoId,
						stato);
			} else {
				statusFlag = ReturnValues.SAVED;
			}
		} catch (Exception e) {
			logger.error("RepertorioDelegate failed aggiornaStato");
		}
		return statusFlag;
	}

	public int aggiornaStatoByFlagTipo(Connection connection, int documentoId,
			int stato) throws Exception {
		int statusFlag = ReturnValues.UNKNOWN;
		try {
			if (documentoId > 0) {
				int flag = editorDAO.getFlagTipoById(connection, documentoId);
				if (flag == 1) {
					editorDAO.eliminaDocumentoTemplate(connection,documentoId);
				} else {
					editorDAO.archiviaDocumento(connection, documentoId);
					statusFlag = editorDAO.aggiornaStato(connection,
							documentoId, stato);
				}
			} else {
				statusFlag = ReturnValues.SAVED;
			}
		} catch (Exception e) {
			logger.error("RepertorioDelegate failed aggiornaStato");
		}
		return statusFlag;
	}

	public boolean setElencoDecretiLavorati(int documentoId,int procedimentoId,
			int stato,Utente utente) throws SQLException {
		boolean status = false;
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			aggiornaStatoByFlagTipo(connection, documentoId, stato);
			ProcedimentoDelegate.getInstance().cambiaStatoProcedimento(connection, procedimentoId, 7, null, utente);

			//ProcedimentoDelegate.getInstance().inviaProcedimento(connection, procedimentoId, null, utente);
			//ProcedimentoDelegate.getInstance().
			logger.info("setElencoDecretiLavorati");
			connection.commit();
			status=true;
		} catch (Exception de) {
			connection.rollback();
			logger.error("EditorDelegate failed setElencoDecretiLavorati", de);
		} finally {
			jdbcMan.close(connection);
		}
		return status;

	}

	
	public boolean aggiornaStatoByFlagTipo(int documentoId,int stato) throws SQLException {
		boolean status = false;
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			aggiornaStatoByFlagTipo(connection, documentoId, stato);
			logger.info("EditorDelegate: aggiornaStatoByFlagTipo");
			connection.commit();
			status=true;
		} catch (Exception de) {
			connection.rollback();
			logger.error("EditorDelegate failed aggiornaStatoByFlagTipo", de);
		} finally {
			jdbcMan.close(connection);
		}
		return status;

	}
	
	public Collection<AssegnatarioVO> getAssegnatari(int id) {
		try {
			return editorDAO.getAssegnatari(id);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getAssegnatari: ");
			return null;
		}
	}

	public Collection<AllaccioVO> getAllacci(int id) {
		try {
			return editorDAO.getAllacci(id);
		} catch (DataException de) {
			logger.error("ProtocolloDelegate: failed getting getAllacci: ");
			return null;
		}
	}

	public Collection<FascicoloVO> getFascicoli(int id) {
		try {
			return editorDAO.getFascicoli(id);
		} catch (Exception de) {
			logger
					.error("FascicoloDelegate: failed getting getFascicoliByProtocolloId: ");
			return null;
		}
	}

	public EditorVO getDocumentoTemplate(int id) {
		EditorVO doc = new EditorVO();
		doc.setReturnValue(ReturnValues.UNKNOWN);
	
		try {
			logger.info("getting documento editor");
			doc = editorDAO.getDocumento(id);
			doc.setAssegnatari(getAssegnatari(id));
			doc.setAllacci(getAllacci(id));
			doc.setFascicoli(getFascicoli(id));
		} catch (Exception de) {
			logger.error("RepertorioDelegate failed getDocumento");
		}
		return doc;
	}

	public EditorVO getDocumento(int docId) {
		EditorVO doc = new EditorVO();
		doc.setReturnValue(ReturnValues.UNKNOWN);
		try {
			logger.info("getting documento editor");
			doc = editorDAO.getDocumento(docId);
		} catch (Exception de) {
			logger.error("RepertorioDelegate failed getDocumento");
		}
		return doc;
	}

	public int cancellaDocumento(int docId) {
		int val = ReturnValues.UNKNOWN;
		try {
			logger.info("getting documento editor");
			val = editorDAO.cancellaDocumento(docId);
		} catch (Exception de) {
			logger.error("RepertorioDelegate failed getDocumento");
		}
		return val;
	}

	public int invioClassificati(InvioClassificati invioClassificati,
			Documento doc, Utente utente, int edId) {
		JDBCManager jdbcMan = null;
		Connection connection = null;
		int statusFlag = 0;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			DocumentaleDelegate dd = DocumentaleDelegate.getInstance();
			int cartellaId = dd.getCartellaVOByCaricaId(
					doc.getFileVO().getCaricaLavId()).getId();
			doc.getFileVO().setCartellaId(cartellaId);
			doc = dd.salvaDocumento(connection, doc, utente);
			InvioClassificatiVO icVO = invioClassificati.getIcVO();
			icVO.setDocumentoId(doc.getFileVO().getId());
			icVO.setId(IdentificativiDelegate.getInstance().getNextId(
					connection, NomiTabelle.INVIO_CLASSIFICATI));
			editorDAO.salvaInvioClassificati(connection, icVO);
			for (Iterator<DestinatarioVO> y = invioClassificati.getDestinatariCollection()
					.iterator(); y.hasNext();) {
				InvioClassificatiDestinatariVO icdVO = new InvioClassificatiDestinatariVO();
				DestinatarioVO destinatario = y.next();
				icdVO.setDocumentoId(icVO.getDocumentoId());
				icdVO
						.setId(IdentificativiDelegate.getInstance().getNextId(
								connection,
								NomiTabelle.INVIO_CLASSIFICATI_DESTINATARI));
				icdVO.setDestinatario(destinatario);
				editorDAO.salvaDestinatariInvioClassificati(connection, icdVO);
			}
			for (Iterator<FascicoloVO> x = invioClassificati.getFascicoliCollection()
					.iterator(); x.hasNext();) {
				FascicoloVO fascicolo =x.next();
				if (fascicolo.getId() == 0)
					fascicolo = FascicoloDelegate.getInstance().nuovoFascicolo(
							connection, fascicolo);
				int id = IdentificativiDelegate.getInstance().getNextId(
						connection, NomiTabelle.INVIO_CLASSIFICATI_FASCICOLI);
				editorDAO.salvaFascicoloInvioClassificati(connection, id, icVO
						.getDocumentoId(), fascicolo.getId());
			}
			statusFlag = aggiornaStato(connection, edId, 2);
			connection.commit();
		} catch (Exception de) {
			de.printStackTrace();
			jdbcMan.rollback(connection);
			logger.error("EditorDelegate: failed invioClassificati: ", de);
		} finally {
			jdbcMan.close(connection);
		}
		return statusFlag;
	}

	public Collection<EditorVO> getStoriaDocumento(int documentoId) {
		try {
			return editorDAO.getStoriaDocumento(documentoId);
		} catch (Exception de) {
			logger
					.error("FascicoloDelegate: failed getting getStoriaFascicolo: ");
			return null;
		}
	}

	public EditorVO getDocumentoByIdVersione(int documentoId, int versione) {
		EditorVO doc = new EditorVO();
		doc.setReturnValue(ReturnValues.UNKNOWN);
		try {
			logger.info(" getDocumentoByIdVersione editor");
			doc = editorDAO.getDocumentoByIdVersione(documentoId, versione);
		} catch (Exception de) {
			logger.error("RepertorioDelegate failed getDocumentoByIdVersione");
		}
		return doc;
	}

	public int inviaPerFirma(EditorVO eVO, int dirigente) {
		int ret = ReturnValues.UNKNOWN;
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			editorDAO.archiviaDocumentoTemplate(connection, eVO
					.getDocumentoId());
			ret = editorDAO.inviaPerFirma(connection, eVO, dirigente);
			connection.commit();
		} catch (Exception de) {
			de.printStackTrace();
			logger.error("RepertorioDelegate failed inviaPerFirma");
		} finally {
			jdbcMan.close(connection);
		}
		return ret;
	}

	public EditorVO salvaDocumentoEditorTemplate(EditorVO eVO) {
		EditorVO salvato = new EditorVO();
		salvato.setReturnValue(ReturnValues.UNKNOWN);
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			salvato = salvaDocumentoEditorTemplate(connection, eVO);
		} catch (Exception de) {
			de.printStackTrace();
			logger.error("RepertorioDelegate failed salvaDocumentoEditorTemplate");
		} finally {
			jdbcMan.close(connection);
		}
		return salvato;
	}

	public boolean rifiutaDocumentoTemplate(EditorVO eVO) {
		boolean ret = false;
		JDBCManager jdbcMan = null;
		Connection connection = null;
		try {
			jdbcMan = new JDBCManager();
			connection = jdbcMan.getConnection();
			connection.setAutoCommit(false);
			salvaDocumentoEditorTemplate(connection, eVO);
			ret = editorDAO.rifiutaDocumentoTemplate(connection, eVO);
			connection.commit();
		} catch (Exception de) {
			de.printStackTrace();
			logger.error("RepertorioDelegate failed ritornaDocumentoTemplate");
		} finally {
			jdbcMan.close(connection);
		}
		return ret;
	}

	
}
