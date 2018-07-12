package it.compit.fenice.cds.action;

import it.compit.fenice.cds.service.bean.JSFileProtoBean;
import it.compit.fenice.cds.service.bean.JSPersonaBean;
import it.compit.fenice.cds.service.bean.JSProtocollazioneBean;
import it.compit.fenice.mvc.business.CdsDelegate;
import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.exception.EmailException;
import it.finsiel.siged.model.documentale.Documento;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.Protocollo;
import it.finsiel.siged.model.protocollo.ProtocolloIngresso;
import it.finsiel.siged.model.protocollo.ProtocolloUscita;
import it.finsiel.siged.mvc.bo.DocumentaleBO;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.business.DocumentaleDelegate;
import it.finsiel.siged.mvc.business.FascicoloDelegate;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.business.TitolarioDelegate;
import it.finsiel.siged.mvc.vo.documentale.CartellaVO;
import it.finsiel.siged.mvc.vo.documentale.FileVO;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.FileUtil;
import it.flosslab.mvc.business.OggettarioDelegate;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CdsAction extends Action {

	static Logger logger = Logger.getLogger(CdsAction.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Utente user = CdsDelegate.getInstance().getUtenteCDS();
		String numProtocollo=null;
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			if (request.getParameter("protocollazioneInfo") != null) {
				logger.debug("CdsAction: Parametro ricevuto");
				JSProtocollazioneBean bean = mapper.readValue(
						request.getParameter("protocollazioneInfo"),
						JSProtocollazioneBean.class);
				if (bean.getTipoProtocollo().equals("I")) {
					logger.debug("CdsAction: preparaProtocolloIngresso");
					numProtocollo=preparaProtocolloIngresso(bean, user);
				} else if (bean.getTipoProtocollo().equals("U")) {
					logger.debug("CdsAction: preparaProtocolloUscita");
					numProtocollo=preparaProtocolloUscita(bean, user, user.getUfficioVOInUso());
				} else if (bean.getTipoProtocollo().equals("N")) {
					logger.debug("CdsAction: preparaDocumenti");
					FascicoloVO fasc = FascicoloDelegate.getInstance()
							.getFascicoloVOByCodice(
									String.valueOf(bean.getRichiestaId()));
					if (fasc.getReturnValue() == ReturnValues.FOUND) {
						numProtocollo=preparaDocumenti(bean, user, fasc);
					} else{
						logger.error("CdsAction: La richiesta "+ bean.getRichiestaId()+" non corrisponde a nessun fascicolo");
						response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"La richiesta "+ bean.getRichiestaId()+" non corrisponde a nessun fascicolo");
					}
				}

			} else{
				logger.error("Nessun parametro inviato");
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Nessun parametro inviato");
			}
			response.getWriter().write(numProtocollo);
			response.getWriter().flush();
			response.setStatus(HttpServletResponse.SC_OK);
			logger.debug("CdsAction: Operazione completata, numero di protocollo "+numProtocollo);
		} catch (Exception e) {
			logger.error("CdsAction: Si e' verificata un eccezione non gestita. ", e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Errore generico");
		}
		return null;
	}

	public String preparaProtocolloIngresso(JSProtocollazioneBean bean,
			Utente utente) {
		ProtocolloIngresso pi = ProtocolloBO.getDefaultProtocolloIngresso(utente);
		allegaDocumenti(pi, bean, utente);
		ProtocolloVO protocollo = pi.getProtocollo();
		protocollo.setDataDocumento(null);
		protocollo.setDataRicezione(null);
		protocollo.setOggetto(bean.getOggetto());
		protocollo.setNumProtocolloMittente(String.valueOf(bean
				.getRichiestaId()));
		protocollo.setDataProtocolloMittente(new Date());
		protocollo.setVersione(0);

		protocollo.setFlagTipoMittente("M");
		protocollo.setMittenti(newPersonafisicaList(bean.getPersona()));

		aggiornaAssegnatariModel(bean, pi, utente);
		aggiornaFascicoliModel(bean, pi, utente);

		pi.setProtocollo(protocollo);
		ProtocolloVO prot = ProtocolloDelegate.getInstance()
				.registraProtocolloIngressoCDS(pi, utente);
		pi.setProtocollo(prot);
		
		pi.getProtocollo().setFlagTipoMittente("M");
		pi.getProtocollo().setMittenti(newPersonafisicaList(bean.getPersona()));
		pi.getFascicoli().clear();
		
		prot = ProtocolloDelegate.getInstance().aggiornaProtocolloIngresso(pi,utente);
		return prot.getNumProtocollo() + "/" + prot.getAnnoRegistrazione();
	}

	public String preparaProtocolloUscita(JSProtocollazioneBean bean,
			Utente utente, UfficioVO ufficio) {
		ProtocolloUscita pu = ProtocolloBO.getDefaultProtocolloUscita(utente);
		allegaDocumenti(pu, bean, utente);
		ProtocolloVO protocollo = pu.getProtocollo();
		protocollo.setDataDocumento(null);
		protocollo.setDataRicezione(null);
		protocollo.setOggetto(bean.getOggetto());
		protocollo.setNumProtocolloMittente(String.valueOf(bean.getRichiestaId()));
		protocollo.setDataProtocolloMittente(new Date());
		protocollo.setVersione(0);
		protocollo.setUfficioMittenteId(utente.getUfficioInUso());
		protocollo.setUtenteMittenteId(0);
		protocollo.setDenominazioneMittente(ufficio.getDescription());
		protocollo.setFlagTipoMittente("G");
		
		aggiornaDestinatariModel(bean, pu, utente);
		aggiornaFascicoliModel(bean, pu, utente);
		
		pu.setProtocollo(protocollo);
		ProtocolloVO prot = ProtocolloDelegate.getInstance().registraProtocolloUscitaCDS(pu, utente);
		pu.setProtocollo(prot);
		
		aggiornaDestinatariModel(bean, pu, utente);
		pu.getFascicoli().clear();
		
		prot = ProtocolloDelegate.getInstance().aggiornaProtocolloUscita(pu,utente);
		return prot.getNumProtocollo() + "/" + prot.getAnnoRegistrazione();
	}

	public String preparaDocumenti(JSProtocollazioneBean bean, Utente utente,
			FascicoloVO fascicolo) throws EmailException {
		for (JSFileProtoBean fileBean : bean.getFileProtos()) {
			File file = new File(fileBean.getDoc());
			String contentType = new MimetypesFileTypeMap().getContentType(file);
			String impronta = FileUtil.calcolaDigest(file.getAbsolutePath(),FileConstants.SHA256);
			DocumentoVO doc = new DocumentoVO();
			doc.setContentType(contentType);
			doc.setImpronta(impronta);
			doc.setPath(file.getAbsolutePath());
			doc.setSize((int) file.length());
			doc.setFileName(file.getName());
			doc.setDescrizione("Documento appartentente alla richiesta "+ bean.getRichiestaId());
			doc.setRowCreatedTime(new Date(System.currentTimeMillis()));
			doc.setRowUpdatedTime(new Date(System.currentTimeMillis()));
			doc.setRowCreatedUser(utente.getValueObject().getUsername());
			doc.setRowUpdatedUser(utente.getValueObject().getUsername());
			Documento documento = DocumentaleBO.getDefaultDocumento(utente);
			aggiornaModelFromFascicolo(doc, documento, utente, fascicolo);
			documento = DocumentaleDelegate.getInstance().salvaDocumento(documento, utente);
		}
		return "Fascicolazione completata";
	}

	public static FascicoloVO nuovoFascicolo(JSProtocollazioneBean bean,
			Utente user) {
		FascicoloVO fVO = new FascicoloVO();
		try {
			fVO.setAooId(1);
			fVO.setRegistroId(1);
			fVO.setCodice(String.valueOf(bean.getRichiestaId()));
			fVO.setDataApertura(new Date());
			fVO.setDataChiusura(null);
			fVO.setDescrizione("Fascicolo appartentente alla richiesta "+ bean.getRichiestaId());
			fVO.setNome(String.valueOf(bean.getRichiestaId()));
			fVO.setOggetto(String.valueOf(bean.getRichiestaId()));
			fVO.setStato(0);
			
			// titolario
			fVO.setTitolarioId(TitolarioDelegate.getInstance().getTitolarioByUfficio(user.getUfficioInUso()).getId());
			
			// referente
			fVO.setUtenteIntestatarioId(user.getValueObject().getId());
			fVO.setCaricaIntestatarioId(user.getCaricaInUso());
			fVO.setGiorniAlert(0);
			fVO.setGiorniMax(0);
			
			// trattato da
			fVO.setUfficioResponsabileId(user.getUfficioInUso());
			fVO.setCaricaResponsabileId(user.getCaricaInUso());

			// ufficio
			fVO.setUfficioIntestatarioId(user.getUfficioInUso());
			fVO.setVersione(0);

			fVO.setRowCreatedUser(user.getValueObject().getUsername());
			fVO.setRowCreatedTime(new Date(System.currentTimeMillis()));
			fVO.setDataCarico(new Date(System.currentTimeMillis()));
			fVO.setRowUpdatedUser(user.getValueObject().getUsername());
			fVO.setRowUpdatedTime(new Date(System.currentTimeMillis()));
			fVO.setAnnoRiferimento(DateUtil.getAnnoCorrente());
			fVO.setTipoFascicolo(0);
			fVO.setDataUltimoMovimento(null);
			fVO.setDataScarto(null);
			fVO.setDataScarico(null);

			fVO.setCollocazioneLabel1("Foglio");
			fVO.setCollocazioneLabel2("Particelle");
			fVO.setCollocazioneLabel3("Indirizzo");
			fVO.setCollocazioneLabel4("Collocazione");

			fVO = FascicoloDelegate.getInstance().nuovoFascicolo(fVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fVO;
	}

	private static void aggiornaAssegnatariModel(JSProtocollazioneBean bean,
			ProtocolloIngresso protocollo, Utente utente) {
		protocollo.removeAssegnatari();
		UtenteVO uteVO = utente.getValueObject();
		Map<Integer, AssegnatarioVO> assegnatari = OggettarioDelegate.getInstance().getAssegnatari("Protocollazione da CDS");
		if (assegnatari != null) {
			for (AssegnatarioVO ass : assegnatari.values()) {
				Date now = new Date();
				ass.setDataAssegnazione(now);
				ass.setDataOperazione(now);
				ass.setCaricaAssegnanteId(utente.getCaricaInUso());
				ass.setUfficioAssegnanteId(utente.getUfficioInUso());
				ass.setRowCreatedUser(uteVO.getUsername());
				ass.setRowUpdatedUser(uteVO.getUsername());
				ass.setCompetente(true);
				protocollo.getProtocollo().setStatoProtocollo("S");
				ass.setPresaVisione(false);
				ass.setLavorato(false);
				protocollo.aggiungiAssegnatario(ass);
			}
		}
	}

	private static void aggiornaDestinatariModel(JSProtocollazioneBean bean,
			ProtocolloUscita protocollo, Utente utente) {
		protocollo.removeDestinatari();
		JSPersonaBean persona = bean.getPersona();
		DestinatarioVO destinatario = new DestinatarioVO();
		destinatario.setDestinatario(persona.getDenominazioneSocieta());
		destinatario.setFlagTipoDestinatario("G");
		destinatario.setEmail(persona.getEmails().get(0));
		destinatario.setCodicePostale(persona.getSedePgCap());
		destinatario.setIndirizzo(persona.getSedePgIndirizzo());
		destinatario.setCitta(persona.getSedePgComune());
		destinatario.setFlagPEC(true);
		protocollo.addDestinatari(destinatario);
		protocollo.getProtocollo().setStatoProtocollo("N");
	}

	private static void aggiornaFascicoliModel(JSProtocollazioneBean bean,
			Protocollo protocollo, Utente utente) {
		FascicoloVO fasc = FascicoloDelegate.getInstance().getFascicoloVOByCodice(String.valueOf(bean.getRichiestaId()));
		if (fasc.getReturnValue() == ReturnValues.NOT_FOUND) {
			fasc = nuovoFascicolo(bean, utente);
		}
		protocollo.aggiungiFascicolo(fasc);
	}

	private static List<SoggettoVO> newPersonafisicaList(JSPersonaBean persona) {
		List<SoggettoVO> mittenti = new ArrayList<SoggettoVO>();
		SoggettoVO mittente = null;
		mittente = new SoggettoVO('F');
		mittente.setCognome(persona.getCognome());
		mittente.setNome(persona.getNome());
		mittente.getIndirizzo().setToponimo(persona.getSedePgIndirizzo());
		mittente.getIndirizzo().setComune(persona.getSedePgComune());
		mittente.getIndirizzo().setProvinciaId(LookupDelegate.getInstance().getProvinciaIdFromCodiProv(persona.getSedePgProvincia()));
		mittente.getIndirizzo().setCap(persona.getSedePgCap());
		mittente.setComuneNascita(persona.getLuogoNascita());
		mittente.setDataNascita(DateUtil.getData(persona.getDataNascita()));
		mittente.setCodiceFiscale(persona.getCodiceFiscale());
		mittente.setIndirizzoEMail(persona.getEmails().get(0));
		mittenti.add(mittente);
		return mittenti;
	}

	private void allegaDocumenti(Protocollo p, JSProtocollazioneBean bean,
			Utente utente) {
		for (JSFileProtoBean fileBean : bean.getFileProtos()) {
			File file = new File(fileBean.getDoc());
			String contentType = new MimetypesFileTypeMap().getContentType(file);
			String impronta = FileUtil.calcolaDigest(file.getAbsolutePath(),FileConstants.SHA256);
			DocumentoVO doc = new DocumentoVO();
			doc.setContentType(contentType);
			doc.setImpronta(impronta);
			doc.setPath(file.getAbsolutePath());
			doc.setSize((int) file.length());
			doc.setFileName(file.getName());
			doc.setDescrizione(file.getName());
			doc.setRowCreatedTime(new Date(System.currentTimeMillis()));
			doc.setRowUpdatedTime(new Date(System.currentTimeMillis()));
			doc.setRowCreatedUser(utente.getValueObject().getUsername());
			doc.setRowUpdatedUser(utente.getValueObject().getUsername());
			if (fileBean.isEnableProto()) {
				p.setDocumentoPrincipale(doc);
			} else {
				p.allegaDocumento(doc);
			}
		}
	}

	public void aggiornaModelFromFascicolo(DocumentoVO docVO,
			Documento documento, Utente utente, FascicoloVO fasc) {
		FileVO fileVO = documento.getFileVO();
		documento.getFileVO().setDocumentoVO(docVO);
		aggiornaDatiGeneraliModelFromFascicolo(docVO, fileVO, utente);
		fileVO.setTitolarioId(fasc.getTitolarioId());
		fileVO.setStatoDocumento(Parametri.CHECKED_IN);
		fileVO.setStatoArchivio(Parametri.STATO_CLASSIFICATO);
		fileVO.aggiungiFascicolo(fasc);

	}

	private void aggiornaDatiGeneraliModelFromFascicolo(DocumentoVO docVO,
			FileVO documento, Utente utente) {
		documento.setId(0);
		documento.setTipoDocumentoId(0);
		documento.setDataDocumento(new Date());
		documento.setRepositoryFileId(0);
		CartellaVO root = null;
		try {
			root = DocumentaleDelegate.getInstance().getCartellaVOByCaricaId(utente.getCaricaInUso());
		} catch (DataException e) {
			e.printStackTrace();
		}
		documento.setCartellaId(root.getId());
		documento.setOggetto(docVO.getDescrizione());
		documento.setDescrizione(docVO.getDescrizione());
		documento.setDescrizioneArgomento(docVO.getDescrizione());
		documento.setNomeFile(docVO.getFileName());
		documento.setRowCreatedTime(new Date());
		documento.setVersione(0);
		documento.setCaricaLavId(utente.getCaricaInUso());
	}

}
