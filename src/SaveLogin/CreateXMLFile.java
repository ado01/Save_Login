package SaveLogin;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import burp.IExtensionHelpers;
import burp.IHttpRequestResponse;

public class CreateXMLFile {
	private String nomeFile;   
	private String idText;
	private String codiceFiscale; 
	private String password;   
	private String tipologia;
	private List<IHttpRequestResponse> action_login;
	private List<Element> elementRequest;
	private IExtensionHelpers helper;
	private File XMLFile;
	private PrintWriter Sout;
	public CreateXMLFile(String nomeFile, int id, String codiceFiscale, String password, String tipologia, List<IHttpRequestResponse> action_login, PrintWriter Sout, IExtensionHelpers helper){
		this.nomeFile = nomeFile;
		this.idText = Integer.toString(id);
		this.codiceFiscale = codiceFiscale;
		this.password = password;
		this.tipologia = tipologia;
		this.action_login = action_login;
		this.Sout = Sout;
		this.helper = helper;
		try{
			DocumentBuilderFactory documentBuildFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuildFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			
			Element roots = document.createElement("Logins");
			document.appendChild(roots);
			
			Element root = document.createElement("Login");
			roots.appendChild(root);
			
			Element elementId = document.createElement("id");
			elementId.appendChild(document.createTextNode(idText));
			root.appendChild(elementId);
			
			Element elementCodiceFiscale = document.createElement("codiceFiscale");
			elementCodiceFiscale.appendChild(document.createTextNode(codiceFiscale));
			root.appendChild(elementCodiceFiscale);

			Element elementPassword = document.createElement("password");
			elementPassword.appendChild(document.createTextNode(password));
			root.appendChild(elementPassword);
			
			Element elementTipologia = document.createElement("tipologia");
			elementTipologia.appendChild(document.createTextNode(tipologia));
			root.appendChild(elementTipologia);
			
			Element elementRequests = document.createElement("requests");
			root.appendChild(elementRequests);
			
			int ordine = 0;
			Sout.println("dimensione actionlogin:   "+action_login.size());
			for(IHttpRequestResponse req: action_login){
				Element temp = document.createElement("req");
				Attr attr = document.createAttribute("id");
				attr.setValue(Integer.toString(ordine));
				temp.setAttributeNode(attr);
				Sout.println(helper.bytesToString((req.getRequest())));
				temp.appendChild(document.createTextNode(helper.base64Encode(req.getRequest())));
				elementRequests.appendChild(temp);
				//elementRequest.add(temp);
				ordine++;
			}
			
			TransformerFactory trasformerFactory = TransformerFactory.newInstance();
			Transformer trasformer = trasformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			XMLFile = new File(nomeFile);
			Sout.println(XMLFile.getAbsolutePath());
			StreamResult streamResult = new StreamResult(XMLFile);
			trasformer.transform(domSource, streamResult);
		}catch(ParserConfigurationException pce){
			pce.printStackTrace();
		}catch(TransformerException tce){
			tce.printStackTrace();
		}
	}
	
	public File getXMLFile(){
		return this.XMLFile;
	}
}
