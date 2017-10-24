/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SaveLogin;

import javax.swing.JLabel;

/**
 *
 * @author xyzt
 */
public class MyLabel extends JLabel implements MyLabelListener {

    private SaveLogin hadeler;
    
    public MyLabel(SaveLogin handler){
        super();
        this.hadeler = hadeler;
    }
    
    public void actionLabel(RequestWorker sorgente, String messaggio) {
        this.setText(messaggio);
    }
    
}
