
package it.finsiel.siged.util.ldap;

import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.AuthenticationException;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.vo.IdentityVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.util.FileUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPSearchConstraints;
import com.novell.ldap.LDAPSearchResults;
import com.novell.ldap.LDAPUrl;

/**
 * @author Almaviva sud
 * 
 */
public class LdapUtil {
    static Logger logger = Logger.getLogger(LdapUtil.class.getName());

    public static boolean autenticaUtente(String host, int port, String dn,
            String passwd) throws AuthenticationException {
        boolean auth = false;
        try {
            LDAPConnection conn = new LDAPConnection();
            conn.connect(host, port);
            try {
                conn.bind(LDAPConnection.LDAP_V3, dn, passwd.getBytes("UTF8"));
                auth = conn.isBound();
            } catch (UnsupportedEncodingException u) {
                throw new LDAPException("UTF8 Invalid Encoding",
                        LDAPException.LOCAL_ERROR, (String) null, u);
            }

            try {
                conn.disconnect();
            } catch (LDAPException e1) {
                logger.debug("", e1);
            }
        } catch (LDAPException e) {
            throw new AuthenticationException(
                    "Errore nella connessione al server LDAP.\n"
                            + e.getLocalizedMessage());
        }
        return auth;
    }

    public static ArrayList cercaAmministrazione(String host, int port,
            String searchBase, String searchFilter, int searchScope,
            int maxResult) throws DataException {
        ArrayList res = new ArrayList();

        try {
            LDAPConnection conn = new LDAPConnection();
            conn.connect(host, port);

            LDAPSearchConstraints constraints = new LDAPSearchConstraints();
            constraints.setMaxResults(maxResult);

            LDAPSearchResults searchResults = conn.search(searchBase,
                    searchScope, searchFilter, null, false, constraints);
            logger.info(searchBase + " . " + searchFilter);
            while (searchResults.hasMore()) {
                LDAPEntry nextEntry = null;
                try {
                    nextEntry = searchResults.next();
                } catch (LDAPException e) {
                    logger.error("", e);
                    continue;
                }

                IdentityVO rec = new IdentityVO();
                LDAPAttributeSet attributeSet = nextEntry.getAttributeSet();
                LDAPAttribute cur = attributeSet.getAttribute("o");
                rec.setCodice(cur != null ? cur.getStringValue() : "");
                cur = attributeSet.getAttribute("provincia");
                rec.setName(cur != null ? cur.getStringValue() : "");
                cur = attributeSet.getAttribute("description");
                rec.setDescription(cur != null ? cur.getStringValue() : "");
                res.add(rec);
            }
            conn.disconnect();
        } catch (Exception e) {
            logger.error("", e);
            throw new DataException(e.getMessage());
        }
        return res;
    }

    public static ArrayList listaAOO(String host, int port, String searchBase,
            String searchFilter, int searchScope, int maxResult)
            throws DataException {
        ArrayList res = new ArrayList();

        try {
            LDAPConnection conn = new LDAPConnection();
            conn.connect(host, port);

            LDAPSearchConstraints constraints = new LDAPSearchConstraints();
            constraints.setMaxResults(maxResult);

            LDAPSearchResults searchResults = conn.search(searchBase,
                    searchScope, searchFilter, null, false, constraints);

            while (searchResults.hasMore()) {
                LDAPEntry nextEntry = null;
                try {
                    nextEntry = searchResults.next();
                } catch (LDAPException e) {
                    logger.error("", e);
                    continue;
                }
                DestinatarioVO vo = new DestinatarioVO();

                LDAPAttributeSet attributeSet = nextEntry.getAttributeSet();
                LDAPAttribute cur = attributeSet.getAttribute("cognomeResp");
                vo.setDestinatario(cur != null ? cur.getStringValue() : "");
                cur = attributeSet.getAttribute("nomeResp");
                vo
                        .setDestinatario(cur != null ? (vo.getDestinatario()
                                + " " + cur.getStringValue()) : vo
                                .getDestinatario());
                vo.setCodice(nextEntry.getDN());
                cur = attributeSet.getAttribute("l");
                vo.setCitta(cur != null ? cur.getStringValue() : "");
                cur = attributeSet.getAttribute("mail");
                vo.setEmail(cur != null ? cur.getStringValue() : "");
                cur = attributeSet.getAttribute("street");
                vo.setIndirizzo(cur != null ? cur.getStringValue() : "");
                cur = attributeSet.getAttribute("description");
                vo.setIntestazione(cur != null ? cur.getStringValue() : "");
                cur = attributeSet.getAttribute("postalCode");
                vo.setCodicePostale(cur != null ? cur.getStringValue() : "");
                cur = attributeSet.getAttribute("provincia");
                vo.setProvinciaId(cur != null ? cur.getStringValue() : "");
                res.add(vo);
            }
            conn.disconnect();
        } catch (Exception e) {
            logger.error("", e);
            throw new DataException(e.getMessage());
        }
        return res;
    }

    public static DestinatarioVO getAOO(String host, int port, String dn)
            throws DataException {
        DestinatarioVO vo = new DestinatarioVO();
        try {
            LDAPConnection conn = new LDAPConnection();
            conn.connect(host, port);

            try {
                LDAPEntry nextEntry = conn.read(dn);

                LDAPAttributeSet attributeSet = nextEntry.getAttributeSet();
                LDAPAttribute cur = attributeSet.getAttribute("cognomeResp");
                vo.setDestinatario(cur != null ? cur.getStringValue() : "");
                cur = attributeSet.getAttribute("nomeResp");
                vo
                        .setDestinatario(cur != null ? (vo.getDestinatario()
                                + " " + cur.getStringValue()) : vo
                                .getDestinatario());
                cur = attributeSet.getAttribute("aoo");
                vo.setCodice(cur != null ? cur.getStringValue() : "");
                cur = attributeSet.getAttribute("l");
                vo.setCitta(cur != null ? cur.getStringValue() : "");
                cur = attributeSet.getAttribute("mail");
                vo.setEmail(cur != null ? cur.getStringValue() : "");
                cur = attributeSet.getAttribute("street");
                vo.setIndirizzo(cur != null ? cur.getStringValue() : "");
                cur = attributeSet.getAttribute("description");
                vo.setIntestazione(cur != null ? cur.getStringValue() : "");
                cur = attributeSet.getAttribute("postalCode");
                vo.setCodicePostale(cur != null ? cur.getStringValue() : "");
                cur = attributeSet.getAttribute("provincia");
                vo.setProvinciaId(cur != null ? cur.getStringValue() : "");
                logger.info(vo.getCodice());
            } catch (LDAPException e) {
                logger.error("", e);
                vo.setReturnValue(ReturnValues.NOT_FOUND);
            }
            conn.disconnect();
        } catch (Exception e) {
            logger.error("", e);
            throw new DataException(e.getMessage());
        }
        return vo;
    }

    public static byte[] downloadCRLfromURL(String url) throws DataException {
        byte[] crlBytes = new byte[0];
        try {

            LDAPUrl ldapUrl = new LDAPUrl(LDAPUrl.decode(url));
            LDAPEntry entry = LDAPConnection.read(ldapUrl);
            LDAPAttributeSet attributeSet = entry.getAttributeSet();
            Iterator allAttributes = attributeSet.iterator();
            while (allAttributes.hasNext()) {
                LDAPAttribute attribute = (LDAPAttribute) allAttributes.next();
                String attributeName = attribute.getName();
                logger.info("    " + attributeName);
                if (attributeName.toLowerCase().indexOf(
                        "certificaterevocationlist") >= 0) {
                    logger.info("Saving CRL...");
                    byte[] content = attribute.getByteValue();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ByteArrayInputStream bais = new ByteArrayInputStream(
                            content);
                    FileUtil.writeFile(bais, baos);
                    crlBytes = baos.toByteArray();
                    baos.close();
                    bais.close();
                    break;
                } else {
                    logger.info("Attributo sconosciuto=" + attributeName);
                }
            }
        } catch (MalformedURLException e) {
            throw new DataException("URL non valido.\n" + e.getMessage());
        } catch (LDAPException e) {
            throw new DataException("Errore durante il download dei dati.\n"
                    + e.getMessage());
        } catch (IOException e) {
            throw new DataException("Errore durante il download dei dati.\n"
                    + e.getMessage());
        }
        return crlBytes;
    }

    public static String getDN(String name, String baseDN) {
        return "cn=" + name + "," + baseDN;
    }

}
