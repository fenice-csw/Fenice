/*
 * Created on 24-mar-2005
 *
 */
package it.finsiel.siged.mvc.plugin;

import it.finsiel.siged.mvc.business.EmailDelegate;
import it.finsiel.siged.task.jobs.AggiornaCRLJob;

import java.security.Security;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author Almaviva sud
 * 
 */
public class CRLDaemonPlugin implements PlugIn {

    private static Logger logger = Logger.getLogger(EmailDelegate.class
            .getName());

    Scheduler scheduler = null;

    private String cronExp;

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.PlugIn#destroy()
     */
    public void destroy() {
        try {
            logger.debug("shutting down CRLDaemon...");
            scheduler.shutdown(true);
            logger.debug("stopped.");
        } catch (SchedulerException e) {
            logger.error("", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.PlugIn#init(org.apache.struts.action.ActionServlet,
     *      org.apache.struts.config.ModuleConfig)
     */
    public void init(ActionServlet servlet, ModuleConfig config)
            throws ServletException {

        try {
            if (Security.getProvider("BC") == null)
                Security.addProvider(new BouncyCastleProvider());
            SchedulerFactory factory = new StdSchedulerFactory();
            scheduler = factory.getScheduler();

            JobDetail crl = new JobDetail("AggiornaCRL",Scheduler.DEFAULT_GROUP, AggiornaCRLJob.class);
            // ogni giorno alle 4 di mattina
            CronTrigger trigger = new CronTrigger("CRL_Giornaliero",
                    "Giornalieri", "AggiornaCRL", Scheduler.DEFAULT_GROUP,
                    getCronExp());

            scheduler.scheduleJob(crl, trigger);

            scheduler.start();
        } catch (Exception e) {
            logger.error("", e);
            throw new ServletException("CRL Task Manager: can not be started!!");
        }

    }

    public String getCronExp() {
        return cronExp;
    }

    public void setCronExp(String cronExp) {
        this.cronExp = cronExp;
    }
}