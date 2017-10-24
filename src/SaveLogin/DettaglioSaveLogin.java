package SaveLogin;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import burp.IHttpRequestResponse;

public class DettaglioSaveLogin extends JFrame {

	private JPanel panel;
	private LoginXmlWorker loginXML;
	private List<IHttpRequestResponse> action_list;
	private Login login;
	
	public DettaglioSaveLogin(final int indice, final JPanel panel, final LoginXmlWorker loginXML, final List<IHttpRequestResponse> action_list, final DefaultListModel lmodelLogin, final List<Login> listLogin) {
		this.panel = panel;
		this.loginXML = loginXML;
		this.action_list = action_list;
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSED));
		this.setSize(new Dimension(600,400));
		this.setLocationRelativeTo(null);
		Container dettagliContainer = this.getContentPane();
		JPanel dettagliPanel = new JPanel();
		dettagliContainer.add(dettagliPanel);
		GridBagConstraints dettagliLay = new GridBagConstraints();
		dettagliPanel.setLayout(new GridBagLayout());

		JLabel dettaglioCodiceFiscaleLb = new JLabel("Codice Fiscale : ");
		dettagliLay.weightx = 0.5;
		dettagliLay.fill = GridBagConstraints.HORIZONTAL;
		dettagliLay.gridx = 0;
		dettagliLay.gridy = 0;
		dettagliLay.insets = new Insets(10, 10, 10, 10);
		dettagliPanel.add(dettaglioCodiceFiscaleLb, dettagliLay);

		final JTextField dettaglioCodiceFiscaleTx = new JTextField();
		dettagliLay.weightx = 0.5;
		dettagliLay.fill = GridBagConstraints.HORIZONTAL;
		dettagliLay.gridx = 1;
		dettagliLay.gridy = 0;
		dettagliLay.insets = new Insets(10, 10, 10, 10);
		dettagliPanel.add(dettaglioCodiceFiscaleTx, dettagliLay);

		JLabel dettaglioPasswordLb = new JLabel("Password : ");
		dettagliLay.weightx = 0.5;
		dettagliLay.fill = GridBagConstraints.HORIZONTAL;
		dettagliLay.gridx = 0;
		dettagliLay.gridy = 1;
		dettagliLay.insets = new Insets(10, 10, 10, 10);
		dettagliPanel.add(dettaglioPasswordLb, dettagliLay);

		final JTextField dettaglioPasswordTx = new JTextField();
		dettagliLay.weightx = 0.5;
		dettagliLay.fill = GridBagConstraints.HORIZONTAL;
		dettagliLay.gridx = 1;
		dettagliLay.gridy = 1;
		dettagliLay.insets = new Insets(10, 10, 10, 10);
		dettagliPanel.add(dettaglioPasswordTx, dettagliLay);

		JLabel dettaglioTipologiaLb = new JLabel("Tipologia : ");
		dettagliLay.weightx = 0.5;
		dettagliLay.fill = GridBagConstraints.HORIZONTAL;
		dettagliLay.gridx = 0;
		dettagliLay.gridy = 2;
		dettagliLay.insets = new Insets(10, 10, 10, 10);
		dettagliPanel.add(dettaglioTipologiaLb, dettagliLay);

		final JTextField dettaglioTipologiaTx = new JTextField();
		dettagliLay.weightx = 0.5;
		dettagliLay.fill = GridBagConstraints.HORIZONTAL;
		dettagliLay.gridx = 1;
		dettagliLay.gridy = 2;
		dettagliLay.insets = new Insets(10, 10, 10, 10);
		dettagliPanel.add(dettaglioTipologiaTx, dettagliLay);

		JButton dettaglioAnnulla = new JButton("Annulla");
		dettaglioAnnulla.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dettaglioCodiceFiscaleTx.setText("");
				dettaglioPasswordTx.setText("");
				dettaglioTipologiaTx.setText("");
			}
		});
		dettagliLay.weightx = 0.5;
		dettagliLay.fill = GridBagConstraints.HORIZONTAL;
		dettagliLay.gridx = 0;
		dettagliLay.gridy = 3;
		dettagliLay.insets = new Insets(10, 10, 10, 10);
		dettagliPanel.add(dettaglioAnnulla, dettagliLay);
		
		JButton dettaglioConferma = new JButton("Conferma");
		dettaglioConferma.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				//TODO 
				if(dettaglioCodiceFiscaleTx.getText().equals("") || dettaglioPasswordTx.getText().equals("") || dettaglioTipologiaTx.getText().equals("")){
					JOptionPane.showMessageDialog(panel, "valorizzare tutti i campi");
				}else{
					//inserimento nel file xml
					//trasformo IHttpRe... in HttpRe...
					List<IHttpRequestResponse> listReq = new ArrayList<IHttpRequestResponse>();
					for(IHttpRequestResponse action: action_list) {
						listReq.add(action);
					}
					login = new Login(indice,dettaglioCodiceFiscaleTx.getText(), dettaglioPasswordTx.getText(), dettaglioTipologiaTx.getText(), listReq);
					loginXML.insertTagXMLApi(login);
					listLogin.add(login);
					lmodelLogin.addElement(new String(dettaglioCodiceFiscaleTx.getText()+" "+dettaglioPasswordTx.getText()+" "+dettaglioTipologiaTx.getText()));
				}
				dispose();
			}	
		});
		dettagliLay.weightx = 0.5;
		dettagliLay.fill = GridBagConstraints.HORIZONTAL;
		dettagliLay.gridx = 1;
		dettagliLay.gridy = 3;
		dettagliLay.insets = new Insets(10, 10, 10, 10);
		dettagliPanel.add(dettaglioConferma, dettagliLay);
		
		JLabel msgDettaglio = new JLabel("Controllare bene i campi inseriti");	//utilizzato per i messaggi di errore durante l'inserimento
		//dettagliLay.weightx = 0.5;
		dettagliLay.fill = GridBagConstraints.CENTER;
		//dettagliLay.gridx = 0;
		dettagliLay.gridy = 4;
		dettagliLay.insets = new Insets(10, 10, 10, 10);
		dettagliPanel.add(msgDettaglio, dettagliLay);
	}
	
	public Login getLogin() {
		return this.login;
	}
}
