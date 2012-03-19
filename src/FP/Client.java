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
        Login login = new Login(connection);
    }

    public void loginSuccessful() throws IOException, ClassNotFoundException {
        //me = (Person) Server.Deserialize(connection.receive());
        CalendarWindow calwin = new CalendarWindow(connection);
    }
}