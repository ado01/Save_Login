
package burp;

import java.io.PrintWriter;
import javax.swing.SwingUtilities;

import SaveLogin.SaveLogin;

/**
 *
 * @author xyzt
 */
public class BurpExtender implements IBurpExtender{

    private PrintWriter Sout;
    private PrintWriter Serr;
    private SaveLogin rec;
    
    public void registerExtenderCallbacks(final IBurpExtenderCallbacks callbacks) {
        
        Sout = new PrintWriter(callbacks.getStdout(), true);
        Serr = new PrintWriter(callbacks.getStderr(), true);
        
        SwingUtilities.invokeLater(new Runnable (){
            public void run() {
                rec = new SaveLogin(callbacks, Sout);
                callbacks.registerHttpListener(rec);
                callbacks.addSuiteTab(rec);
            }     
        }); 
        Sout.println("Estensione Ok");
    } 
}
