package it.compit.fenice.restful;

import it.compit.fenice.mvc.presentation.helper.ResponseView;
import it.compit.fenice.util.JSONUtil;
import it.finsiel.siged.exception.DataException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;

public class GestioneArchiviClient {

	private enum GestioneArchiviContextEnum {

		TOKEN("/api/token"),
		UPLOAD("/api/document/upload");

		GestioneArchiviContextEnum(String context) {
			this.context = context;
		}

		private String context;

		public String getContext() {
			return context;
		};

	}

	final static String AUTHORIZATION = "Bearer";
	final static String ACCEPT = "application/vnd.ga.[v1]+json";
	final static String CONTENT_TYPE = "application/json";
	final static String ENC_TYPE = "multipart/form-data";
	final static String GRANT_TYPE = "password";
	final static String SECURITY = "TLS";
	final static String HOST = "https://conservazionedocumentielettronici.gestionearchivi.it";
	final static String PORT = "9443";

	HttpsClient client;
	String username;
	String password;
	String token;
	
	public GestioneArchiviClient(String username, String password) {
		this.username = username;
		this.password = password;
		client = new HttpsClient(SECURITY);
	}

	private String getUrl(GestioneArchiviContextEnum context) {
		return HOST + ":" + PORT + context.getContext();
	};

	private ResponseView getToken() {
		ResponseView view=new ResponseView();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(HttpHeaders.ACCEPT, ACCEPT);
		headers.put(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE);
		Map<String, String> payload = new HashMap<String, String>();
		payload.put("grant_type", GRANT_TYPE);
		payload.put("username", username);
		payload.put("password", password);
		try {
			HttpResponse response = client.post(
					getUrl(GestioneArchiviContextEnum.TOKEN), headers,
					new StringEntity(JSONUtil.mapToJson(payload)));
			view.setStatusCode(response.getStatusLine().getStatusCode());
			
			if (view.getStatusCode() == 200) {
				token = JSONUtil.getJsonNodeValue(
						EntityUtils.toString(response.getEntity()), "token");
			} else {
				view.setMessage(JSONUtil.getJsonNodeValue(
						EntityUtils.toString(response.getEntity()), "message"));
			}
		} catch (DataException e) {
			view.setStatusCode(500);
			view.setMessage(e.getMessage());
		} catch (ParseException e) {
			view.setStatusCode(500);
			view.setMessage(e.getMessage());
		} catch (IOException e) {
			view.setStatusCode(500);
			view.setMessage(e.getMessage());
		}
		return view;
	};

	public ResponseView uploadDocument(GestioneArchiviData d) {
		ResponseView view=new ResponseView();
		String boundary = "-----------"+System.currentTimeMillis();
		view=getToken();
		if (view.getStatusCode()!=200)
			return view;

		Map<String, String> headers = new HashMap<String, String>();
		headers.put(HttpHeaders.AUTHORIZATION, AUTHORIZATION + " " + token);
		headers.put(HttpHeaders.ACCEPT, ACCEPT);
		headers.put(HttpHeaders.CONTENT_TYPE, ENC_TYPE+"; boundary="+boundary);
		headers.put(HttpHeaders.CACHE_CONTROL, "no-cache");

		HttpEntity entity = MultipartEntityBuilder
			    .create()
			    .setBoundary(boundary)
			    .addTextBody("description", d.getDescrizione())
			    .addBinaryBody("file_csv", new ByteArrayInputStream( d.getFileCsv()), ContentType.create("text/csv"), d.getNomeFileCsv())
			    .addBinaryBody("file_zip", new ByteArrayInputStream( d.getFileZip()), ContentType.create("application/zip"), d.getNomeFileZip())
			    .addTextBody("tipo_doc" , d.getTipoDoc())
			    .build();
		try {
			HttpResponse response = client.post(getUrl(GestioneArchiviContextEnum.UPLOAD), headers,entity);
			view.setStatusCode(response.getStatusLine().getStatusCode());
			if(response.getEntity()!=null)
				view.setMessage(JSONUtil.getJsonNodeValue(EntityUtils.toString(response.getEntity()), "message"));
			
		} catch (DataException e) {
			view.setStatusCode(500);
			view.setMessage(e.getMessage());
		} catch (ParseException e) {
			view.setStatusCode(500);
			view.setMessage(e.getMessage());
		} catch (IOException e) {
			view.setStatusCode(500);
			view.setMessage(e.getMessage());
		}
		return view;
	}

}
