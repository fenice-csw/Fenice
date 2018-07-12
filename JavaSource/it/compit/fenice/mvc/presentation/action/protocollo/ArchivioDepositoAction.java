package it.compit.fenice.mvc.presentation.action.protocollo;

import it.compit.fenice.mvc.presentation.actionform.protocollo.ArchivioDepositoForm;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.model.documentale.Documento;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.FascicoloDelegate;
import it.finsiel.siged.mvc.presentation.helper.FascicoloView;
import it.finsiel.siged.mvc.vo.documentale.FileVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.report.protocollo.CommonReportDS;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.FileUtil;
import it.finsiel.siged.util.ServletUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
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

public class ArchivioDepositoAction extends Action {

	static Logger logger = Logger.getLogger(ArchivioDepositoAction.class
			.getName());

	protected void aggiungiFascicoloSelezionato(ArchivioDepositoForm form,
			int fascId, FascicoloDelegate delegate) {
		FascicoloView f = delegate.getFascicoloViewById(fascId);
		if (f != null)
			form.addFascicoloSelezionato(f);
	}

	private void rimuoviFascicoliSelezionati(ArchivioDepositoForm form) {
		String[] fascicoli = form.getFascicoliIds();
		if (fascicoli != null) {
			for (int i = 0; i < fascicoli.length; i++) {
				int fascicoloId = Integer.valueOf(fascicoli[i]);
				form.removeFascicoloSelezionato(fascicoloId);
			}
		}
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession();
		ArchivioDepositoForm aForm = (ArchivioDepositoForm) form;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		FascicoloDelegate delegate = FascicoloDelegate.getInstance();

		if (request.getParameter("btnVersamento") != null) {
			aForm.removeFascicoli();
			aForm
					.setFascicoli(delegate.getFascicoliDepositoArchivio(utente,
							1));
			return (mapping.findForward("edit"));
		}
		if (request.getParameter("btnAggiungi") != null) {
			if (aForm.getFascicoloChkBox() != null) {
				for (String id : aForm.getFascicoloChkBox())
					aggiungiFascicoloSelezionato(aForm, Integer.valueOf(id),
							delegate);
			}
			return (mapping.findForward("edit"));
		}
		if (request.getParameter("btnConferma") != null) {
			if (aForm.getFascicoliSelezionatiCollection() != null
					&& aForm.getFascicoliSelezionatiCollection().size() != 0) {
				Documento doc = new Documento();
								DateUtil.formattaData(System.currentTimeMillis());
				aggiornaDocumentoModel(aForm.getFascicoliSelezionatiCollection(),"Elenco versamento del "+DateUtil.formattaData(System.currentTimeMillis()), doc, utente, request, errors);
				doc=delegate.versamentoDeposito(utente, aForm.getFascicoliSelezionatiCollection(), doc);
				if(doc.getFileVO()!=null && doc.getFileVO().getId()!=0){
					request.setAttribute("documentoId", doc.getFileVO().getId());
					session.setAttribute("tornaArchivio", true);
					aForm.removeFascicoli();
					aForm.removeFascicoliSelezionati();
					return (mapping.findForward("fascicola"));
				}else{
					errors.add("generale", new ActionMessage(
					"errore_nel_salvataggio"));
				}
			}
			saveErrors(request, errors);
			return (mapping.findForward("edit"));
		}
		if (request.getParameter("btnRimuovi") != null) {
			rimuoviFascicoliSelezionati(aForm);
			return (mapping.findForward("edit"));
		}
		if (request.getParameter("btnFiltra") != null) {
			Date dataChiusuraDa = null;
			Date dataChiusuraA = null;
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			if (request.getParameter("dataChiusuraDa") != null
					&& !"".equals(request.getParameter("dataChiusuraDa"))) {
				dataChiusuraDa = df.parse(request
						.getParameter("dataChiusuraDa"));
			}
			if (request.getParameter("dataChiusuraA") != null
					&& !"".equals(request.getParameter("dataChiusuraA"))) {
				dataChiusuraA = new Date(df.parse(
						request.getParameter("dataChiusuraA")).getTime()
						+ Constants.GIORNO_MILLISECONDS - 1);
			}
			int maxRighe=Integer.parseInt(utente.getAreaOrganizzativa().getMaxRighe());
			FascicoloDelegate fascicoloDelegate = FascicoloDelegate
					.getInstance();
			int conta = fascicoloDelegate.contaFascicoliPerDeposito(utente,
					dataChiusuraDa, dataChiusuraA);
			aForm.setFascicoli(null);
			if (conta == 0) {
				errors.add("nessun_dato", new ActionMessage("nessun_dato", "",
						""));
			} else if (conta <= maxRighe) {
				aForm.setFascicoli(FascicoloDelegate.getInstance()
						.getFascicoliPerDeposito(utente, dataChiusuraDa,
								dataChiusuraA));
				return (mapping.findForward("edit"));
			} else {
				errors.add("controllo.maxrighe", new ActionMessage(
						"controllo.maxrighe", "" + conta, "fascicoli", ""
								+ maxRighe));
			}
			return (mapping.findForward("edit"));
		}
		aForm.removeFascicoliSelezionati();
		aForm.setFascicoli(delegate.getFascicoliDepositoArchivio(utente,1));
		return (mapping.findForward("edit"));
	}

	public File creaPdfElencoVersamento(HttpServletRequest request,
			Collection<FascicoloView> c, String title) {
		File tempFile=null;
		ServletContext context = this.getServlet().getServletContext();
		try {
			String folder = ServletUtil.getTempUserPath(request.getSession());
			tempFile = File.createTempFile("temp_", ".upload", new File(folder));
			OutputStream bos = new BufferedOutputStream(new FileOutputStream(tempFile), FileConstants.BUFFER_SIZE);
			JasperDesign jasperDesign = JRXmlLoader.load(context.getRealPath("/") + FileConstants.STAMPA_ELENCO_DEPOSITO_TEMPLATE);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("ReportTitle", title);
			CommonReportDS ds = new CommonReportDS(c, FascicoloView.class);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ds);
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, bos);
			exporter.exportReport();
		} catch (Exception e) {
			logger.error("", e);
			e.printStackTrace();

		}
		return tempFile;
	}

	public void aggiornaDocumentoModel(Collection<FascicoloView> c, String title,
			Documento documento, Utente utente, HttpServletRequest request,
			ActionMessages errors) {
		File tempFile = creaPdfElencoVersamento(request, c, title);
		if(tempFile!=null){
			String impronta = FileUtil.calcolaDigest(tempFile.getAbsolutePath(), errors);
			File file=new File(tempFile.getAbsolutePath());
			int size = Long.valueOf(file.length()).intValue();
			if (!"".equals(title) && title.length() > 100) {
				errors.add("documento", new ActionMessage("error.nomefile.lungo","", ""));
			}
			FileVO fileVO = documento.getFileVO();
			DocumentoVO docVO=new DocumentoVO();
			docVO.setMustCreateNew(true);
			docVO.setDescrizione(null);
			docVO.setFileName(title.replace(" ", "_")+".pdf");
			docVO.setPath(tempFile.getAbsolutePath());
			docVO.setImpronta(impronta);
			docVO.setSize(size);
			docVO.setContentType("application/pdf");
			docVO.setRowCreatedTime(new Date(System.currentTimeMillis()));
			docVO.setRowUpdatedTime(new Date(System.currentTimeMillis()));
			docVO.setRowCreatedUser(utente.getValueObject().getUsername());
			docVO.setRowUpdatedUser(utente.getValueObject().getUsername());
			fileVO.setDocumentoVO(docVO);
			aggiornaDatiGeneraliDocumentoModel(title, fileVO, utente);
		}
	}

	private void aggiornaDatiGeneraliDocumentoModel(String title,
			FileVO documento, Utente utente) {
		documento.setId(0);
		documento.setDataDocumento(new java.sql.Date(System.currentTimeMillis()));
		documento.setOggetto(title);
		documento.setDescrizione(title);
		documento.setNomeFile(title + ".pdf");
		documento.setRowCreatedTime(new java.sql.Date(System
				.currentTimeMillis()));
		documento.setVersione(0);
		documento.setCaricaLavId(utente.getCaricaInUso());
		documento.setOwnerCaricaId(utente.getCaricaInUso());

	}

}
