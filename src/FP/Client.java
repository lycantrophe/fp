/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.ntnu.fp.net.co.Connection;
import no.ntnu.fp.net.co.ConnectionImpl;

/**
 *
 * @author lycantrophe
 */
public class Client {

    private static Connection connection;
    private static Login login;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        connection = new ConnectionImpl(4001);
        InetAddress addr;

        try {
            addr = InetAddress.getLocalHost();
            connection.connect(addr, 5555);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        login = new Login(connection);
        login.pack();
        login.setLocationRelativeTo(null);
        login.setVisible(true);
    }

    public static void loginSuccessful() throws IOException, ClassNotFoundException {

        System.out.println("Login was successful! This from loginSuccessful");
        CalendarWindow calwin = new CalendarWindow(connection);
        calwin.pack();
        calwin.setVisible(true);
        calwin.setLocationRelativeTo(null);
        login.dispose();
    }
}