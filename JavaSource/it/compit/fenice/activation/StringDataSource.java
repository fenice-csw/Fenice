package it.compit.fenice.activation;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class StringDataSource implements javax.activation.DataSource {

	private String data;
	private String contentType = "text/plain";
	private String name = null;

	public void setData(String data) {
		this.data = data;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InputStream getInputStream() {
		return new ByteArrayInputStream(data.getBytes());
	}

	public OutputStream getOutputStream() {
		return null;
	}

	public String getContentType() {
		return contentType;
	}

	public String getName() {
		return name;
	}

}