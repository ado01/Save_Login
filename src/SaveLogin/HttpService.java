package SaveLogin;

import java.io.Serializable;

import burp.IHttpService;

public class HttpService implements IHttpService, Serializable{

	private String host;
	private int port;
	private String protocol;
	
	public HttpService(IHttpService ihs) {
		this.host = ihs.getHost();
		this.port = ihs.getPort();
		this.protocol = ihs.getProtocol();
	}
	
	public String getHost() {
		return this.host;
	}

	public int getPort() {
		return this.port;
	}

	public String getProtocol() {
		return this.protocol;
	}

}
