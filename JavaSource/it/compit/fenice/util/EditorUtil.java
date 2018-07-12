package it.compit.fenice.util;

import it.compit.fenice.model.protocollo.PostaInterna;
import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.presentation.actionform.documentale.EditorForm;
import it.compit.fenice.mvc.presentation.helper.TemplateBBCCAAView;
import it.compit.fenice.mvc.vo.documentale.EditorVO;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.dao.digitalsignature.VerificaFirma;
import it.finsiel.siged.exception.CRLNonAggiornataException;
import it.finsiel.siged.exception.CertificatoNonValidoException;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.exception.FirmaNonValidaException;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.Protocollo;
import it.finsiel.siged.model.protocollo.ProtocolloUscita;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.business.AmministrazioneDelegate;
import it.finsiel.siged.mvc.business.FascicoloDelegate;
import it.finsiel.siged.mvc.business.ProcedimentoDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.business.TitoliDestinatarioDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProcedimentoForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.presentation.helper.DestinatarioView;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.lookup.TipoProcedimentoVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.ProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.report.protocollo.CommonReportDS;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.FileUtil;
import it.finsiel.siged.util.PdfUtil;
import it.finsiel.siged.util.ServletUtil;
import it.finsiel.siged.util.StringUtil;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class EditorUtil {

	static Logger logger = Logger.getLogger(EditorUtil.class.getName());

	public static OutputStream creaPDF(HttpServletRequest request,
			EditorForm form, OutputStream os, Utente utente, int numProt)
			throws IOException, ServletException {
		ServletContext context = request.getSession().getServletContext();
		try {
			JasperDesign jasperDesign = JRXmlLoader.load(context
					.getRealPath("/")
					+ FileConstants.STAMPA_BBCCAA_TEMPLATE_TEMPLATE);
			JasperReport jasperReport = JasperCompileManager
					.compileReport(jasperDesign);
			Map<String,Object> parameters = new HashMap<String,Object>();
			File reportImage = new File(context.getRealPath("/")
					+ FileConstants.STAMPA_BBCCAA_TEMPLATE_IMAGE);
			InputStream inStream = new java.io.FileInputStream(reportImage);
			parameters.put("oggetto", form.getOggetto());
			if (form.getAllaccio().getProtocolloAllacciatoId() != 0) {
				parameters.put("numAllaccio", String.valueOf(form.getAllaccio()
						.getNumeroProtocollo()));
				parameters.put("dataAllaccio", DateUtil.formattaData(form
						.getAllaccio().getDataRegistrazione().getTime()));
			}
			if (form.getTipo().equals("IN")) {
				parameters.put("numProt", String.valueOf(numProt + 1));
				parameters.put("dataProt", DateUtil.formattaData(new Date()
						.getTime()));
			}
			parameters.put("mittenteUfficio", utente.getUfficioVOInUso()
					.getDescription());
			parameters.put("mittenteTelefono", utente.getUfficioVOInUso()
					.getTeleFax());
			parameters.put("mittenteMail", utente.getUfficioVOInUso()
					.getEmail());
			parameters.put("image", inStream);
			Collection<TemplateBBCCAAView> reportList = new ArrayList<TemplateBBCCAAView>();
			if (form.getTipo().equals("OUT")) {
				int size = form.getDestinatari().size();
				for (DestinatarioView d : form.getDestinatari()) {
					TemplateBBCCAAView t = new TemplateBBCCAAView(d);
					if (reportList.size() == size - 1)
						t.setTesto(form.getTesto());
					reportList.add(t);
				}
			} else {
				TemplateBBCCAAView t = new TemplateBBCCAAView();
				t.setTesto(form.getTesto());
				reportList.add(t);
			}
			CommonReportDS ds = new CommonReportDS(reportList,
					TemplateBBCCAAView.class);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			//Map imagesMap = new HashMap();
			JRHtmlExporter exporter = new JRHtmlExporter();
			exporter
					.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
			//exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, imagesMap);
			exporter.setParameter(
					JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,
					new Boolean(false));
			exporter.exportReport();
		} catch (Exception e) {
			logger.error("", e);
			e.printStackTrace();
		}
		return os;
	}

	public static void aggiornaModel(EditorForm eForm, EditorVO eVO,
			Utente utente) {
		eVO.setCaricaId(utente.getCaricaInUso());
		eVO.setDocumentoId(eForm.getDocumentoId());
		eVO.setTxt(eForm.getTesto());
		eVO.setRowCreatedUser(utente.getValueObject().getUsername());
		eVO.setFlagStato(eForm.getFlagStato());
		eVO.setNome(eForm.getNomeFile());
		eVO.setOggetto(eForm.getOggetto());
		if (eForm.getTipo() != null && eForm.getTipo().equals("OUT"))
			aggiornaDestinatariModel(eForm, eVO, utente);
		else if (eForm.getTipo() != null && eForm.getTipo().equals("IN"))
			aggiornaAssegnatariModel(eForm, eVO, utente);
		if (eForm.getAllaccio().getNumeroProtocollo() > 0)
			eVO.allacciaProtocollo(eForm.getAllaccio());
		eVO.setFascicoli(eForm.getFascicoliProtocollo());
	}

	private static void aggiornaAssegnatariModel(EditorForm form, EditorVO vo,
			Utente utente) {
		vo.removeAssegnatari();
		UtenteVO uteVO = utente.getValueObject();
		Collection<AssegnatarioView> assegnatari = form.getAssegnatari();
		if (assegnatari != null) {
			for (AssegnatarioView ass : assegnatari) {
				AssegnatarioVO assegnatario = new AssegnatarioVO();
				Date now = new Date();
				assegnatario.setDataAssegnazione(now);
				assegnatario.setDataOperazione(now);
				assegnatario.setRowCreatedUser(uteVO.getUsername());
				assegnatario.setRowUpdatedUser(uteVO.getUsername());
				assegnatario.setUfficioAssegnanteId(utente.getUfficioInUso());
				assegnatario.setCaricaAssegnanteId(utente.getCaricaInUso());
				assegnatario.setUfficioAssegnatarioId(ass.getUfficioId());
				if (ass.getUtenteId() != 0) {
					assegnatario.setCaricaAssegnatarioId(CaricaDelegate
							.getInstance().getCaricaByUtenteAndUfficio(
									ass.getUtenteId(), ass.getUfficioId())
							.getCaricaId());
				} else {
					assegnatario.setCaricaAssegnatarioId(0);
				}
				if (form.isCompetente(ass)) {
					assegnatario.setCompetente(true);
					assegnatario.setStatoAssegnazione('S');
				}
				assegnatario.setPresaVisione(ass.isPresaVisione());
				vo.aggiungiAssegnatario(assegnatario);
			}
		}
	}

	private static void aggiornaDestinatariModel(EditorForm form, EditorVO vo,
			Utente utente) {
		vo.removeDestinatari();
		Collection<DestinatarioView> destinatari = form.getDestinatari();
		if (destinatari != null) {
			for (Iterator<DestinatarioView> i = destinatari.iterator(); i.hasNext();) {
				DestinatarioView dest =i.next();
				DestinatarioVO destinatario = new DestinatarioVO();
				destinatario.setIdx(dest.getIdx());
				if ("F".equals(dest.getFlagTipoDestinatario())) {
					destinatario.setNome(dest.getNome());
					destinatario.setCognome(dest.getCognome());
					destinatario.setDestinatario(dest.getDestinatario());
				} else {
					destinatario.setDestinatario(dest.getDestinatario());
				}
				destinatario.setFlagTipoDestinatario(dest
						.getFlagTipoDestinatario());
				destinatario.setEmail(dest.getEmail());
				destinatario.setCodicePostale(dest.getCapDestinatario());

				if (dest.getIndirizzo() != null
						&& !(dest.getIndirizzo().equals(""))) {
					destinatario.setIndirizzo(dest.getIndirizzo());

				}
				if (dest.getCitta() != null) {
					destinatario.setCitta(dest.getCitta());

				} else {
					destinatario.setCitta("");
				}

				destinatario.setMezzoSpedizioneId(dest.getMezzoSpedizioneId());
				if (dest.getDataSpedizione() != null
						&& !"".equals(dest.getDataSpedizione())) {
				}
				destinatario.setDataSpedizione(DateUtil.toDate(dest
						.getDataSpedizione()));
				destinatario.setFlagConoscenza(dest.getFlagConoscenza());
				destinatario.setFlagPresso(dest.getFlagPresso());
				destinatario.setFlagPEC(dest.getFlagPEC());
				destinatario.setTitoloId(dest.getTitoloId());
				destinatario.setNote(dest.getNote());
				destinatario.setMezzoDesc(dest.getMezzoDesc());
				destinatario.setPrezzo(dest.getPrezzoSpedizione());
				vo.addDestinatari(destinatario);
			}
		}
	}

	public static void aggiornaDocumentoForm(EditorVO vo, EditorForm form, Utente utente) {
		form.setDocumentoId(vo.getDocumentoId());
		form.setCaricaId(vo.getCaricaId());
		form.setTesto(vo.getTxt());
		form.setNomeFile(vo.getNome());
		form.setFlagStato(vo.getFlagStato());
		form.setVersione(vo.getVersione());
		form.setOggetto(vo.getOggetto());
		form.setTipo(vo.getFlagTipo() == 1 ? "IN" : "OUT");
		if(utente.getCaricaVOInUso().isResponsabileEnte()){
			form.setResponsabileEnte(true);
			form.setDirigente(true);
		}
		aggiornaDestinatariForm(vo, form);
		aggiornaAllacciForm(vo, form);
		aggiornaFascicoloForm(vo, form);
		aggiornaAssegnatariForm(vo, form);
		
	}

	public static void aggiornaFormDaProcedimento(EditorForm form,
			HttpServletRequest request) {
		ProcedimentoForm proc = (ProcedimentoForm) request.getSession()
				.getAttribute("procedimentoForm");
		FascicoloVO fasc = FascicoloDelegate.getInstance().getFascicoloVOById(
				proc.getFascicoloId());
		form.rimuoviDestinatari();
		form.rimuoviFascicoli();
		form.aggiungiFascicolo(fasc);
		/*
		if (proc.getInteressato().getId() != null) {
			form
					.aggiungiDestinatario(getDestinatarioView(proc
							.getInteressato()));
		}
		if (proc.getDelegato().getId() != null) {
			form.aggiungiDestinatario(getDestinatarioView(proc.getDelegato()));
		}
		*/
	}

	public static DestinatarioView getDestinatarioView(SoggettoVO s) {
		DestinatarioView dest = new DestinatarioView();
		if (s.getTipo().equals("F"))
			dest.setDestinatario(s.getCognome() + " " + s.getNome());
		else {
			dest.setDestinatario(s.getDescrizioneDitta());
		}
		dest.setFlagTipoDestinatario(s.getTipo());
		if (s.getIndirizzo().getComune() != null) {
			dest.setCitta(s.getIndirizzo().getComune());
		}

		dest.setEmail(s.getIndirizzoEMail());
		if (s.getIndirizzo().getCap() != null) {
			dest.setCapDestinatario(s.getIndirizzo().getCap());
		}
		if (s.getIndirizzo().getToponimo() != null) {
			dest
					.setIndirizzo((s.getIndirizzo().getDug() == null
							|| s.getIndirizzo().getDug().trim().equals("") ? ""
							: s.getIndirizzo().getDug())
							+ (s.getIndirizzo().getToponimo() == null
									|| s.getIndirizzo().getToponimo().trim()
											.equals("") ? "" : " "
									+ s.getIndirizzo().getToponimo())
							+ (s.getIndirizzo().getCivico() == null
									|| s.getIndirizzo().getCivico().trim()
											.equals("") ? "" : " "
									+ s.getIndirizzo().getCivico()));
		}
		dest.setFlagConoscenza(false);
		dest.setFlagPresso(false);
		dest.setFlagPEC(false);
		return dest;
	}

	private static void aggiornaDestinatariForm(EditorVO vo, EditorForm form) {
		for (Iterator<DestinatarioVO> i = vo.getDestinatari().iterator(); i.hasNext();) {
			DestinatarioVO destinatario =  i.next();
			DestinatarioView dest = new DestinatarioView();
			dest.setTitoloId(destinatario.getTitoloId());
			if (destinatario.getTitoloId() > 0) {
				dest.setTitoloDestinatario(TitoliDestinatarioDelegate
						.getInstance().getTitoloDestinatario(
								destinatario.getTitoloId()).getDescription());
			}
			dest.setDestinatario(destinatario.getDestinatario());
			dest
					.setFlagTipoDestinatario(destinatario
							.getFlagTipoDestinatario());
			if (destinatario.getCitta() != null) {
				dest.setCitta(destinatario.getCitta());
			}

			dest.setEmail(destinatario.getEmail());
			if (destinatario.getCodicePostale() != null) {
				dest.setCapDestinatario(destinatario.getCodicePostale());
			}
			if (destinatario.getIndirizzo() != null) {
				dest.setIndirizzo(destinatario.getIndirizzo());
			}
			dest.setMezzoSpedizioneId(destinatario.getMezzoSpedizioneId());
			dest.setMezzoDesc(destinatario.getMezzoDesc());

			Date dataSped = destinatario.getDataSpedizione();
			if (dataSped != null) {
				dest.setDataSpedizione(DateUtil
						.formattaData(dataSped.getTime()));
			}
			dest.setFlagConoscenza(destinatario.getFlagConoscenza());
			dest.setFlagPresso(destinatario.getFlagPresso());
			dest.setFlagPEC(destinatario.getFlagPEC());
			dest.setNote(destinatario.getNote());
			dest.setPrezzoSpedizione(destinatario.getPrezzo());
			form.aggiungiDestinatario(dest);
		}
	}

	private static void aggiornaAllacciForm(EditorVO vo, EditorForm form) {
		for (Iterator<AllaccioVO> i = vo.getAllacci().iterator(); i.hasNext();) {
			AllaccioVO allaccio =  i.next();
			form.setAllaccio(allaccio);
		}
	}

	private static void aggiornaFascicoloForm(EditorVO vo, EditorForm form) {
		if (vo != null && vo.getFascicoli() != null) {
			for (Iterator<FascicoloVO> i = vo.getFascicoli().iterator(); i.hasNext();) {
				FascicoloVO fascicolo =  i.next();
				form.aggiungiFascicolo(fascicolo);
				int procedimentoId=ProcedimentoDelegate.getInstance().getProcedimentoByFascicoloId(fascicolo.getId());
				if(procedimentoId!=0) {
					form.setProcedimentoId(procedimentoId);
					ProcedimentoVO proc=ProcedimentoDelegate.getInstance().getProcedimentoVO(procedimentoId);
					TipoProcedimentoVO tp=AmministrazioneDelegate.getInstance().getTipoProcedimento(proc.getTipoProcedimentoId());
					if(tp.isUll()){
						form.setUll(true);
						form.setStatoProcedimentoULL(proc.getStatoId());
						form.setProcedimentoMsg("Procedimento n."+proc.getNumeroProcedimento());
						if(!form.isResponsabileEnte()){
							form.setTipo("IN");
						}
						else{
							form.setTipo("IN");
						}
					}
				}
			}
		}
	}

	private static void aggiornaAssegnatariForm(EditorVO vo, EditorForm form) {
		Organizzazione org = Organizzazione.getInstance();
		for (Iterator<AssegnatarioVO> i = vo.getAssegnatari().iterator(); i.hasNext();) {
			AssegnatarioVO assegnatario =  i.next();
			AssegnatarioView ass = new AssegnatarioView();
			int uffId = assegnatario.getUfficioAssegnatarioId();
			ass.setUfficioId(uffId);
			Ufficio uff = org.getUfficio(uffId);
			if (uff != null) {
				ass.setNomeUfficio(uff.getValueObject().getDescription());
			}
			if (assegnatario.getCaricaAssegnatarioId() != 0) {
				int caricaId = assegnatario.getCaricaAssegnatarioId();
				CaricaVO carica = org.getCarica(caricaId);
				ass.setUtenteId(carica.getUtenteId());
				Utente ute = org.getUtente(carica.getUtenteId());
				if (ute != null) {
					ute.getValueObject().setCarica(carica.getNome());
					if (carica.isAttivo())
						ass.setNomeUtente(ute.getValueObject()
								.getCaricaFullName());
					else
						ass.setNomeUtente(ute.getValueObject()
								.getCaricaFullNameNonAttivo());
				} else
					ass.setNomeUtente(carica.getNome());
			}
			ass.setStato(assegnatario.getStatoAssegnazione());
			form.aggiungiAssegnatario(ass);
			if (assegnatario.isCompetente()) {
				form.setAssegnatarioCompetente(ass.getKey());
			}
			ass.setPresaVisione(assegnatario.isPresaVisione());
		}

	}

	public static void preparaPostaInterna(HttpServletRequest request,
			EditorForm form, Utente utente, ActionMessages errors, int flagTipo)
			throws DataException {
		PostaInterna pi = ProtocolloBO.getDefaultPostaInterna(utente);
		try {
			pi.setIntDocEditor(form.getDocumentoId());
			ProtocolloVO protocollo = pi.getProtocollo();
			if (form.isCaricaDocumento()) {
				protocollo
						.setDataDocumento(new Date(System.currentTimeMillis()));
				if (flagTipo == 0)
					pi.setDocumentoPrincipale(FileUtil.uploadDocumento(form,
							request, errors));
				else
					pi.setDocumentoPrincipale(EditorUtil
							.uploadDocumentoTemplate(form, request, errors,
									utente));
			}
			AssegnatarioVO mittente = new AssegnatarioVO();
			mittente.setUfficioAssegnatarioId(utente.getUfficioInUso());
			mittente.setUtenteAssegnatarioId(utente.getValueObject().getId());
			pi.setMittente(mittente);
			pi.setFascicoliRisposta(form.getFascicoliProtocollo());
			protocollo.setOggetto(form.getOggetto());
			pi.setProtocollo(protocollo);
			pi.getProtocollo().setStatoProtocollo("P");
			if (form.getAllaccio().getNumeroProtocollo() != 0)
				pi.allacciaProtocollo(form.getAllaccio());
			aggiornaAssegnatariPostaInternaModel(form, pi, utente);
			aggiornaProcedimentoProtocolloModel(form, pi, utente);
			request.setAttribute(Constants.PROTOCOLLO_DA_EDITOR, pi);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException("Errore nella generazione del Protocollo");
		}
	}

	public static void preparaProtocolloUscita(HttpServletRequest request,
			EditorForm form, Utente utente, ActionMessages errors, int flagTipo)
			throws DataException {
		ProtocolloUscita pu = ProtocolloBO.getDefaultProtocolloUscita(utente);
		try {
			pu.setIntDocEditor(form.getDocumentoId());
			ProtocolloVO protocollo = pu.getProtocollo();
			if (form.isCaricaDocumento()) {
				protocollo
						.setDataDocumento(new Date(System.currentTimeMillis()));
				if (flagTipo == 0)
					pu.setDocumentoPrincipale(FileUtil.uploadDocumento(form,
							request, errors));
				else
					pu.setDocumentoPrincipale(EditorUtil
							.uploadDocumentoTemplate(form, request, errors,
									utente));
			}
			AssegnatarioVO mittente = new AssegnatarioVO();
			mittente.setUfficioAssegnatarioId(utente.getUfficioInUso());
			mittente.setUtenteAssegnatarioId(utente.getValueObject().getId());
			pu.setMittente(mittente);
			pu.setFascicoli(form.getFascicoliProtocollo());
			protocollo.setOggetto(form.getOggetto());
			pu.setProtocollo(protocollo);
			pu.getProtocollo().setStatoProtocollo("P");
			if (form.getAllaccio().getNumeroProtocollo() != 0)
				pu.allacciaProtocollo(form.getAllaccio());
			aggiornaDestinatariProtocolloModel(form, pu, utente);
			aggiornaProcedimentoProtocolloModel(form, pu, utente);
			if (form.getDataScadenza() != null
					&& !form.getDataScadenza().equals(""))
				pu.getProtocollo().setDataScadenza(
						DateUtil.getData(form.getDataScadenza()));
			if (form.getTextScadenza() != null
					&& !form.getTextScadenza().equals(""))
				pu.getProtocollo().setEstermiAutorizzazione(
						form.getTextScadenza());

			request.setAttribute(Constants.PROTOCOLLO_DA_EDITOR, pu);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException("Errore nella generazione del Protocollo");
		}
	}

	private static void aggiornaProcedimentoProtocolloModel(EditorForm form,
			Protocollo p, Utente utente) {
		ProcedimentoDelegate delegate=ProcedimentoDelegate.getInstance();
		if(form.getFascicoliProtocollo().size()!=0){
			for(Object o:form.getFascicoliProtocollo()){
				FascicoloVO f=(FascicoloVO)o;
				ProcedimentoVO proc=delegate.getProcedimentoVO(delegate.getProcedimentoByFascicoloId(f.getId()));
				ProtocolloProcedimentoVO procedimento = new ProtocolloProcedimentoVO();
				procedimento.setProcedimentoId(proc.getId());
				procedimento.setNumeroProcedimento(proc.getNumeroProcedimento());
				procedimento.setOggetto(proc.getOggetto());
				procedimento.setModificabile(true);
				p.getProcedimenti().add(procedimento);
			}
		}
		
		if (form.getProcedimentoId() > 0) {
			ProcedimentoVO proc = delegate.getProcedimentoVO(form.getProcedimentoId());
			if (proc != null) {
				ProtocolloProcedimentoVO procedimento = new ProtocolloProcedimentoVO();
				procedimento.setProcedimentoId(proc.getId());
				procedimento.setNumeroProcedimento(proc.getAnno()+ StringUtil.formattaNumeroProcedimento(proc.getNumeroProcedimento(), 7));
				procedimento.setOggetto(proc.getOggetto());
				procedimento.setModificabile(true);
				p.getProcedimenti().add(procedimento);
			}
		}
	}

	private static void aggiornaDestinatariProtocolloModel(EditorForm form,
			ProtocolloUscita protocollo, Utente utente) {
		protocollo.removeDestinatari();
		Collection<DestinatarioView> destinatari = form.getDestinatari();
		boolean spedito = false;
		if (destinatari != null) {
			for (Iterator<DestinatarioView> i = destinatari.iterator(); i.hasNext();) {
				DestinatarioView dest =  i.next();
				DestinatarioVO destinatario = new DestinatarioVO();
				destinatario.setIdx(dest.getIdx());
				if ("F".equals(dest.getFlagTipoDestinatario())) {
					destinatario.setNome(dest.getNome());
					destinatario.setCognome(dest.getCognome());
					destinatario.setDestinatario(dest.getDestinatario());
				} else {
					destinatario.setDestinatario(dest.getDestinatario());
				}
				destinatario.setFlagTipoDestinatario(dest
						.getFlagTipoDestinatario());
				destinatario.setEmail(dest.getEmail());
				destinatario.setCodicePostale(dest.getCapDestinatario());

				if (dest.getIndirizzo() != null
						&& !(dest.getIndirizzo().equals(""))) {
					destinatario.setIndirizzo(dest.getIndirizzo());

				}
				if (dest.getCitta() != null) {
					destinatario.setCitta(dest.getCitta());

				} else {
					destinatario.setCitta("");
				}

				destinatario.setMezzoSpedizioneId(dest.getMezzoSpedizioneId());
				if (dest.getDataSpedizione() != null
						&& !"".equals(dest.getDataSpedizione())) {
					spedito = true;
				}
				destinatario.setDataSpedizione(DateUtil.toDate(dest
						.getDataSpedizione()));
				destinatario.setFlagConoscenza(dest.getFlagConoscenza());
				destinatario.setFlagPresso(dest.getFlagPresso());
				destinatario.setFlagPEC(dest.getFlagPEC());
				destinatario.setTitoloId(dest.getTitoloId());
				destinatario.setNote(dest.getNote());
				destinatario.setMezzoDesc(dest.getMezzoDesc());
				destinatario.setPrezzo(dest.getPrezzoSpedizione());
				protocollo.addDestinatari(destinatario);
			}
		}
		if (spedito) {
			protocollo.getProtocollo().setStatoProtocollo("S");
		} else {
			protocollo.getProtocollo().setStatoProtocollo("N");
		}
	}

	private static void aggiornaAssegnatariPostaInternaModel(EditorForm form,
			PostaInterna protocollo, Utente utente) {
		protocollo.removeDestinatari();
		UtenteVO uteVO = utente.getValueObject();
		Collection<AssegnatarioView> assegnatari = form.getAssegnatari();
		if (assegnatari != null) {
			for (AssegnatarioView ass : assegnatari) {
				AssegnatarioVO assegnatario = new AssegnatarioVO();
				Date now = new Date();
				assegnatario.setDataAssegnazione(now);
				assegnatario.setDataOperazione(now);
				assegnatario.setRowCreatedUser(uteVO.getUsername());
				assegnatario.setRowUpdatedUser(uteVO.getUsername());
				assegnatario.setUfficioAssegnanteId(utente.getUfficioInUso());
				assegnatario.setUtenteAssegnanteId(utente.getCaricaInUso());
				assegnatario.setUfficioAssegnatarioId(ass.getUfficioId());
				if (ass.getUtenteId() != 0) {
					assegnatario.setCaricaAssegnatarioId(CaricaDelegate
							.getInstance().getCaricaByUtenteAndUfficio(
									ass.getUtenteId(), ass.getUfficioId())
							.getCaricaId());
				} else {
					assegnatario.setCaricaAssegnatarioId(0);
				}
				if (form.isCompetente(ass)) {
					assegnatario.setCompetente(true);
					if (ass.getUtenteId() > 0)
						protocollo.getProtocollo().setStatoProtocollo("N");
					else
						protocollo.getProtocollo().setStatoProtocollo("S");
					if (protocollo.getFascicoli().size() > 0)
						protocollo.getProtocollo().setStatoProtocollo("A");
				}
				assegnatario.setPresaVisione(ass.isPresaVisione());
				protocollo.aggiungiDestinatario(assegnatario);
			}
		}
	}

	public static DocumentoVO uploadDocumentoTemplate(EditorForm form,
			HttpServletRequest request, ActionMessages errors, Utente utente) {
		//InputStream is = new ByteArrayInputStream(form.getTesto().getBytes());
		DocumentoVO documento = new DocumentoVO();
		int ultimoNum = ProtocolloDelegate.getInstance().getUltimoProtocollo(
				DateUtil.getAnnoCorrente(), utente.getRegistroPostaInterna());
		String fileName = "";
		String tempFilePath = "";
		String ctxPath="http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath();
		if (form.getTipo().equals("IN")) {
			fileName = "P"
					+ DateUtil.getAnnoCorrente()
					+ ""
					+ StringUtil.formattaNumeroProtocollo(String
							.valueOf(ultimoNum + 1), 7) + ""
					+ utente.getAreaOrganizzativa().getCodi_aoo() + ".pdf";
		} else {
			fileName = utente.getRegistroInUso()
					+ "I"
					+ DateUtil.getAnnoCorrente()
					+ ""
					+ StringUtil.formattaNumeroProtocollo(String
							.valueOf(ultimoNum + 1), 7) + ""
					+ utente.getAreaOrganizzativa().getCodi_aoo() + ".pdf";
		}
		String contentType = "application/pdf";
		logger.info(contentType);
		try {
			String text="";
			
			if(form.isUll())
				text=VelocityTemplateUtils.createULLDocument(form.getTesto(), utente,ctxPath);
			else
				text=VelocityTemplateUtils.createBBCCAADocument(form, utente,ctxPath);
			InputStream vis = new ByteArrayInputStream(text.getBytes("UTF-8"));
			tempFilePath = PdfUtil
					.creaPDFdaHtml(fileName, vis, request, errors);
			String impronta = FileUtil.calcolaDigest(tempFilePath, errors);
			File file = new File(tempFilePath);
			int size = Long.valueOf(file.length()).intValue();
			if (!"".equals(fileName) && fileName.length() > 100) {
				errors.add("documento", new ActionMessage(
						"error.nomefile.lungo", "", ""));
			} else if (size > 0 && !"".equals(fileName)) {
				if (errors.isEmpty()) {

					VerificaFirma
							.verificaFileFirmato(tempFilePath, contentType);

					String username = ((Utente) request.getSession()
							.getAttribute(Constants.UTENTE_KEY))
							.getValueObject().getUsername();
					documento.setMustCreateNew(true);
					documento.setDescrizione(null);
					documento.setFileName(fileName);
					documento.setPath(tempFilePath);
					documento.setImpronta(impronta);
					documento.setSize(size);
					documento.setContentType(contentType);
					documento.setRowCreatedTime(new Date(System
							.currentTimeMillis()));
					documento.setRowUpdatedTime(new Date(System
							.currentTimeMillis()));
					documento.setRowCreatedUser(username);
					documento.setRowUpdatedUser(username);
				}
			} else {
				errors.add("documento", new ActionMessage("campo.obbligatorio",
						"File", ""));
			}
		} catch (DataException e) {
			errors.add("allegati", new ActionMessage("database.cannot.load"));
		} catch (CertificatoNonValidoException e) {
			errors.add("allegati", new ActionMessage(
					"errore.verificafirma.doc.non_valido", e.getMessage()));
		} catch (FirmaNonValidaException e) {
			errors.add("allegati", new ActionMessage(
					"errore.verificafirma.doc.non_valido", e.getMessage()));
		} catch (CRLNonAggiornataException e) {
			errors.add("allegati", new ActionMessage(
					"errore.verificafirma.crl_non_aggiornata"));
		} catch (UnsupportedEncodingException ence) {
			errors.add("allegati", new ActionMessage(
					"errore.verificafirma.crl_non_aggiornata"));
		}
		return documento;
	}

	public static String creaFileDaOutputStream(String fileName,
			EditorForm form, HttpServletRequest request, ActionMessages errors,
			Utente utente, int numProt) {
		String pathFileTemporaneo = null;
		String folder = null;
		try {
			folder = ServletUtil.getTempUserPath(request.getSession());
			File tempFile = File.createTempFile("temp_", ".upload", new File(
					folder));
			OutputStream os = new BufferedOutputStream(new FileOutputStream(
					tempFile), FileConstants.BUFFER_SIZE);
			os = EditorUtil.creaPDF(request, form, os, utente, numProt);
			os.close();
			pathFileTemporaneo = tempFile.getAbsolutePath();
		} catch (FileNotFoundException fnfe) {
			logger.error("Error uploading file to:" + folder + " - file name:"
					+ fileName);
			logger.error(fnfe.getMessage(), fnfe);
			errors.add("filePrincipaleUpload", new ActionMessage(
					"upload.error.filenotfound"));
		} catch (IOException ioe) {
			logger.error("Error uploading file to:" + folder + " - file name:"
					+ fileName);
			logger.error(ioe.getMessage(), ioe);
			errors.add("filePrincipaleUpload", new ActionMessage(
					"upload.error.io"));
		} catch (Exception ioe) {
			logger.error("Error uploading file to:" + folder + " - file name:"
					+ fileName);
			logger.error(ioe.getMessage(), ioe);
			errors.add("filePrincipaleUpload", new ActionMessage(
					"upload.error.io"));
		}

		return pathFileTemporaneo;
	}

}
