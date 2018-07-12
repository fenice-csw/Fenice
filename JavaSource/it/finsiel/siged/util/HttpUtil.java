/*
 * Created on 30-nov-2004
 *
 * 
 */
package it.finsiel.siged.util;

import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.util.ssl.EprotSSLSocketFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;


public final class HttpUtil {

    static Logger logger = Logger.getLogger(HttpUtil.class.getName());

    public static byte[] writeToStream(String urlString) throws DataException {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            if (connection instanceof HttpURLConnection) {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.connect();
                InputStream in = httpConnection.getInputStream();
                BufferedInputStream is = new BufferedInputStream(in);
                byte[] buf = new byte[4096];
                int r = -1;
                while ((r = is.read(buf)) > 0) {
                    os.write(buf, 0, r);
                    os.flush();
                }
                is.close();
            } else {
                throw new DataException(
                        "Protocollo non HTTP per l'URL fornito.");
            }
            return os.toByteArray();
        } catch (MalformedURLException e) {
            throw new DataException("URL non valido." + e.getMessage());
        } catch (IOException e) {
            throw new DataException("Errore nel download dei dati dall'URL:"
                    + urlString + "\n" + e.getMessage());
        }
    }

    public static byte[] writeToStreamFromHttps(String urlString)
            throws DataException {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(new EprotSSLSocketFactory());
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            if (connection instanceof HttpsURLConnection) {
                HttpURLConnection httpConnection = (HttpsURLConnection) connection;
                httpConnection.connect();
                InputStream in = httpConnection.getInputStream();
                BufferedInputStream is = new BufferedInputStream(in);
                byte[] buf = new byte[4096];
                int r = -1;
                while ((r = is.read(buf)) > 0) {
                    os.write(buf, 0, r);
                    os.flush();
                }
                is.close();
            } else {
                throw new DataException(
                        "Protocollo non HTTPS per l'URL fornito.");
            }
            return os.toByteArray();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new DataException("URL non valido." + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new DataException("Errore nel download dei dati dall'URL:"
                    + urlString + "\n" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            writeToStreamFromHttps("https://e-trustcom.intesa.it/CRL/INTESA_TS.crl");
        } catch (DataException e) {
            e.printStackTrace();
        }
    }

}