/*
 * Created on 24-mar-2005
 *
 */
package it.finsiel.siged.task.jobs;

import it.finsiel.siged.constant.EmailConstants;
import it.finsiel.siged.mvc.business.EmailDelegate;
import it.finsiel.siged.mvc.business.FirmaDigitaleDelegate;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

/**
 * @author Almaviva sud
 * 
 */
public class AggiornaCRLJob implements Serializable, StatefulJob {

    static Logger logger = Logger.getLogger(AggiornaCRLJob.class.getName());

    /*
     * (non-Javadoc)
     * 
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        logger.info("Aggiornamento delle CRL in corso...");
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        int aooId = dataMap.getInt("aoo_id");
        try {
            ActionMessages errors = FirmaDigitaleDelegate.getInstance().aggiornaListaCertificatiRevocati();
            Iterator it = errors.get();
            while (it.hasNext()) {
                ActionMessage m = (ActionMessage) it.next();
                //EmailDelegate.getInstance().salvaEmailLog("CRL","Aggiornamento", m.toString(), EmailConstants.LOG_CRL,aooId);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }
}
