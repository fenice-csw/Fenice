package it.compit.fenice.mvc.presentation.helper;

public class ResponseView {

	private int statusCode;
	
	private String message;
	
	public ResponseView() {
	}


	public ResponseView(int statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
	
}
