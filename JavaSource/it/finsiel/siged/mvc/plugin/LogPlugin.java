package it.finsiel.siged.mvc.plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.servlet.ServletException;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

/**
 * The LogPlugIn loads and initializes the log4j properties. Property file name
 * should be set in the strutscx-config.xml. <br>
 * 
 * @author Almaviva sud
 */
public class LogPlugin implements PlugIn {


    private String config;

    /**
     * Initializes the PlugIn.
     * 
     * @param servlet
     * @param config
     */
    public void init(ActionServlet servlet, ModuleConfig config)
            throws ServletException {

        String configPath = getConfig();
        // if the log4j-config parameter is not set, then no point in trying
        if (configPath != null) {
            // if the configPath is an empty string, then no point in trying
            if (configPath.length() >= 1) {

                // check if the config file is XML or a Properties file
                boolean isXMLConfigFile = (configPath.toLowerCase().endsWith(".xml")) ? true
                        : false;

                String contextPath = servlet.getServletContext().getRealPath(
                        "/");
                if (contextPath != null) {
                    // The webapp is deployed directly off the filesystem,
                    // not from a .war file so we *can* do File IO.
                    // This means we can use configureAndWatch() to re-read
                    // the the config file at defined intervals.
                    // Now let's check if the given configPath actually exists.
                    String systemConfigPath = configPath.replace('/',
                            File.separatorChar);
                    File log4jFile = new File(contextPath + systemConfigPath);

                    if (log4jFile.canRead()) {
                        LogLog.debug("Configuring Log4j from File: "
                                + log4jFile.getAbsolutePath());
                        if (isXMLConfigFile) {
                            DOMConfigurator.configure(log4jFile
                                    .getAbsolutePath());
                        } else {
                            PropertyConfigurator.configure(log4jFile
                                    .getAbsolutePath());
                        }
                        log4jFile = null;
                    }
                    LogLog.debug("SIGED - Log4j successfully configured!!");
                } else {
                    // The webapp is deployed from a .war file, not directly
                    // off the file system so we *cannot* do File IO.
                    // Note that we *won't* be able to use configureAndWatch()
                    // here
                    // because that requires an absolute system file path.
                    // Now let's check if the given configPath actually exists.

                    URL log4jURL = null;
                    try {
                        log4jURL = servlet.getServletContext().getResource(
                                "/" + configPath);
                    } catch (MalformedURLException e) {
                        LogLog.error("MalformedURLException");
                    }
                    if (log4jURL != null) {
                        LogLog.debug("Configuring Log4j from URL at path: / "
                                + configPath);
                        if (isXMLConfigFile) {
                            try {
                                DOMConfigurator.configure(log4jURL);
                            } catch (Exception e) {
                                // report errors to server logs
                                LogLog.error(e.getMessage());
                            }
                        } else {
                            Properties log4jProps = new Properties();
                            try {
                                log4jProps.load(log4jURL.openStream());
                                PropertyConfigurator.configure(log4jProps);
                            } catch (Exception e) {
                                // report errors to server logs
                                LogLog.error(e.getMessage());
                            }
                        }
                        LogLog.debug("eprot - Log4j successfully configured!!");
                    } else {
                        // The given configPath does not exist. So, let's just
                        // let Log4j look for the default files
                        // (log4j.properties or log4j.xml) on its own.
                        displayConfigNotFoundMessage();
                    }

                }
            } else {
                LogLog.error("Zero length Log4j config file path given.");
                displayConfigNotFoundMessage();
            }

        } else {
            LogLog.error("Missing log4j-config init parameter missing.");
            displayConfigNotFoundMessage();
        }

    }

    private void displayConfigNotFoundMessage() {
        LogLog
                .warn("No Log4j configuration file found at given path. Falling back.");
    }

    /**
     * Cleans up the Logger information from this context.
     * 
     * @see org.apache.struts.action.PlugIn#destroy()
     */
    public void destroy() {

        // shutdown this webapp's logger repository
        LogLog.debug("Cleaning up Log4j resources for context...");
        LogLog.debug("Shutting down all loggers and appenders...");
        org.apache.log4j.LogManager.shutdown();
        LogLog.debug("Log4j cleaned up!!!");

    }

    /**
     * @return config
     */
    public String getConfig() {
        return config;
    }

    /**
     * @param string
     */
    public void setConfig(String string) {
        config = string;
    }

}
