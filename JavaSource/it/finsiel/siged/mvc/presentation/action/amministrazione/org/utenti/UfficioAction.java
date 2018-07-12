package it.finsiel.siged.mvc.presentation.action.amministrazione.org.utenti;

import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.compit.fenice.mvc.vo.organizzazione.StoricoOrganigrammaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.AlberoUfficiBO;
import it.finsiel.siged.mvc.bo.OrganizzazioneBO;
import it.finsiel.siged.mvc.business.AreaOrganizzativaDelegate;
import it.finsiel.siged.mvc.business.UfficioDelegate;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.org.utenti.UfficioForm;
import it.finsiel.siged.mvc.presentation.helper.StatisticheView;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.report.protocollo.CommonReportDS;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
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

/**
 * Implementation of <strong>Action </strong> to create a new E-Photo Utente.
 * 
 * @author Almaviva sud.
 * 
 */

public class UfficioAction extends Action {

	// ----------------------------------------------------- Instance Variables

	/**
	 * The <code>Log</code> instance for this application.
	 */
	static Logger logger = Logger.getLogger(UfficioAction.class.getName());

	// --------------------------------------------------------- Public Methods

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		Organizzazione org=Organizzazione.getInstance();
		int aooId = utente.getAreaOrganizzativa().getId();
		UfficioForm ufficioForm = (UfficioForm) form;
		UfficioVO ufficioVO = new UfficioVO();
		
		if (form == null) {
			form = new UfficioForm();
			request.setAttribute(mapping.getAttribute(), form);
		}

		if (ufficioForm.getId() == 0) {
			impostaUfficioUtenti(org, utente, ufficioForm, true, aooId);
		}

		if (request.getParameter("btnCaricaAction") != null) {
			session.setAttribute("ufficioId", ufficioForm.getId());
			return (mapping.findForward("carica"));
		}
		if (request.getParameter("impostaUfficioAction") != null) {
			ufficioForm.setUfficioCorrenteId(ufficioForm
					.getUfficioSelezionatoId());
			impostaUfficioUtenti(org, utente, ufficioForm, true, aooId);
			ufficioForm.setParentId(ufficioForm.getUfficioCorrente()
					.getParentId());
		} else if (request.getParameter("ufficioPrecedenteAction") != null) {
			ufficioForm.setUfficioSelezionatoId(0);
			ufficioForm.setUfficioCorrenteId(ufficioForm.getUfficioCorrente()
					.getParentId());
			impostaUfficioUtenti(org, utente, ufficioForm, true, aooId);
			ufficioForm.setParentId(ufficioForm.getUfficioCorrente()
					.getParentId());
		} else if (request.getParameter("btnNuovo") != null) {
			ufficioForm.setAooId(utente.getRegistroVOInUso().getAooId());
			ufficioForm.inizializzaForm();
			ufficioForm.setParentId(ufficioForm.getUfficioCorrenteId());
			request.setAttribute(mapping.getAttribute(), ufficioForm);
			return mapping.findForward("aggiorna");
		} else if (request.getParameter("btnSposta") != null) {
			ufficioForm.setUfficioDaSpostare(org.getUfficio(ufficioForm.getUfficioCorrenteId()));
			request.setAttribute(mapping.getAttribute(), ufficioForm);
			ufficioForm.setUfficioCorrenteId(ufficioForm.getParentId());
			impostaUfficioUtenti(org, utente, ufficioForm, true, aooId);
			return mapping.findForward("sposta");
		} else if (request.getParameter("btnConfermaSposta") != null) {
			if (ufficioForm.getUfficioCorrenteId() != ufficioForm
					.getUfficioDaSpostare().getValueObject().getId()) {
				int newParentId = ufficioForm.getUfficioCorrenteId();
				//int oldParentId = ufficioForm.getParentId();
				int oldParentId = ufficioForm.getUfficioDaSpostare().getValueObject().getParentId();
				ufficioForm.setUfficioCorrenteId(ufficioForm
						.getUfficioDaSpostare().getValueObject().getId());
				caricaDatiNelForm(org,ufficioForm, ufficioForm
						.getUfficioCorrenteId());
				ufficioForm.setAooId(utente.getRegistroVOInUso().getAooId());
				ufficioForm.setReferentiId(UfficioDelegate.getInstance()
						.getReferentiByUfficio(
								ufficioForm.getUfficioCorrenteId()));
				ufficioForm.setParentId(newParentId);
				caricaDatiNelVO(org, ufficioVO, ufficioForm, utente);
				int returnValue = spostaUfficio(org,ufficioVO, ufficioForm
						.getReferentiId(), oldParentId, utente);
				if (returnValue != ReturnValues.SAVED) {
					if (returnValue == ReturnValues.FOUND) {
						errors.add("record_non_inseribile", new ActionMessage(
								"record_non_modificabile", "l'Ufficio",
								" - descrizione già utilizzata "));
					} else {
						errors.add("generale", new ActionMessage(
								"errore_nel_salvataggio"));
					}
				} else {
					ufficioForm.setUfficioCorrenteId(ufficioForm.getParentId());
					impostaUfficioUtenti(org, utente, ufficioForm, true, aooId);
					return (mapping.findForward("annulla"));
				}
			} else
				errors.add("ufficio.no_spostamento", new ActionMessage(
						"ufficio.no_spostamento"));
			saveErrors(request, errors);
			return (mapping.findForward("input"));
		} else if (request.getParameter("btnAnnullaSposta") != null) {
			return mapping.findForward("annulla");
		} else if (request.getParameter("btnModifica") != null) {
			caricaDatiNelForm(org,ufficioForm, ufficioForm.getUfficioCorrenteId());
			ufficioForm.setAooId(utente.getRegistroVOInUso().getAooId());
			ufficioForm.setReferentiId(UfficioDelegate.getInstance()
					.getReferentiByUfficio(ufficioForm.getUfficioCorrenteId()));
			request.setAttribute(mapping.getAttribute(), ufficioForm);
			return mapping.findForward("aggiorna");
		} else if (request.getParameter("indietroCaricaAction") != null) {
			int ufficioId = 1;
			if (session.getAttribute("ufficioId") != null)
				ufficioId = (Integer) session.getAttribute("ufficioId");
			if (request.getParameter("ufficioId") != null)
				ufficioId = Integer.valueOf(request.getParameter("ufficioId"));
			caricaDatiNelForm(org,ufficioForm, ufficioId);
			ufficioForm.setAooId(utente.getRegistroVOInUso().getAooId());
			ufficioForm.setReferentiId(UfficioDelegate.getInstance()
					.getReferentiByUfficio(ufficioForm.getId()));
			ufficioForm.setStoricoSaved(true);
			request.setAttribute(mapping.getAttribute(), ufficioForm);
			session.removeAttribute("ufficioId");
			return mapping.findForward("aggiorna");
		} else if (request.getParameter("btnCancella") != null) {
			UfficioDelegate td = UfficioDelegate.getInstance();
			if (!td.cancellaUfficio(ufficioForm.getUfficioCorrenteId())) {
				errors.add("ufficioNonCancellabile", new ActionMessage(
						"ufficio.non_cancellabile", "", ""));
				saveErrors(request, errors);
			} else {
				removeUfficio(org,ufficioForm.getUfficioCorrenteId(),utente);
				ufficioForm.setUfficioCorrenteId(ufficioForm.getParentId());
				impostaUfficioUtenti(org, utente, ufficioForm, true, aooId);
				messages.add("ufficioCancellato", new ActionMessage(
						"operazione_ok"));
				saveMessages(request, messages);
			}

			return (mapping.findForward("input"));

		} else if (request.getParameter("btnAnnulla") != null) {
			impostaUfficioUtenti(org, utente, ufficioForm, true, aooId);
		} else if (request.getParameter("btnDisattiva") != null) {
			boolean updated = UfficioDelegate.getInstance().setUfficioAttivo(ufficioForm.getId(), false);
			if (updated) {
				ufficioForm.setStoricoSaved(true);
				org.getUfficio(ufficioForm.getId()).getValueObject().setAttivo(false);
				ufficioForm.setAttivo(false);
				String desc = "Disattivato ufficio "+ org.getUfficio(ufficioForm.getId()).getPath();
				addStorico(utente, desc, org);
			}
			return mapping.findForward("aggiorna");
		} else if (request.getParameter("btnAttiva") != null) {
			boolean updated = UfficioDelegate.getInstance().setUfficioAttivo(ufficioForm.getId(), true);
			if (updated) {
				ufficioForm.setStoricoSaved(true);
				org.getUfficio(ufficioForm.getId()).getValueObject().setAttivo(true);
				ufficioForm.setAttivo(true);
				String desc = "Attivato ufficio "+ org.getUfficio(ufficioForm.getId()).getPath();
				addStorico(utente, desc, org);
			}
			return mapping.findForward("aggiorna");
		} else if (request.getParameter("btnDisattivaUfficioProtocollo") != null) {
			boolean removed=UfficioDelegate.getInstance().removeUfficioProtocollo();
			if(removed){
				org.removeUfficioProtocollo();
				ufficioForm.setStoricoSaved(true);
				String desc = org.getUfficio(ufficioForm.getId()).getPath()+" non più ufficio di protocollo";
				addStorico(utente, desc, org);
				ufficioForm.setUfficioProtocollo(false);
			} 
			return mapping.findForward("aggiorna");
		} else if (request.getParameter("btnAttivaUfficioProtocollo") != null) {
			boolean removed=UfficioDelegate.getInstance().removeUfficioProtocollo();
			if(removed){
				org.removeUfficioProtocollo();
				ufficioForm.setStoricoSaved(true);
				String desc="Rimosso responsabile dell'ufficio protocollo";
				addStorico(utente, desc, Organizzazione.getInstance());
				ufficioVO=UfficioDelegate.getInstance().setUfficioProtocollo(ufficioForm.getId());
				if(ufficioVO.getReturnValue()==ReturnValues.SAVED){
					desc = Organizzazione.getInstance().getUfficio(ufficioForm.getId()).getPath()+" diventa ufficio di protocollo";
					addStorico(utente, desc, Organizzazione.getInstance());
					ufficioForm.setUfficioProtocollo(true);
					org.getUfficio(ufficioForm.getId()).getValueObject().setUfficioProtocollo(true);
				}
				return mapping.findForward("aggiorna");
			}
		} else if (request.getParameter("btnSalva") != null) {
			caricaDatiNelVO(org, ufficioVO, ufficioForm, utente);
			int returnValue = aggiornaUfficio(org,ufficioVO, ufficioForm.getReferentiId(), utente,ufficioForm.isStoricoSaved());
			if (returnValue != ReturnValues.SAVED) {
				if (returnValue == ReturnValues.FOUND) {
					errors.add("record_non_inseribile", new ActionMessage(
							"record_non_modificabile", "l'Ufficio",
							" - descrizione già utilizzata "));
				} else {
					errors.add("generale", new ActionMessage(
							"errore_nel_salvataggio"));
				}
			} else {
				ufficioForm.setUfficioCorrenteId(ufficioForm.getParentId());
				impostaUfficioUtenti(org, utente, ufficioForm, true, aooId);
			}
		} else if (request.getParameter("selectResponsabileAction") != null) {
			ufficioForm.setUtenteResponsabileSelezionatoId(ufficioForm
					.getUtenteResponsabileId());
			AreaOrganizzativaDelegate.getInstance().salvaUtenteResponsabile(
					aooId, ufficioForm.getUtenteResponsabile().getId());
		} else if (request.getParameter("removeResponsabileAction") != null) {
			ufficioForm.setUtenteSelezionatoId(0);

		} else if (request.getParameter("btnStoria") != null) {
			ufficioForm.setStoricoOrganigramma(UfficioDelegate.getInstance()
					.getStoricoOrganigrammaCollection(aooId));
			return mapping.findForward("storia");
		} else if (request.getParameter("btnIndietroStoria") != null) {
			return mapping.findForward("input");
		} else if (request.getParameter("stampaSelezionato") != null) {
			Integer id = new Integer(Integer.parseInt(request.getParameter("stampaSelezionato")));
			StoricoOrganigrammaVO vo = UfficioDelegate.getInstance().getStoricoOrganigramma(id);
			stampaReport(org, vo, response);
			return null;
		} else {
			ufficioForm.inizializzaForm();
			impostaUfficioUtenti(org, utente, ufficioForm, true, aooId);
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return mapping.findForward("aggiorna");
		}
		return (mapping.findForward("input"));
	}

	private static void impostaUfficioUtenti(Organizzazione org, Utente utente,
			UfficioForm ufficioForm, boolean bool, int aooId) {
		AlberoUfficiBO.impostaUfficioUtenti(utente, ufficioForm, bool);
		if (ufficioForm.getParentId() == 0) {
			ufficioForm.setUtenteResponsabileModificabile(true);
			ufficioForm.setUtentiResponsabile(AreaOrganizzativaDelegate
					.getInstance().getUtenti(aooId));
		} else
			ufficioForm.setUtenteResponsabileModificabile(false);
		if (org.getAreaOrganizzativa(aooId)
				.getUtenteResponsabileId() != 0)
			ufficioForm.setUtenteResponsabileSelezionatoId(Organizzazione
					.getInstance().getAreaOrganizzativa(aooId)
					.getUtenteResponsabileId());

	}

	public void caricaDatiNelVO(Organizzazione org, UfficioVO vo, UfficioForm form, Utente utente) {
		vo.setId(form.getId());
		vo.setDescription(form.getDescription());
		vo.setPiano(form.getPiano());
		vo.setStanza(form.getStanza());
		vo.setTelefono(form.getTelefono());
		vo.setFax(form.getFax());
		vo.setEmail(form.getEmail());
		vo.setEmailUsername(form.getEmailUsername());
		vo.setEmailPassword(form.getEmailPassword());
		vo.setAttivo(form.getAttivo() != null ? form.getAttivo().booleanValue()
				: false);
		vo.setUfficioProtocollo(form.getUfficioProtocollo() != null ? form.getUfficioProtocollo().booleanValue()
				: false);
		vo.setTipo(form.getTipo());
		vo
				.setAccettazioneAutomatica(form.getAccettazioneAutomatica() != null ? form
						.getAccettazioneAutomatica().booleanValue()
						: false);
		vo.setAooId(utente.getUfficioVOInUso().getAooId());
		vo.setParentId(form.getParentId());
		vo.setCaricaDirigenteId(form.getCaricaDirigenteId());
		if(vo.isUfficioProtocollo())
			vo.setCaricaResponsabileUfficioProtocolloId(form.getCaricaResponsabileUfficioProtocolloId());
		if (form.getId() > 0) {
			vo.setRowCreatedUser(utente.getValueObject().getUsername());
		}
		vo.setRowUpdatedUser(utente.getValueObject().getUsername());
	}

	public void caricaDatiNelForm(Organizzazione org, UfficioForm form, int ufficioId) {
		UfficioVO ufficioVO = org.getUfficio(ufficioId).getValueObject();
		form.setId(ufficioId);
		form.setParentId(ufficioVO.getParentId());
		form.setDescription(ufficioVO.getDescription());
		form.setAttivo(Boolean.valueOf(ufficioVO.isAttivo()));
		form.setUfficioProtocollo(Boolean.valueOf(ufficioVO.isUfficioProtocollo()));
		form.setAccettazioneAutomatica(Boolean.valueOf(ufficioVO
				.isAccettazioneAutomatica()));
		form.setTipo(ufficioVO.getTipo());
		form.setPiano(ufficioVO.getPiano());
		form.setStanza(ufficioVO.getStanza());
		form.setTelefono(ufficioVO.getTelefono());
		form.setFax(ufficioVO.getFax());
		form.setEmail(ufficioVO.getEmail());
		form.setEmailUsername(ufficioVO.getEmailUsername());
		form.setEmailPassword(ufficioVO.getEmailPassword());
		form.setCaricaDirigenteId(ufficioVO.getCaricaDirigenteId());
		if(form.getUfficioProtocollo() && org.getCaricaResponsabileUfficioProtocollo()!=null)
			form.setCaricaResponsabileUfficioProtocolloId(org.getCaricaResponsabileUfficioProtocollo().getCaricaId());
		UfficioDelegate td = UfficioDelegate.getInstance();
		form.setDipendentiUfficio(td.getCaricheByUfficio(ufficioId));
		form.setVociTitolario(td.getTitolarioByUfficio(ufficioId));
		form.setStoricoSaved(false);
	}
	

	public int aggiornaUfficio(Organizzazione org, UfficioVO ufficioVO, String[] referenti,
			Utente utente,Boolean storicoSaved) {
		int ufficioId = ufficioVO.getId().intValue();
		ufficioVO = UfficioDelegate.getInstance().salvaUfficio(ufficioVO,
				referenti);
		if (ufficioVO.getReturnValue() == ReturnValues.SAVED) {
			Ufficio uff;
			String desc = null;
			if (ufficioId == 0) {
				uff = new Ufficio(ufficioVO);
				Ufficio uffPadre = org.getUfficio(ufficioVO.getParentId());
				uff.setUfficioDiAppartenenza(uffPadre);
				desc = "Creato ufficio " + uff.getPath();
			} else {
				uff = org.getUfficio(ufficioId);
				uff.setValueObject(ufficioVO);
				desc = "Modificato ufficio " + uff.getPath();
			}
			uff.removeReferenti();
			int i = 0;

			CaricaDelegate delegate = CaricaDelegate.getInstance();
			if (referenti != null) {
				while (i < referenti.length) {
					int utenteId = Integer.parseInt(referenti[i]);
					CaricaVO carVO = delegate.getCaricaByUtenteAndUfficio(
							utenteId, ufficioId);
					uff.addCaricaReferente(carVO);
					i = i + 1;
				}
			}
			org.addUfficio(uff);
			if(!storicoSaved)
				addStorico(utente, desc, org);
		}
		return ufficioVO.getReturnValue();
	}
	
	public void addStorico(Utente utente, String descrizioneModifica,Organizzazione org){
		StoricoOrganigrammaVO storgVO = new StoricoOrganigrammaVO();
		OrganizzazioneBO.setOrganigramma(org, storgVO, utente, descrizioneModifica);
		UfficioDelegate.getInstance().salvaStoricoOrganigramma(storgVO);
	}
	
	public int spostaUfficio(Organizzazione org, UfficioVO ufficioVO, String[] referenti,
			int oldParentId, Utente utente) {
		int ufficioId = ufficioVO.getId().intValue();
		ufficioVO = UfficioDelegate.getInstance().salvaUfficio(ufficioVO,
				referenti);
		if (ufficioVO.getReturnValue() == ReturnValues.SAVED) {
			org.getUfficio(oldParentId).removeUfficioDipendente(ufficioId);
			Ufficio uff = org.getUfficio(ufficioId);
			uff.setValueObject(ufficioVO);
			uff.setUfficioDiAppartenenza(org
					.getUfficio(ufficioVO.getParentId()));
			org.getUfficio(ufficioVO.getParentId()).addUfficioDipendente(uff);
			org.addUfficio(uff);
			String desc = "Spostato Ufficio "
					+ uff.getValueObject().getDescription() + " da "
					+ org.getUfficio(oldParentId).getPath() + " a "
					+ uff.getUfficioDiAppartenenza().getPath();
			addStorico(utente, desc, org);

		}
		return ufficioVO.getReturnValue();
	}

	private void removeUfficio(Organizzazione org, int ufficioId,Utente utente) {
		Ufficio uff = org.getUfficio(ufficioId);
		String desc="Cancellato ufficio "+uff.getPath();
		org.removeUfficio(new Integer(ufficioId));
		Ufficio uffPadre = org.getUfficio(uff.getValueObject().getParentId());
		uffPadre.removeUfficioDipendente(new Integer(ufficioId));
		addStorico(utente, desc, org);

	}

	public void stampaReport(Organizzazione org, StoricoOrganigrammaVO vo,
			HttpServletResponse response) throws IOException, ServletException {
		ServletContext context = getServlet().getServletContext();
		OutputStream os = response.getOutputStream();
		try {
			File reportFile = new File(context.getRealPath("/")
					+ FileConstants.STAMPA_ORGANIZZAZIONE_REPORT_TEMPLATE);
			JasperDesign jasperDesign = JRXmlLoader.load(context
					.getRealPath("/")
					+ FileConstants.STAMPA_ORGANIZZAZIONE_REPORT_TEMPLATE);
			JasperReport jasperReport = JasperCompileManager
					.compileReport(jasperDesign);

			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("ReportTitle", "Organigramma del "
					+ vo.getDataStorico());
			parameters.put("ReportSubTitle", org.getAreaOrganizzativa(
					vo.getAooId()).getValueObject().getDescription());
			parameters.put("BaseDir", reportFile.getParentFile());

			CommonReportDS ds = new CommonReportDS(vo.getOrganigramma(),
					StatisticheView.class);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);

			response.setHeader("Content-Disposition",
					"attachment;filename=Organigramma " + vo.getDataStorico()
							+ ".pdf");
			response.setHeader("Cache-control", "");
			response.setContentType("application/pdf");
			JRPdfExporter exporter = new JRPdfExporter();
			exporter
					.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
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
