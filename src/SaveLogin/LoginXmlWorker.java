package SaveLogin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import burp.IBurpExtenderCallbacks;
import burp.IExtensionHelpers;
import burp.IHttpRequestResponse;

public class LoginXmlWorker extends SwingWorker<List<Login>, Void> implements MyXmlInterface {

	private String nomeFile;
	private File fileXml;
	private IBurpExtenderCallbacks callbacks;
	private PrintWriter Sout;
	private List<Login> listLogin;
	private JPanel panel;
	private IExtensionHelpers helper;
	private int operazioneXML = 0;
	private Login loginXML;

	public LoginXmlWorker(String nomeFile, IBurpExtenderCallbacks callbacks, PrintWriter Sout, IExtensionHelpers helper,
			JPanel panel) {
		this.nomeFile = nomeFile;
		this.callbacks = callbacks;
		this.Sout = Sout;
		this.helper = helper;
		this.panel = panel;
		fileXml = new File(nomeFile);
	}

	protected List<Login> loadXML() {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = saxParserFactory.newSAXParser();
			MyHandler handler = new MyHandler(listLogin, helper, Sout);
			saxParser.parse(fileXml, handler);
			listLogin = handler.getListLogin();
			for (Login l : listLogin) {
				Sout.println("Codice Fiscale : " + l.getCodiceFiscale() + "  Password : " + l.getPassword()
						+ " Tipologia : " + l.getTipologia());
			}
		} catch (ParserConfigurationException e) {
			Sout.println(e.getMessage());
		} catch (SAXException e) {
			Sout.println(e.getMessage());
		} catch (IOException e) {
			Sout.println(e.getMessage());
		}
		return listLogin;
	}

	protected void insertTagXML(Login login) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			org.w3c.dom.Document document;
			Element roots;
			Element root;
			if (fileXml.exists()) {
				document = builder.parse(fileXml);
				root = ((org.w3c.dom.Document) document).createElement("Login");
				// ((Node) document).appendChild(root);
			} else {
				document = builder.newDocument();

				roots = document.createElement("Logins");
				document.appendChild(roots);

				root = document.createElement("Login");
				roots.appendChild(root);
			}
			Sout.println("percorso File: " + fileXml.getAbsolutePath());

			Element elementId = ((org.w3c.dom.Document) document).createElement("id");
			elementId.appendChild(((org.w3c.dom.Document) document).createTextNode(Integer.toString(login.getId())));
			root.appendChild(elementId);

			Element elementCodiceFiscale = ((org.w3c.dom.Document) document).createElement("codiceFiscale");
			elementCodiceFiscale
					.appendChild(((org.w3c.dom.Document) document).createTextNode(login.getCodiceFiscale()));
			root.appendChild(elementCodiceFiscale);

			Element elementPassword = ((org.w3c.dom.Document) document).createElement("password");
			elementPassword.appendChild(((org.w3c.dom.Document) document).createTextNode(login.getPassword()));
			root.appendChild(elementPassword);

			Element elementTipologia = ((org.w3c.dom.Document) document).createElement("tipologia");
			elementTipologia.appendChild(((org.w3c.dom.Document) document).createTextNode(login.getTipologia()));
			root.appendChild(elementTipologia);

			Element elementRequests = ((org.w3c.dom.Document) document).createElement("requests");
			root.appendChild(elementRequests);

			int ordine = 0;
			Sout.println("dimensione actionlogin:   " + login.getAction_Login().size());
			for (IHttpRequestResponse req : login.getAction_Login()) {
				Element temp = ((org.w3c.dom.Document) document).createElement("req");
				Attr attr = ((org.w3c.dom.Document) document).createAttribute("id");
				attr.setValue(Integer.toString(ordine));
				temp.setAttributeNode(attr);

				// inserisco tutta la richiesta all'interno del tag
				HttpRequestResponse objSerialization = new HttpRequestResponse(req);
				byte[] tempStringSerialization = MySerialization.objToBytes(objSerialization);
				Sout.println(
						"TEST Serializable----------------" + tempStringSerialization.toString() + "-------------111");

				HttpRequestResponse objderialization = (HttpRequestResponse) MySerialization
						.bytesToObj(tempStringSerialization);
				Sout.println("TEST Deserializable----------------" + objderialization.getHttpService().getHost()
						+ "-------------222");

				temp.appendChild(
						((org.w3c.dom.Document) document).createTextNode(helper.base64Encode(tempStringSerialization)));
				elementRequests.appendChild(temp);
				ordine++;
			}

			if (fileXml.exists()) {
				NodeList nodes = document.getElementsByTagName("Login");
				nodes.item(0).getParentNode().appendChild(root);
			} else {
				fileXml = new File(nomeFile);
				Sout.println("percorso File: " + fileXml.getAbsolutePath());
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(fileXml);
			transformer.transform(source, result);
			Sout.println("--Inserimento fatto con successo-- " + nomeFile + " " + login.getId());
		} catch (Exception e) {
			Sout.println(e.getMessage());
		}
	}

	protected void deleteTagXML(Login login) {
		int id = login.getId();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			org.w3c.dom.Document document = builder.parse(fileXml);

			NodeList nodes = document.getElementsByTagName("Login");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element elementLogin = (Element) nodes.item(i);
				Element elementId = (Element) elementLogin.getElementsByTagName("id").item(0);
				Sout.println("DELETE LOGIN : " + nodes.getLength() + elementId.getTextContent());
				int pId = Integer.parseInt(elementId.getTextContent());
				if (pId == id) {
					elementLogin.getParentNode().removeChild(elementLogin);
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(fileXml);
			transformer.transform(source, result);
		} catch (Exception e) {
			Sout.println(e.getMessage());
		}
	}

	protected void changeTagXML(Login login) {
		int id = login.getId();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			org.w3c.dom.Document document = builder.parse(fileXml);

			NodeList nodes = document.getElementsByTagName("Login");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element elementLogin = (Element) nodes.item(i);
				Element elementId = (Element) elementLogin.getElementsByTagName("id").item(0);
				int pId = Integer.parseInt(elementId.getTagName());
				if (pId == id) {
					// elementLogin.getParentNode().removeChild(elementLogin);
					Element elementCodiceFiscale = (Element) elementLogin.getElementsByTagName("codiceFiscale").item(0);
					elementCodiceFiscale.setTextContent(login.getCodiceFiscale());

					Element elementPassword = (Element) elementLogin.getElementsByTagName("password").item(0);
					elementPassword.setTextContent(login.getPassword());

					Element elementTipologia = (Element) elementLogin.getElementsByTagName("tipologia").item(0);
					elementTipologia.setTextContent(login.getTipologia());
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(fileXml);
			transformer.transform(source, result);
		} catch (Exception e) {
			Sout.println(e.getMessage());
		}
	}

	public synchronized List<Login> loadXMLApi() {
		this.operazioneXML = 0;
		// this.execute();
		this.listLogin = loadXML();
		return listLogin;
	}

	public synchronized void insertTagXMLApi(Login login) {
		this.operazioneXML = 1;
		this.loginXML = login;
		this.execute();
	}

	public synchronized void deleteTagXMLApi(Login login) {
		this.operazioneXML = 2;
		this.loginXML = login;
		this.execute();
	}

	public synchronized void changeTagXMLApi(Login login) {
		this.operazioneXML = 3;
		this.loginXML = login;
		this.execute();
	}

	@Override
	protected List<Login> doInBackground() throws Exception {
		if (operazioneXML == 0) {
			this.listLogin = loadXML();
		} else if (operazioneXML == 1) {
			insertTagXML(loginXML);
		} else if (operazioneXML == 2) {
			deleteTagXML(loginXML);
		} else if (operazioneXML == 3) {
			changeTagXML(loginXML);
		}
		return listLogin;
	}

}
