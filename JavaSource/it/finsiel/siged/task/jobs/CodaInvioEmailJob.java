/*
 * Created on 24-mar-2005
 *
 */
package it.finsiel.siged.task.jobs;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.dao.mail.PecEmailUscita;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.mvc.business.EmailDelegate;
import it.finsiel.siged.mvc.vo.organizzazione.MailConfigVO;

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

public class CodaInvioEmailJob implements Serializable, StatefulJob {

    private static final long serialVersionUID = 1L;
	
	static Logger logger = Logger.getLogger(CodaInvioEmailJob.class.getName());

    public void execute(JobExecutionContext context)
            throws JobExecutionException {

        logger
                .info("Invio messaggi protocollo in ustita tramite PEC in corso per AOO:\n"
                        + context.getJobDetail().getFullName());
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        int aooId = dataMap.getInt("aoo_id");
        int intervallo = dataMap.getInt("intervallo");
		String tempFolder = dataMap.getString("mail.tempfolder");
        MailConfigVO mailConfigVO =Organizzazione.getInstance().getAreaOrganizzativa(aooId).getMailConfig();
        String host = mailConfigVO.getPecSmtp();
        String port = mailConfigVO.getPecSmtpPort();
        String authentication = "true";
        String username = mailConfigVO.getPecUsername();
        String password = mailConfigVO.getPecPwd();
        String emailMittente = mailConfigVO.getPecIndirizzo();
        try {
            PecEmailUscita.inviaProtocolliUscita(EmailDelegate.getInstance(),
                    aooId, host, port, username, password, emailMittente,
                    authentication, tempFolder);
        } catch (Exception e) {
           logger.error("CodaInvioEmailJob: ", e);
        } finally{
        	JobDetail protocolloUscita = new JobDetail("OutEmailAoo_"+ aooId+""+UUID.randomUUID().getLeastSignificantBits(),Constants.SCHEDULER_NAME, CodaInvioEmailJob.class);
			protocolloUscita.getJobDataMap().put("aoo_id",aooId);
			protocolloUscita.getJobDataMap().put("mail.tempfolder",tempFolder);
			protocolloUscita.getJobDataMap().put("intervallo",intervallo);
			SimpleTrigger triggerUscita =new SimpleTrigger("OutTrigger_"+ aooId+""+UUID.randomUUID().getLeastSignificantBits(), Constants.SCHEDULER_NAME,new Date(System.currentTimeMillis()+(intervallo * 60L * 1000L)));
			try {
				context.getScheduler().scheduleJob(protocolloUscita, triggerUscita);
			} catch (SchedulerException e) {
				logger.error("CodaInvioEmailJob:", e);
			}
        }

    }

}
