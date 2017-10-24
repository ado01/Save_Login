package SaveLogin;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import burp.IExtensionHelpers;
import burp.IHttpRequestResponse;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyHandler extends DefaultHandler{

	private IExtensionHelpers helper;
	private PrintWriter Sout;
	
	private List<Login> listLogin;
	private Login login;
	
	private List<IHttpRequestResponse> requests = new ArrayList<IHttpRequestResponse>();
	private IHttpRequestResponse req;
	
	boolean bId = false;
	boolean bCodiceFiscale = false;
	boolean bPassword = false;
	boolean bTipologia = false;
	boolean bActionLogin = false;
	boolean bRequest = false;
	
	public MyHandler(List<Login> listLogin, IExtensionHelpers helper, PrintWriter Sout){
		this.listLogin = listLogin;
		this.helper = helper;
		this.Sout = Sout;
	}
	
	public Login getLogin(){
		return this.login;
	}
	
	public List<Login> getListLogin(){
		return this.listLogin;
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes)throws SAXException{
		if(qName.equalsIgnoreCase("Login")){
			login = new Login();
		}else if(qName.equalsIgnoreCase("id")){
			bId = true;
		}else if(qName.equalsIgnoreCase("codiceFiscale")){
			bCodiceFiscale = true;
		}else if(qName.equalsIgnoreCase("password")){
			bPassword = true;
		}else if(qName.equalsIgnoreCase("tipologia")){
			bTipologia = true;
		}else if(qName.equalsIgnoreCase("requests")){
			requests = new ArrayList<IHttpRequestResponse>();
			bActionLogin = true;
		}else if(qName.equalsIgnoreCase("req")){
			String idReq = attributes.getValue("id");
			Sout.println("LOGIN : "+idReq);
			bRequest = true;
		}
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException{
		if(qName.equalsIgnoreCase("Login")){
			listLogin.add(login);
		}else if(qName.equalsIgnoreCase("requests")){
			login.setAction_Login(requests);
		}
	}
	
	public void characters(char ch[], int start, int length) throws SAXException{
		if(bId){
			login.setId(Integer.parseInt(new String(ch,start,length)));
			Sout.println(Integer.parseInt(new String(ch,start,length)));
			bId = false;
		}else if(bCodiceFiscale){
			login.setCodiceFiscale(new String(ch, start, length));
			Sout.println(new String(ch, start, length));
			bCodiceFiscale = false;
		}else if(bPassword){
			login.setPassword(new String(ch, start, length));
			Sout.println(new String(ch, start, length));
			bPassword = false;
		}else if(bTipologia){
			login.setTipologia(new String(ch, start, length));
			Sout.println(new String(ch, start, length));
			bTipologia = false;
		}else if(bActionLogin){
			bActionLogin = false;
		}else if(bRequest){
			byte[] byteDeserialization = (byte[])helper.base64Decode(new String(ch, start, length));
			IHttpRequestResponse request = (IHttpRequestResponse)MySerialization.bytesToObj(byteDeserialization);
			requests.add(request);	//aggiunto la lista di richieste al login (una lista di HttpRequestResponse)
			//Sout.println("Request = " + Arrays.toString(tt.getRequest()));
			bRequest = false;
		}
	}
}
