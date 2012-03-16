/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import no.ntnu.fp.net.co.Connection;
import no.ntnu.fp.net.co.ConnectionImpl;

/**
 *
 * @author lycantrophe
 */
public class Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Connection connection = new ConnectionImpl(4001);
        InetAddress addr;

        try {
            addr = InetAddress.getLocalHost();
            connection.connect(addr, 5555);

            boolean loginSuccess = false;
            while (!loginSuccess) {
                String lg = connection.send(login());
                if (lg.equals("Login successful")) {
                    loginSuccess = true;
                }
            }
            connection.receive();

            /*
             * Build initial connection data
             */

            /*
             * get messages
             */

        } catch (ConnectException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
