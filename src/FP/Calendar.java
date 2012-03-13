/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.io.EOFException;
import java.io.IOException;
import no.ntnu.fp.net.admin.Log;
import no.ntnu.fp.net.co.Connection;
import no.ntnu.fp.net.co.ConnectionImpl;

/**
 *
 * @author lycantrophe
 */
public class Calendar {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        // Create log
        Log log = new Log();
        log.setLogName("Server-side application");

        // server connection instance, listen on port 5555
        Connection server = new ConnectionImpl(5555);
        // each new connection lives in its own instance
        Connection conn;
        // TODO: Thread at this point?
        try {
            conn = server.accept();

            try {
                String uname = conn.receive();
                String pw = conn.receive();
                if (authorize(uname, pw)) {
                    User user = new User(uname);
                    
                    // Send notifications
                    
                    // Send calendar info (Serialize data?
                    
                }
                else{
                    // TODO: Wrong login handling.
                    conn.send("Try again!");
                }


            } catch (EOFException e) {
                Log.writeToLog("Got close request (EOFException), closing.",
                        "TestServer");
                conn.close();
            }

            System.out.println("SERVER TEST FINISHED");
            Log.writeToLog("TEST SERVER FINISHED", "TestServer");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean authorize(String username, String pw) {
        SQL.conn("SELECT COUNT(*) FROM users WHERE username=? AND password=?");
        SQL.setString(1, username);
        SQL.setString(2, pw);
        SQL.execute();
        return SQL == 1 ? true : false;
    }
}
