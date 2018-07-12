package it.finsiel.siged.task.jobs;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.dao.mail.PecEmailIngresso;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.mvc.vo.organizzazione.MailConfigVO;
import it.finsiel.siged.util.FileUtil;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.StatefulJob;

public class FetchEmailJob implements Serializable, StatefulJob {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(FetchEmailJob.class.getName());
	
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("Controllo della casella email per protocollo in ingresso su PEC in corso per AOO:\n"+ context.getJobDetail().getFullName());
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		int aooId = dataMap.getInt("aoo_id");
		int intervallo = dataMap.getInt("intervallo");
		String tempFolder = dataMap.getString("mail.tempfolder");
		MailConfigVO mailVO = Organizzazione.getInstance().getAreaOrganizzativa(aooId).getMailConfig();
		String hostPec = mailVO.getPecPop3();
		String portPec = mailVO.getPecSslPort();
		String authenticationPec = "false";
		String usernamePec = mailVO.getPecUsername();
		String passwordPec = mailVO.getPecPwd();
		Date dateControlPec=mailVO.getDataUltimaPecRicevuta();
		FileUtil.deltree(tempFolder);
		File tmp = new File(tempFolder);
		tmp.mkdirs();
		try {
			PecEmailIngresso.preparaProtocolliMessaggiIngresso(aooId, hostPec,
					portPec, usernamePec, passwordPec, authenticationPec,dateControlPec,
					tempFolder, intervallo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("FetchEmailJob:", e);
		} finally{
			JobDetail protocolloIngresso = new JobDetail("InEmailAoo_"+ aooId+""+UUID.randomUUID().getLeastSignificantBits(),Constants.SCHEDULER_NAME, FetchEmailJob.class);
			protocolloIngresso.getJobDataMap().put("aoo_id",aooId);
			protocolloIngresso.getJobDataMap().put("mail.tempfolder",tempFolder);
			protocolloIngresso.getJobDataMap().put("intervallo",intervallo);
			SimpleTrigger triggerIngresso =new SimpleTrigger("InTrigger_"+ aooId+""+UUID.randomUUID().getLeastSignificantBits(), Constants.SCHEDULER_NAME,new Date(System.currentTimeMillis()+(intervallo * 60L * 1000L)));
			try {
				context.getScheduler().scheduleJob(protocolloIngresso, triggerIngresso);
			} catch (SchedulerException e) {
				e.printStackTrace();
				logger.error("FetchEmailJob:", e);
			}
		}
		
	}
}
