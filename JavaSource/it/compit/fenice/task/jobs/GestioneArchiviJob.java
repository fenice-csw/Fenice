package it.compit.fenice.task.jobs;

import it.compit.fenice.mvc.bo.ConservazioneBO;
import it.compit.fenice.mvc.business.LogDelegate;
import it.compit.fenice.mvc.presentation.helper.ResponseView;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.compit.fenice.restful.GestioneArchiviClient;
import it.compit.fenice.restful.GestioneArchiviData;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.RegistroBO;
import it.finsiel.siged.mvc.business.RegistroDelegate;
import it.finsiel.siged.mvc.business.ReportProtocolloDelegate;
import it.finsiel.siged.mvc.vo.log.EventoVO;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.FileUtil;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.StatefulJob;

public class GestioneArchiviJob implements Serializable, StatefulJob {

	private static final long serialVersionUID = 1L;
	
	static Logger logger = Logger.getLogger(GestioneArchiviJob.class.getName());

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		logger.debug("GestioneArchiviJob: Invio Registro Giornaliero a GestioneArchivi.");
		LogDelegate delegate=LogDelegate.getInstance();
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		int aooId = dataMap.getInt("aoo_id");
		String dataSchedulazione = dataMap.getString("data_schedulazione");
		String realPath = dataMap.getString("real_path");
		
		Date yesterday= DateUtil.getYesterdayMidnight();
		String yesterdayStr= DateUtil.getYesterdayDateString();
		
		CaricaVO caricaResponsabile=Organizzazione.getInstance().getCaricaResponsabileUfficioProtocollo();
		Utente utente=Organizzazione.getInstance().getUtente(caricaResponsabile.getUtenteId());
		Map<Integer,RegistroVO> registri = RegistroDelegate.getInstance().getRegistriUtente(caricaResponsabile.getUtenteId());
		utente.setRegistri(registri);
		utente.setRegistroInUso(RegistroBO.getRegistroUfficialeId(registri.values()));
		utente.setRegistroUfficialeId(RegistroBO.getRegistroUfficialeId(registri.values()));
		utente.setRegistroPostaInterna(RegistroBO.getRegistroPostaInternaId(registri.values()));
		try {
			if(!delegate.isStatusJobScheduledLogPresent(yesterday)){
			int totalReg=ReportProtocolloDelegate.getInstance().countStampaRegistroReport(utente, "I",yesterday,yesterday, 0);
			int totalMod= ReportProtocolloDelegate.getInstance().countProtocolliModificatiReport(utente, "I",
						yesterday, yesterday, 0);
			if (totalReg > 0) {
				File directory=FileUtil.createTempDir();
				logger.debug("GestioneArchiviJob: directory "+directory.getAbsolutePath());
				ConservazioneBO bo=new ConservazioneBO();
				byte[] zipFile = bo.getReportAndZip(yesterday, "I", totalReg, totalMod, realPath, utente, directory);
				byte[] csvFile= FileUtil.leggiFileAsBytes(ConservazioneBO.getFileIndice(directory).getAbsolutePath());
				GestioneArchiviClient client=new GestioneArchiviClient(utente.getAreaOrganizzativa().getGaUsername(), utente.getAreaOrganizzativa().getGaPwd());

				GestioneArchiviData d=new GestioneArchiviData("Versamento registro di protocollo del "+yesterdayStr, ConservazioneBO.getFileIndice(directory).getName(), "Registro"+DateUtil.getDataForIndex(yesterday.getTime())+".zip", csvFile, zipFile, "11");
				
				ResponseView view=client.uploadDocument(d);
				if(view.getStatusCode()!=200 && view.getStatusCode()!=204){
					EventoVO vo=new EventoVO(EventoVO.NON_INVIATO, "Errori nell'invio del file", aooId,yesterday);
					delegate.newJobScheduledLog(utente, vo);
					logger.error("GestioneArchiviJob: Errore nell'invio del file di registro del "+yesterdayStr);
				} else{
					EventoVO vo=new EventoVO(EventoVO.INVIATO, "Registro inviato correttamente", aooId,yesterday);
					delegate.newJobScheduledLog(utente, vo);
					logger.debug("GestioneArchiviJob: Invio del file di registro del "+yesterdayStr+" completato");

				}
				
			}else{
				EventoVO vo=new EventoVO(EventoVO.NON_NECESSARIO, "Registro vuoto", aooId,yesterday);
				delegate.newJobScheduledLog(utente, vo);
				}
			}else{
				logger.debug("GestioneArchiviJob: Log dell'invio gia presente.");
			}
			
		}catch (Exception e) {
			EventoVO vo=new EventoVO(EventoVO.NON_INVIATO, "Errori nell'invio del file", aooId,yesterday);
			delegate.newJobScheduledLog(utente, vo);
			logger.error("GestioneArchiviJob:", e);
		}finally{
			JobDetail gestioneArchivi = new JobDetail("gestioneArchivi_"+ aooId+""+UUID.randomUUID().getLeastSignificantBits(),Constants.SCHEDULER_NAME, GestioneArchiviJob.class);
			gestioneArchivi.getJobDataMap().put("aoo_id",aooId);
			gestioneArchivi.getJobDataMap().put("data_schedulazione",DateUtil.addDaysToDataOraString(dataSchedulazione, 1));
			gestioneArchivi.getJobDataMap().put("real_path", realPath);
			SimpleTrigger triggerGestioneArchivi =new SimpleTrigger("triggerGestioneArchivi_"+ aooId+""+UUID.randomUUID().getLeastSignificantBits(), Constants.SCHEDULER_NAME,DateUtil.getDataOra(dataSchedulazione));
			try {
				context.getScheduler().scheduleJob(gestioneArchivi, triggerGestioneArchivi);
			} catch (SchedulerException e) {
				logger.error("GestioneArchiviJob:", e);
			}
		}
	}

}
