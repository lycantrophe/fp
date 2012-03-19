/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import no.ntnu.fp.net.admin.Log;
import no.ntnu.fp.net.co.Connection;

/**
 *
 * @author lycantrophe
 */
public class ServerThread extends Thread {
    
    private Connection connection;
    
    public ServerThread( Connection connection ){
        this.connection = connection;
    }
    
    // handle exceptions
    public void run( ) throws ConnectException, IOException {
        Query query = new Query();
            try {
                String uname = connection.receive();
                String pw = connection.receive();
                if (query.authorize(uname, pw)) {
                    query.close();
                    connection.send("Login successful");
                    User user = new User(uname);
                    user.bind(connection);
                    // Sends "me" and (hopefully) all structures connected to "me"
                    connection.send(Server.Serialize(user.initialSend()));
                    String cmd;
                    while ((cmd = connection.receive()) != null) {
                        // Exit if receives end call
                        if (cmd.equalsIgnoreCase("exit")) {
                            break;
                        }
                        // Input should be in the form: command :: arg1 :: arg2 :: arg3 
                        String[] opts = cmd.split("::");
                        if (opts[0].equalsIgnoreCase("delete")) {

                            if (user.deleteAppointment(opts[1])) {
                                // takes: delete :: [id]
                                // TODO: Validate input
                                connection.send("delete ACK");
                            } else {
                                connection.send("delete FAIL");
                            }

                        } else if (opts[0].equalsIgnoreCase("decline")) {

                            if (user.declineAppointment(opts[1])) {

                                // takes: decline :: [id]
                                connection.send("decline ACK");
                            } else {
                                connection.send("decline FAIL");
                            }
                        } else if (opts[0].equalsIgnoreCase("edit")) {
                            // parse json
                            // TODO: validate input
                            //user.editAppointment(null, null, null, null, cmd, null, null, null); )
                        }
                        /*
                         * do things
                         */
                    }

                    // Send notifications

                } else {
                    // TODO: Wrong login handling.
                    connection.send("Try again!");
                }


            } catch (EOFException e) {
                query.close();
                Log.writeToLog("Got close request (EOFException), closing.",
                        "TestServer");
                connection.close();
            }
    }
}
