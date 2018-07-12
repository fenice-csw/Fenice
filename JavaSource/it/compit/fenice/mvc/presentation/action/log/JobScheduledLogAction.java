package it.compit.fenice.mvc.presentation.action.log;

import it.compit.fenice.mvc.bo.ConservazioneBO;
import it.compit.fenice.mvc.business.LogDelegate;
import it.compit.fenice.mvc.presentation.actionform.log.JobScheduledLogForm;
import it.compit.fenice.mvc.presentation.helper.ResponseView;
import it.compit.fenice.restful.GestioneArchiviClient;
import it.compit.fenice.restful.GestioneArchiviData;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.ReportProtocolloDelegate;
import it.finsiel.siged.mvc.vo.log.EventoVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.FileUtil;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class JobScheduledLogAction extends Action {

	static Logger logger = Logger.getLogger(JobScheduledLogAction.class
			.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward actionForward = super.execute(mapping, form, request,response);
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		if (actionForward != null) {
			return actionForward;
		}
		HttpSession session = request.getSession(true);
		LogDelegate delegate = LogDelegate.getInstance();
		JobScheduledLogForm jobForm = (JobScheduledLogForm) form;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		if (form == null) {
			logger.info(" Creating new JobScheduledLogForm");
			form = new JobScheduledLogForm();
			session.setAttribute(mapping.getAttribute(), form);
		}
		if (request.getParameter("inviaRegistro") != null) {
			int eventoId = Integer.valueOf(request
					.getParameter("inviaRegistro"));
			EventoVO vo = delegate.getJobScheduledLogById(eventoId);
			ServletContext context = request.getSession().getServletContext();
			boolean sended=inviaRegistro(request, vo, utente, delegate, context.getRealPath("/"),errors, messages);
			if(sended)
				delegate.aggiornaStatusJobScheduledLog(eventoId, EventoVO.INVIATO, "Secondo tentativo: Registro inviato correttamente");
		}
		jobForm.setLogs(delegate.getJobScheduledLogsByStatus(utente
				.getAreaOrganizzativa().getId(), EventoVO.NON_INVIATO));
		return mapping.findForward("input");
	}

	public boolean inviaRegistro(HttpServletRequest request, EventoVO vo, Utente utente, LogDelegate delegate, String realPath, ActionMessages errors, ActionMessages messages) {
		boolean sended=false;
		try{
			int totalReg = ReportProtocolloDelegate.getInstance().countStampaRegistroReport(utente, "I", vo.getData(), vo.getData(), 0);
			int totalMod = ReportProtocolloDelegate.getInstance().countProtocolliModificatiReport(utente, "I", vo.getData(), vo.getData(), 0);
			ConservazioneBO bo=new ConservazioneBO();
			File directory = FileUtil.createTempDir();
			byte[] zipFile = bo.getReportAndZip(vo.getData(), "I", totalReg,totalMod, realPath, utente, directory);
			byte[] csvFile = FileUtil.leggiFileAsBytes(ConservazioneBO.getFileIndice(directory).getAbsolutePath());
			GestioneArchiviClient client = new GestioneArchiviClient(utente.getAreaOrganizzativa().getGaUsername(), utente.getAreaOrganizzativa().getGaPwd());
			GestioneArchiviData d = new GestioneArchiviData("Versamento registro di protocollo del " + vo.getDataStringForConservazione(),
					ConservazioneBO.getFileIndice(directory).getName(),
					"Registro" + DateUtil.getDataForIndex(vo.getData().getTime())
							+ ".zip", csvFile, zipFile, "11");
			ResponseView view = client.uploadDocument(d);
			if(view.getStatusCode()!=200 && view.getStatusCode()!=204){
				errors.add("d", new ActionMessage("conservazione.registro.upload.error",view.getStatusCode(),view.getMessage()));
				saveErrors(request, messages);
			} else{
				sended=true;
				messages.add("d", new ActionMessage("conservazione.registro.upload.success"));
				saveMessages(request, messages);
			}
		}catch(Exception e){
			logger.error("JobScheduledLogAction:", e);
		}
		return sended;
	}

}
