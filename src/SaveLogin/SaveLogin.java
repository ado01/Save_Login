
package SaveLogin;

import burp.IBurpExtenderCallbacks;
import burp.ICookie;
import burp.IExtensionHelpers;
import burp.IHttpListener;
import burp.IHttpRequestResponse;
import burp.IParameter;
import burp.ITab;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

/**
 *
 * @author xyzt
 */
public class SaveLogin implements ITab, IHttpListener {

	private IBurpExtenderCallbacks callbacks;
	private JPanel Rec;
	private String login;
	private int on_off = 0; 
	private JList<String> listview;
	private DefaultListModel lmodel;
	private List<IHttpRequestResponse> action_login;
	private List<Login> listLogin;
	private PrintWriter Sout;
	private IExtensionHelpers helper;
	private JLabel requestlabelview;
	private MyLabel responselabelview;
	private MyProgressBar proBar;
	private int select = 0;
	private int loginSelezionato = 0;
	private IHttpRequestResponse lastresponse;
	private java.util.HashMap<String, String> hashcookie;
	private MyButton inizio_login = new MyButton("/Image/play.png", "/Image/play_down.png", "/Image/play_down.png", 0,0);
	private MyButton fine_login = new MyButton("/Image/stop.png", "/Image/stop_down.png", "/Image/stop_down.png", 0, 0);
	private MyButton reset_login = new MyButton("/Image/reset.png", "/Image/reset_down.png", "/Image/reset_down.png", 0,0);
	private JButton go = new JButton("go");
	private JButton buttonOnOff = new JButton("On");
	private JButton save = new JButton("save");
	private JButton load = new JButton("load");

	private String nomeFile = "test.xml";
	private DefaultListModel lmodelLogin;
	private JList<String> listviewLogin;
	private JLabel requestlabelviewLogin;
	
	private int IDlogin=0;

	public SaveLogin(IBurpExtenderCallbacks callbacks, PrintWriter Sout) {

		this.Sout = Sout;
		this.callbacks = callbacks;
		this.login = "off";
		this.action_login = new ArrayList<IHttpRequestResponse>();
		this.helper = callbacks.getHelpers();
		this.hashcookie = new java.util.HashMap<String, String>();
		this.listLogin = new ArrayList<Login>();

		go.setEnabled(false);
		fine_login.setEnabled(false);
		inizio_login.setEnabled(false);
		save.setEnabled(false);
		load.setEnabled(false);
	}

	public String getTabCaption() {
		return "Save Login";
	}

	public Component getUiComponent() {

		Rec = new JPanel();

		GridBagConstraints lay = new GridBagConstraints();

		Rec.setLayout(new GridBagLayout());

		// JButton inizio_login = new JButton("inizio_login");
		inizio_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login = "on";
				fine_login.setEnabled(true);
				go.setEnabled(false);
				
				lmodel.removeAllElements();
				listview.repaint();
				action_login.clear();
				requestlabelview.setText("");
				requestlabelview.repaint();
				responselabelview.setText("");
				responselabelview.repaint();
				//hashcookie.clear();
				go.setEnabled(false);
				load.setEnabled(false);
				
				Sout.println(login);
			}
		});
		lay.weightx = 0.5;
		lay.fill = GridBagConstraints.BOTH;
		lay.gridx = 0;
		lay.gridy = 0;
		lay.insets = new Insets(10, 10, 10, 10);
		Rec.add(inizio_login, lay);

		// JButton fine_login = new JButton("fine_login");
		fine_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login = "off";
				go.setEnabled(true);
				inizio_login.setEnabled(true);
				save.setEnabled(true);
				Sout.println(login);
			}
		});
		lay.weightx = 0.5;
		lay.fill = GridBagConstraints.BOTH;
		lay.gridx = 1;
		lay.gridy = 0;
		lay.insets = new Insets(10, 10, 10, 10);
		Rec.add(fine_login, lay);

		// JButton reset_login = new JButton("reset_login");
		reset_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login = "reset";
				lmodel.removeAllElements();
				listview.repaint();
				action_login.clear();
				requestlabelview.setText("");
				requestlabelview.repaint();
				responselabelview.setText("");
				responselabelview.repaint();
				hashcookie.clear();
				go.setEnabled(false);
				fine_login.setEnabled(false);
			}
		});
		lay.weightx = 0.5;
		lay.fill = GridBagConstraints.BOTH;
		lay.gridx = 2;
		lay.gridy = 0;
		lay.insets = new Insets(10, 10, 10, 10);
		Rec.add(reset_login, lay);

		lmodel = new DefaultListModel();
		listview = new JList(lmodel);
		listview.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting()) {
					select = listview.getSelectedIndex();
					String param = "";
					action_login.get(select);
					for (IParameter s : helper.analyzeRequest(action_login.get(select)).getParameters()) {
						param = param + "<span>" + s.getName() + "       :        " + helper.urlDecode(s.getValue())+ "</span><br>";
					}
					requestlabelview.setText("<html>" + param + "</html>");
					for (byte b : action_login.get(select).getRequest()) {
						Sout.write(Byte.toString(b));
					}
				}
			}
		});

		JScrollPane scroll = new JScrollPane(listview);
		scroll.setPreferredSize(new Dimension(800, 200));
		listview.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listview.setSelectedIndex(0);
		lay.weightx = 1.0;
		// lay.fill = GridBagConstraints.BOTH;
		lay.gridx = 0;
		lay.gridy = 1;
		lay.insets = new Insets(10, 10, 10, 10);
		lay.gridheight = 3;
		lay.gridwidth = 3;
		Rec.add(scroll, lay);

		//Thread degli inserimenti, eliminazioni e modifiche all'interno del XML (utilizzo i metodi di questo oggetto per gli inserimenti ecc) 
		final LoginXmlWorker loginXML = new LoginXmlWorker(nomeFile, callbacks, Sout, helper, Rec);

		// List<Login> logins = loginXML.loadXMLApi();	//questo metodo non è utilizabile perchè non funzionante
		lmodelLogin = new DefaultListModel();
		listviewLogin = new JList(lmodelLogin);
		listviewLogin.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting()) {
					loginSelezionato = listviewLogin.getSelectedIndex();	//setto il login che voglio caricare
					String param = "";
					//listLogin.get(loginSelezionato);
					for (Login s : listLogin) {
						param = param + "<span>" + s.getCodiceFiscale() + "       :        " + s.getPassword()
								+ "		:		" + s.getTipologia() + "</span><br>";
					}
					requestlabelviewLogin.setText("<html>" + param + "</html>");
					Sout.println(param);
				}
			}
		});
		
		Sout.println("Numero di Login presenti : "+listLogin.size());
		JScrollPane scrollLogin = new JScrollPane(listviewLogin);
		scrollLogin.setPreferredSize(new Dimension(400, 200));
		listviewLogin.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listviewLogin.setSelectedIndex(0);
		lay.weightx = 0.5;
		lay.weighty = 0.5;
		// lay.fill = GridBagConstraints.BOTH;
		lay.gridx = 0;
		lay.gridy = 4;
		lay.insets = new Insets(10, 10, 10, 10);
		lay.gridheight = 3;
		lay.gridwidth = 1;
		Rec.add(scrollLogin, lay);

		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//resetto tutti i valori
				lmodel.removeAllElements();
				listview.repaint();
				action_login.clear();
				//carico le richieste nell'ArrayList actin_login  e  ricarico le richieste nel Jlist
				Login REQloadLogin = listLogin.get(loginSelezionato);
				Sout.println("ID LOGIN SELEZIONATO"+loginSelezionato + "  " +listLogin.size());
				List<IHttpRequestResponse> loadReqResLogin = REQloadLogin.getAction_Login();
				Sout.println("LOGIN SELEZIONATO : " + loginSelezionato + " " + REQloadLogin.getId() +" "+ REQloadLogin.getCodiceFiscale() + " "+loadReqResLogin.size());
				//List<IHttpRequestResponse> temp_action_login = new ArrayList<IHttpRequestResponse>();
				for(IHttpRequestResponse tempReqResLoad: loadReqResLogin) {
					//IHttpRequestResponse tempre = (IHttpRequestResponse)tempReqResLoad;
					//Sout.println(helper.base64Encode(tempReqResLoad.getRequest()));
					byte[] bytesReqAll= tempReqResLoad.getRequest();
					String stringReqAll = new String(bytesReqAll,StandardCharsets.UTF_8);	//converto l'array di byte in stringhe utilizando una codifica UTF8
					String[] stringReqURLLine = stringReqAll.split("\n");
					String[] stringReqURL = stringReqURLLine[0].split(" ");
					Sout.println("test per verificare la richiesta:    "+stringReqURL[1]);
					Sout.println(tempReqResLoad.getHttpService().getHost());
					action_login.add(tempReqResLoad);
					lmodel.addElement(tempReqResLoad.getHttpService().getProtocol()+"://"+tempReqResLoad.getHttpService().getHost()+stringReqURL[1]);
				}
				Sout.println("ACTION_LOGIN LOAD DONE: "+ action_login.size());
				go.setEnabled(true);
			}	
		});
		lay.weightx = 0.1;
		lay.weighty = 0.1;
		lay.fill = GridBagConstraints.HORIZONTAL;
		lay.gridx = 1;
		lay.gridy = 4;
		lay.insets = new Insets(10, 10, 10, 10);
		Rec.add(load, lay);
		
		responselabelview = new MyLabel(this);
		responselabelview.setVerticalAlignment(SwingConstants.NORTH);
		responselabelview.setHorizontalAlignment(SwingConstants.LEFT);
		JScrollPane scrolllabelresponse = new JScrollPane(responselabelview);
		scrolllabelresponse.setPreferredSize(new Dimension(200, 300));
		lay.weightx = 0.5;
		lay.weighty = 0.5;
		lay.fill = GridBagConstraints.BOTH;
		lay.gridx = 2;
		lay.gridy = 4;
		lay.insets = new Insets(10, 10, 10, 10);
		lay.gridheight = 3;
		lay.gridwidth = 1;
		Rec.add(scrolllabelresponse, lay);

		// MyButton go = new
		// MyButton("/Image/play.png","/Image/play_down.png","",0,0);
		go.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inizio_login.setEnabled(false);
				fine_login.setEnabled(false);
				reset_login.setEnabled(false);
				
				synchronized (hashcookie) {
					RequestWorker loginWorker = new RequestWorker(callbacks, Sout, action_login, hashcookie, Rec,responselabelview);
					loginWorker.addLabelEnvListener(responselabelview);
					loginWorker.addProgressEvnListener(proBar);
					proBar.setValue(0);
					loginWorker.execute();
				}
				inizio_login.setEnabled(true);
				fine_login.setEnabled(true);
				reset_login.setEnabled(true);

				Sout.println("fine login");
			}
		});
		lay.weightx = 1;
		lay.fill = GridBagConstraints.HORIZONTAL;
		lay.gridx = 3;
		lay.gridy = 3;
		lay.insets = new Insets(10, 10, 10, 10);
		Rec.add(go, lay);

		proBar = new MyProgressBar();
		proBar.setValue(0);
		lay.weightx = 1;
		lay.fill = GridBagConstraints.HORIZONTAL;
		lay.gridx = 3;
		lay.gridy = 4;
		lay.insets = new Insets(10, 10, 10, 10);
		Rec.add(proBar, lay);

		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// apro un frame per il salvataggio del login
				int indiceLogin;
				if(IDlogin == 0) {
					indiceLogin=0;
				}else {
					indiceLogin = IDlogin+1; 
				}
				DettaglioSaveLogin createNewLogin = new DettaglioSaveLogin(indiceLogin, Rec, loginXML, action_login, lmodelLogin, listLogin);
				createNewLogin.setVisible(true);
				//Sout.println("<---------->"+createNewLogin.getLogin().getCodiceFiscale());
				//listLogin.add(createNewLogin.getLogin());
				IDlogin++;
				// CreateXMLFile xml = new CreateXMLFile("test.xml", 1,
				// "mtacrl50c10f839x", "password", "cittadino",
				// action_login, Sout, helper);

				/*SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
				try {
					SAXParser saxParser = saxParserFactory.newSAXParser();
					MyHandler handler = new MyHandler(listLogin, helper, Sout);
					saxParser.parse(new File("test.xml"), handler);
					listLogin = handler.getListLogin();
					Sout.println("Number of Login : " + listLogin.size());
					for (Login l : listLogin) {
						Sout.println("Codice Fiscale : " + l.getCodiceFiscale() + "  Password : " + l.getPassword()
								+ " Tipologia : " + l.getTipologia());
					}
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}*/
			}
		});
		lay.weightx = 1;
		lay.fill = GridBagConstraints.HORIZONTAL;
		lay.gridx = 3;
		lay.gridy = 1;
		lay.insets = new Insets(10, 10, 10, 10);
		Rec.add(save, lay);

		buttonOnOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (on_off == 1) {
					on_off = 0;
					buttonOnOff.setText("On");
					inizio_login.setEnabled(false);
					fine_login.setEnabled(false);
					reset_login.setEnabled(false);
					go.setEnabled(false);
					save.setEnabled(false);
					load.setEnabled(false);
				} else {
					on_off = 1;
					buttonOnOff.setText("Off");
					inizio_login.setEnabled(true);
					reset_login.setEnabled(true);
					load.setEnabled(true);
				}
			}
		});
		lay.weightx = 1;
		lay.fill = GridBagConstraints.HORIZONTAL;
		lay.gridx = 3;
		lay.gridy = 0;
		lay.insets = new Insets(10, 10, 10, 10);
		Rec.add(buttonOnOff, lay);

		loadXmlFromFile();
		
		return Rec;
	}

	public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo) {
		if (on_off == 1) {
			if (login.equals("on")) {
				if (messageIsRequest) {// è una richiesta
					action_login.add(messageInfo);
					lmodel.addElement(helper.analyzeRequest(messageInfo).getUrl().getHost()
							+ helper.urlDecode(helper.analyzeRequest(messageInfo).getUrl().getFile()));
				} else {// è una risposta
					for (ICookie lc : helper.analyzeResponse(messageInfo.getResponse()).getCookies()) {
						boolean trovato = false;
						Iterator it = hashcookie.entrySet().iterator();
						while (it.hasNext()) {
							Map.Entry pair = (Map.Entry<String, String>) it.next();
							Sout.println("Chiave:" + pair.getKey() + " Valore:" + pair.getValue());
							if (lc.getName().equals((String) pair.getKey())) {
								pair.setValue(lc.getValue());
								trovato = true;
							}
						}
						if (!trovato) {
							hashcookie.put(lc.getName(), lc.getValue());
						}
					}
				}
			} else if (login.equals("off")) {
				// TODO non sto registrando
				// action_login_response.add(messageInfo);
			}

			if (toolFlag == callbacks.TOOL_PROXY || toolFlag == callbacks.TOOL_REPEATER) {
				if (toolFlag == callbacks.TOOL_PROXY) {
					Sout.println("----------------Provieni da PROXY-----------------");
				} else {
					Sout.println("----------------Provieni da REPEATER--------------");
				}
				if (messageIsRequest) {
					Iterator it = hashcookie.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry pair = (Map.Entry<String, String>) it.next();
						boolean cookiePresente = false;
						for (IParameter par : helper.analyzeRequest(messageInfo).getParameters()) {
							if (par.getName().equals(pair.getKey())) {
								IParameter newParam = helper.buildParameter((String) pair.getKey(),
										(String) pair.getValue(), IParameter.PARAM_COOKIE);
								messageInfo.setRequest(helper.updateParameter(messageInfo.getRequest(), newParam));
								cookiePresente = true;
							}
						}
						if (!cookiePresente) {
							IParameter newParam = helper.buildParameter((String) pair.getKey(),
									(String) pair.getValue(), IParameter.PARAM_COOKIE);
							messageInfo.setRequest(helper.addParameter(messageInfo.getRequest(), newParam));
						}
					}
					// stampa per verifica
					for (IParameter par : helper.analyzeRequest(messageInfo).getParameters()) {
						Sout.println("Nome Parametro:" + par.getName() + "Valore Parametro: " + par.getValue());
					}
				}
			} else if (toolFlag == callbacks.TOOL_SCANNER && messageIsRequest) {
				Sout.println("----------------Provieni da SCANNER---------------");
				if (messageIsRequest) {
					synchronized (hashcookie) {
						Iterator it = hashcookie.entrySet().iterator();
						while (it.hasNext()) {
							Map.Entry pair = (Map.Entry<String, String>) it.next();
							boolean cookiePresente = false;
							for (IParameter par : helper.analyzeRequest(messageInfo).getParameters()) {
								if (par.getName().equals(pair.getKey())) {
									IParameter newParam = helper.buildParameter((String) pair.getKey(),
											(String) pair.getValue(), IParameter.PARAM_COOKIE);
									messageInfo.setRequest(helper.updateParameter(messageInfo.getRequest(), newParam));
									cookiePresente = true;
								}
							}
							if (!cookiePresente) {
								IParameter newParam = helper.buildParameter((String) pair.getValue(),
										(String) pair.getKey(), IParameter.PARAM_COOKIE);
								messageInfo.setRequest(helper.addParameter(messageInfo.getRequest(), newParam));
							}
						}
						// stampa per verifica
						for (IParameter par : helper.analyzeRequest(messageInfo).getParameters()) {
							Sout.println("Nome Parametro:" + par.getName() + "Valore Parametro: " + par.getValue());
						}
					}
				}
			} else if (toolFlag == callbacks.TOOL_INTRUDER) {
				Sout.println("----------------Provieni da INTRUDER--------------");
			}
		} else {
			// TODO in caso in cui l'estenzione è spenta (posso anche non fare nulla)
		}
	}

	public void loadXmlFromFile() { // utilizzato nel load iniziale da file
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = saxParserFactory.newSAXParser();
			MyHandler handler = new MyHandler(listLogin, helper, Sout);
			saxParser.parse(new File("test.xml"), handler);
			listLogin = handler.getListLogin();
			for(Login lo: listLogin){
				lmodelLogin.addElement(new String(lo.getCodiceFiscale()+" "+lo.getPassword()+" "+lo.getTipologia()));
				if(IDlogin < lo.getId()) {
					IDlogin = lo.getId(); //quando avrò parsato tutto l'xml IDlogin avrà il valore più grande (dovrò quindi incrementarlo di 1)
				}
				
				Sout.println(new String(lo.getCodiceFiscale()+" "+lo.getPassword()+" "+lo.getTipologia()));
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
