package it.compit.fenice.restful;

import it.finsiel.siged.exception.DataException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpsClient {

	CloseableHttpClient httpClient;
	String security;
	
	public HttpsClient(String security) {
		this.security = security;
		setHttpsClient();
	}

	public HttpResponse post(String url, Map<String, String> headers, HttpEntity entity) throws DataException{
		HttpResponse response=null;
		try {
			HttpPost request = new HttpPost(new URI(url));
			if(headers!=null){
				for(String key: headers.keySet()){
					request.addHeader(key, headers.get(key));
				}
			}
			if(entity!=null)
				request.setEntity(entity);
			response = httpClient.execute(request);
		} catch (ClientProtocolException e) {
			throw new DataException("Request/Response non valida." + e.getMessage());
		} catch (IOException e) {
			 throw new DataException("Errore nella comunicazione dei dati all'URL:" + url + ". \n" + e.getMessage());
		} catch (URISyntaxException e) {
			throw new DataException("URL non valido." + e.getMessage());
			
		} catch (Exception e) {
			throw new DataException("Eccezione non gestita:" + e.getMessage());
		}
		return response;
		
	}
	
	public HttpResponse get(String url, Map<String, String> headers) throws DataException{
		HttpResponse response=null;
		
		try {
			HttpGet request = new HttpGet(url);
			if(headers!=null){
				for(String key: headers.keySet()){
					request.addHeader(key, headers.get(key));
				}
			}
			response = httpClient.execute(request);
		} catch (IOException e) {
			throw new DataException("Eccezione non gestita:" + e.getMessage());
		}
		return response;
	}

	public void setHttpsClient(){
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
			}
			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
			}
		} };

		try {
			SSLContext sc = SSLContext.getInstance(security);
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HostnameVerifier allHostsValid = new HostnameVerifier() {

				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};
			httpClient = HttpClientBuilder.create().setSSLContext(sc).setSSLHostnameVerifier(allHostsValid).build();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
