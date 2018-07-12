package it.finsiel.siged.mvc.business;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.NomiTabelle;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.constant.ProtocolConstants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.dao.digitalsignature.VerificaFirma;
import it.finsiel.siged.exception.CRLNonAggiornataException;
import it.finsiel.siged.exception.ConversionException;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.integration.FirmaDigitaleDAO;
import it.finsiel.siged.mvc.vo.firma.CaVO;
import it.finsiel.siged.mvc.vo.firma.CrlUrlVO;
import it.finsiel.siged.rdb.JDBCManager;
import it.finsiel.siged.util.HttpUtil;
import it.finsiel.siged.util.ldap.LdapUtil;

import java.io.IOException;
import java.security.cert.X509CRL;
import java.sql.Connection;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class FirmaDigitaleDelegate implements ComponentStatus {

    private static Logger logger = Logger.getLogger(FirmaDigitaleDelegate.class
            .getName());

    private int status;

    private FirmaDigitaleDAO dao = null;

    //private ServletConfig config = null;

    private static FirmaDigitaleDelegate delegate = null;

    private FirmaDigitaleDelegate() {
        try {
            if (dao == null) {
                dao = (FirmaDigitaleDAO) DAOFactory
                        .getDAO(Constants.FIRMA_DIGITALE_DAO_CLASS);
                logger.debug("FirmaDigitaleDAO instantiated:"
                        + Constants.FIRMA_DIGITALE_DAO_CLASS);
                status = STATUS_OK;
            }
        } catch (Exception e) {
            status = STATUS_ERROR;
            logger.error("", e);
        }

    }

    public static FirmaDigitaleDelegate getInstance() {
        if (delegate == null)
            delegate = new FirmaDigitaleDelegate();
        return delegate;
    }

    public static String getIdentifier() {
        return Constants.FIRMA_DIGITALE_DELEGATE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.finsiel.siged.mvc.business.ComponentStatus#getStatus()
     */
    public int getStatus() {
        return status;
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.finsiel.siged.mvc.business.ComponentStatus#setStatus(int)
     */
    public void setStatus(int s) {
        this.status = s;
    }

    // fine metodi interfaccia

    public Collection<CaVO> getAllCA() throws DataException {
        return dao.getAllCA();
    }

    public CaVO getCaById(int id) throws DataException {
        return dao.getCA(id);
    }

    public Date getDataSeRevocato(String issuerDN, String serialNumber)
            throws DataException, CRLNonAggiornataException {
        java.sql.Date revoca = dao.getDataSeRevocato(issuerDN, serialNumber);
        if (revoca != null)
            return new java.util.Date(revoca.getTime());
        else
            return null;
    }

    public void salvaCertificateAuthority(CaVO ca) throws DataException {
        JDBCManager jdbcMan = null;
        Connection connection = null;
        try {
            jdbcMan = new JDBCManager();
            connection = jdbcMan.getConnection();
            connection.setAutoCommit(false);

            salvaCertificateAuthority(connection, ca);

            connection.commit();
        } catch (Exception de) {
            jdbcMan.rollback(connection);
            throw new DataException(de.getMessage());
        } finally {
            jdbcMan.close(connection);
        }
    }

    public void salvaCertificateAuthority(Connection connection, CaVO ca)
            throws DataException {
        CaVO oldCa = dao.getCAByCN(connection, ca.getIssuerCN());
        boolean aggiornaCa = oldCa.getReturnValue() == ReturnValues.FOUND;
        if (aggiornaCa) {
            // elimina dati presenti
            dao.cancellaCA(connection, oldCa.getId().intValue());
        }
        // inserisci
        ca.setId(IdentificativiDelegate.getInstance().getNextId(connection,
                NomiTabelle.CA_LISTA));
        dao.salvaCertificateAuthotority(connection, ca);
        Iterator<CrlUrlVO> iter = ca.getCrlUrls().values().iterator();
        while (iter.hasNext()) {
            CrlUrlVO vo = (CrlUrlVO) iter.next();
            vo.setCaId(ca.getId().intValue());
            vo.setId(IdentificativiDelegate.getInstance().getNextId(connection,
                    NomiTabelle.CA_CRL));
            dao.salvaCaCRLPuntoDistribuzione(connection, vo);
        }
    }

    public void cancellaTutteCA() throws DataException {
        dao.cancellaTutteCA();
    }

    public ActionErrors aggiornaListaCA(String pathToZip) throws DataException {
        ActionErrors errors = new ActionErrors();
        ZipFile zip;
        try {
            zip = new ZipFile(pathToZip);
            ZipEntry entry = null;
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                entry =  entries.nextElement();
                if (entry != null && !entry.isDirectory()) {
                    try {
                        CaVO ca = VerificaFirma.getCaFromCertificate(zip
                                .getInputStream(entry));
                        salvaCertificateAuthority(ca);
                        zip.close();
                        logger.info("CA salvata: " + ca.getIssuerCN());
                    } catch (ConversionException e) {
                        logger.warn("", e);
                        errors.add("generale", new ActionMessage(
                                "firmadigitale.errore.conversione", entry
                                        .getName()));
                    } catch (IOException e) {
                        logger.warn("", e);
                        errors.add("generale", new ActionMessage(
                                "firmadigitale.errore.conversione", entry
                                        .getName()));
                    } catch (DataException e) {
                        logger.error("", e);
                        errors.add("generale", new ActionMessage(
                                "firmadigitale.errore.salvataggio", entry
                                        .getName()));
                    } catch (Exception e) {
                        logger.error("", e);
                        errors.add("generale", new ActionMessage(
                                "firmadigitale.errore.sconosciuto", entry
                                        .getName(), e.getMessage()));
                    }
                }
            }
        } catch (IOException e1) {
            throw new DataException(
                    "Errore nella lettura del Zip File. File: '" + pathToZip
                            + "'" + "\nDettaglio Errore:" + e1.getMessage());
        }
       
        return errors;
    }

    // private void scanFolderForCaCerts(File directory, ArrayList errors) {
    // if (directory.exists() && directory.isDirectory()) {
    // File[] fileArray = directory.listFiles();
    // for (int i = 0; i < fileArray.length; i++) {
    // if (fileArray[i].isDirectory()) {
    // scanFolderForCaCerts(fileArray[i], errors);
    // } else {
    // try {
    // salvaCertificateAuthority(VerificaFirma
    // .getCaFromCertificate(fileArray[i]
    // .getAbsolutePath()));
    // } catch (Exception e) {
    // errors.add(e.getMessage());
    // }
    // }
    // }
    // }
    // }

    /**
     * Aggiorna la lista dei Certificati Revocati di tutte le CA presenti nella
     * base dati.
     * 
     * @return ArrayList Contenente tutti gli eventuali messaggi di errore
     *         riscontrati durante l'operazione.
     */

    public ActionMessages aggiornaListaCertificatiRevocati() {
        // get all CA_CRL
        ActionMessages errors = new ActionMessages();
        Iterator<CrlUrlVO> itUrls;
        try {
            itUrls = dao.getPuntiDistribuzioneCRL().iterator();
        } catch (DataException e1) {
            errors
                    .add("generale", new ActionMessage("errore.crl.generale",
                            "Impossibile leggere dalla base dati.\n"
                                    + e1.getMessage()));
            return errors;
        }
        // for each CRL URL --> cruCRL
        while (itUrls.hasNext()) {
            CrlUrlVO vo = (CrlUrlVO) itUrls.next();
            logger.info("Checking for CRL update of CA id:" + vo.getCaId());
            Date lastUpdate = vo.getDataEmissione();
            byte[] bytes = null;
            boolean gotData = false;
            boolean crlValidFormat = false;
            boolean dataSaved = false;
            try {
                if (ProtocolConstants.HTTP.equalsIgnoreCase(vo.getTipo())) {
                    // download from http
                    bytes = HttpUtil.writeToStream(vo.getUrl());
                    gotData = bytes != null;
                } else if (ProtocolConstants.HTTPS.equalsIgnoreCase(vo
                        .getTipo())) {
                    // download from https
                    bytes = HttpUtil.writeToStreamFromHttps(vo.getUrl());
                    gotData = bytes != null;
                } else if (ProtocolConstants.LDAP
                        .equalsIgnoreCase(vo.getTipo())) {
                    // download from ldap
                    bytes = LdapUtil.downloadCRLfromURL(vo.getUrl());
                    gotData = bytes != null;
                } else {
                    throw new Exception("Protocollo non supportato:"
                            + vo.getTipo());
                }
                if (bytes != null && bytes.length > 0) {
                    X509CRL curCRL = VerificaFirma.getCRL(bytes);
                    Set revokedCertificates = curCRL.getRevokedCertificates();
                    crlValidFormat = true;
                    if (lastUpdate == null
                            || curCRL.getThisUpdate().after(lastUpdate)) {
                        dao.salvaListaCertificatiRevocati(revokedCertificates,
                                vo.getCaId(), vo.getId().intValue());
                        dataSaved = true;
                    }
                } else {
                    throw new Exception(
                            "Errore nell'elaborazione dei dati della CRL:"
                                    + vo.getUrl());
                }
            } catch (Exception e) {
                if (gotData) {
                    if (crlValidFormat) {
                        if (dataSaved) {
                            // dovrebbe essere irrangiungibile questo punto.....
                        } else {
                            // errore nel salvataggio della CRL nella base dati:
                            // set stato su ErroreSalvataggio
                            dao.setStatoErroreCRL(vo.getId().intValue(),
                                    Parametri.CRL_ERRORE_DB);
                        }
                    } else {
                        // link ok ma dati non di tipo CRL valido: set statoCRL
                        // su ErroreDownload
                        dao.setStatoErroreCRL(vo.getId().intValue(),
                                Parametri.CRL_ERRORE_FORMATO);
                    }
                } else {
                    // errore nel download della CRL, set statoCRL su
                    // ErrorDownload;
                    dao.setStatoErroreCRL(vo.getId().intValue(),
                            Parametri.CRL_ERRORE_DOWNLOAD);
                }
                errors.add("generale", new ActionMessage("errore.crl.generale",
                        "Errore nel salvataggio dei dati:" + e.getMessage()));
                logger.error("", e);
            }
        }
        return errors;
    }
}