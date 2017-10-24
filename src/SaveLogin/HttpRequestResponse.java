package SaveLogin;

import java.io.Serializable;

import burp.IHttpRequestResponse;
import burp.IHttpService;

public class HttpRequestResponse implements IHttpRequestResponse, Serializable{

	private byte[] request;
	private byte[] respons;
	private String comment;
	private String highlight;
	private HttpService httpService;
	
	public HttpRequestResponse(){}
	
	public HttpRequestResponse(IHttpRequestResponse ihrr) {
		this.request = ihrr.getRequest();
		this.respons = ihrr.getResponse();
		this.comment = ihrr.getComment();
		this.highlight = ihrr.getHighlight();
		this.httpService = new HttpService(ihrr.getHttpService());
	}
	
	public byte[] getRequest() {
		return this.request;
	}

	public void setRequest(byte[] message) {
		this.request = message;
	}

	public byte[] getResponse() {
		return this.respons;
	}

	public void setResponse(byte[] message) {
		this.respons = message;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getHighlight() {
		return this.highlight;
	}

	public void setHighlight(String color) {
		this.highlight = color;
	}

	public HttpService getHttpService() {
		return this.httpService;
	}

	public void setHttpService(IHttpService httpService) {
		//this.httpService = new HttpService(httpService);
		this.httpService = (HttpService)httpService;
	}

}
