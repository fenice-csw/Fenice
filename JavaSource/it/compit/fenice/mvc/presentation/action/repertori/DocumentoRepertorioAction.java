package it.compit.fenice.mvc.presentation.action.repertori;

import it.compit.fenice.enums.UnitaAmministrativaEnum;
import it.compit.fenice.ftpHelper.BbccaaPaHtmlUtility;
import it.compit.fenice.ftpHelper.ErsuCataniaHtmlUtility;
import it.compit.fenice.ftpHelper.FtpHelper;
import it.compit.fenice.ftpHelper.GenericHtmlUtility;
import it.compit.fenice.ftpHelper.HtmlUtility;
import it.compit.fenice.ftpHelper.PoliclinicoHtmlUtility;
import it.compit.fenice.ftpHelper.ScuoleHtmlUtility;
import it.compit.fenice.mvc.bo.RepertorioBO;
import it.compit.fenice.mvc.business.RepertorioDelegate;
import it.compit.fenice.mvc.presentation.action.amministrazione.helper.file.RepertorioFileUtility;
import it.compit.fenice.mvc.presentation.actionform.repertori.DocumentoRepertorioForm;
import it.compit.fenice.mvc.presentation.helper.DocumentoRepertorioView;
import it.compit.fenice.mvc.vo.repertori.DocumentoRepertorioVO;
import it.compit.fenice.mvc.vo.repertori.RepertorioVO;
import it.compit.fenice.report.utility.LegacyJasperInputStream;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.presentation.action.amministrazione.TitolarioAction;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.vo.organizzazione.AmministrazioneVO;
import it.finsiel.siged.mvc.vo.organizzazione.AreaOrganizzativaVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.report.protocollo.CommonReportDS;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;
import it.finsiel.siged.util.PdfUtil;
import it.flosslab.mvc.presentation.action.protocollo.helper.file.ProtocolloFileUtility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class DocumentoRepertorioAction extends Action {

	static Logger logger = Logger.getLogger(TitolarioAction.class.getName());

	protected void assegnaAdUfficio(DocumentoRepertorioForm form, int ufficioId) {
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(ufficioId);
		Organizzazione org = Organizzazione.getInstance();
		Ufficio uff = org.getUfficio(ufficioId);
		ass.setNomeUfficio(uff.getValueObject().getDescription());
		ass.setDescrizioneUfficio(uff.getPath());
		form.setUfficioResponsabile(ass);
	}
	
	private void assegnaSettore(DocumentoRepertorioForm form, int ufficioId) {
		Organizzazione org = Organizzazione.getInstance();
		Ufficio uff = org.getUfficio(ufficioId);
		form.setSettoreProponente(uff.getValueObject().getDescription());
	}
	
	public File creaPDFTimbrato(InputStream is, String mark, int aooId, HttpServletRequest request) throws Exception {
		return PdfUtil.creaPDFTimbrato(is,mark, aooId,request);
	}

	private void pubblicaDocumentoRepertorio(HttpServletRequest request, UnitaAmministrativaEnum unitaAmministrativa, DocumentoRepertorioVO dRepVO, FtpHelper ftp, HtmlUtility hut,Utente utente) throws Exception{
		RepertorioDelegate delegate = RepertorioDelegate.getInstance();
			
		ftp.creaStruttura(dRepVO);
		Collection<RepertorioVO> listRepertori = delegate.getRepertoriByFlagWeb(utente.getAreaOrganizzativa().getId(), RepertorioVO.REPERTORIO_WEB);
		ftp.uploadFileOutputStream(hut.creaListaRepertoriHTML(listRepertori),"lista_repertori.html");
		ftp.changeDir(String.valueOf(dRepVO.getRepId()));
		Collection<DocumentoRepertorioView> listDoc = delegate.getDocumentiRepertorio(dRepVO.getRepId());
		RepertorioVO repVO = delegate.getRepertorio(dRepVO.getRepId());
		ftp.uploadFileOutputStream(hut.creaRepertorioHTML(repVO, dRepVO.getNumeroDocumentoRepertorio(), listDoc),"repertorio.html");
		ftp.changeDir(String.valueOf(dRepVO.getDocRepertorioId()));
		ftp.uploadFileOutputStream(hut.creaDocumentoRepertorioHTML(dRepVO,utente.getAreaOrganizzativa().getFlagPubblicazioneP7m()),"doc_repertorio.html");
		
		if(utente.getAreaOrganizzativa().getFlagPubblicazioneP7m()==AreaOrganizzativaVO.PUBBLICA_ORIGINALI){
			for (DocumentoVO allegatoVO : dRepVO.getDocumentiCollection()) {
				if (!allegatoVO.getRiservato() && allegatoVO.getPubblicabile()) {
					InputStream is = new FileInputStream(Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo()+ "/"+ "aoo_"+ utente.getAreaOrganizzativa().getId()+ "/"+ allegatoVO.getPath());
					ftp.uploadFileOutputStream(is, allegatoVO.getFileName());
				}
			}
		} else if(utente.getAreaOrganizzativa().getFlagPubblicazioneP7m()==AreaOrganizzativaVO.PUBBLICA_COPIA){
			for (DocumentoVO allegatoVO : dRepVO.getDocumentiCollection()) {
				if (!allegatoVO.getRiservato() && allegatoVO.getPubblicabile()) {
					if(!allegatoVO.getFileName().contains(".p7m")){
						InputStream is = new FileInputStream(Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo()+ "/"+ "aoo_"+ utente.getAreaOrganizzativa().getId()+ "/"+ allegatoVO.getPath());
						ftp.uploadFileOutputStream(is, allegatoVO.getFileName());
					}else{
						InputStream is = new FileInputStream(Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo()+ "/"+ "aoo_"+ utente.getAreaOrganizzativa().getId()+ "/"+ allegatoVO.getPath());
						String text=repVO.getDescrizione()+"n."+dRepVO.getNumeroDocumentoRepertorio()+" del "+dRepVO.getDataRepertorio()+" - copia conforme dell'originale firmato digitalmente";
						File fileTimbrato=creaPDFTimbrato(is, text, repVO.getAooId(), request);
						ftp.uploadFileOutputStream(new FileInputStream(fileTimbrato), allegatoVO.getFileName().replace(".p7m", ""));
					}
				}
			}
		} else if(utente.getAreaOrganizzativa().getFlagPubblicazioneP7m()==AreaOrganizzativaVO.PUBBLICA_ENTRAMBI){
			for (DocumentoVO allegatoVO : dRepVO.getDocumentiCollection()) {
				if (!allegatoVO.getRiservato() && allegatoVO.getPubblicabile()) {
						InputStream is = new FileInputStream(Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo()+ "/"+ "aoo_"+ utente.getAreaOrganizzativa().getId()+ "/"+ allegatoVO.getPath());
						ftp.uploadFileOutputStream(is, allegatoVO.getFileName());
					if(allegatoVO.getFileName().contains(".p7m")){
						InputStream isTmp = new FileInputStream(Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo()+ "/"+ "aoo_"+ utente.getAreaOrganizzativa().getId()+ "/"+ allegatoVO.getPath());
						String text=repVO.getDescrizione()+"n."+dRepVO.getNumeroDocumentoRepertorio()+" del "+dRepVO.getDataRepertorio()+" - copia conforme dell'originale firmato digitalmente";
						File fileTimbrato=creaPDFTimbrato(isTmp, text, repVO.getAooId(), request);
						ftp.uploadFileOutputStream(new FileInputStream(fileTimbrato), allegatoVO.getFileName().replace(".p7m", ""));
					}
				}
			}
		}
		ftp.goInRoot();
		ftp.disconnect();
		if(dRepVO.getFlagStato()==DocumentoRepertorioVO.REGISTRATO || dRepVO.getFlagStato()==DocumentoRepertorioVO.PUBBLICATO)
			delegate.aggiornaStato(dRepVO.getDocRepertorioId(), DocumentoRepertorioVO.PUBBLICATO);
		if(dRepVO.getFlagStato()==DocumentoRepertorioVO.PROTOCOLLATO || dRepVO.getFlagStato()==DocumentoRepertorioVO.PUBBLICATO_PROTOCOLLATO)
			delegate.aggiornaStato(dRepVO.getDocRepertorioId(), DocumentoRepertorioVO.PUBBLICATO_PROTOCOLLATO);
	}
	
	public ActionForward downloadDocumento(ActionMapping mapping,
			DocumentoVO doc, HttpServletRequest request,
			HttpServletResponse response, int aooId) throws Exception {
		ActionMessages errors = ProtocolloFileUtility.downloadFileProtocollo(
				doc, response, aooId);
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return mapping.findForward("erroreDownload");
		}
		ActionForward actionForward = new ActionForward();
		actionForward.setName("none");
		return actionForward;
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		HttpSession session = request.getSession();
		DocumentoRepertorioForm rForm = (DocumentoRepertorioForm) form;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		RepertorioDelegate delegate = RepertorioDelegate.getInstance();
		if (session.getAttribute("repertorioId") != null) {
			rForm.setRepertorioId((Integer) session
					.getAttribute("repertorioId"));
			session.removeAttribute("repertorioId");
		}
		
		boolean ufficioCompleto = true;
		if (request.getParameter("docId") != null) {
			int docRepertorioId = Integer
					.valueOf(request.getParameter("docId")).intValue();
			DocumentoRepertorioVO dRep = RepertorioDelegate.getInstance()
					.getDocumentoRepertorio(docRepertorioId);
			aggiornaDocumentoRepertorioForm(dRep, rForm);
			rForm.setBtnProtocollaVisibile(true);
			rForm.setBtnPubblicaVisibile(RepertorioDelegate.getInstance().getRepertorioFlagWeb(rForm.getRepertorioId()));
			request.setAttribute(mapping.getAttribute(), rForm);
		}
		if(request.getAttribute(Constants.DOCUMENTO_REPERTORIO)!=null){
			DocumentoRepertorioVO vo=(DocumentoRepertorioVO)request.getAttribute(Constants.DOCUMENTO_REPERTORIO);
			vo.setNumeroDocumentoRepertorio(delegate.getMaxNumeroRepertorio(
					vo.getRepId(), DateUtil.getYear(new Date())));
			aggiornaDocumentoRepertorioForm(vo, rForm);
			request.removeAttribute(Constants.DOCUMENTO_REPERTORIO);
			request.setAttribute(mapping.getAttribute(), rForm);
		}
		if (request.getParameter("docRepertorialeId") != null) {
			int docRepertorioId = Integer.valueOf(
					request.getParameter("docRepertorialeId")).intValue();
			DocumentoRepertorioVO dRep = delegate
					.getDocumentoRepertorio(docRepertorioId);
			dRep.setNumeroDocumentoRepertorio(delegate.getMaxNumeroRepertorio(
					dRep.getRepId(), DateUtil.getYear(new Date())));
			aggiornaDocumentoRepertorioForm(dRep, rForm);
			rForm.setBtnProtocollaVisibile(true);
			rForm.setBtnProtocollaVisibile(RepertorioDelegate.getInstance().getRepertorioFlagWeb(rForm.getRepertorioId()));
			request.setAttribute(mapping.getAttribute(), rForm);
		}
		if (request.getParameter("stampaDoc") != null) {
			Integer docId = Integer.valueOf(request.getParameter("stampaDoc"));
			DocumentoRepertorioVO dRepVO = delegate
					.getDocumentoRepertorio(docId);
			stampaDocumento(response, dRepVO,
					delegate.getRepertorio(dRepVO.getRepId()).getDescrizione());
			return (mapping.findForward("success"));
		}
		if (request.getParameter("btnPubblica") != null) {
			DocumentoRepertorioVO dRepVO = new DocumentoRepertorioVO();
			aggiornaDocumentoRepertorioModel(request, dRepVO, rForm, utente);
			delegate.salvaDocumentoRepertorio(utente, dRepVO);
			AmministrazioneVO amm = Organizzazione.getInstance().getValueObject();
			FtpHelper ftp = new FtpHelper();
			HtmlUtility hut = null;
			if(rForm.getUnitaAmministrativa().equals(UnitaAmministrativaEnum.POLICLINICO_CT)){
				hut=new PoliclinicoHtmlUtility();
			}else if(rForm.getUnitaAmministrativa().equals(UnitaAmministrativaEnum.BBCCAA_PA)){
				hut=new BbccaaPaHtmlUtility();
			}else if(rForm.getUnitaAmministrativa().equals(UnitaAmministrativaEnum.ERSU_CT)){
				hut=new ErsuCataniaHtmlUtility();
			}else if(rForm.getUnitaAmministrativa().equals(UnitaAmministrativaEnum.SCUOLA)){
				hut=new ScuoleHtmlUtility();
			}
			else{
				hut=new GenericHtmlUtility();
			}
			if (ftp.connect(amm.getHostFtp(), amm.getUserFtp(),
					amm.getPassFtp(), amm.getPortaFtp())) {
				if(amm.getFolderFtp()!=null && !amm.getFolderFtp().equals("")){
					ftp.changeDir(amm.getFolderFtp());
				}
				pubblicaDocumentoRepertorio(request, rForm.getUnitaAmministrativa(), dRepVO, ftp, hut, utente);
			} else {
				errors.add("errore connessione", new ActionMessage(
						"repertorio.not_connect"));
			}
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return (mapping.findForward("input"));
			}
			messages.add("pubblicazione_ok", new ActionMessage("msg.pubblicazione.ok"));
			saveMessages(request, messages);
			return (mapping.findForward("input"));
		}
		if (request.getParameter("btnNuovo") != null) {
			rForm.inizializza();
			int maxDocId = RepertorioDelegate.getInstance()
					.getMaxNumeroRepertorio(rForm.getRepertorioId(),
							DateUtil.getAnnoCorrente());
			rForm.setNumeroDocumentoRepertorio(String.valueOf(maxDocId));
		} if (request.getParameter("downloadAllegatoId") != null) {
			int docId = NumberUtil.getInt((String) request
					.getParameter("downloadAllegatoId"));
			int aooId = utente.getAreaOrganizzativa().getId();
			DocumentoVO doc = rForm.getDocumentoAllegato(String.valueOf(docId));
			return downloadDocumento(mapping, doc, request, response, aooId);
		}
		/*
		if (request.getParameter("downloadAllegatoTimbratoId") != null) {
			int docId = NumberUtil.getInt((String) request
					.getParameter("downloadAllegatoTimbratoId"));
			int aooId = utente.getAreaOrganizzativa().getId();
			DocumentoVO doc = rForm.getDocumentoAllegatoTimbrato(String.valueOf(docId));
			return downloadDocumento(mapping, doc, request, response, aooId);
		} 
		*/
		if (request.getParameter("allegaDocumentoAction") != null) {
			RepertorioFileUtility.uploadFile(rForm, request, errors);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			}
			return mapping.findForward("input");
		} if (request.getParameter("rimuoviAllegatiAction") != null) {
			String[] allegati = rForm.getAllegatiSelezionatiId();
			if (allegati != null) {
				for (int i = 0; i < allegati.length; i++) {
					//if 
					rForm.rimuoviAllegato(allegati[i]);
					allegati[i] = null;
				}
			}
			return mapping.findForward("input");
		} if (request.getParameter("impostaUfficioAction") != null) {
			rForm.setUfficioCorrenteId(rForm.getUfficioSelezionatoId());
		} if (request.getParameter("ufficioPrecedenteAction") != null) {
			rForm.setUfficioCorrenteId(rForm.getUfficioPrecedenteId());
		} if (request.getParameter("assegnaUfficioCorrenteAction") != null) {
			assegnaAdUfficio(rForm, rForm.getUfficioCorrenteId());
		} if (request.getParameter("annullaResponsabileAction") != null) {
			rForm.setUfficioResponsabile(null);
		} if (request.getParameter("impostaSettoreAction") != null) {
			rForm.setSettoreCorrenteId(rForm.getSettoreSelezionatoId());
		} if (request.getParameter("settorePrecedenteAction") != null) {
			rForm.setSettoreCorrenteId(rForm.getSettorePrecedenteId());
		} if (request.getParameter("assegnaSettoreCorrenteAction") != null) {
			assegnaSettore(rForm, rForm.getSettoreCorrenteId());
		} if (request.getParameter("annullaSettoreAction") != null) {
			rForm.setSettoreProponente(null);
		}
		/*
		if (request.getParameter("btnToPDF") != null) {
			RepertorioVO rep=RepertorioDelegate.getInstance().getRepertorio(rForm.getRepertorioId());
			int aooId = utente.getAreaOrganizzativa().getId();
			for(DocumentoVO doc: rForm.getDocumentiAllegatiCollection()){
				String text=rep.getDescrizione()+"n."+rForm.getNumeroDocumentoRepertorio()+" del "+rForm.getDataRepertorio()+" - copia conforme dell'originale firmato digitalmente";
				String fileTimbratoPath=creaPDFTimbrato(doc, text, aooId, request);
				RepertorioFileUtility.uploadFileTimbrato(doc, rForm, fileTimbratoPath, utente.getValueObject().getUsername(), errors);
			}
		}*/ 
		if (request.getParameter("btnConferma") != null) {
			RepertorioDelegate rd = RepertorioDelegate.getInstance();
			DocumentoRepertorioVO repertorio = new DocumentoRepertorioVO();
			aggiornaDocumentoRepertorioModel(request, repertorio, rForm, utente);
			if (!rd.isNumeroDocumentoRepertorioUsed(repertorio))
				repertorio=rd.salvaDocumentoRepertorio(utente, repertorio);
			else {
				errors.add("numeroDocumentoRepertorio", new ActionMessage(
						"doc_repertorio.numero_doc_rep.usato"));
			}
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			}else{
				repertorio=delegate.getDocumentoRepertorio(repertorio.getDocRepertorioId());
				aggiornaDocumentoRepertorioForm(repertorio, rForm);
				rForm.setBtnProtocollaVisibile(true);
				rForm.setBtnPubblicaVisibile(RepertorioDelegate.getInstance().getRepertorioFlagWeb(rForm.getRepertorioId()));
				messages.add("save_ok", new ActionMessage("msg.save.ok"));
				saveMessages(request, messages);
			}
			return (mapping.findForward("input"));
		} if (request.getParameter("btnProtocolla") != null) {
			RepertorioBO.preparaPostaInterna(request, rForm, utente, errors,0);
			return (mapping.findForward("protocollazionePostaInterna"));
		} if (request.getParameter("btnIndietro") != null) {
			request.setAttribute("repertorioId", rForm.getRepertorioId());
			return (mapping.findForward("indietro"));
		} if (!errors.isEmpty())
			saveErrors(request, errors);
		logger.info("Execute Documentov Repertorio");
		RepertorioBO.impostaUfficio(utente, rForm, ufficioCompleto);
		if(rForm.getUnitaAmministrativa().equals(UnitaAmministrativaEnum.POLICLINICO_CT))
			RepertorioBO.impostaSettore(utente, rForm, ufficioCompleto);
		return (mapping.findForward("input"));
	}

	private void aggiornaDocumentoRepertorioForm(DocumentoRepertorioVO vo,
			DocumentoRepertorioForm form) {
		form.setDocRepertorioId(vo.getDocRepertorioId());
		form.setRepertorioId(vo.getRepId());
		form.setOggetto(vo.getOggetto());
		form.setNumeroDocumento(vo.getNumeroDocumento());
		form.setDescrizione(vo.getDescrizione());
		form.setCapitolo(vo.getCapitolo());
		form.setNote(vo.getNote());
		if (vo.getNumeroDocumentoRepertorio() != 0)
			form.setNumeroDocumentoRepertorio(String.valueOf(vo
					.getNumeroDocumentoRepertorio()));
		if (vo.getImporto()!=null && vo.getImporto().intValue() != 0)
			form.setImporto(String.valueOf(vo.getImporto()));
		if (vo.getDataRepertorio() != null)
			form.setDataRepertorio(DateUtil.formattaData(vo.getDataRepertorio()
					.getTime()));
		if (vo.getDataValiditaInizio() != null)
			form.setDataValiditaInizio(DateUtil.formattaData(vo
					.getDataValiditaInizio().getTime()));
		if (vo.getDataValiditaFine() != null)
			form.setDataValiditaFine(DateUtil.formattaData(vo
					.getDataValiditaFine().getTime()));
		if (vo.getUfficioId() != 0) {
			assegnaAdUfficio(form, vo.getUfficioId());
			form.setUfficioCorrenteId(vo.getUfficioId());
		}
		
		if(vo.getDocumenti()!=null){
			form.setDocumentiAllegati(vo.getDocumenti());
		}
		
		form.setSettoreProponente(vo.getSettoreProponente());
		form.setProtocolloId(vo.getProtocolloId());
		form.setFlagStato(vo.getFlagStato());
		updateDocumentiPubblicabili(form);
	}
	
	private void updateDocumentiPubblicabili(DocumentoRepertorioForm form) {
		List<String> documentiPubblicabili = new ArrayList<String>();
		for (Iterator<DocumentoVO> i = form.getDocumentiAllegati().values().iterator(); i.hasNext();) {
			DocumentoVO doc = i.next();
			if (doc.getPubblicabile()) {
				documentiPubblicabili.add(String.valueOf(doc.getIdx()));
			}
		}
		String[] documentiPubblicabiliArray = new String[documentiPubblicabili.size()];
		int index = 0;
		for (String idxString : documentiPubblicabili) {
			documentiPubblicabiliArray[index] = idxString;
			index++;
		}
		form.setDocumentiPubblicabili(documentiPubblicabiliArray);

	}

	private void aggiornaDocumentoRepertorioModel(HttpServletRequest request, DocumentoRepertorioVO repVO,
			DocumentoRepertorioForm form, Utente utente) {
		
		if (request.getParameter("documentiPubblicabili") == null) { 
			form.resetDocumentiPubblicabili(); 
		} 
		
		repVO.setDocRepertorioId(form.getDocRepertorioId());
		repVO.setRepId(form.getRepertorioId());
		repVO.setOggetto(form.getOggetto());
		repVO.setNumeroDocumento(form.getNumeroDocumento());
		repVO.setDescrizione(form.getDescrizione());
		repVO.setCapitolo(form.getCapitolo());
		repVO.setNote(form.getNote());
		repVO.setSettoreProponente(form.getSettoreProponente());
		repVO.setProtocolloId(form.getProtocolloId());
		if (form.getImporto() != null && !"".equals(form.getImporto().trim()))
			repVO.setImporto(new BigDecimal(form.getImporto()));
		repVO.setNumeroDocumentoRepertorio(Integer.parseInt(form
				.getNumeroDocumentoRepertorio()));
		Date dataRep = DateUtil.toDate(form.getDataRepertorio());
		if (dataRep != null)
			repVO.setDataRepertorio(new java.sql.Date(dataRep.getTime()));
		else
			repVO.setDataRepertorio(null);

		Date dataValInizio = DateUtil.toDate(form.getDataValiditaInizio());
		if (dataValInizio != null)
			repVO.setDataValiditaInizio(new java.sql.Date(dataValInizio
					.getTime()));
		else
			repVO.setDataValiditaInizio(null);
		Date dataValFine = DateUtil.toDate(form.getDataValiditaFine());
		if (dataValFine != null)
			repVO.setDataValiditaFine(new java.sql.Date(dataValFine.getTime()));
		else
			repVO.setDataValiditaFine(null);
		if (form.getUfficioResponsabile() != null)
			repVO.setUfficioId(form.getUfficioResponsabile().getUfficioId());
		
		if(form.getDocumentiAllegati().size()!=0){
			Collection<DocumentoVO> docs = form.getDocumentiAllegati().values();
			for(DocumentoVO doc:docs){
				doc.setPubblicabile(form.isDocumentoPubblicabile(doc));
				repVO.allegaDocumento(doc);
			}
		}
		
		if (form.getRepertorioId() == 0) {
			repVO.setRowCreatedUser(utente.getValueObject().getUsername());
			repVO.setRowCreatedTime(new java.sql.Date(System
					.currentTimeMillis()));
		} else {
			repVO.setRowUpdatedUser(utente.getValueObject().getUsername());
			repVO.setRowUpdatedTime(new java.sql.Date(System
					.currentTimeMillis()));
		}
		repVO.setFlagStato(form.getFlagStato());
	}

	public void stampaDocumento(HttpServletResponse response,
			DocumentoRepertorioVO vo, String nomeRepertorio)
			throws IOException, ServletException {
		ServletContext context = this.getServlet().getServletContext();
		OutputStream os = response.getOutputStream();
		Organizzazione org = Organizzazione.getInstance();

		try {
			JasperDesign jasperDesign = JRXmlLoader
					.load(new LegacyJasperInputStream(
							new FileInputStream(
									context.getRealPath("/")
											+ FileConstants.STAMPA_DOC_REPERTORIO_TEMPLATE)));
			JasperReport jasperReport = JasperCompileManager
					.compileReport(jasperDesign);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("oggetto", vo.getOggetto());
			parameters
					.put("data_inizio", DateUtil.formattaData(vo
							.getDataValiditaInizio().getTime()));
			parameters.put("data_fine",
					DateUtil.formattaData(vo.getDataValiditaFine().getTime()));
			parameters.put("numero",
					String.valueOf(vo.getNumeroDocumentoRepertorio()));
			parameters.put("nome_repertorio", nomeRepertorio);
			if (vo.getImporto()!=null&& vo.getImporto().intValue() != 0)
				parameters.put("importo", String.valueOf(vo.getImporto()));
			else
				parameters.put("importo", "");
			if (vo.getCapitolo() != null)
				parameters.put("capitolo", vo.getCapitolo());
			if (vo.getUfficioId() != 0)
				parameters.put("responsabile", org
						.getUfficio(vo.getUfficioId()).getPath());
			else
				parameters.put("responsabile", "");
			CommonReportDS ds = new CommonReportDS(vo.getDocumentiCollection(),
					DocumentoVO.class);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			response.setHeader("Content-Disposition", "attachment;filename="
					+ vo.getOggetto() + ".pdf");
			response.setHeader("Cache-control", "");
			response.setContentType("application/pdf");
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
			exporter.exportReport();
		} catch (Exception e) {
			logger.error("", e);
			response.setContentType("text/plain");
			e.printStackTrace(new PrintStream(os));
		} finally {
			os.close();
		}
	}

}
