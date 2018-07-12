package it.compit.fenice.task.jobs;

import it.compit.fenice.dao.mail.EmailIngresso;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
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

public class FetchEmailUfficioJob implements Serializable, StatefulJob {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(FetchEmailUfficioJob.class
			.getName());

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		int aooId = dataMap.getInt("aoo_id");
		String tempFolder = dataMap.getString("mail.tempfolder");
		int intervallo = dataMap.getInt("intervallo");
		MailConfigVO mailConfigVO =Organizzazione.getInstance().getAreaOrganizzativa(aooId).getMailConfig();

		String hostPn = mailConfigVO.getPnPop3();
		String portPn = mailConfigVO.getPnSslPort();
		
		String authentication = "false";
		FileUtil.deltree(tempFolder);
		File tmp = new File(tempFolder);
		tmp.mkdirs();
		try {
			for (Object o : Organizzazione.getInstance().getUffici()) {
				Ufficio uff = (Ufficio) o;
				if (uff.getValueObject().getEmailUsername() != null
						&&  uff.getValueObject().getEmailPassword() != null
						&& !uff.getValueObject().getEmailUsername().trim().equals("")
						&& !uff.getValueObject().getEmailPassword().trim().equals(""))
					EmailIngresso.preparaProtocolliMessaggiIngresso(aooId,hostPn, portPn, uff, authentication,tempFolder);
			}
		} catch (Exception e) {
			logger.error("FetchEmailUfficioJob:", e);
		} finally{
			JobDetail protocolliIngressoEmailUfficio = new JobDetail("InEmailUffAoo_"+aooId+""+UUID.randomUUID().getLeastSignificantBits(),Constants.SCHEDULER_NAME, FetchEmailUfficioJob.class);
			protocolliIngressoEmailUfficio.getJobDataMap().put("aoo_id", aooId);
			protocolliIngressoEmailUfficio.getJobDataMap().put("mail.tempfolder", tempFolder);
			protocolliIngressoEmailUfficio.getJobDataMap().put("intervallo",intervallo);
			SimpleTrigger triggerIngresso =new SimpleTrigger("InEmailUffTrigger_"+ aooId+""+UUID.randomUUID().getLeastSignificantBits(), Constants.SCHEDULER_NAME,new Date(System.currentTimeMillis()+(intervallo * 60L * 1000L)));
			try {
				context.getScheduler().scheduleJob(protocolliIngressoEmailUfficio, triggerIngresso);
			} catch (SchedulerException e) {
				logger.error("FetchEmailUfficioJob:", e);
			}
		}
	}
}
