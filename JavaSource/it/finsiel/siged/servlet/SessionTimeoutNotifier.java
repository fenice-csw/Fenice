package it.finsiel.siged.servlet;

import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.util.FileUtil;

import java.io.File;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Logger;

public class SessionTimeoutNotifier implements HttpSessionBindingListener {
    static Logger logger = Logger.getLogger(SessionTimeoutNotifier.class
            .getName());

    private String tempFolderPath = null;

    private String username = null;

    private String tempDocumentPath = null;

    private boolean pathCreated = false;

    public SessionTimeoutNotifier(String path, String username,
            HttpSession session) {
        this.tempFolderPath = path;
        this.tempDocumentPath = tempFolderPath + session.getId();
        this.username = username;
    }

    /*
     * The object have been bound into the session
     */
    public void valueBound(HttpSessionBindingEvent event) {
        try {
            File f = new File(tempDocumentPath);
            boolean created = f.mkdirs();
            pathCreated = created;
            logger.debug("Session " + event.getSession().getId() + " created:"
                    + pathCreated);
        } catch (SecurityException se) {
            logger.error("SessionTimeoutNotifier.valueBound();", se);
        }
    }

    public void valueUnbound(HttpSessionBindingEvent event) {
        try {
            Organizzazione org = Organizzazione.getInstance();
            if (username != null) {
                Utente ute = org.getUtente(username);
                org.disconnettiUtente(ute);
                logger.info("Disconnesso l'utente: " + username);
                if (tempDocumentPath != null) {
                    FileUtil.deltree(tempDocumentPath);
                    logger.debug("Eliminata directory temporanea: "
                            + tempDocumentPath);
                }
            } else {
                logger.info("Impossibile Disconnettere l'utente: " + username);
            }
        } catch (SecurityException se) {
            logger.error("SessionTimeoutNotifier.valueUnbound();", se);
        }
    }

    /*
     * Return true if the temporary path for document filtering has ben created.
     */
    public boolean isBounded() {
        return pathCreated;
    }

    /*
     * Return a String that point to the Temporary Path in the FileSystem, used
     * by the filter to store temporary HTML version of user's documents
     * filtered in real time
     */
    public String getTempPath() {
        return tempDocumentPath;
    }
}