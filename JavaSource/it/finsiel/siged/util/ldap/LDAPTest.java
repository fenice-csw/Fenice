
package it.finsiel.siged.util.ldap;

import it.finsiel.siged.util.FileUtil;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Iterator;

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPJSSESecureSocketFactory;
import com.novell.ldap.LDAPSearchResults;
import com.novell.ldap.LDAPUrl;
import com.novell.ldap.util.Base64;

/**
 * @author Paolo Spadafora - digital highwat
 * 
 */
public class LDAPTest {

    public static void main(String[] args) {

        // int ldapVersion = LDAPConnection.LDAP_V3;
        // int ldapPort = LDAPConnection.DEFAULT_PORT;
        // int ldapSSLPort = LDAPConnection.DEFAULT_SSL_PORT;
        // String ldapHost = "indicepa.gov.it";
        // String loginDN = "c=it";
        // String password = "paolo";
        // LDAPConnection conn = new LDAPConnection();

        // connessioneAnonima(conn, ldapHost, ldapPort);

        // autenticazioneUtente(conn, ldapHost, ldapPort, loginDN, password);

        // autenticazioneUtenteConVersione(ldapVersion, conn, ldapHost,
        // ldapPort,loginDN, password);

        // cerca(conn, ldapVersion, ldapHost,ldapPort, loginDN,password);
        downloadCRLfromURL(
                "ldap://ldap2.infocamere.it/cn%3dInfoCamere%20Firma%20Qualificata,ou%3dCertificatore%20Accreditato%20del%20Sistema%20Camerale,o%3dInfoCamere%20SCpA,c%3dIT?certificateRevocationList",
                "Infocamere");
        downloadCRLfromURL(
                "ldap://fe.csp.multicertify.com:389/ou%3DProvincia%20di%20Roma%20Firma%20digitale%2Co%3DProvincia%20di%20Roma?certificaterevocationlist",
                "Actalis");
        downloadCRLfromURL(
                "ldap://fe.csp.multicertify.com:389/ou%3D-%20Firma%20Digitale%20Intersiel%20S.p.A.%2Co%3DIntersiel%20S.p.A.?certificaterevocationlist",
                "Intersiel");
        // java.security.Security.addProvider(new
        // com.sun.net.ssl.internal.ssl.Provider());

        /*
         * The property "javax.net.ssl.trustStore" must be set to the path of a
         * keystore that holds the certificate of the server
         * -Djavax.net.ssl.trustStore=/path/keystoreName.keystore
         */
        // autenticazioneSSL(ldapVersion, ldapHost, ldapSSLPort, loginDN,
        // password);
        System.exit(0);

    }

    private static void connessioneAnonima(LDAPConnection conn, String host,
            int port) {
        try {
            System.out.println("connessione anonima:");
            // connect to the server
            conn.connect(host, port);

            System.out.println((conn.isBound()) ? "\n Autenticato."
                    : "NON Autenticato.");

            conn.disconnect();
        } catch (LDAPException e) {
            e.printStackTrace();
            System.out.println("Errore: " + e.toString());
        }
        return;
    }

    private static void autenticazioneUtente(LDAPConnection conn, String host,
            int port, String dn, String passwd) {
        try {
            System.out.println("Autenticazione Utente:\t" + dn);

            conn.connect(host, port);

            try {
                conn.bind(LDAPConnection.LDAP_V3, dn, passwd.getBytes("UTF8"));
            } catch (UnsupportedEncodingException u) {
                throw new LDAPException("UTF8 Invalid Encoding",
                        LDAPException.LOCAL_ERROR, (String) null, u);
            }

            System.out.println((conn.isBound()) ? "\tAutenticato."
                    : "NON autenticato.");

            conn.disconnect();
        } catch (LDAPException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.toString());
        }
        return;
    }

    private static void autenticazioneUtenteConVersione(int version,
            LDAPConnection conn, String host, int port, String dn, String passwd) {
        try {
            System.out.println("Autenticazione Utente:\t" + dn
                    + " con versione LDAP:" + version);

            conn.connect(host, port);

            try {

                conn.bind(version, dn, passwd.getBytes("UTF8"));
            } catch (UnsupportedEncodingException u) {
                throw new LDAPException("UTF8 Invalid Encoding",
                        LDAPException.LOCAL_ERROR, (String) null, u);
            }

            System.out.println((conn.isBound()) ? "\tAutenticato\n"
                    : "\tNON Autenticato\n");

            conn.disconnect();
        } catch (LDAPException e) {
            System.out.println("Error: " + e.toString());
        }
        return;
    }

    private static void autenticazioneSSL(int version, String host,
            int SSLPort, String dn, String passwd) {

        LDAPJSSESecureSocketFactory ssf = new LDAPJSSESecureSocketFactory();
        LDAPConnection conn = new LDAPConnection(ssf);

        try {
            System.out
                    .println("Autenticazione tramite SSL dell'utente:\t" + dn);

            conn.connect(host, SSLPort);

            try {
                conn.bind(version, dn, passwd.getBytes("UTF8"));
            } catch (UnsupportedEncodingException u) {
                throw new LDAPException("UTF8 Invalid Encoding",
                        LDAPException.LOCAL_ERROR, (String) null, u);
            }

            System.out.println((conn.isBound()) ? "\tAutenticato (SSL)\n"
                    : "\tNON Autenticato (SSL)\n");

            conn.disconnect();
        } catch (LDAPException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.toString());
        }
        return;
    }

    public static void cerca(LDAPConnection conn, int version, String host,
            int port, String dn, String passwd) {

        // ricerca per categoria e nome
        // searchBase = "c=it"
        // searchScope = SCOPE_SUB
        // searchFilter =
        // (&(description=*Roma*)(objectClass=amministrazione)(tipoAmm=P))
        // P=Azienda sanitaria

        // lista aoo per una amministrazione
        // searchBase = "o=agdogane,c=it" agdogane oppure nome amministrazione
        // searchFilter = "(aoo=*)"

        String searchBase = "o=agdogane,c=it";
        String searchFilter = "(aoo=*)";
        int searchScope = LDAPConnection.SCOPE_SUB;

        try {
            // connect to the server
            conn.connect(host, port);
            // bind to the server
            // conn.bind( version, dn, passwd.getBytes("UTF8") );

            LDAPSearchResults searchResults = conn.search(searchBase,
                    searchScope, searchFilter, null, false);

            /*
             * To print out the search results, -- The first while loop goes
             * through all the entries -- The second while loop goes through all
             * the attributes -- The third while loop goes through all the
             * attribute values
             */
            while (searchResults.hasMore()) {
                LDAPEntry nextEntry = null;
                try {
                    nextEntry = searchResults.next();
                } catch (LDAPException e) {
                    e.printStackTrace();
                    continue;
                }

                System.out.println("\n" + nextEntry.getDN());
                System.out.println("  Attributi: ");

                LDAPAttributeSet attributeSet = nextEntry.getAttributeSet();
                Iterator allAttributes = attributeSet.iterator();

                while (allAttributes.hasNext()) {
                    LDAPAttribute attribute = (LDAPAttribute) allAttributes
                            .next();
                    String attributeName = attribute.getName();

                    System.out.println("    " + attributeName);

                    Enumeration allValues = attribute.getStringValues();

                    if (allValues != null) {
                        while (allValues.hasMoreElements()) {
                            String Value = (String) allValues.nextElement();
                            if (Base64.isLDIFSafe(Value)) {
                                // is printable
                                System.out.println("      " + Value);
                            } else {
                                // base64 encode and then print out
                                Value = Base64.encode(Value.getBytes());
                                System.out.println("      " + Value);
                            }
                        }
                    }
                }
            }
            // disconnect with the server
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downloadCRLfromURL(String url, String nomeCa) {

        try {
            LDAPUrl ldapUrl = new LDAPUrl(LDAPUrl.decode(url));

            LDAPEntry entry = LDAPConnection.read(ldapUrl);

            System.out.println(entry.getDN());

            LDAPAttributeSet attributeSet = entry.getAttributeSet();
            Iterator allAttributes = attributeSet.iterator();

            while (allAttributes.hasNext()) {
                LDAPAttribute attribute = (LDAPAttribute) allAttributes.next();
                String attributeName = attribute.getName();
                System.out.println("    " + attributeName);
                if (attributeName.toLowerCase().indexOf(
                        "certificaterevocationlist") >= 0) {
                    System.out.println("Saving CRL...");
                    byte[] content = attribute.getByteValue();
                    FileUtil.writeFile(new ByteArrayInputStream(content), "C:/"
                            + nomeCa + ".crl");
                    // break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
