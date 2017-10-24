package SaveLogin;

import java.util.List;

import burp.IHttpRequestResponse;

public class Login {
	private int id;
	private String codiceFiscale;
	private String password;
	private String tipologia;
	private List<IHttpRequestResponse> action_login;
	public Login(){}
	public Login(int id, String codiceFiscale, String password, String tipologia,List<IHttpRequestResponse> action_login){
		this.id = id;
		this.codiceFiscale = codiceFiscale;
		this.password = password;
		this.tipologia = tipologia;
		this.action_login = action_login;
	}
	public int getId(){
		return this.id;
	}
	public String getCodiceFiscale(){
		return this.codiceFiscale;
	}
	public String getPassword(){
		return this.password;
	}
	public String getTipologia(){
		return this.tipologia;
	}
	public List<IHttpRequestResponse> getAction_Login(){
		return this.action_login;
	}
	public void setId(int id){
		this.id = id;
	}
	public void setCodiceFiscale(String codiceFiscale){
		this.codiceFiscale = codiceFiscale;
	}
	public void setPassword(String password){
		this.password = password;
	}
	public void setTipologia(String tipologia){
		this.tipologia = tipologia;
	}
	public void setAction_Login(List<IHttpRequestResponse> action_login){
		this.action_login = action_login;
	}
}
